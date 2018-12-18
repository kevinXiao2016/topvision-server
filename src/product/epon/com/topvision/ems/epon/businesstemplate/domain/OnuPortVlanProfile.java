/***********************************************************************
 * $Id: OnuPortVlanProfile.java,v1.0 2015-12-8 下午2:40:28 $
 * 
 * @author: lizongtian
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.businesstemplate.domain;

import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author lizongtian
 * @created @2015-12-8-下午2:40:28
 *
 */
public class OnuPortVlanProfile implements AliasesSuperType {
    private static final long serialVersionUID = 6093115584616253069L;

    public static final String VLAN_PVID_PRI_SUPPORT_VERSION = "1.8.0.10";

    private Long entityId;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.4.16.2.1.1", index = true)
    private Integer vlanProfileId;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.4.16.2.1.2", index = true)
    private Integer srvPortId;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.4.16.2.1.3", writable = true, type = "Integer32")
    private Integer bindVlanProfile;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.4.16.2.1.4", writable = true, type = "Integer32")
    private Integer profileVlanPvid;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.4.16.2.1.5", writable = true, type = "Integer32")
    private Integer rowStatus;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.4.16.2.1.6", writable = true, type = "Integer32")
    private Integer profileVlanPvidPri;
    //业务模板绑定能力集和绑定次数数据
    private Integer srvBindCap;

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public Integer getVlanProfileId() {
        return vlanProfileId;
    }

    public void setVlanProfileId(Integer vlanProfileId) {
        this.vlanProfileId = vlanProfileId;
    }

    public Integer getSrvPortId() {
        return srvPortId;
    }

    public void setSrvPortId(Integer srvPortId) {
        this.srvPortId = srvPortId;
    }

    public Integer getBindVlanProfile() {
        return bindVlanProfile;
    }

    public void setBindVlanProfile(Integer bindVlanProfile) {
        this.bindVlanProfile = bindVlanProfile;
    }

    public Integer getProfileVlanPvid() {
        return profileVlanPvid;
    }

    public void setProfileVlanPvid(Integer profileVlanPvid) {
        this.profileVlanPvid = profileVlanPvid;
    }

    public Integer getRowStatus() {
        return rowStatus;
    }

    public void setRowStatus(Integer rowStatus) {
        this.rowStatus = rowStatus;
    }

    public Integer getSrvBindCap() {
        return srvBindCap;
    }

    public void setSrvBindCap(Integer srvBindCap) {
        this.srvBindCap = srvBindCap;
    }

    /**
     * @return the profileVlanPvidPri
     */
    public Integer getProfileVlanPvidPri() {
        if (profileVlanPvid == 0) {
            profileVlanPvidPri = 0;
        }
        return profileVlanPvidPri;
    }

    /**
     * @param profileVlanPvidPri the profileVlanPvidPri to set
     */
    public void setProfileVlanPvidPri(Integer profileVlanPvidPri) {
        this.profileVlanPvidPri = profileVlanPvidPri;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("OnuPortVlanProfile [entityId=");
        builder.append(entityId);
        builder.append(", vlanProfileId=");
        builder.append(vlanProfileId);
        builder.append(", srvPortId=");
        builder.append(srvPortId);
        builder.append(", bindVlanProfile=");
        builder.append(bindVlanProfile);
        builder.append(", profileVlanPvid=");
        builder.append(profileVlanPvid);
        builder.append(", rowStatus=");
        builder.append(rowStatus);
        builder.append(", srvBindCap=");
        builder.append(srvBindCap);
        builder.append("]");
        return builder.toString();
    }
}
