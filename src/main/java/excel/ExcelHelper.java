package excel;

import data.Album;
import data.Configs;
import data.Constants;
import data.Matchup;
import javafx.scene.control.Alert;
import javafx.stage.FileChooser;
import org.apache.poi.EmptyFileException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jetbrains.annotations.Nullable;
import ui.CSSHelper;

import java.io.*;
import java.util.*;

import static data.Constants.RESULT_EMPTY;
import static org.apache.poi.ss.usermodel.CellType.NUMERIC;
import static org.apache.poi.ss.usermodel.CellType.STRING;

public class ExcelHelper {

    private final static int NUM_HEADER_ROWS_IN_MATCHUPS = 1;

    XSSFWorkbook albumsWorkbook;
    XSSFSheet albumsSheet;
    XSSFSheet sorted;
    Sheet resultsSheet;
    Sheet metadataSheet;
    File resultsFile;
    Album[] albums;
    List<Matchup> matchupsList;
    private static int albumCount;
    private static int currentMatchupIndex;
    private static int earliestUnsavedResultIndex;
    private static int latestUnsavedResultIndex;
    private static int rngSeed = 0;
    private static boolean hasUnsavedChanges = false;

    public ExcelHelper(){
    }

    public boolean isAlbumsFetched() {
        return albums != null;
    }

    public void getAlbumsSpreadsheet() throws IOException {
        try (XSSFWorkbook myWorkBook = new XSSFWorkbook(getClass().getResourceAsStream("/Albums_2010s.xlsx"))) {
            albumsWorkbook = myWorkBook;
            albumsSheet = myWorkBook.getSheetAt(0);
            //unused "sorted" sheet
//            sorted = myWorkBook.getSheetAt(1);
        }

        //top row is info, bottom two are metadata
        albumCount = albumsSheet.getPhysicalNumberOfRows()-3;
        if(Configs.maxAlbums != null) {
            albumCount = Configs.maxAlbums;
        }
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
    public boolean createResultsSpreadsheetFile(String name) throws IOException {
        if(albums == null) {
            return false;
        }

        if(Constants.USE_COMPACT_EXCEL) {
            maybeCreateCompactResultsSheet(name);
        } else {
            maybeCreateResultsSheet(name);
        }

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
            CSSHelper.maybeApplyCSS(alert.getDialogPane());
            alert.showAndWait();
            return false;
        }
//        resultsSheet.getWorkbook().close();
        currentMatchupIndex = 0;
        earliestUnsavedResultIndex = 0;
        latestUnsavedResultIndex = 0;
        return true;
    }

