/***********************************************************************
 * $Id: VersionService.java,v1.0 2012-11-28 下午2:29:37 $
 * 
 * @author: dengl
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.platform.service;

import java.util.List;

import com.topvision.framework.version.domain.Version;

/**
 * @author dengl
 * @created @2012-11-28-下午2:29:37
 *
 */
public interface VersionService {
    /**
     * 获取软件版本号
     * @return
     */
    List<Version> queryVersion();
}
