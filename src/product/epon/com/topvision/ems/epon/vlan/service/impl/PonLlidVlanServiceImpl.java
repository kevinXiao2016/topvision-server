/***********************************************************************
 * $Id: PonLlidVlanServiceImpl.java,v1.0 2013-10-25 下午4:38:09 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.vlan.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.epon.exception.PonVlanConfigException;
import com.topvision.ems.epon.olt.dao.OltPonDao;
import com.topvision.ems.epon.olt.service.OltPonService;
import com.topvision.ems.epon.onu.domain.OltOnuAttribute;
import com.topvision.ems.epon.onu.service.OnuService;
import com.topvision.ems.epon.vlan.dao.PonLlidVlanDao;
import com.topvision.ems.epon.vlan.domain.VlanLlidAggregationRule;
import com.topvision.ems.epon.vlan.domain.VlanLlidOnuQinQRule;
import com.topvision.ems.epon.vlan.domain.VlanLlidQinQRule;
import com.topvision.ems.epon.vlan.domain.VlanLlidTranslationRule;
import com.topvision.ems.epon.vlan.domain.VlanLlidTrunkRule;
import com.topvision.ems.epon.vlan.facade.OltVlanFacade;
import com.topvision.ems.epon.vlan.service.PonLlidVlanService;
import com.topvision.ems.epon.vlan.service.PonPortVlanService;
import com.topvision.ems.network.service.EntityService;
import com.topvision.framework.common.MacUtils;
import com.topvision.framework.domain.PhysAddress;
import com.topvision.framework.service.BaseService;
import com.topvision.framework.snmp.SnmpParam;
import com.topvision.framework.utils.EponIndex;
import com.topvision.platform.facade.FacadeFactory;

/**
 * @author flack
 * @created @2013-10-25-下午4:38:09
 *
 */
@Service("ponLlidVlanService")
public class PonLlidVlanServiceImpl extends BaseService implements PonLlidVlanService {
    @Autowired
    private PonLlidVlanDao ponLlidVlanDao;
    @Autowired
    private EntityService entityService;
    @Autowired
    private FacadeFactory facadeFactory;
    @Autowired
    private PonPortVlanService ponPortVlanService;
    @Autowired
    private OltPonDao oltPonDao;
    @Autowired
    private OnuService onuService;
    @Autowired
    private OltPonService oltPonService;

    @Override
    public List<String> getOnuMacAddress(Long ponId) {
        List<String> onuMacStringList = new ArrayList<String>();
        // List<Long> onuMacList = ponLlidVlanDao.getOnuMacAddress(ponId);
        List<OltOnuAttribute> onuMacList = ponLlidVlanDao.getOnuMacAndName(ponId);
        String onuMacAndName = null;
        MacUtils macUtil = null;
        for (OltOnuAttribute onuMac : onuMacList) {
            macUtil = new MacUtils(onuMac.getOnuMacAddress());
            onuMacAndName = macUtil.toString(MacUtils.MAOHAO).toUpperCase() + "[" + onuMac.getOnuName() + "]";
            onuMacStringList.add(onuMacAndName);
        }
        return onuMacStringList;
    }

    @Override
    public List<VlanLlidTranslationRule> getLlidTransList(Long ponId, String mac) {
        return ponLlidVlanDao.getLlidTransList(ponId, mac);
    }

