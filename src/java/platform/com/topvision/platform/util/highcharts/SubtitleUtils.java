/***********************************************************************
 * $ SubtitleUtils.java,v1.0 2012-7-13 10:26:57 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.platform.util.highcharts;

import com.topvision.platform.util.highcharts.domain.Subtitle;

/**
 * @author jay
 * @created @2012-7-13-10:26:57
 */
public class SubtitleUtils {
    public static Subtitle createDefaultTitle(String t) {
        Subtitle subtitle = new Subtitle();
        subtitle.setText(t);
        return subtitle; // To change body of created methods use File | Settings | File Templates.
    }
}
