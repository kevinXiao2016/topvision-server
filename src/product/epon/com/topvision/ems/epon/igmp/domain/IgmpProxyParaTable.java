/***********************************************************************
 * $ IgmpProxyParaTable.java,v1.0 2011-11-23 8:32:37 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.igmp.domain;

import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author jay
 * @created @2011-11-23-8:32:37
 */
public class IgmpProxyParaTable implements AliasesSuperType {
    private static final long serialVersionUID = 7342180206202447869L;
    private Long entityId;
    /**
     * IGMP Proxy索引号 - 频道号 INTEGER (1..2000)
     */
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.6.2.1.1", index = true)
    private Integer proxyIndex;
    /**
     * IGMP Proxy别名（如央视、文广传媒等） OCTET STRING (SIZE (0..256))
     */
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.6.2.1.2", writable = true, type = "OctetString")
    private String proxyName;
    private String proxyChName;
    /**
     * proxy源IP地址 OCTET STRING (SIZE (4))
     */
    // TODO agent特殊处理
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.6.2.1.3")
    private String proxySrcIPAddress;

    /**
     * IGMP Proxy对应组播VID 注：只有在可控组播模式下组播VID才有意义
     */
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.6.2.1.4", writable = true, type = "Integer32")
    private Integer proxyMulticastVID;
    /**
     * 组播IP地址 OCTET STRING (SIZE (4))
     */
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.6.2.1.5", writable = true, type = "IpAddress")
    private String proxyMulticastIPAddress;
    /**
     * 组播保证带宽 单位：Kbps INTEGER (0..4294967295)
     */
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.6.2.1.6", writable = true, type = "Integer32")
    private Long multicastAssuredBW;
    /**
     * 组播最大带宽 单位：Kbps INTEGER (0..4294967295)
     */
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.6.2.1.7", writable = true, type = "Gauge32")
    private Long multicastMaxBW;
    /**
     * 行状态 INTEGER { active(1), notInService(2), notReady(3), createAndGo(4), createAndWait(5),
     * destroy(6) }
     */
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.6.2.1.8", writable = true, type = "Integer32")
    private Integer proxyRowStatus;

    public String getProxyChName() {
        return proxyChName;
    }

    public void setProxyChName(String proxyChName) {
        this.proxyChName = proxyChName;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public Long getMulticastAssuredBW() {
        return multicastAssuredBW;
    }

    public void setMulticastAssuredBW(Long multicastAssuredBW) {
        this.multicastAssuredBW = multicastAssuredBW;
    }

    public Long getMulticastMaxBW() {
        return multicastMaxBW;
    }

    public void setMulticastMaxBW(Long multicastMaxBW) {
        this.multicastMaxBW = multicastMaxBW;
    }

    public Integer getProxyIndex() {
        return proxyIndex;
    }

    public void setProxyIndex(Integer proxyIndex) {
        this.proxyIndex = proxyIndex;
    }

    public String getProxyMulticastIPAddress() {
        return proxyMulticastIPAddress;
    }

    public void setProxyMulticastIPAddress(String proxyMulticastIPAddress) {
        this.proxyMulticastIPAddress = proxyMulticastIPAddress;
    }

    public Integer getProxyMulticastVID() {
        return proxyMulticastVID;
    }

    public void setProxyMulticastVID(Integer proxyMulticastVID) {
        this.proxyMulticastVID = proxyMulticastVID;
    }

    public String getProxyName() {
        return proxyName;
    }

    public void setProxyName(String proxyName) {
        this.proxyName = proxyName;
    }

    public Integer getProxyRowStatus() {
        return proxyRowStatus;
    }

    public void setProxyRowStatus(Integer proxyRowStatus) {
        this.proxyRowStatus = proxyRowStatus;
    }

    public String getProxySrcIPAddress() {
        return proxySrcIPAddress;
    }

    public void setProxySrcIPAddress(String proxySrcIPAddress) {
        this.proxySrcIPAddress = proxySrcIPAddress;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("IgmpProxyParaTable");
        sb.append("{entityId=").append(entityId);
        sb.append(", proxyIndex=").append(proxyIndex);
        sb.append(", proxyName='").append(proxyName).append('\'');
        sb.append(", proxyChName='").append(proxyChName).append('\'');
        sb.append(", proxySrcIPAddress='").append(proxySrcIPAddress).append('\'');
        sb.append(", proxyMulticastVID=").append(proxyMulticastVID);
        sb.append(", proxyMulticastIPAddress='").append(proxyMulticastIPAddress).append('\'');
        sb.append(", multicastAssuredBW=").append(multicastAssuredBW);
        sb.append(", multicastMaxBW=").append(multicastMaxBW);
        sb.append(", proxyRowStatus=").append(proxyRowStatus);
        sb.append('}');
        return sb.toString();
    }
}
