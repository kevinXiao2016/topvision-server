package com.topvision.ems.cmc.dhcp.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.topvision.ems.cmc.dhcp.facade.domain.CmcDhcpBundle;
import com.topvision.ems.cmc.dhcp.facade.domain.CmcDhcpGiAddr;
import com.topvision.ems.cmc.dhcp.facade.domain.CmcDhcpIntIp;
import com.topvision.ems.cmc.dhcp.facade.domain.CmcDhcpOption60;
import com.topvision.ems.cmc.dhcp.facade.domain.CmcDhcpPacketVlan;
import com.topvision.ems.cmc.dhcp.facade.domain.CmcDhcpServerConfig;
import com.topvision.framework.snmp.RowStatus;

public class DhcpRelayConfigSetting implements Serializable {
    private static final long serialVersionUID = -8643132720571532006L;
    private Long entityId;
    private CmcDhcpBundle cmcDhcpBundle;
    private List<CmcDhcpGiAddr> cmcDhcpGiAddr;
    private List<CmcDhcpOption60> cmcDhcpOption60;
    private List<CmcDhcpServerConfig> cmcDhcpServer;
    private List<CmcDhcpPacketVlan> cmcDhcpPacketVlan;
    private List<CmcDhcpIntIp> virtualIp;
    private String bundleInterface;
    private Integer policy;
    private Integer cableSourceVerify;
    private String primaryIp;
    private String primaryIpMask;
    private List<String> giAddrList;
    private List<String> giAddrMaskList;
    private List<Integer> giAddrTypeList;
    private List<String> addOption60;
    private List<Integer> addOption60Type;
    private List<String> addServerList;
    private List<Integer> addServerTypeList;
    private List<String> addVirtualIp;
    private List<String> addVirtualIpMask;
    private List<Long> delOption60;
    private List<Long> delServerList;
    private List<String> delVirtualIp;
    private List<Integer> vlan;
    private List<Integer> priotity;
    private List<String> bundleListEnd;
    
