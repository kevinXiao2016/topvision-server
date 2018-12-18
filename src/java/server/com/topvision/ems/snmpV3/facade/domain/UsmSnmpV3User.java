/***********************************************************************
 * $Id: UsmSnmpV3User.java,v1.0 2013-1-9 上午9:23:18 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.snmpV3.facade.domain;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.ibatis.type.Alias;

import com.topvision.ems.snmpV3.exception.UnCorrectEngineLengthException;
import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.common.ByteUtils;
import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author Bravin
 * @created @2013-1-9-上午9:23:18
 * 
 */
@Alias("usmSnmpV3User")
public class UsmSnmpV3User implements AliasesSuperType {
    private static final long serialVersionUID = 478020920559135768L;
    private static final Map<String, String> PROTOCOL_MAP;
    static {
        PROTOCOL_MAP = new HashMap<String, String>();
        PROTOCOL_MAP.put("NOAUTH", "1.3.6.1.6.3.10.1.1.1");
        PROTOCOL_MAP.put("MD5", "1.3.6.1.6.3.10.1.1.2");
        PROTOCOL_MAP.put("SHA", "1.3.6.1.6.3.10.1.1.3");
        PROTOCOL_MAP.put("NOPRIV", "1.3.6.1.6.3.10.1.2.1");
        PROTOCOL_MAP.put("CBC-DES", "1.3.6.1.6.3.10.1.2.2");

        PROTOCOL_MAP.put("1.3.6.1.6.3.10.1.1.1", "NOAUTH");
        PROTOCOL_MAP.put("1.3.6.1.6.3.10.1.1.2", "MD5");
        PROTOCOL_MAP.put("1.3.6.1.6.3.10.1.1.3", "SHA");
        PROTOCOL_MAP.put("1.3.6.1.6.3.10.1.2.1", "NOPRIV");
        PROTOCOL_MAP.put("1.3.6.1.6.3.10.1.2.2", "CBC-DES");
    }
    private Long entityId;
    @SnmpProperty(oid = "1.3.6.1.6.3.15.1.2.2.1.1", index = true)
    private byte[] snmpUserEngineOid;
    private String snmpUserEngineId;
    @SnmpProperty(oid = "1.3.6.1.6.3.15.1.2.2.1.2", index = true)
    private String snmpUserName;
    // READ ONLY
    @SnmpProperty(oid = "1.3.6.1.6.3.15.1.2.2.1.3")
    private String snmpSecurityName;
    @SnmpProperty(oid = "1.3.6.1.6.3.15.1.2.2.1.4", writable = true, type = "OBJECT IDENTIFIER")
    private String snmpCloneFrom;
    @SnmpProperty(oid = "1.3.6.1.6.3.15.1.2.2.1.5", writable = true, type = "OBJECT IDENTIFIER")
    private String snmpAuthProtocolOid;
    private String snmpAuthProtocol;
    @SnmpProperty(oid = "1.3.6.1.6.3.15.1.2.2.1.6", writable = true, type = "OctetString")
    private String snmpAuthKeyChange;
    @SnmpProperty(oid = "1.3.6.1.6.3.15.1.2.2.1.7", writable = true, type = "OctetString")
    private String snmpOwnAuthKeyChange;
    @SnmpProperty(oid = "1.3.6.1.6.3.15.1.2.2.1.8", writable = true, type = "OBJECT IDENTIFIER")
    private String snmpPrivProtocolOid;
    private String snmpPrivProtocol;
    @SnmpProperty(oid = "1.3.6.1.6.3.15.1.2.2.1.9", writable = true, type = "OctetString")
    private String snmpPrivKeyChange;
    @SnmpProperty(oid = "1.3.6.1.6.3.15.1.2.2.1.10", writable = true, type = "OctetString")
    private String snmpOwnPrivKeyChange;
    @SnmpProperty(oid = "1.3.6.1.6.3.15.1.2.2.1.11", writable = true, type = "OctetString")
    private String snmpUserPublic;
    @SnmpProperty(oid = "1.3.6.1.6.3.15.1.2.2.1.12", writable = true, type = "Integer32")
    private Integer usmUserStorageType;
    @SnmpProperty(oid = "1.3.6.1.6.3.15.1.2.2.1.13", writable = true, type = "Integer32")
    private Integer usmUserStatus;
    private String snmpGroupName;
    private Integer snmpSecurityMode;
    private Integer useNotifyUser;

