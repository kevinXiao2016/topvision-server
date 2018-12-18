/***********************************************************************
 * $Id: CmcSyslogConfigDaoImpl.java,v1.0 2013-4-26 下午4:55:18 $
 * 
 * @author: Administrator
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.docsis.dao.mybatis;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.topvision.ems.cmc.docsis.dao.CmcDocsisConfigDao;
import com.topvision.ems.cmc.docsis.facade.domain.CmcDocsisConfig;
import com.topvision.framework.dao.mybatis.MyBatisDaoSupport;

/**
 * @author Administrator
 * @created @2013-4-26-下午4:55:18
 * 
 */
@Repository("cmcDocsisConfigDao")
public class CmcDocsisConfigDaoImpl extends MyBatisDaoSupport<CmcDocsisConfig> implements CmcDocsisConfigDao {

    @Override
    public String getDomainName() {
        return "com.topvision.ems.cmc.docsis.domain.CmcDocsisConfig";
    }

    @Override
    public void insertCmcDocsisConfig(CmcDocsisConfig cmcDocsis) {
        this.deleteCmcDocsisConfig(cmcDocsis);
        this.getSqlSession().insert(getNameSpace() + "insertCmcDocsisConfig", cmcDocsis);
    }

    @Override
    public void deleteCmcDocsisConfig(CmcDocsisConfig cmcDocsis) {
        this.getSqlSession().delete(getNameSpace() + "deleteCmcDocsisConfig", cmcDocsis);
    }

    @Override
    public void updateCmcDocsisConfig(CmcDocsisConfig cmcDocsis) {
        this.getSqlSession().update(getNameSpace() + "updateCmcDocsisConfig", cmcDocsis);
    }

    @Override
    public CmcDocsisConfig getCmcDocsisConfig(Long entityId, Long ifIndex) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("entityId", entityId);
        map.put("ifIndex", ifIndex);
        return this.getSqlSession().selectOne(getNameSpace() + "getCmcDocsisConfig", map);
    }

}
