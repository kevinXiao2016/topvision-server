package com.topvision.platform.service;

import java.util.List;

import com.topvision.exception.dao.HasBindByIPException;
import com.topvision.exception.dao.UpdatePasswdException;
import com.topvision.exception.service.AuthenticationException;
import com.topvision.exception.service.LimitIpLogonException;
import com.topvision.exception.service.UserStateException;
import com.topvision.framework.service.Service;
import com.topvision.platform.domain.FavouriteFolder;
import com.topvision.platform.domain.FunctionItem;
import com.topvision.platform.domain.GeneralPreferences;
import com.topvision.platform.domain.NavigationButton;
import com.topvision.platform.domain.Role;
import com.topvision.platform.domain.User;
import com.topvision.platform.domain.UserContext;
import com.topvision.platform.domain.UserEx;
import com.topvision.platform.domain.UserPreferences;

public interface UserService extends Service {

    /**
     * 批量删除收藏夹
     * 
     * @param folderId
     */
    void deleteFavouriteFolder(List<Long> folderId);

    /**
     * 删除收藏夹
     * 
     * @param folderId
     */
    void deleteFavouriteFolder(Long folderId);

    /**
     * 通过folderPath删除收藏夹
     * 
     * @param folderPath
     */
    void deleteFavouriteFolder(String folderPath);

    /**
     * 删除一个用户.
     * 
     * @param userIds
     */
    void deleteUser(List<Long> userIds);

    /**
     * 通过收藏夹ID获得收藏夹
     * 
     * @param folderId
     * @return
     */
    FavouriteFolder getFavouriteFolder(Long folderId);

    /**
     * 获取用户的所有角色.
     * 
     * @param userId
     * @return
     */
    List<Role> getRoleByUserId(Long userId);

    /**
     * 通过用户名获取用户对象
     * 
     * @param username
     * @return
     */
    User getUser(String username);

    /**
     * 通过用户ID获取用户扩展属性
     * 
     * @param userId
     * @return
     */
    UserEx getUserEx(Long userId);

    /**
     * 通过用户ID获取用户配置
     * 
     * @param userId
     * @return
     */
    List<UserPreferences> getUserPreferences(Long userId);

    /**
     * 判断给定的IP是否存在绑定关系.
     * 
     * @param ip
     * @return @
     */
    User hasBindByIp(String ip) throws HasBindByIPException;

    /**
     * 添加一个收藏夹
     * 
     * @param folder
     */
    void insertFavouriteFolder(FavouriteFolder folder);

    /**
     * 添加一批用户配置
     * 
     * @param preferences
     */
    void insertPreferences(List<UserPreferences> preferences);

    /**
     * 保存用户设置.
     * 
     * @param Preferences
     */
    void insertPreferences(UserPreferences Preferences);

    /**
     * 插入一个用户.
     * 
     * @param user
     */
    void insertUser(UserEx user);

    /**
     * 通过用户ID获取该用户的收藏夹
     * 
     * @param userId
     * @return
     */
    List<FavouriteFolder> loadFavouriteFolder(Long userId);

    /**
     * 载入所有的用户扩展
     * 
     * @return
     */
    List<UserEx> loadUserList();

    /**
     * 获得指定用户权限表
     * 
     * @return
     */
    String getUserAuthorityViewName(Long userId);

    /**
     * 获取指定用户的地域表
     * 
     * @param userId
     * @return
     */
    String getUserAuthorityFolderName(Long userId);

    /**
     * 获取用户界面.
     * 
     * @param uc
     */
    void loadUserUI(UserContext uc);

    /**
     * 登录
     * 
     * @param username
     *            用户名
     * @param passwd
     *            密码
     * @param ip
     *            登录IP
     * @throws UserStateException
     * @throws AuthenticationException
     */
    void loginForDesktop(String username, String passwd, String ip) throws UserStateException, AuthenticationException;

    /**
     * 删除一个用户配置
     * 
     * @param Preferences
     */
    void removePreferences(UserPreferences Preferences);

    /**
     * 保存用户常规选项.
     * 
     * @param userId
     * @param preferences
     */
    void saveGeneralPreferences(Long userId, GeneralPreferences preferences);

    /**
     * 通过IP找到一个用户
     * 
     * @param ip
     * @return
     */
    User selectUserByIp(String ip);

    /**
     * 登录验证.
     * 
     * @param username
     * @param passwd
     * @return
     * @throws AuthenticationException
     *             ,UserStateException, LimitIpLogonException
     */
    UserContext txLogin(String username, String passwd, String ip)
            throws AuthenticationException, UserStateException, LimitIpLogonException;

    /**
     * 
     * @param ip
     * @return
     */
    UserContext txLoginByIp(String ip);

    /**
     * 移动收藏夹
     * 
     * @param folder
     */
    void txMoveFavouriteFolder(FavouriteFolder folder);

    /**
     * 重命名收藏夹
     * 
     * @param folder
     */
    void txRenameFavouriteFolder(FavouriteFolder folder);

    /**
     * 更新收藏夹目录
     * 
     * @param folder
     */
    void updateFavouriteFolder(FavouriteFolder folder);

    /**
     * 修改某个用户的密码.
     * 
     * @param user
     */
    void updatePasswd(User user) throws UpdatePasswdException;

    /**
     * 更新一批用户配置
     * 
     * @param preferences
     */
    void updatePreferences(List<UserPreferences> preferences);

    /**
     * 更新用户配置
     * 
     * @param Preferences
     */
    void updatePreferences(UserPreferences Preferences);

    /**
     * 更新一个用户的状态
     * 
     * @param username
     * @param state
     */
    void updateStatusByUserName(String username, Integer state);

    /**
     * 修改用户的基本信息.
     * 
     * @param user
     */
    void updateUser(UserEx user);

    /**
     * 修改用户详细信息.
     * 
     * @param user
     */
    void updateUserDetail(User user);

    /**
     * 更新用户IP绑定功能.
     * 
     * @param userId
     * @param ip
     */
    void updateUserIpBind(Long userId, String ip);

    /**
     * 批量修改用户的密码.
     * 
     * @param userIds
     * @param passwd
     */
    void updateUserPasswd(List<Long> userIds, String passwd);

    /**
     * 修改用户的角色.
     * 
     * @param userIds
     * @param roleIds
     */
    void updateUserRole(List<Long> userIds, List<Long> roleIds);

    /**
     * 修改用户的状态.
     */
    void updateUserState(List<Long> userIds, Integer state);

    /**
     * 加载我的工作台定制化
     * 
     * @return
     */
    List<NavigationButton> loadcustomMyDesk();

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
     * 更改用户的语言
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
     * 记录用户登录退出日志
     * 
     * @param username
     *            用户名
     * @param ip
     *            登录IP
     * @param desc
     *            日志描述
     */
    void syslog(String username, String ip, String desc);

    /**
     * 获取使用单个角色的用户数
     * 
     * @return
     */
    Integer getRoleUsedCount(Long roleId);

    /**
     * 获取单个部门用户数
     * 
     * @return
     */
    Integer getDepartmentUsedCount(Long departmentId);

    /**
     * 获取单个职位用户数
     * 
     * @return
     */
    Integer getPostUsedCount(Long postId);

    /**
     * 切换用户当前根地域
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
     * 获取所有的用户
     * @return
     */
    List<User> getAllUser();

    /**
     * 获得用户可选根地域集合
     * 
     * @param userId
     * @return
     */
    // List<Long> getUserAuthFolderIds(Long userId);

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

}
