/***********************************************************************
 * $Id: OltAclPortAction.java,v1.0 2013-10-25 下午5:28:23 $
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
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.topvision.ems.epon.acl.domain.AclPortACLListTable;
import com.topvision.ems.epon.acl.domain.AclRuleTable;
import com.topvision.framework.annotation.OperationLogProperty;
import com.topvision.framework.common.IpUtils;
import com.topvision.framework.exception.engine.SnmpException;
import com.topvision.framework.utils.EponConstants;
import com.topvision.platform.domain.OperationLog;

/**
 * @author Bravin
 * @created @2013-10-25-下午5:28:23
 *
 */
@Controller("oltAclPortAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class OltAclPortAction extends OltAclAction {
    private static final long serialVersionUID = 8457687241004569884L;
    private final Logger logger = LoggerFactory.getLogger(OltAclPortAction.class);

    private Integer aclIndex;
    private Integer ruleNo;

    /**
     * 解除端口的ACL绑定 public 方法
     * 
     * @return
     */
    public String deleteAclPort() throws Exception {
        String message = delAclPort();
        writeDataToAjax(message);
        return NONE;
    }

    /**
    * 修改端口的ACL绑定（批量及同时进行ACL绑定的添加或删除）
    * 
    * @return
    */
    @OperationLogProperty(actionName = "oltAclPortAction", operationName = "modifyAclPort")
    public String modifyAclPort() throws Exception {
        // TODO 待进一步详细测试
        List<Integer> aclListOld = getAclListByPort();
        List<Integer> aclListNew = new ArrayList<Integer>();
        String message = "success";
        if (!portAclListStr.equals("") && !portAclListStr.equals(null) && portAclListStr.length() != 0) {
            String[] tempStr = portAclListStr.split("q");
            for (String anAcl : tempStr) {
                aclListNew.add(Integer.valueOf(anAcl).intValue());
            }
            // 比较
            ArrayList<Integer> sameInOld = new ArrayList<Integer>();
            ArrayList<Integer> sameInNew = new ArrayList<Integer>();
            int tempOldNo = 0;
            for (Integer tempAclOld : aclListOld) {
                int tempNewNo = 0;
                for (Integer tempAclNew : aclListNew) {
                    if (tempAclOld.equals(tempAclNew)) {
                        sameInOld.add(tempOldNo);
                        sameInNew.add(tempNewNo);
                    }
                    tempNewNo++;
                }
                tempOldNo++;
            }
            int tmpJ = 0;
            for (Integer anIndex : sameInOld) {
                aclListOld.remove(anIndex - tmpJ);
                tmpJ++;
            }
            tmpJ = 0;
            for (Integer anIndex : sameInNew) {
                aclListNew.remove(anIndex - tmpJ);
                tmpJ++;
            }
            // 先删后加
            for (Integer anAcl : aclListOld) {
                portAclId = anAcl;
                String tmpD = delAclPort();
                if (!tmpD.equalsIgnoreCase("success") && message.equalsIgnoreCase("success")) {
                    message = tmpD;
                }
            }
            for (Integer anAcl : aclListNew) {
                portAclId = anAcl;
                String tmpA = addAclPort();
                if (!tmpA.equalsIgnoreCase("success") && message.equalsIgnoreCase("success")) {
                    message = tmpA;
                }
            }
        } else {
            for (Integer anAcl : aclListOld) {
                portAclId = anAcl;
                String tmpD = delAclPort();
                if (!tmpD.equalsIgnoreCase("success") && message.equalsIgnoreCase("success")) {
                    message = tmpD;
                }
            }
        }
        writeDataToAjax(message);
        return NONE;
    }

    /**
    * 解除端口的ACL绑定 private 方法
    * 
    * @return
    * @throws Exception
    */
    @OperationLogProperty(actionName = "oltAclPortAction", operationName = "delAclPort")
    private String delAclPort() throws Exception {
        String message = "success";
        AclPortACLListTable newPortList = new AclPortACLListTable();
        newPortList.setEntityId(entityId);
        newPortList.setPortIndex(portIndex);
        newPortList.setTopAclPortDirection(direction);
        newPortList.setTopPortAclListIndex(portAclId);
        try {
            oltAclService.deleteAclPortACLList(newPortList);
            operationResult = OperationLog.SUCCESS;
        } catch (SnmpException e) {
            logger.debug("deleteAclPort error: {}", e);
            operationResult = OperationLog.FAILURE;
            message = e.getMessage();
        }
        return message;
    }

    /**
    * 展示ACL端口页面
    * 
    * @return
    */
    public String showAclPort() {
        List<String> mes = new ArrayList<String>();
        // 获得并向前台传递端口绑定的ACL列表
        try {
            getAclListByPort();
        } catch (Exception e) {
            logger.error("getAclListByPort error: " + e.getMessage());
            mes.add("getAclListByPort error!");
        }
        // 获得并向前台传递所有的ACL列表
        try {
            getAclList();
        } catch (Exception e) {
            logger.error("getAclList error: " + e.getMessage());
            mes.add("getAclList error!");
        }
        // 获得该端口所在板卡的绑定的ACL的规则中的范围匹配的数目
        try {
            matchRangeNum = getMatchRangeNumInSlot();
        } catch (Exception e) {
            logger.error("getMatchRangeNumInSlot error: " + e.getMessage());
            mes.add("getMatchRangeNumInSlot error!");
        }
        // 获得所有的ACL规则的列表
        List<AclRuleTable> allRule = getAllAclRule();
        if (allRule != null && allRule.size() != 0) {
            // 组装aclRule结构用于冲突检验:[[[aclid],[ruleId,tmpI],[matchList<Integer>],[actionList<Integer>],[actionValue<List>]],[],[]]
            List<List<List<Integer>>> allRuleList = new ArrayList<List<List<Integer>>>();
            List<List<List<String>>> allRuleStr = new ArrayList<List<List<String>>>();
            try {
                int tmpI = 0;// 标示是acllist列表里的数据的索引号，从0开始
                for (AclRuleTable aRule : allRule) {
                    List<Integer> tmpListId = new ArrayList<Integer>();
                    List<Integer> tmpRuleId = new ArrayList<Integer>();
                    List<Integer> tmpMatchIndex = aRule.getTopMatchedFieldSelectionSymbol();
                    List<Integer> tmpActionIndex = aRule.getTopAclActionList();
                    List<Integer> tmpActionValue = aRule.getTopAclActionParameterValueList();
                    List<List<Integer>> tmpRule = new ArrayList<List<Integer>>();
                    tmpListId.add(aRule.getTopAclRuleListIndex());
                    tmpRuleId.add(aRule.getTopAclRuleIndex());
                    tmpRuleId.add(tmpI);
                    tmpRule.add(tmpListId);
                    tmpRule.add(tmpRuleId);
                    tmpRule.add(tmpMatchIndex);
                    tmpRule.add(tmpActionIndex);
                    tmpRule.add(tmpActionValue);
                    allRuleList.add(tmpRule);
                    List<List<String>> tmpMactchValue = new ArrayList<List<String>>();
                    for (int e = 0; e < 19; e++) {
                        List<String> temp = new ArrayList<String>();
                        tmpMactchValue.add(temp);
                    }
                    tmpMactchValue.get(17).add(aRule.getTopAclRuleListIndex().toString());
                    tmpMactchValue.get(17).add(aRule.getTopAclRuleIndex().toString());
                    tmpMactchValue.get(17).add("" + tmpI);
                    if (tmpMatchIndex != null && tmpMatchIndex.size() != 0) {
                        for (Integer tmpNo : tmpMatchIndex) {
                            switch (tmpNo) {
                            case 0:
                                if (aRule.getTopMatchedSrcMac() != null && aRule.getTopMatchedSrcMacMask() != null) {
                                    tmpMactchValue.get(0).add(aRule.getTopMatchedSrcMac().toString());
                                    tmpMactchValue.get(0).add(aRule.getTopMatchedSrcMacMask().toString());
                                } else {
                                    tmpMactchValue = matchParamError(tmpMactchValue, tmpI, tmpNo, 0);
                                }
                                break;
                            case 1:
                                if (aRule.getTopMatchedDstMac() != null && aRule.getTopMatchedDstMacMask() != null) {
                                    tmpMactchValue.get(1).add(aRule.getTopMatchedDstMac().toString());
                                    tmpMactchValue.get(1).add(aRule.getTopMatchedDstMacMask().toString());
                                } else {
                                    tmpMactchValue = matchParamError(tmpMactchValue, tmpI, tmpNo, 1);
                                }
                                break;
                            case 2:
                                if (aRule.getTopMatchedStartSVid() != null && aRule.getTopMatchedEndSVid() != null) {
                                    tmpMactchValue.get(2).add(aRule.getTopMatchedStartSVid().toString());
                                    tmpMactchValue.get(2).add(aRule.getTopMatchedEndSVid().toString());
                                } else {
                                    tmpMactchValue = matchParamError(tmpMactchValue, tmpI, tmpNo, 2);
                                }
                                break;
                            case 3:
                                if (aRule.getTopMatchedStartCVid() != null && aRule.getTopMatchedEndCVid() != null) {
                                    tmpMactchValue.get(3).add(aRule.getTopMatchedStartCVid().toString());
                                    tmpMactchValue.get(3).add(aRule.getTopMatchedEndCVid().toString());
                                } else {
                                    tmpMactchValue = matchParamError(tmpMactchValue, tmpI, tmpNo, 3);
                                }
                                break;
                            case 4:
                                if (aRule.getTopMatchedOuterCos() != null) {
                                    tmpMactchValue.get(4).add(aRule.getTopMatchedOuterCos().toString());
                                    tmpMactchValue.get(4).add("noData");
                                } else {
                                    tmpMactchValue = matchParamError(tmpMactchValue, tmpI, tmpNo, 4);
                                }
                                break;
                            case 5:
                                if (aRule.getTopMatchedInnerCos() != null) {
                                    tmpMactchValue.get(5).add(aRule.getTopMatchedInnerCos().toString());
                                    tmpMactchValue.get(5).add("noData");
                                } else {
                                    tmpMactchValue = matchParamError(tmpMactchValue, tmpI, tmpNo, 5);
                                }
                                break;
                            case 6:
                                if (aRule.getTopMatchedOuterTpid() != null) {
                                    tmpMactchValue.get(6).add(aRule.getTopMatchedOuterTpid().toString());
                                    tmpMactchValue.get(6).add("noData");
                                } else {
                                    tmpMactchValue = matchParamError(tmpMactchValue, tmpI, tmpNo, 6);
                                }
                                break;
                            case 7:
                                if (aRule.getTopMatchedInnerTpid() != null) {
                                    tmpMactchValue.get(7).add(aRule.getTopMatchedInnerTpid().toString());
                                    tmpMactchValue.get(7).add("noData");
                                } else {
                                    tmpMactchValue = matchParamError(tmpMactchValue, tmpI, tmpNo, 7);
                                }
                                break;
                            case 8:
                                if (aRule.getTopMatchedEthernetType() != null) {
                                    tmpMactchValue.get(8).add(aRule.getTopMatchedEthernetType().toString());
                                    tmpMactchValue.get(8).add("noData");
                                } else {
                                    tmpMactchValue = matchParamError(tmpMactchValue, tmpI, tmpNo, 8);
                                }
                                break;
                            case 9:
                                if (aRule.getTopMatchedSrcIP() != null && aRule.getTopMatchedSrcIPMask() != null) {
                                    tmpMactchValue.get(9).add(aRule.getTopMatchedSrcIP().toString());
                                    tmpMactchValue.get(9).add(aRule.getTopMatchedSrcIPMask().toString());
                                } else {
                                    tmpMactchValue = matchParamError(tmpMactchValue, tmpI, tmpNo, 9);
                                }
                                break;
                            case 10:
                                if (aRule.getTopMatchedDstIP() != null && aRule.getTopMatchedDstIPMask() != null) {
                                    tmpMactchValue.get(10).add(aRule.getTopMatchedDstIP().toString());
                                    tmpMactchValue.get(10).add(aRule.getTopMatchedDstIPMask().toString());
                                } else {
                                    tmpMactchValue = matchParamError(tmpMactchValue, tmpI, tmpNo, 10);
                                }
                                break;
                            case 11:
                                if (aRule.getTopMatchedIpProtocol() != null) {
                                    tmpMactchValue.get(11).add(aRule.getTopMatchedIpProtocol().toString());
                                    tmpMactchValue.get(11).add("noData");
                                } else {
                                    tmpMactchValue = matchParamError(tmpMactchValue, tmpI, tmpNo, 11);
                                }
                                break;
                            case 12:
                                if (aRule.getTopMatchedL3ProtocolClass() != null) {
                                    tmpMactchValue.get(12).add(aRule.getTopMatchedL3ProtocolClass().toString());
                                    tmpMactchValue.get(12).add("noData");
                                } else {
                                    tmpMactchValue = matchParamError(tmpMactchValue, tmpI, tmpNo, 12);
                                }
                                break;
                            case 13:
                                if (aRule.getTopMatchedDscp() != null) {
                                    tmpMactchValue.get(13).add(aRule.getTopMatchedDscp().toString());
                                    tmpMactchValue.get(13).add("noData");
                                } else {
                                    tmpMactchValue = matchParamError(tmpMactchValue, tmpI, tmpNo, 13);
                                }
                                break;
                            case 14:
                                if (aRule.getTopMatchedTos() != null) {
                                    tmpMactchValue.get(14).add(aRule.getTopMatchedTos().toString());
                                    tmpMactchValue.get(14).add("noData");
                                } else {
                                    tmpMactchValue = matchParamError(tmpMactchValue, tmpI, tmpNo, 14);
                                }
                                break;
                            case 15:
                                if (aRule.getTopMatchedStartSrcPort() != null
                                        && aRule.getTopMatchedEndSrcPort() != null) {
                                    tmpMactchValue.get(15).add(aRule.getTopMatchedStartSrcPort().toString());
                                    tmpMactchValue.get(15).add(aRule.getTopMatchedEndSrcPort().toString());
                                } else {
                                    tmpMactchValue = matchParamError(tmpMactchValue, tmpI, tmpNo, 15);
                                }
                                break;
                            case 16:
                                if (aRule.getTopMatchedStartDstPort() != null
                                        && aRule.getTopMatchedEndDstPort() != null) {
                                    tmpMactchValue.get(16).add(aRule.getTopMatchedStartDstPort().toString());
                                    tmpMactchValue.get(16).add(aRule.getTopMatchedEndDstPort().toString());
                                } else {
                                    tmpMactchValue = matchParamError(tmpMactchValue, tmpI, tmpNo, 1);
                                }
                                break;
                            default:
                                tmpMactchValue = matchParamError(tmpMactchValue, tmpI, tmpNo, 18);
                                break;
                            }
                        }
                    }
                    allRuleStr.add(tmpMactchValue);
                    tmpI++;
                }
            } catch (Exception e) {
                logger.error("create aclRuleList error: " + e.getMessage());
                mes.add("create aclRuleList error!");
            }
            allAclRuleList = JSONArray.fromObject(allRuleList);
            // 冲突验证及标志位构造并传递到界面，结构[[],[2],[1,4],[],[2],[],[],[],[]...]
            // 现在共4001项,第一项不使用
            try {
                isConflictRule(allRuleList, allRuleStr);
            } catch (Exception e) {
                logger.error("isConflictRule error: " + e.getMessage());
                mes.add("isConflictRule error!");
            }
        } else {
            allAclRuleList = JSONArray.fromObject(false);
            // RULL没有时，防止空指针
            aclConflict = JSONArray.fromObject(false);
        }
        if (mes.size() > 0) {
            aclListDescr = JSONArray.fromObject(mes);
        } else {
            aclListDescr = JSONArray.fromObject(false);
        }
        return SUCCESS;
    }

    /**
    * ACL规则的冲突检测，检测两个规则是否冲突
    * 
    * @return
    */
    private List<List<Integer>> isConflictRule(List<List<List<Integer>>> allRuleList,
            List<List<List<String>>> allRuleStr) {
        // TODO 待添加，待进一步测试
        // 冲突验证及标志位构造，结构[[],[2],[1,4],[],[2],[],[],[],[]...] 现在共4001项,第一项不使用
        List<List<Integer>> conflictList = new ArrayList<List<Integer>>();
        for (int i = 0; i < EponConstants.ACL_LIST_MAXNUM + 1; i++) {
            List<Integer> tmpI = new ArrayList<Integer>();
            conflictList.add(tmpI);
        }
        boolean tmpFlag = false;
        // 冲突检测
        if (allRuleList != null && allRuleList.size() != 0) {
            for (List<List<Integer>> aRule : allRuleList) {
                for (List<List<Integer>> bRule : allRuleList) {
                    if (!aRule.get(1).get(1).equals(bRule.get(1).get(1))) {
                        if ((aRule.get(3).contains(1) && !bRule.get(3).contains(1))
                                || (!aRule.get(3).contains(1) && bRule.get(3).contains(1))) {// ACTION若不同（有1为permit，无则为deny）
                            if (matchConfilict(aRule, bRule, allRuleStr)) {
                                if (!conflictList.get(bRule.get(0).get(0)).contains(aRule.get(0).get(0))) {
                                    conflictList.get(bRule.get(0).get(0)).add(aRule.get(0).get(0));
                                    tmpFlag = true;
                                }
                            }
                        }
                    }
                }
            }
        }
        if (tmpFlag) {
            for (int u = 0; u < EponConstants.ACL_LIST_MAXNUM + 1; u++) {
                if (conflictList.get(u).contains(u)) {
                    conflictList.get(u).remove(conflictList.get(u).indexOf(u));
                }
            }
            aclConflict = JSONArray.fromObject(conflictList);
        } else {
            aclConflict = JSONArray.fromObject(false);
        }
        return conflictList;
    }

    // isConflictRule的子方法，判断match的冲突
    private Boolean matchConfilict(List<List<Integer>> aRule, List<List<Integer>> bRule,
            List<List<List<String>>> allRuleStr) {
        /*
         * if(aRule.get(2).size() == 0 || bRule.get(2).size() == 0){ return
         * true; }
         */
        for (Integer tmpA : aRule.get(2)) {
            if (bRule.get(2).contains(tmpA)) {
                if (tmpA == 9 || tmpA == 10) {// IP
                    String tmpS1 = allRuleStr.get(aRule.get(1).get(1)).get(tmpA).get(0);// a的IP
                    String tmpS2 = allRuleStr.get(aRule.get(1).get(1)).get(tmpA).get(1);// a的mask
                    String tmpS3 = allRuleStr.get(bRule.get(1).get(1)).get(tmpA).get(0);// b的IP
                    String tmpS4 = allRuleStr.get(bRule.get(1).get(1)).get(tmpA).get(1);// b的mask
                    if (tmpS2.equals(tmpS4) && IpUtils.isAtMask(tmpS2, tmpS1, tmpS3)) {
                        return true;
                    }
                } else if (tmpA == 2 || tmpA == 15 || tmpA == 16) {// 范围匹配
                    Integer tmpI1 = Integer.parseInt(allRuleStr.get(aRule.get(1).get(1)).get(tmpA).get(0));// START
                    Integer tmpI2 = Integer.parseInt(allRuleStr.get(aRule.get(1).get(1)).get(tmpA).get(1));// END
                    Integer tmpI3 = Integer.parseInt(allRuleStr.get(bRule.get(1).get(1)).get(tmpA).get(0));// START
                    Integer tmpI4 = Integer.parseInt(allRuleStr.get(bRule.get(1).get(1)).get(tmpA).get(1));// END
                    if (tmpI1 <= tmpI4 && tmpI2 >= tmpI3) {
                        return true;
                    }
                } else if (tmpA == 0 || tmpA == 1 || tmpA == 3 || tmpA == 4 || tmpA == 5 || tmpA == 8 || tmpA == 11
                        || tmpA == 12 || tmpA == 13 || tmpA == 14) {// 一般的匹配
                    if (allRuleStr.get(aRule.get(1).get(1)).get(tmpA).get(0)
                            .equals(allRuleStr.get(bRule.get(1).get(1)).get(tmpA).get(0))
                            && allRuleStr.get(aRule.get(1).get(1)).get(tmpA).get(1)
                                    .equals(allRuleStr.get(bRule.get(1).get(1)).get(tmpA).get(1))) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private List<List<String>> matchParamError(List<List<String>> tmpMactchValue, Integer tmpI, Integer tmpNo,
            Integer index) {
        tmpMactchValue.get(index).add("wrongData:ACLRule" + tmpI + "match" + tmpNo);
        tmpMactchValue.get(index).add("wrongData:ACLRule" + tmpI + "match" + tmpNo);
        return tmpMactchValue;
    }

    public Integer getAclIndex() {
        return aclIndex;
    }

    public void setAclIndex(Integer aclIndex) {
        this.aclIndex = aclIndex;
    }

    public Integer getRuleNo() {
        return ruleNo;
    }

    public void setRuleNo(Integer ruleNo) {
        this.ruleNo = ruleNo;
    }

}
