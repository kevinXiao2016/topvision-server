/***********************************************************************
 * $Id: UniPortVlanAction.java,v1.0 2016-2-29 下午3:33:33 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.univlanprofile.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.topvision.ems.epon.univlanprofile.domain.UniVlanBindTable;
import com.topvision.ems.epon.univlanprofile.domain.UniVlanProfileTable;
import com.topvision.ems.epon.univlanprofile.service.UniVlanProfileService;
import com.topvision.ems.epon.vlan.domain.PortVlanAttribute;
import com.topvision.ems.epon.vlan.domain.VlanAggregationRule;
import com.topvision.ems.epon.vlan.domain.VlanTranslationRule;
import com.topvision.ems.epon.vlan.domain.VlanTrunkRule;
import com.topvision.ems.epon.vlan.service.UniVlanService;
import com.topvision.framework.annotation.OperationLogProperty;
import com.topvision.framework.exception.engine.SnmpSetException;
import com.topvision.framework.utils.EponConstants;
import com.topvision.framework.web.struts2.BaseAction;
import com.topvision.platform.domain.OperationLog;

/**
 * @author flack
 * @created @2016-2-29-下午3:33:33 UNI端口VLAN业务
 */
@Controller("uniPortVlanAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class UniPortVlanAction extends BaseAction {
    private static final long serialVersionUID = -1264520915710737896L;
    private final Logger logger = LoggerFactory.getLogger(UniPortVlanAction.class);

    private Long entityId;
    private Long onuId;
    private Long uniIndex;
    private Long uniId;
    private Integer uniPvid;
    private Integer vlanMode;
    private Integer vlanIndex;
    private Integer translationNewVid;
    private String trunkList;
    private String aggrCvids;
    private Integer aggrSvid;
    // 用以记录配置日志
    protected Integer operationResult;

    private List<VlanTranslationRule> vlanTranslationRuleList;
    private List<VlanAggregationRule> vlanAggregationRuleList;
    private PortVlanAttribute uniPortVlan;
    private UniVlanBindTable uniBindInfo;
    private UniVlanProfileTable uniVlanProfile;

    @Autowired
    private UniVlanService uniVlanService;
    @Autowired
    private UniVlanProfileService uniVlanProfileService;

    /**
     * 显示UNI端口VLAN信息 根据不同的VLNA模式展示不同的页面
     * 
     * @return
     */
    public String showUniVlanView() {
        // 获取UNI端口VLAN信息
        uniPortVlan = uniVlanService.getUniVlanAttribute(uniId);
        // 获取关联模板信息
        uniBindInfo = uniVlanProfileService.getUniBindInfo(uniId, entityId);
        if (uniBindInfo != null && uniBindInfo.getBindProfileId() > EponConstants.UNIVALN_UNBIND_PROFILEID) {
            uniVlanProfile = uniVlanProfileService.getUniVlanProfile(entityId, uniBindInfo.getBindProfileId());
        } else {
            uniVlanProfile = new UniVlanProfileTable();
        }
        if (uniPortVlan.getVlanMode() != null) {
            switch (uniPortVlan.getVlanMode()) {
            case 0:
            case 1:
                return EponConstants.VLAN_MODE_TAG;
            case 2:
                return EponConstants.VLAN_MODE_TRANSLATION;
            case 3:
                return EponConstants.VLAN_MODE_AGGREGATION;
            case 4:
                return EponConstants.VLAN_MODE_TRUNK;
            case 5:
                return EponConstants.VLAN_MODE_STACKING;
            default:
                return INPUT;
            }
        } else {
            return INPUT;
        }
    }

    /**
     * 切换VLAN模式 可同时配置PVID
     * 
     * @return
     * @throws Exception
     */
    @OperationLogProperty(actionName = "uniPortVlanAction", operationName = "modifyVlanMode")
    public String modifyVlanMode() throws Exception {
        PortVlanAttribute uniVlan = new PortVlanAttribute();
        uniVlan.setEntityId(entityId);
        uniVlan.setPortIndex(uniIndex);
        uniVlan.setPortId(uniId);
        uniVlan.setVlanPVid(uniPvid);
        uniVlan.setVlanMode(vlanMode);
        // 记录操作结果
        JSONObject resultJson = new JSONObject();
        try {
            uniVlanService.updateUniVlanAttribute(uniVlan);
            resultJson.put("success", true);
            operationResult = OperationLog.SUCCESS;
        } catch (SnmpSetException e) {
            resultJson.put("success", false);
            resultJson.put("message", e.getMessage());
            operationResult = OperationLog.FAILURE;
            logger.error("Modify Uni[{}] Vlan mode failed : {}", uniId, e);
        }
        writeDataToAjax(resultJson);
        return NONE;
    }

    /**
     * 从设备获取UNI VLAN数据
     * 
     * @return
     * @throws Exception
     */
    public String refreshUniVlanData() throws Exception {
        // 刷新UNI VLAN基本属性
        uniVlanService.refreshSingleUniVlanAttribute(entityId, uniId);
        // 还需要刷新UNI VLAN模板绑定信息
        uniVlanProfileService.refreshUniBindInfo(entityId, uniIndex);
        return NONE;
    }

    /**
     * 加载转换规则数据
     * 
     * @return
     * @throws Exception
     */
    public String loadTranslationRuleList() throws Exception {
        // 将数据库中获取的transRuleList数据
        vlanTranslationRuleList = uniVlanService.getVlanTranslationRuleList(uniId);
        JSONArray translateArray = JSONArray.fromObject(vlanTranslationRuleList);
        translateArray.write(response.getWriter());
        return NONE;
    }

    /**
     * 添加转换规则
     * 
     * @return
     * @throws IOException
     */
    @OperationLogProperty(actionName = "uniPortVlanAction", operationName = "addTranslateRule")
    public String addTranslateRule() throws IOException {
        VlanTranslationRule vlanTranslationRule = new VlanTranslationRule();
        vlanTranslationRule.setEntityId(entityId);
        vlanTranslationRule.setVlanIndex(vlanIndex);
        vlanTranslationRule.setTranslationNewVid(translationNewVid);
        vlanTranslationRule.setPortId(uniId);
        // 记录操作结果
        JSONObject resultJson = new JSONObject();
        try {
            uniVlanService.addVlanTranslationRule(vlanTranslationRule);
            resultJson.put("success", true);
            operationResult = OperationLog.SUCCESS;
        } catch (SnmpSetException e) {
            resultJson.put("success", false);
            resultJson.put("message", e.getMessage());
            operationResult = OperationLog.FAILURE;
            logger.error("Add Uni[{}] Translate Rule failed : {}", uniId, e);
        }
        writeDataToAjax(resultJson);
        return NONE;
    }

    /**
     * 删除转换规则数据
     * 
     * @return
     * @throws Exception
     */
    @OperationLogProperty(actionName = "uniPortVlanAction", operationName = "deleteTranslationRule")
    public String deleteTranslationRule() throws Exception {
        VlanTranslationRule vlanTranslationRule = new VlanTranslationRule();
        vlanTranslationRule.setEntityId(entityId);
        vlanTranslationRule.setVlanIndex(vlanIndex);
        vlanTranslationRule.setPortIndex(uniIndex);
        vlanTranslationRule.setPortId(uniId);
        // 记录操作结果
        JSONObject resultJson = new JSONObject();
        try {
            uniVlanService.deleteVlanTranslationRule(vlanTranslationRule);
            resultJson.put("success", true);
            operationResult = OperationLog.SUCCESS;
        } catch (SnmpSetException e) {
            resultJson.put("success", false);
            resultJson.put("message", e.getMessage());
            operationResult = OperationLog.FAILURE;
            logger.error("Delete Uni[{}] Translation Rule failed : {}", uniId, e);
        }
        writeDataToAjax(resultJson);
        return NONE;
    }

    /**
     * 加载UNI端口VLAN聚合规则列表
     * 
     * @return String
     * @throws Exception
     */
    public String loadAggregationRule() throws Exception {
        vlanAggregationRuleList = uniVlanService.getVlanAggregationRuleList(uniId);
        JSONArray aggArray = JSONArray.fromObject(vlanAggregationRuleList);
        aggArray.write(response.getWriter());
        return NONE;
    }

    /**
     * 添加聚合规则
     * 
     * @return
     * @throws IOException
     */
    @OperationLogProperty(actionName = "uniPortVlanAction", operationName = "addAggregationRule")
    public String addAggregationRule() throws IOException {
        String[] cvids = aggrCvids.split(",");
        List<Integer> cvidAggrList = new ArrayList<Integer>();
        for (String vid : cvids) {
            cvidAggrList.add(Integer.parseInt(vid));
        }
        VlanAggregationRule vlanAggregationRule = new VlanAggregationRule();
        vlanAggregationRule.setEntityId(entityId);
        vlanAggregationRule.setPortId(uniId);
        vlanAggregationRule.setPortIndex(uniIndex);
        vlanAggregationRule.setPortAggregationVidIndex(aggrSvid);
        vlanAggregationRule.setAggregationVidListAfterSwitch(cvidAggrList);
        // 记录操作结果
        JSONObject resultJson = new JSONObject();
        try {
            uniVlanService.addSVlanAggregationRule(vlanAggregationRule);
            resultJson.put("success", true);
            operationResult = OperationLog.SUCCESS;
        } catch (SnmpSetException e) {
            resultJson.put("success", false);
            resultJson.put("message", e.getMessage());
            operationResult = OperationLog.FAILURE;
            logger.error("Add Uni[{}] Aggregation Rule failed : {}", uniId, e);
        }
        writeDataToAjax(resultJson);
        return NONE;
    }

    /**
     * 修改聚合规则
     * 
     * @return
     * @throws Exception
     */
    @OperationLogProperty(actionName = "uniPortVlanAction", operationName = "modifyAggregationRule")
    public String modifyAggregationRule() throws Exception {
        List<Integer> cvidAggrList = new ArrayList<Integer>();
        if (!aggrCvids.equals("")) {
            String[] cvids = aggrCvids.split(",");
            for (String vid : cvids) {
                cvidAggrList.add(Integer.parseInt(vid));
            }
        }
        VlanAggregationRule deleteRule = new VlanAggregationRule();
        VlanAggregationRule addRule = new VlanAggregationRule();
        addRule.setEntityId(entityId);
        addRule.setPortId(uniId);
        addRule.setPortIndex(uniIndex);
        addRule.setPortAggregationVidIndex(aggrSvid);
        addRule.setAggregationVidListAfterSwitch(cvidAggrList);
        deleteRule.setEntityId(entityId);
        deleteRule.setPortId(uniId);
        deleteRule.setPortIndex(uniIndex);
        deleteRule.setPortAggregationVidIndex(aggrSvid);
        deleteRule.setAggregationVidListAfterSwitch(cvidAggrList);
        uniVlanService.deleteSVlanAggregationRule(deleteRule);
        uniVlanService.addSVlanAggregationRule(addRule);
        // 记录操作结果
        JSONObject resultJson = new JSONObject();
        try {
            // 调用先删除后添加实现修改
            uniVlanService.deleteSVlanAggregationRule(deleteRule);
            uniVlanService.addSVlanAggregationRule(addRule);
            resultJson.put("success", true);
            operationResult = OperationLog.SUCCESS;
        } catch (SnmpSetException e) {
            resultJson.put("success", false);
            resultJson.put("message", e.getMessage());
            operationResult = OperationLog.FAILURE;
            logger.error("Modify Uni[{}] Aggregation Rule failed : {}", uniId, e);
        }
        writeDataToAjax(resultJson);
        return NONE;
    }

    /**
     * 删除聚合规则
     * 
     * @return
     * @throws Exception
     */
    @OperationLogProperty(actionName = "uniPortVlanAction", operationName = "deleteAggregationRule")
    public String deleteAggregationRule() throws Exception {
        VlanAggregationRule vlanAggregationRule = new VlanAggregationRule();
        vlanAggregationRule.setEntityId(entityId);
        vlanAggregationRule.setPortIndex(uniIndex);
        vlanAggregationRule.setPortAggregationVidIndex(aggrSvid);
        vlanAggregationRule.setPortId(uniId);
        // 记录操作结果
        JSONObject resultJson = new JSONObject();
        try {
            uniVlanService.deleteSVlanAggregationRule(vlanAggregationRule);
            resultJson.put("success", true);
            operationResult = OperationLog.SUCCESS;
        } catch (SnmpSetException e) {
            resultJson.put("success", false);
            resultJson.put("message", e.getMessage());
            operationResult = OperationLog.FAILURE;
            logger.error("Delete Uni[{}] Aggregation Rule failed : {}", uniId, e);
        }
        writeDataToAjax(resultJson);
        return NONE;
    }

    /**
     * 加载UNI端口VLAN Trunk规则列表
     * 
     * @return String
     * @throws Exception
     */
    public String loadTrunkRuleList() throws Exception {
        // 获得数据库中保存的trunk规则
        VlanTrunkRule trunkRule = uniVlanService.getVlanTrunkRules(uniId);
        JSONArray trunkArray = new JSONArray();
        if (trunkRule != null && trunkRule.getTrunkVidListAfterSwitch() != null) {
            for (Integer trunkId : trunkRule.getTrunkVidListAfterSwitch()) {
                JSONObject trunkJson = new JSONObject();
                trunkJson.put("trunkId", trunkId);
                trunkArray.add(trunkJson);
            }
        }
        trunkArray.write(response.getWriter());
        return NONE;
    }

    /**
     * 添加Trunk规则
     * 
     * @return
     * @throws IOException
     */
    @OperationLogProperty(actionName = "uniPortVlanAction", operationName = "addTrunkRule")
    public String addTrunkRule() throws IOException {
        List<Integer> trunkVidList = new ArrayList<Integer>();
        VlanTrunkRule vlanTrunkRule = new VlanTrunkRule();
        vlanTrunkRule.setEntityId(entityId);
        vlanTrunkRule.setPortId(uniId);
        vlanTrunkRule.setPortIndex(uniIndex);
        if (trunkList != null && !"".equals(trunkList)) {
            String[] vids = trunkList.split(",");
            for (String vid : vids) {
                trunkVidList.add(Integer.parseInt(vid));
            }
            vlanTrunkRule.setTrunkVidListAfterSwitch(trunkVidList);
        } else {
            vlanTrunkRule.setTrunkVidListAfterSwitch(null);
        }
        // 记录操作结果
        JSONObject resultJson = new JSONObject();
        try {
            uniVlanService.updateUniVlanTrunkRule(vlanTrunkRule);
            resultJson.put("success", true);
            operationResult = OperationLog.SUCCESS;
        } catch (SnmpSetException e) {
            resultJson.put("success", false);
            resultJson.put("message", e.getMessage());
            operationResult = OperationLog.FAILURE;
            logger.error("Add Uni[{}] Trunk Rule failed : {}", uniId, e);
        }
        writeDataToAjax(resultJson);
        return NONE;
    }

    /**
     * 删除Trunk规则
     * 
     * @return
     * @throws Exception
     */
    @OperationLogProperty(actionName = "uniPortVlanAction", operationName = "deleteTrunkRule")
    public String deleteTrunkRule() throws Exception {
        List<Integer> trunkVidList = new ArrayList<Integer>();
        VlanTrunkRule vlanTrunkRule = new VlanTrunkRule();
        vlanTrunkRule.setEntityId(entityId);
        vlanTrunkRule.setPortId(uniId);
        vlanTrunkRule.setPortIndex(uniIndex);
        if (trunkList != null && !"".equals(trunkList)) {
            String[] vids = trunkList.split(",");
            for (String vid : vids) {
                trunkVidList.add(Integer.parseInt(vid));
            }
            vlanTrunkRule.setTrunkVidListAfterSwitch(trunkVidList);
        } else {
            vlanTrunkRule.setTrunkVidListAfterSwitch(null);
        }
        // 记录操作结果
        JSONObject resultJson = new JSONObject();
        try {
            uniVlanService.updateUniVlanTrunkRule(vlanTrunkRule);
            resultJson.put("success", true);
            operationResult = OperationLog.SUCCESS;
        } catch (SnmpSetException e) {
            resultJson.put("success", false);
            resultJson.put("message", e.getMessage());
            operationResult = OperationLog.FAILURE;
            logger.error("Delete Uni[{}] Trunk Rule failed : {}", uniId, e);
        }
        writeDataToAjax(resultJson);
        return NONE;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public Long getOnuId() {
        return onuId;
    }

    public void setOnuId(Long onuId) {
        this.onuId = onuId;
    }

    public Long getUniIndex() {
        return uniIndex;
    }

    public void setUniIndex(Long uniIndex) {
        this.uniIndex = uniIndex;
    }

    public Long getUniId() {
        return uniId;
    }

    public void setUniId(Long uniId) {
        this.uniId = uniId;
    }

    public Integer getUniPvid() {
        return uniPvid;
    }

    public void setUniPvid(Integer uniPvid) {
        this.uniPvid = uniPvid;
    }

    public Integer getVlanMode() {
        return vlanMode;
    }

    public void setVlanMode(Integer vlanMode) {
        this.vlanMode = vlanMode;
    }

    public PortVlanAttribute getUniPortVlan() {
        return uniPortVlan;
    }

    public void setUniPortVlan(PortVlanAttribute uniPortVlan) {
        this.uniPortVlan = uniPortVlan;
    }

    public UniVlanBindTable getUniBindInfo() {
        return uniBindInfo;
    }

    public void setUniBindInfo(UniVlanBindTable uniBindInfo) {
        this.uniBindInfo = uniBindInfo;
    }

    public UniVlanProfileTable getUniVlanProfile() {
        return uniVlanProfile;
    }

    public void setUniVlanProfile(UniVlanProfileTable uniVlanProfile) {
        this.uniVlanProfile = uniVlanProfile;
    }

    public Integer getVlanIndex() {
        return vlanIndex;
    }

    public void setVlanIndex(Integer vlanIndex) {
        this.vlanIndex = vlanIndex;
    }

    public Integer getTranslationNewVid() {
        return translationNewVid;
    }

    public void setTranslationNewVid(Integer translationNewVid) {
        this.translationNewVid = translationNewVid;
    }

    public String getTrunkList() {
        return trunkList;
    }

    public void setTrunkList(String trunkList) {
        this.trunkList = trunkList;
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

    public List<VlanTranslationRule> getVlanTranslationRuleList() {
        return vlanTranslationRuleList;
    }

    public void setVlanTranslationRuleList(List<VlanTranslationRule> vlanTranslationRuleList) {
        this.vlanTranslationRuleList = vlanTranslationRuleList;
    }

    public List<VlanAggregationRule> getVlanAggregationRuleList() {
        return vlanAggregationRuleList;
    }

    public void setVlanAggregationRuleList(List<VlanAggregationRule> vlanAggregationRuleList) {
        this.vlanAggregationRuleList = vlanAggregationRuleList;
    }

    public Integer getOperationResult() {
        return operationResult;
    }

    public void setOperationResult(Integer operationResult) {
        this.operationResult = operationResult;
    }

}
