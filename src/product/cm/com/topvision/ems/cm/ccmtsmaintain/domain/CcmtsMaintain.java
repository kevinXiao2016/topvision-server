/***********************************************************************
 * $Id: CcmtsMaintain.java,v1.0 2015-5-27 下午5:26:45 $
 * 
 * @author: Fanzidong
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cm.ccmtsmaintain.domain;

import java.sql.Timestamp;

import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author fanzidong
 * @created @2015-5-27-下午5:26:45
 * 
 */
public class CcmtsMaintain implements AliasesSuperType {
    private static final long serialVersionUID = 8074520914352776852L;
    
    private Long cmcId;
    private Integer allChlNum;
    private Integer allStdNum;
    private Integer avgSnrStdNum;
    private Integer lowSnrStdNum;
    private Integer bigPowerStdNum;
    private Integer chlWidthStdNum;
    private Timestamp collectTime;

    public CcmtsMaintain() {
        super();
    }

    public CcmtsMaintain(Long cmcId, Timestamp collectTime) {
        super();
        this.cmcId = cmcId;
        this.allChlNum = 0;
        this.allStdNum = 0;
        this.avgSnrStdNum = 0;
        this.lowSnrStdNum = 0;
        this.bigPowerStdNum = 0;
        this.chlWidthStdNum = 0;
        this.collectTime = collectTime;
    }

    public Long getCmcId() {
        return cmcId;
    }

    public void setCmcId(Long cmcId) {
        this.cmcId = cmcId;
    }

    public Integer getAllChlNum() {
        return allChlNum;
    }

    public void setAllChlNum(Integer allChlNum) {
        this.allChlNum = allChlNum;
    }

    public Integer getAllStdNum() {
        return allStdNum;
    }

    public void setAllStdNum(Integer allStdNum) {
        this.allStdNum = allStdNum;
    }

    public Integer getAvgSnrStdNum() {
        return avgSnrStdNum;
    }

    public void setAvgSnrStdNum(Integer avgSnrStdNum) {
        this.avgSnrStdNum = avgSnrStdNum;
    }

    public Integer getLowSnrStdNum() {
        return lowSnrStdNum;
    }

    public void setLowSnrStdNum(Integer lowSnrStdNum) {
        this.lowSnrStdNum = lowSnrStdNum;
    }

    public Integer getBigPowerStdNum() {
        return bigPowerStdNum;
    }

    public void setBigPowerStdNum(Integer bigPowerStdNum) {
        this.bigPowerStdNum = bigPowerStdNum;
    }

    public Integer getChlWidthStdNum() {
        return chlWidthStdNum;
    }

    public void setChlWidthStdNum(Integer chlWidthStdNum) {
        this.chlWidthStdNum = chlWidthStdNum;
    }

    public Timestamp getCollectTime() {
        return collectTime;
    }

    public void setCollectTime(Timestamp collectTime) {
        this.collectTime = collectTime;
    }

    public void increaseAllChlNum() {
        if(allChlNum==null){
            allChlNum = 0;
        }
        allChlNum++;
    }
    
    public void increaseAllStdNum() {
        if(allStdNum==null){
            allStdNum = 0;
        }
        allStdNum++;
    }
    
    public void increaseAvgSnrStdNum() {
        if(avgSnrStdNum==null){
            avgSnrStdNum = 0;
        }
        avgSnrStdNum++;
    }
    
    public void increaseLowSnrStdNum() {
        if(lowSnrStdNum==null){
            lowSnrStdNum = 0;
        }
        lowSnrStdNum++;
    }
    
    public void increaseBigPowerStdNum() {
        if(bigPowerStdNum==null){
            bigPowerStdNum = 0;
        }
        bigPowerStdNum++;
    }
    
    public void increaseChlWidthStdNum() {
        if(chlWidthStdNum==null){
            chlWidthStdNum = 0;
        }
        chlWidthStdNum++;
    }

}
