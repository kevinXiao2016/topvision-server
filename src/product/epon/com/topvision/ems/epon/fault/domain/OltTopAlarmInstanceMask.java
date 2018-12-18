/***********************************************************************
 * $Id: OltTopAlarmInstanceMask.java,v1.0 2011-11-21 下午01:33:01 $
 * 
 * @author: zhanglongyang
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.fault.domain;

import java.io.Serializable;

import com.topvision.framework.utils.EponIndex;
import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * OLT告警实体屏蔽
 * 
 * @author zhanglongyang
 * @created @2011-11-21-下午01:33:01
 * 
 */
public class OltTopAlarmInstanceMask implements Serializable, AliasesSuperType {
    private static final long serialVersionUID = -2694575859967187416L;
    // 设备ID
    private Long entityId;
    // OLT告警实体屏蔽索引
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.2.11.4.2.1.1", index = true)
    private Long topAlarmInstanceMaskIndex;
    private Long topAlarmInstanceMaskIndex5Byte;
    private String instanceName;
    private String instanceType;
    private Long slotNo;
    // OLT告警实体屏蔽状态
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.2.11.4.2.1.2", writable = true, type = "Integer32")
    private Integer topAlarmInstanceMaskEnable;
    // 行创建状态
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.2.11.4.2.1.3", writable = true, type = "Integer32")
    private Integer topAlarmInstanceMaskRowStatus;

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public Long getTopAlarmInstanceMaskIndex() {
        return topAlarmInstanceMaskIndex;
    }

    public void setTopAlarmInstanceMaskIndex(Long topAlarmInstanceMaskIndex) {
        this.topAlarmInstanceMaskIndex = topAlarmInstanceMaskIndex;
        // 设置系统中使用的Index类型
        this.topAlarmInstanceMaskIndex5Byte = EponIndex.getInstanceIndexByAlertIndex(topAlarmInstanceMaskIndex);
    }

    public Long getTopAlarmInstanceMaskIndex5Byte() {
        return topAlarmInstanceMaskIndex5Byte;
    }

    public Long getSlotNo() {
        return slotNo;
    }

    public void setSlotNo(Long slotNo) {
        this.slotNo = slotNo;
    }

    public void setTopAlarmInstanceMaskIndex5Byte(Long topAlarmInstanceMaskIndex5Byte) {
        this.topAlarmInstanceMaskIndex5Byte = topAlarmInstanceMaskIndex5Byte;
        Long slotNo = EponIndex.getSlotNo(topAlarmInstanceMaskIndex5Byte);
        Long portNo = EponIndex.getSniNo(topAlarmInstanceMaskIndex5Byte);
        Long onuNo = EponIndex.getOnuNo(topAlarmInstanceMaskIndex5Byte);
        Long uniNo = EponIndex.getUniNo(topAlarmInstanceMaskIndex5Byte);
        // 设置实体名称
        StringBuilder sb = new StringBuilder();
        sb.append(slotNo);
        if (portNo > 0) {
            sb.append("/");
            sb.append(portNo);
        }
        if (onuNo > 0) {
            sb.append(":");
            sb.append(onuNo);
        }
        if (uniNo > 0) {
            sb.append("/");
            sb.append(uniNo);
        }
        this.instanceName = sb.toString();
        // 设置实体类型
        if (uniNo > 0) {
            this.instanceType = "UNI";
        } else if (onuNo > 0) {
            this.instanceType = "ONU";
        } else if (portNo > 0) {
            this.instanceType = "PORT";
        } else if (slotNo <= 18) {
            this.instanceType = "SLOT";
        } else {
            this.instanceType = "OTHERS";
        }
        // 设置MIB中使用的Index类型
        this.topAlarmInstanceMaskIndex = EponIndex.getAlertIndexByInstanceIndex(topAlarmInstanceMaskIndex5Byte);
    }

    public String getInstanceName() {
        return instanceName;
    }

    public void setInstanceName(String instanceName) {
        this.instanceName = instanceName;
    }

    public String getInstanceType() {
        return instanceType;
    }

    public void setInstanceType(String instanceType) {
        this.instanceType = instanceType;
    }

    public Integer getTopAlarmInstanceMaskEnable() {
        return topAlarmInstanceMaskEnable;
    }

    public void setTopAlarmInstanceMaskEnable(Integer topAlarmInstanceMaskEnable) {
        this.topAlarmInstanceMaskEnable = topAlarmInstanceMaskEnable;
    }

    public Integer getTopAlarmInstanceMaskRowStatus() {
        return topAlarmInstanceMaskRowStatus;
    }

    public void setTopAlarmInstanceMaskRowStatus(Integer topAlarmInstanceMaskRowStatus) {
        this.topAlarmInstanceMaskRowStatus = topAlarmInstanceMaskRowStatus;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("OltTopAlarmInstanceMask{");
        sb.append("entityId=").append(entityId);
        sb.append(", topAlarmInstanceMaskIndex=").append(topAlarmInstanceMaskIndex);
        sb.append(", instanceName=").append(instanceName);
        sb.append(", instanceType").append(instanceType);
        sb.append(", topAlarmInstanceMaskEnable=").append(topAlarmInstanceMaskEnable);
        sb.append(", topAlarmInstanceMaskRowStatus=").append(topAlarmInstanceMaskRowStatus);
        sb.append('}');
        return sb.toString();
    }
}
