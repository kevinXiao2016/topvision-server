/***********************************************************************
 * P2PChannel.java,v1.0 17-5-22 上午10:45 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.webproxy.nat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 * @author jay
 * @created 17-5-22 上午10:45
 */
public class P2PChannel extends Thread {
    private Logger logger = LoggerFactory.getLogger(P2PChannel.class);
    private Socket outSocket;
    private DataOutputStream out;
    private InputStream in;
    private String srcIp;
    private Integer srcPort;
    private String descIp;
    private Integer descPort;
    private boolean htmlData = false;
    private List<byte[]> datas = new ArrayList<>();

    public P2PChannel(Socket outSocket, Socket inSocket) throws IOException {
        this.out =new DataOutputStream(outSocket.getOutputStream());
        this.in = inSocket.getInputStream();
        this.outSocket = outSocket;
    }

    public P2PChannel(Socket out, Socket in, String descIp, Integer descPort, String srcIp, Integer srcPort) throws IOException {
        this(out,in);
        this.srcIp = srcIp;
        this.srcPort = srcPort;
        this.descIp = descIp;
        this.descPort = descPort;
    }

    @Override
    public void run() {
        try {
            String replaceMapString;
            String replaceProxyString;
            byte[] buffer;
            int temp;
            while (true) {
                try {
                    try {
                        buffer = new byte[1024 * 4];
                        temp = in.read(buffer);
                    } catch (SocketException e) {
                        if ("socket closed".equalsIgnoreCase(e.getMessage().toLowerCase())) {
                            logger.debug("P2PChannel socket closed");
                            break;
                        } else {
                            logger.debug("", e);
                            break;
                        }
                    }
                    if (temp == -1) {
                        if (descIp != null && htmlData) {
                            replaceMapString = "http://" + descIp + "/";
                            replaceProxyString = "http://" + srcIp + ":" + srcPort + "/";
                            int totalLength = 0;
                            List<byte[]> subs = new ArrayList<>();
                            for (byte[] data : datas) {
                                byte[] sub = trim(data);
                                totalLength = totalLength + sub.length;
                                subs.add(sub);
                            }
                            ByteBuffer bu = ByteBuffer.allocate(totalLength);
                            for (byte[] sub : subs) {
                                bu.put(sub);
                            }
                            byte[] bb = bu.array();
                            String str = new String(bb);
                            String httpStr = "http://" + descIp + "/";
                            if (str.contains(httpStr)) {
                                byte[] bufferWrite = replaceAll(bb,replaceMapString,replaceProxyString);
                                out.write(bufferWrite, 0, bufferWrite.length);
                            } else {
                                out.write(bb, 0, bb.length);
                            }
                        }
                        break;
                    } else if (descIp != null) {
                        String str = null;
                        if (!htmlData) {
                            str = new String(buffer);
                        }
                        if (htmlData || isHtml(str)) {
                            htmlData = true;
                            datas.add(buffer);
                        } else {
                            out.write(buffer, 0, temp);
                        }
                    } else {
                        out.write(buffer, 0, temp);
                    }
                } catch (Exception e) {
                    logger.debug("", e);
                }
            }
        } finally {
            channelClose();
        }
    }
      
    private byte[] replaceAll(byte[] bb, String replaceMapString, String replaceProxyString) {
        byte[] replaceMapBs = replaceMapString.getBytes();
        byte[] replaceProxyBs = replaceProxyString.getBytes();

        List<byte[]> datas = new ArrayList<>();
        int i = 0;
        int offset = 0;//需要被裁剪的起始位置
        for (; i < bb.length; ) {
            int j = 0;
            for (; j < replaceMapBs.length; j++) {
                if (bb[i + j] != replaceMapBs[j]) {
                    break;
                }
            }
            if (j == replaceMapBs.length) {
                int length = i - offset;
                if (length > 0) {
                    ByteBuffer bu = ByteBuffer.allocate(length);
                    bu.put(bb,offset,length);
                    datas.add(bu.array());
                    datas.add(replaceProxyBs);
                }
                offset = i = i + replaceMapBs.length;
            } else {
                i++;
            }
        }
        int length = i - offset;
        if (length > 0) {
            ByteBuffer bu = ByteBuffer.allocate(length);
            bu.put(bb, offset, length);
            datas.add(bu.array());
        }
        int totalLength = 0;
        for (byte[] data : datas) {
            totalLength = totalLength + data.length;
        }
        ByteBuffer re = ByteBuffer.allocate(totalLength);
        for (byte[] bs : datas) {
            re.put(bs);
        }
        return re.array();
    }

    private byte[] trim(byte[] bytes) {
        int len = bytes.length;
        int st = 0;
        while ((st < len) && (bytes[st] == 0)) {
            st++;
        }
        while ((st < len) && (bytes[len - 1] == 0)) {
            len--;
        }
        byte[] tmp = new byte[len - st];
        for (int i = 0;st < len ;st++,i++) {
            tmp[i] = bytes[st];
        }
        return tmp;
    }

    public void channelClose(){
        try {
            if (out != null) {
                out.close();
            }
        } catch (Exception e) {
            logger.debug("", e);
        }
        try {
            if (outSocket != null) {
                outSocket.close();
            }
        } catch (Exception e) {
            logger.debug("", e);
        }
    }

    public boolean isHtml(String str) {
        if (str == null) {
            return false;
        }
        return str.contains("</http>")
                ||str.contains("</a>")
                ||str.contains("</td>")
                ||str.contains("</div>")
                ||str.contains("url=\"http://")
                ||str.contains("src=\"http://")
                ||str.contains("href=\"http://")
                ||str.contains("url=\'http://")
                ||str.contains("src=\'http://")
                ||str.contains("href=\'http://")
                ||str.contains("url=http://")
                ||str.contains("src=http://")
                ||str.contains("href=http://");
    }

    @Override
    public String toString() {
        return "P2PChannel{" +
                ",&nbsp;&nbsp;&nbsp;&nbsp;srcIp='" + srcIp + '\'' + "<br>" +
                ",&nbsp;&nbsp;&nbsp;&nbsp;srcPort=" + srcPort + "<br>" +
                ",&nbsp;&nbsp;&nbsp;&nbsp;descIp='" + descIp + '\'' + "<br>" +
                ",&nbsp;&nbsp;&nbsp;&nbsp;descPort=" + descPort + "<br>" +
                '}';
    }
}
