/***********************************************************************
 * $Id: TftpClientServiceImpl.java,v1.0 2014-1-21 上午10:50:03 $
 * 
 * @author: fanzidong
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/

package com.topvision.platform.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.net.tftp.TFTP;
import org.apache.commons.net.tftp.TFTPClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.framework.common.FileUtils;
import com.topvision.framework.service.BaseService;
import com.topvision.platform.SystemConstants;
import com.topvision.platform.dao.SystemPreferencesDao;
import com.topvision.platform.domain.SystemPreferences;
import com.topvision.platform.domain.TftpClientInfo;
import com.topvision.platform.domain.TftpInfo;
import com.topvision.platform.service.TftpClientService;
import com.topvision.platform.service.TftpServerService;
import com.topvision.platform.util.LocalhostNetUtil;

@Service("tftpClientService")
public class TftpClientServiceImpl extends BaseService implements TftpClientService {
    @Autowired
    private SystemPreferencesDao systemPreferencesDao;

    public static final String TFTP_ROOT_PATH = SystemConstants.ROOT_REAL_PATH + File.separator + "META-INF"
            + File.separator + "tftpTemp";
    public static final String HUBPATH = TFTP_ROOT_PATH + File.separator + "Hub";
    @Autowired
    private TftpServerService tftpServerService;

    @Override
    public TftpClientInfo getTftpClientInfo() {
        // 取出数据库中数据，转换为ftpServer对象
        List<SystemPreferences> systemPreferences = systemPreferencesDao.selectByModule("tftpClient");
        if (systemPreferences == null) {
            logger.debug("failed to get data from database!");
            return null;
        }
        TftpClientInfo tftpClientInfo = new TftpClientInfo();
        for (SystemPreferences record : systemPreferences) {
            String recordName = record.getName();
            if ("ip".equals(recordName)) {
                tftpClientInfo.setIp(record.getValue());
            } else if ("port".equals(recordName)) {
                tftpClientInfo.setPort(Integer.valueOf(record.getValue()));
            }
        }
        // 不能使用127.0.0.1
        if (tftpClientInfo.getIp().equals("127.0.0.1")) {
            List<String> ips = LocalhostNetUtil.getInetAddress();
            for (String ip : ips) {
                if (!ip.equals("127.0.0.1")) {
                    tftpClientInfo.setIp(ip);
                    // 修改数据库中的TFTP连接参数的IP地址
                    modifyTftpClient(tftpClientInfo);
                    break;
                }
            }
        }
        return tftpClientInfo;
    }

    @Override
    public void modifyTftpClient(TftpClientInfo tftpClientInfo) {
        List<SystemPreferences> tftpClientAttrs = new ArrayList<SystemPreferences>();
        SystemPreferences preferenceIp = new SystemPreferences();
        preferenceIp.setModule("tftpClient");
        preferenceIp.setName("ip");
        preferenceIp.setValue(tftpClientInfo.getIp());
        tftpClientAttrs.add(preferenceIp);

        SystemPreferences preferencePort = new SystemPreferences();
        preferencePort.setModule("tftpClient");
        preferencePort.setName("port");
        preferencePort.setValue(tftpClientInfo.getPort().toString());
        tftpClientAttrs.add(preferencePort);
        systemPreferencesDao.updateEntity(tftpClientAttrs);
    }

    @Override
    public Boolean downloadFile(String remoteFileName) {
        // 读取TFTP连接的配置
        TftpClientInfo tftpClientInfo = getTftpClientInfo();
        TFTPClient tftp = new TFTPClient();
        // 设置超时时间
        tftp.setDefaultTimeout(30000);
        // 打开本地UDP的socket
        try {
            tftp.open();
        } catch (SocketException e) {
            logger.debug("Error: could not open local UDP socket.");
            return false;
        }
        // 确保临时存放文件夹存在
        File folder = new File(HUBPATH);
        if (!folder.exists()) {
            folder.mkdirs();
        }
        FileOutputStream output = null;
        File file = new File(HUBPATH + File.separator + remoteFileName);
        // 如果文件已经存在，则覆盖它
        if (file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                logger.error("", e);
                return false;
            }
        }
        // 打开本地文件为了存储数据
        try {
            output = new FileOutputStream(file);
        } catch (IOException e) {
            tftp.close();
            logger.debug("Error: could not open local file for writing.");
            return false;
        }
        // 尝试通过TFTP协议接收文件
        try {
            tftp.receiveFile(remoteFileName, TFTP.BINARY_MODE, output, tftpClientInfo.getIp(),
                    tftpClientInfo.getPort());
        } catch (UnknownHostException e) {
            logger.debug("Error: could not resolve hostname.");
            return false;
        } catch (IOException e) {
            logger.debug("Error: I/O exception occurred while receiving file.");
            logger.debug(e.getMessage());
            return false;
        } finally {
            // 关闭socket和本地文件
            tftp.close();
            try {
                if (output != null) {
                    output.close();
                }
            } catch (IOException e) {
                logger.debug("Error: error closing file.");
                return false;
            }
        }
        return true;
    }

    @Override
    public Boolean uploadFile(File file, String remoteFileName) {
        // 读取TFTP连接的配置
        TftpClientInfo tftpClientInfo = getTftpClientInfo();
        TFTPClient tftp = new TFTPClient();
        // 设置超时时间
        tftp.setDefaultTimeout(30000);
        // 打开本地UDP的socket
        try {
            tftp.open();
        } catch (SocketException e) {
            logger.debug("Error: could not open local UDP socket.");
            return false;
        }
        FileInputStream input = null;
        // 打开文件为了传输
        try {
            input = new FileInputStream(file);
        } catch (IOException e) {
            tftp.close();
            logger.debug("Error: could not open local file for reading.");
            return false;
        }
        // 开始传输文件
        try {
            tftp.sendFile(remoteFileName, TFTP.ASCII_MODE, input, tftpClientInfo.getIp(), tftpClientInfo.getPort());
        } catch (UnknownHostException e) {
            logger.debug("Error: could not resolve hostname.");
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            logger.debug("Error: I/O exception occurred while sending file." + e);
            return false;
        } finally {
            // 关闭socket和本地文件
            tftp.close();
            try {
                if (input != null) {
                    input.close();
                }
            } catch (IOException e) {
                logger.debug("Error: error closing file.");
                return false;
            }
        }
        return true;
    }

    @Override
    public Boolean sendFileToInnerServer(File file, String remoteFileName) {
        try {
            TftpInfo tftpServerInfo = tftpServerService.getTftpServerAttr();
            File destFile = new File(tftpServerInfo.getRealPath() + remoteFileName);
            FileUtils.copy(file, destFile);
            return true;
        } catch (IOException e) {
            logger.debug("cannot copy file to inner tftp server", e);
            return false;
        }
    }

}
