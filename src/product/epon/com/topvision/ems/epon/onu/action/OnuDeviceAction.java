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

import com.topvision.ems.epon.onu.domain.OltOnuAttribute;
import com.topvision.ems.epon.onu.domain.OnuDeviceInfo;
import com.topvision.ems.epon.onu.domain.OnuLinkThreshold;
import com.topvision.ems.epon.onu.service.OnuDeviceService;
import com.topvision.ems.epon.onu.service.OnuLinkService;
import com.topvision.ems.epon.onu.service.OnuService;
import com.topvision.ems.epon.utils.OnuTypeConvertor;
import com.topvision.ems.gpon.onuauth.GponConstant;
import com.topvision.framework.common.DateUtils;
import com.topvision.framework.common.MacUtils;
import com.topvision.framework.utils.EponConstants;
import com.topvision.framework.web.struts2.BaseAction;
import com.topvision.platform.ResourceManager;
import com.topvision.platform.domain.UserContext;
import com.topvision.platform.util.CurrentRequest;

/**
 * ONU设备列表
 * 
 * @author w1992wishes
 * @created @2017年12月22日-上午9:06:47
 *
 */
@Controller("onuDeviceAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class OnuDeviceAction extends BaseAction {

    private static final long serialVersionUID = 8807942288352162696L;

    private String queryContent;
    private String onuName;
    private Integer status;
    private Integer ponId;
    private Integer slotId;
    private String onuEorG;
    private String onuPreType;
    private Long entityId;
    private String macAddress;
    private String onuEnvironment;// ONU 环境
    private JSONArray onuLinkThresholdJson;

    @Autowired
    private OnuDeviceService onuDeviceService;
    @Autowired
    private OnuService onuService;
    @Autowired
    private OnuLinkService onuLinkService;

    public String showOnuDeviceView() throws Exception {
        List<OnuLinkThreshold> onuLinkThresholdList = onuLinkService.queryOnuLinkThreshold();
        onuLinkThresholdJson = JSONArray.fromObject(onuLinkThresholdList);
        onuEnvironment = onuService.queryOnuEnvi(new HashMap<String, Object>()).getPattern();
        return SUCCESS;
    }

    public String queryOnuDeviceList() throws IOException {
        ResourceManager resourceManager = ResourceManager.getResourceManager("com.topvision.ems.resources.resources");
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
            if (onuEorG != null && onuEorG.length() != 0) {
                queryMap.put("onuEorG", onuEorG);
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
        List<OnuDeviceInfo> onuDeviceInfos = onuDeviceService.queryOnuDeviceList(queryMap);
        JSONObject json = new JSONObject();
        UserContext uc = CurrentRequest.getCurrentUser();
        String displayRule = uc.getMacDisplayStyle();
        for (OnuDeviceInfo onuDeviceInfo : onuDeviceInfos) {
            String formatedMac = MacUtils.convertMacToDisplayFormat(onuDeviceInfo.getOnuMac(), displayRule);
            onuDeviceInfo.setOnuMac(formatedMac);
            // 对其它厂商的ONU处理,其它厂商的ONU类型是对字符串进行ascii码转换后的数字
            if (onuDeviceInfo.getOnuPreType() > EponConstants.UNKNOWN_ONU_TYPE
                    && onuDeviceInfo.getTypeId().intValue() == OltOnuAttribute.ONU_ENTITYTYPE_OTHERCORP) {
                onuDeviceInfo.setTypeName(OnuTypeConvertor.convertTypeName(onuDeviceInfo.getOnuPreType()));
            }
            if (GponConstant.GPON_ONU.equals(onuDeviceInfo.getOnuEorG())) {
                String[] unqStr = onuDeviceInfo.getOnuUniqueIdentification().split(":");
                String uniStr = "";
                if (unqStr.length == 8) {
                    for (int i = 0; i < 4; i++) {
                        uniStr += (char) (Integer.parseInt(unqStr[i], 16));
                    }
                }
                onuDeviceInfo.setOnuUniqueIdentification(onuDeviceInfo.getOnuUniqueIdentification().replaceAll(":", "")
                        + "(" + uniStr + ")");
            }

            if (Integer.valueOf(EponConstants.ADMIN_STATUS_ENABLE).equals(onuDeviceInfo.getOnuOperationStatus())) {
                String onuRunTime = DateUtils.getTimePeriod(System.currentTimeMillis()
                        - onuDeviceInfo.getChangeTime().getTime() + onuDeviceInfo.getOnuTimeSinceLastRegister() * 1000,
                        uc.getUser().getLanguage());
                onuDeviceInfo.setOnuRunTime(onuRunTime);
            } else {
                String onuRunTime = resourceManager.getNotNullString("deviceInfo.offline");
                if (onuDeviceInfo.getLastDeregisterTime() != null) {
                    String offlineInfo = "("
                            + DateUtils.getTimeDesInObscure(System.currentTimeMillis()
                                    - onuDeviceInfo.getLastDeregisterTime().getTime(), uc.getUser().getLanguage())
                            + ")";
                    onuRunTime = onuRunTime + offlineInfo;
                }
                onuDeviceInfo.setOnuRunTime(onuRunTime);
            }
        }
        int count = onuDeviceService.queryOnuDeviceCount(queryMap);
        json.put("rowCount", count);
        json.put("data", onuDeviceInfos);
        writeDataToAjax(json);
        return NONE;
    }

    public String getOnuEnvironment() {
        return onuEnvironment;
    }

    public void setOnuEnvironment(String onuEnvironment) {
        this.onuEnvironment = onuEnvironment;
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

    public String getOnuEorG() {
        return onuEorG;
    }

    public void setOnuEorG(String onuEorG) {
        this.onuEorG = onuEorG;
    }

    public String getOnuPreType() {
        return onuPreType;
    }

    public void setOnuPreType(String onuPreType) {
        this.onuPreType = onuPreType;
    }

    public JSONArray getOnuLinkThresholdJson() {
        return onuLinkThresholdJson;
    }

    public void setOnuLinkThresholdJson(JSONArray onuLinkThresholdJson) {
        this.onuLinkThresholdJson = onuLinkThresholdJson;
    }

}
