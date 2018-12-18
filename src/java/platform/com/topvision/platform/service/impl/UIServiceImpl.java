package com.topvision.platform.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.topvision.framework.service.BaseService;
import com.topvision.platform.dao.FunctionItemDao;
import com.topvision.platform.dao.MenuItemDao;
import com.topvision.platform.dao.NavigationButtonDao;
import com.topvision.platform.dao.RoleDao;
import com.topvision.platform.dao.ToolbarButtonDao;
import com.topvision.platform.domain.FunctionItem;
import com.topvision.platform.domain.FunctionItemEx;
import com.topvision.platform.domain.MenuItem;
import com.topvision.platform.domain.NavigationButton;
import com.topvision.platform.domain.NavigationButtonEx;
import com.topvision.platform.domain.Role;
import com.topvision.platform.domain.ToolbarButton;
import com.topvision.platform.service.UIService;

/**
 * @author kelers
 * @Create Date Oct 24, 2009 9:46:27 AM
 */
public class UIServiceImpl extends BaseService implements UIService {
    @Autowired
    private RoleDao roleDao;
    @Autowired
    private FunctionItemDao functionItemDao = null;
    @Autowired
    private ToolbarButtonDao toolbarButtonDao;
    @Autowired
    private NavigationButtonDao navigationButtonDao;
    @Autowired
    private MenuItemDao menuItemDao;

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.platform.system.service.SystemService#getFunctionItemByName
     * (java.lang.String)
     */
    @Override
    public FunctionItem getFunctionItemByName(String name) {
        return functionItemDao.selectByPrimaryKey(name);
    }

    @Override
    public List<MenuItem> getMenuItemByUser(Long userId) {
        return roleDao.getMenuItemByUser(userId);
    }

    @Override
    public List<NavigationButton> getNavigationButtonByUser(Long userId) {
        return roleDao.getNavigationButtonByUser(userId);
    }

    @Override
    public List<ToolbarButton> getToolbarButtonByUser(Long userId) {
        return roleDao.getToolbarButtonByUser(userId);
    }

    @Override
    public List<String> getUserPowerByUser(Long userId) {
        return roleDao.getUserPowerByUser(userId);
    }

    public List<Role> loadAllRole() {
        return roleDao.selectByMap(null);
    }

    @Override
    public List<ToolbarButton> loadButtonByRole(Long roleId) {
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.server.system.service.SystemService# loadFunctionItem()
     */
    @Override
    public List<FunctionItem> loadFunctionItem() {
        return functionItemDao.selectByMap(null);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.server.system.service.SystemService# loadFunctionItemByRole(int)
     */
    @Override
    public List<FunctionItem> loadFunctionItemByRole(Long roleId) {
        return roleDao.getFunctionItemByRole(roleId);
    }

    @Override
    public List<MenuItem> loadMenuItem() {
        return menuItemDao.selectByMap(null);
    }

    @Override
    public List<MenuItem> loadMenuItemByRole(Long roleId) {
        return null;
    }

    @Override
    public List<MenuItem> loadMenus() {
        return null;
    }

    @Override
    public List<NavigationButton> loadNavigationButtonByRole(Long roleid) {
        return roleDao.getNavigationButtonByRole(roleid);
    }

    @Override
    public List<NavigationButton> loadNavigationButtons() {
        return navigationButtonDao.selectByMap(null);
    }

    @Override
    public List<ToolbarButton> loadToolbarButton() {
        return toolbarButtonDao.selectByMap(null);
    }

    public void setFunctionItemDao(FunctionItemDao functionItemDao) {
        this.functionItemDao = functionItemDao;
    }

    public void setMenuItemDao(MenuItemDao menuItemDao) {
        this.menuItemDao = menuItemDao;
    }

    public void setNavigationButtonDao(NavigationButtonDao navigationButtonDao) {
        this.navigationButtonDao = navigationButtonDao;
    }

    public void setRoleDao(RoleDao roleDao) {
        this.roleDao = roleDao;
    }

    public void setToolbarButtonDao(ToolbarButtonDao toolbarButtonDao) {
        this.toolbarButtonDao = toolbarButtonDao;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.server.system.service.UIService# saveFunctionItem(Long,
     * java.util.List)
     */
    @Override
    public void txUpdateFunctionItem(Long roleId, List<Long> itemIds) {
        roleDao.deleteFunctionItem(roleId);
        int size = itemIds == null ? 0 : itemIds.size();
        if (size > 0) {
            List<FunctionItemEx> items = new ArrayList<FunctionItemEx>();
            for (Long aLong : itemIds) {
                if (aLong != -1) {
                    FunctionItemEx item = new FunctionItemEx();
                    item.setRoleId(roleId);
                    item.setFunctionId(aLong);
                    items.add(item);
                }
            }
            roleDao.insertFunctionItem(items);
        }
    }

    @Override
    public void txUpdateNaviButton(Long roleId, List<Integer> itemIds) {
        roleDao.deleteNavigationButton(roleId);
        int size = itemIds == null ? 0 : itemIds.size();
        if (size > 0) {
            List<NavigationButtonEx> items = new ArrayList<NavigationButtonEx>();
            for (int i = 0; i < size; i++) {
                NavigationButtonEx item = new NavigationButtonEx();
                item.setRoleId(roleId);
                item.setNaviId(itemIds.get(i));
                items.add(item);
            }
            roleDao.insertNavigationButton(items);
        }
    }

}
