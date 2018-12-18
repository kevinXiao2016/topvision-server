/***********************************************************************
 * $Id: CmcDSIpqamBaseInfo.java,v1.0 2013-10-12 上午09:07:27 $
 * 
 * @author:bryan
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.downchannel.domain;

import java.io.Serializable;
import java.text.DecimalFormat;

import org.apache.ibatis.type.Alias;

import com.topvision.framework.dao.mybatis.AliasesSuperType;
import com.topvision.platform.zetaframework.var.UnitConfigConstant;

/**
 * @author bryan
 * @created @2013-10-12-上午09:07:27
 *
 */
@Alias("dsIpqamBaseInfo")
public class CmcDSIpqamBaseInfo implements Serializable, AliasesSuperType{

    /**
     * 
     */
    private static final long serialVersionUID = -3173243759455533613L;
    
    //private Long entityId;
    private Long cmcId;
    private Long cmcPortId;
    private Long channelIndex;//
    private Integer docsIfDownChannelId; // 下行通道ID
    private Integer docsIfDownChannelSymRate;// 下行信道符号率    
    private Integer ipqamTranspStreamID; // 
    private Integer ipqamOriginalNetworkID; // 下行通道
    private Integer ipqamQAMManager; // 下行通道
    private String ipqamQAMGroupName;//
    private String ipqamAtten;//
    private Integer ipqamDtsAdjust; // 下行通道
    
    private Long docsIfDownChannelFrequency; // 下行通道频率HZ
    private Integer docsIfDownChannelWidth; // 下行通道带宽HZ
    private Integer ifAdminStatus;
    private Integer ifOperStatus;
    private Integer docsIfDownChannelModulation; // 下行通道调制信息
    private Integer docsIfDownChannelInterleave; // 下行通道交织
    private Long docsIfDownChannelPower; // 下行通道电平dBmV
    private Integer docsIfDownChannelAnnex; // 下行通道标准(eo,usa)
    /**
     * 在IPQAM模式下，channelType采集值与ifAdminStatus值一致 （3）
     */
    private Integer channelType;//使用http方式采集的值为ifAdminStatus.
    
    private String docsIfDownChannelFrequencyForunit;

    //@flackyang 电平展示
    private double downChannelPower;
    
    public Long getCmcId() {
        return cmcId;
    }
    public void setCmcId(Long cmcId) {
        this.cmcId = cmcId;
    }
    public Long getCmcPortId() {
        return cmcPortId;
    }
    public void setCmcPortId(Long cmcPortId) {
        this.cmcPortId = cmcPortId;
    }
    /**
     * @return the channelIndex
     */
    public Long getChannelIndex() {
        return channelIndex;
    }
    /**
     * @param channelIndex the channelIndex to set
     */
    public void setChannelIndex(Long channelIndex) {
        this.channelIndex = channelIndex;
    }
    public Integer getDocsIfDownChannelId() {
        return docsIfDownChannelId;
    }
    public void setDocsIfDownChannelId(Integer docsIfDownChannelId) {
        this.docsIfDownChannelId = docsIfDownChannelId;
    }
    public Integer getDocsIfDownChannelSymRate() {
        return docsIfDownChannelSymRate;
    }
    public void setDocsIfDownChannelSymRate(Integer docsIfDownChannelSymRate) {
        this.docsIfDownChannelSymRate = docsIfDownChannelSymRate;
    }
    public Integer getIpqamTranspStreamID() {
        return ipqamTranspStreamID;
    }
    public void setIpqamTranspStreamID(Integer ipqamTranspStreamID) {
        this.ipqamTranspStreamID = ipqamTranspStreamID;
    }
    public Integer getIpqamOriginalNetworkID() {
        return ipqamOriginalNetworkID;
    }
    public void setIpqamOriginalNetworkID(Integer ipqamOriginalNetworkID) {
        this.ipqamOriginalNetworkID = ipqamOriginalNetworkID;
    }
    public Integer getIpqamQAMManager() {
        return ipqamQAMManager;
    }
    public void setIpqamQAMManager(Integer ipqamQAMManager) {
        this.ipqamQAMManager = ipqamQAMManager;
    }
    public String getIpqamQAMGroupName() {
        return ipqamQAMGroupName;
    }
    public void setIpqamQAMGroupName(String ipqamQAMGroupName) {
        this.ipqamQAMGroupName = ipqamQAMGroupName;
    }
    public String getIpqamAtten() {
        return ipqamAtten;
    }
    public void setIpqamAtten(String ipqamAtten) {
        this.ipqamAtten = ipqamAtten;
    }
    public Integer getIpqamDtsAdjust() {
        return ipqamDtsAdjust;
    }
    public void setIpqamDtsAdjust(Integer ipqamDtsAdjust) {
        this.ipqamDtsAdjust = ipqamDtsAdjust;
    }
    public static long getSerialversionuid() {
        return serialVersionUID;
    }
    
