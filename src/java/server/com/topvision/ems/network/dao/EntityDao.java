package com.topvision.ems.network.dao;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.facade.domain.Snap;
import com.topvision.ems.network.domain.EntityFolderRela;
import com.topvision.ems.network.domain.EntityImport;
import com.topvision.ems.network.domain.EntitySnap;
import com.topvision.framework.dao.BaseEntityDao;
import com.topvision.framework.domain.Page;
import com.topvision.framework.domain.PageData;
import com.topvision.framework.event.MyResultHandler;
import com.topvision.framework.snmp.SnmpParam;
import com.topvision.platform.domain.User;

/**
 * @author admin
 * @created @2017年1月3日-下午4:58:17
 */
public interface EntityDao extends BaseEntityDao<Entity> {
    /**
     * 删除给定拓扑文件夹ID的设备关联.
     *
     * @param entities
     */
    void deleteEntity(List<Entity> entities);

    /**
     * 取消管理.
     *
     * @param entityId
     */
    void disableManagement(Long entityId);

    /**
     * 清除脱管设备.
     */
    void emptyRecyle();

    /**
     * 激活管理.
     *
     * @param entityId
     */
    void enableManagement(List<Long> entityIds);

    /**
     * 获取不在回收站中的指定页支持Agent的设备.
     *
     * @param p
     * @return
     */
    PageData<Entity> getEntityAgentSupported(Page p);

    /**
     * 获取指定IP的设备信息.
     *
     * @param ips
     * @return
     */
    List<Entity> getEntityByIp(List<String> ips);

    /**
     * 获取某个设备的下级设备
     *
     * @param entityId
     * @return
     */
    List<Entity> getSubEntityByEntityId(Long entityId);

    /**
     * 获取给定类型的设备.
     *
     * @param type
     * @return
     */
    List<Entity> getEntityByType(Long type);

    /**
     * 获取指定类型的设备,与地域无关
     *
     * @param type
     * @return
     */
    List<Entity> getEntityListByType(Long type);

    /**
     * 获取不在回收站中的指定类型的指定页设备.
     *
     * @param p
     * @param typeId
     * @return
     */
    PageData<Entity> getEntityByType(Page p, Long typeId);

    /**
     * 获取给定拓扑文件下的设备和子文件夹的坐标信息.
     *
     * @param folderId
     * @return
     */
    List<Entity> getEntityCoordByFolderId(Long folderId);

    /**
     * 获取设备总数.
     *
     * @return
     */
    Long getEntityCount();

    /**
     * 获取给定拓扑文件夹下给定ID的设备.
     *
     * @param ids
     * @param folderId
     * @return
     */
    List<Entity> getEntityByFolderId(List<Long> ids, Long folderId);

    /**
     * 获取某个拓扑文件夹下的设备, 并按给定的名称进行排序.
     *
     * @param folderId
     * @param sortName
     * @return
     */
    List<Entity> getEntityByFolderId(Long folderId, String sortName);

    /**
     * 获取某个拓扑文件夹及子文件夹下的设备
     *
     * @param folderId
     * @return
     */
    List<Entity> getAllEntityByFolderId(Long folderId);

    /**
     * 获取给定IP的设备.
     *
     * @param ip
     * @return
     */
    Entity getEntityByIp(String ip);

    /**
     * 获取指定设备信息.
     *
     * @param entity
     * @return
     */
    Entity getEntityInFolder(Entity entity);

    /**
     * 获取指定拓扑文件夹下的所有设备ID.
     *
     * @param folderId
     * @return
     */
    List<Long> getEntityIdByFolderId(Long folderId);

    /**
     * 获取孤立的设备(不在拓扑文件夹中的设备).
     *
     * @return
     */
    List<Entity> getEntityInLonely();

    /**
     * 获取指定页的孤立设备(不在拓扑文件夹中的设备).
     *
     * @param page
     * @return
     */
    PageData<Entity> getEntityInLonely(Page page);

