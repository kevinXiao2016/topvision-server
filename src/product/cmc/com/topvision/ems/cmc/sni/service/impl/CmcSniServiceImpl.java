/***********************************************************************
 * $Id: CmcSniServiceImpl.java,v1.0 2013-4-23 下午4:53:45 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.sni.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.cmc.service.impl.CmcBaseCommonService;
import com.topvision.ems.cmc.sni.dao.CmcSniDao;
import com.topvision.ems.cmc.sni.facade.domain.CmcPhyConfig;
import com.topvision.ems.cmc.sni.facade.domain.CmcRateLimit;
import com.topvision.ems.cmc.sni.facade.domain.CmcSniConfig;
import com.topvision.ems.cmc.sni.service.CmcSniService;
import com.topvision.ems.template.service.EntityTypeService;
import com.topvision.platform.message.event.CmcSynchronizedEvent;
import com.topvision.platform.message.event.CmcSynchronizedListener;
import com.topvision.platform.message.service.MessageService;

/**
 * @author haojie
 * @created @2013-4-23-下午4:53:45
 * 
 */
@Service("cmcSniService")
public class CmcSniServiceImpl extends CmcBaseCommonService implements CmcSniService, CmcSynchronizedListener {
    @Resource(name = "cmcSniDao")
    private CmcSniDao cmcSniDao;
    @Autowired
    private MessageService messageService;
    @Autowired
    private EntityTypeService entityTypeService;

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
        if (event.getEntityType().equals(entityTypeService.getCcmtswithagentType())) {
            // 获取CMC限速属性,8800B只有一条
            try {
                refreshCmcRateLimit(event.getEntityId());
                logger.info("topology CmcRateLimit finished!");
            } catch (Exception e) {
                logger.error("get cmcRateLimit info error");
            }

            // 获取CMC环回使能,8800B只有一条
            try {
                refreshCmcSniConfig(event.getEntityId());
                logger.info("topology CmcSniConfig finished!");
            } catch (Exception e) {
                logger.error("get cmcSniConfig info error");
            }
        }
    }

    @Override
    public CmcRateLimit getCmcRateLimit(Long cmcId) {
        return cmcSniDao.queryCmcRateLimit(cmcId);
    }

    @Override
    public void modifyCmcCpuPortRateLimit(Long cmcId, Integer arpLimit, Integer uniLimit, Integer udpLimit,
            Integer dhcpLimit) {
        CmcRateLimit cmcRateLimit = new CmcRateLimit();
        cmcRateLimit.setCmcId(cmcId);
        cmcRateLimit.setTopCcmtsRateLimiteCpuPortEgressArp(arpLimit);
        cmcRateLimit.setTopCcmtsRateLimiteCpuPortEgressDhcp(dhcpLimit);
        cmcRateLimit.setTopCcmtsRateLimiteCpuPortEgressUdp(udpLimit);
        cmcRateLimit.setTopCcmtsRateLimiteCpuPortEgressUni(uniLimit);
        snmpParam = getSnmpParamByEntityId(cmcId);
        getCmcFacade(snmpParam.getIpAddress()).modifyCmcCpuPortRateLimit(snmpParam, cmcRateLimit);
        cmcSniDao.updateCmcCpuPortRateLimit(cmcRateLimit);
    }

    @Override
    public void modifyCmcSniRateLimit(Long cmcId, Integer icmpLimit, Integer igmpLimit) {
        CmcRateLimit cmcRateLimit = new CmcRateLimit();
        cmcRateLimit.setCmcId(cmcId);
        cmcRateLimit.setTopCcmtsRateLimiteUplinkEgressIcmp(icmpLimit);
        cmcRateLimit.setTopCcmtsRateLimiteUplinkEgressIgmp(igmpLimit);
        snmpParam = getSnmpParamByEntityId(cmcId);
        getCmcFacade(snmpParam.getIpAddress()).modifyCmcSniRateLimit(snmpParam, cmcRateLimit);
        cmcSniDao.updateCmcSniRateLimit(cmcRateLimit);
    }

    @Override
    public List<CmcPhyConfig> getCmcPhyConfigList(Long cmcId) {
        return cmcSniDao.queryCmcPhyConfigList(cmcId);
    }

    @Override
    public boolean modifyCmcSniPhyConfig(Long cmcId, Integer sniPhy1Config, Integer sniPhy2Config) {
        List<CmcPhyConfig> cmcPhyConfigList = new ArrayList<CmcPhyConfig>();
        // PHY1口配置
        CmcPhyConfig cmcphy1Config = new CmcPhyConfig();
        cmcphy1Config.setCmcId(cmcId);
        cmcphy1Config.setPhyIndex(1);
        cmcphy1Config.setTopCcmtsSniInt(sniPhy1Config);
        // PHY2口配置
        if (sniPhy2Config != null) {
            CmcPhyConfig cmcphy2Config = new CmcPhyConfig();
            cmcphy2Config.setCmcId(cmcId);
            cmcphy2Config.setPhyIndex(2);
            cmcphy2Config.setTopCcmtsSniInt(sniPhy2Config);
            cmcPhyConfigList.add(cmcphy2Config);
        }

        cmcPhyConfigList.add(cmcphy1Config);
        // 批量设置到设备
        snmpParam = getSnmpParamByEntityId(cmcId);
        try {
            getCmcFacade(snmpParam.getIpAddress()).modifyCmcSniPhyConfig(snmpParam, cmcPhyConfigList);
        } catch (Exception ex) {
            logger.debug("", ex);
        }
        // 判断是否设置成功
        try {
            Thread.sleep(3000);
            List<CmcPhyConfig> list = refreshCmcPhyConfig(cmcId);
            for (CmcPhyConfig cmcPhyConfig : list) {
                if (cmcPhyConfig.getPhyIndex() == 1 && sniPhy1Config.equals(cmcPhyConfig.getTopCcmtsSniInt())) {
                    // 批量存入数据库
                    cmcSniDao.batchUpdateCmcSniPhyConfig(cmcPhyConfigList);
                    return true;
                } else if (cmcPhyConfig.getPhyIndex() == 2 && sniPhy2Config.equals(cmcPhyConfig.getTopCcmtsSniInt())) {
                    // 批量存入数据库
                    cmcSniDao.batchUpdateCmcSniPhyConfig(cmcPhyConfigList);
                    return true;
                } else {
                    return false;
                }
            }
        } catch (InterruptedException e) {
            logger.error("", e);
            return false;
        }
        return false;
    }

    @Override
    public CmcSniConfig getCmcSniConfig(Long cmcId) {
        return cmcSniDao.queryCmcSniConfig(cmcId);
    }

    @Override
    public void modifySniLoopbackStatus(Long cmcId, Integer loopbackStatus) {
        CmcSniConfig cmcSniConfig = new CmcSniConfig();
        cmcSniConfig.setCmcId(cmcId);
        cmcSniConfig.setTopCcmtsSniUplinkLoopbackStatus(loopbackStatus);
        snmpParam = getSnmpParamByEntityId(cmcId);
        getCmcFacade(snmpParam.getIpAddress()).modifySniLoopbackStatus(snmpParam, cmcSniConfig);
        cmcSniDao.updateSniLoopbackStatus(cmcSniConfig);
    }

    @Override
    public void modifyCmcSniRateLimit(Long cmcId, CmcRateLimit cmcRateLimit) {
        cmcRateLimit.setCmcId(cmcId);
        snmpParam = getSnmpParamByEntityId(cmcId);
        getCmcFacade(snmpParam.getIpAddress()).modifyCmcSniRateLimit(snmpParam, cmcRateLimit);
        cmcSniDao.updateCmcSniRateLimit(cmcRateLimit);
    }

    @Override
    public void modifyStormLimitConfig(Long cmcId, CmcRateLimit cmcRateLimit) {
        cmcRateLimit.setCmcId(cmcId);
        snmpParam = getSnmpParamByEntityId(cmcId);
        getCmcFacade(snmpParam.getIpAddress()).modifyCmcSniRateLimit(snmpParam, cmcRateLimit);
        cmcSniDao.updateStormLimitConfig(cmcRateLimit);
    }

    @Override
    public List<CmcPhyConfig> refreshCmcPhyConfig(Long cmcId) {
        snmpParam = getSnmpParamByEntityId(cmcId);
        List<CmcPhyConfig> list = getCmcFacade(snmpParam.getIpAddress()).getCmcPhyConfig(snmpParam);
        for (CmcPhyConfig c : list) {
            c.setCmcId(cmcId);
        }
        cmcSniDao.batchInsertOrUpdatePhyConfig(list);
        return list;
    }

    @Override
    public void refreshCmcRateLimit(Long cmcId) {
        snmpParam = getSnmpParamByEntityId(cmcId);
        CmcRateLimit cmcRateLimit = getCmcFacade(snmpParam.getIpAddress()).getCmcRateLimit(snmpParam);

        if (cmcRateLimit != null) {
            cmcRateLimit.setCmcId(cmcId);
            cmcSniDao.insertOrUpdateCmcRateLimit(cmcRateLimit);
        }
    }

    @Override
    public void refreshCmcSniConfig(Long cmcId) {
        snmpParam = getSnmpParamByEntityId(cmcId);
        CmcSniConfig cmcSniConfig = getCmcFacade(snmpParam.getIpAddress()).getCmcSniConfig(snmpParam);

        if (cmcSniConfig != null) {
            cmcSniConfig.setCmcId(cmcId);
            cmcSniDao.insertOrUpdateCmcSniConfig(cmcSniConfig);
        }
    }

}
