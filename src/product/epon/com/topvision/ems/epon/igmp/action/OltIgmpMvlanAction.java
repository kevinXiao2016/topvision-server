/***********************************************************************
 * $Id: OltIgmpAction.java,v1.0 2013-10-25 下午4:40:40 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.igmp.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.topvision.ems.epon.exception.AddIgmpMvlanException;
import com.topvision.ems.epon.exception.DeleteIgmpMvlanException;
import com.topvision.ems.epon.exception.ModifyIgmpMvlanInfoException;
import com.topvision.ems.epon.exception.RefreshIgmpControlledMulticastPackageTableException;
import com.topvision.ems.epon.igmp.domain.IgmpControlledMulticastPackageTable;
import com.topvision.ems.epon.igmp.domain.IgmpForwardingTable;
import com.topvision.ems.epon.igmp.domain.IgmpMcOnuVlanTransTable;
import com.topvision.ems.epon.igmp.domain.IgmpProxyParaTable;
import com.topvision.ems.epon.igmp.service.OltIgmpService;
import com.topvision.ems.epon.olt.action.AbstractEponAction;
import com.topvision.framework.annotation.OperationLogProperty;
import com.topvision.framework.common.MacUtils;
import com.topvision.platform.domain.OperationLog;
import com.topvision.platform.domain.UserContext;

/**
 * @author flack
 * @created @2013-10-25-下午4:40:40
 *
 */
