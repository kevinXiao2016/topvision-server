/***********************************************************************
 * $Id: PingParamDao.java,v1.0 2017年3月17日 下午4:16:06 $
 * 
 * @author: ls
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.performance.engine;

import java.util.List;

import com.topvision.ems.performance.domain.PingParam;

/**
 * @author admin
 * @created @2017年3月17日-下午4:16:06
 *
 */
public interface PingParamDao {
    List<PingParam> queryPingParamByModule(String module);
}
