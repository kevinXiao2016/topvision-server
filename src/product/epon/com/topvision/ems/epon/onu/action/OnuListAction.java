/***********************************************************************

 * $Id: OnuListAction.java,v1.0 2015年4月23日 上午10:27:12 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.onu.action;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.topvision.ems.epon.domain.Onu;
import com.topvision.ems.epon.olt.service.OltService;
import com.topvision.ems.epon.onu.domain.OltOnuAttribute;
import com.topvision.ems.epon.onu.domain.OnuInfo;
import com.topvision.ems.epon.onu.domain.OnuLinkInfo;
import com.topvision.ems.epon.onu.domain.OnuWanConnect;
import com.topvision.ems.epon.onu.domain.OnuWanSsid;
import com.topvision.ems.epon.onu.domain.UniPort;
import com.topvision.ems.epon.onu.service.OnuAssemblyService;
import com.topvision.ems.epon.onu.service.OnuLinkService;
import com.topvision.ems.epon.onu.service.OnuService;
import com.topvision.ems.epon.onu.service.OnuWanService;
import com.topvision.ems.epon.onucpe.domain.OnuUniCpe;
import com.topvision.ems.epon.onucpe.service.OnuCpeService;
import com.topvision.ems.epon.performance.service.OnuPerfService;
import com.topvision.ems.epon.portinfo.domain.OltPortOpticalInfo;
import com.topvision.ems.epon.portinfo.service.OltPortInfoService;
import com.topvision.ems.epon.utils.OnuTypeConvertor;
import com.topvision.ems.epon.vlan.service.UniVlanService;
import com.topvision.ems.gpon.onu.domain.GponUniAttribute;
import com.topvision.ems.gpon.onu.service.GponOnuService;
import com.topvision.ems.gpon.onuauth.GponConstant;
import com.topvision.ems.network.domain.SubDeviceCount;
import com.topvision.framework.common.DateUtils;
import com.topvision.framework.common.MacUtils;
import com.topvision.framework.utils.EponConstants;
import com.topvision.framework.utils.EponIndex;
import com.topvision.framework.web.struts2.BaseAction;
import com.topvision.platform.ResourceManager;
import com.topvision.platform.domain.UserContext;
import com.topvision.platform.service.SystemPreferencesService;
import com.topvision.platform.util.CurrentRequest;

/**
 * @author Bravin
 * @created @2015年4月23日-上午10:27:12
 *
 */
