/***********************************************************************
 * $Id: ExcelUtil.java,v1.0 2013-4-27 上午11:46:18 $
 * 
 * @author: Rod John
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.framework.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * Excel工具类 支持2007的Excel转换，支持2003的转换
 * 
 * @author Rod John
 * @created @2013-4-27-上午11:46:18
 * 
 */
public class ExcelUtil {

    /**
     * 将Excel数据转成以sheetName为key的map数据,支持xls和xlsx格式
     * 
     * @param file
     * @author fanzidong
     * @return
     * @throws IOException
     */
    public static Map<String, String[][]> getExcelMapData(File file) throws IOException {
        InputStream fs = null;
        try {
            fs = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            throw e;
        }
        // 尝试用XSSFWorkbook去读取文件，如果成功，表明是xlsx，失败，使用jxl去尝试
        XSSFWorkbook xssfWorkbook = null;
        Workbook workBook = null;
        try {
            xssfWorkbook = new XSSFWorkbook(fs);
        } catch (Exception e1) {
            // 表明尝试用xlsx格式解析失败，尝试xls格式
            try {
                // XSSF读取失败后，需要重新建立读取流
                fs = new FileInputStream(file);
                workBook = Workbook.getWorkbook(fs);
            } catch (Exception e2) {
                // 使用xls解析也失败，返回错误
                if (workBook != null) {
                    workBook.close();
                }
                if (fs != null) {
                    fs.close();
                }
                return null;
            }
        }
        Map<String, String[][]> retMap = new HashMap<String, String[][]>();
        try {
            if (xssfWorkbook != null) {
                // 使用POI进行后续解析
                retMap = getExcelMapDataFor2007(xssfWorkbook);
            } else if (workBook != null) {
                // 使用JXL进行后续解析
                retMap = getExcelMapDataFor2003(workBook);
            }
            return retMap;
        } catch (Exception e3) {
            throw e3;
        } finally {
            if (workBook != null) {
                workBook.close();
            }
            if (fs != null) {
                fs.close();
            }
        }
    }

    private static Map<String, String[][]> getExcelMapDataFor2007(XSSFWorkbook xssfWorkbook) throws IOException {
        try {
            Map<String, String[][]> map = new HashMap<String, String[][]>();
            // 循环工作表Sheet
            for (int numSheet = 0; numSheet < xssfWorkbook.getNumberOfSheets(); numSheet++) {
                XSSFSheet xssfSheet = xssfWorkbook.getSheetAt(numSheet);
                if (xssfSheet == null) {
                    continue;
                }
                String[][] tmp = new String[xssfSheet.getLastRowNum() + 1][];
                map.put(xssfSheet.getSheetName(), tmp);
                // 循环行Row
                for (int rowNum = 0; rowNum <= xssfSheet.getLastRowNum(); rowNum++) {
                    XSSFRow xssfRow = xssfSheet.getRow(rowNum);
                    if (xssfRow == null) {
                        continue;
                    }
                    tmp[rowNum] = new String[xssfRow.getLastCellNum()];
                    // 循环单元格Cell
                    for (int cellNum = 0; cellNum < xssfRow.getLastCellNum(); cellNum++) {
                        XSSFCell cell = xssfRow.getCell(cellNum);
                        if (cell != null) {
                            cell.setCellType(org.apache.poi.ss.usermodel.Cell.CELL_TYPE_STRING);
                            tmp[rowNum][cellNum] = cell.getRichStringCellValue().getString();
                        } else {
                            tmp[rowNum][cellNum] = null;
                        }
                    }
                }
            }
            return map;
        } catch (Exception e) {
            return null;
        }
    }

    private static Map<String, String[][]> getExcelMapDataFor2003(Workbook workBook) throws IOException {
        try {
            Map<String, String[][]> map = new HashMap<String, String[][]>();
            // 循环工作表Sheet
            for (int sheetNum = 0; sheetNum < workBook.getNumberOfSheets(); sheetNum++) {
                Sheet sheet = workBook.getSheet(sheetNum);
                String[][] tmp = new String[sheet.getRows() + 1][];
                // 循环行Row
                for (int rowNum = 0; rowNum < sheet.getRows(); rowNum++) {
                    int cellNumInRow = sheet.getRow(rowNum).length;
                    tmp[rowNum] = new String[cellNumInRow];
                    // 循环单元格Cell
                    for (int cellNum = 0; cellNum < cellNumInRow; cellNum++) {
                        // modified by huangdongsheng @2013-7-1 getCell方法第一个参数为列，第二个参数为行
                        Cell cell = sheet.getCell(cellNum, rowNum);
                        tmp[rowNum][cellNum] = cell.getContents();
                    }
                }
                // added by huangdongsheng @2013-7-1 放到结果中
                map.put(sheet.getName(), tmp);
            }
            return map;
        } catch (Exception e) {
            return null;
        } finally {
            if (workBook != null) {
                workBook.close();
            }
        }
    }
    
