/***********************************************************************
 * $Id: CmcQosPktClassInfo.java,v1.0 2011-10-20 下午03:55:22 $
 * 
 * @author: Dosion_Huang
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.qos.facade.domain;

import java.io.Serializable;

import org.apache.ibatis.type.Alias;

import com.topvision.framework.constants.Symbol;
import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * 
 * 包分类器
 * 
 * @author Dosion_Huang
 * @created @2011-10-20-下午03:55:22
 * 
 */
@Alias("cmcQosPktClassInfo")
public class CmcQosPktClassInfo implements Serializable, AliasesSuperType {
    private static final long serialVersionUID = 7128208800913008515L;
    private Long servicePacketId;
    private Long cmcId;
    private Long sId;
    @SnmpProperty(oid = "1.3.6.1.2.1.2.2.1.1", index = true)
    private Long cmcIndex;
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.7.1.3.1.1", index = true)
    private Long serviceFlowId;
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.7.1.1.1.1", index = true)
    private Integer classId;
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.7.1.1.1.2")
    // 方向 1：DownStream 2：UpStream
    private Integer classDirection;
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.7.1.1.1.3")
    private Integer classPriority;
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.7.1.1.1.4")
    private String classIpTosLow;
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.7.1.1.1.5")
    private String classIpTosHigh;
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.7.1.1.1.6")
    private String classIpTosMask = "";
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.7.1.1.1.7")
    private Integer classIpProtocol;
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.7.1.1.1.8")
    private String classIpSourceAddr = "";
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.7.1.1.1.9")
    private String classIpSourceMask = "";
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.7.1.1.1.10")
    private String classIpDestAddr = "";
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.7.1.1.1.11")
    private String classIpDestMask = "";
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.7.1.1.1.12")
    private Integer classSourcePortStart;
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.7.1.1.1.13")
    private Integer classSourcePortEnd;
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.7.1.1.1.14")
    private Integer classDestPortStart;
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.7.1.1.1.15")
    private Integer classDestPortEnd;
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.7.1.1.1.16")
    private String classDestMacAddr;
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.7.1.1.1.17")
    private String classDestMacMask;
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.7.1.1.1.18")
    private String classSourceMacAddr;
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.7.1.1.1.19")
    private Integer classEnetProtocolType;
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.7.1.1.1.20")
    private Integer classEnetProtocol;
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.7.1.1.1.22")
    private Integer classUserPriLow;
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.7.1.1.1.23")
    private Integer classUserPriHigh;
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.7.1.1.1.24")
    private Integer classVlanId;
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.7.1.1.1.25")
    private Integer classState;
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.7.1.1.1.26")
    private Integer classPkts;
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.7.1.1.1.27")
    private String classBitMap;
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.7.1.1.1.28")
    private Integer classInetSourceAddrType;
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.7.1.1.1.29")
    private String classInetSourceAddr = "";
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.7.1.1.1.30")
    private Integer classInetSourceMaskType;
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.7.1.1.1.31")
    private String classInetSourceMask = "";
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.7.1.1.1.32")
    private Integer classInetDestAddrType;
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.7.1.1.1.33")
    private String classInetDestAddr = "";
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.7.1.1.1.34")
    private Integer classInetDestMaskType;
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.7.1.1.1.35")
    private String classInetDestMask = "";
    private String docsQosPktClassEnetProtocolTypeName;
    private String docsQosPktClassBitMapName;
    private String docsQosPktClassInetSourceAddrTypeName;
    private String docsQosPktClassInetSourceMaskTypeName;
    private String docsQosPktClassInetDestAddrTypeName;
    private String docsQosPktClassInetDestMaskTypeName;
    public static final String[] ENETPROTOCOL = { "none", "ethertype", "dsap", "mac", "all" };
    public static final String[] INETADDRESSTYPE = { "unknown", "ipv4", "ipv6", "ipv4z", "ipv6z", "", "", "", "", "",
            "", "", "", "", "", "", "dns" };
    public static final String[] BITMAP = { "rulePriority", "activationState", "ipTos", "ipProtocol", "ipSourceAddr",
            "ipSourceMask", "ipDestAddr", "ipDestMask", "sourcePortStart", "sourcePortEnd", "destPortStart",
            "destPortEnd", "destMac", "sourceMac", "ethertype", "userPri", "vlanId" };

