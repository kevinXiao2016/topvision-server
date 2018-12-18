/***********************************************************************
 * $Id: OnuCpeLocationServiceImpl.java,v1.0 2016-5-6 上午11:24:53 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.cpelocation.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.epon.cpelocation.dao.OnuCpeLocatonDao;
import com.topvision.ems.epon.cpelocation.domain.OnuCpeIpLocation;
import com.topvision.ems.epon.cpelocation.domain.OnuCpeLocation;
import com.topvision.ems.epon.cpelocation.service.OnuCpeLocationService;
import com.topvision.ems.epon.onu.facade.OnuFacade;
import com.topvision.ems.epon.onucpe.facade.OnuCpeFacade;
import com.topvision.ems.epon.onucpe.facade.OnuCpeUtil;
import com.topvision.ems.epon.performance.domain.OnuCpe;
import com.topvision.ems.epon.performance.domain.OnuCpeIpInfo;
import com.topvision.ems.epon.performance.domain.OnuUniCpeList;
import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.network.service.EntityService;
import com.topvision.ems.template.service.EntityTypeService;
import com.topvision.framework.common.MacUtils;
import com.topvision.framework.service.BaseService;
import com.topvision.framework.snmp.SnmpParam;
import com.topvision.platform.facade.FacadeFactory;
import com.topvision.platform.zetaframework.util.Validator;

/**
 * @author flack
 * @created @2016-5-6-上午11:24:53
 *
 */
@Service("onuCpeLocationService")
public class OnuCpeLocationServiceImpl extends BaseService implements OnuCpeLocationService {

    @Autowired
    private OnuCpeLocatonDao onuCpeLocatonDao;
    @Autowired
    private FacadeFactory facadeFactory;
    @Autowired
    private EntityService entityService;
    @Autowired
    private EntityTypeService entityTypeService;

    @Override
    public OnuCpeLocation getOnuCpeLocation(String queryContent) {
        OnuCpeLocation resultCpeLoc = null;
        //首先从数据库获取
        if (Validator.isIpV4(queryContent)) {
            return queryOnuCpeLocationByIp(queryContent);
        } else {
            String cpeMac = MacUtils.formatMac(queryContent);
            OnuCpeLocation cpeLoc = onuCpeLocatonDao.queryOnuCpeLoc(cpeMac);
            //如果从数据库获取到了,还需要验证现在与设备是否一致
            if (cpeLoc != null) {
                OnuCpeLocation deviceCpeLoc = getDeviceCpeLoc(cpeLoc.getEntityId(), cpeMac);
                if (deviceCpeLoc != null) {
                    //如果能在缓存的设备上获取到CPE信息，则直接返回设备CPE信息
                    deviceCpeLoc.setEntityId(cpeLoc.getEntityId());
                    resultCpeLoc = deviceCpeLoc;
                } else {
                    //在缓存的设备上不存在，还需要从其它所有设备上获取
                    resultCpeLoc = this.getCpeLocFromDevice(cpeMac);
                }
            } else {
                //如果在数据库中没有CPE缓存记录则直接从设备上获取
                resultCpeLoc = this.getCpeLocFromDevice(cpeMac);
            }
            //处理CPE结果信息
            if (resultCpeLoc != null) {
                fullfillIpAddress(resultCpeLoc);
                //组装完整的展示信息并进行记录
                resultCpeLoc = this.getCompleteCpeInfo(resultCpeLoc);
                onuCpeLocatonDao.insertOrUpdateCpeLoc(resultCpeLoc);
            }
        }
        return resultCpeLoc;
    }

    /**
     * 通过CPE的IP查询终端信息
     * @param queryContent
     * @return
     */
    private OnuCpeLocation queryOnuCpeLocationByIp(String ip) {
        //获取所有的OLT设备信息
        OnuCpeLocation onuCpeLocation = null;
        List<Entity> list = entityService.getEntityListByType(entityTypeService.getOltType());
        OnuCpeIpLocation ipLocation = null;
        for (Entity entiy : list) {
            SnmpParam snmpParam = entityService.getSnmpParamByEntity(entiy.getEntityId());
            try {
                ipLocation = getOnuCpeFacade(snmpParam.getIpAddress()).fetchOnuCpeLocationByIP(snmpParam, ip);
            } catch (Exception e) {
                logger.trace("", e);
            }
            if (ipLocation != null) {
                String macAddress = ipLocation.getMacAddress().substring(0, 17);
                onuCpeLocation = getCpeLocFromDevice(macAddress);
                onuCpeLocation.setCpeType(ipLocation.getCpeType());
                onuCpeLocation.setIpAddress(ipLocation.getIpAddress());
                //组装完整的展示信息并进行记录
                getCompleteCpeInfo(onuCpeLocation);
                onuCpeLocatonDao.insertOrUpdateCpeLoc(onuCpeLocation);
            }
        }
        return onuCpeLocation;
    }

