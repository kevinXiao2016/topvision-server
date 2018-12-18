/***********************************************************************
 * $Id: OltTelnetUtil.java,v1.0 2014年9月23日 下午7:05:09 $
 * 
 * @author: loyal
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.upgrade.telnet;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.topvision.platform.domain.FtpConnectInfo;
import com.topvision.platform.domain.TftpClientInfo;

/**
 * @author loyal
 * @created @2014年9月23日-下午7:05:09
 * 
 */
public class OltTelnetUtil extends CommonTelnetUtil {
    private Logger logger = LoggerFactory.getLogger(OltTelnetUtil.class);

    public OltTelnetUtil(String ip) {
        super(ip);
    }

    @Override
    public synchronized boolean login(String userName, String password, String enablePassword,Boolean isAAA) {
        if (isAAA) {
            execCmd(userName);
            execCmd(password);
            setPrompt("#,>,[n],% Bad passwords, too many failures!", "--More--");
            String result = execCmd("en");
            setPrompt("#,>,Password:,[n]", "--More--");
            return result.trim().endsWith("#");
        } else {
            execCmd(password);
            execCmd("en");
            setPrompt("#,>,[n],% Bad passwords, too many failures!", "--More--");
            String result = execCmd(enablePassword);
            setPrompt("#,>,Password:,[n]", "--More--");
            return result.trim().endsWith("#");
        }
    }

    @Override
    public boolean enterEnable(String password) {
        return false;
    }

    @Override
    public boolean writeConfig() {
        execCmd("end");
        setPrompt("#,[n],This will save the configuration", "--More--");
        execCmd("write file");
        String result = execCmd("y");
        setPrompt("#,>,Password:,[n]", "--More--");
        return result.contains("Success");
    }

    @Override
    public void reset() {
        execCmd("end");
        execCmd("system reboot");
        execCmd("y");
    }

    @Override
    public boolean enterConfig() {
        execCmd("end");
        String result = execCmd("config terminal");
        return result.contains("<config>");
    }

    @Override
    public boolean uploadConfig(FtpConnectInfo connectInfo, String fileName) throws IOException {
        execCmd("end");
        boolean result = execCmd1("upload config " + connectInfo.getIp() + " " + connectInfo.getUserName() + " "
                + connectInfo.getPwd() + " " + fileName);
        if (result) {
            wait4TransforComplete();
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void downLoadConfig(FtpConnectInfo connectInfo, String fileName, String newFileName) throws IOException {
        execCmd("end");
        execCmd1("download config " + connectInfo.getIp() + " " + connectInfo.getUserName() + " "
                + connectInfo.getPwd() + " " + newFileName);
        wait4TransforComplete();
    }

    private void wait4TransforComplete() {
        int count = 12; // 设定超时时间为1分钟
        while (true) {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                logger.error("", e);
            }
            if (count < 1) {
                break;
            }
            String downLoadResult = execCmd("show file-transfer-status");
            count--;
            if (downLoadResult.contains("transferring")) {
                logger.debug("transferring upgrade file");
                continue;
            } else if (downLoadResult.contains("transfer successfully")) {
                logger.debug("transfer upgrade file successfully");
                break;
            } else {
                logger.error("transfer upgrade file error");
            }
        }
    }

    public static void main(String[] a) {
        Long x = 205L;
        Long y = 205L;

        System.out.print(x == y);
    }

    /* (non-Javadoc)
     * @see com.topvision.ems.upgrade.telnet.TelnetUtil#downLoadConfig(com.topvision.platform.domain.TftpClientInfo, java.lang.String)
     */
    @Override
    public void downLoadConfig(TftpClientInfo tftpClientInfo, String fileName) throws Exception {

    }

    /* (non-Javadoc)
     * @see com.topvision.ems.upgrade.telnet.TelnetUtil#downLoadConfig(com.topvision.platform.domain.TftpClientInfo, java.lang.String, java.lang.String)
     */
    @Override
    public void downLoadConfig(TftpClientInfo tftpClientInfo, String fileName, String newFileName) throws Exception {
    }
}
