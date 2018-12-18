/***********************************************************************
 * $Id: CmcServiceImpl.java,v1.0 2011-10-25 下午04:30:31 $
 * 
 * @author: loyal
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.ccmts.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.cmc.ccmts.dao.CmcChannelDao;
import com.topvision.ems.cmc.ccmts.domain.ChannelPerfInfo;
import com.topvision.ems.cmc.ccmts.facade.domain.CmcPort;
import com.topvision.ems.cmc.ccmts.service.CmcChannelService;
import com.topvision.ems.cmc.ccmts.service.CmcService;
import com.topvision.ems.cmc.cm.service.CmService;
import com.topvision.ems.cmc.domain.CmcDownChannelBaseShowInfo;
import com.topvision.ems.cmc.downchannel.dao.CmcDownChannelDao;
import com.topvision.ems.cmc.downchannel.domain.CmcDSIpqamBaseInfo;
import com.topvision.ems.cmc.downchannel.facade.CmcIpqamCollectFacade;
import com.topvision.ems.cmc.downchannel.service.CmcDownChannelService;
import com.topvision.ems.cmc.facade.CmcFacade;
import com.topvision.ems.cmc.facade.domain.CmAttribute;
import com.topvision.ems.cmc.performance.domain.ChannelCmNum;
import com.topvision.ems.cmc.service.impl.CmcBaseCommonService;
import com.topvision.ems.cmc.topology.dao.CmcDiscoveryDao;
import com.topvision.ems.cmc.upchannel.domain.CmcUpChannelBaseShowInfo;
import com.topvision.ems.cmc.upchannel.service.CmcUpChannelService;
import com.topvision.framework.utils.CmcIndexUtils;
import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.network.dao.PortDao;
import com.topvision.ems.network.domain.Port;
import com.topvision.ems.network.service.EntityService;
import com.topvision.ems.template.service.EntityTypeService;
import com.topvision.framework.http.HttpParam;

/**
 * 基本功能实现
 * 
 * @author loyal
 * @created @2011-10-25-下午04:30:31
 * 
 */
