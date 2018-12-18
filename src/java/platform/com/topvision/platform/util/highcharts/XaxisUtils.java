/***********************************************************************
 * $ XaxisUtils.java,v1.0 2012-7-13 10:27:22 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.platform.util.highcharts;

import com.topvision.platform.util.highcharts.domain.Xaxis;

import java.util.List;
import java.util.ArrayList;

/**
 * @author jay
 * @created @2012-7-13-10:27:22
 */
public class XaxisUtils {
    public static List<Xaxis> createDefaultXaxisArray(List<Xaxis> list, String type) {
        if (list == null) {
            list = new ArrayList<Xaxis>();
        }
        Xaxis xaxis = new Xaxis();
        xaxis.setType(type);
        xaxis.setTickPixelInterval(75);
        list.add(xaxis);
        return list; // To change body of created methods use File | Settings | File Templates.
    }
}
