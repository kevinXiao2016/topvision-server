/***********************************************************************
 * $Id: TopCcmtsDorRRXOptPow.java,v1.0 2016年9月13日 下午2:44:33 $
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
 * @created @2016年9月13日-下午2:44:33
 *
 */
public class TopCcmtsDorRRXOptPow implements AliasesSuperType {
    private static final long serialVersionUID = 7747861599123121845L;
    private Long cmcId;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.21.25.1.1", index = true)
    private Long rrxOptPowIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.21.25.1.2")
    private Integer rrxOptPow;// 反向光收1-4输入光功率

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
     * @return the rrxOptPowIndex
     */
    public Long getRrxOptPowIndex() {
        return rrxOptPowIndex;
    }

    /**
     * @param rrxOptPowIndex
     *            the rrxOptPowIndex to set
     */
    public void setRrxOptPowIndex(Long rrxOptPowIndex) {
        this.rrxOptPowIndex = rrxOptPowIndex;
    }

    /**
     * @return the rrxOptPow
     */
    public Integer getRrxOptPow() {
        return rrxOptPow;
    }

    /**
     * @param rrxOptPow
     *            the rrxOptPow to set
     */
    public void setRrxOptPow(Integer rrxOptPow) {
        this.rrxOptPow = rrxOptPow;
    }

}
