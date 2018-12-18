/***********************************************************************
 * $Id: SnmpV3ActionUser.java,v1.0 2013-1-9 下午2:17:05 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.snmpV3.action;

import java.io.IOException;
import java.rmi.UnexpectedException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.struts2.ServletActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.security.SecurityLevel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.topvision.ems.network.service.EntityService;
import com.topvision.ems.snmpV3.domain.SnmpAgentVersion;
import com.topvision.ems.snmpV3.facade.domain.UsmSnmpV3User;
import com.topvision.ems.snmpV3.facade.domain.VacmSnmpV3Access;
import com.topvision.ems.snmpV3.facade.domain.VacmSnmpV3Group;
import com.topvision.ems.snmpV3.facade.domain.VacmSnmpV3View;
import com.topvision.ems.snmpV3.service.SnmpV3UserService;
import com.topvision.ems.snmpV3.util.SnmpV3Util;
import com.topvision.framework.snmp.SnmpParam;
import com.topvision.framework.web.struts2.BaseAction;

/**
 * @author Bravin
 * @created @2013-1-9-下午2:17:05
 * 
 */
@Controller("snmpV3UserAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class SnmpV3UserAction extends BaseAction {
    private static final long serialVersionUID = -53072180623818173L;
    private static final Logger logger = LoggerFactory.getLogger(SnmpV3UserAction.class);
    private static final String BLAN_GROUP_VIEW = "";
    private static final String IS_LOCAL_ENGINE_FETCHED = "-1";
    @Autowired
    private SnmpV3UserService snmpV3UserService;
    @Autowired
    private EntityService entityService;
    private Long entityId;
    private String snmpViewName;
    private String snmpViewSubtree;
    private String snmpPrvSubtree;
    private Integer snmpViewMode;
    private String snmpGroupName;
    private String snmpNotifyView;
    private String snmpReadView;
    private String snmpWriteView;
    private Integer snmpSecurityLevel;
    private Boolean useExistedGroup;
    private String snmpUserName;
    private String snmpUserEngineId;
    private String snmpAuthProtocol;
    private String snmpAuthPwd;
    private String snmpPrivProtocol;
    private String snmpPrivPwd;
    private String snmpUserEngineIdOid;
    private JSONObject entitySnmpConfig;
    private String targetUserName;
    private String targetUserEngineId;
    private String targetUserAuthPwd;
    private String targetUserPrivPwd;
    private Integer snmpVersion;
    private String readCommunity;
    private String writeCommunity;
    private VacmSnmpV3Access access;
    private VacmSnmpV3View view;
    private UsmSnmpV3User user;
    private String snmpNewAuthPwd;
    private String snmpNewPrivPwd;
    private Boolean ignoreUserTest;

    /**
     * 刷新SNMP V3配置信息
     * 
     * @return
     * @throws Exception 
     */
    public String refreshSnmpV3Config() throws Exception {
        snmpV3UserService.refreshSnmpV3Config(entityId);
        return NONE;
    }

    /**
     * 展示USM SNMPV3 USER 列表
     * 
     * @return
     * @throws IOException
     */
    public String showSnmpV3UserList() throws IOException {
        return SUCCESS;
    }

    /**
     * 展示USM SNMPV3 ACCESS 列表
     * 
     * @return
     */
    public String showSnmpV3AccessList() {
        return SUCCESS;
    }

    /**
     * 展示USM SNMPV3 VIEW 列表
     * 
     * @return
     */
    public String showSnmpV3ViewList() {
        return SUCCESS;
    }

    /**
     * SNMP 版本配置
     * 
     * @return
     */
    public String showSnmpConfigForManager() {
        SnmpParam param = snmpV3UserService.loadEntitySnmpConfig(entityId);
        entitySnmpConfig = new JSONObject();
        entitySnmpConfig.put("data", param);
        return SUCCESS;
    }

    /**
     * 加载用户列表
     * 
     * @return
     * @throws IOException
     */
    public String loadSnmpV3UserList() throws IOException {
        JSONObject json = new JSONObject();
        List<UsmSnmpV3User> list = null;
        try {
            snmpUserEngineIdOid = snmpV3UserService.getAgentEngineId(entityId);
        } catch (Exception e) {
            snmpUserEngineIdOid = IS_LOCAL_ENGINE_FETCHED;
            logger.info("can not fetch entity local engine: {}", e);
        }
        try {
            list = snmpV3UserService.loadSnmpV3UserList(entityId);
            for (UsmSnmpV3User user : list) {
                if (IS_LOCAL_ENGINE_FETCHED.equals(snmpUserEngineIdOid)) {
                    user.setUseNotifyUser(-1);
                } else if (user.getSnmpUserEngineId().equals(snmpUserEngineIdOid)) {
                    user.setUseNotifyUser(0);
                } else {
                    user.setUseNotifyUser(1);
                }
            }
        } catch (SQLException e) {
            logger.error("", e);
        }
        json.put("data", list);
        writeDataToAjax(json);
        return NONE;
    }

    /**
     * 加载access列表
     * 
     * @return
     * @throws IOException
     */
    public String loadSnmpV3AccessList() throws IOException {
        JSONObject json = new JSONObject();
        List<VacmSnmpV3Access> list = null;
        try {
            list = snmpV3UserService.loadSnmpV3AccessList(entityId, snmpSecurityLevel);
        } catch (SQLException e) {
            logger.error("", e);
        }
        json.put("data", list);
        writeDataToAjax(json);
        return NONE;
    }

    /**
     * 加载视图列表
     * 
     * @return
     * @throws IOException
     */
    public String loadSnmpV3ViewList() throws IOException {
        JSONObject json = new JSONObject();
        List<VacmSnmpV3View> list = null;
        try {
            list = snmpV3UserService.loadSnmpV3ViewList(entityId);
        } catch (SQLException e) {
            logger.error("", e);
        }
        json.put("data", list);
        writeDataToAjax(json);
        return NONE;
    }

    /**
     * 加载视图列表
     * 
     * @return
     * @throws IOException
     */
    public String loadSnmpV3ViewNameList() throws IOException {
        JSONObject json = new JSONObject();
        List<VacmSnmpV3View> list = null;
        try {
            list = snmpV3UserService.loadSnmpV3ViewNameList(entityId);
        } catch (SQLException e) {
            logger.error("", e);
        }
        json.put("data", list);
        writeDataToAjax(json);
        return NONE;
    }

    /**
     * 动态获取SNMP 组的配置信息
     * 
     * @return
     * @throws SQLException
     * @throws IOException
     */
    public String loadSnmpAccessInfo() throws SQLException, IOException {
        JSONObject json = new JSONObject();
        Map<String, String> map = new HashMap<String, String>();
        map.put("entityId", entityId.toString());
        map.put("snmpGroupName", snmpGroupName);
        map.put("snmpSecurityLevel", snmpSecurityLevel.toString());
        access = snmpV3UserService.loadSnmpAccessInfo(map);
        VacmSnmpV3Access access = snmpV3UserService.loadSnmpAccessInfo(map);
        json.put("data", access);
        writeDataToAjax(json);
        return NONE;
    }

    /**
     * 添加用户
     * 
     * @return
     * @throws IOException
     * @throws SQLException
     */
    public String addUser() throws IOException {
        String result = "ok";
        JSONObject json = new JSONObject();
        UsmSnmpV3User snmpV3User = new UsmSnmpV3User();
        // securityName 和 username是一样的
        // FIRST STEP:CLONE USER
        String engineId;
        if (snmpUserEngineId != null) {// 告警用户
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < snmpUserEngineId.length() / 2; i++) {
                String s = "00" + snmpUserEngineId.substring(2 * i, 2 * i + 2);
                sb.append(s.substring(s.length() - 2));
                sb.append(":");
            }
            engineId = sb.substring(0, sb.length() - 1).toString();
        } else {// 普通用户
            engineId = snmpV3UserService.getAgentEngineId(entityId);
        }
        try {
            snmpV3User.setSnmpUserName(snmpUserName);
            snmpV3User.setSnmpSecurityName(snmpUserName);
            snmpV3User.setSnmpUserEngineId(engineId);
            snmpV3User.setSnmpCloneFrom(UsmSnmpV3User.getCloneUserOid(targetUserEngineId, targetUserName));
            snmpV3UserService.cloneUser(entityId, snmpV3User);
        } catch (Exception e) {
            logger.error("clone snmpV3User error:", e);
            result = "cloneError";
        }
        // SECOND STEP:MODIFY USER
        if ("ok".equals(result)) {
            try {
                snmpV3User.setSnmpAuthProtocol(snmpAuthProtocol);
                snmpV3User.setSnmpPrivProtocol(snmpPrivProtocol);
                // 如果有认证模式,则设置其认证

                if (!"NOAUTH".equalsIgnoreCase(snmpAuthProtocol)) {
                    String authKeychange = SnmpV3Util.getKeyChange(snmpAuthProtocol, targetUserAuthPwd, snmpAuthPwd,
                            engineId);
                    snmpV3User.setSnmpAuthKeyChange(authKeychange); // 只有有认证模式的时候才能有加密模式
                    if (!"NOPRIV".equalsIgnoreCase(snmpPrivProtocol)) {
                        String privKeychange = SnmpV3Util.getKeyChange(snmpAuthProtocol, targetUserPrivPwd,
                                snmpPrivPwd, engineId);
                        snmpV3User.setSnmpPrivKeyChange(privKeychange);
                    }
                }

                snmpV3User.setSnmpCloneFrom(null);
                snmpV3UserService.activateUser(entityId, snmpV3User);
            } catch (Exception e) {
                logger.error("activate snmpV3User error:", e);
                result = "activateError";
                try {
                    snmpV3UserService.deleteUser(entityId, snmpUserName, snmpV3User.getSnmpUserEngineId(), null);
                } catch (SQLException e1) {
                    logger.error("deleteUser err", e1);
                    result = "deleteUserError";
                }
            }
        }

        // THIRD STEP : SAVE USER,借用这些属性，保存用户信息
        if ("ok".equals(result)) {
            try {
                snmpV3User.setEntityId(entityId);
                snmpV3User.setSnmpCloneFrom(targetUserName);
                snmpV3User.setSnmpAuthKeyChange(snmpAuthPwd);
                snmpV3User.setSnmpPrivKeyChange(snmpPrivPwd);
                snmpV3UserService.addSnmpV3User(snmpV3User);
            } catch (SQLException e) {
                logger.error("insert snmpv3 user error", e);
                result = "dbError";
            }
        }
        // FOURTH STEP : : create group
        if ("ok".equals(result)) {
            try {
                VacmSnmpV3Group snmpV3Group = new VacmSnmpV3Group();
                snmpV3Group.setEntityId(entityId);
                snmpV3Group.setSnmpGroupName(snmpGroupName);
                snmpV3Group.setSnmpSecurityMode(VacmSnmpV3Group.DEFAULT_SECURITY_MODE);
                snmpV3Group.setSnmpSecurityName(snmpUserName);
                snmpV3UserService.addSnmpV3Group(entityId, snmpV3Group);
            } catch (Exception e) {
                logger.error("create group error:", e);
                result = "groupError";
            }
        }

        // FIRTH STEP : create ACCESS
        if ("ok".equals(result)) {
            if (!useExistedGroup) {
                try {
                    VacmSnmpV3Access access = new VacmSnmpV3Access();
                    access.setEntityId(entityId);
                    access.setSnmpGroupName(snmpGroupName);
                    access.setSnmpSecurityMode(VacmSnmpV3Access.DEFAULT_SECURITY_MODE);
                    access.setSnmpContextPrefix(VacmSnmpV3Access.DEFAULT_CONTEXT_PREFIX);
                    if (!BLAN_GROUP_VIEW.equals(snmpReadView)) {
                        access.setSnmpReadView(snmpReadView);
                    }
                    if (!BLAN_GROUP_VIEW.equals(snmpWriteView)) {
                        access.setSnmpWriteView(snmpWriteView);
                    }
                    if (!BLAN_GROUP_VIEW.equals(snmpNotifyView)) {
                        access.setSnmpNotifyView(snmpNotifyView);
                    }
                    access.setSnmpSecurityLevel(snmpSecurityLevel);
                    snmpV3UserService.addAccess(entityId, access);
                } catch (Exception e) {
                    logger.error("create access error:", e);
                    result = "accessError";
                }
            }
        }
        json.put("data", result);
        writeDataToAjax(json);
        return NONE;
    }

    /**
     * 添加视图
     * 
     * @return
     * @throws SQLException
     */
    public String addView() throws SQLException {
        snmpV3UserService.addView(entityId, snmpViewName, snmpViewSubtree, snmpViewMode);
        return NONE;
    }

    /**
     * 添加组
     * 
     * @return
     */
    public String addAccess() {
        VacmSnmpV3Access access = new VacmSnmpV3Access();
        access.setEntityId(entityId);
        access.setSnmpGroupName(snmpGroupName);
        access.setSnmpSecurityMode(VacmSnmpV3Access.DEFAULT_SECURITY_MODE);
        access.setSnmpContextPrefix(VacmSnmpV3Access.DEFAULT_CONTEXT_PREFIX);
        access.setSnmpNotifyView(snmpNotifyView);
        access.setSnmpReadView(snmpReadView);
        access.setSnmpWriteView(snmpWriteView);
        access.setSnmpSecurityLevel(snmpSecurityLevel);
        try {
            snmpV3UserService.addAccess(entityId, access);
        } catch (SQLException e) {
            logger.error("", e);
        }
        return NONE;
    }

    /**
     * 修改组
     * 
     * @return
     * @throws SQLException 
     */
    public String modifyAccess() throws SQLException {
        VacmSnmpV3Access access = new VacmSnmpV3Access();
        access.setEntityId(entityId);
        access.setSnmpGroupName(snmpGroupName);
        access.setSnmpSecurityMode(VacmSnmpV3Access.DEFAULT_SECURITY_MODE);
        access.setSnmpContextPrefix(new String());
        access.setSnmpNotifyView(snmpNotifyView);
        access.setSnmpReadView(snmpReadView);
        access.setSnmpWriteView(snmpWriteView);
        access.setSnmpSecurityLevel(snmpSecurityLevel);
        snmpV3UserService.modifyAccess(entityId, access);
        return NONE;
    }

    /**
     * 展示用户修改配置
     * @return
     * @throws SQLException
     */
    public String showModifySnmpV3User() throws SQLException {
        user = snmpV3UserService.querySnmpV3UserInfo(entityId, snmpUserName, snmpUserEngineId);
        return SUCCESS;
    }

    /**
     * 得到设备的ENGINE ID
     * @return
     * @throws IOException
     */
    public String getEntitySnmpEngineId() throws IOException {
        JSONObject json = new JSONObject();
        String engineId = snmpV3UserService.getAgentEngineId(entityId);
        json.put("engine", engineId);
        writeDataToAjax(json);
        return NONE;
    }

    /**
     * 修改视图
     * 
     * @return
     */
    public String modifySnmpV3View() {
        try {
            snmpV3UserService.modifyView(entityId, snmpViewName, snmpViewSubtree, snmpViewMode, snmpPrvSubtree);
        } catch (SQLException e) {
            logger.error("", e);
        }
        return NONE;
    }

    /**
     * 修改用户
     * 
     * @return
     * @throws SQLException
     * @throws IOException
     */
    public String modifyUser() throws SQLException, IOException {
        JSONObject json = new JSONObject();
        // 如果没有传递旧密码，则从数据库读取
        if (snmpAuthPwd == null) {
            UsmSnmpV3User oldUser = snmpV3UserService.querySnmpV3UserInfo(entityId, snmpUserName, snmpUserEngineId);
            snmpAuthPwd = oldUser.getSnmpAuthKeyChange();
            snmpPrivPwd = oldUser.getSnmpPrivKeyChange();
        }
        UsmSnmpV3User snmpV3User = new UsmSnmpV3User();
        // securityName 和 username是一样的
        snmpV3User.setSnmpUserName(snmpUserName);
        snmpV3User.setSnmpUserEngineId(snmpUserEngineId);
        // snmpV3User.setSnmpUserEngineOid(SnmpV3Util.octo2byte(snmpUserEngineId));
        snmpV3User.setSnmpAuthProtocol(snmpAuthProtocol);
        snmpV3User.setSnmpPrivProtocol(snmpPrivProtocol);
        // 由于传递过去的delta中包含oldKey,并且设备不支持回滚，所以如果要保证安全的修改密码，只能保证我们使用的oldKey
        // 也是准确的，故在修改前必须对设备进行测试。如果设备当前不支持V3，可以直接返回错误给用户。
        // 说明：网管创建的这群账户使用的engine都是设备侧的engine，而我们修改的也是设备侧的用户，所以理论上engineid是一致的。
        // 但是对于 remote的用户，就不能通过这种办法进行修改了。只能提示风险
        // 如果忽略了则不测试
        String result = "valid";
        json.put("data", result);
        if (ignoreUserTest == null || !ignoreUserTest) {
            snmpV3User.setSnmpAuthKeyChange(snmpAuthPwd);// 用于测试
            snmpV3User.setSnmpPrivKeyChange(snmpPrivPwd);// 用于测试
            result = snmpV3UserService.tryUserValid(entityId, snmpV3User);
        }
        if ("valid".equals(result)) {
            // 如果有认证模式,则设置其认证
            // 由于之前要测试，keychange这个字段被共用了，所以只能将其放在此处
            if (!"NOAUTH".equalsIgnoreCase(snmpAuthProtocol)) {
                // 如果没有新密码,则表示不修改
                if (snmpNewAuthPwd != null && !"".equals(snmpNewAuthPwd)) {
                } else {
                    // 新密码等于旧密码，相当于是没有改
                    snmpNewAuthPwd = snmpAuthPwd;
                }
                String authKeychange = SnmpV3Util.getKeyChange(snmpAuthProtocol, snmpAuthPwd, snmpNewAuthPwd,
                        snmpUserEngineId);
                snmpV3User.setSnmpAuthKeyChange(authKeychange);

                if (!"NOPRIV".equalsIgnoreCase(snmpPrivProtocol)) {
                    // 只有有认证模式的时候才能有加密模式
                    if (snmpNewPrivPwd != null && !"".equals(snmpNewPrivPwd)) {
                    } else {
                        snmpNewPrivPwd = snmpPrivPwd;
                    }
                    String privKeychange = SnmpV3Util.getKeyChange(snmpAuthProtocol, snmpPrivPwd, snmpNewPrivPwd,
                            snmpUserEngineId);
                    snmpV3User.setSnmpPrivKeyChange(privKeychange);
                }
            }
            snmpV3UserService.modifySnmpV3User(entityId, snmpV3User);
            VacmSnmpV3Group snmpV3Group = new VacmSnmpV3Group();
            snmpV3Group.setEntityId(entityId);
            snmpV3Group.setSnmpGroupName(snmpGroupName);
            snmpV3Group.setSnmpSecurityMode(VacmSnmpV3Group.DEFAULT_SECURITY_MODE);
            snmpV3Group.setSnmpSecurityName(snmpUserName);
            snmpV3UserService.modifySnmpV3Group(entityId, snmpV3Group);
            // 为了防止传递大量参数，故在action中作业务
            snmpV3User.setEntityId(entityId);
            if (snmpNewAuthPwd != null && !"".equals(snmpNewAuthPwd)) {
                snmpV3User.setSnmpAuthKeyChange(snmpNewAuthPwd);
            }
            if (snmpNewPrivPwd != null && !"".equals(snmpNewPrivPwd)) {
                snmpV3User.setSnmpPrivKeyChange(snmpNewPrivPwd);
            }
            snmpV3User.setSnmpGroupName(snmpGroupName);
            snmpV3UserService.modifyUser(snmpV3User);
        } else {
            json.put("data", result);
        }
        writeDataToAjax(json);
        return NONE;
    }

    /**
     * 删除视图
     * 
     * @return
     * @throws SQLException
     */
    public String deleteView() throws SQLException {
        snmpV3UserService.deleteView(entityId, snmpViewName, snmpViewSubtree);
        return NONE;
    }

    /**
     * 删除视图
     * 
     * @return
     * @throws SQLException
     */
    public String deleteUser() throws SQLException {
        snmpV3UserService.deleteUser(entityId, snmpUserName, snmpUserEngineId, snmpGroupName);
        return NONE;
    }

    // 删除组
    public String deleteAccess() throws SQLException {
        VacmSnmpV3Access access = new VacmSnmpV3Access();
        access.setEntityId(entityId);
        access.setSnmpGroupName(snmpGroupName);
        // prefix 要为空，只要设置一个 空字符串下去了，详询：@rodJohn
        access.setSnmpContextPrefix(new String());
        access.setSnmpSecurityMode(VacmSnmpV3Access.DEFAULT_SECURITY_MODE);
        access.setSnmpSecurityLevel(snmpSecurityLevel);
        snmpV3UserService.deleteAccess(entityId, access);
        return NONE;
    }

    /**
     * 测试账户是否可用
     * 
     * @return
     * @throws IOException
     * @throws SQLException
     */
    public String tryUserValid() throws IOException, SQLException {
        UsmSnmpV3User snmpV3User = snmpV3UserService.querySnmpV3UserInfo(entityId, snmpUserName, snmpUserEngineId);
        if (snmpAuthPwd != null) {
            snmpV3User.setSnmpAuthKeyChange(snmpAuthPwd);
        }
        if (snmpPrivPwd != null) {
            snmpV3User.setSnmpPrivKeyChange(snmpPrivPwd);
        }
        String result = snmpV3UserService.tryUserValid(entityId, snmpV3User);
        JSONObject json = new JSONObject();
        json.put("data", result);
        writeDataToAjax(json);
        // 返回的结果并不一定代表用户名错误，而可能是该用户不是由当前网管创建，结果应属未知
        return NONE;
    }

    /**
     * 切换设备SNMP版本。设备使用 1.2.3代表v1,v2,v3.网管使用0.1.3代表
     * 
     * @MARK 老大说了，这里的切换安全性由用户保证，不允许网管在后台切换设备版本，直接切换
     * 
     * @return
     * @throws IOException
     */
    public String updateSnmpVersion() throws IOException {
        snmpV3UserService.updateSnmpVersion(entityId, snmpVersion);
        if (snmpVersion == SnmpAgentVersion.V1 || snmpVersion == SnmpAgentVersion.V2) {// snmpv1,v2
            try {
                // 更新网管端SNMP访问参数
                SnmpParam param = entityService.getSnmpParamByEntity(entityId);
                param.setEntityId(entityId);
                // 网管 0 1 设备  1 2
                param.setVersion(snmpVersion - 1);
                param.setCommunity(readCommunity);
                param.setWriteCommunity(writeCommunity);
                entityService.updateSnmpParam(param);
            } catch (Exception e) {
                logger.error("", e);
            }
        } else if (snmpVersion == SnmpConstants.version3) {// snmpv3
            // 更新网管端SNMP访问参数
            SnmpParam param = entityService.getSnmpParamByEntity(entityId);
            param.setEntityId(entityId);
            param.setVersion(snmpVersion);
            param.setUsername(snmpUserName);
            param.setAuthProtocol(snmpAuthProtocol);
            param.setAuthPassword(snmpAuthPwd);
            param.setPrivProtocol(snmpPrivProtocol);
            param.setPrivPassword(snmpPrivPwd);
            int securityLevel = SecurityLevel.NOAUTH_NOPRIV;
            if (!"NOPRIV".equals(snmpAuthProtocol)) {
                securityLevel++;
            }
            // 不存在 NOAUTH_PRIV的情况,故Priv在前
            if (!"NOAUTH".equals(snmpAuthProtocol)) {
                securityLevel++;
            } else {
                // 如果是noauth,并且是priv，那就表示出错了
                if (securityLevel > 1) {
                    throw new UnexpectedException("wrong snmp protocol config");
                }
            }
            param.setSecurityLevel(securityLevel);
            entityService.updateSnmpParam(param);
        }
        return NONE;
    }

    /**
     * 修改设备SNMP V1，V2配置
     * 
     * @return
     */
    public String modifySnmpv2Cfg() {
        SnmpParam param = new SnmpParam();
        param.setEntityId(entityId);
        param.setCommunity(readCommunity);
        param.setWriteCommunity(writeCommunity);
        entityService.updateSnmpParam(param);
        return NONE;
    }

    /**
     * 加载可用的克隆用户列表
     * 
     * @return
     * @throws SQLException
     * @throws IOException
     */
    public String loadAvaiableCloneUserList() throws SQLException, IOException {
        JSONObject json = new JSONObject();
        List<UsmSnmpV3User> list = snmpV3UserService.getAvaiableCloneUserList(entityId, snmpUserName, snmpUserEngineId);
        json.put("data", list);
        writeDataToAjax(json);
        return NONE;
    }

    /**
     * 加载可用的克隆引擎列表
     * 
     * @return
     * @throws SQLException
     * @throws IOException
     */
    public String loadAvaiableCloneEngineList() throws SQLException, IOException {
        JSONObject json = new JSONObject();
        List<UsmSnmpV3User> list = snmpV3UserService.getAvaiableCloneEngineList(entityId, snmpUserName);
        json.put("data", list);
        writeDataToAjax(json);
        return NONE;
    }

    /**
     * 进入用户向导页面
     * 
     * @return
     */
    public String showUserAddtionWizard() {
        return SUCCESS;
    }

    /**
     * snmp v2 配置
     * 
     * @return
     */
    public String showSnmpV2Cfg() {
        return SUCCESS;
    }

    /**
     * 进入用户向导页面
     * 
     * @return
     */
    public String showUserAddtionWizardGroup() {
        return SUCCESS;
    }

    /**
     * 进入用户向导页面
     * 由于SecurityLevel是主键约束，所以没有必要去修改组的securityLevel
     * 
     * @return
     * @throws SQLException
     */
    public String showVacmAccessAddtion() throws SQLException {
        if (snmpGroupName != null) {
            Map<String, String> map = new HashMap<String, String>();
            map.put("entityId", entityId.toString());
            map.put("snmpGroupName", snmpGroupName);
            map.put("snmpSecurityLevel", snmpSecurityLevel.toString());
            access = snmpV3UserService.loadSnmpAccessInfo(map);
        }
        return SUCCESS;
    }

    /**
     * 进入用户向导页面
     * 
     * @return
     * @throws SQLException
     */
    public String showVacmViewAddtion() throws SQLException {
        if (snmpViewName != null) {
            view = snmpV3UserService.loadSnmpViewInfo(entityId, snmpViewName, snmpViewSubtree);
        }
        return SUCCESS;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public SnmpV3UserService getSnmpV3UserService() {
        return snmpV3UserService;
    }

    public void setSnmpV3UserService(SnmpV3UserService snmpV3UserService) {
        this.snmpV3UserService = snmpV3UserService;
    }

    public String getSnmpViewName() {
        return snmpViewName;
    }

    public void setSnmpViewName(String snmpViewName) {
        this.snmpViewName = snmpViewName;
    }

    public String getSnmpViewSubtree() {
        return snmpViewSubtree;
    }

    public void setSnmpViewSubtree(String snmpViewSubtree) {
        this.snmpViewSubtree = snmpViewSubtree;
    }

    public Integer getSnmpViewMode() {
        return snmpViewMode;
    }

    public void setSnmpViewMode(Integer snmpViewMode) {
        this.snmpViewMode = snmpViewMode;
    }

    public String getSnmpGroupName() {
        return snmpGroupName;
    }

    public void setSnmpGroupName(String snmpGroupName) {
        this.snmpGroupName = snmpGroupName;
    }

    public String getSnmpNotifyView() {
        return snmpNotifyView;
    }

    public void setSnmpNotifyView(String snmpNotifyView) {
        this.snmpNotifyView = snmpNotifyView;
    }

    public String getSnmpReadView() {
        return snmpReadView;
    }

    public void setSnmpReadView(String snmpReadView) {
        this.snmpReadView = snmpReadView;
    }

    public String getSnmpWriteView() {
        return snmpWriteView;
    }

    public void setSnmpWriteView(String snmpWriteView) {
        this.snmpWriteView = snmpWriteView;
    }

    public Integer getSnmpSecurityLevel() {
        return snmpSecurityLevel;
    }

    public void setSnmpSecurityLevel(Integer snmpSecurityLevel) {
        this.snmpSecurityLevel = snmpSecurityLevel;
    }

    public String getSnmpUserName() {
        return snmpUserName;
    }

    public void setSnmpUserName(String snmpUserName) {
        this.snmpUserName = snmpUserName;
    }

    public String getSnmpUserEngineId() {
        return snmpUserEngineId;
    }

    public void setSnmpUserEngineId(String snmpUserEngineId) {
        this.snmpUserEngineId = snmpUserEngineId;
    }

    public String getSnmpAuthProtocol() {
        return snmpAuthProtocol;
    }

    public void setSnmpAuthProtocol(String snmpAuthProtocol) {
        this.snmpAuthProtocol = snmpAuthProtocol;
    }

    public String getSnmpPrivProtocol() {
        return snmpPrivProtocol;
    }

    public void setSnmpPrivProtocol(String snmpPrivProtocol) {
        this.snmpPrivProtocol = snmpPrivProtocol;
    }

    public String getSnmpUserEngineIdOid() {
        return snmpUserEngineIdOid;
    }

    public void setSnmpUserEngineIdOid(String snmpUserEngineIdOid) {
        this.snmpUserEngineIdOid = snmpUserEngineIdOid;
    }

    public JSONObject getEntitySnmpConfig() {
        return entitySnmpConfig;
    }

    public void setEntitySnmpConfig(JSONObject entitySnmpConfig) {
        this.entitySnmpConfig = entitySnmpConfig;
    }

    public String getSnmpAuthPwd() {
        return snmpAuthPwd;
    }

    public void setSnmpAuthPwd(String snmpAuthPwd) {
        this.snmpAuthPwd = snmpAuthPwd;
    }

    public String getSnmpPrivPwd() {
        return snmpPrivPwd;
    }

    public void setSnmpPrivPwd(String snmpPrivPwd) {
        this.snmpPrivPwd = snmpPrivPwd;
    }

    public String getTargetUserName() {
        return targetUserName;
    }

    public void setTargetUserName(String targetUserName) {
        this.targetUserName = targetUserName;
    }

    public String getTargetUserEngineId() {
        return targetUserEngineId;
    }

    public void setTargetUserEngineId(String targetUserEngineId) {
        this.targetUserEngineId = targetUserEngineId;
    }

    public String getTargetUserPwd() {
        return targetUserAuthPwd;
    }

    public void setTargetUserPwd(String targetUserPwd) {
        this.targetUserAuthPwd = targetUserPwd;
    }

    public String getTargetUserAuthPwd() {
        return targetUserAuthPwd;
    }

    public void setTargetUserAuthPwd(String targetUserAuthPwd) {
        this.targetUserAuthPwd = targetUserAuthPwd;
    }

    public String getTargetUserPrivPwd() {
        return targetUserPrivPwd;
    }

    public void setTargetUserPrivPwd(String targetUserPrivPwd) {
        this.targetUserPrivPwd = targetUserPrivPwd;
    }

    public Integer getSnmpVersion() {
        return snmpVersion;
    }

    public void setSnmpVersion(Integer snmpVersion) {
        this.snmpVersion = snmpVersion;
    }

    public String getReadCommunity() {
        return readCommunity;
    }

    public void setReadCommunity(String readCommunity) {
        this.readCommunity = readCommunity;
    }

    public String getWriteCommuntity() {
        return writeCommunity;
    }

    public void setWriteCommuntity(String writeCommuntity) {
        this.writeCommunity = writeCommuntity;
    }

    public String getWriteCommunity() {
        return writeCommunity;
    }

    public void setWriteCommunity(String writeCommunity) {
        this.writeCommunity = writeCommunity;
    }

    public EntityService getEntityService() {
        return entityService;
    }

    public void setEntityService(EntityService entityService) {
        this.entityService = entityService;
    }

    public Boolean getUseExistedGroup() {
        return useExistedGroup;
    }

    public void setUseExistedGroup(Boolean useExistedGroup) {
        this.useExistedGroup = useExistedGroup;
    }

    public VacmSnmpV3Access getAccess() {
        return access;
    }

    public void setAccess(VacmSnmpV3Access access) {
        this.access = access;
    }

    public VacmSnmpV3View getView() {
        return view;
    }

    public void setView(VacmSnmpV3View view) {
        this.view = view;
    }

    public String getSnmpPrvSubtree() {
        return snmpPrvSubtree;
    }

    public void setSnmpPrvSubtree(String snmpPrvSubtree) {
        this.snmpPrvSubtree = snmpPrvSubtree;
    }

    public UsmSnmpV3User getUser() {
        return user;
    }

    public void setUser(UsmSnmpV3User user) {
        this.user = user;
    }

    public String getSnmpNewAuthPwd() {
        return snmpNewAuthPwd;
    }

    public void setSnmpNewAuthPwd(String snmpNewAuthPwd) {
        this.snmpNewAuthPwd = snmpNewAuthPwd;
    }

    public String getSnmpNewPrivPwd() {
        return snmpNewPrivPwd;
    }

    public void setSnmpNewPrivPwd(String snmpNewPrivPwd) {
        this.snmpNewPrivPwd = snmpNewPrivPwd;
    }

    public Boolean getIgnoreUserTest() {
        return ignoreUserTest;
    }

    public void setIgnoreUserTest(Boolean ignoreUserTest) {
        this.ignoreUserTest = ignoreUserTest;
    }

}
