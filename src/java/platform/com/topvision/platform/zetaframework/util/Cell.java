/***********************************************************************
 * $Id: Cell.java,v1.0 2013年12月23日 下午3:10:27 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.platform.zetaframework.util;

/**
 * @author Bravin
 * @created @2013年12月23日-下午3:10:27
 *
 */
public class Cell {
    private String content;
    private int cellNum;

    public Cell(int cellNum) {
        this.cellNum = cellNum;
    }

    public int getCellNum() {
        return cellNum;
    }

    public void setCellNum(int cellNum) {
        this.cellNum = cellNum;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "Cell [content=" + content + ", cellNum=" + cellNum + "]";
    }

}
