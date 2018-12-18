package com.topvision.ems.network.service;

import java.sql.SQLException;
import java.util.List;

import net.sf.json.JSONObject;

import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.network.domain.FolderRelation;
import com.topvision.ems.network.domain.Link;
import com.topvision.ems.network.domain.MapNode;
import com.topvision.ems.network.domain.ModuleParam;
import com.topvision.ems.network.domain.TopoFolder;
import com.topvision.ems.network.domain.TopoFolderEx;
import com.topvision.ems.network.domain.TopoFolderStat;
import com.topvision.ems.network.domain.TopoLabel;
import com.topvision.framework.event.MyResultHandler;
import com.topvision.framework.service.Service;
import com.topvision.platform.domain.SystemLog;

public interface TopologyService extends Service {
    /**
     * 排列拓扑图.
     * 
     * @param folderId
     * @param type
     * @return
     */
    List<Entity> arrangeTopoMap(Long folderId, Integer type, Integer mapWidth, Integer mapHeight);

    /**
     * 取消拓扑.
     * 
     * @param requestId
     */
    void cancelTopology(Long requestId);

    /**
     * 批量删除指定拓扑文件夹中的图形结点.
     * 
     * @param folderId
     * @param nodeIds
     */
    void deleteMapNode(Long folderId, List<Long> nodeIds);

    /**
     * 删除指定拓扑文件夹中指定的图形结点.
     * 
     * @param folderId
     * @param nodeId
     */
    void deleteMapNode(Long folderId, Long nodeId);

    /**
     * 得到某个拓扑文件夹中的子文件夹.
     * 
     * @param folderId
     * @return
     */
    List<TopoFolder> getChildTopoFolder(Long folderId);

    /**
     * 得到拓扑文件夹下的所有边.
     * 
     * @param folderId
     * @return
     */
    List<Link> getEdgeByFolderId(Long folderId);

    /**
     * 得到某个拓扑文件夹下的边的ID.
     * 
     * @param folderId
     * @return
     */
    List<Long> getEdgeIdByFolderId(Long folderId);

    /**
     * 得到某个拓扑文件中的所有设备ID.
     * 
     * @param folderId
     * @return
     */
    List<Long> getEntityIdByFolderId(Long folderId);

    /**
     * 获取当前的拓扑图标设备注类型.
     * 
     * @param folderId
     * @return
     */
    String getEntityLabelType(Long folderId);

    /**
     * 得到某个拓扑文件夹下的线路ID.
     * 
     * @param folderId
     * @return
     */
    List<Long> getLinkIdByFolderId(Long folderId);

    /**
     * 获取拓扑图的线路标注类型.
     * 
     * @param folderId
     * @return
     */
    String getLinkLabelType(Long folderId);

    /**
     * 获取给定结点ID的结点实体.
     * 
     * @param nodeId
     * @return
     */
    MapNode getMapNode(Long nodeId);

    /**
     * 根据拓扑文件夹ID得到拓扑文件夹实例.
     * 
     * @param folderId
     * @return
     */
    TopoFolder getTopoFolder(Long folderId);

    /**
     * 获取指定组ID下的拓扑图.
     * 
     * @param userGroupId
     * @return
     */
    List<TopoFolder> getTopoFolderByGroup(Long userGroupId);

    /**
     * 根据设备IP得到拓扑文件夹, 用于设备定位.
     * 
     * @param ip
     * @return
     */
    List<TopoFolderEx> getTopoFolderByIp(String ip);

    /**
     * 获取给定类型(cpu/mem/linkflow)的标注.
     * 
     * @param type
     * @return
     */
    List<TopoLabel> getTopoLabel(Long folderId, String type);

    /**
     * 获取拓扑文件夹中的结点.
     * 
     * @param folderId
     * @return
     */
    List<Entity> getVertexByFolderId(Long folderId);

    /**
     * 插入一个拓扑图结点.
     * 
     * @param mapNode
     */
    void insertMapNode(MapNode mapNode);

