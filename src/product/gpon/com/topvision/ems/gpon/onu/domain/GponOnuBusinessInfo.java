package com.topvision.ems.gpon.onu.domain;

import java.util.List;

import com.topvision.ems.epon.onu.domain.OnuCommonInfo;
import com.topvision.ems.epon.onu.domain.OnuWanConnect;
import com.topvision.ems.epon.onu.domain.OnuWanSsid;

/**
 * 
 * @author CWQ
 * @created @2017年12月25日-上午9:02:08
 *
 */
public class GponOnuBusinessInfo extends OnuCommonInfo {

    private static final long serialVersionUID = -2814826632483942654L;
    private List<GponUniAttribute> uniPorts;// uni
    private Integer cpeNum;// cpe数量
    private List<OnuWanConnect> wanConnects; // wan
    private List<OnuWanSsid> wanSsids;// wlan信息

    public List<GponUniAttribute> getUniPorts() {
        return uniPorts;
    }

    public void setUniPorts(List<GponUniAttribute> uniPorts) {
        this.uniPorts = uniPorts;
    }

    public Integer getCpeNum() {
        return cpeNum;
    }

    public void setCpeNum(Integer cpeNum) {
        this.cpeNum = cpeNum;
    }

    public List<OnuWanConnect> getWanConnects() {
        return wanConnects;
    }

    public void setWanConnects(List<OnuWanConnect> wanConnects) {
        this.wanConnects = wanConnects;
    }

    public List<OnuWanSsid> getWanSsids() {
        return wanSsids;
    }

    public void setWanSsids(List<OnuWanSsid> wanSsids) {
        this.wanSsids = wanSsids;
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
        builder.append(", tagName=");
        builder.append(tagName);
        builder.append(", tagId=");
        builder.append(tagId);
        builder.append(", uniPorts=");
        builder.append(uniPorts);
        builder.append(", cpeNum=");
        builder.append(cpeNum);
        builder.append(", wanConnects=");
        builder.append(wanConnects);
        builder.append(", wanSsids=");
        builder.append(wanSsids);
        builder.append("]");
        return builder.toString();
    }

}
