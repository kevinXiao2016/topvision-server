package com.topvision.ems.network.service;

import com.topvision.ems.facade.domain.TopologyResult;
import com.topvision.exception.service.NetworkException;
import com.topvision.framework.service.Service;

public interface OnlineService extends Service {
    /**
     * 添加数据.
     * 
     * @param result
     */
    void addData(TopologyResult result);

    /**
     * 获取指定MAC的IP.
     * 
     * @param mac
     * @return
     */
    String getIpByMac(String mac);

    /**
     * 获取指定IP的MAC.
     * 
     * @param ip
     * @return
     */
    String getMacByIp(String ip);

    /**
     * 获取指定IP的主机名.
     * 
     * @param ip
     * @return
     */
    String getNameByIp(String ip);

    /**
     * 判断指定IP是否在线.
     * 
     * @param ip
     * @return
     */
    Boolean isOnlineByIp(String ip);

    /**
     * 判断指定MAC是否在线.
     * 
     * @param mac
     * @return
     */
    Boolean isOnlineByMac(String mac);

    /**
     * Ping指定IP.
     * 
     * @param ip
     * @return
     * @throws NetworkException
     */
    Integer ping(String ip) throws NetworkException;

    /**
     * 将指定IP设置为下线状态.
     * 
     * @param ip
     */
    void setDownByIp(String ip);

    /**
     * 将指定MAC设置为下线状态.
     * 
     * @param mac
     */
    void setDownByMac(String mac);
}
