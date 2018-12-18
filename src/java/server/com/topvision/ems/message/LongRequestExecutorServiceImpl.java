/***********************************************************************
 * $Id: LongRequestExecutorServiceImpl.java,v1.0 2016年4月29日 下午4:59:51 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.message;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.framework.service.BaseService;

/**
 * @author Bravin
 * @created @2016年4月29日-下午4:59:51
 *
 */
@Service("longRequestExecutorService")
public class LongRequestExecutorServiceImpl extends BaseService implements LongRequestExecutorService {
    private ExecutorService cachedThreadPool;
    @Autowired
    protected MessagePusher messagePusher;

    @Override
    public void start() {
        cachedThreadPool = Executors.newCachedThreadPool();
    }

    @Override
    public void executeRequest(CallbackableRequest request) {
        request.setMessagePusher(messagePusher);
        cachedThreadPool.execute(request);
    }
}
