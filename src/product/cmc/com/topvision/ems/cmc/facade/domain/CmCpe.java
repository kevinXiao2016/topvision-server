/***********************************************************************
 * $Id: CmCpe.java,v1.0 2013-4-26 上午10:07:32 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.facade.domain;

import java.io.Serializable;
import java.sql.Timestamp;

import org.apache.ibatis.type.Alias;

import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.dao.mybatis.AliasesSuperType;
import com.topvision.framework.domain.PhysAddress;

/**
 * @author haojie
 * @created @2013-4-26-上午10:07:32
 * 
 */
@Alias("cmCpe")
public class CmCpe implements Serializable, AliasesSuperType {
    private static final long serialVersionUID = 1389489881051008023L;
    private Long cmId;
    private Long entityId;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.10.1.1.1", index = true)
    private PhysAddress topCmCpeMacAddress;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.10.1.1.2")
    private Integer topCmCpeType;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.10.1.1.3")
    private String topCmCpeIpAddress;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.10.1.1.4")
    private Long topCmCpeCcmtsIfIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.10.1.1.5")
    private Long topCmCpeCmStatusIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.10.1.1.6")
    private String topCmCpeToCmMacAddr;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.10.1.1.7", writable = true, type = "Integer32")
    private Integer topCmCpeStatus;
    private String topCmCpeMacAddressString;
    private String topCmCpeTypeString;
    private Timestamp updateTime;

    public Long getCmId() {
        return cmId;
    }

    public void setCmId(Long cmId) {
        this.cmId = cmId;
    }

    public Integer getTopCmCpeType() {
        return topCmCpeType;
    }

    public void setTopCmCpeType(Integer topCmCpeType) {
        this.topCmCpeType = topCmCpeType;
    }

    public String getTopCmCpeIpAddress() {
        return topCmCpeIpAddress;
    }

    public void setTopCmCpeIpAddress(String topCmCpeIpAddress) {
        this.topCmCpeIpAddress = topCmCpeIpAddress;
    }

    public Long getTopCmCpeCcmtsIfIndex() {
        return topCmCpeCcmtsIfIndex;
    }

    public void setTopCmCpeCcmtsIfIndex(Long topCmCpeCcmtsIfIndex) {
        this.topCmCpeCcmtsIfIndex = topCmCpeCcmtsIfIndex;
    }

    public Long getTopCmCpeCmStatusIndex() {
        return topCmCpeCmStatusIndex;
    }

    public void setTopCmCpeCmStatusIndex(Long topCmCpeCmStatusIndex) {
        this.topCmCpeCmStatusIndex = topCmCpeCmStatusIndex;
    }

    public String getTopCmCpeToCmMacAddr() {
        return topCmCpeToCmMacAddr;
    }

    public void setTopCmCpeToCmMacAddr(String topCmCpeToCmMacAddr) {
        this.topCmCpeToCmMacAddr = topCmCpeToCmMacAddr;
    }
    
    public String getTopCmCpeTypeStr() {
        return getTopCmCpeTypeString();
    }
    
    public String getTopCmCpeTypeString() {
        if (this.topCmCpeType != null) {
            switch (topCmCpeType) {
            case 1:
                topCmCpeTypeString = "HOST";
                break;
            case 2:
                topCmCpeTypeString = "MTA";
                break;
            case 3:
                topCmCpeTypeString = "STB";
                break;
            default:
                topCmCpeTypeString = "Extension Device";
                break;
            }
        }
        return topCmCpeTypeString;
    }

    public void setTopCmCpeTypeString(String topCmCpeTypeString) {
        this.topCmCpeTypeString = topCmCpeTypeString;
    }

    public PhysAddress getTopCmCpeMacAddress() {
        return topCmCpeMacAddress;
    }

    public void setTopCmCpeMacAddress(PhysAddress topCmCpeMacAddress) {
        this.topCmCpeMacAddress = topCmCpeMacAddress;
        this.topCmCpeMacAddressString = topCmCpeMacAddress.toString();
    }

    public String getTopCmCpeMacAddressString() {
        return topCmCpeMacAddressString;
    }

    public void setTopCmCpeMacAddressString(String topCmCpeMacAddressString) {
        this.topCmCpeMacAddressString = topCmCpeMacAddressString;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public Timestamp getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Timestamp updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getTopCmCpeStatus() {
        return topCmCpeStatus;
    }

    public void setTopCmCpeStatus(Integer topCmCpeStatus) {
        this.topCmCpeStatus = topCmCpeStatus;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("CmCpe [cmId=");
        builder.append(cmId);
        builder.append(", entityId=");
        builder.append(entityId);
        builder.append(", topCmCpeMacAddress=");
        builder.append(topCmCpeMacAddress);
        builder.append(", topCmCpeType=");
        builder.append(topCmCpeType);
        builder.append(", topCmCpeIpAddress=");
        builder.append(topCmCpeIpAddress);
        builder.append(", topCmCpeCcmtsIfIndex=");
        builder.append(topCmCpeCcmtsIfIndex);
        builder.append(", topCmCpeCmStatusIndex=");
        builder.append(topCmCpeCmStatusIndex);
        builder.append(", topCmCpeToCmMacAddr=");
        builder.append(topCmCpeToCmMacAddr);
        builder.append(", topCmCpeMacAddressString=");
        builder.append(topCmCpeMacAddressString);
        builder.append(", topCmCpeTypeString=");
        builder.append(topCmCpeTypeString);
        builder.append(", topCmCpeStatus=");
        builder.append(topCmCpeStatus);
        builder.append("]");
        return builder.toString();
    }

}
