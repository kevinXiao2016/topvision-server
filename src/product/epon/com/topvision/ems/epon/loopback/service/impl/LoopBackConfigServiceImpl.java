/***********************************************************************
 * $Id: LoopBackConfigServiceImpl.java,v1.0 2013-11-16 上午11:51:33 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.loopback.service.impl;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.epon.loopback.dao.LoopBackConfigDao;
import com.topvision.ems.epon.loopback.domain.LoopbackConfigTable;
import com.topvision.ems.epon.loopback.domain.LoopbackSubIpTable;
import com.topvision.ems.epon.loopback.facade.LoopBackFacade;
import com.topvision.ems.epon.loopback.service.LoopBackConfigService;
import com.topvision.ems.network.service.EntityService;
import com.topvision.framework.service.BaseService;
import com.topvision.framework.snmp.SnmpParam;
import com.topvision.platform.facade.FacadeFactory;
import com.topvision.platform.message.event.SynchronizedEvent;
import com.topvision.platform.message.event.SynchronizedListener;
import com.topvision.platform.message.service.MessageService;

/**
 * @author flack
 * @created @2013-11-16-上午11:51:33
 */
@Service("loopBackConfigService")
public class LoopBackConfigServiceImpl extends BaseService implements LoopBackConfigService, SynchronizedListener {
    @Autowired
    private MessageService messageService;
    @Autowired
    private LoopBackConfigDao loopBackConfigDao;
    @Autowired
    private EntityService entityService;
    @Autowired
    private FacadeFactory facadeFactory;

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.framework.service.BaseService#initialize()
     */
    @Override
    @PostConstruct
    public void initialize() {
        super.initialize();
        messageService.addListener(SynchronizedListener.class, this);
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

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.platform.message.event.SynchronizedListener#insertEntityStates(com.topvision
     * .platform.message.event.SynchronizedEvent)
     */
    @Override
    public void insertEntityStates(SynchronizedEvent event) {
        Long entityId = event.getEntityId();
        try {
            refreshLoopBackConfig(entityId);
            logger.info("refreshLoopBackConfig finish");
        } catch (Exception e) {
            logger.error("refreshLoopBackConfig wrong", e);
        }
        try {
            refreshLoopBackSub(entityId);
            logger.info("refreshLoopBackSub finish");
        } catch (Exception e) {
            logger.error("refreshLoopBackSub wrong", e);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.platform.message.event.SynchronizedListener#updateEntityStates(com.topvision
     * .platform.message.event.SynchronizedEvent)
     */
    @Override
    public void updateEntityStates(SynchronizedEvent event) {
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.epon.loopback.service.LoopBackConfigService#refreshLoopBackConfig(java.
     * lang.Long)
     */
    @Override
    public void refreshLoopBackConfig(Long entityId) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        List<LoopbackConfigTable> loopbackConfigTables = getLoopBackFacade(snmpParam.getIpAddress())
                .getLoopbackConfigTables(snmpParam);
        loopBackConfigDao.refreshLoopBackConfigTable(entityId, loopbackConfigTables);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.epon.loopback.service.LoopBackConfigService#refreshLoopBackSub(java.lang
     * .Long)
     */
    @Override
    public void refreshLoopBackSub(Long entityId) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        List<LoopbackSubIpTable> loopbackSubIpTables = getLoopBackFacade(snmpParam.getIpAddress())
                .getLoopbackSubIpTables(snmpParam);
        loopBackConfigDao.refreshLoopBackSubTable(entityId, loopbackSubIpTables);
    }

    /**
     * 获取Facade对象
     * 
     * @param ip
     *            被采集设备IP
     * @return LoopBackFacade
     */
    private LoopBackFacade getLoopBackFacade(String ip) {
        return facadeFactory.getFacade(ip, LoopBackFacade.class);
    }

    @Override
    public List<LoopbackConfigTable> getLBInterfaceList(Long entityId) {
        return loopBackConfigDao.queryLoopbackList(entityId);
    }

    @Override
    public void addLoopBackInterface(LoopbackConfigTable loopBack) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(loopBack.getEntityId());
        LoopbackConfigTable newLoopBack = this.getLoopBackFacade(snmpParam.getIpAddress()).addLoopBackInterface(
                snmpParam, loopBack);
        loopBackConfigDao.insertLoopBackInterface(newLoopBack);
    }

    @Override
    public void deleteLoopBackInterface(LoopbackConfigTable loopBack) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(loopBack.getEntityId());
        this.getLoopBackFacade(snmpParam.getIpAddress()).deleteLoopBackInterface(snmpParam, loopBack);
        loopBackConfigDao.deleteLoopBackInterface(loopBack);
        //需要同步删除该环回接口下所有子IP配置
        if (!this.getLBSubIpList(loopBack.getEntityId(), loopBack.getLoopbackIndex()).isEmpty()) {
            LoopbackSubIpTable subIpTable = new LoopbackSubIpTable();
            subIpTable.setEntityId(loopBack.getEntityId());
            subIpTable.setLoopbackSubIpIndex(loopBack.getLoopbackIndex());
            loopBackConfigDao.deleteLBSubIp(subIpTable);
        }
    }

    @Override
    public List<LoopbackSubIpTable> getLBSubIpList(Long entityId, Integer interfaceIndex) {
        return loopBackConfigDao.querySubIpList(entityId, interfaceIndex);
    }

    @Override
    public void addLBSubIp(LoopbackSubIpTable subIpTable) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(subIpTable.getEntityId());
        LoopbackSubIpTable newSubIp = this.getLoopBackFacade(snmpParam.getIpAddress()).addLoopBackSubIp(snmpParam,
                subIpTable);
        loopBackConfigDao.insertLBSubIp(newSubIp);
    }

    @Override
    public void deleteLBSubIp(LoopbackSubIpTable subIpTable) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(subIpTable.getEntityId());
        this.getLoopBackFacade(snmpParam.getIpAddress()).deleteLoopBackSubIp(snmpParam, subIpTable);
        loopBackConfigDao.deleteLBSubIp(subIpTable);
    }

    @Override
    public void modifyLoopBackInterface(LoopbackConfigTable loopBack) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(loopBack.getEntityId());
        LoopbackConfigTable newLoopBack = this.getLoopBackFacade(snmpParam.getIpAddress()).modifyLoopBackInterface(
                snmpParam, loopBack);
        loopBackConfigDao.updateLoopBackInterface(newLoopBack);
    }

    @Override
    public void modifyLBSubIp(LoopbackSubIpTable subIpTable) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(subIpTable.getEntityId());
        LoopbackSubIpTable newSubIp = this.getLoopBackFacade(snmpParam.getIpAddress()).modifyLoopBackSubIp(snmpParam,
                subIpTable);
        loopBackConfigDao.updateLBSubIp(newSubIp);
    }

}
