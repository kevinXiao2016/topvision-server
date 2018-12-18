/***********************************************************************
 * $ Tooltip.java,v1.0 2012-7-12 14:33:00 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.platform.util.highcharts.domain;

import java.io.Serializable;

/**
 * @author jay
 * @created @2012-7-12-14:33:00
 */
public class Tooltip implements Serializable {
    private static final long serialVersionUID = 7342180206202447869L;
    /**
     * The background color or gradient for the tooltip. Defaults to "rgba(255, 255, 255, .85)".
     */
    private String backgroundColor;
    /**
     * The color of the tooltip border. When null, the border takes the color of the corresponding
     * series or point. Defaults to "auto"
     */
    private String borderColor;
    /**
     * The radius of the rounded border corners. Defaults to 5.
     */
    private Integer borderRadius;
    /**
     * The pixel width of the tooltip border. Defaults to 2.
     */
    private Integer borderWidth;
    /**
     * Enable or disable the tooltip. Defaults to true.
     */
    private Boolean enabled;
    /**
     * A string to append to the tooltip format. Defaults to false.
     */
    private String footerFormat;
    /**
     * Callback function to format the text of the tooltip. Return false to disable tooltip for a
     * specific point on series.
     * 
     * A subset of HTML is supported. The HTML of the tooltip is parsed and converted to SVG,
     * therefore this isn't a complete HTML renderer. The following tabs are supported: <b>,
     * <strong>, <i>,
     * <em>, <br/>, <span>. Spans can be styled with a style attribute, but only text-related CSS that is shared with SVG is handled.

Since version 2.1 the tooltip can be shared between multiple series through the shared option. The available data in the formatter differ a bit depending on whether the tooltip is shared or not. In a shared tooltip, all properties except x, which is common for all points, are kept in an array, this.points.

Available data are:

this.percentage (not shared) / this.points[i].percentage (shared)
Stacked series and pies only. The point's percentage of the total.
this.point (not shared) / this.points[i].point (shared)
The point object. The point name, if defined, is available through this.point.name.
this.points
In a shared tooltip, this is an array containing all other properties for each point.
this.series (not shared) / this.points[i].series (shared)
The series object. The series name is available through this.series.name.
this.total (not shared) / this.points[i].total (shared)
Stacked series only. The total value at this point's x value.
this.x
The x value. This property is the same regardless of the tooltip being shared or not.
this.y (not shared) / this.points[i].y (shared)
The y value.
     */
    private String formatter;
    /**
     * The HTML of the point's line in the tooltip. Variables are enclosed by curly brackets.
     * Available variables are point.x, point.y, series.name and series.color and other properties
     * on the same form. Furthermore, point.y can be extended by the tooltip.yPrefix and
     * tooltip.ySuffix variables. This can also be overridden for each series, which makes it a good
     * hook for displaying units.
     */
    private String pointFormat;
    /**
     * Whether to apply a drop shadow to the tooltip. Defaults to true.
     */
    private Boolean shadow;
    /**
     * When the tooltip is shared, the entire plot area will capture mouse movement, and tooltip
     * texts for all series will be shown in a single bubble. This is recommended for single series
     * charts and for iPad optimized sites. Defaults to false.
     */
    private Boolean shared;
    /**
     * Proximity snap for graphs or single points. Does not apply to bars, columns and pie slices.
     * It defaults to 10 for mouse-powered devices and 25 for touch devices. Defaults to 10/25.
     */
    private Integer snap;
    /**
     * How many decimals to show in each series' y value. This is overridable in each series'
     * tooltip options object. The default is to preserve all decimals
     */
    private Integer valueDecimals;
    /**
     * A string to prepend to each series' y value. Overridable in each series' tooltip options
     * object. Defaults to "".
     */
    private String valuePrefix;
    /**
     * A string to append to each series' y value. Overridable in each series' tooltip options
     * object. Defaults to "".
     */
    private String valueSuffix;
    /**
     * The format for the date in the tooltip header if the X axis is a datetime axis. The default
     * is a best guess based on the smallest distance between points in the chart.
     */
    private String xDateFormat;
    /**
     * Use HTML to render the contents of the tooltip instead of SVG. Using HTML allows advanced
     * formatting like tables and images in the tooltip. It is also recommended for rtl languages as
     * it works around rtl bugs in early Firefox. Defaults to false.
     */
    private Boolean useHTML;

    public String getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(String backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public String getBorderColor() {
        return borderColor;
    }

    public void setBorderColor(String borderColor) {
        this.borderColor = borderColor;
    }

    public Integer getBorderRadius() {
        return borderRadius;
    }

    public void setBorderRadius(Integer borderRadius) {
        this.borderRadius = borderRadius;
    }

    public Integer getBorderWidth() {
        return borderWidth;
    }

    public void setBorderWidth(Integer borderWidth) {
        this.borderWidth = borderWidth;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public String getFooterFormat() {
        return footerFormat;
    }

    public void setFooterFormat(String footerFormat) {
        this.footerFormat = footerFormat;
    }

    public String getFormatter() {
        return formatter;
    }

    public void setFormatter(String formatter) {
        this.formatter = formatter;
    }

    public String getPointFormat() {
        return pointFormat;
    }

    public void setPointFormat(String pointFormat) {
        this.pointFormat = pointFormat;
    }

    public Boolean getShadow() {
        return shadow;
    }

    public void setShadow(Boolean shadow) {
        this.shadow = shadow;
    }

    public Boolean getShared() {
        return shared;
    }

    public void setShared(Boolean shared) {
        this.shared = shared;
    }

    public Integer getSnap() {
        return snap;
    }

    public void setSnap(Integer snap) {
        this.snap = snap;
    }

    public Boolean getUseHTML() {
        return useHTML;
    }

    public void setUseHTML(Boolean useHTML) {
        this.useHTML = useHTML;
    }

    public Integer getValueDecimals() {
        return valueDecimals;
    }

    public void setValueDecimals(Integer valueDecimals) {
        this.valueDecimals = valueDecimals;
    }

    public String getValuePrefix() {
        return valuePrefix;
    }

    public void setValuePrefix(String valuePrefix) {
        this.valuePrefix = valuePrefix;
    }

    public String getValueSuffix() {
        return valueSuffix;
    }

    public void setValueSuffix(String valueSuffix) {
        this.valueSuffix = valueSuffix;
    }

    public String getXDateFormat() {
        return xDateFormat;
    }

    public void setXDateFormat(String xDateFormat) {
        this.xDateFormat = xDateFormat;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("Tooltip");
        sb.append("{backgroundColor='").append(backgroundColor).append('\'');
        sb.append(", borderColor='").append(borderColor).append('\'');
        sb.append(", borderRadius=").append(borderRadius);
        sb.append(", borderWidth=").append(borderWidth);
        sb.append(", enabled=").append(enabled);
        sb.append(", footerFormat='").append(footerFormat).append('\'');
        sb.append(", formatter='").append(formatter).append('\'');
        sb.append(", pointFormat='").append(pointFormat).append('\'');
        sb.append(", shadow=").append(shadow);
        sb.append(", shared=").append(shared);
        sb.append(", snap=").append(snap);
        sb.append(", valueDecimals=").append(valueDecimals);
        sb.append(", valuePrefix='").append(valuePrefix).append('\'');
        sb.append(", valueSuffix='").append(valueSuffix).append('\'');
        sb.append(", xDateFormat='").append(xDateFormat).append('\'');
        sb.append(", useHTML=").append(useHTML);
        sb.append('}');
        return sb.toString();
    }
}
