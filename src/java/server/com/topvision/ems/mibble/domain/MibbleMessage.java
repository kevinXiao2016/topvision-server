package com.topvision.ems.mibble.domain;

/**
 * 
 * @author Bravin
 * 
 */
public class MibbleMessage {
    private String oid;
    private String rawOid;
    private String name;
    private String value;
    private String sessionId;/* 表示词条记录是谁操作出来的 */

    /**
     * @return the oid
     */
    public String getOid() {
        return oid;
    }

    /**
     * @param oid
     *            the oid to set
     */
    public void setOid(String oid) {
        this.oid = oid;
    }

    /**
     * @return the rawOid
     */
    public String getRawOid() {
        return rawOid;
    }

    /**
     * @param rawOid
     *            the rawOid to set
     */
    public void setRawOid(String rawOid) {
        this.rawOid = rawOid;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name
     *            the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the value
     */
    public String getValue() {
        return value;
    }

    /**
     * @param value
     *            the value to set
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * @return the sessionId
     */
    public String getSessionId() {
        return sessionId;
    }

    /**
     * @param sessionId2
     *            the sessionId to set
     */
    public void setSessionId(String sessionId2) {
        this.sessionId = sessionId2;
    }

}
