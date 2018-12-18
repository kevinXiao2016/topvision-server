/***********************************************************************
 * $Id: OltOnuBlockAuthen.java,v1.0 2011-10-20 上午09:43:07 $
 * 
 * @author: huqiao
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.onuauth.domain;

import java.io.Serializable;

import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.dao.mybatis.AliasesSuperType;
import com.topvision.framework.utils.EponIndex;

/**
 * @author huqiao
 * @created @2011-10-20-上午09:43:07
 * 
 */
public class OltOnuBlockAuthen implements Serializable, AliasesSuperType {
    private static final long serialVersionUID = -6494386908850472316L;
    private Long entityId;
    private Long ponId;
    private Long onuIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.4.5.3.1.1,V1.10:1.3.6.1.4.1.32285.11.2.3.4.2.5.1.1", index = true)
    private Long onuMibIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.4.5.3.1.2,V1.10:1.3.6.1.4.1.32285.11.2.3.4.2.5.1.2")
    private String macAddress;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.4.5.3.1.3,V1.10:1.3.6.1.4.1.32285.11.2.3.4.2.5.1.3")
    private Long authTime;
    private String topOnuAuthBlockedExtLogicSn;
    private String topOnuAuthBlockedExtPwd;

    /**
     * @return the onuMibIndex
     */
    public Long getOnuMibIndex() {
        return onuMibIndex;
    }

    /**
     * @param onuMibIndex
     *            the onuMibIndex to set
     */
    public void setOnuMibIndex(Long onuMibIndex) {
        this.onuMibIndex = onuMibIndex;
        onuIndex = EponIndex.getOnuIndexByMibIndex(onuMibIndex);
    }

    /**
     * @return the macAddress
     */
    public String getMacAddress() {
        return macAddress;
    }

    /**
     * @param macAddress
     *            the macAddress to set
     */
    public void setMacAddress(String macAddress) {
        //需要格式化MAC地址为数据库存储的无间隔的标准格式
        this.macAddress = macAddress;
    }

    /**
     * @return the authTime
     */
    public Long getAuthTime() {
        return authTime;
    }

    /**
     * @param authTime
     *            the authTime to set
     */
    public void setAuthTime(Long authTime) {
        this.authTime = authTime;
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
     * @return the ponId
     */
    public Long getPonId() {
        return ponId;
    }

    /**
     * @param ponId
     *            the ponId to set
     */
    public void setPonId(Long ponId) {
        this.ponId = ponId;
    }

    /**
     * @return the onuIndex
     */
    public Long getOnuIndex() {
        return onuIndex;
    }

    /**
     * @param onuIndex
     *            the onuIndex to set
     */
    public void setOnuIndex(Long onuIndex) {
        this.onuIndex = onuIndex;
        onuMibIndex = EponIndex.getOnuMibIndexByIndex(onuIndex);
    }

    public String getTopOnuAuthBlockedExtLogicSn() {
        return topOnuAuthBlockedExtLogicSn;
    }

    public void setTopOnuAuthBlockedExtLogicSn(String topOnuAuthBlockedExtLogicSn) {
        this.topOnuAuthBlockedExtLogicSn = topOnuAuthBlockedExtLogicSn;
    }

    public String getTopOnuAuthBlockedExtPwd() {
        return topOnuAuthBlockedExtPwd;
    }

    public void setTopOnuAuthBlockedExtPwd(String topOnuAuthBlockedExtPwd) {
        this.topOnuAuthBlockedExtPwd = topOnuAuthBlockedExtPwd;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("OltOnuBlockAuthen");
        sb.append("{authTime=").append(authTime);
        sb.append(", entityId=").append(entityId);
        sb.append(", ponId=").append(ponId);
        sb.append(", onuIndex=").append(onuIndex);
        sb.append(", onuMibIndex=").append(onuMibIndex);
        sb.append(", macAddress='").append(macAddress).append('\'');
        sb.append(", topOnuAuthBlockedExtLogicSn='").append(topOnuAuthBlockedExtLogicSn).append('\'');
        sb.append(", topOnuAuthBlockedExtPwd='").append(topOnuAuthBlockedExtPwd).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
