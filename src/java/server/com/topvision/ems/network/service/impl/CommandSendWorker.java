/***********************************************************************
 * $Id: CommandSendWorker.java,v1.0 2015年6月4日 下午3:47:50 $
 * 
 * @author: loyal
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.network.service.impl;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactory;

import com.topvision.ems.network.dao.CommandSendDao;
import com.topvision.ems.network.domain.SendConfigEntityObject;
import com.topvision.ems.network.domain.SendConfigResult;
import com.topvision.ems.network.domain.TelnetLogin;
import com.topvision.ems.template.service.EntityTypeService;
import com.topvision.ems.upgrade.telnet.TelnetUtil;
import com.topvision.ems.upgrade.telnet.TelnetUtilFactory;
import com.topvision.framework.ping.PingExecutorService;
import com.topvision.framework.ping.PingResult;
import com.topvision.framework.ping.PingWorker;

/**
 * @author loyal
 * @created @2015年6月4日-下午3:47:50
 * 
 */
public abstract class CommandSendWorker {
    protected Logger logger = LoggerFactory.getLogger(getClass());
    protected SendConfigEntityObject sendConfigEntityObject;
    protected TelnetLogin telnetLogin;
    protected TelnetUtil telnetUtil;
    protected BeanFactory beanFactory;
    protected EntityTypeService entityTypeService;
    protected PingExecutorService pingExecutorService;
    protected TelnetUtilFactory telnetUtilFactory;
    protected CommandSendDao commandSendDao;
    protected Long sendCommandInterval;
    // protected List<String> oltConfigs;
    // protected List<String> onuConfigs;
    // protected List<String> cmtsAConfigs;
    // protected List<String> cmtsBConfigs;
    protected Map<Long, List<String>> configsMap;

    public SendConfigEntityObject getSendConfigEntityObject() {
        return sendConfigEntityObject;
    }

    public void setSendConfigEntityObject(SendConfigEntityObject sendConfigEntityObject) {
        this.sendConfigEntityObject = sendConfigEntityObject;
    }

    public void savaCommandSendResult(Long entityId, Integer result, String resultString) {
        SendConfigResult sendConfigResult = new SendConfigResult();
        sendConfigResult.setEntityId(entityId);
        sendConfigResult.setState(result);
        sendConfigResult.setDt(new Timestamp(System.currentTimeMillis()));
        sendConfigResult.setResult(resultString);
        recordSendConfigResult(sendConfigResult);
    }

    public void recordSendConfigResult(SendConfigResult sendConfigResult) {
        commandSendDao.updateSendConfigEntity(sendConfigResult);
    }

    public boolean checkPingReachable(String ip) {
        PingResult r = new PingResult();
        PingWorker worker = new PingWorker(ip, r);
        // TODO 需要使用数据库配置来进行控制
        // worker.setCount(count);
        // worker.setTimeout(timeout);
        try {
            Future<PingResult> f = pingExecutorService.submit(worker, r);
            return f.get().available();
        } catch (InterruptedException e) {
            logger.debug("checkPingReachable" + ip, e);
        } catch (ExecutionException e) {
            logger.debug("checkPingReachable" + ip, e);
        } catch (Exception e) {
            logger.debug("checkPingReachable" + ip, e);
        }
        return false;
    }

    protected abstract void process();

    public BeanFactory getBeanFactory() {
        return beanFactory;
    }

    public void setBeanFactory(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    public Long getSendCommandInterval() {
        return sendCommandInterval;
    }

    public void setSendCommandInterval(Long sendCommandInterval) {
        this.sendCommandInterval = sendCommandInterval;
    }

    // public List<String> getOltConfigs() {
    // return oltConfigs;
    // }
    //
    // public void setOltConfigs(List<String> oltConfigs) {
    // this.oltConfigs = oltConfigs;
    // }
    //
    // public List<String> getOnuConfigs() {
    // return onuConfigs;
    // }
    //
    // public void setOnuConfigs(List<String> onuConfigs) {
    // this.onuConfigs = onuConfigs;
    // }
    //
    // public List<String> getCmtsAConfigs() {
    // return cmtsAConfigs;
    // }
    //
    // public void setCmtsAConfigs(List<String> cmtsAConfigs) {
    // this.cmtsAConfigs = cmtsAConfigs;
    // }
    //
    // public List<String> getCmtsBConfigs() {
    // return cmtsBConfigs;
    // }
    //
    // public void setCmtsBConfigs(List<String> cmtsBConfigs) {
    // this.cmtsBConfigs = cmtsBConfigs;
    // }

    public TelnetLogin getTelnetLogin() {
        return telnetLogin;
    }

    public void setTelnetLogin(TelnetLogin telnetLogin) {
        this.telnetLogin = telnetLogin;
    }

    public Map<Long, List<String>> getConfigsMap() {
        return configsMap;
    }

    public void setConfigsMap(Map<Long, List<String>> configsMap) {
        this.configsMap = configsMap;
    }
}
