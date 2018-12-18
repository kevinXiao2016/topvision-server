/***********************************************************************
 * $ HighChartsUtilsTest.java,v1.0 2012-7-13 10:30:30 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.platform.util.highcharts;

/**
 * @author jay
 * @created @2012-7-13-10:30:30
 *
 */

import org.junit.Test;
import com.topvision.platform.util.highcharts.*;
import com.topvision.platform.util.highcharts.domain.*;

import java.util.List;
import java.util.ArrayList;

public class HighChartsUtilsTest {

    @Test
    public void testCreateDefaultSplineXdateTimeChart() throws Exception {
        Highcharts highcharts = HighChartsUtils.createDefaultLineXdateTimeChart("test1", "this is a test", null, 400,
                300);
        List<Xaxis> xaxis = XaxisUtils.createDefaultXaxisArray(null, "datetime");
        highcharts.setxAxis(xaxis);
        List<Yaxis> yaxis = YaxisUtils.createDefaultYaxisArray(null, "ChannelUsage");
        highcharts.setyAxis(yaxis);
        Legend legend = LegendUtils.createDefaultLegend();
        highcharts.setLegend(legend);
        Credits credits = CreditsUtils.createDefaultCredits();
        highcharts.setCredits(credits);
        Labels labels = LabelsUtils.createDefaultLabels(null, "test label", 200, 350, "black");
        highcharts.setLabels(labels);
        System.out.println("json = " + HighChartsUtils.toJSON(highcharts));
    }

    @Test
    public void testSeries() throws Exception {
        /*
         * 
         * [1274457600000, 1200], [1274544000000, 1300], [1274630400000, 1250], [1274803200000,1350]
         */
        List<Point> list = new ArrayList<Point>();
        Point point1 = new Point();
        point1.setX(1274457600000d);
        point1.setY(1200d);
        Point point2 = new Point();
        point2.setX(1274544000000d);
        point2.setY(1300d);
        Point point3 = new Point();
        point3.setX(1274630400000d);
        point3.setY(1250d);
        Point point4 = new Point();
        point4.setX(1274803200000d);
        point4.setY(1350d);
        list.add(point1);
        list.add(point2);
        list.add(point3);
        list.add(point4);
        List<Series> series = SeriesUtils.createSeriesArray(null, list, "US 1/1/1", "#4572A7", null, "dB");
        System.out.println("series = " + HighChartsUtils.toJSON(series));
    }
}