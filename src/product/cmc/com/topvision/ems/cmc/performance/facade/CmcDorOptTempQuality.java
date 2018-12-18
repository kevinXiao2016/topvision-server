package com.topvision.ems.cmc.performance.facade;

import java.io.Serializable;
import java.sql.Timestamp;

import org.apache.ibatis.type.Alias;

import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.dao.mybatis.AliasesSuperType;

@Alias("cmcDorOptTempQuality")
public class CmcDorOptTempQuality implements Serializable, AliasesSuperType {

    private static final long serialVersionUID = 960619158030390472L;

    private Long cmcId;
    private Long cmcIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.21.24.1.1", index = true)
    private Long dorOptTempIndex;// 是表的ifIndex
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.21.24.1.4")
    private Long dorOptNodeTemp;
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

    public Long getDorOptNodeTemp() {
        return dorOptNodeTemp;
    }

    public void setDorOptNodeTemp(Long dorOptNodeTemp) {
        this.dorOptNodeTemp = dorOptNodeTemp;
    }

    public Timestamp getCollectTime() {
        return collectTime;
    }

    public void setCollectTime(Timestamp collectTime) {
        this.collectTime = collectTime;
    }

    public Long getDorOptTempIndex() {
        return dorOptTempIndex;
    }

    public void setDorOptTempIndex(Long dorOptTempIndex) {
        this.dorOptTempIndex = dorOptTempIndex;
    }

}
