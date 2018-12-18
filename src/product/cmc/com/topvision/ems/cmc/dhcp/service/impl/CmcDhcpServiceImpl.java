/***********************************************************************
 * $Id: CmcDhcpServiceImpl.java,v1.0 2012-2-13 下午02:36:31 $
 * 
 * @author: zhanglongyang
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.dhcp.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.cmc.dhcp.dao.CmcDhcpDao;
import com.topvision.ems.cmc.dhcp.facade.domain.CmcDhcpBaseConfig;
import com.topvision.ems.cmc.dhcp.facade.domain.CmcDhcpBundle;
import com.topvision.ems.cmc.dhcp.facade.domain.CmcDhcpGiAddr;
import com.topvision.ems.cmc.dhcp.facade.domain.CmcDhcpIntIp;
import com.topvision.ems.cmc.dhcp.facade.domain.CmcDhcpOption60;
import com.topvision.ems.cmc.dhcp.facade.domain.CmcDhcpServerConfig;
import com.topvision.ems.cmc.dhcp.service.CmcDhcpService;
import com.topvision.ems.cmc.service.impl.CmcBaseCommonService;
import com.topvision.ems.template.service.EntityTypeService;

/**
 * DHCP功能实现
 * 
 * @author zhanglongyang
 * @created @2012-2-13-下午02:36:31
 * 
 */
@Service("cmcDhcpService")
public class CmcDhcpServiceImpl extends CmcBaseCommonService implements CmcDhcpService {
    @Resource(name = "cmcDhcpDao")
    private CmcDhcpDao cmcDhcpDao;
    @Autowired
    private EntityTypeService entityTypeService;

    @Override
    public CmcDhcpBundle getCmcDhcpBundle(Map<String, Object> map) {
        return cmcDhcpDao.getCmcDhcpBundle(map);
    }

    @Override
    public List<CmcDhcpBundle> getCmcDhcpBundleList(Map<String, Object> map) {
        //return cmcDhcpDao.getCmcDhcpBundleList(map);
        return null;
    }

    @Override
    public void deleteDhcpBundle(Map<String, Object> map) {
        CmcDhcpBundle cmcDhcpBundle = cmcDhcpDao.getCmcDhcpBundle(map);
        Long productType = cmcDhcpDao.getCmcDeviceStyleByCmcId(Long.valueOf(map.get("cmcId").toString()));
        cmcDhcpBundle.setTopCcmtsDhcpBundleStatus(6);
        if (entityTypeService.isCcmtsWithoutAgent(productType)) {
            snmpParam = getSnmpParamByEntityId(cmcDhcpBundle.getEntityId());
        } else if (entityTypeService.isCcmtsWithAgent(productType)) {
            snmpParam = getSnmpParamByCmcId(cmcDhcpBundle.getCmcId());
        }
        getCmcFacade(snmpParam.getIpAddress()).modifyDhcpBundleInfo(snmpParam, cmcDhcpBundle);
        cmcDhcpDao.deleteDhcpBundle(map);
    }

    @Override
    public void addDhcpBundle(CmcDhcpBundle cmcDhcpBundle) {
        Long productType = cmcDhcpDao.getCmcDeviceStyleByCmcId(cmcDhcpBundle.getCmcId());
        if (entityTypeService.isCcmtsWithoutAgent(productType)) {
            snmpParam = getSnmpParamByEntityId(cmcDhcpBundle.getEntityId());
        } else if (entityTypeService.isCcmtsWithAgent(productType)) {
            snmpParam = getSnmpParamByCmcId(cmcDhcpBundle.getCmcId());
        }
        CmcDhcpBundle cmcDhcpBundleAfterModified = getCmcFacade(snmpParam.getIpAddress()).modifyDhcpBundleInfo(
                snmpParam, cmcDhcpBundle);
        cmcDhcpDao.addDhcpBundle(cmcDhcpBundleAfterModified);
    }

