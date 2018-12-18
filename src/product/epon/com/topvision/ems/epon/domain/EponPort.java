/***********************************************************************
 * $Id: Vertex.java,v1.0 2011-9-26 下午01:17:34 $
 * 
 * @author: lizongtian
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.domain;

import java.io.Serializable;

import com.topvision.ems.epon.olt.domain.OltPonAttribute;
import com.topvision.ems.epon.olt.domain.OltSniAttribute;
import com.topvision.framework.utils.EponIndex;

/**
 * Port关系
 * 
 * @author lizongtian
 * 
 */
@SuppressWarnings("rawtypes")
public class EponPort implements Serializable, Comparable {
    private static final long serialVersionUID = -445471800563591334L;
    public static final String[] MEDIATYPE = { "", "twistedPair", "fiber", "other" };
    public static final String[] AUTONEGO = { "", "auto-negotiate", "half-10", "full-10", "half-100", "full-100",
            "full-1000", "full-10000", "unknown" };
    public static final String[] PONPORTTYPE = { "", "GE-EPON", "TENGE-EPON", "GPON", "OTHER" };

    private Long portId;
    private Long portIndex;
    private Long slotRealIndex;
    private Long portRealIndex;
    private String portSubType;
    private String sMaxDsBandwidth;
    private String sMaxUsBandwidth;

    private String sniPortName;// sni端口名
    private String sniMediaType;// 媒介类型
    private String sniAutoNegotiationStatus;// 自协商状态
    private Integer sniAutoNegotiationMode;// 自协商模式
    private Long sniMacAddrLearnMaxNum;// MAC地址最大学习数
    private Integer sniIsolationEnable;// 隔离使能
    private Long sniLastStatusChangeTime;// 状态变更时间
    private Integer sniOperationStatus;
    private Integer sniAdminStatus;
    private Integer sniPerfStats15minuteEnable;
    private Integer sniPerfStats24hourEnable;
    private Long topSniAttrCardNo;
    private Long topSniAttrPortNo;
    private Integer topSniAttrFlowCtrlEnable;
    private Integer topSniAttrIngressRate;
    private Integer topSniAttrEgressRate;
    private Integer topSniAttrActualSpeed;

    private String ponPortType;// Pon端口类型
    private Integer ponPortMaxOnuNumSupport;// Pon端口最大支持ONU数
    private Integer ponPortUpOnuNum;// Pon口下在线Onu数
    private Integer ponPortEncryptMode;// 加密模式
    private Integer ponPortEncryptKeyExchangeTime;// 密钥交换时间
    private Integer maxDsBandwidth;// 最大下行带宽
    private Integer actualDsBandwidthInUse;// 实际使用下行带宽
    private Integer remainDsBandwidth;// 剩余使用下行带宽
    private Long ponPortMacAddrLearnMaxNum;// MAC地址最大学习数
    private Integer maxUsBandwidth;// 最大上行带宽
    private Integer actualUsBandwidthInUse;// 实际使用上行带宽
    private Integer remainUsBandwidth;// 剩余使用上行带宽
    private Integer ponOperationStatus;
    private Integer ponPortAdminStatus;
    private Integer ponPortIsolationEnable;
    private Integer perfStats15minuteEnable;
    private Integer perfStats24hourEnable;
    private Integer stpPortEnabled;
    private Integer stpPortRstpProtocolMigration;
    private boolean isStandbyPort; // 标记它是否是PON保护组备用端口，如果是备用端口，在前端将无法进行任何操作
    private Integer ponSpeedMode;
    private Integer ponRogueSwitch;

