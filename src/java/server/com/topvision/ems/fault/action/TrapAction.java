package com.topvision.ems.fault.action;

import net.sf.json.JSONObject;

import org.apache.struts2.ServletActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.topvision.ems.fault.domain.NbiConfig;
import com.topvision.ems.fault.service.FaultService;
import com.topvision.ems.fault.service.TrapService;
import com.topvision.framework.snmp.Trap;
import com.topvision.framework.web.struts2.BaseAction;
import com.topvision.framework.web.util.HttpResponseUtils;
import com.topvision.platform.domain.Action;
import com.topvision.platform.service.ActionService;

@Controller("trapAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class TrapAction extends BaseAction {
    private static final long serialVersionUID = -1155836168471483010L;
    private static final Logger logger = LoggerFactory.getLogger(TrapAction.class);
    public static final int ACTION_TYPE = 3;
    public static final String SYS_OBJECT_ID = "1.3.6.1.2.1.1.2.0";
    public static final String OID = "1.3.1";
    public static final String CONTENT = "1.3.2";
    private long actionId;
    private String name = null;
    private String address = null;
    private int port = 162;
    private String sysObjectID = null;
    private String community = "public";
    private String content = null;
    private String oid = null;
    @Autowired
    private FaultService faultService;
    @Autowired
    @Qualifier(value = "trapService")
    private ActionService actionService;
    @Autowired
    private TrapService trapService;
    private NbiConfig nbiConfig;
    private JSONObject nbiConfigJson;

    private String nbiIpAddress;
    private String nbiPort;
    private Boolean nbiHeartBeatSwitch;
    private Integer nbiHeartBeatInterval;
    private String nbiCommunity;
    private String nbiHeartBeatLabel;

    /**
     * 跳转NBI全局配置
     * 
     * @return
     */
    public String showNorthBoundConfig() {
        nbiConfig = trapService.getNbiConfig();
        setNbiConfigJson(JSONObject.fromObject(nbiConfig));
        return SUCCESS;
    }

    /**
     * saveNbiTrapConfig
     * 
     * @return
     * @throws Exception
     */
    public String saveNorthBoundConfig() throws Exception {
        String message;
        try {
            NbiConfig nbiConfig = new NbiConfig(nbiIpAddress, nbiPort, nbiHeartBeatSwitch, nbiHeartBeatInterval,
                    nbiCommunity, nbiHeartBeatLabel);
            trapService.modifyNbiConfig(nbiConfig);
            message = "success";
        } catch (Exception e) {
            logger.error("saveNbiTrapConfig error:{}", e);
            message = "error";
        }
        writeDataToAjax(message);
        return NONE;
    }

    /**
     * Send North Test Alaram
     * 
     * @return
     * @throws Exception
     */
    public String sendNorthBoundTestTrap() throws Exception {
        String message;
        try {
            NbiConfig nbiConfig = new NbiConfig(nbiIpAddress, nbiPort, nbiHeartBeatSwitch, nbiHeartBeatInterval,
                    nbiCommunity, nbiHeartBeatLabel);
            trapService.sendNorthBoundTestAlarm(nbiConfig);
            message = "success";
        } catch (Exception e) {
            logger.error("sendNorthBoundTestAlarm error:{}", e);
            message = "error";
        }
        writeDataToAjax(message);
        return NONE;
    }

    private Action createAction() {
        Action action = new Action();
        action.setActionTypeId(ACTION_TYPE);
        action.setEnabled(true);
        action.setName(name);
        Trap trap = new Trap();
        trap.setAddress(address);
        trap.setPort(port);
        trap.setSecurityName(community.getBytes());
        trap.addVariableBinding(OID, oid);
        trap.addVariableBinding(CONTENT, content);
        trap.addVariableBinding(SYS_OBJECT_ID, sysObjectID);
        action.setParamsObject(trap);
        return action;
    }

    public String newTrapAction() {
        if (name == null) {
            return SUCCESS;
        }
        faultService.insertAction(createAction());
        return NONE;
    }

    public String sendTestTrap() throws Exception {
        JSONObject json = new JSONObject();
        try {
            json.put("success", true);
            Trap trap = new Trap();
            trap.setAddress(address);
            trap.setPort(port);
            trap.setSecurityName(community.getBytes());
            trap.addVariableBinding(OID, oid);
            trap.addVariableBinding(CONTENT, content);
            trap.addVariableBinding(SYS_OBJECT_ID, sysObjectID);
            actionService.sendAction(createAction(), null, content);
            json.put("feedback", "Send a test Trap to " + address);
        } catch (Exception ex) {
            logger.debug("send test trap error:", ex);
            json.put("success", false);
            json.put("feedback", ex.getMessage());
            // throw ex;
        } finally {
            writeDataToAjax(json);
        }
        return NONE;
    }

    public String showTrapAction() {
        Action action = faultService.getActionById(actionId);
        name = action.getName();
        Trap trap = (Trap) action.getParamsObject();
        address = trap.getAddress();
        port = trap.getPort();
        community = new String(trap.getSecurityName());
        oid = trap.getVarialbleValue(OID);
        content = trap.getVarialbleValue(CONTENT);
        sysObjectID = trap.getVarialbleValue(SYS_OBJECT_ID);
        return SUCCESS;
    }

    public String updateTrapAction() {
        Action action = faultService.getActionById(actionId);
        action.setName(name);
        Trap trap = new Trap();
        trap.setAddress(address);
        trap.setPort(port);
        trap.setSecurityName(community.getBytes());
        trap.addVariableBinding(OID, oid);
        trap.addVariableBinding(CONTENT, content);
        trap.addVariableBinding(SYS_OBJECT_ID, sysObjectID);
        action.setParamsObject(trap);
        faultService.updateAction(action);
        return NONE;
    }

    public long getActionId() {
        return actionId;
    }

    public String getAddress() {
        return address;
    }

    public String getCommunity() {
        return community;
    }

    public String getContent() {
        return content;
    }

    public FaultService getFaultService() {
        return faultService;
    }

    public String getName() {
        return name;
    }

    public String getOid() {
        return oid;
    }

    public int getPort() {
        return port;
    }

    public String getSysObjectID() {
        return sysObjectID;
    }

    public void setActionId(long actionId) {
        this.actionId = actionId;
    }

    public void setActionService(ActionService actionService) {
        this.actionService = actionService;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setCommunity(String community) {
        this.community = community;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setFaultService(FaultService faultService) {
        this.faultService = faultService;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void setSysObjectID(String sysObjectID) {
        this.sysObjectID = sysObjectID;
    }

    public NbiConfig getNbiConfig() {
        return nbiConfig;
    }

    public void setNbiConfig(NbiConfig nbiConfig) {
        this.nbiConfig = nbiConfig;
    }

    public JSONObject getNbiConfigJson() {
        return nbiConfigJson;
    }

    public void setNbiConfigJson(JSONObject nbiConfigJson) {
        this.nbiConfigJson = nbiConfigJson;
    }

    public String getNbiIpAddress() {
        return nbiIpAddress;
    }

    public void setNbiIpAddress(String nbiIpAddress) {
        this.nbiIpAddress = nbiIpAddress;
    }

    public String getNbiPort() {
        return nbiPort;
    }

    public void setNbiPort(String nbiPort) {
        this.nbiPort = nbiPort;
    }

    public Boolean getNbiHeartBeatSwitch() {
        return nbiHeartBeatSwitch;
    }

    public void setNbiHeartBeatSwitch(Boolean nbiHeartBeatSwitch) {
        this.nbiHeartBeatSwitch = nbiHeartBeatSwitch;
    }

    public Integer getNbiHeartBeatInterval() {
        return nbiHeartBeatInterval;
    }

    public void setNbiHeartBeatInterval(Integer nbiHeartBeatInterval) {
        this.nbiHeartBeatInterval = nbiHeartBeatInterval;
    }

    public String getNbiCommunity() {
        return nbiCommunity;
    }

    public void setNbiCommunity(String nbiCommunity) {
        this.nbiCommunity = nbiCommunity;
    }

    public String getNbiHeartBeatLabel() {
        return nbiHeartBeatLabel;
    }

    public void setNbiHeartBeatLabel(String nbiHeartBeatLabel) {
        this.nbiHeartBeatLabel = nbiHeartBeatLabel;
    }

}
