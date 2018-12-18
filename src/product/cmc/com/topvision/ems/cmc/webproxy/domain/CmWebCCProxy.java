/***********************************************************************
 * CmWebCCProxy.java,v1.0 17-4-26 上午8:02 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.webproxy.domain;

import com.topvision.ems.upgrade.telnet.TelnetUtil;
import com.topvision.framework.dao.mybatis.AliasesSuperType;
import org.apache.ibatis.type.Alias;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.*;

/**
 * @author jay
 * @created 17-4-26 上午8:02
 */
@Alias("cmWebCCProxy")
public class CmWebCCProxy implements Serializable, AliasesSuperType {
    private Logger logger = LoggerFactory.getLogger(CmWebCCProxy.class);
    private static final long serialVersionUID = 8571259721660333279L;
    private Long cmcId;
    private String mangerIp;
    private Integer proxyPort;
    private Boolean loginStatus;
    private Boolean status;
    private TelnetUtil telnetUtil;
    private Set<Long> heartbeatIds = Collections.synchronizedSet(new HashSet<Long>());
    private Object hO = new Object();

    public Long getCmcId() {
        return cmcId;
    }

    public void setCmcId(Long cmcId) {
        this.cmcId = cmcId;
    }

    public String getMangerIp() {
        return mangerIp;
    }

    public void setMangerIp(String mangerIp) {
        this.mangerIp = mangerIp;
    }

    public Integer getProxyPort() {
        return proxyPort;
    }

    public void setProxyPort(Integer proxyPort) {
        this.proxyPort = proxyPort;
    }

    public Set<Long> getHeartbeatIds() {
        synchronized (hO) {
            return heartbeatIds;
        }
    }

    public Integer heartbeatNum() {
        synchronized (hO) {
            return heartbeatIds.size();
        }
    }

    public void addHeartbeatId(Long heartbeatId) {
        synchronized (hO) {
            heartbeatIds.add(heartbeatId);
        }
    }

    public void releaseHeartbeatId(Long heartbeatId) {
        synchronized (hO) {
            heartbeatIds.remove(heartbeatId);
        }
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public TelnetUtil getTelnetUtil() {
        return telnetUtil;
    }

    public void setTelnetUtil(TelnetUtil telnetUtil) {
        this.telnetUtil = telnetUtil;
    }

    public Boolean getLoginStatus() {
        return loginStatus;
    }

    public void setLoginStatus(Boolean loginStatus) {
        this.loginStatus = loginStatus;
    }



    public void checkStatus() {
        if (!loginStatus) {
            status = false;
            return;
        }
        try {
            String str = telnetUtil.execCmd("netstat -nap | grep " + proxyPort);
            logger.debug("CmWebCCProxy checkStatus : {}" ,str);
            String keyStrV4 = "0.0.0.0:" + proxyPort;
            String keyStrV6 = ":::" + proxyPort;
            if (str != null && (str.contains(keyStrV4) || str.contains(keyStrV6))) {
                status = true;
            } else {
                status = false;
            }
        } catch (Exception e) {
            logger.debug("",e);
            status = false;
        }
    }

    @Override
    public String toString() {
        return "CmWebCCProxy{" + "<br>" +
                "&nbsp;&nbsp;&nbsp;&nbsp;cmcId=" + cmcId + "<br>" +
                ",&nbsp;&nbsp;&nbsp;&nbsp;mangerIp=" + mangerIp + "<br>" +
                ",&nbsp;&nbsp;&nbsp;&nbsp;proxyPort=" + proxyPort + "<br>" +
                ",&nbsp;&nbsp;&nbsp;&nbsp;loginStatus=" + loginStatus + "<br>" +
                ",&nbsp;&nbsp;&nbsp;&nbsp;status=" + status + "<br>" +
                ",&nbsp;&nbsp;&nbsp;&nbsp;heartbeatIds=" + heartbeatIds + "<br>" +
                '}';
    }

    public void stopProxy() {
        checkStatus();
        if (status) {
            /*
                # netstat -nap | grep 7000
                tcp        0      0 0.0.0.0:7000            0.0.0.0:*               LISTEN      9475/ssh
                tcp        0      0 :::7000                 :::*                    LISTEN      9475/ssh
                # exit
                cc247(config-super)#exit
                cc247(config)# exit
                cc247# exit
                cc247> exit
                Connection closed by foreign host
                Connection to 172.17.2.148 closed.
                # exit
                cc247(config-super)#exit
                cc247(config)# exit
                cc247# exit
                cc247> exit
             */
            try {
                telnetUtil.execCmd("exit");
                telnetUtil.execCmd("exit");
                telnetUtil.execCmd("exit");
                telnetUtil.execCmd("exit");
                telnetUtil.execCmd("exit");
                telnetUtil.execCmd("exit");
                telnetUtil.execCmd("exit");
                telnetUtil.execCmd("exit");
                telnetUtil.execCmd("exit");
            } catch (Exception e) {
                logger.debug("", e);
            }
            try {
                telnetUtil.disconnect();
            } catch (Exception e) {
                logger.debug("", e);
            }
        } else {
            try {
                telnetUtil.disconnect();
            } catch (Exception e) {
                logger.debug("", e);
            }
        }
    }
}
