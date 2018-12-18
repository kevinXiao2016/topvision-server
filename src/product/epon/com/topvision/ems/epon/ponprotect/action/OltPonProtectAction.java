/***********************************************************************
 * $Id: OltPonProtectAction.java,v1.0 2013-10-25 下午3:31:42 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.ponprotect.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import net.sf.json.JSONObject;

import org.apache.struts2.ServletActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.topvision.ems.epon.domain.OltPonProtect;
import com.topvision.ems.epon.olt.action.AbstractEponAction;
import com.topvision.ems.epon.ponprotect.domain.OltPonProtectConfig;
import com.topvision.ems.epon.ponprotect.service.OltPonProtectService;
import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.network.service.EntityService;
import com.topvision.framework.annotation.OperationLogProperty;
import com.topvision.framework.utils.EponConstants;
import com.topvision.framework.utils.EponIndex;
import com.topvision.platform.domain.UserContext;
import com.topvision.platform.domain.UserPreferences;
import com.topvision.platform.service.SystemPreferencesService;
import com.topvision.platform.service.UserPreferencesService;

/**
 * @author flack
 * @created @2013-10-25-下午3:31:42
 *
 */
@Controller("oltPonProtectAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class OltPonProtectAction extends AbstractEponAction {
    private static final long serialVersionUID = 8055999561378528410L;
    private final Logger logger = LoggerFactory.getLogger(OltPonProtectAction.class);
    @Autowired
    private OltPonProtectService ponProtectServie;
    @Autowired
    private EntityService entityService;
    @Autowired
    private UserPreferencesService userPreferencesService;
    @Autowired
    private OltPonProtectService oltPonProtectService;
    @Autowired
    private SystemPreferencesService systemPreferencesService;
    private String cameraSwitch;
    private Integer ppgId;
    private Long masterIndex;
    private Long slaveIndex;
    private String alias;
    private Entity entity;
    private String oltSoftVersion;
    private JSONObject layout;
    private OltPonProtect oltPonProtect;
    private Integer protectId;
    private Long ponIdMaster;
    private Long ponIdReserve;
    private String protectName;

    /**
     * 加载没有被使用过的PPGID
     * @return
     * @throws IOException
     */
    public String loadPPGAvialList() throws IOException {
        List<Integer> PPGList = ponProtectServie.loadPPGArray(entityId);
        List<JSONObject> list = new ArrayList<JSONObject>();
        for (int i = 1; i < 33; i++) {
            if (!PPGList.contains(i)) {
                JSONObject o = new JSONObject();
                o.put("id", i);
                list.add(o);
            }
        }
        JSONObject json = new JSONObject();
        json.put("data", list);
        json.write(response.getWriter());
        return NONE;
    }

    /**
     * 加载所有可用的主端口
     * @return
     * @throws IOException
     */
    public String loadPPGAvialMasterPort() throws IOException {
        List<JSONObject> list = new ArrayList<JSONObject>();
        Map<String, String> map = new HashMap<String, String>();
        map.put("entityId", entityId.toString());
        if (slaveIndex != null) {
            map.put("selectedPonIndex", slaveIndex.toString());
        }
        List<Long> PPGList = ponProtectServie.loadAvialPorts(map);
        if (masterIndex != null) {
            long sameChipPonIndex = EponIndex.getChipPortIndex(slaveIndex);
            PPGList.remove(sameChipPonIndex);
        }
        while (PPGList.size() > 0) {
            long ponIndex = PPGList.remove(0);
            JSONObject o = new JSONObject();
            o.put("port", parseIndex(ponIndex));
            o.put("index", ponIndex);
            list.add(o);
        }
        JSONObject json = new JSONObject();
        json.put("data", list);
        json.write(response.getWriter());
        return NONE;
    }

    /**
     * 加载所有可用的备端口
     * @return
     * @throws IOException
     */
    public String loadPPGAvialSlavePort() throws IOException {
        List<JSONObject> list = new ArrayList<JSONObject>();
        Map<String, String> map = new HashMap<String, String>();
        map.put("entityId", entityId.toString());
        if (masterIndex != null) {
            map.put("selectedPonIndex", masterIndex.toString());
        }
        List<Long> PPGList = ponProtectServie.loadAvialPorts(map);
        if (masterIndex != null) {
            long sameChipPonIndex = EponIndex.getChipPortIndex(masterIndex);
            PPGList.remove(sameChipPonIndex);
        }
        while (PPGList.size() > 0) {
            long ponIndex = PPGList.remove(0);
            JSONObject o = new JSONObject();
            o.put("port", parseIndex(ponIndex));
            o.put("index", ponIndex);
            list.add(o);
        }
        JSONObject json = new JSONObject();
        json.put("data", list);
        json.write(response.getWriter());
        return NONE;
    }

    /**
     * 跳转到添加保护组界面
     * @return
     */
    public String createPPG() {
        return SUCCESS;
    }

    /**
     * 添加保护组:不仅包含组编号，还包含主端口，备端口
     * @return
     */
    public String addPPG() {
        OltPonProtectConfig config = new OltPonProtectConfig();
        config.setEntityId(entityId);
        config.setTopPonPSGrpIndex(ppgId);
        config.setTopPonPSWorkPortIndex(masterIndex);
        config.setTopPonPSStandbyPortIndex(slaveIndex);
        //config.setAlias(alias);
        ponProtectServie.addPonProtect(config);
        return NONE;
    }

    /**
     * 添加保护组成员:主备端口一起添加
     * @return
     */
    public String addPPGPorts() {
        return NONE;
    }

    /**
     * 加载某设备的保护组
     * @return
     * @throws IOException
     */
    public String loadPPGList() throws IOException {
        List<OltPonProtectConfig> list = ponProtectServie.loadPPGList(entityId);
        JSONObject json = new JSONObject();
        json.put("data", list);
        json.write(response.getWriter());
        return NONE;
    }

    /**
     * 组使能
     * @return
     * @throws IOException
     */
    public String enablePPG() throws IOException {
        OltPonProtectConfig config = new OltPonProtectConfig();
        config.setTopPonPSGrpIndex(ppgId);
        // 只在创建和删除时需要带端口，其他设置的时候都不带端口
        // config.setTopPonPSWorkPortIndex(masterIndex);
        // config.setTopPonPSStandbyPortIndex(slaveIndex);
        config.setTopPonPsGrpAdmin(EponConstants.ADMIN_STATUS_ENABLE);
        config = ponProtectServie.setPonProtectAdmin(config, entityId);
        JSONObject json = new JSONObject();
        json.put("data", config);
        json.write(response.getWriter());
        return NONE;
    }

    /**
     * 去使能
     * @return
     * @throws IOException
     */
    public String disablePPG() throws IOException {
        OltPonProtectConfig config = new OltPonProtectConfig();
        config.setTopPonPSGrpIndex(ppgId);
        // 只在创建和删除时需要带端口，其他设置的时候都不带端口
        // config.setTopPonPSWorkPortIndex(masterIndex);
        // config.setTopPonPSStandbyPortIndex(slaveIndex);
        config.setTopPonPsGrpAdmin(EponConstants.ADMIN_STATUS_DISABLE);
        config = ponProtectServie.setPonProtectAdmin(config, entityId);
        JSONObject json = new JSONObject();
        json.put("data", config);
        json.write(response.getWriter());
        return NONE;
    }

    /**
     * 删除组
     * @return
     */
    public String deletePPG() {
        OltPonProtectConfig config = new OltPonProtectConfig();
        config.setTopPonPSGrpIndex(ppgId);
        config.setTopPonPSWorkPortIndex(masterIndex);
        config.setTopPonPSStandbyPortIndex(slaveIndex);
        ponProtectServie.deletePPG(config, entityId);
        return NONE;
    }

    /**
     * 保护组手动倒换
     * @return
     * @throws IOException
     */
    public String manuSwitch() throws IOException {
        OltPonProtectConfig config = new OltPonProtectConfig();
        config.setTopPonPSGrpIndex(ppgId);
        config.setTopPonPsManualSwitch(EponConstants.SWITCH_OVER);
        config = ponProtectServie.manuSwitch(config, entityId);
        JSONObject json = new JSONObject();
        json.put("data", config);
        json.write(response.getWriter());
        return NONE;
    }

    /**
     * OLT PON保护视图
     * 
     * @return String
     */
    public String showOltPonProtectView() {
        Properties properties = systemPreferencesService.getModulePreferences("camera");
        cameraSwitch = properties.getProperty("camera.switch");
        entity = entityService.getEntity(entityId);
        oltSoftVersion = oltService.getOltSoftVersion(entityId);
        //加载用户视图信息
        layout = this.getUserView();
        return SUCCESS;
    }

    /**
     * 获取用户视图信息
     * @return
     */
    private JSONObject getUserView() {
        JSONObject view = new JSONObject();
        UserContext uc = (UserContext) ServletActionContext.getRequest().getSession().getAttribute("UserContext");
        UserPreferences up = new UserPreferences();
        up.setModule("oltFaceplateView");
        up.setUserId(uc.getUserId());
        Properties oltView = userPreferencesService.getModulePreferences(up);
        Map<String, Object> viewMap = new HashMap<String, Object>();
        viewMap.put("rightTopHeight", oltView.get("rightTopHeight"));
        viewMap.put("middleBottomHeight", oltView.get("middleBottomHeight"));
        viewMap.put("leftWidth", oltView.get("leftWidth"));
        viewMap.put("rightWidth", oltView.get("rightWidth"));
        viewMap.put("rightTopOpen", oltView.get("rightTopOpen"));
        viewMap.put("rightBottomOpen", oltView.get("rightBottomOpen"));
        viewMap.put("middleBottomOpen", oltView.get("middleBottomOpen"));
        viewMap.put("layoutToMiddle", oltView.get("layoutToMiddle"));
        viewMap.put("tabBtnSelectedIndex", oltView.get("tabBtnSelectedIndex"));
        view = JSONObject.fromObject(viewMap);
        return view;
    }

    /**
     * OLT保护组列表
     * 
     * @return String
     * @throws java.io.IOException
     */
    public String loadOltPonProtectList() throws IOException {
        Map<String, Object> ponProtectsJson = new HashMap<String, Object>();
        List<OltPonProtect> ponProtectsList = oltPonProtectService.getOltPonProtectsList(entityId);
        ponProtectsJson.put("data", ponProtectsList);
        writeDataToAjax(JSONObject.fromObject(ponProtectsJson));
        return NONE;
    }

    /**
     * OLT保护组对象
     * 
     * @return String
     * @throws IOException
     */
    public String getOltPonProtectById() throws IOException {
        oltPonProtect = oltPonProtectService.getOltPonProtectById(entityId, protectId);
        writeDataToAjax(JSONObject.fromObject(oltPonProtect));
        return NONE;
    }

    /**
     * 跳转新增保护组界面
     * 
     * @return String
     */
    public String showAddPonProtectJsp() {
        entity = entityService.getEntity(entityId);
        return SUCCESS;
    }

    /**
     * 新增保护组对象
     * 
     * @return String
     * @throws Exception
     */
    @OperationLogProperty(actionName = "oltPonProtectAction", operationName = "addOltPonProtect")
    public String addOltPonProtect() throws Exception {
        String message;
        OltPonProtect oltPonProtect = new OltPonProtect();
        oltPonProtect.setEntityId(entityId);
        oltPonProtect.setProtectId(protectId);
        oltPonProtect.setPonIdMaster(ponIdMaster);
        oltPonProtect.setPonIdReserve(ponIdReserve);
        oltPonProtect.setProtectName(protectName);
        try {
            oltPonProtectService.addPonProtect(oltPonProtect);
            message = "success";
        } catch (Exception e) {
            message = e.getMessage();
            logger.debug("save OltPonProtect error:{}", message);
        }
        writeDataToAjax(message);
        return NONE;
    }

    /**
     * 主备倒换
     * 
     * @return String
     * @throws Exception
     */
    @OperationLogProperty(actionName = "oltPonProtectAction", operationName = "updateOltPonProtect")
    public String updateOltPonProtect() throws Exception {
        String message;
        try {
            OltPonProtect oltPonProtect = new OltPonProtect();
            oltPonProtect = oltPonProtectService.getOltPonProtectById(entityId, protectId);
            Long temp = oltPonProtect.getPonIdMaster();
            oltPonProtect.setPonIdMaster(oltPonProtect.getPonIdReserve());
            oltPonProtect.setPonIdReserve(temp);
            oltPonProtectService.updateOltPonProtectById(oltPonProtect);
            message = "success";
        } catch (Exception e) {
            message = e.getMessage();
            logger.debug("update OltPonProtect error:{}", message);
        }
        writeDataToAjax(message);
        return NONE;
    }

    /**
     * 跳转修改保护组名称界面
     * 
     * @return String
     */
    public String setPonProtectName() {
        entity = entityService.getEntity(entityId);
        return SUCCESS;
    }

    /**
     * 修改保护组名称
     * 
     * @return String
     * @throws Exception
     */
    @OperationLogProperty(actionName = "oltPonProtectAction", operationName = "modifyPonProtectName")
    public String modifyPonProtectName() throws Exception {
        String message;
        try {
            OltPonProtect oltPonProtect = new OltPonProtect();
            oltPonProtect = oltPonProtectService.getOltPonProtectById(entityId, protectId);
            oltPonProtect.setProtectName(protectName);
            oltPonProtectService.updateOltPonProtectById(oltPonProtect);
            message = "success";
        } catch (Exception e) {
            message = e.getMessage();
            logger.debug("modifyPonProtectName error:{}", e);
        }
        writeDataToAjax(message);
        return NONE;
    }

    /**
     * 删除保护组对象
     * 
     * @return String
     * @throws Exception
     */
    @OperationLogProperty(actionName = "oltPonProtectAction", operationName = "deleteOltPonProtect")
    public String deleteOltPonProtect() throws Exception {
        String message;
        try {
            oltPonProtectService.deleteOltPonProtectById(entityId, protectId);
            message = "success";
        } catch (Exception e) {
            message = e.getMessage();
            logger.debug("delete OltPonProtect error:{}", e);
        }
        writeDataToAjax(message);
        return NONE;
    }

    /**
    * 解析端口index格式 为 slot/pon的形式
    * @param index
    * @return
    */
    private String parseIndex(long index) {
        return EponIndex.getSlotNo(index) + "/" + EponIndex.getPonNo(index);
    }

    public Integer getPpgId() {
        return ppgId;
    }

    public void setPpgId(Integer ppgId) {
        this.ppgId = ppgId;
    }

    public Long getMasterIndex() {
        return masterIndex;
    }

    public void setMasterIndex(Long masterIndex) {
        this.masterIndex = masterIndex;
    }

    public Long getSlaveIndex() {
        return slaveIndex;
    }

    public void setSlaveIndex(Long slaveIndex) {
        this.slaveIndex = slaveIndex;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public Entity getEntity() {
        return entity;
    }

    public void setEntity(Entity entity) {
        this.entity = entity;
    }

    public String getOltSoftVersion() {
        return oltSoftVersion;
    }

    public void setOltSoftVersion(String oltSoftVersion) {
        this.oltSoftVersion = oltSoftVersion;
    }

    public JSONObject getLayout() {
        return layout;
    }

    public void setLayout(JSONObject layout) {
        this.layout = layout;
    }

    public OltPonProtect getOltPonProtect() {
        return oltPonProtect;
    }

    public void setOltPonProtect(OltPonProtect oltPonProtect) {
        this.oltPonProtect = oltPonProtect;
    }

    public Integer getProtectId() {
        return protectId;
    }

    public void setProtectId(Integer protectId) {
        this.protectId = protectId;
    }

    public Long getPonIdMaster() {
        return ponIdMaster;
    }

    public void setPonIdMaster(Long ponIdMaster) {
        this.ponIdMaster = ponIdMaster;
    }

    public Long getPonIdReserve() {
        return ponIdReserve;
    }

    public void setPonIdReserve(Long ponIdReserve) {
        this.ponIdReserve = ponIdReserve;
    }

    public String getProtectName() {
        return protectName;
    }

    public void setProtectName(String protectName) {
        this.protectName = protectName;
    }

    public String getCameraSwitch() {
        return cameraSwitch;
    }

    public void setCameraSwitch(String cameraSwitch) {
        this.cameraSwitch = cameraSwitch;
    }

}
