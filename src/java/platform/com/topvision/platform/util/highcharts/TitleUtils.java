/***********************************************************************
 * $ TitleUtils.java,v1.0 2012-7-13 10:26:45 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.platform.util.highcharts;

import com.topvision.platform.util.highcharts.domain.Title;

/**
 * @author jay
 * @created @2012-7-13-10:26:45
 */
public class TitleUtils {
    public static Title createDefaultTitle(String t) {
        Title title = new Title();
        title.setText(t);
        return title; // To change body of created methods use File | Settings | File Templates.
    }
}
