package com.topvision.ems.cmc.cmtsInfo.domain;

import java.io.Serializable;

import com.topvision.framework.dao.mybatis.AliasesSuperType;

public class UpSnrNotInRange implements Serializable, AliasesSuperType {
    private static final long serialVersionUID = -7879341779703196993L;
    private Long cmcId;
    private Integer upSnrNotInRange;
    public Long getCmcId() {
        return cmcId;
    }
    public void setCmcId(Long cmcId) {
        this.cmcId = cmcId;
    }
    public Integer getUpSnrNotInRange() {
        return upSnrNotInRange;
    }
    public void setUpSnrNotInRange(Integer upSnrNotInRange) {
        this.upSnrNotInRange = upSnrNotInRange;
    }
}
