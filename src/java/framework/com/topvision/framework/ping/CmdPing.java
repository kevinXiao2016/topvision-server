/***********************************************************************
 * $Id: CmdPing.java,v 1.1 Aug 26, 2009 8:23:52 PM kelers Exp $
 * 
 * @author: kelers
 * 
 * (c)Copyright 2011 Topoview All rights reserved.
 ***********************************************************************/
package com.topvision.framework.ping;

import com.topvision.framework.common.RunCmd;

/**
 * @Create Date Aug 26, 2009 8:23:52 PM
 * 
 *         Ping在不同平台的返回结果： Request timed out. 来自 172.17.0.2 的回复: 无法访问目标主机。 From 172.17.1.247
 *         icmp_seq=3 Destination Host Unreachable
 * 
 *         Reply from 172.22.4.37: TTL expired in transit.
 * 
 *         Reply from 172.17.1.1: bytes=32 time<1ms TTL=64 来自 172.17.1.1 的回复: 字节=32 时间=23ms TTL=61
 *         64 bytes from 172.17.1.1: icmp_seq=5 ttl=64 time=0.250 ms
 * 
 * @author kelers
 * 
 */
public class CmdPing extends Ping {
    public static String osname = System.getProperty("os.name");
    private RunCmd runcmd = null;

    @Override
    public int ping(String host, int timeout, int count) {
        return ping(host, timeout, count, 0);
    }

    @Override
    public int ping(String host, int timeout, int count, int retry) {

        for (int i = 0; i <= retry; i++) {
            runPing(host, timeout, count);

            if (!runcmd.getResult() || runcmd.getExitValue() != 0) {
                if (logger.isDebugEnabled()) {
                    logger.debug("getStdout:{}", runcmd.getStdout());
                    logger.debug("getStderr:{}", runcmd.getStderr());
                }
                continue;
            }

            String output = runcmd.getStdout();
            if (logger.isDebugEnabled()) {
                logger.debug(output);
            }
            if (output.indexOf("TTL expired in transit.") != -1) {
                continue;
            }
            int index2 = output.indexOf("ms");
            if (index2 == -1) {
                continue;
            }

            output = output.substring(0, index2);
            int index1 = output.indexOf("<");
            if (index1 == -1) {
                index1 = output.lastIndexOf("=");
            }
            // Linux返回值有小数值
            try {
                int result = (int) (Float.parseFloat(output.substring(index1 + 1, index2)));
                if (logger.isDebugEnabled()) {
                    logger.debug("ping [{} -w {} -n {}] expends {} Millis",
                            new Object[] { host, timeout, count, result });
                }
                return result;
            } catch (Exception e) {
            }

        }

        if (logger.isDebugEnabled()) {
            logger.debug("ping [{} -w {} -n {} -r {}] timed out.", new Object[] { host, timeout, count, retry });
        }
        return -1;
    }

    private void runPing(String ip, int timeout, int count) {
        String[] command = getCommand(ip, timeout, count);
        runcmd = new RunCmd(command);
        runcmd.start();
        do {
            try {
                Thread.sleep(100L);
            } catch (Exception ex) {
            }
        } while (!runcmd.getFinished());
    }

    private String[] getCommand(String ip, int timeout, int count) {
        String[] command = null;
        if (osname.startsWith("Windows")) {
            command = new String[] { "ping", "-n", String.valueOf(count), "-w", String.valueOf(timeout), ip };
        } else if (osname.startsWith("Linux")) {
            int timeoutInSecond = timeout / 1000;
            if (timeoutInSecond < 1) {
                timeoutInSecond = 1;
            }
            command = new String[] { "/bin/ping", "-c", String.valueOf(count), "-W", String.valueOf(timeoutInSecond),
                    ip };
        } else {
            command = new String[] { "ping", ip };
        }
        return command;
    }
}
