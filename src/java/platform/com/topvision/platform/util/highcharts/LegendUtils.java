/***********************************************************************
 * $ LegendUtils.java,v1.0 2012-7-13 14:11:13 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.platform.util.highcharts;

import com.topvision.platform.util.highcharts.domain.Legend;

/**
 * @author jay
 * @created @2012-7-13-14:11:13
 */
public class LegendUtils {
    public static Legend createDefaultLegend() {
        Legend legend = new Legend();
        legend.setEnabled(true);
        return legend;
    }
}
