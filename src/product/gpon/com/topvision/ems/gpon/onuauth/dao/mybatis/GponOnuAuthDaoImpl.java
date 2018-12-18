/***********************************************************************
 * $Id: GponOnuAuthDaoImpl.java,v1.0 2016年12月19日 下午1:27:16 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.gpon.onuauth.dao.mybatis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.topvision.ems.epon.olt.domain.OltPonAttribute;
import com.topvision.ems.gpon.onuauth.dao.GponOnuAuthDao;
import com.topvision.ems.gpon.onuauth.domain.GponAutoAuthConfig;
import com.topvision.ems.gpon.onuauth.domain.GponOnuAuthConfig;
import com.topvision.ems.gpon.onuauth.domain.GponOnuAuthMode;
import com.topvision.ems.gpon.onuauth.domain.GponOnuAutoFind;
import com.topvision.framework.dao.mybatis.MyBatisDaoSupport;

/**
 * @author Bravin
 * @created @2016年12月19日-下午1:27:16
 *
 */
@Repository
public class GponOnuAuthDaoImpl extends MyBatisDaoSupport<Object> implements GponOnuAuthDao {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.topvision.framework.dao.mybatis.MyBatisDaoSupport#getDomainName()
	 */
	@Override
	protected String getDomainName() {
		return "GponOnuAuth";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.topvision.ems.gpon.onuauth.dao.GponOnuAuthDao#
	 * batchInsertOrUpdateOnuAutoAuth(long, java.util.List)
	 */
	@Override
	public void batchInsertOrUpdateOnuAutoAuth(long entityId, List<GponAutoAuthConfig> list) {
		SqlSession sqlSession = getBatchSession();
		try {
			sqlSession.delete(getNameSpace() + "deleteOnuAutoAuthConfig", entityId);
			for (GponAutoAuthConfig autoAuthOnu : list) {
				autoAuthOnu.setEntityId(entityId);
				sqlSession.insert(getNameSpace("insertOnuAutoAuthConfig"), autoAuthOnu);
			}
			sqlSession.commit();
		} catch (Exception e) {
			logger.error("", e);
			sqlSession.rollback();
		} finally {
			sqlSession.close();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.topvision.ems.gpon.onuauth.dao.GponOnuAuthDao#
	 * batchInsertOrUpdateOnuAutoFind(long, java.util.List)
	 */
	@Override
	public void batchInsertOrUpdateOnuAutoFind(long entityId, List<GponOnuAutoFind> list) {
		SqlSession sqlSession = getBatchSession();
		try {
			sqlSession.delete(getNameSpace() + "deleteOnuAutoFind", entityId);
			for (GponOnuAutoFind autoFindOnu : list) {
				autoFindOnu.setEntityId(entityId);
				sqlSession.insert(getNameSpace("insertOnuAutoFind"), autoFindOnu);
			}
			sqlSession.commit();
		} catch (Exception e) {
			logger.error("", e);
			sqlSession.rollback();
		} finally {
			sqlSession.close();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.topvision.ems.gpon.onuauth.dao.GponOnuAuthDao#
	 * batchInsertOrUpdateOnuAuthConfig(long, java.util.List)
	 */
	@Override
	public void batchInsertOrUpdateOnuAuthConfig(long entityId, List<GponOnuAuthConfig> list) {
		SqlSession sqlSession = getBatchSession();
		try {
			sqlSession.delete(getNameSpace() + "deleteOnuAuthConfig", entityId);
			for (GponOnuAuthConfig authConfig : list) {
				authConfig.setEntityId(entityId);
				OltPonAttribute oltPonAttribute = new OltPonAttribute();
				oltPonAttribute.setEntityId(entityId);
				oltPonAttribute.setPonIndex(authConfig.getPonIndex());
				authConfig.setPonId((Long) sqlSession.selectOne("com.topvision.ems.epon.olt.domain.OltPon.getPonId",
						oltPonAttribute));

				sqlSession.insert(getNameSpace("insertOnuAuthConfig"), authConfig);
			}
			sqlSession.commit();
		} catch (Exception e) {
			logger.error("", e);
			sqlSession.rollback();
		} finally {
			sqlSession.close();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.topvision.ems.gpon.onuauth.dao.GponOnuAuthDao#
	 * batchInsertOrUpdateOnuAuthMode(long, java.util.List)
	 */
	@Override
	public void batchInsertOrUpdateOnuAuthMode(long entityId, List<GponOnuAuthMode> list) {
		SqlSession sqlSession = getBatchSession();
		try {
			sqlSession.delete(getNameSpace() + "deleteOnuAuthMode", entityId);
			for (GponOnuAuthMode authMode : list) {
				authMode.setEntityId(entityId);
				OltPonAttribute oltPonAttribute = new OltPonAttribute();
				oltPonAttribute.setEntityId(entityId);
				oltPonAttribute.setPonIndex(authMode.getPonIndex());
				authMode.setPonId((Long) sqlSession.selectOne("com.topvision.ems.epon.olt.domain.OltPon.getPonId",
						oltPonAttribute));
				sqlSession.insert(getNameSpace("insertOnuAuthMode"), authMode);
			}
			sqlSession.commit();
		} catch (Exception e) {
			logger.error("", e);
			sqlSession.rollback();
		} finally {
			sqlSession.close();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.topvision.ems.gpon.onuauth.dao.GponOnuAuthDao#selectPonAuthMode(java.
	 * lang.Long)
	 */
	@Override
	public List<GponOnuAuthMode> selectPonAuthMode(Long entityId) {
		return getSqlSession().selectList(getNameSpace("selectPonAuthMode"), entityId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.topvision.ems.gpon.onuauth.dao.GponOnuAuthDao#selectOnuAuthConfigList
	 * (java.lang.Long, java.lang.Long)
	 */
	@Override
	public List<GponOnuAuthConfig> selectOnuAuthConfigList(Long entityId, Long ponIndex) {
		Map<String, Long> map = new HashMap<String, Long>();
		map.put("entityId", entityId);
		map.put("ponIndex", ponIndex);
		return getSqlSession().selectList(getNameSpace("selectOnuAuthConfigList"), map);
	}

	@Override
	public GponOnuAuthConfig selectOnuAuthConfig(Long entityId, String sn) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("entityId", entityId);
		map.put("sn", sn);
		return getSqlSession().selectOne(getNameSpace("selectOnuAuthConfig"), map);
	}

	@Override
	public String selectOnuAuthConfigSn(Long entityId, Long authenOnuId) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("entityId", entityId);
		map.put("authenOnuId", authenOnuId);
		return getSqlSession().selectOne(getNameSpace("selectOnuAuthConfigSn"), map);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.topvision.ems.gpon.onuauth.dao.GponOnuAuthDao#selectOnuAutoFindList(
	 * java.lang.Long, java.lang.Long)
	 */
	@Override
	public List<GponOnuAutoFind> selectAutoFindOnuList(Long entityId, Long ponIndex) {
		Map<String, Long> map = new HashMap<String, Long>();
		map.put("entityId", entityId);
		map.put("ponIndex", ponIndex);
		return getSqlSession().selectList(getNameSpace("selectAutoFindOnuList"), map);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.topvision.ems.gpon.onuauth.dao.GponOnuAuthDao#
	 * selectAutoAuthConfigList(java.lang.Long, java.lang.Long)
	 */
	@Override
	public List<GponAutoAuthConfig> selectAutoAuthConfigList(Long entityId) {
		return getSqlSession().selectList(getNameSpace("selectAutoAuthConfigList"), entityId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.topvision.ems.gpon.onuauth.dao.GponOnuAuthDao#insertGponOnuAuth(com.
	 * topvision.ems.gpon .onuauth.domain.GponOnuAuthConfig)
	 */
	@Override
	public void insertGponOnuAuth(GponOnuAuthConfig gponOnuAuthConfig) {
		getSqlSession().insert(getNameSpace("insertOnuAuthConfig"), gponOnuAuthConfig);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.topvision.ems.gpon.onuauth.dao.GponOnuAuthDao#
	 * selectGponAutoAuthConfig(java.lang.Long)
	 */
	@Override
	public List<GponAutoAuthConfig> selectGponAutoAuthConfig(Long entityId) {
		return getSqlSession().selectList(getNameSpace("selectGponAutoAuthConfig"), entityId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.topvision.ems.gpon.onuauth.dao.GponOnuAuthDao#insertGponAutoAuth(com.
	 * topvision.ems.gpon .onuauth.domain.GponAutoAuthConfig)
	 */
	@Override
	public void insertGponAutoAuth(GponAutoAuthConfig gponAutoAuthConfig) {
		getSqlSession().insert(getNameSpace("insertOnuAutoAuthConfig"), gponAutoAuthConfig);
	}

	@Override
	public void updateGponAutoAuth(GponAutoAuthConfig gponAutoAuthConfig) {
		getSqlSession().update(getNameSpace("updateOnuAutoAuthConfig"), gponAutoAuthConfig);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.topvision.ems.gpon.onuauth.dao.GponOnuAuthDao#deleteGponOnuAuth(com.
	 * topvision.ems.gpon .onuauth.domain.GponOnuAuthConfig)
	 */
	@Override
	public void deleteGponOnuAuth(GponOnuAuthConfig gponOnuAuthConfig) {
		getSqlSession().delete(getNameSpace("deleteGponOnuAuth"), gponOnuAuthConfig);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.topvision.ems.gpon.onuauth.dao.GponOnuAuthDao#queryGponOnuAutoFind(
	 * java.util.Map)
	 */
	@Override
	public List<GponOnuAutoFind> queryGponOnuAutoFindList(Map<String, Object> conditions) {
		return getSqlSession().selectList(getNameSpace("queryGponOnuAutoFindList"), conditions);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.topvision.ems.gpon.onuauth.dao.GponOnuAuthDao#
	 * queryGponOnuAutoFindCount(java.util.Map)
	 */
	@Override
	public int queryGponOnuAutoFindCount(Map<String, Object> conditions) {
		return getSqlSession().selectOne(getNameSpace("queryGponOnuAutoFindCount"), conditions);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.topvision.ems.gpon.onuauth.dao.GponOnuAuthDao#queryGponOnuAutoFind(
	 * java.lang.Long, java.lang.Long)
	 */
	@Override
	public GponOnuAutoFind queryGponOnuAutoFind(Long entityId, Long onuIndex) {
		Map<String, Long> map = new HashMap<String, Long>();
		map.put("entityId", entityId);
		map.put("onuIndex", onuIndex);
		return getSqlSession().selectOne(getNameSpace("queryGponOnuAutoFind"), map);
	}

	@Override
	public GponOnuAutoFind selectGponOnuAutoFind(Long entityId, Long onuIndex) {
		Map<String, Long> map = new HashMap<String, Long>();
		map.put("entityId", entityId);
		map.put("onuIndex", onuIndex);
		return getSqlSession().selectOne(getNameSpace("selectGponOnuAutoFind"), map);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.topvision.ems.gpon.onuauth.dao.GponOnuAuthDao#deleteGponAutoAuth(com.
	 * topvision.ems.gpon .onuauth.domain.GponAutoAuthConfig)
	 */
	@Override
	public void deleteGponAutoAuth(GponAutoAuthConfig gponAutoAuthConfig) {
		getSqlSession().delete(getNameSpace("deleteGponAutoAuth"), gponAutoAuthConfig);
	}

	@Override
	public void deleteGponAutoAuthBySn(Long entityId, String sn) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("entityId", entityId);
		map.put("sn", sn);
		getSqlSession().delete(getNameSpace("deleteGponAutoAuthBySn"), map);
	}

	@Override
	public GponAutoAuthConfig queryAutoAuthConfig(GponAutoAuthConfig gponAutoAuthConfig) {
		return getSqlSession().selectOne(getNameSpace("selectAutoAuthConfig"), gponAutoAuthConfig);
	}

	@Override
	public List<Long> getOnuAuthIdList(Long ponId) {
		return getSqlSession().selectList(getNameSpace() + "getOnuAuthIdList", ponId);
	}

	@Override
	public List<GponOnuAutoFind> selectAutoFindOnuList(Map<String, Object> conditions) {
		return getSqlSession().selectList(getNameSpace("selectAutoFindOnu"), conditions);
	}

	@Override
	public Long selectPonId(long entityId, Long ponIndex) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("entityId", entityId);
		map.put("ponIndex", ponIndex);
		return getSqlSession().selectOne(getNameSpace("selectPonId"), map);
	}

	@Override
	public Integer getAutoFindOnuNum(Map<String, Object> conditions) {
		return getSqlSession().selectOne(getNameSpace("getAutoFindOnuNum"), conditions);
	}
}
