/***********************************************************************
 * $Id: CmcSyslogRecordType.java,v1.0 2013-4-26 下午8:56:05 $
 * 
 * @author: Administrator
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.syslog.domain;

import java.io.Serializable;

import org.apache.ibatis.type.Alias;

import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author Administrator
 * @created @2013-4-26-下午8:56:05
 *
 */
@Alias("cmcSyslogRecordType")
public class CmcSyslogRecordType implements Serializable, AliasesSuperType{
    
    private static final long serialVersionUID = 4318547670640194977L;
    
    private Long entityId;
    private String topCcmtsSyslogRecordType;
    private int topCcmtsSyslogMinEvtLvl;
    
    public CmcSyslogRecordType() {
        super();
    }
    public CmcSyslogRecordType(Long entityId, String topCcmtsSyslogRecordType, int topCcmtsSyslogMinEvtLvl) {
        super();
        this.entityId = entityId;
        this.topCcmtsSyslogRecordType = topCcmtsSyslogRecordType;
        this.topCcmtsSyslogMinEvtLvl = topCcmtsSyslogMinEvtLvl;
    }
    
    public Long getEntityId() {
        return entityId;
    }
    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }
    public String getTopCcmtsSyslogRecordType() {
        return topCcmtsSyslogRecordType;
    }
    public void setTopCcmtsSyslogRecordType(String topCcmtsSyslogRecordType) {
        this.topCcmtsSyslogRecordType = topCcmtsSyslogRecordType;
    }
    public int getTopCcmtsSyslogMinEvtLvl() {
        return topCcmtsSyslogMinEvtLvl;
    }
    public void setTopCcmtsSyslogMinEvtLvl(int topCcmtsSyslogMinEvtLvl) {
        this.topCcmtsSyslogMinEvtLvl = topCcmtsSyslogMinEvtLvl;
    }
    public static long getSerialversionuid() {
        return serialVersionUID;
    }
    
}
