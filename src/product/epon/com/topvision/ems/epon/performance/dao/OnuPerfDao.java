/***********************************************************************
 * $Id: OnuPerfDao.java,v1.0 2015-4-24 上午10:14:45 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.performance.dao;

import java.util.List;

import com.topvision.ems.epon.onu.domain.OnuCatvInfo;
import com.topvision.ems.epon.performance.domain.OnuFlowCollectInfo;
import com.topvision.ems.epon.performance.domain.OnuLinkCollectInfo;
import com.topvision.ems.epon.performance.domain.OnuLinkQualityResult;
import com.topvision.ems.epon.performance.domain.OnuOnlineResult;
import com.topvision.ems.epon.performance.domain.PerfOnuQualityHistory;
import com.topvision.framework.dao.BaseEntityDao;

/**
 * @author flack
 * @created @2015-4-24-上午10:14:45
 *
 */
public interface OnuPerfDao extends BaseEntityDao<Object> {

    /**
     * 添加ONU在线状态数据
     * 
     * @param onlineResult
     */
    void insertOnuOnlineStatus(OnuOnlineResult onlineResult);

    /**
     * 添加ONU链路质量数据
     * 
     * @param linkQuality
     */
    void insertOnuLinkQuality(OnuLinkQualityResult linkQuality);

    /**
     * 查询ONU UNIINDEX
     * 
     * @param onuId
     * @return
     */
    List<Long> queryUniIndexByOnuId(Long onuId);

    /**
     * 查询ONU PONINDEx
     * 
     * @param onuId
     * @return
     */
    List<Long> queryPonIndexByOnuId(Long onuId);

    /**
     * 添加ONU端口速率数据
     * 
     * @param flowList
     */
    void batchInsertOnuFlowQuality(List<OnuFlowCollectInfo> flowList);

    /**
     * 插入或者更新Onu光功率信息
     * 
     * @param linkInfo
     */
    void insertOrUpdateLinkInfo(OnuLinkCollectInfo linkInfo);

    /**
     * 插入或者更新OnuCatv光功率信息
     * 
     * @param onuCatvInfo
     */
    void insertOrUpdateCatvInfo(OnuCatvInfo onuCatvInfo);

    Long getMonitorIdByIdentifyKeyAndCategory(Long entityId, String category);

    /**
     * 查询24h内Onu历史最低光接收功率
     * 
     * @param onuLinkCollectInfo
     * @return
     */
    PerfOnuQualityHistory queryMinPonRevPower(OnuLinkCollectInfo onuLinkCollectInfo);

    /**
     * 查询一个月内Onu历史最好光接收功率
     * 
     * @param onuLinkCollectInfo
     * @return
     */
    PerfOnuQualityHistory queryMaxPonRevPower(OnuLinkCollectInfo onuLinkCollectInfo);

    /**
     * 查询24h内CATV历史最低光接收功率
     * 
     * @param onuLinkCollectInfo
     * @return
     */
    PerfOnuQualityHistory queryMinCATVRevPower(OnuLinkCollectInfo onuLinkCollectInfo);

    /**
     * 查询一个月内CATV历史最好光接收功率
     * 
     * @param onuLinkCollectInfo
     * @return
     */
    PerfOnuQualityHistory queryMaxCATVRevPower(OnuLinkCollectInfo onuLinkCollectInfo);

    /**
     * 将汇总的24h最低收光功率插入数据库中
     * 
     * @param perfOnuQualityHistory
     */
    void insertOrUpdateMinReceivedPower(PerfOnuQualityHistory perfOnuQualityHistory);
}
