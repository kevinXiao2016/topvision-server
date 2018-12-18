/***********************************************************************
 * $Id: OltDao.java,v1.0 2013-10-25 上午10:00:52 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.olt.dao;

import java.util.List;
import java.util.Map;

import com.topvision.ems.epon.domain.CcmtsFftMonitorScalar;
import com.topvision.ems.epon.domain.DeviceListItem;
import com.topvision.ems.epon.domain.DeviceLocation;
import com.topvision.ems.epon.domain.Olt;
import com.topvision.ems.epon.olt.domain.OltAttribute;
import com.topvision.ems.epon.olt.domain.OltMacAddressLearnTable;
import com.topvision.ems.epon.olt.domain.TopOnuGlobalCfgMgmt;
import com.topvision.ems.epon.olt.domain.TopSysFileDirEntry;
import com.topvision.ems.epon.realtime.domain.OltBaseInfo;
import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.facade.domain.EntityAttribute;
import com.topvision.ems.network.domain.EntitySnap;
import com.topvision.ems.network.domain.SubDeviceCount;
import com.topvision.framework.dao.BaseEntityDao;

/**
 * @author flack
 * @created @2013-10-25-上午10:00:52
 *
 */
public interface OltDao extends BaseEntityDao<Object> {
    /**
     * 获得OLT的基本属性
     * 
     * @param entityId
     *            设备ID
     * @return
     */
    OltAttribute getOltAttribute(Long entityId);

    /**
     * 修改OLT设备信息
     * 
     * @param entityId
     *            设备ID
     * 
     */
    void updateOltBaseInfo(Long entityId, String oltName, Integer rackNum, Integer frameNum);

    /**
     * 获取所有设备的位置信息
     * 
     * @return
     */
    List<DeviceLocation> getAllDeviceLocation();

    /**
     * 更新OLT的基本信息
     * 
     * @param oltAttribute
     *            OLT属性
     */
    void updateOltAttribute(OltAttribute oltAttribute);

    /**
     * 修改设置在线时长
     * 
     * @param entityId
     * @param deviceUpTime
     */
    void updateOltDeviceUpTime(Long entityId, Long deviceUpTime);

    /**
     * 获得OLT设备列表
     * @param paramsMap
     * @return
     */
    List<OltAttribute> getOltList(Map<String, Object> paramsMap);

    /**
     * 获得OLT设备列表数目
     * @param paramsMap
     * @return
     */
    int getOltListCount(Map<String, Object> paramsMap);

    /**
     * 批量插入Olt mac地址表数据
     * 
     * @param oltMacLearnTableList
     * @param entityId
     */
    void batchInsertOltMacLearnTable(List<OltMacAddressLearnTable> oltMacLearnTableList, long entityId);

    /**
     * 从DB获取设备的Mac地址学习表
     * 
     * @param entityId
     * @return
     */
    List<OltMacAddressLearnTable> getOltMacLearnTableList(Long entityId);

    /**
     * Add by Rod
     * 
     * 更新OLT设备的在线时长
     * 
     * @param entityId
     * @param sysUpTime
     * @param collectTime
     */
    void addOltDeviceUpTime(Long entityId, Long sysUpTime, Long collectTime);

    /**
     * Add by Rod
     * 
     * 删除OLT设备的在线时长历史数据
     * 
     * @param entityId
     * @param collectTime
     */
    void cleanOltDeviceUpTime(Long entityId, Long collectTime);

    /**
     * Add by lzt
     * 
     * 获得OLT软件版本
     * 
     * @param entityId
     */
    String selectOltSoftVersion(Long entityId);

    /**
     * 获得OLT相应的快照信息
     * 
     * @param entityId
     *            设备ID
     * @return EntitySnap
     */
    EntitySnap getOltCurrentPerformance(Long entityId);

    /**
     * 插入更新OLT基本信息
     * 
     * @param oltAttribute
     *            OLT属性
     */
    void insertOrUpdateOltAttribute(OltAttribute oltAttribute);

    /**
     * 获取OLT结构关系
     * 
     * @param entityId
     *            设备ID
     * @return Olt
     */
    Olt getOltStructure(Long entityId);

    /**
     * 获取OLT基本信息
     * 
     * @param EntityId
     *            设备ID
     * @return Entity
     */
    Entity getEntity(Long EntityId);