    @Override
    public void modifyDhcpBundle(CmcDhcpBundle cmcDhcpBundle) {
        Long productType = cmcDhcpDao.getCmcDeviceStyleByCmcId(cmcDhcpBundle.getCmcId());
        if (entityTypeService.isCcmtsWithoutAgent(productType)) {
            snmpParam = getSnmpParamByEntityId(cmcDhcpBundle.getEntityId());
        } else if (entityTypeService.isCcmtsWithAgent(productType)) {
            snmpParam = getSnmpParamByCmcId(cmcDhcpBundle.getCmcId());
        }
        CmcDhcpBundle cmcDhcpBundleAfterModified = getCmcFacade(snmpParam.getIpAddress()).modifyDhcpBundleInfo(
                snmpParam, cmcDhcpBundle);
        cmcDhcpDao.updateDhcpBundle(cmcDhcpBundleAfterModified);
    }

    @Override
    public List<CmcDhcpServerConfig> getCmcDhcpServerList(Map<String, Object> map) {
        return cmcDhcpDao.getCmcDhcpServerList(map);
    }

    @Override
    public void deleteDhcpServer(Map<String, Object> map) {
        CmcDhcpServerConfig cmcDhcpServerConfig = cmcDhcpDao.getCmcDhcpServerConfig(map);
        Long productType = cmcDhcpDao.getCmcDeviceStyleByCmcId(Long.valueOf(map.get("cmcId").toString()));
        cmcDhcpServerConfig.setTopCcmtsDhcpHelperStatus(6);
        if (entityTypeService.isCcmtsWithoutAgent(productType)) {
            snmpParam = getSnmpParamByEntityId(cmcDhcpServerConfig.getEntityId());
        } else if (entityTypeService.isCcmtsWithAgent(productType)) {
            snmpParam = getSnmpParamByCmcId(cmcDhcpServerConfig.getCmcId());
        }
        getCmcFacade(snmpParam.getIpAddress()).modifyDhcpServerInfo(snmpParam, cmcDhcpServerConfig);
        cmcDhcpDao.deleteDhcpServer(map);
    }

    @Override
    public List<CmcDhcpGiAddr> getCmcDhcpGiAddrList(Map<String, Object> map) {
        return cmcDhcpDao.getCmcDhcpGiAddrList(map);
    }

    @Override
    public void deleteDhcpGiAddr(Map<String, Object> map) {
        CmcDhcpGiAddr cmcDhcpGiAddr = cmcDhcpDao.getCmcDhcpGiAddr(map);
        Long productType = cmcDhcpDao.getCmcDeviceStyleByCmcId(Long.valueOf(map.get("cmcId").toString()));
        cmcDhcpGiAddr.setTopCcmtsDhcpGiAddrStatus(6);
        if (entityTypeService.isCcmtsWithoutAgent(productType)) {
            snmpParam = getSnmpParamByEntityId(cmcDhcpGiAddr.getEntityId());
        } else if (entityTypeService.isCcmtsWithAgent(productType)) {
            snmpParam = getSnmpParamByCmcId(cmcDhcpGiAddr.getCmcId());
        }
        getCmcFacade(snmpParam.getIpAddress()).modifyDhcpGiAddrInfo(snmpParam, cmcDhcpGiAddr);
        cmcDhcpDao.deleteDhcpGiAddr(map);
    }

    @Override
    public List<CmcDhcpIntIp> getCmcDhcpIntIpList(Long cmcId) {
        return cmcDhcpDao.getCmcDhcpIntIpList(cmcId);
    }

    @Override
    public void deleteDhcpIntIp(Long cmcId, Integer index, Long ifIndex) {
        //cmcDhcpDao.deleteDhcpIntIp(cmcId, index, ifIndex);
    }

    @Override
    public void addDhcpServer(CmcDhcpServerConfig cmcDhcpServerConfig) {
        Long productType = cmcDhcpDao.getCmcDeviceStyleByCmcId(cmcDhcpServerConfig.getCmcId());
        if (entityTypeService.isCcmtsWithoutAgent(productType)) {
            snmpParam = getSnmpParamByEntityId(cmcDhcpServerConfig.getEntityId());
        } else if (entityTypeService.isCcmtsWithAgent(productType)) {
            snmpParam = getSnmpParamByCmcId(cmcDhcpServerConfig.getCmcId());
        }
        CmcDhcpServerConfig cmcDhcpServerConfigAfterModified = getCmcFacade(snmpParam.getIpAddress())
                .modifyDhcpServerInfo(snmpParam, cmcDhcpServerConfig);
        cmcDhcpDao.addDhcpServer(cmcDhcpServerConfigAfterModified);
    }

