/***********************************************************************
 * $Id: GponDbaProfileInfo.java,v1.0 2016年10月24日 下午5:48:02 $
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
 * @created @2016年10月24日-下午5:48:02
 *
 */
public class GponDbaProfileInfo implements AliasesSuperType {
    private static final long serialVersionUID = 367998655580658310L;

    private Long entityId;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.8.11.1.1.1.1", index = true)
    private Integer gponDbaProfileId;// 1-256
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.8.11.1.1.1.2", writable = true, type = "OctetString")
    private String gponDbaProfileName;// 1-31
    
     //that means template isn't setted when DBA template is Type0, can't use
     //only 'fixed bandwidth' attribute can be setted when DBA template is Type1
     //only 'Assured Bandwidth' attribute can be setted when DBA template is Type2
     //only 'fixed bandwidth' and 'Max Bandwidth' attribute can be setted when DBA template is Type3
     //only 'Max Bandwidth' attribute can be setted when DBA template is Type4
     //'fixed bandwidth' 'Assured Bandwidth' and 'Max Bandwidth' attribute can be setted when DBA template is Type5
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.8.11.1.1.1.3", writable = true, type = "Integer32")
    private Integer gponDbaProfileType;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.8.11.1.1.1.4", writable = true, type = "Integer32")
    private Integer gponDbaProfileFixRate;// 512-10240000 kbit/s
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.8.11.1.1.1.5", writable = true, type = "Integer32")
    private Integer gponDbaProfileAssureRate;// 512-10240000 kbit/s
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.8.11.1.1.1.6", writable = true, type = "Integer32")
    private Integer gponDbaProfileMaxRate;// 512-10240000 kbit/s
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.8.11.1.1.1.7", writable = false, type = "Integer32")
    private Integer gponDbaProfileBindNum;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.8.11.1.1.1.8", writable = true, type = "Integer32")
    private Integer gponDbaProfileRowStatus;

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public Integer getGponDbaProfileId() {
        return gponDbaProfileId;
    }

    public void setGponDbaProfileId(Integer gponDbaProfileId) {
        this.gponDbaProfileId = gponDbaProfileId;
    }

    public Integer getGponDbaProfileType() {
        return gponDbaProfileType;
    }

    public void setGponDbaProfileType(Integer gponDbaProfileType) {
        this.gponDbaProfileType = gponDbaProfileType;
    }

    public String getGponDbaProfileName() {
        return gponDbaProfileName;
    }

    public void setGponDbaProfileName(String gponDbaProfileName) {
        this.gponDbaProfileName = gponDbaProfileName;
    }

    public Integer getGponDbaProfileAssureRate() {
        return gponDbaProfileAssureRate;
    }

    public void setGponDbaProfileAssureRate(Integer gponDbaProfileAssureRate) {
        this.gponDbaProfileAssureRate = gponDbaProfileAssureRate;
    }

    public Integer getGponDbaProfileFixRate() {
        return gponDbaProfileFixRate;
    }

    public void setGponDbaProfileFixRate(Integer gponDbaProfileFixRate) {
        this.gponDbaProfileFixRate = gponDbaProfileFixRate;
    }

    public Integer getGponDbaProfileMaxRate() {
        return gponDbaProfileMaxRate;
    }

    public void setGponDbaProfileMaxRate(Integer gponDbaProfileMaxRate) {
        this.gponDbaProfileMaxRate = gponDbaProfileMaxRate;
    }

    public Integer getGponDbaProfileBindNum() {
        return gponDbaProfileBindNum;
    }

    public void setGponDbaProfileBindNum(Integer gponDbaProfileBindNum) {
        this.gponDbaProfileBindNum = gponDbaProfileBindNum;
    }

    public Integer getGponDbaProfileRowStatus() {
        return gponDbaProfileRowStatus;
    }

    public void setGponDbaProfileRowStatus(Integer gponDbaProfileRowStatus) {
        this.gponDbaProfileRowStatus = gponDbaProfileRowStatus;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("GponDbaProfileInfo [entityId=");
        builder.append(entityId);
        builder.append(", gponDbaProfileId=");
        builder.append(gponDbaProfileId);
        builder.append(", gponDbaProfileName=");
        builder.append(gponDbaProfileName);
        builder.append(", gponDbaProfileType=");
        builder.append(gponDbaProfileType);
        builder.append(", gponDbaProfileAssureRate=");
        builder.append(gponDbaProfileAssureRate);
        builder.append(", gponDbaProfileFixRate=");
        builder.append(gponDbaProfileFixRate);
        builder.append(", gponDbaProfileMaxRate=");
        builder.append(gponDbaProfileMaxRate);
        builder.append(", gponDbaProfileBindNum=");
        builder.append(gponDbaProfileBindNum);
        builder.append(", gponDbaProfileRowStatus=");
        builder.append(gponDbaProfileRowStatus);
        builder.append("]");
        return builder.toString();
    }

}
