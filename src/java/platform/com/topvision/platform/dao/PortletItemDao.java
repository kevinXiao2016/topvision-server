package com.topvision.platform.dao;

import java.util.List;

import com.topvision.framework.dao.BaseEntityDao;
import com.topvision.platform.domain.PortletCategory;
import com.topvision.platform.domain.PortletItem;

public interface PortletItemDao extends BaseEntityDao<PortletItem> {
    
    List<PortletItem> loadAllPortletItem();

    /**
     * 获取Portlet分类
     * 
     * @return
     */
    List<PortletCategory> getPortletCategory();

    /**
     * 通过模块名获取 PortletItem
     * 
     * @param module
     * @return
     */
    List<PortletItem> getPortletItemByModule(String module);

    /**
     * 通过用户获取PortletItem
     * 
     * @param userId
     * @return
     */
    List<PortletItem> getPortletItemByUser(Long userId);

    /**
     * 通过用户删除PortletItem
     * 
     * @param userId
     */
    void removePortletItemByUserId(Long userId);

    /**
     * 将个人信息portal页插入到数据库中
     * 
     * @param userId
     */

    void insertUserPortletItem(PortletItem item);
}
