/***********************************************************************
 * $Id: GponOnuAuthDao.java,v1.0 2016年12月19日 下午1:26:51 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.gpon.onuauth.dao;

import java.util.List;
import java.util.Map;

import com.topvision.ems.gpon.onuauth.domain.GponAutoAuthConfig;
import com.topvision.ems.gpon.onuauth.domain.GponOnuAuthConfig;
import com.topvision.ems.gpon.onuauth.domain.GponOnuAuthMode;
import com.topvision.ems.gpon.onuauth.domain.GponOnuAutoFind;
import com.topvision.framework.dao.Dao;

/**
 * @author Bravin
 * @created @2016年12月19日-下午1:26:51
 *
 */
public interface GponOnuAuthDao extends Dao {

	/**
	 * @param entityId
	 * @param list
	 */
	void batchInsertOrUpdateOnuAutoAuth(long entityId, List<GponAutoAuthConfig> list);

	/**
	 * @param entityId
	 * @param list
	 */
	void batchInsertOrUpdateOnuAutoFind(long entityId, List<GponOnuAutoFind> list);

	/**
	 * @param entityId
	 * @param list
	 */
	void batchInsertOrUpdateOnuAuthConfig(long entityId, List<GponOnuAuthConfig> list);

	/**
	 * @param entityId
	 * @param list
	 */
	void batchInsertOrUpdateOnuAuthMode(long entityId, List<GponOnuAuthMode> list);

	/**
	 * @param entityId
	 * @return
	 */
	List<GponOnuAuthMode> selectPonAuthMode(Long entityId);

	/**
	 * @param entityId
	 * @param ponIndex
	 * @return
	 */
	List<GponOnuAuthConfig> selectOnuAuthConfigList(Long entityId, Long ponIndex);

	GponOnuAuthConfig selectOnuAuthConfig(Long entityId, String sn);

	String selectOnuAuthConfigSn(Long entityId, Long authenOnuId);

	/**
	 * @param entityId
	 * @param ponIndex
	 * @return
	 */
	List<GponOnuAutoFind> selectAutoFindOnuList(Long entityId, Long ponIndex);

	/**
	 * @param entityId
	 * @return
	 */
	List<GponAutoAuthConfig> selectAutoAuthConfigList(Long entityId);

	/**
	 * @param gponOnuAuthConfig
	 */
	void insertGponOnuAuth(GponOnuAuthConfig gponOnuAuthConfig);

	/**
	 * @param entityId
	 * @return
	 */
	List<GponAutoAuthConfig> selectGponAutoAuthConfig(Long entityId);

	/**
	 * @param gponAutoAuthConfig
	 */
	void insertGponAutoAuth(GponAutoAuthConfig gponAutoAuthConfig);

	/**
	 * @param gponAutoAuthConfig
	 */
	void updateGponAutoAuth(GponAutoAuthConfig gponAutoAuthConfig);

	/**
	 * @param gponOnuAuthConfig
	 */
	void deleteGponOnuAuth(GponOnuAuthConfig gponOnuAuthConfig);

	/**
	 * @param conditions
	 * @return
	 */
	List<GponOnuAutoFind> queryGponOnuAutoFindList(Map<String, Object> conditions);

	/**
	 * @param conditions
	 * @return
	 */
	int queryGponOnuAutoFindCount(Map<String, Object> conditions);

	/**
	 * @param entityId
	 * @param onuIndex
	 * @return
	 */
	GponOnuAutoFind queryGponOnuAutoFind(Long entityId, Long onuIndex);

	/**
	 * 
	 * @param entityId
	 * @param onuIndex
	 * @return
	 */
	GponOnuAutoFind selectGponOnuAutoFind(Long entityId, Long onuIndex);

	/**
	 * @param gponAutoAuthConfig
	 */
	void deleteGponAutoAuth(GponAutoAuthConfig gponAutoAuthConfig);

	/**
	 * @param gponAutoAuthConfig
	 */
	void deleteGponAutoAuthBySn(Long entityId, String sn);

	/**
	 * 
	 * @param gponAutoAuthConfig
	 * @return
	 */
	GponAutoAuthConfig queryAutoAuthConfig(GponAutoAuthConfig gponAutoAuthConfig);

	/**
	 * 获取认证onuId列表
	 * 
	 * @param ponId
	 *            PONID
	 * @return List<Long>
	 */
	List<Long> getOnuAuthIdList(Long ponId);

	/**
	 * @param conditions
	 * @return
	 */
	List<GponOnuAutoFind> selectAutoFindOnuList(Map<String, Object> conditions);

	Long selectPonId(long entityId, Long ponIndex);

	Integer getAutoFindOnuNum(Map<String, Object> conditions);
}
