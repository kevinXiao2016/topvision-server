/***********************************************************************
 * $Id: OltSniTrunkConfig.java,v1.0 2011-11-5 下午02:36:50 $
 * 
 * @author: huqiao
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.trunk.domain;

import java.io.Serializable;
import java.util.List;

import com.topvision.ems.epon.utils.EponUtil;
import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.annotation.TableProperty;
import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author huqiao
 * @created @2011-11-5-下午02:36:50
 * 
 */
@TableProperty(tables = { "default", "trunk" })
public class OltSniTrunkConfig implements Serializable, AliasesSuperType {
    private static final long serialVersionUID = 1371976501840602302L;
    private Long entityId;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.2.2.1.1.1", index = true)
    private Integer sniTrunkGroupConfigIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.2.2.1.1.2", writable = true, type = "OctetString")
    private String sniTrunkGroupConfigName;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.2.2.1.1.3", writable = true, type = "OctetString")
    private String sniTrunkGroupConfigMember;
    private List<Long> sniTrunkGroupConfigGroup;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.2.2.1.1.4", writable = true, type = "Integer32")
    private Integer sniTrunkGroupConfigPolicy;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.2.2.1.1.5", writable = true, type = "Integer32")
    private Integer sniTrunkGroupConfigRowstatus;
    @SnmpProperty(table = "trunk", oid = "1.3.6.1.4.1.17409.2.3.2.2.2.1.1", index = true)
    private Integer sniTrunkGroupIndex;
    @SnmpProperty(table = "trunk", oid = "1.3.6.1.4.1.17409.2.3.2.2.2.1.2")
    private Integer sniTrunkGroupOperationStatus;
    @SnmpProperty(table = "trunk", oid = "1.3.6.1.4.1.17409.2.3.2.2.2.1.3")
    private Integer sniTrunkGroupActualSpeed;
    @SnmpProperty(table = "trunk", oid = "1.3.6.1.4.1.17409.2.3.2.2.2.1.4", writable = true, type = "Integer32")
    private Integer sniTrunkGroupAdminStatus;
    //trunk组中端口名称
    private List<String> trunkPortNameList;

    /**
     * @return the entityId
     */
    public Long getEntityId() {
        return entityId;
    }

    /**
     * @param entityId
     *            the entityId to set
     */
    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    /**
     * @return the sniTrunkGroupConfigIndex
     */
    public Integer getSniTrunkGroupConfigIndex() {
        return sniTrunkGroupConfigIndex;
    }

    /**
     * @param sniTrunkGroupConfigIndex
     *            the sniTrunkGroupConfigIndex to set
     */
    public void setSniTrunkGroupConfigIndex(Integer sniTrunkGroupConfigIndex) {
        this.sniTrunkGroupConfigIndex = sniTrunkGroupConfigIndex;
        this.sniTrunkGroupIndex = sniTrunkGroupConfigIndex;
    }

    /**
     * @return the sniTrunkGroupConfigName
     */
    public String getSniTrunkGroupConfigName() {
        return sniTrunkGroupConfigName;
    }

    /**
     * @param sniTrunkGroupConfigName
     *            the sniTrunkGroupConfigName to set
     */
    public void setSniTrunkGroupConfigName(String sniTrunkGroupConfigName) {
        this.sniTrunkGroupConfigName = sniTrunkGroupConfigName;
    }

    /**
     * @return the sniTrunkGroupConfigMember
     */
    public String getSniTrunkGroupConfigMember() {
        return sniTrunkGroupConfigMember;
    }

    /**
     * @param sniTrunkGroupConfigMember
     *            the sniTrunkGroupConfigMember to set
     */
    public void setSniTrunkGroupConfigMember(String sniTrunkGroupConfigMember) {
        this.sniTrunkGroupConfigMember = sniTrunkGroupConfigMember;
        sniTrunkGroupConfigGroup = EponUtil.getSniPortIndexFromMib(sniTrunkGroupConfigMember);
    }

    /**
     * @return the sniTrunkGroupConfigPolicy
     */
    public Integer getSniTrunkGroupConfigPolicy() {
        return sniTrunkGroupConfigPolicy;
    }

    /**
     * @param sniTrunkGroupConfigPolicy
     *            the sniTrunkGroupConfigPolicy to set
     */
    public void setSniTrunkGroupConfigPolicy(Integer sniTrunkGroupConfigPolicy) {
        this.sniTrunkGroupConfigPolicy = sniTrunkGroupConfigPolicy;
    }

