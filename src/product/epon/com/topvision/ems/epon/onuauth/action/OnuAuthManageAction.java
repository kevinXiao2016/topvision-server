/***********************************************************************
 * $Id: OnuAuthManageAction.java,v1.0 2015年4月17日 下午1:51:10 $
 * 
 * @author: loyal
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.onuauth.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.topvision.ems.epon.olt.action.AbstractEponAction;
import com.topvision.ems.epon.olt.domain.OltSlotAttribute;
import com.topvision.ems.epon.olt.service.OltPonService;
import com.topvision.ems.epon.olt.service.OltSlotService;
import com.topvision.ems.epon.onuauth.domain.OltAuthentication;
import com.topvision.ems.epon.onuauth.domain.OltOnuAuthStatistics;
import com.topvision.ems.epon.onuauth.domain.OltPonOnuAuthModeTable;
import com.topvision.ems.epon.onuauth.domain.OnuAuthFail;
import com.topvision.ems.epon.onuauth.domain.OnuAuthInfo;
import com.topvision.ems.epon.onuauth.domain.PonOnuAuthStatistics;
import com.topvision.ems.epon.onuauth.service.OnuAuthManageService;
import com.topvision.ems.epon.onuauth.service.OnuAuthService;
import com.topvision.ems.facade.domain.DiscoveryData;
import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.facade.domain.EntityType;
import com.topvision.ems.gpon.onuauth.service.GponOnuAuthService;
import com.topvision.ems.network.service.DiscoveryService;
import com.topvision.ems.network.service.EntityService;
import com.topvision.ems.template.service.EntityTypeService;
import com.topvision.framework.common.MacUtils;
import com.topvision.framework.utils.EponConstants;
import com.topvision.framework.utils.EponIndex;
import com.topvision.platform.domain.UserContext;

/**
 * @author loyal
 * @created @2015年4月17日-下午1:51:10
 * 
 */
