/***********************************************************************
 * $Id: CmcSyslogSwitchEntry.java,v1.0 2013-4-29 下午4:12:31 $
 * 
 * @author: Administrator
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.syslog.facade.domain;

import java.io.Serializable;

import com.topvision.framework.annotation.SnmpProperty;

/**
 * @author Administrator
 * @created @2013-4-29-下午4:12:31
 *
 */
public class CmcSyslogSwitchEntry implements Serializable {

    private static final long serialVersionUID = 167609383403065166L;
    
    private Long entityId;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.6.4.1.1", index = true)
    private Integer topCcmtsSyslogSlotNum;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.6.4.1.2", writable = true, type = "Integer32")
    private Integer topCcmtsSyslogPonSwitch;
    public Long getEntityId() {
        return entityId;
    }
    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }
    public Integer getTopCcmtsSyslogSlotNum() {
        return topCcmtsSyslogSlotNum;
    }
    public void setTopCcmtsSyslogSlotNum(Integer topCcmtsSyslogSlotNum) {
        this.topCcmtsSyslogSlotNum = topCcmtsSyslogSlotNum;
    }
    public Integer getTopCcmtsSyslogPonSwitch() {
        return topCcmtsSyslogPonSwitch;
    }
    public void setTopCcmtsSyslogPonSwitch(Integer topCcmtsSyslogPonSwitch) {
        this.topCcmtsSyslogPonSwitch = topCcmtsSyslogPonSwitch;
    }
    @Override
    public String toString() {
        return "CmcSyslogSwitchEntry [entityId=" + entityId + ", topCcmtsSyslogSlotNum=" + topCcmtsSyslogSlotNum
                + ", topCcmtsSyslogPonSwitch=" + topCcmtsSyslogPonSwitch + "]";
    }
    
}
