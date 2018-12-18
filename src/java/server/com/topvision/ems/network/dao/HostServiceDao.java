package com.topvision.ems.network.dao;

import java.util.List;

import com.topvision.ems.facade.domain.HostService;
import com.topvision.ems.network.domain.Services;
import com.topvision.framework.dao.BaseEntityDao;

public interface HostServiceDao extends BaseEntityDao<HostService> {
    /**
     * 获取网络服务类型.
     * 
     * @return
     */
    List<Services> getNetworkServices();

    /**
     * 设置指定端口是否已被扫描.
     * 
     * @param services
     */
    void setServicesScanned(List<Services> services);
}
