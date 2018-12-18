/***********************************************************************
 * $Id: DevicePerfTargetServiceImpl.java,v1.0 2014-3-12 下午2:25:21 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.performance.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.facade.domain.OperClass;
import com.topvision.ems.facade.domain.ScheduleMessage;
import com.topvision.ems.network.service.EntityService;
import com.topvision.ems.performance.dao.DevicePerfTargetDao;
import com.topvision.ems.performance.dao.PerformanceDao;
import com.topvision.ems.performance.domain.DevicePerfTarget;
import com.topvision.ems.performance.domain.PerfTaskUpdateInfo;
import com.topvision.ems.performance.domain.ScheduleUpdateFuture;
import com.topvision.ems.performance.service.DevicePerfTargetService;
import com.topvision.ems.performance.service.PerfTargetService;
import com.topvision.ems.performance.service.PerformanceService;
import com.topvision.ems.template.service.EntityTypeService;
import com.topvision.framework.service.BaseService;
import com.topvision.framework.snmp.SnmpParam;

/**
 * @author flack
 * @created @2014-3-12-下午2:25:21
 *
 */
@Service("devicePerfTargetService")
public class DevicePerfTargetServiceImpl extends BaseService implements DevicePerfTargetService {
    private static final Logger logger = LoggerFactory.getLogger(DevicePerfTargetServiceImpl.class);

    @Autowired
    private EntityService entityService;
    @Autowired
    private EntityTypeService entityTypeService;
    @Autowired
    private PerformanceService<OperClass> performanceService;
    @Autowired
    private DevicePerfTargetDao devicePerfTargetDao;
    @Autowired
    private PerfTargetService perfTargetService;
    @Autowired
    private PerformanceDao performanceDao;
    @Value("${perfTarget.batchPoolSize}")
    private Integer batchPoolSize;

    @Override
    public List<DevicePerfTarget> getDevicePerfList(Map<String, Object> paramsMap) {
        // 先查询到满足条件的设备,只需要entityId就可以
        List<Long> idList = devicePerfTargetDao.queryDevice(paramsMap);
        List<DevicePerfTarget> allTargetList = new ArrayList<DevicePerfTarget>();
        List<DevicePerfTarget> deviceTarget = null;
        for (Long entityId : idList) {
            paramsMap.put("entityId", entityId);
            deviceTarget = devicePerfTargetDao.queryDevicePerfList(paramsMap);
            if (deviceTarget.size() == 0) {
                Entity entity = entityService.getEntity(entityId);
                DevicePerfTarget $target = new DevicePerfTarget();
                $target.setEntityId(entityId);
                if (entity.getIp() == null) {
                    $target.setManageIp(devicePerfTargetDao.getManageIpById(entityId));
                } else {
                    $target.setManageIp(entity.getIp());
                }
                $target.setDeviceName(entity.getName());
                $target.setDisplayName(entity.getTypeName());
                $target.setPerfTargetName("none");
                $target.setTypeId(-1L);
                $target.setEntityType(entity.getTypeId());
                allTargetList.add($target);
            } else {
                allTargetList.addAll(deviceTarget);
            }
        }
        return allTargetList;
    }

    @Override
    public int getDeviceNum(Map<String, Object> paramsMap) {
        return devicePerfTargetDao.queryDeviceNum(paramsMap);
    }

    @Override
    public List<DevicePerfTarget> getAllDevicePerfTarget(Long entityId) {
        return devicePerfTargetDao.queryAllDevicePerfTarget(entityId);
    }

    @Override
    public List<DevicePerfTarget> getGlobalPerfTargetList(Long entityType) {
        return devicePerfTargetDao.queryGlobalPerfTargetList(entityType);
    }

    @Override
    public List<DevicePerfTarget> getDeviceGlobalPerfList(Long entityId) {
        return devicePerfTargetDao.queryDeviceGlobalPerfList(entityId);
    }

    @Override
    public List<DevicePerfTarget> getPerfTargetDeviceList(Map<String, Object> paramsMap) {
        // 先查询到满足条件的设备,只需要entityId就可以
        List<Long> idList = devicePerfTargetDao.querySupportTargetDevice(paramsMap);
        List<DevicePerfTarget> allTargetList = new ArrayList<DevicePerfTarget>();
        List<DevicePerfTarget> deviceTarget = null;
        for (Long entityId : idList) {
            paramsMap.put("entityId", entityId);
            deviceTarget = devicePerfTargetDao.queryDevicePerfList(paramsMap);
            allTargetList.addAll(deviceTarget);
        }
        return allTargetList;
    }

