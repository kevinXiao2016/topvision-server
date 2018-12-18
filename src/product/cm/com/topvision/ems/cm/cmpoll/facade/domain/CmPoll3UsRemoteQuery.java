/***********************************************************************
 * $Id: CmPoll3UsRemoteQuery.java,v1.0 2015年3月21日 上午10:05:28 $
 * 
 * @author: loyal
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cm.cmpoll.facade.domain;

import java.io.Serializable;

import com.topvision.framework.annotation.SnmpProperty;

/**
 * @author loyal
 * @created @2015年3月21日-上午10:05:28
 * 
 */
public class CmPoll3UsRemoteQuery implements Serializable {
    private static final long serialVersionUID = -8770204210975796096L;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.9.4.1.1", index = true)
    private Long cmIndex; // CM的Index
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.9.5.1.1", index = true)
    private Long cmUsIndex; // CM上行信道的Index
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.9.5.1.2")
    private Integer cmUsId; // CM上行信道的Id
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.9.5.1.3")
    private Long cmUsTxPower; // 3.0CM 上行发射电平

    public Long getCmIndex() {
        return cmIndex;
    }

    public void setCmIndex(Long cmIndex) {
        this.cmIndex = cmIndex;
    }

    public Long getCmUsIndex() {
        return cmUsIndex;
    }

    public void setCmUsIndex(Long cmUsIndex) {
        this.cmUsIndex = cmUsIndex;
    }

    public Long getCmUsTxPower() {
        return cmUsTxPower;
    }

    public void setCmUsTxPower(Long cmUsTxPower) {
        this.cmUsTxPower = cmUsTxPower;
    }

    public Integer getCmUsId() {
        return cmUsId;
    }

    public void setCmUsId(Integer cmUsId) {
        this.cmUsId = cmUsId;
    }

    @Override
    public String toString() {
        return "CmPoll3UsRemoteQuery{" +
                "cmIndex=" + cmIndex +
                ", cmUsIndex=" + cmUsIndex +
                ", cmUsId=" + cmUsId +
                ", cmUsTxPower=" + cmUsTxPower +
                '}';
    }
}
