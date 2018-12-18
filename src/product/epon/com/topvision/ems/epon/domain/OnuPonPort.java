/***********************************************************************
 * $Id: OnuUniPort.java,v1.0 2011-9-27 下午04:26:11 $
 * 
 * @author: loyal
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.domain;

import java.io.Serializable;

import com.topvision.framework.dao.mybatis.AliasesSuperType;
import com.topvision.framework.utils.EponConstants;
import com.topvision.framework.utils.EponIndex;

/**
 * @author loyal
 * @created @2011-9-27-下午04:26:11
 * 
 */
public class OnuPonPort implements Serializable, AliasesSuperType {
    private static final long serialVersionUID = -698010863281655357L;
    private Integer onuPonId;
    private Long onuPonIndex;
    private Long onuPonRealIndex;
    private Integer onuReceivedOpticalPower;
    private Integer onuTramsmittedOpticalPower;
    private Integer onuBiasCurrent;
    private Integer onuWorkingVoltage;
    private Integer onuWorkingTemperature;
    private Long slotNo;
    private Long ponNo;

    //因业务需要，用以保存计算后的发送光功率与接收光功率
    private Double onuRevPower;
    private Double onuTransPower;

    public Integer getOnuPonId() {
        return onuPonId;
    }

    public void setOnuPonId(Integer onuPonId) {
        this.onuPonId = onuPonId;
    }

    public Long getOnuPonIndex() {
        return onuPonIndex;
    }

    public void setOnuPonIndex(Long onuPonIndex) {
        setOnuPonRealIndex(EponIndex.getPonNo(onuPonIndex));
        this.onuPonIndex = onuPonIndex;
    }

    public Long getOnuPonRealIndex() {
        return onuPonRealIndex;
    }

    public void setOnuPonRealIndex(Long onuPonRealIndex) {
        this.onuPonRealIndex = onuPonRealIndex;
    }

    public Integer getOnuReceivedOpticalPower() {
        return onuReceivedOpticalPower;
    }

    public void setOnuReceivedOpticalPower(Integer onuReceivedOpticalPower) {
        if (onuReceivedOpticalPower != null && EponConstants.RE_POWER != onuReceivedOpticalPower) {
            this.onuReceivedOpticalPower = onuReceivedOpticalPower;
            this.onuRevPower = (double) onuReceivedOpticalPower / 100;
        } else {
            this.onuReceivedOpticalPower = null;
        }
    }

    public Integer getOnuTramsmittedOpticalPower() {
        return onuTramsmittedOpticalPower;
    }

    public void setOnuTramsmittedOpticalPower(Integer onuTramsmittedOpticalPower) {
        if (onuTramsmittedOpticalPower != null && EponConstants.TX_POWER != onuTramsmittedOpticalPower) {
            this.onuTramsmittedOpticalPower = onuTramsmittedOpticalPower;
            this.onuTransPower = (double) onuTramsmittedOpticalPower / 100;
        } else {
            this.onuTramsmittedOpticalPower = null;
        }
    }

    public Integer getOnuBiasCurrent() {
        return onuBiasCurrent;
    }

    public void setOnuBiasCurrent(Integer onuBiasCurrent) {
        this.onuBiasCurrent = onuBiasCurrent;
    }

    public Integer getOnuWorkingVoltage() {
        return onuWorkingVoltage;
    }

    public void setOnuWorkingVoltage(Integer onuWorkingVoltage) {
        this.onuWorkingVoltage = onuWorkingVoltage;
    }

    public Integer getOnuWorkingTemperature() {
        return onuWorkingTemperature;
    }

    public void setOnuWorkingTemperature(Integer onuWorkingTemperature) {
        this.onuWorkingTemperature = onuWorkingTemperature;
    }

    public Long getSlotNo() {
        return slotNo;
    }

    public void setSlotNo(Long slotNo) {
        this.slotNo = slotNo;
    }

    public Long getPonNo() {
        return ponNo;
    }

    public void setPonNo(Long ponNo) {
        this.ponNo = ponNo;
    }

    public Double getOnuRevPower() {
        return onuRevPower;
    }

    public void setOnuRevPower(Double onuRevPower) {
        this.onuRevPower = onuRevPower;
    }

    public Double getOnuTransPower() {
        return onuTransPower;
    }

    public void setOnuTransPower(Double onuTransPower) {
        this.onuTransPower = onuTransPower;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("OnuPonPort [onuPonId=");
        builder.append(onuPonId);
        builder.append(", onuPonIndex=");
        builder.append(onuPonIndex);
        builder.append(", onuPonRealIndex=");
        builder.append(onuPonRealIndex);
        builder.append(", onuReceivedOpticalPower=");
        builder.append(onuReceivedOpticalPower);
        builder.append(", onuTramsmittedOpticalPower=");
        builder.append(onuTramsmittedOpticalPower);
        builder.append(", onuBiasCurrent=");
        builder.append(onuBiasCurrent);
        builder.append(", onuWorkingVoltage=");
        builder.append(onuWorkingVoltage);
        builder.append(", onuWorkingTemperature=");
        builder.append(onuWorkingTemperature);
        builder.append(", slotNo=");
        builder.append(slotNo);
        builder.append(", ponNo=");
        builder.append(ponNo);
        builder.append(", onuRevPower=");
        builder.append(onuRevPower);
        builder.append(", onuTransPower=");
        builder.append(onuTransPower);
        builder.append("]");
        return builder.toString();
    }

}