    @Override
    public void addLlidTransRule(VlanLlidTranslationRule vlanLlidTranslationRule) {
        if (vlanLlidTranslationRule != null && vlanLlidTranslationRule.getPortId() != null) {
            // 获取并设置ponIndex
            Long ponIndex = oltPonDao.getPonIndex(vlanLlidTranslationRule.getPortId());
            vlanLlidTranslationRule.setPortIndex(ponIndex);
            // 设备上添加转换规则
            Long entityId = vlanLlidTranslationRule.getEntityId();
            if (entityId != null) {
                SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
                VlanLlidTranslationRule newRule = getOltVlanFacade(snmpParam.getIpAddress()).addLlidTransRule(snmpParam,
                        vlanLlidTranslationRule);
                // Facade端返回数据验证
                if (newRule == null) {
                    // 与OLT连接异常
                    throw new PonVlanConfigException("Business.connection");
                } else {
                    if (newRule.getTopLlidTransVidIdx() != null && newRule.getTopLlidTransNewVid() != null
                            && newRule.getTopLlidTransCosMode() != null && newRule.getTopLlidTransNewCos() != null
                            && newRule.getTopLlidTransNewTpid() != null) {
                        // 数据库中添加转换规则
                        ponLlidVlanDao.addLlidTransRule(newRule);
                        if (!newRule.getTopLlidTransVidIdx().equals(vlanLlidTranslationRule.getTopLlidTransVidIdx())
                                || !newRule.getTopLlidTransNewVid()
                                        .equals(vlanLlidTranslationRule.getTopLlidTransNewVid())) {
                            // 设置后的值与预期值不一致
                            throw new PonVlanConfigException("Business.addLlidTransRuleDisaccord");
                        }
                        if (!newRule.getTopLlidTransCosMode().equals(vlanLlidTranslationRule.getTopLlidTransCosMode())
                                || !newRule.getTopLlidTransNewCos()
                                        .equals(vlanLlidTranslationRule.getTopLlidTransNewCos())) {
                            // 设置后COS与预期值不一致
                            throw new PonVlanConfigException("Business.addLlidTransRuleCosDisaccord");
                        }
                        if (!newRule.getTopLlidTransNewTpid()
                                .equals(vlanLlidTranslationRule.getTopLlidTransNewTpid())) {
                            // 设置后TPID与预期值不一致
                            throw new PonVlanConfigException("Business.addLlidTransRuleTpidDisaccord");
                        }
                    } else {
                        // 设置后的规则存在null值,设置失败.
                        throw new PonVlanConfigException("Business.addLlidTransRuleFailure");
                    }
                }
            }
        }
    }

    @Override
    public void deleteLlidTransRule(Long entityId, Long ponId, String mac, Integer vlanId) {
        // 设备上删除转换规则
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        VlanLlidTranslationRule vlanLlidTranslationRule = new VlanLlidTranslationRule();
        vlanLlidTranslationRule.setEntityId(entityId);
        Long ponIndex = oltPonDao.getPonIndex(ponId);
        vlanLlidTranslationRule.setPortIndex(ponIndex);
        // vlanLlidTranslationRule.setOnuMacLong(new MacUtils(mac).longValue());
        vlanLlidTranslationRule.setOnuMac(new PhysAddress(mac));
        vlanLlidTranslationRule.setTopLlidTransVidIdx(vlanId);
        getOltVlanFacade(snmpParam.getIpAddress()).deleteLlidTransRule(snmpParam, vlanLlidTranslationRule);
        // 数据库中删除转换规则
        ponLlidVlanDao.deleteLlidTransRule(ponId, vlanId, mac);
    }

    @Override
    public List<VlanLlidAggregationRule> getLlidAggrList(Long ponId, String mac) {
        // Long macLong = new MacUtils(mac).longValue();
        return ponLlidVlanDao.getLlidAggrList(ponId, mac);
    }