    public void modifyAttribute(OltPonAttribute oltPonAttribute) {
        if (oltPonAttribute.getPonPortType() != null && oltPonAttribute.getPonPortType() >= 0
                && oltPonAttribute.getPonPortType() < PONPORTTYPE.length) {
            this.ponPortType = PONPORTTYPE[oltPonAttribute.getPonPortType()];// Pon端口类型
        }
        this.ponPortMaxOnuNumSupport = oltPonAttribute.getPonPortMaxOnuNumSupport();// Pon端口最大支持ONU数
        this.ponPortUpOnuNum = oltPonAttribute.getPonPortUpOnuNum();// Pon口下在线Onu数
        this.ponPortEncryptMode = oltPonAttribute.getPonPortEncryptMode();// 加密模式
        this.ponPortEncryptKeyExchangeTime = oltPonAttribute.getPonPortEncryptKeyExchangeTime();// 密钥交换时间
        this.maxDsBandwidth = oltPonAttribute.getMaxDsBandwidth();// 最大下行带宽
        this.actualDsBandwidthInUse = oltPonAttribute.getActualDsBandwidthInUse();// 实际使用下行带宽
        this.remainDsBandwidth = oltPonAttribute.getRemainDsBandwidth();// 剩余使用下行带宽
        this.ponPortMacAddrLearnMaxNum = oltPonAttribute.getPonPortMacAddrLearnMaxNum();// MAC地址最大学习数
        this.maxUsBandwidth = oltPonAttribute.getMaxUsBandwidth();// 最大上行带宽
        this.actualUsBandwidthInUse = oltPonAttribute.getActualUsBandwidthInUse();// 实际使用上行带宽
        this.remainUsBandwidth = oltPonAttribute.getRemainUsBandwidth();// 剩余使用上行带宽
        this.ponOperationStatus = oltPonAttribute.getPonOperationStatus();
        this.ponPortAdminStatus = oltPonAttribute.getPonPortAdminStatus();
        this.ponPortIsolationEnable = oltPonAttribute.getPonPortIsolationEnable();
        this.perfStats15minuteEnable = oltPonAttribute.getPerfStats15minuteEnable();
        this.perfStats24hourEnable = oltPonAttribute.getPerfStats24hourEnable();
        this.sMaxUsBandwidth = oltPonAttribute.getSMaxUsBandwidth();
        this.sMaxDsBandwidth = oltPonAttribute.getSMaxDsBandwidth();
        this.isStandbyPort = oltPonAttribute.isStandbyPort();
        this.ponSpeedMode = oltPonAttribute.getPonSpeedMode();
        this.ponRogueSwitch = oltPonAttribute.getPonRogueSwitch();
    }

    public void modifyAttribute(OltSniAttribute oltSniAttribute) {
        this.sniPortName = oltSniAttribute.getSniPortName();// sni端口名
        if (oltSniAttribute.getSniMediaType() != null && oltSniAttribute.getSniMediaType() >= 0
                && oltSniAttribute.getSniMediaType() < MEDIATYPE.length) {
            this.sniMediaType = MEDIATYPE[oltSniAttribute.getSniMediaType()];// 媒介类型
        }
        if (oltSniAttribute.getSniAutoNegotiationStatus() != null && oltSniAttribute.getSniAutoNegotiationStatus() >= 0
                && oltSniAttribute.getSniAutoNegotiationStatus() < AUTONEGO.length) {
            this.sniAutoNegotiationStatus = AUTONEGO[oltSniAttribute.getSniAutoNegotiationStatus()];// 自协商状态
        }
        this.sniAutoNegotiationMode = oltSniAttribute.getSniAutoNegotiationMode();// 自协商模式
        this.sniMacAddrLearnMaxNum = oltSniAttribute.getSniMacAddrLearnMaxNum();// MAC地址最大学习数
        this.sniIsolationEnable = oltSniAttribute.getSniIsolationEnable();// 隔离使能
        this.sniLastStatusChangeTime = oltSniAttribute.getSniLastStatusChangeTime();// 状态变更时间
        this.sniOperationStatus = oltSniAttribute.getSniOperationStatus();
        this.sniAdminStatus = oltSniAttribute.getSniAdminStatus();
        this.sniPerfStats15minuteEnable = oltSniAttribute.getSniPerfStats15minuteEnable();
        this.sniPerfStats24hourEnable = oltSniAttribute.getSniPerfStats24hourEnable();
        this.topSniAttrCardNo = oltSniAttribute.getTopSniAttrCardNo();
        this.topSniAttrPortNo = oltSniAttribute.getTopSniAttrPortNo();
        this.topSniAttrFlowCtrlEnable = oltSniAttribute.getTopSniAttrFlowCtrlEnable();
        this.topSniAttrIngressRate = oltSniAttribute.getTopSniAttrIngressRate();
        this.topSniAttrEgressRate = oltSniAttribute.getTopSniAttrEgressRate();
        this.stpPortEnabled = oltSniAttribute.getStpPortEnabled();
        this.stpPortRstpProtocolMigration = oltSniAttribute.getStpPortRstpProtocolMigration();
    }

