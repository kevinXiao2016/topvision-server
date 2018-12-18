/***********************************************************************
 * $Id: CommonConfigService.java,v1.0 2014年7月16日 下午3:50:40 $
 * 
 * @author: loyal
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.network.service;

import java.util.List;

import com.topvision.framework.service.Service;

/**
 * @author loyal
 * @created @2014年7月16日-下午3:50:40
 *
 */
public interface CommonConfigService extends Service {
    /**
     * 获取配置命令下发时间间隔
     * 
     * @return
     */
    public Long getSendCommandInterval();

    /**
     * 修改配置命令下发时间间隔
     * 
     * @return
     */
    public void modifySendCommandInterval(Long sendCommandInterval);

    /**
     * 获取轮询间隔
     * 
     * @return
     */
    public Long getPollInterval();

    /**
     * @return
     */
    public List<String> getConfigs(Long typeId);

    /**
     * @return
     */
    public List<String> txGetConfigs(Long typeId);

    /**
     * 
     * @param typeId
     * @param folderId
     * @return
     */
    public List<String> txGetConfigs(Long typeId, Long folderId);

    /**
     * 修改轮询间隔
     * 
     * @return
     */
    public void modifyPollInterval(Long pollInterval);

    /**
     * 读取配置
     * 
     * @param cmcId
     * @return
     */
    public String readCommonConfig(Long type);

    /**
     * 读取配置
     * 
     * @param type
     * @param folderId
     * @return
     */
    public String readCommonConfig(Long type, Long folderId);

    /**
     * 保存配置
     * 
     * @param textArea
     */
    public void saveCommonConfig(String textArea, Long type);

    /**
     * 保存配置
     * 
     * @param textArea
     * @param type
     * @param folderId
     */
    public void saveCommonConfig(String textArea, Long type, Long folderId);

    /**
     * 保存配置
     * 
     * @param config
     */
    public void saveCommonConfig(List<String> configs, Long type);

    /**
     * 保存配置
     * 
     * @param configs
     * @param type
     * @param folderId
     */
    public void saveCommonConfig(List<String> configs, Long type, Long folderId);

    /**
     * 获取自动配置下发开关状态
     * 
     * @return
     */
    Boolean loadAutoSendConfigSwitch();

    /**
     * 获取失败后自动下发开关状态
     * 
     * @return
     */
    Boolean loadFailAutoSendConfigSwitch();

    /**
     * 设置自动配置下发开关状态
     */
    void configAutoSendConfigSwitch(Boolean switchStatus);

    /**
     * 设置失败自动配置下发开关状态
     */
    void configFailAutoSendConfigSwitch(Boolean switchStatus);

}
