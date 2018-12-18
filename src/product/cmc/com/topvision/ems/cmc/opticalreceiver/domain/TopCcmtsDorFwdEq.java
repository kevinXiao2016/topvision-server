/***********************************************************************
 * $Id: TopCcmtsDorFwdEq.java,v1.0 2016年9月13日 下午2:09:24 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.opticalreceiver.domain;

import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author haojie
 * @created @2016年9月13日-下午2:09:24
 *
 */
public class TopCcmtsDorFwdEq implements AliasesSuperType {
    private static final long serialVersionUID = -798136889015354911L;
    private Long cmcId;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.21.22.1.1", index = true)
    private Long fwdEqIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.21.22.1.2", writable = true, type = "Integer32")
    private Integer fwdEq;// 正向射频支路1-4均衡

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
     * @return the fwdEqIndex
     */
    public Long getFwdEqIndex() {
        return fwdEqIndex;
    }

    /**
     * @param fwdEqIndex
     *            the fwdEqIndex to set
     */
    public void setFwdEqIndex(Long fwdEqIndex) {
        this.fwdEqIndex = fwdEqIndex;
    }

    /**
     * @return the fwdEq
     */
    public Integer getFwdEq() {
        return fwdEq;
    }

    /**
     * @param fwdEq
     *            the fwdEq to set
     */
    public void setFwdEq(Integer fwdEq) {
        this.fwdEq = fwdEq;
    }

}
