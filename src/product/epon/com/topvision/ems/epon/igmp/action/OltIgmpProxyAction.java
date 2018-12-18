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

import com.topvision.ems.epon.igmp.domain.IgmpControlledMulticastPackageTable;
import com.topvision.ems.epon.igmp.domain.IgmpMcParamMgmtObjects;
import com.topvision.ems.epon.igmp.domain.IgmpProxyParaTable;
import com.topvision.ems.epon.igmp.service.OltIgmpService;
import com.topvision.ems.epon.olt.action.AbstractEponAction;
import com.topvision.framework.annotation.OperationLogProperty;
import com.topvision.platform.domain.OperationLog;

/**
 * @author flack
 * @created @2013-10-25-下午4:40:40
 *
 */
@Controller("oltIgmpProxyAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class OltIgmpProxyAction extends AbstractEponAction {
    private static final long serialVersionUID = -8850965444735210209L;
    private final Logger logger = LoggerFactory.getLogger(OltIgmpProxyAction.class);
    @Autowired
    private OltIgmpService oltIgmpService;
    private Integer proxyIndex;
    private Integer proxyId;
    private String proxyChName;
    private String proxyName;
    private String proxySrcIPAddress;
    private Integer proxyMulticastVID;
    private String proxyMulticastIPAddress;
    private String proxyIdList;
    private Long multicastMaxBW;
    private Integer cmIndex;
    private String igmpProxyParaTables;
    private Integer groupNum;
    private Integer maxGroupNum;
    private String mvlanIp;
    private Integer maxBW;
    private Integer proxyMvlanId;
    private JSONArray mvlanList = new JSONArray();
    private JSONArray mvidList = new JSONArray();

    /**
     * 修改组播组
     * 
     * @return
     */
    @OperationLogProperty(actionName = "oltIgmpProxyAction", operationName = "modifyIgmpProxyInfo${proxyName}")
    public String modifyIgmpProxyInfo() throws Exception {
        IgmpProxyParaTable table = new IgmpProxyParaTable();
        table.setProxyName(proxyName);
        table.setProxyChName(proxyChName);
        table.setEntityId(entityId);
        table.setMulticastMaxBW(multicastMaxBW);
        table.setProxyIndex(proxyId);
        table.setProxyMulticastIPAddress(proxyMulticastIPAddress);
        table.setProxyMulticastVID(proxyMulticastVID);
        table.setProxySrcIPAddress(proxySrcIPAddress);
        String message = "success";
        try {
            oltIgmpService.modifyIgmpProxyInfo(table);
            proxyIndex = proxyId;
            // modifyProxyMulticastVID();
            operationResult = OperationLog.SUCCESS;
        } catch (Exception e) {
            message = e.getMessage();
            logger.debug("modifyIgmpProxyInfo error: {}", e);
            operationResult = OperationLog.FAILURE;
        }
        writeDataToAjax(message);
        return NONE;
    }

    /**
     * 添加组播组
     * 
     * @return
     */
    @OperationLogProperty(actionName = "oltIgmpProxyAction", operationName = "addIgmpProxy${proxyName}")
    public String addIgmpProxy() throws Exception {
        IgmpProxyParaTable table = new IgmpProxyParaTable();
        table.setProxyName(proxyName);
        table.setProxyChName(proxyChName);
        table.setEntityId(entityId);
        table.setMulticastMaxBW(multicastMaxBW);
        table.setProxyIndex(proxyId);
        table.setProxyMulticastIPAddress(proxyMulticastIPAddress);
        table.setProxyMulticastVID(proxyMulticastVID);
        table.setProxySrcIPAddress(proxySrcIPAddress);
        String message = "success";
        try {
            oltIgmpService.addIgmpProxy(table);
            proxyIndex = 0;
            // modifyProxyMulticastVID();
            operationResult = OperationLog.SUCCESS;
        } catch (Exception e) {
            message = e.getMessage();
            logger.debug("addIgmpProxy error: {}", e);
            operationResult = OperationLog.FAILURE;
        }
        writeDataToAjax(message);
        return NONE;
    }

    /**
     * 删除组播组
     * 
     * @return
     */
    @OperationLogProperty(actionName = "oltIgmpProxyAction", operationName = "deleteIgmpProxy${proxyIndex}")
    public String deleteIgmpProxy() throws Exception {
        String message = "success";
        try {
            oltIgmpService.deleteIgmpProxy(entityId, proxyIndex);
            proxyId = 0;
            // modifyProxyMulticastVID();
            operationResult = OperationLog.SUCCESS;
        } catch (Exception e) {
            message = e.getMessage();
            logger.debug("deleteIgmpProxy error: {}", e);
            operationResult = OperationLog.FAILURE;
        }
        writeDataToAjax(message);
        return NONE;
    }

    /**
     * 修改频道的组播组vlan ID
     * 
     * @return
     */
    @SuppressWarnings("unused")
    private String modifyProxyMulticastVID() {
        if (!proxyIndex.equals(0)) {// 添加的时候不删除原来的频道
            List<Integer> oldMvlanProxyList = getIgmpOldMvlanProxyList();// cmIndex
            if (oldMvlanProxyList != null && oldMvlanProxyList.size() != 0) {
                int i = 0;
                int tempNum = -1;
                for (Integer aProxy : oldMvlanProxyList) {
                    if (aProxy.equals(proxyIndex)) {
                        tempNum = i;
                    }
                    i++;
                }
                if (tempNum != -1) {
                    oldMvlanProxyList.remove(tempNum);
                }
            } else {
                oldMvlanProxyList = new ArrayList<Integer>();
            }
            oltIgmpService.modifyMulticastPackage(entityId, cmIndex, oldMvlanProxyList);
        }
        if (!proxyId.equals(0)) {// 删除的时候不添加新的频道
            List<Integer> newMvlanProxyList = getIgmpNewMvlanProxyList();// proxyMulticastVID
            if (newMvlanProxyList == null) {
                newMvlanProxyList = new ArrayList<Integer>();
            }
            newMvlanProxyList.add(proxyId);
            oltIgmpService.modifyMulticastPackage(entityId, proxyMulticastVID, newMvlanProxyList);
        }
        return NONE;
    }

    /**
     * 展示IGMP频道列表
     */
    public String showIgmpProxy() {
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
     * 显示IGMP频道详细信息修改、新增页面
     */
    public String showIgmpProxyDetail() {
        // 目前的组播组数量
        groupNum = oltIgmpService.getIgmpProxyInfo(entityId).size();
        // 支持的最大组播组数量
        maxGroupNum = oltIgmpService.getIgmpMaxGroupNum(entityId).getTopMcMaxGroupNum();
        List<String> mvlanListForProxy = new ArrayList<String>();
        List<IgmpControlledMulticastPackageTable> mvlanListTmp = new ArrayList<IgmpControlledMulticastPackageTable>();
        try {
            mvlanListTmp = oltIgmpService.getIgmpMvlanInfo(entityId);
        } catch (Exception e) {
            logger.debug("getIgmpMvlanInfo error: {}", e);
        }
        if (mvlanListTmp != null && mvlanListTmp.size() != 0) {
            for (IgmpControlledMulticastPackageTable aMvlan : mvlanListTmp) {
                mvlanListForProxy.add(aMvlan.getCmIndex().toString());
                mvlanListForProxy.add(aMvlan.getCmChName());// 组播组别名
                mvlanListForProxy.add(aMvlan.getCmName());
                mvlanListForProxy.add(aMvlan.getMulticastUserAuthority().toString());
                mvlanListForProxy.add(aMvlan.getCmProxyList());
            }
        }
        if (mvlanListForProxy.size() > 0) {
            mvlanList = JSONArray.fromObject(mvlanListForProxy);
        } else {
            mvlanList = JSONArray.fromObject(false);
        }
        // MVLAN
        IgmpMcParamMgmtObjects m = oltIgmpService.getIgmpMaxGroupNum(entityId);
        if (m != null && m.getTopMcMVlanList() != null && m.getTopMcMVlanList().size() > 0) {
            mvidList = JSONArray.fromObject(m.getTopMcMVlanList());
        } else {
            mvidList = JSONArray.fromObject(false);
        }
        return SUCCESS;
    }

    /**
     * 刷新组播组列表信息
     * @throws IOException 
     */
    @OperationLogProperty(actionName = "oltIgmpProxyAction", operationName = "refreshIgmpProxy")
    public String refreshIgmpProxy() throws IOException {
        String message = "success";
        try {
            oltIgmpService.refreshIgmpProxyParaTable(entityId);
            oltIgmpService.refreshIgmpControlledMulticastPackageTable(entityId);
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
     * <proxy>:获取原组播组频道LIST
     * 
     * @return
     */
    private List<Integer> getIgmpOldMvlanProxyList() {
        List<Integer> list = new ArrayList<Integer>();
        try {
            list = oltIgmpService.getIgmpMvlanProxyList(entityId, cmIndex);
        } catch (Exception e) {
            logger.debug("getIgmpOldMvlanProxyList error: {}", e);
        }
        return list;
    }

    /**
     * <proxy>:获取原组播组频道LIST
     * 
     * @return
     */
    private List<Integer> getIgmpNewMvlanProxyList() {
        List<Integer> list = new ArrayList<Integer>();
        try {
            list = oltIgmpService.getIgmpMvlanProxyList(entityId, proxyMulticastVID);
        } catch (Exception e) {
            logger.debug("getIgmpNewMvlanProxyList error: {}", e);
        }
        return list;
    }

    public OltIgmpService getOltIgmpService() {
        return oltIgmpService;
    }

    public void setOltIgmpService(OltIgmpService oltIgmpService) {
        this.oltIgmpService = oltIgmpService;
    }

    public Integer getProxyIndex() {
        return proxyIndex;
    }

    public void setProxyIndex(Integer proxyIndex) {
        this.proxyIndex = proxyIndex;
    }

    public Integer getProxyId() {
        return proxyId;
    }

    public void setProxyId(Integer proxyId) {
        this.proxyId = proxyId;
    }

    public String getProxyChName() {
        return proxyChName;
    }

    public void setProxyChName(String proxyChName) {
        this.proxyChName = proxyChName;
    }

    public String getProxyName() {
        return proxyName;
    }

    public void setProxyName(String proxyName) {
        this.proxyName = proxyName;
    }

    public String getProxySrcIPAddress() {
        return proxySrcIPAddress;
    }

    public void setProxySrcIPAddress(String proxySrcIPAddress) {
        this.proxySrcIPAddress = proxySrcIPAddress;
    }

    public Integer getProxyMulticastVID() {
        return proxyMulticastVID;
    }

    public void setProxyMulticastVID(Integer proxyMulticastVID) {
        this.proxyMulticastVID = proxyMulticastVID;
    }

    public String getProxyMulticastIPAddress() {
        return proxyMulticastIPAddress;
    }

    public void setProxyMulticastIPAddress(String proxyMulticastIPAddress) {
        this.proxyMulticastIPAddress = proxyMulticastIPAddress;
    }

    public String getProxyIdList() {
        return proxyIdList;
    }

    public void setProxyIdList(String proxyIdList) {
        this.proxyIdList = proxyIdList;
    }

    public Long getMulticastMaxBW() {
        return multicastMaxBW;
    }

    public void setMulticastMaxBW(Long multicastMaxBW) {
        this.multicastMaxBW = multicastMaxBW;
    }

    public Integer getCmIndex() {
        return cmIndex;
    }

    public void setCmIndex(Integer cmIndex) {
        this.cmIndex = cmIndex;
    }

    public String getIgmpProxyParaTables() {
        return igmpProxyParaTables;
    }

    public void setIgmpProxyParaTables(String igmpProxyParaTables) {
        this.igmpProxyParaTables = igmpProxyParaTables;
    }

    public Integer getGroupNum() {
        return groupNum;
    }

    public void setGroupNum(Integer groupNum) {
        this.groupNum = groupNum;
    }

    public Integer getMaxGroupNum() {
        return maxGroupNum;
    }

    public void setMaxGroupNum(Integer maxGroupNum) {
        this.maxGroupNum = maxGroupNum;
    }

    public JSONArray getMvlanList() {
        return mvlanList;
    }

    public void setMvlanList(JSONArray mvlanList) {
        this.mvlanList = mvlanList;
    }

    public JSONArray getMvidList() {
        return mvidList;
    }

    public void setMvidList(JSONArray mvidList) {
        this.mvidList = mvidList;
    }

    public String getMvlanIp() {
        return mvlanIp;
    }

    public void setMvlanIp(String mvlanIp) {
        this.mvlanIp = mvlanIp;
    }

    public Integer getMaxBW() {
        return maxBW;
    }

    public void setMaxBW(Integer maxBW) {
        this.maxBW = maxBW;
    }

    public Integer getProxyMvlanId() {
        return proxyMvlanId;
    }

    public void setProxyMvlanId(Integer proxyMvlanId) {
        this.proxyMvlanId = proxyMvlanId;
    }

}
