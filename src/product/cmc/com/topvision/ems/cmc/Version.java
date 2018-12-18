/***********************************************************************
 * $Id: Version.java,v1.0 2011-8-16 下午02:54:21 $
 * 
 * @author: Victor
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc;

/**
 * @author Victor
 * @created @2011-8-16-下午02:54:21
 * 
 */
@com.topvision.framework.annotation.Database(module = "cmc", version = "2.10.0.13", date = "2018-09-04")
public class Version extends com.topvision.framework.Version {
    private static final long serialVersionUID = -1025616448699606279L;

    public Version() {
        setBuildTime("@20110101-12:00:00@");
        setBuildVersion("@V100R001@");
        setBuildNumber("@B001@");
        setBuildUser("@Victor@");
    }
}