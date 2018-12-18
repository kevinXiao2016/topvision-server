/***********************************************************************
 * $Id: GponSrvProfileInfo.java,v1.0 2016年10月24日 下午6:16:43 $
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
 * @created @2016年10月24日-下午6:16:43
 *
 */
public class GponSrvProfileInfo implements AliasesSuperType {
    private static final long serialVersionUID = 6000792273449103948L;

    private Long entityId;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.8.11.3.1.1.1", index = true, type = "Integer32")
    private Integer gponSrvProfileId;// 1-1024
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.8.11.3.1.1.2", writable = true, type = "OctetString")
    private String gponSrvProfileName;// 1-31字节
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.8.11.3.1.1.3", writable = false, type = "Integer32")
    private Integer gponSrvProfileBindNum;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.8.11.3.1.1.4", writable = true, type = "Integer32")
    private Integer gponSrvProfileRowStatus;

    // 基本配置
    private Integer gponSrvProfileMacLearning;// 0:unconcern 1:enable 2:disable
    private Integer gponSrvProfileMacAgeSeconds;// 0:unconcern -1:unlimited 10-1000000:实际老化时间 second
    private Integer gponSrvProfileLoopbackDetectCheck;// 0:unconcern 1:enable 2:disable
    private Integer gponSrvProfileMcMode;// 0:unconcern 1:igmp snooping 2:olt-control
    private Integer gponSrvProfileMcFastLeave;// 0:unconcern 1:enable 2:disable
    private Integer gponSrvProfileUpIgmpFwdMode;// 0:unconcern 1:translation 2:vlan 3:transparent
    private Integer gponSrvProfileUpIgmpTCI;// 0：unconcern 0-15bit:vlan 16-31bit:pri
    // 端口配置
    private Integer gponSrvProfileEthNum;// -1:adaptive 0-24:端口数
    private Integer gponSrvProfileCatvNum;// -1:adaptive 0-2:端口数
    private Integer gponSrvProfileWlanNum;// -1:adaptive 0-2:端口数
    private Integer gponSrvProfileVeipNum;// -1:adaptive 0-8:端口数 
    private Integer topGponSrvProfilePotsNum;// -1:adaptive 0-2:端口数

    // 此三项已经移入了POTS口模板中，针对每个POTS口有一条配置
    private Integer topGponSrvProfileSIPAgtId;
    private Integer topGponSrvProfileIpIdx;
    private Integer topGponSrvProfileMgAttriId;

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public Integer getGponSrvProfileId() {
        return gponSrvProfileId;
    }

    public void setGponSrvProfileId(Integer gponSrvProfileId) {
        this.gponSrvProfileId = gponSrvProfileId;
    }

    public String getGponSrvProfileName() {
        return gponSrvProfileName;
    }

    public void setGponSrvProfileName(String gponSrvProfileName) {
        this.gponSrvProfileName = gponSrvProfileName;
    }

    public Integer getGponSrvProfileBindNum() {
        return gponSrvProfileBindNum;
    }

    public void setGponSrvProfileBindNum(Integer gponSrvProfileBindNum) {
        this.gponSrvProfileBindNum = gponSrvProfileBindNum;
    }

    public Integer getGponSrvProfileRowStatus() {
        return gponSrvProfileRowStatus;
    }

    public void setGponSrvProfileRowStatus(Integer gponSrvProfileRowStatus) {
        this.gponSrvProfileRowStatus = gponSrvProfileRowStatus;
    }

    /**
     * @return the gponSrvProfileEthNum
     */
    public Integer getGponSrvProfileEthNum() {
        return gponSrvProfileEthNum;
    }

    /**
     * @param gponSrvProfileEthNum
     *            the gponSrvProfileEthNum to set
     */
    public void setGponSrvProfileEthNum(Integer gponSrvProfileEthNum) {
        this.gponSrvProfileEthNum = gponSrvProfileEthNum;
    }

