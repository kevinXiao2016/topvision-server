/***********************************************************************
 * $Id: AclRuleTable.java,v1.0 2013年10月25日 下午5:46:18 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.acl.domain;

import java.util.List;

import com.topvision.ems.epon.utils.EponUtil;
import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.annotation.TableProperty;
import com.topvision.framework.common.IpUtils;
import com.topvision.framework.common.MacUtils;
import com.topvision.framework.dao.mybatis.AliasesSuperType;
import com.topvision.framework.domain.BaseEntity;

/**
 * @author Bravin
 * @created @2013年10月25日-下午5:46:18
 *
 */
@TableProperty(tables = { "default" })
public class AclRuleTable extends BaseEntity implements AliasesSuperType {
    private static final long serialVersionUID = 6814478795382970588L;
    private Long entityId;
    @SnmpProperty(table = "default", oid = "1.3.6.1.4.1.32285.11.2.3.3.2.3.1.1", index = true)
    private Integer topAclRuleListIndex;
    @SnmpProperty(table = "default", oid = "1.3.6.1.4.1.32285.11.2.3.3.2.3.1.2", index = true)
    private Integer topAclRuleIndex;
    @SnmpProperty(table = "default", oid = "1.3.6.1.4.1.32285.11.2.3.3.2.3.1.3", writable = true, type = "OctetString")
    private String topMatchedFieldSelection;
    private List<Integer> topMatchedFieldSelectionSymbol;
    @SnmpProperty(table = "default", oid = "1.3.6.1.4.1.32285.11.2.3.3.2.3.1.4", writable = true, type = "OctetString")
    private String topMatchedSrcMac;
    private Long topMatchedSrcMacLong;
    @SnmpProperty(table = "default", oid = "1.3.6.1.4.1.32285.11.2.3.3.2.3.1.5", writable = true, type = "OctetString")
    private String topMatchedSrcMacMask;
    private Long topMatchedSrcMacMaskLong;
    @SnmpProperty(table = "default", oid = "1.3.6.1.4.1.32285.11.2.3.3.2.3.1.6", writable = true, type = "OctetString")
    private String topMatchedDstMac;
    private Long topMatchedDstMacLong;
    @SnmpProperty(table = "default", oid = "1.3.6.1.4.1.32285.11.2.3.3.2.3.1.7", writable = true, type = "OctetString")
    private String topMatchedDstMacMask;
    private Long topMatchedDstMacMaskLong;
    @SnmpProperty(table = "default", oid = "1.3.6.1.4.1.32285.11.2.3.3.2.3.1.8", writable = true, type = "Integer32")
    private Integer topMatchedStartSVid;
    @SnmpProperty(table = "default", oid = "1.3.6.1.4.1.32285.11.2.3.3.2.3.1.9", writable = true, type = "Integer32")
    private Integer topMatchedEndSVid;
    @SnmpProperty(table = "default", oid = "1.3.6.1.4.1.32285.11.2.3.3.2.3.1.10", writable = true, type = "Integer32")
    private Integer topMatchedStartCVid;
    @SnmpProperty(table = "default", oid = "1.3.6.1.4.1.32285.11.2.3.3.2.3.1.11", writable = true, type = "Integer32")
    private Integer topMatchedEndCVid;
    @SnmpProperty(table = "default", oid = "1.3.6.1.4.1.32285.11.2.3.3.2.3.1.12", writable = true, type = "Integer32")
    private Integer topMatchedOuterCos;
    @SnmpProperty(table = "default", oid = "1.3.6.1.4.1.32285.11.2.3.3.2.3.1.13", writable = true, type = "Integer32")
    private Integer topMatchedInnerCos;
    @SnmpProperty(table = "default", oid = "1.3.6.1.4.1.32285.11.2.3.3.2.3.1.14", writable = true, type = "Integer32")
    private Integer topMatchedOuterTpid;
    @SnmpProperty(table = "default", oid = "1.3.6.1.4.1.32285.11.2.3.3.2.3.1.15", writable = true, type = "Integer32")
    private Integer topMatchedInnerTpid;
    @SnmpProperty(table = "default", oid = "1.3.6.1.4.1.32285.11.2.3.3.2.3.1.16", writable = true, type = "Integer32")
    private Integer topMatchedEthernetType;
    @SnmpProperty(table = "default", oid = "1.3.6.1.4.1.32285.11.2.3.3.2.3.1.17", writable = true, type = "IpAddress")
    private String topMatchedSrcIP;
    private Long topMatchedSrcIPLong;
    @SnmpProperty(table = "default", oid = "1.3.6.1.4.1.32285.11.2.3.3.2.3.1.18", writable = true, type = "IpAddress")
    private String topMatchedSrcIPMask;
    private Long topMatchedSrcIPMaskLong;
    @SnmpProperty(table = "default", oid = "1.3.6.1.4.1.32285.11.2.3.3.2.3.1.19", writable = true, type = "IpAddress")
    private String topMatchedDstIP;
    private Long topMatchedDstIPLong;
    @SnmpProperty(table = "default", oid = "1.3.6.1.4.1.32285.11.2.3.3.2.3.1.20", writable = true, type = "IpAddress")
    private String topMatchedDstIPMask;
    private Long topMatchedDstIPMaskLong;
    @SnmpProperty(table = "default", oid = "1.3.6.1.4.1.32285.11.2.3.3.2.3.1.21", writable = true, type = "Integer32")
    private Integer topMatchedL3ProtocolClass;
    @SnmpProperty(table = "default", oid = "1.3.6.1.4.1.32285.11.2.3.3.2.3.1.22", writable = true, type = "Integer32")
    private Integer topMatchedIpProtocol;
    @SnmpProperty(table = "default", oid = "1.3.6.1.4.1.32285.11.2.3.3.2.3.1.23", writable = true, type = "Integer32")
    private Integer topMatchedDscp;
    @SnmpProperty(table = "default", oid = "1.3.6.1.4.1.32285.11.2.3.3.2.3.1.24", writable = true, type = "Integer32")
    private Integer topMatchedTos;
    @SnmpProperty(table = "default", oid = "1.3.6.1.4.1.32285.11.2.3.3.2.3.1.25", writable = true, type = "Integer32")
    private Integer topMatchedStartSrcPort;
    @SnmpProperty(table = "default", oid = "1.3.6.1.4.1.32285.11.2.3.3.2.3.1.26", writable = true, type = "Integer32")
    private Integer topMatchedEndSrcPort;
    @SnmpProperty(table = "default", oid = "1.3.6.1.4.1.32285.11.2.3.3.2.3.1.27", writable = true, type = "Integer32")
    private Integer topMatchedStartDstPort;
    @SnmpProperty(table = "default", oid = "1.3.6.1.4.1.32285.11.2.3.3.2.3.1.28", writable = true, type = "Integer32")
    private Integer topMatchedEndDstPort;
    @SnmpProperty(table = "default", oid = "1.3.6.1.4.1.32285.11.2.3.3.2.3.1.29", writable = true, type = "OctetString")
    private String topAclAction;
    private List<Integer> topAclActionList;
    @SnmpProperty(table = "default", oid = "1.3.6.1.4.1.32285.11.2.3.3.2.3.1.30", writable = true, type = "OctetString")
    private String topAclActionParameter;
    private List<Integer> topAclActionParameterValueList;
    @SnmpProperty(table = "default", oid = "1.3.6.1.4.1.32285.11.2.3.3.2.3.1.31", writable = true, type = "Integer32")
    private Integer topAclRuleRowStatus;

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public String getTopAclAction() {
        return topAclAction;
    }

