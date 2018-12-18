package com.topvision.ems.cmc.cmtsInfo.domain;

import java.io.Serializable;

import com.topvision.framework.dao.mybatis.AliasesSuperType;

public class CmOutPowerNotInRange implements Serializable, AliasesSuperType {

    private static final long serialVersionUID = -4433084300502720837L;
    private Long cmcId;
    private Integer cmOutPowerNotInRange;
    public Long getCmcId() {
        return cmcId;
    }
    public void setCmcId(Long cmcId) {
        this.cmcId = cmcId;
    }
    public Integer getCmOutPowerNotInRange() {
        return cmOutPowerNotInRange;
    }
    public void setCmOutPowerNotInRange(Integer cmOutPowerNotInRange) {
        this.cmOutPowerNotInRange = cmOutPowerNotInRange;
    }

}
