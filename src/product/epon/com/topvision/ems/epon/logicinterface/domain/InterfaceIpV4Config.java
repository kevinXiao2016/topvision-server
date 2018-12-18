/***********************************************************************
 * $Id: InterfaceIpV4Config.java,v1.0 2016年10月14日 上午10:57:51 $
 * 
 * @author: lzt
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.logicinterface.domain;

import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.dao.mybatis.AliasesSuperType;
import com.topvision.framework.domain.IpsAddress;

/**
 * @author lzt
 * @created @2016年10月14日-上午10:57:51
 *
 */
public class InterfaceIpV4Config implements AliasesSuperType{

    private static final long serialVersionUID = 7610793153456539630L;
    private Long entityId;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.11.3.1.1.1", index = true)
    private Integer ipV4ConfigIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.11.3.1.1.2", index = true)
    private IpsAddress ipV4AddrDevice;
    private String ipV4Addr;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.11.3.1.1.3", writable = true, type = "IpAddress")
    private String ipV4NetMask;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.11.3.1.1.4", writable = true, type = "Integer32")
    private Integer ipV4AddrType;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.11.3.1.1.5", writable = true, type = "Integer32")
    private Integer ipV4ConfigRowStatus;

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public Integer getIpV4ConfigIndex() {
        return ipV4ConfigIndex;
    }

    public void setIpV4ConfigIndex(Integer ipV4ConfigIndex) {
        this.ipV4ConfigIndex = ipV4ConfigIndex;
    }

    public String getIpV4Addr() {
        return ipV4Addr;
    }

    public void setIpV4Addr(String ipV4Addr) {
        this.ipV4Addr = ipV4Addr;
        if (ipV4Addr != null) {
            ipV4AddrDevice = new IpsAddress(ipV4Addr);
        }
    }

    public IpsAddress getIpV4AddrDevice() {
        return ipV4AddrDevice;
    }

    public void setIpV4AddrDevice(IpsAddress ipV4AddrDevice) {
        this.ipV4AddrDevice = ipV4AddrDevice;
        if (ipV4AddrDevice != null) {
            ipV4Addr = ipV4AddrDevice.getIpAddress();
        }
    }

    public String getIpV4NetMask() {
        return ipV4NetMask;
    }

    public void setIpV4NetMask(String ipV4NetMask) {
        this.ipV4NetMask = ipV4NetMask;
    }

    public Integer getIpV4AddrType() {
        return ipV4AddrType;
    }

    public void setIpV4AddrType(Integer ipV4AddrType) {
        this.ipV4AddrType = ipV4AddrType;
    }

    public Integer getIpV4ConfigRowStatus() {
        return ipV4ConfigRowStatus;
    }

    public void setIpV4ConfigRowStatus(Integer ipV4ConfigRowStatus) {
        this.ipV4ConfigRowStatus = ipV4ConfigRowStatus;
    }

}
