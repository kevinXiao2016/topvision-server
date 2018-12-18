/***********************************************************************
 * $Id: OltUniStormSuppressionEntry.java,v1.0 2011-10-18 下午04:06:42 $
 * 
 * @author: huqiao
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.onu.domain;

import java.io.Serializable;

import com.topvision.framework.utils.EponIndex;
import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author huqiao
 * @created @2011-10-18-下午04:06:42
 * 
 */
public class OltUniStormSuppressionEntry implements Serializable, AliasesSuperType {
	private static final long serialVersionUID = 536257125555471803L;
	private Long entityId;
	private Long uniId;
	private Long uniIndex;
	@SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.5.2.1.1", index = true)
	private Integer uniCardNo;
	@SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.5.2.1.2", index = true)
	private Integer uniPonNo;
	@SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.5.2.1.3", index = true)
	private Integer uniOnuNo;
	@SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.5.2.1.4", index = true)
	private Integer uniPortNo;
	@SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.5.2.1.5", writable = true, type = "Integer32")
	private Integer unicastStormEnable;
	@SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.5.2.1.6", writable = true, type = "Integer32")
	private Long unicastStormInPacketRate;
	@SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.5.2.1.7", writable = true, type = "Integer32")
	private Long unicastStormOutPacketRate;
	@SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.5.2.1.8", writable = true, type = "Integer32")
	private Integer multicastStormEnable;
	@SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.5.2.1.9", writable = true, type = "Integer32")
	private Long multicastStormInPacketRate;
	@SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.5.2.1.10", writable = true, type = "Integer32")
	private Long multicastStormOutPacketRate;
	@SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.5.2.1.11", writable = true, type = "Integer32")
	private Integer broadcastStormEnable;
	@SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.5.2.1.12", writable = true, type = "Integer32")
	private Long broadcastStormInPacketRate;
	@SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.5.2.1.13", writable = true, type = "Integer32")
	private Long broadcastStormOutPacketRate;

	/**
	 * @return the uniCardNo
	 */
	public Integer getUniCardNo() {
		return uniCardNo;
	}

	/**
	 * @param uniCardNo
	 *            the uniCardNo to set
	 */
	public void setUniCardNo(Integer uniCardNo) {
		this.uniCardNo = uniCardNo;
	}

	/**
	 * @return the uniPonNo
	 */
	public Integer getUniPonNo() {
		return uniPonNo;
	}

	/**
	 * @param uniPonNo
	 *            the uniPonNo to set
	 */
	public void setUniPonNo(Integer uniPonNo) {
		this.uniPonNo = uniPonNo;
	}

	/**
	 * @return the uniOnuNo
	 */
	public Integer getUniOnuNo() {
		return uniOnuNo;
	}

	/**
	 * @param uniOnuNo
	 *            the uniOnuNo to set
	 */
	public void setUniOnuNo(Integer uniOnuNo) {
		this.uniOnuNo = uniOnuNo;
	}

	/**
	 * @return the uniPortNo
	 */
	public Integer getUniPortNo() {
		return uniPortNo;
	}

	/**
	 * @param uniPortNo
	 *            the uniPortNo to set
	 */
	public void setUniPortNo(Integer uniPortNo) {
		this.uniPortNo = uniPortNo;
	}

	/**
	 * @return the unicastStormEnable
	 */
	public Integer getUnicastStormEnable() {
		return unicastStormEnable;
	}

	/**
	 * @param unicastStormEnable
	 *            the unicastStormEnable to set
	 */
	public void setUnicastStormEnable(Integer unicastStormEnable) {
		this.unicastStormEnable = unicastStormEnable;
	}

	/**
	 * @return the unicastStormInPacketRate
	 */
	public Long getUnicastStormInPacketRate() {
		return unicastStormInPacketRate;
	}

	/**
	 * @param unicastStormInPacketRate
	 *            the unicastStormInPacketRate to set
	 */
	public void setUnicastStormInPacketRate(Long unicastStormInPacketRate) {
		this.unicastStormInPacketRate = unicastStormInPacketRate;
	}

	/**
	 * @return the unicastStormOutPacketRate
	 */
	public Long getUnicastStormOutPacketRate() {
		return unicastStormOutPacketRate;
	}

	/**
	 * @param unicastStormOutPacketRate
	 *            the unicastStormOutPacketRate to set
	 */
	public void setUnicastStormOutPacketRate(Long unicastStormOutPacketRate) {
		this.unicastStormOutPacketRate = unicastStormOutPacketRate;
	}

	/**
	 * @return the multicastStormEnable
	 */
	public Integer getMulticastStormEnable() {
		return multicastStormEnable;
	}

	/**
	 * @param multicastStormEnable
	 *            the multicastStormEnable to set
	 */
	public void setMulticastStormEnable(Integer multicastStormEnable) {
		this.multicastStormEnable = multicastStormEnable;
	}

