/***********************************************************************
 * $Id: UniVlanServiceImpl.java,v1.0 2013-10-25 下午4:34:53 $
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

import com.topvision.ems.epon.onu.dao.OnuDao;
import com.topvision.ems.epon.onu.dao.UniDao;
import com.topvision.ems.epon.onu.domain.OltOnuAttribute;
import com.topvision.ems.epon.topology.event.OnuSynchronizedEvent;
import com.topvision.ems.epon.topology.event.OnuSynchronizedListener;
import com.topvision.ems.epon.univlanprofile.domain.UniVlanBindTable;
import com.topvision.ems.epon.univlanprofile.service.UniVlanProfileService;
import com.topvision.ems.epon.vlan.dao.UniVlanDao;
import com.topvision.ems.epon.vlan.domain.PortVlanAttribute;
import com.topvision.ems.epon.vlan.domain.VlanAggregationRule;
import com.topvision.ems.epon.vlan.domain.VlanTranslationRule;
import com.topvision.ems.epon.vlan.domain.VlanTrunkRule;
import com.topvision.ems.epon.vlan.facade.OltVlanFacade;
import com.topvision.ems.epon.vlan.service.UniVlanService;
import com.topvision.ems.network.service.EntityService;
import com.topvision.framework.service.BaseService;
import com.topvision.framework.snmp.SnmpParam;
import com.topvision.framework.utils.EponConstants;
import com.topvision.framework.utils.EponIndex;
import com.topvision.platform.facade.FacadeFactory;
import com.topvision.platform.message.service.MessageService;

/**
 * @author jay
 * @created @2011-10-24-19:02:29
 */
@Service("uniVlanService")
public class UniVlanServiceImpl extends BaseService implements UniVlanService, OnuSynchronizedListener {
    @Autowired
    private UniVlanDao uniVlanDao;
    @Autowired
    private FacadeFactory facadeFactory;
    @Autowired
    protected MessageService messageService;
    @Autowired
    private EntityService entityService;
    @Autowired
    private UniVlanProfileService uniVlanProfileService;
    @Autowired
    private UniDao uniDao;
    @Autowired
    private OnuDao onuDao;
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
    public PortVlanAttribute getUniVlanAttribute(Long uniId) {
        // 调用底层UniVlanDao的getPortVlanAttribute(uniId)方法获得PortVlanAttribute对象
        PortVlanAttribute portVlanAttribute = uniVlanDao.getPortVlanAttribute(uniId);
        if (portVlanAttribute != null) {
            portVlanAttribute.setPortString(EponIndex.getStringFromIndex(portVlanAttribute.getPortIndex()));
        } else {
            portVlanAttribute = new PortVlanAttribute();
            Long uniIndex = uniDao.getUniIndex(uniId);
            portVlanAttribute.setPortIndex(uniIndex);
            portVlanAttribute.setPortString(EponIndex.getStringFromIndex(uniIndex));
        }
        // 返回PortVlanAttribute对象
        return portVlanAttribute;
    }

    @Override
    public void updateUniVlanAttribute(PortVlanAttribute portVlanAttribute) {
        // 构建snmpParam参数
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(portVlanAttribute.getEntityId());
        // 调用facade的modifyPortVlanAttributes方法修改端口Vlan基本属性
        getOltVlanFacade(snmpParam.getIpAddress()).modifyPortVlanAttributes(snmpParam, portVlanAttribute);
        uniVlanDao.updatePortVlanAttribute(portVlanAttribute);
        //切换模式后需要同步模板数据和UNI口绑定信息
        //if (portVlanAttribute.getVlanMode() != null) {
        //修改模式后需要更新VLAN模板和规则数据
        uniVlanProfileService.refreshUniVlanInfo(portVlanAttribute.getEntityId(), portVlanAttribute.getPortIndex());
        refreshSingleUniVlanAttribute(portVlanAttribute.getEntityId(), portVlanAttribute.getPortId());
        //}
    }

    @Override
    public List<VlanTranslationRule> getVlanTranslationRuleList(Long uniId) {
        // 调用底层UniVlanDao的getVlanTranslationRuleList(uniId)方法获得转换规则列表
        return uniVlanDao.getVlanTranslationRuleList(uniId);
    }

