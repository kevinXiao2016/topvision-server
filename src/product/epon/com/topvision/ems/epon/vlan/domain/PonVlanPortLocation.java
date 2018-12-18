/***********************************************************************
 * $Id: PonVlanPortLocation.java,v1.0 2016年6月17日 上午11:42:55 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.vlan.domain;

import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author Bravin
 * @created @2016年6月17日-上午11:42:55
 *
 */
public class PonVlanPortLocation implements AliasesSuperType {
    private static final long serialVersionUID = -4144373107395076873L;

    private Long entityId;
    private Long ponId;
    private Long slotNo;
    private Long slotIndex;
    private Long ponIndex;
    private Integer topsysbdpreconfigtype;
    private String bname;

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public Long getPonId() {
        return ponId;
    }

    public void setPonId(Long ponId) {
        this.ponId = ponId;
    }

    public Long getSlotNo() {
        return slotNo;
    }

    public void setSlotNo(Long slotNo) {
        this.slotNo = slotNo;
    }

    public Long getSlotIndex() {
        return slotIndex;
    }

    public void setSlotIndex(Long slotIndex) {
        this.slotIndex = slotIndex;
    }

    public Long getPonIndex() {
        return ponIndex;
    }

    public void setPonIndex(Long ponIndex) {
        this.ponIndex = ponIndex;
    }

    public Integer getTopsysbdpreconfigtype() {
        return topsysbdpreconfigtype;
    }

    public void setTopsysbdpreconfigtype(Integer topsysbdpreconfigtype) {
        this.topsysbdpreconfigtype = topsysbdpreconfigtype;
    }

    public String getBname() {
        return bname;
    }

    public void setBname(String bname) {
        this.bname = bname;
    }

}
