package com.topvision.ems.network.action;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.fault.domain.Level;
import com.topvision.ems.network.domain.EntitySnap;
import com.topvision.ems.network.domain.Link;
import com.topvision.ems.network.domain.MapNode;
import com.topvision.ems.network.domain.TopoFolder;
import com.topvision.ems.network.service.EntityService;
import com.topvision.ems.network.service.NetworkConstants;
import com.topvision.ems.network.service.NetworkSnapManager;
import com.topvision.ems.network.service.TopologyService;
import com.topvision.ems.template.service.EntityTypeService;
import com.topvision.framework.common.DateUtils;
import com.topvision.framework.common.MacUtils;
import com.topvision.framework.web.struts2.BaseAction;
import com.topvision.platform.ResourceManager;
import com.topvision.platform.SystemConstants;
import com.topvision.platform.domain.UserContext;
import com.topvision.platform.domain.UserPreferences;
import com.topvision.platform.domain.UserPreferencesMap;
import com.topvision.platform.service.UserPreferencesService;
import com.topvision.platform.service.UserService;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Controller("topologyAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class TopologyAction extends BaseAction {
    private static final long serialVersionUID = 3935462735105834032L;
    private final String graphType = SystemConstants.getInstance().getStringParam("graphType", "map");
    @Autowired
    private UserService userService;
    @Autowired
    private EntityService entityService;
    @Autowired
    private EntityTypeService entityTypeService;
    @Autowired
    private TopologyService topologyService;
    @Autowired
    private UserPreferencesService userPreferencesService;

    private TopoFolder topoFolder;
    private long folderId;
    private long superiorId;
    private long entityId;
    private boolean refreshed;
    // icon表示图标, detail表示详细信息, map表示拓扑图
    private String viewType;
    private String sortType;
    private int arrangeType = 0;
    private int mapWidth = 1024;
    private int mapHeight = 768;
    private long destFolderId;
    private List<Long> entityIds;
    private List<Long> folderIds;
    private List<Long> nodeIds;
    private List<Long> edgeIds;
    private List<Long> noEntityIds;
    private List<Integer> x;
    private List<Integer> y;
    private List<Integer> nodex;
    private List<Integer> nodey;
    private List<Integer> folderx;
    private List<Integer> foldery;
    private long virtualNetId;
    private String topoToolView;
    private String zoomValue;
    private Boolean displaySubnet;

    public String arrangeEntityByFolderId() throws Exception {
        JSONArray json = new JSONArray();
        List<Entity> entities = topologyService.arrangeTopoMap(folderId, arrangeType, mapWidth, mapHeight);
        int size = entities == null ? 0 : entities.size();
        Entity entity = null;
        JSONObject temp = null;
        for (int i = 0; i < size; i++) {
            entity = entities.get(i);
            temp = new JSONObject();
            temp.put("nodeId", entity.getEntityId());
            temp.put("x", entity.getX());
            temp.put("y", entity.getY());
            temp.put("fixed", entity.getFixed());

            json.add(temp);
        }

        writeDataToAjax(json);

        return NONE;
    }

    public String copyEntityById() throws Exception {
        JSONObject json = new JSONObject();
        List<Entity> entities = topologyService.txCopyEntity(null, folderId, destFolderId, entityIds);
        JSONArray array = new JSONArray();
        int size = entities == null ? 0 : entities.size();
        // add by fanzidong,需要在展示前格式化MAC地址
        UserContext uc = (UserContext) super.getSession().get(UserContext.KEY);
        String macRule = uc.getMacDisplayStyle();
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                JSONObject temp = new JSONObject();
                Entity entity = entities.get(i);
                temp.put("id", "cell" + entity.getEntityId());
                temp.put("nodeId", entity.getEntityId());
                temp.put("text", entity.getDisplayName());
                temp.put("x", entity.getX());
                temp.put("y", entity.getY());
                temp.put("icon", entity.getIcon());
                temp.put("url", entity.getUrl());
                temp.put("fixed", entity.getFixed());
                temp.put("groupId", entity.getGroupId());
                temp.put("objType", NetworkConstants.TYPE_ENTITY);
                temp.put("userObjId", entity.getEntityId());
                temp.put("status", entity.isStatus());
                temp.put("parentId", entity.getParentId());
                temp.put("name", entity.getShowName());
                temp.put("sysName", entity.getShowSysName());
                temp.put("ip", entity.getIp());
                temp.put("mac", MacUtils.convertMacToDisplayFormat(entity.getMac(), macRule));
                temp.put("type", entityTypeService.getEntityNetworkGroupIdByEntityTypeId(entity.getTypeId()));
                temp.put("module", entity.getModule());
                temp.put("modulePath", entity.getModulePath());
                temp.put("snmpSupport", entity.isSnmpSupport());
                // temp.put("agentSupport", entity.isAgentSupport());
                temp.put("virtualNetworkStatus", entity.getVirtualNetworkStatus());
                temp.put("alert", 0);
                array.add(temp);
            }
        }
        json.put("entity", array);
        JSONArray array1 = new JSONArray();
        if (size > 0) {
            List<Link> links = topologyService.getEdgeByFolderId(destFolderId);
            size = links == null ? 0 : links.size();
            for (int i = 0; i < size; i++) {
                JSONObject temp = new JSONObject();
                Link link = links.get(i);
                temp.put("id", "edge" + link.getLinkId());
                temp.put("nodeId", link.getLinkId());
                temp.put("objType", NetworkConstants.TYPE_LINK);
                temp.put("userObjId", link.getLinkId());
                temp.put("srcEntityId", link.getSrcEntityId());
                temp.put("destEntityId", link.getDestEntityId());
                array1.add(temp);
            }
        }
        json.put("link", array1);

        writeDataToAjax(json);

        return NONE;
    }

    public String cutEntityById() throws Exception {
        JSONObject json = new JSONObject();
        List<Entity> entities = topologyService.txCutEntity(null, folderId, destFolderId, entityIds);
        JSONArray array = new JSONArray();
        int size = entities == null ? 0 : entities.size();
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                JSONObject temp = new JSONObject();
                Entity entity = entities.get(i);
                temp.put("id", "cell" + entity.getEntityId());
                temp.put("nodeId", entity.getEntityId());
                temp.put("text", entity.getDisplayName());
                temp.put("x", entity.getX());
                temp.put("y", entity.getY());
                temp.put("icon", entity.getIcon());
                temp.put("url", entity.getUrl());
                temp.put("fixed", entity.getFixed());
                temp.put("groupId", entity.getGroupId());
                temp.put("objType", NetworkConstants.TYPE_ENTITY);
                temp.put("userObjId", entity.getEntityId());
                temp.put("status", entity.isStatus());
                temp.put("parentId", entity.getParentId());
                temp.put("name", entity.getShowName());
                temp.put("sysName", entity.getShowSysName());
                temp.put("ip", entity.getIp());
                temp.put("mac", entity.getMac());
                temp.put("type", entityTypeService.getEntityNetworkGroupIdByEntityTypeId(entity.getTypeId()));
                temp.put("module", entity.getModule());
                temp.put("modulePath", entity.getModulePath());
                temp.put("snmpSupport", entity.isSnmpSupport());
                // temp.put("agentSupport", entity.isAgentSupport());
                temp.put("virtualNetworkStatus", entity.getVirtualNetworkStatus());
                temp.put("alert", 0);
                array.add(temp);
            }
        }
        json.put("entity", array);
        JSONArray array1 = new JSONArray();
        if (size > 0) {
            List<Link> links = topologyService.getEdgeByFolderId(destFolderId);
            size = links == null ? 0 : links.size();
            for (int i = 0; i < size; i++) {
                JSONObject temp = new JSONObject();
                Link link = links.get(i);
                temp.put("id", "edge" + link.getLinkId());
                temp.put("linkId", link.getLinkId());
                temp.put("srcEntityId", link.getSrcEntityId());
                temp.put("destEntityId", link.getDestEntityId());
                temp.put("objType", NetworkConstants.TYPE_LINK);
                temp.put("userObjId", link.getLinkId());
                array1.add(temp);
            }
        }
        json.put("link", array1);

        writeDataToAjax(json);

        return NONE;
    }

    /**
     * 删除拓扑文件夹下的设备, 子文件夹和边.
     * 
     * @return
     * @throws Exception
     */
    public String deleteCellByIds() throws Exception {
        // json：传递给前台的数据
        // json.successIds: 删除成功的id
        // json.failedIds：删除失败的id
        JSONObject json = new JSONObject();
        List<Long> successEntityIds = new ArrayList<Long>();
        List<Long> successFolderIds = new ArrayList<Long>();
        List<Long> successEdgeIds = new ArrayList<Long>();
        List<Long> failedEntityIds = new ArrayList<Long>();
        List<Long> failedFolderIds = new ArrayList<Long>();

        // 删除逻辑：
        // 1、先挑出不能删除的地域/设备，准备反馈给前台
        // 2、对于选中的地域：能够被删除的地域都是空地域，直接删除mapnode、topofolder表中的数据即可，应该也要删除掉v_topo_x
        // 3、对于选中的设备，需要删除mapnode、entityfolderrela表中的相关数据，并且删除link表中的相关节点，这些节点也需要反馈给前台
        // 4、对于选中的连线，直接删除link表中的相关数据即可

        // 边目前直接进行删除
        if (edgeIds != null && edgeIds.size() > 0) {
            topologyService.removeEntityAndLink(folderId, null, edgeIds);
            successEdgeIds.addAll(edgeIds);
        }
        // 节点需要进行判断是否可以删除，地域有设备或者子地域无法删除，设备只有一个地域也无法被删除
        if (nodeIds != null && nodeIds.size() > 0) {
            List<Long> tobeDeletedEntityIds = new ArrayList<Long>();
            for (Long nodeId : nodeIds) {
                if (nodeId < MapNode.MAPNODE_INC) {
                    // 表明当前node是地域
                    TopoFolder folder = new TopoFolder();
                    folder.setFolderId(nodeId);
                    folder.setSuperiorId(folderId);
                    boolean hasChild = topologyService.hasChild(folder);
                    if (hasChild) {
                        // 有地域则无法删除
                        failedFolderIds.add(nodeId);
                    } else {
                        successFolderIds.add(nodeId);
                        // 删除地域信息（已经删除相关节点信息）
                        topologyService.txDeleteTopoFolder(null, folder);
                    }
                } else {
                    // TODO 当前是设备，需要判断当前设备是否可以被删除
                    List<Long> folderIds = new ArrayList<Long>();
                    folderIds.add(folderId);
                    Boolean canRemove = entityService.canRemoveEntityFromFolder(nodeId, folderIds);
                    if (canRemove) {
                        tobeDeletedEntityIds.add(nodeId);
                        successEntityIds.add(nodeId);
                    } else {
                        failedEntityIds.add(nodeId);
                    }
                }
            }
            // 删除图表中的设备节点数据（地域节点已经被删除）
            topologyService.deleteMapNode(folderId, tobeDeletedEntityIds);
            // 删除设备与地域之间的关系，及设备相关的连线
            if (tobeDeletedEntityIds.size() > 0) {
                List<Long> relaLinkIds = topologyService.removeEntityAndLink(folderId, tobeDeletedEntityIds, edgeIds);
                successEdgeIds.addAll(relaLinkIds);
            }
        }
        json.put("successEntityIds", successEntityIds);
        json.put("successFolderIds", successFolderIds);
        json.put("successEdgeIds", successEdgeIds);
        json.put("failedEntityIds", failedEntityIds);
        json.put("failedFolderIds", failedFolderIds);
        // 根据失败设备Id获取失败entity的名称
        List<String> failedEntityNames = entityService.getEntityNamesByIds(failedEntityIds);
        // 根据失败地域Id获取失败entity的名称
        List<String> failedFolderNames = topologyService.getFolderNamesByIds(failedFolderIds);
        json.put("failedEntityNames", failedEntityNames);
        json.put("failedFolderNames", failedFolderNames);

        writeDataToAjax(json);

        return NONE;
    }

    public String getEntityByDetail() throws Exception {
        UserContext uc = (UserContext) getSession().get(UserContext.KEY);
        // add by fanzidong,需要在展示前格式化MAC地址
        String macRule = uc.getMacDisplayStyle();
        JSONObject json = new JSONObject();
        List<Entity> entities = entityService.getEntityByFolderId(folderId, null);
        int size = entities == null ? 0 : entities.size();
        JSONArray array = new JSONArray();
        JSONObject temp = null;
        Entity entity = null;
        EntitySnap snap = null;

        json.put("rowCount", 0);
        for (int i = 0; i < size; i++) {
            temp = new JSONObject();
            entity = entities.get(i);
            if (!displaySubnet && entity.getParentId() !=null) {
            	continue;
            }
            temp.put("entityId", entity.getEntityId());
            temp.put("name", entity.getShowName());
            temp.put("sysName", entity.getShowSysName());
            temp.put("ip", entity.getIp());
            temp.put("uplinkDevice", entity.getUplinkDevice());
            temp.put("status", entity.isStatus());
            temp.put("module", entity.getModule());
            temp.put("typeName", entity.getTypeName());
            temp.put("snmpSupport", entity.isSnmpSupport());
            temp.put("agentInstalled", entity.getAgentInstalled());
            temp.put("url", entity.getUrl());
            temp.put("type", entityTypeService.getEntityNetworkGroupIdByEntityTypeId(entity.getTypeId()));
            temp.put("typeId", entity.getTypeId());
            String formatedMac = MacUtils.convertMacToDisplayFormat(entity.getMac(), macRule);
            temp.put("mac", formatedMac);
            temp.put("parentId", entity.getParentId());// modify by loyal
                                                       // 拓扑图中添加对cc刷新设备支持，parentId对于cc来说是cmcId
            // 由于内存数据与数据库不一致，按照haojie要求修改为从数据库中读取SNAP信息
            snap = entityService.getEntitySnapById(entity.getEntityId());
            if (snap != null) {
                // 保证SNAP的在线状态从EntitySnap表获取
                temp.put("state", snap.isState());
                // SNAP的告警信息无法出EntitySnap表获取，只能通过内存中的SNAP获取告警信息
                EntitySnap alertSnap = NetworkSnapManager.getInstance().getEntitySnap(entity.getEntityId());
                if (alertSnap != null) {
                    temp.put("alert", alertSnap.getAlertLevel());
                } else {
                    temp.put("alert", Level.CLEAR_LEVEL);
                }
                // 保证SNAP的最近更新时间从EntitySnap表获取
                temp.put("snapTime",
                        snap.getSnapTime() == null ? ""
                                : DateUtils.getTimeDesInObscure(
                                        System.currentTimeMillis() - snap.getSnapTime().getTime(),
                                        uc.getUser().getLanguage()));
            } else {
                temp.put("state", true);
                temp.put("alert", Level.CLEAR_LEVEL);
                temp.put("snapTime", "");
            }
            array.add(temp);
        }
        json.put("data", array);

        writeDataToAjax(json);

        return NONE;
    }

    public String getEntityByIconView() throws Exception {
        JSONObject json = new JSONObject();
        int entityCount = 0;
        List<Entity> entities = entityService.getEntityByFolderId(folderId, sortType);
        int size = entities == null ? 0 : entities.size();
        JSONArray array = new JSONArray();
        entityCount = size;
        Entity entity = null;
        EntitySnap snap = null;
        JSONObject temp = null;
        for (int i = 0; i < size; i++) {
            temp = new JSONObject();
            entity = entities.get(i);
            temp.put("name", entity.getShowName());
            temp.put("sysName", entity.getShowSysName());
            temp.put("ip", entity.getIp());
            temp.put("entityId", entity.getEntityId());
            temp.put("icon", entity.getIcon());
            temp.put("type", entityTypeService.getEntityNetworkGroupIdByEntityTypeId(entity.getTypeId()));
            temp.put("snmpSupport", entity.isSnmpSupport());
            temp.put("agentInstalled", entity.getAgentInstalled());

            snap = NetworkSnapManager.getInstance().getEntitySnap(entity.getEntityId());
            if (snap != null && snap.getAlertLevel() == Level.CLEAR_LEVEL) {
                temp.put("alert", snap.isState() ? snap.getAlertLevel() : Level.OFFLINE);
            } else {
                temp.put("alert", Level.CLEAR_LEVEL);
            }
            array.add(temp);
        }
        json.put("entityCount", entityCount);
        json.put("entity", array);

        writeDataToAjax(json);

        return NONE;
    }

    /**
     * 载入某个拓扑文件下的设备, 结点, 和子文件夹.
     * 
     * @return
     * @throws Exception
     */
    public String loadVertexByFolderId() throws Exception {
        topologyService.refreshTopoFolder(folderId);
        net.sf.json.JSONObject json = topologyService.loadJSONCellByFolderId(folderId);
        // add by fanzidong,需要在展示前格式化MAC地址
        UserContext uc = (UserContext) super.getSession().get(UserContext.KEY);
        String macRule = uc.getMacDisplayStyle();
        if (json != null && json.size() != 0 && json.has("entity")) {
            net.sf.json.JSONArray entities = json.getJSONArray("entity");
            net.sf.json.JSONArray links = null;
            if (json.has("link")) {
                links = json.getJSONArray("link");
            }
            if (entities != null) {
                Iterator<net.sf.json.JSONObject> iterator = entities.iterator();
                while (iterator.hasNext()) {
                    net.sf.json.JSONObject entity = iterator.next();
                    if (!displaySubnet && entity.has("parentId")) {
                        /**删除设备*/
                        iterator.remove();
                        /**删除连线*/
                        if (links != null) {
                            Iterator<net.sf.json.JSONObject> linkIterator = links.iterator();
                            long $entityId = entity.getLong("nodeId");
                            while (linkIterator.hasNext()) {
                                net.sf.json.JSONObject link = linkIterator.next();
                                if (link.getLong("destEntityId") == $entityId || link.getLong("srcEntityId") == $entityId) {
                                    linkIterator.remove();
                                    //break;
                                }
                            }
                        }
                        continue;
                    }
                    if (entity.has("mac")) {
                        String formatedMac = MacUtils.convertMacToDisplayFormat(entity.getString("mac"), macRule);
                        entity.put("mac", formatedMac);
                    }
                }
            }
        }
        json.write(response.getWriter());
        return NONE;
    }

    /**
     * 保存拓扑结构的位置信息
     * 
     * @return
     */
    public String saveCoordinateByIds() {
        List<Entity> entities = new ArrayList<Entity>();
        List<MapNode> nodes = new ArrayList<MapNode>();
        long id = 0;
        MapNode node = null;
        Entity entity = null;
        int size = nodeIds.size();
        for (int i = 0; i < size; i++) {
            id = nodeIds.get(i);
            if (id < Entity.ENTITY_INC) {
                node = new MapNode();
                node.setFolderId(folderId);
                node.setNodeId(id);
                node.setX(x.get(i));
                node.setY(y.get(i));
                nodes.add(node);
            } else {
                entity = new Entity();
                entity.setEntityId(id);
                entity.setFolderId(folderId);
                entity.setX(x.get(i));
                entity.setY(y.get(i));
                entities.add(entity);
            }
        }
        topologyService.txSaveCoordinate(folderId, entities, nodes);

        return NONE;
    }

    public String getTopoMapByFolderId() {
        UserContext uc = (UserContext) getSession().get(UserContext.KEY);
        UserPreferencesMap<String, String> preferencesMap = uc.getUserPreferencesMap();
        // 是否显示名称
        topoFolder = topologyService.getTopoFolder(folderId);
        if (topoFolder == null) {
            return NONE;
        }
        topoFolder.setName(getResourceManager().getString(topoFolder.getName()));
        setTopoFolder(topoFolder);
        String temp = preferencesMap.getString(NetworkConstants.TOPOLOGY_VIEW, "map");
        // 查看方式
        if (viewType == null) {
            viewType = temp;
        } else {
            // save view type
            UserPreferences userPreferences = new UserPreferences();
            userPreferences.setUserId(uc.getUserId());
            userPreferences.setModule("network");
            userPreferences.setName("topology.view");
            userPreferences.setValue(viewType);
            if (temp == null) {
                userService.insertPreferences(userPreferences);
            } else {
                userService.updatePreferences(userPreferences);
            }
            uc.getUserPreferencesMap().put(NetworkConstants.TOPOLOGY_VIEW, viewType);
        }

        if ("map".equals(viewType)) {
            return graphType;
        } else if ("icon".equals(viewType)) {
            // 当使用icon查看时, 使用的排序排列方式
            sortType = preferencesMap.getString(NetworkConstants.TOPOLOGY_SORT, "name");
        }

        return viewType;
    }

    public String showNewTopoDemo() {
        UserContext uc = (UserContext) getSession().get(UserContext.KEY);
        UserPreferencesMap<String, String> preferencesMap = uc.getUserPreferencesMap();
        // 是否显示名称
        topoFolder = topologyService.getTopoFolder(folderId);
        if (topoFolder == null) {
            return NONE;
        }
        topoFolder.setName(getResourceManager().getString(topoFolder.getName()));
        setTopoFolder(topoFolder);
        String temp = preferencesMap.getString(NetworkConstants.TOPOLOGY_VIEW, "map");
        // 获取topo图工具栏文字显示视图
        topoToolView = getToolView().getProperty("toolTextView");
        zoomValue = getToolView().getProperty("zoomValue");
        displaySubnet = Boolean.valueOf(getToolView().getProperty("displaySubnet"));
        // 查看方式
        if (viewType == null) {
            viewType = temp;
        } else {
            // save view type
            UserPreferences userPreferences = new UserPreferences();
            userPreferences.setUserId(uc.getUserId());
            userPreferences.setModule("network");
            userPreferences.setName("topology.view");
            userPreferences.setValue(viewType);
            if (temp == null) {
                userService.insertPreferences(userPreferences);
            } else {
                userService.updatePreferences(userPreferences);
            }
            uc.getUserPreferencesMap().put(NetworkConstants.TOPOLOGY_VIEW, viewType);
        }

        if ("map".equals(viewType)) {
            return graphType;
        } else if ("icon".equals(viewType)) {
            // 当使用icon查看时, 使用的排序排列方式
            sortType = preferencesMap.getString(NetworkConstants.TOPOLOGY_SORT, "name");
        }
        return viewType;
    }

    /**
     * 获取topo图工具栏文字显示视图
     * 
     * @return
     */
    private Properties getToolView() {
        UserContext uc = (UserContext) ServletActionContext.getRequest().getSession().getAttribute("UserContext");
        UserPreferences up = new UserPreferences();
        up.setModule("topoToolView");
        up.setUserId(uc.getUserId());
        Properties topoView = userPreferencesService.getModulePreferences(up);
        return topoView;
    }

    /**
     * 保存工具栏是否显示文字
     * 
     * @return
     */
    public String saveToolView() {
        UserContext uc = (UserContext) ServletActionContext.getRequest().getSession().getAttribute("UserContext");
        Properties topoView = new Properties();
        if (topoToolView != null && !"".equals(topoToolView)) {
            topoView.setProperty("toolTextView", topoToolView);
        }
        if (zoomValue != null && !"".equals(zoomValue)) {
            topoView.setProperty("zoomValue", zoomValue);
        }
        if (displaySubnet != null && !"".equals(displaySubnet)) {
            topoView.setProperty("displaySubnet", displaySubnet ? "true" : "false");
        }
        userPreferencesService.batchSaveModulePreferences("topoToolView", uc.getUserId(), topoView);
        return NONE;
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

    private ResourceManager getResourceManager() {
        return ResourceManager.getResourceManager("com.topvision.ems.resources.resources");
    }

    public long getDestFolderId() {
        return destFolderId;
    }

    public List<Long> getEdgeIds() {
        return edgeIds;
    }

    public long getEntityId() {
        return entityId;
    }

    public List<Long> getEntityIds() {
        return entityIds;
    }

    public long getFolderId() {
        return folderId;
    }

    public List<Long> getFolderIds() {
        return folderIds;
    }

    public List<Integer> getFolderx() {
        return folderx;
    }

    public List<Integer> getFoldery() {
        return foldery;
    }

    public int getMapHeight() {
        return mapHeight;
    }

    public int getMapWidth() {
        return mapWidth;
    }

    public List<Long> getNodeIds() {
        return nodeIds;
    }

    public List<Integer> getNodex() {
        return nodex;
    }

    public List<Integer> getNodey() {
        return nodey;
    }

    public String getSortType() {
        return sortType;
    }

    public long getSuperiorId() {
        return superiorId;
    }

    public TopoFolder getTopoFolder() {
        return topoFolder;
    }

    public String getViewType() {
        return viewType;
    }

    public List<Integer> getX() {
        return x;
    }

    public List<Integer> getY() {
        return y;
    }

    public boolean isRefreshed() {
        return refreshed;
    }

    public String showVitualDevice() {
        return SUCCESS;
    }

    public void setArrangeType(int arrangeType) {
        this.arrangeType = arrangeType;
    }

    public void setDestFolderId(long destFolderId) {
        this.destFolderId = destFolderId;
    }

    public void setEdgeIds(List<Long> edgeIds) {
        this.edgeIds = edgeIds;
    }

    public void setEntityId(long entityId) {
        this.entityId = entityId;
    }

    public void setEntityIds(List<Long> entityIds) {
        this.entityIds = entityIds;
    }

    public void setEntityService(EntityService entityService) {
        this.entityService = entityService;
    }

    public void setFolderId(long folderId) {
        this.folderId = folderId;
    }

    public void setFolderIds(List<Long> folderIds) {
        this.folderIds = folderIds;
    }

    public void setFolderx(List<Integer> folderx) {
        this.folderx = folderx;
    }

    public void setFoldery(List<Integer> foldery) {
        this.foldery = foldery;
    }

    public void setMapHeight(int mapHeight) {
        this.mapHeight = mapHeight;
    }

    public void setMapWidth(int mapWidth) {
        this.mapWidth = mapWidth;
    }

    public void setNodeIds(List<Long> nodeIds) {
        this.nodeIds = nodeIds;
    }

    public void setNodex(List<Integer> nodex) {
        this.nodex = nodex;
    }

    public void setNodey(List<Integer> nodey) {
        this.nodey = nodey;
    }

    public void setRefreshed(boolean refreshed) {
        this.refreshed = refreshed;
    }

    public void setSortType(String sortType) {
        this.sortType = sortType;
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

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public void setViewType(String viewType) {
        this.viewType = viewType;
    }

    public void setX(List<Integer> x) {
        this.x = x;
    }

    public void setY(List<Integer> y) {
        this.y = y;
    }

    public long getVirtualNetId() {
        return virtualNetId;
    }

    public void setVirtualNetId(long virtualNetId) {
        this.virtualNetId = virtualNetId;
    }

    public List<Long> getNoEntityIds() {
        return noEntityIds;
    }

    public void setNoEntityIds(List<Long> noEntityIds) {
        this.noEntityIds = noEntityIds;
    }

    public String getTopoToolView() {
        return topoToolView;
    }

    public void setTopoToolView(String topoToolView) {
        this.topoToolView = topoToolView;
    }

    public String getZoomValue() {
        return zoomValue;
    }

    public void setZoomValue(String zoomValue) {
        this.zoomValue = zoomValue;
    }

    public Boolean getDisplaySubnet() {
        return displaySubnet;
    }

    public void setDisplaySubnet(Boolean displaySubnet) {
        this.displaySubnet = displaySubnet;
    }

}
