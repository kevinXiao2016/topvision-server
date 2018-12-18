/***********************************************************************
 * $Id: OltPortInfoServiceImpl.java,v1.0 2016-4-12 上午11:55:28 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.portinfo.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.epon.olt.domain.OltPonAttribute;
import com.topvision.ems.epon.olt.domain.OltSniAttribute;
import com.topvision.ems.epon.olt.service.OltPonService;
import com.topvision.ems.epon.olt.service.OltSniService;
import com.topvision.ems.epon.optical.domain.OltPonOptical;
import com.topvision.ems.epon.optical.domain.OltSniOptical;
import com.topvision.ems.epon.optical.facade.OltOpticalFacade;
import com.topvision.ems.epon.portinfo.dao.OltPortInfoDao;
import com.topvision.ems.epon.portinfo.domain.OltPortOpticalInfo;
import com.topvision.ems.epon.portinfo.service.OltPortInfoService;
import com.topvision.ems.epon.realtime.domain.OltPortSpeedInfo;
import com.topvision.ems.epon.realtime.facade.OltRealtimeFacade;
import com.topvision.ems.message.Message;
import com.topvision.ems.message.MessagePusher;
import com.topvision.ems.network.service.EntityService;
import com.topvision.framework.service.BaseService;
import com.topvision.framework.snmp.SnmpParam;
import com.topvision.framework.utils.EponConstants;
import com.topvision.framework.utils.EponIndex;
import com.topvision.platform.facade.FacadeFactory;

/**
 * @author flack
 * @created @2016-4-12-上午11:55:28
 *
 */
@Service("oltPortInfoService")
public class OltPortInfoServiceImpl extends BaseService implements OltPortInfoService {

    @Autowired
    private OltPortInfoDao oltPortInfoDao;
    @Autowired
    private OltSniService oltSniService;
    @Autowired
    private FacadeFactory facadeFactory;
    @Autowired
    private EntityService entityService;
    @Autowired
    private MessagePusher messagePusher;
    @Autowired
    private OltPonService oltPonService;

    @Override
    public List<OltPortOpticalInfo> getSniOpticalInfoList(Long entityId) {
        List<OltPortOpticalInfo> sniOpticalList = oltPortInfoDao.querySniOpticalInfoList(entityId);
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        //按要求进行实时速率的刷新
        for (OltPortOpticalInfo sniOptical : sniOpticalList) {
            try {
                //获取实时速率信息,但是只有在端口性能统计使能开启的情况下才能获取到数据,否则会抛异常
                OltPortSpeedInfo speed = new OltPortSpeedInfo();
                if (sniOptical.getPerfStats().equals(EponConstants.ABALITY_ENABLE)) {
                    speed.setEntityId(entityId);
                    speed.setDeviceIndex(EponIndex.getPortDeviceIndex(sniOptical.getPortIndex(),
                            EponConstants.PERF_TYPE_OLT_ETH));
                    speed.setCardIndex(0);
                    speed.setPortIndex(0);
                    try {
                        speed = getRealtimeFacade(snmpParam.getIpAddress()).getPortSpeedInfo(snmpParam, speed);
                    } catch (Exception e) {
                        logger.error("Sync sni port speed rate failed : {}", e.getMessage());
                    }
                }
                if (speed.getInBindWidth() == null || speed.getOutBindWidth() == null) {
                    sniOptical.setInCurrentRate(-1L);
                    sniOptical.setOutCurrentRate(-1L);
                } else {
                    sniOptical.setInCurrentRate(speed.getInBindWidth());
                    sniOptical.setOutCurrentRate(speed.getOutBindWidth());
                }
                sniOptical.setModifyTime(new Date());
                oltPortInfoDao.updateSniOpticalInfo(sniOptical);
            } catch (Exception e) {
                logger.error("Sync sni port current rate failed when getSniOpticalInfoList :", e);
            }
        }
        return sniOpticalList;
    }

