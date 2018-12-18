/***********************************************************************
 * $Id: OltServiceImpl.java,v1.0 2013-10-25 上午10:28:47 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.olt.service.impl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.epon.domain.CcmtsFftMonitorScalar;
import com.topvision.ems.epon.domain.DeviceListItem;
import com.topvision.ems.epon.domain.EponPort;
import com.topvision.ems.epon.domain.Fan;
import com.topvision.ems.epon.domain.Olt;
import com.topvision.ems.epon.domain.PonUsedInfo;
import com.topvision.ems.epon.domain.Power;
import com.topvision.ems.epon.domain.Room;
import com.topvision.ems.epon.domain.Slot;
import com.topvision.ems.epon.exception.SwitchOverException;
import com.topvision.ems.epon.exception.SyncSlaveBoardException;
import com.topvision.ems.epon.fault.EponCode;
import com.topvision.ems.epon.olt.dao.OltDao;
import com.topvision.ems.epon.olt.dao.OltPonDao;
import com.topvision.ems.epon.olt.dao.OltSlotDao;
import com.topvision.ems.epon.olt.dao.OltSniDao;
import com.topvision.ems.epon.olt.dao.OltSpectrumSwitchDao;
import com.topvision.ems.epon.olt.domain.OltAttribute;
import com.topvision.ems.epon.olt.domain.OltClearCmOnTime;
import com.topvision.ems.epon.olt.domain.OltFanAttribute;
import com.topvision.ems.epon.olt.domain.OltFanStatus;
import com.topvision.ems.epon.olt.domain.OltMacAddressLearnTable;
import com.topvision.ems.epon.olt.domain.OltPonAttribute;
import com.topvision.ems.epon.olt.domain.OltPowerAttribute;
import com.topvision.ems.epon.olt.domain.OltPowerStatus;
import com.topvision.ems.epon.olt.domain.OltSlotAttribute;
import com.topvision.ems.epon.olt.domain.OltSlotMapTable;
import com.topvision.ems.epon.olt.domain.OltSlotStatus;
import com.topvision.ems.epon.olt.domain.OltSniAttribute;
import com.topvision.ems.epon.olt.domain.OltSpectrumSwitch;
import com.topvision.ems.epon.olt.domain.TopOnuGlobalCfgMgmt;
import com.topvision.ems.epon.olt.domain.TopSysFileDirEntry;
import com.topvision.ems.epon.olt.facade.OltFacade;
import com.topvision.ems.epon.olt.facade.OltSlotFacade;
import com.topvision.ems.epon.olt.service.OltService;
import com.topvision.ems.epon.olt.service.OltSlotService;
import com.topvision.ems.epon.onu.dao.OnuDao;
import com.topvision.ems.epon.onu.domain.OltOnuAttribute;
import com.topvision.ems.epon.onu.service.RogueOnuService;
import com.topvision.ems.epon.rstp.dao.OltRstpDao;
import com.topvision.ems.epon.rstp.domain.OltStpGlobalConfig;
import com.topvision.ems.epon.rstp.domain.OltStpPortConfig;
import com.topvision.ems.epon.topology.domain.OltDiscoveryData;
import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.facade.domain.EntityAttribute;
import com.topvision.ems.fault.message.TrapEvent;
import com.topvision.ems.fault.message.TrapListener;
import com.topvision.ems.network.dao.EntityDao;
import com.topvision.ems.network.domain.EntitySnap;
import com.topvision.ems.network.domain.ModuleParam;
import com.topvision.ems.network.domain.SubDeviceCount;
import com.topvision.ems.network.domain.TelnetLogin;
import com.topvision.ems.network.service.DiscoveryService;
import com.topvision.ems.network.service.EntityService;
import com.topvision.ems.network.service.TelnetLoginService;
import com.topvision.ems.network.service.TopologyService;
import com.topvision.ems.network.util.XmlOperationUtil;
import com.topvision.ems.performance.job.PingJob;
import com.topvision.ems.template.service.EntityTypeService;
import com.topvision.ems.upgrade.telnet.OltTelnetUtil;
import com.topvision.ems.upgrade.telnet.TelnetUtilFactory;
import com.topvision.framework.common.IpUtils;
import com.topvision.framework.common.MacUtils;
import com.topvision.framework.common.RunCmd;
import com.topvision.framework.exception.engine.SnmpException;
import com.topvision.framework.service.BaseService;
import com.topvision.framework.snmp.SnmpParam;
import com.topvision.framework.utils.EponConstants;
import com.topvision.framework.utils.EponIndex;
import com.topvision.platform.ResourceManager;
import com.topvision.platform.SystemConstants;
import com.topvision.platform.facade.FacadeFactory;
import com.topvision.platform.message.event.PingEvent;
import com.topvision.platform.message.event.PingListener;
import com.topvision.platform.message.event.SynchronizedEvent;
import com.topvision.platform.message.event.SynchronizedListener;
import com.topvision.platform.message.service.MessageService;
import com.topvision.platform.util.CurrentRequest;

/**
 * @author flack
 * @created @2013-10-25-上午10:28:47
 * 
 */
