package com.topvision.ems.fault.service;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class UDPServer implements Runnable {
    protected Logger logger = LoggerFactory.getLogger(getClass());
    private static final int MAX_BUFFER_SIZE = 8096;// 8k
    private static final int DEFAULT_PORT = 9090;
    private Integer bufferSize;
    protected DatagramSocket socket;
    private Thread runner = null;
    private volatile Boolean shouldStop = false;

    /**
     * 使用默认监听端口(9090)和默认的接收缓存大小(8096字节)构建一个UDPServer对象。
     * 
     */
    protected UDPServer() throws SocketException {
        this(DEFAULT_PORT, MAX_BUFFER_SIZE);
    }

    /**
     * 
     * 构建一个UDPServer对象。使用默认的接收缓存大小(8096字节)
     * 
     * @param port
     *            服务监听端口
     * @throws SocketException
     */
    protected UDPServer(int port) throws SocketException {
        this(port, MAX_BUFFER_SIZE);
    }

    /**
     * 
     * 构建一个UDPServer对象。
     * 
     * @param port
     *            服务监听端口
     * @param bufferSize
     *            接收缓存大小（单位字节）
     * @throws SocketException
     *             端口被占用或没有权限时抛出的例外
     */
    protected UDPServer(int port, int bufferSize) throws SocketException {
        this.bufferSize = bufferSize;
        this.socket = new DatagramSocket(port);
    }

    /**
     * 102. * 处理UDP请求 103. * 104. *
     * 
     * @param input
     *            UDP数据报 105.
     */
    public abstract void requestHandle(DatagramPacket input);

    /**
     * 84. * (non-Javadoc) 85. * 86. *
     * 
     * @see java.lang.Runnable#run() 87.
     */
    @Override
    public void run() {
        byte[] buffer = new byte[bufferSize];
        while (!shouldStop) {
            DatagramPacket input = new DatagramPacket(buffer, buffer.length);
            try {
                socket.receive(input);
                requestHandle(input);

                /*
                 * InetAddress target=input.getAddress();
                 * System.out.println("Received from"+target); //输出请求地址 int port=input.getPort();
                 * //得到接收端口 byte[] message="This is server,Who are you?".getBytes(); //服务器返回信息
                 * input=new DatagramPacket(message,message.length,target,port); //实例化数据报
                 * socket.send(input); //发送数据报
                 */

            } catch (IOException ioe) {
                if (logger.isDebugEnabled()) {
                    logger.error("socket.receive failed", ioe.getMessage());
                }
            }
        }
    }

    /**
     * 开始UDP服务
     */
    public synchronized void start() {
        if (runner == null) {
            runner = new Thread(this);
            runner.setDaemon(false);
            runner.start();
        }
    }

    /**
     * 停止UDP服务
     */
    public synchronized void stop() {
        if (socket != null) {
            shouldStop = true;
            runner.interrupt();
            runner = null;
            socket.close();
            socket = null;
        }
    }

}
