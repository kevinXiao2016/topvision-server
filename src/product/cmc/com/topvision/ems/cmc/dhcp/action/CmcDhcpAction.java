/***********************************************************************
 * $Id: CmcDhcpAction.java,v1.0 2012-2-12 下午05:28:31 $
 * 
 * @author: zhanglongyang
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.dhcp.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.topvision.ems.cmc.ccmts.facade.domain.CmcAttribute;
import com.topvision.ems.cmc.ccmts.service.CmcService;
import com.topvision.framework.constants.Symbol;
import com.topvision.ems.cmc.dhcp.facade.domain.CmcDhcpBaseConfig;
import com.topvision.ems.cmc.dhcp.facade.domain.CmcDhcpBundle;
import com.topvision.ems.cmc.dhcp.facade.domain.CmcDhcpGiAddr;
import com.topvision.ems.cmc.dhcp.facade.domain.CmcDhcpIntIp;
import com.topvision.ems.cmc.dhcp.facade.domain.CmcDhcpOption60;
import com.topvision.ems.cmc.dhcp.facade.domain.CmcDhcpServerConfig;
import com.topvision.ems.cmc.dhcp.service.CmcDhcpService;
import com.topvision.ems.cmc.util.StringUtils;
import com.topvision.framework.web.struts2.BaseAction;

/**
 * DHCP相关功能
 * 
 * @author zhanglongyang
 * @created @2012-2-12-下午05:28:31
 * 
 */
