/***********************************************************************
 * $Id: MacDomainStatusInfo.java,v1.0 2011-10-26 下午04:07:53 $
 * 
 * @author: Administrator
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.macdomain.facade.domain;

import java.io.Serializable;

import org.apache.ibatis.type.Alias;

import com.topvision.framework.constants.Symbol;
import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author Administrator
 * @created @2011-10-26-下午04:07:53
 * 
 */
@Alias("macDomainStatusInfo")
public class MacDomainStatusInfo implements Serializable, AliasesSuperType {
    private static final long serialVersionUID = 7629255163883880399L;
    private Long cmcId;
    @SnmpProperty(oid = "1.3.6.1.2.1.2.2.1.1", index = true)
    private Long cmcIndex;
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.1.3.2.1.1")
    private Long invalidRangeReqs;
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.1.3.2.1.2")
    private Long rangingAborteds;
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.1.3.2.1.3")
    private Long invalidRegReqs;
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.1.3.2.1.4")
    private Long failedRegReqs;
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.1.3.2.1.5")
    private Long invalidDataReqs;
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.1.3.2.1.6")
    private Long t5Timeouts;

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
     * @return the docsIfCmtsStatusInvalidRangeReqs
     */
    public Long getInvalidRangeReqs() {
        return invalidRangeReqs;
    }

    /**
     * @param invalidRangeReqs
     *            the docsIfCmtsStatusInvalidRangeReqs to set
     */
    public void setInvalidRangeReqs(Long invalidRangeReqs) {
        this.invalidRangeReqs = invalidRangeReqs;
    }

    /**
     * @return the docsIfCmtsStatusRangingAborteds
     */
    public Long getRangingAborteds() {
        return rangingAborteds;
    }

    /**
     * @param rangingAborteds
     *            the docsIfCmtsStatusRangingAborteds to set
     */
    public void setRangingAborteds(Long rangingAborteds) {
        this.rangingAborteds = rangingAborteds;
    }

    /**
     * @return the docsIfCmtsStatusInvalidRegReqs
     */
    public Long getInvalidRegReqs() {
        return invalidRegReqs;
    }

    /**
     * @param invalidRegReqs
     *            the docsIfCmtsStatusInvalidRegReqs to set
     */
    public void setInvalidRegReqs(Long invalidRegReqs) {
        this.invalidRegReqs = invalidRegReqs;
    }

    /**
     * @return the docsIfCmtsStatusFailedRegReqs
     */
    public Long getFailedRegReqs() {
        return failedRegReqs;
    }

    /**
     * @param failedRegReqs
     *            the docsIfCmtsStatusFailedRegReqs to set
     */
    public void setFailedRegReqs(Long failedRegReqs) {
        this.failedRegReqs = failedRegReqs;
    }

    /**
     * @return the docsIfCmtsStatusInvalidDataReqs
     */
    public Long getInvalidDataReqs() {
        return invalidDataReqs;
    }

    /**
     * @param invalidDataReqs
     *            the docsIfCmtsStatusInvalidDataReqs to set
     */
    public void setInvalidDataReqs(Long invalidDataReqs) {
        this.invalidDataReqs = invalidDataReqs;
    }

    /**
     * @return the docsIfCmtsStatusT5Timeouts
     */
    public Long getT5Timeouts() {
        return t5Timeouts;
    }

    /**
     * @param t5Timeouts
     *            the docsIfCmtsStatusT5Timeouts to set
     */
    public void setT5Timeouts(Long t5Timeouts) {
        this.t5Timeouts = t5Timeouts;
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
        builder.append("MacDomainStatusInfo [cmcId=");
        builder.append(cmcId);
        builder.append(", cmcIndex=");
        builder.append(cmcIndex);
        builder.append(", invalidRangeReqs=");
        builder.append(invalidRangeReqs);
        builder.append(", rangingAborteds=");
        builder.append(rangingAborteds);
        builder.append(", invalidRegReqs=");
        builder.append(invalidRegReqs);
        builder.append(", failedRegReqs=");
        builder.append(failedRegReqs);
        builder.append(", invalidDataReqs=");
        builder.append(invalidDataReqs);
        builder.append(", t5Timeouts=");
        builder.append(t5Timeouts);
        builder.append(Symbol.BRACKET_RIGHT);
        return builder.toString();
    }

}
