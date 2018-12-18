/**
 *
 */
package com.topvision.platform.dao.mybatis;

import org.springframework.stereotype.Repository;

import com.topvision.framework.dao.mybatis.MyBatisDaoSupport;
import com.topvision.platform.dao.UserGroupDao;
import com.topvision.platform.domain.UserGroup;

/**
 * @author kelers
 */
@Repository("userGroupDao")
public class UserGroupDaoImpl extends MyBatisDaoSupport<UserGroup> implements UserGroupDao {
    @Override
    public String getDomainName() {
        return UserGroup.class.getName();
    }
}
