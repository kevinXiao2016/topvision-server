/***********************************************************************
 * $Id: CmcDSIpqamISInfo.java,v1.0 2013-10-22 上午11:02:11 $
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
 * @created @2013-10-22-上午11:02:11
 *
 */
@Alias("dsIpqamIsInfo")
public class CmcDSIpqamISInfo implements Serializable, AliasesSuperType {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private Long isProgramId;// bigint(20),
    private Long cmcId;//bigint(20),
    private String ipqamDestinationIP;//  varchar(15),
    private Integer ipqamSendMode;//   int,
    private String ipqamSourceIP;//   varchar(15),
    private Integer ipqamSourcePort;// int,
    private Integer ipqamUDPPort;//    int,
    private Integer ipqamProgType;//   int,
    private Integer ipqamSYNC;//   int,
    private Integer ipqamType;//   int,
    private Integer ipqamInputProgramNumber;// int,
    private Integer ipqamInputPMTID;// int,
    private Integer ipqamInputPCRID;// int,
    private Integer ipqamTotalESPIDs;//    int,
    private BigDecimal ipqamInputBitrate;//   decimal(6,3)
    /**
     * @return the isProgramId
     */
    public Long getIsProgramId() {
        return isProgramId;
    }
    /**
     * @param isProgramId the isProgramId to set
     */
    public void setIsProgramId(Long isProgramId) {
        this.isProgramId = isProgramId;
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
     * @return the ipqamSendMode
     */
    public Integer getIpqamSendMode() {
        return ipqamSendMode;
    }
    /**
     * @param ipqamSendMode the ipqamSendMode to set
     */
    public void setIpqamSendMode(Integer ipqamSendMode) {
        this.ipqamSendMode = ipqamSendMode;
    }
    /**
     * @return the ipqamSourceIP
     */
    public String getIpqamSourceIP() {
        return ipqamSourceIP;
    }
    /**
     * @param ipqamSourceIP the ipqamSourceIP to set
     */
    public void setIpqamSourceIP(String ipqamSourceIP) {
        this.ipqamSourceIP = ipqamSourceIP;
    }
    /**
     * @return the ipqamSourcePort
     */
    public Integer getIpqamSourcePort() {
        return ipqamSourcePort;
    }
    /**
     * @param ipqamSourcePort the ipqamSourcePort to set
     */
    public void setIpqamSourcePort(Integer ipqamSourcePort) {
        this.ipqamSourcePort = ipqamSourcePort;
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
     * @return the ipqamProgType
     */
    public Integer getIpqamProgType() {
        return ipqamProgType;
    }
    /**
     * @param ipqamProgType the ipqamProgType to set
     */
    public void setIpqamProgType(Integer ipqamProgType) {
        this.ipqamProgType = ipqamProgType;
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
     * @return the ipqamInputProgramNumber
     */
    public Integer getIpqamInputProgramNumber() {
        return ipqamInputProgramNumber;
    }
    /**
     * @param ipqamInputProgramNumber the ipqamInputProgramNumber to set
     */
    public void setIpqamInputProgramNumber(Integer ipqamInputProgramNumber) {
        this.ipqamInputProgramNumber = ipqamInputProgramNumber;
    }
    /**
     * @return the ipqamInputPMTID
     */
    public Integer getIpqamInputPMTID() {
        return ipqamInputPMTID;
    }
    /**
     * @param ipqamInputPMTID the ipqamInputPMTID to set
     */
    public void setIpqamInputPMTID(Integer ipqamInputPMTID) {
        this.ipqamInputPMTID = ipqamInputPMTID;
    }
    /**
     * @return the ipqamInputPCRID
     */
    public Integer getIpqamInputPCRID() {
        return ipqamInputPCRID;
    }
    /**
     * @param ipqamInputPCRID the ipqamInputPCRID to set
     */
    public void setIpqamInputPCRID(Integer ipqamInputPCRID) {
        this.ipqamInputPCRID = ipqamInputPCRID;
    }
    /**
     * @return the ipqamTotalESPIDs
     */
    public Integer getIpqamTotalESPIDs() {
        return ipqamTotalESPIDs;
    }
    /**
     * @param ipqamTotalESPIDs the ipqamTotalESPIDs to set
     */
    public void setIpqamTotalESPIDs(Integer ipqamTotalESPIDs) {
        this.ipqamTotalESPIDs = ipqamTotalESPIDs;
    }
    /**
     * @return the ipqamInputBitrate
     */
    public BigDecimal getIpqamInputBitrate() {
        return ipqamInputBitrate;
    }
    /**
     * @param ipqamInputBitrate the ipqamInputBitrate to set
     */
    public void setIpqamInputBitrate(BigDecimal ipqamInputBitrate) {
        this.ipqamInputBitrate = ipqamInputBitrate;
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
        return "CmcDSIpqamISInfo [isProgramId=" + isProgramId + ", cmcId=" + cmcId + ", ipqamDestinationIP="
                + ipqamDestinationIP + ", ipqamSendMode=" + ipqamSendMode + ", ipqamSourceIP=" + ipqamSourceIP
                + ", ipqamSourcePort=" + ipqamSourcePort + ", ipqamUDPPort=" + ipqamUDPPort + ", ipqamProgType="
                + ipqamProgType + ", ipqamSYNC=" + ipqamSYNC + ", ipqamType=" + ipqamType
                + ", ipqamInputProgramNumber=" + ipqamInputProgramNumber + ", ipqamInputPMTID=" + ipqamInputPMTID
                + ", ipqamInputPCRID=" + ipqamInputPCRID + ", ipqamTotalESPIDs=" + ipqamTotalESPIDs
                + ", ipqamInputBitrate=" + ipqamInputBitrate + "]";
    }
   

}
