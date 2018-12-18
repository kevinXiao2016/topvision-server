/***********************************************************************
 * $Id: TopVoipMediaProfService.java,v1.0 2017年6月21日 上午9:00:01 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.gpon.profile.service;

import java.util.List;

import com.topvision.ems.gpon.profile.facade.domain.TopVoipMediaProfInfo;

/**
 * @author haojie
 * @created @2017年6月21日-上午9:00:01
 *
 */
public interface TopVoipMediaProfService {
    /**
     * 获取VOIP媒体模板列表数据
     * 
     * @param entityId
     * @return
     */
    List<TopVoipMediaProfInfo> loadTopVoipMediaProfInfoList(Long entityId);

    /**
     * 获取单个VOIP媒体模板信息
     * 
     * @param entityId
     * @param profileId
     * @return
     */
    TopVoipMediaProfInfo loadTopVoipMediaProfInfo(Long entityId, Integer profileId);

    /**
     * 添加VOIP媒体模板
     * @param topVoipMediaProfInfo
     */
    void addTopVoipMediaProfInfo(TopVoipMediaProfInfo topVoipMediaProfInfo);

    /**
     * 修改VOIP媒体模板
     * @param topVoipMediaProfInfo
     */
    void modifyTopVoipMediaProfInfo(TopVoipMediaProfInfo topVoipMediaProfInfo);

    /**
     * 删除VOIP媒体模板
     * @param entityId
     * @param profileId
     */
    void deleteTopVoipMediaProfInfo(Long entityId, Integer profileId);

    /**
     * VOIP媒体模板列表从设备获取数据
     * 
     * @param entityId
     */
    void refreshTopVoipMediaProfInfo(Long entityId);
}
