/***********************************************************************
 * $Id: OnuWanBind.java,v1.0 2017年10月17日 下午3:18:35 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.onu.domain;

import java.io.Serializable;

import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author haojie
 * @created @2017年10月17日-下午3:18:35
 *
 */
public class OnuWanBind implements Serializable, AliasesSuperType {
    private static final long serialVersionUID = 672868199102402313L;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.4.6.111.10.1.4.1.1,V1.10:1.3.6.1.4.1.17409.2.9.1.1.4.1.1", index = true)
    private Long onuMibIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.4.6.111.10.1.4.1.2,V1.10:1.3.6.1.4.1.17409.2.9.1.1.4.1.2", index = true)
    private Integer connectId;// WAN ID
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.4.6.111.10.1.4.1.3,V1.10:1.3.6.1.4.1.17409.2.9.1.1.4.1.3", writable = true, type = "Integer32")
    private Integer serviceMode;// 业务模式
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.4.6.111.10.1.4.1.4,V1.10:1.3.6.1.4.1.17409.2.9.1.1.4.1.4", writable = true)
    private String bindInterface;// 绑定端口（vlan/ssid）

    public Long getOnuMibIndex() {
        return onuMibIndex;
    }

    public void setOnuMibIndex(Long onuMibIndex) {
        this.onuMibIndex = onuMibIndex;
    }

    public Integer getConnectId() {
        return connectId;
    }

    public void setConnectId(Integer connectId) {
        this.connectId = connectId;
    }

    public String getBindInterface() {
        return bindInterface;
    }

    public void setBindInterface(String bindInterface) {
        this.bindInterface = bindInterface;
    }

    public Integer getServiceMode() {
        return serviceMode;
    }

    public void setServiceMode(Integer serviceMode) {
        this.serviceMode = serviceMode;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("OnuWanBind [onuMibIndex=");
        builder.append(onuMibIndex);
        builder.append(", connectId=");
        builder.append(connectId);
        builder.append(", serviceMode=");
        builder.append(serviceMode);
        builder.append(", bindInterface=");
        builder.append(bindInterface);
        builder.append("]");
        return builder.toString();
    }

}
