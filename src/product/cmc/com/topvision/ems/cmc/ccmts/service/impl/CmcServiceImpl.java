/***********************************************************************
 * $Id: CmcServiceImpl.java,v1.0 2011-10-25 下午04:30:31 $
 * 
 * @author: loyal
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.ccmts.service.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.cmc.ccmts.dao.CmcDao;
import com.topvision.ems.cmc.ccmts.domain.Cmc;
import com.topvision.ems.cmc.ccmts.facade.CmcBfsxFacade;
import com.topvision.ems.cmc.ccmts.facade.CmcFacade;
import com.topvision.ems.cmc.ccmts.facade.domain.CmcAttribute;
import com.topvision.ems.cmc.ccmts.facade.domain.CmcBfsxSnapInfo;
import com.topvision.ems.cmc.ccmts.facade.domain.CmcDevSoftware;
import com.topvision.ems.cmc.ccmts.facade.domain.CmcSysControl;
import com.topvision.ems.cmc.ccmts.service.CmcService;
import com.topvision.ems.cmc.downchannel.domain.CmcFpgaSpecification;
import com.topvision.ems.cmc.exception.SetValueFailException;
import com.topvision.ems.cmc.facade.domain.CmcSystemIpInfo;
import com.topvision.ems.cmc.topology.service.CmcRefreshService;
import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.network.dao.EntityDao;
import com.topvision.ems.network.domain.ModuleParam;
import com.topvision.ems.network.domain.SubDeviceCount;
import com.topvision.ems.network.service.EntityService;
import com.topvision.ems.network.service.TopologyService;
import com.topvision.ems.template.service.EntityTypeService;
import com.topvision.framework.exception.engine.SnmpException;
import com.topvision.framework.service.BaseService;
import com.topvision.framework.snmp.SnmpParam;
import com.topvision.framework.utils.DeviceTypeUtils;
import com.topvision.platform.facade.FacadeFactory;
import com.topvision.platform.message.event.CmcSynchronizedEvent;
import com.topvision.platform.message.event.CmcSynchronizedListener;
import com.topvision.platform.message.event.EntityEvent;
import com.topvision.platform.message.event.EntityListener;
import com.topvision.platform.message.service.MessageService;

/**
 * 基本功能实现
 * 
 * @author loyal
 * @created @2011-10-25-下午04:30:31
 * 
 */
