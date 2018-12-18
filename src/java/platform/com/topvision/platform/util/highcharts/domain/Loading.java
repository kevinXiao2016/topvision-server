/***********************************************************************
 * $ Loading.java,v1.0 2012-7-12 14:26:16 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.platform.util.highcharts.domain;

import java.io.Serializable;

/**
 * @author jay
 * @created @2012-7-12-14:26:16
 */
public class Loading implements Serializable {
    private static final long serialVersionUID = 7342180206202447869L;
    /**
     * The duration in milliseconds of the fade out effect. Defaults to 100
     */
    private Integer hideDuration;
    /**
     * The duration in milliseconds of the fade in effect. Defaults to 100
     */
    private Integer showDuration;

    public Integer getHideDuration() {
        return hideDuration;
    }

    public void setHideDuration(Integer hideDuration) {
        this.hideDuration = hideDuration;
    }

    public Integer getShowDuration() {
        return showDuration;
    }

    public void setShowDuration(Integer showDuration) {
        this.showDuration = showDuration;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("Loading");
        sb.append("{hideDuration=").append(hideDuration);
        sb.append(", showDuration=").append(showDuration);
        sb.append('}');
        return sb.toString();
    }
}
