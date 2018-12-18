/***********************************************************************
 * $Id: OltMirrorDao.java,v1.0 2013-10-25 下午2:02:40 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.mirror.dao;

import java.util.List;

import com.topvision.ems.epon.mirror.domain.OltSniMirrorConfig;
import com.topvision.framework.dao.BaseEntityDao;

/**
 * @author flack
 * @created @2013-10-25-下午2:02:40
 *
 */
public interface OltMirrorDao extends BaseEntityDao<Object> {

    /**
     * 批量插入以及更新Sni口Mirror配置
     * 
     * @param sniMirrorConfigs
     */
    void batchInsertOltSniMirrorConfig(final List<OltSniMirrorConfig> sniMirrorConfigs, Long entityId);

    /**
     * 修改镜像名称
     * 
     * @param entityId
     * @param sniMirrorGroupIndex
     * @param sniMirrorGroupName
     */
    void modifyMirrorName(Long entityId, Integer sniMirrorGroupIndex, String sniMirrorGroupName);

    /**
     * 获取镜像组列表
     * 
     * @param entityId
     * @return
     */
    List<OltSniMirrorConfig> getMirrorConfigList(Long entityId);

    /**
     * MIRROR视图中更新目的端口、入流量端口、出流量端口列表
     * 
     * @param oltSniMirrorConfig
     */
    void updateMirrorPortList(OltSniMirrorConfig oltSniMirrorConfig);
}