@Service("cmcService")
public class CmcServiceImpl extends BaseService implements CmcService, EntityListener, BeanFactoryAware,
        CmcSynchronizedListener {
    private final Logger logger = LoggerFactory.getLogger(CmcServiceImpl.class);

    @Resource(name = "entityService")
    private EntityService entityService;
    @Autowired
    private EntityTypeService entityTypeService;
    @Autowired
    private CmcDao cmcDao;
    @Resource(name = "entityDao")
    private EntityDao entityDao;
    @Resource(name = "facadeFactory")
    private FacadeFactory facadeFactory;
    @Autowired
    private CmcRefreshService cmcRefreshService;
    @Autowired
    private TopologyService topologyService;
    @Autowired
    private MessageService messageService;
    private SnmpParam snmpParam = new SnmpParam();

    @PreDestroy
    public void destroy() {
        super.destroy();
        messageService.removeListener(CmcSynchronizedListener.class, this);
    }

    @Override
    @PostConstruct
    public void initialize() {
        super.initialize();
        messageService.addListener(CmcSynchronizedListener.class, this);
        ModuleParam moduleParam = new ModuleParam();
        moduleParam.setCos(1001);
        moduleParam.setModuleName("ccmts");
        moduleParam.setBeanName("ccmtsGetTopoFolderNum");
        topologyService.registerTopoFolderNumModuleParam(moduleParam);
    }

    @Override
    public void insertEntityStates(CmcSynchronizedEvent event) {
        if (event.getEntityType().equals(entityTypeService.getCcmtswithagentType())) {
            // 获取CMC基本网络信息
            try {
                refreshCmcSystemIpInfo(event.getEntityId());
                logger.info("topology CmcSystemIpInfo finished!");
            } catch (Exception e) {
                logger.error("get CmcSystemIpInfo info error");
            }
        }
    }

    @Override
    public void refreshCmcSystemIpInfo(Long cmcId) {
        snmpParam = getSnmpParamByEntityId(cmcId);
        CmcSystemIpInfo cmcSystemIpInfo = getCmcFacade(snmpParam.getIpAddress(), CmcFacade.class).getCmcSystemIpInfo(
                snmpParam);

        if (cmcSystemIpInfo != null) {
            cmcSystemIpInfo.setEntityId(cmcId);
            cmcDao.batchInsertCmcSystemIpInfo(cmcSystemIpInfo, cmcId);
        }
    }

    public Long getEntityIdByCmcId(Long cmcId) {
        return cmcDao.getEntityIdByCmcId(cmcId);
    }

    public SnmpParam getSnmpParamByCmcId(Long cmcId) {
        Long entityId = cmcDao.getEntityIdByCmcId(cmcId);
        return entityDao.getSnmpParamByEntityId(entityId);
    }

    public SnmpParam getSnmpParamByEntityId(Long entityId) {
        return entityDao.getSnmpParamByEntityId(entityId);
    }

    public <T> T getCmcFacade(String ip, Class<T> clazz) {
        return facadeFactory.getFacade(ip, clazz);
    }

    @Override
    public CmcAttribute getCmcAttributeByCmcId(Long cmcId) {
        return cmcDao.getCmcAttributeByCmcId(cmcId);
    }

    @Override
    public String obtainCcmtsVersion(Long cmcId) {
        CmcAttribute cmcAttribute = getCmcAttributeByCmcId(cmcId);
        String ccmtsVersion = null;
        Entity entity = entityService.getEntity(cmcId);
        if (entityTypeService.isCcmtsWithoutAgent(entity.getTypeId())) {
            Long entityId = getEntityIdByCmcId(cmcId);
            ccmtsVersion = entityService.getDeviceVersion(entityId);
        } else if (entityTypeService.isCcmtsWithAgent(entity.getTypeId())) {
            ccmtsVersion = cmcAttribute.getDolVersion();
        }
        return ccmtsVersion;
    }

    @Override
    public Long getCmcIdByEntityId(Long entityId) {
        return cmcDao.getCmcIdByEntityId(entityId);
    }

    @Override
    public Integer getCmcTypeByCmcId(Long cmcId) {
        return cmcDao.getCmcTypeByCmcId(cmcId);
    }

    @Override
    public Long getCmcIndexByCmcId(Long cmcId) {
        return cmcDao.getCmcIndexByCmcId(cmcId);
    }

    @Override
    public List<CmcAttribute> getDeviceListItem(Map<String, Object> map) {
        return cmcDao.getDeviceListItem(map);
    }

    @Override
    public Long getCmcIdByOnuId(Long onuId) {
        return cmcDao.getCmcIdByOnuId(onuId);
    }

    @Override
    public Map<Long, String> getCmcNotRelated() {
        return cmcDao.getCmcNotRelated();
    }

    @Override
    public List<Cmc> getCmcList(Long onuId) {
        return cmcDao.getCmcList(onuId);
    }

    @Override
    public void refreshCC(Long entityId, Long cmcId, Integer cmcType) {
        Long cmcIndex = cmcDao.getCmcIndexByCmcId(cmcId);
        entityId = cmcDao.getEntityIdByCmcId(cmcId);
        cmcRefreshService.refreshCC8800A(entityId, cmcIndex, cmcId);
    }

    @Deprecated
    public void discoveryCmcAgain(Long cmcId, Long cmcType) {
    }

    @Override
    public void resetCmcWithoutAgent(Long cmcId) {
        CmcSysControl cmcSysControl = new CmcSysControl();
        cmcSysControl.setIfIndex(cmcDao.getCmcIndexByCmcId(cmcId));
        cmcSysControl.setTopCcmtsSysResetNow(1);
        Long entityId = getEntityIdByCmcId(cmcId);
        snmpParam = getSnmpParamByEntityId(entityId);
        CmcSysControl cmcSysControlAfterModified = getCmcFacade(snmpParam.getIpAddress(), CmcFacade.class)
                .cmcSysControlSetWithotAgent(snmpParam, cmcSysControl);
        if (!cmcSysControlAfterModified.getTopCcmtsSysResetNow().equals(cmcSysControl.getTopCcmtsSysResetNow())) {
            throw new SetValueFailException("cmc.message.cmc.setTopCcmtsSysResetNow");
        }
    }

    @Override
    public CmcDevSoftware updateCmc(CmcDevSoftware cmcDevSoftware) {
        CmcDevSoftware cmcDevSoftwareTemp = null;
        Entity entity = entityService.getEntity(cmcDevSoftware.getCmcId());
        if (entityTypeService.isCcmtsWithoutAgent(entity.getTypeId())) {

        } else if (entityTypeService.isCcmtsWithAgent(entity.getTypeId())) {
            snmpParam = getSnmpParamByCmcId(cmcDevSoftware.getCmcId());
            cmcDevSoftwareTemp = getCmcFacade(snmpParam.getIpAddress(), CmcFacade.class).updateCmc(snmpParam,
                    cmcDevSoftware);
        } else {
            logger.info("something is wrong with cmcType");
        }
        return cmcDevSoftwareTemp;
    }

    @Override
    public Integer getCmcUpdateRecord(Long cmcId) {
        snmpParam = getSnmpParamByCmcId(cmcId);
        CmcFacade cmcFacade = getCmcFacade(snmpParam.getIpAddress(), CmcFacade.class);
        return cmcFacade.getCmcUpdateProgress(snmpParam);
    }

    @Override
    public Integer getCmcUpdateStatus(Long cmcId) {
        int waitTime = 10;
        try {
            Thread.sleep(waitTime * 1000);
        } catch (InterruptedException e1) {
            // 不需要记录，直接进入下一步，开始轮询
        }
        int result = getCmcUpdateRecord(cmcId);
        int countLimit = 10;
        int timeoutCount = 0;
        while (result < 100) {
            try {

                try {
                    Thread.sleep(waitTime * 1000);
                } catch (InterruptedException e) {
                    logger.info("", "file trap receive");
                    result = getCmcUpdateRecord(cmcId);
                    continue;
                }
                result = getCmcUpdateRecord(cmcId);
                timeoutCount = 0;
            } catch (SnmpException e) {
                timeoutCount++;
                if (timeoutCount > countLimit) {
                    result = 300;
                }
                logger.info("", e);
            }
        }
        return result;
    }

    @Override
    public void relateCmcToOnu(Long onuId, Long cmcId) {
        cmcDao.relateCmcToOnu(onuId, cmcId);
    }

    @Override
    public void cmcNoMacBind(Long cmcId) {
        CmcSysControl cmcSysControl = new CmcSysControl();
        cmcSysControl.setIfIndex(cmcDao.getCmcIndexByCmcId(cmcId));
        cmcSysControl.setTopCcmtsSysNoMacBind(1);
        Long entityId = getEntityIdByCmcId(cmcId);
        snmpParam = getSnmpParamByEntityId(entityId);
        CmcSysControl cmcSysControlAfterModified = getCmcFacade(snmpParam.getIpAddress(), CmcFacade.class)
                .cmcSysControlSetWithotAgent(snmpParam, cmcSysControl);
        if (!cmcSysControlAfterModified.getTopCcmtsSysNoMacBind().equals(cmcSysControl.getTopCcmtsSysNoMacBind())) {
            throw new SetValueFailException("cmc.message.cmc.setTopCcmtsSysNoMacBind");
        }

        //解绑定成功后发消息进行删除设备的相关操作
        List<Long> entityIds = new ArrayList<>();
        entityIds.add(cmcId);
        entityService.removeEntity(entityIds);
    }

    @Override
    public Long getOnuIdByCmcId(Long cmcId) {
        return cmcDao.getOnuIdByCmcId(cmcId);

    }

    @Override
    public void renameCc(Long entityId, Long cmcId, String ccName) {
        //modify by lzt 统一使用entityDao 的更新方法
        entityDao.renameEntity(cmcId, ccName);
    }

    @Override
    public boolean isCmcExistsInTopo(Long cmcId, Long folderId) {
        return cmcDao.isCmcExistsInTopo(cmcId, folderId);
    }

    @Override
    public Long moveToTopoFromOnuView(Long cmcId, Entity entity) {
        // 新增entity记录
        Timestamp ts = new Timestamp(System.currentTimeMillis());
        entity.setCreateTime(ts);
        entity.setModifyTime(ts);
        CmcAttribute cmcAttribute = cmcDao.getCmcAttributeByCmcId(cmcId);
        entity.setMac(cmcAttribute.getTopCcmtsSysMacAddr());
        entity.setParentId(cmcId);
        List<Entity> entities = new ArrayList<Entity>();
        entities.add(entity);
        entityDao.txMoveEntity(entities);
        // 更新entity表中ccmts的mac地址
        entityDao.updateEntityMac(cmcId, cmcAttribute.getTopCcmtsSysMacAddr());
        return entity.getEntityId();
    }

    @Override
    public Integer getCmtsChannelUtilizationInterval(Long entityId, Long cmcId) {
        // 判断来自OLT还是CC8800B,有cmcId就是来自CC8800B
        if (cmcId != null) {
            snmpParam = getSnmpParamByCmcId(cmcId);
        } else {
            snmpParam = getSnmpParamByEntityId(entityId);
        }
        try {
            return new Integer(getCmcFacade(snmpParam.getIpAddress(), CmcFacade.class)
                    .getChannelUtilizationInteInterval(snmpParam));
        } catch (Exception e) {
            return 0;
        }
    }

    @Override
    public List<Long> getCmcIdsByEntityId(Long entityId) {
        return cmcDao.getCmcIdsByEntityId(entityId);
    }

    @Override
    public void modifyOnuName(Long onuId, String onuName) {
        cmcDao.modifyOnuName(onuId, onuName);
    }

    @Override
    public List<Long> getCmcIdListByEntityId(Long entityId) {
        return cmcDao.getCmcIdListByEntityId(entityId);
    }

    @Override
    public void deleteTopoEntityByEntityIdAndType(Long type) {
        entityDao.deleteTopoEntityByEntityIdAndType(type);
    }

    @Override
    public CmcBfsxSnapInfo refreshCC8800A(Long upperDeviceId, Long cmcId, Long cmcIndex) {
        SnmpParam snmpParam = this.getSnmpParamByEntityId(upperDeviceId);
        CmcBfsxSnapInfo cmcBaseInfo = new CmcBfsxSnapInfo();
        cmcBaseInfo.setCmcIndex(cmcIndex);
        cmcBaseInfo = this.getCmcFacade(snmpParam.getIpAddress(), CmcBfsxFacade.class).getCC8800ABaseInfo(snmpParam,
                cmcBaseInfo);
        cmcBaseInfo.setEntityId(cmcId);
        //更新cmc基本信息
        cmcDao.updateCmcBaseInfo(cmcBaseInfo);
        entityDao.updateEntityNameAndMac(cmcId, cmcBaseInfo.getCmcSysName(), cmcBaseInfo.getCmcSysMacAddr());
        return cmcBaseInfo;
    }

    @Override
    public void setCmtsChannelUtilizationInterval(Long entityId, Long peroid, SnmpParam snmpParam) {
        getCmcFacade(snmpParam.getIpAddress(), CmcFacade.class).setChannelUtilizationInteInterval(snmpParam, peroid);
    }

    @Override
    public void setBeanFactory(BeanFactory beanfactory) throws BeansException {
    }

    @Override
    public CmcFpgaSpecification getFpgaSpecificationById(Long cmcId) {
        return cmcDao.getFpgaSpecificationById(cmcId);
    }

    @Override
    public SubDeviceCount getSubCountInfo(Long entityId) {
        return cmcDao.querySubCountInfo(entityId);
    }

    public SnmpParam getSnmpParam() {
        return snmpParam;
    }

    public void setSnmpParam(SnmpParam snmpParam) {
        this.snmpParam = snmpParam;
    }

    public EntityService getEntityService() {
        return entityService;
    }

    public void setEntityService(EntityService entityService) {
        this.entityService = entityService;
    }

    public CmcDao getCmcDao() {
        return cmcDao;
    }

    public void setCmcDao(CmcDao cmcDao) {
        this.cmcDao = cmcDao;
    }

    public EntityDao getEntityDao() {
        return entityDao;
    }

    public void setEntityDao(EntityDao entityDao) {
        this.entityDao = entityDao;
    }

    public FacadeFactory getFacadeFactory() {
        return facadeFactory;
    }

    public void setFacadeFactory(FacadeFactory facadeFactory) {
        this.facadeFactory = facadeFactory;
    }

    public CmcRefreshService getCmcRefreshService() {
        return cmcRefreshService;
    }

    public void setCmcRefreshService(CmcRefreshService cmcRefreshService) {
        this.cmcRefreshService = cmcRefreshService;
    }

    @Override
    public void analyseSofewareVersion(CmcAttribute cmc) {
        String sysDesc = cmc.getTopCcmtsSysDescr();
        boolean analyseSuccess = false;
        if (DeviceTypeUtils.isBSR2000(cmc.getCmcDeviceStyle())) {// BSR
            int startIndex = sysDesc.indexOf("SW Version:");
            int endIndex = sysDesc.indexOf(">>");
            if (startIndex > 0 && endIndex > 0) {
                String swVersion = sysDesc.substring(startIndex + 11, endIndex).trim();
                if (swVersion.length() > 0) {
                    cmc.setTopCcmtsSysSwVersion(swVersion);
                    analyseSuccess = true;
                }
            }
        } else if (DeviceTypeUtils.isCASA(cmc.getCmcDeviceStyle())) {// CASA
            int startIndex = sysDesc.indexOf("software release version");
            if (startIndex > 0) {
                String swVersion = sysDesc.substring(startIndex + 24).trim();
                if (swVersion.length() > 0) {
                    cmc.setTopCcmtsSysSwVersion(swVersion);
                    analyseSuccess = true;
                }
            }
        }
        if (!analyseSuccess) {
            cmc.setTopCcmtsSysSwVersion("-");
        }
    }

    @Override
    public void modifyCmcMac(Long onuId, String topCcmtsSysMacAddr) {
        cmcDao.updateCmcMac(onuId, topCcmtsSysMacAddr);
    }

    @Override
    public void updateCmcStatus(Long cmcId, Integer status) {
        cmcDao.updateCmcStatus(cmcId, status);
    }

    @Override
    public List<CmcAttribute> getContainOptDorCmcList() {
        return cmcDao.getContainOptDorCmcList();
    }

    /* (non-Javadoc)
     * @see com.topvision.platform.message.event.EntityListener#entityAdded(com.topvision.platform.message.event.EntityEvent)
     */
    @Override
    public void entityAdded(EntityEvent event) {
    }

    /* (non-Javadoc)
     * @see com.topvision.platform.message.event.EntityListener#entityDiscovered(com.topvision.platform.message.event.EntityEvent)
     */
    @Override
    public void entityDiscovered(EntityEvent event) {
    }

    /* (non-Javadoc)
     * @see com.topvision.platform.message.event.EntityListener#attributeChanged(long, java.lang.String[], java.lang.String[])
     */
    @Override
    public void attributeChanged(long entityId, String[] attrNames, String[] attrValues) {
    }

    /* (non-Javadoc)
     * @see com.topvision.platform.message.event.EntityListener#entityChanged(com.topvision.platform.message.event.EntityEvent)
     */
    @Override
    public void entityChanged(EntityEvent event) {
    }

    /* (non-Javadoc)
     * @see com.topvision.platform.message.event.EntityListener#entityRemoved(com.topvision.platform.message.event.EntityEvent)
     */
    @Override
    public void entityRemoved(EntityEvent event) {

    }

    /* (non-Javadoc)
     * @see com.topvision.platform.message.event.EntityListener#managerChanged(com.topvision.platform.message.event.EntityEvent)
     */
    @Override
    public void managerChanged(EntityEvent event) {
    }

    @Override
    public void entityReplaced(EntityEvent event) {
    }

}