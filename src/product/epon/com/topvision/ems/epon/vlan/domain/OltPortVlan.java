/***********************************************************************
 * $Id: OltSniPortVlan.java,v1.0 2016年6月8日 上午10:34:48 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.vlan.domain;

import java.util.ArrayList;
import java.util.List;

import com.topvision.framework.dao.mybatis.AliasesSuperType;
import com.topvision.framework.utils.EponConstants;
import com.topvision.framework.utils.EponIndex;

/**
 * @author Bravin
 * @created @2016年6月8日-上午10:34:48
 *
 */
public class OltPortVlan implements AliasesSuperType {
    private static final long serialVersionUID = 3821539793676868211L;
    private Long portIndex;
    private Long entityId;
    private Long portId;
    private String sniPortName;
    private List<Integer> tagVlanList = new ArrayList<Integer>();
    private List<Integer> untagVlanList = new ArrayList<Integer>();
    private String tpid;
    private Integer priority;
    private Integer vlanPvid;
    private Integer vlanMode;
    private String bName;
    private Integer slotType;

    public Long getPortIndex() {
        return portIndex;
    }

    public void setPortIndex(Long portIndex) {
        this.portIndex = portIndex;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public String getSniPortName() {
        return sniPortName;
    }

    public void setSniPortName(String sniPortName) {
        this.sniPortName = sniPortName;
    }

    public String getPortDisplay() {
        return String.format("%s(%s)", EponIndex.getPortStringByIndex(portIndex), bName.toUpperCase());
    }

    public List<Integer> getTagVlanList() {
        return tagVlanList;
    }

    public List<Integer> getUntagVlanList() {
        return untagVlanList;
    }

    public String getTpid() {
        return tpid;
    }

    public void setTpid(String tpid) {
        this.tpid = tpid;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public Integer getVlanPvid() {
        return vlanPvid;
    }

    public void setVlanPvid(Integer vlanPvid) {
        this.vlanPvid = vlanPvid;
    }

    public Integer getVlanMode() {
        return vlanMode;
    }

    public void setVlanMode(Integer vlanMode) {
        this.vlanMode = vlanMode;
    }

    /**
     * @param vlanIndex
     */
    public void addPortUntagVlan(Integer vlanIndex) {
        untagVlanList.add(vlanIndex);
    }

    /**
     * @param vlanIndex
     */
    public void addPortTagVlan(Integer vlanIndex) {
        tagVlanList.add(vlanIndex);
    }

    public String getbName() {
        return bName;
    }

    public void setbName(String bName) {
        this.bName = bName;
    }

    public Long getPortId() {
        return portId;
    }

    public void setPortId(Long portId) {
        this.portId = portId;
    }

    public Integer getSlotType() {
        return slotType;
    }

    public void setSlotType(Integer slotType) {
        this.slotType = slotType;
        if (slotType != null) {
            this.bName = EponConstants.SLOT_TYPE[slotType];
        } else {
            this.bName = "--";
        }
    }

}
