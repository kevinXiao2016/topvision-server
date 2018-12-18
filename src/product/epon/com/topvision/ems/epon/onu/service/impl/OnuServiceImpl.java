/***********************************************************************
 * $Id: OnuServiceImpl.java,v1.0 2013-10-25 上午11:15:46 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.onu.service.impl;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.epon.domain.OltOnuTypeInfo;
import com.topvision.ems.epon.domain.Onu;
import com.topvision.ems.epon.exception.SetValueConflictException;
import com.topvision.ems.epon.fault.trap.OltTrapConstants;
import com.topvision.ems.epon.olt.facade.OltFacade;
import com.topvision.ems.epon.olt.service.OltService;
import com.topvision.ems.epon.onu.dao.OnuDao;
import com.topvision.ems.epon.onu.dao.UniDao;
import com.topvision.ems.epon.onu.domain.CC8800ABaseInfo;
import com.topvision.ems.epon.onu.domain.OltOnuAttribute;
import com.topvision.ems.epon.onu.domain.OltOnuCapability;
import com.topvision.ems.epon.onu.domain.OltOnuCatv;
import com.topvision.ems.epon.onu.domain.OltOnuPonAttribute;
import com.topvision.ems.epon.onu.domain.OltOnuRstp;
import com.topvision.ems.epon.onu.domain.OltOnuVoip;
import com.topvision.ems.epon.onu.domain.OltTopOnuCapability;
import com.topvision.ems.epon.onu.domain.OltUniAttribute;
import com.topvision.ems.epon.onu.domain.OltUniExtAttribute;
import com.topvision.ems.epon.onu.domain.OnuBaseInfo;
import com.topvision.ems.epon.onu.domain.OnuEnviEnum;
import com.topvision.ems.epon.onu.domain.TopGponOnuSpeed;
import com.topvision.ems.epon.onu.domain.TopOnuSpeed;
import com.topvision.ems.epon.onu.facade.OnuFacade;
import com.topvision.ems.epon.onu.facade.UniFacade;
import com.topvision.ems.epon.onu.service.OnuService;
import com.topvision.ems.epon.optical.domain.OnuPonOptical;
import com.topvision.ems.epon.optical.service.OltOpticalService;
import com.topvision.ems.epon.performance.service.OnuPerfService;
import com.topvision.ems.epon.topology.event.OnuSynchronizedEvent;
import com.topvision.ems.epon.topology.event.OnuSynchronizedListener;
import com.topvision.ems.epon.topology.facade.OltDiscoveryFacade;
import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.facade.domain.EntityType;
import com.topvision.ems.fault.message.TrapEvent;
import com.topvision.ems.fault.message.TrapListener;
import com.topvision.ems.gpon.onu.facade.GponOnuFacade;
import com.topvision.ems.gpon.onuauth.GponConstant;
import com.topvision.ems.network.dao.EntityDao;
import com.topvision.ems.network.domain.EntitySnap;
import com.topvision.ems.network.service.DiscoveryService;
import com.topvision.ems.network.service.EntityService;
import com.topvision.ems.template.service.EntityTypeService;
import com.topvision.framework.common.NumberUtils;
import com.topvision.framework.service.BaseService;
import com.topvision.framework.snmp.SnmpParam;
import com.topvision.framework.snmp.Trap;
import com.topvision.framework.utils.EponConstants;
import com.topvision.framework.utils.EponIndex;
import com.topvision.nbi.tl1.api.domain.OnuInfo;
import com.topvision.platform.facade.FacadeFactory;
import com.topvision.platform.message.event.EntityEvent;
import com.topvision.platform.message.event.EntityListener;
import com.topvision.platform.message.service.MessageService;
import com.topvision.platform.zetaframework.var.UnitConfigConstant;

/**
 * @author flack
 * @created @2013-10-25-上午11:15:46
 * 
 */