    @Override
    public void addLlidSvlanAggrRule(VlanLlidAggregationRule vlanLlidAggregationRule) {
        if (vlanLlidAggregationRule != null && vlanLlidAggregationRule.getPortId() != null
                && vlanLlidAggregationRule.getOnuMacString() != null) {
            // 获取并设置ponIndex
            Long ponIndex = oltPonDao.getPonIndex(vlanLlidAggregationRule.getPortId());
            vlanLlidAggregationRule.setPortIndex(ponIndex);
            // 设备上添加转换规则
            Long entityId = vlanLlidAggregationRule.getEntityId();
            if (entityId != null) {
                SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
                VlanLlidAggregationRule newRule = getOltVlanFacade(snmpParam.getIpAddress())
                        .addLlidSvlanAggrRule(snmpParam, vlanLlidAggregationRule);
                // Facade端返回数据验证
                if (newRule == null) {
                    // 与OLT连接异常
                    throw new PonVlanConfigException("Business.connection");
                } else {
                    if (newRule.getLlidVlanAfterAggVid() != null && newRule.getLlidVlanBeforeAggVidList() != null
                            && newRule.getLlidVlanAggCosMode() != null && newRule.getLlidVlanAggNewCos() != null) {
                        // 数据库中添加转换规则
                        ponLlidVlanDao.addLlidSvlanAggrRule(newRule);
                        if (!newRule.getLlidVlanAfterAggVid().equals(vlanLlidAggregationRule.getLlidVlanAfterAggVid())
                                || !newRule.getLlidVlanBeforeAggVidList()
                                        .equals(vlanLlidAggregationRule.getLlidVlanBeforeAggVidList())) {
                            // 设置后的值与预期值不一致
                            throw new PonVlanConfigException("Business.addLlidSvlanAggrRuleDisaccord");
                        }
                        if (!newRule.getLlidVlanAggCosMode().equals(vlanLlidAggregationRule.getLlidVlanAggCosMode())
                                || !newRule.getLlidVlanAggNewCos()
                                        .equals(vlanLlidAggregationRule.getLlidVlanAggNewCos())) {
                            // 设置后COS与预期值不一致
                            throw new PonVlanConfigException("Business.addLlidSvlanAggrRuleCosDisaccord");
                        }
                    } else {
                        // 设置后的规则存在null值,设置失败.
                        throw new PonVlanConfigException("Business.addLlidSvlanAggrRuleFailure");
                    }
                }
            }
        }
    }

    @Override
    public void deleteLlidSvlanAggrRule(Long entityId, Long ponId, String mac, Integer vlanId) {
        // 设备上删除聚合规则
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        VlanLlidAggregationRule vlanLlidAggregationRule = new VlanLlidAggregationRule();
        vlanLlidAggregationRule.setEntityId(entityId);
        Long ponIndex = oltPonDao.getPonIndex(ponId);
        vlanLlidAggregationRule.setPortIndex(ponIndex);
        vlanLlidAggregationRule.setLlidVlanAfterAggVid(vlanId);
        vlanLlidAggregationRule.setOnuMacString(mac);
        getOltVlanFacade(snmpParam.getIpAddress()).deleteLlidSvlanAggrRule(snmpParam, vlanLlidAggregationRule);
        // 数据库中删除聚合规则
        // Long macLong = new MacUtils(mac).longValue();
        ponLlidVlanDao.deleteLlidSvlanAggrRule(ponId, mac, vlanId);
    }

    @Override
    public void modifyLlidCvlanAggrRule(VlanLlidAggregationRule vlanLlidAggregationRule) {
        if (vlanLlidAggregationRule != null && vlanLlidAggregationRule.getPortId() != null
                && vlanLlidAggregationRule.getOnuMac() != null) {
            Long ponId = vlanLlidAggregationRule.getPortId();
            Long portIndex = oltPonDao.getPonIndex(ponId);
            vlanLlidAggregationRule.setPortIndex(portIndex);
            Long entityId = vlanLlidAggregationRule.getEntityId();
            if (entityId != null || vlanLlidAggregationRule.getLlidVlanBeforeAggVidList() != null) {
                SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
                if (vlanLlidAggregationRule.getLlidVlanBeforeAggVidListAfterSwitch() != null
                        && vlanLlidAggregationRule.getLlidVlanBeforeAggVidListAfterSwitch().size() > 0) {
                    VlanLlidAggregationRule newRule = getOltVlanFacade(snmpParam.getIpAddress())
                            .modifyLlidCvlanAggrRule(snmpParam, vlanLlidAggregationRule);
                    // Facade端返回数据验证
                    if (newRule == null) {
                        // 与OLT连接异常
                        throw new PonVlanConfigException("Business.connection");
                    } else if (newRule.getLlidVlanAfterAggVid() != null && newRule.getLlidVlanBeforeAggVidList() != null
                            && newRule.getLlidVlanAggCosMode() != null) {
                        ponLlidVlanDao.modifyLlidCvlanAggrRule(newRule);
                    }
                } else if (vlanLlidAggregationRule.getLlidVlanAfterAggVid() != null) {
                    Integer afterVid = vlanLlidAggregationRule.getLlidVlanAfterAggVid();
                    // Long macLong = vlanLlidAggregationRule.getOnuMacLong();
                    getOltVlanFacade(snmpParam.getIpAddress()).deleteLlidSvlanAggrRule(snmpParam,
                            vlanLlidAggregationRule);
                    ponLlidVlanDao.deleteLlidSvlanAggrRule(ponId, vlanLlidAggregationRule.getOnuMacString(), afterVid);
                }
            }
        }
    }

