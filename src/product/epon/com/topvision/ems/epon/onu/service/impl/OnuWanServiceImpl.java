/***********************************************************************
 * $Id: OnuWanServiceImpl.java,v1.0 2016年5月30日 下午4:04:11 $
 * 
 * @author: loyal
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.onu.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.devicesupport.version.service.DeviceVersionService;
import com.topvision.ems.devicesupport.version.util.DeviceFuctionSupport;
import com.topvision.ems.epon.olt.service.OltService;
import com.topvision.ems.epon.onu.dao.OnuDao;
import com.topvision.ems.epon.onu.dao.OnuWanDao;
import com.topvision.ems.epon.onu.domain.OnuWanBind;
import com.topvision.ems.epon.onu.domain.OnuWanConfig;
import com.topvision.ems.epon.onu.domain.OnuWanConnect;
import com.topvision.ems.epon.onu.domain.OnuWanConnectStatus;
import com.topvision.ems.epon.onu.domain.OnuWanSsid;
import com.topvision.ems.epon.onu.facade.OnuWanFacade;
import com.topvision.ems.epon.onu.service.OnuWanService;
import com.topvision.ems.epon.topology.event.OnuSynchronizedEvent;
import com.topvision.ems.epon.topology.event.OnuSynchronizedListener;
import com.topvision.ems.epon.utils.EponUtil;
import com.topvision.ems.network.dao.EntityDao;
import com.topvision.ems.network.service.EntityService;
import com.topvision.framework.service.BaseService;
import com.topvision.framework.snmp.SnmpParam;
import com.topvision.platform.facade.FacadeFactory;
import com.topvision.platform.message.service.MessageService;

/**
 * @author loyal
 * @created @2016年5月30日-下午4:04:11
 *
 */
@Service("onuWanService")
public class OnuWanServiceImpl extends BaseService implements OnuWanService, OnuSynchronizedListener {
    @Autowired
    protected MessageService messageService;
    @Autowired
    private FacadeFactory facadeFactory;
    @Autowired
    private OnuWanDao onuWanDao;
    @Autowired
    private OnuDao onuDao;
    @Autowired
    private EntityService entityService;
    @Autowired
    private EntityDao entityDao;
    @Autowired
    private DeviceVersionService deviceVersionService;
    @Autowired
    private OltService oltService;
    public static final Integer ONU_SINGLE_TOPO = 1;

    @PreDestroy
    @Override
    public void destroy() {
        super.destroy();
        messageService.removeListener(OnuSynchronizedListener.class, this);
    }

    @PostConstruct
    @Override
    public void initialize() {
        super.initialize();
        messageService.addListener(OnuSynchronizedListener.class, this);
    }

    @Override
    public void insertEntityStates(OnuSynchronizedEvent event) {
        Long entityId = event.getEntityId();
        List<Long> onuIndexList = event.getOnuIndexList();
        try {
            if (onuIndexList.size() == ONU_SINGLE_TOPO) {
                refreshOnuWanConfig(entityId, onuIndexList.get(0));
            } else if (onuIndexList.size() > ONU_SINGLE_TOPO) {
                refreshOnuWanConfig(entityId);
            }
            logger.info("refreshOnuWanConfig finish");
        } catch (Exception e) {
            logger.error("refreshOnuWanConfig wrong", e);
        }

        try {
            if (onuIndexList.size() == ONU_SINGLE_TOPO) {
                refreshOnuWanSsid(entityId, onuIndexList.get(0));
            } else if (onuIndexList.size() > ONU_SINGLE_TOPO) {
                refreshOnuWanSsid(entityId);
            }
            logger.info("refreshOnuWanSsid finish");
        } catch (Exception e) {
            logger.error("refreshOnuWanSsid wrong", e);
        }

        try {
            if (onuIndexList.size() == ONU_SINGLE_TOPO) {
                refreshOnuWanConnect(entityId, onuIndexList.get(0));
            } else if (onuIndexList.size() > ONU_SINGLE_TOPO) {
                refreshOnuWanConnect(entityId);
            }
            logger.info("refreshOnuWanConnect finish");
        } catch (Exception e) {
            logger.error("refreshOnuWanConnect wrong", e);
        }

        try {
            if (onuIndexList.size() == ONU_SINGLE_TOPO) {
                refreshOnuWanConnectStatus(entityId, onuIndexList.get(0));
            } else if (onuIndexList.size() > ONU_SINGLE_TOPO) {
                refreshOnuWanConnectStatus(entityId);
            }
            logger.info("refreshOnuWanConnectStatus finish");
        } catch (Exception e) {
            logger.error("refreshOnuWanConnectStatus wrong", e);
        }
    }