@Controller("oltIgmpMvlanAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class OltIgmpMvlanAction extends AbstractEponAction {
    private static final long serialVersionUID = 1806993920831529221L;
    private final Logger logger = LoggerFactory.getLogger(OltIgmpSnoopingAction.class);
    @Autowired
    private OltIgmpService oltIgmpService;
    private Integer mvlanId;
    private Integer modifyFlag;
    private String mvlanName;
    private String mvlanChName;
    private String mvlanProxyList;
    private Integer maxProxyNum;
    private Integer authority;
    private Integer singlePreviewTime;
    private Integer previewTotalTime;
    private Integer resetPreviewTime;
    private Integer previewCount;
    private Integer previewInterval;
    private String igmpProxyParaTables;
    private Long uniIndex;
    private Integer transId;
    private List<Integer> transOldIdList;
    private List<Integer> transNewIdList;
    private JSONArray transList = new JSONArray();
    private JSONArray cmPackageObject = new JSONArray();
    private JSONArray actionPonListObject = new JSONArray();
    private JSONArray uniMvlanList = new JSONArray();
    private JSONArray mvlanList = new JSONArray();

    /**
     * 跳转新增组播组页面
     * 
     * @return
     */
    public String addMvlanJsp() {
        Map<String, Object> json = new HashMap<String, Object>();
        List<IgmpProxyParaTable> groups = new ArrayList<IgmpProxyParaTable>();
        try {
            groups = oltIgmpService.getIgmpProxyInfo(entityId);
        } catch (Exception e) {
            logger.debug("getIgmpProxyInfo error: {}", e);
        }
        json.put("data", groups);
        igmpProxyParaTables = JSONObject.fromObject(json).toString();
        return SUCCESS;
    }

    /**
     * 显示IGMP可控组播模板信息页面
     */
    public String showIgmpMvlan() {
        List<IgmpControlledMulticastPackageTable> cmPackageList = new ArrayList<IgmpControlledMulticastPackageTable>();
        try {
            cmPackageList = oltIgmpService.getIgmpMvlanInfo(entityId);
        } catch (Exception e) {
            logger.debug("getIgmpMvlanInfo error: {}", e);
        }
        cmPackageObject = JSONArray.fromObject(cmPackageList);
        List<IgmpForwardingTable> actionPonList = new ArrayList<IgmpForwardingTable>();
        try {
            actionPonList = oltIgmpService.getIgmpForwardingInfo(entityId);
            //add by fanzidong,需要在展示前格式化MAC地址
            UserContext uc = (UserContext) super.getSession().get(UserContext.KEY);
            String displayRule = uc.getMacDisplayStyle();
            for (IgmpForwardingTable table : actionPonList) {
                String formatedMac = MacUtils.convertMacToDisplayFormat(table.getMacMibIndex(), displayRule);
                table.setMacMibIndex(formatedMac);
            }
        } catch (Exception e) {
            logger.debug("getIgmpForwardingInfo error: {}", e);
        }
        actionPonListObject = JSONArray.fromObject(actionPonList);
        return SUCCESS;
    }

    /**
     * 新增组播组VLAN
     * 
     * @return
     * @throws Exception
     */
    @OperationLogProperty(actionName = "oltIgmpMvlanAction", operationName = "addMvlan${mvlanId}")
    public String addMvlan() throws Exception {
        String result;
        IgmpControlledMulticastPackageTable mvlanPackage = new IgmpControlledMulticastPackageTable();
        mvlanPackage.setCmIndex(mvlanId);
        mvlanPackage.setCmName(mvlanName);
        mvlanPackage.setCmChName(mvlanChName);
        mvlanPackage.setEntityId(entityId);
        List<Integer> cmProxyListNum = new ArrayList<Integer>();
        if (mvlanProxyList.split(",").length > 1) {
            for (String s : mvlanProxyList.split(",")) {
                cmProxyListNum.add(Integer.parseInt(s));
            }
        } else {
            if (mvlanProxyList.length() != 0) {
                cmProxyListNum.add(Integer.parseInt(mvlanProxyList));
            }
        }
        mvlanPackage.setCmProxyListNum(cmProxyListNum);
        mvlanPackage.setMaxRequestChannelNum(maxProxyNum);
        mvlanPackage.setMulticastUserAuthority(authority);
        mvlanPackage.setPreviewCount(previewCount);
        mvlanPackage.setPreviewResetTime(resetPreviewTime);
        mvlanPackage.setSinglePreviewTime(singlePreviewTime);
        mvlanPackage.setTotalPreviewTime(previewTotalTime);
        mvlanPackage.setPreviewInterval(previewInterval);
        try {
            oltIgmpService.addIgmpMvlan(mvlanPackage);
            result = "success";
            operationResult = OperationLog.SUCCESS;
        } catch (AddIgmpMvlanException sce) {
            result = sce.getMessage();
            operationResult = OperationLog.FAILURE;
            logger.debug("addIgmpMvlan error:{}", sce);
        }
        writeDataToAjax(result);
        return NONE;
    }

    /**
     * 修改组播组VLAN
     * 
     * @return
     * @throws Exception
     */
    @OperationLogProperty(actionName = "oltIgmpMvlanAction", operationName = "modifyMvlan${mvlanId}")
    public String modifyMvlan() throws Exception {
        String result;
        IgmpControlledMulticastPackageTable mvlanPackage = new IgmpControlledMulticastPackageTable();
        mvlanPackage.setCmIndex(mvlanId);
        mvlanPackage.setCmName(mvlanName);
        mvlanPackage.setCmChName(mvlanChName);
        List<Integer> cmProxyListNum = new ArrayList<Integer>();
        if (mvlanProxyList.split(",").length > 1) {
            for (String s : mvlanProxyList.split(",")) {
                cmProxyListNum.add(Integer.parseInt(s));
            }
        } else {
            if (mvlanProxyList.length() != 0) {
                cmProxyListNum.add(Integer.parseInt(mvlanProxyList));
            }
        }
        mvlanPackage.setCmProxyListNum(cmProxyListNum);
        mvlanPackage.setEntityId(entityId);
        mvlanPackage.setMaxRequestChannelNum(maxProxyNum);
        mvlanPackage.setMulticastUserAuthority(authority);
        mvlanPackage.setPreviewCount(previewCount);
        mvlanPackage.setPreviewResetTime(resetPreviewTime);
        mvlanPackage.setSinglePreviewTime(singlePreviewTime);
        mvlanPackage.setTotalPreviewTime(previewTotalTime);
        mvlanPackage.setPreviewInterval(previewInterval);
        try {
            oltIgmpService.modifyIgmpMvlanInfo(mvlanPackage);
            result = "success";
            operationResult = OperationLog.SUCCESS;
        } catch (ModifyIgmpMvlanInfoException sce) {
            result = sce.getMessage();
            operationResult = OperationLog.FAILURE;
            logger.debug("modifyIgmpMvlanInfo error:{}", sce);
        }
        writeDataToAjax(result);
        return NONE;
    }

    /**
     * 删除组播组VLAN
     * 
     * @return
     * @throws Exception
     */
    @OperationLogProperty(actionName = "oltIgmpMvlanAction", operationName = "deleteMvlan${mvlanId}")
    public String deleteMvlan() throws Exception {
        String result;
        try {
            oltIgmpService.deleteIgmpMvlan(entityId, mvlanId);
            result = "success";
            operationResult = OperationLog.SUCCESS;
        } catch (DeleteIgmpMvlanException sce) {
            result = sce.getMessage();
            operationResult = OperationLog.FAILURE;
            logger.debug("deleteIgmpMvlan error:{}", sce);
        }
        writeDataToAjax(result);
        return NONE;
    }

    /**
     * 显示IGMP UNI组播模板配置
     * 
     * @return
     * @throws Exception
     */
    public String showIgmpUniMvlan() throws Exception {
        List<String> mvlanListForUni = new ArrayList<String>();
        try {
            List<IgmpControlledMulticastPackageTable> mvlanListTmp = oltIgmpService.getIgmpMvlanInfo(entityId);
            if (mvlanListTmp != null && mvlanListTmp.size() != 0) {
                for (IgmpControlledMulticastPackageTable aMvlan : mvlanListTmp) {
                    mvlanListForUni.add(aMvlan.getCmIndex().toString());
                    mvlanListForUni.add(aMvlan.getCmChName());// 组播组别名
                    mvlanListForUni.add(aMvlan.getCmName());
                    mvlanListForUni.add(aMvlan.getMulticastUserAuthority().toString());
                    mvlanListForUni.add(aMvlan.getCmProxyListNum().toString());
                }
            }
        } catch (Exception e) {
            logger.debug("loadIgmpUniMvlanInfo error: {}", e);
        }
        if (mvlanListForUni.size() > 0) {
            mvlanList = JSONArray.fromObject(mvlanListForUni);
        } else {
            mvlanList = JSONArray.fromObject(false);
        }
        // end of uniMvlanGrid的数据
        // UNI绑定的组播模板的数据
        List<Integer> tmpUniMvlanList = oltIgmpService.getIgmpUniPortInfo(entityId, uniIndex);
        if (tmpUniMvlanList.size() > 0) {
            uniMvlanList = JSONArray.fromObject(tmpUniMvlanList);
        } else {
            uniMvlanList = JSONArray.fromObject(false);
        }
        return SUCCESS;
    }

    /**
     * 从设备获取MVLAN基本配置信息
     * 
     * @return
     * @throws Exception
     */
    @OperationLogProperty(actionName = "oltIgmpMvlanAction", operationName = "refreshMvlan")
    public String refreshMvlan() throws Exception {
        String result;
        try {
            oltIgmpService.refreshIgmpControlledMulticastPackageTable(entityId);
            result = "success";
            operationResult = OperationLog.SUCCESS;
        } catch (RefreshIgmpControlledMulticastPackageTableException sse) {
            result = sse.getMessage();
            logger.debug("refreshIgmpControlledMulticastPackageTable error:{}", sse);
            operationResult = OperationLog.FAILURE;
        }
        writeDataToAjax(result);
        return NONE;
    }

    /**
     * 添加组播VLAN转换规则
     * 
     * @return
     */
    @OperationLogProperty(actionName = "oltIgmpMvlanAction", operationName = "addIgmpVlanTrans${transId}")
    public String addIgmpVlanTrans() throws Exception {
        IgmpMcOnuVlanTransTable table = new IgmpMcOnuVlanTransTable();
        table.setEntityId(entityId);
        table.setTopMcOnuVlanTransIndex(transId);
        table.setTopMcOnuVlanTransOldVidList(transOldIdList);
        table.setTopMcOnuVlanTransNewVidList(transNewIdList);
        String message = "success";
        try {
            oltIgmpService.addIgmpVlanTrans(table);
            operationResult = OperationLog.SUCCESS;
        } catch (Exception e) {
            message = e.getMessage();
            logger.debug("addIgmpVlanTrans error: {}", e);
            operationResult = OperationLog.FAILURE;
        }
        writeDataToAjax(message);
        return NONE;
    }

    /**
     * 修改组播VLAN转换规则
     * 
     * @return
     */
    @OperationLogProperty(actionName = "oltIgmpMvlanAction", operationName = "modifyIgmpVlanTrans${transId}")
    public String modifyIgmpVlanTrans() throws Exception {
        IgmpMcOnuVlanTransTable table = new IgmpMcOnuVlanTransTable();
        table.setEntityId(entityId);
        table.setTopMcOnuVlanTransIndex(transId);
        table.setTopMcOnuVlanTransOldVidList(transOldIdList);
        table.setTopMcOnuVlanTransNewVidList(transNewIdList);
        String message = "success";
        try {
            oltIgmpService.modifyIgmpVlanTrans(table);
            operationResult = OperationLog.SUCCESS;
        } catch (Exception e) {
            message = e.getMessage();
            logger.debug("modifyIgmpVlanTrans error: {}", e);
            operationResult = OperationLog.FAILURE;
        }
        writeDataToAjax(message);
        return NONE;
    }

    /**
     * 删除组播VLAN转换规则
     * 
     * @return
     */
    @OperationLogProperty(actionName = "oltIgmpMvlanAction", operationName = "deleteIgmpVlanTrans${transId}")
    public String deleteIgmpVlanTrans() throws Exception {
        String message = "success";
        try {
            oltIgmpService.deleteIgmpVlanTrans(entityId, transId);
            operationResult = OperationLog.SUCCESS;
        } catch (Exception e) {
            message = e.getMessage();
            logger.debug("deleteIgmpVlanTrans error: {}", e);
            operationResult = OperationLog.FAILURE;
        }
        writeDataToAjax(message);
        return NONE;
    }

    /**
     * 显示IGMP 转换列表
     */
    public String showIgmpTranslation() {
        // transGrid的数据
        List<List<Integer>> transTempList = new ArrayList<List<Integer>>();
        try {
            List<IgmpMcOnuVlanTransTable> transObj = oltIgmpService.getIgmpVlanTrans(entityId);
            if (transObj != null) {
                for (IgmpMcOnuVlanTransTable aTransRule : transObj) {
                    Integer transId = aTransRule.getTopMcOnuVlanTransIndex();
                    List<Integer> transNewList = aTransRule.getTopMcOnuVlanTransNewVidList();
                    List<Integer> transOldList = aTransRule.getTopMcOnuVlanTransOldVidList();
                    List<Integer> tempList = new ArrayList<Integer>();
                    tempList.add(transId);
                    int j = 0;
                    for (Integer anNewId : transNewList) {
                        if (!anNewId.equals(0)) {
                            tempList.add(transOldList.get(j));
                            tempList.add(anNewId);
                        }
                        j++;
                    }
                    transTempList.add(tempList);
                }
            }
        } catch (Exception e) {
            logger.debug("loadIgmpVlanTrans error: {}", e);
        }
        transList = JSONArray.fromObject(transTempList);
        return SUCCESS;
    }

    /**
     * 刷新IGMP转换列表信息
     * 
     * @param entityId
     * @throws IOException 
     */
    public String refreshIgmpTranslation() throws IOException {
        String message = "success";
        try {
            oltIgmpService.refreshIgmpMcOnuVlanTransTable(entityId);
        } catch (Exception e) {
            message = e.getMessage();
            logger.debug("refreshIgmpUni error: {}", e);
        }
        writeDataToAjax(message);
        return NONE;
    }

    public OltIgmpService getOltIgmpService() {
        return oltIgmpService;
    }

    public void setOltIgmpService(OltIgmpService oltIgmpService) {
        this.oltIgmpService = oltIgmpService;
    }

    public JSONArray getCmPackageObject() {
        return cmPackageObject;
    }

    public void setCmPackageObject(JSONArray cmPackageObject) {
        this.cmPackageObject = cmPackageObject;
    }

    public JSONArray getActionPonListObject() {
        return actionPonListObject;
    }

    public void setActionPonListObject(JSONArray actionPonListObject) {
        this.actionPonListObject = actionPonListObject;
    }

    public Integer getMvlanId() {
        return mvlanId;
    }

    public void setMvlanId(Integer mvlanId) {
        this.mvlanId = mvlanId;
    }

    public String getMvlanName() {
        return mvlanName;
    }

    public void setMvlanName(String mvlanName) {
        this.mvlanName = mvlanName;
    }

    public String getMvlanChName() {
        return mvlanChName;
    }

    public void setMvlanChName(String mvlanChName) {
        this.mvlanChName = mvlanChName;
    }

    public String getMvlanProxyList() {
        return mvlanProxyList;
    }

    public void setMvlanProxyList(String mvlanProxyList) {
        this.mvlanProxyList = mvlanProxyList;
    }

    public Integer getMaxProxyNum() {
        return maxProxyNum;
    }

    public void setMaxProxyNum(Integer maxProxyNum) {
        this.maxProxyNum = maxProxyNum;
    }

    public Integer getAuthority() {
        return authority;
    }

    public void setAuthority(Integer authority) {
        this.authority = authority;
    }

    public Integer getSinglePreviewTime() {
        return singlePreviewTime;
    }

    public void setSinglePreviewTime(Integer singlePreviewTime) {
        this.singlePreviewTime = singlePreviewTime;
    }

    public Integer getPreviewTotalTime() {
        return previewTotalTime;
    }

    public void setPreviewTotalTime(Integer previewTotalTime) {
        this.previewTotalTime = previewTotalTime;
    }

    public Integer getResetPreviewTime() {
        return resetPreviewTime;
    }

    public void setResetPreviewTime(Integer resetPreviewTime) {
        this.resetPreviewTime = resetPreviewTime;
    }

    public Integer getPreviewCount() {
        return previewCount;
    }

    public void setPreviewCount(Integer previewCount) {
        this.previewCount = previewCount;
    }

    public Integer getPreviewInterval() {
        return previewInterval;
    }

    public void setPreviewInterval(Integer previewInterval) {
        this.previewInterval = previewInterval;
    }

    public String getIgmpProxyParaTables() {
        return igmpProxyParaTables;
    }

    public void setIgmpProxyParaTables(String igmpProxyParaTables) {
        this.igmpProxyParaTables = igmpProxyParaTables;
    }

    public JSONArray getUniMvlanList() {
        return uniMvlanList;
    }

    public void setUniMvlanList(JSONArray uniMvlanList) {
        this.uniMvlanList = uniMvlanList;
    }

    public JSONArray getMvlanList() {
        return mvlanList;
    }

    public void setMvlanList(JSONArray mvlanList) {
        this.mvlanList = mvlanList;
    }

    public Long getUniIndex() {
        return uniIndex;
    }

    public void setUniIndex(Long uniIndex) {
        this.uniIndex = uniIndex;
    }

    public Integer getTransId() {
        return transId;
    }

    public void setTransId(Integer transId) {
        this.transId = transId;
    }

    public List<Integer> getTransOldIdList() {
        return transOldIdList;
    }

    public void setTransOldIdList(List<Integer> transOldIdList) {
        this.transOldIdList = transOldIdList;
    }

    public List<Integer> getTransNewIdList() {
        return transNewIdList;
    }

    public void setTransNewIdList(List<Integer> transNewIdList) {
        this.transNewIdList = transNewIdList;
    }

    public JSONArray getTransList() {
        return transList;
    }

    public void setTransList(JSONArray transList) {
        this.transList = transList;
    }

    public Integer getModifyFlag() {
        return modifyFlag;
    }

    public void setModifyFlag(Integer modifyFlag) {
        this.modifyFlag = modifyFlag;
    }

}
