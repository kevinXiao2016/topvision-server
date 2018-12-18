/***********************************************************************
 * $Id: TopCcmtsDorLinePower.java,v1.0 2016年9月13日 下午1:14:55 $
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
 * @created @2016年9月13日-下午1:14:55
 *
 */
public class TopCcmtsDorLinePower implements AliasesSuperType {
    private static final long serialVersionUID = -6874751372960716648L;
    private Long cmcId;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.21.13.1.1", index = true)
    private Long linePowerIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.21.13.1.2")
    private Integer linePowerVoltage1;// 同轴供电输入电压 AC 60V电压

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
     * @return the linePowerIndex
     */
    public Long getLinePowerIndex() {
        return linePowerIndex;
    }

    /**
     * @param linePowerIndex
     *            the linePowerIndex to set
     */
    public void setLinePowerIndex(Long linePowerIndex) {
        this.linePowerIndex = linePowerIndex;
    }

    /**
     * @return the linePowerVoltage1
     */
    public Integer getLinePowerVoltage1() {
        return linePowerVoltage1;
    }

    /**
     * @param linePowerVoltage1
     *            the linePowerVoltage1 to set
     */
    public void setLinePowerVoltage1(Integer linePowerVoltage1) {
        this.linePowerVoltage1 = linePowerVoltage1;
    }

}