    @Override
    public List<OltPortOpticalInfo> getPonOpticalInfoList(Long entityId) {
        List<OltPortOpticalInfo> ponOpticalList = oltPortInfoDao.queryPonOpticalInfoList(entityId);
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        //按要求进行实时速率的刷新
        for (OltPortOpticalInfo ponOptical : ponOpticalList) {
            try {
                //获取实时速率信息,但是只有在端口性能统计使能开启的情况下才能获取到数据,否则会抛异常
                OltPortSpeedInfo speed = new OltPortSpeedInfo();
                if (ponOptical.getPerfStats().equals(EponConstants.ABALITY_ENABLE)) {
                    speed.setEntityId(entityId);
                    if (EponConstants.PON_PORT_TYPE_GPON.equals(ponOptical.getPortType())) {
                        speed.setDeviceIndex(EponIndex.getPortDeviceIndex(ponOptical.getPortIndex(),
                                EponConstants.PERF_TYPE_OLT_GPON));
                    } else {
                        speed.setDeviceIndex(EponIndex.getOnuMibIndexByIndex(ponOptical.getPortIndex()));
                    }
                    speed.setCardIndex(0);
                    speed.setPortIndex(0);
                    speed = getRealtimeFacade(snmpParam.getIpAddress()).getPortSpeedInfo(snmpParam, speed);
                }
                if (speed.getInBindWidth() == null || speed.getOutBindWidth() == null) {
                    ponOptical.setInCurrentRate(-1L);
                    ponOptical.setOutCurrentRate(-1L);
                } else {
                    ponOptical.setInCurrentRate(speed.getInBindWidth());
                    ponOptical.setOutCurrentRate(speed.getOutBindWidth());
                }
                ponOptical.setModifyTime(new Date());
                oltPortInfoDao.updatePonOpticalInfo(ponOptical);
            } catch (Exception e) {
                logger.error("Sync pon port current rate failed when getPonOpticalInfoList :", e);
            }
        }
        return ponOpticalList;
    }

    @Override
    public void updateSniOpticalInfo(OltPortOpticalInfo sniOpticalInfo) {
        oltPortInfoDao.updateSniOpticalInfo(sniOpticalInfo);
    }

    @Override
    public void updatePonOpticalInfo(OltPortOpticalInfo ponOpticalInfo) {
        oltPortInfoDao.updatePonOpticalInfo(ponOpticalInfo);
    }