    /**
     * 获取脱管的设备(被取消管理的设备).
     *
     * @return
     */
    List<Entity> getEntityInOffManagement();

    /**
     * 获取设备基本信息.
     *
     * @param page
     * @return
     */
    PageData<Entity> getEntitySnap(Page page);

    /**
     * 获取设备基本信息.
     *
     * @param page
     * @return
     */
    EntitySnap getEntitySnapById(Long entityId);

    /**
     * 获取不在回收站中的指定页支持SNMP协议的设备.
     *
     * @param p
     * @return
     */
    PageData<Entity> getEntitySnmpSupported(Page p);

    /**
     * 获取给定类型的支持SNMP协议的设备.
     *
     * @param typeId
     * @return
     */
    List<Entity> getSnmpEntityByType(Long typeId);

    /**
     * 获取给定设备ID的SNMP参数.
     *
     * @param entityId
     * @return
     */
    SnmpParam getSnmpParamByEntityId(Long entityId);

    /**
     * 获取指定条件设备基本信息.
     *
     * @param p
     * @param map
     * @param handler
     */
    void handleEntitySnap(Page p, Map<String, String> map, MyResultHandler handler);

    /**
     * 获取全部设备基本信息.
     *
     * @param handler
     */
    void handleEntitySnap(MyResultHandler handler);

    /**
     * 插入设备到拓扑文件夹.
     *
     * @param entity
     */
    @Override
    void insertEntity(Entity entity);

    /**
     * 下级设备数据处理
     *
     * @param entity
     * @param onuOperationStatus
     */
    Long insertEntityWithId(Entity entity, Integer onuOperationStatus);

    /**
     * 同步下级设备权限数据
     *
     * @param entityIds
     */
    void syncOnuAuthority(List<Long> entityIds);

    /**
     * 批量插入设备到拓扑文件夹.
     *
     * @param entities
     * @return
     */
    @Override
    void insertEntity(List<Entity> entities);

    /**
     * copy设备到拓扑文件夹.
     *
     * @param entity
     */
    void copyEntity(Entity entity);

    /**
     * 批量copy设备到拓扑文件夹.
     *
     * @param entities
     * @return
     */
    void copyEntity(List<Entity> entities);

    /**
     * 移动给定的ID的设备到目的文件夹.
     *
     * @param srcFolderId
     * @param destFolderId
     * @param entityIds
     */
    void moveEntity(Long srcFolderId, Long destFolderId, List<Long> entityIds);

    /**
     * 清除拓扑文件夹下给定设备ID对应的设备.
     *
     * @param entityIds
     */
    void moveEntityToRecyle(List<Long> entityIds);

    /**
     * 获取满足给定条件的设备信息.
     *
     * @param map
     * @param p
     * @return
     */
    PageData<Entity> queryEntity(Map<String, String> map, Page p);

    /**
     * 获取指定拓扑文件夹ID及类型ID的设备信息.
     *
     * @param map
     * @return
     */
    List<Entity> queryEntityByFolderIdAndType(Map<String, String> map);

    /**
     * 获取满足指定条件的设备信息.
     *
     * @param query
     * @return
     */
    List<Entity> queryEntityForAc(String query);

    /**
     * 更新指定设备fixed信息.
     *
     * @param entity
     */
    void updateEntityFixed(Entity entity);

    /**
     * 批量更新指定设备fixed信息.
     *
     * @param entities
     */
    void updateEntityFixed(List<Entity> entities);

    /**
     * 更新指定设备的组ID.
     *
     * @param entity
     */
    void updateEntityGroup(Entity entity);

    /**
     * 更新指定设备图标信息.
     *
     * @param entity
     */
    void updateEntityIcon(Entity entity);

    /**
     * 更新指定设备基本信息.
     *
     * @param entity
     */
    void updateEntityOutline(Entity entity);

    /**
     * 更新指定设备的类型信息.
     *
     * @param entity
     */
    void updateEntityType(Entity entity);

