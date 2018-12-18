/***********************************************************************
 * $ Title.java,v1.0 2012-7-12 14:32:21 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.platform.util.highcharts.domain;

import java.io.Serializable;

/**
 * @author jay
 * @created @2012-7-12-14:32:21
 */
public class Title implements Serializable {
    private static final long serialVersionUID = 7342180206202447869L;

    /**
     * The horizontal alignment of the subtitle. Can be one of "left", "center" and "right".
     * Defaults to "center".
     */
    private String align;
    /**
     * When the subtitle is floating, the plot area will not move to make space for it. Defaults to
     * false.
     */
    private Boolean floating;
    /**
     * The title of the chart. To disable the title, set the text to null. Defaults to
     * "Chart title".
     */
    private String text;
    /**
     * The vertical alignment of the title. Can be one of "top", "middle" and "bottom". Defaults to
     * "top"
     */
    private String verticalAlign;
    /**
     * The x position of the subtitle relative to the alignment within chart.spacingLeft and
     * chart.spacingRight. Defaults to 0.
     */
    private Integer x;
    /**
     * The y position of the title relative to the alignment within chart.spacingTop and
     * chart.spacingBottom. Defaults to 15.
     */
    private Integer y;
    /**
     * The margin between the title and the plot area, or if a subtitle is present, the margin
     * between the subtitle and the plot area. Defaults to 15.
     */
    private Integer margin;

    public String getAlign() {
        return align;
    }

    public void setAlign(String align) {
        this.align = align;
    }

    public Boolean getFloating() {
        return floating;
    }

    public void setFloating(Boolean floating) {
        this.floating = floating;
    }

    public Integer getMargin() {
        return margin;
    }

    public void setMargin(Integer margin) {
        this.margin = margin;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getVerticalAlign() {
        return verticalAlign;
    }

    public void setVerticalAlign(String verticalAlign) {
        this.verticalAlign = verticalAlign;
    }

    public Integer getX() {
        return x;
    }

    public void setX(Integer x) {
        this.x = x;
    }

    public Integer getY() {
        return y;
    }

    public void setY(Integer y) {
        this.y = y;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("Title");
        sb.append("{align='").append(align).append('\'');
        sb.append(", floating=").append(floating);
        sb.append(", text='").append(text).append('\'');
        sb.append(", verticalAlign='").append(verticalAlign).append('\'');
        sb.append(", x=").append(x);
        sb.append(", y=").append(y);
        sb.append(", margin=").append(margin);
        sb.append('}');
        return sb.toString();
    }
}
