package com.topvision.ems.epon.onu.domain;

import java.util.List;

public class EponOnuBusinessInfo extends OnuCommonInfo {

    private static final long serialVersionUID = 4400974208203781279L;

    private List<UniPort> uniPorts;// uni
    private Integer cpeNum;// cpe数量
    private List<OnuWanConnect> wanConnects; // wan
    private List<OnuWanSsid> wanSsids;// wlan信息

    public Integer getCpeNum() {
        return cpeNum;
    }

    public void setCpeNum(Integer cpeNum) {
        this.cpeNum = cpeNum;
    }

    public List<OnuWanSsid> getWanSsids() {
        return wanSsids;
    }

    public void setWanSsids(List<OnuWanSsid> wanSsids) {
        this.wanSsids = wanSsids;
    }

    public List<UniPort> getUniPorts() {
        return uniPorts;
    }

    public void setUniPorts(List<UniPort> uniPorts) {
        this.uniPorts = uniPorts;
    }

    public List<OnuWanConnect> getWanConnects() {
        return wanConnects;
    }

    public void setWanConnects(List<OnuWanConnect> wanConnects) {
        this.wanConnects = wanConnects;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("OnuDeviceInfo [onuId=");
        builder.append(onuId);
        builder.append(", onuIndex=");
        builder.append(onuIndex);
        builder.append(", entityId=");
        builder.append(entityId);
        builder.append(", entityIp=");
        builder.append(entityIp);
        builder.append(", name=");
        builder.append(name);
        builder.append(", onuOperationStatus=");
        builder.append(onuOperationStatus);
        builder.append(", onuUniqueIdentification=");
        builder.append(onuUniqueIdentification);
        builder.append(", uniPorts=");
        builder.append(uniPorts);
        builder.append(", cpeNum=");
        builder.append(", tagName=");
        builder.append(tagName);
        builder.append(", tagId=");
        builder.append(tagId);
        builder.append(cpeNum);
        builder.append(", wanInfo=");
        builder.append(wanConnects);
        builder.append(", wanSsids=");
        builder.append(wanSsids);
        builder.append("]");
        return builder.toString();
    }

}
