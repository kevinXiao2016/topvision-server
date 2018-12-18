package com.topvision.platform.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.topvision.framework.service.BaseService;
import com.topvision.platform.dao.PortletItemDao;
import com.topvision.platform.domain.PortletCategory;
import com.topvision.platform.domain.PortletItem;
import com.topvision.platform.service.PortletService;

@Service("portletService")
public class PortletServiceImpl extends BaseService implements PortletService {
    @Autowired
    private PortletItemDao portletItemDao = null;
    private List<PortletCategory> portletCategorys = null;
    private List<PortletItem> portletItems = null;

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.server.portal.service.PortletService#getPortletCategory ()
     */
    @Override
    public List<PortletCategory> getPortletCategory() {
        return portletItemDao.getPortletCategory();
    }

    public List<PortletCategory> getPortletCategorys() {
        return portletCategorys;
    }

    @Override
    public List<PortletItem> getPortletItem() {
        return portletItems;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.server.core.service.PortletService#getPortletItem(Long)
     */
    @Override
    public List<PortletItem> getPortletItem(Long userId) {
        return portletItemDao.getPortletItemByUser(userId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.server.core.service.PortletService#getPortletItem(java .lang.String)
     */
    @Override
    public List<PortletItem> getPortletItem(String module) {
        return portletItemDao.getPortletItemByModule(module);
    }

    public List<PortletItem> getPortletItems() {
        return portletItems;
    }

    @Override
    @PostConstruct
    public void initialize() {
        super.initialize();
        try {
            portletCategorys = portletItemDao.getPortletCategory();
            portletItems = portletItemDao.selectByMap(null);
        } catch (DataAccessException e) {
            getLogger().error("Load Portlet Item error", e);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.server.portal.service.PortletService#savePortletItem (Long,
     * java.util.List)
     */
    @Override
    public void savePortletItem(Long userId, List<Long> itemIds) {
        List<PortletItem> items = new ArrayList<PortletItem>();
        int size = itemIds == null ? 0 : itemIds.size();
        for (int i = 0; i < size; i++) {
            if (itemIds.get(i) > 100) {
                PortletItem item = new PortletItem();
                item.setUserId(userId);
                item.setItemId(itemIds.get(i));
                items.add(item);
            }
        }
        portletItemDao.removePortletItemByUserId(userId);
        portletItemDao.insertEntity(items);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.platform.service.PortletService#initializePortletItem(java .lang.Long)
     */
    public void initializeUserPortletItem(PortletItem item) {
        portletItemDao.insertUserPortletItem(item);
    }

    public void setPortletCategorys(List<PortletCategory> portletCategorys) {
        this.portletCategorys = portletCategorys;
    }

    public void setPortletItemDao(PortletItemDao portletItemDao) {
        this.portletItemDao = portletItemDao;
    }

    public void setPortletItems(List<PortletItem> portletItems) {
        this.portletItems = portletItems;
    }

    @Override
    public void updatePortletItem(PortletItem item) {
        portletItemDao.updateEntity(item);
    }

}
