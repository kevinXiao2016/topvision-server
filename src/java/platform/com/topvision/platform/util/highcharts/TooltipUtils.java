/***********************************************************************
 * $ TooltipUtils.java,v1.0 2012-7-13 10:28:15 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.platform.util.highcharts;

import com.topvision.platform.util.highcharts.domain.Tooltip;

/**
 * @author jay
 * @created @2012-7-13-10:28:15
 */
public class TooltipUtils {
    public static Tooltip createDefaultTooltip() {
        Tooltip tooltip = new Tooltip();
        tooltip.setShared(true);
        return tooltip;
    }
}
