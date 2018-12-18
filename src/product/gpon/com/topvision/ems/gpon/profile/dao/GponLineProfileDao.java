/***********************************************************************
 * $Id: GponLineProffileDao.java,v1.0 2016年12月20日 上午11:53:38 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.gpon.profile.dao;

import java.util.List;

import com.topvision.ems.gpon.profile.facade.domain.GponLineProfileGem;
import com.topvision.ems.gpon.profile.facade.domain.GponLineProfileGemMap;
import com.topvision.ems.gpon.profile.facade.domain.GponLineProfileInfo;
import com.topvision.ems.gpon.profile.facade.domain.GponLineProfileTcont;
import com.topvision.framework.dao.Dao;

/**
 * @author haojie
 * @created @2016年12月20日-上午11:53:38
 *
 */
public interface GponLineProfileDao extends Dao {

    /**
     * 获取线路模板列表数据
     * 
     * @param entityId
     * @return
     */
    List<GponLineProfileInfo> selectGponLineProfileInfoList(Long entityId);

    /**
     * 获取T-CONT配置列表
     * 
     * @param entityId
     * @param profileIndex
     * @return
     */
    List<GponLineProfileTcont> selectGponLineProfileTcontList(Long entityId, Integer profileIndex);

    /**
     * 获取GEM配置列表
     * 
     * @param entityId
     * @param profileIndex
     * @return
     */
    List<GponLineProfileGem> selectGponLineProfileGemList(Long entityId, Integer profileIndex);

    /**
     * 获取GEM映射列表
     * 
     * @param entityId
     * @param profileIndex
     * @param gemIndex
     * @return
     */
    List<GponLineProfileGemMap> selectGponLineProfileGemMapList(Long entityId, Integer profileIndex, Integer gemIndex);

    /**
     * 获取单个线路模板信息
     * 
     * @param entityId
     * @param profileIndex
     * @return
     */
    GponLineProfileInfo selectGponLineProfileInfo(Long entityId, Integer profileIndex);

    /**
     * 获取单个T-CONT配置
     * 
     * @param entityId
     * @param profileIndex
     * @param tcontIndex
     * @return
     */
    GponLineProfileTcont selectGponLineProfileTcont(Long entityId, Integer profileIndex, Integer tcontIndex);

    /**
     * 获取单个GEM配置
     * 
     * @param entityId
     * @param profileIndex
     * @param gemIndex
     * @return
     */
    GponLineProfileGem selectGponLineProfileGem(Long entityId, Integer profileIndex, Integer gemIndex);

    /**
     * 获取单个GEM MAP配置
     * 
     * @param entityId
     * @param profileIndex
     * @param gemIndex
     * @param gemMapIndex
     * @return
     */
    GponLineProfileGemMap selectGponLineProfileGemMap(Long entityId, Integer profileIndex, Integer gemIndex,
            Integer gemMapIndex);

    /**
     * 新增线路模板
     * 
     * @param gponLineProfileInfo
     */
    void insertGponLineProfileInfo(GponLineProfileInfo gponLineProfileInfo);

    /**
     * 新增T-CONT配置
     * 
     * @param gponLineProfileTcont
     */
    void insertGponLineProfileTcont(GponLineProfileTcont gponLineProfileTcont);

    /**
     * 新增GEM配置
     * 
     * @param gponLineProfileGem
     */
    void insertGponLineProfileGem(GponLineProfileGem gponLineProfileGem);

    /**
     * 新增GEM映射配置
     * 
     * @param gponLineProfileGemMap
     */
    void insertGponLineProfileGemMap(GponLineProfileGemMap gponLineProfileGemMap);

    /**
     * 修改线路模板
     * 
     * @param gponLineProfileInfo
     */
    void updateGponLineProfileInfo(GponLineProfileInfo gponLineProfileInfo);

    /**
     * 修改T-CONT
     * 
     * @param gponLineProfileTcont
     */
    void updateGponLineProfileTcont(GponLineProfileTcont gponLineProfileTcont);

    /**
     * 修改GEM
     * 
     * @param gponLineProfileGem
     */
    void updateGponLineProfileGem(GponLineProfileGem gponLineProfileGem);

    /**
     * 修改GEM映射
     * 
     * @param gponLineProfileGemMap
     */
    void updateGponLineProfileGemMap(GponLineProfileGemMap gponLineProfileGemMap);

    /**
     * 删除业务模板
     * 
     * @param entityId
     * @param gponLineProfileId
     */
    void deleteGponLineProfileInfo(Long entityId, Integer gponLineProfileId);

    /**
     * 删除T-CONT
     * 
     * @param entityId
     * @param profileIndex
     * @param tcontIndex
     */
    void deleteGponLineProfileTcont(Long entityId, Integer profileIndex, Integer tcontIndex);

    /**
     * 删除GEM配置
     * @param entityId
     * @param profileIndex
     * @param gemIndex
     */
    void deleteGponLineProfileGem(Long entityId, Integer profileIndex, Integer gemIndex);

    /**
     * 删除GEM映射
     * @param entityId
     * @param profileIndex
     * @param gemIndex
     * @param gemMapIndex
     */
    void deleteGponLineProfileGemMap(Long entityId, Integer profileIndex, Integer gemIndex, Integer gemMapIndex);

    /**
     * 通过线路模板ID获取映射模式
     * @param entityId
     * @param profileId
     * @return
     */
    Integer getMappingModeByProfileId(Long entityId, Integer profileId);

}
