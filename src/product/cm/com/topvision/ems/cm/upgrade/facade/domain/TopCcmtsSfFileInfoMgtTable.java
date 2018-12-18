/***********************************************************************
 * $Id: TopCcmtsSfFileInfoMgtTable.java,v1.0 2016年12月5日 下午2:16:48 $
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
 * @created @2016年12月5日-下午2:16:48
 *
 */
public class TopCcmtsSfFileInfoMgtTable implements AliasesSuperType {

    private static final long serialVersionUID = -346718730375964880L;
    // Syntax: [UNIVERSAL 2] INTEGER { kernel(1), rootfs(2), cmcapp(3), config(4), packimage(5), cmimage(6) }
    public static final Integer CM_IMAGE = 6;
    // Syntax: [UNIVERSAL 2] INTEGER { noOperation(1), erase(2) }
    public static final Integer ERASE = 2;

    private Long entityId;
    private Long cmcId;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.5.1.10.1.1", index = true)
    private Integer topCcmtsSfFileType;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.5.1.10.1.2", index = true)
    private String topCcmtsSfFileName;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.5.1.10.1.3")
    private Integer topCcmtsSfFileSize;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.5.1.10.1.4")
    private Long topCcmtsSfFileDate;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.5.1.10.1.5", writable = true, type = "Integer32")
    private Integer topCcmtsSfFileMgtAction;

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
     * @return the topCcmtsSfFileType
     */
    public Integer getTopCcmtsSfFileType() {
        return topCcmtsSfFileType;
    }

    /**
     * @param topCcmtsSfFileType the topCcmtsSfFileType to set
     */
    public void setTopCcmtsSfFileType(Integer topCcmtsSfFileType) {
        this.topCcmtsSfFileType = topCcmtsSfFileType;
    }

    /**
     * @return the topCcmtsSfFileName
     */
    public String getTopCcmtsSfFileName() {
        return topCcmtsSfFileName;
    }

    /**
     * @param topCcmtsSfFileName the topCcmtsSfFileName to set
     */
    public void setTopCcmtsSfFileName(String topCcmtsSfFileName) {
        this.topCcmtsSfFileName = topCcmtsSfFileName;
    }

    /**
     * @return the topCcmtsSfFileSize
     */
    public Integer getTopCcmtsSfFileSize() {
        return topCcmtsSfFileSize;
    }

    /**
     * @param topCcmtsSfFileSize the topCcmtsSfFileSize to set
     */
    public void setTopCcmtsSfFileSize(Integer topCcmtsSfFileSize) {
        this.topCcmtsSfFileSize = topCcmtsSfFileSize;
    }

    /**
     * @return the topCcmtsSfFileDate
     */
    public Long getTopCcmtsSfFileDate() {
        return topCcmtsSfFileDate;
    }

    /**
     * @param topCcmtsSfFileDate the topCcmtsSfFileDate to set
     */
    public void setTopCcmtsSfFileDate(Long topCcmtsSfFileDate) {
        this.topCcmtsSfFileDate = topCcmtsSfFileDate;
    }

    /**
     * @return the topCcmtsSfFileMgtAction
     */
    public Integer getTopCcmtsSfFileMgtAction() {
        return topCcmtsSfFileMgtAction;
    }

    /**
     * @param topCcmtsSfFileMgtAction the topCcmtsSfFileMgtAction to set
     */
    public void setTopCcmtsSfFileMgtAction(Integer topCcmtsSfFileMgtAction) {
        this.topCcmtsSfFileMgtAction = topCcmtsSfFileMgtAction;
    }

}
