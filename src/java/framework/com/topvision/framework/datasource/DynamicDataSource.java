/***********************************************************************
 * $Id: DynamicDataSource.java,v1.0 2015-3-27 上午9:16:03 $
 * 
 * @author: Rod John
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.framework.datasource;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * @author Rod John
 * @created @2015-3-27-上午9:16:03
 *
 */
public class DynamicDataSource extends AbstractRoutingDataSource {

    /* (non-Javadoc)
     * @see org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource#determineCurrentLookupKey()
     */
    @Override
    protected Object determineCurrentLookupKey() {
        return DbContextHolder.getJdbcType();
    }

}
