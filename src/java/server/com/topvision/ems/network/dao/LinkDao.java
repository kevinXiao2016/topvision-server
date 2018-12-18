package com.topvision.ems.network.dao;

import java.util.List;
import java.util.Map;

import com.topvision.ems.network.domain.Link;
import com.topvision.ems.network.domain.LinkEx;
import com.topvision.ems.network.domain.PortEx;
import com.topvision.framework.dao.BaseEntityDao;

public interface LinkDao extends BaseEntityDao<Link> {
    /**
     * 批量删除指定设备ID的连接信息.
     * 
     * @param entityIds
     */
    void deleteLinkByEntityId(List<Long> entityIds);

    /**
     * 删除指定设备ID的连接信息.
     * 
     * @param entityId
     */
    void deleteLinkByEntityId(Long entityId);

    /**
     * 清除脱管设备.
     * 
     */
    void emptyLinkByRecyle();

    /**
     * 获取所有连接信息.
     * 
     * @param map
     * @return
     */
    List<LinkEx> getAllLink(Map<String, Long> map);

    /**
     * 获取指定拓扑文件夹ID的连接信息.
     * 
     * @param folderId
     * @return
     */
    List<Link> getLinkByFolderId(Long folderId);

    /**
     * 获取指定设备ID和端口ID对应的连接信息.
     * 
     * @param entityId
     * @param ifIndex
     * @return
     */
    List<Link> getLinkByPort(Long entityId, Long ifIndex);

    /**
     * 获取连接信息的连接流量排行.
     * 
     * @param map
     * @return
     */
    // FIXME Link.xml中【getLinkFlowTop】的sql中，参数实际上未被使用
    List<LinkEx> getLinkFlowTop(Map<String, String> map);

    /**
     * 获取指定拓扑文件夹ID对应的连接信息.
     * 
     * @param folderId
     * @return
     */
    List<Long> getLinkIdByFolderId(Long folderId);

    /**
     * 获取连接信息的连接速度排行.
     * 
     * @param map
     * @return
     */
    // FIXME Link.xml中【getLinkRateTop】的sql中，参数实际上未被使用
    List<LinkEx> getLinkRateTop(Map<String, String> map);

    /**
     * 获取指定设备ID的连接相关信息.
     * 
     * @param entityId
     * @return
     */
    List<PortEx> getLinkTableByEntity(Long entityId);

    /**
     * 插入/更新指定连接信息.
     * 
     * @param link
     */
    void insertOrUpdateEntity(Link link);

    /**
     * 批量插入/更新指定连接信息.
     * 
     * @param links
     */
    void insertOrUpdateEntity(List<Link> links);

    /**
     * 查询指定条件的连接相关信息.
     * 
     * @param query
     * @return
     */
    List<LinkEx> queryLink(String query);

    /**
     * 更新指定连接ID的连接信息.
     * 
     * @param link
     */
    void updateOutline(Link link);

    /**
     * 判断连接是否存在
     * 
     * @param port
     * @return
     */
    Long isLinkExists(Link link);

    /**
     * 找到设备相关联的所有link
     * 
     * @param entityIds
     * @return
     */
    List<Long> getLinkIdByEntityIds(List<Long> entityIds);
}
