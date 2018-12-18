/***********************************************************************
 * $Id: OltAlertInstance.java,v1.0 2011-11-22 下午06:34:27 $
 * 
 * @author: zhanglongyang
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.domain;

import java.io.Serializable;

import com.topvision.framework.utils.EponIndex;
import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * OLT告警实体
 * 
 * @author zhanglongyang
 * @created @2011-11-22-下午06:34:27
 * 
 */
public class OltAlertInstance implements Serializable, AliasesSuperType {
    private static final long serialVersionUID = -8172943564694456588L;
    private Long instanceIndex;
    private String instanceType;
    private String instanceState;
    private Long slotNo;

    public Long getInstanceIndex() {
        return instanceIndex;
    }

    public void setInstanceIndex(Long instanceIndex) {
        this.instanceIndex = instanceIndex;
        // 设置实体说明
        StringBuilder sb = new StringBuilder();
        sb.append(EponIndex.getSlotNo(instanceIndex));
        sb.append("_");
        sb.append(EponIndex.getSniNo(instanceIndex));
        sb.append("_");
        sb.append(EponIndex.getOnuNo(instanceIndex));
        sb.append("_");
        sb.append(EponIndex.getUniNo(instanceIndex));
        this.instanceState = sb.toString();
    }

    public String getInstanceType() {
        return instanceType;
    }

    public void setInstanceType(String instanceType) {
        this.instanceType = instanceType;
    }

    public String getInstanceState() {
        return instanceState;
    }

    public void setInstanceState(String instanceState) {
        this.instanceState = instanceState;
    }

    public Long getSlotNo() {
        return slotNo;
    }

    public void setSlotNo(Long slotNo) {
        this.slotNo = slotNo;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("OltAlertInstance{");
        sb.append("instanceIndex=").append(instanceIndex);
        sb.append(", instanceType=").append(instanceType);
        sb.append(", instanceState=").append(instanceState);
        sb.append(", slotNo=").append(slotNo);
        sb.append('}');
        return sb.toString();
    }
}
