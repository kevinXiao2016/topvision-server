/***********************************************************************
 * $Id: OltUniAttribute.java,v1.0 2011-9-26 上午09:09:05 $
 * 
 * @author: zhanglongyang
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.onu.domain;

import java.io.Serializable;

import com.topvision.framework.utils.EponIndex;
import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * Uni口属性表
 * 
 * @author zhanglongyang
 * 
 */
public class OltUniAttribute implements Serializable, AliasesSuperType {
    private static final long serialVersionUID = -5884979372105044075L;
    protected Long entityId;
    protected Long uniId;
    protected Long onuId;
    protected Long uniIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.5.1.1.1", index = true)
    private Long onuMibIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.5.1.1.2", index = true)
    private Long onuCardNo;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.5.1.1.3", index = true)
    private Long uniNo;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.5.1.1.4", writable = true, type = "Integer32")
    private Integer uniAdminStatus;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.5.1.1.5")
    private Integer uniOperationStatus;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.5.1.1.6", writable = true, type = "Integer32")
    private Integer uniAutoNegotiationEnable;
    // 该属性被废弃不用了
    // @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.5.1.1.7")
    @Deprecated
    private Integer uniAutoNegLocalTechAbility;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.5.1.1.9", writable = true, type = "Integer32")
    private Integer uniAutoNegotiationRestart;
    private Integer flowCtrl;

    // EPON OR GPON
    private String uniEorG = "E";

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
     * @return the uniId
     */
    public Long getUniId() {
        return uniId;
    }

    /**
     * @param uniId
     *            the uniId to set
     */
    public void setUniId(Long uniId) {
        this.uniId = uniId;
    }

    /**
     * @return the onuId
     */
    public Long getOnuId() {
        return onuId;
    }

    /**
     * @param onuId
     *            the onuId to set
     */
    public void setOnuId(Long onuId) {
        this.onuId = onuId;
    }

    /**
     * @return the uniIndex
     */
    public Long getUniIndex() {
        if (uniIndex == null) {
            uniIndex = EponIndex.getUniIndexByMibIndex(onuMibIndex, onuCardNo, uniNo);
        }
        return uniIndex;
    }

    /**
     * @param uniIndex
     *            the uniIndex to set
     */
    public void setUniIndex(Long uniIndex) {
        this.uniIndex = uniIndex;
        onuMibIndex = EponIndex.getOnuMibIndexByIndex(uniIndex);
        onuCardNo = EponIndex.getOnuCardNo(uniIndex);
        uniNo = EponIndex.getUniNo(uniIndex);
    }

    /**
     * @return the uniOperationStatus
     */
    public Integer getUniOperationStatus() {
        return uniOperationStatus;
    }

    /**
     * @param uniOperationStatus
     *            the uniOperationStatus to set
     */
    public void setUniOperationStatus(Integer uniOperationStatus) {
        this.uniOperationStatus = uniOperationStatus;
    }

    /**
     * @return the uniAdminStatus
     */
    public Integer getUniAdminStatus() {
        return uniAdminStatus;
    }

    /**
     * @param uniAdminStatus
     *            the uniAdminStatus to set
     */
    public void setUniAdminStatus(Integer uniAdminStatus) {
        this.uniAdminStatus = uniAdminStatus;
    }

    /**
     * @return the uniAutoNegotiationEnable
     */
    public Integer getUniAutoNegotiationEnable() {
        return uniAutoNegotiationEnable;
    }

    /**
     * @param uniAutoNegotiationEnable
     *            the uniAutoNegotiationEnable to set
     */
    public void setUniAutoNegotiationEnable(Integer uniAutoNegotiationEnable) {
        this.uniAutoNegotiationEnable = uniAutoNegotiationEnable;
    }

    /**
     * @return the uniAutoNegotiationLocalTechAbility
     */
    @Deprecated
    public Integer getUniAutoNegotiationLocalTechAbility() {
        return uniAutoNegLocalTechAbility;
    }

    /**
     * @param uniAutoNegotiationLocalTechAbility
     *            the uniAutoNegotiationLocalTechAbility to set
     */
    @Deprecated
    public void setUniAutoNegotiationLocalTechAbility(Integer uniAutoNegotiationLocalTechAbility) {
        this.uniAutoNegLocalTechAbility = uniAutoNegotiationLocalTechAbility;
    }

    public Long getOnuCardNo() {
        return onuCardNo;
    }

    public void setOnuCardNo(Long onuCardNo) {
        this.onuCardNo = onuCardNo;
    }

    /**
     * @return the onuMibIndex
     */
    public Long getOnuMibIndex() {
        return onuMibIndex;
    }

    /**
     * @param onuMibIndex
     *            the onuMibIndex to set
     */
    public void setOnuMibIndex(Long onuMibIndex) {
        this.onuMibIndex = onuMibIndex;
        // uniIndex = EponIndex.getUniIndexByMibIndex(onuMibIndex, onuCardNo, uniNo);
    }

    public Integer getUniAutoNegotiationRestart() {
        return uniAutoNegotiationRestart;
    }

    public void setUniAutoNegotiationRestart(Integer uniAutoNegotiationRestart) {
        this.uniAutoNegotiationRestart = uniAutoNegotiationRestart;
    }

    public Long getUniNo() {
        return uniNo;
    }

    public void setUniNo(Long uniNo) {
        this.uniNo = uniNo;
    }

    public Integer getFlowCtrl() {
        return flowCtrl;
    }

    public void setFlowCtrl(Integer flowCtrl) {
        this.flowCtrl = flowCtrl;
    }

    /**
     * @return the uniEorG
     */
    public String getUniEorG() {
        return uniEorG;
    }

    /**
     * @param uniEorG the uniEorG to set
     */
    public void setUniEorG(String uniEorG) {
        this.uniEorG = uniEorG;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("OltUniAttribute [entityId=");
        builder.append(entityId);
        builder.append(", uniId=");
        builder.append(uniId);
        builder.append(", onuId=");
        builder.append(onuId);
        builder.append(", uniIndex=");
        builder.append(uniIndex);
        builder.append(", onuMibIndex=");
        builder.append(onuMibIndex);
        builder.append(", onuCardNo=");
        builder.append(onuCardNo);
        builder.append(", uniNo=");
        builder.append(uniNo);
        builder.append(", uniAdminStatus=");
        builder.append(uniAdminStatus);
        builder.append(", uniOperationStatus=");
        builder.append(uniOperationStatus);
        builder.append(", uniAutoNegotiationEnable=");
        builder.append(uniAutoNegotiationEnable);
        builder.append(", uniAutoNegLocalTechAbility=");
        builder.append(uniAutoNegLocalTechAbility);
        builder.append(", uniAutoNegotiationRestart=");
        builder.append(uniAutoNegotiationRestart);
        builder.append(", flowCtrl=");
        builder.append(flowCtrl);
        builder.append("]");
        return builder.toString();
    }

}
