package com.topvision.ems.epon.vlan.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.epon.univlanprofile.service.UniVlanProfileService;
import com.topvision.ems.epon.vlan.dao.PonPortVlanDao;
import com.topvision.ems.epon.vlan.dao.SniVlanDao;
import com.topvision.ems.epon.vlan.dao.UniVlanDao;
import com.topvision.ems.epon.vlan.domain.OltPortVlanEntry;
import com.topvision.ems.epon.vlan.domain.PortVlanAttribute;
import com.topvision.ems.epon.vlan.domain.VlanAggregationRule;
import com.topvision.ems.epon.vlan.domain.VlanTranslationRule;
import com.topvision.ems.epon.vlan.domain.VlanTrunkRule;
import com.topvision.ems.epon.vlan.facade.OltVlanFacade;
import com.topvision.ems.network.service.EntityService;
import com.topvision.framework.service.BaseService;
import com.topvision.framework.snmp.SnmpParam;
import com.topvision.framework.utils.EponIndex;
import com.topvision.platform.facade.FacadeFactory;
import com.topvision.platform.message.event.SynchronizedEvent;
import com.topvision.platform.message.event.SynchronizedListener;
import com.topvision.platform.message.service.MessageService;

/**
 * @author huqiao
 * @created @2011-11-1-上午09:13:44
 * 
 */
@Service("oltVlanService")
public class OltVlanServiceImpl extends BaseService implements SynchronizedListener {
    @Autowired
    private MessageService messageService;
    @Autowired
    private EntityService entityService;
    @Autowired
    private FacadeFactory facadeFactory;
    @Autowired
    private PonPortVlanDao ponPortVlanDao;
    @Autowired
    private SniVlanDao sniVlanDao;
    @Autowired
    private UniVlanDao uniVlanDao;
    @Autowired
    private UniVlanProfileService uniVlanProfileService;

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
    public void insertEntityStates(SynchronizedEvent event) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(event.getEntityId());
        try {
            discoveryPortVlanAttribute(snmpParam);
            logger.info("discoveryPortVlanAttribute finish");
        } catch (Exception e) {
            logger.error("discoveryPortVlanAttribute wrong", e);
        }
        // 端口规则（包括UNI口和PON口共有的规则）
        try {
            discoveryPortVlanTranslationEntry(snmpParam);
            logger.info("discoveryPortVlanTranslationEntry finish");
        } catch (Exception e) {
            logger.error("discoveryPortVlanTranslationEntry wrong", e);
        }

        try {
            discovertPortVlanAggregationConfigEntry(snmpParam);
            logger.info("discovertPortVlanAggregationConfigEntry finish");
        } catch (Exception e) {
            logger.error("discovertPortVlanAggregationConfigEntry wrong", e);
        }

        try {
            discoveryPortVlanTrunkEntry(snmpParam);
            logger.info("discoveryPortVlanTrunkEntry finish");
        } catch (Exception e) {
            logger.error("discoveryPortVlanTrunkEntry wrong", e);
        }

        try {
            uniVlanProfileService.refreshProfileData(event.getEntityId());
            logger.info("refreshProfileData finish");
        } catch (Exception e) {
            logger.error("refreshProfileData wrong", e);
        }

