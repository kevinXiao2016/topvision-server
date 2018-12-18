package com.topvision.ems.cmc.dhcp.domain;

import java.io.Serializable;
import java.util.List;

public class DhcpRelayConfig implements Serializable {
    private static final long serialVersionUID = 4581680385664448506L;
    private String topCcmtsDhcpBundleInterface;
    private Integer topCcmtsDhcpHelperDeviceType;
    private List<String> topCcmtsDhcpOption60Str;
    private List<String> topCcmtsDhcpHelperIpAddr;
    private List<String> topCcmtsDhcpGiAddress;
    private List<String> topCcmtsDhcpGiAddrMask;
    private Integer vlanTag;
    private Integer vlanPriority;
    private Integer cableSourceVerify;
    private Integer policy;
    private Integer serverSize;
    private Integer bundleNum;

    /**
     * @return the topCcmtsDhcpBundleInterface
     */
    public String getTopCcmtsDhcpBundleInterface() {
        return topCcmtsDhcpBundleInterface;
    }

    /**
     * @param topCcmtsDhcpBundleInterface
     *            the topCcmtsDhcpBundleInterface to set
     */
    public void setTopCcmtsDhcpBundleInterface(String topCcmtsDhcpBundleInterface) {
        this.topCcmtsDhcpBundleInterface = topCcmtsDhcpBundleInterface;
        if (topCcmtsDhcpBundleInterface != null) {
            String[] split = topCcmtsDhcpBundleInterface.replace("bundle", "").split("\\.");
            if (split.length > 1) {
                this.bundleNum = Integer.parseInt(split[0].trim()) + Integer.parseInt(split[1].trim());
            } else {
                this.bundleNum = Integer.parseInt(split[0].trim());
            }
        }
    }

    /**
     * @return the topCcmtsDhcpHelperDeviceType
     */
    public Integer getTopCcmtsDhcpHelperDeviceType() {
        return topCcmtsDhcpHelperDeviceType;
    }

    /**
     * @param topCcmtsDhcpHelperDeviceType
     *            the topCcmtsDhcpHelperDeviceType to set
     */
    public void setTopCcmtsDhcpHelperDeviceType(Integer topCcmtsDhcpHelperDeviceType) {
        this.topCcmtsDhcpHelperDeviceType = topCcmtsDhcpHelperDeviceType;
    }

    /**
     * @return the topCcmtsDhcpOption60Str
     */
    public List<String> getTopCcmtsDhcpOption60Str() {
        return topCcmtsDhcpOption60Str;
    }

    /**
     * @param topCcmtsDhcpOption60Str
     *            the topCcmtsDhcpOption60Str to set
     */
    public void setTopCcmtsDhcpOption60Str(List<String> topCcmtsDhcpOption60Str) {
        this.topCcmtsDhcpOption60Str = topCcmtsDhcpOption60Str;
    }

    /**
     * @return the topCcmtsDhcpHelperIpAddr
     */
    public List<String> getTopCcmtsDhcpHelperIpAddr() {
        return topCcmtsDhcpHelperIpAddr;
    }

    /**
     * @param topCcmtsDhcpHelperIpAddr
     *            the topCcmtsDhcpHelperIpAddr to set
     */
    public void setTopCcmtsDhcpHelperIpAddr(List<String> topCcmtsDhcpHelperIpAddr) {
        this.topCcmtsDhcpHelperIpAddr = topCcmtsDhcpHelperIpAddr;
    }

    /**
     * @return the topCcmtsDhcpGiAddress
     */
    public List<String> getTopCcmtsDhcpGiAddress() {
        return topCcmtsDhcpGiAddress;
    }

    /**
     * @param topCcmtsDhcpGiAddress
     *            the topCcmtsDhcpGiAddress to set
     */
    public void setTopCcmtsDhcpGiAddress(List<String> topCcmtsDhcpGiAddress) {
        this.topCcmtsDhcpGiAddress = topCcmtsDhcpGiAddress;
    }

    /**
     * @return the topCcmtsDhcpGiAddrMask
     */
    public List<String> getTopCcmtsDhcpGiAddrMask() {
        return topCcmtsDhcpGiAddrMask;
    }

    /**
     * @param topCcmtsDhcpGiAddrMask
     *            the topCcmtsDhcpGiAddrMask to set
     */
    public void setTopCcmtsDhcpGiAddrMask(List<String> topCcmtsDhcpGiAddrMask) {
        this.topCcmtsDhcpGiAddrMask = topCcmtsDhcpGiAddrMask;
    }

    /**
     * @return the vlanTag
     */
    public Integer getVlanTag() {
        return vlanTag;
    }

    /**
     * @param vlanTag
     *            the vlanTag to set
     */
    public void setVlanTag(Integer vlanTag) {
        this.vlanTag = vlanTag;
    }

    /**
     * @return the vlanPriority
     */
    public Integer getVlanPriority() {
        return vlanPriority;
    }

    /**
     * @param vlanPriority
     *            the vlanPriority to set
     */
    public void setVlanPriority(Integer vlanPriority) {
        this.vlanPriority = vlanPriority;
    }

    /**
     * @return the cableSourceVerify
     */
    public Integer getCableSourceVerify() {
        return cableSourceVerify;
    }

    /**
     * @param cableSourceVerify
     *            the cableSourceVerify to set
     */
    public void setCableSourceVerify(Integer cableSourceVerify) {
        this.cableSourceVerify = cableSourceVerify;
    }

    /**
     * @return the policy
     */
    public Integer getPolicy() {
        return policy;
    }

    /**
     * @param policy
     *            the policy to set
     */
    public void setPolicy(Integer policy) {
        this.policy = policy;
    }

    /**
     * @return the serialversionuid
     */
    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public Integer getServerSize() {
        return serverSize;
    }

    public void setServerSize(Integer serverSize) {
        this.serverSize = serverSize;
    }

    public Integer getBundleNum() {
        return bundleNum;
    }

    public void setBundleNum(Integer bundleNum) {
        this.bundleNum = bundleNum;
    }

}
