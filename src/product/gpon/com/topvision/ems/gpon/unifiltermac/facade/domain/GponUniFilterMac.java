/***********************************************************************
 * $Id: GponUniFilterMac.java,v1.0 2016年12月24日 下午6:16:43 $
 * 
 * @author: jay
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.gpon.unifiltermac.facade.domain;

import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.dao.mybatis.AliasesSuperType;
import com.topvision.framework.domain.PhysAddress;
import com.topvision.framework.utils.EponIndex;

/**
 * @author jay
 * @created @2016年12月24日-下午6:16:43
 *
 */
public class GponUniFilterMac implements AliasesSuperType {
    private static final long serialVersionUID = 6000792273449103948L;

    private Long entityId;
    private Long uniId;
    private Long uniIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.5.2.2.1.1.1", index = true)
    private Integer slotNo;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.5.2.2.1.1.2", index = true)
    private Integer ponNo;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.5.2.2.1.1.3", index = true)
    private Integer onuNo;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.5.2.2.1.1.4", index = true)
    private Integer uniNo;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.5.2.2.1.1.5", index = true)
    private PhysAddress macAddress;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.5.2.2.1.1.6", writable = true, type = "Integer32")
    private Integer rowStatus;
    private String macAddressString;

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public Long getUniId() {
        return uniId;
    }

    public void setUniId(Long uniId) {
        this.uniId = uniId;
    }

    public Long getUniIndex() {
        if (uniIndex == null) {
            uniIndex = EponIndex.getUniIndex(slotNo, ponNo, onuNo, 0, uniNo);
        }
        return uniIndex;
    }

    public void setUniIndex(Long uniIndex) {
        this.uniIndex = uniIndex;
        slotNo = EponIndex.getSlotNo(uniIndex).intValue();
        ponNo = EponIndex.getPonNo(uniIndex).intValue();
        onuNo = EponIndex.getOnuNo(uniIndex).intValue();
        uniNo = EponIndex.getUniNo(uniIndex).intValue();
    }

    public Integer getSlotNo() {
        return slotNo;
    }

    public void setSlotNo(Integer slotNo) {
        this.slotNo = slotNo;
    }

    public Integer getPonNo() {
        return ponNo;
    }

    public void setPonNo(Integer ponNo) {
        this.ponNo = ponNo;
    }

    public Integer getOnuNo() {
        return onuNo;
    }

    public void setOnuNo(Integer onuNo) {
        this.onuNo = onuNo;
    }

    public Integer getUniNo() {
        return uniNo;
    }

    public void setUniNo(Integer uniNo) {
        this.uniNo = uniNo;
    }

    public PhysAddress getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(PhysAddress macAddress) {
        this.macAddress = macAddress;
        this.macAddressString = macAddress.toString();
    }

    public Integer getRowStatus() {
        return rowStatus;
    }

    public void setRowStatus(Integer rowStatus) {
        this.rowStatus = rowStatus;
    }

    public String getMacAddressString() {
        return macAddressString;
    }

    public void setMacAddressString(String macAddressString) {
        this.macAddressString = macAddressString;
        this.macAddress = new PhysAddress(macAddressString);
    }
}
