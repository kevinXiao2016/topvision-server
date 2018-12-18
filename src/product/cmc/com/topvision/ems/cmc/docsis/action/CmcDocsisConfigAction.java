/***********************************************************************
 * $Id: CmcDocsisConfigAction.java,v1.0 2013-4-26 下午7:24:52 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.docsis.action;

import java.io.IOException;

import javax.annotation.Resource;

import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.topvision.ems.cmc.ccmts.service.CmcService;
import com.topvision.ems.cmc.docsis.facade.domain.CmcDocsisConfig;
import com.topvision.ems.cmc.docsis.service.CmcDocsisConfigService;
import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.network.service.EntityService;
import com.topvision.framework.web.struts2.BaseAction;

/**
 * @author flack
 * @created @2013-4-26-下午7:24:52
 * 
 */
@Controller("cmcDocsisConfigAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class CmcDocsisConfigAction extends BaseAction {
    private static final long serialVersionUID = -837389965539972201L;
    private final Logger logger = LoggerFactory.getLogger(CmcDocsisConfigAction.class);

    private Long entityId;
    private Long ifIndex;
    private Integer cmcMDDTime = 1500;
    private Integer cmcMDFEnabled = 1;
    private CmcDocsisConfig cmcDocsis;
    private String cmcType;
    private Long cmcId;
    @Autowired
    private EntityService entityService;
    @Autowired
    private CmcService cmcService;
    @Resource(name = "cmcDocsisConfigService")
    private CmcDocsisConfigService cmcDocsisConfigService;

    /**
     * 显示Docsis 3.0 MDD/MDF配置
     * 
     * @return
     */
    public String loadDocsisConfig() {
        Long ifIndex = cmcService.getCmcAttributeByCmcId(cmcId).getCmcIndex();
        Entity entity = entityService.getEntity(cmcId);
        cmcDocsis = cmcDocsisConfigService.getCmcDocsis(entityId, ifIndex);
        if (cmcDocsis == null) {
            cmcDocsis = new CmcDocsisConfig();
            cmcDocsis.setCmcId(cmcId);
            cmcDocsis.setEntityId(entityId);
            cmcDocsis.setCmcType("" + entity.getTypeId());
            cmcDocsis.setIfIndex(ifIndex);
            return INPUT;
        } else {
            cmcDocsis.setCmcId(cmcId);
            cmcDocsis.setCmcType("" + entity.getTypeId());
        }
        return SUCCESS;
    }

    public String savaDocsisConfig() throws IOException {
        CmcDocsisConfig cmcDocsis = new CmcDocsisConfig();
        cmcDocsis.setEntityId(entityId);
        cmcDocsis.setCmcId(cmcId);
        cmcDocsis.setIfIndex(ifIndex);
        cmcDocsis.setCcmtsMddInterval(cmcMDDTime);
        cmcDocsis.setCcmtsMdfEnabled(cmcMDFEnabled);
        JSONObject json = new JSONObject();
        try {
            cmcDocsisConfigService.updateCmcDocsis(cmcDocsis);
            json.put("success", true);
        } catch (Exception e) {
            json.put("success", false);
            logger.error("Update Docsis 3.0 MDD/MDF config to facility failed:{}", e.getMessage());
        }
        writeDataToAjax(json);
        return NONE;
    }

    public String refreshCmcDocsisFromFacility() throws IOException {
        JSONObject json = new JSONObject();
        try {
            CmcDocsisConfig cmcDocsis = new CmcDocsisConfig();
            cmcDocsis.setEntityId(entityId);
            cmcDocsis.setCmcId(cmcId);
            cmcDocsis.setCmcType(cmcType);
            cmcDocsisConfigService.refreshCmcDocsisFromFacility(cmcDocsis);
            json.put("success", true);
        } catch (Exception e) {
            json.put("success", false);
            logger.error("Get Docsis 3.0 MDD/MDF config from facility failed:{}", e.getMessage());
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

    /**
     * @return the ifIndex
     */
    public Long getIfIndex() {
        return ifIndex;
    }

    /**
     * @param ifIndex
     *            the ifIndex to set
     */
    public void setIfIndex(Long ifIndex) {
        this.ifIndex = ifIndex;
    }

    public Integer getCmcMDDTime() {
        return cmcMDDTime;
    }

    public void setCmcMDDTime(Integer cmcMDDTime) {
        this.cmcMDDTime = cmcMDDTime;
    }

    public Integer getCmcMDFEnabled() {
        return cmcMDFEnabled;
    }

    public void setCmcMDFEnabled(Integer cmcMDFEnabled) {
        this.cmcMDFEnabled = cmcMDFEnabled;
    }

    public CmcDocsisConfigService getCmcDocsisConfigService() {
        return cmcDocsisConfigService;
    }

    public void setCmcDocsisConfigService(CmcDocsisConfigService cmcDocsisConfigService) {
        this.cmcDocsisConfigService = cmcDocsisConfigService;
    }

    public CmcDocsisConfig getCmcDocsis() {
        return cmcDocsis;
    }

    public void setCmcDocsis(CmcDocsisConfig cmcDocsis) {
        this.cmcDocsis = cmcDocsis;
    }

    public String getCmcType() {
        return cmcType;
    }

    public void setCmcType(String cmcType) {
        this.cmcType = cmcType;
    }

    public Long getCmcId() {
        return cmcId;
    }

    public void setCmcId(Long cmcId) {
        this.cmcId = cmcId;
    }

}
