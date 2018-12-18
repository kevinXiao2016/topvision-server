/***********************************************************************
 * $ Graph.java,v1.0 2012-7-11 16:05:39 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.platform.util.highcharts.domain;

import java.io.Serializable;

/**
 * @author jay
 * @created @2012-7-11-16:05:39
 */
public class Chart implements Serializable {
    private static final long serialVersionUID = 7342180206202447869L;
    /**
     * When using multiple axis, the ticks of two or more opposite axes will automatically be
     * aligned by adding ticks to the axis or axes with the least ticks. This can be prevented by
     * setting alignTicks to false. If the grid lines look messy, it's a good idea to hide them for
     * the secondary axis by setting gridLineWidth to 0. Defaults to true.
     */
    private Boolean alignTicks;
    /**
     * Set the overall animation for all chart updating. Animation can be disabled throughout the
     * chart by setting it to false here. It can be overridden for each individual API method as a
     * function parameter. The only animation not affected by this option is the initial series
     * animation, see plotOptions.series => animation.
     * 
     * The animation can either be set as a boolean or a configuration object. If true, it will use
     * the 'swing' jQuery easing and a duration of 500 ms. If used as a configuration object, the
     * following properties are supported:
     * 
     * duration The duration of the animation in milliseconds. easing When using jQuery as the
     * general framework, the easing can be set to linear or swing. More easing functions are
     * available with the use of jQuery plug-ins, most notably the jQuery UI suite. See the jQuery
     * docs. When using MooTools as the general framework, use the property name transition instead
     * of easing. Defaults to true.
     */
    private Boolean animation;
    /**
     * The background color or gradient for the outer chart area. Defaults to "#FFFFFF".
     */
    private String backgroundColor;
    /**
     * The color of the outer chart border. The border is painted using vector graphic techniques to
     * allow rounded corners. Defaults to "#4572A7".
     */
    private String borderColor;
    /**
     * The corner radius of the outer chart border. Defaults to 5.
     */
    private Integer borderRadius;
    /**
     * The pixel width of the outer chart border. The border is painted using vector graphic
     * techniques to allow rounded corners. Defaults to 0.
     */
    private Integer borderWidth;
    /**
     * A CSS class name to apply to the charts container div, allowing unique CSS styling for each
     * chart. Defaults to "".
     */
    private String className;
    /**
     * Alias of type.
     */
    private String defaultSeriesType;
    /**
     * An explicit height for the chart. By default the height is calculated from the offset height
     * of the containing element. Defaults to null.
     */
    private Integer height;
    /**
     * If true, the axes will scale to the remaining visible series once one series is hidden. If
     * false, hiding and showing a series will not affect the axes or the other series. For stacks,
     * once one series within the stack is hidden, the rest of the stack will close in around it
     * even if the axis is not affected. Defaults to true.
     */
    private Boolean ignoreHiddenSeries;
    /**
     * Whether to invert the axes so that the x axis is horizontal and y axis is vertical. When
     * true, the x axis is reversed by default. If a bar plot is present in the chart, it will be
     * inverted automatically. Defaults to false
     */
    private Boolean inverted;
    /**
     * The margin between the top outer edge of the chart and the plot area. Use this to set a fixed
     * pixel value for the margin as opposed to the default dynamic margin. See also spacingTop.
     * Defaults to null.
     */
    private Integer marginTop;
    /**
     * The margin between the right outer edge of the chart and the plot area. Use this to set a
     * fixed pixel value for the margin as opposed to the default dynamic margin. See also
     * spacingRight. Defaults to 50.
     */
    private Integer marginRight;
    /**
     * The margin between the bottom outer edge of the chart and the plot area. Use this to set a
     * fixed pixel value for the margin as opposed to the default dynamic margin. See also
     * spacingBottom. Defaults to 70
     */
    private Integer marginBottom;
    /**
     * The margin between the left outer edge of the chart and the plot area. Use this to set a
     * fixed pixel value for the margin as opposed to the default dynamic margin. See also
     * spacingLeft. Defaults to 80.
     */
    private Integer marginLeft;
    /**
     * The background color or gradient for the plot area. Defaults to null
     */
    private String plotBackgroundColor;
    /**
     * The URL for an image to use as the plot background. To set an image as the background for the
     * entire chart, set a CSS background image to the container element. Defaults to null.
     */
    private String plotBackgroundImage;
    /**
     * The color of the inner chart or plot area border. Defaults to "#C0C0C0".
     */
    private String plotBorderColor;
    /**
     * The pixel width of the plot area border. Defaults to 0.
     */
    private Integer plotBorderWidth;
    /**
     * Whether to apply a drop shadow to the plot area. Requires that plotBackgroundColor be set.
     * Defaults to false
     */
    private Boolean plotShadow;
    /**
     * Whether to reflow the chart to fit the width of the container div on resizing the window.
     * Defaults to true.
     */
    private Boolean reflow;
    /**
     * The HTML element where the chart will be rendered. If it is a string, the element by that id
     * is used. The HTML element can also be passed by direct reference. Defaults to null.
     */
    private String renderTo;
    /**
     * The background color of the marker square when selecting (zooming in on) an area of the
     * chart. Defaults to rgba(69,114,167,0.25).
     */
    private String selectionMarkerFill;
    /**
     * Whether to apply a drop shadow to the outer chart area. Requires that backgroundColor be set.
     * Defaults to false
     */
    private Boolean shadow;
    /**
     * Whether to show the axes initially. This only applies to empty charts where series are added
     * dynamically, as axes are automatically added to cartesian series. Defaults to false.
     */
    private Boolean showAxes;
    /**
     * The space between the top edge of the chart and the content (plot area, axis title and
     * labels, title, subtitle or legend in top position).
     * 
     * Defaults to 10.
     */
    private Integer spacingTop;
    /**
     * The space between the right edge of the chart and the content (plot area, axis title and
     * labels, title, subtitle or legend in top position).
     * 
     * Defaults to 10.
     */
    private Integer spacingRight;
    /**
     * The space between the bottom edge of the chart and the content (plot area, axis title and
     * labels, title, subtitle or legend in top position).
     * 
     * Defaults to 15
     */
    private Integer spacingBottom;
    /**
     * The space between the left edge of the chart and the content (plot area, axis title and
     * labels, title, subtitle or legend in top position).
     * 
     * Defaults to 10.
     */
    private Integer spacingLeft;
    /**
     * The default series type for the chart. Can be one of line, spline, area, areaspline, column,
     * bar, pie and scatter. Defaults to "line".
     */
    private String type;
    /**
     * An explicit width for the chart. By default the width is calculated from the offset width of
     * the containing element. Defaults to null.
     */
    private Integer width;
    /**
     * Decides in what dimentions the user can zoom by dragging the mouse. Can be one of x, y or xy.
     * Defaults to "".
     */
    private String zoomType;
    /**
     * chartId
     */
    private String id;

