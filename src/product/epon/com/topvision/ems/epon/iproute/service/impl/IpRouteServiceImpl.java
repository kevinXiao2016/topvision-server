/***********************************************************************
 * $Id: IpRouteServiceImpl.java,v1.0 2013-11-16 下午03:32:06 $
 * 
 * @author: Rod John
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.iproute.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.epon.iproute.dao.IpRouteDao;
import com.topvision.ems.epon.iproute.domain.IpRoute;
import com.topvision.ems.epon.iproute.domain.IpRouteRecord;
import com.topvision.ems.epon.iproute.domain.StaticIpRouteConfig;
import com.topvision.ems.epon.iproute.facade.IpRouteFacade;
import com.topvision.ems.epon.iproute.service.IpRouteService;
import com.topvision.ems.network.service.EntityService;
import com.topvision.framework.common.IpUtils;
import com.topvision.framework.service.BaseService;
import com.topvision.framework.snmp.SnmpParam;
import com.topvision.platform.facade.FacadeFactory;
import com.topvision.platform.message.event.SynchronizedEvent;
import com.topvision.platform.message.event.SynchronizedListener;
import com.topvision.platform.message.service.MessageService;

/**
 * @author Rod John
 * @created @2013-11-16-下午03:32:06
 * 
 */
@Service("ipRouteService")
public class IpRouteServiceImpl extends BaseService implements IpRouteService, SynchronizedListener {
    @Autowired
    private MessageService messageService;
    @Autowired
    private IpRouteDao ipRouteDao;
    @Autowired
    private EntityService entityService;
    @Autowired
    private FacadeFactory facadeFactory;

    @Override
    @PostConstruct
    public void initialize() {
        super.initialize();
        messageService.addListener(SynchronizedListener.class, this);
    }

    @Override
    @PreDestroy
    public void destroy() {
        super.destroy();
    }

    @Override
    public void insertEntityStates(SynchronizedEvent event) {
        Long entityId = event.getEntityId();
        try {
            //refreshIpRoute(entityId);
            refreshStaticIpRoute(entityId);
            logger.info("refreshIpRoute finish");
        } catch (Exception e) {
            logger.error("refreshIpRoute wrong", e);
        }
    }

    @Override
    public void updateEntityStates(SynchronizedEvent event) {
    }

