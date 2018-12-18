/***********************************************************************
 * $Id: IgmpCtcProfile.java,v1.0 2016-6-7 下午4:10:31 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.igmpconfig.domain;

import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author flack
 * @created @2016-6-7-下午4:10:31
 * CTC组播模板配置
 */
public class IgmpCtcProfile implements AliasesSuperType {
    private static final long serialVersionUID = -364715763227598025L;

    private Long entityId;
    //CTC模板ID
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.6.1.5.2.1.1", index = true)
    private Integer profileId;
    //CTC模板描述
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.6.1.5.2.1.2", writable = true, type = "OctetString")
    private String profileDesc;
    //CTC模板权限
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.6.1.5.2.1.3", writable = true, type = "Integer32")
    private Integer profileAuth;
    //CTC模板每次预览最大时长
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.6.1.5.2.1.4", writable = true, type = "Integer32")
    private Integer previewTime;
    //CTC模板两次预览时间间隔
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.6.1.5.2.1.5", writable = true, type = "Integer32")
    private Integer previewInterval;
    //CTC模板允许预览次数
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.6.1.5.2.1.6", writable = true, type = "Integer32")
    private Integer previewCount;
    //行创建
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.6.1.5.2.1.7", writable = true, type = "Integer32")
    private Integer rowStatus;

    //模板别名,支持中文,由网管维护
    private String profileName;

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public Integer getProfileId() {
        return profileId;
    }

    public void setProfileId(Integer profileId) {
        this.profileId = profileId;
    }

    public String getProfileDesc() {
        return profileDesc;
    }

    public void setProfileDesc(String profileDesc) {
        this.profileDesc = profileDesc;
    }

    public Integer getProfileAuth() {
        return profileAuth;
    }

    public void setProfileAuth(Integer profileAuth) {
        this.profileAuth = profileAuth;
    }

    public Integer getPreviewTime() {
        return previewTime;
    }

    public void setPreviewTime(Integer previewTime) {
        this.previewTime = previewTime;
    }

    public Integer getPreviewInterval() {
        return previewInterval;
    }

    public void setPreviewInterval(Integer previewInterval) {
        this.previewInterval = previewInterval;
    }

    public Integer getPreviewCount() {
        return previewCount;
    }

    public void setPreviewCount(Integer previewCount) {
        this.previewCount = previewCount;
    }

    public Integer getRowStatus() {
        return rowStatus;
    }

    public void setRowStatus(Integer rowStatus) {
        this.rowStatus = rowStatus;
    }

    public String getProfileName() {
        return profileName;
    }

    public void setProfileName(String profileName) {
        this.profileName = profileName;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("IgmpCtcProfile [entityId=");
        builder.append(entityId);
        builder.append(", profileId=");
        builder.append(profileId);
        builder.append(", profileDesc=");
        builder.append(profileDesc);
        builder.append(", profileAuth=");
        builder.append(profileAuth);
        builder.append(", previewTime=");
        builder.append(previewTime);
        builder.append(", previewInterval=");
        builder.append(previewInterval);
        builder.append(", previewCount=");
        builder.append(previewCount);
        builder.append(", rowStatus=");
        builder.append(rowStatus);
        builder.append(", profileName=");
        builder.append(profileName);
        builder.append("]");
        return builder.toString();
    }

}
