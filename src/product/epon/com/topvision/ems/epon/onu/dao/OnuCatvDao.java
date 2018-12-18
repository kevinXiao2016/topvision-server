/***********************************************************************
 * $Id: OnuCatvConfigDao.java,v1.0 2016-4-27 上午9:50:16 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.onu.dao;

import java.util.List;

import com.topvision.ems.epon.onu.domain.OnuCatvConfig;
import com.topvision.ems.epon.onu.domain.OnuCatvInfo;

/**
 * @author haojie
 * @created @2016-4-27-上午9:50:16
 *
 */
public interface OnuCatvDao {

    /**
     * 获取ONU catv 配置信息
     * @param onuId
     * @return
     */
    OnuCatvConfig getOnuCatvConfig(Long onuId);

    /**
     * 获取onu catv快照信息
     * @param onuId
     * @return
     */
    OnuCatvInfo getOnuCatvInfo(Long onuId);

    /**
     * 批量插入或刷新onu catv 配置信息
     * @param onuCatvConfigList
     * @param entityId
     */
    void batchInsertOrUpdateOnuCatvConfig(List<OnuCatvConfig> onuCatvConfigList, Long entityId);

    /**
     * 插入或刷新onu catv配置信息
     * @param config
     */
    void insertOrUpdateOnuCatvConfig(OnuCatvConfig config);

}