    @Override
    public OnuWanConfig getOnuWanConfig(Long onuId) {
        return onuWanDao.getOnuWanConfig(onuId);
    }

    @Override
    public List<OnuWanSsid> getOnuWanSsid(Long onuId) {
        return onuWanDao.getOnuWanSsid(onuId);
    }

    @Override
    public OnuWanSsid getOnuWanSsid(Long onuId, Integer ssid) {
        return onuWanDao.getOnuWanSsid(onuId, ssid);
    }

    @Override
    public List<OnuWanConnect> getOnuWanConnect(Long onuId) {
        return onuWanDao.getOnuWanConnect(onuId);
    }

    @Override
    public OnuWanConnect loadWanConnection(Long onuId, Integer connectId) {
        return onuWanDao.getOnuWanConnect(onuId, connectId);
    }

    @Override
    public List<OnuWanConnectStatus> getOnuWanConnectStatus(Long onuId) {
        return onuWanDao.getOnuWanConnectStatus(onuId);
    }

    @Override
    public void insertOnuWanConfig(OnuWanConfig onuWanConfig) {
        onuWanDao.insertOnuWanConfig(onuWanConfig);
    }

    @Override
    public void insertOnuWanSsid(OnuWanSsid onuWanSsid) {
        onuWanDao.insertOnuWanSsid(onuWanSsid);
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(onuWanSsid.getEntityId());
        String deviceVersion = deviceVersionService.getParamValue(onuWanSsid.getEntityId(), "onuWan", "deviceVersion");
        snmpParam.setDeviceVersion(deviceVersion);
        getOnuWanFacade(snmpParam.getIpAddress()).addOnuWanSsid(snmpParam, onuWanSsid);
    }

    @Override
    public void insertOnuWanConnect(OnuWanConnect onuWanConnect) {
        Long onuIndex = onuDao.getOnuIndex(onuWanConnect.getOnuId());
        onuWanConnect.setOnuIndex(onuIndex);
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(onuWanConnect.getEntityId());
        String deviceVersion = deviceVersionService.getParamValue(onuWanConnect.getEntityId(), "onuWan",
                "deviceVersion");
        snmpParam.setDeviceVersion(deviceVersion);
        Integer serviceMode = onuWanConnect.getServiceMode();
        onuWanConnect.setServiceMode(null);
        getOnuWanFacade(snmpParam.getIpAddress()).addOnuWanConnect(snmpParam, onuWanConnect);
        try {
            OnuWanBind onuWanBind = new OnuWanBind();
            onuWanBind.setOnuMibIndex(onuWanConnect.getOnuMibIndex());
            onuWanBind.setConnectId(onuWanConnect.getConnectId());
            onuWanBind.setServiceMode(serviceMode);
            getOnuWanFacade(snmpParam.getIpAddress()).modifyServieMode(snmpParam, onuWanBind);
        } catch (Exception e) {
            logger.error("insertOnuWanConnect error,modifyServieMode", e);
        }
        // EMS-13736 新建wan连接时刷新wan连接状态
        refreshOnuWanConnect(onuWanConnect.getEntityId(), onuIndex);
        refreshOnuWanConnectStatus(onuWanConnect.getEntityId(), onuIndex);
    }

