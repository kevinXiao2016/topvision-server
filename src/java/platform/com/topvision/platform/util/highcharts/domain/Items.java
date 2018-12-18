/***********************************************************************
 * $ Items.java,v1.0 2012-7-12 15:08:44 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.platform.util.highcharts.domain;

import java.io.Serializable;

/**
 * @author jay
 * @created @2012-7-12-15:08:44
 */
public class Items implements Serializable {
    private static final long serialVersionUID = 7342180206202447869L;
    private String html;
    private Style style;

    public String getHtml() {
        return html;
    }

    public void setHtml(String html) {
        this.html = html;
    }

    public Style getStyle() {
        return style;
    }

    public void setStyle(Style style) {
        this.style = style;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("Items");
        sb.append("{html='").append(html).append('\'');
        sb.append('}');
        return sb.toString();
    }

    public class Style {
        private Integer left;
        private Integer top;
        private String color;

        public String getColor() {
            return color;
        }

        public void setColor(String color) {
            this.color = color;
        }

        public Integer getLeft() {
            return left;
        }

        public void setLeft(Integer left) {
            this.left = left;
        }

        public Integer getTop() {
            return top;
        }

        public void setTop(Integer top) {
            this.top = top;
        }
    }
}