    private void maybeCreateResultsSheet(String name) {
        if(matchupsList != null && resultsSheet != null) {
            return;
        }

        matchupsList = createMatchupsList();
        Workbook workbook = new XSSFWorkbook();
        workbook.createSheet("albums_info");
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
            row.createCell(4).setCellValue(RESULT_EMPTY);
        }
        // Resize all columns to fit the content size
        for(int i = 0; i < 5; i++) {
            resultsSheet.autoSizeColumn(i);
        }
    }

    private void maybeCreateCompactResultsSheet(String name) {
        if(matchupsList != null && resultsSheet != null) {
            return;
        }

        matchupsList = createMatchupsList();
        XSSFWorkbook workbook = new XSSFWorkbook();
        copyAlbumSheetToAnotherSheet(workbook.createSheet("albums_info"));
        resultsSheet = workbook.createSheet(name + "_albums_results");

        //header
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerFont.setFontHeightInPoints((short) 14);
        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFont(headerFont);

        Row headerRow = resultsSheet.createRow(0);
        headerRow.setRowStyle(headerStyle);
        headerRow.createCell(0);
        for(int i = 0; i < albumCount; i++) {
            headerRow.createCell(i+1).setCellValue(i+1);
        }

        for(int i = 0; i < albumCount; i++) {
            Row row = resultsSheet.createRow(i + 1);
            row.createCell(0).setCellValue(i+1);
            row.getCell(0).setCellStyle(headerStyle);
            for(int j = 1; j < albumCount+1; j++) {
                row.createCell(j).setCellValue(RESULT_EMPTY);
            }
        }

        metadataSheet = workbook.createSheet("metadata");
        Row metadataRow = metadataSheet.createRow(0);
        metadataRow.createCell(0).setCellValue("Don't alter any info in this sheet unless you know what you're doing");
        metadataRow.createCell(1).setCellValue("RNG Seed: ");
        metadataRow.createCell(2).setCellValue(rngSeed);
        metadataRow.createCell(3).setCellValue("lastUnsavedResultIndex: ");
        metadataRow.createCell(4).setCellValue(0);

        // Resize all columns to fit the content size
        for(int i = 0; i < 5; i++) {
            metadataSheet.autoSizeColumn(i);
        }
    }

    private void copyAlbumSheetToAnotherSheet(Sheet sheet) {
        // not trivial, apparently https://stackoverflow.com/questions/848212/copying-excel-worksheets-in-poi
        for(int i = 0; i < albumsSheet.getPhysicalNumberOfRows(); i++) {
            Row r = albumsSheet.getRow(i);
            Row rCopy = sheet.createRow(i);
            for(int j = 0; j < r.getPhysicalNumberOfCells(); j++) {
                Cell c = r.getCell(j);
                Cell cCopy = rCopy.createCell(j);

                CellType cellType = c.getCellType();
                switch(cellType) {
                    case STRING:
                        cCopy.setCellValue(c.getStringCellValue());
                        break;
                    case NUMERIC:
                        cCopy.setCellValue(c.getNumericCellValue());
                        break;
                    case FORMULA:
                        cCopy.setCellValue("placeholder");
                        //bug where it doesn't set formula when cell is initially blank
                        cCopy.setCellFormula(c.getCellFormula());
                        break;
                    default:
                        break;
                }
            }
        }
        for(int i = 0; i < 9; i++) {
            sheet.autoSizeColumn(i);
        }
    }

    private List<Matchup> createMatchupsList() {
        List<Matchup> matchupsList = new ArrayList<>((albumCount * (albumCount-1))/2);
        int i = 0;
        while(i < albumCount) {
            for(int j = i+1; j < albumCount; j++) {
                matchupsList.add(new Matchup(i+1, albums[i].getName() + ", " + albums[i].getArtist(), j+1, albums[j].getName() + ", " + albums[j].getArtist()));
            }
            i++;
        }

        if(rngSeed == 0) {
            Random r = new Random();
            rngSeed = r.nextInt();
        }

        Collections.shuffle(matchupsList, new Random(rngSeed));
        return matchupsList;
    }

    public boolean getResultsSpreadsheet(@Nullable File file) {
        if(file == null || !file.isFile()) {
            return false;
        }

        try {
            resultsFile = file;
            FileInputStream excelFile = new FileInputStream(file);
            Workbook matchupWorkbook = new XSSFWorkbook(excelFile);
            resultsSheet = matchupWorkbook.getSheetAt(1);

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
                if(getIntFromCellSafe(row.getCell(4)) != RESULT_EMPTY) {
                    currentMatchupIndex = i;
                    earliestUnsavedResultIndex = i;
                    latestUnsavedResultIndex = i;
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

    public boolean getCompactResultsSpreadsheet(@Nullable File file) {
        if(file == null || !file.isFile()) {
            return false;
        }

        try {
            resultsFile = file;
            FileInputStream excelFile = new FileInputStream(file);
            Workbook matchupWorkbook = new XSSFWorkbook(excelFile);
            if(matchupWorkbook.getNumberOfSheets() < 3) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle(">:(");
                alert.setContentText("You're using an old version, Jorge.");
                CSSHelper.maybeApplyCSS(alert.getDialogPane());
                alert.showAndWait();
                System.exit(0);
            }
            resultsSheet = matchupWorkbook.getSheetAt(1);
            metadataSheet = matchupWorkbook.getSheetAt(2);
            rngSeed = (int) metadataSheet.getRow(0).getCell(2).getNumericCellValue();
            final int currentMatchup = (int) metadataSheet.getRow(0).getCell(4).getNumericCellValue();
            latestUnsavedResultIndex = currentMatchup;
            earliestUnsavedResultIndex = currentMatchup;
            currentMatchupIndex = currentMatchup;


            matchupsList = createMatchupsList();
            if(albumCount + 1 != resultsSheet.getPhysicalNumberOfRows()){
                return false;
            }

            for(int i = 0; i < currentMatchupIndex; i++) {
                Matchup m = matchupsList.get(i);
                Row r = resultsSheet.getRow(m.getAlbum1());
                m.setResult((int) r.getCell(m.getAlbum2()).getNumericCellValue());
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

    public Matchup getPrevMatchup() {
        if(currentMatchupIndex <= 0) {
            return null;
        }
        return matchupsList.get(--currentMatchupIndex);
    }

    public Album getAlbum(int index) {
        return albums[index];
    }

    public void setResult(int winningAlbumIndex) {
        if(currentMatchupIndex < earliestUnsavedResultIndex) {
            earliestUnsavedResultIndex = currentMatchupIndex;
        } else if(currentMatchupIndex > latestUnsavedResultIndex) {
            latestUnsavedResultIndex = currentMatchupIndex;
        }

        matchupsList.get(currentMatchupIndex).setResult(winningAlbumIndex);
        currentMatchupIndex++;
        hasUnsavedChanges = true;
    }

    public void setResult(int matchupIndex, int winningAlbumIndex) {
        matchupsList.get(matchupIndex).setResult(winningAlbumIndex);
        hasUnsavedChanges = true;
    }


    public void saveResults() {
        for(int i = earliestUnsavedResultIndex; i <= latestUnsavedResultIndex; i++) {
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
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error while saving results");
                alert.setContentText("Please save to a valid file");
                CSSHelper.maybeApplyCSS(alert.getDialogPane());
                alert.showAndWait();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        earliestUnsavedResultIndex = currentMatchupIndex;
        latestUnsavedResultIndex = currentMatchupIndex;
        hasUnsavedChanges = false;
    }

    public void saveCompactResults() {
        for(int i = earliestUnsavedResultIndex; i <= latestUnsavedResultIndex; i++) {
            Matchup m = matchupsList.get(i);
            resultsSheet.getRow(m.getAlbum1()).getCell(m.getAlbum2()).setCellValue(m.getResult());
        }
        metadataSheet.getRow(0).getCell(4).setCellValue(latestUnsavedResultIndex + 1);

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
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error while saving results");
                alert.setContentText("Please save to a valid file");
                CSSHelper.maybeApplyCSS(alert.getDialogPane());
                alert.showAndWait();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        earliestUnsavedResultIndex = currentMatchupIndex;
        latestUnsavedResultIndex = currentMatchupIndex;
        hasUnsavedChanges = false;
    }

    public boolean hasUnsavedChanges() {
        return hasUnsavedChanges;
    }

}