    /**
     * 
     * @return
     */
    public static List<String[][]> getExcelData(File file) throws IOException{
        List<String[][]> rList = new ArrayList<String[][]>();
        InputStream fs = null;
        try{
            // 加载excel文件
            fs = new FileInputStream(file);
        }catch(FileNotFoundException e){
            throw e;
        }
        // 尝试用XSSFWorkbook去读取文件，如果成功，表明是xlsx，失败，使用jxl去尝试
        XSSFWorkbook xssfWorkbook = null;
        Workbook workBook = null;
        try {
            xssfWorkbook = new XSSFWorkbook(fs);
        } catch (Exception e1) {
            // 表明尝试用xlsx格式解析失败，尝试xls格式
            try {
                // XSSF读取失败后，需要重新建立读取流
                fs = new FileInputStream(file);
                workBook = Workbook.getWorkbook(fs);
            } catch (Exception e2) {
                // 使用xls解析也失败，返回错误
                if (workBook != null) {
                    workBook.close();
                }
                if (fs != null) {
                    fs.close();
                }
                return null;
            }
        }
        try {
            if (xssfWorkbook != null) {
                // 使用POI进行后续解析
                rList = getExcelDataFor2007(xssfWorkbook);
            } else if (workBook != null) {
                // 使用JXL进行后续解析
                rList = getExcelDataFor2003(workBook);
            }
            return rList;
        } catch (Exception e3) {
            throw e3;
        } finally {
            if (workBook != null) {
                workBook.close();
            }
            if (fs != null) {
                fs.close();
            }
        }
    }
    
    private static List<String[][]> getExcelDataFor2003(Workbook workBook) {
        try {
            List<String[][]> rList = new ArrayList<String[][]>();
            // 循环工作表Sheet
             for (int sheetNum = 0; sheetNum < workBook.getNumberOfSheets(); sheetNum++) {
                Sheet sheet = workBook.getSheet(sheetNum);
                String[][] tmp = new String[sheet.getRows()][];
                int cellNumInRow = 0;
                // 循环行Row
                for (int rowNum = 0; rowNum < sheet.getRows(); rowNum++) {
                	if(rowNum==0){
                		cellNumInRow = sheet.getRow(rowNum).length;
                	}
                    tmp[rowNum] = new String[cellNumInRow];
                    // 循环单元格Cell
                    for (int cellNum = 0; cellNum < cellNumInRow; cellNum++) {
                        // modified by huangdongsheng @2013-7-1 getCell方法第一个参数为列，第二个参数为行
                        Cell cell = sheet.getCell(cellNum, rowNum);
                        tmp[rowNum][cellNum] = cell.getContents();
                    }
                }
                // added by huangdongsheng @2013-7-1 放到结果中
                rList.add(tmp);
            }
            return rList;
        } catch (Exception e) {
            return null;
        } finally {
            if (workBook != null) {
                workBook.close();
            }
        }
    }

    private static List<String[][]> getExcelDataFor2007(XSSFWorkbook xssfWorkbook) {
        try {
            List<String[][]> rList = new ArrayList<String[][]>();
            // 循环工作表Sheet
            for (int numSheet = 0; numSheet < xssfWorkbook.getNumberOfSheets(); numSheet++) {
                XSSFSheet xssfSheet = xssfWorkbook.getSheetAt(numSheet);
                if (xssfSheet == null) {
                    continue;
                }
                String[][] tmp = new String[xssfSheet.getLastRowNum() + 1][];
                rList.add(tmp);
                // 循环行Row
                for (int rowNum = 0; rowNum <= xssfSheet.getLastRowNum(); rowNum++) {
                    XSSFRow xssfRow = xssfSheet.getRow(rowNum);
                    if (xssfRow == null) {
                        continue;
                    }
                    tmp[rowNum] = new String[xssfRow.getLastCellNum()];
                    // 循环单元格Cell
                    for (int cellNum = 0; cellNum < xssfRow.getLastCellNum(); cellNum++) {
                        XSSFCell cell = xssfRow.getCell(cellNum);
                        if (cell != null) {
                            cell.setCellType(org.apache.poi.ss.usermodel.Cell.CELL_TYPE_STRING);
                            tmp[rowNum][cellNum] = cell.getRichStringCellValue().getString();
                        } else {
                            tmp[rowNum][cellNum] = null;
                        }
                    }
                }
            }
            return rList;
        } catch (Exception e) {
            return null;
        }
    }

