/***********************************************************************
 * $Id: VlanListServiceImpl.java,v1.0 2016年6月8日 上午10:19:58 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.vlan.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.epon.olt.domain.OltPonAttribute;
import com.topvision.ems.epon.olt.service.OltPonService;
import com.topvision.ems.epon.vlan.dao.VlanListDao;
import com.topvision.ems.epon.vlan.domain.OltPortVlan;
import com.topvision.ems.epon.vlan.domain.OltPortVlanRelation;
import com.topvision.ems.epon.vlan.domain.PonVlanPortLocation;
import com.topvision.ems.epon.vlan.domain.UniPortVlan;
import com.topvision.ems.epon.vlan.domain.VlanAggregationRule;
import com.topvision.ems.epon.vlan.domain.VlanAttribute;
import com.topvision.ems.epon.vlan.domain.VlanQinQRule;
import com.topvision.ems.epon.vlan.domain.VlanTranslationRule;
import com.topvision.ems.epon.vlan.domain.VlanTransparentRule;
import com.topvision.ems.epon.vlan.service.PonLlidVlanService;
import com.topvision.ems.epon.vlan.service.PonPortVlanService;
import com.topvision.ems.epon.vlan.service.SniVlanService;
import com.topvision.ems.epon.vlan.service.UniVlanService;
import com.topvision.ems.epon.vlan.service.VlanListService;
import com.topvision.ems.message.CallbackableRequest;
import com.topvision.ems.message.LongRequestExecutorService;
import com.topvision.ems.network.service.EntityService;
import com.topvision.framework.service.BaseService;
import com.topvision.framework.snmp.SnmpParam;
import com.topvision.platform.util.StringUtil;
import com.topvision.platform.zetaframework.util.ZetaUtil;

/**
 * @author Bravin
 * @created @2016年6月8日-上午10:19:58
 *
 */
