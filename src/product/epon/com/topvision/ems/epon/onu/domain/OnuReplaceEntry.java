/***********************************************************************
 * $Id: OnuReplaceEntry.java,v1.0 2016-4-18 上午11:24:47 $
 * 
 * @author: Rod John
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.onu.domain;

import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.dao.mybatis.AliasesSuperType;
import com.topvision.framework.utils.EponIndex;

/**
 * @author Rod John
 * @created @2016-4-18-上午11:24:47
 *
 */
public class OnuReplaceEntry implements AliasesSuperType {

    private static final long serialVersionUID = -6030890728300788459L;
    private Long entityId;
    private Long onuId;
    private Long onuIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.4.8.1.1", index = true)
    private Integer topOnuModifyCardIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.4.8.1.2", index = true)
    private Integer topOnuModifyPonIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.4.8.1.3", index = true)
    private Integer topOnuModifyOnuIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.4.8.1.4", writable = true, type = "OctetString")
    private String topOnuModifyMacAddress;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.4.8.1.5", writable = true, type = "OctetString")
    private String topOnuModifyLogicSn;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.4.8.1.6", writable = true, type = "OctetString")
    private String topOnuModifyPwd;

    /**
     * @return the entityId
     */
    public Long getEntityId() {
        return entityId;
    }

    /**
     * @param entityId the entityId to set
     */
    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    /**
     * @return the onuId
     */
    public Long getOnuId() {
        return onuId;
    }

    /**
     * @param onuId the onuId to set
     */
    public void setOnuId(Long onuId) {
        this.onuId = onuId;
    }

    /**
     * @return the onuIndex
     */
    public Long getOnuIndex() {
        return onuIndex;
    }

    /**
     * @param onuIndex the onuIndex to set
     */
    public void setOnuIndex(Long onuIndex) {
        this.onuIndex = onuIndex;
        topOnuModifyCardIndex = EponIndex.getSlotNo(onuIndex).intValue();
        topOnuModifyPonIndex = EponIndex.getPonNo(onuIndex).intValue();
        topOnuModifyOnuIndex = EponIndex.getOnuNo(onuIndex).intValue();
    }

    /**
     * @return the topOnuModifyCardIndex
     */
    public Integer getTopOnuModifyCardIndex() {
        return topOnuModifyCardIndex;
    }

    /**
     * @param topOnuModifyCardIndex the topOnuModifyCardIndex to set
     */
    public void setTopOnuModifyCardIndex(Integer topOnuModifyCardIndex) {
        this.topOnuModifyCardIndex = topOnuModifyCardIndex;
    }

    /**
     * @return the topOnuModifyPonIndex
     */
    public Integer getTopOnuModifyPonIndex() {
        return topOnuModifyPonIndex;
    }

    /**
     * @param topOnuModifyPonIndex the topOnuModifyPonIndex to set
     */
    public void setTopOnuModifyPonIndex(Integer topOnuModifyPonIndex) {
        this.topOnuModifyPonIndex = topOnuModifyPonIndex;
    }

    /**
     * @return the topOnuModifyOnuIndex
     */
    public Integer getTopOnuModifyOnuIndex() {
        return topOnuModifyOnuIndex;
    }

    /**
     * @param topOnuModifyOnuIndex the topOnuModifyOnuIndex to set
     */
    public void setTopOnuModifyOnuIndex(Integer topOnuModifyOnuIndex) {
        this.topOnuModifyOnuIndex = topOnuModifyOnuIndex;
    }

    /**
     * @return the topOnuModifyMacAddress
     */
    public String getTopOnuModifyMacAddress() {
        return topOnuModifyMacAddress;
    }

    /**
     * @param topOnuModifyMacAddress the topOnuModifyMacAddress to set
     */
    public void setTopOnuModifyMacAddress(String topOnuModifyMacAddress) {
        this.topOnuModifyMacAddress = topOnuModifyMacAddress;
    }

    /**
     * @return the topOnuModifyLogicSn
     */
    public String getTopOnuModifyLogicSn() {
        return topOnuModifyLogicSn;
    }

    /**
     * @param topOnuModifyLogicSn the topOnuModifyLogicSn to set
     */
    public void setTopOnuModifyLogicSn(String topOnuModifyLogicSn) {
        this.topOnuModifyLogicSn = topOnuModifyLogicSn;
    }

    /**
     * @return the topOnuModifyPwd
     */
    public String getTopOnuModifyPwd() {
        return topOnuModifyPwd;
    }

    /**
     * @param topOnuModifyPwd the topOnuModifyPwd to set
     */
    public void setTopOnuModifyPwd(String topOnuModifyPwd) {
        this.topOnuModifyPwd = topOnuModifyPwd;
    }

}
