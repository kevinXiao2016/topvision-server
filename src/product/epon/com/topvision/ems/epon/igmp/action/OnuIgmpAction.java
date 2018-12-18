/***********************************************************************
 * $Id: OnuIgmpAction.java,v1.0 2013年10月28日 上午9:53:19 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.igmp.action;

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

import com.topvision.ems.epon.igmp.domain.IgmpControlledMulticastPackageTable;
import com.topvision.ems.epon.igmp.domain.IgmpEntityTable;
import com.topvision.ems.epon.igmp.domain.IgmpMcOnuTable;
import com.topvision.ems.epon.igmp.domain.IgmpMcOnuVlanTransTable;
import com.topvision.ems.epon.igmp.domain.IgmpMcUniConfigTable;
import com.topvision.ems.epon.igmp.service.OltIgmpService;
import com.topvision.ems.epon.olt.action.AbstractEponAction;
import com.topvision.framework.annotation.OperationLogProperty;
import com.topvision.framework.utils.EponConstants;
import com.topvision.framework.utils.EponIndex;
import com.topvision.platform.domain.OperationLog;

/**
 * @author Bravin
 * @created @2013年10月28日-上午9:53:19
 *
 */
@Controller("onuIgmpAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class OnuIgmpAction extends AbstractEponAction {
    private static final long serialVersionUID = 2678648562521205424L;
    private final Logger logger = LoggerFactory.getLogger(OnuIgmpAction.class);
    @Autowired
    private OltIgmpService oltIgmpService;
    private Long onuIndex;
    private Integer onuIgmpMode;
    private Integer fastLeaveEnabled;
    private Long uniIndex;
    private String uniPortMvlanListStr;
    private String uniPortMvidList;
    private Integer uniVlanMode;
    private Integer maxGroupNum;
    private Integer transId;
    private JSONArray onuValue = new JSONArray();
    private JSONArray mvlanList = new JSONArray();
    private JSONArray uniMvlanList = new JSONArray();
    private JSONArray uniIgmpValue = new JSONArray();
    private JSONArray uniMvidList = new JSONArray();
    private JSONArray transList = new JSONArray();

    /**
     * 显示ONU侧IGMP配置
     * 
     * @return
     */
    public String showOnuIgmp() {
        List<Integer> onuTempValue = new ArrayList<Integer>();
        try {
            IgmpMcOnuTable table = oltIgmpService.getIgmpMcOnuInfo(entityId, onuIndex);
            IgmpEntityTable igmpGlobal = oltIgmpService.getIgmpGlobalInfo(entityId);
            if (table != null) {
                onuTempValue.add(table.getTopMcOnuMode());
                onuTempValue.add(table.getTopMcOnuFastLeave());
            } else {
                onuTempValue.add(0);
                onuTempValue.add(0);
            }
            if (igmpGlobal != null) {
                onuTempValue.add(igmpGlobal.getIgmpMode());
            } else {
                onuTempValue.add(0);
            }
        } catch (Exception e) {
            logger.debug("loadIgmpOnuInfo error: {}", e);
        }
        // 假数据
        /*
         * onuTempValue.add(1); onuTempValue.add(1); onuTempValue.add(4);
         */
        onuValue = JSONArray.fromObject(onuTempValue);
        return SUCCESS;
    }

    /**
     * 修改ONU可控组播信息
     * 
     * @return
     */
    @OperationLogProperty(actionName = "onuIgmpAction", operationName = "modifyIgmpMcOnuInfo")
    public String modifyIgmpMcOnuInfo() throws Exception {
        IgmpMcOnuTable table = new IgmpMcOnuTable();
        table.setEntityId(entityId);
        table.setOnuIndex(onuIndex);
        table.setTopMcOnuMode(onuIgmpMode);
        table.setTopMcOnuFastLeave(fastLeaveEnabled);
        String message = "success";
        try {
            oltIgmpService.modifyIgmpMcOnuInfo(table);
            source = EponIndex.getOnuStringByIndex(onuIndex).toString();
            operationResult = OperationLog.SUCCESS;
        } catch (Exception e) {
            message = e.getMessage();
            logger.debug("modifyIgmpMcOnuInfo error: {}", e);
            operationResult = OperationLog.FAILURE;
        }
        writeDataToAjax(message);
        return NONE;
    }

    /**
     * 显示UNI口IGMP配置
     */
    public String showUniIgmp() {
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
        mvlanList = JSONArray.fromObject(mvlanListForUni);
        // end of uniMvlanGrid的数据
        // UNI绑定的组播模板的数据
        List<Integer> tmpUniMvlanList = oltIgmpService.getIgmpUniPortInfo(entityId, uniIndex);
        uniMvlanList = JSONArray.fromObject(tmpUniMvlanList);
        // uni的IGMP的数据
        List<Integer> uniMvlanListForUni = new ArrayList<Integer>();
        List<Integer> tempValueList = new ArrayList<Integer>();
        try {
            IgmpMcUniConfigTable igmpMcUniConfig = oltIgmpService.getIgmpMcUniConfig(entityId, uniIndex);
            if (igmpMcUniConfig != null) {
                tempValueList.add(igmpMcUniConfig.getTopMcUniMaxGroupQuantity());
                tempValueList.add(igmpMcUniConfig.getTopMcUniVlanMode());
                if (igmpMcUniConfig.getTopMcUniVlanMode() == 2) {
                    tempValueList.add(igmpMcUniConfig.getTopMcUniVlanTransIdx());
                }
                uniMvlanListForUni = igmpMcUniConfig.getTopMcUniVlanListList();
                while (uniMvlanListForUni.contains(new Integer(0))) {
                    uniMvlanListForUni.remove(new Integer(0));
                }
            }
        } catch (Exception e) {
            logger.debug("loadIgmpUniInfo error: {}", e);
        }
        uniIgmpValue = JSONArray.fromObject(tempValueList);
        uniMvidList = JSONArray.fromObject(uniMvlanListForUni);
        // transGrid的数据
        List<List<Integer>> transTempList = new ArrayList<List<Integer>>();
        try {
            List<IgmpMcOnuVlanTransTable> transObj = oltIgmpService.getIgmpVlanTrans(entityId);
            if (transObj != null && transObj.size() != 0) {
                for (IgmpMcOnuVlanTransTable aTransRule : transObj) {
                    Integer transId = aTransRule.getTopMcOnuVlanTransIndex();
                    List<Integer> transNewList = aTransRule.getTopMcOnuVlanTransNewVidList();
                    List<Integer> transOldList = aTransRule.getTopMcOnuVlanTransOldVidList();
                    List<Integer> tempList = new ArrayList<Integer>();
                    tempList.add(transId);
                    int j = 0;
                    for (Integer anNewId : transNewList) {
                        if (!anNewId.equals(0)) {
                            tempList.add(anNewId);
                            tempList.add(transOldList.get(j));
                        }
                        j++;
                    }
                    transTempList.add(tempList);
                }
            }
        } catch (Exception e) {
            logger.debug("loadIgmpUniVlanTransInfo error: {}", e);
        }
        transList = JSONArray.fromObject(transTempList);
        return SUCCESS;
    }

    /**
     * 显示IGMP UNI端口配置
     * 
     * @return
     * @throws Exception
     */
    public String showIgmpUniMgmt() throws Exception {
        List<Integer> uniMvlanListForUni = new ArrayList<Integer>();
        List<Integer> tempValueList = new ArrayList<Integer>();
        try {
            IgmpMcUniConfigTable igmpMcUniConfig = oltIgmpService.getIgmpMcUniConfig(entityId, uniIndex);
            if (igmpMcUniConfig != null) {
                tempValueList.add(igmpMcUniConfig.getTopMcUniMaxGroupQuantity());
                tempValueList.add(igmpMcUniConfig.getTopMcUniVlanMode());
                if (igmpMcUniConfig.getTopMcUniVlanMode() == 2) {
                    tempValueList.add(igmpMcUniConfig.getTopMcUniVlanTransIdx());
                }
                uniMvlanListForUni = igmpMcUniConfig.getTopMcUniVlanListList();
                while (uniMvlanListForUni.contains(new Integer(0))) {
                    uniMvlanListForUni.remove(new Integer(0));
                }
            }
        } catch (Exception e) {
            logger.debug("loadIgmpUniInfo error: {}", e);
        }
        if (tempValueList.size() > 0) {
            uniIgmpValue = JSONArray.fromObject(tempValueList);
        } else {
            uniIgmpValue = JSONArray.fromObject(false);
        }
        if (uniMvlanListForUni != null && uniMvlanListForUni.size() > 0) {
            uniMvidList = JSONArray.fromObject(uniMvlanListForUni);
        } else {
            uniMvidList = JSONArray.fromObject(false);
        }
        // transGrid的数据
        List<List<Integer>> transTempList = new ArrayList<List<Integer>>();
        try {
            List<IgmpMcOnuVlanTransTable> transObj = oltIgmpService.getIgmpVlanTrans(entityId);
            if (transObj != null && transObj.size() != 0) {
                for (IgmpMcOnuVlanTransTable aTransRule : transObj) {
                    Integer transId = aTransRule.getTopMcOnuVlanTransIndex();
                    List<Integer> transNewList = aTransRule.getTopMcOnuVlanTransNewVidList();
                    List<Integer> transOldList = aTransRule.getTopMcOnuVlanTransOldVidList();
                    List<Integer> tempList = new ArrayList<Integer>();
                    tempList.add(transId);
                    int j = 0;
                    for (Integer anNewId : transNewList) {
                        if (!anNewId.equals(0)) {
                            tempList.add(anNewId);
                            tempList.add(transOldList.get(j));
                        }
                        j++;
                    }
                    transTempList.add(tempList);
                }
            }
        } catch (Exception e) {
            logger.debug("loadIgmpUniVlanTransInfo error: {}", e);
        }
        if (transTempList.size() > 0) {
            transList = JSONArray.fromObject(transTempList);
        } else {
            transList = JSONArray.fromObject(false);
        }
        return SUCCESS;
    }

    /**
     * 添加uni口组播模板
     */
    @OperationLogProperty(actionName = "onuIgmpAction", operationName = "addIgmpUniPort")
    public String addIgmpUniPort() throws Exception {
        String message = "success";
        if (uniPortMvlanListStr.equals("") || uniPortMvlanListStr.length() == 0) {
            message = "wrong data";
        } else {
            String[] tmpStrList = uniPortMvlanListStr.split(",");
            List<Integer> tmpMvlanList = new ArrayList<Integer>();
            for (String aTmp : tmpStrList) {
                tmpMvlanList.add(Integer.parseInt(aTmp));
            }
            try {
                oltIgmpService.addIgmpUni(entityId, uniIndex, tmpMvlanList);
                source = EponIndex.getUniStringByIndex(uniIndex).toString();
                operationResult = OperationLog.SUCCESS;
            } catch (Exception e) {
                message = e.getMessage();
                logger.debug("modifyIgmpUniPort error: {}", e);
                operationResult = OperationLog.FAILURE;
            }
        }
        writeDataToAjax(message);
        return NONE;
    }

    /**
     * 删除uni口组播模板
     */
    @OperationLogProperty(actionName = "onuIgmpAction", operationName = "deleteIgmpUniPort")
    public String deleteIgmpUniPort() throws Exception {
        String message = "success";
        try {
            oltIgmpService.deleteIgmpUniInOnu(entityId, uniIndex);
            source = EponIndex.getUniStringByIndex(uniIndex).toString();
            operationResult = OperationLog.SUCCESS;
        } catch (Exception e) {
            message = e.getMessage();
            logger.debug("modifyIgmpUniPort error: {}", e);
            operationResult = OperationLog.FAILURE;
        }
        writeDataToAjax(message);
        return NONE;
    }

    /**
     * 修改uni口组播模板
     */
    @OperationLogProperty(actionName = "onuIgmpAction", operationName = "modifyIgmpUniPort")
    public String modifyIgmpUniPort() throws Exception {
        String message = "success";
        String[] tmpStrList = uniPortMvlanListStr.split(",");
        List<Integer> tmpMvlanList = new ArrayList<Integer>();
        if (tmpStrList.length > 1 || !tmpStrList[0].equalsIgnoreCase("")) {
            for (String aTmp : tmpStrList) {
                tmpMvlanList.add(Integer.parseInt(aTmp));
            }
        }
        try {
            oltIgmpService.modifyIgmpUni(entityId, uniIndex, tmpMvlanList);
            source = EponIndex.getUniStringByIndex(uniIndex).toString();
            operationResult = OperationLog.SUCCESS;
        } catch (Exception e) {
            message = e.getMessage();
            logger.debug("modifyIgmpUniPort error: {}", e);
            operationResult = OperationLog.FAILURE;
        }
        writeDataToAjax(message);
        return NONE;
    }

    /**
     * 修改UNI口的IGMP信息
     * 
     * @return
     */
    @OperationLogProperty(actionName = "onuIgmpAction", operationName = "modifyIgmpUniConfig")
    public String modifyIgmpMcUniConfig() throws Exception {
        IgmpMcUniConfigTable table = new IgmpMcUniConfigTable();
        table.setEntityId(entityId);
        table.setUniIndex(uniIndex);
        table.setTopMcUniMaxGroupQuantity(maxGroupNum);
        table.setTopMcUniVlanMode(uniVlanMode);
        if (uniVlanMode.equals(EponConstants.IGMP_UNI_MODE_TRANS)) {
            table.setTopMcUniVlanTransIdx(transId);
        }
        if (!uniPortMvidList.equals(0) && !uniPortMvidList.equals("") && uniPortMvidList != null
                && uniPortMvidList.length() != 0) {
            List<Integer> tmpList = new ArrayList<Integer>();
            String[] tmpStrList = uniPortMvidList.split(",");
            for (String aTmp : tmpStrList) {
                tmpList.add(Integer.parseInt(aTmp));
            }
            table.setTopMcUniVlanListList(tmpList);
        }
        String message = "success";
        try {
            source = EponIndex.getUniStringByIndex(uniIndex).toString();
            oltIgmpService.modifyIgmpMcUniConfig(table);
            operationResult = OperationLog.SUCCESS;
        } catch (Exception e) {
            message = e.getMessage();
            logger.debug("modifyIgmpMcUniConfig error: {}", e);
            operationResult = OperationLog.FAILURE;
        }
        writeDataToAjax(message);
        return NONE;
    }

    /**
     * 刷新当前UNI口的IGMP信息
     * 
     * @param entityId
     * @param uniIndex
     * @throws IOException 
     */
    public String refreshIgmpUni() throws IOException {
        String message = "success";
        try {
            oltIgmpService.refreshIgmpUniInOnu(entityId, uniIndex);
        } catch (Exception e) {
            message = e.getMessage();
            logger.debug("refreshIgmpUni error: {}", e);
        }
        writeDataToAjax(message);
        return NONE;
    }

    /**
     * 添加一个ONU下的IGMP的UNI口IGMP信息
     * 
     * @return
     * @throws Exception
     */
    public String addIgmpUniInOnu() throws Exception {
        String result;
        try {
            oltIgmpService.addIgmpUniInOnu(entityId, onuIndex);
            result = "success";
        } catch (Exception e) {
            result = e.getMessage();
            logger.debug("addIgmpUniInOnu error:{}", e);
        }
        writeDataToAjax(result);
        return NONE;
    }

    /**
     * 删除一个ONU下的IGMP的UNI口IGMP信息
     * 
     * @return
     * @throws Exception
     */
    public String deleteIgmpUniInOnu() throws Exception {
        String result;
        try {
            oltIgmpService.deleteIgmpUniInOnu(entityId, onuIndex);
            result = "success";
        } catch (Exception e) {
            result = e.getMessage();
            logger.debug("addIgmpUniInOnu error:{}", e);
        }
        writeDataToAjax(result);
        return NONE;
    }

    public OltIgmpService getOltIgmpService() {
        return oltIgmpService;
    }

    public void setOltIgmpService(OltIgmpService oltIgmpService) {
        this.oltIgmpService = oltIgmpService;
    }

    public Long getOnuIndex() {
        return onuIndex;
    }

    public void setOnuIndex(Long onuIndex) {
        this.onuIndex = onuIndex;
    }

    public JSONArray getOnuValue() {
        return onuValue;
    }

    public void setOnuValue(JSONArray onuValue) {
        this.onuValue = onuValue;
    }

    public Integer getOnuIgmpMode() {
        return onuIgmpMode;
    }

    public void setOnuIgmpMode(Integer onuIgmpMode) {
        this.onuIgmpMode = onuIgmpMode;
    }

    public Integer getFastLeaveEnabled() {
        return fastLeaveEnabled;
    }

    public void setFastLeaveEnabled(Integer fastLeaveEnabled) {
        this.fastLeaveEnabled = fastLeaveEnabled;
    }

    public JSONArray getMvlanList() {
        return mvlanList;
    }

    public void setMvlanList(JSONArray mvlanList) {
        this.mvlanList = mvlanList;
    }

    public JSONArray getUniMvlanList() {
        return uniMvlanList;
    }

    public void setUniMvlanList(JSONArray uniMvlanList) {
        this.uniMvlanList = uniMvlanList;
    }

    public Long getUniIndex() {
        return uniIndex;
    }

    public void setUniIndex(Long uniIndex) {
        this.uniIndex = uniIndex;
    }

    public JSONArray getUniIgmpValue() {
        return uniIgmpValue;
    }

    public void setUniIgmpValue(JSONArray uniIgmpValue) {
        this.uniIgmpValue = uniIgmpValue;
    }

    public JSONArray getUniMvidList() {
        return uniMvidList;
    }

    public void setUniMvidList(JSONArray uniMvidList) {
        this.uniMvidList = uniMvidList;
    }

    public JSONArray getTransList() {
        return transList;
    }

    public void setTransList(JSONArray transList) {
        this.transList = transList;
    }

    public String getUniPortMvlanListStr() {
        return uniPortMvlanListStr;
    }

    public void setUniPortMvlanListStr(String uniPortMvlanListStr) {
        this.uniPortMvlanListStr = uniPortMvlanListStr;
    }

    public String getUniPortMvidList() {
        return uniPortMvidList;
    }

    public void setUniPortMvidList(String uniPortMvidList) {
        this.uniPortMvidList = uniPortMvidList;
    }

    public Integer getUniVlanMode() {
        return uniVlanMode;
    }

    public void setUniVlanMode(Integer uniVlanMode) {
        this.uniVlanMode = uniVlanMode;
    }

    public Integer getMaxGroupNum() {
        return maxGroupNum;
    }

    public void setMaxGroupNum(Integer maxGroupNum) {
        this.maxGroupNum = maxGroupNum;
    }

    public Integer getTransId() {
        return transId;
    }

    public void setTransId(Integer transId) {
        this.transId = transId;
    }

}
