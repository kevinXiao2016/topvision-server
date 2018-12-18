/***********************************************************************
 * $Id: OltIgmpService.java,v1.0 2013-10-25 下午4:31:07 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.igmp.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.epon.domain.IgmpForwardingSnooping;
import com.topvision.ems.epon.exception.AddIgmpMvlanException;
import com.topvision.ems.epon.exception.AddIgmpProxyException;
import com.topvision.ems.epon.exception.AddIgmpVlanTransException;
import com.topvision.ems.epon.exception.DeleteIgmpMvlanException;
import com.topvision.ems.epon.exception.DeleteIgmpProxyException;
import com.topvision.ems.epon.exception.DeleteIgmpVlanTransException;
import com.topvision.ems.epon.exception.ModifyIgmpGlobalInfoException;
import com.topvision.ems.epon.exception.ModifyIgmpMaxGroupNumException;
import com.topvision.ems.epon.exception.ModifyIgmpMcOnuInfoException;
import com.topvision.ems.epon.exception.ModifyIgmpMcUniConfigException;
import com.topvision.ems.epon.exception.ModifyIgmpMvlanInfoException;
import com.topvision.ems.epon.exception.ModifyIgmpProxyInfoException;
import com.topvision.ems.epon.exception.ModifyIgmpSniConfigException;
import com.topvision.ems.epon.exception.ModifyIgmpVlanTransException;
import com.topvision.ems.epon.exception.ModifyMulticastUserAuthorityListException;
import com.topvision.ems.epon.exception.ModifyProxyMulticastVIDException;
import com.topvision.ems.epon.exception.RefreshIgmpControlledMcCdrTableException;
import com.topvision.ems.epon.exception.RefreshIgmpControlledMulticastPackageTableException;
import com.topvision.ems.epon.exception.RefreshIgmpControlledMulticastUserAuthorityTableException;
import com.topvision.ems.epon.exception.RefreshIgmpEntityTableException;
import com.topvision.ems.epon.exception.RefreshIgmpForwardingTableException;
import com.topvision.ems.epon.exception.RefreshIgmpMcOnuTableException;
import com.topvision.ems.epon.exception.RefreshIgmpMcOnuVlanTransTableException;
import com.topvision.ems.epon.exception.RefreshIgmpMcParamMgmtObjectsException;
import com.topvision.ems.epon.exception.RefreshIgmpMcSniConfigMgmtObjectsException;
import com.topvision.ems.epon.exception.RefreshIgmpMcUniConfigTableException;
import com.topvision.ems.epon.exception.RefreshIgmpProxyParaTableException;
import com.topvision.ems.epon.fault.EponCode;
import com.topvision.ems.epon.fault.trap.OltTrapConstants;
import com.topvision.ems.epon.igmp.dao.OltIgmpDao;
import com.topvision.ems.epon.igmp.domain.IgmpControlledMcCdrTable;
import com.topvision.ems.epon.igmp.domain.IgmpControlledMcPreviewIntervalTable;
import com.topvision.ems.epon.igmp.domain.IgmpControlledMulticastPackageTable;
import com.topvision.ems.epon.igmp.domain.IgmpControlledMulticastUserAuthorityTable;
import com.topvision.ems.epon.igmp.domain.IgmpEntityTable;
import com.topvision.ems.epon.igmp.domain.IgmpForwardingTable;
import com.topvision.ems.epon.igmp.domain.IgmpMcOnuTable;
import com.topvision.ems.epon.igmp.domain.IgmpMcOnuVlanTransTable;
import com.topvision.ems.epon.igmp.domain.IgmpMcParamMgmtObjects;
import com.topvision.ems.epon.igmp.domain.IgmpMcSniConfigMgmtObjects;
import com.topvision.ems.epon.igmp.domain.IgmpMcUniConfigTable;
import com.topvision.ems.epon.igmp.domain.IgmpProxyParaTable;
import com.topvision.ems.epon.igmp.domain.TopIgmpForwardingSnooping;
import com.topvision.ems.epon.igmp.domain.TopMcForwardingOnuTable;
import com.topvision.ems.epon.igmp.domain.TopMcForwardingPortTable;
import com.topvision.ems.epon.igmp.domain.TopMcForwardingSlotTable;
import com.topvision.ems.epon.igmp.facade.OltIgmpFacade;
import com.topvision.ems.epon.igmp.service.OltIgmpService;
import com.topvision.ems.epon.olt.dao.OltDao;
import com.topvision.ems.epon.olt.dao.OltSlotDao;
import com.topvision.ems.epon.onu.dao.OnuDao;
import com.topvision.ems.epon.onu.domain.OltUniAttribute;
import com.topvision.ems.epon.utils.EponUtil;
import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.fault.message.TrapEvent;
import com.topvision.ems.fault.message.TrapListener;
import com.topvision.ems.network.dao.EntityDao;
import com.topvision.ems.network.service.EntityService;
import com.topvision.framework.exception.engine.SnmpException;
import com.topvision.framework.exception.engine.SnmpNoSuchInstanceException;
import com.topvision.framework.service.BaseService;
import com.topvision.framework.snmp.SnmpParam;
import com.topvision.framework.snmp.Trap;
import com.topvision.platform.facade.FacadeFactory;
import com.topvision.platform.message.event.SynchronizedEvent;
import com.topvision.platform.message.event.SynchronizedListener;
import com.topvision.platform.message.service.MessageService;

/**
 * @author flack
 * @created @2013-10-25-下午4:31:07
 *
 */
@Service("oltIgmpService")
public class OltIgmpServiceImpl extends BaseService implements OltIgmpService, TrapListener, SynchronizedListener {
    @Autowired
    private EntityService entityService;
    @Autowired
    private FacadeFactory facadeFactory;
    @Autowired
    private MessageService messageService;
    @Autowired
    private OltIgmpDao oltIgmpDao;
    @Autowired
    private OltDao oltDao;
    @Autowired
    private OnuDao onuDao;
    @Autowired
    private OltSlotDao oltSlotDao;
    @Autowired
    private EntityDao entityDao;

    @Override
    @PreDestroy
    public void destroy() {
        super.destroy();
        //messageService.removeListener(SynchronizedListener.class, this);
        //messageService.removeListener(TrapListener.class, this);
    }

    @Override
    @PostConstruct
    public void initialize() {
        super.initialize();
        //messageService.addListener(SynchronizedListener.class, this);
        //messageService.addListener(TrapListener.class, this);
    }

