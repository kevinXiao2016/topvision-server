package com.topvision.ems.epon.onu.domain;

import java.io.Serializable;
import java.sql.Timestamp;

import com.topvision.framework.common.DateUtils;
import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * 上下线历史记录domain
 * 
 * @author w1992wishes
 * @created @2017年6月14日-下午3:36:32
 *
 */
public class OnuOnOffRecord implements Serializable, AliasesSuperType {

    private static final long serialVersionUID = 6953506126895580791L;

    private Long onuId;
    private Long onuIndex;
    private Long entityId;
    private Timestamp onTime;
    private Timestamp offTime;
    private String onTimeString;
    private String offTimeString;
    private Short offReason;// 下线原因，有0,1,2,3,4,5等值分别对应不同的原因
    private Timestamp collectTime;
    private String collectTimeString;
    private String onOffRecordByteList;//onu上下线单条记录对应的字节（从设备获取到的是多条记录混合一起的字节）

    public Long getOnuId() {
        return onuId;
    }

    public Long getOnuIndex() {
        return onuIndex;
    }

    public Long getEntityId() {
        return entityId;
    }

    public Timestamp getOnTime() {
        return onTime;
    }

    public Timestamp getOffTime() {
        return offTime;
    }

    public Short getOffReason() {
        return offReason;
    }

    public Timestamp getCollectTime() {
        return collectTime;
    }

    public void setOnuId(Long onuId) {
        this.onuId = onuId;
    }

    public void setOnuIndex(Long onuIndex) {
        this.onuIndex = onuIndex;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public void setOnTime(Timestamp onTime) {
        this.onTime = onTime;
    }

    public void setOffTime(Timestamp offTime) {
        this.offTime = offTime;
    }

    public void setOffReason(Short offReason) {
        this.offReason = offReason;
    }

    public void setCollectTime(Timestamp collectTime) {
        collectTimeString = DateUtils.format(collectTime);
        this.collectTime = collectTime;
    }

    public String getOnTimeString() {
        this.onTimeString = this.onTime == null ? "--" : DateUtils.format(onTime.getTime());
        return onTimeString;
    }

    public String getOffTimeString() {
        this.offTimeString = this.offTime == null ? "--" : DateUtils.format(offTime.getTime());
        return offTimeString;
    }

    public String getCollectTimeString() {
        return collectTimeString;
    }

    public void setOnTimeString(String onTimeString) {
        this.onTimeString = onTimeString;
    }

    public void setOffTimeString(String offTimeString) {
        this.offTimeString = offTimeString;
    }

    public void setCollectTimeString(String collectTimeString) {
        this.collectTimeString = collectTimeString;
    }

    public String getOnOffRecordByteList() {
        return onOffRecordByteList;
    }

    public void setOnOffRecordByteList(String onOffRecordByteList) {
        this.onOffRecordByteList = onOffRecordByteList;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("OnuOnOffRecord [entityId=");
        builder.append(entityId);
        builder.append(", onuId=");
        builder.append(onuId);
        builder.append(", onuIndex=");
        builder.append(onuIndex);
        builder.append(", onTime=");
        builder.append(onTime);
        builder.append(", offTime=");
        builder.append(offTime);
        builder.append(", offReason=");
        builder.append(offReason);
        builder.append(", collectTime=");
        builder.append(collectTime);
        builder.append("]");
        return builder.toString();
    }
}
