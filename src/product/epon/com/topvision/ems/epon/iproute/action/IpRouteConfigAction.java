/***********************************************************************
 * $Id: IpRouteConfigAction.java,v1.0 2013-11-21 下午7:20:19 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.iproute.action;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.topvision.ems.epon.iproute.domain.IpRoute;
import com.topvision.ems.epon.iproute.domain.StaticIpRouteConfig;
import com.topvision.ems.epon.iproute.service.IpRouteService;
import com.topvision.framework.domain.IpsAddress;
import com.topvision.framework.web.struts2.BaseAction;

/**
 * @author flack
 * @created @2013-11-21-下午7:20:19
 *
 */
@Controller("ipRouteConfigAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class IpRouteConfigAction extends BaseAction {
    private static final long serialVersionUID = 5106132473091660641L;

    private Long entityId;
    private String destIpAddr;
    private String destMaskAddr;
    private String nextHopIndex;
    private Integer ipRouteDistance;
    private Integer ipRouteTrack;
    private int start;
    private int limit;

    @Autowired
    private IpRouteService ipRouteService;

    /**
     * 显示IpRoute配置页面
     * @return
     */
    public String showIpRouteConfig() {
        return SUCCESS;
    }

    /**
     * 加载IpRoute数据
     * @return
     * @throws IOException 
     */
    public String loadIpRouteList() throws IOException {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("entityId", entityId);
        param.put("start", start);
        param.put("limit", limit);
        param.put("routeType", IpRoute.STATIC_IPROUTE);
        List<IpRoute> ipRouteList = ipRouteService.getIpRouteList(param);
        int totalCount = ipRouteService.getIpRouteCount(param);
        JSONObject json = new JSONObject();
        json.put("rowCount", totalCount);
        json.put("data", ipRouteList);
        json.write(response.getWriter());
        return NONE;
    }

    /**
     * 添加IpRoute配置
     * @return
     */
    public String addIpRouteConfig() {
        StaticIpRouteConfig ipRouteConfig = new StaticIpRouteConfig();
        ipRouteConfig.setStaticRouteDstIpAddrIndex(new IpsAddress(destIpAddr));
        ipRouteConfig.setStaticRouteDstIpMaskIndex(new IpsAddress(destMaskAddr));
        ipRouteConfig.setStaticRouteNextHopIndex(new IpsAddress(nextHopIndex));
        ipRouteConfig.setStaticRouteDistance(ipRouteDistance);
        if (ipRouteTrack != StaticIpRouteConfig.DEFAULT) {
            ipRouteConfig.setStaticRouteTrack(ipRouteTrack);
        }
        ipRouteService.addIpRouteConfig(entityId, ipRouteConfig);
        return NONE;
    }

    /**
     * 修改静态路由
     */
    public String modifyIpRouteConfig() {
        StaticIpRouteConfig ipRouteConfig = new StaticIpRouteConfig();
        ipRouteConfig.setStaticRouteDstIpAddrIndex(new IpsAddress(destIpAddr));
        ipRouteConfig.setStaticRouteDstIpMaskIndex(new IpsAddress(destMaskAddr));
        ipRouteConfig.setStaticRouteNextHopIndex(new IpsAddress(nextHopIndex));
        ipRouteConfig.setStaticRouteDistance(ipRouteDistance);
        ipRouteConfig.setStaticRouteTrack(ipRouteTrack);
        ipRouteService.modifyIpRouteConfig(entityId, ipRouteConfig);
        return NONE;
    }

    /**
     * 删除IpRoute配置
     * @return
     */
    public String deleteIpRouteConfig() {
        StaticIpRouteConfig ipRouteConfig = new StaticIpRouteConfig();
        ipRouteConfig.setStaticRouteDstIpAddrIndex(new IpsAddress(destIpAddr));
        ipRouteConfig.setStaticRouteDstIpMaskIndex(new IpsAddress(destMaskAddr));
        ipRouteConfig.setStaticRouteNextHopIndex(new IpsAddress(nextHopIndex));
        ipRouteService.deleteIpRouteConfig(entityId, ipRouteConfig);
        return NONE;
    }

    /**
     * 从设备刷新IpRoute数据
     * @return
     */
    public String refreshIpRouteConfig() {
        //ipRouteService.refreshIpRoute(entityId);
        ipRouteService.refreshStaticIpRoute(entityId);
        return NONE;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public String getDestIpAddr() {
        return destIpAddr;
    }

    public void setDestIpAddr(String destIpAddr) {
        this.destIpAddr = destIpAddr;
    }

    public String getDestMaskAddr() {
        return destMaskAddr;
    }

    public void setDestMaskAddr(String destMaskAddr) {
        this.destMaskAddr = destMaskAddr;
    }

    public String getNextHopIndex() {
        return nextHopIndex;
    }

    public void setNextHopIndex(String nextHopIndex) {
        this.nextHopIndex = nextHopIndex;
    }

    public Integer getIpRouteDistance() {
        return ipRouteDistance;
    }

    public void setIpRouteDistance(Integer ipRouteDistance) {
        this.ipRouteDistance = ipRouteDistance;
    }

    public Integer getIpRouteTrack() {
        return ipRouteTrack;
    }

    public void setIpRouteTrack(Integer ipRouteTrack) {
        this.ipRouteTrack = ipRouteTrack;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }
}
