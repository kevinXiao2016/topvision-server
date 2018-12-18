package com.topvision.ems.epon.ofa.dao;

import java.util.List;

import com.topvision.ems.epon.ofa.facade.domain.OfaBasicInfo;

/**
 * 
 * @author CWQ
 * @created @2017年10月13日-下午5:08:12
 *
 */
public interface OfaBasicInfoDao {

    /**
     * 获取基本信息
     * 
     * @param entityId
     * @return
     */
    OfaBasicInfo getOfaBasicInfoById(Long entityId);

    /**
     * 批量插入或修改基本信息
     * 
     * @param topOFABasicInfoEntry
     */
    void batchInsertOrUpdateOfaBasicInfo(List<OfaBasicInfo> ofaBasicInfos);

    /**
     * 插入或修改基本信息
     * 
     * @param ofaBasicInfo
     */
    void insertOrUpdateOfaBasicInfo(OfaBasicInfo ofaBasicInfo);

    /**
     * 修改基本信息
     * 
     * @param ofaBasicInfo
     */
    void updateOfaBasicInfo(OfaBasicInfo ofaBasicInfo);
}
