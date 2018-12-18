package com.topvision.ems.network.domain;

@SuppressWarnings("serial")
public class PortEx extends Port {
    private String entityIp;
    private String entityName;
    private String mac;
    private String ip;
    private String destName;
    private String destType;
    private String destIfIndex;
    private String destIfName;

    /**
     * @return the destIfIndex
     */
    public String getDestIfIndex() {
        return destIfIndex;
    }

    /**
     * @return the destIfName
     */
    public String getDestIfName() {
        return destIfName;
    }

    /**
     * @return the destName
     */
    public String getDestName() {
        return destName;
    }

    /**
     * @return the destType
     */
    public String getDestType() {
        return destType;
    }

    /**
     * @return the entityIp
     */
    public String getEntityIp() {
        return entityIp;
    }

    /**
     * @return the entityName
     */
    public String getEntityName() {
        return entityName;
    }

    /**
     * @return the ip
     */
    public String getIp() {
        return ip;
    }

    /**
     * @return the mac
     */
    public String getMac() {
        return mac;
    }

    /**
     * @param destIfIndex
     *            the destIfIndex to set
     */
    public void setDestIfIndex(String destIfIndex) {
        this.destIfIndex = destIfIndex;
    }

    /**
     * @param destIfName
     *            the destIfName to set
     */
    public void setDestIfName(String destIfName) {
        this.destIfName = destIfName;
    }

    /**
     * @param destName
     *            the destName to set
     */
    public void setDestName(String destName) {
        this.destName = destName;
    }

    /**
     * @param destType
     *            the destType to set
     */
    public void setDestType(String destType) {
        this.destType = destType;
    }

    /**
     * @param entityIp
     *            the entityIp to set
     */
    public void setEntityIp(String entityIp) {
        this.entityIp = entityIp;
    }

    /**
     * @param entityName
     *            the entityName to set
     */
    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    /**
     * @param ip
     *            the ip to set
     */
    public void setIp(String ip) {
        this.ip = ip;
    }

    /**
     * @param mac
     *            the mac to set
     */
    public void setMac(String mac) {
        this.mac = mac;
    }
}
