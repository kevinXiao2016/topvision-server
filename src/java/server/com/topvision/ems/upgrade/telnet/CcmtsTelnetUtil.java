/***********************************************************************
 * $Id: CcmtsTelnetUtil.java,v1.0 2014年9月23日 下午7:04:39 $
 * 
 * @author: loyal
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.upgrade.telnet;

import com.topvision.platform.domain.FtpConnectInfo;
import com.topvision.platform.domain.TftpClientInfo;

/**
 * @author loyal
 * @created @2014年9月23日-下午7:04:39
 * 
 */
public class CcmtsTelnetUtil extends CommonTelnetUtil {

    public CcmtsTelnetUtil(String ip) {
        super(ip);
    }

    @Override
    public boolean login(String userName, String password, String enablePassword,Boolean isAAA) {
        execCmd(userName);
        execCmd(password);
        String result = execCmd("enable");
        if (!result.trim().endsWith("#")) {
            if (enablePassword != null && enablePassword.equalsIgnoreCase("")) {
                return true;
            } else {
                String resultEnable = execCmd(enablePassword);
                return resultEnable.trim().endsWith("#");
            }
        } else {
            return true;
        }
    }

    @Override
    public boolean enterEnable(String password) {
        execCmd("end");
        String result = execCmd("enable");
        return result.trim().endsWith("#");
    }

    @Override
    public boolean writeConfig() {
        execCmd("end");
        setPrompt("#,[n],This will save the configuration", "--More--");
        execCmd("copy running-config startup-config");
        String result = execCmd("y");
        setPrompt("#,>,Password:,[n]", "--More--");
        return result.contains("Configuration saved successfully");
    }

    @Override
    public void reset() {
        execCmd("end");
        String prompt = execCmd("").trim();
        // 升级命令结果中有#号，必须使用完整的提示符表示结束
        setPrompt(prompt + ",>,Password:,[n]", "--More--");
        execCmd("reboot");
        setPrompt("#,>,Password:,[n],System is going to reboot...", "--More--");
        execCmd("y");
        setPrompt("#,>,Password:,[n]", "--More--");
        // return result.contains("System is going to reboot...");
    }

    @Override
    public boolean enterConfig() {
        execCmd("end");
        String result = execCmd("config terminal");
        return result.contains("<config>");
    }

    @Override
    public boolean uploadConfig(FtpConnectInfo connectInfo, String fileName) throws Exception {
        execCmd("end");
        return execCmd1("upload " + fileName + " tftp " + connectInfo.getIp() + " " + fileName);
    }

    @Override
    public void downLoadConfig(FtpConnectInfo connectInfo, String config, String newFileName) throws Exception {
        execCmd("end");
        String uploadText = "load " + config + " ftp " + connectInfo.getIp() + " " + connectInfo.getUserName() + " "
                + connectInfo.getPwd() + " " + newFileName;
        Boolean result = execCmd1(uploadText);
        if (!result) {
            throw new Exception(uploadText);
        }
        Thread.sleep(1000l);
    }

    /* (non-Javadoc)
     * @see com.topvision.ems.upgrade.telnet.TelnetUtil#downLoadConfig(com.topvision.platform.domain.TftpClientInfo, java.lang.String)
     */
    @Override
    public void downLoadConfig(TftpClientInfo tftpClientInfo, String fileName) throws Exception {
        execCmd("end");
        String uploadText = "load " + fileName + " tftp " + tftpClientInfo.getIp() + " " + fileName;
        Boolean result = execCmd1(uploadText);
        if (!result) {
            throw new Exception(uploadText);
        }
        Thread.sleep(1000l);
    }

    /* (non-Javadoc)
     * @see com.topvision.ems.upgrade.telnet.TelnetUtil#downLoadConfig(com.topvision.platform.domain.TftpClientInfo, java.lang.String, java.lang.String)
     */
    @Override
    public void downLoadConfig(TftpClientInfo tftpClientInfo, String config, String newFileName) throws Exception {
        execCmd("end");
        String uploadText = "load " + config + " tftp " + tftpClientInfo.getIp() + " " + newFileName;
        Boolean result = execCmd1(uploadText);
        if (!result) {
            throw new Exception(uploadText);
        }
        Thread.sleep(1000l);
    }

}
