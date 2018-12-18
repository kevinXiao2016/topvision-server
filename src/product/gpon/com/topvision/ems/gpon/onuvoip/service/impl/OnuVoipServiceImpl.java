/***********************************************************************
 * $Id: OnuVoipServiceImpl.java,v1.0 2017年5月4日 上午11:22:39 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.gpon.onuvoip.service.impl;

import java.util.HashMap;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.epon.onu.dao.OnuDao;
import com.topvision.ems.epon.onu.domain.OltOnuAttribute;
import com.topvision.ems.epon.onu.service.OnuService;
import com.topvision.ems.epon.topology.event.OnuSynchronizedEvent;
import com.topvision.ems.epon.topology.event.OnuSynchronizedListener;
import com.topvision.ems.gpon.onuvoip.dao.OnuVoipDao;
import com.topvision.ems.gpon.onuvoip.domain.TopGponOnuPots;
import com.topvision.ems.gpon.onuvoip.domain.TopOnuIfPotsInfo;
import com.topvision.ems.gpon.onuvoip.domain.TopSIPPstnUser;
import com.topvision.ems.gpon.onuvoip.domain.TopVoIPLineStatus;
import com.topvision.ems.gpon.onuvoip.facade.OnuVoipFacade;
import com.topvision.ems.gpon.onuvoip.service.OnuVoipService;
import com.topvision.ems.network.service.EntityService;
import com.topvision.framework.service.BaseService;
import com.topvision.framework.snmp.SnmpParam;
import com.topvision.platform.facade.FacadeFactory;
import com.topvision.platform.message.service.MessageService;

/**
 * @author haojie
 * @created @2017年5月4日-上午11:22:39
 *
 */
@Service
public class OnuVoipServiceImpl extends BaseService implements OnuVoipService, OnuSynchronizedListener {
    @Autowired
    private FacadeFactory facadeFactory;
    @Autowired
    protected MessageService messageService;
    @Autowired
    private EntityService entityService;
    @Autowired
    private OnuService onuService;
    @Autowired
    private OnuVoipDao onuVoipDao;
    @Autowired
    private OnuDao onuDao;

    public static final Integer ONU_SINGLE_TOPO = 1;

    @Override
    public void insertEntityStates(OnuSynchronizedEvent event) {
        Long entityId = event.getEntityId();
        List<Long> onuIndexList = event.getOnuIndexList();
        try {
            if (onuIndexList.size() == ONU_SINGLE_TOPO) {
                refreshTopSIPPstnUser(entityId, onuIndexList.get(0));
            } else if (onuIndexList.size() > ONU_SINGLE_TOPO) {
                refreshTopSIPPstnUser(entityId);
            }
            logger.info("refreshTopSIPPstnUser finish");
        } catch (Exception e) {
            logger.error("refreshTopSIPPstnUser wrong", e);
        }

        try {
            if (onuIndexList.size() == ONU_SINGLE_TOPO) {
                refreshTopVoIPLineStatus(entityId, onuIndexList.get(0));
            } else if (onuIndexList.size() > ONU_SINGLE_TOPO) {
                refreshTopVoIPLineStatus(entityId);
            }
            logger.info("refreshTopVoIPLineStatus finish");
        } catch (Exception e) {
            logger.error("refreshTopVoIPLineStatus wrong", e);
        }

        try {
            if (onuIndexList.size() == ONU_SINGLE_TOPO) {
                refreshTopOnuIfPotsInfo(entityId, onuIndexList.get(0));
            } else if (onuIndexList.size() > ONU_SINGLE_TOPO) {
                refreshTopOnuIfPotsInfo(entityId);
            }
            logger.info("refreshTopOnuIfPotsInfo finish");
        } catch (Exception e) {
            logger.error("refreshTopOnuIfPotsInfo wrong", e);
        }

    }

