/***********************************************************************
 * $Id: OnuServiceImpl.java,v1.0 2013-10-25 上午11:15:46 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.onu.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.epon.olt.dao.OltPonDao;
import com.topvision.ems.epon.onu.dao.ElectricityOnuDao;
import com.topvision.ems.epon.onu.dao.OnuDao;
import com.topvision.ems.epon.onu.dao.UniDao;
import com.topvision.ems.epon.onu.domain.OltOnuComAttribute;
import com.topvision.ems.epon.onu.domain.OltOnuComStatics;
import com.topvision.ems.epon.onu.domain.OltOnuComVlanConfig;
import com.topvision.ems.epon.onu.domain.OltOnuMacMgmt;
import com.topvision.ems.epon.onu.domain.OltTopOnuCapability;
import com.topvision.ems.epon.onu.domain.OltUniExtAttribute;
import com.topvision.ems.epon.onu.domain.OltUniStormSuppressionEntry;
import com.topvision.ems.epon.onu.facade.ElecOnuFacade;
import com.topvision.ems.epon.onu.facade.UniFacade;
import com.topvision.ems.epon.onu.service.ElectricityOnuService;
import com.topvision.ems.network.service.EntityService;
import com.topvision.framework.service.BaseService;
import com.topvision.framework.snmp.SnmpParam;
import com.topvision.platform.facade.FacadeFactory;

/**
 * @author flack
 * @created @2013-10-25-上午11:15:46
 *
 */
@Service("electricityOnuService")
public class ElectricityOnuServiceImpl extends BaseService implements ElectricityOnuService {
    @Autowired
    private FacadeFactory facadeFactory;
    @Autowired
    private EntityService entityService;
    @Autowired
    private ElectricityOnuDao electricityOnuDao;
    @Autowired
    private OnuDao onuDao;
    @Autowired
    private UniDao uniDao;
    @Autowired
    private OltPonDao oltPonDao;

