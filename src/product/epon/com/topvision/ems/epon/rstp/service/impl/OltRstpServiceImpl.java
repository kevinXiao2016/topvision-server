/***********************************************************************
 * $Id: OltRstpServiceImpl.java,v1.0 2013-10-25 下午5:33:54 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.rstp.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.epon.olt.dao.OltSniDao;
import com.topvision.ems.epon.rstp.dao.OltRstpDao;
import com.topvision.ems.epon.rstp.domain.OltStpGlobalConfig;
import com.topvision.ems.epon.rstp.domain.OltStpPortConfig;
import com.topvision.ems.epon.rstp.facade.OltRstpFacade;
import com.topvision.ems.epon.rstp.service.OltRstpService;
import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.network.service.EntityService;
import com.topvision.ems.template.service.EntityTypeService;
import com.topvision.framework.common.LoggerUtil;
import com.topvision.framework.service.BaseService;
import com.topvision.framework.snmp.SnmpParam;
import com.topvision.framework.utils.EponIndex;
import com.topvision.platform.facade.FacadeFactory;
import com.topvision.platform.message.event.SynchronizedEvent;
import com.topvision.platform.message.event.SynchronizedListener;
import com.topvision.platform.message.service.MessageService;

/**
 * @author flack
 * @created @2013-10-25-下午5:33:54
 *
 */
@Service("oltRstpService")
public class OltRstpServiceImpl extends BaseService implements OltRstpService, SynchronizedListener {
    @Autowired
    private OltRstpDao oltRstpDao;
    @Autowired
    private FacadeFactory facadeFactory;
    @Autowired
    protected MessageService messageService;
    @Autowired
    private EntityService entityService;
    @Autowired
    private OltSniDao oltSniDao;
    @Autowired
    private EntityTypeService entityTypeService;

    @Override
    @PreDestroy
    public void destroy() {
        super.destroy();
        messageService.removeListener(SynchronizedListener.class, this);
    }

    @Override
    @PostConstruct
    public void initialize() {
        super.initialize();
        messageService.addListener(SynchronizedListener.class, this);
    }

    @Override
    public void insertEntityStates(SynchronizedEvent event) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(event.getEntityId());
        Long timeTmp = 0L;
        timeTmp = LoggerUtil.topoStartTimeLog(event.getIpAddress(), "OltStpGlobalConfig");
        try {
            discoveryOltStpGlobalConfig(snmpParam);
            logger.info("discoveryOltStpGlobalConfig finish");
        } catch (Exception e) {
            logger.error("discoveryOltStpGlobalConfig wrong", e);
        }
        LoggerUtil.topoEndTimeLog(event.getIpAddress(), "OltStpGlobalConfig", timeTmp);

