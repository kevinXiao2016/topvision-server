/***********************************************************************
 * $Id: CameraAction.java,v1.0 2013年12月10日 上午10:10:28 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.camera.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.topvision.ems.epon.camera.domain.CameraFilterTable;
import com.topvision.ems.epon.camera.service.CameraService;
import com.topvision.ems.epon.olt.domain.OltPonAttribute;
import com.topvision.ems.epon.olt.domain.OltSlotAttribute;
import com.topvision.ems.epon.olt.service.OltSlotService;
import com.topvision.ems.epon.onu.domain.OltOnuAttribute;
import com.topvision.ems.epon.onu.service.OnuService;
import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.network.service.EntityService;
import com.topvision.framework.web.struts2.BaseAction;
import com.topvision.platform.service.SystemPreferencesService;

/**
 * @author Bravin
 * @created @2013年12月10日-上午10:10:28
 *
 */
@Controller("cameraAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class CameraAction extends BaseAction {
    private static final long serialVersionUID = 8139428622104385257L;

    private Long entityId;
    private Long onuIndex;
    private String ip;
    private String mac;
    private Integer cameraIndex;
    private Long eponIndex;
    private String queryInfo;
    private int start;
    private int limit;
    private Entity entity;
    private Long slotId;
    private Long ponId;
    private String location;
    private String noteInfo;
    private String cameraType;

    @Autowired
    private CameraService cameraService;
    @Autowired
    private EntityService entityService;
    @Autowired
    private OltSlotService oltSlotService;
    @Autowired
    private OnuService onuService;
    @Autowired
    private SystemPreferencesService systemPreferencesService;
    private String cameraSwitch;

    /**
     * 显示全局摄像头列表
     * @return
     */
    public String showGlobalCameraList() {
        return SUCCESS;
    }

    /**
     * 加载摄像头列表
     * @return
     * @throws IOException 
     */
    public String loadCameraList() throws IOException {
        Map<String, Object> params = new HashMap<String, Object>();
        if (entityId != null && entityId != -1) {
            params.put("entityId", entityId);
        }
        //简单模糊查询的条件
        if (queryInfo != null && !"".equals(queryInfo)) {
            params.put("queryInfo", queryInfo);
        }
        //高级查询的处理
        if (slotId != null && !"".equals(slotId)) {
            if (ponId != null && !"".equals(ponId)) {
                if (eponIndex != null && !"".equals(eponIndex)) {
                    params.put("eponIndex", eponIndex);
                } else {
                    //只选了板卡和pon口的情况
                    List<OltOnuAttribute> onuList = onuService.getOnuListByPonId(ponId);
                    StringBuilder indexBuilder = new StringBuilder();
                    for (int i = 0; i < onuList.size(); i++) {
                        indexBuilder.append(onuList.get(i).getOnuIndex()).append(",");
                    }
                    if (indexBuilder.length() > 0) { // 可能没有查找到onu,防止下标越界
                        params.put("eponIndex", indexBuilder.substring(0, indexBuilder.length() - 1));
                    } else { //没有找到onu的情况
                        params.put("eponIndex", "0");
                    }
                }
            } else {
                //只选了板卡的情况,先根据板卡查询所有的pon口
                List<OltPonAttribute> oltPonList = oltSlotService.getSlotPonList(slotId);
                List<OltOnuAttribute> allOnu = new ArrayList<OltOnuAttribute>();
                List<OltOnuAttribute> onuList = null;
                for (OltPonAttribute oltPon : oltPonList) {
                    onuList = onuService.getOnuListByPonId(oltPon.getPonId());
                    allOnu.addAll(onuList);
                }
                StringBuilder indexBuilder = new StringBuilder();
                for (int i = 0; i < allOnu.size(); i++) {
                    indexBuilder.append(allOnu.get(i).getOnuIndex()).append(",");
                }
                if (indexBuilder.length() > 0) { // 可能没有查找到onu,防止下标越界
                    params.put("eponIndex", indexBuilder.substring(0, indexBuilder.length() - 1));
                } else { //没有找到onu的情况
                    params.put("eponIndex", "0");
                }
            }
        }
        if (ip != null && !"".equals(ip)) {
            params.put("ip", ip);
        }
        if (mac != null && !"".equals(mac)) {
            params.put("mac", mac);
        }
        if (location != null && !"".equals(location)) {
            params.put("location", location);
        }
        if (noteInfo != null && !"".equals(noteInfo)) {
            params.put("noteInfo", noteInfo);
        }
        if (cameraType != null && !"".equals(cameraType)) {
            params.put("cameraType", cameraType);
        }
        params.put("start", start);
        params.put("limit", limit);
        List<CameraFilterTable> cameraList = cameraService.loadCameraList(params);
        int totalCount = cameraService.getCameraCount(params);
        JSONObject json = new JSONObject();
        json.put("rowCount", totalCount);
        json.put("data", cameraList);
        json.write(response.getWriter());
        return NONE;
    }

    /**
     * 显示Olt摄像头列表
     * @return
     */
    public String showOltCamereList() {
        entity = entityService.getEntity(entityId);
        Properties properties = systemPreferencesService.getModulePreferences("camera");
        cameraSwitch = properties.getProperty("camera.switch");
        return SUCCESS;
    }

    /**
     * 刷新单个olt数据
     * @return
     */
    public String refreshCameraData() {
        cameraService.refreshCameraList(entityId);
        return NONE;
    }

    public String refreshAllCamera() {
        cameraService.refreshAllCamera();
        return NONE;
    }

    /**
     * 保存摄像头配置(绑定)
     * @return
     */
    public String saveCameraConfig() {
        CameraFilterTable camera = new CameraFilterTable();
        camera.setEntityId(entityId);
        camera.setEponIndex(eponIndex);
        camera.setIp(ip);
        camera.setMac(mac);
        cameraService.addCameraFilter(camera);
        return NONE;
    }

    /**
     * 替换摄像头
     * @return
     */
    public String replaceCamera() {
        CameraFilterTable camera = new CameraFilterTable();
        camera.setEntityId(entityId);
        camera.setEponIndex(eponIndex);
        camera.setCameraIndex(cameraIndex);
        //替换时只替换MAC，不替换IP。所以此处的MAC绝对不能删，方便做MAC重复校验
        camera.setMac(mac);
        cameraService.deleteCameraConfig(camera);
        //删除camera的时候已经把IP,MAC删除掉了
        camera.setIp(ip);
        camera.setMac(mac);
        cameraService.addCameraFilter(camera);
        return NONE;
    }

    /**
     * 删除摄像头配置(解绑定)
     * @return
     */
    public String deleteCameraConfig() {
        CameraFilterTable camera = new CameraFilterTable();
        camera.setEntityId(entityId);
        camera.setEponIndex(eponIndex);
        camera.setCameraIndex(cameraIndex);
        cameraService.deleteCameraConfig(camera);
        return NONE;
    }

    /**
     * 获得单个olt下的所有slot列表
     * @return
     * @throws IOException 
     */
    public String getOltSlotList() throws IOException {
        List<OltSlotAttribute> oltSlotList = oltSlotService.getOltPonSlotList(entityId);
        JSONArray slotArray = JSONArray.fromObject(oltSlotList);
        slotArray.write(response.getWriter());
        return NONE;
    }

    /**
     * 获取olt业务板pon口列表
     * @return
     * @throws IOException 
     */
    public String getOltPonList() throws IOException {
        List<OltPonAttribute> oltPonList = oltSlotService.getSlotPonList(slotId);
        JSONArray ponArray = JSONArray.fromObject(oltPonList);
        ponArray.write(response.getWriter());
        return NONE;
    }

    /**
     * 获取pon口下所有的Onu
     * @return
     * @throws IOException 
     */
    public String getPonOnuList() throws IOException {
        List<OltOnuAttribute> onuList = onuService.getOnuListByPonId(ponId);
        JSONArray onuArray = JSONArray.fromObject(onuList);
        onuArray.write(response.getWriter());
        return NONE;
    }

    /**
     * 获取所有的摄像头类型
     * @return
     * @throws IOException 
     */
    public String getAllCameraType() throws IOException {
        List<String> typeList = cameraService.getAllCameraType();
        JSONArray array = new JSONArray();
        for (String type : typeList) {
            JSONObject json = new JSONObject();
            json.put("type", type);
            array.add(json);
        }
        array.write(response.getWriter());
        return NONE;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public Long getOnuIndex() {
        return onuIndex;
    }

    public void setOnuIndex(Long onuIndex) {
        this.onuIndex = onuIndex;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public Integer getCameraIndex() {
        return cameraIndex;
    }

    public void setCameraIndex(Integer cameraIndex) {
        this.cameraIndex = cameraIndex;
    }

    public Long getEponIndex() {
        return eponIndex;
    }

    public void setEponIndex(Long eponIndex) {
        this.eponIndex = eponIndex;
    }

    public String getQueryInfo() {
        return queryInfo;
    }

    public void setQueryInfo(String queryInfo) {
        this.queryInfo = queryInfo;
    }

    @Override
    public int getStart() {
        return start;
    }

    @Override
    public void setStart(int start) {
        this.start = start;
    }

    @Override
    public int getLimit() {
        return limit;
    }

    @Override
    public void setLimit(int limit) {
        this.limit = limit;
    }

    public Entity getEntity() {
        return entity;
    }

    public void setEntity(Entity entity) {
        this.entity = entity;
    }

    public Long getSlotId() {
        return slotId;
    }

    public void setSlotId(Long slotId) {
        this.slotId = slotId;
    }

    public Long getPonId() {
        return ponId;
    }

    public void setPonId(Long ponId) {
        this.ponId = ponId;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getNoteInfo() {
        return noteInfo;
    }

    public void setNoteInfo(String noteInfo) {
        this.noteInfo = noteInfo;
    }

    public String getCameraType() {
        return cameraType;
    }

    public void setCameraType(String cameraType) {
        this.cameraType = cameraType;
    }

    public String getCameraSwitch() {
        return cameraSwitch;
    }

    public void setCameraSwitch(String cameraSwitch) {
        this.cameraSwitch = cameraSwitch;
    }

}

