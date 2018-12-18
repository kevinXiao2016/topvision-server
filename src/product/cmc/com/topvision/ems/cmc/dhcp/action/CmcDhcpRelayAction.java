package com.topvision.ems.cmc.dhcp.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.topvision.ems.cmc.ccmts.facade.domain.CmcAttribute;
import com.topvision.ems.cmc.ccmts.service.CmcService;
import com.topvision.ems.cmc.config.service.CmcConfigService;
import com.topvision.ems.cmc.dhcp.domain.DhcpRelayConfig;
import com.topvision.ems.cmc.dhcp.domain.DhcpRelayConfigSetting;
import com.topvision.ems.cmc.dhcp.exception.SetDhcpRelayFailException;
import com.topvision.ems.cmc.dhcp.facade.domain.CmcDhcpBaseConfig;
import com.topvision.ems.cmc.dhcp.facade.domain.CmcDhcpIntIp;
import com.topvision.ems.cmc.dhcp.facade.domain.CmcDhcpOption60;
import com.topvision.ems.cmc.dhcp.facade.domain.CmcDhcpServerConfig;
import com.topvision.ems.cmc.dhcp.service.CmcDhcpRelayService;
import com.topvision.ems.cmc.vlan.service.CmcVlanService;
import com.topvision.framework.annotation.OperationLogProperty;
import com.topvision.framework.snmp.RowStatus;
import com.topvision.framework.web.struts2.BaseAction;
import com.topvision.platform.domain.OperationLog;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Controller("cmcDhcpRelayAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class CmcDhcpRelayAction extends BaseAction {
    private static final int CREATE = 1;
    private static final int MODIFY = 2;
    private static final long serialVersionUID = -2477301190008654882L;
    private final Logger logger = LoggerFactory.getLogger(CmcDhcpRelayAction.class);
    @Resource(name = "cmcDhcpRelayService")
    private CmcDhcpRelayService cmcDhcpRelayService;
    @Resource(name = "cmcConfigService")
    private CmcConfigService cmcConfigService;
    @Resource(name = "cmcService")
    private CmcService cmcService;
    @Resource(name = "cmcVlanService")
    private CmcVlanService cmcVlanService;
    private CmcAttribute cmcAttribute;
    private Long entityId;
    private Long productType;
    private Long cmcId;
    private Integer deviceType;
    private String option60Str;
    private String dhcpServerIp;
    private JSONObject cmcDhcpBaseConfig;
    private CmcDhcpBaseConfig modifyCmcDhcpBaseConfig;
    private JSONObject dhcpRelayConfig;
    private String bundleInterface;
    private Integer policy;
    private Integer cableSourceVerify;
    private String primaryIp;
    private String primaryIpMask;
    private List<String> giAddrList;
    private List<String> giAddrMaskList;
    private List<Integer> giAddrTypeList;
    private List<String> addOption60;
    private List<Integer> addOption60Type;
    private List<Long> delOption60;
    private List<String> addServerList;
    private List<Integer> addServerTypeList;
    private List<Long> delServerList;
    private List<String> addVirtualIp;
    private List<String> addVirtualIpMask;
    private List<String> delVirtualIp;
    private List<Integer> vlan;
    private List<Integer> priotity;
    private JSONArray cmcDhcpIntIpList;
    private JSONArray option60s;

    private JSONArray vlanList;

    private Integer action;

    private JSONArray bundleEndList = new JSONArray();
    private JSONObject bundleConfig = new JSONObject();
    private JSONArray giAddrArray = new JSONArray();
    private JSONArray packetVlanArray = new JSONArray();
    private JSONArray intIpArray = new JSONArray();
    private JSONArray dhcpServerArray = new JSONArray();
    private JSONArray dhcpOptionArray = new JSONArray();
    
    private String source;
    private Integer operationResult;
    

    /**
     * 显示DHCP配置列表页面
     * 
     * @return
     */
    public String showDhcpRelayConfigList() {
    	setCmcAttribute(cmcService.getCmcAttributeByCmcId(cmcId));
        entityId = cmcService.getEntityIdByCmcId(cmcId);
        CmcDhcpBaseConfig baseConfig = cmcConfigService.getCC8800BCmcDhcpBaseConfig(cmcId);
        cmcDhcpBaseConfig = JSONObject.fromObject(baseConfig);

        return SUCCESS;
    }

    /**
     * 获取DHCP配置列表数据
     * 
     * @return
     */
    public String getDhcpRelayConfigList() {
        Map<String, Object> json = new HashMap<String, Object>();
        List<DhcpRelayConfig> relay = cmcDhcpRelayService.getDhcpRelayConfigList(entityId);
        json.put("data", relay);
        writeDataToAjax(JSONObject.fromObject(json));
        return NONE;
    }

    public String getDhcpVirtualIpList() {
        Map<String, Object> json = new HashMap<String, Object>();
        List<CmcDhcpIntIp> virtualIps = null;
        if (bundleInterface != null && bundleInterface.length() > 0) {
            virtualIps = cmcDhcpRelayService.getCmcDhcpIntIpList(entityId, bundleInterface);
        }
        json.put("data", virtualIps);
        writeDataToAjax(JSONObject.fromObject(json));
        return NONE;
    }

    @OperationLogProperty(actionName = "cmcDhcpRelayAction", operationName = "modifyDhcpRelayBase")
    public String modifyDhcpRelayBase() {
        Map<String, String> message = new HashMap<String, String>();
        modifyCmcDhcpBaseConfig.setEntityId(entityId);
        String result = null;
        try {
            cmcDhcpRelayService.modifyDhcpBaseConfig(modifyCmcDhcpBaseConfig);
            result = "success";
            operationResult = OperationLog.SUCCESS;
        } catch (Exception e) {
            logger.error("", e);
            operationResult = OperationLog.FAILURE;
            result = "error";
        } finally {
            message.put("message", result);
            writeDataToAjax(JSONObject.fromObject(message));
        }
        return NONE;
    }

    @OperationLogProperty(actionName = "cmcDhcpRelayAction", operationName = "modifyDhcpRelayConfig")
    public String modifyDhcpRelayConfig() {
        Map<String, Object> message = new HashMap<String, Object>();
        String result = null;
        DhcpRelayConfigSetting setting = new DhcpRelayConfigSetting();
        /**
         * 初始化数据 begin
         */
        setting.setEntityId(entityId);
        setting.setPolicy(policy);
        setting.setPrimaryIp(primaryIp);
        setting.setPrimaryIpMask(primaryIpMask);
        setting.setBundleInterface(bundleInterface);
        setting.setCableSourceVerify(cableSourceVerify);
        setting.setDelOption60(delOption60);
        setting.setAddOption60(addOption60);
        setting.setAddOption60Type(addOption60Type);
        setting.setDelServerList(delServerList);
        setting.setAddServerList(addServerList);
        setting.setAddServerTypeList(addServerTypeList);
        setting.setAddVirtualIp(addVirtualIp);
        setting.setAddVirtualIpMask(addVirtualIpMask);
        setting.setDelVirtualIp(delVirtualIp);
        setting.setGiAddrList(giAddrList);
        setting.setGiAddrMaskList(giAddrMaskList);
        setting.setGiAddrTypeList(giAddrTypeList);
        setting.setVlan(vlan);
        setting.setPriotity(priotity);
        setting.addInitialization();
        /**
         * 初始化数据 end
         */
        try {
            cmcDhcpRelayService.modifyDhcpRelayConfig(entityId, setting);
            result = "success";
            operationResult = OperationLog.SUCCESS;
        } catch (SetDhcpRelayFailException e) {
            logger.error("", e);
            result = "error";
            message.put("result", e.getResult());
            operationResult = OperationLog.FAILURE;
        } finally {
            message.put("message", result);
            writeDataToAjax(JSONObject.fromObject(message));
        }
        return NONE;
    }

    /**
     * 删除DHCP relay配置
     * 
     * @return
     */
    @OperationLogProperty(actionName = "cmcDhcpRelayAction", operationName = "deleteDhcpRelayConfig")
    public String deleteDhcpRelayConfig() {
        Map<String, String> message = new HashMap<String, String>();
        String result = null;
        try {
            cmcDhcpRelayService.deleteDhcpRelayConfig(entityId, bundleInterface);
            result = "success";
            operationResult = OperationLog.SUCCESS;
        } catch (Exception e) {
            logger.error("", e);
            result = "error";
            operationResult = OperationLog.FAILURE;
        } finally {
            message.put("message", result);
            writeDataToAjax(JSONObject.fromObject(message));
        }
        return NONE;
    }

    @OperationLogProperty(actionName = "cmcDhcpRelayAction", operationName = "addDhcpOption60")
    public String addDhcpOption60() {
        Map<String, String> message = new HashMap<String, String>();
        CmcDhcpOption60 option60 = new CmcDhcpOption60();
        option60.setEntityId(entityId);
        option60.setTopCcmtsDhcpBundleInterface(bundleInterface);
        option60.setTopCcmtsDhcpOption60DeviceType(deviceType);
        option60.setTopCcmtsDhcpOption60Str(option60Str);
        option60.setTopCcmtsDhcpOption60Status(RowStatus.CREATE_AND_GO);
        String result = null;
        try {
            cmcDhcpRelayService.addDhcpOption60(entityId, option60);
            operationResult = OperationLog.SUCCESS;
            result = "success";
        } catch (Exception e) {
            logger.error("", e);
            operationResult = OperationLog.FAILURE;
            result = "error";
        } finally {
            message.put("message", result);
            writeDataToAjax(JSONObject.fromObject(message));
        }
        return NONE;
    }

    @OperationLogProperty(actionName = "cmcDhcpRelayAction", operationName = "addDhcpServer")
    public String addDhcpServer() {
        Map<String, String> message = new HashMap<String, String>();
        CmcDhcpServerConfig server = new CmcDhcpServerConfig();
        server.setTopCcmtsDhcpBundleInterface(bundleInterface);
        server.setTopCcmtsDhcpHelperDeviceType(deviceType);
        server.setTopCcmtsDhcpHelperIpAddr(dhcpServerIp);
        server.setTopCcmtsDhcpHelperStatus(RowStatus.CREATE_AND_GO);
        server.setEntityId(entityId);
        String result = null;
        try {
            cmcDhcpRelayService.addDhcpServer(entityId, server);
            result = "success";
            operationResult = OperationLog.SUCCESS;
        } catch (Exception e) {
            logger.error("", e);
            result = "error";
            operationResult = OperationLog.FAILURE;
        } finally {
            message.put("message", result);
            writeDataToAjax(JSONObject.fromObject(message));
        }
        return NONE;
    }

    /**
     * 显示创建DHCP Relay配置页面
     * 
     * @return
     */
    public String showCreateDhcpRelayConfig() {
        action = CREATE;
        DhcpRelayConfigSetting relayConfig = new DhcpRelayConfigSetting();
        relayConfig.setBundleListEnd(cmcDhcpRelayService.getCmcDhcpBundleEndList(entityId));
        dhcpRelayConfig = JSONObject.fromObject(relayConfig);
        cmcDhcpIntIpList = JSONArray.fromObject(cmcDhcpRelayService.getVirtulIpList(entityId));
        CmcDhcpBaseConfig baseConfig = cmcConfigService.getCC8800BCmcDhcpBaseConfig(cmcId);
        cmcDhcpBaseConfig = JSONObject.fromObject(baseConfig);
        vlanList = JSONArray.fromObject(cmcVlanService.getVlanConfigList(entityId));
        return SUCCESS;
    }

    /**
     * 显示修改DHCP Relay配置页面
     * 
     * @return
     */
    public String showModifyDhcpRelayConfig() {
        action = MODIFY;
        DhcpRelayConfigSetting relayConfig = new DhcpRelayConfigSetting();
        relayConfig.setCmcDhcpBundle(cmcDhcpRelayService.getCmcDhcpBundle(entityId, bundleInterface));
        relayConfig.setCmcDhcpGiAddr(cmcDhcpRelayService.getCmcDhcpGiAddrList(entityId, bundleInterface));
        relayConfig.setCmcDhcpOption60(cmcDhcpRelayService.getCmcDhcpOption60List(entityId, bundleInterface));
        relayConfig.setCmcDhcpServer(cmcDhcpRelayService.getCmcDhcpServerList(entityId, bundleInterface));
        relayConfig.setVirtualIp(cmcDhcpRelayService.getCmcDhcpIntIpList(entityId, bundleInterface));
        relayConfig.setCmcDhcpPacketVlan(cmcDhcpRelayService.getCmcDhcpPacketVlanList(entityId, bundleInterface));
        dhcpRelayConfig = JSONObject.fromObject(relayConfig);
        cmcDhcpIntIpList = JSONArray.fromObject(cmcDhcpRelayService.getVirtulIpList(entityId));
        CmcDhcpBaseConfig baseConfig = cmcConfigService.getCC8800BCmcDhcpBaseConfig(cmcId);
        cmcDhcpBaseConfig = JSONObject.fromObject(baseConfig);
        vlanList = JSONArray.fromObject(cmcVlanService.getVlanConfigList(entityId));
        return SUCCESS;
    }

    public String showDhcpRelayConfigBaseStep() {
        bundleEndList = JSONArray.fromObject(cmcDhcpRelayService.getCmcDhcpBundleEndList(entityId));
        bundleConfig = JSONObject.fromObject(cmcDhcpRelayService.getCmcDhcpBundle(entityId, bundleInterface));
        return SUCCESS;
    }

    public String showDhcpRelayConfigGiAddrStep() {
        bundleConfig = JSONObject.fromObject(cmcDhcpRelayService.getCmcDhcpBundle(entityId, bundleInterface));
        giAddrArray = JSONArray.fromObject(cmcDhcpRelayService.getCmcDhcpGiAddrList(entityId, bundleInterface));
        packetVlanArray = JSONArray.fromObject(cmcDhcpRelayService.getCmcDhcpPacketVlanList(entityId, bundleInterface));
        if(action == MODIFY){
            intIpArray = JSONArray.fromObject(cmcDhcpRelayService.getCmcDhcpIntIpList(entityId, bundleInterface));
        }
        cmcDhcpIntIpList = JSONArray.fromObject(cmcDhcpRelayService.getVirtulIpList(entityId));
        CmcDhcpBaseConfig baseConfig = cmcConfigService.getCC8800BCmcDhcpBaseConfig(cmcId);
        cmcDhcpBaseConfig = JSONObject.fromObject(baseConfig);
        return SUCCESS;
    }

    public String showDhcpRelayConfigIntIpStep() {
        if(action == MODIFY){
            intIpArray = JSONArray.fromObject(cmcDhcpRelayService.getCmcDhcpIntIpList(entityId, bundleInterface));
        }
        cmcDhcpIntIpList = JSONArray.fromObject(cmcDhcpRelayService.getVirtulIpList(entityId));
        return SUCCESS;
    }

    public String showDhcpRelayConfigServerStep() {
        dhcpServerArray = JSONArray.fromObject(cmcDhcpRelayService.getCmcDhcpServerList(entityId, bundleInterface));
        vlanList = JSONArray.fromObject(cmcVlanService.getVlanConfigList(entityId));
        return SUCCESS;
    }

    public String showDhcpRelayConfigOptionStep() {
        dhcpOptionArray = JSONArray.fromObject(cmcDhcpRelayService.getCmcDhcpOption60List(entityId, bundleInterface));
        return SUCCESS;
    }

    /**
     * 显示添加Option60配置页面
     * 
     * @return
     */
    public String showAddOption60Config() {
        option60s = JSONArray.fromObject(cmcDhcpRelayService.getCmcDhcpOption60List(entityId, bundleInterface));
        return SUCCESS;
    }

    /**
     * 显示添加DHCP Server配置页面
     * 
     * @return
     */
    public String showAddDhcpServerConfig() {
        vlanList = JSONArray.fromObject(cmcVlanService.getVlanConfigList(entityId));
        return SUCCESS;
    }

    /**
     * 从设备获取基本配置信息
     * 
     * @return
     */
    public String refreshDhcpBaseConfigFromDevice() {
        Map<String, String> message = new HashMap<String, String>();
        String result = null;
        try {
            cmcDhcpRelayService.refreshDhcpBaseConfig(entityId);
            result = "success";
        } catch (Exception e) {
            logger.error("", e);
            result = "error";
        } finally {
            message.put("message", result);
            writeDataToAjax(JSONObject.fromObject(message));
        }
        return NONE;
    }

    public String refreshDhcpRelayConfigFromDevice() {
        Map<String, String> message = new HashMap<String, String>();
        String result = null;
        try {
            cmcDhcpRelayService.refreshDhcpRelayConfig(entityId);
            result = "success";
        } catch (Exception e) {
            logger.error("", e);
            result = "error";
        } finally {
            message.put("message", result);
            writeDataToAjax(JSONObject.fromObject(message));
        }
        return NONE;
    }

    /**
     * @return the cmcDhcpRelayService
     */
    public CmcDhcpRelayService getCmcDhcpRelayService() {
        return cmcDhcpRelayService;
    }

    /**
     * @param cmcDhcpRelayService
     *            the cmcDhcpRelayService to set
     */
    public void setCmcDhcpRelayService(CmcDhcpRelayService cmcDhcpRelayService) {
        this.cmcDhcpRelayService = cmcDhcpRelayService;
    }

    /**
     * @return the cmcService
     */
    public CmcService getCmcService() {
        return cmcService;
    }

    /**
     * @param cmcService
     *            the cmcService to set
     */
    public void setCmcService(CmcService cmcService) {
        this.cmcService = cmcService;
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
     * @return the productType
     */
    public Long getProductType() {
        return productType;
    }

    /**
     * @param productType
     *            the productType to set
     */
    public void setProductType(Long productType) {
        this.productType = productType;
    }

    /**
     * @return the cmcId
     */
    public Long getCmcId() {
        return cmcId;
    }

    /**
     * @param cmcId
     *            the cmcId to set
     */
    public void setCmcId(Long cmcId) {
        this.cmcId = cmcId;
    }

    /**
     * @return the cmcConfigService
     */
    public CmcConfigService getCmcConfigService() {
        return cmcConfigService;
    }

    /**
     * @param cmcConfigService
     *            the cmcConfigService to set
     */
    public void setCmcConfigService(CmcConfigService cmcConfigService) {
        this.cmcConfigService = cmcConfigService;
    }

    /**
     * @return the cmcDhcpBaseConfig
     */
    public JSONObject getCmcDhcpBaseConfig() {
        return cmcDhcpBaseConfig;
    }

    /**
     * @param cmcDhcpBaseConfig
     *            the cmcDhcpBaseConfig to set
     */
    public void setCmcDhcpBaseConfig(JSONObject cmcDhcpBaseConfig) {
        this.cmcDhcpBaseConfig = cmcDhcpBaseConfig;
    }

    /**
     * @return the modifyCmcDhcpBaseConfig
     */
    public CmcDhcpBaseConfig getModifyCmcDhcpBaseConfig() {
        return modifyCmcDhcpBaseConfig;
    }

    /**
     * @param modifyCmcDhcpBaseConfig
     *            the modifyCmcDhcpBaseConfig to set
     */
    public void setModifyCmcDhcpBaseConfig(CmcDhcpBaseConfig modifyCmcDhcpBaseConfig) {
        this.modifyCmcDhcpBaseConfig = modifyCmcDhcpBaseConfig;
    }

    /**
     * @return the dhcpRelayConfig
     */
    public JSONObject getDhcpRelayConfig() {
        return dhcpRelayConfig;
    }

    /**
     * @param dhcpRelayConfig
     *            the dhcpRelayConfig to set
     */
    public void setDhcpRelayConfig(JSONObject dhcpRelayConfig) {
        this.dhcpRelayConfig = dhcpRelayConfig;
    }

    /**
     * @return the addOption60
     */
    public List<String> getAddOption60() {
        return addOption60;
    }

    /**
     * @param addOption60
     *            the addOption60 to set
     */
    public void setAddOption60(List<String> addOption60) {
        this.addOption60 = addOption60;
    }

    /**
     * @return the addOption60Type
     */
    public List<Integer> getAddOption60Type() {
        return addOption60Type;
    }

    /**
     * @param addOption60Type
     *            the addOption60Type to set
     */
    public void setAddOption60Type(List<Integer> addOption60Type) {
        this.addOption60Type = addOption60Type;
    }

    /**
     * @return the delOption60
     */
    public List<Long> getDelOption60() {
        return delOption60;
    }

    /**
     * @param delOption60
     *            the delOption60 to set
     */
    public void setDelOption60(List<Long> delOption60) {
        this.delOption60 = delOption60;
    }

    /**
     * @return the addServerList
     */
    public List<String> getAddServerList() {
        return addServerList;
    }

    /**
     * @param addServerList
     *            the addServerList to set
     */
    public void setAddServerList(List<String> addServerList) {
        this.addServerList = addServerList;
    }

    /**
     * @return the addServerTypeList
     */
    public List<Integer> getAddServerTypeList() {
        return addServerTypeList;
    }

    /**
     * @param addServerTypeList
     *            the addServerTypeList to set
     */
    public void setAddServerTypeList(List<Integer> addServerTypeList) {
        this.addServerTypeList = addServerTypeList;
    }

    /**
     * @return the delServerList
     */
    public List<Long> getDelServerList() {
        return delServerList;
    }

    /**
     * @param delServerList
     *            the delServerList to set
     */
    public void setDelServerList(List<Long> delServerList) {
        this.delServerList = delServerList;
    }

    /**
     * @return the addVirtualIp
     */
    public List<String> getAddVirtualIp() {
        return addVirtualIp;
    }

    /**
     * @param addVirtualIp
     *            the addVirtualIp to set
     */
    public void setAddVirtualIp(List<String> addVirtualIp) {
        this.addVirtualIp = addVirtualIp;
    }

    /**
     * @return the addVirtualIpMask
     */
    public List<String> getAddVirtualIpMask() {
        return addVirtualIpMask;
    }

    /**
     * @param addVirtualIpMask
     *            the addVirtualIpMask to set
     */
    public void setAddVirtualIpMask(List<String> addVirtualIpMask) {
        this.addVirtualIpMask = addVirtualIpMask;
    }

    /**
     * @return the delVirtualIp
     */
    public List<String> getDelVirtualIp() {
        return delVirtualIp;
    }

    /**
     * @param delVirtualIp
     *            the delVirtualIp to set
     */
    public void setDelVirtualIp(List<String> delVirtualIp) {
        this.delVirtualIp = delVirtualIp;
    }

    /**
     * @return the giAddrList
     */
    public List<String> getGiAddrList() {
        return giAddrList;
    }

    /**
     * @param giAddrList
     *            the giAddrList to set
     */
    public void setGiAddrList(List<String> giAddrList) {
        this.giAddrList = giAddrList;
    }

    /**
     * @return the giAddrMaskList
     */
    public List<String> getGiAddrMaskList() {
        return giAddrMaskList;
    }

    /**
     * @param giAddrMaskList
     *            the giAddrMaskList to set
     */
    public void setGiAddrMaskList(List<String> giAddrMaskList) {
        this.giAddrMaskList = giAddrMaskList;
    }

    /**
     * @return the giAddrTypeList
     */
    public List<Integer> getGiAddrTypeList() {
        return giAddrTypeList;
    }

    /**
     * @param giAddrTypeList
     *            the giAddrTypeList to set
     */
    public void setGiAddrTypeList(List<Integer> giAddrTypeList) {
        this.giAddrTypeList = giAddrTypeList;
    }

    /**
     * @return the bundleInterface
     */
    public String getBundleInterface() {
        return bundleInterface;
    }

    /**
     * @param bundleInterface
     *            the bundleInterface to set
     */
    public void setBundleInterface(String bundleInterface) {
        this.bundleInterface = bundleInterface;
    }

    /**
     * @return the policy
     */
    public Integer getPolicy() {
        return policy;
    }

    /**
     * @param policy
     *            the policy to set
     */
    public void setPolicy(Integer policy) {
        this.policy = policy;
    }

    /**
     * @return the cableSourceVerify
     */
    public Integer getCableSourceVerify() {
        return cableSourceVerify;
    }

    /**
     * @param cableSourceVerify
     *            the cableSourceVerify to set
     */
    public void setCableSourceVerify(Integer cableSourceVerify) {
        this.cableSourceVerify = cableSourceVerify;
    }

    /**
     * @return the primaryIp
     */
    public String getPrimaryIp() {
        return primaryIp;
    }

    /**
     * @param primaryIp
     *            the primaryIp to set
     */
    public void setPrimaryIp(String primaryIp) {
        this.primaryIp = primaryIp;
    }

    /**
     * @return the primaryIpMask
     */
    public String getPrimaryIpMask() {
        return primaryIpMask;
    }

    /**
     * @param primaryIpMask
     *            the primaryIpMask to set
     */
    public void setPrimaryIpMask(String primaryIpMask) {
        this.primaryIpMask = primaryIpMask;
    }

    /**
     * @return the deviceType
     */
    public Integer getDeviceType() {
        return deviceType;
    }

    /**
     * @param deviceType
     *            the deviceType to set
     */
    public void setDeviceType(Integer deviceType) {
        this.deviceType = deviceType;
    }

    /**
     * @return the option60Str
     */
    public String getOption60Str() {
        return option60Str;
    }

    /**
     * @param option60Str
     *            the option60Str to set
     */
    public void setOption60Str(String option60Str) {
        this.option60Str = option60Str;
    }

    /**
     * @return the dhcpServerIp
     */
    public String getDhcpServerIp() {
        return dhcpServerIp;
    }

    /**
     * @param dhcpServerIp
     *            the dhcpServerIp to set
     */
    public void setDhcpServerIp(String dhcpServerIp) {
        this.dhcpServerIp = dhcpServerIp;
    }

    /**
     * @return the vlan
     */
    public List<Integer> getVlan() {
        return vlan;
    }

    /**
     * @param vlan
     *            the vlan to set
     */
    public void setVlan(List<Integer> vlan) {
        this.vlan = vlan;
    }

    /**
     * @return the priotity
     */
    public List<Integer> getPriotity() {
        return priotity;
    }

    /**
     * @param priotity
     *            the priotity to set
     */
    public void setPriotity(List<Integer> priotity) {
        this.priotity = priotity;
    }

    /**
     * @return the cmcDhcpIntIpList
     */
    public JSONArray getCmcDhcpIntIpList() {
        return cmcDhcpIntIpList;
    }

    /**
     * @param cmcDhcpIntIpList
     *            the cmcDhcpIntIpList to set
     */
    public void setCmcDhcpIntIpList(JSONArray cmcDhcpIntIpList) {
        this.cmcDhcpIntIpList = cmcDhcpIntIpList;
    }

    /**
     * @return the option60s
     */
    public JSONArray getOption60s() {
        return option60s;
    }

    /**
     * @param option60s
     *            the option60s to set
     */
    public void setOption60s(JSONArray option60s) {
        this.option60s = option60s;
    }

    public CmcVlanService getCmcVlanService() {
        return cmcVlanService;
    }

    public void setCmcVlanService(CmcVlanService cmcVlanService) {
        this.cmcVlanService = cmcVlanService;
    }

    public JSONArray getVlanList() {
        return vlanList;
    }

    public void setVlanList(JSONArray vlanList) {
        this.vlanList = vlanList;
    }

    public Integer getAction() {
        return action;
    }

    public void setAction(Integer action) {
        this.action = action;
    }

    public JSONArray getBundleEndList() {
        return bundleEndList;
    }

    public void setBundleEndList(JSONArray bundleEndList) {
        this.bundleEndList = bundleEndList;
    }

    public JSONObject getBundleConfig() {
        return bundleConfig;
    }

    public void setBundleConfig(JSONObject bundleConfig) {
        this.bundleConfig = bundleConfig;
    }

    public JSONArray getGiAddrArray() {
        return giAddrArray;
    }

    public void setGiAddrArray(JSONArray giAddrArray) {
        this.giAddrArray = giAddrArray;
    }

    public JSONArray getPacketVlanArray() {
        return packetVlanArray;
    }

    public void setPacketVlanArray(JSONArray packetVlanArray) {
        this.packetVlanArray = packetVlanArray;
    }

    public JSONArray getIntIpArray() {
        return intIpArray;
    }

    public void setIntIpArray(JSONArray intIpArray) {
        this.intIpArray = intIpArray;
    }

    public JSONArray getDhcpServerArray() {
        return dhcpServerArray;
    }

    public void setDhcpServerArray(JSONArray dhcpServerArray) {
        this.dhcpServerArray = dhcpServerArray;
    }

    public JSONArray getDhcpOptionArray() {
        return dhcpOptionArray;
    }

    public void setDhcpOptionArray(JSONArray dhcpOptionArray) {
        this.dhcpOptionArray = dhcpOptionArray;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public Integer getOperationResult() {
        return operationResult;
    }

    public void setOperationResult(Integer operationResult) {
        this.operationResult = operationResult;
    }

	public CmcAttribute getCmcAttribute() {
		return cmcAttribute;
	}

	public void setCmcAttribute(CmcAttribute cmcAttribute) {
		this.cmcAttribute = cmcAttribute;
	}

}
