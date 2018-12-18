/***********************************************************************
\ * $Id: OltAclRuleAction.java,v1.0 2013-10-25 下午5:28:16 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.acl.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.topvision.ems.epon.acl.domain.AclRuleTable;
import com.topvision.ems.epon.olt.domain.OltSniAttribute;
import com.topvision.ems.epon.olt.service.OltSniService;
import com.topvision.framework.annotation.OperationLogProperty;
import com.topvision.framework.common.MacUtils;
import com.topvision.framework.exception.engine.SnmpException;
import com.topvision.platform.domain.OperationLog;
import com.topvision.platform.domain.UserContext;

/**
 * @author Bravin
 * @created @2013-10-25-下午5:28:16
 *
 */
@Controller("oltAclRuleAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class OltAclRuleAction extends OltAclAction {
    private static final long serialVersionUID = -8357611885946696262L;
    private final Logger logger = LoggerFactory.getLogger(OltAclRuleAction.class);
    @Autowired
    private OltSniService oltSniService;
    private Integer aclIndex;
    private Integer ruleNo;
    private Integer aclRuleIndex;

    /**
     * 加载一个ACL列表中的规则
     * @return
     * @throws Exception
     */
    public String loadAclRule() throws Exception {
        List<AclRuleTable> aclRuleParamTmp = oltAclService.getAclRuleList(entityId, aclIndex);
        //add by fanzidong,需要在展示前格式化MAC地址
        UserContext uc = (UserContext) super.getSession().get(UserContext.KEY);
        String displayRule = uc.getMacDisplayStyle();
        for (AclRuleTable aclRuleTable : aclRuleParamTmp) {
            String formatedSrcMac = MacUtils.convertMacToDisplayFormat(aclRuleTable.getTopMatchedSrcMac(), displayRule);
            aclRuleTable.setTopMatchedSrcMac(formatedSrcMac);
            String formatedDestMac = MacUtils
                    .convertMacToDisplayFormat(aclRuleTable.getTopMatchedDstMac(), displayRule);
            aclRuleTable.setTopMatchedDstMac(formatedDestMac);
        }
        logger.debug("aclRuleParam:{}", aclRuleParamTmp);
        writeDataToAjax(JSONArray.fromObject(aclRuleParamTmp));
        return NONE;
    }

    /**
     * 添加ACL规则
     * 
     * @return
     * @throws IOException
     */
    @OperationLogProperty(actionName = "oltAclAction", operationName = "addAclRule")
    public String addAclRule() throws IOException {
        String message;
        AclRuleTable aclRuleTable = new AclRuleTable();
        aclRuleTable.setEntityId(entityId);
        aclRuleTable.setTopAclRuleListIndex(aclIndex);
        aclRuleTable.setTopAclRuleIndex(aclRuleIndex);
        List<Integer> tempSelection = new ArrayList<Integer>();
        //可以不用配置任何规则
        if (topMatchedFieldSelectionSymbol != null && !"".equals(topMatchedFieldSelectionSymbol)) {
            topMatchedFieldSelectionSymbol = topMatchedFieldSelectionSymbol.replace(" ", "");
            String[] tempArray = topMatchedFieldSelectionSymbol.split(",");
            for (String temp : tempArray) {
                tempSelection.add(Integer.parseInt(temp));
            }
        }
        /*
        if (topMatchedFieldSelectionSymbol.length() == 1) {
            tempSelection.add(Integer.parseInt(topMatchedFieldSelectionSymbol));
        } else if (topMatchedFieldSelectionSymbol.length() == 0) {
            // 无match项的情况
        } else {
            topMatchedFieldSelectionSymbol = topMatchedFieldSelectionSymbol.replace(" ", "");
            for (String temp : topMatchedFieldSelectionSymbol.split(",")) {
                tempSelection.add(Integer.parseInt(temp));
            }
        }
        */
        aclRuleTable.setTopMatchedFieldSelectionSymbol(tempSelection);
        String[] fieldSelection = matchParasJsonStr.split(",");
        for (Integer index : tempSelection) {
            switch (index) {
            case 0:
                String temp0 = fieldSelection[0];
                String temp1 = fieldSelection[1];
                Long srcMacTmp = new MacUtils(temp0.split("_")[1]).longValue();
                aclRuleTable.setTopMatchedSrcMacLong(srcMacTmp);
                Long srcMacTmpMask = new MacUtils(temp1.split("_")[1]).longValue();
                aclRuleTable.setTopMatchedSrcMacMaskLong(srcMacTmpMask);
                break;
            case 1:
                String temp2 = fieldSelection[2];
                String temp3 = fieldSelection[3];
                Long dstMacTmp = new MacUtils(temp2.split("_")[1]).longValue();
                aclRuleTable.setTopMatchedDstMacLong(dstMacTmp);
                Long dstMacTmpMask = new MacUtils(temp3.split("_")[1]).longValue();
                aclRuleTable.setTopMatchedDstMacMaskLong(dstMacTmpMask);
                break;
            case 2:
                String temp4 = fieldSelection[4];
                String temp5 = fieldSelection[5];
                aclRuleTable.setTopMatchedStartSVid(Integer.parseInt(temp4.split("_")[1]));
                aclRuleTable.setTopMatchedEndSVid(Integer.parseInt(temp5.split("_")[1]));
                break;
            case 3:
                String temp6 = fieldSelection[6];
                aclRuleTable.setTopMatchedStartCVid(Integer.parseInt(temp6.split("_")[1]));
                aclRuleTable.setTopMatchedEndCVid(Integer.parseInt(temp6.split("_")[1]));
                break;
            case 4:
                String temp7 = fieldSelection[7];
                aclRuleTable.setTopMatchedOuterCos(Integer.parseInt(temp7.split("_")[1]));
                break;
            case 5:
                String temp8 = fieldSelection[8];
                aclRuleTable.setTopMatchedInnerCos(Integer.parseInt(temp8.split("_")[1]));
                break;
            case 6:
                String temp9 = fieldSelection[9];
                aclRuleTable.setTopMatchedOuterTpid(Integer.parseInt(temp9.split("_")[1].substring(2), 16));
                break;
            case 7:
                String temp10 = fieldSelection[10];
                aclRuleTable.setTopMatchedInnerTpid(Integer.parseInt(temp10.split("_")[1].substring(2), 16));
                break;
            case 8:
                String temp11 = fieldSelection[11];
                aclRuleTable.setTopMatchedEthernetType(Integer.parseInt(temp11.split("_")[1]));
                break;
            case 9:
                String temp12 = fieldSelection[12];
                String temp13 = fieldSelection[13];
                aclRuleTable.setTopMatchedSrcIP(temp12.split("_")[1]);
                aclRuleTable.setTopMatchedSrcIPMask(temp13.split("_")[1]);
                break;
            case 10:
                String temp14 = fieldSelection[14];
                String temp15 = fieldSelection[15];
                aclRuleTable.setTopMatchedDstIP(temp14.split("_")[1]);
                aclRuleTable.setTopMatchedDstIPMask(temp15.split("_")[1]);
                break;
            case 11:
                String temp16 = fieldSelection[16];
                aclRuleTable.setTopMatchedL3ProtocolClass(Integer.parseInt(temp16.split("_")[1]));
                break;
            case 12:
                String temp17 = fieldSelection[17];
                aclRuleTable.setTopMatchedIpProtocol(Integer.parseInt(temp17.split("_")[1]));
                break;
            case 13:
                String temp18 = fieldSelection[18];
                aclRuleTable.setTopMatchedDscp(Integer.parseInt(temp18.split("_")[1]));
                break;
            case 14:
                String temp19 = fieldSelection[19];
                aclRuleTable.setTopMatchedTos(Integer.parseInt(temp19.split("_")[1]));
                break;
            case 15:
                String temp20 = fieldSelection[20];
                String temp21 = fieldSelection[21];
                aclRuleTable.setTopMatchedStartSrcPort(Integer.parseInt(temp20.split("_")[1]));
                aclRuleTable.setTopMatchedEndSrcPort(Integer.parseInt(temp21.split("_")[1]));
                break;
            case 16:
                String temp22 = fieldSelection[22];
                String temp23 = fieldSelection[23];
                aclRuleTable.setTopMatchedStartDstPort(Integer.parseInt(temp22.split("_")[1]));
                aclRuleTable.setTopMatchedEndDstPort(Integer.parseInt(temp23.split("_")[1]));
                break;
            default:
                break;
            }
        }
        List<Integer> aclActionTempList = new ArrayList<Integer>();
        List<Integer> aclActionParamList = new ArrayList<Integer>();
        aclActionParamList.add(0);// 端口限速
        aclActionParamList.add(0);// slot
        aclActionParamList.add(0);// sni
        aclActionParamList.add(0);// onu
        aclActionParamList.add(0);// uni
        aclActionParamList.add(0);// 队列Id
        aclActionParamList.add(0);// inner CoS
        aclActionParamList.add(0);// DSCP
        aclActionParamList.add(0);// TOS
        aclActionParamList.add(0);// SVLAN ID
        aclActionParamList.add(0);// SCOS
        aclActionParamList.add(0);// TPID 0x8100
        aclActionParamList.add(0);// SVLAN 的 优先级
        String tempString = aclAction.replace(" ", "");

        String tempParaString = aclActionPara.replace(" ", "");
        String[] para = tempParaString.split(",");
        for (String index : tempString.split("_")) {
            if (index.equals("0")) {
                aclActionTempList.add(0);
            }
            if (index.equals("1")) {
                aclActionTempList.add(1);
            }
            if (index.equals("2")) {
                aclActionTempList.add(2);
                aclActionParamList.set(0, Integer.parseInt(para[0]));
            }
            if (index.equals("3")) {
                aclActionTempList.add(3);
                aclActionParamList.set(1, Integer.parseInt(para[1].split("/")[0]));
                aclActionParamList.set(2, Integer.parseInt(para[1].split("/")[1]));
            }
            if (index.equals("4")) {
                aclActionTempList.add(4);
                aclActionParamList.set(5, Integer.parseInt(para[2]));
            }
            if (index.equals("5")) {
                aclActionTempList.add(5);
                aclActionParamList.set(6, Integer.parseInt(para[3]));
            }
            if (index.equals("6")) {
                aclActionTempList.add(6);
                aclActionParamList.set(7, Integer.parseInt(para[4]));
            }
            if (index.equals("7")) {
                aclActionTempList.add(7);
                aclActionParamList.set(8, Integer.parseInt(para[5]));
            }
            if (index.equals("8")) {
                aclActionTempList.add(8);
                aclActionParamList.set(9, Integer.parseInt(para[6]));
                aclActionParamList.set(12, Integer.parseInt(para[9]));
            }
            if (index.equals("9")) {
                aclActionTempList.add(9);
                aclActionParamList.set(10, Integer.parseInt(para[7]));
            }
            if (index.equals("10")) {
                aclActionTempList.add(10);
                aclActionParamList.set(11, Integer.parseInt(para[8], 16));
            }
            if (index.equals("11")) {
                aclActionTempList.add(11);
            }
            if (index.equals("12")) {
                aclActionTempList.add(12);
            }
        }
        aclRuleTable.setTopAclActionList(aclActionTempList);
        aclRuleTable.setTopAclActionParameterValueList(aclActionParamList);
        try {
            oltAclService.addAclRuleList(aclRuleTable);
            message = "success";
            operationResult = OperationLog.SUCCESS;
        } catch (SnmpException e) {
            message = "fail";
            operationResult = OperationLog.FAILURE;
            logger.debug("addAclRule error:{}", e);
        }
        // 返回刷新历史性能成功与否结果
        writeDataToAjax(message);
        return NONE;
    }

    /**
    * 修改ACL规则
    * 
    * @return
    * @throws IOException
    */
    @OperationLogProperty(actionName = "oltAclRuleAction", operationName = "modifyAclRule")
    public String modifyAclRule() throws IOException {
        String message;
        AclRuleTable aclRuleTable = new AclRuleTable();
        aclRuleTable.setEntityId(entityId);
        aclRuleTable.setTopAclRuleListIndex(aclIndex);
        aclRuleTable.setTopAclRuleIndex(aclRuleIndex);
        List<Integer> tempSelection = new ArrayList<Integer>();
        if (topMatchedFieldSelectionSymbol.length() == 1) {
            tempSelection.add(Integer.parseInt(topMatchedFieldSelectionSymbol));
        } else if (topMatchedFieldSelectionSymbol.length() == 0) {
            // 无match项的情况
        } else {
            topMatchedFieldSelectionSymbol = topMatchedFieldSelectionSymbol.replace(" ", "");
            for (String temp : topMatchedFieldSelectionSymbol.split(",")) {
                tempSelection.add(Integer.parseInt(temp));
            }
        }
        aclRuleTable.setTopMatchedFieldSelectionSymbol(tempSelection);
        String[] fieldSelection = matchParasJsonStr.split(",");
        for (Integer index : tempSelection) {
            switch (index) {
            case 0:
                String temp0 = fieldSelection[0];
                String temp1 = fieldSelection[1];
                Long srcMacTmp = new MacUtils(temp0.split("_")[1]).longValue();
                aclRuleTable.setTopMatchedSrcMacLong(srcMacTmp);
                Long srcMacTmpMask = new MacUtils(temp1.split("_")[1]).longValue();
                aclRuleTable.setTopMatchedSrcMacMaskLong(srcMacTmpMask);
                break;
            case 1:
                String temp2 = fieldSelection[2];
                String temp3 = fieldSelection[3];
                Long dstMacTmp = new MacUtils(temp2.split("_")[1]).longValue();
                aclRuleTable.setTopMatchedDstMacLong(dstMacTmp);
                Long dstMacTmpMask = new MacUtils(temp3.split("_")[1]).longValue();
                aclRuleTable.setTopMatchedDstMacMaskLong(dstMacTmpMask);
                break;
            case 2:
                String temp4 = fieldSelection[4];
                String temp5 = fieldSelection[5];
                aclRuleTable.setTopMatchedStartSVid(Integer.parseInt(temp4.split("_")[1]));
                aclRuleTable.setTopMatchedEndSVid(Integer.parseInt(temp5.split("_")[1]));
                break;
            case 3:
                String temp6 = fieldSelection[6];
                aclRuleTable.setTopMatchedStartCVid(Integer.parseInt(temp6.split("_")[1]));
                aclRuleTable.setTopMatchedEndCVid(Integer.parseInt(temp6.split("_")[1]));
                break;
            case 4:
                String temp7 = fieldSelection[7];
                aclRuleTable.setTopMatchedOuterCos(Integer.parseInt(temp7.split("_")[1]));
                break;
            case 5:
                String temp8 = fieldSelection[8];
                aclRuleTable.setTopMatchedInnerCos(Integer.parseInt(temp8.split("_")[1]));
                break;
            case 6:
                String temp9 = fieldSelection[9];
                aclRuleTable.setTopMatchedOuterTpid(Integer.parseInt(temp9.split("_")[1].substring(2), 16));
                break;
            case 7:
                String temp10 = fieldSelection[10];
                aclRuleTable.setTopMatchedInnerTpid(Integer.parseInt(temp10.split("_")[1].substring(2), 16));
                break;
            case 8:
                String temp11 = fieldSelection[11];
                aclRuleTable.setTopMatchedEthernetType(Integer.parseInt(temp11.split("_")[1]));
                break;
            case 9:
                String temp12 = fieldSelection[12];
                String temp13 = fieldSelection[13];
                aclRuleTable.setTopMatchedSrcIP(temp12.split("_")[1]);
                aclRuleTable.setTopMatchedSrcIPMask(temp13.split("_")[1]);
                break;
            case 10:
                String temp14 = fieldSelection[14];
                String temp15 = fieldSelection[15];
                aclRuleTable.setTopMatchedDstIP(temp14.split("_")[1]);
                aclRuleTable.setTopMatchedDstIPMask(temp15.split("_")[1]);
                break;
            case 11:
                String temp16 = fieldSelection[16];
                aclRuleTable.setTopMatchedL3ProtocolClass(Integer.parseInt(temp16.split("_")[1]));
                break;
            case 12:
                String temp17 = fieldSelection[17];
                aclRuleTable.setTopMatchedIpProtocol(Integer.parseInt(temp17.split("_")[1]));
                break;
            case 13:
                String temp18 = fieldSelection[18];
                aclRuleTable.setTopMatchedDscp(Integer.parseInt(temp18.split("_")[1]));
                break;
            case 14:
                String temp19 = fieldSelection[19];
                aclRuleTable.setTopMatchedTos(Integer.parseInt(temp19.split("_")[1]));
                break;
            case 15:
                String temp20 = fieldSelection[20];
                String temp21 = fieldSelection[21];
                aclRuleTable.setTopMatchedStartSrcPort(Integer.parseInt(temp20.split("_")[1]));
                aclRuleTable.setTopMatchedEndSrcPort(Integer.parseInt(temp21.split("_")[1]));
                break;
            case 16:
                String temp22 = fieldSelection[22];
                String temp23 = fieldSelection[23];
                aclRuleTable.setTopMatchedStartDstPort(Integer.parseInt(temp22.split("_")[1]));
                aclRuleTable.setTopMatchedEndDstPort(Integer.parseInt(temp23.split("_")[1]));
                break;
            default:
                break;
            }
        }
        List<Integer> aclActionTempList = new ArrayList<Integer>();
        List<Integer> aclActionParamList = new ArrayList<Integer>();
        aclActionParamList.add(0);// 端口限速
        aclActionParamList.add(0);// slot
        aclActionParamList.add(0);// sni
        aclActionParamList.add(0);// onu
        aclActionParamList.add(0);// uni
        aclActionParamList.add(0);// 队列Id
        aclActionParamList.add(0);// inner CoS
        aclActionParamList.add(0);// DSCP
        aclActionParamList.add(0);// TOS
        aclActionParamList.add(0);// SVLAN ID
        aclActionParamList.add(0);// SCOS
        aclActionParamList.add(0);// TPID 0x8100
        aclActionParamList.add(0);// SVLAN 的 优先级
        String tempString = aclAction.replace(" ", "");

        String tempParaString = aclActionPara.replace(" ", "");
        String[] para = tempParaString.split(",");
        for (String index : tempString.split("_")) {
            if (index.equals("0")) {
                aclActionTempList.add(0);
            }
            if (index.equals("1")) {
                aclActionTempList.add(1);
            }
            if (index.equals("2")) {
                aclActionTempList.add(2);
                aclActionParamList.set(0, Integer.parseInt(para[0]));
            }
            if (index.equals("3")) {
                aclActionTempList.add(3);
                aclActionParamList.set(1, Integer.parseInt(para[1].split("/")[0]));
                aclActionParamList.set(2, Integer.parseInt(para[1].split("/")[1]));
            }
            if (index.equals("4")) {
                aclActionTempList.add(4);
                aclActionParamList.set(5, Integer.parseInt(para[2]));
            }
            if (index.equals("5")) {
                aclActionTempList.add(5);
                aclActionParamList.set(6, Integer.parseInt(para[3]));
            }
            if (index.equals("6")) {
                aclActionTempList.add(6);
                aclActionParamList.set(7, Integer.parseInt(para[4]));
            }
            if (index.equals("7")) {
                aclActionTempList.add(7);
                aclActionParamList.set(8, Integer.parseInt(para[5]));
            }
            if (index.equals("8")) {
                aclActionTempList.add(8);
                aclActionParamList.set(9, Integer.parseInt(para[6]));
                aclActionParamList.set(12, Integer.parseInt(para[9]));
            }
            if (index.equals("9")) {
                aclActionTempList.add(9);
                aclActionParamList.set(10, Integer.parseInt(para[7]));
            }
            if (index.equals("10")) {
                aclActionTempList.add(10);
                aclActionParamList.set(11, Integer.parseInt(para[8], 16));
            }
            if (index.equals("11")) {
                aclActionTempList.add(11);
            }
            if (index.equals("12")) {
                aclActionTempList.add(12);
            }
        }
        aclRuleTable.setTopAclActionList(aclActionTempList);
        aclRuleTable.setTopAclActionParameterValueList(aclActionParamList);
        try {
            oltAclService.modifyAclRuleList(aclRuleTable);
            message = "success";
            operationResult = OperationLog.SUCCESS;
        } catch (SnmpException e) {
            message = e.getMessage();
            operationResult = OperationLog.FAILURE;
            logger.info("modifyAclRule error:{}", e);
        }
        writeDataToAjax(message);
        return NONE;
    }

    /**
    * 删除ACL规则
    * 
    * @return
    */
    @OperationLogProperty(actionName = "oltAclRuleAction", operationName = "deletAclRule")
    public String deletAclRule() throws Exception {
        String message;
        try {
            oltAclService.deleteAclRuleList(entityId, aclIndex, ruleNo);
            operationResult = OperationLog.SUCCESS;
            message = "success";
        } catch (SnmpException e) {
            logger.debug("deletAclRule error: {}", e);
            operationResult = OperationLog.FAILURE;
            message = e.getMessage();
        }
        writeDataToAjax(message);
        return NONE;
    }

    /**
    * 显示ACL规则
    * 
    * @return
    * @throws IOException
    */
    public String showAclRule() throws IOException {
        if (modifyFlag == 1 || modifyFlag == 9) {// 0:新建 1:修改 9:查看
            AclRuleTable aclRuleTable = new AclRuleTable();
            aclRuleTable = oltAclService.getAclRule(entityId, aclIndex, aclRuleIndex);
            aclRuleParam = JSONArray.fromObject(aclRuleTable);
        } else {
            aclRuleParam = JSONArray.fromObject(false);
        }
        List<AclRuleTable> aclRuleListTmp = oltAclService.getAclRuleList(entityId, aclIndex);
        if (aclRuleListTmp.size() == 0 || aclRuleListTmp == null) {
            aclRuleListParam = JSONArray.fromObject(false);
        } else {
            //add by fanzidong,需要在展示前格式化MAC地址
            UserContext uc = (UserContext) super.getSession().get(UserContext.KEY);
            String displayRule = uc.getMacDisplayStyle();
            for (AclRuleTable aclRuleTable : aclRuleListTmp) {
                String formatedSrcMac = MacUtils.convertMacToDisplayFormat(aclRuleTable.getTopMatchedSrcMac(),
                        displayRule);
                aclRuleTable.setTopMatchedSrcMac(formatedSrcMac);
                String formatedDestMac = MacUtils.convertMacToDisplayFormat(aclRuleTable.getTopMatchedDstMac(),
                        displayRule);
                aclRuleTable.setTopMatchedDstMac(formatedDestMac);
            }
            aclRuleListParam = JSONArray.fromObject(aclRuleListTmp);
        }
        // 获取可选镜像端口数据
        List<OltSniAttribute> list = oltSniService.getAllSniList(entityId);
        if (list.size() == 0 || list == null) {
            slotSniObject = JSONArray.fromObject(false);
        } else {
            slotSniObject = JSONArray.fromObject(list);
        }
        return SUCCESS;
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

    public Integer getAclRuleIndex() {
        return aclRuleIndex;
    }

    public void setAclRuleIndex(Integer aclRuleIndex) {
        this.aclRuleIndex = aclRuleIndex;
    }

}
