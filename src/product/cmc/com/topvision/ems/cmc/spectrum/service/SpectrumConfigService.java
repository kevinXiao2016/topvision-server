/***********************************************************************
 * $ com.topvision.ems.cmc.spectrum.service.SpectrumConfigService,v1.0 2014-1-4 15:54:05 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.spectrum.service;

import java.util.List;
import java.util.Map;

import com.topvision.ems.cmc.spectrum.domain.CmtsSpectrumConfig;
import com.topvision.ems.cmc.spectrum.facade.domain.SpectrumOltSwitch;

/**
 * 开关管理模块(ConfigManager) 详细解释 OLT开关管理 　　在每次判断OLT开关状态的时候，直接去设备上获取一次OLT开关状态 　　提供设置设备侧OLT开关的方法
 * 　　提供刷新获取设备侧开关状态的方法 CC网管侧开关管理 　　提供存储、查询CC采集开关状态的接口 　　提供存储、查询CC历史频谱采集开关状态的接口
 * <p/>
 * 设备参数设置模块(ConfigManager) 详细解释 1、提供存储和查询采集参数的接口
 * 
 * @author jay
 * @created @2014-1-4-15:54:05
 */
public interface SpectrumConfigService {

    /**
     * 查询采集步长
     * 
     * @return
     */
    Integer getTimeInterval();

    /**
     * 从数据库获取cc开关状态
     * 
     * @param cmcId
     * @return
     */
    Boolean getCmcSwitchStatus(Long cmcId);

    /**
     * 客户端最大限制时长
     * 
     * @return
     */
    Long getTimeLimit();

    /**
     * 获取历史采集周期
     * 
     * @return
     */
    Long getHisCollectCycle();

    /**
     * 获取历史采集时长
     * 
     * @return
     */
    Long getHisCollectDuration();

    /**
     * 根据条件查询CMTS频谱配置
     * 
     * @param map
     * @return List<CmtsSpectrumConfig>
     * @add By YangYi 2014-01-22
     */
    List<CmtsSpectrumConfig> getCmtsSpectrumConfig(Map<String, Object> map);

    /**
     * 根据条件查询CMTS频谱配置的总数
     * 
     * @param map
     * @return Long
     * @add By YangYi 2014-01-22
     */
    Long getCmtsSpectrumConfigCount(Map<String, Object> map);

    /**
     * 批量开启CMTS频谱采集（网管侧）
     * 
     * @param cmcIds
     */
    void startSpectrumSwitchCmts(List<Long> cmcIds);

    /**
     * 批量关闭CMTS频谱采集开关（网管侧）
     * 
     * @param cmcIds
     */
    void stopSpectrumSwitchCmts(List<Long> cmcIds);

    /**
     * 批量开启CMTS历史频谱录像开关
     * 
     * @param cmcIds
     */
    void startHisVideoSwitch(List<Long> cmcIds, String dwrId, String sessionId);

    /**
     * 批量关闭CMTS历史频谱录像开关
     * 
     * @param cmcIds
     */
    void stopHisVideoSwitch(List<Long> cmcIds, String dwrId, String sessionId);

    /**
     * 获取历史采集步长
     * 
     * @return
     */
    Integer getHisTimeInterval();

    /**
     * 获取OLT频谱开关配置
     * 
     * @param map
     * @return
     */
    List<SpectrumOltSwitch> getOltSpectrumConfig(Map<String, Object> map);

    /**
     * 获取OLT频谱开关配置数量
     * 
     * @param map
     * @return
     */
    Long getOltSpectrumConfigCount(Map<String, Object> map);

    /**
     * 从数据库获取cc上联OLT开关状态
     * 
     * @param cmcId
     * @return
     */
    Boolean getOltSwitchStatus(Long cmcId);

    /**
     * 开启OLT频谱开关
     * 
     * @param entityIds
     */
    void startSpectrumSwitchOlt(Long[] entityIds, String dwrId);

    /**
     * 关闭OLT频谱开关
     * 
     * @param entityIds
     */
    void stopSpectrumSwitchOlt(Long[] entityIds, String dwrId);

    /**
     * 刷新OLT频谱开关
     * 
     * @param entityIds
     */
    void refreshSpectrumSwitchOlt(Long[] entityIds, String dwrId);

    /**
     * 从设备上读取OLT频谱开关
     * 
     * @param cmcId
     * @return
     */
    Boolean getOltSwtichFromDevice(Long cmcId);

    /**
     * 保存SystemPreferences
     * 
     * @param module
     * @param name
     * @param value
     */
    public void saveSystemPreference(String name, Object value);
}
