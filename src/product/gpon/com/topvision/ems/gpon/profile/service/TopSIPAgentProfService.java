/***********************************************************************
 * $Id: TopSIPAgentProfService.java,v1.0 2017年5月5日 下午1:30:50 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.gpon.profile.service;

import java.util.List;

import com.topvision.ems.gpon.profile.facade.domain.TopSIPAgentProfInfo;

/**
 * @author haojie
 * @created @2017年5月5日-下午1:30:50
 *
 */
public interface TopSIPAgentProfService {
    
    /**
     * 获取SIP代理模板列表数据
     * 
     * @param entityId
     * @return
     */
    List<TopSIPAgentProfInfo> loadTopSIPAgentProfInfoList(Long entityId);

    /**
     * 获取单个SIP代理模板信息
     * 
     * @param entityId
     * @param profileId
     * @return
     */
    TopSIPAgentProfInfo loadTopSIPAgentProfInfo(Long entityId, Integer profileId);

    /**
     * 添加SIP代理模板
     * @param topSIPAgentProfInfo
     */
    void addTopSIPAgentProfInfo(TopSIPAgentProfInfo topSIPAgentProfInfo);

    /**
     * 修改SIP代理模板
     * @param topSIPAgentProfInfo
     */
    void modifyTopSIPAgentProfInfo(TopSIPAgentProfInfo topSIPAgentProfInfo);

    /**
     * 删除SIP代理模板
     * @param entityId
     * @param profileId
     */
    void deleteTopSIPAgentProfInfo(Long entityId, Integer profileId);

    /**
     * SIP代理模板列表从设备获取数据
     * 
     * @param entityId
     */
    void refreshTopSIPAgentProfInfo(Long entityId);
}
