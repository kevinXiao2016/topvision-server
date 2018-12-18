/***********************************************************************
 * $Id: CmImportError.java,v1.0 2017年8月7日 上午10:33:39 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.cm.domain;

/**
 * @author haojie
 * @created @2017年8月7日-上午10:33:39
 *
 */
public class CmImportError {

    private Integer errorLine;
    private String errorMessage;
    private String cmMacAddr;
    private String cmAlias;
    private String cmClassified;

    public Integer getErrorLine() {
        return errorLine;
    }

    public void setErrorLine(Integer errorLine) {
        this.errorLine = errorLine;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

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

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("CmImportError [errorLine=");
        builder.append(errorLine);
        builder.append(", errorMessage=");
        builder.append(errorMessage);
        builder.append(", cmMacAddr=");
        builder.append(cmMacAddr);
        builder.append(", cmAlias=");
        builder.append(cmAlias);
        builder.append(", cmClassified=");
        builder.append(cmClassified);
        builder.append("]");
        return builder.toString();
    }

}
