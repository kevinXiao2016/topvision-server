/***********************************************************************
 * $Id: DatabaseInfo.java,v 1.1 Sep 29, 2009 5:01:02 PM kelers Exp $
 *
 * @author: kelers
 *
 * (c)Copyright 2011 Topoview All rights reserved.
 ***********************************************************************/
package com.topvision.platform.domain;

import com.topvision.framework.domain.BaseEntity;

/**
 * @author kelers
 * @Create Date Sep 29, 2009 5:01:02 PM
 */
public class DatabaseInfo extends BaseEntity {
    private static final long serialVersionUID = -1591412059336302255L;

    private String databaseProductName;
    private String databaseProductVersion;

    public String getDatabaseProductName() {
        return databaseProductName;
    }

    public String getDatabaseProductVersion() {
        return databaseProductVersion;
    }

    public void setDatabaseProductName(String databaseProductName) {
        this.databaseProductName = databaseProductName;
    }

    public void setDatabaseProductVersion(String databaseProductVersion) {
        this.databaseProductVersion = databaseProductVersion;
    }

    @Override
    public String toString() {
        return "DatabaseInfo{" + "databaseProductName='" + databaseProductName + '\'' + ", databaseProductVersion='"
                + databaseProductVersion + '\'' + '}';
    }
}