@Controller("cmcDhcpAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class CmcDhcpAction extends BaseAction {
    private static final long serialVersionUID = 4969666150156817575L;
    private Logger logger = LoggerFactory.getLogger(CmcDhcpAction.class);
    @Resource(name = "cmcDhcpService")
    protected CmcDhcpService cmcDhcpService;
    @Resource(name = "cmcService")
    protected CmcService cmcService;

    private JSONArray dhcpBundleList;
    private JSONArray dhcpServerList;
    private JSONArray dhcpGiAddrList;
    private JSONArray dhcpIntIpList;
    private JSONArray dhcpOption60List;

    // server\giaddr\intip在action中公用：修改/新增标志、 index、type、ip、ipmask
    private Integer modifyFlag;
    private Long cmcId;
    private Long index;
    private Integer type;
    private String ip;
    private String ipMask;
    private String option60Str;
    private JSONArray indexList;
    // end公用的
    private JSONArray ifIndexList;
    private Long ifIndex;

    private String dhcpType;
    private String bundle;
    private CmcDhcpBaseConfig cmcDhcpBaseConfigInfo;
    private CmcAttribute cmcAttribute;

    /**
     * 展示DHCP relay配置页面
     *
     * @return String
     */
    public String showDhcpRelay() {
    	cmcAttribute = cmcService.getCmcAttributeByCmcId(cmcId);
        return SUCCESS;
    }

    /**
     * 修改DHCP relay
     *
     * @return String
     */
    public String modifyDhcpRelay() {
        return NONE;
    }

    /**
     * 刷新DHCP relay
     *
     * @return String
     */
    public String refreshDhcpRelay() {
        return NONE;
    }

    /**
     * 展示DHCP Bundle配置页面
     *
     * @return String
     */
    public String showDhcpBundle() {
        JSONArray childrens = new JSONArray();
        List<CmcDhcpBundle> tmpBundleList;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("cmcId", cmcId);

        tmpBundleList = cmcDhcpService.getCmcDhcpBundleList(map);
        for (CmcDhcpBundle dhcpBundle : tmpBundleList) {
            JSONObject childrenNode = new JSONObject();
            childrenNode.put("id", "0_" + dhcpBundle.getTopCcmtsDhcpBundleInterface());
            childrenNode.put("text", dhcpBundle.getTopCcmtsDhcpBundleInterface());
            childrenNode.put("leaf", false);
            childrens.add(childrenNode);
            childrenNode.put("id", "1_" + dhcpBundle.getTopCcmtsDhcpBundleInterface());
            childrenNode.put("text", "CM");
            childrenNode.put("leaf", true);
            childrens.add(childrenNode);
            childrenNode.put("id", "2_" + dhcpBundle.getTopCcmtsDhcpBundleInterface());
            childrenNode.put("text", "HOST");
            childrenNode.put("leaf", true);
            childrens.add(childrenNode);
            childrenNode.put("id", "3_" + dhcpBundle.getTopCcmtsDhcpBundleInterface());
            childrenNode.put("text", "MTA");
            childrenNode.put("leaf", true);
            childrens.add(childrenNode);
            childrenNode.put("id", "4_" + dhcpBundle.getTopCcmtsDhcpBundleInterface());
            childrenNode.put("text", "STB");
            childrenNode.put("leaf", true);
            childrens.add(childrenNode);
            childrenNode.put("id", "5_" + dhcpBundle.getTopCcmtsDhcpBundleInterface());
            childrenNode.put("text", "ALL");
            childrenNode.put("leaf", true);
            childrens.add(childrenNode);
        }
        writeDataToAjax(childrens.toString());
        return NONE;
    }

    /**
     * 删除一条DHCP Bundle配置
     *
     * @return String
     */
    public String deleteDhcpBundle() {
        String message;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("cmcId", cmcId);
        map.put("topCcmtsDhcpBundleInterface", bundle);
        try {
            cmcDhcpService.deleteDhcpBundle(map);
            message = "success";
        } catch (Exception e) {
            message = "error[" + e.getMessage() + Symbol.BRACKET_RIGHT;
            logger.debug("deleteDhcpBundle is error:{}", e);
        }
        writeDataToAjax(message);
        return NONE;
    }

    /**
     * 添加一条DHCP Bundle配置
     *
     * @return String
     */
    public String addDhcpBundle() {
        String message;
        try {
            CmcDhcpBundle tmpObj = new CmcDhcpBundle();
            tmpObj.setCmcId(cmcId);
            tmpObj.setTopCcmtsDhcpBundlePolicy(type);
            tmpObj.setTopCcmtsDhcpBundleInterface(bundle);
            tmpObj.setEntityId(cmcService.getEntityIdByCmcId(cmcId));
            cmcDhcpService.addDhcpBundle(tmpObj);
            message = "success";
        } catch (Exception e) {
            message = "error[" + e.getMessage() + Symbol.BRACKET_RIGHT;
            logger.debug("addDhcpBundle is error:{}", e);
        }
        writeDataToAjax(message);
        return NONE;
    }

    /**
     * 展示DHCP Bundle修改页面
     *
     * @return String
     */
    public String showDhcpBundleModify() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("cmcId", cmcId);
        List<CmcDhcpBundle> tmpBundleList = cmcDhcpService.getCmcDhcpBundleList(map);
        dhcpBundleList = JSONArray.fromObject(tmpBundleList);
        if (modifyFlag == 1) {
            map.put("cmcId", cmcId);
            map.put("topCcmtsDhcpBundleInterface", bundle);
            CmcDhcpBundle tmpObj = cmcDhcpService.getCmcDhcpBundle(map);
            type = tmpObj.getTopCcmtsDhcpBundlePolicy();
        }
        return SUCCESS;
    }

    /**
     * 修改一条DHCP Bundle配置
     *
     * @return String
     */
    public String modifyDhcpBundle() {
        String message;
        try {
            CmcDhcpBundle tmpObj = new CmcDhcpBundle();
            tmpObj.setCmcId(cmcId);
            tmpObj.setTopCcmtsDhcpBundlePolicy(type);
            tmpObj.setTopCcmtsDhcpBundleInterface(bundle);
            tmpObj.setEntityId(cmcService.getEntityIdByCmcId(cmcId));
            cmcDhcpService.modifyDhcpBundle(tmpObj);
            message = "success";
        } catch (Exception e) {
            message = "error[" + e.getMessage() + Symbol.BRACKET_RIGHT;
            logger.debug("modifyDhcpBundle is error:{}", e);
        }
        writeDataToAjax(message);
        return NONE;
    }

    /**
     * 刷新DHCP Bundle
     *
     * @return String
     */
    public String refreshDhcpBundle() {
        String message;
        try {
            cmcDhcpService.refreshDhcpInfo(cmcId);
            message = "true";
        } catch (Exception e) {
            message = e.getMessage();
            logger.debug("", e);
        }
        writeDataToAjax(message);
        return NONE;
    }

    /**
     * 展示DHCP server配置页面
     *
     * @return String
     */
    public String showDhcpServer() {
        Map<String, Object> json = new HashMap<String, Object>();
        List<CmcDhcpServerConfig> tmpServerList;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("cmcId", cmcId);
        if (dhcpType != null) {
            String[] types = dhcpType.split(Symbol.UNDERLINE);
            if (!StringUtils.isZERO(types[1])) {
                map.put("topCcmtsDhcpBundleInterface", types[1]);
            }
            if (!StringUtils.isZERO(types[0])) {
                map.put("topCcmtsDhcpBundleInterface", types[0]);
            }
        }

        tmpServerList = cmcDhcpService.getCmcDhcpServerList(map);
        json.put("data", tmpServerList);
        writeDataToAjax(JSONObject.fromObject(json));

        /*
         * if (tmpServerList.size() > 0) { dhcpServerList = JSONArray.fromObject(tmpServerList); }
         * else { dhcpServerList = JSONArray.fromObject(false); }
         */
        return NONE;
    }

    /**
     * 删除一条DHCP server配置
     *
     * @return String
     */
    public String deleteDhcpServer() {
        String message;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("cmcId", cmcId);
        map.put("helperId", index);
        try {
            cmcDhcpService.deleteDhcpServer(map);
            message = "success";
        } catch (Exception e) {
            message = "error[" + e.getMessage() + Symbol.BRACKET_RIGHT;
            logger.debug("deleteDhcpServer is error:{}", e);
        }
        writeDataToAjax(message);
        return NONE;
    }

    /**
     * 添加一条DHCP server配置
     *
     * @return String
     */
    public String addDhcpServer() {
        String message;
        try {
            CmcDhcpServerConfig tmpObj = new CmcDhcpServerConfig();
            tmpObj.setCmcId(cmcId);
            tmpObj.setTopCcmtsDhcpHelperIpAddr(ip);
            tmpObj.setTopCcmtsDhcpBundleInterface(bundle);
            tmpObj.setTopCcmtsDhcpHelperDeviceType(type);
            tmpObj.setEntityId(cmcService.getEntityIdByCmcId(cmcId));
            tmpObj.setTopCcmtsDhcpHelperIndex(cmcDhcpService.getCcmtsDhcpIndex(cmcId, bundle, type, 1));
            cmcDhcpService.addDhcpServer(tmpObj);
            message = "success";
        } catch (Exception e) {
            message = "error[" + e.getMessage() + Symbol.BRACKET_RIGHT;
            logger.debug("addDhcpServer is error:{}", e);
        }
        writeDataToAjax(message);
        return NONE;
    }

    /**
     * 展示DHCP server修改页面
     *
     * @return String
     */
    public String showDhcpServerModify() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("cmcId", cmcId);
        List<CmcDhcpBundle> tmpBundleList = cmcDhcpService.getCmcDhcpBundleList(map);
        dhcpBundleList = JSONArray.fromObject(tmpBundleList);
        return SUCCESS;
    }

    /**
     * 修改一条DHCP server配置
     *
     * @return String
     */
    public String modifyDhcpServer() {
        String message;
        try {
            CmcDhcpServerConfig tmpObj = new CmcDhcpServerConfig();
            tmpObj.setCmcId(cmcId);
            tmpObj.setTopCcmtsDhcpHelperIpAddr(ip);
            tmpObj.setTopCcmtsDhcpBundleInterface(bundle);
            tmpObj.setHelperId(index);
            tmpObj.setEntityId(cmcService.getEntityIdByCmcId(cmcId));
            tmpObj.setTopCcmtsDhcpHelperDeviceType(type);
            cmcDhcpService.modifyDhcpServer(tmpObj);
            message = "success";
        } catch (Exception e) {
            message = "error[" + e.getMessage() + Symbol.BRACKET_RIGHT;
            logger.debug("modifyDhcpServer is error:{}", e);
        }
        writeDataToAjax(message);
        return NONE;
    }

    /**
     * 刷新DHCP server
     *
     * @return String
     */
    public String refreshDhcpServer() {
        // TODO 数据
        return NONE;
    }

    /**
     * 展示DHCP GiAddr配置页面
     *
     * @return String
     */
    public String showDhcpGiAddr() {
        Map<String, Object> json = new HashMap<String, Object>();
        List<CmcDhcpGiAddr> tmpGiAddrList;
        Map<String, Object> map = new HashMap<String, Object>();
        CmcDhcpBundle tmpCmcDhcpBundle;
        map.put("cmcId", cmcId);
        if (dhcpType != null) {
            String[] types = dhcpType.split(Symbol.UNDERLINE);
            if (!StringUtils.isZERO(types[1])) {

                map.put("topCcmtsDhcpBundleInterface", types[1]);
                tmpCmcDhcpBundle = cmcDhcpService.getCmcDhcpBundle(map);
                map.put("topCcmtsDhcpGiAddrPolicyType", tmpCmcDhcpBundle.getTopCcmtsDhcpBundlePolicy());
                if (tmpCmcDhcpBundle.getTopCcmtsDhcpBundlePolicy() == 2) {
                    if (types[0].endsWith("1")) {
                        map.put("topCcmtsDhcpGiAddrIndex", 1);
                    } else if (types[0].endsWith("0")) {
                        map.put("topCcmtsDhcpGiAddrIndex", null);
                    } else if (types[0].endsWith("5")) {
                        map.put("topCcmtsDhcpGiAddrIndex", 0);
                    } else {
                        map.put("noTopCcmtsDhcpGiAddrIndex", 1);
                    }
                } else if (tmpCmcDhcpBundle.getTopCcmtsDhcpBundlePolicy() == 3) {
                    if (types[0].endsWith("1")) {
                        map.put("topCcmtsDhcpGiAddrIndex", 1);
                    } else if (types[0].endsWith("2")) {
                        map.put("topCcmtsDhcpGiAddrIndex", 2);
                    } else if (types[0].endsWith("3")) {
                        map.put("topCcmtsDhcpGiAddrIndex", 3);
                    } else if (types[0].endsWith("4")) {
                        map.put("topCcmtsDhcpGiAddrIndex", 4);
                    } else if (types[0].endsWith("0")) {
                        map.put("topCcmtsDhcpGiAddrIndex", null);
                    } else {
                        map.put("topCcmtsDhcpGiAddrIndex", 0);
                    }
                } else if (tmpCmcDhcpBundle.getTopCcmtsDhcpBundlePolicy() == 1) {
                    if (types[0].endsWith("0")) {
                        map.put("topCcmtsDhcpGiAddrIndex", null);
                    } else {
                        map.put("topCcmtsDhcpGiAddrIndex", 1);
                    }
                }

            }
        }
        tmpGiAddrList = cmcDhcpService.getCmcDhcpGiAddrList(map);
        json.put("data", tmpGiAddrList);
        writeDataToAjax(JSONObject.fromObject(json));
        /*
         * if (tmpGiAddrList.size() > 0) { dhcpGiAddrList = JSONArray.fromObject(tmpGiAddrList); }
         * else { dhcpGiAddrList = JSONArray.fromObject(false); }
         */
        return NONE;
    }

    /**
     * 删除一条DHCP GiAddr配置
     *
     * @return String
     */
    public String deleteDhcpGiAddr() {
        String message;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("cmcId", cmcId);
        map.put("giAddrId", index);
        try {
            cmcDhcpService.deleteDhcpGiAddr(map);
            message = "success";
        } catch (Exception e) {
            message = "error[" + e.getMessage() + Symbol.BRACKET_RIGHT;
            logger.debug("deleteDhcpGiAddr is error:{}", e);
        }
        writeDataToAjax(message);
        return NONE;
    }

    /**
     * 添加一条DHCP GiAddr配置
     *
     * @return String
     */
    public String addDhcpGiAddr() {
        String message;
        try {
            CmcDhcpGiAddr tmpObj = new CmcDhcpGiAddr();
            tmpObj.setCmcId(cmcId);
            tmpObj.setTopCcmtsDhcpGiAddress(ip);
            //tmpObj.setTopCcmtsDhcpGiAddrMask(ipMask);
            tmpObj.setTopCcmtsDhcpBundleInterface(bundle);
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("cmcId", cmcId);
            map.put("topCcmtsDhcpBundleInterface", bundle);
            type = cmcDhcpService.getCmcDhcpBundle(map).getTopCcmtsDhcpBundlePolicy();
            //tmpObj.setTopCcmtsDhcpGiAddrPolicyType(type);
            tmpObj.setEntityId(cmcService.getEntityIdByCmcId(cmcId));
            //tmpObj.setTopCcmtsDhcpGiAddrIndex(cmcDhcpService.getCcmtsDhcpIndex(cmcId, bundle, type, 2));
            cmcDhcpService.addDhcpGiAddr(tmpObj);
            message = "success";
        } catch (Exception e) {
            message = "error[" + e.getMessage() + Symbol.BRACKET_RIGHT;
            logger.debug("addDhcpGiAddr is error:{}", e);
        }
        writeDataToAjax(message);
        return NONE;
    }

    /**
     * 展示DHCP GiAddr修改页面
     *
     * @return String
     */
    public String showDhcpGiAddrModify() {
        // tmpBundleList = cmcDhcpService.getCmcDhcpBundleList(cmcId, 0);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("cmcId", cmcId);
        List<CmcDhcpBundle> tmpBundleList = cmcDhcpService.getCmcDhcpBundleList(map);
        dhcpBundleList = JSONArray.fromObject(tmpBundleList);
        List<CmcDhcpGiAddr> tmpGiAddrList = cmcDhcpService.getCmcDhcpGiAddrList(map);
        dhcpGiAddrList = JSONArray.fromObject(tmpGiAddrList);

        /*
         * if (tmpBundleList!=null && tmpBundleList.size() > 0) { indexList =
         * JSONArray.fromObject(tmpBundleList); } else { indexList = JSONArray.fromObject(false); }
         */
        return SUCCESS;
    }

    /**
     * 修改一条DHCP GiAddr配置
     *
     * @return String
     */
    public String modifyDhcpGiAddr() {
        String message;
        try {
            CmcDhcpGiAddr tmpObj = new CmcDhcpGiAddr();
            tmpObj.setCmcId(cmcId);
            tmpObj.setGiAddrId(index);
            tmpObj.setTopCcmtsDhcpGiAddress(ip);
            // tmpObj.setTopCcmtsDhcpGiAddrMask(ipMask);
            tmpObj.setTopCcmtsDhcpBundleInterface(bundle);
            tmpObj.setEntityId(cmcService.getEntityIdByCmcId(cmcId));
            //tmpObj.setTopCcmtsDhcpGiAddrPolicyType(type);
            cmcDhcpService.modifyDhcpGiAddr(tmpObj);
            message = "success";
        } catch (Exception e) {
            message = "error[" + e.getMessage() + Symbol.BRACKET_RIGHT;
            logger.debug("modifyDhcpGiAddr is error:{}", e);
        }
        writeDataToAjax(message);
        return NONE;
    }

    /**
     * 刷新DHCP GiAddr
     *
     * @return String
     */
    public String refreshDhcpGiAddr() {
        // TODO 数据
        return NONE;
    }

    /**
     * 展示DHCP IntIp配置页面
     *
     * @return String
     */
    public String showDhcpIntIp() {
        List<CmcDhcpIntIp> tmpIntIpList;
        tmpIntIpList = cmcDhcpService.getCmcDhcpIntIpList(cmcId);
        if (tmpIntIpList.size() > 0) {
            dhcpIntIpList = JSONArray.fromObject(tmpIntIpList);
        } else {
            dhcpIntIpList = JSONArray.fromObject(false);
        }
        return SUCCESS;
    }

    /**
     * 删除一条DHCP IntIp配置
     *
     * @return String
     */
    public String deleteDhcpIntIp() {
        String message = "success";
        try {
            cmcDhcpService.deleteDhcpIntIp(cmcId, index.intValue(), ifIndex);
        } catch (Exception e) {
            message = "error[" + e.getMessage() + Symbol.BRACKET_RIGHT;
            logger.debug("deleteDhcpIntIp is error:{}", e);
        }
        writeDataToAjax(message);
        return NONE;
    }

    /**
     * 添加一条DHCP IntIp配置
     *
     * @return String
     */
    public String addDhcpIntIp() {
        String message = "success";
        try {
            CmcDhcpIntIp tmpObj = new CmcDhcpIntIp();
            tmpObj.setCmcId(cmcId);
            //tmpObj.setTopCcmtsDhcpIfIndex(ifIndex);
            tmpObj.setTopCcmtsDhcpIntIpAddr(ip);
            tmpObj.setTopCcmtsDhcpIntIpIndex(index.intValue());
            tmpObj.setTopCcmtsDhcpIntIpMask(ipMask);
            cmcDhcpService.addDhcpIntIp(tmpObj);
        } catch (Exception e) {
            message = "error[" + e.getMessage() + Symbol.BRACKET_RIGHT;
            logger.debug("addDhcpIntIp is error:{}", e);
        }
        writeDataToAjax(message);
        return NONE;
    }

    /**
     * 展示DHCP IntIp修改页面
     *
     * @return String
     */
    public String showDhcpIntIpModify() {
        List<Integer> tmpList;
        List<Long> tmpIfList;
        tmpList = cmcDhcpService.getCmcDhcpIndexList(cmcId, null, null, 4);
        tmpIfList = cmcDhcpService.getCmcDhcpIfIndexList(cmcId);
        if (tmpList.size() > 0 && tmpIfList.size() > 0) {
            indexList = JSONArray.fromObject(tmpList);
            ifIndexList = JSONArray.fromObject(tmpIfList);
        } else {
            indexList = JSONArray.fromObject(false);
            ifIndexList = JSONArray.fromObject(false);
        }
        return SUCCESS;
    }

    /**
     * 修改一条DHCP IntIp配置
     *
     * @return String
     */
    public String modifyDhcpIntIp() {
        String message = "success";
        try {
            CmcDhcpIntIp tmpObj = new CmcDhcpIntIp();
            tmpObj.setCmcId(cmcId);
            //tmpObj.setTopCcmtsDhcpIfIndex(ifIndex);
            tmpObj.setTopCcmtsDhcpIntIpAddr(ip);
            tmpObj.setTopCcmtsDhcpIntIpIndex(index.intValue());
            tmpObj.setTopCcmtsDhcpIntIpMask(ipMask);
            cmcDhcpService.modifyDhcpIntIp(tmpObj);
        } catch (Exception e) {
            message = "error[" + e.getMessage() + Symbol.BRACKET_RIGHT;
            logger.debug("modifyDhcpIntIp is error:{}", e);
        }
        writeDataToAjax(message);
        return NONE;
    }

    /**
     * 刷新DHCP IntIp
     *
     * @return String
     */
    public String refreshDhcpIntIp() {
        // TODO 数据
        return NONE;
    }

    /**
     * 展示DHCP Option60配置页面
     *
     * @return String
     */
    public String showDhcpOption60() {
        Map<String, Object> json = new HashMap<String, Object>();
        List<CmcDhcpOption60> tmpOption60List;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("cmcId", cmcId);
        if(dhcpType!=null){
            String[] types = dhcpType.split(Symbol.UNDERLINE);
            if(!StringUtils.isZERO(types[1])){
                map.put("topCcmtsDhcpBundleInterface", types[1]); 
            }
            if(!StringUtils.isZERO(types[0])){
                map.put("deviceType", types[0]); 
            }
        }
        tmpOption60List = cmcDhcpService.getCmcDhcpOption60List(map);
        json.put("data", tmpOption60List);
        writeDataToAjax(JSONObject.fromObject(json));
        /*
         * if (tmpOption60List.size() > 0) { dhcpOption60List =
         * JSONArray.fromObject(tmpOption60List); } else { dhcpOption60List =
         * JSONArray.fromObject(false); }
         */
        return NONE;
    }

    /**
     * 删除一条DHCP Option60配置
     *
     * @return String
     */
    public String deleteDhcpOption60() {
        String message;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("cmcId", cmcId);
        map.put("option60Id", index);
        try {
            cmcDhcpService.deleteDhcpOption60(map);
            message = "success";
        } catch (Exception e) {
            message = "error[" + e.getMessage() + Symbol.BRACKET_RIGHT;
            logger.debug("deleteDhcpOption60 is error:{}", e);
        }
        writeDataToAjax(message);
        return NONE;
    }

    /**
     * 添加一条DHCP Option60配置
     *
     * @return String
     */
    public String addDhcpOption60() {
        String message;
        try {
            CmcDhcpOption60 tmpObj = new CmcDhcpOption60();
            tmpObj.setCmcId(cmcId);
            tmpObj.setTopCcmtsDhcpOption60Str(option60Str);
            tmpObj.setTopCcmtsDhcpBundleInterface(bundle);
            tmpObj.setTopCcmtsDhcpOption60DeviceType(type);
            tmpObj.setEntityId(cmcService.getEntityIdByCmcId(cmcId));
            tmpObj.setTopCcmtsDhcpOption60Index(cmcDhcpService.getCcmtsDhcpIndex(cmcId, bundle, type, 3));
            cmcDhcpService.addDhcpOption60(tmpObj);
            message = "success";
        } catch (Exception e) {
            message = "error[" + e.getMessage() + Symbol.BRACKET_RIGHT;
            logger.debug("addDhcpOption60 is error:{}", e);
        }
        writeDataToAjax(message);
        return NONE;
    }

    /**
     * 展示DHCP Option60修改页面
     *
     * @return String
     */
    public String showDhcpOption60Modify() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("cmcId", cmcId);
        List<CmcDhcpBundle> tmpBundleList = cmcDhcpService.getCmcDhcpBundleList(map);
        dhcpBundleList = JSONArray.fromObject(tmpBundleList);
        return SUCCESS;
    }

    /**
     * 修改一条DHCP Option60配置
     *
     * @return String
     */
    public String modifyDhcpOption60() {
        String message;
        try {
            CmcDhcpOption60 tmpObj = new CmcDhcpOption60();
            tmpObj.setCmcId(cmcId);
            tmpObj.setOption60Id(index);
            tmpObj.setTopCcmtsDhcpOption60Str(option60Str);
            tmpObj.setTopCcmtsDhcpBundleInterface(bundle);
            tmpObj.setTopCcmtsDhcpOption60DeviceType(type);
            tmpObj.setEntityId(cmcService.getEntityIdByCmcId(cmcId));
            cmcDhcpService.modifyDhcpOption60(tmpObj);
            message = "success";
        } catch (Exception e) {
            message = "error[" + e.getMessage() + Symbol.BRACKET_RIGHT;
            logger.debug("modifyDhcpOption60 is error:{}", e);
        }
        writeDataToAjax(message);
        return NONE;
    }

    /**
     * 刷新DHCP Option60
     *
     * @return String
     */
    public String refreshDhcpOption60() {
        // TODO 数据
        return NONE;
    }

    /**
     * 修改DHCP Relay基本信息
     *
     * @return String
     */
    public String modifyCmcDhcpRelayBaseInfo() {
        String message;
        cmcDhcpBaseConfigInfo.setCmcId(cmcId);
        cmcDhcpBaseConfigInfo.setEntityId(cmcService.getEntityIdByCmcId(cmcId));
        //cmcDhcpBaseConfigInfo.calTopCcmtsDhcpSnoopingHex();
        try {
            cmcDhcpService.modifyCmcDhcpRelayBaseInfo(cmcDhcpBaseConfigInfo);
            message = "success";
        } catch (Exception e) {
            message = "error[" + e.getMessage() + Symbol.BRACKET_RIGHT;
            logger.debug("modifyCmcBasicInfo is error:{}", e);
        }
        writeDataToAjax(message);
        return NONE;
    }
    

    public CmcDhcpService getCmcDhcpService() {
        return cmcDhcpService;
    }

    public void setCmcDhcpService(CmcDhcpService cmcDhcpService) {
        this.cmcDhcpService = cmcDhcpService;
    }

    public CmcService getCmcService() {
        return cmcService;
    }

    public void setCmcService(CmcService cmcService) {
        this.cmcService = cmcService;
    }

    public JSONArray getDhcpServerList() {
        return dhcpServerList;
    }

    public void setDhcpServerList(JSONArray dhcpServerList) {
        this.dhcpServerList = dhcpServerList;
    }

    public JSONArray getDhcpGiAddrList() {
        return dhcpGiAddrList;
    }

    public void setDhcpGiAddrList(JSONArray dhcpGiAddrList) {
        this.dhcpGiAddrList = dhcpGiAddrList;
    }

    public JSONArray getDhcpIntIpList() {
        return dhcpIntIpList;
    }

    public void setDhcpIntIpList(JSONArray dhcpIntIpList) {
        this.dhcpIntIpList = dhcpIntIpList;
    }

    public JSONArray getDhcpOption60List() {
        return dhcpOption60List;
    }

    public void setDhcpOption60List(JSONArray dhcpOption60List) {
        this.dhcpOption60List = dhcpOption60List;
    }

    public Integer getModifyFlag() {
        return modifyFlag;
    }

    public void setModifyFlag(Integer modifyFlag) {
        this.modifyFlag = modifyFlag;
    }

    public Long getIndex() {
        return index;
    }

    public void setIndex(Long index) {
        this.index = index;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getIpMask() {
        return ipMask;
    }

    public void setIpMask(String ipMask) {
        this.ipMask = ipMask;
    }

    public Long getIfIndex() {
        return ifIndex;
    }

    public void setIfIndex(Long ifIndex) {
        this.ifIndex = ifIndex;
    }

    public JSONArray getIndexList() {
        return indexList;
    }

    public void setIndexList(JSONArray indexList) {
        this.indexList = indexList;
    }

    public JSONArray getIfIndexList() {
        return ifIndexList;
    }

    public void setIfIndexList(JSONArray ifIndexList) {
        this.ifIndexList = ifIndexList;
    }

    public JSONArray getDhcpBundleList() {
        return dhcpBundleList;
    }

    public void setDhcpBundleList(JSONArray dhcpBundleList) {
        this.dhcpBundleList = dhcpBundleList;
    }

    public String getDhcpType() {
        return dhcpType;
    }

    public void setDhcpType(String dhcpType) {
        this.dhcpType = dhcpType;
    }

    public String getBundle() {
        return bundle;
    }

    public void setBundle(String bundle) {
        this.bundle = bundle;
    }

    public String getOption60Str() {
        return option60Str;
    }

    public void setOption60Str(String option60Str) {
        this.option60Str = option60Str;
    }

    public CmcDhcpBaseConfig getCmcDhcpBaseConfigInfo() {
        return cmcDhcpBaseConfigInfo;
    }

    public void setCmcDhcpBaseConfigInfo(CmcDhcpBaseConfig cmcDhcpBaseConfigInfo) {
        this.cmcDhcpBaseConfigInfo = cmcDhcpBaseConfigInfo;
    }

    public Long getCmcId() {
        return cmcId;
    }

    public void setCmcId(Long cmcId) {
        this.cmcId = cmcId;
    }

	public CmcAttribute getCmcAttribute() {
		return cmcAttribute;
	}

	public void setCmcAttribute(CmcAttribute cmcAttribute) {
		this.cmcAttribute = cmcAttribute;
	}
}
