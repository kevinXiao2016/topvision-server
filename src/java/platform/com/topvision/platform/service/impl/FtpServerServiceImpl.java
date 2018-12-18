/***********************************************************************
 * $Id: FtpClientException.java,v1.0 2013-1-22 上午10:50:03 $
 * 
 * @author: fanzidong
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.platform.service.impl;

import java.beans.PropertyDescriptor;
import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.apache.ftpserver.ConnectionConfigFactory;
import org.apache.ftpserver.FtpServer;
import org.apache.ftpserver.FtpServerFactory;
import org.apache.ftpserver.ftplet.Authentication;
import org.apache.ftpserver.ftplet.AuthenticationFailedException;
import org.apache.ftpserver.ftplet.Authority;
import org.apache.ftpserver.ftplet.FtpException;
import org.apache.ftpserver.ftplet.User;
import org.apache.ftpserver.ftplet.UserManager;
import org.apache.ftpserver.listener.ListenerFactory;
import org.apache.ftpserver.usermanager.AnonymousAuthentication;
import org.apache.ftpserver.usermanager.UsernamePasswordAuthentication;
import org.apache.ftpserver.usermanager.impl.BaseUser;
import org.apache.ftpserver.usermanager.impl.ConcurrentLoginPermission;
import org.apache.ftpserver.usermanager.impl.TransferRatePermission;
import org.apache.ftpserver.usermanager.impl.WritePermission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.framework.service.BaseService;
import com.topvision.platform.SystemConstants;
import com.topvision.platform.dao.SystemPreferencesDao;
import com.topvision.platform.domain.FtpServerInfo;
import com.topvision.platform.domain.SystemPreferences;
import com.topvision.platform.service.FtpServerService;
import com.topvision.platform.util.FtpClientUtil;
import com.topvision.platform.util.LocalhostNetUtil;

/**
 * FTP服务器管理业务类
 * 
 * @author fanzidong
 * @created @2013-1-22-上午10:50:03
 */
@Service("ftpServerService")
public class FtpServerServiceImpl extends BaseService implements FtpServerService {
    @Autowired
    private SystemPreferencesDao systemPreferencesDao;
    // 创建FTP服务器时的根路径
    public static final String FTPROOTPATH = SystemConstants.ROOT_REAL_PATH + "/META-INF/ftpTemp";

    private FtpServer apacheFtpServer;
    private BaseUser user;
    private BaseUser adminUser;
    private boolean started;

    @PostConstruct
    public void initialize() {
        try {
            FtpServerInfo ftpServer = getFtpServerAttr();
            if (ftpServer != null && ftpServer.getEnable() == FtpServerInfo.ON) {
                startFtpServer();
            }
        } catch (Exception e) {
            logger.debug("init wrong!");
        }
    }