    /**
     * 以JSON格式获取拓扑文件夹中的所有的节点, 提供WEB拓扑图使用. key: entity, node, folder.
     * 
     * @param folderId
     * @return
     * @throws Exception
     */
    JSONObject loadJSONCellByFolderId(Long folderId) throws Exception;

    /**
     * 载入拓扑图上的绘图结点, 该结点是非设备和拓扑文件夹, 例如是直线, 标签, 矩形等.
     * 
     * @param folderId
     * @return
     */
    List<MapNode> loadMapNodeByFolderId(Long folderId);

    /**
     * 获取我的拓扑文件夹.
     * 
     * @param userId
     * @return
     */
    List<TopoFolder> loadMyTopoFolder(Long userId, Integer type);

    /**
     * 获取子网拓扑图.
     * 
     * @return
     */
    List<TopoFolder> loadSubnetFolder();

    /**
     * 获取所有的拓扑文件夹，及其子文件夹.
     * 
     * @return
     */
    List<TopoFolder> loadTopoFolder();

    /**
     * 获取所有的拓扑文件夹, 使用给定的行记录处理器处理.
     * 
     * @param handler
     */
    void loadTopoFolder(MyResultHandler handler);

    /**
     * 获取所有的拓扑图, 不带拓扑文件夹、远程系统。
     * 
     * @return
     */
    List<TopoFolder> loadTopoMap();

    /**
     * 刷新拓扑文件夹.
     * 
     * @param folderId
     */
    void refreshTopoFolder(Long folderId);

    /**
     * 逻辑删除设备, 将设备放入回收站. 如果entityIds为null，则只删除link(s)
     * 
     * @param entityIds
     * @param entityIds
     */
    List<Long> removeEntityAndLink(Long folerId, List<Long> entityIds, List<Long> linkIds);

    /**
     * 保存用户组的拓扑文件夹的权限.
     * 
     * @param userGroupId
     * @param folderIds
     * @param powers
     */
    void saveUserGroupFolderPower(Long userGroupId, List<Long> folderIds, List<Integer> powers);

    /**
     * 
     * @param node
     */
    void setMapNodeExpaned(MapNode node);

    /**
     * 统计各个拓扑文件中的设备数量.
     * 
     * @return
     * @throws Exception
     */
    List<TopoFolderStat> statTopoFolder() throws Exception;

    List<String> getColNames();

    /**
     * 注册计算拓扑图下相关资产个数的执行类
     * 
     * @param moduleParam
     */
    void registerTopoFolderNumModuleParam(ModuleParam moduleParam);

    /**
     * 粘贴设备到目标文件夹.
     * 
     * @param sysLog
     * @param srcFolderId
     * @param destFolderId
     * @param entities
     */
    List<Entity> txCopyEntity(SystemLog sysLog, Long srcFolderId, Long destFolderId, List<Long> entities);

    /**
     * 剪切设备到目标文件夹.
     * 
     * @param sysLog
     * @param srcFolderId
     * @param destFolderId
     * @param entities
     * @return
     */
    List<Entity> txCutEntity(SystemLog sysLog, Long srcFolderId, Long destFolderId, List<Long> entities);

    /**
     * 删除一个拓扑文件夹.
     * 
     * @param sysLog
     * @param folder
     */
    void txDeleteTopoFolder(SystemLog sysLog, TopoFolder folder);

    /**
     * 插入一个新的拓扑文件夹.
     * 
     * @param sysLog
     * @param folder
     * @param userGroupId
     * @throws SQLException
     */
    void txInsertTopoFolder(SystemLog sysLog, TopoFolder folder, Long userGroupId) throws SQLException;

    /**
     * 将源文件夹下设备关联移动到目的文件夹.
     * 
     * @param destFolderId
     * @param entities
     */
    void txMoveEntity(Long destFolderId, List<Long> entities);

    /**
     * 移动设备到目标文件夹.
     * 
     * @param sysLog
     * @param srcFolderId
     * @param destFolderId
     * @param entities
     */
    void txMoveEntity(SystemLog sysLog, Long srcFolderId, Long destFolderId, List<Long> entities);

