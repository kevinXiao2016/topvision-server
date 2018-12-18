/***********************************************************************
 * $Id: CmCpeCollectDataPolicy.java,v1.0 2013-6-19 下午2:14:23 $
 * 
 * @author: loyal
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.domain;

/**
 * @author loyal
 * @created @2013-6-19-下午2:14:23
 * 
 */
public class CmCpeCollectDataPolicy {
    // 原始数据时间策略，单位毫秒，指定数据保存多长时间，单位毫秒
    private Long initDataSavePolicy;
    // 统计数据时间策略，单位毫秒，指定数据保存多长时间，单位毫秒
    private Long statisticDataSavePolicy;
    // 行为数据时间策略，单位毫秒，指定数据保存多长时间，单位毫秒
    private Long actionDataSavePolicy;
    // CM历史信号质量数据,单位毫秒，指定数据保存多长时间，单位毫秒
    private Long cmHistorySavePolicy;

    public Long getCmHistorySavePolicy() {
        return cmHistorySavePolicy;
    }

    public void setCmHistorySavePolicy(Long cmHistorySavePolicy) {
        this.cmHistorySavePolicy = cmHistorySavePolicy;
    }

    public Long getInitDataSavePolicy() {
        return initDataSavePolicy;
    }

    public void setInitDataSavePolicy(Long initDataSavePolicy) {
        this.initDataSavePolicy = initDataSavePolicy;
    }

    public Long getStatisticDataSavePolicy() {
        return statisticDataSavePolicy;
    }

    public void setStatisticDataSavePolicy(Long statisticDataSavePolicy) {
        this.statisticDataSavePolicy = statisticDataSavePolicy;
    }

    public Long getActionDataSavePolicy() {
        return actionDataSavePolicy;
    }

    public void setActionDataSavePolicy(Long actionDataSavePolicy) {
        this.actionDataSavePolicy = actionDataSavePolicy;
    }
}
