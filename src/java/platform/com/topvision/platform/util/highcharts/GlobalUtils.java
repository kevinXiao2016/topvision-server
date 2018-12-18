/***********************************************************************
 * $ GlobalUtils.java,v1.0 2012-7-13 10:28:36 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.platform.util.highcharts;

import com.topvision.platform.util.highcharts.domain.Global;

/**
 * @author jay
 * @created @2012-7-13-10:28:36
 */
public class GlobalUtils {
    public static Global createDefaultGlobal() {
        Global global = new Global();
        global.setUseUTC(false);
        return global; // To change body of created methods use File | Settings | File Templates.
    }
}
