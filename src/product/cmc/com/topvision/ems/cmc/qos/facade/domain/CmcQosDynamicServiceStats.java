/***********************************************************************
 * $Id: CmcQosDynamicServiceStats.java,v1.0 2011-10-21 上午09:04:49 $
 * 
 * @author: Dosion_Huang
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.qos.facade.domain;

import java.io.Serializable;
import java.sql.Timestamp;

import org.apache.ibatis.type.Alias;

import com.topvision.framework.constants.Symbol;
import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author Dosion_Huang
 * @created @2011-10-21-上午09:04:49
 * 
 */
@Alias("cmcQosDynamicServiceStats")
public class CmcQosDynamicServiceStats implements Serializable, AliasesSuperType {
    private static final long serialVersionUID = 7777365863433277542L;
    private Long cmcId;
    private String docsQosIfDirectionString;
    @SnmpProperty(oid = "1.3.6.1.2.1.2.2.1.1", index = true)
    private Long cmcIndex;
    // 方向 1：DownStream 2：UpStream
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.7.1.6.1.1", index = true)
    private Integer docsQosIfDirection;
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.7.1.6.1.2")
    private Long docsQosDsaReqs;
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.7.1.6.1.3")
    private Long docsQosDsaRsps;
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.7.1.6.1.4")
    private Long docsQosDsaAcks;
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.7.1.6.1.5")
    private Long docsQosDscReqs;
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.7.1.6.1.6")
    private Long docsQosDscRsps;
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.7.1.6.1.7")
    private Long docsQosDscAcks;
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.7.1.6.1.8")
    private Long docsQosDsdReqs;
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.7.1.6.1.9")
    private Long docsQosDsdRsps;
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.7.1.6.1.10")
    private Long docsQosDynamicAdds;
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.7.1.6.1.11")
    private Long docsQosDynamicAddFails;
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.7.1.6.1.12")
    private Long docsQosDynamicChanges;
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.7.1.6.1.13")
    private Long docsQosDynamicChangeFails;
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.7.1.6.1.14")
    private Long docsQosDynamicDeletes;
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.7.1.6.1.15")
    private Long docsQosDynamicDeleteFails;
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.7.1.6.1.16")
    private Long docsQosDccReqs;
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.7.1.6.1.17")
    private Long docsQosDccRsps;
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.7.1.6.1.18")
    private Long docsQosDccAcks;
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.7.1.6.1.19")
    private Long docsQosDccs;
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.7.1.6.1.20")
    private Long docsQosDccFails;
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.7.1.6.1.21")
    private Long docsQosDccRspDeparts;
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.7.1.6.1.22")
    private Long docsQosDccRspArrives;
    // private Long currentServiceFlowNum; 私有MIB实现前不使用
    private Timestamp collectTime;

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
     * @return the docsQosIfDirection
     */
    public Integer getDocsQosIfDirection() {
        return docsQosIfDirection;
    }

    /**
     * @param docsQosIfDirection
     *            the docsQosIfDirection to set
     */
    public void setDocsQosIfDirection(Integer docsQosIfDirection) {
        this.docsQosIfDirection = docsQosIfDirection;
        // this.docsQosIfDirectionString =
        // CmcQosServiceFlowInfo.direction[docsQosIfDirection.intValue()];
    }

    /**
     * @return the docsQosDsaReqs
     */
    public Long getDocsQosDsaReqs() {
        return docsQosDsaReqs;
    }

    /**
     * @param docsQosDsaReqs
     *            the docsQosDsaReqs to set
     */
    public void setDocsQosDsaReqs(Long docsQosDsaReqs) {
        this.docsQosDsaReqs = docsQosDsaReqs;
    }

    /**
     * @return the docsQosDsaRsps
     */
    public Long getDocsQosDsaRsps() {
        return docsQosDsaRsps;
    }

    /**
     * @param docsQosDsaRsps
     *            the docsQosDsaRsps to set
     */
    public void setDocsQosDsaRsps(Long docsQosDsaRsps) {
        this.docsQosDsaRsps = docsQosDsaRsps;
    }

    /**
     * @return the docsQosDsaAcks
     */
    public Long getDocsQosDsaAcks() {
        return docsQosDsaAcks;
    }

    /**
     * @param docsQosDsaAcks
     *            the docsQosDsaAcks to set
     */
    public void setDocsQosDsaAcks(Long docsQosDsaAcks) {
        this.docsQosDsaAcks = docsQosDsaAcks;
    }

    /**
     * @return the docsQosDscReqs
     */
    public Long getDocsQosDscReqs() {
        return docsQosDscReqs;
    }

    /**
     * @param docsQosDscReqs
     *            the docsQosDscReqs to set
     */
    public void setDocsQosDscReqs(Long docsQosDscReqs) {
        this.docsQosDscReqs = docsQosDscReqs;
    }

    /**
     * @return the docsQosDscRsps
     */
    public Long getDocsQosDscRsps() {
        return docsQosDscRsps;
    }

