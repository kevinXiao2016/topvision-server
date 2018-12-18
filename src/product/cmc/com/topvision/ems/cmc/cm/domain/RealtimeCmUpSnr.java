/***********************************************************************
 * $Id: RealtimeCmUpSnr.java,v1.0 2017年1月20日 上午9:27:41 $
 * 
 * @author: ls
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.cm.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author admin
 * @created @2017年1月20日-上午9:27:41
 *
 */
public class RealtimeCmUpSnr implements Serializable {
	private static final long serialVersionUID = -5497331090258545453L;
	private Long cmIndex;
	private List<String> upChannelSnr = new ArrayList<String>();
	private List<Long> upChannelId = new ArrayList<Long>();

	public Long getCmIndex() {
		return cmIndex;
	}

	public void setCmIndex(Long cmIndex) {
		this.cmIndex = cmIndex;
	}

	public List<String> getUpChannelSnr() {
		return upChannelSnr;
	}

	public void setUpChannelSnr(List<String> upChannelSnr) {
		this.upChannelSnr = upChannelSnr;
	}

	public List<Long> getUpChannelId() {
		return upChannelId;
	}

	public void setUpChannelId(List<Long> upChannelId) {
		this.upChannelId = upChannelId;
	}

}