    /**
     * 新增设备业务属性
     * 
     * @param event
     */
    @Override
    public void insertEntityStates(SynchronizedEvent event) {
        /*
        Long timeTmp = 0L;
        logger.info("begin to discovery IgmpData EntityId:" + event.getEntityId());

        timeTmp = LoggerUtil.topoStartTimeLog(event.getIpAddress(), "TopMcForwardingSlotTable");
        try {
            refreshTopMcForwardingSlotTable(event.getEntityId());
            logger.info("refreshTopMcForwardingSlotTable finished!");
        } catch (Exception e) {
            logger.error("refreshTopMcForwardingSlotTable error:", e);
        }
        LoggerUtil.topoEndTimeLog(event.getIpAddress(), "TopMcForwardingSlotTable", timeTmp);

        timeTmp = LoggerUtil.topoStartTimeLog(event.getIpAddress(), "TopMcForwardingPortTable");
        try {
            refreshTopMcForwardingPortTable(event.getEntityId());
            logger.info("refreshTopMcForwardingPortTable finished!");
        } catch (Exception e) {
            logger.error("refreshTopMcForwardingPortTable error:", e);
        }
        LoggerUtil.topoEndTimeLog(event.getIpAddress(), "TopMcForwardingPortTable", timeTmp);

        timeTmp = LoggerUtil.topoStartTimeLog(event.getIpAddress(), "TopMcForwardingOnuTable");
        try {
            refreshTopMcForwardingOnuTable(event.getEntityId());
            logger.info("refreshTopMcForwardingOnuTable finished!");
        } catch (Exception e) {
            logger.error("refreshTopMcForwardingOnuTable error:", e);
        }
        LoggerUtil.topoEndTimeLog(event.getIpAddress(), "TopMcForwardingOnuTable", timeTmp);

        timeTmp = LoggerUtil.topoStartTimeLog(event.getIpAddress(), "IgmpControlledMcCdrTable");
        try {
            refreshIgmpControlledMcCdrTable(event.getEntityId());
            logger.info("refreshIgmpControlledMcCdrTable finished!");
        } catch (Exception e) {
            logger.error("refreshIgmpControlledMcCdrTable error:", e);
        }
        LoggerUtil.topoEndTimeLog(event.getIpAddress(), "IgmpControlledMcCdrTable", timeTmp);

        timeTmp = LoggerUtil.topoStartTimeLog(event.getIpAddress(), "IgmpControlledMulticastPackageTable");
        try {
            refreshIgmpControlledMulticastPackageTable(event.getEntityId());
            logger.info("refreshIgmpControlledMulticastPackageTable finished!");
        } catch (Exception e) {
            logger.error("refreshIgmpControlledMulticastPackageTable error:", e);
        }
        LoggerUtil.topoEndTimeLog(event.getIpAddress(), "IgmpControlledMulticastPackageTable", timeTmp);

        timeTmp = LoggerUtil.topoStartTimeLog(event.getIpAddress(), "IgmpControlledMulticastUserAuthorityTable");
        try {
            refreshIgmpControlledMulticastUserAuthorityTable(event.getEntityId());
            logger.info("refreshIgmpControlledMulticastUserAuthorityTable finished!");
        } catch (Exception e) {
            logger.error("refreshIgmpControlledMulticastUserAuthorityTable error:", e);
        }
        LoggerUtil.topoEndTimeLog(event.getIpAddress(), "IgmpControlledMulticastUserAuthorityTable", timeTmp);

        timeTmp = LoggerUtil.topoStartTimeLog(event.getIpAddress(), "IgmpEntityTable");
        try {
            refreshIgmpEntityTable(event.getEntityId());
            logger.info("refreshIgmpEntityTable finished!");
        } catch (Exception e) {
            logger.error("refreshIgmpEntityTable error:", e);
        }
        LoggerUtil.topoEndTimeLog(event.getIpAddress(), "IgmpEntityTable", timeTmp);

        timeTmp = LoggerUtil.topoStartTimeLog(event.getIpAddress(), "IgmpForwardingTable");
        try {
            refreshIgmpForwardingTable(event.getEntityId());
            logger.info("refreshIgmpForwardingTable finished!");
        } catch (Exception e) {
            logger.error("refreshIgmpForwardingTable error:", e);
        }
        LoggerUtil.topoEndTimeLog(event.getIpAddress(), "IgmpForwardingTable", timeTmp);

        timeTmp = LoggerUtil.topoStartTimeLog(event.getIpAddress(), "IgmpMcOnuTable");
        try {
            refreshIgmpMcOnuTable(event.getEntityId());
            logger.info("refreshIgmpMcOnuTable finished!");
        } catch (Exception e) {
            logger.error("refreshIgmpMcOnuTable error:", e);
        }
        LoggerUtil.topoEndTimeLog(event.getIpAddress(), "IgmpMcOnuTable", timeTmp);

        timeTmp = LoggerUtil.topoStartTimeLog(event.getIpAddress(), "IgmpMcOnuVlanTransTable");
        try {
            refreshIgmpMcOnuVlanTransTable(event.getEntityId());
            logger.info("refreshIgmpMcOnuVlanTransTable finished!");
        } catch (Exception e) {
            logger.error("refreshIgmpMcOnuVlanTransTable error:", e);
        }
        LoggerUtil.topoEndTimeLog(event.getIpAddress(), "IgmpMcOnuVlanTransTable", timeTmp);

        timeTmp = LoggerUtil.topoStartTimeLog(event.getIpAddress(), "IgmpMcParamMgmtObjects");
        try {
            refreshIgmpMcParamMgmtObjects(event.getEntityId());
            logger.info("refreshIgmpMcParamMgmtObjects finished!");
        } catch (Exception e) {
            logger.error("refreshIgmpMcParamMgmtObjects error:", e);
        }
        LoggerUtil.topoEndTimeLog(event.getIpAddress(), "IgmpMcParamMgmtObjects", timeTmp);

        timeTmp = LoggerUtil.topoStartTimeLog(event.getIpAddress(), "IgmpMcSniConfigMgmtObjects");
        try {
            refreshIgmpMcSniConfigMgmtObjects(event.getEntityId());
            logger.info("refreshIgmpMcSniConfigMgmtObjects finished!");
        } catch (Exception e) {
            logger.error("refreshIgmpMcSniConfigMgmtObjects error:", e);
        }
        LoggerUtil.topoEndTimeLog(event.getIpAddress(), "IgmpMcSniConfigMgmtObjects", timeTmp);

        timeTmp = LoggerUtil.topoStartTimeLog(event.getIpAddress(), "IgmpMcUniConfigTable");
        try {
            refreshIgmpMcUniConfigTable(event.getEntityId());
            logger.info("refreshIgmpMcUniConfigTable finished!");
        } catch (Exception e) {
            logger.error("refreshIgmpMcUniConfigTable error:", e);
        }
        LoggerUtil.topoEndTimeLog(event.getIpAddress(), "IgmpMcUniConfigTable", timeTmp);

        timeTmp = LoggerUtil.topoStartTimeLog(event.getIpAddress(), "IgmpProxyParaTable");
        try {
            refreshIgmpProxyParaTable(event.getEntityId());
            logger.info("refreshIgmpProxyParaTable finished!");
        } catch (Exception e) {
            logger.error("refreshIgmpProxyParaTable error:", e);
        }
        LoggerUtil.topoEndTimeLog(event.getIpAddress(), "IgmpProxyParaTable", timeTmp);

        timeTmp = LoggerUtil.topoStartTimeLog(event.getIpAddress(), "TopIgmpForwardingSnooping");
        try {
            refreshIgmpSnooping(event.getEntityId());
            logger.info("refreshIgmpSnooping finished!");
        } catch (Exception e) {
            logger.error("refreshIgmpSnooping error:", e);
        }
        LoggerUtil.topoEndTimeLog(event.getIpAddress(), "TopIgmpForwardingSnooping", timeTmp);

        timeTmp = LoggerUtil.topoStartTimeLog(event.getIpAddress(), "IgmpMcOnuTable");
        try {
            refreshIgmpMcOnuTable(event.getEntityId());
            logger.info("refreshIgmpMcOnuTable finished!");
        } catch (Exception e) {
            logger.error("refreshIgmpMcOnuTable error:", e);
        }
        LoggerUtil.topoEndTimeLog(event.getIpAddress(), "IgmpMcOnuTable", timeTmp);

        logger.info("finish discovery IgmpData EntityId:" + event.getEntityId());
        */
    }

    /**
     * 同步设备业务属性
     * 
     * @param event
     */
    @Override
    public void updateEntityStates(SynchronizedEvent event) {
    }

    /**
     * 添加一个组播组
     * 
     * @param igmpControlledMulticastPackageTable
     *            组播组
     * @throws AddIgmpMvlanException
     */
    @Override
    public void addIgmpMvlan(IgmpControlledMulticastPackageTable igmpControlledMulticastPackageTable) {
        // 获得设备实体
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(igmpControlledMulticastPackageTable.getEntityId());
        // 获取facade
        OltIgmpFacade oltIgmpFacade = getOltIgmpFacade(snmpParam.getIpAddress());

        Integer previewInterval = igmpControlledMulticastPackageTable.getPreviewInterval();
        try {
            igmpControlledMulticastPackageTable.setPreviewInterval(null);
            oltIgmpFacade.addIgmpMvlan(snmpParam, igmpControlledMulticastPackageTable);

            IgmpControlledMcPreviewIntervalTable igmpControlledMcPreviewInterval = new IgmpControlledMcPreviewIntervalTable();
            igmpControlledMcPreviewInterval.setEntityId(igmpControlledMulticastPackageTable.getEntityId());
            igmpControlledMcPreviewInterval.setCmIndex(igmpControlledMulticastPackageTable.getCmIndex());
            igmpControlledMcPreviewInterval.setPreviewInterval(previewInterval);
            oltIgmpFacade.modifyMcPreviewInterval(snmpParam, igmpControlledMcPreviewInterval);
        } catch (Exception e) {
            throw new AddIgmpMvlanException(e);
        }
        igmpControlledMulticastPackageTable.setPreviewInterval(previewInterval);
        oltIgmpDao.addIgmpMvlan(igmpControlledMulticastPackageTable);
    }

