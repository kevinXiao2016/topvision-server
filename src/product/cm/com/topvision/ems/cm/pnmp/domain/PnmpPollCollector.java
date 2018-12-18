/***********************************************************************
 * $Id: PnmpPollCollector.java,v1.0 2013-5-3 下午04:44:20 $
 * 
 * @author: lzs
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cm.pnmp.domain;

import com.topvision.ems.cm.pnmp.facade.domain.DelayItem;
import com.topvision.framework.dao.mybatis.AliasesSuperType;
import org.apache.ibatis.type.Alias;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.concurrent.DelayQueue;

/**
 * @author jay
 * @created @2013-5-3-下午04:44:20
 *
 */
@Alias("pnmpPollCollector")
public class PnmpPollCollector implements Serializable, AliasesSuperType, Comparable<PnmpPollCollector> {
    private static final long serialVersionUID = -420336880881481963L;
    private Logger logger = LoggerFactory.getLogger(PnmpPollCollector.class);
    public static final int OFFLINE = 0;
    public static final int ONLINE = 1;
    /**
     * engineId
     */
    private Integer engineId;
    /**
     * 采集器描述
     */
    private String desc;

    /**
     * 采集器状态
     */
    private int state = ONLINE;

    /**
     * 5分钟内接收到的任务id列表
     */
    private DelayQueue<DelayItem<Long>> taskDlayQueue = new DelayQueue<>();

    private Thread taskQueueThread = null;
    /**
     * 正在执行的任务数
     */
    private int executeTaskCounts = 0;

    /**
     * 本轮此采集器处理的任务总数
     */
    private int roundTotalTaskCounts = 0;
    /**
     * 上轮此采集器处理的任务总数
     */
    private int nextRoundTotalTaskCounts = 0;
    /**
     * 最大同时执行任务数
     */
    private int maxTaskCounts = 0;
    /**
     * 空闲任务数
     */
    private int idleCounts = 0;
    //最近五分钟执行任务数
    @SuppressWarnings("unused")
    private int executedTaskIn5m = 0;

    private static Object executeTaskCountsFlag = new Object();

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public void setEngineId(Integer engineId) {
        this.engineId = engineId;
    }

    public void setIdleCounts(int idleCounts) {
        this.idleCounts = idleCounts;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public DelayQueue<DelayItem<Long>> getTaskDlayQueue() {
        return taskDlayQueue;
    }

    public void setTaskDlayQueue(DelayQueue<DelayItem<Long>> taskDlayQueue) {
        this.taskDlayQueue = taskDlayQueue;
    }

    public int getExecuteTaskCounts() {
        return executeTaskCounts;
    }

    public void setExecuteTaskCounts(int executeTaskCounts) {
        synchronized (executeTaskCountsFlag) {
            this.executeTaskCounts = executeTaskCounts;
            idleCounts = maxTaskCounts - executeTaskCounts;
        }
    }

    public void addExecuteTaskCounts() {
        synchronized (executeTaskCountsFlag) {
            executeTaskCounts++;
            idleCounts = maxTaskCounts - executeTaskCounts;
        }
    }

    public void delExecuteTaskCounts() {
        synchronized (executeTaskCountsFlag) {
            executeTaskCounts--;
            idleCounts = maxTaskCounts - executeTaskCounts;
        }
    }

    public int getRoundTotalTaskCounts() {
        return roundTotalTaskCounts;
    }

    public void setRoundTotalTaskCounts(int roundTotalTaskCounts) {
        this.roundTotalTaskCounts = roundTotalTaskCounts;
    }

    public void addRoundTotalTaskCounts() {
        roundTotalTaskCounts++;
    }

    public int getNextRoundTotalTaskCounts() {
        return nextRoundTotalTaskCounts;
    }

    public void setNextRoundTotalTaskCounts(int nextRoundTotalTaskCounts) {
        this.nextRoundTotalTaskCounts = nextRoundTotalTaskCounts;
    }

    public int getMaxTaskCounts() {
        return maxTaskCounts;
    }

    public void setMaxTaskCounts(int maxTaskCounts) {
        synchronized (executeTaskCountsFlag) {
            this.maxTaskCounts = maxTaskCounts;
            idleCounts = maxTaskCounts - executeTaskCounts;
        }
    }

    public int getIdleCounts() {
        return idleCounts;
    }

    public void init() {
        state = OFFLINE;
        taskDlayQueue.clear();
        synchronized (executeTaskCountsFlag) {
            executeTaskCounts = 0;
        }
        roundTotalTaskCounts = 0;
        nextRoundTotalTaskCounts = 0;
        maxTaskCounts = 0;
        idleCounts = 0;
        if (taskQueueThread != null) {
            try {
                taskQueueThread.interrupt();
            } catch (Exception e) {
            }
        }
        taskQueueThread = new Thread() {
            @Override
            public void run() {
                while (true) {
                    try {
                        taskDlayQueue.take();
                        if (logger.isTraceEnabled()) {
                            logger.trace("taskQueueThread  taskDlayQueue.take");
                        }
                    } catch (InterruptedException e) {
                    }
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                    }
                }
            }
        };
        taskQueueThread.start();
    }

    @Override
    public int compareTo(PnmpPollCollector other) {
        return idleCounts > other.getIdleCounts() ? -1 : idleCounts == other.getIdleCounts() ? 0 : 1;
    }

    public Integer getEngineId() {
        return engineId;
    }

    public int getExecutedTaskIn5m() {
        return taskDlayQueue.size();
    }

    @Override
    public String toString() {
        return "PnmpPollCollector{" +
                "engineId=" + engineId +
                ", desc='" + desc + '\'' +
                ", state=" + state +
                ", taskDlayQueue=" + taskDlayQueue +
                ", taskQueueThread=" + taskQueueThread +
                ", executeTaskCounts=" + executeTaskCounts +
                ", roundTotalTaskCounts=" + roundTotalTaskCounts +
                ", nextRoundTotalTaskCounts=" + nextRoundTotalTaskCounts +
                ", maxTaskCounts=" + maxTaskCounts +
                ", idleCounts=" + idleCounts +
                ", executedTaskIn5m=" + executedTaskIn5m +
                '}';
    }
}
