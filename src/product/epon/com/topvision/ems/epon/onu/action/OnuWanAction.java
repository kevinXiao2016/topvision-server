/***********************************************************************
 * $Id: OnuWanAction.java,v1.0 2016年5月27日 上午9:58:21 $
 * 
 * @author: loyal
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.onu.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.topvision.ems.epon.domain.Onu;
import com.topvision.ems.epon.onu.domain.OltOnuAttribute;
import com.topvision.ems.epon.onu.domain.OnuWanConfig;
import com.topvision.ems.epon.onu.domain.OnuWanConnect;
import com.topvision.ems.epon.onu.domain.OnuWanSsid;
import com.topvision.ems.epon.onu.service.OnuService;
import com.topvision.ems.epon.onu.service.OnuWanService;
import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.network.service.EntityService;
import com.topvision.framework.annotation.OperationLogProperty;
import com.topvision.framework.utils.EponConstants;
import com.topvision.framework.utils.EponIndex;
import com.topvision.framework.web.struts2.BaseAction;
import com.topvision.platform.ResourceManager;
import com.topvision.platform.domain.OperationLog;

/**
 * @author loyal
 * @created @2016年5月27日-上午9:58:21
 * 
 */
@Controller("onuWanAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class OnuWanAction extends BaseAction {
    private static final long serialVersionUID = -5726930008568327371L;
    @Autowired
    private EntityService entityService;
    @Autowired
    private OnuWanService onuWanService;
    @Autowired
    private OnuService onuService;
    private Entity onu;
    private Long onuId;
    private Long entityId;
    private Long onuIndex;
    private Integer ssid;
    private String ssidName;
    private Integer encryptMode;
    private String password;
    private Integer ssidEnnable;
    private Integer ssidBroadcastEnnable;
    private Integer ssidMaxUser;
    private Integer connectId;
    private Integer connectMtu;
    private Integer connectMode;
    private String connectName;
    private Integer ipMode;
    private String ipv4Address;
    private String ipv4Dns;
    private String ipv4DnsAlternative;
    private String ipv4Gateway;
    private String pppoePassword;
    private String pppoeUserName;
    private Integer vlanId;
    private Integer vlanPriority;
    private String ipv4Mask;
    private Integer serviceMode;
    private List<String> wanIdList;
    private List<Integer> bindInterface;
    private OnuWanConfig onuWanConfig;
    private OltOnuAttribute onuAttribute;
    private String ssidList;
    private String source;// 操作日志source
    private Integer operationResult;

    /**
     * 恢复出厂设置
     * 
     * @return
     */
    public String restoreOnu() {
        String result = "success";
        try {
            onuWanService.restoreOnu(onuId, entityId);
        } catch (Exception e) {
            logger.error("saveOnuWanConfig error", e);
            result = "error";
        } finally {
            writeDataToAjax(result);
        }
        return NONE;
    }

    /**
     * 打开无线配置界面
     * 
     * @return
     */
    public String showOnuWanInfo() {
        onuAttribute = onuService.getOnuAttribute(onuId);
        onu = entityService.getEntity(onuId);
        Onu onuStructure = onuService.getOnuStructure(onuId);
        entityId = onuStructure.getEntityId();
        onuIndex = onuStructure.getOnuIndex();
        return SUCCESS;
    }

    /**
     * 读取WLAN配置（无线配置界面中）
     * 
     * @return
     */
    public String loadWLANConfig() {
        JSONObject json = new JSONObject();
        json.put("onuWanConfig", onuWanService.getOnuWanConfig(onuId));
        writeDataToAjax(json);
        return NONE;
    }

    /**
     * 保存WLAN配置
     * 
     * @return
     */
    public String saveWLANConfig() {
        onuWanService.saveWLANConfig(onuWanConfig);
        return NONE;
    }

    /**
     * 刷新WLAN配置
     * 
     * @return
     */
    public String refreshWLANConfig() {
        JSONObject json = new JSONObject();
        onuWanService.refreshOnuWanConfig(entityId, onuIndex);
        json.put("onuWanConfig", onuWanService.getOnuWanConfig(onuId));
        writeDataToAjax(json);
        return NONE;
    }

    /**
     * 打开新增WAN SSID界面
     * 
     * @return
     */
    public String showAddWanSsid() {
        onu = entityService.getEntity(onuId);
        return SUCCESS;
    }

    /**
     * 打开修改WAN SSID界面
     * 
     * @return
     */
    public String showModifyWanSsid() {
        onu = entityService.getEntity(onuId);
        return SUCCESS;
    }

    /**
     * 根据SSID获取该SSID的具体内容
     * 
     * @return
     */
    public String getWanSsid() {
        OnuWanSsid wanSsid = onuWanService.getOnuWanSsid(onuId, ssid);
        writeDataToAjax(JSONObject.fromObject(wanSsid));
        return NONE;
    }

    /**
     * 新增WAN SSID
     * 
     * @return
     */
    public String addWanSsid() {
        OnuWanSsid onuWanSsid = new OnuWanSsid();
        onuWanSsid.setEntityId(entityId);
        onuWanSsid.setOnuId(onuId);
        onuWanSsid.setOnuIndex(onuIndex);
        onuWanSsid.setSsid(ssid);
        onuWanSsid.setSsidName(ssidName);
        onuWanSsid.setEncryptMode(encryptMode);
        onuWanSsid.setPassword(password);
        onuWanSsid.setSsidBroadcastEnnable(ssidBroadcastEnnable);
        onuWanSsid.setSsidEnnable(ssidEnnable);
        onuWanSsid.setSsidMaxUser(ssidMaxUser);
        onuWanService.insertOnuWanSsid(onuWanSsid);
        return NONE;
    }

    /**
     * 修改 WAN SSID
     * 
     * @return
     */
    public String modifyWanSsid() {
        OnuWanSsid onuWanSsid = new OnuWanSsid();
        onuWanSsid.setEntityId(entityId);
        onuWanSsid.setOnuId(onuId);
        onuWanSsid.setOnuIndex(onuIndex);
        onuWanSsid.setSsid(ssid);
        onuWanSsid.setSsidName(ssidName);
        onuWanSsid.setEncryptMode(encryptMode);
        onuWanSsid.setPassword(password);
        onuWanSsid.setSsidBroadcastEnnable(ssidBroadcastEnnable);
        onuWanSsid.setSsidEnnable(ssidEnnable);
        onuWanSsid.setSsidMaxUser(ssidMaxUser);
        onuWanService.updateOnuWanSsid(onuWanSsid);
        return NONE;
    }

    /**
     * 删除WAN SSID
     * 
     * @return
     */
    public String deleteWanSsid() {
        onuWanService.deleteOnuWanSsid(onuId, onuIndex, entityId, ssid);
        return NONE;
    }

    /**
     * 刷新WAN SSID列表（会从设备读数据）
     * 
     * @return
     */
    public String refreshSSIDList() {
        onuWanService.refreshOnuWanSsid(entityId, onuIndex);
        return NONE;
    }

    /**
     * 读取WAN SSID列表
     * 
     * @return
     */
    public String getWanSsidList() {
        Map<String, Object> json = new HashMap<String, Object>();
        List<OnuWanSsid> wanSsidList = onuWanService.getOnuWanSsid(onuId);
        json.put("data", wanSsidList);
        json.put("rowCount", wanSsidList.size());
        writeDataToAjax(JSONObject.fromObject(json));
        return NONE;
    }

    /**
     * 打开WAN连接信息 界面
     * 
     * @return
     */
    public String showOnuWanStatus() {
        onu = entityService.getEntity(onuId);
        onuAttribute = onuService.getOnuAttribute(onuId);
        return SUCCESS;
    }

    /**
     * 显示WAN连接列表
     * 
     * @return
     */
    public String getWanConnectList() {
        Map<String, Object> json = new HashMap<String, Object>();
        LinkedHashMap<String, JSONObject> map = new LinkedHashMap<String, JSONObject>();
        List<OnuWanConnect> wanConnectList = onuWanService.getOnuWanConnect(onuId);
        String UnBindSSID = this.getUnBindInterfaces(onuId, "SSID");
        String UnBindLAN = this.getUnBindInterfaces(onuId, "LAN");
        for (OnuWanConnect onuWanConnect : wanConnectList) {
            insertGroupValue(map, getString("ONU.basicinfo"), getString("ONU.WAN.connectName"),
                    onuWanConnect.getConnectName(), onuWanConnect.getConnectId());
            insertGroupValue(map, getString("ONU.basicinfo"), getString("ONU.WAN.mtuConnectNum"), onuWanConnect
                    .getConnectMtu().toString(), onuWanConnect.getConnectId());
            insertGroupValue(map, getString("ONU.basicinfo"), getString("ONU.WAN.ipDistributeMode"),
                    onuWanConnect.getIpModeString(), onuWanConnect.getConnectId());
            insertGroupValue(map, getString("ONU.basicinfo"), getString("ONU.WAN.serviceMode"),
                    onuWanConnect.getServiceModeString(), onuWanConnect.getConnectId());
            insertGroupValue(map, getString("ONU.basicinfo"), getString("ONU.portStatus"),
                    onuWanConnect.getConnectStatusString(), onuWanConnect.getConnectId());
            insertGroupValue(map, getString("ONU.statusinfo"), getString("ONU.connecterrorcode"),
                    String.valueOf(onuWanConnect.getConnectErrorCode()), onuWanConnect.getConnectId());
            insertGroupValue(map, getString("ONU.WAN.vlanInfo"), "VLAN ID", String.valueOf(onuWanConnect.getVlanId()),
                    onuWanConnect.getConnectId());
            insertGroupValue(map, getString("ONU.WAN.vlanInfo"), getString("ONU.WAN.vlanPriority"),
                    String.valueOf(onuWanConnect.getVlanPriority()), onuWanConnect.getConnectId());
            insertGroupValue(map, getString("ONU.WAN.bindInterface"), "SSID" + UnBindSSID,
                    getBindInterfaces(onuId, onuWanConnect.getConnectId(), "SSID"), onuWanConnect.getConnectId());
            insertGroupValue(map, getString("ONU.WAN.bindInterface"), "LAN" + UnBindLAN,
                    getBindInterfaces(onuId, onuWanConnect.getConnectId(), "LAN"), onuWanConnect.getConnectId());
            insertGroupValue(map, getString("ONU.WAN.ipInfo"), getString("ONU.WAN.ipV4Addr"),
                    onuWanConnect.getPppoeStatusIpv4Addr(), onuWanConnect.getConnectId());
            insertGroupValue(map, getString("ONU.WAN.ipInfo"), getString("ONU.WAN.ipV4Mask"),
                    onuWanConnect.getPppoeStatusIpv4Mask(), onuWanConnect.getConnectId());
            insertGroupValue(map, getString("ONU.WAN.ipInfo"), getString("ONU.WAN.ipV4Gateway"),
                    onuWanConnect.getPppoeStatusIpv4Gw(), onuWanConnect.getConnectId());
            insertGroupValue(map, getString("ONU.WAN.ipInfo"), getString("ONU.WAN.ipV4Dns"),
                    onuWanConnect.getPppoeStatusIpv4DnsPrimary(), onuWanConnect.getConnectId());
            insertGroupValue(map, getString("ONU.WAN.ipInfo"), getString("ONU.WAN.ipV4AlternativeDns"),
                    onuWanConnect.getPppoeStatusIpv4DnsSecondary(), onuWanConnect.getConnectId());
            insertGroupValue(map, "PPPoE", getString("ONU.WAN.username"), onuWanConnect.getPppoeUserName(),
                    onuWanConnect.getConnectId());
            insertGroupValue(map, "001", getString("ONU.WAN.operation"), onuWanConnect.getConnectId().toString(),
                    onuWanConnect.getConnectId());
        }
        List<JSONObject> array = new ArrayList<JSONObject>();

        for (String key : map.keySet()) {
            array.add(map.get(key));
        }
        json.put("data", array);
        json.put("rowCount", array.size());
        writeDataToAjax(JSONObject.fromObject(json));
        return NONE;
    }

    /**
     * 显示新增WAN连接列表
     * 
     * @return
     */
    public String showAddWanConnect() {
        onu = entityService.getEntity(onuId);
        List<OnuWanConnect> wanConnectList = onuWanService.getOnuWanConnect(onuId);
        wanIdList = new ArrayList<String>();
        for (OnuWanConnect onuWanConnect : wanConnectList) {
            wanIdList.add(String.valueOf(onuWanConnect.getConnectId()));
        }
        return SUCCESS;
    }

    /**
     * 新增WAN连接
     * 
     * @return
     */
    @OperationLogProperty(actionName = "onuWanAction", operationName = "addWanConnect${source}")
    public String addWanConnect() throws Exception {
        String result;
        try {
            // 操作日志
            Long onuIndex = onuService.getOnuAttribute(onuId).getOnuIndex();
            source = "[" + EponIndex.getSlotNo(onuIndex).toString() + "/" + EponIndex.getPonNo(onuIndex).toString()
                    + ":" + EponIndex.getOnuNo(onuIndex).toString() + "-" + "WAN" + connectId + "]";

            OnuWanConnect onuWanConnect = new OnuWanConnect();
            onuWanConnect.setEntityId(entityId);
            onuWanConnect.setOnuId(onuId);
            onuWanConnect.setConnectId(connectId);
            // 基本信息
            onuWanConnect.setConnectMtu(connectMtu);
            onuWanConnect.setIpMode(ipMode);
            // onuWanConnect.setConnectName(connectName);
            onuWanConnect.setServiceMode(serviceMode);
            // VLAN信息
            onuWanConnect.setVlanPriority(vlanPriority);
            onuWanConnect.setVlanId(vlanId);
            if (ipMode == EponConstants.ONU_WANCONNNECT_IPMODE_BRIGE) {// Bridge 如果IP MODE是brige wan
                                                                       // mode固定为brige
                onuWanConnect.setConnectMode(EponConstants.ONU_WANCONNNECT_WANMODE_BRIDGE);
            } else {
                onuWanConnect.setConnectMode(EponConstants.ONU_WANCONNNECT_WANMODE_ROUTER);
            }
            if (ipMode == EponConstants.ONU_WANCONNNECT_IPMODE_PPPOE) {// PPPOE
                onuWanConnect.setPppoePassword(pppoePassword);
                onuWanConnect.setPppoeUserName(pppoeUserName);
            } else if (ipMode == EponConstants.ONU_WANCONNNECT_IPMODE_STATIC) { // IP信息
                onuWanConnect.setIpv4Mask(ipv4Mask);
                onuWanConnect.setIpv4DnsAlternative(ipv4DnsAlternative);
                onuWanConnect.setIpv4Address(ipv4Address);
                onuWanConnect.setIpv4Dns(ipv4Dns);
                onuWanConnect.setIpv4Gateway(ipv4Gateway);
            }
            onuWanService.insertOnuWanConnect(onuWanConnect);
            operationResult = OperationLog.SUCCESS;
            result = "success";
        } catch (Exception e) {
            operationResult = OperationLog.FAILURE;
            result = "error";
            logger.debug("addWanConnect error", e);
        }
        writeDataToAjax(result);
        return NONE;
    }

    /**
     * 显示修改WAN连接列表
     * 
     * @return
     */
    public String showModifyWanConnect() {
        onu = entityService.getEntity(onuId);
        return SUCCESS;
    }

    /**
     * 保存修改WAN连接列表
     * 
     * @return
     */
    @OperationLogProperty(actionName = "onuWanAction", operationName = "modifyWanConnect${source}")
    public String modifyWanConnect() throws Exception {
        String result;
        try {
            // 操作日志
            Long onuIndex = onuService.getOnuAttribute(onuId).getOnuIndex();
            source = "[" + EponIndex.getSlotNo(onuIndex).toString() + "/" + EponIndex.getPonNo(onuIndex).toString()
                    + ":" + EponIndex.getOnuNo(onuIndex).toString() + "-" + "WAN" + connectId + "]";

            OnuWanConnect onuWanConnect = new OnuWanConnect();
            onuWanConnect.setEntityId(entityId);
            onuWanConnect.setOnuId(onuId);
            onuWanConnect.setConnectId(connectId);
            // 基本信息
            onuWanConnect.setConnectMtu(connectMtu);
            onuWanConnect.setIpMode(ipMode);
            // onuWanConnect.setConnectName(connectName);
            onuWanConnect.setServiceMode(serviceMode);
            // VLAN信息
            onuWanConnect.setVlanPriority(vlanPriority);
            onuWanConnect.setVlanId(vlanId);
            if (ipMode == EponConstants.ONU_WANCONNNECT_IPMODE_BRIGE) {// Bridge
                onuWanConnect.setConnectMode(EponConstants.ONU_WANCONNNECT_WANMODE_BRIDGE);
            } else {
                onuWanConnect.setConnectMode(EponConstants.ONU_WANCONNNECT_WANMODE_ROUTER);
            }
            if (ipMode == EponConstants.ONU_WANCONNNECT_IPMODE_PPPOE) {// PPPOE
                onuWanConnect.setPppoePassword(pppoePassword);
                onuWanConnect.setPppoeUserName(pppoeUserName);
            } else if (ipMode == EponConstants.ONU_WANCONNNECT_IPMODE_STATIC) { // IP信息
                onuWanConnect.setIpv4Mask(ipv4Mask);
                onuWanConnect.setIpv4DnsAlternative(ipv4DnsAlternative);
                onuWanConnect.setIpv4Address(ipv4Address);
                onuWanConnect.setIpv4Dns(ipv4Dns);
                onuWanConnect.setIpv4Gateway(ipv4Gateway);
            }
            onuWanService.updateOnuWanConnect(onuWanConnect);
            operationResult = OperationLog.SUCCESS;
            result = "success";
        } catch (Exception e) {
            operationResult = OperationLog.FAILURE;
            result = "error";
            logger.debug("addWanConnect operation log error", e);
        }
        writeDataToAjax(result);
        return NONE;
    }

    /**
     * 根据connectID 读取WAN连接的信息
     * 
     * @return
     */
    public String loadWanConnection() {
        OnuWanConnect onuWanConnect = onuWanService.loadWanConnection(onuId, connectId);
        writeDataToAjax(JSONObject.fromObject(onuWanConnect));
        return NONE;
    }

    /**
     * 删除某一WAN连接
     * 
     * @return
     */
    @OperationLogProperty(actionName = "onuWanAction", operationName = "deleteWanConnect${source}")
    public String deleteWanConnect() throws Exception {
        String result = "success";
        try {
            // 操作日志
            Long onuIndex = onuService.getOnuAttribute(onuId).getOnuIndex();
            source = "[" + EponIndex.getSlotNo(onuIndex).toString() + "/" + EponIndex.getPonNo(onuIndex).toString()
                    + ":" + EponIndex.getOnuNo(onuIndex).toString() + "-" + "WAN" + connectId + "]";

            onuWanService.deleteOnuWanConnect(entityId, onuId, connectId);
            operationResult = OperationLog.SUCCESS;
        } catch (Exception ex) {
            operationResult = OperationLog.FAILURE;
            logger.error("deleteWanConnect error", ex);
            result = "error";
        } finally {
            writeDataToAjax(result);
        }
        return NONE;
    }

    /**
     * 清除所有WAN连接
     * 
     * @return
     */
    @OperationLogProperty(actionName = "onuWanAction", operationName = "clearWanConnection${source}")
    public String clearWanConnection() throws Exception {
        String result;
        try {
            // 操作日志
            Long onuIndex = onuService.getOnuAttribute(onuId).getOnuIndex();
            source = "[" + EponIndex.getSlotNo(onuIndex).toString() + "/" + EponIndex.getPonNo(onuIndex).toString()
                    + ":" + EponIndex.getOnuNo(onuIndex).toString() + "]";

            onuWanService.clearWanConnection(onuId, entityId);
            operationResult = OperationLog.SUCCESS;
            result = "success";
        } catch (Exception e) {
            operationResult = OperationLog.FAILURE;
            result = "error";
            logger.error("clearWanConnection error", e);
        }
        writeDataToAjax(result);
        return NONE;
    }

    /**
     * 刷新WAN连接
     * 
     * @return
     */
    public String refreshWanConnection() {
        onuWanService.refreshWanConnection(entityId, onuId);
        return NONE;
    }

    /**
     * 显示绑定端口界面
     * 
     * @return
     */
    public String showBindInterface() {
        return SUCCESS;
    }

    /**
     * 获取绑定端口
     * 
     * @return
     */
    public String loadBindInterface() {
        JSONObject json = new JSONObject();
        List<Integer> bindInterface = onuWanService.loadBindInterface(onuId, connectId);
        List<Integer> alreadyBindInterface = onuWanService.loadAlreadyBandInterface(onuId, connectId);
        json.put("bindInterface", bindInterface);
        json.put("alreadyBindInterface", alreadyBindInterface);
        writeDataToAjax(json);
        return NONE;
    }

    /**
     * 保存绑定端口
     * 
     * @return
     */
    @OperationLogProperty(actionName = "onuWanAction", operationName = "saveBindInterface${source}")
    public String saveBindInterface() throws Exception {
        String result;
        try {
            // 操作日志
            Long onuIndex = onuService.getOnuAttribute(onuId).getOnuIndex();
            source = "[" + EponIndex.getSlotNo(onuIndex).toString() + "/" + EponIndex.getPonNo(onuIndex).toString()
                    + ":" + EponIndex.getOnuNo(onuIndex).toString() + "-" + "WAN" + connectId + "]";
            onuWanService.saveBindInterface(entityId, onuId, connectId, bindInterface);
            operationResult = OperationLog.SUCCESS;
            result = "success";
        } catch (Exception e) {
            operationResult = OperationLog.FAILURE;
            result = "error";
            logger.error("saveBindInterface error", e);
        }
        writeDataToAjax(result);
        return NONE;
    }

    private void insertGroupValue(Map<String, JSONObject> map, String group, String valueName, String value,
            Integer connectId) {
        if (map.get(valueName) == null) {
            JSONObject jSONObject = new JSONObject();
            map.put(valueName, jSONObject);
        }
        JSONObject temp = map.get(valueName);
        temp.put("group", group);
        temp.put("name", valueName);
        temp.put("value" + connectId, value);
    }

    /**
     * 获取绑定端口的列表
     * 
     * @param onuId
     * @param connectId
     * @param interfaceType
     * @return
     */
    private String getBindInterfaces(Long onuId, Integer connectId, String interfaceType) {
        String result = "";
        List<Integer> interfaces = onuWanService.loadBindInterface(onuId, connectId);
        if ("SSID".equals(interfaceType)) {
            for (Integer i : interfaces) {
                if (i >= 1 && i <= 4) {
                    result += i + ",";
                }
            }
        } else if ("LAN".equals(interfaceType)) {
            for (Integer i : interfaces) {
                if (i >= 5 && i <= 12) {
                    result += (i - 4) + ",";
                }
            }
        }
        if (result.endsWith(",")) {
            result = result.substring(0, result.length() - 1);
        }
        return result;
    }

    /**
     * 获取未绑定端口的列表
     * 
     * @param onuId
     * @param interfaceType
     * @return
     */
    private String getUnBindInterfaces(Long onuId, String interfaceType) {
        String result = "";
        List<Integer> interfaces = onuWanService.loadBindInterface(onuId);
        if ("SSID".equals(interfaceType)) {
            for (int i = 1; i <= 4; i++) {
                if (!interfaces.contains(i)) {
                    result += i + ",";
                }
            }
        } else if ("LAN".equals(interfaceType)) {
            for (int i = 5; i <= 8; i++) {
                if (!interfaces.contains(i)) {
                    result += (i - 4) + ",";
                }
            }
        }
        if (result.endsWith(",")) {
            result = result.substring(0, result.length() - 1);
            result = getString("ONU.unbind") + result + ")";
        }
        return result;
    }

    public Entity getOnu() {
        return onu;
    }

    public void setOnu(Entity onu) {
        this.onu = onu;
    }

    public Long getOnuId() {
        return onuId;
    }

    public void setOnuId(Long onuId) {
        this.onuId = onuId;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public EntityService getEntityService() {
        return entityService;
    }

    public void setEntityService(EntityService entityService) {
        this.entityService = entityService;
    }

    public OnuWanService getOnuWanService() {
        return onuWanService;
    }

    public void setOnuWanService(OnuWanService onuWanService) {
        this.onuWanService = onuWanService;
    }

    public Integer getSsid() {
        return ssid;
    }

    public void setSsid(Integer ssid) {
        this.ssid = ssid;
    }

    public String getSsidName() {
        return ssidName;
    }

    public void setSsidName(String ssidName) {
        this.ssidName = ssidName;
    }

    public Integer getEncryptMode() {
        return encryptMode;
    }

    public void setEncryptMode(Integer encryptMode) {
        this.encryptMode = encryptMode;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getSsidEnable() {
        return ssidEnnable;
    }

    public void setSsidEnable(Integer ssidEnnable) {
        this.ssidEnnable = ssidEnnable;
    }

    public Integer getSsidBroadcastEnnable() {
        return ssidBroadcastEnnable;
    }

    public void setSsidBroadcastEnable(Integer ssidBroadcastEnnable) {
        this.ssidBroadcastEnnable = ssidBroadcastEnnable;
    }

    public Integer getSsidMaxUser() {
        return ssidMaxUser;
    }

    public void setSsidMaxUser(Integer ssidMaxUser) {
        this.ssidMaxUser = ssidMaxUser;
    }

    public Integer getSsidEnnable() {
        return ssidEnnable;
    }

    public void setSsidEnnable(Integer ssidEnnable) {
        this.ssidEnnable = ssidEnnable;
    }

    public Integer getConnectId() {
        return connectId;
    }

    public void setConnectId(Integer connectId) {
        this.connectId = connectId;
    }

    public Integer getConnectMtu() {
        return connectMtu;
    }

    public void setConnectMtu(Integer connectMtu) {
        this.connectMtu = connectMtu;
    }

    public Integer getConnectMode() {
        return connectMode;
    }

    public void setConnectMode(Integer connectMode) {
        this.connectMode = connectMode;
    }

    public String getConnectName() {
        return connectName;
    }

    public void setConnectName(String connectName) {
        this.connectName = connectName;
    }

    public Integer getIpMode() {
        return ipMode;
    }

    public void setIpMode(Integer ipMode) {
        this.ipMode = ipMode;
    }

    public String getIpv4Address() {
        return ipv4Address;
    }

    public void setIpv4Address(String ipv4Address) {
        this.ipv4Address = ipv4Address;
    }

    public String getIpv4Dns() {
        return ipv4Dns;
    }

    public void setIpv4Dns(String ipv4Dns) {
        this.ipv4Dns = ipv4Dns;
    }

    public String getIpv4DnsAlternative() {
        return ipv4DnsAlternative;
    }

    public void setIpv4DnsAlternative(String ipv4DnsAlternative) {
        this.ipv4DnsAlternative = ipv4DnsAlternative;
    }

    public String getIpv4Gateway() {
        return ipv4Gateway;
    }

    public void setIpv4Gateway(String ipv4Gateway) {
        this.ipv4Gateway = ipv4Gateway;
    }

    public String getPppoePassword() {
        return pppoePassword;
    }

    public void setPppoePassword(String pppoePassword) {
        this.pppoePassword = pppoePassword;
    }

    public String getPppoeUserName() {
        return pppoeUserName;
    }

    public void setPppoeUserName(String pppoeUserName) {
        this.pppoeUserName = pppoeUserName;
    }

    public Integer getVlanId() {
        return vlanId;
    }

    public void setVlanId(Integer vlanId) {
        this.vlanId = vlanId;
    }

    public Integer getVlanPriority() {
        return vlanPriority;
    }

    public void setVlanPriority(Integer vlanPriority) {
        this.vlanPriority = vlanPriority;
    }

    public void setSsidBroadcastEnnable(Integer ssidBroadcastEnnable) {
        this.ssidBroadcastEnnable = ssidBroadcastEnnable;
    }

    public String getIpv4Mask() {
        return ipv4Mask;
    }

    public void setIpv4Mask(String ipv4Mask) {
        this.ipv4Mask = ipv4Mask;
    }

    public Integer getServiceMode() {
        return serviceMode;
    }

    public void setServiceMode(Integer serviceMode) {
        this.serviceMode = serviceMode;
    }

    public List<String> getWanIdList() {
        return wanIdList;
    }

    public void setWanIdList(List<String> wanIdList) {
        this.wanIdList = wanIdList;
    }

    public List<Integer> getBindInterface() {
        return bindInterface;
    }

    public void setBindInterface(List<Integer> bindInterface) {
        this.bindInterface = bindInterface;
    }

    public OnuWanConfig getOnuWanConfig() {
        return onuWanConfig;
    }

    public void setOnuWanConfig(OnuWanConfig onuWanConfig) {
        this.onuWanConfig = onuWanConfig;
    }

    public Long getOnuIndex() {
        return onuIndex;
    }

    public void setOnuIndex(Long onuIndex) {
        this.onuIndex = onuIndex;
    }

    public OltOnuAttribute getOnuAttribute() {
        return onuAttribute;
    }

    public void setOnuAttribute(OltOnuAttribute onuAttribute) {
        this.onuAttribute = onuAttribute;
    }

    public String getSsidList() {
        return ssidList;
    }

    public void setSsidList(String ssidList) {
        this.ssidList = ssidList;
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

    protected String getString(String key) {
        String module = String.format("com.topvision.ems.epon.onu.resources");
        try {
            ResourceManager resourceManager = ResourceManager.getResourceManager(module);
            return resourceManager.getNotNullString(key);
        } catch (Exception e) {
            return key;
        }
    }

}
