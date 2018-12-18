/***********************************************************************
 * $Id: GponTrafficProfileInfo.java,v1.0 2016年10月24日 下午6:13:04 $
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
 * @created @2016年10月24日-下午6:13:04
 *
 */
public class GponTrafficProfileInfo implements AliasesSuperType {
    private static final long serialVersionUID = 3849399514307740675L;

    private Long entityId;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.8.11.4.1.1.1", index = true, type = "Integer32")
    private Integer gponTrafficProfileId;// 1-1024
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.8.11.4.1.1.2", writable = true, type = "OctetString")
    private String gponTrafficProfileName;// 1-31字节
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.8.11.4.1.1.3", writable = true, type = "Integer32")
    private Integer gponTrafficProfileCfgCir;// 64-10240000 kbit/s
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.8.11.4.1.1.4", writable = true, type = "Integer32")
    private Integer gponTrafficProfileCfgPir;// 64-10240000 kbit/s
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.8.11.4.1.1.5", writable = true, type = "Integer32")
    private Integer gponTrafficProfileCfgCbs;// 2000-1024000000 byte
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.8.11.4.1.1.6", writable = true, type = "Integer32")
    private Integer gponTrafficProfileCfgPbs;// 2000-1024000000 byte
    // @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.8.11.4.1.1.7", writable = true, type = "Integer32")
    private Integer gponTrafficProfileCfgPriority;// 0-7
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.8.11.4.1.1.8", writable = false, type = "Integer32")
    private Integer gponTrafficProfileBindNum;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.8.11.4.1.1.9", writable = true, type = "Integer32")
    private Integer gponTrafficProfileRowStatus;

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public Integer getGponTrafficProfileId() {
        return gponTrafficProfileId;
    }

    public void setGponTrafficProfileId(Integer gponTrafficProfileId) {
        this.gponTrafficProfileId = gponTrafficProfileId;
    }

    public String getGponTrafficProfileName() {
        return gponTrafficProfileName;
    }

    public void setGponTrafficProfileName(String gponTrafficProfileName) {
        this.gponTrafficProfileName = gponTrafficProfileName;
    }

    public Integer getGponTrafficProfileCfgCir() {
        return gponTrafficProfileCfgCir;
    }

    public void setGponTrafficProfileCfgCir(Integer gponTrafficProfileCfgCir) {
        this.gponTrafficProfileCfgCir = gponTrafficProfileCfgCir;
    }

    public Integer getGponTrafficProfileCfgPir() {
        return gponTrafficProfileCfgPir;
    }

    public void setGponTrafficProfileCfgPir(Integer gponTrafficProfileCfgPir) {
        this.gponTrafficProfileCfgPir = gponTrafficProfileCfgPir;
    }

    public Integer getGponTrafficProfileCfgCbs() {
        return gponTrafficProfileCfgCbs;
    }

    public void setGponTrafficProfileCfgCbs(Integer gponTrafficProfileCfgCbs) {
        this.gponTrafficProfileCfgCbs = gponTrafficProfileCfgCbs;
    }

    public Integer getGponTrafficProfileCfgPbs() {
        return gponTrafficProfileCfgPbs;
    }

    public void setGponTrafficProfileCfgPbs(Integer gponTrafficProfileCfgPbs) {
        this.gponTrafficProfileCfgPbs = gponTrafficProfileCfgPbs;
    }

    public Integer getGponTrafficProfileCfgPriority() {
        return gponTrafficProfileCfgPriority;
    }

    public void setGponTrafficProfileCfgPriority(Integer gponTrafficProfileCfgPriority) {
        this.gponTrafficProfileCfgPriority = gponTrafficProfileCfgPriority;
    }

    public Integer getGponTrafficProfileBindNum() {
        return gponTrafficProfileBindNum;
    }

    public void setGponTrafficProfileBindNum(Integer gponTrafficProfileBindNum) {
        this.gponTrafficProfileBindNum = gponTrafficProfileBindNum;
    }

    public Integer getGponTrafficProfileRowStatus() {
        return gponTrafficProfileRowStatus;
    }

    public void setGponTrafficProfileRowStatus(Integer gponTrafficProfileRowStatus) {
        this.gponTrafficProfileRowStatus = gponTrafficProfileRowStatus;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("GponTrafficProfileInfo [entityId=");
        builder.append(entityId);
        builder.append(", gponTrafficProfileId=");
        builder.append(gponTrafficProfileId);
        builder.append(", gponTrafficProfileName=");
        builder.append(gponTrafficProfileName);
        builder.append(", gponTrafficProfileCfgCir=");
        builder.append(gponTrafficProfileCfgCir);
        builder.append(", gponTrafficProfileCfgPir=");
        builder.append(gponTrafficProfileCfgPir);
        builder.append(", gponTrafficProfileCfgCbs=");
        builder.append(gponTrafficProfileCfgCbs);
        builder.append(", gponTrafficProfileCfgPbs=");
        builder.append(gponTrafficProfileCfgPbs);
        builder.append(", gponTrafficProfileCfgPriority=");
        builder.append(gponTrafficProfileCfgPriority);
        builder.append(", gponTrafficProfileBindNum=");
        builder.append(gponTrafficProfileBindNum);
        builder.append(", gponTrafficProfileRowStatus=");
        builder.append(gponTrafficProfileRowStatus);
        builder.append("]");
        return builder.toString();
    }

}
