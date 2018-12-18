/***********************************************************************
 * $Id: CmcSnmpCommunityTable.java,v1.0 2013-4-26 上午09:06:45 $
 * 
 * @author: Rod John
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.config.facade.domain;

import java.io.Serializable;

import org.apache.ibatis.type.Alias;

import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author Rod John
 * @created @2013-4-26-上午09:06:45
 * 
 */
@Alias("cmcSnmpCommunityTable")
public class CmcSnmpCommunityTable implements Serializable, AliasesSuperType {

    private static final long serialVersionUID = -6669302598571760461L;
    private static final String WRITE_INDEX = "1";
    private static final String READ_INDEX = "2";
    private Long entityId;
    @SnmpProperty(oid = "1.3.6.1.6.3.18.1.1.1.1", index = true,type = "OcterString")
    private String snmpCommunityIndex;
    @SnmpProperty(oid = "1.3.6.1.6.3.18.1.1.1.2", writable = true, type = "OcterString")
    private String snmpCommunityName;
    @SnmpProperty(oid = "1.3.6.1.6.3.18.1.1.1.8", writable = true, type = "Integer")
    private Integer snmpCommunityStatus;
    private String readCommunity;
    private String writeCommunity;

    public static CmcSnmpCommunityTable getReadTable(Long entityId, String readCommunity) {
        CmcSnmpCommunityTable readTable = new CmcSnmpCommunityTable();
        readTable.setEntityId(entityId); 
        readTable.setSnmpCommunityIndex(READ_INDEX);
        readTable.setSnmpCommunityName(readCommunity);
        return readTable;
    }

    public static CmcSnmpCommunityTable getWriteTable(Long entityId, String writeCommunity) {
        CmcSnmpCommunityTable writeTable = new CmcSnmpCommunityTable();
        writeTable.setEntityId(entityId);
        writeTable.setSnmpCommunityIndex(WRITE_INDEX);
        writeTable.setSnmpCommunityName(writeCommunity);
        return writeTable;
    }

    /**
     * @return the snmpCommunityIndex
     */
    public String getSnmpCommunityIndex() {
        return snmpCommunityIndex;
    }

    /**
     * @param snmpCommunityIndex
     *            the snmpCommunityIndex to set
     */
    public void setSnmpCommunityIndex(String snmpCommunityIndex) {
        this.snmpCommunityIndex = snmpCommunityIndex;
    }

    /**
     * @return the snmpCommunityName
     */
    public String getSnmpCommunityName() {
        return snmpCommunityName;
    }

    /**
     * @param snmpCommunityName
     *            the snmpCommunityName to set
     */
    public void setSnmpCommunityName(String snmpCommunityName) {
        this.snmpCommunityName = snmpCommunityName;
    }

    /**
     * @return the snmpCommunityStatus
     */
    public Integer getSnmpCommunityStatus() {
        return snmpCommunityStatus;
    }

    /**
     * @param snmpCommunityStatus
     *            the snmpCommunityStatus to set
     */
    public void setSnmpCommunityStatus(Integer snmpCommunityStatus) {
        this.snmpCommunityStatus = snmpCommunityStatus;
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
     * @return the readCommunity
     */
    public String getReadCommunity() {
        return readCommunity;
    }

    /**
     * @param readCommunity the readCommunity to set
     */
    public void setReadCommunity(String readCommunity) {
        this.readCommunity = readCommunity;
    }

    /**
     * @return the writeCommunity
     */
    public String getWriteCommunity() {
        return writeCommunity;
    }

    /**
     * @param writeCommunity the writeCommunity to set
     */
    public void setWriteCommunity(String writeCommunity) {
        this.writeCommunity = writeCommunity;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("CmcSnmpCommunityTable [entityId=");
        builder.append(entityId);
        builder.append(", snmpCommunityIndex=");
        builder.append(snmpCommunityIndex);
        builder.append(", snmpCommunityName=");
        builder.append(snmpCommunityName);
        builder.append(", snmpCommunityStatus=");
        builder.append(snmpCommunityStatus);
        builder.append("]");
        return builder.toString();
    }

}
