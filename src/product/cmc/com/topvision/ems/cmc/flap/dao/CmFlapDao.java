package com.topvision.ems.cmc.flap.dao;

import java.util.List;

import com.topvision.ems.cmc.performance.domain.CMFlapHis;
import com.topvision.ems.cmc.performance.facade.CmFlap;
import com.topvision.framework.dao.BaseEntityDao;

public interface CmFlapDao extends BaseEntityDao<CMFlapHis> {

    /**
     * 查询历史记录-modified by bryan time由Date改为String
     * 
     * @param cmMac
     * @param startTime
     * @param endTime
     * @return
     */
    public List<CMFlapHis> getCmFlapHis(String cmMac, Long startTime, Long endTime);

    /**
     * 查询当前最新flap信息（根据 轮询和手动刷新皆更新cmflap表）
     * 
     * @param cmId
     * @return
     */
    CmFlap queryForLastCmFlapByCmId(Long cmId);

    /**
     * 根据EntityID，删除其所属所有CM的FLAP信息
     * 
     * @param cmcId
     */
    void deleteCmFlapByEntityId(Long entityId);

    /**
     * 批量插入或更新CM Flap信息
     * 
     * @param cmFlapList
     * @param entityId
     */
    void batchInsertOrUpdateCmFlap(final List<CmFlap> cmFlapList, Long entityId);
}
