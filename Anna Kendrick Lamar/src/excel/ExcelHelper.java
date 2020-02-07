package excel;

import com.sun.istack.internal.Nullable;
import data.Album;
import data.Constants;
import data.Matchup;
import javafx.scene.control.Alert;
import javafx.stage.FileChooser;
import org.apache.poi.EmptyFileException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.*;

import static org.apache.poi.ss.usermodel.CellType.NUMERIC;
import static org.apache.poi.ss.usermodel.CellType.STRING;

public class ExcelHelper {

    final private static int NUM_HEADER_ROWS_IN_MATCHUPS = 1;


    XSSFWorkbook albumsWorkbook;
    XSSFSheet albumsSheet;
    XSSFSheet sorted;
    Sheet resultsSheet;
    File resultsFile;
    Album[] albums;
    List<Matchup> matchupsList;
    private static int albumCount;
    private static int currentMatchupIndex;
    private static int lastUnsavedResultIndex;

    public ExcelHelper(){
    }

    public boolean isAlbumsFetched() {
        return albums != null;
    }

    public void getAlbumsSpreadsheet() throws IOException {
        File spreadsheet = new File(".\\src\\res\\Albums_2010s.xlsx");

        FileInputStream fis = new FileInputStream(spreadsheet);
        try (XSSFWorkbook myWorkBook = new XSSFWorkbook(fis)) {
            albumsWorkbook = myWorkBook;
            albumsSheet = myWorkBook.getSheetAt(0);
            //unused "sorted" sheet
//            sorted = myWorkBook.getSheetAt(1);
        }

        //top row is info, bottom two are metadata
        albumCount = albumsSheet.getPhysicalNumberOfRows()-3;
        albumCount = 15;
        // todo: fix loading UX which can be long
        albums = new Album[albumCount];

        for(int i = 1; i < albumCount + 1; i++) {
            Row row = albumsSheet.getRow(i);
            albums[i-1] = new Album(
                    getStringFromCellSafe(row.getCell(0)),
                    getStringFromCellSafe(row.getCell(1)),
                    (int)(row.getCell(2).getNumericCellValue()),
                    getScoreStringFromCellSafe(row.getCell(3)),
                    getScoreStringFromCellSafe(row.getCell(4)),
                    getScoreStringFromCellSafe(row.getCell(5)),
                    getScoreStringFromCellSafe(row.getCell(6)),
                    getScoreStringFromCellSafe(row.getCell(7)),
                    row.getCell(8).getStringCellValue());
        }
    }

    private String getStringFromCellSafe(Cell cell) {
        return cell.getCellType().equals(NUMERIC) ? Double.toString(cell.getNumericCellValue()) : cell.getStringCellValue();
    }

    private String getScoreStringFromCellSafe(Cell cell) {
        return cell.getCellType().equals(STRING) ? cell.getStringCellValue() : Double.toString(cell.getNumericCellValue());
    }

    private int getIntFromCellSafe(Cell cell) {
        return (int) cell.getNumericCellValue();
    }

    // return true if created successfully
    public boolean createResultsSpreadsheet(String name) throws IOException {
        if(albums == null) {
            return false;
        }
        maybeCreateResultsSheet(name);
        try
        {
            // Write the output to a file
            FileChooser fileChooser = new FileChooser();
            fileChooser.setInitialFileName(name + "_albums_results.xlsx");
            fileChooser.setTitle("Keep the name [your name]_albums_results");
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Excel Sheet", "*.xlsx"));

            resultsFile = fileChooser.showSaveDialog(null);
            if(resultsFile == null) {
                return false;
            }
            FileOutputStream fos = new FileOutputStream(resultsFile);
            resultsSheet.getWorkbook().write(fos);
            fos.close();
        }
        catch(IOException e)
        {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error while creating results spreadsheet");
            alert.setContentText(e.toString());
            alert.showAndWait();
            return false;
        }
//        resultsSheet.getWorkbook().close();
        currentMatchupIndex = 0;
        lastUnsavedResultIndex = 0;
        return true;
    }

