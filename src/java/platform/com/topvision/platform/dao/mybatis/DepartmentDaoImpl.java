/**
 *
 */
package com.topvision.platform.dao.mybatis;

import org.springframework.stereotype.Repository;

import com.topvision.framework.dao.mybatis.MyBatisDaoSupport;
import com.topvision.platform.dao.DepartmentDao;
import com.topvision.platform.domain.Department;
import com.topvision.platform.domain.UserEx;

/**
 * @author niejun
 */
@Repository("departmentDao")
public class DepartmentDaoImpl extends MyBatisDaoSupport<Department> implements DepartmentDao {
    @Override
    public String getDomainName() {
        return Department.class.getName();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.platform.dao.DepartmentDao#insertUserDepartment(com.topvision
     * .platform.domain.UserEx)
     */
    @Override
    public void insertUserDepartment(UserEx userEx) {
        getSqlSession().insert(getNameSpace("insertUserDepartment"), userEx);
    }
}