    public Long getPortId() {
        return portId;
    }

    public void setPortId(Long portId) {
        this.portId = portId;
    }

    public String getSniPortName() {
        return sniPortName;
    }

    public void setSniPortName(String sniPortName) {
        this.sniPortName = sniPortName;
    }

    public String getSniMediaType() {
        return sniMediaType;
    }

    public void setSniMediaType(String sniMediaType) {
        this.sniMediaType = sniMediaType;
    }

    public String getSniAutoNegotiationStatus() {
        return sniAutoNegotiationStatus;
    }

    public void setSniAutoNegotiationStatus(String sniAutoNegotiationStatus) {
        this.sniAutoNegotiationStatus = sniAutoNegotiationStatus;
    }

    public Integer getSniAutoNegotiationMode() {
        return sniAutoNegotiationMode;
    }

    public void setSniAutoNegotiationMode(Integer sniAutoNegotiationMode) {
        this.sniAutoNegotiationMode = sniAutoNegotiationMode;
    }

    public Long getSniMacAddrLearnMaxNum() {
        return sniMacAddrLearnMaxNum;
    }

    public void setSniMacAddrLearnMaxNum(Long sniMacAddrLearnMaxNum) {
        this.sniMacAddrLearnMaxNum = sniMacAddrLearnMaxNum;
    }

    public Integer getSniIsolationEnable() {
        return sniIsolationEnable;
    }

    public void setSniIsolationEnable(Integer sniIsolationEnable) {
        this.sniIsolationEnable = sniIsolationEnable;
    }

    public Integer getPonPortMaxOnuNumSupport() {
        return ponPortMaxOnuNumSupport;
    }

    public void setPonPortMaxOnuNumSupport(Integer ponPortMaxOnuNumSupport) {
        this.ponPortMaxOnuNumSupport = ponPortMaxOnuNumSupport;
    }

    public Integer getPonPortUpOnuNum() {
        return ponPortUpOnuNum;
    }

    public void setPonPortUpOnuNum(Integer ponPortUpOnuNum) {
        this.ponPortUpOnuNum = ponPortUpOnuNum;
    }

    public Integer getPonPortEncryptKeyExchangeTime() {
        return ponPortEncryptKeyExchangeTime;
    }

    public void setPonPortEncryptKeyExchangeTime(Integer ponPortEncryptKeyExchangeTime) {
        this.ponPortEncryptKeyExchangeTime = ponPortEncryptKeyExchangeTime;
    }

    public Integer getPonPortEncryptMode() {
        return ponPortEncryptMode;
    }

    public void setPonPortEncryptMode(Integer ponPortEncryptMode) {
        this.ponPortEncryptMode = ponPortEncryptMode;
    }

    public String getPonPortType() {
        return ponPortType;
    }

    public void setPonPortType(String ponPortType) {
        this.ponPortType = ponPortType;
    }

    public Integer getPonRogueSwitch() {
        return ponRogueSwitch;
    }

    public void setPonRogueSwitch(Integer ponRogueSwitch) {
        this.ponRogueSwitch = ponRogueSwitch;
    }

    public Integer getMaxDsBandwidth() {
        return maxDsBandwidth;
    }

    public void setMaxDsBandwidth(Integer maxDsBandwidth) {
        this.maxDsBandwidth = maxDsBandwidth;
    }

    public Integer getActualDsBandwidthInUse() {
        return actualDsBandwidthInUse;
    }

    public void setActualDsBandwidthInUse(Integer actualDsBandwidthInUse) {
        this.actualDsBandwidthInUse = actualDsBandwidthInUse;
    }

    public Integer getRemainDsBandwidth() {
        return remainDsBandwidth;
    }

    public void setRemainDsBandwidth(Integer remainDsBandwidth) {
        this.remainDsBandwidth = remainDsBandwidth;
    }

