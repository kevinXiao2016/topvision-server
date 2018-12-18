/***********************************************************************
 * $Id: OnuWanConnect.java,v1.0 2016年5月30日 下午5:03:41 $
 * 
 * @author: loyal
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.onu.domain;

import java.io.Serializable;
import java.util.List;

import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.dao.mybatis.AliasesSuperType;
import com.topvision.framework.utils.EponIndex;

/**
 * @author loyal
 * @created @2016年5月30日-下午5:03:41
 * 
 */
public class OnuWanConnect implements Serializable, AliasesSuperType {
    private static final long serialVersionUID = -7414339901970830742L;
    private Long entityId;
    private Long onuId;
    private Long onuIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.4.6.111.10.1.3.1.1,V1.10:1.3.6.1.4.1.17409.2.9.1.1.3.1.1", index = true)
    private Long onuMibIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.4.6.111.10.1.3.1.2,V1.10:1.3.6.1.4.1.17409.2.9.1.1.3.1.2", index = true)
    private Integer connectId;// WAN ID
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.4.6.111.10.1.3.1.3,V1.10:1.3.6.1.4.1.17409.2.9.1.1.3.1.3", writable = true)
    private String connectName;// 连接名称
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.4.6.111.10.1.3.1.4,V1.10:1.3.6.1.4.1.17409.2.9.1.1.3.1.4", writable = true, type = "Integer32")
    private Integer connectMtu;// 连接MTU
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.4.6.111.10.1.3.1.5,V1.10:1.3.6.1.4.1.17409.2.9.1.1.3.1.5", writable = true, type = "Integer32")
    private Integer vlanId;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.4.6.111.10.1.3.1.6,V1.10:1.3.6.1.4.1.17409.2.9.1.1.3.1.6", writable = true, type = "Integer32")
    private Integer vlanPriority;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.4.6.111.10.1.3.1.7,V1.10:1.3.6.1.4.1.17409.2.9.1.1.3.1.7", writable = true, type = "Integer32")
    private Integer connectMode;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.4.6.111.10.1.3.1.8,V1.10:1.3.6.1.4.1.17409.2.9.1.1.3.1.8", writable = true, type = "Integer32")
    private Integer ipMode;// IP分配模式
    private String ipModeString;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.4.6.111.10.1.3.1.9,V1.10:1.3.6.1.4.1.17409.2.9.1.1.3.1.9", writable = true)
    private String pppoeUserName;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.4.6.111.10.1.3.1.10,V1.10:1.3.6.1.4.1.17409.2.9.1.1.3.1.10", writable = true)
    private String pppoePassword;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.4.6.111.10.1.3.1.11,V1.10:1.3.6.1.4.1.17409.2.9.1.1.3.1.11", writable = true, type = "IpAddress")
    private String ipv4Address;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.4.6.111.10.1.3.1.12,V1.10:1.3.6.1.4.1.17409.2.9.1.1.3.1.12", writable = true, type = "IpAddress")
    private String ipv4Mask;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.4.6.111.10.1.3.1.13,V1.10:1.3.6.1.4.1.17409.2.9.1.1.3.1.13", writable = true, type = "IpAddress")
    private String ipv4Gateway;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.4.6.111.10.1.3.1.14,V1.10:1.3.6.1.4.1.17409.2.9.1.1.3.1.14", writable = true, type = "IpAddress")
    private String ipv4Dns;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.4.6.111.10.1.3.1.15,V1.10:1.3.6.1.4.1.17409.2.9.1.1.3.1.15", writable = true, type = "IpAddress")
    private String ipv4DnsAlternative;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.4.6.111.10.1.3.1.16,V1.10:1.3.6.1.4.1.17409.2.9.1.1.3.1.16", writable = true, type = "Integer32")
    private Integer wanConnectRowStatus;

    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.4.6.111.10.1.4.1.3,V1.10:1.3.6.1.4.1.17409.2.9.1.1.4.1.3", writable = true, type = "Integer32")
    private Integer serviceMode;// 业务模式
    private String serviceModeString;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.4.6.111.10.1.4.1.4,V1.10:1.3.6.1.4.1.17409.2.9.1.1.4.1.4", writable = true, isHex = true)
    private String bindInterface;// 绑定端口（vlan/ssid）
    private List<Integer> bindInterfaceList;

