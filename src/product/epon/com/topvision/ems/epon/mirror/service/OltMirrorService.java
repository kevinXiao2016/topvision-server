/***********************************************************************
 * $Id: OltMirrorService.java,v1.0 2013-10-25 下午2:07:42 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.mirror.service;

import java.util.List;

import com.topvision.ems.epon.mirror.domain.OltSniMirrorConfig;
import com.topvision.framework.service.Service;

/**
 * @author flack
 * @created @2013-10-25-下午2:07:42
 *
 */
public interface OltMirrorService extends Service {
    /**
     * 修改镜像组名称
     * 
     * @param entityId
     * @param sniMirrorGroupIndex
     * @param sniMirrorGroupName
     */
    void modifyMirrorName(Long entityId, Integer sniMirrorGroupIndex, String sniMirrorGroupName);

    /**
     * 获取镜像列表
     * 
     * @param entityId
     * @return
     */
    List<OltSniMirrorConfig> getMirrorConfigList(Long entityId);

    /**
     * MIRROR视图中更新目的端口、入流量端口、出流量端口列表
     * 
     * @param entityId
     * @param sniMirrorGroupIndex
     * @param sniMirrorGroupSrcOutPortIndexList
     * @param sniMirrorGroupSrcInPortIndexList
     * @param sniMirrorGroupDstPortIndexList
     */
    void updateMirrorPortList(Long entityId, Integer sniMirrorGroupIndex, List<Long> sniMirrorGroupSrcOutPortIndexList,
            List<Long> sniMirrorGroupSrcInPortIndexList, List<Long> sniMirrorGroupDstPortIndexList);

    /**
     * 刷新OLT mirror信息
     * @param entityId
     */
    void refreshOltMirror(Long entityId);
}
