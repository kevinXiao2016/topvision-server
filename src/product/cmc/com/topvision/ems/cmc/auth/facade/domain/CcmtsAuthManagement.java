/***********************************************************************
 * $Id: CcmtsAuthManagement.java,v1.0 2012-10-9 上午09:59:11 $
 * 
 * @author: dosion
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.auth.facade.domain;

import java.io.Serializable;

import org.apache.ibatis.type.Alias;

import com.topvision.framework.constants.Symbol;
import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author dosion
 * @created @2012-10-9-上午09:59:11
 * 
 */
@Alias("ccmtsAuthManagement")
public class CcmtsAuthManagement  implements Serializable, AliasesSuperType{
    private static final long serialVersionUID = -7431290868729262540L;
    private Long cmcId;
    @SnmpProperty(oid = "1.3.6.1.2.1.2.2.1.1", index = true)
    private Long ifIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.8.1.1.1", writable = true, type = "Integer32")
    private Integer topCcmtsAuthTransferProtocol;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.8.1.1.2", writable = true, type = "OctetString")
    private String topCcmtsAuthFileURI;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.8.1.1.3")
    private String topCcmtsAuthCurrentInfo;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.8.1.1.4", writable = true, type = "Integer32")
    private Integer topCcmtsAuthApply;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.8.1.1.5")
    private Integer topCcmtsAuthApplyError;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.8.1.1.6")
    private Integer topCcmtsAuthApplyStatus;
    public static String[] status = { "init", "getAUpdateCmd", "startTftp", "tftpGetOk", "tftpGetError",
            "startSendToCmc", "sendingToCmc", "sendToCmcOk", "sendToCmcError", "success" };
    private String ipAddress;
    private String fileName;

    /**
     * @return the cmcId
     */
    public Long getCmcId() {
        return cmcId;
    }

    /**
     * @param cmcId
     *            the cmcId to set
     */
    public void setCmcId(Long cmcId) {
        this.cmcId = cmcId;
    }

    /**
     * @return the ifIndex
     */
    public Long getIfIndex() {
        return ifIndex;
    }

    /**
     * @param ifIndex
     *            the ifIndex to set
     */
    public void setIfIndex(Long ifIndex) {
        this.ifIndex = ifIndex;
    }

    /**
     * @return the topCcmtsAuthTransferProtocol
     */
    public Integer getTopCcmtsAuthTransferProtocol() {
        return topCcmtsAuthTransferProtocol;
    }

    /**
     * @param topCcmtsAuthTransferProtocol
     *            the topCcmtsAuthTransferProtocol to set
     */
    public void setTopCcmtsAuthTransferProtocol(Integer topCcmtsAuthTransferProtocol) {
        this.topCcmtsAuthTransferProtocol = topCcmtsAuthTransferProtocol;
    }

    /**
     * @return the topCcmtsAuthFileURI
     */
    public String getTopCcmtsAuthFileURI() {
        return topCcmtsAuthFileURI;
    }

    /**
     * @param topCcmtsAuthFileURI
     *            the topCcmtsAuthFileURI to set
     */
    public void setTopCcmtsAuthFileURI(String topCcmtsAuthFileURI) {
        this.topCcmtsAuthFileURI = topCcmtsAuthFileURI;
    }

    /**
     * @return the topCcmtsAuthCurrentInfo
     */
    public String getTopCcmtsAuthCurrentInfo() {
        return topCcmtsAuthCurrentInfo;
    }

    /**
     * @param topCcmtsAuthCurrentInfo
     *            the topCcmtsAuthCurrentInfo to set
     */
    public void setTopCcmtsAuthCurrentInfo(String topCcmtsAuthCurrentInfo) {
        this.topCcmtsAuthCurrentInfo = topCcmtsAuthCurrentInfo;
    }

    /**
     * @return the topCcmtsAuthApply
     */
    public Integer getTopCcmtsAuthApply() {
        return topCcmtsAuthApply;
    }

    /**
     * @param topCcmtsAuthApply
     *            the topCcmtsAuthApply to set
     */
    public void setTopCcmtsAuthApply(Integer topCcmtsAuthApply) {
        this.topCcmtsAuthApply = topCcmtsAuthApply;
    }

    /**
     * @return the topCcmtsAuthApplyError
     */
    public Integer getTopCcmtsAuthApplyError() {
        return topCcmtsAuthApplyError;
    }

    /**
     * @param topCcmtsAuthApplyError
     *            the topCcmtsAuthApplyError to set
     */
    public void setTopCcmtsAuthApplyError(Integer topCcmtsAuthApplyError) {
        this.topCcmtsAuthApplyError = topCcmtsAuthApplyError;
    }

    /**
     * @return the topCcmtsAuthApplyStatus
     */
    public Integer getTopCcmtsAuthApplyStatus() {
        return topCcmtsAuthApplyStatus;
    }

    /**
     * @param topCcmtsAuthApplyStatus
     *            the topCcmtsAuthApplyStatus to set
     */
    public void setTopCcmtsAuthApplyStatus(Integer topCcmtsAuthApplyStatus) {
        this.topCcmtsAuthApplyStatus = topCcmtsAuthApplyStatus;
    }

    /**
     * @return the ipAddress
     */
    public String getIpAddress() {
        return ipAddress;
    }

    /**
     * @param ipAddress
     *            the ipAddress to set
     */
    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
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
    
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("cmcAuth=[ifIndex=");
        builder.append(ifIndex);
        builder.append(", cmcId=");
        builder.append(cmcId);
        builder.append(", topCcmtsAuthTransferProtocol=");
        builder.append(topCcmtsAuthTransferProtocol);
        builder.append(", topCcmtsAuthFileURI=");
        builder.append(topCcmtsAuthFileURI);
        builder.append(", topCcmtsAuthCurrentInfo=");
        builder.append(topCcmtsAuthCurrentInfo);
        builder.append(", topCcmtsAuthApply=");
        builder.append(topCcmtsAuthApply);
        builder.append(", topCcmtsAuthApplyError=");
        builder.append(topCcmtsAuthApplyError);
        builder.append(", topCcmtsAuthApplyStatus=");
        builder.append(topCcmtsAuthApplyStatus);
        builder.append(", ipAddress=");
        builder.append(ipAddress);
        builder.append(", fileName=");
        builder.append(fileName);
        builder.append(Symbol.BRACKET_RIGHT);
        return builder.toString();
    }

}
