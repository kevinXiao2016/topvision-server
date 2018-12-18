package com.topvision.ems.cmc.cm.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.type.Alias;

/**
 * @author ls
 * @created @2017年1月6日-下午2:35:02
 *
 */
public class RealtimeCmSignal implements Serializable{

	private static final long serialVersionUID = 6254403516117395346L;
	private Long cmIndex;
	private List<String> downChannelRx=new ArrayList<String>();
	private List<String> downChannelSnr=new ArrayList<String>();
	private List<Long> cmDownChId=new ArrayList<Long>();
	private List<Long> cmUpChId=new ArrayList<Long>();
	private List<String> upChannelTx=new ArrayList<String>();
	public Long getCmIndex() {
		return cmIndex;
	}
	public void setCmIndex(Long cmcIndex) {
		this.cmIndex = cmcIndex;
	}
	public List<String> getDownChannelRx() {
		return downChannelRx;
	}
	public void setDownChannelRx(List<String> downChannelRx) {
		this.downChannelRx = downChannelRx;
	}
	public List<String> getDownChannelSnr() {
		return downChannelSnr;
	}
	public void setDownChannelSnr(List<String> downChannelSnr) {
		this.downChannelSnr = downChannelSnr;
	}
	public List<Long> getCmDownChId() {
		return cmDownChId;
	}
	public void setCmDownChId(List<Long> cmDownChId) {
		this.cmDownChId = cmDownChId;
	}
	public List<Long> getCmUpChId() {
		return cmUpChId;
	}
	public void setCmUpChId(List<Long> cmUpChId) {
		this.cmUpChId = cmUpChId;
	}
	public List<String> getUpChannelTx() {
		return upChannelTx;
	}
	public void setUpChannelTx(List<String> upChannelTx) {
		this.upChannelTx = upChannelTx;
	}


}
