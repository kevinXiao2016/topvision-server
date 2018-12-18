package com.topvision.ems.cmc.cm.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.annotation.Resource;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.struts2.ServletActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.topvision.ems.cmc.ccmts.service.CmcService;
import com.topvision.ems.cmc.cm.domain.Cm3Signal;
import com.topvision.ems.cmc.cm.domain.CmLocationInfo;
import com.topvision.ems.cmc.cm.service.CcmtsCmListService;
import com.topvision.ems.cmc.cm.service.CmListService;
import com.topvision.ems.cmc.cm.service.CmService;
import com.topvision.ems.cmc.cm.service.CmSignalService;
import com.topvision.ems.cmc.cpe.service.CpeService;
import com.topvision.ems.cmc.domain.CmcDownChannelBaseShowInfo;
import com.topvision.ems.cmc.downchannel.service.CmcDownChannelService;
import com.topvision.ems.cmc.facade.domain.CmAttribute;
import com.topvision.ems.cmc.facade.domain.CmCpe;
import com.topvision.ems.cmc.upchannel.domain.CmcUpChannelBaseShowInfo;
import com.topvision.ems.cmc.upchannel.service.CmcUpChannelService;
import com.topvision.ems.cmc.webproxy.domain.CmWebProxyModule;
import com.topvision.ems.cmc.webproxy.service.CmWebProxyConfigService;
import com.topvision.ems.devicesupport.version.service.DeviceVersionService;
import com.topvision.ems.facade.domain.EntityType;
import com.topvision.ems.network.service.EntityService;
import com.topvision.ems.template.service.EntityTypeService;
import com.topvision.framework.annotation.OperationLogProperty;
import com.topvision.framework.common.MacUtils;
import com.topvision.framework.utils.CmcIndexUtils;
import com.topvision.framework.web.struts2.BaseAction;
import com.topvision.platform.domain.OperationLog;
import com.topvision.platform.domain.UserContext;
import com.topvision.platform.domain.UserPreferences;
import com.topvision.platform.service.SystemPreferencesService;
import com.topvision.platform.service.UserPreferencesService;
import com.topvision.platform.zetaframework.var.UnitConfigConstant;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Controller("cmListAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class CmListAction extends BaseAction {
    private Logger logger = LoggerFactory.getLogger(CmListAction.class);
    private static final long serialVersionUID = 1L;
    @Resource(name = "cmSignalService")
    private CmSignalService cmSignalService;
    @Resource(name = "cmListService")
    private CmListService cmListService;
    @Resource(name = "entityService")
    private EntityService entityService;
    @Resource(name = "cmcUpChannelService")
    private CmcUpChannelService cmcUpChannelService;
    @Resource(name = "cmcDownChannelService")
    private CmcDownChannelService cmcDownChannelService;
    @Resource(name = "ccmtsCmListService")
    private CcmtsCmListService ccmtsCmListService;
    @Resource(name = "cmService")
    private CmService cmService;
    @Resource(name = "cmcService")
    private CmcService cmcService;
    @Autowired
    private EntityTypeService entityTypeService;
    @Autowired
    private SystemPreferencesService systemPreferencesService;
    @Autowired
    private CpeService cpeService;
    @Autowired
    private UserPreferencesService userPreferencesService;
    @Autowired
    private CmWebProxyConfigService cmWebProxyConfigService;
    @Resource(name = "deviceVersionService")
    private DeviceVersionService deviceVersionService;

    private JSONArray oltPairList;
    private JSONArray cmcPairList_b;
    private JSONArray cmcPairList_d;
    private JSONArray cmtsPairList;

    private boolean hasSupportEpon;
    private boolean cpeClearSurport;
    // CM列表查询方式
    private Boolean simpleModeFlag = true;
    private static final String QUERYMODE_SIMPLE = "Simple";
    private static final String QUERYMODE_ADVANCED = "Advanced";

    private String queryModel;
    private String queryContent;
    private Long deviceType;
    private Long entityId;
    private Long ponId;
    private Long cmcId;
    private Long cmtsId;
    private Integer upChannelId;
    private Integer downChannelId;
    private Long upChannelIndex;
    private Long downChannelIndex;
    private String cmMac;
    private String cmIp;
    private Integer statusValue;
    private Long cmId;
    private Long cmcDeviceStyle;
    private String cmIds;
    private String dwrId;
    private String jconnectedId;
    private String operationId;
    private Long statusIndex;
    private Integer channelType; // 0为上行，1为下行
    private Long channelIndex;
    private Integer channelId;
    private JSONObject queryInitData;
    private String queryInitDataStr;
    private Long upperEntityType;
    private Boolean cpeSwitch = true;
    private Integer docsisMode;
    private String cmServiceType;
    private Integer operationResult;
    private List<Long>entityIds;

    private JSONObject partitionData;
    private String partitionDataStr;
    private Integer upSnrMin;
    private Integer upSnrMax;
    private Integer downSnrMin;
    private Integer downSnrMax;
    private Integer upPowerMin;
    private Integer upPowerMax;
    private Integer downPowerMin;
    private Integer downPowerMax;

    private String userId;
    private String userName;
    private String userAddr;
    private String userPhoneNo;
    private String offerName;
    private String configFile;

    private Integer cmCollectMode; // CM 采集方式，1为直接从CM采集，2为RemoteQuery方式
    private JSONArray entityTypes = new JSONArray();

    private Integer cmPingMode; // CM PING方式， 1为从网管服务器PING， 2为从上联设备PING

    private int start;
    private int limit;

    /**
     * 展示CM列表页面
     * 
     * @return
     */
    public String showCmListPage() {
        Properties properties = systemPreferencesService.getModulePreferences("RemoteQuery");
        String mode = properties.getProperty("RemoteQueryCmMode");
        if (mode != null) {
            cmCollectMode = Integer.valueOf(mode);
        } else {
            cmCollectMode = 1;
        }
        properties = systemPreferencesService.getModulePreferences("toolPing");
        mode = properties.getProperty("Ping.cmping");
        if (mode != null) {
            cmPingMode = Integer.valueOf(mode);
        } else {
            cmPingMode = 1;
        }
        // 如果是由CC/CMTS的portal页查看信道跳转过来，需要加上相应的参数
        if (queryInitDataStr != null && queryInitDataStr != "") {
            queryInitData = JSONObject.fromObject(queryInitDataStr);
        } else {
            queryInitData = new JSONObject();
        }
        UserContext uc = (UserContext) super.getSession().get(UserContext.KEY);
        List<EntityType> entityTypeList = new ArrayList<EntityType>();
        if (uc.hasSupportModule("cmc")) {
            Long cmcType = entityTypeService.getCcmtswithagentType();
            entityTypeList = entityTypeService.loadSubType(cmcType);
        }
        entityTypes = JSONArray.fromObject(entityTypeList);
        try {
            cpeSwitch = cpeService.getCpeCollectConfig().getCpeNumStatisticStatus() == 0;
            String queryMode = getCmQueryView();
            if (QUERYMODE_SIMPLE.equals(queryMode)) {
                simpleModeFlag = true;
            } else {
                simpleModeFlag = false;
            }
        } catch (Exception e) {
            logger.info("", e);
        }
        return SUCCESS;
    }

    /**
     * 获取用户设置的CM列表查询选择
     * 
     * @return
     */
    private String getCmQueryView() {
        UserContext uc = (UserContext) ServletActionContext.getRequest().getSession().getAttribute("UserContext");
        UserPreferences up = new UserPreferences();
        up.setModule("cmQueryView");
        up.setUserId(uc.getUserId());
        Properties cmQueryView = userPreferencesService.getModulePreferences(up);
        return (String) cmQueryView.get("queryMode");
    }

    /**
     * 保存用户设置的CM列表查询选择
     * 
     * @return
     */
    public String saveCmQueryView() {
        UserContext uc = (UserContext) ServletActionContext.getRequest().getSession().getAttribute("UserContext");
        Properties cmQueryView = new Properties();
        if (simpleModeFlag) {
            cmQueryView.setProperty("queryMode", QUERYMODE_SIMPLE);
        } else {
            cmQueryView.setProperty("queryMode", QUERYMODE_ADVANCED);
        }
        userPreferencesService.batchSaveModulePreferences("cmQueryView", uc.getUserId(), cmQueryView);
        return NONE;
    }

    public String loadDeviceListByTypeId() throws IOException {
        JSONArray devices = cmListService.loadDeviceListByTypeId(deviceType);
        devices.write(response.getWriter());
        return NONE;
    }

    public String loadCmCollectMode() throws IOException {
        Properties properties = systemPreferencesService.getModulePreferences("RemoteQuery");
        String mode = properties.getProperty("RemoteQueryCmMode");
        if (mode != null) {
            cmCollectMode = Integer.valueOf(mode);
        } else {
            cmCollectMode = 1;
        }
        writeDataToAjax(cmCollectMode.toString());
        return NONE;
    }

    public String loadDeviceListByType() throws IOException {
        JSONArray devices = cmListService.loadDeviceListByType(deviceType);
        devices.write(response.getWriter());
        return NONE;
    }

    /**
     * 加载PON口下的CCMTS列表
     * 
     * @return
     * @throws IOException
     */
    public String loadCcmtsOfPon() throws IOException {
        JSONArray cmcOfPon = cmListService.loadCcmtsOfPon(entityId, ponId);
        cmcOfPon.write(response.getWriter());
        return NONE;
    }

    /**
     * 加载指定CMTS的上下行信道
     * 
     * @return
     * @throws IOException
     */
    public String loadUpDownChlOfCmts() throws IOException {
        // 获取该CMTS的设备类型
        Long typeId = entityService.getEntity(cmtsId).getTypeId();
        Long type = entityTypeService.getEntityNetworkGroupIdByEntityTypeId(typeId);
        // 查询该CMTS的上行信道列表
        List<CmcUpChannelBaseShowInfo> cmtsUpChannelList = cmcUpChannelService.getUpChannelBaseShowInfoList(cmtsId);
        // 查询该CMTS的下行信道列表
        List<CmcDownChannelBaseShowInfo> cmtsDownChannelList = cmcDownChannelService
                .getDownChannelBaseShowInfoList(cmtsId);
        // 转换上下行信道的index格式
        for (CmcUpChannelBaseShowInfo cmcUpChannelBaseShowInfo : cmtsUpChannelList) {
            if (entityTypeService.isCcmts(type)) {
                cmcUpChannelBaseShowInfo.setIfDescr("US "
                        + CmcIndexUtils.getChannelId(cmcUpChannelBaseShowInfo.getChannelIndex()));
            } else {
                cmcUpChannelBaseShowInfo.setIfDescr(cmcUpChannelBaseShowInfo.getIfName());
            }
        }
        for (CmcDownChannelBaseShowInfo cmcDownChannelBaseShowInfo : cmtsDownChannelList) {
            if (entityTypeService.isCcmts(type)) {
                cmcDownChannelBaseShowInfo.setIfDescr("DS "
                        + CmcIndexUtils.getChannelId(cmcDownChannelBaseShowInfo.getChannelIndex()));
            } else {
                cmcDownChannelBaseShowInfo.setIfDescr(cmcDownChannelBaseShowInfo.getIfName());
            }
        }
        JSONObject json = new JSONObject();
        json.put("cmtsUpChannelList", cmtsUpChannelList);
        json.put("cmtsDownChannelList", cmtsDownChannelList);
        json.write(response.getWriter());
        return NONE;
    }

    /**
     * 查询CM列表
     * 
     * @return
     * @throws IOException
     */
    public String loadCmList() throws IOException {
        // 封装查询条件
        Map<String, Object> queryMap = new HashMap<String, Object>();
        UserContext uc = (UserContext) super.getSession().get(UserContext.KEY);
        String macRule = uc.getMacDisplayStyle();
        queryMap.put("queryModel", queryModel);
        queryMap.put("sort", sort);
        queryMap.put("dir", dir);
        if ("simple".equals(queryModel)) {
            if (queryContent != null && !queryContent.equals("")) {
                // mysql中下划线是特殊的，like的时候必须转义
                if (queryContent.contains("_")) {
                    queryContent = queryContent.replace("_", "\\_");
                }
                queryContent = StringEscapeUtils.escapeSql(queryContent);
                queryMap.put("queryContent", queryContent);
            }
        } else {
            if (deviceType != null && !deviceType.equals(0L) && !deviceType.equals(-1L)) {
                queryMap.put("deviceType", deviceType);
            }
            if (entityId != null && !entityId.equals(-1L) && !entityId.equals(0L)) {
                queryMap.put("entityId", entityId);
            }
            if (ponId != null && !ponId.equals(0L)) {
                queryMap.put("ponId", ponId);
            }
            if (cmtsId != null && !cmtsId.equals(0L)) {
                queryMap.put("cmtsId", cmtsId);
            }
            if (upChannelId != null && !upChannelId.equals(0)) {
                queryMap.put("upChannelId", upChannelId);
            }
            if (downChannelId != null && !downChannelId.equals(0)) {
                queryMap.put("downChannelId", downChannelId);
            }
            if (upChannelIndex != null && !upChannelIndex.equals(0L)) {
                queryMap.put("upChannelIndex", upChannelIndex);
            }
            if (downChannelIndex != null && !downChannelIndex.equals(0L)) {
                queryMap.put("downChannelIndex", downChannelIndex);
            }
            if (docsisMode != null && !docsisMode.equals(0)) {
                queryMap.put("docsisMode", docsisMode);
            }
            if (cmServiceType != null && !"".equals(cmServiceType)) {
                queryMap.put("cmServiceType", cmServiceType);
            }
            if (statusValue != null && !statusValue.equals(0)) {
                queryMap.put("statusValue", statusValue);
            }
            if (cmMac != null && !cmMac.equals("")) {
                String formatQueryMac = MacUtils.formatQueryMac(cmMac);
                if (formatQueryMac.indexOf(":") == -1) {
                    queryMap.put("queryMacWithoutSplit", formatQueryMac);
                }
                queryMap.put("cmMac", formatQueryMac);
            }
            if (cmIp != null && !cmIp.equals("")) {
                queryMap.put("cmIp", cmIp);
            }

            if (userId != null && !userId.equals("")) {
                queryMap.put("userId", userId);
            }
            if (userName != null && !userName.equals("")) {
                queryMap.put("userName", userName);
            }
            if (userAddr != null && !userAddr.equals("")) {
                queryMap.put("userAddr", userAddr);
            }
            if (userPhoneNo != null && !userPhoneNo.equals("")) {
                queryMap.put("userPhoneNo", userPhoneNo);
            }
            if (offerName != null && !offerName.equals("")) {
                queryMap.put("offerName", offerName);
            }
            if (configFile != null && !configFile.equals("")) {
                queryMap.put("configFile", configFile);
            }
        }
        // 在拓扑视图中CM列表查询时会根据上联的cmcId过滤，所以将条件提出来
        if (cmcId != null && !cmcId.equals(0L)) {
            queryMap.put("cmcId", cmcId);
        }
        if (upSnrMin != null) {
            queryMap.put("upSnrMin", upSnrMin);
        }
        if (upSnrMax != null) {
            queryMap.put("upSnrMax", upSnrMax);
        }
        if (downSnrMin != null) {
            queryMap.put("downSnrMin", downSnrMin);
        }
        if (downSnrMax != null) {
            queryMap.put("downSnrMax", downSnrMax);
        }
        if (upPowerMin != null) {
            queryMap.put("upPowerMin", UnitConfigConstant.transPowerToDBmV(upPowerMin));
        }
        if (upPowerMax != null) {
            queryMap.put("upPowerMax", UnitConfigConstant.transPowerToDBmV(upPowerMax));
        }
        if (downPowerMin != null) {
            queryMap.put("downPowerMin", UnitConfigConstant.transPowerToDBmV(downPowerMin));
        }
        if (downPowerMax != null) {
            queryMap.put("downPowerMax", UnitConfigConstant.transPowerToDBmV(downPowerMax));
        }
        queryMap.put("start", start);
        queryMap.put("limit", limit);
        queryMap.put("sort", sort);// 排序字段
        queryMap.put("dir", dir);// 排序方向
        // 查询出分页后的符合查询条件的数据

        List<CmAttribute> attributes = cmListService.getCmList(queryMap);
        List<Long> cmIds = new ArrayList<Long>();
        Boolean cmWebJumpModule = cmWebProxyConfigService.loadCmWebJumpModule().equals(CmWebProxyModule.PROXYJUMP);
        for (CmAttribute attribute : attributes) {
            cmIds.add(attribute.getCmId());
            String formatedMac = MacUtils.convertMacToDisplayFormat(attribute.getStatusMacAddress(), macRule);
            attribute.setStatusMacAddress(formatedMac);
            attribute.setSupportWebProxy(cmWebJumpModule);
        }

        // add by fanzidong，获取多信道信号质量，兼容3.0CM
        if (cmIds.size() > 0) {
            List<Cm3Signal> cm3SignalList = cmListService.getCmSignalByCmIds(cmIds);
            // 组装成cmId-List<Cm3Signal>的形式
            Map<Long, List<Cm3Signal>> upChannelMap = new HashMap<Long, List<Cm3Signal>>();
            Map<Long, List<Cm3Signal>> downChannelMap = new HashMap<Long, List<Cm3Signal>>();
            List<Cm3Signal> curList;
            for (Cm3Signal cm3Signal : cm3SignalList) {
                if (Cm3Signal.CHANNEL_TYPE_UP.equals(cm3Signal.getChannelType())) {
                    // 上行
                    curList = upChannelMap.get(cm3Signal.getCmId());
                    if (curList == null) {
                        curList = new ArrayList<Cm3Signal>();
                        upChannelMap.put(cm3Signal.getCmId(), curList);
                    }
                    curList.add(cm3Signal);
                } else if (Cm3Signal.CHANNEL_TYPE_DOWN.equals(cm3Signal.getChannelType())) {
                    // 下行
                    curList = downChannelMap.get(cm3Signal.getCmId());
                    if (curList == null) {
                        curList = new ArrayList<Cm3Signal>();
                        downChannelMap.put(cm3Signal.getCmId(), curList);
                    }
                    curList.add(cm3Signal);
                }
            }
            // 填充到attributes中
            for (CmAttribute attribute : attributes) {
                attribute.setUpChannelCm3Signal(upChannelMap.get(attribute.getCmId()));
                attribute.setDownChannelCm3Signal(downChannelMap.get(attribute.getCmId()));
            }
        }

        // 获取符合查询条件的数据总数
        Integer cmNum = cmListService.getCmNum(queryMap);
        JSONObject json = new JSONObject();
        json.put("data", attributes);
        json.put("rowCount", cmNum);
        writeDataToAjax(json);
        return NONE;
    }

    public String loadCmcVersion() throws IOException {
        JSONObject json = new JSONObject();
        // 获取版本信息
        json.put("supportStaticIp", deviceVersionService.isFunctionSupported(cmcId, "staticIp"));
        json.put("supportCpeInfo", deviceVersionService.isFunctionSupported(cmcId, "cpeInfo"));
        json.put("supportReset", deviceVersionService.isFunctionSupported(cmcId, "supportReset"));
        json.put("supportCmUpgrade", deviceVersionService.isFunctionSupported(cmcId, "cmUpgrade"));
        json.put("supportClearSingleCm", deviceVersionService.isFunctionSupported(cmcId, "clearSingleCm"));
        json.write(response.getWriter());
        return NONE;
    }

    /**
     * 获取CM定位信息
     * 
     * @return
     */
    public String loadCmLocationInfo() {
        CmLocationInfo cmLocation = cmListService.getCmLocation(cmId, cmcDeviceStyle);
        UserContext uc = (UserContext) super.getSession().get(UserContext.KEY);
        // add by fanzidong,格式化mac地址
        String macRule = uc.getMacDisplayStyle();
        String formatedMac = MacUtils.convertMacToDisplayFormat(cmLocation.getCmLocation().getMac(), macRule);
        cmLocation.getCmLocation().setMac(formatedMac);
        writeDataToAjax(JSONObject.fromObject(cmLocation));
        return NONE;
    }

    public String refreshCm() throws IOException {
        UserContext uc = (UserContext) super.getSession().get(UserContext.KEY);
        cmMac = MacUtils.convertToMaohaoFormat(cmMac);
        JSONObject json = cmListService.refreshCm(cmcId, cmId, statusIndex, cmMac, cmcDeviceStyle, uc);
        json.write(response.getWriter());
        return NONE;
    }

    public String refreshCmList() {
        String message = "success";
        try {
            cmListService.refreshCmList(entityId, cmcId);
        } catch (Exception e) {
            logger.debug("", e);
            message = "fail";
        }
        writeDataToAjax(message);
        return NONE;
    }

    /**
     * 获取CM的数目信息
     * 
     * @return
     * @throws IOException
     */
    public String loadCmNum() throws IOException {
        // 封装查询条件
        Map<String, Object> queryMap = new HashMap<String, Object>();
        queryMap.put("queryModel", queryModel);
        if (queryModel.equals("simple")) {
            if (queryContent != null && !queryContent.equals("")) {
                queryMap.put("queryContent", queryContent);
                String formatQueryMac = MacUtils.formatQueryMac(queryContent);
                if (formatQueryMac.indexOf(":") == -1) {
                    queryMap.put("queryMacWithoutSplit", formatQueryMac);
                }
                queryMap.put("queryContentMac", formatQueryMac);
            }
        } else {
            if (deviceType != null && !deviceType.equals(0L) && !deviceType.equals(-1L)) {
                queryMap.put("deviceType", deviceType);
            }
            if (entityId != null && !entityId.equals(-1L) && !entityId.equals(0L)) {
                queryMap.put("entityId", entityId);
            }
            if (ponId != null && !ponId.equals(0L)) {
                queryMap.put("ponId", ponId);
            }
            if (cmtsId != null && !cmtsId.equals(0L)) {
                queryMap.put("cmtsId", cmtsId);
            }
            if (upChannelId != null && !upChannelId.equals(0)) {
                queryMap.put("upChannelId", upChannelId);
            }
            if (downChannelId != null && !downChannelId.equals(0)) {
                queryMap.put("downChannelId", downChannelId);
            }
            if (upChannelIndex != null && !upChannelIndex.equals(0L)) {
                queryMap.put("upChannelIndex", upChannelIndex);
            }
            if (downChannelIndex != null && !downChannelIndex.equals(0L)) {
                queryMap.put("downChannelIndex", downChannelIndex);
            }
            if (docsisMode != null && !docsisMode.equals(0)) {
                queryMap.put("docsisMode", docsisMode);
            }
            if (statusValue != null && !statusValue.equals(0)) {
                queryMap.put("statusValue", statusValue);
            }
            if (cmMac != null && !cmMac.equals("")) {
                String formatQueryMac = MacUtils.formatQueryMac(cmMac);
                if (formatQueryMac.indexOf(":") == -1) {
                    queryMap.put("queryMacWithoutSplit", formatQueryMac);
                }
                queryMap.put("cmMac", formatQueryMac);
            }
            if (cmIp != null && !cmIp.equals("")) {
                queryMap.put("cmIp", cmIp);
            }

            if (userId != null && !userId.equals("")) {
                queryMap.put("userId", userId);
            }
            if (userName != null && !userName.equals("")) {
                queryMap.put("userName", userName);
            }
            if (userAddr != null && !userAddr.equals("")) {
                queryMap.put("userAddr", userAddr);
            }
            if (userPhoneNo != null && !userPhoneNo.equals("")) {
                queryMap.put("userPhoneNo", userPhoneNo);
            }
            if (offerName != null && !offerName.equals("")) {
                queryMap.put("offerName", offerName);
            }
            if (configFile != null && !configFile.equals("")) {
                queryMap.put("configFile", configFile);
            }
            if (docsisMode != null && !docsisMode.equals(0)) {
                queryMap.put("docsisMode", docsisMode);
            }
            if (cmServiceType != null && !"".equals(cmServiceType)) {
                queryMap.put("cmServiceType", cmServiceType);
            }
        }
        // 在拓扑视图中CM列表查询时会根据上联的cmcId过滤，所以将条件提出来
        if (cmcId != null && !cmcId.equals(0L)) {
            queryMap.put("cmcId", cmcId);
        }
        if (upSnrMin != null) {
            queryMap.put("upSnrMin", upSnrMin);
        }
        if (upSnrMax != null) {
            queryMap.put("upSnrMax", upSnrMax);
        }
        if (downSnrMin != null) {
            queryMap.put("downSnrMin", downSnrMin);
        }
        if (downSnrMax != null) {
            queryMap.put("downSnrMax", downSnrMax);
        }
        if (upPowerMin != null) {
            queryMap.put("upPowerMin", UnitConfigConstant.transPowerToDBmV(upPowerMin));
        }
        if (upPowerMax != null) {
            queryMap.put("upPowerMax", UnitConfigConstant.transPowerToDBmV(upPowerMax));
        }
        if (downPowerMin != null) {
            queryMap.put("downPowerMin", UnitConfigConstant.transPowerToDBmV(downPowerMin));
        }
        if (downPowerMax != null) {
            queryMap.put("downPowerMax", UnitConfigConstant.transPowerToDBmV(downPowerMax));
        }
        // 获取符合查询条件的数据总数
        Integer cmTotalNum = cmListService.getCmNum(queryMap);
        // 获取在线CM数，只需将在线状态设置为online即可
        Integer cmOnlineNum = 0;
        if (statusValue == null || statusValue == 0) {
            // 如果没有选择状态，需要查询在线数目
            queryMap.put("statusValues", "6,21,26,27,30,31");
            cmOnlineNum = cmListService.getCmNum(queryMap);
        } else if (CmAttribute.isCmOnline(statusValue)) {
            // 如果查询条件为在线状态，在线数目就等于总数
            cmOnlineNum = cmTotalNum;
        }
        JSONObject json = new JSONObject();
        json.put("cmTotalNum", cmTotalNum);
        json.put("cmOnlineNum", cmOnlineNum);
        json.write(response.getWriter());
        return NONE;
    }

    /**
     * 重启所有在线CM
     * 
     * @return
     * @throws IOException
     */
    public String refreshAllOnLineCm() throws IOException {
        cmListService.restartCm(cmId, dwrId);
        return NONE;
    }

    /**
     * 全局Global 显示CM CPE信息页面
     * 
     * @return
     */
    public String showCmCpeInfo() {
        Long cmcId = cmService.getCmcIdByCmId(cmId);
        cpeClearSurport = deviceVersionService.isFunctionSupported(cmcId, "clearCpe");
        return SUCCESS;
    }

    /**
     * 全局Global 获取CM下CPE信息
     * 
     * @return
     */
    public String loadCmCpeList() {
        Map<String, Object> json = new HashMap<String, Object>();
        List<CmCpe> cmCpeList = cmService.getCmCpeList(cmId);
        // add by fanzidong
        UserContext uc = (UserContext) super.getSession().get(UserContext.KEY);
        String macRule = uc.getMacDisplayStyle();
        for (CmCpe cmCpe : cmCpeList) {
            String formatedMac = MacUtils.convertMacToDisplayFormat(cmCpe.getTopCmCpeMacAddressString(), macRule);
            cmCpe.setTopCmCpeMacAddressString(formatedMac);
        }
        Integer size = cmCpeList.size();
        json.put("rowCount", size);
        json.put("data", cmCpeList);
        writeDataToAjax(JSONObject.fromObject(json));
        return NONE;
    }

    /**
     * 根据CMC的id去获取对应CCMTS下的CM列表
     * 
     * @return
     * @throws IOException
     */
    public String getCmByCmcId() throws IOException {
        // 查询条件可能有上行信道id、下行信道id、在线状态、分页条件
        // 封装查询条件
        Map<String, Object> queryMap = new HashMap<String, Object>();
        queryMap.put("sort", sort);
        queryMap.put("dir", dir);
        if (upChannelId != null && !upChannelId.equals(0)) {
            queryMap.put("upChannelId", upChannelId);
        }
        if (downChannelId != null && !downChannelId.equals(0)) {
            queryMap.put("downChannelId", downChannelId);
        }
        if (statusValue != null && !statusValue.equals(0)) {
            queryMap.put("statusValue", statusValue);
        }
        if (cmMac != null && !cmMac.equals("")) {
            String formatQueryMac = MacUtils.formatQueryMac(cmMac);
            if (formatQueryMac.indexOf(":") == -1) {
                queryMap.put("queryMacWithoutSplit", formatQueryMac);
            }
            queryMap.put("cmMac", formatQueryMac);
        }

        if (userId != null && !userId.equals("")) {
            queryMap.put("userId", userId);
        }
        if (userName != null && !userName.equals("")) {
            queryMap.put("userName", userName);
        }
        if (userAddr != null && !userAddr.equals("")) {
            queryMap.put("userAddr", userAddr);
        }
        if (userPhoneNo != null && !userPhoneNo.equals("")) {
            queryMap.put("userPhoneNo", userPhoneNo);
        }
        if (offerName != null && !offerName.equals("")) {
            queryMap.put("offerName", offerName);
        }
        if (configFile != null && !configFile.equals("")) {
            queryMap.put("configFile", configFile);
        }
        if (docsisMode != null && !docsisMode.equals(0)) {
            queryMap.put("docsisMode", docsisMode);
        }

        if (upSnrMin != null) {
            queryMap.put("upSnrMin", upSnrMin);
        }
        if (upSnrMax != null) {
            queryMap.put("upSnrMax", upSnrMax);
        }
        if (downSnrMin != null) {
            queryMap.put("downSnrMin", downSnrMin);
        }
        if (downSnrMax != null) {
            queryMap.put("downSnrMax", downSnrMax);
        }
        if (upPowerMin != null) {
            queryMap.put("upPowerMin", UnitConfigConstant.transPowerToDBmV(upPowerMin));
        }
        if (upPowerMax != null) {
            queryMap.put("upPowerMax", UnitConfigConstant.transPowerToDBmV(upPowerMax));
        }
        if (downPowerMin != null) {
            queryMap.put("downPowerMin", UnitConfigConstant.transPowerToDBmV(downPowerMin));
        }
        if (downPowerMax != null) {
            queryMap.put("downPowerMax", UnitConfigConstant.transPowerToDBmV(downPowerMax));
        }
        queryMap.put("cmcId", cmcId);
        queryMap.put("start", start);
        queryMap.put("limit", limit);
        // 根据查询条件获取CM列表
        List<CmAttribute> cmList = cmListService.getCmByCmcId(queryMap);
        Map<Long, String> upMap = new HashMap<Long, String>();
        Map<Long, String> downMap = new HashMap<Long, String>();

        // 如果是CMTS
        Long cmtsType = entityTypeService.getCmtsType();
        if (upperEntityType != null && upperEntityType.equals(cmtsType)) {
            List<CmcUpChannelBaseShowInfo> cmcUpChannelBaseShowInfoList = cmcUpChannelService
                    .getUpChannelBaseShowInfoList(cmcId);
            for (CmcUpChannelBaseShowInfo cmcUpChannelBaseShowInfo : cmcUpChannelBaseShowInfoList) {
                Long ifIndex = cmcUpChannelBaseShowInfo.getChannelIndex();
                upMap.put(ifIndex, cmcUpChannelBaseShowInfo.getIfName());
            }

            List<CmcDownChannelBaseShowInfo> cmcDownChannelBaseShowInfoList = cmcDownChannelService
                    .getDownChannelBaseShowInfoList(cmcId);
            for (CmcDownChannelBaseShowInfo cmcDownChannelBaseShowInfo : cmcDownChannelBaseShowInfoList) {
                Long ifIndex = cmcDownChannelBaseShowInfo.getChannelIndex();
                downMap.put(ifIndex, cmcDownChannelBaseShowInfo.getIfName());
            }
        }

        UserContext uc = (UserContext) getSession().get(UserContext.KEY);
        String macRule = uc.getMacDisplayStyle();
        Map<Long, Boolean> staticIpSupportMap = new HashMap<Long, Boolean>();
        Map<Long, Boolean> cpeSupportMap = new HashMap<Long, Boolean>();
        Map<Long, Boolean> resetSupportMap = new HashMap<Long, Boolean>();
        Map<Long, Boolean> cmUpgradeSupportMap = new HashMap<Long, Boolean>();
        Map<Long, Boolean> clearSingleOfflineCmMap = new HashMap<Long, Boolean>();

        List<Long> cmIds = new ArrayList<Long>();
        Boolean cmWebJumpModule = cmWebProxyConfigService.loadCmWebJumpModule().equals(CmWebProxyModule.PROXYJUMP);
        for (CmAttribute cmAttribute : cmList) {
            // add by fanzidong，格式化MAC地址
            cmIds.add(cmAttribute.getCmId());
            cmAttribute.setCmcId(cmcId);
            String formatedMac = MacUtils.convertMacToDisplayFormat(cmAttribute.getStatusMacAddress(), macRule);
            cmAttribute.setStatusMacAddress(formatedMac);
            cmAttribute.setSupportWebProxy(cmWebJumpModule);
            cmAttribute.setUpChannelIndexString(upMap.get(cmAttribute.getStatusUpChannelIfIndex()));
            cmAttribute.setDownChannelIndexString(downMap.get(cmAttribute.getStatusDownChannelIfIndex()));
            try {
                if (staticIpSupportMap.containsKey(cmAttribute.getCmcId())) {
                    cmAttribute.setSupportStaticIp(staticIpSupportMap.get(cmAttribute.getCmcId()));
                } else {
                    Boolean supportStaticIp = deviceVersionService.isFunctionSupported(cmAttribute.getCmcId(), "staticIp");
                    cmAttribute.setSupportStaticIp(supportStaticIp);
                    staticIpSupportMap.put(cmAttribute.getCmcId(), supportStaticIp);
                }
                if (cpeSupportMap.containsKey(cmAttribute.getCmcId())) {
                    cmAttribute.setSupportCpeInfo(cpeSupportMap.get(cmAttribute.getCmcId()));
                } else {
                    Boolean supportCpeInfo = deviceVersionService.isFunctionSupported(cmAttribute.getCmcId(), "cpeInfo");
                    cmAttribute.setSupportCpeInfo(supportCpeInfo);
                    staticIpSupportMap.put(cmAttribute.getCmcId(), supportCpeInfo);
                }
                if (resetSupportMap.containsKey(cmAttribute.getCmcId())) {
                    cmAttribute.setSupportReset(resetSupportMap.get(cmAttribute.getCmcId()));
                } else {
                    Boolean supportReset = deviceVersionService.isFunctionSupported(cmAttribute.getCmcId(), "resetCm");
                    cmAttribute.setSupportReset(supportReset);
                    resetSupportMap.put(cmAttribute.getCmcId(), supportReset);
                }
                if (cmUpgradeSupportMap.containsKey(cmAttribute.getCmcId())) {
                    cmAttribute.setSupportCmUpgrade(cmUpgradeSupportMap.get(cmAttribute.getCmcId()));
                } else {
                    Boolean supportCmUpgrade = deviceVersionService.isFunctionSupported(cmAttribute.getCmcId(), "cmUpgrade");
                    cmAttribute.setSupportCmUpgrade(supportCmUpgrade);
                    cmUpgradeSupportMap.put(cmAttribute.getCmcId(), supportCmUpgrade);
                }
                if(clearSingleOfflineCmMap.containsKey(cmAttribute.getCmcId())){
                    cmAttribute.setSupportClearSingleCm(clearSingleOfflineCmMap.get(cmAttribute.getCmcId()));
                }else{
                    Boolean supportClearSingleCm = deviceVersionService.isFunctionSupported(cmAttribute.getCmcId(), "clearSingleCm");
                    cmAttribute.setSupportClearSingleCm(supportClearSingleCm);
                    clearSingleOfflineCmMap.put(cmAttribute.getCmcId(), supportClearSingleCm);
                }
            } catch (Exception e) {
                continue;
            }
        }

        // add by fanzidong，获取多信道信号质量，兼容3.0CM
        if (cmIds.size() > 0) {
            List<Cm3Signal> cm3SignalList = cmListService.getCmSignalByCmIds(cmIds);
            // 组装成cmId-List<Cm3Signal>的形式
            Map<Long, List<Cm3Signal>> upChannelMap = new HashMap<Long, List<Cm3Signal>>();
            Map<Long, List<Cm3Signal>> downChannelMap = new HashMap<Long, List<Cm3Signal>>();
            List<Cm3Signal> curList;
            for (Cm3Signal cm3Signal : cm3SignalList) {
                if (Cm3Signal.CHANNEL_TYPE_UP.equals(cm3Signal.getChannelType())) {
                    // 上行
                    curList = upChannelMap.get(cm3Signal.getCmId());
                    if (curList == null) {
                        curList = new ArrayList<Cm3Signal>();
                        upChannelMap.put(cm3Signal.getCmId(), curList);
                    }
                    curList.add(cm3Signal);
                } else if (Cm3Signal.CHANNEL_TYPE_DOWN.equals(cm3Signal.getChannelType())) {
                    // 下行
                    curList = downChannelMap.get(cm3Signal.getCmId());
                    if (curList == null) {
                        curList = new ArrayList<Cm3Signal>();
                        downChannelMap.put(cm3Signal.getCmId(), curList);
                    }
                    curList.add(cm3Signal);
                }
            }
            // 填充到attributes中
            for (CmAttribute attribute : cmList) {
                attribute.setUpChannelCm3Signal(upChannelMap.get(attribute.getCmId()));
                attribute.setDownChannelCm3Signal(downChannelMap.get(attribute.getCmId()));
            }
        }
        // 获取符合查询条件的CM总数
        Integer cmNum = cmListService.getCmNumByCmcId(queryMap);
        // 返回数据
        JSONObject json = new JSONObject();
        json.put("data", cmList);
        json.put("rowCount", cmNum);
        json.write(response.getWriter());
        return NONE;
    }

    public String loadCmNumUnderCcmts() throws IOException {
        Map<String, Object> queryMap = new HashMap<String, Object>();
        if (upChannelId != null && !upChannelId.equals(0)) {
            queryMap.put("upChannelId", upChannelId);
        }
        if (downChannelId != null && !downChannelId.equals(0)) {
            queryMap.put("downChannelId", downChannelId);
        }
        if (statusValue != null && !statusValue.equals(0)) {
            queryMap.put("statusValue", statusValue);
        }
        if (userId != null && !userId.equals("")) {
            queryMap.put("userId", userId);
        }
        if (userName != null && !userName.equals("")) {
            queryMap.put("userName", userName);
        }
        if (userAddr != null && !userAddr.equals("")) {
            queryMap.put("userAddr", userAddr);
        }
        if (userPhoneNo != null && !userPhoneNo.equals("")) {
            queryMap.put("userPhoneNo", userPhoneNo);
        }
        if (offerName != null && !offerName.equals("")) {
            queryMap.put("offerName", offerName);
        }
        if (configFile != null && !configFile.equals("")) {
            queryMap.put("configFile", configFile);
        }
        if (cmMac != null && !cmMac.equals("")) {
            String formatQueryMac = MacUtils.formatQueryMac(cmMac);
            if (formatQueryMac.indexOf(":") == -1) {
                queryMap.put("queryMacWithoutSplit", formatQueryMac);
            }
            queryMap.put("cmMac", formatQueryMac);
        }
        // add by fanzidong，个数统计加上指标分区间条件
        if (upSnrMin != null) {
            queryMap.put("upSnrMin", upSnrMin);
        }
        if (upSnrMax != null) {
            queryMap.put("upSnrMax", upSnrMax);
        }
        if (downSnrMin != null) {
            queryMap.put("downSnrMin", downSnrMin);
        }
        if (downSnrMax != null) {
            queryMap.put("downSnrMax", downSnrMax);
        }
        if (upPowerMin != null) {
            queryMap.put("upPowerMin", UnitConfigConstant.transPowerToDBmV(upPowerMin));
        }
        if (upPowerMax != null) {
            queryMap.put("upPowerMax", UnitConfigConstant.transPowerToDBmV(upPowerMax));
        }
        if (downPowerMin != null) {
            queryMap.put("downPowerMin", UnitConfigConstant.transPowerToDBmV(downPowerMin));
        }
        if (downPowerMax != null) {
            queryMap.put("downPowerMax", UnitConfigConstant.transPowerToDBmV(downPowerMax));
        }
        queryMap.put("cmcId", cmcId);
        // 获取符合查询条件的CM总数
        Integer totalNum = cmListService.getCmNumByCmcId(queryMap);
        // 获取符合查询条件的在线CM总数
        Integer onlineNum = 0;
        if (statusValue == null || statusValue == 0) {
            // 如果没有选择状态，需要查询在线数目
            queryMap.put("statusValues", "6,21,26,27,30,31");
            onlineNum = cmListService.getCmNumByCmcId(queryMap);
        } else if (CmAttribute.isCmOnline(statusValue)) {
            // 如果查询条件为在线状态，在线数目就等于总数
            onlineNum = totalNum;
        }
        JSONObject json = new JSONObject();
        json.put("totalNum", totalNum);
        json.put("onlineNum", onlineNum);
        json.write(response.getWriter());
        return NONE;
    }

    public String refreshCmSignal() {
        UserContext uc = (UserContext) getSession().get(UserContext.KEY);
        cmSignalService.getSignalAndPush(cmIds, jconnectedId, operationId, uc);
        return NONE;
    }

    public String resetOnLineCm() {
        String[] cmIdArr = cmIds.split(",");
        for (int i = 0; i < cmIdArr.length; i++) {
            cmId = Long.parseLong(cmIdArr[i]);
            cmListService.restartCm(cmId, dwrId);
        }
        return NONE;
    }

    @OperationLogProperty(actionName="cmListAction" ,operationName="clearOfflineCm")
    public String clearSingleCM() {
        String[] cmIdArr = cmIds.split(",");
        entityIds=new ArrayList<Long>();
        List<Long> isClearedList = new ArrayList<Long>();
        JSONObject result = new JSONObject();       
        try {
            for (int i = 0; i < cmIdArr.length; i++) {
                cmId = Long.parseLong(cmIdArr[i]);
                cmcId = cmService.getCmcIdByCmId(cmId);
                entityId = cmcService.getEntityIdByCmcId(cmcId);
                if (!entityIds.contains(entityId)) {
                    entityIds.add(entityId);
                }
                int res=cmListService.clearSingleCm(cmId);
                if(res==0){
                    isClearedList.add(cmId);
                 // 清除成功一个就删除数据库
                    cmService.deleteCmClearedOne(cmId);
                }
            }
            // 在数据库中删除被清除成功的CM
//            if(isClearedList.size()!=0){
//                cmService.deleteCmCleared(isClearedList); 
//            }
            operationResult = OperationLog.SUCCESS;
            result.put("success", true);
        } catch (Exception e) {
            logger.warn("", e);
            operationResult = OperationLog.FAILURE;
            result.put("success", false);
        }
        result.put("clearSuccess", isClearedList.size());
        writeDataToAjax(result);
        return NONE;
    }

    public String showPartitionSelect() {
        try {
            partitionData = JSONObject.fromObject(partitionDataStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return SUCCESS;
    }
    public String showPartitionSelect2() {
        try {
            partitionData = JSONObject.fromObject(partitionDataStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return SUCCESS;
    }

    /**
     * @return the cmPingMode
     */
    public Integer getCmPingMode() {
        return cmPingMode;
    }

    /**
     * @param cmPingMode
     *            the cmPingMode to set
     */
    public void setCmPingMode(Integer cmPingMode) {
        this.cmPingMode = cmPingMode;
    }

    public JSONArray getOltPairList() {
        return oltPairList;
    }

    public void setOltPairList(JSONArray oltPairList) {
        this.oltPairList = oltPairList;
    }

    public JSONArray getCmcPairList_b() {
        return cmcPairList_b;
    }

    public void setCmcPairList_b(JSONArray cmcPairList_b) {
        this.cmcPairList_b = cmcPairList_b;
    }

    public JSONArray getCmtsPairList() {
        return cmtsPairList;
    }

    public void setCmtsPairList(JSONArray cmtsPairList) {
        this.cmtsPairList = cmtsPairList;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public Long getPonId() {
        return ponId;
    }

    public void setPonId(Long ponId) {
        this.ponId = ponId;
    }

    public Long getCmtsId() {
        return cmtsId;
    }

    public void setCmtsId(Long cmtsId) {
        this.cmtsId = cmtsId;
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

    public Integer getStatusValue() {
        return statusValue;
    }

    public void setStatusValue(Integer statusValue) {
        this.statusValue = statusValue;
    }

    public String getCmMac() {
        return cmMac;
    }

    public void setCmMac(String cmMac) {
        this.cmMac = cmMac;
    }

    public Long getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(Long deviceType) {
        this.deviceType = deviceType;
    }

    public Long getCmcId() {
        return cmcId;
    }

    public void setCmcId(Long cmcId) {
        this.cmcId = cmcId;
    }

    public Integer getUpChannelId() {
        return upChannelId;
    }

    public void setUpChannelId(Integer upChannelId) {
        this.upChannelId = upChannelId;
    }

    public Integer getDownChannelId() {
        return downChannelId;
    }

    public void setDownChannelId(Integer downChannelId) {
        this.downChannelId = downChannelId;
    }

    public boolean isHasSupportEpon() {
        return hasSupportEpon;
    }

    public void setHasSupportEpon(boolean hasSupportEpon) {
        this.hasSupportEpon = hasSupportEpon;
    }

    public Long getUpChannelIndex() {
        return upChannelIndex;
    }

    public void setUpChannelIndex(Long upChannelIndex) {
        this.upChannelIndex = upChannelIndex;
    }

    public Long getDownChannelIndex() {
        return downChannelIndex;
    }

    public void setDownChannelIndex(Long downChannelIndex) {
        this.downChannelIndex = downChannelIndex;
    }

    public Long getCmId() {
        return cmId;
    }

    public void setCmId(Long cmId) {
        this.cmId = cmId;
    }

    public Long getCmcDeviceStyle() {
        return cmcDeviceStyle;
    }

    public void setCmcDeviceStyle(Long cmcDeviceStyle) {
        this.cmcDeviceStyle = cmcDeviceStyle;
    }

    public String getCmIds() {
        return cmIds;
    }

    public void setCmIds(String cmIds) {
        this.cmIds = cmIds;
    }

    public String getDwrId() {
        return dwrId;
    }

    public void setDwrId(String dwrId) {
        this.dwrId = dwrId;
    }

    public String getQueryModel() {
        return queryModel;
    }

    public void setQueryModel(String queryModel) {
        this.queryModel = queryModel;
    }

    public String getQueryContent() {
        return queryContent;
    }

    public void setQueryContent(String queryContent) {
        this.queryContent = queryContent;
    }

    public Long getStatusIndex() {
        return statusIndex;
    }

    public void setStatusIndex(Long statusIndex) {
        this.statusIndex = statusIndex;
    }

    public String getCmIp() {
        return cmIp;
    }

    public void setCmIp(String cmIp) {
        this.cmIp = cmIp;
    }

    public Integer getChannelType() {
        return channelType;
    }

    public void setChannelType(Integer channelType) {
        this.channelType = channelType;
    }

    public Long getChannelIndex() {
        return channelIndex;
    }

    public void setChannelIndex(Long channelIndex) {
        this.channelIndex = channelIndex;
    }

    public JSONObject getQueryInitData() {
        return queryInitData;
    }

    public void setQueryInitData(JSONObject queryInitData) {
        this.queryInitData = queryInitData;
    }

    public Integer getChannelId() {
        return channelId;
    }

    public void setChannelId(Integer channelId) {
        this.channelId = channelId;
    }

    public JSONArray getCmcPairList_d() {
        return cmcPairList_d;
    }

    public void setCmcPairList_d(JSONArray cmcPairList_d) {
        this.cmcPairList_d = cmcPairList_d;
    }

    public boolean isCpeClearSurport() {
        return cpeClearSurport;
    }

    public void setCpeClearSurport(boolean cpeClearSurport) {
        this.cpeClearSurport = cpeClearSurport;
    }

    public String getQueryInitDataStr() {
        return queryInitDataStr;
    }

    public void setQueryInitDataStr(String queryInitDataStr) {
        this.queryInitDataStr = queryInitDataStr;
    }

    public Long getUpperEntityType() {
        return upperEntityType;
    }

    public void setUpperEntityType(Long upperEntityType) {
        this.upperEntityType = upperEntityType;
    }

    public Integer getCmCollectMode() {
        return cmCollectMode;
    }

    public void setCmCollectMode(Integer cmCollectMode) {
        this.cmCollectMode = cmCollectMode;
    }

    public String getPartitionDataStr() {
        return partitionDataStr;
    }

    public void setPartitionDataStr(String partitionDataStr) {
        this.partitionDataStr = partitionDataStr;
    }

    public JSONObject getPartitionData() {
        return partitionData;
    }

    public void setPartitionData(JSONObject partitionData) {
        this.partitionData = partitionData;
    }

    public Integer getUpSnrMin() {
        return upSnrMin;
    }

    public void setUpSnrMin(Integer upSnrMin) {
        this.upSnrMin = upSnrMin;
    }

    public Integer getUpSnrMax() {
        return upSnrMax;
    }

    public void setUpSnrMax(Integer upSnrMax) {
        this.upSnrMax = upSnrMax;
    }

    public Integer getDownSnrMin() {
        return downSnrMin;
    }

    public void setDownSnrMin(Integer downSnrMin) {
        this.downSnrMin = downSnrMin;
    }

    public Integer getDownSnrMax() {
        return downSnrMax;
    }

    public void setDownSnrMax(Integer downSnrMax) {
        this.downSnrMax = downSnrMax;
    }

    public Integer getUpPowerMin() {
        return upPowerMin;
    }

    public void setUpPowerMin(Integer upPowerMin) {
        this.upPowerMin = upPowerMin;
    }

    public Integer getUpPowerMax() {
        return upPowerMax;
    }

    public void setUpPowerMax(Integer upPowerMax) {
        this.upPowerMax = upPowerMax;
    }

    public Integer getDownPowerMin() {
        return downPowerMin;
    }

    public void setDownPowerMin(Integer downPowerMin) {
        this.downPowerMin = downPowerMin;
    }

    public Integer getDownPowerMax() {
        return downPowerMax;
    }

    public void setDownPowerMax(Integer downPowerMax) {
        this.downPowerMax = downPowerMax;
    }

    public JSONArray getEntityTypes() {
        return entityTypes;
    }

    public void setEntityTypes(JSONArray entityTypes) {
        this.entityTypes = entityTypes;
    }

    public Boolean getCpeSwitch() {
        return cpeSwitch;
    }

    public Boolean isCpeSwitch() {
        return cpeSwitch;
    }

    public void setCpeSwitch(Boolean cpeSwitch) {
        this.cpeSwitch = cpeSwitch;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserAddr() {
        return userAddr;
    }

    public void setUserAddr(String userAddr) {
        this.userAddr = userAddr;
    }

    public String getUserPhoneNo() {
        return userPhoneNo;
    }

    public void setUserPhoneNo(String userPhoneNo) {
        this.userPhoneNo = userPhoneNo;
    }

    public String getOfferName() {
        return offerName;
    }

    public void setOfferName(String offerName) {
        this.offerName = offerName;
    }

    public String getConfigFile() {
        return configFile;
    }

    public void setConfigFile(String configFile) {
        this.configFile = configFile;
    }

    public Boolean getSimpleModeFlag() {
        return simpleModeFlag;
    }

    public void setSimpleModeFlag(Boolean simpleModeFlag) {
        this.simpleModeFlag = simpleModeFlag;
    }

    public Integer getDocsisMode() {
        return docsisMode;
    }

    public void setDocsisMode(Integer docsisMode) {
        this.docsisMode = docsisMode;
    }

    public String getJconnectedId() {
        return jconnectedId;
    }

    public void setJconnectedId(String jconnectedId) {
        this.jconnectedId = jconnectedId;
    }

    /**
     * @return the cmServiceType
     */
    public String getCmServiceType() {
        return cmServiceType;
    }

    /**
     * @param cmServiceType
     *            the cmServiceType to set
     */
    public void setCmServiceType(String cmServiceType) {
        this.cmServiceType = cmServiceType;
    }

    public String getOperationId() {
        return operationId;
    }

    public void setOperationId(String operationId) {
        this.operationId = operationId;
    }

    public Integer getOperationResult() {
        return operationResult;
    }

    public void setOperationResult(Integer operationResult) {
        this.operationResult = operationResult;
    }
    

    public List<Long> getEntityIds() {
        return entityIds;
    }

    public void setEntityIds(List<Long> entityIds) {
        this.entityIds = entityIds;
    }


}