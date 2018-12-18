/***********************************************************************
 * $Id: CmcAclDaoImpl.java,v1.0 2013-5-3 下午01:40:57 $
 * 
 * @author: lzs
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.acl.dao.mybatis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.topvision.ems.cmc.acl.dao.CmcAclDao;
import com.topvision.ems.cmc.acl.domain.CmcAclDefAct;
import com.topvision.ems.cmc.acl.facade.domain.CmcAclDefAction;
import com.topvision.ems.cmc.acl.facade.domain.CmcAclInfo;
import com.topvision.ems.cmc.domain.CmcEntity;
import com.topvision.framework.common.MacUtils;
import com.topvision.framework.dao.mybatis.MyBatisDaoSupport;

/**
 * @author lzs
 * @created @2013-5-3-下午01:40:57
 * 
 */
@Repository("cmcAclDao")
public class CmcAclDaoImpl extends MyBatisDaoSupport<CmcEntity> implements
		CmcAclDao {
	private Logger logger = LoggerFactory.getLogger(CmcAclDaoImpl.class);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.topvision.ems.cmc.dao.CmcAclDao#refreshAllPositionDefAct(java.lang
	 * .Long, java.util.List)
	 */
	@Override
	public void refreshAllPositionDefAct(Long entityId,
			List<CmcAclDefAction> aclDefActList) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("entityId", entityId);
		getSqlSession().delete(getNameSpace() + "deleteAllAclDefAct", param);
		param = new HashMap<String, Object>();
		param.put("entityId", entityId);
		param.put("uplinkIngress", 1);
		param.put("uplingEgress", 1);
		param.put("cableEgress", 1);
		param.put("cabelIngress", 1);
		for (CmcAclDefAction defAct : aclDefActList) {
			/**
			 * 1: uplinkIngress(1) 2: uplinkEgress(2) 3: cableEgress(3) 4:
			 * cableIngress(4)
			 */
			int position = defAct.getTopAclPositionIndex();
			/**
			 * 1: deny(0) 2: permit(1)
			 */
			int act = defAct.getTopPositionDefAction();
			if (1 == position) {
				param.put("uplinkIngress", act);
			} else if (2 == position) {
				param.put("uplinkEgress", act);
			} else if (3 == position) {
				param.put("cableEgress", act);
			} else if (4 == position) {
				param.put("cabelIngress", act);
			}
		}
		getSqlSession().insert(getNameSpace() + "insertAclDefAct", param);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.topvision.ems.cmc.dao.CmcAclDao#modifyOnePositionDefAct(java.lang
	 * .Long, com.topvision.ems.cmc.facade.domain.CmcAclDefAction)
	 */
	@Override
	public void modifyOnePositionDefAct(Long entityId, CmcAclDefAction defAct) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.topvision.ems.cmc.dao.CmcAclDao#getOnePositionDefAct(java.lang.Long,
	 * java.lang.Integer)
	 */
	@Override
	public CmcAclDefAction getOnePositionDefAct(Long entityId, Integer position) {
		CmcAclDefAction result = null;
		if (position != 0) {

			Map<String, Object> param = new HashMap<String, Object>();
			param.put("entityId", entityId);

			CmcAclDefAct act = (CmcAclDefAct) getSqlSession().selectOne(
					this.getNameSpace() + "getAclDefAct", param);
			if (act != null) {
				result = new CmcAclDefAction();

				result.setTopAclPositionIndex(position);
				if (position == 1) {
					result.setTopPositionDefAction(act.getUplinkIngress());
				} else if (position == 2) {
					result.setTopPositionDefAction(act.getUplinkEgress());
				} else if (position == 3) {
					result.setTopPositionDefAction(act.getCableEgress());
				} else if (position == 4) {
					result.setTopPositionDefAction(act.getCabelIngress());
				}

			}
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.topvision.ems.cmc.dao.CmcAclDao#refreshAllAclList(java.lang.Long,
	 * java.util.List)
	 */
	@Override
	public void refreshAllAclList(final Long entityId,
			final List<CmcAclInfo> allAclList) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("entityId", entityId);

		SqlSession sqlSession = getBatchSession();
		try {
			sqlSession.delete(getNameSpace() + "deleteAllAcl", param);
			for (CmcAclInfo aclInfo : allAclList) {
				aclInfo.setEntityId(entityId);
				sqlSession.insert(getNameSpace() + "addAclInfo", aclInfo);
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
	 * com.topvision.ems.cmc.dao.CmcAclDao#refreshOneAclInfo(java.lang.Long,
	 * com.topvision.ems.cmc.facade.domain.CmcAclInfo)
	 */
	@Override
	public void refreshOneAclInfo(Long entityId, CmcAclInfo aclInfo) {

		deleteOneAclInfo(entityId, aclInfo.getTopCcmtsAclListIndex());
		aclInfo.setEntityId(entityId);
        //add by fanzidong, 需要在入库前格式化MAC地址
        String formatedSrcMac = MacUtils.convertToMaohaoFormat(aclInfo.getTopMatchlSrcMac());
        aclInfo.setTopMatchlSrcMac(formatedSrcMac);
        String formatedDestMac = MacUtils.convertToMaohaoFormat(aclInfo.getTopMatchDstMac());
        aclInfo.setTopMatchDstMac(formatedDestMac);
		getSqlSession().insert(getNameSpace() + "addAclInfo", aclInfo);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.topvision.ems.cmc.dao.CmcAclDao#deleteOneAclInfo(java.lang.Long,
	 * java.lang.Integer)
	 */
	@Override
	public void deleteOneAclInfo(Long entityId, Integer aclId) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("entityId", entityId);
		param.put("topCcmtsAclListIndex", aclId);
		getSqlSession().delete(getNameSpace() + "deleteOneAcl", param);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.topvision.ems.cmc.dao.CmcAclDao#getOnePositionAclList(java.lang.Long,
	 * java.lang.Integer)
	 */
	@Override
	public List<CmcAclInfo> getOnePositionAclList(Long entityId,
			Integer position) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("entityId", entityId);
		if (position == 0) {
			param.put("installPosition", "0000");
		} else if (position == 1) {
			param.put("installPosition", "___1");
		} else if (position == 2) {
			param.put("installPosition", "__1_");
		} else if (position == 3) {
			param.put("installPosition", "_1__");
		} else if (position == 4) {
			param.put("installPosition", "1___");
		}

		List<CmcAclInfo> allAclList = getSqlSession().selectList(
				getNameSpace() + "getOnePositionAclList", param);
		return allAclList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.topvision.ems.cmc.dao.CmcAclDao#getOneAclInfo(java.lang.Long,
	 * java.lang.Integer)
	 */
	@Override
	public CmcAclInfo getOneAclInfo(Long entityId, Integer aclId) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("entityId", entityId);
		param.put("topCcmtsAclListIndex", aclId);
		CmcAclInfo acl = (CmcAclInfo) getSqlSession().selectOne(
				getNameSpace() + "getOneAcl", param);
		return acl;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.topvision.framework.dao.ibatis.IbatisDaoSupport#getNameSpace()
	 */
	@Override
	public String getDomainName() {
		return "com.topvision.ems.cmc.acl.domain.CmcAcl";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.topvision.ems.cmc.dao.CmcAclDao#getAllAclList(java.lang.Long)
	 */
	@Override
	public List<CmcAclInfo> getAllAclList(Long entityId) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("entityId", entityId);
		List<CmcAclInfo> allAclList = getSqlSession().selectList(
				getNameSpace() + "getAllAclList", param);
		return allAclList;
	}

}