    public void addInitialization(){
        cmcDhcpBundle = new CmcDhcpBundle();
        cmcDhcpBundle.setEntityId(entityId);
        cmcDhcpBundle.setTopCcmtsDhcpBundleInterface(bundleInterface);
        cmcDhcpBundle.setTopCcmtsDhcpBundlePolicy(policy);
        cmcDhcpBundle.setVirtualPrimaryIpAddr(primaryIp);
        cmcDhcpBundle.setVirtualPrimaryIpMask(primaryIpMask);
        cmcDhcpBundle.setCableSourceVerify(cableSourceVerify);
        cmcDhcpBundle.setTopCcmtsDhcpBundleStatus(RowStatus.CREATE_AND_GO);
        
        cmcDhcpGiAddr = new ArrayList<CmcDhcpGiAddr>();
        virtualIp = new ArrayList<CmcDhcpIntIp> ();
        if(giAddrList != null){
            for(int i = 0; i < giAddrList.size(); i++){
                if(giAddrList.get(i) != null && !"0.0.0.0".equals(giAddrList.get(i)) &&
                        !"".equals(giAddrList.get(i))){
                    CmcDhcpGiAddr giAddr = new CmcDhcpGiAddr();
                    giAddr.setTopCcmtsDhcpGiAddrDeviceType(giAddrTypeList.get(i));
                    giAddr.setTopCcmtsDhcpBundleInterface(bundleInterface);
                    giAddr.setEntityId(entityId);
                    giAddr.setTopCcmtsDhcpGiAddress(giAddrList.get(i));
                    giAddr.setTopCcmtsDhcpGiAddrMask(giAddrMaskList.get(i));
                    giAddr.setTopCcmtsDhcpGiAddrStatus(RowStatus.CREATE_AND_GO);
                    cmcDhcpGiAddr.add(giAddr);
                }                
            }
        }        
        
        if(addOption60 != null){
            cmcDhcpOption60 = new ArrayList<CmcDhcpOption60>();
            for(int i = 0; i< addOption60.size(); i++){
                CmcDhcpOption60 option60 = new CmcDhcpOption60();
                option60.setEntityId(entityId);
                option60.setTopCcmtsDhcpOption60DeviceType(addOption60Type.get(i));
                option60.setTopCcmtsDhcpOption60Str(addOption60.get(i));
                option60.setTopCcmtsDhcpBundleInterface(bundleInterface);
                option60.setTopCcmtsDhcpOption60Status(RowStatus.CREATE_AND_GO);
                cmcDhcpOption60.add(option60);
            }
        }
        
        if(addServerList != null ){
            cmcDhcpServer = new ArrayList<CmcDhcpServerConfig>();
            for(int i = 0; i < addServerList.size(); i++){
                //addServerTypeList
                CmcDhcpServerConfig server = new CmcDhcpServerConfig();
                server.setTopCcmtsDhcpBundleInterface(bundleInterface);
                server.setEntityId(entityId);
                server.setTopCcmtsDhcpHelperIpAddr(addServerList.get(i));
                server.setTopCcmtsDhcpHelperDeviceType(addServerTypeList.get(i));
                server.setTopCcmtsDhcpHelperStatus(RowStatus.CREATE_AND_GO);
                cmcDhcpServer.add(server);
            }
        }
        
        if(addVirtualIp != null){
            virtualIp = new ArrayList<CmcDhcpIntIp>();
            for(int i = 0; i < addVirtualIp.size(); i++){
                CmcDhcpIntIp ip = new CmcDhcpIntIp();
                ip.setTopCcmtsDhcpBundleInterface(bundleInterface);
                ip.setTopCcmtsDhcpIntIpAddr(addVirtualIp.get(i));
                ip.setTopCcmtsDhcpIntIpMask(addVirtualIpMask.get(i));
                ip.setEntityId(entityId);
                ip.setTopCcmtsDhcpIntIpStatus(RowStatus.CREATE_AND_GO);
                virtualIp.add(ip);
            }
        }     
        if(vlan != null && priotity != null){
            cmcDhcpPacketVlan = new ArrayList<CmcDhcpPacketVlan>();
            for(int i = 0; i < vlan.size(); i++){
                CmcDhcpPacketVlan packetVlan = new CmcDhcpPacketVlan();
                packetVlan.setTopCcmtsDhcpBundleInterface(bundleInterface);
                packetVlan.setEntityId(entityId);
                packetVlan.setTopCcmtsDhcpDeviceType(i+1);
                packetVlan.setTopCcmtsDhcpPacketVlanStatus(RowStatus.CREATE_AND_GO);
                packetVlan.setTopCcmtsDhcpTagVlan(vlan.get(i));
                packetVlan.setTopCcmtsDhcpTagPriority(priotity.get(i));
                cmcDhcpPacketVlan.add(packetVlan);
            }
        }      
        
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
     * @return the cmcDhcpBundle
     */
    public CmcDhcpBundle getCmcDhcpBundle() {
        return cmcDhcpBundle;
    }
    /**
     * @param cmcDhcpBundle the cmcDhcpBundle to set
     */
    public void setCmcDhcpBundle(CmcDhcpBundle cmcDhcpBundle) {
        this.cmcDhcpBundle = cmcDhcpBundle;
    }
    /**
     * @return the cmcDhcpGiAddr
     */
    public List<CmcDhcpGiAddr> getCmcDhcpGiAddr() {
        return cmcDhcpGiAddr;
    }
    /**
     * @param cmcDhcpGiAddr the cmcDhcpGiAddr to set
     */
    public void setCmcDhcpGiAddr(List<CmcDhcpGiAddr> cmcDhcpGiAddr) {
        this.cmcDhcpGiAddr = cmcDhcpGiAddr;
    }
    /**
     * @return the cmcDhcpOption60
     */
    public List<CmcDhcpOption60> getCmcDhcpOption60() {
        return cmcDhcpOption60;
    }
    /**
     * @param cmcDhcpOption60 the cmcDhcpOption60 to set
     */
    public void setCmcDhcpOption60(List<CmcDhcpOption60> cmcDhcpOption60) {
        this.cmcDhcpOption60 = cmcDhcpOption60;
    }
    /**
     * @return the cmcDhcpServer
     */
    public List<CmcDhcpServerConfig> getCmcDhcpServer() {
        return cmcDhcpServer;
    }
    /**
     * @param cmcDhcpServer the cmcDhcpServer to set
     */
    public void setCmcDhcpServer(List<CmcDhcpServerConfig> cmcDhcpServer) {
        this.cmcDhcpServer = cmcDhcpServer;
    }
    /**
     * @return the cmcDhcpPacketVlan
     */
    public List<CmcDhcpPacketVlan> getCmcDhcpPacketVlan() {
        return cmcDhcpPacketVlan;
    }
    /**
     * @param cmcDhcpPacketVlan the cmcDhcpPacketVlan to set
     */
    public void setCmcDhcpPacketVlan(List<CmcDhcpPacketVlan> cmcDhcpPacketVlan) {
        this.cmcDhcpPacketVlan = cmcDhcpPacketVlan;
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
    /**
     * @return the policy
     */
    public Integer getPolicy() {
        return policy;
    }
    /**
     * @param policy the policy to set
     */
    public void setPolicy(Integer policy) {
        this.policy = policy;
    }
    /**
     * @return the cableSourceVerify
     */
    public Integer getCableSourceVerify() {
        return cableSourceVerify;
    }
    /**
     * @param cableSourceVerify the cableSourceVerify to set
     */
    public void setCableSourceVerify(Integer cableSourceVerify) {
        this.cableSourceVerify = cableSourceVerify;
    }
    /**
     * @return the primaryIp
     */
    public String getPrimaryIp() {
        return primaryIp;
    }
    /**
     * @param primaryIp the primaryIp to set
     */
    public void setPrimaryIp(String primaryIp) {
        this.primaryIp = primaryIp;
    }
    /**
     * @return the primaryIpMask
     */
    public String getPrimaryIpMask() {
        return primaryIpMask;
    }
    /**
     * @param primaryIpMask the primaryIpMask to set
     */
    public void setPrimaryIpMask(String primaryIpMask) {
        this.primaryIpMask = primaryIpMask;
    }
    /**
     * @return the giAddrList
     */
    public List<String> getGiAddrList() {
        return giAddrList;
    }
    /**
     * @param giAddrList the giAddrList to set
     */
    public void setGiAddrList(List<String> giAddrList) {
        this.giAddrList = giAddrList;
    }
    /**
     * @return the giAddrMaskList
     */
    public List<String> getGiAddrMaskList() {
        return giAddrMaskList;
    }
    /**
     * @param giAddrMaskList the giAddrMaskList to set
     */
    public void setGiAddrMaskList(List<String> giAddrMaskList) {
        this.giAddrMaskList = giAddrMaskList;
    }
    /**
     * @return the giAddrTypeList
     */
    public List<Integer> getGiAddrTypeList() {
        return giAddrTypeList;
    }
    /**
     * @param giAddrTypeList the giAddrTypeList to set
     */
    public void setGiAddrTypeList(List<Integer> giAddrTypeList) {
        this.giAddrTypeList = giAddrTypeList;
    }
    /**
     * @return the addOption60
     */
    public List<String> getAddOption60() {
        return addOption60;
    }
    /**
     * @param addOption60 the addOption60 to set
     */
    public void setAddOption60(List<String> addOption60) {
        this.addOption60 = addOption60;
    }
    /**
     * @return the addOption60Type
     */
    public List<Integer> getAddOption60Type() {
        return addOption60Type;
    }
    /**
     * @param addOption60Type the addOption60Type to set
     */
    public void setAddOption60Type(List<Integer> addOption60Type) {
        this.addOption60Type = addOption60Type;
    }
    /**
     * @return the addServerList
     */
    public List<String> getAddServerList() {
        return addServerList;
    }
    /**
     * @param addServerList the addServerList to set
     */
    public void setAddServerList(List<String> addServerList) {
        this.addServerList = addServerList;
    }
    /**
     * @return the addServerTypeList
     */
    public List<Integer> getAddServerTypeList() {
        return addServerTypeList;
    }
    /**
     * @param addServerTypeList the addServerTypeList to set
     */
    public void setAddServerTypeList(List<Integer> addServerTypeList) {
        this.addServerTypeList = addServerTypeList;
    }
    /**
     * @return the addVirtualIp
     */
    public List<String> getAddVirtualIp() {
        return addVirtualIp;
    }
    /**
     * @param addVirtualIp the addVirtualIp to set
     */
    public void setAddVirtualIp(List<String> addVirtualIp) {
        this.addVirtualIp = addVirtualIp;
    }
    /**
     * @return the addVirtualIpMask
     */
    public List<String> getAddVirtualIpMask() {
        return addVirtualIpMask;
    }
    /**
     * @param addVirtualIpMask the addVirtualIpMask to set
     */
    public void setAddVirtualIpMask(List<String> addVirtualIpMask) {
        this.addVirtualIpMask = addVirtualIpMask;
    }

    /**
     * @return the virtualIp
     */
    public List<CmcDhcpIntIp> getVirtualIp() {
        return virtualIp;
    }

    /**
     * @param virtualIp the virtualIp to set
     */
    public void setVirtualIp(List<CmcDhcpIntIp> virtualIp) {
        this.virtualIp = virtualIp;
    }

    /**
     * @return the delOption60
     */
    public List<Long> getDelOption60() {
        return delOption60;
    }

    /**
     * @param delOption60 the delOption60 to set
     */
    public void setDelOption60(List<Long> delOption60) {
        this.delOption60 = delOption60;
    }

    /**
     * @return the delServerList
     */
    public List<Long> getDelServerList() {
        return delServerList;
    }

    /**
     * @param delServerList the delServerList to set
     */
    public void setDelServerList(List<Long> delServerList) {
        this.delServerList = delServerList;
    }

    /**
     * @return the delVirtualIp
     */
    public List<String> getDelVirtualIp() {
        return delVirtualIp;
    }

    /**
     * @param delVirtualIp the delVirtualIp to set
     */
    public void setDelVirtualIp(List<String> delVirtualIp) {
        this.delVirtualIp = delVirtualIp;
    }

    /**
     * @return the vlan
     */
    public List<Integer> getVlan() {
        return vlan;
    }

    /**
     * @param vlan the vlan to set
     */
    public void setVlan(List<Integer> vlan) {
        this.vlan = vlan;
    }

    /**
     * @return the priotity
     */
    public List<Integer> getPriotity() {
        return priotity;
    }

    /**
     * @param priotity the priotity to set
     */
    public void setPriotity(List<Integer> priotity) {
        this.priotity = priotity;
    }

    /**
     * @return the bundleListEnd
     */
    public List<String> getBundleListEnd() {
        return bundleListEnd;
    }

    /**
     * @param bundleListEnd the bundleListEnd to set
     */
    public void setBundleListEnd(List<String> bundleListEnd) {
        this.bundleListEnd = bundleListEnd;
    }
    
}