    private List<Integer> bindVlan;
    private List<Integer> bindSsid;

    private Integer connectStatus;
    private String connectStatusString;
    private Integer connectErrorCode;

    // pppoe info
    private String pppoeStatusIpv4Addr;
    private String pppoeStatusIpv4Mask;
    private String pppoeStatusIpv4Gw;
    private String pppoeStatusIpv4DnsPrimary;
    private String pppoeStatusIpv4DnsSecondary;

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public Long getOnuId() {
        return onuId;
    }

    public void setOnuId(Long onuId) {
        this.onuId = onuId;
    }

    public Long getOnuIndex() {
        return onuIndex;
    }

    public void setOnuIndex(Long onuIndex) {
        this.onuIndex = onuIndex;
        if (onuIndex != null) {
            onuMibIndex = EponIndex.getOnuMibIndexByIndex(onuIndex);
        }
    }

    public Integer getConnectId() {
        return connectId;
    }

    public void setConnectId(Integer connectId) {
        this.connectId = connectId;
    }

    public String getConnectName() {
        return connectName;
    }

    public void setConnectName(String connectName) {
        this.connectName = connectName;
    }

    public Integer getConnectMtu() {
        return connectMtu;
    }

    public void setConnectMtu(Integer connectMtu) {
        this.connectMtu = connectMtu;
    }

    public Integer getVlanId() {
        return vlanId;
    }

    public void setVlanId(Integer vlanId) {
        this.vlanId = vlanId;
    }

    public Integer getVlanPriority() {
        return vlanPriority;
    }

    public void setVlanPriority(Integer vlanPriority) {
        this.vlanPriority = vlanPriority;
    }

    public Integer getIpMode() {
        return ipMode;
    }

    public void setIpMode(Integer ipMode) {
        this.ipMode = ipMode;
    }

    public String getPppoeUserName() {
        return pppoeUserName;
    }

    public void setPppoeUserName(String pppoeUserName) {
        this.pppoeUserName = pppoeUserName;
    }

    public String getPppoePassword() {
        return pppoePassword;
    }

    public void setPppoePassword(String pppoePassword) {
        this.pppoePassword = pppoePassword;
    }

    public String getIpv4Address() {
        return ipv4Address;
    }

    public void setIpv4Address(String ipv4Address) {
        this.ipv4Address = ipv4Address;
    }

    public String getIpv4Gateway() {
        return ipv4Gateway;
    }

    public void setIpv4Gateway(String ipv4Gateway) {
        this.ipv4Gateway = ipv4Gateway;
    }

    public String getIpv4Dns() {
        return ipv4Dns;
    }

    public void setIpv4Dns(String ipv4Dns) {
        this.ipv4Dns = ipv4Dns;
    }

    public String getIpv4DnsAlternative() {
        return ipv4DnsAlternative;
    }

    public void setIpv4DnsAlternative(String ipv4DnsAlternative) {
        this.ipv4DnsAlternative = ipv4DnsAlternative;
    }

    public String getIpv4Mask() {
        return ipv4Mask;
    }

    public void setIpv4Mask(String ipv4Mask) {
        this.ipv4Mask = ipv4Mask;
    }

    public Integer getServiceMode() {
        return serviceMode;
    }

    public void setServiceMode(Integer serviceMode) {
        this.serviceMode = serviceMode;
    }

    /**
     * @return the bindInterface
     */
    public String getBindInterface() {
        return bindInterface;
    }

    /**
     * @param bindInterface
     *            the bindInterface to set
     */
    public void setBindInterface(String bindInterface) {
        this.bindInterface = bindInterface;
    }

    public Integer getConnectStatus() {
        return connectStatus;
    }

    public void setConnectStatus(Integer connectStatus) {
        this.connectStatus = connectStatus;
    }

    public Integer getConnectErrorCode() {
        return connectErrorCode;
    }

    public void setConnectErrorCode(Integer connectErrorCode) {
        this.connectErrorCode = connectErrorCode;
    }

    /**
     * @return the bindVlan
     */
    public List<Integer> getBindVlan() {
        return bindVlan;
    }