    /**
     * 添加一个频道
     * 
     * @param igmpProxyParaTable
     *            频道
     * @throws AddIgmpProxyException
     */
    @Override
    public void addIgmpProxy(IgmpProxyParaTable igmpProxyParaTable) {
        // 获得设备实体
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(igmpProxyParaTable.getEntityId());
        // 获取facade
        OltIgmpFacade oltIgmpFacade = getOltIgmpFacade(snmpParam.getIpAddress());
        try {
            oltIgmpFacade.addIgmpProxy(snmpParam, igmpProxyParaTable);
            oltIgmpDao.addIgmpProxy(igmpProxyParaTable);
        } catch (Exception e) {
            throw new AddIgmpProxyException(e);
        }
    }

    /**
     * 添加组播VLAN转换规则
     * 
     * @param igmpMcOnuVlanTransTable
     *            组播VLAN转换规则
     * @throws AddIgmpVlanTransException
     */
    @Override
    public void addIgmpVlanTrans(IgmpMcOnuVlanTransTable igmpMcOnuVlanTransTable) {
        // 获得设备实体
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(igmpMcOnuVlanTransTable.getEntityId());
        // 获取facade
        OltIgmpFacade oltIgmpFacade = getOltIgmpFacade(snmpParam.getIpAddress());
        try {
            oltIgmpFacade.addIgmpVlanTrans(snmpParam, igmpMcOnuVlanTransTable);
            oltIgmpDao.addIgmpVlanTrans(igmpMcOnuVlanTransTable);
        } catch (Exception e) {
            throw new AddIgmpVlanTransException(e);
        }
    }

    /**
     * 删除一个组播组
     * 
     * @param entityId
     *            设备ID
     * @param cmIndex
     *            组播组INDEX
     * @throws DeleteIgmpMvlanException
     */
    @Override
    public void deleteIgmpMvlan(Long entityId, Integer cmIndex) {
        // 获得设备实体
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        // 获取facade
        OltIgmpFacade oltIgmpFacade = getOltIgmpFacade(snmpParam.getIpAddress());
        try {
            IgmpControlledMulticastPackageTable igmpControlledMulticastPackageTable = new IgmpControlledMulticastPackageTable();
            igmpControlledMulticastPackageTable.setEntityId(entityId);
            igmpControlledMulticastPackageTable.setCmIndex(cmIndex);
            oltIgmpFacade.deleteIgmpMvlan(snmpParam, igmpControlledMulticastPackageTable);
            oltIgmpDao.deleteIgmpMvlan(igmpControlledMulticastPackageTable);
        } catch (Exception e) {
            throw new DeleteIgmpMvlanException(e);
        }
    }

    /**
     * 删除一个频道
     * 
     * @param entityId
     *            设备ID
     * @param proxyIndex
     *            频道INDEX
     * @throws DeleteIgmpProxyException
     */
    @Override
    public void deleteIgmpProxy(Long entityId, Integer proxyIndex) {
        // 获得设备实体
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        // 获取facade
        OltIgmpFacade oltIgmpFacade = getOltIgmpFacade(snmpParam.getIpAddress());
        try {
            IgmpProxyParaTable igmpProxyParaTable = new IgmpProxyParaTable();
            igmpProxyParaTable.setEntityId(entityId);
            igmpProxyParaTable.setProxyIndex(proxyIndex);
            oltIgmpFacade.deleteIgmpProxy(snmpParam, igmpProxyParaTable);
            oltIgmpDao.deleteIgmpProxy(igmpProxyParaTable);
        } catch (Exception e) {
            throw new DeleteIgmpProxyException(e);
        }
    }

    /**
     * 删除组播VLAN转换规则
     * 
     * @param entityId
     *            设备ID
     * @param topMcOnuVlanTransIndex
     *            组播VLAN转换规则INDEX
     * @throws DeleteIgmpVlanTransException
     */
    @Override
    public void deleteIgmpVlanTrans(Long entityId, Integer topMcOnuVlanTransIndex) {
        // 获得设备实体
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        // 获取facade
        OltIgmpFacade oltIgmpFacade = getOltIgmpFacade(snmpParam.getIpAddress());
        try {
            IgmpMcOnuVlanTransTable igmpMcOnuVlanTransTable = new IgmpMcOnuVlanTransTable();
            igmpMcOnuVlanTransTable.setEntityId(entityId);
            igmpMcOnuVlanTransTable.setTopMcOnuVlanTransIndex(topMcOnuVlanTransIndex);
            oltIgmpFacade.deleteIgmpVlanTrans(snmpParam, igmpMcOnuVlanTransTable);
            oltIgmpDao.deleteIgmpVlanTrans(igmpMcOnuVlanTransTable);
        } catch (Exception e) {
            throw new DeleteIgmpVlanTransException(e);
        }
    }

    /**
     * 获取呼叫信息记录
     * 
     * @param entityId
     *            设备ID
     * @return <IgmpControlledMcCdrTable>
     */
    @Override
    public List<IgmpControlledMcCdrTable> getIgmpControlledMcCdr(Long entityId) {
        return oltIgmpDao.getIgmpControlledMcCdr(entityId);
    }

    /**
     * 获取组播组当前活跃pon口列表
     * 
     * @param entityId
     *            设备ID
     * @return List<IgmpForwardingTable>
     */
    @Override
    public List<IgmpForwardingTable> getIgmpForwardingInfo(Long entityId) {
        return oltIgmpDao.getIgmpForwardingInfo(entityId);
    }

    /**
     * 获得IGMP全局属性
     * 
     * @param entityId
     *            设备ID
     * @return
     */
    @Override
    public IgmpEntityTable getIgmpGlobalInfo(Long entityId) {
        return oltIgmpDao.getIgmpGlobalInfo(entityId);
    }

    /**
     * 获取最大组播组数目
     * 
     * @param entityId
     *            设备ID
     * @return
     */
    @Override
    public IgmpMcParamMgmtObjects getIgmpMaxGroupNum(Long entityId) {
        return oltIgmpDao.getIgmpMaxGroupNum(entityId);
    }

    /**
     * 获取ONU IGMP信息
     * 
     * @param entityId
     *            设备ID
     * @param onuIndex
     *            ONU INDEX
     * @return IgmpMcOnuTable
     */
    @Override
    public IgmpMcOnuTable getIgmpMcOnuInfo(Long entityId, Long onuIndex) {
        return oltIgmpDao.getIgmpMcOnuInfo(entityId, onuIndex);
    }

    /**
     * 获取所有ONU IGMP模式信息
     * 
     * @param entityId
     *            设备ID
     * @return List<IgmpMcOnuTable>
     */
    @Override
    public List<IgmpMcOnuTable> getIgmpMcOnuInfoList(Long entityId) {
        return oltIgmpDao.getIgmpMcOnuInfo(entityId);
    }

    /**
     * 获取UNI口的IGMP信息
     * 
     * @param entityId
     *            设备ID
     * @param uniIndex
     *            UNI INDEX
     * @return IgmpMcUniConfigTable
     */
    @Override
    public IgmpMcUniConfigTable getIgmpMcUniConfig(Long entityId, Long uniIndex) {
        return oltIgmpDao.getIgmpMcUniConfig(entityId, uniIndex);
    }

    /**
     * 获取组播组列表
     * 
     * @param entityId
     *            设备ID
     * @return List<IgmpControlledMulticastPackageTable>
     */
    @Override
    public List<IgmpControlledMulticastPackageTable> getIgmpMvlanInfo(Long entityId) {
        return oltIgmpDao.getIgmpMvlanInfo(entityId);
    }

    /**
     * 获取组播组频道LIST
     * 
     * @param entityId
     *            设备ID
     * @param cmIndex
     *            组播组INDEX
     * @return List<Long>
     */
    @Override
    public List<Integer> getIgmpMvlanProxyList(Long entityId, Integer cmIndex) {
        return oltIgmpDao.getIgmpControlledMulticastPackageTable(entityId, cmIndex).getCmProxyListNum();
    }

    /**
     * 获取 PON口IGMP信息
     * 
     * @param entityId
     *            设备ID
     * @param ponIndex
     *            PON口INDEX
     * @return List<Integer>
     */
    @Override
    public List<Integer> getIgmpUniPortInfo(Long entityId, Long uniIndex) {
        IgmpControlledMulticastUserAuthorityTable tempObj = oltIgmpDao.getIgmpControlledMulticastUserAuthorityTable(
                entityId, uniIndex);
        List<Integer> tempList = new ArrayList<Integer>();
        if (tempObj != null) {
            tempList = tempObj.getMulticastPackageListNum();
        }
        return tempList;
    }

