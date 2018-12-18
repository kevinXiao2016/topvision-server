/***********************************************************************
 * $Id: TftpServerServiceImpl.java,v1.0 2013-1-26 下午3:58:19 $
 * 
 * @author: fanzidong
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.platform.service.impl;

import java.beans.PropertyDescriptor;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import com.topvision.framework.tftp.TftpEventListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.framework.service.BaseService;
import com.topvision.framework.tftp.Server;
import com.topvision.platform.dao.SystemPreferencesDao;
import com.topvision.platform.domain.SystemPreferences;
import com.topvision.platform.domain.TftpFile;
import com.topvision.platform.domain.TftpInfo;
import com.topvision.platform.service.TftpServerService;
import com.topvision.platform.util.LocalhostNetUtil;

/**
 * @author fanzidong
 * @created @2013-1-26-下午3:58:19
 * 
 */
@Service("tftpServerService")
public class TftpServerServiceImpl extends BaseService implements TftpServerService {
    @Autowired
    private SystemPreferencesDao systemPreferencesDao;

    private Server server;
    private boolean started;

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.framework.service.BaseService#destroy()
     */
    @Override
    @PreDestroy
    public void destroy() {
        if (server != null) {
            server.disconnect();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.framework.service.BaseService#initialize()
     */
    @Override
    @PostConstruct
    public void initialize() {
        TftpInfo tftpServer = getTftpServerAttr();
        if (tftpServer != null && tftpServer.getEnable() == TftpInfo.ON) {
            startTftpServer();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.platform.service.TftpServerService#getTftpServerAttr()
     */
    @Override
    public TftpInfo getTftpServerAttr() {
        // 取出数据库中数据
        List<SystemPreferences> systemPreferences = systemPreferencesDao.selectByModule("tftpServer");
        if (systemPreferences != null) {
            TftpInfo tftpServer = new TftpInfo();
            for (SystemPreferences systemPreferences2 : systemPreferences) {
                try {
                    PropertyDescriptor pd = new PropertyDescriptor(systemPreferences2.getName(), tftpServer.getClass());
                    Method setMethod = pd.getWriteMethod();
                    String typeName = pd.getPropertyType().getName();
                    if (typeName.equals("java.lang.String")) {
                        setMethod.invoke(tftpServer, systemPreferences2.getValue());
                    } else if (typeName.equals("int")) {
                        setMethod.invoke(tftpServer, Integer.valueOf(systemPreferences2.getValue()));
                    } else if (typeName.equals("boolean")) {
                        setMethod.invoke(tftpServer, Boolean.valueOf(systemPreferences2.getValue()));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }
            // 将IP地址转换成网卡地址，不能使用127.0.0.1(多网卡可能存在隐患，请注意)
            if (tftpServer.getIp().equals("127.0.0.1")) {
                List<String> ips = getAllIpAddress();
                for (String ip : ips) {
                    if (!ip.equals("127.0.0.1")) {
                        tftpServer.setIp(ip);
                        // 修改数据库中的FTP服务器参数的IP地址
                        modifyTftpServerAttr(tftpServer);
                        break;
                    }
                }
            }
            return tftpServer;
        } else {
            logger.debug("failed to get data from database!");
            return null;
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.platform.service.TftpServerService#isTftpServerStarted()
     */
    @Override
    public boolean isTftpServerStarted() {
        return isStarted();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.platform.service.TftpServerService#modifyTftpServerAttr(com.topvision.platform
     * .domain.TftpInfo)
     */
    @Override
    public void modifyTftpServerAttr(TftpInfo tftpServer) {
        // 将ftpServer中的属性封装成List<SystemPreferences>
        List<SystemPreferences> tftpServerAttrs = server2Prefer(tftpServer);
        systemPreferencesDao.updateEntity(tftpServerAttrs);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.platform.service.TftpServerService#startTftpServer()
     */
    @Override
    public boolean startTftpServer() {
        TftpInfo tftpServer = getTftpServerAttr();
        if (tftpServer != null) {
            // 判断tftpServer所指定的根路径是否存在，不存在则创建
            String tftpRoot = TftpInfo.getTftprootpath() + tftpServer.getRootPath();
            File rootFolder = new File(tftpRoot);
            if (!rootFolder.isDirectory()) {
                if (!rootFolder.mkdirs()) {
                    logger.info("failed to create the root path: " + tftpServer.getRootPath());
                    return false;
                }
            }
            // 判断server对象是否存在，不存在则创建
            if (server == null) {
                server = new Server(tftpRoot, tftpServer.getPort());
            }
            // 判断端口是否被占用
            if (LocalhostNetUtil.checkUdpPortUsage(tftpServer.getPort())) {
                setStarted(false);
                logger.info("UDP port " + tftpServer.getPort()
                        + " is used by another program! We cannot start tftpServer!");
                return false;
            }
            try {
                server.connect();
                setStarted(true);
                SystemPreferences sp = new SystemPreferences();
                sp.setName("enable");
                sp.setValue("1");
                sp.setModule("tftpServer");
                systemPreferencesDao.updateEntity(sp);
                return true;
            } catch (Exception e) {
                setStarted(false);
                logger.info("TFtpServer can't be initialized with the params:{port:" + tftpServer.getPort()
                        + ", rootPath:" + tftpRoot + "}");
                return false;
            }
        } else {
            return false;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.platform.service.TftpServerService#stopTftpServer()
     */
    @Override
    public void stopTftpServer() {
        if (server != null) {
            server.disconnect();
            server = null;
            SystemPreferences sp = new SystemPreferences();
            sp.setName("enable");
            sp.setValue("0");
            sp.setModule("tftpServer");
            systemPreferencesDao.updateEntity(sp);
        }
        // 将server置为空，以防下次start时启动参数不是最新的
        setStarted(false);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.platform.service.TftpServerService#reStartTftpServer()
     */
    @Override
    public boolean reStartTftpServer() {
        stopTftpServer();
        return startTftpServer();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.platform.service.TftpServerService#isUdpPortUsed(int)
     */
    @Override
    public boolean isUdpPortUsed(int port) {
        return LocalhostNetUtil.checkUdpPortUsage(port);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.platform.service.TftpServerService#getTftpServerDirectories()
     */
    @Override
    public List<String> getTftpServerDirectories() {
        List<String> allSubDirectoryNames = new ArrayList<String>();
        File rootFolder = new File(TftpInfo.getTftprootpath());
        // 遍历获取其下所有的文件夹路径
        List<File> directories = getAllSubDirectories(rootFolder);
        allSubDirectoryNames.add("/");
        if (directories != null) {
            for (File directory : directories) {
                String pathName = directory.getPath();
                String relativePathName = pathName.substring(TftpInfo.getTftprootpath().length() - 1);
                // 将所有的\转换为/
                relativePathName = relativePathName.replaceAll("\\\\", "/");
                allSubDirectoryNames.add(relativePathName);
            }
        }
        return allSubDirectoryNames;
    }

    /**
     * 获取文件夹下的所有子文件夹
     * 
     * @param file
     * @return
     */
    public List<File> getAllSubDirectories(File file) {
        List<File> allSubFolders = new ArrayList<File>();
        File[] files = file.listFiles();
        if (files != null) {
            for (File file1 : files) {
                if (file1.isDirectory()) {
                    allSubFolders.add(file1);
                    allSubFolders.addAll(getAllSubDirectories(file1));
                }
            }
        }
        return allSubFolders;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.platform.service.TftpServerService#getFileList()
     */
    @Override
    public List<TftpFile> getFileList() {
        List<TftpFile> tftpFiles = new ArrayList<TftpFile>();
        // 获取当前TFTP服务器根路径下的所有文件
        TftpInfo tftpServer = getTftpServerAttr();
        String absoluteRootPath = TftpInfo.getTftprootpath() + tftpServer.getRootPath();
        File rootFolder = new File(absoluteRootPath);
        if (!rootFolder.isDirectory()) {
            rootFolder.mkdirs();
        }
        File[] files = rootFolder.listFiles();
        // 将文件信息转换为TftpFile封装
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (files != null && files.length != 0) {
            for (File file : files) {
                if (file.isFile()) {
                    TftpFile tftpFile = new TftpFile(file.getName(), file.length(), formatter.format(new Date(file
                            .lastModified())));
                    tftpFiles.add(tftpFile);
                }
            }
        }
        return tftpFiles;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.platform.service.TftpServerService#uploadFile(java.io.File,
     * java.lang.String)
     */
    @Override
    public boolean uploadFile(File file, String remoteFileName) {
        File destFile = new File(TftpInfo.getTftprootpath() + getTftpServerAttr().getRootPath() + "/" + remoteFileName);
        return copyFile(file, destFile);
    }

    /**
     * 复制文件
     * 
     * @param sourceFile
     * @param destFile
     * @return
     */
    public boolean copyFile(File sourceFile, File destFile) {
        FileInputStream fis = null;
        FileOutputStream fos = null;
        FileChannel inChannel = null;
        FileChannel outChannel = null;
        try {
            fis = new FileInputStream(sourceFile);
            fos = new FileOutputStream(destFile);
            inChannel = fis.getChannel();
            outChannel = fos.getChannel();
            long size = inChannel.size();
            inChannel.transferTo(0, size, outChannel);
            return true;
        } catch (IOException e) {
            logger.info("I/O exception happened when transfer file!");
            return false;
        } finally {
            try {
                if (inChannel != null)
                    inChannel.close();
                if (outChannel != null)
                    outChannel.close();
                if (fis != null)
                    fis.close();
                if (fos != null)
                    fos.close();
            } catch (IOException e) {
                logger.info("I/O exception happened when transfer file!");
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.platform.service.TftpServerService#downloadFile(java.lang.String,
     * java.lang.String)
     */
    @Override
    public boolean downloadFile(String remoteFileName, String serverFilePathName) {
        String tftpFilePathName = TftpInfo.getTftprootpath() + getTftpServerAttr().getRootPath() + "/" + remoteFileName;
        File tftpFile = new File(tftpFilePathName);
        File serverFile = new File(serverFilePathName);
        if (!tftpFile.exists()) {
            logger.info("Error: " + remoteFileName + " don't exists.");
            return false;
        }
        // If file exists, don't overwrite it.
        if (serverFile.exists()) {
            // 如果存在文件，则直接删除新建
            serverFile.delete();
            serverFile = new File(serverFilePathName);
            // logger.info("Error: " + serverFilePathName + " already exists.");
            // return false;
        }
        return copyFile(tftpFile, serverFile);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.platform.service.TftpServerService#deleteFile(java.lang.String)
     */
    @Override
    public boolean deleteFile(String fileName) {
        // 获取该文件的绝对路径
        TftpInfo tftpServer = getTftpServerAttr();
        String fileAbsolutePath = TftpInfo.getTftprootpath() + tftpServer.getRootPath() + "/" + fileName;
        // 删除该文件
        File fileToDelete = new File(fileAbsolutePath);
        if (fileToDelete.exists() && fileToDelete.isFile()) {
            return fileToDelete.delete();
        } else {
            logger.debug("this file does not exist!");
            return false;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.platform.service.TftpServerService#getAllIpAddress()
     */
    @Override
    public List<String> getAllIpAddress() {
        List<String> ips = new ArrayList<String>();
        ips = LocalhostNetUtil.getInetAddress();
        return ips;
    }

    /**
     * 将TFTP服务器的参数封装成SystemPreferences对象
     * 
     * @param ftpServer
     * @return
     */
    private List<SystemPreferences> server2Prefer(TftpInfo tftpServer) {
        List<SystemPreferences> ftpServerAttrs = new ArrayList<SystemPreferences>();
        SystemPreferences ip = new SystemPreferences();
        ip.setModule("tftpServer");
        ip.setName("ip");
        ip.setValue(tftpServer.getIp());
        ftpServerAttrs.add(ip);

        SystemPreferences port = new SystemPreferences();
        port.setModule("tftpServer");
        port.setName("port");
        port.setValue(String.valueOf(tftpServer.getPort()));
        ftpServerAttrs.add(port);

        SystemPreferences rootPath = new SystemPreferences();
        rootPath.setModule("tftpServer");
        rootPath.setName("rootPath");
        rootPath.setValue(tftpServer.getRootPath());
        ftpServerAttrs.add(rootPath);

        return ftpServerAttrs;
    }

    public void onAfterDownload(InetAddress addr, int port, String fileName, boolean ok) {

    }

    public void onAfterUpload(InetAddress addr, int port, String fileName, boolean ok) {

    }

    public void downloading(InetAddress addr, int port, String fileName, long filesize, long complete) {
        if (logger.isDebugEnabled()) {
            logger.debug("address[" + addr.getHostAddress() + "] port[" + port + "] filename[" + fileName + "] filesize[" + filesize + "] complete[" + complete + "]");
        }
    }

    public void addTftpEventListener(TftpEventListener tftpEventListener) {
        server.addTftpEventListener(tftpEventListener);
    }

    public void removeTftpEventListener(TftpEventListener tftpEventListener) {
        server.removeTftpEventListener(tftpEventListener);
    }

    public SystemPreferencesDao getSystemPreferencesDao() {
        return systemPreferencesDao;
    }

    public void setSystemPreferencesDao(SystemPreferencesDao systemPreferencesDao) {
        this.systemPreferencesDao = systemPreferencesDao;
    }

    public Server getServer() {
        return server;
    }

    public void setServer(Server server) {
        this.server = server;
    }

    public boolean isStarted() {
        return started;
    }

    public void setStarted(boolean started) {
        this.started = started;
    }
}
