package com.topvision.platform.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.framework.service.BaseService;
import com.topvision.platform.dao.SystemLogDao;
import com.topvision.platform.dao.UserGroupDao;
import com.topvision.platform.domain.SystemLog;
import com.topvision.platform.domain.UserGroup;
import com.topvision.platform.service.UserGroupService;

@Service("userGroupService")
public class UserGroupServiceImpl extends BaseService implements UserGroupService {
    @Autowired
    private SystemLogDao systemLogDao;
    @Autowired
    private UserGroupDao userGroupDao;

    @Override
    public void createUserGroup(SystemLog sysLog, UserGroup group) {
        userGroupDao.insertEntity(group);
        if (sysLog != null) {
            systemLogDao.insertEntity(sysLog);
        }
    }

    @Override
    public void deleteUserGroup(SystemLog sysLog, List<Long> groupIds) {
        userGroupDao.deleteByPrimaryKey(groupIds);
        if (sysLog != null) {
            systemLogDao.insertEntity(sysLog);
        }
    }

    @Override
    public List<UserGroup> getAllUserGroup() {
        return userGroupDao.selectByMap(null);
    }

    public void setSystemLogDao(SystemLogDao systemLogDao) {
        this.systemLogDao = systemLogDao;
    }

    public void setUserGroupDao(UserGroupDao userGroupDao) {
        this.userGroupDao = userGroupDao;
    }

}