    /**
     * @return the sniTrunkGroupConfigRowstatus
     */
    public Integer getSniTrunkGroupConfigRowstatus() {
        return sniTrunkGroupConfigRowstatus;
    }

    /**
     * @param sniTrunkGroupConfigRowstatus
     *            the sniTrunkGroupConfigRowstatus to set
     */
    public void setSniTrunkGroupConfigRowstatus(Integer sniTrunkGroupConfigRowstatus) {
        this.sniTrunkGroupConfigRowstatus = sniTrunkGroupConfigRowstatus;
    }

    /**
     * @return the sniTrunkGroupOperationStatus
     */
    public Integer getSniTrunkGroupOperationStatus() {
        return sniTrunkGroupOperationStatus;
    }

    /**
     * @param sniTrunkGroupOperationStatus
     *            the sniTrunkGroupOperationStatus to set
     */
    public void setSniTrunkGroupOperationStatus(Integer sniTrunkGroupOperationStatus) {
        this.sniTrunkGroupOperationStatus = sniTrunkGroupOperationStatus;
    }

    /**
     * @return the sniTrunkGroupActualSpeed
     */
    public Integer getSniTrunkGroupActualSpeed() {
        return sniTrunkGroupActualSpeed;
    }

    /**
     * @param sniTrunkGroupActualSpeed
     *            the sniTrunkGroupActualSpeed to set
     */
    public void setSniTrunkGroupActualSpeed(Integer sniTrunkGroupActualSpeed) {
        this.sniTrunkGroupActualSpeed = sniTrunkGroupActualSpeed;
    }

    /**
     * @return the sniTrunkGroupAdminStatus
     */
    public Integer getSniTrunkGroupAdminStatus() {
        return sniTrunkGroupAdminStatus;
    }

    /**
     * @param sniTrunkGroupAdminStatus
     *            the sniTrunkGroupAdminStatus to set
     */
    public void setSniTrunkGroupAdminStatus(Integer sniTrunkGroupAdminStatus) {
        this.sniTrunkGroupAdminStatus = sniTrunkGroupAdminStatus;
    }

    public List<Long> getSniTrunkGroupConfigGroup() {
        return sniTrunkGroupConfigGroup;
    }

    public void setSniTrunkGroupConfigGroup(List<Long> sniTrunkGroupConfigGroup) {
        this.sniTrunkGroupConfigGroup = sniTrunkGroupConfigGroup;
        sniTrunkGroupConfigMember = EponUtil.getMibStringFromSniPortList(sniTrunkGroupConfigGroup);
    }

    public Integer getSniTrunkGroupIndex() {
        return sniTrunkGroupIndex;
    }

    public void setSniTrunkGroupIndex(Integer sniTrunkGroupIndex) {
        this.sniTrunkGroupIndex = sniTrunkGroupIndex;
        this.sniTrunkGroupConfigIndex = sniTrunkGroupIndex;
    }

    public List<String> getTrunkPortNameList() {
        return trunkPortNameList;
    }

    public void setTrunkPortNameList(List<String> trunkPortNameList) {
        this.trunkPortNameList = trunkPortNameList;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("OltSniTrunkConfig");
        sb.append("{entityId=").append(entityId);
        sb.append(", sniTrunkGroupConfigIndex=").append(sniTrunkGroupConfigIndex);
        sb.append(", sniTrunkGroupConfigName='").append(sniTrunkGroupConfigName).append('\'');
        sb.append(", sniTrunkGroupConfigMember='").append(sniTrunkGroupConfigMember).append('\'');
        sb.append(", sniTrunkGroupConfigGroup=").append(sniTrunkGroupConfigGroup);
        sb.append(", sniTrunkGroupConfigPolicy=").append(sniTrunkGroupConfigPolicy);
        sb.append(", sniTrunkGroupConfigRowstatus=").append(sniTrunkGroupConfigRowstatus);
        sb.append(", sniTrunkGroupIndex=").append(sniTrunkGroupIndex);
        sb.append(", sniTrunkGroupOperationStatus=").append(sniTrunkGroupOperationStatus);
        sb.append(", sniTrunkGroupActualSpeed=").append(sniTrunkGroupActualSpeed);
        sb.append(", sniTrunkGroupAdminStatus=").append(sniTrunkGroupAdminStatus);
        sb.append('}');
        return sb.toString();
    }

}