    /**
     * @return the servicePacketId
     */
    public Long getServicePacketId() {
        return servicePacketId;
    }

    /**
     * @param servicePacketId
     *            the servicePacketId to set
     */
    public void setServicePacketId(Long servicePacketId) {
        this.servicePacketId = servicePacketId;
    }

    /**
     * @return the cmcId
     */
    public Long getCmcId() {
        return cmcId;
    }

    /**
     * @param cmcId
     *            the cmcId to set
     */
    public void setCmcId(Long cmcId) {
        this.cmcId = cmcId;
    }

    /**
     * @return the sId
     */
    public Long getsId() {
        return sId;
    }

    /**
     * @param sId
     *            the sId to set
     */
    public void setsId(Long sId) {
        this.sId = sId;
    }

    /**
     * @return the cmcIndex
     */
    public Long getCmcIndex() {
        return cmcIndex;
    }

    /**
     * @param cmcIndex
     *            the cmcIndex to set
     */
    public void setCmcIndex(Long cmcIndex) {
        this.cmcIndex = cmcIndex;
    }

    /**
     * @return the docsQosServiceFlowId
     */
    public Long getServiceFlowId() {
        return serviceFlowId;
    }

    /**
     * @param serviceFlowId
     *            the docsQosServiceFlowId to set
     */
    public void setServiceFlowId(Long serviceFlowId) {
        this.serviceFlowId = serviceFlowId;
    }

    /**
     * @return the docsQosPktClassId
     */
    public Integer getClassId() {
        return classId;
    }

    /**
     * @param classId
     *            the docsQosPktClassId to set
     */
    public void setClassId(Integer classId) {
        this.classId = classId;
    }

    /**
     * @return the docsQosPktClassDirection
     */
    public Integer getClassDirection() {
        return classDirection;
    }

    /**
     * @param classDirection
     *            the docsQosPktClassDirection to set
     */
    public void setClassDirection(Integer classDirection) {
        this.classDirection = classDirection;
    }

    /**
     * @return the docsQosPktClassPriority
     */
    public Integer getClassPriority() {
        return classPriority;
    }

    /**
     * @param classPriority
     *            the docsQosPktClassPriority to set
     */
    public void setClassPriority(Integer classPriority) {
        this.classPriority = classPriority;
    }

    /**
     * @return the docsQosPktClassIpTosLow
     */
    public String getClassIpTosLow() {
        return classIpTosLow;
    }

    /**
     * @param classIpTosLow
     *            the docsQosPktClassIpTosLow to set
     */
    public void setClassIpTosLow(String classIpTosLow) {
        this.classIpTosLow = classIpTosLow;
    }

    /**
     * @return the docsQosPktClassIpTosHigh
     */
    public String getClassIpTosHigh() {
        return classIpTosHigh;
    }

    /**
     * @param classIpTosHigh
     *            the docsQosPktClassIpTosHigh to set
     */
    public void setClassIpTosHigh(String classIpTosHigh) {
        this.classIpTosHigh = classIpTosHigh;
    }

    /**
     * @return the docsQosPktClassIpTosMask
     */
    public String getClassIpTosMask() {
        return classIpTosMask;
    }

    /**
     * @param classIpTosMask
     *            the docsQosPktClassIpTosMask to set
     */
    public void setClassIpTosMask(String classIpTosMask) {
        this.classIpTosMask = classIpTosMask;
    }

    /**
     * @return the docsQosPktClassIpProtocol
     */
    public Integer getClassIpProtocol() {
        return classIpProtocol;
    }

    /**
     * @param classIpProtocol
     *            the docsQosPktClassIpProtocol to set
     */
    public void setClassIpProtocol(Integer classIpProtocol) {
        this.classIpProtocol = classIpProtocol;
    }

    /**
     * @return the docsQosPktClassIpSourceAddr
     */
    public String getClassIpSourceAddr() {
        return classIpSourceAddr;
    }

