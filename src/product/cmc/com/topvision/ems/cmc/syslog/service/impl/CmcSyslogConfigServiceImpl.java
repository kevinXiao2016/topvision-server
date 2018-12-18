/***********************************************************************
 * $Id: CmcSyslogConfigServiceImpl.java,v1.0 2013-4-26 下午8:37:02 $
 * 
 * @author: fanzidong
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.syslog.service.impl;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.cmc.service.impl.CmcBaseCommonService;
import com.topvision.ems.cmc.syslog.dao.CmcSyslogConfigDao;
import com.topvision.ems.cmc.syslog.domain.CmcSyslogRecordType;
import com.topvision.ems.cmc.syslog.domain.CmcSyslogRecordTypeII;
import com.topvision.ems.cmc.syslog.facade.domain.CmcSyslogConfig;
import com.topvision.ems.cmc.syslog.service.CmcSyslogConfigService;
import com.topvision.ems.cmc.util.CmcConstants;
import com.topvision.ems.devicesupport.version.service.DeviceVersionService;
import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.network.service.EntityService;
import com.topvision.ems.template.service.EntityTypeService;
import com.topvision.platform.message.event.CmcSynchronizedEvent;
import com.topvision.platform.message.event.CmcSynchronizedListener;
import com.topvision.platform.message.service.MessageService;

/**
 * @author fanzidong
 * @created @2013-4-26-下午8:37:02
 * 
 */