    /**
     * 对设备进行重命名, 更改设备在拓扑图中的显示.
     * 
     * @param entity
     */
    void txRenameEntity(Entity entity);

    /**
     * 重命名给定的拓扑文件夹.
     * 
     * @param sysLog
     * @param folder
     */
    void txRenameTopoFolder(SystemLog sysLog, TopoFolder folder);

    /**
     * 保存拓扑图上的设备, 结点等坐标.
     * 
     * @param folderId
     * @param entities
     * @param nodes
     */
    void txSaveCoordinate(Long folderId, List<Entity> entities, List<MapNode> nodes);

    /**
     * 修改拓扑拓扑图的概要信息, 例如名称和备注.
     * 
     * @param folder
     */
    void txUpdateTopoFolderOutline(TopoFolder folder);

    /**
     * 修改拓扑图报警显示图标.
     * 
     * @param folder
     */
    void updateDisplayAlertIcon(TopoFolder folder);

    /**
     * 修改拓扑图显示信息.
     * 
     * @param topoFolder
     */
    void updateDisplayAllEntity(TopoFolder topoFolder);

    /**
     * 修改拓扑图DisplayCluetip.
     * 
     * @param folder
     */
    void updateDisplayCluetip(TopoFolder folder);

    /**
     * 修改拓扑图显示连接.
     * 
     * @param topoFolder
     */
    void updateDisplayLink(TopoFolder topoFolder);

    /**
     * 更新指定设备的组ID.
     * 
     * @param entity
     */
    void updateEntityGroup(Entity entity);

    /**
     * 更新指定设备拓扑文件夹图标信息.
     * 
     * @param entity
     */
    void updateEntityIcon(Entity entity);

    /**
     * 修改指定图形结点的虚线边界信息.
     * 
     * @param mapNode
     */
    void updateMapNodeDashedBorder(MapNode mapNode);

    /**
     * 修改指定图形结点的扩展信息.
     * 
     * @param mapNode
     */
    void updateMapNodeExpanded(MapNode mapNode);

    /**
     * 修改指定图形结点的填充颜色信息.
     * 
     * @param mapNode
     */
    void updateMapNodeFillColor(MapNode mapNode);

    /**
     * 批量修改指定图形结点的fixed信息.
     * 
     * @param mapNodes
     */
    void updateMapNodeFixed(List<MapNode> mapNodes);

    /**
     * 修改指定图形结点的fixed信息.
     * 
     * @param mapNode
     */
    void updateMapNodeFixed(MapNode mapNode);

    /**
     * 修改指定图形结点的字体颜色信息.
     * 
     * @param mapNode
     */
    void updateMapNodeFontColor(MapNode mapNode);

    /**
     * 修改指定图形结点的字体大小信息.
     * 
     * @param mapNode
     */
    void updateMapNodeFontSize(MapNode mapNode);

    /**
     * 修改指定图形结点的组信息.
     * 
     * @param mapNode
     */
    void updateMapNodeGroup(MapNode mapNode);

    /**
     * 修改指定图形结点的图标信息.
     * 
     * @param mapNode
     */
    void updateMapNodeIcon(MapNode mapNode);

    /**
     * 修改指定图形结点的阴影信息.
     * 
     * @param mapNode
     */
    void updateMapNodeShadow(MapNode mapNode);

    /**
     * 修改指定图形结点的宽高信息.
     * 
     * @param mapNode
     */
    void updateMapNodeSize(MapNode mapNode);

    /**
     * 修改指定图形结点的笔划颜色信息.
     * 
     * @param mapNode
     */
    void updateMapNodeStrokeColor(MapNode mapNode);

    /**
     * 修改指定图形结点的笔划粗细信息.
     * 
     * @param mapNode
     */
    void updateMapNodeStrokeWeight(MapNode mapNode);

    /**
     * 修改指定图形结点的Text信息.
     * 
     * @param mapNode
     */
    void updateMapNodeText(MapNode mapNode);

