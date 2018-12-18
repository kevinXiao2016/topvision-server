/***********************************************************************
 * $Id: GponLineProfileInfo.java,v1.0 2016年10月24日 下午5:53:02 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.gpon.profile.facade.domain;

import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author Bravin
 * @created @2016年10月24日-下午5:53:02
 *
 */
public class GponLineProfileInfo implements AliasesSuperType {
    private static final long serialVersionUID = 5883210154667367934L;
    private Long entityId;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.8.11.2.1.1.1", index = true, type = "Integer32")
    private Integer gponLineProfileId;// 1-1024
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.8.11.2.1.1.2", writable = true, type = "OctetString")
    private String gponLineProfileName;// 1-31
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.8.11.2.1.1.3", writable = true, type = "Integer32")
    private Integer gponLineProfileUpstreamFECMode;// 1：on 2：off
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.8.11.2.1.1.4", writable = true, type = "Integer32")
    private Integer gponLineProfileMappingMode;// 1：vlan 2：priority 3：vlan+priority 4：port
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.8.11.2.1.1.5", writable = false, type = "Integer32")
    private Integer gponLineProfileTcontNum;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.8.11.2.1.1.6", writable = false, type = "Integer32")
    private Integer gponLineProfileGemNum;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.8.11.2.1.1.7", writable = false, type = "Integer32")
    private Integer gponLineProfileBindNum;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.8.11.2.1.1.8", writable = true, type = "Integer32")
    private Integer gponLineProfileRowStatus;

    private Integer gemMapNum;

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public Integer getGponLineProfileId() {
        return gponLineProfileId;
    }

    public void setGponLineProfileId(Integer gponLineProfileId) {
        this.gponLineProfileId = gponLineProfileId;
    }

    public String getGponLineProfileName() {
        return gponLineProfileName;
    }

    public void setGponLineProfileName(String gponLineProfileName) {
        this.gponLineProfileName = gponLineProfileName;
    }

    public Integer getGponLineProfileUpstreamFECMode() {
        return gponLineProfileUpstreamFECMode;
    }

    public void setGponLineProfileUpstreamFECMode(Integer gponLineProfileUpstreamFECMode) {
        this.gponLineProfileUpstreamFECMode = gponLineProfileUpstreamFECMode;
    }

    public Integer getGponLineProfileMappingMode() {
        return gponLineProfileMappingMode;
    }

    public void setGponLineProfileMappingMode(Integer gponLineProfileMappingMode) {
        this.gponLineProfileMappingMode = gponLineProfileMappingMode;
    }

    public Integer getGponLineProfileTcontNum() {
        return gponLineProfileTcontNum;
    }

    public void setGponLineProfileTcontNum(Integer gponLineProfileTcontNum) {
        this.gponLineProfileTcontNum = gponLineProfileTcontNum;
    }

    public Integer getGponLineProfileGemNum() {
        return gponLineProfileGemNum;
    }

    public void setGponLineProfileGemNum(Integer gponLineProfileGemNum) {
        this.gponLineProfileGemNum = gponLineProfileGemNum;
    }

    public Integer getGponLineProfileBindNum() {
        return gponLineProfileBindNum;
    }

    public void setGponLineProfileBindNum(Integer gponLineProfileBindNum) {
        this.gponLineProfileBindNum = gponLineProfileBindNum;
    }

    public Integer getGponLineProfileRowStatus() {
        return gponLineProfileRowStatus;
    }

    public void setGponLineProfileRowStatus(Integer gponLineProfileRowStatus) {
        this.gponLineProfileRowStatus = gponLineProfileRowStatus;
    }

    public Integer getGemMapNum() {
        return gemMapNum;
    }

    public void setGemMapNum(Integer gemMapNum) {
        this.gemMapNum = gemMapNum;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("GponLineProfileInfo [entityId=");
        builder.append(entityId);
        builder.append(", gponLineProfileId=");
        builder.append(gponLineProfileId);
        builder.append(", gponLineProfileName=");
        builder.append(gponLineProfileName);
        builder.append(", gponLineProfileUpstreamFECMode=");
        builder.append(gponLineProfileUpstreamFECMode);
        builder.append(", gponLineProfileMappingMode=");
        builder.append(gponLineProfileMappingMode);
        builder.append(", gponLineProfileTcontNum=");
        builder.append(gponLineProfileTcontNum);
        builder.append(", gponLineProfileGemNum=");
        builder.append(gponLineProfileGemNum);
        builder.append(", gponLineProfileBindNum=");
        builder.append(gponLineProfileBindNum);
        builder.append(", gponLineProfileRowStatus=");
        builder.append(gponLineProfileRowStatus);
        builder.append(", gemMapNum=");
        builder.append(gemMapNum);
        builder.append("]");
        return builder.toString();
    }

}
