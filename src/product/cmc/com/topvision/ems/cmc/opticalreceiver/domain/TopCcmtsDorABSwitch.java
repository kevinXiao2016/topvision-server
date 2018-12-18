/***********************************************************************
 * $Id: TopCcmtsDorABSwitch.java,v1.0 2016年9月13日 下午1:30:27 $
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
 * @created @2016年9月13日-下午1:30:27
 *
 */
public class TopCcmtsDorABSwitch implements AliasesSuperType {
    private static final long serialVersionUID = -6817123075816879612L;
    private Long cmcId;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.21.10.1.1", index = true)
    private Long abSwitchIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.21.10.1.2")
    private Integer abSwitchState;// State of A-B switch

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
     * @return the abSwitchIndex
     */
    public Long getAbSwitchIndex() {
        return abSwitchIndex;
    }

    /**
     * @param abSwitchIndex
     *            the abSwitchIndex to set
     */
    public void setAbSwitchIndex(Long abSwitchIndex) {
        this.abSwitchIndex = abSwitchIndex;
    }

    /**
     * @return the abSwitchState
     */
    public Integer getAbSwitchState() {
        return abSwitchState;
    }

    /**
     * @param abSwitchState
     *            the abSwitchState to set
     */
    public void setAbSwitchState(Integer abSwitchState) {
        this.abSwitchState = abSwitchState;
    }

}
