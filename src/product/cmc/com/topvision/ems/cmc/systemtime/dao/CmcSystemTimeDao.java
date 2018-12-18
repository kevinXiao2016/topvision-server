package com.topvision.ems.cmc.systemtime.dao;

import com.topvision.ems.cmc.domain.CmcEntity;
import com.topvision.ems.cmc.systemtime.facade.domain.CmcSystemTimeConfig;
import com.topvision.framework.dao.BaseEntityDao;

public interface CmcSystemTimeDao extends BaseEntityDao<CmcEntity> {
    public static final String NAME_SPACE = "CmcSystemTime.";
    
    /**
     * 获取系统时间配置
     * @param entityId
     * @return
     */
    CmcSystemTimeConfig selectCmcSystemTimeConfig(Long entityId);
    
    /**
     * 修改系统时间配置
     * @param cmcSystemTimeConfig
     */
    void updateCmcSystemTimeConfig(CmcSystemTimeConfig cmcSystemTimeConfig);   
    
    /**
     * 插入系统时间配置
     * @param entityId
     * @param cmcSystemTimeConfig
     */
    public void insertCmcSystemTimeConfig(Long entityId, CmcSystemTimeConfig cmcSystemTimeConfig);
    
}