@Controller("onuListAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class OnuListAction extends BaseAction {
    private static final long serialVersionUID = 312804567663559761L;

    @Autowired
    private OnuAssemblyService onuAssemblyService;
    @Autowired
    private OnuService onuService;
    @Autowired
    private OnuPerfService onuPerfService;
    @Autowired
    private SystemPreferencesService systemPreferencesService;
    @Autowired
    private OnuWanService onuWanService;
    @Autowired
    private UniVlanService uniVlanService;
    @Autowired
    private OnuCpeService onuCpeService;
    @Autowired
    private OnuLinkService onuLinkService;
    @Autowired
    private OltPortInfoService oltPortInfoService;
    @Autowired
    private OltService oltService;
    @Autowired
    private GponOnuService gponOnuService;

    private String queryContent;
    private String onuName;
    private String onuPreType;
    private Integer ponId;
    private Integer slotId;
    private Long entityId;
    private String macAddress;
    private String partitionData;
    private Integer receiveMin;
    private Integer receiveMax;
    private Integer transmitMin;
    private Integer transmitMax;
    private Integer ponRecvMin;
    private Integer ponRecvMax;
    private Long onuId;
    private Long onuIndex;
    private Integer status;
    private String jConnectedId;
    private Integer onuLevel;
    // onuId
    private List<String> onuIdList;
    private String onuEorG;
    private String cameraSwitch;
    private String pageId;
    private String tagName;
    private Integer tagId;
    // wanssid
    private Integer ssid;
    private OnuWanSsid onuWanSsid;
    // wan
    private Integer connectId;
    private OnuWanConnect onuWanConnect;

    /**
     * 查询ONU
     * 
     * @return
     * @throws IOException
     */
    public String queryForOnuList() throws IOException {
        ResourceManager resourceManager = ResourceManager.getResourceManager("com.topvision.ems.resources.resources");
        Map<String, Object> queryMap = new HashMap<String, Object>();
        queryMap.put("start", start);
        queryMap.put("limit", limit);
        queryMap.put("sort", sort);
        queryMap.put("dir", dir);
        // mysql中下划线是特殊的，like的时候必须转义
        if (queryContent != null) {// 简单查询模式
            if (queryContent.contains("_")) {
                queryContent = queryContent.replace("_", "\\_");
            }
            queryMap.put("queryContent", queryContent);
            String formatQueryMac = MacUtils.formatQueryMac(queryContent);
            if (formatQueryMac.indexOf(":") == -1) {
                queryMap.put("queryMacWithoutSplit", formatQueryMac);
            }
            queryMap.put("queryContentMac", formatQueryMac);
        } else {// 高级查询模式
            if (onuName != null && !"".equals(onuName)) {
                // mysql中下划线是特殊的，like的时候必须转义
                if (onuName.contains("_")) {
                    onuName = onuName.replace("_", "\\_");
                }
                queryMap.put("onuName", onuName);
            }
            if (status != null && status != -1) {
                queryMap.put("status", status);
            }
            if (onuEorG != null && onuEorG.length() != 0) {
                queryMap.put("onuEorG", onuEorG);
            }
            if (onuLevel != null) {
                queryMap.put("onuLevel", onuLevel);
            }
            if (onuPreType != null && !"".equals(onuPreType) && !"-1".equals(onuPreType)) {
                queryMap.put("onuPreType", onuPreType);
            }
            if (entityId != null && entityId != -1) {
                queryMap.put("entityId", entityId);
            }
            if (slotId != null && slotId != -1) {
                queryMap.put("slotId", slotId);
            }
            if (ponId != null && ponId != -1) {
                queryMap.put("ponId", ponId);
            }
            if (macAddress != null && !"".equals(macAddress)) {
                String formatQueryMac = MacUtils.formatQueryMac(macAddress);
                if (formatQueryMac.indexOf(":") == -1) {
                    queryMap.put("queryMacWithoutSplit", formatQueryMac);
                }
                queryMap.put("macAddress", formatQueryMac);
            }
            if (receiveMin != null) {
                queryMap.put("receiveMin", receiveMin);
            }
            if (receiveMax != null) {
                queryMap.put("receiveMax", receiveMax);
            }
            if (transmitMin != null) {
                queryMap.put("transmitMin", transmitMin);
            }
            if (transmitMax != null) {
                queryMap.put("transmitMax", transmitMax);
            }
            if (ponRecvMin != null) {
                queryMap.put("ponRecvMin", ponRecvMin);
            }
            if (ponRecvMax != null) {
                queryMap.put("ponRecvMax", ponRecvMax);
            }
        }

        List<OnuInfo> onuList = onuAssemblyService.queryForOnuList(queryMap);
        JSONObject json = new JSONObject();
        // add by fanzidong,需要在展示前格式化MAC地址
        UserContext uc = CurrentRequest.getCurrentUser();
        String displayRule = uc.getMacDisplayStyle();
        for (OnuInfo onuInfo : onuList) {
            String formatedMac = MacUtils.convertMacToDisplayFormat(onuInfo.getOnuMac(), displayRule);
            onuInfo.setOnuMac(formatedMac);
            // 对其它厂商的ONU处理,其它厂商的ONU类型是对字符串进行ascii码转换后的数字
            if (onuInfo.getOnuPreType() > EponConstants.UNKNOWN_ONU_TYPE
                    && onuInfo.getTypeId().intValue() == OltOnuAttribute.ONU_ENTITYTYPE_OTHERCORP) {
                onuInfo.setTypeName(OnuTypeConvertor.convertTypeName(onuInfo.getOnuPreType()));
            }
            if (GponConstant.GPON_ONU.equals(onuInfo.getOnuEorG())) {
                String[] unqStr = onuInfo.getOnuUniqueIdentification().split(":");
                String uniStr = "";
                if (unqStr.length == 8) {
                    for (int i = 0; i < 4; i++) {
                        uniStr += (char) (Integer.parseInt(unqStr[i], 16));
                    }
                }
                onuInfo.setOnuUniqueIdentification(onuInfo.getOnuUniqueIdentification().replaceAll(":", "") + "("
                        + uniStr + ")");
            }

            if (Integer.valueOf(EponConstants.ADMIN_STATUS_ENABLE).equals(onuInfo.getOnuOperationStatus())) {
                String onuRunTime = DateUtils.getTimePeriod(System.currentTimeMillis()
                        - onuInfo.getChangeTime().getTime() + +onuInfo.getOnuTimeSinceLastRegister() * 1000, uc
                        .getUser().getLanguage());
                onuInfo.setOnuRunTime(onuRunTime);
            } else {
                String onuRunTime = resourceManager.getNotNullString("deviceInfo.offline");
                if (onuInfo.getLastDeregisterTime() != null) {
                    String offlineInfo = "("
                            + DateUtils.getTimeDesInObscure(System.currentTimeMillis()
                                    - onuInfo.getLastDeregisterTime().getTime(), uc.getUser().getLanguage()) + ")";
                    onuRunTime = onuRunTime + offlineInfo;
                }
                onuInfo.setOnuRunTime(onuRunTime);
            }
        }
        int count = onuAssemblyService.queryForOnuCount(queryMap);
        json.put("rowCount", count);
        json.put("data", onuList);
        json.write(response.getWriter());
        return NONE;
    }

    public String loadOnuLocationInfo() throws IOException {
        Onu onu = onuService.getOnuStructure(onuId);
        if (onu != null) {
            OnuLinkInfo onuLinkInfo = onuLinkService.getOnuLinkInfo(onuId);
            OltPortOpticalInfo oltPortOpticalInfo = new OltPortOpticalInfo();
            try {
                oltPortOpticalInfo = oltPortInfoService.refreshPonOpticalInfo(onuLinkInfo.getEntityId(),
                        onuLinkInfo.getPortIndex(), onuLinkInfo.getPonId(), onuLinkInfo.getPerfStats());
            } catch (Exception e) {
                String portLocation = EponIndex.getPortStringByIndex(onuLinkInfo.getPortIndex()).toString();
                oltPortOpticalInfo.setPortLocation(portLocation);
                logger.debug("oltPortOpticalInfo refreshPonOpticalInfo failed" + e.getMessage());
            }
            SubDeviceCount subCount = oltService.getSubCountInfo(onuLinkInfo.getEntityId());
            List<OnuWanSsid> onuWanSsidList = onuWanService.getOnuWanSsid(onuId);
            List<OnuWanConnect> onuWanConnectList = onuWanService.getOnuWanConnect(onuId);
            // uniPorts在EPON、GPON下分别获取
            if (EponConstants.EPON_ONU.equals(onuLinkInfo.getOnuEorG())) {
                List<UniPort> eponUniPorts = onuAssemblyService.loadUniList(onuId);
                onu.setEponUniPorts(eponUniPorts);
            } else if (GponConstant.GPON_ONU.equals(onuLinkInfo.getOnuEorG())) {
                List<GponUniAttribute> gponUniPorts = gponOnuService.loadGponOnuUniList(onuId);
                onu.setGponUniPorts(gponUniPorts);
            }
            List<OnuUniCpe> cpeList = onuCpeService.loadOnuUniCpeList(onuId);
            onu.setOnuLinkInfo(onuLinkInfo);
            onu.setOltPortOpticalInfo(oltPortOpticalInfo);
            onu.setSubCount(subCount);
            onu.setOnuWanConnectList(onuWanConnectList);
            onu.setOnuWanSsidList(onuWanSsidList);
            onu.setCpeNum(cpeList.size());
        }
        writeDataToAjax(onu);
        return NONE;
    }

    /**
     * 刷新ONU信号质量等
     * 
     * @return
     */
    public String refreshOnuQuality() {
        onuAssemblyService.refreshOnuQuality(entityId, onuId, onuIndex);
        return NONE;
    }

    /**
     * 刷新ONU信息，包括ONU链路信息，UNI信息，WAN信息，WLAN信息
     * 
     * @return
     */
    public String refreshOnuInfo() {
        onuAssemblyService.refreshOnuInfo(entityId, onuId, onuIndex);
        return NONE;
    }

    /**
     * 跳转onu标签页
     * 
     * @return
     */
    public String showOnuTagView() {
        return SUCCESS;
    }

    /**
     * 给onu打上标签
     * 
     * @return
     */
    public String saveOnuTagRelation() {
        onuAssemblyService.saveOnuTagRelation(onuId, tagId);
        return NONE;
    }

    /**
     * 跳转修改wifi 信息界面
     * 
     * @return
     */
    public String showWifiPasswordModifyView() {
        return SUCCESS;
    }

    /**
     * 修该ssid 名称密码
     * 
     * @return
     */
    public String modifyWifiPassord() {
        String result = "success";
        try {
            onuWanService.updateWifiPassword(onuWanSsid);
        } catch (Exception e) {
            logger.error("modify wifi password failure", e);
            result = "error";
        }
        writeDataToAjax(result);
        return NONE;
    }

    /**
     * 跳转修改wan PPPoE密码界面
     * 
     * @return
     */
    public String showWanPasswordModifyView() {
        return SUCCESS;
    }

    /**
     * 修改wan PPPoE密码
     * 
     * @return
     */
    public String modifyWanPPPoEPassord() {
        String result = "success";
        try {
            if (onuWanConnect != null) {
                if (onuWanConnect.getIpMode() == EponConstants.ONU_WANCONNNECT_IPMODE_BRIGE) {// Bridge
                    onuWanConnect.setConnectMode(EponConstants.ONU_WANCONNNECT_WANMODE_BRIDGE);
                } else {
                    onuWanConnect.setConnectMode(EponConstants.ONU_WANCONNNECT_WANMODE_ROUTER);
                }
            }
            onuWanService.updateWanPPPoEPassord(onuWanConnect);
        } catch (Exception e) {
            logger.error("modify PPPoE password failure", e);
            result = "error";
        }
        writeDataToAjax(result);
        return NONE;
    }

    /**
     * ONU指标分区间选择
     * 
     * @return
     */
    public String showPartitionSelect() {
        return SUCCESS;
    }

    public String showOnuServerLevel() {
        return SUCCESS;
    }

    public String saveOnuServerLevel() {
        onuAssemblyService.saveOnuServerLevel(onuId, onuLevel);
        return NONE;
    }

    /**
     * 刷新ONU光功率信息
     * 
     * @return
     */
    public String refreshOnuOptical() {
        String sessionId = ServletActionContext.getRequest().getSession().getId();
        onuAssemblyService.refreshOnuOptical(onuIdList, jConnectedId, sessionId);
        return NONE;
    }

    /**
     * 查询ONU和CATV 24h内最低收光功率
     * 
     * @return
     */
    public String queryOnuOpticalHistory() {
        String sessionId = ServletActionContext.getRequest().getSession().getId();
        onuAssemblyService.queryOnuOpticalHistory(onuIdList, jConnectedId, sessionId);
        return NONE;
    }

    public String getQueryContent() {
        return queryContent;
    }

    public void setQueryContent(String queryContent) {
        this.queryContent = queryContent;
    }

    public String getOnuName() {
        return onuName;
    }

    public void setOnuName(String onuName) {
        this.onuName = onuName;
    }

    public String getOnuPreType() {
        return onuPreType;
    }

    public void setOnuPreType(String onuPreType) {
        this.onuPreType = onuPreType;
    }

    public Integer getPonId() {
        return ponId;
    }

    public void setPonId(Integer ponId) {
        this.ponId = ponId;
    }

    public Integer getSlotId() {
        return slotId;
    }

    public void setSlotId(Integer slotId) {
        this.slotId = slotId;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    public String getPartitionData() {
        return partitionData;
    }

    public void setPartitionData(String partitionData) {
        this.partitionData = partitionData;
    }

    public Integer getReceiveMin() {
        return receiveMin;
    }

    public void setReceiveMin(Integer receiveMin) {
        this.receiveMin = receiveMin;
    }

    public Integer getReceiveMax() {
        return receiveMax;
    }

    public void setReceiveMax(Integer receiveMax) {
        this.receiveMax = receiveMax;
    }

    public Integer getTransmitMin() {
        return transmitMin;
    }

    public void setTransmitMin(Integer transmitMin) {
        this.transmitMin = transmitMin;
    }

    public Integer getTransmitMax() {
        return transmitMax;
    }

    public void setTransmitMax(Integer transmitMax) {
        this.transmitMax = transmitMax;
    }

    public Integer getPonRecvMin() {
        return ponRecvMin;
    }

    public void setPonRecvMin(Integer ponRecvMin) {
        this.ponRecvMin = ponRecvMin;
    }

    public Integer getPonRecvMax() {
        return ponRecvMax;
    }

    public void setPonRecvMax(Integer ponRecvMax) {
        this.ponRecvMax = ponRecvMax;
    }

    public Long getOnuId() {
        return onuId;
    }

    public void setOnuId(Long onuId) {
        this.onuId = onuId;
    }

    public Long getOnuIndex() {
        return onuIndex;
    }

    public void setOnuIndex(Long onuIndex) {
        this.onuIndex = onuIndex;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getjConnectedId() {
        return jConnectedId;
    }

    public void setjConnectedId(String jConnectedId) {
        this.jConnectedId = jConnectedId;
    }

    public List<String> getOnuIdList() {
        return onuIdList;
    }

    public void setOnuIdList(List<String> onuIdList) {
        this.onuIdList = onuIdList;
    }

    public String getOnuEorG() {
        return onuEorG;
    }

    public void setOnuEorG(String onuEorG) {
        this.onuEorG = onuEorG;
    }

    public Integer getOnuLevel() {
        return onuLevel;
    }

    public void setOnuLevel(Integer onuLevel) {
        this.onuLevel = onuLevel;
    }

    public String getCameraSwitch() {
        return cameraSwitch;
    }

    public void setCameraSwitch(String cameraSwitch) {
        this.cameraSwitch = cameraSwitch;
    }

    public String getPageId() {
        return pageId;
    }

    public void setPageId(String pageId) {
        this.pageId = pageId;
    }

    public Integer getTagId() {
        return tagId;
    }

    public void setTagId(Integer tagId) {
        this.tagId = tagId;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public OnuWanSsid getOnuWanSsid() {
        return onuWanSsid;
    }

    public void setOnuWanSsid(OnuWanSsid onuWanSsid) {
        this.onuWanSsid = onuWanSsid;
    }

    public Integer getSsid() {
        return ssid;
    }

    public void setSsid(Integer ssid) {
        this.ssid = ssid;
    }

    public OnuWanConnect getOnuWanConnect() {
        return onuWanConnect;
    }

    public void setOnuWanConnect(OnuWanConnect onuWanConnect) {
        this.onuWanConnect = onuWanConnect;
    }

    public Integer getConnectId() {
        return connectId;
    }

    public void setConnectId(Integer connectId) {
        this.connectId = connectId;
    }
}