@Service("cmcSyslogConfigService")
public class CmcSyslogConfigServiceImpl extends CmcBaseCommonService
        implements CmcSyslogConfigService, CmcSynchronizedListener {
    @Resource(name = "cmcSyslogConfigDao")
    private CmcSyslogConfigDao cmcSyslogConfigDao;
    @Autowired
    private MessageService messageService;
    @Autowired
    private EntityTypeService entityTypeService;
    @Autowired
    private EntityService entityService;
    @Resource(name = "deviceVersionService")
    private DeviceVersionService deviceVersionService;

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

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.cmc.service.CmcSyslogConfigService#getCmcEventLevel(java.lang.Long)
     */
    @Override
    public List<CmcSyslogRecordType> getCmcEventLevel(Long entityId) {
        return cmcSyslogConfigDao.getCmcRecordTypeMinLvl(entityId);
    }

    @Override
    public List<CmcSyslogRecordTypeII> getCmcRecordTypeMinLvlII(Long entityId) {
        return cmcSyslogConfigDao.getCmcRecordTypeMinLvlII(entityId);
    }

    @Override
    public void updateCmcRecordEventLevelII(CmcSyslogRecordTypeII cmcSyslogRecordTypeII) {
        // 更新设备
        snmpParam = getSnmpParamByEntityId(cmcSyslogRecordTypeII.getEntityId());
        getCmcFacade(snmpParam.getIpAddress()).updateCmcRcdEvtLvlII(snmpParam, cmcSyslogRecordTypeII);
        // 更新数据库
        cmcSyslogConfigDao.updateRcdTypeMinEvtLvlII(cmcSyslogRecordTypeII);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.cmc.service.CmcSyslogConfigService#updateCmcRecordEventLevel(java.lang.
     * Long, java.lang.String, int)
     */
    @Override
    public void updateCmcRecordEventLevel(Long entityId, String recordType, int minLevel) {
        // 更新设备
        snmpParam = getSnmpParamByEntityId(entityId);
        CmcSyslogConfig cmcSyslogConfig = new CmcSyslogConfig();
        cmcSyslogConfig.setEntityId(entityId);
        if (recordType.equals(CmcConstants.RECORDTYPE_LOCALNONVOL)) {
            cmcSyslogConfig.setTopCcmtsSyslogFlash(minLevel);
        } else if (recordType.equals(CmcConstants.RECORDTYPE_TRAPS)) {
            cmcSyslogConfig.setTopCcmtsSyslogTrapServer(minLevel);
        } else if (recordType.equals(CmcConstants.RECORDTYPE_SYSLOG)) {
            cmcSyslogConfig.setTopCcmtsSysloglogServer(minLevel);
        } else if (recordType.equals(CmcConstants.RECORDTYPE_LOCALVOLATILE)) {
            cmcSyslogConfig.setTopCcmtsSyslogMemory(minLevel);
        }
        getCmcFacade(snmpParam.getIpAddress()).updateCmcRcdEvtLvl(snmpParam, cmcSyslogConfig);
        // 更新数据库
        CmcSyslogRecordType cmcSyslogRecordType = new CmcSyslogRecordType();
        cmcSyslogRecordType.setEntityId(entityId);
        cmcSyslogRecordType.setTopCcmtsSyslogMinEvtLvl(minLevel);
        cmcSyslogRecordType.setTopCcmtsSyslogRecordType(recordType);
        cmcSyslogConfigDao.updateRcdTypeMinEvtLvl(cmcSyslogRecordType);
    }

    @Override
    public void undoAllEventLevels(Long entityId) {
        // 更新设备
        snmpParam = getSnmpParamByEntityId(entityId);
        CmcSyslogConfig cmcSyslogConfig = new CmcSyslogConfig();
        cmcSyslogConfig.setEntityId(entityId);
        cmcSyslogConfig.setTopCcmtsSyslogFlash(CmcSyslogConfig.DEFAULTLEVEL);
        cmcSyslogConfig.setTopCcmtsSyslogTrapServer(CmcSyslogConfig.DEFAULTLEVEL);
        cmcSyslogConfig.setTopCcmtsSysloglogServer(CmcSyslogConfig.DEFAULTLEVEL);
        cmcSyslogConfig.setTopCcmtsSyslogMemory(CmcSyslogConfig.DEFAULTLEVEL);
        getCmcFacade(snmpParam.getIpAddress()).updateCmcRcdEvtLvl(snmpParam, cmcSyslogConfig);
        // 更新数据库
        cmcSyslogConfigDao.undoAllMinEvtLvls(entityId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.cmc.service.CmcSyslogConfigService#getCmcSyslogConfig(java.lang.Long)
     */
    @Override
    public CmcSyslogConfig getCmcSyslogConfig(Long entityId) {
        return cmcSyslogConfigDao.getCmcSyslogConfig(entityId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.cmc.service.CmcSyslogConfigService#updateCmcSyslogConfig(com.topvision.
     * ems.cmc.facade.domain.CmcSyslogConfig)
     */
    @Override
    public void updateCmcSyslogConfig(CmcSyslogConfig cmcSyslogConfig) {
        // 更新设备
        snmpParam = getSnmpParamByEntityId(cmcSyslogConfig.getEntityId());
        getCmcFacade(snmpParam.getIpAddress()).updateCmcConfigParams(snmpParam, cmcSyslogConfig);
        // 更新数据库
        cmcSyslogConfigDao.updateCmcSyslogConfigParams(cmcSyslogConfig);
    }

    @Override
    public CmcSyslogConfig getSyslogConfigFromEntity(Long entityId) {
        refreshSyslogConfig(entityId);
        CmcSyslogConfig cmcSyslogConfig = getCmcSyslogConfig(entityId);
        return cmcSyslogConfig;
    }

    @Override
    public Boolean isSupportSyslogII(Long entityId) {
        Entity entity = entityService.getEntity(entityId);
        if (!entityTypeService.isCcmtsWithAgent(entity.getTypeId())) {
            return false;
        }
        return deviceVersionService.isFunctionSupported(entityId, "syslogII");
    }
   
    private void refreshSyslogConfig(Long entityId) {
        boolean isOld = true;
        if (isSupportSyslogII(entityId)) {
            isOld = false;
            // 对独立型CC，并且支持了新MIB的采用新MIB来实现读取
            snmpParam = getSnmpParamByEntityId(entityId);
            List<CmcSyslogRecordTypeII> cmcSyslogRecordTypeIIs = getCmcFacade(snmpParam.getIpAddress())
                    .getCmcSyslogRecordTypeII(snmpParam);
            for (CmcSyslogRecordTypeII cmcSyslogRecordTypeII : cmcSyslogRecordTypeIIs) {
                cmcSyslogRecordTypeII.setEntityId(entityId);
                cmcSyslogConfigDao.updateRcdTypeMinEvtLvlII(cmcSyslogRecordTypeII);
            }
        }
        snmpParam = getSnmpParamByEntityId(entityId);
        CmcSyslogConfig cmcSyslogConfig = getCmcFacade(snmpParam.getIpAddress()).getCmcSyslogConfig(snmpParam);
        cmcSyslogConfig.setEntityId(entityId);
        cmcSyslogConfigDao.insertCmcSyslogConfig(cmcSyslogConfig);

        if (isOld) {
            // OLT和老版本还是需要通过老节点来读取的
            CmcSyslogRecordType cmcSyslogRecordType = new CmcSyslogRecordType(cmcSyslogConfig.getEntityId(),
                    CmcConstants.RECORDTYPE_LOCALNONVOL, cmcSyslogConfig.getTopCcmtsSyslogFlash());
            cmcSyslogConfigDao.updateRcdTypeMinEvtLvl(cmcSyslogRecordType);
            cmcSyslogRecordType = new CmcSyslogRecordType(cmcSyslogConfig.getEntityId(),
                    CmcConstants.RECORDTYPE_LOCALVOLATILE, cmcSyslogConfig.getTopCcmtsSyslogMemory());
            cmcSyslogConfigDao.updateRcdTypeMinEvtLvl(cmcSyslogRecordType);
            cmcSyslogRecordType = new CmcSyslogRecordType(cmcSyslogConfig.getEntityId(), CmcConstants.RECORDTYPE_SYSLOG,
                    cmcSyslogConfig.getTopCcmtsSysloglogServer());
            cmcSyslogConfigDao.updateRcdTypeMinEvtLvl(cmcSyslogRecordType);
            cmcSyslogRecordType = new CmcSyslogRecordType(cmcSyslogConfig.getEntityId(), CmcConstants.RECORDTYPE_TRAPS,
                    cmcSyslogConfig.getTopCcmtsSyslogTrapServer());
            cmcSyslogConfigDao.updateRcdTypeMinEvtLvl(cmcSyslogRecordType);
        }
    }

    public CmcSyslogConfigDao getCmcSyslogConfigDao() {
        return cmcSyslogConfigDao;
    }

    public void setCmcSyslogConfigDao(CmcSyslogConfigDao cmcSyslogConfigDao) {
        this.cmcSyslogConfigDao = cmcSyslogConfigDao;
    }

    @Override
    public void insertEntityStates(CmcSynchronizedEvent event) {
        if (event.getEntityType().equals(entityTypeService.getCcmtswithagentType())) {
            try {
                refreshSyslogConfig(event.getEntityId());
                logger.info("refreshSyslogConfig finish");
            } catch (Exception e) {
                logger.error("refreshSyslogConfig wrong", e);
            }
        }
    }

}
