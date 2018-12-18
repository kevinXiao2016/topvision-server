/***********************************************************************
 * $Id: CmcVlanData.java,v1.0 2015-7-20 下午1:47:12 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.vlan.domain;

import java.io.Serializable;
import java.util.List;

import com.topvision.ems.cmc.vlan.facade.domain.CmcVifSubIpEntry;
import com.topvision.ems.cmc.vlan.facade.domain.CmcVlanConfigEntry;
import com.topvision.ems.cmc.vlan.facade.domain.CmcVlanDhcpAllocEntry;
import com.topvision.ems.cmc.vlan.facade.domain.CmcVlanPrimaryInterface;
import com.topvision.ems.cmc.vlan.facade.domain.CmcVlanPrimaryIp;
import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author haojie
 * @created @2015-7-20-下午1:47:12
 *
 */
public class CmcVlanData implements Serializable, AliasesSuperType {
    private static final long serialVersionUID = -8748896113127374856L;

    List<CmcVlanConfigEntry> cmcVlanConfigEntries;
    List<CmcVlanPrimaryIp> cmcVlanPrimaryIps;
    List<CmcVifSubIpEntry> cmcVifSubIpEntries;
    List<CmcVlanDhcpAllocEntry> cmcVlanDhcpAllocEntries;
    CmcVlanPrimaryInterface cmcVlanPrimaryInterface;

    public List<CmcVlanConfigEntry> getCmcVlanConfigEntries() {
        return cmcVlanConfigEntries;
    }

    public void setCmcVlanConfigEntries(List<CmcVlanConfigEntry> cmcVlanConfigEntries) {
        this.cmcVlanConfigEntries = cmcVlanConfigEntries;
    }

    public List<CmcVlanPrimaryIp> getCmcVlanPrimaryIps() {
        return cmcVlanPrimaryIps;
    }

    public void setCmcVlanPrimaryIps(List<CmcVlanPrimaryIp> cmcVlanPrimaryIps) {
        this.cmcVlanPrimaryIps = cmcVlanPrimaryIps;
    }

    public List<CmcVifSubIpEntry> getCmcVifSubIpEntries() {
        return cmcVifSubIpEntries;
    }

    public void setCmcVifSubIpEntries(List<CmcVifSubIpEntry> cmcVifSubIpEntries) {
        this.cmcVifSubIpEntries = cmcVifSubIpEntries;
    }

    public List<CmcVlanDhcpAllocEntry> getCmcVlanDhcpAllocEntries() {
        return cmcVlanDhcpAllocEntries;
    }

    public void setCmcVlanDhcpAllocEntries(List<CmcVlanDhcpAllocEntry> cmcVlanDhcpAllocEntries) {
        this.cmcVlanDhcpAllocEntries = cmcVlanDhcpAllocEntries;
    }

    public CmcVlanPrimaryInterface getCmcVlanPrimaryInterface() {
        return cmcVlanPrimaryInterface;
    }

    public void setCmcVlanPrimaryInterface(CmcVlanPrimaryInterface cmcVlanPrimaryInterface) {
        this.cmcVlanPrimaryInterface = cmcVlanPrimaryInterface;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("CmcVlanData [cmcVlanConfigEntries=");
        builder.append(cmcVlanConfigEntries);
        builder.append(", cmcVlanPrimaryIps=");
        builder.append(cmcVlanPrimaryIps);
        builder.append(", cmcVifSubIpEntries=");
        builder.append(cmcVifSubIpEntries);
        builder.append(", cmcVlanDhcpAllocEntries=");
        builder.append(cmcVlanDhcpAllocEntries);
        builder.append(", cmcVlanPrimaryInterface=");
        builder.append(cmcVlanPrimaryInterface);
        builder.append("]");
        return builder.toString();
    }

}
