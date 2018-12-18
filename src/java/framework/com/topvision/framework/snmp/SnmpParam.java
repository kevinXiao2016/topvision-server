/***********************************************************************
 * $Id: SnmpParam.java,v 1.1 Jul 31, 2008 4:34:38 PM kelers Exp $
 * 
 * @author: kelers
 * 
 * (c)Copyright 2011 Topoview All rights reserved.
 ***********************************************************************/
package com.topvision.framework.snmp;

import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.security.SecurityLevel;

import com.topvision.framework.dao.mybatis.AliasesSuperType;
import com.topvision.framework.domain.DeviceParam;

/**
 * @Create Date Jul 31, 2008 4:34:38 PM
 * 
 * @author kelers
 * 
 */
public class SnmpParam extends DeviceParam implements AliasesSuperType {
    private static final long serialVersionUID = -4805603295628320173L;
    private static final String NO_AUTH_PROTOCOL = "NOAUTH";
    private static final String NO_PRIV_PROTOCOL = "NOPRIV";
    private static final String DEFAULT_PASSWORD = "12345678";
    private int version = SnmpConstants.version2c;
    private long timeout = 10000;
    private byte retry = 0;
    private int port = 161;
    private String mibs = "RFC1213-MIB";
    // for v1,v2c
    private String community = "public";
    private String writeCommunity = "private";
    private String deviceVersion = null;
    // for v3
    // private String username = null;//DeviceParam中已经定义
    // Modify by Rod Default is AUTH AND PRIV
    private int securityLevel = SecurityLevel.AUTH_PRIV;
    private String authProtocol = "MD5";// MD5,SHA
    private String privProtocol = "CBC-DES";// CBC-DES
    private String authPassword = "12345678";
    private String privPassword = "12345678";
    private String authoritativeEngineID = null;
    private String contextName = null;
    private String contextId = null;

    /**
     * (non-Javadoc)
     * 
     * @see java.lang.Object#clone()
     */
    @Override
    public SnmpParam clone() {
        SnmpParam param = new SnmpParam();
        param.setAuthoritativeEngineID(authoritativeEngineID);
        param.setAuthPassword(authPassword);
        param.setAuthProtocol(authProtocol);
        param.setCommunity(community);
        param.setWriteCommunity(writeCommunity);
        param.setContextId(contextId);
        param.setContextName(contextName);
        param.setEntityId(getEntityId());
        param.setIpAddress(getIpAddress());
        param.setMibs(mibs);
        param.setPort(port);
        param.setPrivPassword(privPassword);
        param.setPrivProtocol(privProtocol);
        param.setRetry(retry);
        param.setSecurityLevel(securityLevel);
        param.setTimeout(timeout);
        param.setUsername(getUsername());
        param.setVersion(version);
        param.setDeviceVersion(deviceVersion);
        return param;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("SnmpParam [version=");
        builder.append(version);
        builder.append(", timeout=");
        builder.append(timeout);
        builder.append(", retry=");
        builder.append(retry);
        builder.append(", port=");
        builder.append(port);
        builder.append(", mibs=");
        builder.append(mibs);
        builder.append(", community=");
        builder.append(community);
        builder.append(", writeCommunity=");
        builder.append(writeCommunity);
        builder.append(", securityLevel=");
        builder.append(securityLevel);
        builder.append(", authProtocol=");
        builder.append(authProtocol);
        builder.append(", privProtocol=");
        builder.append(privProtocol);
        builder.append(", authPassword=");
        builder.append(authPassword);
        builder.append(", privPassword=");
        builder.append(privPassword);
        builder.append(", authoritativeEngineID=");
        builder.append(authoritativeEngineID);
        builder.append(", contextName=");
        builder.append(contextName);
        builder.append(", contextId=");
        builder.append(contextId);
        builder.append("]");
        return builder.toString();
    }

    /**
     * @return the version
     */
    public int getVersion() {
        return version;
    }

    /**
     * @param version
     *            the version to set
     */
    public void setVersion(int version) {
        this.version = version;
    }

    /**
     * @return the timeout
     */
    public long getTimeout() {
        return timeout;
    }

