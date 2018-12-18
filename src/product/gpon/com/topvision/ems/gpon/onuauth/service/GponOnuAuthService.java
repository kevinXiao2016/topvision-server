/***********************************************************************
 * $Id: GponOnuService.java,v1.0 2016年12月19日 下午1:12:52 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.gpon.onuauth.service;

import java.util.List;
import java.util.Map;

import com.topvision.ems.gpon.onuauth.domain.GponAutoAuthConfig;
import com.topvision.ems.gpon.onuauth.domain.GponOnuAuthConfig;
import com.topvision.ems.gpon.onuauth.domain.GponOnuAuthMode;
import com.topvision.ems.gpon.onuauth.domain.GponOnuAutoFind;

/**
 * @author Bravin
 * @created @2016年12月19日-下午1:12:52
 *
 */
public interface GponOnuAuthService {

	/**
	 * @param entityId
	 * @return
	 */
	List<GponOnuAuthMode> loadPonAuthMode(Long entityId);

	/**
	 * @param entityId
	 * @param ponIndex
	 * @return
	 */
	List<GponOnuAuthConfig> loadOnuAuthConfigList(Long entityId, Long ponIndex);

	/**
	 * @param entityId
	 * @param ponIndex
	 * @return
	 */
	List<GponOnuAutoFind> loadAutoFindOnuList(Long entityId, Long ponIndex);

	/**
	 * @param entityId
	 * @return
	 */
	List<GponAutoAuthConfig> loadAutoAuthConfigList(Long entityId);

	/**
	 * @param entityId
	 */
	void refreshGponOnuAuth(Long entityId);

	/**
	 * @param gponOnuAuthConfig
	 */
	void addGponOnuAuth(GponOnuAuthConfig gponOnuAuthConfig, Long onuNo);

	/**
	 * @param gponOnuAuthMode
	 */
	void modifyOnuAuthMode(GponOnuAuthMode gponOnuAuthMode);

	void modifySingleOnuAuthMode(GponOnuAuthMode gponOnuAuthMode);

	String batchModifyGponOnuAuthMode(Long entityId, List<Long> ponIndexs, List<GponOnuAuthMode> gponOnuAuthModes);

	/**
	 * @param gponAutoAuthConfig
	 */
	void addGponAutoAuth(GponAutoAuthConfig gponAutoAuthConfig);

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
	List<GponOnuAutoFind> queryGponOnuAutoFind(Map<String, Object> conditions);

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
	 * 
	 * @param gponAutoAuthConfig
	 * @return
	 */
	GponAutoAuthConfig queryAutoAuthConfig(GponAutoAuthConfig gponAutoAuthConfig);

	/**
	 * 获取PON口下的onuId列表
	 * 
	 * @param ponId
	 *            ponId
	 * @return List<Long>
	 */
	List<Long> getOnuAuthIdList(Long ponId);

	/**
	 * @param conditions
	 * @return
	 */
	List<GponOnuAutoFind> loadAutoFindOnuList(Map<String, Object> conditions);

	Long selectPonId(long entityId, Long ponIndex);

	Integer getAutoFindOnuNum(Map<String, Object> conditions);
}
