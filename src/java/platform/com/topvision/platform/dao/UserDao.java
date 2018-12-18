package com.topvision.platform.dao;

import java.util.List;
import java.util.Map;

import com.topvision.exception.dao.HasBindByIPException;
import com.topvision.exception.dao.UpdatePasswdException;
import com.topvision.framework.dao.BaseEntityDao;
import com.topvision.platform.domain.FunctionItem;
import com.topvision.platform.domain.User;
import com.topvision.platform.domain.UserAuthFolder;
import com.topvision.platform.domain.UserEx;
import com.topvision.platform.domain.UserPreferences;

/**
 * @author niejun
 */
public interface UserDao extends BaseEntityDao<UserEx> {
    /**
     * 删除一批用户
     * 
     * @param users
     */
    void deleteUserByUsername(List<String> users);

    /**
     * 获取绑定与一个IP的用户
     * 
     * @param ip
     *            绑定的IP
     * @return 用户对象
     * @throws HasBindByIPException
     */
    User hasBindByIp(String ip) throws HasBindByIPException;

    /**
     * 获取给定ip绑定的用户.
     * 
     * @param ip
     * @return
     */
    User selectByIp(String ip);

    /**
     * 通过用户名获取用户对象
     * 
     * @param username
     * @return
     */
    User selectByUsername(String username);

    /**
     * 更新最后登录时间
     * 
     * @param user
     */
    void updateLastLoginTime(User user);

    /**
     * 更新用户密码
     * 
     * @param user
     * @throws UpdatePasswdException
     */
    void updatePasswd(User user) throws UpdatePasswdException;

    /**
     * 更新用户状态
     * 
     * @param user
     */
    void updateStatusByUserName(User user);

    /**
     * 绑定一个用户到一个IP
     * 
     * @param user
     */
    void updateUserIpBind(User user);

    /**
     * 更新一批用户的密码
     * 
     * @param users
     */
    void updateUserPasswd(List<User> users);

    /**
     * 更新一批用户的状态
     * 
     * @param users
     */
    void updateUserStatus(List<User> users);

    /**
     * 更新用户信息
     * 
     * @param users
     */
    void updateUserDetail(User user);

    /**
     */
    public List<UserEx> loadUserList();

    /**
     * 加载个性化我的工作台
     * 
     * @param userId
     * @return
     */
    List<FunctionItem> loadUserWorkbence(long userId);

    /**
     * 定制我的工作台
     * 
     * @param userId
     * @param items
     */
    void updateCustomMydesck(long userId, List<FunctionItem> items);

    /**
     * 加载已选择的个性化工作台
     * 
     * @param userId
     * @return
     */
    List<String> loadCustomMyDesk(long userId);

    /**
     * 更改用户语言
     * 
     * @param userId
     * @param lang
     */
    void updateLanguage(long userId, String lang);

    /**
     * 获取用户数量
     * 
     * @return
     */
    Integer getUserCount();

    /**
     * 获取使用单个角色的用户数
     * 
     * @return
     */
    Integer getRoleUsedCount(Long roleId);

    /**
     * 获取部门的用户数
     * 
     * @return
     */
    Integer getDepartmentUsedCount(Long departmentId);

    /**
     * 获取职位的用户数
     * 
     * @return
     */
    Integer getPostUsedCount(Long postId);

    /**
     * 查询用户是否开启了声音提示的功能
     * 
     * @param userId
     * @return
     */
    Object selectUserSoundStatus(long userId);

    /**
     * 插入用户是否声音提示的状态
     * 
     * @param userSoundStatus
     * @param userId
     */
    void insertUserSoundStatus(Boolean userSoundStatus, Long userId);

    /**
     * 更改用户是否声音提示的状态
     * 
     * @param userSoundStatus
     * @param userId
     */
    void updateUserSoundStatus(Boolean userSoundStatus, Long userId);

    /**
     * 查询特定的用户选项
     * 
     * @param paramMap
     * @return
     */
    UserPreferences queryUserPreferences(Map<String, String> paramMap);

    /**
     * 插入新的用户选项
     * 
     * @param paramMap
     */
    void insertUserPreferences(Map<String, String> paramMap);

    /**
     * 更新用户选项
     * 
     * @param paramMap
     */
    void updateUserPreferences(Map<String, String> paramMap);

    /**
     * 新增或修改用户可选的地域空间
     * 
     * @param userId
     * @param folderIds
     */
    void insertOrUpdateUserAuthFolder(Long userId, List<Long> folderIds);

    /**
     * 切换用户根地域
     * 
     * @param userId
     * @param folderId
     */
    void switchRoorFolder(Long userId, Long folderId);

    /**
     * 设置用户是否允许多点登录
     * 
     * @param userId
     * @param allowMutliIpLoginStatus
     */
    void updateMutilIpLogin(long userId, boolean allowMutliIpLoginStatus);

    /**
     * 设置用户超时时间
     * 
     * @param userId
     * @param timeout
     */
    void updateUserSession(long userId, int timeout);

    /**
     * 获取指定用户的可选根地域
     * 
     * @param userId
     * @return
     */
    // List<Long> getUserAuthFolderIds(Long userId);

    /**
     * 获取所有的用户信息，包括superadmin
     * 
     * @return
     */
    List<User> getAllUser();
    
    /**
     * 获取所有用户的权限地域信息
     * @return
     */
    List<UserAuthFolder> getAllUserAuthFolder();
    
    /**
     * 获得用户关注的告警ID
     * 
     * @param userId
     * @return
     */
    List<Integer> getUserAlertTypeId(Long userId);
    
    /**
     * 更新用户关注的告警ID
     * 
     * @param userId
     * @param alertTypeId
     */
    void updateUserAlertType(Long userId, List<Integer> alertTypeId);

    /**
     * 插入用户信息
     * @param
     * @return 
     */
    void insertUsersInfo(UserEx user);}
