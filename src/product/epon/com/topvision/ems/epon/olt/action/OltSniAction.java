/***********************************************************************
 * $Id: OltSlotAction.java,v1.0 2013-10-25 上午10:36:11 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.olt.action;

import java.io.IOException;
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

import com.topvision.ems.epon.exception.SetValueConflictException;
import com.topvision.ems.epon.olt.domain.OltAttribute;
import com.topvision.ems.epon.olt.domain.OltSniAttribute;
import com.topvision.ems.epon.olt.domain.OltSniMacAddress;
import com.topvision.ems.epon.olt.domain.OltSniRedirect;
import com.topvision.ems.epon.olt.domain.OltSniStormSuppressionEntry;
import com.topvision.ems.epon.olt.service.OltPonService;
import com.topvision.ems.epon.olt.service.OltSniService;
import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.network.service.EntityService;
import com.topvision.framework.annotation.OperationLogProperty;
import com.topvision.framework.common.MacUtils;
import com.topvision.framework.exception.engine.SnmpNoResponseException;
import com.topvision.framework.snmp.RowStatus;
import com.topvision.framework.utils.EponConstants;
import com.topvision.framework.utils.EponIndex;
import com.topvision.platform.domain.OperationLog;
import com.topvision.platform.domain.UserContext;

/**
 * @author flack
 * @created @2013-10-25-上午10:36:11
 *
 */
