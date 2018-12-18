/***********************************************************************
 * $Id: OltOnuCatv.java,v1.0 2011-9-26 上午09:18:16 $
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
 * OnuCATV
 * 
 * @author zhanglongyang
 * 
 */
public class OltOnuCatv implements Serializable, AliasesSuperType {
    private static final long serialVersionUID = -4682843933096808561L;
    private Long entityId;
    private Long onuId;
    private Long ponId;
    private Long onuIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.4.5.1.1.1", index = true)
    private Long topOnuCatvCardNo;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.4.5.1.1.2", index = true)
    private Long topOnuCatvPonNo;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.4.5.1.1.3", index = true)
    private Long topOnuCatvOnuNo;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.4.5.1.1.4", writable = true, type = "Integer32")
    private Integer onuCatvEnable;

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
        onuIndex = new EponIndex(topOnuCatvCardNo.intValue(), topOnuCatvPonNo.intValue(), topOnuCatvOnuNo.intValue())
                .getOnuIndex();
        return onuIndex;
    }

    /**
     * @param onuIndex
     *            the onuIndex to set
     */
    public void setOnuIndex(Long onuIndex) {
        this.onuIndex = onuIndex;
        topOnuCatvCardNo = EponIndex.getSlotNo(onuIndex);
        topOnuCatvPonNo = EponIndex.getPonNo(onuIndex);
        topOnuCatvOnuNo = EponIndex.getOnuNo(onuIndex);
    }

    public Integer getOnuCatvEnable() {
        return onuCatvEnable;
    }

    public void setOnuCatvEnable(Integer onuCatvEnable) {
        this.onuCatvEnable = onuCatvEnable;
    }

    public Long getPonId() {
        return ponId;
    }

    public void setPonId(Long ponId) {
        this.ponId = ponId;
    }

    public Long getTopOnuCatvCardNo() {
        return topOnuCatvCardNo;
    }

    public void setTopOnuCatvCardNo(Long topOnuCatvCardNo) {
        this.topOnuCatvCardNo = topOnuCatvCardNo;
    }

    public Long getTopOnuCatvOnuNo() {
        return topOnuCatvOnuNo;
    }

    public void setTopOnuCatvOnuNo(Long topOnuCatvOnuNo) {
        this.topOnuCatvOnuNo = topOnuCatvOnuNo;
    }

    public Long getTopOnuCatvPonNo() {
        return topOnuCatvPonNo;
    }

    public void setTopOnuCatvPonNo(Long topOnuCatvPonNo) {
        this.topOnuCatvPonNo = topOnuCatvPonNo;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("OltOnuVoip");
        sb.append("{entityId=").append(entityId);
        sb.append(", onuId=").append(onuId);
        sb.append(", ponId=").append(ponId);
        sb.append(", onuIndex=").append(onuIndex);
        sb.append(", topOnuCatvCardNo=").append(topOnuCatvCardNo);
        sb.append(", topOnuCatvPonNo=").append(topOnuCatvPonNo);
        sb.append(", topOnuCatvOnuNo=").append(topOnuCatvOnuNo);
        sb.append(", onuCatvEnable=").append(onuCatvEnable);
        sb.append('}');
        return sb.toString();
    }
}