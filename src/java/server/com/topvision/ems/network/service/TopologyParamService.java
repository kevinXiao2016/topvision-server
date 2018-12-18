package com.topvision.ems.network.service;

import com.topvision.ems.facade.domain.TopologyParam;
import com.topvision.framework.service.Service;

public interface TopologyParamService extends Service {
    /**
     * 插入拓扑参数.
     * 
     * @param param
     */
    void insertParam(TopologyParam param);

    /**
     * 获取拓扑参数.
     * 
     * @return
     */
    TopologyParam txGetTopologyParam();

    /**
     * 更新拓扑参数.
     * 
     * @param param
     */
    void updateParam(TopologyParam param);
}
