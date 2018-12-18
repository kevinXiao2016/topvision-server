/***********************************************************************
 * $Id: OltSniMacAddress.java,v1.0 2011-11-8 下午03:03:37 $
 * 
 * @author: zhanglongyang
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.olt.domain;

import java.io.Serializable;

import com.topvision.framework.utils.EponIndex;
import com.topvision.ems.epon.utils.EponUtil;
import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.common.MacUtils;
import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * SNI口MAC地址管理
 * 
 * @author zhanglongyang
 * @created @2011-11-8-下午03:03:37
 * 
 */
public class OltSniMacAddress implements Serializable, AliasesSuperType {
    private static final long serialVersionUID = 2511352976784738001L;
    private Long entityId;
    private Long sniId;
    private Long sniIndex;
    // MIB中使用值
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.2.4.2.1.1", index = true)
    private String sniMacAddrIndex;
    // 页面中使用值
    private String sniMacAddrIndexHex;
    // 数据库中使用值
    private Long sniMacAddrIndexLong;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.2.4.2.1.2", index = true)
    private Integer sniMacAddrVlanIdIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.2.4.2.1.3", writable = true, type = "Integer32")
    private Integer sniMacAddrType;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.2.4.2.1.4", writable = true, type = "Integer32")
    private Long sniMacAddrPortId;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.2.4.2.1.5", writable = true, type = "Integer32")
    private Integer sniMacAddrRowStatus;

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
     * @return the sniId
     */
    public Long getSniId() {
        return sniId;
    }

    /**
     * @param sniId
     *            the sniId to set
     */
    public void setSniId(Long sniId) {
        this.sniId = sniId;
    }

    /**
     * @return the sniIndex
     */
    public Long getSniIndex() {
        return sniIndex;
    }

    /**
     * @param sniIndex
     *            the sniIndex to set
     */
    public void setSniIndex(Long sniIndex) {
        this.sniIndex = sniIndex;
        sniMacAddrPortId = EponIndex.getOnuMibIndexByIndex(sniIndex);
    }

    /**
     * @return the sniMacAddrIndex
     */
    public String getSniMacAddrIndex() {
        return sniMacAddrIndex;
    }

    /**
     * @param sniMacAddrIndex
     *            the sniMacAddrIndex to set
     */
    public void setSniMacAddrIndex(String sniMacAddrIndex) {
        this.sniMacAddrIndex = sniMacAddrIndex;
        sniMacAddrIndexHex = EponUtil.getMacFromMibMac(sniMacAddrIndex);
        sniMacAddrIndexLong = new MacUtils(EponUtil.getMacFromMibMac(sniMacAddrIndex)).longValue();
    }

    /**
     * @return the sniMacAddrIndexHex
     */
    public String getSniMacAddrIndexHex() {
        return sniMacAddrIndexHex;
    }

    /**
     * @param sniMacAddrIndexHex
     *            the sniMacAddrIndexHex to set
     */
    public void setSniMacAddrIndexHex(String sniMacAddrIndexHex) {
        this.sniMacAddrIndexHex = sniMacAddrIndexHex;
        sniMacAddrIndexLong = new MacUtils(sniMacAddrIndexHex).longValue();
        sniMacAddrIndex = EponUtil.getMibMacFromHexMac(sniMacAddrIndexHex);
    }

    /**
     * @return the sniMacAddrIndexLong
     */
    public Long getSniMacAddrIndexLong() {
        return sniMacAddrIndexLong;
    }

    /**
     * @param sniMacAddrIndexLong
     *            the sniMacAddrIndexLong to set
     */
    public void setSniMacAddrIndexLong(Long sniMacAddrIndexLong) {
        this.sniMacAddrIndexLong = sniMacAddrIndexLong;
        sniMacAddrIndex = EponUtil.getMibMacFromHexMac(new MacUtils(sniMacAddrIndexLong).toString(MacUtils.MAOHAO)
                .toUpperCase());
        sniMacAddrIndexHex = new MacUtils(sniMacAddrIndexLong).toString(MacUtils.MAOHAO).toUpperCase();
    }

    /**
     * @return the sniMacAddrVlanIdIndex
     */
    public Integer getSniMacAddrVlanIdIndex() {
        return sniMacAddrVlanIdIndex;
    }

    /**
     * @param sniMacAddrVlanIdIndex
     *            the sniMacAddrVlanIdIndex to set
     */
    public void setSniMacAddrVlanIdIndex(Integer sniMacAddrVlanIdIndex) {
        this.sniMacAddrVlanIdIndex = sniMacAddrVlanIdIndex;
    }

    /**
     * @return the sniMacAddrType
     */
    public Integer getSniMacAddrType() {
        return sniMacAddrType;
    }

    /**
     * @param sniMacAddrType
     *            the sniMacAddrType to set
     */
    public void setSniMacAddrType(Integer sniMacAddrType) {
        this.sniMacAddrType = sniMacAddrType;
    }

    /**
     * @return the sniMacAddrPortId
     */
    public Long getSniMacAddrPortId() {
        return sniMacAddrPortId;
    }

    /**
     * @param sniMacAddrPortId
     *            the sniMacAddrPortId to set
     */
    public void setSniMacAddrPortId(Long sniMacAddrPortId) {
        this.sniMacAddrPortId = sniMacAddrPortId;
        sniIndex = EponIndex.getOnuIndexByMibIndex(sniMacAddrPortId);
    }

    /**
     * @return the sniMacAddrRowStatus
     */
    public Integer getSniMacAddrRowStatus() {
        return sniMacAddrRowStatus;
    }

    /**
     * @param sniMacAddrRowStatus
     *            the sniMacAddrRowStatus to set
     */
    public void setSniMacAddrRowStatus(Integer sniMacAddrRowStatus) {
        this.sniMacAddrRowStatus = sniMacAddrRowStatus;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("OltSniMacAddress");
        sb.append("{entityId=").append(entityId);
        sb.append(", sniId=").append(sniId);
        sb.append(", sniMacAddrIndex=").append(sniMacAddrIndex);
        sb.append(", sniMacAddrIndexHex=").append(sniMacAddrIndexHex);
        sb.append(", sniMacAddrIndexLong=").append(sniMacAddrIndexLong);
        sb.append(", sniMacAddrVlanIdIndex=").append(sniMacAddrVlanIdIndex);
        sb.append(", sniMacAddrType=").append(sniMacAddrType);
        sb.append(", sniMacAddrPortId=").append(sniMacAddrPortId);
        sb.append(", sniMacAddrRowStatus=").append(sniMacAddrRowStatus);
        sb.append('}');
        return sb.toString();
    }
}