    /**
     * 更新指定设备的URL信息.
     *
     * @param entity
     */
    void updateEntityUrl(Entity entity);

    /**
     * 更新SNMP参数.
     *
     * @param snmpParam
     */
    void updateSnmpParam(SnmpParam snmpParam);

    /**
     * 修改SNMP全局配置应用到设备
     *
     * @param snmpParam
     */
    void updateSnmpConfig(SnmpParam snmpParam);

    /**
     * 修改SNMP V2参数
     *
     * @param snmpParam
     */
    void updateSnmpV2Param(SnmpParam snmpParam);

    /**
     * 移动孤立的设备文件夹中的设备
     */
    void txMoveEntity(final List<Entity> entities);

    /**
     * 获得设备责任人列表
     */
    List<User> getEntityRelationUsers(Long entityId);

    /**
     * 更新设备责任人
     *
     * @param userIds
     * @param entityId
     */
    void updateRelationUser(Long entityId, List<Long> userIds);

    /**
     * 删除设备责任人
     *
     * @param entityId
     */
    void deleteRelationUser(Long entityId);

    /**
     * 更新设备快照信息，CPU利用率、内存利用率等
     *
     * @param snap
     *            设备快照
     */
    void updateEntityPerf(EntitySnap snap);

    /**
     * 修改设备信息
     *
     * @param entityId
     * @param name
     */
    void modifyEntityInfo(Long entityId, String name, String location, String contact, String note);

    /**
     * 重命名设备别名
     *
     * @param entityId
     * @param name
     */
    void renameEntity(Long entityId, String name);

    /**
     * 获取设备的刷新状态
     *
     * @param entityId
     * @return
     */
    Integer getRefreshStatus(Long entityId);

    /**
     * 更新设备的刷新状态
     *
     * @param entityId
     * @param status
     */
    void updateRefreshStatus(Long entityId, Integer status);

    /**
     * 重置设备表的刷新状态
     */
    void resetRefreshStatus();

    /**
     * 加载所有的设备信息
     *
     * @param map
     * @return
     */
    List<Snap> loadEntitySnapList(Map<String, Object> map);

    /**
     * 查找设备总量
     *
     * @param map
     * @return
     */
    int loadEntitySnapCount(Map<String, Object> map);

    /**
     * 取消关注
     *
     * @param userId
     * @param entityId
     */
    void cancelAttention(long userId, long entityId);

    /**
     * 添加关注
     *
     * @param userId
     * @param entityId
     */
    void pushAttention(long userId, long entityId);

    /**
     * 加载关注设备列表
     *
     * @param map
     * @return
     */
    List<Snap> getAttentionEntityList(Map<String, String> map);

    /**
     * 新增EntitySnap记录
     *
     * @param entitySnap
     */
    void insertEntitySnap(EntitySnap entitySnap);

    void updateEntitySnap(EntitySnap entitySnap);

    /**
     * 判断设备是否在拓扑图上存在
     *
     * @param productId
     * @param productType
     * @param folderId
     * @return boolean
     */
    boolean isProductExistsInTopo(Long productId, Long productType, Long folderId);

    /**
     * 更新设备类型信息
     *
     * @param entityId
     */
    void updateEntityType(Long entityId, Long typeId);

    List<Entity> selectEntityFromView();

    /**
     * 获得单个用户的权限设备
     *
     * @param user
     * @return
     */
    List<Entity> getUserAuthorityEntity(String authorityView);

    /**
     * 获得设备软件版本
     *
     * @param entityId
     * @param type
     * @return
     */
    String getDeviceVersion(Long entityId);

    /**
     * 修改设备IP地址
     *
     * @param entityId
     * @param ipAddress
     */
    void updateEntityIpAddress(Long entityId, String ip);

    /**
     * 修改设备IP地表
     *
     * @param entityId
     * @param ipAddress
     */
    void updateEntityAddressTable(Long entityId, String ip);

