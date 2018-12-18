/***********************************************************************
 * $Id: SpectrumConfigDao.java,v1.0 2014-1-14 下午4:36:09 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.spectrum.dao;

import java.util.List;
import java.util.Map;

import com.topvision.ems.cmc.spectrum.domain.CmtsSpectrumConfig;
import com.topvision.ems.cmc.spectrum.facade.domain.SpectrumOltSwitch;

/**
 * @author haojie
 * @created @2014-1-14-下午4:36:09
 * 
 */
public interface SpectrumConfigDao {

    /**
     * 获取CMTS网管侧频谱开关
     * 
     * @param cmcId
     * @return Boolean
     */
    Boolean getCmtsSwitchStatus(Long cmcId);

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
     * 获取OLT频谱采集开关配置
     * 
     * @param map
     * @return
     */
    List<SpectrumOltSwitch> getOltSpectrumConfig(Map<String, Object> map);

    /**
     * 获取OLT频谱采集开关配置数量
     * 
     * @param map
     * @return
     */
    Long getOltSpectrumConfigCount(Map<String, Object> map);

    /**
     * 获取CMTS上联OLT网管侧频谱开关
     * 
     * @param cmcId
     * @return
     */
    Boolean getOltSwitchStatus(Long cmcId);

    /**
     * 更新OLT频谱开关
     * 
     * @param entityId
     * @param status
     */
    void updateSpectrumSwitchOlt(Long entityId, Integer status);

    /**
     * 根据OLT的entityId获取其频谱开关状态
     * 
     * @param entityId
     */
    Integer getSpectrumSwitchOlt(Long entityId);

}
