/***********************************************************************
 * $Id: SnmpV3ServiceImpl.java,v1.0 2013-1-9 上午9:32:14 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.snmpV3.service.impl;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.network.service.EntityService;
import com.topvision.ems.snmpV3.dao.SnmpV3Dao;
import com.topvision.ems.snmpV3.dao.SnmpV3UserDao;
import com.topvision.ems.snmpV3.facade.SnmpV3Facade;
import com.topvision.ems.snmpV3.facade.domain.SnmpNotifyFilterProfile;
import com.topvision.ems.snmpV3.facade.domain.SnmpNotifyFilterTable;
import com.topvision.ems.snmpV3.facade.domain.SnmpNotifyTable;
import com.topvision.ems.snmpV3.facade.domain.SnmpTargetParams;
import com.topvision.ems.snmpV3.facade.domain.SnmpTargetTable;
import com.topvision.ems.snmpV3.service.SnmpV3Service;
import com.topvision.framework.common.LoggerUtil;
import com.topvision.framework.service.BaseService;
import com.topvision.framework.snmp.SnmpParam;
import com.topvision.platform.facade.FacadeFactory;
import com.topvision.platform.message.event.SynchronizedEvent;
import com.topvision.platform.message.event.SynchronizedListener;
import com.topvision.platform.message.service.MessageService;

/**
 * @author Bravin
 * @created @2013-1-9-上午9:32:14
 * 
 */
@Service("snmpV3Service")
public class SnmpV3ServiceImpl extends BaseService implements SnmpV3Service, SynchronizedListener {
    @Autowired
    private SnmpV3Dao snmpV3Dao;
    @Autowired
    private SnmpV3UserDao snmpV3UserDao;
    @Autowired
    private FacadeFactory facadeFactory;
    @Autowired
    private EntityService entityService;
    @Autowired
    private MessageService messageService;

    @Override
    @PostConstruct
    public void initialize() {
        super.initialize();
        messageService.addListener(SynchronizedListener.class, this);
    }

    @PreDestroy
    public void destory() {
        super.destroy();
        messageService.removeListener(SynchronizedListener.class, this);
    }

    @Override
    public void insertEntityStates(SynchronizedEvent event) {
        Long timeTmp = 0L;
        timeTmp = LoggerUtil.topoStartTimeLog(event.getIpAddress(), "SnmpTargetTable");
        try {
            refreshTarget(event.getEntityId());
            logger.info("refresh SNMPV3 Target Finish!");
        } catch (Exception e) {
            logger.error("refresh SNMPV3 Target Wrong!", e);
        }
        LoggerUtil.topoEndTimeLog(event.getIpAddress(), "SnmpTargetTable", timeTmp);
        
        timeTmp = LoggerUtil.topoStartTimeLog(event.getIpAddress(), "SnmpTargetParams");
        try {
            refreshTargetParams(event.getEntityId());
            logger.info("refresh SNMPV3 Target Param Finish!");
        } catch (Exception e) {
            logger.error("refresh SNMPV3 Target Param Wrong!", e);
        }
        LoggerUtil.topoEndTimeLog(event.getIpAddress(), "SnmpTargetParams", timeTmp);
        
        timeTmp = LoggerUtil.topoStartTimeLog(event.getIpAddress(), "SnmpNotifyTable");
        try {
            refreshNotify(event.getEntityId());
            logger.info("refresh SNMPV3 Nodify Finish!");
        } catch (Exception e) {
            logger.error("refresh SNMPV3 Nodify Wrong!", e);
        }
        LoggerUtil.topoEndTimeLog(event.getIpAddress(), "SnmpNotifyTable", timeTmp);
        
        timeTmp = LoggerUtil.topoStartTimeLog(event.getIpAddress(), "SnmpNotifyFilterTable");
        try {
            refreshNotifyFilter(event.getEntityId());
            logger.info("refresh SNMPV3 Notify Filter Finish!");
        } catch (Exception e) {
            logger.error("refresh SNMPV3 Notify Filter Wrong!", e);
        }
        LoggerUtil.topoEndTimeLog(event.getIpAddress(), "SnmpNotifyFilterTable", timeTmp);
        
        timeTmp = LoggerUtil.topoStartTimeLog(event.getIpAddress(), "SnmpNotifyFilterProfile");
        try {
            refreshNotifyProfile(event.getEntityId());
            logger.info("refresh SNMPV3 Notify Profile Finish!");
        } catch (Exception e) {
            logger.error("refresh SNMPV3 Notify Profile Wrong!", e);
        }
        LoggerUtil.topoEndTimeLog(event.getIpAddress(), "SnmpNotifyFilterProfile", timeTmp);
    }

