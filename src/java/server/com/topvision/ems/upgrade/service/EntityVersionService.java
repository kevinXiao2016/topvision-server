/***********************************************************************
 * $Id: EntityVersionService.java,v1.0 2014年9月23日 下午3:12:53 $
 * 
 * @author: loyal
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.upgrade.service;

import java.util.List;

import com.topvision.ems.upgrade.domain.EntityVersion;
import com.topvision.framework.service.Service;

/**
 * @author loyal
 * @created @2014年9月23日-下午3:12:53
 * 
 */
public interface EntityVersionService extends Service {
    /**
     * 获取版本列表
     * 
     * @param versionName
     * @param typeId
     * @return
     */
    List<EntityVersion> getEntityVersionList(String versionName, Long typeId);

    /**
     * 获取版本说明文件内容
     * 
     * @param versionName
     * @return
     */
    String getEntityVersionProperty(String versionName);

    /**
     * 删除版本
     * 
     * @param versionName
     */
    List<Long> deleteEntityVersion(String versionName);

    /**
     * 添加版本
     * 
     * @param tempName
     *            临时文件名，不需要完整路径
     * @return
     */
    void addEntityVersion(String tempName);

    /**
     * 获取上传文件临时目录
     * 
     * @return
     */
    String getTempDir();

    /**
     * 通过VersionName获取一个版本的详细信息
     * 
     * @param versionName
     * @return
     */
    EntityVersion getEntityVersion(String versionName);
}