    /**
     * @return the gponSrvProfileCatvNum
     */
    public Integer getGponSrvProfileCatvNum() {
        return gponSrvProfileCatvNum;
    }

    /**
     * @param gponSrvProfileCatvNum
     *            the gponSrvProfileCatvNum to set
     */
    public void setGponSrvProfileCatvNum(Integer gponSrvProfileCatvNum) {
        this.gponSrvProfileCatvNum = gponSrvProfileCatvNum;
    }

    /**
     * @return the gponSrvProfileWlanNum
     */
    public Integer getGponSrvProfileWlanNum() {
        return gponSrvProfileWlanNum;
    }

    /**
     * @param gponSrvProfileWlanNum
     *            the gponSrvProfileWlanNum to set
     */
    public void setGponSrvProfileWlanNum(Integer gponSrvProfileWlanNum) {
        this.gponSrvProfileWlanNum = gponSrvProfileWlanNum;
    }

    /**
     * @return the gponSrvProfileVeipNum
     */
    public Integer getGponSrvProfileVeipNum() {
        return gponSrvProfileVeipNum;
    }

    /**
     * @param gponSrvProfileVeipNum
     *            the gponSrvProfileVeipNum to set
     */
    public void setGponSrvProfileVeipNum(Integer gponSrvProfileVeipNum) {
        this.gponSrvProfileVeipNum = gponSrvProfileVeipNum;
    }

    /**
     * @return the gponSrvProfileMacLearning
     */
    public Integer getGponSrvProfileMacLearning() {
        return gponSrvProfileMacLearning;
    }

    /**
     * @param gponSrvProfileMacLearning
     *            the gponSrvProfileMacLearning to set
     */
    public void setGponSrvProfileMacLearning(Integer gponSrvProfileMacLearning) {
        this.gponSrvProfileMacLearning = gponSrvProfileMacLearning;
    }

    /**
     * @return the gponSrvProfileMacAgeSeconds
     */
    public Integer getGponSrvProfileMacAgeSeconds() {
        return gponSrvProfileMacAgeSeconds;
    }

    /**
     * @param gponSrvProfileMacAgeSeconds
     *            the gponSrvProfileMacAgeSeconds to set
     */
    public void setGponSrvProfileMacAgeSeconds(Integer gponSrvProfileMacAgeSeconds) {
        this.gponSrvProfileMacAgeSeconds = gponSrvProfileMacAgeSeconds;
    }

    /**
     * @return the gponSrvProfileLoopbackDetectCheck
     */
    public Integer getGponSrvProfileLoopbackDetectCheck() {
        return gponSrvProfileLoopbackDetectCheck;
    }

    /**
     * @param gponSrvProfileLoopbackDetectCheck
     *            the gponSrvProfileLoopbackDetectCheck to set
     */
    public void setGponSrvProfileLoopbackDetectCheck(Integer gponSrvProfileLoopbackDetectCheck) {
        this.gponSrvProfileLoopbackDetectCheck = gponSrvProfileLoopbackDetectCheck;
    }

    /**
     * @return the gponSrvProfileMcMode
     */
    public Integer getGponSrvProfileMcMode() {
        return gponSrvProfileMcMode;
    }

    /**
     * @param gponSrvProfileMcMode
     *            the gponSrvProfileMcMode to set
     */
    public void setGponSrvProfileMcMode(Integer gponSrvProfileMcMode) {
        this.gponSrvProfileMcMode = gponSrvProfileMcMode;
    }

    /**
     * @return the gponSrvProfileMcFastLeave
     */
    public Integer getGponSrvProfileMcFastLeave() {
        return gponSrvProfileMcFastLeave;
    }

    /**
     * @param gponSrvProfileMcFastLeave
     *            the gponSrvProfileMcFastLeave to set
     */
    public void setGponSrvProfileMcFastLeave(Integer gponSrvProfileMcFastLeave) {
        this.gponSrvProfileMcFastLeave = gponSrvProfileMcFastLeave;
    }