    public Long getPonPortMacAddrLearnMaxNum() {
        return ponPortMacAddrLearnMaxNum;
    }

    public void setPonPortMacAddrLearnMaxNum(Long ponPortMacAddrLearnMaxNum) {
        this.ponPortMacAddrLearnMaxNum = ponPortMacAddrLearnMaxNum;
    }

    public Integer getMaxUsBandwidth() {
        return maxUsBandwidth;
    }

    public void setMaxUsBandwidth(Integer maxUsBandwidth) {
        this.maxUsBandwidth = maxUsBandwidth;
    }

    public Integer getActualUsBandwidthInUse() {
        return actualUsBandwidthInUse;
    }

    public void setActualUsBandwidthInUse(Integer actualUsBandwidthInUse) {
        this.actualUsBandwidthInUse = actualUsBandwidthInUse;
    }

    public Integer getRemainUsBandwidth() {
        return remainUsBandwidth;
    }

    public void setRemainUsBandwidth(Integer remainUsBandwidth) {
        this.remainUsBandwidth = remainUsBandwidth;
    }

    public Long getPortIndex() {
        return portIndex;
    }

    public void setPortIndex(Long portIndex) {
        setSlotRealIndex(EponIndex.getSlotNo(portIndex));
        setPortRealIndex(EponIndex.getSniNo(portIndex));
        this.portIndex = portIndex;
    }

    public String getPortSubType() {
        return portSubType;
    }

    public void setPortSubType(String portSubType) {
        this.portSubType = portSubType;
    }

    public Long getPortRealIndex() {
        return portRealIndex;
    }

    public void setPortRealIndex(Long portRealIndex) {
        this.portRealIndex = portRealIndex;
    }

    public Long getSlotRealIndex() {
        return slotRealIndex;
    }

    public void setSlotRealIndex(Long slotRealIndex) {
        this.slotRealIndex = slotRealIndex;
    }

    /**
     * @return the sniLastStatusChangeTime
     */
    public Long getSniLastStatusChangeTime() {
        return sniLastStatusChangeTime;
    }

    /**
     * @param sniLastStatusChangeTime
     *            the sniLastStatusChangeTime to set
     */
    public void setSniLastStatusChangeTime(Long sniLastStatusChangeTime) {
        this.sniLastStatusChangeTime = sniLastStatusChangeTime;
    }

    /**
     * @return the sniOperationStatus
     */
    public Integer getSniOperationStatus() {
        return sniOperationStatus;
    }

    /**
     * @param sniOperationStatus
     *            the sniOperationStatus to set
     */
    public void setSniOperationStatus(Integer sniOperationStatus) {
        this.sniOperationStatus = sniOperationStatus;
    }

    /**
     * @return the sniAdminStatus
     */
    public Integer getSniAdminStatus() {
        return sniAdminStatus;
    }

    /**
     * @param sniAdminStatus
     *            the sniAdminStatus to set
     */
    public void setSniAdminStatus(Integer sniAdminStatus) {
        this.sniAdminStatus = sniAdminStatus;
    }

    /**
     * @return the sniPerfStats15minuteEnable
     */
    public Integer getSniPerfStats15minuteEnable() {
        return sniPerfStats15minuteEnable;
    }

    /**
     * @param sniPerfStats15minuteEnable
     *            the sniPerfStats15minuteEnable to set
     */
    public void setSniPerfStats15minuteEnable(Integer sniPerfStats15minuteEnable) {
        this.sniPerfStats15minuteEnable = sniPerfStats15minuteEnable;
    }

    /**
     * @return the sniPerfStats24hourEnable
     */
    public Integer getSniPerfStats24hourEnable() {
        return sniPerfStats24hourEnable;
    }

    /**
     * @param sniPerfStats24hourEnable
     *            the sniPerfStats24hourEnable to set
     */
    public void setSniPerfStats24hourEnable(Integer sniPerfStats24hourEnable) {
        this.sniPerfStats24hourEnable = sniPerfStats24hourEnable;
    }

    /**
     * @return the topSniAttrCardNo
     */
    public Long getTopSniAttrCardNo() {
        return topSniAttrCardNo;
    }