    @Override
    public void insertOnuWanConnectStatus(OnuWanConnectStatus onuWanConnectStatus) {
        onuWanDao.insertOnuWanConnectStatus(onuWanConnectStatus);
    }

    @Override
    public void deleteOnuWanSsid(Long onuId, Long onuIndex, Long entityId, Integer ssid) {
        OnuWanSsid onuWanSsid = new OnuWanSsid();
        onuWanSsid.setEntityId(entityId);
        onuWanSsid.setOnuId(onuId);
        onuWanSsid.setOnuIndex(onuIndex);
        onuWanSsid.setSsid(ssid);
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        String deviceVersion = deviceVersionService.getParamValue(entityId, "onuWan", "deviceVersion");
        snmpParam.setDeviceVersion(deviceVersion);
        getOnuWanFacade(snmpParam.getIpAddress()).deleteOnuWanSsid(snmpParam, onuWanSsid);
        onuWanDao.deleteOnuWanSsid(onuId, ssid);
    }

    @Override
    public void deleteOnuWanConnect(Long entityId, Long onuId, Integer connectId) {
        OnuWanConnect onuWanConnect = new OnuWanConnect();
        onuWanConnect.setOnuIndex(onuDao.getOnuIndex(onuId));
        onuWanConnect.setConnectId(connectId);
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        String deviceVersion = deviceVersionService.getParamValue(entityId, "onuWan", "deviceVersion");
        snmpParam.setDeviceVersion(deviceVersion);
        getOnuWanFacade(snmpParam.getIpAddress()).deleteOnuWanConnect(snmpParam, onuWanConnect);
        onuWanDao.deleteOnuWanConnect(onuId, connectId);
    }

    @Override
    public void updateOnuWanConfig(OnuWanConfig onuWanConfig) {
        onuWanDao.updateOnuWanConfig(onuWanConfig);
    }

