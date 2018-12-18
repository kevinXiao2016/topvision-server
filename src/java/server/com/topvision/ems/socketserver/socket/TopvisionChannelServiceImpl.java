/***********************************************************************
 * $Id: ChannelServer.java,v1.0 2014��10��13�� ����5:42:15 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.socketserver.socket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.topvision.ems.socketserver.domain.SocketSession;
import com.topvision.framework.service.BaseService;

/**
 * @author Bravin
 * 
 */
@Service("topvisionChannelService")
public class TopvisionChannelServiceImpl extends BaseService implements TopvisionChannelService {
    private static final Logger logger = LoggerFactory.getLogger(TopvisionChannelServiceImpl.class);
    @Value("${socket.port:3006}")
    public int socketServerPort;
    @Value("${socket.corePoolSize:20}")
    private Integer corePoolSize;
    @Value("${socket.maximumPoolSize:50}")
    private Integer maximumPoolSize;
    @Value("${socket.keepAliveTime:60000}")
    private Integer keepAliveTime;
    @Value("${socket.queueSize:30}")
    private Integer queueSize;
    @Autowired
    private SocketRequestManager manager;
    private ServerSocket serverSocket;
    private ThreadPoolExecutor threadPoolExecutor;
    private List<SocketWorker> socketWorkers = new ArrayList<>();
    private Object socketWait = new Object();

    @PostConstruct
    @Override
    public void initialize() {
        try {
            listen();
        } catch (Exception e) {
            logger.error("", e);
        }
    }

    public void listen() throws IOException {
        threadPoolExecutor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, TimeUnit.MINUTES,
                new ArrayBlockingQueue<Runnable>(queueSize));
        threadPoolExecutor.allowCoreThreadTimeOut(true);
        serverSocket = new ServerSocket(socketServerPort);
        new Thread(new Runnable() {

            @Override
            public void run() {
                for (;;) {
                    Socket socket;
                    try {
                        socket = serverSocket.accept();
                        synchronized (socketWait) {
                            UUID sessionId = UUID.randomUUID();
                            SocketSession session = new SocketSession();
                            session.setSessionId(sessionId.toString());
                            SocketWorker scw = new SocketWorker(socket, manager, session);
                            if (threadPoolExecutor.getActiveCount() >= threadPoolExecutor.getCorePoolSize()) {
                                logger.error("too many flash socket request!new request will be auto closed!");
                                socket.close();
                            } else {
                                threadPoolExecutor.execute(scw);
                                socketWorkers.add(scw);
                            }
                        }
                    } catch (IOException e) {
                        logger.error("", e);
                    }
                }
            }
        }).start();

        new Thread(new Runnable() {

            @Override
            public void run() {
                for (;;) {
                    try {
                        Thread.sleep(10000);
                    } catch (InterruptedException e) {

                    }
                    synchronized (socketWait) {
                        for (Iterator<SocketWorker> iterator = socketWorkers.iterator(); iterator.hasNext(); ) {
                            SocketWorker socketWorker = iterator.next();
                            if (socketWorker.isStop()) {
                                iterator.remove();
                            }
                        }
                    }
                }
            }
        }).start();
    }

    public String listConfigInfo() {
        StringBuilder sb = new StringBuilder();
        sb.append("socketServerPort:").append(socketServerPort).append("<br>");
        sb.append("corePoolSize:").append(corePoolSize).append("<br>");
        sb.append("maximumPoolSize:").append(maximumPoolSize).append("<br>");
        sb.append("keepAliveTime:").append(keepAliveTime).append("<br>");
        sb.append("queueSize:").append(queueSize).append("<br>");
        return sb.toString();
    }

    public String listSocketInfo() {
        StringBuilder sb = new StringBuilder();
        sb.append("Poll Active size:").append(threadPoolExecutor.getActiveCount()).append("<br>");
        sb.append("Poll Completed size:").append(threadPoolExecutor.getCompletedTaskCount()).append("<br>");
        sb.append("Poll Core Pool size:").append(threadPoolExecutor.getCorePoolSize()).append("<br>");
        sb.append("Poll Maximum Pool size:").append(threadPoolExecutor.getMaximumPoolSize()).append("<br>");
        sb.append("Worker count:").append(socketWorkers.size()).append("<br>");
        for (SocketWorker socketWorker : socketWorkers) {
            sb.append(socketWorker.toString()).append("<br>");
        }
        return sb.toString();
    }

    public void stopSocketWorker(String sessionId) {
        for (Iterator<SocketWorker> iterator = socketWorkers.iterator(); iterator.hasNext(); ) {
            SocketWorker socketWorker = iterator.next();
            if (socketWorker.getSession().getSessionId().equalsIgnoreCase(sessionId)) {
                socketWorker.stop();
                iterator.remove();
            }
        }
    }

    public void stopAllSocketWorker() {
        for (Iterator<SocketWorker> iterator = socketWorkers.iterator(); iterator.hasNext(); ) {
            SocketWorker socketWorker = iterator.next();
            socketWorker.stop();
            iterator.remove();
        }
    }

    public int getSocketServerPort() {
        return socketServerPort;
    }

    public void setSocketServerPort(int socketServerPort) {
        this.socketServerPort = socketServerPort;
    }

    public Integer getCorePoolSize() {
        return corePoolSize;
    }

    public void setCorePoolSize(Integer corePoolSize) {
        this.corePoolSize = corePoolSize;
    }

    public Integer getMaximumPoolSize() {
        return maximumPoolSize;
    }

    public void setMaximumPoolSize(Integer maximumPoolSize) {
        this.maximumPoolSize = maximumPoolSize;
    }

    public Integer getKeepAliveTime() {
        return keepAliveTime;
    }

    public void setKeepAliveTime(Integer keepAliveTime) {
        this.keepAliveTime = keepAliveTime;
    }

    public Integer getQueueSize() {
        return queueSize;
    }

    public void setQueueSize(Integer queueSize) {
        this.queueSize = queueSize;
    }

}
