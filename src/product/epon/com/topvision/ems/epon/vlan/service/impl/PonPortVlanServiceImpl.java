/***********************************************************************
 * $Id: PonPortVlanService.java,v1.0 2013-10-25 下午4:37:12 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.vlan.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.devicesupport.version.service.DeviceVersionService;
import com.topvision.ems.epon.domain.PonCvidModeRela;
import com.topvision.ems.epon.domain.PonSvidModeRela;
import com.topvision.ems.epon.exception.PonVlanConfigException;
import com.topvision.ems.epon.olt.dao.OltPonDao;
import com.topvision.ems.epon.onu.service.OnuService;
import com.topvision.ems.epon.vlan.dao.PonPortVlanDao;
import com.topvision.ems.epon.vlan.domain.OltPortVlanEntry;
import com.topvision.ems.epon.vlan.domain.PortVlanAttribute;
import com.topvision.ems.epon.vlan.domain.VlanAggregationRule;
import com.topvision.ems.epon.vlan.domain.VlanLlidAggregationRule;
import com.topvision.ems.epon.vlan.domain.VlanLlidOnuQinQRule;
import com.topvision.ems.epon.vlan.domain.VlanLlidQinQRule;
import com.topvision.ems.epon.vlan.domain.VlanLlidTranslationRule;
import com.topvision.ems.epon.vlan.domain.VlanLlidTrunkRule;
import com.topvision.ems.epon.vlan.domain.VlanQinQRule;
import com.topvision.ems.epon.vlan.domain.VlanTranslationRule;
import com.topvision.ems.epon.vlan.domain.VlanTransparentRule;
import com.topvision.ems.epon.vlan.domain.VlanTrunkRule;
import com.topvision.ems.epon.vlan.facade.OltVlanFacade;
import com.topvision.ems.epon.vlan.service.PonPortVlanService;
import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.network.service.EntityService;
import com.topvision.ems.template.service.EntityTypeService;
import com.topvision.framework.service.BaseService;
import com.topvision.framework.snmp.SnmpParam;
import com.topvision.framework.utils.EponIndex;
import com.topvision.nbi.tl1.api.domain.OnuInfo;
import com.topvision.platform.facade.FacadeFactory;
import com.topvision.platform.message.event.SynchronizedEvent;
import com.topvision.platform.message.event.SynchronizedListener;
import com.topvision.platform.message.service.MessageService;

/**
 * @author flack
 * @created @2013-10-25-下午4:37:12
 *
 */
@Service("ponPortVlanService")
public class PonPortVlanServiceImpl extends BaseService implements PonPortVlanService, SynchronizedListener {
    @Autowired
    private PonPortVlanDao ponPortVlanDao;
    @Autowired
    private MessageService messageService;
    @Autowired
    private EntityService entityService;
    @Autowired
    private FacadeFactory facadeFactory;
    @Autowired
    private OltPonDao oltPonDao;
    @Autowired
    private DeviceVersionService deviceVersionService;
    @Autowired
    private EntityTypeService entityTypeService;

    @Override
    @PreDestroy
    public void destroy() {
        super.destroy();
        messageService.removeListener(SynchronizedListener.class, this);
    }

    @Override
    @PostConstruct
    public void initialize() {
        super.initialize();
        messageService.addListener(SynchronizedListener.class, this);
    }

    @Override
    public void refreshVlanDataFromOlt(Long entityId) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        try {
            refreshPonTranslation(snmpParam);
            logger.info("refreshPonTranslation finish");
        } catch (Exception e) {
            logger.error("refreshPonTranslation wrong", e);
        }

        try {
            refreshPonTrunk(snmpParam);
            logger.info("refreshPonTrunk finish");
        } catch (Exception e) {
            logger.error("refreshPonTrunk wrong", e);
        }

        try {
            refreshPonTransparent(snmpParam);
            logger.info("refreshPonTransparent finish");
        } catch (Exception e) {
            logger.error("refreshPonTransparent wrong", e);
        }

        try {
            refreshPonAgg(snmpParam);
            logger.info("refreshPonAgg finish");
        } catch (Exception e) {
            logger.error("refreshPonAgg wrong", e);
        }

        try {
            refreshPonQinQ(snmpParam);
            logger.info("refreshPonQinQ finish");
        } catch (Exception e) {
            logger.error("refreshPonQinQ wrong", e);
        }