    /**
      * 获得整个设备关系（以Map形式，包括关系所在表）
      * 
      * @param entityId
      * @return
      */
    Map<Long, String> getOltRelation(Long entityId);

    /**
     * 获得OLT端口关系
     * 
     * @param entityId
     *            设备ID
     * @return HashMap<Long, Long>
     */
    Map<Long, Long> getOltMap(Long entityId);

    /**
     * 删除OLT设备中不存在的关系
     * 
     * @param index
     * @param relationTable
     */
    //void deleteRelation(Long index, String relationTable);

    /**
     * 不应该使用的方法
     * @param folderId
     * @param type
     * @return
     */
    @Deprecated
    List<Long> getEntityByFolder(Long folderId, Long type);

    /**
     * 动态更新属性值
     * 
     * @param tableName
     * @param fieldName
     * @param fieldValue
     * @param primaryKey
     * @param primaryKeyValue
     */
    /*void updateMonitorValue(String tableName, String fieldName, Object fieldValue, String primaryKey,
            Long primaryKeyValue);*/

    /**
     * 得到所有处于管理中的设备清单
     * @param type
     * @return
     */
    List<Long> getIsManagedEntityList(Long type);

    /**
     * 获取设备dol状态
     * 
     * @param entityId
     * @return
     */
    String getEntityDolStatus(Long entityId);

    /**
     * 更新EntityAttribute(是否有外置Dol)
     * 
     * @param entityAttribute
     */
    void updateEntityDolStatus(EntityAttribute entityAttribute);

    /**
     * 获取指定条件的EPON设备列表
     * 
     * @param map
     * @return
     */
    List<DeviceListItem> getDeviceListItem(Map<String, Object> map);

    /**
     * CCMTS 频谱全局开关信息
     * @param ccmtsFftMonitorScalar
     */
    void insertCcmtsFftMonitorScalar(CcmtsFftMonitorScalar ccmtsFftMonitorScalar);

    /**
     * 获取CCMTS频谱全局开关
     * @param entityId
     * @return
     */
    CcmtsFftMonitorScalar queryCcmtsFftGbStatus(Long entityId);

    /**
     * 修改CCMTS频谱全局开关
     * @param ccmtsFftMonitorScalar
     */
    void updateCcmtsFftGbStatus(CcmtsFftMonitorScalar ccmtsFftMonitorScalar);

    /**
     * 获得所有的Monitor对象
     * 
     * @return
     */
    /*List<MonitorType> getMonitorTypes();*/

    /**
     * 更新Olt设备文件路径
     * @param fileDirList
     * @param entityId
     */
    void updateOltFileDir(List<TopSysFileDirEntry> fileDirList, Long entityId);

    /**
     * 获得Olt设备某种文件类型路径
     * @param fileType
     * @param entityId
     */
    TopSysFileDirEntry getOltFileDirEntry(Long entityId, Integer fileType);

    /**
     * 获取Olt下级设备统计信息
     * @return
     */
    SubDeviceCount querySubCountInfo(Long entityId);

    /**
     * 更新Olt基本信息
     * @param baseInfo
     */
    void updateOltBaseInfo(OltBaseInfo baseInfo);

    /**
     * 更新Olt设备版本信息
     * @param baseInfo
     */
    void updateOltSoftVersion(OltBaseInfo baseInfo);

    /**
     * 批量插入MTK ONU 关于全局 WLAN WAN CATV开关参数
     * @param onuGlobalCfgMgmts
     * @param entityId
     */
    void batchInsertTopOnuGlobalCfgMgmt(List<TopOnuGlobalCfgMgmt> onuGlobalCfgMgmts, Long entityId);

    /**
     * 获取MTK ONU 关于全局 WLAN WAN CATV开关参数
     * @param entityId
     * @return
     */
    List<TopOnuGlobalCfgMgmt> getTopOnuGlobalCfgMgmt(Long entityId);

    /**
     * 更新olt离线cm时间信息
     * @param
     * @return void
     */
    void updateOltClearTime(Integer oltClearTime, Long entityId);

    /**
     * 获取有onu的olt 
     * @return
     */
    List<Long> queryEntityIdOfOlt();
}
