/***********************************************************************
 * $Id: TopOltDhcpVifCfg.java,v1.0 2017年11月16日 下午4:08:28 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.oltdhcp.facade.domain;

import com.topvision.ems.epon.oltdhcp.utils.OltDhcpUtils;
import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author haojie
 * @created @2017年11月16日-下午4:08:28
 *
 */
public class TopOltDhcpVifCfg implements AliasesSuperType {
    private static final long serialVersionUID = 7062985916450798113L;

    private Long entityId;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.7.3.1.1", index = true)
    private Integer topOltDhcpVifIndex;// 1-1024
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.7.3.1.2", index = true)
    private String topOltDhcpVifOpt60StrIndex;// 1-33Bytes(第一个字节表示字符串长度)
    private String opt60StrDisplay;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.7.3.1.3", writable = true, type = "IpAddress")
    private String topOltDhcpVifAgentAddr;// 可以不设置
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.7.3.1.4", writable = true, type = "Integer32")
    private Integer topOltDhcpVifServerGroup;// 1-20
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.7.3.1.5", writable = true, type = "Integer32")
    private Integer topOltDhcpVifRowStatus;

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public Integer getTopOltDhcpVifIndex() {
        return topOltDhcpVifIndex;
    }

    public void setTopOltDhcpVifIndex(Integer topOltDhcpVifIndex) {
        this.topOltDhcpVifIndex = topOltDhcpVifIndex;
    }

    public String getTopOltDhcpVifOpt60StrIndex() {
        return topOltDhcpVifOpt60StrIndex;
    }

    public void setTopOltDhcpVifOpt60StrIndex(String topOltDhcpVifOpt60StrIndex) {
        this.topOltDhcpVifOpt60StrIndex = topOltDhcpVifOpt60StrIndex;
        /*if (topOltDhcpVifOpt60StrIndex != null) {
            this.opt60StrDisplay = OltDhcpUtils.convertMibStrToOpt60Str(topOltDhcpVifOpt60StrIndex);
        }*/
    }

    public String getTopOltDhcpVifAgentAddr() {
        return topOltDhcpVifAgentAddr;
    }

    public void setTopOltDhcpVifAgentAddr(String topOltDhcpVifAgentAddr) {
        this.topOltDhcpVifAgentAddr = topOltDhcpVifAgentAddr;
    }

    public Integer getTopOltDhcpVifServerGroup() {
        return topOltDhcpVifServerGroup;
    }

    public void setTopOltDhcpVifServerGroup(Integer topOltDhcpVifServerGroup) {
        this.topOltDhcpVifServerGroup = topOltDhcpVifServerGroup;
    }

    public Integer getTopOltDhcpVifRowStatus() {
        return topOltDhcpVifRowStatus;
    }

    public void setTopOltDhcpVifRowStatus(Integer topOltDhcpVifRowStatus) {
        this.topOltDhcpVifRowStatus = topOltDhcpVifRowStatus;
    }

    public String getOpt60StrDisplay() {
        return OltDhcpUtils.convertMibStrToOpt60Str(topOltDhcpVifOpt60StrIndex);
    }

    /*public void setOpt60StrDisplay(String opt60StrDisplay) {
        this.opt60StrDisplay = opt60StrDisplay;
        if (opt60StrDisplay != null) {
            this.topOltDhcpVifOpt60StrIndex = OltDhcpUtils.convertOpt60StrToMibStr(opt60StrDisplay);
        }
    }*/

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("TopOltDhcpVifCfg [entityId=");
        builder.append(entityId);
        builder.append(", topOltDhcpVifIndex=");
        builder.append(topOltDhcpVifIndex);
        builder.append(", topOltDhcpVifOpt60StrIndex=");
        builder.append(topOltDhcpVifOpt60StrIndex);
        builder.append(", opt60StrDisplay=");
        builder.append(opt60StrDisplay);
        builder.append(", topOltDhcpVifAgentAddr=");
        builder.append(topOltDhcpVifAgentAddr);
        builder.append(", topOltDhcpVifServerGroup=");
        builder.append(topOltDhcpVifServerGroup);
        builder.append(", topOltDhcpVifRowStatus=");
        builder.append(topOltDhcpVifRowStatus);
        builder.append("]");
        return builder.toString();
    }

}