    /**
     * 获取组播组频道列表
     * 
     * @param entityId
     *            设备ID
     * @return List<IgmpProxyParaTable>
     */
    @Override
    public List<IgmpProxyParaTable> getIgmpProxyInfo(Long entityId) {
        return oltIgmpDao.getIgmpProxyInfo(entityId);
    }

    /**
     * 获取SNI口的IGMP全局属性
     * 
     * @param entityId
     *            设备ID
     * @return
     */
    @Override
    public IgmpMcSniConfigMgmtObjects getIgmpSniConfig(Long entityId) {
        return oltIgmpDao.getIgmpSniConfig(entityId);
    }

    /**
     * 获取组播VLAN转换列表
     * 
     * @param entityId
     *            设备ID
     * @return List<IgmpMcOnuVlanTransTable>
     */
    @Override
    public List<IgmpMcOnuVlanTransTable> getIgmpVlanTrans(Long entityId) {
        return oltIgmpDao.getIgmpVlanTrans(entityId);
    }

    /**
     * 修改IGMP全局属性
     * 
     * @param igmpEntityTable
     *            IGMP全局配置
     * @throws ModifyIgmpGlobalInfoException
     */
    @Override
    public void modifyIgmpGlobalInfo(IgmpEntityTable igmpEntityTable) {
        // 获得设备实体
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(igmpEntityTable.getEntityId());
        // 获取facade
        OltIgmpFacade oltIgmpFacade = getOltIgmpFacade(snmpParam.getIpAddress());
        try {
            oltIgmpFacade.modifyIgmpGlobalInfo(snmpParam, igmpEntityTable);
            oltIgmpDao.modifyIgmpGlobalInfo(igmpEntityTable);
        } catch (Exception e) {
            throw new ModifyIgmpGlobalInfoException(e);
        }
    }

    /**
     * 修改最大组播组数
     * 
     * @param entityId
     *            设备ID
     * @param topMcMaxGroupNum
     *            最大组播组数
     * @throws ModifyIgmpMaxGroupNumException
     */
    @Override
    public void modifyIgmpMaxGroupNum(Long entityId, Integer topMcMaxGroupNum, Long topMcMaxBw,
            Integer topMcSnoopingAgingTime, List<Integer> topMcMVlanList) {
        // 获得设备实体
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        // 获取facade
        OltIgmpFacade oltIgmpFacade = getOltIgmpFacade(snmpParam.getIpAddress());
        try {
            IgmpMcParamMgmtObjects igmpMcParamMgmtObjects = new IgmpMcParamMgmtObjects();
            igmpMcParamMgmtObjects.setEntityId(entityId);
            igmpMcParamMgmtObjects.setTopMcMaxGroupNum(topMcMaxGroupNum);
            igmpMcParamMgmtObjects.setTopMcMaxBw(topMcMaxBw);
            igmpMcParamMgmtObjects.setTopMcSnoopingAgingTime(topMcSnoopingAgingTime);
            igmpMcParamMgmtObjects.setTopMcMVlanList(topMcMVlanList);
            oltIgmpFacade.modifyIgmpMaxGroupNum(snmpParam, igmpMcParamMgmtObjects);
            oltIgmpDao.modifyIgmpMaxGroupNum(igmpMcParamMgmtObjects);
        } catch (Exception e) {
            throw new ModifyIgmpMaxGroupNumException(e);
        }
    }

    /**
     * 修改ONU IGMP信息
     * 
     * @param igmpMcOnuTable
     *            ONU组播组信息
     * @throws ModifyIgmpMcOnuInfoException
     */
    @Override
    public void modifyIgmpMcOnuInfo(IgmpMcOnuTable igmpMcOnuTable) {
        // 获得设备实体
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(igmpMcOnuTable.getEntityId());
        // 获取facade
        OltIgmpFacade oltIgmpFacade = getOltIgmpFacade(snmpParam.getIpAddress());
        try {
            // 配置为关闭模式，快速离开不能进行配置
            if (igmpMcOnuTable.getTopMcOnuMode() == 3) {
                igmpMcOnuTable.setTopMcOnuFastLeave(null);
            }
            oltIgmpFacade.modifyIgmpMcOnuInfo(snmpParam, igmpMcOnuTable);
            oltIgmpDao.modifyIgmpMcOnuInfo(igmpMcOnuTable);
        } catch (Exception e) {
            throw new ModifyIgmpMcOnuInfoException(e);
        }
    }

    /**
     * 添加一个ONU下的IGMP的UNI口IGMP信息
     * 
     * @param entityId
     *            设备索引
     * @param onuIndex
     *            onu索引
     * @throws Exception
     */
    @Override
    @Deprecated
    public void addIgmpUniInOnu(Long entityId, Long onuIndex) {
        // 获得设备实体
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        OltIgmpFacade oltIgmpFacade = getOltIgmpFacade(snmpParam.getIpAddress());
        Long onuId = onuDao.getOnuIdByIndex(entityId, onuIndex);
        List<OltUniAttribute> uniList = oltSlotDao.getSlotUniList(onuId);
        IgmpControlledMulticastUserAuthorityTable userAuthorityTable = new IgmpControlledMulticastUserAuthorityTable();
        userAuthorityTable.setEntityId(entityId);
        List<Integer> multicastPackageListNum = new ArrayList<Integer>();
        userAuthorityTable.setMulticastPackageListNum(multicastPackageListNum);
        for (OltUniAttribute uni : uniList) {
            userAuthorityTable.setPortIndex(uni.getUniIndex());
            try {
                // 调用facede方法设置值到设备中
                oltIgmpFacade.addMulticastUserAuthorityList(snmpParam, userAuthorityTable);
                // 调用dao存储到数据库中
                oltIgmpDao.modifyMulticastUserAuthorityList(userAuthorityTable);
            } catch (Exception e) {
                logger.info(e.getMessage());
            }
        }
    }

