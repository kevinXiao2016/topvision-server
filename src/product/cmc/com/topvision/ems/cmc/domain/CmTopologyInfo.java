/***********************************************************************
 * $Id: TopologyInfo.java,v1.0 2011-10-31 上午10:20:48 $
 * 
 * @author: Administrator
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.domain;

import java.io.Serializable;
import java.util.List;

import org.apache.ibatis.type.Alias;

import com.topvision.framework.constants.Symbol;
import com.topvision.framework.utils.CmcIndexUtils;
import com.topvision.framework.common.MacUtils;
import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author Administrator
 * @created @2011-10-31-上午10:20:48
 * 
 */
@Alias("cmTopologyInfo")
public class CmTopologyInfo implements Serializable, AliasesSuperType {
    private static final long serialVersionUID = 1024370276029470654L;
    private Long cmcIndex;
    private Long cmId;
    // 2.0上行信道
    private Long docsIfUpChannelId;
    // 3.0上行信道
    private List<Long> upChannelIdList;
    private String upChannelListString;
    private Long docsIfDownChannelId;
    private Long cmcId;
    private Long ponId;// 表示数据库ponId
    private Long ponNo;// 新增表示pon口 ID
    private Long entityId;
    private Long deviceType;
    private String ip;// entity ip
    private String mac;// cm mac
    private Long cmcMac;
    private String cmcMacString;
    private String slot;
    private String cmNo;
    private String upDesrc;
    private String downDesrc;
    private Long upChannelIndex; // YangYi增加,2013-10-19,上行信道Index，用于CMTS上行信道显示
    private Long downChannelIndex;// YangYi增加,2013-10-19,下行信道Index，用于CMTS下行信道显示

    public Long getUpChannelIndex() {
        return upChannelIndex;
    }

    public void setUpChannelIndex(Long upChannelIndex) {
        this.upChannelIndex = upChannelIndex;
    }

    public Long getDownChannelIndex() {
        return downChannelIndex;
    }

    public void setDownChannelIndex(Long downChannelIndex) {
        this.downChannelIndex = downChannelIndex;
    }

    public String getSlot() {
        if (cmcIndex != null) {
            String str = Long.toBinaryString(cmcIndex);
            int len = str.length();
            if (len < 32) {
                int lent = 32 - len;
                for (int i = 0; i < lent; i++) {
                    str = "0" + str;
                }
            }
            String slotString = (String) str.subSequence(str.length() - 32, str.length() - 27);// slot
            slot = Integer.valueOf(slotString, 2).toString();
        }
        return slot;
    }

    public void setSlot(String slot) {
        this.slot = slot;
    }

    /**
     * @return the cmId
     */
    public Long getCmId() {
        return cmId;
    }

    /**
     * @param cmId
     *            the cmId to set
     */
    public void setCmId(Long cmId) {
        this.cmId = cmId;
    }

    /**
     * @return the cmcId
     */
    public Long getCmcId() {
        return cmcId;
    }

    /**
     * @param cmcId
     *            the cmcId to set
     */
    public void setCmcId(Long cmcId) {
        this.cmcId = cmcId;
    }

    /**
     * @return the ponId
     */
    public Long getPonId() {
        return ponId;
    }

    /**
     * @param ponId
     *            the ponId to set
     */
    public void setPonId(Long ponId) {
        this.ponId = ponId;
    }

    public Long getPonNo() {
        if (cmcIndex != null) {
            String str = Long.toBinaryString(cmcIndex);
            int len = str.length();
            if (len < 32) {
                int lent = 32 - len;
                for (int i = 0; i < lent; i++) {
                    str = "0" + str;
                }
            }
            String ponString = (String) str.subSequence(str.length() - 27, str.length() - 23);// pon
            ponNo = Long.valueOf(ponString, 2);
        } else {
            ponNo = null;
        }
        return ponNo;
    }

    public void setPonNo(Long ponNo) {
        this.ponNo = ponNo;
    }

    /**
     * @return the entityId
     */
    public Long getEntityId() {
        return entityId;
    }

    /**
     * @param entityId
     *            the entityId to set
     */
    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public Long getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(Long deviceType) {
        this.deviceType = deviceType;
    }

    /**
     * @return the ip
     */
    public String getIp() {
        return ip;
    }

    /**
     * @param ip
     *            the ip to set
     */
    public void setIp(String ip) {
        this.ip = ip;
    }

    /**
     * @return the cmcMac
     */
    public Long getCmcMac() {
        return cmcMac;
    }

    /**
     * @param cmcMac
     *            the cmcMac to set
     */
    public void setCmcMac(Long cmcMac) {
        this.cmcMac = cmcMac;
        //生成标准格式的mac字符串
        this.cmcMacString = new MacUtils(cmcMac).toString(MacUtils.MAOHAO).toUpperCase();
    }

    /**
     * @return the mac
     */
    public String getMac() {
        return mac;
    }

