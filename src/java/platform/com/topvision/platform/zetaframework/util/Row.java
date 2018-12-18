/***********************************************************************
 * $Id: Row.java,v1.0 2013年12月23日 下午3:09:57 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.platform.zetaframework.util;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Bravin
 * @created @2013年12月23日-下午3:09:57
 *
 */
public class Row {
    private List<Cell> cells;
    private int rowNum;
    private int sheetNum;

    public Row() {
        cells = new ArrayList<>();
    }

    public Row(int rowNum, int sheetNum) {
        this();
        this.rowNum = rowNum;
        this.sheetNum = sheetNum;
    }

    public List<Cell> getCells() {
        return cells;
    }

    public void setCells(List<Cell> cells) {
        this.cells = cells;
    }

    public void addCell(Cell cell) {
        cells.add(cell);
    }

    public int getRowNum() {
        return rowNum;
    }

    public void setRowNum(int rowNum) {
        this.rowNum = rowNum;
    }

    public int getSheetNum() {
        return sheetNum;
    }

    public void setSheetNum(int sheetNum) {
        this.sheetNum = sheetNum;
    }

    @Override
    public String toString() {
        return "Row [cells=" + cells + ", rowNum=" + rowNum + ", sheetNum=" + sheetNum + "]";
    }
}
