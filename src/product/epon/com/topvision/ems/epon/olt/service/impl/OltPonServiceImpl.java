/***********************************************************************
 * $Id: OltServiceImpl.java,v1.0 2013-10-25 上午10:28:47 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.olt.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.epon.exception.SetValueConflictException;
import com.topvision.ems.epon.olt.dao.OltPonDao;
import com.topvision.ems.epon.olt.domain.OltPonAttribute;
import com.topvision.ems.epon.olt.domain.OltPonStormSuppressionEntry;
import com.topvision.ems.epon.olt.domain.TopPonPortRateLimit;
import com.topvision.ems.epon.olt.domain.TopPonPortSpeedEntry;
import com.topvision.ems.epon.olt.facade.OltPonFacade;
import com.topvision.ems.epon.olt.service.OltPonService;
import com.topvision.ems.epon.onu.dao.OnuDao;
import com.topvision.ems.epon.onu.domain.OltOnuAttribute;
import com.topvision.ems.epon.onu.service.RogueOnuService;
import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.network.service.EntityService;
import com.topvision.ems.template.service.EntityTypeService;
import com.topvision.framework.service.BaseService;
import com.topvision.framework.snmp.SnmpParam;
import com.topvision.framework.utils.EponConstants;
import com.topvision.framework.utils.EponIndex;
import com.topvision.platform.facade.FacadeFactory;
import com.topvision.platform.message.event.SynchronizedEvent;
import com.topvision.platform.message.event.SynchronizedListener;
import com.topvision.platform.message.service.MessageService;

/**
 * @author flack
 * @created @2013-10-25-上午10:28:47
 *
 */
@Service("oltPonService")
public class OltPonServiceImpl extends BaseService implements OltPonService, SynchronizedListener {
    @Autowired
    private OltPonDao oltPonDao;
    @Autowired
    private OnuDao onuDao;
    @Autowired
    private EntityService entityService;
    @Autowired
    private FacadeFactory facadeFactory;
    @Autowired
    private EntityTypeService entityTypeService;
    @Autowired
    private RogueOnuService rogueOnuService;

    @Autowired
    private MessageService messageService;

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

        try {
            refreshOltPonStormInfo(event.getEntityId());
            logger.info("refreshOltPonStormInfo finish");
        } catch (Exception e) {
            logger.error("refreshOltPonStormInfo wrong", e);
        }

        try {
            refreshOltPonPortSpeed(event.getEntityId());
            logger.info("refreshOltPonPortSpeed finish");
        } catch (Exception e) {
            logger.error("refreshOltPonPortSpeed wrong", e);
        }

