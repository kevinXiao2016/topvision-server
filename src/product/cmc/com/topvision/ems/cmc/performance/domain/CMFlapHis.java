package com.topvision.ems.cmc.performance.domain;

import java.io.Serializable;
import java.sql.Timestamp;

import org.apache.ibatis.type.Alias;

import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * 用来封装flap历史数据，主要针对cmFlapHis这张表。
 * @author smsx
 *
 */
@Alias("cmFlapHis")
public class CMFlapHis implements Serializable, AliasesSuperType{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8809318789106815506L;
	private Long cmId;
	private Long cmcId;
	private String cmMac;
	private Timestamp collectTime;
	private String lastFlapTime;
	private Integer insFailNum = 0;
	private Integer hitNum = 0;
	private Integer missNum = 0;
	private Integer crcErrorNum = 0;
	private Integer powerAdjLowerNum = 0;
	private Integer powerAdjHigherNum = 0;
	
	private Integer increaseInsNum = 0;
	private Float increaseHitPercent = 0.0f;
	private Integer increasePowerAdjNum = 0;
	
	
	public String getCmMac() {
		return cmMac;
	}
	public void setCmMac(String cmMac) {
		this.cmMac = cmMac;
	}
	public Timestamp getCollectTime() {
		return collectTime;
	}
	public void setCollectTime(Timestamp collectTime) {
		this.collectTime = collectTime;
	}
	public String getLastFlapTime() {
		return lastFlapTime;
	}
	public void setLastFlapTime(String lastFlapTime) {
		this.lastFlapTime = lastFlapTime;
	}
	public Integer getInsFailNum() {
		if(this.insFailNum==null){
			return 0;
		}
		return insFailNum;
	}
	public void setInsFailNum(Integer insFailNum) {
		this.insFailNum = insFailNum;
	}
	public Integer getHitNum() {
		if(this.hitNum==null){
			return 0;
		}
		return hitNum;
	}
	public void setHitNum(Integer hitNum) {
		this.hitNum = hitNum;
	}
	public Integer getMissNum() {
		if(this.missNum==null){
			return 0;
		}
		return missNum;
	}
	public void setMissNum(Integer missNum) {
		this.missNum = missNum;
	}
	public Integer getCrcErrorNum() {
		if(this.crcErrorNum==null){
			return 0;
		}
		return crcErrorNum;
	}
	public void setCrcErrorNum(Integer crcErrorNum) {
		this.crcErrorNum = crcErrorNum;
	}
	public Integer getPowerAdjLowerNum() {
		if(this.powerAdjLowerNum==null){
			return 0;
		}
		return powerAdjLowerNum;
	}
	public void setPowerAdjLowerNum(Integer powerAdjLowerNum) {
		
		this.powerAdjLowerNum = powerAdjLowerNum;
	}
	public Integer getPowerAdjHigherNum() {
		if(this.powerAdjHigherNum==null){
			return 0;
		}
		return powerAdjHigherNum;
	}
	public void setPowerAdjHigherNum(Integer powerAdjHigherNum) {
		this.powerAdjHigherNum = powerAdjHigherNum;
	}
	/**
	 * @return the increaseInsNum
	 */
	public Integer getIncreaseInsNum() {
		return increaseInsNum;
	}
	/**
	 * @param increaseInsNum the increaseInsNum to set
	 */
	public void setIncreaseInsNum(Integer increaseInsNum) {
		this.increaseInsNum = increaseInsNum;
	}
	/**
	 * @return the increaseHitPercent
	 */
	public Float getIncreaseHitPercent() {
		return increaseHitPercent;
	}
	/**
	 * @param increaseHitPercent the increaseHitPercent to set
	 */
	public void setIncreaseHitPercent(Float increaseHitPercent) {
		this.increaseHitPercent = increaseHitPercent;
	}
	/**
	 * @return the increasePowerAdjNum
	 */
	public Integer getIncreasePowerAdjNum() {
        //modify by jay 电平调整次数为powerAdjHigherNum + powerAdjLowerNum
		return powerAdjHigherNum + powerAdjLowerNum;
	}
	/**
	 * @param increasePowerAdjNum the increasePowerAdjNum to set
	 */
	public void setIncreasePowerAdjNum(Integer increasePowerAdjNum) {
		this.increasePowerAdjNum = increasePowerAdjNum;
	}
    /**
     * @return the cmcId
     */
    public Long getCmcId() {
        return cmcId;
    }
    /**
     * @param cmcId the cmcId to set
     */
    public void setCmcId(Long cmcId) {
        this.cmcId = cmcId;
    }
    /**
     * @return the cmId
     */
    public Long getCmId() {
        return cmId;
    }
    /**
     * @param cmId the cmId to set
     */
    public void setCmId(Long cmId) {
        this.cmId = cmId;
    }
	
}
