package com.topvision.ems.cmc.performance.facade;

import java.io.Serializable;
import java.sql.Timestamp;

import org.apache.ibatis.type.Alias;

import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.dao.mybatis.AliasesSuperType;

@Alias("cmcDorLinePowerQuality")
public class CmcDorLinePowerQuality implements Serializable, AliasesSuperType {

    private static final long serialVersionUID = 8095877648730438379L;

    private Long cmcId;
    private Long cmcIndex;

    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.21.13.1.1", index = true)
    private Long dorLinePowerIndex;// 是表的ifIndex
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.21.13.1.2")
    private Long dorLinePower;
    private Timestamp collectTime;

    public Long getCmcId() {
        return cmcId;
    }

    public void setCmcId(Long cmcId) {
        this.cmcId = cmcId;
    }

    public Long getCmcIndex() {
        return cmcIndex;
    }

    public void setCmcIndex(Long cmcIndex) {
        this.cmcIndex = cmcIndex;
    }

    public Long getDorLinePower() {
        return dorLinePower;
    }

    public void setDorLinePower(Long dorLinePower) {
        this.dorLinePower = dorLinePower;
    }

    public Timestamp getCollectTime() {
        return collectTime;
    }

    public void setCollectTime(Timestamp collectTime) {
        this.collectTime = collectTime;
    }

    public Long getDorLinePowerIndex() {
        return dorLinePowerIndex;
    }

    public void setDorLinePowerIndex(Long dorLinePowerIndex) {
        this.dorLinePowerIndex = dorLinePowerIndex;
    }

}
