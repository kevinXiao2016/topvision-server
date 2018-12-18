/***********************************************************************
 * $Id: LogicInterface.java,v1.0 2016年10月14日 上午10:11:25 $
 * 
 * @author: lzt
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.logicinterface.domain;

import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author lzt
 * @created @2016年10月14日-上午10:11:25
 *
 */
public class LogicInterface implements AliasesSuperType{

    private static final long serialVersionUID = -1186568659256154982L;
    public static final int VLANIF_TYPE = 1;
    public static final int LOOPBACK_TYPE = 2;
    public static final int OUTBAND_TYPE = 3;
    private Long entityId;
    // Interface type:1->vlanif,2->loopback,3->outband
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.11.2.1.1.1", index = true)
    private Integer interfaceType;
    // Interface ID,vlanif ID from 1 to 4096 and loopback ID from 1 to 8 and outband id is 0.
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.11.2.1.1.2", index = true)
    private Integer interfaceId;
    /*
     * Interface Ifindex:for vlanif,from 3073 to 7169,total 4096,for loopback,form 11265 to
     * 11273,total 8.
     */
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.11.2.1.1.3", writable = true, type = "Integer32")
    private Integer interfaceIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.11.2.1.1.4", type = "OctetString")
    private String interfaceName;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.11.2.1.1.5", writable = true, type = "OctetString")
    private String interfaceDesc;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.11.2.1.1.6", writable = true, type = "Integer32")
    private Integer interfaceAdminStatus;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.11.2.1.1.7", writable = true, type = "Integer32")
    private Integer interfaceOperateStatus;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.11.2.1.1.8", type = "IpAddress")
    private String interfaceMac;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.11.2.1.1.9", writable = true, type = "Integer32")
    private Integer interfaceRowStatus;

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public Integer getInterfaceType() {
        return interfaceType;
    }

    public void setInterfaceType(Integer interfaceType) {
        this.interfaceType = interfaceType;
    }

    public Integer getInterfaceId() {
        return interfaceId;
    }

    public void setInterfaceId(Integer interfaceId) {
        this.interfaceId = interfaceId;
    }

    public Integer getInterfaceIndex() {
        return interfaceIndex;
    }

    public void setInterfaceIndex(Integer interfaceIndex) {
        this.interfaceIndex = interfaceIndex;
    }

    public String getInterfaceName() {
        return interfaceName;
    }

    public void setInterfaceName(String interfaceName) {
        this.interfaceName = interfaceName;
    }

    public String getInterfaceDesc() {
        return interfaceDesc;
    }

    public void setInterfaceDesc(String interfaceDesc) {
        this.interfaceDesc = interfaceDesc;
    }

    public Integer getInterfaceAdminStatus() {
        return interfaceAdminStatus;
    }

    public void setInterfaceAdminStatus(Integer interfaceAdminStatus) {
        this.interfaceAdminStatus = interfaceAdminStatus;
    }

    public Integer getInterfaceOperateStatus() {
        return interfaceOperateStatus;
    }

    public void setInterfaceOperateStatus(Integer interfaceOperateStatus) {
        this.interfaceOperateStatus = interfaceOperateStatus;
    }

    public String getInterfaceMac() {
        return interfaceMac;
    }

    public void setInterfaceMac(String interfaceMac) {
        this.interfaceMac = interfaceMac;
    }

    public Integer getInterfaceRowStatus() {
        return interfaceRowStatus;
    }

    public void setInterfaceRowStatus(Integer interfaceRowStatus) {
        this.interfaceRowStatus = interfaceRowStatus;
    }

}
