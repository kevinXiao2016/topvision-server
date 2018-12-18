/***********************************************************************
 * $Id: CmcLoadBalPolicyTplDaoImpl.java,v1.0 2013-4-25 上午10:11:35 $
 * 
 * @author: dengl
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.loadbalance.dao.mybatis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.topvision.ems.cmc.loadbalance.dao.CmcLoadBalPolicyTplDao;
import com.topvision.ems.cmc.loadbalance.domain.CmcLoadBalBasicRuleTpl;
import com.topvision.ems.cmc.loadbalance.domain.CmcLoadBalPolicyTpl;
import com.topvision.framework.dao.mybatis.MyBatisDaoSupport;

/**
 * @author dengl
 * @created @2013-4-25-上午10:11:35
 * 
 */
 @Repository("cmcLoadBalPolicyTplDao")
public class CmcLoadBalPolicyTplDaoImpl extends
		MyBatisDaoSupport<CmcLoadBalPolicyTpl> implements
		CmcLoadBalPolicyTplDao {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.topvision.ems.cmc.dao.CmcLoadBalPolicyTplDao#queryLoadBalPolicyTpl()
	 */
	@Override
	public List<CmcLoadBalPolicyTpl> selectLoadBalPolicyTpl() {

		List<CmcLoadBalPolicyTpl> policyTpls = getSqlSession().selectList(
				getNameSpace() + "selectLoadBalPolicyTpl");
		for (CmcLoadBalPolicyTpl policyTpl : policyTpls) {
			List<CmcLoadBalBasicRuleTpl> ruleTpls = getSqlSession().selectList(
					getNameSpace() + "selectLoadBalBasicRuleTplByPolicy",
					policyTpl.getPolicyTplId());
			policyTpl.setRules(ruleTpls);
		}
		return policyTpls;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.topvision.ems.cmc.dao.CmcLoadBalPolicyTplDao#selectLoadBalPolicyTplById
	 * (java.lang.Long)
	 */
	@Override
	public CmcLoadBalPolicyTpl selectLoadBalPolicyTplById(final Long policyTplId) {

		CmcLoadBalPolicyTpl policyTpl = (CmcLoadBalPolicyTpl) getSqlSession()
				.selectOne(getNameSpace() + "selectLoadBalPolicyTplById",
						policyTplId);
		List<CmcLoadBalBasicRuleTpl> ruleTpls = getSqlSession().selectList(
				getNameSpace() + "selectLoadBalBasicRuleTplByPolicy",
				policyTplId);
		policyTpl.setRules(ruleTpls);
		return policyTpl;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.topvision.ems.cmc.dao.CmcLoadBalPolicyTplDao#insertLoadBalPolicyTpl
	 * (com.topvision.ems.cmc.domain.CmcLoadBalPolicyTpl)
	 */
	@Override
	public void insertLoadBalPolicyTpl(final CmcLoadBalPolicyTpl balPolicyTpl) {
		
		SqlSession batchSqlSession = getBatchSession();
		SqlSession sqlSession = getSqlSession();
		try {
			sqlSession.insert(getNameSpace() + "insertLoadBalPolicyTpl",
					balPolicyTpl);
			List<CmcLoadBalBasicRuleTpl> rules = balPolicyTpl.getRules();
			for (CmcLoadBalBasicRuleTpl r : rules) {
				sqlSession.insert(getNameSpace()
						+ "insertLoadBalBasicRuleTpl", r);
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("ruleTplId", r.getRuleTplId());
				map.put("policyTplId", balPolicyTpl.getPolicyTplId());
				sqlSession.insert(getNameSpace() + "insertPolicyRuleTplRef",
						map);
			}
			batchSqlSession.commit();
		} catch (Exception e) {
			logger.error("", e);
			batchSqlSession.rollback();
		} finally {
		    batchSqlSession.close();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.topvision.ems.cmc.dao.CmcLoadBalPolicyTplDao#deleteLoadBalPolicyTpl
	 * (java.lang.Long)
	 */
	@Override
	public void deleteLoadBalPolicyTpl(final Long policyTplId) {
		SqlSession sqlSession = getBatchSession();
		try {

			sqlSession.delete(getNameSpace() + "deleteRuleTplByPolicyTplId",
					policyTplId);
			sqlSession.delete(getNameSpace() + "deletePolicyTplById",
					policyTplId);
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
	 * com.topvision.ems.cmc.dao.CmcLoadBalPolicyTplDao#updateLoadBalPolicyTpl
	 * (com.topvision.ems.cmc.domain.CmcLoadBalPolicyTpl)
	 */
	@Override
	public void updateLoadBalPolicyTpl(final CmcLoadBalPolicyTpl balPolicyTpl) {
		
		SqlSession sqlSession = getBatchSession();
		try {
			// 删除规则
			getSqlSession().delete(getNameSpace() + "deleteRuleTplByPolicyTplId",
					balPolicyTpl.getPolicyTplId());
			// 添加规则
			List<CmcLoadBalBasicRuleTpl> rules = balPolicyTpl.getRules();
			for (CmcLoadBalBasicRuleTpl r : rules) {
				getSqlSession().insert(getNameSpace()
						+ "insertLoadBalBasicRuleTpl", r);
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("ruleTplId", r.getRuleTplId());
				map.put("policyTplId", balPolicyTpl.getPolicyTplId());
				getSqlSession().insert(getNameSpace() + "insertPolicyRuleTplRef",
						map);
			}
			// 修改策略
			sqlSession.update(getNameSpace() + "updateLoadBalPolicyTpl",
					balPolicyTpl);
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
	 * @see com.topvision.framework.dao.ibatis.IbatisDaoSupport#getNameSpace()
	 */
	@Override
	public String getDomainName() {
		return "com.topvision.ems.cmc.loadbalance.domain.CmcLoadBalPolicyTpl";
	}

}