        try {
            rogueOnuService.refreshPonPortRogueEntry(event.getEntityId());
            logger.info("refreshPonPortRogueEntry finish");
        } catch (Exception e) {
            logger.error("refreshPonPortRogueEntry wrong", e);
        }

    }

    @Override
    public void refreshOltPonStormInfo(Long entityId) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        List<OltPonStormSuppressionEntry> oltPonStormSuppressionEntries = getOltPonFacade(snmpParam.getIpAddress())
                .getOltPonStormSuppressionEntries(snmpParam);
        if (oltPonStormSuppressionEntries != null) {
            for (int i = 0; i < oltPonStormSuppressionEntries.size(); i++) {
                oltPonStormSuppressionEntries.get(i).setEntityId(entityId);
            }
            oltPonDao.batchInsertOltPonStormInfo(oltPonStormSuppressionEntries);
        }
    }

    @Override
    public void refreshOltPonPortSpeed(Long entityId) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        List<TopPonPortSpeedEntry> topPonPortSpeedEntries = getOltPonFacade(snmpParam.getIpAddress()).getPonPortSpeed(
                snmpParam);
        if (topPonPortSpeedEntries != null) {
            for (int i = 0; i < topPonPortSpeedEntries.size(); i++) {
                topPonPortSpeedEntries.get(i).setEntityId(entityId);
                topPonPortSpeedEntries.get(i).setPonIndex(
                        EponIndex.getPonIndex(topPonPortSpeedEntries.get(i).getPonPortSpeedCardIndex(),
                                topPonPortSpeedEntries.get(i).getPonPortSpeedPortIndex()));
                topPonPortSpeedEntries.get(i).setPonId(
                        oltPonDao.getPonIdByPonIndex(entityId, topPonPortSpeedEntries.get(i).getPonIndex()));
            }
            oltPonDao.batchInsertOltPonSpeed(topPonPortSpeedEntries, entityId);
        }
    }

    @Override
    public OltPonAttribute getPonAttribute(Long ponId) {
        return oltPonDao.getPonAttribute(ponId);
    }

    @Override
    public void setPonAdminStatus(Long entityId, Long ponId, Integer adminStatus) {
        Long ponIndex = oltPonDao.getPonIndex(ponId);
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        Integer newStatus = getOltPonFacade(snmpParam.getIpAddress()).setPonAdminStatus(snmpParam, ponIndex,
                adminStatus);
        if (newStatus == null) {
            throw new SetValueConflictException("Business.connection");
        } else {
            oltPonDao.updatePonAdminStatus(ponId, newStatus);
            if (!newStatus.equals(adminStatus)) {
                throw new SetValueConflictException("Business.setPonAdminStatus");
            }
        }
    }

    @Override
    public void setPonIsolationStatus(Long entityId, Long ponId, Integer isolationStatus) {
        Long ponIndex = oltPonDao.getPonIndex(ponId);
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        Integer newStatus = getOltPonFacade(snmpParam.getIpAddress()).setPonIsolationStatus(snmpParam, ponIndex,
                isolationStatus);
        if (newStatus == null) {
            throw new SetValueConflictException("Business.connection");
        } else {
            oltPonDao.updatePonIsolationStatus(ponId, newStatus);
            if (!newStatus.equals(isolationStatus)) {
                throw new SetValueConflictException("Business.setPonIsolationStatus");
            }
        }
    }

    @Override
    public void setPonPortEncryptMode(Long entityId, Long ponId, Integer encryptMode, Integer exchangeTime) {
        Long ponIndex = oltPonDao.getPonIndex(ponId);
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        OltPonAttribute oltPonAttribute = getOltPonFacade(snmpParam.getIpAddress()).setPonPortEncryptMode(snmpParam,
                ponIndex, encryptMode, exchangeTime);
        Integer newEncryptMode = oltPonAttribute.getPonPortEncryptMode();
        Integer newExchangeTime = oltPonAttribute.getPonPortEncryptKeyExchangeTime();
        StringBuilder sBuilder = new StringBuilder();
        if (!newEncryptMode.equals(encryptMode)) {
            sBuilder.append("Business.setPonPortEncryptMode");
        }
        if (!newExchangeTime.equals(exchangeTime)) {
            sBuilder.append("Business.setPonPortEncryptKeyExchangeTime");
        }
        oltPonDao.updatePonPortEncryptMode(ponId, newEncryptMode, newExchangeTime);
        if (sBuilder.length() > 0) {
            throw new SetValueConflictException(sBuilder.toString());
        }
    }

    @Override
    public OltPonStormSuppressionEntry getPonStormSuppression(Long ponId) {
        return oltPonDao.getPonStormSuppression(ponId);
    }

    @Override
    public void setPonMaxLearnMacNum(Long entityId, Long ponId, Long maxLearnMacNum) {
        Long ponIndex = oltPonDao.getPonIndex(ponId);
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        Long newMacNum = getOltPonFacade(snmpParam.getIpAddress()).setPonMaxLearnMacNum(snmpParam, ponIndex,
                maxLearnMacNum);
        if (newMacNum == null) {
            throw new SetValueConflictException("Business.connection");
        } else {
            oltPonDao.updatePonMaxLearnMacNum(ponId, maxLearnMacNum);
            if (!newMacNum.equals(maxLearnMacNum)) {
                throw new SetValueConflictException("Business.setPonMaxLearnMacNum");
            }
        }
    }

    @Override
    public void modifyPonStormInfo(OltPonStormSuppressionEntry oltPonStormSuppressionEntry) {
        Long ponIndex = oltPonDao.getPonIndex(oltPonStormSuppressionEntry.getPonId());
        oltPonStormSuppressionEntry.setPonIndex(ponIndex);
        // 修改设备上PON口广播风暴参数
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(oltPonStormSuppressionEntry.getEntityId());
        OltPonStormSuppressionEntry newStormSuppression = getOltPonFacade(snmpParam.getIpAddress()).setPonStormInfo(
                snmpParam, oltPonStormSuppressionEntry);
        // 更新数据库中PON口广播风暴参数
        OltPonStormSuppressionEntry pss = oltPonDao.getPonStormSuppression(oltPonStormSuppressionEntry.getPonId());
        if (pss != null) {
            oltPonDao.updatePonStormInfo(newStormSuppression);
        } else {
            oltPonDao.insertPonStormInfo(newStormSuppression);
        }
    }

    @Override
    public void refreshOltPonStormSuppressionEntry(Long entityId) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        List<OltPonStormSuppressionEntry> oltPonStormSuppressionEntries = getOltPonFacade(snmpParam.getIpAddress())
                .getOltPonStormSuppressionEntries(snmpParam);
        if (oltPonStormSuppressionEntries != null) {
            for (int i = 0; i < oltPonStormSuppressionEntries.size(); i++) {
                oltPonStormSuppressionEntries.get(i).setEntityId(entityId);
            }
            oltPonDao.batchInsertOltPonStormInfo(oltPonStormSuppressionEntries);
        }
    }

    @Override
    public void setPon15MinPerfStatus(Long entityId, Long ponId, Integer perfStatus) {
        Long ponIndex = oltPonDao.getPonIndex(ponId);
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        Integer newStatus = getOltPonFacade(snmpParam.getIpAddress()).setPon15MinPerfStatus(snmpParam, ponIndex,
                perfStatus);
        if (newStatus == null) {
            throw new SetValueConflictException("Business.connection");
        } else {
            oltPonDao.updatePon15MinPerfStatus(ponId, newStatus);
            //Long index = oltDao.getPonIndex(ponId);
            //modifyOltCollectors(entityId, index, perfStatus, 1);
            if (!newStatus.equals(perfStatus)) {
                throw new SetValueConflictException("Business.setPon15MinPerfStatus");
            }
        }
    }

    @Override
    public void updateEntityPon15MinPerfStatus(Long entityId, List<OltPonAttribute> ponAttributes) {
        for (OltPonAttribute ponPort : ponAttributes) {
            setPon15MinPerfStatus(entityId, ponPort.getPonId(), 1);
        }
    }

    @Override
    public void setPon24HourPerfStatus(Long entityId, Long ponId, Integer perfStatus) {
        Long ponIndex = oltPonDao.getPonIndex(ponId);
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        Integer newStatus = getOltPonFacade(snmpParam.getIpAddress()).setPon24HourPerfStatus(snmpParam, ponIndex,
                perfStatus);
        //Long index = oltDao.getPonIndex(ponId);
        //modifyOltCollectors(entityId, index, perfStatus, 2);
        if (newStatus == null) {
            throw new SetValueConflictException("Business.connection");
        } else {
            oltPonDao.updatePon24HourPerfStatus(ponId, newStatus);
            if (!newStatus.equals(perfStatus)) {
                throw new SetValueConflictException("Business.setPon24HourPerfStatus");
            }
        }
    }

    @Override
    public void setPonPortRateLimit(TopPonPortRateLimit topPonPortRateLimit) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(topPonPortRateLimit.getEntityId());
        getOltPonFacade(snmpParam.getIpAddress()).setPonRateLimit(snmpParam, topPonPortRateLimit);
        oltPonDao.updatePonRateLimit(topPonPortRateLimit.getPonId(), topPonPortRateLimit.getPonPortUpRateLmt(),
                topPonPortRateLimit.getPonPortDownRateLmt());
    }

    private OltPonFacade getOltPonFacade(String ip) {
        return facadeFactory.getFacade(ip, OltPonFacade.class);
    }

    @Override
    public Long getPonIndex(Long id) {
        return oltPonDao.getPonIndex(id);
    }

    @Override
    public TopPonPortSpeedEntry getPonPortSpeedMode(Long entityId, Long ponId) {
        return oltPonDao.getPonPortSpeedMode(entityId, ponId);
    }

    @Override
    public void modifyPonPortSpeedMode(Long entityId, Long ponId, Integer speedMode) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        TopPonPortSpeedEntry ponPortSpeedEntry = oltPonDao.getPonPortSpeedMode(entityId, ponId);
        ponPortSpeedEntry.setPonPortSpeedMod(speedMode);
        ponPortSpeedEntry.setPonPortSpeedPortIndex(EponIndex.getPonNo(ponPortSpeedEntry.getPonIndex()).intValue());
        getOltPonFacade(snmpParam.getIpAddress()).setPonPortSpeedMode(snmpParam, ponPortSpeedEntry);
        oltPonDao.modifyPonPortSpeedMode(entityId, ponId, speedMode);
    }

    @Override
    public Long getPonIdByIndex(Long entityId, Long ponIndex) {
        return oltPonDao.getPonIdByPonIndex(entityId, ponIndex);
    }

    @Override
    public List<Long> getAllPonIndex(Long entityId) {
        return oltPonDao.getAllPonIndex(entityId);
    }

    @Override
    public List<Long> getAllEponIndex(Long entityId) {
        return oltPonDao.getAllEponIndex(entityId);
    }

    @Override
    public List<OltOnuAttribute> getOnuAttributeByPonIndexs(Long entityId, List<Long> ponIndexList, Integer onuType) {
        List<OltOnuAttribute> onuAttributes = oltPonDao.getOnuAttributeByPonIndexs(entityId, ponIndexList, onuType);
        List<OltOnuAttribute> onuList = new ArrayList<OltOnuAttribute>();
        // 过滤不在线的CC和ONU，只需要提供在线的CC和ONU
        for (OltOnuAttribute onuAttribute : onuAttributes) {
            // 241表示CC8800A/C-A 4表示在线
            Entity entity = entityService.getEntity(onuAttribute.getOnuId());
            onuAttribute.setOnuUniqueIdentification(onuAttribute.getOnuUniqueIdentification().replaceAll(":", ""));
            onuAttribute.setTypeId(entity.getTypeId());
            //if (onuAttribute.getOnuPreType().equals(OltOnuAttribute.ONU_TYPE_CCMTS)) {
            if (entityTypeService.isCcmtsWithoutAgent(entity.getTypeId())) {
                Integer status = onuDao.selectOltCcmtsStatus(onuAttribute.getOnuId());
                if (status.equals(4)) {
                    onuList.add(onuAttribute);
                }
            } else {
                if (onuAttribute.getOnuOperationStatus().equals(1)) {
                    onuList.add(onuAttribute);
                }
            }
        }
        return onuList;
    }

    @Override
    public List<OltPonAttribute> getPonPortList(Map<String, Object> map) {
        return oltPonDao.getPonPortList(map);
    }

    @Override
    public void updatePonPort15MinStatus(Long entityId) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        List<OltPonAttribute> ponAttributes = getOltPonFacade(snmpParam.getIpAddress()).getPonListAttribute(snmpParam);
        for (OltPonAttribute ponAttr : ponAttributes) {
            ponAttr.setEntityId(entityId);
        }
        oltPonDao.batchUpdatePon15MinStatus(ponAttributes);
    }

    @Override
    public void updatePon15PerfStatus(Long entityId, Long ponIndex, Integer status) {
        long ponId = oltPonDao.getPonIdByPonIndex(entityId, ponIndex);
        oltPonDao.updatePon15MinPerfStatus(ponId, status);
    }

    @Override
    public void resetPon(Long entityId, Long ponId) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        Long ponIndex = oltPonDao.getPonIndex(ponId);
        OltPonFacade oltPonFacade = getOltPonFacade(snmpParam.getIpAddress());
        oltPonFacade.setPonAdminStatus(snmpParam, ponIndex, EponConstants.PORT_STATUS_DOWN);
        try {
            Thread.sleep(5000l);
        } catch (InterruptedException e) {
            logger.trace("", e);
        }
        oltPonFacade.setPonAdminStatus(snmpParam, ponIndex, EponConstants.PORT_STATUS_UP);
    }

    @Override
    public void refreshAllPonStatus(Long entityId) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        OltPonFacade oltPonFacade = getOltPonFacade(snmpParam.getIpAddress());
        List<OltPonAttribute> oltPonAttributes = oltPonFacade.getPonListAttribute(snmpParam);
        for (OltPonAttribute ponAttribute : oltPonAttributes) {
            ponAttribute.setEntityId(entityId);
            Long ponId = oltPonDao.getPonIdByPonIndex(entityId, ponAttribute.getPonIndex());
            oltPonDao.updatePonOperationStatus(ponId, ponAttribute.getPonOperationStatus());
            oltPonDao.updatePonAdminStatus(ponId, ponAttribute.getPonPortAdminStatus());
        }
    }

    @Override
    public OltPonAttribute refreshPonStatus(Long entityId, Long ponIndex) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        OltPonFacade oltPonFacade = getOltPonFacade(snmpParam.getIpAddress());
        OltPonAttribute ponAttribute = oltPonFacade.getPonAttribute(snmpParam, ponIndex);
        if (ponAttribute != null) {
            Long ponId = oltPonDao.getPonIdByPonIndex(entityId, ponAttribute.getPonIndex());
            oltPonDao.updatePonOperationStatus(ponId, ponAttribute.getPonOperationStatus());
            oltPonDao.updatePonAdminStatus(ponId, ponAttribute.getPonPortAdminStatus());
        }
        return ponAttribute;
    }

    @Override
    public void updateEntityStates(SynchronizedEvent event) {
    }

    @Override
    public List<OltPonAttribute> getOltPonList(Long entityId) {
        return oltPonDao.getPonListByEntityId(entityId);
    }

    @Override
    public OltPonAttribute loadPonPortLmt(Long entityId, Long ponId) {
        Long ponIndex = oltPonDao.getPonIndex(ponId);
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        OltPonAttribute ponAttribute = getOltPonFacade(snmpParam.getIpAddress()).getPonAttribute(snmpParam, ponIndex);
        if (ponAttribute != null) {
            oltPonDao.updatePonRateLimit(ponId, ponAttribute.getMaxUsBandwidth(), ponAttribute.getMaxDsBandwidth());
        }
        return ponAttribute;
    }
}
