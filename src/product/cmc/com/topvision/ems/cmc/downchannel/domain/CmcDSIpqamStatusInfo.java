/***********************************************************************
 * $Id: CC8800bHttpDSIpqamStatusInfo.java,v1.0 2013-10-22 上午10:46:36 $
 * 
 * @author:bryan
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.downchannel.domain;

import java.io.Serializable;
import java.math.BigDecimal;

import org.apache.ibatis.type.Alias;

import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author bryan
 * @created @2013-10-22-上午10:46:36
 *
 */
@Alias("dsIpqamStatusInfo")
public class CmcDSIpqamStatusInfo implements Serializable, AliasesSuperType{
    
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private Long cmcPortId;//   bigint(20) ,
    private Long cmcId;//                   bigint(20),
    //private Integer channelType;//             Tinyint(2),
    //private Integer docsIfDownChannelId;//     int(3),
    private Integer ipqamOutputQAMChannel;//   int(3),
    private String ipqamFrequency;//  varchar(7),
    private Integer ipqamUsedUDPPorts;//   int(3),
    private String ipqamUsedBandwidth;//  varchar(7),
    private String ipqamBandwidthCapacity;//  varchar(7),
    private BigDecimal ipqamPercent;//    decimal(6,3),
    private Integer ipqamAtten;//  int(11),
    private String ipqamSymbolRate;// varchar(7),
    private String ipqamModulation;// varchar(10)
    
    /**
     * @return the cmcPortId
     */
    public Long getCmcPortId() {
        return cmcPortId;
    }
    /**
     * @param cmcPortId the cmcPortId to set
     */
    public void setCmcPortId(Long cmcPortId) {
        this.cmcPortId = cmcPortId;
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
     * @return the ipqamOutputQAMChannel
     */
    public Integer getIpqamOutputQAMChannel() {
        return ipqamOutputQAMChannel;
    }
    /**
     * @param ipqamOutputQAMChannel the ipqamOutputQAMChannel to set
     */
    public void setIpqamOutputQAMChannel(Integer ipqamOutputQAMChannel) {
        this.ipqamOutputQAMChannel = ipqamOutputQAMChannel;
    }
    /**
     * @return the ipqamFrequency
     */
    public String getIpqamFrequency() {
        return ipqamFrequency;
    }
    /**
     * @param ipqamFrequency the ipqamFrequency to set
     */
    public void setIpqamFrequency(String ipqamFrequency) {
        this.ipqamFrequency = ipqamFrequency;
    }
    /**
     * @return the ipqamUsedUDPPorts
     */
    public Integer getIpqamUsedUDPPorts() {
        return ipqamUsedUDPPorts;
    }
    /**
     * @param ipqamUsedUDPPorts the ipqamUsedUDPPorts to set
     */
    public void setIpqamUsedUDPPorts(Integer ipqamUsedUDPPorts) {
        this.ipqamUsedUDPPorts = ipqamUsedUDPPorts;
    }
    /**
     * @return the ipqamUsedBandwidth
     */
    public String getIpqamUsedBandwidth() {
        return ipqamUsedBandwidth;
    }
    /**
     * @param ipqamUsedBandwidth the ipqamUsedBandwidth to set
     */
    public void setIpqamUsedBandwidth(String ipqamUsedBandwidth) {
        this.ipqamUsedBandwidth = ipqamUsedBandwidth;
    }
    /**
     * @return the ipqamBandwidthCapacity
     */
    public String getIpqamBandwidthCapacity() {
        return ipqamBandwidthCapacity;
    }
    /**
     * @param ipqamBandwidthCapacity the ipqamBandwidthCapacity to set
     */
    public void setIpqamBandwidthCapacity(String ipqamBandwidthCapacity) {
        this.ipqamBandwidthCapacity = ipqamBandwidthCapacity;
    }
    /**
     * @return the ipqamPercent
     */
    public BigDecimal getIpqamPercent() {
        BigDecimal bd = new BigDecimal(100.000);
        if ((ipqamPercent.compareTo(bd))==1) {
            return bd;
        }
        return ipqamPercent;
    }
    /**
     * @param ipqamPercent the ipqamPercent to set
     */
    public void setIpqamPercent(BigDecimal ipqamPercent) {
        this.ipqamPercent = ipqamPercent;
    }
    /**
     * @return the ipqamAtten
     */
    public Integer getIpqamAtten() {
        return ipqamAtten;
    }
    /**
     * @param ipqamAtten the ipqamAtten to set
     */
    public void setIpqamAtten(Integer ipqamAtten) {
        this.ipqamAtten = ipqamAtten;
    }
    /**
     * @return the ipqamSymbolRate
     */
    public String getIpqamSymbolRate() {
        return ipqamSymbolRate;
    }
    /**
     * @param ipqamSymbolRate the ipqamSymbolRate to set
     */
    public void setIpqamSymbolRate(String ipqamSymbolRate) {
        this.ipqamSymbolRate = ipqamSymbolRate;
    }
    /**
     * @return the ipqamModulation
     */
    public String getIpqamModulation() {
        return ipqamModulation;
    }
    /**
     * @param ipqamModulation the ipqamModulation to set
     */
    public void setIpqamModulation(String ipqamModulation) {
        this.ipqamModulation = ipqamModulation;
    }
    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "CmcDSIpqamStatusInfo [cmcPortId=" + cmcPortId + ", cmcId=" + cmcId + ", ipqamOutputQAMChannel="
                + ipqamOutputQAMChannel + ", ipqamFrequency=" + ipqamFrequency + ", ipqamUsedUDPPorts="
                + ipqamUsedUDPPorts + ", ipqamUsedBandwidth=" + ipqamUsedBandwidth + ", ipqamBandwidthCapacity="
                + ipqamBandwidthCapacity + ", ipqamPercent=" + ipqamPercent + ", ipqamAtten=" + ipqamAtten
                + ", ipqamSymbolRate=" + ipqamSymbolRate + ", ipqamModulation=" + ipqamModulation + "]";
    }
    
    
    
}
