/***********************************************************************
 * $Id: OltConfigInfoService.java,v1.0 2013-10-26 上午9:39:31 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.congfigbackup.service;

import java.text.ParseException;

import org.quartz.SchedulerException;

import com.topvision.framework.service.Service;

/**
 * @author flack
 * @created @2013-10-26-上午9:39:31
 *
 */
public interface FileAutoSaveService extends Service {

    /**
     *  重新调度任务执行时机
     * @param _autoWriteTime
     * @throws SchedulerException
     * @throws ParseException
     */
    void reConfigureTrigger(String _autoWriteTime) throws SchedulerException, ParseException;

}
