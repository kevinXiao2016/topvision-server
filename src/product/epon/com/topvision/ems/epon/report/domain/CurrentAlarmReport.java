/***********************************************************************
 * $Id: CurrentAlert.java,v1.0 2013-6-8 下午1:25:46 $
 * 
 * @author: Administrator
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.report.domain;

import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author Administrator
 * @created @2013-6-8-下午1:25:46
 * 
 */
public class CurrentAlarmReport implements AliasesSuperType {
    private static final long serialVersionUID = 1054078859742403281L;

    private Long folderId; // 区域ID
    private Long entityId;
    private String folderName; // 区域名称
    private String displayName; // 设备显示类型
    private String entityName; // 设备名称
    private Long allAlarmNum; // 所有告警数量
    private Long emergencyAlarmNum; // 紧急告警数量
    private Long seriousAlarmNum; // 严重告警数量
    private Long mainAlarmNum; // 主要告警数量
    private Long minorAlarmNum; // 次要告警数量
    private Long generalAlarmNum; // 一般告警数量
    private Long messageNum; // 提示信息数量

    public String getFolderName() {
        return folderName;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public Long getAllAlarmNum() {
        return allAlarmNum;
    }

    public void setAllAlarmNum(Long allAlarmNum) {
        this.allAlarmNum = allAlarmNum;
    }

    public Long getEmergencyAlarmNum() {
        return emergencyAlarmNum;
    }

    public void setEmergencyAlarmNum(Long emergencyAlarmNum) {
        this.emergencyAlarmNum = emergencyAlarmNum;
    }

    public Long getSeriousAlarmNum() {
        return seriousAlarmNum;
    }

    public void setSeriousAlarmNum(Long seriousAlarmNum) {
        this.seriousAlarmNum = seriousAlarmNum;
    }

    public Long getMainAlarmNum() {
        return mainAlarmNum;
    }

    public void setMainAlarmNum(Long mainAlarmNum) {
        this.mainAlarmNum = mainAlarmNum;
    }

    public Long getMinorAlarmNum() {
        return minorAlarmNum;
    }

    public void setMinorAlarmNum(Long minorAlarmNum) {
        this.minorAlarmNum = minorAlarmNum;
    }

    public Long getGeneralAlarmNum() {
        return generalAlarmNum;
    }

    public void setGeneralAlarmNum(Long generalAlarmNum) {
        this.generalAlarmNum = generalAlarmNum;
    }

    public Long getMessageNum() {
        return messageNum;
    }

    public void setMessageNum(Long messageNum) {
        this.messageNum = messageNum;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public Long getFolderId() {
        return folderId;
    }

    public void setFolderId(Long folderId) {
        this.folderId = folderId;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("CurrentAlarmReport [folderId=");
        builder.append(folderId);
        builder.append(", entityId=");
        builder.append(entityId);
        builder.append(", folderName=");
        builder.append(folderName);
        builder.append(", displayName=");
        builder.append(displayName);
        builder.append(", entityName=");
        builder.append(entityName);
        builder.append(", allAlarmNum=");
        builder.append(allAlarmNum);
        builder.append(", emergencyAlarmNum=");
        builder.append(emergencyAlarmNum);
        builder.append(", seriousAlarmNum=");
        builder.append(seriousAlarmNum);
        builder.append(", mainAlarmNum=");
        builder.append(mainAlarmNum);
        builder.append(", minorAlarmNum=");
        builder.append(minorAlarmNum);
        builder.append(", generalAlarmNum=");
        builder.append(generalAlarmNum);
        builder.append(", messageNum=");
        builder.append(messageNum);
        builder.append("]");
        return builder.toString();
    }

}