    private void maybeCreateResultsSheet(String name) {
        if(matchupsList != null && resultsSheet != null) {
            return;
        }

        matchupsList = createMatchupsList(name.hashCode());

        Workbook workbook = new XSSFWorkbook();
        resultsSheet = workbook.createSheet(name + "_albums_results");

        //header
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerFont.setFontHeightInPoints((short) 14);
        CellStyle headerCellStyle = workbook.createCellStyle();
        headerCellStyle.setFont(headerFont);

        Row headerRow = resultsSheet.createRow(0);
        headerRow.setRowStyle(headerCellStyle);
        headerRow.createCell(0).setCellValue("Album 1 #");
        headerRow.createCell(1).setCellValue("Album 1 Name + Artist");
        headerRow.createCell(2).setCellValue("Album 2 #");
        headerRow.createCell(3).setCellValue("Album 2 Name + Artist");
        headerRow.createCell(4).setCellValue("Result (-1 is empty, -2 is skip)");

        //content
        for(int i = 0; i < matchupsList.size(); i++) {
            Row row = resultsSheet.createRow(i + NUM_HEADER_ROWS_IN_MATCHUPS);
            Matchup matchup = matchupsList.get(i);
            row.createCell(0).setCellValue(matchup.getAlbum1());
            row.createCell(1).setCellValue(matchup.getAlbum1String());
            row.createCell(2).setCellValue(matchup.getAlbum2());
            row.createCell(3).setCellValue(matchup.getAlbum2String());
            row.createCell(4).setCellValue(Constants.RESULT_EMPTY);
        }
        // Resize all columns to fit the content size
        for(int i = 0; i < 5; i++) {
            resultsSheet.autoSizeColumn(i);
        }
    }

    private List<Matchup> createMatchupsList(long seed) {
        List<Matchup> matchupsList = new ArrayList<>((albumCount * (albumCount-1))/2);
        int i = 0;
        while(i < albumCount) {
            for(int j = i+1; j < albumCount; j++) {
                matchupsList.add(new Matchup(i, albums[i].getName() + ", " + albums[i].getArtist(), j, albums[j].getName() + ", " + albums[j].getArtist()));
            }
            i++;
        }

        Collections.shuffle(matchupsList, new Random(seed));
        return matchupsList;
    }

    public boolean getResultsSpreadsheet(@Nullable File file) {
        if(file == null || !file.isFile()) {
            return false;
        }

        try {
            FileInputStream excelFile = new FileInputStream(file);
            Workbook matchupWorkbook = new XSSFWorkbook(excelFile);
            resultsSheet = matchupWorkbook.getSheetAt(0);

            final int matchupCount = (albumCount * (albumCount-1))/2;
            matchupsList = new ArrayList<>(matchupCount);
            if(matchupCount + NUM_HEADER_ROWS_IN_MATCHUPS != resultsSheet.getPhysicalNumberOfRows()){
                return false;
            }

            for(int i = NUM_HEADER_ROWS_IN_MATCHUPS; i < matchupCount + NUM_HEADER_ROWS_IN_MATCHUPS; i++ ){
                Row row = resultsSheet.getRow(i);
                matchupsList.add(new Matchup(
                        getIntFromCellSafe(row.getCell(0)),
                        getStringFromCellSafe(row.getCell(1)),
                        getIntFromCellSafe(row.getCell(2)),
                        getStringFromCellSafe(row.getCell(3)),
                        getIntFromCellSafe(row.getCell(4))));
                if(getIntFromCellSafe(row.getCell(4)) != Constants.RESULT_EMPTY) {
                    currentMatchupIndex = i;
                    lastUnsavedResultIndex = i;
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } catch (EmptyFileException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public Matchup getNextMatchup() {
        if(currentMatchupIndex >= matchupsList.size()) {
            return null;
        }
        return matchupsList.get(currentMatchupIndex);
    }

    public Album getAlbum(int index) {
        return albums[index];
    }

    public void setResult(int winningAlbumIndex) {
        matchupsList.get(currentMatchupIndex).setResult(winningAlbumIndex);
        currentMatchupIndex++;

        //todo: have a buffer of unsaved progress that user has to manually save. hopefully a concerned dialog in case they try to close without doing so
    }

    public void saveResults() {
        for(int i = lastUnsavedResultIndex; i < currentMatchupIndex; i++) {
            resultsSheet.getRow(i + NUM_HEADER_ROWS_IN_MATCHUPS).getCell(4).setCellValue(matchupsList.get(i).getResult());
        }

        try {
            if(resultsFile == null) {
                FileChooser fileChooser = new FileChooser();
                fileChooser.setInitialFileName("(your_name)_albums_results.xlsx");
                fileChooser.setTitle("Weirdly, we lost the original excel. Keep the name [your name]_albums_results");
                fileChooser.getExtensionFilters().addAll(
                        new FileChooser.ExtensionFilter("Excel Sheet", "*.xlsx"));

                resultsFile = fileChooser.showSaveDialog(null);
            }

            if(resultsFile != null) {
                FileOutputStream outputStream = new FileOutputStream(resultsFile);
                resultsSheet.getWorkbook().write(outputStream);
//                resultsSheet.getWorkbook().close();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error while saving results");
                alert.setContentText("Please save to a valid file");
                alert.showAndWait();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
