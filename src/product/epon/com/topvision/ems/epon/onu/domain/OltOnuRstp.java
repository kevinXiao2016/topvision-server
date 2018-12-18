/***********************************************************************
 * $Id: OltOnuCatv.java,v1.0 2011-9-26 上午09:18:16 $
 * 
 * @author: zhanglongyang
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.onu.domain;

import java.io.Serializable;

import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.dao.mybatis.AliasesSuperType;
import com.topvision.framework.utils.EponIndex;

/**
 * OnuCATV
 * 
 * @author zhanglongyang
 * 
 */
public class OltOnuRstp implements Serializable, AliasesSuperType {
    private static final long serialVersionUID = -4682843933096808561L;
    private Long entityId;
    private Long onuId;
    private Long ponId;
    private Long onuIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.4.3.1.1.1", index = true)
    private Long topOnuRstpCardNo;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.4.3.1.1.2", index = true)
    private Long topOnuRstpPonNo;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.4.3.1.1.3", index = true)
    private Long topOnuRstpOnuNo;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.4.3.1.1.4", writable = true, type = "Integer32")
    private Integer rstpBridgeMode;

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public Long getOnuId() {
        return onuId;
    }

    public void setOnuId(Long onuId) {
        this.onuId = onuId;
    }

    /**
     * @return the onuIndex
     */
    public Long getOnuIndex() {
        if (onuIndex == null && topOnuRstpCardNo != null && topOnuRstpPonNo != null && topOnuRstpOnuNo != null) {
            onuIndex = new EponIndex(topOnuRstpCardNo.intValue(), topOnuRstpPonNo.intValue(),
                    topOnuRstpOnuNo.intValue()).getOnuIndex();
        }
        return onuIndex;
    }

    /**
     * @param onuIndex
     *            the onuIndex to set
     */
    public void setOnuIndex(Long onuIndex) {
        this.onuIndex = onuIndex;
        topOnuRstpCardNo = EponIndex.getSlotNo(onuIndex);
        topOnuRstpPonNo = EponIndex.getPonNo(onuIndex);
        topOnuRstpOnuNo = EponIndex.getOnuNo(onuIndex);
    }

    public Long getPonId() {
        return ponId;
    }

    public void setPonId(Long ponId) {
        this.ponId = ponId;
    }

    public Long getTopOnuRstpCardNo() {
        return topOnuRstpCardNo;
    }

    public void setTopOnuRstpCardNo(Long topOnuRstpCardNo) {
        this.topOnuRstpCardNo = topOnuRstpCardNo;
    }

    public Long getTopOnuRstpOnuNo() {
        return topOnuRstpOnuNo;
    }

    public void setTopOnuRstpOnuNo(Long topOnuRstpOnuNo) {
        this.topOnuRstpOnuNo = topOnuRstpOnuNo;
    }

    public Long getTopOnuRstpPonNo() {
        return topOnuRstpPonNo;
    }

    public void setTopOnuRstpPonNo(Long topOnuRstpPonNo) {
        this.topOnuRstpPonNo = topOnuRstpPonNo;
    }

    public Integer getRstpBridgeMode() {
        return rstpBridgeMode;
    }

    public void setRstpBridgeMode(Integer rstpBridgeMode) {
        this.rstpBridgeMode = rstpBridgeMode;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("OltOnuRstp");
        sb.append("{entityId=").append(entityId);
        sb.append(", onuId=").append(onuId);
        sb.append(", ponId=").append(ponId);
        sb.append(", onuIndex=").append(onuIndex);
        sb.append(", topOnuRstpCardNo=").append(topOnuRstpCardNo);
        sb.append(", topOnuRstpPonNo=").append(topOnuRstpPonNo);
        sb.append(", topOnuRstpOnuNo=").append(topOnuRstpOnuNo);
        sb.append(", rstpBridgeMode=").append(rstpBridgeMode);
        sb.append('}');
        return sb.toString();
    }
}