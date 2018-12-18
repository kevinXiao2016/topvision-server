/***********************************************************************
 * $Id: OltOnuVoip.java,v1.0 2011-9-26 上午09:18:16 $
 * 
 * @author: zhanglongyang
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.onu.domain;

import java.io.Serializable;

import com.topvision.framework.utils.EponIndex;
import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * OnuVOIP
 * 
 * @author zhanglongyang
 * 
 */
public class OltOnuVoip implements Serializable, AliasesSuperType {
    private static final long serialVersionUID = -4682843933096808561L;
    private Long entityId;
    private Long onuId;
    private Long ponId;
    private Long onuIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.4.4.1.1.1", index = true)
    private Long topOnuVoipCardNo;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.4.4.1.1.2", index = true)
    private Long topOnuVoipPonNo;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.4.4.1.1.3", index = true)
    private Long topOnuVoipOnuNo;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.4.4.1.1.4", writable = true, type = "Integer32")
    private Integer onuVoipEnable;

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
        if (onuIndex == null) {
            onuIndex = new EponIndex(topOnuVoipCardNo.intValue(), topOnuVoipPonNo.intValue(),
                    topOnuVoipOnuNo.intValue()).getOnuIndex();
        }
        return onuIndex;
    }

    /**
     * @param onuIndex
     *            the onuIndex to set
     */
    public void setOnuIndex(Long onuIndex) {
        this.onuIndex = onuIndex;
        topOnuVoipCardNo = EponIndex.getSlotNo(onuIndex);
        topOnuVoipPonNo = EponIndex.getPonNo(onuIndex);
        topOnuVoipOnuNo = EponIndex.getOnuNo(onuIndex);
    }

    public Integer getOnuVoipEnable() {
        return onuVoipEnable;
    }

    public void setOnuVoipEnable(Integer onuVoipEnable) {
        this.onuVoipEnable = onuVoipEnable;
    }

    public Long getPonId() {
        return ponId;
    }

    public void setPonId(Long ponId) {
        this.ponId = ponId;
    }

    public Long getTopOnuVoipCardNo() {
        return topOnuVoipCardNo;
    }

    public void setTopOnuVoipCardNo(Long topOnuVoipCardNo) {
        this.topOnuVoipCardNo = topOnuVoipCardNo;
    }

    public Long getTopOnuVoipOnuNo() {
        return topOnuVoipOnuNo;
    }

    public void setTopOnuVoipOnuNo(Long topOnuVoipOnuNo) {
        this.topOnuVoipOnuNo = topOnuVoipOnuNo;
    }

    public Long getTopOnuVoipPonNo() {
        return topOnuVoipPonNo;
    }

    public void setTopOnuVoipPonNo(Long topOnuVoipPonNo) {
        this.topOnuVoipPonNo = topOnuVoipPonNo;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("OltOnuVoip");
        sb.append("{entityId=").append(entityId);
        sb.append(", onuId=").append(onuId);
        sb.append(", ponId=").append(ponId);
        sb.append(", onuIndex=").append(onuIndex);
        sb.append(", topOnuVoipCardNo=").append(topOnuVoipCardNo);
        sb.append(", topOnuVoipPonNo=").append(topOnuVoipPonNo);
        sb.append(", topOnuVoipOnuNo=").append(topOnuVoipOnuNo);
        sb.append(", onuVoipEnable=").append(onuVoipEnable);
        sb.append('}');
        return sb.toString();
    }
}