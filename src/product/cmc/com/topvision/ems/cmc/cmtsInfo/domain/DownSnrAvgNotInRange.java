package com.topvision.ems.cmc.cmtsInfo.domain;

import java.io.Serializable;

import com.topvision.framework.dao.mybatis.AliasesSuperType;

public class DownSnrAvgNotInRange implements Serializable, AliasesSuperType {
    private static final long serialVersionUID = -5427632487366439149L;
    private Long cmcId;
    private Integer downSnrAvgNotInRange;
    public Long getCmcId() {
        return cmcId;
    }
    public void setCmcId(Long cmcId) {
        this.cmcId = cmcId;
    }
    public Integer getDownSnrAvgNotInRange() {
        return downSnrAvgNotInRange;
    }
    public void setDownSnrAvgNotInRange(Integer downSnrAvgNotInRange) {
        this.downSnrAvgNotInRange = downSnrAvgNotInRange;
    }

}
