/***********************************************************************
 * $Id: TrapServerConfigAction.java,v1.0 2013-4-23 上午9:15:45 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.trapserver.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.topvision.ems.cmc.trapserver.facade.domain.CmcTrapServer;
import com.topvision.ems.cmc.trapserver.service.CmcTrapServerConfigService;
import com.topvision.ems.cmc.util.CmcConstants;
import com.topvision.framework.exception.engine.SnmpException;
import com.topvision.framework.web.struts2.BaseAction;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * @author flack
 * @created @2013-4-23-上午9:15:45
 *
 */
@Controller("cmcTrapServerConfigAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class CmcTrapServerConfigAction extends BaseAction {
    private static final long serialVersionUID = 562098028495396009L;
    private final Logger logger = LoggerFactory.getLogger(CmcTrapServerConfigAction.class);

    private Long entityId;
    private String trapServerIp;
    private Integer trapServerIndex;
    private Integer[] usedIndex;
    @Resource(name = "cmcTrapServerConfigService")
    private CmcTrapServerConfigService cmcTrapServerConfigService;

    /**
     * 显示TrapServer配置页面
     * 
     * @return
     */
    public String showTrapServer() {
        return SUCCESS;
    }

    /**
     * 从数据库加载当前配置的TrapServer
     * 
     * @return
     * @throws IOException
     */
    public String loadTrapServer() throws IOException {
        List<CmcTrapServer> trapServerList = cmcTrapServerConfigService.getAllTrapServer(entityId);
        JSONArray jArray = JSONArray.fromObject(trapServerList);
        writeDataToAjax(jArray);
        return NONE;
    }

    /**
     * 添加TrapServer
     * 
     * @return
     * @throws IOException
     */
    public String addTrapServer() throws IOException {
        CmcTrapServer trapServer = new CmcTrapServer();
        trapServer.setEntityId(entityId);
        List<Integer> avaiableList = new ArrayList<Integer>();
        avaiableList.add(0);
        avaiableList.add(1);
        avaiableList.add(2);
        avaiableList.add(3);
        avaiableList.add(4);
        if (usedIndex != null && usedIndex.length > 0) {
            avaiableList.removeAll(Arrays.asList(usedIndex));
        }
        trapServer.setTopCcmtsTrapServerIndex(avaiableList.get(0));
        trapServer.setTopCcmtsTrapServerIpAddr(trapServerIp);
        trapServer.setTopCcmtsTrapServerStatus(CmcConstants.ROWSTATUS_CREATE);
        JSONObject json = new JSONObject();
        try {
            cmcTrapServerConfigService.addTrapServer(trapServer);
            json.put("success", true);
        } catch (SnmpException e) {
            json.put("success", false);
            logger.error("Add TrapServer to Facility Failed:{}", e.getMessage());
        }
        writeDataToAjax(json);
        return NONE;
    }

    /**
     * 删除TrapServer配置信息
     * 
     * @return
     * @throws IOException
     */
    public String deleteTrapServer() throws IOException {
        CmcTrapServer trapServer = new CmcTrapServer();
        trapServer.setEntityId(entityId);
        trapServer.setTopCcmtsTrapServerIndex(trapServerIndex);
        trapServer.setTopCcmtsTrapServerStatus(CmcConstants.ROWSTATUS_DROP);
        JSONObject json = new JSONObject();
        try {
            cmcTrapServerConfigService.deleteTrapServer(trapServer);
            json.put("success", true);
        } catch (SnmpException e) {
            json.put("success", false);
            logger.error("Delete TrapServer From Facility Failed:{}", e.getMessage());
        }
        writeDataToAjax(json);
        return NONE;
    }

    /**
     * 从设备刷新TrapServer配置信息
     * 
     * @return
     * @throws IOException
     */
    public String refreshTrapServerFromFacility() throws IOException {
        JSONObject json = new JSONObject();
        try {
            cmcTrapServerConfigService.refreshTrapServerFromFacility(entityId);
            json.put("success", true);
        } catch (SnmpException e) {
            json.put("success", false);
            logger.error("Refresh TrapServer from Facility failed:{}", e.getMessage());
        }
        writeDataToAjax(json);
        return NONE;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public String getTrapServerIp() {
        return trapServerIp;
    }

    public void setTrapServerIp(String trapServerIp) {
        this.trapServerIp = trapServerIp;
    }

    public Integer getTrapServerIndex() {
        return trapServerIndex;
    }

    public void setTrapServerIndex(Integer trapServerIndex) {
        this.trapServerIndex = trapServerIndex;
    }

    public Integer[] getUsedIndex() {
        return usedIndex;
    }

    public void setUsedIndex(Integer[] usedIndex) {
        this.usedIndex = usedIndex;
    }

    public CmcTrapServerConfigService getCmcTrapServerConfigService() {
        return cmcTrapServerConfigService;
    }

    public void setCmcTrapServerConfigService(CmcTrapServerConfigService cmcTrapServerConfigService) {
        this.cmcTrapServerConfigService = cmcTrapServerConfigService;
    }

}
