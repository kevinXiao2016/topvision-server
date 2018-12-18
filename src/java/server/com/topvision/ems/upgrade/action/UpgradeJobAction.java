/***********************************************************************
 * $Id: UpgradeJobAction.java,v1.0 2014年9月25日 上午10:19:44 $
 * 
 * @author: loyal
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.upgrade.action;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.facade.domain.EntityType;
import com.topvision.ems.template.service.EntityTypeService;
import com.topvision.ems.upgrade.domain.EntityVersion;
import com.topvision.ems.upgrade.domain.UpgradeEntity;
import com.topvision.ems.upgrade.domain.UpgradeJobInfo;
import com.topvision.ems.upgrade.domain.UpgradeStatusCode;
import com.topvision.ems.upgrade.service.EntityVersionService;
import com.topvision.ems.upgrade.service.UpgradeJobService;
import com.topvision.ems.upgrade.service.UpgradeStatusService;
import com.topvision.ems.upgrade.utils.UpgradeStatusConstants;
import com.topvision.framework.common.MacUtils;
import com.topvision.framework.web.struts2.BaseAction;
import com.topvision.platform.ResourceManager;
import com.topvision.platform.domain.UserContext;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * @author loyal
 * @created @2014年9月25日-上午10:19:44
 * 
 */
