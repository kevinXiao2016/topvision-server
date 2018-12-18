/***********************************************************************
 * $Id: DispersionAction.java,v1.0 2015-3-12 上午11:30:55 $
 * 
 * @author: Fanzidong
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cm.dispersion.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.topvision.ems.cm.dispersion.domain.Dispersion;
import com.topvision.ems.cm.dispersion.service.DispersionService;
import com.topvision.ems.facade.domain.EntityType;
import com.topvision.ems.report.service.StatReportService;
import com.topvision.ems.template.service.EntityTypeService;
import com.topvision.framework.web.struts2.BaseAction;
import com.topvision.platform.domain.UserContext;

/**
 * @author fanzidong
 * @created @2015-3-12-上午11:30:55
 * 
 */
@Controller("dispersionAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class DispersionAction extends BaseAction {

    private static final long serialVersionUID = -4973548323924738419L;

    @Autowired
    private DispersionService dispersionService;
    @Autowired
    private EntityTypeService entityTypeService;
    @Autowired
    private StatReportService statReportService;

    private JSONArray entityTypes = new JSONArray();
    
    private JSONObject opticalNode;

    private Integer deviceType;
    private String deviceName;
    private String manageIp;
    private Long opticalNodeId;
    private String startTime;
    private String endTime;
    private String exactTime;
    private List<Long> folderIds;

    /**
     * 展示离散度列表页
     * 
     * @return
     */
    public String showDispersion() {
        UserContext uc = (UserContext) super.getSession().get(UserContext.KEY);
        List<EntityType> entityTypeList = new ArrayList<EntityType>();
        if (uc.hasSupportModule("cmc")) {
            Long cmcType = entityTypeService.getCcmtsType();
            entityTypeList = entityTypeService.loadSubType(cmcType);
        }
        setEntityTypes(JSONArray.fromObject(entityTypeList));
        return SUCCESS;
    }

    /**
     * 加载离散度列表
     * 
     * @return
     * @throws IOException
     */
    public String loadDispersions() throws IOException {
        // 封装查询条件
        Map<String, Object> queryMap = new HashMap<String, Object>();
        queryMap.put("start", start);
        queryMap.put("limit", limit);
        if(sort!=null){
            queryMap.put("sort", sort);
        }
        if(dir!=null){
            queryMap.put("dir", dir);
        }
        if (deviceType != null && !deviceType.equals(-1)) {
            queryMap.put("deviceType", deviceType);
        }
        if (deviceName != null && !deviceName.equals("")) {
            queryMap.put("deviceName", deviceName);
        }
        if (manageIp != null && !manageIp.equals("")) {
            queryMap.put("manageIp", manageIp);
        }
        if (folderIds != null && folderIds.size() > 0) {
            List<Long> authFolderIds = statReportService.getAuthFolderIds(folderIds);
            String folderStr = "";
            for (int i = 0; i < authFolderIds.size(); i++) {
                if (i + 1 == authFolderIds.size()) {
                    folderStr += authFolderIds.get(i).toString();
                } else {
                    folderStr += authFolderIds.get(i).toString() + ",";
                }
            }
            queryMap.put("folderIds", folderStr);
        }

        // 查询分页数据和总个数
        List<Dispersion> dispersions = dispersionService.getDispersionList(queryMap);
        Integer dispersionCount = dispersionService.getDispersionListNum(queryMap);

        // 返回数据
        JSONObject json = new JSONObject();
        json.put("data", dispersions);
        json.put("rowCount", dispersionCount);
        json.write(response.getWriter());
        return NONE;
    }

    /**
     * 打开指定光节点离散度详细页
     * 
     * @return
     */
    public String showDispersionDetail() {
        //获得该光节点的基本信息
        Dispersion dispersion = dispersionService.getDispersionById(opticalNodeId);
        opticalNode = JSONObject.fromObject(dispersion);
        return SUCCESS;
    }

    /**
     * 加载指定光节点指定时间断内的离散度历史数据
     * 
     * @return
     * @throws IOException
     */
    public String loadDispersionTrend() throws IOException {
        List<Dispersion> dispersions = dispersionService.selectDispersionsByIdAndRange(opticalNodeId, startTime,
                endTime);
        JSONArray.fromObject(dispersions).write(response.getWriter());
        return NONE;
    }

    /**
     * 获取指定光节点在某时刻的离散度数据
     * 
     * @return
     * @throws IOException
     */
    public String loadDispersionByIdAndTime() throws IOException {
        Dispersion dispersion = dispersionService.loadDispersionByIdAndTime(opticalNodeId, exactTime);
        JSONObject.fromObject(dispersion).write(response.getWriter());
        return NONE;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getManageIp() {
        return manageIp;
    }

    public void setManageIp(String manageIp) {
        this.manageIp = manageIp;
    }

    public JSONArray getEntityTypes() {
        return entityTypes;
    }

    public void setEntityTypes(JSONArray entityTypes) {
        this.entityTypes = entityTypes;
    }

    public Integer getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(Integer deviceType) {
        this.deviceType = deviceType;
    }

    public Long getOpticalNodeId() {
        return opticalNodeId;
    }

    public void setOpticalNodeId(Long opticalNodeId) {
        this.opticalNodeId = opticalNodeId;
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

    public String getExactTime() {
        return exactTime;
    }

    public void setExactTime(String exactTime) {
        this.exactTime = exactTime;
    }

    public List<Long> getFolderIds() {
        return folderIds;
    }

    public void setFolderIds(List<Long> folderIds) {
        this.folderIds = folderIds;
    }

    public JSONObject getOpticalNode() {
        return opticalNode;
    }

    public void setOpticalNode(JSONObject opticalNode) {
        this.opticalNode = opticalNode;
    }

}
