/***********************************************************************
 * $Id: VersionDao.java,v1.0 2012-11-28 下午2:33:08 $
 * 
 * @author: dengl
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.platform.dao;

import java.util.List;

import com.topvision.framework.version.domain.Version;

/**
 * @author dengl
 * @created @2012-11-28-下午2:33:08
 *
 */
public interface VersionControlDao {
    /**
     * 获取软件版本号
     * @return
     */
    List<Version> queryVersion();
}
