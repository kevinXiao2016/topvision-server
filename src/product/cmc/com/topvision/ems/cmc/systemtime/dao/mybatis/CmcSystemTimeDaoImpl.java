/***********************************************************************
 * $Id: CmcSystemTimeDaoImpl.java,v1.0 2013-7-18 下午4:46:49 $
 * 
 * @author: dosion
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.systemtime.dao.mybatis;

import org.springframework.stereotype.Repository;

import com.topvision.ems.cmc.domain.CmcEntity;
import com.topvision.ems.cmc.systemtime.dao.CmcSystemTimeDao;
import com.topvision.ems.cmc.systemtime.facade.domain.CmcSystemTimeConfig;
import com.topvision.framework.dao.mybatis.MyBatisDaoSupport;

/**
 * @author dosion
 * @created @2013-7-18-下午4:46:49
 * 
 */
@Repository("cmcSystemTimeDao")
public class CmcSystemTimeDaoImpl extends MyBatisDaoSupport<CmcEntity>
		implements CmcSystemTimeDao {

	@Override
	public CmcSystemTimeConfig selectCmcSystemTimeConfig(Long entityId) {
		return (CmcSystemTimeConfig) getSqlSession().selectOne(
				getNameSpace() + "getCmcSystemTime", entityId);
	}

	@Override
	public void updateCmcSystemTimeConfig(
			CmcSystemTimeConfig cmcSystemTimeConfig) {
		getSqlSession().update(getNameSpace() + "updateCmcSystemTime",
				cmcSystemTimeConfig);
	}
	
	@Override
    public void insertCmcSystemTimeConfig(Long entityId, CmcSystemTimeConfig cmcSystemTimeConfig) {
        cmcSystemTimeConfig.setEntityId(entityId);
        getSqlSession().insert(getNameSpace() + "insertCmcSystemTimeConfig", cmcSystemTimeConfig);
    }

	@Override
	public String getDomainName() {
		return "com.topvision.ems.cmc.systemtime.domain.CmcSystemTime";
	}

}