    @Override
    public int getPerfTargetDeviceNum(Map<String, Object> paramsMap) {
        return devicePerfTargetDao.querySupportTargetDeviceNum(paramsMap);
    }

    @Override
    public List<DevicePerfTarget> getDeviceSupportTarget(Long typeId) {
        return devicePerfTargetDao.queryDeviceSupportTarget(typeId);
    }

    @Override
    public List<DevicePerfTarget> getTargetByTypeIdAndGroup(Long typeId, String targetGroup) {
        Map<String, Object> paramsMap = new HashMap<String, Object>();
        paramsMap.put("typeId", typeId);
        paramsMap.put("targetGroup", targetGroup);
        return devicePerfTargetDao.queryTargetByTypeIdAndGroup(paramsMap);
    }

    @Override
    public List<DevicePerfTarget> getGroupTargetByType(String groupName, Long entityType) {
        return devicePerfTargetDao.queryGroupTargetByType(groupName, entityType);
    }

    @Override
    public DevicePerfTarget getGlobalTargetByType(String targetName, Long entityType) {
        return devicePerfTargetDao.queryGlobalTargetByType(targetName, entityType);
    }

    @Override
    public void modifyDevicePerfTarget(List<DevicePerfTarget> perfTargetList, Long entityId) {
        List<DevicePerfTarget> oldTargetlist = devicePerfTargetDao.queryAllDevicePerfTarget(entityId);
        // 与更改的性能指标进行比较并更改当前设备的性能采集
        List<PerfTaskUpdateInfo> updateTaskList = this.getNeedUpdateTaskList(perfTargetList, oldTargetlist);
        // 更新采集任务
        List<ScheduleMessage<OperClass>> scheduleList = performanceDao.queryScheduleByEntityId(entityId);
        performanceService.updatePerformanceTask(scheduleList, updateTaskList);
        // 更新数据库
        devicePerfTargetDao.batchUpdateDevicePerf(perfTargetList);
    }

    @Override
    public void modifyGlobalPerfTarget(List<DevicePerfTarget> perfTargetList) {
        devicePerfTargetDao.batchUpdateGlobalPerf(perfTargetList);
    }

    @Override
    public void batchInsertDeviceTarget(Long entityId, List<DevicePerfTarget> perfTargetList) {
        devicePerfTargetDao.batchInsertDevicePerf(entityId, perfTargetList);
    }

    @Override
    public void addDevicePerfTarget(DevicePerfTarget target) {
        devicePerfTargetDao.insertOrUpdatePerfTarget(target);
    }

