/***********************************************************************
 * $Id: CmcConfigServiceImpl.java,v1.0 2012-2-13 下午02:35:08 $
 * 
 * @author: zhanglongyang
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.config.service.impl;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.topvision.ems.cmc.ccmts.facade.domain.CmcAttribute;
import com.topvision.ems.cmc.config.dao.CmcConfigDao;
import com.topvision.ems.cmc.config.facade.domain.CmcEmsConfig;
import com.topvision.ems.cmc.config.facade.domain.CmcSnmpCommunityTable;
import com.topvision.ems.cmc.config.facade.domain.CmcSysConfig;
import com.topvision.ems.cmc.config.service.CmcConfigService;
import com.topvision.framework.constants.Symbol;
import com.topvision.ems.cmc.dhcp.facade.domain.CmcDhcpBaseConfig;
import com.topvision.ems.cmc.exception.SetCmcBackupTransmisionModeException;
import com.topvision.ems.cmc.exception.SetCmcMainTransmisionModeException;
import com.topvision.ems.cmc.exception.SetValueFailException;
import com.topvision.ems.cmc.facade.domain.CmcSystemBasicInfo;
import com.topvision.ems.cmc.facade.domain.CmcSystemIpInfo;
import com.topvision.ems.cmc.facade.domain.DocsDevEvControl;
import com.topvision.ems.cmc.service.impl.CmcBaseCommonService;
import com.topvision.ems.cmc.sni.exception.SetCmcSniStatusException;
import com.topvision.ems.cmc.sni.facade.domain.CcmtsSniObject;
import com.topvision.ems.cmc.vlan.domain.CmcPrimaryVlan;
import com.topvision.ems.cmc.vlan.facade.domain.CmcVlanPrimaryInterface;
import com.topvision.ems.cmc.vlan.facade.domain.CmcVlanPrimaryIp;
import com.topvision.ems.performance.domain.Monitor;
import com.topvision.framework.exception.engine.SnmpNoResponseException;
import com.topvision.framework.snmp.SnmpParam;

/**
 * 配置功能实现
 * 
 * @author zhanglongyang
 * @created @2012-2-13-下午02:35:08
 * 
 */
@Service("cmcConfigService")
public class CmcConfigServiceImpl extends CmcBaseCommonService implements CmcConfigService {
    @Resource(name = "cmcConfigDao")
    private CmcConfigDao cmcConfigDao;

    @PreDestroy
    public void destroy() {
        super.destroy();
    }

    @PostConstruct
    public void initialize() {
        super.initialize();
    }

