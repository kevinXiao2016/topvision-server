/***********************************************************************
 * $Id: OnuPerfDao.java,v1.0 2015-4-24 上午10:14:45 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.performance.engine;

import java.util.List;

import com.topvision.ems.epon.performance.domain.OnuCatvOrInfoResult;
import com.topvision.ems.epon.performance.domain.OnuCpe;
import com.topvision.ems.epon.performance.domain.OnuFlowCollectInfo;
import com.topvision.ems.epon.performance.domain.OnuLinkQualityResult;
import com.topvision.ems.epon.performance.domain.OnuOnlineResult;
import com.topvision.ems.epon.performance.domain.OnuUniCpeCount;

/**
 * @author flack
 * @created @2015-4-24-上午10:14:45
 *
 */
public interface OnuPerfDao {

    /**
     * 添加ONU在线状态数据
     * @param onlineResult
     */
    void insertOnuOnlineStatus(OnuOnlineResult onlineResult);

    /**
     * 添加ONU链路质量数据
     * @param linkQuality
     */
    void insertOnuLinkQuality(OnuLinkQualityResult linkQuality);

    /**
     * Perf: Onu Catv
     * 
     * @param catvOrInfoEntry
     */
    void insertOnuCatvQuality(OnuCatvOrInfoResult onuCatvOrInfoResult);

    /**
     * 查询ONU UNIINDEX
     * @param onuId
     * @return
     */
    List<Long> queryUniIndexByOnuId(Long onuId);

    /**
     * 查询ONU 类型
     * @param onuId
     * @return
     */
    String queryOnuTypeByOnuId(Long onuId);

    /**
     * 查询ONU PONINDEx
     * @param onuId
     * @return
     */
    List<Long> queryPonIndexByOnuId(Long onuId);

    /**
     * 添加ONU端口速率数据
     * @param flowList
     */
    void batchInsertOnuFlowQuality(List<OnuFlowCollectInfo> flowList);

    void removeOnuCpeListByEntityId(Long entityId);

    void removeOnuCpeCountByEntityId(Long entityId);

    void batchInsertOnuCpeList(List<OnuCpe> allOnuCpes);

    void batchInsertOnuCpeCount(List<OnuUniCpeCount> onuUniCpeCounts);
}
