/***********************************************************************
 * $Id: OltPonProtectServiceImpl.java,v1.0 2013-10-25 下午3:30:37 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.ponprotect.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.epon.domain.OltPonProtect;
import com.topvision.ems.epon.domain.PonProtectConfig;
import com.topvision.ems.epon.olt.dao.OltPonDao;
import com.topvision.ems.epon.ponprotect.dao.OltPonProtectDao;
import com.topvision.ems.epon.ponprotect.domain.OltPonProtectConfig;
import com.topvision.ems.epon.ponprotect.facade.OltPonProtectFacade;
import com.topvision.ems.epon.ponprotect.service.OltPonProtectService;
import com.topvision.framework.utils.EponIndex;
import com.topvision.ems.network.service.EntityService;
import com.topvision.framework.common.LoggerUtil;
import com.topvision.framework.service.BaseService;
import com.topvision.framework.snmp.RowStatus;
import com.topvision.framework.snmp.SnmpParam;
import com.topvision.platform.facade.FacadeFactory;
import com.topvision.platform.message.event.SynchronizedEvent;
import com.topvision.platform.message.event.SynchronizedListener;
import com.topvision.platform.message.service.MessageService;

/**
 * @author flack
 * @created @2013-10-25-下午3:30:37
 *
 */
@Service("oltPonProtectService")
public class OltPonProtectServiceImpl extends BaseService implements OltPonProtectService, SynchronizedListener {
    @Autowired
    private OltPonProtectDao oltPonProtectDao;
    @Autowired
    private EntityService entityService;
    @Autowired
    private FacadeFactory facadeFactory;
    @Autowired
    private MessageService messageService;
    @Autowired
    private OltPonDao oltPonDao;

    @Override
    @PreDestroy
    public void destroy() {
        super.destroy();
        messageService.removeListener(SynchronizedListener.class, this);
    }

    @Override
    @PostConstruct
    public void initialize() {
        super.initialize();
        messageService.addListener(SynchronizedListener.class, this);
    }

    @Override
    public List<OltPonProtect> getOltPonProtectsList(Long entityId) {
        List<OltPonProtect> ponProtectList = oltPonProtectDao.getOltPonProtectList(entityId);
        if (ponProtectList.size() > 0) {
            for (OltPonProtect o : ponProtectList) {
                Long ponIdMasterIndex = oltPonDao.getPonIndex(o.getPonIdMaster());
                Long ponIdReserveIndex = oltPonDao.getPonIndex(o.getPonIdReserve());
                o.setPonIdMasterIndex(ponIdMasterIndex);
                o.setPonIdReserveIndex(ponIdReserveIndex);
                o.setPortMasterString((EponIndex.getSlotNo(ponIdMasterIndex).toString()) + '/'
                        + (EponIndex.getPonNo(ponIdMasterIndex).toString()));
                o.setPortReserveString((EponIndex.getSlotNo(ponIdReserveIndex).toString()) + '/'
                        + (EponIndex.getPonNo(ponIdReserveIndex).toString()));
            }
        }
        return ponProtectList;
    }

    @Override
    public OltPonProtect getOltPonProtectById(Long entityId, Integer protectId) {
        OltPonProtect ponProtect = oltPonProtectDao.getOltPonProtectById(entityId, protectId);
        Long ponIdMasterIndex = oltPonDao.getPonIndex(ponProtect.getPonIdMaster());
        Long ponIdReserveIndex = oltPonDao.getPonIndex(ponProtect.getPonIdReserve());
        ponProtect.setPonIdMasterIndex(ponIdMasterIndex);
        ponProtect.setPonIdReserveIndex(ponIdReserveIndex);
        ponProtect.setPortMasterString((EponIndex.getSlotNo(ponIdMasterIndex).toString()) + '/'
                + (EponIndex.getPonNo(ponIdMasterIndex).toString()));
        ponProtect.setPortReserveString((EponIndex.getSlotNo(ponIdReserveIndex).toString()) + '/'
                + (EponIndex.getPonNo(ponIdReserveIndex).toString()));
        return ponProtect;
    }

    @Override
    public void addPonProtect(OltPonProtect oltPonProtect) {
        oltPonProtectDao.addPonProtect(oltPonProtect);
    }

    @Override
    public void updateOltPonProtectById(OltPonProtect oltPonProtect) {
        oltPonProtectDao.updateOltPonProtectById(oltPonProtect);
    }

    @Override
    public void deleteOltPonProtectById(Long entityId, Integer protectId) {
        oltPonProtectDao.deleteOltPonProtectById(entityId, protectId);
    }

