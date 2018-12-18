package com.topvision.ems.cmc.syslog.dao.mybatis;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.topvision.ems.cmc.syslog.dao.CmcSyslogServerDao;
import com.topvision.ems.cmc.syslog.facade.domain.CmcSyslogServerEntry;
import com.topvision.framework.dao.mybatis.MyBatisDaoSupport;

@Repository("cmcSyslogServerDao")
public class CmcSyslogServerDaoImpl extends
		MyBatisDaoSupport<CmcSyslogServerEntry> implements CmcSyslogServerDao {

	@Override
	public String getDomainName() {
		return "com.topvision.ems.cmc.syslog.domain.CmcSyslogServerEntry";
	}

	@Override
	public List<CmcSyslogServerEntry> getCmcSyslogServerListByEntityId(
			Long entityId) {
		List<CmcSyslogServerEntry> cmcSyslogServerEntrys = getSqlSession()
				.selectList(getNameSpace() + "getCmcSyslogServerListByCmcId",
						entityId);
		return cmcSyslogServerEntrys;
	}

	@Override
	public void batchRefreshCmcSyslogServerEntrys(
			final List<CmcSyslogServerEntry> cmcSyslogServerEntrys,
			final Long entityId) {
		SqlSession sqlSession = getBatchSession();
		try {
			getSqlSession().delete(
					getNameSpace() + "deleteCmcSyslogServersByCmcId", entityId);
			for (CmcSyslogServerEntry cmcSyslogServerEntry : cmcSyslogServerEntrys) {
				cmcSyslogServerEntry.setEntityId(entityId);
				sqlSession.insert(
						getNameSpace() + "insertCmcSyslogServerEntry",
						cmcSyslogServerEntry);
			}
			sqlSession.commit();
		} catch (Exception e) {
			logger.error("", e);
			sqlSession.rollback();
		} finally {
			sqlSession.close();
		}

	}

	@Override
	public void insertCmcSyslogServerEntry(
			CmcSyslogServerEntry cmcSyslogServerEntry) {
		getSqlSession().insert(getNameSpace() + "insertCmcSyslogServerEntry",
				cmcSyslogServerEntry);
	}

	@Override
	public void deleteCmcSyslogServerEntry(
			CmcSyslogServerEntry cmcSyslogServerEntry) {
		getSqlSession().delete(getNameSpace() + "deleteCmcSyslogServerEntry",
				cmcSyslogServerEntry);
	}

	@Override
	public void updateCmcSyslogServerEntry(
			CmcSyslogServerEntry cmcSyslogServerEntry) {
		getSqlSession().update(getNameSpace() + "updateCmcSyslogServerEntry",
				cmcSyslogServerEntry);
	}

}