    @Override
    public void addVlanTranslationRule(VlanTranslationRule vlanTranslationRule) {
        Long entityId = vlanTranslationRule.getEntityId();
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        // 获取并设置ponIndex
        Long uniIndex = uniDao.getUniIndex(vlanTranslationRule.getPortId());
        vlanTranslationRule.setPortIndex(uniIndex);
        // 设备上添加转换规则
        getOltVlanFacade(snmpParam.getIpAddress()).addTransRule(snmpParam, vlanTranslationRule);
        uniVlanDao.addVlanTranslationRule(vlanTranslationRule);
    }

    @Override
    public void deleteVlanTranslationRule(VlanTranslationRule vlanTranslationRule) {
        // 构建snmpParam参数
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(vlanTranslationRule.getEntityId());
        getOltVlanFacade(snmpParam.getIpAddress()).deleteTransRule(snmpParam, vlanTranslationRule);
        uniVlanDao.deleteVlanTranslationRule(vlanTranslationRule);
    }

    @Deprecated
    @Override
    public void deleteVlanTransDB(Long uniId, Integer vlanIndex) {
        // 获得需要删除的转换规则
        VlanTranslationRule vlanTranslationRule = uniVlanDao.getVlanTranslationRule(uniId, vlanIndex);
        uniVlanDao.deleteVlanTranslationRule(vlanTranslationRule);
    }

    @Override
    public void deleteVlanTranslationRuleByAfter(Long uniId, Integer vlanIndex, Integer newVlan) {
        // 获得需要删除的转换规则
        VlanTranslationRule vlanTranslationRule = uniVlanDao.getVlanTranslationRuleByAfter(uniId, vlanIndex, newVlan);
        // 构建snmpParam参数
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(vlanTranslationRule.getEntityId());
        // 调用facade的deleteTransRule方法删除设备中的一条转换规则
        // TODO 调用facade删除规则时不能带有除索引外的其它属性，否则会出现no such name异常
        VlanTranslationRule v1 = new VlanTranslationRule();
        v1.setPortIndex(vlanTranslationRule.getPortIndex());
        v1.setVlanIndex(vlanTranslationRule.getVlanIndex());
        getOltVlanFacade(snmpParam.getIpAddress()).deleteTransRule(snmpParam, v1);
        // 调用底层UniVlanDao的deleteVlanTranslationRule(vlanTranslationRule)方法删除一条转换规则
        uniVlanDao.deleteVlanTranslationRule(vlanTranslationRule);
    }

    @Override
    public List<VlanAggregationRule> getVlanAggregationRuleList(Long uniId) {
        // 调用uniVlanDao的getVlanAggregationRuleList(uniId)方法获得聚合规则列表
        return uniVlanDao.getVlanAggregationRuleList(uniId);
    }

