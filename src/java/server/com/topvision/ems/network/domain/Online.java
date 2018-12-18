package com.topvision.ems.network.domain;

import java.io.Serializable;

public class Online implements Serializable {
    private static final long serialVersionUID = 2103375456900237902L;
    private String mac;
    private String ip;
    private String name;
    private Boolean online;
    private Integer response;
    private Long updateTime = -1L;

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
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @return the response
     */
    public Integer getResponse() {
        return response;
    }

    /**
     * @return the updateTime
     */
    public Long getUpdateTime() {
        return updateTime;
    }

    /**
     * @return the online
     */
    public Boolean isOnline() {
        return online;
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

    /**
     * @param name
     *            the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @param online
     *            the online to set
     */
    public void setOnline(Boolean online) {
        this.online = online;
    }

    /**
     * @param response
     *            the response to set
     */
    public void setResponse(Integer response) {
        this.response = response;
    }

    /**
     * @param updateTime
     *            the updateTime to set
     */
    public void setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("[Mac:");
        str.append(mac);
        str.append(", Ip:");
        str.append(ip);
        str.append(", Name:");
        str.append(name);
        str.append(", Online:");
        str.append(online);
        str.append(", Response:");
        str.append(response);
        str.append("]");

        return str.toString();
    }
}
