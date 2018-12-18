package com.topvision.ems.network.service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.facade.domain.EntityAttribute;
import com.topvision.ems.facade.domain.EntityType;
import com.topvision.ems.facade.domain.PingResultsEntry;
import com.topvision.ems.facade.domain.Snap;
import com.topvision.ems.network.domain.EntityAddress;
import com.topvision.ems.network.domain.EntitySnap;
import com.topvision.exception.service.ExistEntityException;
import com.topvision.framework.domain.Page;
import com.topvision.framework.domain.PageData;
import com.topvision.framework.event.MyResultHandler;
import com.topvision.framework.service.Service;
import com.topvision.framework.snmp.SnmpParam;
import com.topvision.platform.domain.SystemLog;
import com.topvision.platform.domain.User;

public interface EntityService extends Service {

    /**
     * 清除缓存
     * 
     * @param entityId
     */
    void clearCache(Long entityId);

    /**
     * 获取代理设备状态.
     * 
     * @param P
     * @return
     */
    PageData<EntitySnap> getAgentEntityStateByPage(Page P);

    /**
     * 获取所有设备模板.
     * 
     * @return
     */
    List<EntityType> getAllEntityType();

    /**
     * 获取不在回收站中的某页支持Agent的设备.
     * 
     * @param p
     * @return
     */
    PageData<Entity> getEntityAgentSupported(Page p);

    /**
     * 获取给定设备id的所有设备属性.
     * 
     * @param entityId
     * @return
     */
    List<EntityAttribute> getEntityAttribute(Long entityId);

    /**
     * 获取给定设备id的所有设备属性.
     * 
     * @param entityId
     * @return
     */
    List<EntityAttribute> getEntityAttribute(Long entityId, String group);

    /**
     * 得到每个IP对应的设备名称.
     * 
     * @param ips
     * @return
     */
    List<Entity> getEntityByIp(List<String> ips);

    /**
     * 获取给定类型的所有设备.
     * 
     * @param typeId
     * @return
     */
    List<Entity> getEntityByType(Long typeId);

    /**
     * 获取指定类型的设备,与地域无关
     * 
     * @param type
     * @return
     */
    List<Entity> getEntityListByType(Long type);

    /**
     * 根据子类型获取给定类型的所有设备.
     * 
     * @param typeId
     * @return
     */
    List<Entity> getEntityByTypeId(Long typeId);

    /**
     * 获取不在回收站中的指定类型的某页设备.
     * 
     * @param p
     * @param typeId
     * @return
     */
    PageData<Entity> getEntityByType(Page p, Long typeId);

    /**
     * 获取不在回收站中的所有设备.
     * 
     * @return
     */
    List<Entity> getEntity();

    /**
     * 获取给定设备ID的设备.
     * 
     * @param entityId
     * @return
     */
    Entity getEntity(Long entityId);

    /**
     * 获取在某个子文件夹中给定设备ID的设备信息.
     * 
     * @param entityId
     * @param folderId
     * @return
     */
    Entity getEntity(Long entityId, Long folderId);

    /**
     * 获取不在回收站中某页设备.
     * 
     * @param p
     * @return
     */
    PageData<Entity> getEntity(Page p);

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
     * 获取某个对应IP的设备.
     * 
     * @param ip
     * @return
     */
    Entity getEntityByIp(String ip);

    /**
     * 获取某个设备的下级设备
     * 
     * @param entityId
     * @return
     */
    List<Entity> getSubEntityByEntityId(Long entityId);

    /**
     * 获取某个ip对应的entity id。
     * 
     * @param ip
     * @return
     */
    Long getEntityIdByIp(String ip);

    /**
     * 获取孤立的设备(不在拓扑文件夹中的设备)
     * 
     * @param page
     * @return
     */
    PageData<Entity> getEntityInLonely(Page page);

    /**
     * 获取脱管的设备(被取消管理的设备)
     * 
     * @param page
     * @return
     */
    List<Entity> getEntityInOffManagement();

    /**
     * 获取设备基本信息.
     * 
     * @param p
     * @return
     */
    PageData<Entity> getEntitySnap(Page p);

    /**
     * 获取不在回收站中的某页支持SNMP设备.
     * 
     * @param p
     * @return
     */
    PageData<Entity> getEntitySnmpSupported(Page p);

    /**
     * 获取设备状态.
     * 
     * @param P
     * @return
     */
    PageData<EntitySnap> getEntityStateByPage(Page P);

    /**
     * 获取给定类型的SNMP设备.
     * 
     * @param typeId
     * @return
     */
    List<Entity> getSnmpEntityByType(Long typeId);

    /**
     * 获取Snmp设备状态.
     * 
     * @param P
     * @return
     */
    PageData<EntitySnap> getSnmpEntityStateByPage(Page P);

    /**
     * 得到给定设备ID的SNMP参数.
     * 
     * @param entityId
     * @return
     */
    SnmpParam getSnmpParamByEntity(Long entityId);

    /**
     * 获取类型设备状态.
     * 
     * @param P
     * @param typeId
     * @return
     */
    PageData<EntitySnap> getTypeEntityStateByPage(Page P, Long typeId);