    public static String getCloneUserOid(String engineId, String userName) {
        StringBuilder sb = new StringBuilder();
        sb.append("1.3.6.1.6.3.15.1.2.2.1.3.");
        // byte[] uEngineId = MPv3.createLocalEngineID().toString().getBytes();\
        if (engineId.length() % 2 != 0) {
            throw new UnCorrectEngineLengthException();
        } else {
            sb.append(engineId.length() / 2);
            for (int i = 0; i < engineId.length() / 2; i++) {
                sb.append(".").append(Integer.parseInt(engineId.substring(2 * i, 2 * i + 2), 16));
            }
        }
        // String[] e = engineId.split(":");
        // sb.append(e.length);
        // if (e.length % 2 != 0) {
        // throw new UnCorrectEngineLengthException();
        // } else {
        // for (int i = 0; i < e.length; i++) {
        // sb.append(".").append(Integer.parseInt(e[i], 16));
        // }
        // }
        byte[] uByte = userName.getBytes();
        sb.append(".");
        sb.append(uByte.length);
        for (byte b : uByte) {
            sb.append(".").append(b);
        }
        return sb.toString();
    }

    public static String getUserPublicOid(String engineId, String userName) {
        StringBuilder sb = new StringBuilder();
        sb.append("1.3.6.1.6.3.15.1.2.2.1.11.");
        // byte[] uEngineId = MPv3.createLocalEngineID().toString().getBytes();\
        // if (engineId.length() % 2 != 0) {
        // throw new UnCorrectEngineLengthException();
        // } else {
        // sb.append(engineId.length() / 2);
        // for (int i = 0; i < engineId.length() / 2; i++) {
        // sb.append(".").append(Integer.parseInt(engineId.substring(2 * i, 2 * i + 2), 16));
        // }
        // }
        String[] e = engineId.split(":");
        sb.append(e.length);
        if (e.length % 2 != 0) {
            throw new UnCorrectEngineLengthException();
        } else {
            for (int i = 0; i < e.length; i++) {
                sb.append(".").append(Integer.parseInt(e[i], 16));
            }
        }
        byte[] uByte = userName.getBytes();
        sb.append(".");
        sb.append(uByte.length);
        for (byte b : uByte) {
            sb.append(".").append(b);
        }
        return sb.toString();
    }

    public static String getByteStringFromNoISOControl(String noISOControlString) {
        Pattern hexStringPattern = Pattern.compile("^([0-9a-fA-F]{2})([:][0-9a-fA-F]{2})+$");
        if (!hexStringPattern.matcher(noISOControlString).matches()) {
            String result = "";
            char[] charArray = noISOControlString.toCharArray();
            for (char c : charArray) {
                result += Integer.toHexString(c);
            }
            return result.substring(0, result.length() - 1);
        }
        return noISOControlString;
    }

    /**
     * @return the entityId
     */
    public Long getEntityId() {
        return entityId;
    }

    /**
     * @param entityId
     *            the entityId to set
     */
    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    /**
     * @return the snmpUserEngineId
     */
    public String getSnmpUserEngineId() {
        return snmpUserEngineId;
    }

    /**
     * @param snmpUserEngineId
     *            the snmpUserEngineId to set
     */
    public void setSnmpUserEngineId(String snmpUserEngineId) {
        this.snmpUserEngineId = snmpUserEngineId;
    }

    /**
     * @return the snmpUserName
     */
    public String getSnmpUserName() {
        return snmpUserName;
    }

    /**
     * @param snmpUserName
     *            the snmpUserName to set
     */
    public void setSnmpUserName(String snmpUserName) {
        this.snmpUserName = snmpUserName;
    }

    /**
     * @return the snmpSecurityName
     */
    public String getSnmpSecurityName() {
        return snmpSecurityName;
    }

