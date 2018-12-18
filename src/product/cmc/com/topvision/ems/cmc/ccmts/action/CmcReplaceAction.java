/***********************************************************************
 * $Id: CmcReplaceAction.java,v1.0 2016-4-18 下午1:58:01 $
 * 
 * @author: Rod John
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.ccmts.action;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.topvision.ems.cmc.ccmts.domain.CmcReplaceInfo;
import com.topvision.ems.cmc.ccmts.facade.domain.CmcAttribute;
import com.topvision.ems.cmc.ccmts.service.CmcReplaceService;
import com.topvision.ems.cmc.ccmts.service.CmcService;
import com.topvision.ems.cmc.exception.CmcForceReplaceException;
import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.network.service.EntityService;
import com.topvision.framework.annotation.OperationLogProperty;
import com.topvision.framework.common.MacUtils;
import com.topvision.framework.exception.engine.SnmpSetException;
import com.topvision.framework.web.struts2.BaseAction;
import com.topvision.platform.domain.OperationLog;
import com.topvision.platform.domain.UserContext;

import net.sf.json.JSONObject;

/**
 * @author Rod John
 * @created @2016-4-18-下午1:58:01
 *
 */
@Controller("cmcReplaceAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class CmcReplaceAction extends BaseAction {
    private static final long serialVersionUID = -2492823765506667764L;
    private final Logger logger = LoggerFactory.getLogger(CmcReplaceAction.class);
    private Long entityId;
    private Long cmcId;
    private Long replaceCmcId;
    private Long cmcIndex;
    private CmcAttribute cmcAttribute;
    private String cmcMac;
    private String configFile;
    private Entity entity;
    @Autowired
    private CmcService cmcService;
    @Autowired
    private CmcReplaceService cmcReplaceService;
    @Autowired
    private EntityService entityService;
    private Map<String, Map<String, Object>> onuMacList;
    private JSONObject onuMacsJson;
    private Integer forceReplace;
    private Integer operationResult;

    /**
     * CMC-II替换页面信息
     * 
     * @return
     */
    public String showCmcIIReplaceView() {
        UserContext uc = (UserContext) super.getSession().get(UserContext.KEY);
        String macRule = uc.getMacDisplayStyle();
        entity = entityService.getEntity(cmcId);
        entity.setMac(MacUtils.convertMacToDisplayFormat(entity.getMac(), macRule));
        cmcAttribute = cmcService.getCmcAttributeByCmcId(cmcId);
        entityId = cmcAttribute.getEntityId();
        cmcIndex = cmcAttribute.getCmcIndex();
        onuMacList = cmcReplaceService.getOnuMacListByEntityId(entityId);
        onuMacsJson = JSONObject.fromObject(onuMacList);
        return SUCCESS;
    }

    /**
     * CMC-I替换页面信息
     * 
     * @return
     */
    public String showCmcIReplaceView() {
        entity = entityService.getEntity(cmcId);
        cmcAttribute = cmcService.getCmcAttributeByCmcId(cmcId);
        // entityId = cmcAttribute.getEntityId();
        entityId = cmcAttribute.getCmcId();
        cmcIndex = cmcAttribute.getCmcIndex();
        return SUCCESS;
    }

    /**
     * 加载替换列表
     * 
     * @return
     * @throws IOException
     */
    public String loadCmcReplaceList() throws IOException {
        List<CmcReplaceInfo> cmcReplaceInfos = cmcReplaceService.loadCmcReplaceList(cmcId);
        writeDataToAjax(cmcReplaceInfos);
        return NONE;
    }

    /**
     * 加载配置文件列表
     * 
     * @return
     * @throws IOException
     */
    public String loadCmcReplaceConfigFile() throws IOException {
        Map<String, Object> treeResult = cmcReplaceService.loadConfigFile(cmcId);
        writeDataToAjax(treeResult);
        return NONE;
    }

    /**
     * Replace Cmc II
     * 
     * @return
     * @throws Exception
     */
    @OperationLogProperty(actionName = "replaceCmcII", operationName = "replaceCmcII")
    public String replaceCmcII() throws Exception {
        String result = null;
        try {
            cmcReplaceService.replaceCmc(entityId, cmcId, cmcIndex, cmcMac, forceReplace);
            operationResult = OperationLog.SUCCESS;
            result = "success";
        } catch (CmcForceReplaceException e) {
            logger.error("replaceCmcII error:", e);
            operationResult = OperationLog.FAILURE;
            result = "cmcforcereplaceerror";
        } catch (SnmpSetException e) {
            logger.error("replaceCmcII error:", e);
            operationResult = OperationLog.FAILURE;
            result = "seterror";
        } catch (Exception e) {
            logger.error("replaceCmcII error:", e);
            operationResult = OperationLog.FAILURE;
            result = "error";
        }
        writeDataToAjax(result);
        return NONE;
    }

    /**
     * Replace Cmc I
     * 
     * @return
     * @throws Exception
     */
    @OperationLogProperty(actionName = "replaceCmcI", operationName = "replaceCmcI")
    public String replaceCmcI() throws Exception {
        String result = null;
        //Do For OperationLog
        entityId = cmcId;
        try {
            result = cmcReplaceService.replaceCmc(cmcId, replaceCmcId, configFile);
            operationResult = OperationLog.SUCCESS;
        } catch (Exception e) {
            logger.error("replaceCmcI error:", e);
            operationResult = OperationLog.FAILURE;
            result = "error";
        }
        writeDataToAjax(result);
        return NONE;
    }

    /**
     * @return the cmcId
     */
    public Long getCmcId() {
        return cmcId;
    }

    /**
     * @param cmcId
     *            the cmcId to set
     */
    public void setCmcId(Long cmcId) {
        this.cmcId = cmcId;
    }

    /**
     * @return the cmcMac
     */
    public String getCmcMac() {
        return cmcMac;
    }

    /**
     * @param cmcMac
     *            the cmcMac to set
     */
    public void setCmcMac(String cmcMac) {
        this.cmcMac = cmcMac;
    }

    /**
     * @return the cmcIndex
     */
    public Long getCmcIndex() {
        return cmcIndex;
    }

    /**
     * @param cmcIndex
     *            the cmcIndex to set
     */
    public void setCmcIndex(Long cmcIndex) {
        this.cmcIndex = cmcIndex;
    }

    /**
     * @return the cmcAttribute
     */
    public CmcAttribute getCmcAttribute() {
        return cmcAttribute;
    }

    /**
     * @param cmcAttribute
     *            the cmcAttribute to set
     */
    public void setCmcAttribute(CmcAttribute cmcAttribute) {
        this.cmcAttribute = cmcAttribute;
    }

    /**
     * @return the cmcService
     */
    public CmcService getCmcService() {
        return cmcService;
    }

    /**
     * @param cmcService
     *            the cmcService to set
     */
    public void setCmcService(CmcService cmcService) {
        this.cmcService = cmcService;
    }

    /**
     * @return the entityId
     */
    public Long getEntityId() {
        return entityId;
    }

    /**
     * @param entityId
     *            the entityId to set
     */
    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    /**
     * @return the cmcReplaceService
     */
    public CmcReplaceService getCmcReplaceService() {
        return cmcReplaceService;
    }

    /**
     * @param cmcReplaceService
     *            the cmcReplaceService to set
     */
    public void setCmcReplaceService(CmcReplaceService cmcReplaceService) {
        this.cmcReplaceService = cmcReplaceService;
    }

    /**
     * @return the entityService
     */
    public EntityService getEntityService() {
        return entityService;
    }

    /**
     * @param entityService
     *            the entityService to set
     */
    public void setEntityService(EntityService entityService) {
        this.entityService = entityService;
    }

    /**
     * @return the forceReplace
     */
    public Integer getForceReplace() {
        return forceReplace;
    }

    /**
     * @param forceReplace
     *            the forceReplace to set
     */
    public void setForceReplace(Integer forceReplace) {
        this.forceReplace = forceReplace;
    }

    /**
     * @return the entity
     */
    public Entity getEntity() {
        return entity;
    }

    /**
     * @param entity
     *            the entity to set
     */
    public void setEntity(Entity entity) {
        this.entity = entity;
    }

    /**
     * @return the onuMacList
     */
    public Map<String, Map<String, Object>> getOnuMacList() {
        return onuMacList;
    }

    /**
     * @param onuMacList
     *            the onuMacList to set
     */
    public void setOnuMacList(Map<String, Map<String, Object>> onuMacList) {
        this.onuMacList = onuMacList;
    }

    /**
     * @return the onuMacsJson
     */
    public JSONObject getOnuMacsJson() {
        return onuMacsJson;
    }

    /**
     * @param onuMacsJson
     *            the onuMacsJson to set
     */
    public void setOnuMacsJson(JSONObject onuMacsJson) {
        this.onuMacsJson = onuMacsJson;
    }

    /**
     * @return the replaceCmcId
     */
    public Long getReplaceCmcId() {
        return replaceCmcId;
    }

    /**
     * @param replaceCmcId
     *            the replaceCmcId to set
     */
    public void setReplaceCmcId(Long replaceCmcId) {
        this.replaceCmcId = replaceCmcId;
    }

    /**
     * @return the configFile
     */
    public String getConfigFile() {
        return configFile;
    }

    /**
     * @param configFile
     *            the configFile to set
     */
    public void setConfigFile(String configFile) {
        this.configFile = configFile;
    }
 
    /**
     * @return the operationResult
     */
    public Integer getOperationResult() {
        return operationResult;
    }

    /**
     * @param operationResult the operationResult to set
     */
    public void setOperationResult(Integer operationResult) {
        this.operationResult = operationResult;
    }
}