    /**
     * @param docsQosDscRsps
     *            the docsQosDscRsps to set
     */
    public void setDocsQosDscRsps(Long docsQosDscRsps) {
        this.docsQosDscRsps = docsQosDscRsps;
    }

    /**
     * @return the docsQosDscAcks
     */
    public Long getDocsQosDscAcks() {
        return docsQosDscAcks;
    }

    /**
     * @param docsQosDscAcks
     *            the docsQosDscAcks to set
     */
    public void setDocsQosDscAcks(Long docsQosDscAcks) {
        this.docsQosDscAcks = docsQosDscAcks;
    }

    /**
     * @return the docsQosDsdReqs
     */
    public Long getDocsQosDsdReqs() {
        return docsQosDsdReqs;
    }

    /**
     * @param docsQosDsdReqs
     *            the docsQosDsdReqs to set
     */
    public void setDocsQosDsdReqs(Long docsQosDsdReqs) {
        this.docsQosDsdReqs = docsQosDsdReqs;
    }

    /**
     * @return the docsQosDsdRsps
     */
    public Long getDocsQosDsdRsps() {
        return docsQosDsdRsps;
    }

    /**
     * @param docsQosDsdRsps
     *            the docsQosDsdRsps to set
     */
    public void setDocsQosDsdRsps(Long docsQosDsdRsps) {
        this.docsQosDsdRsps = docsQosDsdRsps;
    }

    /**
     * @return the docsQosDynamicAdds
     */
    public Long getDocsQosDynamicAdds() {
        return docsQosDynamicAdds;
    }

    /**
     * @param docsQosDynamicAdds
     *            the docsQosDynamicAdds to set
     */
    public void setDocsQosDynamicAdds(Long docsQosDynamicAdds) {
        this.docsQosDynamicAdds = docsQosDynamicAdds;
    }

    /**
     * @return the docsQosDynamicAddFails
     */
    public Long getDocsQosDynamicAddFails() {
        return docsQosDynamicAddFails;
    }

    /**
     * @param docsQosDynamicAddFails
     *            the docsQosDynamicAddFails to set
     */
    public void setDocsQosDynamicAddFails(Long docsQosDynamicAddFails) {
        this.docsQosDynamicAddFails = docsQosDynamicAddFails;
    }

    /**
     * @return the docsQosDynamicChanges
     */
    public Long getDocsQosDynamicChanges() {
        return docsQosDynamicChanges;
    }

    /**
     * @param docsQosDynamicChanges
     *            the docsQosDynamicChanges to set
     */
    public void setDocsQosDynamicChanges(Long docsQosDynamicChanges) {
        this.docsQosDynamicChanges = docsQosDynamicChanges;
    }

    /**
     * @return the docsQosDynamicChangeFails
     */
    public Long getDocsQosDynamicChangeFails() {
        return docsQosDynamicChangeFails;
    }

    /**
     * @param docsQosDynamicChangeFails
     *            the docsQosDynamicChangeFails to set
     */
    public void setDocsQosDynamicChangeFails(Long docsQosDynamicChangeFails) {
        this.docsQosDynamicChangeFails = docsQosDynamicChangeFails;
    }

    /**
     * @return the docsQosDynamicDeletes
     */
    public Long getDocsQosDynamicDeletes() {
        return docsQosDynamicDeletes;
    }

    /**
     * @param docsQosDynamicDeletes
     *            the docsQosDynamicDeletes to set
     */
    public void setDocsQosDynamicDeletes(Long docsQosDynamicDeletes) {
        this.docsQosDynamicDeletes = docsQosDynamicDeletes;
    }

    /**
     * @return the docsQosDynamicDeleteFails
     */
    public Long getDocsQosDynamicDeleteFails() {
        return docsQosDynamicDeleteFails;
    }

    /**
     * @param docsQosDynamicDeleteFails
     *            the docsQosDynamicDeleteFails to set
     */
    public void setDocsQosDynamicDeleteFails(Long docsQosDynamicDeleteFails) {
        this.docsQosDynamicDeleteFails = docsQosDynamicDeleteFails;
    }

    /**
     * @return the docsQosDccReqs
     */
    public Long getDocsQosDccReqs() {
        return docsQosDccReqs;
    }

    /**
     * @param docsQosDccReqs
     *            the docsQosDccReqs to set
     */
    public void setDocsQosDccReqs(Long docsQosDccReqs) {
        this.docsQosDccReqs = docsQosDccReqs;
    }

    /**
     * @return the docsQosDccRsps
     */
    public Long getDocsQosDccRsps() {
        return docsQosDccRsps;
    }

    /**
     * @param docsQosDccRsps
     *            the docsQosDccRsps to set
     */
    public void setDocsQosDccRsps(Long docsQosDccRsps) {
        this.docsQosDccRsps = docsQosDccRsps;
    }

    /**
     * @return the docsQosDccAcks
     */
    public Long getDocsQosDccAcks() {
        return docsQosDccAcks;
    }

