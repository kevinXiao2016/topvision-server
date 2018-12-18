/***********************************************************************
 * $Id: CmcTrapCodeDaoImpl.java,v1.0 2013-4-24 上午09:37:56 $
 * 
 * @author: Rod John
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.fault.dao.mybatis;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.topvision.ems.cmc.domain.CmcEntity;
import com.topvision.ems.cmc.fault.dao.CmcTrapCodeDao;
import com.topvision.framework.dao.mybatis.MyBatisDaoSupport;

/**
 * @author Rod John
 * @created @2013-4-24-上午09:37:56
 *
 */
@Repository("cmcTrapCodeDao")
public class CmcTrapCodeDaoImpl extends MyBatisDaoSupport<CmcEntity> implements CmcTrapCodeDao {
    
    private Map<Long, Integer> emsCodeCaches = new HashMap<Long, Integer>();
    
    public String getDomainName() {
        return "com.topvision.ems.cmc.fault.domain.CmcTrapCode";
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.cmc.dao.CmcTrapCodeDao#getEmsCodeFromTrap(java.lang.Integer)
     */
    @Override
    public Integer getEmsCodeFromTrap(Long trapCode) {
        synchronized (emsCodeCaches) {
            if (!emsCodeCaches.containsKey(trapCode)) {
                logger.debug("emsCodeCaches does not have " + trapCode);
                Integer emsCode = (Integer) getSqlSession().selectOne(getNameSpace() + "getCmcCodeFromTrapCode", trapCode);
                emsCodeCaches.put(trapCode, emsCode);
            } else {
                logger.debug("emsCodeCaches have " + trapCode);
            }
            return emsCodeCaches.get(trapCode);
        }
    }

    @Override
    public Boolean isCcmtsOnline(Long entityId, Long cmcIndex) {
        Map<String, Long> queryMap = new HashMap<String, Long>();
        queryMap.put("entityId", entityId);
        queryMap.put("cmcIndex", cmcIndex);
        Integer state = getSqlSession().selectOne(getNameSpace() + "getCcmtsState", queryMap);
        if (state != null && state.equals(1)) {
            return true;
        }
        return false;
    }
 
}
