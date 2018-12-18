/***********************************************************************
 * $Id: OnuSrvProfile.java,v1.0 2015-12-8 下午2:30:22 $
 * 
 * @author: lizongtian
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.businesstemplate.domain;

import java.io.Serializable;

import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author lizongtian
 * @created @2015-12-8-下午2:30:22
 *
 */
public class OnuSrvProfile implements Serializable, AliasesSuperType {
    private static final long serialVersionUID = -1226728670757669976L;

    private Long entityId;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.4.16.1.1.1", index = true)
    private Integer srvProfileId;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.4.16.1.1.2", writable = true, type = "OctetString")
    private String srvProfileName;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.4.16.1.1.3", writable = true, type = "Integer32")
    private Integer srvImgpMode;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.4.16.1.1.4", writable = true, type = "Integer32")
    private Integer srvIgmpFastLeave;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.4.16.1.1.5", writable = true, type = "Integer32")
    private Integer srvBindCap;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.4.16.1.1.6", type = "Integer32")
    private Integer srvBindCnt;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.4.16.1.1.7", writable = true, type = "Integer32")
    private Integer rowStatus;

    public Integer getSrvProfileId() {
        return srvProfileId;
    }

    public void setSrvProfileId(Integer srvProfileId) {
        this.srvProfileId = srvProfileId;
    }

    public String getSrvProfileName() {
        return srvProfileName;
    }

    public void setSrvProfileName(String srvProfileName) {
        this.srvProfileName = srvProfileName;
    }

    public Integer getSrvImgpMode() {
        return srvImgpMode;
    }

    public void setSrvImgpMode(Integer srvImgpMode) {
        this.srvImgpMode = srvImgpMode;
    }

    public Integer getSrvIgmpFastLeave() {
        return srvIgmpFastLeave;
    }

    public void setSrvIgmpFastLeave(Integer srvIgmpFastLeave) {
        this.srvIgmpFastLeave = srvIgmpFastLeave;
    }

    public Integer getSrvBindCap() {
        return srvBindCap;
    }

    public void setSrvBindCap(Integer srvBindCap) {
        this.srvBindCap = srvBindCap;
    }

    public Integer getSrvBindCnt() {
        return srvBindCnt;
    }

    public void setSrvBindCnt(Integer srvBindCnt) {
        this.srvBindCnt = srvBindCnt;
    }

    public Integer getRowStatus() {
        return rowStatus;
    }

    public void setRowStatus(Integer rowStatus) {
        this.rowStatus = rowStatus;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("OnuSrvProfile [entityId=");
        builder.append(entityId);
        builder.append(", srvProfileId=");
        builder.append(srvProfileId);
        builder.append(", srvProfileName=");
        builder.append(srvProfileName);
        builder.append(", srvImgpMode=");
        builder.append(srvImgpMode);
        builder.append(", srvIgmpFastLeave=");
        builder.append(srvIgmpFastLeave);
        builder.append(", srvBindCap=");
        builder.append(srvBindCap);
        builder.append(", srvBindCnt=");
        builder.append(srvBindCnt);
        builder.append(", rowStatus=");
        builder.append(rowStatus);
        builder.append("]");
        return builder.toString();
    }

}
