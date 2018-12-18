/***********************************************************************
 * $Id: MachineUtils.java,v1.0 2011-5-22 上午10:04:47 $
 * 
 * @author: Victor
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.framework.common;

import java.io.IOException;
import java.io.LineNumberReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Victor
 * @created @2011-5-22-上午10:04:47
 * 
 */
public class MachineUtils {
    protected static Logger logger = LoggerFactory.getLogger(MachineUtils.class);
    public static String osname = System.getProperty("os.name");

    public static String getMac() {
        RunCmd runcmd = null;
        String output = null;

        if (osname.startsWith("Linux")) {
            runcmd = new RunCmd("ifconfig");
        } else if (osname.startsWith("HP-UX")) {
            runcmd = new RunCmd("ifconfig");
        } else {
            runcmd = new RunCmd(new String[]{"ipconfig","/all"});
        }
        runcmd.start();
        do {
            try {
                Thread.sleep(100L);
            } catch (Exception ex) {
            }
        } while (!runcmd.getFinished());

        if (!runcmd.getResult() || runcmd.getExitValue() != 0) {
            return null;
        }
        output = runcmd.getStdout().toString();
        List<String> macs = new ArrayList<String>();
        if (osname.startsWith("Linux")) {
            // TODO
            macs.add(output);
        } else if (osname.startsWith("HP-UX")) {
            // TODO
            macs.add(output);
        } else {
            try {
                LineNumberReader reader = new LineNumberReader(new StringReader(output));
                String line;
                while ((line = reader.readLine()) != null) {
                    if (line.indexOf("Physical Address") > 0) {
                        macs.add(line.substring(line.indexOf("-") - 2));
                    }
                }
                reader.close();
            } catch (IOException e) {
                logger.error("", e);
            }
        }
        if (macs.size() == 0) {
            return null;
        } else if (macs.size() == 1) {
            return macs.get(0);
        } else {
            Collections.sort(macs);
            logger.debug("All macs:" + macs);
            String mac = null;
            while (mac == null) {
                for (int i = macs.size() - 1; i >= 0; i--) {
                    if (!macs.get(i).startsWith("00-00") && !macs.get(i).startsWith("FF-")) {
                        mac = macs.get(i);
                    }
                }
            }
            return mac;
        }
    }
}
