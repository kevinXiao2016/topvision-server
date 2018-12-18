/***********************************************************************
 * $Id: CmCmcRelation.java,v1.0 2011-11-28 下午01:48:43 $
 * 
 * @author: loyal
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.domain;

import java.io.Serializable;

import org.apache.ibatis.type.Alias;

import com.topvision.framework.common.MacUtils;
import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author loyal
 * @created @2011-11-28-下午01:48:43
 * 
 */
@Alias("cmCmcRelation")
public class CmCmcRelation implements Serializable, AliasesSuperType {
    private static final long serialVersionUID = -8913491052589705539L;
    private Long cmId;
    private Long cmcId;
    private Long entityId;
    private Long cmIndex;
    private Long upPortId;
    private Long downPortId;
    private String mac;
    private Long maclong;

    /**
     * @return the cmId
     */
    public Long getCmId() {
        return cmId;
    }

    /**
     * @param cmId
     *            the cmId to set
     */
    public void setCmId(Long cmId) {
        this.cmId = cmId;
    }

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
     * @return the upPortId
     */
    public Long getUpPortId() {
        return upPortId;
    }

    /**
     * @param upPortId
     *            the upPortId to set
     */
    public void setUpPortId(Long upPortId) {
        this.upPortId = upPortId;
    }

    /**
     * @return the downPortId
     */
    public Long getDownPortId() {
        return downPortId;
    }

    /**
     * @param downPortId
     *            the downPortId to set
     */
    public void setDownPortId(Long downPortId) {
        this.downPortId = downPortId;
    }

    /**
     * @return the mac
     */
    public String getMac() {
        return mac;
    }

    /**
     * @param mac
     *            the mac to set
     */
    public void setMac(String mac) {
        mac = MacUtils.getMacStringFromNoISOControl(mac);
        this.mac = mac;
        this.maclong = new MacUtils(mac).longValue();
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */

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

    /**
     * @return the cmIndex
     */
    public Long getCmIndex() {
        return cmIndex;
    }

    /**
     * @param cmIndex
     *            the cmIndex to set
     */
    public void setCmIndex(Long cmIndex) {
        this.cmIndex = cmIndex;
    }

    /*public Long getMaclong() {
        return new MacUtils(mac).longValue();
    }

    public void setMaclong(Long maclong) {
        mac = new MacUtils(maclong).toString(MacUtils.MAOHAO).toUpperCase();
    }*/

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("CmCmcRelation");
        sb.append("{cmcId=").append(cmcId);
        sb.append(", cmId=").append(cmId);
        sb.append(", entityId=").append(entityId);
        sb.append(", cmIndex=").append(cmIndex);
        sb.append(", upPortId=").append(upPortId);
        sb.append(", downPortId=").append(downPortId);
        sb.append(", mac='").append(mac).append('\'');
        sb.append('}');
        return sb.toString();
    }

    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        CmCmcRelation that = (CmCmcRelation) o;

        if (!cmcId.equals(that.cmcId))
            return false;
        if (!entityId.equals(that.entityId))
            return false;
        if (!mac.equals(that.mac))
            return false;

        return true;
    }

    public int hashCode() {
        int result;
        result = cmcId.hashCode();
        result = 31 * result + entityId.hashCode();
        result = 31 * result + mac.hashCode();
        return result;
    }

    public Long getMaclong() {
        return maclong;
    }

    public void setMaclong(Long maclong) {
        this.maclong = maclong;
        this.mac = new MacUtils(maclong).toString(MacUtils.MAOHAO).toUpperCase();
    }
}
