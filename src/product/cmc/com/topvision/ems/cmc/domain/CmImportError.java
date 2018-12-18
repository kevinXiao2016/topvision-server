/***********************************************************************
 * $Id: CmImportError.java,v1.0 2017年8月9日 上午8:40:04 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.domain;

/**
 * @author haojie
 * @created @2017年8月9日-上午8:40:04
 *
 */
public class CmImportError {

    private String cmMacAddr;
    private String cmAlias;
    private String cmClassified;
    private String errorMessage;
    private Integer errorLine;

    public String getCmMacAddr() {
        return cmMacAddr;
    }

    public void setCmMacAddr(String cmMacAddr) {
        this.cmMacAddr = cmMacAddr;
    }

    public String getCmAlias() {
        return cmAlias;
    }

    public void setCmAlias(String cmAlias) {
        this.cmAlias = cmAlias;
    }

    public String getCmClassified() {
        return cmClassified;
    }

    public void setCmClassified(String cmClassified) {
        this.cmClassified = cmClassified;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public Integer getErrorLine() {
        return errorLine;
    }

    public void setErrorLine(Integer errorLine) {
        this.errorLine = errorLine;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("CmImportError [cmMacAddr=");
        builder.append(cmMacAddr);
        builder.append(", cmAlias=");
        builder.append(cmAlias);
        builder.append(", cmClassified=");
        builder.append(cmClassified);
        builder.append(", errorMessage=");
        builder.append(errorMessage);
        builder.append(", errorLine=");
        builder.append(errorLine);
        builder.append("]");
        return builder.toString();
    }

}