    public void setTopAclAction(String topAclAction) {
        this.topAclAction = topAclAction;
        topAclActionList = EponUtil.getSymbolListFromMib(topAclAction);
    }

    public List<Integer> getTopAclActionList() {
        return topAclActionList;
    }

    public void setTopAclActionList(List<Integer> topAclActionList) {
        this.topAclActionList = topAclActionList;
        // 此处的ACL ACTION 用2个字节标示
        topAclAction = EponUtil.getMibByteFromSymbolInfoListByStartZero(2, topAclActionList);
    }

    public Integer getTopAclRuleIndex() {
        return topAclRuleIndex;
    }

    public void setTopAclRuleIndex(Integer topAclRuleIndex) {
        this.topAclRuleIndex = topAclRuleIndex;
    }

    public Integer getTopAclRuleListIndex() {
        return topAclRuleListIndex;
    }

    public void setTopAclRuleListIndex(Integer topAclRuleListIndex) {
        this.topAclRuleListIndex = topAclRuleListIndex;
    }

    public Integer getTopAclRuleRowStatus() {
        return topAclRuleRowStatus;
    }

    public void setTopAclRuleRowStatus(Integer topAclRuleRowStatus) {
        this.topAclRuleRowStatus = topAclRuleRowStatus;
    }

    public Integer getTopMatchedDscp() {
        return topMatchedDscp;
    }

