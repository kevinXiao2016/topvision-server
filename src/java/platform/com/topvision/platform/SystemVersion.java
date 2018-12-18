/***********************************************************************
 * $Id: SystemVersion.java,v1.0 2013-5-28 下午12:12:28 $
 * 
 * @author: Victor
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.platform;

/**
 * @author Victor
 * @created @2013-5-28-下午12:12:28
 * 
 */
public class SystemVersion extends com.topvision.framework.Version {
    private static final long serialVersionUID = 981828115101336434L;

    public SystemVersion() {
        setBuildVersion("2.11.0.0-F.1");
        setBuildTime("@20110101-12:00:00@");
        setBuildNumber("@B001@");
        setBuildUser("@Victor@");
    }
}