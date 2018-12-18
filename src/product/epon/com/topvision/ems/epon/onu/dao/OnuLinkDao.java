package com.topvision.ems.epon.onu.dao;

import java.util.List;
import java.util.Map;

import com.topvision.ems.epon.onu.domain.OnuLinkInfo;
import com.topvision.ems.epon.onu.domain.OnuLinkThreshold;

public interface OnuLinkDao {

    /**
     * 获得onu链路视图数据
     * 
     * @param queryMap
     * @return
     */
    List<OnuLinkInfo> selectOnuLinkList(Map<String, Object> queryMap);

    /**
     * 获得onu链路视图数据条数
     * 
     * @param queryMap
     * @return
     */
    Integer selectOnuLinkListCount(Map<String, Object> queryMap);

    /**
     * 查询阈值规则
     * 
     * @return
     */
    List<OnuLinkThreshold> selectOnuLinkThreshold();

    /**
     * 获取单个OnuLinkInfo
     * 
     * @param onuId
     * @return
     */
    OnuLinkInfo selectOnuLinkInfo(Long onuId);
}
