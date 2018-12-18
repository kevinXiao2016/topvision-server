/***********************************************************************
 * $Id: CmtsNetworkInfoAction.java,v1.0 2017年8月1日 上午11:46:48 $
 * 
 * @author: ls
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.cmtsInfo.action;

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

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.processors.DefaultDefaultValueProcessor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.topvision.ems.cmc.ccmts.action.CmcListAction;
import com.topvision.ems.cmc.ccmts.domain.Cmc;
import com.topvision.ems.cmc.ccmts.domain.EntityPonRelation;
import com.topvision.ems.cmc.ccmts.facade.domain.CmcAttribute;
import com.topvision.ems.cmc.ccmts.service.CmcListService;
import com.topvision.ems.cmc.ccmts.service.CmcService;
import com.topvision.ems.cmc.cmtsInfo.domain.CmOutPowerNotInRange;
import com.topvision.ems.cmc.cmtsInfo.domain.CmRePowerNotInRange;
import com.topvision.ems.cmc.cmtsInfo.domain.CmtsInfoNotInRange;
import com.topvision.ems.cmc.cmtsInfo.domain.CmtsInfoThreshold;
import com.topvision.ems.cmc.cmtsInfo.domain.CmtsNetworkInfo;
import com.topvision.ems.cmc.cmtsInfo.domain.DownSnrAvgNotInRange;
import com.topvision.ems.cmc.cmtsInfo.domain.UpSnrNotInRange;
import com.topvision.ems.cmc.cmtsInfo.service.CmtsNetworkInfoService;
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
import com.topvision.platform.zetaframework.var.UnitConfigConstant;

/**
 * @author ls
 * @created @2017年8月1日-上午11:46:48
 *
 */
