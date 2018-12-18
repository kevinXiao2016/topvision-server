/***********************************************************************
 * $Id: OltDhcpStatisticsAction.java,v1.0 2017年11月24日 上午9:29:49 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.oltdhcp.action;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.topvision.ems.epon.oltdhcp.facade.domain.TopOltDhcpStatisticsObjects;
import com.topvision.ems.epon.oltdhcp.facade.domain.TopOltPppoeStatisticsObjects;
import com.topvision.ems.epon.oltdhcp.service.OltDhcpStatisticsService;
import com.topvision.framework.web.struts2.BaseAction;

import net.sf.json.JSONObject;

/**
 * @author haojie
 * @created @2017年11月24日-上午9:29:49
 *
 */
@Controller("oltDhcpStatisticsAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class OltDhcpStatisticsAction extends BaseAction {
    private static final long serialVersionUID = 6462747965242703972L;
    private Long entityId;
    @Autowired
    private OltDhcpStatisticsService oltDhcpStatisticsService;

    /**
     * 跳转DHCP报文统计页面
     * 
     * @return
     */
    public String showOltDhcpStatistics() {
        return SUCCESS;
    }

    /**
     * 获取DHCP报文统计数据
     * 
     * @return
     */
    public String loadltDhcpStatistics() {
        TopOltDhcpStatisticsObjects dhcpStatistics = oltDhcpStatisticsService.getOltDhcpStatistics(entityId);
        writeDataToAjax(dhcpStatistics);
        return NONE;
    }

    /**
     * 清零DHCP报文统计数据
     * 
     * @return
     */
    public String clearOltDhcpStatistics() {
        oltDhcpStatisticsService.clearOltDhcpStatistics(entityId);
        return NONE;
    }

    /**
     * 刷新DHCP报文统计数据
     * 
     * @return
     */
    public String refreshOltDhcpStatistics() {
        oltDhcpStatisticsService.refreshOltDhcpStatistics(entityId);
        return NONE;
    }

    /**
     * 跳转PPPoE报文统计页面
     * 
     * @return
     */
    public String showOltPppoeStatistics() {
        return SUCCESS;
    }

    /**
     * 获取PPPoE报文统计数据
     * 
     * @return
     */
    public String loadOltPppoeStatistics() {
        TopOltPppoeStatisticsObjects pppoeStatistics = oltDhcpStatisticsService.getOltPppoeStatistics(entityId);
        writeDataToAjax(pppoeStatistics);
        return NONE;
    }

    /**
     * 清零PPPoE报文统计数据
     * 
     * @return
     */
    public String clearPppoeDhcpStatistics() {
        oltDhcpStatisticsService.clearOltPppoeStatistics(entityId);
        return NONE;
    }

    /**
     * 刷新PPPoE报文统计数据
     * 
     * @return
     */
    public String refreshOltPppoeStatistics() {
        oltDhcpStatisticsService.refreshOltPppoeStatistics(entityId);
        return NONE;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

}
