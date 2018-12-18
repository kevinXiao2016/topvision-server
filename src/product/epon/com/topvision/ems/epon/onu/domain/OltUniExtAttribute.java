/***********************************************************************
 * $Id: OltUniExtAttributr.java,v1.0 2011-11-7 上午10:47:59 $
 * 
 * @author: huqiao
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.onu.domain;

import java.io.Serializable;

import com.topvision.ems.epon.utils.EponUtil;
import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.dao.mybatis.AliasesSuperType;
import com.topvision.framework.utils.EponIndex;

/**
 * @author huqiao
 * @created @2011-11-7-上午10:47:59
 * 
 */
public class OltUniExtAttribute implements Serializable, AliasesSuperType {
    private static final long serialVersionUID = 9062214360923233028L;
    private Long entityId;
    private Long uniId;
    private Long uniIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.5.1.1.1", index = true)
    private Integer uniCardNo;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.5.1.1.2", index = true)
    private Integer uniPonNo;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.5.1.1.3", index = true)
    private Integer uniOnuNo;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.5.1.1.4", index = true)
    private Integer uniPortNo;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.5.1.1.5", writable = true, type = "Integer32")
    private Integer flowCtrl;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.5.1.1.6", writable = true, type = "Integer32")
    private Integer uniDSLoopBackEnable;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.5.1.1.7", writable = true, type = "Integer32")
    private Integer uniUSUtgPri;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.5.1.1.8", writable = true, type = "Integer32")
    private Integer perfStats15minuteEnable;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.5.1.1.9", writable = true, type = "Integer32")
    private Integer perfStats24hourEnable;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.5.1.1.10", writable = true, type = "Integer32")
    private Long lastChangeTime;
    // UNI端口不支持端口隔离使能配置，隔离使能配置提升到ONU进行配置
    // @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.5.1.1.11", writable = true,
    // type = "Integer32")
    private Integer isolationEnable;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.5.1.1.12", writable = true, type = "Integer32")
    private Long macAddrLearnMaxNum;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.5.1.1.13", writable = true, type = "OctetString")
    private String autoNegAdvertisedTechAbility;
    private Integer topUniAttrAutoNegotiationAdvertisedTechAbilityInteger;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.5.1.1.14", writable = true, type = "Integer32")
    private Integer macAddrClearByPort;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.5.1.1.15", writable = true, type = "Integer32")
    private Integer macAge;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.5.1.1.16", writable = true, type = "Integer32")
    private Integer topUniLoopDetectEnable;

    /**
     * @return the uniCardNo
     */
    public Integer getUniCardNo() {
        return uniCardNo;
    }

    /**
     * @param uniCardNo
     *            the uniCardNo to set
     */
    public void setUniCardNo(Integer uniCardNo) {
        this.uniCardNo = uniCardNo;
    }

    /**
     * @return the uniPonNo
     */
    public Integer getUniPonNo() {
        return uniPonNo;
    }

    /**
     * @param uniPonNo
     *            the uniPonNo to set
     */
    public void setUniPonNo(Integer uniPonNo) {
        this.uniPonNo = uniPonNo;
    }

    /**
     * @return the uniOnuNo
     */
    public Integer getUniOnuNo() {
        return uniOnuNo;
    }

    /**
     * @param uniOnuNo
     *            the uniOnuNo to set
     */
    public void setUniOnuNo(Integer uniOnuNo) {
        this.uniOnuNo = uniOnuNo;
    }

    /**
     * @return the uniPortNo
     */
    public Integer getUniPortNo() {
        return uniPortNo;
    }

    /**
     * @param uniPortNo
     *            the uniPortNo to set
     */
    public void setUniPortNo(Integer uniPortNo) {
        this.uniPortNo = uniPortNo;
    }

    /**
     * @return the topUniAttrFlowCtrl
     */
    public Integer getFlowCtrl() {
        return flowCtrl;
    }

    /**
     * @param flowCtrl
     *            the topUniAttrFlowCtrl to set
     */
    public void setFlowCtrl(Integer flowCtrl) {
        this.flowCtrl = flowCtrl;
    }

    public Integer getUniDSLoopBackEnable() {
        if (uniDSLoopBackEnable != null) {
            uniDSLoopBackEnable = uniDSLoopBackEnable == 1 ? 1 : 2;
        }
        return uniDSLoopBackEnable;
    }

    public void setUniDSLoopBackEnable(Integer uniDSLoopBackEnable) {
        this.uniDSLoopBackEnable = uniDSLoopBackEnable == 1 ? 1 : 2;
    }

    public Integer getUniUSUtgPri() {
        return uniUSUtgPri;
    }

    public void setUniUSUtgPri(Integer uniUSUtgPri) {
        this.uniUSUtgPri = uniUSUtgPri;
    }

    /**
     * @return the topUniAttrPerfStats15minuteEnable
     */
    public Integer getPerfStats15minuteEnable() {
        return perfStats15minuteEnable;
    }

    /**
     * @param perfStats15minuteEnable
     *            the topUniAttrPerfStats15minuteEnable to set
     */
    public void setPerfStats15minuteEnable(Integer perfStats15minuteEnable) {
        this.perfStats15minuteEnable = perfStats15minuteEnable;
    }

    /**
     * @return the topUniAttrPerfStats24hourEnable
     */
    public Integer getPerfStats24hourEnable() {
        return perfStats24hourEnable;
    }

    /**
     * @param perfStats24hourEnable
     *            the topUniAttrPerfStats24hourEnable to set
     */
    public void setPerfStats24hourEnable(Integer perfStats24hourEnable) {
        this.perfStats24hourEnable = perfStats24hourEnable;
    }

