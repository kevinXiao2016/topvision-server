package com.topvision.framework.utils;

import com.topvision.framework.common.RunCmd;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jay on 18-6-25.
 */
public class CmdDiskSpace {
    protected static Logger logger = LoggerFactory.getLogger(CmdDiskSpace.class);
    public static String osname = System.getProperty("os.name");
    public static String userdir = System.getProperty("user.dir");
    private RunCmd runcmd = null;

    public Map<String,Integer> diskSpace() {
        logger.debug("CmdDiskSpace.osname:{};userdir:{}",osname,userdir);
        runDiskspace();
        if (!runcmd.getResult() || runcmd.getExitValue() != 0) {
            if (logger.isDebugEnabled()) {
                logger.debug("getStdout:{}", runcmd.getStdout());
                logger.debug("getStderr:{}", runcmd.getStderr());
            }
            return null;
        }

        String output = runcmd.getStdout();
        if (logger.isDebugEnabled()) {
            logger.debug(output);
        }
        try {
            Map<String,Integer> map = new HashMap<>();
            String[] strs = output.split(",");
            map.put("free",Integer.parseInt(strs[0]));
            map.put("total",Integer.parseInt(strs[1]));
            return map;
        } catch (NumberFormatException e) {
            logger.error("CmdDiskSpace.parseInt error{}", output);
            return null;
        }
    }
    private void runDiskspace() {
        String[] command = getCommand();
        runcmd = new RunCmd(command);
        runcmd.start();
        do {
            try {
                Thread.sleep(100L);
            } catch (Exception ex) {
            }
        } while (!runcmd.getFinished());
    }

    private String[] getCommand() {
        String[] command;
        if (osname.startsWith("Windows")) {
            command = new String[] { userdir + "/bin/diskspace.exe", userdir };
        } else if (osname.startsWith("Linux")) {
            command = new String[] { userdir + "/bin/diskspace", userdir };
        } else {
            command = new String[] { userdir + "/bin/diskspace", userdir };
        }
        return command;
    }
}
