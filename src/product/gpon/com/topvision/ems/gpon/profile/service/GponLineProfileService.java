/***********************************************************************
 * $Id: GponLineProfileService.java,v1.0 2016年12月17日 上午8:59:40 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.gpon.profile.service;

import java.util.List;

import com.topvision.ems.gpon.profile.facade.domain.GponLineProfileGem;
import com.topvision.ems.gpon.profile.facade.domain.GponLineProfileGemMap;
import com.topvision.ems.gpon.profile.facade.domain.GponLineProfileInfo;
import com.topvision.ems.gpon.profile.facade.domain.GponLineProfileTcont;

/**
 * @author haojie
 * @created @2016年12月17日-上午8:59:40
 *
 */
public interface GponLineProfileService {

    /**
     * 获取线路模板列表数据
     * 
     * @param entityId
     * @return
     */
    List<GponLineProfileInfo> loadGponLineProfileInfoList(Long entityId);

    /**
     * 获取单个线路模板信息
     * 
     * @param entityId
     * @param profileIndex
     * @return
     */
    GponLineProfileInfo loadGponLineProfileInfo(Long entityId, Integer profileIndex);

    /**
     * 添加线路模板
     * 
     * @param gponLineProfileInfo
     */
    void addGponLineProfileInfo(GponLineProfileInfo gponLineProfileInfo);

    /**
     * 修改线路模板
     * 
     * @param gponLineProfileInfo
     */
    void modifyGponLineProfileInfo(GponLineProfileInfo gponLineProfileInfo);

    /**
     * 删除线路模板
     * 
     * @param entityId
     * @param profileId
     */
    void deleteGponLineProfileInfo(Long entityId, Integer profileId);

    /**
     * 刷新OLT下线路模板列表
     * 
     * @param entityId
     */
    void refreshGponLineProfileList(Long entityId);

    /**
     * 获取某一线路模板下T-CONT列表
     * 
     * @param entityId
     * @return
     */
    List<GponLineProfileTcont> loadGponLineProfileTcontList(Long entityId, Integer profileIndex);

    /**
     * 获取单个T-CONT的信息
     * 
     * @param entityId
     * @param profileIndex
     * @param tcontIndex
     * @return
     */
    GponLineProfileTcont loadGponLineProfileTcont(Long entityId, Integer profileIndex, Integer tcontIndex);

    /**
     * 添加T-CONT
     * 
     * @param gponLineProfileTcont
     */
    void addGponLineProfileTcont(GponLineProfileTcont gponLineProfileTcont);

    /**
     * 修改T-CONT
     * 
     * @param gponLineProfileTcont
     */
    void modifyGponLineProfileTcont(GponLineProfileTcont gponLineProfileTcont);

    /**
     * 删除T-CONT
     * 
     * @param entity
     * @param profileIndex
     * @param tcontIndex
     */
    void deleteGponLineProfileTcont(Long entity, Integer profileIndex, Integer tcontIndex);

    /**
     * 刷新某一线路模板下T-CONT列表
     * 
     * @param entityId
     * @param profileIndex
     */
    void refreshGponLineProfileTcontList(Long entityId, Integer profileIndex);

    /**
     * 获取某一线路模板下GEM列表
     * 
     * @param entityId
     * @return
     */
    List<GponLineProfileGem> loadGponLineProfileGemList(Long entityId, Integer profileIndex);

    /**
     * 获取单个GEM信息
     * 
     * @param entityId
     * @param profileIndex
     * @param gemIndex
     * @return
     */
    GponLineProfileGem loadGponLineProfileGem(Long entityId, Integer profileIndex, Integer gemIndex);

    /**
     * 添加GEM
     * 
     * @param gponLineProfileGem
     */
    void addGponLineProfileGem(GponLineProfileGem gponLineProfileGem);

    /**
     * 修改GEM
     * 
     * @param gponLineProfileGem
     */
    void modifyGponLineProfileGem(GponLineProfileGem gponLineProfileGem);

    /**
     * 删除GEM
     * 
     * @param entityId
     * @param profileIndex
     * @param gemIndex
     */
    void deleteGponLineProfileGem(Long entityId, Integer profileIndex, Integer gemIndex);

    /**
     * 刷新某一线路模板下的GEM列表
     * 
     * @param entityId
     * @param profileIndex
     */
    void refreshGponLineProfileGemList(Long entityId, Integer profileIndex);

    /**
     * 获取某一GEM配置下的GEM映射列表
     * 
     * @param entityId
     * @return
     */
    List<GponLineProfileGemMap> loadGponLineProfileGemMapList(Long entityId, Integer profileIndex, Integer gemIndex);

    /**
     * 获取单个GEM映射信息
     * 
     * @param entityId
     * @param profileIndex
     * @param gemIndex
     * @param gemMapIndex
     * @return
     */
    GponLineProfileGemMap loadGponLineProfileGemMap(Long entityId, Integer profileIndex, Integer gemIndex,
            Integer gemMapIndex);

    /**
     * 添加GEM映射
     * 
     * @param gponLineProfileGemMap
     */
    void addGponLineProfileGemMap(GponLineProfileGemMap gponLineProfileGemMap);

    /**
     * 修改GEM映射
     * 
     * @param gponLineProfileGemMap
     */
    void modifyGponLineProfileGemMap(GponLineProfileGemMap gponLineProfileGemMap);

    /**
     * 删除GEM映射
     * 
     * @param entityId
     * @param profileIndex
     * @param gemIndex
     * @param gemMapIndex
     */
    void deleteGponLineProfileGemMap(Long entityId, Integer profileIndex, Integer gemIndex, Integer gemMapIndex);

    /**
     * 刷新某一GEM的GEM映射列表
     * 
     * @param entityId
     * @param profileIndex
     * @param gemIndex
     */
    void refreshGponLineProfileGemMapList(Long entityId, Integer profileIndex, Integer gemIndex);

    /**
     * 通过线路模板ID获取映射模式
     * @param entityId
     * @param profileId
     * @return
     */
    Integer getMappingModeByProfileId(Long entityId, Integer profileId);
}