@Service("onuService")
public class OnuServiceImpl extends BaseService
        implements OnuService, TrapListener, EntityListener, OnuSynchronizedListener {
    @Autowired
    private EntityDao entityDao;
    @Autowired
    private OnuDao onuDao;
    @Autowired
    private UniDao uniDao;
    @Autowired
    private EntityService entityService;
    @Autowired
    private FacadeFactory facadeFactory;
    @Autowired
    private MessageService messageService;
    @Autowired
    private EntityTypeService entityTypeService;
    @Autowired
    private OltOpticalService oltOpticalService;
    @Autowired
    private OnuPerfService onuPerfService;
    @Autowired
    private OltService oltService;
    @SuppressWarnings("rawtypes")
    @Autowired
    private DiscoveryService discoveryService;
    private ExecutorService executorService;

    public static final Integer ONU_SINGLE_TOPO = 1;

    private Set<Long> trapSet = new HashSet<>();

    @Override
    public void destroy() {
        super.destroy();
        messageService.removeListener(TrapListener.class, this);
        messageService.removeListener(EntityListener.class, this);
        messageService.removeListener(OnuSynchronizedListener.class, this);
    }

    @Override
    @PostConstruct
    public void initialize() {
        super.initialize();
        messageService.addListener(TrapListener.class, this);
        messageService.addListener(EntityListener.class, this);
        messageService.addListener(OnuSynchronizedListener.class, this);
        this.executorService = Executors.newFixedThreadPool(10);

        Thread onuTrapRefreshThread = new Thread(new Runnable() {

            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(60000);
                    } catch (InterruptedException e) {
                    }

                    try {
                        for (final Long entityId : trapSet) {
                            executorService.execute(new Runnable() {

                                @Override
                                public void run() {
                                    discoveryService.refresh(entityId);
                                }
                            });
                        }
                    } catch (Exception e) {
                        logger.error("OnuTrapRefresh error:", e);
                    } finally {
                        trapSet.clear();
                    }

                }
            }
        });

        onuTrapRefreshThread.start();

        // 解决EMS-14789 的bug，将拓扑的时候已经离线的CC lastDegeristerTime设置为当前时间
        List<OltOnuAttribute> offlineCmcs = onuDao.getAllCmcForAutoClean();

        for (Iterator<OltOnuAttribute> iterator = offlineCmcs.iterator(); iterator.hasNext();) {
            OltOnuAttribute offlineCmc = iterator.next();
            if (offlineCmc.getLastDeregisterTime() == null) {
                offlineCmc.setLastDeregisterTime(new Timestamp(new Date().getTime()));
            } else {
                iterator.remove();
            }
        }
        if (offlineCmcs.size() > 0) {
            onuDao.updateOnuLastDeregisterTime(offlineCmcs);
        }
    }

    @Override
    public void insertEntityStates(OnuSynchronizedEvent event) {
        Long entityId = event.getEntityId();
        List<Long> onuIndexList = event.getOnuIndexList();
        try {
            if (onuIndexList.size() == ONU_SINGLE_TOPO) {
                // 当刷新单个onu，用gettableline
                refreshOltOnuCapatilityExt(entityId, onuIndexList.get(0));
            } else if (onuIndexList.size() > ONU_SINGLE_TOPO) {
                // 刷新多个onu，一般是刷新olt的流程，用gettable
                refreshOltOnuCapatilityExt(entityId);
            }
            logger.info("refreshOltOnuCapatilityExt finish");
        } catch (Exception e) {
            logger.error("refreshOltOnuCapatilityExt wrong", e);
        }
    }

    @Override
    @PreDestroy
    public List<EntityType> loadOnuTypes() throws SQLException {
        Long onuType = entityTypeService.getOnuType();
        return onuDao.getOnuTypes(onuType);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Onu getOnuStructure(Long onuId) {
        Onu onu = onuDao.getOnuStructure(onuId);
        Collections.sort(onu.getOnuUniPortList());
        return onu;
    }

    @Override
    public List<OltOnuAttribute> getOnuList(Long entityId, Long slotId, Long ponId, Integer llid, String mac,
            String onuName) {
        List<OltOnuAttribute> onuList = onuDao.getOnuList(entityId, slotId, ponId, llid, mac, onuName);
        return onuList;
    }

    /**
     * 获取ONU的列表及使能状态 return onuNo、onuId、15minStat、24hourStat、temperatureStat
     */
    @Override
    public List<Map<String, Long>> getOnuStatList(Long portId) {
        List<Map<String, Long>> tmpData = new ArrayList<Map<String, Long>>();
        List<OltOnuAttribute> onuListTmp = onuDao.getOnuListByPonId(portId);
        if (onuListTmp.size() > 0) {
            for (OltOnuAttribute onu : onuListTmp) {
                Onu onuStat = onuDao.getOnuStructure(onu.getOnuId());
                Map<String, Long> tmp = new HashMap<String, Long>();
                if (onuStat != null) {
                    tmp.put("num", EponIndex.getOnuNo(onu.getOnuIndex()));
                    tmp.put("id", onu.getOnuId());
                    Long stat = 0L;
                    if (EponConstants.ABALITY_ENABLE.equals(onuStat.getPonPerfStats15minuteEnable())) {
                        stat += 1L;
                    }
                    if (EponConstants.ABALITY_ENABLE.equals(onuStat.getPonPerfStats24hourEnable())) {
                        stat += 2L;
                    }
                    if (EponConstants.ABALITY_ENABLE.equals(onuStat.getTemperatureDetectEnable())) {
                        stat += 4L;
                    }
                    tmp.put("stat", stat);
                }
                tmpData.add(tmp);
            }
        }
        return tmpData;
    }

    @Override
    public OltOnuAttribute getOnuAttribute(Long onuId) {
        OltOnuAttribute onuAttribute = onuDao.getOnuEntityById(onuId);
        onuAttribute.setOnuIndex(onuDao.getOnuIndex(onuId));
        return onuAttribute;
    }

    @Override
    public void updateOnuAttribute(OltOnuAttribute onuAttribute) {
        onuDao.updateOnuAttribute(onuAttribute);
    }

    @Override
    public OltOnuPonAttribute getOnuPonAttribute(Long onuPonId) {
        return onuDao.getOnuPonAttribute(onuPonId);
    }

    @Override
    public OltUniAttribute getOnuUniAttribute(Long uniId) {
        return uniDao.getOnuUniAttribute(uniId);
    }

    @Override
    public void updateOnuUniAttribute(OltUniAttribute uniAttribute) {
        uniDao.updateOnuUniAttribute(uniAttribute);
    }

    @Override
    public void resetOnu(Long entityId, Long onuId) {
        OltOnuAttribute onuAttribute = this.getOnuAttribute(onuId);
        Long onuIndex = onuDao.getOnuIndex(onuId);
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        if (GponConstant.GPON_ONU.equals(onuAttribute.getOnuEorG())) {
            getGponOnuFacade(snmpParam.getIpAddress()).resetOnu(snmpParam, onuIndex);
        } else {
            getOnuFacade(snmpParam.getIpAddress()).resetOnu(snmpParam, onuIndex);
        }
    }

    @Override
    public void deregisterOnu(Long entityId, Long onuId) {
        Long onuIndex = onuDao.getOnuIndex(onuId);
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        getOnuFacade(snmpParam.getIpAddress()).deregisterOnu(snmpParam, onuIndex);
        // 在解注册成功后进行为ONU性能采集任务的删除
        /*
         * OnuEntityEvent onu = new OnuEntityEvent(entityId); onu.setEntityId(entityId);
         * onu.setOnuId(onuId); onu.setActionName("onuRemoved");
         * onu.setListener(OnuEntityListener.class); messageService.fireMessage(onu);
         */
        onuPerfService.stopOnuPerfCollect(onuId, snmpParam);
    }

    @Override
    public void setOnuAdminStatus(Long entityId, Long onuId, Integer onuAdminStatus) {
        OltOnuAttribute onuAttribute = this.getOnuAttribute(onuId);
        Long onuIndex = onuDao.getOnuIndex(onuId);
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        if (GponConstant.GPON_ONU.equals(onuAttribute.getOnuEorG())) {
            getGponOnuFacade(snmpParam.getIpAddress()).setOnuAdminStatus(snmpParam, onuIndex, onuAdminStatus);
        } else {
            getOnuFacade(snmpParam.getIpAddress()).setOnuAdminStatus(snmpParam, onuIndex, onuAdminStatus);
        }
        onuDao.updateOnuAdminStatus(onuId, onuAdminStatus);
    }

    @Override
    public String modifyOnuName(Long entityId, Long onuId, String onuName) {
        Long onuIndex = onuDao.getOnuIndex(onuId);
        onuDao.updateOnuName(onuId, onuName);
        /**
         * modified by huangdongsheng 更行entity中的name，供拓扑图和google地图使用
         */
        onuDao.updateOnuEntityName(onuId, onuName);
        String onuIndexString = EponIndex.getPortIndex(onuIndex);
        return onuIndexString;
    }

    @Override
    public void setOnuVoipEnable(Long entityId, Long onuId, Integer onuVoipEnable) {
        Long onuIndex = onuDao.getOnuIndex(onuId);
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        Integer newStatus = getOnuFacade(snmpParam.getIpAddress()).setOnuVoipEnable(snmpParam, onuIndex, onuVoipEnable);
        if (newStatus == null) {
            throw new SetValueConflictException("Business.connection");
        } else {
            if (!newStatus.equals(onuVoipEnable)) {
                throw new SetValueConflictException("Business.setOnuVoipEnable");
            }
        }
    }

    @Override
    public void setOnuTemperatureDetectEnable(Long entityId, Long onuId, Integer onuTemperatureDetectEnable) {
        Long onuIndex = onuDao.getOnuIndex(onuId);
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        Integer newStatus = getOnuFacade(snmpParam.getIpAddress()).setOnuTemperatureDetectEnable(snmpParam, onuIndex,
                onuTemperatureDetectEnable);
        if (newStatus == null) {
            throw new SetValueConflictException("Business.connection");
        } else {
            onuDao.updateOnuTemperatureDetectEnable(onuId, onuTemperatureDetectEnable);
            if (!newStatus.equals(onuTemperatureDetectEnable)) {
                throw new SetValueConflictException("Business.setOnuTemperatureDetectEnable");
            }
        }
    }

    @Override
    public void setOnuFecEnable(Long entityId, Long onuId, Integer onuFecEnable) {
        Long onuIndex = onuDao.getOnuIndex(onuId);
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        Integer newStatus = getOnuFacade(snmpParam.getIpAddress()).setOnuFecEnable(snmpParam, onuIndex, onuFecEnable);
        if (newStatus == null) {
            throw new SetValueConflictException("Business.connection");
        } else {
            onuDao.updateOnuFecEnable(onuId, onuFecEnable);
            if (!newStatus.equals(onuFecEnable)) {
                throw new SetValueConflictException("Business.setOnuFecEnable");
            }
        }
    }

    @Override
    public void configOnuIsolationEnable(Long entityId, Long onuId, Integer onuIsolationEnable) {
        Long onuIndex = onuDao.getOnuIndex(onuId);
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        Integer newStatus = getOnuFacade(snmpParam.getIpAddress()).setOnuIsolationEnable(snmpParam, onuIndex,
                onuIsolationEnable);
        if (newStatus == null) {
            throw new SetValueConflictException("Business.connection");
        } else {
            onuDao.updateOnuIsolationEnable(onuId, onuIsolationEnable);
            if (!newStatus.equals(onuIsolationEnable)) {
                throw new SetValueConflictException("Business.setOnuIsolationEnable");
            }
        }
    }

    @Override
    public void setOnu15minEnable(Long entityId, Long onuId, Integer onu15minEnable) {
        OltOnuAttribute onuAttribute = this.getOnuAttribute(onuId);
        Long onuIndex = onuDao.getOnuIndex(onuId);
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        if (GponConstant.GPON_ONU.equals(onuAttribute.getOnuEorG())) {
            getGponOnuFacade(snmpParam.getIpAddress()).setOnu15minEnable(snmpParam, onuIndex, onu15minEnable);
        } else {
            getOnuFacade(snmpParam.getIpAddress()).setOnu15minEnable(snmpParam, onuIndex, onu15minEnable);
        }
        onuDao.updateOnu15minEnable(onuId, onu15minEnable);
    }

    @Override
    public void updateEntityOnu15minEnable(List<OltOnuPonAttribute> onuPonAttributes) {
        for (OltOnuPonAttribute onuPonPort : onuPonAttributes) {
            setOnu15minEnable(onuPonPort.getEntityId(), onuPonPort.getOnuPonId(), 1);
        }
    }

    @Override
    public void setOnu24hEnable(Long entityId, Long onuId, Integer onu24hEnable) {
        OltOnuAttribute onuAttribute = this.getOnuAttribute(onuId);
        Long onuIndex = onuDao.getOnuIndex(onuId);
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        if (GponConstant.GPON_ONU.equals(onuAttribute.getOnuEorG())) {
            getGponOnuFacade(snmpParam.getIpAddress()).setOnu24hEnable(snmpParam, onuIndex, onu24hEnable);
        } else {
            getOnuFacade(snmpParam.getIpAddress()).setOnu24hEnable(snmpParam, onuIndex, onu24hEnable);
        }
        onuDao.updateOnu24hEnable(onuId, onu24hEnable);
    }

    @Override
    public void setOnuCatvEnable(Long entityId, Long onuId, Integer onuCatvEnable) {
        Long onuIndex = onuDao.getOnuIndex(onuId);
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        Integer newStatus = getOnuFacade(snmpParam.getIpAddress()).setOnuCatvEnable(snmpParam, onuIndex, onuCatvEnable);
        if (newStatus == null) {
            throw new SetValueConflictException("Business.connection");
        } else {
            if (!newStatus.equals(onuCatvEnable)) {
                throw new SetValueConflictException("Business.setOnuCatvEnable");
            }
        }
    }

    @Override
    public void setOnuMacMaxNum(Long entityId, Long onuId, Integer onuMacMaxNum) {
        Long onuIndex = onuDao.getOnuIndex(onuId);
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        Integer newNum = getOnuFacade(snmpParam.getIpAddress()).setOnuMacMaxNum(snmpParam, onuIndex, onuMacMaxNum);
        if (newNum == null) {
            throw new SetValueConflictException("Business.connection");
        } else {
            onuDao.updateOnuMacMaxNum(onuId, onuMacMaxNum);
            if (!newNum.equals(onuMacMaxNum)) {
                throw new SetValueConflictException("Business.setOnuMacMaxNum");
            }
        }
    }

    @Override
    public void setOnuRstpBridgeMode(Long entityId, Long onuId, Integer onuRstpBridgeMode) {
        Long onuIndex = onuDao.getOnuIndex(onuId);
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        Integer newMode = getOnuFacade(snmpParam.getIpAddress()).setOnuRstpBridgeMode(snmpParam, onuIndex,
                onuRstpBridgeMode);
        if (newMode == null) {
            throw new SetValueConflictException("Business.connection");
        } else {
            onuDao.updateOnuRstpBridgeMode(onuId, onuRstpBridgeMode);
            if (!newMode.equals(onuRstpBridgeMode)) {
                throw new SetValueConflictException("Business.setOnuRstpBridgeMode");
            }
        }
    }

    @Override
    public OltOnuPonAttribute getOnuPonAttributeByOnuId(Long onuId) {
        OltOnuPonAttribute onuPonAttribute = onuDao.getOnuPonAttributeByOnuId(onuId);
        if (onuPonAttribute != null) {
            // 温度单位转换
            String tempUnit = (String) UnitConfigConstant.get(UnitConfigConstant.TEMP_UNIT);
            if (onuPonAttribute.getOnuWorkingTemperature() != null
                    && EponConstants.OPT_TEMP != onuPonAttribute.getOnuWorkingTemperature()) {
                String temperature;
                if (tempUnit.equalsIgnoreCase(UnitConfigConstant.CENTI_TEMP_UNIT)) {
                    temperature = NumberUtils.TWODOT_FORMAT.format(onuPonAttribute.getOnuWorkingTemperature() / 100F)
                            + " " + tempUnit;
                } else {
                    temperature = NumberUtils.TWODOT_FORMAT
                            .format(UnitConfigConstant.transCentiToF(onuPonAttribute.getOnuWorkingTemperature() / 100F))
                            + " " + tempUnit;
                }
                onuPonAttribute.setWorkTempStr(temperature);
            } else {
                onuPonAttribute.setWorkTempStr("--");
            }
        }
        return onuPonAttribute;
    }

    @Override
    public OltOnuCapability getOltOnuCapabilityByOnuId(Long onuId) {
        return onuDao.getOltOnuCapability(onuId);
    }

    @Override
    public OltTopOnuCapability getOltTopOnuCapabilityByOnuId(Long onuId) {
        return onuDao.getOltTopOnuCapability(onuId);
    }

    @Override
    public OltOnuRstp getOltOnuRstpByOnuId(Long onuId) {
        return onuDao.getOltOnuRstpByOnuId(onuId);
    }

    /**
     * 刷新ONU温度
     * 
     * @param entityId
     * @param onuId
     */
    @Override
    public Integer refreshOnuTemperature(Long entityId, Long onuId) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        Long onuIndex = onuDao.getOnuIndex(onuId);
        Integer onuTemp = getOnuFacade(snmpParam.getIpAddress()).getOnuTemperature(snmpParam, onuIndex);
        if (onuTemp != null) {
            onuDao.updateOnuTemperature(onuId, onuTemp);
        }
        return onuTemp;
    }

    @Override
    public OltOnuTypeInfo getOnuTypeInfo(Integer onuTypeId) {
        return onuDao.getOnuTypeInfo(onuTypeId);
    }

    @Override
    public void moveToTopoFromOnuView(Long onuId, Entity entity) {
        // 建立设备与拓扑图关系
        onuDao.insertOnuToEntity(entity);
        // 建立设备与拓扑图关系 deprecated
        // oltDao.insertOnuEntityProductRelation(onuId, entity.getEntityId(), entity.getTypeId());
        // return entityId;
    }

    @Override
    public List<OltOnuAttribute> getOnuDeviceListItem(Map<String, Object> map) {
        return onuDao.getOnuDeviceListItem(map);
    }

    @Override
    public List<OltOnuAttribute> getOnuList(Map<String, Object> paramMap) {
        return onuDao.getOnuList(paramMap);
    }

    private OnuFacade getOnuFacade(String ip) {
        return facadeFactory.getFacade(ip, OnuFacade.class);
    }

    private GponOnuFacade getGponOnuFacade(String ip) {
        return facadeFactory.getFacade(ip, GponOnuFacade.class);
    }

    private OltFacade getOltFacade(String ip) {
        return facadeFactory.getFacade(ip, OltFacade.class);
    }

    /**
     * 获取OltDiscoveryFacade对象
     * 
     * @param ip
     *            被采集设备IP
     * @return OltDiscoveryFacade
     */
    @SuppressWarnings("unused")
    private OltDiscoveryFacade getOltDiscoveryFacade(String ip) {
        return facadeFactory.getFacade(ip, OltDiscoveryFacade.class);
    }

    @Override
    public int getOnuListCount(Map<String, Object> paramMap) {
        return onuDao.getOnuListCount(paramMap);
    }

    @Override
    public Long getOnuIdByIndex(Long entityId, Long onuIndex) throws Exception {
        return onuDao.getOnuIdByIndex(entityId, onuIndex);
    }

    @Override
    public List<String> getOnuHwList(Long entityId) {
        return onuDao.getOnuHwList(entityId);
    }

    @Override
    public List<OltOnuAttribute> getOnuListByPonId(Long ponId) {
        return onuDao.getOnuListByPonId(ponId);
    }

    /*
     * @Override public void refreshOnuInfo(Long entityId) { Entity entity =
     * entityDao.selectByPrimaryKey(entityId); List<OltOnuAttribute> onuList = new
     * ArrayList<OltOnuAttribute>(); SnmpParam snmpParam = new SnmpParam(); HashMap<Long, Long>
     * oltMap = (HashMap<Long, Long>) oltDao.getOltMap(entityId); snmpParam =
     * entityService.getSnmpParamByEntity(entityId); OltDiscoveryData onuData =
     * getOltDiscoveryFacade(snmpParam.getIpAddress()) .discoveryOnuByOlt(snmpParam, onuList);
     * List<OltOnuAttribute> deleteOnuList = onuDao.getOnuList(entityId, null, null, null, null,
     * null); // 获得需要删除的ONU列表 deleteOnuList.removeAll(onuData.getOnus()); // 获得需要新增的ONU列表 //
     * 处理ONU删除需要的业务 for (OltOnuAttribute deleteOnuAttribute : deleteOnuList) { if
     * (deleteOnuAttribute.getOnuPreType() != null && deleteOnuAttribute.getOnuPreType().equals(""))
     * { // 为具有独立IP的ONU进行预留 } else if (deleteOnuAttribute.getOnuPreType() != null &&
     * deleteOnuAttribute.getOnuPreType().equals(OltOnuAttribute.ONU_TYPE_CCMTS)) { CmcEntityEvent
     * cmc = new CmcEntityEvent(deleteOnuAttribute); cmc.setActionName("cmcRemoved");
     * cmc.setOnuIndex(deleteOnuAttribute.getOnuIndex());
     * cmc.setOnuId(oltMap.get(deleteOnuAttribute.getOnuIndex())); cmc.setEntityId(entityId);
     * cmc.setListener(CmcEntityListener.class); messageService.fireMessage(cmc); } }
     * List<OltOnuAttribute> cmcList = new ArrayList<OltOnuAttribute>(); for (OltOnuAttribute
     * onuAttribute : onuData.getOnus()) { if (onuAttribute.getOnuPreType() != null) {
     * onuAttribute.setEntityType(entityTypeService.getEntityType(onuAttribute.getOnuPreType())); if
     * (onuAttribute.getOnuPreType().equals(OltOnuAttribute.ONU_TYPE_CCMTS)) { try { EntityType
     * entityType = getCmcEntityType(
     * EponIndex.getCmcIndexByOnuMibIndex(onuAttribute.getOnuMibIndex()), entity.getEntityId()); if
     * (entityType != null) { onuAttribute.setEntityType(entityType); cmcList.add(onuAttribute); } }
     * catch (Exception e) { // 默认赋值为8800A
     * onuAttribute.setEntityType(entityTypeService.getEntityType(entityTypeService
     * .getCcmts8800AType())); cmcList.add(onuAttribute); } } } } // 数据库层保证数据一致 //
     * 保证在ONU删除的时候先关闭业务后删除数据库记录，而在新增的时候先新增数据记录后开启业务
     * onuDao.batchInsertOnuAttribute(onuData.getOnus(), oltMap, entity); // 处理ONU更新与新增两种情况 for
     * (OltOnuAttribute cmcAttribute : cmcList) { CmcEntityEvent cmc = new
     * CmcEntityEvent(cmcAttribute); cmc.setActionName("cmcAdded");
     * cmc.setOnuIndex(cmcAttribute.getOnuIndex());
     * cmc.setOnuId(oltMap.get(cmcAttribute.getOnuIndex())); cmc.setEntityId(entityId);
     * cmc.setListener(CmcEntityListener.class); messageService.fireMessage(cmc); } // 针对CMC的业务消息 if
     * (cmcList.size() > 0) { CmcBusinessEvent cmcBusiness = new CmcBusinessEvent(onuData);
     * cmcBusiness.setActionName("cmcBusinessAdded");// 获取无法单个采集的cc信息（qos，cm，cpe）
     * cmcBusiness.setEntityId(entityId); cmcBusiness.setListener(CmcBusinessListener.class);
     * messageService.addMessage(cmcBusiness); } if (onuData.getOnuPons() != null) {
     * onuDao.batchInsertOnuPonAttribute(onuData.getOnuPons(), oltMap); } if (onuData.getUnis() !=
     * null) { uniDao.batchInsertUniAttribute(onuData.getUnis(), oltMap); } if
     * (onuData.getUniExtAttributes() != null) {
     * uniDao.batchInsertOltUniExtAttribute(onuData.getUniExtAttributes()); } if
     * (onuData.getOnuCapabilities() != null) {
     * onuDao.batchUpdateOltOnuCapatility(onuData.getOnuCapabilities(),
     * onuData.getTopOnuCapabilities(), onuData.getOnuCatvs(), onuData.getOnuRstps(),
     * onuData.getOnuVoips()); } if (onuData.getUniPortRateLimits() != null) {
     * uniDao.batchInsertOltUniRateLimit(onuData.getUniPortRateLimits()); } // 刷新认证表以及阻塞表
     * 为了删除不存在的ONU List<OltAuthentication> oList = new ArrayList<OltAuthentication>(); if
     * (onuData.getAuthenMacInfos() != null) { for (OltAuthenMacInfo mac :
     * onuData.getAuthenMacInfos()) { OltAuthentication oltAuthentication = new
     * OltAuthentication(mac); oList.add(oltAuthentication); } } if (onuData.getAuthenSnInfos() !=
     * null) { for (OltAuthenSnInfo sn : onuData.getAuthenSnInfos()) { OltAuthentication
     * oltAuthentication = new OltAuthentication(sn); oList.add(oltAuthentication); } } if (oList !=
     * null) { onuAuthDao.insertOrUpdateOltAuthentication(oList, entityId); } // ONU 认证 block表
     * List<OltOnuBlockAuthen> newBlockList = onuData.getOnuBlockAuthens(); if (newBlockList !=
     * null) { try { onuAuthDao.batchInsertOltOnuBlockAuth(newBlockList); } catch (Exception e) {
     * logger.error("batchInsertOltOnuBlockAuth", e); } } // UNI风暴抑制 if
     * (onuData.getUniStormSuppressionEntries() != null) { try {
     * uniDao.batchInsertOltUniStormInfo(onuData.getUniStormSuppressionEntries(), entityId); } catch
     * (Exception e) { logger.error("", e); } } if (onuData.getOnuOpticals() != null) {
     * oltOpticalDao.batchInsertOnuPonOptical(onuData.getOnuOpticals(), entityId); } //
     * 发送消息刷新ONU的业务数据 // （所有实现接口的Service判断类型是否为OnuManagement，data中的数据包括需要刷新的ONU） SynchronizedEvent
     * synchronizedEvent = new SynchronizedEvent(this);
     * synchronizedEvent.setAction(SynchronizedEvent.ADD_SYNCHRONIZED);
     * synchronizedEvent.setActionName("insertEntityStates");
     * synchronizedEvent.setEntityId(entityId); synchronizedEvent.setEventType("OnuManagement");
     * synchronizedEvent.setData(onuData);
     * synchronizedEvent.setListener(SynchronizedListener.class);
     * messageService.fireMessage(synchronizedEvent); }
     */

    @Override
    public Map<String, String> getAllOnuMacAndIndex(Long entityId) {
        Map<Long, String> sMap = onuDao.getAllOnuMacAndIndex(entityId);
        Map<String, String> rMap = new HashMap<String, String>();
        for (Map.Entry<Long, String> entry : sMap.entrySet()) {
            rMap.put("1.3.6.1.4.1.17409.2.3.4.1.1.7." + EponIndex.getOnuMibIndexByIndex(entry.getKey()),
                    entry.getValue());
        }
        return rMap;
    }

    @Override
    public EntityType getCmcEntityType(Integer onuPreType) {
        Long typeId = EponConstants.SUB_CMC_TYPE.get(onuPreType);
        EntityType entityType = entityTypeService.getEntityType(typeId);
        return entityType;
    }

    @Override
    public EntityType getCmcEntityType(Long cmcIndex, Long entityId) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        String cmcSysObjectId = getOltFacade(snmpParam.getIpAddress()).getCmcEntityType(cmcIndex, snmpParam);
        EntityType entityType = entityTypeService.getEntityTypeBySysObjId(cmcSysObjectId);
        return entityType;
    }

    @Override
    public EntityType getCmcEntityType(Long cmcIndex, Long entityId, Integer standard) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        String cmcSysObjectId = getOltFacade(snmpParam.getIpAddress()).getCmcEntityType(cmcIndex, snmpParam);
        EntityType entityType = entityTypeService.getEntityTypeBySysObjId(cmcSysObjectId, standard);
        return entityType;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.platform.message.event.EntityListener#entityAdded(com.topvision.platform.
     * message .event.EntityEvent)
     */
    @Override
    public void entityAdded(EntityEvent event) {
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.platform.message.event.EntityListener#entityDiscovered(com.topvision.platform
     * .message.event.EntityEvent)
     */
    @Override
    public void entityDiscovered(EntityEvent event) {
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.platform.message.event.EntityListener#attributeChanged(long,
     * java.lang.String[], java.lang.String[])
     */
    @Override
    public void attributeChanged(long entityId, String[] attrNames, String[] attrValues) {
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.platform.message.event.EntityListener#entityChanged(com.topvision.platform.
     * message.event.EntityEvent)
     */
    @Override
    public void entityChanged(EntityEvent event) {
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.platform.message.event.EntityListener#entityRemoved(com.topvision.platform.
     * message.event.EntityEvent)
     */
    @Override
    public void entityRemoved(EntityEvent event) {
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.platform.message.event.EntityListener#managerChanged(com.topvision.platform
     * .message.event.EntityEvent)
     */
    @Override
    public void managerChanged(EntityEvent event) {

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.fault.message.TrapListener#onTrapMessage(com.topvision.ems.fault.message
     * .TrapEvent)
     */
    @Override
    public void onTrapMessage(TrapEvent evt) {
        Trap trap = evt.getTrap();
        if (OltTrapConstants.ONU_EVENT_OFFLINE.contains(evt.getCode())
                || OltTrapConstants.ONU_EVENT_ONLINE.contains(evt.getCode())
                || evt.getCode().equals(OltTrapConstants.ONU_DELETE)) {
            doOnu(evt.getCode(), trap, evt);
        }
    }

    /**
     * @param code
     * @param trap
     */
    private void doOnu(Integer code, Trap trap, TrapEvent trapEvent) {
        Entity entity = entityDao.getEntityByIp(trap.getAddress());
        // String[] instance =
        // trap.getVarialbleValue("1.3.6.1.4.1.17409.2.2.11.1.2.1.0").split(":");
        // Long index = EponIndex.getIndex(instance);
        /*
         * String[] sources = trapEvent.getSource().split(":"); String[] instance = null; if
         * (sources.length > 1) { // 兼容新版的告警处理情况 instance = sources[1].split("/"); } else { instance
         * = sources[0].split("/"); } // String instance[] = trapEvent.getSource().split("/"); Long
         * index = EponIndex.getOnuIndex(instance[0], instance[1], instance[2]);
         */
        Long index = trapEvent.getDeviceIndex();
        if (OltTrapConstants.ONU_EVENT_OFFLINE.contains(code)) {
            // 只需要在ONU下线的时候更新相应ONU的在线状态
            OltOnuAttribute onu = onuDao.getOnuAttributeByIndex(entity.getEntityId(), index);
            if (onu != null) {
                // onu.setOnuOperationStatus(EponConstants.ONU_STATUS_DOWN);
                // onuDao.insertOrUpdateOltOnuAttribute(onu);
                onuDao.updateOnuOperationStatus(onu.getOnuId(), EponConstants.ONU_STATUS_DOWN);
                // 更新entitysnap表中onu的在线状态
                EntitySnap onuSnap = new EntitySnap();
                onuSnap.setEntityId(onu.getOnuId());
                onuSnap.setState(false);
                entityDao.updateOnuEntitySnap(onuSnap);
            }
        } else if (OltTrapConstants.ONU_EVENT_ONLINE.contains(code)) {
            // @Modify by Rod ONU的上线告警只对存在的ONU设置上线状态，不再进行ONU的刷新操作
            OltOnuAttribute onu = onuDao.getOnuAttributeByIndex(entity.getEntityId(), index);
            if (onu != null) {
                // onu.setOnuOperationStatus(EponConstants.ONU_STATUS_UP);
                // onuDao.insertOrUpdateOltOnuAttribute(onu);
                onuDao.updateOnuOperationStatus(onu.getOnuId(), EponConstants.ONU_STATUS_UP);
                // 更新entitysnap表中onu的在线状态
                EntitySnap onuSnap = new EntitySnap();
                onuSnap.setEntityId(onu.getOnuId());
                onuSnap.setState(true);
                entityDao.updateOnuEntitySnap(onuSnap);
            } else {
                trapSet.add(entity.getEntityId());
            }
        } else if (code.equals(OltTrapConstants.ONU_DELETE)) {
            // 在ONU和CC解绑定时(删除)时将网管数据删除
            OltOnuAttribute onu = onuDao.getOnuAttributeByIndex(entity.getEntityId(), index);
            if (onu != null) {
                // 解绑定成功后发消息进行删除设备的相关操作
                List<Long> entityIds = new ArrayList<>();
                entityIds.add(onu.getOnuId());
                entityService.removeEntity(entityIds);
            }
        }
    }

    @Override
    public void addOnuDeviceUpTime(Long entityId, Map<String, String> rMap) {
        try {
            Long collectTime = System.currentTimeMillis();
            onuDao.addOnuDeviceUpTime(entityId, rMap, collectTime);
            Thread.sleep(10000);
            onuDao.cleanOnuDeviceUpTime(entityId, rMap, collectTime);
        } catch (InterruptedException e) {
            logger.error("", e);
        }
    }

    @Override
    public List<OltOnuAttribute> getOnuListByEntity(Long entityId) {
        return onuDao.getOnuListByEntity(entityId);
    }

    @Override
    public void updateCC8800ARestartTime(Map<String, Long> attrs) {
        onuDao.updateCC8800ARestartTime(attrs);
    }

    @Override
    public void updateOnuPon15MinStatus(Long entityId) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        List<OltTopOnuCapability> onuAttributes = getOnuFacade(snmpParam.getIpAddress()).getOnuListAttribute(snmpParam);
        for (OltTopOnuCapability onuAttr : onuAttributes) {
            onuAttr.setEntityId(entityId);
        }
        onuDao.batchUpdateOnu15MinStatus(onuAttributes);
    }

    @Override
    public void modifyOnuMacAgeTime(OltUniExtAttribute oltUniExtAttribute, Long onuId) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(oltUniExtAttribute.getEntityId());
        getUniFacade(snmpParam.getIpAddress()).updateOltUniMacAge(snmpParam, oltUniExtAttribute);
        onuDao.modifyOnuMacAgeTime(onuId, oltUniExtAttribute.getMacAge());
    }

    private UniFacade getUniFacade(String ipAddress) {
        return facadeFactory.getFacade(ipAddress, UniFacade.class);
    }

    @Override
    public void refreshOnuBaseInfo(OnuBaseInfo baseInfo) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(baseInfo.getEntityId());
        // 获取ONU基本信息
        baseInfo = this.getOnuFacade(snmpParam.getIpAddress()).getOnuBaseInfo(snmpParam, baseInfo);
        onuDao.updateOnuBaseInfo(baseInfo);
        entityDao.updateEntityNameAndMac(baseInfo.getOnuId(), null, baseInfo.getOnuMac());
        // 获取ONU光功率信息
        OnuPonOptical onuOptical = oltOpticalService.getOnuOpticalInfo(baseInfo.getEntityId(), baseInfo.getOnuIndex());
        if (onuOptical != null) {
            Long onuPonId = onuDao.getOnuPonId(baseInfo.getOnuId());
            onuOptical.setOnuPonId(onuPonId);
            onuDao.updateOnuOpticalInfo(onuOptical);
        }
    }

    @Override
    public void refreshCC8800ABaseInfo(CC8800ABaseInfo caInfo, OnuBaseInfo onuInfo) {
        OltOnuAttribute onuAttribute = getOnuAttribute(onuInfo.getOnuId());
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(caInfo.getEntityId());
        // 刷新CC的基本信息
        caInfo = this.getOnuFacade(snmpParam.getIpAddress()).getCC8800ABaseInfo(snmpParam, caInfo);
        onuDao.updateCC8800AInfo(caInfo);
        entityDao.updateEntityNameAndMac(caInfo.getEntityId(), caInfo.getCmcSysName(), caInfo.getCmcSysMacAddr());
        // 刷新ONU部分的信息
        if (GponConstant.GPON_ONU.equals(onuAttribute.getOnuEorG())) {
            // TODO GPON CCMTS刷新onuattribute需要单独提供方法
        } else {
            onuInfo = this.getOnuFacade(snmpParam.getIpAddress()).getOnuBaseInfo(snmpParam, onuInfo);
            onuDao.updateOnuBaseInfo(onuInfo);
        }
        // 获取光功率信息
        OnuPonOptical onuOptical = oltOpticalService.getOnuOpticalInfo(onuInfo.getEntityId(), onuInfo.getOnuIndex());
        if (onuOptical != null) {
            Long onuPonId = onuDao.getOnuPonId(onuInfo.getOnuId());
            onuOptical.setOnuPonId(onuPonId);
            onuDao.updateOnuOpticalInfo(onuOptical);
            // 更新CC的实时光功率信息
            onuOptical.setEntityId(onuInfo.getOnuId());
            onuOptical.setOnuPonIndex(onuInfo.getOnuIndex());
            onuDao.updateCmcOpticalInfo(onuOptical);
        }
    }

    @Override
    public void refreshOltOnuCapatilityExt(Long entityId, Long onuIndex) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        OnuFacade onuFacade = getOnuFacade(snmpParam.getIpAddress());
        OltOnuCatv oltOnuCatv = onuFacade.getOltOnuCatv(snmpParam, onuIndex);
        OltOnuRstp oltOnuRstp = onuFacade.getOltOnuRstp(snmpParam, onuIndex);
        OltOnuVoip oltOnuVoip = onuFacade.getOnuVoip(snmpParam, onuIndex);
        List<OltOnuCatv> oltOnuCatvs = new ArrayList<OltOnuCatv>();
        List<OltOnuRstp> oltOnuRstps = new ArrayList<OltOnuRstp>();
        List<OltOnuVoip> oltOnuVoips = new ArrayList<OltOnuVoip>();
        if (oltOnuCatv != null) {
            oltOnuCatv.setEntityId(entityId);
            oltOnuCatv.setOnuIndex(onuIndex);
            oltOnuCatvs.add(oltOnuCatv);
        }
        if (oltOnuRstp != null) {
            oltOnuRstp.setEntityId(entityId);
            oltOnuRstp.setOnuIndex(onuIndex);
            oltOnuRstps.add(oltOnuRstp);
        }

        if (oltOnuVoip != null) {
            oltOnuVoip.setEntityId(entityId);
            oltOnuVoip.setOnuIndex(onuIndex);
            oltOnuVoips.add(oltOnuVoip);
        }
        onuDao.batchUpdateOltOnuCapatilityExt(oltOnuCatvs, oltOnuRstps, oltOnuVoips);
    }

    @Override
    public void refreshOltOnuCapatilityExt(Long entityId) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        OnuFacade onuFacade = getOnuFacade(snmpParam.getIpAddress());
        List<OltOnuCatv> oltOnuCatvs = onuFacade.getOltOnuCatv(snmpParam);
        List<OltOnuRstp> oltOnuRstps = onuFacade.getOltOnuRstp(snmpParam);
        List<OltOnuVoip> oltOnuVoips = onuFacade.getOnuVoip(snmpParam);
        if (oltOnuCatvs != null || oltOnuRstps != null || oltOnuVoips != null) {
            for (int i = 0; i < oltOnuCatvs.size(); i++) {
                oltOnuCatvs.get(i).setEntityId(entityId);
            }
            for (int i = 0; i < oltOnuRstps.size(); i++) {
                oltOnuRstps.get(i).setEntityId(entityId);
            }
            for (int i = 0; i < oltOnuVoips.size(); i++) {
                oltOnuVoips.get(i).setEntityId(entityId);
            }
            onuDao.batchUpdateOltOnuCapatilityExt(oltOnuCatvs, oltOnuRstps, oltOnuVoips);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public Onu getStandardOnuInfo(Long onuId) {
        Onu onu = onuDao.getStandardOnuInfo(onuId);
        Collections.sort(onu.getOnuUniPortList());
        return onu;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.epon.onu.service.OnuService#getOnuAttributeByMac(long,
     * java.lang.String)
     */
    @Override
    public OltOnuAttribute getOnuAttributeByMac(long entityId, String macaddress) {
        return onuDao.getOnuAttributeByMac(entityId, macaddress);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.epon.onu.service.OnuService#getOnuAttributeByLoid(long,
     * java.lang.String)
     */
    @Override
    public OltOnuAttribute getOnuAttributeByLoid(long entityId, String loid) {
        return onuDao.getOnuAttributeByLoid(entityId, loid);
    }

    @Override
    public Integer getOnuDownRate(Long entityId, Long onuId, String onuEorG) {
        Long onuIndex = onuDao.getOnuIndex(onuId);
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        if (onuEorG.equals("E")) {
            try {
                TopOnuSpeed onuSpeed = getOnuFacade(snmpParam.getIpAddress()).getEponOnuDownRate(snmpParam, onuIndex);
                if (onuSpeed.getTopOnuSpeedTestState() == 1) {
                    // [EMS-14688]此时正在测速,为避免测速冲突此时不能下发测速命令
                    return -1;
                }
                getOnuFacade(snmpParam.getIpAddress()).startEponOnuDownRateTest(snmpParam, onuIndex);
                int num = 0;
                while (true) {
                    if (num >= 10) {
                        return -5;
                    }
                    Thread.sleep(2000);
                    onuSpeed = getOnuFacade(snmpParam.getIpAddress()).getEponOnuDownRate(snmpParam, onuIndex);
                    Integer testState = onuSpeed.getTopOnuSpeedTestState();
                    // 0是成功，1是正在测试，2是失败,3是dns解析错误, 4网络原因
                    if (testState == 1) {
                        num++;
                        continue;
                    } else if (testState == 0) {
                        return onuSpeed.getTopOnuSpeedTestDownRate();
                    } else if (testState == 2) {
                        return -2;
                    } else if (testState == 3) {
                        return -3;
                    } else if (testState == 4) {
                        return -4;
                    } else {
                        return -5;
                    }
                }
            } catch (InterruptedException e) {
                logger.error("", e);
                return -5;
            }
        } else {
            TopGponOnuSpeed onuSpeed = getOnuFacade(snmpParam.getIpAddress()).getGponOnuDownRate(snmpParam, onuIndex);
            if (onuSpeed.getTopGponOnuSpeedTestState() == 1) {
                return -1;
            }
            getOnuFacade(snmpParam.getIpAddress()).startGponOnuDownRateTest(snmpParam, onuIndex);
            int num = 0;
            try {
                while (true) {
                    if (num >= 20) {
                        return -5;
                    }
                    Thread.sleep(2000);
                    onuSpeed = getOnuFacade(snmpParam.getIpAddress()).getGponOnuDownRate(snmpParam, onuIndex);
                    Integer testState = onuSpeed.getTopGponOnuSpeedTestState();
                    // 0是成功，1是正在测试，2是失败,3是dns解析错误, 4网络原因
                    if (testState == 1) {
                        num++;
                        continue;
                    } else if (testState == 0) {
                        return onuSpeed.getTopGponOnuSpeedTestDownRate();
                    } else if (testState == 2) {
                        return -2;
                    } else if (testState == 3) {
                        return -3;
                    } else if (testState == 4) {
                        return -4;
                    } else {
                        return -5;
                    }
                }
            } catch (Exception e) {
                logger.error("", e);
                return -5;
            }
        }
    }

    @Override
    public Integer getOnuCountByOnuId(Long onuId) {
        return onuDao.getOnuCountByOnuId(onuId);
    }

    @Override
    public void entityReplaced(EntityEvent event) {
    }

    @Override
    public OltOnuRstp getOltOnuRstp(Long entityId, Long onuId) {
        Long onuIndex = onuDao.getOnuIndex(onuId);
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        OltOnuRstp oltOnuRstp = getOnuFacade(snmpParam.getIpAddress()).getOltOnuRstp(snmpParam, onuIndex);
        onuDao.updateOnuRstpBridgeMode(onuId, oltOnuRstp.getRstpBridgeMode());
        return oltOnuRstp;
    }

    @Override
    public OltUniExtAttribute fetchOnuMacAgeTime(OltUniExtAttribute uniExtAttribute, Long onuId) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(uniExtAttribute.getEntityId());
        uniExtAttribute = getUniFacade(snmpParam.getIpAddress()).fetchOltUniMacAge(snmpParam, uniExtAttribute);
        onuDao.modifyOnuMacAgeTime(onuId, uniExtAttribute.getMacAge());
        return uniExtAttribute;
    }

    @Override
    public OnuInfo getOnuTl1InfoByIndex(Long entityId, Long onuIndex) {
        return onuDao.getOnuTl1InfoByIndex(entityId, onuIndex);
    }

    @Override
    public OnuEnviEnum queryOnuEnvi(Map<String, Object> queryMap) {
        OnuEnviEnum enviEnum;
        int eponCount = onuDao.selectEponOnuCount(queryMap);
        int gponCount = onuDao.selectGponOnuCount(queryMap);
        if (eponCount == 0) {
            enviEnum = OnuEnviEnum.GPON;
        } else if (gponCount == 0) {
            enviEnum = OnuEnviEnum.EPON;
        } else {
            enviEnum = OnuEnviEnum.MIXTURE;
        }
        return enviEnum;
    }

    @Override
    public void onuDeactive(Long onuId, Integer onuDeactive) {
        OltOnuAttribute onu = onuDao.getOnuEntityById(onuId);
        Long onuIndex = onu.getOnuIndex();
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(onu.getEntityId());
        facadeFactory.getFacade(snmpParam.getIpAddress(), GponOnuFacade.class).deActiveOnu(snmpParam, onuIndex,
                onuDeactive);
        onuDao.updateOnuDeactive(onuId, onuDeactive);
    }
}
