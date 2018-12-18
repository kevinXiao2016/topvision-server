/***********************************************************************
 * $Id: Version.java,v 1.1 2010-1-17 下午12:05:38 kelers Exp $
 *
 * @author: kelers
 *
 * (c)Copyright 2011-2010 kelers All rights reserved.
 ***********************************************************************/
package com.topvision.ems.enginemgr;

/**
 * @author kelers
 * @Create Date 2010-1-17 下午12:05:38
 */

@com.topvision.framework.annotation.Database(module = "enginemgr", version = "1.0.0.0", date = "2016-6-17")
public class Version extends com.topvision.framework.Version {
    private static final long serialVersionUID = -7447344873919273321L;

    public Version() {
        setBuildTime("@20110101-12:00:00@");
        setBuildVersion("@V100R001@");
        setBuildNumber("@B001@");
        setBuildUser("@Victor@");
    }
}
