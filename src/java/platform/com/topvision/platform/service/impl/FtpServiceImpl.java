/***********************************************************************
 * $Id: FtpService.java,v1.0 2011-10-8 下午04:09:41 $
 * 
 * @author: huqiao
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.platform.service.impl;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;

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

import com.topvision.framework.service.BaseService;
import com.topvision.platform.SystemConstants;
import com.topvision.platform.service.FtpService;

/**
 * @author huqiao
 * @created @2011-10-8-下午04:09:41
 * 
 */
public class FtpServiceImpl extends BaseService implements FtpService {
    private FtpServer server;
    private int port;
    private String userName;
    private String passWord;
    private String ftproot;
    private BaseUser user;
    private boolean ftpEnable;

    public void initialize() {
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            serverSocket.close();
            FtpServerFactory serverFactory = new FtpServerFactory();
            ListenerFactory factory = new ListenerFactory();
            factory.setPort(port);
            serverFactory.addListener("default", factory.createListener());
            ConnectionConfigFactory ccFactory = new ConnectionConfigFactory();
            ccFactory.setAnonymousLoginEnabled(true);
            serverFactory.setConnectionConfig(ccFactory.createConnectionConfig());
            user = new BaseUser();
            user.setName(userName);
            user.setPassword(passWord);
            user.setHomeDirectory(SystemConstants.ROOT_REAL_PATH + ftproot);
            user.setEnabled(true);
            List<Authority> authorities = new ArrayList<Authority>();
            authorities.add(new WritePermission());
            int maxLogin = 0;
            int maxLoginPerIP = 0;
            authorities.add(new ConcurrentLoginPermission(maxLogin, maxLoginPerIP));
            int uploadRate = 0;
            int downloadRate = 0;  
            authorities.add(new TransferRatePermission(downloadRate, uploadRate));
            user.setAuthorities(authorities);
            user.setMaxIdleTime(0);
            server = serverFactory.createServer();
            serverFactory.setUserManager(new InnerUserManager());
            try {
                server.start();
                setFtpEnable(true);
            } catch (FtpException e) {
                logger.debug("ftp method is error:{}", e);
                setFtpEnable(false);
            }
        } catch (IOException e) {
            logger.info("FtpServer can't be initialized");
            setFtpEnable(false);
        }
    }

    /**
     * @return the ftpEnable
     */
    public boolean isFtpEnable() {
        return ftpEnable;
    }

    /**
     * @param ftpEnable
     *            the ftpEnable to set
     */
    public void setFtpEnable(boolean ftpEnable) {
        this.ftpEnable = ftpEnable;
    }

    public void destroy() {
        if (!server.isStopped()) {
            server.stop();
        }
    }

    /**
     * @return the server
     */
    public FtpServer getServer() {
        return server;
    }

    /**
     * @param server
     *            the server to set
     */
    public void setServer(FtpServer server) {
        this.server = server;
    }

    /**
     * @return the port
     */
    public int getPort() {
        return port;
    }

    /**
     * @param port
     *            the port to set
     */
    public void setPort(int port) {
        this.port = port;
    }

    /**
     * @return the userName
     */
    public String getUserName() {
        return userName;
    }

    /**
     * @param userName
     *            the userName to set
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * @return the passWord
     */
    public String getPassWord() {
        return passWord;
    }

    /**
     * @param passWord
     *            the passWord to set
     */
    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    /**
     * @return the ftproot
     */
    public String getFtproot() {
        return ftproot;
    }

    /**
     * @param ftproot
     *            the ftproot to set
     */
    public void setFtproot(String ftproot) {
        this.ftproot = ftproot;
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
         * @see org.apache.ftpserver.ftplet.UserManager#authenticate(org.apache.ftpserver.ftplet.
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
            return new String[] { userName };
        }

        /*
         * (non-Javadoc)
         * 
         * @see org.apache.ftpserver.ftplet.UserManager#getUserByName(java.lang.String)
         */
        @Override
        public User getUserByName(String name) throws FtpException {
            if (user.getName().equalsIgnoreCase(name)) {
                return user;
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
         * @see org.apache.ftpserver.ftplet.UserManager#save(org.apache.ftpserver.ftplet.User)
         */
        @Override
        public void save(User user) throws FtpException {
            throw new FtpException("Cann't save the user.");
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.platform.service.FtpService#getFtpServiceStatus()
     */
    @Override
    public boolean getFtpServiceStatus() {
        return isFtpEnable();
    }
}