    @Override
    public void updateEntityStates(SynchronizedEvent event) {
    }

    /**
     * @return the entityService
     */
    public EntityService getEntityService() {
        return entityService;
    }

    /**
     * @param entityService
     *            the entityService to set
     */
    public void setEntityService(EntityService entityService) {
        this.entityService = entityService;
    }

    /**
     * @return the facadeFactory
     */
    public FacadeFactory getFacadeFactory() {
        return facadeFactory;
    }

    /**
     * @param facadeFactory
     *            the facadeFactory to set
     */
    public void setFacadeFactory(FacadeFactory facadeFactory) {
        this.facadeFactory = facadeFactory;
    }

    /**
     * @return the snmpV3Dao
     */
    public SnmpV3Dao getSnmpV3Dao() {
        return snmpV3Dao;
    }

    public SnmpV3UserDao getSnmpV3UserDao() {
        return snmpV3UserDao;
    }

    public void setSnmpV3UserDao(SnmpV3UserDao snmpV3UserDao) {
        this.snmpV3UserDao = snmpV3UserDao;
    }

    /**
     * @param snmpV3Dao
     *            the snmpV3Dao to set
     */
    public void setSnmpV3Dao(SnmpV3Dao snmpV3Dao) {
        this.snmpV3Dao = snmpV3Dao;
    }