    @Override
    public void setUniDSLoopBackEnable(Long entityId, Long uniId, Integer uniDSLoopBackEnable) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        OltUniExtAttribute v = new OltUniExtAttribute();
        v.setUniId(uniId);
        v.setEntityId(entityId);
        v.setUniDSLoopBackEnable(uniDSLoopBackEnable);
        Long uniIndex = uniDao.getUniIndex(uniId);
        v.setUniIndex(uniIndex);
        getUniFacade(snmpParam.getIpAddress()).setUniDSLoopBackEnable(snmpParam, v);
        uniDao.updateUniUniDSLoopBackEnable(v);
    }

    @Override
    public void setUniUSUtgPri(Long entityId, Long uniId, Integer uniUSUtgPri) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        OltUniExtAttribute v = new OltUniExtAttribute();
        v.setEntityId(entityId);
        v.setUniId(uniId);
        v.setUniUSUtgPri(uniUSUtgPri);
        v.setUniIndex(uniDao.getUniIndex(uniId));
        getUniFacade(snmpParam.getIpAddress()).setUniUSUtgPri(snmpParam, v);
        uniDao.updateUniUSUtgPri(v);
    }

    @Override
    public void modifyOnuStormOutPacketRate(Long entityId, Long uniId, Long unicastStormOutPacketRate,
            Long multicastStormOutPacketRate, Long broadcastStormOutPacketRate) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        OltUniStormSuppressionEntry v = uniDao.getUniStormSuppression(uniId);
        if (v == null) {
            v = new OltUniStormSuppressionEntry();
            v.setEntityId(entityId);
        }
        Long uniIndex = uniDao.getUniIndex(uniId);
        v.setUniIndex(uniIndex);
        v.setUnicastStormOutPacketRate(unicastStormOutPacketRate);
        v.setMulticastStormOutPacketRate(multicastStormOutPacketRate);
        v.setBroadcastStormOutPacketRate(broadcastStormOutPacketRate);
        getUniFacade(snmpParam.getIpAddress()).modifyUniStormInfo(snmpParam, v);
        uniDao.updateUniStormSuppression(v);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.epon.service.OltService#refreshUniUSUtgPri(java.lang .Long, java.lang
     * .Long)
     */
    @Override
    public void refreshUniUSUtgPri(Long entityId, Long uniId) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        OltUniExtAttribute v = new OltUniExtAttribute();
        v.setUniIndex(uniDao.getUniIndex(uniId));
        OltUniExtAttribute o = getUniFacade(snmpParam.getIpAddress()).refreshUniUSUtgPri(snmpParam, v);
        o.setUniIndex(v.getUniIndex());
        o.setEntityId(entityId);
        o.setUniId(uniId);
        uniDao.updateOltUniExtAttribute(o);
    }

    @Override
    public void modifyPonBandMax(Long ponId, Integer bandMax) {
        // TODO 假数据,仅在数据库
        oltPonDao.updatePonBandMax(ponId, bandMax);
    }

    @Override
    public void configUniLoopDetectEnable(Long entityId, Long uniId, int topUniLoopDetectEnable) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        OltUniExtAttribute v = new OltUniExtAttribute();
        v.setUniId(uniId);
        v.setEntityId(entityId);
        v.setTopUniLoopDetectEnable(topUniLoopDetectEnable);
        Long uniIndex = uniDao.getUniIndex(uniId);
        v.setUniIndex(uniIndex);
        getUniFacade(snmpParam.getIpAddress()).setUniLoopDetectEnable(snmpParam, v);
        uniDao.updateUniLoopDetectEnable(v);
    }

    private UniFacade getUniFacade(String ip) {
        return facadeFactory.getFacade(ip, UniFacade.class);
    }

    /**
     * 获取ElecOnuFacade对象
     * 
     * @param ip
     *            被采集设备IP
     * @return ElecOnuFacade
     */
    private ElecOnuFacade getElecOnuFacade(String ip) {
        return facadeFactory.getFacade(ip, ElecOnuFacade.class);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.epon.service.OltElecOnuService#setOnuComVlan(com.topvision.framework.snmp
     * .SnmpParam, java.lang.Integer)
     */
    @Override
    public void setOnuComVlan(Long entityId, Integer onuComVlan) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        // 设置到设备
        getElecOnuFacade(snmpParam.getIpAddress()).setOnuComVlan(snmpParam, onuComVlan);
        // 更新到数据库
        electricityOnuDao.updateOnuComVlan(entityId, onuComVlan);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.epon.service.OltElecOnuService#setOnuIpMaskInfo(com.topvision.framework
     * .snmp.SnmpParam, java.lang.Long, java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    public void setOnuIpMaskInfo(Long entityId, Long onuIndex, String onuIp, String onuMask, String onuGateway) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        // 设置到设备
        getElecOnuFacade(snmpParam.getIpAddress()).setOnuIpMaskInfo(snmpParam, onuIndex, onuIp, onuMask, onuGateway);
        // 更新到数据库
        electricityOnuDao.updateOnuIpMaskInfo(entityId, onuIndex, onuIp, onuMask, onuGateway);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.epon.service.OltElecOnuService#setOnuComAttribute(com.topvision.framework
     * .snmp.SnmpParam, com.topvision.ems.epon.facade.domain.OltOnuComAttribute)
     */
    @Override
    public void setOnuComAttribute(Long entityId, OltOnuComAttribute attribute) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        getElecOnuFacade(snmpParam.getIpAddress()).setOnuComAttribute(snmpParam, attribute);
        electricityOnuDao.updateOnuComAttribute(entityId, attribute);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.epon.service.OltElecOnuService#getOnuComStatisc(com.topvision.framework
     * .snmp.SnmpParam, java.lang.Long)
     */
    @Override
    public OltOnuComStatics getOnuComStatisc(Long entityId, Long onuComIndex) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        return getElecOnuFacade(snmpParam.getIpAddress()).getOnuComStatisc(snmpParam, onuComIndex);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.epon.service.OltElecOnuService#cleanOnuComStatisc(com.topvision.framework
     * .snmp.SnmpParam, java.lang.Long)
     */
    @Override
    public void cleanOnuComStatisc(Long entityId, Long onuComIndex) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        getElecOnuFacade(snmpParam.getIpAddress()).cleanOnuComStatisc(snmpParam, onuComIndex);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.epon.service.OltElecOnuService#switchPonInfo(com.topvision.framework.snmp
     * .SnmpParam, java.lang.Long, java.lang.Long, java.lang.Integer)
     */
    @Override
    public void switchPonInfo(Long entityId, Long srcPonIndex, Long dstPonIndex) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        // TODO 静态变量
        getElecOnuFacade(snmpParam.getIpAddress()).switchPonInfo(snmpParam, srcPonIndex, dstPonIndex, 1);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.epon.service.OltElecOnuService#getOnuComVlan(com.topvision.framework.snmp
     * .SnmpParam)
     */
    @Override
    public OltOnuComVlanConfig getOnuComVlan(Long entityId) {
        return electricityOnuDao.getComVlanConfig(entityId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.epon.service.OltElecOnuService#getOnuComAttribute(com.topvision.framework
     * .snmp.SnmpParam, java.lang.Long)
     */
    @Override
    public OltOnuComAttribute getOnuComAttribute(Long entityId, Long onuComIndex) {
        return electricityOnuDao.getComVlamAttribute(entityId, onuComIndex);
    }

    /**
     * 拓扑COM VLAN数据
     * 
     * @param entityId
     */
    @SuppressWarnings("unused")
    private void refreshComVlan(Long entityId) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        OltOnuComVlanConfig config = getElecOnuFacade(snmpParam.getIpAddress()).getOnuComVlan(snmpParam);
        electricityOnuDao.batchInsertOnuComVlan(entityId, config.getOnuComVlan());
    }

    /**
     * 拓扑COM ATT数据
     * 
     * @param entityId
     */
    @SuppressWarnings("unused")
    private void refreshComAttribute(Long entityId) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        List<OltOnuComAttribute> attributes = getElecOnuFacade(snmpParam.getIpAddress()).getOltOnuComAttributes(
                snmpParam);
        for (OltOnuComAttribute comAttribute : attributes) {
            comAttribute.setEntityId(entityId);
            if (!comAttribute.getOnuComInfoSrvType().equals(1) && !comAttribute.getOnuComInfoSrvType().equals(3)) {
                comAttribute.setOnuComInfoSrvPort(0);
            }
            if (comAttribute.getOnuComInfoMainRemoteIp().equalsIgnoreCase("0.0.0.0")
                    || comAttribute.getOnuComInfoBackRemoteIp().equalsIgnoreCase("")) {
                comAttribute.setOnuComInfoBackRemotePort(0);
            }
            if (comAttribute.getOnuComInfoMainRemoteIp().equalsIgnoreCase("0.0.0.0")
                    || comAttribute.getOnuComInfoMainRemoteIp().equalsIgnoreCase("")) {
                comAttribute.setOnuComInfoBackRemotePort(0);
            }
        }
        electricityOnuDao.batchInsertOnuComAttribute(entityId, attributes);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.epon.service.OltElecOnuService#refreshOnuComVlan(java.lang.Long)
     */
    @Override
    public void refreshOnuComVlan(Long entityId) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        OltOnuComVlanConfig config = getElecOnuFacade(snmpParam.getIpAddress()).getOnuComVlan(snmpParam);
        electricityOnuDao.updateOnuComVlan(entityId, config.getOnuComVlan());
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.epon.service.OltElecOnuService#refreshOnucomaAttribute(java.lang.Long,
     * java.lang.Long)
     */
    @Override
    public void refreshOnuComAttribute(Long entityId, Long onuComIndex) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        OltOnuComAttribute attribute = getElecOnuFacade(snmpParam.getIpAddress()).getOnuComAttribute(snmpParam,
                onuComIndex);
        if (!attribute.getOnuComInfoSrvType().equals(1) && !attribute.getOnuComInfoSrvType().equals(3)) {
            attribute.setOnuComInfoSrvPort(0);
        }
        if (attribute.getOnuComInfoMainRemoteIp().equalsIgnoreCase("0.0.0.0")
                || attribute.getOnuComInfoBackRemoteIp().equalsIgnoreCase("")) {
            attribute.setOnuComInfoBackRemotePort(0);
        }
        if (attribute.getOnuComInfoMainRemoteIp().equalsIgnoreCase("0.0.0.0")
                || attribute.getOnuComInfoMainRemoteIp().equalsIgnoreCase("")) {
            attribute.setOnuComInfoBackRemotePort(0);
        }
        electricityOnuDao.updateOnuComAttribute(entityId, attribute);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.epon.service.OltElecOnuService#loadPonCutOverPort(java .lang.Long,
     * java.lang.Long)
     */
    @Override
    public List<Long> loadPonCutOverPort(Long entityId, Long ponCutOverPortIndex) {
        return electricityOnuDao.loadPonCutOverPort(entityId, ponCutOverPortIndex);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.epon.service.OltElecOnuService#getOnuMacMgmt(java.lang .Long,
     * java.lang.Long)
     */
    @Override
    public OltOnuMacMgmt getOnuMacMgmt(Long entityId, Long onuIndex) {
        return electricityOnuDao.getOnuMacMgmt(entityId, onuIndex);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.epon.service.OltElecOnuService#refreshOnuMacMgmt(java .lang.Long,
     * java.lang.Long)
     */
    @Override
    public void refreshOnuMacMgmt(Long entityId, Long onuIndex) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        OltOnuMacMgmt n = new OltOnuMacMgmt();
        n.setOnuIndex(onuIndex);
        try {
            n = getElecOnuFacade(snmpParam.getIpAddress()).getOnuMacMgmt(snmpParam, onuIndex);
        } catch (Exception e) {
            getElecOnuFacade(snmpParam.getIpAddress()).addOnuMacMgmt(snmpParam, n);
            n.setTopOnuMacList("");
        }
        OltOnuMacMgmt o = electricityOnuDao.getOnuMacMgmt(entityId, onuIndex);
        n.setOnuId(onuDao.getOnuIdByIndex(entityId, onuIndex));
        n.setEntityId(entityId);
        if (o != null) {
            // TODO static
            if (n.getTopOnuMacList().equalsIgnoreCase("")) {
                n.setTopOnuMacList(o.getTopOnuMacList());
                n.setTopOnuMacMark(o.getTopOnuMacMark());
                n.setMgmtEnable(2);
            } else {
                n.setMgmtEnable(1);
            }
            electricityOnuDao.updateOnuMacMgmt(n);
        } else {
            // TODO static
            if (n.getTopOnuMacList().equalsIgnoreCase("")) {
                n.setMgmtEnable(2);
            } else {
                n.setMgmtEnable(1);
            }
            electricityOnuDao.insertOnuMacMgmt(n);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.epon.service.OltElecOnuService#setOnuMacMgmt(com.topvision
     * .ems.epon.facade.domain.OltOnuMacMgmt)
     */
    @Override
    public void setOnuMacMgmt(OltOnuMacMgmt oltOnuMacMgmt) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(oltOnuMacMgmt.getEntityId());
        OltOnuMacMgmt o = new OltOnuMacMgmt();
        o.setOnuMibIndex(oltOnuMacMgmt.getOnuMibIndex());
        if (oltOnuMacMgmt.getMgmtEnable().equals(2)) {
            o.setTopOnuMacList("");
        } else {
            o.setTopOnuMacList(oltOnuMacMgmt.getTopOnuMacList());
            o.setTopOnuMacMark(oltOnuMacMgmt.getTopOnuMacMark());
        }
        try {
            getElecOnuFacade(snmpParam.getIpAddress()).setOnuMacMgmt(snmpParam, o);
        } catch (Exception e) {
            getElecOnuFacade(snmpParam.getIpAddress()).addOnuMacMgmt(snmpParam, o);
        }
        oltOnuMacMgmt.setOnuId(onuDao.getOnuIdByIndex(oltOnuMacMgmt.getEntityId(), oltOnuMacMgmt.getOnuIndex()));
        OltOnuMacMgmt old = electricityOnuDao.getOnuMacMgmt(oltOnuMacMgmt.getEntityId(), oltOnuMacMgmt.getOnuIndex());
        if (old != null) {
            electricityOnuDao.updateOnuMacMgmt(oltOnuMacMgmt);
        } else {
            electricityOnuDao.insertOnuMacMgmt(oltOnuMacMgmt);
        }
    }

    @Override
    public OltTopOnuCapability getElecOnuCapability(Long entityId, Long onuIndex) {
        return electricityOnuDao.getElecOnuCapability(entityId, onuIndex);
    }
}
