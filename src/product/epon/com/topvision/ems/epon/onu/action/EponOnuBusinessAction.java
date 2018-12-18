package com.topvision.ems.epon.onu.action;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.topvision.ems.epon.onu.domain.EponOnuBusinessInfo;
import com.topvision.ems.epon.onu.domain.OnuLinkThreshold;
import com.topvision.ems.epon.onu.service.EponOnuBusinessService;
import com.topvision.ems.epon.onu.service.OnuAssemblyService;
import com.topvision.ems.epon.onu.service.OnuLinkService;
import com.topvision.ems.epon.onu.service.OnuWanService;
import com.topvision.framework.common.MacUtils;
import com.topvision.framework.web.struts2.BaseAction;
import com.topvision.platform.domain.UserContext;
import com.topvision.platform.util.CurrentRequest;

@Controller("eponOnuBusinessAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class EponOnuBusinessAction extends BaseAction {

    private static final long serialVersionUID = 589529745130021104L;

    private String queryContent;
    private String onuName;
    private Integer status;
    private Integer ponId;
    private Integer slotId;
    private Long entityId;
    private Integer tagId;
    private String macAddress;
    private String onuPreType;
    private JSONArray onuLinkThresholdJson;

    @Autowired
    private EponOnuBusinessService eponOnuBusinessService;
    @Autowired
    private OnuAssemblyService onuAssemblyService;
    @Autowired
    private OnuWanService onuWanService;
    @Autowired
    private OnuLinkService onuLinkService;

    /**
     * 跳转epon业务视图
     * 
     * @return
     */
    public String showEponOnuBusinessView() {
        List<OnuLinkThreshold> onuLinkThresholdList = onuLinkService.queryOnuLinkThreshold();
        onuLinkThresholdJson = JSONArray.fromObject(onuLinkThresholdList);
        return SUCCESS;
    }

    /**
     * 查询epon业务信息
     * 
     * @return
     * @throws IOException
     */
    public String queryEponOnuBusinessList() throws IOException {
        Map<String, Object> queryMap = new HashMap<String, Object>();
        queryMap.put("start", start);
        queryMap.put("limit", limit);
        queryMap.put("sort", sort);
        queryMap.put("dir", dir);
        // mysql中下划线是特殊的，like的时候必须转义
        if (queryContent != null) {// 简单查询模式
            if (queryContent.contains("_")) {
                queryContent = queryContent.replace("_", "\\_");
            }
            queryMap.put("queryContent", queryContent);
            String formatQueryMac = MacUtils.formatQueryMac(queryContent);
            if (formatQueryMac.indexOf(":") == -1) {
                queryMap.put("queryMacWithoutSplit", formatQueryMac);
            }
            queryMap.put("queryContentMac", formatQueryMac);
        } else {// 高级查询模式
            if (onuName != null && !"".equals(onuName)) {
                // mysql中下划线是特殊的，like的时候必须转义
                if (onuName.contains("_")) {
                    onuName = onuName.replace("_", "\\_");
                }
                queryMap.put("onuName", onuName);
            }
            if (macAddress != null && !"".equals(macAddress)) {
                String formatQueryMac = MacUtils.formatQueryMac(macAddress);
                if (formatQueryMac.indexOf(":") == -1) {
                    queryMap.put("queryMacWithoutSplit", formatQueryMac);
                }
                queryMap.put("macAddress", formatQueryMac);
            }
            if (tagId != null && tagId != -1) {
                queryMap.put("tagId", tagId);
            }
            if (onuPreType != null && !"".equals(onuPreType) && !"-1".equals(onuPreType)) {
                queryMap.put("onuPreType", onuPreType);
            }
            if (entityId != null && entityId != -1) {
                queryMap.put("entityId", entityId);
            }
            if (slotId != null && slotId != -1) {
                queryMap.put("slotId", slotId);
            }
            if (ponId != null && ponId != -1) {
                queryMap.put("ponId", ponId);
            }
            if (status != null && status != -1) {
                queryMap.put("status", status);
            }
        }
        List<EponOnuBusinessInfo> eponOnuBusinessInfos = eponOnuBusinessService.queryEponOnuBusinessList(queryMap);
        UserContext uc = CurrentRequest.getCurrentUser();
        String displayRule = uc.getMacDisplayStyle();
        for (EponOnuBusinessInfo eponOnuBusinessInfo : eponOnuBusinessInfos) {
            String formatedMac = MacUtils.convertMacToDisplayFormat(eponOnuBusinessInfo.getOnuMac(), displayRule);
            eponOnuBusinessInfo.setOnuMac(formatedMac);
        }

        JSONObject json = new JSONObject();
        int count = eponOnuBusinessService.queryEponOnuBusinessCount(queryMap);
        json.put("rowCount", count);
        json.put("data", eponOnuBusinessInfos);
        writeDataToAjax(json);
        return NONE;
    }

    public String getQueryContent() {
        return queryContent;
    }

    public String getOnuName() {
        return onuName;
    }

    public Integer getStatus() {
        return status;
    }

    public Integer getPonId() {
        return ponId;
    }

    public Integer getSlotId() {
        return slotId;
    }

    public Long getEntityId() {
        return entityId;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public String getOnuPreType() {
        return onuPreType;
    }

    public void setQueryContent(String queryContent) {
        this.queryContent = queryContent;
    }

    public void setOnuName(String onuName) {
        this.onuName = onuName;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public void setPonId(Integer ponId) {
        this.ponId = ponId;
    }

    public void setSlotId(Integer slotId) {
        this.slotId = slotId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    public void setOnuPreType(String onuPreType) {
        this.onuPreType = onuPreType;
    }

    public Integer getTagId() {
        return tagId;
    }

    public void setTagId(Integer tagId) {
        this.tagId = tagId;
    }

    public JSONArray getOnuLinkThresholdJson() {
        return onuLinkThresholdJson;
    }

    public void setOnuLinkThresholdJson(JSONArray onuLinkThresholdJson) {
        this.onuLinkThresholdJson = onuLinkThresholdJson;
    }

}
