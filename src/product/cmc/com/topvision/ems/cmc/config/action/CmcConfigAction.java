/***********************************************************************
 * $Id: CmcConfigAction.java,v1.0 2012-2-12 下午05:27:50 $
 * 
 * @author: zhanglongyang
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.config.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.topvision.ems.template.service.EntityTypeService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.topvision.ems.cmc.ccmts.facade.domain.CmcAttribute;
import com.topvision.ems.cmc.ccmts.service.CmcService;
import com.topvision.ems.cmc.config.facade.domain.CmcSnmpCommunityTable;
import com.topvision.ems.cmc.config.service.CmcConfigService;
import com.topvision.framework.constants.Symbol;
import com.topvision.ems.cmc.dhcp.facade.domain.CmcDhcpBaseConfig;
import com.topvision.ems.cmc.dhcp.facade.domain.CmcDhcpBundle;
import com.topvision.ems.cmc.dhcp.facade.domain.CmcDhcpGiAddr;
import com.topvision.ems.cmc.dhcp.service.CmcDhcpService;
import com.topvision.ems.cmc.domain.CmcIpInfo;
import com.topvision.ems.cmc.facade.domain.CmcSystemIpInfo;
import com.topvision.ems.cmc.facade.domain.DocsDevEvControl;
import com.topvision.ems.cmc.sni.facade.domain.CcmtsSniObject;
import com.topvision.framework.utils.CmcIndexUtils;
import com.topvision.ems.cmc.vlan.domain.CmcPrimaryVlan;
import com.topvision.ems.cmc.vlan.facade.domain.CmcVifSubIpEntry;
import com.topvision.ems.cmc.vlan.facade.domain.CmcVlanPrimaryInterface;
import com.topvision.ems.cmc.vlan.service.CmcVlanService;
import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.network.service.EntityService;
import com.topvision.ems.performance.domain.Monitor;
import com.topvision.framework.annotation.OperationLogProperty;
import com.topvision.framework.snmp.SnmpParam;
import com.topvision.framework.web.struts2.BaseAction;
import com.topvision.platform.domain.OperationLog;

/**
 * 配置相关功能
 * 
 * @author zhanglongyang
 * @created @2012-2-12-下午05:27:50
 * 
 */
