/***********************************************************************
 * $Id: SyslogManager.java,v 1.1 May 20, 2009 8:41:59 PM kelers Exp $
 * 
 * @author: kelers
 * 
 * (c)Copyright 2011 Topoview All rights reserved.
 ***********************************************************************/
package com.topvision.framework.syslog;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.remoting.RemoteLookupFailureException;

import com.topvision.framework.annotation.Engine;

/**
 * @Create Date May 20, 2009 8:41:59 PM
 * 
 * @author kelers
 * 
 */
@Engine("syslogManager")
public class SyslogManager {
    public static void main(String[] args) {
        try {
            SyslogManager sm = new SyslogManager();
            sm.addPort(DEFAULT_PORT);
            // sm.addListener(new FileSyslogListener());
        } catch (Throwable e) {
            e.printStackTrace(System.err);
        }
    }

    protected Logger logger = LoggerFactory.getLogger(getClass());
    public final static int BUF_LENGTH = 1024;
    public final static int DEFAULT_PORT = 514;
    private ArrayBlockingQueue<DatagramPacket> logQueue = new ArrayBlockingQueue<DatagramPacket>(1024);
    private Map<Integer, DatagramSocket> ports = new HashMap<Integer, DatagramSocket>(2);
    private List<SyslogCallback> listeners = new ArrayList<SyslogCallback>();

    private ExecutorService executorService;

    /**
     * @param listeners
     */
    public void addListener(List<SyslogCallback> listeners) {
        if (listeners == null || listeners.isEmpty()) {
            return;
        }
        for (SyslogCallback l : listeners) {
            if (!this.listeners.contains(l)) {
                this.listeners.add(l);
            }
        }
    }

    /**
     * @param listener
     */
    public void addListener(SyslogCallback listener) {
        this.listeners.add(listener);
    }

    /**
     * 添加一个端口到接收syslog端口列表
     * 
     * @param port
     * @throws IOException
     * @throws InterruptedException
     */
    public void addPort(int port) throws IOException, InterruptedException {
        if (ports == null) {
            return;
        }
        if (!ports.containsKey(port)) {
            DatagramSocket socket = new DatagramSocket(port);
            ports.put(port, socket);
            runListener(socket);
            if (logger.isInfoEnabled()) {
                logger.info("Syslog listener[" + port + "] started...");
            }
        }
    }

    @PreDestroy
    public void destroy() {
        if (logger.isDebugEnabled()) {
            logger.debug("SyslogManager destroy:" + ports);
        }
        while (!ports.isEmpty()) {
            removePort(ports.keySet().iterator().next());
        }
        ports.clear();
        ports = null;
        executorService.shutdownNow();
        logQueue = null;
    }

    /**
     * @return the listeners
     */
    public List<SyslogCallback> getListeners() {
        return listeners;
    }

    @PostConstruct
    public void initialize() {
        executorService = Executors.newSingleThreadExecutor();
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                Thread.currentThread().setName("SyslogManager");
                String host = null;
                String text = null;
                while (logQueue != null) {
                    try {
                        DatagramPacket packet = logQueue.take();
                        if (packet == null) {
                            continue;
                        }
                        host = packet.getAddress().getHostAddress();
                        text = new String(packet.getData(), 0, packet.getLength());
                        // System.out.println("#" + text);
                        int start = text.indexOf('<');
                        int stop = text.indexOf('>');
                        int priority = 0;

                        try {
                            priority = Integer.parseInt(text.substring(start + 1, stop));
                        } catch (Exception e1) {
                            priority = 0;
                        }
                        String msg = text.substring(stop + 1);
                        Syslog log = new Syslog();
                        log.setText(text);
                        log.message(DateFormat.getInstance().format(new Date()), host, priority, msg);
                        for (Iterator<SyslogCallback> itr = listeners.iterator(); itr.hasNext();) {
                            try {
                                itr.next().onSyslog(log);
                            } catch (RemoteLookupFailureException e) {
                                itr.remove();
                            }
                        }
                    } catch (Exception e) {
                        logger.error("Syslog error:{}", e.getMessage());
                        logger.debug("syslog error", e);
                    }
                }
            }
        });
    }

    /**
     * @param listener
     */
    public void removeListener(SyslogCallback listener) {
        if (listeners.contains(listener)) {
            this.listeners.remove(listener);
        }
    }

    /**
     * 取消端口对syslog的接收
     * 
     * @param port
     * @throws IOException
     * @throws InterruptedException
     */
    public void removePort(int port) {
        if (ports.containsKey(port)) {
            DatagramSocket socket = ports.remove(port);
            socket.close();
            if (logger.isInfoEnabled()) {
                logger.info("Syslog listener[" + port + "] removed");
            }
        }
    }

    /**
     * @param param
     */
    public void reset(SyslogServerParam param) {
        List<Integer> ps = param.getListenPorts();
        for (Integer port : ps) {
            try {
                addPort(port);
            } catch (IOException e) {
                logger.debug(e.getMessage(), e);
            } catch (InterruptedException e) {
                logger.debug(e.getMessage(), e);
            }
        }
        Iterator<Integer> it = ports.keySet().iterator();
        while (it.hasNext()) {
            Integer port = it.next();
            if (!ps.contains(port)) {
                DatagramSocket socket = ports.get(port);
                socket.close();
                if (logger.isInfoEnabled()) {
                    logger.info("Syslog listener[" + port + "] removed");
                }
                it.remove();
            }
        }
    }

    /**
     * @param socket
     * @throws IOException
     * @throws InterruptedException
     */
    private void runListener(final DatagramSocket socket) {
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                Thread.currentThread().setName("SyslogListener");
                DatagramPacket packet;
                while (!socket.isClosed()) {
                    packet = new DatagramPacket(new byte[BUF_LENGTH], BUF_LENGTH);
                    try {
                        socket.receive(packet);
                        logQueue.put(packet);
                    } catch (IOException e) {
                    } catch (InterruptedException e) {
                    }
                }
            }
        });
    }

    /**
     * @param syslog
     */
    public void sendSyslog(Syslog syslog) {
    }

    /**
     * @param listeners
     *            the listeners to set
     */
    public void setListeners(List<SyslogCallback> listeners) {
        this.listeners = listeners;
    }
}
