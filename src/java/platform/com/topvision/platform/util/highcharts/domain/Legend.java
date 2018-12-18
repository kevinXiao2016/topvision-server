/***********************************************************************
 * $ Legend.java,v1.0 2012-7-12 14:25:52 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.platform.util.highcharts.domain;

import java.io.Serializable;

/**
 * @author jay
 * @created @2012-7-12-14:25:52
 */
public class Legend implements Serializable {
    private static final long serialVersionUID = 7342180206202447869L;
    /**
     * The horizontal alignment of the legend box within the chart area. Defaults to "center".
     */
    private String align;
    /**
     * The background color of the legend, filling the rounded corner border. Defaults to null.
     */
    private String backgroundColor;
    /**
     * The color of the drawn border around the legend. Defaults to #909090.
     */
    private String borderColor;
    /**
     * The border corner radius of the legend. Defaults to 5.
     */
    private String borderRadius;
    /**
     * The width of the drawn border around the legend. Defaults to 1
     */
    private Integer borderWidth;
    /**
     * Enable or disable the legend. Defaults to true.
     */
    private Boolean enabled;
    /**
     * When the legend is floating, the plot area ignores it and is allowed to be placed below it.
     * Defaults to false.
     */
    private Boolean floating;
    /**
     * The pixel bottom margin for each legend item. Defaults to 0.
     */
    private Integer itemMarginBottom;
    /**
     * The pixel top margin for each legend item. Defaults to 0.
     */
    private Integer itemMarginTop;
    /**
     * The width for each legend item. This is useful in a horizontal layout with many items when
     * you want the items to align vertically. Defaults to null.
     */
    private Integer itemWidth;
    /**
     * The layout of the legend items. Can be one of "horizontal" or "vertical". Defaults to
     * "horizontal".
     */
    private String layout;
    /**
     * Line height for the legend items. Deprecated as of 2.1. Instead, the line height for each
     * item can be set using itemStyle.lineHeight, and the padding between items using itemMarginTop
     * and itemMarginBottom. Defaults to 16.
     */
    private Integer lineHeight;
    /**
     * If the plot area sized is calculated automatically and the legend is not floating, the legend
     * margin is the space between the legend and the axis labels or plot area. Defaults to 15.
     */
    private Integer margin;
    /**
     * The inner padding of the legend box. Defaults to 8
     */
    private Integer padding;
    /**
     * Whether to reverse the order of the legend items compared to the order of the series or
     * points as defined in the configuration object. Defaults to false.
     */
    private Boolean reversed;
    /**
     * Whether to show the symbol on the right side of the text rather than the left side. This is
     * common in Arabic and Hebraic. Defaults to false.
     */
    private Boolean rtl;
    /**
     * Whether to apply a drop shadow to the legend. A backgroundColor also needs to be applied for
     * this to take effect. Defaults to false.
     */
    private Boolean shadow;
    /**
     * The pixel padding between the legend item symbol and the legend item text. Defaults to 5.
     */
    private Integer symbolPadding;
    /**
     * The pixel width of the legend item symbol. Defaults to 30.
     */
    private Integer symbolWidth;
    /**
     * The vertical alignment of the legend box. Can be one of "top", "middle" or "bottom". Vertical
     * position can be further determined by the y option. Defaults to "bottom".
     */
    private String verticalAlign;
    /**
     * The width of the legend box, not including style.padding. Defaults to null.
     */
    private Integer width;
    /**
     * The x offset of the legend relative to it's horizontal alignment align within
     * chart.spacingLeft and chart.spacingRight. Negative x moves it to the left, positive x moves
     * it to the right. The default value of 15 together with align: "center" puts it in the center
     * of the plot area. Defaults to 0.
     */
    private Integer x;
    /**
     * The vertical offset of the legend relative to it's vertical alignment verticalAlign within
     * chart.spacingTop and chart.spacingBottom. Negative y moves it up, positive y moves it down.
     * Defaults to 0.
     */
    private Integer y;

