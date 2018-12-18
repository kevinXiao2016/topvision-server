package com.topvision.platform.service;

import java.util.List;

import com.topvision.framework.service.Service;
import com.topvision.platform.domain.SystemLog;
import com.topvision.platform.domain.UserGroup;

public interface UserGroupService extends Service {

    /**
     * 创建给定的用户组.
     * 
     * @param sysLog
     * @param group
     */
    void createUserGroup(SystemLog sysLog, UserGroup group);

    /**
     * 删除给定ID集合的用户组.
     * 
     * @param groupIds
     */
    void deleteUserGroup(SystemLog sysLog, List<Long> groupIds);

    /**
     * 得到所有用户组.
     * 
     * @return
     */
    List<UserGroup> getAllUserGroup();

}