    /**
     * @return the topUniAttrLastChangeTime
     */
    public Long getLastChangeTime() {
        return lastChangeTime;
    }

    /**
     * @param lastChangeTime
     *            the topUniAttrLastChangeTime to set
     */
    public void setLastChangeTime(Long lastChangeTime) {
        this.lastChangeTime = lastChangeTime;
    }

    /**
     * @return the topUniAttrMacAddrLearnMaxNum
     */
    public Long getMacAddrLearnMaxNum() {
        return macAddrLearnMaxNum;
    }

    /**
     * @param macAddrLearnMaxNum
     *            the topUniAttrMacAddrLearnMaxNum to set
     */
    public void setMacAddrLearnMaxNum(Long macAddrLearnMaxNum) {
        this.macAddrLearnMaxNum = macAddrLearnMaxNum;
    }

    public String getAutoNegAdvertisedTechAbility() {
        return autoNegAdvertisedTechAbility;
    }

    public void setAutoNegAdvertisedTechAbility(String autoNegAdvertisedTechAbility) {
        this.autoNegAdvertisedTechAbility = autoNegAdvertisedTechAbility;
        if (autoNegAdvertisedTechAbility != null) {
            topUniAttrAutoNegotiationAdvertisedTechAbilityInteger = EponUtil
                    .getSymbolInfoFromMibByteStartFromZero(autoNegAdvertisedTechAbility);
        } else {
            topUniAttrAutoNegotiationAdvertisedTechAbilityInteger = null;
        }
    }

    /**
     * @return the topUniAttrMacAddrClearByPort
     */
    public Integer getMacAddrClearByPort() {
        return macAddrClearByPort;
    }

    /**
     * @param macAddrClearByPort
     *            the topUniAttrMacAddrClearByPort to set
     */
    public void setMacAddrClearByPort(Integer macAddrClearByPort) {
        this.macAddrClearByPort = macAddrClearByPort;
    }

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

    public Integer getMacAge() {
        return macAge;
    }

    public void setMacAge(Integer macAge) {
        this.macAge = macAge;
    }

    public Integer getIsolationEnable() {
        return isolationEnable;
    }

    public void setIsolationEnable(Integer isolationEnable) {
        this.isolationEnable = isolationEnable;
    }

    /**
     * @return the uniIndex
     */
    public Long getUniIndex() {
        if (uniIndex == null) {
            uniIndex = EponIndex.getUniIndex(uniCardNo, uniPonNo, uniOnuNo, 0, uniPortNo);
        }
        return uniIndex;
    }

    /**
     * @return the topUniAttrAutoNegotiationAdvertisedTechAbilityInteger
     */
    public Integer getTopUniAttrAutoNegotiationAdvertisedTechAbilityInteger() {
        return topUniAttrAutoNegotiationAdvertisedTechAbilityInteger;
    }

    /**
     * @param topUniAttrAutoNegotiationAdvertisedTechAbilityInteger
     *            the topUniAttrAutoNegotiationAdvertisedTechAbilityInteger to set
     */
    public void setTopUniAttrAutoNegotiationAdvertisedTechAbilityInteger(
            Integer topUniAttrAutoNegotiationAdvertisedTechAbilityInteger) {
        this.topUniAttrAutoNegotiationAdvertisedTechAbilityInteger = topUniAttrAutoNegotiationAdvertisedTechAbilityInteger;
        autoNegAdvertisedTechAbility = EponUtil.getMibByteFromSymbolInfoByStartZero(2,
                topUniAttrAutoNegotiationAdvertisedTechAbilityInteger);
    }

    /**
     * @param uniIndex
     *            the uniIndex to set
     */
    public void setUniIndex(Long uniIndex) {
        this.uniIndex = uniIndex;
        uniCardNo = EponIndex.getSlotNo(uniIndex).intValue();
        uniPonNo = EponIndex.getPonNo(uniIndex).intValue();
        uniOnuNo = EponIndex.getOnuNo(uniIndex).intValue();
        uniPortNo = EponIndex.getUniNo(uniIndex).intValue();
    }

    public Integer getTopUniLoopDetectEnable() {
        return topUniLoopDetectEnable;
    }

    public void setTopUniLoopDetectEnable(Integer topUniLoopDetectEnable) {
        this.topUniLoopDetectEnable = topUniLoopDetectEnable;
    }

    @Override
    public String toString() {
        return "OltUniExtAttribute [entityId=" + entityId + ", uniId=" + uniId + ", uniIndex=" + uniIndex
                + ", uniCardNo=" + uniCardNo + ", uniPonNo=" + uniPonNo + ", uniOnuNo=" + uniOnuNo + ", uniPortNo="
                + uniPortNo + ", flowCtrl=" + flowCtrl + ", uniDSLoopBackEnable=" + uniDSLoopBackEnable
                + ", uniUSUtgPri=" + uniUSUtgPri + ", perfStats15minuteEnable=" + perfStats15minuteEnable
                + ", perfStats24hourEnable=" + perfStats24hourEnable + ", lastChangeTime=" + lastChangeTime
                + ", isolationEnable=" + isolationEnable + ", macAddrLearnMaxNum=" + macAddrLearnMaxNum
                + ", autoNegAdvertisedTechAbility=" + autoNegAdvertisedTechAbility
                + ", topUniAttrAutoNegotiationAdvertisedTechAbilityInteger="
                + topUniAttrAutoNegotiationAdvertisedTechAbilityInteger + ", macAddrClearByPort=" + macAddrClearByPort
                + ", macAge=" + macAge + ", topUniLoopDetectEnable=" + topUniLoopDetectEnable + "]";
    }

}
