/***********************************************************************
 * $Id: OltBatchPerfCollect.java,v1.0 2014-12-05 下午3:41:18 $
 * 
 * @author: bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.performance.domain;

/**
 * @author bravin
 * @created 2014-12-05-下午3:41:18
 */
public class OltBatchPerfCollect {
	private long entityId;
	private boolean sniPerfEnabled;
	private boolean ponPerfEnabled;
	private boolean onuPerfEnabled;
	private boolean uniPerfEnabled;
	
	
	public long getEntityId() {
		return entityId;
	}
	public void setEntityId(long entityId) {
		this.entityId = entityId;
	}
	public boolean isSniPerfEnabled() {
		return sniPerfEnabled;
	}
	public void setSniPerfEnabled(boolean sniPerfEnabled) {
		this.sniPerfEnabled = sniPerfEnabled;
	}
	public boolean isPonPerfEnabled() {
		return ponPerfEnabled;
	}
	public void setPonPerfEnabled(boolean ponPerfEnabled) {
		this.ponPerfEnabled = ponPerfEnabled;
	}
	public boolean isOnuPerfEnabled() {
		return onuPerfEnabled;
	}
	public void setOnuPerfEnabled(boolean onuPerfEnabled) {
		this.onuPerfEnabled = onuPerfEnabled;
	}
	public boolean isUniPerfEnabled() {
		return uniPerfEnabled;
	}
	public void setUniPerfEnabled(boolean uniPerfEnabled) {
		this.uniPerfEnabled = uniPerfEnabled;
	}
	
	
	
}
