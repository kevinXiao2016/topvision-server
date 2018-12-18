/***********************************************************************
 * $Id: Cmc_bRouteAction.java,v1.0 2013-8-6 下午4:40:27 $
 * 
 * @author: dosion
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.ccmts.action;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import com.topvision.framework.utils.CmdDiskSpace;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.topvision.ems.cmc.ccmts.domain.Cmc;
import com.topvision.ems.cmc.ccmts.domain.EntityPonRelation;
import com.topvision.ems.cmc.ccmts.facade.domain.CmcAttribute;
import com.topvision.ems.cmc.ccmts.service.CmcListService;
import com.topvision.ems.cmc.ccmts.service.CmcService;
import com.topvision.ems.cmc.util.CmcConstants;
import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.facade.domain.EntityType;
import com.topvision.ems.network.service.EntityService;
import com.topvision.ems.template.service.EntityTypeService;
import com.topvision.framework.common.DateUtils;
import com.topvision.framework.common.MacUtils;
import com.topvision.framework.snmp.SnmpParam;
import com.topvision.framework.web.struts2.BaseAction;
import com.topvision.platform.ResourceManager;
import com.topvision.platform.domain.UserContext;
import com.topvision.platform.util.CurrentRequest;

/**
 * @author dosion
 * @created @2013-8-6-下午4:40:27
 * 
 */