    /**
     * @param bindVlan
     *            the bindVlan to set
     */
    public void setBindVlan(List<Integer> bindVlan) {
        this.bindVlan = bindVlan;
    }

    /**
     * @return the bindSsid
     */
    public List<Integer> getBindSsid() {
        return bindSsid;
    }

    /**
     * @param bindSsid
     *            the bindSsid to set
     */
    public void setBindSsid(List<Integer> bindSsid) {
        this.bindSsid = bindSsid;
    }

    /**
     * @return the onuMibIndex
     */
    public Long getOnuMibIndex() {
        return onuMibIndex;
    }

    /**
     * @param onuMibIndex
     *            the onuMibIndex to set
     */
    public void setOnuMibIndex(Long onuMibIndex) {
        this.onuMibIndex = onuMibIndex;
        if (onuMibIndex != null) {
            onuIndex = EponIndex.getOnuIndexByMibIndex(onuMibIndex);
        }
    }

    /**
     * @return the connectMode
     */
    public Integer getConnectMode() {
        return connectMode;
    }

    /**
     * @param connectMode
     *            the connectMode to set
     */
    public void setConnectMode(Integer connectMode) {
        this.connectMode = connectMode;
    }

    public List<Integer> getBindInterfaceList() {
        return bindInterfaceList;
    }

    public void setBindInterfaceList(List<Integer> bindInterfaceList) {
        this.bindInterfaceList = bindInterfaceList;
    }

    /**
     * @return the wanConnectRowStatus
     */
    public Integer getWanConnectRowStatus() {
        return wanConnectRowStatus;
    }

    /**
     * @param wanConnectRowStatus
     *            the wanConnectRowStatus to set
     */
    public void setWanConnectRowStatus(Integer wanConnectRowStatus) {
        this.wanConnectRowStatus = wanConnectRowStatus;
    }

    /**
     * @return the ipModeString
     */
    public String getIpModeString() {
        switch (ipMode) {
        case 0:
            ipModeString = "Bridge";
            break;
        case 1:
            ipModeString = "DHCP";
            break;
        case 2:
            ipModeString = "Static";
            break;
        case 3:
            ipModeString = "PPPOE";
            break;
        default:
            break;
        }
        return ipModeString;
    }

    /**
     * @param ipModeString
     *            the ipModeString to set
     */
    public void setIpModeString(String ipModeString) {
        this.ipModeString = ipModeString;
    }

    /**
     * @return the serviceModeString
     */
    public String getServiceModeString() {
        if (serviceMode != null) {
            switch (serviceMode) {
            case 1:
                serviceModeString = "internet";
                break;
            case 2:
                serviceModeString = "VOD";
                break;
            case 3:
                serviceModeString = "VoIP";
                break;
            case 4:
                serviceModeString = "Mgmt";
                break;
            case 5:
                serviceModeString = "TR069";
                break;
            default:
                break;
            }
        }
        return serviceModeString;
    }

    /**
     * @param serviceModeString
     *            the serviceModeString to set
     */
    public void setServiceModeString(String serviceModeString) {
        this.serviceModeString = serviceModeString;
    }

    /**
     * @return the connectStatusString
     */
    public String getConnectStatusString() {
        if (connectStatus != null) {
            switch (connectStatus) {
            case 0:
                connectStatusString = "disconnect";
                break;
            case 1:
                connectStatusString = "connect";
                break;
            default:
                break;
            }
        }
        return connectStatusString;
    }

    /**
     * @param connectStatusString
     *            the connectStatusString to set
     */
    public void setConnectStatusString(String connectStatusString) {
        this.connectStatusString = connectStatusString;
    }

    public String getPppoeStatusIpv4Addr() {
        if (ipMode != null && ipMode == 2) {
            return ipv4Address;
        }
        return pppoeStatusIpv4Addr;
    }

    public void setPppoeStatusIpv4Addr(String pppoeStatusIpv4Addr) {
        this.pppoeStatusIpv4Addr = pppoeStatusIpv4Addr;
    }

    public String getPppoeStatusIpv4Mask() {
        if (ipMode != null && ipMode == 2) {
            return ipv4Mask;
        }
        return pppoeStatusIpv4Mask;
    }

