/***********************************************************************
 * $Id: BatchAutoDiscoveryDao.java,v1.0 2014-5-11 上午11:34:42 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.network.dao;

import java.util.List;
import java.util.Map;

import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.network.domain.AutoDiscoveryPeriodConfig;
import com.topvision.ems.network.domain.BatchAutoDiscoveryEntityType;
import com.topvision.ems.network.domain.BatchAutoDiscoveryIps;
import com.topvision.ems.network.domain.BatchAutoDiscoveryPeriod;
import com.topvision.ems.network.domain.BatchAutoDiscoverySnmpConfig;
import com.topvision.ems.network.domain.EntitySnap;
import com.topvision.ems.network.domain.IpSegmentInfo;
import com.topvision.framework.dao.BaseEntityDao;

/**
 * @author haojie
 * @created @2014-5-11-上午11:34:42
 * 
 */
public interface BatchAutoDiscoveryDao extends BaseEntityDao<Entity> {

	/**
	 * getAutoDiscoveryIps
	 * 
	 * @return
	 */
	List<BatchAutoDiscoveryIps> getAutoDiscoveryIps();

	/**
	 * getAutoDiscoverySnmpConfigs
	 * 
	 * @return
	 */
	List<BatchAutoDiscoverySnmpConfig> getAutoDiscoverySnmpConfigs();

	/**
	 * getAutoDiscoverySysObjectId
	 * 
	 * @return
	 */
	List<String> getAutoDiscoverySysObjectId();

	/**
	 * updateLastAutoDiscoveryTime
	 * 
	 * @param batchAutoDiscoveryIps
	 */
	void updateLastAutoDiscoveryTime(BatchAutoDiscoveryIps batchAutoDiscoveryIps);

	/**
	 * 查询所有的拓扑IP段
	 * 
	 * @return
	 */
	List<BatchAutoDiscoveryIps> queryAllDiscoveryIps(Map<String, Object> queryMap);

	/**
	 * 查询IP段对应的设备信息
	 * 
	 * @param ipList
	 * @return
	 */
	IpSegmentInfo queryDeviceInfoByIps(List<String> ipList);

	/**
	 * 查询指定Ip段下的设备列表
	 * 
	 * @param ipList
	 * @return
	 */
	List<EntitySnap> queryDeviceByIpSegment(List<String> ipList);

	/**
	 * 获取自动扫描策略
	 * 
	 * @return
	 */
	List<AutoDiscoveryPeriodConfig> getAutoDiscoveryPeriodConfigs();

	/**
	 * 获取所有扫描的设备类型
	 * 
	 * @return
	 */
	List<BatchAutoDiscoveryEntityType> queryAllTypeIds();

	/**
	 * 获取snmp标签
	 * 
	 * @return
	 */
	List<BatchAutoDiscoverySnmpConfig> querySnmpConfigs();

	/**
	 * 获取自动扫描策略
	 * 
	 * @return
	 */
	BatchAutoDiscoveryPeriod queryPeriod();

	/**
	 * 增加IP段
	 * 
	 * @param ips
	 */
	void addNetSegment(BatchAutoDiscoveryIps ips);

	/**
	 * 修改IP段
	 * 
	 * @param ips
	 */
	void modifyNetSegment(BatchAutoDiscoveryIps ips);

	/**
	 * 批量删除IP段
	 * 
	 * @param ids
	 */
	void batchDeleteNetSegment(List<Long> ids);

	/**
	 * 增加snmp标签
	 * 
	 * @param snmpConfig
	 */
	void addSnmpConfig(BatchAutoDiscoverySnmpConfig snmpConfig);

	/**
	 * 修改snmp标签
	 * 
	 * @param snmpConfig
	 */
	void modifySnmpConfig(BatchAutoDiscoverySnmpConfig snmpConfig);

	/**
	 * 删除snmp标签
	 * 
	 * @param id
	 */
	void deleteSnmpConfig(Long id);

	/**
	 * 修改扫描设备类型
	 * 
	 * @param typeIds
	 */
	void modifyTypeIds(List<Long> typeIds);

	/**
	 * 修改扫描间隔
	 * 
	 * @param period
	 */
	void modifyPeriod(BatchAutoDiscoveryPeriod period);

	/**
	 * 根据ip段的id获取所有ip段信息
	 * 
	 * @param ids
	 * @return
	 */
	List<BatchAutoDiscoveryIps> getIpsByIds(List<Long> ids);

	/**
	 * 根据ID获取IP段
	 * 
	 * @param id
	 * @return
	 */
	BatchAutoDiscoveryIps queryNetSegmentById(Long id);

	/**
	 * 根据ID获取标签
	 * 
	 * @param id
	 * @return
	 */
	BatchAutoDiscoverySnmpConfig querySnmpConfigById(Long id);

	/**
	 * 修改单个ip段扫描开关
	 * 
	 * @param batchAutoDiscoveryIps
	 */
	void updateAutoDiscovery(BatchAutoDiscoveryIps batchAutoDiscoveryIps);
}