    @Override
    public void updateOnuWanSsid(OnuWanSsid onuWanSsid) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(onuWanSsid.getEntityId());
        String deviceVersion = deviceVersionService.getParamValue(onuWanSsid.getEntityId(), "onuWan", "deviceVersion");
        snmpParam.setDeviceVersion(deviceVersion);
        getOnuWanFacade(snmpParam.getIpAddress()).modifyOnuWanSsid(snmpParam, onuWanSsid);
        onuWanDao.updateOnuWanSsid(onuWanSsid);
    }

    @Override
    public void updateWifiPassword(OnuWanSsid onuWanSsid) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(onuWanSsid.getEntityId());
        String deviceVersion = deviceVersionService.getParamValue(onuWanSsid.getEntityId(), "onuWan", "deviceVersion");
        snmpParam.setDeviceVersion(deviceVersion);
        getOnuWanFacade(snmpParam.getIpAddress()).modifyOnuWanSsid(snmpParam, onuWanSsid);
        onuWanDao.updateWanSsidAndName(onuWanSsid);
    }

    @Override
    public void updateOnuWanConnect(OnuWanConnect onuWanConnect) {
        Long onuIndex = onuDao.getOnuIndex(onuWanConnect.getOnuId());
        onuWanConnect.setOnuIndex(onuIndex);
        // modify by haojie 广西实网出现的问题，wan连接修改改用set，之前用的delete+add
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(onuWanConnect.getEntityId());
        String deviceVersion = deviceVersionService.getParamValue(onuWanConnect.getEntityId(), "onuWan",
                "deviceVersion");
        snmpParam.setDeviceVersion(deviceVersion);
        getOnuWanFacade(snmpParam.getIpAddress()).modifyOnuWanConnect(snmpParam, onuWanConnect);
        onuWanDao.updateOnuWanConnect(onuWanConnect);
        // 同步刷新wan连接状态
        refreshOnuWanConnect(onuWanConnect.getEntityId(), onuIndex);
        refreshOnuWanConnectStatus(onuWanConnect.getEntityId(), onuWanConnect.getOnuIndex());
    }

    @Override
    public void updateWanPPPoEPassord(OnuWanConnect onuWanConnect) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(onuWanConnect.getEntityId());
        String deviceVersion = deviceVersionService.getParamValue(onuWanConnect.getEntityId(), "onuWan",
                "deviceVersion");
        snmpParam.setDeviceVersion(deviceVersion);
        getOnuWanFacade(snmpParam.getIpAddress()).modifyOnuWanConnect(snmpParam, onuWanConnect);
        onuWanDao.updateOnuWanPassord(onuWanConnect);
        // 同步刷新wan连接状态
        refreshOnuWanConnect(onuWanConnect.getEntityId(), onuWanConnect.getOnuIndex());
        refreshOnuWanConnectStatus(onuWanConnect.getEntityId(), onuWanConnect.getOnuIndex());
    }

    @Override
    public List<Integer> loadBindInterface(Long onuId, Integer connectId) {
        String bindInterface = onuWanDao.loadBindInterface(onuId, connectId);
        List<Integer> bindInterfaceList = new ArrayList<Integer>();
        if (bindInterface != null && bindInterface.length() > 0) {
            bindInterfaceList = EponUtil.getWanBandIfList(bindInterface);
        }
        return bindInterfaceList;
    }

    @Override
    public List<Integer> loadAlreadyBandInterface(Long onuId, Integer connectId) {
        List<String> alreadyBandInterface = onuWanDao.loadAlreadyBindInterface(onuId, connectId);
        List<Integer> alreadyBandInterfaceList = new ArrayList<Integer>();
        for (String bandInterface : alreadyBandInterface) {
            if (bandInterface != null && bandInterface.length() > 0) {
                List<Integer> tempList = EponUtil.getWanBandIfList(bandInterface);
                alreadyBandInterfaceList.addAll(tempList);
            }
        }
        return alreadyBandInterfaceList;
    }

    @Override
    public void saveBindInterface(Long entityId, Long onuId, Integer connectId, List<Integer> bindInterface) {
        String oltVersion = oltService.getOltSoftVersion(entityId);
        Boolean isNewMib = DeviceFuctionSupport.compareVersion(oltVersion, "PN8600-V1.10.0.0") >= 0;
        String bindInterfaceString = EponUtil.getWanBandIfMibString(bindInterface, isNewMib);
        Long onuIndex = onuDao.getOnuIndex(onuId);
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        String deviceVersion = deviceVersionService.getParamValue(entityId, "onuWan", "deviceVersion");
        snmpParam.setDeviceVersion(deviceVersion);
        getOnuWanFacade(snmpParam.getIpAddress()).modifyBindInterface(snmpParam, onuIndex, connectId,
                bindInterfaceString);
        onuWanDao.saveBindInterface(onuId, connectId, bindInterfaceString);
    }

    @Override
    public List<Integer> loadBindInterface(Long onuId) {
        List<String> bindInterfaces = onuWanDao.loadBindInterface(onuId);
        List<Integer> bindInterfaceAllList = new ArrayList<Integer>();
        for (int i = 0; i < bindInterfaces.size(); i++) {
            if (bindInterfaces.get(i) != null && bindInterfaces.get(i).length() > 0) {
                List<Integer> bindInterfaceList = EponUtil.getWanBandIfList(bindInterfaces.get(i));
                bindInterfaceAllList.addAll(bindInterfaceList);
            }
        }
        return bindInterfaceAllList;
    }

    private OnuWanFacade getOnuWanFacade(String ip) {
        return facadeFactory.getFacade(ip, OnuWanFacade.class);
    }

    @Override
    public void refreshOnuWanConfig(Long entityId) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        String deviceVersion = deviceVersionService.getParamValue(entityId, "onuWan", "deviceVersion");
        snmpParam.setDeviceVersion(deviceVersion);
        List<OnuWanConfig> onuWanConfigList = getOnuWanFacade(snmpParam.getIpAddress()).getOnuWanConfig(snmpParam);
        onuWanDao.batchInsertOrUpdateOnuWanConfig(entityId, onuWanConfigList);
    }

    @Override
    public void refreshOnuWanConfig(Long entityId, Long onuIndex) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        String deviceVersion = deviceVersionService.getParamValue(entityId, "onuWan", "deviceVersion");
        snmpParam.setDeviceVersion(deviceVersion);
        OnuWanConfig onuWanConfig = getOnuWanFacade(snmpParam.getIpAddress()).getOnuWanConfig(snmpParam, onuIndex);
        onuWanDao.batchInsertOrUpdateOnuWanConfig(entityId, onuIndex, onuWanConfig);
    }

    @Override
    public void refreshOnuWanSsid(Long entityId) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        String deviceVersion = deviceVersionService.getParamValue(entityId, "onuWan", "deviceVersion");
        snmpParam.setDeviceVersion(deviceVersion);
        List<OnuWanSsid> onuWanSsids = getOnuWanFacade(snmpParam.getIpAddress()).getOnuWanSsids(snmpParam);
        onuWanDao.batchInsertOrUpdateOnuWanSsid(entityId, onuWanSsids);
    }

    @Override
    public void refreshOnuWanSsid(Long entityId, Long onuIndex) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        refreshOnuWanSsid(snmpParam, entityId, onuIndex);
    }

    @Override
    public void refreshOnuWanSsid(SnmpParam snmpParam, Long entityId, Long onuIndex) {
        String deviceVersion = deviceVersionService.getParamValue(entityId, "onuWan", "deviceVersion");
        snmpParam.setDeviceVersion(deviceVersion);

        List<OnuWanSsid> onuWanSsids = getOnuWanFacade(snmpParam.getIpAddress()).getOnuWanSsids(snmpParam, onuIndex);
        onuWanDao.batchInsertOrUpdateOnuWanSsid(entityId, onuIndex, onuWanSsids);
    }

    @Override
    public void refreshOnuWanConnect(Long entityId) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        String deviceVersion = deviceVersionService.getParamValue(entityId, "onuWan", "deviceVersion");
        snmpParam.setDeviceVersion(deviceVersion);
        List<OnuWanConnect> onuWanConnects = getOnuWanFacade(snmpParam.getIpAddress()).getOnuWanConnects(snmpParam);
        onuWanDao.batchInsertOrUpdateOnuWanConnect(entityId, onuWanConnects);
    }

    @Override
    public void refreshWanConnection(Long entityId, Long onuId) {
        Long onuIndex = onuDao.getOnuIndex(onuId);
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);

        refreshWanConnection(snmpParam, entityId, onuId, onuIndex);
    }

    @Override
    public void refreshWanConnection(SnmpParam snmpParam, Long entityId, Long onuId, Long onuIndex) {
        onuWanDao.deleteOnuWanConnectsByOnuId(onuId);

        this.refreshOnuWanConnect(snmpParam, entityId, onuIndex);
        this.refreshOnuWanConnectStatus(snmpParam, entityId, onuIndex);
    }

    private void refreshOnuWanConnect(Long entityId, Long onuIndex) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);

        refreshOnuWanConnect(snmpParam, entityId, onuIndex);
    }

    private void refreshOnuWanConnect(SnmpParam snmpParam, Long entityId, Long onuIndex) {
        String deviceVersion = deviceVersionService.getParamValue(entityId, "onuWan", "deviceVersion");
        snmpParam.setDeviceVersion(deviceVersion);

        List<OnuWanConnect> onuWanConnects = getOnuWanFacade(snmpParam.getIpAddress()).getOnuWanConnects(snmpParam,
                onuIndex);
        onuWanDao.batchInsertOrUpdateOnuWanConnect(entityId, onuIndex, onuWanConnects);
    }

    @Override
    public void refreshOnuWanConnectStatus(Long entityId) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        String deviceVersion = deviceVersionService.getParamValue(entityId, "onuWan", "deviceVersion");
        snmpParam.setDeviceVersion(deviceVersion);
        List<OnuWanConnectStatus> onuWanConnectStatus = getOnuWanFacade(snmpParam.getIpAddress())
                .getOnuWanConnectStatus(snmpParam);
        onuWanDao.batchInsertOrUpdateOnuWanConnectStatus(entityId, onuWanConnectStatus);
    }

    @Override
    public void refreshOnuWanConnectStatus(Long entityId, Long onuIndex) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);

        refreshOnuWanConnectStatus(snmpParam, entityId, onuIndex);
    }

    private void refreshOnuWanConnectStatus(SnmpParam snmpParam, Long entityId, Long onuIndex) {
        String deviceVersion = deviceVersionService.getParamValue(entityId, "onuWan", "deviceVersion");
        snmpParam.setDeviceVersion(deviceVersion);

        List<OnuWanConnectStatus> onuWanConnectStatus = getOnuWanFacade(snmpParam.getIpAddress())
                .getOnuWanConnectStatus(snmpParam, onuIndex);
        onuWanDao.batchInsertOrUpdateOnuWanConnectStatus(entityId, onuIndex, onuWanConnectStatus);
    }

    @Override
    public void saveWLANConfig(OnuWanConfig onuWanConfig) {
        OnuWanConfig selectedOnuWanConfig = onuWanDao.getOnuWanConfig(onuWanConfig.getOnuId());
        if (selectedOnuWanConfig != null) {
            SnmpParam snmpParam = entityDao.getSnmpParamByEntityId(onuWanConfig.getEntityId());
            String deviceVersion = deviceVersionService.getParamValue(onuWanConfig.getEntityId(), "onuWan",
                    "deviceVersion");
            snmpParam.setDeviceVersion(deviceVersion);
            String workMode = onuWanConfig.getWorkMode();
            onuWanConfig.setWorkMode("0x" + workMode);
            getOnuWanFacade(snmpParam.getIpAddress()).modifyOnuWanConfig(snmpParam, onuWanConfig);
            selectedOnuWanConfig.setChannelId(onuWanConfig.getChannelId());
            selectedOnuWanConfig.setWorkMode(workMode);
            selectedOnuWanConfig.setChannelWidth(onuWanConfig.getChannelWidth());
            selectedOnuWanConfig.setSendPower(onuWanConfig.getSendPower());
            selectedOnuWanConfig.setWanEnnable(onuWanConfig.getWanEnnable());
            onuWanDao.updateOnuWanConfig(selectedOnuWanConfig);
        }
    }

    @Override
    public void clearWanConnection(Long onuId, Long entityId) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        String deviceVersion = deviceVersionService.getParamValue(entityId, "onuWan", "deviceVersion");
        snmpParam.setDeviceVersion(deviceVersion);
        Long onuIndex = onuDao.getOnuIndex(onuId);
        getOnuWanFacade(snmpParam.getIpAddress()).clearWanConnect(snmpParam, onuIndex);
        onuWanDao.deleteOnuWanConnectsByOnuId(onuId);
    }

    @Override
    public void restoreOnu(Long onuId, Long entityId) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        String deviceVersion = deviceVersionService.getParamValue(entityId, "onuWan", "deviceVersion");
        snmpParam.setDeviceVersion(deviceVersion);
        Long onuIndex = onuDao.getOnuIndex(onuId);
        getOnuWanFacade(snmpParam.getIpAddress()).restoreWifiOnu(snmpParam, onuIndex);
    }

}