    /**
     * @param mac
     *            the mac to set
     */
    public void setMac(String mac) {
        this.mac = mac;
    }

    /**
     * @return the docsIfUpChannelId
     */
    public Long getDocsIfUpChannelId() {
        return docsIfUpChannelId;
    }

    /**
     * @param docsIfUpChannelId
     *            the docsIfUpChannelId to set
     */
    public void setDocsIfUpChannelId(Long docsIfUpChannelId) {
        this.docsIfUpChannelId = docsIfUpChannelId;
    }

    /**
     * @return the docsIfDownChannelId
     */
    public Long getDocsIfDownChannelId() {
        return docsIfDownChannelId;
    }

    /**
     * @param docsIfDownChannelId
     *            the docsIfDownChannelId to set
     */
    public void setDocsIfDownChannelId(Long docsIfDownChannelId) {
        this.docsIfDownChannelId = docsIfDownChannelId;
    }

    /**
     * @return the cmcMacString
     */
    public String getCmcMacString() {
        /*if (cmcMac != null) {
            this.cmcMacString = new MacUtils(cmcMac).toString(MacUtils.MAOHAO).toUpperCase();
        } else {
            cmcMacString = null;
        }*/
        return cmcMacString;
    }

    /**
     * @param cmcMacString
     *            the cmcMacString to set
     */
    public void setCmcMacString(String cmcMacString) {
        this.cmcMacString = cmcMacString;
        //生成long型的mac
        this.cmcMac = new MacUtils(cmcMacString).longValue();
    }

    /**
     * @return the cmcIndex
     */
    public Long getCmcIndex() {
        return cmcIndex;
    }

    /**
     * @param cmcIndex
     *            the cmcIndex to set
     */
    public void setCmcIndex(Long cmcIndex) {
        this.cmcIndex = cmcIndex;
    }

    public String getCmNo() {
        if (getCmcIndex() != null) {
            Long cmNol;
            cmNol = ((this.getCmcIndex()) & 0x007F0000) >> 16;
            cmNo = cmNol.toString();
        } else {
            cmNo = null;
        }
        return cmNo;
    }

    public void setCmNo(String cmNo) {
        this.cmNo = cmNo;
    }

    public List<Long> getUpChannelIdList() {
        return upChannelIdList;
    }

    public void setUpChannelIdList(List<Long> upChannelIdList) {
        this.upChannelIdList = upChannelIdList;
    }

    public String getUpChannelListString() {
        upChannelListString = "";
        if (upChannelIdList != null && upChannelIdList.size() != 0) {
            for (int i = 0; i < upChannelIdList.size(); i++) {
                if (i == 0) {
                    upChannelListString = CmcIndexUtils.getChannelId(upChannelIdList.get(i)).toString();
                } else {
                    upChannelListString = upChannelListString + ","
                            + CmcIndexUtils.getChannelId(upChannelIdList.get(i)).toString();
                }
            }
        } else if(docsIfUpChannelId != null){
            //为了兼容CC版本不匹配导致的3.0MIB里面取不到上行信道信息的问题，如果3.0mib里取不到就取2.0mib里的上行信道信息
            upChannelListString = this.docsIfUpChannelId.toString();
        }
        return upChannelListString;
    }

    public void setUpChannelListString(String upChannelListString) {
        this.upChannelListString = upChannelListString;
    }

    public String getDownDesrc() {
        return downDesrc;
    }

    public void setDownDesrc(String downDesrc) {
        this.downDesrc = downDesrc;
    }

    public String getUpDesrc() {
        return upDesrc;
    }

    public void setUpDesrc(String upDesrc) {
        this.upDesrc = upDesrc;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("CmTopologyInfo [cmcIndex=");
        builder.append(cmcIndex);
        builder.append(", cmId=");
        builder.append(cmId);
        builder.append(", docsIfUpChannelId=");
        builder.append(docsIfUpChannelId);
        builder.append(", upChannelIdList=");
        builder.append(upChannelIdList);
        builder.append(", upChannelListString=");
        builder.append(upChannelListString);
        builder.append(", docsIfDownChannelId=");
        builder.append(docsIfDownChannelId);
        builder.append(", cmcId=");
        builder.append(cmcId);
        builder.append(", ponId=");
        builder.append(ponId);
        builder.append(", ponNo=");
        builder.append(ponNo);
        builder.append(", entityId=");
        builder.append(entityId);
        builder.append(", deviceType=");
        builder.append(deviceType);
        builder.append(", ip=");
        builder.append(ip);
        builder.append(", mac=");
        builder.append(mac);
        builder.append(", cmcMac=");
        builder.append(cmcMac);
        builder.append(", cmcMacString=");
        builder.append(cmcMacString);
        builder.append(", slot=");
        builder.append(slot);
        builder.append(", cmNo=");
        builder.append(cmNo);
        builder.append(Symbol.BRACKET_RIGHT);
        return builder.toString();
    }

}
