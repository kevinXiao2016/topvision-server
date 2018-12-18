/***********************************************************************
 * $ YaxisUtils.java,v1.0 2012-7-13 10:27:33 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.platform.util.highcharts;

import java.util.ArrayList;
import java.util.List;

import com.topvision.platform.util.highcharts.domain.Yaxis;

/**
 * @author jay
 * @created @2012-7-13-10:27:33
 */
public class YaxisUtils {
    public static List<Yaxis> createDefaultYaxisArray(List<Yaxis> list, String yTitle) {
        if (list == null) {
            list = new ArrayList<Yaxis>();
        }
        Yaxis yaxis = new Yaxis();
        Yaxis.Title title = yaxis.new Title();
        title.setText(yTitle);
        yaxis.setTitle(title);
        yaxis.setMin(0.6);
        yaxis.setStartOnTick(false);
        yaxis.setShowFirstLabel(false);
        list.add(yaxis);
        return list; // To change body of created methods use File | Settings | File Templates.
    }

}
