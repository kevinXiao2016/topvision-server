/***********************************************************************
 * $Id: CmcPerfTargetManageAction.java,v1.0 2014-3-14 下午3:23:05 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.perf.action;

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

import com.topvision.ems.cmc.perf.service.CmcPerfService;
import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.network.service.EntityService;
import com.topvision.ems.performance.action.PerfTargetManageAction;
import com.topvision.ems.performance.domain.DevicePerfTarget;
import com.topvision.ems.template.service.EntityTypeService;

/**
 * @author flack
 * @created @2014-3-14-下午3:23:05
 *
 */
@Controller("cmcPerfTargetManageAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class CmcPerfTargetManageAction extends PerfTargetManageAction {

    private static final long serialVersionUID = 2472229360146406060L;

    @Resource(name = "entityTypeService")
    private EntityTypeService entityTypeService;
    @Autowired
    private EntityService entityService;
    @Autowired
    private CmcPerfService cmcPerfService;

    /**
     * 加载Cmc性能指标采集管理页面
     * @return
     */
    public String showCmcPerfManage() {
        return SUCCESS;
    }

    /**
     * 加载Cmc设备性能指标数据
     * @return
     * @throws IOException 
     */
    public String loadCmcTargetList() throws IOException {
        Map<String, Object> paramsMap = new HashMap<String, Object>();
        paramsMap.put("entityType", entityTypeService.getCcmtsandcmtsType());
        paramsMap.put("parentType", entityTypeService.getCcmtsandcmtsType());
        paramsMap.put("start", start);
        paramsMap.put("limit", limit);
        if (deviceName != null && !"".equals(deviceName)) {
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
        if (entityId != null) {
            paramsMap.put("entityId", entityId);
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
    public String loadCmcPerfTarget() throws IOException {
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
    public String modifyCmcPerfTarget() throws IOException {
        //将前台传回来的数据封装为对象列表
        List<DevicePerfTarget> targetList = decodeDataToTargetList(targetData, entityId, null,
                entityTypeService.getCcmtsandcmtsType(), null);
        devicePerfTargetService.modifyDevicePerfTarget(targetList, entityId);
        return NONE;
    }

    /**
     * 将当前性能指标配置保存为全局配置
     * @return
     * @throws IOException 
     */
    public String saveAsCmcGlobalTarget() throws IOException {
        //将前台传回来的数据封装为对象列表
        List<DevicePerfTarget> currentTargetList = decodeDataToTargetList(targetData, null,
                entityTypeService.getCcmtsandcmtsType(), entityTypeService.getCcmtsandcmtsType(), null);
        //使用当前的性能指标数据去更新全局性能指标数据
        devicePerfTargetService.modifyGlobalPerfTarget(currentTargetList);
        return NONE;
    }

    /**
     * 将当前配置的性能指标数据应用到所有的Cmc
     * @return
     * @throws IOException 
     */
    public String applyCurrentTargetToAllCmc() throws IOException {
        //将前台传回来的数据封装为对象列表
        List<DevicePerfTarget> targetList = decodeDataToTargetList(targetData, entityId, entityType,
                entityTypeService.getCcmtsandcmtsType(), typeId);
        //将当前指标应用到所有的Cmc设备
        Map<String, Object> resultMap = devicePerfTargetService.applyTargetToAllDevice(targetList, entityType, typeId,
                saveGlobalFlag);
        JSONObject result = JSONObject.fromObject(resultMap);
        writeDataToAjax(result);
        return NONE;
    }

    /**
     * 显示Cmc全局性能指标配置页面
     * @return
     */
    public String showCmcGlobalPerfTarget() {
        //查询Cmc全局性能指标
        ccmtsTargetJson = this.getGlobalTargetByType(entityTypeService.getCcmtsType());
        //加载是否关联默认阀值模板和是否开启性能告警
        loadBindTempAndPerfAlert(entityTypeService.getCcmtsandcmtsType());
        return SUCCESS;
    }

    /**
     * 更改全局性能指标配置
     * @return
     * @throws IOException 
     */
    public String modifyCmcGlobalTarget() throws IOException {
        Long ccmtsandcmtsType = entityTypeService.getCcmtsandcmtsType();
        //将前台传回来的数据封装为对象列表
        List<DevicePerfTarget> globalTargetList = this.decodeDataToTargetList(targetData, null, ccmtsandcmtsType,
                ccmtsandcmtsType, null);
        //修改是否绑定默认阀值模板和是否开启性能告警
        udpateBindTempAndPerfAlert(ccmtsandcmtsType, isBindDftTemp, isPerfThdOn);
        //使用当前的性能指标数据去更新全局性能指标数据
        devicePerfTargetService.modifyGlobalPerfTarget(globalTargetList);
        //获取指定设备的全局性能指标
        JSONObject globalData = this.getGlobalTargetByType(entityTypeService.getCcmtsType());
        writeDataToAjax(globalData);
        return NONE;
    }

    /**
     * 开启CMC性能采集
     * @return
     */
    public String saveCmcPerfCollect() {
        List<Entity> cmcList = entityService.getEntityListByType(entityTypeService.getCcmtsType());
        for (Entity entity : cmcList) {
            cmcPerfService.startCmcPerfCollect(entity);
        }
        return NONE;
    }

    /**
     * 使用全局配置
     * @return
     */
    public String useCmcGolbalPerf() {
        Entity entity = entityService.getEntity(entityId);
        cmcPerfService.startCmcPerfCollect(entity);
        return NONE;
    }
}
