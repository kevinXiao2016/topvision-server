package com.topvision.platform.util;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot3D;
import org.jfree.chart.plot.Plot;
import org.jfree.util.Rotation;

public class PortalUtils {

    public static void setChartStyle(JFreeChart jfreechart) {
        Plot plot = jfreechart.getPlot();
        jfreechart.getLegend().setItemFont(ReportThemeManager.DEFAULT_CHART_FONT);
        if (plot instanceof PiePlot3D) {
            PiePlot3D plot3d = (PiePlot3D) jfreechart.getPlot();
            plot3d.setLabelFont(ReportThemeManager.DEFAULT_CHART_FONT);
            plot3d.setDirection(Rotation.CLOCKWISE);
            plot3d.setForegroundAlpha(0.5F);
        }
    }
}