    public Boolean getAlignTicks() {
        return alignTicks;
    }

    public void setAlignTicks(Boolean alignTicks) {
        this.alignTicks = alignTicks;
    }

    public Boolean getAnimation() {
        return animation;
    }

    public void setAnimation(Boolean animation) {
        this.animation = animation;
    }

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

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getDefaultSeriesType() {
        return defaultSeriesType;
    }

    public void setDefaultSeriesType(String defaultSeriesType) {
        this.defaultSeriesType = defaultSeriesType;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Boolean getIgnoreHiddenSeries() {
        return ignoreHiddenSeries;
    }

    public void setIgnoreHiddenSeries(Boolean ignoreHiddenSeries) {
        this.ignoreHiddenSeries = ignoreHiddenSeries;
    }

    public Boolean getInverted() {
        return inverted;
    }

    public void setInverted(Boolean inverted) {
        this.inverted = inverted;
    }

    public Integer getMarginBottom() {
        return marginBottom;
    }

    public void setMarginBottom(Integer marginBottom) {
        this.marginBottom = marginBottom;
    }

    public Integer getMarginLeft() {
        return marginLeft;
    }

    public void setMarginLeft(Integer marginLeft) {
        this.marginLeft = marginLeft;
    }

    public Integer getMarginRight() {
        return marginRight;
    }

    public void setMarginRight(Integer marginRight) {
        this.marginRight = marginRight;
    }

    public Integer getMarginTop() {
        return marginTop;
    }

    public void setMarginTop(Integer marginTop) {
        this.marginTop = marginTop;
    }

    public String getPlotBackgroundColor() {
        return plotBackgroundColor;
    }

    public void setPlotBackgroundColor(String plotBackgroundColor) {
        this.plotBackgroundColor = plotBackgroundColor;
    }

    public String getPlotBackgroundImage() {
        return plotBackgroundImage;
    }

    public void setPlotBackgroundImage(String plotBackgroundImage) {
        this.plotBackgroundImage = plotBackgroundImage;
    }

    public String getPlotBorderColor() {
        return plotBorderColor;
    }

    public void setPlotBorderColor(String plotBorderColor) {
        this.plotBorderColor = plotBorderColor;
    }

    public Integer getPlotBorderWidth() {
        return plotBorderWidth;
    }

    public void setPlotBorderWidth(Integer plotBorderWidth) {
        this.plotBorderWidth = plotBorderWidth;
    }

    public Boolean getPlotShadow() {
        return plotShadow;
    }

    public void setPlotShadow(Boolean plotShadow) {
        this.plotShadow = plotShadow;
    }

    public Boolean getReflow() {
        return reflow;
    }

    public void setReflow(Boolean reflow) {
        this.reflow = reflow;
    }

    public String getRenderTo() {
        return renderTo;
    }

    public void setRenderTo(String renderTo) {
        this.renderTo = renderTo;
    }

    public String getSelectionMarkerFill() {
        return selectionMarkerFill;
    }

    public void setSelectionMarkerFill(String selectionMarkerFill) {
        this.selectionMarkerFill = selectionMarkerFill;
    }

    public Boolean getShadow() {
        return shadow;
    }

    public void setShadow(Boolean shadow) {
        this.shadow = shadow;
    }

    public Boolean getShowAxes() {
        return showAxes;
    }

    public void setShowAxes(Boolean showAxes) {
        this.showAxes = showAxes;
    }

    public Integer getSpacingBottom() {
        return spacingBottom;
    }

    public void setSpacingBottom(Integer spacingBottom) {
        this.spacingBottom = spacingBottom;
    }

    public Integer getSpacingLeft() {
        return spacingLeft;
    }

    public void setSpacingLeft(Integer spacingLeft) {
        this.spacingLeft = spacingLeft;
    }

    public Integer getSpacingRight() {
        return spacingRight;
    }

    public void setSpacingRight(Integer spacingRight) {
        this.spacingRight = spacingRight;
    }

    public Integer getSpacingTop() {
        return spacingTop;
    }

    public void setSpacingTop(Integer spacingTop) {
        this.spacingTop = spacingTop;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public String getZoomType() {
        return zoomType;
    }

    public void setZoomType(String zoomType) {
        this.zoomType = zoomType;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("Chart");
        sb.append("{alignTicks=").append(alignTicks);
        sb.append(", animation=").append(animation);
        sb.append(", backgroundColor='").append(backgroundColor).append('\'');
        sb.append(", borderColor='").append(borderColor).append('\'');
        sb.append(", borderRadius=").append(borderRadius);
        sb.append(", borderWidth=").append(borderWidth);
        sb.append(", className='").append(className).append('\'');
        sb.append(", defaultSeriesType='").append(defaultSeriesType).append('\'');
        sb.append(", height=").append(height);
        sb.append(", ignoreHiddenSeries=").append(ignoreHiddenSeries);
        sb.append(", inverted=").append(inverted);
        sb.append(", marginTop=").append(marginTop);
        sb.append(", marginRight=").append(marginRight);
        sb.append(", marginBottom=").append(marginBottom);
        sb.append(", marginLeft=").append(marginLeft);
        sb.append(", plotBackgroundColor='").append(plotBackgroundColor).append('\'');
        sb.append(", plotBackgroundImage='").append(plotBackgroundImage).append('\'');
        sb.append(", plotBorderColor='").append(plotBorderColor).append('\'');
        sb.append(", plotBorderWidth=").append(plotBorderWidth);
        sb.append(", plotShadow=").append(plotShadow);
        sb.append(", reflow=").append(reflow);
        sb.append(", renderTo='").append(renderTo).append('\'');
        sb.append(", selectionMarkerFill='").append(selectionMarkerFill).append('\'');
        sb.append(", shadow=").append(shadow);
        sb.append(", showAxes=").append(showAxes);
        sb.append(", spacingTop=").append(spacingTop);
        sb.append(", spacingRight=").append(spacingRight);
        sb.append(", spacingBottom=").append(spacingBottom);
        sb.append(", spacingLeft=").append(spacingLeft);
        sb.append(", type='").append(type).append('\'');
        sb.append(", width=").append(width);
        sb.append(", zoomType='").append(zoomType).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
