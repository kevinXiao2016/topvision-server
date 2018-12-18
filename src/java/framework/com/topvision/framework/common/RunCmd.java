/***********************************************************************
 * $Id: RunCmd.java,v 1.1 2007-3-7 上午11:29:25 kelers Exp $
 * 
 * @author: kelers
 * 
 * (c)Copyright 2006 WantTo All rights reserved.
 ***********************************************************************/
package com.topvision.framework.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Create Date 2007-3-7 上午11:29:25
 * 
 * @author kelers
 * 
 */
public final class RunCmd extends Thread {
    private static Logger logger = LoggerFactory.getLogger(RunCmd.class);
    private String[] command = null;
    private StringBuffer stdout = null;
    private StringBuffer stderr = null;
    private boolean result = false;
    private int exitValue = -1;
    private Process proc = null;
    private RunCmd readErr = null;
    private boolean finished = false;

    public RunCmd(String... s) {
        stdout = new StringBuffer();
        stderr = new StringBuffer();
        command = s;
    }

    /**
     * @return the exitValue
     */
    public int getExitValue() {
        return exitValue;
    }

    /**
     * 
     * @return the finished
     */
    public boolean getFinished() {
        return finished;
    }

    /**
     * @return the result
     */
    public boolean getResult() {
        return result;
    }

    /**
     * @return the stderr
     */
    public String getStderr() {
        return stderr.toString();
    }

    boolean getStdErr() {
        BufferedReader bufferedreader = null;
        try {
            bufferedreader = new BufferedReader(
                    new InputStreamReader(proc.getErrorStream(), System.getProperty("sun.jnu.encoding", "GBK")));
            do
                if (!bufferedreader.ready()) {
                    try {
                        proc.exitValue();
                        break;
                    } catch (IllegalThreadStateException _ex) {
                        try {
                            Thread.sleep(100L);
                        } catch (InterruptedException _ex2) {
                        }
                    }
                } else {
                    String s = bufferedreader.readLine();
                    stderr.append(s + "\n");
                }
            while (true);
        } catch (IOException ioexception) {
            stderr.append("Error running command: ");
            if (command != null && command.length > 0) {
                for (String tmp : command) {
                    stderr.append(tmp).append(" ");
                }
            } else {
                stderr.append("null");
            }
            stderr.append(" : ").append(ioexception);
            try {
                bufferedreader.close();
            } catch (IOException ioexception2) {
            }
            return false;
        }
        try {
            bufferedreader.close();
        } catch (IOException ioexception1) {
        }
        return true;
    }

    /**
     * @return the stdout
     */
    public String getStdout() {
        return stdout.toString();
    }

    @Override
    public void run() {
        if (command == null || command.length == 0) {
            if (proc == null) {
                return;
            }
            getStdErr();
        } else {
            try {
                result = runCommand(command);
            } catch (Throwable e) {
                logger.error("exec command error:{},{},{},{},{},{},{},{},{},{},{},", command, e);
                result = false;
            }
        }
        finished = true;
    }

    public boolean runCommand(String... s) {
        Process process = null;
        try {
            if (logger.isDebugEnabled()) {
                logger.debug("exec command:{},{},{},{},{},{},{},{},{},{},{}", (Object[]) s);
            }
            if (s.length == 1) {
                process = Runtime.getRuntime().exec(s[0]);
            } else {
                process = Runtime.getRuntime().exec(s);
            }
            // process.waitFor();
        } catch (Exception exception) {
            logger.debug(exception.getMessage(), exception);
            stderr.append("Command did not execute: ");
            for (String tmp : s) {
                stderr.append(tmp).append(" ");
            }
            stderr.append(" \nException: ").append(exception);
            return false;
        }
        readErr = new RunCmd();
        proc = process;
        readErr.proc = process;
        readErr.stderr = stderr;
        readErr.start();
        InputStreamReader inputstreamreader = null;
        try {
            inputstreamreader = new InputStreamReader(process.getInputStream(),
                    System.getProperty("sun.jnu.encoding", "GBK"));
            char c = (char) inputstreamreader.read();
            if (c != '\uFFFF')
                stdout.append(c);
            while (c != '\uFFFF')
                if (!inputstreamreader.ready()) {
                    try {
                        process.exitValue();
                        break;
                    } catch (IllegalThreadStateException _ex) {
                        try {
                            Thread.sleep(100L);
                        } catch (InterruptedException _ex2) {
                        }
                    }
                } else {
                    c = (char) inputstreamreader.read();
                    stdout.append(c);
                }

        } catch (IOException ioexception) {
            logger.debug(ioexception.getMessage(), ioexception);
            stderr.append("Error running command: ");
            for (String tmp : s) {
                stderr.append(tmp).append(" ");
            }
            stderr.append(" : ").append(ioexception);
            process.destroy();
            try {
                inputstreamreader.close();
            } catch (IOException ioexception1) {
                logger.error("RunCmd : Error closing InputStream {}", ioexception1);
            }
            return false;
        }
        boolean flag = false;
        if ((exitValue = process.exitValue()) == 0)
            flag = true;
        process.destroy();
        try {
            inputstreamreader.close();
        } catch (IOException ioexception2) {
            logger.error("RunCmd : Error closing InputStream {}", ioexception2);
        }
        return flag;
    }

    public void stopCommand() {
        if (proc != null)
            proc.destroy();
    }
}
