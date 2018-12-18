/***********************************************************************
 * $Id: Cmc_bRouteAction.java,v1.0 2013-8-6 下午4:40:27 $
 * 
 * @author: dosion
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.ccmts.action;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.topvision.ems.cmc.ccmts.domain.ChannelPerfInfo;
import com.topvision.ems.cmc.ccmts.service.CmcChannelService;
import com.topvision.ems.cmc.downchannel.service.CmcDownChannelService;
import com.topvision.framework.web.struts2.BaseAction;

/**
 * @author dosion
 * @created @2013-8-6-下午4:40:27
 * 
 */
@Controller("cmcChannelAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class CmcChannelAction extends BaseAction {
    private static final long serialVersionUID = 4259869100676362331L;
    private final Logger logger = LoggerFactory.getLogger(CmcChannelAction.class);
    @Autowired
    private CmcChannelService cmcChannelService;
    @Autowired
    private CmcDownChannelService cmcDownChannelService;
    private Long cmcId;
    private List<Long> channelIndexs;
    private Integer channelId;
    private Integer channelAdminstatus;

    /**
     * 取得portal中用到的端口列表信息
     * 
     * @return String
     */
    public String getCmcPortInfo() {
        Map<String, Object> json = new HashMap<String, Object>();
        List<ChannelPerfInfo> cmcChannelPerfInfoList = null;
        try {
            cmcChannelPerfInfoList = cmcChannelService.getCmcChannelPerfInfoList(cmcId);
            Iterator<ChannelPerfInfo> it = cmcChannelPerfInfoList.iterator();
            while (it.hasNext()) {
                if (it.next().getIfOperStatus().intValue() == 2) {
                    //it.remove();
                }
            }
        } catch (Exception ex) {
            logger.debug("", ex);
        }
        json.put("data", cmcChannelPerfInfoList);
        writeDataToAjax(JSONObject.fromObject(json));
        return NONE;
    }

    /**
     * 批量开启信道 added by huangdongsheng
     * 
     * @return
     */
    public String batchOpenChannels() {
        Map<String, Object> json = new HashMap<String, Object>();
        int status = 1;
        if (channelAdminstatus != null && channelAdminstatus == 3) {
            status = 3;//IPQAM
            String message = cmcDownChannelService.setChannelsAdminStatus(cmcId, channelId, status);
            json.put("message", message);
        } else {
            // 批量开启DOCSIS信道
            List<Long> failerList = cmcChannelService.batchChangeChannelAdminstatus(cmcId, channelIndexs, status);
            if (failerList == null || failerList.size() == 0) {
                json.put("message", "success");
            } else {
                json.put("message", "fail");
                json.put("failureList", failerList);
            }
        }

        writeDataToAjax(JSONObject.fromObject(json));
        return NONE;
    }

    /**
     * 批量关闭信道 added by huangdongsheng
     * @return
     */
    public String batchCloseChannels() {
        // 批量关闭信道
        List<Long> failerList = cmcChannelService.batchChangeChannelAdminstatus(cmcId, channelIndexs, 2);
        Map<String, Object> json = new HashMap<String, Object>();
        if (failerList == null || failerList.size() == 0) {
            json.put("message", "success");
        } else {
            json.put("message", "fail");
            json.put("failureList", failerList);
        }
        writeDataToAjax(JSONObject.fromObject(json));
        return NONE;
    }

    public Long getCmcId() {
        return cmcId;
    }

    public void setCmcId(Long cmcId) {
        this.cmcId = cmcId;
    }

    public List<Long> getChannelIndexs() {
        return channelIndexs;
    }

    public void setChannelIndexs(List<Long> channelIndexs) {
        this.channelIndexs = channelIndexs;
    }

    /**
     * @return the channelId
     */
    public Integer getChannelId() {
        return channelId;
    }

    /**
     * @param channelId the channelId to set
     */
    public void setChannelId(Integer channelId) {
        this.channelId = channelId;
    }

    /**
     * @return the channelAdminstatus
     */
    public Integer getChannelAdminstatus() {
        return channelAdminstatus;
    }

    /**
     * @param channelAdminstatus the channelAdminstatus to set
     */
    public void setChannelAdminstatus(Integer channelAdminstatus) {
        this.channelAdminstatus = channelAdminstatus;
    }

}
