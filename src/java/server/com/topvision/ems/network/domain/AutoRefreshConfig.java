/***********************************************************************
 * $Id: AutoRefreshConfig.java,v1.0 2014-10-17 上午10:49:11 $
 * 
 * @author: Rod John
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.network.domain;

import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author Rod John
 * @created @2014-10-17-上午10:49:11
 * 
 */
public class AutoRefreshConfig implements AliasesSuperType {
    private static final long serialVersionUID = -8781561144624389923L;
    private Boolean autoRefreshSwitch;
    private Integer autoRefreshInterval;
    
    public AutoRefreshConfig(){
    }

    public AutoRefreshConfig(Boolean autoRefreshSwitch, Integer autoRefreshInterval) {
        this.autoRefreshSwitch = autoRefreshSwitch;
        this.autoRefreshInterval = autoRefreshInterval;
    }

    /**
     * @return the autoRefreshSwitch
     */
    public Boolean getAutoRefreshSwitch() {
        return autoRefreshSwitch;
    }

    /**
     * @param autoRefreshSwitch
     *            the autoRefreshSwitch to set
     */
    public void setAutoRefreshSwitch(Boolean autoRefreshSwitch) {
        this.autoRefreshSwitch = autoRefreshSwitch;
    }

    /**
     * @return the autoRefreshInterval
     */
    public Integer getAutoRefreshInterval() {
        return autoRefreshInterval;
    }

    /**
     * @param autoRefreshInterval
     *            the autoRefreshInterval to set
     */
    public void setAutoRefreshInterval(Integer autoRefreshInterval) {
        this.autoRefreshInterval = autoRefreshInterval;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "AutoRefreshConfig [autoRefreshSwitch=" + autoRefreshSwitch + ", autoRefreshInterval="
                + autoRefreshInterval + "]";
    }

}