    /**
     * 补充IP和终端类型信息
     * @param cpeLocation
     */
    private void fullfillIpAddress(OnuCpeLocation cpeLocation) {
        String mac = cpeLocation.getMacLocation();
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(cpeLocation.getEntityId());
        String message = getOnuCpeFacade(snmpParam.getIpAddress()).fetchOnuCpeLocationList(snmpParam, cpeLocation);
        List<OnuCpeIpInfo> list = OnuCpeUtil.parseOnuCpeIpInfoList(message);
        for (OnuCpeIpInfo info : list) {
            if (mac.equalsIgnoreCase(info.getMac())) {
                cpeLocation.setIpAddress(info.getIp());
                cpeLocation.setCpeType(info.getCpeType());
                break;
            }
        }
    }

    /**
     * 从设备上获取CPE定位信息
     * @return
     */
    private OnuCpeLocation getCpeLocFromDevice(String cpeMac) {
        //获取所有的OLT设备信息
        List<Entity> oltList = entityService.getEntityListByType(entityTypeService.getOltType());
        //遍历OLT获取CPE定位信息
        OnuCpeLocation deviceCpeLoc = null;
        for (Entity entity : oltList) {
            deviceCpeLoc = getDeviceCpeLoc(entity.getEntityId(), cpeMac);
            //如果在该设备上找到了CPE信息,则直接结束
            if (deviceCpeLoc != null) {
                deviceCpeLoc.setEntityId(entity.getEntityId());
                break;
            }
        }
        return deviceCpeLoc;
    }

    /**
     * 获取设备上的CPE定位信息
     * 封装对异常的处理
     * @param entityId
     * @param cpeMac
     * @return
     */
    private OnuCpeLocation getDeviceCpeLoc(Long entityId, String cpeMac) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        OnuCpeLocation cpeLocation = null;
        try {
            OnuFacade onuFacade = getOnuFacade(snmpParam.getIpAddress());
            cpeLocation = onuFacade.getOnuCpeLocation(snmpParam, cpeMac);
            OnuCpeFacade onuCpeFacade = getOnuCpeFacade(snmpParam.getIpAddress());
            OnuUniCpeList onuUniCpeList = onuCpeFacade.refreshEponOnuUniCpe(snmpParam, cpeLocation.getUniIndex());
            List<OnuCpe> list = OnuCpeUtil.makeOnuCpeList(onuUniCpeList, entityId, null);
            for (OnuCpe cpe : list) {
                if (cpe.getMac().equalsIgnoreCase(cpeLocation.getMacLocation())) {
                    if (cpeLocation.getVlanId() == null) {
                        cpeLocation.setVlanId(cpe.getVlan());
                    }
                    cpeLocation.setMacType(cpe.getType());
                    break;
                }
            }
        } catch (Exception e) {
            logger.trace("", e);
        }
        return cpeLocation;
    }

    /**
     * 获取完整的CPE展示信息
     * 包括OLT信息和ONU信息
     * @return
     */
    private OnuCpeLocation getCompleteCpeInfo(OnuCpeLocation cpeLoc) {
        OnuCpeLocation cpeRelaInfo = onuCpeLocatonDao.getOnuCpeRelaInfo(cpeLoc);
        cpeLoc.setOltIp(cpeRelaInfo.getOltIp());
        cpeLoc.setOltName(cpeRelaInfo.getOltName());
        cpeLoc.setOnuId(cpeRelaInfo.getOnuId());
        cpeLoc.setOnuName(cpeRelaInfo.getOnuName());
        return cpeLoc;
    }

    private OnuFacade getOnuFacade(String ip) {
        return facadeFactory.getFacade(ip, OnuFacade.class);
    }

    private OnuCpeFacade getOnuCpeFacade(String ip) {
        return facadeFactory.getFacade(ip, OnuCpeFacade.class);
    }
}
