package excel;

import data.Album;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Iterator;

public class ExcelHelper {

    XSSFWorkbook workbook;
    XSSFSheet albums;
    XSSFSheet sorted;
    XSSFSheet metadata;

    public ExcelHelper(){
    }

    public void getSpreadsheet(String path) throws IOException {
        File myFile = new File(path);
        FileInputStream fis = new FileInputStream(myFile);
        // Finds the workbook instance for XLSX file
        try (XSSFWorkbook myWorkBook = new XSSFWorkbook(fis)) {
            workbook = myWorkBook;
            // Return first sheet from the XLSX workbook
            albums = myWorkBook.getSheetAt(0);
            sorted = myWorkBook.getSheetAt(1);
            metadata = myWorkBook.getSheetAt(2);
        }

        // Get iterator to all the rows in current sheet
        Iterator<Row> rowIterator = albums.iterator();

        // Traversing over each row of XLSX file
        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();

            // For each row, iterate through each columns
            Iterator<Cell> cellIterator = row.cellIterator();
            while (cellIterator.hasNext()) {

                Cell cell = cellIterator.next();

//                switch (cell.getCellType()) {
//                    case CellType.STRING:
//                        System.out.print(cell.getStringCellValue() + "\t");
//                        break;
//                    case CellType.NUMERIC:
//                        System.out.print(cell.getNumericCellValue() + "\t");
//                        break;
//                    case CellType.BOOLEAN:
//                        System.out.print(cell.getBooleanCellValue() + "\t");
//                        break;
//                    default :
//
//                }
            }
            System.out.println("");
        }
    }

//    public Album getRow(int index) {
//        return new Album();
//    }
}
