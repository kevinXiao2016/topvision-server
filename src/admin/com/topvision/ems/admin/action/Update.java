/***********************************************************************
 * $Id: Update.java,v1.0 2011-11-7 上午08:49:56 $
 * 
 * @author: Victor
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.admin.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.HashSet;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Victor
 * @created @2011-11-7-上午08:49:56
 * 
 */
public class Update {
    protected static final Logger logger = LoggerFactory.getLogger(Update.class);
    public static String osname = System.getProperty("os.name");

    /**
     * @param args
     */
    public static void main(String[] args) {
        String startServer = null;
        String stopServer = null;
        String startMysql = null;
        String stopMysql = null;
        boolean update = false;
        boolean isWin = false;
        if (osname.startsWith("Windows")) {
            isWin = true;
        }
        Set<String> argSet = new HashSet<String>();
        if (args.length > 0) {
            for (String arg : args) {
                argSet.add(arg);
            }
        }
        if (argSet.isEmpty() || argSet.contains("nm3000")) {
            startServer = isWin ? "sc start TopvisionNM3000" : "service nm3000_server start";
            stopServer = isWin ? "sc stop TopvisionNM3000" : "service nm3000_server stop";
        }
        if (argSet.isEmpty() || argSet.contains("mysql")) {
            startMysql = isWin ? "sc start \"Topvision Mysql\"" : "service nm3000_mysql start";
            stopMysql = isWin ? "sc stop \"Topvision Mysql\"" : "service nm3000_mysql stop";
        }
        if (argSet.isEmpty() || argSet.contains("update")) {
            update = true;
        }
        new Update().exe(startServer, stopServer, startMysql, stopMysql, update);
    }

    /**
     * 
     * @param startServer
     * @param stopServer
     * @param startMysql
     * @param stopMysql
     * @param update
     */
    private void exe(String startServer, String stopServer, String startMysql, String stopMysql, boolean update) {
        if (stopMysql != null) {
            runCmd(stopMysql);
            logger.info("Mysql stopped");
        }
        if (stopServer != null) {
            runCmd(stopServer);
            logger.info("NM3000 stopped");
        }
        if (update) {
            updateWebapp();
        }
        if (startMysql != null) {
            runCmd(startMysql);
            logger.info("Mysql started");
        }
        if (startServer != null) {
            runCmd(startServer);
            logger.info("NM3000 started");
        }
    }

    private void runCmd(String cmd) {
        try {
            File bin = new File(".");
            logger.info("Run command at {}", bin.getAbsoluteFile());
            final Process process = Runtime.getRuntime().exec(cmd, null, bin.getAbsoluteFile());
            new Thread() {
                public void run() {
                    OutputStream output = process.getOutputStream();
                    try {
                        while (output != null) {
                            output.write('\n');
                            output.flush();
                        }
                    } catch (Exception e) {
                        logger.info("Process is finished");
                        logger.trace("Error:", e);
                    } finally {
                        try {
                            if (output != null) {
                                output.close();
                            }
                        } catch (IOException e) {
                        }
                    }
                }
            }.start();
            process.waitFor();
        } catch (IOException e) {
            logger.warn("", e);
        } catch (InterruptedException e) {
            logger.warn("", e);
        }
    }

    private void updateWebapp() {
        File file = new File("update/update.zip");
        if (file.exists()) {
            File webinf = new File("webapp/WEB-INF");
            boolean b = webinf.delete();
            logger.info("delete the old program:[{}]{}", b, webinf.getPath());
            try {
                Thread.sleep(20000);
            } catch (InterruptedException e) {
            }
            extZipFileList(file, "webapp/");
            logger.info("extract the update file successful!");
            file.deleteOnExit();
            logger.info("delete the update file successful!");
            logger.info("Update successful!");
        }
    }

    private void extZipFileList(File zipFile, String extPlace) {
        try {
            ZipInputStream in = new ZipInputStream(new FileInputStream(zipFile), Charset.forName("GBK"));
            ZipEntry entry = null;
            while ((entry = in.getNextEntry()) != null) {
                String entryName = entry.getName();
                if (entry.isDirectory()) {
                    logger.info("unzip directory:{} ", entryName);
                    File file = new File(extPlace + entryName);
                    file.mkdirs();
                } else {
                    logger.info("unzip file:{} ", entryName);
                    OutputStream os = new FileOutputStream(extPlace + entryName);
                    // Transfer bytes from the ZIP file to the output file
                    byte[] buf = new byte[1024];
                    int len;
                    while ((len = in.read(buf)) > 0) {
                        os.write(buf, 0, len);
                    }
                    os.close();
                }
            }
            in.close();
        } catch (IOException e) {
            logger.debug(zipFile.getAbsolutePath(), e);
        }
    }
}