@Service("cmcChannelService")
public class CmcChannelServiceImpl extends CmcBaseCommonService implements CmcChannelService {
    private final Logger logger = LoggerFactory.getLogger(CmcChannelServiceImpl.class);
    @Autowired
    private CmcChannelDao cmcChannelDao;
    @Resource(name = "cmcDiscoveryDao")
    private CmcDiscoveryDao cmcDiscoveryDao;
    @Resource(name = "cmService")
    private CmService cmService;
    @Resource(name = "cmcService")
    private CmcService cmcService;
    @Resource(name = "cmcDownChannelService")
    private CmcDownChannelService cmcDownChannelService;
    @Resource(name = "cmcUpChannelService")
    private CmcUpChannelService cmcUpChannelService;
    @Autowired
    private EntityService entityService;
    @Autowired
    private EntityTypeService entityTypeService;
    @Autowired
    private CmcDownChannelDao cmcDownChannelDao;
    @Autowired
    private PortDao portDao;

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.cmc.service.CmcChannelService#getCmtsPort(java.lang .Long,
     * java.lang.Long)
     */
    @Override
    public Port getCmtsPort(Long entityId, Long ifIndex) {
        return cmcChannelDao.selectCmtsPort(entityId, ifIndex);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.cmc.service.CmcChannelService#refreshCmtsPorts(java .lang.Long)
     */
    @Override
    public void refreshCmtsPorts(Long cmcId) {
        snmpParam = getSnmpParamByCmcId(cmcId);
        CmcFacade cmcFacade = getCmcFacade(snmpParam.getIpAddress());
        List<CmcPort> cmcPorts = cmcFacade.getCmc8800BPorts(snmpParam);
        cmcDiscoveryDao.batchInsertCmtsPorts(cmcId, cmcPorts);
    }

    @Override
    public Long getChannelIndexByPortId(Long cmcPortId) {
        return cmcChannelDao.getChannelIndexByPortId(cmcPortId);
    }

    @Override
    public List<ChannelPerfInfo> getCmcChannelPerfInfoList(Long cmcId) {
        return cmcChannelDao.getCmcChannelPerfInfoList(cmcId);
    }

    @Override
    public List<Long> batchChangeChannelAdminstatus(Long cmcId, List<Long> channelIndexs, Integer status) {
        // 开启DOCSIS信道,关闭DOCSIS、IPQAM信道

        List<Long> successList = new ArrayList<Long>();
        List<Long> failureList = new ArrayList<Long>();

        Long entityId = cmcDao.getEntityIdByCmcId(cmcId);
        snmpParam = getSnmpParamByEntityId(entityId);
        String ip = snmpParam.getIpAddress();
        CmcFacade cmcFacade = getCmcFacade(ip);

        for (Long ifIndex : channelIndexs) {
            try {
                int statusAfter = cmcFacade.changeChannelAdminstatus(snmpParam, ifIndex, status);
                if (status == statusAfter) {
                    successList.add(ifIndex);
                } else {
                    failureList.add(CmcIndexUtils.getChannelId(ifIndex));
                }
            } catch (Exception e) {
                failureList.add(CmcIndexUtils.getChannelId(ifIndex));
                logger.debug("", e);
            }
        }

        // 存入数据库
        cmcChannelDao.batchUpdateChannelAdminStatus(cmcId, successList, status);

        Entity entity = entityService.getEntity(entityId);
        if (entityTypeService.isCcmtsWithAgent(entity.getTypeId())) {
            // Modified by huangdongsheng 修改处理，仅对8800B设备使用http进行采集
            // 获取IPQAM信道信息
            HttpParam httpParam = new HttpParam();
            httpParam.setIpAddress(ip);
            CmcIpqamCollectFacade httpFacade = getFacade(ip, CmcIpqamCollectFacade.class);
            List<CmcDSIpqamBaseInfo> ipqamBaseList = httpFacade.getDSChannelIpqamBaseList(httpParam);

            cmcDownChannelDao.batchInsertCC8800BDSIPQAMBaseList(ipqamBaseList, cmcId);
        }

        return failureList;
    }

    @Override
    public List<ChannelCmNum> getChannelCmNumListNew(Long cmcId, Integer channelType) {
        return cmcChannelDao.getChannelCmNumList(cmcId, channelType);
    }

    @Override
    @Deprecated
    public List<ChannelCmNum> getChannelCmNumList(Long cmcId) {
        Long entityID = cmcService.getEntityIdByCmcId(cmcId);
        List<CmAttribute> cmList = cmService.getCmAttributeByCmcId(cmcId);
        Map<String, ChannelCmNum> temp = new HashMap<String, ChannelCmNum>();

        List<CmcUpChannelBaseShowInfo> allUpList = cmcUpChannelService.getUpChannelBaseShowInfoList(cmcId);
        Map<Long, Long> portMap = new HashMap<Long, Long>();
        for (CmcUpChannelBaseShowInfo up : allUpList) {
            portMap.put(up.getChannelIndex(), up.getCmcPortId());
        }
        List<CmcDownChannelBaseShowInfo> allDownList = cmcDownChannelService.getDownChannelBaseShowInfoList(cmcId);
        for (CmcDownChannelBaseShowInfo down : allDownList) {
            portMap.put(down.getChannelIndex(), down.getCmcPortId());
        }
        for (CmAttribute cm : cmList) {
            Long upChannelID = cm.getUpChannelId();
            ChannelCmNum upChan = temp.get("U" + upChannelID);
            if (upChan == null) {
                upChan = new ChannelCmNum();
                upChan.setChannelId(upChannelID.intValue());

                upChan.setChannelType(0);
                temp.put("U" + upChannelID, upChan);
            }

            Long downChannelID = cm.getDownChannelId();
            ChannelCmNum downChan = temp.get("D" + downChannelID);
            if (downChan == null) {
                downChan = new ChannelCmNum();
                downChan.setChannelId(downChannelID.intValue());
                downChan.setChannelType(1);
                temp.put("D" + downChannelID, downChan);
            }
            @SuppressWarnings("unused")
            Port p = portDao.getPortByIfIndex(entityID, cm.getStatusUpChannelIfIndex());
            upChan.setChannelIndex(cm.getStatusUpChannelIfIndex());
            upChan.setCmcPortId(portMap.get(cm.getStatusUpChannelIfIndex()));
            downChan.setChannelIndex(cm.getStatusDownChannelIfIndex());
            downChan.setCmcPortId(portMap.get(cm.getStatusDownChannelIfIndex()));
            upChan.setCmcId(cmcId);
            downChan.setCmcId(cmcId);
            upChan.setEntityId(entityID);
            downChan.setEntityId(entityID);

            upChan.setCmNumTotal(upChan.getCmNumTotal() + 1);
            downChan.setCmNumTotal(downChan.getCmNumTotal() + 1);
            int status = cm.getStatusValue();
            // String statusStr = CmAttribute.cmtsCmStatusValue[status];
            boolean onlineStatus = CmAttribute.isCmOnline(status);
            boolean offlineStatus = CmAttribute.isCmOffline(status);
            if (onlineStatus) {
                upChan.setCmNumOnline(upChan.getCmNumOnline() + 1);
                downChan.setCmNumOnline(downChan.getCmNumOnline() + 1);
            } else if (offlineStatus) {
                upChan.setCmNumOffline(upChan.getCmNumOffline() + 1);
                downChan.setCmNumOffline(downChan.getCmNumOffline() + 1);
            } else {
                upChan.setCmNumActive(upChan.getCmNumActive() + 1);
                downChan.setCmNumActive(downChan.getCmNumActive() + 1);
            }
        }

        List<ChannelCmNum> allCmList = new ArrayList<ChannelCmNum>();
        for (int i = 1; i <= 4; i++) {
            ChannelCmNum channelCM = temp.get("U" + i);
            if (channelCM != null) {
                allCmList.add(channelCM);
            }

        }
        for (int i = 1; i <= 16; i++) {
            ChannelCmNum channelCM = temp.get("D" + i);
            if (channelCM != null) {
                allCmList.add(channelCM);
            }

        }

        return allCmList;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.cmc.ccmts.service.CmcChannelService#getDSChannelFrequencyListByPortId(java
     * .lang.Long, java.lang.Long)
     */
    @Override
    public Map<String, List<?>> getDSChannelFrequencyListByPortId(Long cmcPortId, Long cmcId) {
        Map<String, List<?>> map = new HashMap<>();
        List<CmcUpChannelBaseShowInfo> upList = cmcUpChannelService.getUpChannelFrequencyListByCmcId(cmcId);
        List<CmcDownChannelBaseShowInfo> downList = cmcDownChannelService.getDownChannelListByPortId(cmcPortId);

        map.put("upList", upList);
        map.put("downList", downList);
        return map;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.cmc.ccmts.service.CmcChannelService#getUSChannelFrequencyListByPortId(java
     * .lang.Long, java.lang.Long)
     * 
     */
    @Override
    public Map<String, List<?>> getUSChannelFrequencyListByPortId(Long cmcPortId, Long cmcId) {
        Map<String, List<?>> map = new HashMap<>();
        List<CmcUpChannelBaseShowInfo> upList = cmcUpChannelService.getUpChannelListByPortId(cmcPortId);
        List<CmcDownChannelBaseShowInfo> downList = cmcDownChannelService.getDownChannelFrequencyListByCmcId(cmcId);

        map.put("upList", upList);
        map.put("downList", downList);
        return map;
    }

}