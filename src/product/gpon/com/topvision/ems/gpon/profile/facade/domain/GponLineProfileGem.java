/***********************************************************************
 * $Id: GponLineProfileGem.java,v1.0 2016年10月24日 下午6:01:56 $
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
 * @created @2016年10月24日-下午6:01:56
 *
 */
public class GponLineProfileGem implements AliasesSuperType {
    private static final long serialVersionUID = -5681335807001178871L;
    private Long entityId;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.8.11.2.3.1.1", index = true, type = "Integer32")
    private Integer gponLineProfileGemProfileIndex;// 1-1024
    private String profileName;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.8.11.2.3.1.2", index = true, type = "Integer32")
    private Integer gponLineProfileGemIndex;// 1-64
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.8.11.2.3.1.3", writable = true, type = "Integer32")
    private Integer gponLineProfileGemEncrypt;// 0：unconcern 1：enable 2：disable
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.8.11.2.3.1.4", writable = true, type = "Integer32")
    private Integer gponLineProfileGemTcontId;// 1-7 创建gemport时必须携带tcontid
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.8.11.2.3.1.5", writable = true, type = "Integer32")
    private Integer gponLineProfileGemQueuePri;// 0-7
    // @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.8.11.2.3.1.6", writable = true, type = "Integer32")
    private Integer gponLineProfileGemUpCar;// 0-1024 0:unconcern
    // @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.8.11.2.3.1.7", writable = true, type = "Integer32")
    private Integer gponLineProfileGemDownCar;// 0-1024 0:unconcern
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.8.11.2.3.1.8", writable = false, type = "Integer32")
    private Integer gponLineProfileGemMapNum;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.8.11.2.3.1.9", writable = true, type = "Integer32")
    private Integer gponLineProfileGemRowStatus;

    private Integer mappingMode;

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public Integer getGponLineProfileGemProfileIndex() {
        return gponLineProfileGemProfileIndex;
    }

    public void setGponLineProfileGemProfileIndex(Integer gponLineProfileGemProfileIndex) {
        this.gponLineProfileGemProfileIndex = gponLineProfileGemProfileIndex;
    }

    public Integer getGponLineProfileGemIndex() {
        return gponLineProfileGemIndex;
    }

    public void setGponLineProfileGemIndex(Integer gponLineProfileGemIndex) {
        this.gponLineProfileGemIndex = gponLineProfileGemIndex;
    }

    public Integer getGponLineProfileGemEncrypt() {
        return gponLineProfileGemEncrypt;
    }

    public void setGponLineProfileGemEncrypt(Integer gponLineProfileGemEncrypt) {
        this.gponLineProfileGemEncrypt = gponLineProfileGemEncrypt;
    }

    public Integer getGponLineProfileGemTcontId() {
        return gponLineProfileGemTcontId;
    }

    public void setGponLineProfileGemTcontId(Integer gponLineProfileGemTcontId) {
        this.gponLineProfileGemTcontId = gponLineProfileGemTcontId;
    }

    public Integer getGponLineProfileGemQueuePri() {
        return gponLineProfileGemQueuePri;
    }

    public void setGponLineProfileGemQueuePri(Integer gponLineProfileGemQueuePri) {
        this.gponLineProfileGemQueuePri = gponLineProfileGemQueuePri;
    }

    public Integer getGponLineProfileGemUpCar() {
        return gponLineProfileGemUpCar;
    }

    public void setGponLineProfileGemUpCar(Integer gponLineProfileGemUpCar) {
        this.gponLineProfileGemUpCar = gponLineProfileGemUpCar;
    }

    public Integer getGponLineProfileGemDownCar() {
        return gponLineProfileGemDownCar;
    }

    public void setGponLineProfileGemDownCar(Integer gponLineProfileGemDownCar) {
        this.gponLineProfileGemDownCar = gponLineProfileGemDownCar;
    }

    public Integer getGponLineProfileGemMapNum() {
        return gponLineProfileGemMapNum;
    }

    public void setGponLineProfileGemMapNum(Integer gponLineProfileGemMapNum) {
        this.gponLineProfileGemMapNum = gponLineProfileGemMapNum;
    }

    public Integer getGponLineProfileGemRowStatus() {
        return gponLineProfileGemRowStatus;
    }

    public void setGponLineProfileGemRowStatus(Integer gponLineProfileGemRowStatus) {
        this.gponLineProfileGemRowStatus = gponLineProfileGemRowStatus;
    }

    /**
     * @return the profileName
     */
    public String getProfileName() {
        return profileName;
    }

    /**
     * @param profileName
     *            the profileName to set
     */
    public void setProfileName(String profileName) {
        this.profileName = profileName;
    }

    public Integer getMappingMode() {
        return mappingMode;
    }

    public void setMappingMode(Integer mappingMode) {
        this.mappingMode = mappingMode;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("GponLineProfileGem [entityId=");
        builder.append(entityId);
        builder.append(", gponLineProfileGemProfileIndex=");
        builder.append(gponLineProfileGemProfileIndex);
        builder.append(", profileName=");
        builder.append(profileName);
        builder.append(", gponLineProfileGemIndex=");
        builder.append(gponLineProfileGemIndex);
        builder.append(", gponLineProfileGemEncrypt=");
        builder.append(gponLineProfileGemEncrypt);
        builder.append(", gponLineProfileGemTcontId=");
        builder.append(gponLineProfileGemTcontId);
        builder.append(", gponLineProfileGemQueuePri=");
        builder.append(gponLineProfileGemQueuePri);
        builder.append(", gponLineProfileGemUpCar=");
        builder.append(gponLineProfileGemUpCar);
        builder.append(", gponLineProfileGemDownCar=");
        builder.append(gponLineProfileGemDownCar);
        builder.append(", gponLineProfileGemMapNum=");
        builder.append(gponLineProfileGemMapNum);
        builder.append(", gponLineProfileGemRowStatus=");
        builder.append(gponLineProfileGemRowStatus);
        builder.append(", mappingMode=");
        builder.append(mappingMode);
        builder.append("]");
        return builder.toString();
    }

}