    /**
     * 获取指定条件设备基本信息.
     * 
     * @param p
     * @param map
     * @param handler
     */
    void handleEntitySnap(Page p, Map<String, String> map, MyResultHandler handler);

    /**
     * 动态查询给定条件的设备, key为folderId, typeId, snmpSupport等。
     * 
     * @param map
     * @return
     */
    List<Entity> queryEntity(Map<String, String> map);

    /**
     * 获取满足给定条件的设备信息.
     * 
     * @param map
     * @param p
     * @return
     */
    PageData<Entity> queryEntity(Map<String, String> map, Page p);

    /**
     * 通过拓扑文件夹ID和设备类型查询设备.
     * 
     * @param folderId
     * @param typeId
     * @return
     */
    List<Entity> queryEntityByFolderIdAndType(Long folderId, Long typeId, Boolean snmpSupport);

    /**
     * 按照给定的查询条件查询设备, 该方法为AutoComplete, 最大返回10条记录.
     * 
     * @param query
     * @return
     */
    List<Entity> queryEntityForAc(String query);

    /**
     * 物理删除设备.
     * 
     * @param entityIds
     */
    void removeEntity(List<Long> entityIds);

    /**
     * 恢复设备的最后一次状态, 用于演示版本有数据.
     * 
     * @param handler
     */
    void restoreEntityCurrentState(MyResultHandler handler);

    /**
     * 取消或者激活设备的管理, enable为true表示激活, false表示取消.
     * 
     * @param entityIds
     */
    void txCancelManagement(List<Long> entityIds, Boolean enable);

    /**
     * 创建设备.
     * 
     * @param entity
     */
    void txCreateEntity(Entity entity) throws ExistEntityException;

    // TODO
    /**
     * 创建设备后发送消息
     * 
     * @param entity
     */
    void txCreateMessage(Entity entity);

    /**
     * 清空所有孤立的设备, 即物理删除.
     * 
     * @param sysLog
     */
    void txEmptyRecyle(SystemLog sysLog);

    /**
     * 将设备放入回收站.
     * 
     * @param entityIds
     */
    void txMoveEntityToRecyle(List<Long> entityIds);

    /**
     * 更新指定设备fixed信息.
     * 
     * @param entity
     */
    void updateEntityFixed(Entity entity);

    /**
     * 批量更新指定设备fixed信息.
     * 
     * 
     * @param entities
     */
    void updateEntityFixed(List<Entity> entities);

    /**
     * 更新设备的概要信息:ip, 位置, 负责人和描述.
     * 
     * @param entity
     * @throws ExistEntityException
     */
    void updateEntityOutline(Entity entity) throws ExistEntityException;

    /**
     * 修改设备的类型.
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
     * 获得设备责任人列表
     * 
     * @param entityId
     * @return
     */
    List<User> getEntityRelationUsers(Long entityId);

    /**
     * 更新设备责任人
     * 
     * @param entityId
     * @param userIds
     */
    void updateEntityRelationUsers(Long entityId, String[] userIds);

    /**
     * 查找设备总量
     * 
     * @param map
     * @return
     */
    int loadEntitySnapCount(Map<String, Object> map);

    /**
     * 加载所有的设备信息
     * 
     * @return
     */
    List<Snap> loadEntitySnapList(Map<String, Object> map);

    /**
     * 
     * @param map
     * @return
     */
    List<Entity> loadEntityList(Map<String, String> map);

    Long getEntityListCount(Map<String, String> map);

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
     * Add by Rod EMS-3816
     * 
     * 添加设备IP到EntityAddress表
     * 
     * @param entityId
     * @param entityAddresses
     */
    void insertEntityAddress(Long entityId, List<EntityAddress> entityAddresses);

    /**
     * 拓扑Entity的typeId
     * 
     * @param snmpParam
     */
    EntityType topoEntityTypeId(Entity entity, SnmpParam snmpParam);

    /**
     * Topo Entity SysName
     * 
     * @param entity
     * @param snmpParam
     * @return
     */
    String topoEntitySysName(Entity entity, SnmpParam snmpParam);

    List<Entity> getEntityJson();

    /**
     * 获得单个用户的权限设备
     * 
     * @param user
     * @return
     */
    List<Entity> getUserAuthorityEntity(User user);

    /**
     * 获得设备软件版本
     * 
     * @param entityId
     * @return
     */
    String getDeviceVersion(Long entityId);

    /**
     * 获得设备软件版本
     * 
     * @param entityId
     * @return
     */
    String getDeviceVersion(Entity entity);

    /**
     * 修改设备IP信息
     * 
     * @param entityId
     * @param oldIp
     * @param newIp
     * @return
     */
    String updateEntityIpInfo(Long entityId, String oldIp, String newIp);

    /**
     * 获得Entity
     * 
     * @param entityId
     * @param ip
     * @return
     */
    Entity selectEntityId(Long entityId, String ip);

    /**
     * 获得EntityAddress
     * 
     * @param entityId
     * @param ip
     * @return
     */
    List<EntityAddress> selectEntityAddressList(String ip);

