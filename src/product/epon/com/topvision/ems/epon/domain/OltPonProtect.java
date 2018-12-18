/***********************************************************************
 * $ OltPonProtect.java,v1.0 2011-12-14 08:45:39 $
 *
 * @author: lizongtian
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.domain;

import java.io.Serializable;

import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author lizongtian
 * @created @2011-12-14 08:45:39
 */
public class OltPonProtect implements Serializable, AliasesSuperType {

    private static final long serialVersionUID = 8970688733892338434L;
    private Long entityId;
    private Integer protectId;
    private Long ponIdMaster;
    private Long ponIdReserve;
    private String protectName;
    private Long ponIdMasterIndex;
    private Long ponIdReserveIndex;
    private String portMasterString;
    private String portReserveString;

    /**
     * @return the entityiId
     */
    public Long getEntityId() {
        return entityId;
    }

    /**
     * @return the protectId
     */
    public Integer getProtectId() {
        return protectId;
    }

    /**
     * @param protectId
     *            the protectId to set
     */
    public void setProtectId(Integer protectId) {
        this.protectId = protectId;
    }

    /**
     * @return the ponIdMaster
     */
    public Long getPonIdMaster() {
        return ponIdMaster;
    }

    /**
     * @param ponIdMaster
     *            the ponIdMaster to set
     */
    public void setPonIdMaster(Long ponIdMaster) {
        this.ponIdMaster = ponIdMaster;
    }

    /**
     * @return the ponIdReserve
     */
    public Long getPonIdReserve() {
        return ponIdReserve;
    }

    /**
     * @param ponIdReserve
     *            the ponIdReserve to set
     */
    public void setPonIdReserve(Long ponIdReserve) {
        this.ponIdReserve = ponIdReserve;
    }

    /**
     * @return the protectName
     */
    public String getProtectName() {
        return protectName;
    }

    /**
     * @param protectName
     *            the protectName to set
     */
    public void setProtectName(String protectName) {
        this.protectName = protectName;
    }

    /**
     * @param entityId
     *            the entityId to set
     */
    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    /**
     * @return the portMasterString
     */
    public String getPortMasterString() {
        return portMasterString;
    }

    /**
     * @param portMasterString
     *            the portMasterString to set
     */
    public void setPortMasterString(String portMasterString) {
        this.portMasterString = portMasterString;
    }

    /**
     * @return the portReserveString
     */
    public String getPortReserveString() {
        return portReserveString;
    }

    /**
     * @param portReserveString
     *            the portReserveString to set
     */
    public void setPortReserveString(String portReserveString) {
        this.portReserveString = portReserveString;
    }

    /**
     * @return the ponIdMasterIndex
     */
    public Long getPonIdMasterIndex() {
        return ponIdMasterIndex;
    }

    /**
     * @param ponIdMasterIndex
     *            the ponIdMasterIndex to set
     */
    public void setPonIdMasterIndex(Long ponIdMasterIndex) {
        this.ponIdMasterIndex = ponIdMasterIndex;
    }

    /**
     * @return the ponIdReserveIndex
     */
    public Long getPonIdReserveIndex() {
        return ponIdReserveIndex;
    }

    /**
     * @param ponIdReserveIndex
     *            the ponIdReserveIndex to set
     */
    public void setPonIdReserveIndex(Long ponIdReserveIndex) {
        this.ponIdReserveIndex = ponIdReserveIndex;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "OltPonProtect [entityId=" + entityId + ", protectId=" + protectId + ", ponIdMaster=" + ponIdMaster
                + ", ponIdReserve=" + ponIdReserve + ", protectName=" + protectName + ", ponIdMasterIndex="
                + ponIdMasterIndex + ", ponIdReserveIndex=" + ponIdReserveIndex + ", portMasterString="
                + portMasterString + ", portReserveString=" + portReserveString + "]";
    }

}
