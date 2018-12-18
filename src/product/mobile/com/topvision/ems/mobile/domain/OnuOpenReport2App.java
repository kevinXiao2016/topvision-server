package com.topvision.ems.mobile.domain;

import java.io.Serializable;
import java.util.Map;

public class OnuOpenReport2App implements Serializable{
    private static final long serialVersionUID = -874268425628983175L;
    private String mac;
    // 在线状态
    private Map onuOnlineState;
    // 接收功率
    private Map onuOptical;
    // wan口状态
    private Map wanState;
    // wifi状态
    private Map wifiState;
    // NM3000连接状态
    private Map nm3000Connect;
    // 网络连接状态
    private Map internetConnect;
    // 网络连接状态
    private Map nm3000Control;
    // 修改时间
    private String time;

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public Map getOnuOnlineState() {
        return onuOnlineState;
    }

    public void setOnuOnlineState(Map onuOnlineState) {
        this.onuOnlineState = onuOnlineState;
    }

    public Map getOnuOptical() {
        return onuOptical;
    }

    public void setOnuOptical(Map onuOptical) {
        this.onuOptical = onuOptical;
    }

    public Map getWanState() {
        return wanState;
    }

    public void setWanState(Map wanState) {
        this.wanState = wanState;
    }

    public Map getWifiState() {
        return wifiState;
    }

    public void setWifiState(Map wifiState) {
        this.wifiState = wifiState;
    }

    public Map getNm3000Connect() {
        return nm3000Connect;
    }

    public void setNm3000Connect(Map nm3000Connect) {
        this.nm3000Connect = nm3000Connect;
    }

    public Map getInternetConnect() {
        return internetConnect;
    }

    public void setInternetConnect(Map internetConnect) {
        this.internetConnect = internetConnect;
    }

    public Map getNm3000Control() {
        return nm3000Control;
    }

    public void setNm3000Control(Map nm3000Control) {
        this.nm3000Control = nm3000Control;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

}
