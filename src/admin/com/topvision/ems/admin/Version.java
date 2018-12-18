/***********************************************************************
 * $Id: Version.java,v 1.1 2010-1-17 下午03:25:40 kelers Exp $
 * 
 * @author: kelers
 * 
 * (c)Copyright 2011-2010 kelers All rights reserved.
 ***********************************************************************/
package com.topvision.ems.admin;

/**
 * @Create Date 2010-1-17 下午03:25:40
 * 
 * @author kelers
 * 
 */
@com.topvision.framework.annotation.Database(module = "admin", version = "2.2.5.0", date = "2014-07-15")
public class Version extends com.topvision.framework.Version {
    private static final long serialVersionUID = 5957790826554977011L;

    public Version() {
        setBuildTime("@20110101-12:00:00@");
        setBuildVersion("@V100R001@");
        setBuildNumber("@B001@");
        setBuildUser("@Victor@");
    }
}