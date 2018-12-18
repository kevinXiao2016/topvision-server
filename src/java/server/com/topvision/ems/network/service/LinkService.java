package com.topvision.ems.network.service;

import java.util.List;
import java.util.Map;

import com.topvision.ems.network.domain.Link;
import com.topvision.ems.network.domain.LinkEx;
import com.topvision.ems.network.domain.LinkRealFlow;
import com.topvision.ems.network.domain.PortEx;
import com.topvision.ems.network.domain.PortPerf;
import com.topvision.framework.event.MyResultHandler;
import com.topvision.framework.service.Service;
import com.topvision.platform.domain.SystemLog;

public interface LinkService extends Service {
    /**
     * 获取所有连接.
     * 
     * @return
     */
    List<LinkEx> getAllLink();

    /**
     * 获取指定设备ID的连接.
     * 
     * @param entityId
     * @return
     */
    List<LinkEx> getAllLink(Long entityId);

    /**
     * 获取连接.
     * 
     * @param linkId
     * @return
     */
    LinkEx getLinkEx(Long linkId);

    /**
     * 获取连接信息的连接流量排行.
     * 
     * @param map
     * @return
     */
    List<LinkEx> getLinkFlowTop(Map<String, String> map);

    /**
     * 获取连接信息的连接速度排行.
     * 
     * @param map
     * @return
     */
    List<LinkEx> getLinkRateTop(Map<String, String> map);

    /**
     * 获取连接实际流量.
     * 
     * 
     * @param linkId
     * @return
     */
    LinkRealFlow getLinkRealFlow(Long linkId);

    /**
     * 获取指定设备ID的连接相关信息.
     * 
     * @param entityId
     * @return
     */
    List<PortEx> getLinkTableByEntity(Long entityId);

    /**
     * 获取指定设备ID的端口性能.
     * 
     * @param entityId
     * @return
     */
    List<PortPerf> getPortPerfByEntityId(Long entityId);

    /**
     * 插入连接.
     * 
     * @param sysLog
     * @param link
     * @param folderId
     */
    void insertLink(SystemLog sysLog, Link link, Long folderId);

    /**
     * 查询给定设备IP或者名称的线路.
     * 
     * @param query
     * @return
     */
    List<LinkEx> queryLink(String query);

    /**
     * 保存连接当前状态.
     * 
     * @param handler
     */
    void restoreLinkCurrentState(MyResultHandler handler);

    /**
     * 更新连接.
     * 
     * @param link
     */
    void updateLink(LinkEx link);

    /**
     * 更新指定连接ID的连接信息.
     * 
     * @param link
     */
    void updateOutline(LinkEx link);

    /**
     * 通过entityId和portId.
     * 
     * @param entityId
     * @param ifIndex
     */
    Link getLinkByPort(Long entityId, Long ifIndex);
}
