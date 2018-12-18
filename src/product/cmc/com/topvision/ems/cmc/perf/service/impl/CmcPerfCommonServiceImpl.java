/***********************************************************************
 * $Id: CmcPerfCommonServiceImpl.java,v1.0 2013-12-2 上午09:32:44 $
 * 
 * @author: Rod John
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.perf.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.topvision.ems.cmc.ccmts.dao.CmcDao;
import com.topvision.ems.cmc.facade.CmcPerfCommonFacade;
import com.topvision.ems.cmc.perf.dao.CmcPerfCommonDao;
import com.topvision.ems.cmc.perf.service.CmcPerfCommonService;
import com.topvision.ems.cmc.performance.domain.CmcChannelStaticInfo;
import com.topvision.ems.cmc.performance.domain.CmcServiceQualityStatic;
import com.topvision.ems.cmc.performance.domain.CmcSignalQualityStatic;
import com.topvision.ems.cmc.performance.facade.CmcFlowQuality;
import com.topvision.ems.cmc.performance.facade.CmcServiceQuality;
import com.topvision.ems.cmc.performance.facade.CmcSignalQuality;
import com.topvision.ems.cmc.upchannel.domain.CmcUpChannelBaseShowInfo;
import com.topvision.ems.cmc.upchannel.service.CmcUpChannelService;
import com.topvision.ems.network.service.EntityService;
import com.topvision.framework.snmp.SnmpParam;
import com.topvision.platform.facade.FacadeFactory;

/**
 * @author Rod John
 * @created @2013-12-2-上午09:32:44
 * 
 */
@Service("cmcPerfCommonService")
public class CmcPerfCommonServiceImpl implements CmcPerfCommonService {
    @Resource(name = "entityService")
    private EntityService entityService;
    @Resource(name = "cmcUpChannelService")
    private CmcUpChannelService cmcUpChannelService;
    @Resource(name = "cmcDao")
    private CmcDao cmcDao;
    @Resource(name = "cmcPerfCommonDao")
    private CmcPerfCommonDao cmcPerfCommonDao;
    @Resource(name = "facadeFactory")
    private FacadeFactory facadeFactory;

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.cmc.perf.service.CmcPerfCommonService#loadCmcServiceQuality(java.lang.Long)
     */
    @Override
    public CmcServiceQualityStatic loadCmcServiceQuality(Long cmcId) {
        return cmcPerfCommonDao.loadCmcServiceQuality(cmcId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.cmc.perf.service.CmcPerfCommonService#fetchCmcServiceQuality(java.lang.
     * Long)
     */
    @Override
    public CmcServiceQuality fetchCmcServiceQuality(Long cmcId) {
        Long entityId = cmcDao.getEntityIdByCmcId(cmcId);
        Long cmcIndex = cmcDao.getCmcIndexByCmcId(cmcId);
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        return getCmcPerfCommonFacade(snmpParam.getIpAddress()).fetchCmcServiceQuality(snmpParam, cmcIndex);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.cmc.perf.service.CmcPerfCommonService#loadCmcSignalQuality(java.lang.Long)
     */
    @Override
    public List<CmcSignalQualityStatic> loadCmcSignalQuality(Long cmcId) {
        return cmcPerfCommonDao.loadCmcSignalQuality(cmcId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.cmc.perf.service.CmcPerfCommonService#fetchCmcSignalQuality(java.lang.Long)
     */
    @Override
    public List<CmcSignalQuality> fetchCmcSignalQuality(Long cmcId) {
        Long entityId = cmcDao.getEntityIdByCmcId(cmcId);
        Long cmcIndex = cmcDao.getCmcIndexByCmcId(cmcId);
        // 获得CMC的上行信道
        List<CmcUpChannelBaseShowInfo> cmcUpChannelBaseShowInfoList = cmcUpChannelService
                .getUpChannelBaseShowInfoList(cmcId);
        List<Long> channelIndexs = new ArrayList<Long>();
        for (CmcUpChannelBaseShowInfo info : cmcUpChannelBaseShowInfoList) {
            channelIndexs.add(info.getChannelIndex());
        }
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        return getCmcPerfCommonFacade(snmpParam.getIpAddress()).fetchCmcSignalQuality(snmpParam, cmcIndex,
                channelIndexs);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.cmc.perf.service.CmcPerfCommonService#loadCmcSignalQuality(java.lang.Long,
     * java.lang.Long)
     */
    @Override
    public CmcSignalQualityStatic loadCmcSignalQuality(Long cmcId, Long channelIndex) {
        return cmcPerfCommonDao.loadCmcSignalQuality(cmcId, channelIndex);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.cmc.perf.service.CmcPerfCommonService#fetchCmcSignalQuality(java.lang.Long,
     * java.lang.Long)
     */
    @Override
    public CmcSignalQuality fetchCmcSignalQuality(Long cmcId, Long channelIndex) {
        Long entityId = cmcDao.getEntityIdByCmcId(cmcId);
        Long cmcIndex = cmcDao.getCmcIndexByCmcId(cmcId);
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        return getCmcPerfCommonFacade(snmpParam.getIpAddress())
                .fetchCmcSignalQuality(snmpParam, cmcIndex, channelIndex);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.cmc.perf.service.CmcPerfCommonService#loadCmcFlowQuality(java.lang.Long)
     */
    @Override
    public List<CmcFlowQuality> loadCmcFlowQuality(Long cmcId) {
        return cmcPerfCommonDao.loadCmcFlowQuality(cmcId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.cmc.perf.service.CmcPerfCommonService#loadCmcFlowQuality(java.lang.Long,
     * java.lang.Long)
     */
    @Override
    public CmcFlowQuality loadCmcFlowQuality(Long cmcId, Long channelIndex) {
        return cmcPerfCommonDao.loadCmcFlowQuality(cmcId, channelIndex);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.cmc.perf.service.CmcPerfCommonService#loadCmcChannelFlowStatic(java.lang
     * .Long, java.lang.Long, java.lang.Long, java.lang.Long)
     */
    @Override
    public CmcChannelStaticInfo loadCmcChannelFlowStatic(Long cmcId, Long channelIndex, String startTime, String endTime) {
        return cmcPerfCommonDao.loadCmcChannelFlowStatic(cmcId, channelIndex, startTime, endTime);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.cmc.perf.service.CmcPerfCommonService#loadCmcChannelSignalStatic(java.lang
     * .Long, java.lang.Long, java.lang.Long, java.lang.Long)
     */
    @Override
    public CmcChannelStaticInfo loadCmcChannelSignalStatic(Long cmcId, Long channelIndex, String startTime,
            String endTime) {
        return cmcPerfCommonDao.loadCmcChannelSignalStatic(cmcId, channelIndex, startTime, endTime);
    }

    private CmcPerfCommonFacade getCmcPerfCommonFacade(String ip) {
        return facadeFactory.getFacade(ip, CmcPerfCommonFacade.class);
    }

}
