/***********************************************************************
 * $Id: SocketChannelWorker.java,v1.0 2014��10��13�� ����5:43:56 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.socketserver.socket;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.topvision.ems.socketserver.domain.SocketRequest;
import com.topvision.ems.socketserver.domain.SocketResponse;
import com.topvision.ems.socketserver.domain.SocketSession;

/**
 * 
 * @author Bravin
 * 
 */
public class SocketWorker implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(SocketWorker.class);
    private Socket socket;
    private OutputStream os = null;
    private InputStream is = null;
    private SocketRequestManager manager;
    private SocketSession session;
    private String beanName;
    private boolean stop = false;

    public SocketWorker(Socket socket) {
        this.socket = socket;
    }

    public SocketWorker(Socket socket, SocketRequestManager manager) {
        this(socket);
        this.manager = manager;
    }

    public SocketWorker(Socket socket, SocketRequestManager manager, SocketSession session) {
        this(socket, manager);
        this.session = session;
    }

    @Override
    public void run() {
        try {
            is = socket.getInputStream();
            os = socket.getOutputStream();
            while (!stop) {
                byte[] data = new byte[1024];
                is.read(data);
                String cmd = new String(data).trim();
                if (cmd.equals("<policy-file-request/>")) {
                    os.write(("<cross-domain-policy>  <allow-access-from  domain=\"*\"  to-ports=\"*\"  />"
                            + "</cross-domain-policy>\0").getBytes());
                    os.flush();
                    break;
                }
                SocketRequest request = parseRequest(cmd);
                if (request != null) {
                    beanName = request.getRequestExecBeanName();
                    request.setSession(session);
                    manager.submitRequest(request);
                } else {
                    break;
                }
            }
        } catch (IOException e) {
            logger.error("", e);
        } finally {
            stop();
        }
    }

    /**
     * 解析请求
     * 
     * @param cmd
     * @return
     */
    public SocketRequest parseRequest(String cmd) {
        SocketRequest socketRequest = new SocketRequest();
        SocketResponse response = new SocketResponse();
        response.setSocket(socket);
        socketRequest.setResponse(response);
        socketRequest.setRequestHost(socket.getInetAddress().getHostAddress());
        if (cmd.startsWith("/")) {
            int first = cmd.indexOf("?");
            String beanName = cmd.substring(1, first);
            socketRequest.setRequestExecBeanName(beanName);
            String cmdparams = cmd.substring(first + 1);
            String[] params = cmdparams.split("&");
            for (String param : params) {
                if (!param.contains("=")) {
                    continue;
                }
                int c = param.indexOf("=");
                socketRequest.addRequestParameter(param.substring(0, c), param.substring(c + 1));
            }
        } else {
            return null;
        }
        return socketRequest;
    }

    public SocketSession getSession() {
        return session;
    }

    public void setSession(SocketSession session) {
        this.session = session;
    }

    public void stop() {
        try {
            os.close();
            logger.info("socket " + socket.toString() + " closed");
        } catch (IOException e) {
            logger.error("", e);
        }
        try {
            is.close();
            logger.info("socket " + socket.toString() + " closed");
        } catch (IOException e) {
            logger.error("", e);
        }
        try {
            socket.close();
            logger.info("socket " + socket.toString() + " closed");
        } catch (IOException e) {
            logger.error("", e);
        }
        stop = true;
    }

    public boolean isStop() {
        return stop;
    }

    @Override
    public String toString() {
        return "SocketWorker{" +
                "socket.client=" + socket.getInetAddress() +
                ", beanName=" + beanName +
                ", session=" + session.getSessionId() +
                ", stop=" + stop +
                '}';
    }
}
