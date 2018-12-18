/**
 *
 */
package com.topvision.platform.dao;

import com.topvision.framework.dao.BaseEntityDao;
import com.topvision.platform.domain.Department;
import com.topvision.platform.domain.UserEx;

/**
 * @author niejun
 */
public interface DepartmentDao extends BaseEntityDao<Department> {

    /**
     * 加入一个用户部门
     * 
     * @param userEx
     */
    void insertUserDepartment(UserEx userEx);
}