    /**
     * @param topSniAttrCardNo
     *            the topSniAttrCardNo to set
     */
    public void setTopSniAttrCardNo(Long topSniAttrCardNo) {
        this.topSniAttrCardNo = topSniAttrCardNo;
    }

    /**
     * @return the topSniAttrPortNo
     */
    public Long getTopSniAttrPortNo() {
        return topSniAttrPortNo;
    }

    /**
     * @param topSniAttrPortNo
     *            the topSniAttrPortNo to set
     */
    public void setTopSniAttrPortNo(Long topSniAttrPortNo) {
        this.topSniAttrPortNo = topSniAttrPortNo;
    }

    /**
     * @return the topSniAttrFlowCtrlEnable
     */
    public Integer getTopSniAttrFlowCtrlEnable() {
        return topSniAttrFlowCtrlEnable;
    }

    /**
     * @param topSniAttrFlowCtrlEnable
     *            the topSniAttrFlowCtrlEnable to set
     */
    public void setTopSniAttrFlowCtrlEnable(Integer topSniAttrFlowCtrlEnable) {
        this.topSniAttrFlowCtrlEnable = topSniAttrFlowCtrlEnable;
    }

    /**
     * @return the topSniAttrIngressRate
     */
    public Integer getTopSniAttrIngressRate() {
        return topSniAttrIngressRate;
    }

    /**
     * @param topSniAttrIngressRate
     *            the topSniAttrIngressRate to set
     */
    public void setTopSniAttrIngressRate(Integer topSniAttrIngressRate) {
        this.topSniAttrIngressRate = topSniAttrIngressRate;
    }

    /**
     * @return the topSniAttrEgressRate
     */
    public Integer getTopSniAttrEgressRate() {
        return topSniAttrEgressRate;
    }

    /**
     * @param topSniAttrEgressRate
     *            the topSniAttrEgressRate to set
     */
    public void setTopSniAttrEgressRate(Integer topSniAttrEgressRate) {
        this.topSniAttrEgressRate = topSniAttrEgressRate;
    }

    /**
     * @return the ponOperationStatus
     */
    public Integer getPonOperationStatus() {
        return ponOperationStatus;
    }

    /**
     * @param ponOperationStatus
     *            the ponOperationStatus to set
     */
    public void setPonOperationStatus(Integer ponOperationStatus) {
        this.ponOperationStatus = ponOperationStatus;
    }

    /**
     * @return the ponPortAdminStatus
     */
    public Integer getPonPortAdminStatus() {
        return ponPortAdminStatus;
    }

    /**
     * @param ponPortAdminStatus
     *            the ponPortAdminStatus to set
     */
    public void setPonPortAdminStatus(Integer ponPortAdminStatus) {
        this.ponPortAdminStatus = ponPortAdminStatus;
    }

    /**
     * @return the ponPortIsolationEnable
     */
    public Integer getPonPortIsolationEnable() {
        return ponPortIsolationEnable;
    }

    /**
     * @param ponPortIsolationEnable
     *            the ponPortIsolationEnable to set
     */
    public void setPonPortIsolationEnable(Integer ponPortIsolationEnable) {
        this.ponPortIsolationEnable = ponPortIsolationEnable;
    }

    /**
     * @return the perfStats15minuteEnable
     */
    public Integer getPerfStats15minuteEnable() {
        return perfStats15minuteEnable;
    }

    /**
     * @param perfStats15minuteEnable
     *            the perfStats15minuteEnable to set
     */
    public void setPerfStats15minuteEnable(Integer perfStats15minuteEnable) {
        this.perfStats15minuteEnable = perfStats15minuteEnable;
    }

    /**
     * @return the perfStats24hourEnable
     */
    public Integer getPerfStats24hourEnable() {
        return perfStats24hourEnable;
    }

    /**
     * @param perfStats24hourEnable
     *            the perfStats24hourEnable to set
     */
    public void setPerfStats24hourEnable(Integer perfStats24hourEnable) {
        this.perfStats24hourEnable = perfStats24hourEnable;
    }

    /**
     * @return the sMaxDsBandwidth
     */
    public String getSMaxDsBandwidth() {
        return sMaxDsBandwidth;
    }

