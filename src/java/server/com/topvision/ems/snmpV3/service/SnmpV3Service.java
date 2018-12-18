/***********************************************************************
 * $Id: SnmpV3Service.java,v1.0 2013-1-9 上午9:31:45 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.snmpV3.service;

import java.util.List;

import com.topvision.ems.snmpV3.facade.domain.SnmpNotifyFilterProfile;
import com.topvision.ems.snmpV3.facade.domain.SnmpNotifyFilterTable;
import com.topvision.ems.snmpV3.facade.domain.SnmpNotifyTable;
import com.topvision.ems.snmpV3.facade.domain.SnmpTargetParams;
import com.topvision.ems.snmpV3.facade.domain.SnmpTargetTable;
import com.topvision.framework.service.Service;

/**
 * @author Bravin
 * @created @2013-1-9-上午9:31:45
 * 
 */
public interface SnmpV3Service extends Service {

	/**
	 * 新增一条Target的数据
	 * 
	 * @param snmpTargetTable
	 */
	public void addTarget(SnmpTargetTable snmpTargetTable);

	/**
	 * 修改一条Target的数据
	 * 
	 * @param snmpTargetTable
	 */
    public void modifyTarget(SnmpTargetTable snmpTargetTable);

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
     * 刷新Target的数据
     * 
     * @param entityId
     * @return
     */
	public void refreshTarget(Long entityId);

	/**
	 * 新增一条TargetParams的数据
	 * 
	 * @param snmpTargetParams
	 */
    public void addTargetParams(SnmpTargetParams snmpTargetParams);

    /**
	 * 修改一条TargetParams的数据
	 * 
	 * @param snmpTargetParams
	 */
    public void modifyTargetParams(SnmpTargetParams snmpTargetParams);

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
     * 刷新TargetParams的数据
     * 
     * @param entityId
     * @return
     */
	public void refreshTargetParams(Long entityId);

	/**
	 * 新增一条notify的数据
	 * 
	 * @param snmpNotifyTable
	 */
    public void addNotify(SnmpNotifyTable snmpNotifyTable);

    /**
	 * 修改一条notify的数据
	 * 
	 * @param snmpNotifyTable
	 */
    public void modifyNotify(SnmpNotifyTable snmpNotifyTable);

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
     * 刷新notify的数据
     * 
     * @param entityId
     * @return
     */
	public void refreshNotify(Long entityId);

	/**
	 * 新增一条notify-filter的关系表的数据
	 * 
	 * @param snmpNotifyFilterProfile
	 */
    public void addNotifyProfile(SnmpNotifyFilterProfile snmpNotifyFilterProfile);

    /**
	 * 修改一条notify-filter的关系表的数据
	 * 
	 * @param snmpNotifyFilterProfile
	 */
    public void modifyNotifyProfile(
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
	public List<SnmpNotifyFilterProfile> loadNotifyProfile(Long entityId);

    /**
     * 刷新notify-filter的关系表的数据
     * 
     * @param entityId
     * @return
     */
	public void refreshNotifyProfile(Long entityId);

	/**
	 * 新建一条notifyFilter的数据
	 * 
	 * @param snmpNotifyFilterTable
	 */
	public void addNotifyFilter(SnmpNotifyFilterTable snmpNotifyFilterTable);

    /**
	 * 修改一条notifyFilter的数据
	 * 
	 * @param snmpNotifyFilterTable
	 */
	public void modifyNotifyFilter(SnmpNotifyFilterTable snmpNotifyFilterTable);

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
     * 刷新notifyFilter的数据
     * 
     * @param entityId
     * @return
     */
	public void refreshNotifyFilter(Long entityId);

}
