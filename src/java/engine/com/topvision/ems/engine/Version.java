/***********************************************************************
 * $Id: Version.java,v 1.1 2010-1-17 下午03:25:40 kelers Exp $
 * 
 * @author: kelers
 * 
 * (c)Copyright 2011-2010 kelers All rights reserved.
 ***********************************************************************/
package com.topvision.ems.engine;

/**
 * @Create Date 2010-1-17 下午03:25:40
 * 
 * @author kelers
 * 
 */
@com.topvision.framework.annotation.Database(module = "engine", version = "1.0.0.0", date = "2011-4-1")
public class Version extends com.topvision.framework.Version {
    private static final long serialVersionUID = 2704283508316428260L;

    public Version() {
        setBuildTime("@20110101-12:00:00@");
        setBuildVersion("@V100R001@");
        setBuildNumber("@B001@");
        setBuildUser("@Victor@");
    }
}