@Controller("upgradeJobAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class UpgradeJobAction extends BaseAction {
    private static final long serialVersionUID = -5127746661886359827L;
    @Resource(name = "upgradeJobService")
    private UpgradeJobService upgradeJobService;
    @Autowired
    private EntityVersionService entityVersionService;
    @Autowired
    private EntityTypeService entityTypeService;
    @Autowired
    private UpgradeStatusService upgradeStatusService;
    private String name;
    private String startTime;
    private String upgradeTime;
    private String imageFile;
    private String versionName;
    private Integer type;
    private String transferType;
    private String mac;
    private String ip;
    private String softVersion;
    private Long jobId;
    private String entityIds;
    private Long typeId;
    private Long entityType;
    List<UpgradeJobInfo> upgradeJobInfos = new ArrayList<UpgradeJobInfo>();
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    List<EntityType> entityTypeList = new ArrayList<EntityType>();
    private JSONArray entityTypes = new JSONArray();
    private JSONArray ccWithAgentTypes = new JSONArray();
    private JSONArray oltTypes = new JSONArray();
    private JSONArray upgradeStatus = new JSONArray();
    private static String TFTP = "tftp";
    private Long entityId;
    private String result;
    private String subType;
    private final Logger logger = LoggerFactory.getLogger(UpgradeJobAction.class);
    private String statusIds;

    private JSONArray versions = new JSONArray();

    public String showUpgradeJob() {
        versionName = upgradeJobService.getJobById(jobId).getVersionName();
        Map<Integer, String> upgradeStatusMap = upgradeStatusService.getAllStatus();
        List<UpgradeStatusCode> upgradeStatusCodes = new ArrayList<UpgradeStatusCode>();
        for (Integer key : upgradeStatusMap.keySet()) {
            upgradeStatusCodes.add(new UpgradeStatusCode(key, upgradeStatusMap.get(key)));
        }
        upgradeStatus = JSONArray.fromObject(upgradeStatusCodes);
        return SUCCESS;
    }

    /**
     * 获取所有升级任务
     * @return
     */
    public String getUpgradeJob() {
        upgradeJobInfos = upgradeJobService.getAllUpgradeJob();
        writeDataToAjax(JSONArray.fromObject(upgradeJobInfos));
        return NONE;
    }

    /**
     *
     * @return
     */
    public String getUpgradeJobList() {
        JSONObject json = new JSONObject();
        upgradeJobInfos = upgradeJobService.getAllUpgradeJob();
        json.put("data", upgradeJobInfos);
        json.put("rowCount", upgradeJobInfos.size());
        writeDataToAjax(json);
        return NONE;
    }

    public String showAddJob() {
        List<EntityVersion> list = entityVersionService.getEntityVersionList(null, null);
        List<EntityType> ccWithAgentTypeList = new ArrayList<EntityType>();
        List<EntityType> oltTypeList = entityTypeService.loadSubType(entityTypeService.getOltType());
        ccWithAgentTypeList = entityTypeService.loadSubType(entityTypeService.getCcmtsType());
        versions = JSONArray.fromObject(list);
        ccWithAgentTypes = JSONArray.fromObject(ccWithAgentTypeList);
        oltTypes = JSONArray.fromObject(oltTypeList);
        return SUCCESS;
    }

    /**
     * 添加一个job
     * @return
     */
    public String addUpgradeJob() {
        String message = "";
        if (upgradeJobService.isJobExist(name)) {
            message = "exist";
        } else {
            UpgradeJobInfo upgradeJobInfo = new UpgradeJobInfo();
            upgradeJobInfo.setName(name);
            upgradeJobInfo.setSubType(subType);
            upgradeJobInfo.setJobClass("com.topvision.ems.upgrade.job.UpgradeJob");
            if (TFTP.equals(transferType)) {
                upgradeJobInfo.setWorkerClass("com.topvision.ems.upgrade.worker.CcmtsUpgradeWorkerB");
            } else {
                upgradeJobInfo.setWorkerClass("com.topvision.ems.upgrade.worker.CcmtsUpgradeWorkerA");
            }
            if (subType != null && subType.contains("pn")) {
                upgradeJobInfo.setWorkerClass("com.topvision.ems.upgrade.worker.OnuUpgradeWorker");
            }
            if (startTime == null || "".equals(startTime)) {
                upgradeJobInfo.setStartTime(new Timestamp(System.currentTimeMillis()));
            } else {
                upgradeJobInfo.setStartTime(Timestamp.valueOf(startTime));
            }
            upgradeJobInfo.setType(type);
            upgradeJobInfo.setTransferType(transferType);
            upgradeJobInfo.setImageFile(imageFile);
            upgradeJobInfo.setVersionName(versionName);
            upgradeJobInfo.setEntityIds(entityIds);
            upgradeJobService.addJob(upgradeJobInfo);
        }
        writeDataToAjax(message);
        return NONE;
    }

    public String showDeleteJob() {
        return SUCCESS;
    }

    /**
     * 获取某个job下可添加entity列表
     * @return
     */
    public String getEntity() {
        List<Entity> entityList = new ArrayList<Entity>();
        JSONObject json = new JSONObject();
        Map<String, Object> map = new HashMap<String, Object>();
        if (mac != null && !mac.equals("")) {
            String formatQueryMac = MacUtils.formatQueryMac(mac);
            if (formatQueryMac.indexOf(":") == -1) {
                map.put("queryMacWithoutSplit", formatQueryMac);
            }
            map.put("mac", formatQueryMac);
        }
        if (ip != null && !ip.equals("")) {
            map.put("ip", ip);
        }
        if (name != null && !name.equals("")) {
            map.put("name", name);
        }
        if (softVersion != null && !softVersion.equals("")) {
            map.put("softVersion", softVersion);
        }
        if (typeId != null && typeId > 0) {
            map.put("typeId", typeId);
        } else {
            if (versionName == null) {
                versionName = upgradeJobService.getJobById(jobId).getVersionName();
                transferType = upgradeJobService.getJobById(jobId).getTransferType();
            }
            EntityVersion entityVersion = entityVersionService.getEntityVersion(versionName);
            if (entityVersion != null && !"ftp".equals(transferType)) {
                String typeDisplayNames = entityVersion.getTypeDisplayNames();
                map.put("typeIds", getEntityTypeIdsByDisplayNames(typeDisplayNames));
            }
        }
        if (entityType != null) {
            map.put("type", entityType);
        }
        if (sort != null && !"".equals(sort.trim())) {
            map.put("sort", sort);
        }
        if (dir != null && !"".equals(dir.trim())) {
            map.put("dir", dir);
        }
        map.put("start", start);
        map.put("limit", limit);
        UserContext uc = (UserContext) super.getSession().get(UserContext.KEY);
        String macRule = uc.getMacDisplayStyle();
        entityList = upgradeJobService.getEntity(map, entityType);
        for (Entity anEntityList : entityList) {
            String mac = anEntityList.getMac();
            Timestamp createTime = anEntityList.getCreateTime();
            if (mac != null) {
                anEntityList.setMac(MacUtils.convertMacToDisplayFormat(mac, macRule));
            }
            if (createTime != null) {
                anEntityList.setSysUpTime(sdf.format(createTime));
            }
        }
        Long entityNum = upgradeJobService.getEntityNum(map);
        json.put("data", entityList);
        json.put("rowCount", entityNum);
        writeDataToAjax(json);
        return NONE;
    }

    /**
     * 通过displayNames获得对应的typeIds
     * @param typeDisplayNames
     * @return
     */
    private String getEntityTypeIdsByDisplayNames(String typeDisplayNames){
        String typeIds = "";
        List<String> displayNameList = Arrays.asList(typeDisplayNames.split(","));
        List<EntityType> entityTypes = entityTypeService.getLicenseGroupEntityTypes();
        for(EntityType entityType : entityTypes){
            if(displayNameList.contains(entityType.getDisplayName())){
                typeIds += entityType.getTypeId() + ",";
            }
        }
        if(typeIds.endsWith(",")){
            typeIds = typeIds.substring(0, typeIds.length() - 1);
        }
        return typeIds;
    }

    /**
     * 删除某个job
     * @return
     * @throws SchedulerException
     */
    public String deleteUpgradeJob() throws SchedulerException {
        upgradeJobService.deleteJob(jobId);
        return NONE;
    }

    /**
     * 获取某个job下的entity
     * @return
     */
    public String getUpgradeEntity() {
        List<UpgradeEntity> entityList = new ArrayList<UpgradeEntity>();
        JSONObject json = new JSONObject();
        Map<String, Object> map = new HashMap<String, Object>();
        UserContext uc = (UserContext) super.getSession().get(UserContext.KEY);
        String macRule = uc.getMacDisplayStyle();
        Map<Integer, String> statusMap = upgradeStatusService.getAllStatus();
        if (mac != null && !mac.equals("")) {
            String formatQueryMac = MacUtils.formatQueryMac(mac);
            if (formatQueryMac.indexOf(":") == -1) {
                map.put("queryMacWithoutSplit", formatQueryMac);
            }
            map.put("mac", formatQueryMac);
        }
        if (ip != null && !ip.equals("")) {
            map.put("ip", ip);
        }
        if (name != null && !name.equals("")) {
            map.put("name", name);
        }
        if (statusIds != null && !"".equals(statusIds.trim())) {
            map.put("statusIds", statusIds);
        }
        if (sort != null && !"".equals(sort.trim())) {
            map.put("sort", sort);
        }
        if (dir != null && !"".equals(dir.trim())) {
            map.put("dir", dir);
        }
        map.put("start", start);
        map.put("limit", limit);
        map.put("jobId", jobId);
        entityList = upgradeJobService.getUpgradeEntity(map);
        for (UpgradeEntity anEntityList : entityList) {
            String mac = anEntityList.getMac();
            Timestamp upgradeTime = anEntityList.getUpgradeTime();
            if (mac != null) {
                anEntityList.setMac(MacUtils.convertMacToDisplayFormat(mac, macRule));
            }
            if (upgradeTime != null) {
                anEntityList.setUpgradeTimeString(sdf.format(upgradeTime));
            }
            anEntityList.setUpgradeStatusString(
                    getNetworkResourceManager().getString(statusMap.get(anEntityList.getUpgradeStatus())));
        }
        Long entityNum = upgradeJobService.getUpgradeEntityNum(map);
        json.put("data", entityList);
        json.put("rowCount", entityNum);
        writeDataToAjax(json);
        return NONE;
    }

    /**
     * 展示某个job的升级设备列表
     * @return
     */
    public String showEntityList() {
        UpgradeJobInfo upgradeJobInfo = upgradeJobService.getJobById(jobId);
        String versionName = upgradeJobInfo.getVersionName();
        EntityVersion entityVersion = entityVersionService.getEntityVersion(versionName);
        String typeDisplayNames = entityVersion.getTypeDisplayNames();
        String[] typeNames = typeDisplayNames.split(",");
        List<String> typeNameList = Arrays.asList(typeNames);
        String transferType = entityVersion.getTransferType();
        ArrayList<EntityType> newEntityType = new ArrayList<EntityType>();
        if ("ftp".equals(transferType)) {
            entityTypeList = entityTypeService.loadSubType(entityTypeService.getOltType());
            entityType = entityTypeService.getOltType();
            newEntityType = (ArrayList<EntityType>) entityTypeList;
        } else if ("tftp".equals(transferType)) {
            entityTypeList = entityTypeService.loadSubType(entityTypeService.getCcmtsType());
            entityType = entityTypeService.getCcmtsType();
            for (EntityType anEntityTypeList : entityTypeList) {
                if (typeNameList.contains(anEntityTypeList.getDisplayName())) {
                    newEntityType.add(anEntityTypeList);
                }
            }
        }
        entityTypes = JSONArray.fromObject(newEntityType);
        return SUCCESS;
    }

    private ResourceManager getNetworkResourceManager() {
        return ResourceManager.getResourceManager("com.topvision.ems.network.resources");
    }

    public String showUpgradeResult() {
        return SUCCESS;
    }

    /**
     * 获取某个job中某个entity的升级结果
     * @return
     */
    public String getUpgradeResult() {
        result = upgradeJobService.getUpgradeEntityByEntityId(entityId, jobId).getUpgradeNote();
        writeDataToAjax(result == null ? "" : result);
        return NONE;
    }

    /**
     * 添加一个entity到某个job
     * @return
     */
    public String addEntityToJob() {
        upgradeJobService.addEntityToJob(entityIds, jobId);
        return NONE;
    }

    /**
     * 删除某个job下的entity
     * @return
     */
    public String deleteJobEntity() {
        upgradeJobService.deleteJobEntity(entityIds, jobId);
        return NONE;
    }

    /**
     * TODO
     * 立即升级所有设备,不知哪个是有用的
     * @return
     */
    public String upgradeNow() {
        try {
            String[] entityIdsArrayString = entityIds.split("\\,");
            List<Long> entityIdList = new ArrayList<Long>();
            for (String anEntityIdsArrayString : entityIdsArrayString) {
                try {
                    Long entityId = new Long(anEntityIdsArrayString.trim());
                    UpgradeEntity upgradeEntity = upgradeJobService.getUpgradeEntityByEntityId(entityId, jobId);
                    if (upgradeEntity != null && UpgradeStatusConstants.isUpgradeNow(upgradeEntity.getUpgradeStatus())) {
                        continue;
                    }
                    entityIdList.add(entityId);
                } catch (NumberFormatException e) {
                    logger.error("", e);
                }
            }
            upgradeJobService.upgradeNow(entityIdList, jobId);
        } catch (Exception e) {
            logger.error("", e);
        }
        return NONE;
    }

    /**
     * 立即升级所有设备
     * @return
     */
    public String upgradeAllNow() {
        try {
            Map<String, Object> map = new HashMap<String, Object>();
            if (mac != null && !mac.equals("")) {
                String formatQueryMac = MacUtils.formatQueryMac(mac);
                if (formatQueryMac.indexOf(":") == -1) {
                    map.put("queryMacWithoutSplit", formatQueryMac);
                }
                map.put("mac", formatQueryMac);
            }
            if (ip != null && !ip.equals("")) {
                map.put("ip", ip);
            }
            if (name != null && !name.equals("")) {
                map.put("name", name);
            }
            if (statusIds != null && !"".equals(statusIds.trim())) {
                map.put("statusIds", statusIds);
            }
            map.put("jobId", jobId);
            map.put("start", 0);
            map.put("limit", Integer.MAX_VALUE);
            List<UpgradeEntity> entityList = upgradeJobService.getUpgradeEntity(map);
            List<Long> entityIdList = new ArrayList<Long>();
            for (UpgradeEntity anEntityList : entityList) {
                Long entityId = anEntityList.getEntityId();
                UpgradeEntity upgradeEntity = upgradeJobService.getUpgradeEntityByEntityId(entityId, jobId);
                if (upgradeEntity != null && UpgradeStatusConstants.isUpgradeNow(upgradeEntity.getUpgradeStatus())) {
                    continue;
                }
                entityIdList.add(entityId);
            }
            upgradeJobService.upgradeNow(entityIdList, jobId);
        } catch (Exception e) {
            logger.error("", e);
        }
        return NONE;
    }

    /**
     * 立即升级单个设备
     */
    public String upgradeSingleNow() {
        try {
            UpgradeEntity upgradeEntity = upgradeJobService.getUpgradeEntityByEntityId(entityId, jobId);
            if (upgradeEntity != null && UpgradeStatusConstants.isUpgradeNow(upgradeEntity.getUpgradeStatus())) {
                writeDataToAjax("upgradeNow");
            } else {
                upgradeJobService.upgradeSingleNow(entityId, jobId);
            }
        } catch (Exception e) {
            logger.error("", e);
        }
        return NONE;
    }

    public String showUpgradeStatusParam() {
        if (entityIds == null) {
            entityIds = "";
        }
        Map<Integer, String> upgradeStatusMap = upgradeStatusService.getErrorStatus();
        List<UpgradeStatusCode> upgradeStatusCodes = new ArrayList<UpgradeStatusCode>();
        for (Integer key : upgradeStatusMap.keySet()) {
            upgradeStatusCodes.add(new UpgradeStatusCode(key, getNetworkResourceManager().getString(
                    upgradeStatusMap.get(key))));
        }
        upgradeStatus = JSONArray.fromObject(upgradeStatusCodes);
        return SUCCESS;
    }

    public String getUpgradeStatusTree() throws Exception {
        Map<String, Object> json = new HashMap<String, Object>();
        Map<Integer, String> upgradeStatusMap = upgradeStatusService.getAllStatus();
        List<UpgradeStatusCode> upgradeStatusCodes = new ArrayList<UpgradeStatusCode>();
        for (Integer key : upgradeStatusMap.keySet()) {
            upgradeStatusCodes.add(new UpgradeStatusCode(key, getNetworkResourceManager().getString(
                    upgradeStatusMap.get(key))));
        }
        json.put("data", upgradeStatusCodes);
        writeDataToAjax(JSONObject.fromObject(json));
        return NONE;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageFile() {
        return imageFile;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getUpgradeTime() {
        return upgradeTime;
    }

    public void setUpgradeTime(String upgradeTime) {
        this.upgradeTime = upgradeTime;
    }

    public void setImageFile(String imageFile) {
        this.imageFile = imageFile;
    }

    public String getVersionName() {
        return versionName;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public Long getJobId() {
        return jobId;
    }

    public void setJobId(Long jobId) {
        this.jobId = jobId;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getSoftVersion() {
        return softVersion;
    }

    public void setSoftVersion(String softVersion) {
        this.softVersion = softVersion;
    }

    public String getEntityIds() {
        return entityIds;
    }

    public void setEntityIds(String entityIds) {
        this.entityIds = entityIds;
    }

    public Long getTypeId() {
        return typeId;
    }

    public void setTypeId(Long typeId) {
        this.typeId = typeId;
    }

    public JSONArray getVersions() {
        return versions;
    }

    public void setVersions(JSONArray versions) {
        this.versions = versions;
    }

    public List<EntityType> getEntityTypeList() {
        return entityTypeList;
    }

    public void setEntityTypeList(List<EntityType> entityTypeList) {
        this.entityTypeList = entityTypeList;
    }

    public JSONArray getEntityTypes() {
        return entityTypes;
    }

    public void setEntityTypes(JSONArray entityTypes) {
        this.entityTypes = entityTypes;
    }

    public Long getEntityType() {
        return entityType;
    }

    public void setEntityType(Long entityType) {
        this.entityType = entityType;
    }

    public JSONArray getCcWithAgentTypes() {
        return ccWithAgentTypes;
    }

    public void setCcWithAgentTypes(JSONArray ccWithAgentTypes) {
        this.ccWithAgentTypes = ccWithAgentTypes;
    }

    public JSONArray getOltTypes() {
        return oltTypes;
    }

    public void setOltTypes(JSONArray oltTypes) {
        this.oltTypes = oltTypes;
    }

    public String getTransferType() {
        return transferType;
    }

    public void setTransferType(String transferType) {
        this.transferType = transferType;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getSubType() {
        return subType;
    }

    public void setSubType(String subType) {
        this.subType = subType;
    }

    public JSONArray getUpgradeStatus() {
        return upgradeStatus;
    }

    public void setUpgradeStatus(JSONArray upgradeStatus) {
        this.upgradeStatus = upgradeStatus;
    }

    public String getStatusIds() {
        return statusIds;
    }

    public void setStatusIds(String statusIds) {
        this.statusIds = statusIds;
    }

}