    public void setTopMatchedDscp(Integer topMatchedDscp) {
        this.topMatchedDscp = topMatchedDscp;
    }

    public String getTopMatchedDstMac() {
        return topMatchedDstMac;
    }

    public void setTopMatchedDstMac(String topMatchedDstMac) {
        this.topMatchedDstMac = topMatchedDstMac;
        if (topMatchedDstMac != null) {
            topMatchedDstMacLong = new MacUtils(this.topMatchedDstMac).longValue();
        }
    }

    public Long getTopMatchedDstMacLong() {
        return topMatchedDstMacLong;
    }

    public void setTopMatchedDstMacLong(Long topMatchedDstMacLong) {
        this.topMatchedDstMacLong = topMatchedDstMacLong;
        if (topMatchedDstMacLong != null) {
            this.topMatchedDstMac = new MacUtils(topMatchedDstMacLong).toString(MacUtils.MAOHAO).toUpperCase();
        }
    }

    public String getTopMatchedDstMacMask() {
        return topMatchedDstMacMask;
    }

    public void setTopMatchedDstMacMask(String topMatchedDstMacMask) {
        this.topMatchedDstMacMask = topMatchedDstMacMask;
        if (topMatchedDstMacMask != null) {
            topMatchedDstMacMaskLong = new MacUtils(this.topMatchedDstMacMask).longValue();
        }
    }

    public Long getTopMatchedDstMacMaskLong() {
        return topMatchedDstMacMaskLong;
    }

    public void setTopMatchedDstMacMaskLong(Long topMatchedDstMacMaskLong) {
        this.topMatchedDstMacMaskLong = topMatchedDstMacMaskLong;
        if (topMatchedDstMacMaskLong != null) {
            topMatchedDstMacMask = new MacUtils(topMatchedDstMacMaskLong).toString(MacUtils.MAOHAO).toUpperCase();
        }
    }

    public Integer getTopMatchedEndCVid() {
        return topMatchedEndCVid;
    }

    public void setTopMatchedEndCVid(Integer topMatchedEndCVid) {
        this.topMatchedEndCVid = topMatchedEndCVid;
    }

    public Integer getTopMatchedEndDstPort() {
        return topMatchedEndDstPort;
    }

    public void setTopMatchedEndDstPort(Integer topMatchedEndDstPort) {
        this.topMatchedEndDstPort = topMatchedEndDstPort;
    }

    public Integer getTopMatchedEndSrcPort() {
        return topMatchedEndSrcPort;
    }

    public void setTopMatchedEndSrcPort(Integer topMatchedEndSrcPort) {
        this.topMatchedEndSrcPort = topMatchedEndSrcPort;
    }

    public Integer getTopMatchedEndSVid() {
        return topMatchedEndSVid;
    }

