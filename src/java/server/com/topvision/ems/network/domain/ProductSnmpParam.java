/***********************************************************************
 * $Id: ProductSnmpParam.java,v1.0 2012-2-23 下午02:29:25 $
 * 
 * @author: loyal
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.network.domain;

import com.topvision.framework.snmp.SnmpParam;

/**
 * @author loyal
 * @created @2012-2-23-下午02:29:25
 * 
 */
public class ProductSnmpParam extends SnmpParam {
    private static final long serialVersionUID = 2019896427154605709L;
    private Long productId;
    private String productType;
    private String productIp;
    private int version;
    private long timeout;
    private byte retry;
    private int port;
    private String mibs;
    private String readCommunity;
    private String writeCommunity;
    private String username;
    private int securityLevel;
    private String authProtocol;
    private String privProtocol;
    private String authPassword;
    private String privPassword;
    private String authoritativeEngineID;
    private String contextName;
    private String contextId;

    public ProductSnmpParam() {
        this.version = 1;
        this.timeout = 10000L;
        this.retry = 0;
        this.port = 161;
        this.mibs = "RFC1213-MIB";

        this.readCommunity = "public";
        this.writeCommunity = "private";

        this.securityLevel = 1;
        this.authProtocol = "MD5";
        this.privProtocol = "CBC-DES";
        this.authPassword = null;
        this.privPassword = null;
        this.authoritativeEngineID = null;
        this.contextName = null;
        this.contextId = null;
    }

    /**
     * 从SnmpParam克隆
     */
    public static ProductSnmpParam clone(SnmpParam sp) {
        ProductSnmpParam param = new ProductSnmpParam();
        param.setAuthoritativeEngineID(sp.getAuthoritativeEngineID());
        param.setAuthPassword(sp.getAuthPassword());
        param.setAuthProtocol(sp.getAuthProtocol());
        param.setReadCommunity(sp.getCommunity());
        param.setWriteCommunity(sp.getWriteCommunity());
        // param.setCommunity(sp.getCommunity());
        param.setContextId(sp.getContextId());
        param.setContextName(sp.getContextName());
        param.setEntityId(sp.getEntityId());
        param.setIpAddress(sp.getIpAddress());
        param.setProductIp(sp.getIpAddress());
        param.setMibs(sp.getMibs());
        param.setPort(sp.getPort());
        param.setPrivPassword(sp.getPrivPassword());
        param.setPrivProtocol(sp.getPrivProtocol());
        param.setRetry(sp.getRetry());
        param.setSecurityLevel(sp.getSecurityLevel());
        param.setTimeout(sp.getTimeout());
        param.setUsername(sp.getUsername());
        param.setVersion(sp.getVersion());

        return param;
    }

    /**
     * 从productSnmpParam克隆到SnmpParam
     */
    public static SnmpParam cloneFromProductSnmp(ProductSnmpParam psp) {
//        SnmpParam snmpParam = new SnmpParam();
//
//        snmpParam.setAuthoritativeEngineID(psp.getAuthoritativeEngineID());
//        snmpParam.setAuthPassword(psp.getAuthPassword());
//        snmpParam.setAuthProtocol(psp.getAuthProtocol());
//        snmpParam.setWriteCommunity(psp.getWriteCommunity());
//        snmpParam.setCommunity(psp.getReadCommunity());
//        snmpParam.setContextId(psp.getContextId());
//        snmpParam.setContextName(psp.getContextName());
//        snmpParam.setEntityId(psp.getEntityId());
//        snmpParam.setIpAddress(psp.getProductIp());
//        // snmpParam.setProductIp(psp.getIpAddress());
//        snmpParam.setMibs(psp.getMibs());
//        snmpParam.setPort(psp.getPort());
//        snmpParam.setPrivPassword(psp.getPrivPassword());
//        snmpParam.setPrivProtocol(psp.getPrivProtocol());
//        snmpParam.setRetry(psp.getRetry());
//        snmpParam.setSecurityLevel(psp.getSecurityLevel());
//        snmpParam.setTimeout(psp.getTimeout());
//        snmpParam.setUsername(psp.getUsername());
//        snmpParam.setVersion(psp.getVersion());

        return psp;
    }

    /**
     * @return the productId
     */
    public Long getProductId() {
        return productId;
    }

    /**
     * @param productId
     *            the productId to set
     */
    public void setProductId(Long productId) {
        this.productId = productId;
    }

    /**
     * @return the productType
     */
    public String getProductType() {
        return productType;
    }

    /**
     * @param productType
     *            the productType to set
     */
    public void setProductType(String productType) {
        this.productType = productType;
    }

    /**
     * @return the version
     */
    public int getVersion() {
        return version;
    }

    /**
     * @return the productIp
     */
    public String getProductIp() {
        return productIp;
    }

    /**
     * @param productIp
     *            the productIp to set
     */
    public void setProductIp(String productIp) {
        this.productIp = productIp;
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
     * @return the readCommunity
     */
    public String getReadCommunity() {
        return readCommunity;
    }

    /**
     * @param readCommunity
     *            the readCommunity to set
     */
    public void setReadCommunity(String readCommunity) {
        this.readCommunity = readCommunity;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("ProductSnmpParam [productId=");
        builder.append(productId);
        builder.append(", productType=");
        builder.append(productType);
        builder.append(", productIp=");
        builder.append(productIp);
        builder.append(", version=");
        builder.append(version);
        builder.append(", timeout=");
        builder.append(timeout);
        builder.append(", retry=");
        builder.append(retry);
        builder.append(", port=");
        builder.append(port);
        builder.append(", mibs=");
        builder.append(mibs);
        builder.append(", readCommunity=");
        builder.append(readCommunity);
        builder.append(", writeCommunity=");
        builder.append(writeCommunity);
        builder.append(", username=");
        builder.append(username);
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
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username
     *            the username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @return the securityLevel
     */
    public int getSecurityLevel() {
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

}