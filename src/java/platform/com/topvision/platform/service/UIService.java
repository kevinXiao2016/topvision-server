package com.topvision.platform.service;

import java.util.List;

import com.topvision.framework.service.Service;
import com.topvision.platform.domain.FunctionItem;
import com.topvision.platform.domain.MenuItem;
import com.topvision.platform.domain.NavigationButton;
import com.topvision.platform.domain.ToolbarButton;

/**
 * @author kelers
 * @Create Date Oct 24, 2009 9:45:35 AM
 */
public interface UIService extends Service {

    /**
     * 
     * @param name
     * @return
     */
    FunctionItem getFunctionItemByName(String name);

    /**
     * 通过用户ID获取菜单列表
     * 
     * @param userId
     * @return
     */
    List<MenuItem> getMenuItemByUser(Long userId);

    /**
     * 通过用户ID获取导航按钮列表
     * 
     * @param userId
     * @return
     */
    List<NavigationButton> getNavigationButtonByUser(Long userId);

    /**
     * 通过用户ID获取工具栏按钮列表
     * 
     * @param userId
     * @return
     */
    List<ToolbarButton> getToolbarButtonByUser(Long userId);

    /**
     * 通过用户ID获得用户权限列表
     * 
     * @param userId
     * @return
     */
    List<String> getUserPowerByUser(Long userId);

    /**
     * 通过角色ID载入按钮列表
     * 
     * @param roleId
     * @return
     */
    List<ToolbarButton> loadButtonByRole(Long roleId);

    /**
     * 
     * @return
     */
    List<FunctionItem> loadFunctionItem();

    /**
     * 
     * @param roleId
     * @return
     */
    List<FunctionItem> loadFunctionItemByRole(Long roleId);

    /**
     * 载入按钮列表
     * 
     * @return
     */
    List<MenuItem> loadMenuItem();

    /**
     * 通过角色获得菜单列表
     * 
     * @param roleId
     * @return
     */
    List<MenuItem> loadMenuItemByRole(Long roleId);

    /**
     * 载入菜单列表
     * 
     * @return
     */
    List<MenuItem> loadMenus();

    /**
     * 载入导航按钮
     * 
     * @param roleid
     * @return
     */
    List<NavigationButton> loadNavigationButtonByRole(Long roleid);

    /**
     * 载入所有导航按钮
     * 
     * @return
     */
    List<NavigationButton> loadNavigationButtons();

    /**
     * 载入所有工具栏按钮
     * 
     * @return
     */
    List<ToolbarButton> loadToolbarButton();

    /**
     * 更新角色的操作权限.
     * 
     * @param roleId
     * @param itemIds
     */
    void txUpdateFunctionItem(Long roleId, List<Long> itemIds);

    /**
     * 更新角色导航按钮权限.
     * 
     * @param roleId
     * @param itemIds
     */
    void txUpdateNaviButton(Long roleId, List<Integer> itemIds);

}