    @PreDestroy
    public void destroy() {
        if (apacheFtpServer != null && !apacheFtpServer.isStopped()) {
            apacheFtpServer.stop();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.platform.service.FtpServerService#getFtpServerAttr()
     */
    @Override
    public FtpServerInfo getFtpServerAttr() {
        // 取出数据库中数据，转换为ftpServer对象
        List<SystemPreferences> systemPreferences = systemPreferencesDao.selectByModule("ftpServer");
        if (systemPreferences != null) {
            FtpServerInfo ftpServer = new FtpServerInfo();
            for (SystemPreferences systemPreferences2 : systemPreferences) {
                try {
                    PropertyDescriptor pd = new PropertyDescriptor(systemPreferences2.getName(), ftpServer.getClass());
                    Method setMethod = pd.getWriteMethod();
                    String typeName = pd.getPropertyType().getName();
                    if (typeName.equals("java.lang.String")) {
                        setMethod.invoke(ftpServer, systemPreferences2.getValue());
                    } else if (typeName.equals("int")) {
                        setMethod.invoke(ftpServer, Integer.valueOf(systemPreferences2.getValue()));
                    } else if (typeName.equals("boolean")) {
                        setMethod.invoke(ftpServer, Boolean.valueOf(systemPreferences2.getValue()));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }
            // 将IP地址转换成网卡地址，不能使用127.0.0.1(多网卡可能存在隐患，请注意)
            if ("127.0.0.1".equals(ftpServer.getIp())) {
                List<String> ips = getAllIpAddress();
                for (String ip : ips) {
                    if (!ip.equals("127.0.0.1")) {
                        ftpServer.setIp(ip);
                        // 修改数据库中的FTP服务器参数的IP地址
                        modifyFtpServerAttr(ftpServer);
                        break;
                    }
                }
            }
            return ftpServer;
        } else {
            logger.debug("failed to get data from database!");
            return null;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.platform.service.FtpServerService#getFtpServerStatus(com
     * .topvision.platform.domain.FtpServerInfo)
     */
    @Override
    public FtpServerInfo getFtpServerStatus() {
        FtpServerInfo ftpServer = getFtpServerAttr();
        if (ftpServer != null) {
            ftpServer.setStarted(started);
            ftpServer.setReachable(false);
            // 如果FTP服务器是启动的，则测试FTP服务器的可达性，否则肯定不可达
            if (started) {
                FtpClientUtil ftpClientUtil = new FtpClientUtil();
                boolean isConnectable = ftpClientUtil.connect(ftpServer.getIp(), ftpServer.getPort(),
                        ftpServer.getUserName(), ftpServer.getPwd());
                ftpServer.setReachable(isConnectable);
                ftpClientUtil.disconnect();
            }
            return ftpServer;
        } else {
            return null;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.platform.service.FtpServerService#modifyFtpServerAttr(com
     * .topvision.platform.domain.FtpServerInfo)
     */
    @Override
    public void modifyFtpServerAttr(FtpServerInfo ftpServer) {
        // 将ftpServer中的属性封装成List<SystemPreferences>
        List<SystemPreferences> ftpServerAttrs = server2Prefer(ftpServer);
        systemPreferencesDao.updateEntity(ftpServerAttrs);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.platform.service.FtpServerService#startFtpServer(com.topvision
     * .platform.domain.FtpServerInfo)
     */
    @Override
    public boolean startFtpServer() {
        FtpServerInfo ftpServer = getFtpServerAttr();
        if (ftpServer == null) {
            if (logger.isDebugEnabled()) {
                logger.debug("get ftp service attribute faild.!");
            }
            return false;
        }
        int port = ftpServer.getPort();
        boolean isPortUsed = LocalhostNetUtil.checkTcpPortUsage(port);
        if (isPortUsed) {
            if (logger.isDebugEnabled()) {
                logger.debug("TCP port:" + ftpServer.getPort() + " has been used!");
            }
            return false;
        } else {
            if (logger.isDebugEnabled()) {
                logger.debug("TCP port:" + ftpServer.getPort() + " has not been used!");
            }
        }
        // 判断内置FTP服务对象是否已存在，不存在则创建
        if (apacheFtpServer == null && ftpServer != null) {
            // 检查端口是否被占用
            // 分别创建ftpServer和listener，并为ftpServer添加端口监听器
            FtpServerFactory serverFactory = new FtpServerFactory();
            ListenerFactory factory = new ListenerFactory();
            factory.setPort(port);
            serverFactory.addListener("default", factory.createListener());
            // 设置serverFactory的连接配置，可进行匿名登录
            ConnectionConfigFactory ccFactory = new ConnectionConfigFactory();
            ccFactory.setAnonymousLoginEnabled(true);
            serverFactory.setConnectionConfig(ccFactory.createConnectionConfig());
            // 根据内置FTP的配置信息创建用户对象
            user = new BaseUser();
            user.setName(ftpServer.getUserName());
            user.setPassword(ftpServer.getPwd());
            // 如果赋给该用户的根目录不存在，则创建
            String userRootPath = FTPROOTPATH + ftpServer.getRootPath();
            File userRootFolder = new File(userRootPath);
            if (!userRootFolder.isDirectory()) {
                if (!userRootFolder.mkdirs()) {
                    logger.info("failed to create the root path!");
                    return false;
                }
            }
            user.setHomeDirectory(userRootPath);
            user.setEnabled(true);
            // 配置授权信息，并授权给用户
            List<Authority> authorities = new ArrayList<Authority>();
            if (ftpServer.isWriteable()) {
                authorities.add(new WritePermission());
            }
            int maxLogin = 0;
            int maxLoginPerIP = 0;
            authorities.add(new ConcurrentLoginPermission(maxLogin, maxLoginPerIP));
            int uploadRate = 0;
            int downloadRate = 0;
            authorities.add(new TransferRatePermission(downloadRate, uploadRate));
            user.setAuthorities(authorities);
            // 设置用户可以一直不做操作而不被收回权限
            user.setMaxIdleTime(0);

            adminUser = new BaseUser();
            adminUser.setName("admin");
            adminUser.setPassword("admin");
            adminUser.setHomeDirectory(userRootPath);
            adminUser.setEnabled(false);
            // 配置授权信息，并授权给用户
            authorities = new ArrayList<Authority>();
            authorities.add(new WritePermission());
            authorities.add(new ConcurrentLoginPermission(maxLogin, maxLoginPerIP));
            authorities.add(new TransferRatePermission(downloadRate, uploadRate));
            adminUser.setAuthorities(authorities);
            // 设置用户可以一直不做操作而不被收回权限
            adminUser.setMaxIdleTime(0);

            apacheFtpServer = serverFactory.createServer();
            serverFactory.setUserManager(new InnerUserManager());
        }
        // 启动内置FTP
        try {
            if (apacheFtpServer != null) {
                apacheFtpServer.start();
                setStarted(true);
                SystemPreferences sp = new SystemPreferences();
                sp.setName("enable");
                sp.setValue("1");
                sp.setModule("ftpServer");
                systemPreferencesDao.updateEntity(sp);
                return true;
            } else {
                return false;
            }
        } catch (FtpException e) {
            logger.info("ftp can't be started:{}");
            setStarted(false);
            return false;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.platform.service.FtpServerService#stopFtpServer(com.topvision
     * .platform.domain.FtpServerInfo)
     */
    @Override
    public void stopFtpServer() {
        if (apacheFtpServer != null && !apacheFtpServer.isStopped()) {
            apacheFtpServer.stop();
            apacheFtpServer = null;
            setStarted(false);
            SystemPreferences sp = new SystemPreferences();
            sp.setName("enable");
            sp.setValue("0");
            sp.setModule("ftpServer");
            systemPreferencesDao.updateEntity(sp);
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.platform.service.FtpServerService#reStartFtpServer(com.
     * topvision.platform.domain.FtpServerInfo)
     */
    @Override
    public boolean reStartFtpServer() {
        stopFtpServer();
        boolean result = startFtpServer();
        return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.platform.service.FtpServerService#getFtpServerDirectories()
     */
    @Override
    public List<String> getFtpServerDirectories() {
        List<String> allSubDirectoryNames = new ArrayList<String>();
        File rootFolder = new File(FTPROOTPATH);
        // 遍历获取其下所有的文件夹路径
        List<File> directories = getAllSubDirectories(rootFolder);
        allSubDirectoryNames.add("/");
        if (directories != null) {
            for (File directory : directories) {
                String pathName = directory.getPath();
                String relativePathName = pathName.substring(FTPROOTPATH.length() - 1);
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
     * @see com.topvision.platform.service.FtpServerService#getAllIpAddress()
     */
    @Override
    public List<String> getAllIpAddress() {
        List<String> ips = new ArrayList<String>();
        ips = LocalhostNetUtil.getInetAddress();
        return ips;
    }

    /**
     * 将FTP服务器的参数封装成SystemPreferences对象
     * 
     * @param ftpServer
     * @return
     */
    private List<SystemPreferences> server2Prefer(FtpServerInfo ftpServer) {
        List<SystemPreferences> ftpServerAttrs = new ArrayList<SystemPreferences>();
        SystemPreferences ip = new SystemPreferences();
        ip.setModule("ftpServer");
        ip.setName("ip");
        ip.setValue(ftpServer.getIp());
        ftpServerAttrs.add(ip);

        SystemPreferences port = new SystemPreferences();
        port.setModule("ftpServer");
        port.setName("port");
        port.setValue(String.valueOf(ftpServer.getPort()));
        ftpServerAttrs.add(port);

        SystemPreferences userName = new SystemPreferences();
        userName.setModule("ftpServer");
        userName.setName("userName");
        userName.setValue(ftpServer.getUserName());
        ftpServerAttrs.add(userName);

        SystemPreferences pwd = new SystemPreferences();
        pwd.setModule("ftpServer");
        pwd.setName("pwd");
        pwd.setValue(ftpServer.getPwd());
        ftpServerAttrs.add(pwd);

        SystemPreferences rootPath = new SystemPreferences();
        rootPath.setModule("ftpServer");
        rootPath.setName("rootPath");
        rootPath.setValue(ftpServer.getRootPath());
        ftpServerAttrs.add(rootPath);

        SystemPreferences writeable = new SystemPreferences();
        writeable.setModule("ftpServer");
        writeable.setName("writeable");
        writeable.setValue(String.valueOf(ftpServer.isWriteable()));
        ftpServerAttrs.add(writeable);
        return ftpServerAttrs;
    }

    /**
     * 内部用户管理实现，此实现仅支持注入的用户名，如果需要多用户可以修改注入List<User>的方式。
     * 
     * @author Victor
     * @created @2011-11-13-下午03:21:40
     * 
     */
    class InnerUserManager implements UserManager {
        /*
         * (non-Javadoc)
         * 
         * @see org.apache.ftpserver.ftplet.UserManager#authenticate(org.apache.ftpserver .ftplet.
         * Authentication)
         */
        @Override
        public User authenticate(Authentication auth) throws AuthenticationFailedException {
            if (auth instanceof UsernamePasswordAuthentication) {
                UsernamePasswordAuthentication upauth = (UsernamePasswordAuthentication) auth;

                String login = upauth.getUsername();
                String password = upauth.getPassword();

                if (login == null) {
                    throw new AuthenticationFailedException("Authentication failed");
                }

                if (password == null) {
                    password = "";
                }
                if (user.getName().equalsIgnoreCase(login) && password.equals(user.getPassword())) {
                    return user;
                } else if (adminUser.getEnabled() && adminUser.getName().equalsIgnoreCase(login)
                        && password.equals(adminUser.getPassword())) {
                    return adminUser;
                } else {
                    return null;
                }
            } else if (auth instanceof AnonymousAuthentication) {
                throw new AuthenticationFailedException("Anonymous forbidden.");
            } else {
                throw new IllegalArgumentException("Authentication not supported by this user manager");
            }
        }

        /*
         * (non-Javadoc)
         * 
         * @see org.apache.ftpserver.ftplet.UserManager#delete(java.lang.String)
         */
        @Override
        public void delete(String name) throws FtpException {
            throw new FtpException("Cann't delete the user " + name);
        }

        /*
         * (non-Javadoc)
         * 
         * @see org.apache.ftpserver.ftplet.UserManager#doesExist(java.lang.String)
         */
        @Override
        public boolean doesExist(String name) throws FtpException {
            return getUserByName(name) != null;
        }

        /*
         * (non-Javadoc)
         * 
         * @see org.apache.ftpserver.ftplet.UserManager#getAdminName()
         */
        @Override
        public String getAdminName() throws FtpException {
            return "admin";
        }

        /*
         * (non-Javadoc)
         * 
         * @see org.apache.ftpserver.ftplet.UserManager#getAllUserNames()
         */
        @Override
        public String[] getAllUserNames() throws FtpException {
            if (getFtpServerAttr() != null) {
                return new String[] { getFtpServerAttr().getUserName() };
            } else {
                return null;
            }
        }

        /*
         * (non-Javadoc)
         * 
         * @see org.apache.ftpserver.ftplet.UserManager#getUserByName(java.lang.String )
         */
        @Override
        public User getUserByName(String name) throws FtpException {
            if (user.getName().equalsIgnoreCase(name)) {
                return user;
            } else if (adminUser.getEnabled() && adminUser.getName().equalsIgnoreCase(name)) {
                return adminUser;
            } else {
                return null;
            }
        }

        /*
         * (non-Javadoc)
         * 
         * @see org.apache.ftpserver.ftplet.UserManager#isAdmin(java.lang.String)
         */
        @Override
        public boolean isAdmin(String name) throws FtpException {
            return name.equals("admin");
        }

        /*
         * (non-Javadoc)
         * 
         * @see org.apache.ftpserver.ftplet.UserManager#save(org.apache.ftpserver .ftplet.User)
         */
        @Override
        public void save(User user) throws FtpException {
            throw new FtpException("Cann't save the user.");
        }
    }

    public SystemPreferencesDao getSystemPreferencesDao() {
        return systemPreferencesDao;
    }

    public void setSystemPreferencesDao(SystemPreferencesDao systemPreferencesDao) {
        this.systemPreferencesDao = systemPreferencesDao;
    }

    public FtpServer getApacheFtpServer() {
        return apacheFtpServer;
    }

    public void setApacheFtpServer(FtpServer apacheFtpServer) {
        this.apacheFtpServer = apacheFtpServer;
    }

    public BaseUser getUser() {
        return user;
    }

    public BaseUser getAdminUser() {
        return adminUser;
    }

    public void setUser(BaseUser user) {
        this.user = user;
    }

    public boolean isStarted() {
        return started;
    }

    public void setStarted(boolean started) {
        this.started = started;
    }

    public static String getFtprootpath() {
        return FTPROOTPATH;
    }
}
