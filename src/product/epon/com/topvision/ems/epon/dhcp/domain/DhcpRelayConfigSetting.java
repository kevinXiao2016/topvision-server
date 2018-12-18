package com.topvision.ems.epon.dhcp.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.topvision.framework.utils.EponConstants;

public class DhcpRelayConfigSetting implements Serializable {
    private static final long serialVersionUID = -8643132720571532006L;
    private Long entityId;
    private DhcpBundle dhcpBundle;
    private List<DhcpGiaddrConfig> dhcpGiAddr;
    private List<DhcpOption60> dhcpOption60;
    private List<DhcpServerConfig> dhcpServer;
    private List<DhcpIntIpConfig> virtualIp;
    DhcpRelayVlanMap dhcpRelayVlanMap;
    private String bundleInterface;
    
    public DhcpRelayConfigSetting(){
        
    }
    public DhcpRelayConfigSetting(String bundleInterface, DhcpBundle dhcpBundle, DhcpGiaddrConfig[] dhcpGiaddr, 
            DhcpOption60[] dhcpOption60, DhcpServerConfig[] dhcpServer, DhcpRelayVlanMap dhcpRelayVlanMap){
        this.bundleInterface = bundleInterface;
        this.dhcpBundle = dhcpBundle;
        this.dhcpBundle.setBundleInterface(bundleInterface);
        String topCcmtsBundleInterface = this.dhcpBundle.getTopCcmtsDhcpBundleInterface();
        this.dhcpGiAddr = new ArrayList<DhcpGiaddrConfig>();
        this.virtualIp = new ArrayList<DhcpIntIpConfig>();
        this.dhcpBundle.setVirtualPrimaryIpAddr(dhcpGiaddr[0].getTopCcmtsDhcpGiAddress());
        this.dhcpBundle.setVirtualPrimaryIpMask(dhcpGiaddr[0].getTopCcmtsDhcpGiAddrMask());
        for(int i = 0; i < dhcpGiaddr.length; i++){
            if(dhcpGiaddr[i].getTopCcmtsDhcpGiAddress() == null || "".equals(dhcpGiaddr[i].getTopCcmtsDhcpGiAddress())){
                break;
            }
            if(dhcpBundle.getTopCcmtsDhcpBundlePolicy() == EponConstants.DHCP_RELAY_STRICT){
                dhcpGiaddr[i].setTopCcmtsDhcpBundleInterface(topCcmtsBundleInterface);
                this.dhcpGiAddr.add(dhcpGiaddr[i]);
            }
            if(i > 0){
                DhcpIntIpConfig intIpconfig = new DhcpIntIpConfig();
                intIpconfig.setTopCcmtsDhcpBundleInterface(topCcmtsBundleInterface);
                intIpconfig.setTopCcmtsDhcpIntIpAddr(dhcpGiaddr[i].getTopCcmtsDhcpGiAddress());
                intIpconfig.setTopCcmtsDhcpIntIpMask(dhcpGiaddr[i].getTopCcmtsDhcpGiAddrMask());
                virtualIp.add(intIpconfig);
            }            
        }
        this.dhcpServer = new ArrayList<DhcpServerConfig>();
        for(int i = 0; i < dhcpServer.length; i++){
            if(dhcpServer[i].getTopCcmtsDhcpHelperIpAddr() == null || "".equals(dhcpServer[i].getTopCcmtsDhcpHelperIpAddr())){
                break;
            }
            dhcpServer[i].setTopCcmtsDhcpBundleInterface(topCcmtsBundleInterface);
            this.dhcpServer.add(dhcpServer[i]);
            
        }
        this.dhcpOption60 = new ArrayList<DhcpOption60>();
        for(int i = 0; i < dhcpOption60.length; i++){
            if(dhcpOption60[i].getTopCcmtsDhcpOption60Str() == null || "".equals(dhcpOption60[i].getTopCcmtsDhcpOption60Str())){
                break;
            }
            dhcpOption60[i].setTopCcmtsDhcpBundleInterface(topCcmtsBundleInterface);
            this.dhcpOption60.add(dhcpOption60[i]);
        }
        
        this.dhcpRelayVlanMap = dhcpRelayVlanMap;
        this.dhcpRelayVlanMap.setEntityId(entityId);
        this.dhcpRelayVlanMap.setTopCcmtsDhcpBundleInterface(topCcmtsBundleInterface);
    }
    
    /**
     * @return the entityId
     */
    public Long getEntityId() {
        return entityId;
    }
    /**
     * @param entityId the entityId to set
     */
    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }
    /**
     * @return the bundleInterface
     */
    public String getBundleInterface() {
        return bundleInterface;
    }
    /**
     * @param bundleInterface the bundleInterface to set
     */
    public void setBundleInterface(String bundleInterface) {
        this.bundleInterface = bundleInterface;
    }

    public DhcpBundle getDhcpBundle() {
        return dhcpBundle;
    }

    public void setDhcpBundle(DhcpBundle dhcpBundle) {
        this.dhcpBundle = dhcpBundle;
    }

    public List<DhcpGiaddrConfig> getDhcpGiAddr() {
        return dhcpGiAddr;
    }

    public void setDhcpGiAddr(List<DhcpGiaddrConfig> dhcpGiAddr) {
        this.dhcpGiAddr = dhcpGiAddr;
    }

    public List<DhcpOption60> getDhcpOption60() {
        return dhcpOption60;
    }

    public void setDhcpOption60(List<DhcpOption60> dhcpOption60) {
        this.dhcpOption60 = dhcpOption60;
    }

    public List<DhcpServerConfig> getDhcpServer() {
        return dhcpServer;
    }

    public void setDhcpServer(List<DhcpServerConfig> dhcpServer) {
        this.dhcpServer = dhcpServer;
    }

    public List<DhcpIntIpConfig> getVirtualIp() {
        return virtualIp;
    }

    public void setVirtualIp(List<DhcpIntIpConfig> virtualIp) {
        this.virtualIp = virtualIp;
    }
    public DhcpRelayVlanMap getDhcpRelayVlanMap() {
        return dhcpRelayVlanMap;
    }
    public void setDhcpRelayVlanMap(DhcpRelayVlanMap dhcpRelayVlanMap) {
        this.dhcpRelayVlanMap = dhcpRelayVlanMap;
    } 
    
}
