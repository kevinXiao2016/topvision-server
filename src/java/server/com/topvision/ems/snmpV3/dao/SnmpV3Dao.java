/***********************************************************************
 * $Id: SnmpV3Dao.java,v1.0 2013-1-9 上午9:35:00 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.snmpV3.dao;

import java.util.List;

import com.topvision.ems.snmpV3.facade.domain.SnmpNotifyFilterProfile;
import com.topvision.ems.snmpV3.facade.domain.SnmpNotifyFilterTable;
import com.topvision.ems.snmpV3.facade.domain.SnmpNotifyTable;
import com.topvision.ems.snmpV3.facade.domain.SnmpTargetParams;
import com.topvision.ems.snmpV3.facade.domain.SnmpTargetTable;
import com.topvision.framework.dao.BaseEntityDao;
import com.topvision.framework.domain.BaseEntity;

/**
 * @author Bravin
 * @created @2013-1-9-上午9:35:00
 * 
 */
public interface SnmpV3Dao extends BaseEntityDao<BaseEntity> {

	/**
	 * 新增一条Target的数据
	 * 
	 * @param snmpTargetTable
	 */
	public void insertTarget(SnmpTargetTable snmpTargetTable);

	/**
	 * 修改一条Target的数据
	 * 
	 * @param snmpTargetTable
	 */
	public void updateTarget(SnmpTargetTable snmpTargetTable);

	/**
	 * 删除一条Target的数据
	 * 
	 * @param entityId
	 * @param targetName
	 */
	public void deleteTarget(Long entityId, String targetName);

	/**
	 * 从数据库获取Target的数据
	 * 
	 * @param entityId
	 * @return
	 */
	public List<SnmpTargetTable> loadTarget(Long entityId);

	/**
	 * 删除entity对应的所有Target数据
	 * 
	 * @param entityId
	 */
	public void deleteAllTarget(Long entityId);

	/**
	 * 新增一条TargetParams的数据
	 * 
	 * @param snmpTargetParams
	 */
	public void insertTargetParams(SnmpTargetParams snmpTargetParams);

	/**
	 * 修改一条TargetParams的数据
	 * 
	 * @param snmpTargetParams
	 */
	public void updateTargetParams(SnmpTargetParams snmpTargetParams);

	/**
	 * 删除一条TargetParams的数据
	 * 
	 * @param entityId
	 * @param targetParamsName
	 */
	public void deleteTargetParams(Long entityId, String targetParamsName);

	/**
	 * 从数据库获取TargetParams的数据
	 * 
	 * @param entityId
	 * @return
	 */
	public List<SnmpTargetParams> loadTargetParams(Long entityId);

	/**
	 * 删除entity对应的所有TargetParams数据
	 * 
	 * @param entityId
	 */
	public void deleteAllTargetParams(Long entityId);

	/**
	 * 新增一条notify的数据
	 * 
	 * @param snmpNotifyTable
	 */
	public void insertNotify(SnmpNotifyTable snmpNotifyTable);

	/**
	 * 修改一条notify的数据
	 * 
	 * @param snmpNotifyTable
	 */
	public void updateNotify(SnmpNotifyTable snmpNotifyTable);

	/**
	 * 删除一条notify的数据
	 * 
	 * @param entityId
	 * @param notifyName
	 */
	public void deleteNotify(Long entityId, String notifyName);

	/**
	 * 从数据库获取notify的数据
	 * 
	 * @param entityId
	 * @return
	 */
	public List<SnmpNotifyTable> loadNotify(Long entityId);

	/**
	 * 删除entity对应的所有notify数据
	 * 
	 * @param entityId
	 */
	public void deleteAllNotify(Long entityId);

	/**
	 * 新增一条notify-filter的关系表的数据
	 * 
	 * @param snmpNotifyFilterProfile
	 */
	public void insertNotifyProfile(
			SnmpNotifyFilterProfile snmpNotifyFilterProfile);

	/**
	 * 修改一条notify-filter的关系表的数据
	 * 
	 * @param snmpNotifyFilterProfile
	 */
	public void updateNotifyProfile(
			SnmpNotifyFilterProfile snmpNotifyFilterProfile);

	/**
	 * 删除一条notify-filter的关系表的数据
	 * 
	 * @param snmpNotifyFilterProfile
	 */
	public void deleteNotifyProfile(
			SnmpNotifyFilterProfile snmpNotifyFilterProfile);

	/**
	 * 从数据库获取notify-filter的关系表的数据
	 * 
	 * @param entityId
	 * @return
	 */
	public List<SnmpNotifyFilterProfile> loadNotifyFilterProfile(Long entityId);

	/**
	 * 删除entity对应的所有notify-filter的关系表的数据
	 * 
	 * @param entityId
	 */
	public void deleteAllNotifyProfile(Long entityId);

	/**
	 * 新建一条notifyFilter的数据
	 * 
	 * @param snmpNotifyFilterTable
	 */
	public void insertNotifyFilter(SnmpNotifyFilterTable snmpNotifyFilterTable);

	/**
	 * 修改一条notifyFilter的数据
	 * 
	 * @param snmpNotifyFilterTable
	 */
	public void updateNotifyFilter(SnmpNotifyFilterTable snmpNotifyFilterTable);

	/**
	 * 删除一条notifyFilter的数据
	 * 
	 * @param entityId
	 * @param notifyFilterProfileName
	 * @param notifyFilterSubtree
	 */
	public void deleteNotifyFilter(Long entityId,
			String notifyFilterProfileName, String notifyFilterSubtree);

	/**
	 * 从数据库获取notifyFilter的数据
	 * 
	 * @param entityId
	 * @return
	 */
	public List<SnmpNotifyFilterTable> loadNotifyFilter(Long entityId);

	/**
	 * 删除entity对应的所有notifyFilter的数据
	 * 
	 * @param entityId
	 */
	public void deleteAllNotifyFilter(Long entityId);

	/**
	 * 批量修改Target的数据
	 * 
	 * @param entityId
	 * @param targets
	 */
	public void batchInsertTarget(Long entityId, List<SnmpTargetTable> targets);

	/**
	 * 批量修改TargetParams的数据
	 * 
	 * @param entityId
	 * @param targetParams
	 */
	public void batchInsertTargetParams(Long entityId,
			List<SnmpTargetParams> targetParams);

	/**
	 * 批量修改Notify的数据
	 * 
	 * @param entityId
	 * @param notifys
	 */
	public void batchInsertNotify(Long entityId, List<SnmpNotifyTable> notifys);

	/**
	 * 批量修改NotifyFilterProfile的数据
	 * 
	 * @param entityId
	 * @param notifyFilterProfiles
	 */
	public void batchInsertNotifyFilterProfile(Long entityId,
			List<SnmpNotifyFilterProfile> notifyFilterProfiles);

	/**
	 * 批量修改NotifyFilterTable的数据
	 * 
	 * @param entityId
	 * @param notifyFilterTables
	 */
	public void batchInsertNotifyFilterTable(Long entityId,
			List<SnmpNotifyFilterTable> notifyFilterTables);
}