    /**
     * 添加一个UNI口IGMP信息
     * 
     * @param entityId
     *            设备索引
     * @param uniIndex
     *            uni索引
     * @throws Exception
     */
    @Override
    public void addIgmpUni(Long entityId, Long uniIndex, List<Integer> multicastPackageListNum) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        OltIgmpFacade oltIgmpFacade = getOltIgmpFacade(snmpParam.getIpAddress());
        IgmpControlledMulticastUserAuthorityTable userAuthorityTable = new IgmpControlledMulticastUserAuthorityTable();
        userAuthorityTable.setEntityId(entityId);
        userAuthorityTable.setPortIndex(uniIndex);
        userAuthorityTable.setMulticastPackageListNum(multicastPackageListNum);
        IgmpMcUniConfigTable igmpMcUniConfig = new IgmpMcUniConfigTable();
        igmpMcUniConfig.setEntityId(entityId);
        igmpMcUniConfig.setUniIndex(uniIndex);
        igmpMcUniConfig.setTopMcUniMaxGroupQuantity(4);// 未设置时的默认值
        try {
            // 调用facede方法设置值到设备中
            oltIgmpFacade.addMulticastUserAuthorityList(snmpParam, userAuthorityTable);
            // 调用dao存储到数据库中
            oltIgmpDao.addMulticastUserAuthorityList(userAuthorityTable);
            oltIgmpDao.addMcUniConfig(igmpMcUniConfig);
        } catch (Exception e) {
            throw new ModifyMulticastUserAuthorityListException(e);
        }
    }

    /**
     * 修改一个UNI口IGMP信息
     * 
     * @param entityId
     *            设备索引
     * @param uniIndex
     *            uni索引
     * @throws Exception
     */
    @Override
    public void modifyIgmpUni(Long entityId, Long uniIndex, List<Integer> multicastPackageListNum) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        OltIgmpFacade oltIgmpFacade = getOltIgmpFacade(snmpParam.getIpAddress());
        IgmpControlledMulticastUserAuthorityTable userAuthorityTable = new IgmpControlledMulticastUserAuthorityTable();
        userAuthorityTable.setEntityId(entityId);
        userAuthorityTable.setPortIndex(uniIndex);
        userAuthorityTable.setMulticastPackageListNum(multicastPackageListNum);
        try {
            // 调用facede方法设置值到设备中
            oltIgmpFacade.modifyMulticastUserAuthorityList(snmpParam, userAuthorityTable);
            // 调用dao存储到数据库中
            oltIgmpDao.modifyMulticastUserAuthorityList(userAuthorityTable);
        } catch (Exception e) {
            throw new ModifyMulticastUserAuthorityListException(e);
        }
    }

    /**
     * 删除一个UNI口IGMP信息
     * 
     * @param entityId
     *            设备索引
     * @param uniIndex
     *            uni索引
     * @throws Exception
     */
    @Override
    public void deleteIgmpUniInOnu(Long entityId, Long uniIndex) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        OltIgmpFacade oltIgmpFacade = getOltIgmpFacade(snmpParam.getIpAddress());
        IgmpControlledMulticastUserAuthorityTable userAuthorityTable = new IgmpControlledMulticastUserAuthorityTable();
        userAuthorityTable.setEntityId(entityId);
        userAuthorityTable.setPortIndex(uniIndex);
        IgmpMcUniConfigTable igmpMcUniConfig = new IgmpMcUniConfigTable();
        igmpMcUniConfig.setEntityId(entityId);
        igmpMcUniConfig.setUniIndex(uniIndex);
        try {
            // 调用facede方法设置值到设备中
            oltIgmpFacade.deleteMulticastUserAuthorityList(snmpParam, userAuthorityTable);
            // 调用dao存储到数据库中
            oltIgmpDao.deleteMulticastUserAuthorityList(userAuthorityTable);
            oltIgmpDao.deleteMcUniConfig(igmpMcUniConfig);
        } catch (Exception e) {
            throw new ModifyMulticastUserAuthorityListException(e);
        }
    }

    /**
     * 从设备刷新 UNI口IGMP信息
     * 
     * @param entityId
     *            设备索引
     * @param uniIndex
     *            uni索引
     * @throws Exception
     */
    @Override
    public void refreshIgmpUniInOnu(Long entityId, Long uniIndex) {
        try {
            // 调用facede方法刷新设备数据到数据库
            SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
            OltIgmpFacade oltIgmpFacade = getOltIgmpFacade(snmpParam.getIpAddress());
            IgmpControlledMulticastUserAuthorityTable userAuthorityTable = new IgmpControlledMulticastUserAuthorityTable();
            IgmpMcUniConfigTable uniConfigTable = new IgmpMcUniConfigTable();
            userAuthorityTable.setPortIndex(uniIndex);
            uniConfigTable.setUniIndex(uniIndex);
            try {
                userAuthorityTable = oltIgmpFacade.getDomainInfoLine(snmpParam, userAuthorityTable);
            } catch (SnmpException e) {
                // 出现noSuchInstance情况后，表示该ONUUNI不存在，继续获得下一个
                if (e instanceof SnmpNoSuchInstanceException) {
                    userAuthorityTable = null;
                } else {
                    throw e;
                }
            }
            try {
                uniConfigTable = oltIgmpFacade.getDomainInfoLine(snmpParam, uniConfigTable);
            } catch (SnmpException e) {
                // 出现noSuchInstance情况后，表示该ONUUNI不存在，继续获得下一个
                if (e instanceof SnmpNoSuchInstanceException) {
                    uniConfigTable = null;
                } else {
                    throw e;
                }
            }
            if (userAuthorityTable != null) {
                userAuthorityTable.setEntityId(entityId);
                oltIgmpDao.deleteMulticastUserAuthorityList(userAuthorityTable);
                oltIgmpDao.addMulticastUserAuthorityList(userAuthorityTable);
            }
            if (uniConfigTable != null) {
                uniConfigTable.setEntityId(entityId);
                oltIgmpDao.deleteMcUniConfig(uniConfigTable);
                oltIgmpDao.addMcUniConfig(uniConfigTable);
            }
        } catch (Exception e) {
            throw new ModifyMulticastUserAuthorityListException(e);
        }
    }

    /**
     * 修改UNI口的IGMP信息
     * 
     * @param igmpMcUniConfigTable
     *            UNI口IGMP信息
     * @throws ModifyIgmpMcUniConfigException
     */
    @Override
    public void modifyIgmpMcUniConfig(IgmpMcUniConfigTable igmpMcUniConfigTable) {
        // 获得设备实体
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(igmpMcUniConfigTable.getEntityId());
        // 获取facade
        OltIgmpFacade oltIgmpFacade = getOltIgmpFacade(snmpParam.getIpAddress());
        try {
            oltIgmpFacade.modifyIgmpMcUniConfig(snmpParam, igmpMcUniConfigTable);
            oltIgmpDao.modifyIgmpMcUniConfig(igmpMcUniConfigTable);
        } catch (Exception e) {
            throw new ModifyIgmpMcUniConfigException(e);
        }
    }

    /**
     * 修改组播组信息
     * 
     * @param igmpControlledMulticastPackageTable
     *            组播组信息
     * @throws ModifyIgmpMvlanInfoException
     */
    @Override
    public void modifyIgmpMvlanInfo(IgmpControlledMulticastPackageTable igmpControlledMulticastPackageTable) {
        // 获得设备实体
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(igmpControlledMulticastPackageTable.getEntityId());
        // 获取facade
        OltIgmpFacade oltIgmpFacade = getOltIgmpFacade(snmpParam.getIpAddress());
        Integer previewInterval = igmpControlledMulticastPackageTable.getPreviewInterval();
        try {
            igmpControlledMulticastPackageTable.setPreviewInterval(null);
            oltIgmpFacade.modifyIgmpMvlanInfo(snmpParam, igmpControlledMulticastPackageTable);

            IgmpControlledMcPreviewIntervalTable igmpControlledMcPreviewInterval = new IgmpControlledMcPreviewIntervalTable();
            igmpControlledMcPreviewInterval.setEntityId(igmpControlledMulticastPackageTable.getEntityId());
            igmpControlledMcPreviewInterval.setCmIndex(igmpControlledMulticastPackageTable.getCmIndex());
            igmpControlledMcPreviewInterval.setPreviewInterval(previewInterval);
            oltIgmpFacade.modifyMcPreviewInterval(snmpParam, igmpControlledMcPreviewInterval);
        } catch (Exception e) {
            throw new ModifyIgmpMvlanInfoException(e);
        }
        igmpControlledMulticastPackageTable.setPreviewInterval(previewInterval);
        oltIgmpDao.modifyIgmpMvlanInfo(igmpControlledMulticastPackageTable);
    }

    /**
     * 修改频道
     * 
     * @param igmpProxyParaTable
     *            频道
     * @throws ModifyIgmpProxyInfoException
     */
    @Override
    public void modifyIgmpProxyInfo(IgmpProxyParaTable igmpProxyParaTable) {
        // 获得设备实体
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(igmpProxyParaTable.getEntityId());
        // 获取facade
        OltIgmpFacade oltIgmpFacade = getOltIgmpFacade(snmpParam.getIpAddress());
        try {
            oltIgmpFacade.modifyIgmpProxyInfo(snmpParam, igmpProxyParaTable);
            oltIgmpDao.modifyIgmpProxyInfo(igmpProxyParaTable);
        } catch (Exception e) {
            throw new ModifyIgmpProxyInfoException(e);
        }
    }

    /**
     * 修改IGMP SNI口全局属性
     * 
     * @param entityId
     *            设备ID
     * @param topMcSniPortType
     *            所有SNI口、指定SNI口、trunk组
     * @param topMcSniPort
     *            指定端口
     * @param topMcSniAggPort
     *            trunk组 ID
     * @throws ModifyIgmpSniConfigException
     */
    @Override
    public void modifyIgmpSniConfig(Long entityId, Integer topMcSniPortType, String topMcSniPort,
            Integer topMcSniAggPort) {
        // 获得设备实体
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        // 获取facade
        OltIgmpFacade oltIgmpFacade = getOltIgmpFacade(snmpParam.getIpAddress());
        IgmpMcSniConfigMgmtObjects igmpMcSniConfigMgmtObjects = new IgmpMcSniConfigMgmtObjects();
        igmpMcSniConfigMgmtObjects.setEntityId(entityId);
        igmpMcSniConfigMgmtObjects.setTopMcSniPortType(topMcSniPortType);
        switch (topMcSniPortType) {
        case 0: {
            igmpMcSniConfigMgmtObjects.setTopMcSniPort(null);
            igmpMcSniConfigMgmtObjects.setTopMcSniAggPort(null);
            oltIgmpFacade.modifyIgmpSniConfig(snmpParam, igmpMcSniConfigMgmtObjects);
            break;
        }
        case 1: {
            if (topMcSniPort != null) {
                try {
                    igmpMcSniConfigMgmtObjects.setTopMcSniPort(topMcSniPort);
                    igmpMcSniConfigMgmtObjects.setTopMcSniAggPort(null);
                    oltIgmpFacade.modifyIgmpSniConfig(snmpParam, igmpMcSniConfigMgmtObjects);
                } catch (Exception e) {
                    throw new ModifyIgmpSniConfigException(e);
                }
            } else {
                throw new ModifyIgmpSniConfigException("topMcSniPort is null");
            }
            break;
        }
        case 2: {
            if (topMcSniAggPort != null) {
                try {
                    igmpMcSniConfigMgmtObjects.setTopMcSniAggPort(topMcSniAggPort);
                    igmpMcSniConfigMgmtObjects.setTopMcSniPort(null);
                    oltIgmpFacade.modifyIgmpSniConfig(snmpParam, igmpMcSniConfigMgmtObjects);
                } catch (Exception e) {
                    throw new ModifyIgmpSniConfigException(e);
                }
            } else {
                throw new ModifyIgmpSniConfigException("topMcSniAggPort is null");
            }
            break;
        }
        }
        try {
            oltIgmpDao.modifyIgmpSniConfig(igmpMcSniConfigMgmtObjects);
        } catch (Exception e) {
            throw new ModifyIgmpSniConfigException(e);
        }
    }

    /**
     * 修改组播VLAN转换规则
     * 
     * @param igmpMcOnuVlanTransTable
     *            组播VLAN转换规则
     * @throws ModifyIgmpVlanTransException
     */
    @Override
    public void modifyIgmpVlanTrans(IgmpMcOnuVlanTransTable igmpMcOnuVlanTransTable) {
        // 获得设备实体
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(igmpMcOnuVlanTransTable.getEntityId());
        // 获取facade
        OltIgmpFacade oltIgmpFacade = getOltIgmpFacade(snmpParam.getIpAddress());
        try {
            oltIgmpFacade.modifyIgmpVlanTrans(snmpParam, igmpMcOnuVlanTransTable);
            oltIgmpDao.modifyIgmpVlanTrans(igmpMcOnuVlanTransTable);
        } catch (Exception e) {
            throw new ModifyIgmpVlanTransException(e);
        }
    }

    /**
     * 修改UNI口 IGMP信息
     * 
     * @param entityId
     *            设备ID
     * @param portIndex
     *            端口INDEX
     * @param multicastPackageList
     *            组播组ID列表
     * @throws ModifyMulticastUserAuthorityListException
     * 
     */
    @Override
    public void modifyMulticastUserAuthorityList(Long entityId, Long portIndex, List<Integer> multicastPackageList) {
        // 获得设备实体
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        // 获取facade
        OltIgmpFacade oltIgmpFacade = getOltIgmpFacade(snmpParam.getIpAddress());
        try {
            IgmpControlledMulticastUserAuthorityTable igmpControlledMulticastUserAuthorityTable = new IgmpControlledMulticastUserAuthorityTable();
            igmpControlledMulticastUserAuthorityTable.setEntityId(entityId);
            igmpControlledMulticastUserAuthorityTable.setPortIndex(portIndex);
            igmpControlledMulticastUserAuthorityTable.setMulticastPackageListNum(multicastPackageList);
            oltIgmpFacade.modifyMulticastUserAuthorityList(snmpParam, igmpControlledMulticastUserAuthorityTable);
            oltIgmpDao.modifyMulticastUserAuthorityList(igmpControlledMulticastUserAuthorityTable);
        } catch (Exception e) {
            throw new ModifyMulticastUserAuthorityListException(e);
        }
    }

    /**
     * 修改频道的组播组vlan ID
     * 
     * @param entityId
     *            设备ID
     * @param cmIndex
     *            频道对应的组播组ID
     * @param cmProxyListList
     *            可控组播的频道列表
     * @throws ModifyProxyMulticastVIDException
     * 
     */
    @Override
    public void modifyMulticastPackage(Long entityId, Integer cmIndex, List<Integer> cmProxyListList) {
        // 获得设备实体
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        // 获取facade
        OltIgmpFacade oltIgmpFacade = getOltIgmpFacade(snmpParam.getIpAddress());
        try {
            IgmpControlledMulticastPackageTable igmpControlledMulticastPackageTable = new IgmpControlledMulticastPackageTable();
            igmpControlledMulticastPackageTable.setEntityId(entityId);
            igmpControlledMulticastPackageTable.setCmIndex(cmIndex);
            igmpControlledMulticastPackageTable.setCmProxyListNum(cmProxyListList);
            oltIgmpFacade.modifyMulticastPackage(snmpParam, igmpControlledMulticastPackageTable);
            oltIgmpDao.modifyMulticastPackage(igmpControlledMulticastPackageTable);
        } catch (Exception e) {
            throw new ModifyProxyMulticastVIDException(e);
        }
    }

    /**
     * 刷新活跃PON口列表
     * 
     * @param entityId
     *            设备ID
     * @return List<IgmpForwardingTable>
     * @throws RefreshIgmpForwardingTableException
     * 
     */
    @Override
    public List<IgmpForwardingTable> refreshIgmpForwardingInfo(Long entityId) {
        List<IgmpForwardingTable> igmpForwardingTables;
        // 获得设备实体
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        // 获取facade
        OltIgmpFacade oltPerfFacade = getOltIgmpFacade(snmpParam.getIpAddress());
        // 从设备获取轮询间隔设置
        try {
            igmpForwardingTables = oltPerfFacade.getIgmpForwardingTable(snmpParam);
            for (IgmpForwardingTable igmpForwardingTable : igmpForwardingTables) {
                igmpForwardingTable.setEntityId(entityId);
            }
            // 将设置保存到数据库
            oltIgmpDao.saveIgmpForwardingTable(entityId, igmpForwardingTables);
        } catch (Exception e) {
            throw new RefreshIgmpForwardingTableException(e);
        }
        return igmpForwardingTables;
    }

    /**
     * 获取OltQosFacade对象
     * 
     * @param ip
     *            被采集设备IP
     * @return OltPerfFacade
     */
    private OltIgmpFacade getOltIgmpFacade(String ip) {
        return facadeFactory.getFacade(ip, OltIgmpFacade.class);
    }

    @Override
    public void refreshIgmpControlledMcCdrTable(Long entityId) {
        // 获得设备实体
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        // 获取facade
        OltIgmpFacade oltPerfFacade = getOltIgmpFacade(snmpParam.getIpAddress());
        // 从设备获取轮询间隔设置
        try {
            List<IgmpControlledMcCdrTable> igmpControlledMcCdrTables = oltPerfFacade
                    .getIgmpControlledMcCdrTable(snmpParam);
            for (IgmpControlledMcCdrTable igmpControlledMcCdrTable : igmpControlledMcCdrTables) {
                igmpControlledMcCdrTable.setEntityId(entityId);
            }
            // 将设置保存到数据库
            oltIgmpDao.saveIgmpControlledMcCdrTable(entityId, igmpControlledMcCdrTables);
        } catch (Exception e) {
            throw new RefreshIgmpControlledMcCdrTableException(e);
        }
    }

    @Override
    public void refreshIgmpControlledMulticastPackageTable(Long entityId) {
        // 获得设备实体
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        // 获取facade
        OltIgmpFacade oltPerfFacade = getOltIgmpFacade(snmpParam.getIpAddress());
        // 从设备获取轮询间隔设置
        try {
            List<IgmpControlledMulticastPackageTable> igmpControlledMulticastPackageTables = oltPerfFacade
                    .getIgmpControlledMulticastPackageTable(snmpParam);
            for (IgmpControlledMulticastPackageTable igmpControlledMulticastPackageTable : igmpControlledMulticastPackageTables) {
                igmpControlledMulticastPackageTable.setEntityId(entityId);
            }
            // 将设置保存到数据库
            oltIgmpDao.saveIgmpControlledMulticastPackageTable(entityId, igmpControlledMulticastPackageTables);

            List<IgmpControlledMcPreviewIntervalTable> IgmpControlledMcPreviewIntervalTables = oltPerfFacade
                    .getIgmpControlledMcPreviewIntervalTable(snmpParam);
            for (IgmpControlledMcPreviewIntervalTable igmpControlledMcPreviewIntervalTable : IgmpControlledMcPreviewIntervalTables) {
                igmpControlledMcPreviewIntervalTable.setEntityId(entityId);
            }
            // 将设置保存到数据库
            oltIgmpDao.saveIgmpControlledMcPreviewIntervalTable(entityId, IgmpControlledMcPreviewIntervalTables);
        } catch (Exception e) {
            throw new RefreshIgmpControlledMulticastPackageTableException(e);
        }
    }

    @Override
    public void refreshIgmpControlledMulticastUserAuthorityTable(Long entityId) {
        // 获得设备实体
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        // 获取facade
        OltIgmpFacade oltPerfFacade = getOltIgmpFacade(snmpParam.getIpAddress());
        // 从设备获取轮询间隔设置
        try {
            List<IgmpControlledMulticastUserAuthorityTable> igmpControlledMulticastUserAuthorityTables = oltPerfFacade
                    .getIgmpControlledMulticastUserAuthorityTable(snmpParam);
            for (IgmpControlledMulticastUserAuthorityTable igmpControlledMulticastUserAuthorityTable : igmpControlledMulticastUserAuthorityTables) {
                igmpControlledMulticastUserAuthorityTable.setEntityId(entityId);
            }
            // 将设置保存到数据库
            oltIgmpDao.saveIgmpControlledMulticastUserAuthorityTable(entityId,
                    igmpControlledMulticastUserAuthorityTables);
        } catch (Exception e) {
            throw new RefreshIgmpControlledMulticastUserAuthorityTableException(e);
        }
    }

    @Override
    public void refreshIgmpEntityTable(Long entityId) {
        // 获得设备实体
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        // 获取facade
        OltIgmpFacade oltPerfFacade = getOltIgmpFacade(snmpParam.getIpAddress());
        // 从设备获取轮询间隔设置
        try {
            List<IgmpEntityTable> igmpEntityTables = oltPerfFacade.getIgmpEntityTable(snmpParam);
            for (IgmpEntityTable igmpEntityTable : igmpEntityTables) {
                igmpEntityTable.setEntityId(entityId);
            }
            // 将设置保存到数据库
            oltIgmpDao.saveIgmpEntityTable(entityId, igmpEntityTables);
        } catch (Exception e) {
            throw new RefreshIgmpEntityTableException(e);
        }
    }

    @Override
    public void refreshIgmpForwardingTable(Long entityId) {
        // 获得设备实体
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        // 获取facade
        OltIgmpFacade oltPerfFacade = getOltIgmpFacade(snmpParam.getIpAddress());
        // 从设备获取轮询间隔设置
        try {
            List<IgmpForwardingTable> igmpForwardingTables = oltPerfFacade.getIgmpForwardingTable(snmpParam);
            for (IgmpForwardingTable igmpForwardingTable : igmpForwardingTables) {
                igmpForwardingTable.setEntityId(entityId);
            }
            // 将设置保存到数据库
            oltIgmpDao.saveIgmpForwardingTable(entityId, igmpForwardingTables);
        } catch (Exception e) {
            throw new RefreshIgmpForwardingTableException(e);
        }
    }

    @Override
    public void refreshIgmpMcOnuTable(Long entityId) {
        // 获得设备实体
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        // 获取facade
        OltIgmpFacade oltPerfFacade = getOltIgmpFacade(snmpParam.getIpAddress());
        // 从设备获取轮询间隔设置
        try {
            List<IgmpMcOnuTable> igmpMcOnuTables = oltPerfFacade.getIgmpMcOnuTable(snmpParam);
            for (IgmpMcOnuTable igmpMcOnuTable : igmpMcOnuTables) {
                igmpMcOnuTable.setEntityId(entityId);
            }
            // 将设置保存到数据库
            oltIgmpDao.saveIgmpMcOnuTable(entityId, igmpMcOnuTables);
        } catch (Exception e) {
            throw new RefreshIgmpMcOnuTableException(e);
        }
    }

    @Override
    public void refreshIgmpMcOnuVlanTransTable(Long entityId) {
        // 获得设备实体
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        // 获取facade
        OltIgmpFacade oltPerfFacade = getOltIgmpFacade(snmpParam.getIpAddress());
        // 从设备获取轮询间隔设置
        try {
            List<IgmpMcOnuVlanTransTable> igmpMcOnuVlanTransTables = oltPerfFacade
                    .getIgmpMcOnuVlanTransTable(snmpParam);
            for (IgmpMcOnuVlanTransTable igmpMcOnuVlanTransTable : igmpMcOnuVlanTransTables) {
                igmpMcOnuVlanTransTable.setEntityId(entityId);
            }
            // 将设置保存到数据库
            oltIgmpDao.saveIgmpMcOnuVlanTransTable(entityId, igmpMcOnuVlanTransTables);
        } catch (Exception e) {
            throw new RefreshIgmpMcOnuVlanTransTableException(e);
        }
    }

    @Override
    public void refreshIgmpMcParamMgmtObjects(Long entityId) {
        // 获得设备实体
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        // 获取facade
        OltIgmpFacade oltPerfFacade = getOltIgmpFacade(snmpParam.getIpAddress());
        // 从设备获取轮询间隔设置
        try {
            IgmpMcParamMgmtObjects igmpMcParamMgmtObject = oltPerfFacade.getIgmpMcParamMgmtObjects(snmpParam);
            igmpMcParamMgmtObject.setEntityId(entityId);
            // 将设置保存到数据库
            oltIgmpDao.saveIgmpMcParamMgmtObjects(entityId, igmpMcParamMgmtObject);
        } catch (Exception e) {
            throw new RefreshIgmpMcParamMgmtObjectsException(e);
        }
    }

    @Override
    public void refreshIgmpMcSniConfigMgmtObjects(Long entityId) {
        // 获得设备实体
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        // 获取facade
        OltIgmpFacade oltPerfFacade = getOltIgmpFacade(snmpParam.getIpAddress());
        // 从设备获取轮询间隔设置
        try {
            IgmpMcSniConfigMgmtObjects igmpMcSniConfigMgmtObject = oltPerfFacade
                    .getIgmpMcSniConfigMgmtObjects(snmpParam);
            igmpMcSniConfigMgmtObject.setEntityId(entityId);

            // 将设置保存到数据库
            oltIgmpDao.saveIgmpMcSniConfigMgmtObjects(entityId, igmpMcSniConfigMgmtObject);
        } catch (Exception e) {
            throw new RefreshIgmpMcSniConfigMgmtObjectsException(e);
        }
    }

    @Override
    public void refreshIgmpMcUniConfigTable(Long entityId) {
        // 获得设备实体
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        // 获取facade
        OltIgmpFacade oltPerfFacade = getOltIgmpFacade(snmpParam.getIpAddress());
        // 从设备获取轮询间隔设置
        try {
            List<IgmpMcUniConfigTable> igmpMcUniConfigTables = oltPerfFacade.getIgmpMcUniConfigTable(snmpParam);
            for (IgmpMcUniConfigTable igmpMcUniConfigTable : igmpMcUniConfigTables) {
                igmpMcUniConfigTable.setEntityId(entityId);
            }
            // 将设置保存到数据库
            oltIgmpDao.saveIgmpMcUniConfigTable(entityId, igmpMcUniConfigTables);
        } catch (Exception e) {
            throw new RefreshIgmpMcUniConfigTableException(e);
        }
    }

    @Override
    public void refreshIgmpProxyParaTable(Long entityId) {
        // 获得设备实体
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        // 获取facade
        OltIgmpFacade oltPerfFacade = getOltIgmpFacade(snmpParam.getIpAddress());
        // 从设备获取轮询间隔设置
        try {
            List<IgmpProxyParaTable> igmpProxyParaTables = oltPerfFacade.getIgmpProxyParaTable(snmpParam);
            for (IgmpProxyParaTable igmpProxyParaTable : igmpProxyParaTables) {
                igmpProxyParaTable.setEntityId(entityId);
            }
            // 将设置保存到数据库
            oltIgmpDao.saveIgmpProxyParaTable(entityId, igmpProxyParaTables);
        } catch (Exception e) {
            throw new RefreshIgmpProxyParaTableException(e);
        }
    }

    public EntityService getEntityService() {
        return entityService;
    }

    public void setEntityService(EntityService entityService) {
        this.entityService = entityService;
    }

    public FacadeFactory getFacadeFactory() {
        return facadeFactory;
    }

    public void setFacadeFactory(FacadeFactory facadeFactory) {
        this.facadeFactory = facadeFactory;
    }

    public MessageService getMessageService() {
        return messageService;
    }

    public void setMessageService(MessageService messageService) {
        this.messageService = messageService;
    }

    public OltIgmpDao getOltIgmpDao() {
        return oltIgmpDao;
    }

    public void setOltIgmpDao(OltIgmpDao oltIgmpDao) {
        this.oltIgmpDao = oltIgmpDao;
    }

    /**
     * @return the oltDao
     */
    public OltDao getOltDao() {
        return oltDao;
    }

    /**
     * @param oltDao
     *            the oltDao to set
     */
    public void setOltDao(OltDao oltDao) {
        this.oltDao = oltDao;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.epon.service.OltIgmpService#getTopMcForwardingSlot(java.lang.Long,
     * java.lang.Integer)
     */
    @Override
    public TopMcForwardingSlotTable getTopMcForwardingSlot(Long entityId, Integer proxyId) {
        return oltIgmpDao.getTopMcForwardingSlot(entityId, proxyId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.epon.service.OltIgmpService#getTopMcForwardingPort(java.lang.Long,
     * java.lang.Integer, java.lang.Integer)
     */
    @Override
    public TopMcForwardingPortTable getTopMcForwardingPort(Long entityId, Integer proxyId, Integer slotNo) {
        return oltIgmpDao.getTopMcForwardingPort(entityId, proxyId, slotNo);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.epon.service.OltIgmpService#getTopMcForwardingOnu(java.lang.Long,
     * java.lang.Integer, java.lang.Integer, java.lang.Integer)
     */
    @Override
    public TopMcForwardingOnuTable getTopMcForwardingOnu(Long entityId, Integer proxyId, Integer slotNo,
            Integer ponPortNo) {
        return oltIgmpDao.getTopMcForwardingOnu(entityId, proxyId, slotNo, ponPortNo);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.epon.service.OltIgmpService#refreshTopMcForwardingSlotTable(java.lang.Long)
     */
    @Override
    public void refreshTopMcForwardingSlotTable(Long entityId) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        List<TopMcForwardingSlotTable> topMcForwardingSlotTables = new ArrayList<TopMcForwardingSlotTable>();
        List<IgmpProxyParaTable> groups = oltIgmpDao.getIgmpProxyInfo(entityId);
        for (IgmpProxyParaTable group : groups) {
            try {
                TopMcForwardingSlotTable forwardingSlot = getOltIgmpFacade(snmpParam.getIpAddress())
                        .getTopMcForwardingSlotTable(snmpParam, group.getProxyIndex());
                if (forwardingSlot != null) {
                    topMcForwardingSlotTables.add(forwardingSlot);
                }
            } catch (SnmpException e) {
                // 出现noSuchInstance情况后，继续获得下一个
                if (e instanceof SnmpNoSuchInstanceException) {
                    continue;
                } else {
                    throw e;
                }
            }
        }
        for (TopMcForwardingSlotTable topMcForwardingSlotTable : topMcForwardingSlotTables) {
            topMcForwardingSlotTable.setEntityId(snmpParam.getEntityId());
        }
        oltIgmpDao.batchInsertTopMcForwardingSlotTables(topMcForwardingSlotTables, snmpParam.getEntityId());
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.epon.service.OltIgmpService#refreshTopMcForwardingPortTable(java.lang.Long)
     */
    @Override
    public void refreshTopMcForwardingPortTable(Long entityId) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        List<TopMcForwardingPortTable> topMcForwardingPortTables = new ArrayList<TopMcForwardingPortTable>();
        List<TopMcForwardingSlotTable> topMcForwardingSlotTables = oltIgmpDao.getForwardingSlot(entityId);
        for (TopMcForwardingSlotTable forwardingSlot : topMcForwardingSlotTables) {
            for (Integer slotIndex : forwardingSlot.getTopMcSlotListIntegers()) {
                try {
                    TopMcForwardingPortTable forwardingPort = getOltIgmpFacade(snmpParam.getIpAddress())
                            .getTopMcForwardingPortTable(snmpParam, forwardingSlot.getTopMcGroupIdIndex(), slotIndex);
                    if (forwardingPort != null) {
                        topMcForwardingPortTables.add(forwardingPort);
                    }
                } catch (SnmpException e) {
                    // 出现noSuchInstance情况后，继续获得下一个
                    if (e instanceof SnmpNoSuchInstanceException) {
                        continue;
                    } else {
                        throw e;
                    }
                }
            }
        }
        for (TopMcForwardingPortTable topMcForwardingPortTable : topMcForwardingPortTables) {
            topMcForwardingPortTable.setEntityId(snmpParam.getEntityId());
        }
        oltIgmpDao.batchInsertTopMcForwardingPortTables(topMcForwardingPortTables, snmpParam.getEntityId());
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.epon.service.OltIgmpService#refreshTopMcForwardingOnuTable(java.lang.Long)
     */
    @Override
    public void refreshTopMcForwardingOnuTable(Long entityId) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        List<TopMcForwardingOnuTable> topMcForwardingOnuTables = new ArrayList<TopMcForwardingOnuTable>();
        List<TopMcForwardingPortTable> topMcForwardingPortTables = oltIgmpDao.getForwardingPort(entityId);
        for (TopMcForwardingPortTable forwardingPort : topMcForwardingPortTables) {
            for (Integer portIndex : forwardingPort.getTopMcPortListIntegers()) {
                try {
                    TopMcForwardingOnuTable forwardingOnu = getOltIgmpFacade(snmpParam.getIpAddress())
                            .getTopMcForwardingOnuTable(snmpParam, forwardingPort.getTopMcGroupIdIndex(),
                                    forwardingPort.getTopMcSlotIndex(), portIndex);
                    if (forwardingOnu != null) {
                        topMcForwardingOnuTables.add(forwardingOnu);
                    }
                } catch (SnmpException e) {
                    // 出现noSuchInstance情况后，继续获得下一个
                    if (e instanceof SnmpNoSuchInstanceException) {
                        continue;
                    } else {
                        throw e;
                    }
                }
            }
        }
        for (TopMcForwardingOnuTable topMcForwardingOnuTable : topMcForwardingOnuTables) {
            topMcForwardingOnuTable.setEntityId(snmpParam.getEntityId());
        }
        oltIgmpDao.batchInsertTopMcForwardingOnuTables(topMcForwardingOnuTables, snmpParam.getEntityId());
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.epon.service.OltIgmpService#getIgmpControlledMcCdr(java.lang.Long,
     * int, int)
     */
    @Override
    public List<IgmpControlledMcCdrTable> getIgmpControlledMcCdr(Long entityId, int start, int limit) {
        return oltIgmpDao.getIgmpControlledMcCdr(entityId, start, limit);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.message.event.TrapListener#onTrapMessage(com.topvision.ems.message.event
     * .TrapEvent)
     */
    @Override
    public void onTrapMessage(TrapEvent evt) {
        if (evt.getCode() == EponCode.IGMP_CALL_CRD) {
            Trap e = evt.getTrap();
            Entity entity = entityDao.getEntityByIp(evt.getTrap().getAddress());
            SnmpParam snmpParam = entityService.getSnmpParamByEntity(entity.getEntityId());
            String message = e.getVariableBindings().get(OltTrapConstants.TRAP_OBJECT_ADDITIONALTEXT).toString();
            Long temp[] = EponUtil.getArrayByHexString(message);
            List<IgmpControlledMcCdrTable> igmpCdrTables = getOltIgmpFacade(snmpParam.getIpAddress())
                    .getIgmpControlledMcCdrTableList(snmpParam, temp[0], temp[1]);
            oltIgmpDao.saveIgmpControlledMcCdrTable(entity.getEntityId(), igmpCdrTables);
        }
    }

    public EntityDao getEntityDao() {
        return entityDao;
    }

    public void setEntityDao(EntityDao entityDao) {
        this.entityDao = entityDao;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.epon.service.OltIgmpService#loadIgmpSnoopingData(java.lang.Long,
     * java.lang.Long, java.lang.Integer)
     */
    @Override
    public List<IgmpForwardingSnooping> loadIgmpSnoopingData(Long entityId, Long portIndex, Integer vid) {
        return oltIgmpDao.loadIgmpSnoopingData(entityId, portIndex, vid);

    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.epon.service.OltIgmpService#refreshIgmpSnooping()
     */
    @Override
    public void refreshIgmpSnooping(Long entityId) {
        // 获得设备实体
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        // 获取facade
        OltIgmpFacade oltIgmpFacade = getOltIgmpFacade(snmpParam.getIpAddress());
        List<TopIgmpForwardingSnooping> list = oltIgmpFacade.refreshIgmpSnooping(snmpParam);
        oltIgmpDao.batchInsertTopIgmpForwardingSnooping(list, entityId);
    }
}
