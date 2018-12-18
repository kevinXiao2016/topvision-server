/***********************************************************************
 * $Id: SniVlanAction.java,v1.0 2013-10-25 下午1:50:56 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.vlan.action;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.topvision.ems.epon.domain.PonCvidModeRela;
import com.topvision.ems.epon.domain.PonSvidModeRela;
import com.topvision.ems.epon.olt.action.AbstractEponAction;
import com.topvision.ems.epon.olt.domain.OltPonAttribute;
import com.topvision.ems.epon.olt.service.OltPonService;
import com.topvision.ems.epon.vlan.domain.VlanAggregationRule;
import com.topvision.ems.epon.vlan.domain.VlanQinQRule;
import com.topvision.ems.epon.vlan.domain.VlanTranslationRule;
import com.topvision.ems.epon.vlan.domain.VlanTransparentRule;
import com.topvision.ems.epon.vlan.domain.VlanTrunkRule;
import com.topvision.ems.epon.vlan.service.PonPortVlanService;
import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.network.service.EntityService;
import com.topvision.ems.template.service.EntityTypeService;
import com.topvision.framework.annotation.OperationLogProperty;
import com.topvision.framework.common.MacUtils;
import com.topvision.framework.utils.EponConstants;
import com.topvision.platform.domain.UserContext;

/**
 * @author flack
 * @created @2013-10-25-下午1:50:56
 *
 */
