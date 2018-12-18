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

import org.apache.struts2.ServletActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.topvision.ems.epon.exception.ModifyIgmpGlobalInfoException;
import com.topvision.ems.epon.exception.ModifyIgmpMaxGroupNumException;
import com.topvision.ems.epon.exception.ModifyIgmpSniConfigException;
import com.topvision.ems.epon.exception.RefreshIgmpControlledMcCdrTableException;
import com.topvision.ems.epon.exception.RefreshIgmpEntityTableException;
import com.topvision.ems.epon.exception.RefreshIgmpMcParamMgmtObjectsException;
import com.topvision.ems.epon.exception.RefreshIgmpMcSniConfigMgmtObjectsException;
import com.topvision.ems.epon.igmp.domain.IgmpControlledMcCdrTable;
import com.topvision.ems.epon.igmp.domain.IgmpEntityTable;
import com.topvision.ems.epon.igmp.domain.IgmpMcParamMgmtObjects;
import com.topvision.ems.epon.igmp.domain.IgmpMcSniConfigMgmtObjects;
import com.topvision.ems.epon.igmp.domain.IgmpProxyParaTable;
import com.topvision.ems.epon.igmp.domain.TopMcForwardingOnuTable;
import com.topvision.ems.epon.igmp.domain.TopMcForwardingPortTable;
import com.topvision.ems.epon.igmp.domain.TopMcForwardingSlotTable;
import com.topvision.ems.epon.igmp.service.OltIgmpService;
import com.topvision.ems.epon.olt.action.AbstractEponAction;
import com.topvision.ems.epon.olt.domain.OltPonAttribute;
import com.topvision.ems.epon.olt.domain.OltSlotAttribute;
import com.topvision.ems.epon.olt.service.OltSlotService;
import com.topvision.framework.utils.EponConstants;
import com.topvision.ems.epon.vlan.domain.TopOltVlanVifPriIpTable;
import com.topvision.ems.epon.vlan.domain.VlanAttribute;
import com.topvision.ems.epon.vlan.service.SniVlanService;
import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.network.service.EntityService;
import com.topvision.framework.annotation.OperationLogProperty;
import com.topvision.platform.domain.OperationLog;

/**
 * @author flack
 * @created @2013-10-25-下午4:40:40
 *
 */