@Service("oltService")
public class OltServiceImpl extends BaseService implements OltService, TrapListener, SynchronizedListener, PingListener {
    // 注入discoveryService 用来刷新设备
    @Resource(name = "oltDiscoveryService")
    private DiscoveryService<OltDiscoveryData> discoveryService;
    @Autowired
    private TopologyService topologyService;
    @Autowired
    private MessageService messageService;
    @Autowired
    private EntityDao entityDao;
    @Autowired
    private OltDao oltDao;
    @Autowired
    private OltPonDao oltPonDao;
    @Autowired
    private OltSniDao oltSniDao;
    @Autowired
    private OltSlotDao oltSlotDao;
    @Autowired
    private OnuDao onuDao;
    @Autowired
    private EntityService entityService;
    @Autowired
    private FacadeFactory facadeFactory;
    @Autowired
    private OltRstpDao oltRstpDao;
    @Autowired
    private EntityTypeService entityTypeService;
    @Autowired
    private TelnetLoginService telnetLoginService;
    @Autowired
    private TelnetUtilFactory telnetUtilFactory;
    @Autowired
    private OltSlotService oltSlotService;
    @Autowired
    private RogueOnuService rogueOnuService;
    @Autowired
    private OltSpectrumSwitchDao oltSpectrumSwitchDao;
    private final Object mutex = new Object();

    @Override
    @PostConstruct
    public void initialize() {
        super.initialize();
        ModuleParam moduleParam = new ModuleParam();
        moduleParam.setCos(1000);
        moduleParam.setModuleName("epon");
        moduleParam.setBeanName("eponGetTopoFolderNum");
        topologyService.registerTopoFolderNumModuleParam(moduleParam);
        messageService.addListener(TrapListener.class, this);
        messageService.addListener(SynchronizedListener.class, this);
        messageService.addListener(PingListener.class, this);
    }

