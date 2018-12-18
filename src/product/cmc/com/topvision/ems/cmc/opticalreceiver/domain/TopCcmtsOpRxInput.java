/***********************************************************************
 * $Id: TopCcmtsOpRxInput.java,v1.0 2016年9月13日 上午11:54:32 $
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
 * @created @2016年9月13日-上午11:54:32
 *
 */
public class TopCcmtsOpRxInput implements AliasesSuperType {
    private static final long serialVersionUID = -5421990338240734858L;
    private Long cmcId;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.21.3.1.1", index = true)
    private Long inputIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.21.3.1.2")
    private Integer inputPower;// 正向光收A路光功率

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
     * @return the inputIndex
     */
    public Long getInputIndex() {
        return inputIndex;
    }

    /**
     * @param inputIndex
     *            the inputIndex to set
     */
    public void setInputIndex(Long inputIndex) {
        this.inputIndex = inputIndex;
    }

    /**
     * @return the inputPower
     */
    public Integer getInputPower() {
        return inputPower;
    }

    /**
     * @param inputPower
     *            the inputPower to set
     */
    public void setInputPower(Integer inputPower) {
        this.inputPower = inputPower;
    }

}
