/***********************************************************************
 * $Id: BatchAutoDiscoveryServiceImpl.java,v1.0 2014-5-11 上午11:31:03 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.network.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.snmp4j.mp.SnmpConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.network.dao.BatchAutoDiscoveryDao;
import com.topvision.ems.network.domain.BatchAutoDiscoveryEntityType;
import com.topvision.ems.network.domain.BatchAutoDiscoveryIps;
import com.topvision.ems.network.domain.BatchAutoDiscoveryPeriod;
import com.topvision.ems.network.domain.BatchAutoDiscoverySnmpConfig;
import com.topvision.ems.network.service.AutoDiscoveryService;
import com.topvision.ems.network.service.BatchAutoDiscoveryService;
import com.topvision.ems.network.service.BatchDiscoveryService;
import com.topvision.ems.template.service.EntityTypeService;
import com.topvision.framework.common.IpUtils;
import com.topvision.framework.service.BaseService;
import com.topvision.framework.snmp.SnmpParam;
import com.topvision.platform.ResourceManager;
import com.topvision.platform.domain.UserContext;
import com.topvision.platform.service.SystemPreferencesService;

/**
 * @author haojie
 * @created @2014-5-11-上午11:31:03
 * 
 */
@Service("batchAutoDiscoveryService")
public class BatchAutoDiscoveryServiceImpl extends BaseService implements BatchAutoDiscoveryService {
    private static Logger logger = LoggerFactory.getLogger(BatchAutoDiscoveryServiceImpl.class);
    @Autowired
    private BatchAutoDiscoveryDao autoDiscoveryDao;
    @Autowired
    private AutoDiscoveryService autoDiscoveryService;
    @Autowired
    private BatchDiscoveryService batchDiscoveryService;
    @Autowired
    private BatchAutoDiscoveryDao batchAutoDiscoveryDao;
    @Autowired
    private SystemPreferencesService systemPreferencesService;
    @Autowired
    private EntityTypeService entityTypeService;

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.framework.service.BaseService#initialize()
     */
    @Override
    public void initialize() {
        super.initialize();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.framework.service.BaseService#destroy()
     */
    @Override
    @PreDestroy
    public void destroy() {
        super.destroy();
    }

    @Override
    public List<BatchAutoDiscoveryIps> getIps(Map<String,Object>queryMap) {
        return autoDiscoveryDao.queryAllDiscoveryIps(queryMap);
    }

    @Override
    public List<BatchAutoDiscoveryEntityType> getTypeIds() {
        return autoDiscoveryDao.queryAllTypeIds();
    }

    @Override
    public List<BatchAutoDiscoverySnmpConfig> getSnmpConfigs() {
        return autoDiscoveryDao.querySnmpConfigs();
    }

    @Override
    public BatchAutoDiscoveryPeriod getPeriod() {
        return autoDiscoveryDao.queryPeriod();
    }

    @Override
    public void addNetSegment(BatchAutoDiscoveryIps ips) {
        autoDiscoveryDao.addNetSegment(ips);
    }

    @Override
    public void modifyNetSegment(BatchAutoDiscoveryIps ips) {
        autoDiscoveryDao.modifyNetSegment(ips);
    }

    @Override
    public void batchDeleteNetSegment(List<Long> ids) {
        autoDiscoveryDao.batchDeleteNetSegment(ids);
    }

    @Override
    public void addSnmpConfig(BatchAutoDiscoverySnmpConfig snmpConfig) {
        autoDiscoveryDao.addSnmpConfig(snmpConfig);
    }

    @Override
    public void modifySnmpConfig(BatchAutoDiscoverySnmpConfig snmpConfig) {
        autoDiscoveryDao.modifySnmpConfig(snmpConfig);
    }

    @Override
    public void deleteSnmpConfig(Long id) {
        autoDiscoveryDao.deleteSnmpConfig(id);
    }

    @Override
    public void modifyTypeIds(List<Long> typeIds) {
        autoDiscoveryDao.modifyTypeIds(typeIds);
    }

    @Override
    public void modifyPeriod(BatchAutoDiscoveryPeriod period) {
        try {
            // 先更新DB
            autoDiscoveryDao.modifyPeriod(period);
            // 再修改任务
            autoDiscoveryService.reStartAutoDiscoveryJob();
        } catch (Exception e) {
            logger.debug("modifyPeriod fail!", e);
        }
    }

    @Override
    public void scanNetSegment(List<Long> ids, String jconnectID, String operationId, UserContext uc) {
        String dwrId = operationId;
        // ips
        List<BatchAutoDiscoveryIps> ipSegments = autoDiscoveryDao.getIpsByIds(ids);
        List<String> ips = new ArrayList<String>();
        List<String> entityNames = new ArrayList<>();
        List<String> topoNames = new ArrayList<>();
        for (BatchAutoDiscoveryIps batchAutoDiscoveryIps : ipSegments) {
            List<String> ipTemp = IpUtils.parseIp(batchAutoDiscoveryIps.getIpInfo());
            int size = ipTemp.size();
            ips.addAll(ipTemp);
            for (int i = 0; i < size; i++) {
                entityNames.add(null);
                Long folderId = batchAutoDiscoveryIps.getFolderId();
                if (folderId == null || !(folderId == 10L)) {
                    topoNames.add(batchAutoDiscoveryIps.getFolderName());
                } else {
                    topoNames.add("");
                }
            }
        }
        List<BatchAutoDiscoverySnmpConfig> autoDiscoverySnmpConfigs = batchAutoDiscoveryDao
                .getAutoDiscoverySnmpConfigs();
        List<String> autoDiscoverySysObjectId = new ArrayList<>();
        List<String> autoDiscoveryConfig = batchAutoDiscoveryDao.getAutoDiscoverySysObjectId();
        for(String sysObjectId : autoDiscoveryConfig){
            if(entityTypeService.getEntityTypeBySysObjId(sysObjectId) != null){
                autoDiscoverySysObjectId.add(sysObjectId);
            }
        }
        List<SnmpParam> snmpParams = new ArrayList<SnmpParam>();
        for (BatchAutoDiscoverySnmpConfig snmpConfig : autoDiscoverySnmpConfigs) {
            SnmpParam snmpParam = new SnmpParam();
            snmpParam.setVersion(snmpConfig.getVersion());
            if (SnmpConstants.version3 == snmpConfig.getVersion()) {
                snmpParam.setUsername(snmpConfig.getUsername());
                snmpParam.setAuthProtocol(snmpConfig.getAuthProtocol());
                snmpParam.setAuthPassword(snmpConfig.getAuthPassword());
                snmpParam.setPrivProtocol(snmpConfig.getPrivProtocol());
                snmpParam.setPrivPassword(snmpConfig.getPrivPassword());
            } else if (SnmpConstants.version2c == snmpConfig.getVersion()) {
                snmpParam.setCommunity(snmpConfig.getReadCommunity());
                snmpParam.setWriteCommunity(snmpConfig.getWriteCommunity());
            }
            snmpParam.setTimeout(Integer.parseInt(systemPreferencesService.getModulePreferences("Snmp").getProperty(
                    "Snmp.timeout", "10000")));
            snmpParam.setRetry(Byte.parseByte(systemPreferencesService.getModulePreferences("Snmp").getProperty(
                    "Snmp.retries", "0")));
            snmpParam.setPort(Integer.parseInt(systemPreferencesService.getModulePreferences("Snmp").getProperty(
                    "Snmp.port", "161")));
            snmpParams.add(snmpParam);
        }
        batchDiscoveryService.batchDiscovery(ids, dwrId, jconnectID, ips, entityNames, topoNames, snmpParams,
                autoDiscoverySysObjectId, uc);

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

    @Override
    public BatchAutoDiscoveryIps getNetSegmentById(Long id) {
        return autoDiscoveryDao.queryNetSegmentById(id);
    }

    @Override
    public BatchAutoDiscoverySnmpConfig getSnmpConfigById(Long id) {
        return autoDiscoveryDao.querySnmpConfigById(id);
    }

    @Override
    public void modifyAutoDiscovery(BatchAutoDiscoveryIps batchAutoDiscoveryIps) {
        autoDiscoveryDao.updateAutoDiscovery(batchAutoDiscoveryIps);
    }
}
