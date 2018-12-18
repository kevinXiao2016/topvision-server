/***********************************************************************
 * $Id: OltOpticalServiceImpl.java,v1.0 2013-10-26 上午09:18:14 $
 * 
 * @author: lizongtian
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.optical.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.epon.olt.dao.OltPonDao;
import com.topvision.ems.epon.olt.dao.OltSniDao;
import com.topvision.ems.epon.optical.dao.OltOpticalDao;
import com.topvision.ems.epon.optical.domain.OltOnuOpticalAlarm;
import com.topvision.ems.epon.optical.domain.OltPonOptical;
import com.topvision.ems.epon.optical.domain.OltPonOpticalAlarm;
import com.topvision.ems.epon.optical.domain.OltSniOptical;
import com.topvision.ems.epon.optical.domain.OltSysOpticalAlarm;
import com.topvision.ems.epon.optical.domain.OnuPonOptical;
import com.topvision.ems.epon.optical.facade.OltOpticalFacade;
import com.topvision.ems.epon.optical.service.OltOpticalService;
import com.topvision.ems.network.service.EntityService;
import com.topvision.framework.service.BaseService;
import com.topvision.framework.snmp.SnmpParam;
import com.topvision.framework.utils.EponConstants;
import com.topvision.framework.utils.EponIndex;
import com.topvision.platform.facade.FacadeFactory;
import com.topvision.platform.message.event.SynchronizedEvent;
import com.topvision.platform.message.event.SynchronizedListener;
import com.topvision.platform.message.service.MessageService;

/**
 * @author lizongtian
 * @created @2013-10-26-上午09:18:14
 *
 */
