/***********************************************************************
 * $Id: FtpClientException.java,v1.0 2013-1-22 上午10:50:03 $
 * 
 * @author: fanzidong
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.platform.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.net.ftp.FTPFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.topvision.framework.service.BaseService;
import com.topvision.platform.SystemConstants;
import com.topvision.platform.dao.SystemPreferencesDao;
import com.topvision.platform.domain.FtpConnectInfo;
import com.topvision.platform.domain.FtpFile;
import com.topvision.platform.domain.SystemPreferences;
import com.topvision.platform.exception.FtpConnectException;
import com.topvision.platform.service.FtpConnectService;
import com.topvision.platform.util.FtpClientUtil;
import com.topvision.platform.util.LocalhostNetUtil;

/**
 * FTP连接管理业务类
 * 
 * @author fanzidong
 * @created @2013-1-22-上午10:50:03
 */
@Service("ftpConnectService")
public class FtpConnectServiceImpl extends BaseService implements FtpConnectService {
    @Autowired
    private SystemPreferencesDao systemPreferencesDao;
    @Value("${FileTemp.Root}")
    private String fileTempRoot;

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.platform.service.FtpConnectService#getFtpConnectAttr()
     */
    @Override
    public FtpConnectInfo getFtpConnectAttr() {
        FtpConnectInfo ftpConnect = new FtpConnectInfo();
        // 取出数据库中数据
        List<SystemPreferences> systemPreferences = systemPreferencesDao.selectByModule("ftpConnect");
        if (systemPreferences != null) {
            for (SystemPreferences systemPreferences2 : systemPreferences) {
                // 拼接出set方法名
                String name = systemPreferences2.getName();
                StringBuffer sb = new StringBuffer();
                sb.append("set");
                sb.append(name.substring(0, 1).toUpperCase());
                sb.append(name.substring(1));

                try {
                    Class<?> objectClass = ftpConnect.getClass();
                    Class<?>[] parameterTypes = new Class[1];
                    Field field = objectClass.getDeclaredField(name);
                    parameterTypes[0] = field.getType();
                    // 获取该方法
                    Method method = objectClass.getMethod(sb.toString(), parameterTypes);
                    // 执行该set方法
                    String type = parameterTypes[0].getName();
                    if (type.equals("java.lang.String")) {
                        method.invoke(ftpConnect, new Object[] { systemPreferences2.getValue() });
                    } else if (type.equals("int")) {
                        method.invoke(ftpConnect, new Object[] { new Integer(systemPreferences2.getValue()) });
                    } else if (type.equals("boolean")) {
                        method.invoke(ftpConnect, new Object[] { new Boolean(systemPreferences2.getValue()) });
                    }
                } catch (Exception e) {
                    logger.debug("there is something wrong with reflact!", e);
                }
            }
        } else {
            logger.debug("failed to get data from database!");
        }
        // 不能使用127.0.0.1(多网卡可能存在隐患，请注意)
        if (ftpConnect.getIp().equals("127.0.0.1")) {
            List<String> ips = getAllIpAddress();
            for (String ip : ips) {
                if (!ip.equals("127.0.0.1")) {
                    ftpConnect.setIp(ip);
                    // 修改数据库中的FTP连接参数的IP地址
                    modiftyFtpConnectAttr(ftpConnect);
                    break;
                }
            }
        }
        return ftpConnect;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.platform.service.FtpConnectService#getFtpConnectStatus()
     */
    @Override
    public FtpConnectInfo getFtpConnectStatus() {
        FtpConnectInfo ftpConnect = getFtpConnectAttr();
        FtpClientUtil ftpClientUtil = new FtpClientUtil();
        File file = null;
        boolean connected = ftpClientUtil.connect(ftpConnect.getIp(), ftpConnect.getPort(), ftpConnect.getUserName(),
                ftpConnect.getPwd());
        ftpConnect.setReachable(connected);
        ftpConnect.setReadable(connected);
        if (connected) {
            // 如果可以连通FTP服务器，则需要测试写入权限
            // 在网管服务器端创建临时文件，写入FTP服务器，根据结果来得出写入权限
            String fileAbsolutePath = System.getProperty("user.dir") + "/tmp/testWriteable.txt";
            file = new File(fileAbsolutePath);
            try {
                file.createNewFile();
            } catch (IOException e) {
                logger.info("failed to create file");
                ftpConnect.setWriteable(false);
                return ftpConnect;
            }
            boolean isWriteable = ftpClientUtil.uploadFile(file, "testWriteable.txt");
            ftpConnect.setWriteable(isWriteable);
            ftpClientUtil.deleteFile("testWriteable.txt", 0);
        }
        ftpClientUtil.disconnect();
        if (file != null) {
            file.delete();
        }
        return ftpConnect;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.platform.service.FtpConnectService#modiftyFtpConnectAttr
     * (com.topvision.platform.domain.FtpConnectInfo)
     */
    @Override
    public void modiftyFtpConnectAttr(FtpConnectInfo ftpConnect) {
        List<SystemPreferences> ftpConnectAttrs = connect2Prefer(ftpConnect);
        systemPreferencesDao.updateEntity(ftpConnectAttrs);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.platform.service.FtpConnectService#getWorkingDirectory(
     * com.topvision.platform.domain.FtpConnectInfo)
     */
    @Override
    public String getWorkingDirectory(String folderName) throws FtpConnectException {
        FtpConnectInfo ftpConnect = getFtpConnectAttr();
        ftpConnect.setRemotePath(folderName);
        String currentPath = null;
        FtpClientUtil ftpClientUtil = new FtpClientUtil();
        boolean connected = ftpClientUtil.connect(ftpConnect.getIp(), ftpConnect.getPort(), ftpConnect.getUserName(),
                ftpConnect.getPwd());
        if (connected) {
            try {
                ftpClientUtil.fTPClient.changeWorkingDirectory(new String(ftpConnect.getRemotePath().getBytes(
                        FtpClientUtil.UTF8), FtpClientUtil.ISO));
                currentPath = new String(ftpClientUtil.fTPClient.printWorkingDirectory().getBytes(FtpClientUtil.ISO),
                        FtpClientUtil.UTF8);
            } catch (UnsupportedEncodingException e) {
                throw new FtpConnectException("the encodingType is not supported", e);
            } catch (IOException e) {
                throw new FtpConnectException("failed to getWorkingDirectory", e);
            }
        }
        ftpClientUtil.disconnect();
        return currentPath;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.platform.service.FtpConnectService#getFileList(com.topvision
     * .platform.domain.FtpConnectInfo)
     */
    @Override
    public List<FtpFile> getFileList(String folderName) throws FtpConnectException {
        FtpConnectInfo ftpConnect = getFtpConnectAttr();
        ftpConnect.setRemotePath(folderName);
        List<FtpFile> ftpFiles = null;
        FtpClientUtil ftpClientUtil = new FtpClientUtil();
        boolean connected = ftpClientUtil.connect(ftpConnect.getIp(), ftpConnect.getPort(), ftpConnect.getUserName(),
                ftpConnect.getPwd());
        if (connected) {
            String currentRemotePath = "";
            try {
                ftpClientUtil.fTPClient.changeWorkingDirectory(new String(ftpConnect.getRemotePath().getBytes(
                        FtpClientUtil.UTF8), FtpClientUtil.ISO));
                currentRemotePath = new String(ftpClientUtil.fTPClient.printWorkingDirectory().getBytes(
                        FtpClientUtil.ISO), FtpClientUtil.UTF8);
                FTPFile[] fTPFiles = null;
                fTPFiles = ftpClientUtil.fTPClient.listFiles();
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                // 封装成FtpFile类
                ftpFiles = new ArrayList<FtpFile>();
                if (fTPFiles != null) {
                    for (FTPFile fTPFile : fTPFiles) {
                        FtpFile ftpFile = new FtpFile();
                        ftpFile.setName(new String(fTPFile.getName().getBytes("iso-8859-1"), "UTF-8"));
                        ftpFile.setPathName(currentRemotePath);
                        ftpFile.setType(fTPFile.getType());
                        ftpFile.setSize(fTPFile.getSize());
                        ftpFile.setUpdateTime(formatter.format(fTPFile.getTimestamp().getTime()));
                        ftpFiles.add(ftpFile);
                    }
                }
            } catch (UnsupportedEncodingException e) {
                throw new FtpConnectException("the encodingType is not supported");
            } catch (IOException e) {
                throw new FtpConnectException("an I/O exception has occurred");
            } finally {
                ftpClientUtil.disconnect();
            }
        } else {
            ftpClientUtil.disconnect();
        }
        return ftpFiles;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.platform.service.FtpConnectService#uploadFile(com.topvision
     * .platform.domain.FtpConnectInfo, java.io.File, java.lang.String)
     */
    @Override
    public boolean uploadFile(File file, String remoteFilePathName) {
        FtpConnectInfo ftpConnect = getFtpConnectAttr();
        boolean result = false;
        FtpClientUtil ftpClientUtil = new FtpClientUtil();
        boolean connected = ftpClientUtil.connect(ftpConnect.getIp(), ftpConnect.getPort(), ftpConnect.getUserName(),
                ftpConnect.getPwd());
        if (connected) {
            result = ftpClientUtil.uploadFile(file, remoteFilePathName);
        }
        ftpClientUtil.disconnect();
        return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.platform.service.FtpConnectService#downloadFile(com.topvision
     * .platform.domain.FtpConnectInfo, java.lang.String)
     */
    @Override
    public File downloadFile(String ftpFileName, String serverFileName) {
        File file = null;

        Properties prop = new Properties();
        InputStream in;
        try {
            in = new FileInputStream(new File(SystemConstants.WEB_INF_REAL_PATH + "/conf/ftp.properties"));
            prop.load(in);
        } catch (FileNotFoundException e) {
            logger.error("", e);
            return null;
        } catch (IOException e) {
            logger.error("", e);
            return null;
        }

        FtpConnectInfo ftpConnect = getFtpConnectAttr();
        FtpClientUtil ftpClientUtil = new FtpClientUtil();
        boolean connected = ftpClientUtil.connect(ftpConnect.getIp(), ftpConnect.getPort(), ftpConnect.getUserName(),
                ftpConnect.getPwd());
        try {
            if (connected) {
                StringBuilder sb = new StringBuilder();
                sb.append(SystemConstants.ROOT_REAL_PATH);
                sb.append(fileTempRoot);
                sb.append("/");
                sb.append(serverFileName);
                boolean result = ftpClientUtil.downloadFile(ftpFileName, sb.toString());
                if (result) {
                    file = new File(sb.toString());
                }

            }
        } finally {
            /**add by bravin@20160331:建立的连接需要关闭*/
            ftpClientUtil.disconnect();
        }
        return file;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.platform.service.FtpConnectService#deleteFile(com.topvision
     * .platform.domain.FtpConnectInfo, java.lang.String, int)
     */
    @Override
    public boolean deleteFile(String fileFullName, int fileType) {
        FtpConnectInfo ftpConnect = getFtpConnectAttr();
        boolean result = false;
        FtpClientUtil ftpClientUtil = new FtpClientUtil();
        boolean connected = ftpClientUtil.connect(ftpConnect.getIp(), ftpConnect.getPort(), ftpConnect.getUserName(),
                ftpConnect.getPwd());
        if (connected) {
            result = ftpClientUtil.deleteFile(fileFullName, fileType);
        }
        ftpClientUtil.disconnect();
        return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.platform.service.FtpConnectService#createFolder(com.topvision
     * .platform.domain.FtpConnectInfo, java.lang.String)
     */
    @Override
    public boolean createFolder(String folerFullName) {
        FtpConnectInfo ftpConnect = getFtpConnectAttr();
        FtpClientUtil ftpClientUtil = new FtpClientUtil();
        boolean result = false;
        boolean connected = ftpClientUtil.connect(ftpConnect.getIp(), ftpConnect.getPort(), ftpConnect.getUserName(),
                ftpConnect.getPwd());
        if (connected) {
            result = ftpClientUtil.createSubDirectory(ftpConnect.getRemotePath(), folerFullName);
        }
        ftpClientUtil.disconnect();
        return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.platform.service.FtpServerService#getAllIpAddress()
     */
    @Override
    public List<String> getAllIpAddress() {
        List<String> ips = new ArrayList<String>();
        ips = LocalhostNetUtil.getInetAddress();
        return ips;
    }

    @Override
    public boolean isFileExist(String filePathName) {
        boolean result = false;
        // 从filePathName中得出文件夹folderName及文件名称fileName
        String folderName = "";
        String fileName = "";
        try {
            if (filePathName.indexOf("/") != -1) {
                Pattern pattern = Pattern.compile("(.*/)(.*)");
                Matcher matcher = pattern.matcher(filePathName);
                while (matcher.find()) {
                    folderName = matcher.group(1);
                    fileName = matcher.group(2);
                }
            } else {
                fileName = filePathName;
            }
            List<FtpFile> ftpFiles = getFileList(folderName);
            // 遍历该目录下的所有文件，查看是否有同名文件的存在
            for (FtpFile ftpFile : ftpFiles) {
                if (ftpFile.getName().equals(fileName)) {
                    result = true;
                    break;
                }
            }
        } catch (Exception e) {
            logger.info("call isFileExist {0}", e);
            result = false;
        }
        return result;
    }

    private List<SystemPreferences> connect2Prefer(FtpConnectInfo ftpConnect) {
        List<SystemPreferences> ftpConnectAttrs = new ArrayList<SystemPreferences>();
        SystemPreferences ip = new SystemPreferences();
        ip.setModule("ftpConnect");
        ip.setName("ip");
        ip.setValue(ftpConnect.getIp());
        ftpConnectAttrs.add(ip);

        SystemPreferences port = new SystemPreferences();
        port.setModule("ftpConnect");
        port.setName("port");
        port.setValue(String.valueOf(ftpConnect.getPort()));
        ftpConnectAttrs.add(port);

        SystemPreferences userName = new SystemPreferences();
        userName.setModule("ftpConnect");
        userName.setName("userName");
        userName.setValue(ftpConnect.getUserName());
        ftpConnectAttrs.add(userName);

        SystemPreferences pwd = new SystemPreferences();
        pwd.setModule("ftpConnect");
        pwd.setName("pwd");
        pwd.setValue(ftpConnect.getPwd());
        ftpConnectAttrs.add(pwd);

        SystemPreferences remotePath = new SystemPreferences();
        remotePath.setModule("ftpConnect");
        remotePath.setName("remotePath");
        remotePath.setValue(ftpConnect.getRemotePath());
        ftpConnectAttrs.add(remotePath);

        return ftpConnectAttrs;
    }

    public SystemPreferencesDao getSystemPreferencesDao() {
        return systemPreferencesDao;
    }

    public void setSystemPreferencesDao(SystemPreferencesDao systemPreferencesDao) {
        this.systemPreferencesDao = systemPreferencesDao;
    }

}
