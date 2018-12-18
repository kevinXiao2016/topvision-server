/***********************************************************************
 * $Id: CmtsPerfTargetManageAction.java,v1.0 2014-3-15 上午11:50:48 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmts.action;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.topvision.ems.cmts.service.CmtsPerfService;
import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.network.service.EntityService;
import com.topvision.ems.performance.action.PerfTargetManageAction;
import com.topvision.ems.performance.domain.DevicePerfTarget;
import com.topvision.ems.template.service.EntityTypeService;

/**
 * @author flack
 * @created @2014-3-15-上午11:50:48
 *
 */
@Controller("cmtsPerfTargetManageAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class CmtsPerfTargetManageAction extends PerfTargetManageAction {
    private static final long serialVersionUID = -1057786508761933811L;

    @Autowired
    private EntityTypeService entityTypeService;
    @Autowired
    private EntityService entityService;
    @Autowired
    private CmtsPerfService cmtsPerfService;

    /**
     * 加载Cmts性能指标采集管理页面
     * @return
     */
    public String showCmtsPerfManage() {
        return SUCCESS;
    }

    /**
     * 加载Cmts设备性能指标数据
     * @return
     * @throws IOException 
     */
    public String loadCmtsTargetList() throws IOException {
        Map<String, Object> paramsMap = new HashMap<String, Object>();
        paramsMap.put("entityType", entityTypeService.getCmtsType());
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
    public String loadCmtsPerfTarget() throws IOException {
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
    public String modifyCmtsPerfTarget() throws IOException {
        //将前台传回来的数据封装为对象列表
        List<DevicePerfTarget> targetList = decodeDataToTargetList(targetData, entityId, null,
                entityTypeService.getCmtsType(), null);
        devicePerfTargetService.modifyDevicePerfTarget(targetList, entityId);
        //获取指定设备的性能指标及对应的全局性能指标
        JSONObject targetData = getDeviceTargetById(entityId);
        writeDataToAjax(targetData);
        return NONE;
    }

    /**
     * 将当前性能指标配置保存为全局配置
     * @return
     * @throws IOException 
     */
    public String saveAsCmtsGlobalTarget() throws IOException {
        //将前台传回来的数据封装为对象列表
        List<DevicePerfTarget> currentTargetList = decodeDataToTargetList(targetData, null, entityType,
                entityTypeService.getCmtsType(), null);
        //使用当前的性能指标数据去更新全局性能指标数据
        devicePerfTargetService.modifyGlobalPerfTarget(currentTargetList);
        //获取指定设备的性能指标及对应的全局性能指标
        JSONObject targetData = getDeviceTargetById(entityId);
        writeDataToAjax(targetData);
        return NONE;
    }

    /**
     * 将当前配置的性能指标数据应用到所有的Cmc
     * @return
     * @throws IOException 
     */
    public String applyCurrentTargetToAllCmts() throws IOException {
        //将前台传回来的数据封装为对象列表
        List<DevicePerfTarget> targetList = decodeDataToTargetList(targetData, entityId, entityType,
                entityTypeService.getCmtsType(), typeId);
        //将当前指标应用到所有的Cmc设备,cmts现在不用区分类型
        Map<String, Object> resultMap = devicePerfTargetService.applyTargetToAllDevice(targetList, entityType, null,
                saveGlobalFlag);
        JSONObject result = JSONObject.fromObject(resultMap);
        writeDataToAjax(result);
        //获取指定设备的性能指标及对应的全局性能指标
        //JSONObject targetData = getDeviceTargetById(entityId);
        //write(targetData);
        return NONE;
    }

    /**
     * 显示CMTS全局性能指标配置页面
     * @return
     */
    public String showCmtsGlobalPerfTarget() {
        //查询Cmts全局性能指标
        cmtsTargetJson = this.getGlobalTargetByType(entityTypeService.getCmtsType());
        //加载是否关联默认阀值模板和是否开启性能告警
        loadBindTempAndPerfAlert(entityTypeService.getCcmtsandcmtsType());
        return SUCCESS;
    }

    /**
     * 更改全局性能指标配置
     * @return
     * @throws IOException 
     */
    public String modifyCmtsGlobalTarget() throws IOException {
        Long typeId = entityTypeService.getCmtsType();
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
     * 开启CMTS性能采集
     * @return
     */
    public String saveCmtsPerfCollect() {
        List<Entity> cmcList = entityService.getEntityListByType(entityTypeService.getCmtsType());
        for (Entity entity : cmcList) {
            cmtsPerfService.startCmtsPerfCollect(entity);
        }
        return NONE;
    }

    /**
     * 开启CMTS性能采集
     * @return
     */
    public String useCmtsGolbalPerf() {
        Entity entity = entityService.getEntity(entityId);
        cmtsPerfService.startCmtsPerfCollect(entity);
        return NONE;
    }

}
