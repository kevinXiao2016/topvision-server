package com.topvision.ems.cm.cmsignal.domain;

import java.sql.Timestamp;

public class Cm3Signal {

	public static final Long CHANNEL_TYPE_UP = 0L;
	public static final Long CHANNEL_TYPE_DOWN = 1L;

	private Long cmId;
	private Long channelId;
	private Long channelType;// 0上行，1下行
	private String downChannelSnr; // 下行SNR
	private String downChannelTx;// 下行电平
	private String upChannelTx;// 上行电平
	private String upChannelSnr; // 上行SNR
	private String downChannelFrequency; // 下行信道频率
	private String upChannelFrequency; // 上行信道频率
	private Timestamp collectTime;

	public Cm3Signal() {
		super();
	}

	public Cm3Signal(Long cmId, Long channelId, Long channelType) {
		super();
		this.cmId = cmId;
		this.channelType = channelType;
		this.channelId = channelId;
	}

	public Long getCmId() {
		return cmId;
	}

	public void setCmId(Long cmId) {
		this.cmId = cmId;
	}

	public Long getChannelType() {
		return channelType;
	}

	public void setChannelType(Long channelType) {
		this.channelType = channelType;
	}

	public Long getChannelId() {
		return channelId;
	}

	public void setChannelId(Long channelId) {
		this.channelId = channelId;
	}

	public String getDownChannelSnr() {
		return downChannelSnr;
	}

	public void setDownChannelSnr(String downChannelSnr) {
		this.downChannelSnr = downChannelSnr;
	}

	public String getDownChannelTx() {
		return downChannelTx;
	}

	public void setDownChannelTx(String downChannelTx) {
		this.downChannelTx = downChannelTx;
	}

	public String getUpChannelTx() {
		return upChannelTx;
	}

	public void setUpChannelTx(String upChannelTx) {
		this.upChannelTx = upChannelTx;
	}

	public String getUpChannelSnr() {
		return upChannelSnr;
	}

	public void setUpChannelSnr(String upChannelSnr) {
		this.upChannelSnr = upChannelSnr;
	}

	public String getDownChannelFrequency() {
		return downChannelFrequency;
	}

	public void setDownChannelFrequency(String downChannelFrequency) {
		this.downChannelFrequency = downChannelFrequency;
	}

	public String getUpChannelFrequency() {
		return upChannelFrequency;
	}

	public void setUpChannelFrequency(String upChannelFrequency) {
		this.upChannelFrequency = upChannelFrequency;
	}

	public Timestamp getCollectTime() {
		return collectTime;
	}

	public void setCollectTime(Timestamp collectTime) {
		this.collectTime = collectTime;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((channelId == null) ? 0 : channelId.hashCode());
		result = prime * result + ((channelType == null) ? 0 : channelType.hashCode());
		result = prime * result + ((cmId == null) ? 0 : cmId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Cm3Signal other = (Cm3Signal) obj;
		if (channelId == null) {
			if (other.channelId != null)
				return false;
		} else if (!channelId.equals(other.channelId))
			return false;
		if (channelType == null) {
			if (other.channelType != null)
				return false;
		} else if (!channelType.equals(other.channelType))
			return false;
		if (cmId == null) {
			if (other.cmId != null)
				return false;
		} else if (!cmId.equals(other.cmId))
			return false;
		return true;
	}
}