    public void setTopMatchedEndSVid(Integer topMatchedEndSVid) {
        this.topMatchedEndSVid = topMatchedEndSVid;
    }

    public Integer getTopMatchedEthernetType() {
        return topMatchedEthernetType;
    }

    public void setTopMatchedEthernetType(Integer topMatchedEthernetType) {
        this.topMatchedEthernetType = topMatchedEthernetType;
    }

    public String getTopMatchedFieldSelection() {
        return topMatchedFieldSelection;
    }

    public void setTopMatchedFieldSelection(String topMatchedFieldSelection) {
        this.topMatchedFieldSelection = topMatchedFieldSelection;
        topMatchedFieldSelectionSymbol = EponUtil.getSymbolListFromMib(topMatchedFieldSelection);
    }

    /**
     * @return the topMatchedFieldSelectionSymbol
     */
    public List<Integer> getTopMatchedFieldSelectionSymbol() {
        return topMatchedFieldSelectionSymbol;
    }

    /**
     * @param topMatchedFieldSelectionSymbol
     *            the topMatchedFieldSelectionSymbol to set
     */
    public void setTopMatchedFieldSelectionSymbol(List<Integer> topMatchedFieldSelectionSymbol) {
        this.topMatchedFieldSelectionSymbol = topMatchedFieldSelectionSymbol;
        topMatchedFieldSelection = EponUtil.getMibByteFromSymbolInfoListByStartZero(4, topMatchedFieldSelectionSymbol);
    }

    public Integer getTopMatchedInnerCos() {
        return topMatchedInnerCos;
    }

    public void setTopMatchedInnerCos(Integer topMatchedInnerCos) {
        this.topMatchedInnerCos = topMatchedInnerCos;
    }

    public Integer getTopMatchedInnerTpid() {
        return topMatchedInnerTpid;
    }

    public void setTopMatchedInnerTpid(Integer topMatchedInnerTpid) {
        this.topMatchedInnerTpid = topMatchedInnerTpid;
    }

    public Integer getTopMatchedIpProtocol() {
        return topMatchedIpProtocol;
    }

    public void setTopMatchedIpProtocol(Integer topMatchedIpProtocol) {
        this.topMatchedIpProtocol = topMatchedIpProtocol;
    }

    public Integer getTopMatchedL3ProtocolClass() {
        return topMatchedL3ProtocolClass;
    }

    public void setTopMatchedL3ProtocolClass(Integer topMatchedL3ProtocolClass) {
        this.topMatchedL3ProtocolClass = topMatchedL3ProtocolClass;
    }

    public Integer getTopMatchedOuterCos() {
        return topMatchedOuterCos;
    }

    public void setTopMatchedOuterCos(Integer topMatchedOuterCos) {
        this.topMatchedOuterCos = topMatchedOuterCos;
    }

    public Integer getTopMatchedOuterTpid() {
        return topMatchedOuterTpid;
    }

    public void setTopMatchedOuterTpid(Integer topMatchedOuterTpid) {
        this.topMatchedOuterTpid = topMatchedOuterTpid;
    }

    public String getTopMatchedSrcMac() {
        return topMatchedSrcMac;
    }

    public void setTopMatchedSrcMac(String topMatchedSrcMac) {
        if (topMatchedSrcMac != null) {
            this.topMatchedSrcMac = EponUtil.getMacStringFromNoISOControl(topMatchedSrcMac);
            topMatchedSrcMacLong = new MacUtils(this.topMatchedSrcMac).longValue();
        }
    }

    public Long getTopMatchedSrcMacLong() {
        return topMatchedSrcMacLong;
    }

    public void setTopMatchedSrcMacLong(Long topMatchedSrcMacLong) {
        this.topMatchedSrcMacLong = topMatchedSrcMacLong;
        if (topMatchedSrcMacLong != null) {
            this.topMatchedSrcMac = new MacUtils(topMatchedSrcMacLong).toString(MacUtils.MAOHAO).toUpperCase();
        }
    }

