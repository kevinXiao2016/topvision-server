/***********************************************************************
 * $Id: EponEventLog.java,v1.0 2012-4-26 上午10:36:47 $
 * 
 * @author: huqiao
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.fault.domain;

import java.io.Serializable;
import java.sql.Timestamp;

import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author huqiao
 * @created @2012-4-26-上午10:36:47
 * 
 */
public class EponEventLog implements Serializable, AliasesSuperType {

    private static final long serialVersionUID = -7630632279979356594L;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.2.11.2.3.1.1", index = true)
    private Integer eventSeqNum;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.2.11.2.3.1.2", writable = true, type = "Integer32")
    private Integer eventCode;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.2.11.2.3.1.3", writable = true, type = "OctetString")
    private String eventInstance;
    private String eventSource;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.2.11.2.3.1.4", writable = true, type = "OctetString")
    private String eventOccurTime;
    private Timestamp eventTime;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.2.11.2.3.1.5", writable = true, type = "OctetString")
    private String eventAdditionalText;

    /**
     * @return the eventSeqNum
     */
    public Integer getEventSeqNum() {
        return eventSeqNum;
    }

    /**
     * @param eventSeqNum
     *            the eventSeqNum to set
     */
    public void setEventSeqNum(Integer eventSeqNum) {
        this.eventSeqNum = eventSeqNum;
    }

    /**
     * @return the eventCode
     */
    public Integer getEventCode() {
        return eventCode;
    }

    /**
     * @param eventCode
     *            the eventCode to set
     */
    public void setEventCode(Integer eventCode) {
        this.eventCode = eventCode;
    }

    /**
     * @return the eventInstance
     */
    public String getEventInstance() {
        return eventInstance;
    }

    /**
     * @param eventInstance
     *            the eventInstance to set
     */
    public void setEventInstance(String eventInstance) {
        this.eventInstance = eventInstance;
        String[] instance = eventInstance.split(":");
        StringBuilder source = new StringBuilder(String.valueOf(Long.parseLong(instance[1], 16)));
        if (!instance[2].equals("00")) {
            source.append("/").append(Long.parseLong(instance[2], 16));
        }
        if (!instance[3].equals("00")) {
            source.append(":").append(Long.parseLong(instance[3], 16));
        }
        if (!instance[5].equals("00")) {
            source.append("/").append(Long.parseLong(instance[5], 16));
        }
        setEventSource(source.toString());
    }

    /**
     * @return the eventOccurTime
     */
    public String getEventOccurTime() {
        return eventOccurTime;
    }

    /**
     * @param eventOccurTime
     *            the eventOccurTime to set
     */
    public void setEventOccurTime(String eventOccurTime) {
        this.eventOccurTime = eventOccurTime;
        Timestamp timestamp;
        if (Long.parseLong(eventOccurTime) < 0L) {
            timestamp = new Timestamp(Long.parseLong(eventOccurTime) + 28800000L);
        } else {
            timestamp = new Timestamp(Long.parseLong(eventOccurTime));
        }
        setEventTime(timestamp);
    }

    /**
     * @return the eventAdditionalText
     */
    public String getEventAdditionalText() {
        return eventAdditionalText;
    }

    /**
     * @param eventAdditionalText
     *            the eventAdditionalText to set
     */
    public void setEventAdditionalText(String eventAdditionalText) {
        this.eventAdditionalText = eventAdditionalText;
    }

    /**
     * @return the eventSource
     */
    public String getEventSource() {
        return eventSource;
    }

    /**
     * @param eventSource
     *            the eventSource to set
     */
    public void setEventSource(String eventSource) {
        this.eventSource = eventSource;
    }

    /**
     * @return the eventTime
     */
    public Timestamp getEventTime() {
        return eventTime;
    }

    /**
     * @param eventTime
     *            the eventTime to set
     */
    public void setEventTime(Timestamp eventTime) {
        this.eventTime = eventTime;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("EponEventLog [eventSeqNum=");
        builder.append(eventSeqNum);
        builder.append(", eventCode=");
        builder.append(eventCode);
        builder.append(", eventInstance=");
        builder.append(eventInstance);
        builder.append(", eventSource=");
        builder.append(eventSource);
        builder.append(", eventOccurTime=");
        builder.append(eventOccurTime);
        builder.append(", eventTime=");
        builder.append(eventTime);
        builder.append(", eventAdditionalText=");
        builder.append(eventAdditionalText);
        builder.append("]");
        return builder.toString();
    }

}
