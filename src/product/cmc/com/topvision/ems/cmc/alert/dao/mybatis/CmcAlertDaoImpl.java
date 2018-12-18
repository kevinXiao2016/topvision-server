/***********************************************************************
 * $Id: CmcAlertDaoImpl.java,v1.0 2011-12-8 上午11:05:01 $
 * 
 * @author: loyal
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.alert.dao.mybatis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Repository;

import com.topvision.ems.cmc.alert.dao.CmcAlertDao;
import com.topvision.ems.cmc.facade.domain.DocsDevEvControl;
import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.fault.domain.Alert;
import com.topvision.ems.fault.domain.AlertType;
import com.topvision.ems.fault.domain.HistoryAlert;
import com.topvision.framework.dao.mybatis.MyBatisDaoSupport;

/**
 * @author loyal
 * @created @2011-12-8-上午11:05:01
 * 
 */
@Repository("cmcAlertDao")
public class CmcAlertDaoImpl extends MyBatisDaoSupport<Entity> implements
		CmcAlertDao {

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.topvision.framework.dao.ibatis.IbatisDaoSupport#getNameSpace()
	 */
	@Override
	public String getDomainName() {
		return "com.topvision.ems.cmc.alert.domain.CmcAlert";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.topvision.ems.cmc.dao.CmcAlertDao#getCmcAlertList(java.lang.Long)
	 */
	@SuppressWarnings("rawtypes")
    @Override
	public List<Alert> getCmcAlertList(Map map, Integer start, Integer limit) {
		/*Long cmcMac = (Long) getSqlSession().selectOne(
                getNameSpace() + "getCmcMacByCmcId", map.get("cmcId"));
		if(cmcMac != null){
		    String mac = new MacUtils(cmcMac).toString(MacUtils.MAOHAO).toUpperCase();
		    map.put("cmcMac", mac);
		}*/
		RowBounds r= new RowBounds(start, limit);
		return getSqlSession().selectList(getNameSpace() + "getCmcAlertList",
				map, r);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.topvision.ems.cmc.dao.CmcAlertDao#getCmcHistoryAlertList(java.lang
	 * .Long)
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public List<HistoryAlert> getCmcHistoryAlertList(Map map, Integer start,
			Integer limit) {
	    /*Long cmcMac = (Long) getSqlSession().selectOne(
                getNameSpace() + "getCmcMacByCmcId", map.get("cmcId"));
        if(cmcMac != null){
            String mac = new MacUtils(cmcMac).toString(MacUtils.MAOHAO).toUpperCase();
            map.put("cmcMac", mac);
        }*/
		RowBounds r= new RowBounds(start, limit);
		return getSqlSession().selectList(
				getNameSpace() + "getCmcHistoryAlertList", map,r);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.topvision.ems.cmc.dao.CmcAlertDao#getCmcAlertListNum(java.lang.Long)
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public Integer getCmcAlertListNum(Map map) {
		return (Integer) getSqlSession().selectOne(
				this.getNameSpace() + "getCmcAlertListNum", map);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.topvision.ems.cmc.dao.CmcAlertDao#getCmcHistoryAlertListNum(java.
	 * lang.Long)
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public Integer getCmcHistoryAlertListNum(Map map) {
		return (Integer) getSqlSession().selectOne(
				this.getNameSpace() + "getCmcHistoryAlertListNum", map);
	}

	@Override
	public List<DocsDevEvControl> getdocsDevEvControlList(Long entityId) {
		return getSqlSession().selectList(
				getNameSpace() + "getDevEvControlList", entityId);
	}

	@Override
	public String getCmcMacByCmcIndexAndEntityId(Long cmcIndex, Long entityId) {
		Map<String, Long> map = new HashMap<String, Long>();
		map.put("cmcIndex", cmcIndex);
		map.put("entityId", entityId);
		return (String) getSqlSession().selectOne(
				getNameSpace() + "getCmcMacByCmcIndexAndEntityId", map);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.topvision.ems.cmc.dao.CmcAlertDao#selectCmPollAlertType()
	 */
	public List<AlertType> selectCmPollAlertType() {
		return getSqlSession().selectList(
				getNameSpace() + "selectCmPollAlertType");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.topvision.ems.cmc.dao.CmcAlertDao#getCurrentCmPollAlertList(java.
	 * util.Map, java.lang.Integer, java.lang.Integer)
	 */
	public List<Alert> selectCurrentCmPollAlertList(Map<String, String> map,
			Integer start, Integer limit) {
		RowBounds r = new RowBounds(start, limit);
		return getSqlSession().selectList(
				getNameSpace() + "selectCurrentCmPollAlertList", map, r);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.topvision.ems.cmc.dao.CmcAlertDao#getHisCmPollAlertList(java.util
	 * .Map, java.lang.Integer, java.lang.Integer)
	 */
	public List<HistoryAlert> selectHisCmPollAlertList(Map<String, String> map,
			Integer start, Integer limit) {
		RowBounds r= new RowBounds(start, limit);
		return getSqlSession().selectList(
				getNameSpace() + "selectHisCmPollAlertList", map, r);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.topvision.ems.cmc.dao.CmcAlertDao#selectCurrentCmPollAlertNum(java
	 * .util.Map)
	 */
	public Integer selectCurrentCmPollAlertNum(Map<String, String> map) {
		return (Integer) getSqlSession().selectOne(
				this.getNameSpace() + "selectCurrentCmPollAlertNum", map);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.topvision.ems.cmc.dao.CmcAlertDao#selectHisCmPollAlertListNum(java
	 * .util.Map)
	 */
	public Integer selectHisCmPollAlertListNum(Map<String, String> map) {
		return (Integer) getSqlSession().selectOne(
				this.getNameSpace() + "selectHisCmPollAlertListNum", map);
	}

}
