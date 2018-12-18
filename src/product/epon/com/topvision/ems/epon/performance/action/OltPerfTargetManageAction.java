/***********************************************************************
 * $Id: OltPerfTargetManageAction.java,v1.0 2014-3-12 下午7:53:35 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.performance.action;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.topvision.ems.epon.performance.service.EponStatsService;
import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.network.service.EntityService;
import com.topvision.ems.performance.action.PerfTargetManageAction;
import com.topvision.ems.performance.domain.DevicePerfTarget;
import com.topvision.ems.template.service.EntityTypeService;

/**
 * @author flack
 * @created @2014-3-12-下午7:53:35
 *
 */
@Controller("oltPerfTargetManageAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class OltPerfTargetManageAction extends PerfTargetManageAction {
    private static final long serialVersionUID = 1553239228337321054L;

    @Resource(name = "entityTypeService")
    private EntityTypeService entityTypeService;
    @Autowired
    private EntityService entityService;
    @Autowired
    EponStatsService eponStatsService;

    /**
     * 加载Olt性能指标采集管理页面
     * @return
     */
    public String showOltPerfManage() {
        return SUCCESS;
    }

    /**
     * 加载olt设备性能指标数据
     * @return
     * @throws IOException 
     */
    public String loadOltPerfTargetList() throws IOException {
        Map<String, Object> paramsMap = new HashMap<String, Object>();
        paramsMap.put("entityType", entityTypeService.getOltType());
        paramsMap.put("parentType", entityTypeService.getOltType());
        paramsMap.put("start", start);
        paramsMap.put("limit", limit);
        if (deviceName != null && !"".equals(deviceName)) {
            //mysql中下划线是特殊的，like的时候必须转义
            if (deviceName.contains("_")) {
                deviceName = deviceName.replace("_", "\\_");
            }
            paramsMap.put("deviceName", deviceName);
        }
        if (deviceIp != null && !"".equals(deviceIp)) {
            paramsMap.put("manageIp", deviceIp);
        }
        if (folderId != null && folderId != -1) {
            paramsMap.put("folderId", folderId);
        }
        if (typeId != null && typeId != -1) {
            paramsMap.put("typeId", typeId);
        }
        //paramsMap.put("perfTargetName", perfTargetName);
        //paramsMap.put("collectInterval", collectInterval);
        List<DevicePerfTarget> deviceTargetList = devicePerfTargetService.getDevicePerfList(paramsMap);
        JSONArray targetArray = encodeAllTargetToDevice(deviceTargetList);
        int totalCount = devicePerfTargetService.getDeviceNum(paramsMap);
        JSONObject deviceJson = new JSONObject();
        deviceJson.put("totalCount", totalCount);
        deviceJson.put("data", targetArray);
        deviceJson.write(response.getWriter());
        return NONE;
    }

    /**
     * 显示指定设备的性能指标数据
     * @return
     * @throws IOException 
     */
    public String loadOltPerfTarget() throws IOException {
        //获取指定设备的性能指标及对应的全局性能指标
        JSONObject targetData = getDeviceTargetById(entityId);
        writeDataToAjax(targetData);
        return NONE;
    }

    /**
     * 修改设备的性能指标数据
     * @return
     * @throws IOException 
     */
    public String modifyOltPerfTarget() throws IOException {
        //将前台传回来的数据封装为对象列表
        List<DevicePerfTarget> targetList = decodeDataToTargetList(targetData, entityId, null,
                entityTypeService.getOltType(), null);
        devicePerfTargetService.modifyDevicePerfTarget(targetList, entityId);
        return NONE;
    }

    /**
     * 将当前性能指标配置保存为全局配置
     * @return
     * @throws IOException 
     */
    public String saveAsOltGlobalTarget() throws IOException {
        //将前台传回来的数据封装为对象列表
        List<DevicePerfTarget> currentTargetList = decodeDataToTargetList(targetData, null, entityType,
                entityTypeService.getOltType(), null);
        //使用当前的性能指标数据去更新全局性能指标数据
        devicePerfTargetService.modifyGlobalPerfTarget(currentTargetList);
        return NONE;
    }

    /**
     * 将当前配置的性能指标数据应用到所有的Olt
     * @return
     * @throws IOException 
     */
    public String applyCurrentTargetToAllOlt() throws IOException {
        //将前台传回来的数据封装为对象列表
        List<DevicePerfTarget> targetList = decodeDataToTargetList(targetData, entityId, null, entityType, null);
        //将当前指标应用到所有的OLt设备,Olt不用区分具体的设备类型
        Map<String, Object> resultMap = devicePerfTargetService.applyTargetToAllDevice(targetList, entityType, null,
                saveGlobalFlag);
        JSONObject result = JSONObject.fromObject(resultMap);
        writeDataToAjax(result);
        return NONE;
    }

    /**
     * 显示olt全局性能指标配置页面
     * @return
     */
    public String showOltGlobalPerfTarget() {
        //查询olt全局性能指标
        oltTargetJson = this.getGlobalTargetByType(entityTypeService.getOltType());
        //加载是否关联默认阀值模板和是否开启性能告警
        loadBindTempAndPerfAlert(entityTypeService.getOltType());
        return SUCCESS;
    }

    /**
     * 更改全局性能指标配置
     * @return
     * @throws IOException 
     */
    public String modifyOltGlobalTarget() throws IOException {
        Long typeId = entityTypeService.getOltType();
        //将前台传回来的数据封装为对象列表
        List<DevicePerfTarget> globalTargetList = this.decodeDataToTargetList(targetData, null, typeId, typeId, null);
        //修改是否绑定默认阀值模板和是否开启性能告警
        udpateBindTempAndPerfAlert(typeId, isBindDftTemp, isPerfThdOn);
        //使用当前的性能指标数据去更新全局性能指标数据
        devicePerfTargetService.modifyGlobalPerfTarget(globalTargetList);
        //获取指定设备的全局性能指标
        JSONObject globalData = this.getGlobalTargetByType(typeId);
        writeDataToAjax(globalData);
        return NONE;
    }

    /**
     * 开启Olt性能采集
     * @return
     */
    public String saveOltPerfCollect() {
        List<Entity> oltList = entityService.getEntityListByType(entityTypeService.getOltType());
        for (Entity entity : oltList) {
            eponStatsService.startOltPerfCollect(entity);
        }
        return NONE;
    }

    public String useOltGolbalCollect() {
        Entity entity = entityService.getEntity(entityId);
        eponStatsService.startOltPerfCollect(entity);
        return NONE;
    }

    public String getEntityIds() {
        return entityIds;
    }

    public void setEntityIds(String entityIds) {
        this.entityIds = entityIds;
    }

}