    /**
     * @param sMaxDsBandwidth
     *            the sMaxDsBandwidth to set
     */
    public void setSMaxDsBandwidth(String sMaxDsBandwidth) {
        this.sMaxDsBandwidth = sMaxDsBandwidth;
    }

    /**
     * @return the sMaxUsBandwidth
     */
    public String getSMaxUsBandwidth() {
        return sMaxUsBandwidth;
    }

    /**
     * @param sMaxUsBandwidth
     *            the sMaxUsBandwidth to set
     */
    public void setSMaxUsBandwidth(String sMaxUsBandwidth) {
        this.sMaxUsBandwidth = sMaxUsBandwidth;
    }

    /**
     * @return the stpPortEnabled
     */
    public Integer getStpPortEnabled() {
        return stpPortEnabled;
    }

    /**
     * @param stpPortEnabled
     *            the stpPortEnabled to set
     */
    public void setStpPortEnabled(Integer stpPortEnabled) {
        this.stpPortEnabled = stpPortEnabled;
    }

    /**
     * @return the stpPortRstpProtocolMigration
     */
    public Integer getStpPortRstpProtocolMigration() {
        return stpPortRstpProtocolMigration;
    }

    /**
     * @param stpPortRstpProtocolMigration
     *            the stpPortRstpProtocolMigration to set
     */
    public void setStpPortRstpProtocolMigration(Integer stpPortRstpProtocolMigration) {
        this.stpPortRstpProtocolMigration = stpPortRstpProtocolMigration;
    }

    /**
     * @return the topSniAttrActualSpeed
     */
    public Integer getTopSniAttrActualSpeed() {
        return topSniAttrActualSpeed;
    }

    /**
     * @param topSniAttrActualSpeed
     *            the topSniAttrActualSpeed to set
     */
    public void setTopSniAttrActualSpeed(Integer topSniAttrActualSpeed) {
        this.topSniAttrActualSpeed = topSniAttrActualSpeed;
    }

    /**
     * @return the isStandbyPort
     */
    public boolean isStandbyPort() {
        return isStandbyPort;
    }

    /**
     * @param isStandbyPort
     *            the isStandbyPort to set
     */
    public void setStandbyPort(boolean isStandbyPort) {
        this.isStandbyPort = isStandbyPort;
    }

    public Integer getPonSpeedMode() {
        return ponSpeedMode;
    }

    public void setPonSpeedMode(Integer ponSpeedMode) {
        this.ponSpeedMode = ponSpeedMode;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("EponPort");
        sb.append("{actualDsBandwidthInUse=").append(actualDsBandwidthInUse);
        sb.append(", portId=").append(portId);
        sb.append(", portIndex=").append(portIndex);
        sb.append(", slotRealIndex=").append(slotRealIndex);
        sb.append(", portRealIndex=").append(portRealIndex);
        sb.append(", portSubType='").append(portSubType).append('\'');
        sb.append(", sniPortName='").append(sniPortName).append('\'');
        sb.append(", sniMediaType='").append(sniMediaType).append('\'');
        sb.append(", sniAutoNegotiationStatus='").append(sniAutoNegotiationStatus).append('\'');
        sb.append(", sniAutoNegotiationMode=").append(sniAutoNegotiationMode);
        sb.append(", sniMacAddrLearnMaxNum=").append(sniMacAddrLearnMaxNum);
        sb.append(", sniIsolationEnable=").append(sniIsolationEnable);
        sb.append(", sniLastStatusChangeTime=").append(sniLastStatusChangeTime);
        sb.append(", sniOperationStatus=").append(sniOperationStatus);
        sb.append(", sniAdminStatus=").append(sniAdminStatus);
        sb.append(", sniPerfStats15minuteEnable=").append(sniPerfStats15minuteEnable);
        sb.append(", sniPerfStats24hourEnable=").append(sniPerfStats24hourEnable);
        sb.append(", topSniAttrCardNo=").append(topSniAttrCardNo);
        sb.append(", topSniAttrPortNo=").append(topSniAttrPortNo);
        sb.append(", topSniAttrFlowCtrlEnable=").append(topSniAttrFlowCtrlEnable);
        sb.append(", topSniAttrIngressRate=").append(topSniAttrIngressRate);
        sb.append(", topSniAttrEgressRate=").append(topSniAttrEgressRate);
        sb.append(", ponPortType='").append(ponPortType).append('\'');
        sb.append(", ponPortMaxOnuNumSupport=").append(ponPortMaxOnuNumSupport);
        sb.append(", ponPortUpOnuNum=").append(ponPortUpOnuNum);
        sb.append(", ponPortEncryptMode=").append(ponPortEncryptMode);
        sb.append(", ponPortEncryptKeyExchangeTime=").append(ponPortEncryptKeyExchangeTime);
        sb.append(", maxDsBandwidth=").append(maxDsBandwidth);
        sb.append(", sMaxDsBandwidth=").append(sMaxDsBandwidth);
        sb.append(", remainDsBandwidth=").append(remainDsBandwidth);
        sb.append(", ponPortMacAddrLearnMaxNum=").append(ponPortMacAddrLearnMaxNum);
        sb.append(", maxUsBandwidth=").append(maxUsBandwidth);
        sb.append(", sMaxUsBandwidth=").append(sMaxUsBandwidth);
        sb.append(", actualUsBandwidthInUse=").append(actualUsBandwidthInUse);
        sb.append(", remainUsBandwidth=").append(remainUsBandwidth);
        sb.append(", ponOperationStatus=").append(ponOperationStatus);
        sb.append(", ponPortAdminStatus=").append(ponPortAdminStatus);
        sb.append(", ponPortIsolationEnable=").append(ponPortIsolationEnable);
        sb.append(", perfStats15minuteEnable=").append(perfStats15minuteEnable);
        sb.append(", perfStats24hourEnable=").append(perfStats24hourEnable);
        sb.append('}');
        return sb.toString();
    }

