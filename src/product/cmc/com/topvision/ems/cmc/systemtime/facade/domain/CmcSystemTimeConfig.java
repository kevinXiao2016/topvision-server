/***********************************************************************
 * $Id: CmcSystemTimeConfig.java,v1.0 2013-7-18 下午3:51:39 $
 * 
 * @author: dosion
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.systemtime.facade.domain;

import org.apache.ibatis.type.Alias;

import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author dosion
 * @created @2013-7-18-下午3:51:39
 * 
 */
@Alias("cmcSystemTimeConfig")
public class CmcSystemTimeConfig implements AliasesSuperType {
    private static final long serialVersionUID = -8428082631904399459L;
    private Long entityId;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.1.16.1.0", writable=true, type = "UnsignedInteger32")
    private Long topCcmtsSysTime;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.1.16.2.0", writable=true, type = "Integer32")
    private Integer topCcmtsSysTimeZone;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.1.16.3.0", writable=true, type = "OctetString")
    private String topCcmtsNtpserverAddress;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.1.16.4.0", writable=true, type = "UnsignedInteger32")
    private Integer topCcmtsSysTimeSynInterval;
    private Long collectTime;
    private Long increaseTime;

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public Long getTopCcmtsSysTime() {
        return topCcmtsSysTime;
    }

    public void setTopCcmtsSysTime(Long topCcmtsSysTime) {
        this.topCcmtsSysTime = topCcmtsSysTime;
    }

    public Integer getTopCcmtsSysTimeZone() {
        return topCcmtsSysTimeZone;
    }

    public void setTopCcmtsSysTimeZone(Integer topCcmtsSysTimeZone) {
        this.topCcmtsSysTimeZone = topCcmtsSysTimeZone;
    }

    public String getTopCcmtsNtpserverAddress() {
        return topCcmtsNtpserverAddress;
    }

    public void setTopCcmtsNtpserverAddress(String topCcmtsNtpserverAddress) {
        this.topCcmtsNtpserverAddress = topCcmtsNtpserverAddress;
    }

    public Integer getTopCcmtsSysTimeSynInterval() {
        return topCcmtsSysTimeSynInterval;
    }

    public void setTopCcmtsSysTimeSynInterval(Integer topCcmtsSysTimeSynInterval) {
        this.topCcmtsSysTimeSynInterval = topCcmtsSysTimeSynInterval;
    }

    public Long getCollectTime() {
        return collectTime;
    }

    public void setCollectTime(Long collectTime) {
        this.collectTime = collectTime;
        if(collectTime != null){
            this.increaseTime = System.currentTimeMillis() - collectTime;
        }        
    }

    public Long getIncreaseTime() {
        return increaseTime;
    }

    public void setIncreaseTime(Long increaseTime) {
        this.increaseTime = increaseTime;
    }
    
    public CmcSystemTimeConfig clone(){
        CmcSystemTimeConfig clone = new CmcSystemTimeConfig();
        clone.setCollectTime(collectTime);
        clone.setEntityId(entityId);
        clone.setIncreaseTime(increaseTime);
        clone.setTopCcmtsNtpserverAddress(topCcmtsNtpserverAddress);
        clone.setTopCcmtsSysTime(topCcmtsSysTime);
        clone.setTopCcmtsSysTimeSynInterval(topCcmtsSysTimeSynInterval);
        clone.setTopCcmtsSysTimeZone(topCcmtsSysTimeZone);
        return clone;
    }
}
