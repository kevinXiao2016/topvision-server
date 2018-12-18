/***********************************************************************
 * $ Highcharts.java,v1.0 2012-7-12 14:21:38 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.platform.util.highcharts.domain;

import java.util.List;

/**
 * @author jay
 * @created @2012-7-12-14:21:38
 */
public class Highcharts {
    private String graphTitle;
    /**
     * Options regarding the chart area and plot area as well as general chart options.
     */
    private Chart chart;
    /**
     * 
     * An array containing the default colors for the chart's series. When all colors are used, new
     * colors are pulled from the start again. Defaults to: colors: [ '#4572A7', '#AA4643',
     * '#89A54E', '#80699B', '#3D96AE', '#DB843D', '#92A8CD', '#A47D7C', '#B5CA92' ]
     */
    private List<String> colors;
    /**
     * Highchart by default puts a credits label in the lower right corner of the chart. This can be
     * changed using these options.
     */
    private Credits credits;
    /**
     * Global options that don't apply to each chart. These options, like the lang options, must be
     * set using the Highcharts.setOptions method. Highcharts.setOptions({ global: { useUTC: false }
     * })
     */
    private Global global;
    private Labels labels;
    /**
     * Language object. The language object is global and it can't be set on each chart initiation.
     * Instead, use Highcharts.setOptions to set it before any chart is initiated.
     * Highcharts.setOptions({ lang: { months: ['Janvier', 'Fevrier', 'Mars', 'Avril', 'Mai',
     * 'Juin', 'Juillet', 'Aout', 'Septembre', 'Octobre', 'Novembre', 'Decembre'], weekdays:
     * ['Dimanche', 'Lundi', 'Mardi', 'Mercredi', 'Jeudi', 'Vendredi', 'Samedi'] } });
     */
    private Lang lang;
    /**
     * The legend is a box containing a symbol and name for each series item or point item in the
     * chart.
     */
    private Legend legend;
    /**
     * The loading options control the appearance of the loading screen that covers the plot area on
     * chart operations. This screen only appears after an explicit call to chart.showLoading(). It
     * is a utility for developers to communicate to the end user that something is going on, for
     * example while retrieving new data via an XHR connection. The "Loading..." text itself is not
     * part of this configuration object, but part of the lang object.
     */
    private Loading loading;
    /**
     * The plotOptions is a wrapper object for config objects for each series type. The config
     * objects for each series can also be overridden for each series item as given in the series
     * array.
     * 
     * Configuration options for the series are given in three levels. Options for all series in a
     * chart are given in the plotOptions.series object. Then options for all series of a specific
     * type are given in the plotOptions of that type, for example plotOptions.line. Next, options
     * for one single series are given in the series array.
     */
    private PlotOptions plotOptions;
    /**
     * Config options for the individual point as given in series.data.
     */
    private Point point;
    /**
     * The actual series to append to the chart. In addition to the members listed below, any member
     * of the plotOptions for that specific type of plot can be added to a series individually. For
     * example, even though a general lineWidth is specified in plotOptions.series, an individual
     * lineWidth can be specified for each series.
     */
    private List<Series> series;
    /**
     * The chart's subtitle
     */
    private Subtitle subtitle;
    /**
     * An array containing the default symbols for the series point markers. When all symbols are
     * used, new symbols are pulled from the start again. Defaults to: symbols: [ 'circle',
     * 'diamond', 'square', 'triangle', 'triangle-down' ]
     */
    private List<String> symbols;
    /**
     * The chart's main title
     */
    private Title title;
    /**
     * Options for the tooltip that appears when the user hovers over a series or point.
     */
    private Tooltip tooltip;
    /**
     * The X axis or category axis. Normally this is the horizontal axis, though if the chart is
     * inverted this is the vertical axis. In case of multiple axes, the xAxis node is an array of
     * configuration objects.
     */
    private List<Xaxis> xAxis;
    /**
     * The Y axis or value axis. Normally this is the vertical axis, though if the chart is inverted
     * this is the horiontal axis. In case of multiple axes, the yAxis node is an array of
     * configuration objects.
     */
    private List<Yaxis> yAxis;
    /**
     * Options for the exporting module.
     */
    private Exporting exporting;
    /**
     * A collection of options for buttons and menus appearing in the exporting module
     */
    private Navigation navigation;

    public Chart getChart() {
        return chart;
    }

    public void setChart(Chart chart) {
        this.chart = chart;
    }

    public List<String> getColors() {
        return colors;
    }

    public void setColors(List<String> colors) {
        this.colors = colors;
    }

    public Credits getCredits() {
        return credits;
    }

    public void setCredits(Credits credits) {
        this.credits = credits;
    }

    public Exporting getExporting() {
        return exporting;
    }

    public void setExporting(Exporting exporting) {
        this.exporting = exporting;
    }

    public Global getGlobal() {
        return global;
    }

    public void setGlobal(Global global) {
        this.global = global;
    }

    public Labels getLabels() {
        return labels;
    }

    public void setLabels(Labels labels) {
        this.labels = labels;
    }

    public Lang getLang() {
        return lang;
    }

    public void setLang(Lang lang) {
        this.lang = lang;
    }

    public Legend getLegend() {
        return legend;
    }

    public void setLegend(Legend legend) {
        this.legend = legend;
    }

    public Loading getLoading() {
        return loading;
    }

    public void setLoading(Loading loading) {
        this.loading = loading;
    }

    public Navigation getNavigation() {
        return navigation;
    }

    public void setNavigation(Navigation navigation) {
        this.navigation = navigation;
    }

    public PlotOptions getPlotOptions() {
        return plotOptions;
    }

    public void setPlotOptions(PlotOptions plotOptions) {
        this.plotOptions = plotOptions;
    }

    public Point getPoint() {
        return point;
    }

    public void setPoint(Point point) {
        this.point = point;
    }

    public List<Series> getSeries() {
        return series;
    }

    public void setSeries(List<Series> series) {
        this.series = series;
    }

    public Subtitle getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(Subtitle subtitle) {
        this.subtitle = subtitle;
    }

    public List<String> getSymbols() {
        return symbols;
    }

    public void setSymbols(List<String> symbols) {
        this.symbols = symbols;
    }

    public Title getTitle() {
        return title;
    }

    public void setTitle(Title title) {
        this.title = title;
    }

    public Tooltip getTooltip() {
        return tooltip;
    }

    public void setTooltip(Tooltip tooltip) {
        this.tooltip = tooltip;
    }

    public List<Xaxis> getxAxis() {
        return xAxis;
    }

    public void setxAxis(List<Xaxis> xAxis) {
        this.xAxis = xAxis;
    }

    public List<Yaxis> getyAxis() {
        return yAxis;
    }

    public void setyAxis(List<Yaxis> yAxis) {
        this.yAxis = yAxis;
    }

    public String getGraphTitle() {
        return graphTitle;
    }

    public void setGraphTitle(String graphTitle) {
        this.graphTitle = graphTitle;
    }

}
