/***********************************************************************
 * $Id: CmcShareSecretDaoImpl.java,v1.0 2013-7-23 下午2:39:49 $
 * 
 * @author: dosion
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.sharesecret.dao.mybatis;

import com.topvision.ems.cmc.sharesecret.dao.CmcShareSecretDao;
import com.topvision.ems.cmc.sharesecret.facade.domain.CmcShareSecretConfig;
import com.topvision.ems.cmc.syslog.facade.domain.CmcSyslogConfig;
import com.topvision.framework.dao.mybatis.MyBatisDaoSupport;

/**
 * @author dosion
 * @created @2013-7-23-下午2:39:49
 *
 */
//@Repository("cmcShareSecretDao") 
public class CmcShareSecretDaoImpl extends MyBatisDaoSupport<CmcSyslogConfig> implements CmcShareSecretDao {

    /* (non-Javadoc)
     * @see com.topvision.ems.cmc.dao.CmcShareSecretDao#selectCmcShareSecretConfig(java.lang.Long)
     */
    @Override
    public CmcShareSecretConfig selectCmcShareSecretConfig(Long cmcId) {
        return (CmcShareSecretConfig) getSqlSession().selectOne(getNameSpace() + "getCmcShareSecret", cmcId);
    }

    /* (non-Javadoc)
     * @see com.topvision.ems.cmc.dao.CmcShareSecretDao#updateCmcShareSecretConfig(java.lang.Long, com.topvision.ems.cmc.facade.domain.CmcShareSecretConfig)
     */
    @Override
    public void updateCmcShareSecretConfig(Long cmcId, CmcShareSecretConfig cmcShareSecretConfig) {
        cmcShareSecretConfig.setCmcId(cmcId);
        getSqlSession().update(getNameSpace() + "updateCmcShareSecret", cmcShareSecretConfig);
    }

    @Override
    public void insertCmcShareSecret(Long cmcId, CmcShareSecretConfig cmcShareSecretConfig) {
        cmcShareSecretConfig.setCmcId(cmcId);
        getSqlSession().insert(getNameSpace() + "insertCmcShareSecretConfig", cmcShareSecretConfig);
    }

    @Override
    public String getDomainName() {
        return "com.topvision.ems.cmc.sharesecret.domain.CmcShareSecret";
    }

}
