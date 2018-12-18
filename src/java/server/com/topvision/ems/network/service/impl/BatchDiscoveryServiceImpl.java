/***********************************************************************
 * $Id: BatchDiscoveryServiceImpl.java,v1.0 2012-10-31 下午04:55:06 $
 * 
 * @author: Rod John
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.network.service.impl;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.facade.callback.BatchDiscoveryCallBack;
import com.topvision.ems.facade.domain.BatchDiscoveryInfo;
import com.topvision.ems.facade.domain.DiscoveryData;
import com.topvision.ems.facade.domain.DwrInfo;
import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.facade.domain.EntityType;
import com.topvision.ems.facade.domain.TopoHandlerProperty;
import com.topvision.ems.facade.network.BatchDiscoveryFacade;
import com.topvision.ems.message.Message;
import com.topvision.ems.message.MessagePusher;
import com.topvision.ems.network.dao.BatchAutoDiscoveryDao;
import com.topvision.ems.network.dao.EntityAddressDao;
import com.topvision.ems.network.dao.EntityDao;
import com.topvision.ems.network.domain.BatchAutoDiscoveryIps;
import com.topvision.ems.network.domain.EntityAddress;
import com.topvision.ems.network.domain.EntitySnap;
import com.topvision.ems.network.parser.DefaultTopologyHandle;
import com.topvision.ems.network.parser.TopologyHandle;
import com.topvision.ems.network.service.BatchDiscoveryService;
import com.topvision.ems.network.service.DiscoveryService;
import com.topvision.ems.network.service.EntityService;
import com.topvision.ems.network.service.TopologyService;
import com.topvision.ems.template.service.EntityTypeService;
import com.topvision.framework.service.BaseService;
import com.topvision.framework.snmp.SnmpParam;
import com.topvision.platform.domain.UserContext;
import com.topvision.platform.facade.FacadeFactory;
import com.topvision.platform.service.SystemPreferencesService;
import com.topvision.platform.util.StringUtil;

import net.sf.json.JSONObject;

/**
 * @author RodJohn
 * @created @2012-10-31-下午04:55:06
 * 
 */
