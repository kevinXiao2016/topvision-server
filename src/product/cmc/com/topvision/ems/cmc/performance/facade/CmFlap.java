/***********************************************************************
 * $Id: CmFlap.java,v1.0 2013-4-25 下午1:52:57 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.performance.facade;

import java.io.Serializable;
import java.sql.Timestamp;

import org.apache.ibatis.type.Alias;

import com.topvision.ems.cmc.util.CmcUtil;
import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.common.DateUtils;
import com.topvision.framework.common.MacUtils;
import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author haojie
 * @created @2013-4-25-下午1:52:57
 * 
 */
@Alias("cmFlap")
public class CmFlap implements Serializable, AliasesSuperType {
    private static final long serialVersionUID = -7045042613585280354L;
    private Long cmcId;
    private Long cmId;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.11.1.1.1", index = true)
    private String topCmFlapMacAddr;
    private String topCmFlagMacAddrString;
    // TODO 已经将flap
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.11.1.1.2")
    private String topCmFlapLastFlapTime;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.11.1.1.3")
    private Integer topCmFlapInsertionFailNum;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.11.1.1.4")
    private Integer topCmFlapHitNum;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.11.1.1.5")
    private Integer topCmFlapMissNum;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.11.1.1.6")
    private Integer topCmFlapCrcErrorNum;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.11.1.1.7")
    private Integer topCmFlapPowerAdjLowerNum;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.11.1.1.8")
    private Integer topCmFlapPowerAdjHigherNum;
    // @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.11.1.1.9")
    private Integer topCmFlapTotalNum;

    private Integer increaseInsNum = 0;
    private Float increaseHitPercent = 0.0f;
    private Integer increasePowerAdjNum = 0;

    private String cmcName;
    private String topCcmtsSysName;
    private String cmcMac;
    private String cmcType;
    private Timestamp dt;
    private String recentUpdateTime; // 距离上次更新时间
    private Long parentId;
    private String displayName;

    public Long getCmcId() {
        return cmcId;
    }

    public void setCmcId(Long cmcId) {
        this.cmcId = cmcId;
    }

    public Long getCmId() {
        return cmId;
    }

    public void setCmId(Long cmId) {
        this.cmId = cmId;
    }

    public String getTopCmFlapMacAddr() {
        return topCmFlapMacAddr;
    }

    public void setTopCmFlapMacAddr(String topCmFlapMacAddr) {
        this.topCmFlapMacAddr = topCmFlapMacAddr;
        if (topCmFlapMacAddr != null) {
            String[] hex = topCmFlapMacAddr.split(".");
            if (hex.length == 6) {
                CmcUtil.getMacFromMibMac(topCmFlapMacAddr);
            } else {
                this.topCmFlagMacAddrString = topCmFlapMacAddr;
            }
        }
    }

    public Integer getTopCmFlapInsertionFailNum() {
        return topCmFlapInsertionFailNum;
    }

    public void setTopCmFlapInsertionFailNum(Integer topCmFlapInsertionFailNum) {
        this.topCmFlapInsertionFailNum = topCmFlapInsertionFailNum;
    }

    public String getTopCmFlagMacAddrString() {
        if (topCmFlagMacAddrString == null && topCmFlapMacAddr != null) {
            topCmFlagMacAddrString = topCmFlapMacAddr.toString();
        }
        return topCmFlagMacAddrString;
    }

    public void setTopCmFlagMacAddrString(String topCmFlagMacAddrString) {
        // @EMS-10953
        this.topCmFlagMacAddrString = MacUtils.getMacStringFromNoISOControl(topCmFlagMacAddrString);
        // add by fanzidong,需要将MAC地址格式化为以：分隔的格式传递给turnHexMacToMibMacIndex
        topCmFlagMacAddrString = MacUtils.convertToMaohaoFormat(topCmFlagMacAddrString);
        this.topCmFlapMacAddr = CmcUtil.turnHexMacToMibMacIndex(topCmFlagMacAddrString);
    }

