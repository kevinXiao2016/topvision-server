/***********************************************************************
 * $Id: CmUpgradeServiceImpl.java,v1.0 2016年12月5日 下午2:09:14 $
 * 
 * @author: Rod John
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cm.upgrade.service.impl;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.repeatSecondlyForever;
import static org.quartz.TriggerBuilder.newTrigger;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.quartz.JobDetail;
import org.quartz.SimpleTrigger;
import org.quartz.TriggerBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.cm.upgrade.dao.CmUpgradeDao;
import com.topvision.ems.cm.upgrade.domain.CmUpgradeConfig;
import com.topvision.ems.cm.upgrade.domain.CmcAutoUpgradeProcess;
import com.topvision.ems.cm.upgrade.domain.CmcUpgradeInfo;
import com.topvision.ems.cm.upgrade.facade.CmUpgradeFacade;
import com.topvision.ems.cm.upgrade.facade.domain.TopCcmtsCmAutoUpgradeCfgTable;
import com.topvision.ems.cm.upgrade.facade.domain.TopCcmtsCmSwVersionTable;
import com.topvision.ems.cm.upgrade.facade.domain.TopCcmtsSfFileInfoMgtTable;
import com.topvision.ems.cm.upgrade.facade.domain.TopCcmtsSfFileObject;
import com.topvision.ems.cm.upgrade.facade.domain.TopCcmtsSingleCmUpgradeObject;
import com.topvision.ems.cm.upgrade.job.CollectCmModulAndVersionJob;
import com.topvision.ems.cm.upgrade.service.CmUpgradeService;
import com.topvision.ems.cmc.cm.service.CmService;
import com.topvision.ems.cmc.facade.domain.CmAttribute;
import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.network.service.EntityService;
import com.topvision.framework.exception.engine.SnmpSetException;
import com.topvision.framework.service.BaseService;
import com.topvision.framework.snmp.RowStatus;
import com.topvision.framework.snmp.SnmpParam;
import com.topvision.platform.domain.FtpConnectInfo;
import com.topvision.platform.facade.FacadeFactory;
import com.topvision.platform.message.event.CmcSynchronizedEvent;
import com.topvision.platform.message.event.CmcSynchronizedListener;
import com.topvision.platform.message.service.MessageService;
import com.topvision.platform.service.FtpConnectService;
import com.topvision.platform.service.SchedulerService;
import com.topvision.platform.service.SystemPreferencesService;
import com.topvision.platform.service.impl.FtpServerServiceImpl;

/**
 * @author Rod John
 * @created @2016年12月5日-下午2:09:14
 *
 */
@Service("cmUpgradeService")
public class CmUpgradeServiceImpl extends BaseService implements CmUpgradeService, CmcSynchronizedListener {
    @Autowired
    private EntityService entityService;
    @Autowired
    private FacadeFactory facadeFactory;
    @Autowired
    private CmUpgradeDao cmUpgradeDao;
    @Autowired
    private CmService cmService;
    @Autowired
    private SystemPreferencesService systemPreferencesService;
    @Autowired
    private SchedulerService schedulerService;
    @Autowired
    private FtpConnectService ftpConnectService;
    @Autowired
    private MessageService messageService;
    // 如果有同步需求,可以修改为ThreadLocal
    private Map<Long, CmcAutoUpgradeProcess> process = new HashMap<>();
    private ExecutorService executorService = Executors.newFixedThreadPool(10);

    /* (non-Javadoc)
     * @see com.topvision.framework.service.BaseService#start()
     */
    @Override
    public void start() {
        try {
            JobDetail job = newJob(CollectCmModulAndVersionJob.class).withIdentity("collectCmModulAndVersionJob")
                    .build();
            job.getJobDataMap().put(EntityService.class.getSimpleName(), entityService);
            job.getJobDataMap().put(CmUpgradeDao.class.getSimpleName(), cmUpgradeDao);
            job.getJobDataMap().put(CmUpgradeService.class.getSimpleName(), this);
            job.getJobDataMap().put(FacadeFactory.class.getSimpleName(), facadeFactory);

            TriggerBuilder<SimpleTrigger> builder = newTrigger()
                    .withIdentity(job.getKey().getName(), job.getKey().getGroup())
                    .withSchedule(repeatSecondlyForever(Integer.parseInt(systemPreferencesService
                            .getModulePreferences("cmUpgrade").getProperty("cmUpgrade.interval", "86400"))));
            builder.startAt(new Date(System.currentTimeMillis() + 60));
            schedulerService.scheduleJob(job, builder.build());
        } catch (Exception e) {
            logger.error("", e);
        }
    }