    public String getTopMatchedSrcMacMask() {
        return topMatchedSrcMacMask;
    }

    public void setTopMatchedSrcMacMask(String topMatchedSrcMacMask) {
        this.topMatchedSrcMacMask = EponUtil.getMacStringFromNoISOControl(topMatchedSrcMacMask);
        if (topMatchedSrcMacMask != null) {
            topMatchedSrcMacMaskLong = new MacUtils(this.topMatchedSrcMacMask).longValue();
        }
    }

    public Long getTopMatchedSrcMacMaskLong() {
        return topMatchedSrcMacMaskLong;
    }

    public void setTopMatchedSrcMacMaskLong(Long topMatchedSrcMacMaskLong) {
        this.topMatchedSrcMacMaskLong = topMatchedSrcMacMaskLong;
        if (topMatchedSrcMacMaskLong != null) {
            topMatchedSrcMacMask = new MacUtils(topMatchedSrcMacMaskLong).toString(MacUtils.MAOHAO).toUpperCase();
        }
    }

    public Integer getTopMatchedStartCVid() {
        return topMatchedStartCVid;
    }

    public void setTopMatchedStartCVid(Integer topMatchedStartCVid) {
        this.topMatchedStartCVid = topMatchedStartCVid;
    }

    public Integer getTopMatchedStartDstPort() {
        return topMatchedStartDstPort;
    }

    public void setTopMatchedStartDstPort(Integer topMatchedStartDstPort) {
        this.topMatchedStartDstPort = topMatchedStartDstPort;
    }

    public Integer getTopMatchedStartSrcPort() {
        return topMatchedStartSrcPort;
    }

    public void setTopMatchedStartSrcPort(Integer topMatchedStartSrcPort) {
        this.topMatchedStartSrcPort = topMatchedStartSrcPort;
    }

    public Integer getTopMatchedStartSVid() {
        return topMatchedStartSVid;
    }

    public void setTopMatchedStartSVid(Integer topMatchedStartSVid) {
        this.topMatchedStartSVid = topMatchedStartSVid;
    }

    public Integer getTopMatchedTos() {
        return topMatchedTos;
    }

    public void setTopMatchedTos(Integer topMatchedTos) {
        this.topMatchedTos = topMatchedTos;
    }

    /**
     * @return the topAclActionParameter
     */
    public String getTopAclActionParameter() {
        return topAclActionParameter;
    }

    /**
     * @param topAclActionParameter
     *            the topAclActionParameter to set
     */
    public void setTopAclActionParameter(String topAclActionParameter) {
        this.topAclActionParameter = topAclActionParameter;
        if (topAclActionParameter.length() > 0) {
            topAclActionParameterValueList = EponUtil.getTopAclActionParameterValueList(topAclActionParameter);
        }
    }

    /**
     * @return the topAclActionParameterValueList
     */
    public List<Integer> getTopAclActionParameterValueList() {
        return topAclActionParameterValueList;
    }

    /**
     * @param topAclActionParameterValueList
     *            the topAclActionParameterValueList to set
     */
    public void setTopAclActionParameterValueList(List<Integer> topAclActionParameterValueList) {
        this.topAclActionParameterValueList = topAclActionParameterValueList;
        topAclActionParameter = EponUtil.getTopAclActionParameterFromValueList(topAclActionParameterValueList);
    }

    /**
     * @return the topMatchedSrcIP
     */
    public String getTopMatchedSrcIP() {
        return topMatchedSrcIP;
    }

    /**
     * @param topMatchedSrcIP
     *            the topMatchedSrcIP to set
     */
    public void setTopMatchedSrcIP(String topMatchedSrcIP) {
        this.topMatchedSrcIP = topMatchedSrcIP;
        if (topMatchedSrcIP != null) {
            topMatchedSrcIPLong = new IpUtils(topMatchedSrcIP).longValue();
        }
    }

    /**
     * @return the topMatchedSrcIPLong
     */
    public Long getTopMatchedSrcIPLong() {
        return topMatchedSrcIPLong;
    }