@Controller("oltSniAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class OltSniAction extends AbstractEponAction {
    private static final long serialVersionUID = -8370337827662343986L;
    private final Logger logger = LoggerFactory.getLogger(OltSniAction.class);
    private Entity entity;
    @Autowired
    private EntityService entityService;
    @Autowired
    private OltSniService oltSniService;
    @Autowired
    private OltPonService oltPonService;
    private boolean portIsXGUxFiber = false;
    private Integer sniRedirectDirection;
    private Long sniRedirectSrcPortId;
    private Long sniRedirectDstPortId;
    private String source;
    private Long sniId;
    private Integer modifySniMacAddrVlanIdIndex;
    private String modifyMacAddress;
    private Long modifySniMacAddrPortId;
    private String sniMacOperation;
    private Integer sniMacAddrAgingTime;
    private Integer topSysArpAgingTime;
    private Long sniMacAddrLearnMaxNum;
    private JSONArray slotSniObject = new JSONArray();
    private OltSniStormSuppressionEntry sniStormSuppression;
    private Integer unicastStormEnable;
    private Integer unicastStormInPacketRate;
    private Integer multicastStormEnable;
    private Integer multicastStormInPacketRate;
    private Integer broadcastStormEnable;
    private Integer broadcastStormInPacketRate;
    private String sniMacAddrType;
    private String sniName;
    private Integer sniAdminStatus;
    private Integer sniIsolationStatus;
    private Integer sni15MinPerfStatus;
    private Integer sni24HourPerfStatus;
    private Integer sniAutoNegotiationMode;
    private Integer ctrlFlowEnable;
    private Integer ingressRate;
    private Integer egressRate;
    private OltSniAttribute sniAttribute;
    private String sniPortType;

    /**
     * 显示SNI基本配置信息
     * 
     * @return String
     */
    public String showSniBaseConfig() {
        entity = entityService.getEntity(entityId);
        sniAttribute = oltSniService.getSniAttribute(sniId);
        // Long sniIndex = oltService.getSniIndex(sniId);
        // portIsXGUxFiber = oltService.portIsXGUxFiber(entityId, sniIndex);
        return SUCCESS;
    }

    /**
     * 显示SNI流量控制
     * 
     * @return String
     */
    public String showSniFlowControl() {
        entity = entityService.getEntity(entityId);
        try {
            Long sniIndex = oltSniService.getSniIndex(sniId);
            portIsXGUxFiber = oltSniService.portIsXGUxFiber(entityId, sniIndex);
            OltSniAttribute sniAttribute = oltSniService.getSniAttribute(sniId);
            ingressRate = sniAttribute.getTopSniAttrIngressRate();
            egressRate = sniAttribute.getTopSniAttrEgressRate();
        } catch (Exception e) {
            logger.error("portIsXGUxFiber error: " + e);
        }
        return SUCCESS;
    }

    /**
     * 显示SNI口重定向
     * 
     * @return String
     */
    public String showSniRedirect() {
        entity = entityService.getEntity(entityId);
        return SUCCESS;
    }

    /**
     * 显示添加SNI口重定向
     * 
     * @return String
     */
    public String showAddSniRedirect() {
        return SUCCESS;
    }

    /**
     * 添加SNI口重定向
     * 
     * @return String
     * @throws Exception
     */
    @OperationLogProperty(actionName = "oltSniAction", operationName = "addSniRedirect")
    public String addSniRedirect() throws Exception {
        OltSniRedirect oltSniRedirect = new OltSniRedirect();
        oltSniRedirect.setEntityId(entityId);
        oltSniRedirect.setTopSniRedirectGroupDirection(sniRedirectDirection);
        oltSniRedirect.setTopSniRedirectGroupDstPortId(sniRedirectDstPortId);
        oltSniRedirect.setTopSniRedirectGroupSrcPortId(sniRedirectSrcPortId);
        try {
            oltSniService.addSniRedirect(oltSniRedirect);
            source = getPortLoc(sniRedirectSrcPortId, 1);
            operationResult = OperationLog.SUCCESS;
        } catch (Exception e) {
            logger.debug("addSniRedirect error: {}", e);
            operationResult = OperationLog.FAILURE;
            // 操作失败时，返回前台结果。
            writeDataToAjax("failure");
        }
        return NONE;
    }

    /**
     * 删除SNI口重定向
     * 
     * @return String String
     * @throws Exception
     */
    @OperationLogProperty(actionName = "oltSniAction", operationName = "deleteSniRedirect")
    public String deleteSniRedirect() throws Exception {
        try {
            oltSniService.deleteSniRedirect(entityId, sniId);
            source = getPortLoc(sniId, 1);
            operationResult = OperationLog.SUCCESS;
        } catch (Exception e) {
            logger.debug("deleteSniRedirect error: {}", e);
            operationResult = OperationLog.FAILURE;
            // 操作失败时，返回前台结果。
            writeDataToAjax("failure");
        }
        return NONE;
    }

    /**
     * 获取SNI口重定向数据
     * 
     * @return String String
     * @throws Exception
     */
    public String loadSniRedirect() throws Exception {
        List<OltSniRedirect> oltSniRedirectList = oltSniService.getSniRedirect(entityId);
        logger.debug("loadSniRedirect: {}", oltSniRedirectList);
        writeDataToAjax(JSONArray.fromObject(oltSniRedirectList));
        return NONE;
    }

    /**
     * 加载可用的sni口重定向列表
     * 
     * @return String
     * @throws Exception
     */
    public String loadAvailableSniRedirect() throws Exception {
        List<OltSniRedirect> oltSniRedirectList = oltSniService.getAvailableSniRedirect(entityId);
        logger.debug("loadAvailableSniRedirect: {}", oltSniRedirectList);
        writeDataToAjax(JSONArray.fromObject(oltSniRedirectList));
        return NONE;
    }

    /**
     * 加载所有的sni口重定向列表
     * 
     * @author flackyang
     * @return
     * @throws IOException
     */
    public String loadAllSniRedirect() throws IOException {
        List<OltSniRedirect> allSniRedirect = oltSniService.getAllSniRedirect(entityId);
        logger.debug("loadAvailableSniRedirect: {}", allSniRedirect);
        writeDataToAjax(JSONArray.fromObject(allSniRedirect));
        return NONE;
    }

    /**
     * 显示SNI MAC地址管理
     * 
     * @return String
     */
    public String showSniMacAddrMgmt() {
        entity = entityService.getEntity(entityId);
        return SUCCESS;
    }

    /**
     * 获取SNI口MAC地址管理数据
     * 
     * @return String
     * @throws Exception
     */
    public String loadSniMacAddress() throws Exception {
        List<OltSniMacAddress> oltSniMacAddressList = oltSniService.getSniMacAddress(sniId);
        // add by fanzidong,展示之前需要格式化MAC地址
        UserContext uc = (UserContext) super.getSession().get(UserContext.KEY);
        String macRule = uc.getMacDisplayStyle();
        for (OltSniMacAddress oltSniMacAddress : oltSniMacAddressList) {
            String formatedMac = MacUtils.convertMacToDisplayFormat(oltSniMacAddress.getSniMacAddrIndex(), macRule);
            oltSniMacAddress.setSniMacAddrIndex(formatedMac);
        }
        logger.debug("loadSniMacAddress: {}", oltSniMacAddressList);
        writeDataToAjax(JSONArray.fromObject(oltSniMacAddressList));
        return NONE;
    }

    /**
     * 显示SNI口MAC老化时间
     * 
     * @return String
     */
    public String showSniMacAddressAgingTime() {
        OltAttribute oltAttr = oltService.getOltAttribute(entityId);
        if (oltAttr != null) {
            sniMacAddrAgingTime = oltAttr.getSniMacAddrTableAgingTime();
            topSysArpAgingTime = oltAttr.getTopSysArpAgingTime();
        } else {
            sniMacAddrAgingTime = EponConstants.MAC_ADDR_AGING_TIME;
            topSysArpAgingTime = EponConstants.ARP_AGING_TIME;
        }
        return SUCCESS;
    }

    /**
     * 保存端口MAC老化时间
     * 
     * @return String
     * @throws Exception
     */
    @OperationLogProperty(actionName = "oltSniAction", operationName = "saveSniMacAddressAgingTime")
    public String saveSniMacAddressAgingTime() throws Exception {
        String result = null;
        try {
            oltSniService.modifySniMacAddressAgingTime(entityId, sniMacAddrAgingTime, topSysArpAgingTime);
            operationResult = OperationLog.SUCCESS;
            result = "success";
        } catch (Exception e) {
            logger.debug("saveSniMacAddressAgingTime error: {}", e);
            operationResult = OperationLog.FAILURE;
            result = "fail";
        }
        writeDataToAjax(result);
        return NONE;
    }

    /**
     * 获取SNI口MAC地址最大学习数
     * 
     * @return String
     */
    public String showSniMacAddressMaxLearningNum() {
        OltSniAttribute sniAttr = oltSniService.getSniAttribute(sniId);
        if (sniAttr != null) {
            sniMacAddrLearnMaxNum = sniAttr.getSniMacAddrLearnMaxNum();
        } else {
            sniMacAddrLearnMaxNum = EponConstants.SNI_MAC_DEFAULT_MAX_LEARN_NUM;
        }
        return SUCCESS;
    }

    /**
     * 保存SNI口MAC最大学习数
     * 
     * @return String
     */
    @OperationLogProperty(actionName = "oltSniAction", operationName = "saveSniMacAddressMaxLearningNum")
    public String saveSniMacAddressMaxLearningNum() {
        try {
            oltSniService.modifySniMacAddressMaxLearningNum(entityId, sniId, sniMacAddrLearnMaxNum);
            Long sniIndex = oltSniService.getSniIndex(sniId);
            source = EponIndex.getPortStringByIndex(sniIndex).toString();
            operationResult = OperationLog.SUCCESS;
        } catch (Exception e) {
            logger.debug("saveSniMacAddressMaxLearningNum error: {}", e);
            operationResult = OperationLog.FAILURE;
        }
        return NONE;
    }

    public String showSniMacAddrModify() {
        List<OltSniAttribute> list = oltSniService.getAllSniList(entityId);
        slotSniObject = JSONArray.fromObject(list);
        return SUCCESS;
    }

    /**
     * 显示SNI广播风暴管理
     * 
     * @return String
     */
    public String showSniBroadCastStormMgmt() {
        entity = entityService.getEntity(entityId);
        sniStormSuppression = oltSniService.getSniStormSuppressionBySniId(sniId);
        try {
            Long sniIndex = oltSniService.getSniIndex(sniId);
            portIsXGUxFiber = oltSniService.portIsXGUxFiber(entityId, sniIndex);
        } catch (Exception e) {
            logger.error("portIsXGUxFiber error: " + e);
        }
        return SUCCESS;
    }

    /**
     * 保存SNI口广播风暴抑制参数
     * 
     * @return String
     * @throws Exception
     */
    @OperationLogProperty(actionName = "oltSniAction", operationName = "saveSniBroadCastStorm")
    public String saveSniBroadCastStorm() throws Exception {
        String result;
        OltSniStormSuppressionEntry oltSniStormSuppressionEntry = new OltSniStormSuppressionEntry();
        oltSniStormSuppressionEntry.setEntityId(entityId);
        oltSniStormSuppressionEntry.setSniId(sniId);
        oltSniStormSuppressionEntry.setUnicastStormEnable(unicastStormEnable);
        oltSniStormSuppressionEntry.setUnicastStormInPacketRate(unicastStormInPacketRate);
        oltSniStormSuppressionEntry.setMulticastStormEnable(multicastStormEnable);
        oltSniStormSuppressionEntry.setMulticastStormInPacketRate(multicastStormInPacketRate);
        oltSniStormSuppressionEntry.setBroadcastStormEnable(broadcastStormEnable);
        oltSniStormSuppressionEntry.setBroadcastStormInPacketRate(broadcastStormInPacketRate);
        try {
            oltSniService.modifySniStoreSuppression(oltSniStormSuppressionEntry);
            result = "success";
            source = getPortLoc(sniId, 1);
            operationResult = OperationLog.SUCCESS;
        } catch (Exception e) {
            logger.debug("saveSniBroadCastStorm error: {}", e);
            result = e.getMessage();
            operationResult = OperationLog.FAILURE;
        }
        writeDataToAjax(result);
        return NONE;
    }

    public String refreshSniBroadCastStorm() {
        oltSniService.refreshOltSniStormSuppressionEntry(entityId);
        return NONE;
    }

    /**
     * 修改SNI口MAC地址管理 （包括新增与删除）
     * 
     * @return String
     */
    @OperationLogProperty(actionName = "oltSniAction", operationName = "modifySniMacAddress")
    public String modifySniMacAddress() {
        OltSniMacAddress sniMacAddress = new OltSniMacAddress();
        Long macTmp = new MacUtils(modifyMacAddress).longValue();
        sniMacAddress.setEntityId(entityId);
        sniMacAddress.setSniMacAddrIndexLong(macTmp);
        if (sniMacOperation.equals("Add")) {
            sniMacAddress.setSniMacAddrRowStatus(RowStatus.CREATE_AND_GO);
            sniMacAddress.setSniMacAddrVlanIdIndex(modifySniMacAddrVlanIdIndex);
            sniMacAddress.setSniIndex(modifySniMacAddrPortId);
            sniMacAddress.setSniMacAddrType(sniMacAddrType.equals("static") ? EponConstants.SNI_MAC_TYPE_STATIC
                    : EponConstants.SNI_MAC_TYPE_DYNAMIC);
        } else if (sniMacOperation.equals("Delete")) {
            sniMacAddress.setSniMacAddrRowStatus(RowStatus.DESTORY);
        }
        try {
            oltSniService.modifySniMacAddress(entityId, sniMacAddress);
            operationResult = OperationLog.SUCCESS;
        } catch (Exception e) {
            logger.debug("modifySniMacAddress error: {}", e);
            operationResult = OperationLog.FAILURE;
        }
        return NONE;
    }

    /**
     * 修改SNI口名称与自协商模式
     * 
     * @return String
     * @throws Exception
     */
    @OperationLogProperty(actionName = "oltSniAction", operationName = "modifySniBasicConfig")
    public String modifySniBasicConfig() throws Exception {
        Map<String, String> message = new HashMap<String, String>();
        int autoneg = 0;
        try {
            oltSniService.setSniName(entityId, sniId, sniName);
            if (sniPortType != null) {
                try {
                    OltSniAttribute sniAttribute = oltSniService.getSniAttribute(sniId);
                    if (sniAttribute.getSniAutoNegotiationMode().intValue() != sniAutoNegotiationMode) {
                        autoneg = oltSniService.setSniAutoNegotiationMode(entityId, sniId, sniAutoNegotiationMode);
                    }
                } catch (SnmpNoResponseException e) {
                    // TODO设置端口协商，设备会断网
                }
            }
            source = getPortLoc(sniId, 1);
            message.put("status", autoneg + "");
            operationResult = OperationLog.SUCCESS;
        } catch (SetValueConflictException svce) {
            message.put("message", getString(svce.getMessage(), "epon"));
            operationResult = OperationLog.FAILURE;
            logger.debug("modifySniAutoNegotiationMode error:{}", svce);
        }
        writeDataToAjax(JSONObject.fromObject(message));
        return NONE;
    }

    /**
     * 设置SNI口使能
     * 
     * @return String
     * @throws Exception
     */
    @OperationLogProperty(actionName = "oltSniAction", operationName = "modifySniAdminStatus")
    public String modifySniAdminStatus() throws Exception {
        Map<String, String> message = new HashMap<String, String>();
        try {
            oltSniService.setSniAdminStatus(entityId, sniId, sniAdminStatus);
            source = getPortLoc(sniId, 1);
            operationResult = OperationLog.SUCCESS;
        } catch (SetValueConflictException sce) {
            message.put("message", getString(sce.getMessage(), "epon"));
            operationResult = OperationLog.FAILURE;
            logger.debug("setSniAdminStatus Error:{}", sce);
        }
        writeDataToAjax(JSONObject.fromObject(message));
        return NONE;
    }

    /**
     * 设置SNI口隔离使能
     * 
     * @return String
     * @throws Exception
     */
    @OperationLogProperty(actionName = "oltSniAction", operationName = "modifySniIsolationStatus")
    public String modifySniIsolationStatus() throws Exception {
        Map<String, String> message = new HashMap<String, String>();
        try {
            oltSniService.setSniIsolationStatus(entityId, sniId, sniIsolationStatus);
            source = getPortLoc(sniId, 1);
            operationResult = OperationLog.SUCCESS;
        } catch (SetValueConflictException sce) {
            message.put("message", getString(sce.getMessage(), "epon"));
            operationResult = OperationLog.FAILURE;
            logger.debug("setSniIsolationStatus Error:{}", sce);
        }
        writeDataToAjax(JSONObject.fromObject(message));
        return NONE;
    }

    /**
     * 设置SNI口15分钟性能统计使能状态
     * 
     * @return String
     * @throws Exception
     */
    @OperationLogProperty(actionName = "oltSniAction", operationName = "modifySni15MinPerfStatus")
    public String modifySni15MinPerfStatus() throws Exception {
        Map<String, String> message = new HashMap<String, String>();
        try {
            oltSniService.setSni15MinPerfStatus(entityId, sniId, sni15MinPerfStatus);
            source = getPortLoc(sniId, 1);
            operationResult = OperationLog.SUCCESS;
        } catch (SetValueConflictException sce) {
            message.put("message", getString(sce.getMessage(), "epon"));
            operationResult = OperationLog.FAILURE;
            logger.debug("setSni15MinPerfStatus Error:{}", sce);
        }
        writeDataToAjax(JSONObject.fromObject(message));
        return NONE;
    }

    /**
     * 设置SNI口24小时性能统计使能状态
     * 
     * @return String
     * @throws Exception
     */
    @OperationLogProperty(actionName = "oltSniAction", operationName = "modifySni24HourPerfStatus")
    public String modifySni24HourPerfStatus() throws Exception {
        Map<String, String> message = new HashMap<String, String>();
        try {
            oltSniService.setSni24HourPerfStatus(entityId, sniId, sni24HourPerfStatus);
            source = getPortLoc(sniId, 1);
            operationResult = OperationLog.SUCCESS;
        } catch (SetValueConflictException sce) {
            message.put("message", getString(sce.getMessage(), "epon"));
            operationResult = OperationLog.FAILURE;
            logger.debug("setSni24HourPerfStatus Error:{}", sce);
        }
        writeDataToAjax(JSONObject.fromObject(message));
        return NONE;
    }

    /**
     * SNI口流量控制
     * 
     * @return String
     * @throws Exception
     */
    @OperationLogProperty(actionName = "oltSniAction", operationName = "updateSniCtrlFlow")
    public String updateSniCtrlFlow() throws Exception {
        try {
            oltSniService.setSniFlowControl(entityId, sniId, ingressRate, egressRate);
            source = getPortLoc(sniId, 1);
            operationResult = OperationLog.SUCCESS;
        } catch (Exception e) {
            writeDataToAjax(getString(e.getMessage(), "epon"));
            operationResult = OperationLog.FAILURE;
            logger.debug("updateSniCtrlFlow error: {}", e);
        }
        return NONE;
    }

    /**
     * SNI端口流量控制使能
     * 
     * @return String
     * @throws Exception
     */
    @OperationLogProperty(actionName = "oltSniAction", operationName = "setSniCtrlFlowEnable")
    public String setSniCtrlFlowEnable() throws Exception {
        try {
            oltSniService.setSniCtrlFlowEnable(entityId, sniId, ctrlFlowEnable);
            source = getPortLoc(sniId, 1);
            operationResult = OperationLog.SUCCESS;
        } catch (Exception e) {
            writeDataToAjax(getString(e.getMessage(), "epon"));
            operationResult = OperationLog.FAILURE;
            logger.debug("sniCtrlFlowEnable error:{}", e);
        }
        return NONE;
    }

    /**
     * SNI流量限制信息的采集
     */
    public String refreshSniFlowControl() {
        oltSniService.refreshSniFlowControl(entityId, sniId);
        return NONE;
    }

    /**
     * 获取SNI端口属性
     * 
     * @return String
     */
    public String getOltSniAttribute() {
        sniAttribute = oltSniService.getSniAttribute(sniId);
        return SUCCESS;
    }

    /**
     * 更新SNI端口属性
     * 
     * @return String
     */
    @OperationLogProperty(actionName = "oltSniAction", operationName = "updateOltSniAttribute")
    public String updateOltSniAttribute() {
        try {
            sniAttribute = oltSniService.getSniAttribute(sniId);
            source = getPortLoc(sniId, 1);
            operationResult = OperationLog.SUCCESS;
        } catch (Exception e) {
            logger.debug("updateOltSniAttribute error: {}", e);
            operationResult = OperationLog.FAILURE;
        }
        return NONE;
    }

    /**
     * 获取SNI口信息
     * 
     * @return
     */
    public String refreshSniBaseInfo() {
        oltSniService.refreshSniBaseInfo(entityId, sniId);
        return NONE;
    }

    /**
     * 获取PON口或SNI口的loc
     * 
     * @param id
     * @param type
     *            : 0:ponId, 1:sniId
     * @return
     */
    private String getPortLoc(Long id, Integer type) {
        String loc = "";
        if (type.equals(0)) {// ponId
            id = oltPonService.getPonIndex(id);
        } else if (type.equals(1)) {// sniId
            id = oltSniService.getSniIndex(id);
        }
        loc = EponIndex.getSlotNo(id) + "/" + EponIndex.getSniNo(id);
        return loc;
    }

    public Entity getEntity() {
        return entity;
    }

    public void setEntity(Entity entity) {
        this.entity = entity;
    }

    public boolean isPortIsXGUxFiber() {
        return portIsXGUxFiber;
    }

    public void setPortIsXGUxFiber(boolean portIsXGUxFiber) {
        this.portIsXGUxFiber = portIsXGUxFiber;
    }

    public Integer getSniRedirectDirection() {
        return sniRedirectDirection;
    }

    public void setSniRedirectDirection(Integer sniRedirectDirection) {
        this.sniRedirectDirection = sniRedirectDirection;
    }

    public Long getSniRedirectSrcPortId() {
        return sniRedirectSrcPortId;
    }

    public void setSniRedirectSrcPortId(Long sniRedirectSrcPortId) {
        this.sniRedirectSrcPortId = sniRedirectSrcPortId;
    }

    public Long getSniRedirectDstPortId() {
        return sniRedirectDstPortId;
    }

    public void setSniRedirectDstPortId(Long sniRedirectDstPortId) {
        this.sniRedirectDstPortId = sniRedirectDstPortId;
    }

    @Override
    public String getSource() {
        return source;
    }

    @Override
    public void setSource(String source) {
        this.source = source;
    }

    public Long getSniId() {
        return sniId;
    }

    public void setSniId(Long sniId) {
        this.sniId = sniId;
    }

    public Integer getModifySniMacAddrVlanIdIndex() {
        return modifySniMacAddrVlanIdIndex;
    }

    public void setModifySniMacAddrVlanIdIndex(Integer modifySniMacAddrVlanIdIndex) {
        this.modifySniMacAddrVlanIdIndex = modifySniMacAddrVlanIdIndex;
    }

    public String getModifyMacAddress() {
        return modifyMacAddress;
    }

    public void setModifyMacAddress(String modifyMacAddress) {
        this.modifyMacAddress = modifyMacAddress;
    }

    public Long getModifySniMacAddrPortId() {
        return modifySniMacAddrPortId;
    }

    public void setModifySniMacAddrPortId(Long modifySniMacAddrPortId) {
        this.modifySniMacAddrPortId = modifySniMacAddrPortId;
    }

    public String getSniMacOperation() {
        return sniMacOperation;
    }

    public void setSniMacOperation(String sniMacOperation) {
        this.sniMacOperation = sniMacOperation;
    }

    public Integer getSniMacAddrAgingTime() {
        return sniMacAddrAgingTime;
    }

    public void setSniMacAddrAgingTime(Integer sniMacAddrAgingTime) {
        this.sniMacAddrAgingTime = sniMacAddrAgingTime;
    }

    public Integer getTopSysArpAgingTime() {
        return topSysArpAgingTime;
    }

    public void setTopSysArpAgingTime(Integer topSysArpAgingTime) {
        this.topSysArpAgingTime = topSysArpAgingTime;
    }

    public Long getSniMacAddrLearnMaxNum() {
        return sniMacAddrLearnMaxNum;
    }

    public void setSniMacAddrLearnMaxNum(Long sniMacAddrLearnMaxNum) {
        this.sniMacAddrLearnMaxNum = sniMacAddrLearnMaxNum;
    }

    public JSONArray getSlotSniObject() {
        return slotSniObject;
    }

    public void setSlotSniObject(JSONArray slotSniObject) {
        this.slotSniObject = slotSniObject;
    }

    public OltSniStormSuppressionEntry getSniStormSuppression() {
        return sniStormSuppression;
    }

    public void setSniStormSuppression(OltSniStormSuppressionEntry sniStormSuppression) {
        this.sniStormSuppression = sniStormSuppression;
    }

    public Integer getUnicastStormEnable() {
        return unicastStormEnable;
    }

    public void setUnicastStormEnable(Integer unicastStormEnable) {
        this.unicastStormEnable = unicastStormEnable;
    }

    public Integer getUnicastStormInPacketRate() {
        return unicastStormInPacketRate;
    }

    public void setUnicastStormInPacketRate(Integer unicastStormInPacketRate) {
        this.unicastStormInPacketRate = unicastStormInPacketRate;
    }

    public Integer getMulticastStormEnable() {
        return multicastStormEnable;
    }

    public void setMulticastStormEnable(Integer multicastStormEnable) {
        this.multicastStormEnable = multicastStormEnable;
    }

    public Integer getMulticastStormInPacketRate() {
        return multicastStormInPacketRate;
    }

    public void setMulticastStormInPacketRate(Integer multicastStormInPacketRate) {
        this.multicastStormInPacketRate = multicastStormInPacketRate;
    }

    public Integer getBroadcastStormEnable() {
        return broadcastStormEnable;
    }

    public void setBroadcastStormEnable(Integer broadcastStormEnable) {
        this.broadcastStormEnable = broadcastStormEnable;
    }

    public Integer getBroadcastStormInPacketRate() {
        return broadcastStormInPacketRate;
    }

    public void setBroadcastStormInPacketRate(Integer broadcastStormInPacketRate) {
        this.broadcastStormInPacketRate = broadcastStormInPacketRate;
    }

    public String getSniMacAddrType() {
        return sniMacAddrType;
    }

    public void setSniMacAddrType(String sniMacAddrType) {
        this.sniMacAddrType = sniMacAddrType;
    }

    public String getSniName() {
        return sniName;
    }

    public void setSniName(String sniName) {
        this.sniName = sniName;
    }

    public Integer getSniAdminStatus() {
        return sniAdminStatus;
    }

    public void setSniAdminStatus(Integer sniAdminStatus) {
        this.sniAdminStatus = sniAdminStatus;
    }

    public Integer getSniIsolationStatus() {
        return sniIsolationStatus;
    }

    public void setSniIsolationStatus(Integer sniIsolationStatus) {
        this.sniIsolationStatus = sniIsolationStatus;
    }

    public Integer getSni15MinPerfStatus() {
        return sni15MinPerfStatus;
    }

    public void setSni15MinPerfStatus(Integer sni15MinPerfStatus) {
        this.sni15MinPerfStatus = sni15MinPerfStatus;
    }

    public Integer getSni24HourPerfStatus() {
        return sni24HourPerfStatus;
    }

    public void setSni24HourPerfStatus(Integer sni24HourPerfStatus) {
        this.sni24HourPerfStatus = sni24HourPerfStatus;
    }

    public Integer getSniAutoNegotiationMode() {
        return sniAutoNegotiationMode;
    }

    public void setSniAutoNegotiationMode(Integer sniAutoNegotiationMode) {
        this.sniAutoNegotiationMode = sniAutoNegotiationMode;
    }

    public Integer getCtrlFlowEnable() {
        return ctrlFlowEnable;
    }

    public void setCtrlFlowEnable(Integer ctrlFlowEnable) {
        this.ctrlFlowEnable = ctrlFlowEnable;
    }

    public Integer getIngressRate() {
        return ingressRate;
    }

    public void setIngressRate(Integer ingressRate) {
        this.ingressRate = ingressRate;
    }

    public Integer getEgressRate() {
        return egressRate;
    }

    public void setEgressRate(Integer egressRate) {
        this.egressRate = egressRate;
    }

    public OltSniAttribute getSniAttribute() {
        return sniAttribute;
    }

    public void setSniAttribute(OltSniAttribute sniAttribute) {
        this.sniAttribute = sniAttribute;
    }

    public String getSniPortType() {
        return sniPortType;
    }

    public void setSniPortType(String sniPortType) {
        this.sniPortType = sniPortType;
    }

}