    /**
     * @return the topCmFlapLastFlapTime
     */
    public String getTopCmFlapLastFlapTime() {
        return topCmFlapLastFlapTime;
    }

    /**
     * @param topCmFlapLastFlapTime
     *            the topCmFlapLastFlapTime to set
     */
    public void setTopCmFlapLastFlapTime(String topCmFlapLastFlapTime) {
        this.topCmFlapLastFlapTime = topCmFlapLastFlapTime;
    }

    /**
     * @return the topCmFlapHitNum
     */
    public Integer getTopCmFlapHitNum() {
        return topCmFlapHitNum;
    }

    /**
     * @param topCmFlapHitNum
     *            the topCmFlapHitNum to set
     */
    public void setTopCmFlapHitNum(Integer topCmFlapHitNum) {
        this.topCmFlapHitNum = topCmFlapHitNum;
    }

    /**
     * @return the topCmFlapMissNum
     */
    public Integer getTopCmFlapMissNum() {
        return topCmFlapMissNum;
    }

    /**
     * @param topCmFlapMissNum
     *            the topCmFlapMissNum to set
     */
    public void setTopCmFlapMissNum(Integer topCmFlapMissNum) {
        this.topCmFlapMissNum = topCmFlapMissNum;
    }

    /**
     * @return the topCmFlapCrcErrorNum
     */
    public Integer getTopCmFlapCrcErrorNum() {
        return topCmFlapCrcErrorNum;
    }

    /**
     * @param topCmFlapCrcErrorNum
     *            the topCmFlapCrcErrorNum to set
     */
    public void setTopCmFlapCrcErrorNum(Integer topCmFlapCrcErrorNum) {
        this.topCmFlapCrcErrorNum = topCmFlapCrcErrorNum;
    }

    /**
     * @return the topCmFlapPowerAdjLowerNum
     */
    public Integer getTopCmFlapPowerAdjLowerNum() {
        return topCmFlapPowerAdjLowerNum;
    }

    /**
     * @param topCmFlapPowerAdjLowerNum
     *            the topCmFlapPowerAdjLowerNum to set
     */
    public void setTopCmFlapPowerAdjLowerNum(Integer topCmFlapPowerAdjLowerNum) {
        this.topCmFlapPowerAdjLowerNum = topCmFlapPowerAdjLowerNum;
    }

    /**
     * @return the topCmFlapPowerAdjHigherNum
     */
    public Integer getTopCmFlapPowerAdjHigherNum() {
        return topCmFlapPowerAdjHigherNum;
    }

    /**
     * @param topCmFlapPowerAdjHigherNum
     *            the topCmFlapPowerAdjHigherNum to set
     */
    public void setTopCmFlapPowerAdjHigherNum(Integer topCmFlapPowerAdjHigherNum) {
        this.topCmFlapPowerAdjHigherNum = topCmFlapPowerAdjHigherNum;
    }

    /**
     * @return the topCmFlapTotalNum
     */
    public Integer getTopCmFlapTotalNum() {
        return topCmFlapTotalNum;
    }

    /**
     * @param topCmFlapTotalNum
     *            the topCmFlapTotalNum to set
     */
    public void setTopCmFlapTotalNum(Integer topCmFlapTotalNum) {
        this.topCmFlapTotalNum = topCmFlapTotalNum;
    }

    /**
     * @return the increaseInsNum
     */
    public Integer getIncreaseInsNum() {
        return increaseInsNum;
    }

    /**
     * @param increaseInsNum
     *            the increaseInsNum to set
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
     * @param increaseHitPercent
     *            the increaseHitPercent to set
     */
    public void setIncreaseHitPercent(Float increaseHitPercent) {
        this.increaseHitPercent = increaseHitPercent;
    }

    /**
     * @return the increasePowerAdjNum
     */
    public Integer getIncreasePowerAdjNum() {
        return increasePowerAdjNum;
    }

