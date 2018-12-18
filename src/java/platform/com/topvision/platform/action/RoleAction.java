package com.topvision.platform.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.struts2.ServletActionContext;
import org.dom4j.Element;
import org.dom4j.tree.DefaultElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.topvision.exception.service.ExistException;
import com.topvision.framework.web.dhtmlx.DefaultDhtmlxHandler;
import com.topvision.framework.web.dhtmlx.DhtmlxTreeOutputter;
import com.topvision.framework.web.struts2.BaseAction;
import com.topvision.framework.web.util.HttpResponseUtils;
import com.topvision.platform.ResourceManager;
import com.topvision.platform.domain.FunctionItem;
import com.topvision.platform.domain.NavigationButton;
import com.topvision.platform.domain.Role;
import com.topvision.platform.service.SystemService;
import com.topvision.platform.service.UIService;
import com.topvision.platform.service.UserService;
import com.topvision.platform.util.EscapeUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Controller("roleAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class RoleAction extends BaseAction {
    private static final long serialVersionUID = -5619814988627186597L;
    private List<Long> functionIds = null;
    private long roleId = Role.ADMINISTRATOR_ID;
    @Autowired
    private SystemService systemService = null;
    @Autowired
    private UserService userService = null;
    @Autowired
    private UIService uiService;
    private Role role = null;
    private List<NavigationButton> naviBars;
    private List<Integer> naviIds;
    private String roleName = null;
    private Long time;

    /**
     * 创建角色
     * 
     * @return
     */
    public String createRole() throws Exception {
        JSONObject json = new JSONObject();
        try {
            systemService.createRole(role);
            json.put("exists", false);
        } catch (ExistException ee) {
            json.put("exists", true);
        } finally {
            writeDataToAjax(json);
        }
        return NONE;
    }

    /**
     * 在添加新角色时判断角色名是否已存在
     * 
     * @return
     * @throws Exception
     */
    public String isRoleNameExist() throws Exception {
        JSONObject json = new JSONObject();
        List<Role> roleList = systemService.getRole(role);
        if (roleList != null && !roleList.isEmpty()) {
            json.put("exists", true);
        } else {
            json.put("exists", false);
        }
        writeDataToAjax(json);
        return NONE;
    }

    /**
     * 删除角色
     * 
     * @return
     */
    public String deleteRole() throws Exception {
        JSONObject json = new JSONObject();
        Integer count = userService.getRoleUsedCount(roleId);
        if (roleId > Role.ADMINISTRATOR_ID) {
            if (count > 0) {
                json.put("exists", true);
            } else {
                systemService.removeRole(roleId);
            }
        }
        writeDataToAjax(json);
        return NONE;
    }

    /**
     * 载入
     * 
     * @return
     * @throws IOException
     */
    public String loadAllRole() throws IOException {
        final ResourceManager resourceManager = ResourceManager
                .getResourceManager("com.topvision.ems.resources.resources");
        HttpResponseUtils.setXMLResponse(ServletActionContext.getResponse());
        List<Role> list = systemService.getAllRole();
        DefaultDhtmlxHandler handler = new DefaultDhtmlxHandler(list) {
            @Override
            public Element buildElement(Object obj) {
                Role item = (Role) obj;
                Element el = new DefaultElement("item");
                el.addAttribute("id", String.valueOf(item.getRoleId()));
                el.addAttribute("text", resourceManager.getString(item.getName()));
                if (item.getNote() != null) {
                    el.addAttribute("tooltip", item.getNote());
                }
                el.addAttribute("open", "1");
                el.addAttribute("im0", "role.png");
                el.addAttribute("im1", "role.png");
                el.addAttribute("im2", "role.png");
                return el;
            }
        };
        DhtmlxTreeOutputter.output(handler, ServletActionContext.getResponse().getOutputStream());
        return NONE;
    }

    /**
     * 根据角色载入导航按钮
     * 
     * @return
     * @throws Exception
     */
    public String loadNaviButtonByRole() throws Exception {
        JSONArray array = new JSONArray();
        List<NavigationButton> buttons = uiService.loadNavigationButtonByRole(roleId);
        if (buttons != null) {
            JSONObject json = null;
            NavigationButton button = null;
            for (int i = 0; i < buttons.size(); i++) {
                button = buttons.get(i);
                json = new JSONObject();
                json.put("name", button.getName());
                json.put("naviId", button.getNaviId());
                array.add(json);
            }
        }
        writeDataToAjax(array);
        return NONE;
    }

    /**
     * 根据角色载入权限
     * 
     * @return
     * @throws IOException
     */
    public String loadPowerByRole() throws IOException {
        final ResourceManager resourceManager = ResourceManager
                .getResourceManager("com.topvision.ems.resources.resources");
        HttpResponseUtils.setXMLResponse(ServletActionContext.getResponse());
        List<FunctionItem> funcs = uiService.loadFunctionItem();
        List<FunctionItem> list = uiService.loadFunctionItemByRole(roleId);
        final Set<String> set = new HashSet<String>();
        int size = list == null ? 0 : list.size();
        for (int i = 0; i < size; i++) {
            set.add(String.valueOf(list.get(i).getFunctionId()));
        }
        DefaultDhtmlxHandler handler = new DefaultDhtmlxHandler(funcs) {
            @Override
            public Element buildElement(Object obj) {
                FunctionItem item = (FunctionItem) obj;
                Element el = new DefaultElement("item");
                String id = String.valueOf(item.getFunctionId());
                el.addAttribute("id", id);
                el.addAttribute("text", resourceManager.getString(item.getDisplayName()));
                el.addAttribute("open", "1");
                el.addAttribute("im0", "key.png");
                el.addAttribute("im1", "folderOpen.gif");
                el.addAttribute("im2", "folderClosed.gif");
                if (roleId == Role.ADMINISTRATOR_ID || set.contains(id)) {
                    el.addAttribute("checked", "1");
                } else {
                    el.addAttribute("checked", "");
                }
                // 所有角色都给予操作控制，导航控制、我的工作台，我的桌面权限，并且不让修改
                if (item.getFunctionId() == 1 || item.getFunctionId() == 2 || item.getFunctionId() == 1000000) {
                    el.addAttribute("nocheckbox", "1");
                }
                return el;
            }
        };
        DhtmlxTreeOutputter.output(handler, ServletActionContext.getResponse().getOutputStream());
        return NONE;
    }

    /**
     * 载入用户角色
     * 
     * @return
     * @throws Exception
     */
    public String loadUserRole() throws Exception {
        JSONArray array = new JSONArray();
        HashMap<String, JSONObject> map = new HashMap<String, JSONObject>();
        List<Role> list = systemService.getAllRole();
        List<String> userRoles = null;
        if (roleName != null) {
            roleName = EscapeUtil.unescape(roleName);
            userRoles = new ArrayList<String>();
            String[] tmps = roleName.split(",");
            for (String tmp : tmps) {
                if (tmp != null && !tmp.trim().equalsIgnoreCase("")) {
                    userRoles.add(tmp.trim());
                }
            }
        }
        for (Role role : list) {
            JSONObject json = new JSONObject();
            json.put("text", role.getName());
            json.put("expanded", true);
            json.put("id", role.getRoleId());
            json.put("children", new JSONArray());
            json.put("icon", "../images/system/user.png");
            if (userRoles != null && !userRoles.isEmpty() && userRoles.contains(role.getName())) {
                json.put("checked", true);
            } else {
                json.put("checked", false);
            }
            map.put(String.valueOf(role.getRoleId()), json);
            JSONObject parent = map.get(String.valueOf(role.getSuperiorId()));
            if (parent == null) {
                array.add(json);
            } else {
                parent.getJSONArray("children").add(json);
            }
        }
        writeDataToAjax(array);
        return NONE;
    }

    /**
     * 保存权限
     * 
     * @return
     */
    public String savePower() {
        if (roleId == Role.ADMINISTRATOR_ID) {
            return NONE;
        }
        // modify by haojie 我的工作台权限（1000000）默认不可修改，所有角色均有此权限
        functionIds.add(1000000L);
        uiService.txUpdateFunctionItem(roleId, functionIds);
        return NONE;
    }

    /**
     * 保存角色的导航按钮权限
     * 
     * @return
     */
    public String saveRoleNaviPower() {
        uiService.txUpdateNaviButton(roleId, naviIds);
        return NONE;
    }

    /**
     * 保存角色的导航按钮
     * 
     * @return
     */
    public String showNaviBarsForRole() {
        naviBars = uiService.loadNavigationButtons();
        return SUCCESS;
    }

    /**
     * 显示新建角色
     * 
     * @return
     */
    public String showNewRole() {
        return SUCCESS;
    }

    /**
     * 显示新建角色权限
     * 
     * @return
     */
    public String showPowerForRole() {
        return SUCCESS;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public List<Long> getFunctionIds() {
        return functionIds;
    }

    public List<NavigationButton> getNaviBars() {
        return naviBars;
    }

    public List<Integer> getNaviIds() {
        return naviIds;
    }

    public Role getRole() {
        return role;
    }

    public long getRoleId() {
        return roleId;
    }

    public void setFunctionIds(List<Long> functionIds) {
        this.functionIds = functionIds;
    }

    public void setNaviBars(List<NavigationButton> naviBars) {
        this.naviBars = naviBars;
    }

    public void setNaviIds(List<Integer> naviIds) {
        this.naviIds = naviIds;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public void setRoleId(long roleId) {
        this.roleId = roleId;
    }

    public void setSystemService(SystemService systemService) {
        this.systemService = systemService;
    }

    public void setUiService(UIService uiService) {
        this.uiService = uiService;
    }

    public UserService getUserService() {
        return userService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

}
