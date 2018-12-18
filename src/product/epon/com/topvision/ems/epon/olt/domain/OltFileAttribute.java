/***********************************************************************
 * $Id: OltFileAttribute.java,v1.0 2011-9-30 下午02:42:01 $
 * 
 * @author: Administrator
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.olt.domain;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

import com.topvision.framework.annotation.SnmpProperty;

/**
 * @author huqiao
 * @created @2011-9-30-下午02:42:01
 * 
 */
public class OltFileAttribute implements Serializable {
    public static final Pattern oidPattern = Pattern.compile("[0-9]+");
    private static final long serialVersionUID = 6211697636630441802L;
    private Long entityId;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.1.6.2.1.1", index = true)
    private String filePath;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.1.6.2.1.2", index = true)
    private String fileName;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.1.6.2.1.3")
    private Long fileSize;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.1.6.2.1.4")
    private String strFileModifyTime;
    private Long fileModifyTime;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.1.6.2.1.5", writable = true, type = "Integer32")
    private Integer fileManagementAction;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.1.6.2.1.6")
    private Integer fileAttribute;

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
    public Long getFileSize() {
        return fileSize;
    }

    /**
     * @param fileSize
     *            the fileSize to set
     */
    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    /**
     * @return the fileModifyTime
     */
    public Long getFileModifyTime() {
        return fileModifyTime;
    }

    /**
     * @param fileModifyTime
     *            the fileModifyTime to set
     */
    public void setFileModifyTime(Long fileModifyTime) {
        this.fileModifyTime = fileModifyTime;
        this.strFileModifyTime = String.valueOf(fileModifyTime);
    }

    /**
     * @return the fileManagementAction
     */
    public Integer getFileManagementAction() {
        return fileManagementAction;
    }

    /**
     * @param fileManagementAction
     *            the fileManagementAction to set
     */
    public void setFileManagementAction(Integer fileManagementAction) {
        this.fileManagementAction = fileManagementAction;
    }

    /**
     * @return the fileAttribute
     */
    public Integer getFileAttribute() {
        return fileAttribute;
    }

    /**
     * @param fileAttribute
     *            the fileAttribute to set
     */
    public void setFileAttribute(Integer fileAttribute) {
        this.fileAttribute = fileAttribute;
    }

    /**
     * @return the strFileModifyTime
     */
    public String getStrFileModifyTime() {
        if (fileModifyTime != null) {
            this.strFileModifyTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(fileModifyTime));
        }
        return strFileModifyTime;

    }

    /**
     * @param strFileModifyTime
     *            the strFileModifyTime to set
     */
    public void setStrFileModifyTime(String strFileModifyTime) {
        this.strFileModifyTime = strFileModifyTime;
        this.fileModifyTime = Long.parseLong(strFileModifyTime);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("OltFileAttribute [filePath=");
        builder.append(filePath);
        builder.append(", fileName=");
        builder.append(fileName);
        builder.append(", fileSize=");
        builder.append(fileSize);
        builder.append(", strFileModifyTime=");
        builder.append(strFileModifyTime);
        builder.append(", fileModifyTime=");
        builder.append(fileModifyTime);
        builder.append(", fileManagementAction=");
        builder.append(fileManagementAction);
        builder.append(", fileAttribute=");
        builder.append(fileAttribute);
        builder.append("]");
        return builder.toString();
    }

    /**
     * @return the entityId
     */
    public Long getEntityId() {
        return entityId;
    }

    /**
     * @param entityId
     *            the entityId to set
     */
    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public static String checkFileName(String index) {
        StringBuilder indexBuilder = new StringBuilder();
        if (oidPattern.matcher(index).matches()) {
            byte[] values = index.getBytes();
            indexBuilder.append(".").append(values.length);
            for (int i = 0; i < values.length; i++) {
                indexBuilder.append(".").append((int) values[i]);
            }
            return indexBuilder.toString().substring(1);
        }
        return index;
    }

}
