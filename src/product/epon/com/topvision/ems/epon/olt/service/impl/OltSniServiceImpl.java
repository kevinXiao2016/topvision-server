/***********************************************************************
 * $Id: OltServiceImpl.java,v1.0 2013-10-25 上午10:28:47 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.olt.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.epon.exception.SetValueConflictException;
import com.topvision.ems.epon.olt.dao.OltSlotDao;
import com.topvision.ems.epon.olt.dao.OltSniDao;
import com.topvision.ems.epon.olt.domain.OltAttribute;
import com.topvision.ems.epon.olt.domain.OltSlotAttribute;
import com.topvision.ems.epon.olt.domain.OltSniAttribute;
import com.topvision.ems.epon.olt.domain.OltSniMacAddress;
import com.topvision.ems.epon.olt.domain.OltSniRedirect;
import com.topvision.ems.epon.olt.domain.OltSniStormSuppressionEntry;
import com.topvision.ems.epon.olt.facade.OltSniFacade;
import com.topvision.ems.epon.olt.service.OltSniService;
import com.topvision.ems.network.dao.EntityDao;
import com.topvision.ems.network.service.EntityService;
import com.topvision.framework.service.BaseService;
import com.topvision.framework.snmp.RowStatus;
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
@Service("oltSniService")
public class OltSniServiceImpl extends BaseService implements OltSniService, SynchronizedListener {
    @Autowired
    private OltSniDao oltSniDao;
    @Autowired
    private OltSlotDao oltSlotDao;
    @Autowired
    private EntityService entityService;
    @Autowired
    private FacadeFactory facadeFactory;
    @Autowired
    private MessageService messageService;
    @Autowired
    private EntityDao entityDao;

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
    public void insertEntityStates(SynchronizedEvent event) {
        try {
            refreshOltSniStormSuppressionEntry(event.getEntityId());
            logger.info("refreshOltSniStormSuppressionEntry finish");
        } catch (Exception e) {
            logger.error("refreshOltSniStormSuppressionEntry wrong", e);
        }
    }

    @Override
    public void refreshOltSniStormSuppressionEntry(Long entityId) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        List<OltSniStormSuppressionEntry> oltSniStormSuppressionEntries = getOltSniFacade(snmpParam.getIpAddress())
                .getoltSniStormSuppressionEntries(snmpParam);
        if (oltSniStormSuppressionEntries != null) {
            for (int i = 0; i < oltSniStormSuppressionEntries.size(); i++) {
                oltSniStormSuppressionEntries.get(i).setEntityId(entityId);
            }
            oltSniDao.batchInsertOltSniStormSuppressionEntry(oltSniStormSuppressionEntries, entityId);
        }
    }

    @Override
    public OltSniAttribute getSniAttribute(Long sniId) {
        return oltSniDao.getSniAttribute(sniId);
    }

    @Override
    public OltSniAttribute getSniAttribute(Long entityId, Long sniIndex) {
        return oltSniDao.getSniAttribute(entityId, sniIndex);
    }

    @Override
    public List<OltSniAttribute> getSniAttrList(Long entityId, List<Long> sniIndexList) {
        return null;
    }

    @Override
    public List<OltSniAttribute> getAllSniList(Long entityId) {
        // 该方法可用来获得所有的SNI口信息
        return oltSniDao.availableSniListForTrunkGroupByEntityId(entityId);
    }

    @Override
    public void setSniName(Long entityId, Long sniId, String name) {
        Long sniIndex = oltSniDao.getSniIndex(sniId);
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        String newSniName = getOltSniFacade(snmpParam.getIpAddress()).setSniName(snmpParam, sniIndex, name);
        if (newSniName == null) {
            throw new SetValueConflictException("Business.connection");
        } else {
            oltSniDao.updateSniPortName(sniId, newSniName);
            if (!newSniName.equals(name)) {
                throw new SetValueConflictException("Business.setSniName");
            }
        }
    }

    @Override
    public void setSniAdminStatus(Long entityId, Long sniId, Integer adminStatus) {
        Long sniIndex = oltSniDao.getSniIndex(sniId);
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        // 调用facade后返回值使用方式待确认
        Integer newStatus = getOltSniFacade(snmpParam.getIpAddress()).setSniAdminStatus(snmpParam, sniIndex,
                adminStatus);
        if (newStatus == null) {
            throw new SetValueConflictException("Business.connection");
        } else {
            oltSniDao.updateSniAdminStatus(sniId, newStatus);
            if (!newStatus.equals(adminStatus)) {
                throw new SetValueConflictException("Business.setAdminStatus");
            }
        }
    }

    @Override
    public void setSniIsolationStatus(Long entityId, Long sniId, Integer isolationStatus) {
        Long sniIndex = oltSniDao.getSniIndex(sniId);
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        Integer newStatus = getOltSniFacade(snmpParam.getIpAddress()).setSniIsolationStatus(snmpParam, sniIndex,
                isolationStatus);
        if (newStatus == null) {
            throw new SetValueConflictException("Business.connection");
        } else {
            oltSniDao.updateSniIsolationStatus(sniId, newStatus);
            if (!newStatus.equals(isolationStatus)) {
                throw new SetValueConflictException("Business.setSniIsolationStatus");
            }
        }
    }

    @Override
    public void setSniFlowControl(Long entityId, Long sniId, Integer ingressRate, Integer egressRate) {
        Long sniIndex = oltSniDao.getSniIndex(sniId);
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        OltSniAttribute oltSniAttribute = getOltSniFacade(snmpParam.getIpAddress()).setSniFlowControl(snmpParam,
                sniIndex, ingressRate, egressRate);
        Integer newIngressRate = oltSniAttribute.getTopSniAttrIngressRate();
        Integer newEgressRate = oltSniAttribute.getTopSniAttrEgressRate();
        // 如果一个没设置成功是否就不做更新了 是否太草率？
        // 更改为分别进行判断
        // 更改报错,因为enable设置失败的话，另外两个参数也不会设置成功，故enable的if判断包括到后两个参数
        StringBuilder sBuilder = new StringBuilder();
        if (newIngressRate == null && newEgressRate == null) {
            throw new SetValueConflictException("Business.connection");
        }
        if (!newIngressRate.equals(ingressRate)) {
            throw new SetValueConflictException("Business.setIngressRate");
        }
        if (!newEgressRate.equals(egressRate)) {
            throw new SetValueConflictException("Business.setEngressRate");
        }
        oltSniAttribute.setSniId(sniId);
        oltSniAttribute.setEntityId(entityId);
        oltSniDao.updateSniFlowControl(oltSniAttribute);
        if (sBuilder.length() > 0) {
            throw new SetValueConflictException(sBuilder.toString());
        }
    }

    @Override
    public void setSniCtrlFlowEnable(Long entityId, Long sniId, Integer ctrlFlowEnable) {
        Long sniIndex = oltSniDao.getSniIndex(sniId);
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        OltSniAttribute oltSniAttribute = getOltSniFacade(snmpParam.getIpAddress()).setSniCtrlFlowEnable(snmpParam,
                sniIndex, ctrlFlowEnable);
        Integer newCtrlEnable = oltSniAttribute.getTopSniAttrFlowCtrlEnable();
        // 更改为分别进行判断
        StringBuilder sBuilder = new StringBuilder();
        if (newCtrlEnable == null) {
            throw new SetValueConflictException("Business.connection");
        }
        if (!newCtrlEnable.equals(ctrlFlowEnable)) {
            throw new SetValueConflictException("Business.setCtrlEnable");
        }
        oltSniAttribute.setEntityId(entityId);
        oltSniAttribute.setSniId(sniId);
        oltSniAttribute.setTopSniAttrFlowCtrlEnable(ctrlFlowEnable);
        oltSniDao.updateSniFlowControl(oltSniAttribute);
        if (sBuilder.length() > 0) {
            throw new SetValueConflictException(sBuilder.toString());
        }
    }

    @Override
    public int setSniAutoNegotiationMode(Long entityId, Long sniId, Integer modeValue) {
        Long sniIndex = oltSniDao.getSniIndex(sniId);
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        Integer newMode = getOltSniFacade(snmpParam.getIpAddress()).setSniAutoNegotiationMode(snmpParam, sniIndex,
                modeValue);
        if (newMode == null) {
            throw new SetValueConflictException("Business.connection");
        } else {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                logger.error("", e);
            }
            oltSniDao.updateSniAutoNegotiationMode(sniId, modeValue);
            return modeValue;
        }
    }

    @Override
    public List<OltSniMacAddress> getSniMacAddress(Long sniId) {
        return oltSniDao.getSniMacAddress(sniId);
    }

    @Override
    public void modifySniMacAddressAgingTime(Long entityId, Integer sniAddressAgingTime, Integer topSysArpAgingTime) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        OltAttribute oltAttr = new OltAttribute();
        oltAttr.setSniMacAddrTableAgingTime(sniAddressAgingTime);
        oltAttr.setTopSysArpAgingTime(topSysArpAgingTime);
        Integer newAgingTime = getOltSniFacade(snmpParam.getIpAddress()).modifySniMacAddressAgingTime(snmpParam,
                oltAttr);
        // 更新数据库中数据
        oltSniDao.updateSniMacAddressAgingTime(entityId, newAgingTime, topSysArpAgingTime);
    }

    @Override
    public void modifySniMacAddressMaxLearningNum(Long entityId, Long sniId, Long sniMacAddrMaxLearningNum) {
        Long sniIndex = oltSniDao.getSniIndex(sniId);
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        Long newMaxLearningNum = getOltSniFacade(snmpParam.getIpAddress()).setSniMaxLearnMacNum(snmpParam, sniIndex,
                sniMacAddrMaxLearningNum);
        // 更新数据库中
        oltSniDao.updateSniMacAddrLearnMaxNum(sniId, newMaxLearningNum);
    }

    @Override
    public OltSniStormSuppressionEntry getSniStormSuppressionBySniId(Long sniId) {
        return oltSniDao.getSniStormSuppression(sniId);
    }

    @Override
    public void modifySniStoreSuppression(OltSniStormSuppressionEntry oltSniStormSuppressionEntry) {
        // 获取index
        Long sniIndex = oltSniDao.getSniIndex(oltSniStormSuppressionEntry.getSniId());
        oltSniStormSuppressionEntry.setSniIndex(sniIndex);
        // 修改设备上SNI口广播风暴参数
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(oltSniStormSuppressionEntry.getEntityId());
        OltSniStormSuppressionEntry newStormSuppression = getOltSniFacade(snmpParam.getIpAddress())
                .modifySniStormSuppression(snmpParam, oltSniStormSuppressionEntry);
        // 更新数据库中数据
        OltSniStormSuppressionEntry sss = oltSniDao.getSniStormSuppression(oltSniStormSuppressionEntry.getSniId());
        if (sss != null) {
            oltSniDao.updateSniStormSuppression(newStormSuppression);
        } else {
            oltSniDao.insertSniStormSuppression(newStormSuppression);
        }
    }

    @Override
    @Deprecated
    public void redirectSni(Long entityId, Long sniId, Integer direction, String name, Integer dstPort) {
    }

    @Override
    public List<OltSniRedirect> getSniRedirect(Long entityId) {
        return oltSniDao.getSniRedirect(entityId);
    }

    @Override
    public List<OltSniRedirect> getAvailableSniRedirect(Long entityId) {
        return oltSniDao.getAvailableSniRedirect(entityId);
    }

    @Override
    public List<OltSniRedirect> getAllSniRedirect(Long entityId) {
        return oltSniDao.queryAllSniRedirect(entityId);
    }

    @Override
    public void addSniRedirect(OltSniRedirect oltSniRedirect) {
        Long sniSrcIndex = oltSniDao.getSniIndex(oltSniRedirect.getTopSniRedirectGroupSrcPortId());
        oltSniRedirect.setSrcIndex(sniSrcIndex);
        Long sniDstIndex = oltSniDao.getSniIndex(oltSniRedirect.getTopSniRedirectGroupDstPortId());
        oltSniRedirect.setDstIndex(sniDstIndex);
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(oltSniRedirect.getEntityId());
        getOltSniFacade(snmpParam.getIpAddress()).addRedirectSni(snmpParam, oltSniRedirect);
        // 添加数据库中数据
        oltSniDao.addSniRedirect(oltSniRedirect);
    }

    @Override
    public void deleteSniRedirect(Long entityId, Long sniId) {
        // 删除设备上数据
        Long sniIndex = oltSniDao.getSniIndex(sniId);
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        OltSniRedirect oltSniReidrect = new OltSniRedirect();
        oltSniReidrect.setSrcIndex(sniIndex);
        getOltSniFacade(snmpParam.getIpAddress()).deleteRedirectSni(snmpParam, oltSniReidrect);

        // 删除数据库中数据
        oltSniDao.deleteSniRedirect(sniId);
    }

    @Override
    public void setSni15MinPerfStatus(Long entityId, Long sniId, Integer perfStatus) {
        Long sniIndex = oltSniDao.getSniIndex(sniId);
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        Integer newStatus = getOltSniFacade(snmpParam.getIpAddress()).setSni15MinPerfStatus(snmpParam, sniIndex,
                perfStatus);
        if (newStatus == null) {
            throw new SetValueConflictException("Business.connection");
        } else {
            oltSniDao.updateSni15MinPerfStatus(sniId, newStatus);
            /*
             * Long index = oltDao.getSniIndex(sniId); modifyOltCollectors(entityId, index,
             * perfStatus, 1);
             */
            if (!newStatus.equals(perfStatus)) {
                throw new SetValueConflictException("Business.setSni15MinPerfStatus");
            }
        }
    }

    @Override
    public void updateEntitySni15MinPerfStatus(Long entityId, List<OltSniAttribute> sniAttributes) {
        for (OltSniAttribute sniPort : sniAttributes) {
            setSni15MinPerfStatus(entityId, sniPort.getSniId(), 1);
        }
    }

    @Override
    public void setSni24HourPerfStatus(Long entityId, Long sniId, Integer perfStatus) {
        Long sniIndex = oltSniDao.getSniIndex(sniId);
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        Integer newStatus = getOltSniFacade(snmpParam.getIpAddress()).setSni24HourPerfStatus(snmpParam, sniIndex,
                perfStatus);
        /*
         * Long index = oltDao.getSniIndex(sniId); modifyOltCollectors(entityId, index, perfStatus,
         * 2);
         */
        if (newStatus == null) {
            throw new SetValueConflictException("Business.connection");
        } else {
            oltSniDao.updateSni24HourPerfStatus(sniId, newStatus);
        }
    }

    @Override
    public void modifySniMacAddress(Long entityId, OltSniMacAddress sniMacAddress) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        getOltSniFacade(snmpParam.getIpAddress()).modifySniMacAddress(snmpParam, sniMacAddress);
        if (RowStatus.CREATE_AND_GO.equals(sniMacAddress.getSniMacAddrRowStatus())) {
            oltSniDao.insertSniMacAddress(entityId, sniMacAddress);
        } else if (sniMacAddress.getSniMacAddrRowStatus().equals(RowStatus.DESTORY)) {
            oltSniDao.deleteSniMacAddress(entityId, sniMacAddress);
        }
    }

    @Override
    public Long getSniIdByIndex(Long sniIndex, Long entityId) {
        return oltSniDao.getSniIdByIndex(sniIndex, entityId);
    }

    @Override
    public Long getSniIndex(Long sniId) {
        return oltSniDao.getSniIndex(sniId);
    }

    @Override
    public Boolean portIsXGUxFiber(Long entityId, Long sniIndex) {
        OltSniAttribute s = oltSniDao.getSniAttribute(entityId, sniIndex);
        if (s != null) {
            Integer sType = s.getTopSniAttrPortType();
            List<OltSlotAttribute> slotList = oltSlotDao.getOltSlotList(entityId);
            for (OltSlotAttribute slot : slotList) {
                if (EponIndex.getSlotNo(slot.getSlotIndex()).equals(s.getSlotNo())) {
                    return EponConstants.EPON_SNI_FIBER_TYPE.contains(sType)
                            && EponConstants.EPON_SLOT_XGUx_TYPE.contains(slot.getTopSysBdPreConfigType());
                }
            }
        }
        return false;
    }

    @Override
    public List<OltSniAttribute> getSniPortList(Map<String, Object> map) {
        return oltSniDao.getSniPortList(map);
    }

    private OltSniFacade getOltSniFacade(String ip) {
        return facadeFactory.getFacade(ip, OltSniFacade.class);
    }

    @Override
    public void updateSniPort15MinStatus(Long entityId) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        List<OltSniAttribute> sniAttributes = getOltSniFacade(snmpParam.getIpAddress()).getSniListAttribute(snmpParam);
        for (OltSniAttribute sniAttr : sniAttributes) {
            sniAttr.setEntityId(entityId);
        }
        oltSniDao.batchUpdateSni15MinStatus(sniAttributes);
    }

    @Override
    public void refreshSniBaseInfo(Long entityId, Long sniId) {
        Long sniIndex = oltSniDao.getSniIndex(sniId);
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        OltSniAttribute sniAttribute = getOltSniFacade(snmpParam.getIpAddress()).getOltSniAttributeBySniIndex(
                snmpParam, sniIndex);
        sniAttribute.setEntityId(entityId);
        oltSniDao.updateSniPortName(sniId, sniAttribute.getSniPortName());
        oltSniDao.updateSniAutoNegotiationMode(sniId, sniAttribute.getSniAutoNegotiationMode());
    }

    @Override
    public void updateSni15PerfStatus(Long entityId, Long sniIndex, Integer status) {
        Long $sniId = oltSniDao.getSniIdByIndex(sniIndex, entityId);
        oltSniDao.updateSni15MinPerfStatus($sniId, status);
    }

    @Override
    public void updateEntityStates(SynchronizedEvent event) {
    }

    @Override
    public String getSniDisplayName(Long entityId, Long sniIndex) {
        return oltSniDao.querySniDisplayName(entityId, sniIndex);
    }

    @Override
    public OltSniAttribute updateSniPortStatus(Long entityId, Long sniId) {
        Long sniIndex = oltSniDao.getSniIndex(sniId);
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        OltSniAttribute sniAttribute = getOltSniFacade(snmpParam.getIpAddress()).getOltSniAttributeBySniIndex(
                snmpParam, sniIndex);
        oltSniDao.updateSniOperationStatus(sniId, sniAttribute.getSniOperationStatus());
        oltSniDao.updateSniAdminStatus(sniId, sniAttribute.getSniAdminStatus());
        return sniAttribute;
    }

    @Override
    public void refreshSniFlowControl(Long entityId, Long sniId) {
        Long sniIndex = oltSniDao.getSniIndex(sniId);
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        OltSniAttribute sniAttribute = getOltSniFacade(snmpParam.getIpAddress()).getOltSniAttributeBySniIndex(
                snmpParam, sniIndex);
        oltSniDao.updateSniIngressRateAndEgressRate(sniId, sniAttribute.getTopSniAttrIngressRate(),
                sniAttribute.getTopSniAttrEgressRate());
    }

}
