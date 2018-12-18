/***********************************************************************
 * $Id: CmcAuthDaoImpl.java,v1.0 2012-10-9 下午02:28:39 $
 * 
 * @author: dosion
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.auth.dao.mybatis;

import org.springframework.stereotype.Repository;

import com.topvision.ems.cmc.auth.dao.CmcAuthDao;
import com.topvision.ems.cmc.auth.facade.domain.CcmtsAuthManagement;
import com.topvision.ems.cmc.domain.CmcEntity;
import com.topvision.framework.dao.mybatis.MyBatisDaoSupport;

/**
 * @author dosion
 * @created @2012-10-9-下午02:28:39
 * 
 */
@Repository("cmcAuthDao")
public class CmcAuthDaoImpl extends MyBatisDaoSupport<CmcEntity> implements
		CmcAuthDao {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.topvision.ems.cmc.dao.CmcAuthDao#getCcmtsAuthInfo(java.lang.Long)
	 */
	@Override
	public CcmtsAuthManagement getCcmtsAuthInfo(Long cmcId) {
		return (CcmtsAuthManagement) getSqlSession().selectOne(
				getNameSpace() + "selectCmcAuthInfo", cmcId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.topvision.ems.cmc.dao.CmcAuthDao#getCmcIndexByCmcId(java.lang.Long)
	 */
	@Override
	public Long getCmcIndexByCmcId(Long cmcId) {
		return (Long) getSqlSession().selectOne(
				getNameSpace() + "selectCmcIndexByCmcId", cmcId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.topvision.framework.dao.ibatis.IbatisDaoSupport#getNameSpace()
	 */
	@Override
	public String getDomainName() {
		return "com.topvision.ems.cmc.auth.domain.CmcAuth";
	}
	
    @Override
    public void updateCmcAuthInfo(CcmtsAuthManagement authMgmt, Long cmcId) {
        authMgmt.setCmcId(cmcId);
        getSqlSession().update(getNameSpace() + "updateCmcAuthInfo", authMgmt);
    }

}
