/***********************************************************************
 * $Id: VersionServiceImpl.java,v1.0 2012-11-28 下午2:51:13 $
 * 
 * @author: dengl
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.platform.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.framework.service.BaseService;
import com.topvision.framework.version.domain.Version;
import com.topvision.platform.dao.VersionControlDao;
import com.topvision.platform.service.VersionService;

/**
 * @author dengl
 * @created @2012-11-28-下午2:51:13
 * 
 */
@Service("versionService")
public class VersionServiceImpl extends BaseService implements VersionService {
    @Autowired
    private VersionControlDao versionControlDao;

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.platform.service.VersionService#queryVersion()
     */
    @Override
    public List<Version> queryVersion() {
        return versionControlDao.queryVersion();
    }

    public void setVersionControlDao(VersionControlDao versionControlDao) {
        this.versionControlDao = versionControlDao;
    }
}
