/***********************************************************************
 * $Id: CmcSyslogServerServiceImpl.java,v1.0 2013-4-23 下午4:10:54 $
 * 
 * @author: Administrator
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
import com.topvision.ems.cmc.syslog.dao.CmcSyslogServerDao;
import com.topvision.ems.cmc.syslog.facade.domain.CmcSyslogServerEntry;
import com.topvision.ems.cmc.syslog.service.CmcSyslogServerService;
import com.topvision.ems.template.service.EntityTypeService;
import com.topvision.platform.message.event.CmcSynchronizedEvent;
import com.topvision.platform.message.event.CmcSynchronizedListener;
import com.topvision.platform.message.service.MessageService;

/**
 * @author Administrator
 * @created @2013-4-23-下午4:10:54
 * 
 */
@Service("cmcSyslogServerService")
public class CmcSyslogServerServiceImpl extends CmcBaseCommonService implements CmcSyslogServerService,
        CmcSynchronizedListener {
    @Resource(name = "cmcSyslogServerDao")
    private CmcSyslogServerDao cmcSyslogServerDao;
    @Autowired
    private MessageService messageService;
    @Autowired
    private EntityTypeService entityTypeService;

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
     * @see com.topvision.ems.cmc.service.CmcSyslogServerService#getCmcSyslogServer(java.lang.Long)
     */
    @Override
    public List<CmcSyslogServerEntry> getCmcSyslogServer(Long entityId) {
        List<CmcSyslogServerEntry> cmsSyslogServers = cmcSyslogServerDao.getCmcSyslogServerListByEntityId(entityId);
        return cmsSyslogServers;
    }

    @Override
    public List<CmcSyslogServerEntry> getEntitySyslogServer(Long entityId) {
        // TODO Auto-generated method stub
        snmpParam = getSnmpParamByEntityId(entityId);
        return getCmcFacade(snmpParam.getIpAddress()).getCmcSyslogServerAttrs(snmpParam);
    }

    @Override
    public void refreshDatabase(List<CmcSyslogServerEntry> cmcSyslogServerEntrys, Long entityId) {
        // 批量刷新数据
        cmcSyslogServerDao.batchRefreshCmcSyslogServerEntrys(cmcSyslogServerEntrys, entityId);
    }

    @Override
    public void insertCmcSyslogServer(CmcSyslogServerEntry cmcSyslogServerEntry) {
        // 首先向设备添加syslog服务器
        snmpParam = getSnmpParamByEntityId(cmcSyslogServerEntry.getEntityId());
        getCmcFacade(snmpParam.getIpAddress()).createCmcSyslogServer(snmpParam, cmcSyslogServerEntry);
        // 如果成功，则向数据库插入数据
        cmcSyslogServerDao.insertCmcSyslogServerEntry(cmcSyslogServerEntry);
    }

    @Override
    public void deleteCmcSyslogServer(CmcSyslogServerEntry cmcSyslogServerEntry) {
        // 首先从设备删除syslog服务器
        snmpParam = getSnmpParamByEntityId(cmcSyslogServerEntry.getEntityId());
        getCmcFacade(snmpParam.getIpAddress()).deleteCmcSyslogServer(snmpParam, cmcSyslogServerEntry);
        // 如果成功，则从数据库删除数据
        cmcSyslogServerDao.deleteCmcSyslogServerEntry(cmcSyslogServerEntry);
    }

    public CmcSyslogServerDao getCmcSyslogServerDao() {
        return cmcSyslogServerDao;
    }

    public void setCmcSyslogServerDao(CmcSyslogServerDao cmcSyslogServerDao) {
        this.cmcSyslogServerDao = cmcSyslogServerDao;
    }

    @Override
    public void modifyCmcSyslogServer(CmcSyslogServerEntry cmcSyslogServerEntry) {
        long entityId = cmcSyslogServerEntry.getEntityId();
        snmpParam = getSnmpParamByEntityId(entityId);
        getCmcFacade(snmpParam.getIpAddress()).updateCmcSyslogServer(snmpParam, cmcSyslogServerEntry);
        cmcSyslogServerDao.updateCmcSyslogServerEntry(cmcSyslogServerEntry);
    }

    @Override
    public void insertEntityStates(CmcSynchronizedEvent event) {
        if (event.getEntityType().equals(entityTypeService.getCcmtswithagentType())) {
            long entityId = event.getEntityId();
            try {
                List<CmcSyslogServerEntry> list = getEntitySyslogServer(entityId);
                cmcSyslogServerDao.batchRefreshCmcSyslogServerEntrys(list, entityId);
                logger.info("refreshSyslogConfig finish");
            } catch (Exception e) {
                logger.error("refreshSyslogConfig wrong", e);
            }
        }
    }
}
