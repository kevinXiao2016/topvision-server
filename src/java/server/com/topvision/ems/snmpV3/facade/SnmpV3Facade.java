/***********************************************************************
 * $Id: SnmpV4Facade.java,v1.0 2013-1-9 上午9:45:00 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.snmpV3.facade;

import java.util.List;

import com.topvision.ems.facade.Facade;
import com.topvision.ems.snmpV3.facade.domain.SnmpNotifyFilterProfile;
import com.topvision.ems.snmpV3.facade.domain.SnmpNotifyFilterTable;
import com.topvision.ems.snmpV3.facade.domain.SnmpNotifyTable;
import com.topvision.ems.snmpV3.facade.domain.SnmpTargetParams;
import com.topvision.ems.snmpV3.facade.domain.SnmpTargetTable;
import com.topvision.framework.annotation.EngineFacade;
import com.topvision.framework.snmp.SnmpParam;

/**
 * @author Bravin
 * @created @2013-1-9-上午9:45:00
 * 
 */
@EngineFacade(serviceName = "snmpV3Facade", beanName = "snmpV3Facade")
public interface SnmpV3Facade extends Facade {

	/**
	 * 新增一条Target的数据
	 * 
	 * @param snmpTargetTable
	 */
	public void addTarget(SnmpParam snmpParam, SnmpTargetTable snmpTargetTable);

	/**
	 * 修改一条Target的数据
	 * 
	 * @param snmpTargetTable
	 */
	public void modifyTarget(SnmpParam snmpParam,
			SnmpTargetTable snmpTargetTable);

	/**
	 * 删除一条Target的数据
	 * 
	 * @param entityId
	 * @param targetName
	 */
	public void deleteTarget(SnmpParam snmpParam, String targetName);

	/**
	 * 刷新Target的数据
	 * 
	 * @param entityId
	 * @return
	 */
	public List<SnmpTargetTable> refreshTarget(SnmpParam snmpParam);

	/**
	 * 新增一条TargetParams的数据
	 * 
	 * @param snmpTargetParams
	 */
	public void addTargetParams(SnmpParam snmpParam,
			SnmpTargetParams snmpTargetParams);

	/**
	 * 修改一条TargetParams的数据
	 * 
	 * @param snmpTargetParams
	 */
	public void modifyTargetParams(SnmpParam snmpParam,
			SnmpTargetParams snmpTargetParams);

	/**
	 * 删除一条TargetParams的数据
	 * 
	 * @param entityId
	 * @param targetParamsName
	 */
	public void deleteTargetParams(SnmpParam snmpParam, String targetParamsName);

	/**
	 * 刷新TargetParams的数据
	 * 
	 * @param entityId
	 * @return
	 */
	public List<SnmpTargetParams> refreshTargetParams(SnmpParam snmpParam);

	/**
	 * 新增一条notify的数据
	 * 
	 * @param snmpNotifyTable
	 */
	public void addNotify(SnmpParam snmpParam, SnmpNotifyTable snmpNotifyTable);

	/**
	 * 修改一条notify的数据
	 * 
	 * @param snmpNotifyTable
	 */
	public void modifyNotify(SnmpParam snmpParam,
			SnmpNotifyTable snmpNotifyTable);

	/**
	 * 删除一条notify的数据
	 * 
	 * @param entityId
	 * @param notifyName
	 */
	public void deleteNotify(SnmpParam snmpParam, String notifyName);

	/**
	 * 刷新notify的数据
	 * 
	 * @param entityId
	 * @return
	 */
	public List<SnmpNotifyTable> refreshNotify(SnmpParam snmpParam);

	/**
	 * 新增一条notify-filter的关系表的数据
	 * 
	 * @param snmpNotifyFilterProfile
	 */
	public void addNotifyProfile(SnmpParam snmpParam,
			SnmpNotifyFilterProfile snmpNotifyFilterProfile);

	/**
	 * 修改一条notify-filter的关系表的数据
	 * 
	 * @param snmpNotifyFilterProfile
	 */
	public void modifyNotifyProfile(SnmpParam snmpParam,
			SnmpNotifyFilterProfile snmpNotifyFilterProfile);

	/**
	 * 删除一条notify-filter的关系表的数据
	 * 
	 * @param snmpNotifyFilterProfile
	 */
	public void deleteNotifyProfile(SnmpParam snmpParam,
			SnmpNotifyFilterProfile snmpNotifyFilterProfile);

	/**
	 * 刷新notify-filter的关系表的数据
	 * 
	 * @param entityId
	 * @return
	 */
	public List<SnmpNotifyFilterProfile> refreshNotifyProfile(
			SnmpParam snmpParam);

	/**
	 * 新建一条notifyFilter的数据
	 * 
	 * @param snmpNotifyTable
	 */
	public void addNotifyFilter(SnmpParam snmpParam,
			SnmpNotifyFilterTable snmpNotifyFilterTable);

	/**
	 * 修改一条notifyFilter的数据
	 * 
	 * @param snmpNotifyTable
	 */
	public void modifyNotifyFilter(SnmpParam snmpParam,
			SnmpNotifyFilterTable snmpNotifyFilterTable);

	/**
	 * 删除一条notifyFilter的数据
	 * 
	 * @param entityId
	 * @param notifyFilterProfileName
	 * @param notifyFilterSubtree
	 */
	public void deleteNotifyFilter(SnmpParam snmpParam,
			String notifyFilterProfileName,
			String notifyFilterSubtree);

	/**
	 * 刷新notifyFilter的数据
	 * 
	 * @param entityId
	 * @return
	 */
	public List<SnmpNotifyFilterTable> refreshNotifyFilter(SnmpParam snmpParam);

}
