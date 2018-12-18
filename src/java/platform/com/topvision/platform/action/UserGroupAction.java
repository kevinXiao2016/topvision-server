package com.topvision.platform.action;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.topvision.framework.web.struts2.BaseAction;
import com.topvision.platform.domain.UserGroup;
import com.topvision.platform.service.UserGroupService;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Controller("userGroupAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class UserGroupAction extends BaseAction {
    private static final long serialVersionUID = 7976044164316157025L;

    private List<Long> desktopGroupIds;
    private List<Long> folderIds;
    private List<Long> serverGroupIds;
    private UserGroup userGroup;
    private long userGroupId;
    private List<Long> userGroupIds;
    @Autowired
    private UserGroupService userGroupService;

    /**
     * 创建用户组
     * 
     * @return
     */
    public String createUserGroup() {
        userGroupService.createUserGroup(null, userGroup);
        return NONE;
    }

    /**
     * 删除用户组
     * 
     * @return
     */
    public String deleteUserGroup() {
        userGroupService.deleteUserGroup(null, userGroupIds);
        return NONE;
    }

    public List<Long> getDesktopGroupIds() {
        return desktopGroupIds;
    }

    public List<Long> getFolderIds() {
        return folderIds;
    }

    public List<Long> getServerGroupIds() {
        return serverGroupIds;
    }

    public UserGroup getUserGroup() {
        return userGroup;
    }

    public long getUserGroupId() {
        return userGroupId;
    }

    public List<Long> getUserGroupIds() {
        return userGroupIds;
    }

    /**
     * 载入用户组
     * 
     * @return
     * @throws Exception
     */
    public String loadUserGroups() throws Exception {
        JSONObject json = new JSONObject();
        List<UserGroup> list = userGroupService.getAllUserGroup();
        JSONArray array = new JSONArray();
        int size = list == null ? 0 : list.size();
        json.put("rowCount", size);
        JSONObject temp = null;
        UserGroup group = null;
        for (int i = 0; i < size; i++) {
            temp = new JSONObject();
            group = list.get(i);
            temp.put("userGroupId", group.getUserGroupId());
            temp.put("name", group.getName());
            temp.put("description", group.getDescription());
            // temp.put("createTime",
            // DateUtils.format(group.getCreateTime()));
            array.add(temp);
        }
        json.put("data", array);
        writeDataToAjax(json);
        return NONE;
    }

    public void setDesktopGroupIds(List<Long> desktopGroupIds) {
        this.desktopGroupIds = desktopGroupIds;
    }

    public void setFolderIds(List<Long> folderIds) {
        this.folderIds = folderIds;
    }

    public void setServerGroupIds(List<Long> serverGroupIds) {
        this.serverGroupIds = serverGroupIds;
    }

    public void setUserGroup(UserGroup userGroup) {
        this.userGroup = userGroup;
    }

    public void setUserGroupId(long userGroupId) {
        this.userGroupId = userGroupId;
    }

    public void setUserGroupIds(List<Long> userGroupIds) {
        this.userGroupIds = userGroupIds;
    }

    public void setUserGroupService(UserGroupService userGroupService) {
        this.userGroupService = userGroupService;
    }

}
