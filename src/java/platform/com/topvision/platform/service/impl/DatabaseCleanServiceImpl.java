/***********************************************************************
 * $Id: DatabaseCleanServiceImpl.java,v1.0 2015-6-3 下午3:33:44 $
 * 
 * @author: Rod John
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.platform.service.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.framework.datasource.DbContextHolder;
import com.topvision.framework.service.BaseService;
import com.topvision.platform.dao.DatabaseDao;
import com.topvision.platform.service.DatabaseCleanService;

/**
 * @author Rod John
 * @created @2015-6-3-下午3:33:44
 *
 */
@Service("databaseCleanService")
public class DatabaseCleanServiceImpl extends BaseService implements DatabaseCleanService {
    @Autowired
    private DatabaseDao databaseDao;
    private Timer historyDataCleanTimer;
    private boolean switchPartition = true;
    private long interval = 3600 * 24 * 1000;

    private void runPartitionScript() {
        databaseDao.runPartitionScript();
    }

    @Override
    @PostConstruct
    public void initialize() {
        super.initialize();
    }

    /* (non-Javadoc)
     * @see com.topvision.platform.service.DatabaseCleanService#txCleanHistoryData()
     */
    @Override
    public void txCleanHistoryData(final Integer keepMonth) {
        if (switchPartition) {
            // Run Partition Script
            runPartitionScript();

            // Create History Data Clean Timer
            // Date nextTime = getTimeOfNextDay1();
            historyDataCleanTimer = new Timer();
            historyDataCleanTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    // 定期清除
                    try {
                        DbContextHolder.setEngine();
                        cleanHistoryData(keepMonth);
                        logger.debug("Default time comes, and history alert data has been delete!");
                    } catch (Exception e) {
                        logger.error("", e);
                    } finally {
                        DbContextHolder.cleanJdbcType();
                    }
                }
            }, 0, interval);
        }
    }

    private Date getTimeOfNextDay1() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 10);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        cal.add(Calendar.DAY_OF_MONTH, 1);
        return cal.getTime();
    }

    /**
     * 清除历史告警数据
     */
    private void cleanHistoryData(Integer keepMonth) {
        databaseDao.cleanHistoryData(keepMonth);
    }

    /* (non-Javadoc)
     * @see com.topvision.framework.service.BaseService#destroy()
     */
    @Override
    public void destroy() {
        historyDataCleanTimer.cancel();
        historyDataCleanTimer = null;
    }

    /* (non-Javadoc)
     * @see com.topvision.platform.service.DatabaseCleanService#txUpdateCleanTask(java.lang.Integer)
     */
    @Override
    public void txUpdateCleanTask(final Integer keepMonth) {
        if (switchPartition) {
            historyDataCleanTimer.cancel();
            historyDataCleanTimer = new Timer();
            //Date nextTime = getTimeOfNextDay1();
            historyDataCleanTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    // 定期清除
                    try {
                        DbContextHolder.setEngine();
                        cleanHistoryData(keepMonth);
                        logger.debug("Someone has changed the clean cycle");
                    } catch (Exception e) {
                        logger.error("", e);
                    } finally {
                        DbContextHolder.cleanJdbcType();
                    }
                }
            }, 0, interval);
        }
    }
}
