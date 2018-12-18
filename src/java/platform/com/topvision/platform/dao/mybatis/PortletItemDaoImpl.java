/**
 *
 */
package com.topvision.platform.dao.mybatis;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.topvision.framework.dao.mybatis.MyBatisDaoSupport;
import com.topvision.platform.dao.PortletItemDao;
import com.topvision.platform.domain.PortletCategory;
import com.topvision.platform.domain.PortletItem;

/**
 * @author hp
 */
@Repository("portletItemDao")
public class PortletItemDaoImpl extends MyBatisDaoSupport<PortletItem> implements PortletItemDao {

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.framework.dao.ibatis.MyBatisDaoSupport#getNameSpace()
     */
    @Override
    public String getDomainName() {
        return PortletItem.class.getName();
    }

    @Override
    public List<PortletItem> loadAllPortletItem() {
        return getSqlSession().selectList(getNameSpace("loadAllPortletItem"));
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.server.portal.dao.PortletItemDao#getPortletCategory()
     */
    @Override
    public List<PortletCategory> getPortletCategory() {
        return getSqlSession().selectList(getNameSpace("getPortletCategory"));
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.server.core.dao.PortletItemDao#getPortletItemByModule
     * (java.lang.String)
     */
    @Override
    public List<PortletItem> getPortletItemByModule(String module) {
        return getSqlSession().selectList(getNameSpace("getPortletItemByModule"), module);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.server.core.dao.PortletItemDao#getPortletItemByUser( Long)
     */
    @Override
    public List<PortletItem> getPortletItemByUser(Long userId) {
        return getSqlSession().selectList(getNameSpace("getPortletItemByUser"), userId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.server.portal.dao.PortletItemDao#removePortletItemByUserId (Long)
     */
    @Override
    public void removePortletItemByUserId(Long userId) {
        getSqlSession().delete(getNameSpace("removePortletItemByUserId"), userId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.platform.dao.PortletItemDao#initializeUserPortletItem(com
     * .topvision.platform.domain.PortletItem)
     */
    public void insertUserPortletItem(PortletItem item) {
        getSqlSession().update(getNameSpace("insertEntity"), item);
    }
}
