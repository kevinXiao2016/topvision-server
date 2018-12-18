/***********************************************************************
 * $Id: OltSniOptical.java,v1.0 2012-11-03 上午11:09:57 $
 * 
 * @author: yq
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.optical.domain;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.dao.mybatis.AliasesSuperType;
import com.topvision.framework.utils.EponConstants;
import com.topvision.platform.zetaframework.var.UnitConfigConstant;

/**
 * SNI光口的光传输属性
 * 
 * @author yq
 * 
 */
public class OltSniOptical implements Serializable, AliasesSuperType {
	private static final long serialVersionUID = -5813493533841806786L;
	private Long entityId;
	private Long sniId;
	private Long portIndex;
	@SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.2.3.1.1", index = true)
	private Integer slotNo;
	@SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.2.3.1.2", index = true)
	private Integer portNo;
	@SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.2.3.1.3")
	private Integer identifier;
	@SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.2.3.1.4")
	private String vendorName;
	@SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.2.3.1.5")
	private Integer waveLength;
	@SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.2.3.1.6")
	private String vendorPN;
	@SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.2.3.1.7")
	private String vendorSN;
	@SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.2.3.1.8")
	private String dateCode;
	@SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.2.3.1.9")
    private Long workingTemp;
	@SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.2.3.1.10")
    private Long workingVoltage;
	@SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.2.3.1.11")
    private Long biasCurrent;
	@SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.2.3.1.12")
    private Long txPower;
	@SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.2.3.1.13")
    private Long rxPower;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.2.3.1.14")
    private Integer bitRate;

    //sni口温度展示
    private Integer oltSniDisplayTemp;

	public Long getEntityId() {
		return entityId;
	}

	public void setEntityId(Long entityId) {
		this.entityId = entityId;
	}

	public Long getSniId() {
		return sniId;
	}

	public void setSniId(Long sniId) {
		this.sniId = sniId;
	}

	public Long getPortIndex() {
		return portIndex;
	}

	public void setPortIndex(Long portIndex) {
		this.portIndex = portIndex;
	}

	public Integer getSlotNo() {
		return slotNo;
	}

	public void setSlotNo(Integer slotNo) {
		this.slotNo = slotNo;
	}

	public Integer getPortNo() {
		return portNo;
	}

	public void setPortNo(Integer portNo) {
		this.portNo = portNo;
	}

	public Integer getIdentifier() {
		return identifier;
	}

	public void setIdentifier(Integer identifier) {
		this.identifier = identifier;
	}

	public String getVendorName() {
		return vendorName;
	}

	public void setVendorName(String vendorName) {
		this.vendorName = vendorName;
	}

	public Integer getWaveLength() {
		return waveLength;
	}

	public void setWaveLength(Integer waveLength) {
		this.waveLength = waveLength;
	}

	public String getVendorPN() {
		return vendorPN;
	}

	public void setVendorPN(String vendorPN) {
		this.vendorPN = vendorPN;
	}

	public String getVendorSN() {
		return vendorSN;
	}

	public void setVendorSN(String vendorSN) {
		this.vendorSN = vendorSN;
	}

	public String getDateCode() {
		return dateCode;
	}

	public void setDateCode(String dateCode) {
		if (dateCode.split(":").length == 8) {
			this.dateCode = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
					.format(new Date(Long.parseLong(dateCode)));
		} else {
			this.dateCode = dateCode;
		}
	}

    public Long getWorkingTemp() {
		return workingTemp;
	}

    public void setWorkingTemp(Long workingTemp) {
        if (workingTemp != null && EponConstants.OPT_TEMP != workingTemp) {
            int tempValue = Math.round(workingTemp / 256);
            if (workingTemp != null) {
                this.oltSniDisplayTemp = UnitConfigConstant.translateTemperature(tempValue);
            }
            this.workingTemp = workingTemp;
        } else {
            this.workingTemp = null;
        }
	}

    public Long getWorkingVoltage() {
		return workingVoltage;
	}

    public void setWorkingVoltage(Long workingVoltage) {
        if (EponConstants.OPT_VOLTAGE != workingVoltage) {
            this.workingVoltage = workingVoltage;
        } else {
            this.workingVoltage = null;
        }
	}

    public Long getBiasCurrent() {
		return biasCurrent;
	}

    public void setBiasCurrent(Long biasCurrent) {
        if (EponConstants.OPT_CURRENT != biasCurrent) {
            this.biasCurrent = biasCurrent;
        } else {
            this.biasCurrent = null;
        }

	}

    public Long getTxPower() {
		return txPower;
	}

    public void setTxPower(Long txPower) {
        if (EponConstants.TX_POWER != txPower) {
            this.txPower = txPower;
        } else {
            this.txPower = null;
        }
	}

    public Long getRxPower() {
		return rxPower;
	}

    public void setRxPower(Long rxPower) {
        if (EponConstants.RE_POWER != rxPower) {
            this.rxPower = rxPower;
        } else {
            this.rxPower = null;
        }
	}

    public Integer getBitRate() {
        return bitRate;
    }

    public void setBitRate(Integer bitRate) {
        this.bitRate = bitRate;
    }

    public Integer getOltSniDisplayTemp() {
        return oltSniDisplayTemp;
    }

    public void setOltSniDisplayTemp(Integer oltSniDisplayTemp) {
        this.oltSniDisplayTemp = oltSniDisplayTemp;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("OltSniOptical [entityId=");
        builder.append(entityId);
        builder.append(", sniId=");
        builder.append(sniId);
        builder.append(", portIndex=");
        builder.append(portIndex);
        builder.append(", slotNo=");
        builder.append(slotNo);
        builder.append(", portNo=");
        builder.append(portNo);
        builder.append(", identifier=");
        builder.append(identifier);
        builder.append(", vendorName=");
        builder.append(vendorName);
        builder.append(", waveLength=");
        builder.append(waveLength);
        builder.append(", vendorPN=");
        builder.append(vendorPN);
        builder.append(", vendorSN=");
        builder.append(vendorSN);
        builder.append(", dateCode=");
        builder.append(dateCode);
        builder.append(", workingTemp=");
        builder.append(workingTemp);
        builder.append(", workingVoltage=");
        builder.append(workingVoltage);
        builder.append(", biasCurrent=");
        builder.append(biasCurrent);
        builder.append(", txPower=");
        builder.append(txPower);
        builder.append(", rxPower=");
        builder.append(rxPower);
        builder.append(", bitRate=");
        builder.append(bitRate);
        builder.append("]");
        return builder.toString();
    }

}