@Controller("cmtsNetworkInfoAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class CmtsNetworkInfoAction extends BaseAction {
    private static final long serialVersionUID = 1653722091765031352L;
    private final Logger logger = LoggerFactory.getLogger(CmtsNetworkInfoAction.class);
    @Autowired
    private CmcService cmcService;
    @Autowired
    private CmcListService cmcListService;
    @Autowired
    private EntityService entityService;
    @Autowired
    private CmtsNetworkInfoService cmtsNetworkInfoService;
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
    private CmtsInfoThreshold ccInfoThreshold;

    private Integer upSnrMin;
    private Integer upSnrMax;
    private Integer downSnrMin;
    private Integer downSnrMax;
    private Integer upPowerMin;
    private Integer upPowerMax;
    private Integer downPowerMin;
    private Integer downPowerMax;
    
    public String showCmtsInfoPage(){  
        UserContext uc = (UserContext) super.getSession().get(UserContext.KEY);
        hasSupportEpon = uc.hasSupportModule("olt");
        ccInfoThreshold = cmtsNetworkInfoService.getLocalThreshold();
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
        List<CmtsNetworkInfo>cmtsNetInfo=new ArrayList<CmtsNetworkInfo>();
        UserContext uc = (UserContext) super.getSession().get(UserContext.KEY);
        hasSupportEpon = uc.hasSupportModule("olt");
        Long cmcNum = 0l;

        if (upSnrMin != null) {
            cmcQueryMap.put("upSnrMin", upSnrMin);
        }
        if (upSnrMax != null) {
            cmcQueryMap.put("upSnrMax", upSnrMax);
        }
        if (downSnrMin != null) {
            cmcQueryMap.put("downSnrMin", downSnrMin);
        }
        if (downSnrMax != null) {
            cmcQueryMap.put("downSnrMax", downSnrMax);
        }
        if (upPowerMin != null) {
            cmcQueryMap.put("upPowerMin", UnitConfigConstant.transPowerToDBmV(upPowerMin));
        }
        if (upPowerMax != null) {
            cmcQueryMap.put("upPowerMax", UnitConfigConstant.transPowerToDBmV(upPowerMax));
        }
        if (downPowerMin != null) {
            cmcQueryMap.put("downPowerMin", UnitConfigConstant.transPowerToDBmV(downPowerMin));
        }
        if (downPowerMax != null) {
            cmcQueryMap.put("downPowerMax", UnitConfigConstant.transPowerToDBmV(downPowerMax));
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
        cmcQueryMap.put("start", start);
        cmcQueryMap.put("limit", limit);
        cmcQueryMap.put("sort", sort);
        cmcQueryMap.put("dir", dir);

        if(logger.isDebugEnabled()) {
            logger.debug("begin query cmclist at: " + System.currentTimeMillis());
        }
        cmtsNetInfo = cmtsNetworkInfoService.queryCmtsInfoList(cmcQueryMap);
//        List<CmOutPowerNotInRange> cmOutPowerNotInRange=cmtsNetworkInfoService.queryCmOutPowerNotInRange(cmcQueryMap);
//        List<CmRePowerNotInRange> cmRePowerNotInRange=cmtsNetworkInfoService.queryCmRePowerNotInRange(cmcQueryMap);
//        List<UpSnrNotInRange> upSnrNotInRange=cmtsNetworkInfoService.queryUpSnrNotInRange(cmcQueryMap);
//        List<DownSnrAvgNotInRange> downSnrAvgNotInRange=cmtsNetworkInfoService.queryDownSnrAvgNotInRange(cmcQueryMap);
//        for(CmtsNetworkInfo cni:cmtsNetInfo){
//            System.out.println(cni.getCmcId());
//            for(int j=0;j<cmtsNetInfo.size();j++){
//                Integer cmTotal=cni.getCountTotal();
//                if(cmTotal!=null){
//                    if(cni.getCmcId().equals(cmOutPowerNotInRange.get(j).getCmcId())){
//                        if(cmOutPowerNotInRange.get(j).getCmOutPowerNotInRange()!=null){
//                            cni.setCmOutPowerNotInRange(1-(double)(cmOutPowerNotInRange.get(j).getCmOutPowerNotInRange()/cmTotal)); 
//                        }                        
//                    }
//                    if(cni.getCmcId().equals(cmRePowerNotInRange.get(j).getCmcId())){
//                        if(cmRePowerNotInRange.get(j).getCmRePowerNotInRange()!=null){
//                            cni.setCmRePowerNotInRange(1-(double)(cmRePowerNotInRange.get(j).getCmRePowerNotInRange()/cmTotal)); 
//                        }                        
//                    }
//                    if(cni.getCmcId().equals(upSnrNotInRange.get(j).getCmcId())){
//                        if(upSnrNotInRange.get(j).getUpSnrNotInRange()!=null){
//                            cni.setUpSnrNotInRange(1-(double)(upSnrNotInRange.get(j).getUpSnrNotInRange()/cmTotal)); 
//                        }                       
//                    }
//                    if(cni.getCmcId().equals(downSnrAvgNotInRange.get(j).getCmcId())){
//                        if(downSnrAvgNotInRange.get(j).getDownSnrAvgNotInRange()!=null){
//                            cni.setDownSnrAvgNotInRange(1-(double)(downSnrAvgNotInRange.get(j).getDownSnrAvgNotInRange()/cmTotal)); 
//                        }                        
//                    } 
//                }                  
//            }                        
//        }
        cmcNum = cmtsNetworkInfoService.getCmcNum(cmcQueryMap);
        if(logger.isDebugEnabled()) {
            logger.debug("after query cmtsInfolist at: " + System.currentTimeMillis());
        }
        JsonConfig jsonConfig = new JsonConfig();
        jsonConfig.registerDefaultValueProcessor(Double.class, new DefaultDefaultValueProcessor() {
            public Object getDefaultValue(Class type) {
                return null;
            }
        });
        json.put("data1", cmtsNetInfo);
        json.put("rowCount", cmcNum);
        writeDataToAjax(JSONObject.fromObject(json,jsonConfig));
        return NONE;
    }

    
    public String saveLocalThreshold() throws IOException {
        Map<String, Object> cmcQueryMap = new HashMap<String, Object>();
        cmcQueryMap.put("upSnrMin", upSnrMin);
        cmcQueryMap.put("upSnrMax", upSnrMax);
        cmcQueryMap.put("downSnrMin", downSnrMin);
        cmcQueryMap.put("downSnrMax", downSnrMax);
        cmcQueryMap.put("upPowerMin", UnitConfigConstant.transPowerToDBmV(upPowerMin));
        cmcQueryMap.put("upPowerMax", UnitConfigConstant.transPowerToDBmV(upPowerMax));
        cmcQueryMap.put("downPowerMin", UnitConfigConstant.transPowerToDBmV(downPowerMin));
        cmcQueryMap.put("downPowerMax", UnitConfigConstant.transPowerToDBmV(downPowerMax));
        JSONObject jso = new JSONObject();
        try{
            cmtsNetworkInfoService.modifyLocalThreshold(cmcQueryMap); 
            jso.put("data", "success");
        }catch(Exception e){
            jso.put("data", "fail");
        }    
        writeDataToAjax(jso);
        return NONE;
    }
    
    public String getLocalThreshold() throws IOException {
        CmtsInfoThreshold cmtsInfoThreshold=new CmtsInfoThreshold();
        cmtsInfoThreshold = cmtsNetworkInfoService.getLocalThreshold();
        JsonConfig jsonConfig = new JsonConfig();
        jsonConfig.registerDefaultValueProcessor(Integer.class, new DefaultDefaultValueProcessor() {
            public Object getDefaultValue(Class type) {
                return null;
            }
        });
        JSONObject jso = new JSONObject();
        jso = JSONObject.fromObject(cmtsInfoThreshold,jsonConfig);
        writeDataToAjax(jso);
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
                    String cmcOffline = "("
                            + DateUtils.getTimeDesInObscure(System.currentTimeMillis()
                                    - cmc.getLastDeregisterTime().getTime(), uc.getUser().getLanguage()) + ")";
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

    public CmcService getCmcService() {
        return cmcService;
    }

    public void setCmcService(CmcService cmcService) {
        this.cmcService = cmcService;
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

    public EntityTypeService getEntityTypeService() {
        return entityTypeService;
    }

    public void setEntityTypeService(EntityTypeService entityTypeService) {
        this.entityTypeService = entityTypeService;
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

    public Integer getMddInterval() {
        return mddInterval;
    }

    public void setMddInterval(Integer mddInterval) {
        this.mddInterval = mddInterval;
    }

    public Integer getUpSnrMin() {
        return upSnrMin;
    }

    public void setUpSnrMin(Integer upSnrMin) {
        this.upSnrMin = upSnrMin;
    }

    public Integer getUpSnrMax() {
        return upSnrMax;
    }

    public void setUpSnrMax(Integer upSnrMax) {
        this.upSnrMax = upSnrMax;
    }

    public Integer getDownSnrMin() {
        return downSnrMin;
    }

    public void setDownSnrMin(Integer downSnrMin) {
        this.downSnrMin = downSnrMin;
    }

    public Integer getDownSnrMax() {
        return downSnrMax;
    }

    public void setDownSnrMax(Integer downSnrMax) {
        this.downSnrMax = downSnrMax;
    }

    public Integer getUpPowerMin() {
        return upPowerMin;
    }

    public void setUpPowerMin(Integer upPowerMin) {
        this.upPowerMin = upPowerMin;
    }

    public Integer getUpPowerMax() {
        return upPowerMax;
    }

    public void setUpPowerMax(Integer upPowerMax) {
        this.upPowerMax = upPowerMax;
    }

    public Integer getDownPowerMin() {
        return downPowerMin;
    }

    public void setDownPowerMin(Integer downPowerMin) {
        this.downPowerMin = downPowerMin;
    }

    public Integer getDownPowerMax() {
        return downPowerMax;
    }

    public void setDownPowerMax(Integer downPowerMax) {
        this.downPowerMax = downPowerMax;
    }
    
    public CmtsInfoThreshold getCcInfoThreshold() {
        return ccInfoThreshold;
    }

    public void setCcInfoThreshold(CmtsInfoThreshold ccInfoThreshold) {
        this.ccInfoThreshold = ccInfoThreshold;
    }
    public Logger getLogger() {
        return logger;
    }
    
    
}
