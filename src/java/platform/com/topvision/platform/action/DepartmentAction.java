package com.topvision.platform.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.topvision.exception.service.ExistException;
import com.topvision.framework.domain.TreeNode;
import com.topvision.framework.web.struts2.BaseAction;
import com.topvision.platform.domain.Department;
import com.topvision.platform.service.SystemService;
import com.topvision.platform.service.UserService;

import net.sf.json.JSONObject;

@Controller("departmentAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class DepartmentAction extends BaseAction {
    private static final long serialVersionUID = 7675538787028585221L;
    @Autowired
    private SystemService systemService = null;
    @Autowired
    private UserService userService = null;
    private long departmentId;
    private long superiorId;
    private Department department = null;

    /**
     * 载入所有部门列表
     * 
     * @return Action指定字符串
     * @throws Exception
     */
    public String loadAllDepartment() throws Exception {
        //JSONArray array = new JSONArray();
        List<TreeNode> array = new ArrayList<TreeNode>();
        HashMap<String, TreeNode> map = new HashMap<String, TreeNode>();
        List<Department> list = null;
        list = systemService.getAllDepartment();
        for (int i = 0; i < list.size(); i++) {
            Department department = list.get(i);
            TreeNode treeNode = new TreeNode();
            treeNode.setText(department.getName());
            treeNode.setId(department.getDepartmentId());
            treeNode.setIconCls("departmentIcon");
            //json.put("text", department.getName());
            //if (department.getNote() != null) {
            //    json.put("qtip", department.getNote());
            //}
            //json.put("expanded", true);
            //json.put("id", department.getDepartmentId());
            //json.put("children", new ArrayList<>());
            //json.put("iconCls", "departmentIcon");
            TreeNode parent = map.get(String.valueOf(department.getSuperiorId()));
            if (parent == null) {
                array.add(treeNode);
            } else {
                parent.getChildren().add(treeNode);
                /*List<JSONObject> children = (List<JSONObject>) parent.get("children");//.add(json);
                children.add(json);
                json.put("children", children);*/
            }
            map.put(String.valueOf(department.getDepartmentId()), treeNode);
        }
        writeDataToAjax(array);
        return NONE;
    }

    /**
     * 创建一个部门
     * 
     * @return Action指定字符串
     */
    public String createDepartment() throws Exception {
        JSONObject json = new JSONObject();
        try {
            systemService.createDepatment(department);
            json.put("exists", false);
        } catch (ExistException ee) {
            json.put("exists", true);
        } finally {
            writeDataToAjax(json);
        }
        return NONE;
    }

    /**
     * 删除一个部门
     * 
     * @return Action指定字符串
     */
    public String deleteDepartment() throws Exception {
        JSONObject json = new JSONObject();
        Integer count = userService.getDepartmentUsedCount(departmentId);
        if (departmentId > 0) {
            if (count > 0) {
                json.put("exists", true);
            } else {
                systemService.removeDepartment(departmentId);
            }
        }
        writeDataToAjax(json);
        return NONE;
    }

    /**
     * 显示新建部门的ACTION 无操作
     * 
     * @return
     */
    public String showNewDepartment() {
        return SUCCESS;
    }

    public Department getDepartment() {
        return department;
    }

    public long getDepartmentId() {
        return departmentId;
    }

    public long getSuperiorId() {
        return superiorId;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public void setDepartmentId(long departmentId) {
        this.departmentId = departmentId;
    }

    public void setSuperiorId(long superiorId) {
        this.superiorId = superiorId;
    }

    public void setSystemService(SystemService systemService) {
        this.systemService = systemService;
    }

    public SystemService getSystemService() {
        return systemService;
    }

    public UserService getUserService() {
        return userService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

}
