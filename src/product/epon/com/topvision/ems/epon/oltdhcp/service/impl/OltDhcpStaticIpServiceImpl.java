/***********************************************************************
 * $Id: OltDhcpStaticIpServiceImpl.java,v1.0 2017年11月23日 上午9:36:55 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.oltdhcp.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.epon.exception.AddStaticIpException;
import com.topvision.ems.epon.olt.domain.OltSlotAttribute;
import com.topvision.ems.epon.olt.service.OltService;
import com.topvision.ems.epon.olt.service.OltSlotService;
import com.topvision.ems.epon.oltdhcp.dao.OltDhcpStaticIpDao;
import com.topvision.ems.epon.oltdhcp.facade.OltDhcpFacade;
import com.topvision.ems.epon.oltdhcp.facade.domain.TopOltDhcpGlobalObjects;
import com.topvision.ems.epon.oltdhcp.facade.domain.TopOltDhcpStaticIp;
import com.topvision.ems.epon.oltdhcp.service.OltDhcpRefreshService;
import com.topvision.ems.epon.oltdhcp.service.OltDhcpStaticIpService;
import com.topvision.ems.network.service.EntityService;
import com.topvision.framework.domain.IpsAddress;
import com.topvision.framework.service.BaseService;
import com.topvision.framework.snmp.RowStatus;
import com.topvision.framework.snmp.SnmpParam;
import com.topvision.framework.utils.EponConstants;
import com.topvision.platform.facade.FacadeFactory;

/**
 * @author haojie
 * @created @2017年11月23日-上午9:36:55
 *
 */
@Service("oltDhcpStaticIpService")
public class OltDhcpStaticIpServiceImpl extends BaseService implements OltDhcpStaticIpService {
    @Autowired
    private FacadeFactory facadeFactory;
    @Autowired
    private EntityService entityService;
    @Autowired
    private OltDhcpStaticIpDao oltDhcpStaticIpDao;
    @Autowired
    private OltDhcpRefreshService oltDhcpRefreshService;
    @Autowired
    private OltSlotService oltSlotService;
    @Autowired
    private OltService oltService;

    @Override
    public List<TopOltDhcpStaticIp> getOltDhcpStaticIp(Map<String, Object> queryMap) {
        return oltDhcpStaticIpDao.getOltDhcpStaticIp(queryMap);
    }

    @Override
    public Long getOltDhcpStaticIpCount(Map<String, Object> queryMap) {
        return oltDhcpStaticIpDao.getOltDhcpStaticIpCount(queryMap);
    }

    @Override
    public void refreshOltDhcpStaticIp(Long entityId) {
        // 刷新防静态IP开关
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        TopOltDhcpGlobalObjects globalObjects = getOltDhcpFacade(snmpParam).getTopOltDhcpGlobalObjects(snmpParam);
        if (globalObjects != null) {
            oltDhcpStaticIpDao.updateOltDhcpSourceVerifyEnable(entityId,
                    globalObjects.getTopOltDhcpSourceVerifyEnable());
        }
        // 刷新静态IP列表信息
        oltDhcpRefreshService.refreshTopOltDhcpStaticIp(entityId);
    }

    @Override
    public void addOltDhcpStaticIp(TopOltDhcpStaticIp staticIp) {
        TopOltDhcpStaticIp ip = oltDhcpStaticIpDao.selectUniqueStaticIp(staticIp.getEntityId(), staticIp.getIpIndex(),
                staticIp.getMaskIndex());
        if (ip != null) {
            logger.error("addOltDhcpStaticIp error:IP exist");
            throw new AddStaticIpException("Exist");
        }
        Boolean allowedToBeAdd = isAllowedToBeAdd(staticIp);
        if (allowedToBeAdd) {
            SnmpParam snmpParam = entityService.getSnmpParamByEntity(staticIp.getEntityId());
            staticIp.setTopOltDhcpStaticIpRowStatus(RowStatus.CREATE_AND_GO);
            getOltDhcpFacade(snmpParam).setOltDhcpStaticIp(snmpParam, staticIp);
            oltDhcpStaticIpDao.insertOltDhcpStaticIp(staticIp);
        } else {
            logger.error("addOltDhcpStaticIp error:IP Full");
            throw new AddStaticIpException("Full");
        }
    }

    /**
     * 判断是否可以添加 1、每ONU限制：epuc板卡11个/ONU， 其它EPON板卡16个/ONU ； 2、整框限制：PN8602 256条，其它框 1K
     * 
     * @param staticIp
     */
    private Boolean isAllowedToBeAdd(TopOltDhcpStaticIp staticIp) {
        // 1、每ONU限制：epuc板卡11个/ONU， 其它EPON板卡16个/ONU ；
        Integer slotType = oltSlotService.getOltSlotActualType(staticIp.getEntityId(),
                staticIp.getTopOltDhcpStaticIpSlot());
        List<OltSlotAttribute> eponSlotList = oltSlotService.getOltEponSlotList(staticIp.getEntityId());
        List<Integer> slotTypeList = new ArrayList<>();
        for (OltSlotAttribute oltSlotAttribute : eponSlotList) {
            slotTypeList.add(oltSlotAttribute.getTopSysBdActualType());
        }
        if (slotTypeList.contains(slotType)) {
            Long staticIpCount = oltDhcpStaticIpDao.getOnuStaticIpCount(staticIp);
            if (slotType == EponConstants.BOARD_ONLINE_EPUC) {
                if (staticIpCount >= 11) {
                    return false;
                }
            } else {
                if (staticIpCount >= 16) {
                    return false;
                }
            }
        }
        // 2、整框限制：PN8602 256条，其它框 1024
        String oltType = oltService.getOltAttribute(staticIp.getEntityId()).getOltType();
        Map<String, Object> queryMap = new HashMap<String, Object>();
        queryMap.put("entityId", staticIp.getEntityId());
        Long ipCount = getOltDhcpStaticIpCount(queryMap);
        if ("PN8602".equals(oltType)) {
            if (ipCount >= 256) {
                return false;
            }
        } else {
            if (ipCount >= 1024) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void deleteOltDhcpStaticIp(Long entityId, String ipIndex, String maskIndex) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        TopOltDhcpStaticIp staticIp = new TopOltDhcpStaticIp();
        staticIp.setEntityId(entityId);
        staticIp.setTopOltDhcpStaticIpVrfNameIndex("");
        staticIp.setTopOltDhcpStaticIpIndex(new IpsAddress(ipIndex));
        staticIp.setTopOltDhcpStaticIpMaskIndex(new IpsAddress(maskIndex));
        staticIp.setTopOltDhcpStaticIpRowStatus(RowStatus.DESTORY);
        getOltDhcpFacade(snmpParam).setOltDhcpStaticIp(snmpParam, staticIp);
        oltDhcpStaticIpDao.deleteOltDhcpStaticIp(entityId, ipIndex, maskIndex);
    }

    private OltDhcpFacade getOltDhcpFacade(SnmpParam snmpParam) {
        return facadeFactory.getFacade(snmpParam.getIpAddress(), OltDhcpFacade.class);
    }

    @Override
    public List<Long> getOltSlotIdList(Long entityId) {
        return oltDhcpStaticIpDao.selectOltSlotIdList(entityId);
    }

    @Override
    public List<Long> getOltSlotPonIndexList(Map<String, Object> map) {
        return oltDhcpStaticIpDao.selectOltSlotPonIndexList(map);
    }

    @Override
    public List<Long> getOltSlotPonOnuIndexList(Map<String, Object> map) {
        return oltDhcpStaticIpDao.selectOltSlotPonOnuIndexList(map);
    }

}
