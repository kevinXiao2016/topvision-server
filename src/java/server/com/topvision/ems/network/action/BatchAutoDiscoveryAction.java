/***********************************************************************
 * $Id: BatchAutoDiscoveryAction.java,v1.0 2014-5-11 上午11:33:32 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.network.action;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.topvision.ems.facade.domain.EntityType;
import com.topvision.ems.network.domain.BatchAutoDiscoveryEntityType;
import com.topvision.ems.network.domain.BatchAutoDiscoveryIps;
import com.topvision.ems.network.domain.BatchAutoDiscoveryPeriod;
import com.topvision.ems.network.domain.BatchAutoDiscoverySnmpConfig;
import com.topvision.ems.network.domain.FolderCategory;
import com.topvision.ems.network.domain.TopoFolder;
import com.topvision.ems.network.service.AutoDiscoveryService;
import com.topvision.ems.network.service.BatchAutoDiscoveryService;
import com.topvision.ems.network.service.TopologyService;
import com.topvision.ems.template.service.EntityTypeService;
import com.topvision.framework.web.struts2.BaseAction;
import com.topvision.platform.ResourceManager;
import com.topvision.platform.domain.SystemPreferences;
import com.topvision.platform.domain.UserContext;
import com.topvision.platform.service.SystemPreferencesService;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * @author haojie
 * @created @2014-5-11-上午11:33:32
 * 
 */