    public static List<String[][]> getExcelDataFor2003(String fileUrl) throws IOException {
        List<String[][]> rList = new ArrayList<String[][]>();
        Workbook workBook = null;
        InputStream fs = null;
        try {
            // 加载excel文件
            fs = new FileInputStream(new File(fileUrl));
            // 得到 workbook
            workBook = Workbook.getWorkbook(fs);
            // 循环工作表Sheet
            for (int sheetNum = 0; sheetNum < workBook.getNumberOfSheets(); sheetNum++) {
                Sheet sheet = workBook.getSheet(sheetNum);
                String[][] tmp = new String[sheet.getRows() + 1][];
                // 循环行Row
                for (int rowNum = 0; rowNum < sheet.getRows(); rowNum++) {
                    int cellNumInRow = sheet.getRow(rowNum).length;
                    tmp[rowNum] = new String[cellNumInRow];
                    // 循环单元格Cell
                    for (int cellNum = 0; cellNum < cellNumInRow; cellNum++) {
                        // modified by huangdongsheng @2013-7-1 getCell方法第一个参数为列，第二个参数为行
                        Cell cell = sheet.getCell(cellNum, rowNum);
                        tmp[rowNum][cellNum] = cell.getContents();
                    }
                }
                // added by huangdongsheng @2013-7-1 放到结果中
                rList.add(tmp);
            }
            return rList;
        } catch (Exception e) {
            return null;
        } finally {
            if (workBook != null) {
                workBook.close();
            }
            // added by huangdongsheng @2013-7-1 关闭输入流
            if (fs != null) {
                fs.close();
            }
        }
    }

    public static List<String[][]> getExcelDataFor2007(File file) throws IOException {
        InputStream is = null;
        XSSFWorkbook xssfWorkbook = null;
        try {
            List<String[][]> rList = new ArrayList<String[][]>();
            is = new FileInputStream(file);
            xssfWorkbook = new XSSFWorkbook(is);
            // 循环工作表Sheet
            for (int numSheet = 0; numSheet < xssfWorkbook.getNumberOfSheets(); numSheet++) {
                XSSFSheet xssfSheet = xssfWorkbook.getSheetAt(numSheet);
                if (xssfSheet == null) {
                    continue;
                }
                String[][] tmp = new String[xssfSheet.getLastRowNum() + 1][];
                rList.add(tmp);
                // 循环行Row
                for (int rowNum = 0; rowNum <= xssfSheet.getLastRowNum(); rowNum++) {
                    XSSFRow xssfRow = xssfSheet.getRow(rowNum);
                    if (xssfRow == null) {
                        continue;
                    }
                    tmp[rowNum] = new String[xssfRow.getLastCellNum()];
                    // 循环单元格Cell
                    for (int cellNum = 0; cellNum < xssfRow.getLastCellNum(); cellNum++) {
                        XSSFCell cell = xssfRow.getCell(cellNum);
                        if (cell != null) {
                            cell.setCellType(org.apache.poi.ss.usermodel.Cell.CELL_TYPE_STRING);
                            tmp[rowNum][cellNum] = cell.getRichStringCellValue().getString();
                        } else {
                            tmp[rowNum][cellNum] = null;
                        }
                    }
                }
            }
            return rList;
        } catch (Exception e) {
            return null;
        } finally {
            // added by huangdongsheng @2013-7-1 关闭输入流
            if (xssfWorkbook != null) {
                is.close();
            }
        }
    }

    /**
     * 
     * 
     * @param originData
     * @return
     */
    public static String[][] getExcelDataForDiscovery(String[][] originData) {
        int columnLength = originData[0].length;
        String[][] resultData = new String[originData.length][columnLength];
        for (int i = 0; i < originData.length; i++) {
            String[] originRow = originData[i];
            for (int j = 0; j < originRow.length; j++) {
                resultData[i][j] = originData[i][j];
            }
        }
        return resultData;
    }

}
