/***********************************************************************
 * $Id: UpgradeJobDao.java,v1.0 2014年9月23日 下午3:56:27 $
 * 
 * @author: loyal
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.upgrade.dao;

import java.util.List;
import java.util.Map;

import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.upgrade.domain.UpgradeEntity;
import com.topvision.ems.upgrade.domain.UpgradeJobInfo;
import com.topvision.framework.dao.BaseEntityDao;

/**
 * @author loyal
 * @created @2014年9月23日-下午3:56:27
 * 
 */
public interface UpgradeJobDao extends BaseEntityDao<UpgradeJobInfo> {
    /**
     * 获取所有任务
     * 
     * @return
     */
    List<UpgradeJobInfo> selectAllJob();

    /**
     * 获取任务
     * 
     * @param jobId
     * @return
     */
    UpgradeJobInfo getJob(Long jobId);

    /**
     * 插入任务
     * 
     * @param job
     */
    Long insertJob(UpgradeJobInfo job);

    /**
     * 删除任务
     * 
     * @param jobId
     */
    void deleteJob(Long jobId);

    /**
     * 获取支持升级的设备（cc、onu）数量
     * 
     * @param map
     * @return
     */
    Long selectEntityNum(Map<String, Object> map);

    /**
     * 添加设备到任务
     * 
     * @param upgradeEntityList
     */
    void addJobEntity(List<UpgradeEntity> upgradeEntityList);

    /**
     * 获取升级任务中升级设备列表
     * 
     * @param map
     * @return
     */
    List<UpgradeEntity> selectUpgradeEntity(Map<String, Object> map);

    /**
     * 获取升级任务中升级设备数量
     * 
     * @param map
     * @return
     */
    Long selectUpgradeEntityNum(Map<String, Object> map);

    /**
     * 删除任务中设备
     * 
     * @param upgradeEntityList
     */
    void deleteJobEntity(List<UpgradeEntity> upgradeEntityList);

    /**
     * 根据Id获得UpgradeJobInfo
     * 
     * @param jobId
     * @return
     */
    UpgradeJobInfo getJobById(Long jobId);

    /**
     * 获取支持升级的设备（OLT）
     * 
     * @param map
     * @return
     */
    List<Entity> selectOltEntity(Map<String, Object> map);

    /**
     * 获取支持升级的设备（CCMTS）
     * 
     * @param map
     * @return
     */
    List<Entity> selectCcmtsEntity(Map<String, Object> map);

    /**
     * 更新升级状态
     * 
     * @param map
     * @return
     */
    void updateUpgradeEntity(UpgradeEntity upgradeEntity);

    /**
     * 获取升级设备信息
     * 
     * @param map
     * @return
     */
    UpgradeEntity selectUpgradeEntityByEntityId(Long entityId, Long jobId);

    /**
     * 获取job关联的entityId
     *
     * @param jobId
     * @return
     */
    List<UpgradeEntity> selectUpgradeEntityList(Long jobId);

    /**
     * 获取job关联的entityId
     *
     * @param jobId
     * @return
     */
    List<Long> selectUpgradeEntityIdList(Long jobId);

    /**
     * 根据name获得UpgradeJobInfo
     *
     * @param jobName
     * @return
     */
    UpgradeJobInfo selectJobByName(String jobName);

    /**
     * 根据versionName获得UpgradeJobInfo
     *
     * @param versionName
     * @return
     */
    List<UpgradeJobInfo> selectJobByVersionName(String versionName);

    /**
     * 根据slotNo 获取升级板卡的类型
     * 
     * @param map
     * @return
     */
    Integer selectBdPreConfigType(Map<String, Object> map);

    /**
     * 获取cc typeId
     * 
     * @param cmcEntityId
     * @return
     */
    List<Long> selectCmcTypeIdList(Long cmcEntityId);
}
