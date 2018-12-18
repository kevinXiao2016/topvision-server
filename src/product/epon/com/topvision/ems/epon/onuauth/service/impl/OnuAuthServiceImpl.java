/***********************************************************************
 * $Id: OnuAuthService.java,v1.0 2013年10月25日 下午6:00:00 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.onuauth.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.devicesupport.version.service.DeviceVersionService;
import com.topvision.ems.epon.exception.SetValueConflictException;
import com.topvision.ems.epon.olt.dao.OltPonDao;
import com.topvision.ems.epon.olt.facade.OltFacade;
import com.topvision.ems.epon.onu.dao.OnuDao;
import com.topvision.ems.epon.onu.domain.OltOnuAttribute;
import com.topvision.ems.epon.onu.domain.OltOnuAuthModify;
import com.topvision.ems.epon.onu.domain.OltTopOnuProductTable;
import com.topvision.ems.epon.onu.facade.OnuFacade;
import com.topvision.ems.epon.onuauth.dao.OnuAuthDao;
import com.topvision.ems.epon.onuauth.domain.OltAuthenMacInfo;
import com.topvision.ems.epon.onuauth.domain.OltAuthenSnInfo;
import com.topvision.ems.epon.onuauth.domain.OltAuthentication;
import com.topvision.ems.epon.onuauth.domain.OltOnuBlockAuthen;
import com.topvision.ems.epon.onuauth.domain.OltOnuBlockExtAuthen;
import com.topvision.ems.epon.onuauth.domain.OltPonOnuAuthModeTable;
import com.topvision.ems.epon.onuauth.facade.OnuAuthFacade;
import com.topvision.ems.epon.onuauth.service.OnuAuthService;
import com.topvision.ems.network.service.EntityService;
import com.topvision.framework.common.MacUtils;
import com.topvision.framework.service.BaseService;
import com.topvision.framework.snmp.SnmpParam;
import com.topvision.framework.utils.EponConstants;
import com.topvision.framework.utils.EponIndex;
import com.topvision.platform.facade.FacadeFactory;
import com.topvision.platform.message.event.SynchronizedEvent;
import com.topvision.platform.message.event.SynchronizedListener;
import com.topvision.platform.message.service.MessageService;

/**
 * @author Bravin
 * @created @2013年10月25日-下午6:00:00
 * 
 */
@Service("onuAuthService")
public class OnuAuthServiceImpl extends BaseService implements OnuAuthService, SynchronizedListener {
    @Autowired
    private OnuAuthDao onuAuthDao;
    @Autowired
    private EntityService entityService;
    @Autowired
    private FacadeFactory facadeFactory;
    @Autowired
    private OnuDao onuDao;
    @Autowired
    private OltPonDao oltPonDao;
    @Autowired
    private MessageService messageService;
    @Autowired
    private DeviceVersionService deviceVersionService;
    private Map<String, Integer> onuLevelCache;

    @Override
    @PreDestroy
    public void destroy() {
        super.destroy();
        messageService.removeListener(SynchronizedListener.class, this);
        onuLevelCache.clear();
        onuLevelCache = null;
    }

    @Override
    @PostConstruct
    public void initialize() {
        super.initialize();
        messageService.addListener(SynchronizedListener.class, this);
        onuLevelCache = new ConcurrentHashMap<String, Integer>();
    }

    @Override
    public void insertEntityStates(SynchronizedEvent event) {

        try {
            refreshOnuAuthInfo(event.getEntityId());
            logger.info("refreshOnuAuthInfo finish");
        } catch (Exception e) {
            logger.error("refreshOnuAuthInfo wrong", e);
        }

        try {
            refreshOnuAuthenBlockList(event.getEntityId());
            logger.info("refreshOnuAuthenBlockList finish");
        } catch (Exception e) {
            logger.error("refreshOnuAuthenBlockList wrong", e);
        }

        try {
            refreshOnuAuthEnable(event.getEntityId());
            logger.info("refreshOnuAuthEnable finish");
        } catch (Exception e) {
            logger.error("refreshOnuAuthEnable wrong", e);
        }

        try {
            refreshOnuAuthMode(event.getEntityId());
            logger.info("refreshOnuAuthMode finish");
        } catch (Exception e) {
            logger.error("refreshOnuAuthMode wrong", e);
        }

        try {
            refreshOnuAuthPreType(event.getEntityId());
            logger.info("refreshOnuAuthPreType finish");
        } catch (Exception e) {
            logger.error("refreshOnuAuthPreType wrong", e);
        }

    }

