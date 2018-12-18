/***********************************************************************
 * $Id: CmRealTimeUserStaticReport.java,v1.0 2013-9-11 下午4:50:02 $
 * 
 * @author: fanzidong
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.report.domain;

import com.topvision.ems.cmc.util.ResourcesUtil;

/**
 * @author fanzidong
 * @created @2013-9-11-下午4:50:02
 * 
 */
public class CmRealTimeUserStaticReport {
    public static final String NONEVALUE = "noneValue";

    // 设备ID
    private Long cmcId;
    // 设备名称
    private String cmcName;
    // 区域ID
    private Long folderId;
    // 区域名称
    private String folderName;
    // OLT名称
    private String oltName;
    // OLT的IP
    private String oltIp;
    // PON 的 INDEX
    private Long ponIndex;
    // PON的格式化后的字符串
    private String ponIndexStr;
    // 在线CM总数
    private Integer onLineCmNum;
    // 离线CM总数
    private Integer offLineCmNum;
    // 其他状态CM总数
    private Integer otherStatusCmNum;
    // CM总数
    private Integer allStatusCmNum;

    public Long getCmcId() {
        return cmcId;
    }

    public void setCmcId(Long cmcId) {
        this.cmcId = cmcId;
    }

    public String getCmcName() {
        return cmcName;
    }

    public void setCmcName(String cmcName) {
        this.cmcName = cmcName;
    }

    public Long getFolderId() {
        return folderId;
    }

    public void setFolderId(Long folderId) {
        this.folderId = folderId;
    }

    public String getFolderName() {
        return folderName;
    }

    public void setFolderName(String folderName) {
        this.folderName = ResourcesUtil.getString(folderName);
    }

    public String getOltName() {
        return oltName;
    }

    public void setOltName(String oltName) {
        this.oltName = (null == oltName || oltName.equals("")) ? NONEVALUE : oltName;
    }

    public String getOltIp() {
        return oltIp;
    }

    public void setOltIp(String oltIp) {
        this.oltIp = (null == oltIp || oltIp.equals("")) ? NONEVALUE : oltIp;
    }

    public Long getPonIndex() {
        return ponIndex;
    }

    public void setPonIndex(Long ponIndex) {
        this.ponIndex = (ponIndex == null) ? 0 : ponIndex;
        // 将PONindex转成格式化的str
        if (ponIndex == null) {
            setPonIndexStr(NONEVALUE);
        } else {
            String hexStr = Long.toHexString(ponIndex);
            String formatStr = hexStr.charAt(0) + "/" + hexStr.charAt(2);
            setPonIndexStr(formatStr);
        }
    }

    public Integer getOnLineCmNum() {
        return onLineCmNum;
    }

    public void setOnLineCmNum(Integer onLineCmNum) {
        this.onLineCmNum = (onLineCmNum == null) ? 0 : onLineCmNum;
    }

    public Integer getOffLineCmNum() {
        return offLineCmNum;
    }

    public void setOffLineCmNum(Integer offLineCmNum) {
        this.offLineCmNum = (offLineCmNum == null) ? 0 : offLineCmNum;
    }

    public Integer getOtherStatusCmNum() {
        return otherStatusCmNum;
    }

    public void setOtherStatusCmNum(Integer otherStatusCmNum) {
        this.otherStatusCmNum = (otherStatusCmNum == null) ? 0 : otherStatusCmNum;
    }

    public Integer getAllStatusCmNum() {
        return allStatusCmNum;
    }

    public void setAllStatusCmNum(Integer allStatusCmNum) {
        this.allStatusCmNum = (allStatusCmNum == null) ? 0 : allStatusCmNum;
    }

    public String getPonIndexStr() {
        return ponIndexStr;
    }

    public void setPonIndexStr(String ponIndexStr) {
        this.ponIndexStr = ponIndexStr;
    }

    @Override
    public String toString() {
        return "CmRealTimeUserStaticReport [cmcId=" + cmcId + ", cmcName=" + cmcName + ", folderId=" + folderId
                + ", folderName=" + folderName + ", oltName=" + oltName + ", oltIp=" + oltIp + ", ponIndex=" + ponIndex
                + ", ponIndexStr=" + ponIndexStr + ", onLineCmNum=" + onLineCmNum + ", offLineCmNum=" + offLineCmNum
                + ", otherStatusCmNum=" + otherStatusCmNum + ", allStatusCmNum=" + allStatusCmNum + "]";
    }

}