    /**
     * 修改超链的URL属性.
     * 
     * @param mapNode
     */
    void updateMapNodeUrl(MapNode mapNode);

    /**
     * 修改拓扑图报警模式.
     * 
     * @param folder
     */
    void updateMarkerAlertMode(TopoFolder folder);

    /**
     * 修改拓扑文件夹的基本信息.
     * 
     * @param sysLog
     * @param folder
     */
    void updateTopoFolder(SystemLog sysLog, TopoFolder folder);

    /**
     * 修改拓扑图形背景色.
     * 
     * @param folder
     */
    void updateTopoFolderBgColor(TopoFolder folder);

    /**
     * 修改拓扑图背景图使用状态.
     * 
     */
    void updateTopoFolderBgFlag(TopoFolder folder);

    /**
     * 修改拓扑图背景图.
     * 
     * @param folder
     */
    void updateTopoFolderBgImg(TopoFolder folder);

    /**
     * 修改拓扑图背景位置.
     * 
     * @param topoFolder
     */
    void updateTopoFolderBgPosition(TopoFolder topoFolder);

    /**
     * 修改拓扑图DisplayDesktop.
     * 
     * @param folder
     */
    void updateTopoFolderDisplayDesktop(TopoFolder folder);

    /**
     * 修改拓扑图DisplayEntityLabel.
     * 
     * @param folder
     */
    void updateTopoFolderDisplayEntityLabel(TopoFolder folder);

    /**
     * 修改拓扑文件夹显示网格.
     * 
     * @param topoFolder
     */
    void updateTopoFolderDisplayGrid(TopoFolder topoFolder);

    /**
     * 修改拓扑图DisplayL3switch.
     * 
     * @param folder
     */
    void updateTopoFolderDisplayL3switch(TopoFolder folder);

    /**
     * 修改拓扑图DisplayLinkLabel.
     * 
     * @param folder
     */
    void updateTopoFolderDisplayLinkLabel(TopoFolder folder);

    /**
     * 修改拓扑文件夹的显示名称.
     * 
     * @param topoFolder
     */
    void updateTopoFolderDisplayName(TopoFolder topoFolder);

    /**
     * 修改拓扑图DisplayNoSnmp.
     * 
     * @param folder
     */
    void updateTopoFolderDisplayNoSnmp(TopoFolder folder);

    /**
     * 修改拓扑图DisplayOthers.
     * 
     * @param folder
     */
    void updateTopoFolderDisplayOthers(TopoFolder folder);

    /**
     * 修改拓扑图DisplayRouter.
     * 
     * @param folder
     */
    void updateTopoFolderDisplayRouter(TopoFolder folder);

    /**
     * 修改拓扑图DisplayServer.
     * 
     * @param folder
     */
    void updateTopoFolderDisplayServer(TopoFolder folder);

    /**
     * 修改拓扑图DisplaySwitch.
     * 
     * @param folder
     */
    void updateTopoFolderDisplaySwitch(TopoFolder folder);

    /**
     * 批量修改拓扑图fixed.
     * 
     * @param folders
     */
    void updateTopoFolderFixed(List<TopoFolder> folders);

    /**
     * 修改拓扑图fixed.
     * 
     * @param folder
     */
    void updateTopoFolderFixed(TopoFolder folder);

    /**
     * 修改拓扑文件夹的图片.
     * 
     * @param folder
     */
    void updateTopoFolderIcon(TopoFolder folder);

    /**
     * 修改连接颜色.
     * 
     * @param folder
     */
    void updateTopoFolderLinkColor(TopoFolder folder);

    /**
     * 修改拓扑图linkshadow.
     * 
     * @param folder
     */
    void updateTopoFolderLinkShadow(TopoFolder folder);

    /**
     * 修改连接宽度.
     * 
     * @param folder
     */
    void updateTopoFolderLinkWidth(TopoFolder folder);

    /**
     * 修改拓扑图OrginEntity.
     * 
     * @param folder
     */
    void updateTopoFolderOrginEntity(TopoFolder folder);

