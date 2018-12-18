package com.topvision.ems.network.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.network.domain.FolderRelation;
import com.topvision.ems.network.domain.FolderUserGroupRela;
import com.topvision.ems.network.domain.MapNode;
import com.topvision.ems.network.domain.TopoFolder;
import com.topvision.ems.network.domain.TopoFolderEx;
import com.topvision.ems.network.domain.TopoFolderStat;
import com.topvision.framework.dao.BaseEntityDao;
import com.topvision.framework.event.MyResultHandler;

public interface TopoFolderDao extends BaseEntityDao<TopoFolder> {
    /**
     * 删除文件夹用户组关系.
     * 
     * @param userGroupId
     */
    void deleteFolderUserGroup(Long userGroupId);

    /**
     * 删除指定路径对应的拓扑图.
     * 
     * @param folderId
     */
    void deleteTopoFolder(Long folderId);

    /**
     * 获取子结点的个数.
     * 
     * @param folderId
     * @return
     */
    Integer getChildCount(Long folderId);

    /**
     * 得到给定拓扑文件夹下的网段和子网.
     * 
     * @param superiorId
     * @return
     */
    List<TopoFolder> getChildTopoFolder(Long superiorId);

    /**
     * 获取给定path的子拓扑文件夹.
     * 
     * @param superiorPath
     * @return
     */
    List<TopoFolderEx> getChildTopoFolderByPath(String superiorPath);

    /**
     * 获取给定拓扑文件下设备数量.
     * 
     * @param folder
     * @return
     */
    Integer getEntityCountInFolder(TopoFolder folder);

    /**
     * 获取指定文件夹ID的拓扑文件夹路径.
     * 
     * @param folderId
     * @return
     */
    String getFolderPath(Long folderId);

    /**
     * 获取指定拓扑图.
     * 
     * @param map
     * @return
     */
    List<TopoFolder> getMyTopoFolder(Map<String, Object> map);

    /**
     * 获取拓扑图名称及ID.
     * 
     * @return
     */
    List<TopoFolderStat> getStatTopoFolder();

    /**
     * 获取指定组ID下的拓扑图.
     * 
     * @param userGroupId
     * @return
     */
    List<TopoFolder> getTopoFolderByGroup(Long userGroupId);

    /**
     * 获取指定IP的拓扑图.
     * 
     * @param ip
     * @return
     */
    List<TopoFolderEx> getTopoFolderByIp(String ip);

    /**
     * 在给定父结点id的情况下获取名称为name的组织.
     * 
     * @param folder
     * @return
     */
    TopoFolder getTopoFolderByName(TopoFolder folder);

    /**
     * getFolderIdByName
     * 
     * @param topoName
     * @return
     */
    Long getFolderIdByName(String topoName);

    /**
     * 插入文件夹用户组关系.
     * 
     * @param rela
     */
    void insertFolderUserGroup(FolderUserGroupRela rela);

    /**
     * 批量插入文件夹用户组关系.
     * 
     * @param relas
     */
    void insertFolderUserGroup(List<FolderUserGroupRela> relas);

    /**
     * 获取指定文件夹ID的映射节点信息.
     * 
     * @param folderId
     * @return
     */
    // FIXME xml文件中没有loadMapNodeByFolderId
    List<MapNode> loadMapNodeByFolderId(Long folderId);

    /**
     * 获取子网拓扑图.
     * 
     * @return
     */
    List<TopoFolder> loadSubnetFolder();

    /**
     * 获取拓扑文件夹下信息.
     * 
     * @return
     */
    List<TopoFolder> loadTopoFolder();

    /**
     * 获取拓扑文件夹映射.
     * 
     * @return
     */
    List<TopoFolder> loadTopoMap();

    /**
     * 重命名指定拓扑图.
     * 
     * @param folder
     */
    void renameTopoFolder(TopoFolder folder);

    /**
     * 获取拓扑图状态.
     * 
     * @param handler
     */
    void statTopoFolder(MyResultHandler handler);

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
     * 修改拓扑图显示连接.
     * 
     * @param topoFolder
     */
    void updateDisplayLink(TopoFolder topoFolder);

    /**
     * 修改拓扑图背景位置.
     * 
     * @param topoFolder
     */
    void updateFolderBgPosition(TopoFolder topoFolder);

    /**
     * 修改拓扑图标注.
     * 
     * @param folder
     */
    void updateFolderEntityLabel(TopoFolder folder);

    /**
     * 批量修改拓扑图fixed.
     * 
     * @param folders
     */
    void updateFolderFixed(List<TopoFolder> folders);

    /**
     * 修改拓扑图fixed.
     * 
     * @param folder
     */
    void updateFolderFixed(TopoFolder folder);

    /**
     * 修改拓扑图连接标注.
     * 
     * @param folder
     */
    void updateFolderLinkLabel(TopoFolder folder);

    /**
     * 修改指定拓扑文件夹路径.
     * 
     * @param folder
     */
    void updateFolderPath(TopoFolder folder);

