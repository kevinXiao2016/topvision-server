/***********************************************************************
 * $Id: TopOltDhcpServerGroup.java,v1.0 2017年11月16日 下午4:08:56 $
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
 * @created @2017年11月16日-下午4:08:56
 *
 */
public class TopOltDhcpServerGroup implements AliasesSuperType {
    private static final long serialVersionUID = -6752151570377189517L;

    private Long entityId;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.7.4.1.1", index = true)
    private Integer topOltDhcpServerGroupIndex;// 1-20
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.7.4.1.2", writable = true, type = "OctetString")
    private String topOltDhcpServerIpList;// 4-20长度
    private String serverIpsDisplay;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.7.4.1.3", type = "Integer32")
    private Integer topOltDhcpServerBindNum;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.7.4.1.4", writable = true, type = "Integer32")
    private Integer topOltDhcpServerRowStatus;

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public Integer getTopOltDhcpServerGroupIndex() {
        return topOltDhcpServerGroupIndex;
    }

    public void setTopOltDhcpServerGroupIndex(Integer topOltDhcpServerGroupIndex) {
        this.topOltDhcpServerGroupIndex = topOltDhcpServerGroupIndex;
    }

    public String getTopOltDhcpServerIpList() {
        return topOltDhcpServerIpList;
    }

    public void setTopOltDhcpServerIpList(String topOltDhcpServerIpList) {
        this.topOltDhcpServerIpList = topOltDhcpServerIpList;
        if (topOltDhcpServerIpList != null) {
            this.serverIpsDisplay = OltDhcpUtils.convertMibStrToGroupStr(topOltDhcpServerIpList);
        }
    }

    public Integer getTopOltDhcpServerBindNum() {
        return topOltDhcpServerBindNum;
    }

    public void setTopOltDhcpServerBindNum(Integer topOltDhcpServerBindNum) {
        this.topOltDhcpServerBindNum = topOltDhcpServerBindNum;
    }

    public Integer getTopOltDhcpServerRowStatus() {
        return topOltDhcpServerRowStatus;
    }

    public void setTopOltDhcpServerRowStatus(Integer topOltDhcpServerRowStatus) {
        this.topOltDhcpServerRowStatus = topOltDhcpServerRowStatus;
    }

    public String getServerIpsDisplay() {
        return serverIpsDisplay;
    }

    public void setServerIpsDisplay(String serverIpsDisplay) {
        this.serverIpsDisplay = serverIpsDisplay;
        if (serverIpsDisplay != null) {
            topOltDhcpServerIpList = OltDhcpUtils.convertGroupStrToMibStr(serverIpsDisplay);
        }
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("TopOltDhcpServerGroup [entityId=");
        builder.append(entityId);
        builder.append(", topOltDhcpServerGroupIndex=");
        builder.append(topOltDhcpServerGroupIndex);
        builder.append(", topOltDhcpServerIpList=");
        builder.append(topOltDhcpServerIpList);
        builder.append(", serverIpsDisplay=");
        builder.append(serverIpsDisplay);
        builder.append(", topOltDhcpServerBindNum=");
        builder.append(topOltDhcpServerBindNum);
        builder.append(", topOltDhcpServerRowStatus=");
        builder.append(topOltDhcpServerRowStatus);
        builder.append("]");
        return builder.toString();
    }

}
