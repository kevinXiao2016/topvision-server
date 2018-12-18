/***********************************************************************
 * $Id: Cmc.java,v1.0 2012-2-14 上午11:49:41 $
 * 
 * @author: zhanglongyang
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.ccmts.domain;

import java.io.Serializable;
import java.sql.Timestamp;

import org.apache.ibatis.type.Alias;

import com.topvision.framework.constants.Symbol;
import com.topvision.framework.common.MacUtils;
import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * 数据结构 CMC类型(8800A/8800B)与是否挂接OLT关系说明：
 * ①CMC类型为8800A时，必须挂接在OLT下，entityId和cmcEntityId相同，均为OLT的entityId，onuId为null。
 * ②CMC类型为8800B，且挂接在ONU下时，entityId为OLT的entityId，而cmcEntityId为该CMC的ID，onuId为所挂接的onuId。
 * ③CMC类型为8800B，且独立工作时，entityId和cmcEntityId均为该CMC的ID，onuId为null。
 * 
 * @author zhanglongyang
 * @created @2012-2-14-上午11:49:41
 * 
 */
@Alias("cmc")
public class Cmc implements Serializable, AliasesSuperType {
    private static final long serialVersionUID = 8422293001848946072L;
    private Long cmcId;
    private Long entityId;
    private Long cmcEntityId;
    private Long cmcIndex;
    private Long onuId;
    private Long onuIndex;
    private Integer cmcType;
    private Long uniIdMain;
    private Long uniIdBack;

    private String cmcName;
    private String nmName;
    private String cmcIp;
    private Long cmcIpLong;
    private Integer cmcDeviceStyle;
    private String topCcmtsSysDescr;
    private String topCcmtsSysObjectId;
    private Long topCcmtsSysUpTime;
    private String topCcmtsSysContact;
    private String topCcmtsSysName;
    private String topCcmtsSysLocation;
    private Integer topCcmtsSysService;
    private Long topCcmtsSysORLastChange;
    private Long topCcmtsDocsisBaseCapability;
    private Integer topCcmtsSysRAMRatio;
    private Integer topCcmtsSysCPURatio;
    private Integer topCcmtsSysFlashRatio;
    private String topCcmtsSysMacAddr;
    private Long topCcmtsSysMacAddrLong;
    private Integer topCcmtsCmNumTotal;
    private Integer topCcmtsCmNumOutline;
    private Integer topCcmtsCmNumOnline;
    private Integer topCcmtsCmNumReg;
    private Integer topCcmtsCmNumRanged;
    private Integer topCcmtsCmNumRanging;
    private Integer topCcmtsSysStatus;
    private Timestamp snapTime = new Timestamp(System.currentTimeMillis());

    private String topCcmtsSysCPURatiotoString = "---";
    private String topCcmtsSysRAMRatiotoString = "---";
    private Timestamp dt;
    private Timestamp lastDeregisterTime;
    private String cmcRunTime;

    public Long getCmcId() {
        return cmcId;
    }

    public void setCmcId(Long cmcId) {
        this.cmcId = cmcId;
    }

    public String getTopCcmtsSysCPURatiotoString() {
        if (this.getTopCcmtsSysCPURatio() != null && this.getTopCcmtsSysCPURatio() <= 100) {
            topCcmtsSysCPURatiotoString = this.getTopCcmtsSysCPURatio() + Symbol.PERCENT;
        }
        return topCcmtsSysCPURatiotoString;
    }

    public void setTopCcmtsSysCPURatiotoString(String topCcmtsSysCPURatiotoString) {
        this.topCcmtsSysCPURatiotoString = topCcmtsSysCPURatiotoString;
    }

    public String getTopCcmtsSysRAMRatiotoString() {
        if (this.getTopCcmtsSysRAMRatio() != null && this.getTopCcmtsSysRAMRatio() <= 100) {
            topCcmtsSysRAMRatiotoString = this.getTopCcmtsSysRAMRatio() + Symbol.PERCENT;
        }
        return topCcmtsSysRAMRatiotoString;
    }