    @Override
    public void addSVlanAggregationRule(VlanAggregationRule vlanAggregationRule) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(vlanAggregationRule.getEntityId());
        getOltVlanFacade(snmpParam.getIpAddress()).addSvlanAggrRule(snmpParam, vlanAggregationRule);
        uniVlanDao.addVlanAggregationRule(vlanAggregationRule);
    }

    @Override
    public void deleteSVlanAggregationRule(VlanAggregationRule vlanAggregationRule) {
        // 构建snmpParam参数
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(vlanAggregationRule.getEntityId());
        // 调用facade的deleteSvlanAggrRule方法删除设备中的聚合规则
        getOltVlanFacade(snmpParam.getIpAddress()).deleteSvlanAggrRule(snmpParam, vlanAggregationRule);
        // 调用底层UniVlanDao的deleteVlanAggregationRule(uniId, vlanIndex)方法删除聚合规则
        uniVlanDao.deleteVlanAggregationRule(vlanAggregationRule);
    }

    @Deprecated
    @Override
    public void deleteSVlanAggrDB(Long uniId, Integer vlanIndex) {
        // 获得需要删除的聚合规则对象
        VlanAggregationRule vlanAggregationRule = uniVlanDao.getVlanAggregationRule(uniId, vlanIndex);
        uniVlanDao.deleteVlanAggregationRule(vlanAggregationRule);
    }

    @Override
    public void modifyCVlanAggregationRule(VlanAggregationRule vlanAggregationRule) {
        // 构建snmp参数
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(vlanAggregationRule.getEntityId());
        getOltVlanFacade(snmpParam.getIpAddress()).deleteSvlanAggrRule(snmpParam, vlanAggregationRule);
        uniVlanDao.addVlanAggregationRule(vlanAggregationRule);

    }

    @Override
    public VlanTrunkRule getVlanTrunkRules(Long uniId) {
        // 调用uniVlanDao的getVlanTrunkRuleList(uniId)方法获取Trunk规则列表
        return uniVlanDao.getVlanTrunkRules(uniId);
    }

    @Override
    public void updateUniVlanTrunkRule(VlanTrunkRule vlanTrunkRule) {
        Long entityId = vlanTrunkRule.getEntityId();
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        // 去数据库获取该Uni口的trunk列表
        VlanTrunkRule sqlVlanTrunkRule = uniVlanDao.getVlanTrunkRules(vlanTrunkRule.getPortId());
        if (sqlVlanTrunkRule != null && sqlVlanTrunkRule.getTrunkVidList() != null
                && sqlVlanTrunkRule.getTrunkVidListAfterSwitch() != null) {
            if (vlanTrunkRule.getTrunkVidListAfterSwitch() == null) {
                // delete该trunk规则
                VlanTrunkRule delTrunkRule = new VlanTrunkRule();
                delTrunkRule.setPortIndex(vlanTrunkRule.getPortIndex());
                getOltVlanFacade(snmpParam.getIpAddress()).deleteTrunkRule(snmpParam, delTrunkRule);
                // 数据库中删除转换规则
                uniVlanDao.deleteVlanTrunkRule(vlanTrunkRule.getPortId());
            } else {
                // 修改TrunkVidList
                VlanTrunkRule newRule = getOltVlanFacade(snmpParam.getIpAddress()).modifyTrunkRule(snmpParam,
                        vlanTrunkRule);
                uniVlanDao.updateVlanTrunkRule(newRule);
            }
        } else {
            // 该Uni口之前没有trunk的规则，添加新规则
            VlanTrunkRule newRule = getOltVlanFacade(snmpParam.getIpAddress()).addTrunkRule(snmpParam, vlanTrunkRule);
            uniVlanDao.addVlanTrunkRule(newRule);
        }
    }

    /**
     * 更新OLT设备 UNI口VLAN基本属性
     * 
     * @param snmpParam
     */
    @Override
    public void refreshUniPortVlanAttribute(Long entityId) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        //修改模式后需要更新VLAN模板和规则数据
        uniVlanProfileService.refreshProfileAndRule(entityId);
        //修改模式后需要更新VLAN模板绑定关系
        uniVlanProfileService.refreshUniVlanData(entityId);
        //刷新整个设备
        List<PortVlanAttribute> uniPortVlanAttributes = getOltVlanFacade(snmpParam.getIpAddress())
                .getPortVlanAttributes(snmpParam);
        List<PortVlanAttribute> uniPortVlan = new ArrayList<PortVlanAttribute>();
        if (uniPortVlanAttributes != null) {
            for (PortVlanAttribute portVlanAttribute : uniPortVlanAttributes) {
                portVlanAttribute.setEntityId(snmpParam.getEntityId());
                // 判断deviceIndex是否为1来区分是UNI端口数据还是其它端口数据
                if (EponIndex.getOnuNoByMibDeviceIndex(portVlanAttribute.getDeviceIndex()) != 0) {
                    uniPortVlan.add(portVlanAttribute);
                }
            }
            uniVlanDao.batchInsertOnuPortVlan(uniPortVlan, snmpParam.getEntityId());
            //刷新转换规则
            refreshUniTranslationRule(entityId);
            //刷新聚合规则
            refreshUniAggRule(entityId);
            //刷新Trunk规则
            refreshUniTrunkRule(entityId);
        }
    }

    @Override
    public void refreshSingleUniVlanAttribute(Long entityId, Long uniId) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        //刷新单个UNI端口
        Long uniIndex = uniDao.getUniIndex(uniId);
        PortVlanAttribute uniPortVlanAttribute = new PortVlanAttribute();
        uniPortVlanAttribute.setEntityId(entityId);
        uniPortVlanAttribute.setPortId(uniId);
        uniPortVlanAttribute.setPortIndex(uniIndex);
        uniPortVlanAttribute = getOltVlanFacade(snmpParam.getIpAddress()).getSinglePortVlanAttribute(snmpParam,
                uniPortVlanAttribute);
        uniPortVlanAttribute.setPortId(uniId);
        uniPortVlanAttribute.setEntityId(entityId);
        uniVlanDao.updatePortVlanAttribute(uniPortVlanAttribute);
        switch (uniPortVlanAttribute.getVlanMode()) {
        case EponConstants.TRANSLATION_MODE:
            //设备不支持提供部分索引的方式获取数据
            //refreshSingleUniTranslationRule(entityId, uniId);
            refreshUniTranslationRule(entityId);
            break;
        case EponConstants.AGGREGATION_MODE:
            //TODO 设备不支持单个端口刷新，只能刷新整个设备
            refreshUniAggRule(entityId);
            break;
        case EponConstants.TRUNK_MODE:
            //refreshSingleUniTrunkRule(entityId, uniId);
            refreshUniTrunkRule(entityId);
            break;
        default:
            break;
        }
    }

    /**
     * 更新UNI口 转换规则
     * 
     * @param snmpParam
     */
    @Override
    public void refreshUniTranslationRule(Long entityId) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        //刷新整个设备
        List<VlanTranslationRule> translationRules = getOltVlanFacade(snmpParam.getIpAddress())
                .getVlanTranslationRules(snmpParam);
        List<VlanTranslationRule> uniRules = new ArrayList<VlanTranslationRule>();
        if (translationRules != null) {
            for (VlanTranslationRule translationRule : translationRules) {
                translationRule.setEntityId(snmpParam.getEntityId());
                if (EponIndex.getOnuNoByMibDeviceIndex(translationRule.getDeviceIndex()) != 0) {
                    uniRules.add(translationRule);
                }
            }
            uniVlanDao.batchInsertOnuVlanTranslation(uniRules, entityId);
        }
    }

    @Override
    public void refreshSingleUniTranslationRule(Long entityId, Long uniId) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        //刷新单个UNI端口
        Long uniIndex = uniDao.getUniIndex(uniId);
        VlanTranslationRule transRule = new VlanTranslationRule();
        transRule.setPortIndex(uniIndex);
        List<VlanTranslationRule> translationRules = getOltVlanFacade(snmpParam.getIpAddress())
                .getPortTranslationRules(snmpParam, transRule);
        if (translationRules != null) {
            for (VlanTranslationRule translationRule : translationRules) {
                translationRule.setEntityId(entityId);
            }
            uniVlanDao.batchInsertUniVlanTranslation(translationRules, uniId);
        }
    }

    /**
     * 更新UNI口 Trunk规则
     * 
     * @param snmpParam
     */
    @Override
    public void refreshUniTrunkRule(Long entityId) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        //刷新整个设备
        List<VlanTrunkRule> trunkRules = getOltVlanFacade(snmpParam.getIpAddress()).getVlanTrunkRules(snmpParam);
        List<VlanTrunkRule> uniRules = new ArrayList<VlanTrunkRule>();
        if (trunkRules != null) {
            for (VlanTrunkRule trunkRule : trunkRules) {
                trunkRule.setEntityId(snmpParam.getEntityId());
                // 判断deviceIndex是否为1来区分是UNI端口数据还是其它端口数据
                if (EponIndex.getOnuNoByMibDeviceIndex(trunkRule.getDeviceIndex()) != 0) {
                    uniRules.add(trunkRule);
                }
            }
            uniVlanDao.batchInsertOnuVlanTrunk(uniRules, snmpParam.getEntityId());
        }
    }

    @Override
    public void refreshSingleUniTrunkRule(Long entityId, Long uniId) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        Long uniIndex = uniDao.getUniIndex(uniId);
        VlanTrunkRule trunkRule = new VlanTrunkRule();
        trunkRule.setPortIndex(uniIndex);
        trunkRule.setEntityId(entityId);
        trunkRule.setPortId(uniId);
        trunkRule = getOltVlanFacade(snmpParam.getIpAddress()).getPortVlanTrunkRule(snmpParam, trunkRule);
        if (trunkRule != null) {
            uniVlanDao.updateVlanTrunkRule(trunkRule);
        }
    }

    /**
     * 更新UNI口 聚合规则
     * 
     * @param snmpParam
     */
    @Override
    public void refreshUniAggRule(Long entityId) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        List<VlanAggregationRule> aggregationRules = getOltVlanFacade(snmpParam.getIpAddress())
                .getVlanAggregationRules(snmpParam);
        List<VlanAggregationRule> uniRules = new ArrayList<VlanAggregationRule>();
        if (aggregationRules != null) {
            for (VlanAggregationRule aggregationRule : aggregationRules) {
                aggregationRule.setEntityId(snmpParam.getEntityId());
                // 判断deviceIndex是否为1来区分是UNI端口数据还是其它端口数据
                if (EponIndex.getOnuNoByMibDeviceIndex(aggregationRule.getDeviceIndex()) != 0) {
                    uniRules.add(aggregationRule);
                }
            }
            uniVlanDao.batchInsertOnuVlanAgg(uniRules, snmpParam.getEntityId());
        }
    }

    @Override
    public void refreshSingleUniAggRule(Long entityId, Long uniId) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        //刷新单个UNI端口
        Long uniIndex = uniDao.getUniIndex(uniId);
        VlanAggregationRule aggrRule = new VlanAggregationRule();
        aggrRule.setPortIndex(uniIndex);
        List<VlanAggregationRule> aggrRules = getOltVlanFacade(snmpParam.getIpAddress()).getPortAggregationRules(
                snmpParam, aggrRule);
        if (aggrRules != null) {
            for (VlanAggregationRule aggr : aggrRules) {
                aggr.setEntityId(entityId);
            }
            uniVlanDao.batchInsertUniVlanAgg(aggrRules, uniId);
        }
    }

    @Override
    public void copyUniVlanAttribute(Long entityId, Long sourceIndex, Long uniIndex) {
        // SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        // 获得复制源UNI Id
        Long sourceUniId = uniDao.getUniIdByIndex(entityId, sourceIndex);
        Long targetId = uniDao.getUniIdByIndex(entityId, uniIndex);
        UniVlanBindTable sourceBind = uniVlanProfileService.getUniBindInfo(sourceUniId, entityId);
        if (sourceBind != null && sourceBind.getBindProfileId() != 0) {
            // 获取复制配置的源端口VLAN 模板绑定关系
            UniVlanBindTable targetBind = uniVlanProfileService.getUniBindInfo(targetId, entityId);
            /*
             * if (targetBind != null && targetBind.getBindProfileId() != 0) {
             * targetBind.setBindProfileId(0); uniVlanProfileService.unBindUniProfile(targetBind); }
             */
            targetBind.setBindProfileId(sourceBind.getBindProfileId());
            targetBind.setBindPvid(sourceBind.getBindPvid());
            uniVlanProfileService.replaceBindProfile(targetBind);
        } else {
            PortVlanAttribute uniPortVlan = this.getUniVlanAttribute(sourceUniId);
            PortVlanAttribute uniVlan = new PortVlanAttribute();
            uniVlan.setEntityId(entityId);
            uniVlan.setPortIndex(uniIndex);
            uniVlan.setPortId(targetId);
            uniVlan.setVlanPVid(uniPortVlan.getVlanPVid());
            uniVlan.setVlanMode(uniPortVlan.getVlanMode());
            this.updateUniVlanAttribute(uniVlan);
        }
        uniVlanProfileService.refreshUniVlanInfo(entityId, uniIndex);
        refreshSingleUniVlanAttribute(entityId, targetId);
    }

    @Override
    public List<PortVlanAttribute> getOnuPortVlanList(Long onuId) {
        return uniVlanDao.getOnuPortVlanList(onuId);
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
    public void insertEntityStates(OnuSynchronizedEvent event) {
        Long entityId = event.getEntityId();
        List<Long> onuIndexlList = event.getOnuIndexList();
        if (onuIndexlList.size() == ONU_SINGLE_TOPO) {
            List<Long> uniIndexList = uniDao.getUniIndexListByEntityIdAndOnuIndex(entityId, onuIndexlList.get(0));
            OltOnuAttribute onuAttr = onuDao.getOnuAttributeByIndex(entityId, onuIndexlList.get(0));
            if (EponConstants.EPON_ONU.equals(onuAttr.getOnuEorG())) {
                for (Long index : uniIndexList) {
                    Long uniId = uniDao.getUniIdByIndex(entityId, index);
                    refreshSingleUniVlanAttribute(entityId, uniId);
                }
            }
        } else if (onuIndexlList.size() > ONU_SINGLE_TOPO) {
            try {
                refreshUniPortVlanAttribute(entityId);
                logger.info("refresh uni port vlan finish");
            } catch (Exception e) {
                logger.error("refresh uni port vlan wrong {}", e);
            }
        }
    }
}