    /**
     * @param classIpSourceAddr
     *            the docsQosPktClassIpSourceAddr to set
     */
    public void setClassIpSourceAddr(String classIpSourceAddr) {
        this.classIpSourceAddr = classIpSourceAddr;
    }

    /**
     * @return the docsQosPktClassIpSourceMask
     */
    public String getClassIpSourceMask() {
        return classIpSourceMask;
    }

    /**
     * @param classIpSourceMask
     *            the docsQosPktClassIpSourceMask to set
     */
    public void setClassIpSourceMask(String classIpSourceMask) {
        this.classIpSourceMask = classIpSourceMask;
    }

    /**
     * @return the docsQosPktClassIpDestAddr
     */
    public String getClassIpDestAddr() {
        return classIpDestAddr;
    }

    /**
     * @param classIpDestAddr
     *            the docsQosPktClassIpDestAddr to set
     */
    public void setClassIpDestAddr(String classIpDestAddr) {
        this.classIpDestAddr = classIpDestAddr;
    }

    /**
     * @return the docsQosPktClassIpDestMask
     */
    public String getClassIpDestMask() {
        return classIpDestMask;
    }

    /**
     * @param classIpDestMask
     *            the docsQosPktClassIpDestMask to set
     */
    public void setClassIpDestMask(String classIpDestMask) {
        this.classIpDestMask = classIpDestMask;
    }

    /**
     * @return the docsQosPktClassSourcePortStart
     */
    public Integer getClassSourcePortStart() {
        return classSourcePortStart;
    }

    /**
     * @param classSourcePortStart
     *            the docsQosPktClassSourcePortStart to set
     */
    public void setClassSourcePortStart(Integer classSourcePortStart) {
        this.classSourcePortStart = classSourcePortStart;
    }

    /**
     * @return the docsQosPktClassSourcePortEnd
     */
    public Integer getClassSourcePortEnd() {
        return classSourcePortEnd;
    }

    /**
     * @param classSourcePortEnd
     *            the docsQosPktClassSourcePortEnd to set
     */
    public void setClassSourcePortEnd(Integer classSourcePortEnd) {
        this.classSourcePortEnd = classSourcePortEnd;
    }

    /**
     * @return the docsQosPktClassDestPortStart
     */
    public Integer getClassDestPortStart() {
        return classDestPortStart;
    }

    /**
     * @param classDestPortStart
     *            the docsQosPktClassDestPortStart to set
     */
    public void setClassDestPortStart(Integer classDestPortStart) {
        this.classDestPortStart = classDestPortStart;
    }

    /**
     * @return the docsQosPktClassDestPortEnd
     */
    public Integer getClassDestPortEnd() {
        return classDestPortEnd;
    }

    /**
     * @param classDestPortEnd
     *            the docsQosPktClassDestPortEnd to set
     */
    public void setClassDestPortEnd(Integer classDestPortEnd) {
        this.classDestPortEnd = classDestPortEnd;
    }

    /**
     * @return the docsQosPktClassDestMacAddr
     */
    public String getClassDestMacAddr() {
        return classDestMacAddr;
    }

    /**
     * @param classDestMacAddr
     *            the docsQosPktClassDestMacAddr to set
     */
    public void setClassDestMacAddr(String classDestMacAddr) {
        this.classDestMacAddr = classDestMacAddr;
    }

    /**
     * @return the docsQosPktClassDestMacMask
     */
    public String getClassDestMacMask() {
        return classDestMacMask;
    }

    /**
     * @param classDestMacMask
     *            the docsQosPktClassDestMacMask to set
     */
    public void setClassDestMacMask(String classDestMacMask) {
        this.classDestMacMask = classDestMacMask;
    }

    /**
     * @return the docsQosPktClassSourceMacAddr
     */
    public String getClassSourceMacAddr() {
        return classSourceMacAddr;
    }

    /**
     * @param classSourceMacAddr
     *            the docsQosPktClassSourceMacAddr to set
     */
    public void setClassSourceMacAddr(String classSourceMacAddr) {
        this.classSourceMacAddr = classSourceMacAddr;
    }

