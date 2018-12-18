/***********************************************************************
 * $Id: LoggerUtil.java,v1.0 2013-4-3 上午10:02:05 $
 * 
 * @author: Rod John
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.framework.common;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Rod John
 * @created @2013-4-3-上午10:02:05
 * 
 */
public class LoggerUtil {
    private static Logger collectLogger = LoggerFactory.getLogger("com.topvision.refreshCollect");
    public static Logger sampleCollectLogger = LoggerFactory.getLogger("com.topvision.sampleCollect");

    public static Long topoStartTimeLog(String ipAddress, String tableName) {
        if (collectLogger.isDebugEnabled()) {
            collectLogger.debug("Topology %" + ipAddress + "% " + tableName + " Start At "
                    + DateUtils.FULL_S_FORMAT.format(new Date()));
        }
        return System.currentTimeMillis();
    }

    public static void topoEndTimeLog(String ipAddress, String tableName, Long startTime) {
        if (collectLogger.isDebugEnabled()) {
            collectLogger.debug("Topology %" + ipAddress + "% " + tableName + " End At "
                    + DateUtils.FULL_S_FORMAT.format(new Date()) + " Table Time Last "
                    + (System.currentTimeMillis() - startTime) + "ms");
        }
    }

}
