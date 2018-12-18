/***********************************************************************
 * $Id: CmtsInfoNotInRange.java,v1.0 2017年8月4日 下午3:43:18 $
 * 
 * @author: ls
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.cmtsInfo.domain;

import java.io.Serializable;

import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author ls
 * @created @2017年8月4日-下午3:43:18
 *
 */
public class CmtsInfoNotInRange implements Serializable, AliasesSuperType {
    private static final long serialVersionUID = -9031749621722812814L;
    private Long cmcId;
    private String cmOutPowerNotInRange;
    private String cmRePowerNotInRange;
    private String upSnrNotInRange;
    private String downSnrAvgNotInRange;
    public Long getCmcId() {
        return cmcId;
    }
    public void setCmcId(Long cmcId) {
        this.cmcId = cmcId;
    }
    public String getCmOutPowerNotInRange() {
        return cmOutPowerNotInRange;
    }
    public void setCmOutPowerNotInRange(String cmOutPowerNotInRange) {
        this.cmOutPowerNotInRange = cmOutPowerNotInRange;
    }
    public String getCmRePowerNotInRange() {
        return cmRePowerNotInRange;
    }
    public void setCmRePowerNotInRange(String cmRePowerNotInRange) {
        this.cmRePowerNotInRange = cmRePowerNotInRange;
    }
    public String getUpSnrNotInRange() {
        return upSnrNotInRange;
    }
    public void setUpSnrNotInRange(String upSnrNotInRange) {
        this.upSnrNotInRange = upSnrNotInRange;
    }
    public String getDownSnrAvgNotInRange() {
        return downSnrAvgNotInRange;
    }
    public void setDownSnrAvgNotInRange(String downSnrAvgNotInRange) {
        this.downSnrAvgNotInRange = downSnrAvgNotInRange;
    }
    
}
