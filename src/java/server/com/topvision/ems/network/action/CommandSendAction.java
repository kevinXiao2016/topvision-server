/***********************************************************************
 * $Id: CommandSendAction.java,v1.0 2014年7月17日 上午10:36:12 $
 * 
 * @author: loyal
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.network.action;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.facade.domain.EntityType;
import com.topvision.ems.network.domain.EntityTypeRelation;
import com.topvision.ems.network.domain.SendConfigEntity;
import com.topvision.ems.network.service.CommandSendService;
import com.topvision.ems.template.service.EntityTypeService;
import com.topvision.framework.common.IpUtils;
import com.topvision.framework.common.MacUtils;
import com.topvision.framework.web.struts2.BaseAction;
import com.topvision.platform.ResourceManager;
import com.topvision.platform.domain.UserContext;

/**
 * @author loyal
 * @created @2014年7月17日-上午10:36:12
 * 
 */
@Controller("commandSendAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class CommandSendAction extends BaseAction {
    private static final long serialVersionUID = 6923248733636300987L;
    @Resource(name = "commandSendService")
    private CommandSendService commandSendService;
    private String regionId;
    private String ip;
    private String mac;
    private String typeId;
    private String name;
    private String createTime;
    private String endTime;
    private int start;
    private int limit;
    private String entityIds;
    private Long resultId;
    private String result;
    private Integer state;
    private Long entityId;
    @Autowired
    private EntityTypeService entityTypeService;
    private final Logger logger = LoggerFactory.getLogger(CommandSendAction.class);
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private JSONArray entityTypes = new JSONArray();

    public String showCommandSendEntityList() {/*
                                                * List<EntityType> entityTypeList = new
                                                * ArrayList<EntityType>(); UserContext uc =
                                                * (UserContext)
                                                * super.getSession().get(UserContext.KEY);
                                                * if(uc.hasSupportModule("olt")){ Long oltType =
                                                * entityTypeService.getOltType(); List<EntityType>
                                                * temp = entityTypeService.loadSubType(oltType);
                                                * entityTypeList.addAll(temp); }
                                                * if(uc.hasSupportModule("cmc")){ Long cmcType =
                                                * entityTypeService.getCcmtswithagentType();
                                                * List<EntityType> temp =
                                                * entityTypeService.loadSubType(cmcType);
                                                * entityTypeList.addAll(temp); } entityTypes =
                                                * JSONArray.fromObject(entityTypeList);
                                                */
        return SUCCESS;
    }

    public String showEntityList() {
        List<EntityType> entityTypeList = new ArrayList<EntityType>();
        UserContext uc = (UserContext) super.getSession().get(UserContext.KEY);
        if (uc.hasSupportModule("olt")) {
            Long oltType = entityTypeService.getOltType();
            List<EntityType> temp = entityTypeService.loadSubType(oltType);
            entityTypeList.addAll(temp);
        }
        if (uc.hasSupportModule("cmc")) {
            Long cmcType = entityTypeService.getCcmtswithagentType();
            List<EntityType> temp = entityTypeService.loadSubType(cmcType);
            entityTypeList.addAll(temp);
        }
        entityTypes = JSONArray.fromObject(entityTypeList);
        return SUCCESS;
    }

    public String loadEntityList() {
        Map<String, Object> json = new HashMap<String, Object>();
        Map<String, Object> map = new HashMap<String, Object>();
        UserContext uc = (UserContext) super.getSession().get(UserContext.KEY);
        String macRule = uc.getMacDisplayStyle();
        if (regionId != null && !"".equals(regionId.trim())) {
            map.put("regionId", regionId);
        }
        if (ip != null && !"".equals(ip.trim())) {
            map.put("ip", ip);
        }
        if (createTime != null && !"".equals(createTime.trim())) {
            map.put("createTime", createTime);
        }
        if (endTime != null && !"".equals(endTime.trim())) {
            map.put("endTime", endTime);
        }
        if (name != null && !"".equals(name.trim())) {
            map.put("name", name);
        }
        if (mac != null && !"".equals(mac.trim())) {
            String formatQueryMac = MacUtils.formatQueryMac(mac);
            if (formatQueryMac.indexOf(":") == -1) {
                map.put("queryMacWithoutSplit", formatQueryMac);
            }
            map.put("mac", formatQueryMac);
        }
        if (typeId != null && !"".equals(typeId.trim())) {
            map.put("typeId", typeId);
        }
        if (sort != null && !"".equals(sort.trim())) {
            map.put("sort", sort);
        }
        if (dir != null && !"".equals(dir.trim())) {
            map.put("dir", dir);
        }
        List<Entity> entityList = commandSendService.loadEntityList(map, start, limit);
        Long entityNum = commandSendService.loadEntityListNum(map);
        for (Entity anEntityList : entityList) {
            String localtion = anEntityList.getLocation();
            Timestamp createTime = anEntityList.getCreateTime();
            String mac = anEntityList.getMac();
            Long entityId = anEntityList.getEntityId();
            List<String> folderList = new ArrayList<String>();
            folderList = commandSendService.getEntityFolder(entityId);
            StringBuilder location = new StringBuilder();
            int size = folderList.size();
            for (int j = 0; j < size; j++) {
                if (folderList.get(j) != null) {
                    if (j == (size - 1)) {
                        location.append(getResourceManager().getString(folderList.get(j)));
                    } else {
                        location.append(getResourceManager().getString(folderList.get(j))).append(",");
                    }
                }
            }
            if (localtion != null) {
                anEntityList.setLocation(location.toString());
            }
            if (createTime != null) {
                anEntityList.setSysUpTime(sdf.format(createTime));
            }
            if (mac != null) {
                anEntityList.setMac(MacUtils.convertMacToDisplayFormat(mac, macRule));
            }
        }
        json.put("data", entityList);
        json.put("rowCount", entityNum);
        writeDataToAjax(JSONObject.fromObject(json));
        return NONE;
    }

    public String addSendConfigEntity() {
        Map<String, Object> json = new HashMap<String, Object>();
        String result = "true";
        try {
            String[] entityIdsArrayString = entityIds.split("\\$");
            List<Long> entityIdList = new ArrayList<>();
            for (String entityId : entityIdsArrayString) {
                try {
                    Long eId = Long.parseLong(entityId);
                    entityIdList.add(eId);
                } catch (NumberFormatException e) {
                    logger.error("addSendConfigEntity entityId error [" + entityId + "]", e);
                }
            }
            commandSendService.addSendConfigEntity(entityIdList);
        } catch (Exception e) {
            result = "false";
            logger.error("", e);
        }
        json.put("result", result);
        writeDataToAjax(JSONObject.fromObject(json));
        return NONE;
    }

    public String loadCommandSendEntityList() {
        Map<String, Object> json = new HashMap<String, Object>();
        Map<String, Object> map = new HashMap<String, Object>();
        UserContext uc = (UserContext) super.getSession().get(UserContext.KEY);
        String macRule = uc.getMacDisplayStyle();
        if (regionId != null && !"".equals(regionId.trim())) {
            map.put("regionId", regionId);
        }
        if (typeId != null && !"".equals(typeId.trim())) {
            map.put("typeId", typeId);
        }
        if (state != null) {
            map.put("state", state);
        }
        if (sort != null && !"".equals(sort.trim())) {
            map.put("sort", sort);
        }
        if (dir != null && !"".equals(dir.trim())) {
            map.put("dir", dir);
        }
        List<SendConfigEntity> entityList = commandSendService.getCommandSendEntityList(map, start, limit);
        Long entityNum = commandSendService.getCommandSendEntityListNum(map);

        for (SendConfigEntity anEntityList : entityList) {
            String folderName = anEntityList.getFolderName();
            Long ip = anEntityList.getIp();
            Timestamp dt = anEntityList.getDt();
            String mac = anEntityList.getMac();
            Long entityId = anEntityList.getEntityId();
            List<String> folderList = new ArrayList<String>();
            folderList = commandSendService.getEntityFolder(entityId);
            StringBuilder location = new StringBuilder();
            int size = folderList.size();
            for (int j = 0; j < size; j++) {
                if (folderList.get(j) != null) {
                    if (j == (size - 1)) {
                        location.append(getResourceManager().getString(folderList.get(j)));
                    } else {
                        location.append(getResourceManager().getString(folderList.get(j))).append(",");
                    }
                }
            }
            if (folderName != null) {
                anEntityList.setFolderName(location.toString());
            }
            if (ip != null) {
                anEntityList.setIpString(new IpUtils(ip).toString());
            }
            if (ip != null) {
                anEntityList.setIpString(new IpUtils(ip).toString());
            }
            if (dt != null) {
                anEntityList.setDtString(sdf.format(dt));
            }
            if (mac != null) {
                anEntityList.setMac(MacUtils.convertMacToDisplayFormat(mac, macRule));
            }
        }
        json.put("data", entityList);
        json.put("rowCount", entityNum);
        writeDataToAjax(JSONObject.fromObject(json));
        return NONE;
    }

    public String deleteSendConfigEntity() {
        Map<String, Object> json = new HashMap<String, Object>();
        String result = "true";
        try {
            String[] entityIdsArrayString = entityIds.split("\\$");
            List<Long> entityIdList = new ArrayList<>();
            for (String entityId : entityIdsArrayString) {
                try {
                    Long eId = Long.parseLong(entityId);
                    entityIdList.add(eId);
                } catch (NumberFormatException e) {
                    logger.error("deleteSendConfigEntity entityId error [" + entityId + "]", e);
                }
            }
            commandSendService.deleteSendConfigEntity(entityIdList);
        } catch (Exception e) {
            result = "false";
            logger.error("", e);
        }
        json.put("result", result);
        writeDataToAjax(JSONObject.fromObject(json));
        return NONE;
    }

    public String showCommandResult() {
        return SUCCESS;
    }

    public String getCommandResult() {
        // result = commandSendService.readSendConfigResult(new IpUtils(ip).longValue(), resultId);
        result = commandSendService.getSendConfigResult(entityId);
        writeDataToAjax(result);
        return NONE;
    }

    public String sendConfig() {
        commandSendService.makeSendConfigArray();
        return NONE;
    }

    /**
     * 根据entity id下发配置
     * 
     * @return
     */
    public String makeSendConfigById() {
        commandSendService.addAutoSendConfigEntity(entityId);
        return NONE;
    }

    public String entityTypeTree() {
        List<EntityTypeRelation> entityTypeList = new ArrayList<EntityTypeRelation>();
        UserContext uc = (UserContext) super.getSession().get(UserContext.KEY);
        if (uc.hasSupportModule("olt")) {
            Long oltType = entityTypeService.getOltType();
            EntityType olt = entityTypeService.getEntityType(oltType);
            EntityTypeRelation oltTypeRelation = new EntityTypeRelation();
            oltTypeRelation.setName(olt.getDisplayName());
            oltTypeRelation.setTypeId(olt.getTypeId());
            entityTypeList.add(oltTypeRelation);
            List<EntityType> temp = entityTypeService.loadSubType(oltType);
            for (EntityType entityType : temp) {
                EntityTypeRelation typeRelation = new EntityTypeRelation();
                typeRelation.setName(entityType.getDisplayName());
                typeRelation.setType(olt.getTypeId());
                typeRelation.setTypeId(entityType.getTypeId());
                entityTypeList.add(typeRelation);
            }
        }
        if (uc.hasSupportModule("cmc")) {
            Long cmcType = entityTypeService.getCcmtsType();
            EntityType cmc = entityTypeService.getEntityType(cmcType);
            EntityTypeRelation cmcTypeRelation = new EntityTypeRelation();
            cmcTypeRelation.setName(cmc.getDisplayName());
            cmcTypeRelation.setTypeId(cmc.getTypeId());
            entityTypeList.add(cmcTypeRelation);
            List<EntityType> temp = entityTypeService.loadSubType(cmcType);
            for (EntityType entityType : temp) {
                EntityTypeRelation typeRelation = new EntityTypeRelation();
                typeRelation.setName(entityType.getDisplayName());
                typeRelation.setType(cmc.getTypeId());
                typeRelation.setTypeId(entityType.getTypeId());
                entityTypeList.add(typeRelation);
            }
        }
        if (uc.hasSupportModule("onu")) {
            Long onuType = entityTypeService.getOnuType();
            EntityType onu = entityTypeService.getEntityType(onuType);
            EntityTypeRelation onuTypeRelation = new EntityTypeRelation();
            onuTypeRelation.setName(onu.getDisplayName());
            onuTypeRelation.setTypeId(onu.getTypeId());
            entityTypeList.add(onuTypeRelation);
            List<EntityType> temp = entityTypeService.loadSubType(onuType);
            for (EntityType entityType : temp) {
                EntityTypeRelation typeRelation = new EntityTypeRelation();
                typeRelation.setName(entityType.getDisplayName());
                typeRelation.setType(onu.getTypeId());
                typeRelation.setTypeId(entityType.getTypeId());
                entityTypeList.add(typeRelation);
            }
        }
        entityTypes = convertEntityTypeToZtreeFormat(entityTypeList);
        writeDataToAjax(entityTypes);
        return NONE;
    }

    /**
     * 将TopoFolder集合转换成ztree要求的格式
     *
     * @param entityTypes
     * @return
     */
    private JSONArray convertEntityTypeToZtreeFormat(List<EntityTypeRelation> entityTypes) {
        JSONArray entityTypeList = new JSONArray();
        for (EntityTypeRelation entityType : entityTypes) {
            JSONObject json = new JSONObject();
            json.put("id", entityType.getTypeId());
            json.put("pId", entityType.getType());
            json.put("name", entityType.getName());
            json.put("open", true);
            json.put("chkDisabled", false);
            json.put("checked", false);
            entityTypeList.add(json);
        }
        return entityTypeList;
    }

    private ResourceManager getResourceManager() {
        return ResourceManager.getResourceManager("com.topvision.ems.resources.resources");
    }

    public String getRegionId() {
        return regionId;
    }

    public void setRegionId(String regionId) {
        this.regionId = regionId;
    }

    public CommandSendService getCommandSendService() {
        return commandSendService;
    }

    public void setCommandSendService(CommandSendService commandSendService) {
        this.commandSendService = commandSendService;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getEntityIds() {
        return entityIds;
    }

    public void setEntityIds(String entityIds) {
        this.entityIds = entityIds;
    }

    public Long getResultId() {
        return resultId;
    }

    public void setResultId(Long resultId) {
        this.resultId = resultId;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public JSONArray getEntityTypes() {
        return entityTypes;
    }

    public void setEntityTypes(JSONArray entityTypes) {
        this.entityTypes = entityTypes;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }
}
