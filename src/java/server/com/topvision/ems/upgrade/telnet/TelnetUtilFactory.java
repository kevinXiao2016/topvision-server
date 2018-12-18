/***********************************************************************
 * $Id: TelnetUtilFactory.java,v1.0 2014年9月23日 下午4:06:53 $
 * 
 * @author: loyal
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.upgrade.telnet;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.network.domain.TelnetLogin;
import com.topvision.ems.template.service.EntityTypeService;
import com.topvision.ems.upgrade.exception.ExceedMaxTelnetNumberException;
import com.topvision.ems.upgrade.exception.TelnetConnectException;
import com.topvision.ems.upgrade.exception.TelnetLoginException;

/**
 * @author loyal
 * @created @2014年9月23日-下午4:06:53
 * 
 */
@Service("telnetUtilFactory")
public class TelnetUtilFactory implements BeanFactoryAware {
    Map<String, Long> telnetUtilNumMap = new HashMap<String, Long>();
    private static Integer maxTelnetNum = 10;
    @Autowired
    private BeanFactory beanFactory;
    @Autowired
    private EntityTypeService entityTypeService;

    public TelnetUtil getOltTelnetUtil(String ip) throws Exception {
        if (hasConnect(ip)) {
            if (telnetUtilNumMap.get(ip) == null) {
                telnetUtilNumMap.put(ip, 1L);
            } else {
                telnetUtilNumMap.put(ip, telnetUtilNumMap.get(ip) + 1);
            }
            return new OltTelnetUtil(ip);
        } else {
            throw new ExceedMaxTelnetNumberException();
        }

    }

    public TelnetUtil getCcmtsTelnetUtil(String ip) throws Exception {
        if (hasConnect(ip)) {
            return new CcmtsTelnetUtil(ip);
        } else {
            throw new ExceedMaxTelnetNumberException();
        }

    }

    public TelnetUtil getTelnetUtil(Long typeId, String ip, TelnetLogin telnetLogin) {
        TelnetUtil telnetUtil;
        if (entityTypeService.isOlt(typeId)) {
            try {
                telnetUtil = getOltTelnetUtil(ip);
            } catch (Exception e) {
                throw new ExceedMaxTelnetNumberException();
            }
        } else {
            try {
                telnetUtil = getCcmtsTelnetUtil(ip);
            } catch (Exception e) {
                throw new ExceedMaxTelnetNumberException();
            }
        }
        try {
            telnetUtil.connect(ip);
        } catch (IOException e) {
            throw new TelnetConnectException();
        }
        if (!telnetUtil.login(telnetLogin.getUserName(), telnetLogin.getPassword(), telnetLogin.getEnablePassword(),telnetLogin.getIsAAA())) {
            throw new TelnetLoginException();
        }
        return telnetUtil;
    }

    public Boolean hasConnect(String ip) {
        if (telnetUtilNumMap.get(ip) == null) {
            return true;
        }
        Long currentNum = telnetUtilNumMap.get(ip);
        if (currentNum < maxTelnetNum) {
            return true;
        }
        return false;
    }

    public void releaseTelnetUtil(TelnetUtil telnetUtil) {
        String ip = telnetUtil.getIp();
        telnetUtil.disconnect();
        if (telnetUtilNumMap.get(ip) != null) {
            telnetUtilNumMap.put(ip, telnetUtilNumMap.get(ip) - 1);
        }
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

}
