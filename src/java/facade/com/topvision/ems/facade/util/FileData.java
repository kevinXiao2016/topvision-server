/***********************************************************************
 * $ com.topvision.ems.engine.util.FileData,v1.0 2012-5-2 11:56:08 $
 *
 * @author: Administrator
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.facade.util;

import java.io.Serializable;

/**
 * @author Administrator
 * @created @2012-5-2-11:56:08
 */
public abstract class FileData implements Serializable {
    private static final long serialVersionUID = 8610442015974081188L;

    protected FileData() {
        fileName = String.valueOf(System.nanoTime());
    }

    protected String fileName;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