    /**
     * @param snmpSecurityName
     *            the snmpSecurityName to set
     */
    public void setSnmpSecurityName(String snmpSecurityName) {
        this.snmpSecurityName = snmpSecurityName;
    }

    /**
     * @return the snmpCloneFrom
     */
    public String getSnmpCloneFrom() {
        return snmpCloneFrom;
    }

    /**
     * @param snmpCloneFrom
     *            the snmpCloneFrom to set
     */
    public void setSnmpCloneFrom(String snmpCloneFrom) {
        this.snmpCloneFrom = snmpCloneFrom;
    }

    /**
     * @return the snmpAuthProtocol
     */
    public String getSnmpAuthProtocol() {
        return snmpAuthProtocol;
    }

    /**
     * @param snmpAuthProtocol
     *            the snmpAuthProtocol to set
     */
    public void setSnmpAuthProtocol(String snmpAuthProtocol) {
        this.snmpAuthProtocolOid = PROTOCOL_MAP.get(snmpAuthProtocol);
        this.snmpAuthProtocol = snmpAuthProtocol;
    }

    /**
     * @return the snmpAuthKeyChange
     */
    public String getSnmpAuthKeyChange() {
        return snmpAuthKeyChange;
    }

    /**
     * @param snmpAuthKeyChange
     *            the snmpAuthKeyChange to set
     */
    public void setSnmpAuthKeyChange(String snmpAuthKeyChange) {
        this.snmpAuthKeyChange = snmpAuthKeyChange;
    }

    /**
     * @return the snmpOwnAuthKeyChange
     */
    public String getSnmpOwnAuthKeyChange() {
        return snmpOwnAuthKeyChange;
    }

    /**
     * @param snmpOwnAuthKeyChange
     *            the snmpOwnAuthKeyChange to set
     */
    public void setSnmpOwnAuthKeyChange(String snmpOwnAuthKeyChange) {
        this.snmpOwnAuthKeyChange = snmpOwnAuthKeyChange;
    }

    /**
     * @return the snmpPrivProtocol
     */
    public String getSnmpPrivProtocol() {
        return snmpPrivProtocol;
    }

    /**
     * @param snmpPrivProtocol
     *            the snmpPrivProtocol to set
     */
    public void setSnmpPrivProtocol(String snmpPrivProtocol) {
        this.snmpPrivProtocolOid = PROTOCOL_MAP.get(snmpPrivProtocol);
        this.snmpPrivProtocol = snmpPrivProtocol;

    }

    /**
     * @return the snmpPrivKeyChange
     */
    public String getSnmpPrivKeyChange() {
        return snmpPrivKeyChange;
    }

    /**
     * @param snmpPrivKeyChange
     *            the snmpPrivKeyChange to set
     */
    public void setSnmpPrivKeyChange(String snmpPrivKeyChange) {
        this.snmpPrivKeyChange = snmpPrivKeyChange;
    }

    /**
     * @return the snmpOwnPrivKeyChange
     */
    public String getSnmpOwnPrivKeyChange() {
        return snmpOwnPrivKeyChange;
    }

    /**
     * @param snmpOwnPrivKeyChange
     *            the snmpOwnPrivKeyChange to set
     */
    public void setSnmpOwnPrivKeyChange(String snmpOwnPrivKeyChange) {
        this.snmpOwnPrivKeyChange = snmpOwnPrivKeyChange;
    }

    /**
     * @return the snmpUserPublic
     */
    public String getSnmpUserPublic() {
        return snmpUserPublic;
    }

    /**
     * @param snmpUserPublic
     *            the snmpUserPublic to set
     */
    public void setSnmpUserPublic(String snmpUserPublic) {
        this.snmpUserPublic = snmpUserPublic;
    }

    /**
     * @return the usmUserStorageType
     */
    public Integer getUsmUserStorageType() {
        return usmUserStorageType;
    }

    /**
     * @param usmUserStorageType
     *            the usmUserStorageType to set
     */
    public void setUsmUserStorageType(Integer usmUserStorageType) {
        this.usmUserStorageType = usmUserStorageType;
    }

