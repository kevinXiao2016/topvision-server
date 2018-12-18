package com.topvision.ems.cmc.cm.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;

import com.topvision.ems.cmc.ccmts.facade.domain.CmcAttribute;
import com.topvision.ems.cmc.ccmts.service.CmcService;
import com.topvision.ems.cmc.cm.service.CcmtsCmListService;
import com.topvision.ems.cmc.cm.service.CmListService;
import com.topvision.ems.cmc.cm.service.CmService;
import com.topvision.ems.cmc.cm.service.CmSignalService;
import com.topvision.ems.cmc.cpe.service.CpeService;
import com.topvision.ems.cmc.domain.CmcDownChannelBaseShowInfo;
import com.topvision.ems.cmc.downchannel.service.CmcDownChannelService;
import com.topvision.ems.cmc.facade.domain.CmAttribute;
import com.topvision.ems.cmc.upchannel.domain.CmcUpChannelBaseShowInfo;
import com.topvision.ems.cmc.upchannel.service.CmcUpChannelService;
import com.topvision.ems.cmc.util.StringUtils;
import com.topvision.ems.devicesupport.version.service.DeviceVersionService;
import com.topvision.ems.template.service.EntityTypeService;
import com.topvision.framework.annotation.OperationLogProperty;
import com.topvision.framework.common.MacUtils;
import com.topvision.framework.constants.Symbol;
import com.topvision.framework.exception.engine.SnmpException;
import com.topvision.framework.utils.CmcIndexUtils;
import com.topvision.framework.web.struts2.BaseAction;
import com.topvision.platform.domain.OperationLog;
import com.topvision.platform.domain.UserContext;
import com.topvision.platform.service.SystemPreferencesService;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 
 * @author YangYi
 * @created @2013-10-10-下午4:43:48 从CmAction中拆分，级联CM模块
 */
