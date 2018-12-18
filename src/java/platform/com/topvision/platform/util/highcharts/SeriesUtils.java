/***********************************************************************
 * $ SeriesUtils.java,v1.0 2012-7-13 10:27:56 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.platform.util.highcharts;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import com.topvision.platform.util.highcharts.domain.Point;
import com.topvision.platform.util.highcharts.domain.Series;

/**
 * @author jay
 * @created @2012-7-13-10:27:56
 */
public class SeriesUtils {
    /**
     * 构造一条曲线加入到曲线队列
     * 
     * @param list
     *            曲线队列 如果为空则新建一个曲线队列
     * @param points
     *            点集合 从数据库中查询获取
     * @param name
     *            曲线名称 在Legend中显示
     * @param color
     *            曲线的颜色
     * @param type
     *            曲线的绘图类型
     * @param unit
     *            曲线显示的单位
     * @return List<Series>
     */
    public static List<Series> createSeriesArray(List<Series> list, List<Point> points, String name, String color,
            String type, String unit) {
        if (list == null) {
            list = new ArrayList<Series>();
        }
        NumberFormat nf = new DecimalFormat("##.##");
        Series series = new Series();
        series.setName(name);
        series.setUnit(unit);
        series.setType(type);
        series.setColor(color);
        List<List<Double>> data = new ArrayList<List<Double>>();
        double total = 0, max = 0, min = Double.MAX_VALUE, avg = 0, cur = 0;
        int count = 0;
        for (Point point : points) {
            //Modified by huangdongsheng，由于历史性能曲线图要展示负数（光机接收功率），注释掉这里的特殊处理
            //if (point.getY() < 0) {
            //    point.setY(0d);
            //}
            List<Double> pd = new ArrayList<Double>();
            pd.add(point.getX());
            pd.add(point.getY());
            data.add(pd);
            if (point.getY() > max) {
                max = point.getY();
            }
            if (point.getY() < min) {
                min = point.getY();
            }
            count++;
            total += point.getY();
            cur = point.getY();
        }
        if (count != 0) {
            avg = Double.parseDouble(nf.format(total / count));
        } else {
            min = 0;
            max = 0;
            avg = 0;
        }
        series.setMax(max);
        series.setMin(min);
        series.setAvg(avg);
        series.setCur(cur);
        series.setData(data);
        list.add(series);
        return list; // To change body of created methods use File | Settings | File Templates.
    }
}
