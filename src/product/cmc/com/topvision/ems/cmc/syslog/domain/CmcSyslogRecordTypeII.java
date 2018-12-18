/***********************************************************************
 * $Id: CmcSyslogRecordType.java,v1.0 2013-4-26 下午8:56:05 $
 * 
 * @author: Administrator
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.syslog.domain;

import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.dao.mybatis.AliasesSuperType;
import org.apache.ibatis.type.Alias;

import java.io.Serializable;

/**
 * @author Administrator
 * @created @2013-4-26-下午8:56:05
 *
 */
@Alias("cmcSyslogRecordTypeII")
public class CmcSyslogRecordTypeII implements Serializable, AliasesSuperType{

    private static final long serialVersionUID = 4318547670640194977L;

    private Long entityId;
    @SnmpProperty(oid = "1.3.6.1.2.1.69.1.5.7.1.1", index = true)
    private Integer evPriority ;
    @SnmpProperty(oid = "1.3.6.1.2.1.69.1.5.7.1.2", writable = true, type = "OctetString")
    private byte[] evReportingByte;
    private String evReporting;
    private Integer reporting = 0;

    /**
     * flash
     */
    private Boolean local;
    /**
     * trap
     */
    private Boolean traps;
    /**
     * syslog
     */
    private Boolean syslog;
    /**
     * memory
     */
    private Boolean localVolatile;


    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public Integer getEvPriority() {
        return evPriority;
    }

    public void setEvPriority(Integer evPriority) {
        this.evPriority = evPriority;
    }

    public void setEvReporting(String evReporting) {
        this.evReporting = evReporting;
        if (evReporting != null) {
            try {
                int reporting = Integer.parseInt(this.evReporting,16);
                calculation(reporting);
                return;
            } catch (Exception e) {
            }
        }
        initValue();
    }

    public String getEvReporting() {
        return evReporting;
    }

    public byte[] getEvReportingByte() {
        return evReportingByte;
    }

    public void setEvReportingByte(byte[] evReportingByte) {
        this.evReportingByte = evReportingByte;
        if (evReportingByte != null && evReportingByte.length == 2) {
            try {
                String evReporting = String.format("%02X",evReportingByte[0]) + String.format("%02X",evReportingByte[1]);
                int reporting = Integer.parseInt(evReporting,16);
                calculation(reporting);
                return;
            } catch (Exception e) {
            }
        }
        initValue();
    }

    public Boolean getLocal() {
        return local;
    }

    public void setLocal(Boolean local) {
        this.local = local;
        int reporting = this.reporting;
        if (local) {
            reporting = reporting | 0x8000;
        } else {
            reporting = reporting & 0x7FFF;
        }
        calculation(reporting);
    }

    public Boolean getTraps() {
        return traps;
    }

    public void setTraps(Boolean traps) {
        this.traps = traps;
        int reporting = this.reporting;
        if (traps) {
            reporting = reporting | 0x4000;
        } else {
            reporting = reporting & 0xBFFF;
        }
        calculation(reporting);
    }

    public Boolean getSyslog() {
        return syslog;
    }

    public void setSyslog(Boolean syslog) {
        this.syslog = syslog;
        int reporting = this.reporting;
        if (syslog) {
            reporting = reporting | 0x2000;
        } else {
            reporting = reporting & 0xDFFF;
        }
        calculation(reporting);
    }

    public Boolean getLocalVolatile() {
        return localVolatile;
    }

    public void setLocalVolatile(Boolean localVolatile) {
        this.localVolatile = localVolatile;
        int reporting = this.reporting;
        if (localVolatile) {
            reporting = reporting | 0x80;
        } else {
            reporting = reporting & 0xFF7F;
        }
        calculation(reporting);
    }

    private void calculation (Integer reporting) {
        this.reporting = reporting;
        this.evReporting = Integer.toHexString(reporting);
        this.evReportingByte = new byte[2];
        this.evReportingByte[0] =  (byte)((reporting & 0xFF00) >> 8);
        this.evReportingByte[1] = (byte)(reporting & 0xFF);
        this.local = (reporting & 0x8000) == 0x8000;
        this.traps = (reporting & 0x4000) == 0x4000;
        this.syslog = (reporting & 0x2000) == 0x2000;
        this.localVolatile = (reporting & 0x80) == 0x0080;
    }

    private void initValue() {
        this.evReportingByte = new byte[2];
        this.evReportingByte[0] = 0;
        this.evReportingByte[1] = 0;
        this.evReporting = "0000";
        this.reporting = 0;
        this.local = false;
        this.traps = false;
        this.syslog = false;
        this.localVolatile = false;
    }
}
