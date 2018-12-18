/***********************************************************************
 * $ ConfigWorkService.java,v1.0 2011-7-21 18:31:16 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/

package com.topvision.ems.template.service;

import java.util.List;
import java.util.Map;

import com.topvision.ems.template.domain.ApConfigParam;
import com.topvision.ems.template.domain.ApWorkTemplate;
import com.topvision.framework.service.Service;

/**
 * @author jay
 * @created @2011-7-21-18:31:16
 */
public interface ConfigWorkService extends Service {

    /**
     * 获取所有AP配置流程模板
     * 
     * @return
     */
    Map<String, ApWorkTemplate> getApWorkTemplates();

    /**
     * 通过模板名获得对应模板的所有参数模板
     * 
     * @param workTemplateName
     * @return
     */
    Map<String, ApConfigParam> getConfigParamByTemplate(String workTemplateName);

    /**
     * 通过流程模板名和参数模板名获得一个参数模板对象
     * 
     * @param workTemplateName
     * @param configTemplateName
     * @return
     */
    ApConfigParam getConfigParam(String workTemplateName, String configTemplateName);

    /**
     * 保存一个参数模板
     * 
     * @param workTemplateName
     * @param configTemplateName
     * @param apConfigParam
     */
    void saveConfigParamMode(String workTemplateName, String configTemplateName, ApConfigParam apConfigParam);

    /**
     * 是否有正在执行的配置流程
     * 
     * @return
     */
    boolean isWorking();

    /**
     * 执行一个配置流程
     * 
     * @param entitys
     *            需要配置的所有设备ID
     * @param workName
     *            配置流程名
     * @param apConfigParam
     *            配置流程参数
     * @throws com.topvision.exception.service.NowIsWorkingException
     *             配置流程全局只允许同时执行一个 当正在执行的时候 发起其他配置流程会抛出该异常
     * @throws com.topvision.exception.service.NoSuchWorkException
     *             没有找到对应的配置流程时抛出
     * @throws com.topvision.exception.service.ConfigException
     *             配置错误时抛出
     */
    void executeWorks(List<Long> entitys, String workName, ApConfigParam apConfigParam);

}