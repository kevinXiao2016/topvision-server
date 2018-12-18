/***********************************************************************
 * $Id: CmcEventSource.java,v1.0 2017年11月16日 下午2:51:38 $
 * 
 * @author: vanzand
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.fault.domain;

import com.topvision.ems.fault.domain.EventSource;

/**
 * @author vanzand
 * @created @2017年11月16日-下午2:51:38
 *
 */
public class CmcEventSource extends EventSource {

    public static final String SOURCE_UP_CHANNEL = "upChannel";
    public static final String SOURCE_DOWN_CHANNEL = "downChannel";

    private Long channelId;
    private String channelName;
    private String mac;
    private String name;

    public CmcEventSource(Long entityId) {
        super(entityId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.fault.domain.EventSource#formatSource()
     */
    @Override
    public String formatSource() {
        StringBuilder sb = new StringBuilder("CMTS");
        if (mac != null) {
            sb.append(String.format("[%s]", mac));
        }
        if (channelName != null) {
            sb.append(channelName);
        } else if (SOURCE_UP_CHANNEL.equals(source)) {
            sb.append("[US").append(channelId).append("]");
        } else if (SOURCE_DOWN_CHANNEL.equals(source)) {
            sb.append("[DS").append(channelId).append("]");
        }
        return sb.toString();
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public Long getChannelId() {
        return channelId;
    }

    public void setChannelId(Long channelId) {
        this.channelId = channelId;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
