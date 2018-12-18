package com.topvision.ems.epon.onu.dao;

import java.util.List;
import java.util.Map;

import com.topvision.ems.epon.onu.domain.OnuTag;
import com.topvision.framework.dao.BaseEntityDao;

public interface OnuTagDao extends BaseEntityDao<OnuTag> {

    /**
     * 获取标签
     * 
     * @param queryMap
     * @return
     */
    List<OnuTag> selectOnuTags(Map<String, Object> queryMap);

    /**
     * 标签数量
     * 
     * @return
     */
    Integer selectOnuTagsCount();

}
