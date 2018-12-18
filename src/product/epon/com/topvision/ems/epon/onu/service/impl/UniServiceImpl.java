/***********************************************************************
 * $Id: OnuServiceImpl.java,v1.0 2013-10-25 上午11:15:46 $
 * 
 * @author: flack
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

import com.topvision.ems.epon.exception.SetValueConflictException;
import com.topvision.ems.epon.onu.dao.OnuDao;
import com.topvision.ems.epon.onu.dao.UniDao;
import com.topvision.ems.epon.onu.domain.OltOnuAttribute;
import com.topvision.ems.epon.onu.domain.OltUniAttribute;
import com.topvision.ems.epon.onu.domain.OltUniExtAttribute;
import com.topvision.ems.epon.onu.domain.OltUniPortRateLimit;
import com.topvision.ems.epon.onu.domain.OltUniStormSuppressionEntry;
import com.topvision.ems.epon.onu.facade.UniFacade;
import com.topvision.ems.epon.onu.service.UniService;
import com.topvision.ems.epon.topology.event.OnuSynchronizedEvent;
import com.topvision.ems.epon.topology.event.OnuSynchronizedListener;
import com.topvision.ems.gpon.onu.facade.GponUniFacade;
import com.topvision.ems.gpon.onuauth.GponConstant;
import com.topvision.ems.network.service.EntityService;
import com.topvision.framework.service.BaseService;
import com.topvision.framework.snmp.SnmpParam;
import com.topvision.framework.utils.EponConstants;
import com.topvision.framework.utils.EponIndex;
import com.topvision.platform.facade.FacadeFactory;
import com.topvision.platform.message.service.MessageService;

/**
 * @author flack
 * @created @2013-10-25-上午11:15:46
 *
 */
@Service("uniService")
public class UniServiceImpl extends BaseService implements UniService, OnuSynchronizedListener {
    @Autowired
    private UniDao uniDao;
    @Autowired
    private EntityService entityService;
    @Autowired
    private FacadeFactory facadeFactory;
    @Autowired
    private MessageService messageService;

    @Autowired
    private OnuDao onuDao;

    public static final Integer ONU_SINGLE_TOPO = 1;

    @Override
    @PreDestroy
    public void destroy() {
        super.destroy();
        messageService.removeListener(OnuSynchronizedListener.class, this);
    }

