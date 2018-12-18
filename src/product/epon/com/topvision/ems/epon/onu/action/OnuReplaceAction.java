/***********************************************************************
 * $Id: OnuReplaceAction.java,v1.0 2016-4-18 上午10:08:07 $
 * 
 * @author: Rod John
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.onu.action;

import java.util.Map;

import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.topvision.ems.epon.exception.OnuForceReplaceException;
import com.topvision.ems.epon.onu.domain.OltOnuAttribute;
import com.topvision.ems.epon.onu.service.OnuReplaceService;
import com.topvision.ems.epon.onu.service.OnuService;
import com.topvision.ems.epon.onuauth.domain.OltAuthentication;
import com.topvision.ems.epon.onuauth.service.OnuAuthService;
import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.network.service.EntityService;
import com.topvision.framework.annotation.OperationLogProperty;
import com.topvision.framework.common.MacUtils;
import com.topvision.framework.exception.engine.SnmpSetException;
import com.topvision.framework.utils.EponIndex;
import com.topvision.framework.web.struts2.BaseAction;
import com.topvision.platform.domain.OperationLog;
import com.topvision.platform.domain.UserContext;

/**
 * @author Rod John
 * @created @2016-4-18-上午10:08:07
 *
 */
@Controller("onuReplaceAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class OnuReplaceAction extends BaseAction {
    private static final long serialVersionUID = 4855915835936903733L;
    private final Logger logger = LoggerFactory.getLogger(OnuReplaceAction.class);
    private Long entityId;
    private Long onuId;
    private Long onuIndex;
    private String onuIndexString;
    private String onuMac;
    private String sn;
    private String pwd;
    private Entity entity;
    private OltOnuAttribute oltOnuAttribute;
    private OltAuthentication oltAuthentication;
    private Integer ponAuthType;
    @Autowired
    private OnuService onuService;
    @Autowired
    private OnuReplaceService onuReplaceService;
    @Autowired
    private OnuAuthService onuAuthService;
    @Autowired
    private EntityService entityService;
    private Map<String, Map<String, Object>> onuMacList;
    private Map<String, Map<String, Object>> onuSnList;
    private JSONObject onuMacsJson;
    private JSONObject onuSnJson;
    private Integer forceReplace;
    private Integer operationResult;
    private String pageId;

    /**
     * ONU替换页面信息
     * 
     * @return
     */
    public String showOnuReplaceView() {
        UserContext uc = (UserContext) super.getSession().get(UserContext.KEY);
        String macRule = uc.getMacDisplayStyle();
        entity = entityService.getEntity(onuId);
        entity.setMac(MacUtils.convertMacToDisplayFormat(entity.getMac(), macRule));
        oltOnuAttribute = onuService.getOnuAttribute(onuId);
        entityId = oltOnuAttribute.getEntityId();
        onuIndex = oltOnuAttribute.getOnuIndex();
        onuIndexString = EponIndex.getOnuStringByIndex(onuIndex).toString();
        onuMacList = onuReplaceService.getOnuMacListByEntityId(entityId);
        onuMacsJson = JSONObject.fromObject(onuMacList);
        onuSnList = onuReplaceService.getOnuSnListByEntityId(entityId);
        onuSnJson = JSONObject.fromObject(onuSnList);
        oltAuthentication = onuAuthService.getOltAuthenticationByIndex(entityId, onuIndex);
        ponAuthType = onuReplaceService.getPonAuthType(entityId, onuIndex);
        return SUCCESS;
    }

    /**
     * Replace ONU
     * 
     * @return
     * @throws Exception
     * 
     */
    @OperationLogProperty(actionName = "replaceOnuEntityByMac", operationName = "replaceOnuEntityByMac")
    public String replaceOnuEntityByMac() throws Exception {
        String result = null;
        try {
            onuReplaceService.replaceOnuEntityByMac(entityId, onuId, onuIndex, onuMac, forceReplace);
            operationResult = OperationLog.SUCCESS;
            result = "success";
        } catch (OnuForceReplaceException e) {
            logger.error("replaceOnu error:", e);
            operationResult = OperationLog.FAILURE;
            result = "onuforcereplaceerror";
        } catch (SnmpSetException e) {
            logger.error("replaceOnu error:", e);
            operationResult = OperationLog.FAILURE;
            result = "seterror";
        } catch (Exception e) {
            logger.error("replaceOnu error:", e);
            operationResult = OperationLog.FAILURE;
            result = "error";
        }
        writeDataToAjax(result);
        return NONE;
    }

    /**
     * Replace ONU
     * 
     * @return
     * @throws Exception
     */
    @OperationLogProperty(actionName = "replaceOnuEntityBySnAndPwd", operationName = "replaceOnuEntityBySnAndPwd")
    public String replaceOnuEntityBySnAndPwd() throws Exception {
        String result = null;
        try {
            onuReplaceService.replaceOnuEntityBySn(entityId, onuId, onuIndex, sn, pwd, forceReplace);
            operationResult = OperationLog.SUCCESS;
            result = "success";
        } catch (OnuForceReplaceException e) {
            logger.error("replaceOnu error:", e);
            operationResult = OperationLog.FAILURE;
            result = "onuforcereplaceerror";
        } catch (SnmpSetException e) {
            logger.error("replaceOnu error:", e);
            operationResult = OperationLog.FAILURE;
            result = "seterror";
        } catch (Exception e) {
            logger.error("replaceOnu error:", e);
            operationResult = OperationLog.FAILURE;
            result = "error";
        }
        writeDataToAjax(result);
        return NONE;
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
     * @return the onuId
     */
    public Long getOnuId() {
        return onuId;
    }

    /**
     * @param onuId
     *            the onuId to set
     */
    public void setOnuId(Long onuId) {
        this.onuId = onuId;
    }

    /**
     * @return the onuIndex
     */
    public Long getOnuIndex() {
        return onuIndex;
    }

    /**
     * @param onuIndex
     *            the onuIndex to set
     */
    public void setOnuIndex(Long onuIndex) {
        this.onuIndex = onuIndex;
    }

    /**
     * @return the onuMac
     */
    public String getOnuMac() {
        return onuMac;
    }

    /**
     * @param onuMac
     *            the onuMac to set
     */
    public void setOnuMac(String onuMac) {
        this.onuMac = onuMac;
    }

    /**
     * @return the oltOnuAttribute
     */
    public OltOnuAttribute getOltOnuAttribute() {
        return oltOnuAttribute;
    }

    /**
     * @param oltOnuAttribute
     *            the oltOnuAttribute to set
     */
    public void setOltOnuAttribute(OltOnuAttribute oltOnuAttribute) {
        this.oltOnuAttribute = oltOnuAttribute;
    }

    /**
     * @return the oltAuthentication
     */
    public OltAuthentication getOltAuthentication() {
        return oltAuthentication;
    }

    /**
     * @param oltAuthentication
     *            the oltAuthentication to set
     */
    public void setOltAuthentication(OltAuthentication oltAuthentication) {
        this.oltAuthentication = oltAuthentication;
    }

    /**
     * @return the onuService
     */
    public OnuService getOnuService() {
        return onuService;
    }

    /**
     * @param onuService
     *            the onuService to set
     */
    public void setOnuService(OnuService onuService) {
        this.onuService = onuService;
    }

    /**
     * @return the onuReplaceService
     */
    public OnuReplaceService getOnuReplaceService() {
        return onuReplaceService;
    }

    /**
     * @param onuReplaceService
     *            the onuReplaceService to set
     */
    public void setOnuReplaceService(OnuReplaceService onuReplaceService) {
        this.onuReplaceService = onuReplaceService;
    }

    /**
     * @return the onuAuthService
     */
    public OnuAuthService getOnuAuthService() {
        return onuAuthService;
    }

    /**
     * @param onuAuthService
     *            the onuAuthService to set
     */
    public void setOnuAuthService(OnuAuthService onuAuthService) {
        this.onuAuthService = onuAuthService;
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
     * @return the onuSnList
     */
    public Map<String, Map<String, Object>> getOnuSnList() {
        return onuSnList;
    }

    /**
     * @param onuSnList
     *            the onuSnList to set
     */
    public void setOnuSnList(Map<String, Map<String, Object>> onuSnList) {
        this.onuSnList = onuSnList;
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
     * @return the onuIndexString
     */
    public String getOnuIndexString() {
        return onuIndexString;
    }

    /**
     * @param onuIndexString
     *            the onuIndexString to set
     */
    public void setOnuIndexString(String onuIndexString) {
        this.onuIndexString = onuIndexString;
    }

    /**
     * @return the sn
     */
    public String getSn() {
        return sn;
    }

    /**
     * @param sn
     *            the sn to set
     */
    public void setSn(String sn) {
        this.sn = sn;
    }

    /**
     * @return the pwd
     */
    public String getPwd() {
        return pwd;
    }

    /**
     * @param pwd
     *            the pwd to set
     */
    public void setPwd(String pwd) {
        this.pwd = pwd;
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
     * @return the ponAuthType
     */
    public Integer getPonAuthType() {
        return ponAuthType;
    }

    /**
     * @param ponAuthType
     *            the ponAuthType to set
     */
    public void setPonAuthType(Integer ponAuthType) {
        this.ponAuthType = ponAuthType;
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
     * @return the onuSnJson
     */
    public JSONObject getOnuSnJson() {
        return onuSnJson;
    }

    /**
     * @param onuSnJson
     *            the onuSnJson to set
     */
    public void setOnuSnJson(JSONObject onuSnJson) {
        this.onuSnJson = onuSnJson;
    }

    /**
     * @return the operationResult
     */
    public Integer getOperationResult() {
        return operationResult;
    }

    /**
     * @param operationResult
     *            the operationResult to set
     */
    public void setOperationResult(Integer operationResult) {
        this.operationResult = operationResult;
    }

    public String getPageId() {
        return pageId;
    }

    public void setPageId(String pageId) {
        this.pageId = pageId;
    }

}
