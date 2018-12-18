/***********************************************************************
 * $Id: IpSegmentDisplayServiceImpl.java,v1.0 2014-5-11 上午8:52:26 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.ccmts.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.cmc.ccmts.dao.IpSegmentDisplayDao;
import com.topvision.ems.cmc.ccmts.domain.TopoFolderDisplayInfo;
import com.topvision.ems.cmc.ccmts.facade.domain.CmcAttribute;
import com.topvision.ems.cmc.ccmts.service.IpSegmentDisplayService;
import com.topvision.ems.cmc.util.CmcConstants;
import com.topvision.ems.network.dao.BatchAutoDiscoveryDao;
import com.topvision.ems.network.dao.EntityDao;
import com.topvision.ems.network.dao.TopoFolderDao;
import com.topvision.ems.network.domain.BatchAutoDiscoveryIps;
import com.topvision.ems.network.domain.EntitySnap;
import com.topvision.ems.network.domain.IpSegmentInfo;
import com.topvision.ems.network.domain.TopoFolder;
import com.topvision.framework.common.IpUtils;
import com.topvision.framework.service.BaseService;

/**
 * @author flack
 * @created @2014-5-11-上午8:52:26
 * 
 */
@Service("ipSegmentDisplayService")
public class IpSegmentDisplayServiceImpl extends BaseService implements IpSegmentDisplayService {
	@Autowired
	private IpSegmentDisplayDao ipsegmentDisplayDao;
	@Autowired
	private BatchAutoDiscoveryDao batchAutoDiscoveryDao;
	@Autowired
	private EntityDao entityDao;
	@Autowired
	private TopoFolderDao topoFolderDao;

	private final static Long SEVEN_TIME = 7 * 24 * 60 * 60L;

	@Override
	public List<IpSegmentInfo> getIpSegmentDeviceInfoList() {
		Map<String, Object> map = new HashMap<String, Object>();
		// 获取所有的IP段
		List<BatchAutoDiscoveryIps> discoveryIps = batchAutoDiscoveryDao.queryAllDiscoveryIps(map);
		List<IpSegmentInfo> deviceInfoList = new ArrayList<IpSegmentInfo>();
		for (BatchAutoDiscoveryIps topoIp : discoveryIps) {
			// 根据IP段解析出对应的IP列表
			List<String> ipList = IpUtils.parseIp(topoIp.getIpInfo());
			// 查询该列表下的设备信息
			IpSegmentInfo ipDeviceInfo = batchAutoDiscoveryDao.queryDeviceInfoByIps(ipList);
			ipDeviceInfo.setIpSegment(topoIp.getIpInfo());
			ipDeviceInfo.setDisplayName(topoIp.getName());
			deviceInfoList.add(ipDeviceInfo);
		}
		return deviceInfoList;
	}

	@Override
	public IpSegmentInfo getRootDeviceInfo() {
		return batchAutoDiscoveryDao.queryDeviceInfoByIps(null);
	}

	@Override
	public List<EntitySnap> getDeviceInfoByIpSegment(String ipSegment) {
		// 根据IP段解析出对应的IP列表
		List<String> ipList = IpUtils.parseIp(ipSegment);
		// 查询该列表下的设备信息
		List<EntitySnap> entityList = batchAutoDiscoveryDao.queryDeviceByIpSegment(ipList);
		return entityList;
	}

	@Override
	public List<CmcAttribute> getDeviceListByIpSegment(String ipSegment, Integer start, Integer limit,
			Boolean sevenDay) {
		Map<String, Object> params = new HashMap<String, Object>();
		List<String> ipList = null;
		if (ipSegment != null && !"".equals(ipSegment) && !"-1".equals(ipSegment)) {
			ipList = IpUtils.parseIp(ipSegment);
		}
		if (sevenDay) {
			params.put("upTime", SEVEN_TIME);
		} else {
			params.put("upTime", -1);
		}
		params.put("ipList", ipList);
		params.put("start", start);
		params.put("limit", limit);
		return ipsegmentDisplayDao.getDeviceListByIpSegment(params);
	}

