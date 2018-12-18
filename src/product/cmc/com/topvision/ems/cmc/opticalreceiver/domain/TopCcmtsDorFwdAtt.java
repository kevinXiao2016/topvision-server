/***********************************************************************
 * $Id: TopCcmtsDorFwdAtt.java,v1.0 2016年9月13日 下午2:05:13 $
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
 * @created @2016年9月13日-下午2:05:13
 *
 */
public class TopCcmtsDorFwdAtt implements AliasesSuperType {
    private static final long serialVersionUID = 8356067250699579471L;
    private Long cmcId;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.21.21.1.1", index = true)
    private Long fwdAttIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.21.21.1.2", writable = true, type = "Integer32")
    private Integer fwdAtt;// 正向射频支路1-4衰减

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
     * @return the fwdAttIndex
     */
    public Long getFwdAttIndex() {
        return fwdAttIndex;
    }

    /**
     * @param fwdAttIndex
     *            the fwdAttIndex to set
     */
    public void setFwdAttIndex(Long fwdAttIndex) {
        this.fwdAttIndex = fwdAttIndex;
    }

    /**
     * @return the fwdAtt
     */
    public Integer getFwdAtt() {
        return fwdAtt;
    }

    /**
     * @param fwdAtt
     *            the fwdAtt to set
     */
    public void setFwdAtt(Integer fwdAtt) {
        this.fwdAtt = fwdAtt;
    }

}