@Controller("batchAutoDiscoveryAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class BatchAutoDiscoveryAction extends BaseAction {
    private static final long serialVersionUID = -7439257453856980682L;
    @Autowired
    private BatchAutoDiscoveryService batchAutoDiscoveryService;
    @Autowired
    private SystemPreferencesService systemPreferencesService;
    @Autowired
    private EntityTypeService entityTypeService;
    @Autowired
    private AutoDiscoveryService autoDiscoveryService;
    private String snmpRetries;
    private String snmpTimeout;
    private String pingTimeoutConfig;
    @Autowired
    private TopologyService topologyService;
    private Long id;
    private String ids;
    private String name;
    private String ipInfo;
    private Long folderId;
    private Integer autoDiscovery;
    private JSONObject batchTopoData = new JSONObject();
    private Integer strategyType;
    private Long periodStart;
    private Long period;
    private String readCommunity;
    private String writeCommunity;
    private Integer version;
    private String username;
    private String authProtocol;
    private String authPassword;
    private String privProtocol;
    private String privPassword;
    private JSONObject netSegmentJson;
    private JSONObject snmpTabJson;
    private String action;
    private String queryName;
    private String operationId;

    // TODO demo 写死，后续必须修改
    private String[] sysObjectNotUse = { "1.3.6.1.4.1.32285.11.2.1", "1.3.6.1.4.1.32285.11.1.1.1",
            "1.3.6.1.4.1.32285.11.1.1.1.1", "1.3.6.1.4.1.32285.11.2.4", "1.3.6.1.4.1.32285.11.4.21",
            "1.3.6.1.4.1.32285.11.1.1.1.3.1" };
    private List<String> sysObjectNotUseList = Arrays.asList(sysObjectNotUse);

    /**
     * 跳转自动拓扑页面
     * 
     * @return
     */
    public String showBatchTopo() {
        // 所有设备类型查询
        List<EntityType> allTypes = entityTypeService.loadSubType(entityTypeService.getEntitywithipType());
        List<BatchAutoDiscoveryEntityType> typeIds = batchAutoDiscoveryService.getTypeIds();
        // snmp参数配置
        Properties snmpProperty = systemPreferencesService.getModulePreferences("Snmp");
        Properties pingProperty = systemPreferencesService.getModulePreferences("Ping");
        snmpRetries = snmpProperty.getProperty("Snmp.topo.retries");
        snmpTimeout = snmpProperty.getProperty("Snmp.topo.timeout");
        pingTimeoutConfig = pingProperty.getProperty("Ping.batchtopo.timeout");
        // snmp标签
        List<BatchAutoDiscoverySnmpConfig> snmpConfigs = batchAutoDiscoveryService.getSnmpConfigs();
        // 定时扫描策略
        BatchAutoDiscoveryPeriod batchAutoDiscoveryPeriod = batchAutoDiscoveryService.getPeriod();

        batchTopoData.put("allTypes", allTypes);
        batchTopoData.put("typeIds", typeIds);
        batchTopoData.put("snmpRetries", snmpRetries);
        batchTopoData.put("snmpTimeout", snmpTimeout);
        batchTopoData.put("pingTimeoutConfig", pingTimeoutConfig);
        batchTopoData.put("snmpConfigs", snmpConfigs);
        batchTopoData.put("batchAutoDiscoveryPeriod", batchAutoDiscoveryPeriod);
        return SUCCESS;
    }

    public String loadSnmpTabs() throws IOException {
        JSONObject resultMsg = new JSONObject();
        try {
            // snmp标签
            List<BatchAutoDiscoverySnmpConfig> snmpConfigs = batchAutoDiscoveryService.getSnmpConfigs();
            resultMsg.put("snmpConfigs", snmpConfigs);
        } catch (Exception e) {
            resultMsg.put("error", e.getMessage());
        }
        resultMsg.write(response.getWriter());
        return NONE;
    }

    public String addSnmpTab() throws IOException {
        JSONObject resultMsg = new JSONObject();
        try {
            BatchAutoDiscoverySnmpConfig autoDiscoverySnmpConfig = new BatchAutoDiscoverySnmpConfig();
            autoDiscoverySnmpConfig.setId(id);
            autoDiscoverySnmpConfig.setName(name);
            autoDiscoverySnmpConfig.setReadCommunity(readCommunity);
            autoDiscoverySnmpConfig.setWriteCommunity(writeCommunity);
            autoDiscoverySnmpConfig.setVersion(version);
            autoDiscoverySnmpConfig.setUsername(username);
            autoDiscoverySnmpConfig.setAuthProtocol(authProtocol);
            autoDiscoverySnmpConfig.setAuthPassword(authPassword);
            autoDiscoverySnmpConfig.setPrivProtocol(privProtocol);
            autoDiscoverySnmpConfig.setPrivPassword(privPassword);
            batchAutoDiscoveryService.addSnmpConfig(autoDiscoverySnmpConfig);
        } catch (Exception e) {
            resultMsg.put("error", e.getMessage());
        }
        resultMsg.write(response.getWriter());
        return NONE;
    }

    public String showModifySnmpTab() {
        // batchAutoDiscoveryService.
        BatchAutoDiscoverySnmpConfig discoverySnmpConfig = batchAutoDiscoveryService.getSnmpConfigById(id);
        snmpTabJson = JSONObject.fromObject(discoverySnmpConfig);
        return SUCCESS;
    }

    public String modifySnmpTab() throws IOException {
        JSONObject resultMsg = new JSONObject();
        try {
            BatchAutoDiscoverySnmpConfig autoDiscoverySnmpConfig = new BatchAutoDiscoverySnmpConfig();
            autoDiscoverySnmpConfig.setId(id);
            autoDiscoverySnmpConfig.setName(name);
            autoDiscoverySnmpConfig.setReadCommunity(readCommunity);
            autoDiscoverySnmpConfig.setWriteCommunity(writeCommunity);
            autoDiscoverySnmpConfig.setVersion(version);
            autoDiscoverySnmpConfig.setUsername(username);
            autoDiscoverySnmpConfig.setAuthProtocol(authProtocol);
            autoDiscoverySnmpConfig.setAuthPassword(authPassword);
            autoDiscoverySnmpConfig.setPrivProtocol(privProtocol);
            autoDiscoverySnmpConfig.setPrivPassword(privPassword);
            batchAutoDiscoveryService.modifySnmpConfig(autoDiscoverySnmpConfig);
        } catch (Exception e) {
            resultMsg.put("error", e.getMessage());
        }
        resultMsg.write(response.getWriter());
        return NONE;
    }

    public String deleteSnmpTab() throws IOException {
        JSONObject resultMsg = new JSONObject();
        try {
            batchAutoDiscoveryService.deleteSnmpConfig(id);
        } catch (Exception e) {
            resultMsg.put("error", e.getMessage());
        }
        resultMsg.write(response.getWriter());
        return NONE;
    }

    /**
     * 修改单个扫描任务的执行开关
     * 
     * @return
     * @throws IOException
     */
    public String modifyAutoDiscovery() throws IOException {
        JSONObject resultMsg = new JSONObject();
        BatchAutoDiscoveryIps batchAutoDiscoveryIps = new BatchAutoDiscoveryIps();
        batchAutoDiscoveryIps.setId(id);
        batchAutoDiscoveryIps.setAutoDiscovery(autoDiscovery);
        try {
            batchAutoDiscoveryService.modifyAutoDiscovery(batchAutoDiscoveryIps);
        } catch (Exception e) {
            resultMsg.put("error", e.getMessage());
        }
        resultMsg.write(response.getWriter());
        return NONE;
    }

    /**
     * 修改批量拓扑专用ping参数
     * 
     * @return
     */
    public String saveSnmpConfig() {
        List<SystemPreferences> preferences = new ArrayList<SystemPreferences>();
        SystemPreferences pingPreference = new SystemPreferences();
        // save Ping.timeout
        pingPreference = new SystemPreferences();
        pingPreference.setName("Ping.batchtopo.timeout");
        pingPreference.setValue(pingTimeoutConfig);
        pingPreference.setModule("Ping");
        preferences.add(pingPreference);

        /*SystemPreferences snmpPreference = new SystemPreferences();
        snmpPreference = new SystemPreferences();
        snmpPreference.setName("Snmp.topo.retries");
        snmpPreference.setValue(snmpRetries);
        snmpPreference.setModule("Snmp");
        preferences.add(snmpPreference);
        // sava Snmp.timeout
        snmpPreference = new SystemPreferences();
        snmpPreference.setName("Snmp.topo.timeout");
        snmpPreference.setValue(snmpTimeout);
        snmpPreference.setModule("Snmp");
        preferences.add(snmpPreference);*/
        // save to database
        systemPreferencesService.savePreferences(preferences);
        return NONE;
    }

    /**
     * 加载网段列表
     * 
     * @return
     * @throws IOException
     */
    public String loadNetSegments() throws IOException {
        // 所有IP段查询
    	Map<String,Object>queryMap=new HashMap<String,Object>();
    	queryMap.put("sort", sort);
    	queryMap.put("dir", dir);
    	queryMap.put("queryName", queryName);
        List<BatchAutoDiscoveryIps> ips = batchAutoDiscoveryService.getIps(queryMap);
        List<BatchAutoDiscoveryIps> status = autoDiscoveryService.getAutoDiscoveryIpsDiscoveryStatus();
        for (BatchAutoDiscoveryIps tmp : ips) {
            for (BatchAutoDiscoveryIps tmpStatus : status) {
                if (tmp.getId() == tmpStatus.getId()) {
                    tmp.setAutoDiscoveryStatus(tmpStatus.getAutoDiscoveryStatus());
                    break;
                }
            }
        }
        Date nextFireTime = autoDiscoveryService.getAutoDiscoveryNextFireTime();
        for (BatchAutoDiscoveryIps ip : ips) {
            ip.setFolderName(getI18NString(ip.getFolderName(), "resources"));
            ip.setNextFireTime(new Timestamp(nextFireTime.getTime()));
        }
        JSONObject json = new JSONObject();
        JSONArray netSegmentArr = JSONArray.fromObject(ips);
        json.put("data", netSegmentArr);
        json.write(response.getWriter());
        return NONE;
    }

    public String loadTopoFolder() throws IOException {
        UserContext uc = (UserContext) getSession().get(UserContext.KEY);
        List<TopoFolder> list = topologyService.loadMyTopoFolder(uc.getUserId(), FolderCategory.CLASS_NETWORK);
        JSONArray folders = new JSONArray();
        JSONObject json = new JSONObject();
        for (TopoFolder folder : list) {
            json.put("id", folder.getFolderId());
            json.put("name", getI18NString(folder.getName(), "resources"));
            folders.add(json);
        }
        folders.write(response.getWriter());
        return NONE;
    }

    public String addNetSegment() throws IOException {
        BatchAutoDiscoveryIps netSegment = new BatchAutoDiscoveryIps();
        netSegment.setName(name);
        netSegment.setIpInfo(ipInfo);
        if (folderId != null && !folderId.equals(-1L)) {
            netSegment.setFolderId(folderId);
        }
        netSegment.setAutoDiscovery(autoDiscovery);
        JSONObject resultMsg = new JSONObject();
        try {
            batchAutoDiscoveryService.addNetSegment(netSegment);
        } catch (Exception e) {
            resultMsg.put("error", e.getMessage());
        }
        resultMsg.write(response.getWriter());
        return NONE;
    }

    public String showModifyNetSegment() {
        BatchAutoDiscoveryIps autoDiscoveryIps = batchAutoDiscoveryService.getNetSegmentById(id);
        netSegmentJson = JSONObject.fromObject(autoDiscoveryIps);
        return SUCCESS;
    }

    public String modifyNetSegment() throws IOException {
        BatchAutoDiscoveryIps netSegment = new BatchAutoDiscoveryIps();
        netSegment.setId(id);
        netSegment.setName(name);
        netSegment.setIpInfo(ipInfo);
        if (folderId != null && !folderId.equals(-1L)) {
            netSegment.setFolderId(folderId);
        }
        netSegment.setAutoDiscovery(autoDiscovery);
        JSONObject resultMsg = new JSONObject();
        try {
            batchAutoDiscoveryService.modifyNetSegment(netSegment);
        } catch (Exception e) {
            resultMsg.put("error", e.getMessage());
        }
        resultMsg.write(response.getWriter());
        return NONE;
    }

    public String deleteNetSegment() throws IOException {
        String[] idArr = ids.split(",");
        if (!"".equals(ids) && idArr.length > 0) {
            List<Long> deletedIds = new ArrayList<Long>();
            for (String id : Arrays.asList(idArr)) {
                deletedIds.add(Long.parseLong(id));
            }
            JSONObject resultMsg = new JSONObject();
            try {
                batchAutoDiscoveryService.batchDeleteNetSegment(deletedIds);
            } catch (Exception e) {
                resultMsg.put("error", e.getMessage());
            }
            resultMsg.write(response.getWriter());
        }
        return NONE;
    }

    public String saveScanConfig() throws IOException {
        JSONObject resultMsg = new JSONObject();
        try {
            // 保存设备类型
            String[] idArr = ids.split(",");
            List<Long> entityTypes = new ArrayList<Long>();
            if (!"".equals(ids) && idArr.length > 0) {
                for (String typeId : idArr) {
                    entityTypes.add(Long.parseLong(typeId));
                }
            }
            batchAutoDiscoveryService.modifyTypeIds(entityTypes);
            // 保存策略,如果策略没有修改，则不下发策略修改
            BatchAutoDiscoveryPeriod autoDiscoveryPeriodOld = batchAutoDiscoveryService.getPeriod();
            if (autoDiscoveryPeriodOld.getStrategyType().equals(strategyType)
                    && autoDiscoveryPeriodOld.getPeriodStart().equals(periodStart)
                    && autoDiscoveryPeriodOld.getPeriod().equals(period)) {
                // 策略未作修改
            } else {
                BatchAutoDiscoveryPeriod autoDiscoveryPeriod = new BatchAutoDiscoveryPeriod();
                autoDiscoveryPeriod.setStrategyType(strategyType);
                autoDiscoveryPeriod.setPeriodStart(periodStart);
                autoDiscoveryPeriod.setPeriod(period);
                batchAutoDiscoveryService.modifyPeriod(autoDiscoveryPeriod);
            }
        } catch (Exception e) {
            resultMsg.put("error", e.getMessage());
        }
        resultMsg.write(response.getWriter());
        return NONE;
    }

    public String scanNetSegment() {
        UserContext uc = (UserContext) getSession().get(UserContext.KEY);
        List<Long> idArray = new ArrayList<Long>();
        String[] netIdArr = ids.split(",");
        if (!"".equals(ids) && netIdArr.length > 0) {
            for (int i = 0; i < netIdArr.length; i++) {
                idArray.add(Long.parseLong(netIdArr[i]));
            }
        }
        String jconnectID = request.getParameter("jconnectID");
        batchAutoDiscoveryService.scanNetSegment(idArray, jconnectID, operationId, uc);
        return NONE;
    }

    public static String getI18NString(String key, String module) {
        module = String.format("com.topvision.ems.%s.resources", module);
        try {
            ResourceManager resourceManager = ResourceManager.getResourceManager(module);
            return resourceManager.getNotNullString(key);
        } catch (Exception e) {
            return key;
        }
    }

    public String getSnmpRetries() {
        return snmpRetries;
    }

    public void setSnmpRetries(String snmpRetries) {
        this.snmpRetries = snmpRetries;
    }

    public String getSnmpTimeout() {
        return snmpTimeout;
    }

    public void setSnmpTimeout(String snmpTimeout) {
        this.snmpTimeout = snmpTimeout;
    }

    public String getPingTimeoutConfig() {
        return pingTimeoutConfig;
    }

    public void setPingTimeoutConfig(String pingTimeoutConfig) {
        this.pingTimeoutConfig = pingTimeoutConfig;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIpInfo() {
        return ipInfo;
    }

    public void setIpInfo(String ipInfo) {
        this.ipInfo = ipInfo;
    }

    public Long getFolderId() {
        return folderId;
    }

    public void setFolderId(Long folderId) {
        this.folderId = folderId;
    }

    public Integer getAutoDiscovery() {
        return autoDiscovery;
    }

    public void setAutoDiscovery(Integer autoDiscovery) {
        this.autoDiscovery = autoDiscovery;
    }

    public String getIds() {
        return ids;
    }

    public void setIds(String ids) {
        this.ids = ids;
    }

    public JSONObject getBatchTopoData() {
        return batchTopoData;
    }

    public void setBatchTopoData(JSONObject batchTopoData) {
        this.batchTopoData = batchTopoData;
    }

    public Integer getStrategyType() {
        return strategyType;
    }

    public void setStrategyType(Integer strategyType) {
        this.strategyType = strategyType;
    }

    public Long getPeriodStart() {
        return periodStart;
    }

    public void setPeriodStart(Long periodStart) {
        this.periodStart = periodStart;
    }

    public Long getPeriod() {
        return period;
    }

    public void setPeriod(Long period) {
        this.period = period;
    }

    public String getReadCommunity() {
        return readCommunity;
    }

    public void setReadCommunity(String readCommunity) {
        this.readCommunity = readCommunity;
    }

    public String getWriteCommunity() {
        return writeCommunity;
    }

    public void setWriteCommunity(String writeCommunity) {
        this.writeCommunity = writeCommunity;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAuthProtocol() {
        return authProtocol;
    }

    public void setAuthProtocol(String authProtocol) {
        this.authProtocol = authProtocol;
    }

    public String getAuthPassword() {
        return authPassword;
    }

    public void setAuthPassword(String authPassword) {
        this.authPassword = authPassword;
    }

    public String getPrivProtocol() {
        return privProtocol;
    }

    public void setPrivProtocol(String privProtocol) {
        this.privProtocol = privProtocol;
    }

    public String getPrivPassword() {
        return privPassword;
    }

    public void setPrivPassword(String privPassword) {
        this.privPassword = privPassword;
    }

    public JSONObject getNetSegmentJson() {
        return netSegmentJson;
    }

    public void setNetSegmentJson(JSONObject netSegmentJson) {
        this.netSegmentJson = netSegmentJson;
    }

    public JSONObject getSnmpTabJson() {
        return snmpTabJson;
    }

    public void setSnmpTabJson(JSONObject snmpTabJson) {
        this.snmpTabJson = snmpTabJson;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

	public String getQueryName() {
		return queryName;
	}

	public void setQueryName(String queryName) {
		this.queryName = queryName;
	}

    public String getOperationId() {
        return operationId;
    }

    public void setOperationId(String operationId) {
        this.operationId = operationId;
    }
    
    
}