    public void setTopCcmtsSysRAMRatiotoString(String topCcmtsSysRAMRatiotoString) {
        this.topCcmtsSysRAMRatiotoString = topCcmtsSysRAMRatiotoString;
    }

    public Timestamp getSnapTime() {
        return snapTime;
    }

    public void setSnapTime(Timestamp snapTime) {
        this.snapTime = snapTime;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public Long getCmcEntityId() {
        return cmcEntityId;
    }

    public void setCmcEntityId(Long cmcEntityId) {
        this.cmcEntityId = cmcEntityId;
    }

    public Long getCmcIndex() {
        return cmcIndex;
    }

    public void setCmcIndex(Long cmcIndex) {
        this.cmcIndex = cmcIndex;
    }

    public Long getOnuId() {
        return onuId;
    }

    public void setOnuId(Long onuId) {
        this.onuId = onuId;
    }

    public Long getOnuIndex() {
        return onuIndex;
    }

    public void setOnuIndex(Long onuIndex) {
        this.onuIndex = onuIndex;
    }

    public Integer getCmcType() {
        return cmcType;
    }

    public void setCmcType(Integer cmcType) {
        this.cmcType = cmcType;
    }

    public Long getUniIdMain() {
        return uniIdMain;
    }

    public void setUniIdMain(Long uniIdMain) {
        this.uniIdMain = uniIdMain;
    }

    public Long getUniIdBack() {
        return uniIdBack;
    }

    public void setUniIdBack(Long uniIdBack) {
        this.uniIdBack = uniIdBack;
    }

    public String getCmcName() {
        return cmcName;
    }

    public void setCmcName(String cmcName) {
        this.cmcName = cmcName;
    }

    public String getCmcIp() {
        return cmcIp;
    }

    public void setCmcIp(String cmcIp) {
        this.cmcIp = cmcIp;
    }

    public Long getCmcIpLong() {
        return cmcIpLong;
    }

    public void setCmcIpLong(Long cmcIpLong) {
        this.cmcIpLong = cmcIpLong;
    }

    public Integer getCmcDeviceStyle() {
        return cmcDeviceStyle;
    }

    public void setCmcDeviceStyle(Integer cmcDeviceStyle) {
        this.cmcDeviceStyle = cmcDeviceStyle;
    }

    public String getTopCcmtsSysDescr() {
        return topCcmtsSysDescr;
    }

    public void setTopCcmtsSysDescr(String topCcmtsSysDescr) {
        this.topCcmtsSysDescr = topCcmtsSysDescr;
    }

    public String getTopCcmtsSysObjectId() {
        return topCcmtsSysObjectId;
    }

    public void setTopCcmtsSysObjectId(String topCcmtsSysObjectId) {
        this.topCcmtsSysObjectId = topCcmtsSysObjectId;
    }

    public Long getTopCcmtsSysUpTime() {
        return topCcmtsSysUpTime;
    }

    public void setTopCcmtsSysUpTime(Long topCcmtsSysUpTime) {
        this.topCcmtsSysUpTime = topCcmtsSysUpTime;
    }

    public String getTopCcmtsSysContact() {
        return topCcmtsSysContact;
    }

    public void setTopCcmtsSysContact(String topCcmtsSysContact) {
        this.topCcmtsSysContact = topCcmtsSysContact;
    }

    public String getTopCcmtsSysName() {
        return topCcmtsSysName;
    }

    public void setTopCcmtsSysName(String topCcmtsSysName) {
        this.topCcmtsSysName = topCcmtsSysName;
    }

    public String getTopCcmtsSysLocation() {
        return topCcmtsSysLocation;
    }

    public void setTopCcmtsSysLocation(String topCcmtsSysLocation) {
        this.topCcmtsSysLocation = topCcmtsSysLocation;
    }

    public Integer getTopCcmtsSysService() {
        return topCcmtsSysService;
    }

    public void setTopCcmtsSysService(Integer topCcmtsSysService) {
        this.topCcmtsSysService = topCcmtsSysService;
    }

    public Long getTopCcmtsSysORLastChange() {
        return topCcmtsSysORLastChange;
    }

    public void setTopCcmtsSysORLastChange(Long topCcmtsSysORLastChange) {
        this.topCcmtsSysORLastChange = topCcmtsSysORLastChange;
    }

    public Long getTopCcmtsDocsisBaseCapability() {
        return topCcmtsDocsisBaseCapability;
    }

    public void setTopCcmtsDocsisBaseCapability(Long topCcmtsDocsisBaseCapability) {
        this.topCcmtsDocsisBaseCapability = topCcmtsDocsisBaseCapability;
    }

    public Integer getTopCcmtsSysRAMRatio() {
        return topCcmtsSysRAMRatio;
    }

    public void setTopCcmtsSysRAMRatio(Integer topCcmtsSysRAMRatio) {
        this.topCcmtsSysRAMRatio = topCcmtsSysRAMRatio;
    }

    public Integer getTopCcmtsSysCPURatio() {
        return topCcmtsSysCPURatio;
    }

    public void setTopCcmtsSysCPURatio(Integer topCcmtsSysCPURatio) {
        this.topCcmtsSysCPURatio = topCcmtsSysCPURatio;
    }

    public Integer getTopCcmtsSysFlashRatio() {
        return topCcmtsSysFlashRatio;
    }

    public void setTopCcmtsSysFlashRatio(Integer topCcmtsSysFlashRatio) {
        this.topCcmtsSysFlashRatio = topCcmtsSysFlashRatio;
    }

    public String getTopCcmtsSysMacAddr() {
        if (topCcmtsSysMacAddrLong != null) {
            this.topCcmtsSysMacAddr = new MacUtils(topCcmtsSysMacAddrLong).toString(MacUtils.MAOHAO).toUpperCase();
        }
        return topCcmtsSysMacAddr;
    }

    public void setTopCcmtsSysMacAddr(String topCcmtsSysMacAddr) {
        this.topCcmtsSysMacAddr = topCcmtsSysMacAddr;
    }

    public Long getTopCcmtsSysMacAddrLong() {
        return topCcmtsSysMacAddrLong;
    }

    public void setTopCcmtsSysMacAddrLong(Long topCcmtsSysMacAddrLong) {
        this.topCcmtsSysMacAddrLong = topCcmtsSysMacAddrLong;
    }

    public Integer getTopCcmtsCmNumTotal() {
        return topCcmtsCmNumTotal;
    }

    public void setTopCcmtsCmNumTotal(Integer topCcmtsCmNumTotal) {
        this.topCcmtsCmNumTotal = topCcmtsCmNumTotal;
    }

    public Integer getTopCcmtsCmNumOutline() {
        return topCcmtsCmNumOutline;
    }

    public void setTopCcmtsCmNumOutline(Integer topCcmtsCmNumOutline) {
        this.topCcmtsCmNumOutline = topCcmtsCmNumOutline;
    }

    public Integer getTopCcmtsCmNumOnline() {
        return topCcmtsCmNumOnline;
    }

    public void setTopCcmtsCmNumOnline(Integer topCcmtsCmNumOnline) {
        this.topCcmtsCmNumOnline = topCcmtsCmNumOnline;
    }

    public Integer getTopCcmtsCmNumReg() {
        return topCcmtsCmNumReg;
    }

    public void setTopCcmtsCmNumReg(Integer topCcmtsCmNumReg) {
        this.topCcmtsCmNumReg = topCcmtsCmNumReg;
    }

    public Integer getTopCcmtsCmNumRanged() {
        return topCcmtsCmNumRanged;
    }

    public void setTopCcmtsCmNumRanged(Integer topCcmtsCmNumRanged) {
        this.topCcmtsCmNumRanged = topCcmtsCmNumRanged;
    }

    public Integer getTopCcmtsCmNumRanging() {
        return topCcmtsCmNumRanging;
    }

    public void setTopCcmtsCmNumRanging(Integer topCcmtsCmNumRanging) {
        this.topCcmtsCmNumRanging = topCcmtsCmNumRanging;
    }

    public Integer getTopCcmtsSysStatus() {
        return topCcmtsSysStatus;
    }

    public void setTopCcmtsSysStatus(Integer topCcmtsSysStatus) {
        this.topCcmtsSysStatus = topCcmtsSysStatus;
    }

    public String getCmcRunTime() {
        return cmcRunTime;
    }

    public void setCmcRunTime(String cmcRunTime) {
        this.cmcRunTime = cmcRunTime;
    }

    /**
     * @return the nmName
     */
    public String getNmName() {
        return nmName;
    }

    /**
     * @param nmName the nmName to set
     */
    public void setNmName(String nmName) {
        this.nmName = nmName;
    }

    public Timestamp getDt() {
        return dt;
    }

    public void setDt(Timestamp dt) {
        this.dt = dt;
    }

    public Timestamp getLastDeregisterTime() {
        return lastDeregisterTime;
    }

    public void setLastDeregisterTime(Timestamp lastDeregisterTime) {
        this.lastDeregisterTime = lastDeregisterTime;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Cmc [entityId=").append(entityId);
        sb.append(", cmcEntityId=").append(cmcEntityId);
        sb.append(", cmcIndex=").append(cmcIndex);
        sb.append(", onuId=").append(onuId);
        sb.append(", onuIndex=").append(onuIndex);
        sb.append(", cmcType=").append(cmcType);
        sb.append(", uniIdMain=").append(uniIdMain);
        sb.append(", uniIdBack=").append(uniIdBack);
        sb.append(", cmcName=").append(cmcName);
        sb.append(", cmcIp=").append(cmcIp);
        sb.append(", cmcIpLong=").append(cmcIpLong);
        sb.append(", cmcDeviceStyle=").append(cmcDeviceStyle);
        sb.append(", topCcmtsSysDescr=").append(topCcmtsSysDescr);
        sb.append(", topCcmtsSysObjectId=").append(topCcmtsSysObjectId);
        sb.append(", topCcmtsSysUpTime=").append(topCcmtsSysUpTime);
        sb.append(", topCcmtsSysContact=").append(topCcmtsSysContact);
        sb.append(", topCcmtsSysName=").append(topCcmtsSysName);
        sb.append(", topCcmtsSysLocation=").append(topCcmtsSysLocation);
        sb.append(", topCcmtsSysService=").append(topCcmtsSysService);
        sb.append(", topCcmtsSysORLastChange=").append(topCcmtsSysORLastChange);
        sb.append(", topCcmtsDocsisBaseCapability=").append(topCcmtsDocsisBaseCapability);
        sb.append(", topCcmtsSysRAMRatio=").append(topCcmtsSysRAMRatio);
        sb.append(", topCcmtsSysCPURatio=").append(topCcmtsSysCPURatio);
        sb.append(", topCcmtsSysFlashRatio=").append(topCcmtsSysFlashRatio);
        sb.append(", topCcmtsSysMacAddr=").append(topCcmtsSysMacAddr);
        sb.append(", topCcmtsSysMacAddrLong=").append(topCcmtsSysMacAddrLong);
        sb.append(", topCcmtsCmNumTotal=").append(topCcmtsCmNumTotal);
        sb.append(", topCcmtsCmNumOutline=").append(topCcmtsCmNumOutline);
        sb.append(", topCcmtsCmNumOnline=").append("topCcmtsCmNumOnline");
        sb.append(", topCcmtsCmNumReg=").append(topCcmtsCmNumReg);
        sb.append(", topCcmtsCmNumRanged=").append(topCcmtsCmNumRanged);
        sb.append(", topCcmtsCmNumRanging=").append(topCcmtsCmNumRanging);
        sb.append(Symbol.BRACKET_RIGHT);
        return sb.toString();
    }
}