    /**
     * @return the usmUserStatus
     */
    public Integer getUsmUserStatus() {
        return usmUserStatus;
    }

    /**
     * @param usmUserStatus
     *            the usmUserStatus to set
     */
    public void setUsmUserStatus(Integer usmUserStatus) {
        this.usmUserStatus = usmUserStatus;
    }

    /**
     * @return the snmpGroupName
     */
    public String getSnmpGroupName() {
        return snmpGroupName;
    }

    /**
     * @param snmpGroupName
     *            the snmpGroupName to set
     */
    public void setSnmpGroupName(String snmpGroupName) {
        this.snmpGroupName = snmpGroupName;
    }

    /**
     * @return the snmpSecurityMode
     */
    public Integer getSnmpSecurityMode() {
        return snmpSecurityMode;
    }

    /**
     * @param snmpSecurityMode
     *            the snmpSecurityMode to set
     */
    public void setSnmpSecurityMode(Integer snmpSecurityMode) {
        this.snmpSecurityMode = snmpSecurityMode;
    }

    /**
     * @return the snmpAuthProtocolOid
     */
    public String getSnmpAuthProtocolOid() {
        return snmpAuthProtocolOid;
    }

    /**
     * @param snmpAuthProtocolOid
     *            the snmpAuthProtocolOid to set
     */
    public void setSnmpAuthProtocolOid(String snmpAuthProtocolOid) {
        this.snmpAuthProtocolOid = snmpAuthProtocolOid;
        this.snmpAuthProtocol = PROTOCOL_MAP.get(snmpAuthProtocolOid);
    }

    /**
     * @return the snmpPrivProtocolOid
     */
    public String getSnmpPrivProtocolOid() {
        return snmpPrivProtocolOid;
    }

    /**
     * @param snmpPrivProtocolOid
     *            the snmpPrivProtocolOid to set
     */
    public void setSnmpPrivProtocolOid(String snmpPrivProtocolOid) {
        this.snmpPrivProtocolOid = snmpPrivProtocolOid;
        this.snmpPrivProtocol = PROTOCOL_MAP.get(snmpPrivProtocolOid);
    }

    /**
     * @return the snmpUserEngineOid
     */
    public byte[] getSnmpUserEngineOid() {
        if (snmpUserEngineOid == null) {
            snmpUserEngineOid = ByteUtils.toBytes(snmpUserEngineId);
        }
        return snmpUserEngineOid;
    }

    /**
     * @param snmpUserEngineOid
     *            the snmpUserEngineOid to set
     */
    public void setSnmpUserEngineOid(byte[] snmpUserEngineOid) {
        this.snmpUserEngineId = ByteUtils.toString(snmpUserEngineOid);
        this.snmpUserEngineOid = snmpUserEngineOid;
    }

    /**
     * @return the useNotifyUser
     */
    public Integer getUseNotifyUser() {
        return useNotifyUser;
    }

    /**
     * @param useNotifyUser
     *            the useNotifyUser to set
     */
    public void setUseNotifyUser(Integer useNotifyUser) {
        this.useNotifyUser = useNotifyUser;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("UsmSnmpV3User [entityId=");
        builder.append(entityId);
        builder.append(", snmpUserEngineId=");
        builder.append(snmpUserEngineId);
        builder.append(", snmpUserName=");
        builder.append(snmpUserName);
        builder.append(", snmpSecurityName=");
        builder.append(snmpSecurityName);
        builder.append(", snmpCloneFrom=");
        builder.append(snmpCloneFrom);
        builder.append(", snmpAuthProtocol=");
        builder.append(snmpAuthProtocol);
        builder.append(", snmpPrivProtocol=");
        builder.append(snmpPrivProtocol);
        builder.append(", snmpUserPublic=");
        builder.append(snmpUserPublic);
        builder.append(", usmUserStorageType=");
        builder.append(usmUserStorageType);
        builder.append(", usmUserStatus=");
        builder.append(usmUserStatus);
        builder.append("]");
        return builder.toString();
    }

}
