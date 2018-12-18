/***********************************************************************
 * $ TenetBase.java,v1.0 2013-2-26 17:02:48 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.framework.telnet;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author jay
 * @created @2013-2-26-17:02:48
 */
public class TelnetVty {
    private Logger logger = LoggerFactory.getLogger(TelnetVty.class);
    private TelnetIO telnetIO;
    private Set<String> prompt;
    private String morePrompt;
    private long timeout;

    public boolean debug = false;

    public TelnetVty() {
        this.telnetIO = new TelnetIO();
        prompt = new HashSet<String>();
        prompt.add("#");
        prompt.add(">");
        prompt.add(":");
        prompt.add("[n]");
        morePrompt = "--More--";
        timeout = 5000;
    }

    /**
     * connect server with default port(23)
     */
    public void connect(String address) throws IOException {
        connect(address, 23);
    }

    /**
     * connect server
     *
     * @param address
     *            ,host port
     */
    public void connect(String address, int port) throws IOException {
        telnetIO.connect(address, port);
    }

    /**
     * Returns bytes available to be read. Since they haven't been negotiated over, this could be
     * misleading...
     */
    public int available() throws IOException {
        return telnetIO.available();
    }

    /**
     * Sends a String to the remote host. NOTE: uses Java 1.0.2 style String-bytes conversion.
     *
     * @throws IOException
     *             on problems with the socket connection
     */
    public void send(String s) throws IOException {
        if (s.equals("###nortel25")) {
            char nortel = (char) 25;
            s = String.valueOf(nortel);
        }
        byte[] buf = s.getBytes();
        telnetIO.send(buf);
    }

    /**
     * Returns a String from the telnet connection. Blocks until one is available. No guarantees
     * that the string is in any way complete. NOTE: uses Java 1.0.2 style String-bytes conversion.
     */
    public String receive() throws IOException {
        String s;
        byte receiveBytes[] = receiveBytes();
        if (receiveBytes == null) {
            return null;
        }
        s = new String(receiveBytes, "ISO-8859-1");
        return s;
    }

    /**
     * Returns a byte array. Blocks until data is available.
     */
    public byte[] receiveBytes() throws IOException {
        return telnetIO.receive();
    }

    /**
     * Returns all data received up until a certain token.
     *
     * @throws IOException
     *             on problems with the socket connection
     */
    public String receiveUntil() throws IOException {
        StringBuffer buf = new StringBuffer();
        long deadline = 0;
        if (timeout >= 0) {
            deadline = System.currentTimeMillis() + timeout;
        }
        while (true) {
            String tmp;
            if (timeout >= 0) {
                if (logger.isTraceEnabled()) {
                    logger.trace("timeout[" + timeout + "] deadline[" + deadline + "] cru[" + System.currentTimeMillis()
                            + "]");
                }
                if (System.currentTimeMillis() > deadline) {
                    if (logger.isDebugEnabled()) {
                        logger.debug("telnet receiveUntil timeout!!!");
                    }
                    return buf.toString();
                }
                int count = 0;
                int ava = available();
                while (ava <= 0) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException ignored) {
                    }
                    count++;
                    if (count >= 2000) {
                        break;
                    }
                    ava = available();
                }
            }
            tmp = receive();
            tmp = tmp.replaceAll("\u0000", "");
            if (tmp == null || tmp.length() == 0) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException ignored) {
                }
                continue;
            }
            if (morePrompt != null && !morePrompt.equalsIgnoreCase("") && tmp.indexOf(morePrompt) > 0) {
                send(" ");
                tmp = tmp.substring(0, tmp.indexOf(morePrompt));
            }
            logger.debug("tmp.indexOf(morePrompt):" + tmp.indexOf(morePrompt));
            buf.append(tmp);
            boolean isFindP = false;
            for (String p : prompt) {
                if (tmp != null && tmp.trim().endsWith(p.trim())) {
                    isFindP = true;
                }
            }
            if (isFindP) {
                break;
            }
        }
        return buf.toString();
    }

    /**
     * Sends a String to the remote host. NOTE: uses Java 1.0.2 style String-bytes conversion.
     * prompt is not promptflag says that some Command return value frequency and contains prompt
     *
     * @throws IOException
     *             on problems with the socket connection
     */
    public String sendLine(String command) throws IOException {
        if (command.length() > 0 && command.charAt(command.length() - 1) == '\r') {
            command = command.substring(0, command.length() - 1);
        }
        if (command != null && !command.equalsIgnoreCase("")) {
            send(command);
        }
        send("\r");
        String s = receiveUntil();
        if (s == null) {
            return null;
        }
        return s;
    }

    /**
     * Ends the telnet connection.
     */
    public void disconnect() throws IOException {
        if (telnetIO != null) {
            telnetIO.disconnect();
        }
        telnetIO = null;
    }

    public long getTimeout() {
        return timeout;
    }

    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }

    public void setPrompt(String pList, String morePromptString) {
        prompt.clear();
        String[] ps = pList.split(",");
        for (String p : ps) {
            if (p != null && !p.trim().equalsIgnoreCase("")) {
                prompt.add(p);
            }
        }
        morePrompt = morePromptString;
    }

    public Object fullCmd(String command) throws IOException {
        if (command.length() > 0 && command.charAt(command.length() - 1) == '\t') {
            command = command.substring(0, command.length() - 1);
        }
        sendLine("");
        String linePrompt = sendLine("").trim();
        prompt.add(linePrompt);
        if (command != null && !command.equalsIgnoreCase("")) {
            send(command);
        }
        send("\t");
        String s = receiveUntil();
        prompt.remove(linePrompt);
        if (s == null) {
            return null;
        }
        return s;
    }
}
