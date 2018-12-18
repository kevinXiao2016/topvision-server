/***********************************************************************
 * $Id: RealtimeCm.java,v1.0 2014年5月13日 下午2:06:57 $
 * 
 * @author: YangYi
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.cm.domain;

import java.io.Serializable;
import java.util.List;

import com.topvision.framework.utils.CmcIndexUtils;
import com.topvision.framework.annotation.SnmpProperty;

/**
 * @author YangYi
 * @created @2014年5月13日-下午2:06:57
 * 
 */
public class RealtimeCm implements Serializable {
	private static final long serialVersionUID = 2646893565977606741L;
	@SnmpProperty(oid = "1.3.6.1.2.1.10.127.1.3.3.1.1", index = true)
	private Long statusIndex;
	@SnmpProperty(oid = "1.3.6.1.2.1.10.127.1.3.3.1.2")
	private String statusMacAddress; // MAC地址
	@SnmpProperty(oid = "1.3.6.1.2.1.10.127.1.3.3.1.3")
	private String statusIpAddress;// Ip地址
	@SnmpProperty(oid = "1.3.6.1.2.1.10.127.1.3.3.1.4")
	private Long statusDownChannelIfIndex; // 下行通道索引
	@SnmpProperty(oid = "1.3.6.1.2.1.10.127.1.3.3.1.5")
	private Long statusUpChannelIfIndex; // 上行通道索引
	@SnmpProperty(oid = "1.3.6.1.2.1.10.127.1.3.3.1.9")
	private Integer statusValue;
	@SnmpProperty(oid = "1.3.6.1.2.1.10.127.1.3.3.1.21")
	private String statusInetAddress; // 因特网地址

	@SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.9.4.1.3")
	private Long cmRxPower; // 下行接收电平
	@SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.9.4.1.4")
	private Long cmTxPower; // 上行发射电平
	@SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.9.4.1.5")
	private Long cmSignalNoise; // 下行信噪比
	@SnmpProperty(oid = "1.3.6.1.2.1.10.127.1.3.3.1.13")
	private Long statusSignalNoise; // CMTS侧信噪比

	private List<String> downChannelSnr;
	private List<String> downChannelRx;
	private List<String> upChannelTx;
	private List<String> upChannelSnr;
	private List<Long> cmUpChId;
	private List<Long> cmDownChId;
	private List<Long> cmUpSnrChId;

	private Long upChannelId;
	private Long downChannelId;

	public Long getStatusIndex() {
		return statusIndex;
	}

	public void setStatusIndex(Long statusIndex) {
		this.statusIndex = statusIndex;
	}

	public Integer getStatusValue() {
		return statusValue;
	}

	public String getStatusIpAddress() {
		return statusIpAddress;
	}

	public void setStatusIpAddress(String statusIpAddress) {
		this.statusIpAddress = statusIpAddress;
	}

	public void setStatusValue(Integer statusValue) {
		// TODO 临时将所有不小于10的值修改为6--mib中定义大于10的值为上线状态（10,11,12,13,14）
		// if (statusValue >= 14 || statusValue == 8 || statusValue == 9) {
		// statusValue = 6;
		// }
		this.statusValue = statusValue;
	}

	public String getStatusInetAddress() {
		if (statusIpAddress != null && !statusIpAddress.equals("")) {
			return statusIpAddress;
		} else {
			return statusInetAddress;
		}
	}

	public void setStatusInetAddress(String statusInetAddress) {
		this.statusInetAddress = statusInetAddress;
	}

	public String getStatusMacAddress() {
		return statusMacAddress;
	}

	public void setStatusMacAddress(String statusMacAddress) {
		this.statusMacAddress = statusMacAddress;
	}

	public Long getUpChannelId() {
		return upChannelId;
	}

	public void setUpChannelId(Long upChannelId) {
		this.upChannelId = upChannelId;
	}

	/**
	 * @return the docsIfCmtsCmStatusUpChannelIfIndex
	 */
	public Long getStatusUpChannelIfIndex() {
		return statusUpChannelIfIndex;
	}

	/**
	 * @param statusUpChannelIfIndex
	 *            the docsIfCmtsCmStatusUpChannelIfIndex to set
	 */
	public void setStatusUpChannelIfIndex(Long statusUpChannelIfIndex) {
		this.statusUpChannelIfIndex = statusUpChannelIfIndex;
		if (statusUpChannelIfIndex != null) {
			this.upChannelId = CmcIndexUtils.getChannelId(statusUpChannelIfIndex);
		}
	}

	/**
	 * @return the docsIfCmtsCmStatusDownChannelIfIndex
	 */
	public Long getStatusDownChannelIfIndex() {
		return statusDownChannelIfIndex;
	}

	/**
	 * @param statusDownChannelIfIndex
	 *            the docsIfCmtsCmStatusDownChannelIfIndex to set
	 */
	public void setStatusDownChannelIfIndex(Long statusDownChannelIfIndex) {
		this.statusDownChannelIfIndex = statusDownChannelIfIndex;
		if (statusDownChannelIfIndex != null) {
			this.downChannelId = CmcIndexUtils.getChannelId(statusDownChannelIfIndex);
		}
	}

	public Long getDownChannelId() {
		return downChannelId;
	}

	public void setDownChannelId(Long downChannelId) {
		this.downChannelId = downChannelId;
	}

	public List<String> getDownChannelSnr() {
		return downChannelSnr;
	}

	public void setDownChannelSnr(List<String> downChannelSnr) {
		this.downChannelSnr = downChannelSnr;
	}

	public List<String> getDownChannelRx() {
		return downChannelRx;
	}

	public void setDownChannelRx(List<String> downChannelRx) {
		this.downChannelRx = downChannelRx;
	}

	public List<String> getUpChannelTx() {
		return upChannelTx;
	}

	public void setUpChannelTx(List<String> upChannelTx) {
		this.upChannelTx = upChannelTx;
	}

	public List<String> getUpChannelSnr() {
		return upChannelSnr;
	}

	public void setUpChannelSnr(List<String> upChannelSnr) {
		this.upChannelSnr = upChannelSnr;
	}

	public Long getCmRxPower() {
		return cmRxPower;
	}

	public void setCmRxPower(Long cmRxPower) {
		this.cmRxPower = cmRxPower;
	}

	public Long getCmTxPower() {
		return cmTxPower;
	}

	public void setCmTxPower(Long cmTxPower) {
		this.cmTxPower = cmTxPower;
	}

	public Long getCmSignalNoise() {
		return cmSignalNoise;
	}

	public void setCmSignalNoise(Long cmSignalNoise) {
		this.cmSignalNoise = cmSignalNoise;
	}

	public Long getStatusSignalNoise() {
		return statusSignalNoise;
	}

	public void setStatusSignalNoise(Long statusSignalNoise) {
		this.statusSignalNoise = statusSignalNoise;
	}

	public List<Long> getCmUpChId() {
		return cmUpChId;
	}

	public void setCmUpChId(List<Long> cmUpChId) {
		this.cmUpChId = cmUpChId;
	}

	public List<Long> getCmDownChId() {
		return cmDownChId;
	}

	public void setCmDownChId(List<Long> cmDownChId) {
		this.cmDownChId = cmDownChId;
	}

	public List<Long> getCmUpSnrChId() {
		return cmUpSnrChId;
	}

	public void setCmUpSnrChId(List<Long> cmUpSnrChId) {
		this.cmUpSnrChId = cmUpSnrChId;
	}

}
