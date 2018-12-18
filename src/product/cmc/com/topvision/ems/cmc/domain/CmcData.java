/***********************************************************************
 * $Id: CmcData.java,v1.0 2011-6-28 下午08:18:41 $
 * 
 * @author: Victor
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.domain;

import com.topvision.framework.constants.Symbol;
import com.topvision.ems.facade.domain.DiscoveryData;

/**
 * @author Victor
 * @created @2011-6-28-下午08:18:41
 * 
 */
public class CmcData extends DiscoveryData {
    private static final long serialVersionUID = 1233074999171525284L;
    private String sn = "";
    private String osVersion = "";
    private String hardVersion = "";
    private String channelAbility = "";

    /**
     * @return the sn
     */
    public String getSn() {
        return sn;
    }

    /**
     * @param sn
     *            the sn to set
     */
    public void setSn(String sn) {
        this.sn = sn;
    }

    /**
     * @return the osVersion
     */
    public String getOsVersion() {
        return osVersion;
    }

    /**
     * @param osVersion
     *            the osVersion to set
     */
    public void setOsVersion(String osVersion) {
        this.osVersion = osVersion;
    }

    /**
     * @return the hardVersion
     */
    public String getHardVersion() {
        return hardVersion;
    }

    /**
     * @param hardVersion
     *            the hardVersion to set
     */
    public void setHardVersion(String hardVersion) {
        this.hardVersion = hardVersion;
    }

    /**
     * @return the channelAbility
     */
    public String getChannelAbility() {
        return channelAbility;
    }

    /**
     * @param channelAbility
     *            the channelAbility to set
     */
    public void setChannelAbility(String channelAbility) {
        this.channelAbility = channelAbility;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("CmcData [sn=");
        builder.append(sn);
        builder.append(", osVersion=");
        builder.append(osVersion);
        builder.append(", hardVersion=");
        builder.append(hardVersion);
        builder.append(", channelAbility=");
        builder.append(channelAbility);
        builder.append(Symbol.BRACKET_RIGHT);
        return builder.toString();
    }

}
