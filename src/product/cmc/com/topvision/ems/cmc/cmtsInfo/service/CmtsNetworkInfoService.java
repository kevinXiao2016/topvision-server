/***********************************************************************
 * $Id: CmtsNetworkInfoService.java,v1.0 2017年8月2日 下午1:42:15 $
 * 
 * @author: ls
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.cmtsInfo.service;

import java.util.List;
import java.util.Map;

import com.topvision.ems.cmc.cmtsInfo.domain.CmOutPowerNotInRange;
import com.topvision.ems.cmc.cmtsInfo.domain.CmRePowerNotInRange;
import com.topvision.ems.cmc.cmtsInfo.domain.CmtsInfoNotInRange;
import com.topvision.ems.cmc.cmtsInfo.domain.CmtsInfoThreshold;
import com.topvision.ems.cmc.cmtsInfo.domain.CmtsNetworkInfo;
import com.topvision.ems.cmc.cmtsInfo.domain.DownSnrAvgNotInRange;
import com.topvision.ems.cmc.cmtsInfo.domain.UpSnrNotInRange;
import com.topvision.framework.service.Service;

/**
 * @author ls
 * @created @2017年8月2日-下午1:42:15
 *
 */
public interface CmtsNetworkInfoService extends Service {
    /**
     * 根据条件查询网络概况
     * 
     * @param cmcQueryMap
     *            cmc查询信息
     * @param start Integer
     *            分页strat
     * @param limit Integer
     *            分页limit
     * @return List<CmcAttribute>
     */
    List<CmtsNetworkInfo> queryCmtsInfoList(Map<String, Object> cmcQueryMap);

    List<CmtsInfoNotInRange> queryCmtsNotInRange(Map<String, Object> cmcQueryMap);

    /**
     * 查发射电平不合格
     * @param
     * @return List<DownSnrAvgNotInRange>
     */
    List<CmOutPowerNotInRange> queryCmOutPowerNotInRange(Map<String, Object> cmcQueryMap);
    /**
     * 查接收电平不合格
     * @param
     * @return List<DownSnrAvgNotInRange>
     */
    List<CmRePowerNotInRange> queryCmRePowerNotInRange(Map<String, Object> cmcQueryMap);

    /**
     * 查上行snr不合格
     * @param
     * @return List<DownSnrAvgNotInRange>
     */
    List<UpSnrNotInRange> queryUpSnrNotInRange(Map<String, Object> cmcQueryMap);

    /**
     * 查下行snr不合格
     * @param
     * @return List<DownSnrAvgNotInRange>
     */
    List<DownSnrAvgNotInRange> queryDownSnrAvgNotInRange(Map<String, Object> cmcQueryMap);

    /**
     * 保存合格指标
     * @param
     * @return void
     */
    void modifyLocalThreshold(Map<String, Object> cmcQueryMap);

    CmtsInfoThreshold getLocalThreshold();

    /**
     * 列表行数
     * @param
     * @return Long
     */
    Long getCmcNum(Map<String, Object> cmcQueryMap);
}
