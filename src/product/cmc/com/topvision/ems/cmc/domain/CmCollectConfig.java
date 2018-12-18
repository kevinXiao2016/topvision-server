/***********************************************************************
 * $Id: CmCollectConfig.java,v1.0 2013-6-19 下午2:12:38 $
 * 
 * @author: loyal
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.domain;

/**
 * @author loyal
 * @created @2013-6-19-下午2:12:38
 *
 */
public class CmCollectConfig {
    public static Integer START = 0;
    public static Integer STOP = 1;
    public static Integer DEVICE = 0;
    public static Integer CMMAC = 1;
    //CM采集是否开启 0 开启采集  1表关闭采集
    private Integer cmCollectStatus;
    private Long cmCollectPeriod;
    //CM类型统计是否开启  0 开启  1关闭
    private Integer cmTypeStatisticStatus;
    //CM动作统计 0 开启  1关闭
    private Integer cmActionStatisticStatus;
    //统计来源 0设备直接采集  1表示通过CM列表统计
    private Integer cmStatisticSource;
    public Integer getCmCollectStatus() {
        return cmCollectStatus;
    }
    public void setCmCollectStatus(Integer cmCollectStatus) {
        this.cmCollectStatus = cmCollectStatus;
    }
    public Long getCmCollectPeriod() {
        return cmCollectPeriod;
    }
    public void setCmCollectPeriod(Long cmCollectPeriod) {
        this.cmCollectPeriod = cmCollectPeriod;
    }
    public Integer getCmTypeStatisticStatus() {
        return cmTypeStatisticStatus;
    }
    public void setCmTypeStatisticStatus(Integer cmTypeStatisticStatus) {
        this.cmTypeStatisticStatus = cmTypeStatisticStatus;
    }
    public Integer getCmActionStatisticStatus() {
        return cmActionStatisticStatus;
    }
    public void setCmActionStatisticStatus(Integer cmActionStatisticStatus) {
        this.cmActionStatisticStatus = cmActionStatisticStatus;
    }
    public Integer getCmStatisticSource() {
        return cmStatisticSource;
    }
    public void setCmStatisticSource(Integer cmStatisticSource) {
        this.cmStatisticSource = cmStatisticSource;
    }
}