	/**
	 * @return the multicastStormInPacketRate
	 */
	public Long getMulticastStormInPacketRate() {
		return multicastStormInPacketRate;
	}

	/**
	 * @param multicastStormInPacketRate
	 *            the multicastStormInPacketRate to set
	 */
	public void setMulticastStormInPacketRate(Long multicastStormInPacketRate) {
		this.multicastStormInPacketRate = multicastStormInPacketRate;
	}

	/**
	 * @return the multicastStormOutPacketRate
	 */
	public Long getMulticastStormOutPacketRate() {
		return multicastStormOutPacketRate;
	}

	/**
	 * @param multicastStormOutPacketRate
	 *            the multicastStormOutPacketRate to set
	 */
	public void setMulticastStormOutPacketRate(Long multicastStormOutPacketRate) {
		this.multicastStormOutPacketRate = multicastStormOutPacketRate;
	}

	/**
	 * @return the broadcastStormEnable
	 */
	public Integer getBroadcastStormEnable() {
		return broadcastStormEnable;
	}

	/**
	 * @param broadcastStormEnable
	 *            the broadcastStormEnable to set
	 */
	public void setBroadcastStormEnable(Integer broadcastStormEnable) {
		this.broadcastStormEnable = broadcastStormEnable;
	}

	/**
	 * @return the broadcastStormInPacketRate
	 */
	public Long getBroadcastStormInPacketRate() {
		return broadcastStormInPacketRate;
	}

	/**
	 * @param broadcastStormInPacketRate
	 *            the broadcastStormInPacketRate to set
	 */
	public void setBroadcastStormInPacketRate(Long broadcastStormInPacketRate) {
		this.broadcastStormInPacketRate = broadcastStormInPacketRate;
	}

	/**
	 * @return the broadcastStormOutPacketRate
	 */
	public Long getBroadcastStormOutPacketRate() {
		return broadcastStormOutPacketRate;
	}

	/**
	 * @param broadcastStormOutPacketRate
	 *            the broadcastStormOutPacketRate to set
	 */
	public void setBroadcastStormOutPacketRate(Long broadcastStormOutPacketRate) {
		this.broadcastStormOutPacketRate = broadcastStormOutPacketRate;
	}

	/**
	 * @return the uniId
	 */
	public Long getUniId() {
		return uniId;
	}

	/**
	 * @param uniId
	 *            the uniId to set
	 */
	public void setUniId(Long uniId) {
		this.uniId = uniId;
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
	 * @return the uniIndex
	 */
	public Long getUniIndex() {
		// 由于MIB定义中不存在onu中的cardIndex 所以默认此时的cardIndex为0
		if (uniIndex == null) {
			uniIndex = EponIndex.getUniIndex(uniCardNo, uniPonNo, uniOnuNo, 0,
					uniPortNo);
		}
		return uniIndex;
	}

	/**
	 * @param uniIndex
	 *            the uniIndex to set
	 */
	public void setUniIndex(Long uniIndex) {
		this.uniIndex = uniIndex;
		uniCardNo = EponIndex.getSlotNo(uniIndex).intValue();
		uniPonNo = EponIndex.getPonNo(uniIndex).intValue();
		uniOnuNo = EponIndex.getOnuNo(uniIndex).intValue();
		uniPortNo = EponIndex.getUniNo(uniIndex).intValue();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("OltUniStormSuppressionEntry [entityId=");
		builder.append(entityId);
		builder.append(", uniId=");
		builder.append(uniId);
		builder.append(", uniIndex=");
		builder.append(uniIndex);
		builder.append(", uniCardNo=");
		builder.append(uniCardNo);
		builder.append(", uniPonNo=");
		builder.append(uniPonNo);
		builder.append(", uniOnuNo=");
		builder.append(uniOnuNo);
		builder.append(", uniPortNo=");
		builder.append(uniPortNo);
		builder.append(", unicastStormEnable=");
		builder.append(unicastStormEnable);
		builder.append(", unicastStormInPacketRate=");
		builder.append(unicastStormInPacketRate);
		builder.append(", unicastStormOutPacketRate=");
		builder.append(unicastStormOutPacketRate);
		builder.append(", multicastStormEnable=");
		builder.append(multicastStormEnable);
		builder.append(", multicastStormInPacketRate=");
		builder.append(multicastStormInPacketRate);
		builder.append(", multicastStormOutPacketRate=");
		builder.append(multicastStormOutPacketRate);
		builder.append(", broadcastStormEnable=");
		builder.append(broadcastStormEnable);
		builder.append(", broadcastStormInPacketRate=");
		builder.append(broadcastStormInPacketRate);
		builder.append(", broadcastStormOutPacketRate=");
		builder.append(broadcastStormOutPacketRate);
		builder.append("]");
		return builder.toString();
	}

}
