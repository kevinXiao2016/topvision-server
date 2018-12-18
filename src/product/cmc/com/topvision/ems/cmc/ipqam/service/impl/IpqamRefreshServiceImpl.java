/***********************************************************************
 * $Id: IpqamTopoServiceImpl.java,v1.0 2016年5月6日 下午5:07:45 $
 * 
 * @author: loyal
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.ipqam.service.impl;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.cmc.ccmts.service.CmcService;
import com.topvision.ems.cmc.downchannel.domain.CmcFpgaSpecification;
import com.topvision.ems.cmc.downchannel.facade.CmcIpqamCollectFacade;
import com.topvision.ems.cmc.ipqam.dao.IpqamRefreshDao;
import com.topvision.ems.cmc.ipqam.domain.CmcEqamProgram;
import com.topvision.ems.cmc.ipqam.domain.CmcEqamStatus;
import com.topvision.ems.cmc.ipqam.domain.IpqamData;
import com.topvision.ems.cmc.ipqam.domain.ProgramIn;
import com.topvision.ems.cmc.ipqam.domain.ProgramOut;
import com.topvision.ems.cmc.ipqam.service.IpqamRefreshService;
import com.topvision.ems.cmc.service.impl.CmcBaseCommonService;
import com.topvision.ems.devicesupport.version.service.DeviceVersionService;
import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.network.service.EntityService;
import com.topvision.ems.template.service.EntityTypeService;
import com.topvision.framework.http.HttpParam;
import com.topvision.platform.message.event.CmcSynchronizedEvent;
import com.topvision.platform.message.event.CmcSynchronizedListener;
import com.topvision.platform.message.service.MessageService;

/**
 * @author loyal
 * @created @2016年5月6日-下午5:07:45
 * 
 */
