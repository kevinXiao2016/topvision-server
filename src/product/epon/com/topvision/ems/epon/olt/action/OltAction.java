/***********************************************************************
 * $Id: OltSlotAction.java,v1.0 2013-10-25 上午10:36:11 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.olt.action;

import java.io.IOException;
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

import com.topvision.ems.epon.domain.CcmtsFftMonitorScalar;
import com.topvision.ems.epon.domain.DeviceItem;
import com.topvision.ems.epon.domain.Olt;
import com.topvision.ems.epon.domain.Room;
import com.topvision.ems.epon.exception.SwitchOverException;
import com.topvision.ems.epon.exception.SyncSlaveBoardException;
import com.topvision.ems.epon.igmp.domain.IgmpEntityTable;
import com.topvision.ems.epon.igmp.service.OltIgmpService;
import com.topvision.ems.epon.olt.domain.OltAttribute;
import com.topvision.ems.epon.olt.domain.OltMacAddressLearnTable;
import com.topvision.ems.epon.olt.domain.TopOnuGlobalCfgMgmt;
import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.network.service.EntityService;
import com.topvision.framework.annotation.OperationLogProperty;
import com.topvision.framework.common.MacUtils;
import com.topvision.framework.common.NumberUtils;
import com.topvision.framework.exception.engine.SnmpSetException;
import com.topvision.platform.domain.OperationLog;
import com.topvision.platform.domain.UserContext;
import com.topvision.platform.domain.UserPreferences;
import com.topvision.platform.service.SystemPreferencesService;
import com.topvision.platform.service.UserPreferencesService;
import com.topvision.platform.util.ReportConstants;

/**
 * @author flack
 * @created @2013-10-25-上午10:36:11
 * 
 */
