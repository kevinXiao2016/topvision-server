/***********************************************************************
 * $Id: IpSegmentDisplayService.java,v1.0 2014-5-11 上午8:51:11 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.ccmts.service;

import java.util.List;

import com.topvision.ems.cmc.ccmts.domain.TopoFolderDisplayInfo;
import com.topvision.ems.cmc.ccmts.facade.domain.CmcAttribute;
import com.topvision.ems.network.domain.EntitySnap;
import com.topvision.ems.network.domain.IpSegmentInfo;
import com.topvision.framework.service.Service;

/**
 * @author flack
 * @created @2014-5-11-上午8:51:11
 * 
 */
public interface IpSegmentDisplayService extends Service {
    /**
     * 获取Ip段列表并查询对应设备信息
     * 
     * @return
     */
    public List<IpSegmentInfo> getIpSegmentDeviceInfoList();

    /**
     * 单独处理根结点的设备数据统计
     * 
     * @return
     */
    public IpSegmentInfo getRootDeviceInfo();

    /**
     * 根据Ip段查询对应的设备信息 在树结点使用
     * 
     * @param ipSegment
     * @return
     */
    public List<EntitySnap> getDeviceInfoByIpSegment(String ipSegment);

    /**
     * 根据Ip段查询对应的设备
     * 
     * @param ipSegment
     * @return
     */
    public List<CmcAttribute> getDeviceListByIpSegment(String ipSegment, Integer start, Integer limit, Boolean sevenDay);

    /**
     * 查询设备数量
     * 
     * @param ipSegment
     * @param start
     * @param limit
     * @return
     */
    public int getDeviceListNum(String ipSegment, Integer start, Integer limit, Boolean sevenDay);

    /**
     * 根据地域段查询对应的设备
     * 
     * @param folderId
     * @return
     */
    public List<CmcAttribute> getDeviceListByFolder(Long folderId, Integer start, Integer limit, Boolean sevenDay);

    /**
     * 查询设备数量
     * 
     * @param folderId
     * @param start
     * @param limit
     * @return
     */
    public int getDeviceListNumByFolder(Long folderId, Integer start, Integer limit, Boolean sevenDay);

    /**
     * 更新设备信息
     * 
     * @param entityId
     * @param folderId
     * @param name
     */
    public void updateDeviceInfo(Long entityId, Integer folderId, String name);

    /**
     * 根据地域Id查找设备
     * 
     * @param folderId
     * @return
     */
    public List<EntitySnap> getDeviceByFolderList(Long folderId);

    /**
     * 查询所有地域下的统计信息
     * 
     * @return
     */
    TopoFolderDisplayInfo getAreaDeviceTotalInfo();

    /**
     * 查询指定地域下的设备统计信息
     * 
     * @param superiorId
     * @return
     */
    List<TopoFolderDisplayInfo> getTopoDisplayInfo(Long superiorId);

    /**
     * 查询无地域下设备统计信息
     * 
     * @return
     */
    TopoFolderDisplayInfo getNoAreaTotalInfo();

    /**
     * 获取当前用户所在地域下的信息
     * 
     * @param folderId
     * @return
     */
    TopoFolderDisplayInfo getCurrentFolderInfo(Long folderId);

}
