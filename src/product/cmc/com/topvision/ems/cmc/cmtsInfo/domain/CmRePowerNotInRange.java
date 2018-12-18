package com.topvision.ems.cmc.cmtsInfo.domain;

import java.io.Serializable;

import com.topvision.framework.dao.mybatis.AliasesSuperType;

public class CmRePowerNotInRange implements Serializable, AliasesSuperType{

    private static final long serialVersionUID = -8334246914685615335L;

    private Long cmcId;
    private Integer cmRePowerNotInRange;
    public Long getCmcId() {
        return cmcId;
    }
    public void setCmcId(Long cmcId) {
        this.cmcId = cmcId;
    }
    public Integer getCmRePowerNotInRange() {
        return cmRePowerNotInRange;
    }
    public void setCmRePowerNotInRange(Integer cmRePowerNotInRange) {
        this.cmRePowerNotInRange = cmRePowerNotInRange;
    }

}
