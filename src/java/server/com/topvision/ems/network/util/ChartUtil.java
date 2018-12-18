/***********************************************************************
 * $ ChartUtil.java,v1.0 2011-8-13 11:00:36 $
 *
 * @author: zhanglongyang
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.network.util;

import java.util.ArrayList;
import java.util.List;

/**
 * 图表工具类
 * 
 * @author zhanglongyang
 * @created @2011-7-26-14:47:36
 */
public class ChartUtil {
    /**
     * 生成silverlight饼图用xml
     * 
     * @param title
     * @param labelList
     * @param valueList
     * @param colors
     * @return
     */
    public static String getPieXml(String title, List<String> labelList, List<Double> valueList, String[] colors) {
        StringBuilder sBuilder = new StringBuilder();
        sBuilder.append("<vc:Chart xmlns:vc='clr-namespace:Visifire.Charts;assembly=SLVisifire.Charts' Width='400' Height='240' Theme='Theme1' View3D='True' BorderThickness='0'> ");
        sBuilder.append(" <vc:Chart.Titles> ");
        sBuilder.append("<vc:Title FontSize='16' Padding='10' Text='");
        sBuilder.append(title);
        sBuilder.append("'/></vc:Chart.Titles> ");
        sBuilder.append(" <vc:Chart.Series> ");
        sBuilder.append("<vc:DataSeries RenderAs='Pie' LabelEnabled='True' LabelFontFamily='Verdana'  LabelText='#AxisXLabel' LabelLineThickness='0.5' Bevel='False' ShowInLegend='False'> ");
        sBuilder.append(" <vc:DataSeries.DataPoints> ");
        for (int i = 0; i < labelList.size(); i++) {
            sBuilder.append("<vc:DataPoint Color='");
            sBuilder.append(colors[i]);
            sBuilder.append("' AxisXLabel='");
            sBuilder.append(labelList.get(i));
            sBuilder.append("' YValue='");
            sBuilder.append(valueList.get(i));
            sBuilder.append("' /> ");
        }
        sBuilder.append("</vc:DataSeries.DataPoints> </vc:DataSeries> </vc:Chart.Series> </vc:Chart>");
        return sBuilder.toString();
    }

    /**
     * 根据点数计算X轴坐标点位
     * 
     * @param size
     * @return
     */
    public static List<Integer> getMatch(int points) {
        int range = 0;
        if (points >= 1 && points <= 9) {
            range = 1;
        } else if (points >= 10 && points <= 19) {
            range = 2;
        } else if (points >= 20 && points <= 49) {
            range = 5;
        } else if (points >= 50 && points <= 99) {
            range = 10;
        } else if (points >= 100 && points <= 199) {
            range = 20;
        } else if (points >= 200 && points <= 499) {
            range = 50;
        } else if (points >= 500) {
            range = 100;
        }
        int line = points / range + 1;
        if (points % range == 0) {
            line--;
        }
        List<Integer> count = new ArrayList<Integer>();
        int c = 1;
        for (int i = 0; i < line; i++) {
            count.add(c);
            c += range;
        }
        return count;
    }
}