    public Long getDocsIfDownChannelFrequency() {
        return docsIfDownChannelFrequency;
    }
    public void setDocsIfDownChannelFrequency(Long docsIfDownChannelFrequency) {
        this.docsIfDownChannelFrequency = docsIfDownChannelFrequency;
    }
    public Integer getDocsIfDownChannelWidth() {
        return docsIfDownChannelWidth;
    }
    public void setDocsIfDownChannelWidth(Integer docsIfDownChannelWidth) {
        this.docsIfDownChannelWidth = docsIfDownChannelWidth;
    }
    public Integer getIfAdminStatus() {
        if (channelType!=null) {
            ifAdminStatus = channelType;
        }
        return ifAdminStatus;
    }
    public void setIfAdminStatus(Integer ifAdminStatus) {
        this.ifAdminStatus = ifAdminStatus;
    }
    public Integer getIfOperStatus() {
        return ifOperStatus;
    }
    public void setIfOperStatus(Integer ifOperStatus) {
        this.ifOperStatus = ifOperStatus;
    }
    public Integer getDocsIfDownChannelModulation() {
        return docsIfDownChannelModulation;
    }
    public void setDocsIfDownChannelModulation(Integer docsIfDownChannelModulation) {
        this.docsIfDownChannelModulation = docsIfDownChannelModulation;
    }
    public Integer getDocsIfDownChannelInterleave() {
        return docsIfDownChannelInterleave;
    }
    public void setDocsIfDownChannelInterleave(Integer docsIfDownChannelInterleave) {
        this.docsIfDownChannelInterleave = docsIfDownChannelInterleave;
    }
    public Long getDocsIfDownChannelPower() {
        return docsIfDownChannelPower;
    }
    public void setDocsIfDownChannelPower(Long docsIfDownChannelPower) {
        this.docsIfDownChannelPower = docsIfDownChannelPower;
        if (docsIfDownChannelPower != null) {
            this.downChannelPower = UnitConfigConstant.parsePowerValue((double) docsIfDownChannelPower / 10);
        }
    }
    public Integer getDocsIfDownChannelAnnex() {
        return docsIfDownChannelAnnex;
    }
    public void setDocsIfDownChannelAnnex(Integer docsIfDownChannelAnnex) {
        this.docsIfDownChannelAnnex = docsIfDownChannelAnnex;
    }
    
    public Integer getChannelType() {
        return channelType;
    }
    public void setChannelType(Integer channelType) {
        this.channelType = channelType;
    }
    
    /**
     * @return the docsIfDownChannelFrequencyForunit
     */
    public String getDocsIfDownChannelFrequencyForunit() {
        if (this.docsIfDownChannelFrequency != null) {
            double downChannelFrequency = this.docsIfDownChannelFrequency;
            String freInfo = downChannelFrequency / 1000000 + "";
            if (freInfo.matches("^(([0-9]+)[.]+([0-9]{0,6}))$")) {
                freInfo = freInfo + " MHz";
            } else {
                DecimalFormat df = new DecimalFormat("0.0");
                double info = Double.parseDouble(df.format(downChannelFrequency / 1000000));
                freInfo = info + " MHz";
            }
            docsIfDownChannelFrequencyForunit = freInfo;
        }
        return docsIfDownChannelFrequencyForunit;
    }
    /**
     * @param docsIfDownChannelFrequencyForunit the docsIfDownChannelFrequencyForunit to set
     */
    public void setDocsIfDownChannelFrequencyForunit(String docsIfDownChannelFrequencyForunit) {
        this.docsIfDownChannelFrequencyForunit = docsIfDownChannelFrequencyForunit;
    }

    public double getDownChannelPower() {
        return downChannelPower;
    }

    public void setDownChannelPower(double downChannelPower) {
        this.downChannelPower = downChannelPower;
    }

    @Override
    public String toString() {
        return "CmcDSIpqamBaseInfo [cmcId=" + cmcId + ", cmcPortId=" + cmcPortId + ", channelIndex=" + channelIndex
                + ", docsIfDownChannelId=" + docsIfDownChannelId + ", docsIfDownChannelSymRate="
                + docsIfDownChannelSymRate + ", ipqamTranspStreamID=" + ipqamTranspStreamID
                + ", ipqamOriginalNetworkID=" + ipqamOriginalNetworkID + ", ipqamQAMManager=" + ipqamQAMManager
                + ", ipqamQAMGroupName=" + ipqamQAMGroupName + ", ipqamAtten=" + ipqamAtten + ", ipqamDtsAdjust="
                + ipqamDtsAdjust + ", docsIfDownChannelFrequency=" + docsIfDownChannelFrequency
                + ", docsIfDownChannelWidth=" + docsIfDownChannelWidth + ", ifAdminStatus=" + ifAdminStatus
                + ", ifOperStatus=" + ifOperStatus + ", docsIfDownChannelModulation=" + docsIfDownChannelModulation
                + ", docsIfDownChannelInterleave=" + docsIfDownChannelInterleave + ", docsIfDownChannelPower="
                + docsIfDownChannelPower + ", docsIfDownChannelAnnex=" + docsIfDownChannelAnnex + ", channelType="
                + channelType + ", docsIfDownChannelFrequencyForunit=" + docsIfDownChannelFrequencyForunit + "]";
    }
    
}