    @Override
    public void setOnuAuthPolicy(Long entityId, Integer policy) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        Integer newPolicy = getOnuAuthFacade(snmpParam.getIpAddress()).setOnuAuthenPolicy(snmpParam, policy);
        if (newPolicy == null) {
            throw new SetValueConflictException("Business.connection");
        } else {
            onuAuthDao.updateOnuAuthPolicy(entityId, policy);
            if (!newPolicy.equals(policy)) {
                throw new SetValueConflictException("Business.setOnuAuthPolicy");
            }
        }
    }

    @Override
    public List<OltAuthentication> getOnuAuthenPreConfigList(OltAuthentication oltAuthen) {
        return onuAuthDao.getOnuAuthenPreConfigList(oltAuthen);
    }

    @Override
    public List<OltAuthentication> getOnuAuthenListBySlot(OltAuthentication oltAuthen) {
        return onuAuthDao.getOnuAuthenListBySlot(oltAuthen);
    }

    @Override
    public List<OltAuthentication> getOnuAuthenListByEntity(Long entityId) {
        return onuAuthDao.getOnuAuthenListByEntity(entityId);
    }

    @Override
    public List<OltAuthentication> getOnuAuthenListByMac(Long entityId, String mac) {
        List<OltAuthentication> newRule = onuAuthDao.getOnuAuthenListByEntity(entityId);
        List<OltAuthentication> macList = new ArrayList<OltAuthentication>();
        OltAuthentication newMac = new OltAuthentication();
        for (OltAuthentication aNewRule : newRule) {
            String tmpMac = aNewRule.getOnuAuthenMacAddress();
            if (tmpMac != null && tmpMac.equals(mac)) {
                newMac.setPonId(aNewRule.getPonId());
                newMac.setAuthAction(aNewRule.getAuthAction());
                newMac.setAuthType(aNewRule.getAuthType());
                newMac.setOnuAuthenMacAddress(mac);
                newMac.setOnuIndex(aNewRule.getOnuIndex());
                macList.add(newMac);
            }
        }
        return macList;
    }

    @Override
    public List<OltAuthentication> getOnuAuthenListBySn(Long entityId, String sn) {
        List<OltAuthentication> newRule = onuAuthDao.getOnuAuthenListByEntity(entityId);
        List<OltAuthentication> snList = new ArrayList<OltAuthentication>();
        OltAuthentication newSn = new OltAuthentication();
        for (OltAuthentication aNewRule : newRule) {
            String tmpSn = aNewRule.getTopOnuAuthLogicSn();
            if (tmpSn != null && tmpSn.equals(sn)) {
                newSn.setPonId(aNewRule.getPonId());
                newSn.setAuthType(aNewRule.getAuthType());
                newSn.setOnuIndex(aNewRule.getOnuIndex());
                newSn.setOnuSnMode(aNewRule.getOnuSnMode());
                newSn.setTopOnuAuthLogicSn(sn);
                newSn.setTopOnuAuthPassword(aNewRule.getTopOnuAuthPassword());
                snList.add(newSn);
            }
        }
        return snList;
    }

    @Override
    public List<Long> getOnuAuthIdList(Long ponId) {
        List<Long> newList = onuAuthDao.getOnuAuthIdList(ponId);
        List<Long> onuNoList = new ArrayList<Long>();
        for (Long aNewList : newList) {
            onuNoList.add(EponIndex.getOnuNo(aNewList));
        }
        return onuNoList;
    }

    @Override
    public List<Long> getBlockOnuAuthIdList(Long ponId) {
        List<Long> newList = onuAuthDao.getBlockOnuAuthIdList(ponId);
        List<Long> blockOnuNoList = new ArrayList<Long>();
        for (Long aNewList : newList) {
            blockOnuNoList.add(EponIndex.getOnuNo(aNewList));
        }
        return blockOnuNoList;
    }

    @Override
    public void addOnuAuthenPreConfig(OltAuthentication oltAuthen) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(oltAuthen.getEntityId());
        String deviceVersion = deviceVersionService.getParamValue(oltAuthen.getEntityId(), "onuAuth", "deviceVersion");
        snmpParam.setDeviceVersion(deviceVersion);
        if (oltAuthen.getAuthType().equals(EponConstants.OLT_AUTHEN_MAC)) {
            oltAuthen.setTopOnuAuthLogicSn(null);
            oltAuthen.setTopOnuAuthPassword(null);
            OltAuthenMacInfo oltAuthenMacInfo = new OltAuthenMacInfo();
            oltAuthenMacInfo.setAuthAction(oltAuthen.getAuthAction());
            oltAuthenMacInfo.setAuthType(oltAuthen.getAuthType());
            String macStr = new MacUtils(oltAuthen.getOnuAuthenMacAddress()).toString(MacUtils.MAOHAO).toUpperCase();
            oltAuthenMacInfo.setOnuAuthenMacAddress(macStr);
            oltAuthenMacInfo.setOnuIndex(oltAuthen.getOnuIndex());
            getOnuAuthFacade(snmpParam.getIpAddress()).addOnuMacAuthen(snmpParam, oltAuthenMacInfo);
        } else if (oltAuthen.getAuthType().equals(EponConstants.OLT_AUTHEN_SN)) {
            oltAuthen.setOnuAuthenMacAddress(null);
            OltAuthenSnInfo oltAuthenSnInfo = new OltAuthenSnInfo();
            oltAuthenSnInfo.setTopOnuAuthLogicSn(oltAuthen.getTopOnuAuthLogicSn());
            oltAuthenSnInfo.setTopOnuAuthLogicSnAction(EponConstants.OLT_AUTHEN_SN_ACTION);// accept
            // oltAuthenSnInfo.setTopOnuAuthLogicSnMode(oltAuthen.getOnuSnMode());
            oltAuthenSnInfo.setTopOnuAuthPassword(oltAuthen.getTopOnuAuthPassword());
            oltAuthenSnInfo.setOnuIndex(oltAuthen.getOnuIndex());
            getOnuAuthFacade(snmpParam.getIpAddress()).addOnuSnAuthen(snmpParam, oltAuthenSnInfo);
        }
        onuAuthDao.insertOnuAuthenPreConfig(oltAuthen);
    }

    @Override
    public void deleteAuthenPreConfig(Long entityId, Long ponId, Long onuIndex, Integer authType) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        String deviceVersion = deviceVersionService.getParamValue(entityId, "onuAuth", "deviceVersion");
        snmpParam.setDeviceVersion(deviceVersion);
        if (authType.equals(EponConstants.OLT_AUTHEN_MAC)) {
            getOnuAuthFacade(snmpParam.getIpAddress()).deleteOnuMacAuthen(snmpParam, onuIndex);
        } else if (authType.equals(EponConstants.OLT_AUTHEN_SN)) {
            getOnuAuthFacade(snmpParam.getIpAddress()).deleteOnuSnAuthen(snmpParam, onuIndex);
        }
        onuAuthDao.deleteAuthenPreConfig(ponId, onuIndex);
        // 同时删除所有的ONU相关表项
        Long onuId = onuDao.getOnuIdByIndex(entityId, onuIndex);
        List<Long> list = new ArrayList<>();
        if (onuId != null) {
            list.add(onuId);
            entityService.removeEntity(list);
            // onuAuthDao.deleteOnuAllInfo(onuId);
        }

    }

    @Override
    public List<OltOnuBlockAuthen> getOnuAuthenBlockList(Long entityId, Long slotId, Long ponId) {
        if (ponId > -1) {
            return onuAuthDao.getOnuAuthenBlockList(ponId);
        } else if (slotId > -1) {
            return onuAuthDao.getOnuAuthenBlockListBySlot(slotId);
        } else {
            return onuAuthDao.getOnuAuthenBlockListByOlt(entityId);
        }
    }

    @Override
    public void refreshOnuAuthenBlockList(Long entityId) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        String deviceVersion = deviceVersionService.getParamValue(entityId, "onuAuth", "deviceVersion");
        snmpParam.setDeviceVersion(deviceVersion);
        List<OltOnuBlockAuthen> onuBlockAuthenList = getOnuAuthFacade(snmpParam.getIpAddress()).getBlockAuthens(
                snmpParam);
        for (OltOnuBlockAuthen aTemp : onuBlockAuthenList) {
            aTemp.setEntityId(entityId);
        }
        onuAuthDao.refreshOnuAuthenBlockList(entityId, onuBlockAuthenList);
        List<OltOnuBlockExtAuthen> onuBlockExtAuthenList = getOltFacade(snmpParam.getIpAddress()).getDomainInfoList(
                snmpParam, OltOnuBlockExtAuthen.class);
        for (OltOnuBlockExtAuthen aTemp : onuBlockExtAuthenList) {
            aTemp.setEntityId(entityId);
        }
        onuAuthDao.refreshOnuAuthenExtBlockList(onuBlockExtAuthenList);
    }

    /**
     * 从设备上刷新ONU认证表
     * 
     * @param entityId
     *            设备 ID
     */
    @Override
    public void refreshOnuAuthInfo(Long entityId) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        String deviceVersion = deviceVersionService.getParamValue(entityId, "onuAuth", "deviceVersion");
        snmpParam.setDeviceVersion(deviceVersion);
        List<OltAuthenMacInfo> macInfo = getOnuAuthFacade(snmpParam.getIpAddress()).getAuthenMacInfos(snmpParam);
        List<OltAuthenSnInfo> snInfo = getOnuAuthFacade(snmpParam.getIpAddress()).getAuthenSnInfos(snmpParam);
        List<OltAuthentication> authRule = new ArrayList<OltAuthentication>();
        if (macInfo != null && macInfo.size() != 0) {
            for (OltAuthenMacInfo aMacInfo : macInfo) {
                OltAuthentication tempAuth = new OltAuthentication();
                tempAuth.setEntityId(entityId);
                if (aMacInfo.getAuthAction() != null && aMacInfo.getOnuAuthenMacAddress() != null
                        && aMacInfo.getOnuIndex() != null) {
                    tempAuth.setAuthAction(aMacInfo.getAuthAction());
                    tempAuth.setOnuAuthenMacAddress(aMacInfo.getOnuAuthenMacAddress());
                    tempAuth.setOnuIndex(aMacInfo.getOnuIndex());
                    tempAuth.setAuthType(EponConstants.OLT_AUTHEN_MAC);
                    authRule.add(tempAuth);
                } else {
                    logger.debug("some dirty data in onuMacAuthInfo");
                }
            }
        }
        if (snInfo != null && snInfo.size() != 0) {
            for (OltAuthenSnInfo aSnInfo : snInfo) {
                OltAuthentication tempAuth = new OltAuthentication();
                tempAuth.setEntityId(entityId);
                if (aSnInfo.getOnuIndex() != null && aSnInfo.getTopOnuAuthLogicSn() != null
                        && aSnInfo.getTopOnuAuthPassword() != null && aSnInfo.getTopOnuAuthLogicSnMode() != null) {
                    tempAuth.setOnuIndex(aSnInfo.getOnuIndex());
                    tempAuth.setOnuSnMode(aSnInfo.getTopOnuAuthLogicSnMode());
                    tempAuth.setTopOnuAuthLogicSn(aSnInfo.getTopOnuAuthLogicSn());
                    tempAuth.setTopOnuAuthPassword(aSnInfo.getTopOnuAuthPassword());
                    tempAuth.setAuthType(EponConstants.OLT_AUTHEN_SN);
                    tempAuth.setAuthAction(EponConstants.OLT_AUTHEN_SN_ACTION);
                    authRule.add(tempAuth);
                } else {
                    logger.debug("some dirty data in onuSnAuthInfo");
                }
            }
        }
        if (authRule != null && authRule.size() > 0) {
            onuAuthDao.batchInsertOltOnuAuthInfo(authRule);
        } else {
            onuAuthDao.deleteAllOnuAuth(entityId);
        }
    }

    @Override
    public void deleteOnuAuthBlock(Long ponId, Long onuIndex) {
        onuAuthDao.deleteOnuAuthBlock(ponId, onuIndex);
    }

    @Override
    public void deleteOnuAuthBlockByMac(Long entityId, Long ponIndex, String mac) {
        Long ponId = oltPonDao.getPonIdByPonIndex(entityId, ponIndex);
        onuAuthDao.deleteOnuAuthBlockByMac(ponId, mac);
    }

    @Override
    public List<OltAuthentication> getOnuAuthMacList(Long entityId) {
        return onuAuthDao.getOnuAuthMacList(entityId);
    }

    @Override
    public List<OltAuthentication> getOnuAuthSnList(Long entityId) {
        return onuAuthDao.getOnuAuthSnList(entityId);
    }

    /**
     * PON口认证模式
     */
    @Override
    public void refreshOnuAuthMode(Long entityId) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        List<OltPonOnuAuthModeTable> list = getOltFacade(snmpParam.getIpAddress()).getDomainInfoList(snmpParam,
                OltPonOnuAuthModeTable.class);
        for (OltPonOnuAuthModeTable oltPonOnuAuthModeTable : list) {
            oltPonOnuAuthModeTable.setEntityId(entityId);
        }
        onuAuthDao.refreshOnuAuthMode(entityId, list);
    }

    @Override
    public void modifyOnuAuthMode(Long entityId, Long ponIndex, Integer mode) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        OltPonOnuAuthModeTable oltPonOnuAuthModeTable = new OltPonOnuAuthModeTable();
        oltPonOnuAuthModeTable.setEntityId(entityId);
        oltPonOnuAuthModeTable.setPonIndex(ponIndex);
        oltPonOnuAuthModeTable.setTopPonOnuAuthMode(mode);
        getOnuAuthFacade(snmpParam.getIpAddress()).setOnuAuthMode(snmpParam, oltPonOnuAuthModeTable);
        Long ponId = oltPonDao.getPonIdByPonIndex(entityId, ponIndex);
        oltPonOnuAuthModeTable.setPonId(ponId);
        onuAuthDao.updateOnuAuthMode(oltPonOnuAuthModeTable);
        onuAuthDao.deletePonAuthRule(entityId, ponId);

        // 同时删除所有的ONU相关表项
        List<Long> list = onuDao.getOnuIdByPonId(entityId, ponId);
        if (list != null) {
            entityService.removeEntity(list);
            /*for (Long onuId : list) {
                onuAuthDao.deleteOnuAllInfo(onuId);
            }*/
        }
    }

    @Override
    public List<OltPonOnuAuthModeTable> getOnuAuthMode(Long entityId) {
        return onuAuthDao.getOnuAuthMode(entityId);
    }

    @Override
    public Integer getPonOnuAuthMode(Long entityId, Long ponIndex) {
        return onuAuthDao.getPonOnuAuthMode(entityId, ponIndex);
    }

    /**
     * 获取ONU预配置类型
     * 
     * @param entityId
     * @return
     */
    @Override
    public List<OltOnuAttribute> getOnuPreTypeByPonId(Long ponId) {
        return onuDao.getOnuPreTypeByPonId(ponId);
    }

    /**
     * 设置ONU预配置类型
     * 
     * @param entityId
     * @param onuIndex
     * @param type
     */
    @Override
    public void modifyOnuPreType(Long entityId, Long onuIndex, String type) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        OltTopOnuProductTable se = new OltTopOnuProductTable();
        se.setOnuIndex(onuIndex);
        se.setTopOnuProductTypeNum(Integer.parseInt(type));
        getOnuFacade(snmpParam.getIpAddress()).setOnuPreType(snmpParam, se);
        onuAuthDao.updateOnuPreType(entityId, onuIndex, type);
    }

    @Override
    public void refreshOnuAuthEnable(Long entityId) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        List<OltPonOnuAuthModeTable> list = getOltFacade(snmpParam.getIpAddress()).getDomainInfoList(snmpParam,
                OltPonOnuAuthModeTable.class);
        for (OltPonOnuAuthModeTable oltPonOnuAuthModeTable : list) {
            oltPonOnuAuthModeTable.setEntityId(entityId);
        }
        onuAuthDao.refreshOnuAuthEnable(entityId, list);
    }

    @Override
    public void refreshOnuAuthPreType(Long entityId) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        List<OltTopOnuProductTable> list = getOnuFacade(snmpParam.getIpAddress()).getOnuPreType(snmpParam);
        for (OltTopOnuProductTable l : list) {
            l.setEntityId(entityId);
            l.setOnuIndex(EponIndex.getOnuIndex(l.getTopOnuProductCardIndex(), l.getTopOnuProductPonIndex(),
                    l.getTopOnuProductOnuIndex()));
        }
        onuAuthDao.updateAllOnuPreType(list);
    }

    @Override
    public void modifyOnuAuthEnable(Long entityId, Long ponId, Integer status) {
        Long ponIndex = oltPonDao.getPonIndex(ponId);
        OltPonOnuAuthModeTable tmp = new OltPonOnuAuthModeTable();
        tmp.setEntityId(entityId);
        tmp.setPonIndex(ponIndex);
        tmp.setTopPonOnuAuthMode(status);
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        getOnuAuthFacade(snmpParam.getIpAddress()).setOnuAuthMode(snmpParam, tmp);
        onuAuthDao.updateOnuAuthEnable(tmp);
    }

    @Override
    public List<OltPonOnuAuthModeTable> getOnuAuthEnable(Long entityId) {
        return onuAuthDao.getOnuAuthEnable(entityId);
    }

    @Override
    public void onuAuthMacInstead(Long entityId, Long onuIndex, String mac) throws Exception {
        OltOnuAuthModify oltOnuAuthModify = new OltOnuAuthModify();
        oltOnuAuthModify.setEntityId(entityId);
        oltOnuAuthModify.setOnuIndex(onuIndex);
        oltOnuAuthModify.setTopOnuModifyMacAddress(mac);
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        getOnuAuthFacade(snmpParam.getIpAddress()).onuAuthInstead(snmpParam, oltOnuAuthModify);
        OltAuthentication oltAuthentication = new OltAuthentication();
        oltAuthentication.setEntityId(entityId);
        oltAuthentication.setOnuIndex(onuIndex);
        oltAuthentication.setOnuAuthenMacAddress(mac);
        onuAuthDao.updateOltAuthentication(oltAuthentication);
        OltOnuAttribute oltOnuAttribute = new OltOnuAttribute();
        oltOnuAttribute.setEntityId(entityId);
        oltOnuAttribute.setOnuIndex(onuIndex);
        oltOnuAttribute.setOnuMacAddress(new MacUtils(mac).longValue());
        oltOnuAttribute.setOnuMac(mac);
        onuDao.updateOltOnuAttribute(oltOnuAttribute);
    }

    @Override
    public void onuAuthSnInstead(Long entityId, Long onuIndex, String sn, String password) throws Exception {
        OltOnuAuthModify oltOnuAuthModify = new OltOnuAuthModify();
        oltOnuAuthModify.setEntityId(entityId);
        oltOnuAuthModify.setOnuIndex(onuIndex);
        oltOnuAuthModify.setTopOnuModifyLogicSn(sn);
        oltOnuAuthModify.setTopOnuModifyPwd(password);
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        getOnuAuthFacade(snmpParam.getIpAddress()).onuAuthInstead(snmpParam, oltOnuAuthModify);
        OltAuthentication oltAuthentication = new OltAuthentication();
        oltAuthentication.setEntityId(entityId);
        oltAuthentication.setOnuIndex(onuIndex);
        oltAuthentication.setTopOnuAuthLogicSn(sn);
        oltAuthentication.setTopOnuAuthPassword(password);
        onuAuthDao.updateOltAuthentication(oltAuthentication);
    }

    private OnuFacade getOnuFacade(String ip) {
        return facadeFactory.getFacade(ip, OnuFacade.class);
    }

    private OnuAuthFacade getOnuAuthFacade(String ip) {
        return facadeFactory.getFacade(ip, OnuAuthFacade.class);
    }

    private OltFacade getOltFacade(String ip) {
        return facadeFactory.getFacade(ip, OltFacade.class);
    }

    @Override
    public List<OltAuthentication> loadRejectedMacList(Long entityId, Long ponId) {
        return onuAuthDao.loadRejectedMacList(entityId, ponId);
    }

    @Override
    public void updateEntityStates(SynchronizedEvent event) {
    }

    @Override
    public OltAuthentication getOltAuthenticationByIndex(Long entityId, Long onuIndex) {
        return onuAuthDao.selectOltAuthenticationByIndex(entityId, onuIndex);
    }

    @Override
    public Map<String, Integer> getOnuLevelCache() {
        return onuLevelCache;
    }

}