    @Override
    public void insertEntityStates(SynchronizedEvent event) {
        if (event.getEventType().equals("OltManagement")) {
            Long timeTmp = 0L;
            timeTmp = LoggerUtil.topoStartTimeLog(event.getIpAddress(), "OltPonProtectConfig");
            try {
                refreshOltPonProtect(event.getEntityId());
                logger.debug("refreshOltPonProtect finished!");
            } catch (Exception e) {
                if (logger.isDebugEnabled()) {
                    logger.debug("refreshOltPonProtect error:", e);
                }
            }
            LoggerUtil.topoEndTimeLog(event.getIpAddress(), "OltPonProtectConfig", timeTmp);
        }
    }

    private void refreshOltPonProtect(long entityId) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        // -------------- PON保护数据采集 --------------------- //
        try {
            List<OltPonProtectConfig> ponProtectConfigs = getPonProtectFacade(snmpParam.getIpAddress())
                    .refreshOltPonProtect(snmpParam);
            if (ponProtectConfigs != null) {
                if (ponProtectConfigs != null) {
                    try {
                        oltPonProtectDao.batchInsertOltPonProtectConfigs(ponProtectConfigs, entityId);
                    } catch (Exception e) {
                        logger.error("batch insert snmpV3UserList error:{}", e);
                    }
                }
            }
        } catch (Exception e) {
            logger.error("", e);
        }
    }

    @Override
    public void updateEntityStates(SynchronizedEvent event) {
    }

    @Override
    public void addPonProtect(OltPonProtectConfig config) {
        SnmpParam param = entityService.getSnmpParamByEntity(config.getEntityId());
        // 有管理者设置，表明希望创建一个概念行并设置该行的状态列对象为active false
        config.setTopPonPsRowstatus(RowStatus.CREATE_AND_GO);
        OltPonProtectConfig ponprotect = getPonProtectFacade(param.getIpAddress()).addPonProtect(param, config);
        ponprotect.setAlias(config.getAlias());
        oltPonProtectDao.insertOltPonProtectConfig(ponprotect);
    }

    @Override
    public List<OltPonProtectConfig> loadPPGList(Long entityId) {
        return oltPonProtectDao.loadPPGList(entityId);
    }

    @Override
    public List<Integer> loadPPGArray(Long entityId) {
        return oltPonProtectDao.loadPPGArray(entityId);
    }

    @Override
    public List<Long> loadAvialPorts(Map<String, String> map) {
        return oltPonProtectDao.loadAvialPorts(map);
    }

    @Override
    public void deletePPG(OltPonProtectConfig config, long entityId) {
        SnmpParam param = entityService.getSnmpParamByEntity(entityId);
        getPonProtectFacade(param.getIpAddress()).deletePonProtect(param, config);
        // 修改之后存DB
        oltPonProtectDao.deletePPG(config.getTopPonPSGrpIndex(), entityId);
    }

    @Override
    public OltPonProtectConfig setPonProtectAdmin(OltPonProtectConfig config, Long entityId) {
        SnmpParam param = entityService.getSnmpParamByEntity(entityId);
        // reset config object
        config = getPonProtectFacade(param.getIpAddress()).modifyOltPonProtect(param, config);
        // 修改之后存DB。由于添加PON保护组的时候保护组是可能没有任何激活端口的，但是使能后会有激活端口，所以需要把
        // 所有相关的信息都存入DB.当去使能的时候需要将倒换次数，上次倒换时间等信息清零。
        config.setEntityId(entityId);
        oltPonProtectDao.setPonProtectAdmin(config);
        return config;
    }

    @Override
    public void addPPGMembers(PonProtectConfig config) {
        SnmpParam param = entityService.getSnmpParamByEntity(config.getEntityId());
        OltPonProtectConfig ponprotect = new OltPonProtectConfig();
        ponprotect.setTopPonPSGrpIndex(config.getGroupIndex());
        // reset ponprotect object
        ponprotect = getPonProtectFacade(param.getIpAddress()).modifyOltPonProtect(param, ponprotect);
        // TODO 修改之后存DB
    }

    @Override
    public OltPonProtectConfig manuSwitch(OltPonProtectConfig config, Long entityId) {
        SnmpParam param = entityService.getSnmpParamByEntity(entityId);
        config = getPonProtectFacade(param.getIpAddress()).modifyOltPonProtect(param, config);
        // TODO 倒换成功后，刷新该保护组
        // TODO 倒换后将倒换时间与次数计入DB
        config.setEntityId(entityId);
        oltPonProtectDao.updateSwitchInfo(config);
        return config;
    }

    /**
     * 获取Facade实例
     * 
     * @param ip
     * @return
     */
    public OltPonProtectFacade getPonProtectFacade(String ip) {
        return facadeFactory.getFacade(ip, OltPonProtectFacade.class);
    }

}