    private void refreshTopOnuIfPotsInfo(Long entityId) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        List<TopOnuIfPotsInfo> potsList = getOnuVoipFacade(snmpParam).getTopOnuIfPotsInfo(snmpParam);
        onuVoipDao.batchInsertOrUpdateTopOnuIfPotsInfo(entityId, potsList);
    }

    private void refreshTopOnuIfPotsInfo(Long entityId, Long onuIndex) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        List<TopOnuIfPotsInfo> potsInfo = getOnuVoipFacade(snmpParam).getTopOnuIfPotsInfo(snmpParam, onuIndex);
        onuVoipDao.batchInsertOrUpdateTopOnuIfPotsInfo(entityId, potsInfo);
    }

    private void refreshTopVoIPLineStatus(Long entityId, Long onuIndex) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        List<TopVoIPLineStatus> topVoIPLineStatusList = getOnuVoipFacade(snmpParam).getTopVoIPLineStatus(snmpParam,
                onuIndex);
        onuVoipDao.batchInsertOrUpdateTopVoIPLineStatus(entityId, topVoIPLineStatusList);
    }

    private void refreshTopSIPPstnUser(Long entityId, Long onuIndex) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        List<TopSIPPstnUser> topSIPPstnUserList = getOnuVoipFacade(snmpParam).getTopSIPPstnUser(snmpParam, onuIndex);
        onuVoipDao.batchInsertOrUpdateTopSIPPstnUser(entityId, topSIPPstnUserList);
    }

    private void refreshTopVoIPLineStatus(Long entityId) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        List<TopVoIPLineStatus> topVoIPLineStatusList = getOnuVoipFacade(snmpParam).getTopVoIPLineStatus(snmpParam);
        onuVoipDao.batchInsertOrUpdateTopVoIPLineStatus(entityId, topVoIPLineStatusList);
    }

    private void refreshTopSIPPstnUser(Long entityId) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        List<TopSIPPstnUser> topSIPPstnUserList = getOnuVoipFacade(snmpParam).getTopSIPPstnUser(snmpParam);
        onuVoipDao.batchInsertOrUpdateTopSIPPstnUser(entityId, topSIPPstnUserList);
    }

    @PostConstruct
    @Override
    public void initialize() {
        super.initialize();
        messageService.addListener(OnuSynchronizedListener.class, this);
    }

    @PreDestroy
    @Override
    public void destroy() {
        super.destroy();
        messageService.removeListener(OnuSynchronizedListener.class, this);
    }

    private OnuVoipFacade getOnuVoipFacade(SnmpParam snmpParam) {
        return facadeFactory.getFacade(snmpParam.getIpAddress(), OnuVoipFacade.class);
    }

    @Override
    public void modifyTopSIPPstnUser(TopSIPPstnUser topSIPPstnUser) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(topSIPPstnUser.getEntityId());
        getOnuVoipFacade(snmpParam).modifyTopSIPPstnUser(snmpParam, topSIPPstnUser);
        onuVoipDao.updateTopSIPPstnUser(topSIPPstnUser);
    }

    @Override
    public void refreshTopSIPPstnUserByOnuId(Long onuId) {
        OltOnuAttribute onu = onuDao.getOnuEntityById(onuId);
        refreshTopSIPPstnUser(onu.getEntityId(), onu.getOnuIndex());
    }

    @Override
    public TopVoIPLineStatus getTopVoIPLineStatus(Long onuId) {
        return onuVoipDao.getTopVoIPLineStatus(onuId);
    }

    @Override
    public void refreshTopVoIPLineStatusByOnuId(Long onuId) {
        OltOnuAttribute onu = onuDao.getOnuEntityById(onuId);
        refreshTopVoIPLineStatus(onu.getEntityId(), onu.getOnuIndex());
    }

    public void refreshTopOnuIfPotsInfoByOnuId(Long onuId) {
        OltOnuAttribute onu = onuDao.getOnuEntityById(onuId);
        refreshTopOnuIfPotsInfo(onu.getEntityId(), onu.getOnuIndex());
    }

    @Override
    public List<TopGponOnuPots> loadGponOnuPotsList(Long onuId) {
        return onuVoipDao.getGponOnuPotsList(onuId);
    }

    @Override
    public void refreshGponOnuPotsInfo(Long onuId) {
        // POTS列表从以下4个mib取数据
        refreshTopSIPPstnUserByOnuId(onuId);
        refreshTopVoIPLineStatusByOnuId(onuId);
        // refreshTopGponSrvPotsInfoByOnuId(onuId);//模板数据,此时不做拓扑
        refreshTopOnuIfPotsInfoByOnuId(onuId);
    }

    @Override
    public TopSIPPstnUser loadGponOnuPotsConfig(HashMap<String, Object> map) {
        return onuVoipDao.getTopSIPPstnUser(map);
    }

    @Override
    public void setOnuPotsAdminStatus(Long onuId, Integer topSIPPstnUserPotsIdx, Integer potsAdminStatus) {
        OltOnuAttribute oltOnuAttribute = onuService.getOnuAttribute(onuId);
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(oltOnuAttribute.getEntityId());
        TopOnuIfPotsInfo onuIfPotsInfo = new TopOnuIfPotsInfo();
        onuIfPotsInfo.setOnuIndex(oltOnuAttribute.getOnuIndex());
        onuIfPotsInfo.setTopOnuIfPotsPotsIdx(topSIPPstnUserPotsIdx);
        onuIfPotsInfo.setTopOnuIfPotsAdminState(potsAdminStatus);

        getOnuVoipFacade(snmpParam).setOnuPotsAdminStatus(snmpParam, onuIfPotsInfo);
        onuVoipDao.updatePotsAdminStatus(onuId, topSIPPstnUserPotsIdx, potsAdminStatus);
    }

}
