/***********************************************************************
 * $Id: OltMacLearnTable.java,v1.0 2012-12-28 下午12:32:34 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.olt.domain;

import java.sql.Timestamp;

import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author Bravin
 * @created @2012-12-28-下午12:32:34
 * 
 */
public class OltMacAddressLearnTable implements AliasesSuperType {
    private static final long serialVersionUID = -8731779615567550717L;
    private Long entityId;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.1.8.2.1.1", index = true)
    private Integer topSysMacIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.1.8.2.1.2")
    private String topSysMacAddr;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.1.8.2.1.3")
    private Integer topSysMacVid;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.1.8.2.1.4")
    private Integer topSysMacSlot;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.1.8.2.1.5")
    private Integer topSysMacPort;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.1.8.2.1.6")
    private Integer topSysMacFlag;
    private Timestamp lastChangeTime;

    /**
     * @return the entityID
     */
    public Long getEntityId() {
        return entityId;
    }

    /**
     * @param entityID
     *            the entityID to set
     */
    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    /**
     * @return the topSysMacIndex
     */
    public Integer getTopSysMacIndex() {
        return topSysMacIndex;
    }

    /**
     * @param topSysMacIndex
     *            the topSysMacIndex to set
     */
    public void setTopSysMacIndex(Integer topSysMacIndex) {
        this.topSysMacIndex = topSysMacIndex;
    }

    /**
     * @return the topSysMacAddr
     */
    public String getTopSysMacAddr() {
        return topSysMacAddr;
    }

    /**
     * @param topSysMacAddr
     *            the topSysMacAddr to set
     */
    public void setTopSysMacAddr(String topSysMacAddr) {
        this.topSysMacAddr = topSysMacAddr;
    }

    /**
     * @return the topSysMacVid
     */
    public Integer getTopSysMacVid() {
        return topSysMacVid;
    }

    /**
     * @param topSysMacVid
     *            the topSysMacVid to set
     */
    public void setTopSysMacVid(Integer topSysMacVid) {
        this.topSysMacVid = topSysMacVid;
    }

    /**
     * @return the topSysMacSlot
     */
    public Integer getTopSysMacSlot() {
        return topSysMacSlot;
    }

    /**
     * @param topSysMacSlot
     *            the topSysMacSlot to set
     */
    public void setTopSysMacSlot(Integer topSysMacSlot) {
        this.topSysMacSlot = topSysMacSlot;
    }

    /**
     * @return the topSysMacPort
     */
    public Integer getTopSysMacPort() {
        return topSysMacPort;
    }

    /**
     * @param topSysMacPort
     *            the topSysMacPort to set
     */
    public void setTopSysMacPort(Integer topSysMacPort) {
        this.topSysMacPort = topSysMacPort;
    }

    /**
     * @return the topSysMacFlag
     */
    public Integer getTopSysMacFlag() {
        return topSysMacFlag;
    }

    /**
     * @param topSysMacFlag
     *            the topSysMacFlag to set
     */
    public void setTopSysMacFlag(Integer topSysMacFlag) {
        this.topSysMacFlag = topSysMacFlag;
    }

    /**
     * @return the lastChangeTime
     */
    public Timestamp getLastChangeTime() {
        return lastChangeTime;
    }

    /**
     * @param lastChangeTime
     *            the lastChangeTime to set
     */
    public void setLastChangeTime(Timestamp lastChangeTime) {
        this.lastChangeTime = lastChangeTime;
    }

}
