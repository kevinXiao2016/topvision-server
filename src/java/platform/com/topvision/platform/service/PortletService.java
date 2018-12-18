package com.topvision.platform.service;

import java.util.List;

import com.topvision.framework.service.Service;
import com.topvision.platform.domain.PortletCategory;
import com.topvision.platform.domain.PortletItem;

public interface PortletService extends Service {

    /**
     * 获得所有Portlet的分类
     * 
     * @return List<PortletCategory>
     */
    List<PortletCategory> getPortletCategory();

    /**
     * 获得所有的Portlet
     * 
     * @return List<PortletItem>
     */
    List<PortletItem> getPortletItem();

    /**
     * 通过用户ID获得所有相关的Portlet
     * 
     * @param userId
     *            指定用户ID
     * @return List<PortletItem>
     */
    List<PortletItem> getPortletItem(Long userId);

    /**
     * 通过模块名获得所有相关的Portlet
     * 
     * @param module
     *            模块名
     * @return List<PortletItem>
     */
    List<PortletItem> getPortletItem(String module);

    /**
     * 保存一批Portlet
     * 
     * @param userId
     *            对应的用户ID
     * @param itemIds
     *            一批Portlet
     */
    void savePortletItem(Long userId, List<Long> itemIds);

    /**
     * 更新一个Portlet
     * 
     * @param item
     */
    void updatePortletItem(PortletItem item);

    /**
     * 默认显示个人信息portal页
     * 
     * @param userId
     */
    void initializeUserPortletItem(PortletItem item);
}
