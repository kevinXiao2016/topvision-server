/***********************************************************************
 * $Id: OnuCatvConfigService.java,v1.0 2016-4-27 上午9:44:07 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.onu.service;

import com.topvision.ems.epon.onu.domain.OnuCatvConfig;
import com.topvision.ems.epon.onu.domain.OnuCatvInfo;

/**
 * @author haojie
 * @created @2016-4-27-上午9:44:07
 *
 */
public interface OnuCatvService {

    /**
     * 获取onu catv 配置信息
     * @param onuId
     * @return
     */
    OnuCatvConfig getOnuCatvConfig(Long onuId);

    /**
     * 修改onu catv 配置信息
     * @param onuCatvConfig
     */
    void modifyOnuCatvConfig(OnuCatvConfig onuCatvConfig);

    /**
     * 刷新onu catv 配置信息
     * @param onuCatvConfig
     */
    void refreshOnuCatvConfig(OnuCatvConfig onuCatvConfig);

    /**
     * 刷新olt下所有onu catv配置信息
     * @param entityId
     */
    void refreshOnuCatvConfigAll(Long entityId);

    /**
     * 获取onu catv 信息
     * @param onuId
     * @return
     */
    OnuCatvInfo getOnuCatvInfo(Long onuId);

}
