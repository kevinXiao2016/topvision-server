/***********************************************************************
 * $Id: OltOnuComAttribute.java,v1.0 2012-12-20 下午14:47:59 $
 * 
 * @author: yq
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.onu.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.topvision.ems.epon.utils.EponUtil;
import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author yq
 * @created @2012-12-20-下午14:47:59
 * 
 */
public class OltOnuMacMgmt implements Serializable, AliasesSuperType {
	private static final long serialVersionUID = 8913178386486764049L;
	private Long entityId;
	private Long onuIndex;
	private Long onuId;
	@SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.4.9.1.1.1", index = true)
	private Long onuMibIndex;
	@SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.4.9.1.1.2", writable = true, type = "OctetString")
	private String topOnuMacList;
	private List<String> topOnuMac;
	@SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.4.9.1.1.3", writable = true, type = "Integer32")
	private Integer topOnuMacMark;
	@SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.4.9.1.1.4", writable = true, type = "Integer32")
	private Integer topOnuMacRowStatus;
	private Integer mgmtEnable;

	public Long getEntityId() {
		return entityId;
	}

	public void setEntityId(Long entityId) {
		this.entityId = entityId;
	}

	public Long getOnuIndex() {
		return onuIndex;
	}

	public void setOnuIndex(Long onuIndex) {
		this.onuIndex = onuIndex;
		if (onuIndex != null) {
			this.onuMibIndex = (onuIndex >> 8) & 0xFFFFFF00;
		}
	}

	public Long getOnuId() {
		return onuId;
	}

	public void setOnuId(Long onuId) {
		this.onuId = onuId;
	}

	public Long getOnuMibIndex() {
		return onuMibIndex;
	}

	public void setOnuMibIndex(Long onuMibIndex) {
		this.onuMibIndex = onuMibIndex;
		if (onuMibIndex != null) {
			this.onuIndex = onuMibIndex << 8;
		}
	}

	public String getTopOnuMacList() {
		return topOnuMacList;
	}

	public void setTopOnuMacList(String topOnuMacList) {
		if (topOnuMacList != null && !topOnuMacList.equalsIgnoreCase("")) {
			topOnuMacList = EponUtil
					.getMacStringFromNoISOControl(topOnuMacList);
			this.topOnuMacList = topOnuMacList;
			String[] s = topOnuMacList.split(":");
			int sl = s.length;
			this.topOnuMac = new ArrayList<String>();
			if (sl > 0 && sl % 6 == 0) {
				for (int i = 0; 6 * i < sl; i++) {
					StringBuilder sb = new StringBuilder();
					sb.append(s[i].toUpperCase()).append(":")
							.append(s[i + 1].toUpperCase()).append(":")
							.append(s[i + 2].toUpperCase()).append(":")
							.append(s[i + 3].toUpperCase()).append(":")
							.append(s[i + 4].toUpperCase()).append(":")
							.append(s[i + 5].toUpperCase());
					this.topOnuMac.add(sb.toString());
				}
			}
		} else {
			this.topOnuMacList = "";
			this.topOnuMac = new ArrayList<String>();
		}
	}

	public List<String> getTopOnuMac() {
		return topOnuMac;
	}

	public void setTopOnuMac(List<String> topOnuMac) {
		this.topOnuMac = topOnuMac;
		if (topOnuMac.size() > 0) {
			StringBuilder sb = new StringBuilder();
			for (String s : topOnuMac) {
				sb.append(":").append(s);
			}
			this.topOnuMacList = sb.toString().substring(1);
		} else {
			this.topOnuMacList = "";
		}
	}

	public Integer getTopOnuMacMark() {
		return topOnuMacMark;
	}

	public void setTopOnuMacMark(Integer topOnuMacMark) {
		this.topOnuMacMark = topOnuMacMark;
	}

	public Integer getTopOnuMacRowStatus() {
		return topOnuMacRowStatus;
	}

	public void setTopOnuMacRowStatus(Integer topOnuMacRowStatus) {
		this.topOnuMacRowStatus = topOnuMacRowStatus;
	}

	public Integer getMgmtEnable() {
		return mgmtEnable;
	}

	public void setMgmtEnable(Integer mgmtEnable) {
		this.mgmtEnable = mgmtEnable;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("OltOnuMacMgmt [entityId=");
		builder.append(entityId);
		builder.append(", onuIndex=");
		builder.append(onuIndex);
		builder.append(", topOnuMacList=");
		builder.append(topOnuMacList);
		builder.append(", topOnuMacMark=");
		builder.append(topOnuMacMark);
		builder.append(", topOnuMacRowStatus=");
		builder.append(topOnuMacRowStatus);
		builder.append(", mgmtEnable=");
		builder.append(mgmtEnable);
		builder.append("]");
		return builder.toString();
	}

}
