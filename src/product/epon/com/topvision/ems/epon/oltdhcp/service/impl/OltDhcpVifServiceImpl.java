/***********************************************************************
 * $Id: OltDhcpVifServiceImpl.java,v1.0 2017年11月23日 下午2:32:39 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.oltdhcp.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.epon.logicinterface.domain.LogicInterface;
import com.topvision.ems.epon.oltdhcp.dao.OltDhcpVifDao;
import com.topvision.ems.epon.oltdhcp.facade.OltDhcpFacade;
import com.topvision.ems.epon.oltdhcp.facade.domain.TopOltDhcpVifCfg;
import com.topvision.ems.epon.oltdhcp.service.OltDhcpRefreshService;
import com.topvision.ems.epon.oltdhcp.service.OltDhcpVifService;
import com.topvision.ems.epon.oltdhcp.utils.OltDhcpUtils;
import com.topvision.ems.network.service.EntityService;
import com.topvision.framework.service.BaseService;
import com.topvision.framework.snmp.RowStatus;
import com.topvision.framework.snmp.SnmpParam;
import com.topvision.platform.facade.FacadeFactory;

/**
 * @author haojie
 * @created @2017年11月23日-下午2:32:39
 *
 */
@Service("oltDhcpVifService")
public class OltDhcpVifServiceImpl extends BaseService implements OltDhcpVifService {
    @Autowired
    private FacadeFactory facadeFactory;
    @Autowired
    private EntityService entityService;
    @Autowired
    private OltDhcpVifDao oltDhcpVifDao;
    @Autowired
    private OltDhcpRefreshService oltDhcpRefreshService;

    @Override
    public List<TopOltDhcpVifCfg> getOltDhcpVifCfg(Long entityId) {
        return oltDhcpVifDao.getOltDhcpVifCfg(entityId);
    }

    @Override
    public void addOltDhcpVifCfg(TopOltDhcpVifCfg vifCfg) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(vifCfg.getEntityId());
        vifCfg.setTopOltDhcpVifRowStatus(RowStatus.CREATE_AND_GO);
        getOltDhcpFacade(snmpParam).setOltDhcpVifCfg(snmpParam, vifCfg);
        vifCfg.setTopOltDhcpVifOpt60StrIndex(OltDhcpUtils.convertOpt60StrToMibStr(vifCfg.getTopOltDhcpVifOpt60StrIndex()));
        oltDhcpVifDao.insertOltDhcpVifCfg(vifCfg);
    }

    @Override
    public void modifyOltDhcpVifCfg(TopOltDhcpVifCfg vifCfg) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(vifCfg.getEntityId());
        getOltDhcpFacade(snmpParam).setOltDhcpVifCfg(snmpParam, vifCfg);
        vifCfg.setTopOltDhcpVifOpt60StrIndex(OltDhcpUtils.convertOpt60StrToMibStr(vifCfg.getTopOltDhcpVifOpt60StrIndex()));
        oltDhcpVifDao.updateOltDhcpVifCfg(vifCfg);
    }

    @Override
    public void deleteOltDhcpVifCfg(Long entityId, Integer vifIndex, String opt60StrIndex) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        TopOltDhcpVifCfg vif = new TopOltDhcpVifCfg();
        vif.setEntityId(entityId);
        vif.setTopOltDhcpVifIndex(vifIndex);
        vif.setTopOltDhcpVifOpt60StrIndex(OltDhcpUtils.convertMibStrToOpt60Str(opt60StrIndex));
        vif.setTopOltDhcpVifRowStatus(RowStatus.DESTORY);
        getOltDhcpFacade(snmpParam).setOltDhcpVifCfg(snmpParam, vif);
        oltDhcpVifDao.deleteOltDhcpVifCfg(entityId, vifIndex, opt60StrIndex);
    }

    @Override
    public void refreshOltDhcpVifCfg(Long entityId) {
        oltDhcpRefreshService.refreshTopOltDhcpVifCfg(entityId);
    }

    private OltDhcpFacade getOltDhcpFacade(SnmpParam snmpParam) {
        return facadeFactory.getFacade(snmpParam.getIpAddress(), OltDhcpFacade.class);
    }

    @Override
    public TopOltDhcpVifCfg getOltDhcpVifCfg(Long entityId, Integer vifIndex, String opt60StrIndex) {
        return oltDhcpVifDao.getOltDhcpVifCfg(entityId, vifIndex, opt60StrIndex);
    }

    @Override
    public List<LogicInterface> getOltLogicInterfaceByType(Map<String, Object> param) {
        return oltDhcpVifDao.getOltLogicInterfaceByType(param);
    }

}
