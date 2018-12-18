package com.topvision.ems.epon.onu.service;

import java.util.List;
import java.util.Map;

import com.topvision.ems.epon.onu.domain.OnuLinkInfo;
import com.topvision.ems.epon.onu.domain.OnuLinkThreshold;
import com.topvision.framework.service.Service;

public interface OnuLinkService extends Service {

    /**
     * 查询onu链路视图数据
     * 
     * @param queryMap
     * @return
     */
    List<OnuLinkInfo> queryOnuLinkList(Map<String, Object> queryMap);

    /**
     * 获得onu链路视图数据条数
     * 
     * @param queryMap
     * @return
     */
    Integer queryOnuLinkListCount(Map<String, Object> queryMap);

    /**
     * 查询阈值条件
     * 
     * @return
     */
    List<OnuLinkThreshold> queryOnuLinkThreshold();

    /**
     * 获取单个OnuLinkInfo
     * 
     * @param onuId
     * @return
     */
    OnuLinkInfo getOnuLinkInfo(Long onuId);
}