@Controller("ponPortVlanAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class PonPortVlanAction extends AbstractEponAction {
    private static final long serialVersionUID = 2258040514027117318L;
    private final Logger logger = LoggerFactory.getLogger(PonPortVlanAction.class);

    private Long ponId;
    private Integer newBeforeTransVid;
    private Integer newAfterTransVid;
    private Integer oldBeforeTransVid;
    // private Integer oldAfterTransVid;
    private String aggrCvids;
    private Integer aggrSvid;
    private String trunkVids;
    private Integer qinqStartCvid;
    private Integer qinqEndCvid;
    private Integer qinqSvid;
    private Integer qinqSTagCosDetermine;
    private Integer qinqSTagCosNewValue;
    private String transparentIdStr;
    private String transparentModeStr;
    private Entity entity;
    private String tabNum;
    private String tipNum;
    private Integer ponPortType = 1;
    private boolean is8602G;

    @Autowired
    private PonPortVlanService ponPortVlanService;
    @Autowired
    private EntityService entityService;
    @Autowired
    private OltPonService oltPonService;
    @Autowired
    private EntityTypeService entitytypeService;

    /**
     * 显示PON口VLAN配置管理
     * 
     * @return String
     */
    public String showPonVlanConfig() {
        entity = entityService.getEntity(entityId);
        is8602G = entitytypeService.isPN8602_GType(entity.getTypeId());
        OltPonAttribute port = oltPonService.getPonAttribute(ponId);
        if (port != null) {
            ponPortType = port.getPonPortType();
        }
        return SUCCESS;
    }

    /**
     * 获取CVID与VLAN模式关系
     * 
     * @return
     * @throws Exception
     */
    public String loadPonCvidModeRela() throws Exception {
        // add by fanzidong,格式化mac地址
        UserContext uc = (UserContext) super.getSession().get(UserContext.KEY);
        String macRule = uc.getMacDisplayStyle();
        List<PonCvidModeRela> cvidList = ponPortVlanService.getPonCvidModeRela(ponId);
        // 将数据库中获取的cvidList数据转换为页面使用的CVID列表数据
        List<PonCvidModeRela> cvidData = new ArrayList<PonCvidModeRela>();
        PonCvidModeRela rela;
        for (PonCvidModeRela ponCvidModeRela : cvidList) {
            if (ponCvidModeRela != null && ponCvidModeRela.getCvidList() != null) {
                for (Integer cvidTmp : ponCvidModeRela.getCvidList()) {
                    if (cvidTmp >= 1) {
                        rela = new PonCvidModeRela();
                        rela.setCvid(cvidTmp);
                        rela.setVlanMode(ponCvidModeRela.getVlanMode());
                        if (ponCvidModeRela.getOnuMac() != null) {
                            String onuMacStr = new MacUtils(ponCvidModeRela.getOnuMacAddress())
                                    .toString(MacUtils.MAOHAO).toUpperCase();
                            // add by fanzidong,需要在展示前格式化MAC地址
                            String formatedMac = MacUtils.convertMacToDisplayFormat(onuMacStr, macRule);
                            rela.setOnuMacAddress(formatedMac);
                        }
                        cvidData.add(rela);
                    }
                }
            }
        }
        logger.debug("cvidData:{}", cvidData);
        writeDataToAjax(JSONArray.fromObject(cvidData));
        return NONE;
    }

    /**
     * 获取SVID与VLAN模式关系
     * 
     * @return
     * @throws Exception
     */
    public String loadPonSvidModeRela() throws Exception {
        // add by fanzidong,格式化mac地址
        UserContext uc = (UserContext) super.getSession().get(UserContext.KEY);
        String macRule = uc.getMacDisplayStyle();
        List<PonSvidModeRela> svidList = ponPortVlanService.getPonSvidModeRela(entityId, ponId);
        // 将数据库中获取的svidList数据转换为页面使用的SVID列表数据
        List<PonSvidModeRela> svidData = new ArrayList<PonSvidModeRela>();
        PonSvidModeRela rela;
        List<Integer> tmpConfigSvid = new ArrayList<Integer>();// 配置过的SVID
        for (PonSvidModeRela ponSvidModeRela : svidList) {
            if (ponSvidModeRela != null && ponSvidModeRela.getSvidList() != null) {
                for (Integer svidTmp : ponSvidModeRela.getSvidList()) {
                    if (svidTmp > 0) {
                        rela = new PonSvidModeRela();
                        rela.setSvid(svidTmp);
                        rela.setVlanMode(ponSvidModeRela.getVlanMode());
                        if (ponSvidModeRela.getVlanMode() != 0 && !tmpConfigSvid.contains(svidTmp)) {
                            tmpConfigSvid.add(svidTmp);
                        }
                        if (ponSvidModeRela.getOnuMacAddress() != null) {
                            String onuMacStr = new MacUtils(ponSvidModeRela.getOnuMacAddress())
                                    .toString(MacUtils.MAOHAO).toUpperCase();
                            // add by fanzidong,需要在展示前格式化MAC地址
                            String formatedMac = MacUtils.convertMacToDisplayFormat(onuMacStr, macRule);
                            rela.setOnuMacAddress(formatedMac);
                        }
                        svidData.add(rela);
                    }
                }
            }
        }
        List<PonSvidModeRela> tmpNeedDel = new ArrayList<PonSvidModeRela>();
        for (PonSvidModeRela p : svidData) {
            if (p.getVlanMode() == 0 && tmpConfigSvid.contains(p.getSvid())) {
                tmpNeedDel.add(p);
            }
        }
        svidData.removeAll(tmpNeedDel);
        logger.debug("svidData:{}", svidData);
        writeDataToAjax(JSONArray.fromObject(svidData));
        return NONE;
    }

    /**
     * 获取转换列表JSON数据
     * 
     * @return
     * @throws Exception
     */
    public String loadTransData() throws Exception {
        List<VlanTranslationRule> transRuleList = ponPortVlanService.getTransList(ponId);
        // 将数据库中获取的transRuleList数据转换为页面使用的transData列表数据
        List<List<Integer>> result = new ArrayList<List<Integer>>();
        if (transRuleList != null) {
            List<Integer> tmpList;
            for (VlanTranslationRule rule : transRuleList) {
                tmpList = new ArrayList<Integer>();
                tmpList.add(rule.getVlanIndex());
                tmpList.add(rule.getTranslationNewVid());
                result.add(tmpList);
            }
        }
        logger.debug("transData:{}", result);
        writeDataToAjax(JSONArray.fromObject(result));
        return NONE;
    }

    /**
     * 添加转换规则
     * 
     * @return
     */
    @OperationLogProperty(actionName = "ponPortVlanAction", operationName = "addTransRule")
    public String addTransRule() throws Exception {
        VlanTranslationRule vlanTranslationRule = new VlanTranslationRule();
        vlanTranslationRule.setEntityId(entityId);
        vlanTranslationRule.setPortId(ponId);
        vlanTranslationRule.setVlanIndex(newBeforeTransVid);
        vlanTranslationRule.setTranslationNewVid(newAfterTransVid);
        ponPortVlanService.addTransRule(vlanTranslationRule);
        return NONE;
    }

    /**
     * 删除转换规则
     * 
     * @return
     */
    @OperationLogProperty(actionName = "ponPortVlanAction", operationName = "deleteTransRule")
    public String deleteTransRule() throws Exception {
        ponPortVlanService.deleteTransRule(entityId, ponId, oldBeforeTransVid);
        return NONE;
    }

    /**
     * 修改转换规则
     * 
     * @return
     */
    @OperationLogProperty(actionName = "ponPortVlanAction", operationName = "modifyTransRule")
    public String modifyTransRule() throws Exception {
        // 删除原有转换规则
        ponPortVlanService.deleteTransRule(entityId, ponId, oldBeforeTransVid);
        // 添加新的转换规则
        VlanTranslationRule transRule = new VlanTranslationRule();
        transRule.setEntityId(entityId);
        transRule.setPortId(ponId);
        transRule.setVlanIndex(newBeforeTransVid);
        transRule.setTranslationNewVid(newAfterTransVid);
        ponPortVlanService.addTransRule(transRule);
        return NONE;
    }

    /**
     * 获取聚合列表JSON数据
     * 
     * @return
     * @throws Exception
     */
    public String loadAggrData() throws Exception {
        List<VlanAggregationRule> aggrRuleList = ponPortVlanService.getAggrList(ponId);
        // 将数据库中获取的aggrRuleList数据转换为页面使用的aggrData列表数据
        List<List<List<Integer>>> result = new ArrayList<List<List<Integer>>>();
        if (aggrRuleList != null) {
            List<Integer> svid;
            List<Integer> cvids;
            List<List<Integer>> tmpList;
            for (VlanAggregationRule rule : aggrRuleList) {
                svid = new ArrayList<Integer>();
                cvids = new ArrayList<Integer>();
                tmpList = new ArrayList<List<Integer>>();
                svid.add(rule.getPortAggregationVidIndex());
                if (rule.getAggregationVidListAfterSwitch() != null) {
                    for (Integer cvidTmp : rule.getAggregationVidListAfterSwitch()) {
                        cvids.add(cvidTmp);
                    }
                }
                tmpList.add(svid);
                tmpList.add(cvids);
                result.add(tmpList);
            }
        }
        logger.debug("aggrData:{}", result);
        writeDataToAjax(JSONArray.fromObject(result));
        return NONE;
    }

    public String loadAggrRule() {
        List<VlanAggregationRule> aggrRuleList = ponPortVlanService.getAggrList(ponId);
    	writeDataToAjax(JSONArray.fromObject(aggrRuleList));
        return NONE;
    }

    /**
     * 添加SVLAN聚合规则
     * 
     * @return
     */
    @OperationLogProperty(actionName = "ponPortVlanAction", operationName = "addSvlanAggrRule")
    public String addSvlanAggrRule() throws Exception {
        List<Integer> cvidAggrList = new ArrayList<Integer>();
        if (!aggrCvids.equals("")) {
            String[] cvids = aggrCvids.split(",");
            for (String vid : cvids) {
                cvidAggrList.add(Integer.parseInt(vid));
            }
        }
        VlanAggregationRule vlanAggregationRule = new VlanAggregationRule();
        vlanAggregationRule.setEntityId(entityId);
        vlanAggregationRule.setPortId(ponId);
        vlanAggregationRule.setPortAggregationVidIndex(aggrSvid);
        vlanAggregationRule.setAggregationVidListAfterSwitch(cvidAggrList);
        ponPortVlanService.addSvlanAggrRule(vlanAggregationRule);
        return NONE;
    }

    /**
     * 删除SVLAN聚合规则
     * 
     * @return
     */
    @OperationLogProperty(actionName = "ponPortVlanAction", operationName = "deleteSvlanAggrRule")
    public String deleteSvlanAggrRule() throws Exception {
        ponPortVlanService.deleteSvlanAggrRule(entityId, ponId, aggrSvid);
        return NONE;
    }

    /**
     * 删除、添加CVLAN聚合规则（即修改聚合规则）
     * 
     * @return
     */
    @OperationLogProperty(actionName = "ponPortVlanAction", operationName = "modifyCvlanAggrRule")
    public String modifyCvlanAggrRule() throws Exception {
        List<Integer> cvidAggrList = new ArrayList<Integer>();
        if (!aggrCvids.equals("")) {
            String[] cvids = aggrCvids.split(",");
            for (String vid : cvids) {
                cvidAggrList.add(Integer.parseInt(vid));
            }
        }
        VlanAggregationRule vlanAggregationRule = new VlanAggregationRule();
        vlanAggregationRule.setEntityId(entityId);
        vlanAggregationRule.setPortId(ponId);
        vlanAggregationRule.setPortAggregationVidIndex(aggrSvid);
        vlanAggregationRule.setAggregationVidListAfterSwitch(cvidAggrList);
        ponPortVlanService.modifyCvlanAggrRule(vlanAggregationRule);
        return NONE;
    }

    /**
     * 获取Trunk列表JSON数据
     * 
     * @return
     * @throws Exception
     */
    public String loadTrunkData() throws Exception {
        VlanTrunkRule trunkRule = ponPortVlanService.getTrunkList(ponId);
        // 将数据库中获取的trunkRule数据转换为页面使用的trunkData列表数据
        List<List<Integer>> result = new ArrayList<List<Integer>>();
        List<Integer> tmpList;
        if (trunkRule != null && trunkRule.getTrunkVidListAfterSwitch() != null) {
            for (Integer vid : trunkRule.getTrunkVidListAfterSwitch()) {
                tmpList = new ArrayList<Integer>();
                tmpList.add(vid);
                result.add(tmpList);
            }
        }
        logger.debug("trunkData:{}", result);
        writeDataToAjax(JSONArray.fromObject(result));
        return NONE;
    }

    /**
     * 修改Trunk规则
     * 
     * @return
     */
    @OperationLogProperty(actionName = "ponPortVlanAction", operationName = "modifyTrunkRule")
    public String modifyTrunkRule() throws Exception {
        List<Integer> trunkVidList = new ArrayList<Integer>();
        if (!trunkVids.equals("")) {
            String[] vids = trunkVids.split(",");
            for (String vid : vids) {
                trunkVidList.add(Integer.parseInt(vid));
            }
        }
        VlanTrunkRule vlanTrunkRule = new VlanTrunkRule();
        vlanTrunkRule.setEntityId(entityId);
        vlanTrunkRule.setPortId(ponId);
        vlanTrunkRule.setTrunkVidListAfterSwitch(trunkVidList);
        ponPortVlanService.modifyTrunkRule(vlanTrunkRule);
        return NONE;
    }

    /**
     * 获取QinQ列表JSON数据
     * 
     * @return
     * @throws Exception
     */
    public String loadQinqData() throws Exception {
        List<VlanQinQRule> qinqRuleList = ponPortVlanService.getQinQList(ponId);
        // 将数据库中获取的qinqRuleList数据转换为页面使用的qinqData列表数据
        List<List<Integer>> svidList;
        List<Integer> svid;
        List<List<List<Integer>>> cvidList;
        List<List<Integer>> cvids;
        List<Integer> cvidTmp;
        List<List<List<List<Integer>>>> result = new ArrayList<List<List<List<Integer>>>>();
        if (qinqRuleList != null) {
            VlanQinQRule currentRule;
            VlanQinQRule nextRule;
            cvidList = new ArrayList<List<List<Integer>>>();
            cvids = new ArrayList<List<Integer>>();
            for (int i = 0; i < qinqRuleList.size(); i++) {
                svid = new ArrayList<Integer>();
                svidList = new ArrayList<List<Integer>>();

                currentRule = qinqRuleList.get(i);
                nextRule = (i + 1) < qinqRuleList.size() ? qinqRuleList.get(i + 1) : null;
                if (nextRule == null || !currentRule.getPqSVlanId().equals(nextRule.getPqSVlanId())) {
                    cvidTmp = new ArrayList<Integer>();
                    cvidTmp.add(currentRule.getPqStartVlanId());
                    cvidTmp.add(currentRule.getPqEndVlanId());
                    cvidTmp.add(currentRule.getPqSTagCosDetermine());
                    cvidTmp.add(currentRule.getPqSTagCosNewValue());
                    cvids.add(cvidTmp);

                    svid.add(currentRule.getPqSVlanId());
                    svidList.add(svid);
                    cvidList.add(svidList);
                    cvidList.add(cvids);
                    result.add(cvidList);
                    cvids = new ArrayList<List<Integer>>();
                    cvidList = new ArrayList<List<List<Integer>>>();
                } else {
                    cvidTmp = new ArrayList<Integer>();
                    cvidTmp.add(currentRule.getPqStartVlanId());
                    cvidTmp.add(currentRule.getPqEndVlanId());
                    cvidTmp.add(currentRule.getPqSTagCosDetermine());
                    cvidTmp.add(currentRule.getPqSTagCosNewValue());
                    cvids.add(cvidTmp);
                }
            }
        }
        logger.debug("qinqData:{}", result);
        writeDataToAjax(JSONArray.fromObject(result));
        return NONE;
    }

    public String loadQinqRule() {
        List<VlanQinQRule> qinqRuleList = ponPortVlanService.getQinQList(ponId);
    	writeDataToAjax(JSONArray.fromObject(qinqRuleList));
        return NONE;
    }

    /**
     * 添加QinQ规则
     * 
     * @return
     */
    @OperationLogProperty(actionName = "ponPortVlanAction", operationName = "addQinqRule")
    public String addQinqRule() throws Exception {
        VlanQinQRule vlanQinQRule = new VlanQinQRule();
        vlanQinQRule.setEntityId(entityId);
        vlanQinQRule.setPortId(ponId);
        vlanQinQRule.setPqSVlanId(qinqSvid);
        vlanQinQRule.setPqStartVlanId(qinqStartCvid);
        vlanQinQRule.setPqEndVlanId(qinqEndCvid);
        vlanQinQRule.setPqSTagCosDetermine(qinqSTagCosDetermine);
        vlanQinQRule.setPqSTagCosNewValue(qinqSTagCosNewValue);
        ponPortVlanService.addQinQRule(vlanQinQRule);
        return NONE;
    }

    /**
     * 删除QinQ规则
     * 
     * @return
     */
    @OperationLogProperty(actionName = "ponPortVlanAction", operationName = "deleteQinqRule")
    public String deleteQinqRule() throws Exception {
        ponPortVlanService.deleteQinQRule(entityId, ponId, qinqStartCvid, qinqEndCvid);
        return NONE;
    }

    /**
     * load transparent data
     * 
     * @return
     * @throws Exception
     */
    public String loadTransparentData() throws Exception {
        List<List<Integer>> re = new ArrayList<List<Integer>>();
        VlanTransparentRule v = ponPortVlanService.loadTransparentData(entityId, ponId);
        if (v != null && v.getTransparentIdList() != null && v.getTransparentModeList() != null) {
            if (v.getTransparentIdList().size() > 0) {
                for (Integer id : v.getTransparentIdList()) {
                    if (id > 0) {
                        List<Integer> tmp = new ArrayList<Integer>();
                        tmp.add(id);
                        tmp.add(v.getTransparentModeList().contains(id)
                                ? EponConstants.OLT_PONVLAN_TRANSPARENT_MODE_UNTAG
                                : EponConstants.OLT_PONVLAN_TRANSPARENT_MODE_TAG);
                        re.add(tmp);
                    }
                }
            }
        }
        if (re.size() > 0) {
            writeDataToAjax(JSONArray.fromObject(re));
        } else {
            writeDataToAjax(JSONArray.fromObject(false));
        }
        return NONE;
    }

    /**
     * 添加Transparent规则
     * 
     * @return
     * @throws Exception
     */
    @OperationLogProperty(actionName = "ponPortVlanAction", operationName = "addTransparentRule")
    public String addTransparentRule() throws Exception {
        VlanTransparentRule rule = getTheDomainForTransparent();
        ponPortVlanService.addTransparentRule(rule);
        return NONE;
    }

    /**
     * 删除Transparent规则
     * 
     * @return
     * @throws Exception
     */
    @OperationLogProperty(actionName = "ponPortVlanAction", operationName = "delTransparentRule")
    public String delTransparentRule() throws Exception {
        VlanTransparentRule rule = getTheDomainForTransparent();
        ponPortVlanService.delTransparentRule(rule);
        return NONE;
    }

    /**
     * 修改Transparent规则
     * 
     * @return
     * @throws Exception
     */
    @OperationLogProperty(actionName = "ponPortVlanAction", operationName = "modifyTransparentRule")
    public String modifyTransparentRule() throws Exception {
        VlanTransparentRule rule = getTheDomainForTransparent();
        ponPortVlanService.modifyTransparentRule(rule);
        return NONE;
    }

    /**
     * 从设备重新获取VLAN数据
     * 
     * @return
     */
    public String refreshVlanDataFromOlt() {
        ponPortVlanService.refreshVlanDataFromOlt(entityId);
        return NONE;
    }

    /**
     * 组装transparent的domain数据
     * 
     * @return
     * @throws Exception
     */
    private VlanTransparentRule getTheDomainForTransparent() throws Exception {
        VlanTransparentRule re = new VlanTransparentRule();
        re.setEntityId(entityId);
        re.setPortId(ponId);
        re.setPortIndex(oltPonService.getPonIndex(ponId));
        List<Integer> l1 = new ArrayList<Integer>();
        List<Integer> l2 = new ArrayList<Integer>();
        if (transparentIdStr != null && !transparentIdStr.equalsIgnoreCase("")) {
            String[] tmp1 = transparentIdStr.split(",");
            for (String s1 : tmp1) {
                Integer i = Integer.parseInt(s1);
                if (i < 4095 && i > 0) {
                    l1.add(i);
                }
            }
        }
        if (transparentModeStr != null && !transparentModeStr.equalsIgnoreCase("")) {
            String[] tmp2 = transparentModeStr.split(",");
            for (String s2 : tmp2) {
                Integer i = Integer.parseInt(s2);
                if (i < 4095 && i > 0) {
                    l2.add(i);
                }
            }
        }
        re.setTransparentIdList(l1);
        re.setTransparentModeList(l2);
        return re;
    }

    public Long getPonId() {
        return ponId;
    }

    public void setPonId(Long ponId) {
        this.ponId = ponId;
    }

    public Integer getNewBeforeTransVid() {
        return newBeforeTransVid;
    }

    public void setNewBeforeTransVid(Integer newBeforeTransVid) {
        this.newBeforeTransVid = newBeforeTransVid;
    }

    public Integer getNewAfterTransVid() {
        return newAfterTransVid;
    }

    public void setNewAfterTransVid(Integer newAfterTransVid) {
        this.newAfterTransVid = newAfterTransVid;
    }

    public Integer getOldBeforeTransVid() {
        return oldBeforeTransVid;
    }

    public void setOldBeforeTransVid(Integer oldBeforeTransVid) {
        this.oldBeforeTransVid = oldBeforeTransVid;
    }

    public String getAggrCvids() {
        return aggrCvids;
    }

    public void setAggrCvids(String aggrCvids) {
        this.aggrCvids = aggrCvids;
    }

    public Integer getAggrSvid() {
        return aggrSvid;
    }

    public void setAggrSvid(Integer aggrSvid) {
        this.aggrSvid = aggrSvid;
    }

    public String getTrunkVids() {
        return trunkVids;
    }

    public void setTrunkVids(String trunkVids) {
        this.trunkVids = trunkVids;
    }

    public Integer getQinqStartCvid() {
        return qinqStartCvid;
    }

    public void setQinqStartCvid(Integer qinqStartCvid) {
        this.qinqStartCvid = qinqStartCvid;
    }

    public Integer getQinqEndCvid() {
        return qinqEndCvid;
    }

    public void setQinqEndCvid(Integer qinqEndCvid) {
        this.qinqEndCvid = qinqEndCvid;
    }

    public Integer getQinqSvid() {
        return qinqSvid;
    }

    public void setQinqSvid(Integer qinqSvid) {
        this.qinqSvid = qinqSvid;
    }

    public Integer getQinqSTagCosDetermine() {
        return qinqSTagCosDetermine;
    }

    public void setQinqSTagCosDetermine(Integer qinqSTagCosDetermine) {
        this.qinqSTagCosDetermine = qinqSTagCosDetermine;
    }

    public Integer getQinqSTagCosNewValue() {
        return qinqSTagCosNewValue;
    }

    public void setQinqSTagCosNewValue(Integer qinqSTagCosNewValue) {
        this.qinqSTagCosNewValue = qinqSTagCosNewValue;
    }

    public String getTransparentIdStr() {
        return transparentIdStr;
    }

    public void setTransparentIdStr(String transparentIdStr) {
        this.transparentIdStr = transparentIdStr;
    }

    public String getTransparentModeStr() {
        return transparentModeStr;
    }

    public void setTransparentModeStr(String transparentModeStr) {
        this.transparentModeStr = transparentModeStr;
    }

    public Entity getEntity() {
        return entity;
    }

    public void setEntity(Entity entity) {
        this.entity = entity;
    }

    public String getTabNum() {
        return tabNum;
    }

    public void setTabNum(String tabNum) {
        this.tabNum = tabNum;
    }

    public String getTipNum() {
        return tipNum;
    }

    public void setTipNum(String tipNum) {
        this.tipNum = tipNum;
    }

    public Integer getPonPortType() {
        return ponPortType;
    }

    public void setPonPortType(Integer ponPortType) {
        this.ponPortType = ponPortType;
    }

    public boolean isIs8602G() {
        return is8602G;
    }

    public void setIs8602G(boolean is8602g) {
        is8602G = is8602g;
    }
 
}