    /**
     * 修改设备别名
     * 
     * @param entity
     */
    void renameEntity(Long entityId, String name);

    /**
     * 修改设备信息
     * 
     * @param name,location,contact
     * 
     */
    void modifyEntityInfo(Long entityId, String name, String location, String contact, String note);

    /**
     * 更新EntityAddress
     * 
     * @param entityId
     */
    void updateEntityAddress(Long entityId);

    /**
     * 查询所有的局端设备 目前是Olt和8800B
     * 
     * @return
     */
    List<Entity> getCentralEntity();

    /**
     * 此方法很特殊，请注意！！！！！
     * 
     * 执行采集端系统的命令行ping/tracert，此方法可以多次调用，如果ip为空表示获取上一次的值，如果ip不为空，则进行新的ping命令。命令结束后悔在返回字符串最后附加一个#OK#
     * 字符串。
     * 
     * @param sessionId
     *            客户端的sessionId，可以支持多个client端同时ping/tracert，但是单个客户端只支持一个ping/tracert，
     *            存在风险是一个客户端开多个浏览器窗口同时ping/tracert只执行一个。
     * @param ip
     *            目标IP
     * @return 操作系统执行ping/tracert命令的返回结果
     */
    String cmd(String sessionId, String cmd, String ip);

    /**
     * Snmp Ping
     * 
     * @param entityId
     * @param sessionId
     * @param cmd
     * @param ip
     * @return
     */
    PingResultsEntry snmpPing(Long entityId, String ip, Integer pingCount, Integer pingTimeout);

    /**
     * 获取用户权限下的指定设备类型的ID列表
     * 
     * @param userId
     * @return
     */
    List<Long> getEntityIdsByAuthority(Long type);

    /**
     * 判断指定模块的设备数量是否超过License
     * 
     * @param type
     * @return
     */
    boolean checkEntityLimitInLicense(Long type);

    /**
     * 获得设备IP列表
     * 
     * @param entityId
     * @return
     */
    List<String> getEntityIpList(Long entityId);

    /**
     * 获得设备快照信息
     * 
     * @param entityId
     */
    EntitySnap getEntitySnapById(Long entityId);

    /**
     * 修改SNMP全局配置应用到设备
     * 
     * @param timeout
     * @param retry
     * @param port
     */
    void applySnmpConfig(Integer timeout, byte retry, Integer port);

    /**
     * 获取全局SnmpParam
     * 
     * @return
     */
    SnmpParam getCmSnmpParam();

    /**
     * 修改全局SnmpParam
     * 
     * @param snmpParam
     */
    void modifyCmSnmpParam(SnmpParam snmpParam);

    /**
     * 根据设备地址和类型获取设备
     * 
     * @return
     */
    Entity getEntityWithMacAndTypeId(String macAddress, Long typeId);
    
    List<Entity> getEntityListWithMacAndTypeId(String macAddress, Long typeId);

    /**
     * 替换设备
     * 
     * @param preEntityId
     * @param ip
     */
    void replaceEntity(Long preEntityId, String ip);

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
     * 设置该用户指定设备的所在地域
     * 
     * @param userId
     * @param entityId
     * @param folderList
     */
    void setEntityLocatedFolders(Long userId, Long entityId, List<Long> folderList);

    /**
     * 批量配置设备所在的地域
     * 
     * @param userId
     * @param entityIds
     * @param folderList
     */
    void setEntitiesLocatedFolders(long userId, List<Long> entityIds, List<Long> folderList);

    /**
     * 将指定设备加入到指定地域中去
     * 
     * @param entityIds
     * @param folderIds
     */
    void addEntitiesToFolders(List<Long> entityIds, List<Long> folderIds);

    /**
     * 将指定设备从指定地域中移出
     * 
     * @param entityIds
     * @param folderIds
     */
    void removeEntitiesFromFolders(List<Long> entityIds, List<Long> folderIds);

    /**
     * 判断指定设备能否被移出所选地域
     * 
     * @param entityId
     * @return
     */
    Boolean canRemoveEntityFromFolder(Long entityId, List<Long> folderIds);

    /**
     * 根据设备ID列表获取设备名称列表
     * 
     * @param entityIds
     * @return
     */
    List<String> getEntityNamesByIds(List<Long> entityIds);

    Map<Long, Entity> getEntityCaches();

    Entity getEntityFromCache(Long entityId);

    Map<Long, SnmpParam> getSnmpParamCaches();

    void updateEntitySnap(EntitySnap entitySnap);

    /**
     * 加载用户关注设备列表
     * 
     * @return
     */
    List<Snap> loadUserAttentionList(Map<String, Object> map);

    int userAttentionCount(Map<String, Object> map);

    /**
     * 获取 entity 注意使用这个方法时是直接从数据库中获取,不会使用缓存
     * 
     * @param entity
     *            设备实体
     */
    Entity getEntityFromDB(Long entityId);

    /**
     * 获取有IP的entity
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
     * 修改cc设备联系人和位置
     * 
     * @param cmcId,ccSysLocation,ccSysContact
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
     * 获取ONU服务等级
     * 
     * @param entityId
     * @return
     */
    Integer getOnuLevel(Long entityId);
}
