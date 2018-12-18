package com.topvision.ems.epon.ofa.service.impl;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.epon.ofa.dao.OfaBasicInfoDao;
import com.topvision.ems.epon.ofa.facade.OfaBasicInfoFacade;
import com.topvision.ems.epon.ofa.facade.domain.OfaBasicInfo;
import com.topvision.ems.epon.ofa.service.OfaBasicInfoService;
import com.topvision.ems.network.service.EntityService;
import com.topvision.framework.service.BaseService;
import com.topvision.framework.snmp.SnmpParam;
import com.topvision.platform.facade.FacadeFactory;
import com.topvision.platform.message.event.SynchronizedEvent;
import com.topvision.platform.message.event.SynchronizedListener;
import com.topvision.platform.message.service.MessageService;
import com.topvision.platform.zetaframework.var.UnitConfigConstant;

/**
 * 
 * @author CWQ
 * @created @2017年10月13日-下午7:37:59
 *
 */
@Service("ofaBasicInfoService")
public class OfaBasicInfoServiceImpl extends BaseService implements OfaBasicInfoService, SynchronizedListener {

    @Autowired
    private OfaBasicInfoDao ofaBasicInfoDao;
    @Autowired
    private EntityService entityService;
    @Autowired
    private FacadeFactory facadeFactory;
    @Autowired
    private MessageService messageService;

    @Override
    public OfaBasicInfo getOfaBasicInfo(Long entityId) {
        OfaBasicInfo ofaBasicInfo = ofaBasicInfoDao.getOfaBasicInfoById(entityId);
        return formatOfaBasicInfoTemperature(ofaBasicInfo);
    }

    @Override
    public void modifyOfaBasicInfo(OfaBasicInfo ofaBasicInfo, Long entityId) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        OfaBasicInfo newOfaBasicInfo = getOfaBasicInfoFacade(snmpParam.getIpAddress()).modifyOfaBasicInfo(snmpParam,
                ofaBasicInfo);
        newOfaBasicInfo.setEntityId(entityId);
        ofaBasicInfoDao.updateOfaBasicInfo(newOfaBasicInfo);
    }

    @Override
    public void refreshOfaBasicInfo(Long entityId) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        List<OfaBasicInfo> OfaBasicInfos = getOfaBasicInfoFacade(snmpParam.getIpAddress()).getOfaBasicInfo(snmpParam);
        for (OfaBasicInfo ofaBasicInfo : OfaBasicInfos) {
            ofaBasicInfo.setEntityId(entityId);
        }
        ofaBasicInfoDao.batchInsertOrUpdateOfaBasicInfo(OfaBasicInfos);
    }

    public OfaBasicInfoFacade getOfaBasicInfoFacade(String ip) {
        return facadeFactory.getFacade(ip, OfaBasicInfoFacade.class);
    }

    /**
     * 对OfaBasicInfoEntry中的温度属性做转换
     * 
     * @param ofaBasicInfoEntry
     * @return
     */
    private OfaBasicInfo formatOfaBasicInfoTemperature(OfaBasicInfo ofaBasicInfoEntry) {
        if (ofaBasicInfoEntry == null) {
            logger.info("ofaBasicInfoEntry is null");
            return ofaBasicInfoEntry;
        }
        String tempUnit = (String) UnitConfigConstant.get(UnitConfigConstant.TEMP_UNIT);
        if (UnitConfigConstant.FAHR_TEMP_UNIT.equalsIgnoreCase(tempUnit)) {// 用户设置为华氏度
            if (ofaBasicInfoEntry.getPump1Temp() != null) {
                ofaBasicInfoEntry
                        .setPump1Temp(UnitConfigConstant.translateTemperature(ofaBasicInfoEntry.getPump1Temp() / 10f) * 10);
            }
            if (ofaBasicInfoEntry.getPump2Temp() != null) {
                ofaBasicInfoEntry
                        .setPump2Temp(UnitConfigConstant.translateTemperature(ofaBasicInfoEntry.getPump2Temp() / 10f) * 10);
            }
            if (ofaBasicInfoEntry.getSystemTemp() != null) {
                ofaBasicInfoEntry.setSystemTemp(UnitConfigConstant.translateTemperature(ofaBasicInfoEntry
                        .getSystemTemp() / 10f) * 10);
            }
        }
        return ofaBasicInfoEntry;
    }

    @Override
    public void insertEntityStates(SynchronizedEvent event) {
        try {
            refreshOfaBasicInfo(event.getEntityId());
            logger.info("refreshOfaBasicInfo finish");
        } catch (Exception e) {
            logger.error("refreshOfaBasicInfo wrong", e);
        }
    }

    @Override
    public void updateEntityStates(SynchronizedEvent event) {
    }

    @PostConstruct
    @Override
    public void initialize() {
        super.initialize();
        messageService.addListener(SynchronizedListener.class, this);
    }

    @PreDestroy
    @Override
    public void destroy() {
        super.destroy();
        messageService.removeListener(SynchronizedListener.class, this);
    }
}
