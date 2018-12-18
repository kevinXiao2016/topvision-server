/***********************************************************************
 * $Id: OnuPonOptical.java,v1.0 2012-11-07 上午11:09:57 $
 * 
 * @author: yq
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.optical.domain;

import java.io.Serializable;

import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.dao.mybatis.AliasesSuperType;
import com.topvision.framework.utils.EponConstants;
import com.topvision.platform.zetaframework.var.UnitConfigConstant;

/**
 * ONU PON口的光传输属性
 * 
 * @author yq
 * 
 */
public class OnuPonOptical implements Serializable, AliasesSuperType {
	private static final long serialVersionUID = -3856403270502965874L;
	private Long entityId;
	private Long onuPonIndex;
	private Long onuPonId;
	@SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.4.2.1.1", index = true)
	private Long deviceIndex;
	@SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.4.2.1.2", index = true)
	private Integer cardNo;
	@SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.4.2.1.3", index = true)
	private Integer portNo;
	@SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.4.2.1.4")
	private Integer rxPower;
	@SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.4.2.1.5")
	private Integer txPower;
	@SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.4.2.1.6")
	private Integer biasCurrent;
	@SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.4.2.1.7")
	private Integer workingVoltage;
	@SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.4.2.1.8")
	private Integer workingTemp;

    //因业务需要，用以保存计算后的发送光功率与接收光功率
    private Double onuRevPower;
    private Double onuTransPower;

    //onu pon口温度展示
    private Integer onuPonDisplayTemp;

	public Long getEntityId() {
		return entityId;
	}

	public void setEntityId(Long entityId) {
		this.entityId = entityId;
	}

	public Long getOnuPonIndex() {
		return onuPonIndex;
	}

	public void setOnuPonIndex(Long onuPonIndex) {
		this.onuPonIndex = onuPonIndex;
	}

	public Long getOnuPonId() {
		return onuPonId;
	}

	public void setOnuPonId(Long onuPonId) {
		this.onuPonId = onuPonId;
	}

	public Long getDeviceIndex() {
		return deviceIndex;
	}

	public void setDeviceIndex(Long deviceIndex) {
		this.deviceIndex = deviceIndex;
	}

	public Integer getCardNo() {
		return cardNo;
	}

	public void setCardNo(Integer cardNo) {
		this.cardNo = cardNo;
	}

	public Integer getPortNo() {
		return portNo;
	}

	public void setPortNo(Integer portNo) {
		this.portNo = portNo;
	}

	public Integer getWorkingTemp() {
		return workingTemp;
	}

	public void setWorkingTemp(Integer workingTemp) {
        if (workingTemp != null && EponConstants.OPT_TEMP != workingTemp) {
            this.workingTemp = workingTemp;
            this.onuPonDisplayTemp = UnitConfigConstant.translateTemperature((double) workingTemp / 100);
        } else {
            this.workingTemp = null;
        }
	}

	public Integer getWorkingVoltage() {
		return workingVoltage;
	}

	public void setWorkingVoltage(Integer workingVoltage) {
        if (workingVoltage != null && EponConstants.OPT_VOLTAGE != workingVoltage) {
            this.workingVoltage = workingVoltage;
        } else {
            this.workingVoltage = null;
        }
	}

	public Integer getBiasCurrent() {
		return biasCurrent;
	}

	public void setBiasCurrent(Integer biasCurrent) {
        if (biasCurrent != null && EponConstants.OPT_CURRENT != biasCurrent) {
            this.biasCurrent = biasCurrent;
        } else {
            this.biasCurrent = null;
        }
	}

	public Integer getTxPower() {
        if (txPower != null && txPower.equals(EponConstants.TX_POWER)) {
            return null;
        } else {
            return txPower;
        }
	}

	public void setTxPower(Integer txPower) {
        if (txPower != null && EponConstants.TX_POWER != txPower) {
            this.txPower = txPower;
            this.onuTransPower = (double) txPower / 100;
        } else {
            this.txPower = null;
            this.onuTransPower = 0d;
        }
	}

	public Integer getRxPower() {
        if (rxPower != null && rxPower.equals(EponConstants.RE_POWER)) {
            return null;
        } else {
            return rxPower;
        }
	}

	public void setRxPower(Integer rxPower) {
        if (rxPower != null && EponConstants.RE_POWER != rxPower) {
            this.rxPower = rxPower;
            this.onuRevPower = (double) rxPower / 100;
        } else {
            this.rxPower = null;
            this.onuRevPower = 0d;
        }
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

    public Integer getOnuPonDisplayTemp() {
        return onuPonDisplayTemp;
    }

    public void setOnuPonDisplayTemp(Integer onuPonDisplayTemp) {
        this.onuPonDisplayTemp = onuPonDisplayTemp;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("OnuPonOptical [entityId=");
        builder.append(entityId);
        builder.append(", onuPonIndex=");
        builder.append(onuPonIndex);
        builder.append(", onuPonId=");
        builder.append(onuPonId);
        builder.append(", deviceIndex=");
        builder.append(deviceIndex);
        builder.append(", cardNo=");
        builder.append(cardNo);
        builder.append(", portNo=");
        builder.append(portNo);
        builder.append(", rxPower=");
        builder.append(rxPower);
        builder.append(", txPower=");
        builder.append(txPower);
        builder.append(", biasCurrent=");
        builder.append(biasCurrent);
        builder.append(", workingVoltage=");
        builder.append(workingVoltage);
        builder.append(", workingTemp=");
        builder.append(workingTemp);
        builder.append(", onuRevPower=");
        builder.append(onuRevPower);
        builder.append(", onuTransPower=");
        builder.append(onuTransPower);
        builder.append("]");
        return builder.toString();
    }
}
