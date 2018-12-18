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
import com.topvision.ems.epon.onu.domain.OnuLinkInfo;
import com.topvision.ems.epon.onu.domain.OnuLinkThreshold;
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
 * 
 * @author CWQ
 * @created @2017年12月22日-上午9:06:47
 *
 */
@Controller("onuLinkAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class OnuLinkAction extends BaseAction {

    private static final long serialVersionUID = 1364655041446425079L;
    private String queryContent;
    private String onuName;
    private String macAddress;
    private String onuEorG;
    private String onuPreType;
    private Long entityId;
    private Integer slotId;
    private Integer ponId;
    private Integer status;
    private Integer receiveMin;
    private Integer receiveMax;
    private Integer transmitMin;
    private Integer transmitMax;
    private Integer ponRecvMin;
    private Integer ponRecvMax;
    private String onuEnvironment;// ONU 环境
    private JSONArray onuLinkThresholdJson;

    @Autowired
    private OnuLinkService onuLinkService;
    @Autowired
    private OnuService onuService;

    public String showOnuLinkView() {
        List<OnuLinkThreshold> onuLinkThresholdList = onuLinkService.queryOnuLinkThreshold();
        onuLinkThresholdJson = JSONArray.fromObject(onuLinkThresholdList);
        onuEnvironment = onuService.queryOnuEnvi(new HashMap<String, Object>()).getPattern();
        return SUCCESS;
    }

    public String queryOnuLinkList() throws IOException {
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
            if (receiveMin != null) {
                queryMap.put("receiveMin", receiveMin);
            }
            if (receiveMax != null) {
                queryMap.put("receiveMax", receiveMax);
            }
            if (transmitMin != null) {
                queryMap.put("transmitMin", transmitMin);
            }
            if (transmitMax != null) {
                queryMap.put("transmitMax", transmitMax);
            }
            if (ponRecvMin != null) {
                queryMap.put("ponRecvMin", ponRecvMin);
            }
            if (ponRecvMax != null) {
                queryMap.put("ponRecvMax", ponRecvMax);
            }
        }

        List<OnuLinkInfo> onuLinkList = onuLinkService.queryOnuLinkList(queryMap);
        JSONObject json = new JSONObject();
        UserContext uc = CurrentRequest.getCurrentUser();
        String displayRule = uc.getMacDisplayStyle();
        for (OnuLinkInfo onuLink : onuLinkList) {
            String formatedMac = MacUtils.convertMacToDisplayFormat(onuLink.getOnuMac(), displayRule);
            onuLink.setOnuMac(formatedMac);
            // 对其它厂商的ONU处理,其它厂商的ONU类型是对字符串进行ascii码转换后的数字
            if (onuLink.getOnuPreType() > EponConstants.UNKNOWN_ONU_TYPE
                    && onuLink.getTypeId().intValue() == OltOnuAttribute.ONU_ENTITYTYPE_OTHERCORP) {
                onuLink.setTypeName(OnuTypeConvertor.convertTypeName(onuLink.getOnuPreType()));
            }
            if (GponConstant.GPON_ONU.equals(onuLink.getOnuEorG())) {
                String[] unqStr = onuLink.getOnuUniqueIdentification().split(":");
                String uniStr = "";
                if (unqStr.length == 8) {
                    for (int i = 0; i < 4; i++) {
                        uniStr += (char) (Integer.parseInt(unqStr[i], 16));
                    }
                }
                onuLink.setOnuUniqueIdentification(onuLink.getOnuUniqueIdentification().replaceAll(":", "") + "("
                        + uniStr + ")");
            }

            if (Integer.valueOf(EponConstants.ADMIN_STATUS_ENABLE).equals(onuLink.getOnuOperationStatus())) {
                String onuRunTime = DateUtils.getTimePeriod(System.currentTimeMillis()
                        - onuLink.getChangeTime().getTime() + +onuLink.getOnuTimeSinceLastRegister() * 1000, uc
                        .getUser().getLanguage());
                onuLink.setOnuRunTime(onuRunTime);
            } else {
                String onuRunTime = resourceManager.getNotNullString("deviceInfo.offline");
                if (onuLink.getLastDeregisterTime() != null) {
                    String offlineInfo = "("
                            + DateUtils.getTimeDesInObscure(System.currentTimeMillis()
                                    - onuLink.getLastDeregisterTime().getTime(), uc.getUser().getLanguage()) + ")";
                    onuRunTime = onuRunTime + offlineInfo;
                }
                onuLink.setOnuRunTime(onuRunTime);
            }
        }
        Integer count = onuLinkService.queryOnuLinkListCount(queryMap);
        json.put("rowCount", count);
        json.put("data", onuLinkList);
        writeDataToAjax(json);
        return NONE;
    }

    public Integer getSlotId() {
        return slotId;
    }

    public void setSlotId(Integer slotId) {
        this.slotId = slotId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getOnuEorG() {
        return onuEorG;
    }

    public void setOnuEorG(String onuEorG) {
        this.onuEorG = onuEorG;
    }

    public String getOnuName() {
        return onuName;
    }

    public void setOnuName(String onuName) {
        this.onuName = onuName;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    public String getQueryContent() {
        return queryContent;
    }

    public void setQueryContent(String queryContent) {
        this.queryContent = queryContent;
    }

    public String getOnuPreType() {
        return onuPreType;
    }

    public void setOnuPreType(String onuPreType) {
        this.onuPreType = onuPreType;
    }

    public Integer getPonId() {
        return ponId;
    }

    public void setPonId(Integer ponId) {
        this.ponId = ponId;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public Integer getReceiveMin() {
        return receiveMin;
    }

    public void setReceiveMin(Integer receiveMin) {
        this.receiveMin = receiveMin;
    }

    public Integer getReceiveMax() {
        return receiveMax;
    }

    public void setReceiveMax(Integer receiveMax) {
        this.receiveMax = receiveMax;
    }

    public Integer getTransmitMin() {
        return transmitMin;
    }

    public void setTransmitMin(Integer transmitMin) {
        this.transmitMin = transmitMin;
    }

    public Integer getTransmitMax() {
        return transmitMax;
    }

    public void setTransmitMax(Integer transmitMax) {
        this.transmitMax = transmitMax;
    }

    public Integer getPonRecvMin() {
        return ponRecvMin;
    }

    public void setPonRecvMin(Integer ponRecvMin) {
        this.ponRecvMin = ponRecvMin;
    }

    public Integer getPonRecvMax() {
        return ponRecvMax;
    }

    public void setPonRecvMax(Integer ponRecvMax) {
        this.ponRecvMax = ponRecvMax;
    }

    public String getOnuEnvironment() {
        return onuEnvironment;
    }

    public void setOnuEnvironment(String onuEnvironment) {
        this.onuEnvironment = onuEnvironment;
    }

    public JSONArray getOnuLinkThresholdJson() {
        return onuLinkThresholdJson;
    }

    public void setOnuLinkThresholdJson(JSONArray onuLinkThresholdJson) {
        this.onuLinkThresholdJson = onuLinkThresholdJson;
    }

}
