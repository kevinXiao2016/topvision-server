/***********************************************************************
 * $Id: SQLDecompileService.java,v1.0 2016年7月21日 上午9:12:56 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.framework.version.service;


/**
 * @author Bravin
 * @created @2016年7月21日-上午9:12:56
 *
 */
public interface SQLDecompileService {

    /**
     * @param sql 
     * @param content
     * @return 
     */
    RollbackableProcess decompileSQL(String sql);

}
