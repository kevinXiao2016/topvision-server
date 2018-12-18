/***********************************************************************
 * $Id: CmcDSIpqamOSInfo.java,v1.0 2013-10-22 上午11:05:05 $
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
 * @created @2013-10-22-上午11:05:05
 *
 */
@Alias("dsIpqamOsInfo")
public class CmcDSIpqamOSInfo implements Serializable, AliasesSuperType {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private Long osProgramId;// bigint(20),
    private Long cmcId;//bigint(20),
    private Integer ipqamType;//   int,
    private Integer ipqamQAMManager;// int,
    private Integer ipqamOutputQAMChannel;//   int,
    private String ipqamDestinationIP;//  varchar(15),
    private Integer ipqamUDPPort;//    int,
    private Integer ipqamSYNC;//   int,
    private Integer ipqamOutputProgramNumber;//    int,
    private Integer ipqamOutputPMTID;//    int,
    private Integer ipqamOutputPCRID;//    int,
    private BigDecimal ipqamOutputBitrate;//  decimal(6,3),
    private Integer ipqamActive;// int
    /**
     * @return the osProgramId
     */
    public Long getOsProgramId() {
        return osProgramId;
    }
    /**
     * @param osProgramId the osProgramId to set
     */
    public void setOsProgramId(Long osProgramId) {
        this.osProgramId = osProgramId;
    }
    /**
     * @return the ipqamType
     */
    public Integer getIpqamType() {
        return ipqamType;
    }
    /**
     * @param ipqamType the ipqamType to set
     */
    public void setIpqamType(Integer ipqamType) {
        this.ipqamType = ipqamType;
    }
    /**
     * @return the ipqamQAMManager
     */
    public Integer getIpqamQAMManager() {
        return ipqamQAMManager;
    }
    /**
     * @param ipqamQAMManager the ipqamQAMManager to set
     */
    public void setIpqamQAMManager(Integer ipqamQAMManager) {
        this.ipqamQAMManager = ipqamQAMManager;
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
     * @return the ipqamDestinationIP
     */
    public String getIpqamDestinationIP() {
        return ipqamDestinationIP;
    }
    /**
     * @param ipqamDestinationIP the ipqamDestinationIP to set
     */
    public void setIpqamDestinationIP(String ipqamDestinationIP) {
        this.ipqamDestinationIP = ipqamDestinationIP;
    }
    /**
     * @return the ipqamUDPPort
     */
    public Integer getIpqamUDPPort() {
        return ipqamUDPPort;
    }
    /**
     * @param ipqamUDPPort the ipqamUDPPort to set
     */
    public void setIpqamUDPPort(Integer ipqamUDPPort) {
        this.ipqamUDPPort = ipqamUDPPort;
    }
    /**
     * @return the ipqamSYNC
     */
    public Integer getIpqamSYNC() {
        return ipqamSYNC;
    }
    /**
     * @param ipqamSYNC the ipqamSYNC to set
     */
    public void setIpqamSYNC(Integer ipqamSYNC) {
        this.ipqamSYNC = ipqamSYNC;
    }
    /**
     * @return the ipqamOutputProgramNumber
     */
    public Integer getIpqamOutputProgramNumber() {
        return ipqamOutputProgramNumber;
    }
    /**
     * @param ipqamOutputProgramNumber the ipqamOutputProgramNumber to set
     */
    public void setIpqamOutputProgramNumber(Integer ipqamOutputProgramNumber) {
        this.ipqamOutputProgramNumber = ipqamOutputProgramNumber;
    }
    /**
     * @return the ipqamOutputPMTID
     */
    public Integer getIpqamOutputPMTID() {
        return ipqamOutputPMTID;
    }
    /**
     * @param ipqamOutputPMTID the ipqamOutputPMTID to set
     */
    public void setIpqamOutputPMTID(Integer ipqamOutputPMTID) {
        this.ipqamOutputPMTID = ipqamOutputPMTID;
    }
    /**
     * @return the ipqamOutputPCRID
     */
    public Integer getIpqamOutputPCRID() {
        return ipqamOutputPCRID;
    }
    /**
     * @param ipqamOutputPCRID the ipqamOutputPCRID to set
     */
    public void setIpqamOutputPCRID(Integer ipqamOutputPCRID) {
        this.ipqamOutputPCRID = ipqamOutputPCRID;
    }
    /**
     * @return the ipqamOutputBitrate
     */
    public BigDecimal getIpqamOutputBitrate() {
        return ipqamOutputBitrate;
    }
    /**
     * @param ipqamOutputBitrate the ipqamOutputBitrate to set
     */
    public void setIpqamOutputBitrate(BigDecimal ipqamOutputBitrate) {
        this.ipqamOutputBitrate = ipqamOutputBitrate;
    }
    /**
     * @return the ipqamActive
     */
    public Integer getIpqamActive() {
        return ipqamActive;
    }
    /**
     * @param ipqamActive the ipqamActive to set
     */
    public void setIpqamActive(Integer ipqamActive) {
        this.ipqamActive = ipqamActive;
    }
    /**
     * @return the serialversionuid
     */
    public static long getSerialversionuid() {
        return serialVersionUID;
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
    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "CmcDSIpqamOSInfo [osProgramId=" + osProgramId + ", cmcId=" + cmcId + ", ipqamType=" + ipqamType
                + ", ipqamQAMManager=" + ipqamQAMManager + ", ipqamOutputQAMChannel=" + ipqamOutputQAMChannel
                + ", ipqamDestinationIP=" + ipqamDestinationIP + ", ipqamUDPPort=" + ipqamUDPPort + ", ipqamSYNC="
                + ipqamSYNC + ", ipqamOutputProgramNumber=" + ipqamOutputProgramNumber + ", ipqamOutputPMTID="
                + ipqamOutputPMTID + ", ipqamOutputPCRID=" + ipqamOutputPCRID + ", ipqamOutputBitrate="
                + ipqamOutputBitrate + ", ipqamActive=" + ipqamActive + "]";
    }
    
    
    
}
