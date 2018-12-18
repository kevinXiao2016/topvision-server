/***********************************************************************
 * $Id: OltSniRedirect.java,v1.0 2011-11-5 下午04:20:42 $
 * 
 * @author: huqiao
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.olt.domain;

import java.io.Serializable;

import com.topvision.framework.utils.EponIndex;
import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author huqiao
 * @created @2011-11-5-下午04:20:42
 * 
 */
public class OltSniRedirect implements Serializable, AliasesSuperType {

    private static final long serialVersionUID = 8422667594872769514L;
    private Long entityId;
    private Long topSniRedirectGroupSrcPortId;
    private Long srcIndex;
    private String srcPortName;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.2.2.1.1", index = true)
    private Long topSniRedirectGroupSrcPort;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.2.2.1.2", writable = true, type = "Integer32")
    private Integer topSniRedirectGroupDirection;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.2.2.1.3", writable = true, type = "OctetString")
    private String topSniRedirectGroupName;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.2.2.1.4", writable = true, type = "Integer32")
    private Long topSniRedirectGroupDstPort;
    private Long topSniRedirectGroupDstPortId;
    private Long dstIndex;
    private String dstPortName;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.2.2.1.5", writable = true, type = "Integer32")
    private Integer topSniRedirectGroupRowstatus;

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
     * @return the topSniRedirectGroupSrcPortId
     */
    public Long getTopSniRedirectGroupSrcPortId() {
        return topSniRedirectGroupSrcPortId;
    }

    /**
     * @param topSniRedirectGroupSrcPortId
     *            the topSniRedirectGroupSrcPortId to set
     */
    public void setTopSniRedirectGroupSrcPortId(Long topSniRedirectGroupSrcPortId) {
        this.topSniRedirectGroupSrcPortId = topSniRedirectGroupSrcPortId;
    }

    /**
     * @return the topSniRedirectGroupSrcPortIndex
     */
    public Long getSrcIndex() {
        return srcIndex;
    }

    /**
     * @param srcIndex
     *            the topSniRedirectGroupSrcPortIndex to set
     */
    public void setSrcIndex(Long srcIndex) {
        this.srcIndex = srcIndex;
        if (srcIndex != null) {
            topSniRedirectGroupSrcPort = EponIndex.getMibSniRedirectIndexBySniIndex(srcIndex);
            srcPortName = EponIndex.getSlotNo(srcIndex) + "/" + EponIndex.getSniNo(srcIndex);
        }
    }

    /**
     * @return the srcPortName
     */
    public String getSrcPortName() {
        return srcPortName;
    }

    /**
     * @param srcPortName
     *            the srcPortName to set
     */
    public void setSrcPortName(String srcPortName) {
        this.srcPortName = srcPortName;
    }

    /**
     * @return the topSniRedirectGroupDstPortId
     */
    public Long getTopSniRedirectGroupDstPortId() {
        return topSniRedirectGroupDstPortId;
    }

    /**
     * @param topSniRedirectGroupDstPortId
     *            the topSniRedirectGroupDstPortId to set
     */
    public void setTopSniRedirectGroupDstPortId(Long topSniRedirectGroupDstPortId) {
        this.topSniRedirectGroupDstPortId = topSniRedirectGroupDstPortId;
    }

    /**
     * @return the topSniRedirectGroupDstPortIndex
     */
    public Long getDstIndex() {
        return dstIndex;
    }

    /**
     * @param dstIndex
     *            the topSniRedirectGroupDstPortIndex to set
     */
    public void setDstIndex(Long dstIndex) {
        this.dstIndex = dstIndex;
        if (dstIndex != null) {
            topSniRedirectGroupDstPort = EponIndex.getMibSniRedirectIndexBySniIndex(dstIndex);
            dstPortName = EponIndex.getSlotNo(dstIndex) + "/" + EponIndex.getSniNo(dstIndex);
        }
    }

    /**
     * @return the topSniRedirectGroupSrcPort
     */
    public Long getTopSniRedirectGroupSrcPort() {
        return topSniRedirectGroupSrcPort;
    }

