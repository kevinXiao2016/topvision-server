/***********************************************************************
 * $Id: SpectrumDiscoveryDao.java,v1.0 2014-3-5 上午10:29:35 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.spectrum.dao;

import java.util.List;

import com.topvision.ems.cmc.spectrum.facade.domain.SpectrumCfg;
import com.topvision.ems.cmc.spectrum.facade.domain.SpectrumOltSwitch;

/**
 * @author haojie
 * @created @2014-3-5-上午10:29:35
 *
 */
public interface SpectrumDiscoveryDao {

    /**
     * 插入CMTS频谱配置（刷新OLT或者B型的时候）
     * @param entityId
     * @param spectrumCfgs
     */
    void inserSpectrumCfg(Long entityId, List<SpectrumCfg> spectrumCfgs);

    /**
     * 插入CMTS频谱配置（A型单独刷新的时候）
     * @param cmcId
     * @param spectrumCfgs
     */
    void inserSpectrumCfgForA(Long cmcId, List<SpectrumCfg> spectrumCfgs);

    /**
     * 插入A型频谱OLT开关配置
     * @param spectrumOltSwitch
     */
    void insertSpectrumOltSwitch(SpectrumOltSwitch spectrumOltSwitch);

    /**
     * 在发现的时候默认插入网管侧频谱开关状态
     * @param entityId
     */
    void insertSpectrumCmtsSwitch(Long entityId);
}
