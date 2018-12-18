/***********************************************************************
 * $Id: OltFile.java,v1.0 2011-9-28 ����12:10:27 $
 * 
 * @author: huqiao
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.domain;

import java.io.Serializable;

/**
 * @author huqiao
 * 
 */
public class OltFile implements Serializable {
    private static final long serialVersionUID = -6984175738779991526L;
    private String filePath;
    private String fileName;
    private long fileSize;
    private String fileModifyTime;
    private String fileType;

    public OltFile(String filePath, String fileName, long fileSize, String fileModifyTime, String fileType) {
        super();
        this.filePath = filePath;
        this.fileName = fileName;
        this.fileSize = fileSize;
        this.fileModifyTime = fileModifyTime;
        this.fileType = fileType;
    }

    /**
     * @return the filePath
     */
    public String getFilePath() {
        return filePath;
    }

    /**
     * @param filePath
     *            the filePath to set
     */
    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    /**
     * @return the fileName
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * @param fileName
     *            the fileName to set
     */
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    /**
     * @return the fileSize
     */
    public long getFileSize() {
        return fileSize;
    }

    /**
     * @param fileSize
     *            the fileSize to set
     */
    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    /**
     * @return the fileType
     */
    /**
     * @return the fileType
     */
    public String getFileType() {
        return fileType;
    }

    /**
     * @param fileType
     *            the fileType to set
     */
    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    /**
     * @return the fileModifyTime
     */
    public String getFileModifyTime() {
        return fileModifyTime;
    }

    /**
     * @param fileModifyTime
     *            the fileModifyTime to set
     */
    public void setFileModifyTime(String fileModifyTime) {
        this.fileModifyTime = fileModifyTime;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("OltFile");
        sb.append("{fileModifyTime='").append(fileModifyTime).append('\'');
        sb.append(", filePath='").append(filePath).append('\'');
        sb.append(", fileName='").append(fileName).append('\'');
        sb.append(", fileSize=").append(fileSize);
        sb.append(", fileType='").append(fileType).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
