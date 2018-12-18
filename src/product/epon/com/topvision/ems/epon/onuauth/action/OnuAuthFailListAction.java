/***********************************************************************
 * $Id: OnuAuthFailListAction.java,v1.0 2015年4月18日 上午9:53:17 $
 * 
 * @author: loyal
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.onuauth.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.topvision.ems.epon.olt.action.AbstractEponAction;
import com.topvision.ems.epon.onuauth.domain.OnuAuthFail;
import com.topvision.ems.epon.onuauth.service.OnuAuthFailListService;
import com.topvision.framework.common.MacUtils;
import com.topvision.platform.domain.UserContext;

/**
 * @author loyal
 * @created @2015年4月18日-上午9:53:17
 * 
 */
@Controller("onuAuthFailListAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class OnuAuthFailListAction extends AbstractEponAction {
    private static final long serialVersionUID = 1544961564751453285L;
    private String name;
    private Long entityId;
    private String mac;
    private Long slotId;
    private Long ponId;
    @Autowired
    private OnuAuthFailListService onuAuthFailListService;

    public String getOnuAuthFailList() throws IOException {
        List<OnuAuthFail> onuAuthFailList = new ArrayList<OnuAuthFail>();
        Map<String, Object> queryMap = new HashMap<String, Object>();
        Map<String, Object> json = new HashMap<String, Object>();
        UserContext uc = (UserContext) super.getSession().get(UserContext.KEY);
        String macRule = uc.getMacDisplayStyle();
        queryMap.put("start", start);
        queryMap.put("limit", limit);
        if (entityId != null && entityId > 0) {
            queryMap.put("entityId", entityId);
        }
        if (slotId != null && slotId > 0) {
            queryMap.put("slotId", slotId);
        }
        if (ponId != null && ponId > 0) {
            queryMap.put("ponId", ponId);
        }
        if (mac != null && !"".equals(mac.trim())) {
            String formatQueryMac = MacUtils.formatQueryMac(mac);
            if (formatQueryMac.indexOf(":") == -1) {
                queryMap.put("queryMacWithoutSplit", formatQueryMac);
            }
            queryMap.put("mac", formatQueryMac);
        }
        onuAuthFailList = onuAuthFailListService.getOnuAuthFailList(queryMap);
        for (int i = 0; i < onuAuthFailList.size(); i++) {
            String mac = onuAuthFailList.get(i).getMac();
            if (mac != null) {
                onuAuthFailList.get(i).setMac(
                        MacUtils.convertMacToDisplayFormat(mac, macRule));
            }
        }
        Long rowCount = onuAuthFailListService.getOnuAuthFailCount(queryMap);
        json.put("data", onuAuthFailList);
        json.put("rowCount", rowCount);
        writeDataToAjax(JSONObject.fromObject(json));
        return NONE;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public Long getSlotId() {
        return slotId;
    }

    public void setSlotId(Long slotId) {
        this.slotId = slotId;
    }

    public Long getPonId() {
        return ponId;
    }

    public void setPonId(Long ponId) {
        this.ponId = ponId;
    }

}
