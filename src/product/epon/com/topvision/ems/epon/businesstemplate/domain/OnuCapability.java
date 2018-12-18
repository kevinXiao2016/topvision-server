/***********************************************************************
 * $Id: OnuCapability.java,v1.0 2015-12-9 下午5:27:55 $
 * 
 * @author: lizongtian
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.businesstemplate.domain;

import java.io.Serializable;

import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author lizongtian
 * @created @2015-12-9-下午5:27:55
 *
 */
public class OnuCapability implements Serializable, AliasesSuperType {

    private static final long serialVersionUID = 3759663917419458088L;
    private Long entityId;

    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.4.17.1.1", index = true)
    private Integer capabilityId;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.4.17.1.2", writable = true, type = "Integer32")
    private Integer gePortNum;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.4.17.1.3", writable = true, type = "Integer32")
    private Integer fePortNum;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.4.17.1.4", writable = true, type = "Integer32")
    private Integer potsPortNum;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.4.17.1.5", writable = true, type = "Integer32")
    private Integer e1PortNum;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.4.17.1.6", writable = true, type = "Integer32")
    private Integer wlanPortNum;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.4.17.1.7", writable = true, type = "Integer32")
    private Integer catvPortNum;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.4.17.1.8", writable = true, type = "Integer32")
    private Integer uartPortNum;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.4.17.1.9", writable = true, type = "Integer32")
    private Integer rowStatus;

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public Integer getCapabilityId() {
        return capabilityId;
    }

    public void setCapabilityId(Integer capabilityId) {
        this.capabilityId = capabilityId;
    }

    public Integer getGePortNum() {
        return gePortNum;
    }

    public void setGePortNum(Integer gePortNum) {
        this.gePortNum = gePortNum;
    }

    public Integer getFePortNum() {
        return fePortNum;
    }

    public void setFePortNum(Integer fePortNum) {
        this.fePortNum = fePortNum;
    }

    public Integer getPotsPortNum() {
        return potsPortNum;
    }

    public void setPotsPortNum(Integer potsPortNum) {
        this.potsPortNum = potsPortNum;
    }

    public Integer getE1PortNum() {
        return e1PortNum;
    }

    public void setE1PortNum(Integer e1PortNum) {
        this.e1PortNum = e1PortNum;
    }

    public Integer getWlanPortNum() {
        return wlanPortNum;
    }

    public void setWlanPortNum(Integer wlanPortNum) {
        this.wlanPortNum = wlanPortNum;
    }

    public Integer getCatvPortNum() {
        return catvPortNum;
    }

    public void setCatvPortNum(Integer catvPortNum) {
        this.catvPortNum = catvPortNum;
    }

    public Integer getUartPortNum() {
        return uartPortNum;
    }

    public void setUartPortNum(Integer uartPortNum) {
        this.uartPortNum = uartPortNum;
    }

    public Integer getRowStatus() {
        return rowStatus;
    }

    public void setRowStatus(Integer rowStatus) {
        this.rowStatus = rowStatus;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("OnuCapability [entityId=");
        builder.append(entityId);
        builder.append(", capabilityId=");
        builder.append(capabilityId);
        builder.append(", gePortNum=");
        builder.append(gePortNum);
        builder.append(", fePortNum=");
        builder.append(fePortNum);
        builder.append(", potsPortNum=");
        builder.append(potsPortNum);
        builder.append(", e1PortNum=");
        builder.append(e1PortNum);
        builder.append(", wlanPortNum=");
        builder.append(wlanPortNum);
        builder.append(", catvPortNum=");
        builder.append(catvPortNum);
        builder.append(", uartPortNum=");
        builder.append(uartPortNum);
        builder.append(", rowStatus=");
        builder.append(rowStatus);
        builder.append("]");
        return builder.toString();
    }

}
