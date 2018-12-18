/***********************************************************************
 * $ com.topvision.ems.cmc.spectrum.service.MonitorService,v1.0 2014-1-9 15:02:08 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.spectrum.service;

/**
 * @author jay
 * @created @2014-1-9-15:02:08
 */
public interface SpectrumMonitorService {

    /**
     * 开启频谱采集
     * 
     * @param cmcId
     *            Long
     * @return 开启采集是否成功
     */
    public Boolean startSpectrumMonitor(Long cmcId, Integer period);

    /**
     * 关闭频谱采集monitor
     * 
     * @param cmcId
     *            Long
     */
    public void stopSpectrumMonitor(Long cmcId);

    /**
     * Add by Victor@20160823增加删除频谱monitor的方法，解决数据库存在但是后台任务存在的问题
     * 
     * 关闭频谱采集monitor
     * 
     * @param cmcId
     *            Long
     * @param monitroId
     *            Long
     */
    public void stopSpectrumMonitor(Long cmcId, Long monitroId);

    /**
     * 关闭所有的频谱采集MONITOR
     */
    void stopAllSpectrumMonitor();

    /**
     * 添加Monitor
     * 
     * @param cmcId
     *            CMC_ID 在执行JOB时需要使用
     * @param initInterval
     *            起始时间间隔
     * @param intervalOfNormal
     *            时间间隔
     * 
     */
    Boolean addMonitor(Long cmcId, Long initInterval, Long intervalOfNormal);

    /**
     * 根据CC的Id删除掉历史频谱录像的Monitor
     * 
     * @param cmcId
     */
    Boolean removeMonitor(Long cmcId);

    /**
     * 判断是否开启monitor
     * 
     * @param cmcId
     * @return
     */
    public boolean hasSpectrumMonitor(Long cmcId);

    /**
     * 更新历史频谱采集周期
     * 
     * @param cmcId
     * @param intervalOfNormal
     */
    public void updateAllHisMonitor(Long intervalOfNormal);
}