@Controller("cmcConfigAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class CmcConfigAction extends BaseAction {
    private static final long serialVersionUID = 1249428489626066386L;
    @Resource(name = "entityService")
    private EntityService entityService;
    @Autowired
    private EntityTypeService entityTypeService;
    @Resource(name = "cmcService")
    private CmcService cmcService;
    @Resource(name = "cmcConfigService")
    private CmcConfigService cmcConfigService;
    @Resource(name = "cmcDhcpService")
    private CmcDhcpService cmcDhcpService;
    private Long pollingInterval;
    private Boolean pollingStatus;
    private JSONArray cmcSystemIpInfo;
    private String location;
    private String cmcName;
    private String contact;
    private String cmcDescr;
    private JSONArray cmcEmsParam;
    private String ipParamStr;
    private Integer physicalInterfaceMode;
    private DocsDevEvControl docsDevEvControl;
    private Integer topCcmtsDhcpAlloc;
    private String dhcpAllocOption60;
    private CmcAttribute cmcAttribute;
    private CmcPrimaryVlan cmcPrimaryVlan;
    private CmcDhcpBaseConfig cmcDhcpBaseConfigInfo;
    private CcmtsSniObject ccmctSni;// 用于存放上联口链路配置，用于Action与HTML之间传递数据
    private JSONArray ccmctSniJson;
    private String sniParamStr;
    private JSONArray dhcpGiAddrList;
    private JSONArray dhcpBundleList;
    private String source;
    private Long entityId;
    private Long cmcId;
    private Entity entity;
    private SnmpParam snmpParam;
    private Integer operationResult;
    private Integer productType;
    private String readCommunity;
    private String writeCommunity;
    private CmcSnmpCommunityTable snmpCommunityTable;
    @Resource(name = "cmcVlanService")
    private CmcVlanService cmcVlanService;
    private String emsCmcIpAddress;
    private String emsCmcReadCommunity;
    private String emsCmcWriteCommunity;
    private String subIpAddress;
    private String subIpMask;
    private Integer subSeqIndex;
    private String topCcmtsEthIpAddr;
    private String topCcmtsEthIpMask;
    private String topCcmtsEthVlanGateway;
    private JSONObject cmcSysConfig;
    private Integer piggyBack;
    private JSONArray vlanList;

    private JSONObject snmpJson;

    public String getSniParamStr() {
        return sniParamStr;
    }

    public void setSniParamStr(String sniParamStr) {
        this.sniParamStr = sniParamStr;
    }

    public JSONArray getCcmctSniJson() {
        return ccmctSniJson;
    }

    public void setCcmctSniJson(JSONArray ccmctSniJson) {
        this.ccmctSniJson = ccmctSniJson;
    }

    private Logger logger = LoggerFactory.getLogger(CmcConfigAction.class);

    /**
     * 显示配置信息tab页面
     * 
     * @return String
     */
    public String showCmcConfig() {
        cmcAttribute = cmcService.getCmcAttributeByCmcId(cmcId);
        CmcDhcpBaseConfig cmcDhcpBaseConfig = cmcConfigService.getCC8800BCmcDhcpBaseConfig(cmcId);
        CmcVlanPrimaryInterface cmcVlanPrimaryInterface = cmcConfigService.getCC8800BVlanPriInterface(cmcId);
        if (cmcVlanPrimaryInterface != null) {
            cmcPrimaryVlan = new CmcPrimaryVlan();
            cmcPrimaryVlan = cmcConfigService.getCC8800BCcPrimaryVlanAsSnmp(cmcId,
                    cmcVlanPrimaryInterface.getVlanPrimaryInterface());
        } else {
            cmcPrimaryVlan = new CmcPrimaryVlan();
            cmcPrimaryVlan.setPriIpAddr("0.0.0.0");
            cmcPrimaryVlan.setPriIpMask("255.255.255.0");
            cmcPrimaryVlan.setDefaultRoute("0.0.0.0");
        }
        if (cmcDhcpBaseConfig != null) {
            // topCcmtsDhcpAlloc = cmcDhcpBaseConfig.getTopCcmtsDhcpAlloc();
            // dhcpAllocOption60 = cmcDhcpBaseConfig.getTopCcmtsDhcpAllocOption60();
        } else {
            topCcmtsDhcpAlloc = 2;// 2 means disable
        }
        if (cmcPrimaryVlan == null) {
            cmcSystemIpInfo = JSONArray.fromObject(false);
        } else {
            if (cmcVlanPrimaryInterface != null) {
                cmcPrimaryVlan.setDefaultRoute(cmcVlanPrimaryInterface.getVlanPrimaryDefaultRoute());
            } else {
                cmcPrimaryVlan.setDefaultRoute("0.0.0.0");
            }
            cmcSystemIpInfo = JSONArray.fromObject(cmcPrimaryVlan);
        }

        // 获取上联口链路配置信息
        ccmctSni = cmcConfigService.getCC8800BSniConfig(cmcId);

        if (ccmctSni != null) {
            ccmctSniJson = JSONArray.fromObject(ccmctSni);
        } else {
            ccmctSniJson = JSONArray.fromObject(false);
        }
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("cmcId", cmcId);
        List<CmcDhcpGiAddr> tmpGiAddrList = cmcDhcpService.getCmcDhcpGiAddrList(map);
        dhcpGiAddrList = JSONArray.fromObject(tmpGiAddrList);
        List<CmcDhcpBundle> tmpBundleList = cmcDhcpService.getCmcDhcpBundleList(map);
        dhcpBundleList = JSONArray.fromObject(tmpBundleList);

        entityId = cmcService.getEntityIdByCmcId(cmcId);
        entity = entityService.getEntity(entityId);
        snmpParam = entityService.getSnmpParamByEntity(entityId);
        return SUCCESS;
    }

    /**
     * 修改基本配置信息
     * 
     * @return String
     */
    @OperationLogProperty(actionName = "cmcConfigAction", operationName = "modifyCCMTS${source}BasicInfo")
    public String modifyCmcBasicInfo() {
        String message = "success";
        entityId = cmcService.getEntityIdByCmcId(cmcId);
        Long cmcIndex = cmcService.getCmcIndexByCmcId(cmcId);
        Integer cmcType = cmcService.getCmcTypeByCmcId(cmcId);
        if (entityTypeService.isCcmtsWithAgent(cmcType.longValue())) {
            source = "";
        } else {
            source = Symbol.BRACKET_LEFT + CmcIndexUtils.getSlotNo(cmcIndex).toString() + Symbol.SLASH
                    + CmcIndexUtils.getPonNo(cmcIndex).toString() + Symbol.BRACKET_RIGHT;
        }
        try {
            cmcConfigService.modifyCmcBasicInfo(cmcId, cmcName, location, contact);
            operationResult = OperationLog.SUCCESS;
        } catch (Exception e) {
            message = "error[" + e.getMessage() + Symbol.BRACKET_RIGHT;
            operationResult = OperationLog.FAILURE;
            logger.debug("modifyCmcBasicInfo is error:{}", e);
        }
        writeDataToAjax(message);
        return NONE;
    }

    /**
     * 修改IP配置信息
     * 
     * @return String
     */
    @OperationLogProperty(actionName = "cmcConfigAction", operationName = "modifyCCMTS${source}IPConfig")
    public String modifyCmcIpInfo() {
        String message = "success";
        entityId = cmcService.getEntityIdByCmcId(cmcId);
        Long cmcIndex = cmcService.getCmcIndexByCmcId(cmcId);
        Integer cmcType = cmcService.getCmcTypeByCmcId(cmcId);
        if (entityTypeService.isCcmtsWithAgent(cmcType.longValue())) {
            source = "";
        } else {
            source = Symbol.BRACKET_LEFT + CmcIndexUtils.getSlotNo(cmcIndex).toString() + Symbol.SLASH
                    + CmcIndexUtils.getPonNo(cmcIndex).toString() + Symbol.BRACKET_RIGHT;
        }
        try {
            cmcConfigService.modifyCmcIpInfo(cmcId, ipParamStr);
            operationResult = OperationLog.SUCCESS;
        } catch (Exception e) {
            message = "error[" + e.getMessage() + Symbol.BRACKET_RIGHT;
            operationResult = OperationLog.FAILURE;
            logger.debug("modifyCmcBasicInfo is error:{}", e);
        }
        writeDataToAjax(message);
        return NONE;
    }

    /**
     * 修改EMS配置信息
     * 
     * @return String
     */
    public String modifyCmcEmsInfo() {
        return NONE;
    }

    /**
     * 添加EMS配置信息
     * 
     * @return String
     */
    public String addCmcEmsInfo() {
        return NONE;
    }

    /**
     * 删除EMS配置信息
     * 
     * @return String
     */
    public String deleteCmcEmsInfo() {
        return NONE;
    }

    /**
     * 刷新配置信息
     * 
     * @return String
     */
    @OperationLogProperty(actionName = "cmcConfigAction", operationName = "refreshCCMTS${source}Config")
    public String refreshCmcConfig() {
        // 1,刷新base信息-cmcAttribute 2,刷新SNMP信息
        entityId = cmcService.getEntityIdByCmcId(cmcId);
        Long cmcIndex = cmcService.getCmcIndexByCmcId(cmcId);
        Integer cmcType = cmcService.getCmcTypeByCmcId(cmcId);
        if (entityTypeService.isCcmtsWithAgent(cmcType.longValue())) {
            source = "";
        } else {
            source = Symbol.BRACKET_LEFT + CmcIndexUtils.getSlotNo(cmcIndex).toString() + Symbol.SLASH
                    + CmcIndexUtils.getPonNo(cmcIndex).toString() + Symbol.BRACKET_RIGHT;
        }
        try {
            cmcAttribute = cmcConfigService.refreshCmcAttribute(cmcId, productType);
            cmcPrimaryVlan = cmcConfigService.refreshCmcPrimaryVlanAsSnmp(cmcId, productType);
            // 刷新DHCP Relay基本信息
            /* cmcDhcpBaseConfigInfo = cmcDhcpService.refreshCmcDhcpBaseConfig(cmcId); */
            // 刷新Bundle信息
            cmcDhcpService.refreshDhcpInfo(cmcId);
            operationResult = OperationLog.SUCCESS;
        } catch (Exception e) {
            operationResult = OperationLog.FAILURE;
            logger.debug("", e);
        }
        Map<String, Object> json = new HashMap<String, Object>();
        json.put("cmcSystemIpInfo", cmcSystemIpInfo);
        json.put("cmcAttribute", cmcAttribute);
        writeDataToAjax(JSONObject.fromObject(json));
        return NONE;
    }

    /**
     * 显示轮询设置页面
     * 
     * @return String
     */
    public String showCmcPollingConfig() {
        return SUCCESS;
    }

    /**
     * 轮询设置
     * 
     * @return String
     */
    public String updateCmcPollingConfig() {
        Monitor monitor = cmcConfigService.updateCmcPollingConfig(cmcId);
        pollingInterval = monitor.getIntervalOfNormal();
        pollingStatus = monitor.isEnabled();
        return NONE;
    }

    /**
     * 设置主备优先模式
     * 
     * 刘占山增加废弃注解，若发现此方法有效，请删除注解。20130411
     * 
     * 
     * @return String
     */
    @Deprecated
    @OperationLogProperty(actionName = "cmcConfigAction", operationName = "modifyCCMTS${source}Mode")
    public String modifyPhysicalInterfacePreferredMode() {
        entityId = cmcService.getEntityIdByCmcId(cmcId);
        Long cmcIndex = cmcService.getCmcIndexByCmcId(cmcId);
        Integer cmcType = cmcService.getCmcTypeByCmcId(cmcId);
        if (entityTypeService.isCcmtsWithAgent(cmcType.longValue())) {
            source = "";
        } else {
            source = Symbol.BRACKET_LEFT + CmcIndexUtils.getSlotNo(cmcIndex).toString() + Symbol.SLASH
                    + CmcIndexUtils.getPonNo(cmcIndex).toString() + Symbol.BRACKET_RIGHT;
        }
        try {
            cmcConfigService.modifyPhysicalInterfacePreferredMode(cmcId, physicalInterfaceMode);
            operationResult = OperationLog.SUCCESS;
        } catch (Exception e) {
            operationResult = OperationLog.FAILURE;
            logger.debug("", e);
        }
        return NONE;
    }

    /**
     * 设置loglevel动作
     * 
     * @return String
     */
    @OperationLogProperty(actionName = "cmcConfigAction", operationName = "setOLT${source}loglevelAction")
    public String deviceEventControl() {
        String result = null;
        source = entityService.getEntity(entityId).getIp();
        docsDevEvControl.setEntityId(entityId);
        docsDevEvControl.calDocsDevEvReportingHex();
        Map<String, String> message = new HashMap<String, String>();
        try {
            cmcConfigService.modifyDeviceEventControl(docsDevEvControl);
            result = "success";
            operationResult = OperationLog.SUCCESS;
        } catch (Exception e) {
            result = "error";
            operationResult = OperationLog.FAILURE;
            logger.debug("", e);
        } finally {
            message.put("message", result);
            writeDataToAjax(JSONObject.fromObject(message));
        }
        return NONE;
    }

    /**
     * 修改上联口链路状态配置
     * 
     * @return String
     */
    @OperationLogProperty(actionName = "cmcConfigAction", operationName = "modifyCCMTS${source}SniConfig")
    public String modifyCc8800BSniConfig() {
        String result = "success";
        entityId = cmcService.getEntityIdByCmcId(cmcId);
        Long cmcIndex = cmcService.getCmcIndexByCmcId(cmcId);
        Integer cmcType = cmcService.getCmcTypeByCmcId(cmcId);
        if (entityTypeService.isCcmtsWithAgent(cmcType.longValue())) {
            source = "";
        } else {
            source = Symbol.BRACKET_LEFT + CmcIndexUtils.getSlotNo(cmcIndex).toString() + Symbol.SLASH
                    + CmcIndexUtils.getPonNo(cmcIndex).toString() + Symbol.BRACKET_RIGHT;
        }
        try {
            String[] tmp = sniParamStr.split(Symbol.UNDERLINE);
            ccmctSni = new CcmtsSniObject();
            ccmctSni.setTopCcmtsSniEthInt(Integer.parseInt(tmp[0]));
            ccmctSni.setTopCcmtsSniMainInt(Integer.parseInt(tmp[1]));
            ccmctSni.setTopCcmtsSniBackupInt(Integer.parseInt(tmp[2]));
            ccmctSni.setCmcId(cmcId);
            cmcConfigService.setCC8800BSniConfig(ccmctSni);
            operationResult = OperationLog.SUCCESS;
        } catch (Exception e) {
            operationResult = OperationLog.FAILURE;
            result = "error";
            logger.debug("", e);
        }
        try {
            writeDataToAjax(result);
        } catch (Exception e) {
            logger.error("", e);
        }
        return NONE;
    }

    /**
     * 显示SNMP配置页面
     * 
     * @return String
     */
    public String showSnmpConfigJsp() {
        snmpCommunityTable = cmcConfigService.getCmcSnmpCommunityTable(entityId);
        if (snmpCommunityTable == null) {
            snmpCommunityTable = new CmcSnmpCommunityTable();
        }
        return SUCCESS;
    }

    /**
     * 展示网管的基本配置信息
     * 
     * @return
     */
    public String showEmsBasicInfoJsp() {
        SnmpParam param = entityService.getSnmpParamByEntity(entityId);
        emsCmcReadCommunity = param.getCommunity().replace("\"", "&quot;");
        emsCmcWriteCommunity = param.getWriteCommunity().replace("\"", "&quot;");
        snmpJson = JSONObject.fromObject(param);
        emsCmcIpAddress = entityService.getEntity(entityId).getIp();
        return SUCCESS;
    }

    /**
     * 显示网络基本信息
     * 
     * @return
     */
    public String showSystemIpInfoJsp() {
        vlanList = JSONArray.fromObject(cmcVlanService.getVlanConfigList(entityId));
        return SUCCESS;
    }

    /**
     * 加载设备网络基本信息
     * 
     * @return
     * @throws IOException
     */
    public String loadSystemIpInfo() throws IOException {

        List<CmcIpInfo> result = new ArrayList<CmcIpInfo>();
        CmcSystemIpInfo systemIpInfo = cmcConfigService.getCmcIpInfo(entityId);
        List<CmcVifSubIpEntry> cmcVifSubIpEntries = cmcVlanService.getCmcVlanListFromVlan0(entityId);
        CmcIpInfo mainIpInfo = new CmcIpInfo();
        mainIpInfo.setEntityId(entityId);
        mainIpInfo.setIpType("Primary IP");
        mainIpInfo.setIpAddress(systemIpInfo.getTopCcmtsEthIpAddr());
        mainIpInfo.setIpGateAddress(systemIpInfo.getTopCcmtsEthGateway());
        mainIpInfo.setIpMaskAddress(systemIpInfo.getTopCcmtsEthIpMask());
        result.add(mainIpInfo);
        for (CmcVifSubIpEntry tmp : cmcVifSubIpEntries) {
            CmcIpInfo subInfo = new CmcIpInfo();
            subInfo.setEntityId(entityId);
            subInfo.setIpType("Second IP");
            subInfo.setIpAddress(tmp.getTopCcmtsVifSubIpAddr());
            subInfo.setIpGateAddress("--");
            subInfo.setIpMaskAddress(tmp.getTopCcmtsVifSubIpMask());
            subInfo.setSeqIndex(tmp.getTopCcmtsVifSubIpSeqIdx());
            result.add(subInfo);
        }
        Map<String, Object> json = new HashMap<String, Object>();
        json.put("totalProperty", result.size());
        json.put("data", result);

        writeDataToAjax(JSONObject.fromObject(json));
        return NONE;
    }

    /**
     * 设置8800B的SNMP基本信息
     * 
     * @return
     */
    public String setSnmpConfig() {
        String result = "success";
        try {
            cmcConfigService.setCC8800BSnmpInfo(entityId, readCommunity, writeCommunity);
            operationResult = OperationLog.SUCCESS;
        } catch (Exception e) {
            operationResult = OperationLog.FAILURE;
            result = "error";
            logger.debug("", e);
        }
        writeDataToAjax(result);
        return NONE;
    }

    /**
     * 设置8800B的网管基本信息
     * 
     * @return
     */
    public String modifyEmsConfigInfo() {
        String result = "success";
        try {
            SnmpParam param = new SnmpParam();
            param.setEntityId(entityId);
            param.setCommunity(emsCmcReadCommunity);
            param.setWriteCommunity(emsCmcWriteCommunity);
            entityService.updateSnmpParam(param);
            // entityService.updateEntityIpInfo的接口做了修改，此处入存在问题请查看接口实现中的说明
            entityService.updateEntityIpInfo(entityId, emsCmcIpAddress, emsCmcIpAddress);
            operationResult = OperationLog.SUCCESS;
        } catch (Exception e) {
            operationResult = OperationLog.FAILURE;
            result = "error";
            logger.debug("", e);
        }
        writeDataToAjax(result);
        return NONE;
    }

    /**
     * 删除从IP
     * 
     * @return
     */
    public String deleteSubIpVlan0() {
        String result = "success";
        try {
            cmcVlanService.deleteCmcVlanSecIp(entityId, 0, subSeqIndex);
        } catch (Exception e) {
            operationResult = OperationLog.FAILURE;
            result = "error";
            logger.debug("", e);
        }
        writeDataToAjax(result);
        return NONE;
    }

    /**
     * 修改CMC网络信息
     * 
     * @return
     */
    public String modifyCmcSystemIpInfo() {
        String result = "success";
        try {
            CmcSystemIpInfo info = new CmcSystemIpInfo();
            info.setTopCcmtsEthIpAddr(topCcmtsEthIpAddr);
            info.setTopCcmtsEthIpMask(topCcmtsEthIpMask);
            boolean flag = cmcConfigService.setCC8800BSystemIpInfo(entityId, info);
            if (flag) {
                result = "success";
            } else {
                result = "error";
            }
        } catch (Exception e) {
            operationResult = OperationLog.FAILURE;
            result = "error";
            logger.debug("", e);
        }
        writeDataToAjax(result);
        return NONE;
    }

    public String modifyCmcGatewayInfo() {
        String result = "success";
        try {
            CmcSystemIpInfo info = new CmcSystemIpInfo();
            info.setTopCcmtsEthGateway(topCcmtsEthVlanGateway);
            boolean flag = cmcConfigService.setCC8800BGateway(entityId, info);
            if (flag) {
                result = "success";
            } else {
                result = "error";
            }
        } catch (Exception e) {
            operationResult = OperationLog.FAILURE;
            result = "error";
            logger.debug("", e);
        }
        writeDataToAjax(result);
        return NONE;
    }

    /**
     * 修改CMC网络信息
     * 
     * @return
     */
    public String addCmcSubIpInfo() {
        String result = "success";
        try {
            cmcVlanService.addCmcVlanSecIp(entityId, 0, subIpAddress, subIpMask);
        } catch (Exception e) {
            operationResult = OperationLog.FAILURE;
            result = "error";
            logger.debug("", e);
        }
        writeDataToAjax(result);
        return NONE;
    }

    /**
     * 跳转新建CMC网络信息页面
     * 
     * @return
     */
    public String showAddCmcSubIpInfo() {
        vlanList = JSONArray.fromObject(cmcVlanService.getVlanConfigList(entityId));
        return SUCCESS;
    }

    /**
     * 跳转修改CMC网络信息页面
     * 
     * @return
     */
    public String showModifyCmcSystemIpInfo() {
        vlanList = JSONArray.fromObject(cmcVlanService.getVlanConfigList(entityId));
        return SUCCESS;
    }

    public String showCmcPiggyBackConfig() {
        cmcSysConfig = JSONObject.fromObject(cmcConfigService.getCmcSysConfig(cmcId));
        return SUCCESS;
    }

    public String modifyCmcPiggyBackConfig() throws Exception {
        String result;
        try {
            cmcConfigService.modifyCmcSysPiggyBack(cmcId, piggyBack);
            result = "true";
        } catch (Exception e) {
            logger.error("", e);
            result = "false";
        }
        writeDataToAjax(result);
        return NONE;
    }

    /**
     * @return the snmpCommunityTable
     */
    public CmcSnmpCommunityTable getSnmpCommunityTable() {
        return snmpCommunityTable;
    }

    /**
     * @param snmpCommunityTable
     *            the snmpCommunityTable to set
     */
    public void setSnmpCommunityTable(CmcSnmpCommunityTable snmpCommunityTable) {
        this.snmpCommunityTable = snmpCommunityTable;
    }

    public CmcConfigService getCmcConfigService() {
        return cmcConfigService;
    }

    public void setCmcConfigService(CmcConfigService cmcConfigService) {
        this.cmcConfigService = cmcConfigService;
    }

    public CmcDhcpService getCmcDhcpService() {
        return cmcDhcpService;
    }

    public void setCmcDhcpService(CmcDhcpService cmcDhcpService) {
        this.cmcDhcpService = cmcDhcpService;
    }

    public Long getPollingInterval() {
        return pollingInterval;
    }

    public void setPollingInterval(Long pollingInterval) {
        this.pollingInterval = pollingInterval;
    }

    public Boolean getPollingStatus() {
        return pollingStatus;
    }

    public void setPollingStatus(Boolean pollingStatus) {
        this.pollingStatus = pollingStatus;
    }

    public JSONArray getCmcSystemIpInfo() {
        return cmcSystemIpInfo;
    }

    public void setCmcSystemIpInfo(JSONArray cmcSystemIpInfo) {
        this.cmcSystemIpInfo = cmcSystemIpInfo;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getCmcName() {
        return cmcName;
    }

    public void setCmcName(String cmcName) {
        this.cmcName = cmcName;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getCmcDescr() {
        return cmcDescr;
    }

    public void setCmcDescr(String cmcDescr) {
        this.cmcDescr = cmcDescr;
    }

    public JSONArray getCmcEmsParam() {
        return cmcEmsParam;
    }

    public void setCmcEmsParam(JSONArray cmcEmsParam) {
        this.cmcEmsParam = cmcEmsParam;
    }

    public DocsDevEvControl getDocsDevEvControl() {
        return docsDevEvControl;
    }

    public void setDocsDevEvControl(DocsDevEvControl docsDevEvControl) {
        this.docsDevEvControl = docsDevEvControl;
    }

    public String getIpParamStr() {
        return ipParamStr;
    }

    public void setIpParamStr(String ipParamStr) {
        this.ipParamStr = ipParamStr;
    }

    public Integer getPhysicalInterfaceMode() {
        return physicalInterfaceMode;
    }

    public void setPhysicalInterfaceMode(Integer physicalInterfaceMode) {
        this.physicalInterfaceMode = physicalInterfaceMode;
    }

    public Integer getTopCcmtsDhcpAlloc() {
        return topCcmtsDhcpAlloc;
    }

    public void setTopCcmtsDhcpAlloc(Integer topCcmtsDhcpAlloc) {
        this.topCcmtsDhcpAlloc = topCcmtsDhcpAlloc;
    }

    public String getDhcpAllocOption60() {
        return dhcpAllocOption60;
    }

    public void setDhcpAllocOption60(String dhcpAllocOption60) {
        this.dhcpAllocOption60 = dhcpAllocOption60;
    }

    public CmcPrimaryVlan getCmcPrimaryVlan() {
        return cmcPrimaryVlan;
    }

    public void setCmcPrimaryVlan(CmcPrimaryVlan cmcPrimaryVlan) {
        this.cmcPrimaryVlan = cmcPrimaryVlan;
    }

    public CmcDhcpBaseConfig getCmcDhcpBaseConfigInfo() {
        return cmcDhcpBaseConfigInfo;
    }

    public void setCmcDhcpBaseConfigInfo(CmcDhcpBaseConfig cmcDhcpBaseConfigInfo) {
        this.cmcDhcpBaseConfigInfo = cmcDhcpBaseConfigInfo;
    }

    /**
     * @return the ccmctSni
     */
    public CcmtsSniObject getCcmctSni() {
        return ccmctSni;
    }

    /**
     * @param ccmctSni
     *            the ccmctSni to set
     */
    public void setCcmctSni(CcmtsSniObject ccmctSni) {
        this.ccmctSni = ccmctSni;
    }

    public JSONArray getDhcpGiAddrList() {
        return dhcpGiAddrList;
    }

    public void setDhcpGiAddrList(JSONArray dhcpGiAddrList) {
        this.dhcpGiAddrList = dhcpGiAddrList;
    }

    public JSONArray getDhcpBundleList() {
        return dhcpBundleList;
    }

    public void setDhcpBundleList(JSONArray dhcpBundleList) {
        this.dhcpBundleList = dhcpBundleList;
    }

    /**
     * @return the source
     */
    public String getSource() {
        return source;
    }

    /**
     * @param source
     *            the source to set
     */
    public void setSource(String source) {
        this.source = source;
    }

    /**
     * @return the entityId
     */
    public Long getEntityId() {
        return entityId;
    }

    /**
     * @param entityId
     *            the entityId to set
     */
    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    /**
     * @return the operationResult
     */
    public Integer getOperationResult() {
        return operationResult;
    }

    /**
     * @param operationResult
     *            the operationResult to set
     */
    public void setOperationResult(Integer operationResult) {
        this.operationResult = operationResult;
    }

    public CmcAttribute getCmcAttribute() {
        return cmcAttribute;
    }

    public void setCmcAttribute(CmcAttribute cmcAttribute) {
        this.cmcAttribute = cmcAttribute;
    }

    public Long getCmcId() {
        return cmcId;
    }

    public void setCmcId(Long cmcId) {
        this.cmcId = cmcId;
    }

    public CmcService getCmcService() {
        return cmcService;
    }

    public void setCmcService(CmcService cmcService) {
        this.cmcService = cmcService;
    }

    public Entity getEntity() {
        return entity;
    }

    public void setEntity(Entity entity) {
        this.entity = entity;
    }

    public EntityService getEntityService() {
        return entityService;
    }

    public void setEntityService(EntityService entityService) {
        this.entityService = entityService;
    }

    public SnmpParam getSnmpParam() {
        return snmpParam;
    }

    public void setSnmpParam(SnmpParam snmpParam) {
        this.snmpParam = snmpParam;
    }

    public Integer getProductType() {
        return productType;
    }

    public void setProductType(Integer productType) {
        this.productType = productType;
    }

    /**
     * @return the readCommunity
     */
    public String getReadCommunity() {
        return readCommunity;
    }

    /**
     * @param readCommunity
     *            the readCommunity to set
     */
    public void setReadCommunity(String readCommunity) {
        this.readCommunity = readCommunity;
    }

    /**
     * @return the writeCommunity
     */
    public String getWriteCommunity() {
        return writeCommunity;
    }

    /**
     * @param writeCommunity
     *            the writeCommunity to set
     */
    public void setWriteCommunity(String writeCommunity) {
        this.writeCommunity = writeCommunity;
    }

    /**
     * @return the cmcVlanService
     */
    public CmcVlanService getCmcVlanService() {
        return cmcVlanService;
    }

    /**
     * @param cmcVlanService
     *            the cmcVlanService to set
     */
    public void setCmcVlanService(CmcVlanService cmcVlanService) {
        this.cmcVlanService = cmcVlanService;
    }

    /**
     * @return the emsCmcIpAddress
     */
    public String getEmsCmcIpAddress() {
        return emsCmcIpAddress;
    }

    /**
     * @param emsCmcIpAddress
     *            the emsCmcIpAddress to set
     */
    public void setEmsCmcIpAddress(String emsCmcIpAddress) {
        this.emsCmcIpAddress = emsCmcIpAddress;
    }

    /**
     * @return the emsCmcReadCommunity
     */
    public String getEmsCmcReadCommunity() {
        return emsCmcReadCommunity;
    }

    /**
     * @param emsCmcReadCommunity
     *            the emsCmcReadCommunity to set
     */
    public void setEmsCmcReadCommunity(String emsCmcReadCommunity) {
        this.emsCmcReadCommunity = emsCmcReadCommunity;
    }

    /**
     * @return the emsCmcWriteCommunity
     */
    public String getEmsCmcWriteCommunity() {
        return emsCmcWriteCommunity;
    }

    /**
     * @param emsCmcWriteCommunity
     *            the emsCmcWriteCommunity to set
     */
    public void setEmsCmcWriteCommunity(String emsCmcWriteCommunity) {
        this.emsCmcWriteCommunity = emsCmcWriteCommunity;
    }

    /**
     * @return the subIpAddress
     */
    public String getSubIpAddress() {
        return subIpAddress;
    }

    /**
     * @param subIpAddress
     *            the subIpAddress to set
     */
    public void setSubIpAddress(String subIpAddress) {
        this.subIpAddress = subIpAddress;
    }

    /**
     * @return the subSeqIndex
     */
    public Integer getSubSeqIndex() {
        return subSeqIndex;
    }

    /**
     * @param subSeqIndex
     *            the subSeqIndex to set
     */
    public void setSubSeqIndex(Integer subSeqIndex) {
        this.subSeqIndex = subSeqIndex;
    }

    /**
     * @return the topCcmtsEthIpAddr
     */
    public String getTopCcmtsEthIpAddr() {
        return topCcmtsEthIpAddr;
    }

    /**
     * @param topCcmtsEthIpAddr
     *            the topCcmtsEthIpAddr to set
     */
    public void setTopCcmtsEthIpAddr(String topCcmtsEthIpAddr) {
        this.topCcmtsEthIpAddr = topCcmtsEthIpAddr;
    }

    /**
     * @return the topCcmtsEthIpMask
     */
    public String getTopCcmtsEthIpMask() {
        return topCcmtsEthIpMask;
    }

    /**
     * @param topCcmtsEthIpMask
     *            the topCcmtsEthIpMask to set
     */
    public void setTopCcmtsEthIpMask(String topCcmtsEthIpMask) {
        this.topCcmtsEthIpMask = topCcmtsEthIpMask;
    }

    /**
     * @return the topCcmtsEthVlanGateway
     */
    public String getTopCcmtsEthVlanGateway() {
        return topCcmtsEthVlanGateway;
    }

    /**
     * @param topCcmtsEthVlanGateway
     *            the topCcmtsEthVlanGateway to set
     */
    public void setTopCcmtsEthVlanGateway(String topCcmtsEthVlanGateway) {
        this.topCcmtsEthVlanGateway = topCcmtsEthVlanGateway;
    }

    /**
     * @return the subIpMask
     */
    public String getSubIpMask() {
        return subIpMask;
    }

    /**
     * @param subIpMask
     *            the subIpMask to set
     */
    public void setSubIpMask(String subIpMask) {
        this.subIpMask = subIpMask;
    }

    public JSONObject getCmcSysConfig() {
        return cmcSysConfig;
    }

    public void setCmcSysConfig(JSONObject cmcSysConfig) {
        this.cmcSysConfig = cmcSysConfig;
    }

    public Integer getPiggyBack() {
        return piggyBack;
    }

    public void setPiggyBack(Integer piggyBack) {
        this.piggyBack = piggyBack;
    }

    public JSONArray getVlanList() {
        return vlanList;
    }

    public void setVlanList(JSONArray vlanList) {
        this.vlanList = vlanList;
    }

    public JSONObject getSnmpJson() {
        return snmpJson;
    }

    public void setSnmpJson(JSONObject snmpJson) {
        this.snmpJson = snmpJson;
    }

}
