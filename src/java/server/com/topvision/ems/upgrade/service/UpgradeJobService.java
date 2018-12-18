/***********************************************************************
 * $Id: UpgradeJobService.java,v1.0 2014年9月23日 下午3:53:46 $
 * 
 * @author: loyal
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.upgrade.service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

import org.quartz.SchedulerException;

import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.upgrade.domain.UpgradeEntity;
import com.topvision.ems.upgrade.domain.UpgradeJobInfo;
import com.topvision.framework.service.Service;

/**
 * @author loyal
 * @created @2014年9月23日-下午3:53:46
 * 
 */
public interface UpgradeJobService extends Service {
    /**
     * 添加任务
     * 
     * @param job
     */
    void addJob(UpgradeJobInfo job);

    /**
     * 删除任务
     * 
     * @param jobId
     * @throws SchedulerException
     */
    void deleteJob(Long jobId) throws SchedulerException;

    /**
     * 修改任务间隔
     * 
     * @param job
     * @param interval
     */
    void modifyJobInterval(UpgradeJobInfo job, Long interval);

    /**
     * 获取所有升级任务
     * 
     * @return
     */
    List<UpgradeJobInfo> getAllUpgradeJob();

    /**
     * 获取支持升级的设备（cc、onu）
     * 
     * @param map
     * @return
     */
    List<Entity> getEntity(Map<String, Object> map, Long type);

    /**
     * 获取支持升级的设备（cc、onu）数量
     * 
     * @param map
     * @return
     */
    Long getEntityNum(Map<String, Object> map);

    /**
     * 获取升级任务中升级设备列表
     * 
     * @param map
     * @return
     */
    List<UpgradeEntity> getUpgradeEntity(Map<String, Object> map);

    /**
     * 获取升级任务中升级设备数量
     * 
     * @param map
     * @return
     */
    Long getUpgradeEntityNum(Map<String, Object> map);

    /**
     * 添加设备到任务
     * 
     * @param entityIds
     */
    void addEntityToJob(String entityIds, Long jobId);

    /**
     * 删除任务中设备
     * 
     * @param entityIds
     */
    void deleteJobEntity(String entityIds, Long jobId);

    /**
     * 根据ID获得UpgradeJobInfo
     * 
     * @param jobId
     * @return
     */
    UpgradeJobInfo getJobById(Long jobId);

    /**
     * 更新升级状态
     *
     * @return
     */
    void updateUpgradeEntity(UpgradeEntity upgradeEntity);

    /**
     * 获取升级设备信息
     *
     * @return
     */
    UpgradeEntity getUpgradeEntityByEntityId(Long entityId, Long jobId);

    /**
     * 根据name获得UpgradeJobInfo
     * 
     * @param jobName
     * @return
     */
    UpgradeJobInfo getJobByName(String jobName);

    /**
     * 判断任务名是否存在
     * 
     * @param jobName
     * @return
     */
    boolean isJobExist(String jobName);

    /**
     * 单个设备立即升级
     * 
     * @param entityId
     * @param jobId
     * @throws SchedulerException
     * @throws ClassNotFoundException
     */
    void upgradeSingleNow(Long entityId, Long jobId) throws ClassNotFoundException, SchedulerException;

    /**
     * 立即升级指定设备
     * 
     * @param entityIds
     * @param jobId
     * @throws SchedulerException
     * @throws ClassNotFoundException
     */
    void upgradeNow(List<Long> entityIds, Long jobId) throws SchedulerException, ClassNotFoundException;

    void putFutureJob(Long jobId, List<Future<UpgradeEntity>> jobFuture);

    List<Future<UpgradeEntity>> getFutureJob(Long jobId);

    /**
     * 根据versionName获得UpgradeJobInfo
     *
     * @param versionName
     * @return
     */
    List<UpgradeJobInfo> getJobByVersionName(String versionName);

    List<Future<UpgradeEntity>> upgradeEntityList(UpgradeJobInfo upgradeJobInfo, List<UpgradeEntity> upgradeEntityList);

    /**
     * 判断升级的板卡类型是否为gpua
     * 
     * @param map
     * @return
     */
    Boolean slotTypeIsGpua(Map<String, Object> map);

    /**
     * 判断olt下有没有某种类型的cc
     * 
     * @param entityId
     * @return
     */
    Boolean hasCmtsType(Long entityId, Long typeId);
}
