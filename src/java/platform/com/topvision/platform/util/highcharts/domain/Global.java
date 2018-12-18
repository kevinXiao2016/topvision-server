/***********************************************************************
 * $ Global.java,v1.0 2012-7-12 14:24:58 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.platform.util.highcharts.domain;

import java.io.Serializable;

/**
 * @author jay
 * @created @2012-7-12-14:24:58
 */
public class Global implements Serializable {
    private static final long serialVersionUID = 7342180206202447869L;
    /**
     * The URL to the additional file to lazy load for Android 2.x devices. These devices don't
     * support SVG, so we download a helper file that contains canvg, its dependecy rbcolor, and our
     * own CanVG Renderer class. To avoid hotlinking to our site, you can install canvas-tools.js on
     * your own server and change this option accordingly. Defaults to
     * "http://www.highcharts.com/js/canvas-tools.js".
     */
    private String canvasToolsURL;
    /**
     * Whether to use UTC time for axis scaling, tickmark placement and time display in
     * Highcharts.dateFormat. Advantages of using UTC is that the time displays equally regardless
     * of the user agent's time zone settings. Local time can be used when the data is loaded in
     * real time or when correct Daylight Saving Time transitions are required. Defaults to true.
     */
    private Boolean useUTC;

    public String getCanvasToolsURL() {
        return canvasToolsURL;
    }

    public void setCanvasToolsURL(String canvasToolsURL) {
        this.canvasToolsURL = canvasToolsURL;
    }

    public Boolean getUseUTC() {
        return useUTC;
    }

    public void setUseUTC(Boolean useUTC) {
        this.useUTC = useUTC;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("Global");
        sb.append("{canvasToolsURL='").append(canvasToolsURL).append('\'');
        sb.append(", useUTC=").append(useUTC);
        sb.append('}');
        return sb.toString();
    }
}
