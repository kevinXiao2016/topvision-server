/***********************************************************************
 * $Id: CmcReplaceServiceImpl.java,v1.0 2016-4-18 下午1:56:44 $
 * 
 * @author: Rod John
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.ccmts.service.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.topvision.ems.cmc.ccmts.dao.CmcReplaceDao;
import com.topvision.ems.cmc.ccmts.domain.CmcReplaceInfo;
import com.topvision.ems.cmc.ccmts.facade.CmcFacade;
import com.topvision.ems.cmc.ccmts.facade.domain.CmcAttribute;
import com.topvision.ems.cmc.ccmts.facade.domain.CmcReplaceEntry;
import com.topvision.ems.cmc.ccmts.service.CmcReplaceService;
import com.topvision.ems.cmc.ccmts.service.CmcService;
import com.topvision.ems.cmc.exception.CmcForceReplaceException;
import com.topvision.ems.cmc.service.impl.CmcBaseCommonService;
import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.network.domain.EntitySnap;
import com.topvision.ems.network.domain.TelnetLogin;
import com.topvision.ems.network.service.DiscoveryService;
import com.topvision.ems.network.service.EntityService;
import com.topvision.ems.network.service.OnlineService;
import com.topvision.ems.network.service.TelnetLoginService;
import com.topvision.ems.network.service.impl.AutoRefreshServiceImpl;
import com.topvision.ems.upgrade.telnet.TelnetUtil;
import com.topvision.ems.upgrade.telnet.TelnetUtilFactory;
import com.topvision.framework.exception.engine.SnmpSetException;
import com.topvision.framework.snmp.SnmpParam;
import com.topvision.platform.SystemConstants;
import com.topvision.platform.domain.TftpClientInfo;
import com.topvision.platform.facade.FacadeFactory;
import com.topvision.platform.service.FtpConnectService;
import com.topvision.platform.service.TftpClientService;

/**
 * @author Rod John
 * @created @2016-4-18-下午1:56:44
 *
 */