    @Override
    @PreDestroy
    public void destroy() {
        super.destroy();
        messageService.removeListener(TrapListener.class, this);
        messageService.removeListener(SynchronizedListener.class, this);
        messageService.removeListener(PingListener.class, this);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.epon.olt.service.OltService#getOltAttribute(java.lang.Long)
     */
    @Override
    public OltAttribute getOltAttribute(Long entityId) {
        return oltDao.getOltAttribute(entityId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.epon.olt.service.OltService#getOltCurrentPerformance(java.lang.Long)
     */
    @Override
    public EntitySnap getOltCurrentPerformance(Long entityId) {
        return oltDao.getOltCurrentPerformance(entityId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.epon.olt.service.OltService#getOltPosition(java.lang.String)
     */
    @Override
    public Room getOltPosition(String location) {
        // location = "topvision";
        StringBuilder path = new StringBuilder();
        path.append(SystemConstants.WEB_INF_REAL_PATH);
        path.append("rooms");
        File rootDir = new File(path.toString());
        File[] files = rootDir.listFiles();
        File file;
        for (int i = files.length - 1; i >= 0; i--) {
            file = files[i];
            if (!file.isDirectory()) {
                String[] parms = (file.toString()).split("\\.");
                if ("xml".equals(parms[parms.length - 1])) {
                    Room room = (Room) XmlOperationUtil.readObject(file);
                    if (room.getEnName().equals(location)) {
                        return room;
                    }
                }
            }
        }
        return null;
    }

    @Override
    public Long getOltSysTime(Long entityId) {
        // 获取采集参数
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        // 调用采集端程序去设备上获取系统时间
        return getOltFacade(snmpParam.getIpAddress()).getOltSysTime(snmpParam);
    }

    @Override
    public void checkSysTiming(Long entityId) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        // 获取网管系统时间
        // 更新设备时间
        getOltFacade(snmpParam.getIpAddress()).sysTiming(snmpParam, System.currentTimeMillis());
    }

    /**
     * 获取OltControlFacade对象
     * 
     * @param ip
     *            被采集设备IP
     * @return OltControlFacade
     */
    private OltFacade getOltFacade(String ip) {
        return facadeFactory.getFacade(ip, OltFacade.class);
    }

    private OltSlotFacade getOltSlotFacade(String ip) {
        return facadeFactory.getFacade(ip, OltSlotFacade.class);
    }

    private ResourceManager getResourece() {
        return ResourceManager.getResourceManager("com.topvision.ems.epon.olt");
    }

    @Override
    public void switchoverOlt(Long entityId) {
        // 说明：bAttribute为active(1)表示主用板卡，为standby(2)表示备用板卡，为standalone(3)表示独立工作模式
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        // 通过数据库获取主用板卡 获得slotIndex
        List<OltSlotAttribute> oltSlotList = oltSlotDao.getOltSlotList(entityId);
        Long masterSlotIndex = 0L;
        Long masterSlotNo = 0L;
        Long masterSlotId = 0L;
        Long slaveSlotIndex = 0L;
        Long slaveSlotNo = 0L;
        Long slaveSlotId = 0L;
        for (OltSlotAttribute sa : oltSlotList) {
            if (sa.getSlotIndex().equals(EponIndex.getSlotIndex(EponConstants.MASTERINDEX.intValue()))) {
                masterSlotIndex = sa.getSlotIndex();
                masterSlotNo = sa.getSlotNo();
                masterSlotId = sa.getSlotId();
            }
            if (sa.getSlotIndex().equals(EponIndex.getSlotIndex(EponConstants.SLAVEINDEX.intValue()))) {
                slaveSlotIndex = sa.getSlotIndex();
                slaveSlotNo = sa.getSlotNo();
                slaveSlotId = sa.getSlotId();
            }
        }
        // 实时获取9号和10号槽位bAttribute和bPresenceStatus信息
        OltSlotAttribute masterSlot = getOltSlotFacade(snmpParam.getIpAddress()).getMpuSlotInfo(snmpParam,
                masterSlotIndex, masterSlotNo);
        OltSlotAttribute slaverSlot = getOltSlotFacade(snmpParam.getIpAddress()).getMpuSlotInfo(snmpParam,
                slaveSlotIndex, slaveSlotNo);
        logger.info("master presence status is:" + masterSlot.getBPresenceStatus() + "(1: installed 2: notinstalled)");
        logger.info("slaver presence status is:" + slaverSlot.getBPresenceStatus() + "(1: installed 2: notinstalled)");
        // 判断9号和10号槽是否插入 没有全部插入则不能倒换 抛异常
        if (masterSlot.getBPresenceStatus().equals(EponConstants.OLT_BOARD_PRESENCE_STATUS_NOTINSTALLED)) {
            logger.info("masterSlot BPresenceStatus {}", masterSlot.getBPresenceStatus());
            throw new SwitchOverException("Business.switchoverNotInstalled");
        } else if (slaverSlot.getBPresenceStatus().equals(EponConstants.OLT_BOARD_PRESENCE_STATUS_NOTINSTALLED)) {
            logger.info("slaverSlot BPresenceStatus {}", slaverSlot.getBPresenceStatus());
            throw new SwitchOverException("Business.switchoverNotInstalled");
        } else if (masterSlot.getBAttribute().equals(EponConstants.OLT_BOARD_ATTRIBUTE_STANDALONE)) {
            // bAttribute状态为standalone(3) 返回无法进行主备倒换操作 抛异常
            logger.info("masterSlot BAttribute {}", masterSlot.getBAttribute());
            throw new SwitchOverException("Business.switchoverStandalone");
        } else if (slaverSlot.getBAttribute().equals(EponConstants.OLT_BOARD_ATTRIBUTE_STANDALONE)) {
            logger.info("slaverSlot BAttribute {}", slaverSlot.getBAttribute());
            throw new SwitchOverException("Business.switchoverStandalone");
        } else if (!slaverSlot.getBAdminStatus().equals(EponConstants.OLT_BOARD_ADMIN_STATUS_UP)
                || !slaverSlot.getbOperationStatus().equals(EponConstants.OLT_BOARD_OPERATION_STATUS_UP)) {
            logger.info("slaverSlot BAdminStatus {0},bOperationStatus {1}", slaverSlot.getBAdminStatus(),
                    slaverSlot.getbOperationStatus());
            throw new SwitchOverException("Business.switchoverNoService");
        } else {
            // Long masterSlotIndex = 0L;
            // 9号和10号槽位均为主控板
            if (masterSlot.getBAttribute().equals(EponConstants.OLT_BOARD_ATTRIBUTE_ACTIVE)
                    && slaverSlot.getBAttribute().equals(EponConstants.OLT_BOARD_ATTRIBUTE_ACTIVE)) {
                logger.info("masterSlot BAttribute {0},slaverSlot BAttribute {1}", masterSlot.getBAttribute(),
                        slaverSlot.getBAttribute());
                throw new SwitchOverException("Business.switchoverAllMaster");
            }
            // 主备倒换
            getOltFacade(snmpParam.getIpAddress()).switchoverOlt(snmpParam, masterSlotNo);
            // 倒换之后MAC地址发生了变化 需要进行一次arp -d 然后才能正常访问倒换过后的ip
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {

            }

            RunCmd cmd = new RunCmd();
            try {
                cmd.runCommand("arp -d " + snmpParam.getIpAddress());
            } catch (Exception e) {
                logger.error("runCommand error {0}", cmd.getStderr());
            }

            // 以下步骤需要由主备倒换事件触发
            final long msn = masterSlotNo;
            final long ssn = slaveSlotNo;
            final long eid = entityId;
            final SnmpParam sp = snmpParam;
            final long msi = masterSlotId;
            final long ssi = slaveSlotId;
            ScheduledThreadPoolExecutor scheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(2);
            Runnable master = new Runnable() {
                @Override
                public void run() {
                    Thread.currentThread().setName("OltService_master");
                    OltSlotAttribute slotA = new OltSlotAttribute();
                    slotA.setSlotNo(msn);
                    slotA = getOltSlotAttribute(eid, sp, slotA, msi, ssi);
                    OltSlotAttribute slotB = new OltSlotAttribute();
                    slotB.setSlotNo(ssn);
                    slotB = getOltSlotAttribute(eid, sp, slotB, msi, ssi);
                    OltSlotStatus slotStatusA = new OltSlotStatus();
                    slotStatusA.setSlotNo(msn);
                    slotStatusA = getOltSlotStatus(eid, sp, slotStatusA, msi, ssi);
                    OltSlotStatus slotStatusB = new OltSlotStatus();
                    slotStatusB.setSlotNo(ssn);
                    slotStatusB = getOltSlotStatus(eid, sp, slotStatusB, msi, ssi);
                    // 将设备上最新bAttribute状态更新至数据库
                    // TODO 同时更新物理槽位号
                    HashMap<Long, Long> map = new HashMap<Long, Long>();
                    map.put(slotA.getSlotIndex(), slotA.getSlotId());
                    map.put(slotB.getSlotIndex(), slotB.getSlotId());
                    List<OltSlotAttribute> attributeList = new ArrayList<OltSlotAttribute>();
                    attributeList.add(slotA);
                    attributeList.add(slotB);
                    oltSlotService.batchInsertOrUpdateSlotAttribute(attributeList, map);
                    // 更新状态值
                    List<OltSlotStatus> statusList = new ArrayList<OltSlotStatus>();
                    statusList.add(slotStatusA);
                    statusList.add(slotStatusB);
                    oltSlotDao.batchInsertOltSlotStatus(statusList);
                }
            };
            Runnable slaver = new Runnable() {

                @Override
                public void run() {
                    Thread.currentThread().setName("OltService_slaver");
                    OltSlotAttribute slotA = new OltSlotAttribute();
                    slotA.setSlotNo(msn);
                    slotA = getOltSlotAttribute(eid, sp, slotA, msi, ssi);
                    OltSlotAttribute slotB = new OltSlotAttribute();
                    slotB.setSlotNo(ssn);
                    slotB = getOltSlotAttribute(eid, sp, slotB, msi, ssi);
                    OltSlotStatus slotStatusA = new OltSlotStatus();
                    slotStatusA.setSlotNo(msn);
                    slotStatusA = getOltSlotStatus(eid, sp, slotStatusA, msi, ssi);
                    OltSlotStatus slotStatusB = new OltSlotStatus();
                    slotStatusB.setSlotNo(ssn);
                    slotStatusB = getOltSlotStatus(eid, sp, slotStatusB, msi, ssi);
                    // 将设备上最新bAttribute状态更新至数据库
                    // TODO 同时更新物理槽位号
                    HashMap<Long, Long> map = new HashMap<Long, Long>();
                    map.put(slotA.getSlotIndex(), slotA.getSlotId());
                    map.put(slotB.getSlotIndex(), slotB.getSlotId());
                    List<OltSlotAttribute> attributeList = new ArrayList<OltSlotAttribute>();
                    attributeList.add(slotA);
                    attributeList.add(slotB);
                    oltSlotService.batchInsertOrUpdateSlotAttribute(attributeList, map);
                    // 更新状态值
                    List<OltSlotStatus> statusList = new ArrayList<OltSlotStatus>();
                    statusList.add(slotStatusA);
                    statusList.add(slotStatusB);
                    oltSlotDao.batchInsertOltSlotStatus(statusList);
                }
            };
            scheduledThreadPoolExecutor.schedule(master, 5, TimeUnit.SECONDS);
            scheduledThreadPoolExecutor.schedule(slaver, 5, TimeUnit.SECONDS);
        }
    }

    @Override
    public OltSlotAttribute getOltSlotAttribute(Long entityId, SnmpParam snmpParam, OltSlotAttribute domain,
            Long masterSlotId, Long slaveSlotId) {
        OltSlotAttribute slot = getOltFacade(snmpParam.getIpAddress()).getDomainInfoLine(snmpParam, domain);
        slot.setEntityId(entityId);
        if (slot.getSlotIndex().equals(EponIndex.getSlotIndex(EponConstants.MASTERINDEX.intValue()))) {
            slot.setSlotId(masterSlotId);
        }
        if (slot.getSlotIndex().equals(EponIndex.getSlotIndex(EponConstants.SLAVEINDEX.intValue()))) {
            slot.setSlotId(slaveSlotId);
        }
        return slot;
    }

    @Override
    public OltSlotStatus getOltSlotStatus(Long entityId, SnmpParam snmpParam, OltSlotStatus domain, Long masterSlotId,
            Long slaveSlotId) {
        OltSlotStatus slot = getOltFacade(snmpParam.getIpAddress()).getDomainInfoLine(snmpParam, domain);
        slot.setEntityId(entityId);
        if (slot.getSlotIndex().equals(EponIndex.getSlotIndex(EponConstants.MASTERINDEX.intValue()))) {
            slot.setSlotId(masterSlotId);
        }
        if (slot.getSlotIndex().equals(EponIndex.getSlotIndex(EponConstants.SLAVEINDEX.intValue()))) {
            slot.setSlotId(slaveSlotId);
        }
        return slot;
    }

    @Override
    public void resetOlt(Long entityId) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        getOltFacade(snmpParam.getIpAddress()).resetOlt(snmpParam);
    }

    @Override
    public void syncSlaveBoard(Long entityId, Integer syncAction) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        try {
            Thread.sleep(30000);
        } catch (InterruptedException e) {
            logger.debug("sleep error");
        }
        Integer status = getOltFacade(snmpParam.getIpAddress()).getOltSyncSlaveBoardStatus(snmpParam);
        if (status.equals(1) || status.equals(2)) {
            throw new SyncSlaveBoardException();
        } else {
            getOltFacade(snmpParam.getIpAddress()).syncSlaveBoard(snmpParam, syncAction);
        }
    }

    @Override
    public void restoreOlt(Long entityId) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        getOltFacade(snmpParam.getIpAddress()).restoreOlt(snmpParam);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Olt getOltStructure(Long entityId) {
        Entity entity = entityService.getEntity(entityId);
        // 拿到OLT的结构信息，供之后获取状态信息,业务信息等使用
        Olt olt = oltDao.getOltStructure(entityId);
        // 处理OLT的结构数据
        OltAttribute oltAttribute = oltDao.getOltAttribute(entityId);
        if (oltAttribute == null || olt.getSlotList() == null || olt.getFanList() == null || olt.getPowerList() == null) {
            return olt;
        }
        OltSlotStatus oltSlotStatus;
        olt.setAttribute(entity, oltAttribute);

        // 循环调用，加入板卡的状态信息,业务信息等
        for (Slot slot : olt.getSlotList()) {
            List<EponPort> slotPorts = new ArrayList<EponPort>();
            // 获取业务信息
            OltSlotAttribute oltSlotAttribute = oltSlotDao.getSlotAttribute(slot.getSlotId());
            // 获取状态信息
            oltSlotStatus = oltSlotDao.getOltSlotStatus(slot.getSlotId());
            slot.setAttribute(oltSlotAttribute, oltSlotStatus);
            // 循环调用获取端口,通过端口获取端口自身的状态信息，业务信息等
            for (EponPort port : slot.getPortList()) {
                if (port == null || port.getPortSubType() == null) {
                    continue;
                }
                // 走PON口的业务/状态信息获取流程
                if (port.getPortSubType().equalsIgnoreCase("pon")) {
                    OltPonAttribute oltPonAttribute = oltPonDao.getPonAttribute(port.getPortId());
                    if (oltPonAttribute != null) {
                        // 通过端口类型将PON口区分成不同的子类型以供前台使用
                        if (oltPonAttribute.getPonPortType().equals(EponConstants.PON_PORT_TYPE_GEEPON)) {
                            port.setPortSubType("geEpon");
                        } else if (oltPonAttribute.getPonPortType().equals(EponConstants.PON_PORT_TYPE_TENGEEPON)) {
                            port.setPortSubType("tengeEpon");
                        } else if (oltPonAttribute.getPonPortType().equals(EponConstants.PON_PORT_TYPE_GPON)) {
                            port.setPortSubType("gpon");
                        } else {
                            port.setPortSubType("geEpon");
                        }
                        // 将oltPonAttribute对象的数据复制到EPONPORT对象中
                        port.modifyAttribute(oltPonAttribute);
                        slotPorts.add(port);
                    }
                    // 走SNI口的业务/状态信息获取流程
                } else if (port.getPortSubType().equalsIgnoreCase("sni")) {
                    OltStpPortConfig oltStpPortConfig = oltRstpDao.getOltStpPortConfig(entityId, port.getPortId());
                    OltSniAttribute oltSniAttribute = oltSniDao.getSniAttribute(port.getPortId());

                    // 通过端口类型将SNI口区分成不同的子类型以供前台使用
                    if (oltSniAttribute != null) {
                        // 获取sni端口RSTP属性
                        if (oltStpPortConfig != null) {
                            oltSniAttribute.setStpPortEnabled(oltStpPortConfig.getStpPortEnabled());
                            oltSniAttribute.setStpPortRstpProtocolMigration(oltStpPortConfig
                                    .getStpPortRstpProtocolMigration());
                        }
                        // 获取端口基本属性
                        if (oltSniAttribute.getTopSniAttrPortType() != null
                                && oltSniAttribute.getSniMediaType() != null) {
                            if (oltSniAttribute.getTopSniAttrPortType().equals(EponConstants.SNI_PORT_TYPE_GECOPPER)) {
                                port.setPortSubType("geCopper");
                            } else if (oltSniAttribute.getTopSniAttrPortType().equals(
                                    EponConstants.SNI_PORT_TYPE_GEFIBER)) {
                                port.setPortSubType("geFiber");
                            } else if (oltSniAttribute.getTopSniAttrPortType().equals(EponConstants.SNI_PORT_TYPE_XEFIBER)) {
                                port.setPortSubType("xeFiber");
                            } else {
                                // 如果没有取得SNI端口类型，则通过SNI介质类型判断。
                                if (oltSniAttribute.getSniMediaType() == 1) {
                                    port.setPortSubType("geCopper");
                                } else if (oltSniAttribute.getSniMediaType() == 2) {
                                    port.setPortSubType("geFiber");
                                }
                            }
                            port.modifyAttribute(oltSniAttribute);
                            slotPorts.add(port);
                        }
                    }
                } else {
                    //
                }
            }
            slot.setPortList(slotPorts);
            Collections.sort(slot.getPortList());
        }
        Collections.sort(olt.getSlotList());
        for (Fan fan : olt.getFanList()) {
            OltFanAttribute oltFanAttribute = oltSlotDao.getFanAttribute(fan.getFanCardId());
            OltFanStatus oltFanStatus = oltSlotDao.getFanStatus(fan.getFanCardId());
            fan.setAttribute(oltFanAttribute, oltFanStatus);
        }
        Collections.sort(olt.getFanList());
        for (Power power : olt.getPowerList()) {
            OltPowerAttribute oltPowerAttribute = oltSlotDao.getPowerAttribute(power.getPowerCardId());
            OltPowerStatus oltPowerStatus = oltSlotDao.getPowerStatus(power.getPowerCardId());
            power.setAttribute(oltPowerAttribute, oltPowerStatus);
        }
        Collections.sort(olt.getPowerList());
        OltStpGlobalConfig oltStpGlobalConfig = oltRstpDao.getOltStpGlobalConfig(entityId);
        if (oltStpGlobalConfig != null) {
            olt.setStpGlobalSetEnable(oltStpGlobalConfig.getEnable());
        }
        return olt;
    }

    @Override
    public OltSlotAttribute getSlotAttribute(Long slotId) {
        return oltSlotDao.getSlotAttribute(slotId);
    }

    @Override
    public OltPowerAttribute getPowerAttribute(Long powerCardId) {
        return oltSlotDao.getPowerAttribute(powerCardId);
    }

    @Override
    public List<DeviceListItem> getDeviceListItem(Map<String, Object> map) {
        Long oltType = entityTypeService.getOltType();
        map.put("type", oltType);
        return oltDao.getDeviceListItem(map);
    }

    @Override
    public List<OltAttribute> getOltList(Map<String, Object> paramsMap) {
        Long oltType = entityTypeService.getOltType();
        paramsMap.put("type", oltType);
        Long userId = null;
        try {
            userId = CurrentRequest.getCurrentUser().getUserId();
        } catch (Exception e) {
            logger.debug("CurrentRequest.getCurrentUser().getUserId() fail");
        }
        paramsMap.put("userId", userId);
        return oltDao.getOltList(paramsMap);
    }

    @Override
    public int getOltListCount(Map<String, Object> paramsMap) {
        Long oltType = entityTypeService.getOltType();
        paramsMap.put("type", oltType);
        return oltDao.getOltListCount(paramsMap);
    }

    @Override
    public String getEntityDolStatus(Long entityId) {
        return oltDao.getEntityDolStatus(entityId);
    }

    @Override
    public void updateEntityDolStatus(EntityAttribute entityAttribute) {
        oltDao.updateEntityDolStatus(entityAttribute);
    }

    @Override
    public List<OltMacAddressLearnTable> getOltMacLearnTableList(Long entityId) {
        return oltDao.getOltMacLearnTableList(entityId);
    }

    @Override
    public void refreshOltMacLearnTable(Long entityId) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        // 下发命令让设备的macLearn表项刷新
        getOltFacade(snmpParam.getIpAddress()).execOltMacLearnSyncAction(snmpParam);
        List<OltMacAddressLearnTable> table = getOltFacade(snmpParam.getIpAddress()).refreshOltMacLearnTable(snmpParam);
        if (table == null) {
            table = new ArrayList<OltMacAddressLearnTable>();
        }
        for (OltMacAddressLearnTable macLearn : table) {
            macLearn.setEntityId(entityId);
        }
        oltDao.batchInsertOltMacLearnTable(table, entityId);
    }

    @Override
    public void addOltDeviceUpTime(Long entityId, Long sysUpTime) {
        try {
            Long collectTime = System.currentTimeMillis();
            oltDao.addOltDeviceUpTime(entityId, sysUpTime, collectTime);
            Thread.sleep(1000);
            oltDao.cleanOltDeviceUpTime(entityId, collectTime);
        } catch (InterruptedException e) {
            logger.error("", e);
        }
    }

    @Override
    public String getOltSoftVersion(Long entityId) {
        return oltDao.selectOltSoftVersion(entityId);
    }

    @Override
    public void updateOltAttribute(OltAttribute oltAttribute) {
        // SnmpParam snmpParam =
        // entityService.getSnmpParamByEntity(oltAttribute.getEntityId());
        // getOltControlFacade(snmpParam.getIpAddress()).modifyOltFacility(snmpParam,
        // oltAttribute);
        oltDao.updateOltAttribute(oltAttribute);
    }

    @Override
    public void insertEntityStates(SynchronizedEvent event) {
        try {
            this.checkSysTiming(event.getEntityId());
            logger.info("checkSysTiming Finish!");
        } catch (Exception e) {
            logger.error("checkSysTiming wrong", e);
        }

        try {
            refreshFileDir(event.getEntityId());
            logger.info("refreshFileDir Finish!");
        } catch (Exception e) {
            logger.error("refreshFileDir wrong", e);
        }

        try {
            refreshSlotMapInfo(event.getEntityId());
            logger.info("refreshSlotMap Finish!");
        } catch (Exception e) {
            logger.error("refreshSlotMap wrong", e);
        }

        try {
            refreshTopOnuGlobalCfgMgmt(event.getEntityId());
            logger.info("refreshTopOnuGlobalCfgMgmt Finish!");
        } catch (Exception e) {
            logger.error("refreshTopOnuGlobalCfgMgmt wrong", e);
        }
        try {
            refreshCmClearTime(event.getEntityId());
            logger.info("refreshCmClearTime Finish!");
        } catch (Exception e) {
            logger.error("refreshCmClearTime wrong", e);
        }

        try {
            rogueOnuService.refreshSystemRogueCheck(event.getEntityId());
            logger.info("refreshSystemRogueCheck Finish!");
        } catch (Exception e) {
            logger.error("refreshSystemRogueCheck wrong", e);
        }
        // EMS-14856 NM3000默认开启OLT频谱功能
        try {
            defaultStartSpectrumSwitchOlt(event.getEntityId());
            logger.info("defaultStartSpectrumSwitchOlt Finish!");
        } catch (Exception e) {
            logger.error("defaultStartSpectrumSwitchOlt wrong!", e);
        }
    }

    @Override
    public void defaultStartSpectrumSwitchOlt(Long entityId) {
        SnmpParam snmpParam = entityDao.getSnmpParamByEntityId(entityId);
        // 没有开启就默认开启
        if (!getOltFacade(snmpParam.getIpAddress()).getOltSwitchStatus(snmpParam)) {
            getOltFacade(snmpParam.getIpAddress()).startSpectrumSwitchOlt(snmpParam);
        }
        OltSpectrumSwitch oltSpectrumSwitch = new OltSpectrumSwitch();
        Integer status = 1;
        oltSpectrumSwitch.setCollectSwitch(status);
        oltSpectrumSwitch.setEntityId(entityId);
        oltSpectrumSwitchDao.insertSpectrumOltSwitch(oltSpectrumSwitch);
    }

    @Override
    public void refreshCmClearTime(Long entityId) {
        SnmpParam snmpParam = entityDao.getSnmpParamByEntityId(entityId);
        OltClearCmOnTime oltClearOnTime = getOltFacade(snmpParam.getIpAddress()).getCmcClearTime(snmpParam);
        oltDao.updateOltClearTime(oltClearOnTime.getOltClearTime(), entityId);
    }

    @Override
    public void pingAction(PingEvent pingEvent) {
        Entity entity = entityDao.selectByPrimaryKey(pingEvent.getEntityId());
        // 首先判断是否是EPON的设备
        if (entityTypeService.isOlt(entity.getTypeId())) {
            if (pingEvent.getCode() == PingJob.PING_CONNECT_TYPE) {
                OltAttribute oltAttribute = oltDao.getOltAttribute(pingEvent.getEntityId());
                // 表示设备目前联通 根据上次的状态判断是否需要采集在线时间
                if (oltAttribute != null && oltAttribute.getOltDeviceUpTime() == -1) {
                    SnmpParam snmpParam = entityService.getSnmpParamByEntity(pingEvent.getEntityId());
                    Long deviceUpTime = 0L;
                    try {
                        // 此处获得的结果通过SNMP4J自动转化为毫秒
                        deviceUpTime = Long.parseLong(getOltFacade(entity.getIp()).getValueByOid(snmpParam,
                                "1.3.6.1.4.1.17409.2.3.1.2.1.1.5.1"));
                    } catch (SnmpException e) {
                        deviceUpTime = 0L;
                    }
                    oltDao.updateOltDeviceUpTime(entity.getEntityId(), System.currentTimeMillis() - deviceUpTime);
                }
            } else if (pingEvent.getCode() == PingJob.PING_DISCONNECT_TYPE) {
                // 表示设备目前不联通
                oltDao.updateOltDeviceUpTime(entity.getEntityId(), -1L);
            }
        }
    }

    @Override
    public void updateEntityStates(SynchronizedEvent event) {
    }

    @Override
    public void onTrapMessage(TrapEvent evt) {
        // 暂时没有文件告警的处理器 此处没有意义
        /*
         * if (evt.getCode() >= 5 && evt.getCode() <= 8) { mutex.notifyAll(); trap = evt.getTrap();
         * } else if (evt.getCode() >= 129189 && evt.getCode() <= 129192) { onuMutex.notifyAll();
         * trap = evt.getTrap(); }
         */
        if (evt.getCode() == EponCode.FILE_DOWNLOAD) {
            // trap = evt.getTrap();
            mutex.notifyAll();
        }
    }

    @Override
    public CcmtsFftMonitorScalar getCcmtsFftGbStatus(Long entityId) {
        return oltDao.queryCcmtsFftGbStatus(entityId);
    }

    @Override
    public void modifyCcmtsFftGbStatus(Long entityId, Integer fftMonitorGlbStatus) {
        CcmtsFftMonitorScalar ccmtsFftMonitorScalar = new CcmtsFftMonitorScalar();
        ccmtsFftMonitorScalar.setEntityId(entityId);
        ccmtsFftMonitorScalar.setFftMonitorGlbStatus(fftMonitorGlbStatus);
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        getOltFacade(snmpParam.getIpAddress()).modifyCcmtsFftGbStatus(snmpParam, ccmtsFftMonitorScalar);
        oltDao.updateCcmtsFftGbStatus(ccmtsFftMonitorScalar);
    }

    @Override
    public void refreshFileDir(Long entityId) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        List<TopSysFileDirEntry> fileDirList = getOltFacade(snmpParam.getIpAddress()).refreshFileDir(snmpParam);
        oltDao.updateOltFileDir(fileDirList, entityId);
    }

