/***********************************************************************
 * $Id: SnmpV3ErrorInfo.java,v1.0 2013-1-19 上午10:18:11 $
 * 
 * @author: RodJohn
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.framework.common;

import java.util.HashMap;
import java.util.Map;

/**
 * @author RodJohn
 * @created @2013-1-19-上午10:18:11
 * 
 */
public class SnmpV3ErrorInfo {

    static final Map<String, String> usmStats = new HashMap<String, String>() {
        /**
         * 
         */
        private static final long serialVersionUID = -7010799558909526752L;

        {
            put("1.3.6.1.6.3.15.1.1.1.0", "usmStatsUnsupportedSecLevels");
            put("1.3.6.1.6.3.15.1.1.2.0", "usmStatsNotInTimeWindows");
            put("1.3.6.1.6.3.15.1.1.3.0", "usmStatsUnknownUserNames");
            put("1.3.6.1.6.3.15.1.1.4.0", "usmStatsUnknownEngineIDs");
            put("1.3.6.1.6.3.15.1.1.5.0", "usmStatsWrongDigests");
            put("1.3.6.1.6.3.15.1.1.6.0", "usmStatsDecryptionErrors");
        }
    };

    public static String getSnmpV3ErrorInfo(String oid) {
        if (usmStats.containsKey(oid)) {
            return usmStats.get(oid);
        }
        return null;
    }

}
