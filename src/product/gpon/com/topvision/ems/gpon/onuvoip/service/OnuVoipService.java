/***********************************************************************
 * $Id: OnuVoipService.java,v1.0 2017年5月4日 上午11:21:48 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.gpon.onuvoip.service;

import java.util.HashMap;
import java.util.List;

import com.topvision.ems.gpon.onuvoip.domain.TopGponOnuPots;
import com.topvision.ems.gpon.onuvoip.domain.TopSIPPstnUser;
import com.topvision.ems.gpon.onuvoip.domain.TopVoIPLineStatus;

/**
 * @author haojie
 * @created @2017年5月4日-上午11:21:48
 *
 */
public interface OnuVoipService {

    /**
     * 修改用户配置信息
     * 
     * @param topSIPPstnUser
     */
    void modifyTopSIPPstnUser(TopSIPPstnUser topSIPPstnUser);

    /**
     * 用户配置信息从设备获取数据
     * 
     * @param onuId
     */
    void refreshTopSIPPstnUserByOnuId(Long onuId);

    /**
     * 获取ONU POTS1 线路状态
     * 
     * @param onuId
     * @return
     */
    TopVoIPLineStatus getTopVoIPLineStatus(Long onuId);

    /**
     * 线路状态从设备获取数据
     * 
     * @param onuId
     */
    void refreshTopVoIPLineStatusByOnuId(Long onuId);

    /**
     * 获取GPON POTS口列表
     * 
     * @param onuId
     * @return
     */
    List<TopGponOnuPots> loadGponOnuPotsList(Long onuId);

    /**
     * POTS信息从设备获取数据
     * 
     * @param onuId
     */
    void refreshGponOnuPotsInfo(Long onuId);

    /**
     * 获取POTS 口配置信息
     * 
     * @param map
     * @return
     */
    TopSIPPstnUser loadGponOnuPotsConfig(HashMap<String, Object> map);

    /**
     * 设置POTS口使能(管理状态)
     * @param onuId
     * @param topSIPPstnUserPotsIdx
     * @param potsAdminStatus
     */
    void setOnuPotsAdminStatus(Long onuId, Integer topSIPPstnUserPotsIdx, Integer potsAdminStatus);

}
