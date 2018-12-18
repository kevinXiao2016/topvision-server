package com.topvision.ems.epon.performance.domain;

import java.io.Serializable;
import java.sql.Timestamp;

import com.topvision.framework.dao.mybatis.AliasesSuperType;

public class PerfOnuQualityHistory implements Serializable, AliasesSuperType {
    private static final long serialVersionUID = 1587335616271799035L;
    private Long onuId;
    private Long onuIndex;
    private Long entityId;
    private Float minOnuPonRevPower;// 最低光接收功率
    private Timestamp minPowerTime;// 最低光接收功率对应的时间（有多个，取24小时内的最新值）
    private String minPowerTimeStr;// str值
    private Float minCatvRevPower;// CATV最低光接收功率
    private Timestamp minCatvTime;// 最低光接收功率对应的时间（有多个，取24小时内的最新值）
    private String minCatvTimeStr;// str值
    private Timestamp collectTime;// 汇总时间

    public Long getOnuId() {
        return onuId;
    }

    public Long getOnuIndex() {
        return onuIndex;
    }

    public Long getEntityId() {
        return entityId;
    }

    public Float getMinOnuPonRevPower() {
        return minOnuPonRevPower;
    }

    public Timestamp getMinPowerTime() {
        return minPowerTime;
    }

    public String getMinPowerTimeStr() {
        return minPowerTimeStr;
    }

    public Float getMinCatvRevPower() {
        return minCatvRevPower;
    }

    public Timestamp getMinCatvTime() {
        return minCatvTime;
    }

    public String getMinCatvTimeStr() {
        return minCatvTimeStr;
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

    public void setMinOnuPonRevPower(Float minOnuPonRevPower) {
        this.minOnuPonRevPower = minOnuPonRevPower;
    }

    public void setMinPowerTime(Timestamp minPowerTime) {
        this.minPowerTime = minPowerTime;
    }

    public void setMinPowerTimeStr(String minPowerTimeStr) {
        this.minPowerTimeStr = minPowerTimeStr;
    }

    public void setMinCatvRevPower(Float minCatvRevPower) {
        // EMS-14822 小于-200，说明未接catv光机，不入数据库（前台的值是除于10后的值，转换后是小于-20说明未接catv光机）
        if (minCatvRevPower != null && minCatvRevPower > -200) {
            this.minCatvRevPower = minCatvRevPower;
        }
    }

    public void setMinCatvTime(Timestamp minCatvTime) {
        if (minCatvRevPower != null && minCatvRevPower > -200) {
            this.minCatvTime = minCatvTime;
        }
    }

    public void setMinCatvTimeStr(String minCatvTimeStr) {
        if (minCatvRevPower != null && minCatvRevPower > -200) {
            this.minCatvTimeStr = minCatvTimeStr;
        }
    }

    public void setCollectTime(Timestamp collectTime) {
        this.collectTime = collectTime;
    }
}