    /**
     * 获得Entity
     *
     * @param entityId
     * @param ip
     * @return
     */
    Entity selectEntityByIp(Long entityId, String ip);

    /**
     * @param ip
     * @return Entity
     */
    Entity selectEntityBySingleIp(String ip);

    /**
     * 通过Mac地址查询相应的CC
     *
     * @param mac
     * @return
     */
    Long selectEntityByMac(String mac);

    /**
     * 通过PING操作更新设备快照在线状态
     *
     * @param snap
     */
    void updateEntityState(EntitySnap snap);

    /**
     * 根据子类型获取给定类型的所有设备.
     *
     * @param typeId
     * @return
     */
    List<Entity> getEntityByTypeId(Long typeId);

    /**
     * 查询所有的局端设备 目前是Olt和8800B
     *
     * @return
     */
    List<Entity> selectCentralEntity();

    /**
     * 获取指定type的所有设备ID
     *
     * @param type
     * @return
     */
    List<Long> selectEntityIdsByType(Long type);

    /**
     * 获取用户权限下的所有CCMTS/CMTS的ID列表
     *
     * @param userId
     * @return
     */
    List<Long> getEntityIdsByAuthority(Long type);

    /**
     * 改变设备的地域
     *
     * @param entityId
     * @param destFolderId
     */
    void moveEntityForBatchDiscovery(Long entityId, List<Long> destFolderId);

    /**
     * 查询所有的未知类型设备
     *
     * @return
     */
    List<Entity> getAllUnknownEntity();

    /**
     * 查询设备设置的IP列表
     *
     * @return List<String>
     */
    List<String> getEntityIpListById(Long entityId);

    /**
     * 更新ONU在线状态
     *
     * @param entitySnap
     */
    void updateOnuEntitySnap(EntitySnap entitySnap);

    /**
     * 更新设备的设备名称
     *
     * @param entityId
     * @param deviceName
     */
    void updateEntityDeviceName(Long entityId, String deviceName);

    /**
     * 更新设备运行时长
     *
     * @param entityId
     * @param sysUptime
     */
    void updateSnapSysUptime(Long entityId, Long sysUptime);

    /**
     * 更新设备最后刷新时间
     *
     * @param entityId
     * @param lastRefreshTime
     */
    void updateEntityLastRefreshTime(Long entityId, Timestamp lastRefreshTime);

    /**
     * 更新设备的设备名称和mac地址
     *
     * @param entityId
     * @param deviceName
     * @param mac
     */
    void updateEntityNameAndMac(Long entityId, String deviceName, String mac);

    void reOrganizedAuthority(List<Long> entityIds);

    /**
     * 根据设备地址和类型获取设备
     *
     * @return
     */
    Entity getEntityWithMacAndTypeId(String macAddress, Long typeId);

    /**
     * 替换设备
     *
     * @param preEntityId
     * @param ip
     */
    void replaceEntity(Long preEntityId, String ip);

    /**
     * 替换设备
     *
     * @param preEntityId
     * @param ip
     * @param mac
     */
    void replaceEntity(Long preEntityId, String ip, String mac);

    /**
     * 设置设备所在地域
     *
     * @param entityId
     *            设备ID
     * @param folderList
     *            选中地域
     * @param authFolderIds
     *            该用户可操作地域范围
     */
    void setEntityLocatedFolders(Long entityId, List<Long> folderList, List<Long> authFolderIds);

    /**
     * 批量编辑设备所在地域
     *
     * @param entityIds
     *            设备ID列表
     * @param folderList
     *            选中地域
     * @param authFolderIds
     *            该用户可操作地域范围
     */
    void setEntitiesLocatedFolders(List<Long> entityIds, List<Long> folderList, List<Long> authFolderIds);

    /**
     * 批量将设备加入到指定地域中去
     *
     * @param entityIds
     * @param folderIds
     */
    void addEntitiesToFolders(List<Long> entityIds, List<Long> folderIds);

