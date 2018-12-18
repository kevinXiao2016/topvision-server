/***********************************************************************
 * $Id: OnuAction.java,v1.0 2013-10-25 上午11:18:33 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.onu.action;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.struts2.ServletActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.topvision.ems.epon.domain.Onu;
import com.topvision.ems.epon.exception.SetValueConflictException;
import com.topvision.ems.epon.igmp.domain.IgmpMcOnuTable;
import com.topvision.ems.epon.igmp.service.OltIgmpService;
import com.topvision.ems.epon.olt.action.AbstractEponAction;
import com.topvision.ems.epon.olt.domain.OltPonAttribute;
import com.topvision.ems.epon.olt.domain.OltSlotAttribute;
import com.topvision.ems.epon.olt.service.OltSlotService;
import com.topvision.ems.epon.onu.domain.CC8800ABaseInfo;
import com.topvision.ems.epon.onu.domain.OltOnuAttribute;
import com.topvision.ems.epon.onu.domain.OltOnuCapability;
import com.topvision.ems.epon.onu.domain.OltOnuPonAttribute;
import com.topvision.ems.epon.onu.domain.OltTopOnuCapability;
import com.topvision.ems.epon.onu.domain.OltUniExtAttribute;
import com.topvision.ems.epon.onu.domain.OltUniStormSuppressionEntry;
import com.topvision.ems.epon.onu.domain.OnuBaseInfo;
import com.topvision.ems.epon.onu.domain.UniPort;
import com.topvision.ems.epon.onu.service.ElectricityOnuService;
import com.topvision.ems.epon.onu.service.OnuAssemblyService;
import com.topvision.ems.epon.onu.service.OnuService;
import com.topvision.ems.epon.onu.service.UniService;
import com.topvision.ems.epon.onu.util.OnuUtil;
import com.topvision.ems.epon.onuauth.domain.OltAuthentication;
import com.topvision.ems.epon.onuauth.service.OnuAuthService;
import com.topvision.ems.epon.utils.OnuTypeConvertor;
import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.facade.domain.EntityType;
import com.topvision.ems.gpon.onuauth.GponConstant;
import com.topvision.ems.message.Message;
import com.topvision.ems.message.MessagePusher;
import com.topvision.ems.network.service.EntityService;
import com.topvision.ems.template.service.EntityTypeService;
import com.topvision.framework.annotation.OperationLogProperty;
import com.topvision.framework.common.DateUtils;
import com.topvision.framework.common.MacUtils;
import com.topvision.framework.utils.EponConstants;
import com.topvision.framework.utils.EponIndex;
import com.topvision.platform.ResourceManager;
import com.topvision.platform.domain.OperationLog;
import com.topvision.platform.domain.UserContext;
import com.topvision.platform.domain.UserPreferences;
import com.topvision.platform.service.SystemPreferencesService;
import com.topvision.platform.service.UserPreferencesService;
import com.topvision.platform.util.CurrentRequest;
import com.topvision.platform.zetaframework.util.ZetaUtil;
import com.topvision.platform.zetaframework.var.UnitConfigConstant;

/**
 * @author flack
 * @created @2013-10-25-上午11:18:33
 *
 */
