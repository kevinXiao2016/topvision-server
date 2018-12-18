/***********************************************************************
 * $ ChartUtils.java,v1.0 2012-7-13 10:26:34 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.platform.util.highcharts;

import com.topvision.platform.util.highcharts.domain.Chart;

/**
 * @author jay
 * @created @2012-7-13-10:26:34
 */
public class ChartUtils {
    public static Chart createDefaultChart(String chartId, String type, Integer width, Integer height) {
        Chart chart = new Chart();
        chart.setId(chartId);
        chart.setRenderTo(chartId);
        chart.setType(type);
        chart.setWidth(width);
        chart.setHeight(height);
        chart.setZoomType("x");
        chart.setSpacingRight(20);
        return chart; // To change body of created methods use File | Settings | File Templates.
    }
}