    /**
     * @param increasePowerAdjNum
     *            the increasePowerAdjNum to set
     */
    public void setIncreasePowerAdjNum(Integer increasePowerAdjNum) {
        this.increasePowerAdjNum = increasePowerAdjNum;
    }

    /**
     * @return the cmcName
     */
    public String getCmcName() {
        return cmcName;
    }

    /**
     * @param cmcName
     *            the cmcName to set
     */
    public void setCmcName(String cmcName) {
        this.cmcName = cmcName;
    }

    /**
     * @return the cmcMac
     */
    public String getCmcMac() {
        return cmcMac;
    }

    /**
     * @param cmcMac
     *            the cmcMac to set
     */
    public void setCmcMac(String cmcMac) {
        this.cmcMac = cmcMac;
    }

    /**
     * @return the cmcType
     */
    public String getCmcType() {
        return cmcType;
    }

    /**
     * @param cmcType
     *            the cmcType to set
     */
    public void setCmcType(String cmcType) {
        this.cmcType = cmcType;
    }

    /**
     * @return the dt
     */
    public Timestamp getDt() {
        return dt;
    }

    /**
     * @param dt
     *            the dt to set
     */
    public void setDt(Timestamp dt) {
        this.dt = dt;
    }

    public String getDtStr() {
        return DateUtils.format(this.dt);
    }

    public Long getParentId() {
        return parentId;
    }

    public String getRecentUpdateTime() {
        return recentUpdateTime;
    }

    public void setRecentUpdateTime(String recentUpdateTime) {
        this.recentUpdateTime = recentUpdateTime;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getTopCcmtsSysName() {
        return topCcmtsSysName;
    }

    public void setTopCcmtsSysName(String topCcmtsSysName) {
        this.topCcmtsSysName = topCcmtsSysName;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("CmFlap [cmcId=");
        builder.append(cmcId);
        builder.append(", cmId=");
        builder.append(cmId);
        builder.append(", topCmFlapMacAddr=");
        builder.append(topCmFlapMacAddr);
        builder.append(", topCmFlagMacAddrString=");
        builder.append(topCmFlagMacAddrString);
        builder.append(", topCmFlapLastFlapTime=");
        builder.append(topCmFlapLastFlapTime);
        builder.append(", topCmFlapInsertionFailNum=");
        builder.append(topCmFlapInsertionFailNum);
        builder.append(", topCmFlapHitNum=");
        builder.append(topCmFlapHitNum);
        builder.append(", topCmFlapMissNum=");
        builder.append(topCmFlapMissNum);
        builder.append(", topCmFlapCrcErrorNum=");
        builder.append(topCmFlapCrcErrorNum);
        builder.append(", topCmFlapPowerAdjLowerNum=");
        builder.append(topCmFlapPowerAdjLowerNum);
        builder.append(", topCmFlapPowerAdjHigherNum=");
        builder.append(topCmFlapPowerAdjHigherNum);
        builder.append(", topCmFlapTotalNum=");
        builder.append(topCmFlapTotalNum);
        builder.append(", increaseInsNum=");
        builder.append(increaseInsNum);
        builder.append(", increaseHitPercent=");
        builder.append(increaseHitPercent);
        builder.append(", increasePowerAdjNum=");
        builder.append(increasePowerAdjNum);
        builder.append(", cmcName=");
        builder.append(cmcName);
        builder.append(", topCcmtsSysName=");
        builder.append(topCcmtsSysName);
        builder.append(", cmcMac=");
        builder.append(cmcMac);
        builder.append(", cmcType=");
        builder.append(cmcType);
        builder.append(", dt=");
        builder.append(dt);
        builder.append(", recentUpdateTime=");
        builder.append(recentUpdateTime);
        builder.append(", parentId=");
        builder.append(parentId);
        builder.append(", displayName=");
        builder.append(displayName);
        builder.append("]");
        return builder.toString();
    }
}