@Controller("ccmtsCmListAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class CcmtsCmListAction extends BaseAction {
    private static final long serialVersionUID = 1L;
    Logger logger = LoggerFactory.getLogger(this.getClass());
    @Resource(name = "cmService")
    private CmService cmService;
    @Autowired
    private EntityTypeService entityTypeService;
    @Resource(name = "deviceVersionService")
    private DeviceVersionService deviceVersionService;
    @Autowired
    private SystemPreferencesService systemPreferencesService;
    @Autowired
    private CmListService cmListService;
    @Resource(name = "cmcService")
    private CmcService cmcService;
    @Resource(name = "ccmtsCmListService")
    private CcmtsCmListService ccmtsCmListService;
    @Resource(name = "cmSignalService")
    private CmSignalService cmSignalService;
    @Autowired
    private CmcUpChannelService cmcUpChannelService;
    @Autowired
    private CmcDownChannelService cmcDownChannelService;
    @Autowired
    private CpeService cpeService;
    private int start;
    private int limit;
    private boolean hasSupportEpon;
    private boolean signalRefreshFlag;
    protected Integer operationResult;
    private Long cmId;
    private Long cmcId;
    private Long ponId;
    private Long totalNum;
    private Long entityId;
    private Long deniedNum;
    private Long deviceType;
    private Long upChannelId;
    private Long downChannelId;
    private Long registeredNum;
    private String dir;
    private String sort;
    private String cmIp;
    private String cmMac;
    private String cpeIp;
    private String status;
    private String source;
    private String macForSignal;// YangYi增加@20130827,刷新实时信号CM的MAC地址
    private String dwrId;// YangYi增加@20130828,推送DWR的ID
    private String jConnectedId;
    private String cmIds;// YangYi增加@20150926,用于刷新实时信号质量批量推送CMID
    private JSONObject cmFunctionJson = new JSONObject();
    private CmAttribute cmAttribute;
    private Integer productType;
    private Boolean cpeSwitch = true;
    private CmcAttribute cmcAttribute;

    private Integer cmPingMode; // CM PING方式， 1为从网管服务器PING， 2为从上联设备PING

    private JSONArray upChannels = new JSONArray();
    private JSONArray downChannels = new JSONArray();

    /**
     * 2 关联Contacted 显示CM列表tab页面
     * 
     * @return String
     */
    public String showContactedCmList() {
        setCmcAttribute(cmcService.getCmcAttributeByCmcId(cmcId));
        // 获取当前设备的信道列表
        List<CmcUpChannelBaseShowInfo> cmcUpChannelBaseShowInfoList = cmcUpChannelService
                .getUpChannelBaseShowInfoList(cmcId);
        List<CmcDownChannelBaseShowInfo> cmcDownChannelBaseShowInfoList = cmcDownChannelService
                .getDownChannelBaseShowInfoList(cmcId);
        for (CmcUpChannelBaseShowInfo upChl : cmcUpChannelBaseShowInfoList) {
            upChannels.add(upChl.getChannelId());
        }
        for (CmcDownChannelBaseShowInfo downChl : cmcDownChannelBaseShowInfoList) {
            downChannels.add(downChl.getDocsIfDownChannelId());
        }
        // CM列表支持的功能
        Map<String, String> cmTmp = new HashMap<String, String>();
        cmTmp.put("reboot", deviceVersionService.getParamValue(cmcId, "cm", "reboot"));
        cmTmp.put("clearOfflineCm", String.valueOf(deviceVersionService.isFunctionSupported(cmcId, "clearOfflineCm")));
        cmTmp.put("clearSingleCm", String.valueOf(deviceVersionService.isFunctionSupported(cmcId, "clearSingleCm")));
        cmFunctionJson.putAll(cmTmp);
        try {
            cpeSwitch = cpeService.getCpeCollectConfig().getCpeNumStatisticStatus() == 0;
        } catch (Exception e) {
            logger.info("", e);
        }
        Properties properties = systemPreferencesService.getModulePreferences("toolPing");
        String mode = properties.getProperty("Ping.cmping");
        if (mode != null) {
            cmPingMode = Integer.valueOf(mode);
        } else {
            cmPingMode = 1;
        }
        return SUCCESS;
    }

    /**
     * 显示CM关联服务流信息页面
     * 
     * @return String
     */
    public String showServiceFlowContactedToCm() {
        return SUCCESS;
    }

    /**
     * 获取CM统计值，根据过滤条件查询CM的统计值
     * 
     * YangYi 修改 2013-10-19 针对CTMS下的CM总数、在线数
     * 
     * @return String
     */
    public String getCmStatusNum() {
        Map<String, Object> cmQueryMap = new HashMap<String, Object>();
        Map<String, Long> result = new HashMap<String, Long>();
        if (deviceType != null && deviceType > 0) {
            cmQueryMap.put("deviceType", deviceType.toString());
        }
        if (entityId != null && entityId > 0) {
            cmQueryMap.put("entityId", entityId.toString());
        }
        if (ponId != null && ponId > 0) {
            cmQueryMap.put("ponId", ponId.toString());
        }
        if (cmcId != null && cmcId > 0) {
            cmQueryMap.put("cmcId", cmcId.toString());
        }
        if (upChannelId != null && upChannelId > 0) {
            cmQueryMap.put("channelId", upChannelId.toString());
        }
        if (downChannelId != null && downChannelId > 0) {
            cmQueryMap.put("docsIfDownChannelId", downChannelId.toString());
        }
        if (cmMac != null && cmMac.length() > 0) {
            cmQueryMap.put("cmMac", MacUtils.convertToMaohaoFormat(cmMac));
        }
        // 查询CMTS下CM总数、在线数
        if (entityTypeService.isCmts(deviceType)) {
            cmQueryMap.put("upChannelIndex", cmQueryMap.get("channelId"));
            cmQueryMap.put("downChannelIndex", cmQueryMap.get("docsIfDownChannelId"));
            totalNum = cmService.getCmtsCmNumByStatus(cmQueryMap);
            cmQueryMap.put("status", Integer.toString(6));
            registeredNum = cmService.getCmtsCmNumByStatus(cmQueryMap);
            cmQueryMap.remove("status");
            cmQueryMap.put("status", Integer.toString(7));
            deniedNum = cmService.getCmtsCmNumByStatus(cmQueryMap);
        } else { // 查询CCMTS下CM总数、在线数
            totalNum = cmService.getCmNumByStatus(cmQueryMap);
            cmQueryMap.put("status", Integer.toString(6));
            registeredNum = cmService.getCmNumByStatus(cmQueryMap);
            cmQueryMap.remove("status");
            cmQueryMap.put("status", Integer.toString(7));
            deniedNum = cmService.getCmNumByStatus(cmQueryMap);
        }
        result.put("totalNum", totalNum);
        result.put("registeredNum", registeredNum);
        result.put("deniedNum", deniedNum);
        writeDataToAjax(JSONObject.fromObject(result));
        return NONE;
    }

    /**
     * 2 关联Contacted 刷新在CCMTS上的CM信息
     * 
     * @return String
     */
    @OperationLogProperty(actionName = "cmAction", operationName = "refreshCmOnCcmtsInfo${source}")
    public String refreshCmOnCcmtsInfo() {
        String message;
        entityId = cmcService.getEntityIdByCmcId(cmcId);
        Long cmcIndex = cmcService.getCmcIndexByCmcId(cmcId);
        source = Symbol.BRACKET_LEFT + CmcIndexUtils.getSlotNo(cmcIndex).toString() + Symbol.SLASH
                + CmcIndexUtils.getPonNo(cmcIndex).toString() + Symbol.SLASH
                + CmcIndexUtils.getCmcId(cmcIndex).toString() + Symbol.BRACKET_RIGHT;
        try {
            ccmtsCmListService.refreshContactedCmList(cmcId, cmcIndex);
            message = "true";
            operationResult = OperationLog.SUCCESS;
        } catch (Exception e) {
            message = e.getMessage();
            operationResult = OperationLog.FAILURE;
            logger.debug("", e);
        }
        writeDataToAjax(message);
        return NONE;
    }

    /**
     * 2 关联Contacted 剔除下线CM
     * 
     * @return String
     */
    public String offlineCmAll() {
        JSONObject result = new JSONObject();
        try {
            Assert.notNull(cmcId);
            try {
                // 在设备上执行剔除下线CM命令
                ccmtsCmListService.clearAllOfflineCmsOnCC(cmcId);
                // 刷新CM信息(在设备上执行清除CM命令后，需要将cm信息刷一下)
                ccmtsCmListService.refreshContactedCmList(cmcId, cmcService.getCmcIndexByCmcId(cmcId));
                /* 删除数据库中online之外的的cm */
                ccmtsCmListService.deleteCmcOfflineCm(cmcId);
                result.put("success", true);
            } catch (SnmpException snmpException) {
                // 低版本的CC没有mib节点，会报错（兼容低版本CC）
                logger.warn("", snmpException);
                result.put("success", false);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            result.put("success", false);
        }
        writeDataToAjax(result);
        return NONE;
    }

    /**
     * 2 关联Contacted 重启CCMTS下所有CM
     * 
     * @return
     */
    public String restartAllCm() {
        JSONObject result = new JSONObject();
        // 需要细分异常
        // 在设备上执行重启CM命令
        try {
            ccmtsCmListService.restartAllCm(cmcId);
            result.put("restart", true);
            try {
                // 刷新CM信息(在设备上执行完重启CM命令后，需要将cm信息刷一下)
                ccmtsCmListService.refreshContactedCmList(cmcId, cmcService.getCmcIndexByCmcId(cmcId));
                result.put("refresh", true);
                result.put("success", true);
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
                result.put("success", false);
                result.put("refresh", false);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            result.put("success", false);
            result.put("restart", false);
        }
        writeDataToAjax(result);
        /*
         * try { //注释by fanzidong，发布版中怎么能出现assert... //Assert.notNull(cmcId); // 在设备上执行重启CM命令
         * ccmtsCmListService.restartAllCm(cmcId); // 刷新CM信息(在设备上执行完重启CM命令后，需要将cm信息刷一下)
         * ccmtsCmListService.refreshContactedCmList(cmcId, cmcService.getCmcIndexByCmcId(cmcId));
         * result.put("success", true); writeDataToAjax(result); } catch (Exception e) {
         * logger.error(e.getMessage(), e); result.put("success", false); writeDataToAjax(result); }
         */
        return NONE;
    }

    /**
     * 2 关联Contacted
     * 
     * @return
     */
    public String restartCmByMac() {
        JSONObject result = new JSONObject();
        try {
            Assert.notNull(cmcId, cmMac);
            // 在设备上执行重启CM命令
            ccmtsCmListService.restartCmByMac(cmcId, cmMac);
            result.put("success", true);
            writeDataToAjax(result);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            result.put("success", false);
            writeDataToAjax(result);
        }
        return NONE;
    }

    /**
     * 2 关联Contacted 查询CM列表
     * 
     * @return String
     */
    public String queryForCmList() {
        // 改为一个方法
        Map<String, Object> cmQueryMap = new HashMap<String, Object>();
        Map<String, Object> json = new HashMap<String, Object>();
        UserContext uc = (UserContext) super.getSession().get(UserContext.KEY);
        cmQueryMap.put("sort", sort);
        cmQueryMap.put("dir", dir);
        // 查询cm属性列表的条件
        if (deviceType != null && deviceType > 0) {
            cmQueryMap.put("deviceType", deviceType.toString());
        }
        if (entityId != null && entityId > 0) {
            cmQueryMap.put("entityId", entityId.toString());
        }
        if (ponId != null && ponId > 0) {
            cmQueryMap.put("ponId", ponId.toString());
        }
        if (cmcId != null && cmcId > 0) {
            cmQueryMap.put("cmcId", cmcId.toString());
        }
        if (upChannelId != null && upChannelId > 0) {
            cmQueryMap.put("channelId", upChannelId.toString());
        }
        if (downChannelId != null && downChannelId > 0) {
            cmQueryMap.put("docsIfDownChannelId", downChannelId.toString());
        }
        if (!StringUtils.isEmpty(cmMac)) {
            cmQueryMap.put("cmMac", MacUtils.convertToMaohaoFormat(cmMac));
        }
        if (cpeIp != null) {
            List<CmAttribute> cmAttributeList = ccmtsCmListService.showCmByCpeIp(cpeIp);
            // add by fanzidong,格式化mac地址
            String macRule = uc.getMacDisplayStyle();
            for (CmAttribute cm : cmAttributeList) {
                String formatedMac = MacUtils.convertMacToDisplayFormat(cm.getStatusMacAddress(), macRule);
                cm.setStatusMacAddress(formatedMac);
            }
            json = new HashMap<String, Object>();
            json.put("data", cmAttributeList);
            json.put("rowCount", cmAttributeList.size());
            writeDataToAjax(JSONObject.fromObject(json));
            return NONE;
        }
        cmQueryMap.put("start", start);
        cmQueryMap.put("limit", limit);
        // 查询出分页后的符合查询条件的数据
        List<CmAttribute> cmAttributeList = cmListService.getCmList(cmQueryMap);
        // add by fanzidong,格式化mac地址
        String macRule = uc.getMacDisplayStyle();
        for (CmAttribute cm : cmAttributeList) {
            String formatedMac = MacUtils.convertMacToDisplayFormat(cm.getStatusMacAddress(), macRule);
            cm.setStatusMacAddress(formatedMac);
        }
        // 查询cm属性列表数目
        // Long num = cmService.getCmNum(cmQueryMap);这个方法及其下级级联方法考虑删除
        Integer num = cmListService.getCmNum(cmQueryMap);
        json.put("data", cmAttributeList);
        json.put("rowCount", num);
        // json.put("rowCount", cmAttributeList.size());
        writeDataToAjax(JSONObject.fromObject(json));
        return NONE;
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

    public boolean isHasSupportEpon() {
        return hasSupportEpon;
    }

    public void setHasSupportEpon(boolean hasSupportEpon) {
        this.hasSupportEpon = hasSupportEpon;
    }

    public boolean isSignalRefreshFlag() {
        return signalRefreshFlag;
    }

    public void setSignalRefreshFlag(boolean signalRefreshFlag) {
        this.signalRefreshFlag = signalRefreshFlag;
    }

    public Integer getOperationResult() {
        return operationResult;
    }

    public void setOperationResult(Integer operationResult) {
        this.operationResult = operationResult;
    }

    public Long getCmId() {
        return cmId;
    }

    public void setCmId(Long cmId) {
        this.cmId = cmId;
    }

    public Long getCmcId() {
        return cmcId;
    }

    public void setCmcId(Long cmcId) {
        this.cmcId = cmcId;
    }

    public Long getPonId() {
        return ponId;
    }

    public void setPonId(Long ponId) {
        this.ponId = ponId;
    }

    public Long getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(Long totalNum) {
        this.totalNum = totalNum;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public Long getDeniedNum() {
        return deniedNum;
    }

    public void setDeniedNum(Long deniedNum) {
        this.deniedNum = deniedNum;
    }

    public Long getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(Long deviceType) {
        this.deviceType = deviceType;
    }

    public Long getUpChannelId() {
        return upChannelId;
    }

    public void setUpChannelId(Long upChannelId) {
        this.upChannelId = upChannelId;
    }

    public Long getDownChannelId() {
        return downChannelId;
    }

    public void setDownChannelId(Long downChannelId) {
        this.downChannelId = downChannelId;
    }

    public Long getRegisteredNum() {
        return registeredNum;
    }

    public void setRegisteredNum(Long registeredNum) {
        this.registeredNum = registeredNum;
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

    @Override
    public String getDir() {
        return dir;
    }

    @Override
    public void setDir(String dir) {
        this.dir = dir;
    }

    @Override
    public String getSort() {
        return sort;
    }

    @Override
    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getCmIp() {
        return cmIp;
    }

    public void setCmIp(String cmIp) {
        this.cmIp = cmIp;
    }

    public String getCmMac() {
        return cmMac;
    }

    public void setCmMac(String cmMac) {
        this.cmMac = cmMac;
    }

    public String getCpeIp() {
        return cpeIp;
    }

    public void setCpeIp(String cpeIp) {
        this.cpeIp = cpeIp;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getMacForSignal() {
        return macForSignal;
    }

    public void setMacForSignal(String macForSignal) {
        this.macForSignal = macForSignal;
    }

    public String getDwrId() {
        return dwrId;
    }

    public void setDwrId(String dwrId) {
        this.dwrId = dwrId;
    }

    public JSONObject getCmFunctionJson() {
        return cmFunctionJson;
    }

    public void setCmFunctionJson(JSONObject cmFunctionJson) {
        this.cmFunctionJson = cmFunctionJson;
    }

    public CmAttribute getCmAttribute() {
        return cmAttribute;
    }

    public void setCmAttribute(CmAttribute cmAttribute) {
        this.cmAttribute = cmAttribute;
    }

    public Integer getProductType() {
        return productType;
    }

    public void setProductType(Integer productType) {
        this.productType = productType;
    }

    public Boolean getCpeSwitch() {
        return cpeSwitch;
    }

    public void setCpeSwitch(Boolean cpeSwitch) {
        this.cpeSwitch = cpeSwitch;
    }

    public JSONArray getUpChannels() {
        return upChannels;
    }

    public void setUpChannels(JSONArray upChannels) {
        this.upChannels = upChannels;
    }

    public JSONArray getDownChannels() {
        return downChannels;
    }

    public void setDownChannels(JSONArray downChannels) {
        this.downChannels = downChannels;
    }

    public String getCmIds() {
        return cmIds;
    }

    public void setCmIds(String cmIds) {
        this.cmIds = cmIds;
    }

    public String getJConnectedId() {
        return jConnectedId;
    }

    public void setJConnectedId(String jConnectedId) {
        this.jConnectedId = jConnectedId;
    }

    public CmcAttribute getCmcAttribute() {
        return cmcAttribute;
    }

    public void setCmcAttribute(CmcAttribute cmcAttribute) {
        this.cmcAttribute = cmcAttribute;
    }

}