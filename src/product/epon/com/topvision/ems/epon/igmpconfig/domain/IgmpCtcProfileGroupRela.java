/***********************************************************************
 * $Id: IgmpCtcProfileGroupRela.java,v1.0 2016-6-7 下午4:26:48 $
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
 * @created @2016-6-7-下午4:26:48
 * CTC模板绑定组播组
 */
public class IgmpCtcProfileGroupRela implements AliasesSuperType {
    private static final long serialVersionUID = -9017107083809352212L;

    private Long entityId;
    //CTC模板ID
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.6.1.5.3.1.1", index = true)
    private Integer profileId;
    //组播组ID
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.6.1.5.3.1.2", index = true)
    private Integer groupId;
    //行创建
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.6.1.5.3.1.3", writable = true, type = "Integer32")
    private Integer rowStatus;

    private String profileDesc;
    private String groupDesc;

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

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    public Integer getRowStatus() {
        return rowStatus;
    }

    public void setRowStatus(Integer rowStatus) {
        this.rowStatus = rowStatus;
    }

    public String getProfileDesc() {
        return profileDesc;
    }

    public void setProfileDesc(String profileDesc) {
        this.profileDesc = profileDesc;
    }

    public String getGroupDesc() {
        return groupDesc;
    }

    public void setGroupDesc(String groupDesc) {
        this.groupDesc = groupDesc;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("IgmpCtcProfileGroupRela [entityId=");
        builder.append(entityId);
        builder.append(", profileId=");
        builder.append(profileId);
        builder.append(", groupId=");
        builder.append(groupId);
        builder.append(", rowStatus=");
        builder.append(rowStatus);
        builder.append("]");
        return builder.toString();
    }

}
