/***********************************************************************
 * $Id: TopCcmtsDorRevAtt.java,v1.0 2016年9月13日 下午2:11:01 $
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
 * @created @2016年9月13日-下午2:11:01
 *
 */
public class TopCcmtsDorRevAtt implements AliasesSuperType {
    private static final long serialVersionUID = 5917945007785909040L;
    private Long cmcId;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.21.23.1.1", index = true)
    private Long revAttIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.21.23.1.2", writable = true, type = "Integer32")
    private Integer revAtt;// 反向射频支路1-4衰减表

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
     * @return the revAttIndex
     */
    public Long getRevAttIndex() {
        return revAttIndex;
    }

    /**
     * @param revAttIndex
     *            the revAttIndex to set
     */
    public void setRevAttIndex(Long revAttIndex) {
        this.revAttIndex = revAttIndex;
    }

    /**
     * @return the revAtt
     */
    public Integer getRevAtt() {
        return revAtt;
    }

    /**
     * @param revAtt
     *            the revAtt to set
     */
    public void setRevAtt(Integer revAtt) {
        this.revAtt = revAtt;
    }

}
