/************************************************************************
// * $Id: OltUniAttribute.java,v1.0 2011-9-26 上午09:09:05 $
// * 
// * @author: zhanglongyang
// * 
// * disc:该类被废弃使用 OltUniExtAttribute代替 (lizongtian 2011-12-21)
// * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.onu.domain;

import java.io.Serializable;

import com.topvision.framework.utils.EponIndex;
import com.topvision.framework.annotation.SnmpProperty;

/**
 * Uni口属性表
 * 
 * @author zhanglongyang
 * 
 */
@Deprecated
public class OltTopUniAttribute implements Serializable {
    private static final long serialVersionUID = -5884979372105044075L;
    private Long entityId;
    private Long uniId;
    private Long onuId;
    private Long uniIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.5.1.1.1", index = true)
    private Long cardNo;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.5.1.1.2", index = true)
    private Long ponNo;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.5.1.1.3", index = true)
    private Long onuNo;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.5.1.1.4", index = true)
    private Long uniNo;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.5.1.1.5", writable = true, type = "Integer32")
    private Integer flowCtrl;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.5.1.1.6", writable = true, type = "Integer32")
    private Integer perfStats15minuteEnable;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.5.1.1.7", writable = true, type = "Integer32")
    private Integer perfStats24hourEnable;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.5.1.1.8")
    private Long lastChangeTime;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.5.1.1.9", writable = true, type = "Integer32")
    private Integer isolationEnable;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.5.1.1.10", writable = true, type = "Integer32")
    private Long macAddrLearnMaxNum;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.5.1.1.11", writable = true, type = "Integer32")
    private Integer autoNegAdvertisedTechAbility;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.5.1.1.12", writable = true, type = "Integer32")
    private Integer macAddrClearByPort;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.5.1.1.13", writable = true, type = "Integer32")
    private Integer macAge;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.5.1.1.16", writable = true, type = "Integer32")
    private Integer topUniLoopDetectEnable;

    public Long getCardNo() {
        return cardNo;
    }

    public void setCardNo(Long cardNo) {
        this.cardNo = cardNo;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public Long getOnuNo() {
        return onuNo;
    }

    public void setOnuNo(Long onuNo) {
        this.onuNo = onuNo;
    }

    public Long getOnuId() {
        return onuId;
    }

    public void setOnuId(Long onuId) {
        this.onuId = onuId;
    }

    public Long getPonNo() {
        return ponNo;
    }

    public void setPonNo(Long ponNo) {
        this.ponNo = ponNo;
    }

    public Integer getAutoNegAdvertisedTechAbility() {
        return autoNegAdvertisedTechAbility;
    }

    public void setAutoNegAdvertisedTechAbility(Integer autoNegAdvertisedTechAbility) {
        this.autoNegAdvertisedTechAbility = autoNegAdvertisedTechAbility;
    }

    public Integer getFlowCtrl() {
        return flowCtrl;
    }

    public void setFlowCtrl(Integer flowCtrl) {
        this.flowCtrl = flowCtrl;
    }

    public Integer getIsolationEnable() {
        return isolationEnable;
    }

    public void setIsolationEnable(Integer isolationEnable) {
        this.isolationEnable = isolationEnable;
    }

    public Long getLastChangeTime() {
        return lastChangeTime;
    }

    public void setLastChangeTime(Long lastChangeTime) {
        this.lastChangeTime = lastChangeTime;
    }

    public Integer getMacAddrClearByPort() {
        return macAddrClearByPort;
    }

    public void setMacAddrClearByPort(Integer macAddrClearByPort) {
        this.macAddrClearByPort = macAddrClearByPort;
    }

    public Long getMacAddrLearnMaxNum() {
        return macAddrLearnMaxNum;
    }

    public void setMacAddrLearnMaxNum(Long macAddrLearnMaxNum) {
        this.macAddrLearnMaxNum = macAddrLearnMaxNum;
    }

    public Integer getPerfStats15minuteEnable() {
        return perfStats15minuteEnable;
    }

    public void setPerfStats15minuteEnable(Integer perfStats15minuteEnable) {
        this.perfStats15minuteEnable = perfStats15minuteEnable;
    }

    public Integer getPerfStats24hourEnable() {
        return perfStats24hourEnable;
    }

    public void setPerfStats24hourEnable(Integer perfStats24hourEnable) {
        this.perfStats24hourEnable = perfStats24hourEnable;
    }

    public Long getUniId() {
        return uniId;
    }

    public void setUniId(Long uniId) {
        this.uniId = uniId;
    }

    public Long getUniIndex() {
        if (uniIndex == null) {
            uniIndex = new EponIndex(cardNo.intValue(), ponNo.intValue(), onuNo.intValue(), 1, uniNo.intValue())
                    .getUniIndex();
        }
        return uniIndex;
    }

    public void setUniIndex(Long uniIndex) {
        this.uniIndex = uniIndex;
        cardNo = EponIndex.getSlotNo(uniIndex);
        ponNo = EponIndex.getPonNo(uniIndex);
        onuNo = EponIndex.getOnuNo(uniIndex);
        uniNo = EponIndex.getUniNo(uniIndex);
    }

    public Long getUniNo() {
        return uniNo;
    }

    public void setUniNo(Long uniNo) {
        this.uniNo = uniNo;
    }

    /**
     * @return the topUniAttrMacAge
     */
    public Integer getMacAge() {
        return macAge;
    }

    /**
     * @param macAge
     *            the topUniAttrMacAge to set
     */
    public void setMacAge(Integer macAge) {
        this.macAge = macAge;
    }

    public Integer getTopUniLoopDetectEnable() {
        return topUniLoopDetectEnable;
    }

    public void setTopUniLoopDetectEnable(Integer topUniLoopDetectEnable) {
        this.topUniLoopDetectEnable = topUniLoopDetectEnable;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("OltTopUniAttribute [entityId=");
        builder.append(entityId);
        builder.append(", uniId=");
        builder.append(uniId);
        builder.append(", onuId=");
        builder.append(onuId);
        builder.append(", uniIndex=");
        builder.append(uniIndex);
        builder.append(", cardNo=");
        builder.append(cardNo);
        builder.append(", ponNo=");
        builder.append(ponNo);
        builder.append(", onuNo=");
        builder.append(onuNo);
        builder.append(", uniNo=");
        builder.append(uniNo);
        builder.append(", flowCtrl=");
        builder.append(flowCtrl);
        builder.append(", perfStats15minuteEnable=");
        builder.append(perfStats15minuteEnable);
        builder.append(", perfStats24hourEnable=");
        builder.append(perfStats24hourEnable);
        builder.append(", lastChangeTime=");
        builder.append(lastChangeTime);
        builder.append(", isolationEnable=");
        builder.append(isolationEnable);
        builder.append(", macAddrLearnMaxNum=");
        builder.append(macAddrLearnMaxNum);
        builder.append(", autoNegAdvertisedTechAbility=");
        builder.append(autoNegAdvertisedTechAbility);
        builder.append(", macAddrClearByPort=");
        builder.append(macAddrClearByPort);
        builder.append(", macAge=");
        builder.append(macAge);
        builder.append("]");
        return builder.toString();
    }
}