@Service("ipqamTopoService")
public class IpqamRefreshServiceImpl extends CmcBaseCommonService
        implements IpqamRefreshService, CmcSynchronizedListener {
    @Autowired
    private MessageService messageService;
    @Autowired
    private EntityTypeService entityTypeService;
    @Resource(name = "deviceVersionService")
    private DeviceVersionService deviceVersionService;
    @Autowired
    private EntityService entityService;
    @Autowired
    private IpqamRefreshDao ipqamRefreshDao;
    @Autowired
    private CmcService cmcService;

    @PreDestroy
    public void destroy() {
        super.destroy();
        messageService.removeListener(CmcSynchronizedListener.class, this);
    }

    @PostConstruct
    public void initialize() {
        super.initialize();
        messageService.addListener(CmcSynchronizedListener.class, this);
    }

    @Override
    public void insertEntityStates(CmcSynchronizedEvent event) {
        long entityId = event.getEntityId();
        if (event.getEntityType().equals(entityTypeService.getCcmtswithoutagentType())) {
            try {
                refreshCmcIpqamA(entityId, event.getCmcIndexList());
                logger.info("refreshCmcIpqam finish");
            } catch (Exception e) {
                logger.error("refreshCmcIpqam wrong", e);
            }
        } else {
            try {
                refreshCmcIpqamB(entityId);
                logger.info("refreshOltIpqam finish");
            } catch (Exception e) {
                logger.error("refreshOltIpqam wrong", e);
            }

            // 为了兼容之前的通过http方式来获取信息的功能，在独立E型尚不支持snmp的方式的时候，临时应用
            try {
                refreshCmcIpqamByHttp(entityId);
                logger.info("refreshCmcIpqamByHttp finish");
            } catch (Exception e) {
                logger.error("refreshCmcIpqamByHttp wrong", e);
            }
        }
    }

    @Override
    public void refreshEqamInfo(Long cmcId) {
        Entity entity = entityService.getEntity(cmcId);
        if (entityTypeService.isCcmtsWithAgent(entity.getTypeId())) {
            snmpParam = entityService.getSnmpParamByEntity(cmcId);
            List<CmcEqamStatus> cmcEqamStatusList = getCmcFacade(snmpParam.getIpAddress())
                    .refreshIpqamStatusB(snmpParam, cmcId);
            if (cmcEqamStatusList != null) {
                ipqamRefreshDao.batchInsertCmcBIpqam(cmcEqamStatusList, cmcId);
            }
        } else {
            snmpParam = entityService.getSnmpParamByEntity(entity.getParentId());
            Long cmcIndex = cmcService.getCmcIndexByCmcId(cmcId);
            List<CmcEqamStatus> cmcEqamStatusList = getCmcFacade(snmpParam.getIpAddress())
                    .refreshIpqamStatusA(snmpParam, entity.getParentId(), cmcIndex);
            if (cmcEqamStatusList != null) {
                ipqamRefreshDao.batchInsertCmcAIpqam(cmcEqamStatusList, entity.getParentId(), cmcIndex);
            }
        }
    }

    @Override
    public void refreshProgramInfo(Long cmcId) {
        Entity entity = entityService.getEntity(cmcId);
        if (entityTypeService.isCcmtsWithAgent(entity.getTypeId())) {
            snmpParam = entityService.getSnmpParamByEntity(cmcId);
            IpqamData data = getCmcFacade(snmpParam.getIpAddress()).refreshIpqamProgramB(snmpParam, cmcId);
            List<CmcEqamProgram> cmcEqamProgramList = data.getCmcEqamProgramList();
            if (cmcEqamProgramList != null) {
                ipqamRefreshDao.batchInsertCmcBEqamProgram(cmcEqamProgramList, cmcId);
            }

            List<ProgramIn> programInList = data.getProgramInList();
            if (programInList != null) {
                ipqamRefreshDao.batchInsertCmcBProgramIn(programInList, cmcId);
            }

            List<ProgramOut> programOutList = data.getProgramOutList();
            if (programOutList != null) {
                ipqamRefreshDao.batchInsertCmcBProgramOut(programOutList, cmcId);
            }
        } else {
            snmpParam = entityService.getSnmpParamByEntity(entity.getParentId());
            Long cmcIndex = cmcService.getCmcIndexByCmcId(cmcId);
            IpqamData data = getCmcFacade(snmpParam.getIpAddress()).refreshIpqamProgramA(snmpParam,
                    entity.getParentId(), cmcIndex);
            List<CmcEqamProgram> cmcEqamProgramList = data.getCmcEqamProgramList();
            if (cmcEqamProgramList != null) {
                ipqamRefreshDao.batchInsertCmcAEqamProgram(cmcEqamProgramList, entity.getParentId(), cmcIndex);
            }

            List<ProgramIn> programInList = data.getProgramInList();
            if (programInList != null) {
                ipqamRefreshDao.batchInsertCmcAProgramIn(programInList, entity.getParentId(), cmcIndex);
            }

            List<ProgramOut> programOutList = data.getProgramOutList();
            if (programOutList != null) {
                ipqamRefreshDao.batchInsertCmcAProgramOut(programOutList, entity.getParentId(), cmcIndex);
            }
        }

    }

    /**
     * 刷新类B型CC IPQAM信息
     * 
     * @param entityId
     */
    private void refreshCmcIpqamB(Long entityId) {
        snmpParam = getSnmpParamByEntityId(entityId);
        IpqamData data = getCmcFacade(snmpParam.getIpAddress()).refreshIpqamDataB(snmpParam, entityId);
        List<CmcEqamStatus> cmcEqamStatusList = data.getCmcEqamStatusList();
        if (cmcEqamStatusList != null) {
            try {
                ipqamRefreshDao.batchInsertCmcBIpqam(cmcEqamStatusList, entityId);
            } catch (Exception e) {
                logger.error("Syn cmcEqamStatusList error ", e);
            }
        }

        List<CmcEqamProgram> cmcEqamProgramList = data.getCmcEqamProgramList();
        if (cmcEqamProgramList != null) {
            try {
                ipqamRefreshDao.batchInsertCmcBEqamProgram(cmcEqamProgramList, entityId);
            } catch (Exception e) {
                logger.error("Syn cmcEqamProgramList error ", e);
            }
        }

        List<ProgramIn> programInList = data.getProgramInList();
        if (programInList != null) {
            try {
                ipqamRefreshDao.batchInsertCmcBProgramIn(programInList, entityId);
            } catch (Exception e) {
                logger.error("Syn programInList error ", e);
            }
        }

        List<ProgramOut> programOutList = data.getProgramOutList();
        if (programOutList != null) {
            try {
                ipqamRefreshDao.batchInsertCmcBProgramOut(programOutList, entityId);
            } catch (Exception e) {
                logger.error("Syn programOutList error ", e);
            }
        }
    }

    /**
     * 刷新类A型CC IPQAM信息
     * 
     * @param entityId
     * @param cmcIndexList
     */
    private void refreshCmcIpqamA(Long entityId, List<Long> cmcIndexList) {
        snmpParam = getSnmpParamByEntityId(entityId);
        for (Long cmcIndex : cmcIndexList) {
            IpqamData data = getCmcFacade(snmpParam.getIpAddress()).refreshIpqamDataA(snmpParam, entityId, cmcIndex);
            List<CmcEqamStatus> cmcEqamStatusList = data.getCmcEqamStatusList();
            if (cmcEqamStatusList != null) {
                try {
                    ipqamRefreshDao.batchInsertCmcAIpqam(cmcEqamStatusList, entityId, cmcIndex);
                } catch (Exception e) {
                    logger.error("Syn cmcEqamStatusList error ", e);
                }
            }

            List<CmcEqamProgram> cmcEqamProgramList = data.getCmcEqamProgramList();
            if (cmcEqamProgramList != null) {
                try {
                    ipqamRefreshDao.batchInsertCmcAEqamProgram(cmcEqamProgramList, entityId, cmcIndex);
                } catch (Exception e) {
                    logger.error("Syn cmcEqamProgramList error ", e);
                }
            }

            List<ProgramIn> programInList = data.getProgramInList();
            if (programInList != null) {
                try {
                    ipqamRefreshDao.batchInsertCmcAProgramIn(programInList, entityId, cmcIndex);
                } catch (Exception e) {
                    logger.error("Syn programInList error ", e);
                }
            }

            List<ProgramOut> programOutList = data.getProgramOutList();
            if (programOutList != null) {
                try {
                    ipqamRefreshDao.batchInsertCmcAProgramOut(programOutList, entityId, cmcIndex);
                } catch (Exception e) {
                    logger.error("Syn programOutList error ", e);
                }
            }
        }
    }

    @Override
    public void refreshCmcIpqamByHttp(Long cmcId) {
        snmpParam = getSnmpParamByEntityId(cmcId);
        HttpParam httpParam = new HttpParam();
        httpParam.setIpAddress(snmpParam.getIpAddress());
        CmcFpgaSpecification fpga = getFacadeFactory().getFacade(httpParam.getIpAddress(), CmcIpqamCollectFacade.class)
                .getCmcFpgaSpecification(httpParam);
        ipqamRefreshDao.batchInsertCC8800BFPGAInfo(fpga, cmcId);
    }

    @Override
    public void refreshOltProgramInfo(Long entityId) {
        // 获取该OLT下所有CCMTS，进行刷新处理
        List<Entity> subEntities = entityService.getSubEntityByEntityId(entityId);
        for (Entity sub : subEntities) {
            if (!entityTypeService.isCcmts(sub.getTypeId())
                    || !deviceVersionService.isFunctionSupported(sub.getEntityId(), "ipqam")) {
                continue;
            }
            try {
                refreshProgramInfo(sub.getEntityId());
            } catch (Exception e) {
                logger.error("refreshProgramInfo error: ", e);
            }
        }
    }

}
