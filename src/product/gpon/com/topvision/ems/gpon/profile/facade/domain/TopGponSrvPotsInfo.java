/***********************************************************************
 * $Id: TopGponSrvPotsInfo.java,v1.0 2017年6月17日 上午10:28:57 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.gpon.profile.facade.domain;

import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author haojie
 * @created @2017年6月17日-上午10:28:57
 *
 */
public class TopGponSrvPotsInfo implements AliasesSuperType {
    private static final long serialVersionUID = -639237574270374524L;
    private Long entityId;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.5.3.2.1.1", index = true, type = "Integer32")
    private Integer topGponSrvPotsInfoProfIdx;// 1-1024 GPON 业务模板ID
    private String profileName;//GPON业务模板名称
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.5.3.2.1.2", index = true, type = "Integer32")
    private Integer topGponSrvPotsInfoPotsIdx;// GPON Pots接口索引

    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.5.3.2.1.3", writable = true, type = "Integer32")
    private Integer topGponSrvPotsInfoSIPAgtId;// 0-64 绑定的SIP代理模板ID
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.5.3.2.1.4", writable = true, type = "Integer32")
    private Integer topGponSrvPotsInfoVoipMediaId;// 0-64 绑定的Voip media模板ID
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.5.3.2.1.5", writable = true, type = "Integer32")
    private Integer topGponSrvPotsInfoIpIdx;// 0-64 关联IP接口索引

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public Integer getTopGponSrvPotsInfoProfIdx() {
        return topGponSrvPotsInfoProfIdx;
    }

    public void setTopGponSrvPotsInfoProfIdx(Integer topGponSrvPotsInfoProfIdx) {
        this.topGponSrvPotsInfoProfIdx = topGponSrvPotsInfoProfIdx;
    }

    public String getProfileName() {
        return profileName;
    }

    public void setProfileName(String profileName) {
        this.profileName = profileName;
    }

    public Integer getTopGponSrvPotsInfoPotsIdx() {
        return topGponSrvPotsInfoPotsIdx;
    }

    public void setTopGponSrvPotsInfoPotsIdx(Integer topGponSrvPotsInfoPotsIdx) {
        this.topGponSrvPotsInfoPotsIdx = topGponSrvPotsInfoPotsIdx;
    }

    public Integer getTopGponSrvPotsInfoSIPAgtId() {
        return topGponSrvPotsInfoSIPAgtId;
    }

    public void setTopGponSrvPotsInfoSIPAgtId(Integer topGponSrvPotsInfoSIPAgtId) {
        this.topGponSrvPotsInfoSIPAgtId = topGponSrvPotsInfoSIPAgtId;
    }

    public Integer getTopGponSrvPotsInfoVoipMediaId() {
        return topGponSrvPotsInfoVoipMediaId;
    }

    public void setTopGponSrvPotsInfoVoipMediaId(Integer topGponSrvPotsInfoVoipMediaId) {
        this.topGponSrvPotsInfoVoipMediaId = topGponSrvPotsInfoVoipMediaId;
    }

    public Integer getTopGponSrvPotsInfoIpIdx() {
        return topGponSrvPotsInfoIpIdx;
    }

    public void setTopGponSrvPotsInfoIpIdx(Integer topGponSrvPotsInfoIpIdx) {
        this.topGponSrvPotsInfoIpIdx = topGponSrvPotsInfoIpIdx;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("TopGponSrvPotsInfo [entityId=");
        builder.append(entityId);
        builder.append(", topGponSrvPotsInfoProfIdx=");
        builder.append(topGponSrvPotsInfoProfIdx);
        builder.append(", profileName=");
        builder.append(profileName);
        builder.append(", topGponSrvPotsInfoPotsIdx=");
        builder.append(topGponSrvPotsInfoPotsIdx);
        builder.append(", topGponSrvPotsInfoSIPAgtId=");
        builder.append(topGponSrvPotsInfoSIPAgtId);
        builder.append(", topGponSrvPotsInfoVoipMediaId=");
        builder.append(topGponSrvPotsInfoVoipMediaId);
        builder.append(", topGponSrvPotsInfoIpIdx=");
        builder.append(topGponSrvPotsInfoIpIdx);
        builder.append("]");
        return builder.toString();
    }

}
