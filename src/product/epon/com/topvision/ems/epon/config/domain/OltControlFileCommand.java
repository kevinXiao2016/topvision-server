/***********************************************************************
 * $Id: OltControlFileCommand.java,v1.0 2011-10-10 下午05:25:55 $
 * 
 * @author: huqiao
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.config.domain;

import com.topvision.framework.annotation.SnmpProperty;

import java.io.Serializable;

/**
 * @author huqiao
 * @created @2011-10-10-下午05:25:55
 * 
 */
public class OltControlFileCommand implements Serializable {
    private static final long serialVersionUID = 6211397324362208080L;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.1.6.1.1.1", index = true)
    private Integer fileTransferIndex = 1;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.1.6.1.1.2", writable = true, type = "Integer32")
    private Integer fileTransferProtocolType;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.1.6.1.1.3", writable = true, type = "IpAddress")
    private String serverIpAddress;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.1.6.1.1.4", writable = true, type = "OctetString")
    private String ftpUserName;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.1.6.1.1.5", writable = true, type = "OctetString")
    private String ftpUserPassword;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.1.6.1.1.6", writable = true, type = "OctetString")
    private String transferFileSrcNamePath;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.1.6.1.1.7", writable = true, type = "OctetString")
    private String transferFileDstNamePath;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.1.6.1.1.8", writable = true, type = "Integer32")
    private Integer transferAction;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.1.6.1.1.9")
    private Integer transferStatus;

    /**
     * @return the fileTransferProtocolType
     */
    public Integer getFileTransferProtocolType() {
        return fileTransferProtocolType;
    }

    /**
     * @param fileTransferProtocolType
     *            the fileTransferProtocolType to set
     */
    public void setFileTransferProtocolType(Integer fileTransferProtocolType) {
        this.fileTransferProtocolType = fileTransferProtocolType;
    }

    /**
     * @return the serverIpAddress
     */
    public String getServerIpAddress() {
        return serverIpAddress;
    }

    /**
     * @param serverIpAddress
     *            the serverIpAddress to set
     */
    public void setServerIpAddress(String serverIpAddress) {
        this.serverIpAddress = serverIpAddress;
    }

    /**
     * @return the ftpUserName
     */
    public String getFtpUserName() {
        return ftpUserName;
    }

    /**
     * @param ftpUserName
     *            the ftpUserName to set
     */
    public void setFtpUserName(String ftpUserName) {
        this.ftpUserName = ftpUserName;
    }

    /**
     * @return the ftpUserPassword
     */
    public String getFtpUserPassword() {
        return ftpUserPassword;
    }

    /**
     * @param ftpUserPassword
     *            the ftpUserPassword to set
     */
    public void setFtpUserPassword(String ftpUserPassword) {
        this.ftpUserPassword = ftpUserPassword;
    }

    /**
     * @return the transferFileSrcNamePath
     */
    public String getTransferFileSrcNamePath() {
        return transferFileSrcNamePath;
    }

    /**
     * @param transferFileSrcNamePath
     *            the transferFileSrcNamePath to set
     */
    public void setTransferFileSrcNamePath(String transferFileSrcNamePath) {
        this.transferFileSrcNamePath = transferFileSrcNamePath;
    }

    /**
     * @return the transferFileDstNamePath
     */
    public String getTransferFileDstNamePath() {
        return transferFileDstNamePath;
    }

    /**
     * @param transferFileDstNamePath
     *            the transferFileDstNamePath to set
     */
    public void setTransferFileDstNamePath(String transferFileDstNamePath) {
        this.transferFileDstNamePath = transferFileDstNamePath;
    }

    /**
     * @return the transferAction
     */
    public Integer getTransferAction() {
        return transferAction;
    }

    /**
     * @param transferAction
     *            the transferAction to set
     */
    public void setTransferAction(Integer transferAction) {
        this.transferAction = transferAction;
    }

    public Integer getFileTransferIndex() {
        return fileTransferIndex;
    }

    public void setFileTransferIndex(Integer fileTransferIndex) {
        this.fileTransferIndex = fileTransferIndex;
    }

    public Integer getTransferStatus() {
        return transferStatus;
    }

    public void setTransferStatus(Integer transferStatus) {
        this.transferStatus = transferStatus;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("OltControlFileCommand");
        sb.append("{fileTransferIndex=").append(fileTransferIndex);
        sb.append(", fileTransferProtocolType=").append(fileTransferProtocolType);
        sb.append(", serverIpAddress=").append(serverIpAddress);
        sb.append(", ftpUserName='").append(ftpUserName).append('\'');
        sb.append(", ftpUserPassword='").append(ftpUserPassword).append('\'');
        sb.append(", transferFileSrcNamePath='").append(transferFileSrcNamePath).append('\'');
        sb.append(", transferFileDstNamePath='").append(transferFileDstNamePath).append('\'');
        sb.append(", transferAction=").append(transferAction);
        sb.append(", transferStatus=").append(transferStatus);
        sb.append('}');
        return sb.toString();
    }
}