    /**
     * @param timeout
     *            the timeout to set
     */
    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }

    /**
     * @return the retry
     */
    public byte getRetry() {
        return retry;
    }

    /**
     * @param retry
     *            the retry to set
     */
    public void setRetry(byte retry) {
        this.retry = retry;
    }

    /**
     * @return the port
     */
    public int getPort() {
        return port;
    }

    /**
     * @param port
     *            the port to set
     */
    public void setPort(int port) {
        this.port = port;
    }

    /**
     * @return the mibs
     */
    public String getMibs() {
        return mibs;
    }

    /**
     * @param mibs
     *            the mibs to set
     */
    public void setMibs(String mibs) {
        this.mibs = mibs;
    }

    /**
     * @return the community
     */
    public String getCommunity() {
        return community;
    }

    /**
     * @param community
     *            the community to set
     */
    public void setCommunity(String community) {
        this.community = community;
    }

    /**
     * @return the writeCommunity
     */
    public String getWriteCommunity() {
        return writeCommunity;
    }

    /**
     * @param writeCommunity
     *            the writeCommunity to set
     */
    public void setWriteCommunity(String writeCommunity) {
        this.writeCommunity = writeCommunity;
    }

    /**
     * @return the securityLevel
     */
    public int getSecurityLevel() {
        // Modify by Rod @EMS-4441
        if (authProtocol.equalsIgnoreCase("NOAUTH") && privProtocol.equalsIgnoreCase("NOPRIV")) {
            securityLevel = SecurityLevel.NOAUTH_NOPRIV;
        } else if ((!authProtocol.equalsIgnoreCase("NOAUTH")) && privProtocol.equalsIgnoreCase("NOPRIV")) {
            securityLevel = SecurityLevel.AUTH_NOPRIV;
        } else if ((!authProtocol.equalsIgnoreCase("NOAUTH")) && (!privProtocol.equalsIgnoreCase("NOPRIV"))) {
            securityLevel = SecurityLevel.AUTH_PRIV;
        }
        return securityLevel;
    }

    /**
     * @param securityLevel
     *            the securityLevel to set
     */
    public void setSecurityLevel(int securityLevel) {
        this.securityLevel = securityLevel;
    }

    /**
     * @return the authProtocol
     */
    public String getAuthProtocol() {
        return authProtocol;
    }

    /**
     * @param authProtocol
     *            the authProtocol to set
     */
    public void setAuthProtocol(String authProtocol) {
        this.authProtocol = authProtocol;
    }

    /**
     * @return the privProtocol
     */
    public String getPrivProtocol() {
        return privProtocol;
    }

    /**
     * @param privProtocol
     *            the privProtocol to set
     */
    public void setPrivProtocol(String privProtocol) {
        this.privProtocol = privProtocol;
    }

    /**
     * @return the authPassword
     */
    public String getAuthPassword() {
        if (NO_AUTH_PROTOCOL.equalsIgnoreCase(this.authProtocol)) {
            return DEFAULT_PASSWORD;
        }
        return authPassword;
    }

    /**
     * @param authPassword
     *            the authPassword to set
     */
    public void setAuthPassword(String authPassword) {
        this.authPassword = authPassword;
    }

    /**
     * @return the privPassword
     */
    public String getPrivPassword() {
        if (NO_PRIV_PROTOCOL.equalsIgnoreCase(this.privProtocol)) {
            return DEFAULT_PASSWORD;
        }
        return privPassword;
    }

    /**
     * @param privPassword
     *            the privPassword to set
     */
    public void setPrivPassword(String privPassword) {
        this.privPassword = privPassword;
    }

    /**
     * @return the authoritativeEngineID
     */
    public String getAuthoritativeEngineID() {
        return authoritativeEngineID;
    }

    /**
     * @param authoritativeEngineID
     *            the authoritativeEngineID to set
     */
    public void setAuthoritativeEngineID(String authoritativeEngineID) {
        this.authoritativeEngineID = authoritativeEngineID;
    }

    /**
     * @return the contextName
     */
    public String getContextName() {
        return contextName;
    }

    /**
     * @param contextName
     *            the contextName to set
     */
    public void setContextName(String contextName) {
        this.contextName = contextName;
    }

    /**
     * @return the contextId
     */
    public String getContextId() {
        return contextId;
    }

    /**
     * @param contextId
     *            the contextId to set
     */
    public void setContextId(String contextId) {
        this.contextId = contextId;
    }

    public String getDeviceVersion() {
        return deviceVersion;
    }

    public void setDeviceVersion(String deviceVersion) {
        this.deviceVersion = deviceVersion;
    }
}
