/***********************************************************************
 * $Id: CmcSniService.java,v1.0 2013-4-23 下午4:51:54 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.sni.service;

import java.util.List;

import com.topvision.ems.cmc.sni.facade.domain.CmcPhyConfig;
import com.topvision.ems.cmc.sni.facade.domain.CmcRateLimit;
import com.topvision.ems.cmc.sni.facade.domain.CmcSniConfig;
import com.topvision.framework.service.Service;

/**
 * @author haojie
 * @created @2013-4-23-下午4:51:54
 *
 */
public interface CmcSniService extends Service {
    /**
     * 获取CMC端口限速
     * @param cmcId
     * @return
     */
    CmcRateLimit getCmcRateLimit(Long cmcId);

    /**
     * 修改CPU端口限速
     * @param cmcId
     * @param arpLimit
     * @param uniLimit
     * @param udpLimit
     * @param dhcpLimit
     */
    void modifyCmcCpuPortRateLimit(Long cmcId, Integer arpLimit, Integer uniLimit, Integer udpLimit, Integer dhcpLimit);
    
    /**
     * 修改上联口限速
     * @param cmcId
     * @param icmpLimit
     * @param igmpLimit
     */
    void modifyCmcSniRateLimit(Long cmcId, Integer icmpLimit, Integer igmpLimit);
    
    /**
     * 修改上联口限速
     * @param cmcId
     * @param cmcRateLimit
     */
    void modifyCmcSniRateLimit(Long cmcId, CmcRateLimit cmcRateLimit);
    
    /**
     * 获取上联口PHY配置信息
     * @param cmcId
     * @return
     */
    List<CmcPhyConfig> getCmcPhyConfigList(Long cmcId);
    
    /**
     * 修改上联口PHY信息
     * @param cmcId
     * @param sniPhy1Config
     * @param sniPhy2Config
     */
    boolean modifyCmcSniPhyConfig(Long cmcId, Integer sniPhy1Config, Integer sniPhy2Config);
    
    /**
     * 获取上联口环回使能
     * @param cmcId
     * @return
     */
    CmcSniConfig getCmcSniConfig(Long cmcId);

    /**
     * 设置上联口环回使能
     * @param cmcId
     * @param loopbackStatus
     */
    void modifySniLoopbackStatus(Long cmcId, Integer loopbackStatus);
    
    /**
     * 设置风暴抑制参数
     * @param cmcId
     * @param cmcRateLimit
     */
    void modifyStormLimitConfig(Long cmcId, CmcRateLimit cmcRateLimit);
    
    /**
     * 获取Phy配置
     * @param cmcId
     * @return
     */
    List<CmcPhyConfig> refreshCmcPhyConfig(Long cmcId);
    
    /**
     * 刷新限速信息
     * @param cmcId
     */
    void refreshCmcRateLimit(Long cmcId);
    
    /**
     * 刷新SNI配置
     * @param cmcId
     */
    void refreshCmcSniConfig(Long cmcId);

}