    /**
     * @param topMatchedSrcIPLong
     *            the topMatchedSrcIPLong to set
     */
    public void setTopMatchedSrcIPLong(Long topMatchedSrcIPLong) {
        this.topMatchedSrcIPLong = topMatchedSrcIPLong;
        if (topMatchedSrcIPLong != null) {
            topMatchedSrcIP = new IpUtils(topMatchedSrcIPLong).toString();
        }
    }

    /**
     * @return the topMatchedSrcIPMask
     */
    public String getTopMatchedSrcIPMask() {
        return topMatchedSrcIPMask;
    }

    /**
     * @param topMatchedSrcIPMask
     *            the topMatchedSrcIPMask to set
     */
    public void setTopMatchedSrcIPMask(String topMatchedSrcIPMask) {
        this.topMatchedSrcIPMask = topMatchedSrcIPMask;
        if (topMatchedSrcIPMask != null) {
            topMatchedSrcIPMaskLong = new IpUtils(topMatchedSrcIPMask).longValue();
        }
    }

    /**
     * @return the topMatchedSrcIPMaskLong
     */
    public Long getTopMatchedSrcIPMaskLong() {
        return topMatchedSrcIPMaskLong;
    }

    /**
     * @param topMatchedSrcIPMaskLong
     *            the topMatchedSrcIPMaskLong to set
     */
    public void setTopMatchedSrcIPMaskLong(Long topMatchedSrcIPMaskLong) {
        this.topMatchedSrcIPMaskLong = topMatchedSrcIPMaskLong;
        if (topMatchedSrcIPMaskLong != null) {
            topMatchedSrcIPMask = new IpUtils(topMatchedSrcIPMaskLong).toString();
        }
    }

    /**
     * @return the topMatchedDstIP
     */
    public String getTopMatchedDstIP() {
        return topMatchedDstIP;
    }

    /**
     * @param topMatchedDstIP
     *            the topMatchedDstIP to set
     */
    public void setTopMatchedDstIP(String topMatchedDstIP) {
        this.topMatchedDstIP = topMatchedDstIP;
        if (topMatchedDstIP != null) {
            topMatchedDstIPLong = new IpUtils(topMatchedDstIP).longValue();
        }
    }

    /**
     * @return the topMatchedDstIPLong
     */
    public Long getTopMatchedDstIPLong() {
        return topMatchedDstIPLong;
    }

    /**
     * @param topMatchedDstIPLong
     *            the topMatchedDstIPLong to set
     */
    public void setTopMatchedDstIPLong(Long topMatchedDstIPLong) {
        this.topMatchedDstIPLong = topMatchedDstIPLong;
        if (topMatchedDstIPLong != null) {
            topMatchedDstIP = new IpUtils(topMatchedDstIPLong).toString();
        }
    }

    /**
     * @return the topMatchedDstIPMask
     */
    public String getTopMatchedDstIPMask() {
        return topMatchedDstIPMask;
    }

    /**
     * @param topMatchedDstIPMask
     *            the topMatchedDstIPMask to set
     */
    public void setTopMatchedDstIPMask(String topMatchedDstIPMask) {
        this.topMatchedDstIPMask = topMatchedDstIPMask;
        if (topMatchedDstIPMask != null) {
            topMatchedDstIPMaskLong = new IpUtils(topMatchedDstIPMask).longValue();
        }
    }

    /**
     * @return the topMatchedDstIPMaskLong
     */
    public Long getTopMatchedDstIPMaskLong() {
        return topMatchedDstIPMaskLong;
    }

