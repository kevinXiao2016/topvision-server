package com.topvision.platform.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.exception.service.ExistException;
import com.topvision.framework.service.BaseService;
import com.topvision.platform.dao.DepartmentDao;
import com.topvision.platform.dao.PostDao;
import com.topvision.platform.dao.RoleDao;
import com.topvision.platform.dao.SystemLogDao;
import com.topvision.platform.dao.UserDao;
import com.topvision.platform.domain.Department;
import com.topvision.platform.domain.Post;
import com.topvision.platform.domain.Role;
import com.topvision.platform.domain.FunctionItemEx;
import com.topvision.platform.service.SystemService;

@Service("systemService")
public class SystemServiceImpl extends BaseService implements SystemService {
    @Autowired
    private UserDao userDao = null;
    @Autowired
    private RoleDao roleDao = null;
    @Autowired
    private DepartmentDao departmentDao = null;
    @Autowired
    private PostDao postDao = null;
    @Autowired
    private SystemLogDao systemLogDao = null;

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.server.system.service.SystemService#
     * createDepatment(com.topvision.ems.server.system.domain.Department)
     */
    @Override
    public void createDepatment(Department department) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("name", department.getName());
        map.put("superiorId", "" + department.getSuperiorId());
        List<Department> temp = departmentDao.selectByMap(map);
        if (temp != null && !temp.isEmpty()) {
            throw new ExistException("Exist Department " + department.getName());
        }
        departmentDao.insertEntity(department);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.server.system.service.SystemService#
     * createPost(com.topvision.ems.server.system.domain.Post)
     */
    @Override
    public void createPost(Post post) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("name", post.getName());
        map.put("superiorId", "" + post.getSuperiorId());
        List<Post> temp = postDao.selectByMap(map);
        if (temp != null && !temp.isEmpty()) {
            throw new ExistException("Exist Post " + post.getName());
        }
        postDao.insertEntity(post);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.server.system.service.SystemService#
     * createRole(com.topvision.ems.server.system.domain.Role)
     */
    @Override
    public void createRole(Role role) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("name", role.getName());
        map.put("superiorId", "" + role.getSuperiorId());
        List<Role> temp = roleDao.selectByMap(map);
        if (temp != null && !temp.isEmpty()) {
            throw new ExistException("Exist Department " + role.getName());
        }
        roleDao.insertEntity(role);
        List<Role> roles = roleDao.selectByMap(map);
        Long roleId = roles.get(0).getRoleId();
        //默认加上导航控制权限
        FunctionItemEx fie1 = new FunctionItemEx();
        fie1.setRoleId(roleId);
        fie1.setFunctionId(1);
        //默认加上操作控制权限
        FunctionItemEx fie2 = new FunctionItemEx();
        fie2.setRoleId(roleId);
        fie2.setFunctionId(2);
        //默认加上我的工作台权限
        FunctionItemEx fie1000000 = new FunctionItemEx();
        fie1000000.setRoleId(roleId);
        fie1000000.setFunctionId(1000000);
        //默认加上我的桌面权限
        FunctionItemEx fie1000001 = new FunctionItemEx();
        fie1000001.setRoleId(roleId);
        fie1000001.setFunctionId(1000001);
        List<FunctionItemEx> fies = new ArrayList<FunctionItemEx>();
        fies.add(fie1);
        fies.add(fie2);
        fies.add(fie1000000);
        fies.add(fie1000001);
        roleDao.insertFunctionItem(fies);
    }

    @Override
    public List<Role> getRole(Role role) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("name", role.getName());
        map.put("superiorId", "" + role.getSuperiorId());
        return roleDao.selectByMap(map);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.server.system.service.SystemService# getAllDepartment()
     */
    @Override
    public List<Department> getAllDepartment() {
        return departmentDao.selectByMap(null);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.server.system.service.SystemService# getAllPost()
     */
    @Override
    public List<Post> getAllPost() {
        return postDao.selectByMap(null);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.server.system.service.SystemService# getAllRole()
     */
    @Override
    public List<Role> getAllRole() {
        return roleDao.selectByMap(null);
    }

    public RoleDao getRoleDao() {
        return roleDao;
    }

    public SystemLogDao getSystemLogDao() {
        return systemLogDao;
    }

    public UserDao getUserDao() {
        return userDao;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.server.system.service.SystemService# removeDepartment(Long)
     */
    @Override
    public void removeDepartment(Long departmentId) {
        departmentDao.deleteByPrimaryKey(departmentId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.server.system.service.SystemService# removePost(Long)
     */
    @Override
    public void removePost(Long postId) {
        postDao.deleteByPrimaryKey(postId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.server.system.service.SystemService# removeRole(Long)
     */
    @Override
    public void removeRole(Long roleId) {
        roleDao.deleteByPrimaryKey(roleId);
    }

    public void setDepartmentDao(DepartmentDao departmentDao) {
        this.departmentDao = departmentDao;
    }

    public void setPostDao(PostDao postDao) {
        this.postDao = postDao;
    }

    public void setRoleDao(RoleDao roleDao) {
        this.roleDao = roleDao;
    }

    public void setSystemLogDao(SystemLogDao systemLogDao) {
        this.systemLogDao = systemLogDao;
    }

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

}
