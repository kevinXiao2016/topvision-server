/***********************************************************************
 * $Id: CmcDevSoftware.java,v1.0 2012-8-31 上午11:50:56 $
 * 
 * @author: leo
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.ccmts.facade.domain;

import java.io.Serializable;

import com.topvision.framework.annotation.SnmpProperty;

/**
 * @author leo
 * @created @2012-8-31-上午11:50:56
 * 
 */

public class CmcDevSoftware implements Serializable {
    private static final long serialVersionUID = 622224130647279781L;

    private Long cmcId;
    private Long entityId;
    @SnmpProperty(oid = "1.3.6.1.2.1.69.1.3.2.0", writable = true, type = "OctetString")
    private String docsDevSwFilename;
    @SnmpProperty(oid = "1.3.6.1.2.1.69.1.3.3.0", writable = true, type = "Integer32")
    private Integer docsDevSwAdminStatus;
    @SnmpProperty(oid = "1.3.6.1.2.1.69.1.3.4.0")
    private Integer docsDevSwOperStatus;
    @SnmpProperty(oid = "1.3.6.1.2.1.69.1.3.5.0")
    private String docsDevSwCurrentVers;
    @SnmpProperty(oid = "1.3.6.1.2.1.69.1.3.6.0", writable = true, type = "Integer32")
    private Integer docsDevSwServerAddressType;
    @SnmpProperty(oid = "1.3.6.1.2.1.69.1.3.7.0", writable = true, type = "ByteIpAddress")
    private String docsDevSwServerAddress;
    @SnmpProperty(oid = "1.3.6.1.2.1.69.1.3.7.0", writable = true, type = "OctetString")
    private String docsDevSwServerAddressOld;
    private Long docsDevSwServerAddressLong;
    @SnmpProperty(oid = "1.3.6.1.2.1.69.1.3.8.0", writable = true, type = "Integer32")
    private Integer docsDevSwServerTransportProtocol;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.5.1.9.1.0", writable = true, type = "Integer32")
    private Integer topCcmtsSwTftpTransferFileType;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.5.1.9.2.0", writable = true, type = "Integer32")
    private Integer topCcmtsSwTftpTransferAction;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.5.1.9.3.0")
    private Integer topCcmtsSwTftpTransferProgress;

    private String docsDevSwAdminStatusString;
    private String docsDevSwOperStatusString;
    private String docsDevSwServerTransportProtocolString;
    private String topCcmtsSwTftpTransferFileTypeString;
    private String topCcmtsSwTftpTransferActionString;

    private final static String[] ADMINSTATUSTYPES = { "", "upgradeFromMgt", "allowProvisioningUpgrade",
            "ignoreProvisioningUpgrade" };
    private final static String[] OPERSTATUSTYPES = { "", "inProgress", "completeFromProvisioning", "completeFromMgt",
            "failed", "other" };
    private final static String[] PROTOCOLTYPES = { "", "tftp", "http" };
    private final static String[] FILETYPES = { "", "kernel", "rootfs", "cmcapp", "config" };
    private final static String[] ACTIONTYPES = { "", "load", "upload" };

    public Long getCmcId() {
        return cmcId;
    }