    /**
     * @return the gponSrvProfileUpIgmpFwdMode
     */
    public Integer getGponSrvProfileUpIgmpFwdMode() {
        return gponSrvProfileUpIgmpFwdMode;
    }

    /**
     * @param gponSrvProfileUpIgmpFwdMode
     *            the gponSrvProfileUpIgmpFwdMode to set
     */
    public void setGponSrvProfileUpIgmpFwdMode(Integer gponSrvProfileUpIgmpFwdMode) {
        this.gponSrvProfileUpIgmpFwdMode = gponSrvProfileUpIgmpFwdMode;
    }

    /**
     * @return the gponSrvProfileUpIgmpTCI
     */
    public Integer getGponSrvProfileUpIgmpTCI() {
        return gponSrvProfileUpIgmpTCI;
    }

    /**
     * @param gponSrvProfileUpIgmpTCI
     *            the gponSrvProfileUpIgmpTCI to set
     */
    public void setGponSrvProfileUpIgmpTCI(Integer gponSrvProfileUpIgmpTCI) {
        this.gponSrvProfileUpIgmpTCI = gponSrvProfileUpIgmpTCI;
    }

    public Integer getTopGponSrvProfileSIPAgtId() {
        return topGponSrvProfileSIPAgtId;
    }

    public void setTopGponSrvProfileSIPAgtId(Integer topGponSrvProfileSIPAgtId) {
        this.topGponSrvProfileSIPAgtId = topGponSrvProfileSIPAgtId;
    }

    public Integer getTopGponSrvProfileIpIdx() {
        return topGponSrvProfileIpIdx;
    }

    public void setTopGponSrvProfileIpIdx(Integer topGponSrvProfileIpIdx) {
        this.topGponSrvProfileIpIdx = topGponSrvProfileIpIdx;
    }

    public Integer getTopGponSrvProfileMgAttriId() {
        return topGponSrvProfileMgAttriId;
    }

    public void setTopGponSrvProfileMgAttriId(Integer topGponSrvProfileMgAttriId) {
        this.topGponSrvProfileMgAttriId = topGponSrvProfileMgAttriId;
    }

    public Integer getTopGponSrvProfilePotsNum() {
        return topGponSrvProfilePotsNum;
    }

    public void setTopGponSrvProfilePotsNum(Integer topGponSrvProfilePotsNum) {
        this.topGponSrvProfilePotsNum = topGponSrvProfilePotsNum;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("GponSrvProfileInfo [entityId=");
        builder.append(entityId);
        builder.append(", gponSrvProfileId=");
        builder.append(gponSrvProfileId);
        builder.append(", gponSrvProfileName=");
        builder.append(gponSrvProfileName);
        builder.append(", gponSrvProfileBindNum=");
        builder.append(gponSrvProfileBindNum);
        builder.append(", gponSrvProfileRowStatus=");
        builder.append(gponSrvProfileRowStatus);
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
        builder.append(", gponSrvProfileEthNum=");
        builder.append(gponSrvProfileEthNum);
        builder.append(", gponSrvProfileCatvNum=");
        builder.append(gponSrvProfileCatvNum);
        builder.append(", gponSrvProfileWlanNum=");
        builder.append(gponSrvProfileWlanNum);
        builder.append(", gponSrvProfileVeipNum=");
        builder.append(gponSrvProfileVeipNum);
        builder.append(", topGponSrvProfileSIPAgtId=");
        builder.append(topGponSrvProfileSIPAgtId);
        builder.append(", topGponSrvProfileIpIdx=");
        builder.append(topGponSrvProfileIpIdx);
        builder.append(", topGponSrvProfileMgAttriId=");
        builder.append(topGponSrvProfileMgAttriId);
        builder.append(", topGponSrvProfilePotsNum=");
        builder.append(topGponSrvProfilePotsNum);
        builder.append("]");
        return builder.toString();
    }

}
