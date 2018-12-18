/***********************************************************************
 * $Id: LogicInterfaceServiceImpl.java,v1.0 2016年10月14日 上午10:36:53 $
 * 
 * @author: lzt
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.logicinterface.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.epon.logicinterface.dao.LogicInterfaceDao;
import com.topvision.ems.epon.logicinterface.domain.InterfaceIpV4Config;
import com.topvision.ems.epon.logicinterface.domain.LogicInterface;
import com.topvision.ems.epon.logicinterface.facede.LogicInterfaceFacade;
import com.topvision.ems.epon.logicinterface.service.LogicInterfaceService;
import com.topvision.ems.network.service.EntityService;
import com.topvision.framework.service.BaseService;
import com.topvision.framework.snmp.SnmpParam;
import com.topvision.platform.facade.FacadeFactory;
import com.topvision.platform.message.event.SynchronizedEvent;
import com.topvision.platform.message.event.SynchronizedListener;
import com.topvision.platform.message.service.MessageService;

/**
 * @author lzt
 * @created @2016年10月14日-上午10:36:53
 *
 */
@Service("logicInterfaceService")
public class LogicInterfaceServiceImpl extends BaseService implements LogicInterfaceService, SynchronizedListener {
    @Autowired
    private MessageService messageService;
    @Autowired
    private FacadeFactory facadeFactory;
    @Autowired
    private EntityService entityService;
    @Autowired
    private LogicInterfaceDao logicInterfaceDao;

    @Override
    @PostConstruct
    public void initialize() {
        super.initialize();
        messageService.addListener(SynchronizedListener.class, this);
    }

    @Override
    @PreDestroy
    public void destroy() {
        super.destroy();
    }

    @Override
    public void insertEntityStates(SynchronizedEvent event) {
        Long entityId = event.getEntityId();
        try {
            refreshLogicInterface(entityId);
            logger.info("refreshLogicInterface finish");
        } catch (Exception e) {
            logger.error("refreshLogicInterface wrong", e);
        }
        try {
            refreshLogicInterfaceIpConfig(entityId);
            logger.info("refreshLogicInterfaceIpConfig finish");
        } catch (Exception e) {
            logger.error("refreshLogicInterfaceIpConfig wrong", e);
        }
    }

    @Override
    public void updateEntityStates(SynchronizedEvent event) {
    }

    @Override
    public void refreshLogicInterface(Long entityId) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        List<LogicInterface> logicInterfaceList = getLogicInterfaceFacade(snmpParam.getIpAddress())
                .getLogicInterfaceList(snmpParam);
        logicInterfaceDao.updateLogicInterfaceList(entityId, logicInterfaceList);
    }

    @Override
    public void refreshLogicInterfaceIpConfig(Long entityId) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        List<InterfaceIpV4Config> interfaceIpV4ConfigList = getLogicInterfaceFacade(snmpParam.getIpAddress())
                .getInterfaceIpV4ConfigList(snmpParam);
        logicInterfaceDao.updateLogicInterfaceIpV4List(entityId, interfaceIpV4ConfigList);
    }

    /**
     * 获取Facade对象
     * 
     * @param ip
     *            被采集设备IP
     * @return LogicInterfaceFacade
     */
    private LogicInterfaceFacade getLogicInterfaceFacade(String ip) {
        return facadeFactory.getFacade(ip, LogicInterfaceFacade.class);
    }

    @Override
    public List<LogicInterface> getOltLogicInterfaceByType(Map<String, Object> map) {
        return logicInterfaceDao.getOltLogicInterfaceByType(map);
    }

    @Override
    public int getOltLogicInterfaceByTypeCount(Map<String, Object> map) {
        return logicInterfaceDao.getOltLogicInterfaceByTypeCount(map);
    }

    @Override
    public LogicInterface getOltLogicInterface(Long entityId, Integer interfaceType, Integer interfaceId) {
        return logicInterfaceDao.getOltLogicInterface(entityId, interfaceType, interfaceId);
    }

    @Override
    public void addLogicInterface(LogicInterface logicInterface) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(logicInterface.getEntityId());
        this.getLogicInterfaceFacade(snmpParam.getIpAddress()).addLogicInterface(snmpParam, logicInterface);
        LogicInterface newLogicInterface = this.getLogicInterfaceFacade(snmpParam.getIpAddress()).getLogicInterface(
                snmpParam, logicInterface.getInterfaceType(), logicInterface.getInterfaceId());
        newLogicInterface.setEntityId(logicInterface.getEntityId());
        logicInterfaceDao.insertLogicInterface(newLogicInterface);
    }

    @Override
    public void updateLogicInterface(LogicInterface logicInterface) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(logicInterface.getEntityId());
        LogicInterface newLogicInterface = this.getLogicInterfaceFacade(snmpParam.getIpAddress()).modifyLogicInterface(
                snmpParam, logicInterface);
        logicInterfaceDao.updateLogicInterface(newLogicInterface);
    }

    @Override
    public void deleteLogicInterface(Long entityId, Integer interfaceType, Integer interfaceId) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        LogicInterface logicInterface = new LogicInterface();
        logicInterface.setInterfaceType(interfaceType);
        logicInterface.setInterfaceId(interfaceId);
        this.getLogicInterfaceFacade(snmpParam.getIpAddress()).deleteLogicInterface(snmpParam, logicInterface);
        // 删除接口后需要同步删除接口下的IP配置
        logicInterfaceDao.deleteIpV4ConfigByInterface(entityId, interfaceType, interfaceId);
        logicInterfaceDao.deleteLogicInterface(entityId, interfaceType, interfaceId);

    }

    @Override
    public List<InterfaceIpV4Config> getInterfaceIpList(Map<String, Object> map) {
        return logicInterfaceDao.getInterfaceIpList(map);
    }

    @Override
    public InterfaceIpV4Config getInterfaceIpV4Config(Long entityId, Integer ipV4ConfigIndex, String ipV4Addr) {
        return logicInterfaceDao.getInterfaceIpV4Config(entityId, ipV4ConfigIndex, ipV4Addr);
    }

    @Override
    public void addInterfaceIpV4Config(InterfaceIpV4Config ipV4Config) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(ipV4Config.getEntityId());
        InterfaceIpV4Config newIpV4Config = this.getLogicInterfaceFacade(snmpParam.getIpAddress())
                .addInterfaceIpV4Config(snmpParam, ipV4Config);
        if(ipV4Config.getIpV4AddrType() == 0){
            logicInterfaceDao.deletePriIpV4Config(ipV4Config.getEntityId(), ipV4Config.getIpV4ConfigIndex());
        }
        logicInterfaceDao.insertInterfaceIpV4Config(newIpV4Config);
    }

    @Override
    public void updateInterfaceIpV4Config(InterfaceIpV4Config ipV4Config) {
        logicInterfaceDao.updateInterfaceIpV4Config(ipV4Config);
    }

    @Override
    public void deleteInterfaceIpV4Config(Long entityId, Integer ipV4ConfigIndex, String ipV4Addr) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        InterfaceIpV4Config ipV4Config = new InterfaceIpV4Config();
        ipV4Config.setIpV4ConfigIndex(ipV4ConfigIndex);
        ipV4Config.setIpV4Addr(ipV4Addr);
        this.getLogicInterfaceFacade(snmpParam.getIpAddress()).deleteInterfaceIpV4Config(snmpParam, ipV4Config);
        logicInterfaceDao.deleteInterfaceIpV4Config(entityId, ipV4ConfigIndex, ipV4Addr);
    }
}
