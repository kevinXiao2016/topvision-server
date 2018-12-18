/***********************************************************************
 * $Id: GponSrvProfileCfg.java,v1.0 2016年10月24日 下午6:19:11 $
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
 * @created @2016年10月24日-下午6:19:11
 *
 */
public class GponSrvProfileCfg implements AliasesSuperType {
    private static final long serialVersionUID = -8378543760781660421L;

    private Long entityId;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.8.11.3.2.1.1", index = true, type = "Integer32")
    private Integer gponSrvProfileIndex;// 1-1024
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.8.11.3.2.1.2", writable = true, type = "Integer32")
    private Integer gponSrvProfileMacLearning;// 0:unconcern 1:enable 2:disable
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.8.11.3.2.1.3", writable = true, type = "Integer32")
    private Integer gponSrvProfileMacAgeSeconds;// 0:unconcern -1:unlimited 10-1000000:实际老化时间 second
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.8.11.3.2.1.4", writable = true, type = "Integer32")
    private Integer gponSrvProfileLoopbackDetectCheck;// 0:unconcern 1:enable 2:disable
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.8.11.3.2.1.5", writable = true, type = "Integer32")
    private Integer gponSrvProfileMcMode;// 0:unconcern 1:igmp snooping 2:olt-control
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.8.11.3.2.1.6", writable = true, type = "Integer32")
    private Integer gponSrvProfileMcFastLeave;// 0:unconcern 1:enable 2:disable
    // @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.8.11.3.2.1.7", writable = true, type = "Integer32")
    private Integer gponSrvProfileUpIgmpFwdMode;// 0:unconcern 1:translation 2:vlan 3:transparent
    // @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.8.11.3.2.1.8", writable = true, type = "Integer32")
    private Integer gponSrvProfileUpIgmpTCI;// 0：unconcern 0-15bit:vlan 16-31bit:pri

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public Integer getGponSrvProfileIndex() {
        return gponSrvProfileIndex;
    }

    public void setGponSrvProfileIndex(Integer gponSrvProfileIndex) {
        this.gponSrvProfileIndex = gponSrvProfileIndex;
    }

    public Integer getGponSrvProfileMacLearning() {
        return gponSrvProfileMacLearning;
    }

    public void setGponSrvProfileMacLearning(Integer gponSrvProfileMacLearning) {
        this.gponSrvProfileMacLearning = gponSrvProfileMacLearning;
    }

    public Integer getGponSrvProfileMacAgeSeconds() {
        return gponSrvProfileMacAgeSeconds;
    }

    public void setGponSrvProfileMacAgeSeconds(Integer gponSrvProfileMacAgeSeconds) {
        this.gponSrvProfileMacAgeSeconds = gponSrvProfileMacAgeSeconds;
    }

    public Integer getGponSrvProfileLoopbackDetectCheck() {
        return gponSrvProfileLoopbackDetectCheck;
    }

    public void setGponSrvProfileLoopbackDetectCheck(Integer gponSrvProfileLoopbackDetectCheck) {
        this.gponSrvProfileLoopbackDetectCheck = gponSrvProfileLoopbackDetectCheck;
    }

    public Integer getGponSrvProfileMcMode() {
        return gponSrvProfileMcMode;
    }

    public void setGponSrvProfileMcMode(Integer gponSrvProfileMcMode) {
        this.gponSrvProfileMcMode = gponSrvProfileMcMode;
    }

    public Integer getGponSrvProfileMcFastLeave() {
        return gponSrvProfileMcFastLeave;
    }

    public void setGponSrvProfileMcFastLeave(Integer gponSrvProfileMcFastLeave) {
        this.gponSrvProfileMcFastLeave = gponSrvProfileMcFastLeave;
    }

    public Integer getGponSrvProfileUpIgmpFwdMode() {
        return gponSrvProfileUpIgmpFwdMode;
    }

    public void setGponSrvProfileUpIgmpFwdMode(Integer gponSrvProfileUpIgmpFwdMode) {
        this.gponSrvProfileUpIgmpFwdMode = gponSrvProfileUpIgmpFwdMode;
    }

    public Integer getGponSrvProfileUpIgmpTCI() {
        return gponSrvProfileUpIgmpTCI;
    }

    public void setGponSrvProfileUpIgmpTCI(Integer gponSrvProfileUpIgmpTCI) {
        this.gponSrvProfileUpIgmpTCI = gponSrvProfileUpIgmpTCI;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("GponSrvProfileCfg [entityId=");
        builder.append(entityId);
        builder.append(", gponSrvProfileIndex=");
        builder.append(gponSrvProfileIndex);
        builder.append(", gponSrvProfileMacLearning=");
        builder.append(gponSrvProfileMacLearning);
        builder.append(", gponSrvProfileMacAgeSeconds=");
        builder.append(gponSrvProfileMacAgeSeconds);
        builder.append(", gponSrvProfileLoopbackDetectCheck=");
        builder.append(gponSrvProfileLoopbackDetectCheck);
        builder.append(", gponSrvProfileMcMode=");
        builder.append(gponSrvProfileMcMode);
        builder.append(", gponSrvProfileMcFastLeave=");
        builder.append(gponSrvProfileMcFastLeave);
        builder.append(", gponSrvProfileUpIgmpFwdMode=");
        builder.append(gponSrvProfileUpIgmpFwdMode);
        builder.append(", gponSrvProfileUpIgmpTCI=");
        builder.append(gponSrvProfileUpIgmpTCI);
        builder.append("]");
        return builder.toString();
    }

}
