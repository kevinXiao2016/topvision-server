/***********************************************************************
 * $Id: CmPartialSvcState.java,v1.0 2016年11月8日 上午10:04:31 $
 * 
 * @author: vanzand
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.facade.domain;

import java.io.Serializable;

import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author vanzand
 * @created @2016年11月8日-上午10:04:31
 *
 */
public class CmPartialSvcState implements Serializable, AliasesSuperType {

    private static final long serialVersionUID = -5696263449226958366L;

    private Long cmId;
    private Long entityId;
    private Long cmcId;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.2.16.1.1", index = true)
    private Long cmPartialSvcIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.2.16.1.2")
    private Integer cmPrimarySid;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.2.16.1.3")
    private String cmPartialSvcImpairedUS;
    private String partialUpChannels;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.2.16.1.4")
    private String cmPartialSvcImpairedDS;
    private String partialDownChannels;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.2.16.1.5")
    private String cmPartialSvcUSBitmap;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.2.16.1.6")
    private String cmPartialSvcDSBitmap;

    public Long getCmId() {
        return cmId;
    }

    public void setCmId(Long cmId) {
        this.cmId = cmId;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public Long getCmPartialSvcIndex() {
        return cmPartialSvcIndex;
    }

    public void setCmPartialSvcIndex(Long cmPartialSvcIndex) {
        this.cmPartialSvcIndex = cmPartialSvcIndex;
    }

    public Integer getCmPrimarySid() {
        return cmPrimarySid;
    }

    public void setCmPrimarySid(Integer cmPrimarySid) {
        this.cmPrimarySid = cmPrimarySid;
    }

    public String getCmPartialSvcImpairedUS() {
        return cmPartialSvcImpairedUS;
    }

    public void setCmPartialSvcImpairedUS(String cmPartialSvcImpairedUS) {
        this.cmPartialSvcImpairedUS = cmPartialSvcImpairedUS;
        String binaryStr = changeBitMapToBinaryString(cmPartialSvcImpairedUS);
        this.partialUpChannels = selectChannels(binaryStr);
    }

    public String getCmPartialSvcImpairedDS() {
        return cmPartialSvcImpairedDS;
    }

    public void setCmPartialSvcImpairedDS(String cmPartialSvcImpairedDS) {
        this.cmPartialSvcImpairedDS = cmPartialSvcImpairedDS;
        String binaryStr = changeBitMapToBinaryString(cmPartialSvcImpairedDS);
        this.partialDownChannels = selectChannels(binaryStr);
    }
    

    public String getCmPartialSvcUSBitmap() {
        return cmPartialSvcUSBitmap;
    }

    public void setCmPartialSvcUSBitmap(String cmPartialSvcUSBitmap) {
        this.cmPartialSvcUSBitmap = cmPartialSvcUSBitmap;
    }

    public String getCmPartialSvcDSBitmap() {
        return cmPartialSvcDSBitmap;
    }

    public void setCmPartialSvcDSBitmap(String cmPartialSvcDSBitmap) {
        this.cmPartialSvcDSBitmap = cmPartialSvcDSBitmap;
    }

    public Long getCmcId() {
        return cmcId;
    }

    public void setCmcId(Long cmcId) {
        this.cmcId = cmcId;
    }

    public String getPartialUpChannels() {
        return partialUpChannels;
    }

    public void setPartialUpChannels(String partialUpChannels) {
        this.partialUpChannels = partialUpChannels;
    }

    public String getPartialDownChannels() {
        return partialDownChannels;
    }

    public void setPartialDownChannels(String partialDownChannels) {
        this.partialDownChannels = partialDownChannels;
    }
    
    private String changeBitMapToBinaryString(String bitMap) {
        String result = "";
        for (String s : bitMap.split(":")) {
            int i = Integer.parseInt(s, 16);
            int str2 = Integer.parseInt(Integer.toBinaryString(i));
            result += String.format("%08d", str2);
        }
        return result;
    } 

    private String selectChannels(String binaryString) {
        if(binaryString == null || binaryString == "") {
            return "";
        }
        
        StringBuilder channels = new StringBuilder();
        String[] array = binaryString.split("");
        for(int i=0, len=array.length; i<len; i++) {
            if(array[i].equals("1")) {
                channels.append((i+1)).append(",");
            }
        }
        String str = channels.toString();
        if(str.length() == 0) {
            return "";
        }
        return str.substring(0, channels.length()-1);
    }

}
