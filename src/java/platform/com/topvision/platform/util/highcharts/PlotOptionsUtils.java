/***********************************************************************
 * $ PlotOptionsUtils.java,v1.0 2012-7-15 13:20:56 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.platform.util.highcharts;

import com.topvision.platform.util.highcharts.domain.PlotOptions;

/**
 * @author jay
 * @created @2012-7-15-13:20:56
 */
public class PlotOptionsUtils {
    public static PlotOptions createDefaultPlotOptions() {
        PlotOptions plotOptions = new PlotOptions();
        PlotOptions.Line line = createDefaultLine(plotOptions);
        plotOptions.setLine(line);
        return plotOptions;
    }

    public static PlotOptions.Line createDefaultLine(PlotOptions plotOptions) {
        PlotOptions.Line line = plotOptions.new Line();
        PlotOptions.Marker marker = createDefaultMarker(plotOptions);
        PlotOptions.States states = createDefaultStates(plotOptions);
        line.setLineWidth(3);
        line.setMarker(marker);
        line.setShadow(false);
        line.setStates(states);
        return line;
    }

    private static PlotOptions.States createDefaultStates(PlotOptions plotOptions) {
        PlotOptions.States states = plotOptions.new States();
        PlotOptions.Hover hover = createDefaultStatesHover(plotOptions);
        states.setHover(hover);
        return states;
    }

    private static PlotOptions.Hover createDefaultStatesHover(PlotOptions plotOptions) {
        PlotOptions.Hover hover = plotOptions.new Hover();
        hover.setLineWidth(3);
        return hover;
    }

    public static PlotOptions.Marker createDefaultMarker(PlotOptions plotOptions) {
        PlotOptions.Marker marker = plotOptions.new Marker();
        marker.setEnabled(false);
        PlotOptions.States states = createDefaultMarkerStates(plotOptions);
        marker.setStates(states);
        return marker;
    }

    public static PlotOptions.States createDefaultMarkerStates(PlotOptions plotOptions) {
        PlotOptions.States states = plotOptions.new States();
        PlotOptions.Hover hover = createDefaultMarkerStatesHover(plotOptions);
        states.setHover(hover);
        return states;
    }

    public static PlotOptions.Hover createDefaultMarkerStatesHover(PlotOptions plotOptions) {
        PlotOptions.Hover hover = plotOptions.new Hover();
        hover.setEnabled(true);
        hover.setRadius(5);
        return hover;
    }
}
