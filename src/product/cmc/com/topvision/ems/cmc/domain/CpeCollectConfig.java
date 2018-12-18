/***********************************************************************
 * $Id: CpeCollectConfig.java,v1.0 2013-6-19 下午2:13:44 $
 * 
 * @author: loyal
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.domain;

/**
 * @author loyal
 * @created @2013-6-19-下午2:13:44
 *
 */
public class CpeCollectConfig {
    public static Integer START = 0;
    public static Integer STOP = 1;
    //是否采集CPE原始数据 0 开启 1关闭
    private Integer cpeCollectStatus;
    //采集周期
    private Long cpeCollectPeriod;
    //是否统计cpe数量 0开启 1关闭
    private Integer cpeNumStatisticStatus;
    //是否统计cpe上下线行为 0开启 1关闭
    private Integer cpeActionStatisticStatus;
    
    public Integer getCpeCollectStatus() {
        return cpeCollectStatus;
    }
    public void setCpeCollectStatus(Integer cpeCollectStatus) {
        this.cpeCollectStatus = cpeCollectStatus;
    }
    public Long getCpeCollectPeriod() {
        return cpeCollectPeriod;
    }
    public void setCpeCollectPeriod(Long cpeCollectPeriod) {
        this.cpeCollectPeriod = cpeCollectPeriod;
    }
    public Integer getCpeNumStatisticStatus() {
        return cpeNumStatisticStatus;
    }
    public void setCpeNumStatisticStatus(Integer cpeNumStatisticStatus) {
        this.cpeNumStatisticStatus = cpeNumStatisticStatus;
    }
    public Integer getCpeActionStatisticStatus() {
        return cpeActionStatisticStatus;
    }
    public void setCpeActionStatisticStatus(Integer cpeActionStatisticStatus) {
        this.cpeActionStatisticStatus = cpeActionStatisticStatus;
    }
    
}