    @Override
    public Monitor updateCmcPollingConfig(Long cmcId) {
        // TODO 轮询设置
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.cmc.service.CmcConfigService#getCmcBasicInfo(java.lang.Long)
     */
    public CmcSystemBasicInfo getCmcBasicInfo(Long cmcId) {
        return cmcConfigDao.getCmcBasicInfo(cmcId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.cmc.service.CmcConfigService#getCmcIpInfo(java.lang.Long)
     */
    public CmcSystemIpInfo getCmcIpInfo(Long cmcId) {
        return cmcConfigDao.getCmcIpInfo(cmcId);
    }

    @Override
    public List<CmcEmsConfig> getCmcEmsList(Long cmcId) {
        return cmcConfigDao.getCmcEmsList(cmcId);
    }

    /**
     * 修改基本配置信息
     * 
     * @param cmcId
     *            name location contact
     */
    @Override
    public void modifyCmcBasicInfo(Long cmcId, String cmcName, String location, String cmcContact) {
        // TODO 更新设备
        StringBuilder sBuilder = new StringBuilder();
        snmpParam = getSnmpParamByCmcId(cmcId);
        CmcAttribute cmcAttributeBeforeChanged = getCmcDao().getCmcAttributeByCmcId(cmcId);
        Long cmcIndex = getCmcDao().getCmcIndexByCmcId(cmcId);
        cmcAttributeBeforeChanged.setCmcIndex(cmcIndex);

        String cmcNameNewData = cmcAttributeBeforeChanged.getTopCcmtsSysName();
        if (!cmcNameNewData.equals(cmcName)) {
            cmcAttributeBeforeChanged.setTopCcmtsSysName(cmcName);
        }
        String locationNewData = cmcAttributeBeforeChanged.getTopCcmtsSysLocation();
        if (!locationNewData.equals(location)) {
            cmcAttributeBeforeChanged.setTopCcmtsSysLocation(location);
        }
        String cmcContactNewData = cmcAttributeBeforeChanged.getTopCcmtsSysContact();
        if (!cmcContactNewData.equals(cmcContact)) {
            cmcAttributeBeforeChanged.setTopCcmtsSysContact(cmcContact);
        }
        CmcAttribute cmcAttributeAfterModified = getCmcFacade(snmpParam.getIpAddress()).modifyCmcAttribute(snmpParam,
                cmcAttributeBeforeChanged);

        if (cmcAttributeAfterModified.getTopCcmtsSysName() != null
                && !cmcAttributeAfterModified.getTopCcmtsSysName().equals(
                        cmcAttributeBeforeChanged.getTopCcmtsSysName())) {
            sBuilder.append(getString("cmc.message.cmc.setTopCcmtsSysName"));
        }
        if (cmcAttributeAfterModified.getTopCcmtsSysLocation() != null
                && !cmcAttributeAfterModified.getTopCcmtsSysLocation().equals(
                        cmcAttributeBeforeChanged.getTopCcmtsSysLocation())) {
            sBuilder.append(getString("cmc.message.cmc.setTopCcmtsSysLocation"));
        }
        if (cmcAttributeAfterModified.getTopCcmtsSysContact() != null
                && !cmcAttributeAfterModified.getTopCcmtsSysContact().equals(
                        cmcAttributeBeforeChanged.getTopCcmtsSysContact())) {
            sBuilder.append(getString("cmc.message.cmc.setTopCcmtsSysContact"));
        }
        if (sBuilder.length() > 0) {
            throw new SetValueFailException(sBuilder.toString());
        }
        cmcConfigDao.updateCmcConfigBasicInfo(cmcId, cmcName, location, cmcContact);
    }

    @Override
    public void modifyCmcIpInfo(Long cmcId, String ipList) {
        String[] tmp = ipList.split(Symbol.UNDERLINE);
        CmcVlanPrimaryInterface cmcVlanPrimaryInterfaceBeforeChanged = cmcConfigDao.getCC8800BVlanPriInterface(cmcId);
        Integer vlanId = cmcVlanPrimaryInterfaceBeforeChanged.getVlanPrimaryInterface();
        snmpParam = getSnmpParamByCmcId(cmcId);

        CmcVlanPrimaryInterface cmcVlanPrimaryInterfaceModified = new CmcVlanPrimaryInterface();

        String defaultRouteNewData = cmcVlanPrimaryInterfaceBeforeChanged.getVlanPrimaryDefaultRoute();
        if (!defaultRouteNewData.equals(tmp[2])) {
            cmcVlanPrimaryInterfaceBeforeChanged.setVlanPrimaryDefaultRoute(tmp[2]);
        }
        try {
            cmcVlanPrimaryInterfaceModified = getCmcFacade(snmpParam.getIpAddress()).modifyCmcDefaultRoute(snmpParam,
                    cmcVlanPrimaryInterfaceBeforeChanged);
            cmcConfigDao.batchInsertOrUpdateCC8800BVlanPriInterface(cmcVlanPrimaryInterfaceBeforeChanged, cmcId);
        } catch (Exception e) {
            logger.debug("", e);
        }
        /*
         * 依次判断各项属性值是否相等，不相等则抛出异常 刘占山 2013.4.8
         */
        boolean isSuccess = true;
        if (cmcVlanPrimaryInterfaceModified.getVlanPrimaryDefaultRoute() != null
                && !cmcVlanPrimaryInterfaceModified.getVlanPrimaryDefaultRoute().equals(
                        cmcVlanPrimaryInterfaceModified.getVlanPrimaryDefaultRoute())) {
            isSuccess = false;
            logger.error("cmc.message.cmc.setVlanPrimaryDefaultRoute");
        }

        CmcVlanPrimaryIp cmcVlanPrimaryIpbeforeChanged = cmcConfigDao.getCC8800BVlanPriIpByCmcIdAndVId(cmcId, vlanId);
        CmcVlanPrimaryIp cmcVlanPrimaryIpNewData = new CmcVlanPrimaryIp();
        if (cmcVlanPrimaryIpbeforeChanged != null) {
            cmcVlanPrimaryIpNewData = cmcVlanPrimaryIpbeforeChanged;
        }
        cmcVlanPrimaryIpNewData.setTopCcmtsVifPriIpAddr(tmp[0]);
        cmcVlanPrimaryIpNewData.setTopCcmtsVifPriIpMask(tmp[1]);
        cmcVlanPrimaryIpNewData.setCmcId(cmcId);
        cmcVlanPrimaryIpNewData.setTopCcmtsVifPriIpVlanId(vlanId);
        String vlanIp = cmcVlanPrimaryIpNewData.getTopCcmtsVifPriIpAddr();
        if (!vlanIp.equals(tmp[0])) {
            cmcVlanPrimaryIpNewData.setTopCcmtsVifPriIpAddr(tmp[0]);
        }
        String vlanMask = cmcVlanPrimaryIpNewData.getTopCcmtsVifPriIpMask();
        if (!vlanMask.equals(tmp[1])) {
            cmcVlanPrimaryIpNewData.setTopCcmtsVifPriIpMask(tmp[1]);
        }
        CmcVlanPrimaryIp cmcVlanPrimaryIpModified = new CmcVlanPrimaryIp();
        try {
            cmcVlanPrimaryIpModified = getCmcFacade(snmpParam.getIpAddress()).modifyCmcVlanPrimaryIp(snmpParam,
                    cmcVlanPrimaryIpNewData);
            cmcConfigDao.updateCC8800BCcPrimaryVlan(cmcVlanPrimaryIpNewData);
            // TODO 更新productSnmp
            // CmcEntity cmcEntity = new CmcEntity();
            // cmcEntity.setEntityId(entityId);
            // cmcEntity.setIpAddress(vlanIp);
            // cmcDiscoveryDao.updateEntityAndAddress(cmcEntity);
            // TODO 提示重新更新设备
        } catch (Exception e) {
            logger.debug("", e);
        }
        if (cmcVlanPrimaryIpModified.getTopCcmtsVifPriIpAddr() != null
                && !cmcVlanPrimaryIpModified.getTopCcmtsVifPriIpAddr().equals(
                        cmcVlanPrimaryIpNewData.getTopCcmtsVifPriIpAddr())) {
            isSuccess = false;
            logger.error("cmc.message.cmc.setTopCcmtsVifPriIpAddr");
        }
        if (cmcVlanPrimaryIpModified.getTopCcmtsVifPriIpMask() != null
                && !cmcVlanPrimaryIpModified.getTopCcmtsVifPriIpMask().equals(
                        cmcVlanPrimaryIpNewData.getTopCcmtsVifPriIpMask())) {
            isSuccess = false;
            logger.error("cmc.message.cmc.setTopCcmtsVifPriIpAddr");
        }
        if (!isSuccess) {
            throw new SetValueFailException("set failure");
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.cmc.service.CmcConfigService#modifyPhysicalInterfacePreferredMode(java.
     * lang.Long, java.lang.Integer)
     */
    @Override
    public void modifyPhysicalInterfacePreferredMode(Long cmcId, Integer physicalInterfaceMode) {
        SnmpParam snmpParam = getSnmpParamByCmcId(cmcId);
        try {
            getCmcFacade(snmpParam.getIpAddress()).setCmcSniStatus(snmpParam, physicalInterfaceMode);
            cmcConfigDao.modifyPhysicalInterfacePreferredMode(cmcId, physicalInterfaceMode);
        } catch (Exception e) {
            throw new SetCmcSniStatusException(e);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.cmc.service.CmcConfigService#modifyMainTransmissionMode(java.lang.Long,
     * java.lang.Integer)
     */
    @Override
    public void modifyMainTransmissionMode(Long cmcId, Integer transmissionMode) {
        SnmpParam snmpParam = getSnmpParamByCmcId(cmcId);
        try {
            getCmcFacade(snmpParam.getIpAddress()).setCmcSniMainTransmisionMode(snmpParam, transmissionMode);
            cmcConfigDao.modifyMainTransmissionMode(cmcId, transmissionMode);
        } catch (Exception e) {
            throw new SetCmcMainTransmisionModeException(e);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.cmc.service.CmcConfigService#modifyBackupTransmissionMode(java.lang.Long,
     * java.lang.Integer)
     */
    @Override
    public void modifyBackupTransmissionMode(Long cmcId, Integer transmissionMode) {
        SnmpParam snmpParam = getSnmpParamByCmcId(cmcId);
        try {
            getCmcFacade(snmpParam.getIpAddress()).setCmcSniBackupTransmisionMode(snmpParam, transmissionMode);
            cmcConfigDao.modifyBackupTransmissionMode(cmcId, transmissionMode);
        } catch (Exception e) {
            throw new SetCmcBackupTransmisionModeException(e);
        }
    }

    @Override
    public void modifyCmcEms(Long entityId, CmcEmsConfig cmcEmsConfig, boolean insertFlag) {
        SnmpParam snmpParam = getSnmpParamByEntityId(entityId);
        try {
            CmcEmsConfig cmcEmsConfigAfterModified = getCmcFacade(snmpParam.getIpAddress()).modifyCmcEmsConfig(
                    snmpParam, cmcEmsConfig);
            if (insertFlag) {
                cmcConfigDao.insertCmcEmsConfig(cmcEmsConfigAfterModified);
            } else {
                cmcConfigDao.updateCmcEmsConfig(cmcEmsConfigAfterModified);
            }
            /*
             * CmcEmsConfig cmcEmsConfigByCmcId = (CmcEmsConfig)
             * cmcConfigDao.getCmcEmsList(cmcEmsConfig.getEntityId()); if(cmcEmsConfigByCmcId ==
             * null) { cmcConfigDao.batchInsertCcEmsConfig(cmcEmsConfig,
             * cmcEmsConfig.getEntityId()); }else{ cmcConfigDao.modifyCmcEmsConfig(entityId,
             * cmcEmsConfig); }
             */
        } catch (Exception e) {
            logger.debug("", e);
        }
    }

    @Override
    public void modifyDeviceEventControl(DocsDevEvControl docsDevEvControl) {
        SnmpParam snmpParam = getSnmpParamByEntityId(docsDevEvControl.getEntityId());
        DocsDevEvControl docsDevEvControlModified = new DocsDevEvControl();
        docsDevEvControlModified.setDocsDevEvPriority(docsDevEvControl.getDocsDevEvPriority());
        String devEvReporting = getCmcFacade(snmpParam.getIpAddress()).modifyDocsDevEvControl(snmpParam,
                docsDevEvControl);
        docsDevEvControlModified.setDocsDevEvReporting(devEvReporting);
        docsDevEvControlModified.setEntityId(docsDevEvControl.getEntityId());
        cmcConfigDao.updateDevEvControl(docsDevEvControlModified);
    }

    @Override
    public CmcVlanPrimaryInterface getCC8800BVlanPriInterface(Long cmcId) {

        return cmcConfigDao.getCC8800BVlanPriInterface(cmcId);
    }

    @Override
    public List<CmcVlanPrimaryIp> getCC8800BVlanPriIpList(Long cmcId) {
        return cmcConfigDao.getCC8800BVlanPriIpList(cmcId);
    }

    @Override
    public CmcDhcpBaseConfig getCC8800BCmcDhcpBaseConfig(Long cmcId) {
        Long entityId = cmcDao.getEntityIdByCmcId(cmcId);
        return cmcConfigDao.getCC8800BCmcDhcpBaseConfig(entityId);
    }

    @Override
    public CmcPrimaryVlan getCC8800BCcPrimaryVlanAsSnmp(Long cmcId, Integer vlanId) {
        return cmcConfigDao.getCC8800BCcPrimaryVlanAsSnmp(cmcId, vlanId);
    }

    @Override
    public CmcAttribute refreshCmcAttribute(Long cmcId, Integer productType) {
        snmpParam = getSnmpParamByCmcId(cmcId);
        CmcAttribute cmcAttribute = getCmcFacade(snmpParam.getIpAddress()).getCmcAttribute(snmpParam);
        if (cmcAttribute != null) {
            cmcAttribute.setCmcId(cmcId);
            cmcAttribute.setCmcDeviceStyle(productType.longValue());
            getCmcDao().batchInsertCcSplitSystemInfo(cmcAttribute);
        }
        return cmcAttribute;
    }

    @Override
    public CmcPrimaryVlan refreshCmcPrimaryVlanAsSnmp(Long cmcId, Integer productType) {
        CmcPrimaryVlan cmcPrimaryVlan = new CmcPrimaryVlan();
        snmpParam = getSnmpParamByCmcId(cmcId);
        try {
            CmcVlanPrimaryInterface cmcVlanPrimaryInterface = getCmcFacade(snmpParam.getIpAddress())
                    .getCmcVlanPrimaryInterface(snmpParam);
            List<CmcVlanPrimaryIp> cmcVlanPrimaryIpList = getCmcFacade(snmpParam.getIpAddress())
                    .getCmcVlanPrimaryIpList(snmpParam);
            if (cmcVlanPrimaryInterface != null) {
                cmcConfigDao.batchInsertOrUpdateCC8800BVlanPriInterface(cmcVlanPrimaryInterface, cmcId);
            }
            if (cmcVlanPrimaryIpList != null && cmcVlanPrimaryIpList.size() > 0) {
                cmcConfigDao.batchInsertOrUpdateCC8800BVlanPriIpList(cmcVlanPrimaryIpList, cmcId);
            }
            if (cmcVlanPrimaryInterface != null) {
                cmcPrimaryVlan = cmcConfigDao.getCC8800BCcPrimaryVlanAsSnmp(cmcId,
                        cmcVlanPrimaryInterface.getVlanPrimaryInterface());
            }
            if (cmcVlanPrimaryInterface != null) {
                cmcPrimaryVlan.setDefaultRoute(cmcVlanPrimaryInterface.getVlanPrimaryDefaultRoute());
            }

        } catch (Exception e) {
            logger.error("refresh action{} error", "refreshCmcPrimaryVlanAsSnmp");
        }
        return cmcPrimaryVlan;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.cmc.service.CmcConfigService#getCC8800BSniConfig(java.lang.Long)
     */
    @Override
    public CcmtsSniObject getCC8800BSniConfig(Long cmcId) {
        return cmcConfigDao.getCC8800BSniConfig(cmcId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.cmc.service.CmcConfigService#setCC8800BSniConfig(com.topvision.ems.cmc.
     * facade.domain.CcmtsSniObject)
     */
    @Override
    public void setCC8800BSniConfig(CcmtsSniObject sni) {
        snmpParam = getSnmpParamByCmcId(sni.getCmcId());
        CcmtsSniObject afterModified = getCmcFacade(snmpParam.getIpAddress()).updateCc8800bSniObject(snmpParam, sni);
        StringBuilder sb = new StringBuilder();

        if (sni.getTopCcmtsSniEthInt() != null
                && !sni.getTopCcmtsSniEthInt().equals(afterModified.getTopCcmtsSniEthInt())) {
            sb.append("cmc.message.cmc.setTopCcmtsSniEthInt");
        }
        if (sni.getTopCcmtsSniMainInt() != null
                && !sni.getTopCcmtsSniMainInt().equals(afterModified.getTopCcmtsSniMainInt())) {
            sb.append("cmc.message.cmc.setTopCcmtsSniMainInt");
        }
        if (sni.getTopCcmtsSniBackupInt() != null
                && !sni.getTopCcmtsSniBackupInt().equals(afterModified.getTopCcmtsSniBackupInt())) {
            sb.append("cmc.message.cmc.setTopCcmtsSniBackupInt");
        }
        if (sb.length() > 0) {
            throw new SetValueFailException(sb.toString());
        }
        cmcConfigDao.updateCC8800BSniConfig(sni);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.cmc.service.CmcConfigService#getCmcSnmpCommunityTable(java.lang.Long)
     */
    @Override
    public CmcSnmpCommunityTable getCmcSnmpCommunityTable(Long entityId) {
        return cmcConfigDao.getCmcSnmpCommunityTable(entityId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.cmc.service.CmcConfigService#setCC8800BSnmpInfo(com.topvision.ems.cmc.facade
     * .domain.CmcSnmpCommunityTable)
     */
    @Override
    public void setCC8800BSnmpInfo(Long entityId, String readCommunity, String writeCommunity) {
        CmcSnmpCommunityTable table = new CmcSnmpCommunityTable();
        table.setEntityId(entityId);
        table.setReadCommunity(readCommunity);
        table.setWriteCommunity(writeCommunity);
        cmcConfigDao.updateCC8800BSnmpInfo(table);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.cmc.service.CmcConfigService#setCmcIpInfo(com.topvision.ems.cmc.facade.
     * domain.CmcSystemIpInfo)
     */
    @Override
    public void setCmcIpInfo(CmcSystemIpInfo cmcSystemIpInfo) {
        cmcConfigDao.updateCmcConfigIpInfo(cmcSystemIpInfo);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.cmc.service.CmcConfigService#setCC8800BSystemIpInfo(java.lang.Long,
     * com.topvision.ems.cmc.facade.domain.CmcSystemIpInfo)
     */
    @Override
    public boolean setCC8800BSystemIpInfo(Long entityId, CmcSystemIpInfo info) {
        snmpParam = getSnmpParamByCmcId(entityId);
        info.setEntityId(entityId);
        try {
            /**
             * modified by huangdongsheng 将IP与网关分开进行设置,设备在IP 与网关同时设置时存在问题
             * 
             * @data 2013-7-9
             */
            getCmcFacade(snmpParam.getIpAddress()).set8800BSystemIpInfo(snmpParam, info);
            cmcConfigDao.updateCmcConfigIpInfo(info);
        } catch (SnmpNoResponseException e) {
            int pingResult = getPingFacade(info.getTopCcmtsEthIpAddr()).ping(info.getTopCcmtsEthIpAddr(), 1000, 0);
            if (pingResult >= 0) {
                // Add by Rod 修改带入网关会返回NULL，造成更新后的网关丢失
                // info.setTopCcmtsEthGateway(ipGate);
                cmcConfigDao.updateCmcConfigIpInfo(info);
                return true;
            } else {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean setCC8800BGateway(Long entityId, CmcSystemIpInfo info) {
        info.setEntityId(entityId);
        String ip = snmpParam.getIpAddress();
        snmpParam = getSnmpParamByCmcId(entityId);
        try {
            getCmcFacade(ip).set8800BSystemIpInfo(snmpParam, info);
            cmcConfigDao.updateCmcConfigGateway(info);
        } catch (SnmpNoResponseException e) {
            int pingResult = getPingFacade(ip).ping(ip, 1000, 0);
            if (pingResult >= 0) {
                cmcConfigDao.updateCmcConfigGateway(info);
                return true;
            } else {
                return false;
            }
        }
        return true;
    }

    @Override
    public CmcSysConfig getCmcSysConfig(Long cmcId) {
        return cmcConfigDao.selectCmcSysConfig(cmcId);
    }

    @Override
    public void modifyCmcSysPiggyBack(Long cmcId, Integer piggyBack) {
        Long cmcIndex = cmcDao.getCmcIndexByCmcId(cmcId);
        snmpParam = getSnmpParamByCmcId(cmcId);
        CmcSysConfig modify = new CmcSysConfig();
        modify.setIfIndex(cmcIndex);
        modify.setTopCcmtsSysCfgPiggyback(piggyBack);
        getCmcFacade(snmpParam.getIpAddress()).updateCmcSysConfig(snmpParam, modify);
        CmcSysConfig cmcSysConfig = cmcConfigDao.selectCmcSysConfig(cmcId);
        cmcSysConfig.setTopCcmtsSysCfgPiggyback(piggyBack);
        cmcConfigDao.insertCmcSysConfig(cmcId, cmcSysConfig);
    }
}