    @Override
    @PostConstruct
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
                // 单个刷新onu的时候，用gettableline
                OltOnuAttribute onuAttr = onuDao.getOnuAttributeByIndex(entityId, onuIndexList.get(0));
                if (EponConstants.EPON_ONU.equals(onuAttr.getOnuEorG())) {
                    refreshOltUniRateLimit(entityId, onuIndexList.get(0));
                }
            } else if (onuIndexList.size() > ONU_SINGLE_TOPO) {
                // 批量刷新onu时，刷新整个olt
                refreshOltUniRateLimit(event.getEntityId());
            }
            logger.info("refreshOltUniRateLimit finish");
        } catch (Exception e) {
            logger.error("refreshOltUniRateLimit wrong", e);
        }
    }

    @Override
    public void refreshOltUniRateLimit(Long entityId) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        List<OltUniPortRateLimit> oltUniPortRateLimits = getUniFacade(snmpParam.getIpAddress())
                .getOltUniPortRateLimits(snmpParam);
        if (oltUniPortRateLimits != null) {
            for (int i = 0; i < oltUniPortRateLimits.size(); i++) {
                oltUniPortRateLimits.get(i).setEntityId(entityId);
            }
            uniDao.batchInsertOltUniRateLimit(oltUniPortRateLimits);
        }
    }

    @Override
    public void refreshOltUniExtAttribute(Long entityId) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        List<OltUniExtAttribute> oltUniExtAttributes = getUniFacade(snmpParam.getIpAddress()).getUniListAttribute(
                snmpParam);
        if (oltUniExtAttributes != null) {
            for (int i = 0; i < oltUniExtAttributes.size(); i++) {
                oltUniExtAttributes.get(i).setEntityId(entityId);
            }
            uniDao.batchInsertOltUniExtAttribute(oltUniExtAttributes);
        }
    }

    @Override
    public OltUniStormSuppressionEntry getUniStormSuppressionByUniId(Long uniId) {
        return uniDao.getUniStormSuppression(uniId);
    }

    @Override
    public void modifyUniStormInfo(OltUniStormSuppressionEntry oltUniStormSuppressionEntry) {
        // 获取index
        Long uniIndex = uniDao.getUniIndex(oltUniStormSuppressionEntry.getUniId());
        oltUniStormSuppressionEntry.setUniIndex(uniIndex);
        // 修改设备上UNI口广播风暴参数
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(oltUniStormSuppressionEntry.getEntityId());
        OltUniStormSuppressionEntry newStormSuppression = getUniFacade(snmpParam.getIpAddress()).modifyUniStormInfo(
                snmpParam, oltUniStormSuppressionEntry);
        // 更新数据库中数据
        uniDao.updateUniStormSuppression(newStormSuppression);
    }

    @Override
    public void setUniAutoNegotiationStatus(Long entityId, Long uniId, Integer uniAutoNegotiationStatus) {
        Long uniIndex = uniDao.getUniIndex(uniId);
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        Integer newStatus = getUniFacade(snmpParam.getIpAddress()).setUniAutoNegotiationStatus(snmpParam, uniIndex,
                uniAutoNegotiationStatus);
        if (newStatus == null) {
            throw new SetValueConflictException("Business.connection");
        } else {
            uniDao.updateUniAutoNegotiationStatus(uniId, newStatus);
            if (!newStatus.equals(uniAutoNegotiationStatus)) {
                throw new SetValueConflictException("Business.setUniAutoNegotiationStatus");
            }
        }
    }

    @Override
    public void setUniAdminStatus(Long entityId, Long uniId, Integer uniAdminStatus) {
        Long uniIndex = uniDao.getUniIndex(uniId);
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        String onuType = uniDao.getOnuEorGByUniId(uniId);

        if (EponConstants.EPON_ONU.equals(onuType)) {
            OltUniAttribute oltUniAttribute = new OltUniAttribute();
            oltUniAttribute.setUniIndex(uniIndex);
            oltUniAttribute.setUniAdminStatus(uniAdminStatus);
            if (EponConstants.PORT_STATUS_DOWN.equals(uniAdminStatus)) {
                oltUniAttribute.setUniOperationStatus(EponConstants.PORT_STATUS_DOWN);
            }
            getUniFacade(snmpParam.getIpAddress()).setUniAdminStatus(snmpParam, oltUniAttribute);
        } else if (GponConstant.GPON_ONU.equals(onuType)) {
            getGponUniFacade(snmpParam.getIpAddress()).setUniAdminStatus(snmpParam, uniIndex, uniAdminStatus);
        }
        uniDao.updateUniAdminStatus(uniId, uniAdminStatus);
    }

    @Override
    public void setUniIsolationEnable(Long entityId, Long uniId, Integer uniIsolationEnable) {
        Long uniIndex = uniDao.getUniIndex(uniId);
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        Integer newStatus = getUniFacade(snmpParam.getIpAddress()).setUniIsolationEnable(snmpParam, uniIndex,
                uniIsolationEnable);
        if (newStatus == null) {
            throw new SetValueConflictException("Business.connection");
        } else {
            uniDao.updateUniIsolationEnable(uniId, uniIsolationEnable);
            if (!newStatus.equals(uniIsolationEnable)) {
                throw new SetValueConflictException("Business.setUniIsolationEnable");
            }
        }
    }

    @Override
    public void setUniFlowCtrlEnable(Long entityId, Long uniId, Integer uniFlowCtrlEnable) {
        Long uniIndex = uniDao.getUniIndex(uniId);
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        Integer newStatus = getUniFacade(snmpParam.getIpAddress()).setUniFlowCtrlEnable(snmpParam, uniIndex,
                uniFlowCtrlEnable);
        if (newStatus == null) {
            throw new SetValueConflictException("Business.connection");
        } else {
            uniDao.updateUniFlowCtrlEnable(uniId, newStatus);
            if (!newStatus.equals(uniFlowCtrlEnable)) {
                throw new SetValueConflictException("Business.setUniFlowCtrlEnable");
            }
        }
    }

    @Override
    public void setUni15minEnable(Long entityId, Long uniId, Integer uni15minEnable) {
        Long uniIndex = uniDao.getUniIndex(uniId);
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        String onuType = uniDao.getOnuEorGByUniId(uniId);
        if (EponConstants.EPON_ONU.equals(onuType)) {
            getUniFacade(snmpParam.getIpAddress()).setUni15minEnable(snmpParam, uniIndex, uni15minEnable);
        } else if (GponConstant.GPON_ONU.equals(onuType)) {
            getGponUniFacade(snmpParam.getIpAddress()).setUni15minEnable(snmpParam, uniIndex, uni15minEnable);
        }
        uniDao.updateUni15minEnable(uniId, uni15minEnable);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.epon.service.OltService#updateEntityUni15minEnable(java.util.List)
     */
    @Override
    public void updateEntityUni15minEnable(List<OltUniAttribute> uniAttributes) {
        for (OltUniAttribute uniPort : uniAttributes) {
            setUni15minEnable(uniPort.getEntityId(), uniPort.getUniId(), 1);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.epon.service.OltService#setUni24hEnable(java.lang .Long,
     * java.lang.Integer)
     */
    @Override
    public void setUni24hEnable(Long entityId, Long uniId, Integer uni24hEnable) {
        Long uniIndex = uniDao.getUniIndex(uniId);
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        Integer newStatus = getUniFacade(snmpParam.getIpAddress()).setUni24hEnable(snmpParam, uniIndex, uni24hEnable);
        // Long index = oltDao.getUniIndex(uniId);
        // modifyOltCollectors(entityId, index, uni24hEnable, 2);
        if (newStatus == null) {
            throw new SetValueConflictException("Business.connection");
        } else {
            uniDao.updateUni24hEnable(uniId, uni24hEnable);
            if (!newStatus.equals(uni24hEnable)) {
                throw new SetValueConflictException("Business.setUni24hEnable");
            }
        }
    }

    private UniFacade getUniFacade(String ip) {
        return facadeFactory.getFacade(ip, UniFacade.class);
    }

    private GponUniFacade getGponUniFacade(String ip) {
        return facadeFactory.getFacade(ip, GponUniFacade.class);
    }

    @Override
    public OltUniPortRateLimit getUniRateLimitInfo(Long uniId) {
        return uniDao.getUniPortRateLimit(uniId);
    }

    @Override
    public void modifyUniRateLimitInfo(OltUniPortRateLimit oltUniPortRateLimit) {
        Long uniIndex = uniDao.getUniIndex(oltUniPortRateLimit.getUniId());
        oltUniPortRateLimit.setUniIndex(uniIndex);
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(oltUniPortRateLimit.getEntityId());
        OltUniPortRateLimit newStatus = getUniFacade(snmpParam.getIpAddress()).modifyUniRateLimitInfo(snmpParam,
                oltUniPortRateLimit);
        if (newStatus == null) {
            throw new SetValueConflictException("Business.connection");
        } else {
            uniDao.updateUniPortRateLimit(oltUniPortRateLimit);
        }
    }

    @Override
    public void refreshUniRateLimit(Long entityId, Long uniId) {
        OltUniPortRateLimit oltUniPortRateLimit = new OltUniPortRateLimit();
        Long uniIndex = uniDao.getUniIndex(uniId);
        oltUniPortRateLimit.setUniId(uniId);
        oltUniPortRateLimit.setEntityId(entityId);
        oltUniPortRateLimit.setUniIndex(uniIndex);
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        OltUniPortRateLimit newStatus = getUniFacade(snmpParam.getIpAddress()).refreshUniRateLimitInfo(snmpParam,
                oltUniPortRateLimit);
        if (newStatus == null) {
            throw new SetValueConflictException("Business.connection");
        } else {
            uniDao.updateUniPortRateLimit(newStatus);
        }
    }

    @Override
    public OltUniExtAttribute getOltUniExtAttribute(Long uniId) {
        OltUniExtAttribute re = uniDao.getOltUniExtAttribute(uniId);
        Long uniIndex = uniDao.getUniIndex(uniId);
        re.setUniIndex(uniIndex);
        return re;
    }

    @Override
    public void updateOltUniAutoNegotiationMode(Long entityId, Long uniId, Integer modeType) {
        Long uniIndex = uniDao.getUniIndex(uniId);
        OltUniExtAttribute oltUniExtAttribute = new OltUniExtAttribute();
        oltUniExtAttribute.setUniIndex(uniIndex);
        oltUniExtAttribute.setUniId(uniId);
        oltUniExtAttribute.setTopUniAttrAutoNegotiationAdvertisedTechAbilityInteger(modeType);
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        OltUniExtAttribute newStatus = getUniFacade(snmpParam.getIpAddress()).updateOltUniAutoNegoMode(snmpParam,
                oltUniExtAttribute);
        if (newStatus == null) {
            throw new SetValueConflictException("Business.connection");
        } else {
            uniDao.updateOltUniAutoNegoMode(oltUniExtAttribute);
        }
    }

    @Override
    public void restartUniAutoNego(Long entityId, Long uniId) {
        Long uniIndex = uniDao.getUniIndex(uniId);
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        getUniFacade(snmpParam.getIpAddress()).restartUniAutoNego(snmpParam, uniIndex);
    }

    @Override
    public void setUniAutoNegoEnable(Long entityId, Long uniId, Integer uniAutoNegoEnable) {
        Long uniIndex = uniDao.getUniIndex(uniId);
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        Integer newStatus = getUniFacade(snmpParam.getIpAddress()).setUniAutoNegoEnable(snmpParam, uniIndex,
                uniAutoNegoEnable);
        if (uniAutoNegoEnable == 1) {
            // 自协商使能后需要将端口工作模式设置为默认
            // modify by lzt
            OltUniExtAttribute oltUniExtAttribute = new OltUniExtAttribute();
            oltUniExtAttribute.setUniId(uniId);
            oltUniExtAttribute.setTopUniAttrAutoNegotiationAdvertisedTechAbilityInteger(0);
            uniDao.updateOltUniAutoNegoMode(oltUniExtAttribute);
        }
        uniDao.updateUniAutoNegoEnable(uniId, newStatus);
    }

    @Override
    public Integer getUniAutoNegoStatus(Long entityId, Long uniId) {
        Long uniIndex = uniDao.getUniIndex(uniId);
        // 获取采集参数
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        // 调用采集端程序去设备上获取UNI端口自协商状态
        Integer autoNegoStatus = getUniFacade(snmpParam.getIpAddress()).getUniAutoNegoStatus(snmpParam, uniIndex);
        // 更新到数据库
        uniDao.updateUniAutoNegoStatus(uniId, autoNegoStatus);
        return autoNegoStatus;
    }

    @Override
    public void modifyUniMacAgeTime(OltUniExtAttribute oltUniExtAttribute) {
        Long uniIndex = uniDao.getUniIndex(oltUniExtAttribute.getUniId());
        oltUniExtAttribute.setUniIndex(uniIndex);
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(oltUniExtAttribute.getEntityId());
        OltUniExtAttribute newMacAge = getUniFacade(snmpParam.getIpAddress()).updateOltUniMacAge(snmpParam,
                oltUniExtAttribute);
        if (newMacAge != null && newMacAge.getMacAge().equals(oltUniExtAttribute.getMacAge())) {
            uniDao.modifyUniMacAgeTime(oltUniExtAttribute.getUniId(), oltUniExtAttribute.getMacAge());
        } else {
            throw new SetValueConflictException("Business.modifyUniAgeTime");
        }
    }

    @Override
    public void onuMacClear(Long entityId, Long uniId) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        Long uniIndex = uniDao.getUniIndex(uniId);
        OltUniExtAttribute uniExtAttribute = new OltUniExtAttribute();
        uniExtAttribute.setEntityId(entityId);
        uniExtAttribute.setUniIndex(uniIndex);
        uniExtAttribute.setMacAddrClearByPort(1);
        getUniFacade(snmpParam.getIpAddress()).setUniMacClear(snmpParam, uniExtAttribute);
    }

    @Override
    public Long getUniAttrMacAddrLearnMaxNum(Long uniId) {
        return uniDao.getUniAttrMacAddrLearnMaxNum(uniId);
    }

    @Override
    public void modifyUniMacAddrLearnMaxNum(Long entityId, Long uniId, Long topUniAttrMacAddrLearnMaxNum) {
        Long uniIndex = uniDao.getUniIndex(uniId);
        OltUniExtAttribute oltTopUniAttribute = new OltUniExtAttribute();
        oltTopUniAttribute.setUniIndex(uniIndex);
        oltTopUniAttribute.setMacAddrLearnMaxNum(topUniAttrMacAddrLearnMaxNum);
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        OltUniExtAttribute newMac = getUniFacade(snmpParam.getIpAddress()).modifyUniMacAddrLearnMaxNum(snmpParam,
                oltTopUniAttribute);
        if (newMac != null && newMac.getMacAddrLearnMaxNum().equals(oltTopUniAttribute.getMacAddrLearnMaxNum())) {
            uniDao.modifyUniMacAddrLearnMaxNum(uniId, topUniAttrMacAddrLearnMaxNum);
        } else {
            throw new SetValueConflictException("Business.modifyUniMacAddrLearnMaxNum");
        }
    }

    @Override
    public OltUniExtAttribute refreshUniUSUtgPri(Long entityId, Long uniId) {
        Long uniIndex = uniDao.getUniIndex(uniId);
        OltUniExtAttribute oltTopUniAttribute = new OltUniExtAttribute();
        oltTopUniAttribute.setUniIndex(uniIndex);
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        OltUniExtAttribute newMac = getUniFacade(snmpParam.getIpAddress()).refreshUniUSUtgPri(snmpParam,
                oltTopUniAttribute);
        if (newMac != null) {
            uniDao.modifyUniMacAddrLearnMaxNum(uniId, newMac.getMacAddrLearnMaxNum());
        }
        return newMac;
    }

    @Override
    public void refreshUniStormOutPacketRate(Long entityId) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        List<OltUniStormSuppressionEntry> o = getUniFacade(snmpParam.getIpAddress()).refreshOnuStormOutPacketRate(
                snmpParam);
        for (OltUniStormSuppressionEntry i : o) {
            i.setEntityId(entityId);
            i.setUniIndex(EponIndex.getUniIndex(i.getUniCardNo(), i.getUniPonNo(), i.getUniOnuNo(), 0, i.getUniPortNo()));
        }
        uniDao.batchInsertOltUniStormInfo(o);
    }

    @Override
    public void refreshOltUniRateLimit(Long entityId, Long onuIndex) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        List<Long> uniIndexList = uniDao.getUniIndexListByEntityIdAndOnuIndex(entityId, onuIndex);
        List<OltUniPortRateLimit> oltUniPortRateLimits = new ArrayList<OltUniPortRateLimit>();
        for (Long uniIndex : uniIndexList) {
            OltUniPortRateLimit oltUniPortRateLimit = new OltUniPortRateLimit();
            oltUniPortRateLimit.setUniIndex(uniIndex);
            oltUniPortRateLimit.setEntityId(entityId);
            oltUniPortRateLimits.add(oltUniPortRateLimit);
        }
        List<OltUniPortRateLimit> portRateLimits = getUniFacade(snmpParam.getIpAddress()).getUniPortRateLimit(
                snmpParam, oltUniPortRateLimits);
        if (portRateLimits != null) {
            uniDao.batchInsertOltUniRateLimit(portRateLimits);
        }
    }

    @Override
    public void updateUni15MinStatus(Long entityId) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        List<OltUniExtAttribute> uniAttrs = getUniFacade(snmpParam.getIpAddress()).getUniListAttribute(snmpParam);
        for (OltUniExtAttribute uniAttr : uniAttrs) {
            uniAttr.setEntityId(entityId);
        }
        uniDao.batchUpdateUni15MinStatus(uniAttrs);
    }

}