@Controller("cmcListAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class CmcListAction extends BaseAction {
    private static final long serialVersionUID = 4259869100676362331L;
    private final Logger logger = LoggerFactory.getLogger(CmcListAction.class);
    @Autowired
    private CmcService cmcService;
    @Autowired
    private CmcListService cmcListService;
    @Autowired
    private EntityService entityService;
    @Resource(name = "entityTypeService")
    private EntityTypeService entityTypeService;

    private CmcAttribute cmcAttribute;
    private Entity entity;
    private Long deviceType;
    private Long entityId;
    private Long typeId;
    private Long ponId;
    private String cmcMac;
    private Long cmcId;
    private Long cmcPortId;
    private Long channelIndex;
    private String cmMac;
    private Long onuId;
    private Long uniIdMain;
    private Long uniIdBack;
    private Long vnId;
    private SnmpParam snmpParam;
    private Integer operationResult;
    private int start;
    private int limit;
    private JSONArray entityPonRelationObject = new JSONArray();
    private JSONArray oltSelectObject = new JSONArray();
    private Integer productType;
    private Integer cmcType;
    private String cmcName;
    private String cmcIp;// 8800B
    private String connectPerson;// 关联责任人
    private Date statDate;
    private boolean hasSupportEpon;
    private Map<Long, String> cmcNotRelatedOnu;
    private JSONArray cmcList;
    private JSONArray cmcTypes = new JSONArray();
    private Long topCcmtsSysStatus;
    private Integer mddInterval;

    /**
     * 显示cmc列表页面
     * 
     * @return String
     */
    public String showAllCmcList() {
        UserContext uc = (UserContext) super.getSession().get(UserContext.KEY);
        hasSupportEpon = uc.hasSupportModule("olt");
        if (hasSupportEpon) {
            List<EntityPonRelation> entityPonRelationList = cmcListService.getEntityPonInfoList();
            Long cmcType = entityTypeService.getCcmtsandcmtsType();
            List<EntityType> entityType = entityTypeService.loadSubType(cmcType);
            cmcTypes = JSONArray.fromObject(entityType);
            Set<EntityPonRelation> set = new HashSet<EntityPonRelation>();
            for (EntityPonRelation anEntityPonRelationList : entityPonRelationList) {
                set.add(anEntityPonRelationList);
            }
            List<EntityPonRelation> selectList = new ArrayList<EntityPonRelation>();
            for (EntityPonRelation aSet : set) {
                selectList.add(aSet);
            }
            entityPonRelationObject = JSONArray.fromObject(entityPonRelationList);
            oltSelectObject = JSONArray.fromObject(selectList); // 去掉重复的entityId
        } else {
            Long ccWithAgentType = entityTypeService.getCcmtswithagentType();
            Long cmtsType = entityTypeService.getCmtsType();
            List<EntityType> ccWithAgentEntityType = entityTypeService.loadSubType(ccWithAgentType);
            List<EntityType> cmtsEntityType = entityTypeService.loadSubType(cmtsType);
            ccWithAgentEntityType.addAll(cmtsEntityType);
            cmcTypes = JSONArray.fromObject(ccWithAgentEntityType);
        }
        return SUCCESS;
    }

    /**
     * 查询cmc列表
     * 
     * @return String
     */
    public String queryCmcList() {
        Map<String, Object> cmcQueryMap = new HashMap<String, Object>();
        Map<String, Object> json = new HashMap<String, Object>();
        List<CmcAttribute> cmcAttributeList = new ArrayList<CmcAttribute>();
        UserContext uc = (UserContext) super.getSession().get(UserContext.KEY);
        String macRule = uc.getMacDisplayStyle();
        hasSupportEpon = uc.hasSupportModule("olt");
        Long cmcNum = 0l;
        // 杨毅修改,20140903,加入后台排序,上线时间
        // 杨毅修改,20150112,加入后台排序,上线时间,加'-'String按照Long排序，status有多列，加入tmp.
        if (sort != null && sort.equals("topCcmtsSysUpTimeString")) {
            sort = "-sysupTimeSortData";
        }
        if (sort != null && sort.equals("interfaceInfo")) {
            sort = "cmcIndex";
        }
        if (sort != null && sort.equals("topCcmtsSysStatus")) {
            sort = "status";
        }
        cmcQueryMap.put("sort", sort);
        cmcQueryMap.put("dir", dir);
        if (cmcName != null && !"".equals(cmcName.trim())) {
            // mysql中下划线是特殊的，like的时候必须转义
            if (cmcName.contains("_")) {
                cmcName = cmcName.replace("_", "\\_");
            }
            cmcQueryMap.put("cmcName", cmcName.trim());
            String formatQueryMac = MacUtils.formatQueryMac(cmcName);
            if (formatQueryMac.indexOf(":") == -1) {
                cmcQueryMap.put("queryMacWithoutSplit", formatQueryMac);
            }
            cmcQueryMap.put("queryContentMac", formatQueryMac);
        }
        if (topCcmtsSysStatus != null && !topCcmtsSysStatus.equals(-1L)) {
            cmcQueryMap.put("status", topCcmtsSysStatus);
        }
        cmcQueryMap.put("start", start);
        cmcQueryMap.put("limit", limit);
        // cmcQueryMap.put("sort", sort);
        // cmcQueryMap.put("dir", dir);
        if (cmcType != null && cmcType != 0) {
            cmcQueryMap.put("cmcType", cmcType.toString());
        }
        if (deviceType != null && deviceType > 0) {
            cmcQueryMap.put("deviceType", deviceType);
        }
        if (entityId != null && entityId > 0) {
            cmcQueryMap.put("entityId", entityId.toString());
        }
        if (ponId != null && ponId > 0) {
            cmcQueryMap.put("ponId", ponId.toString());
        }
        if (mddInterval != null && mddInterval != -1) {
            cmcQueryMap.put("mddInterval", mddInterval.toString());
        }

        cmcQueryMap.put("userId", uc.getUserId());
        if (logger.isDebugEnabled()) {
            logger.debug("begin query cmclist at: " + System.currentTimeMillis());
        }
        cmcAttributeList = cmcListService.queryCmcList(cmcQueryMap);
        cmcNum = cmcListService.getCmcNum(cmcQueryMap);
        if (logger.isDebugEnabled()) {
            logger.debug("after query cmclist at: " + System.currentTimeMillis());
        }
        JSONArray jsonArray = new JSONArray();
        for (CmcAttribute cmcAttribute : cmcAttributeList) {
            if (cmcAttribute.getMddInterval() == null) {
                cmcAttribute.setMddInterval(-1);
            }
            if (cmcAttribute.getTopCcmtsSysUpTime() == null) {
                cmcAttribute.setTopCcmtsSysUpTime(0L);
            } else {
                Long tempTime = System.currentTimeMillis();
                cmcAttribute.setTopCcmtsSysUpTime(
                        (cmcAttribute.getTopCcmtsSysUpTime() / 100 + (tempTime - cmcAttribute.getDt().getTime()) / 1000)
                                * 100);
            }
            Timestamp statusChangeTime = cmcAttribute.getStatusChangeTime();
            if (statusChangeTime != null) {
                String statusChangeTimeStr = DateUtils.getTimeDesInObscure(
                        System.currentTimeMillis() - statusChangeTime.getTime(), uc.getUser().getLanguage());
                cmcAttribute.setStatusChangeTimeStr(statusChangeTimeStr);
            }
            // add by fanzidong, 展示之前需要格式化MAC地址
            String mac = null;
            if (entityTypeService.isCmts(cmcAttribute.getTypeId())) {
                cmcService.analyseSofewareVersion(cmcAttribute);
            } else { // cmts设备的mac不展示
                mac = MacUtils.convertMacToDisplayFormat(cmcAttribute.getTopCcmtsSysMacAddr(), macRule);
            }
            JSONObject jsonO = JSONObject.fromObject(cmcAttribute);
            jsonO.put("topCcmtsSysMacAddr", mac);
            jsonArray.add(jsonO);
        }
        if (logger.isDebugEnabled()) {
            logger.debug("after package data at: " + System.currentTimeMillis());
        }
        json.put("data", jsonArray);
        json.put("rowCount", cmcNum);
        writeDataToAjax(JSONObject.fromObject(json));
        return NONE;
    }

    /**
     * 获取当前olt设备上的所有CC的nmName
     * 
     * @throws IOException
     */
    public String seachCMCNameList() throws IOException {
        Map<Object, Object> cmcNameMap = new HashMap<Object, Object>();
        cmcNameMap = cmcListService.getAllOnuIdToCmcNmnameMap(entityId);
        JSONObject jso = new JSONObject();
        jso = JSONObject.fromObject(cmcNameMap);
        writeDataToAjax(jso);
        return NONE;
    }

    /**
     * 通过onuId获取Cmc列表
     * 
     * @return String
     */
    public String queryCmcFromOnu() {
        ResourceManager resourceManager = ResourceManager.getResourceManager("com.topvision.ems.resources.resources");
        UserContext uc = CurrentRequest.getCurrentUser();
        List<Cmc> cmcs = cmcService.getCmcList(onuId);
        Long tempTime = System.currentTimeMillis();
        for (Cmc cmc : cmcs) {
            if (CmcConstants.TOPCCMTSSYSSTATUS_OFFLINE.equals(cmc.getTopCcmtsSysStatus())) {
                cmc.setTopCcmtsSysUpTime(0L);
                String cmcRunTime = resourceManager.getNotNullString("deviceInfo.offline");
                if (cmc.getLastDeregisterTime() != null) {
                    String cmcOffline = "(" + DateUtils.getTimeDesInObscure(
                            System.currentTimeMillis() - cmc.getLastDeregisterTime().getTime(),
                            uc.getUser().getLanguage()) + ")";
                    cmcRunTime = cmcRunTime + cmcOffline;
                }
                cmc.setCmcRunTime(cmcRunTime);
            } else {
                Long dt = cmc.getDt().getTime();
                if (cmc.getTopCcmtsSysUpTime() == null) {
                    cmc.setTopCcmtsSysUpTime(0L);
                } else {
                    cmc.setTopCcmtsSysUpTime((cmc.getTopCcmtsSysUpTime() * 10 + (tempTime - dt)));
                }
            }
        }
        Map<String, List<Cmc>> re = new HashMap<String, List<Cmc>>();
        re.put("cmcList", cmcs);
        writeDataToAjax(JSONObject.fromObject(re));
        return NONE;
    }

    /**
     * 跳转到关联CMC页面 未发现使用该Action页面，重构时增加废弃标识，2013-10-28
     * 
     * @return String
     */
    @Deprecated
    public String relateCmc() {
        cmcNotRelatedOnu = cmcService.getCmcNotRelated();
        cmcList = JSONArray.fromObject(cmcNotRelatedOnu);
        return SUCCESS;
    }

    /**
     * 跳转到CMC创建页面 未发现页面使用该项，重构时增加废弃标识，2013-10-28
     * 
     * @return String
     */
    @Deprecated
    public String createCmc() {
        return SUCCESS;
    }

    public CmcListService getCmcListService() {
        return cmcListService;
    }

    public void setCmcListService(CmcListService cmcListService) {
        this.cmcListService = cmcListService;
    }

    public EntityService getEntityService() {
        return entityService;
    }

    public void setEntityService(EntityService entityService) {
        this.entityService = entityService;
    }

    public CmcAttribute getCmcAttribute() {
        return cmcAttribute;
    }

    public void setCmcAttribute(CmcAttribute cmcAttribute) {
        this.cmcAttribute = cmcAttribute;
    }

    public Entity getEntity() {
        return entity;
    }

    public void setEntity(Entity entity) {
        this.entity = entity;
    }

    public Long getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(Long deviceType) {
        this.deviceType = deviceType;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public Long getTypeId() {
        return typeId;
    }

    public void setTypeId(Long typeId) {
        this.typeId = typeId;
    }

    public Long getPonId() {
        return ponId;
    }

    public void setPonId(Long ponId) {
        this.ponId = ponId;
    }

    public String getCmcMac() {
        return cmcMac;
    }

    public void setCmcMac(String cmcMac) {
        this.cmcMac = cmcMac;
    }

    public Long getCmcId() {
        return cmcId;
    }

    public void setCmcId(Long cmcId) {
        this.cmcId = cmcId;
    }

    public Long getCmcPortId() {
        return cmcPortId;
    }

    public void setCmcPortId(Long cmcPortId) {
        this.cmcPortId = cmcPortId;
    }

    public Long getChannelIndex() {
        return channelIndex;
    }

    public void setChannelIndex(Long channelIndex) {
        this.channelIndex = channelIndex;
    }

    public String getCmMac() {
        return cmMac;
    }

    public void setCmMac(String cmMac) {
        this.cmMac = cmMac;
    }

    public Long getOnuId() {
        return onuId;
    }

    public void setOnuId(Long onuId) {
        this.onuId = onuId;
    }

    public Long getUniIdMain() {
        return uniIdMain;
    }

    public void setUniIdMain(Long uniIdMain) {
        this.uniIdMain = uniIdMain;
    }

    public Long getUniIdBack() {
        return uniIdBack;
    }

    public void setUniIdBack(Long uniIdBack) {
        this.uniIdBack = uniIdBack;
    }

    public Long getVnId() {
        return vnId;
    }

    public void setVnId(Long vnId) {
        this.vnId = vnId;
    }

    public SnmpParam getSnmpParam() {
        return snmpParam;
    }

    public void setSnmpParam(SnmpParam snmpParam) {
        this.snmpParam = snmpParam;
    }

    public Integer getOperationResult() {
        return operationResult;
    }

    public void setOperationResult(Integer operationResult) {
        this.operationResult = operationResult;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public JSONArray getEntityPonRelationObject() {
        return entityPonRelationObject;
    }

    public void setEntityPonRelationObject(JSONArray entityPonRelationObject) {
        this.entityPonRelationObject = entityPonRelationObject;
    }

    public JSONArray getOltSelectObject() {
        return oltSelectObject;
    }

    public void setOltSelectObject(JSONArray oltSelectObject) {
        this.oltSelectObject = oltSelectObject;
    }

    public Integer getProductType() {
        return productType;
    }

    public void setProductType(Integer productType) {
        this.productType = productType;
    }

    public Integer getCmcType() {
        return cmcType;
    }

    public void setCmcType(Integer cmcType) {
        this.cmcType = cmcType;
    }

    public String getCmcName() {
        return cmcName;
    }

    public void setCmcName(String cmcName) {
        this.cmcName = cmcName;
    }

    public String getCmcIp() {
        return cmcIp;
    }

    public void setCmcIp(String cmcIp) {
        this.cmcIp = cmcIp;
    }

    public String getConnectPerson() {
        return connectPerson;
    }

    public void setConnectPerson(String connectPerson) {
        this.connectPerson = connectPerson;
    }

    public Date getStatDate() {
        return statDate;
    }

    public void setStatDate(Date statDate) {
        this.statDate = statDate;
    }

    public boolean isHasSupportEpon() {
        return hasSupportEpon;
    }

    public void setHasSupportEpon(boolean hasSupportEpon) {
        this.hasSupportEpon = hasSupportEpon;
    }

    public Logger getLogger() {
        return logger;
    }

    public CmcService getCmcService() {
        return cmcService;
    }

    public void setCmcService(CmcService cmcService) {
        this.cmcService = cmcService;
    }

    public Map<Long, String> getCmcNotRelatedOnu() {
        return cmcNotRelatedOnu;
    }

    public void setCmcNotRelatedOnu(Map<Long, String> cmcNotRelatedOnu) {
        this.cmcNotRelatedOnu = cmcNotRelatedOnu;
    }

    public JSONArray getCmcList() {
        return cmcList;
    }

    public void setCmcList(JSONArray cmcList) {
        this.cmcList = cmcList;
    }

    public JSONArray getCmcTypes() {
        return cmcTypes;
    }

    public void setCmcTypes(JSONArray cmcTypes) {
        this.cmcTypes = cmcTypes;
    }

    public Long getTopCcmtsSysStatus() {
        return topCcmtsSysStatus;
    }

    public void setTopCcmtsSysStatus(Long topCcmtsSysStatus) {
        this.topCcmtsSysStatus = topCcmtsSysStatus;
    }

    /**
     * @return the mddInterval
     */
    public Integer getMddInterval() {
        return mddInterval;
    }

    /**
     * @param mddInterval
     *            the mddInterval to set
     */
    public void setMddInterval(Integer mddInterval) {
        this.mddInterval = mddInterval;
    }

}
