/***********************************************************************
 * $Id: OltAclAction.java,v1.0 2013-10-25 下午5:27:18 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.acl.action;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.topvision.ems.epon.acl.domain.AclListTable;
import com.topvision.ems.epon.acl.domain.AclPortACLListTable;
import com.topvision.ems.epon.acl.domain.AclRuleTable;
import com.topvision.ems.epon.acl.service.OltAclService;
import com.topvision.ems.epon.olt.action.AbstractEponAction;
import com.topvision.framework.annotation.OperationLogProperty;
import com.topvision.framework.common.MacUtils;
import com.topvision.framework.exception.engine.SnmpException;
import com.topvision.platform.domain.OperationLog;
import com.topvision.platform.domain.UserContext;

/**
 * @author Bravin
 * @created @2013-10-25-下午5:27:18
 *
 */
@Controller("oltAclAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class OltAclAction extends AbstractEponAction {
    protected static final long serialVersionUID = -2061674807141634232L;
    protected final Logger logger = LoggerFactory.getLogger(OltAclAction.class);
    @Autowired
    protected OltAclService oltAclService;
    protected Integer aclIndex;
    protected Long portIndex;
    protected Integer direction;
    protected Integer ruleNo;
    // 传递参数
    protected JSONArray aclListParam = new JSONArray();// ACL的参数
    protected JSONArray aclListDescr = new JSONArray();// ACL的描述
    protected JSONArray aclListPortParam = new JSONArray();// ACLlist对应的port
    protected JSONArray aclPortAclListParam = new JSONArray();// ACLport对应的ACLlist
    protected JSONArray aclRuleParam = new JSONArray();// ACLlist对应的rule
    protected JSONArray aclRuleListParam = new JSONArray();// ACLlist对应的rule
    protected JSONArray aclFlagList = new JSONArray();// 当前存在的AclNum的列表，添加时用于判断是否该ACL已经存在
    protected JSONArray slotSniObject = new JSONArray();
    protected JSONArray allAclRuleList;
    protected JSONArray allAclRuleStr;
    protected JSONArray aclConflict;
    protected Integer portAclId;
    protected String descr;
    protected Integer priority;
    // 临时参数
    protected Integer tempAclId;
    protected Integer tempPortNum;
    protected String portAclListStr;
    protected Integer modifyFlag;
    protected Integer aclRuleIndex;
    protected String topMatchedFieldSelectionSymbol;// acl匹配规则选项
    protected String matchParasJsonStr;// 匹配规则参数
    protected String aclAction;
    protected String aclActionPara;
    protected Integer aclPortJspFlag;
    protected Integer matchRangeNum;
    protected String aclMatchRangeFlag;
    protected Integer ruleLeaveNum;

    /**
     * 修改ACL基本信息
     * @return
     */
    public String showAclEdit() {
        return SUCCESS;
    }

    /**
     * 添加一个ACL列表
     * 
     * @return
     */
    @OperationLogProperty(actionName = "oltAclAction", operationName = "addAclList")
    public String addAclList() throws Exception {
        AclListTable tempAcl = new AclListTable();
        tempAcl.setEntityId(entityId);
        tempAcl.setTopAclDescription(descr);
        tempAcl.setTopAclListIndex(aclIndex);
        tempAcl.setTopAclRulePriority(priority);
        String message;
        try {
            oltAclService.addAclList(tempAcl);
            operationResult = OperationLog.SUCCESS;
            message = "success";
        } catch (SnmpException e) {
            message = e.getMessage();
            logger.debug("addAclList failed: " + e);
            operationResult = OperationLog.FAILURE;
        }
        writeDataToAjax(message);
        return NONE;
    }

    /**
     * 删除一个ACL列表
     * 
     * @return
     */
    @OperationLogProperty(actionName = "oltAclAction", operationName = "deletAclList")
    public String deletAclList() throws Exception {
        String message;
        try {
            oltAclService.deleteAclList(entityId, aclIndex);
            operationResult = OperationLog.SUCCESS;
            message = "success";
        } catch (SnmpException e) {
            logger.debug("deletAclList error: {}", e);
            operationResult = OperationLog.FAILURE;
            message = e.getMessage();
        }
        writeDataToAjax(message);
        return NONE;
    }

    /**
     * 修改ACL列表的属性
     * 
     * @return
     */
    @OperationLogProperty(actionName = "oltAclAction", operationName = "modifyAclList")
    public String modifyAclList() {
        AclListTable tempAcl = new AclListTable();
        tempAcl.setEntityId(entityId);
        tempAcl.setTopAclDescription(descr);
        tempAcl.setTopAclListIndex(aclIndex);
        tempAcl.setTopAclRulePriority(priority);
        tempAcl.setTopAclRuleNum(ruleNo);
        oltAclService.modifyAclList(tempAcl);
        return NONE;
    }

    /**
    * 刷新ACL的相关信息
    * 
    */
    @OperationLogProperty(actionName = "oltAclAction", operationName = "refreshAcl")
    public String refreshAcl() throws Exception {
        String message = "success";
        try {
            oltAclService.refreshAclListTable(entityId);
            oltAclService.refreshAclPortACLList(entityId);
            oltAclService.refreshAclRuleList(entityId);
            operationResult = OperationLog.SUCCESS;
        } catch (SnmpException e) {
            logger.debug("refreshAcl error: {}", e);
            operationResult = OperationLog.FAILURE;
            message = "error";
        }
        writeDataToAjax(message);
        return NONE;
    }

    /**
    * ACL列表的添加页面
    * 
    * @return
    */
    public String showAclAdd() {
        List<AclListTable> tmp = oltAclService.getAclList(entityId);
        List<Integer> aclFlagTemp = new ArrayList<Integer>();
        if (tmp != null) {
            if (tmp.size() > 0) {
                for (AclListTable anAcl : tmp) {
                    aclFlagTemp.add(anAcl.getTopAclListIndex());
                }
            }
        }
        if (aclFlagTemp.size() > 0) {
            aclFlagList = JSONArray.fromObject(aclFlagTemp);
        } else {
            aclFlagList = JSONArray.fromObject(false);
        }
        return SUCCESS;
    }

    /**
    * 展示ACL列表的列表
    * 
    * @return
    */
    public String showAclList() {
        aclIndex = getAclList();
        if (!aclIndex.equals(0)) {
            getAclRule();
        } else {
            aclListPortParam = JSONArray.fromObject(false);
            aclRuleParam = JSONArray.fromObject(false);
        }
        return SUCCESS;
    }

    /**
    * 公用方法 获取ACLlist的数据
    * 
    */
    protected Integer getAclList() {
        Integer tempAclIndex = 0;
        List<Integer> aclListParamTmp = new ArrayList<Integer>();
        List<String> aclListDescrTmp = new ArrayList<String>();
        List<List<Long>> aclListPortParamTmpList = new ArrayList<List<Long>>();
        try {
            List<AclListTable> aclListTables = oltAclService.getAclList(entityId);
            if (!aclListTables.equals(null) && aclListTables.size() != 0) {
                for (AclListTable anAcl : aclListTables) {
                    aclListDescrTmp.add(anAcl.getTopAclDescription());
                    aclListParamTmp.add(anAcl.getTopAclListIndex());
                    aclListParamTmp.add(anAcl.getTopAclRuleNum());
                    aclListParamTmp.add(anAcl.getTopAclRulePriority());
                    tempAclId = anAcl.getTopAclListIndex();
                    List<Long> aclListPortParamTmp = getAclPortByList();
                    aclListPortParamTmpList.add(aclListPortParamTmp);
                    aclListParamTmp.add(tempPortNum);
                }
                if (aclListParamTmp.size() > 0) {
                    tempAclIndex = aclListParamTmp.get(0);
                }
            }
        } catch (Exception e) {
            logger.debug("getAclList error: {}", e);
        }
        if (aclListParamTmp.size() != 0 && aclListDescrTmp.size() != 0) {
            aclListParam = JSONArray.fromObject(aclListParamTmp);
            aclListDescr = JSONArray.fromObject(aclListDescrTmp);
        } else {
            aclListParam = JSONArray.fromObject(false);
            aclListDescr = JSONArray.fromObject(false);
        }
        if (aclListPortParamTmpList != null && aclListPortParamTmpList.size() != 0) {
            aclListPortParam = JSONArray.fromObject(aclListPortParamTmpList);
        } else {
            aclListPortParam = JSONArray.fromObject(false);
        }
        return tempAclIndex;
    }

    /**
    * 获取ACLlist对应的port数据
    * 
    */
    protected List<Long> getAclPortByList() {
        tempPortNum = 0;
        List<Long> aclListPortParamTmp = new ArrayList<Long>();
        try {
            List<AclPortACLListTable> aclPortACLListTable = oltAclService.getAclPortByAclList(entityId, tempAclId);
            if (!aclPortACLListTable.equals(null) && aclPortACLListTable.size() != 0) {
                aclListPortParamTmp.add(tempAclId.longValue());
                for (AclPortACLListTable aPort : aclPortACLListTable) {
                    aclListPortParamTmp.add(aPort.getPortIndex());
                    aclListPortParamTmp.add(aPort.getTopAclPortDirection().longValue());
                    tempPortNum++;
                }
            }
        } catch (Exception e) {
            logger.debug("getAclPortByAclList error: {}", e);
        }
        return aclListPortParamTmp;
    }

    /**
    * 获取ACLrule的数据
    * 
    */
    protected void getAclRule() {
        List<AclRuleTable> aclRuleParamTmp = new ArrayList<AclRuleTable>();
        try {
            List<AclRuleTable> tempParam = oltAclService.getAclRuleList(entityId, aclIndex);
            if (!tempParam.equals(null) && tempParam.size() != 0) {
                for (AclRuleTable aRule : tempParam) {
                    aclRuleParamTmp.add(aRule);
                }
            }
        } catch (Exception e) {
            logger.debug("getAclRuleList error: {}", e);
        }
        //add by fanzidong,需要在展示前格式化MAC地址
        UserContext uc = (UserContext) super.getSession().get(UserContext.KEY);
        String displayRule = uc.getMacDisplayStyle();
        for (AclRuleTable aclRuleTable : aclRuleParamTmp) {
            String formatedSrcMac = MacUtils.convertMacToDisplayFormat(aclRuleTable.getTopMatchedSrcMac(), displayRule);
            aclRuleTable.setTopMatchedSrcMac(formatedSrcMac);
            String formatedDestMac = MacUtils.convertMacToDisplayFormat(aclRuleTable.getTopMatchedDstMac(), displayRule);
            aclRuleTable.setTopMatchedDstMac(formatedDestMac);
        }
        aclRuleParam = JSONArray.fromObject(aclRuleParamTmp);
    }

    /**
    * 获取所有的ACLrule的数据
    * 
    */
    protected List<AclRuleTable> getAllAclRule() {
        aclMatchRangeFlag = "0";
        List<AclRuleTable> aclRuleParamTmp = new ArrayList<AclRuleTable>();
        try {
            List<AclRuleTable> tempParam = oltAclService.getAllAclRuleList(entityId);
            if (!tempParam.equals(null) && tempParam.size() != 0) {
                for (AclRuleTable aRule : tempParam) {
                    aclRuleParamTmp.add(aRule);
                    if (aRule.getTopMatchedFieldSelectionSymbol() != null
                            && aRule.getTopMatchedFieldSelectionSymbol().size() != 0) {
                        // 每个rule最多只有一条范围匹配
                        if (aRule.getTopMatchedFieldSelectionSymbol().contains(new Integer(2))
                                || aRule.getTopMatchedFieldSelectionSymbol().contains(new Integer(15))
                                || aRule.getTopMatchedFieldSelectionSymbol().contains(new Integer(16))) {
                            // 组装为最大值为32000的列表，查询时的遍历就只需16次
                            Integer tmpI = 16 * (aRule.getTopAclRuleListIndex() - 1) + aRule.getTopAclRuleIndex();
                            aclMatchRangeFlag = aclMatchRangeFlag + "_" + tmpI.toString();
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.debug("getAllAclRuleList error: {}", e);
        }
        return aclRuleParamTmp;
    }

    /**
    * 获取一个板卡下绑定的ACL规则中的范围匹配的个数
    * 
    */
    protected Integer getMatchRangeNumInSlot() {
        List<Integer> tmpNum = new ArrayList<Integer>();
        // 获得所有的ACLrule的列表
        List<AclRuleTable> aclRuleList = getAllAclRule();
        // 获得该端口所在板卡的所有端口绑定的ACL列表的List<Integer>
        List<Integer> tmpAclList = oltAclService.getAclListBySlot(entityId, portIndex);
        if (aclRuleList != null && aclRuleList.size() != 0 && tmpAclList != null && tmpAclList.size() != 0) {
            for (AclRuleTable tmpRule : aclRuleList) {
                boolean tempFlag = false;
                for (Integer aclId : tmpAclList) {
                    if (tmpRule.getTopAclRuleListIndex() != null) {
                        if (aclId.equals(tmpRule.getTopAclRuleListIndex())) {
                            tempFlag = true;
                        }
                    }
                }
                if (tmpRule.getTopMatchedFieldSelectionSymbol() != null
                        && tmpRule.getTopMatchedFieldSelectionSymbol().size() != 0) {
                    if (tempFlag
                            && ((tmpRule.getTopMatchedFieldSelectionSymbol().contains(new Integer(2)) && !tmpRule
                                    .getTopMatchedStartSVid().equals(tmpRule.getTopMatchedEndSVid()))
                                    || (tmpRule.getTopMatchedFieldSelectionSymbol().contains(new Integer(15)) && !tmpRule
                                            .getTopMatchedStartSrcPort().equals(tmpRule.getTopMatchedEndSrcPort())) || (tmpRule
                                    .getTopMatchedFieldSelectionSymbol().contains(new Integer(16)) && !tmpRule
                                    .getTopMatchedStartDstPort().equals(tmpRule.getTopMatchedEndDstPort())))) {
                        if (!tmpNum.contains(tmpRule.getTopAclRuleListIndex())) {
                            tmpNum.add(tmpRule.getTopAclRuleListIndex());
                        }
                    }
                }
            }
        }
        return tmpNum.size();
    }

    /**
    * 获取port的ACLlist数据
    * 
    * @return
    */
    protected List<Integer> getAclListByPort() {
        List<Integer> aclPortAclListParamTmp = new ArrayList<Integer>();
        try {
            List<AclPortACLListTable> aclPortACLListTable = oltAclService.getAclPortACLList(entityId, portIndex,
                    direction);
            if (!aclPortACLListTable.equals(null) && aclPortACLListTable.size() != 0) {
                for (AclPortACLListTable anAcl : aclPortACLListTable) {
                    aclPortAclListParamTmp.add(anAcl.getTopPortAclListIndex());
                }
            }
        } catch (Exception e) {
            logger.debug("getAclListByPort error: {}", e);
        }
        if (aclPortAclListParamTmp != null && aclPortAclListParamTmp.size() != 0) {
            aclPortAclListParam = JSONArray.fromObject(aclPortAclListParamTmp);
        } else {
            aclPortAclListParam = JSONArray.fromObject(false);
        }
        return aclPortAclListParamTmp;
    }

    /**
    * 添加端口的ACL绑定
    * 
    */
    @OperationLogProperty(actionName = "oltAclAction", operationName = "addAclPort")
    protected String addAclPort() throws Exception {
        AclPortACLListTable newPortList = new AclPortACLListTable();
        newPortList.setEntityId(entityId);
        newPortList.setPortIndex(portIndex);
        newPortList.setTopAclPortDirection(direction);
        newPortList.setTopPortAclListIndex(portAclId);
        String message = "success";
        try {
            oltAclService.addAclPortACLList(newPortList);
            operationResult = OperationLog.SUCCESS;
        } catch (SnmpException e) {
            logger.debug("addAclPort error: {}", e);
            operationResult = OperationLog.FAILURE;
            message = e.getMessage();
        }
        return message;
    }

    public Integer getAclIndex() {
        return aclIndex;
    }

    public void setAclIndex(Integer aclIndex) {
        this.aclIndex = aclIndex;
    }

    public Long getPortIndex() {
        return portIndex;
    }

    public void setPortIndex(Long portIndex) {
        this.portIndex = portIndex;
    }

    public Integer getDirection() {
        return direction;
    }

    public void setDirection(Integer direction) {
        this.direction = direction;
    }

    public Integer getRuleNo() {
        return ruleNo;
    }

    public void setRuleNo(Integer ruleNo) {
        this.ruleNo = ruleNo;
    }

    public JSONArray getAclListParam() {
        return aclListParam;
    }

    public void setAclListParam(JSONArray aclListParam) {
        this.aclListParam = aclListParam;
    }

    public JSONArray getAclListDescr() {
        return aclListDescr;
    }

    public void setAclListDescr(JSONArray aclListDescr) {
        this.aclListDescr = aclListDescr;
    }

    public JSONArray getAclListPortParam() {
        return aclListPortParam;
    }

    public void setAclListPortParam(JSONArray aclListPortParam) {
        this.aclListPortParam = aclListPortParam;
    }

    public JSONArray getAclPortAclListParam() {
        return aclPortAclListParam;
    }

    public void setAclPortAclListParam(JSONArray aclPortAclListParam) {
        this.aclPortAclListParam = aclPortAclListParam;
    }

    public JSONArray getAclRuleParam() {
        return aclRuleParam;
    }

    public void setAclRuleParam(JSONArray aclRuleParam) {
        this.aclRuleParam = aclRuleParam;
    }

    public JSONArray getAclRuleListParam() {
        return aclRuleListParam;
    }

    public void setAclRuleListParam(JSONArray aclRuleListParam) {
        this.aclRuleListParam = aclRuleListParam;
    }

    public JSONArray getAclFlagList() {
        return aclFlagList;
    }

    public void setAclFlagList(JSONArray aclFlagList) {
        this.aclFlagList = aclFlagList;
    }

    public JSONArray getSlotSniObject() {
        return slotSniObject;
    }

    public void setSlotSniObject(JSONArray slotSniObject) {
        this.slotSniObject = slotSniObject;
    }

    public JSONArray getAllAclRuleList() {
        return allAclRuleList;
    }

    public void setAllAclRuleList(JSONArray allAclRuleList) {
        this.allAclRuleList = allAclRuleList;
    }

    public JSONArray getAllAclRuleStr() {
        return allAclRuleStr;
    }

    public void setAllAclRuleStr(JSONArray allAclRuleStr) {
        this.allAclRuleStr = allAclRuleStr;
    }

    public JSONArray getAclConflict() {
        return aclConflict;
    }

    public void setAclConflict(JSONArray aclConflict) {
        this.aclConflict = aclConflict;
    }

    public Integer getPortAclId() {
        return portAclId;
    }

    public void setPortAclId(Integer portAclId) {
        this.portAclId = portAclId;
    }

    public String getDescr() {
        return descr;
    }

    public void setDescr(String descr) {
        this.descr = descr;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public Integer getTempAclId() {
        return tempAclId;
    }

    public void setTempAclId(Integer tempAclId) {
        this.tempAclId = tempAclId;
    }

    public Integer getTempPortNum() {
        return tempPortNum;
    }

    public void setTempPortNum(Integer tempPortNum) {
        this.tempPortNum = tempPortNum;
    }

    public String getPortAclListStr() {
        return portAclListStr;
    }

    public void setPortAclListStr(String portAclListStr) {
        this.portAclListStr = portAclListStr;
    }

    public Integer getModifyFlag() {
        return modifyFlag;
    }

    public void setModifyFlag(Integer modifyFlag) {
        this.modifyFlag = modifyFlag;
    }

    @Override
    public Integer getOperationResult() {
        return operationResult;
    }

    @Override
    public void setOperationResult(Integer operationResult) {
        this.operationResult = operationResult;
    }

    public Integer getAclRuleIndex() {
        return aclRuleIndex;
    }

    public void setAclRuleIndex(Integer aclRuleIndex) {
        this.aclRuleIndex = aclRuleIndex;
    }

    public String getTopMatchedFieldSelectionSymbol() {
        return topMatchedFieldSelectionSymbol;
    }

    public void setTopMatchedFieldSelectionSymbol(String topMatchedFieldSelectionSymbol) {
        this.topMatchedFieldSelectionSymbol = topMatchedFieldSelectionSymbol;
    }

    public String getMatchParasJsonStr() {
        return matchParasJsonStr;
    }

    public void setMatchParasJsonStr(String matchParasJsonStr) {
        this.matchParasJsonStr = matchParasJsonStr;
    }

    public String getAclAction() {
        return aclAction;
    }

    public void setAclAction(String aclAction) {
        this.aclAction = aclAction;
    }

    public String getAclActionPara() {
        return aclActionPara;
    }

    public void setAclActionPara(String aclActionPara) {
        this.aclActionPara = aclActionPara;
    }

    public Integer getAclPortJspFlag() {
        return aclPortJspFlag;
    }

    public void setAclPortJspFlag(Integer aclPortJspFlag) {
        this.aclPortJspFlag = aclPortJspFlag;
    }

    public Integer getMatchRangeNum() {
        return matchRangeNum;
    }

    public void setMatchRangeNum(Integer matchRangeNum) {
        this.matchRangeNum = matchRangeNum;
    }

    public String getAclMatchRangeFlag() {
        return aclMatchRangeFlag;
    }

    public void setAclMatchRangeFlag(String aclMatchRangeFlag) {
        this.aclMatchRangeFlag = aclMatchRangeFlag;
    }

    public Integer getRuleLeaveNum() {
        return ruleLeaveNum;
    }

    public void setRuleLeaveNum(Integer ruleLeaveNum) {
        this.ruleLeaveNum = ruleLeaveNum;
    }

}
