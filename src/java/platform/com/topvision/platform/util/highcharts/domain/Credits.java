/***********************************************************************
 * $ Credits.java,v1.0 2012-7-12 14:24:38 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.platform.util.highcharts.domain;

import java.io.Serializable;

/**
 * @author jay
 * @created @2012-7-12-14:24:38
 */
public class Credits implements Serializable {
    private static final long serialVersionUID = 7342180206202447869L;
    /**
     * Whether to show the credits text. Defaults to true
     */
    private Boolean enabled;
    /**
     * Position configuration for the credtis label. Supported properties are align, verticalAlign,
     * x and y. Defaults to position: { align: 'right', x: -10, verticalAlign: 'bottom', y: -5 }
     */
    private Position position;
    /**
     * The URL for the credits label. Defaults to "http://www.highcharts.com".
     */
    private String href;
    /**
     * The text for the credits label. Defaults to "Highcharts.com".
     */
    private String text;

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("Credits");
        sb.append("{enabled=").append(enabled);
        sb.append(", position=").append(position);
        sb.append(", href='").append(href).append('\'');
        sb.append(", text='").append(text).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
