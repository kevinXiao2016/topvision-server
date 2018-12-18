/**
 *
 */
package com.topvision.platform.dao.mybatis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import com.topvision.exception.dao.HasBindByIPException;
import com.topvision.exception.dao.UpdatePasswdException;
import com.topvision.framework.dao.mybatis.MyBatisDaoSupport;
import com.topvision.platform.dao.UserDao;
import com.topvision.platform.domain.FunctionItem;
import com.topvision.platform.domain.User;
import com.topvision.platform.domain.UserAuthFolder;
import com.topvision.platform.domain.UserEx;
import com.topvision.platform.domain.UserPreferences;

/**
 * @author niejun
 */
@Repository("userDao")
public class UserDaoImpl extends MyBatisDaoSupport<UserEx> implements UserDao {

    @Override
    public String getDomainName() {
        return User.class.getName();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.platform.dao.UserDao#deleteUserByUsername(java.util.List)
     */
    @Override
    public void deleteUserByUsername(final List<String> users) {
        if (users == null || users.size() == 0) {
            return;
        }
        SqlSession session = getBatchSession();
        try {
            for (String user : users) {
                session.delete(getNameSpace("deleteUserByUsername"), user);
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
     * @see com.topvision.platform.dao.UserDao#hasBindByIp(java.lang.String)
     */
    @Override
    public User hasBindByIp(String ip) throws HasBindByIPException {
        try {
            return getSqlSession().selectOne(getNameSpace("hasBindByIp"), ip);
        } catch (DataAccessException e) {
            throw new HasBindByIPException(e);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.platform.system.dao.UserDao#selectByIp(java.lang.String)
     */
    @Override
    public User selectByIp(String ip) {
        return getSqlSession().selectOne(getNameSpace("selectByIp"), ip);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.platform.dao.UserDao#selectByUsername(java.lang.String)
     */
    @Override
    public User selectByUsername(String username) {
        return getSqlSession().selectOne(getNameSpace("selectByUsername"), username);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.platform.dao.UserDao#updateLastLoginTime(com.topvision.
     * platform.domain.User)
     */
    @Override
    public void updateLastLoginTime(User user) {
        getSqlSession().update(getNameSpace("updateLastLoginTime"), user);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.platform.dao.UserDao#updateUserDetail(com.topvision.platform
     * .domain.UserEx)
     */
    @Override
    public void updateUserDetail(User user) {
        getSqlSession().update(getNameSpace("updateUser"), user);
        // �����û���ѡ�����Ĺ�ϵ
        if (user.getUserId() != 2L) {
            insertOrUpdateUserAuthFolder(user.getUserId(), user.getUserGroupIds());
        }
    }

    @Override
    public List<UserEx> loadUserList() {
        // Map depmap =
        // getSqlSession().queryForMap(getNameSpace()+"getDepMap",null,"departmentId","name");
        // Map placemap =
        // getSqlSession().queryForMap(getNameSpace()+"getPlaceMap",null,"postId","name");
        List<UserEx> userExes = selectByMap(null);
        return userExes;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.platform.dao.UserDao#updatePasswd(com.topvision.platform .domain.User)
     */
    @Override
    public void updatePasswd(User user) throws UpdatePasswdException {
        try {
            getSqlSession().update(getNameSpace("updatePasswd"), user);
        } catch (DataAccessException e) {
            throw new UpdatePasswdException(e);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.platform.dao.UserDao#updateStatusByUserName(com.topvision
     * .platform.domain.User)
     */
    @Override
    public void updateStatusByUserName(User user) {
        getSqlSession().update(getNameSpace("updateStatusByUserName"), user);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.platform.dao.UserDao#updateUserIpBind(com.topvision.platform .domain.User)
     */
    @Override
    public void updateUserIpBind(User user) {
        getSqlSession().update(getNameSpace("updateUserIpBind"), user);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.platform.dao.UserDao#updateUserPasswd(java.util.List)
     */
    @Override
    public void updateUserPasswd(final List<User> users) {
        if (users == null || users.size() == 0) {
            return;
        }
        SqlSession session = getBatchSession();
        try {
            for (User user : users) {
                session.delete(getNameSpace("updateUserPasswd"), user);
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
     * @see com.topvision.platform.dao.UserDao#updateUserStatus(java.util.List)
     */
    @Override
    public void updateUserStatus(final List<User> users) {
        if (users == null || users.size() == 0) {
            return;
        }
        SqlSession session = getBatchSession();
        try {
            for (User user : users) {
                session.delete(getNameSpace("updateUserStatus"), user);
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
    public List<FunctionItem> loadUserWorkbence(long userId) {
        return getSqlSession().selectList(getNameSpace("loadUserWorkbence"), userId);
    }

    @Override
    public void updateCustomMydesck(final long userId, final List<FunctionItem> items) {
        SqlSession session = getBatchSession();
        try {
            session.delete(getNameSpace("deleteCustomMyDesk"), userId);
            for (FunctionItem item : items) {
                Map<String, String> map = new HashMap<String, String>();
                map.put("userId", userId + "");
                map.put("functionName", item.getFunctionName());
                map.put("functionAction", item.getFunctionAction());
                map.put("icon", item.getIcon());
                session.insert(getNameSpace("insertCustomMyDesck"), map);
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
    public List<String> loadCustomMyDesk(long userId) {
        return getSqlSession().selectList(getNameSpace("loadCustomMyDesk"), userId);
    }

    @Override
    public void updateLanguage(long userId, String lang) {
        User user = new User();
        user.setUserId(userId);
        user.setLanguage(lang);
        getSqlSession().update(getNameSpace("updateLanguage"), user);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.platform.dao.UserDao#getUserCount()
     */
    @Override
    public Integer getUserCount() {
        return getSqlSession().selectOne(getNameSpace("selectCount"));
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.platform.dao.UserDao#getRoleUsedCount(java.lang.Long)
     */
    @Override
    public Integer getRoleUsedCount(Long roleId) {
        return getSqlSession().selectOne(getNameSpace("selectRoleUsedCount"), roleId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.platform.dao.UserDao#getDepartmentUsedCount(java.lang.Long)
     */
    @Override
    public Integer getDepartmentUsedCount(Long departmentId) {
        return getSqlSession().selectOne(getNameSpace("selectDepartmentUsedCount"), departmentId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.platform.dao.UserDao#getPostUsedCount(java.lang.Long)
     */
    @Override
    public Integer getPostUsedCount(Long placeId) {
        return getSqlSession().selectOne(getNameSpace("selectPostUsedCount"), placeId);
    }

    @Override
    public Object selectUserSoundStatus(long userId) {
        return getSqlSession().selectOne(getNameSpace("selectUserSoundStatus"), userId);
    }

    @Override
    public void insertUserSoundStatus(Boolean userSoundStatus, Long userId) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("userSoundStatus", userSoundStatus.toString());
        map.put("userId", userId.toString());
        getSqlSession().insert(getNameSpace("insertUserSoundStatus"), map);
    }

    @Override
    public void updateUserSoundStatus(Boolean userSoundStatus, Long userId) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("userSoundStatus", userSoundStatus.toString());
        map.put("userId", userId.toString());
        getSqlSession().insert(getNameSpace("updateUserSoundStatus"), map);
    }

    @Override
    public UserPreferences queryUserPreferences(Map<String, String> paramMap) {
        return getSqlSession().selectOne(getNameSpace("selectUserPreferences"), paramMap);
    }

    @Override
    public void insertUserPreferences(Map<String, String> paramMap) {
        getSqlSession().insert(getNameSpace("insertUserPreferences"), paramMap);
    }

    @Override
    public void updateUserPreferences(Map<String, String> paramMap) {
        getSqlSession().update(getNameSpace("updateUserPreferences"), paramMap);
    }

    @Override
    public void insertOrUpdateUserAuthFolder(Long userId, List<Long> folderIds) {
        SqlSession session = getBatchSession();
        try {
            session.delete(getNameSpace("deleteUserAuthFolder"), userId);
            for (Long folderId : folderIds) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("userId", userId);
                map.put("folderId", folderId);
                session.insert(getNameSpace("insertUserAuthFolder"), map);
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
    public void insertEntity(List<UserEx> users) {
        SqlSession session = getBatchSession();
        try {
            for (UserEx user : users) {
                super.insertEntity(user);
                insertOrUpdateUserAuthFolder(user.getUserId(), user.getUserGroupIds());
            }
        } catch (Exception e) {
            session.rollback();
        } finally {
            session.close();
        }
    }

    @Override
    public void insertEntity(UserEx user) {
        // ��users���в�������
        super.insertEntity(user);
        // �����û����ѡ�����֮��Ķ�Ӧ��ϵ
        insertOrUpdateUserAuthFolder(user.getUserId(), user.getUserGroupIds());
    }
    
    public void insertUsersInfo(UserEx user){
		getSqlSession().insert(getNameSpace("insertEntity"), user);
        insertOrUpdateUserAuthFolder(user.getUserId(), user.getUserGroupIds());
    }

    private List<Long> getUserAuthFolderIds(Long userId) {
        return getSqlSession().selectList(getNameSpace("getUserAuthFolderIds"), userId);
    }

    @Override
    public UserEx selectByPrimaryKey(Long userId) {
        UserEx user = super.selectByPrimaryKey(userId);
        // 获取用户可选根地域
        List<Long> folderIds = getUserAuthFolderIds(userId);
        user.setUserGroupIds(folderIds);
        return user;
    }

    @Override
    public void switchRoorFolder(Long userId, Long folderId) {
        Map<String, Object> queryMap = new HashMap<String, Object>();
        queryMap.put("userId", userId);
        queryMap.put("folderId", folderId);
        getSqlSession().update(getNameSpace("switchRoorFolder"), queryMap);
    }

    @Override
    public void updateMutilIpLogin(long userId, boolean allowMutliIpLoginStatus) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("userId", userId);
        map.put("allowMutliIpLoginStatus", allowMutliIpLoginStatus);
        getSqlSession().update(getNameSpace("updateMutilIpLogin"), map);
    }

    @Override
    public void updateUserSession(long userId, int timeout) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("userId", userId);
        map.put("timeout", timeout);
        getSqlSession().update(getNameSpace("updateUserSession"), map);
    }

    @Override
    public List<User> getAllUser() {
        return getSqlSession().selectList(getNameSpace("getAllUser"));
    }

    @Override
    public List<UserAuthFolder> getAllUserAuthFolder() {
        return getSqlSession().selectList(getNameSpace("getAllUserAuthFolder"));
    }

    /* (non-Javadoc)
     * @see com.topvision.platform.dao.UserDao#getUserAlertTypeId(java.lang.Long)
     */
    @Override
    public List<Integer> getUserAlertTypeId(Long userId) {
        return getSqlSession().selectList(getNameSpace("getUserAlertTypeId"), userId);
    }

    /* (non-Javadoc)
     * @see com.topvision.platform.dao.UserDao#updateUserAlertType(java.lang.Long, java.util.List)
     */
    @Override
    public void updateUserAlertType(Long userId, List<Integer> alertTypeId) {
        getSqlSession().delete(getNameSpace("deleteUserAlertType"), userId);
        HashMap<String, Object> params = new HashMap<>();
        for (Integer typeId : alertTypeId) {
            params.clear();
            params.put("userId", userId);
            params.put("alertTypeId", typeId);
            getSqlSession().insert(getNameSpace("insertUserAlertType"), params);
        }
    }

}
