package com.topvision.ems.epon.onu.service;

import java.util.List;
import java.util.Map;

import com.topvision.ems.epon.onu.domain.OnuTag;
import com.topvision.framework.service.Service;

/**
 * 
 * @author w1992wishes
 * @created @2017年12月21日-下午1:57:42
 *
 */
public interface OnuTagService extends Service {

    /**
     * 获取标签
     * 
     * @param queryMap
     * @return
     */
    List<OnuTag> getOnuTags(Map<String, Object> queryMap);

    /**
     * 获取标签数量
     * 
     * @return
     */
    Integer getOnuTagsCount();

    /**
     * 新增标签
     * 
     * @param tag
     */
    void createOnuTag(OnuTag onuTag);

    /**
     * 修改标签
     * 
     * @param tag
     */
    void modifyOnuTag(OnuTag onuTag);

    /**
     * 删除标签
     * 
     * @param tag
     */
    void deleteOnuTag(OnuTag onuTag);

}
