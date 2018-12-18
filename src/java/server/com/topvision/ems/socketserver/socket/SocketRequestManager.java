/***********************************************************************
 * $Id: SocketRequestManager.java,v1.0 2014年10月14日 上午9:07:49 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.socketserver.socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.socketserver.domain.SocketRequest;
import com.topvision.ems.socketserver.domain.SocketResponse;

/**
 * @author Bravin
 * @created @2014年10月14日-上午9:07:49
 * 
 */
@Service
public class SocketRequestManager {
    private static Logger logger = LoggerFactory.getLogger(SocketRequestManager.class);
    @Autowired
    private BeanFactory beanFacatory;

    /**
     * 提交一个SocketRequest
     * 
     * @param socketRequest
     */
    public void submitRequest(SocketRequest socketRequest) {
        String execBean = socketRequest.getRequestExecBeanName();
        try {
            SocketRequestExecutor exector = beanFacatory.getBean(execBean, SocketRequestExecutor.class);
            SocketResponse response = socketRequest.getResponse();
            if (exector.execute(socketRequest, response)) {
                //处理完毕就设置连接已关闭
                response.setClosed(true);
                response.write("CLOSE_SOCKET");
            }
        } catch (Exception e) {
            logger.error("", e);
        }
    }

}