    public void setCmcId(Long cmcId) {
        this.cmcId = cmcId;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public String getDocsDevSwFilename() {
        return docsDevSwFilename;
    }

    public void setDocsDevSwFilename(String docsDevSwFilename) {
        this.docsDevSwFilename = docsDevSwFilename;
    }

    public Integer getDocsDevSwAdminStatus() {
        return docsDevSwAdminStatus;
    }

    public void setDocsDevSwAdminStatus(Integer docsDevSwAdminStatus) {
        if (docsDevSwAdminStatus != null && docsDevSwAdminStatus < ADMINSTATUSTYPES.length) {
            docsDevSwAdminStatusString = ADMINSTATUSTYPES[docsDevSwAdminStatus];
        }
        this.docsDevSwAdminStatus = docsDevSwAdminStatus;
    }

    public Integer getDocsDevSwOperStatus() {
        return docsDevSwOperStatus;
    }

    public void setDocsDevSwOperStatus(Integer docsDevSwOperStatus) {
        if (docsDevSwOperStatus != null && docsDevSwOperStatus < OPERSTATUSTYPES.length) {
            docsDevSwOperStatusString = OPERSTATUSTYPES[docsDevSwOperStatus];
        }
        this.docsDevSwOperStatus = docsDevSwOperStatus;
    }

    public String getDocsDevSwCurrentVers() {
        return docsDevSwCurrentVers;
    }

    public void setDocsDevSwCurrentVers(String docsDevSwCurrentVers) {
        this.docsDevSwCurrentVers = docsDevSwCurrentVers;
    }

    public Integer getDocsDevSwServerAddressType() {
        return docsDevSwServerAddressType;
    }

    public void setDocsDevSwServerAddressType(Integer docsDevSwServerAddressType) {
        this.docsDevSwServerAddressType = docsDevSwServerAddressType;
    }

    public String getDocsDevSwServerAddress() {
        return docsDevSwServerAddress;
    }

    public void setDocsDevSwServerAddress(String docsDevSwServerAddress) {
        this.docsDevSwServerAddress = docsDevSwServerAddress;
    }

    public Long getDocsDevSwServerAddressLong() {
        return docsDevSwServerAddressLong;
    }

    public void setDocsDevSwServerAddressLong(Long docsDevSwServerAddressLong) {
        this.docsDevSwServerAddressLong = docsDevSwServerAddressLong;
    }

    public Integer getDocsDevSwServerTransportProtocol() {
        return docsDevSwServerTransportProtocol;
    }

    public void setDocsDevSwServerTransportProtocol(Integer docsDevSwServerTransportProtocol) {
        if (docsDevSwServerTransportProtocol != null && docsDevSwServerTransportProtocol < PROTOCOLTYPES.length) {
            docsDevSwServerTransportProtocolString = PROTOCOLTYPES[docsDevSwServerTransportProtocol];
        }
        this.docsDevSwServerTransportProtocol = docsDevSwServerTransportProtocol;
    }

    public String getDocsDevSwAdminStatusString() {
        return docsDevSwAdminStatusString;
    }

    public void setDocsDevSwAdminStatusString(String docsDevSwAdminStatusString) {
        this.docsDevSwAdminStatusString = docsDevSwAdminStatusString;
    }

    public String getDocsDevSwOperStatusString() {
        return docsDevSwOperStatusString;
    }

    public void setDocsDevSwOperStatusString(String docsDevSwOperStatusString) {
        this.docsDevSwOperStatusString = docsDevSwOperStatusString;
    }

    public String getDocsDevSwServerTransportProtocolString() {
        return docsDevSwServerTransportProtocolString;
    }

    public void setDocsDevSwServerTransportProtocolString(String docsDevSwServerTransportProtocolString) {
        this.docsDevSwServerTransportProtocolString = docsDevSwServerTransportProtocolString;
    }

    public Integer getTopCcmtsSwTftpTransferFileType() {
        return topCcmtsSwTftpTransferFileType;
    }

    public void setTopCcmtsSwTftpTransferFileType(Integer topCcmtsSwTftpTransferFileType) {
        if (topCcmtsSwTftpTransferFileType != null && topCcmtsSwTftpTransferFileType < FILETYPES.length) {
            topCcmtsSwTftpTransferFileTypeString = FILETYPES[topCcmtsSwTftpTransferFileType];
        }
        this.topCcmtsSwTftpTransferFileType = topCcmtsSwTftpTransferFileType;
    }

    public Integer getTopCcmtsSwTftpTransferAction() {
        return topCcmtsSwTftpTransferAction;
    }

    public void setTopCcmtsSwTftpTransferAction(Integer topCcmtsSwTftpTransferAction) {
        if (topCcmtsSwTftpTransferAction != null && topCcmtsSwTftpTransferAction < ACTIONTYPES.length) {
            topCcmtsSwTftpTransferActionString = ACTIONTYPES[topCcmtsSwTftpTransferAction];
        }
        this.topCcmtsSwTftpTransferAction = topCcmtsSwTftpTransferAction;
    }

    public Integer getTopCcmtsSwTftpTransferProgress() {
        return topCcmtsSwTftpTransferProgress;
    }

    public void setTopCcmtsSwTftpTransferProgress(Integer topCcmtsSwTftpTransferProgress) {
        this.topCcmtsSwTftpTransferProgress = topCcmtsSwTftpTransferProgress;
    }

    public static String[] getAdminstatustypes() {
        return ADMINSTATUSTYPES;
    }

    public static String[] getOperstatustypes() {
        return OPERSTATUSTYPES;
    }

    public String getTopCcmtsSwTftpTransferFileTypeString() {
        return topCcmtsSwTftpTransferFileTypeString;
    }

    public void setTopCcmtsSwTftpTransferFileTypeString(String topCcmtsSwTftpTransferFileTypeString) {
        this.topCcmtsSwTftpTransferFileTypeString = topCcmtsSwTftpTransferFileTypeString;
    }

    public String getTopCcmtsSwTftpTransferActionString() {
        return topCcmtsSwTftpTransferActionString;
    }

    public void setTopCcmtsSwTftpTransferActionString(String topCcmtsSwTftpTransferActionString) {
        this.topCcmtsSwTftpTransferActionString = topCcmtsSwTftpTransferActionString;
    }

    public static String[] getProtocoltypes() {
        return PROTOCOLTYPES;
    }

    public static String[] getFiletypes() {
        return FILETYPES;
    }

    public static String[] getActiontypes() {
        return ACTIONTYPES;
    }

    /**
     * @return the docsDevSwServerAddressOld
     */
    public String getDocsDevSwServerAddressOld() {
        return docsDevSwServerAddressOld;
    }

    /**
     * @param docsDevSwServerAddressOld
     *            the docsDevSwServerAddressOld to set
     */
    public void setDocsDevSwServerAddressOld(String docsDevSwServerAddressOld) {
        this.docsDevSwServerAddressOld = docsDevSwServerAddressOld;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("CmcDevSoftware [cmcId=");
        builder.append(cmcId);
        builder.append(", entityId=");
        builder.append(entityId);
        builder.append(", docsDevSwFilename=");
        builder.append(docsDevSwFilename);
        builder.append(", docsDevSwAdminStatus=");
        builder.append(docsDevSwAdminStatus);
        builder.append(", docsDevSwOperStatus=");
        builder.append(docsDevSwOperStatus);
        builder.append(", docsDevSwCurrentVers=");
        builder.append(docsDevSwCurrentVers);
        builder.append(", docsDevSwServerAddressType=");
        builder.append(docsDevSwServerAddressType);
        builder.append(", docsDevSwServerAddress=");
        builder.append(docsDevSwServerAddress);
        builder.append(", docsDevSwServerAddressOld=");
        builder.append(docsDevSwServerAddressOld);
        builder.append(", docsDevSwServerAddressLong=");
        builder.append(docsDevSwServerAddressLong);
        builder.append(", docsDevSwServerTransportProtocol=");
        builder.append(docsDevSwServerTransportProtocol);
        builder.append(", topCcmtsSwTftpTransferFileType=");
        builder.append(topCcmtsSwTftpTransferFileType);
        builder.append(", topCcmtsSwTftpTransferAction=");
        builder.append(topCcmtsSwTftpTransferAction);
        builder.append(", topCcmtsSwTftpTransferProgress=");
        builder.append(topCcmtsSwTftpTransferProgress);
        builder.append(", docsDevSwAdminStatusString=");
        builder.append(docsDevSwAdminStatusString);
        builder.append(", docsDevSwOperStatusString=");
        builder.append(docsDevSwOperStatusString);
        builder.append(", docsDevSwServerTransportProtocolString=");
        builder.append(docsDevSwServerTransportProtocolString);
        builder.append(", topCcmtsSwTftpTransferFileTypeString=");
        builder.append(topCcmtsSwTftpTransferFileTypeString);
        builder.append(", topCcmtsSwTftpTransferActionString=");
        builder.append(topCcmtsSwTftpTransferActionString);
        builder.append("]");
        return builder.toString();
    }

}