        try {
            refreshPonLlidTranslation(snmpParam);
            logger.info("refreshPonLlidTranslation finish");
        } catch (Exception e) {
            logger.error("refreshPonLlidTranslation wrong", e);
        }

        try {
            refreshPonLlidTrunk(snmpParam);
            logger.info("refreshPonLlidTrunk finish");
        } catch (Exception e) {
            logger.error("refreshPonLlidTrunk wrong", e);
        }

        try {
            refreshPonLlidAgg(snmpParam);
            logger.info("refreshPonLlidAgg finish");
        } catch (Exception e) {
            logger.error("refreshPonLlidAgg wrong", e);
        }

        try {
            // refreshPonLlidQinQ(snmpParam);
            logger.info("refreshPonLlidQinQ finish");
        } catch (Exception e) {
            logger.error("refreshPonLlidQinQ wrong", e);
        }

        try {
            refreshPonLlidOnuQinQ(snmpParam);
            logger.info("refreshPonLlidOnuQinQ finish");
        } catch (Exception e) {
            logger.error("refreshPonLlidOnuQinQ wrong", e);
        }
    }

    @Override
    public List<VlanTranslationRule> getTransList(Long ponId) {
        return ponPortVlanDao.getTransList(ponId);
    }

    @Override
    public void addTransRule(VlanTranslationRule vlanTranslationRule) {
        if (vlanTranslationRule != null && vlanTranslationRule.getPortId() != null) {
            // 获取并设置ponIndex
            Long ponIndex = oltPonDao.getPonIndex(vlanTranslationRule.getPortId());
            vlanTranslationRule.setPortIndex(ponIndex);
            vlanTranslationRule.setDeviceIndex(EponIndex.getOnuMibIndexByIndex(ponIndex));
            // 设备上添加转换规则
            Long entityId = vlanTranslationRule.getEntityId();
            if (entityId != null) {
                SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
                VlanTranslationRule newRule = getOltVlanFacade(snmpParam.getIpAddress()).addTransRule(snmpParam,
                        vlanTranslationRule);
                // Facade端返回数据验证
                if (newRule == null) {
                    // 与OLT连接异常
                    throw new PonVlanConfigException("Business.connection");
                } else {
                    if (newRule.getVlanIndex() != null && newRule.getTranslationNewVid() != null) {
                        // 数据库中添加转换规则
                        ponPortVlanDao.addTransRule(newRule);
                        if (!newRule.getVlanIndex().equals(vlanTranslationRule.getVlanIndex())
                                || !newRule.getTranslationNewVid().equals(vlanTranslationRule.getTranslationNewVid())) {
                            // 设置后的值与预期值不一致
                            throw new PonVlanConfigException("Business.addTransRuleDisaccord");
                        }
                    } else {
                        // 设置后的规则存在null值,设置失败.
                        throw new PonVlanConfigException("Business.addTransRuleFailure");
                    }
                }
            }
        }
    }

    @Override
    public void deleteTransRule(Long entityId, Long ponId, Integer vlanId) {
        // 设备上删除转换规则
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        VlanTranslationRule vlanTranslationRule = new VlanTranslationRule();
        vlanTranslationRule.setEntityId(entityId);
        Long ponIndex = oltPonDao.getPonIndex(ponId);
        vlanTranslationRule.setPortIndex(ponIndex);
        vlanTranslationRule.setVlanIndex(vlanId);
        getOltVlanFacade(snmpParam.getIpAddress()).deleteTransRule(snmpParam, vlanTranslationRule);
        // 数据库中删除转换规则
        ponPortVlanDao.deleteTransRule(ponId, vlanId);
    }

    @Override
    public List<VlanAggregationRule> getAggrList(Long ponId) {
        return ponPortVlanDao.getAggrList(ponId);
    }

    @Override
    public void addSvlanAggrRule(VlanAggregationRule vlanAggregationRule) {
        if (vlanAggregationRule != null && vlanAggregationRule.getPortId() != null) {
            Long portIndex = oltPonDao.getPonIndex(vlanAggregationRule.getPortId());
            vlanAggregationRule.setPortIndex(portIndex);
            Long entityId = vlanAggregationRule.getEntityId();
            if (entityId != null) {
                SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
                VlanAggregationRule newRule = getOltVlanFacade(snmpParam.getIpAddress()).addSvlanAggrRule(snmpParam,
                        vlanAggregationRule);
                // Facade端返回数据验证
                if (newRule == null) {
                    // 与OLT连接异常
                    throw new PonVlanConfigException("Business.connection");
                } else if (newRule.getPortAggregationVidIndex() != null && newRule.getAggregationVidList() != null) {
                    // 数据库中添加转换规则
                    ponPortVlanDao.addSvlanAggrRule(newRule);
                    if (!newRule.getPortAggregationVidIndex()
                            .equals(vlanAggregationRule.getPortAggregationVidIndex())) {
                        // 设置后的值与预期值不一致
                        throw new PonVlanConfigException("Business.addSvlanAggrRuleDisaccord");
                    } else if (!newRule.getAggregationVidList().equals(vlanAggregationRule.getAggregationVidList())) {
                        // 设置CVLAN的值与预期值不一致
                        throw new PonVlanConfigException("Business.addSvlanAggrRuleDisaccord");
                    }
                } else {
                    // 设置后的规则存在null值,设置失败.
                    throw new PonVlanConfigException("Business.addSvlanAggrRuleFailure");
                }
            }
        }
    }

    @Override
    public void deleteSvlanAggrRule(Long entityId, Long ponId, Integer vlanId) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        Long ponIndex = oltPonDao.getPonIndex(ponId);
        VlanAggregationRule vlanAggregationRule = new VlanAggregationRule();
        vlanAggregationRule.setEntityId(entityId);
        vlanAggregationRule.setPortIndex(ponIndex);
        vlanAggregationRule.setPortAggregationVidIndex(vlanId);
        getOltVlanFacade(snmpParam.getIpAddress()).deleteSvlanAggrRule(snmpParam, vlanAggregationRule);
        ponPortVlanDao.deleteSvlanAggrRule(ponId, vlanId);
    }

    @Override
    public void modifyCvlanAggrRule(VlanAggregationRule vlanAggregationRule) {
        if (vlanAggregationRule != null && vlanAggregationRule.getPortId() != null) {
            Long ponId = vlanAggregationRule.getPortId();
            Long portIndex = oltPonDao.getPonIndex(ponId);
            vlanAggregationRule.setPortIndex(portIndex);
            Long entityId = vlanAggregationRule.getEntityId();
            SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
            if (vlanAggregationRule.getAggregationVidList() != null) {
                if (vlanAggregationRule.getAggregationVidListAfterSwitch() != null
                        && vlanAggregationRule.getAggregationVidListAfterSwitch().size() > 0) {
                    VlanAggregationRule newRule = getOltVlanFacade(snmpParam.getIpAddress())
                            .modifyCvlanAggrRule(snmpParam, vlanAggregationRule);
                    // Facade端返回数据验证
                    if (newRule == null) {
                        // 与OLT连接异常
                        throw new PonVlanConfigException("Business.connection");
                    } else if (newRule.getPortAggregationVidIndex() != null
                            && newRule.getAggregationVidList() != null) {
                        ponPortVlanDao.modifyCvlanAggrRule(newRule);
                        if (!newRule.getAggregationVidList().equals(vlanAggregationRule.getAggregationVidList())) {
                            // 设置CVLAN的值与预期值不一致
                            throw new PonVlanConfigException("Business.modifyCvlanAggrRuleDisaccord");
                        }
                    } else {
                        // 设置后的规则存在null值,设置失败
                        throw new PonVlanConfigException("Business.modifyCvlanAggrRuleFailure");
                    }
                } else if (vlanAggregationRule.getPortAggregationVidIndex() != null) {
                    // 正在删除CVLAN的最后一条记录，直接将对应的SVLAN删除。
                    getOltVlanFacade(snmpParam.getIpAddress()).deleteSvlanAggrRule(snmpParam, vlanAggregationRule);
                    ponPortVlanDao.deleteSvlanAggrRule(ponId, vlanAggregationRule.getPortAggregationVidIndex());
                } else {
                    // 设置后的规则存在null值,设置失败
                    throw new PonVlanConfigException("Business.modifyCvlanAggrRuleFailure");
                }
            }
        }
    }

    @Override
    public VlanTrunkRule getTrunkList(Long ponId) {
        return ponPortVlanDao.getTrunkList(ponId);
    }

    @Override
    public void modifyTrunkRule(VlanTrunkRule vlanTrunkRule) {
        if (vlanTrunkRule != null && vlanTrunkRule.getPortId() != null && vlanTrunkRule.getEntityId() != null) {
            // 获取并设置ponIndex
            Long ponIndex = oltPonDao.getPonIndex(vlanTrunkRule.getPortId());
            vlanTrunkRule.setPortIndex(ponIndex);
            Long entityId = vlanTrunkRule.getEntityId();
            SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
            // 去数据库获取该PON口的trunk列表
            VlanTrunkRule sqlVlanTrunkRule = ponPortVlanDao.getTrunkList(vlanTrunkRule.getPortId());
            if (sqlVlanTrunkRule != null) {
                if (sqlVlanTrunkRule.getTrunkVidList() != null
                        && sqlVlanTrunkRule.getTrunkVidListAfterSwitch() != null) {
                    if (vlanTrunkRule.getTrunkVidList() != null) {
                        if (vlanTrunkRule.getTrunkVidListAfterSwitch() == null
                                || vlanTrunkRule.getTrunkVidListAfterSwitch().size() == 0) {
                            // delete该trunk规则
                            getOltVlanFacade(snmpParam.getIpAddress()).deleteTrunkRule(snmpParam, vlanTrunkRule);
                            // 数据库中删除转换规则
                            ponPortVlanDao.deleteTrunkRule(vlanTrunkRule.getPortId());
                        } else {
                            // 修改TrunkVidList
                            VlanTrunkRule newRule = getOltVlanFacade(snmpParam.getIpAddress())
                                    .modifyTrunkRule(snmpParam, vlanTrunkRule);
                            ponPortVlanDao.modifyTrunkRule(newRule);
                        }
                    } else {
                        // 设置后的值与预期值不一致
                        throw new PonVlanConfigException("Business.addTrunkRuleDisaccord");
                    }
                }
            } else {
                // 该PON口之前没有trunk的规则，添加新规则
                VlanTrunkRule newRule = getOltVlanFacade(snmpParam.getIpAddress()).addTrunkRule(snmpParam,
                        vlanTrunkRule);
                ponPortVlanDao.addTrunkRule(newRule);
            }
        } else {
            // 设置后的规则存在null值,设置失败.
            throw new PonVlanConfigException("Business.addTrunkRuleFailure");
        }
    }

    @Override
    public List<VlanQinQRule> getQinQList(Long ponId) {
        return ponPortVlanDao.getQinQList(ponId);
    }

    @Override
    public void addQinQRule(VlanQinQRule vlanQinQRule) {
        if (vlanQinQRule != null && vlanQinQRule.getPortId() != null) {
            Long portIndex = oltPonDao.getPonIndex(vlanQinQRule.getPortId());
            vlanQinQRule.setPortIndex(portIndex);
            Long entityId = vlanQinQRule.getEntityId();

            if (entityId != null) {
                SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
                VlanQinQRule newRule = getOltVlanFacade(snmpParam.getIpAddress()).addQinQRule(snmpParam, vlanQinQRule);
                // Facade端返回数据验证
                if (newRule == null) {
                    // 与OLT连接异常
                    throw new PonVlanConfigException("Business.connection");
                } else if (newRule.getPqStartVlanId() != null && newRule.getPqEndVlanId() != null
                        && newRule.getPqSVlanId() != null && newRule.getPqSTagCosDetermine() != null) {
                    Entity entity = entityService.getEntity(entityId);
                    if (entityTypeService.isPN8602_GType(entity.getTypeId())) {
                        // 数据库中添加转换规则
                        ponPortVlanDao.addQinQRule(newRule);
                        if (!newRule.getPqStartVlanId().equals(vlanQinQRule.getPqStartVlanId())
                                || !newRule.getPqEndVlanId().equals(vlanQinQRule.getPqEndVlanId())
                                || !newRule.getPqSVlanId().equals(vlanQinQRule.getPqSVlanId())) {
                            // 设置后的值与预期值不一致
                            throw new PonVlanConfigException("Business.addQinQRuleDisaccord");
                        } else if (!newRule.getPqSTagCosDetermine().equals(vlanQinQRule.getPqSTagCosDetermine())) {
                            // 设置COS的值与预期值不一致
                            throw new PonVlanConfigException("Business.addQinQCosDisaccord");
                        }
                    } else {
                        if(newRule.getPqSTagCosNewValue() != null){
                            ponPortVlanDao.addQinQRule(newRule);
                            if (!newRule.getPqStartVlanId().equals(vlanQinQRule.getPqStartVlanId())
                                    || !newRule.getPqEndVlanId().equals(vlanQinQRule.getPqEndVlanId())
                                    || !newRule.getPqSVlanId().equals(vlanQinQRule.getPqSVlanId())) {
                                // 设置后的值与预期值不一致
                                throw new PonVlanConfigException("Business.addQinQRuleDisaccord");
                            } else if (!newRule.getPqSTagCosDetermine().equals(vlanQinQRule.getPqSTagCosDetermine())
                                    || !newRule.getPqSTagCosNewValue().equals(vlanQinQRule.getPqSTagCosNewValue())) {
                                // 设置COS的值与预期值不一致
                                throw new PonVlanConfigException("Business.addQinQCosDisaccord");
                            }
                        }
                    }
                } else {
                    // 设置后的规则存在null值,设置失败.
                    throw new PonVlanConfigException("Business.addQinQRuleFailure");
                }
            }
        }
    }

    @Override
    public void deleteQinQRule(Long entityId, Long ponId, Integer startVlanId, Integer endVlanId) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        Long ponIndex = oltPonDao.getPonIndex(ponId);
        VlanQinQRule vlanQinQRule = new VlanQinQRule();
        vlanQinQRule.setPortIndex(ponIndex);
        vlanQinQRule.setPqStartVlanId(startVlanId);
        vlanQinQRule.setPqEndVlanId(endVlanId);
        getOltVlanFacade(snmpParam.getIpAddress()).deleteQinQRule(snmpParam, vlanQinQRule);
        ponPortVlanDao.deleteQinQRule(ponId, startVlanId, endVlanId);
    }

    @Override
    public VlanTransparentRule loadTransparentData(Long entityId, Long ponId) {
        return ponPortVlanDao.loadTransparentData(entityId, ponId);
    }

    @Override
    public void addTransparentRule(VlanTransparentRule vlanTransparentRule) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(vlanTransparentRule.getEntityId());
        getOltVlanFacade(snmpParam.getIpAddress()).addTransparentRule(snmpParam, vlanTransparentRule);
        ponPortVlanDao.addTransparentRule(vlanTransparentRule);
    }

    @Override
    public void delTransparentRule(VlanTransparentRule vlanTransparentRule) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(vlanTransparentRule.getEntityId());
        getOltVlanFacade(snmpParam.getIpAddress()).delTransparentRule(snmpParam, vlanTransparentRule);
        ponPortVlanDao.delTransparentRule(vlanTransparentRule);
    }

    @Override
    public void modifyTransparentRule(VlanTransparentRule vlanTransparentRule) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(vlanTransparentRule.getEntityId());
        getOltVlanFacade(snmpParam.getIpAddress()).modifyTransparentRule(snmpParam, vlanTransparentRule);
        ponPortVlanDao.modifyTransparentRule(vlanTransparentRule);
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

    /**
     * 更新PON口 转换规则
     * 
     * @param snmpParam
     */
    @Override
    public void refreshPonTranslation(SnmpParam snmpParam) {
        List<VlanTranslationRule> translationRules = getOltVlanFacade(snmpParam.getIpAddress())
                .getVlanTranslationRules(snmpParam);
        List<VlanTranslationRule> ponRules = new ArrayList<VlanTranslationRule>();
        if (translationRules != null) {
            for (VlanTranslationRule translationRule : translationRules) {
                translationRule.setEntityId(snmpParam.getEntityId());
                translationRule.setPortIndex(translationRule.getPortIndex());
                translationRule.setPortId(
                        oltPonDao.getPonIdByPonIndex(snmpParam.getEntityId(), translationRule.getPortIndex()));
                ponRules.add(translationRule);
            }
            if (ponRules != null) {
                ponPortVlanDao.batchInsertOltVlanTranslation(ponRules, snmpParam.getEntityId());
            }
        }
    }

    /**
     * 更新PON口 Trunk规则
     * 
     * @param snmpParam
     */
    @Override
    public void refreshPonTrunk(SnmpParam snmpParam) {
        List<VlanTrunkRule> trunkRules = getOltVlanFacade(snmpParam.getIpAddress()).getVlanTrunkRules(snmpParam);
        List<VlanTrunkRule> ponRules = new ArrayList<VlanTrunkRule>();
        if (trunkRules != null) {
            for (VlanTrunkRule trunkRule : trunkRules) {
                trunkRule.setEntityId(snmpParam.getEntityId());
                if (EponIndex.getOnuNoByMibDeviceIndex(trunkRule.getDeviceIndex()) == 0) {
                    ponRules.add(trunkRule);
                }
            }
            if (ponRules != null) {
                ponPortVlanDao.batchInsertOltVlanTrunk(ponRules, snmpParam.getEntityId());
            }
        }
    }

    /**
     * 更新PON口 聚合规则
     * 
     * @param snmpParam
     */
    @Override
    public void refreshPonAgg(SnmpParam snmpParam) {
        List<VlanAggregationRule> aggregationRules = getOltVlanFacade(snmpParam.getIpAddress())
                .getVlanAggregationRules(snmpParam);
        List<VlanAggregationRule> ponRules = new ArrayList<VlanAggregationRule>();
        if (aggregationRules != null) {
            for (VlanAggregationRule aggregationRule : aggregationRules) {
                aggregationRule.setEntityId(snmpParam.getEntityId());
                if (EponIndex.getOnuNoByMibDeviceIndex(aggregationRule.getDeviceIndex()) == 0) {
                    ponRules.add(aggregationRule);
                }
            }
            if (ponRules != null) {
                ponPortVlanDao.batchInsertOltVlanAgg(ponRules, snmpParam.getEntityId());
            }
        }
    }

    /**
     * 更新PON口 Transparent规则
     * 
     * @param snmpParam
     */
    @Override
    public void refreshPonTransparent(SnmpParam snmpParam) {
        List<VlanTransparentRule> rules = getOltVlanFacade(snmpParam.getIpAddress()).getVlanTransparentRules(snmpParam);
        List<VlanTransparentRule> ponRules = new ArrayList<VlanTransparentRule>();
        if (rules != null) {
            for (VlanTransparentRule rule : rules) {
                rule.setEntityId(snmpParam.getEntityId());
                if (EponIndex.getOnuNoByMibDeviceIndex(rule.getDeviceIndex()) == 0) {
                    rule.setPortIndex(EponIndex.getPonIndexByMibDeviceIndex(rule.getDeviceIndex()));
                    ponRules.add(rule);
                }
            }
            if (ponRules != null) {
                ponPortVlanDao.batchInsertOltVlanTransparent(ponRules, snmpParam.getEntityId());
            }
        }
    }

    /**
     * 更新PON口 QinQ规则
     * 
     * @param snmpParam
     */
    @Override
    public void refreshPonQinQ(SnmpParam snmpParam) {
        List<VlanQinQRule> qinQRules = getOltVlanFacade(snmpParam.getIpAddress()).getVlanQinQRules(snmpParam);
        List<VlanQinQRule> ponRules = new ArrayList<VlanQinQRule>();
        if (qinQRules != null) {
            for (VlanQinQRule qinQRule : qinQRules) {
                qinQRule.setEntityId(snmpParam.getEntityId());
                if (EponIndex.getOnuNoByMibDeviceIndex(qinQRule.getDeviceIndex()) == 0) {
                    ponRules.add(qinQRule);
                }
            }
            if (ponRules != null) {
                ponPortVlanDao.batchInsertOltPonQinQ(ponRules, snmpParam.getEntityId());
            }
        }

    }

    /**
     * 更新PON口 转换规则(基于LLID)
     * 
     * @param snmpParam
     */
    @Override
    public void refreshPonLlidTranslation(SnmpParam snmpParam) {
        List<VlanLlidTranslationRule> translationRules = getOltVlanFacade(snmpParam.getIpAddress())
                .getVlanLlidTranslationRules(snmpParam);
        if (translationRules != null) {
            for (VlanLlidTranslationRule translationRule : translationRules) {
                translationRule.setEntityId(snmpParam.getEntityId());
            }
            if (translationRules != null) {
                ponPortVlanDao.batchInsertTopVlanTrans(translationRules, snmpParam.getEntityId());
            }
        }
    }

    /**
     * 更新PON口 Trunk规则(基于LLID)
     * 
     * @param snmpParam
     */
    @Override
    public void refreshPonLlidTrunk(SnmpParam snmpParam) {
        List<VlanLlidTrunkRule> trunkRules = getOltVlanFacade(snmpParam.getIpAddress())
                .getVlanLlidTrunkRules(snmpParam);
        if (trunkRules != null) {
            for (VlanLlidTrunkRule trunkRule : trunkRules) {
                trunkRule.setEntityId(snmpParam.getEntityId());
            }
            if (trunkRules != null) {
                ponPortVlanDao.batchInsertTopVlanTrunk(trunkRules, snmpParam.getEntityId());
            }
        }
    }

    /**
     * 更新PON口 聚合规则(基于LLID)
     * 
     * @param snmpParam
     */
    @Override
    public void refreshPonLlidAgg(SnmpParam snmpParam) {
        List<VlanLlidAggregationRule> aggregationRules = getOltVlanFacade(snmpParam.getIpAddress())
                .getVlanLlidAggregationRules(snmpParam);
        if (aggregationRules != null) {
            for (VlanLlidAggregationRule aggregationRule : aggregationRules) {
                aggregationRule.setEntityId(snmpParam.getEntityId());
            }
            if (aggregationRules != null) {
                ponPortVlanDao.batchInsertTopVlanAgg(aggregationRules, snmpParam.getEntityId());
            }
        }
    }

    /**
     * 更新PON口 QinQ规则(基于LLID)
     * 
     * @param snmpParam
     */
    @Override
    public void refreshPonLlidQinQ(SnmpParam snmpParam) {
        List<VlanLlidQinQRule> qinQRules = getOltVlanFacade(snmpParam.getIpAddress()).getvlanLlidQinQRules(snmpParam);
        if (qinQRules != null) {
            for (VlanLlidQinQRule qinQRule : qinQRules) {
                qinQRule.setEntityId(snmpParam.getEntityId());
            }
            if (qinQRules != null) {
                ponPortVlanDao.batchInsertTopVlanQinQ(qinQRules, snmpParam.getEntityId());
            }
        }
    }

    @Autowired
    private OnuService onuService;

    /**
     * 更新PON口 QinQ规则(基于LLID,适配TL1)
     * 
     * @param snmpParam
     */
    @Override
    public void refreshPonLlidOnuQinQ(SnmpParam snmpParam) {
        List<VlanLlidOnuQinQRule> qinQRules = getOltVlanFacade(snmpParam.getIpAddress())
                .getvlanLlidOnuQinQRules(snmpParam);
        List<VlanLlidOnuQinQRule> updateQinQRules = new ArrayList<>();
        if (qinQRules != null) {
            for (VlanLlidOnuQinQRule qinQRule : qinQRules) {
                qinQRule.setEntityId(snmpParam.getEntityId());
                try {
                    Long onuId = onuService.getOnuIdByIndex(snmpParam.getEntityId(), qinQRule.getOnuIndex());
                    String onuMac = onuService.getOnuAttribute(onuId).getOnuMac();
                    qinQRule.setMac(onuMac);
                    updateQinQRules.add(qinQRule);
                } catch (Exception e) {
                    logger.error("", e);
                }
            }
            if (updateQinQRules != null) {
                ponPortVlanDao.batchInsertTopVlanOnuQinQ(qinQRules, snmpParam.getEntityId());
            }
        }
    }

    @Override
    public List<PonCvidModeRela> getPonCvidModeRela(Long ponId) {
        return ponPortVlanDao.getPonCvidModeRela(ponId);
    }

    @Override
    public List<PonSvidModeRela> getPonSvidModeRela(Long entityId, Long ponId) {
        return ponPortVlanDao.getPonSvidModeRela(entityId, ponId);
    }

    @Override
    public void insertEntityStates(SynchronizedEvent event) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(event.getEntityId());
        try {
            refreshPonTranslation(snmpParam);
            logger.info("refreshPonTranslation finish");
        } catch (Exception e) {
            logger.error("refreshPonTranslation wrong", e);
        }

        try {
            refreshPonTrunk(snmpParam);
            logger.info("refreshPonTrunk finish");
        } catch (Exception e) {
            logger.error("refreshPonTrunk wrong", e);
        }

        try {
            refreshPonTransparent(snmpParam);
            logger.info("refreshPonTransparent finish");
        } catch (Exception e) {
            logger.error("refreshPonTransparent wrong", e);
        }

        try {
            refreshPonAgg(snmpParam);
            logger.info("refreshPonAgg finish");
        } catch (Exception e) {
            logger.error("refreshPonAgg wrong", e);
        }

        try {
            refreshPonQinQ(snmpParam);
            logger.info("refreshPonQinQ finish");
        } catch (Exception e) {
            logger.error("refreshPonQinQ wrong", e);
        }

        try {
            refreshPonLlidTranslation(snmpParam);
            logger.info("refreshPonLlidTranslation finish");
        } catch (Exception e) {
            logger.error("refreshPonLlidTranslation wrong", e);
        }

        try {
            refreshPonLlidTrunk(snmpParam);
            logger.info("refreshPonLlidTrunk finish");
        } catch (Exception e) {
            logger.error("refreshPonLlidTrunk wrong", e);
        }

        try {
            refreshPonLlidAgg(snmpParam);
            logger.info("refreshPonLlidAgg finish");
        } catch (Exception e) {
            logger.error("refreshPonLlidAgg wrong", e);
        }

        try {
            // refreshPonLlidQinQ(snmpParam);
            logger.info("refreshPonLlidQinQ finish");
        } catch (Exception e) {
            logger.error("refreshPonLlidQinQ wrong", e);
        }

        try {
            refreshPonLlidOnuQinQ(snmpParam);
            logger.info("refreshPonLlidOnuQinQ finish");
        } catch (Exception e) {
            logger.error("refreshPonLlidOnuQinQ wrong", e);
        }
    }

    @Override
    public void updateEntityStates(SynchronizedEvent event) {
    }

    @Override
    public PortVlanAttribute getPonVlan(Long ponId) {
        return ponPortVlanDao.queryPonVlan(ponId);
    }

    @Override
    public void updatePonPvid(PortVlanAttribute ponVlan) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(ponVlan.getEntityId());
        // 调用facade的modifyPortVlanAttributes方法修改端口Vlan基本属性
        Boolean isSupport = deviceVersionService.isFunctionSupported(ponVlan.getEntityId(), "ponVlanEntry");
        if (isSupport) {
            OltPortVlanEntry oltPortVlanEntry = new OltPortVlanEntry();
            oltPortVlanEntry.setEntityId(ponVlan.getEntityId());
            oltPortVlanEntry.setPortId(ponVlan.getPortId());
            oltPortVlanEntry.setPortIndex(ponVlan.getPortIndex());
            oltPortVlanEntry.setVlanPVid(ponVlan.getVlanPVid());
            getOltVlanFacade(snmpParam.getIpAddress()).modifyOltPortVlanEntry(snmpParam, oltPortVlanEntry);
        } else {
            getOltVlanFacade(snmpParam.getIpAddress()).modifyPortVlanAttributes(snmpParam, ponVlan);
        }
        ponPortVlanDao.updatePonPvid(ponVlan.getPortId(), ponVlan.getVlanPVid());
    }

    @Override
    public void refreshPonVlanInfo(Long entityId, Long ponId, Long portIndex) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        Boolean isSupport = deviceVersionService.isFunctionSupported(entityId, "ponVlanEntry");
        PortVlanAttribute ponVlan = new PortVlanAttribute();
        ponVlan.setEntityId(entityId);
        ponVlan.setPortId(ponId);
        ponVlan.setPortIndex(portIndex);
        if (isSupport) {
            OltPortVlanEntry oltPortVlanEntry = new OltPortVlanEntry();
            oltPortVlanEntry.setPortIndex(portIndex);
            oltPortVlanEntry = getOltVlanFacade(snmpParam.getIpAddress()).getSingleOltPortVlanEntry(snmpParam,
                    oltPortVlanEntry);
            ponVlan.setVlanMode(oltPortVlanEntry.getVlanMode());
            ponVlan.setVlanPVid(oltPortVlanEntry.getVlanPVid());
            ponVlan.setVlanTagPriority(oltPortVlanEntry.getVlanTagPriority());
        } else {
            ponVlan = getOltVlanFacade(snmpParam.getIpAddress()).getSinglePortVlanAttribute(snmpParam, ponVlan);
        }
        ponPortVlanDao.insertOrUpdatePonVlan(ponVlan);
    }

}
