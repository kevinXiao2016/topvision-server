/***********************************************************************
 * $Id: OltPortVlanEntry.java,v1.0 2018年1月5日-上午10:34:48 $
 * 
 * @author: lzt
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.vlan.domain;

import java.io.Serializable;

import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.dao.mybatis.AliasesSuperType;
import com.topvision.framework.utils.EponIndex;

/**
 * @author lzt
 * @created @2018年1月5日-上午10:34:48
 *
 */
public class OltPortVlanEntry implements Serializable, AliasesSuperType {

    private static final long serialVersionUID = 6932723580798907846L;

    private Long entityId;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.7.3.5.1.1", index = true)
    private Long mibIndex;
    private Long portIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.7.3.5.1.2", writable = true, type = "Integer32")
    private Integer vlanTagPriority;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.7.3.5.1.3", writable = true, type = "Integer32")
    private Integer vlanPVid;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.7.3.5.1.4", writable = true, type = "Integer32")
    private Integer vlanMode;

    private Long portId;

    public Long getPortIndex() {
        return portIndex;
    }

    public void setPortIndex(Long portIndex) {
        if (portIndex != null) {
            mibIndex = EponIndex.getMibPonIndexIndexByPonIndex(portIndex);
        }
        this.portIndex = portIndex;
    }

    public Integer getVlanTagPriority() {
        return vlanTagPriority;
    }

    public void setVlanTagPriority(Integer vlanTagPriority) {
        this.vlanTagPriority = vlanTagPriority;
    }

    public Integer getVlanPVid() {
        return vlanPVid;
    }

    public void setVlanPVid(Integer vlanPVid) {
        this.vlanPVid = vlanPVid;
    }

    public Integer getVlanMode() {
        return vlanMode;
    }

    public Long getMibIndex() {
        return mibIndex;
    }

    public void setMibIndex(Long mibIndex) {
        this.mibIndex = mibIndex;
        this.portIndex = EponIndex.getPonIndexByMibDeviceIndex(mibIndex);
    }

    public void setVlanMode(Integer vlanMode) {
        this.vlanMode = vlanMode;
    }

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

    public Long getPortId() {
        return portId;
    }

    public void setPortId(Long portId) {
        this.portId = portId;
    }

}