    @Override
    public void refreshIpRoute(Long entityId) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        List<IpRouteRecord> ipRouteRecords = getIpRouteFacade(snmpParam.getIpAddress()).getIpRouteRecords(snmpParam);
        ipRouteDao.refreshIpRoute(entityId, IpRoute.getIpRoutesFromRecord(entityId, ipRouteRecords));
    }

    @Override
    public void refreshStaticIpRoute(Long entityId) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        List<StaticIpRouteConfig> staticIpRoutes = this.getIpRouteFacade(snmpParam.getIpAddress()).getStaticIpRoute(
                snmpParam);
        List<IpRoute> ipRouteList = new ArrayList<IpRoute>();
        IpRoute ipRoute = null;
        for (StaticIpRouteConfig staticIp : staticIpRoutes) {
            ipRoute = new IpRoute();
            ipRoute.setEntityId(entityId);
            ipRoute.setIpAddressDst(staticIp.getStaticRouteDstIpAddrIndex().toString());
            ipRoute.setIpMaskDst(staticIp.getStaticRouteDstIpMaskIndex().toString());
            ipRoute.setNextHop(staticIp.getStaticRouteNextHopIndex().toString());
            ipRoute.setDistance(staticIp.getStaticRouteDistance());
            ipRoute.setTrack(staticIp.getStaticRouteTrack());
            ipRoute.setRouteType(IpRoute.STATIC_IPROUTE);
            ipRouteList.add(ipRoute);
        }
        ipRouteDao.refreshIpRoute(entityId, ipRouteList);
    }

    /**
     * 获取Facade对象
     * 
     * @param ip
     *            被采集设备IP
     * @return IpRouteFacade
     */
    private IpRouteFacade getIpRouteFacade(String ip) {
        return facadeFactory.getFacade(ip, IpRouteFacade.class);
    }

    @Override
    public void addIpRouteConfig(Long entityId, StaticIpRouteConfig ipRouteConfig) {
        //先添加到设备
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        StaticIpRouteConfig newIpRoute = this.getIpRouteFacade(snmpParam.getIpAddress()).addIpRouteConfig(snmpParam,
                ipRouteConfig);
        //再存入数据库
        IpRoute ipRoute = new IpRoute();
        ipRoute.setEntityId(entityId);
        //目的Ip以目的Ip掩码决定的网络地址为准
        ipRoute.setIpAddressDst(this.getIpByMask(newIpRoute.getStaticRouteDstIpAddrIndex().toString(), newIpRoute
                .getStaticRouteDstIpMaskIndex().toString()));
        ipRoute.setIpMaskDst(newIpRoute.getStaticRouteDstIpMaskIndex().toString());
        ipRoute.setNextHop(newIpRoute.getStaticRouteNextHopIndex().toString());
        ipRoute.setDistance(newIpRoute.getStaticRouteDistance());
        ipRoute.setTrack(newIpRoute.getStaticRouteTrack());
        ipRoute.setRouteType(IpRoute.STATIC_IPROUTE);
        ipRouteDao.insertIpRoute(ipRoute);
    }

    @Override
    public void deleteIpRouteConfig(Long entityId, StaticIpRouteConfig ipRouteConfig) {
        //先从设备删除记录
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        this.getIpRouteFacade(snmpParam.getIpAddress()).deleteIpRouteConfig(snmpParam, ipRouteConfig);
        //再从数据库删除数据
        IpRoute ipRoute = new IpRoute();
        ipRoute.setEntityId(entityId);
        ipRoute.setIpAddressDst(ipRouteConfig.getStaticRouteDstIpAddrIndex().toString());
        ipRoute.setIpMaskDst(ipRouteConfig.getStaticRouteDstIpMaskIndex().toString());
        ipRoute.setNextHop(ipRouteConfig.getStaticRouteNextHopIndex().toString());
        ipRoute.setRouteType(IpRoute.STATIC_IPROUTE);
        ipRouteDao.deleteIpRoute(ipRoute);
    }

    @Override
    public List<IpRoute> getIpRouteList(Map<String, Object> map) {
        return ipRouteDao.queryIpRouteList(map);
    }

    @Override
    public int getIpRouteCount(Map<String, Object> map) {
        return ipRouteDao.queryIpRouteCount(map);
    }

    @Override
    public void modifyIpRouteConfig(Long entityId, StaticIpRouteConfig ipRouteConfig) {
        //先更改设备数据
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        StaticIpRouteConfig newIpRoute = this.getIpRouteFacade(snmpParam.getIpAddress()).updateIpRouteConfig(snmpParam,
                ipRouteConfig);
        //再更改数据库数据记录
        IpRoute ipRoute = new IpRoute();
        ipRoute.setEntityId(entityId);
        ipRoute.setIpAddressDst(newIpRoute.getStaticRouteDstIpAddrIndex().toString());
        ipRoute.setIpMaskDst(newIpRoute.getStaticRouteDstIpMaskIndex().toString());
        ipRoute.setNextHop(newIpRoute.getStaticRouteNextHopIndex().toString());
        ipRoute.setDistance(newIpRoute.getStaticRouteDistance());
        ipRoute.setTrack(newIpRoute.getStaticRouteTrack());
        ipRoute.setRouteType(IpRoute.STATIC_IPROUTE);
        ipRouteDao.updateIpRoute(ipRoute);
    }

    /**
     * 根据设定的掩码获得指定Ip的网络地址
     * @param ip
     * @param mask
     * @return
     */
    private String getIpByMask(String ip, String mask) {
        int[] ipInt = IpUtils.ipToIntArray(ip);
        int[] maskInt = IpUtils.ipToIntArray(mask);
        int[] temp = IpUtils.ipToIntArray("0.0.0.0");
        for (int j = 0; j < 4; j++) {
            temp[j] = ipInt[j] & maskInt[j];
        }
        return IpUtils.intArrayToIp(temp);
    }

}