    @Override
    public VlanLlidTrunkRule getLlidTrunkList(Long ponId, String mac) {
        return ponLlidVlanDao.getLlidTrunkList(ponId, mac);
    }

    @Override
    public void modifyLlidTrunkRule(VlanLlidTrunkRule vlanLlidTrunkRule) {
        if (vlanLlidTrunkRule != null && vlanLlidTrunkRule.getPortId() != null
                && vlanLlidTrunkRule.getEntityId() != null && vlanLlidTrunkRule.getOnuMac() != null) {
            // 获取并设置ponIndex
            Long ponIndex = oltPonDao.getPonIndex(vlanLlidTrunkRule.getPortId());
            vlanLlidTrunkRule.setPortIndex(ponIndex);
            Long entityId = vlanLlidTrunkRule.getEntityId();
            SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
            // 去数据库获取该PON口的trunk列表
            VlanLlidTrunkRule sqlVlanLlidTrunkRule = ponLlidVlanDao.getLlidTrunkList(vlanLlidTrunkRule.getPortId(),
                    vlanLlidTrunkRule.getOnuMac().toString());
            if (sqlVlanLlidTrunkRule != null) {
                if (sqlVlanLlidTrunkRule.getLlidVlanTrunkVidBmp() != null
                        && sqlVlanLlidTrunkRule.getLlidVlanTrunkVidBmpAfterSwitch() != null) {
                    if (vlanLlidTrunkRule.getLlidVlanTrunkVidBmp() != null) {
                        if (vlanLlidTrunkRule.getLlidVlanTrunkVidBmpAfterSwitch() == null
                                || vlanLlidTrunkRule.getLlidVlanTrunkVidBmpAfterSwitch().size() == 0) {
                            // delete该trunk规则
                            getOltVlanFacade(snmpParam.getIpAddress()).deleteLlidTrunkRule(snmpParam,
                                    vlanLlidTrunkRule);
                            // 数据库中删除转换规则
                            ponLlidVlanDao.deleteLlidTrunkRule(vlanLlidTrunkRule.getPortId(),
                                    vlanLlidTrunkRule.getOnuMacString());
                        } else {
                            // 修改TrunkVidList
                            VlanLlidTrunkRule newRule = getOltVlanFacade(snmpParam.getIpAddress())
                                    .modifyLlidTrunkRule(snmpParam, vlanLlidTrunkRule);
                            ponLlidVlanDao.modifyLlidTrunkRule(newRule);
                        }
                    } else {
                        // 设置后的值与预期值不一致
                        throw new PonVlanConfigException("Business.modifyLlidTrunkRuleDisaccord");
                    }
                }
            } else {
                // 该PON口之前没有trunk的规则，添加新规则
                VlanLlidTrunkRule newRule = getOltVlanFacade(snmpParam.getIpAddress()).addLlidTrunkRule(snmpParam,
                        vlanLlidTrunkRule);
                ponLlidVlanDao.addLlidTrunkRule(newRule);
            }
        } else {
            // 设置后的规则存在null值,设置失败.
            throw new PonVlanConfigException("Business.modifyLlidTrunkRuleFailure");
        }
    }

    @Override
    public List<VlanLlidQinQRule> getLlidQinQList(Long ponId, String mac) {
        // Long macLong = new MacUtils(mac).longValue();
        return ponLlidVlanDao.getLlidQinQList(ponId, mac);
    }

