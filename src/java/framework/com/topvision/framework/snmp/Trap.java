/***********************************************************************
 * $Id: Trap.java,v 1.1 Aug 26, 2008 3:21:02 PM kelers Exp $
 * 
 * @author: kelers
 * 
 * (c)Copyright 2011 Topoview All rights reserved.
 ***********************************************************************/
package com.topvision.framework.snmp;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.snmp4j.CommandResponderEvent;

/**
 * @Create Date Aug 26, 2008 3:21:02 PM
 * 
 * @author kelers
 * 
 */
public class Trap implements Serializable {
    private static final long serialVersionUID = -1847447078195267955L;
    private String address = null;
    private int port = 162;
    private boolean processed;
    private int securityLevel;
    private int securityModel;
    private transient Object source;
    private byte[] securityName = "EMS".getBytes();
    private Map<String, Object> variableBindings = new HashMap<String, Object>();
    private Date trapTime = null;
    private transient CommandResponderEvent commandResponderEvent = null;

    /**
     * @param variableBindings
     *            the variableBindings to set
     */
    public void addVariableBinding(String oid, String variable) {
        this.variableBindings.put(oid, variable);
    }

    /**
     * @return the address
     */
    public String getAddress() {
        return address;
    }

    /**
     * @return the commandResponderEvent
     */
    public CommandResponderEvent getCommandResponderEvent() {
        return commandResponderEvent;
    }

    /**
     * @return the port
     */
    public int getPort() {
        return port;
    }

    /**
     * @return the securityLevel
     */
    public int getSecurityLevel() {
        return securityLevel;
    }

    /**
     * @return the securityModel
     */
    public int getSecurityModel() {
        return securityModel;
    }

    /**
     * @return the securityName
     */
    public byte[] getSecurityName() {
        return securityName;
    }

    /**
     * @return the source
     */
    public Object getSource() {
        return source;
    }

    /**
     * @return the trapTime
     */
    public Date getTrapTime() {
        return trapTime;
    }

    /**
     * @return the variableBindings
     */
    public Map<String, Object> getVariableBindings() {
        return variableBindings;
    }

    /**
     * 返回绑定的String值，如果不存在就返回空
     * 
     * @param oid
     * @return
     */
    public String getVarialbleValue(String oid) {
        if (variableBindings != null && variableBindings.containsKey(oid)) {
            return variableBindings.get(oid).toString();
        }
        return null;
    }

    /**
     * 返回绑定的整型值，如果不存在就返回空
     * 
     * @param oid
     * @return
     */
    public Integer getVarialbleInt(String oid) {
        if (variableBindings != null && variableBindings.containsKey(oid)) {
            return Integer.parseInt(variableBindings.get(oid).toString());
        }
        return null;
    }
    
    /**
     * 返回绑定的Long值，如果不存在就返回空
     * 
     * @param oid
     * @return
     */
    public Long getVarialbleLong(String oid) {
        if (variableBindings != null && variableBindings.containsKey(oid)) {
            return Long.parseLong(variableBindings.get(oid).toString());
        }
        return null;
    }

    /**
     * 返回绑定的Byte值，如果不存在就返回空
     * 
     * @param oid
     * @return
     */
    public Byte getVarialbleByte(String oid) {
        if (variableBindings != null && variableBindings.containsKey(oid)) {
            return Byte.parseByte(variableBindings.get(oid).toString());
        }
        return null;
    }

    /**
     * @return the processed
     */
    public boolean isProcessed() {
        return processed;
    }

    /**
     * @param address
     *            the address to set
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * @param commandResponderEvent
     *            the commandResponderEvent to set
     */
    public void setCommandResponderEvent(CommandResponderEvent commandResponderEvent) {
        this.commandResponderEvent = commandResponderEvent;
    }

    /**
     * @param port
     *            the port to set
     */
    public void setPort(int port) {
        this.port = port;
    }

    /**
     * @param processed
     *            the processed to set
     */
    public void setProcessed(boolean processed) {
        this.processed = processed;
    }

    /**
     * @param securityLevel
     *            the securityLevel to set
     */
    public void setSecurityLevel(int securityLevel) {
        this.securityLevel = securityLevel;
    }

    /**
     * @param securityModel
     *            the securityModel to set
     */
    public void setSecurityModel(int securityModel) {
        this.securityModel = securityModel;
    }

    /**
     * @param securityName
     *            the securityName to set
     */
    public void setSecurityName(byte[] securityName) {
        this.securityName = securityName;
    }

    /**
     * @param source
     *            the source to set
     */
    public void setSource(Object source) {
        this.source = source;
    }

    /**
     * @param trapTime
     *            the trapTime to set
     */
    public void setTrapTime(Date trapTime) {
        this.trapTime = trapTime;
    }

    /**
     * @param variableBindings
     *            the variableBindings to set
     */
    public void setVariableBindings(Map<String, Object> variableBindings) {
        this.variableBindings = variableBindings;
    }

    /**
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder trap = new StringBuilder();
        trap.append("\n************************trap************************\n");
        trap.append("trapTime=").append(getTrapTime()).append("\n");
        trap.append("address=").append(address).append("\n");
        trap.append("processed=").append(processed).append("\n");
        trap.append("securityLevel=").append(securityLevel).append("\n");
        trap.append("securityModel=").append(securityModel).append("\n");
        trap.append("source=").append(source).append("\n");
        trap.append("securityName=").append(new String(securityName)).append("\n");
        trap.append("VariableBinding=").append(variableBindings).append("\n");
        trap.append("************************trap************************");
        return trap.toString();
    }
}
