/***********************************************************************
 * $Id: Cmc8800BHttpDSIpqamMappings.java,v1.0 2013-10-22 上午10:56:44 $
 * 
 * @author:bryan
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.downchannel.domain;

import java.io.Serializable;

import org.apache.ibatis.type.Alias;

import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author bryan
 * @created @2013-10-22-上午10:56:44
 *
 */
@Alias("dsIpqamMappingsInfo")
public class CmcDSIpqamMappings implements Serializable, AliasesSuperType {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private Long cmcId;//
    private Long mappingId;//   bigint(20)  ,
    private Integer ipqamOutputQAMChannel;//   int(3),
    private String ipqamPidMapString="";//   varchar(20),
    private String ipqamPidMapStringShow = "-";
    private String ipqamDestinationIPAddress = "0.0.0.0";//   varchar(15),
    private Integer ipqamOldUDPPort=0;
    private Integer ipqamUDPPort=0;//    int,
    private Integer ipqamActive = 1;// int,
    private Integer ipqamStreamType=0;// int,
    private Integer ipqamProgramNumberInput=0;// int,
    private Integer ipqamProgramNumberOutput = 1;//    int,
    private Integer ipqamPMV=0;//    int,
    private Integer ipqamDataRateEnable=1;// int,
    private Integer ipqamDataRate=0;//   int
    
    private Integer ipqamAction;//
    /**
     * @return the mappingId
     */
    public Long getMappingId() {
        return mappingId;
    }
    /**
     * @param mappingId the mappingId to set
     */
    public void setMappingId(Long mappingId) {
        this.mappingId = mappingId;
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
     * @return the ipqamPidMapString
     */
    public String getIpqamPidMapString() {
        return ipqamPidMapString;
    }
    /**
     * @param ipqamPidMapString the ipqamPidMapString to set
     */
    public void setIpqamPidMapString(String ipqamPidMapString) {
        this.ipqamPidMapString = ipqamPidMapString;
    }
    /**
     * @return the ipqamDestinationIPAddress
     */
    public String getIpqamDestinationIPAddress() {
        return ipqamDestinationIPAddress;
    }
    /**
     * @param ipqamDestinationIPAddress the ipqamDestinationIPAddress to set
     */
    public void setIpqamDestinationIPAddress(String ipqamDestinationIPAddress) {
        this.ipqamDestinationIPAddress = ipqamDestinationIPAddress;
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
     * @return the ipqamStreamType
     */
    public Integer getIpqamStreamType() {
        return ipqamStreamType;
    }
    /**
     * @param ipqamStreamType the ipqamStreamType to set
     */
    public void setIpqamStreamType(Integer ipqamStreamType) {
        this.ipqamStreamType = ipqamStreamType;
    }
    /**
     * @return the ipqamProgramNumberInput
     */
    public Integer getIpqamProgramNumberInput() {
        return ipqamProgramNumberInput;
    }
    /**
     * @param ipqamProgramNumberInput the ipqamProgramNumberInput to set
     */
    public void setIpqamProgramNumberInput(Integer ipqamProgramNumberInput) {
        this.ipqamProgramNumberInput = ipqamProgramNumberInput;
    }
    /**
     * @return the ipqamProgramNumberOutput
     */
    public Integer getIpqamProgramNumberOutput() {
        return ipqamProgramNumberOutput;
    }
    /**
     * @param ipqamProgramNumberOutput the ipqamProgramNumberOutput to set
     */
    public void setIpqamProgramNumberOutput(Integer ipqamProgramNumberOutput) {
        this.ipqamProgramNumberOutput = ipqamProgramNumberOutput;
    }
    /**
     * @return the ipqamPMV
     */
    public Integer getIpqamPMV() {
        return ipqamPMV;
    }
    /**
     * @param ipqamPMV the ipqamPMV to set
     */
    public void setIpqamPMV(Integer ipqamPMV) {
        this.ipqamPMV = ipqamPMV;
    }
    /**
     * @return the ipqamDataRateEnable
     */
    public Integer getIpqamDataRateEnable() {
        return ipqamDataRateEnable;
    }
    /**
     * @param ipqamDataRateEnable the ipqamDataRateEnable to set
     */
    public void setIpqamDataRateEnable(Integer ipqamDataRateEnable) {
        this.ipqamDataRateEnable = ipqamDataRateEnable;
    }
    /**
     * @return the ipqamDataRate
     */
    public Integer getIpqamDataRate() {
        return ipqamDataRate;
    }
    /**
     * @param ipqamDataRate the ipqamDataRate to set
     */
    public void setIpqamDataRate(Integer ipqamDataRate) {
        this.ipqamDataRate = ipqamDataRate;
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
    
    
    /**
     * @return the ipqamAction
     */
    public Integer getIpqamAction() {
        return ipqamAction;
    }
    /**
     * @param ipqamAction the ipqamAction to set
     */
    public void setIpqamAction(Integer ipqamAction) {
        this.ipqamAction = ipqamAction;
    }
    
    /**
     * @return the ipqamOldUDPPort
     */
    public Integer getIpqamOldUDPPort() {
        return ipqamOldUDPPort;
    }
    /**
     * @param ipqamOldUDPPort the ipqamOldUDPPort to set
     */
    public void setIpqamOldUDPPort(Integer ipqamOldUDPPort) {
        this.ipqamOldUDPPort = ipqamOldUDPPort;
    }
    
    /**
     * @return the ipqamPidMapStringShow
     */
    public String getIpqamPidMapStringShow() {
        if (ipqamPidMapString!=null) {
            ipqamPidMapStringShow = "";
            String[]  tmp = ipqamPidMapString.split(",");
            for(int i = 1; i < tmp.length - 1; i = i + 2){
                ipqamPidMapStringShow +=","+tmp[i] + '-' + tmp[i + 1];
            }
            if(ipqamPidMapStringShow.length()>=2){
                ipqamPidMapStringShow = ipqamPidMapStringShow.replaceFirst(",", "");//.substring(1, ipqamPidMapStringShow.length());                
            }
        }
        
        return ipqamPidMapStringShow;
    }
    /**
     * @param ipqamPidMapStringShow the ipqamPidMapStringShow to set
     */
    public void setIpqamPidMapStringShow(String ipqamPidMapStringShow) {
        this.ipqamPidMapStringShow = ipqamPidMapStringShow;
    }
    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "CmcDSIpqamMappings [cmcId=" + cmcId + ", mappingId=" + mappingId + ", ipqamOutputQAMChannel="
                + ipqamOutputQAMChannel + ", ipqamPidMapString=" + ipqamPidMapString + ", ipqamDestinationIPAddress="
                + ipqamDestinationIPAddress + ", ipqamUDPPort=" + ipqamUDPPort + ", ipqamActive=" + ipqamActive
                + ", ipqamStreamType=" + ipqamStreamType + ", ipqamProgramNumberInput=" + ipqamProgramNumberInput
                + ", ipqamProgramNumberOutput=" + ipqamProgramNumberOutput + ", ipqamPMV=" + ipqamPMV
                + ", ipqamDataRateEnable=" + ipqamDataRateEnable + ", ipqamDataRate=" + ipqamDataRate
                + ", ipqamAction=" + ipqamAction + "]";
    }
}
