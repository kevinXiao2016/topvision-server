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

import org.apache.struts2.ServletActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.topvision.ems.epon.exception.UniVlanConfigException;
import com.topvision.ems.epon.vlan.domain.PortVlanAttribute;
import com.topvision.ems.epon.vlan.domain.VlanAggregationRule;
import com.topvision.ems.epon.vlan.domain.VlanTranslationRule;
import com.topvision.ems.epon.vlan.domain.VlanTrunkRule;
import com.topvision.ems.epon.vlan.service.UniVlanService;
import com.topvision.framework.annotation.OperationLogProperty;
import com.topvision.framework.exception.engine.SnmpException;
import com.topvision.framework.utils.EponConstants;
import com.topvision.framework.web.struts2.BaseAction;
import com.topvision.platform.ResourceManager;
import com.topvision.platform.domain.OperationLog;

import net.sf.json.JSONArray;

/**
 * @author flack
 * @created @2013-10-25-下午1:50:56
 *
 */
@Controller("uniVlanAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class UniVlanAction extends BaseAction {
    private static final long serialVersionUID = -8377939086987081620L;
    private final Logger logger = LoggerFactory.getLogger(UniVlanAction.class);

    private Long entityId;
    private Long uniId;
    private Long uniIndex;
    private String vlanTagTpid;
    private Integer vlanPVid;
    private Integer vlanMode;
    private Integer vlanIndex;
    private Integer translationNewVid;
    private String trunkList;
    private String aggrCvids;
    private Integer aggrSvid;
    private List<VlanTranslationRule> vlanTranslationRuleList;
    private List<VlanAggregationRule> vlanAggregationRuleList;
    private Integer operationResult;
    private PortVlanAttribute portVlanAttribute;
    private VlanTranslationRule vlanTranslationRule;
    private VlanAggregationRule vlanAggregationRule;
    private VlanTrunkRule vlanTrunkRule;
    @Autowired
    private UniVlanService uniVlanService;

    /**
     * 加载UNI端口VLAN的基本属性
     * 
     * @return String
     */
    public String showPortVlanAttribute() {
        portVlanAttribute = uniVlanService.getUniVlanAttribute(uniId);
        return SUCCESS;
    }

    /**
     * 更新UNI端口VLAN的基本属性
     * 
     * @return String
     * @throws Exception
     */
    @OperationLogProperty(actionName = "uniVlanAction", operationName = "updatePortVlanAttribute")
    public String updatePortVlanAttribute() throws Exception {
        String result;
        try {
            // uniVlanService.updateUniVlanAttribute(uniId, vlanTagTpid, vlanPVid, vlanMode);
            result = "success";
            operationResult = OperationLog.SUCCESS;
        } catch (UniVlanConfigException sce) {
            result = sce.getMessage();
            operationResult = OperationLog.FAILURE;
            logger.debug("uni vlan config error:{}", sce);
        } catch (SnmpException sse) {
            result = getString("Business.snmpWrong", "epon");
            operationResult = OperationLog.FAILURE;
            logger.debug("uni vlan config snmp error:{}", sse);
        }
        writeDataToAjax(result);
        return NONE;
    }

    /**
     * 从设备上获取UNI端口VLAN的基本属性
     * 
     * @return String
     * @throws Exception
     */
    public String refreshPortVlanAttribute() throws Exception {
        String result;
        try {
            // uniVlanService.refreshPortVlanAttribute(entityId, uniId);
            result = "success";
        } catch (SnmpException sse) {
            result = getString("Business.snmpWrong", "epon");
            logger.error("", sse);
        }
        writeDataToAjax(result);
        return NONE;
    }

    /**
     * UNI端口VLAN模式展示页面跳转
     * 
     * @return String
     */
    public String showUniVlanMode() {
        portVlanAttribute = uniVlanService.getUniVlanAttribute(uniId);
        return SUCCESS;
    }

    /**
     * UNI端口VLAN模式切换
     * 
     * @return String
     * @throws Exception
     */
    @OperationLogProperty(actionName = "uniVlanAction", operationName = "changePortVlanMode")
    public String changePortVlanMode() throws Exception {
        String result;
        try {
            if (vlanMode == EponConstants.TRANS) {
                vlanTranslationRuleList = uniVlanService.getVlanTranslationRuleList(uniId);
                if (vlanTranslationRuleList != null) {
                    for (VlanTranslationRule rule : vlanTranslationRuleList) {
                        uniVlanService.deleteVlanTransDB(uniId, rule.getVlanIndex());
                        uniVlanService.addVlanTranslationRule(rule);
                    }
                }
            } else if (vlanMode == EponConstants.AGGR) {
                vlanAggregationRuleList = uniVlanService.getVlanAggregationRuleList(uniId);
                if (vlanAggregationRuleList != null) {
                    for (VlanAggregationRule rule : vlanAggregationRuleList) {
                        uniVlanService.deleteSVlanAggrDB(uniId, rule.getPortAggregationVidIndex());
                        uniVlanService.addSVlanAggregationRule(rule);
                    }
                }
            } else if (vlanMode == EponConstants.TRUNK) {
                VlanTrunkRule trunkRule = uniVlanService.getVlanTrunkRules(uniId);
                if (trunkRule != null) {
                    uniVlanService.updateUniVlanTrunkRule(trunkRule);
                } else {
                    // uniVlanService.updateUniVlanAttribute(uniId, null, null,
                    // EponConstants.TRUNK);
                }
            }
            result = "success";
            operationResult = OperationLog.SUCCESS;
        } catch (UniVlanConfigException sce) {
            result = sce.getMessage();
            operationResult = OperationLog.FAILURE;
            logger.debug("uni change mode error:{}", sce);
        } catch (SnmpException sse) {
            result = getString("Business.snmpWrong", "epon");
            operationResult = OperationLog.FAILURE;
            logger.debug("uni change mode snmp error:{}", sse);
        }
        writeDataToAjax(result);
        return NONE;
    }

    /**
     * 加载UNI端口VLAN转换规则列表
     * 
     * @return String
     * @throws Exception
     */
    public String loadVlanTranslationRuleList() throws Exception {
        // 将数据库中获取的transRuleList数据转换为页面使用的transData列表数据
        vlanTranslationRuleList = uniVlanService.getVlanTranslationRuleList(uniId);
        List<List<Integer>> result = new ArrayList<List<Integer>>();
        if (vlanTranslationRuleList != null) {
            List<Integer> tmpList;
            for (VlanTranslationRule rule : vlanTranslationRuleList) {
                tmpList = new ArrayList<Integer>();
                tmpList.add(rule.getVlanIndex());
                tmpList.add(rule.getTranslationNewVid());
                result.add(tmpList);
            }
        }
        logger.debug("transData:{}", result);
        ServletActionContext.getResponse().setContentType("application/json");
        ServletActionContext.getResponse().setHeader("Cache-Control", "no-cache");
        ServletActionContext.getResponse().setCharacterEncoding("UTF-8");
        ServletActionContext.getResponse().getWriter().print(JSONArray.fromObject(result));
        return NONE;
    }

    /**
     * 新增UNI端口VLAN转换规则
     * 
     * @return String
     * @throws Exception
     */
    @OperationLogProperty(actionName = "uniVlanAction", operationName = "addVlanTranslationRule")
    public String addVlanTranslationRule() throws Exception {
        VlanTranslationRule vlanTranslationRule = new VlanTranslationRule();
        vlanTranslationRule.setEntityId(entityId);
        vlanTranslationRule.setVlanIndex(vlanIndex);
        vlanTranslationRule.setTranslationNewVid(translationNewVid);
        vlanTranslationRule.setPortId(uniId);
        String result;
        try {
            uniVlanService.addVlanTranslationRule(vlanTranslationRule);
            result = "success";
            operationResult = OperationLog.SUCCESS;
        } catch (UniVlanConfigException sce) {
            result = getString(sce.getMessage(), "epon");
            operationResult = OperationLog.FAILURE;
            logger.debug("uni add TranslationRule error:{}", sce);
        } catch (SnmpException sse) {
            logger.debug("", sse);
            operationResult = OperationLog.FAILURE;
            result = getString("Business.snmpWrong", "epon");
        }
        writeDataToAjax(result);
        return NONE;
    }

    /**
     * 更新UNI端口VLAN转换规则
     * 
     * @return String
     * @throws Exception
     */
    @OperationLogProperty(actionName = "uniVlanAction", operationName = "updateVlanTranslationRule")
    public String updateVlanTranslationRule() throws Exception {
        VlanTranslationRule vlanTranslationRule = new VlanTranslationRule();
        vlanTranslationRule.setEntityId(entityId);
        vlanTranslationRule.setVlanIndex(vlanIndex);
        vlanTranslationRule.setTranslationNewVid(translationNewVid);
        vlanTranslationRule.setPortId(uniId);
        String result;
        try {
            uniVlanService.deleteVlanTranslationRuleByAfter(uniId, vlanIndex, translationNewVid);
            uniVlanService.addVlanTranslationRule(vlanTranslationRule);
            result = "success";
            operationResult = OperationLog.SUCCESS;
        } catch (UniVlanConfigException sce) {
            result = sce.getMessage();
            operationResult = OperationLog.FAILURE;
            logger.debug("uni add TranslationRule error:{}", sce);
        }
        writeDataToAjax(result);
        return NONE;
    }

    /**
     * 删除UNI端口VLAN转换规则
     * 
     * @return String
     * @throws Exception
     */
    @OperationLogProperty(actionName = "uniVlanAction", operationName = "deleteVlanTranslationRule")
    public String deleteVlanTranslationRule() throws Exception {
        String result;
        try {
            // uniVlanService.deleteVlanTranslationRule(uniId, vlanIndex);
            result = "success";
            operationResult = OperationLog.SUCCESS;
        } catch (UniVlanConfigException sce) {
            result = sce.getMessage();
            operationResult = OperationLog.FAILURE;
            logger.debug("uni add TranslationRule error:{}", sce);
        }
        writeDataToAjax(result);
        return NONE;
    }

    /**
     * 从设备上获取UNI端口VLAN转换规则列表
     * 
     * @return String
     * @throws Exception
     */
    public String refreshVlanTranlationRule() throws Exception {
        String result;
        try {
            // uniVlanService.refreshPortVlanAttribute(entityId, uniId);
            // uniVlanService.refreshVlanTranslationRule(entityId, uniId);
            result = "success";
        } catch (SnmpException sse) {
            result = getString("Business.snmpWrong", "epon");
            logger.error("", sse);
        }
        writeDataToAjax(result);
        return NONE;
    }

    /**
     * 加载UNI端口VLAN聚合规则列表
     * 
     * @return String
     * @throws Exception
     */
    public String loadVlanAggregationRuleList() throws Exception {
        vlanAggregationRuleList = uniVlanService.getVlanAggregationRuleList(uniId);
        List<List<List<Integer>>> result = new ArrayList<List<List<Integer>>>();
        if (vlanAggregationRuleList != null) {
            List<Integer> svid;
            List<Integer> cvids;
            List<List<Integer>> tmpList;
            for (VlanAggregationRule rule : vlanAggregationRuleList) {
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
        ServletActionContext.getResponse().setContentType("application/json");
        ServletActionContext.getResponse().setHeader("Cache-Control", "no-cache");
        ServletActionContext.getResponse().setCharacterEncoding("UTF-8");
        ServletActionContext.getResponse().getWriter().print(JSONArray.fromObject(result));
        return NONE;
    }

    /**
     * 新增UNI端口VLAN聚合规则
     * 
     * @return String
     * @throws Exception
     */
    @OperationLogProperty(actionName = "uniVlanAction", operationName = "addVlanAggregationRuleList")
    public String addVlanAggregationRuleList() throws Exception {
        String result;
        String[] cvids = aggrCvids.split(",");
        List<Integer> cvidAggrList = new ArrayList<Integer>();
        for (String vid : cvids) {
            cvidAggrList.add(Integer.parseInt(vid));
        }
        VlanAggregationRule vlanAggregationRule = new VlanAggregationRule();
        vlanAggregationRule.setEntityId(entityId);
        vlanAggregationRule.setPortId(uniId);
        vlanAggregationRule.setPortAggregationVidIndex(aggrSvid);
        vlanAggregationRule.setAggregationVidListAfterSwitch(cvidAggrList);
        try {
            uniVlanService.addSVlanAggregationRule(vlanAggregationRule);
            result = "success";
            operationResult = OperationLog.SUCCESS;
        } catch (UniVlanConfigException sce) {
            result = sce.getMessage();
            operationResult = OperationLog.SUCCESS;
            logger.debug("uni aggr config error:{}", sce);
        }
        writeDataToAjax(result);
        return NONE;
    }

    /**
     * 修改UNI端口VLAN聚合规则
     * 
     * @return String
     * @throws Exception
     */
    @OperationLogProperty(actionName = "uniVlanAction", operationName = "modifyVlanAggregationRuleList")
    public String modifyVlanAggregationRuleList() throws Exception {
        String result;
        List<Integer> cvidAggrList = new ArrayList<Integer>();
        if (!aggrCvids.equals("")) {
            String[] cvids = aggrCvids.split(",");
            for (String vid : cvids) {
                cvidAggrList.add(Integer.parseInt(vid));
            }
        }
        VlanAggregationRule vlanAggregationRule = new VlanAggregationRule();
        vlanAggregationRule.setEntityId(entityId);
        vlanAggregationRule.setPortId(uniId);
        vlanAggregationRule.setPortAggregationVidIndex(aggrSvid);
        vlanAggregationRule.setAggregationVidListAfterSwitch(cvidAggrList);
        try {
            // uniVlanService.deleteSVlanAggregationRule(uniId, aggrSvid);
            uniVlanService.addSVlanAggregationRule(vlanAggregationRule);
            result = "success";
            operationResult = OperationLog.SUCCESS;
        } catch (UniVlanConfigException sce) {
            result = sce.getMessage();
            operationResult = OperationLog.FAILURE;
            logger.debug("uni aggr config error:{}", sce);
        }
        writeDataToAjax(result);
        return NONE;
    }

    /**
     * 删除UNI端口VLAN聚合规则
     * 
     * @return String
     * @throws Exception
     */
    @OperationLogProperty(actionName = "uniVlanAction", operationName = "deleteVlanAggregationRuleList")
    public String deleteVlanAggregationRuleList() throws Exception {
        String result;
        try {
            // uniVlanService.deleteSVlanAggregationRule(uniId, aggrSvid);
            result = "success";
            operationResult = OperationLog.SUCCESS;
        } catch (UniVlanConfigException sce) {
            result = sce.getMessage();
            operationResult = OperationLog.FAILURE;
            logger.debug("uni aggr config error:{}", sce);
        }
        writeDataToAjax(result);
        return NONE;
    }

    /**
     * 从设备上获取UNI端口VLAN聚合规则列表
     * 
     * @return String
     * @throws Exception
     */
    public String refreshVlanAggregationRuleList() throws Exception {
        String result;
        try {
            // uniVlanService.refreshPortVlanAttribute(entityId, uniId);
            // uniVlanService.refreshVlanAggregationRule(entityId, uniId);
            result = "success";
        } catch (SnmpException sse) {
            result = getString("Business.snmpWrong", "epon");
            logger.debug("uni aggr refresh error:{}", sse);
        }
        writeDataToAjax(result);
        return NONE;
    }

    /**
     * 加载UNI端口VLAN Trunk规则列表
     * 
     * @return String
     * @throws Exception
     */
    public String loadVlanTrunkRuleList() throws Exception {
        // 获得数据库中保存的trunk规则
        VlanTrunkRule trunkRule = uniVlanService.getVlanTrunkRules(uniId);
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
        ServletActionContext.getResponse().setContentType("application/json");
        ServletActionContext.getResponse().setHeader("Cache-Control", "no-cache");
        ServletActionContext.getResponse().setCharacterEncoding("UTF-8");
        ServletActionContext.getResponse().getWriter().print(JSONArray.fromObject(result));
        return NONE;
    }

    /**
     * 更新UNI端口VLAN Trunk规则
     * 
     * @return String
     * @throws Exception
     */
    @OperationLogProperty(actionName = "uniVlanAction", operationName = "updateVlanTrunkRule")
    public String updateVlanTrunkRule() throws Exception {
        String result;
        List<Integer> trunkVidList = new ArrayList<Integer>();
        VlanTrunkRule vlanTrunkRule = new VlanTrunkRule();
        vlanTrunkRule.setEntityId(entityId);
        vlanTrunkRule.setPortId(uniId);
        if (!trunkList.equals("") && trunkList != null) {
            String[] vids = trunkList.split(",");
            for (String vid : vids) {
                trunkVidList.add(Integer.parseInt(vid));
            }
            vlanTrunkRule.setTrunkVidListAfterSwitch(trunkVidList);
        } else {
            vlanTrunkRule.setTrunkVidListAfterSwitch(null);
        }
        try {
            uniVlanService.updateUniVlanTrunkRule(vlanTrunkRule);
            result = "success";
            operationResult = OperationLog.SUCCESS;
        } catch (UniVlanConfigException sce) {
            result = sce.getMessage();
            operationResult = OperationLog.FAILURE;
            logger.debug("uni vlan config error:{}", sce);
        } catch (SnmpException sse) {
            result = getString("Business.snmpWrong", "epon");
            operationResult = OperationLog.FAILURE;
            logger.debug("uni vlan config snmp error:{}", sse);
        }
        writeDataToAjax(result);
        return NONE;
    }

    /**
     * 从设备上获取UNI端口VLAN Trunk规则列表
     * 
     * @return String
     * @throws Exception
     */
    public String refreshVlanTrunkRule() throws Exception {
        String result;
        try {
            // uniVlanService.refreshPortVlanAttribute(entityId, uniId);
            // uniVlanService.refreshVlanTrunkRules(entityId, uniId);
            result = "success";
        } catch (SnmpException sse) {
            result = getString("Business.snmpWrong", "epon");
            logger.error("", sse);
        }
        writeDataToAjax(result);
        return NONE;
    }

    protected String getString(String key, String module) {
        module = String.format("com.topvision.ems.%s.resources", module);
        try {
            ResourceManager resourceManager = ResourceManager.getResourceManager(module);
            return resourceManager.getNotNullString(key);
        } catch (Exception e) {
            return key;
        }
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public Long getUniId() {
        return uniId;
    }

    public void setUniId(Long uniId) {
        this.uniId = uniId;
    }

    public String getVlanTagTpid() {
        return vlanTagTpid;
    }

    public void setVlanTagTpid(String vlanTagTpid) {
        this.vlanTagTpid = vlanTagTpid;
    }

    public Integer getVlanPVid() {
        return vlanPVid;
    }

    public void setVlanPVid(Integer vlanPVid) {
        this.vlanPVid = vlanPVid;
    }

    public Integer getVlanMode() {
        return vlanMode;
    }

    public void setVlanMode(Integer vlanMode) {
        this.vlanMode = vlanMode;
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

    public PortVlanAttribute getPortVlanAttribute() {
        return portVlanAttribute;
    }

    public void setPortVlanAttribute(PortVlanAttribute portVlanAttribute) {
        this.portVlanAttribute = portVlanAttribute;
    }

    public VlanTrunkRule getVlanTrunkRule() {
        return vlanTrunkRule;
    }

    public void setVlanTrunkRule(VlanTrunkRule vlanTrunkRule) {
        this.vlanTrunkRule = vlanTrunkRule;
    }

    public Long getUniIndex() {
        return uniIndex;
    }

    public void setUniIndex(Long uniIndex) {
        this.uniIndex = uniIndex;
    }

    public VlanTranslationRule getVlanTranslationRule() {
        return vlanTranslationRule;
    }

    public void setVlanTranslationRule(VlanTranslationRule vlanTranslationRule) {
        this.vlanTranslationRule = vlanTranslationRule;
    }

    public VlanAggregationRule getVlanAggregationRule() {
        return vlanAggregationRule;
    }

    public void setVlanAggregationRule(VlanAggregationRule vlanAggregationRule) {
        this.vlanAggregationRule = vlanAggregationRule;
    }

}
