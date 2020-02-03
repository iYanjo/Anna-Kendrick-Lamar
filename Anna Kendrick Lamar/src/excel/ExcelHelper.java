package excel;

import data.Album;
import data.Matchup;
import javafx.stage.FileChooser;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

import static org.apache.poi.ss.usermodel.CellType.NUMERIC;
import static org.apache.poi.ss.usermodel.CellType.STRING;

public class ExcelHelper {

    private static int albumCount;

    XSSFWorkbook workbook;
    XSSFSheet albumsSheet;
    XSSFSheet sorted;
    Album[] albums;
    List<Matchup> matchupsList;
    private static int nextMatchupIndex;

    public ExcelHelper(){
    }

    public boolean isAlbumsFetched() {
        return albums != null;
    }

    public void getAlbumsSpreadsheet() throws IOException {
        File spreadsheet = new File(".\\src\\res\\Albums_2010s.xlsx");

        FileInputStream fis = new FileInputStream(spreadsheet);
        try (XSSFWorkbook myWorkBook = new XSSFWorkbook(fis)) {
            workbook = myWorkBook;
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
                    getStringFromAlbumCellSafe(row.getCell(0)),
                    getStringFromAlbumCellSafe(row.getCell(1)),
                    (int)(row.getCell(2).getNumericCellValue()),
                    getScoreFromAlbumCellSafe(row.getCell(3)),
                    getScoreFromAlbumCellSafe(row.getCell(4)),
                    getScoreFromAlbumCellSafe(row.getCell(5)),
                    getScoreFromAlbumCellSafe(row.getCell(6)),
                    getScoreFromAlbumCellSafe(row.getCell(7)),
                    row.getCell(8).getStringCellValue());
        }

        outputAlbumPicNames();
    }

    // run once to get names, not for final app
    private void outputAlbumPicNames() {
        String dirPath = ".\\src\\res\\Album Artwork\\";
        System.out.println("here we are");


        for(Album album : albums) {
            String name = dirPath + album.getName();
            File file = new File(name + ".jpg");
            if(file.isFile()) {
                System.out.println("\"" + album.getName() + ".jpg" + "\",");
                continue;
            }

            file = new File(name + ".png");
            if(file.isFile()) {
                System.out.println("\"" + album.getName() + ".png" + "\",");
                continue;
            }

            file = new File(name + ".jpeg");
            if(file.isFile()) {
                System.out.println("\"" + album.getName() + ".jpeg" + "\",");
                continue;
            }

            System.out.println("\"problem with " + album.getName() + "\",");
        }
    }

    private String getStringFromAlbumCellSafe(Cell cell) {
        return cell.getCellType().equals(NUMERIC) ? Double.toString(cell.getNumericCellValue()) : cell.getStringCellValue();
    }

    private String getScoreFromAlbumCellSafe(Cell cell) {
        return cell.getCellType().equals(STRING) ? cell.getStringCellValue() : Double.toString(cell.getNumericCellValue());
    }

    // return true if created successfully
    public boolean createResultsSpreadsheet(String name) throws IOException {
        if(albums == null) {
            return false;
        }

        matchupsList = createMatchupsList(name.hashCode());

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet(name + "_albums_results");

        //header
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerFont.setFontHeightInPoints((short) 14);
        CellStyle headerCellStyle = workbook.createCellStyle();
        headerCellStyle.setFont(headerFont);

        Row headerRow = sheet.createRow(0);
        headerRow.setRowStyle(headerCellStyle);
        headerRow.createCell(0).setCellValue("Album 1 #");
        headerRow.createCell(1).setCellValue("Album 1 Name + Artist");
        headerRow.createCell(2).setCellValue("Album 2 #");
        headerRow.createCell(3).setCellValue("Album 2 Name + Artist");
        headerRow.createCell(4).setCellValue("Result (-1 is empty, -2 is skip)");

        int NUM_HEADER_ROWS_IN_MATCHUPS = 1;
        for(int i = 0; i < matchupsList.size(); i++) {
            Row row = sheet.createRow(i + NUM_HEADER_ROWS_IN_MATCHUPS);
            Matchup matchup = matchupsList.get(i);
            row.createCell(0).setCellValue(matchup.getAlbum1());
            row.createCell(1).setCellValue(matchup.getAlbum1String());
            row.createCell(2).setCellValue(matchup.getAlbum2());
            row.createCell(3).setCellValue(matchup.getAlbum2String());
            row.createCell(4);
        }
        // Resize all columns to fit the content size
        for(int i = 0; i < 5; i++) {
            sheet.autoSizeColumn(i);
        }

        try
        {
            // Write the output to a file
            FileChooser fileChooser = new FileChooser();
            fileChooser.setInitialFileName(name + "_albums_results.xlsx");
            fileChooser.setTitle("Keep the name [your name]_albums_results");
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Excel Sheet", "*.xlsx"));

            File saveDir = fileChooser.showSaveDialog(null);
            if(saveDir == null) {
                return false;
            }
            // todo: catch error if saveDir is null
            FileOutputStream fos = new FileOutputStream(saveDir);
            workbook.write(fos);
            fos.close();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
        workbook.close();
        nextMatchupIndex = 0;
        return true;
    }

    private List<Matchup> createMatchupsList(long seed) {
        List<Matchup> list = new ArrayList<>((albumCount * (albumCount-1))/2);
        int i = 0;
        while(i < albumCount) {
            for(int j = i+1; j < albumCount; j++) {
                list.add(new Matchup(i, albums[i].getName() + ", " + albums[i].getArtist(), j, albums[j].getName() + ", " + albums[j].getArtist()));
            }
            i++;
        }

        Collections.shuffle(list, new Random(seed));
        return list;
    }

    public Matchup getNextMatchup() {
        return matchupsList.get(nextMatchupIndex++);
    }

    public Album getAlbum(int index) {
        return albums[index];
    }
}