    @Override
    public TopSysFileDirEntry getOltFileDirEntry(Long entityId, Integer fileType) {
        return oltDao.getOltFileDirEntry(entityId, fileType);
    }

    @Override
    public Integer oltVersionCompare(Long entityId) {
        String baseVersion = "1.6.20.0";
        String deviceVersion = getOltSoftVersion(entityId);
        if (deviceVersion.contains("PN8600-V")) {
            deviceVersion.replace("PN8600-V", "V");
        }
        deviceVersion = getOltSoftVersion(entityId).split("V")[1];
        String[] split1 = baseVersion.split("\\.|-");
        String[] split2 = deviceVersion.split("\\.|-");

        for (int i = 0; i < 4; i++) {
            int i1 = Integer.parseInt(split1[i]);
            int i2 = Integer.parseInt(split2[i]);
            if (i1 > i2) {
                return 1;
            } else if (i1 < i2) {
                return -1;
            } else if (i1 == i2) {
                continue;
            } else {
                throw new RuntimeException("input error");
            }
        }
        return 0;
    }

    @Override
    public SubDeviceCount getSubCountInfo(Long entityId) {
        return oltDao.querySubCountInfo(entityId);
    }

    @Override
    public void refreshSlotMapInfo(Long entityId) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        List<OltSlotMapTable> slotMapList = getOltSlotFacade(snmpParam.getIpAddress()).getOltSlotMapTable(snmpParam);
        for (OltSlotMapTable slotMap : slotMapList) {
            slotMap.setEntityId(entityId);
        }
        oltSlotDao.batchInsertSlotMap(slotMapList, entityId);
    }

    @Override
    public String getMacInfoStr(Long entityId, String matchMacAddr) throws IOException {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        String ip = snmpParam.getIpAddress();
        TelnetLogin telnetLogin = telnetLoginService.getTelnetLoginConfigByIp(new IpUtils(ip).longValue());
        if (telnetLogin == null) {
            telnetLogin = telnetLoginService.getGlobalTelnetLogin();
        }
        OltTelnetUtil oltTelnetUtil = null;
        String macInfo = null;
        try {
            oltTelnetUtil = (OltTelnetUtil) telnetUtilFactory.getOltTelnetUtil(ip);
            oltTelnetUtil.connect(ip);
            Boolean isLogin = oltTelnetUtil.login(telnetLogin.getUserName(), telnetLogin.getPassword(),
                    telnetLogin.getEnablePassword(), telnetLogin.getIsAAA());
            if (isLogin) {
                oltTelnetUtil.enterEnable(telnetLogin.getEnablePassword());
                oltTelnetUtil.execCmd("terminal length 0");
                oltTelnetUtil.enterConfig();
                String result = oltTelnetUtil.execCmd("show mac-addr vlan all");
                String[] resultArray = result.split("\n");
                Pattern pattern = Pattern.compile("^(\\d+)(.*)");// 以数字开头
                // List<MacAddrInfo> macList = new ArrayList<MacAddrInfo>();
                for (int i = 0; i < resultArray.length; i++) {
                    if (pattern.matcher(resultArray[i].toString().trim()).matches()) {
                        String[] temp = resultArray[i].replaceAll(" +", " ").trim().split(" ");
                        if ("GE".equals(temp[4].toUpperCase())) {// 上联口
                            String learMac = MacUtils.formatMac(temp[1].toUpperCase());
                            if (learMac.equals(matchMacAddr.toUpperCase())) {
                                macInfo = getResourece().getString("EPON.macstudyGE") + temp[5] + "  VLAN: "
                                        + Integer.parseInt(temp[2]);
                                break;
                            }
                            /*
                             * MacAddrInfo macAddrInfo = new MacAddrInfo();
                             * macAddrInfo.setMacAddr(temp[1]);
                             * macAddrInfo.setVlanId(Integer.parseInt(temp[2]));
                             * macAddrInfo.setPortType(temp[4]); macAddrInfo.setPortStr(temp[5]);
                             * macList.add(macAddrInfo);
                             */
                        } else {// PON口
                            String onuList = oltTelnetUtil.execCmd("show onuinfo all");
                            String[] resultOnu = onuList.split("\n");
                            for (int j = 0; j < resultOnu.length; j++) {
                                if (pattern.matcher(resultOnu[j].toString().trim()).matches()) {
                                    String[] tempOnu = resultOnu[j].replaceAll(" +", " ").trim().split(" ");
                                    if (tempOnu[7].equals("8624") && !tempOnu[1].equals("----")) {
                                        String onuAddr = tempOnu[0].split(":")[1];
                                        String portAddr = tempOnu[0].split(":")[0];
                                        oltTelnetUtil.execCmd("int onu " + portAddr + ":" + Integer.parseInt(onuAddr));
                                        for (int k = 1; k <= 4; k++) {
                                            String onuLearMac = oltTelnetUtil
                                                    .execCmd("show port " + k + " mac-address");
                                            String[] resultMac = onuLearMac.split("\n");
                                            for (int m = 0; m < resultMac.length; m++) {
                                                if (pattern.matcher(resultMac[m].toString().trim()).matches()) {
                                                    String[] uniMac = resultMac[m].replaceAll(" +", " ").trim()
                                                            .split(" ");
                                                    if (uniMac[1].toUpperCase().equals(matchMacAddr.toUpperCase())) {
                                                        macInfo = getResourece().getString("EPON.macstudyONUloc")
                                                                + portAddr + ":" + onuAddr
                                                                + getResourece().getString("EPON.UniPort2") + k
                                                                + "    VLAN:  " + uniMac[2];
                                                        break;
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

        } catch (Exception e) {
            logger.error("", e);
        } finally {
            telnetUtilFactory.releaseTelnetUtil(oltTelnetUtil);
        }
        return macInfo;
    }

    @Override
    public PonUsedInfo getPonUsedInfo(Long entityId) {
        PonUsedInfo portUsedInfo = new PonUsedInfo();
        int totalNum = 0;
        int upNum = 0;
        int slotUnusedNum = 0;
        Set<Long> slotIdList = new HashSet<Long>();
        List<OltPonAttribute> oltPonAttributes = oltPonDao.getPonListByEntityId(entityId);
        for (OltPonAttribute ponAttr : oltPonAttributes) {
            totalNum++;
            slotIdList.add(ponAttr.getSlotId());
            List<OltOnuAttribute> onuList = onuDao.getOnuListByPonId(ponAttr.getPonId());
            if (onuList != null && onuList.size() > 0) {
                upNum++;
            }
        }
        List<OltSlotAttribute> slots = oltSlotDao.selectSlotList(entityId);
        for (OltSlotAttribute slot : slots) {
            if (slot.getTopSysBdPreConfigType().equals(0)) {
                slotUnusedNum++;
            }
        }
        portUsedInfo.setPonUpNum(upNum);
        portUsedInfo.setUnusedPonNum(totalNum - upNum);
        portUsedInfo.setSlotTotalNum(slotIdList.size());
        portUsedInfo.setUnusedSlotNum(slotUnusedNum);
        return portUsedInfo;
    }

    @Override
    public void refreshTopOnuGlobalCfgMgmt(Long entityId) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        List<TopOnuGlobalCfgMgmt> onuGlobalCfgMgmts = getOltFacade(snmpParam.getIpAddress()).getOnuGlobalCfgMgmt(
                snmpParam);
        for (TopOnuGlobalCfgMgmt configs : onuGlobalCfgMgmts) {
            configs.setEntityId(entityId);
        }
        oltDao.batchInsertTopOnuGlobalCfgMgmt(onuGlobalCfgMgmts, entityId);
    }

    @Override
    public void modifyTopOnuGlobalCfgMgmt(List<TopOnuGlobalCfgMgmt> topOnuGlobalCfgMgmts, Long entityId) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        for (TopOnuGlobalCfgMgmt topOnuGlobalCfgMgmt : topOnuGlobalCfgMgmts) {
            getOltFacade(snmpParam.getIpAddress()).modifyOnuGlobalCfgMgmt(snmpParam, topOnuGlobalCfgMgmt);
        }
        oltDao.batchInsertTopOnuGlobalCfgMgmt(topOnuGlobalCfgMgmts, entityId);
    }

    @Override
    public List<TopOnuGlobalCfgMgmt> getTopOnuGlobalCfgMgmt(Long entityId) {
        return oltDao.getTopOnuGlobalCfgMgmt(entityId);
    }

    @Override
    public List<Long> queryEntityIdOfOlt() {
        return oltDao.queryEntityIdOfOlt();
    }
}
