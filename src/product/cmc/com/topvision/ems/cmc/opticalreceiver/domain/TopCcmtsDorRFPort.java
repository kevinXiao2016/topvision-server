/***********************************************************************
 * $Id: TopCcmtsDorRFPort.java,v1.0 2016年9月13日 下午1:25:20 $
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
 * @created @2016年9月13日-下午1:25:20
 *
 */
public class TopCcmtsDorRFPort implements AliasesSuperType {
    private static final long serialVersionUID = -3442234979700547040L;
    private Long cmcId;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.21.11.1.1", index = true)
    private Long rfPortIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.21.11.1.2")
    private Integer rfPortOutputRFLevel;// RF1-4端口输出电平

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
     * @return the rfPortIndex
     */
    public Long getRfPortIndex() {
        return rfPortIndex;
    }

    /**
     * @param rfPortIndex
     *            the rfPortIndex to set
     */
    public void setRfPortIndex(Long rfPortIndex) {
        this.rfPortIndex = rfPortIndex;
    }

    /**
     * @return the rfPortOutputRFLevel
     */
    public Integer getRfPortOutputRFLevel() {
        return rfPortOutputRFLevel;
    }

    /**
     * @param rfPortOutputRFLevel
     *            the rfPortOutputRFLevel to set
     */
    public void setRfPortOutputRFLevel(Integer rfPortOutputRFLevel) {
        this.rfPortOutputRFLevel = rfPortOutputRFLevel;
    }

}