    public String getAlign() {
        return align;
    }

    public void setAlign(String align) {
        this.align = align;
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

    public String getBorderRadius() {
        return borderRadius;
    }

    public void setBorderRadius(String borderRadius) {
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

    public Boolean getFloating() {
        return floating;
    }

    public void setFloating(Boolean floating) {
        this.floating = floating;
    }

    public Integer getItemMarginBottom() {
        return itemMarginBottom;
    }

    public void setItemMarginBottom(Integer itemMarginBottom) {
        this.itemMarginBottom = itemMarginBottom;
    }

    public Integer getItemMarginTop() {
        return itemMarginTop;
    }

    public void setItemMarginTop(Integer itemMarginTop) {
        this.itemMarginTop = itemMarginTop;
    }

    public Integer getItemWidth() {
        return itemWidth;
    }

    public void setItemWidth(Integer itemWidth) {
        this.itemWidth = itemWidth;
    }

    public String getLayout() {
        return layout;
    }

    public void setLayout(String layout) {
        this.layout = layout;
    }

    public Integer getLineHeight() {
        return lineHeight;
    }

    public void setLineHeight(Integer lineHeight) {
        this.lineHeight = lineHeight;
    }

    public Integer getMargin() {
        return margin;
    }

    public void setMargin(Integer margin) {
        this.margin = margin;
    }

    public Integer getPadding() {
        return padding;
    }

    public void setPadding(Integer padding) {
        this.padding = padding;
    }

    public Boolean getReversed() {
        return reversed;
    }

    public void setReversed(Boolean reversed) {
        this.reversed = reversed;
    }

    public Boolean getRtl() {
        return rtl;
    }

    public void setRtl(Boolean rtl) {
        this.rtl = rtl;
    }

    public Boolean getShadow() {
        return shadow;
    }

    public void setShadow(Boolean shadow) {
        this.shadow = shadow;
    }

    public Integer getSymbolPadding() {
        return symbolPadding;
    }

    public void setSymbolPadding(Integer symbolPadding) {
        this.symbolPadding = symbolPadding;
    }

    public Integer getSymbolWidth() {
        return symbolWidth;
    }

    public void setSymbolWidth(Integer symbolWidth) {
        this.symbolWidth = symbolWidth;
    }

    public String getVerticalAlign() {
        return verticalAlign;
    }

    public void setVerticalAlign(String verticalAlign) {
        this.verticalAlign = verticalAlign;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
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
        sb.append("Legend");
        sb.append("{align='").append(align).append('\'');
        sb.append(", backgroundColor='").append(backgroundColor).append('\'');
        sb.append(", borderColor='").append(borderColor).append('\'');
        sb.append(", borderRadius='").append(borderRadius).append('\'');
        sb.append(", borderWidth='").append(borderWidth).append('\'');
        sb.append(", enabled=").append(enabled);
        sb.append(", floating=").append(floating);
        sb.append(", itemMarginBottom=").append(itemMarginBottom);
        sb.append(", itemMarginTop=").append(itemMarginTop);
        sb.append(", itemWidth=").append(itemWidth);
        sb.append(", layout='").append(layout).append('\'');
        sb.append(", lineHeight=").append(lineHeight);
        sb.append(", margin=").append(margin);
        sb.append(", padding=").append(padding);
        sb.append(", reversed=").append(reversed);
        sb.append(", rtl=").append(rtl);
        sb.append(", shadow=").append(shadow);
        sb.append(", symbolPadding=").append(symbolPadding);
        sb.append(", symbolWidth=").append(symbolWidth);
        sb.append(", verticalAlign='").append(verticalAlign).append('\'');
        sb.append(", width=").append(width);
        sb.append(", x=").append(x);
        sb.append(", y=").append(y);
        sb.append('}');
        return sb.toString();
    }
}