@Controller("oltAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class OltAction extends AbstractEponAction {
    private static final long serialVersionUID = -4181697255558217530L;
    private final Logger logger = LoggerFactory.getLogger(OltAction.class);
    @Autowired
    private EntityService entityService;
    @Autowired
    private OltIgmpService oltIgmpService;
    @Autowired
    private UserPreferencesService userPreferencesService;
    @Autowired
    private SystemPreferencesService systemPreferencesService;

    private OltAttribute oltAttribute;
    private Entity entity;
    private Olt olt;
    private Integer slotNumInUse;
    private Integer fanNumInUse;
    private Integer powerNumInUse;
    private Integer syncAction;
    private String ip;
    private String entityName;
    protected List<DeviceItem> devices;
    protected String entityIds;
    protected String entityNames;
    protected String entityIps;
    protected int reportCycle = ReportConstants.CYCLE_TODAY;
    protected String startDate;
    protected String endDate;
    protected int startTime;
    protected int endTime;
    protected Date startDay;
    protected Date endDay;
    private int start;
    private int limit;
    // olt面板图页面视图参数
    private Integer rightTopHeight;
    private Integer middleBottomHeight;
    private Integer leftWidth;
    private Integer rightWidth;
    private Boolean rightTopOpen;
    private Boolean rightBottomOpen;
    private Boolean middleBottomOpen;
    private Boolean layoutToMiddle;
    private Integer tabBtnSelectedIndex;
    private String oltSoftVersion;
    private JSONObject layout;
    private Integer deviceIgmpMode;
    private Room room;
    private Integer fftMonitorGlbStatus;
    private String cameraSwitch;
    //OLT列表查询新增参数
    private Long deviceType;
    private Integer onlineStatus;

    private String matchMacAddr;
    private JSONObject onuGlobalCfg;
    private int wanSwitch;
    private int wlanSwitch;
    private int catvSwitch;

    /**
     * 系统校时
     * 
     * @return String
     * @throws Exception
     */
    @OperationLogProperty(actionName = "oltAction", operationName = "checkSysTiming")
    public String checkSysTiming() throws Exception {
        String result;
        try {
            oltService.checkSysTiming(entityId);
            result = "success";
            operationResult = OperationLog.SUCCESS;
        } catch (SnmpSetException e) {
            logger.info("checkSysTiming error: {}", e);
            result = "fail";
            operationResult = OperationLog.FAILURE;
        } catch (Exception e) {
            logger.info("checkSysTiming error: {}", e);
            result = "fail";
            operationResult = OperationLog.FAILURE;
        }
        writeDataToAjax(result);
        return NONE;
    }

    /**
     * OLT复位
     * 
     * @return String
     * @throws Exception
     */
    @OperationLogProperty(actionName = "oltAction", operationName = "resetOlt")
    public String resetOlt() throws Exception {
        String result;
        try {
            oltService.resetOlt(entityId);
            result = "success";
            operationResult = OperationLog.SUCCESS;
        } catch (Exception e) {
            logger.debug("resetOlt error: {}", e);
            result = e.getMessage();
            operationResult = OperationLog.FAILURE;
        }
        writeDataToAjax(result);
        return NONE;
    }

    /**
     * OLT主备倒换
     * 
     * @return String
     * @throws Exception
     */
    @OperationLogProperty(actionName = "oltAction", operationName = "switchoverOlt")
    public String switchoverOlt() throws Exception {
        Map<String, String> message = new HashMap<String, String>();
        try {
            oltService.switchoverOlt(entityId);
            operationResult = OperationLog.SUCCESS;
        } catch (SwitchOverException soe) {
            message.put("message", getString(soe.getMessage(), "epon"));
            operationResult = OperationLog.FAILURE;
            logger.debug("switchoverOlt Error:{}", soe);
        }
        writeDataToAjax(message);
        return NONE;
    }

    /**
     * 备用主控板同步
     * 
     * @return String
     * @throws Exception
     */
    @OperationLogProperty(actionName = "oltAction", operationName = "syncSlaveBoard")
    public String syncSlaveBoard() throws Exception {
        String result;
        try {
            oltService.syncSlaveBoard(entityId, syncAction);
            result = "success";
            operationResult = OperationLog.SUCCESS;
        } catch (SyncSlaveBoardException e) {
            logger.debug("syncSlaveBoard error: {}", e);
            result = "syncInprogress";
            operationResult = OperationLog.FAILURE;
        } catch (Exception e) {
            logger.debug("syncSlaveBoard error: {}", e);
            result = e.getMessage();
            operationResult = OperationLog.FAILURE;
        }
        writeDataToAjax(result);
        return NONE;
    }

    /**
     * OLT恢复出厂设置
     * 
     * @return String
     * @throws Exception
     */
    @OperationLogProperty(actionName = "oltAction", operationName = "restoreOlt")
    public String restoreOlt() throws Exception {
        String result;
        try {
            oltService.restoreOlt(entityId);
            result = "success";
            operationResult = OperationLog.SUCCESS;
        } catch (Exception e) {
            logger.debug("restoreOlt error: {}", e);
            result = e.getMessage();
            operationResult = OperationLog.FAILURE;
        }
        writeDataToAjax(result);
        return NONE;
    }

    /**
     * OLT面板图
     * 
     * @return String
     */
    public String showOltFaceplate() {
        Properties properties = systemPreferencesService.getModulePreferences("camera");
        cameraSwitch = properties.getProperty("camera.switch");
        entity = entityService.getEntity(entityId);
        // 获得OLT设备的软件版本号
        oltSoftVersion = oltService.getOltSoftVersion(entityId);
        // olt面板图页面视图参数
        layout = this.getUserView();
        // TODO 目前IGMP没有数据，为防止页面出不来，故try-catch
        try {
            IgmpEntityTable table = oltIgmpService.getIgmpGlobalInfo(entityId);
            deviceIgmpMode = table.getIgmpMode();
        } catch (Exception e) {
            deviceIgmpMode = 2;
        }
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
     * 保存用户设置的Olt面板图页面视图
     * 
     * @return
     */
    public String saveOltFaceView() {
        UserContext uc = (UserContext) ServletActionContext.getRequest().getSession().getAttribute("UserContext");
        Properties oltView = new Properties();
        oltView.setProperty("rightTopHeight", String.valueOf(rightTopHeight));
        oltView.setProperty("middleBottomHeight", String.valueOf(middleBottomHeight));
        oltView.setProperty("leftWidth", String.valueOf(leftWidth));
        oltView.setProperty("rightWidth", String.valueOf(rightWidth));
        oltView.setProperty("rightTopOpen", String.valueOf(rightTopOpen));
        oltView.setProperty("rightBottomOpen", String.valueOf(rightBottomOpen));
        oltView.setProperty("middleBottomOpen", String.valueOf(middleBottomOpen));
        oltView.setProperty("layoutToMiddle", String.valueOf(layoutToMiddle));
        oltView.setProperty("tabBtnSelectedIndex", String.valueOf(tabBtnSelectedIndex));
        userPreferencesService.batchSaveModulePreferences("oltFaceplateView", uc.getUserId(), oltView);
        return NONE;
    }

    /**
     * 页面OLT对象构造实例
     * 
     * @return String
     * @throws java.io.IOException
     */
    public String oltObjectCreate() throws IOException {
        olt = oltService.getOltStructure(entityId);
        response.getWriter().print(JSONObject.fromObject(olt));
        return NONE;
    }

    /**
     * 主控板升级
     * 
     * @return
     */
    public String showOltControlBoardUpdate() {
        oltAttribute = oltService.getOltAttribute(entityId);
        // add by fanzidong,需要在展示前格式化MAC地址
        UserContext uc = (UserContext) super.getSession().get(UserContext.KEY);
        String displayRule = uc.getMacDisplayStyle();
        String formatedOutbandMac = MacUtils.convertMacToDisplayFormat(oltAttribute.getOutbandMac(), displayRule);
        oltAttribute.setOutbandMac(formatedOutbandMac);
        String formatedInbandMac = MacUtils.convertMacToDisplayFormat(oltAttribute.getInbandMac(), displayRule);
        oltAttribute.setInbandMac(formatedInbandMac);
        return SUCCESS;
    }

    /* *//**
              * 板卡使能数据刷新
              * 
              * @return String
              * @throws Exception
              */
    /*
    public String refreshOlt() throws Exception {
     String message;
     try {
         oltService.refreshOltEntity(entityId);
         message = "success";
     } catch (Exception e) {
         message = e.getMessage();
         logger.debug("refresh Olt error:{}", e);
     }
     write(message);
     return NONE;
    }*/

    /**
     * 获得OLT列表
     * 
     * @return
     */
    public String getOltList() throws Exception {
        Map<String, Object> paramsMap = new HashMap<String, Object>();
        paramsMap.put("start", start);
        paramsMap.put("limit", limit);
        paramsMap.put("sort", sort);// 排序字段
        paramsMap.put("dir", dir);// 排序方向
        if (entityName != null && !"".equals(entityName)) {
            //mysql中下划线是特殊的，like的时候必须转义
            if (entityName.contains("_")) {
                entityName = entityName.replace("_", "\\_");
            }
            paramsMap.put("entityName", entityName);
        }
        if (deviceType != null && deviceType != -1) {
            paramsMap.put("deviceType", deviceType);
        }
        if (onlineStatus != null && onlineStatus != -1) {
            paramsMap.put("onlineStatus", onlineStatus);
        }
        List<OltAttribute> oltList = oltService.getOltList(paramsMap);
        int size = oltService.getOltListCount(paramsMap);
        JSONArray oltListArray = new JSONArray();
        Long tempTime = System.currentTimeMillis();
        for (OltAttribute oltAttribute : oltList) {
            if (oltAttribute.getCpuUsed() != null && oltAttribute.getCpuUsed() > -1.0) {
                String cpuUsed = NumberUtils.getPercentStr(oltAttribute.getCpuUsed());
                oltAttribute.setCpu(cpuUsed);
            } else {
                oltAttribute.setCpu("-");
            }
            if (oltAttribute.getMemUsed() != null && oltAttribute.getMemUsed() > -1.0) {
                String memUsed = NumberUtils.getPercentStr(oltAttribute.getMemUsed());
                oltAttribute.setMem(memUsed);
            } else {
                oltAttribute.setMem("-");
            }
            if (oltAttribute.getSysUpTime() != null && oltAttribute.getSysUpTime() != -1) {
                oltAttribute.setSysUpTime((tempTime - oltAttribute.getSnapTime().getTime()) / 1000
                        + oltAttribute.getSysUpTime() / 100);
            } else {
                oltAttribute.setSysUpTime(-1L);
            }
            oltListArray.add(oltAttribute);
        }
        JSONObject json = new JSONObject();
        json.put("rowCount", size);
        json.put("data", oltListArray);
        writeDataToAjax(json);
        return NONE;
    }

    /**
     * OLT MAC地址表展示
     * 
     * @return
     */
    public String showOltMacListTab() throws Exception {
        return SUCCESS;
    }

    public String showMacInfoJsp() throws Exception {
        return SUCCESS;
    }

    public String loadMacInfo() throws Exception {
        String formatedMac = MacUtils.formatMac(matchMacAddr.trim());
        String macInfo = oltService.getMacInfoStr(entityId, formatedMac);
        writeDataToAjax(macInfo);
        return NONE;
    }

    /**
     * 加载OLT MAC地址表展示
     * 
     * @return
     */
    public String loadOltMacListTab() throws Exception {
        List<OltMacAddressLearnTable> oltMacList = oltService.getOltMacLearnTableList(entityId);
        // add by fanzidong,展示之前需要格式化MAC地址
        UserContext uc = (UserContext) super.getSession().get(UserContext.KEY);
        String macRule = uc.getMacDisplayStyle();
        for (OltMacAddressLearnTable learnTable : oltMacList) {
            String formatedMac = MacUtils.convertMacToDisplayFormat(learnTable.getTopSysMacAddr(), macRule);
            learnTable.setTopSysMacAddr(formatedMac);
        }
        writeDataToAjax(JSONArray.fromObject(oltMacList));
        return NONE;
    }

    /**
     * 刷新OLT MAC地址表展示
     * 
     * @return
     */
    public String refreshOltMacListTab() throws Exception {
        oltService.refreshOltMacLearnTable(entityId);
        return NONE;
    }

    /**
     * 获取CCMTS频谱全局开关
     * 
     * @return String
     * @throws IOException
     */
    public String loadCcmtsFftGbStatus() throws IOException {
        Map<String, Object> json = new HashMap<String, Object>();
        CcmtsFftMonitorScalar cmtsFftMonitorScalar = oltService.getCcmtsFftGbStatus(entityId);
        json.put("ccmtsFftMonitorScalar", cmtsFftMonitorScalar);
        writeDataToAjax(JSONObject.fromObject(json));
        return NONE;
    }

    /**
     * 设置CCMTS频谱全局开关
     * 
     * @return String
     * @throws IOException 
     */
    public String modifyCcmtsFftGbStatus() throws IOException {
        String message;
        try {
            oltService.modifyCcmtsFftGbStatus(entityId, fftMonitorGlbStatus);
            message = "success";
        } catch (Exception e) {
            message = e.getMessage();
            logger.debug("", e);
        }
        writeDataToAjax(message);
        return NONE;
    }

    /**
     * 展示ONU全局管理开关配置页面
     * @return
     */
    public String showOnuGlobalConfig() {
        List<TopOnuGlobalCfgMgmt> topOnuGlobalCfgMgmts = oltService.getTopOnuGlobalCfgMgmt(entityId);
        onuGlobalCfg = new JSONObject();
        if (topOnuGlobalCfgMgmts != null) {
            for (TopOnuGlobalCfgMgmt cfg : topOnuGlobalCfgMgmts) {
                switch (cfg.getTopOnuGlobalCfgMgmtItemIndex()) {
                case TopOnuGlobalCfgMgmt.WAN:
                    onuGlobalCfg.put("wanSwitch", cfg.getMgmtValue());
                    break;
                case TopOnuGlobalCfgMgmt.WLAN:
                    onuGlobalCfg.put("wlanSwitch", cfg.getMgmtValue());
                    break;
                case TopOnuGlobalCfgMgmt.CATV:
                    onuGlobalCfg.put("catvSwitch", cfg.getMgmtValue());
                    break;
                default:
                    break;
                }
            }
        }
        return SUCCESS;
    }

    /**
     * 修改ONU全局管理开关
     * @return
     */
    public String modifyOnuGlobalCfg() {
        //封装ONU全局开关对象
        List<TopOnuGlobalCfgMgmt> topOnuGlobalCfgMgmts = new ArrayList<TopOnuGlobalCfgMgmt>();
        TopOnuGlobalCfgMgmt topOnuGlobalCfgMgmt;

        //wan
        topOnuGlobalCfgMgmt = new TopOnuGlobalCfgMgmt();
        topOnuGlobalCfgMgmt.setEntityId(entityId);
        topOnuGlobalCfgMgmt.setTopOnuGlobalCfgMgmtItemIndex(TopOnuGlobalCfgMgmt.WAN);
        topOnuGlobalCfgMgmt.setMgmtValue(wanSwitch);
        topOnuGlobalCfgMgmts.add(topOnuGlobalCfgMgmt);

        //wlan
        topOnuGlobalCfgMgmt = new TopOnuGlobalCfgMgmt();
        topOnuGlobalCfgMgmt.setEntityId(entityId);
        topOnuGlobalCfgMgmt.setTopOnuGlobalCfgMgmtItemIndex(TopOnuGlobalCfgMgmt.WLAN);
        topOnuGlobalCfgMgmt.setMgmtValue(wlanSwitch);
        topOnuGlobalCfgMgmts.add(topOnuGlobalCfgMgmt);

        //catv
        topOnuGlobalCfgMgmt = new TopOnuGlobalCfgMgmt();
        topOnuGlobalCfgMgmt.setEntityId(entityId);
        topOnuGlobalCfgMgmt.setTopOnuGlobalCfgMgmtItemIndex(TopOnuGlobalCfgMgmt.CATV);
        topOnuGlobalCfgMgmt.setMgmtValue(catvSwitch);
        topOnuGlobalCfgMgmts.add(topOnuGlobalCfgMgmt);

        oltService.modifyTopOnuGlobalCfgMgmt(topOnuGlobalCfgMgmts, entityId);
        return NONE;
    }

    public OltAttribute getOltAttribute() {
        return oltAttribute;
    }

    public void setOltAttribute(OltAttribute oltAttribute) {
        this.oltAttribute = oltAttribute;
    }

    public Entity getEntity() {
        return entity;
    }

    public void setEntity(Entity entity) {
        this.entity = entity;
    }

    public Olt getOlt() {
        return olt;
    }

    public void setOlt(Olt olt) {
        this.olt = olt;
    }

    public Integer getSlotNumInUse() {
        return slotNumInUse;
    }

    public void setSlotNumInUse(Integer slotNumInUse) {
        this.slotNumInUse = slotNumInUse;
    }

    public Integer getFanNumInUse() {
        return fanNumInUse;
    }

    public void setFanNumInUse(Integer fanNumInUse) {
        this.fanNumInUse = fanNumInUse;
    }

    public Integer getPowerNumInUse() {
        return powerNumInUse;
    }

    public void setPowerNumInUse(Integer powerNumInUse) {
        this.powerNumInUse = powerNumInUse;
    }

    public Integer getSyncAction() {
        return syncAction;
    }

    public void setSyncAction(Integer syncAction) {
        this.syncAction = syncAction;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public List<DeviceItem> getDevices() {
        return devices;
    }

    public void setDevices(List<DeviceItem> devices) {
        this.devices = devices;
    }

    public String getEntityIds() {
        return entityIds;
    }

    public void setEntityIds(String entityIds) {
        this.entityIds = entityIds;
    }

    public String getEntityNames() {
        return entityNames;
    }

    public void setEntityNames(String entityNames) {
        this.entityNames = entityNames;
    }

    public String getEntityIps() {
        return entityIps;
    }

    public void setEntityIps(String entityIps) {
        this.entityIps = entityIps;
    }

    public int getReportCycle() {
        return reportCycle;
    }

    public void setReportCycle(int reportCycle) {
        this.reportCycle = reportCycle;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public int getStartTime() {
        return startTime;
    }

    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }

    public int getEndTime() {
        return endTime;
    }

    public void setEndTime(int endTime) {
        this.endTime = endTime;
    }

    public Date getStartDay() {
        return startDay;
    }

    public void setStartDay(Date startDay) {
        this.startDay = startDay;
    }

    public Date getEndDay() {
        return endDay;
    }

    public void setEndDay(Date endDay) {
        this.endDay = endDay;
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

    public Integer getRightTopHeight() {
        return rightTopHeight;
    }

    public void setRightTopHeight(Integer rightTopHeight) {
        this.rightTopHeight = rightTopHeight;
    }

    public Integer getMiddleBottomHeight() {
        return middleBottomHeight;
    }

    public void setMiddleBottomHeight(Integer middleBottomHeight) {
        this.middleBottomHeight = middleBottomHeight;
    }

    public Integer getLeftWidth() {
        return leftWidth;
    }

    public void setLeftWidth(Integer leftWidth) {
        this.leftWidth = leftWidth;
    }

    public Integer getRightWidth() {
        return rightWidth;
    }

    public void setRightWidth(Integer rightWidth) {
        this.rightWidth = rightWidth;
    }

    public Boolean getRightTopOpen() {
        return rightTopOpen;
    }

    public void setRightTopOpen(Boolean rightTopOpen) {
        this.rightTopOpen = rightTopOpen;
    }

    public Boolean getRightBottomOpen() {
        return rightBottomOpen;
    }

    public void setRightBottomOpen(Boolean rightBottomOpen) {
        this.rightBottomOpen = rightBottomOpen;
    }

    public Boolean getMiddleBottomOpen() {
        return middleBottomOpen;
    }

    public void setMiddleBottomOpen(Boolean middleBottomOpen) {
        this.middleBottomOpen = middleBottomOpen;
    }

    public Boolean getLayoutToMiddle() {
        return layoutToMiddle;
    }

    public void setLayoutToMiddle(Boolean layoutToMiddle) {
        this.layoutToMiddle = layoutToMiddle;
    }

    public Integer getTabBtnSelectedIndex() {
        return tabBtnSelectedIndex;
    }

    public void setTabBtnSelectedIndex(Integer tabBtnSelectedIndex) {
        this.tabBtnSelectedIndex = tabBtnSelectedIndex;
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

    public Integer getDeviceIgmpMode() {
        return deviceIgmpMode;
    }

    public void setDeviceIgmpMode(Integer deviceIgmpMode) {
        this.deviceIgmpMode = deviceIgmpMode;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public Integer getFftMonitorGlbStatus() {
        return fftMonitorGlbStatus;
    }

    public void setFftMonitorGlbStatus(Integer fftMonitorGlbStatus) {
        this.fftMonitorGlbStatus = fftMonitorGlbStatus;
    }

    public String getCameraSwitch() {
        return cameraSwitch;
    }

    public void setCameraSwitch(String cameraSwitch) {
        this.cameraSwitch = cameraSwitch;
    }

    public Long getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(Long deviceType) {
        this.deviceType = deviceType;
    }

    public Integer getOnlineStatus() {
        return onlineStatus;
    }

    public void setOnlineStatus(Integer onlineStatus) {
        this.onlineStatus = onlineStatus;
    }

    public String getMatchMacAddr() {
        return matchMacAddr;
    }

    public void setMatchMacAddr(String matchMacAddr) {
        this.matchMacAddr = matchMacAddr;
    }

    public int getWanSwitch() {
        return wanSwitch;
    }

    public void setWanSwitch(int wanSwitch) {
        this.wanSwitch = wanSwitch;
    }

    public int getWlanSwitch() {
        return wlanSwitch;
    }

    public void setWlanSwitch(int wlanSwitch) {
        this.wlanSwitch = wlanSwitch;
    }

    public int getCatvSwitch() {
        return catvSwitch;
    }

    public void setCatvSwitch(int catvSwitch) {
        this.catvSwitch = catvSwitch;
    }

    public JSONObject getOnuGlobalCfg() {
        return onuGlobalCfg;
    }

    public void setOnuGlobalCfg(JSONObject onuGlobalCfg) {
        this.onuGlobalCfg = onuGlobalCfg;
    }

}
