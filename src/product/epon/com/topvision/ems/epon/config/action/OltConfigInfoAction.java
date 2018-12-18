/***********************************************************************
 * $Id: OltConfigInfoAction.java,v1.0 2013-10-26 上午9:38:29 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.config.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.topvision.ems.epon.config.service.OltConfigInfoService;
import com.topvision.ems.epon.domain.OltVlanInterface;
import com.topvision.ems.epon.exception.SetValueConflictException;
import com.topvision.ems.epon.olt.action.AbstractEponAction;
import com.topvision.ems.epon.olt.domain.OltAttribute;
import com.topvision.ems.epon.vlan.domain.VlanAttribute;
import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.network.service.EntityService;
import com.topvision.framework.annotation.OperationLogProperty;
import com.topvision.framework.common.MacUtils;
import com.topvision.framework.snmp.SnmpParam;
import com.topvision.platform.domain.OperationLog;
import com.topvision.platform.domain.UserContext;
import com.topvision.platform.service.SystemPreferencesService;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;

/**
 * @author flack
 * @created @2013-10-26-上午9:38:29
 *
 */
@Controller("oltConfigInfoAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class OltConfigInfoAction extends AbstractEponAction {
    private static final long serialVersionUID = -4736370563194146498L;
    private final Logger logger = LoggerFactory.getLogger(OltConfigInfoAction.class);
    @Autowired
    private OltConfigInfoService oltConfigInfoService;
    @Autowired
    private EntityService entityService;
    @Autowired
    private SystemPreferencesService systemPreferencesService;
    // basic property
    private String oltName;
    private String entityContact;
    private String alias;
    private String location;
    private Integer rack;
    private Integer frame;
    private Integer frameEnd;
    private String desc;
    private String note;
    // inband/outband ip-mask
    private String inbandIp;
    private String inbandMask;
    private String outbandIp;
    private String outbandMask;
    // server side snmp
    private String emsIpAddress;
    private byte serverSnmpVersion;
    private String readCommunity;
    private String writeCommunity;
    // entity side snmp
    private String manageHostMask;
    private Integer snmpVersion;
    private String snmpCommunity;
    private String snmpWriteCommunity;
    private String snmpHostIp;
    private Integer inbandVlanId;
    private Long entityId;
    private Integer operationResult;

    private Integer securityLevel;
    private String authProtocol;
    private String username;
    private String authPassword;
    private String privProtocol;
    private String privPassword;
    private Entity entity;
    private OltAttribute oltAttribute;
    private JSONArray oltVlanInterfaceList;
    private SnmpParam snmpParam;
    private String oldIp;
    private String newIp;
    private Integer modifyEmsSnmpParam;
    private String entityIp;
    private String formatedOutbandMac;
    private String formatedInbandMac;
    private String cameraSwitch;

    /**
    * 显示配置信息
    * 
    * @return String
    */
    public String showOltConfigJsp() {
        Properties properties = systemPreferencesService.getModulePreferences("camera");
        cameraSwitch = properties.getProperty("camera.switch");
        oltAttribute = oltService.getOltAttribute(entityId);
        //add by fanzidong,需要在展示前格式化MAC地址
        UserContext uc = (UserContext) super.getSession().get(UserContext.KEY);
        String displayRule = uc.getMacDisplayStyle();
        formatedOutbandMac = MacUtils.convertMacToDisplayFormat(oltAttribute.getOutbandMac(), displayRule);
        formatedInbandMac = MacUtils.convertMacToDisplayFormat(oltAttribute.getInbandMac(), displayRule);

        entity = entityService.getEntity(entityId);
        snmpParam = entityService.getSnmpParamByEntity(entityId);
        //处理读写共同体中的特殊字符'和"
        snmpParam.setCommunity(snmpParam.getCommunity().replace("\"", "&quot;"));
        snmpParam.setWriteCommunity(snmpParam.getWriteCommunity().replace("\"", "&quot;"));
        List<OltVlanInterface> vlanInterfaceList = oltConfigInfoService.getVlanInterfaceList(entityId);
        List<Map<String, String>> rejectedIpAndMask = new ArrayList<Map<String, String>>();
        for (OltVlanInterface vi : vlanInterfaceList) {
            String ipAddress = vi.getVlanIpAddress();
            String ipMask = vi.getVlanIpMask();
            Map<String, String> map = new HashMap<String, String>();
            map.put("ip", ipAddress);
            map.put("mask", ipMask);
            rejectedIpAndMask.add(map);
        }
        oltVlanInterfaceList = JSONArray.fromObject(rejectedIpAndMask);
        return SUCCESS;
    }

    /**
     * 加载可用的vlan列表
     * 由于vlan的数量往往较多，处理时的开销比较大，所以使用一个列表记录属于vlan虚接口的vlan号，这个列表较小，处理较快，将vlan虚接口从列表中删除
     * @return
     * @throws IOException 
     */
    public String loadAvailableVlanList() throws IOException {
        List<VlanAttribute> oltVlanIndexList = oltConfigInfoService.getAvailableVlanIndexList(entityId);
        JSONObject json = new JSONObject();
        json.put("data", oltVlanIndexList);
        json.write(response.getWriter());
        return NONE;
    }

    /**
     * 保存基本配置信息
     * 
     * @return
     * @throws IOException
     */
    @OperationLogProperty(actionName = "oltConfigInfoAction", operationName = "modifyBasicInfo")
    public String saveBasicConfig() throws IOException {
        JSONObject json = new JSONObject();
        String result = "ok";
        OltAttribute baseInfo = new OltAttribute();
        baseInfo.setEntityId(entityId);
        baseInfo.setOltName(oltName);
        baseInfo.setSysContact(entityContact);
        baseInfo.setSysLocation(location);
        baseInfo.setTopSysOltRackNum(rack);
        baseInfo.setTopSysOltFrameNum(frame);
        Entity entity = new Entity();
        //entity.setName(alias);
        //entity.setNote(note.replace("\n", ""));
        try {
            oltConfigInfoService.saveBasicConfig(baseInfo, entity);// 修改基本信息
            operationResult = OperationLog.SUCCESS;
        } catch (SetValueConflictException e) {
            result = "setValueEroor";
            operationResult = OperationLog.FAILURE;
            logger.debug("saveBasicConfig error: {}", e);
        } catch (Exception ex) {
            result = "unknownError";
            operationResult = OperationLog.FAILURE;
            logger.debug("saveBasicConfig error: {}", ex);
        }
        json.put("message", result);
        json.write(response.getWriter());
        return NONE;
    }

    /**
     * 保存带内带外ip/mask
     * 
     * @return
     * @throws IOException 
     */
    @OperationLogProperty(actionName = "oltConfigInfoAction", operationName = "saveInbandParamAndIP")
    public String saveInbandParamAndIP() throws IOException {
        JSONObject json = new JSONObject();
        String result = "success";
        SnmpParam param = entityService.getSnmpParamByEntity(entityId);
        OltAttribute ipMaskInfo = new OltAttribute();
        ipMaskInfo.setEntityId(entityId);
        // 然后再重新设置PVID,MaxBand,portIndexs等
        ipMaskInfo.setInbandIp(inbandIp);
        ipMaskInfo.setInbandMask(inbandMask);
        ipMaskInfo.setInbandVlanId(inbandVlanId);
        //ipMaskInfo.setTopSysInBandMaxBw(maxBand);
        try {
            oltConfigInfoService.modifyInBandConfig(entityId, ipMaskInfo, param);
            //更改成功过后同时更新entityAddress表项
            oltConfigInfoService.updateEntityIpAddress(entityId, inbandIp, oldIp);
        } catch (Exception e) {
            result = "fail";
            logger.info("modify saveInbandParamAndIP error {0}", e);
        }
        //modify lzt  在修改带内IP时不需要修改网管侧的管理IP
        /*//同时更新网管侧Ip地址
        if (entityService.selectEntityId(entityId, oldIp) != null) {
            Entity entity = new Entity();
            entity.setIp(inbandIp);
            entity.setEntityId(entityId);
            oltService.updateOltEntityIp(entity);
        }*/
        json.put("message", result);
        json.write(response.getWriter());
        return NONE;
    }

    @OperationLogProperty(actionName = "oltConfigInfoAction", operationName = "saveOutbandParamAndIP")
    public String saveOutbandParamAndIP() throws IOException {
        JSONObject json = new JSONObject();
        String result = "success";
        SnmpParam param = entityService.getSnmpParamByEntity(entityId);
        OltAttribute ipMaskInfo = new OltAttribute();
        ipMaskInfo.setEntityId(entityId);
        ipMaskInfo.setOutbandIp(outbandIp);
        ipMaskInfo.setOutbandMask(outbandMask);
        try {
            oltConfigInfoService.updateOutbandParamInfo(ipMaskInfo, param);
            //更改成功过后同时更新entityAddress表项
            oltConfigInfoService.updateEntityIpAddress(entityId, outbandIp, oldIp);
        } catch (Exception e) {
            result = "fail";
            logger.info("modify outbandParamAndIp error {0}", e);
        }
        //modify lzt  修改带外IP时不需要修改网管侧的管理IP
        /* //同时更新网管侧Ip地址
         Entity entity = new Entity();
         entity.setIp(outbandIp);
         entity.setEntityId(entityId);
         oltService.updateOltEntityIp(entity);*/
        json.put("message", result);
        json.write(response.getWriter());
        return NONE;
    }

    /**
     * 保存服务器端SNMP参数
     * 
     * @return
     * @throws Exception 
     */
    @OperationLogProperty(actionName = "oltConfigInfoAction", operationName = "saveServerInfo")
    public String saveServerInfo() throws Exception {
        SnmpParam param = entityService.getSnmpParamByEntity(entityId);
        param.setEntityId(entityId);
        Entity entity = new Entity();
        entity.setIp(emsIpAddress);
        entity.setEntityId(entityId);
        entity.setName(alias);
        entity.setNote(note.replace("\n", ""));
        String result = entityService.updateEntityIpInfo(entityId, oldIp, emsIpAddress);
        if (result == "success") {
            oltConfigInfoService.updateOltEntityIp(entity);
        } else {
            logger.info("saveServerInfo fail");
        }
        writeDataToAjax(result);
        return NONE;
    }

    /**
     * 保存服务器端SNMP参数
     * 
     * @return
     * @throws Exception 
     */
    @OperationLogProperty(actionName = "oltConfigInfoAction", operationName = "saveServerSnmp")
    public String saveServerSnmp() throws Exception {
        String result = "success";
        SnmpParam param = entityService.getSnmpParamByEntity(entityId);
        param.setEntityId(entityId);
        param.setVersion(serverSnmpVersion);
        // V2C模式下更新共同体
        if (serverSnmpVersion == 1) {
            param.setCommunity(readCommunity);
            param.setWriteCommunity(writeCommunity);
        }
        // V3模式下更新相关的参数
        if (serverSnmpVersion == 3) {
            //param.setSecurityLevel(securityLevel);
            param.setAuthProtocol(authProtocol);
            param.setUsername(username);
            param.setAuthPassword(authPassword);
            param.setPrivProtocol(privProtocol);
            param.setPrivPassword(privPassword);
        }
        /* Entity entity = new Entity();
         entity.setIp(emsIpAddress);
         entity.setEntityId(entityId);
         entity.setName(alias);
         entity.setNote(note.replace("\n", ""));
         String result = entityService.updateEntityIpInfo(entityId, oldIp, emsIpAddress);*/
        //if (result == "success") {
        entityService.updateSnmpParam(param);
        //oltConfigInfoService.updateOltEntityIp(entity);
        //} else {
        //logger.info("saveServerSnmp fail");
        //}
        writeDataToAjax(result);
        return NONE;
    }

    /**
     * 检测网管侧输入Ip的合法性
     * @throws Exception 
     * 
     */
    public String checkEmsIpAddress() throws Exception {
        String result = "exist";
        entity = entityService.getEntityByIp(emsIpAddress);
        if (entity != null) {
            result = "exist";
        } else {
            result = "notExist";
        }
        writeDataToAjax(result);
        return NONE;
    }

    /**
     * 更新设备网管侧IP
     * @throws Exception 
     * 
     */
    public String updateEmsIpAddress() throws Exception {
        String result = "success";
        Entity entity = new Entity();
        entity.setIp(newIp);
        entity.setEntityId(entityId);
        try {
            oltConfigInfoService.updateOltEntityIp(entity);
        } catch (Exception e) {
            logger.info("updateEmsIpAddress fial{0}", e);
            result = "fail";
        }
        writeDataToAjax(result);
        return NONE;
    }

    /**
     * 保存设备测SNMP参数
     * 
     * @return
     */
    @OperationLogProperty(actionName = "oltConfigInfoAction", operationName = "saveEntitySnmp")
    public String saveEntitySnmp() {
        SnmpParam param = entityService.getSnmpParamByEntity(entityId);
        OltAttribute snmpParamInfo = new OltAttribute();
        snmpParamInfo.setEntityId(entityId);
        snmpParamInfo.setSnmpHostIp(snmpHostIp);
        snmpParamInfo.setHostIpMask(manageHostMask);
        snmpParamInfo.setSysSnmpVersionInSnmp4J(snmpVersion);
        // snmpParamInfo.setTopSysReadCommunity(snmpCommunity);
        // snmpParamInfo.setTopSysWriteCommunity(snmpWriteCommunity);
        oltConfigInfoService.modifyOltSnmpConfig(snmpParamInfo, param);// 修改SNMP参数
        // 同时更新网管snmp 参数
        // modify lzt 此处只是修改设备snmp 版本不通和网管snmp参数进行关联
        /*
         * param.setVersion((byte) snmpVersion);
         * param.setCommunity(snmpCommunity);
         * param.setWriteCommunity(snmpWriteCommunity);
         * entityService.updateSnmpParam(param);
         */
        return NONE;
    }

    /**
     * 更新网管SNMP参数
     * 
     * @return String
     * @throws IOException
     * @throws JSONException
     */
    @OperationLogProperty(actionName = "oltConfigInfoAction", operationName = "updateEmsSnmpparam")
    public String updateEmsSnmpparam() throws JSONException, IOException {
        snmpParam = entityService.getSnmpParamByEntity(entityId);
        snmpParam.setVersion(snmpVersion);
        snmpParam.setWriteCommunity(snmpWriteCommunity);
        snmpParam.setCommunity(snmpCommunity);
        try {
            oltConfigInfoService.updateEmsSnmpparam(snmpParam);
            operationResult = OperationLog.SUCCESS;
        } catch (Exception e) {
            logger.debug("updateEmsSnmpparam error: {}", e);
            operationResult = OperationLog.FAILURE;
        }
        return NONE;
    }

    /**
     * 更新DB中OLT设备IP
     * 
     * @return String
     * @throws IOException
     * @throws JSONException
     */
    public String updateOltEntityIp() throws JSONException, IOException {
        if (modifyEmsSnmpParam == 1) {
            byte version = 1;
            snmpParam = entityService.getSnmpParamByEntity(entityId);
            snmpParam.setVersion(version);
            snmpParam.setWriteCommunity(snmpWriteCommunity);
            snmpParam.setCommunity(snmpCommunity);
            oltConfigInfoService.updateEmsSnmpparam(snmpParam);
        }
        entity = entityService.getEntity(entityId);
        entity.setIp(entityIp);
        oltConfigInfoService.updateOltEntityIp(entity);
        return NONE;
    }

    /**
     * 修改设备SNMP V2参数
     * @return
     * @throws Exception
     */
    public String modifySnmpV2CCfg() throws Exception {
        SnmpParam param = entityService.getSnmpParamByEntity(entityId);
        OltAttribute oltAttribute = new OltAttribute();
        oltAttribute.setEntityId(entityId);
        oltAttribute.setTopSysReadCommunity(snmpCommunity);
        oltAttribute.setTopSysWriteCommunity(snmpWriteCommunity);
        oltConfigInfoService.modifySnmpV2CConfig(oltAttribute, param);
        return NONE;
    }

    public String showmModifyEmsIpJSP() {
        return SUCCESS;
    }

    /**
     * 刷新OLT配置信息
     * @return
     */
    public String refreshOltConfigInfo() {
        oltConfigInfoService.refreshOltConfigInfo(entityId);
        return NONE;
    }

    public String getOltName() {
        return oltName;
    }

    public void setOltName(String oltName) {
        this.oltName = oltName;
    }

    public String getEntityContact() {
        return entityContact;
    }

    public void setEntityContact(String entityContact) {
        this.entityContact = entityContact;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getInbandIp() {
        return inbandIp;
    }

    public void setInbandIp(String inbandIp) {
        this.inbandIp = inbandIp;
    }

    public String getInbandMask() {
        return inbandMask;
    }

    public void setInbandMask(String inbandMask) {
        this.inbandMask = inbandMask;
    }

    public String getOutbandIp() {
        return outbandIp;
    }

    public void setOutbandIp(String outbandIp) {
        this.outbandIp = outbandIp;
    }

    public String getOutbandMask() {
        return outbandMask;
    }

    public void setOutbandMask(String outbandMask) {
        this.outbandMask = outbandMask;
    }

    public String getEmsIpAddress() {
        return emsIpAddress;
    }

    public void setEmsIpAddress(String emsIpAddress) {
        this.emsIpAddress = emsIpAddress;
    }

    public String getReadCommunity() {
        return readCommunity;
    }

    public void setReadCommunity(String readCommunity) {
        this.readCommunity = readCommunity;
    }

    public String getWriteCommunity() {
        return writeCommunity;
    }

    public void setWriteCommunity(String writeCommunity) {
        this.writeCommunity = writeCommunity;
    }

    public String getManageHostMask() {
        return manageHostMask;
    }

    public void setManageHostMask(String manageHostMask) {
        this.manageHostMask = manageHostMask;
    }

    public String getSnmpCommunity() {
        return snmpCommunity;
    }

    public void setSnmpCommunity(String snmpCommunity) {
        this.snmpCommunity = snmpCommunity;
    }

    public String getSnmpWriteCommunity() {
        return snmpWriteCommunity;
    }

    public void setSnmpWriteCommunity(String snmpWriteCommunity) {
        this.snmpWriteCommunity = snmpWriteCommunity;
    }

    public String getSnmpHostIp() {
        return snmpHostIp;
    }

    public void setSnmpHostIp(String snmpHostIp) {
        this.snmpHostIp = snmpHostIp;
    }

    public Integer getRack() {
        return rack;
    }

    public void setRack(Integer rack) {
        if (rack == 0) {
            this.rack = null;
        } else {
            this.rack = rack;
        }
    }

    public Integer getFrame() {
        return frame;
    }

    public void setFrame(Integer frame) {
        if (frame == 0) {
            this.frame = null;
        } else {
            this.frame = frame;
        }
    }

    public Integer getFrameEnd() {
        return frameEnd;
    }

    public void setFrameEnd(Integer frameEnd) {
        if (frameEnd == 0) {
            this.frameEnd = null;
        } else {
            this.frameEnd = frameEnd;
        }
    }

    @Override
    public Long getEntityId() {
        return entityId;
    }

    @Override
    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public Integer getInbandVlanId() {
        return inbandVlanId;
    }

    public void setInbandVlanId(Integer inbandVlanId) {
        this.inbandVlanId = inbandVlanId;
    }

    public byte getServerSnmpVersion() {
        return serverSnmpVersion;
    }

    public void setServerSnmpVersion(byte serverSnmpVersion) {
        this.serverSnmpVersion = serverSnmpVersion;
    }

    public Integer getSnmpVersion() {
        return snmpVersion;
    }

    public void setSnmpVersion(Integer snmpVersion) {
        this.snmpVersion = snmpVersion;
    }

    @Override
    public Integer getOperationResult() {
        return operationResult;
    }

    @Override
    public void setOperationResult(Integer operationResult) {
        this.operationResult = operationResult;
    }

    public Integer getSecurityLevel() {
        return securityLevel;
    }

    public void setSecurityLevel(Integer securityLevel) {
        this.securityLevel = securityLevel;
    }

    public String getAuthProtocol() {
        return authProtocol;
    }

    public void setAuthProtocol(String authProtocol) {
        this.authProtocol = authProtocol;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAuthPassword() {
        return authPassword;
    }

    public void setAuthPassword(String authPassword) {
        this.authPassword = authPassword;
    }

    public String getPrivProtocol() {
        return privProtocol;
    }

    public void setPrivProtocol(String privProtocol) {
        this.privProtocol = privProtocol;
    }

    public String getPrivPassword() {
        return privPassword;
    }

    public void setPrivPassword(String privPassword) {
        this.privPassword = privPassword;
    }

    public Entity getEntity() {
        return entity;
    }

    public void setEntity(Entity entity) {
        this.entity = entity;
    }

    public OltAttribute getOltAttribute() {
        return oltAttribute;
    }

    public void setOltAttribute(OltAttribute oltAttribute) {
        this.oltAttribute = oltAttribute;
    }

    public void setSnmpVersion(int snmpVersion) {
        this.snmpVersion = snmpVersion;
    }

    public JSONArray getOltVlanInterfaceList() {
        return oltVlanInterfaceList;
    }

    public void setOltVlanInterfaceList(JSONArray oltVlanInterfaceList) {
        this.oltVlanInterfaceList = oltVlanInterfaceList;
    }

    public SnmpParam getSnmpParam() {
        return snmpParam;
    }

    public void setSnmpParam(SnmpParam snmpParam) {
        this.snmpParam = snmpParam;
    }

    public String getOldIp() {
        return oldIp;
    }

    public void setOldIp(String oldIp) {
        this.oldIp = oldIp;
    }

    public String getNewIp() {
        return newIp;
    }

    public void setNewIp(String newIp) {
        this.newIp = newIp;
    }

    public String getFormatedOutbandMac() {
        return formatedOutbandMac;
    }

    public void setFormatedOutbandMac(String formatedOutbandMac) {
        this.formatedOutbandMac = formatedOutbandMac;
    }

    public String getFormatedInbandMac() {
        return formatedInbandMac;
    }

    public void setFormatedInbandMac(String formatedInbandMac) {
        this.formatedInbandMac = formatedInbandMac;
    }

    public String getCameraSwitch() {
        return cameraSwitch;
    }

    public void setCameraSwitch(String cameraSwitch) {
        this.cameraSwitch = cameraSwitch;
    }

    @SuppressWarnings("unused")
    public static void main(String[] args) {
        String str = "hello'";
    }

}
