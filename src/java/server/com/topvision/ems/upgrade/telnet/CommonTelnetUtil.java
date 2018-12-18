/***********************************************************************
 * $Id: CommonTelnetUtil.java,v1.0 2014年9月23日 下午7:02:45 $
 * 
 * @author: loyal
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.upgrade.telnet;

import java.io.IOException;

import com.topvision.framework.telnet.TelnetVty;
import com.topvision.platform.domain.FtpConnectInfo;
import com.topvision.platform.domain.TftpClientInfo;

/**
 * @author loyal
 * @created @2014年9月23日-下午7:02:45
 * 
 */
public abstract class CommonTelnetUtil implements TelnetUtil {

    private TelnetVty telnetVty;
    private String ip;
    private String re;

    public CommonTelnetUtil(String ip) {
        this.ip = ip;
        this.telnetVty = new TelnetVty();
    }

    @Override
    public synchronized void connect(String ip) throws IOException {
        this.ip = ip;
        telnetVty.connect(ip);
    }

    @Override
    public synchronized String execCmd(String cmd) {
        try {
            String result = this.telnetVty.sendLine(cmd);
            re = re + result;
            return result;
        } catch (IOException e) {

        }
        return "";
    }

    @Override
    public synchronized Boolean execCmd1(String cmd) {
        try {
            String result = this.telnetVty.sendLine(cmd);
            re = re + result;
            String[] stringArray = result.split("[\\t\\n\\r]");
            if (stringArray != null) {
                for (int i = 0; i < stringArray.length; i++) {
                    if (stringArray[i].trim().startsWith("%") || stringArray[i].trim().startsWith("Error:")) {
                        return false;
                    }
                }
            }
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public void setPrompt(String pList, String morePromptString) {
        this.telnetVty.setPrompt(pList, morePromptString);
    }

    @Override
    public Long getTimeout() {
        return this.telnetVty.getTimeout();
    }

    @Override
    public void setTimeout(Long timeout) {
        this.telnetVty.setTimeout(timeout);
    }

    @Override
    public void disconnect() {
        try {
            this.telnetVty.disconnect();
        } catch (IOException e) {
        }
    }

    @Override
    public void quit() {
        execCmd("quit");
    }

    @Override
    public void end() {
        execCmd("end");
    }

    @Override
    public String getIp() {
        return ip;
    }

    public TelnetVty getTelnetVty() {
        return telnetVty;
    }

    public void setTelnetVty(TelnetVty telnetVty) {
        this.telnetVty = telnetVty;
    }

    @Override
    public String getRe() {
        return re;
    }

    public void setRe(String re) {
        this.re = re;
    }

    @Override
    public void downLoadConfig(TftpClientInfo tftpClientInfo, String fileName) throws Exception {
        // TODO Add by Victor@20160728合并NewFeature_D20160321_EquipmentReplacement，这几个方法未有实现，增加空方法
    }

    @Override
    public void downLoadConfig(TftpClientInfo tftpClientInfo, String fileName, String newFileName) throws Exception {
        // TODO Add by Victor@20160728合并NewFeature_D20160321_EquipmentReplacement，这几个方法未有实现，增加空方法
    }

    @Override
    public void downLoadConfig(FtpConnectInfo connectInfo, String fileName) throws Exception {
        // TODO Add by Victor@20160728合并NewFeature_D20160321_EquipmentReplacement，这几个方法未有实现，增加空方法
    }
}
