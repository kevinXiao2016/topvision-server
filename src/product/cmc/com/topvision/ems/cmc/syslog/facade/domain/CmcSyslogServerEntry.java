/***********************************************************************
 * $Id: CmcSyslogServerAttr.java,v1.0 2013-4-25 下午3:41:56 $
 * 
 * @author: Administrator
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.syslog.facade.domain;

import java.io.Serializable;

import org.apache.ibatis.type.Alias;

import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.common.IpUtils;
import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author Administrator
 * @created @2013-4-25-下午3:41:56
 *
 */
@Alias("cmcSyslogServerEntry")
public class CmcSyslogServerEntry implements Serializable, AliasesSuperType {

    private static final long serialVersionUID = -7163017538173119398L;

    private Long entityId;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.6.1.1.1", index = true)
    private Integer topCcmtsSyslogServerIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.6.1.1.2", writable = true, type = "IpAddress")
    private String topCcmtsSyslogServerIpAddr;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.6.1.1.3", writable = true, type = "Integer32")
    private Integer topCcmtsSyslogServerStatus;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.6.1.1.4", writable = true, type = "Integer32")
    private Integer topCcmtsSyslogServerIpPort;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.6.1.1.5", writable = true, type = "OctetString")
    private String topCcmtsSyslogServerIpv6Addr;

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public Integer getTopCcmtsSyslogServerIndex() {
        return topCcmtsSyslogServerIndex;
    }

    public void setTopCcmtsSyslogServerIndex(Integer topCcmtsSyslogServerIndex) {
        this.topCcmtsSyslogServerIndex = topCcmtsSyslogServerIndex;
    }

    public String getTopCcmtsSyslogServerIpAddr() {
        return topCcmtsSyslogServerIpAddr;
    }

    public void setTopCcmtsSyslogServerIpAddr(String topCcmtsSyslogServerIpAddr) {
        this.topCcmtsSyslogServerIpAddr = topCcmtsSyslogServerIpAddr;
    }

    public Integer getTopCcmtsSyslogServerStatus() {
        return topCcmtsSyslogServerStatus;
    }

    public void setTopCcmtsSyslogServerStatus(Integer topCcmtsSyslogServerStatus) {
        this.topCcmtsSyslogServerStatus = topCcmtsSyslogServerStatus;
    }

    public Integer getTopCcmtsSyslogServerIpPort() {
        return topCcmtsSyslogServerIpPort;
    }

    public void setTopCcmtsSyslogServerIpPort(Integer topCcmtsSyslogServerIpPort) {
        this.topCcmtsSyslogServerIpPort = topCcmtsSyslogServerIpPort;
    }

    public String getTopCcmtsSyslogServerIpv6Addr() {
        return topCcmtsSyslogServerIpv6Addr;
    }

    public void setTopCcmtsSyslogServerIpv6Addr(String topCcmtsSyslogServerIpv6Addr) {
        this.topCcmtsSyslogServerIpv6Addr = topCcmtsSyslogServerIpv6Addr;
    }
    
    public String getIp() {
        if(this.topCcmtsSyslogServerIpAddr != null && IpUtils.matches(this.topCcmtsSyslogServerIpAddr) && !"0.0.0.0".equals(this.topCcmtsSyslogServerIpAddr)) {
            return IpUtils.formatInetAddress(this.topCcmtsSyslogServerIpAddr);
        } else {
            return IpUtils.formatInetAddress(this.topCcmtsSyslogServerIpv6Addr);
        }
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    @Override
    public String toString() {
        return "CmcSyslogServerEntry [entityId=" + entityId + ", topCcmtsSyslogServerIndex=" + topCcmtsSyslogServerIndex
                + ", topCcmtsSyslogServerIpAddr=" + topCcmtsSyslogServerIpAddr + ", topCcmtsSyslogServerStatus="
                + topCcmtsSyslogServerStatus + ", topCcmtsSyslogServerIpPort=" + topCcmtsSyslogServerIpPort
                + ", topCcmtsSyslogServerIpv6Addr=" + topCcmtsSyslogServerIpv6Addr + "]";
    }

}
