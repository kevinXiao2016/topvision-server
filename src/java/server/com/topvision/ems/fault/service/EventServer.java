package com.topvision.ems.fault.service;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.topvision.ems.fault.domain.Alert;
import com.topvision.framework.common.SocketUtils;

/**
 * 事件接收服务器.
 * 
 */
public class EventServer {
    public class EventReceiver implements Runnable {
        private DatagramPacket packet;
        private byte[] recvBuf;

        public EventReceiver(int port) {
            recvBuf = new byte[1024];

            // 创建接受方的udp端口以接收数据
            try {
                socket = new DatagramSocket(port);
            } catch (SocketException e) {
                if (logger.isDebugEnabled()) {
                    logger.debug("EventReceiver error.", e.getMessage());
                }
            }
            packet = new DatagramPacket(recvBuf, recvBuf.length);
        }

        @Override
        public void run() {
            Thread.currentThread().setName("EventReceiver");
            int bufSize = 528;
            while (!stopped) {
                try {
                    socket.receive(packet);
                } catch (IOException e) {
                    if (logger.isDebugEnabled()) {
                        logger.debug("EventReceiver run error.", e.getMessage());
                    }
                }
                Alert alert = new Alert();
                eventSender.sendAlert(alert);
            }

        }
    }

    private static Logger logger = LoggerFactory.getLogger(EventServer.class);
    private static EventServer server = new EventServer();

    public static EventServer getInstance() {
        return server;
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        EventServer server = new EventServer();
        server.start();
    }

    private int listenerPort = 12125;

    private EventSender eventSender = null;

    private boolean stopped = false;

    private DatagramSocket socket;

    private EventServer() {

    }

    public int getListenerPort() {
        return listenerPort;
    }

    public boolean isStopped() {
        return stopped;
    }

    public void setEventSender(EventSender eventSender) {
        this.eventSender = eventSender;
    }

    public void setListenerPort(int listenerPort) {
        this.listenerPort = listenerPort;
    }

    public void setStopped(boolean stopped) {
        this.stopped = stopped;
    }

    public void start() {
        Thread thread = new Thread(new EventReceiver(listenerPort));
        thread.start();
    }

    public void stop() {
        stopped = true;
        SocketUtils.closeQuietly(socket);
    }

}
