/***********************************************************************
 * $Id: CmdFacadeImpl.java,v1.0 2013-11-12 下午3:23:01 $
 * 
 * @author: Victor
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.engine.executor;

import com.topvision.ems.facade.CmdFacade;
import com.topvision.framework.annotation.Facade;
import com.topvision.framework.common.RunCmd;

/**
 * @author Victor
 * @created @2013-11-12-下午3:23:01
 * 
 */
@Facade("cmdFacade")
public class CmdFacadeImpl implements CmdFacade {
    public static String osname = System.getProperty("os.name");
    private RunCmd runcmd = null;

    @Override
    public String ping(String ip, Integer timeout, Integer count) {
        if (ip != null) {
            runcmd = new RunCmd(getPing(ip, timeout, count));
            runcmd.start();
        }
        return getResult();
    }

    @Override
    public String tracert(String ip) {
        if (ip != null) {
            runcmd = new RunCmd(getTracert(ip));
            runcmd.start();
        }
        return getResult();
    }

    @Override
    public String getResult() {
        if (runcmd == null) {
            return "\nComplete!#OK#";
        }
        StringBuffer result = new StringBuffer();
        if (runcmd.getStdout() == null || runcmd.getStdout().length() == 0) {
            result.append(runcmd.getStderr());
        } else {
            result.append(runcmd.getStdout());
        }
        if (runcmd.getFinished()) {
            result.append("\nComplete!#OK#");
        }
        return result.toString();
    }

    private String[] getPing(String ip, int timeout, int count) {
        String[] command = null;
        if (osname.startsWith("Windows")) {
            command = new String[] { "ping", "-n", String.valueOf(count), "-w", String.valueOf(timeout), ip };
        } else if (osname.startsWith("Linux")) {
            command = new String[] { "/bin/ping", "-c", String.valueOf(count), ip };
        } else {
            command = new String[] { "ping", ip };
        }
        return command;
    }

    private String[] getTracert(String ip) {
        String command[] = null;
        if (osname.startsWith("SunOS") || osname.startsWith("Solaris")) {
            command = new String[] { "traceroute", ip };
        } else if (osname.startsWith("Linux")) {
            command = new String[] { "traceroute", ip };
        } else if (osname.startsWith("HP-UX")) {
            command = new String[] { "traceroute", ip };
        } else {
            command = new String[] { "tracert", ip };
        }
        return command;
    }
}
