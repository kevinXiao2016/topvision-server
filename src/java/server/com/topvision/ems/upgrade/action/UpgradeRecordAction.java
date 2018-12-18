/***********************************************************************
 * $Id: UpgradeRecordAction.java,v1.0 2014年9月23日 下午3:20:03 $
 * 
 * @author: loyal
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.upgrade.action;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.topvision.ems.facade.domain.EntityType;
import com.topvision.ems.template.service.EntityTypeService;
import com.topvision.ems.upgrade.domain.UpgradeRecord;
import com.topvision.ems.upgrade.domain.UpgradeRecordQueryParam;
import com.topvision.ems.upgrade.domain.UpgradeStatusCode;
import com.topvision.ems.upgrade.service.UpgradeRecordService;
import com.topvision.ems.upgrade.service.UpgradeStatusService;
import com.topvision.framework.common.MacUtils;
import com.topvision.platform.ResourceManager;
import com.topvision.framework.web.struts2.BaseAction;
import com.topvision.platform.domain.UserContext;

/**
 * @author loyal
 * @created @2014年9月23日-下午3:20:03
 * 
 */
@Controller("upgradeRecordAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class UpgradeRecordAction extends BaseAction {
    private static final long serialVersionUID = 5015142274889921973L;
    @Resource(name = "upgradeRecordService")
    private UpgradeRecordService upgradeRecordService;
    @Autowired
    private EntityTypeService entityTypeService;
    @Autowired
    private UpgradeStatusService upgradeStatusService;
    private String entityIds;
    private String entityName;
    private String manageIp;
    private String mac;
    private Long status;
    private Long typeId;
    private String upLinkEntityName;
    private String jobName;
    private String startTime;
    private String endTime;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private JSONArray entityTypes = new JSONArray();
    private JSONArray upgradeStatus = new JSONArray();
    private String uplinkDevice;

    public String showUpgradeRecord() {
        Map<Integer, String> upgradeStatusMap = upgradeStatusService.getResultStatus();
        List<UpgradeStatusCode> upgradeStatusCodes = new ArrayList<UpgradeStatusCode>();
        for (Integer key : upgradeStatusMap.keySet()) {
            upgradeStatusCodes.add(new UpgradeStatusCode(key, getNetworkResourceManager().getString(
                    upgradeStatusMap.get(key))));
        }
        upgradeStatus = JSONArray.fromObject(upgradeStatusCodes);
        List<EntityType> entityTypeList = new ArrayList<EntityType>();
        UserContext uc = (UserContext) super.getSession().get(UserContext.KEY);
        if (uc.hasSupportModule("olt")) {
            List<EntityType> oltList = entityTypeService.loadSubType(entityTypeService.getOltType());
            List<EntityType> onulist = entityTypeService.loadSubType(entityTypeService.getOnuType());
            entityTypeList.addAll(oltList);
            entityTypeList.addAll(onulist);
        }
        if (uc.hasSupportModule("cmc")) {
            List<EntityType> ccmtslist = entityTypeService.loadSubType(entityTypeService.getCcmtsType());
            entityTypeList.addAll(ccmtslist);
        }
        entityTypes = JSONArray.fromObject(entityTypeList);
        return SUCCESS;
    }

    public String getUpgradeRecord() {
        Map<String, Object> json = new HashMap<String, Object>();
        UserContext uc = (UserContext) super.getSession().get(UserContext.KEY);
        String macRule = uc.getMacDisplayStyle();
        UpgradeRecordQueryParam upgradeRecordQueryParam = new UpgradeRecordQueryParam(entityName, manageIp, mac,
                status, typeId, uplinkDevice, upLinkEntityName, jobName, startTime, endTime, start, limit, sort, dir);
        List<UpgradeRecord> upgradeRecords = upgradeRecordService.getUpgradeRecord(upgradeRecordQueryParam);
        Map<Integer, String> map = upgradeStatusService.getAllStatus();
        for (int i = 0; i < upgradeRecords.size(); i++) {
            String mac = upgradeRecords.get(i).getMac();
            Timestamp startTime = upgradeRecords.get(i).getStartTime();
            Timestamp endTime = upgradeRecords.get(i).getEndTime();
            if (mac != null) {
                upgradeRecords.get(i).setMac(MacUtils.convertMacToDisplayFormat(mac, macRule));
            }
            if (startTime != null) {
                upgradeRecords.get(i).setStartTimeString(sdf.format(startTime));
            }
            upgradeRecords.get(i).setStatusString(
                    getNetworkResourceManager().getString(map.get(upgradeRecords.get(i).getStatus())));
            if (endTime != null) {
                upgradeRecords.get(i).setEndTimeString(sdf.format(endTime));
            }
        }
        json.put("data", upgradeRecords);
        json.put("rowCount", upgradeRecordService.getUpgradeRecordNum(upgradeRecordQueryParam));
        writeDataToAjax(JSONObject.fromObject(json));
        return NONE;
    }

    private ResourceManager getNetworkResourceManager() {
        return ResourceManager.getResourceManager("com.topvision.ems.network.resources");
    }

    // TODO
    public String deleteUpgradeRecord() {
        return null;
    }

    public String getEntityIds() {
        return entityIds;
    }

    public void setEntityIds(String entityIds) {
        this.entityIds = entityIds;
    }

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public String getManageIp() {
        return manageIp;
    }

    public void setManageIp(String manageIp) {
        this.manageIp = manageIp;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

    public Long getTypeId() {
        return typeId;
    }

    public void setTypeId(Long typeId) {
        this.typeId = typeId;
    }

    public String getUpLinkEntityName() {
        return upLinkEntityName;
    }

    public void setUpLinkEntityName(String upLinkEntityName) {
        this.upLinkEntityName = upLinkEntityName;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
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

    public JSONArray getUpgradeStatus() {
        return upgradeStatus;
    }

    public void setUpgradeStatus(JSONArray upgradeStatus) {
        this.upgradeStatus = upgradeStatus;
    }

    public String getUplinkDevice() {
        return uplinkDevice;
    }

    public void setUplinkDevice(String uplinkDevice) {
        this.uplinkDevice = uplinkDevice;
    }

}
