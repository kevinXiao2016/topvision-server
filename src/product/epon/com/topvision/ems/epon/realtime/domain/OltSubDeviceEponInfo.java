/***********************************************************************
 * $Id: OltSubDeviceInfo.java,v1.0 2014-7-12 下午5:05:08 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.realtime.domain;

import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.annotation.TableProperty;
import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author flack
 * @created @2014-7-12-下午5:05:08
 *
 */
@TableProperty(tables = { "default", "productInfo" })
public class OltSubDeviceEponInfo implements OltOnuBaseInfo {
    private static final long serialVersionUID = 2863504899465113078L;

    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.4.1.1.1", type = "Integer32", index = true)
    private Long onuDeviceIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.4.1.1.2", type = "OctetString")
    private String onuName;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.4.1.1.7", type = "OctetString")
    private String identifyKey;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.4.1.1.8", type = "Integer32")
    private Integer operationStatus;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.4.1.1.15", type = "Integer32")
    private Integer testDistance;


    @SnmpProperty(table = "productInfo", oid = "1.3.6.1.4.1.32285.11.2.3.4.2.3.1.1", type = "Integer32", index = true)
    private Integer slotIndex;
    @SnmpProperty(table = "productInfo", oid = "1.3.6.1.4.1.32285.11.2.3.4.2.3.1.2", type = "Integer32", index = true)
    private Integer ponIndex;
    @SnmpProperty(table = "productInfo", oid = "1.3.6.1.4.1.32285.11.2.3.4.2.3.1.3", type = "Integer32", index = true)
    private Integer onuProductIndex;
    @SnmpProperty(table = "productInfo", oid = "1.3.6.1.4.1.32285.11.2.3.4.2.3.1.4", type = "Integer32")
    private Integer onuType;

    public Long getOnuDeviceIndex() {
        return onuDeviceIndex;
    }

    public void setOnuDeviceIndex(Long onuDeviceIndex) {
        this.onuDeviceIndex = onuDeviceIndex;
    }

    public String getOnuName() {
        return onuName;
    }

    public void setOnuName(String onuName) {
        this.onuName = onuName;
    }

    public String getIdentifyKey() {
        return identifyKey;
    }

    public void setIdentifyKey(String identifyKey) {
        this.identifyKey = identifyKey;
    }

    public Integer getOperationStatus() {
        return operationStatus;
    }

    public void setOperationStatus(Integer operationStatus) {
        this.operationStatus = operationStatus;
    }

    public Integer getTestDistance() {
        return testDistance;
    }

    public void setTestDistance(Integer testDistance) {
        this.testDistance = testDistance;
    }

    public Integer getSlotIndex() {
        return slotIndex;
    }

    public void setSlotIndex(Integer slotIndex) {
        this.slotIndex = slotIndex;
    }

    public Integer getPonIndex() {
        return ponIndex;
    }

    public void setPonIndex(Integer ponIndex) {
        this.ponIndex = ponIndex;
    }

    public Integer getOnuProductIndex() {
        return onuProductIndex;
    }

    public void setOnuProductIndex(Integer onuProductIndex) {
        this.onuProductIndex = onuProductIndex;
    }

    public Integer getOnuType() {
        return onuType;
    }

    public void setOnuType(Integer onuType) {
        this.onuType = onuType;
    }
}
