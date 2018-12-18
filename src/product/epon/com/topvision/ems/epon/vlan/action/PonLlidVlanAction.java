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

import com.topvision.ems.epon.olt.action.AbstractEponAction;
import com.topvision.ems.epon.vlan.domain.VlanLlidAggregationRule;
import com.topvision.ems.epon.vlan.domain.VlanLlidQinQRule;
import com.topvision.ems.epon.vlan.domain.VlanLlidTranslationRule;
import com.topvision.ems.epon.vlan.domain.VlanLlidTrunkRule;
import com.topvision.ems.epon.vlan.service.PonLlidVlanService;
import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.network.service.EntityService;
import com.topvision.framework.annotation.OperationLogProperty;
import com.topvision.framework.utils.EponConstants;

/**
 * @author flack
 * @created @2013-10-25-下午1:50:56
 *
 */
@Controller("ponLlidVlanAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class PonLlidVlanAction extends AbstractEponAction {
    private static final long serialVersionUID = 2853975103948082557L;
    private final Logger logger = LoggerFactory.getLogger(PonLlidVlanAction.class);

    private Long ponId;
    private String transMac;
    private String aggrMac;
    private String trunkMac;
    private String qinqMac;
    private Integer oldBeforeTransVid;
    private Integer newBeforeTransVid;
    private Integer newAfterTransVid;
    private Integer cosMode;
    private Integer cosValue;
    private String tpid;
    private String aggrCvids;
    private Integer aggrSvid;
    private String trunkVids;
    private Integer qinqStartCvid;
    private Integer qinqEndCvid;
    private Integer qinqSvid;
    private String vlanLlidTpidValue;
    private String vlanLlidTpidDefault;
    private Entity entity;
    private String tabNum;
    private String tipNum;
    private String currentId;
    private String treenodeid;

    @Autowired
    private EntityService entityService;
    @Autowired
    private PonLlidVlanService ponLlidVlanService;

    /**
     * 获取当前PON口下所有在线的ONU MAC地址
     * 
     * @return
     * @throws Exception
     */
    public String loadOnuMacAddress() throws Exception {
        List<String> onuMacAddressList = ponLlidVlanService.getOnuMacAddress(ponId);
        logger.debug("onuMacAddressList:{}", onuMacAddressList);
        writeDataToAjax(JSONArray.fromObject(onuMacAddressList));
        return NONE;
    }

    /**
     * 显示PON口LLID方式的VLAN配置管理
     * 
     * @return String
     */
    public String showPonVlanLlidConfig() {
        entity = entityService.getEntity(entityId);
        vlanLlidTpidDefault = EponConstants.EPON_LLID_VLAN_TPID_DEFAULT;
        vlanLlidTpidValue = EponConstants.EPON_LLID_VLAN_TPID_VALUE.toString();
        return SUCCESS;
    }

    /**
     * 获取转换列表JSON数据（LLID）
     * 
     * @return
     * @throws Exception
     */
    public String loadLlidTransData() throws Exception {
        List<VlanLlidTranslationRule> llidTransRuleList = ponLlidVlanService.getLlidTransList(ponId, transMac);
        // 将数据库中获取的llidTransRuleList数据转换为页面使用的transData列表数据
        List<List<Object>> result = new ArrayList<List<Object>>();
        if (llidTransRuleList != null) {
            List<Object> tmpList;
            for (VlanLlidTranslationRule rule : llidTransRuleList) {
                tmpList = new ArrayList<Object>();
                tmpList.add(rule.getTopLlidTransVidIdx());
                tmpList.add(rule.getTopLlidTransNewVid());
                if (rule.getTopLlidTransCosMode() == 2) {
                    tmpList.add(8);
                } else {
                    tmpList.add(rule.getTopLlidTransNewCos());
                }
                tmpList.add(Integer.toHexString(rule.getTopLlidTransNewTpid()));
                result.add(tmpList);
            }
        }
        logger.debug("transData:{}", result);
        writeDataToAjax(JSONArray.fromObject(result));
        return NONE;
    }

    /**
     * 添加转换规则（LLID）
     * 
     * @return
     */
    @OperationLogProperty(actionName = "ponLlidVlanAction", operationName = "addLlidTransRule")
    public String addLlidTransRule() throws Exception {
        VlanLlidTranslationRule vlanLlidTranslationRule = new VlanLlidTranslationRule();
        vlanLlidTranslationRule.setEntityId(entityId);
        vlanLlidTranslationRule.setPortId(ponId);
        vlanLlidTranslationRule.setOnuMacString(transMac);
        vlanLlidTranslationRule.setTopLlidTransVidIdx(newBeforeTransVid);
        vlanLlidTranslationRule.setTopLlidTransNewVid(newAfterTransVid);
        vlanLlidTranslationRule.setTopLlidTransCosMode(cosMode);
        vlanLlidTranslationRule.setTopLlidTransNewCos(cosValue);
        Integer tpidInt = Integer.parseInt(EponConstants.EPON_LLID_VLAN_TPID_DEFAULT, 16);
        if (EponConstants.EPON_LLID_VLAN_TPID_VALUE.contains(tpid.toLowerCase())) {
            tpidInt = Integer.parseInt(tpid, 16);
        }
        vlanLlidTranslationRule.setTopLlidTransNewTpid(tpidInt);
        ponLlidVlanService.addLlidTransRule(vlanLlidTranslationRule);
        return NONE;
    }

    /**
     * 删除转换规则
     * 
     * @return
     */
    @OperationLogProperty(actionName = "ponLlidVlanAction", operationName = "deleteLlidTransRule")
    public String deleteLlidTransRule() throws Exception {
        ponLlidVlanService.deleteLlidTransRule(entityId, ponId, transMac, oldBeforeTransVid);
        return NONE;
    }

    /**
     * 修改转换规则
     * 
     * @return
     */
    @OperationLogProperty(actionName = "ponLlidVlanAction", operationName = "modifyLlidTransRule")
    public String modifyLlidTransRule() throws Exception {
        // 删除原有转换规则
        ponLlidVlanService.deleteLlidTransRule(entityId, ponId, transMac, oldBeforeTransVid);
        // 添加新的转换规则
        VlanLlidTranslationRule vlanLlidTranslationRule = new VlanLlidTranslationRule();
        vlanLlidTranslationRule.setEntityId(entityId);
        vlanLlidTranslationRule.setPortId(ponId);
        vlanLlidTranslationRule.setOnuMacString(transMac);
        vlanLlidTranslationRule.setTopLlidTransVidIdx(newBeforeTransVid);
        vlanLlidTranslationRule.setTopLlidTransNewVid(newAfterTransVid);
        vlanLlidTranslationRule.setTopLlidTransCosMode(cosMode);
        vlanLlidTranslationRule.setTopLlidTransNewCos(cosValue);
        Integer tpidInt = Integer.parseInt(EponConstants.EPON_LLID_VLAN_TPID_DEFAULT, 16);
        if (EponConstants.EPON_LLID_VLAN_TPID_VALUE.contains(tpid.toLowerCase())) {
            tpidInt = Integer.parseInt(tpid, 16);
        }
        vlanLlidTranslationRule.setTopLlidTransNewTpid(tpidInt);
        ponLlidVlanService.addLlidTransRule(vlanLlidTranslationRule);
        return NONE;
    }

    /**
     * 获取聚合列表JSON数据(LLID)
     * 
     * @return
     * @throws Exception
     */
    public String loadLlidAggrData() throws Exception {
        List<VlanLlidAggregationRule> llidAggrRuleList = ponLlidVlanService.getLlidAggrList(ponId, aggrMac);
        // 将数据库中获取的llidAggrRuleList数据转换为页面使用的aggrData列表数据
        List<List<List<Object>>> result = new ArrayList<List<List<Object>>>();
        if (llidAggrRuleList != null) {
            List<Object> svid;
            List<Object> cvids;
            List<List<Object>> tmpList;
            for (VlanLlidAggregationRule rule : llidAggrRuleList) {
                svid = new ArrayList<Object>();
                cvids = new ArrayList<Object>();
                tmpList = new ArrayList<List<Object>>();
                svid.add(rule.getLlidVlanAfterAggVid());
                svid.add(rule.getLlidVlanAggCosMode());
                svid.add(rule.getLlidVlanAggNewCos());
                svid.add(Integer.toHexString(rule.getLlidVlanAggNewTpid()));
                if (rule.getLlidVlanBeforeAggVidListAfterSwitch() != null) {
                    for (Integer cvidTmp : rule.getLlidVlanBeforeAggVidListAfterSwitch()) {
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

    /**
     * 添加SVLAN聚合规则(LLID)
     * 
     * @return
     */
    @OperationLogProperty(actionName = "ponLlidVlanAction", operationName = "addLlidSvlanAggrRule")
    public String addLlidSvlanAggrRule() throws Exception {
        List<Integer> cvidAggrList = new ArrayList<Integer>();
        if (!aggrCvids.equals("")) {
            String[] cvids = aggrCvids.split(",");
            for (String vid : cvids) {
                cvidAggrList.add(Integer.parseInt(vid));
            }
        }
        VlanLlidAggregationRule vlanLlidAggregationRule = new VlanLlidAggregationRule();
        vlanLlidAggregationRule.setEntityId(entityId);
        vlanLlidAggregationRule.setPortId(ponId);
        vlanLlidAggregationRule.setOnuMacString(aggrMac);
        vlanLlidAggregationRule.setLlidVlanAfterAggVid(aggrSvid);
        vlanLlidAggregationRule.setLlidVlanAggCosMode(cosMode);
        vlanLlidAggregationRule.setLlidVlanAggNewCos(cosValue);
        Integer tpidInt = Integer.parseInt(EponConstants.EPON_LLID_VLAN_TPID_DEFAULT, 16);
        if (EponConstants.EPON_LLID_VLAN_TPID_VALUE.contains(tpid.toLowerCase())) {
            tpidInt = Integer.parseInt(tpid, 16);
        }
        vlanLlidAggregationRule.setLlidVlanAggNewTpid(tpidInt);
        vlanLlidAggregationRule.setLlidVlanBeforeAggVidListAfterSwitch(cvidAggrList);
        ponLlidVlanService.addLlidSvlanAggrRule(vlanLlidAggregationRule);
        return NONE;
    }

    /**
     * 删除SVLAN聚合规则(LLID)
     * 
     * @return
     */
    @OperationLogProperty(actionName = "ponLlidVlanAction", operationName = "deleteLlidSvlanAggrRule")
    public String deleteLlidSvlanAggrRule() throws Exception {
        ponLlidVlanService.deleteLlidSvlanAggrRule(entityId, ponId, aggrMac, aggrSvid);
        return NONE;
    }

    /**
     * 删除、添加CVLAN聚合规则（即修改聚合规则）(LLID)
     * 
     * @return
     */
    @OperationLogProperty(actionName = "ponLlidVlanAction", operationName = "modifyLlidCvlanAggrRule")
    public String modifyLlidCvlanAggrRule() throws Exception {
        List<Integer> cvidAggrList = new ArrayList<Integer>();
        if (!aggrCvids.equals("")) {
            String[] cvids = aggrCvids.split(",");
            for (String vid : cvids) {
                cvidAggrList.add(Integer.parseInt(vid));
            }
        }
        VlanLlidAggregationRule vlanLlidAggregationRule = new VlanLlidAggregationRule();
        vlanLlidAggregationRule.setEntityId(entityId);
        vlanLlidAggregationRule.setPortId(ponId);
        vlanLlidAggregationRule.setOnuMacString(aggrMac);
        vlanLlidAggregationRule.setLlidVlanAfterAggVid(aggrSvid);
        if (cosMode != null) {
            vlanLlidAggregationRule.setLlidVlanAggCosMode(cosMode);
        }
        if (cosValue != null) {
            vlanLlidAggregationRule.setLlidVlanAggNewCos(cosValue);
        }
        if (tpid != null) {
            Integer tpidInt = Integer.parseInt(EponConstants.EPON_LLID_VLAN_TPID_DEFAULT, 16);
            if (EponConstants.EPON_LLID_VLAN_TPID_VALUE.contains(tpid.toLowerCase())) {
                tpidInt = Integer.parseInt(tpid, 16);
            }
            vlanLlidAggregationRule.setLlidVlanAggNewTpid(tpidInt);
        }
        //vlanLlidAggregationRule.setOnuHexMac(aggrMac);
        vlanLlidAggregationRule.setLlidVlanBeforeAggVidListAfterSwitch(cvidAggrList);
        if (vlanLlidAggregationRule.getLlidVlanBeforeAggVidListAfterSwitch().size() == 0) {
            ponLlidVlanService.deleteLlidSvlanAggrRule(entityId, ponId, aggrMac, aggrSvid);
        } else {
            ponLlidVlanService.modifyLlidCvlanAggrRule(vlanLlidAggregationRule);
        }
        return NONE;
    }

    /**
     * 获取Trunk列表JSON数据(LLID)
     * 
     * @return
     * @throws Exception
     */
    public String loadLlidTrunkData() throws Exception {
        VlanLlidTrunkRule vlanLlidTrunkRule = ponLlidVlanService.getLlidTrunkList(ponId, trunkMac);
        // 将数据库中获取的trunkRule数据转换为页面使用的trunkData列表数据
        List<List<Integer>> result = new ArrayList<List<Integer>>();
        List<Integer> tmpList;
        if (vlanLlidTrunkRule != null && vlanLlidTrunkRule.getLlidVlanTrunkVidBmpAfterSwitch() != null) {
            for (Integer vid : vlanLlidTrunkRule.getLlidVlanTrunkVidBmpAfterSwitch()) {
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
    @OperationLogProperty(actionName = "ponLlidVlanAction", operationName = "modifyLlidTrunkRule")
    public String modifyLlidTrunkRule() throws Exception {
        List<Integer> trunkVidList = new ArrayList<Integer>();
        if (!trunkVids.equals("")) {
            String[] vids = trunkVids.split(",");
            for (String vid : vids) {
                trunkVidList.add(Integer.parseInt(vid));
            }
        }
        VlanLlidTrunkRule vlanLlidTrunkRule = new VlanLlidTrunkRule();
        vlanLlidTrunkRule.setEntityId(entityId);
        vlanLlidTrunkRule.setPortId(ponId);
        vlanLlidTrunkRule.setOnuMacString(trunkMac);
        vlanLlidTrunkRule.setLlidVlanTrunkVidBmpAfterSwitch(trunkVidList);
        ponLlidVlanService.modifyLlidTrunkRule(vlanLlidTrunkRule);
        return NONE;
    }

    /**
     * 获取QinQ列表JSON数据(LLID)
     * 
     * @return
     * @throws Exception
     */
    public String loadLlidQinqData() throws Exception {
        List<VlanLlidQinQRule> llidQinQRule = ponLlidVlanService.getLlidQinQList(ponId, qinqMac);
        // 将数据库中获取的llidQinQRuleList数据转换为页面使用的qinqData列表数据
        List<List<Object>> svidList;
        List<Object> svid;
        List<List<List<Object>>> cvidList;
        List<List<Object>> cvids;
        List<Object> cvidTmp;
        List<List<List<List<Object>>>> result = new ArrayList<List<List<List<Object>>>>();
        if (llidQinQRule != null) {
            VlanLlidQinQRule currentRule;
            VlanLlidQinQRule nextRule;
            cvidList = new ArrayList<List<List<Object>>>();
            cvids = new ArrayList<List<Object>>();
            for (int i = 0; i < llidQinQRule.size(); i++) {
                svid = new ArrayList<Object>();
                svidList = new ArrayList<List<Object>>();

                currentRule = llidQinQRule.get(i);
                nextRule = (i + 1) < llidQinQRule.size() ? llidQinQRule.get(i + 1) : null;
                if (nextRule == null || !currentRule.getTopLqVlanSVlan().equals(nextRule.getTopLqVlanSVlan())) {
                    cvidTmp = new ArrayList<Object>();
                    cvidTmp.add(currentRule.getTopLqVlanStartCVid());
                    cvidTmp.add(currentRule.getTopLqVlanEndCVid());
                    cvidTmp.add(currentRule.getTopLqVlanCosMode());
                    cvidTmp.add(currentRule.getTopLqVlanSCos());
                    cvidTmp.add(Integer.toHexString(currentRule.getTopLqVlanOuterTpid()));
                    cvids.add(cvidTmp);

                    svid.add(currentRule.getTopLqVlanSVlan());
                    svidList.add(svid);
                    cvidList.add(svidList);
                    cvidList.add(cvids);
                    result.add(cvidList);
                    cvids = new ArrayList<List<Object>>();
                    cvidList = new ArrayList<List<List<Object>>>();
                } else {
                    cvidTmp = new ArrayList<Object>();
                    cvidTmp.add(currentRule.getTopLqVlanStartCVid());
                    cvidTmp.add(currentRule.getTopLqVlanEndCVid());
                    cvidTmp.add(currentRule.getTopLqVlanCosMode());
                    cvidTmp.add(currentRule.getTopLqVlanSCos());
                    cvidTmp.add(Integer.toHexString(currentRule.getTopLqVlanOuterTpid()));
                    cvids.add(cvidTmp);
                }
            }
        }

        logger.debug("qinqData:{}", result);
        writeDataToAjax(JSONArray.fromObject(result));
        return NONE;
    }

    /**
     * 添加QinQ规则(LLID)
     * 
     * @return
     */
    @OperationLogProperty(actionName = "ponLlidVlanAction", operationName = "addLlidQinqRule")
    public String addLlidQinqRule() throws Exception {
        VlanLlidQinQRule vlanLlidQinQRule = new VlanLlidQinQRule();
        vlanLlidQinQRule.setEntityId(entityId);
        vlanLlidQinQRule.setPortId(ponId);
        vlanLlidQinQRule.setOnuMacString(qinqMac);
        vlanLlidQinQRule.setTopLqVlanSVlan(qinqSvid);
        vlanLlidQinQRule.setTopLqVlanStartCVid(qinqStartCvid);
        vlanLlidQinQRule.setTopLqVlanEndCVid(qinqEndCvid);
        vlanLlidQinQRule.setTopLqVlanCosMode(cosMode);
        vlanLlidQinQRule.setTopLqVlanSCos(cosValue);
        Integer tpidInt = Integer.parseInt(EponConstants.EPON_LLID_VLAN_TPID_DEFAULT, 16);
        if (EponConstants.EPON_LLID_VLAN_TPID_VALUE.contains(tpid.toLowerCase())) {
            tpidInt = Integer.parseInt(tpid, 16);
        }
        vlanLlidQinQRule.setTopLqVlanOuterTpid(tpidInt);
        ponLlidVlanService.addLlidQinQRule(vlanLlidQinQRule);
        return NONE;
    }

    /**
     * 删除QinQ规则(LLID)
     * 
     * @return
     */
    @OperationLogProperty(actionName = "ponLlidVlanAction", operationName = "deleteLlidQinqRule")
    public String deleteLlidQinqRule() throws Exception {
        ponLlidVlanService.deleteLlidQinQRule(entityId, ponId, qinqMac, qinqStartCvid, qinqEndCvid);
        return NONE;
    }

    /**
     * 从设备重新获取VLAN的LLID列表数据
     * 
     * @return
     */
    public String refreshVlanLlidListFromOlt() {
        ponLlidVlanService.refreshVlanLlidListFromOlt(entityId, ponId);
        return NONE;
    }

    public Long getPonId() {
        return ponId;
    }

    public void setPonId(Long ponId) {
        this.ponId = ponId;
    }

    public String getTransMac() {
        return transMac;
    }

    public void setTransMac(String transMac) {
        this.transMac = transMac;
    }

    public String getAggrMac() {
        return aggrMac;
    }

    public void setAggrMac(String aggrMac) {
        this.aggrMac = aggrMac;
    }

    public String getTrunkMac() {
        return trunkMac;
    }

    public void setTrunkMac(String trunkMac) {
        this.trunkMac = trunkMac;
    }

    public String getQinqMac() {
        return qinqMac;
    }

    public void setQinqMac(String qinqMac) {
        this.qinqMac = qinqMac;
    }

    public Integer getOldBeforeTransVid() {
        return oldBeforeTransVid;
    }

    public void setOldBeforeTransVid(Integer oldBeforeTransVid) {
        this.oldBeforeTransVid = oldBeforeTransVid;
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

    public Integer getCosMode() {
        return cosMode;
    }

    public void setCosMode(Integer cosMode) {
        this.cosMode = cosMode;
    }

    public Integer getCosValue() {
        return cosValue;
    }

    public void setCosValue(Integer cosValue) {
        this.cosValue = cosValue;
    }

    public String getTpid() {
        return tpid;
    }

    public void setTpid(String tpid) {
        this.tpid = tpid;
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

    public String getVlanLlidTpidValue() {
        return vlanLlidTpidValue;
    }

    public void setVlanLlidTpidValue(String vlanLlidTpidValue) {
        this.vlanLlidTpidValue = vlanLlidTpidValue;
    }

    public String getVlanLlidTpidDefault() {
        return vlanLlidTpidDefault;
    }

    public void setVlanLlidTpidDefault(String vlanLlidTpidDefault) {
        this.vlanLlidTpidDefault = vlanLlidTpidDefault;
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

    @Override
    public String getCurrentId() {
        return currentId;
    }

    @Override
    public void setCurrentId(String currentId) {
        this.currentId = currentId;
    }

	public String getTreenodeid() {
		return treenodeid;
	}

	public void setTreenodeid(String treenodeid) {
		this.treenodeid = treenodeid;
	}

    public String getTipNum() {
        return tipNum;
    }

    public void setTipNum(String tipNum) {
        this.tipNum = tipNum;
    }

}