package com.topvision.ems.epon.ofa.service.impl;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.epon.ofa.dao.OfaAlarmThresholdDao;
import com.topvision.ems.epon.ofa.facade.OfaAlarmThresholdFacade;
import com.topvision.ems.epon.ofa.facade.domain.OfaAlarmThreshold;
import com.topvision.ems.epon.ofa.service.OfaAlarmThresholdService;
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
 * @author w1992wishes
 * @created @2017年10月13日-上午11:01:29
 *
 */
@Service("ofaAlarmThresholdService")
public class OfaAlarmThresholdServiceImpl extends BaseService implements OfaAlarmThresholdService, SynchronizedListener {

    @Autowired
    private OfaAlarmThresholdDao ofaAlarmThresholdDao;
    @Autowired
    private FacadeFactory facadeFactory;
    @Autowired
    private EntityService entityService;
    @Autowired
    private MessageService messageService;

    @PreDestroy
    @Override
    public void destroy() {
        super.destroy();
        messageService.removeListener(SynchronizedListener.class, this);
    }

    @PostConstruct
    @Override
    public void initialize() {
        super.initialize();
        messageService.addListener(SynchronizedListener.class, this);
    }

    @Override
    public OfaAlarmThreshold modifyOfaAlarmThreshold(OfaAlarmThreshold ofaAlarmThreshold) {
        ofaAlarmThreshold = formatTempToSet(ofaAlarmThreshold);
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(ofaAlarmThreshold.getEntityId());
        OfaAlarmThreshold newAlarmThreshold = getOfaAlarmThresholdFacade(snmpParam.getIpAddress())
                .modifyOfaAlarmThreshold(snmpParam, ofaAlarmThreshold);
        newAlarmThreshold.setEntityId(ofaAlarmThreshold.getEntityId());
        ofaAlarmThresholdDao.insertOrUpdateOfaAlarm(newAlarmThreshold);
        return newAlarmThreshold;
    }

    @Override
    public OfaAlarmThreshold getOfaAlarmThresholdById(Long entityId) {
        return formatTempToShow(ofaAlarmThresholdDao.getOfaAlarmThresholdById(entityId));
    }

    @Override
    public List<OfaAlarmThreshold> fetchOfaAlarmThreshold(Long entityId) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        List<OfaAlarmThreshold> ofaAlarmThresholds = getOfaAlarmThresholdFacade(snmpParam.getIpAddress())
                .getOfaAlarmThreshold(snmpParam);
        for (OfaAlarmThreshold ofaAlarmThreshold : ofaAlarmThresholds) {
            ofaAlarmThreshold.setEntityId(entityId);
        }
        ofaAlarmThresholdDao.batchInsertOrUpdateOfaAlarm(ofaAlarmThresholds);
        return ofaAlarmThresholds;
    }

    public OfaAlarmThresholdFacade getOfaAlarmThresholdFacade(String ip) {
        return facadeFactory.getFacade(ip, OfaAlarmThresholdFacade.class);
    }

    @Override
    public void insertEntityStates(SynchronizedEvent event) {
        try {
            fetchOfaAlarmThreshold(event.getEntityId());
            logger.info("fetchOfaAlarmThreshold finish");
        } catch (Exception e) {
            logger.error("fetchOfaAlarmThreshold wrong", e);
        }
    }

    @Override
    public void updateEntityStates(SynchronizedEvent event) {
    }

    /**
     * 根据用户选定的温度单位格式化温度用于页面显示
     * 
     * @param ofaAlarmThreshold
     * @return
     */
    private OfaAlarmThreshold formatTempToShow(OfaAlarmThreshold ofaAlarmThreshold) {
        if (ofaAlarmThreshold == null) {
            return null;
        }
        String tempUnit = (String) UnitConfigConstant.get(UnitConfigConstant.TEMP_UNIT);
        if (UnitConfigConstant.FAHR_TEMP_UNIT.equalsIgnoreCase(tempUnit)) {// 用户设置为华氏度
            if (ofaAlarmThreshold.getPump1TempAlarmUp() != null) {
                // 因为设备获取的摄氏温度是整数，单位是0.1℃，所以转华氏度需要除于10，转换后再乘以10，同前台单位一致
                ofaAlarmThreshold.setPump1TempAlarmUp(UnitConfigConstant.translateTemperature(ofaAlarmThreshold
                        .getPump1TempAlarmUp() / 10) * 10);
            }
            if (ofaAlarmThreshold.getPump1TempAlarmLow() != null) {
                ofaAlarmThreshold.setPump1TempAlarmLow(UnitConfigConstant.translateTemperature(ofaAlarmThreshold
                        .getPump1TempAlarmLow() / 10) * 10);
            }
            if (ofaAlarmThreshold.getPump2TempAlarmUp() != null) {
                ofaAlarmThreshold.setPump2TempAlarmUp(UnitConfigConstant.translateTemperature(ofaAlarmThreshold
                        .getPump2TempAlarmUp() / 10) * 10);
            }
            if (ofaAlarmThreshold.getPump2TempAlarmLow() != null) {
                ofaAlarmThreshold.setPump2TempAlarmLow(UnitConfigConstant.translateTemperature(ofaAlarmThreshold
                        .getPump2TempAlarmLow() / 10) * 10);
            }
        }
        return ofaAlarmThreshold;
    }

    /**
     * 将温度统一转化为摄氏温度才能进行配置
     * 
     * @param ofaAlarmThreshold
     * @return
     */
    private OfaAlarmThreshold formatTempToSet(OfaAlarmThreshold ofaAlarmThreshold) {
        if (ofaAlarmThreshold == null) {
            return null;
        }
        String tempUnit = (String) UnitConfigConstant.get(UnitConfigConstant.TEMP_UNIT);
        if (UnitConfigConstant.FAHR_TEMP_UNIT.equalsIgnoreCase(tempUnit)) {// 用户设置为华氏度
            if (ofaAlarmThreshold.getPump1TempAlarmUp() != null) {
                // 从前台获取的值是放大10倍的值，所以华氏度需要除于10再转换为摄氏度
                ofaAlarmThreshold.setPump1TempAlarmUp(UnitConfigConstant.translateValueToCenti(ofaAlarmThreshold
                        .getPump1TempAlarmUp() / 10) * 10);
            }
            if (ofaAlarmThreshold.getPump1TempAlarmLow() != null) {
                ofaAlarmThreshold.setPump1TempAlarmLow(UnitConfigConstant.translateValueToCenti(ofaAlarmThreshold
                        .getPump1TempAlarmLow() / 10) * 10);
            }
            if (ofaAlarmThreshold.getPump2TempAlarmUp() != null) {
                ofaAlarmThreshold.setPump2TempAlarmUp(UnitConfigConstant.translateValueToCenti(ofaAlarmThreshold
                        .getPump2TempAlarmUp() / 10) * 10);
            }
            if (ofaAlarmThreshold.getPump2TempAlarmLow() != null) {
                ofaAlarmThreshold.setPump2TempAlarmLow(UnitConfigConstant.translateValueToCenti(ofaAlarmThreshold
                        .getPump2TempAlarmLow() / 10) * 10);
            }
        }
        return ofaAlarmThreshold;
    }
}
