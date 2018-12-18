/***********************************************************************
 * $Id: TrapServerConfigDaoImpl.java,v1.0 2013-4-23 下午2:56:47 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.trapserver.dao.mybatis;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.topvision.ems.cmc.trapserver.dao.CmcTrapServerConfigDao;
import com.topvision.ems.cmc.trapserver.facade.domain.CmcTrapServer;
import com.topvision.framework.dao.mybatis.MyBatisDaoSupport;

/**
 * @author flack
 * @created @2013-4-23-下午2:56:47
 * 
 */
 @Repository("cmcTrapServerConfigDao")
public class CmcTrapServerConfigDaoImpl extends
		MyBatisDaoSupport<CmcTrapServer> implements CmcTrapServerConfigDao {

	@Override
	public String getDomainName() {
		return "com.topvision.ems.cmc.trapserver.domain.CmcTrapServer";
	}

	@Override
	public List<CmcTrapServer> getAllTrapServer(Long entityId) {
		return this.getSqlSession().selectList(
				getNameSpace() + "getAllTrapServer", entityId);
	}

	@Override
	public void insertTrapServer(CmcTrapServer trapServer) {
		this.getSqlSession().insert(getNameSpace() + "insertTrapServer",
				trapServer);
	}

	@Override
	public void deleteTrapServer(CmcTrapServer trapServer) {
		this.getSqlSession().delete(getNameSpace() + "deleteTrapServer",
				trapServer);

	}

	public void batchInsertTrapServer(Long entityId,
			final List<CmcTrapServer> trapServerList) {

		SqlSession sqlSession = getBatchSession();
		try {
			this.getSqlSession().delete(getNameSpace() + "deleteAllTrapServer",
					entityId);
			for (CmcTrapServer trapServer : trapServerList) {
				insertTrapServer(trapServer);
			}
			sqlSession.commit();
		} catch (Exception e) {
			logger.error("", e);
			sqlSession.rollback();
		} finally {
			sqlSession.close();
		}
	}

}
