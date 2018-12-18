/***********************************************************************
 * $ CmPollConfigService.java,v1.0 2012-5-2 10:39:52 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cm.cmpoll.config.service;

import com.topvision.ems.cm.cmpoll.config.domain.CmPollCollectParam;
import com.topvision.framework.service.Service;

import java.util.List;

/**
 * @author jay
 * @created @2012-5-2-10:39:52
 */
public interface CmPollConfigService extends Service {
    /**
     * 修改轮询周期
     * 
     * @param interval
     */
    public void modifyCmPollInterval(Long interval);

    /**
     * CM轮询是否关闭
     *
     * @return
     */
    public boolean isCmPollClose();

    /**
     * 采用全局CM列表作为采集列表
     *
     * @return
     */
    public boolean isGlobalCmPoll();

    /**
     * 采用指定CM列表作为采集列表
     *
     * @return
     */
    public boolean isSpecifiedCmPoll();

    /**
     * CM轮询是否为remoteQuery方式
     * 
     * @return
     */
    public boolean isCmPollRemoteQuery();

    /**
     * 获取CM轮询间隔
     * 
     * @return
     */
    public Long getCmPollInterval();

    /**
     * 获取采集器配置信息
     * 
     * @return
     */
    public CmPollCollectParam getCmPollCollectParam();

    /**
     * 切换cm轮询采集范围
     * @param switchName
     * @param cmPollInterval
     */
    void changeSwitchForCmPoll(String switchName, Long cmPollInterval);

    /**
     * 导入指定CM列表
     * @param macList
     */
    void importSpecifiedCmList(List<String> macList);

    /**
     * 获取指定轮询CM列表
     * @return
     */
    List<String> getSpecifiedCmList();

    /**
     * 获取CM轮询开关标签
     * @return
     */
    String getCmPollSwitchName();

}