/***********************************************************************
 * $Id: SnmpTargetParams.java,v1.0 2013-01-09 上午9:41:57 $
 * 
 * @author: yq
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.snmpV3.facade.domain;

import org.apache.ibatis.type.Alias;

import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * SNMP V3 TargetParams
 * 
 * @author yq
 * 
 */
@Alias("snmpTargetParams")
public class SnmpTargetParams implements AliasesSuperType {
    private static final long serialVersionUID = 2953394275025128265L;
    private Long entityId;
    @SnmpProperty(oid = "1.3.6.1.6.3.12.1.3.1.1", index = true)
    private String targetParamsName;
    @SnmpProperty(oid = "1.3.6.1.6.3.12.1.3.1.2", writable = true, type = "Integer32")
    private Long targetParamsMPModel;
    @SnmpProperty(oid = "1.3.6.1.6.3.12.1.3.1.3", writable = true, type = "Integer32")
    private Long targetParamsSecurityModel;
    @SnmpProperty(oid = "1.3.6.1.6.3.12.1.3.1.4", writable = true, type = "OctetString")
    private String targetParamsSecurityName;
    @SnmpProperty(oid = "1.3.6.1.6.3.12.1.3.1.5", writable = true, type = "Integer32")
    private Integer targetParamsSecurityLevel;
    @SnmpProperty(oid = "1.3.6.1.6.3.12.1.3.1.6", writable = true, type = "Integer32")
    private Integer targetParamsStorageType;
    @SnmpProperty(oid = "1.3.6.1.6.3.12.1.3.1.7", writable = true, type = "Integer32")
    private Integer targetParamsRowStatus;

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public String getTargetParamsName() {
        return targetParamsName;
    }

    public void setTargetParamsName(String targetParamsName) {
        this.targetParamsName = targetParamsName;
    }

    public Long getTargetParamsMPModel() {
        return targetParamsMPModel;
    }

    public void setTargetParamsMPModel(Long targetParamsMPModel) {
        this.targetParamsMPModel = targetParamsMPModel;
    }

    public Long getTargetParamsSecurityModel() {
        return targetParamsSecurityModel;
    }

    public void setTargetParamsSecurityModel(Long targetParamsSecurityModel) {
        this.targetParamsSecurityModel = targetParamsSecurityModel;
    }

    public String getTargetParamsSecurityName() {
        return targetParamsSecurityName;
    }

    public void setTargetParamsSecurityName(String targetParamsSecurityName) {
        this.targetParamsSecurityName = targetParamsSecurityName;
    }

    public Integer getTargetParamsSecurityLevel() {
        return targetParamsSecurityLevel;
    }

    public void setTargetParamsSecurityLevel(Integer targetParamsSecurityLevel) {
        this.targetParamsSecurityLevel = targetParamsSecurityLevel;
    }

    public Integer getTargetParamsStorageType() {
        return targetParamsStorageType;
    }

    public void setTargetParamsStorageType(Integer targetParamsStorageType) {
        this.targetParamsStorageType = targetParamsStorageType;
    }

    public Integer getTargetParamsRowStatus() {
        return targetParamsRowStatus;
    }

    public void setTargetParamsRowStatus(Integer targetParamsRowStatus) {
        this.targetParamsRowStatus = targetParamsRowStatus;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("SnmpTargetParams [entityId=").append(entityId).append(", targetParamsName=")
                .append(targetParamsName).append(", targetParamsMPModel=").append(targetParamsMPModel)
                .append(", targetParamsSecurityModel=").append(targetParamsSecurityModel)
                .append(", targetParamsSecurityName=").append(targetParamsSecurityName)
                .append(", targetParamsSecurityLevel=").append(targetParamsSecurityLevel)
                .append(", targetParamsStorageType=").append(targetParamsStorageType)
                .append(", targetParamsRowStatus=").append(targetParamsRowStatus).append("]");
        return sb.toString();
    }

}
