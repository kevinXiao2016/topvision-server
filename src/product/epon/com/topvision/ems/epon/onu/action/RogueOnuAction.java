/***********************************************************************
 * $Id: RogueOnuAction.java,v1.0 2017年6月17日 上午9:17:05 $
 * 
 * @author: lizongtian
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.onu.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.topvision.ems.epon.onu.domain.OltOnuAttribute;
import com.topvision.ems.epon.onu.service.RogueOnuService;
import com.topvision.ems.epon.utils.OnuTypeConvertor;
import com.topvision.ems.gpon.onuauth.GponConstant;
import com.topvision.framework.common.MacUtils;
import com.topvision.framework.utils.EponConstants;
import com.topvision.framework.utils.EponIndex;
import com.topvision.framework.web.struts2.BaseAction;

/**
 * @author lizongtian
 * @created @2017年6月17日-上午9:17:05
 *
 */
@Controller("rogueOnuAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class RogueOnuAction extends BaseAction {

    private static final long serialVersionUID = -3610487356966645625L;

    @Autowired
    private RogueOnuService rogueOnuService;
    private Long entityId;
    private Long slotId;
    private Long ponId;
    private Long onuId;
    private Integer onuLaser;
    private String queryContent;
    private Integer systemRogueCheck;
    private Integer ponRogueSwitch;
    private Long onuIndex;

    public String setOltRogueCheck() {
        rogueOnuService.modifySystemRogueCheck(entityId, systemRogueCheck);
        return NONE;
    }

    public String modifyPonRogueSwitch() {
        rogueOnuService.modifyPonPortRogueSwitch(entityId, ponId, ponRogueSwitch);
        return NONE;
    }

    public String showRogueOnuList() {
        return SUCCESS;
    }

    public String loadRogueOnuList() {
        JSONObject json = new JSONObject();
        Map<String, Object> queryMap = new HashMap<String, Object>();
        queryMap.put("start", start);
        queryMap.put("limit", limit);
        queryMap.put("sort", sort);
        queryMap.put("dir", dir);
        if (entityId != null && entityId > 0) {
            queryMap.put("entityId", entityId);
        }
        if (slotId != null && slotId > 0) {
            queryMap.put("slotId", slotId);
        }
        if (ponId != null && ponId > 0) {
            queryMap.put("ponId", ponId);
        }
        if (queryContent != null) {//简单查询模式
            if (queryContent.contains("_")) {
                queryContent = queryContent.replace("_", "\\_");
            }
            queryMap.put("queryContent", queryContent);
            String formatQueryMac = MacUtils.formatQueryMac(queryContent);
            if (formatQueryMac.indexOf(":") == -1) {
                queryMap.put("queryMacWithoutSplit", formatQueryMac);
            }
            queryMap.put("queryContentMac", formatQueryMac);
        }
        List<OltOnuAttribute> onuList = rogueOnuService.loadRogueOnuList(queryMap);
        for (OltOnuAttribute onuAttribute : onuList) {
            //对其它厂商的ONU处理,其它厂商的ONU类型是对字符串进行ascii码转换后的数字
            if (onuAttribute.getOnuPreType() > EponConstants.UNKNOWN_ONU_TYPE
                    && onuAttribute.getTypeId().intValue() == OltOnuAttribute.ONU_ENTITYTYPE_OTHERCORP) {
                onuAttribute.setOnuTypeString(OnuTypeConvertor.convertTypeName(onuAttribute.getOnuPreType()));
            }
            if (GponConstant.GPON_ONU.equals(onuAttribute.getOnuEorG())) {
                String[] unqStr = onuAttribute.getUniqueId().split(":");
                String uniStr = "";
                if (unqStr.length == 8) {
                    for (int i = 0; i < 4; i++) {
                        uniStr += (char) (Integer.parseInt(unqStr[i], 16));
                    }
                }
                onuAttribute.setUniqueId(onuAttribute.getUniqueId().replaceAll(":", "") + "(" + uniStr + ")");
            }
        }
        int count = rogueOnuService.queryRogueOnuCount(queryMap);
        json.put("rowCount", count);
        json.put("data", onuList);
        writeDataToAjax(json);
        return NONE;
    }

    public String rogueOnuCheckList() {
        ArrayList<Integer> onuNos = new ArrayList<Integer>();
        onuNos.add(EponIndex.getOnuNo(onuIndex).intValue());
        rogueOnuService.ponPortRogueOnuCheck(entityId, ponId, onuNos);
        return NONE;
    }

    public String modifyOnuLaserSwitch() {
        rogueOnuService.modifyOnuLaser(entityId, onuId, onuLaser);
        return NONE;
    }

    public Integer getSystemRogueCheck() {
        return systemRogueCheck;
    }

    public void setSystemRogueCheck(Integer systemRogueCheck) {
        this.systemRogueCheck = systemRogueCheck;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public Long getPonId() {
        return ponId;
    }

    public void setPonId(Long ponId) {
        this.ponId = ponId;
    }

    public Integer getPonRogueSwitch() {
        return ponRogueSwitch;
    }

    public void setPonRogueSwitch(Integer ponRogueSwitch) {
        this.ponRogueSwitch = ponRogueSwitch;
    }

    public Long getOnuId() {
        return onuId;
    }

    public void setOnuId(Long onuId) {
        this.onuId = onuId;
    }

    public Integer getOnuLaser() {
        return onuLaser;
    }

    public void setOnuLaser(Integer onuLaser) {
        this.onuLaser = onuLaser;
    }

    public Long getSlotId() {
        return slotId;
    }

    public void setSlotId(Long slotId) {
        this.slotId = slotId;
    }

    public String getQueryContent() {
        return queryContent;
    }

    public void setQueryContent(String queryContent) {
        this.queryContent = queryContent;
    }

    public Long getOnuIndex() {
        return onuIndex;
    }

    public void setOnuIndex(Long onuIndex) {
        this.onuIndex = onuIndex;
    }

}
