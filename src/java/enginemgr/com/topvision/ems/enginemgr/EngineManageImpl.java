/***********************************************************************
 * $Id: EngineManageImpl.java,v1.0 2016-6-1 下午3:44:19 $
 * 
 * @author: Rod John
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.enginemgr;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.remoting.rmi.RmiProxyFactoryBean;
import org.springframework.stereotype.Service;

import com.topvision.framework.EnvironmentConstants;

/**
 * @author Rod John
 * @created @2016-6-1-下午3:44:19
 *
 */
@Service("engineManage")
public class EngineManageImpl implements EngineManage {
    protected Logger logger = LoggerFactory.getLogger(getClass());
    @Value("${engine.mgr.port:3009}")
    private Integer port;
    @Value("${java.rmi.server.hostname:127.0.0.1}")
    private String hostname;

    @Override
    public void transfer(String filename, byte[] fileByte) {
        if (isWindows()) {
            filename = filename.replace("/", File.separator);
        } else if (isLinux()) {
            filename = filename.replace("\\", File.separator);
        }
        File file = new File(filename);
        BufferedOutputStream bos = null;
        try {
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            if (!file.exists()) {
                file.createNewFile();
                bos = new BufferedOutputStream(new FileOutputStream(file));
                bos.write(fileByte);
                bos.flush();
            }
        } catch (FileNotFoundException e) {
            logger.error("", e);
        } catch (IOException e) {
            logger.error("", e);
        } finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e) {
                }
            }
        }
    }

    private CheckFacade getCheckFacade(String port) {
        try {
            RmiProxyFactoryBean proxy = new RmiProxyFactoryBean();
            StringBuilder serviceUrl = new StringBuilder("rmi://localhost:").append(port).append("/CheckFacade");
            proxy.setServiceUrl(serviceUrl.toString());
            proxy.setServiceInterface(CheckFacade.class);
            proxy.afterPropertiesSet();
            CheckFacade checkFacade = (CheckFacade) proxy.getObject();
            return checkFacade;
        } catch (Exception e) {
            logger.error("getCheckFacade error:" + port, e);
            return null;
        }
    }

    private void generateScript(String port, String xms, String xmx) {
        File batFile = null;
        if (isWindows()) {
            batFile = new File("engine-" + port + "/runEngine.bat");
        } else if (isLinux()) {
            batFile = new File("engine-" + port + "/runEngine.sh");
        }
        try {
            List<String> list = new ArrayList<>();
            BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(new FileInputStream(batFile), "UTF-8"));
            String readString;
            while ((readString = bufferedReader.readLine()) != null) {
                // readString = readString.replace("3004", port).replace("1024",
                // xms).replace("8192", xmx);

                if (readString.contains("OPTS=-Xms")) {
                    if (isWindows()) {
                        StringBuilder sBuilder = new StringBuilder("set OPTS=-Xms").append(xms).append("M -Xmx")
                                .append(xmx).append("M");
                        readString = sBuilder.toString();
                    } else if (isLinux()) {
                        StringBuilder sBuilder = new StringBuilder("OPTS=-Xms").append(xms).append("M -Xmx").append(xmx)
                                .append("M");
                        readString = sBuilder.toString();
                    }
                }
                list.add(readString);
            }
            bufferedReader.close();
            batFile.delete();
            File replacefile = null;
            if (isWindows()) {
                replacefile = new File("engine-" + port + "/runEngine.bat");
            } else if (isLinux()) {
                replacefile = new File("engine-" + port + "/runEngine.sh");
            }
            BufferedWriter bufferedWriter = new BufferedWriter(
                    new OutputStreamWriter(new FileOutputStream(replacefile), "UTF-8"));
            for (String line : list) {
                bufferedWriter.write(line);
                bufferedWriter.newLine();
            }
            bufferedWriter.flush();
            bufferedWriter.close();
        } catch (Exception e) {
            logger.error("", e);
        }
    }

    private void generateEngineConfig(String port) {
        File engineConfigFile = new File("engine-" + port + "/conf/engine.properties");
        try {
            List<String> list = new ArrayList<>();
            BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(new FileInputStream(engineConfigFile), "UTF-8"));
            String readString;
            while ((readString = bufferedReader.readLine()) != null) {
                if (readString.contains("engine.port = ")) {
                    readString = "engine.port = " + port;
                } else if (readString.contains("java.rmi.server.hostname")) {
                    readString = "java.rmi.server.hostname=" + hostname;
                }
                list.add(readString);
            }
            bufferedReader.close();
            engineConfigFile.delete();
            File replacefile = new File("engine-" + port + "/conf/engine.properties");
            BufferedWriter bufferedWriter = new BufferedWriter(
                    new OutputStreamWriter(new FileOutputStream(replacefile), "UTF-8"));
            for (String line : list) {
                bufferedWriter.write(line);
                bufferedWriter.newLine();
            }
            bufferedWriter.flush();
            bufferedWriter.close();
        } catch (Exception e) {
            logger.error("", e);
        }
    }

    @Override
    public void deleteEngineDir(String port) {
        File engineDir = new File("engine-" + port);
        if (engineDir.exists()) {
            // engineDir.delete();
            deleteDir(engineDir);
        }
    }

    @Override
    public void reStartEngine(String port) {
        this.shutdownEngine(port);
        this.startEngine(port);
    }

    @Override
    public void updatePort(String port, String destPort) {
        // First shut down engine
        this.shutdownEngine(port);
        // Second Update File
        if (destPort != null) {
            File engineDir = new File("engine-" + port);
            if (engineDir.exists()) {
                engineDir.renameTo(new File("engine-" + destPort));
                // Update engine.config
                generateEngineConfig(destPort);
            }
        } else {
            // Update engine.config
            generateEngineConfig(port);
        }
    }

    @Override
    public void updateMem(String port, Integer xmx, Integer xms) {
        // First shut down engine
        this.shutdownEngine(port);
        // Second Update File
        generateScript(port, xms.toString(), xmx.toString());
    }

    @Override
    public void shutdownEngine(String port) {
        CheckFacade checkFacade = getCheckFacade(port);
        if (checkFacade != null) {
            try {
                checkFacade.shutDown();
            } catch (Exception e) {
                logger.error("", e);
            }
        }
    }

    private boolean startEngine(String port) {
        if (isWindows()) {
            try {
                StringBuilder engineBat = new StringBuilder("engine-").append(port).append("\\runEngine.bat");
                Runtime.getRuntime().exec("cmd.exe /c " + engineBat);
                Thread.sleep(10000);
                return true;
            } catch (Exception e) {
                logger.error("start Engine:" + port + " error", e);
                return false;
            }
        } else if (isLinux()) {
            try {
                StringBuilder sb = new StringBuilder().append("cd engine-").append(port)
                        .append(";chmod a+x runEngine.sh;").append("./runEngine.sh");
                String[] cmd = { "/bin/sh", "-c", sb.toString() };
                Runtime.getRuntime().exec(cmd);
                Thread.sleep(10000);
                return true;
            } catch (Exception e) {
                logger.error("start Engine:" + port + " error", e);
                return false;
            }
        }
        return false;
    }

    private void deleteDir(File path) {
        if (!path.exists())
            return;
        if (path.isFile()) {
            path.delete();
            return;
        }
        File[] files = path.listFiles();
        for (int i = 0; i < files.length; i++) {
            deleteDir(files[i]);
        }
        path.delete();
    }

    /**
     * 判断操作系统是否是Linux
     * 
     * @return
     */
    private boolean isLinux() {
        String osname = System.getProperty("os.name");
        if (osname.contains("Linux") || osname.contains("linux")) {
            return true;
        }
        return false;
    }

    /**
     * 判断操作系统是否是Windows
     * 
     * @return
     */
    private boolean isWindows() {
        String osname = System.getProperty("os.name");
        if (osname.contains("Windows") || osname.contains("windows")) {
            return true;
        }
        return false;
    }

    /**
     * @return the port
     */
    public Integer getPort() {
        return port;
    }

    @Override
    public Integer getEngineMgrPort() {
        return port;
    }

    @Override
    public String getHostname() {
        if (hostname == null || "127.0.0.1".equals(hostname)) {
            hostname = EnvironmentConstants.getHostAddress();
        }
        return hostname;
    }

    @Override
    public void shutdownMgr() {
        File dir = new File("./");
        for (String file : dir.list()) {
            logger.info("-------------" + file);
            if (file.startsWith("engine-")) {
                try {
                    logger.info("------port-------" + file);
                    String port = file.substring(7);
                    shutdownEngine(port);
                    logger.info("shutdown success");
                } catch (Exception e) {
                    logger.error("ShutdownEngine " + port + "error :", e);
                }
            }
        }
        System.exit(0);
    }
}
