package com.topvision.ems.mobile.domain;

import java.sql.Timestamp;

import com.topvision.framework.dao.mybatis.AliasesSuperType;

public class OnuOpenReport implements AliasesSuperType {
    private static final long serialVersionUID = 8617050627021432951L;
    private String uniqueId;
    // 在线状态
    private String onuOnlineState;
    // 接收功率
    private String onuOptical;
    // wan口状态
    private String wanState;
    // wifi状态
    private String wifiState;
    // NM3000连接状态
    private String nm3000Connect;
    // 网络连接状态
    private String internetConnect;
    // 网络连接状态
    private String nm3000Control;
    // pppoe账号
    private String pppoeParamSet;
    // wifi配置
    private String wifiParamSet;
    // 修改时间
    private Long time;

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public String getOnuOnlineState() {
        return onuOnlineState;
    }

    public void setOnuOnlineState(String onuOnlineState) {
        this.onuOnlineState = onuOnlineState;
    }

    public String getOnuOptical() {
        return onuOptical;
    }

    public void setOnuOptical(String onuOptical) {
        this.onuOptical = onuOptical;
    }

    public String getWanState() {
        return wanState;
    }

    public void setWanState(String wanState) {
        this.wanState = wanState;
    }

    public String getWifiState() {
        return wifiState;
    }

    public void setWifiState(String wifiState) {
        this.wifiState = wifiState;
    }

    public String getNm3000Connect() {
        return nm3000Connect;
    }

    public void setNm3000Connect(String nm3000Connect) {
        this.nm3000Connect = nm3000Connect;
    }

    public String getInternetConnect() {
        return internetConnect;
    }

    public void setInternetConnect(String internetConnect) {
        this.internetConnect = internetConnect;
    }

    public String getNm3000Control() {
        return nm3000Control;
    }

    public void setNm3000Control(String nm3000Control) {
        this.nm3000Control = nm3000Control;
    }

    public String getPppoeParamSet() {
        return pppoeParamSet;
    }

    public void setPppoeParamSet(String pppoeParamSet) {
        this.pppoeParamSet = pppoeParamSet;
    }

    public String getWifiParamSet() {
        return wifiParamSet;
    }

    public void setWifiParamSet(String wifiParamSet) {
        this.wifiParamSet = wifiParamSet;
    }

    public Long getTime() {
        return time;
    }
    
    public Timestamp getTimeStamp() {
        return new Timestamp(time);
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("OnuOpenReport [uniqueId=");
        builder.append(uniqueId);
        builder.append(", onuOnlineState=");
        builder.append(onuOnlineState);
        builder.append(", onuOptical=");
        builder.append(onuOptical);
        builder.append(", wanState=");
        builder.append(wanState);
        builder.append(", wifiState=");
        builder.append(wifiState);
        builder.append(", nm3000Connect=");
        builder.append(nm3000Connect);
        builder.append(", internetConnect=");
        builder.append(internetConnect);
        builder.append(", nm3000Control=");
        builder.append(nm3000Control);
        builder.append(", pppoeParamSet=");
        builder.append(pppoeParamSet);
        builder.append(", wifiParamSet=");
        builder.append(wifiParamSet);
        builder.append(", time=");
        builder.append(time);
        builder.append("]");
        return builder.toString();
    }

}
