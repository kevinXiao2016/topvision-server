/***********************************************************************
 * $Id: EponTrapCodeDaoImpl.java,v1.0 2013-10-26 上午10:08:42 $
 * 
 * @author: lizongtian
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.fault.dao.mybatis;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.topvision.ems.epon.fault.dao.EponTrapCodeDao;
import com.topvision.framework.dao.mybatis.MyBatisDaoSupport;

/**
 * @author lizongtian
 * @created @2013-10-26-上午10:08:42
 *
 */
@Repository("eponTrapCodeDao")
public class EponTrapCodeDaoImpl extends MyBatisDaoSupport<Object> implements EponTrapCodeDao {

    private Map<String, Integer> emsCodeCaches = new HashMap<String, Integer>();

    @Override
    public Integer getEmsCodeFromTrap(Integer trapCode, String module, Integer type) {
        synchronized (emsCodeCaches) {
            String identifyKey = trapCode + "$" + module + "$" + type;
            if (!emsCodeCaches.containsKey(identifyKey)) {
                logger.debug("emsCodeCaches does not have " + identifyKey);
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("trapCode", trapCode);
                map.put("module", module);
                map.put("type", type);
                Integer emsCode = getSqlSession().selectOne(getNameSpace("getEponCodeFromTrapCode"), map);
                emsCodeCaches.put(identifyKey, emsCode);
            } else {
                logger.debug("emsCodeCaches have " + identifyKey);
            }
            return emsCodeCaches.get(identifyKey);
        }
    }

    @Override
    protected String getDomainName() {
        return "com.topvision.ems.epon.fault.domain.EponTrapCode";
    }

    @Override
    public Integer getRelatedCmtsEventCode(Long trapCode) {
        return getSqlSession().selectOne(getNameSpace("getRelatedCmtsEventCode"), trapCode);
    }

}
