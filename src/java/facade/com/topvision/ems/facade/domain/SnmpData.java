/***********************************************************************
 * $Id: SnmpData.java,v 1.1 Aug 12, 2009 10:26:28 PM kelers Exp $
 * 
 * @author: kelers
 * 
 * (c)Copyright 2011 Topoview All rights reserved.
 ***********************************************************************/
package com.topvision.ems.facade.domain;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.topvision.framework.snmp.SnmpParam;

/**
 * @Create Date Aug 12, 2009 10:26:28 PM
 * 
 * @author kelers
 * 
 */
public class SnmpData implements Serializable {
    private static final long serialVersionUID = 7718303336502334558L;
    private SnmpParam snmpParam = null;
    private Map<String, String> data = new HashMap<String, String>();
    private String error = null;

    /**
     * 
     * @param oid
     * @param value
     */
    public void addData(String oid, String value) {
        data.put(oid, value);
    }

    public void clear() {
        data.clear();
    }

    /**
     * @return the data
     */
    public Map<String, String> getData() {
        return data;
    }

    /**
     * @param key
     * @return value
     */
    public String getData(String key) {
        return data.get(key);
    }

    /**
     * @return the error
     */
    public String getError() {
        return error;
    }

    /**
     * @return the snmpParam
     */
    public SnmpParam getSnmpParam() {
        return snmpParam;
    }

    /**
     * 
     * @param oid
     */
    public void removeData(String oid) {
        data.remove(oid);
    }

    /**
     * @param data
     *            the data to set
     */
    public void setData(Map<String, String> data) {
        this.data = data;
    }

    /**
     * @param error
     *            the error to set
     */
    public void setError(String error) {
        this.error = error;
    }

    /**
     * @param snmpParam
     *            the snmpParam to set
     */
    public void setSnmpParam(SnmpParam snmpParam) {
        this.snmpParam = snmpParam;
    }

    public void setValue(String oid, String value) {
        data.put(oid, value);
    }
}