@Service
public class VlanListServiceImpl extends BaseService implements VlanListService {
    @Autowired
    private SniVlanService sniVlanService;
    @Autowired
    private PonPortVlanService ponPortVlanService;
    @Autowired
    private PonLlidVlanService ponLlidVlanService;
    @Autowired
    private UniVlanService uniVlanService;
    @Autowired
    private EntityService entityService;
    @Autowired
    private OltPonService oltPonService;
    @Autowired
    private LongRequestExecutorService longRequestExecutorService;
    @Autowired
    private VlanListDao vlanListDao;

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.epon.vlan.service.VlanListService#refreshVlan(java.lang.Long)
     */
    @Override
    public void refreshVlan(final Long entityId, String jconnectionId) {
        longRequestExecutorService.executeRequest(new CallbackableRequest(jconnectionId) {

            @Override
            protected void execute() {
                SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
                returnMessage(ZetaUtil.getStaticString("PROCESS.1", "oltvlan"));
                sniVlanService.refreshSniVlanAttribute(snmpParam);
                sniVlanService.refreshSniVlanView(snmpParam);
                sniVlanService.refreshSniPortVlan(snmpParam);
                sniVlanService.refreshVlanVif(entityId);
                sniVlanService.refreshTopMcFloodMode(snmpParam);

                returnMessage(ZetaUtil.getStaticString("PROCESS.2", "oltvlan"));
                ponPortVlanService.refreshVlanDataFromOlt(entityId);

                returnMessage(ZetaUtil.getStaticString("PROCESS.3", "oltvlan"));
                uniVlanService.refreshUniPortVlanAttribute(entityId);
            }
        });
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.epon.vlan.service.VlanListService#loadSniPortVlanList(java.lang.Long)
     */
    @Override
    public List<OltPortVlan> loadSniPortVlanList(Long entityId) {
        List<OltPortVlan> list = vlanListDao.selectSniPortVlanList(entityId);
        List<OltPortVlanRelation> relations = vlanListDao.selectPortVlanRelation(entityId);
        Map<Long, OltPortVlan> mapper = new HashMap<>();
        for (OltPortVlan vlan : list) {
            mapper.put(vlan.getPortIndex(), vlan);
        }
        for (OltPortVlanRelation relation : relations) {
            int type = relation.getType();
            Long index = relation.getPortIndex();
            Integer vlanIndex = relation.getVlanIndex();
            OltPortVlan oltSniPortVlan = mapper.get(index);
            if (oltSniPortVlan == null) {
                continue;
            }
            switch (type) {
            case OltPortVlanRelation.UNTAG_PORT:
                oltSniPortVlan.addPortUntagVlan(vlanIndex);
                break;
            case OltPortVlanRelation.TAG_PORT:
                oltSniPortVlan.addPortTagVlan(vlanIndex);
                break;
            }
        }
        return list;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.epon.vlan.service.VlanListService#batchApplyPortVlanConfig(java.lang.Long,
     * java.util.List, java.util.List, java.util.List)
     */
    @Override
    public void batchApplyPortVlanConfig(Long entityId, List<Long> sniIndexs, List<Integer> taggedList,
            List<Integer> untaggedList) {
        /** 第一步:找出未应用前将要配置VLAN的端口所属的VLAN，这些VLAN需要被保留 */
        List<OltPortVlan> portVlanList = new ArrayList<OltPortVlan>();
        List<OltPortVlan> sniPortVlans = loadSniPortVlanList(entityId);
        List<OltPortVlan> ponPortVlans = loadPonPortVlanList(entityId);
        portVlanList.addAll(sniPortVlans);
        portVlanList.addAll(ponPortVlans);
        List<Integer> relatedVlanList = new ArrayList<Integer>();
        for (OltPortVlan $sniPortVlan : portVlanList) {
            if (sniIndexs.contains($sniPortVlan.getPortIndex())) {
                relatedVlanList.addAll($sniPortVlan.getTagVlanList());
                relatedVlanList.addAll($sniPortVlan.getUntagVlanList());
            }
        }
        /** 第二步:将第一步的VLAN和将要配置的VLAN合并,将2部分的VLAN都保留下来 */
        List<VlanAttribute> vlanList = sniVlanService.getOltVlanConfigList(entityId);
        Map<Integer, VlanAttribute> vlanMapper = new HashMap<>();
        relatedVlanList.addAll(untaggedList);
        relatedVlanList.addAll(taggedList);
        for (VlanAttribute $vlanAttribute : vlanList) {
            Integer vlanIndex = $vlanAttribute.getVlanIndex();
            if (relatedVlanList.contains(vlanIndex)) {
                vlanMapper.put(vlanIndex, $vlanAttribute);
            }
        }
        /** 组装出一个表达配置但非最新状态的VLAN-PORT结构 */
        class Vlan {
            public List<Long> taggedPortList = new ArrayList<Long>();
            public List<Long> untaggedPortList = new ArrayList<Long>();

            public void addTagPort(Long index) {
                taggedPortList.add(index);
            }

            public void addUntagPort(Long index) {
                untaggedPortList.add(index);
            }
        }
        Map<Integer, Vlan> map = new HashMap<>();
        for (Long portIndex : sniIndexs) {
            for (Integer vlan : taggedList) {
                Vlan port = map.get(vlan);
                if (port == null) {
                    port = new Vlan();
                    map.put(vlan, port);
                }
                port.addTagPort(portIndex);
            }
            for (Integer vlan : untaggedList) {
                Vlan port = map.get(vlan);
                if (port == null) {
                    port = new Vlan();
                    map.put(vlan, port);
                }
                port.addUntagPort(portIndex);
            }
        }

        /** 第三步:循环被保留的VLAN,对于配置有所变化的VLAN，应用配置,无变化的VLAN跳过 */
        List<VlanAttribute> readyApplyList = new ArrayList<VlanAttribute>();
        Iterator<Integer> iterator = vlanMapper.keySet().iterator();
        while (iterator.hasNext()) {
            Integer vlanIndex = iterator.next();
            VlanAttribute vlanAttribute = vlanMapper.get(vlanIndex);
            /** 循环第一层,prev数据和this数据对比,如果prev有，this没有.则删除 */
            List<Long> untaggedPortIndexList = vlanAttribute.getUntaggedPortIndexList();
            if (untaggedPortIndexList == null) {
                untaggedPortIndexList = new ArrayList<Long>();
                vlanAttribute.setUntaggedPortIndexList(untaggedPortIndexList);
            }
            List<Long> taggedPortIndexList = vlanAttribute.getTaggedPortIndexList();
            if (taggedPortIndexList == null) {
                taggedPortIndexList = new ArrayList<Long>();
                vlanAttribute.setTaggedPortIndexList(taggedPortIndexList);
            }
            /** vlan可能为null */
            Vlan vlan = map.get(vlanIndex);
            Iterator<Long> untagItereator = untaggedPortIndexList.iterator();
            for (; untagItereator.hasNext();) {
                Long index = untagItereator.next();
                if (!sniIndexs.contains(index)) {
                    continue;
                }
                if (vlan == null || !vlan.untaggedPortList.contains(index)) {
                    untagItereator.remove();
                    /* 发生过变化的VLAN都需要被下发* */
                    if (!readyApplyList.contains(vlanAttribute)) {
                        readyApplyList.add(vlanAttribute);
                    }
                }
            }
            Iterator<Long> tagItereator = taggedPortIndexList.iterator();
            for (; tagItereator.hasNext();) {
                Long index = tagItereator.next();
                if (!sniIndexs.contains(index)) {
                    continue;
                }
                if (vlan == null || !vlan.taggedPortList.contains(index)) {
                    tagItereator.remove();
                    /* 发生过变化的VLAN都需要被下发* */
                    if (!readyApplyList.contains(vlanAttribute)) {
                        readyApplyList.add(vlanAttribute);
                    }
                }
            }
            /** 循环第二层,this数据和prev数据对比,如果this有，prev没有.则添加 */
            if (vlan != null) {
                Iterator<Long> vlanUntagIterator = vlan.untaggedPortList.iterator();
                Iterator<Long> vlanTagIterator = vlan.taggedPortList.iterator();
                for (; vlanUntagIterator.hasNext();) {
                    Long index = vlanUntagIterator.next();
                    if (untaggedPortIndexList == null || !untaggedPortIndexList.contains(index)) {
                        untaggedPortIndexList.add(index);
                        /* 发生过变化的VLAN都需要被下发* */
                        if (!readyApplyList.contains(vlanAttribute)) {
                            readyApplyList.add(vlanAttribute);
                        }
                    }
                }
                for (; vlanTagIterator.hasNext();) {
                    Long index = vlanTagIterator.next();
                    if (untaggedPortIndexList == null || !taggedPortIndexList.contains(index)) {
                        taggedPortIndexList.add(index);
                        /* 发生过变化的VLAN都需要被下发* */
                        if (!readyApplyList.contains(vlanAttribute)) {
                            readyApplyList.add(vlanAttribute);
                        }
                    }
                }
            }
        }

        for (VlanAttribute vlanAttribute : readyApplyList) {
            /** 重置一次,使portIndexList和IndexMib建立关系 */
            vlanAttribute.setTaggedPortIndexList(vlanAttribute.getTaggedPortIndexList());
            vlanAttribute.setUntaggedPortIndexList(vlanAttribute.getUntaggedPortIndexList());
            /** 配置下发 */
            sniVlanService.updateTagStatus(entityId, vlanAttribute.getVlanIndex(),
                    vlanAttribute.getTaggedPortIndexList(), vlanAttribute.getUntaggedPortIndexList(), null);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.epon.vlan.service.VlanListService#loadUniPortVlan(java.lang.Long)
     */
    @Override
    public List<UniPortVlan> loadUniPortVlan(Long onuId) {
        return vlanListDao.selectUniPortVlan(onuId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.epon.vlan.service.VlanListService#loadPonPortVlanList(java.lang.Long)
     */
    @Override
    public List<OltPortVlan> loadPonPortVlanList(Long entityId) {
        List<OltPortVlan> list = vlanListDao.selectPonPortVlanList(entityId);
        List<OltPortVlanRelation> relations = vlanListDao.selectPortVlanRelation(entityId);
        Map<Long, OltPortVlan> mapper = new HashMap<>();
        for (OltPortVlan vlan : list) {
            mapper.put(vlan.getPortIndex(), vlan);
        }
        for (OltPortVlanRelation relation : relations) {
            int type = relation.getType();
            Long index = relation.getPortIndex();
            Integer vlanIndex = relation.getVlanIndex();
            OltPortVlan oltSniPortVlan = mapper.get(index);
            if (oltSniPortVlan == null) {
                continue;
            }
            switch (type) {
            case OltPortVlanRelation.UNTAG_PORT:
                oltSniPortVlan.addPortUntagVlan(vlanIndex);
                break;
            case OltPortVlanRelation.TAG_PORT:
                oltSniPortVlan.addPortTagVlan(vlanIndex);
                break;
            }
        }
        return list;
    }

    /**
     * 
     * @param list
     * @param targetPort
     * @param oltPonAttribute
     */
    private void copyAggregationVlanConfig(List<VlanAggregationRule> list, Long targetPort,
            OltPonAttribute oltPonAttribute) {
        Long entityId = oltPonAttribute.getEntityId();
        List<VlanAggregationRule> rules = ponPortVlanService.getAggrList(targetPort);
        if (rules != null) {
            for (VlanAggregationRule rule : rules) {
                ponPortVlanService.deleteSvlanAggrRule(entityId, targetPort, rule.getPortAggregationVidIndex());
            }
        }
        for (VlanAggregationRule rule : list) {
            rule.setPortId(targetPort);
            rule.setPortIndex(oltPonAttribute.getPonIndex());
            ponPortVlanService.addSvlanAggrRule(rule);
        }
    }

    /**
     * 
     * @param list
     * @param targetPort
     * @param oltPonAttribute
     */
    private void copyQinqVlanConfig(List<VlanQinQRule> list, Long targetPort, OltPonAttribute oltPonAttribute) {
        Long entityId = oltPonAttribute.getEntityId();
        List<VlanQinQRule> rules = ponPortVlanService.getQinQList(targetPort);
        if (rules != null) {
            for (VlanQinQRule rule : rules) {
                ponPortVlanService.deleteQinQRule(entityId, targetPort, rule.getPqStartVlanId(), rule.getPqEndVlanId());
            }
        }
        for (VlanQinQRule rule : list) {
            rule.setPortId(targetPort);
            rule.setPortIndex(oltPonAttribute.getPonIndex());
            ponPortVlanService.addQinQRule(rule);
        }
    }

    /**
     * 
     * @param list
     * @param targetPort
     * @param oltPonAttribute
     */
    private void copyTranslationVlanConfig(List<VlanTranslationRule> list, Long targetPort,
            OltPonAttribute oltPonAttribute) {
        Long entityId = oltPonAttribute.getEntityId();
        List<VlanTranslationRule> rules = ponPortVlanService.getTransList(targetPort);
        if (rules != null) {
            for (VlanTranslationRule rule : rules) {
                ponPortVlanService.deleteTransRule(entityId, targetPort, rule.getVlanIndex());
            }
        }
        for (VlanTranslationRule rule : list) {
            rule.setPortId(targetPort);
            rule.setPortIndex(oltPonAttribute.getPonIndex());
            ponPortVlanService.addTransRule(rule);
        }
    }

    /**
     * 
     * @param vlanTransparentRule
     * @param targetPort
     * @param oltPonAttribute
     */
    private void copyTransparentVlanConfig(VlanTransparentRule vlanTransparentRule, Long targetPort,
            OltPonAttribute oltPonAttribute) {
        VlanTransparentRule rule = ponPortVlanService.loadTransparentData(oltPonAttribute.getEntityId(), targetPort);
        // 清除该PON口所有的VLAN配置
        if (rule != null) {
            ponPortVlanService.delTransparentRule(rule);
        }
        // 配置新的规则
        if (vlanTransparentRule != null) {
            vlanTransparentRule.setPortId(targetPort);
            vlanTransparentRule.setPortIndex(oltPonAttribute.getPonIndex());
            ponPortVlanService.addTransparentRule(vlanTransparentRule);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.epon.vlan.service.VlanListService#batchCopyPonServiceVlanConfig(java.lang
     * .Long, java.util.List, java.lang.String, java.lang.String)
     */
    @Override
    public void batchCopyPonServiceVlanConfig(final Long ponId, final List<Long> targetPonIdList,
            final String services, String jconnectionId) {
        longRequestExecutorService.executeRequest(new CallbackableRequest(jconnectionId) {

            @Override
            protected void execute() {
                /** 把src端口的配置提前查询出来，减少对数据库的重复访问 */
                OltPonAttribute srcPon = oltPonService.getPonAttribute(ponId);
                VlanTransparentRule transparent = ponPortVlanService.loadTransparentData(srcPon.getEntityId(), ponId);
                List<VlanTranslationRule> transList = ponPortVlanService.getTransList(ponId);
                List<VlanQinQRule> qinqList = ponPortVlanService.getQinQList(ponId);
                List<VlanAggregationRule> aggrList = ponPortVlanService.getAggrList(ponId);
                /* 分别对各端口应用配置 */
                if (StringUtil.isEmpty(services)) {
                    throw new RuntimeException("services error");
                }
                String[] serviceList = services.split(",");
                int sum = targetPonIdList.size() * serviceList.length;
                int counter = 0;
                List<String> errorPortServices = new ArrayList<String>();
                ZetaUtil zetaUtil = new ZetaUtil("oltVlan");
                for (Long targetPort : targetPonIdList) {
                    OltPonAttribute oltPonAttribute = oltPonService.getPonAttribute(targetPort);
                    for (String service : serviceList) {
                        counter++;
                        try {
                            switch (service) {
                            case "TRANSPARENT":
                                copyTransparentVlanConfig(transparent, targetPort, oltPonAttribute);
                                break;
                            case "TRANSLATION":
                                copyTranslationVlanConfig(transList, targetPort, oltPonAttribute);
                                break;
                            case "QINQ":
                                copyQinqVlanConfig(qinqList, targetPort, oltPonAttribute);
                                break;
                            case "AGGREGATION":
                                copyAggregationVlanConfig(aggrList, targetPort, oltPonAttribute);
                                break;
                            }
                        } catch (Exception e) {
                            logger.error("", e);
                            errorPortServices.add(oltPonAttribute.getPonIndex() + "|" + service);
                        }
                        returnMessage(zetaUtil.getString("Refresh.copyCount") + counter + "/" + sum
                                + zetaUtil.getString("Refresh.errorCount") + errorPortServices.size());
                    }
                }
                completeRequest(zetaUtil.getString("Refresh.successCount") + counter
                        + zetaUtil.getString("Refresh.errorCount") + errorPortServices.size());
            }

        });
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.epon.vlan.service.VlanListService#loadSlotPonList(java.lang.Long)
     */
    @Override
    public List<PonVlanPortLocation> loadSlotPonList(Long entityId) {
        return vlanListDao.selectSlotPonList(entityId);
    }
}