    /**
     * 修改拓扑图报警模式.
     * 
     * @param folder
     */
    void updateMarkerAlertMode(TopoFolder folder);

    /**
     * 修改拓扑图背景色.
     * 
     * @param folder
     */
    void updateTopoFolderBgColor(TopoFolder folder);

    /**
     * 修改拓扑图背景图使用状态.
     * 
     * @param folder
     */
    void updateTopoFolderBgFlag(TopoFolder folder);

    /**
     * 修改指定拓图背景图.
     * 
     * @param folder
     */
    void updateTopoFolderBgImg(TopoFolder folder);

    /**
     * 修改拓扑图DisplayCluetip.
     * 
     * @param folder
     */
    void updateTopoFolderDisplayCluetip(TopoFolder folder);

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
     * 修改拓扑图显示表格.
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
     * 修改拓扑图显示名称.
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
     * 修改拓扑图图标.
     * 
     * @param folder
     */
    void updateTopoFolderIcon(TopoFolder folder);

    /**
     * 修改拓扑图连接颜色.
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
     * 修改拓扑图连接宽度.
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
     * 修改拓扑图基本信息.
     * 
     * @param folder
     */
    void updateTopoFolderOutline(TopoFolder folder);

    /**
     * 修改拓扑图刷新间隔.
     * 
     * @param folder
     */
    void updateTopoFolderRefreshInterval(TopoFolder folder);

    /**
     * 修改拓扑图zoom.
     * 
     * @param topoFolder
     */
    void updateTopoFolderZoom(TopoFolder topoFolder);

    /**
     * 获取拓扑图中显示设备名称类型
     * 
     * @param folderId
     * @return
     */
    TopoFolder getDisplayNameType(Long folderId);

    /**
     * 获取拓扑图中显示设备名称类型
     * 
     * @param topoFolder
     * @return
     */
    List<TopoFolder> getTopoFolderList(TopoFolder topoFolder);

    /**
     * 为了兼容不可识别的设备类型 例如server switch等将这些设备类型归纳为Other，并计算其个数使用，将要逐步被移除
     * 
     * @param folderId
     * @return
     */
    Integer getFodlerOtherNum(Long folderId);

    /**
     * 新建地域
     * 
     * @param topoFolder
     * @return
     */
    Long insertTopoFolder(TopoFolder topoFolder) throws SQLException;

    /**
     * 判断该地域是否有用户关联
     * 
     * @param folderId
     * @return
     */
    boolean hasUser(Long folderId);

    /**
     * 获得folder的名称
     * 
     * @param entityId
     * @return
     */
    String getTopoFolderNameById(Long entityId);

    /**
     * * 获取Folder的id和名称
     * 
     * @param entityId
     * @return
     */
    TopoFolder queryFolderIdAndName(Long entityId);

    /**
     * 获得folder的列表
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
    List<TopoFolder> queryTopoFolderByEntityId(Long entityId);

    /**
     * 获取指定设备所在的地域ID集合
     * 
     * @param entityId
     * @return
     */
    List<Long> getEntityLocatedFolderIds(Long entityId);

    List<Entity> loadEntitiesWithFolderInfo(Map<String, String> map);

    Long getEntityListCount(Map<String, String> map);

    /**
     * 获取指定folderId集合的folder信息
     * 
     * @param userAutoFolderIds
     * @return
     */
    List<TopoFolder> getTopoFolderByIds(List<Long> userAutoFolderIds);

    /**
     * 获取所有地域
     * 
     * @return
     */
    List<TopoFolder> loadAllFolders();

    /**
     * 获取用户当前有权限访问的地域集合
     * 
     * @param userId
     * @return
     */
    List<TopoFolder> fetchUserAuthFolders(Long userId);

    /**
     * 获取所有地域，并将指定用户权限外的地域置为不可选
     * 
     * @param userId
     * @return
     */
    List<TopoFolder> fetchAllFolderWithCheckable(Long userId);

    /**
     * 获取指定用户的可选根地域
     * 
     * @param userId
     * @return
     */
    List<Long> getUserAuthFolderIds(Long userId);

    /**
     * 获取指定设备的名称
     * 
     * @param entityIds
     * @return
     */
    List<String> getEntityNamesByIds(List<Long> entityIds);

    /**
     * 根据地域ID数组获取地域名称数组
     * 
     * @param folderIds
     * @return
     */
    List<String> getFolderNamesByIds(List<Long> folderIds);

    /**
     * 根据id
     * 
     * @param folderId
     * @return
     */
    FolderRelation selectFolderRelationById(Long folderId);

    /**
     * 根据父类 id
     * 
     * @param superiorId
     * @return
     */
    List<FolderRelation> selectFolderRelationBySuperId(Long superiorId);

    /**
     * 设备所处地域关系
     * 
     * @param entityId
     * @return
     */
    List<FolderRelation> selectFolderRelationByEntityId(Long entityId);
}