@Service("batchDiscoveryService")
public class BatchDiscoveryServiceImpl extends BaseService implements BatchDiscoveryService, BatchDiscoveryCallBack {
    private static Logger logger = LoggerFactory.getLogger(BatchDiscoveryServiceImpl.class);
    @Autowired
    private FacadeFactory facadeFactory;
    @Autowired
    private EntityDao entityDao;
    @Autowired
    private EntityAddressDao entityAddressDao;
    @Autowired
    private EntityService entityService;
    @Autowired
    private EntityTypeService entityTypeService;
    @Autowired
    private MessagePusher messagePusher;
    @Autowired
    private DiscoveryService<DiscoveryData> discoveryService;
    // private Map<EngineServer, List<String>> eMap = new HashMap<EngineServer, List<String>>();
    private AtomicBoolean currentTopoFlag = new AtomicBoolean(false);
    private Map<String, TopologyHandle> handleMap;
    @Autowired
    private TopologyService topologyService;
    @Autowired
    private SystemPreferencesService systemPreferencesService;
    @Autowired
    private BatchAutoDiscoveryDao batchAutoDiscoveryDao;

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.framework.service.BaseService#destroy()
     */
    @Override
    @PreDestroy
    public void destroy() {
        super.destroy();
        handleMap = null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.framework.service.BaseService#initialize()
     */
    @Override
    @PostConstruct
    public void initialize() {
        super.initialize();
        handleMap = new HashMap<String, TopologyHandle>();
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

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.service.BatchDiscoveryService#batchDiscovery(java.util.List,
     * java.util.List, java.util.List, java.lang.String, java.lang.String)
     */
    @Override
    public void batchDiscovery(List<Long> ids, String dwrId, String jconnectID, List<String> ips,
            List<String> entityNames, List<String> topoNames, List<SnmpParam> snmpParams, List<String> types,
            UserContext userContext) {
        topoLock();
        Integer pingTimeOut = Integer.parseInt(
                systemPreferencesService.getModulePreferences("Ping").getProperty("Ping.batchtopo.timeout", "4000"));
        Integer pingCount = Integer
                .parseInt(systemPreferencesService.getModulePreferences("Ping").getProperty("Ping.count", "1"));
        Integer pingRetry = Integer
                .parseInt(systemPreferencesService.getModulePreferences("Ping").getProperty("Ping.retry", "0"));
        /*
         * Map<EngineServer, List<String>> eMap = new HashMap<EngineServer, List<String>>(); eMap =
         * facadeFactory.getEngineServer(ips); for (Map.Entry<EngineServer, List<String>> entry :
         * eMap.entrySet()) { if (entry.getKey().getIp() == null ||
         * entry.getKey().getIp().equals("127.0.0.1") || entry.getKey().getIp().equals("localhost"))
         * { facadeFactory.getFacade(entry.getKey(),
         * BatchDiscoveryFacade.class).batchDiscovery(entry.getValue(), snmpParams, types,
         * callBackURL.toString(), new DwrInfo(dwrId, sessionId)); } }
         */
        // TODO 修改为注释的代码，需要根据IP获得对应的EngineServer
        List<EntityType> allSysObjectId = entityTypeService.getAllEntityTypeWithLicenseSupport();

        facadeFactory.getDefaultFacade(BatchDiscoveryFacade.class).batchDiscovery(ips, entityNames, topoNames,
                snmpParams, allSysObjectId, types,
                new DwrInfo(dwrId, jconnectID, userContext.getUserId(), ids.size() == 0 ? null : ids.get(0)), pingCount,
                pingTimeOut, pingRetry);
    }

    private String refresh(BatchDiscoveryInfo option) {
        // 刷新设备结构数据
        Entity entity = packageEntity(option);

        // Add by Rod On Authority
        // 判断License限制设备数量
        Long licenseGroupType = entityTypeService.getEntityLicenseGroupIdByEntityTypeId(entity.getTypeId());
        if (!entityService.checkEntityLimitInLicense(licenseGroupType)) {
            entity.setTopoInfo("LicenseLimit");
            pushBatchTopoMessage(entity, option.getDwrInfo());
            return "LicenseLimit";
        }
        entity.setAuthorityUserId(option.getDwrInfo().getUserId());

        // modify by fanzidong, 根据不同的设备唯一性，有不同的判断逻辑(只针对带agent的CCMTS)
        // ADD by RodJohn
        TopologyHandle handler = getTopologyHandle(option.getSysInfo()[0]);
        if (handler != null) {
            String handleResult = handler.handleTopoResult(option, entity);
            if (handleResult != TopologyHandle.SUCCESS) {
                entity.setIp(option.getIpAddress());
                entity.setTopoInfo(handleResult);
                pushBatchTopoMessage(entity, option.getDwrInfo());
                return handleResult;
            }
        }

        // 流程走到此处，代表该设备可以正常加入网管
        entityDao.insertEntity(entity);
        try {
            if (option.getTopoName() != null) {
                List<Long> folderIds = topologyService.getFolderIdByName(option.getTopoName());
                if (folderIds != null && folderIds.size() > 0) {
                    entityDao.moveEntityForBatchDiscovery(entity.getEntityId(), folderIds);
                }
            }
        } catch (Exception e) {
            logger.error("moveEntityForBatchDiscovery errer:", e);
        }
        // insert address
        EntityAddress ea = new EntityAddress();
        ea.setEntityId(entity.getEntityId());
        ea.setIp(entity.getIp());
        entityAddressDao.insertEntity(ea);
        // insert entitySnap
        EntitySnap entitySnap = new EntitySnap();
        // 初始化的时候默认连通
        entitySnap.setState(true);
        entitySnap.setEntityId(entity.getEntityId());
        entityDao.insertEntitySnap(entitySnap);
        // 前台推送
        entity.setTopoInfo("EntityTopoSuccess");
        pushBatchTopoMessage(entity, option.getDwrInfo());
        // 采集数据 此处不能采用同步方式
        // discoveryService.refresh(entity.getEntityId());
        entityService.txCreateMessage(entity);
        return "success";

    }

    private Entity packageEntity(BatchDiscoveryInfo option) {
        Entity entity = new Entity();
        Timestamp st = new Timestamp(System.currentTimeMillis());
        entity.setIp(option.getIpAddress());
        entity.setCreateTime(st);
        entity.setModifyTime(st);
        // 设置设备的SNMP参数
        entity.setParam(option.getSnmpParam());
        // 设置其默认为可管理的。
        entity.setStatus(Boolean.TRUE);
        entity.setSysObjectID(option.getSysInfo()[0]);
        entity.setSysName(option.getSysInfo()[1]);
        if (option.getEntityName() == null || "".equals(option.getEntityName().trim())) {
            String sysname = option.getSysInfo()[1];
            sysname = StringUtil.stripSpecialChar(sysname);
            if (sysname.length() > 63) {
                entity.setName(sysname.substring(0, 63));
            } else {
                entity.setName(sysname);
            }
        } else {
            entity.setName(option.getEntityName());
        }
        EntityType type = entityTypeService.getEntityTypeBySysObjId(entity.getSysObjectID());
        entity.setTypeId(type.getTypeId());
        option.setTypeId(type.getTypeId());
        entity.setTypeName(type.getDisplayName());

        // 此处针对带agent的CC设置mac
        /*
         * if(entityTypeService.isCcmtsWithAgent(type.getTypeId())) {
         * entity.setMac((String)option.getProductInfo()); }
         */

        return entity;
    }

    /**
     * Push Message to BatchTopo Entity
     * 
     * @param entity
     */
    private void pushBatchTopoMessage(Entity entity, DwrInfo dwrInfo) {
        Message message = new Message("scanOption");
        message.setId(dwrInfo.getDwrId());
        message.setJconnectID(dwrInfo.getJconnectID());
        message.setData(JSONObject.fromObject(entity));
        messagePusher.sendMessage(message);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.network.service.BatchDiscoveryCallBack#discoveryExporter(com.topvision.
     * ems.network.domain.ScanOptions)
     */
    @Override
    public String discoveryExporter(BatchDiscoveryInfo option) {
        try {
            String result = refresh(option);
            return result;
        } catch (Exception e) {
            logger.info("discovery entity:" + option.getIpAddress(), e);
            return "discoveryException";
        }
    }

    /**
     * @return the entityDao
     */
    public EntityDao getEntityDao() {
        return entityDao;
    }

    /**
     * @param entityDao
     *            the entityDao to set
     */
    public void setEntityDao(EntityDao entityDao) {
        this.entityDao = entityDao;
    }

    /**
     * @return the entityAddressDao
     */
    public EntityAddressDao getEntityAddressDao() {
        return entityAddressDao;
    }

    /**
     * @param entityAddressDao
     *            the entityAddressDao to set
     */
    public void setEntityAddressDao(EntityAddressDao entityAddressDao) {
        this.entityAddressDao = entityAddressDao;
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

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.service.BatchDiscoveryCallBack#discoveryComplete()
     */
    @Override
    public void discoveryComplete(DwrInfo dwrInfo) {
        // 通知页面拓扑过程结束
        try {
            Message message = new Message("scanFinish");
            message.setId(dwrInfo.getDwrId());
            message.setJconnectID(dwrInfo.getJconnectID());
            messagePusher.sendMessage(message);
            // 自动扫描的时候更新数据库完成时间
            Long segmentIdLong = dwrInfo.getSegmentId();
            BatchAutoDiscoveryIps tmp = new BatchAutoDiscoveryIps();
            tmp.setId(segmentIdLong);
            tmp.setLastDiscoveryTime(new Timestamp(System.currentTimeMillis()));
            batchAutoDiscoveryDao.updateLastAutoDiscoveryTime(tmp);
        } catch (Exception e) {
            logger.info("discoveryComplete" + e);
        } finally {
            topoUnlock();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.service.BatchDiscoveryService#shutDownBatchDiscovery()
     */
    @Override
    public void shutDownBatchDiscovery() {
        // 页面点击结束动作,此处需要通知所有的采集端停止采集过程
        /*
         * for (Map.Entry<EngineServer, List<String>> entry : eMap.entrySet()) { if
         * (entry.getKey().getIp() == null || entry.getKey().getIp().equals("127.0.0.1") ||
         * entry.getKey().getIp().equals("localhost")) { facadeFactory.getFacade(entry.getKey(),
         * BatchDiscoveryFacade.class).shutDownDiscovery(); }
         * //facadeFactory.getFacade(entry.getKey(),
         * BatchDiscoveryFacade.class).batchDiscovery(entry.getValue(), snmpParams, types,
         * callBackURL.toString()); }
         */
        // TODO
        facadeFactory.getDefaultFacade(BatchDiscoveryFacade.class).shutDownDiscovery();
    }

    /**
     * @return the messagePusher
     */
    public MessagePusher getMessagePusher() {
        return messagePusher;
    }

    /**
     * @param messagePusher
     *            the messagePusher to set
     */
    public void setMessagePusher(MessagePusher messagePusher) {
        this.messagePusher = messagePusher;
    }

    /**
     * @return the discoveryService
     */
    public DiscoveryService<DiscoveryData> getDiscoveryService() {
        return discoveryService;
    }

    /**
     * @param discoveryService
     *            the discoveryService to set
     */
    public void setDiscoveryService(DiscoveryService<DiscoveryData> discoveryService) {
        this.discoveryService = discoveryService;
    }

    /**
     * 
     * 打开拓扑的开关标志
     * 
     */
    private void topoLock() {
        currentTopoFlag.set(true);
    }

    /**
     * 
     * 关闭设备采集的接口方法
     * 
     */
    private void topoUnlock() {
        currentTopoFlag.set(false);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.service.BatchDiscoveryService#getTopoFlag()
     */
    @Override
    public AtomicBoolean getTopoFlag() {
        return currentTopoFlag;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.network.service.BatchDiscoveryService#registerTopoHandler(java.lang.String,
     * com.topvision.ems.network.parser.TopologyHandle)
     */
    @Override
    public void registerTopoHandler(String sysOid, TopologyHandle handle) {
        if (!handleMap.containsKey(sysOid)) {
            handleMap.put(sysOid, handle);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.service.BatchDiscoveryService#unregisterTopoHandler(java.lang.
     * String )
     */
    @Override
    public void unregisterTopoHandler(String sysOid) {
        if (handleMap.containsKey(sysOid)) {
            handleMap.remove(sysOid);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.facade.callback.BatchDiscoveryCallBack#searchProductNeedInfo(java.lang.
     * String)
     */
    @Override
    public TopoHandlerProperty searchProductTopoInfo(String sysOid) {
        if (handleMap.containsKey(sysOid)) {
            return handleMap.get(sysOid).getTopoInfo();
        }
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.network.service.BatchDiscoveryService#getTopologyHandle(java.lang.String)
     */
    @Override
    public TopologyHandle getTopologyHandle(String sysOid) {
        if (handleMap.containsKey(sysOid)) {
            return handleMap.get(sysOid);
        } else {
            return handleMap.get(DefaultTopologyHandle.DEFAULT);
        }
    }

}