    /**
     * @return the docsQosPktClassEnetProtocolType
     */
    public Integer getClassEnetProtocolType() {
        return classEnetProtocolType;
    }

    /**
     * @param classEnetProtocolType
     *            the docsQosPktClassEnetProtocolType to set
     */
    public void setClassEnetProtocolType(Integer classEnetProtocolType) {
        this.classEnetProtocolType = classEnetProtocolType;
        /*
         * if (this.classEnetProtocolType != null) this.docsQosPktClassEnetProtocolTypeName =
         * ENETPROTOCOL[classEnetProtocolType];
         */
    }

    /**
     * @return the docsQosPktClassEnetProtocol
     */
    public Integer getClassEnetProtocol() {
        return classEnetProtocol;
    }

    /**
     * @param classEnetProtocol
     *            the docsQosPktClassEnetProtocol to set
     */
    public void setClassEnetProtocol(Integer classEnetProtocol) {
        this.classEnetProtocol = classEnetProtocol;
    }

    /**
     * @return the docsQosPktClassUserPriLow
     */
    public Integer getClassUserPriLow() {
        return classUserPriLow;
    }

    /**
     * @param classUserPriLow
     *            the docsQosPktClassUserPriLow to set
     */
    public void setClassUserPriLow(Integer classUserPriLow) {
        this.classUserPriLow = classUserPriLow;
    }

    /**
     * @return the docsQosPktClassUserPriHigh
     */
    public Integer getClassUserPriHigh() {
        return classUserPriHigh;
    }

    /**
     * @param classUserPriHigh
     *            the docsQosPktClassUserPriHigh to set
     */
    public void setClassUserPriHigh(Integer classUserPriHigh) {
        this.classUserPriHigh = classUserPriHigh;
    }

    /**
     * @return the docsQosPktClassVlanId
     */
    public Integer getClassVlanId() {
        return classVlanId;
    }

    /**
     * @param classVlanId
     *            the docsQosPktClassVlanId to set
     */
    public void setClassVlanId(Integer classVlanId) {
        this.classVlanId = classVlanId;
    }

    /**
     * @return the docsQosPktClassState
     */
    public Integer getClassState() {
        return classState;
    }

    /**
     * @param classState
     *            the docsQosPktClassState to set
     */
    public void setClassState(Integer classState) {
        this.classState = classState;
    }

    /**
     * @return the docsQosPktClassPkts
     */
    public Integer getClassPkts() {
        return classPkts;
    }

    /**
     * @param classPkts
     *            the docsQosPktClassPkts to set
     */
    public void setClassPkts(Integer classPkts) {
        this.classPkts = classPkts;
    }

    /**
     * @return the docsQosPktClassBitMap
     */
    public String getClassBitMap() {
        return classBitMap;
    }

    /**
     * @param classBitMap
     *            the docsQosPktClassBitMap to set
     */
    public void setClassBitMap(String classBitMap) {
        this.classBitMap = classBitMap;
        /*
         * this.docsQosPktClassBitMapName = CmcUtil.turnBitsToString( classBitMap, BITMAP);
         */
    }

    /**
     * @return the docsQosPktClassInetSourceAddrType
     */
    public Integer getClassInetSourceAddrType() {
        return classInetSourceAddrType;
    }

    /**
     * @param classInetSourceAddrType
     *            the docsQosPktClassInetSourceAddrType to set
     */
    public void setClassInetSourceAddrType(Integer classInetSourceAddrType) {
        this.classInetSourceAddrType = classInetSourceAddrType;
        /*
         * if (this.classInetSourceAddrType != null) { this.docsQosPktClassInetSourceAddrTypeName =
         * INETADDRESSTYPE[classInetSourceAddrType]; }
         */
    }

    /**
     * @return the docsQosPktClassInetSourceAddr
     */
    public String getClassInetSourceAddr() {
        return classInetSourceAddr;
    }

    /**
     * @param classInetSourceAddr
     *            the docsQosPktClassInetSourceAddr to set
     */
    public void setClassInetSourceAddr(String classInetSourceAddr) {
        this.classInetSourceAddr = classInetSourceAddr;
    }