    /**
     * 修改拓扑文件夹的刷新频率, 主要用于WEB展示的时候定时获取设备和线路状态.
     * 
     * @param topoFolder
     */
    void updateTopoFolderRefreshInterval(TopoFolder topoFolder);

    /**
     * 修改拓扑文件夹的缩放级别.
     * 
     * @param topoFolder
     */
    void updateTopoFolderZoom(TopoFolder topoFolder);

    /**
     * 更新拓扑图显示标注.
     * 
     * @param folderId
     * @param type
     * @param labels
     */
    void updateTopoLabel(Long folderId, String type, List<TopoLabel> labels);

    /**
     * 根据设备类型获取设备
     * 
     * @param type
     * @return
     */
    List<Entity> getEntityByType(Long type);

    /**
     * 判断是否存在子节点 loyal add
     * 
     * @param folder
     *            上级地域对象
     * @return 是否存在下级地域
     */
    boolean hasChild(TopoFolder folder);

    /**
     * 获取拓扑图中显示设备名称类型
     * 
     * @param folderId
     * @return
     */
    TopoFolder getDisplayNameType(Long folderId);

    /**
     * 通过上级地域ID和地域名称获得地域列表
     * 
     * @param topoFolder
     *            上级地域对象
     * @return 下级地域列表
     */
    List<TopoFolder> getTopoFolderList(TopoFolder topoFolder);

    /**
     * 判断地域能否被删除 需要考虑三个条件 1.该地域是否有关联设备 2.该地域下是否有子地域 3.该地域是否有关联用户
     * 
     * @param folder
     * @return
     */
    boolean isRegionCanBeDeleted(TopoFolder folder);

    /**
     * 根据地域名称返回对应地域ID，不存在的创建相应地域
     * 
     * @param topoName
     * @return
     */
    Long getOrInsertTopoFolderByName(String topoName);

    /**
     * getFolderIdByName
     * 
     * @param topoName
     * @return
     */
    List<Long> getFolderIdByName(String topoName);

    /**
     * 获取folder的名称和id
     * 
     * @param entityId
     * @return
     */
    TopoFolder getFolderIdAndName(Long entityId);

    /**
     * 获取地域列表
     * 
     * @return
     */
    void updateEntityTopoFolder(Long entityId, Integer folderId);

    /**
     * 根据entityId去查找设备所在的地域 因为存在设备在多个地域下的情况，所以返回值应该用地域列表
     * 
     * @param entityId
     * @return
     */
    List<TopoFolder> getTopoFolderByEntityId(Long entityId);

    /**
     * 获取指定设备所在地域的id集合
     * 
     * @param userId
     * @param entityId
     * @return
     */
    List<Long> getEntityLocatedFolderIds(Long entityId);

    /**
     * 编辑用户可选根地域时树数据的整理
     * 
     * @param user
     *            当前登录用户
     * @param userId
     *            待编辑的用户ID
     * @param ids
     * @return
     */
    List<TopoFolder> fetchUserSwithableFolders(Long curUserId, Long editUserId);

    List<TopoFolder> fetchUserCurRootFolders(Long userId);

    List<TopoFolder> fetchNetSegmentFolders(Long userId);

    /**
     * 获取指定用户可访问的地域集合
     * 
     * @param userId
     * @return
     */
    List<TopoFolder> fetchUserAuthFolders(Long userId);

    /**
     * 在指定用户权限下勾选地域
     * 
     * @param folderIds
     * @return
     */
    List<TopoFolder> checkFoldersOnAllFolders(Long userId, List<Long> folderIds);

    /**
     * 根据设备ID列表获取设备名称列表
     * 
     * @param entityIds
     * @return
     */
    List<String> getFolderNamesByIds(List<Long> folderIds);

    List<TopoFolder> fetchSwithRootFolders(Long userId);

    /**
     * 设备所处地域关系
     * 
     * @param entityId
     * @return
     */
    List<FolderRelation> getFolderRelationByEntityId(Long entityId);
}