    @Override
    public void addLlidQinQRule(VlanLlidQinQRule vlanLlidQinQRule) {
        if (vlanLlidQinQRule != null && vlanLlidQinQRule.getPortId() != null && vlanLlidQinQRule.getOnuMac() != null) {
            // 获取并设置ponIndex
            Long ponIndex = oltPonDao.getPonIndex(vlanLlidQinQRule.getPortId());
            vlanLlidQinQRule.setPortIndex(ponIndex);
            // 设备上添加转换规则
            Long entityId = vlanLlidQinQRule.getEntityId();
            if (entityId != null) {
                SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
                VlanLlidQinQRule newRule = getOltVlanFacade(snmpParam.getIpAddress()).addLlidQinQRule(snmpParam,
                        vlanLlidQinQRule);
                // Facade端返回数据验证
                if (newRule == null) {
                    // 与OLT连接异常
                    throw new PonVlanConfigException("Business.connection");
                } else {
                    if (newRule.getTopLqVlanSVlan() != null && newRule.getTopLqVlanStartCVid() != null
                            && newRule.getTopLqVlanEndCVid() != null && newRule.getTopLqVlanCosMode() != null
                            && newRule.getTopLqVlanSCos() != null && newRule.getTopLqVlanOuterTpid() != null) {
                        // 数据库中添加转换规则
                        ponLlidVlanDao.addLlidQinQRule(newRule);
                        if (!newRule.getTopLqVlanSVlan().equals(vlanLlidQinQRule.getTopLqVlanSVlan())
                                || !newRule.getTopLqVlanStartCVid().equals(vlanLlidQinQRule.getTopLqVlanStartCVid())
                                || !newRule.getTopLqVlanEndCVid().equals(vlanLlidQinQRule.getTopLqVlanEndCVid())) {
                            // 设置后的值与预期值不一致
                            throw new PonVlanConfigException("Business.addLlidQinQRuleDisaccord");
                        }
                        if (!newRule.getTopLqVlanCosMode().equals(vlanLlidQinQRule.getTopLqVlanCosMode())
                                || !newRule.getTopLqVlanSCos().equals(vlanLlidQinQRule.getTopLqVlanSCos())) {
                            // 设置后COS与预期值不一致
                            throw new PonVlanConfigException("Business.addLlidQinQRuleCosDisaccord");
                        }
                        if (!newRule.getTopLqVlanOuterTpid().equals(vlanLlidQinQRule.getTopLqVlanOuterTpid())) {
                            // 设置后TPID与预期值不一致
                            throw new PonVlanConfigException("Business.addLlidQinQRuleTpidDisaccord");
                        }
                    } else {
                        // 设置后的规则存在null值,设置失败.
                        throw new PonVlanConfigException("Business.addLlidQinQRuleFailure");
                    }
                }
            }
        }
    }