@Controller("oltIgmpAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class OltIgmpAction extends AbstractEponAction {
    private static final long serialVersionUID = 1222749854939248788L;
    private final Logger logger = LoggerFactory.getLogger(OltIgmpAction.class);
    @Autowired
    private SniVlanService sniVlanService;
    @Autowired
    private OltIgmpService oltIgmpService;
    @Autowired
    private EntityService entityService;
    @Autowired
    private OltSlotService oltSlotService;
    private Entity entity;
    private Integer proxyId;
    private Integer forwardingType;
    private IgmpEntityTable igmpEntity;
    private IgmpMcSniConfigMgmtObjects sniConfig;
    private IgmpMcParamMgmtObjects igmpMcParamMgmt;
    private Integer igmpWorkMode;
    private Integer maxQueryResponseTime;
    private Integer robustVariable;
    private Integer queryInterval;
    private Integer lastMemberQueryInterval;
    private Integer lastMemberQueryCount;
    private Integer igmpVersion;
    private Integer sniType;
    private Integer igmpMaxGroupNum;
    private Long topMcMaxBw;
    private Integer topMcSnoopingAgingTime;
    private String topMcMVlan;
    private String sniPort;
    private Integer sniAggPort;
    private Integer slotNo;
    private Integer portNo;
    private Integer start;
    private Integer limit;
    private JSONArray proxyListObject = new JSONArray();
    private JSONArray mvidList = new JSONArray();
    private JSONArray vlanListObject = new JSONArray();
    private JSONArray igmpEntityObject = new JSONArray();
    private JSONArray sniConfigObject = new JSONArray();
    private JSONArray igmpMcParamMgmtObject = new JSONArray();
    private JSONArray ponList = new JSONArray();
    private JSONArray slotList = new JSONArray();

    /**
     * FORWARDING 跳转
     */
    public String showIgmpForwarding() {
        // 板卡、端口结构数据
        List<Long> tempSlotList = new ArrayList<Long>();
        List<List<Long>> tempPonList = new ArrayList<List<Long>>();
        for (int k = 0; k < 19; k++) {
            tempSlotList.add(0L);
            List<Long> tmp = new ArrayList<Long>();
            for (int i = 0; i < 9; i++) {
                tmp.add(0L);
            }
            tempPonList.add(tmp);
        }
        try {
            List<OltSlotAttribute> slotObjList = oltSlotService.getOltPonSlotList(entityId);
            for (OltSlotAttribute aSlot : slotObjList) {
                tempSlotList.set(aSlot.getSlotNo().intValue(), aSlot.getSlotId());
                List<OltPonAttribute> ponObjList = oltSlotService.getSlotPonList(aSlot.getSlotId());
                for (OltPonAttribute aPon : ponObjList) {
                    tempPonList.get(aSlot.getSlotNo().intValue()).set(aPon.getPonNo().intValue(), aPon.getPonId());
                }
            }
        } catch (Exception e) {
            logger.debug("getSlotPonList error: {}", e);
        }
        slotList = JSONArray.fromObject(tempSlotList);
        ponList = JSONArray.fromObject(tempPonList);
        // 组播组列表
        List<String> proxyList = new ArrayList<String>();
        for (int i = 0; i < 2001; i++) {
            proxyList.add("");
        }
        try {
            List<IgmpProxyParaTable> groups = oltIgmpService.getIgmpProxyInfo(entityId);
            if (groups.size() > 0) {
                for (IgmpProxyParaTable p : groups) {
                    StringBuilder s = new StringBuilder();
                    s.append(p.getProxyMulticastIPAddress()).append("#").append(p.getProxyName());
                    proxyList.set(p.getProxyIndex(), s.toString());
                }
            }
        } catch (Exception e) {
            logger.debug("getIgmpProxyInfo error: {}", e);
        }
        if (proxyList.size() > 0) {
            proxyListObject = JSONArray.fromObject(proxyList);
        } else {
            proxyListObject = JSONArray.fromObject(false);
        }
        return SUCCESS;
    }

    /**
     * 获取forwarding列表的数据
     * @throws IOException 
     */
    public String getIgmpForwarding() throws IOException {
        List<Integer> tmpList = new ArrayList<Integer>();
        switch (forwardingType) {
        case 0: // 板卡
            TopMcForwardingSlotTable slotTmp = oltIgmpService.getTopMcForwardingSlot(entityId, proxyId);
            if (slotTmp == null) {
                tmpList.add(0);
            } else {
                if (slotTmp.getTopMcSlotCount() == null || slotTmp.getTopMcSlotListIntegers() == null) {
                    tmpList.add(0);
                } else {
                    tmpList.add(slotTmp.getTopMcSlotCount());
                    if (slotTmp.getTopMcSlotListIntegers().size() > 0) {
                        for (Integer slotNo : slotTmp.getTopMcSlotListIntegers()) {
                            tmpList.add(slotNo);
                        }
                    }
                }
            }
            break;
        case 1: // 端口
            TopMcForwardingPortTable portTmp = oltIgmpService.getTopMcForwardingPort(entityId, proxyId, slotNo);
            if (portTmp == null) {
                tmpList.add(0);
            } else {
                if (portTmp.getTopMcPortCount() != null && portTmp.getTopMcPortListIntegers() != null) {
                    tmpList.add(portTmp.getTopMcPortCount());
                    if (portTmp.getTopMcPortListIntegers().size() > 0) {
                        for (Integer portNo : portTmp.getTopMcPortListIntegers()) {
                            tmpList.add(portNo);
                        }
                    }
                } else {
                    tmpList.add(0);
                }
            }
            break;
        case 2: // ONU
        case 3:// UNI
            TopMcForwardingOnuTable onuTmp = oltIgmpService.getTopMcForwardingOnu(entityId, proxyId, slotNo, portNo);
            if (onuTmp == null) {
                tmpList.add(0);
            } else {
                if (onuTmp.getTopMcOnuCount() != null && onuTmp.getTopMcOnuListUniIndexList() != null) {
                    tmpList.add(onuTmp.getTopMcOnuCount());
                    List<Integer> tempL = onuTmp.getTopMcOnuListUniIndexList();
                    for (int i = 0; 5 * i < tempL.size(); i++) {
                        if (tempL.get(5 * i) > 0) {
                            tmpList.add(tempL.get(5 * i));// tempL.get(5*i) == i
                                                          // + 1
                            if (forwardingType == 3) {
                                tmpList.add(tempL.get(5 * i + 1));
                                tmpList.add(tempL.get(5 * i + 2));
                                tmpList.add(tempL.get(5 * i + 3));
                                tmpList.add(tempL.get(5 * i + 4));
                            }
                        }
                    }
                } else {
                    tmpList.add(0);
                }
            }
            break;
        default:
            tmpList.add(0);
        }
        writeDataToAjax(tmpList.toString());
        return NONE;
    }

    /**
     * 显示IGMP协议配置
     */
    public String showIgmpProtocol() {
        try {
            igmpEntity = oltIgmpService.getIgmpGlobalInfo(entityId);
            sniConfig = oltIgmpService.getIgmpSniConfig(entityId);
            igmpMcParamMgmt = oltIgmpService.getIgmpMaxGroupNum(entityId);
            List<VlanAttribute> vlanL = sniVlanService.getOltVlanConfigList(entityId);
            List<Integer> vlanList = new ArrayList<Integer>();
            List<Integer> vlanVif = new ArrayList<Integer>();
            List<List<Integer>> vl = new ArrayList<List<Integer>>();
            if (vlanL.size() > 0) {
                for (VlanAttribute v : vlanL) {
                    Integer vid = v.getVlanIndex();
                    if (vid.equals(1)) {
                        continue;
                    }
                    TopOltVlanVifPriIpTable vlanVifPriIp = sniVlanService.getVlanVifPriIp(entityId, vid);
                    if (vlanVifPriIp == null) {
                        vlanList.add(vid);
                    } else {
                        vlanVif.add(vid);
                    }
                }
            }
            vl.add(vlanList);
            vl.add(vlanVif);
            vlanListObject = JSONArray.fromObject(vl);
        } catch (Exception e) {
            logger.debug("loadIgmpProtocolInfo error: {}", e);
        }
        igmpEntityObject = JSONArray.fromObject(igmpEntity);
        sniConfigObject = JSONArray.fromObject(sniConfig);
        igmpMcParamMgmtObject = JSONArray.fromObject(igmpMcParamMgmt);
        // [[mvid, proxyId, proxyId...], []...]
        List<List<Integer>> mvidL = new ArrayList<List<Integer>>();
        if (igmpMcParamMgmt.getTopMcMVlanList() != null && igmpMcParamMgmt.getTopMcMVlanList().size() > 0) {
            for (Integer mvid : igmpMcParamMgmt.getTopMcMVlanList()) {
                List<Integer> tmp1 = new ArrayList<Integer>();
                tmp1.add(mvid);
                mvidL.add(tmp1);
            }
            List<IgmpProxyParaTable> groups = oltIgmpService.getIgmpProxyInfo(entityId);
            if (groups != null && groups.size() > 0) {
                for (IgmpProxyParaTable group : groups) {
                    if (group.getProxyIndex() != null && group.getProxyMulticastVID() != null) {
                        for (List<Integer> tmp2 : mvidL) {
                            if (tmp2.get(0).equals(group.getProxyMulticastVID())) {
                                tmp2.add(group.getProxyIndex());
                                break;
                            }
                        }
                    }
                }
            }
        }
        if (mvidL.size() > 0) {
            mvidList = JSONArray.fromObject(mvidL);
        } else {
            mvidList = JSONArray.fromObject(false);
        }
        return SUCCESS;
    }

    /**
     * 获取设备的IGMP模式
     * @throws IOException 
     */
    public String getIgmpMode() throws IOException {
        String message = "success";
        try {
            IgmpEntityTable igmpObj = oltIgmpService.getIgmpGlobalInfo(entityId);
            if (igmpObj != null && igmpObj.getIgmpMode() != null) {
                message = message + igmpObj.getIgmpMode().toString();
            }
        } catch (Exception e) {
            message = "failed" + e.getMessage();
            logger.debug("getIgmpMode error: {}", e);
        }
        writeDataToAjax(message);
        return NONE;
    }

    /**
     * 更新IGMP全局配置
     * 
     * @return
     * @throws Exception
     */
    @OperationLogProperty(actionName = "oltIgmpAction", operationName = "modifyIgmpGlobalInfo")
    public String modifyIgmpGlobalInfo() throws Exception {
        String result;
        IgmpEntityTable igmpEntity = new IgmpEntityTable();
        igmpEntity.setEntityId(entityId);
        igmpEntity.setIgmpMode(igmpWorkMode);
        igmpEntity.setIgmpVersion(igmpVersion);
        igmpEntity.setLastMemberQueryCount(lastMemberQueryCount);
        igmpEntity.setLastMemberQueryInterval(lastMemberQueryInterval);
        igmpEntity.setMaxQueryResponseTime(maxQueryResponseTime);
        igmpEntity.setQueryInterval(queryInterval);
        igmpEntity.setRobustVariable(robustVariable);
        try {
            if (igmpWorkMode.equals(EponConstants.IGMP_MODE_DISABLED)) {
                oltIgmpService.modifyIgmpGlobalInfo(igmpEntity);
            } else {
                oltIgmpService.modifyIgmpGlobalInfo(igmpEntity);
                List<Integer> ml = new ArrayList<Integer>();
                if (topMcMVlan.length() > 0) {
                    for (String s : topMcMVlan.split(",")) {
                        ml.add(Integer.parseInt(s));
                    }
                }
                oltIgmpService.modifyIgmpMaxGroupNum(entityId, igmpMaxGroupNum, topMcMaxBw, topMcSnoopingAgingTime, ml);
                oltIgmpService.modifyIgmpSniConfig(entityId, sniType, sniPort, sniAggPort);
            }
            result = "success";
            operationResult = OperationLog.SUCCESS;
        } catch (ModifyIgmpGlobalInfoException sce) {
            result = sce.getMessage();
            operationResult = OperationLog.FAILURE;
            logger.debug("modifyIgmpGlobalInfo error:{}", sce);
        } catch (ModifyIgmpMaxGroupNumException sce) {
            result = sce.getMessage();
            operationResult = OperationLog.FAILURE;
            logger.debug("modifyIgmpMaxGroupNum error:{}", sce);
        } catch (ModifyIgmpSniConfigException sce) {
            result = sce.getMessage();
            operationResult = OperationLog.FAILURE;
            logger.debug("modifyIgmpSniConfig error:{}", sce);
        }
        writeDataToAjax(result);
        return NONE;
    }

    /**
     * 跳转IGMP的呼叫信息记录
     */
    public String showIgmpHistory() {
        return SUCCESS;
    }

    /**
     * 获取IGMP的呼叫信息记录
     * 
     * @return
     * @throws Exception
     */
    public String loadCdrList() throws Exception {
        Map<String, Object> json = new HashMap<String, Object>();
        List<IgmpControlledMcCdrTable> cdrListAll = new ArrayList<IgmpControlledMcCdrTable>();
        List<IgmpControlledMcCdrTable> cdrList = new ArrayList<IgmpControlledMcCdrTable>();
        try {
            cdrListAll = oltIgmpService.getIgmpControlledMcCdr(entityId);
            cdrList = oltIgmpService.getIgmpControlledMcCdr(entityId, start, limit);
        } catch (Exception e) {
            logger.debug("getIgmpControlledMcCdr error: {}", e);
        }
        json.put("totalProperty", cdrListAll.size());
        json.put("data", cdrList);
        writeDataToAjax(JSONObject.fromObject(json));
        return NONE;
    }

    /**
     * 刷新IGMP的forwarding表
     * 
     * @return
     * @throws IOException 
     */
    @OperationLogProperty(actionName = "oltIgmpAction", operationName = "refreshIgmpForwarding")
    public String refreshIgmpForwarding() throws IOException {
        String message = "success";
        try {
            oltIgmpService.refreshIgmpControlledMulticastPackageTable(entityId);
            oltIgmpService.refreshIgmpProxyParaTable(entityId);
            oltIgmpService.refreshTopMcForwardingSlotTable(entityId);
            oltIgmpService.refreshTopMcForwardingPortTable(entityId);
            oltIgmpService.refreshTopMcForwardingOnuTable(entityId);
            operationResult = OperationLog.SUCCESS;
        } catch (Exception e) {
            message = e.getMessage();
            logger.debug("refreshIgmpForwarding error: {}", e);
            operationResult = OperationLog.FAILURE;
        }
        writeDataToAjax(message);
        return NONE;
    }

    /**
     * 从设备获取获取IGMP协议配置信息
     * 
     * @return
     * @throws Exception
     */
    @OperationLogProperty(actionName = "oltIgmpAction", operationName = "refreshIgmpProtocol")
    public String refreshIgmpProtocol() throws Exception {
        String result;
        try {
            oltIgmpService.refreshIgmpEntityTable(entityId);
            oltIgmpService.refreshIgmpMcParamMgmtObjects(entityId);
            oltIgmpService.refreshIgmpMcSniConfigMgmtObjects(entityId);
            operationResult = OperationLog.SUCCESS;
            result = "success";
        } catch (RefreshIgmpEntityTableException sse) {
            result = sse.getMessage();
            logger.debug("refreshIgmpEntityTable error:{}", sse);
            operationResult = OperationLog.FAILURE;
        } catch (RefreshIgmpMcParamMgmtObjectsException sse) {
            result = sse.getMessage();
            logger.debug("refreshIgmpMcParamMgmtObjects error:{}", sse);
            operationResult = OperationLog.FAILURE;
        } catch (RefreshIgmpMcSniConfigMgmtObjectsException sse) {
            result = sse.getMessage();
            logger.debug("refreshIgmpMcSniConfigMgmtObjects error:{}", sse);
            operationResult = OperationLog.FAILURE;
        }
        writeDataToAjax(result);
        return NONE;
    }

    /**
     * 从设备获取IGMP呼叫记录信息
     * 
     * @return
     * @throws Exception
     */
    @OperationLogProperty(actionName = "oltIgmpAction", operationName = "refreshIgmpHistory")
    public String refreshIgmpHistory() throws Exception {
        String result;
        try {
            oltIgmpService.refreshIgmpControlledMcCdrTable(entityId);
            result = "success";
            operationResult = OperationLog.SUCCESS;
        } catch (RefreshIgmpControlledMcCdrTableException sse) {
            result = sse.getMessage();
            logger.debug("refreshIgmpControlledMcCdrTable error:{}", sse);
            operationResult = OperationLog.FAILURE;
        }
        writeDataToAjax(result);
        return NONE;
    }

    /**
     * 显示SNI组播配置管理
     * 
     * @return String
     */
    public String showSniIgmpMgmt() {
        entity = entityService.getEntity(entityId);
        return SUCCESS;
    }

    public String showIgmpMgmt() {
        entity = entityService.getEntity(entityId);
        return SUCCESS;
    }

    public Entity getEntity() {
        return entity;
    }

    public void setEntity(Entity entity) {
        this.entity = entity;
    }

    public JSONArray getMvidList() {
        return mvidList;
    }

    public void setMvidList(JSONArray mvidList) {
        this.mvidList = mvidList;
    }

    public JSONArray getVlanListObject() {
        return vlanListObject;
    }

    public void setVlanListObject(JSONArray vlanListObject) {
        this.vlanListObject = vlanListObject;
    }

    public Integer getForwardingType() {
        return forwardingType;
    }

    public void setForwardingType(Integer forwardingType) {
        this.forwardingType = forwardingType;
    }

    public IgmpEntityTable getIgmpEntity() {
        return igmpEntity;
    }

    public void setIgmpEntity(IgmpEntityTable igmpEntity) {
        this.igmpEntity = igmpEntity;
    }

    public IgmpMcSniConfigMgmtObjects getSniConfig() {
        return sniConfig;
    }

    public void setSniConfig(IgmpMcSniConfigMgmtObjects sniConfig) {
        this.sniConfig = sniConfig;
    }

    public JSONArray getIgmpEntityObject() {
        return igmpEntityObject;
    }

    public void setIgmpEntityObject(JSONArray igmpEntityObject) {
        this.igmpEntityObject = igmpEntityObject;
    }

    public JSONArray getSniConfigObject() {
        return sniConfigObject;
    }

    public void setSniConfigObject(JSONArray sniConfigObject) {
        this.sniConfigObject = sniConfigObject;
    }

    public IgmpMcParamMgmtObjects getIgmpMcParamMgmt() {
        return igmpMcParamMgmt;
    }

    public void setIgmpMcParamMgmt(IgmpMcParamMgmtObjects igmpMcParamMgmt) {
        this.igmpMcParamMgmt = igmpMcParamMgmt;
    }

    public JSONArray getIgmpMcParamMgmtObject() {
        return igmpMcParamMgmtObject;
    }

    public void setIgmpMcParamMgmtObject(JSONArray igmpMcParamMgmtObject) {
        this.igmpMcParamMgmtObject = igmpMcParamMgmtObject;
    }

    public Integer getIgmpWorkMode() {
        return igmpWorkMode;
    }

    public void setIgmpWorkMode(Integer igmpWorkMode) {
        this.igmpWorkMode = igmpWorkMode;
    }

    public Integer getMaxQueryResponseTime() {
        return maxQueryResponseTime;
    }

    public void setMaxQueryResponseTime(Integer maxQueryResponseTime) {
        this.maxQueryResponseTime = maxQueryResponseTime;
    }

    public Integer getRobustVariable() {
        return robustVariable;
    }

    public void setRobustVariable(Integer robustVariable) {
        this.robustVariable = robustVariable;
    }

    public Integer getQueryInterval() {
        return queryInterval;
    }

    public void setQueryInterval(Integer queryInterval) {
        this.queryInterval = queryInterval;
    }

    public Integer getLastMemberQueryInterval() {
        return lastMemberQueryInterval;
    }

    public void setLastMemberQueryInterval(Integer lastMemberQueryInterval) {
        this.lastMemberQueryInterval = lastMemberQueryInterval;
    }

    public Integer getLastMemberQueryCount() {
        return lastMemberQueryCount;
    }

    public void setLastMemberQueryCount(Integer lastMemberQueryCount) {
        this.lastMemberQueryCount = lastMemberQueryCount;
    }

    public Integer getIgmpVersion() {
        return igmpVersion;
    }

    public void setIgmpVersion(Integer igmpVersion) {
        this.igmpVersion = igmpVersion;
    }

    public Integer getSniType() {
        return sniType;
    }

    public void setSniType(Integer sniType) {
        this.sniType = sniType;
    }

    public Integer getIgmpMaxGroupNum() {
        return igmpMaxGroupNum;
    }

    public void setIgmpMaxGroupNum(Integer igmpMaxGroupNum) {
        this.igmpMaxGroupNum = igmpMaxGroupNum;
    }

    public Long getTopMcMaxBw() {
        return topMcMaxBw;
    }

    public void setTopMcMaxBw(Long topMcMaxBw) {
        this.topMcMaxBw = topMcMaxBw;
    }

    public Integer getTopMcSnoopingAgingTime() {
        return topMcSnoopingAgingTime;
    }

    public void setTopMcSnoopingAgingTime(Integer topMcSnoopingAgingTime) {
        this.topMcSnoopingAgingTime = topMcSnoopingAgingTime;
    }

    public String getTopMcMVlan() {
        return topMcMVlan;
    }

    public void setTopMcMVlan(String topMcMVlan) {
        this.topMcMVlan = topMcMVlan;
    }

    public String getSniPort() {
        return sniPort;
    }

    public void setSniPort(String sniPort) {
        this.sniPort = sniPort;
    }

    public Integer getSniAggPort() {
        return sniAggPort;
    }

    public void setSniAggPort(Integer sniAggPort) {
        this.sniAggPort = sniAggPort;
    }

    public JSONArray getProxyListObject() {
        return proxyListObject;
    }

    public void setProxyListObject(JSONArray proxyListObject) {
        this.proxyListObject = proxyListObject;
    }

    public Integer getSlotNo() {
        return slotNo;
    }

    public void setSlotNo(Integer slotNo) {
        this.slotNo = slotNo;
    }

    public Integer getPortNo() {
        return portNo;
    }

    public void setPortNo(Integer portNo) {
        this.portNo = portNo;
    }

    @Override
    public int getStart() {
        return start;
    }

    @Override
    public void setStart(int start) {
        this.start = start;
    }

    @Override
    public int getLimit() {
        return limit;
    }

    @Override
    public void setLimit(int limit) {
        this.limit = limit;
    }

    public JSONArray getPonList() {
        return ponList;
    }

    public void setPonList(JSONArray ponList) {
        this.ponList = ponList;
    }

    public JSONArray getSlotList() {
        return slotList;
    }

    public void setSlotList(JSONArray slotList) {
        this.slotList = slotList;
    }

    public Integer getProxyId() {
        return proxyId;
    }

    public void setProxyId(Integer proxyId) {
        this.proxyId = proxyId;
    }

}