        try {
            uniVlanProfileService.refreshRuleData(event.getEntityId());
            logger.info("refreshRuleData finish");
        } catch (Exception e) {
            logger.error("refreshRuleData wrong", e);
        }
    }

    public void discoveryPortVlanAttribute(SnmpParam snmpParam) {
        List<PortVlanAttribute> portVlanAttributes = getOltVlanFacade(snmpParam.getIpAddress()).getPortVlanAttributes(
                snmpParam);
        Long entityId = snmpParam.getEntityId();
        List<OltPortVlanEntry> oltPortVlanEntrys = new ArrayList<OltPortVlanEntry>();
        List<PortVlanAttribute> oltPortVlan = new ArrayList<PortVlanAttribute>();
        List<PortVlanAttribute> uniPortVlan = new ArrayList<PortVlanAttribute>();
        try {
            oltPortVlanEntrys = getOltVlanFacade(snmpParam.getIpAddress()).getOltPortVlanEntry(snmpParam);
            for (OltPortVlanEntry oltPortVlanEntry : oltPortVlanEntrys) {
                PortVlanAttribute portVlanAttribute = new PortVlanAttribute();
                portVlanAttribute.setPortIndex(oltPortVlanEntry.getPortIndex());
                portVlanAttribute.setVlanPVid(oltPortVlanEntry.getVlanPVid());
                portVlanAttribute.setVlanTagPriority(oltPortVlanEntry.getVlanTagPriority());
                portVlanAttribute.setVlanMode(oltPortVlanEntry.getVlanMode());
                portVlanAttribute.setEntityId(entityId);
                oltPortVlan.add(portVlanAttribute);
            }
        } catch (Exception e) {
            logger.debug("getOltPortVlanEntry fail,please check deviceVersion", e);
        }

        if (portVlanAttributes != null) {
            for (PortVlanAttribute portVlanAttribute : portVlanAttributes) {
                portVlanAttribute.setEntityId(entityId);
                if (EponIndex.getOnuNoByMibDeviceIndex(portVlanAttribute.getDeviceIndex()) != 0) {
                    portVlanAttribute.setVlanTagPriority(0);
                    uniPortVlan.add(portVlanAttribute);
                } else {
                    oltPortVlan.add(portVlanAttribute);
                }
            }
            // 原先需要先判断list的大小，如果为0认为不处理，实则不然，当为0时实际是设备就没有数据，需要删除原先数据
            if (oltPortVlan != null) {
                sniVlanDao.batchInsertOltPortVlan(oltPortVlan, entityId);
                ponPortVlanDao.batchInsertPonVlan(oltPortVlan, entityId);
            }
            if (uniPortVlan != null) {
                uniVlanDao.batchInsertOnuPortVlan(uniPortVlan, snmpParam.getEntityId());
            }
        }
    }

    public void discoveryPortVlanTranslationEntry(SnmpParam snmpParam) {
        List<VlanTranslationRule> translationRules = getOltVlanFacade(snmpParam.getIpAddress())
                .getVlanTranslationRules(snmpParam);
        List<VlanTranslationRule> ponRules = new ArrayList<VlanTranslationRule>();
        List<VlanTranslationRule> uniRules = new ArrayList<VlanTranslationRule>();
        if (translationRules != null) {
            for (VlanTranslationRule translationRule : translationRules) {
                translationRule.setEntityId(snmpParam.getEntityId());
                if (EponIndex.getOnuNoByMibDeviceIndex(translationRule.getDeviceIndex()) == 0) {
                    ponRules.add(translationRule);
                } else if (EponIndex.getOnuNoByMibDeviceIndex(translationRule.getDeviceIndex()) != 0) {
                    uniRules.add(translationRule);
                }
            }
            if (ponRules != null) {
                ponPortVlanDao.batchInsertOltVlanTranslation(ponRules, snmpParam.getEntityId());
            }
            if (uniRules != null) {
                uniVlanDao.batchInsertOnuVlanTranslation(uniRules, snmpParam.getEntityId());
            }
        }
    }

    public void discovertPortVlanAggregationConfigEntry(SnmpParam snmpParam) {
        List<VlanAggregationRule> aggregationRules = getOltVlanFacade(snmpParam.getIpAddress())
                .getVlanAggregationRules(snmpParam);
        List<VlanAggregationRule> ponRules = new ArrayList<VlanAggregationRule>();
        List<VlanAggregationRule> uniRules = new ArrayList<VlanAggregationRule>();
        if (aggregationRules != null) {
            for (VlanAggregationRule aggregationRule : aggregationRules) {
                aggregationRule.setEntityId(snmpParam.getEntityId());
                if (EponIndex.getOnuNoByMibDeviceIndex(aggregationRule.getDeviceIndex()) == 0) {
                    ponRules.add(aggregationRule);
                } else if (EponIndex.getOnuNoByMibDeviceIndex(aggregationRule.getDeviceIndex()) != 0) {
                    uniRules.add(aggregationRule);
                }
            }
            if (ponRules != null) {
                ponPortVlanDao.batchInsertOltVlanAgg(ponRules, snmpParam.getEntityId());
            }
            if (uniRules != null) {
                uniVlanDao.batchInsertOnuVlanAgg(uniRules, snmpParam.getEntityId());
            }
        }
    }

    public void discoveryPortVlanTrunkEntry(SnmpParam snmpParam) {
        List<VlanTrunkRule> trunkRules = getOltVlanFacade(snmpParam.getIpAddress()).getVlanTrunkRules(snmpParam);
        List<VlanTrunkRule> ponRules = new ArrayList<VlanTrunkRule>();
        List<VlanTrunkRule> uniRules = new ArrayList<VlanTrunkRule>();
        if (trunkRules != null) {
            for (VlanTrunkRule trunkRule : trunkRules) {
                trunkRule.setEntityId(snmpParam.getEntityId());
                if (EponIndex.getOnuNoByMibDeviceIndex(trunkRule.getDeviceIndex()) == 0) {
                    ponRules.add(trunkRule);
                } else if (EponIndex.getOnuNoByMibDeviceIndex(trunkRule.getDeviceIndex()) != 0) {
                    uniRules.add(trunkRule);
                }
            }
            if (ponRules != null) {
                ponPortVlanDao.batchInsertOltVlanTrunk(ponRules, snmpParam.getEntityId());
            }
            if (uniRules != null) {
                uniVlanDao.batchInsertOnuVlanTrunk(uniRules, snmpParam.getEntityId());
            }
        }
    }

    @Override
    public void updateEntityStates(SynchronizedEvent event) {
    }

    private OltVlanFacade getOltVlanFacade(String ip) {
        return facadeFactory.getFacade(ip, OltVlanFacade.class);
    }

}