	@Override
	public int getDeviceListNum(String ipSegment, Integer start, Integer limit, Boolean sevenDay) {
		Map<String, Object> params = new HashMap<String, Object>();
		List<String> ipList = null;
		if (ipSegment != null && !"".equals(ipSegment) && !"-1".equals(ipSegment)) {
			ipList = IpUtils.parseIp(ipSegment);
		}
		if (sevenDay) {
			params.put("upTime", SEVEN_TIME);
		} else {
			params.put("upTime", -1);
		}
		params.put("ipList", ipList);
		params.put("start", start);
		params.put("limit", limit);
		return ipsegmentDisplayDao.getDeviceListNum(params);
	}

	@Override
	public List<CmcAttribute> getDeviceListByFolder(Long folderId, Integer start, Integer limit, Boolean sevenDay) {
		Map<String, Object> params = new HashMap<String, Object>();
		if (sevenDay) {
			params.put("upTime", SEVEN_TIME);
		} else {
			params.put("upTime", -1);
		}
		params.put("folderId", folderId);
		params.put("folderTable", "t_entity_" + folderId);
		params.put("start", start);
		params.put("limit", limit);
		return ipsegmentDisplayDao.getDeviceListByFolder(params);
	}

	@Override
	public int getDeviceListNumByFolder(Long folderId, Integer start, Integer limit, Boolean sevenDay) {
		Map<String, Object> params = new HashMap<String, Object>();
		if (sevenDay) {
			params.put("upTime", SEVEN_TIME);
		} else {
			params.put("upTime", -1);
		}
		params.put("folderId", folderId);
		params.put("folderTable", "t_entity_" + folderId);
		params.put("start", start);
		params.put("limit", limit);
		return ipsegmentDisplayDao.getDeviceListNumByFolder(params);
	}

	@Override
	public void updateDeviceInfo(Long entityId, Integer folderId, String name) {
		// 修改设备别名
		entityDao.renameEntity(entityId, name);
		// 修改设备地域信息
		topoFolderDao.updateEntityTopoFolder(entityId, folderId);
	}

	@Override
	public List<EntitySnap> getDeviceByFolderList(Long folderId) {
		// 无地域的情况
		if (folderId == CmcConstants.NO_AREA_FLAG) {
			return ipsegmentDisplayDao.queryNoAreaDevice();
		} else {
			return ipsegmentDisplayDao.queryDeviceByFolderList(folderId);
		}
	}

	@Override
	public TopoFolderDisplayInfo getAreaDeviceTotalInfo() {
		return ipsegmentDisplayDao.queryAreaDeviceTotalInfo();
	}

	@Override
	public List<TopoFolderDisplayInfo> getTopoDisplayInfo(Long superiorId) {
		List<TopoFolder> folderList = topoFolderDao.getChildTopoFolder(superiorId);
		List<TopoFolderDisplayInfo> displayInfoList = new ArrayList<TopoFolderDisplayInfo>();
		TopoFolderDisplayInfo displayInfo = null;
		for (TopoFolder folder : folderList) {
			displayInfo = ipsegmentDisplayDao.queryTopoDisplayInfo(folder.getFolderId());
			displayInfo.setFolderId(folder.getFolderId());
			displayInfo.setSuperiorId(folder.getSuperiorId());
			displayInfo.setName(folder.getName());
			displayInfoList.add(displayInfo);
		}
		return displayInfoList;
	}

	public TopoFolderDisplayInfo getCurrentFolderInfo(Long folderId) {
		TopoFolder folder = topoFolderDao.selectByPrimaryKey(folderId);
		TopoFolderDisplayInfo displayInfo = ipsegmentDisplayDao.queryTopoDisplayInfo(folderId);
		displayInfo.setFolderId(folder.getFolderId());
		displayInfo.setSuperiorId(folder.getSuperiorId());
		displayInfo.setName(folder.getName());
		return displayInfo;
	}

	@Override
	public TopoFolderDisplayInfo getNoAreaTotalInfo() {
		return ipsegmentDisplayDao.queryNoAreaTotalInfo();
	}

}
