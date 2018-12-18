/***********************************************************************
 * $Id: TopCcmtsDorLinePower.java,v1.0 2016年9月13日 下午1:09:06 $
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
 * @created @2016年9月13日-下午1:09:06
 *
 */
public class TopCcmtsDorDCPower implements AliasesSuperType {
    private static final long serialVersionUID = 1429886959614968147L;
    private Long cmcId;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.21.8.1.1", index = true)
    private Long dcPowerIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.21.8.1.2")
    private Integer dcPowerVoltage;// 直流输出电压（DC12V/DC24V）

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
     * @return the dcPowerIndex
     */
    public Long getDcPowerIndex() {
        return dcPowerIndex;
    }

    /**
     * @param dcPowerIndex
     *            the dcPowerIndex to set
     */
    public void setDcPowerIndex(Long dcPowerIndex) {
        this.dcPowerIndex = dcPowerIndex;
    }

    /**
     * @return the dcPowerVoltage
     */
    public Integer getDcPowerVoltage() {
        return dcPowerVoltage;
    }

    /**
     * @param dcPowerVoltage
     *            the dcPowerVoltage to set
     */
    public void setDcPowerVoltage(Integer dcPowerVoltage) {
        this.dcPowerVoltage = dcPowerVoltage;
    }

}
