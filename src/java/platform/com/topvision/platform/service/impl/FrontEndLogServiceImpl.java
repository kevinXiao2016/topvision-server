/***********************************************************************
 * $Id: FrontEndLogServiceImpl.java,v1.0 2017年3月30日 上午9:28:33 $
 * 
 * @author: vanzand
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.platform.service.impl;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.framework.service.BaseService;
import com.topvision.platform.message.event.LogoffEvent;
import com.topvision.platform.message.event.LogoffListener;
import com.topvision.platform.message.service.MessageService;
import com.topvision.platform.service.FrontEndLogService;

/**
 * @author vanzand
 * @created @2017年3月30日-上午9:28:33
 *
 */
@Service("frontEndLogService")
public class FrontEndLogServiceImpl extends BaseService implements FrontEndLogService, LogoffListener {

    // 各session是否开启前端日志开关
    private Map<String, Boolean> sessionSwitchMap = new HashMap<String, Boolean>();
    private Map<String, Long> timeMap = new HashMap<String, Long>();

    @Autowired
    private MessageService messageService;

    // session日志开关的超时时间是2小时
    private static final int cacheTimeout = 2 * 3600 * 1000;

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.framework.service.Service#initialize()
     */
    @Override
    public void initialize() {
        super.initialize();
        messageService.addListener(LogoffListener.class, this);

        // 启动线程维护session日志开关的map
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        long now = System.currentTimeMillis();
                        // 遍历timeMap对象
                        Iterator<Entry<String, Long>> iterator = timeMap.entrySet().iterator();
                        while (iterator.hasNext()) {
                            Entry<String, Long> sessionObj = iterator.next();
                            String sessionId = sessionObj.getKey();
                            Long sessionTime = sessionObj.getValue();
                            if (now - sessionTime > cacheTimeout) {
                                timeMap.remove(sessionId);
                                sessionSwitchMap.remove(sessionId);
                            }
                        }
                        Thread.sleep(cacheTimeout);
                    } catch (Exception e) {
                        logger.debug("cacu cm error: " + e.getMessage());
                    }
                }

            }
        });
        thread.start();
    }

    @Override
    public void logoff(LogoffEvent evt) {
        if (sessionSwitchMap.containsKey(evt.getSessionId())) {
            sessionSwitchMap.remove(evt.getSessionId());
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.platform.service.FrontEndLogService#getFrontEndLogStatus(java.lang.String)
     */
    @Override
    public Boolean getFrontEndLogStatus(String sessionId) {
        if (sessionSwitchMap.containsKey(sessionId)) {
            return sessionSwitchMap.get(sessionId);
        }
        return false;
    }

    @Override
    public void openFrontEndLog(String sessionId) {
        sessionSwitchMap.put(sessionId, true);
        timeMap.put(sessionId, System.currentTimeMillis());
    }

    @Override
    public void closeFrontEndLog(String sessionId) {
        sessionSwitchMap.remove(sessionId);
        timeMap.remove(sessionId);
    }

}
