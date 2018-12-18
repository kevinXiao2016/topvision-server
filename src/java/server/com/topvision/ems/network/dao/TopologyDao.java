package com.topvision.ems.network.dao;

import java.util.List;

import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.network.domain.Link;
import com.topvision.ems.network.domain.TopoFolder;
import com.topvision.ems.network.domain.TopoLabel;
import com.topvision.framework.dao.BaseEntityDao;

// FIXME 没有对应固定的domain，使用Object代替
public interface TopologyDao extends BaseEntityDao<Object> {
    /**
     * 获取设备和拓扑文件夹结点的关联信息.
     * 
     * @param folderId
     * @return
     */
    List<Link> getEdgeByFolderId(Long folderId);

    /**
     * 获取指定拓扑文件夹的连接ID.
     * 
     * @param folderId
     * @return
     */
    List<Long> getEdgeIdByFolderId(Long folderId);

    /**
     * 获取设备和拓扑文件夹结点的坐标.
     * 
     * @param folderId
     * @return
     */
    List<Entity> getNodeCoordByFolderId(Long folderId);

    /**
     * 获取所有的拓扑标注.
     * 
     * @return
     */
    List<TopoLabel> getTopoLabels();

    /**
     * 获取设备和拓扑文件夹结点的基本信息.
     * 
     * @param folderId
     * @return
     */
    List<Entity> getVertexByFolderId(Long folderId);

    /**
     * 批量修改设备的坐标信息.
     * 
     * @param entities
     */
    void saveEntityCoordinate(List<Entity> entities);

    /**
     * 批量修改拓扑文件夹节点的坐标信息.
     * 
     * @param folders
     */
    void saveFolderCoordinate(List<TopoFolder> folders);

    /**
     * 批量修改拓扑标注.
     * 
     * @param labels
     */
    void updateTopoLabel(List<TopoLabel> labels);

    List<Long> getFloderList();
}