    @Override
    public void modifyDhcpServer(CmcDhcpServerConfig cmcDhcpServerConfig) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("cmcId", cmcDhcpServerConfig.getCmcId());
        map.put("helperId", cmcDhcpServerConfig.getHelperId());
        cmcDhcpServerConfig.setTopCcmtsDhcpHelperIndex(cmcDhcpDao.getCmcDhcpServerConfig(map)
                .getTopCcmtsDhcpHelperIndex());
        Long productType = cmcDhcpDao.getCmcDeviceStyleByCmcId(cmcDhcpServerConfig.getCmcId());
        if (entityTypeService.isCcmtsWithoutAgent(productType)) {
            snmpParam = getSnmpParamByEntityId(cmcDhcpServerConfig.getEntityId());
        } else if (entityTypeService.isCcmtsWithAgent(productType)) {
            snmpParam = getSnmpParamByCmcId(cmcDhcpServerConfig.getCmcId());
        }
        CmcDhcpServerConfig cmcDhcpServerConfigAfterModified = getCmcFacade(snmpParam.getIpAddress())
                .modifyDhcpServerInfo(snmpParam, cmcDhcpServerConfig);
        cmcDhcpDao.updateDhcpServer(cmcDhcpServerConfigAfterModified);
    }

    @Override
    public void addDhcpGiAddr(CmcDhcpGiAddr cmcDhcpGiAddr) {
        Long productType = cmcDhcpDao.getCmcDeviceStyleByCmcId(cmcDhcpGiAddr.getCmcId());
        if (entityTypeService.isCcmtsWithoutAgent(productType)) {
            snmpParam = getSnmpParamByEntityId(cmcDhcpGiAddr.getEntityId());
        } else if (entityTypeService.isCcmtsWithAgent(productType)) {
            snmpParam = getSnmpParamByCmcId(cmcDhcpGiAddr.getCmcId());
        }
        CmcDhcpGiAddr cmcDhcpGiAddrAfterModified = getCmcFacade(snmpParam.getIpAddress()).modifyDhcpGiAddrInfo(
                snmpParam, cmcDhcpGiAddr);
        cmcDhcpDao.addDhcpGiAddr(cmcDhcpGiAddrAfterModified);
    }

    @Override
    public void modifyDhcpGiAddr(CmcDhcpGiAddr cmcDhcpGiAddr) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("cmcId", cmcDhcpGiAddr.getCmcId());
        map.put("giAddrId", cmcDhcpGiAddr.getGiAddrId());
        cmcDhcpGiAddr.setTopCcmtsDhcpGiAddrDeviceType(cmcDhcpDao.getCmcDhcpGiAddr(map)
                .getTopCcmtsDhcpGiAddrDeviceType());
        Long productType = cmcDhcpDao.getCmcDeviceStyleByCmcId(cmcDhcpGiAddr.getCmcId());
        if (entityTypeService.isCcmtsWithoutAgent(productType)) {
            snmpParam = getSnmpParamByEntityId(cmcDhcpGiAddr.getEntityId());
        } else if (entityTypeService.isCcmtsWithAgent(productType)) {
            snmpParam = getSnmpParamByCmcId(cmcDhcpGiAddr.getCmcId());
        }
        CmcDhcpGiAddr cmcDhcpGiAddrAfterModified = getCmcFacade(snmpParam.getIpAddress()).modifyDhcpGiAddrInfo(
                snmpParam, cmcDhcpGiAddr);
        cmcDhcpDao.updateDhcpGiAddr(cmcDhcpGiAddrAfterModified);
    }

    @Override
    public void addDhcpIntIp(CmcDhcpIntIp cmcDhcpIntIp) {
        cmcDhcpDao.addDhcpIntIp(cmcDhcpIntIp);
    }

    @Override
    public void modifyDhcpIntIp(CmcDhcpIntIp cmcDhcpIntIp) {
        cmcDhcpDao.updateDhcpIntIp(cmcDhcpIntIp);
    }

    @Override
    public List<Integer> getCmcDhcpIndexList(Long cmcId, String bundle, Integer type, Integer flag) {
        return cmcDhcpDao.getCmcDhcpIndexList(cmcId, bundle, type, flag);
    }

    @Override
    public List<Long> getCmcDhcpIfIndexList(Long cmcId) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<CmcDhcpOption60> getCmcDhcpOption60List(Map<String, Object> map) {
        return cmcDhcpDao.getCmcDhcpOption60List(map);
    }

    @Override
    public void deleteDhcpOption60(Map<String, Object> map) {
        CmcDhcpOption60 cmcDhcpOption60 = cmcDhcpDao.getCmcDhcpOption60(map);
        Long productType = cmcDhcpDao.getCmcDeviceStyleByCmcId(Long.valueOf(map.get("cmcId").toString()));
        cmcDhcpOption60.setTopCcmtsDhcpOption60Status(6);
        if (entityTypeService.isCcmtsWithoutAgent(productType)) {
            snmpParam = getSnmpParamByEntityId(cmcDhcpOption60.getEntityId());
        } else if (entityTypeService.isCcmtsWithAgent(productType)) {
            snmpParam = getSnmpParamByCmcId(cmcDhcpOption60.getCmcId());
        }
        getCmcFacade(snmpParam.getIpAddress()).modifyDhcpOption60Info(snmpParam, cmcDhcpOption60);
        cmcDhcpDao.deleteDhcpOption60(map);
    }

    @Override
    public void addDhcpOption60(CmcDhcpOption60 cmcDhcpOption60) {
        Long productType = cmcDhcpDao.getCmcDeviceStyleByCmcId(cmcDhcpOption60.getCmcId());
        if (entityTypeService.isCcmtsWithoutAgent(productType)) {
            snmpParam = getSnmpParamByEntityId(cmcDhcpOption60.getEntityId());
        } else if (entityTypeService.isCcmtsWithAgent(productType)) {
            snmpParam = getSnmpParamByCmcId(cmcDhcpOption60.getCmcId());
        }
        CmcDhcpOption60 cmcDhcpOption60AfterModified = getCmcFacade(snmpParam.getIpAddress()).modifyDhcpOption60Info(
                snmpParam, cmcDhcpOption60);
        cmcDhcpDao.addDhcpOption60(cmcDhcpOption60AfterModified);
    }

    @Override
    public void modifyDhcpOption60(CmcDhcpOption60 cmcDhcpOption60) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("cmcId", cmcDhcpOption60.getCmcId());
        map.put("option60Id", cmcDhcpOption60.getOption60Id());
        cmcDhcpOption60.setTopCcmtsDhcpOption60Index(cmcDhcpDao.getCmcDhcpOption60(map).getTopCcmtsDhcpOption60Index());
        Long productType = cmcDhcpDao.getCmcDeviceStyleByCmcId(cmcDhcpOption60.getCmcId());
        if (entityTypeService.isCcmtsWithoutAgent(productType)) {
            snmpParam = getSnmpParamByEntityId(cmcDhcpOption60.getEntityId());
        } else if (entityTypeService.isCcmtsWithAgent(productType)) {
            snmpParam = getSnmpParamByCmcId(cmcDhcpOption60.getCmcId());
        }
        CmcDhcpOption60 cmcDhcpOption60AfterModified = getCmcFacade(snmpParam.getIpAddress()).modifyDhcpOption60Info(
                snmpParam, cmcDhcpOption60);
        cmcDhcpDao.updateDhcpOption60(cmcDhcpOption60AfterModified);
    }

    @Override
    public void refreshDhcpInfo(Long cmcId) {
        Long productType = cmcDhcpDao.getCmcDeviceStyleByCmcId(cmcId);
        Long entityId = getEntityIdByCmcId(cmcId);
        if (entityTypeService.isCcmtsWithoutAgent(productType)) {
            snmpParam = getSnmpParamByEntityId(entityId);
        } else if (entityTypeService.isCcmtsWithAgent(productType)) {
            snmpParam = getSnmpParamByCmcId(cmcId);
        }
        List<CmcDhcpBundle> cmcDhcpBundleInfos = getCmcFacade(snmpParam.getIpAddress()).getCmcDhcpBundleInfo(snmpParam);
        List<CmcDhcpServerConfig> cmcDhcpServerConfigInfos = getCmcFacade(snmpParam.getIpAddress())
                .getCmcDhcpServerConfigInfo(snmpParam);
        List<CmcDhcpGiAddr> cmcDhcpGiAddrInfos = getCmcFacade(snmpParam.getIpAddress()).getCmcDhcpGiAddrInfo(snmpParam);
        List<CmcDhcpOption60> cmcDhcpOption60Infos = getCmcFacade(snmpParam.getIpAddress()).getCmcDhcpOption60Info(
                snmpParam);
        if (cmcDhcpBundleInfos != null) {
            cmcDhcpDao.batchInsertCcDhcpBundleConfig(cmcDhcpBundleInfos, cmcId, entityId);
        }
        if (cmcDhcpServerConfigInfos != null) {
            cmcDhcpDao.batchInsertCcDhcpServerConfig(cmcDhcpServerConfigInfos, cmcId, entityId);
        }
        if (cmcDhcpGiAddrInfos != null) {
            cmcDhcpDao.batchInsertCcDhcpGiaddrConfig(cmcDhcpGiAddrInfos, cmcId, entityId);
        }
        if (cmcDhcpOption60Infos != null) {
            cmcDhcpDao.batchInsertCcDhcpOption60Config(cmcDhcpOption60Infos, cmcId, entityId);
        }
    }

    @Override
    public void modifyCmcDhcpRelayBaseInfo(CmcDhcpBaseConfig cmcDhcpBaseConfigInfo) {
        Long productType = cmcDhcpDao.getCmcDeviceStyleByCmcId(cmcDhcpBaseConfigInfo.getCmcId());
        if (entityTypeService.isCcmtsWithoutAgent(productType)) {
            snmpParam = getSnmpParamByEntityId(cmcDhcpBaseConfigInfo.getEntityId());
        } else if (entityTypeService.isCcmtsWithAgent(productType)) {
            snmpParam = getSnmpParamByCmcId(cmcDhcpBaseConfigInfo.getCmcId());
        }
        CmcDhcpBaseConfig cmcDhcpBaseConfigAfterModified = getCmcFacade(snmpParam.getIpAddress())
                .modifyCmcDhcpRelayBaseInfo(snmpParam, cmcDhcpBaseConfigInfo);
        cmcDhcpDao.batchInsertOrUpdateCC8800BCmcDhcpBaseConfig(cmcDhcpBaseConfigAfterModified,
                cmcDhcpBaseConfigInfo.getEntityId());
    }

    @Override
    public CmcDhcpBaseConfig refreshCmcDhcpBaseConfig(Long cmcId) {
        /*
         * Long productType = cmcDhcpDao.getCmcDeviceStyleByCmcId(cmcId);
         * if(entityTypeService.isCcmtsWithoutAgent(productType)){ snmpParam =
         * getDolSnmpParamByEntityId(getEntityIdByCmcId(cmcId)); }else
         * if(entityTypeService.isCmc8800B(productType)){ snmpParam =
         * getSnmpParamByCmcId(cmcId,productType); snmpParam =
         * DolSnmpParam.clone(ProductSnmpParam.cloneFromProductSnmp(productSnmpParam)); }
         * CmcDhcpBaseConfig cmcDhcpBaseConfig =
         * getCmcFacade(snmpParam.getIpAddress()).getCmcDhcpRelayBaseInfo(snmpParam, cmcId);
         * cmcDiscoveryDao.batchInsertOrUpdateCC8800BCmcDhcpBaseConfig(cmcDhcpBaseConfig, cmcId);
         */
        return null;
    }

    @Override
    public Integer getCcmtsDhcpIndex(Long cmcId, String bundle, Integer type, Integer flag) {
        List<Integer> indexList = getCmcDhcpIndexList(cmcId, bundle, type, flag);
        int j = 0;
        int i = 1;
        if (indexList.size() == 0) {
            i = 1;
        } else {
            for (; i < 500; i++) {
                if (j < indexList.size()) {
                    if (indexList.get(j) != i) {
                        break;
                    }
                } else {
                    break;
                }
                j++;
            }
        }
        return i;
    }

}
