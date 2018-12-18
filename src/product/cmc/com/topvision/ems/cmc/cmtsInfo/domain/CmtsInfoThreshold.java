package com.topvision.ems.cmc.cmtsInfo.domain;

import com.topvision.framework.dao.mybatis.AliasesSuperType;

public class CmtsInfoThreshold implements AliasesSuperType {
    private static final long serialVersionUID = -3038938808865475051L;
    
    private Integer upSnrMin;
    private Integer upSnrMax;
    private Integer downSnrMin;
    private Integer downSnrMax;
    private Integer upPowerMin;
    private Integer upPowerMax;
    private Integer downPowerMin;
    private Integer downPowerMax;
    public Integer getUpSnrMin() {
        return upSnrMin;
    }
    public void setUpSnrMin(Integer upSnrMin) {
        this.upSnrMin = upSnrMin;
    }
    public Integer getUpSnrMax() {
        return upSnrMax;
    }
    public void setUpSnrMax(Integer upSnrMax) {
        this.upSnrMax = upSnrMax;
    }
    public Integer getDownSnrMin() {
        return downSnrMin;
    }
    public void setDownSnrMin(Integer downSnrMin) {
        this.downSnrMin = downSnrMin;
    }
    public Integer getDownSnrMax() {
        return downSnrMax;
    }
    public void setDownSnrMax(Integer downSnrMax) {
        this.downSnrMax = downSnrMax;
    }
    public Integer getUpPowerMin() {
        return upPowerMin;
    }
    public void setUpPowerMin(Integer upPowerMin) {
        this.upPowerMin = upPowerMin;
    }
    public Integer getUpPowerMax() {
        return upPowerMax;
    }
    public void setUpPowerMax(Integer upPowerMax) {
        this.upPowerMax = upPowerMax;
    }
    public Integer getDownPowerMin() {
        return downPowerMin;
    }
    public void setDownPowerMin(Integer downPowerMin) {
        this.downPowerMin = downPowerMin;
    }
    public Integer getDownPowerMax() {
        return downPowerMax;
    }
    public void setDownPowerMax(Integer downPowerMax) {
        this.downPowerMax = downPowerMax;
    } 
    

}
