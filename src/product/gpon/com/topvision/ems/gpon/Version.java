/***********************************************************************
 * $Id: Version.java,v1.0 2011-8-16 下午02:54:21 $
 * 
 * @author: Victor
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.gpon;

/**
 * @author jay
 * @created @2011-8-16-下午02:54:21
 * 
 */
@com.topvision.framework.annotation.Database(module = "gpon", version = "2.9.1.6", date = "2017-7-26")
public class Version extends com.topvision.framework.Version {
    private static final long serialVersionUID = -1025616448699606279L;

    public Version() {
        setBuildTime("@20110101-12:00:00@");
        setBuildVersion("@V100R001@");
        setBuildNumber("@B001@");
        setBuildUser("@Victor@");
    }
}