@Controller("onuAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class OnuAction extends AbstractEponAction {
    private static final long serialVersionUID = 520636647153437441L;
    private final Logger logger = LoggerFactory.getLogger(OnuAction.class);
    private String topOnuUpgradeOnuList;
    @Autowired
    private OnuService oltOnuService;
    @Autowired
    private MessagePusher messagePusher;
    @Autowired
    private EntityService entityService;
    @Autowired
    private OltIgmpService oltIgmpService;
    @Autowired
    private UserPreferencesService userPreferencesService;
    @Autowired
    private OnuService onuService;
    @Autowired
    private OnuAuthService onuAuthService;
    @Autowired
    private OltSlotService oltSlotService;
    @Autowired
    private UniService uniService;
    @Autowired
    private ElectricityOnuService electricityOnuService;
    @Autowired
    private SystemPreferencesService systemPreferencesService;
    private Long onuId;
    private Long ponId;
    private List<OltOnuAttribute> oltOnuList;
    private Long slotId;
    private Integer llid;
    private Long mac;
    private String onuName;
    private Long uniId;
    private String llidString;
    private Integer onuVoipEnable;
    private Integer onuTemperatureDetectEnable;
    private Integer onuFecEnable;
    private Integer onuIsolationEnable;
    private Integer onu15minEnable;
    private Integer onu24hEnable;
    private Integer onuCatvEnable;
    private String macAddress;
    private Integer macStatusFlag;
    private Long onuPonId;
    private Integer onuAdminStatus;
    private Integer onuMaxMacNum;
    private Long unicastStormOutPacketRate;
    private Long multicastStormOutPacketRate;
    private Long broadcastStormOutPacketRate;
    private Long folderId;
    protected Date statDate;
    private String onuPreType;
    private int start;
    private int limit;
    private OltOnuAttribute oltOnuAttribute;
    private List<OltOnuAttribute> onuList;
    private String oltSoftVersion;
    private Entity entity;
    private JSONArray ponListObject;
    private JSONArray slotPonObject;
    private JSONArray onuIgmpMode;
    private JSONObject layout;
    private JSONArray entityList;
    private OltOnuPonAttribute oltOnuPonAttribute;
    private OltTopOnuCapability oltTopOnuCapability;
    private OltOnuCapability oltOnuCapability;
    private OltUniStormSuppressionEntry uniStorm;
    private Long uniIndex;
    private String displayStyle;
    private Integer oltOnuMacAge;
    private String cameraSwitch;
    private Long onuIndex;
    private String entityIp;
    private Long cmcId;
    private Long cmcIndex;
    protected String source;
    private Integer onuDeactive;

    // 用于下级设备视图展示下级设备树
    private JSONArray subListArray;
    @Autowired
    private OnuAssemblyService onuAssemblyService;
    @Autowired
    private EntityTypeService entityTypeService;

    /**
     * onu设备列表
     * 
     * @return
     */
    public String showOnuDeviceList() {
        return SUCCESS;
    }

    /**
     * epon onu业务视图
     * 
     * @return
     */
    public String showEponBusiness() {
        return SUCCESS;
    }

    /**
     * 获取onuNo的列表
     * 
     * @return String
     * @throws Exception
     */
    public String loadOnuNoList() throws Exception {
        // 获取onuNo、blockOnuNo列表
        List<Long> onuNoList = onuAuthService.getOnuAuthIdList(ponId);
        List<Long> blockOnuNoList = onuAuthService.getBlockOnuAuthIdList(ponId);
        List<List<Long>> newList = new ArrayList<List<Long>>();
        newList.add(onuNoList);
        newList.add(blockOnuNoList);
        logger.debug("onuNoList:{}", newList);
        writeDataToAjax(JSONArray.fromObject(newList));
        return NONE;
    }

    /**
     * 页面ONU对象构造实例
     * 
     * @return String String
     * @throws java.io.IOException
     */
    public String onuObjectCreate() throws IOException {
        ResourceManager resourceManager = ResourceManager.getResourceManager("com.topvision.ems.resources.resources");
        UserContext uc = CurrentRequest.getCurrentUser();
        JSONObject json = new JSONObject();
        Onu onu = onuService.getOnuStructure(onuId);
        Integer onuPretype = Integer.parseInt(onu.getOnuPreType());
        if (onuPretype != null && entityTypeService.isOnu(onu.getTypeId())) {
            if (onuPretype > OltOnuAttribute.ONU_TYPE_UNKNOWN
                    && onu.getTypeId().intValue() == OltOnuAttribute.ONU_ENTITYTYPE_OTHERCORP) {
                onu.setTypeName(OnuTypeConvertor.convertTypeName(onuPretype));
                onu.setOnuPreType(OnuTypeConvertor.convertTypeName(onuPretype));
            } else if (GponConstant.GPON_ONU.equals(onu.getOnuEorG())) {
                onu.setOnuPreType("GPON ONU");
            } else {
                String produtionType = ZetaUtil.getStaticString("ONU." + onu.getTypeName(), "Onu");
                onu.setOnuPreType(produtionType);
                // onu.setTypeName(produtionType);
            }
        }
        if (EponConstants.ADMIN_STATUS_ENABLE != onu.getOnuOperationStatus()) {
            String onuRunTime = resourceManager.getNotNullString("deviceInfo.offline");
            if (onu.getLastDeregisterTime() != null) {
                String offlineInfo = "(" + DateUtils.getTimeDesInObscure(
                        System.currentTimeMillis() - onu.getLastDeregisterTime().getTime(), uc.getUser().getLanguage())
                        + ")";
                onuRunTime = onuRunTime + offlineInfo;
            }
            onu.setOnuRunTime(onuRunTime);
        }
        json.put("onu", onu);
        writeDataToAjax(json);
        return NONE;
    }

    /**
     * 显示ONU视图
     * 
     * @return String
     */
    public String showOnuViewJsp() {
        UserContext uc = (UserContext) super.getSession().get(UserContext.KEY);
        displayStyle = uc.getMacDisplayStyle();
        Properties properties = systemPreferencesService.getModulePreferences("camera");
        cameraSwitch = properties.getProperty("camera.switch");
        // /得到该OLT的信息
        entity = entityService.getEntity(entityId);
        oltSoftVersion = entityService.getDeviceVersion(entityId);
        // 得到PON板信息？@bravin
        List<OltSlotAttribute> oltPonList = oltSlotService.getOltPonSlotList(entityId);
        ponListObject = JSONArray.fromObject(oltPonList);
        // 得到PON口信息
        List<OltPonAttribute> oltPonAttributes = new ArrayList<OltPonAttribute>();
        for (OltSlotAttribute oltSlotAttribute : oltPonList) {
            oltPonAttributes.addAll(oltSlotService.getSlotPonList(oltSlotAttribute.getSlotId()));
        }
        slotPonObject = JSONArray.fromObject(oltPonAttributes);
        // 得到ONU列表信息
        oltOnuList = onuService.getOnuList(entityId, slotId, ponId, llid, macAddress, onuName);
        JSONObject json = OnuUtil.getOnuListTree(oltOnuList, displayStyle);
        subListArray = json.getJSONArray("tree");
        entityList = json.getJSONArray("entities");
        // 获取所有ONU的IGMP模式
        List<IgmpMcOnuTable> tempOnuIgmpList = oltIgmpService.getIgmpMcOnuInfoList(entityId);
        List<Long> tempOnuIgmpMode = new ArrayList<Long>();
        if (tempOnuIgmpList != null && tempOnuIgmpList.size() != 0) {
            for (IgmpMcOnuTable anOnuIgmp : tempOnuIgmpList) {
                tempOnuIgmpMode.add(anOnuIgmp.getOnuIndex());
                tempOnuIgmpMode.add(anOnuIgmp.getTopMcOnuMode().longValue());
            }
            onuIgmpMode = JSONArray.fromObject(tempOnuIgmpMode);
        } else {
            onuIgmpMode = JSONArray.fromObject(false);
        }
        // 加载用户视图信息
        layout = this.getUserView();
        return SUCCESS;
    }

    /**
     * 获取用户视图信息
     * 
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
     * 根据查询条件获得ONU列表
     * 
     * @return String
     * @throws IOException
     */
    public String getOltOnuList() throws IOException {
        /*
         * if (macStatusFlag == 2) { mac = new MacUtils(macAddress).longValue(); } else if
         * (macStatusFlag == 3) { mac = Long.parseLong(macAddress); } else { // mac = -1L;
         * 
         * }
         */
        if (llidString != null && !llidString.equals("")) {
            llid = Integer.parseInt(llidString, 16);
        }
        onuList = onuService.getOnuList(entityId, slotId, ponId, llid, MacUtils.formatQueryMac(macAddress), onuName);
        // initialize entityList
        entityList = new JSONArray();
        for (OltOnuAttribute o : onuList) {
            JSONObject json = new JSONObject();
            json.put("entityId", o.getOnuId());
            json.put("parentId", o.getEntityId());
            json.put("entityType", o.getOnuPreType());
            json.put("typeId", o.getTypeId());
            json.put("adminStatus", o.getOnuAdminStatus());
            json.put("entityName", o.getOnuName());
            json.put("oprationStatus", o.getOnuOperationStatus());
            entityList.add(json);
        }
        entityList.write(response.getWriter());
        return NONE;
    }

    /**
     * 获得ONU属性
     * 
     * @return String
     */
    // TODO 没有找到对应的配置文件，也没有相应的调用
    public String getOnuAttribute() {
        oltOnuAttribute = onuService.getOnuAttribute(onuId);// 获得ONU属性
        oltOnuPonAttribute = onuService.getOnuPonAttribute(onuPonId);// 获得ONU光传输属性
        return SUCCESS;
    }

    /**
     * 更新ONU基本属性
     * 
     * @return String
     */
    // TODO 没有找到对应的配置文件，也没有相应的调用
    @OperationLogProperty(actionName = "onuAction", operationName = "updateOnuAttribute")
    public String updateOnuAttribute() throws Exception {
        String message = "success";
        try {
            onuService.updateOnuAttribute(oltOnuAttribute);
            operationResult = OperationLog.SUCCESS;
        } catch (Exception e) {
            logger.debug("updateOnuAttribute error: {}", e);
            operationResult = OperationLog.FAILURE;
            message = "error[" + e.getMessage() + "]";
        }
        writeDataToAjax(message);
        return NONE;
    }

    /**
     * ONU 复位
     * 
     * @return String
     */
    @OperationLogProperty(actionName = "onuAction", operationName = "resetOnu")
    public String resetOnu() {
        try {
            onuService.resetOnu(entityId, onuId);
            operationResult = OperationLog.SUCCESS;
        } catch (Exception e) {
            logger.debug("resetOnu error: {}", e);
            operationResult = OperationLog.FAILURE;
        }
        return NONE;
    }

    /**
     * ONU 批量复位
     * 
     * @return String
     */
    @OperationLogProperty(actionName = "onuAction", operationName = "resetOnuList")
    public String resetOnuList() throws Exception {
        String result;
        String[] onuIndexs = topOnuUpgradeOnuList.split(",");
        try {
            for (String onuIndex : onuIndexs) {
                onuId = onuService.getOnuIdByIndex(entityId, Long.parseLong(onuIndex));
                onuService.resetOnu(entityId, onuId);
            }
            operationResult = OperationLog.SUCCESS;
            result = "success";
        } catch (Exception e) {
            result = e.getMessage();
            logger.debug("resetOnu error: {}", e);
            operationResult = OperationLog.FAILURE;
        }
        writeDataToAjax(result);
        return NONE;
    }

    /**
     * ONU解注册
     * 
     * @return String
     */
    @OperationLogProperty(actionName = "onuAction", operationName = "deregisterOnu")
    public String deregisterOnu() {
        try {
            onuService.deregisterOnu(entityId, onuId);
            operationResult = OperationLog.SUCCESS;
        } catch (Exception e) {
            logger.debug("deregisterOnu error: {}", e);
            operationResult = OperationLog.FAILURE;
        }
        return NONE;
    }

    /**
     * ONU去激活
     * 
     * @return String
     */
    public String onuDeactive() {
        try {
            onuService.onuDeactive(onuId, onuDeactive);
        } catch (Exception e) {
            logger.debug("deregisterOnu error: {}", e);
        }
        return NONE;
    }

    /**
     * 设置ONU使能
     * 
     * @return String
     */
    public String setOnuAdminStat() throws Exception {
        Map<String, String> message = new HashMap<String, String>();
        try {
            onuService.setOnuAdminStatus(entityId, onuId, onuAdminStatus);
        } catch (SetValueConflictException svce) {
            message.put("message", getString(svce.getMessage(), "epon"));
            logger.debug("rename sni error:{}", svce);
        }
        writeDataToAjax(JSONObject.fromObject(message));
        return NONE;
    }

    /**
     * 设置ONU别名
     * 
     * @return String
     */
    @OperationLogProperty(actionName = "onuAction", operationName = "modifyOnuName")
    public String modifyOnuName() throws Exception {
        String result;
        try {
            source = onuService.modifyOnuName(entityId, onuId, onuName);
            result = "success";
            operationResult = OperationLog.SUCCESS;
        } catch (SetValueConflictException sce) {
            result = sce.getMessage();
            operationResult = OperationLog.FAILURE;
            logger.debug("onu name config error:{}", sce);
        }
        writeDataToAjax(result);
        return NONE;
    }

    /**
     * 获取ONU的type列表，所有的页面禁止ONU TYPE的硬编码,统一调用此获取ONU TYPE的接口
     * 
     * @return
     * @throws SQLException
     * @throws IOException
     */
    public String loadOnuTypes() throws SQLException, IOException {
        List<EntityType> types = oltOnuService.loadOnuTypes();
        JSONObject json = new JSONObject();
        json.put("data", types);
        json.write(response.getWriter());
        return NONE;
    }

    /**
     * 显示ONU基本配置
     * 
     * @return String
     */
    public String showOnuConfig() {
        oltOnuAttribute = onuService.getOnuAttribute(onuId);
        return SUCCESS;
    }

    /**
     * 配置ONU全局MAC地址老化时间
     * 
     * @return
     */
    public String showOnuMacAgeTime() {
        List<UniPort> list = onuAssemblyService.loadUniList(onuId);
        if (list != null && list.size() > 0) {
            Long uniId = list.get(0).getUniId();
            OltUniExtAttribute oltUniExtAttribute = uniService.getOltUniExtAttribute(uniId);
            oltOnuMacAge = oltUniExtAttribute.getMacAge();
            uniIndex = oltUniExtAttribute.getUniIndex();
        }
        return SUCCESS;
    }

    /**
     * 修改ONU的UNI全局MAC地址老化时间
     * 
     * @return
     */
    public String saveOnuMacAgeTime() {
        OltUniExtAttribute uniExtAttribute = new OltUniExtAttribute();
        uniExtAttribute.setEntityId(entityId);
        /* 和 @平台组 沟通协商了解到，只需要操作第一个UNI口就可以达到全局控制的目的，设备会在设置第一个后自动将配置应用到其他所有UNI口 */
        uniExtAttribute.setUniIndex(uniIndex);
        uniExtAttribute.setMacAge(oltOnuMacAge);
        onuService.modifyOnuMacAgeTime(uniExtAttribute, onuId);
        return NONE;
    }

    /**
     * 修改ONU的UNI全局MAC地址老化时间
     * 
     * @return
     */
    public String fetchOnuMacAgeTime() {
        OltUniExtAttribute uniExtAttribute = new OltUniExtAttribute();
        uniExtAttribute.setEntityId(entityId);
        uniExtAttribute.setUniIndex(uniIndex);
        uniExtAttribute = onuService.fetchOnuMacAgeTime(uniExtAttribute, onuId);
        oltOnuMacAge = uniExtAttribute.getMacAge();
        JSONObject json = new JSONObject();
        json.put("oltOnuMacAge", oltOnuMacAge);
        writeDataToAjax(json);
        return NONE;
    }

    /**
     * 显示ONU mac地址最大学习数
     * 
     * @return String
     */
    public String showOnuMacAddrCap() {
        oltTopOnuCapability = onuService.getOltTopOnuCapabilityByOnuId(onuId);
        return SUCCESS;
    }

    /**
     * 显示ONU能力参数
     * 
     * @return String
     */
    public String showOnuAbility() {
        OltOnuCapability tempObj = onuService.getOltOnuCapabilityByOnuId(onuId);
        if (tempObj != null) {
            oltOnuCapability = tempObj;
        }
        return SUCCESS;
    }

    /**
     * ONU VOIP使能设置
     * 
     * @return String
     */
    public String setOnuVOIPStatus() throws Exception {
        Map<String, String> message = new HashMap<String, String>();
        try {
            onuService.setOnuVoipEnable(entityId, onuId, onuVoipEnable);
        } catch (SetValueConflictException svce) {
            message.put("message", getString(svce.getMessage(), "epon"));
            logger.debug("rename sni error:{}", svce);
        }
        writeDataToAjax(JSONObject.fromObject(message));
        return NONE;
    }

    /**
     * ONU温度检测使能
     * 
     * @return String
     */
    public String setOnuTemperatureStatus() throws Exception {
        Map<String, String> message = new HashMap<String, String>();
        try {
            onuService.setOnuTemperatureDetectEnable(entityId, onuId, onuTemperatureDetectEnable);
            onuService.refreshOnuTemperature(entityId, onuId);
        } catch (SetValueConflictException svce) {
            message.put("message", getString(svce.getMessage(), "epon"));
            logger.debug("rename sni error:{}", svce);
        }
        writeDataToAjax(JSONObject.fromObject(message));
        return NONE;
    }

    /**
     * ONU FEC使能
     * 
     * @return String
     */
    public String setOnuFECStatus() throws Exception {
        Map<String, String> message = new HashMap<String, String>();
        try {
            onuService.setOnuFecEnable(entityId, onuId, onuFecEnable);
        } catch (SetValueConflictException svce) {
            message.put("message", getString(svce.getMessage(), "epon"));
            logger.debug("rename sni error:{}", svce);
        }
        writeDataToAjax(JSONObject.fromObject(message));
        return NONE;
    }

    /**
     * ONU 端口隔离使能
     * 
     * @return String
     */
    public String configOnuIsolationEnable() throws Exception {
        Map<String, String> message = new HashMap<String, String>();
        try {
            onuService.configOnuIsolationEnable(entityId, onuId, onuIsolationEnable);
        } catch (SetValueConflictException svce) {
            message.put("message", getString(svce.getMessage(), "epon"));
            logger.debug("rename sni error:{}", svce);
        }
        writeDataToAjax(JSONObject.fromObject(message));
        return NONE;
    }

    /**
     * ONU 15min使能
     * 
     * @return String
     */
    public String setOnu15MinPerfStatus() throws Exception {
        Map<String, String> message = new HashMap<String, String>();
        try {
            onuService.setOnu15minEnable(entityId, onuId, onu15minEnable);
        } catch (SetValueConflictException svce) {
            message.put("message", getString(svce.getMessage(), "epon"));
            logger.debug("rename sni error:{}", svce);
        }
        writeDataToAjax(JSONObject.fromObject(message));
        return NONE;
    }

    /**
     * ONU 24h使能
     * 
     * @return String
     */
    public String setOnu24HourPerfStatus() throws Exception {
        Map<String, String> message = new HashMap<String, String>();
        try {
            onuService.setOnu24hEnable(entityId, onuId, onu24hEnable);
        } catch (SetValueConflictException svce) {
            message.put("message", getString(svce.getMessage(), "epon"));
            logger.debug("rename sni error:{}", svce);
        }
        writeDataToAjax(JSONObject.fromObject(message));
        return NONE;
    }

    /**
     * ONU CATV使能
     * 
     * @return String
     */
    public String setOnuCATVStatus() throws Exception {
        Map<String, String> message = new HashMap<String, String>();
        try {
            onuService.setOnuCatvEnable(entityId, onuId, onuCatvEnable);
        } catch (SetValueConflictException svce) {
            message.put("message", getString(svce.getMessage(), "epon"));
            logger.debug("rename sni error:{}", svce);
        }
        writeDataToAjax(JSONObject.fromObject(message));
        return NONE;
    }

    /**
     * ONUMAC地址最大学习数设置
     * 
     * @return String
     */
    @OperationLogProperty(actionName = "onuAction", operationName = "modifyOnuMacMax")
    public String modifyOnuMacMax() {
        try {
            onuService.setOnuMacMaxNum(entityId, onuId, onuMaxMacNum);
            operationResult = OperationLog.SUCCESS;
        } catch (Exception e) {
            logger.debug("modifyOnuMacMax error: {}", e);
            operationResult = OperationLog.FAILURE;
        }
        return NONE;
    }

    /**
     * 刷新ONU温度
     * 
     * @return
     * @throws Exception
     */
    public String refreshOnuTemperature() throws Exception {
        String message = "false";
        try {
            Thread.sleep(10000);
            message = String.valueOf(
                    UnitConfigConstant.translateTemperature(onuService.refreshOnuTemperature(entityId, onuId)));
        } catch (Exception sce) {
            logger.debug("refreshOnuTemperature Error:{}", sce);
        } finally {
            JSONObject json = new JSONObject();
            json.put("message", message);
            json.put("entityId", entityId);
            json.put("onuId", onuId);
            Message ms = new Message("refreshOnuTemp");
            String id = ServletActionContext.getRequest().getSession().getId();
            ms.addSessionId(id);
            ms.setData(json.toString());
            messagePusher.sendMessage(ms);
        }
        return NONE;
    }

    /**
     * 从设备刷新ONU的数据
     * 
     * @throws IOException
     * 
     */
    /*
     * public String refreshOnuInfo() throws IOException { JSONObject json = new JSONObject(); try {
     * onuService.refreshOnuInfo(entityId); json.put("message", true); } catch (Exception e) {
     * json.put("message", false); logger.debug("error:{}", e.getMessage()); } write(json); return
     * NONE; }
     */

    /**
     * UNI端口Mac地址清理
     * 
     * @throws Exception
     */
    public String onuMacClear() throws Exception {
        String message;
        try {
            uniService.onuMacClear(entityId, uniId);
            message = "success";
            operationResult = OperationLog.SUCCESS;
        } catch (Exception sce) {
            message = sce.getMessage();
            operationResult = OperationLog.FAILURE;
            logger.debug("error:{}", sce);
        }
        writeDataToAjax(message);
        return NONE;
    }

    /**
     * 添加onu到拓扑图
     * 
     * @return
     * @throws Exception
     */
    // Modify by Rod
    public String moveToTopoFromOnuView() throws Exception {
        // onuId equals entityId
        Long entityId = onuId;
        Entity entity = entityService.getEntity(entityId);
        entity.setFolderId(folderId);
        onuService.moveToTopoFromOnuView(onuId, entity);
        return NONE;
    }

    /**
     * 显示选择拓扑图页面
     * 
     * @return
     */
    public String moveToTopoJsp() {
        return SUCCESS;
    }

    /**
     * 获得ONU列表
     * 
     * @return
     */
    public String loadOnuList() throws Exception {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        if (onuName != null && !"".equals(onuName)) {
            // mysql中下划线是特殊的，like的时候必须转义
            if (onuName.contains("_")) {
                onuName = onuName.replace("_", "\\_");
            }
            paramMap.put("onuName", onuName);
        }
        if (onuPreType != null && !"".equals(onuPreType) && !"-1".equals(onuPreType)) {
            paramMap.put("onuPreType", onuPreType);
        }
        if (entityId != null && entityId != -1) {
            paramMap.put("entityId", entityId);
        }
        if (slotId != null && slotId != -1) {
            paramMap.put("slotId", slotId);
        }
        if (ponId != null && ponId != -1) {
            paramMap.put("ponId", ponId);
        }
        if (macAddress != null && !"".equals(macAddress)) {
            String formatQueryMac = MacUtils.formatQueryMac(macAddress);
            if (formatQueryMac.indexOf(":") == -1) {
                paramMap.put("queryMacWithoutSplit", formatQueryMac);
            }
            paramMap.put("macAddress", formatQueryMac);
        }
        paramMap.put("start", start);
        paramMap.put("limit", limit);
        paramMap.put("sort", sort);
        paramMap.put("dir", dir);
        List<OltOnuAttribute> onuList = onuService.getOnuList(paramMap);
        int size = onuService.getOnuListCount(paramMap);
        JSONObject json = new JSONObject();
        // add by fanzidong,需要在展示前格式化MAC地址
        UserContext uc = (UserContext) super.getSession().get(UserContext.KEY);
        String displayRule = uc.getMacDisplayStyle();
        JSONArray jsonArray = new JSONArray();
        for (OltOnuAttribute oltOnuAttribute : onuList) {
            JSONObject ounJson = JSONObject.fromObject(oltOnuAttribute);
            String formatedMac = MacUtils.convertMacToDisplayFormat(oltOnuAttribute.getOnuMac(), displayRule);
            ounJson.put("onuMac", formatedMac);
            jsonArray.add(ounJson);
        }
        json.put("rowCount", size);
        json.put("data", jsonArray);
        json.write(response.getWriter());
        return NONE;
    }

    /**
     * 跳转到电力ONU的广播风暴抑制页面
     * 
     * @return
     */
    public String showOnuStormOutPacketRate() {
        uniStorm = uniService.getUniStormSuppressionByUniId(uniId);
        uniIndex = uniService.getOltUniExtAttribute(uniId).getUniIndex();
        return SUCCESS;
    }

    /**
     * 修改电力ONU的广播风暴抑制参数
     * 
     * @return
     * @throws Exception
     */
    @OperationLogProperty(actionName = "onuAction", operationName = "modifyOnuStormOutPacketRate")
    public String modifyOnuStormOutPacketRate() throws Exception {
        String mes = "success";
        try {
            electricityOnuService.modifyOnuStormOutPacketRate(entityId, uniId, unicastStormOutPacketRate,
                    multicastStormOutPacketRate, broadcastStormOutPacketRate);
        } catch (Exception e) {
            mes = "modifyOnuStormOutPacketRate failed:<br>" + e.getMessage();
        }
        writeDataToAjax(mes);
        return NONE;
    }

    @OperationLogProperty(actionName = "onuAction", operationName = "deleteOnuAuth")
    public String deleteOnuAuth() {
        OltOnuAttribute onuAttribute = onuService.getOnuAttribute(onuId);
        onuIndex = onuAttribute.getOnuIndex();
        source = EponIndex.getOnuStringByIndex(onuIndex).toString();
        OltAuthentication auth = onuAuthService.getOltAuthenticationByIndex(entityId, onuIndex);
        ponId = auth.getPonId();
        onuAuthService.deleteAuthenPreConfig(entityId, ponId, onuIndex, auth.getAuthType());
        return NONE;
    }

    /**
     * 刷新UNI口的广播风暴抑制参数
     * 
     * @param entityId
     */
    public String refreshOnuStormOutPacketRate() throws Exception {
        String mes = "success";
        try {
            uniService.refreshUniStormOutPacketRate(entityId);
        } catch (Exception e) {
            mes = "refreshOnuStormOutPacketRate failed:<br>" + e.getMessage();
        }
        writeDataToAjax(mes);
        return NONE;
    }

    /**
     * 获得单个olt下的所有slot列表
     * 
     * @author flackyang
     * @since 2013-12-30 onu列表查询使用
     * @return
     * @throws IOException
     */
    public String getOltSlotList() throws IOException {
        List<OltSlotAttribute> oltSlotList = oltSlotService.getOltPonSlotList(entityId);
        for (OltSlotAttribute oltSlotAttribute : oltSlotList) {
            oltSlotAttribute.setSlotNoStr("SLOT" + oltSlotAttribute.getSlotNo());
        }
        JSONArray slotArray = JSONArray.fromObject(oltSlotList);
        slotArray.write(response.getWriter());
        return NONE;
    }

    public String getOltEponSlotList() throws IOException {
        List<OltSlotAttribute> oltSlotList = oltSlotService.getOltEponSlotList(entityId);
        JSONArray slotArray = JSONArray.fromObject(oltSlotList);
        slotArray.write(response.getWriter());
        return NONE;
    }

    public String getOltGponSlotList() throws IOException {
        List<OltSlotAttribute> oltSlotList = oltSlotService.getOltGponSlotList(entityId);
        JSONArray slotArray = JSONArray.fromObject(oltSlotList);
        slotArray.write(response.getWriter());
        return NONE;
    }

    /**
     * 获取olt业务板pon口列表
     * 
     * @author flackyang
     * @since 2013-12-30 onu列表查询使用
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
     * 刷新ONU基本信息
     * 
     * @return
     */
    public String refreshOnuBaseInfo() {
        OnuBaseInfo onuInfo = new OnuBaseInfo();
        onuInfo.setEntityId(entityId);
        onuInfo.setOnuIndex(onuIndex);
        onuInfo.setOnuId(onuId);
        onuService.refreshOnuBaseInfo(onuInfo);
        return NONE;
    }

    /**
     * 刷新OLT下类A型设备基本信息
     * 
     * @return
     */
    public String refreshCC8800ABaseInfo() {
        OnuBaseInfo onuInfo = new OnuBaseInfo();
        onuInfo.setEntityId(entityId);
        onuInfo.setOnuIndex(onuIndex);
        onuInfo.setOnuId(onuId);
        CC8800ABaseInfo caInfo = new CC8800ABaseInfo();
        caInfo.setEntityId(entityId);
        caInfo.setCmcId(cmcId);
        caInfo.setCmcIndex(cmcIndex);
        onuService.refreshCC8800ABaseInfo(caInfo, onuInfo);
        return NONE;
    }

    public String getOnuAttrById() {
        oltOnuAttribute = onuService.getOnuAttribute(onuId);
        writeDataToAjax(oltOnuAttribute);
        return NONE;
    }

    public String getTopOnuUpgradeOnuList() {
        return topOnuUpgradeOnuList;
    }

    public void setTopOnuUpgradeOnuList(String topOnuUpgradeOnuList) {
        this.topOnuUpgradeOnuList = topOnuUpgradeOnuList;
    }

    public Long getOnuId() {
        return onuId;
    }

    public void setOnuId(Long onuId) {
        this.onuId = onuId;
    }

    public Long getPonId() {
        return ponId;
    }

    public void setPonId(Long ponId) {
        this.ponId = ponId;
    }

    public Long getSlotId() {
        return slotId;
    }

    public void setSlotId(Long slotId) {
        this.slotId = slotId;
    }

    public Integer getLlid() {
        return llid;
    }

    public void setLlid(Integer llid) {
        this.llid = llid;
    }

    public Long getMac() {
        return mac;
    }

    public void setMac(Long mac) {
        this.mac = mac;
    }

    public String getOnuName() {
        return onuName;
    }

    public void setOnuName(String onuName) {
        this.onuName = onuName;
    }

    public Long getUniId() {
        return uniId;
    }

    public void setUniId(Long uniId) {
        this.uniId = uniId;
    }

    public String getLlidString() {
        return llidString;
    }

    public void setLlidString(String llidString) {
        this.llidString = llidString;
    }

    public Integer getOnuVoipEnable() {
        return onuVoipEnable;
    }

    public void setOnuVoipEnable(Integer onuVoipEnable) {
        this.onuVoipEnable = onuVoipEnable;
    }

    public Integer getOnuTemperatureDetectEnable() {
        return onuTemperatureDetectEnable;
    }

    public void setOnuTemperatureDetectEnable(Integer onuTemperatureDetectEnable) {
        this.onuTemperatureDetectEnable = onuTemperatureDetectEnable;
    }

    public Integer getOnuFecEnable() {
        return onuFecEnable;
    }

    public void setOnuFecEnable(Integer onuFecEnable) {
        this.onuFecEnable = onuFecEnable;
    }

    public Integer getOnuIsolationEnable() {
        return onuIsolationEnable;
    }

    public void setOnuIsolationEnable(Integer onuIsolationEnable) {
        this.onuIsolationEnable = onuIsolationEnable;
    }

    public Integer getOnu15minEnable() {
        return onu15minEnable;
    }

    public void setOnu15minEnable(Integer onu15minEnable) {
        this.onu15minEnable = onu15minEnable;
    }

    public Integer getOnu24hEnable() {
        return onu24hEnable;
    }

    public void setOnu24hEnable(Integer onu24hEnable) {
        this.onu24hEnable = onu24hEnable;
    }

    public Integer getOnuCatvEnable() {
        return onuCatvEnable;
    }

    public void setOnuCatvEnable(Integer onuCatvEnable) {
        this.onuCatvEnable = onuCatvEnable;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    public Integer getMacStatusFlag() {
        return macStatusFlag;
    }

    public void setMacStatusFlag(Integer macStatusFlag) {
        this.macStatusFlag = macStatusFlag;
    }

    public Long getOnuPonId() {
        return onuPonId;
    }

    public void setOnuPonId(Long onuPonId) {
        this.onuPonId = onuPonId;
    }

    public Integer getOnuAdminStatus() {
        return onuAdminStatus;
    }

    public void setOnuAdminStatus(Integer onuAdminStatus) {
        this.onuAdminStatus = onuAdminStatus;
    }

    public Integer getOnuMaxMacNum() {
        return onuMaxMacNum;
    }

    public void setOnuMaxMacNum(Integer onuMaxMacNum) {
        this.onuMaxMacNum = onuMaxMacNum;
    }

    public Long getUnicastStormOutPacketRate() {
        return unicastStormOutPacketRate;
    }

    public void setUnicastStormOutPacketRate(Long unicastStormOutPacketRate) {
        this.unicastStormOutPacketRate = unicastStormOutPacketRate;
    }

    public Long getMulticastStormOutPacketRate() {
        return multicastStormOutPacketRate;
    }

    public void setMulticastStormOutPacketRate(Long multicastStormOutPacketRate) {
        this.multicastStormOutPacketRate = multicastStormOutPacketRate;
    }

    public Long getBroadcastStormOutPacketRate() {
        return broadcastStormOutPacketRate;
    }

    public void setBroadcastStormOutPacketRate(Long broadcastStormOutPacketRate) {
        this.broadcastStormOutPacketRate = broadcastStormOutPacketRate;
    }

    public Long getFolderId() {
        return folderId;
    }

    public void setFolderId(Long folderId) {
        this.folderId = folderId;
    }

    public Date getStatDate() {
        return statDate;
    }

    public void setStatDate(Date statDate) {
        this.statDate = statDate;
    }

    public String getOnuPreType() {
        return onuPreType;
    }

    public void setOnuPreType(String onuPreType) {
        this.onuPreType = onuPreType;
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

    public OltOnuAttribute getOltOnuAttribute() {
        return oltOnuAttribute;
    }

    public void setOltOnuAttribute(OltOnuAttribute oltOnuAttribute) {
        this.oltOnuAttribute = oltOnuAttribute;
    }

    public List<OltOnuAttribute> getOnuList() {
        return onuList;
    }

    public void setOnuList(List<OltOnuAttribute> onuList) {
        this.onuList = onuList;
    }

    public String getOltSoftVersion() {
        return oltSoftVersion;
    }

    public void setOltSoftVersion(String oltSoftVersion) {
        this.oltSoftVersion = oltSoftVersion;
    }

    public Entity getEntity() {
        return entity;
    }

    public void setEntity(Entity entity) {
        this.entity = entity;
    }

    public JSONArray getPonListObject() {
        return ponListObject;
    }

    public void setPonListObject(JSONArray ponListObject) {
        this.ponListObject = ponListObject;
    }

    public JSONArray getSlotPonObject() {
        return slotPonObject;
    }

    public void setSlotPonObject(JSONArray slotPonObject) {
        this.slotPonObject = slotPonObject;
    }

    public JSONArray getOnuIgmpMode() {
        return onuIgmpMode;
    }

    public void setOnuIgmpMode(JSONArray onuIgmpMode) {
        this.onuIgmpMode = onuIgmpMode;
    }

    public JSONObject getLayout() {
        return layout;
    }

    public void setLayout(JSONObject layout) {
        this.layout = layout;
    }

    public JSONArray getEntityList() {
        return entityList;
    }

    public void setEntityList(JSONArray entityList) {
        this.entityList = entityList;
    }

    public OltOnuPonAttribute getOltOnuPonAttribute() {
        return oltOnuPonAttribute;
    }

    public void setOltOnuPonAttribute(OltOnuPonAttribute oltOnuPonAttribute) {
        this.oltOnuPonAttribute = oltOnuPonAttribute;
    }

    public OltTopOnuCapability getOltTopOnuCapability() {
        return oltTopOnuCapability;
    }

    public void setOltTopOnuCapability(OltTopOnuCapability oltTopOnuCapability) {
        this.oltTopOnuCapability = oltTopOnuCapability;
    }

    public OltOnuCapability getOltOnuCapability() {
        return oltOnuCapability;
    }

    public void setOltOnuCapability(OltOnuCapability oltOnuCapability) {
        this.oltOnuCapability = oltOnuCapability;
    }

    public OltUniStormSuppressionEntry getUniStorm() {
        return uniStorm;
    }

    public void setUniStorm(OltUniStormSuppressionEntry uniStorm) {
        this.uniStorm = uniStorm;
    }

    public Long getUniIndex() {
        return uniIndex;
    }

    public void setUniIndex(Long uniIndex) {
        this.uniIndex = uniIndex;
    }

    public void setOltOnuList(List<OltOnuAttribute> oltOnuList) {
        this.oltOnuList = oltOnuList;
    }

    public String getDisplayStyle() {
        return displayStyle;
    }

    public void setDisplayStyle(String displayStyle) {
        this.displayStyle = displayStyle;
    }

    public Integer getOltOnuMacAge() {
        return oltOnuMacAge;
    }

    public void setOltOnuMacAge(Integer oltOnuMacAge) {
        this.oltOnuMacAge = oltOnuMacAge;
    }

    public String getCameraSwitch() {
        return cameraSwitch;
    }

    public void setCameraSwitch(String cameraSwitch) {
        this.cameraSwitch = cameraSwitch;
    }

    public JSONArray getSubListArray() {
        return subListArray;
    }

    public void setSubListArray(JSONArray subListArray) {
        this.subListArray = subListArray;
    }

    public Long getOnuIndex() {
        return onuIndex;
    }

    public void setOnuIndex(Long onuIndex) {
        this.onuIndex = onuIndex;
    }

    public String getEntityIp() {
        return entityIp;
    }

    public void setEntityIp(String entityIp) {
        this.entityIp = entityIp;
    }

    public Long getCmcId() {
        return cmcId;
    }

    public void setCmcId(Long cmcId) {
        this.cmcId = cmcId;
    }

    public Long getCmcIndex() {
        return cmcIndex;
    }

    public void setCmcIndex(Long cmcIndex) {
        this.cmcIndex = cmcIndex;
    }

    @Override
    public String getSource() {
        return source;
    }

    @Override
    public void setSource(String source) {
        this.source = source;
    }

    public Integer getOnuDeactive() {
        return onuDeactive;
    }

    public void setOnuDeactive(Integer onuDeactive) {
        this.onuDeactive = onuDeactive;
    }

}