    @Override
    public OltPortOpticalInfo refreshSniOpticalInfo(Long entityId, Long portIndex, Long sniId, Integer perfStats) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        Integer slotNo = EponIndex.getSlotNo(portIndex).intValue();
        Integer portNo = EponIndex.getSniNo(portIndex).intValue();
        //获取SNI口光功率信息
        OltSniOptical sniOptical = getOpticalFacade(snmpParam.getIpAddress()).loadOltSniOptical(snmpParam, slotNo,
                portNo);
        sniOptical.setEntityId(entityId);
        sniOptical.setPortIndex(portIndex);
        sniOptical.setSniId(sniId);
        //获取实时速率信息,但是只有在端口性能统计使能开启的情况下才能获取到数据,否则会抛异常
        OltPortSpeedInfo speed = new OltPortSpeedInfo();
        if (perfStats.equals(EponConstants.ABALITY_ENABLE)) {
            speed.setEntityId(entityId);
            speed.setDeviceIndex(EponIndex.getPortDeviceIndex(portIndex, EponConstants.PERF_TYPE_OLT_ETH));
            speed.setCardIndex(0);
            speed.setPortIndex(0);
            speed = getRealtimeFacade(snmpParam.getIpAddress()).getPortSpeedInfo(snmpParam, speed);
        }
        //将获取的值封装为OltPortOpticalInfo
        OltPortOpticalInfo sniPortOptical = new OltPortOpticalInfo(sniOptical, speed);
        try {
            OltSniAttribute sniAttribute = oltSniService.updateSniPortStatus(entityId, sniOptical.getSniId());
            if (sniAttribute != null) {
                sniPortOptical.setAdminStatus(sniAttribute.getSniAdminStatus());
                sniPortOptical.setOperationStatus(sniAttribute.getSniOperationStatus());
            }
        } catch (Exception e) {
            logger.info("refreshSniStatus  error {0}", e.getMessage());
        }
        sniPortOptical.setModifyTime(new Date());
        oltPortInfoDao.updateSniOpticalInfo(sniPortOptical);
        return sniPortOptical;
    }

    @Override
    public void refreshAllSniOptical(Long entityId, String jConnectedId, String seesionId) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        List<OltSniOptical> sniList = oltPortInfoDao.queryOltSniList(entityId);
        //处理为portIndex与端口的对应关系
        Map<Long, OltSniOptical> sniMap = encodeSniMap(sniList);
        List<OltSniOptical> sniOpticalist = getOpticalFacade(snmpParam.getIpAddress()).getOltSniOptical(snmpParam);
        for (OltSniOptical sniOptical : sniOpticalist) {
            sniOptical.setEntityId(entityId);
            int slotNo = sniOptical.getSlotNo();
            int portNo = sniOptical.getPortNo();
            Long portIndex = EponIndex.getSniIndex(slotNo, portNo);
            sniOptical.setPortIndex(portIndex);
            if (sniMap.get(portIndex) != null) {
                sniOptical.setSniId(sniMap.get(portIndex).getSniId());
            }
            OltPortSpeedInfo speed = new OltPortSpeedInfo();
            try {
                speed.setEntityId(entityId);
                speed.setDeviceIndex(EponIndex.getPortDeviceIndex(portIndex, EponConstants.PERF_TYPE_OLT_ETH));
                speed.setCardIndex(0);
                speed.setPortIndex(0);
                speed = getRealtimeFacade(snmpParam.getIpAddress()).getPortSpeedInfo(snmpParam, speed);
            } catch (Exception e) {
                logger.error("", e);
            }
            //将获取的值封装为OltPortOpticalInfo
            OltPortOpticalInfo sniPortOptical = new OltPortOpticalInfo(sniOptical, speed);
            sniPortOptical.setModifyTime(new Date());
            try {
                OltSniAttribute sniAttribute = oltSniService.updateSniPortStatus(entityId, sniOptical.getSniId());
                if (sniAttribute != null) {
                    sniPortOptical.setAdminStatus(sniAttribute.getSniAdminStatus());
                    sniPortOptical.setOperationStatus(sniAttribute.getSniOperationStatus());
                }
            } catch (Exception e) {
                logger.info("refreshSniStatus  error {0}", e.getMessage());
            }
            oltPortInfoDao.updatePonOpticalInfo(sniPortOptical);
            pushPortOpticalMessage(sniPortOptical, jConnectedId, seesionId, "refreshAllSniOptical");
        }
    }

    /**
     * 将SNI端口列表处理为portIndex与端口的对应关系
     * @param sniList
     * @return
     */
    private Map<Long, OltSniOptical> encodeSniMap(List<OltSniOptical> sniList) {
        Map<Long, OltSniOptical> sniMap = new HashMap<Long, OltSniOptical>();
        for (OltSniOptical sniPort : sniList) {
            sniMap.put(sniPort.getPortIndex(), sniPort);
        }
        return sniMap;
    }

    @Override
    public OltPortOpticalInfo refreshPonOpticalInfo(Long entityId, Long portIndex, Long ponId, Integer perfStats) {
        OltPonAttribute ponAttr = oltPonService.getPonAttribute(ponId);
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        Integer slotNo = EponIndex.getSlotNo(portIndex).intValue();
        Integer portNo = EponIndex.getSniNo(portIndex).intValue();
        //获取PON口光功率信息
        OltPonOptical ponOptical = getOpticalFacade(snmpParam.getIpAddress()).loadOltPonOptical(snmpParam, slotNo,
                portNo);
        ponOptical.setEntityId(entityId);
        ponOptical.setPonId(ponId);
        ponOptical.setPortIndex(portIndex);
        //获取实时速率信息,但是只有在端口性能统计使能开启的情况下才能获取到数据,否则会抛异常
        OltPortSpeedInfo speed = new OltPortSpeedInfo();

        if (perfStats.equals(EponConstants.ABALITY_ENABLE)) {
            speed.setEntityId(entityId);
            if (EponConstants.PON_PORT_TYPE_GPON.equals(ponAttr.getPonPortType())) {
                speed.setDeviceIndex(EponIndex.getPortDeviceIndex(portIndex, EponConstants.PERF_TYPE_OLT_GPON));
            } else {
                speed.setDeviceIndex(EponIndex.getOnuMibIndexByIndex(portIndex));
            }
            speed.setCardIndex(0);
            speed.setPortIndex(0);
            try {
                //对GPON端口实时性能统计数据无法获取
                speed = getRealtimeFacade(snmpParam.getIpAddress()).getPortSpeedInfo(snmpParam, speed);
            } catch (Exception e) {
                logger.info("OltPortSpeedInfo get error {0}", e.getMessage());
            }
        }
        //将获取的值封装为OltPortOpticalInfo
        OltPortOpticalInfo ponPortOptical = new OltPortOpticalInfo(ponOptical, speed);
        try {
            OltPonAttribute ponAttribute = oltPonService.refreshPonStatus(entityId, portIndex);
            if (ponAttribute != null) {
                ponPortOptical.setAdminStatus(ponAttribute.getPonPortAdminStatus());
                ponPortOptical.setOperationStatus(ponAttribute.getPonOperationStatus());
            }
        } catch (Exception e) {
            logger.info("refreshPonStatus  error {0}", e.getMessage());
        }
        ponPortOptical.setModifyTime(new Date());
        oltPortInfoDao.updatePonOpticalInfo(ponPortOptical);
        return ponPortOptical;
    }

    @Override
    public void refreshAllPonOptical(Long entityId, String jConnectedId, String seesionId) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        List<OltPonOptical> ponList = oltPortInfoDao.queryOltPonList(entityId);
        Map<Long, OltPonOptical> ponMap = encodePonMap(ponList);
        List<OltPonOptical> ponOpticalist = getOpticalFacade(snmpParam.getIpAddress()).getOtlPonOptical(snmpParam);
        /*try {
            oltPonService.refreshAllPonStatus(entityId);
        } catch (Exception e) {
            logger.info("refreshAllPonStatus  error {0}", e.getMessage());
        }*/
        for (OltPonOptical ponOptical : ponOpticalist) {
            ponOptical.setEntityId(entityId);
            int slotNo = ponOptical.getSlotNo();
            int portNo = ponOptical.getPortNo();
            Long portIndex = EponIndex.getPonIndex(slotNo, portNo);
            ponOptical.setPortIndex(portIndex);
            if (ponMap.get(portIndex) != null) {
                ponOptical.setPonId(ponMap.get(portIndex).getPonId());
                ponOptical.setPortType(ponMap.get(portIndex).getPortType());
            }
            OltPortSpeedInfo speed = new OltPortSpeedInfo();
            try {
                speed.setEntityId(entityId);
                if (EponConstants.PON_PORT_TYPE_GPON.equals(ponOptical.getPortType())) {
                    speed.setDeviceIndex(EponIndex.getPortDeviceIndex(portIndex, EponConstants.PERF_TYPE_OLT_GPON));
                } else {
                    speed.setDeviceIndex(EponIndex.getOnuMibIndexByIndex(portIndex));
                }
                speed.setCardIndex(0);
                speed.setPortIndex(0);
                speed = getRealtimeFacade(snmpParam.getIpAddress()).getPortSpeedInfo(snmpParam, speed);
            } catch (Exception e) {
                logger.error("", e);
            }

            //将获取的值封装为OltPortOpticalInfo
            OltPortOpticalInfo ponPortOptical = new OltPortOpticalInfo(ponOptical, speed);
            ponPortOptical.setModifyTime(new Date());
            try {
                OltPonAttribute ponAttribute = oltPonService.refreshPonStatus(entityId, portIndex);
                if (ponAttribute != null) {
                    ponPortOptical.setAdminStatus(ponAttribute.getPonPortAdminStatus());
                    ponPortOptical.setOperationStatus(ponAttribute.getPonOperationStatus());
                }
            } catch (Exception e) {
                logger.info("refreshPonStatus  error {0}", e.getMessage());
            }
            oltPortInfoDao.updatePonOpticalInfo(ponPortOptical);
            pushPortOpticalMessage(ponPortOptical, jConnectedId, seesionId, "refreshAllPonOptical");
        }
    }

    /**
     * 将Pon端口列表处理为portIndex与端口的对应关系
     * @param ponList
     * @return
     */
    private Map<Long, OltPonOptical> encodePonMap(List<OltPonOptical> ponList) {
        Map<Long, OltPonOptical> ponMap = new HashMap<Long, OltPonOptical>();
        for (OltPonOptical ponPort : ponList) {
            ponMap.put(ponPort.getPortIndex(), ponPort);
        }
        return ponMap;
    }

    /**
     * 消息推送
     * @param portOptical
     * @param jConnectedId
     * @param seesionId
     * @param msgType
     */
    private void pushPortOpticalMessage(OltPortOpticalInfo portOptical, String jConnectedId, String seesionId,
            String msgType) {
        if (jConnectedId != null) {
            Message message = new Message(msgType);
            message.addSessionId(seesionId);
            message.setJconnectID(jConnectedId);
            message.setData(portOptical);
            messagePusher.sendMessage(message);
        }
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

    private OltRealtimeFacade getRealtimeFacade(String ip) {
        return facadeFactory.getFacade(ip, OltRealtimeFacade.class);
    }
}