    /* (non-Javadoc)
     * @see com.topvision.ems.cm.upgrade.service.CmUpgradeService#loadCcmtsFile(java.lang.Long)
     */
    @Override
    public List<TopCcmtsSfFileInfoMgtTable> loadCcmtsFile(Long entityId) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        List<TopCcmtsSfFileInfoMgtTable> file = getCmUpgradeFacade(snmpParam.getIpAddress())
                .getTopCcmtsSfFileInfoMgtTable(snmpParam);
        List<TopCcmtsSfFileInfoMgtTable> cmFile = new ArrayList<>();
        for (TopCcmtsSfFileInfoMgtTable tmp : file) {
            if (tmp.getTopCcmtsSfFileType().equals(TopCcmtsSfFileInfoMgtTable.CM_IMAGE)) {
                cmFile.add(tmp);
            }
        }
        return cmFile;
    }

    /* (non-Javadoc)
     * @see com.topvision.ems.cm.upgrade.service.CmUpgradeService#loadCcmtsFileSize(java.lang.Long)
     */
    @Override
    public Integer loadCcmtsFileSize(Long entityId) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        return getCmUpgradeFacade(snmpParam.getIpAddress()).getCcmtsSfFileTotalSize(snmpParam);
    }

    /* (non-Javadoc)
     * @see com.topvision.ems.cm.upgrade.service.CmUpgradeService#deleteCcmtsFile(java.lang.Long, java.lang.String)
     */
    @Override
    public void deleteCcmtsFile(Long entityId, String fileName) {
        TopCcmtsSfFileInfoMgtTable file = new TopCcmtsSfFileInfoMgtTable();
        file.setEntityId(entityId);
        file.setTopCcmtsSfFileType(TopCcmtsSfFileInfoMgtTable.CM_IMAGE);
        file.setTopCcmtsSfFileName(fileName);
        file.setTopCcmtsSfFileMgtAction(TopCcmtsSfFileInfoMgtTable.ERASE);
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        getCmUpgradeFacade(snmpParam.getIpAddress()).deleteCcmtsFile(snmpParam, file);
    }

    /* (non-Javadoc)
     * @see com.topvision.ems.cm.upgrade.service.CmUpgradeService#uploadCcmtsFile(java.lang.Long, java.io.File)
     */
    @Override
    public void uploadCcmtsFile(Long entityId, File file) {
        updateProcess(entityId, CmcAutoUpgradeProcess.AUTO_UPGRADE_UPLOADING_FILE);
        TopCcmtsSfFileObject action = new TopCcmtsSfFileObject();
        action.setEntityId(entityId);
        FtpConnectInfo connectInfo = ftpConnectService.getFtpConnectAttr();
        String fileName = file.getName();
        action.setTopCcmtsSfFileTransferType(TopCcmtsSfFileInfoMgtTable.CM_IMAGE);
        action.setTopCcmtsSfFileDestPath(fileName);
        action.setTopCcmtsSfFileFtpUserName(connectInfo.getUserName());
        action.setTopCcmtsSfFileFtpUserPassword(connectInfo.getPwd());
        action.setTopCcmtsSfFileServerIpAddr(connectInfo.getIp());
        action.setTopCcmtsSfFileSrcPath(fileName);
        // Syntax: [UNIVERSAL 2] INTEGER { noOperation(0), load(1), upload(2) }
        action.setTopCcmtsSfFileTransferAction(1);
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        getCmUpgradeFacade(snmpParam.getIpAddress()).uploadCmFile(snmpParam, action);
    }

    /* (non-Javadoc)
     * @see com.topvision.ems.cm.upgrade.service.CmUpgradeService#loadUploadProcess(java.lang.Long)
     */
    @Override
    public String loadUploadProcess(Long entityId) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        return getCmUpgradeFacade(snmpParam.getIpAddress()).loadUploadProcess(snmpParam);
    }

    /* (non-Javadoc)
     * @see com.topvision.ems.cm.upgrade.service.CmUpgradeService#upgradeSingleCm(java.lang.Long, java.lang.String, java.lang.Long, java.lang.String)
     */
    @Override
    public String upgradeSingleCm(Long entityId, String cmMac, Long statusIndex, String fileName) {
        TopCcmtsSingleCmUpgradeObject upgrade = new TopCcmtsSingleCmUpgradeObject();
        upgrade.setTopCcmtsSingleCmUpgradeCmMac(cmMac);
        upgrade.setTopCcmtsSingleCmUpgradeImageFileName(fileName);
        upgrade.setTopCcmtsSingleCmUpgradeAction(TopCcmtsSingleCmUpgradeObject.UPGRADE);
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        try {
            getCmUpgradeFacade(snmpParam.getIpAddress()).upgradeSingleCm(snmpParam, upgrade);
            return "success";
        } catch (SnmpSetException e) {
            try {
                Integer upgradeResult = getCmUpgradeFacade(snmpParam.getIpAddress()).getCmUpgradeStatus(snmpParam,
                        statusIndex);
                if (CmUpgradeService.CM_UPGRADING.equals(upgradeResult)) {
                    return "upgrading";
                }
            } catch (Exception e1) {
                logger.error("", e1);
                return "failure";
            }
            logger.error("", e);
            return "failure";
        } catch (Exception e) {
            logger.error("", e);
            return "failure";
        }
    }

    /* (non-Javadoc)
     * @see com.topvision.ems.cm.upgrade.service.CmUpgradeService#getCmVersionInfo()
     */
    @Override
    public Map<String, Map<String, Integer>> getCmVersionInfo(Integer cmVersionInfoType, Long entityId) {
        List<Long> cmcList = new ArrayList<>();
        Map<String, Map<String, Integer>> result = new HashMap<>();
        Map<Long, List<TopCcmtsCmSwVersionTable>> data = new HashMap<>();
        if (entityId == null) {
            List<CmcUpgradeInfo> entityList = loadCmcUpgradeInfo(new HashMap<>());
            for (CmcUpgradeInfo tmp : entityList) {
                cmcList.add(tmp.getEntityId());
            }
        } else {
            cmcList.add(entityId);
        }
        // Loop Get Version Table
        for (Long cmcId : cmcList) {
            SnmpParam snmpParam = entityService.getSnmpParamByEntity(cmcId);
            try {
                List<TopCcmtsCmSwVersionTable> cmVersions = null;
                if (cmVersionInfoType.equals(1)) {
                    cmVersions = getCmUpgradeFacade(snmpParam.getIpAddress()).getTopCcmtsCmSwVersionTable(snmpParam);
                    data.put(cmcId, cmVersions);
                } else if (cmVersionInfoType.equals(2)) {
                    cmVersions = cmUpgradeDao.getCmSwVersionInfo(cmcId);
                }
                for (TopCcmtsCmSwVersionTable version : cmVersions) {
                    String model = version.getTopCcmtsCmModelNum();
                    result.putIfAbsent(model, new HashMap<String, Integer>());
                    Map<String, Integer> versionCount = result.get(model);
                    String ver = version.getTopCcmtsCmSwVersion();
                    if (versionCount.containsKey(ver)) {
                        versionCount.put(ver, versionCount.get(ver) + 1);
                    } else {
                        versionCount.put(ver, 1);
                    }
                }
            } catch (Exception e) {
                logger.info("get " + snmpParam.getIpAddress() + " cmversion error");
            }
        }
        cmUpgradeDao.syncCmModulSoftversion(data);
        return result;
    }

    private CmUpgradeFacade getCmUpgradeFacade(String ip) {
        return facadeFactory.getFacade(ip, CmUpgradeFacade.class);
    }

    /* (non-Javadoc)
     * @see com.topvision.ems.cm.upgrade.service.CmUpgradeService#loadCmcUpgradeInfo(java.util.Map)
     */
    @Override
    public List<CmcUpgradeInfo> loadCmcUpgradeInfo(Map<String, Object> param) {
        return cmUpgradeDao.selectCmcUpgradeEntityInfo(param);
    }

    /* (non-Javadoc)
     * @see com.topvision.ems.cm.upgrade.service.CmUpgradeService#loadCmModuleNum()
     */
    @Override
    public List<String> loadCmModuleNum() {
        return cmUpgradeDao.selectModulList();
    }

    /* (non-Javadoc)
     * @see com.topvision.ems.cm.upgrade.service.CmUpgradeService#getSingleCmSwVersion(java.lang.Long)
     */
    @Override
    public TopCcmtsCmSwVersionTable getSingleCmSwVersion(Long cmId) {
        CmAttribute cmAttribute = cmService.getCmAttributeByCmId(cmId);

        /*SnmpParam snmpParam = entityService.getSnmpParamByEntity(cmAttribute.getCmcId());
        TopCcmtsCmSwVersionTable cm = new TopCcmtsCmSwVersionTable();
        cm.setStatusIndex(cmAttribute.getStatusIndex());
        return getCmUpgradeFacade(snmpParam.getIpAddress()).getSingleCmSwVersion(snmpParam, cm);*/

        return cmUpgradeDao.selectCmModulSoftversion(cmAttribute.getCmcId(), cmAttribute.getStatusIndex());
    }

    /* (non-Javadoc)
     * @see com.topvision.ems.cm.upgrade.service.CmUpgradeService#addAutoUpgradeConfig(com.topvision.ems.cm.upgrade.domain.CmUpgradeConfig)
     */
    @Override
    public void addAutoUpgradeConfig(CmUpgradeConfig config) {
        cmUpgradeDao.insertCmUpgradeConfig(config);
    }

    /* (non-Javadoc)
     * @see com.topvision.ems.cm.upgrade.service.CmUpgradeService#modifyAutoUpgradeConfig(com.topvision.ems.cm.upgrade.domain.CmUpgradeConfig)
     */
    @Override
    public void modifyAutoUpgradeConfig(CmUpgradeConfig config) {
        cmUpgradeDao.modifyCmUpgradeConfig(config);
    }

    /* (non-Javadoc)
     * @see com.topvision.ems.cm.upgrade.service.CmUpgradeService#deleteAutoUpgradeConfig(java.lang.Integer)
     */
    @Override
    public void deleteAutoUpgradeConfig(Integer configId) {
        cmUpgradeDao.deleteCmUpgradeConfig(configId);
    }

    /* (non-Javadoc)
     * @see com.topvision.ems.cm.upgrade.service.CmUpgradeService#loadCmUpgradeConfig()
     */
    @Override
    public List<CmUpgradeConfig> loadCmUpgradeConfig() {
        return cmUpgradeDao.loadCmUpgradeConfig();
    }

    /* (non-Javadoc)
     * @see com.topvision.ems.cm.upgrade.service.CmUpgradeService#applyAutoUpgradeConfig(java.util.List)
     */
    @Override
    public void applyAutoUpgradeConfig(List<Long> entityIds) {
        List<CmUpgradeConfig> autoConfig = cmUpgradeDao.loadCmUpgradeConfig();
        Integer configFileSize = getConfigFileSize(autoConfig);
        // first init process
        this.process.clear();
        CompletionService<String> completionService = new ExecutorCompletionService<String>(executorService);
        for (Long entityId : entityIds) {
            Entity entity = entityService.getEntity(entityId);
            process.put(entityId, new CmcAutoUpgradeProcess(entityId, entity.getName(), entity.getMac()));
            completionService.submit(new Callable<String>() {

                @Override
                public String call() throws Exception {
                    // check version support
                    int checkResult = checkVersionSupport(entityId);
                    if (checkResult == 0) {
                        updateProcess(entityId, CmcAutoUpgradeProcess.AUTO_UPGRADE_VERSION_FAILURE);
                        return "complete";
                    } else if (checkResult == -1) {
                        // Write Community error
                        updateProcess(entityId, CmcAutoUpgradeProcess.AUTO_UPGRADE_FAILURE);
                        return "complete";
                    }

                    // clear auto config
                    if (!deleteAutoUpgradeConfig(entityId)) {
                        updateProcess(entityId, CmcAutoUpgradeProcess.AUTO_UPGRADE_CLEANING_CONFIG_FAILURE);
                        return "complete";
                    }
                    // clear file
                    if (!deleteCmSoftversionFile(entityId)) {
                        updateProcess(entityId, CmcAutoUpgradeProcess.AUTO_UPGRADE_CLEANING_FILE_FAILURE);
                        return "complete";
                    }
                    // check file
                    if (!checkCmFileSize(configFileSize, entityId)) {
                        updateProcess(entityId, CmcAutoUpgradeProcess.AUTO_UPGRADE_FILEERROR);
                        return "complete";
                    }
                    // Apply To Device 1.Upload File use ftp  2.Set Auto Config 
                    try {
                        for (CmUpgradeConfig config : autoConfig) {
                            File file = new File(
                                    FtpServerServiceImpl.FTPROOTPATH + File.separator + config.getVersionFileName());
                            uploadCcmtsFile(entityId, file);
                            applyAutoConfig(entityId, config);
                            waitFileUpload(entityId);
                        }
                        updateProcess(entityId, CmcAutoUpgradeProcess.AUTO_UPGRADE_SUCCESS);
                    } catch (Exception e) {
                        logger.error("", e);
                        updateProcess(entityId, CmcAutoUpgradeProcess.AUTO_UPGRADE_FAILURE);
                    }
                    return "complete";
                }
            });
        }
        for (int i = 0; i < entityIds.size(); i++) {
            try {
                completionService.take().get(10, TimeUnit.SECONDS);
            } catch (Exception e) {
            }
        }
        // Finish
        completeAutoUpgradeProcess();
    }

    /**
     * 清空CM自动升级配置
     * 
     * @param entityIds
     */
    private boolean deleteAutoUpgradeConfig(Long entityId) {
        try {
            updateProcess(entityId, CmcAutoUpgradeProcess.AUTO_UPGRADE_CLEANING_CONFIG);
            SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
            getCmUpgradeFacade(snmpParam.getIpAddress()).clearAutoUpgradeConfig(snmpParam);
            return true;
        } catch (Exception e) {
            logger.error("", e);
            return false;
        }
    }

    /**
     * 清空CM升级文件
     * 
     * @param entityIds
     */
    private boolean deleteCmSoftversionFile(Long entityId) {
        try {
            updateProcess(entityId, CmcAutoUpgradeProcess.AUTO_UPGRADE_CLEANING_FILE);
            SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
            getCmUpgradeFacade(snmpParam.getIpAddress()).clearCmSoftversionFile(snmpParam);
            return true;
        } catch (Exception e) {
            logger.error("", e);
            return false;
        }
    }

    /**
     * 检查设备CM升级文件可用空间
     * 
     * @param entityId
     * @return
     */
    private boolean checkCmFileSize(Integer configSize, Long entityId) {
        try {
            SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
            Integer currentSize = getCmUpgradeFacade(snmpParam.getIpAddress()).getCcmtsSfFileTotalSize(snmpParam);
            if (configSize + currentSize > CmUpgradeService.FILE_MAX) {
                return false;
            }
            return true;
        } catch (Exception e) {
            logger.error("", e);
            return false;
        }
    }

    /**
     * 检查是否支持CM升级
     * 
     * @param entityId
     * @return
     */
    private Integer checkVersionSupport(Long entityId) {
        try {
            SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
            getCmUpgradeFacade(snmpParam.getIpAddress()).setCcmtsCmAutoUpgradeEnable(snmpParam);
            return 1;
        } catch (SnmpSetException e) {
            return 0;
        } catch (Exception e) {
            logger.error("", e);
            return -1;
        }
    }

    /**
     * Update Process
     * 
     * @param entityId
     * @param step
     */
    private void updateProcess(Long entityId, Integer step) {
        CmcAutoUpgradeProcess upgradeProcess = process.get(entityId);
        if (upgradeProcess != null) {
            upgradeProcess.setStep(step);
        } else {
            Entity entity = entityService.getEntity(entityId);
            process.put(entityId, new CmcAutoUpgradeProcess(entityId, entity.getName(), entity.getMac(), step));
        }
    }

    /**
     * Complete Process
     * 
     */
    private void completeAutoUpgradeProcess() {
        for (Entry<Long, CmcAutoUpgradeProcess> entry : process.entrySet()) {
            CmcAutoUpgradeProcess cmcAutoUpgradeProcess = entry.getValue();
            cmcAutoUpgradeProcess.setResult(true);
        }
    }

    /**
     * 
     * 
     * @param cmUpgradeConfigs
     * @return
     */
    private Integer getConfigFileSize(List<CmUpgradeConfig> cmUpgradeConfigs) {
        Integer result = 0;
        for (CmUpgradeConfig config : cmUpgradeConfigs) {
            result += config.getFileSize();
        }
        return result;
    }

    /**
     * 应用自动升级配置到设备
     * 
     * @param entityId
     * @param config
     */
    private void applyAutoConfig(Long entityId, CmUpgradeConfig config) {
        updateProcess(entityId, CmcAutoUpgradeProcess.AUTO_UPGRADE_APPLYING);
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        TopCcmtsCmAutoUpgradeCfgTable device = new TopCcmtsCmAutoUpgradeCfgTable();
        device.setTopCcmtsCmAutoUpgradeCmModelNum(config.getModulNum());
        device.setTopCcmtsCmAutoUpgradeImageFileName(config.getVersionFileName());
        device.setTopCcmtsCmAutoUpgradeSwVersion(config.getSoftVersion());
        device.setTopCcmtsCmAutoUpgradeRowStatus(RowStatus.CREATE_AND_GO);
        getCmUpgradeFacade(snmpParam.getIpAddress()).createAutoUpgradeConfig(snmpParam, device);
    }

    /* (non-Javadoc)
     * @see com.topvision.ems.cm.upgrade.service.CmUpgradeService#loadAutoUpgradeProcess()
     */
    @Override
    public List<CmcAutoUpgradeProcess> loadAutoUpgradeProcess() {
        List<CmcAutoUpgradeProcess> result = new ArrayList<>();
        if (process != null && !process.isEmpty()) {
            for (Entry<Long, CmcAutoUpgradeProcess> entry : process.entrySet()) {
                result.add(entry.getValue());
            }
        }
        return result;
    }

    /**
     * 等待文件上次完成
     * 
     * @param entityId
     */
    private void waitFileUpload(Long entityId) {
        int count = 1;
        while (count <= 30) {
            try {
                Thread.sleep(1000);
                Integer result = Integer.parseInt(loadUploadProcess(entityId));
                if (result.equals(100) || result.equals(200) || result.equals(201)) {
                    return;
                }
                count++;
            } catch (Exception e) {
                logger.error("writeFileUpload error:", e);
                return;
            }
        }
    }

    @PreDestroy
    public void destroy() {
        super.destroy();
        messageService.removeListener(CmcSynchronizedListener.class, this);
    }

    @PostConstruct
    public void initialize() {
        super.initialize();
        messageService.addListener(CmcSynchronizedListener.class, this);
    }

    @Override
    public void insertEntityStates(CmcSynchronizedEvent event) {
        Map<Long, List<TopCcmtsCmSwVersionTable>> result = new HashMap<Long, List<TopCcmtsCmSwVersionTable>>();
        try {
            SnmpParam snmpParam = entityService.getSnmpParamByEntity(event.getEntityId());
            List<TopCcmtsCmSwVersionTable> versions = getCmUpgradeFacade(snmpParam.getIpAddress())
                    .getTopCcmtsCmSwVersionTable(snmpParam);
            result.put(event.getEntityId(), versions);
        } catch (Exception e) {
            logger.info("getTopCcmtsCmSwVersionTable error:" + event.getIpAddress());
        }
        if (result != null && result.size() > 0) {
            cmUpgradeDao.syncCmModulSoftversion(result);
        }
    }
}
