/***********************************************************************
 * $Id: TrapServer.java,v1.0 2013-4-23 下午2:12:16 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.trapserver.facade.domain;

import java.io.Serializable;

import org.apache.ibatis.type.Alias;

import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.common.IpUtils;
import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author flack
 * @created @2013-4-23-下午2:12:16
 *
 */
@Alias("cmcTrapServer")
public class CmcTrapServer implements Serializable, AliasesSuperType {
    private static final long serialVersionUID = -8314228342754394667L;

    private Long entityId;

    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.6.2.1.1", index = true)
    private Integer topCcmtsTrapServerIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.6.2.1.2", writable = true, type = "IpAddress")
    private String topCcmtsTrapServerIpAddr;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.6.2.1.3", writable = true, type = "Integer32")
    private Integer topCcmtsTrapServerStatus;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.6.2.1.4", writable = true, type = "Integer32")
    private Integer topCcmtsTrapServerIpPort;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.6.2.1.5", writable = true, type = "OctetString")
    private String topCcmtsTrapCommunityName;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.6.2.1.6", writable = true, type = "OctetString")
    private String topCcmtsTrapServerIpv6Addr;
    
    private String ip;

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public Integer getTopCcmtsTrapServerIndex() {
        return topCcmtsTrapServerIndex;
    }

    public void setTopCcmtsTrapServerIndex(Integer topCcmtsTrapServerIndex) {
        this.topCcmtsTrapServerIndex = topCcmtsTrapServerIndex;
    }

    public String getTopCcmtsTrapServerIpAddr() {
        return topCcmtsTrapServerIpAddr;
    }

    public void setTopCcmtsTrapServerIpAddr(String topCcmtsTrapServerIpAddr) {
        this.topCcmtsTrapServerIpAddr = topCcmtsTrapServerIpAddr;
    }

    public Integer getTopCcmtsTrapServerStatus() {
        return topCcmtsTrapServerStatus;
    }

    public void setTopCcmtsTrapServerStatus(Integer topCcmtsTrapServerStatus) {
        this.topCcmtsTrapServerStatus = topCcmtsTrapServerStatus;
    }

    public Integer getTopCcmtsTrapServerIpPort() {
        return topCcmtsTrapServerIpPort;
    }

    public void setTopCcmtsTrapServerIpPort(Integer topCcmtsTrapServerIpPort) {
        this.topCcmtsTrapServerIpPort = topCcmtsTrapServerIpPort;
    }

    public String getTopCcmtsTrapCommunityName() {
        return topCcmtsTrapCommunityName;
    }

    public void setTopCcmtsTrapCommunityName(String topCcmtsTrapCommunityName) {
        this.topCcmtsTrapCommunityName = topCcmtsTrapCommunityName;
    }

    public String getTopCcmtsTrapServerIpv6Addr() {
        return topCcmtsTrapServerIpv6Addr;
    }

    public void setTopCcmtsTrapServerIpv6Addr(String topCcmtsTrapServerIpv6Addr) {
        this.topCcmtsTrapServerIpv6Addr = topCcmtsTrapServerIpv6Addr;
    }
    
    public String getIp() {
        if(this.topCcmtsTrapServerIpAddr != null && IpUtils.matches(this.topCcmtsTrapServerIpAddr) && !"0.0.0.0".equals(this.topCcmtsTrapServerIpAddr)) {
            return IpUtils.formatInetAddress(this.topCcmtsTrapServerIpAddr);
        } else {
            return IpUtils.formatInetAddress(this.topCcmtsTrapServerIpv6Addr);
        }
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    @Override
    public String toString() {
        return "CmcTrapServer [entityId=" + entityId + ", topCcmtsTrapServerIndex=" + topCcmtsTrapServerIndex
                + ", topCcmtsTrapServerIpAddr=" + topCcmtsTrapServerIpAddr + ", topCcmtsTrapServerStatus="
                + topCcmtsTrapServerStatus + ", topCcmtsTrapServerIpPort=" + topCcmtsTrapServerIpPort
                + ", topCcmtsTrapCommunityName=" + topCcmtsTrapCommunityName + ", topCcmtsTrapServerIpv6Addr="
                + topCcmtsTrapServerIpv6Addr + "]";
    }

    

}