@Service("cmcReplaceService")
public class CmcReplaceServiceImpl extends CmcBaseCommonService implements CmcReplaceService {
    private static Logger logger = LoggerFactory.getLogger(AutoRefreshServiceImpl.class);
    private static final Integer REPLACE = 1;
    @Autowired
    private CmcReplaceDao cmcReplaceDao;
    @Autowired
    private CmcService cmcService;
    @Autowired
    private EntityService entityService;
    @Autowired
    private FacadeFactory facadeFactory;
    @SuppressWarnings("rawtypes")
    @Autowired
    private DiscoveryService discoveryService;
    @Autowired
    private FtpConnectService ftpConnectService;
    @Autowired
    private TftpClientService tftpClientService;
    @Autowired
    private TelnetUtilFactory telnetUtilFactory;
    @Autowired
    private TelnetLoginService telnetLoginService;
    @Autowired
    private OnlineService onlineService;
    @Value("${fileautosave.cc.config:config}")
    private String CC_CONFIG;

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
    public void destroy() {
        super.destroy();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.framework.service.BaseService#start()
     */
    @Override
    public void start() {
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.framework.service.BaseService#stop()
     */
    @Override
    public void stop() {
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.cmc.ccmts.service.CmcReplaceService#getCmcMacListByEntityId(java.lang.Long)
     */
    @Override
    public Map<String, Map<String, Object>> getOnuMacListByEntityId(Long entityId) {
        return cmcReplaceDao.getOnuMacListByEntityId(entityId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.cmc.ccmts.service.CmcReplaceService#replaceCmc(java.lang.Long,
     * java.lang.String, java.lang.Integer)
     */
    @Override
    public void replaceCmc(Long entityId, Long cmcId, Long cmcIndex, String cmcMac, Integer forceReplace) {
        CmcReplaceEntry cmcReplaceEntry = new CmcReplaceEntry();
        cmcReplaceEntry.setIfIndex(cmcIndex);
        cmcReplaceEntry.setTopNewCcmtsMacAddr(cmcMac);
        boolean deleteCmcResult = true;
        try {
            if (forceReplace == REPLACE) {
                CmcAttribute cmcAttribute = cmcReplaceDao.getCmcAttributeByMacAddress(entityId, cmcMac);
                if (cmcAttribute != null) {
                    // NO MAC-BIND Action
                    cmcService.cmcNoMacBind(cmcAttribute.getCmcId());
                    Thread.sleep(3000);
                }
            }
        } catch (Exception e) {
            deleteCmcResult = false;
            logger.error("Force Replace Cmc error:", e);
        }
        try {
            SnmpParam snmpParam = getSnmpParamByEntityId(entityId);
            CmcFacade cmcFacade = facadeFactory.getFacade(snmpParam.getIpAddress(), CmcFacade.class);
            cmcFacade.replaceCmcEntry(snmpParam, cmcReplaceEntry);
            syncCmcInfoAfterReplace(cmcId, entityId, cmcMac);
        } catch (SnmpSetException e) {
            if (!deleteCmcResult) {
                CmcForceReplaceException cmcForceReplaceException = new CmcForceReplaceException(e);
                throw cmcForceReplaceException;
            } else {
                throw e;
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.cmc.ccmts.service.CmcReplaceService#replaceCmc(java.lang.Long,
     * java.lang.Long, java.lang.String)
     */
    @Override
    public String replaceCmc(final Long cmcId, Long replace_cmcId, String configFilePath) {
        Entity entity = entityService.getEntity(cmcId);
        Entity replaceEntity = entityService.getEntity(replace_cmcId);
        if (cmcId.equals(replace_cmcId)) {
            if (configFilePath != null && configFilePath.length() > 0) {
                try {
                    // Apply Config BackUp File
                    Boolean result = applyConfigToDevice(entity, replaceEntity, configFilePath);
                    if (result) {
                        try {
                            // Reset Entity
                            cmcService.resetCmcWithoutAgent(cmcId);
                            updateCmcDownAfterReplace(cmcId);
                        } catch (Exception e) {
                            logger.error("Entity Replace Reset Entity Error:", e);
                            return "entityResetError";
                        }
                    } else {
                        logger.error("Entity Replace Sync Config File Error: applyConfigToDevice");
                        return "configSyncError";
                    }
                } catch (Exception e) {
                    logger.error("Entity Replace Sync Config File Error:", e);
                    return "configSyncError";
                }
            }
        } else {
            if (configFilePath != null && configFilePath.length() > 0) {
                try {
                    // Apply Config BackUp File
                    Boolean result = applyConfigToDevice(entity, replaceEntity, configFilePath);
                    if (result) {
                        try {
                            // Reset Entity
                            cmcService.resetCmcWithoutAgent(replace_cmcId);
                            updateCmcDownAfterReplace(cmcId);
                        } catch (Exception e) {
                            logger.error("Entity Replace Reset Entity Error:", e);
                            return "entityResetError";
                        }
                    } else {
                        logger.error("Entity Replace Sync Config File Error: applyConfigToDevice");
                        return "configSyncError";
                    }
                    // Delete Replace Entity
                    List<Long> entityIds = new ArrayList<>();
                    entityIds.add(replace_cmcId);
                    entityService.removeEntity(entityIds);
                    // Modify Mac
                    cmcReplaceDao.modifyEntityMac(entity.getEntityId(), replaceEntity.getMac());
                } catch (Exception e) {
                    logger.error("Entity Replace Sync Config File Error:", e);
                    return "configSyncError";
                }
            } else {
                // Delete Replace Entity
                List<Long> entityIds = new ArrayList<>();
                entityIds.add(replace_cmcId);
                entityService.removeEntity(entityIds);
                // Modify Ip and Mac
                cmcReplaceDao.modifyEntityIpAndMac(entity.getEntityId(), replaceEntity.getIp(), replaceEntity.getMac());
            }
        }
        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(60000);
                    } catch (InterruptedException e) {
                    }
                    Entity entity = entityService.getEntity(cmcId);
                    long delay = onlineService.ping(entity.getIp());
                    if (delay < 0) {
                        continue;
                    }
                    discoveryService.refresh(entity.getEntityId());
                    return;
                }
            }
        });
        thread.start();
        return "success";
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.cmc.ccmts.service.CmcReplaceService#loadConfigFile(java.lang.Long)
     */
    @Override
    public Map<String, Object> loadConfigFile(Long cmcId) {
        StringBuilder fileName = new StringBuilder(SystemConstants.ROOT_REAL_PATH);
        fileName.append("META-INF/startConfig/").append(cmcId);
        File file = new File(fileName.toString());
        Map<String, Object> topTree = new HashMap<String, Object>();
        List<HashMap<String, Object>> secondTree = new ArrayList<>();
        topTree.put("text", "ConfigFile");
        topTree.put("iconCls", "dir");
        topTree.put("expanded", true);
        topTree.put("children", secondTree);
        if (!file.exists()) {
            return topTree;
        }
        for (File tmp : file.listFiles()) {
            if (tmp.isDirectory()) {
                HashMap<String, Object> secondTreeData = new HashMap<>();
                secondTreeData.put("text", tmp.getName());
                secondTreeData.put("expanded", true);
                secondTreeData.put("iconCls", "dir");
                secondTreeData.put("checked", false);
                secondTreeData.put("leaf", "true");
                secondTree.add(secondTreeData);
                /*
                 * List<HashMap<String, Object>> thirdTree = new ArrayList<>();
                 * secondTreeData.put("children", thirdTree); for (File tar : tmp.listFiles()) {
                 * HashMap<String, Object> thirdTreeData = new HashMap<>();
                 * thirdTreeData.put("text", tar.getName()); thirdTreeData.put("iconCls", "tar");
                 * thirdTreeData.put("checked", false); thirdTreeData.put("leaf", true);
                 * thirdTree.add(thirdTreeData); }
                 */
            }
        }
        return topTree;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.cmc.ccmts.service.CmcReplaceService#loadCmcReplaceList(java.lang.Long)
     */
    @Override
    public List<CmcReplaceInfo> loadCmcReplaceList(Long cmcId) {
        return cmcReplaceDao.loadCmcReplaceList(cmcId);
    }

    /**
     * syncCmcInfoAfterReplace
     * 
     * @param cmcId
     * @param entityId
     * @param macAddress
     */
    private void syncCmcInfoAfterReplace(Long cmcId, Long entityId, String macAddress) {
        Long onuIndex = cmcDao.getOnuIndexByCmcId(cmcId);
        cmcReplaceDao.syncCmcInfoAfterReplace(cmcId, onuIndex, entityId, macAddress);
    }

    /**
     * updateCmcDownAfterReplace
     * 
     * @param entityId
     */
    private void updateCmcDownAfterReplace(Long entityId) {
        EntitySnap entitySnap = entityDao.getEntitySnapById(entityId);
        entitySnap.setState(false);
        entitySnap.setDelay(-1);
        entityDao.updateEntitySnap(entitySnap);
    }

    /**
     * 
     * @param entity
     * @param configFilePath
     * @return
     */
    private boolean applyConfigToDevice(Entity entity, Entity replaceEntity, String configFilePath) {
        StringBuilder fileBuilder = new StringBuilder();
        configFilePath = configFilePath.split("/")[1];
        fileBuilder.append(SystemConstants.ROOT_REAL_PATH);
        fileBuilder.append("META-INF/startConfig/");
        fileBuilder.append(entity.getEntityId()).append("/");
        fileBuilder.append(configFilePath);
        File configFileDir = new File(fileBuilder.toString());
        List<String> fileList = new ArrayList<>();
        for (File file : configFileDir.listFiles()) {
            fileList.add(file.getName());
            if (!tftpClientService.uploadFile(file, file.getName())) {
                return false;
            }
        }
        if (!fileList.contains(CC_CONFIG)) {
            return false;
        }
        fileList.remove(CC_CONFIG);
        fileList.add(0, CC_CONFIG);
        TelnetUtil telnetUtil = null;
        try {
            TelnetLogin telnetLogin = telnetLoginService.getEntityTelnetLogin(replaceEntity.getIp());
            TftpClientInfo tftpClientInfo = tftpClientService.getTftpClientInfo();
            telnetUtil = telnetUtilFactory.getTelnetUtil(replaceEntity.getTypeId(), replaceEntity.getIp(), telnetLogin);
            for (String fileName : fileList) {
                telnetUtil.downLoadConfig(tftpClientInfo, fileName);
            }
        } catch (Exception e) {
            logger.error("applyConfigToDevice error:", e);
            return false;
        } finally {
            if (telnetUtil != null) {
                telnetUtil.disconnect();
                telnetUtilFactory.releaseTelnetUtil(telnetUtil);
            }
        }
        return true;
    }

}