    /**
     * @param ipAddress
     * @return
     */
    private SnmpV3Facade getSnmpV3Facade(String ipAddress) {
        return facadeFactory.getFacade(ipAddress, SnmpV3Facade.class);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.snmpV3.service.SnmpV3Service#addTarget(com.topvision
     * .ems.snmpV3.facade.domain.SnmpTargetTable)
     */
    @Override
    public void addTarget(SnmpTargetTable o) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(o.getEntityId());
        getSnmpV3Facade(snmpParam.getIpAddress()).addTarget(snmpParam, o);
        snmpV3Dao.insertTarget(o);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.snmpV3.service.SnmpV3Service#modifyTarget(com.topvision
     * .ems.snmpV3.facade.domain.SnmpTargetTable)
     */
    @Override
    public void modifyTarget(SnmpTargetTable o) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(o.getEntityId());
        getSnmpV3Facade(snmpParam.getIpAddress()).modifyTarget(snmpParam, o);
        snmpV3Dao.updateTarget(o);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.snmpV3.service.SnmpV3Service#deleteTarget(java.lang .Long,
     * java.lang.String)
     */
    @Override
    public void deleteTarget(Long entityId, String targetName) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        getSnmpV3Facade(snmpParam.getIpAddress()).deleteTarget(snmpParam, targetName);
        snmpV3Dao.deleteTarget(entityId, targetName);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.snmpV3.service.SnmpV3Service#loadTarget(java.lang.Long)
     */
    @Override
    public List<SnmpTargetTable> loadTarget(Long entityId) {
        return snmpV3Dao.loadTarget(entityId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.snmpV3.service.SnmpV3Service#refreshTarget(java.lang .Long)
     */
    @Override
    public void refreshTarget(Long entityId) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        List<SnmpTargetTable> snmpTargetTables = getSnmpV3Facade(snmpParam.getIpAddress()).refreshTarget(snmpParam);
        snmpV3Dao.batchInsertTarget(entityId, snmpTargetTables);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.snmpV3.service.SnmpV3Service#addTargetParams(com.topvision
     * .ems.snmpV3.facade.domain.SnmpTargetParams)
     */
    @Override
    public void addTargetParams(SnmpTargetParams o) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(o.getEntityId());
        getSnmpV3Facade(snmpParam.getIpAddress()).addTargetParams(snmpParam, o);
        snmpV3Dao.insertTargetParams(o);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.snmpV3.service.SnmpV3Service#modifyTargetParams(com
     * .topvision.ems.snmpV3.facade.domain.SnmpTargetParams)
     */
    @Override
    public void modifyTargetParams(SnmpTargetParams o) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(o.getEntityId());
        getSnmpV3Facade(snmpParam.getIpAddress()).modifyTargetParams(snmpParam, o);
        snmpV3Dao.updateTargetParams(o);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.snmpV3.service.SnmpV3Service#deleteTargetParams(java .lang.Long,
     * java.lang.String)
     */
    @Override
    public void deleteTargetParams(Long entityId, String targetParamsName) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        getSnmpV3Facade(snmpParam.getIpAddress()).deleteTargetParams(snmpParam, targetParamsName);
        snmpV3Dao.deleteTargetParams(entityId, targetParamsName);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.snmpV3.service.SnmpV3Service#loadTargetParams(java. lang.Long)
     */
    @Override
    public List<SnmpTargetParams> loadTargetParams(Long entityId) {
        return snmpV3Dao.loadTargetParams(entityId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.snmpV3.service.SnmpV3Service#refreshTargetParams(java .lang.Long)
     */
    @Override
    public void refreshTargetParams(Long entityId) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        List<SnmpTargetParams> snmpTargetParams = getSnmpV3Facade(snmpParam.getIpAddress()).refreshTargetParams(
                snmpParam);
        snmpV3Dao.batchInsertTargetParams(entityId, snmpTargetParams);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.snmpV3.service.SnmpV3Service#addNotify(com.topvision
     * .ems.snmpV3.facade.domain.SnmpNotifyTable)
     */
    @Override
    public void addNotify(SnmpNotifyTable o) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(o.getEntityId());
        getSnmpV3Facade(snmpParam.getIpAddress()).addNotify(snmpParam, o);
        snmpV3Dao.insertNotify(o);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.snmpV3.service.SnmpV3Service#modifyNotify(com.topvision
     * .ems.snmpV3.facade.domain.SnmpNotifyTable)
     */
    @Override
    public void modifyNotify(SnmpNotifyTable o) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(o.getEntityId());
        getSnmpV3Facade(snmpParam.getIpAddress()).modifyNotify(snmpParam, o);
        snmpV3Dao.updateNotify(o);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.snmpV3.service.SnmpV3Service#delNotify(java.lang.Long,
     * java.lang.String)
     */
    @Override
    public void deleteNotify(Long entityId, String notifyName) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        getSnmpV3Facade(snmpParam.getIpAddress()).deleteNotify(snmpParam, notifyName);
        snmpV3Dao.deleteNotify(entityId, notifyName);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.snmpV3.service.SnmpV3Service#loadNotify(java.lang.Long)
     */
    @Override
    public List<SnmpNotifyTable> loadNotify(Long entityId) {
        return snmpV3Dao.loadNotify(entityId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.snmpV3.service.SnmpV3Service#refreshNotify(java.lang .Long)
     */
    @Override
    public void refreshNotify(Long entityId) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        List<SnmpNotifyTable> snmpNotifyTables = getSnmpV3Facade(snmpParam.getIpAddress()).refreshNotify(snmpParam);
        snmpV3Dao.batchInsertNotify(entityId, snmpNotifyTables);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.snmpV3.service.SnmpV3Service#addNotifyProfile(com.topvision
     * .ems.snmpV3.facade.domain.SnmpNotifyFilterProfile)
     */
    @Override
    public void addNotifyProfile(SnmpNotifyFilterProfile o) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(o.getEntityId());
        getSnmpV3Facade(snmpParam.getIpAddress()).addNotifyProfile(snmpParam, o);
        snmpV3Dao.insertNotifyProfile(o);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.snmpV3.service.SnmpV3Service#modifyNotifyProfile(com
     * .topvision.ems.snmpV3.facade.domain.SnmpNotifyFilterProfile)
     */
    @Override
    public void modifyNotifyProfile(SnmpNotifyFilterProfile o) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(o.getEntityId());
        getSnmpV3Facade(snmpParam.getIpAddress()).modifyNotifyProfile(snmpParam, o);
        snmpV3Dao.updateNotifyProfile(o);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.snmpV3.service.SnmpV3Service#deleteNotifyProfile(com
     * .topvision.ems.snmpV3.facade.domain.SnmpNotifyFilterProfile)
     */
    @Override
    public void deleteNotifyProfile(SnmpNotifyFilterProfile o) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(o.getEntityId());
        getSnmpV3Facade(snmpParam.getIpAddress()).deleteNotifyProfile(snmpParam, o);
        snmpV3Dao.deleteNotifyProfile(o);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.snmpV3.service.SnmpV3Service#loadNotifyProfile(java .lang.Long)
     */
    @Override
    public List<SnmpNotifyFilterProfile> loadNotifyProfile(Long entityId) {
        return snmpV3Dao.loadNotifyFilterProfile(entityId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.snmpV3.service.SnmpV3Service#refreshNotifyProfile(java .lang.Long)
     */
    @Override
    public void refreshNotifyProfile(Long entityId) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        List<SnmpNotifyFilterProfile> snmpNotifyFilterProfiles = getSnmpV3Facade(snmpParam.getIpAddress())
                .refreshNotifyProfile(snmpParam);
        snmpV3Dao.batchInsertNotifyFilterProfile(entityId, snmpNotifyFilterProfiles);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.snmpV3.service.SnmpV3Service#addNotifyFilter(com.topvision
     * .ems.snmpV3.facade.domain.SnmpNotifyTable)
     */
    @Override
    public void addNotifyFilter(SnmpNotifyFilterTable o) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(o.getEntityId());
        getSnmpV3Facade(snmpParam.getIpAddress()).addNotifyFilter(snmpParam, o);
        snmpV3Dao.insertNotifyFilter(o);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.snmpV3.service.SnmpV3Service#modifyNotifyFilter(com
     * .topvision.ems.snmpV3.facade.domain.SnmpNotifyTable)
     */
    @Override
    public void modifyNotifyFilter(SnmpNotifyFilterTable o) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(o.getEntityId());
        getSnmpV3Facade(snmpParam.getIpAddress()).modifyNotifyFilter(snmpParam, o);
        snmpV3Dao.updateNotifyFilter(o);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.snmpV3.service.SnmpV3Service#deleteNotifyFilter(java .lang.Long,
     * java.lang.String)
     */
    @Override
    public void deleteNotifyFilter(Long entityId, String notifyFilterProfileName, String notifyFilterSubtree) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        getSnmpV3Facade(snmpParam.getIpAddress()).deleteNotifyFilter(snmpParam, notifyFilterProfileName,
                notifyFilterSubtree);
        snmpV3Dao.deleteNotifyFilter(entityId, notifyFilterProfileName, notifyFilterSubtree);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.snmpV3.service.SnmpV3Service#loadNotifyFilter(java. lang.Long)
     */
    @Override
    public List<SnmpNotifyFilterTable> loadNotifyFilter(Long entityId) {
        return snmpV3Dao.loadNotifyFilter(entityId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.snmpV3.service.SnmpV3Service#refreshNotifyFilter(java .lang.Long)
     */
    @Override
    public void refreshNotifyFilter(Long entityId) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        List<SnmpNotifyFilterTable> snmpNotifyFilterTables = getSnmpV3Facade(snmpParam.getIpAddress())
                .refreshNotifyFilter(snmpParam);
        snmpV3Dao.batchInsertNotifyFilterTable(entityId, snmpNotifyFilterTables);
    }

    /**
     * @return the messageService
     */
    public MessageService getMessageService() {
        return messageService;
    }

    /**
     * @param messageService
     *            the messageService to set
     */
    public void setMessageService(MessageService messageService) {
        this.messageService = messageService;
    }

}