        timeTmp = LoggerUtil.topoStartTimeLog(event.getIpAddress(), "OltStpPortConfig");
        try {
            discoveryOltStpPortConfig(snmpParam);
            logger.info("discoveryOltStpPortConfig finish");
        } catch (Exception e) {
            logger.error("discoveryOltStpPortConfig wrong", e);
        }
        LoggerUtil.topoEndTimeLog(event.getIpAddress(), "OltStpPortConfig", timeTmp);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.message.event.SynchronizedListener#updateEntityStates(com.topvision.ems
     * .message.event.SynchronizedEvent)
     */
    @Override
    public void updateEntityStates(SynchronizedEvent event) {
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.epon.service.OltStpService#refreshOltStpGlobalConfig(java.lang.Long)
     */
    @Override
    public void refreshOltStpGlobalConfig(Long entityId) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        discoveryOltStpGlobalConfig(snmpParam);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.epon.service.OltStpService#refreshOltStpPortConfig(java.lang.Long)
     */
    @Override
    public void refreshOltStpPortConfig(Long entityId) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        discoveryOltStpPortConfig(snmpParam);
    }

    private void discoveryOltStpGlobalConfig(SnmpParam snmpParam) {
        // 如果结果为空 却不能区分是SNMP错误或者是数据就是为空
        if (getOltRstpFacade(snmpParam.getIpAddress()).getOltStpGlobalConfig(snmpParam) != null) {
            OltStpGlobalConfig globalConfig = getOltRstpFacade(snmpParam.getIpAddress()).getOltStpGlobalConfig(
                    snmpParam);
            globalConfig.setEntityId(snmpParam.getEntityId());
            oltRstpDao.batchInsertOltStpGlobalConfig(snmpParam.getEntityId(), globalConfig);
        }
    }

    private void discoveryOltStpPortConfig(SnmpParam snmpParam) {
        List<OltStpPortConfig> portConfigs = new ArrayList<OltStpPortConfig>();
        portConfigs = getOltRstpFacade(snmpParam.getIpAddress()).getOltStpPortConfigs(snmpParam);
        if (portConfigs.size() > 0) {
            for (OltStpPortConfig portConfig : portConfigs) {
                portConfig.setEntityId(snmpParam.getEntityId());
            }
        }
        oltRstpDao.batchInsertOltStpPortConfig(snmpParam.getEntityId(), portConfigs);
    }

    /**
     * 获取OltStpFacade对象
     * 
     * @param ip
     *            被采集设备IP
     * @return OltStpFacade
     */
    private OltRstpFacade getOltRstpFacade(String ip) {
        return facadeFactory.getFacade(ip, OltRstpFacade.class);
    }

    @Override
    public OltStpGlobalConfig getOltStpGlobalConfig(Long entityId) {
        Entity entity = entityService.getEntity(entityId);
        OltStpGlobalConfig stpGlobalConfig = oltRstpDao.getOltStpGlobalConfig(entityId);
        if (!"00/00".equals(stpGlobalConfig.getRootPortString())) {
            Integer sniSlotNo = Integer.parseInt(stpGlobalConfig.getRootPortString().split("/")[0]);
            Integer sniPortNo = Integer.parseInt(stpGlobalConfig.getRootPortString().split("/")[1]);
            if (entityTypeService.isPN8602_EType(entity.getTypeId())
                    || entityTypeService.isPN8602_EFType(entity.getTypeId())) {
                sniPortNo = sniPortNo + 16;
            }
            Long sniIndex = EponIndex.getSniIndex(sniSlotNo, sniPortNo);
            String sniDisplayName = oltSniDao.querySniDisplayName(entityId, sniIndex);
            if (sniDisplayName != null) {
                stpGlobalConfig.setRootPortString(sniDisplayName);
            }
        }
        return stpGlobalConfig;
    }

    @Override
    public void setStpGlobalSetEnable(Long entityId, Integer stpGlobalSetEnable) {
        OltStpGlobalConfig globalConfig = new OltStpGlobalConfig();
        globalConfig.setEntityId(entityId);
        globalConfig.setEnable(stpGlobalSetEnable);
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        getOltRstpFacade(snmpParam.getIpAddress()).updateOltStpGlobalEnable(snmpParam, globalConfig);
        /*  
          // 此处在开启设备RSTP使能时，同时刷新设备RSTP配置和SNI口RSTP配置
          // 此处可以采用消息机制实现,设备在rstp使能后会存在7s左右的中断
          SynchronizedEvent stpEvent = new SynchronizedEvent(this);
          if (stpGlobalSetEnable == 1) {
              try {
                  stpEvent.setAction(SynchronizedEvent.ADD_SYNCHRONIZED);
                  stpEvent.setActionName("insertEntityStates");
                  stpEvent.setEntityId(entityId);
                  stpEvent.setEventType("stpEnableManagement");
                  stpEvent.setListener(SynchronizedListener.class);
                  Thread.sleep(10000);
              } catch (InterruptedException e) {
                  logger.error("setStpGlobalSetEnable:{}", e);
              } finally {
                  messageService.addMessage(stpEvent);
              }
          }
          */
        oltRstpDao.updateOltStpGlobalEnable(entityId, stpGlobalSetEnable);
    }

    @Override
    public OltStpPortConfig getOltStpPortConfig(Long entityId, Long portId) {
        return oltRstpDao.getOltStpPortConfig(entityId, portId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.epon.service.OltStpService#setStpPortEnabled(java.lang.Long,
     * java.lang.Long, java.lang.Integer)
     */
    @Override
    public void setStpPortEnabled(Long entityId, Long portId, Integer stpPortEnabled) {
        OltStpPortConfig portConfig = new OltStpPortConfig();
        portConfig.setEntityId(entityId);
        portConfig.setSniIndex(oltSniDao.getSniIndex(portId));
        portConfig.setStpPortEnabled(stpPortEnabled);
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        getOltRstpFacade(snmpParam.getIpAddress()).updateOltStpPortConfig(snmpParam, portConfig);
        oltRstpDao.updateOltStpPortEnable(entityId, portId, stpPortEnabled);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.epon.service.OltStpService#setPortRstpProtocolMigration(java.lang.Long,
     * java.lang.Long, java.lang.Integer)
     */
    @Override
    public void setPortRstpProtocolMigration(Long entityId, Long portId, Integer status) {
        OltStpPortConfig portConfig = new OltStpPortConfig();
        portConfig.setEntityId(entityId);
        portConfig.setSniIndex(oltSniDao.getSniIndex(portId));
        portConfig.setStpPortRstpProtocolMigration(status);
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        getOltRstpFacade(snmpParam.getIpAddress()).updateOltStpPortConfig(snmpParam, portConfig);
        oltRstpDao.updateOltStpPortProtocolMigration(entityId, portId, status);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.epon.service.OltStpService#updateStpGlobalConfig(java.lang.Long,
     * com.topvision.ems.epon.facade.domain.OltStpGlobalConfig)
     */
    @Override
    public void updateStpGlobalConfig(Long entityId, OltStpGlobalConfig oltStpGlobalConfig) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        getOltRstpFacade(snmpParam.getIpAddress()).updateOltStpGlobalConfig(snmpParam, oltStpGlobalConfig);
        oltRstpDao.updateOltStpGlobalConfig(oltStpGlobalConfig);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.epon.service.OltStpService#updateStpPortConfig(java.lang.Long,
     * com.topvision.ems.epon.facade.domain.OltStpPortConfig)
     */
    @Override
    public void updateStpPortConfig(Long entityId, OltStpPortConfig oltStpPortConfig) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        getOltRstpFacade(snmpParam.getIpAddress()).updateOltStpPortConfig(snmpParam, oltStpPortConfig);
        oltRstpDao.updateOltStpPortConfig(entityId, oltStpPortConfig);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.epon.service.OltStpService#getEnablePortList(java.lang.Long)
     */
    @Override
    public List<Long> getEnablePortList(Long entityId) {
        return oltRstpDao.getStpEnablePortList(entityId);
    }

}