    /**
     * Compares this object with the specified object for order. Returns a
     * negative integer, zero, or a positive integer as this object is less
     * than, equal to, or greater than the specified object.
     * <p/>
     * <p>
     * The implementor must ensure <tt>sgn(x.compareTo(y)) ==
     * -sgn(y.compareTo(x))</tt> for all <tt>x</tt> and <tt>y</tt>. (This
     * implies that <tt>x.compareTo(y)</tt> must throw an exception iff
     * <tt>y.compareTo(x)</tt> throws an exception.)
     * <p/>
     * <p>
     * The implementor must also ensure that the relation is transitive:
     * <tt>(x.compareTo(y)&gt;0 &amp;&amp; y.compareTo(z)&gt;0)</tt> implies
     * <tt>x.compareTo(z)&gt;0</tt>.
     * <p/>
     * <p>
     * Finally, the implementor must ensure that <tt>x.compareTo(y)==0</tt>
     * implies that <tt>sgn(x.compareTo(z)) == sgn(y.compareTo(z))</tt>, for all
     * <tt>z</tt>.
     * <p/>
     * <p>
     * It is strongly recommended, but <i>not</i> strictly required that
     * <tt>(x.compareTo(y)==0) == (x.equals(y))</tt>. Generally speaking, any
     * class that implements the <tt>Comparable</tt> interface and violates this
     * condition should clearly indicate this fact. The recommended language is
     * "Note: this class has a natural ordering that is inconsistent with
     * equals."
     * <p/>
     * <p>
     * In the foregoing description, the notation <tt>sgn(</tt><i>expression</i>
     * <tt>)</tt> designates the mathematical <i>signum</i> function, which is
     * defined to return one of <tt>-1</tt>, <tt>0</tt>, or <tt>1</tt> according
     * to whether the value of <i>expression</i> is negative, zero or positive.
     * 
     * @param o
     *            the object to be compared.
     * @return a negative integer, zero, or a positive integer as this object is
     *         less than, equal to, or greater than the specified object.
     * @throws ClassCastException
     *             if the specified object's type prevents it from being
     *             compared to this object.
     */
    @Override
    public int compareTo(Object o) {
        EponPort port = (EponPort) o;
        if (this.getPortIndex() > port.getPortIndex()) {
            return 1;
        } else if (this.getPortIndex().equals(port.getPortIndex())) {
            return 0;
        } else {
            return -1;
        }
    }
}
