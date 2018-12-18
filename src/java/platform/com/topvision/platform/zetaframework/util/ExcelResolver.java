package com.topvision.platform.zetaframework.util;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.PushbackInputStream;
import java.util.List;

import jxl.Sheet;
import jxl.Workbook;

import org.apache.poi.POIXMLDocument;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * 
 * @author Bravin
 * @created @2013年12月23日-下午3:24:39
 *
 */
public final class ExcelResolver {

    /**
     * 解析EXCEL
     * @param rowHandler
     * @param fis
     */
    public static void resolve(RowHandler rowHandler, FileInputStream fis) {
        try {
            InputStream pis = fis;
            if (!fis.markSupported()) {
                pis = new PushbackInputStream(fis, 8);
            }
            if (POIXMLDocument.hasOOXMLHeader(pis)) {
                XSSFWorkbook wordbook = new XSSFWorkbook(pis);
                resolveFromExcel2007(wordbook, rowHandler);
            } else if (POIFSFileSystem.hasPOIFSHeader(pis)) {
                Workbook workbook = Workbook.getWorkbook(pis);
                // POIFSFileSystem fs = new POIFSFileSystem(fis);
                resolveFromExcel2003(workbook, rowHandler);
            } else {
                throw new ExcelResolveException();
            }

        } catch (Exception e) {
            throw new ExcelResolveException();
        }
    }

    /**
     * 解析EXCEL2007
     * @param xssfWorkbook
     * @param rowHandler
     */
    private static void resolveFromExcel2007(XSSFWorkbook xssfWorkbook, RowHandler rowHandler) {
        for (int sheetNum = 0; sheetNum < xssfWorkbook.getNumberOfSheets(); sheetNum++) {
            XSSFSheet xssfSheet = xssfWorkbook.getSheetAt(sheetNum);
            if (xssfSheet == null) {
                continue;
            }
            for (int rowNum = 0; rowNum <= xssfSheet.getLastRowNum(); rowNum++) {
                Row row = new Row(rowNum, sheetNum);
                XSSFRow xssfRow = xssfSheet.getRow(rowNum);
                if (xssfRow == null) {
                    continue;
                }
                for (int cellNum = 0; cellNum < xssfRow.getLastCellNum(); cellNum++) {
                    XSSFCell xssfCell = xssfRow.getCell(cellNum);
                    if (xssfCell == null) {
                        continue;
                    }
                    Cell cell = new Cell(cellNum);
                    cell.setContent(getValue(xssfCell));
                    row.addCell(cell);
                }
                if (!emptyRow(row)) {
                    Boolean handleResult = rowHandler.handleRow(row);
                    if (handleResult != null && handleResult == true) {
                        return;
                    }
                }
            }
        }
    }

    /**
     * @param row
     * @return
     */
    private static boolean emptyRow(Row row) {
        List<Cell> cells = row.getCells();
        for (Cell cell : cells) {
            if (!"".equals(cell.getContent())) {
                return false;
            }
        }
        return true;
    }

    private static String getValue(XSSFCell xssfCell) {
        if (xssfCell.getCellType() == XSSFCell.CELL_TYPE_BOOLEAN) {
            return String.valueOf(xssfCell.getBooleanCellValue());
        } else if (xssfCell.getCellType() == XSSFCell.CELL_TYPE_NUMERIC) {
            return xssfCell.getRawValue();
        } else {
            return String.valueOf(xssfCell.getStringCellValue());
        }
    }

    /**
     * 解析EXCEL2003
     * @param workbook
     * @param rowHandler
     */
    private static void resolveFromExcel2003(Workbook workbook, RowHandler rowHandler) {
        for (int sheetNum = 0; sheetNum < workbook.getNumberOfSheets(); sheetNum++) {
            Sheet hssfSheet = workbook.getSheet(sheetNum);
            if (hssfSheet == null) {
                continue;
            }
            for (int rowNum = 0; rowNum <= hssfSheet.getRows(); rowNum++) {
                Row row = new Row(rowNum, sheetNum);
                jxl.Cell[] hssfRow = hssfSheet.getRow(rowNum);
                if (hssfRow == null) {
                    continue;
                }
                for (int cellNum = 0; cellNum < hssfRow.length; cellNum++) {
                    jxl.Cell hssfCell = hssfRow[cellNum];
                    if (hssfCell == null) {
                        continue;
                    }
                    Cell cell = new Cell(cellNum);
                    cell.setContent(hssfCell.getContents());
                    row.addCell(cell);
                }
                if (!emptyRow(row)) {
                    Boolean handleResult = rowHandler.handleRow(row);
                    if (handleResult != null && handleResult == true) {
                        return;
                    }
                }
            }
        }
    }

}