    @Override
    public Map<String, Object> applyTargetToAllDevice(final List<DevicePerfTarget> targetList, Long entityType,
            Long typeId, boolean saveGlobalFlag) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("entityType", entityType);
        paramMap.put("typeId", typeId);
        // 根据设备类型获得所有该类型设备对应的性能指标数据,并将该类型所有设备的性能指标数据转换为entityId与具体设备的性能指标数据相对应的关系
        List<DevicePerfTarget> allDeviceTarget = devicePerfTargetDao.queryDevicePerfByType(paramMap);
        Map<Long, List<DevicePerfTarget>> targetMap = this.encodeTargetToEntity(allDeviceTarget);
        // 查询需要进行修改的perfMonitor,并将查询出来的perfmonitor转换为单个设备与设备monitor的List对应关系
        List<ScheduleMessage<OperClass>> scheduleList = performanceDao.queryScheduleByEntityType(paramMap);
        final Map<Long, List<ScheduleMessage<OperClass>>> scheduleMap = encodeScheduleToEntity(scheduleList);
        // 对每个设备单独进行采集任务的更新
        List<ScheduleUpdateFuture> futureList = new ArrayList<ScheduleUpdateFuture>();
        ExecutorService executor = Executors.newFixedThreadPool(batchPoolSize);
        for (final Entry<Long, List<DevicePerfTarget>> targetEntry : targetMap.entrySet()) {
            Future<?> updateFuture = executor.submit(new Runnable() {
                @Override
                public void run() {
                    Long entityId = targetEntry.getKey();
                    List<DevicePerfTarget> oldTargetList = targetEntry.getValue();
                    // 每次传进去的应该是当前指标列表的副本,因为在方法内部会涉及到对列表的更改
                    List<DevicePerfTarget> updateTargetListCopy = new ArrayList<DevicePerfTarget>(targetList);
                    List<DevicePerfTarget> oldTargetListCopy = new ArrayList<DevicePerfTarget>(oldTargetList);
                    List<PerfTaskUpdateInfo> updateTaskList = getNeedUpdateTaskList(updateTargetListCopy,
                            oldTargetListCopy);
                    // 更新采集任务
                    performanceService.updatePerformanceTask(scheduleMap.get(entityId), updateTaskList);
                    devicePerfTargetDao.batchUpdateDeviceTarget(oldTargetListCopy);
                }
            });
            // 构造用于记录结果的ScheduleUpdateFuture
            ScheduleUpdateFuture scheduleUpdateFuture = new ScheduleUpdateFuture();
            scheduleUpdateFuture.setEntityId(targetEntry.getKey());
            scheduleUpdateFuture.setDeviceName(targetEntry.getValue().get(0).getDeviceName());
            scheduleUpdateFuture.setManageIp(targetEntry.getValue().get(0).getManageIp());
            scheduleUpdateFuture.setUpdateFuture(updateFuture);
            futureList.add(scheduleUpdateFuture);
        }
        // 获取操作结果记录
        Map<String, Object> resultMap = getUpdateResultRecord(futureList);
        // 保存为全局
        if (saveGlobalFlag) {
            devicePerfTargetDao.batchUpdateGlobalPerf(targetList);
        }
        return resultMap;
    }

    @Override
    public Map<String, Object> modifyDeviceSingleTarget(final DevicePerfTarget newTarget, String entityIds) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("perfTargetName", newTarget.getPerfTargetName());
        paramMap.put("entityIds", entityIds);
        // 根据指定的指标名称和选择的设备获取设备性能指标数据
        List<DevicePerfTarget> oldTargetList = devicePerfTargetDao.queryDeviceSingleTarget(paramMap);
        // 查询需要进行修改的perfMonitor,并将查询出来的perfmonitor转换为单个设备与设备monitor的List对应关系
        List<ScheduleMessage<OperClass>> scheduleList = performanceDao.queryScheduleByIdList(paramMap);
        final Map<Long, List<ScheduleMessage<OperClass>>> scheduleMap = encodeScheduleToEntity(scheduleList);
        // 对每个设备单独进行采集任务的更新
        List<ScheduleUpdateFuture> futureList = new ArrayList<ScheduleUpdateFuture>();
        ExecutorService executor = Executors.newFixedThreadPool(batchPoolSize);
        for (final DevicePerfTarget oldTarget : oldTargetList) {
            Future<?> updateFuture = executor.submit(new Runnable() {
                @Override
                public void run() {
                    DevicePerfTarget currentOldTarget = oldTarget;
                    List<PerfTaskUpdateInfo> updateTaskList = getSingleTargetUpdateTaskList(newTarget,
                            currentOldTarget);
                    // 更新设备性能指标采集周期
                    performanceService.updatePerformanceTask(scheduleMap.get(currentOldTarget.getEntityId()),
                            updateTaskList);
                    // 更新数据库单个设备单个指标
                    currentOldTarget.setCollectInterval(newTarget.getCollectInterval());
                    currentOldTarget.setTargetEnable(newTarget.getTargetEnable());
                    devicePerfTargetDao.updateDevicePerfTarget(currentOldTarget);
                }
            });
            // 构造用于记录结果的ScheduleUpdateFuture
            ScheduleUpdateFuture scheduleUpdateFuture = new ScheduleUpdateFuture();
            scheduleUpdateFuture.setEntityId(oldTarget.getEntityId());
            scheduleUpdateFuture.setDeviceName(oldTarget.getDeviceName());
            scheduleUpdateFuture.setManageIp(oldTarget.getManageIp());
            scheduleUpdateFuture.setUpdateFuture(updateFuture);
            futureList.add(scheduleUpdateFuture);
        }
        // 获取操作结果记录
        Map<String, Object> resultMap = getUpdateResultRecord(futureList);
        return resultMap;
    }

    /**
     * 处理修改结果记录
     * 
     * @param futureList
     * @return
     */
    private Map<String, Object> getUpdateResultRecord(List<ScheduleUpdateFuture> futureList) {
        // 用以记录操作结果
        Map<String, Object> resultMap = new HashMap<String, Object>();
        List<JSONObject> succeedData = new ArrayList<JSONObject>();
        List<JSONObject> failedData = new ArrayList<JSONObject>();
        JSONObject resultJson = null;
        // 通过Future进行运行结果的分析，从而记录操作结果
        for (ScheduleUpdateFuture recordFuture : futureList) {
            try {
                recordFuture.getUpdateFuture().get();
                // 记录操作结果
                resultJson = new JSONObject();
                resultJson.put("deviceName", recordFuture.getDeviceName());
                resultJson.put("deviceIp", recordFuture.getManageIp());
                succeedData.add(resultJson);
            } catch (InterruptedException | ExecutionException e) {
                // 记录操作结果
                resultJson = new JSONObject();
                resultJson.put("deviceName", recordFuture.getDeviceName());
                resultJson.put("deviceIp", recordFuture.getManageIp());
                failedData.add(resultJson);
                // 记录错误信息
                logger.error("Entity[{}_{}] updatePerformanceTask Error: {}", recordFuture.getEntityId(),
                        recordFuture.getDeviceName(), e);
            }
        }
        // 记录结果
        resultMap.put("succeedNum", succeedData.size());
        resultMap.put("succeedDetail", succeedData);
        resultMap.put("failedNum", failedData.size());
        resultMap.put("failedDetail", failedData);
        return resultMap;
    }

    /**
     * 将根据类型查找出来的性能指标转化为具体设备对应的性能指标列表
     * 
     * @param allDeviceTarget
     * @return
     */
    private Map<Long, List<DevicePerfTarget>> encodeTargetToEntity(List<DevicePerfTarget> allDeviceTarget) {
        List<DevicePerfTarget> specialDeviceTarget = null;
        // 转换为entityId与具体设备的性能指标数据相对应的关系
        Map<Long, List<DevicePerfTarget>> targetMap = new HashMap<Long, List<DevicePerfTarget>>();
        for (DevicePerfTarget perfTarget : allDeviceTarget) {
            Long entityId = perfTarget.getEntityId();
            specialDeviceTarget = targetMap.get(entityId);
            if (specialDeviceTarget == null) {
                specialDeviceTarget = new ArrayList<DevicePerfTarget>();
                targetMap.put(entityId, specialDeviceTarget);
            }
            specialDeviceTarget.add(perfTarget);
        }
        return targetMap;
    }

    /**
     * 将查询到的ScheduleMessage(PerfMonitor)列表封装为设备entityId与entity对应的ScheduleMessage的List
     * 
     * @param scheduleList
     * @return
     */
    private Map<Long, List<ScheduleMessage<OperClass>>> encodeScheduleToEntity(
            List<ScheduleMessage<OperClass>> scheduleList) {
        Map<Long, List<ScheduleMessage<OperClass>>> scheduleMap = new HashMap<Long, List<ScheduleMessage<OperClass>>>();
        List<ScheduleMessage<OperClass>> entityScheduleList = null;
        for (ScheduleMessage<OperClass> schedule : scheduleList) {
            Long entityId = schedule.getIdentifyKey();
            entityScheduleList = scheduleMap.get(entityId);
            // 如果不存在当前设备的List,则创建新的List放入Map中
            if (entityScheduleList == null) {
                entityScheduleList = new ArrayList<ScheduleMessage<OperClass>>();
                scheduleMap.put(entityId, entityScheduleList);
            }
            entityScheduleList.add(schedule);
        }
        return scheduleMap;
    }

    /**
     * 将当前改变的性能指标数据与原来的进行对比,得到需要进行采集任务更新的指标 同时对原有的配置时应用最新的修改,在更新数据库时使用 在应用到所有同类型的设备时使用
     * 
     * @param newTargetlist
     * @param oldTargetlist
     * @return
     */
    private List<PerfTaskUpdateInfo> getNeedUpdateTaskList(List<DevicePerfTarget> newTargetlist,
            List<DevicePerfTarget> oldTargetlist) {
        // 先对比当前配置和原有的配置，获得需要进行修改的指标
        List<DevicePerfTarget> unChangeList = new ArrayList<DevicePerfTarget>(newTargetlist);
        // 得到新旧性能指标List中没有变化的部分
        unChangeList.retainAll(oldTargetlist);
        // 从新的List中删除没有变化的部分
        newTargetlist.removeAll(unChangeList);
        // 从原来的List中删除没有变化的部分
        oldTargetlist.removeAll(unChangeList);
        // 对需要进行修改的指标进行处理和封装
        List<PerfTaskUpdateInfo> updateTaskList = new ArrayList<PerfTaskUpdateInfo>();
        PerfTaskUpdateInfo updateInfo = null;
        for (DevicePerfTarget newTarget : newTargetlist) {
            String targetName = newTarget.getPerfTargetName();
            for (DevicePerfTarget oldTarget : oldTargetlist) {
                if (oldTarget.getPerfTargetName().equals(targetName)) {
                    Long entityId = oldTarget.getEntityId();
                    Long entityType = oldTarget.getEntityType();
                    Object data = performanceService.getModifyTargetDataByType(entityId, targetName, entityType);
                    // category 根据指标的名称得到perf那个名字
                    String category = perfTargetService.getPerfTargetCategory(targetName, entityType);
                    SnmpParam snmpParam = null;
                    Long typeId = oldTarget.getTypeId();
                    if (entityTypeService.isCcmtsWithoutAgent(typeId) || entityTypeService.isOnu(typeId)) {
                        // 如果是CC8800A或者C-A以及ONU,处理方式不一样
                        snmpParam = entityService.getSnmpParamByEntity(entityService.getEntity(entityId).getParentId());
                    } else {
                        snmpParam = entityService.getSnmpParamByEntity(entityId);
                    }
                    updateInfo = new PerfTaskUpdateInfo(category, targetName, entityId, oldTarget.getCollectInterval(),
                            oldTarget.getTargetEnable(), newTarget.getCollectInterval(), newTarget.getTargetEnable(),
                            data, snmpParam, typeId);
                    updateTaskList.add(updateInfo);
                    // 对原有的Target配置进行修改,在更新数据库配置时使用
                    oldTarget.setCollectInterval(newTarget.getCollectInterval());
                    oldTarget.setTargetEnable(newTarget.getTargetEnable());
                }
            }
        }
        return updateTaskList;
    }

    /**
     * 获得单个指标需要更新的采集任务信息列表
     * 
     * @param newTarget
     * @param oldTarget
     * @return
     */
    private List<PerfTaskUpdateInfo> getSingleTargetUpdateTaskList(DevicePerfTarget newTarget,
            DevicePerfTarget oldTarget) {
        List<PerfTaskUpdateInfo> updateTaskList = new ArrayList<PerfTaskUpdateInfo>();
        // 不需要修改的不做处理
        if (!oldTarget.equals(newTarget)) {
            String targetName = newTarget.getPerfTargetName();
            Long entityId = oldTarget.getEntityId();
            Object data = performanceService.getModifyTargetDataByType(entityId, targetName, oldTarget.getEntityType());
            // category 根据指标的名称得到perf那个名字
            String category = perfTargetService.getPerfTargetCategory(targetName, oldTarget.getEntityType());
            SnmpParam snmpParam = null;
            if (entityTypeService.isCcmtsWithoutAgent(oldTarget.getTypeId())
                    || entityTypeService.isOnu(oldTarget.getTypeId())) {
                // 如果是CC8800A或者C-A以及ONU,处理方式不一样
                snmpParam = entityService.getSnmpParamByEntity(entityService.getEntity(entityId).getParentId());
            } else {
                snmpParam = entityService.getSnmpParamByEntity(entityId);
            }
            PerfTaskUpdateInfo updateInfo = new PerfTaskUpdateInfo(category, targetName, entityId,
                    oldTarget.getCollectInterval(), oldTarget.getTargetEnable(), newTarget.getCollectInterval(),
                    newTarget.getTargetEnable(), data, snmpParam, oldTarget.getTypeId());
            updateTaskList.add(updateInfo);
        }
        return updateTaskList;
    }

    @Override
    public DevicePerfTarget getTargetByTypeIdAndName(Long typeId, String targetName) {
        return devicePerfTargetDao.queryTargetByTypeIdAndName(typeId, targetName);
    }

    @Override
    public boolean isTargetDataExists(Long entityId, String perfTargetName) {
        int targetCount = devicePerfTargetDao.querySpecialTargetCount(entityId, perfTargetName);
        return targetCount > 0;
    }

    @Override
    public boolean isPerfTargetDisabled(Long entityId, String perfTargetName) {
        return devicePerfTargetDao.isPerfTargetDisabled(entityId, perfTargetName);
    }

    public Integer getBatchPoolSize() {
        return batchPoolSize;
    }

    public void setBatchPoolSize(Integer batchPoolSize) {
        this.batchPoolSize = batchPoolSize;
    }

}