    @Override
    public void deleteLlidQinQRule(Long entityId, Long ponId, String mac, Integer startVlanId, Integer endVlanId) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        VlanLlidQinQRule vlanLlidQinQRule = new VlanLlidQinQRule();
        vlanLlidQinQRule.setEntityId(entityId);
        Long ponIndex = oltPonDao.getPonIndex(ponId);
        vlanLlidQinQRule.setPortIndex(ponIndex);
        vlanLlidQinQRule.setTopLqVlanStartCVid(startVlanId);
        vlanLlidQinQRule.setTopLqVlanEndCVid(endVlanId);
        vlanLlidQinQRule.setOnuMacString(mac);
        getOltVlanFacade(snmpParam.getIpAddress()).deleteLlidQinQRule(snmpParam, vlanLlidQinQRule);
        ponLlidVlanDao.deleteLlidQinQRule(ponId, mac, startVlanId, endVlanId);
    }

    @Override
    public void deleteLlidOnuQinQRule(Long entityId, Long onuId, Integer startVlanId, Integer endVlanId) {
        OltOnuAttribute onuAttribute = onuService.getOnuAttribute(onuId);
        Long onuIndex = onuAttribute.getOnuIndex();
        Long ponIndex = EponIndex.getPonIndex(onuIndex);
        Long ponId = oltPonService.getPonIdByIndex(entityId, ponIndex);
        String onuMac = onuAttribute.getOnuMac();

        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        VlanLlidOnuQinQRule vlanLlidOnuQinQRule = new VlanLlidOnuQinQRule();
        vlanLlidOnuQinQRule.setEntityId(entityId);
        vlanLlidOnuQinQRule.setPonId(ponId);
        vlanLlidOnuQinQRule.setOnuIndex(onuIndex);
        vlanLlidOnuQinQRule.setPonIndex(ponIndex);
        vlanLlidOnuQinQRule.setTopOnuQinQStartVlanId(startVlanId);
        vlanLlidOnuQinQRule.setTopOnuQinQEndVlanId(endVlanId);
        vlanLlidOnuQinQRule.setMac(onuMac);
        getOltVlanFacade(snmpParam.getIpAddress()).deleteLlidOnuQinQRule(snmpParam, vlanLlidOnuQinQRule);
        ponLlidVlanDao.deleteLlidQinQRule(ponId, onuMac, startVlanId, endVlanId);
    }

    @Override
    public void refreshVlanLlidListFromOlt(Long entityId, Long ponId) {
        // oltService.refreshOnuInfo(2, ponId);
        ponPortVlanService.refreshVlanDataFromOlt(entityId);
    }

    /**
     * 获取OltControlFacade对象
     * 
     * @param ip
     *            被采集设备IP
     * @return OltControlFacade
     */
    private OltVlanFacade getOltVlanFacade(String ip) {
        return facadeFactory.getFacade(ip, OltVlanFacade.class);
    }

    @Override
    public void addLlidOnuQinQRule(VlanLlidOnuQinQRule onuQinQRule) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(onuQinQRule.getEntityId());
        VlanLlidOnuQinQRule newRule = getOltVlanFacade(snmpParam.getIpAddress()).addLlidOnuQinQRule(snmpParam,
                onuQinQRule);
        // Facade端返回数据验证
        if (newRule == null) {
            // 与OLT连接异常
            throw new PonVlanConfigException("Business.connection");
        } else {
            if (newRule.getTopOnuQinQSVlanId() != null && newRule.getTopOnuQinQStartVlanId() != null
                    && newRule.getTopOnuQinQEndVlanId() != null) {
                // 数据库中添加转换规则
                ponLlidVlanDao.addLlidOnuQinQRule(newRule);
                if (!newRule.getTopOnuQinQSVlanId().equals(onuQinQRule.getTopOnuQinQSVlanId())
                        || !newRule.getTopOnuQinQStartVlanId().equals(onuQinQRule.getTopOnuQinQStartVlanId())
                        || !newRule.getTopOnuQinQEndVlanId().equals(onuQinQRule.getTopOnuQinQEndVlanId())) {
                    // 设置后的值与预期值不一致
                    throw new PonVlanConfigException("Business.addLlidQinQRuleDisaccord");
                }
                /*
                 * if (!newRule.getTopOnuQinQSTagCosDetermine().equals(onuQinQRule.
                 * getTopOnuQinQSTagCosDetermine()) ||
                 * !newRule.getTopOnuQinQSTagCosNewValue().equals(onuQinQRule.
                 * getTopOnuQinQSTagCosNewValue())) { // 设置后COS与预期值不一致 throw new
                 * PonVlanConfigException("Business.addLlidQinQRuleCosDisaccord"); }
                 */
                /*
                 * if
                 * (!newRule.getTopLqVlanOuterTpid().equals(vlanLlidQinQRule.getTopLqVlanOuterTpid()
                 * )) { // 设置后TPID与预期值不一致 throw new
                 * PonVlanConfigException("Business.addLlidQinQRuleTpidDisaccord"); }
                 */
            } else {
                // 设置后的规则存在null值,设置失败.
                throw new PonVlanConfigException("Business.addLlidQinQRuleFailure");
            }
        }
    }

}
