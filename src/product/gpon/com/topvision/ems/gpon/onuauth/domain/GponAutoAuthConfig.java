/***********************************************************************
 * $Id: GponAutoAuthOnu.java,v1.0 2016年12月19日 上午10:46:00 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.gpon.onuauth.domain;

import java.util.List;

import com.topvision.ems.gpon.utils.GponIndex;
import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author Bravin
 * @created @2016年12月19日-上午10:46:00
 *
 */
public class GponAutoAuthConfig implements AliasesSuperType {
    private static final long serialVersionUID = -6721901865560873342L;
    private Long entityId;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.8.4.8.1.1", index = true, type = "Integer32")
    private Integer authIndex;//取值范围1-256
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.8.4.8.1.2", type = "OctetString", writable = true)
    private String onuAutoAuthenPortlist;
    private List<Long> autoAuthPortList;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.8.4.8.1.3", type = "OctetString", writable = true)
    private String onuType;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.8.4.8.1.4", type = "Integer32", writable = true)
    private Integer ethNum;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.8.4.8.1.5", type = "Integer32", writable = true)
    private Integer wlanNum;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.8.4.8.1.6", type = "Integer32", writable = true)
    private Integer catvNum;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.8.4.8.1.7", type = "Integer32", writable = true)
    private Integer veipNum;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.8.4.8.1.8", type = "Integer32", writable = true)
    private Integer lineProfileId;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.8.4.8.1.9", type = "Integer32", writable = true)
    private Integer srvProfileId;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.8.4.8.1.10", type = "Integer32", writable = true)
    private Integer rowStatus;

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public Integer getAuthIndex() {
        return authIndex;
    }

    public void setAuthIndex(Integer authIndex) {
        this.authIndex = authIndex;
    }

    public String getOnuType() {
        return onuType;
    }

    public void setOnuType(String onuType) {
        this.onuType = onuType;
    }

    public Integer getEthNum() {
        return ethNum;
    }

    public void setEthNum(Integer ethNum) {
        this.ethNum = ethNum;
    }

    public Integer getWlanNum() {
        return wlanNum;
    }

    public void setWlanNum(Integer wlanNum) {
        this.wlanNum = wlanNum;
    }

    public Integer getCatvNum() {
        return catvNum;
    }

    public void setCatvNum(Integer catvNum) {
        this.catvNum = catvNum;
    }

    public Integer getVeipNum() {
        return veipNum;
    }

    public void setVeipNum(Integer veipNum) {
        this.veipNum = veipNum;
    }

    public Integer getLineProfileId() {
        return lineProfileId;
    }

    public void setLineProfileId(Integer lineProfileId) {
        this.lineProfileId = lineProfileId;
    }

    public Integer getSrvProfileId() {
        return srvProfileId;
    }

    public void setSrvProfileId(Integer srvProfileId) {
        this.srvProfileId = srvProfileId;
    }

    public String getOnuAutoAuthenPortlist() {
        return onuAutoAuthenPortlist;
    }

    public void setOnuAutoAuthenPortlist(String onuAutoAuthenPortlist) {
        this.onuAutoAuthenPortlist = onuAutoAuthenPortlist;
    }

    public List<Long> getAutoAuthPortList() {
        return autoAuthPortList;
    }

    public void setAutoAuthPortList(List<Long> autoAuthPortList) {
        this.autoAuthPortList = autoAuthPortList;
        onuAutoAuthenPortlist = GponIndex.getMibStringFromGponPorts(autoAuthPortList);
    }

    public Integer getRowStatus() {
        return rowStatus;
    }

    public void setRowStatus(Integer rowStatus) {
        this.rowStatus = rowStatus;
    }

    @Override
    public String toString() {
        return "GponAutoAuthConfig [entityId=" + entityId + ", authIndex=" + authIndex + ", onuAutoAuthenPortlist="
                + onuAutoAuthenPortlist + ", autoAuthPortList=" + autoAuthPortList + ", onuType=" + onuType
                + ", ethNum=" + ethNum + ", wlanNum=" + wlanNum + ", catvNum=" + catvNum + ", veipNum=" + veipNum
                + ", lineProfileId=" + lineProfileId + ", srvProfileId=" + srvProfileId + ", rowStatus=" + rowStatus
                + "]";
    }

}
