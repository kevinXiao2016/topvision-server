package com.topvision.ems.network.dao;

import java.util.List;

import com.topvision.ems.network.domain.MapNode;
import com.topvision.framework.dao.BaseEntityDao;

public interface MapNodeDao extends BaseEntityDao<MapNode> {
    /**
     * 删除指定的文件夹路径对应的图形结点信息.
     * 
     * @param folderId
     */
    void deleteMapNodeByFolder(Long folderId);

    /**
     * 获取指定结点ID对应的图形结点信息.
     * 
     * @param node
     * @return
     */
    MapNode getMapNodeByObjId(MapNode node);

    /**
     * 获取指定文件夹ID对应的图形结点信息.
     * 
     * @param folderId
     * @return
     */
    List<MapNode> getMapNodes(Long folderId);

    /**
     * 批量修改指定图形结点的坐标信息.
     * 
     * @param nodes
     */
    void saveMapNodeCoordinate(List<MapNode> nodes);

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
     * @param mapNodes
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
     * 修改超链的Url属性.
     * 
     * @param mapNode
     */
    void updateMapNodeUrl(MapNode mapNode);

    /**
     * 修改指定用户相关信息.
     * 
     * @param mapNode
     */
    void updateUserObject(MapNode mapNode);

    /**
     * 获取所有的mapnode
     * 
     * @return
     */
    List<MapNode> getAllMapNodes();
    
}
