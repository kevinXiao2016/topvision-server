/***********************************************************************
 * $Id: TopCcmtsSfFileObject.java,v1.0 2016年12月5日 下午2:21:18 $
 * 
 * @author: Rod John
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cm.upgrade.facade.domain;

import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author Rod John
 * @created @2016年12月5日-下午2:21:18
 *
 */
public class TopCcmtsSfFileObject implements AliasesSuperType {

    private static final long serialVersionUID = 3048729360104941845L;
    private Long entityId;
    private Long cmcId;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.5.1.1.0", writable = true, type = "Integer32")
    private Integer topCcmtsSfFileTransferType;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.5.1.2.0", writable = true, type = "IpAddress")
    private String topCcmtsSfFileServerIpAddr;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.5.1.3.0", writable = true, type = "OctetString")
    private String topCcmtsSfFileFtpUserName;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.5.1.4.0", writable = true, type = "OctetString")
    private String topCcmtsSfFileFtpUserPassword;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.5.1.5.0", writable = true, type = "OctetString")
    private String topCcmtsSfFileSrcPath;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.5.1.6.0", writable = true, type = "OctetString")
    private String topCcmtsSfFileDestPath;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.5.1.7.0", writable = true, type = "Integer32")
    private Integer topCcmtsSfFileTransferAction; 

    /**
     * @return the entityId
     */
    public Long getEntityId() {
        return entityId;
    }

    /**
     * @param entityId the entityId to set
     */
    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    /**
     * @return the cmcId
     */
    public Long getCmcId() {
        return cmcId;
    }

    /**
     * @param cmcId the cmcId to set
     */
    public void setCmcId(Long cmcId) {
        this.cmcId = cmcId;
    }

    /**
     * @return the topCcmtsSfFileTransferType
     */
    public Integer getTopCcmtsSfFileTransferType() {
        return topCcmtsSfFileTransferType;
    }

    /**
     * @param topCcmtsSfFileTransferType the topCcmtsSfFileTransferType to set
     */
    public void setTopCcmtsSfFileTransferType(Integer topCcmtsSfFileTransferType) {
        this.topCcmtsSfFileTransferType = topCcmtsSfFileTransferType;
    }

    /**
     * @return the topCcmtsSfFileServerIpAddr
     */
    public String getTopCcmtsSfFileServerIpAddr() {
        return topCcmtsSfFileServerIpAddr;
    }

    /**
     * @param topCcmtsSfFileServerIpAddr the topCcmtsSfFileServerIpAddr to set
     */
    public void setTopCcmtsSfFileServerIpAddr(String topCcmtsSfFileServerIpAddr) {
        this.topCcmtsSfFileServerIpAddr = topCcmtsSfFileServerIpAddr;
    }

    /**
     * @return the topCcmtsSfFileFtpUserName
     */
    public String getTopCcmtsSfFileFtpUserName() {
        return topCcmtsSfFileFtpUserName;
    }

    /**
     * @param topCcmtsSfFileFtpUserName the topCcmtsSfFileFtpUserName to set
     */
    public void setTopCcmtsSfFileFtpUserName(String topCcmtsSfFileFtpUserName) {
        this.topCcmtsSfFileFtpUserName = topCcmtsSfFileFtpUserName;
    }

    /**
     * @return the topCcmtsSfFileFtpUserPassword
     */
    public String getTopCcmtsSfFileFtpUserPassword() {
        return topCcmtsSfFileFtpUserPassword;
    }

    /**
     * @param topCcmtsSfFileFtpUserPassword the topCcmtsSfFileFtpUserPassword to set
     */
    public void setTopCcmtsSfFileFtpUserPassword(String topCcmtsSfFileFtpUserPassword) {
        this.topCcmtsSfFileFtpUserPassword = topCcmtsSfFileFtpUserPassword;
    }

    /**
     * @return the topCcmtsSfFileSrcPath
     */
    public String getTopCcmtsSfFileSrcPath() {
        return topCcmtsSfFileSrcPath;
    }

    /**
     * @param topCcmtsSfFileSrcPath the topCcmtsSfFileSrcPath to set
     */
    public void setTopCcmtsSfFileSrcPath(String topCcmtsSfFileSrcPath) {
        this.topCcmtsSfFileSrcPath = topCcmtsSfFileSrcPath;
    }

    /**
     * @return the topCcmtsSfFileDestPath
     */
    public String getTopCcmtsSfFileDestPath() {
        return topCcmtsSfFileDestPath;
    }

    /**
     * @param topCcmtsSfFileDestPath the topCcmtsSfFileDestPath to set
     */
    public void setTopCcmtsSfFileDestPath(String topCcmtsSfFileDestPath) {
        this.topCcmtsSfFileDestPath = topCcmtsSfFileDestPath;
    }

    /**
     * @return the topCcmtsSfFileTransferAction
     */
    public Integer getTopCcmtsSfFileTransferAction() {
        return topCcmtsSfFileTransferAction;
    }

    /**
     * @param topCcmtsSfFileTransferAction the topCcmtsSfFileTransferAction to set
     */
    public void setTopCcmtsSfFileTransferAction(Integer topCcmtsSfFileTransferAction) {
        this.topCcmtsSfFileTransferAction = topCcmtsSfFileTransferAction;
    }

}