    public void setPppoeStatusIpv4Mask(String pppoeStatusIpv4Mask) {
        this.pppoeStatusIpv4Mask = pppoeStatusIpv4Mask;
    }

    public String getPppoeStatusIpv4Gw() {
        if (ipMode != null && ipMode == 2) {
            return ipv4Gateway;
        }
        return pppoeStatusIpv4Gw;
    }

    public void setPppoeStatusIpv4Gw(String pppoeStatusIpv4Gw) {
        this.pppoeStatusIpv4Gw = pppoeStatusIpv4Gw;
    }

    public String getPppoeStatusIpv4DnsPrimary() {
        if (ipMode != null && ipMode == 2) {
            return ipv4Dns;
        }
        return pppoeStatusIpv4DnsPrimary;
    }

    public void setPppoeStatusIpv4DnsPrimary(String pppoeStatusIpv4DnsPrimary) {
        this.pppoeStatusIpv4DnsPrimary = pppoeStatusIpv4DnsPrimary;
    }

    public String getPppoeStatusIpv4DnsSecondary() {
        if (ipMode != null && ipMode == 2) {
            return ipv4DnsAlternative;
        }
        return pppoeStatusIpv4DnsSecondary;
    }

    public void setPppoeStatusIpv4DnsSecondary(String pppoeStatusIpv4DnsSecondary) {
        this.pppoeStatusIpv4DnsSecondary = pppoeStatusIpv4DnsSecondary;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("OnuWanConnect [entityId=");
        builder.append(entityId);
        builder.append(", onuId=");
        builder.append(onuId);
        builder.append(", onuIndex=");
        builder.append(onuIndex);
        builder.append(", onuMibIndex=");
        builder.append(onuMibIndex);
        builder.append(", connectId=");
        builder.append(connectId);
        builder.append(", connectName=");
        builder.append(connectName);
        builder.append(", connectMtu=");
        builder.append(connectMtu);
        builder.append(", vlanId=");
        builder.append(vlanId);
        builder.append(", vlanPriority=");
        builder.append(vlanPriority);
        builder.append(", connectMode=");
        builder.append(connectMode);
        builder.append(", ipMode=");
        builder.append(ipMode);
        builder.append(", ipModeString=");
        builder.append(ipModeString);
        builder.append(", pppoeUserName=");
        builder.append(pppoeUserName);
        builder.append(", pppoePassword=");
        builder.append(pppoePassword);
        builder.append(", ipv4Address=");
        builder.append(ipv4Address);
        builder.append(", ipv4Mask=");
        builder.append(ipv4Mask);
        builder.append(", ipv4Gateway=");
        builder.append(ipv4Gateway);
        builder.append(", ipv4Dns=");
        builder.append(ipv4Dns);
        builder.append(", ipv4DnsAlternative=");
        builder.append(ipv4DnsAlternative);
        builder.append(", wanConnectRowStatus=");
        builder.append(wanConnectRowStatus);
        builder.append(", serviceMode=");
        builder.append(serviceMode);
        builder.append(", serviceModeString=");
        builder.append(serviceModeString);
        builder.append(", bindInterface=");
        builder.append(bindInterface);
        builder.append(", bindInterfaceList=");
        builder.append(bindInterfaceList);
        builder.append(", bindVlan=");
        builder.append(bindVlan);
        builder.append(", bindSsid=");
        builder.append(bindSsid);
        builder.append(", connectStatus=");
        builder.append(connectStatus);
        builder.append(", connectStatusString=");
        builder.append(connectStatusString);
        builder.append(", connectErrorCode=");
        builder.append(connectErrorCode);
        builder.append(", pppoeStatusIpv4Addr=");
        builder.append(pppoeStatusIpv4Addr);
        builder.append(", pppoeStatusIpv4Mask=");
        builder.append(pppoeStatusIpv4Mask);
        builder.append(", pppoeStatusIpv4Gw=");
        builder.append(pppoeStatusIpv4Gw);
        builder.append(", pppoeStatusIpv4DnsPrimary=");
        builder.append(pppoeStatusIpv4DnsPrimary);
        builder.append(", pppoeStatusIpv4DnsSecondary=");
        builder.append(pppoeStatusIpv4DnsSecondary);
        builder.append("]");
        return builder.toString();
    }

}
