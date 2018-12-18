/***********************************************************************
 * $Id: OltSlotAction.java,v1.0 2013-10-25 上午10:36:11 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.olt.action;

import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.struts2.ServletActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.topvision.ems.epon.exception.BoardPreConfigChangedException;
import com.topvision.ems.epon.exception.SetValueConflictException;
import com.topvision.ems.epon.olt.domain.OltFanAttribute;
import com.topvision.ems.epon.olt.service.OltSlotService;
import com.topvision.ems.epon.topology.domain.OltDiscoveryData;
import com.topvision.ems.message.Message;
import com.topvision.ems.message.MessagePusher;
import com.topvision.ems.network.service.DiscoveryService;
import com.topvision.ems.network.service.EntityService;
import com.topvision.framework.annotation.OperationLogProperty;
import com.topvision.platform.domain.OperationLog;
import com.topvision.platform.zetaframework.var.UnitConfigConstant;

/**
 * @author flack
 * @created @2013-10-25-上午10:36:11
 *
 */
@Controller("oltSlotAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class OltSlotAction extends AbstractEponAction {
    private static final long serialVersionUID = -2311981697043812309L;
    private final Logger logger = LoggerFactory.getLogger(OltSlotAction.class);
    @Autowired
    private MessagePusher messagePusher;
    @Autowired
    private OltSlotService oltSlotService;
    @Autowired
    private EntityService entityService;
    @Autowired
    private DiscoveryService<OltDiscoveryData> discoveryService;
    private Integer preConfigType;
    private Long slotId;
    private OltFanAttribute fanAttribute;
    private Long fanCardId;
    private Long boardId;
    private Integer oltFanSpeedControlLevel;
    private Integer topSysBdTempDetectEnable;
    private int start;
    private Integer boardStatus;
    private Long entityId;
    private String currentId;
    private Integer actualConfigType;
    private Long deviceType;

    /**
     * 显示预配置管理
     * 
     * @return String
     */
    public String showPreConfigMgmt() {
        deviceType = entityService.getEntity(entityId).getTypeId();
        return SUCCESS;
    }

    /**
     * 修改预配置类型
     * 
     * @return String
     * @throws Exception
     */
    @OperationLogProperty(actionName = "oltSlotAction", operationName = "modifyPreconfigMgmt")
    public String modifyPreconfigMgmt() throws Exception {
        Map<String, String> message = new HashMap<String, String>();
        try {
            oltSlotService.setOltBdPreConfigType(entityId, slotId, preConfigType);
            operationResult = OperationLog.SUCCESS;
        } catch (SetValueConflictException svce) {
            message.put("message", getString(svce.getMessage(), "epon"));
            operationResult = OperationLog.FAILURE;
            logger.debug("modifyPreconfigMgmt Error:{}", svce);
        } catch (BoardPreConfigChangedException bpcce) {
            message.put("message", getString(bpcce.getMessage(), "epon"));
            message.put("type", "boardPreConfigChanged");
            operationResult = OperationLog.FAILURE;
            logger.debug("modifyPreconfigMgmt BoardPreConfigChanged Error:{}", bpcce);
        } catch (Exception e) {
            message.put("message", "error");
            operationResult = OperationLog.FAILURE;
            logger.debug("modifyPreconfigMgmt Error:{}", e);
        }
        writeDataToAjax(JSONObject.fromObject(message));
        return NONE;
    }

    /**
     * 刷新槽位信息
     * 
     * @return String
     */
    public String refreshBoardInfo() {
        //oltSlotService.refreshBoardInfo(entityId, slotId);
        //废弃了刷新板卡的方法，改为直接刷新设备
        discoveryService.refresh(entityId);
        return NONE;
    }

    /**
     * 板卡复位
     * 
     * @return String
     * @throws Exception
     */
    @OperationLogProperty(actionName = "oltSlotAction", operationName = "resetOltBoard")
    public String resetOltBoard() throws Exception {
        String result;
        try {
            oltSlotService.resetOltBoard(entityId, boardId);
            result = "success";
            operationResult = OperationLog.SUCCESS;
        } catch (Exception e) {
            logger.debug("resetOltBoard error: {}", e);
            result = e.getMessage();
            operationResult = OperationLog.FAILURE;
        }
        writeDataToAjax(result);
        return NONE;
    }

    /**
     * 获得风扇属性
     * 
     * @return String
     */
    public String getOltFanAttribute() {
        fanAttribute = oltSlotService.getFanAttribute(fanCardId);
        return SUCCESS;
    }

    /**
     * 风扇速度调节
     * 
     * @return String
     * @throws Exception
     */
    @OperationLogProperty(actionName = "oltSlotAction", operationName = "fanSpeedAdjust")
    public String fanSpeedAdjust() throws Exception {
        Map<String, String> message = new HashMap<String, String>();
        try {
            oltSlotService.setOltFanSpeedControl(entityId, fanCardId, oltFanSpeedControlLevel);
            operationResult = OperationLog.SUCCESS;
        } catch (SetValueConflictException svce) {
            message.put("message", getString(svce.getMessage(), "epon"));
            operationResult = OperationLog.FAILURE;
            logger.debug("fanSpeedAdjust Error:{}", svce);
        }
        writeDataToAjax(JSONObject.fromObject(message));
        return NONE;
    }

    /**
     * 设置板卡温度探测使能
     * 
     * @return String
     * @throws Exception
     */
    @OperationLogProperty(actionName = "oltSlotAction", operationName = "setTempDetectEnable")
    public String setTempDetectEnable() throws Exception {
        Map<String, String> message = new HashMap<String, String>();
        try {
            oltSlotService.updateSlotBdTempDetectEnable(entityId, boardId, topSysBdTempDetectEnable);
        } catch (Exception sce) {
            message.put("message", getString(sce.getMessage(), "epon"));
            logger.debug("setTempDetectEnable Error:{}", sce);
        }
        writeDataToAjax(JSONObject.fromObject(message));
        return NONE;
    }

    /**
     * 刷新板卡温度
     * 
     * @return String
     * @throws Exception
     */
    public String refreshBdTemperature() throws Exception {
        String message = "false";
        try {
            // 设备板卡问题使能后，立即采集会返回0，休眠10s后采集温度稳定（10s为经验值）
            Thread.sleep(10000);
            message = String.valueOf(UnitConfigConstant.translateTemperature(oltSlotService.refreshBdTemperature(
                    entityId, boardId)));
        } catch (Exception sce) {
            logger.debug("refreshBdTemperature Error:{}", sce);
        } finally {
            JSONObject json = new JSONObject();
            json.put("message", message);
            json.put("boardId", boardId);
            json.put("index", start);
            Message ms = new Message("refreshBdTemp");
            String id = ServletActionContext.getRequest().getSession().getId();
            ms.addSessionId(id);
            ms.setData(json.toString());
            messagePusher.sendMessage(ms);
        }
        return NONE;
    }

    /**
     * 板卡使能设置
     * 
     * @return String
     * @throws Exception
     */
    @OperationLogProperty(actionName = "oltSlotAction", operationName = "boardAdminStatusConfig")
    public String boardAdminStatusConfig() throws Exception {
        String message;
        try {
            oltSlotService.setBoardAdminStatus(entityId, slotId, boardStatus);
            message = "success";
        } catch (Exception e) {
            message = getString(e.getMessage(), "epon");
            logger.debug("config board status error:{}", e);
        }
        writeDataToAjax(message);
        return NONE;
    }

    /**
     * 获取设备风扇转速
     * 
     * @throws Exception
     */
    public void getFanRealSpeed() throws Exception {
        String message = "false";
        try {
            Integer speed = oltSlotService.getFanRealSpeed(entityId, fanCardId);
            message = speed.toString();
        } catch (Exception e) {
            logger.debug("getFanRealSpeed error:{}", e);
        }
        writeDataToAjax(message);
    }

    public MessagePusher getMessagePusher() {
        return messagePusher;
    }

    public void setMessagePusher(MessagePusher messagePusher) {
        this.messagePusher = messagePusher;
    }

    public Integer getPreConfigType() {
        return preConfigType;
    }

    public void setPreConfigType(Integer preConfigType) {
        this.preConfigType = preConfigType;
    }

    public Long getSlotId() {
        return slotId;
    }

    public void setSlotId(Long slotId) {
        this.slotId = slotId;
    }

    public OltFanAttribute getFanAttribute() {
        return fanAttribute;
    }

    public void setFanAttribute(OltFanAttribute fanAttribute) {
        this.fanAttribute = fanAttribute;
    }

    public Long getFanCardId() {
        return fanCardId;
    }

    public void setFanCardId(Long fanCardId) {
        this.fanCardId = fanCardId;
    }

    public Long getBoardId() {
        return boardId;
    }

    public void setBoardId(Long boardId) {
        this.boardId = boardId;
    }

    public Integer getOltFanSpeedControlLevel() {
        return oltFanSpeedControlLevel;
    }

    public void setOltFanSpeedControlLevel(Integer oltFanSpeedControlLevel) {
        this.oltFanSpeedControlLevel = oltFanSpeedControlLevel;
    }

    public Integer getTopSysBdTempDetectEnable() {
        return topSysBdTempDetectEnable;
    }

    public void setTopSysBdTempDetectEnable(Integer topSysBdTempDetectEnable) {
        this.topSysBdTempDetectEnable = topSysBdTempDetectEnable;
    }

    @Override
    public int getStart() {
        return start;
    }

    @Override
    public void setStart(int start) {
        this.start = start;
    }

    public Integer getBoardStatus() {
        return boardStatus;
    }

    public void setBoardStatus(Integer boardStatus) {
        this.boardStatus = boardStatus;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public String getCurrentId() {
        return currentId;
    }

    public void setCurrentId(String currentId) {
        this.currentId = currentId;
    }

    public Integer getActualConfigType() {
        return actualConfigType;
    }

    public void setActualConfigType(Integer actualConfigType) {
        this.actualConfigType = actualConfigType;
    }

    public Long getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(Long deviceType) {
        this.deviceType = deviceType;
    }

}
