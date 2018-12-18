/**
 *
 */
package com.topvision.platform.dao.mybatis;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.topvision.framework.dao.mybatis.MyBatisDaoSupport;
import com.topvision.platform.dao.RoleDao;
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
@Repository("roleDao")
public class RoleDaoImpl extends MyBatisDaoSupport<Role> implements RoleDao {
    @Override
    public String getDomainName() {
        return Role.class.getName();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.server.system.dao.RoleDao#deleteFunctionItem(Long)
     */
    @Override
    public void deleteFunctionItem(Long roleId) {
        getSqlSession().delete(getNameSpace("deleteFunctionItem"), roleId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.platform.dao.RoleDao#deleteNavigationButton(java.lang.Long)
     */
    @Override
    public void deleteNavigationButton(Long roleId) {
        getSqlSession().delete(getNameSpace("deleteNavigationButton"), roleId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.platform.dao.RoleDao#deleteRoleByUser(java.util.List)
     */
    @Override
    public void deleteRoleByUser(final List<Long> userIds) {
        if (userIds == null || userIds.size() == 0) {
            return;
        }
        SqlSession session = getBatchSession();
        try {
            for (Long id : userIds) {
                session.delete(getNameSpace("deleteRoleByUser"), id);
            }
            session.commit();
        } catch (Exception e) {
            logger.error("", e);
            session.rollback();
        } finally {
            session.close();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.platform.dao.RoleDao#getFunctionItemByRole(java.lang.Long)
     */
    @Override
    public List<FunctionItem> getFunctionItemByRole(Long roleId) {
        return getSqlSession().selectList(getNameSpace("getFunctionItemByRole"), roleId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.platform.dao.RoleDao#getFunctionItemByUser(java.lang.Long)
     */
    @Override
    public List<FunctionItem> getFunctionItemByUser(Long userId) {
        return getSqlSession().selectList(getNameSpace("getFunctionItemByUser"), userId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.platform.dao.RoleDao#getMenuItemByRole(java.lang.Long)
     */
    @Override
    public List<MenuItem> getMenuItemByRole(Long roleId) {
        return getSqlSession().selectList(getNameSpace("getMenuItemByRole"), roleId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.platform.dao.RoleDao#getMenuItemByUser(java.lang.Long)
     */
    @Override
    public List<MenuItem> getMenuItemByUser(Long userId) {
        return getSqlSession().selectList(getNameSpace("getMenuItemByUser"), userId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.platform.dao.RoleDao#getNavigationButtonByRole(java.lang .Long)
     */
    @Override
    public List<NavigationButton> getNavigationButtonByRole(Long roleId) {
        return getSqlSession().selectList(getNameSpace("getNavigationButtonByRole"), roleId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.platform.dao.RoleDao#getNavigationButtonByUser(java.lang .Long)
     */
    @Override
    public List<NavigationButton> getNavigationButtonByUser(Long userId) {
        return getSqlSession().selectList(getNameSpace("getNavigationButtonByUser"), userId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.platform.dao.RoleDao#getRoleByUser(java.lang.Long)
     */
    @Override
    public List<Role> getRoleByUser(Long userId) {
        return getSqlSession().selectList(getNameSpace("getRoleByUser"), userId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.platform.dao.RoleDao#getToolbarButtonByUser(java.lang.Long)
     */
    @Override
    public List<ToolbarButton> getToolbarButtonByUser(Long userId) {
        return getSqlSession().selectList(getNameSpace("getToolbarButtonByUser"), userId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.platform.dao.RoleDao#getUserPowerByUser(java.lang.Long)
     */
    @Override
    public List<String> getUserPowerByUser(Long userId) {
        return getSqlSession().selectList(getNameSpace("getUserPowerByUser"), userId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.platform.dao.RoleDao#getUserRoles()
     */
    @Override
    public List<UserRoleRela> getUserRoles() {
        return getSqlSession().selectList(getNameSpace("getUserRoles"));
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.server.system.dao.RoleDao#insertFunctionItem(java.util .List)
     */
    @Override
    public void insertFunctionItem(final List<FunctionItemEx> items) {
        if (items == null || items.size() == 0) {
            return;
        }
        SqlSession session = getBatchSession();
        try {
            for (FunctionItemEx item : items) {
                session.update(getNameSpace("insertFunctionItemEx"), item);
            }
            session.commit();
        } catch (Exception e) {
            logger.error("", e);
            session.rollback();
        } finally {
            session.close();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.platform.dao.RoleDao#insertNavigationButton(java.util.List)
     */
    @Override
    public void insertNavigationButton(final List<NavigationButtonEx> items) {
        if (items == null || items.size() == 0) {
            return;
        }
        SqlSession session = getBatchSession();
        try {
            for (NavigationButtonEx item : items) {
                session.update(getNameSpace("insertNavigationButtonEx"), item);
            }
            session.commit();
        } catch (Exception e) {
            logger.error("", e);
            session.rollback();
        } finally {
            session.close();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.server.system.dao.RoleDao#inserUserRole(java.util.List)
     */
    @Override
    public void inserUserRole(final List<UserRoleRela> items) {
        if (items == null || items.size() == 0) {
            return;
        }
        SqlSession session = getBatchSession();
        try {
            for (UserRoleRela item : items) {
                session.update(getNameSpace("inserUserRole"), item);
            }
            session.commit();
        } catch (Exception e) {
            logger.error("", e);
            session.rollback();
        } finally {
            session.close();
        }
    }

    @Override
    public List<Role> getAllRoles() {
        return getSqlSession().selectList(getNameSpace("getAllRoles"));
    }

    @Override
    public List<RoleFunctionRela> getAllRoleFunctionRela() {
        return getSqlSession().selectList(getNameSpace("getAllRoleFunctionRela"));
    }

}
