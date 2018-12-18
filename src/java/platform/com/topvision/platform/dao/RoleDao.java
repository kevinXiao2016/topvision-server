/**
 *
 */
package com.topvision.platform.dao;

import java.util.List;

import com.topvision.framework.dao.BaseEntityDao;
import com.topvision.platform.domain.FunctionItem;
import com.topvision.platform.domain.FunctionItemEx;
import com.topvision.platform.domain.MenuItem;
import com.topvision.platform.domain.NavigationButton;
import com.topvision.platform.domain.NavigationButtonEx;
import com.topvision.platform.domain.Role;
import com.topvision.platform.domain.RoleFunctionRela;
import com.topvision.platform.domain.ToolbarButton;
import com.topvision.platform.domain.UserRoleRela;

/**
 * @author niejun
 */
public interface RoleDao extends BaseEntityDao<Role> {

    /**
     * 
     * @param roleId
     */
    void deleteFunctionItem(Long roleId);

    /**
     * 通过角色id删除导航按钮
     * 
     * @param roleId
     */
    void deleteNavigationButton(Long roleId);

    /**
     * 批量删除角色
     * 
     * @param userIds
     */
    void deleteRoleByUser(final List<Long> userIds);

    /**
     * 根据角色ID获取所有的功能项.
     * 
     * @param roleId
     * @return
     */
    List<FunctionItem> getFunctionItemByRole(Long roleId);

    /**
     * 获取给定用户的所有功能项.
     * 
     * @param userId
     * @return
     */
    List<FunctionItem> getFunctionItemByUser(Long userId);

    /**
     * 通过角色ID获取MenuItem
     * 
     * @param roleId
     * @return
     */
    List<MenuItem> getMenuItemByRole(Long roleId);

    /**
     * 通过用户获得MenuItem
     * 
     * @param userId
     * @return
     */
    List<MenuItem> getMenuItemByUser(Long userId);

    /**
     * 通过角色获得导航按钮
     * 
     * @param roleId
     * @return
     */
    List<NavigationButton> getNavigationButtonByRole(Long roleId);

    /**
     * 通过用户ID获得导航按钮
     * 
     * @param userId
     * @return
     */
    List<NavigationButton> getNavigationButtonByUser(Long userId);

    /**
     * 通过用户ID获取角色列表
     * 
     * @param userId
     * @return
     */
    List<Role> getRoleByUser(Long userId);

    /**
     * 通过用户获取工具栏按钮
     * 
     * @param userId
     * @return
     */
    List<ToolbarButton> getToolbarButtonByUser(Long userId);

    /**
     * 通过用户查询用户权限
     * 
     * @param userId
     * @return
     */
    List<String> getUserPowerByUser(Long userId);

    /**
     * 获得所有用户角色
     * 
     * @return
     */
    List<UserRoleRela> getUserRoles();

    /**
     * 
     * @param items
     */
    void insertFunctionItem(List<FunctionItemEx> items);

    /**
     * 添加一批导航按钮
     * 
     * @param items
     */
    void insertNavigationButton(List<NavigationButtonEx> items);

    /**
     * 添加一批用户角色
     * 
     * @param items
     */
    void inserUserRole(List<UserRoleRela> items);

    /**
     * 获取所有角色
     * 
     * @return
     */
    List<Role> getAllRoles();

    /**
     * 获取所有角色对功能的控制信息
     * @return
     */
    List<RoleFunctionRela> getAllRoleFunctionRela();
}
