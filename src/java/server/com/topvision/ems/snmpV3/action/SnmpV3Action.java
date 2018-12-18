/***********************************************************************
 * $Id: SnmpV3Action.java,v1.0 2013-1-9 上午9:29:22 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.snmpV3.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.topvision.ems.snmpV3.facade.domain.SnmpNotifyFilterProfile;
import com.topvision.ems.snmpV3.facade.domain.SnmpNotifyFilterTable;
import com.topvision.ems.snmpV3.facade.domain.SnmpNotifyTable;
import com.topvision.ems.snmpV3.facade.domain.SnmpTargetParams;
import com.topvision.ems.snmpV3.facade.domain.SnmpTargetTable;
import com.topvision.ems.snmpV3.service.SnmpV3Service;
import com.topvision.ems.snmpV3.util.SimpleDefault;
import com.topvision.framework.web.struts2.BaseAction;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * @author Bravin
 * @created @2013-1-9-上午9:29:22
 * 
 */
@Controller("snmpV3Action")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class SnmpV3Action extends BaseAction {
    private static final long serialVersionUID = -53072180623818173L;
    private final Logger logger = LoggerFactory.getLogger(SnmpV3Action.class);
    @Autowired
    private SnmpV3Service snmpV3Service;
    private Long entityId;
    private JSONArray targetJson = new JSONArray();
    private JSONArray tarParamJson = new JSONArray();
    private JSONArray notifyJson = new JSONArray();
    private JSONArray nfProfileJson = new JSONArray();
    private JSONArray notifyFilterJson = new JSONArray();
    // V3_notify param
    private String address;
    private Integer port;
    private Integer notifyType;
    private Long securityModel;
    private String securityName;
    private Integer securityLevel;
    private Integer storType;
    private String targetName;
    private String domain;
    private Long timeout;
    private Integer retryCount;
    private String tagList;
    private String paramsName;
    private Long tarParMPModel;
    private String profileName;
    private String notifyName;
    private String notifyTag;
    private String subtreeMask;
    private Integer type;
    private String subtree;
    private String mask;
    // end of V3_notify param

    /**
     * 跳转到notify页面
     * 
     * @return
     * @throws Exception
     */
    public String showSnmpV3Notify() throws Exception {
        return SUCCESS;
    }

    /**
     * 从数据加载SNMP Notify相关的所有数据
     * 
     * @return
     * @throws Exception
     */
    public String loadAllSnmpNotifyData() throws Exception {
        List<SnmpTargetTable> targetJsonTmp = snmpV3Service.loadTarget(entityId);
        if (targetJsonTmp.size() > 0) {
            targetJson = JSONArray.fromObject(targetJsonTmp);
        } else {
            targetJson = JSONArray.fromObject(false);
        }
        List<SnmpTargetParams> tParamJsonTmp = snmpV3Service.loadTargetParams(entityId);
        if (tParamJsonTmp.size() > 0) {
            tarParamJson = JSONArray.fromObject(tParamJsonTmp);
        } else {
            tarParamJson = JSONArray.fromObject(false);
        }
        List<SnmpNotifyTable> notifyJsonTmp = snmpV3Service.loadNotify(entityId);
        if (notifyJsonTmp.size() > 0) {
            notifyJson = JSONArray.fromObject(notifyJsonTmp);
        } else {
            notifyJson = JSONArray.fromObject(false);
        }
        List<SnmpNotifyFilterProfile> nfProfileJsonTmp = snmpV3Service.loadNotifyProfile(entityId);
        if (nfProfileJsonTmp.size() > 0) {
            nfProfileJson = JSONArray.fromObject(nfProfileJsonTmp);
        } else {
            nfProfileJson = JSONArray.fromObject(false);
        }
        List<SnmpNotifyFilterTable> nFilterJsonTmp = snmpV3Service.loadNotifyFilter(entityId);
        if (nFilterJsonTmp.size() > 0) {
            notifyFilterJson = JSONArray.fromObject(nFilterJsonTmp);
        } else {
            notifyFilterJson = JSONArray.fromObject(false);
        }
        Map<String, Object> o = new HashMap<String, Object>();
        o.put("targetJson", targetJson);
        o.put("tarParamJson", tarParamJson);
        o.put("notifyJson", notifyJson);
        o.put("nfProfileJson", nfProfileJson);
        o.put("notifyFilterJson", notifyFilterJson);
        writeDataToAjax(JSONObject.fromObject(o));
        return NONE;
    }

    /**
     * 刷新SnmpV3 的 Notify 相关数据
     * 
     * @return
     * @throws Exception
     */
    public String refreshSnmpV3Notify() throws Exception {
        String mes = "success";
        try {
            snmpV3Service.refreshTarget(entityId);
            snmpV3Service.refreshTargetParams(entityId);
            snmpV3Service.refreshNotify(entityId);
            snmpV3Service.refreshNotifyFilter(entityId);
            snmpV3Service.refreshNotifyProfile(entityId);
        } catch (Exception e) {
            mes = e.getMessage();
            logger.debug("refreshSnmpV3Notify error: " + e.getMessage());
        }
        writeDataToAjax(mes);
        return NONE;
    }

    /**
     * 新建一条SimpleConfig
     * 
     * @return
     * @throws Exception
     */
    public String create_simple() throws Exception {
        String[] ips = address.split("\\.");
        String ip = "";
        for (String node : ips) {
            ip += Integer.toHexString(Integer.parseInt(node)).toUpperCase();
        }

        // Target
        SnmpTargetTable tar = new SnmpTargetTable();
        tar.setEntityId(entityId);
        tar.setTargetAddress(getAddressFromIpAndPort(address, port));
        tar.setTargetDomain(SimpleDefault.domain);
        tar.setTargetName(SimpleDefault.targetName + ip);
        tar.setTargetParams(SimpleDefault.paramsName + ip);
        tar.setTargetRetryCount(SimpleDefault.retryCount);
        tar.setTargetStorageType(storType);
        tar.setTargetTagList(SimpleDefault.tagList + ip);
        tar.setTargetTimeout(SimpleDefault.timeout);
        // TargetParams
        SnmpTargetParams par = new SnmpTargetParams();
        par.setEntityId(entityId);
        par.setTargetParamsMPModel(SimpleDefault.tarParMPModel);
        par.setTargetParamsName(SimpleDefault.paramsName + ip);
        par.setTargetParamsSecurityLevel(securityLevel);
        par.setTargetParamsSecurityModel(securityModel);
        par.setTargetParamsSecurityName(securityName);
        par.setTargetParamsStorageType(storType);
        // NotifyFilterProfile
        SnmpNotifyFilterProfile pro = new SnmpNotifyFilterProfile();
        pro.setEntityId(entityId);
        pro.setNotifyFilterProfileName(SimpleDefault.profileName + ip);
        pro.setNotifyFilterProfileStorType(storType);
        pro.setTargetParamsName(SimpleDefault.paramsName + ip);
        // Notify
        SnmpNotifyTable n = new SnmpNotifyTable();
        n.setEntityId(entityId);
        n.setNotifyName(SimpleDefault.notifyName + ip);
        n.setNotifyStorageType(storType);
        n.setNotifyTag(SimpleDefault.notifyTag + ip);
        n.setNotifyType(notifyType);
        // NotifyFilter
        SnmpNotifyFilterTable f = new SnmpNotifyFilterTable();
        f.setEntityId(entityId);
        f.setNotifyFilterMask(SimpleDefault.mask);
        f.setNotifyFilterProfileName(SimpleDefault.profileName + ip);
        f.setNotifyFilterStorType(storType);
        f.setNotifyFilterSubtree(SimpleDefault.subtree);
        f.setNotifyFilterType(SimpleDefault.type);
        writeDataToAjax(createOrModifySimpleConfig(tar, par, n, f, pro));
        return NONE;
    }

    /**
     * 修改一个SimpleConfig
     * 
     * @return
     * @throws Exception
     */
    public String modify_simple() throws Exception {
        String[] ips = address.split("\\.");
        String ip = "";
        for (String node : ips) {
            ip += Integer.toHexString(Integer.parseInt(node)).toUpperCase();
        }
        // Target
        SnmpTargetTable tar = new SnmpTargetTable();
        tar.setEntityId(entityId);
        tar.setTargetAddress(getAddressFromIpAndPort(address, port));
        tar.setTargetDomain(SimpleDefault.domain);
        tar.setTargetName(SimpleDefault.targetName + ip);
        tar.setTargetParams(SimpleDefault.paramsName + ip);
        tar.setTargetRetryCount(SimpleDefault.retryCount);
        tar.setTargetStorageType(storType);
        tar.setTargetTagList(SimpleDefault.tagList + ip);
        tar.setTargetTimeout(SimpleDefault.timeout);
        // TargetParams
        SnmpTargetParams par = new SnmpTargetParams();
        par.setEntityId(entityId);
        par.setTargetParamsMPModel(SimpleDefault.tarParMPModel);
        par.setTargetParamsName(SimpleDefault.paramsName + ip);
        par.setTargetParamsSecurityLevel(securityLevel);
        par.setTargetParamsSecurityModel(securityModel);
        par.setTargetParamsSecurityName(securityName);
        par.setTargetParamsStorageType(storType);
        // Notify
        SnmpNotifyTable n = new SnmpNotifyTable();
        n.setEntityId(entityId);
        n.setNotifyName(SimpleDefault.notifyName + ip);
        n.setNotifyStorageType(storType);
        n.setNotifyTag(SimpleDefault.notifyTag + ip);
        n.setNotifyType(notifyType);
        writeDataToAjax(createOrModifySimpleConfig(tar, par, n, null, null));
        return NONE;
    }

    private String createOrModifySimpleConfig(SnmpTargetTable tar, SnmpTargetParams par, SnmpNotifyTable n,
            SnmpNotifyFilterTable f, SnmpNotifyFilterProfile pro) {
        String mes = "success";
        if (tar != null) {
            try {
                snmpV3Service.addTarget(tar);
            } catch (Exception e) {
                try {
                    snmpV3Service.modifyTarget(tar);
                } catch (Exception ee) {
                    mes = e.getMessage();
                    logger.debug("simpleConfig error in Target: " + e.getMessage());
                    return mes;
                }
            }
        }
        if (par != null) {
            try {
                snmpV3Service.addTargetParams(par);
            } catch (Exception e) {
                try {
                    snmpV3Service.modifyTargetParams(par);
                } catch (Exception ee) {
                    mes = e.getMessage();
                    logger.debug("simpleConfig error in TargetParams: " + e.getMessage());
                    return mes;
                }
            }
        }
        if (pro != null) {
            try {
                snmpV3Service.addNotifyProfile(pro);
            } catch (Exception e) {
                try {
                    snmpV3Service.modifyNotifyProfile(pro);
                } catch (Exception ee) {
                    mes = e.getMessage();
                    logger.debug("simpleConfig error in NoitfyProfile: " + e.getMessage());
                    return mes;
                }
            }
        }
        if (n != null) {
            try {
                snmpV3Service.addNotify(n);
            } catch (Exception e) {
                try {
                    snmpV3Service.modifyNotify(n);
                } catch (Exception ee) {
                    mes = e.getMessage();
                    logger.debug("simpleConfig error in Notify: " + e.getMessage());
                    return mes;
                }
            }
        }
        if (f != null) {
            try {
                snmpV3Service.addNotifyFilter(f);
            } catch (Exception e) {
                try {
                    snmpV3Service.modifyNotifyFilter(f);
                } catch (Exception ee) {
                    mes = e.getMessage();
                    logger.debug("simpleConfig error in NotifyFilter: " + e.getMessage());
                    return mes;
                }
            }
        }
        return mes;
    }

    /**
     * 删除一条SimpleConfig
     * 
     * @return
     * @throws Exception
     */
    public String delete_simple() throws Exception {
        String mes = "success";
        String[] ips = address.split("\\.");
        String ip = "";
        for (String node : ips) {
            ip += Integer.toHexString(Integer.parseInt(node)).toUpperCase();
        }
        SnmpNotifyFilterProfile pro = new SnmpNotifyFilterProfile();
        pro.setEntityId(entityId);
        pro.setTargetParamsName(SimpleDefault.paramsName + ip);
        try {
            snmpV3Service.deleteNotifyFilter(entityId, SimpleDefault.profileName + ip, SimpleDefault.subtree);
            snmpV3Service.deleteNotifyProfile(pro);
            snmpV3Service.deleteNotify(entityId, SimpleDefault.notifyName + ip);
            snmpV3Service.deleteTargetParams(entityId, SimpleDefault.paramsName + ip);
            snmpV3Service.deleteTarget(entityId, SimpleDefault.targetName + ip);
        } catch (Exception e) {
            mes = e.getMessage();
            logger.debug("deleteSimpleConfig error: " + e.getMessage());
        }
        writeDataToAjax(mes);
        return NONE;
    }

    /**
     * 新建Target
     * 
     * @return
     * @throws Exception
     */
    public String create_target() throws Exception {
        String mes = "success";
        SnmpTargetTable o = new SnmpTargetTable();
        o.setEntityId(entityId);
        o.setTargetAddress(getAddressFromIpAndPort(address, port));
        o.setTargetDomain(domain);
        o.setTargetName(targetName);
        o.setTargetParams(paramsName);
        o.setTargetRetryCount(retryCount);
        o.setTargetStorageType(storType);
        o.setTargetTimeout(timeout);
        o.setTargetTagList(tagList);
        try {
            snmpV3Service.addTarget(o);
        } catch (Exception e) {
            mes = e.getMessage();
            logger.debug("create_target error: " + e.getMessage());
        }
        writeDataToAjax(mes);
        return NONE;
    }

    /**
     * 把IP和port组成Address字段,共6位,前4位是IP,后两位是port
     * 
     * @param ip
     * @param port
     * @return
     */
    private String getAddressFromIpAndPort(String ip, Integer port) {
        StringBuilder addr = new StringBuilder();
        List<String> tmp = new ArrayList<String>();
        try {
            String[] ips = ip.split("\\.");
            for (int i = 0; i < 4; i++) {
                tmp.add(Integer.toHexString(Integer.parseInt(ips[i])));
            }
        } catch (Exception e) {
            tmp = new ArrayList<String>();
            for (int i = 0; i < 4; i++) {
                tmp.add("00");
            }
        }
        String p = Integer.toHexString(port);
        if (p.length() > 2 && p.length() < 5) {
            tmp.add(p.substring(0, p.length() - 2));
            tmp.add(p.substring(p.length() - 2));
        } else if (p.length() < 3) {
            tmp.add("00");
            tmp.add(p);
        } else {
            tmp.add("00");
            tmp.add("00");
        }
        for (int i = 0; i < 6; i++) {
            if (tmp.get(i).length() == 1) {
                addr.append(":").append(String.format("0%s", tmp.get(i)));
            } else if (tmp.get(i).length() == 2) {
                addr.append(":").append(String.format("%s", tmp.get(i)));
            } else {
                addr.append(":00");
            }
        }
        return addr.toString().substring(1);
    }

    /**
     * 新建TargetParam 和 对应的NotifyFilterProfile
     * 
     * @return
     * @throws Exception
     */
    public String create_tParam() throws Exception {
        String mes = "success";
        SnmpTargetParams o = new SnmpTargetParams();
        o.setEntityId(entityId);
        o.setTargetParamsMPModel(tarParMPModel);
        o.setTargetParamsName(paramsName);
        o.setTargetParamsSecurityName(securityName);
        o.setTargetParamsSecurityModel(securityModel);
        o.setTargetParamsSecurityLevel(securityLevel);
        o.setTargetParamsStorageType(storType);
        try {
            snmpV3Service.addTargetParams(o);
        } catch (Exception e) {
            mes = e.getMessage();
            logger.debug("create_tParam error: " + e.getMessage());
        }
        // 0:don't care; 1:add; 2:modify
        int flag = getAddOrModifyProfileFlag();
        if (flag > 0) {
            SnmpNotifyFilterProfile n = new SnmpNotifyFilterProfile();
            n.setEntityId(entityId);
            n.setNotifyFilterProfileName(profileName);
            n.setNotifyFilterProfileStorType(storType);
            n.setTargetParamsName(paramsName);
            try {
                if (flag == 1) {
                    snmpV3Service.addNotifyProfile(n);
                } else if (flag == 2) {
                    snmpV3Service.modifyNotifyProfile(n);
                }
            } catch (Exception e) {
                mes = e.getMessage();
                logger.debug("create_tParam_notifyProfile error: " + e.getMessage());
            }
        }
        writeDataToAjax(mes);
        return NONE;
    }

    /**
     * 在新建或修改TargetParams时,判断是否要新增或修改notifyProfile
     * 
     * @return
     */
    private int getAddOrModifyProfileFlag() {
        // 0:don't care; 1:add; 2:modify
        int flag = 1;
        if (profileName != null && !profileName.equalsIgnoreCase("")) {
            List<SnmpNotifyFilterProfile> list = snmpV3Service.loadNotifyProfile(entityId);
            for (SnmpNotifyFilterProfile p : list) {
                if (p.getEntityId().equals(entityId) && p.getTargetParamsName().equalsIgnoreCase(paramsName)) {
                    if (p.getNotifyFilterProfileName().equalsIgnoreCase(profileName)
                            && p.getNotifyFilterProfileStorType().equals(storType)) {
                        flag = 0;
                    } else {
                        flag = 2;
                    }
                    break;
                }
            }
        }
        return flag;
    }

    /**
     * 新建Notify
     * 
     * @return
     * @throws Exception
     */
    public String create_notify() throws Exception {
        String mes = "success";
        SnmpNotifyTable n = new SnmpNotifyTable();
        n.setEntityId(entityId);
        n.setNotifyName(notifyName);
        n.setNotifyStorageType(storType);
        n.setNotifyTag(notifyTag);
        n.setNotifyType(notifyType);
        try {
            snmpV3Service.addNotify(n);
        } catch (Exception e) {
            mes = e.getMessage();
            logger.debug("create_notify error: " + e.getMessage());
        }
        writeDataToAjax(mes);
        return NONE;
    }

    /**
     * 新建NotifyFilter
     * 
     * @return
     * @throws Exception
     */
    public String create_nFilter() throws Exception {
        String mes = "success";
        SnmpNotifyFilterTable o = new SnmpNotifyFilterTable();
        o.setEntityId(entityId);
        o.setNotifyFilterProfileName(profileName);
        o.setNotifyFilterMask(mask);
        o.setNotifyFilterStorType(storType);
        o.setNotifyFilterSubtree(subtree);
        o.setNotifyFilterType(type);
        try {
            snmpV3Service.addNotifyFilter(o);
        } catch (Exception e) {
            mes = e.getMessage();
            logger.debug("create_nFilter error: " + e.getMessage());
        }
        writeDataToAjax(mes);
        return NONE;
    }

    /**
     * 修改Target
     * 
     * @return
     * @throws Exception
     */
    public String modify_target() throws Exception {
        String mes = "success";
        SnmpTargetTable o = new SnmpTargetTable();
        o.setEntityId(entityId);
        o.setTargetAddress(getAddressFromIpAndPort(address, port));
        o.setTargetDomain(domain);
        o.setTargetName(targetName);
        o.setTargetParams(paramsName);
        o.setTargetRetryCount(retryCount);
        o.setTargetStorageType(storType);
        o.setTargetTagList(tagList);
        o.setTargetTimeout(timeout);
        try {
            snmpV3Service.modifyTarget(o);
        } catch (Exception e) {
            mes = e.getMessage();
            logger.debug("modify_target error: " + e.getMessage());
        }
        writeDataToAjax(mes);
        return NONE;
    }

    /**
     * 修改TargetParams 和 对应的NotifyFilterProfile
     * 
     * @return
     * @throws Exception
     */
    public String modify_tParam() throws Exception {
        String mes = "success";
        SnmpTargetParams o = new SnmpTargetParams();
        o.setEntityId(entityId);
        o.setTargetParamsMPModel(tarParMPModel);
        o.setTargetParamsName(paramsName);
        o.setTargetParamsSecurityLevel(securityLevel);
        o.setTargetParamsSecurityModel(securityModel);
        o.setTargetParamsSecurityName(securityName);
        o.setTargetParamsStorageType(storType);
        try {
            snmpV3Service.modifyTargetParams(o);
        } catch (Exception e) {
            mes = e.getMessage();
            logger.debug("modify_tParam error: " + e.getMessage());
        }
        // 0:don't care; 1:add; 2:modify
        int flag = getAddOrModifyProfileFlag();
        if (flag > 0) {
            SnmpNotifyFilterProfile n = new SnmpNotifyFilterProfile();
            n.setEntityId(entityId);
            n.setNotifyFilterProfileName(profileName);
            n.setNotifyFilterProfileStorType(storType);
            n.setTargetParamsName(paramsName);
            try {
                if (flag == 1) {
                    snmpV3Service.addNotifyProfile(n);
                } else if (flag == 2) {
                    snmpV3Service.modifyNotifyProfile(n);
                }
            } catch (Exception e) {
                mes = e.getMessage();
                logger.debug("modify_tParam_notifyProfile error: " + e.getMessage());
            }
        }
        writeDataToAjax(mes);
        return NONE;
    }

    /**
     * 修改Notify
     * 
     * @return
     * @throws Exception
     */
    public String modify_notify() throws Exception {
        String mes = "success";
        SnmpNotifyTable o = new SnmpNotifyTable();
        o.setEntityId(entityId);
        o.setNotifyName(notifyName);
        o.setNotifyStorageType(storType);
        o.setNotifyTag(notifyTag);
        o.setNotifyType(notifyType);
        try {
            snmpV3Service.modifyNotify(o);
        } catch (Exception e) {
            mes = e.getMessage();
            logger.debug("modify_notify error: " + e.getMessage());
        }
        writeDataToAjax(mes);
        return NONE;
    }

    /**
     * 修改NotifyFilter
     * 
     * @return
     * @throws Exception
     */
    public String modify_nFilter() throws Exception {
        String mes = "success";
        SnmpNotifyFilterTable o = new SnmpNotifyFilterTable();
        o.setEntityId(entityId);
        o.setNotifyFilterProfileName(profileName);
        o.setNotifyFilterStorType(storType);
        o.setNotifyFilterMask(mask);
        o.setNotifyFilterSubtree(subtree);
        o.setNotifyFilterType(type);
        try {
            snmpV3Service.modifyNotifyFilter(o);
        } catch (Exception e) {
            mes = e.getMessage();
            logger.debug("modify_nFilter error: " + e.getMessage());
        }
        writeDataToAjax(mes);
        return NONE;
    }

    /**
     * 删除Target
     * 
     * @return
     * @throws Exception
     */
    public String delete_target() throws Exception {
        String mes = "success";
        try {
            snmpV3Service.deleteTarget(entityId, targetName);
        } catch (Exception e) {
            mes = e.getMessage();
            logger.debug("delete_target error: " + e.getMessage());
        }
        writeDataToAjax(mes);
        return NONE;
    }

    /**
     * 删除TargetParams和对应的notifyFilterProfile
     * 
     * @return
     * @throws Exception
     */
    public String delete_tParam() throws Exception {
        String mes = "success";
        SnmpNotifyFilterProfile o = new SnmpNotifyFilterProfile();
        o.setEntityId(entityId);
        o.setTargetParamsName(paramsName);
        try {
            snmpV3Service.deleteTargetParams(entityId, paramsName);
            snmpV3Service.deleteNotifyProfile(o);
        } catch (Exception e) {
            mes = e.getMessage();
            logger.debug("delete_tParam error: " + e.getMessage());
        }
        writeDataToAjax(mes);
        return NONE;
    }

    /**
     * 删除Notify
     * 
     * @return
     * @throws Exception
     */
    public String delete_notify() throws Exception {
        String mes = "success";
        try {
            snmpV3Service.deleteNotify(entityId, notifyName);
        } catch (Exception e) {
            mes = e.getMessage();
            logger.debug("delete_notify error: " + e.getMessage());
        }
        writeDataToAjax(mes);
        return NONE;
    }

    /**
     * 删除NotifyFilter
     * 
     * @return
     * @throws Exception
     */
    public String delete_nFilter() throws Exception {
        String mes = "success";
        try {
            snmpV3Service.deleteNotifyFilter(entityId, profileName, subtree);
        } catch (Exception e) {
            mes = e.getMessage();
            logger.debug("delete_nFilter error: " + e.getMessage());
        }
        writeDataToAjax(mes);
        return NONE;
    }

    public JSONArray getTargetJson() {
        return targetJson;
    }

    public void setTargetJson(JSONArray targetJson) {
        this.targetJson = targetJson;
    }

    public JSONArray getNotifyJson() {
        return notifyJson;
    }

    public void setNotifyJson(JSONArray notifyJson) {
        this.notifyJson = notifyJson;
    }

    public JSONArray getNfProfileJson() {
        return nfProfileJson;
    }

    public void setNfProfileJson(JSONArray nfProfileJson) {
        this.nfProfileJson = nfProfileJson;
    }

    public JSONArray getTarParamJson() {
        return tarParamJson;
    }

    public void setTarParamJson(JSONArray tarParamJson) {
        this.tarParamJson = tarParamJson;
    }

    public JSONArray getNotifyFilterJson() {
        return notifyFilterJson;
    }

    public void setNotifyFilterJson(JSONArray notifyFilterJson) {
        this.notifyFilterJson = notifyFilterJson;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public Integer getNotifyType() {
        return notifyType;
    }

    public void setNotifyType(Integer notifyType) {
        this.notifyType = notifyType;
    }

    public Long getSecurityModel() {
        return securityModel;
    }

    public void setSecurityModel(Long securityModel) {
        this.securityModel = securityModel;
    }

    public String getSecurityName() {
        return securityName;
    }

    public void setSecurityName(String securityName) {
        this.securityName = securityName;
    }

    public Integer getSecurityLevel() {
        return securityLevel;
    }

    public void setSecurityLevel(Integer securityLevel) {
        this.securityLevel = securityLevel;
    }

    public Integer getStorType() {
        return storType;
    }

    public void setStorType(Integer storType) {
        this.storType = storType;
    }

    public String getTargetName() {
        return targetName;
    }

    public void setTargetName(String targetName) {
        this.targetName = targetName;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public Long getTimeout() {
        return timeout;
    }

    public void setTimeout(Long timeout) {
        this.timeout = timeout;
    }

    public Integer getRetryCount() {
        return retryCount;
    }

    public void setRetryCount(Integer retryCount) {
        this.retryCount = retryCount;
    }

    public String getTagList() {
        return tagList;
    }

    public void setTagList(String tagList) {
        this.tagList = tagList;
    }

    public String getParamsName() {
        return paramsName;
    }

    public void setParamsName(String paramsName) {
        this.paramsName = paramsName;
    }

    public Long getTarParMPModel() {
        return tarParMPModel;
    }

    public void setTarParMPModel(Long tarParMPModel) {
        this.tarParMPModel = tarParMPModel;
    }

    public String getProfileName() {
        return profileName;
    }

    public void setProfileName(String profileName) {
        this.profileName = profileName;
    }

    public String getNotifyName() {
        return notifyName;
    }

    public void setNotifyName(String notifyName) {
        this.notifyName = notifyName;
    }

    public String getNotifyTag() {
        return notifyTag;
    }

    public void setNotifyTag(String notifyTag) {
        this.notifyTag = notifyTag;
    }

    public String getSubtreeMask() {
        return subtreeMask;
    }

    public void setSubtreeMask(String subtreeMask) {
        this.subtreeMask = subtreeMask;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getSubtree() {
        return subtree;
    }

    public void setSubtree(String subtree) {
        this.subtree = subtree;
    }

    public String getMask() {
        return mask;
    }

    public void setMask(String mask) {
        this.mask = mask;
    }

    public Logger getLogger() {
        return logger;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public SnmpV3Service getSnmpV3Service() {
        return snmpV3Service;
    }

    public void setSnmpV3Service(SnmpV3Service snmpV3Service) {
        this.snmpV3Service = snmpV3Service;
    }
}