    /**
     * 批量将设备从指定地域中移出
     *
     * @param entityIds
     * @param folderIds
     */
    void removeEntitiesFromFolders(List<Long> entityIds, List<Long> folderIds);

    /**
     * 获取所有设备的导出信息
     *
     * @return
     */
    List<Entity> getEntityExportInfo(Map<String, Object> queryMap);

    /**
     * 获取设备所在地域信息
     *
     * @return
     */
    List<EntityFolderRela> getAllEntityFolders();

    /**
     * 获得符合条件的导出设备的数目
     *
     * @param queryMap
     * @return
     */
    Integer getExportEntityNum(Map<String, Object> queryMap);

    /**
     * 获取entitycache
     * 
     * @return
     */
    Map<Long, Entity> getEntityCaches();

    /**
     * 更新cache中的entity
     * 
     * @return
     */
    void updateEntityMac(Long entityId, String entityMac);

    void updateEntityParentId(Long entityId, Long parentId);

    /**
     * 通过类型删除添加到拓扑图的设备
     *
     * @param type
     *            类型
     */
    void deleteTopoEntityByEntityIdAndType(Long type);

    /**
     * 更新entity表中olt信息
     * 
     * @param entity
     */
    void updateEntityInfo(Entity entity);

    /**
     * 将设备从拓扑图上删除
     *
     * @param entityId
     */
    void removeProductFromTopoGraph(Long entityId);

    /**
     * 更新entity
     *
     * @param entity
     *            设备实体
     */
    void updateEntityConnectInfo(Entity entity);

    /**
     * 更新网管SNMP参数
     *
     * @param snmpParam
     *            网管SNMP参数
     */
    void updateEmsSnmpparam(SnmpParam snmpParam);

    /**
     * 获取SnmpParamcache
     * 
     * @return
     */
    Map<Long, SnmpParam> getSnmpParamCaches();

    /**
     * 获取 entity
     *
     * @param entity
     *            设备实体
     */
    Entity selectEntityFromDB(Long entityId);

    /**
     * 获取 entity
     *
     * @param entity
     *            设备实体
     */
    SnmpParam selectSnmpParamFromDB(Long entityId);

    /**
     * 更新设备名
     * 
     * @param entityImportList
     */
    void batchUpdateEntity(List<EntityImport> entityImportList);

    /**
     * 备份entity 别名
     */
    void entityAliasBak();

    /**
     * 获取用户关注设备列表
     */
    List<Snap> loadUserAttentionList(Map<String, Object> map);

    int userAttentionCount(Map<String, Object> map);

    /**
     * Select Histroy Alias Use to recreate entity alias
     * 
     * @param entityId
     * @param mac
     * @param index
     * @param sn
     * @return
     */
    String selectHistoryAlias(Long entityId, String mac, String index, String sn);

    /**
     * 获取有IP的Entity
     * 
     * @return
     */
    List<Entity> getEntityWithIp();

    /**
     * 获取指定设备在OLT上的index
     * 
     * @param entityId
     * @return
     */
    Long getEntityIndexOfOlt(Long entityId);

    /**
     * 查询entity是否在对应权限表
     * 
     * @param entityId
     * @param tableName
     * @return
     */
    Boolean isEntityInAuthorityTable(Long entityId, String tableName);

    /**
     * cc基本信息管理同步更新网管位置和联系人
     * 
     * @param
     * @return void
     */
    void modifyEntityContactAndLocation(Long cmcId, String ccSysLocation, String ccSysContact);

    /**
     * 获取ONU是EPON ONU还是GPON ONU
     * 
     * @param entityId
     * @return
     */
    String getOnuEorG(Long entityId);

    /**
     * 获取entity下在线CM数
     * 
     * @param entityId
     * @return
     */
    Long getEntityRelaOnlineCm(Long entityId);

    List<Entity> getEntityListWithMacAndTypeId(String macAddress, Long typeId);

    /**
     * 获取ONU的服务等级
     * 
     * @param entityId
     * @return
     */
    Integer getOnuLevel(Long entityId);
}
