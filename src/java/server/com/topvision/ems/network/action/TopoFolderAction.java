package com.topvision.ems.network.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.network.domain.FolderCategory;
import com.topvision.ems.network.domain.MapNode;
import com.topvision.ems.network.domain.TopoFolder;
import com.topvision.ems.network.domain.TopoFolderNum;
import com.topvision.ems.network.domain.TopoFolderStat;
import com.topvision.ems.network.domain.TopoLabel;
import com.topvision.ems.network.service.NetworkConstants;
import com.topvision.ems.network.service.TopologyService;
import com.topvision.framework.domain.EntityTreeNode;
import com.topvision.framework.domain.TreeNode;
import com.topvision.framework.web.struts2.BaseAction;
import com.topvision.platform.ResourceManager;
import com.topvision.platform.domain.SystemLog;
import com.topvision.platform.domain.UserContext;
import com.topvision.platform.domain.UserPreferences;
import com.topvision.platform.service.UserPreferencesService;
import com.topvision.platform.util.CurrentRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;

@Controller("topoFolderAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class TopoFolderAction extends BaseAction {
    private static final long serialVersionUID = 3921570558716828472L;
    @Autowired
    private TopologyService topologyService;
    @Autowired
    private UserPreferencesService userPreferencesService;
    private boolean hasTopoFolder = true;
    private boolean hasGoogle = true;
    private boolean hasRecyle = true;
    private boolean hasSubnetFolder = true;
    private boolean hasSubnet = false;
    private boolean hasCheckbox = false;
    private boolean displayAllEntity;
    private long folderId = 0;
    private long nodeId = 0;
    private MapNode mapNode;
    private TopoFolder topoFolder;
    private String oldName;
    private String name;
    private String note;
    private long superiorId = 0;
    private int displayType = 0;
    private String folderSize;
    private List<Long> labelValues;
    private List<String> labelColors;
    private String labelType;
    private Integer isRegion;
    private Long topoFolderId;
    private Long entityId;
    private String folderIds;
    private Long userId;

    public String addCloudy() throws Exception {
        JSONObject json = new JSONObject();
        topoFolder.setType(TopoFolder.TYPE_CLOUDY);
        topoFolder.setIcon(TopoFolder.DEFAULT_CLOUDY_ICON);
        topoFolder.setName(getNetworkResourceManager().getString("WAN"));
        UserContext uc = (UserContext) getSession().get(UserContext.KEY);
        topologyService.txInsertTopoFolder(null, topoFolder, uc.getUser().getUserGroupId());
        json.put("id", "cell" + topoFolder.getFolderId());
        json.put("text", topoFolder.getName());
        json.put("icon", topoFolder.getIcon());
        json.put("x", topoFolder.getX());
        json.put("y", topoFolder.getY());
        json.put("objType", NetworkConstants.TYPE_FOLDER);
        json.put("userObjId", topoFolder.getFolderId());
        json.put("entityId", topoFolder.getFolderId());
        json.put("folderId", topoFolder.getFolderId());
        json.put("name", topoFolder.getName());
        json.put("type", topoFolder.getType());

        json.write(response.getWriter());

        return NONE;
    }

    public String addHrefFolder() throws Exception {
        JSONObject json = new JSONObject();
        topoFolder.setType(TopoFolder.TYPE_HYPERLINK);
        topoFolder.setIcon(TopoFolder.DEFAULT_HYPERLINK_ICON);
        topoFolder.setName(getNetworkResourceManager().getString("hreffolder"));
        UserContext uc = (UserContext) getSession().get(UserContext.KEY);
        topologyService.txInsertTopoFolder(null, topoFolder, uc.getUser().getUserGroupId());
        json.put("id", "cell" + topoFolder.getFolderId());
        json.put("text", topoFolder.getName());
        json.put("icon", topoFolder.getIcon());
        json.put("x", topoFolder.getX());
        json.put("y", topoFolder.getY());
        json.put("objType", NetworkConstants.TYPE_FOLDER);
        json.put("userObjId", topoFolder.getFolderId());
        json.put("entityId", topoFolder.getFolderId());
        json.put("folderId", topoFolder.getFolderId());
        json.put("name", topoFolder.getName());
        json.put("type", topoFolder.getType());
        json.write(response.getWriter());

        return NONE;
    }

    public String addSubnet() throws Exception {
        JSONObject json = new JSONObject();
        topoFolder.setType(TopoFolder.TYPE_SUBNET);
        topoFolder.setName(getNetworkResourceManager().getString("subnet"));
        topoFolder.setIcon(TopoFolder.DEFAULT_SUBNET_ICON);
        UserContext uc = (UserContext) getSession().get(UserContext.KEY);
        topologyService.txInsertTopoFolder(null, topoFolder, uc.getUser().getUserGroupId());
        json.put("id", "cell" + topoFolder.getFolderId());
        json.put("text", topoFolder.getName());
        json.put("icon", topoFolder.getIcon());
        json.put("x", topoFolder.getX());
        json.put("y", topoFolder.getY());
        json.put("objType", NetworkConstants.TYPE_FOLDER);
        json.put("userObjId", topoFolder.getFolderId());
        json.put("entityId", topoFolder.getFolderId());
        json.put("folderId", topoFolder.getFolderId());
        json.put("name", topoFolder.getName());
        json.put("type", topoFolder.getType());

        writeDataToAjax(json);

        return NONE;
    }

    public String createTopoFolder() throws Exception {
        UserContext uc = (UserContext) getSession().get(UserContext.KEY);

        // 添加syslog
        SystemLog sysLog = new SystemLog();
        sysLog.setIp(uc.getUser().getLastLoginIp());
        sysLog.setUserName(uc.getUser().getUserName());
        sysLog.setDescription(getNetworkResourceManager().getString("new.organization", name));

        // 插入topofolder
        TopoFolder topoFolder = new TopoFolder();
        topoFolder.setName(name);
        topoFolder.setNote(note);
        topoFolder.setSuperiorId(superiorId);
        topoFolder.setIcon(TopoFolder.DEFAULT_FOLDER_ICON);
        topologyService.txInsertTopoFolder(null, topoFolder, uc.getUser().getUserGroupId());
        // 返回数据
        /*
         * if (topoFolder.getType() == TopoFolder.TYPE_SUBNET) {
         * topoFolder.setIcon(TopoFolder.DEFAULT_SUBNET_ICON); } else {
         * topoFolder.setIcon(TopoFolder.DEFAULT_FOLDER_ICON); }
         */
        /*
         * json.put("id", "cell" + topoFolder.getFolderId()); json.put("nodeId",
         * topoFolder.getFolderId()); json.put("text", topoFolder.getName()); json.put("icon",
         * topoFolder.getIcon()); json.put("x", topoFolder.getX() == null ? 0 : topoFolder.getX());
         * json.put("y", topoFolder.getY() == null ? 0 : topoFolder.getY()); json.put("objType",
         * NetworkConstants.TYPE_FOLDER); json.put("userObjId", topoFolder.getFolderId());
         * json.put("name", topoFolder.getName()); json.put("type", topoFolder.getType());
         * 
         * write(json);
         */

        return NONE;
    }

    /**
     * 删除地域(不能删除拥有子地域或者有设备的地域)
     * 
     * @modify by fanzidong
     * @return
     * @throws Exception
     */
    public String deleteTopoFolder() throws Exception {
        JSONObject json = new JSONObject();

        UserContext uc = (UserContext) getSession().get(UserContext.KEY);
        TopoFolder folder = new TopoFolder();
        folder.setFolderId(folderId);
        folder.setSuperiorId(superiorId);
        folder.setName(name);

        // 记录syslog
        SystemLog sysLog = new SystemLog();
        sysLog.setUserName(uc.getUser().getUserName());
        sysLog.setIp(uc.getUser().getLastLoginIp());
        sysLog.setDescription(getNetworkResourceManager().getString("delete.topoFolder", folder.getName()));
        // 注释by范子冬，认为觉得判断逻辑有不够的地方
        /* boolean hasChild = topologyService.hasChild(folder); */
        boolean canDelete = topologyService.isRegionCanBeDeleted(folder);
        if (!canDelete) {
            json.put("success", false);
            json.put("message", getResourceString("TopoFolderAction.note", "com.topvision.ems.resources.resources"));
        } else {
            topologyService.txDeleteTopoFolder(sysLog, folder);
            json.put("success", true);
        }
        json.write(response.getWriter());

        return NONE;
    }

    public String getTopoFolderProperty() {
        mapNode = topologyService.getMapNode(nodeId);
        if (mapNode == null) {
            folderId = nodeId;
        } else {
            folderId = mapNode.getUserObjId();
        }
        setTopoFolder(topologyService.getTopoFolder(folderId));
        // 国际化一下名字
        this.topoFolder.setName(getResourceManager().getString(this.topoFolder.getName()));
        if (getTopoFolder().getType() == TopoFolder.TYPE_HYPERLINK) {
            return "hrefTopoFolder";
        } else if (getTopoFolder().getFolderId() != TopoFolder.SUBNET_FOLDER
                && getTopoFolder().getType() == TopoFolder.TYPE_SUBNET) {
            return "subnet";
        }

        return SUCCESS;
    }

    /**
     * 获取当前用户可访问地域集合
     * 
     * @return
     * @throws IOException
     */
    public String fetchLogonUserAuthFolders() throws IOException {
        UserContext uc = (UserContext) getSession().get(UserContext.KEY);
        List<TopoFolder> folders = topologyService.fetchUserAuthFolders(uc.getUserId());
        JSONArray folderList = convertFolderToZtreeFormat(folders);
        folderList.write(response.getWriter());
        return NONE;
    }

    /**
     * 将TopoFolder集合转换成ztree要求的格式
     * 
     * @param folders
     * @return
     */
    private JSONArray convertFolderToZtreeFormat(List<TopoFolder> folders) {
        JSONArray folderList = new JSONArray();
        for (TopoFolder folder : folders) {
            JSONObject json = new JSONObject();
            json.put("id", folder.getId());
            json.put("pId", folder.getParentId());
            json.put("name", getResourceManager().getString(folder.getName()));
            json.put("open", true);
            json.put("chkDisabled", folder.getChkDisabled());
            json.put("checked", folder.getChecked());
            folderList.add(json);
        }
        return folderList;
    }

    /**
     * 获取用户可选根地域下的地域列表
     * 
     * @since 2014-10-29 地域功能改进
     * @return
     * @throws IOException
     */
    public String fetchUserSwithableFolders() throws IOException {
        // 获取指定用户的可选根地域集合
        Long curUserId = CurrentRequest.getCurrentUser().getUserId();
        List<TopoFolder> list = topologyService.fetchUserSwithableFolders(curUserId, userId);
        JSONArray folderList = convertFolderToZtreeFormat(list);
        folderList.write(response.getWriter());
        return NONE;
    }

    /**
     * 获取指定网段的地域树
     * 
     * @return
     * @throws IOException
     */
    public String fetchNetSegmentFolders() throws IOException {
        userId = CurrentRequest.getCurrentUser().getUserId();
        List<Long> ids = new ArrayList<Long>();
        ids.add(topoFolderId);
        List<TopoFolder> list = topologyService.checkFoldersOnAllFolders(userId, ids);
        JSONArray folderList = convertFolderToZtreeFormat(list);
        folderList.write(response.getWriter());
        return NONE;
    }

    /**
     * 获取对应设备的地域树
     * 
     * @return
     * @throws IOException
     */
    public String fetchEntityLocatedFolders() throws IOException {
        // 获取指定设备所在地域列表
        userId = CurrentRequest.getCurrentUser().getUserId();
        List<Long> entityFolderIds = new ArrayList<Long>();
        if (entityId != null && entityId > 0) {
            entityFolderIds = topologyService.getEntityLocatedFolderIds(entityId);
        }
        List<TopoFolder> list = topologyService.checkFoldersOnAllFolders(userId, entityFolderIds);
        JSONArray folderList = convertFolderToZtreeFormat(list);
        folderList.write(response.getWriter());
        return NONE;
    }

    /**
     * 获取指定用户当前根地域
     * 
     * @return
     * @throws IOException
     */
    public String fetchUserCurRootFolders() throws IOException {
        List<TopoFolder> list = topologyService.fetchUserCurRootFolders(userId);
        JSONArray folderList = convertFolderToZtreeFormat(list);
        folderList.write(response.getWriter());
        return NONE;
    }

    /**
     * 获取切换根地域时应展示的树数据
     * 
     * @return
     * @throws IOException
     */
    public String fetchSwithRootFolders() throws IOException {
        List<TopoFolder> list = topologyService.fetchSwithRootFolders(userId);
        JSONArray folderList = convertFolderToZtreeFormat(list);
        folderList.write(response.getWriter());
        return NONE;
    }

    public String loadMyFolder() throws Exception {
        UserContext uc = (UserContext) getSession().get(UserContext.KEY);
        List<TopoFolder> list = topologyService.loadMyTopoFolder(uc.getUserId(), FolderCategory.CLASS_NETWORK);
        UserPreferences preference = userPreferencesService.getUserPreference(uc.getUserId(), "displayField");
        UserPreferences preference2 = userPreferencesService.getUserPreference(uc.getUserId(), "topoTreeDisplayDevice");
        String preferenceValue = null;
        if (preference != null) {
            preferenceValue = preference.getValue();
        } else {
            preferenceValue = UserPreferences.DISPLAY_FIELD_IP;
        }
        int topoTreeDisplayDevice = 1;
        if (preference2 != null) {
            topoTreeDisplayDevice = Integer.valueOf(preference2.getValue());
        }
        TreeNode rootNode = new TreeNode();
        HashMap<String, EntityTreeNode> map = new HashMap<String, EntityTreeNode>();
        // 遍历地域，构建树
        for (TopoFolder topoFolder : list) {
            EntityTreeNode treeNode = new EntityTreeNode();
            treeNode.setText(getResourceManager().getString(topoFolder.getName()));
            treeNode.setId(Long.valueOf(String.valueOf(topoFolder.getFolderId())));
            treeNode.setParentId(Long.valueOf(String.valueOf(topoFolder.getSuperiorId())));
            treeNode.setIconCls("topoFolderIcon20");
            treeNode.setNeedATag(true);
            // EMS-14857 全局个性化设置,是否在导航树中展示设备，默认展示
            if (topoTreeDisplayDevice == 1) {
                List<Entity> entities = topologyService.getVertexByFolderId(topoFolder.getFolderId());
                List<TreeNode> nodes = new ArrayList<TreeNode>();
                for (Entity $entity : entities) {
                    if ($entity.getParentId() == null) {
                    EntityTreeNode $node = new EntityTreeNode();
                        $node.setId($entity.getEntityId());
                        if (Entity.IP_DISPLAY_FIELD.equalsIgnoreCase(preferenceValue)) {
                            $node.setText($entity.getIp());
                        } else if (Entity.NAME_DISPLAY_FIELD.equalsIgnoreCase(preferenceValue)) {
                            $node.setText($entity.getName());
                        } else if (Entity.SYS_NAME_DISPLAY_FIELD.equalsIgnoreCase(preferenceValue)) {
                            $node.setText($entity.getSysName());
                    } else if (Entity.IP_NAME_DISPLAY_FIELD.equalsIgnoreCase(preferenceValue)) {
                        $node.setText(String.format("%s(%s)", $entity.getIp(), $entity.getName()));
                    } else if (Entity.NAME_IP_DISPLAY_FIELD.equalsIgnoreCase(preferenceValue)) {
                        $node.setText(String.format("%s(%s)", $entity.getName(), $entity.getIp()));
                        }
                    $node.setTypeId($entity.getTypeId());
                        $node.setEntity(true);
                        $node.setParentId(treeNode.getId());
                        nodes.add($node);
                    }
                }
                treeNode.setChildren(nodes);
            }
            map.put(String.valueOf(treeNode.getId()), treeNode);
            // 找到其父节点
            TreeNode parentNode = map.get(String.valueOf(topoFolder.getSuperiorId()));
            if (parentNode != null) {
                parentNode.getChildren().add(treeNode);
            }
        }
        // 寻找到根节点
        for (String folderId : map.keySet()) {
            TreeNode node = map.get(folderId);
            // 如果某节点的父节点不存在map中，我们认为它是根节点
            if (!map.containsKey(node.getParentId().toString())) {
                rootNode = node;
                // 将其父节点设为topo管理，只为展示树结构时使用
                rootNode.setParentId(Long.valueOf(String.valueOf(TopoFolder.TOPO_FOLDER)));
            }
        }
        // 加入拓扑管理,并将跟设为其子节点
        TreeNode topoManageNode = new TreeNode();
        topoManageNode.setText(getNetworkResourceManager().getNotNullString("folder.type1"));
        topoManageNode.setId(Long.valueOf(String.valueOf(TopoFolder.TOPO_FOLDER)));
        topoManageNode.setIconCls("folder");
        topoManageNode.setNeedATag(true);
        List<TreeNode> children = new ArrayList<TreeNode>();
        children.add(rootNode);
        topoManageNode.setChildren(children);

        JSONObject json = JSONObject.fromObject(topoManageNode);
        json.write(response.getWriter());
        return NONE;
    }

    public String loadTopoFolder() throws Exception {
        UserContext uc = (UserContext) getSession().get(UserContext.KEY);
        List<TopoFolder> list = topologyService.loadMyTopoFolder(uc.getUserId(), FolderCategory.CLASS_NETWORK);
        TreeNode rootNode = new TreeNode();
        HashMap<String, TreeNode> map = new HashMap<String, TreeNode>();
        // 遍历地域，构建树
        for (TopoFolder topoFolder : list) {
            TreeNode treeNode = new TreeNode();
            treeNode.setText(getResourceManager().getString(topoFolder.getName()));
            treeNode.setId(Long.valueOf(String.valueOf(topoFolder.getFolderId())));
            treeNode.setParentId(Long.valueOf(String.valueOf(topoFolder.getSuperiorId())));
            treeNode.setIconCls("topoFolderIcon20");
            treeNode.setNeedATag(true);
            treeNode.setChildren(new ArrayList<TreeNode>());
            map.put(String.valueOf(treeNode.getId()), treeNode);
            // 找到其父节点
            TreeNode parentNode = map.get(String.valueOf(topoFolder.getSuperiorId()));
            if (parentNode != null) {
                parentNode.getChildren().add(treeNode);
            }
        }
        // 寻找到根节点
        for (String folderId : map.keySet()) {
            TreeNode node = map.get(folderId);
            // 如果某节点的父节点不存在map中，我们认为它是根节点
            if (!map.containsKey(node.getParentId().toString())) {
                rootNode = node;
            }
        }
        JSONObject json = JSONObject.fromObject(rootNode);
        json.write(response.getWriter());
        return NONE;
    }

    /**
     * 
     * @return
     */
    public String showRenameFolder() {
        return SUCCESS;
    }

    /**
     * 组重命名
     * 
     * @return
     * @throws JSONException
     * @throws IOException
     */
    public String renameTopoFolder() throws JSONException, IOException {
        JSONObject json = new JSONObject();
        TopoFolder folder = topologyService.getTopoFolder(folderId);
        folder.setName(name);
        SystemLog sysLog = new SystemLog();
        sysLog.setUserName(CurrentRequest.getCurrentUser().getUser().getUserName());
        sysLog.setDescription(getNetworkResourceManager().getString("rename.topoFolder", oldName, folder.getName()));
        List<TopoFolder> folderList = topologyService.getTopoFolderList(folder);
        if (folderList != null && folderList.size() >= 1) {
            json.put("exists", true);
        } else {
            json.put("exists", false);
            topologyService.txRenameTopoFolder(sysLog, folder);
        }
        json.write(response.getWriter());
        return NONE;
    }

    public String setTopoMapDisplayName() {
        topologyService.updateTopoFolderDisplayName(topoFolder);
        return NONE;
    }

    public String setTopoMapDisplayNoSnmp() {
        topologyService.updateTopoFolderDisplayNoSnmp(topoFolder);
        return NONE;
    }

    public String setTopoMapZoom() {
        topologyService.updateTopoFolderZoom(topoFolder);
        return NONE;
    }

    public String statTopoFolderCols() throws Exception {
        List<String> re = topologyService.getColNames();
        re.add(0, "folderId");
        re.add(1, "name");
        writeDataToAjax(JSONArray.fromObject(re));
        return NONE;
    }

    public String statTopoFolder() throws Exception {
        List<TopoFolderStat> folders = topologyService.statTopoFolder();
        /*
         * List<Map<String, String>> res = new ArrayList<Map<String, String>>(); for (TopoFolderStat
         * topoFolderStat : folders) { Map<String, String> re = new HashMap<String, String>();
         * re.put("folderId", topoFolderStat.getFolderId().toString()); re.put("name",
         * getResourceManager().getString(topoFolderStat.getName())); for (TopoFolderNum
         * topoFolderNum : topoFolderStat.getCols()) { re.put(topoFolderNum.getColName(),
         * topoFolderNum.getNum().toString()); } res.add(re); } write(JSONArray.fromObject(res));
         */
        Map<Long, JSONObject> folderMap = new HashMap<Long, JSONObject>();
        JSONObject folderJson = null;
        for (TopoFolderStat topoFolderStat : folders) {
            folderJson = new JSONObject();
            folderJson.put("folderId", topoFolderStat.getFolderId());
            folderJson.put("name", getResourceManager().getString(topoFolderStat.getName()));
            for (TopoFolderNum topoFolderNum : topoFolderStat.getCols()) {
                folderJson.put(topoFolderNum.getColName(), topoFolderNum.getNum().toString());
            }
            folderMap.put(topoFolderStat.getFolderId(), folderJson);
            JSONObject parentJson = folderMap.get(topoFolderStat.getSuperiorId());
            if (parentJson != null) {
                folderJson.put("name", parentJson.get("name") + " / " + folderJson.getString("name"));
            }
        }
        writeDataToAjax(JSONArray.fromObject(folderMap.values()));

        return NONE;
    }

    public String updateDisplayAlertIcon() {
        topologyService.updateDisplayAlertIcon(topoFolder);
        return NONE;
    }

    public String updateDisplayAllEntity() {
        if (displayAllEntity) {
            topoFolder.setEntityForOrgin(0L);
            topoFolder.setDepthForOrgin(1);
        }
        topoFolder.setDisplaySwitch(displayAllEntity);
        topoFolder.setDisplayRouter(displayAllEntity);
        topoFolder.setDisplayL3switch(displayAllEntity);
        topoFolder.setDisplayServer(displayAllEntity);
        topoFolder.setDisplayDesktop(displayAllEntity);
        topoFolder.setDisplayOthers(displayAllEntity);
        topologyService.updateDisplayAllEntity(topoFolder);

        return NONE;
    }

    public String updateDisplayCluetip() {
        topologyService.updateDisplayCluetip(topoFolder);
        return NONE;
    }

    public String updateDisplayEntityForOrgin() {
        topologyService.updateTopoFolderOrginEntity(topoFolder);
        return NONE;
    }

    public String updateDisplayEntityLabel() {
        topologyService.updateTopoFolderDisplayEntityLabel(topoFolder);
        return NONE;
    }

    public String updateDisplayLink() {
        topologyService.updateDisplayLink(topoFolder);
        return NONE;
    }

    public String updateDisplayLinkLabel() {
        topologyService.updateTopoFolderDisplayLinkLabel(topoFolder);
        return NONE;
    }

    public String updateFolderBgColor() {
        topologyService.updateTopoFolderBgColor(topoFolder);
        return NONE;
    }

    public String updateFolderBgFlag() {
        topologyService.updateTopoFolderBgFlag(topoFolder);
        return NONE;
    }

    public String updateFolderBgImg() {
        topologyService.updateTopoFolderBgImg(topoFolder);
        return NONE;
    }

    public String updateFolderBgPosition() {
        topologyService.updateTopoFolderBgPosition(topoFolder);
        return NONE;
    }

    public String updateFolderDisplayGrid() {
        topologyService.updateTopoFolderDisplayGrid(topoFolder);
        return NONE;
    }

    public String updateFolderFixed() {
        topologyService.updateTopoFolderFixed(topoFolder);
        return NONE;
    }

    public String updateFolderLinkColor() {
        topologyService.updateTopoFolderLinkColor(topoFolder);
        return NONE;
    }

    public String updateFolderLinkWidth() {
        topologyService.updateTopoFolderLinkWidth(topoFolder);
        return NONE;
    }

    public String updateFolderRefreshInterval() {
        topologyService.updateTopoFolderRefreshInterval(topoFolder);
        return NONE;
    }

    public String updateMarkerAlertMode() {
        topologyService.updateMarkerAlertMode(topoFolder);
        return NONE;
    }

    public String updateTopoFolderDisplay() {
        topologyService.updateTopoFolderDisplayOthers(topoFolder);
        return NONE;
    }

    public String updateTopoFolderIcon() {
        topologyService.updateTopoFolderIcon(topoFolder);
        return NONE;
    }

    public String updateTopoFolderOutline() throws JSONException, IOException {
        String[] arr = folderSize.split("x");
        topoFolder.setWidth(Integer.parseInt(arr[0]));
        topoFolder.setHeight(Integer.parseInt(arr[1]));
        JSONObject json = new JSONObject();
        TopoFolder folder = topologyService.getTopoFolder(topoFolder.getFolderId());
        folder.setName(topoFolder.getName());
        List<TopoFolder> folderList = topologyService.getTopoFolderList(folder);
        if (folderList != null && folderList.size() >= 1) {
            json.put("exists", true);
            json.write(response.getWriter());
        } else {
            topologyService.txUpdateTopoFolderOutline(topoFolder);
        }
        return NONE;
    }

    public String updateTopoLabel() {
        List<TopoLabel> labels = new ArrayList<TopoLabel>();
        TopoLabel label = null;
        for (int i = 0; i < labelValues.size(); i++) {
            label = new TopoLabel();
            label.setFolderId(folderId);
            label.setValue(labelValues.get(i));
            label.setColor(labelColors.get(i));
            label.setModule(labelType);
            label.setLabelId(labelType + i);
            labels.add(label);
        }
        topologyService.updateTopoLabel(folderId, labelType, labels);

        return NONE;
    }

    public String updateTopoMapLinkShadow() {
        topologyService.updateTopoFolderLinkShadow(topoFolder);
        return NONE;
    }

    private ResourceManager getNetworkResourceManager() {
        return ResourceManager.getResourceManager("com.topvision.ems.network.resources");
    }

    private ResourceManager getResourceManager() {
        return ResourceManager.getResourceManager("com.topvision.ems.resources.resources");
    }

    /**
     * key：properties文件的keymodule：资源文件
     */
    protected String getResourceString(String key, String module) {
        try {
            ResourceManager resourceManager = ResourceManager.getResourceManager(module);
            return resourceManager.getNotNullString(key);
        } catch (Exception e) {
            return key;
        }
    }

    public int getDisplayType() {
        return displayType;
    }

    public long getFolderId() {
        return folderId;
    }

    public String getFolderSize() {
        return folderSize;
    }

    public List<String> getLabelColors() {
        return labelColors;
    }

    public String getLabelType() {
        return labelType;
    }

    public List<Long> getLabelValues() {
        return labelValues;
    }

    public MapNode getMapNode() {
        return mapNode;
    }

    public String getName() {
        return name;
    }

    public long getNodeId() {
        return nodeId;
    }

    public String getOldName() {
        return oldName;
    }

    public long getSuperiorId() {
        return superiorId;
    }

    public TopoFolder getTopoFolder() {
        return topoFolder;
    }

    public Integer getIsRegion() {
        return isRegion;
    }

    public void setIsRegion(Integer isRegion) {
        this.isRegion = isRegion;
    }

    public boolean isDisplayAllEntity() {
        return displayAllEntity;
    }

    public boolean isHasCheckbox() {
        return hasCheckbox;
    }

    public boolean isHasGoogle() {
        return hasGoogle;
    }

    public boolean isHasRecyle() {
        return hasRecyle;
    }

    public boolean isHasSubnet() {
        return hasSubnet;
    }

    public boolean isHasSubnetFolder() {
        return hasSubnetFolder;
    }

    public boolean isHasTopoFolder() {
        return hasTopoFolder;
    }

    public void setDisplayAllEntity(boolean displayAllEntity) {
        this.displayAllEntity = displayAllEntity;
    }

    public void setDisplayType(int displayType) {
        this.displayType = displayType;
    }

    public void setFolderId(long folderId) {
        this.folderId = folderId;
    }

    public void setFolderSize(String folderSize) {
        this.folderSize = folderSize;
    }

    public void setHasCheckbox(boolean hasCheckbox) {
        this.hasCheckbox = hasCheckbox;
    }

    public void setHasGoogle(boolean hasGoogle) {
        this.hasGoogle = hasGoogle;
    }

    public void setHasRecyle(boolean hasRecyle) {
        this.hasRecyle = hasRecyle;
    }

    public void setHasSubnet(boolean hasSubnet) {
        this.hasSubnet = hasSubnet;
    }

    public void setHasSubnetFolder(boolean hasSubnetFolder) {
        this.hasSubnetFolder = hasSubnetFolder;
    }

    public void setHasTopoFolder(boolean hasTopoFolder) {
        this.hasTopoFolder = hasTopoFolder;
    }

    public void setLabelColors(List<String> labelColors) {
        this.labelColors = labelColors;
    }

    public void setLabelType(String labelType) {
        this.labelType = labelType;
    }

    public void setLabelValues(List<Long> labelValues) {
        this.labelValues = labelValues;
    }

    public void setMapNode(MapNode mapNode) {
        this.mapNode = mapNode;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNodeId(long nodeId) {
        this.nodeId = nodeId;
    }

    public void setOldName(String oldName) {
        this.oldName = oldName;
    }

    public void setSuperiorId(long superiorId) {
        this.superiorId = superiorId;
    }

    public void setTopoFolder(TopoFolder topoFolder) {
        this.topoFolder = topoFolder;
    }

    public void setTopologyService(TopologyService topologyService) {
        this.topologyService = topologyService;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Long getTopoFolderId() {
        return topoFolderId;
    }

    public void setTopoFolderId(Long topoFolderId) {
        this.topoFolderId = topoFolderId;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public String getFolderIds() {
        return folderIds;
    }

    public void setFolderIds(String folderIds) {
        this.folderIds = folderIds;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

}