@Service("oltOpticalService")
public class OltOpticalServiceImpl extends BaseService implements OltOpticalService, SynchronizedListener {
    protected Logger logger = LoggerFactory.getLogger(OltOpticalServiceImpl.class);
    @Autowired
    private FacadeFactory facadeFactory;
    @Autowired
    private EntityService entityService;
    @Autowired
    private MessageService messageService;
    @Autowired
    private OltOpticalDao oltOpticalDao;
    @Autowired
    private OltPonDao oltPonDao;
    @Autowired
    private OltSniDao oltSniDao;

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

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.epon.service.OpticalService#loadOltSniOptical(java.
     * lang.Long, java.lang.Long)
     */
    @Override
    public OltSniOptical loadOltSniOptical(Long entityId, Long portIndex) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        Integer slotNo = EponIndex.getSlotNo(portIndex).intValue();
        Integer portNo = EponIndex.getSniNo(portIndex).intValue();
        OltSniOptical re = getOpticalFacade(snmpParam.getIpAddress()).loadOltSniOptical(snmpParam, slotNo, portNo);
        if (re != null && re.getSlotNo() != null && re.getPortNo() != null) {
            re.setPortIndex(EponIndex.getPonIndex(re.getSlotNo(), re.getPortNo()));
            Long sniId = oltSniDao.getSniIdByIndex(portIndex, entityId);
            re.setSniId(sniId);
            re.setEntityId(entityId);
            /**
             * modify by lzt 2016-6-25 对光口信息无效值进行处理
             * 
             */
            if (re.getBiasCurrent() != null && re.getBiasCurrent().equals(EponConstants.OPT_CURRENT)) {
                re.setBiasCurrent(null);
            }
            if (re.getWorkingVoltage() != null && re.getWorkingVoltage().equals(EponConstants.OPT_VOLTAGE)) {
                re.setWorkingVoltage(null);
            }
            if (re.getWorkingTemp() != null && re.getWorkingTemp().equals(EponConstants.OPT_TEMP)) {
                re.setWorkingTemp(null);
            }
            if (re.getTxPower() != null && re.getTxPower().equals(EponConstants.TX_POWER)) {
                re.setTxPower(null);
            }
            if (re.getRxPower() != null && re.getRxPower().equals(EponConstants.RE_POWER)) {
                re.setRxPower(null);
            }
            if (oltOpticalDao.getOltSniOptical(sniId) == null) {
                oltOpticalDao.addOltSniOptical(re);
            } else {
                oltOpticalDao.updateOltSniOptical(re);
            }
        }
        return re;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.epon.service.OpticalService#loadOltPonOptical(java.
     * lang.Long, java.lang.Long)
     */
    @Override
    public OltPonOptical loadOltPonOptical(Long entityId, Long portIndex) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        Integer slotNo = EponIndex.getSlotNo(portIndex).intValue();
        Integer portNo = EponIndex.getPonNo(portIndex).intValue();
        OltPonOptical re = getOpticalFacade(snmpParam.getIpAddress()).loadOltPonOptical(snmpParam, slotNo, portNo);
        if (re != null && re.getSlotNo() != null && re.getPortNo() != null) {
            re.setPortIndex(EponIndex.getPonIndex(re.getSlotNo(), re.getPortNo()));
            Long ponId = oltPonDao.getPonIdByPonIndex(entityId, portIndex);
            re.setPonId(ponId);
            re.setEntityId(entityId);
            /**
             * modify by lzt 2016-6-25 对光口信息无效值进行处理
             * 
             */
            if (re.getBiasCurrent() != null && re.getBiasCurrent().equals(EponConstants.OPT_CURRENT)) {
                re.setBiasCurrent(null);
            }
            if (re.getWorkingVoltage() != null && re.getWorkingVoltage().equals(EponConstants.OPT_VOLTAGE)) {
                re.setWorkingVoltage(null);
            }
            if (re.getWorkingTemp() != null && re.getWorkingTemp().equals(EponConstants.OPT_TEMP)) {
                re.setWorkingTemp(null);
            }
            if (re.getTxPower() != null && re.getTxPower().equals(EponConstants.TX_POWER)) {
                re.setTxPower(null);
            }
            if (re.getRxPower() != null && re.getRxPower().equals(EponConstants.RE_POWER)) {
                re.setRxPower(null);
            }
            if (oltOpticalDao.getOltPonOptical(ponId) == null) {
                oltOpticalDao.addOltPonOptical(re);
            } else {
                oltOpticalDao.updateOltPonOptical(re);
            }
        }
        return re;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.epon.service.OpticalService#loadOnuPonOptical(java.
     * lang.Long, java.lang.Long)
     */
    @Override
    public OnuPonOptical loadOnuPonOptical(Long entityId, Long onuPonIndex) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        Long deviceIndex = EponIndex.getOnuMibIndexByIndex(onuPonIndex);
        OnuPonOptical re = getOpticalFacade(snmpParam.getIpAddress()).loadOnuPonOptical(snmpParam, deviceIndex);
        if (re != null) {
            re.setOnuPonIndex(onuPonIndex);
            Long onuPonId = oltOpticalDao.getOnuPonId(entityId, onuPonIndex);
            re.setOnuPonId(onuPonId);
            re.setEntityId(entityId);
            if (oltOpticalDao.getOnuPonOptical(entityId, onuPonIndex) == null) {
                oltOpticalDao.addOnuPonOptical(re);
            } else {
                oltOpticalDao.updateOnuPonOptical(re);
            }
        }
        return re;
    }

    @Override
    public OnuPonOptical getOnuOpticalInfo(Long entityId, Long onuIndex) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        Long deviceIndex = EponIndex.getOnuMibIndexByIndex(onuIndex);
        OnuPonOptical re = getOpticalFacade(snmpParam.getIpAddress()).loadOnuPonOptical(snmpParam, deviceIndex);
        return re;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.epon.service.OpticalService#getOltSniOptical(java.lang
     * .Long, java.lang.Long)
     */
    @Override
    public OltSniOptical getOltSniOptical(Long portId) {
        return oltOpticalDao.getOltSniOptical(portId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.epon.service.OpticalService#getOltPonOptical(java.lang
     * .Long, java.lang.Long)
     */
    @Override
    public OltPonOptical getOltPonOptical(Long portId) {
        return oltOpticalDao.getOltPonOptical(portId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.epon.service.OpticalService#getOnuPonOptical(java.lang
     * .Long, java.lang.Long)
     */
    @Override
    public OnuPonOptical getOnuPonOptical(Long entityId, Long onuPonIndex) {
        return oltOpticalDao.getOnuPonOptical(entityId, onuPonIndex);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.epon.service.OpticalService#getOltAllPortOptical(java
     * .lang.Long)
     */
    @Override
    public List<Map<String, String>> getOltAllPortOptical(Long entityId) {
        List<Map<String, String>> res = new ArrayList<Map<String, String>>();
        List<OltPonOptical> ps = oltOpticalDao.getAllPonOptical(entityId);
        if (ps.size() > 0) {
            for (OltPonOptical p : ps) {
                Map<String, String> re = new HashMap<String, String>();
                re.put("portIndex", p.getPortIndex().toString());
                re.put("identifier", p.getIdentifier().toString());
                re.put("vendorName", p.getVendorName());
                re.put("waveLength", p.getWaveLength().toString());
                re.put("vendorPN", p.getVendorPN());
                re.put("vendorSN", p.getVendorSN());
                re.put("dateCode", p.getDateCode());
                if (p.getBitRate() != null) {
                    re.put("bitRate", p.getBitRate().toString());
                } else {
                    re.put("bitRate", "--");
                }
                res.add(re);
            }
        }
        List<OltSniOptical> ss = oltOpticalDao.getAllSniOptical(entityId);
        if (ss.size() > 0) {
            for (OltSniOptical s : ss) {
                Map<String, String> re = new HashMap<String, String>();
                re.put("portIndex", s.getPortIndex().toString());
                re.put("identifier", s.getIdentifier().toString());
                re.put("vendorName", s.getVendorName());
                re.put("waveLength", s.getWaveLength().toString());
                re.put("vendorPN", s.getVendorPN());
                re.put("vendorSN", s.getVendorSN());
                re.put("dateCode", s.getDateCode());
                if (s.getBitRate() != null) {
                    re.put("bitRate", s.getBitRate().toString());
                } else {
                    re.put("bitRate", "--");
                }
                res.add(re);
            }
        }
        return res;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.epon.service.OpticalService#getAllOnuOptical(java.lang
     * .Long)
     */
    @Override
    public List<OnuPonOptical> getAllOnuOptical(Long entityId) {
        return oltOpticalDao.getAllOnuOptical(entityId);
    }

    /**
    * 获取OpticalFacade对象
    * 
    * @param ip
    *            被采集设备IP
    * @return OpticalFacade
    */
    private OltOpticalFacade getOpticalFacade(String ip) {
        return facadeFactory.getFacade(ip, OltOpticalFacade.class);
    }

    @Override
    public OltSysOpticalAlarm getSysOpticalAlarm(OltSysOpticalAlarm sysOpticalAlarm) {
        return oltOpticalDao.querySysOpticalAlarm(sysOpticalAlarm);
    }

    @Override
    public List<OltSysOpticalAlarm> getSysOpticalAlarmList(Long entityId) {
        return oltOpticalDao.querySysOpticalAlarmList(entityId);
    }

    @Override
    public List<OltOnuOpticalAlarm> getOnuOpticalAlarmList(OltOnuOpticalAlarm onuOptical) {
        return oltOpticalDao.queryOnuOpticalAlarmList(onuOptical);
    }

    @Override
    public List<OltPonOpticalAlarm> getPonOpticalAlarmList(OltPonOpticalAlarm ponOptical) {
        return oltOpticalDao.queryPonOpticalAlarmList(ponOptical);
    }

    @Override
    public void modifySysOpticalAlarm(List<OltSysOpticalAlarm> list) {
        for (OltSysOpticalAlarm sysOpticalAlarm : list) {
            oltOpticalDao.updateSysOpticalAlarm(sysOpticalAlarm);
        }
    }

    @Override
    public void modifySysOpticalAlarm(Long entityId, List<OltSysOpticalAlarm> list) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        for (OltSysOpticalAlarm alarm : list) {
            if (alarm.getTopSysOptAlarmSoapTime() > 0) {
                alarm.setTopSysOptAlarmSwitch(1);
            } else {
                alarm.setTopSysOptAlarmSwitch(2);
            }
            getFacade(snmpParam.getIpAddress()).modifySysOpticalAlarm(snmpParam, alarm);
            alarm.setEntityId(entityId);
        }
        oltOpticalDao.batchInsertOltOptical(list, entityId);
    }

    @Override
    public void modifyOltPonOptAlarm(Long entityId, List<OltPonOpticalAlarm> list) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        for (OltPonOpticalAlarm thresh : list) {
            getFacade(snmpParam.getIpAddress()).modifyOltPonOptAlarm(snmpParam, thresh);
            thresh.setEntityId(entityId);
        }
        oltOpticalDao.updateOltPonOptAlarm(list, entityId);
    }

    @Override
    public void deletePonOpticalAlarm(OltPonOpticalAlarm ponOpticalAlarm) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(ponOpticalAlarm.getEntityId());
        getFacade(snmpParam.getIpAddress()).deleteOltPonOptAlarm(snmpParam, ponOpticalAlarm);
        oltOpticalDao.deleteOltPonOptAlarm(ponOpticalAlarm);
    }

    @Override
    public void modifyOltOnuOptAlarm(Long entityId, List<OltOnuOpticalAlarm> list) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        List<OltOnuOpticalAlarm> updatelist = new ArrayList<OltOnuOpticalAlarm>();
        for (OltOnuOpticalAlarm thresh : list) {
            updatelist.add(thresh);
            getFacade(snmpParam.getIpAddress()).modifyOltOnuOptAlarm(snmpParam, thresh);
            thresh.setEntityId(entityId);
        }
        oltOpticalDao.updateOltOnuOptAlarm(updatelist, entityId);
    }

    @Override
    public void deleteOnuOptAlarm(OltOnuOpticalAlarm onuOpticalAlarm) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(onuOpticalAlarm.getEntityId());
        try {
            getFacade(snmpParam.getIpAddress()).deleteOnuOptAlarm(snmpParam, onuOpticalAlarm);
        } catch (Exception e) {
            logger.error("", e);
        } finally {
            oltOpticalDao.deleteOnuOptAlarm(onuOpticalAlarm);
        }
    }

    private OltOpticalFacade getFacade(String ip) {
        return facadeFactory.getFacade(ip, OltOpticalFacade.class);
    }

    @Override
    public void insertEntityStates(SynchronizedEvent event) {
            try {
                refreshOltSysOpticalAlarm(event.getEntityId());
                logger.info("refreshOltSysOpticalAlarm success!");
            } catch (Exception e) {
                logger.error("refreshOltSysOpticalAlarm error:", e);
            }
            try {
                refreshOltPonOpticalAlarm(event.getEntityId());
                logger.info("refreshOltPonOpticalAlarm success!");
            } catch (Exception e) {
                logger.error("refreshOltPonOpticalAlarm error:", e);
            }
            try {
                refreshOltOnuOpticalAlarm(event.getEntityId());
                logger.info("refreshOltOnuOpticalAlarm success!");
            } catch (Exception e) {
                logger.error("refreshOltOnuOpticalAlarm error:", e);
            }

            try {
                refreshOltPonOptical(event.getEntityId());
                logger.info("refreshOltPonOptical success!");
            } catch (Exception e) {
                logger.error("refreshOltPonOptical error:", e);
            }

            try {
                refreshOltSniOptical(event.getEntityId());
                logger.info("refreshOltSniOptical success!");
            } catch (Exception e) {
                logger.error("refreshOltSniOptical error:", e);
            }

    }

    @Override
    public void refreshOltPonOptical(Long entityId) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        List<OltPonOptical> oltPonOpticals = getFacade(snmpParam.getIpAddress()).getOtlPonOptical(snmpParam);
        if (oltPonOpticals != null) {
            for (OltPonOptical oltPonOptical : oltPonOpticals) {
                oltPonOptical.setEntityId(entityId);
                oltPonOptical.setPortIndex(EponIndex.getPonIndex(oltPonOptical.getSlotNo(), oltPonOptical.getPortNo()));
                oltPonOptical.setPonId(oltPonDao.getPonIdByPonIndex(entityId, oltPonOptical.getPortIndex()));
                /**
                 * modify by lzt 2016-6-25 对光口信息无效值进行处理
                 * 
                 */
                if (oltPonOptical.getTxPower() != null && oltPonOptical.getTxPower().equals(EponConstants.TX_POWER)) {
                    oltPonOptical.setTxPower(null);
                }
                if (oltPonOptical.getRxPower() != null && oltPonOptical.getRxPower().equals(EponConstants.RE_POWER)) {
                    oltPonOptical.setRxPower(null);
                }
                if (oltPonOptical.getBiasCurrent() != null
                        && oltPonOptical.getBiasCurrent().equals(EponConstants.OPT_CURRENT)) {
                    oltPonOptical.setBiasCurrent(null);
                }
                if (oltPonOptical.getWorkingTemp() != null
                        && oltPonOptical.getWorkingTemp().equals(EponConstants.OPT_TEMP)) {
                    oltPonOptical.setWorkingTemp(null);
                }
                if (oltPonOptical.getWorkingVoltage() != null
                        && oltPonOptical.getWorkingVoltage().equals(EponConstants.OPT_VOLTAGE)) {
                    oltPonOptical.setWorkingVoltage(null);
                }
            }
            oltOpticalDao.batchInsertPonOptical(oltPonOpticals, entityId);
        }
    }

    @Override
    public void refreshOltSniOptical(Long entityId) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        List<OltSniOptical> oltSniOpticals = getFacade(snmpParam.getIpAddress()).getOltSniOptical(snmpParam);
        if (oltSniOpticals != null) {
            for (OltSniOptical sniOptical : oltSniOpticals) {
                sniOptical.setEntityId(entityId);
                sniOptical.setPortIndex(EponIndex.getPonIndex(sniOptical.getSlotNo(), sniOptical.getPortNo()));
                sniOptical.setSniId(oltSniDao.getSniIdByIndex(sniOptical.getPortIndex(), entityId));
                /**
                 * modify by lzt 2016-6-25 对光口信息无效值进行处理
                 * 
                 */
                if (sniOptical.getTxPower() != null && sniOptical.getTxPower().equals(EponConstants.TX_POWER)) {
                    sniOptical.setTxPower(null);
                }
                if (sniOptical.getRxPower() != null && sniOptical.getRxPower().equals(EponConstants.RE_POWER)) {
                    sniOptical.setRxPower(null);
                }
                if (sniOptical.getBiasCurrent() != null
                        && sniOptical.getBiasCurrent().equals(EponConstants.OPT_CURRENT)) {
                    sniOptical.setBiasCurrent(null);
                }
                if (sniOptical.getWorkingTemp() != null && sniOptical.getWorkingTemp().equals(EponConstants.OPT_TEMP)) {
                    sniOptical.setWorkingTemp(null);
                }
                if (sniOptical.getWorkingVoltage() != null
                        && sniOptical.getWorkingVoltage().equals(EponConstants.OPT_VOLTAGE)) {
                    sniOptical.setWorkingVoltage(null);
                }
            }
            oltOpticalDao.batchInsertSniOptical(oltSniOpticals, entityId);
        }
    }

    @Override
    public void refreshOltOnuOpticalAlarm(long entityId) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        List<OltOnuOpticalAlarm> list = getFacade(snmpParam.getIpAddress()).getOltOnuOpticalAlarm(snmpParam);
        oltOpticalDao.batchInsertOnuOpticalAlarm(list, entityId);
    }

    @Override
    public void refreshOltPonOpticalAlarm(long entityId) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        List<OltPonOpticalAlarm> list = getFacade(snmpParam.getIpAddress()).getOltPonOpticalAlarm(snmpParam);
        oltOpticalDao.batchInsertPonOpticalAlarm(list, entityId);
    }

    @Override
    public void refreshOltSysOpticalAlarm(long entityId) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        List<OltSysOpticalAlarm> list = getFacade(snmpParam.getIpAddress()).getOltSysOpticalAlarm(snmpParam);
        oltOpticalDao.batchInsertOltOptical(list, entityId);
    }

    @Override
    public void updateEntityStates(SynchronizedEvent event) {
    }

}
