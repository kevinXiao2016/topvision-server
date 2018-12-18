package com.topvision.ems.fault.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.topvision.ems.fault.domain.AlertFilter;
import com.topvision.ems.fault.domain.AlertType;
import com.topvision.ems.fault.domain.Level;
import com.topvision.ems.fault.service.AlertFilterService;
import com.topvision.ems.fault.service.AlertService;
import com.topvision.framework.domain.CheckTreeNode;
import com.topvision.framework.domain.TreeNode;
import com.topvision.framework.web.struts2.BaseAction;
import com.topvision.platform.ResourceManager;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.processors.DefaultDefaultValueProcessor;

@Controller("alertFilterAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class AlertFilterAction extends BaseAction {
    private static final long serialVersionUID = 3459668697445768007L;
    @Autowired
    private AlertService alertService;
    @Autowired
    private AlertFilterService alertFilterService;
    private List<AlertFilter> alertFilters = null;
    private List<AlertType> alertTypes = null;
    private List<AlertType> combinateAlertTypes = new ArrayList<AlertType>();
    private AlertFilter alertFilter;
    private JSONObject alertFilterJson;
    private String action = "add";
    private List<Long> filterIds;
    private Long filterId;
    private List<Integer> typeIds;
    private String typeIdsStr;
    private byte level = Level.INFO_LEVEL;
    private boolean filterActived;
    private int filterType;

    public String loadALertFilter() {
        Map<String, Object> queryMap = new HashMap<String, Object>();
        queryMap.put("start", start);
        queryMap.put("limit", limit);
        List<AlertFilter> filters = alertFilterService.getAlertFilter(queryMap);
        Integer count = alertFilterService.getAlertFilterCount(queryMap);
        JSONObject json = new JSONObject();
        json.put("data", filters);
        json.put("rowConut", count);
        writeDataToAjax(json);
        return NONE;
    }

    public String showAddAlertFilter() {
        return SUCCESS;
    }

    public String showEditAlertFilter() {
        action = "edit";
        alertFilter = alertFilterService.getAlertFilter(filterId);

        JsonConfig jsonConfig = new JsonConfig();

        jsonConfig.registerDefaultValueProcessor(Integer.class, new DefaultDefaultValueProcessor() {
            public Object getDefaultValue(Class type) {
                return null;
            }
        });
        alertFilterJson = JSONObject.fromObject(alertFilter,jsonConfig);
        return SUCCESS;
    }

    public String addAlertFilter() throws Exception {
        JSONObject json = new JSONObject();
        alertFilterService.addAlertFilter(alertFilter);
        json.put("filterId", alertFilter.getFilterId());
        json.put("ip", alertFilter.getIp());
        json.put("typeName", alertFilter.getTypeName());
        writeDataToAjax(json);
        return NONE;
    }

    public String modifyAlertFilter() {
        alertFilterService.updateAlertFilter(alertFilter);
        return NONE;
    }

    public String deleteAlertFilter() {
        List<Long> filterIds = new ArrayList<Long>();
        filterIds.add(filterId);
        alertFilterService.removeAlertFilter(filterIds);
        return NONE;
    }

    public String loadAlertFilter() throws Exception {
        JSONArray array = new JSONArray();
        JSONObject json = new JSONObject();
        List<AlertFilter> filters = alertFilterService.getAlertFilterByType(filterType);
        JSONObject temp = null;
        AlertFilter filter = null;
        int size = filters.size();
        for (int i = 0; i < size; i++) {
            filter = filters.get(i);
            temp = new JSONObject();
            temp.put("filterId", filter.getFilterId());
            temp.put("ip", filter.getIp());
            temp.put("typeId", filter.getTypeId());
            temp.put("typeName", getString(filter.getTypeName(), "fault"));
            temp.put("note", filter.getNote());
            array.add(temp);
        }
        json.put("data", array);
        writeDataToAjax(json);
        return NONE;
    }

    public String loadAlertTypeFilter() throws Exception {
        Set<Integer> set = alertFilterService.getTypeIdsInFilter(AlertFilter.TYPE_FILTER);
        ResourceManager resourceManager = ResourceManager.getResourceManager("com.topvision.ems.fault.resources");
        List<AlertType> list = alertService.getAllAlertTypes();
        List<TreeNode> treeNodes = new ArrayList<TreeNode>();
        HashMap<String, CheckTreeNode> map = new HashMap<String, CheckTreeNode>(list == null ? 0 : list.size());
        // 遍历，将告警主分类加入jsonAlertType中
        for (AlertType alertType : list) {
            if (alertType.getCategory() != 0) {
                continue;
            }
            CheckTreeNode treeNode = new CheckTreeNode();
            treeNode.setText(resourceManager.getString(alertType.getDisplayName()));
            treeNode.setId(Long.parseLong(alertType.getTypeId().toString()));
            treeNode.setParentId(0l);
            treeNode.setIconCls("folder");
            treeNode.setNeedATag(true);
            treeNode.setNeedCbx(false);
            treeNode.setChildren(new ArrayList<TreeNode>());
            map.put(treeNode.getId().toString(), treeNode);
            treeNodes.add(treeNode);
        }
        // 再次遍历，将各叶子告警插入相应分类之下
        for (AlertType alertType : list) {
            if (alertType.getCategory() == 0) {
                continue;
            }
            CheckTreeNode treeNode = new CheckTreeNode();
            treeNode.setText(resourceManager.getString(alertType.getDisplayName()));
            treeNode.setId(Long.parseLong(alertType.getTypeId().toString()));
            treeNode.setParentId(Long.parseLong(alertType.getParentId().toString()));
            treeNode.setIconCls("level" + alertType.getLevelId() + "Icon");
            treeNode.setNeedATag(true);
            treeNode.setChecked(set.contains(alertType.getTypeId()));
            treeNode.setChildren(new ArrayList<TreeNode>());
            // map的存在意义是始终保留指向相应节点的引用，这样可以方便的修改其children，快速构建正确的树结构
            map.put(String.valueOf(alertType.getTypeId()), treeNode);
            TreeNode parentNode = map.get(String.valueOf(alertType.getParentId()));
            if (parentNode == null) {
                treeNodes.add(treeNode);
            } else {
                parentNode.getChildren().add(treeNode);
                parentNode.setIconCls("folder");
            }
        }
        // 最后一次遍历，来确定哪些节点是叶子节点，需要在页面加上checkbox
        for (String typeIdStr : map.keySet()) {
            CheckTreeNode treeNode = map.get(typeIdStr);
            if (treeNode.getChildren().size() == 0) {
                treeNode.setNeedCbx(true);
            }
        }
        net.sf.json.JSONArray jsonAlertType = new net.sf.json.JSONArray();
        jsonAlertType.addAll(treeNodes);
        /*
         * ResourceManager resourceManager =
         * ResourceManager.getResourceManager("com.topvision.ems.fault.resources"); List<AlertType>
         * list = alertService.getAllAlertTypes(); JSONArray jsonAlertType = new JSONArray();
         * HashMap<String, JSONObject> map = new HashMap<String, JSONObject>(list == null ? 0 :
         * list.size()); AlertType type = null; JSONObject json = null; for (int i = 0; list != null
         * && i < list.size(); i++) { type = list.get(i); if (type.getCategory() != 0) { continue; }
         * json = new JSONObject(); json.put("text",
         * resourceManager.getString(type.getDisplayName())); json.put("expanded", false);
         * json.put("id", String.valueOf(type.getTypeId())); json.put("children", new JSONArray());
         * json.put("superiorId", "0"); json.put("iconCls", "alertFolderIcon"); json.put("typeId",
         * type.getTypeId()); json.put("checked", set.contains(type.getTypeId()));
         * map.put(String.valueOf(type.getTypeId()), json); jsonAlertType.put(json); } JSONObject
         * parent = null; for (int i = 0; list != null && i < list.size(); i++) { type =
         * list.get(i); if (type.getCategory() == 0) { continue; } json = new JSONObject();
         * json.put("text", resourceManager.getString(type.getDisplayName())); json.put("expanded",
         * true); json.put("id", String.valueOf(type.getTypeId())); json.put("children", new
         * JSONArray()); if (type.getLevelId() == 0) { json.put("superiorId", "0"); } else {
         * json.put("superiorId", type.getParentId()); } json.put("iconCls", "level" +
         * type.getLevelId() + "Icon"); json.put("typeId", type.getTypeId()); json.put("checked",
         * set.contains(type.getTypeId())); map.put(String.valueOf(type.getTypeId()), json); parent
         * = map.get(String.valueOf(type.getParentId())); if (parent == null) {
         * jsonAlertType.put(json); } else { parent.getJSONArray("children").put(json);
         * parent.put("iconCls", "alertFolderIcon"); parent.put("expanded", false); } }
         */
        // JSONArray array = alertService.loadJSONAlertType();
        writeDataToAjax(jsonAlertType);
        return NONE;
        /*
         * JSONArray jsonAlertType = new JSONArray(); Set<Integer> set =
         * alertFilterService.getTypeIdsInFilter(AlertFilter.TYPE_FILTER); List<AlertType> list =
         * alertService.getAllAlertTypes(); HashMap<String, JSONObject> map = new HashMap<String,
         * JSONObject>(list == null ? 0 : list.size()); JSONObject json = null; JSONObject parent =
         * null; AlertType type = null; for (int i = 0; list != null && i < list.size(); i++) { type
         * = list.get(i); json = new JSONObject(); json.put("text", getString(type.getDisplayName(),
         * "fault")); json.put("expanded", true); json.put("id", String.valueOf(type.getTypeId()));
         * json.put("children", new JSONArray()); json.put("superiorId", type.getParentId()); if
         * (Integer.parseInt(type.getParentId()) == 0 || Integer.parseInt(type.getParentId()) ==
         * -10000) { json.put("icon", "../images/fault/alertFolder.gif"); } else {
         * json.put("iconCls", "level" + type.getLevelId() + "Icon"); if (type.getLevelId() == 1) {
         * json.put("icon", "../images/fault/level1.gif"); } else if (type.getLevelId() == 2) {
         * json.put("icon", "../images/fault/level2.gif"); } else if (type.getLevelId() == 3) {
         * json.put("icon", "../images/fault/level3.gif"); } else if (type.getLevelId() == 4) {
         * json.put("icon", "../images/fault/level4.gif"); } else if (type.getLevelId() == 5) {
         * json.put("icon", "../images/fault/level5.gif"); } else if (type.getLevelId() == 6) {
         * json.put("icon", "../images/fault/level6.gif"); } else { json.put("icon",
         * "../images/fault/alertLeaf.gif"); } json.put("typeId", type.getTypeId());
         * json.put("checked", set.contains(type.getTypeId())); }
         * map.put(String.valueOf(type.getTypeId()), json); parent =
         * map.get(String.valueOf(type.getParentId())); if (parent == null) {
         * jsonAlertType.put(json); } else { parent.getJSONArray("children").put(json); } }
         * write(jsonAlertType); return NONE;
         */
    }

    public String saveAlertTypeFilter() {
        List<AlertFilter> filters = new ArrayList<AlertFilter>();
        if (typeIds != null) {
            AlertFilter filter = null;
            for (int i = 0; i < typeIds.size(); i++) {
                filter = new AlertFilter();
                // filter.setType(AlertFilter.TYPE_FILTER);
                filter.setTypeId(typeIds.get(i));
                filters.add(filter);
            }
        }
        alertFilterService.txSaveAlertTypeFilter(filters);
        return NONE;
    }

    public String saveFilterActived() {
        alertFilterService.saveFilterActived(filterActived);
        return NONE;
    }

    public String saveMinLevel() {
        alertFilterService.saveMinLevel(level);
        return NONE;
    }

    public String showAlertFilter() {
        // level = alertFilterService.getMinLevel();
        // filterActived = alertFilterService.isFilterActived();
        // alertTypes = alertService.getAllAlertTypes();
        // for (AlertType alertType : alertTypes) {
        // alertType.setDisplayName(getString(alertType.getDisplayName(), "fault"));
        // if (alertType.getCategory() != -10000) {
        // combinateAlertTypes.add(alertType);
        // }
        // }
        level = alertFilterService.getMinLevel();
        filterActived = alertFilterService.isFilterActived();
        return SUCCESS;
    }

    /**
     * 国际化
     * 
     * @param key
     *            key
     * @return String
     */
    protected String getString(String key, String module) {
        module = String.format("com.topvision.ems.%s.resources", module);
        try {
            ResourceManager resourceManager = ResourceManager.getResourceManager(module);
            return resourceManager.getNotNullString(key);
        } catch (Exception e) {
            return key;
        }
    }

    public AlertFilter getAlertFilter() {
        return alertFilter;
    }

    public List<AlertFilter> getAlertFilters() {
        return alertFilters;
    }

    public List<AlertType> getCombinateAlertTypes() {
        return combinateAlertTypes;
    }

    public void setCombinateAlertTypes(List<AlertType> combinateAlertTypes) {
        this.combinateAlertTypes = combinateAlertTypes;
    }

    public List<AlertType> getAlertTypes() {
        return alertTypes;
    }

    public List<Long> getFilterIds() {
        return filterIds;
    }

    public int getFilterType() {
        return filterType;
    }

    public byte getLevel() {
        return level;
    }

    public List<Integer> getTypeIds() {
        return typeIds;
    }

    public boolean isFilterActived() {
        return filterActived;
    }

    public void setAlertFilter(AlertFilter alertFilter) {
        this.alertFilter = alertFilter;
    }

    public void setAlertFilters(List<AlertFilter> alertFilters) {
        this.alertFilters = alertFilters;
    }

    public void setAlertFilterService(AlertFilterService alertFilterService) {
        this.alertFilterService = alertFilterService;
    }

    public void setAlertService(AlertService alertService) {
        this.alertService = alertService;
    }

    public void setAlertTypes(List<AlertType> alertTypes) {
        this.alertTypes = alertTypes;
    }

    public void setFilterActived(boolean filterActived) {
        this.filterActived = filterActived;
    }

    public void setFilterIds(List<Long> filterIds) {
        this.filterIds = filterIds;
    }

    public void setFilterType(int filterType) {
        this.filterType = filterType;
    }

    public void setLevel(byte level) {
        this.level = level;
    }

    public void setTypeIds(List<Integer> typeIds) {
        this.typeIds = typeIds;
    }

    public String getTypeIdsStr() {
        return typeIdsStr;
    }

    public void setTypeIdsStr(String typeIdsStr) {
        this.typeIdsStr = typeIdsStr;
    }

    public Long getFilterId() {
        return filterId;
    }

    public void setFilterId(Long filterId) {
        this.filterId = filterId;
    }

    public JSONObject getAlertFilterJson() {
        return alertFilterJson;
    }

    public void setAlertFilterJson(JSONObject alertFilterJson) {
        this.alertFilterJson = alertFilterJson;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }
}
