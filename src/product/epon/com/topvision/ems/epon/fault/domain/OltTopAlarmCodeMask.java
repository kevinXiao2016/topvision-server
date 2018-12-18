/***********************************************************************
 * $Id: OltTopAlarmCodeMask.java,v1.0 2011-11-21 下午01:27:26 $
 * 
 * @author: zhanglongyang
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.fault.domain;

import java.io.Serializable;

import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * OLT告警类型屏蔽
 * 
 * @author zhanglongyang
 * @created @2011-11-21-下午01:27:26
 * 
 */
public class OltTopAlarmCodeMask implements Serializable, AliasesSuperType {
    private static final long serialVersionUID = -739387330975789544L;
    // 设备ID
    private Long entityId;
    // 类型屏蔽索引
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.2.11.4.1.1.1", index = true)
    private Long topAlarmCodeMaskIndex;
    private String alarmName;
    private String alarmSeverity;
    // 类型屏蔽状态
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.2.11.4.1.1.2", writable = true, type = "Integer32")
    private Integer topAlarmCodeMaskEnable;
    // 行创建状态
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.2.11.4.1.1.3", writable = true, type = "Integer32")
    private Integer topAlarmCodeMaskRowStatus;

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public Long getTopAlarmCodeMaskIndex() {
        return topAlarmCodeMaskIndex;
    }

    public void setTopAlarmCodeMaskIndex(Long topAlarmCodeMaskIndex) {
        this.topAlarmCodeMaskIndex = topAlarmCodeMaskIndex;
    }

    public String getAlarmName() {
        return alarmName;
    }

    public void setAlarmName(String alarmName) {
        this.alarmName = alarmName;
    }

    public String getAlarmSeverity() {
        return alarmSeverity;
    }

    public void setAlarmSeverity(String alarmSeverity) {
        this.alarmSeverity = alarmSeverity;
    }

    public Integer getTopAlarmCodeMaskEnable() {
        return topAlarmCodeMaskEnable;
    }

    public void setTopAlarmCodeMaskEnable(Integer topAlarmCodeMaskEnable) {
        this.topAlarmCodeMaskEnable = topAlarmCodeMaskEnable;
    }

    public Integer getTopAlarmCodeMaskRowStatus() {
        return topAlarmCodeMaskRowStatus;
    }

    public void setTopAlarmCodeMaskRowStatus(Integer topAlarmCodeMaskRowStatus) {
        this.topAlarmCodeMaskRowStatus = topAlarmCodeMaskRowStatus;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("OltTopAlarmCodeMask{");
        sb.append("entityId=").append(entityId);
        sb.append(", topAlarmCodeMaskIndex=").append(topAlarmCodeMaskIndex);
        sb.append(", alarmName=").append(alarmName);
        sb.append(", topAlarmCodeMaskEnable=").append(topAlarmCodeMaskEnable);
        sb.append(", topAlarmCodeMaskRowStatus=").append(topAlarmCodeMaskRowStatus);
        sb.append('}');
        return sb.toString();
    }
}
