/***********************************************************************
 * $Id: Version.java,v1.0 2015-3-7 13:29:12 $
 * 
 * @author: loyal
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cm;

/**
 * @author loyal
 * @created @2015-3-7-13:29:12
 *
 */
@com.topvision.framework.annotation.Database(module = "cm", version = "2.10.0.13", date = "2018-09-11")
public class Version extends com.topvision.framework.Version {
    private static final long serialVersionUID = 5957790826554977011L;

    public Version() {
        setBuildTime("@20110101-12:00:00@");
        setBuildVersion("@V100R001@");
        setBuildNumber("@B001@");
        setBuildUser("@Victor@");
    }
}