    /**
     * @param topSniRedirectGroupSrcPort
     *            the topSniRedirectGroupSrcPort to set
     */
    public void setTopSniRedirectGroupSrcPort(Long topSniRedirectGroupSrcPort) {
        this.topSniRedirectGroupSrcPort = topSniRedirectGroupSrcPort;
        srcIndex = EponIndex.getSniIndexByMibSniRedirectIndex(topSniRedirectGroupSrcPort);
        srcPortName = EponIndex.getSlotNo(srcIndex) + "/" + EponIndex.getSniNo(srcIndex);
    }

    /**
     * @return the topSniRedirectGroupDirection
     */
    public Integer getTopSniRedirectGroupDirection() {
        return topSniRedirectGroupDirection;
    }

    /**
     * @param topSniRedirectGroupDirection
     *            the topSniRedirectGroupDirection to set
     */
    public void setTopSniRedirectGroupDirection(Integer topSniRedirectGroupDirection) {
        this.topSniRedirectGroupDirection = topSniRedirectGroupDirection;
    }

    /**
     * @return the topSniRedirectGroupName
     */
    public String getTopSniRedirectGroupName() {
        return topSniRedirectGroupName;
    }

    /**
     * @param topSniRedirectGroupName
     *            the topSniRedirectGroupName to set
     */
    public void setTopSniRedirectGroupName(String topSniRedirectGroupName) {
        this.topSniRedirectGroupName = topSniRedirectGroupName;
    }

    /**
     * @return the topSniRedirectGroupDstPort
     */
    public Long getTopSniRedirectGroupDstPort() {
        return topSniRedirectGroupDstPort;
    }

    /**
     * @param topSniRedirectGroupDstPort
     *            the topSniRedirectGroupDstPort to set
     */
    public void setTopSniRedirectGroupDstPort(Long topSniRedirectGroupDstPort) {
        this.topSniRedirectGroupDstPort = topSniRedirectGroupDstPort;
        dstIndex = EponIndex.getSniIndexByMibSniRedirectIndex(topSniRedirectGroupDstPort);
        dstPortName = EponIndex.getSlotNo(dstIndex) + "/" + EponIndex.getSniNo(dstIndex);
    }

    /**
     * @return the dstPortName
     */
    public String getDstPortName() {
        return dstPortName;
    }

    /**
     * @param dstPortName
     *            the dstPortName to set
     */
    public void setDstPortName(String dstPortName) {
        this.dstPortName = dstPortName;
    }

    /**
     * @return the topSniRedirectGroupRowstatus
     */
    public Integer getTopSniRedirectGroupRowstatus() {
        return topSniRedirectGroupRowstatus;
    }

    /**
     * @param topSniRedirectGroupRowstatus
     *            the topSniRedirectGroupRowstatus to set
     */
    public void setTopSniRedirectGroupRowstatus(Integer topSniRedirectGroupRowstatus) {
        this.topSniRedirectGroupRowstatus = topSniRedirectGroupRowstatus;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("OltSniRedirect [entityId=");
        builder.append(entityId);
        builder.append(", topSniRedirectGroupSrcPortId=");
        builder.append(topSniRedirectGroupSrcPortId);
        builder.append(", srcIndex=");
        builder.append(srcIndex);
        builder.append(", topSniRedirectGroupSrcPort=");
        builder.append(topSniRedirectGroupSrcPort);
        builder.append(", srcPortName=");
        builder.append(srcPortName);
        builder.append(", topSniRedirectGroupDirection=");
        builder.append(topSniRedirectGroupDirection);
        builder.append(", topSniRedirectGroupName=");
        builder.append(topSniRedirectGroupName);
        builder.append(", topSniRedirectGroupDstPort=");
        builder.append(topSniRedirectGroupDstPort);
        builder.append(", topSniRedirectGroupDstPortId=");
        builder.append(topSniRedirectGroupDstPortId);
        builder.append(", dstIndex=");
        builder.append(dstIndex);
        builder.append(", dstPortName=");
        builder.append(dstPortName);
        builder.append(", topSniRedirectGroupRowstatus=");
        builder.append(topSniRedirectGroupRowstatus);
        builder.append("]");
        return builder.toString();
    }

}
