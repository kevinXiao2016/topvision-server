package com.topvision.ems.epon.ofa.dao.mybatis;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.topvision.ems.epon.ofa.dao.CommonOptiTransDao;
import com.topvision.ems.epon.ofa.facade.domain.CommonOptiTrans;
import com.topvision.ems.epon.ofa.facade.domain.OfaBasicInfo;
import com.topvision.framework.dao.mybatis.MyBatisDaoSupport;

@Repository("commonOptiTransDao")
public class CommonOptiTransDaoImpl extends MyBatisDaoSupport<OfaBasicInfo> implements
		CommonOptiTransDao{

		@Override
		public CommonOptiTrans getCommonOptiTransById(Long entityId) {
			return getSqlSession().selectOne(getNameSpace("getCommonOptiTransById"), entityId);
		}

		@Override
		public void batchInsertOrUpdateCommonOptiTrans(
				List<CommonOptiTrans> commonOptiTransList) {
			SqlSession sqlSession = getBatchSession();
	        try {
	            for (CommonOptiTrans commonOptiTrans : commonOptiTransList) {
	                sqlSession.insert(getNameSpace("insertOrUpdateCommonOptiTrans"), commonOptiTrans);
	            }
	            sqlSession.commit();
	        } catch (Exception e) {
	            logger.error("batchInsertOrUpdateCommonOptiTrans error", e);
	            sqlSession.rollback();
	        } finally {
	            sqlSession.close();
	        }
		}
		
		@Override
		public void insertOrUpdateCommonOptiTrans(
				CommonOptiTrans commonOptiTrans) {
			getSqlSession().insert(getNameSpace("insertOrUpdateCommonOptiTrans"), commonOptiTrans);
		}

		@Override
		protected String getDomainName() {
			return "com.topvision.ems.epon.ofa.facade.domain.CommonOptiTrans";
		}

}