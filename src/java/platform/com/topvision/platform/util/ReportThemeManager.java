/***********************************************************************
 * $Id: ReportThemeManager.java,v 1.1 Oct 7, 2009 1:16:13 PM kelers Exp $
 *
 * @author: kelers
 *
 * (c)Copyright 2011 Topoview All rights reserved.
 ***********************************************************************/
package com.topvision.platform.util;

import java.awt.Color;
import java.awt.Font;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.Plot;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.title.LegendTitle;
import org.jfree.ui.RectangleInsets;

import com.topvision.platform.ResourceManager;

/**
 * @author kelers
 * @Create Date Oct 7, 2009 1:16:13 PM
 */
public class ReportThemeManager {

    /**
     * 是否采用默认Chart风格.
     */
    public static boolean DEFAULT_CHART_STYLE = true;

    /**
     * 报表图形默认宽度.
     */
    public static int IMAGE_WIDTH = 560;

    /**
     * 报表图形默认高度.
     */
    public static int IMAGE_HEIGHT = 320;

    public static int PIE_CHART_WIDTH = 520;

    public static int PIE_CHART_HEIGHT = 280;

    /**
     * 报表背景.
     */
    public static Color BACKGROUND = Color.white;
    // public static final Paint BACKGROUND = new GradientPaint(80, 80,
    // Color.white, 0, 1000, Color.blue);

    /**
     * 报表图象背景.
     */
    public static Color IMG_BACKGROUND = Color.white;
    // public static Color IMG_BACKGROUND = Color.lightGray;

    /**
     * 报表字体.
     */
    public static Font DEFAULT_CHART_LEGEND_FONT = new Font("SansSerif", 0, 14);

    public static Font DEFAULT_CHART_LABEL_FONT = new Font("SansSerif", 0, 14);

    public static Font DEFAULT_CHART_FONT = new Font("SansSerif", 0, 12);

    public static Font AXIS_LABEL_FONT = new Font("SansSerif", 0, 12);

    public static Font TICK_LABEL_FONT = new Font("SansSerif", 0, 12);

    public static Font NO_MESSAGE_FONT = new Font("SansSerif", 0, 14);

    /**
     * 设置JFreeChart主题.
     * 
     * @param jfreeChart
     */
    public static void setJFreeChartTheme(JFreeChart jfreechart) {
        jfreechart.setBackgroundPaint(BACKGROUND);
        LegendTitle legend = jfreechart.getLegend();
        if (legend != null) {
            legend.setItemFont(DEFAULT_CHART_LEGEND_FONT);
        }

        Plot plot = jfreechart.getPlot();
        plot.setNoDataMessage(getResourceString("WorkBench.NoData", "com.topvision.ems.resources.resources"));
        plot.setNoDataMessageFont(NO_MESSAGE_FONT);
        plot.setBackgroundPaint(IMG_BACKGROUND);
        if (plot instanceof XYPlot) {
            XYPlot xyplot = (XYPlot) jfreechart.getPlot();
            // xyplot.setDomainGridlinePaint(Color.white);
            // xyplot.setRangeGridlinePaint(Color.white);
            xyplot.setDomainCrosshairVisible(true);
            xyplot.setRangeCrosshairVisible(true);
            xyplot.setAxisOffset(new RectangleInsets(5D, 5D, 5D, 5D));

            ValueAxis valueaxis = xyplot.getRangeAxis();
            valueaxis.setLabelFont(AXIS_LABEL_FONT);
            valueaxis.setTickLabelFont(TICK_LABEL_FONT);

            DateAxis dateaxis = (DateAxis) xyplot.getDomainAxis();
            dateaxis.setLabelFont(AXIS_LABEL_FONT);
            dateaxis.setTickLabelFont(TICK_LABEL_FONT);
            xyplot.getDomainAxis().setTickMarkPaint(Color.black);
            xyplot.getRangeAxis().setTickMarkPaint(Color.black);
        } else if (plot instanceof CategoryPlot) {
            CategoryPlot categoryplot = (CategoryPlot) jfreechart.getPlot();
            categoryplot.setAxisOffset(new RectangleInsets(5D, 5D, 5D, 5D));
            categoryplot.setBackgroundPaint(IMG_BACKGROUND);
            categoryplot.setDomainGridlinePaint(Color.white);
            categoryplot.setRangeGridlinePaint(Color.white);
            categoryplot.setForegroundAlpha(0.7F);

            ValueAxis valueaxis = categoryplot.getRangeAxis();
            valueaxis.setLabelFont(AXIS_LABEL_FONT);
            valueaxis.setTickLabelFont(TICK_LABEL_FONT);

            CategoryAxis categoryaxis = categoryplot.getDomainAxis();
            categoryaxis.setLabelFont(AXIS_LABEL_FONT);
            categoryaxis.setTickLabelFont(TICK_LABEL_FONT);
        }
    }

    /*
     * key：properties文件的keymodule：资源文件
     */
    protected static String getResourceString(String key, String module) {
        try {
            ResourceManager resourceManager = ResourceManager.getResourceManager(module);
            return resourceManager.getNotNullString(key);
        } catch (Exception e) {
            return key;
        }
    }
}
