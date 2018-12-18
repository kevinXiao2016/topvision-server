/***********************************************************************
 * $ Count.java,v1.0 2014-1-21 17:18:06 $
 *
 * @author: YangYi
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.spectrum.domain;

import java.io.Serializable;

/**
 * @author YangYi
 * @created @2014-1-21-17:18:06
 */
public class CmtsSpectrumConfig implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long cmcId;
    private Long entityId;//对于整合型，取上联OLT 的Id
    private String cmtsName;
    private String cmtsIp;
    private String oltCollectSwitch;
    private String cmtsCollectSwitch;
    private String hisVideoSwitch; // 历史频谱开关 1为开启，0为关闭
    private Long typeId;
    private String uplinkDevice;

    public Long getCmcId() {
        return cmcId;
    }

    public void setCmcId(Long cmcId) {
        this.cmcId = cmcId;
    }

    public Long getTypeId() {
        return typeId;
    }

    public void setTypeId(Long typeId) {
        this.typeId = typeId;
    }

    public String getCmtsName() {
        return cmtsName;
    }

    public void setCmtsName(String cmtsName) {
        this.cmtsName = cmtsName;
    }

    public String getCmtsIp() {
        return cmtsIp;
    }

    public void setCmtsIp(String cmtsIp) {
        this.cmtsIp = cmtsIp;
    }

    public String getOltCollectSwitch() {
        return oltCollectSwitch;
    }

    public void setOltCollectSwitch(String oltCollectSwitch) {
        this.oltCollectSwitch = oltCollectSwitch;
    }

    public String getCmtsCollectSwitch() {
        return cmtsCollectSwitch;
    }

    public void setCmtsCollectSwitch(String cmtsCollectSwitch) {
        this.cmtsCollectSwitch = cmtsCollectSwitch;
    }

    public String getHisVideoSwitch() {
        return hisVideoSwitch;
    }

    public void setHisVideoSwitch(String hisVideoSwitch) {
        this.hisVideoSwitch = hisVideoSwitch;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public String getUplinkDevice() {
        return uplinkDevice;
    }

    public void setUplinkDevice(String uplinkDevice) {
        this.uplinkDevice = uplinkDevice;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("CmtsSpectrumConfig [cmcId=");
        builder.append(cmcId);
        builder.append(", entityId=");
        builder.append(entityId);
        builder.append(", cmtsName=");
        builder.append(cmtsName);
        builder.append(", cmtsIp=");
        builder.append(cmtsIp);
        builder.append(", oltCollectSwitch=");
        builder.append(oltCollectSwitch);
        builder.append(", cmtsCollectSwitch=");
        builder.append(cmtsCollectSwitch);
        builder.append(", hisVideoSwitch=");
        builder.append(hisVideoSwitch);
        builder.append(", typeId=");
        builder.append(typeId);
        builder.append(", uplinkDevice=");
        builder.append(uplinkDevice);
        builder.append("]");
        return builder.toString();
    }

}
