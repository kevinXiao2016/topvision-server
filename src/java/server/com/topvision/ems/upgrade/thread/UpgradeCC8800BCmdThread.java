/***********************************************************************
 * $Id: UpgradeCC8800BCmdThread.java,v1.0 2014-10-27 下午2:49:35 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.upgrade.thread;

import com.topvision.ems.upgrade.telnet.CcmtsTelnetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author jay
 * @created @2014-10-27-下午2:49:35
 */
public class UpgradeCC8800BCmdThread extends Thread {
    private Logger logger = LoggerFactory.getLogger(UpgradeCC8800BCmdThread.class);
    private CcmtsTelnetUtil ccmtsTelnetUtil;
    private String upgradeCmd;
    private String cmdResult;
    private boolean upgradeCompleteFlag = false;
    private boolean upgradeState = false;

    public UpgradeCC8800BCmdThread(CcmtsTelnetUtil ccmtsTelnetUtil, String upgradeCmd) {
        this.upgradeCmd = upgradeCmd;
        this.ccmtsTelnetUtil = ccmtsTelnetUtil;
    }

    @Override
    public void run() {
        logger.debug("start upgrade [" + upgradeCmd + "]");
        try {
            ccmtsTelnetUtil.execCmd("end");
            String prompt = ccmtsTelnetUtil.execCmd("").trim();
            // 升级命令结果中有#号，必须使用完整的提示符表示结束
            ccmtsTelnetUtil.setPrompt(prompt + ",>,Password:,[n]", "--More--");
            cmdResult = ccmtsTelnetUtil.execCmd(upgradeCmd);
            // 命令结束 回复默认的提示符
            ccmtsTelnetUtil.setPrompt("#,>,Password:,[n]", "--More--");
            upgradeState = true;
        } catch (Exception e) {
            logger.debug("upgrade [" + upgradeCmd + " error]", e);
            upgradeState = false;
        } finally {
            upgradeCompleteFlag = true;
        }
        logger.debug("end upgrade [" + upgradeCmd + "]");
    }

    public String getCmdResult() {
        return cmdResult;
    }

    public void setCmdResult(String cmdResult) {
        this.cmdResult = cmdResult;
    }

    public boolean isUpgradeCompleteFlag() {
        return upgradeCompleteFlag;
    }

    public void setUpgradeCompleteFlag(boolean upgradeCompleteFlag) {
        this.upgradeCompleteFlag = upgradeCompleteFlag;
    }

    public boolean isUpgradeState() {
        return upgradeState;
    }

    public void setUpgradeState(boolean upgradeState) {
        this.upgradeState = upgradeState;
    }
}