    /**
     * @return the docsQosPktClassInetSourceMaskType
     */
    public Integer getClassInetSourceMaskType() {
        return classInetSourceMaskType;
    }

    /**
     * @param classInetSourceMaskType
     *            the docsQosPktClassInetSourceMaskType to set
     */
    public void setClassInetSourceMaskType(Integer classInetSourceMaskType) {
        this.classInetSourceMaskType = classInetSourceMaskType;
        /*
         * if (this.classInetSourceMaskType != null) this.docsQosPktClassInetSourceAddrTypeName =
         * INETADDRESSTYPE[classInetSourceMaskType];
         */
    }

    /**
     * @return the docsQosPktClassInetSourceMask
     */
    public String getClassInetSourceMask() {
        return classInetSourceMask;
    }

    /**
     * @param classInetSourceMask
     *            the docsQosPktClassInetSourceMask to set
     */
    public void setClassInetSourceMask(String classInetSourceMask) {
        this.classInetSourceMask = classInetSourceMask;
    }

    /**
     * @return the docsQosPktClassInetDestAddrType
     */
    public Integer getClassInetDestAddrType() {
        return classInetDestAddrType;
    }

    /**
     * @param classInetDestAddrType
     *            the docsQosPktClassInetDestAddrType to set
     */
    public void setClassInetDestAddrType(Integer classInetDestAddrType) {
        this.classInetDestAddrType = classInetDestAddrType;
        /*
         * if (this.classInetDestAddrType != null) this.docsQosPktClassInetDestAddrTypeName =
         * INETADDRESSTYPE[classInetDestAddrType];
         */
    }

    /**
     * @return the docsQosPktClassInetDestAddr
     */
    public String getClassInetDestAddr() {
        return classInetDestAddr;
    }

    /**
     * @param classInetDestAddr
     *            the docsQosPktClassInetDestAddr to set
     */
    public void setClassInetDestAddr(String classInetDestAddr) {
        this.classInetDestAddr = classInetDestAddr;
    }

    /**
     * @return the docsQosPktClassInetDestMaskType
     */
    public Integer getClassInetDestMaskType() {
        return classInetDestMaskType;
    }

    /**
     * @param classInetDestMaskType
     *            the docsQosPktClassInetDestMaskType to set
     */
    public void setClassInetDestMaskType(Integer classInetDestMaskType) {
        this.classInetDestMaskType = classInetDestMaskType;
        /*
         * if (this.classInetDestMaskType != null) this.docsQosPktClassInetDestMaskTypeName =
         * INETADDRESSTYPE[classInetDestMaskType];
         */
    }

    /**
     * @return the docsQosPktClassInetDestMask
     */
    public String getClassInetDestMask() {
        return classInetDestMask;
    }

    /**
     * @param classInetDestMask
     *            the docsQosPktClassInetDestMask to set
     */
    public void setClassInetDestMask(String classInetDestMask) {
        this.classInetDestMask = classInetDestMask;
    }

    /**
     * @return the docsQosPktClassEnetProtocolTypeName
     */
    public String getDocsQosPktClassEnetProtocolTypeName() {
        return docsQosPktClassEnetProtocolTypeName;
    }

    /**
     * @return the docsQosPktClassBitMapName
     */
    public String getDocsQosPktClassBitMapName() {
        return docsQosPktClassBitMapName;
    }

    /**
     * @return the docsQosPktClassInetSourceAddrTypeName
     */
    public String getDocsQosPktClassInetSourceAddrTypeName() {
        return docsQosPktClassInetSourceAddrTypeName;
    }

    /**
     * @return the docsQosPktClassInetSourceMaskTypeName
     */
    public String getDocsQosPktClassInetSourceMaskTypeName() {
        return docsQosPktClassInetSourceMaskTypeName;
    }

    /**
     * @return the docsQosPktClassInetDestAddrTypeName
     */
    public String getDocsQosPktClassInetDestAddrTypeName() {
        return docsQosPktClassInetDestAddrTypeName;
    }

