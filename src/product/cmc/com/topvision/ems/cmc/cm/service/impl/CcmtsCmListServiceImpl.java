/***********************************************************************
 * $Id: CcmtsCmListServiceImpl.java,v1.0 2013-10-30 上午9:48:30 $
 * 
 * @author: YangYi
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.cm.service.impl;

import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.Resource;

import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.cmc.ccmts.service.CmcService;
import com.topvision.ems.cmc.cm.dao.CcmtsCmListDao;
import com.topvision.ems.cmc.cm.dao.CmDao;
import com.topvision.ems.cmc.cm.domain.Cm3Signal;
import com.topvision.ems.cmc.cm.domain.CmSignal;
import com.topvision.ems.cmc.cm.service.CcmtsCmListService;
import com.topvision.ems.cmc.cm.service.CmService;
import com.topvision.ems.cmc.facade.domain.CmAttribute;
import com.topvision.ems.cmc.facade.domain.CmStatusForContactCmc;
import com.topvision.ems.cmc.facade.domain.DocsIf3CmtsCmUsStatus;
import com.topvision.ems.cmc.facade.domain.DocsIfDownstreamChannelForContactCmc;
import com.topvision.ems.cmc.facade.domain.DocsIfSignalQualityForContactCmc;
import com.topvision.ems.cmc.facade.domain.DocsIfUpstreamChannelForContactCmc;
import com.topvision.ems.cmc.flap.dao.CmFlapDao;
import com.topvision.ems.cmc.performance.facade.CmFlap;
import com.topvision.ems.cmc.remotequerycm.facade.domain.Cm2RemoteQuery;
import com.topvision.ems.cmc.remotequerycm.facade.domain.Cm3DsRemoteQuery;
import com.topvision.ems.cmc.remotequerycm.facade.domain.Cm3UsRemoteQuery;
import com.topvision.ems.cmc.remotequerycm.service.CmRemoteQueryService;
import com.topvision.ems.cmc.service.impl.CmcBaseCommonService;
import com.topvision.ems.cmc.topology.dao.CmcDiscoveryDao;
import com.topvision.ems.cmc.util.CmcConstants;
import com.topvision.framework.utils.CmcIndexUtils;
import com.topvision.ems.message.Message;
import com.topvision.ems.message.MessagePusher;
import com.topvision.ems.network.service.EntityService;
import com.topvision.ems.template.service.EntityTypeService;
import com.topvision.framework.common.MacUtils;
import com.topvision.framework.ping.CmdPing;
import com.topvision.framework.snmp.SnmpParam;
import com.topvision.platform.domain.UserContext;
import com.topvision.platform.service.SystemPreferencesService;

/**
 * @author YangYi
 * @created @2013-10-30-上午9:48:30
 * 
 */
@Service("ccmtsCmListService")
public class CcmtsCmListServiceImpl extends CmcBaseCommonService implements CcmtsCmListService {
    @Resource(name = "cmService")
    private CmService cmService;
    @Resource(name = "cmcService")
    private CmcService cmcService;
    @Resource(name = "ccmtsCmListDao")
    private CcmtsCmListDao ccmtsCmListDao;
    @Resource(name = "cmcDiscoveryDao")
    private CmcDiscoveryDao cmcDiscoveryDao;
    @Autowired
    private CmFlapDao cmFlapDao;

    @Override
    public List<CmAttribute> showCmByCpeIp(String cpeIp) {
        return ccmtsCmListDao.getCmByCpeIp(cpeIp);
    }

    @Override
    public void restartAllCm(Long cmcId) {
        Long cmcIndex = cmcDao.getCmcIndexByCmcId(cmcId);
        long entityId = cmcDao.getEntityIdByCmcId(cmcId);
        snmpParam = getSnmpParamByEntityId(entityId);
        getCcmtsCmListFacade(snmpParam.getIpAddress()).restartAllCm(snmpParam, cmcIndex);
    }

    @Override
    public void restartCmByMac(Long cmcId, String mac) {
        CmAttribute cm = ccmtsCmListDao.selectCmAttribute(cmcId, mac);
        long entityId = cmcDao.getEntityIdByCmcId(cmcId);
        snmpParam = getSnmpParamByEntityId(entityId);
        getCcmtsCmListFacade(snmpParam.getIpAddress()).restartCmByCcmts(snmpParam, cm.getStatusIndex());
        ccmtsCmListDao.updateCmStatus(cmcId, mac, 1);
    }

    @Override
    public void deleteCmcOfflineCm(long cmcId) {
        cmcDiscoveryDao.deleteCmcOfflineCm(cmcId);
    }

    @Override
    public void clearAllOfflineCmsOnCC(Long cmcId) {
        Long cmcIndex = cmcDao.getCmcIndexByCmcId(cmcId);
        long entityId = cmcDao.getEntityIdByCmcId(cmcId);
        snmpParam = getSnmpParamByEntityId(entityId);
        getCcmtsCmListFacade(snmpParam.getIpAddress()).clearAllOfflineCmsOnCC(snmpParam, cmcIndex);
    }

    // TODO Merge By Victor@20130622snmpParam是否有重复获取，建议下次修改时优化
    @Override
    public List<CmAttribute> refreshContactedCmList(Long cmcId, Long cmcIndex) {
        List<CmAttribute> cmAttributeList = cmService.getRealtimeCmAttributeByCmcId(cmcId);
        try {
            refreshCmFlap(cmcId);
        } catch (Exception e) {
            logger.error("", e);
        }
        return cmAttributeList;
    }

    private void refreshCmFlap(Long cmcId) {
        Long entityId = cmcService.getEntityIdByCmcId(cmcId);
        snmpParam.setIpAddress(cmcService.getSnmpParamByEntityId(entityId).getIpAddress());
        List<CmFlap> cmFlaps = getCcmtsCmListFacade(snmpParam.getIpAddress()).getCmFlap(snmpParam);
        cmFlapDao.batchInsertOrUpdateCmFlap(cmFlaps, entityId);
    }

    @Override
    public List<CmAttribute> getCmListByCmcId(Long cmcId) {
        return ccmtsCmListDao.queryCmListByCmcId(cmcId);
    }
}