    /**
     * @param topMatchedDstIPMaskLong
     *            the topMatchedDstIPMaskLong to set
     */
    public void setTopMatchedDstIPMaskLong(Long topMatchedDstIPMaskLong) {
        this.topMatchedDstIPMaskLong = topMatchedDstIPMaskLong;
        if (topMatchedDstIPMaskLong != null) {
            topMatchedDstIPMask = new IpUtils(topMatchedDstIPMaskLong).toString();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("AclRuleTable [entityId=");
        builder.append(entityId);
        builder.append(", topAclRuleListIndex=");
        builder.append(topAclRuleListIndex);
        builder.append(", topAclRuleIndex=");
        builder.append(topAclRuleIndex);
        builder.append(", topMatchedFieldSelection=");
        builder.append(topMatchedFieldSelection);
        builder.append(", topMatchedFieldSelectionSymbol=");
        builder.append(topMatchedFieldSelectionSymbol);
        builder.append(", topMatchedSrcMac=");
        builder.append(topMatchedSrcMac);
        builder.append(", topMatchedSrcMacLong=");
        builder.append(topMatchedSrcMacLong);
        builder.append(", topMatchedSrcMacMask=");
        builder.append(topMatchedSrcMacMask);
        builder.append(", topMatchedSrcMacMaskLong=");
        builder.append(topMatchedSrcMacMaskLong);
        builder.append(", topMatchedDstMac=");
        builder.append(topMatchedDstMac);
        builder.append(", topMatchedDstMacLong=");
        builder.append(topMatchedDstMacLong);
        builder.append(", topMatchedDstMacMask=");
        builder.append(topMatchedDstMacMask);
        builder.append(", topMatchedDstMacMaskLong=");
        builder.append(topMatchedDstMacMaskLong);
        builder.append(", topMatchedStartSVid=");
        builder.append(topMatchedStartSVid);
        builder.append(", topMatchedEndSVid=");
        builder.append(topMatchedEndSVid);
        builder.append(", topMatchedStartCVid=");
        builder.append(topMatchedStartCVid);
        builder.append(", topMatchedEndCVid=");
        builder.append(topMatchedEndCVid);
        builder.append(", topMatchedOuterCos=");
        builder.append(topMatchedOuterCos);
        builder.append(", topMatchedInnerCos=");
        builder.append(topMatchedInnerCos);
        builder.append(", topMatchedOuterTpid=");
        builder.append(topMatchedOuterTpid);
        builder.append(", topMatchedInnerTpid=");
        builder.append(topMatchedInnerTpid);
        builder.append(", topMatchedEthernetType=");
        builder.append(topMatchedEthernetType);
        builder.append(", topMatchedSrcIP=");
        builder.append(topMatchedSrcIP);
        builder.append(", topMatchedSrcIPMask=");
        builder.append(topMatchedSrcIPMask);
        builder.append(", topMatchedDstIP=");
        builder.append(topMatchedDstIP);
        builder.append(", topMatchedDstIPMask=");
        builder.append(topMatchedDstIPMask);
        builder.append(", topMatchedL3ProtocolClass=");
        builder.append(topMatchedL3ProtocolClass);
        builder.append(", topMatchedIpProtocol=");
        builder.append(topMatchedIpProtocol);
        builder.append(", topMatchedDscp=");
        builder.append(topMatchedDscp);
        builder.append(", topMatchedTos=");
        builder.append(topMatchedTos);
        builder.append(", topMatchedStartSrcPort=");
        builder.append(topMatchedStartSrcPort);
        builder.append(", topMatchedEndSrcPort=");
        builder.append(topMatchedEndSrcPort);
        builder.append(", topMatchedStartDstPort=");
        builder.append(topMatchedStartDstPort);
        builder.append(", topMatchedEndDstPort=");
        builder.append(topMatchedEndDstPort);
        builder.append(", topAclAction=");
        builder.append(topAclAction);
        builder.append(", topAclActionList=");
        builder.append(topAclActionList);
        builder.append(", topAclActionParameter=");
        builder.append(topAclActionParameter);
        builder.append(", topAclActionParameterValueList=");
        builder.append(topAclActionParameterValueList);
        builder.append(", topAclRuleRowStatus=");
        builder.append(topAclRuleRowStatus);
        builder.append("]");
        return builder.toString();
    }

}