@Controller("onuAuthManageAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class OnuAuthManageAction extends AbstractEponAction {
    private static final long serialVersionUID = -9204960412539863688L;
    private final Logger logger = LoggerFactory.getLogger(OnuAuthManageAction.class);
    private String name;
    private String ip;
    private Long entityId;
    private Long ponIndex;
    private Integer authType;
    private Integer authAction;
    private Long onuIndex;
    private Long ponId;
    private String mac;
    private String sn;
    private String password;
    private Integer onuId;
    private String onuPreType;
    private String ponIndexs;
    private String entityIds;
    private String onuIndexs;
    private Integer onuLevel;
    private JSONArray entityTypes = new JSONArray();
    private JSONArray ponListObject = new JSONArray();
    private JSONArray oltPonAuthModeObject = new JSONArray();
    private String addAuthListJson;
    @Autowired
    private EntityService entityService;
    @Autowired
    private EntityTypeService entityTypeService;
    @Autowired
    private OnuAuthManageService onuAuthManageService;
    @Autowired
    private OltPonService oltPonService;
    @Autowired
    private OnuAuthService onuAuthService;
    @Autowired
    private OltSlotService oltSlotService;
    @Autowired
    private GponOnuAuthService gponOnuAuthService;
    @Autowired
    private DiscoveryService<DiscoveryData> discoveryService;

    /**
     * 获取OLT ONU认证统计信息
     * 
     * @return
     * @throws IOException
     */
    public String getOltOnuAuthStatistics() throws IOException {
        Map<String, Object> json = new HashMap<String, Object>();
        Map<String, Object> queryMap = new HashMap<String, Object>();
        queryMap.put("start", start);
        queryMap.put("limit", limit);
        if (name != null && !"".equals(name)) {
            queryMap.put("name", name);
        }
        if (ip != null && !"".equals(ip)) {
            queryMap.put("ip", ip);
        }
        List<OltOnuAuthStatistics> oltOnuAuthStatisticsList = onuAuthManageService.getOltOnuAuthStatistics(queryMap);
        Long count = onuAuthManageService.getOltOnuAuthStatisticsCount(queryMap);
        json.put("data", oltOnuAuthStatisticsList);
        json.put("rowCount", count);
        writeDataToAjax(JSONObject.fromObject(json));
        return NONE;
    }

    /**
     * 获取OLT PON认证统计信息
     * 
     * @return
     */
    public String getPonOnuAuthStatistics() {
        JSONObject ponOnuAuthStatisticsJson = new JSONObject();
        List<PonOnuAuthStatistics> ponOnuAuthStatisticsList = onuAuthManageService.getPonOnuAuthStatistics(entityId);
        ponOnuAuthStatisticsJson.put("data", ponOnuAuthStatisticsList);
        writeDataToAjax(ponOnuAuthStatisticsJson);
        return NONE;
    }

    /**
     * 获取OLT列表
     * 
     * @return
     */
    public String getOltList() {
        JSONObject oltListJson = new JSONObject();
        List<Entity> oltList = new ArrayList<Entity>();
        oltList = entityService.getEntityByType(entityTypeService.getOltType());
        oltListJson.put("data", oltList);
        writeDataToAjax(oltListJson);
        return NONE;
    }

    /**
     * 获取ONU认证信息列表
     * 
     * @return
     */
    public String getOnuAuth() {
        JSONObject onuAuthJson = new JSONObject();
        Map<String, Object> queryMap = new HashMap<String, Object>();
        UserContext uc = (UserContext) super.getSession().get(UserContext.KEY);
        String macRule = uc.getMacDisplayStyle();
        queryMap.put("start", start);
        queryMap.put("limit", limit);
        if (entityId != null && !"".equals(entityId)) {
            queryMap.put("entityId", entityId);
        }
        if (ponIndex != null && !"".equals(ponIndex)) {
            Long ponId = oltPonService.getPonIdByIndex(entityId, ponIndex);
            if (ponId != null && !"".equals(ponId)) {
                queryMap.put("ponId", ponId);
            }
        }
        if (authType != null && !"".equals(authType) && authType > 0) {
            queryMap.put("authType", authType);
        }
        if (authType != null && !"".equals(authType) && authType < 0) {
            queryMap.put("entityId", "-1");
        }
        List<OnuAuthInfo> onuAuthList = new ArrayList<OnuAuthInfo>();
        onuAuthList = onuAuthManageService.getOnuAuthList(queryMap);
        for (int i = 0; i < onuAuthList.size(); i++) {
            String mac = onuAuthList.get(i).getMac();
            if (mac != null) {
                onuAuthList.get(i).setMac(MacUtils.convertMacToDisplayFormat(mac, macRule));
            }
        }
        Long count = onuAuthManageService.getOnuAuthListCount(queryMap);
        onuAuthJson.put("data", onuAuthList);
        onuAuthJson.put("rowCount", count);
        writeDataToAjax(onuAuthJson);
        return NONE;
    }

    /**
     * 获取ONU认证失败列表
     * 
     * @return
     */
    public String getOnuAuthFail() {
        JSONObject onuAuthFailJson = new JSONObject();
        Map<String, Object> queryMap = new HashMap<String, Object>();
        UserContext uc = (UserContext) super.getSession().get(UserContext.KEY);
        String macRule = uc.getMacDisplayStyle();
        queryMap.put("start", start);
        queryMap.put("limit", limit);
        if (entityId != null && !"".equals(entityId)) {
            queryMap.put("entityId", entityId);
        }
        if (ponIndex != null && !"".equals(ponIndex)) {
            Long ponId = oltPonService.getPonIdByIndex(entityId, ponIndex);
            if (ponId != null && !"".equals(ponId)) {
                queryMap.put("ponId", ponId);
            }
        }
        List<OnuAuthFail> onuAuthFailList = new ArrayList<OnuAuthFail>();
        onuAuthFailList = onuAuthManageService.getOnuAuthFailList(queryMap);
        for (int i = 0; i < onuAuthFailList.size(); i++) {
            String mac = onuAuthFailList.get(i).getMac();
            if (mac != null) {
                onuAuthFailList.get(i).setMac(MacUtils.convertMacToDisplayFormat(mac, macRule));
            }
        }
        Long count = onuAuthManageService.getOnuAuthFailListCount(queryMap);
        onuAuthFailJson.put("data", onuAuthFailList);
        onuAuthFailJson.put("rowCount", count);
        writeDataToAjax(onuAuthFailJson);
        return NONE;
    }

    /**
     * 获取ONU认证失败列表
     * 
     * @return
     */
    public String getOnuAuthFailByIds() {
        JSONObject onuAuthFailJson = new JSONObject();
        UserContext uc = (UserContext) super.getSession().get(UserContext.KEY);
        String macRule = uc.getMacDisplayStyle();
        Map<String, Object> queryMap = new HashMap<String, Object>();
        List<OnuAuthFail> onuAuthFailList = new ArrayList<OnuAuthFail>();
        queryMap.put("start", start);
        queryMap.put("limit", limit);
        String[] entityIdArray = entityIds.split(",");
        String[] onuIndexArray = onuIndexs.split(",");
        for (int i = 0, size = entityIdArray.length; i < size; i++) {
            queryMap.put("entityId", entityIdArray[i]);
            queryMap.put("onuIndex", onuIndexArray[i]);
            List<OnuAuthFail> temp = onuAuthManageService.getOnuAuthFailList(queryMap);
            onuAuthFailList.addAll(temp);
        }
        for (int i = 0; i < onuAuthFailList.size(); i++) {
            String mac = onuAuthFailList.get(i).getMac();
            // 设置默认值
            onuAuthFailList.get(i).setAuthAction(1);
            onuAuthFailList.get(i).setOnuPreType(0);
            onuAuthFailList.get(i).setOnuId(EponIndex.getOnuNo(onuAuthFailList.get(i).getOnuIndex()));
            if (mac != null) {
                onuAuthFailList.get(i).setMac(MacUtils.convertMacToDisplayFormat(mac, macRule));
            }
        }
        onuAuthFailJson.put("data", onuAuthFailList);
        onuAuthFailJson.put("rowCount", onuAuthFailList.size());
        writeDataToAjax(onuAuthFailJson);
        return NONE;
    }

    /**
     * 获取OLT PON INDEX 列表
     * 
     * @return
     */
    public String getOltPonIndex() {
        JSONObject ponIndexListJson = new JSONObject();
        List<Long> ponIndexList = oltPonService.getAllEponIndex(entityId);
        ponIndexListJson.put("data", ponIndexList);
        writeDataToAjax(ponIndexListJson);
        return NONE;
    }

    public String showAddOnuAuth() {
        List<Long> ponIndexList = oltPonService.getAllEponIndex(entityId);
        List<OltPonOnuAuthModeTable> oltPonAuthModeList = onuAuthService.getOnuAuthEnable(entityId);
        ponListObject = JSONArray.fromObject(ponIndexList);
        oltPonAuthModeObject = JSONArray.fromObject(oltPonAuthModeList);

        List<EntityType> entityTypeList = new ArrayList<EntityType>();
        Long onuType = entityTypeService.getOnuType();
        entityTypeList = entityTypeService.loadSubType(onuType);
        entityTypes = JSONArray.fromObject(entityTypeList);
        return SUCCESS;
    }

    public String showAddOnuAuthList() {
        List<EntityType> entityTypeList = new ArrayList<EntityType>();
        Long onuType = entityTypeService.getOnuType();
        entityTypeList = entityTypeService.loadSubType(onuType);
        entityTypes = JSONArray.fromObject(entityTypeList);
        return SUCCESS;
    }

    public String addOnuAuth() {
        String result = "success";
        try {
            OltAuthentication oltAuthentication = new OltAuthentication();
            oltAuthentication.setEntityId(entityId);
            oltAuthentication.setAuthType(authType);
            oltAuthentication.setAuthAction(authAction);
            oltAuthentication.setOnuAuthenMacAddress(mac);
            Integer slotId = EponIndex.getSlotNo(ponIndex).intValue();
            Integer ponId = EponIndex.getPonNo(ponIndex).intValue();
            onuIndex = EponIndex.getOnuIndex(slotId, ponId, onuId);
            oltAuthentication.setOnuIndex(onuIndex);
            oltAuthentication.setTopOnuAuthLogicSn(sn);
            oltAuthentication.setTopOnuAuthPassword(password);
            onuAuthService.addOnuAuthenPreConfig(oltAuthentication);
            if (mac != null && EponConstants.OLT_AUTHEN_MACACTION_ACCEPT.equals(authAction)) {
                onuAuthService.deleteOnuAuthBlockByMac(entityId, ponIndex, mac);
            } else if (!("").equalsIgnoreCase(sn)) {
                onuAuthService.deleteOnuAuthBlockByMac(entityId, ponIndex, mac);
            }
            if (!(("").equalsIgnoreCase(onuPreType) || (EponConstants.OLT_AUTHEN_MAC.equals(authType) && EponConstants.OLT_AUTHEN_MACACTION_REJECT
                    .equals(authAction)))) {
                onuAuthService.modifyOnuPreType(entityId, onuIndex, onuPreType);
            }
            Map<String, Integer> onuLevelCacheMap = onuAuthService.getOnuLevelCache();
            onuLevelCacheMap.put(entityId + "_" + onuIndex, onuLevel);
            discoveryService.refresh(entityId);
        } catch (Exception ex) {
            if (logger.isDebugEnabled()) {
                logger.debug("addOnuAuth error:{}", ex);
            }
            result = "error";
        } finally {
            writeDataToAjax(result);
        }
        return NONE;
    }

    public String addOnuAuthList() {
        JSONArray addAuthArray = JSONArray.fromObject(addAuthListJson);
        List<Long> entityList = new ArrayList<Long>();
        String[] entitys = entityIds.split(",");
        JSONObject resultJson = onuAuthManageService.addOnuAuthList(addAuthArray);
        for (int i = 0; i < entitys.length; i++) {
            Long entityId = Long.parseLong(entitys[i]);
            if (entityList.indexOf(entityId) == -1) {
                entityList.add(entityId);
                discoveryService.refresh(entityId);
            }
        }
        writeDataToAjax(resultJson);
        return NONE;
    }

    public String deleteOnuAuth() {
        String result = "success";
        try {
            onuAuthService.deleteAuthenPreConfig(entityId, ponId, onuIndex, authType);
            // 刷新认证及阻塞表
            onuAuthService.refreshOnuAuthInfo(entityId);
            onuAuthService.refreshOnuAuthenBlockList(entityId);
        } catch (Exception ex) {
            if (logger.isDebugEnabled()) {
                logger.debug("deleteOnuAuth error:{}", ex);
            }
            result = "error";
        } finally {
            writeDataToAjax(result);
        }
        return NONE;
    }

    public String refreshOnuAuthInfo() {
        String result = "success";
        try {
            onuAuthService.refreshOnuAuthInfo(entityId);
            onuAuthService.refreshOnuAuthMode(entityId);
            onuAuthService.refreshOnuAuthEnable(entityId);
            onuAuthService.refreshOnuAuthPreType(entityId);
            onuAuthService.refreshOnuAuthenBlockList(entityId);
            gponOnuAuthService.refreshGponOnuAuth(entityId);
        } catch (Exception ex) {
            if (logger.isDebugEnabled()) {
                logger.debug("refreshOnuAuthInfo error:{}", ex);
            }
            result = "error";
        } finally {
            writeDataToAjax(result);
        }
        return NONE;
    }

    public String showModifyPonAuthMode() {
        return SUCCESS;
    }

    public String showModifyAuthMode() {
        return SUCCESS;
    }

    public String fetchPonFolders() throws IOException {
        List<Long> ponIndexList = oltPonService.getAllPonIndex(entityId);
        List<OltSlotAttribute> oltSlotList = oltSlotService.getOltPonSlotList(entityId);
        Entity entity = entityService.getEntity(entityId);
        List<Long> slotIndexList = new ArrayList<Long>();
        for (OltSlotAttribute oltSlotAttribute : oltSlotList) {
            slotIndexList.add(oltSlotAttribute.getSlotIndex());
        }
        JSONArray folderList = convertToZtreeFormat(slotIndexList, ponIndexList, entity.getName());
        folderList.write(response.getWriter());
        return NONE;
    }

    public String modifyPonAuthMode() {
        String result = "success";
        try {
            onuAuthService.modifyOnuAuthMode(entityId, ponIndex, authType);
        } catch (Exception ex) {
            if (logger.isDebugEnabled()) {
                logger.debug("modifyPonAuthMode error:{}", ex);
            }
            result = "error";
        } finally {
            writeDataToAjax(result);
        }
        return NONE;
    }

    public String modifyAuthMode() {
        String result = "success";
        String[] ponIndexArray = ponIndexs.split(",");
        List<Long> modifyPonIndex = new ArrayList<Long>();
        List<Long> ponIndexList = oltPonService.getAllPonIndex(entityId);
        for (int i = 0, size = ponIndexArray.length; i < size; i++) {
            if (ponIndexList.contains(new Long(ponIndexArray[i].trim()))) {
                modifyPonIndex.add(new Long(ponIndexArray[i].trim()));
            }
        }
        for (Long ponIndex : modifyPonIndex) {
            try {
                onuAuthService.modifyOnuAuthMode(entityId, ponIndex, authType);
            } catch (Exception ex) {
                if (logger.isDebugEnabled()) {
                    logger.debug("modifyPonAuthMode error:{}", ex);
                }
                result = "error";
            }
        }
        writeDataToAjax(result);
        return NONE;
    }

    private JSONArray convertToZtreeFormat(List<Long> slotIndexList, List<Long> ponIndexList, String oltName) {
        JSONArray folderList = new JSONArray();
        JSONObject olt = new JSONObject();
        olt.put("id", 0);
        olt.put("pId", 0);
        olt.put("name", oltName);
        olt.put("open", true);
        olt.put("chkDisabled", false);
        olt.put("checked", true);
        folderList.add(olt);
        for (Long slotIndex : slotIndexList) {
            JSONObject json = new JSONObject();
            json.put("id", slotIndex);
            json.put("pId", 0);
            json.put("name", EponIndex.getSlotNo(slotIndex));
            json.put("open", false);
            json.put("chkDisabled", false);
            json.put("checked", true);
            folderList.add(json);
        }
        for (Long ponIndex : ponIndexList) {
            JSONObject json = new JSONObject();
            json.put("id", ponIndex);
            json.put("pId", EponIndex.getSlotIndex(ponIndex));
            json.put("name", EponIndex.getSlotNo(ponIndex) + ":" + EponIndex.getPonNo(ponIndex));
            json.put("open", false);
            json.put("chkDisabled", false);
            json.put("checked", true);
            folderList.add(json);
        }
        return folderList;
    }

    public String getAuthOnuId() {
        JSONObject onuIdJson = new JSONObject();
        List<Long> onuIds = onuAuthManageService.getAuthOnuId(entityId, ponIndex, authAction);
        onuIdJson.put("data", onuIds);
        writeDataToAjax(onuIdJson);
        return NONE;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public Long getPonIndex() {
        return ponIndex;
    }

    public void setPonIndex(Long ponIndex) {
        this.ponIndex = ponIndex;
    }

    public Integer getAuthType() {
        return authType;
    }

    public void setAuthType(Integer authType) {
        this.authType = authType;
    }

    public String getOnuPreType() {
        return onuPreType;
    }

    public void setOnuPreType(String onuPreType) {
        this.onuPreType = onuPreType;
    }

    public JSONArray getEntityTypes() {
        return entityTypes;
    }

    public void setEntityTypes(JSONArray entityTypes) {
        this.entityTypes = entityTypes;
    }

    public JSONArray getPonListObject() {
        return ponListObject;
    }

    public void setPonListObject(JSONArray ponListObject) {
        this.ponListObject = ponListObject;
    }

    public JSONArray getOltPonAuthModeObject() {
        return oltPonAuthModeObject;
    }

    public void setOltPonAuthModeObject(JSONArray oltPonAuthModeObject) {
        this.oltPonAuthModeObject = oltPonAuthModeObject;
    }

    public Long getOnuIndex() {
        return onuIndex;
    }

    public void setOnuIndex(Long onuIndex) {
        this.onuIndex = onuIndex;
    }

    public Long getPonId() {
        return ponId;
    }

    public void setPonId(Long ponId) {
        this.ponId = ponId;
    }

    public Integer getAuthAction() {
        return authAction;
    }

    public void setAuthAction(Integer authAction) {
        this.authAction = authAction;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getOnuId() {
        return onuId;
    }

    public void setOnuId(Integer onuId) {
        this.onuId = onuId;
    }

    public String getPonIndexs() {
        return ponIndexs;
    }

    public void setPonIndexs(String ponIndexs) {
        this.ponIndexs = ponIndexs;
    }

    public String getEntityIds() {
        return entityIds;
    }

    public void setEntityIds(String entityIds) {
        this.entityIds = entityIds;
    }

    public String getOnuIndexs() {
        return onuIndexs;
    }

    public void setOnuIndexs(String onuIndexs) {
        this.onuIndexs = onuIndexs;
    }

    public String getAddAuthListJson() {
        return addAuthListJson;
    }

    public void setAddAuthListJson(String addAuthListJson) {
        this.addAuthListJson = addAuthListJson;
    }

    public Integer getOnuLevel() {
        return onuLevel;
    }

    public void setOnuLevel(Integer onuLevel) {
        this.onuLevel = onuLevel;
    }

}
