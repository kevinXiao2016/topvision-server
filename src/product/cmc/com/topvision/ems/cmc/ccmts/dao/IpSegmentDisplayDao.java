package com.topvision.ems.cmc.ccmts.dao;

import java.util.List;
import java.util.Map;

import com.topvision.ems.cmc.ccmts.domain.TopoFolderDisplayInfo;
import com.topvision.ems.cmc.ccmts.facade.domain.CmcAttribute;
import com.topvision.ems.network.domain.EntitySnap;
import com.topvision.framework.dao.BaseEntityDao;

public interface IpSegmentDisplayDao extends BaseEntityDao<CmcAttribute> {

    /**
     * 获得CMC列表
     * @param params
     * @return
     */
    List<CmcAttribute> getDeviceListByIpSegment(Map<String, Object> params);

    /**
     * 获取设备数量
     * @param params
     * @return
     */
    Integer getDeviceListNum(Map<String, Object> params);

    /**
     * 获得CMC列表
     * @param params
     * @return
     */
    List<CmcAttribute> getDeviceListByFolder(Map<String, Object> params);

    /**
     * 获取设备数量
     * @param params
     * @return
     */
    Integer getDeviceListNumByFolder(Map<String, Object> params);

    /**
     * 根据地域Id查找设备
     * @param folderId
     * @return
     */
    List<EntitySnap> queryDeviceByFolderList(Long folderId);

    /**
     * 查询无地域下的设备
     * @return
     */
    List<EntitySnap> queryNoAreaDevice();

    /**
     * 查询所有地域下的统计信息
     * @return
     */
    TopoFolderDisplayInfo queryAreaDeviceTotalInfo();

    /**
     * 查询指定地域下的设备统计信息
     * @return
     */
    TopoFolderDisplayInfo queryTopoDisplayInfo(Long superiorId);

    /**
     * 查询无地域下设备统计信息
     * @return
     */
    TopoFolderDisplayInfo queryNoAreaTotalInfo();

}
