/***********************************************************************
 * $Id: DhcpRelayVlanMap.java,v1.0 2013-6-26 上午9:30:45 $
 * 
 * @author: dosion
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.dhcp.domain;

import java.io.Serializable;

import com.topvision.framework.utils.EponConstants;
import com.topvision.ems.epon.utils.EponUtil;
import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author dosion
 * @created @2013-6-26-上午9:30:45
 * 
 */
public class DhcpRelayVlanMap implements Serializable, AliasesSuperType {
    private static final long serialVersionUID = 3907062387630654692L;
    private Long entityId;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.3.2.1.1", index = true)
    private String topCcmtsDhcpBundleInterface;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.3.8.1.1", writable = true, type = "OctetString")
    private String topCcmtsDhcpBundleVlanMap;
    private String vlanMapStr;

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public String getTopCcmtsDhcpBundleInterface() {
        return topCcmtsDhcpBundleInterface;
    }

    public void setTopCcmtsDhcpBundleInterface(String topCcmtsDhcpBundleInterface) {
        this.topCcmtsDhcpBundleInterface = topCcmtsDhcpBundleInterface;
    }

    public String getTopCcmtsDhcpBundleVlanMap() {
        return topCcmtsDhcpBundleVlanMap;
    }

    public void setTopCcmtsDhcpBundleVlanMap(String topCcmtsDhcpBundleVlanMap) {
        this.topCcmtsDhcpBundleVlanMap = topCcmtsDhcpBundleVlanMap;
        this.vlanMapStr = EponUtil.getStrFromMap(topCcmtsDhcpBundleVlanMap);
    }

    public String getVlanMapStr() {
        return vlanMapStr;
    }

    public void setVlanMapStr(String vlanMapStr) {
        this.vlanMapStr = vlanMapStr;
        this.topCcmtsDhcpBundleVlanMap = EponUtil.getMapfromStr(vlanMapStr, EponConstants.DHCP_RELAY_VLANMAP_LENGTH);
    }

}
