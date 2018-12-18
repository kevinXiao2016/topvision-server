/***********************************************************************
 * $ CreditsUtils.java,v1.0 2012-7-13 14:08:41 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.platform.util.highcharts;

import com.topvision.platform.ResourceManager;
import com.topvision.platform.util.highcharts.domain.Credits;

/**
 * @author jay
 * @created @2012-7-13-14:08:41
 */
public class CreditsUtils {
    public static Credits createDefaultCredits() {
        Credits credits = new Credits();
        credits.setHref("http://www.sumavision.com/");
        credits.setText(getResourceString("CreditsUtils.Sumavision", "com.topvision.ems.resources.resources"));
        return credits; // To change body of created methods use File | Settings | File Templates.
    }

    /*
     * key：properties文件的keymodule：资源文件
     */
    protected static String getResourceString(String key, String module) {
        try {
            ResourceManager resourceManager = ResourceManager.getResourceManager(module);
            return resourceManager.getNotNullString(key);
        } catch (Exception e) {
            return key;
        }
    }
}
