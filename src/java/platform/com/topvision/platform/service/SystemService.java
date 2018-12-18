/**
 *
 */
package com.topvision.platform.service;

import java.util.List;

import com.topvision.framework.service.Service;
import com.topvision.platform.domain.Department;
import com.topvision.platform.domain.Post;
import com.topvision.platform.domain.Role;

/**
 * @author niejun
 */
public interface SystemService extends Service {

    /**
     * 创建部门
     * 
     * @param department
     *            部门
     */
    void createDepatment(Department department);

    /**
     * 创建标记
     * 
     * @param post
     *            标记
     */
    void createPost(Post post);

    /**
     * 创建角色
     * 
     * @param role
     */
    void createRole(Role role);

    /**
     * 获取角色
     * @param role
     * @return
     */
    List<Role> getRole(Role role);

    /**
     * 获得所有部门
     * 
     * @return 部门列表
     */
    List<Department> getAllDepartment();

    /**
     * 获得所有标记
     * 
     * @return 标记列表
     */
    List<Post> getAllPost();

    /**
     * 获得所有角色
     * 
     * @return 角色列表
     */
    List<Role> getAllRole();

    /**
     * 删除部门
     * 
     * @param departmentId
     *            部门ID
     */
    void removeDepartment(Long departmentId);

    /**
     * 删除标记
     * 
     * @param postId
     *            标记ID
     */
    void removePost(Long postId);

    /**
     * 删除角色
     * 
     * @param roleId
     *            角色ID
     */
    void removeRole(Long roleId);
}
