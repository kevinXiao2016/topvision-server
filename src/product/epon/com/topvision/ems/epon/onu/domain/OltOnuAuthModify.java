/***********************************************************************
 * $ OltOnuAuthModify.java,v1.0 2012-3-27 16:30:28 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.onu.domain;

import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.utils.EponIndex;
import com.topvision.ems.epon.utils.EponUtil;

import java.io.Serializable;

/**
 * @author jay
 * @created @2012-3-27-16:30:28
 */
public class OltOnuAuthModify implements Serializable {
    private static final long serialVersionUID = 7342180206202447869L;
    private Long entityId;
    private Long onuIndex; // 系统中所用Index
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.4.8.1.1", index = true)
    private Long topOnuModifyCardIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.4.8.1.2", index = true)
    private Long topOnuModifyPonIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.4.8.1.3", index = true)
    private Long topOnuModifyOnuIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.4.8.1.4", writable = true, type = "OctetString")
    private String topOnuModifyMacAddress;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.4.8.1.5", writable = true, type = "OctetString")
    private String topOnuModifyLogicSn;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.4.8.1.6", writable = true, type = "OctetString")
    private String topOnuModifyPwd;

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public Long getOnuIndex() {
        onuIndex = EponIndex.getOnuIndex(topOnuModifyCardIndex.intValue(), topOnuModifyPonIndex.intValue(),
                topOnuModifyOnuIndex.intValue());
        return onuIndex;
    }

    public void setOnuIndex(Long onuIndex) {
        this.onuIndex = onuIndex;
        topOnuModifyCardIndex = EponIndex.getSlotNo(onuIndex);
        topOnuModifyPonIndex = EponIndex.getPonNo(onuIndex);
        topOnuModifyOnuIndex = EponIndex.getOnuNo(onuIndex);
    }

    public Long getTopOnuModifyCardIndex() {
        return topOnuModifyCardIndex;
    }

    public void setTopOnuModifyCardIndex(Long topOnuModifyCardIndex) {
        this.topOnuModifyCardIndex = topOnuModifyCardIndex;
    }

    public String getTopOnuModifyLogicSn() {
        return topOnuModifyLogicSn;
    }

    public void setTopOnuModifyLogicSn(String topOnuModifyLogicSn) {
        this.topOnuModifyLogicSn = topOnuModifyLogicSn;
    }

    public String getTopOnuModifyMacAddress() {
        return topOnuModifyMacAddress;
    }

    public void setTopOnuModifyMacAddress(String topOnuModifyMacAddress) {
        this.topOnuModifyMacAddress = EponUtil.getMacStringFromNoISOControl(topOnuModifyMacAddress);
    }

    public Long getTopOnuModifyOnuIndex() {
        return topOnuModifyOnuIndex;
    }

    public void setTopOnuModifyOnuIndex(Long topOnuModifyOnuIndex) {
        this.topOnuModifyOnuIndex = topOnuModifyOnuIndex;
    }

    public Long getTopOnuModifyPonIndex() {
        return topOnuModifyPonIndex;
    }

    public void setTopOnuModifyPonIndex(Long topOnuModifyPonIndex) {
        this.topOnuModifyPonIndex = topOnuModifyPonIndex;
    }

    public String getTopOnuModifyPwd() {
        return topOnuModifyPwd;
    }

    public void setTopOnuModifyPwd(String topOnuModifyPwd) {
        this.topOnuModifyPwd = topOnuModifyPwd;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("OltOnuAuthModify");
        sb.append("{entityId=").append(entityId);
        sb.append(", onuIndex=").append(onuIndex);
        sb.append(", topOnuModifyCardIndex=").append(topOnuModifyCardIndex);
        sb.append(", topOnuModifyPonIndex=").append(topOnuModifyPonIndex);
        sb.append(", topOnuModifyOnuIndex=").append(topOnuModifyOnuIndex);
        sb.append(", topOnuModifyMacAddress='").append(topOnuModifyMacAddress).append('\'');
        sb.append(", topOnuModifyLogicSn='").append(topOnuModifyLogicSn).append('\'');
        sb.append(", topOnuModifyPwd='").append(topOnuModifyPwd).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
