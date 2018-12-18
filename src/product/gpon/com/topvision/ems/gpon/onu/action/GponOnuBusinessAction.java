package com.topvision.ems.gpon.onu.action;

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

import com.topvision.ems.epon.onu.domain.OnuLinkThreshold;
import com.topvision.ems.epon.onu.service.OnuLinkService;
import com.topvision.ems.gpon.onu.domain.GponOnuBusinessInfo;
import com.topvision.ems.gpon.onu.service.GponOnuBusinessService;
import com.topvision.framework.common.MacUtils;
import com.topvision.framework.web.struts2.BaseAction;
import com.topvision.platform.domain.UserContext;
import com.topvision.platform.util.CurrentRequest;

/**
 * 
 * @author CWQ
 * @created @2017年12月25日-下午2:29:58
 *
 */
@Controller("gponOnuBusinessAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class GponOnuBusinessAction extends BaseAction {

    private static final long serialVersionUID = 6653277211116032953L;
    private String queryContent;
    private String onuName;
    private Integer ponId;
    private Integer slotId;
    private Long entityId;
    private String macAddress;
    private String onuPreType;
    private Integer status;
    private Integer tagId;
    private JSONArray onuLinkThresholdJson;
    @Autowired
    private GponOnuBusinessService gponOnuBusinessService;
    @Autowired
    private OnuLinkService onuLinkService;

    public String showGponOnuBusinessView() {
        List<OnuLinkThreshold> onuLinkThresholdList = onuLinkService.queryOnuLinkThreshold();
        onuLinkThresholdJson = JSONArray.fromObject(onuLinkThresholdList);
        return SUCCESS;
    }

    public String queryGponOnuBusinessList() throws IOException {
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

        List<GponOnuBusinessInfo> gponOnuBusinessInfoList = gponOnuBusinessService.queryGponOnuBusinessList(queryMap);
        JSONObject json = new JSONObject();
        // 需要在展示前格式化MAC地址
        UserContext uc = CurrentRequest.getCurrentUser();
        String displayRule = uc.getMacDisplayStyle();
        for (GponOnuBusinessInfo gponOnuBusinessInfo : gponOnuBusinessInfoList) {
            String formatedMac = MacUtils.convertMacToDisplayFormat(gponOnuBusinessInfo.getOnuMac(), displayRule);
            gponOnuBusinessInfo.setOnuMac(formatedMac);
            // GPON SN号转换
            String[] unqStr = gponOnuBusinessInfo.getOnuUniqueIdentification().split(":");
            String uniStr = "";
            if (unqStr.length == 8) {
                for (int i = 0; i < 4; i++) {
                    uniStr += (char) (Integer.parseInt(unqStr[i], 16));
                }
            }
            gponOnuBusinessInfo.setOnuUniqueIdentification(gponOnuBusinessInfo.getOnuUniqueIdentification().replaceAll(
                    ":", "")
                    + "(" + uniStr + ")");
        }
        Integer count = gponOnuBusinessService.queryGponOnuBusinessCount(queryMap);
        json.put("rowCount", count);
        json.put("data", gponOnuBusinessInfoList);
        writeDataToAjax(json);
        return NONE;
    }

    public String getQueryContent() {
        return queryContent;
    }

    public void setQueryContent(String queryContent) {
        this.queryContent = queryContent;
    }

    public String getOnuName() {
        return onuName;
    }

    public void setOnuName(String onuName) {
        this.onuName = onuName;
    }

    public Integer getPonId() {
        return ponId;
    }

    public void setPonId(Integer ponId) {
        this.ponId = ponId;
    }

    public Integer getSlotId() {
        return slotId;
    }

    public void setSlotId(Integer slotId) {
        this.slotId = slotId;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getOnuPreType() {
        return onuPreType;
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