    /**
     * @param docsQosDccAcks
     *            the docsQosDccAcks to set
     */
    public void setDocsQosDccAcks(Long docsQosDccAcks) {
        this.docsQosDccAcks = docsQosDccAcks;
    }

    /**
     * @return the docsQosDccs
     */
    public Long getDocsQosDccs() {
        return docsQosDccs;
    }

    /**
     * @param docsQosDccs
     *            the docsQosDccs to set
     */
    public void setDocsQosDccs(Long docsQosDccs) {
        this.docsQosDccs = docsQosDccs;
    }

    /**
     * @return the docsQosDccFails
     */
    public Long getDocsQosDccFails() {
        return docsQosDccFails;
    }

    /**
     * @param docsQosDccFails
     *            the docsQosDccFails to set
     */
    public void setDocsQosDccFails(Long docsQosDccFails) {
        this.docsQosDccFails = docsQosDccFails;
    }

    /**
     * @return the docsQosDccRspDeparts
     */
    public Long getDocsQosDccRspDeparts() {
        return docsQosDccRspDeparts;
    }

    /**
     * @param docsQosDccRspDeparts
     *            the docsQosDccRspDeparts to set
     */
    public void setDocsQosDccRspDeparts(Long docsQosDccRspDeparts) {
        this.docsQosDccRspDeparts = docsQosDccRspDeparts;
    }

    /**
     * @return the docsQosDccRspArrives
     */
    public Long getDocsQosDccRspArrives() {
        return docsQosDccRspArrives;
    }

    /**
     * @param docsQosDccRspArrives
     *            the docsQosDccRspArrives to set
     */
    public void setDocsQosDccRspArrives(Long docsQosDccRspArrives) {
        this.docsQosDccRspArrives = docsQosDccRspArrives;
    }

    /**
     * @return the collectTime
     */
    public Timestamp getCollectTime() {
        return collectTime;
    }

    /**
     * @param collectTime
     *            the collectTime to set
     */
    public void setCollectTime(Timestamp collectTime) {
        this.collectTime = collectTime;
    }

    /**
     * @return the docsQosIfDirectionString
     */
    public String getDocsQosIfDirectionString() {
        return docsQosIfDirectionString;
    }

    /**
     * @param docsQosIfDirectionString
     *            the docsQosIfDirectionString to set
     */
    public void setDocsQosIfDirectionString(String docsQosIfDirectionString) {
        this.docsQosIfDirectionString = docsQosIfDirectionString;
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

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("CmcQosDynamicServiceStats [cmcId=");
        builder.append(cmcId);
        builder.append(", docsQosIfDirectionString=");
        builder.append(docsQosIfDirectionString);
        builder.append(", cmcIndex=");
        builder.append(cmcIndex);
        builder.append(", docsQosIfDirection=");
        builder.append(docsQosIfDirection);
        builder.append(", docsQosDsaReqs=");
        builder.append(docsQosDsaReqs);
        builder.append(", docsQosDsaRsps=");
        builder.append(docsQosDsaRsps);
        builder.append(", docsQosDsaAcks=");
        builder.append(docsQosDsaAcks);
        builder.append(", docsQosDscReqs=");
        builder.append(docsQosDscReqs);
        builder.append(", docsQosDscRsps=");
        builder.append(docsQosDscRsps);
        builder.append(", docsQosDscAcks=");
        builder.append(docsQosDscAcks);
        builder.append(", docsQosDsdReqs=");
        builder.append(docsQosDsdReqs);
        builder.append(", docsQosDsdRsps=");
        builder.append(docsQosDsdRsps);
        builder.append(", docsQosDynamicAdds=");
        builder.append(docsQosDynamicAdds);
        builder.append(", docsQosDynamicAddFails=");
        builder.append(docsQosDynamicAddFails);
        builder.append(", docsQosDynamicChanges=");
        builder.append(docsQosDynamicChanges);
        builder.append(", docsQosDynamicChangeFails=");
        builder.append(docsQosDynamicChangeFails);
        builder.append(", docsQosDynamicDeletes=");
        builder.append(docsQosDynamicDeletes);
        builder.append(", docsQosDynamicDeleteFails=");
        builder.append(docsQosDynamicDeleteFails);
        builder.append(", docsQosDccReqs=");
        builder.append(docsQosDccReqs);
        builder.append(", docsQosDccRsps=");
        builder.append(docsQosDccRsps);
        builder.append(", docsQosDccAcks=");
        builder.append(docsQosDccAcks);
        builder.append(", docsQosDccs=");
        builder.append(docsQosDccs);
        builder.append(", docsQosDccFails=");
        builder.append(docsQosDccFails);
        builder.append(", docsQosDccRspDeparts=");
        builder.append(docsQosDccRspDeparts);
        builder.append(", docsQosDccRspArrives=");
        builder.append(docsQosDccRspArrives);
        builder.append(", collectTime=");
        builder.append(collectTime);
        builder.append(Symbol.BRACKET_RIGHT);
        return builder.toString();
    }

}
