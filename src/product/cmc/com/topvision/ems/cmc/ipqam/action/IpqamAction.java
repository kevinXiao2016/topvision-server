/***********************************************************************
 * $Id: EqamAction.java,v1.0 2016年5月3日 上午10:18:27 $
 * 
 * @author: loyal
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.ipqam.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.topvision.ems.cmc.ccmts.facade.domain.CmcAttribute;
import com.topvision.ems.cmc.ccmts.service.CmcService;
import com.topvision.ems.cmc.ipqam.domain.Ipqam;
import com.topvision.ems.cmc.ipqam.domain.Program;
import com.topvision.ems.cmc.ipqam.service.IpqamRefreshService;
import com.topvision.ems.cmc.ipqam.service.IpqamService;
import com.topvision.ems.devicesupport.version.service.DeviceVersionService;
import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.network.service.EntityService;
import com.topvision.ems.template.service.EntityTypeService;
import com.topvision.framework.common.IpUtils;
import com.topvision.framework.utils.CmcIndexUtils;
import com.topvision.framework.web.struts2.BaseAction;

import net.sf.json.JSONObject;

/**
 * @author loyal
 * @created @2016年5月3日-上午10:18:27
 * 
 */
@Controller("ipqamAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class IpqamAction extends BaseAction {
    private static final long serialVersionUID = -7116423288020122061L;
    @Autowired
    private CmcService cmcService;
    @Autowired
    private IpqamService ipqamService;
    @Autowired
    private IpqamRefreshService ipqamRefreshService;
    @Autowired
    private EntityService entityService;
    @Autowired
    private EntityTypeService entityTypeService;
    @Resource(name = "deviceVersionService")
    private DeviceVersionService deviceVersionService;
    private Long entityId;
    private Long cmcId;
    private Integer productType;
    private CmcAttribute cmcAttribute;
    private String queryContent;
    private Entity entity;

    /**
     * 显示EQAM页面
     * 
     * @return
     */
    public String showEqamPage() {
        cmcAttribute = cmcService.getCmcAttributeByCmcId(cmcId);
        entityId = cmcService.getEntityIdByCmcId(cmcId);
        return SUCCESS;
    }

    public String showOltProgramPage() {
        entity=entityService.getEntity(entityId);
        return SUCCESS;
    }

    /**
     * 获取EQAM列表信息
     * 
     * @return
     */
    public String getEqamInfo() {
        Map<String, Object> json = new HashMap<String, Object>();
        List<Ipqam> eqamList = ipqamService.getEqamList(cmcId);
        for (Ipqam ipqam : eqamList) {
            ipqam.setChannelId(CmcIndexUtils.getChannelId(ipqam.getIfIndex()));
        }
        json.put("data", eqamList);
        writeDataToAjax(JSONObject.fromObject(json));
        return NONE;
    }

    public String getOltProgramInfo() {
        // mysql中下划线是特殊的，like的时候必须转义
        if (queryContent != null && queryContent.contains("_")) {
            queryContent = queryContent.replace("_", "\\_");
        }
        
        Map<String, Object> queryMap = new HashMap<String, Object>();
        queryMap.put("start", start);
        queryMap.put("limit", limit);
        queryMap.put("entityId", entityId);
        queryMap.put("queryContent", queryContent);
        queryMap.put("sort", sort);
        queryMap.put("dir", dir);
        List<Program> programList = ipqamService.getOltEqamList(queryMap);
        for (Program program : programList) {
            if (program.getIfIndex() != null) {
                program.setChannelId(CmcIndexUtils.getChannelId(program.getIfIndex()));
            }
        }
        Long programNum = ipqamService.getOltProgramListCount(queryMap);
        
        
        JSONObject json = new JSONObject();
        json.put("data", programList);
        json.put("rowCount", programNum);
        writeDataToAjax(json);
        return NONE;
    }
    
    public String loadEqamSupportUnderOlt() {
        boolean support = ipqamService.loadEqamSupportUnderOlt(entityId);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("support", support);
        writeDataToAjax(jsonObject);
        return NONE;
    }
    
    public String loadEqamSupport() {
        boolean support = ipqamService.loadEqamSupport(entityId);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("support", support);
        writeDataToAjax(jsonObject);
        return NONE;
    }

    /**
     * 显示节目流页面
     * 
     * @return
     */
    public String showProgramPage() {
        cmcAttribute = cmcService.getCmcAttributeByCmcId(cmcId);
        entityId = cmcService.getEntityIdByCmcId(cmcId);
        return SUCCESS;
    }

    /**
     * 获取节目流信息列表
     * 
     * @return
     */
    public String getProgramInfo() {
        Map<String, Object> json = new HashMap<String, Object>();
        Map<String, Object> queryMap = new HashMap<String, Object>();
        queryMap.put("start", start);
        queryMap.put("limit", limit);
        queryMap.put("cmcId", cmcId);
        queryMap.put("sort", sort);
        queryMap.put("dir", dir);
        List<Program> programList = ipqamService.getProgramList(queryMap);
        for (Program program : programList) {
            if (program.getIfIndex() != null) {
                program.setChannelId(CmcIndexUtils.getChannelId(program.getIfIndex()));
            }
        }
        Long programNum = ipqamService.getProgramListCount(cmcId);
        json.put("data", programList);
        json.put("rowCount", programNum);
        writeDataToAjax(JSONObject.fromObject(json));
        return NONE;
    }

    /**
     * 刷新节目流信息
     * 
     * @return
     */
    public String refreshProgramInfo() {
        ipqamRefreshService.refreshProgramInfo(cmcId);
        return NONE;
    }
    
    /**
     * 刷新OLT下CCMTS节目流信息
     * 
     * @return
     */
    public String refreshOltProgramInfo() {
        ipqamRefreshService.refreshOltProgramInfo(entityId);
        return NONE;
    }

    /**
     * 刷新EQAM信息
     * 
     * @return
     */
    public String refreshEqamInfo() {
        ipqamRefreshService.refreshEqamInfo(cmcId);
        return NONE;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public Long getCmcId() {
        return cmcId;
    }

    public void setCmcId(Long cmcId) {
        this.cmcId = cmcId;
    }

    public Integer getProductType() {
        return productType;
    }

    public void setProductType(Integer productType) {
        this.productType = productType;
    }

    public CmcAttribute getCmcAttribute() {
        return cmcAttribute;
    }

    public void setCmcAttribute(CmcAttribute cmcAttribute) {
        this.cmcAttribute = cmcAttribute;
    }

    public String getQueryContent() {
        return queryContent;
    }

    public void setQueryContent(String queryContent) {
        this.queryContent = queryContent;
    }

    public Entity getEntity() {
        return entity;
    }

    public void setEntity(Entity entity) {
        this.entity = entity;
    }

}
