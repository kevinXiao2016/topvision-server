/***********************************************************************
 * $Id: SummaryListener.java,v1.0 2013-7-2 下午3:39:38 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.report.message;

import com.topvision.platform.message.event.EmsListener;

/**
 * @author Bravin
 * @created @2013-7-2-下午3:39:38
 * 
 */
public interface SummaryListener extends EmsListener {
    /**
     * 处理每个小时定时触发的消息，批量移动该小时内的性能数据
     * 
     * @param event
     */
    void execHourlySummary(SummaryEvent event);

    /**
     * 处理每天定时触发的消息，做日汇总，时汇总
     * 
     * @param event
     */
    void execDailySummary(SummaryEvent event);

}