    /**
     * @return the docsQosPktClassInetDestMaskTypeName
     */
    public String getDocsQosPktClassInetDestMaskTypeName() {
        return docsQosPktClassInetDestMaskTypeName;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("CmcQosPktClassInfo [servicePacketId=");
        builder.append(servicePacketId);
        builder.append(", cmcId=");
        builder.append(cmcId);
        builder.append(", sId=");
        builder.append(sId);
        builder.append(", cmcIndex=");
        builder.append(cmcIndex);
        builder.append(", serviceFlowId=");
        builder.append(serviceFlowId);
        builder.append(", classId=");
        builder.append(classId);
        builder.append(", classDirection=");
        builder.append(classDirection);
        builder.append(", classPriority=");
        builder.append(classPriority);
        builder.append(", classIpTosLow=");
        builder.append(classIpTosLow);
        builder.append(", classIpTosHigh=");
        builder.append(classIpTosHigh);
        builder.append(", classIpTosMask=");
        builder.append(classIpTosMask);
        builder.append(", classIpProtocol=");
        builder.append(classIpProtocol);
        builder.append(", classIpSourceAddr=");
        builder.append(classIpSourceAddr);
        builder.append(", classIpSourceMask=");
        builder.append(classIpSourceMask);
        builder.append(", classIpDestAddr=");
        builder.append(classIpDestAddr);
        builder.append(", classIpDestMask=");
        builder.append(classIpDestMask);
        builder.append(", classSourcePortStart=");
        builder.append(classSourcePortStart);
        builder.append(", classSourcePortEnd=");
        builder.append(classSourcePortEnd);
        builder.append(", classDestPortStart=");
        builder.append(classDestPortStart);
        builder.append(", classDestPortEnd=");
        builder.append(classDestPortEnd);
        builder.append(", classDestMacAddr=");
        builder.append(classDestMacAddr);
        builder.append(", classDestMacMask=");
        builder.append(classDestMacMask);
        builder.append(", classSourceMacAddr=");
        builder.append(classSourceMacAddr);
        builder.append(", classEnetProtocolType=");
        builder.append(classEnetProtocolType);
        builder.append(", classEnetProtocol=");
        builder.append(classEnetProtocol);
        builder.append(", classUserPriLow=");
        builder.append(classUserPriLow);
        builder.append(", classUserPriHigh=");
        builder.append(classUserPriHigh);
        builder.append(", classVlanId=");
        builder.append(classVlanId);
        builder.append(", classState=");
        builder.append(classState);
        builder.append(", classPkts=");
        builder.append(classPkts);
        builder.append(", classBitMap=");
        builder.append(classBitMap);
        builder.append(", classInetSourceAddrType=");
        builder.append(classInetSourceAddrType);
        builder.append(", classInetSourceAddr=");
        builder.append(classInetSourceAddr);
        builder.append(", classInetSourceMaskType=");
        builder.append(classInetSourceMaskType);
        builder.append(", classInetSourceMask=");
        builder.append(classInetSourceMask);
        builder.append(", classInetDestAddrType=");
        builder.append(classInetDestAddrType);
        builder.append(", classInetDestAddr=");
        builder.append(classInetDestAddr);
        builder.append(", classInetDestMaskType=");
        builder.append(classInetDestMaskType);
        builder.append(", classInetDestMask=");
        builder.append(classInetDestMask);
        builder.append(", docsQosPktClassEnetProtocolTypeName=");
        builder.append(docsQosPktClassEnetProtocolTypeName);
        builder.append(", docsQosPktClassBitMapName=");
        builder.append(docsQosPktClassBitMapName);
        builder.append(", docsQosPktClassInetSourceAddrTypeName=");
        builder.append(docsQosPktClassInetSourceAddrTypeName);
        builder.append(", docsQosPktClassInetSourceMaskTypeName=");
        builder.append(docsQosPktClassInetSourceMaskTypeName);
        builder.append(", docsQosPktClassInetDestAddrTypeName=");
        builder.append(docsQosPktClassInetDestAddrTypeName);
        builder.append(", docsQosPktClassInetDestMaskTypeName=");
        builder.append(docsQosPktClassInetDestMaskTypeName);
        builder.append(Symbol.BRACKET_RIGHT);
        return builder.toString();
    }

}
