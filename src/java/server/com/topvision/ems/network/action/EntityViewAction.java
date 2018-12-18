package com.topvision.ems.network.action;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.topvision.ems.devicesupport.version.service.DeviceVersionService;
import com.topvision.ems.devicesupport.version.util.DeviceFuctionSupport;
import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.facade.domain.Snap;
import com.topvision.ems.fault.domain.Level;
import com.topvision.ems.network.domain.EntitySnap;
import com.topvision.ems.network.service.EntityService;
import com.topvision.ems.network.service.NetworkConstants;
import com.topvision.ems.report.service.StatReportService;
import com.topvision.ems.template.service.EntityTypeService;
import com.topvision.framework.common.DateUtils;
import com.topvision.framework.common.MacUtils;
import com.topvision.framework.common.NumberUtils;
import com.topvision.framework.domain.PageData;
import com.topvision.framework.web.struts2.BaseAction;
import com.topvision.platform.ResourceManager;
import com.topvision.platform.SystemConstants;
import com.topvision.platform.domain.UserContext;
import com.topvision.platform.domain.UserPreferences;
import com.topvision.platform.service.UserService;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;

@Controller("entityViewAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class EntityViewAction extends BaseAction {
    private static final long serialVersionUID = 8335215492413867562L;

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    @SuppressWarnings("unused")
    private static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    // 刷新设备列表的时间间隔
    private long refreshInterval = SystemConstants.getInstance().getLongParam("Entity.view.refresh.interval",
            2 * 60 * 1000L);
    private String viewType;
    private String entityType;
    private String sortType;
    private boolean displayName = true;
    private long typeId;
    @Autowired
    private EntityService entityService;
    @Autowired
    private EntityTypeService entityTypeService;
    @Autowired
    private UserService userService;
    @Autowired
    private StatReportService statReportService;
    @Resource(name = "deviceVersionService")
    private DeviceVersionService deviceVersionService;

    private int start;
    private int limit;
    // 资源列表查询参数
    private Long entityId;
    private String queryText;
    private Long deviceType;
    private Integer onlineStatus;
    private String alias;// 设备别名
    private String ip;// 设备IP
    private String upperName;// 上联设备别名
    private List<Long> folderIds;// 所选地域
    private String entityIds;

    /**
     * 获取代理设备状态.
     * 
     * @return
     * @throws Exception
     */
    public String getAgentEntityState() throws Exception {
        JSONArray json = new JSONArray();
        PageData<EntitySnap> pd = entityService.getAgentEntityStateByPage(getPage());
        int size = pd == null ? 0 : pd.getData().size();
        List<EntitySnap> entities = pd.getData();
        for (int i = 0; i < size; i++) {
            EntitySnap snap = entities.get(i);
            JSONObject temp = new JSONObject();
            temp.put("entityId", snap.getEntityId());
            if (snap.getAlertLevel() == Level.CLEAR_LEVEL) {
                temp.put("alert", snap.isState() ? snap.getAlertLevel() : Level.OFFLINE);
            } else {
                temp.put("alert", snap.getAlertLevel());
            }
            json.add(temp);
        }
        writeDataToAjax(json);
        return NONE;
    }

    /**
     * 获取设备状态信息.
     * 
     * @return
     * @throws Exception
     */
    public String getEntityState() throws Exception {
        JSONArray json = new JSONArray();
        PageData<EntitySnap> pd = entityService.getEntityStateByPage(getPage());
        int size = pd == null ? 0 : pd.getData().size();
        List<EntitySnap> entities = pd.getData();
        for (int i = 0; i < size; i++) {
            EntitySnap snap = entities.get(i);
            JSONObject temp = new JSONObject();
            temp.put("entityId", snap.getEntityId());
            if (snap.getAlertLevel() == Level.CLEAR_LEVEL) {
                temp.put("alert", snap.isState() ? snap.getAlertLevel() : Level.OFFLINE);
            } else {
                temp.put("alert", snap.getAlertLevel());
            }
            json.add(temp);
        }
        writeDataToAjax(json);
        return NONE;
    }

    /**
     * 获取支持Snmp设备状态.
     * 
     * @return
     * @throws Exception
     */
    public String getSnmpEntityState() throws Exception {
        JSONArray json = new JSONArray();
        PageData<EntitySnap> pd = entityService.getSnmpEntityStateByPage(getPage());
        int size = pd == null ? 0 : pd.getData().size();
        List<EntitySnap> entities = pd.getData();
        for (int i = 0; i < size; i++) {
            EntitySnap snap = entities.get(i);
            JSONObject temp = new JSONObject();
            temp.put("entityId", snap.getEntityId());
            if (snap.getAlertLevel() == Level.CLEAR_LEVEL) {
                temp.put("alert", snap.isState() ? snap.getAlertLevel() : Level.OFFLINE);
            } else {
                temp.put("alert", snap.getAlertLevel());
            }
            json.add(temp);
        }

        writeDataToAjax(json);

        return NONE;
    }

    /**
     * 获取指定类型设备状态.
     * 
     * @return
     * @throws Exception
     */
    public String getTypeEntityState() throws Exception {
        JSONArray json = new JSONArray();
        PageData<EntitySnap> pd = entityService.getTypeEntityStateByPage(getPage(), typeId);
        int size = pd == null ? 0 : pd.getData().size();
        List<EntitySnap> entities = pd.getData();
        for (int i = 0; i < size; i++) {
            EntitySnap snap = entities.get(i);
            JSONObject temp = new JSONObject();
            temp.put("entityId", snap.getEntityId());
            if (snap.getAlertLevel() == Level.CLEAR_LEVEL) {
                temp.put("alert", snap.isState() ? snap.getAlertLevel() : Level.OFFLINE);
            } else {
                temp.put("alert", snap.getAlertLevel());
            }
            json.add(temp);
        }

        writeDataToAjax(json);

        return NONE;
    }

    /**
     * 返回代理设备详细信息.
     * 
     * @return
     * @throws Exception
     */
    public String loadAgentEntityByDetail() throws Exception {
        JSONObject json = new JSONObject();
        super.setSort("name");
        PageData<Entity> pageData = entityService.getEntityAgentSupported(getExtPage());
        int size = 0;
        JSONArray array = new JSONArray();
        if (pageData == null) {
            json.put("rowCount", 0);
        } else {
            json.put("rowCount", pageData.getRowCount());
            size = pageData.getData().size();
            List<Entity> list = pageData.getData();
            for (int i = 0; i < size; i++) {
                JSONObject temp = new JSONObject();
                EntitySnap entity = (EntitySnap) list.get(i);
                temp.put("entityId", entity.getEntityId());
                temp.put("name", entity.getName());
                temp.put("ip", entity.getIp());
                temp.put("typeName", entity.getTypeName());
                temp.put("snapTime", DateUtils.format(entity.getSnapTime()));
                temp.put("alert", entity.getAlertLevel());
                temp.put("state", entity.isState());
                temp.put("snmpSupport", entity.isSnmpSupport());
                temp.put("agentInstalled", true);
                array.add(temp);
            }
        }
        json.put("data", array);

        writeDataToAjax(json);

        return NONE;
    }

    /**
     * 显示代理设备信息.
     * 
     * @return
     * @throws Exception
     */
    public String loadAgentEntityByIcon() throws Exception {
        UserContext uc = (UserContext) getSession().get(UserContext.KEY);
        JSONObject json = new JSONObject();

        String s = uc.getUserPreferencesMap().get(NetworkConstants.ENTITY_SORT);
        UserPreferences userPreferences = new UserPreferences();
        userPreferences.setUserId(uc.getUserId());
        userPreferences.setModule("network");
        userPreferences.setName("entity.sortType");
        userPreferences.setValue(getSort());
        if (s == null || "".equals(s)) {
            userService.insertPreferences(userPreferences);
        } else {
            if (!s.equals(getSort())) {
                userService.updatePreferences(userPreferences);
            }
        }
        uc.getUserPreferencesMap().put(NetworkConstants.ENTITY_SORT, getSort());

        PageData<Entity> pageData = entityService.getEntityAgentSupported(getExtPage());
        int size = 0;
        JSONArray array = new JSONArray();
        if (pageData == null) {
            json.put("rowCount", 0);
        } else {
            json.put("rowCount", pageData.getRowCount());
            size = pageData.getData().size();
            List<Entity> list = pageData.getData();
            for (int i = 0; i < size; i++) {
                JSONObject temp = new JSONObject();
                EntitySnap entity = (EntitySnap) list.get(i);
                temp.put("entityId", entity.getEntityId());
                temp.put("name", entity.getName());
                temp.put("ip", entity.getIp());
                if (entity.getIcon() == null || "".equals(entity.getIcon())) {
                    temp.put("icon", entity.getIcon48());
                } else {
                    temp.put("icon", entity.getIcon());
                }
                temp.put("state", entity.isState());
                if (entity.getAlertLevel() == Level.CLEAR_LEVEL) {
                    temp.put("alert", entity.isState() ? entity.getAlertLevel() : Level.OFFLINE);
                } else {
                    temp.put("alert", entity.getAlertLevel());
                }
                array.add(temp);
            }
        }
        json.put("data", array);

        writeDataToAjax(json);

        return NONE;
    }

    /**
     * 获取指定类型的设备详细信息.
     * 
     * @return
     * @throws Exception
     */
    public String loadEntityByTypeByDetail() throws Exception {
        JSONObject json = new JSONObject();
        if (getSort() == null) {
            super.setSort("name");
            super.setDir("ASC");
        }

        PageData<Entity> pageData = entityService.getEntityByType(getExtPage(), typeId);
        int size = pageData == null ? 0 : pageData.getRowCount();
        json.put("rowCount", size);
        if (size > 0) {
            JSONArray array = new JSONArray();
            List<Entity> list = pageData.getData();
            size = list == null ? 0 : list.size();
            if (size > 0) {
                Entity entity = null;
                JSONObject temp = null;
                Date snapTime = new Date();
                for (int i = 0; i < size; i++) {
                    temp = new JSONObject();
                    entity = list.get(i);
                    temp.put("entityId", entity.getEntityId());
                    temp.put("name", entity.getName());
                    temp.put("ip", entity.getIp());
                    temp.put("modelName", entity.getModelName());
                    temp.put("alert", 0);
                    temp.put("state", true);
                    temp.put("snmpSupport", entity.isSnmpSupport());
                    temp.put("agentInstalled", entity.getAgentInstalled());
                    temp.put("snapTime", DateUtils.format(snapTime));
                    temp.put("parentId", entity.getParentId());
                    temp.put("mac", entity.getMac());
                    array.add(temp);
                }
                json.put("data", array);
            }
        }

        writeDataToAjax(json);

        return NONE;
    }

    /**
     * 返回特定类型设备信息.
     * 
     * @return
     * @throws Exception
     */
    @SuppressWarnings("unused")
    public String loadEntityByTypeByIcon() throws Exception {
        UserContext uc = (UserContext) getSession().get(UserContext.KEY);
        JSONObject json = new JSONObject();

        String s = uc.getUserPreferencesMap().get(NetworkConstants.ENTITY_SORT);
        UserPreferences userPreferences = new UserPreferences();
        userPreferences.setUserId(uc.getUserId());
        userPreferences.setModule("network");
        userPreferences.setName("entity.sortType");
        userPreferences.setValue(getSort());
        if (s == null || "".equals(s)) {
            userService.insertPreferences(userPreferences);
        } else {
            if (!s.equals(getSort())) {
                userService.updatePreferences(userPreferences);
            }
        }
        uc.getUserPreferencesMap().put(NetworkConstants.ENTITY_SORT, getSort());

        PageData<Entity> pageData = entityService.getEntityByType(getExtPage(), typeId);
        int size = 0;
        JSONArray array = new JSONArray();
        if (pageData == null) {
            json.put("rowCount", 0);
        } else {
            json.put("rowCount", pageData.getRowCount());
            size = pageData.getData().size();
            List<Entity> list = pageData.getData();
            Entity entity = null;
            JSONObject temp = null;
            for (int i = 0; i < size; i++) {
                temp = new JSONObject();
                entity = list.get(i);
                temp.put("entityId", entity.getEntityId());
                temp.put("name", entity.getName());
                temp.put("ip", entity.getIp());
                if (entity.getIcon() == null || "".equals(entity.getIcon())) {
                    temp.put("icon", entity.getIcon48());
                } else {
                    temp.put("icon", entity.getIcon());
                }
                temp.put("state", true);
                if (1 == Level.CLEAR_LEVEL) {
                    temp.put("alert", true ? 1 : Level.OFFLINE);
                } else {
                    temp.put("alert", 0);
                }
                array.add(temp);
            }
        }
        json.put("data", array);

        writeDataToAjax(json);

        return NONE;
    }

    /**
     * 获取取消管理的设备.
     * 
     * @return
     * @throws Exception
     */
    public String loadEntityCanceled() throws Exception {
        JSONObject json = new JSONObject();
        JSONArray array = new JSONArray();
        List<Entity> pd = entityService.getEntityInOffManagement();
        int size = pd == null ? 0 : pd.size();
        json.put("rowCount", size);
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                JSONObject temp = new JSONObject();
                Entity entity = pd.get(i);
                temp.put("entityId", entity.getEntityId());
                temp.put("name", entity.getName());
                temp.put("ip", entity.getIp());
                temp.put("typeName", entity.getTypeName());
                temp.put("modifyTime", DateUtils.format(entity.getOffManagementTime()));
                array.add(temp);
            }
        }
        json.put("data", array);

        writeDataToAjax(json);

        return NONE;
    }

    /**
     * 得到孤立的设备, 即不在任何拓扑文件夹中的设备.
     * 
     * @return
     * @throws Exception
     */
    public String loadEntityInLonely() throws Exception {
        JSONObject json = new JSONObject();
        PageData<Entity> pd = entityService.getEntityInLonely(getExtPage());
        JSONArray array = new JSONArray();
        if (pd == null) {
            json.put("rowCount", 0);
        } else {
            JSONObject temp = null;
            Entity entity = null;
            int size = pd.getData() == null ? 0 : pd.getData().size();
            json.put("rowCount", pd.getRowCount());
            for (int i = 0; i < size; i++) {
                temp = new JSONObject();
                entity = pd.getData().get(i);
                temp.put("entityId", entity.getEntityId());
                temp.put("name", entity.getName());
                temp.put("ip", entity.getIp());
                temp.put("typeName", entity.getTypeName());
                temp.put("modifyTime", DateUtils.format(entity.getModifyTime()));
                array.add(temp);
            }
        }
        json.put("data", array);

        writeDataToAjax(json);

        return NONE;
    }

    /**
     * 返回设备基本信息.
     * 
     * @return
     * @throws IOException
     * @throws JSONException
     */
    public String loadEntitySnapList() throws Exception {
        UserContext uc = (UserContext) getSession().get(UserContext.KEY);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("start", start);
        map.put("limit", limit);
        UserContext context = (UserContext) ServletActionContext.getRequest().getSession().getAttribute("UserContext");
        long userId = context.getUserId();
        map.put("userId", userId);
        if (queryText != null && !"".equals(queryText)) {
            // mysql中下划线是特殊的，like的时候必须转义
            if (queryText.contains("_")) {
                queryText = queryText.replace("_", "\\_");
            }
            map.put("queryText", queryText);
        }
        if (deviceType != null && deviceType != -1) {
            map.put("deviceType", deviceType);
        }
        if (onlineStatus != null && onlineStatus != -1) {
            map.put("onlineStatus", onlineStatus);
        }
        map.put("sort", getSort());// 排序字段
        map.put("dir", getDir());// 排序方向
        List<Snap> list = entityService.loadEntitySnapList(map);
        int count = entityService.loadEntitySnapCount(map);
        List<JSONObject> array = new ArrayList<JSONObject>();
        // add by fanzidong,需要在展示前格式化MAC地址
        String macRule = uc.getMacDisplayStyle();
        for (Snap entity : list) {
            JSONObject temp = new JSONObject();
            temp.put("entityId", entity.getEntityId());
            temp.put("parentId", entity.getParentId());
            temp.put("module", entity.getModule());
            temp.put("modulePath", entity.getModulePath());
            temp.put("name", entity.getName());
            temp.put("ip", entity.getIp());
            String mac = MacUtils.convertMacToDisplayFormat(entity.getMac(), macRule);
            temp.put("mac", mac);
            temp.put("type", entity.getType());
            temp.put("typeId", entity.getTypeId());
            // 如果是CCMTS
            if (entityTypeService.isCcmts(entity.getTypeId())) {
                Map<String, Boolean> supportMap = new HashMap<String, Boolean>();
                // 需要判断上联口配置是否支持
                supportMap.put("uplink", deviceVersionService.isFunctionSupported(entity.getEntityId(), "uplink"));
                // 需要CPU端口限速配置是否支持
                supportMap.put("cpuRateLimit",
                        deviceVersionService.isFunctionSupported(entity.getEntityId(), "cpuRateLimit"));
                temp.put("supportMap", supportMap);
            }
            // 管理状态
            temp.put("status", entity.isStatus());
            temp.put("typeName", entity.getTypeName());
            temp.put("attention", entity.isAttention());
            temp.put("contact", entity.getContact());
            temp.put("location", entity.getLocation());
            temp.put("note", entity.getNote());
            temp.put("alert", 0);
            // 在线状态
            temp.put("state", entity.isState());
            temp.put("lastRefreshTime",
                    DateUtils.getTimeDesInObscure(
                            Math.abs(System.currentTimeMillis() - entity.getLastRefreshTime().getTime()),
                            uc.getUser().getLanguage()));
            if (entity.getCpu() != null && entity.getCpu() > -1.0) {
                temp.put("cpu", NumberUtils.getPercentStr(entity.getCpu()));
            } else {
                temp.put("cpu", "-");
            }
            if (entity.getMem() != null && entity.getMem() > -1.0) {
                temp.put("mem", NumberUtils.getPercentStr(entity.getMem()));
            } else {
                temp.put("mem", "-");
            }
            temp.put("snapTime",
                    DateUtils.getTimeDesInObscure(Math.abs(System.currentTimeMillis() - entity.getSnapTime().getTime()),
                            context.getUser().getLanguage()));
            temp.put("sysName", entity.getSysName());
            if (entity.getSysUpTime() != null && entity.getSysUpTime() != -1) {
                Long tempTime = System.currentTimeMillis();
                temp.put("sysUptime", (tempTime - entity.getSnapTime().getTime()) / 1000 + entity.getSysUpTime() / 100);
            } else {
                temp.put("sysUptime", -1);
            }
            temp.put("createTime", DATE_FORMAT.format(entity.getCreateTime()));
            array.add(temp);
        }
        JSONObject json = new JSONObject();
        json.put("rowCount", count);
        json.put("data", array);
        writeDataToAjax(json);
        return NONE;
    }

    /**
     * 返回设备基本信息.
     * 
     * @return
     * @throws IOException
     * @throws JSONException
     */
    public String loadEntityList() throws Exception {
        JSONObject json = new JSONObject();
        Map<String, String> map = new HashMap<String, String>();
        // 设备别名
        if (alias != null && !"".equals(alias)) {
            map.put("alias", alias);
        }
        // 设备IP
        if (ip != null && !"".equals(ip)) {
            map.put("ip", ip);
        }
        // 设备类型
        if (entityType != null && !"".equals(entityType)) {
            map.put("entityType", entityType);
        }
        // 上级设备名称
        if (upperName != null && !"".equals(upperName)) {
            map.put("upperName", upperName);
        }
        // 所在地域
        if (folderIds != null && folderIds.size() > 0) {
            List<Long> authFolderIds = statReportService.getAuthFolderIds(folderIds);
            String folderStr = "";
            for (int i = 0; i < authFolderIds.size(); i++) {
                if (i + 1 == authFolderIds.size()) {
                    folderStr += authFolderIds.get(i).toString();
                } else {
                    folderStr += authFolderIds.get(i).toString() + ",";
                }
            }
            map.put("folderIds", folderStr);
        }
        map.put("offset", start + "");
        map.put("pageSize", limit + "");
        // map.put("queryText", queryText);// 查询条件
        map.put("sortName", getSort());// 排序字段
        map.put("sortDir", getDir());// 排序方向

        List<Entity> list = entityService.loadEntityList(map);
        long count = entityService.getEntityListCount(map);
        List<JSONObject> array = new ArrayList<JSONObject>();
        Date date = new Date();
        for (Entity entity : list) {
            JSONObject temp = new JSONObject();
            // sysDescr和location这两个地域字段需要做国际化处理
            temp.put("entityId", entity.getEntityId());
            temp.put("name", entity.getName());
            temp.put("ip", entity.getIp());
            temp.put("typeName", entity.getTypeName());
            temp.put("sysName", entity.getSysName());// 借用这个字段
            temp.put("sysDescr", translateFolderNames(entity.getSysDescr()));// 借用这个字段
            date.setTime(entity.getCreateTime().getTime());
            temp.put("createTime", DateUtils.format(date));
            temp.put("location", translateFolderNames(entity.getLocation()));
            temp.put("topoInfo", entity.getTopoInfo());
            temp.put("typeId", entity.getTypeId());
            temp.put("parentId", entity.getParentId());
            temp.put("uplinkDevice", entity.getUplinkDevice());
            array.add(temp);
        }

        json.put("rowCount", count);
        json.put("data", array);
        writeDataToAjax(json);
        return NONE;
    }

    private String translateFolderNames(String names) {
        if (names == null || names == "") {
            return "";
        }
        StringBuilder ret = new StringBuilder("");
        String[] nameArr = names.split(",");
        ResourceManager resourceManager = getResourceManager();
        for (int i = 0, len = nameArr.length; i < len; i++) {
            ret.append(resourceManager.getNotNullString(nameArr[i])).append(",");
        }
        return ret.substring(0, ret.length() - 1);
    }

    private ResourceManager getResourceManager() {
        return ResourceManager.getResourceManager("com.topvision.ems.network.resources");
    }

    /**
     * 返回我的设备信息.
     * 
     * @return
     * @throws Exception
     */
    @SuppressWarnings("unused")
    public String loadMyEntityByIcon() throws Exception {
        UserContext uc = (UserContext) getSession().get(UserContext.KEY);
        JSONObject json = new JSONObject();
        String s = uc.getUserPreferencesMap().get(NetworkConstants.ENTITY_SORT);
        UserPreferences userPreferences = new UserPreferences();
        userPreferences.setUserId(uc.getUserId());
        userPreferences.setModule("network");
        userPreferences.setName("entity.sortType");
        userPreferences.setValue(getSort());
        if (s == null || "".equals(s)) {
            userService.insertPreferences(userPreferences);
        } else {
            if (!s.equals(getSort())) {
                userService.updatePreferences(userPreferences);
            }
        }
        uc.getUserPreferencesMap().put(NetworkConstants.ENTITY_SORT, getSort());

        PageData<Entity> pageData = entityService.getEntity(getExtPage());
        int size = 0;
        JSONArray array = new JSONArray();
        if (pageData == null) {
            json.put("rowCount", 0);
        } else {
            json.put("rowCount", pageData.getRowCount());
            size = pageData.getData().size();
            List<Entity> list = pageData.getData();
            Entity entity = null;
            JSONObject temp = null;
            for (int i = 0; i < size; i++) {
                temp = new JSONObject();
                entity = list.get(i);
                temp.put("entityId", entity.getEntityId());
                temp.put("name", entity.getName());
                temp.put("ip", entity.getIp());
                temp.put("icon", entity.getIcon());
                temp.put("snmpSupport", entity.isSnmpSupport());
                temp.put("agentInstalled", entity.getAgentInstalled());
                temp.put("type", entity.getTypeId());
                if (1 == Level.CLEAR_LEVEL) {
                    temp.put("alert", true ? 1 : Level.OFFLINE);
                } else {
                    temp.put("alert", false);
                }
                array.add(temp);
            }
        }
        json.put("data", array);

        writeDataToAjax(json);
        return NONE;
    }

    /**
     * 获取用户当前浏览所有设备的页面.
     * 
     * @return
     */
    public String loadMyEntityJsp() {
        UserContext uc = (UserContext) getSession().get(UserContext.KEY);
        setPageSize(uc.getUserPreferencesMap().getInt(NetworkConstants.ENTITY_PAGESIZE, uc.getPageSize()));
        displayName = uc.getUserPreferencesMap().getBoolean(NetworkConstants.ENTITY_DISPLAYNAME, true);
        sortType = uc.getUserPreferencesMap().getString(NetworkConstants.ENTITY_SORT, "name");
        return SUCCESS;
    }

    /**
     * 返回Snmp设备详细信息.
     * 
     * @return
     * @throws Exception
     */
    public String loadSnmpEntityByDetail() throws Exception {
        JSONObject json = new JSONObject();

        super.setSort("name");
        PageData<Entity> pageData = entityService.getEntitySnmpSupported(getExtPage());
        int size = 0;
        JSONArray array = new JSONArray();
        if (pageData == null) {
            json.put("rowCount", 0);
        } else {
            json.put("rowCount", pageData.getRowCount());
            size = pageData.getData().size();
            List<Entity> list = pageData.getData();
            for (int i = 0; i < size; i++) {
                JSONObject temp = new JSONObject();
                EntitySnap entity = (EntitySnap) list.get(i);
                temp.put("entityId", entity.getEntityId());
                temp.put("name", entity.getName());
                temp.put("ip", entity.getIp());
                temp.put("typeName", entity.getTypeName());
                temp.put("snapTime", DateUtils.format(entity.getSnapTime()));
                temp.put("alert", entity.getAlertLevel());
                temp.put("state", entity.isState());
                temp.put("snmpSupport", true);
                temp.put("agentInstalled", entity.getAgentInstalled());

                array.add(temp);
            }
        }
        json.put("data", array);

        writeDataToAjax(json);
        return NONE;
    }

    /**
     * 显示支持Snmp设备信息.
     * 
     * @return
     * @throws Exception
     */
    public String loadSnmpEntityByIcon() throws Exception {
        UserContext uc = (UserContext) getSession().get(UserContext.KEY);
        JSONObject json = new JSONObject();

        String s = uc.getUserPreferencesMap().get(NetworkConstants.ENTITY_SORT);
        UserPreferences userPreferences = new UserPreferences();
        userPreferences.setUserId(uc.getUserId());
        userPreferences.setModule("network");
        userPreferences.setName("entity.sortType");
        userPreferences.setValue(getSort());
        if (s == null || "".equals(s)) {
            userService.insertPreferences(userPreferences);
        } else {
            if (!s.equals(getSort())) {
                userService.updatePreferences(userPreferences);
            }
        }
        uc.getUserPreferencesMap().put(NetworkConstants.ENTITY_SORT, getSort());

        PageData<Entity> pageData = entityService.getEntitySnmpSupported(getExtPage());
        int size = 0;
        JSONArray array = new JSONArray();
        if (pageData == null) {
            json.put("rowCount", 0);
        } else {
            json.put("rowCount", pageData.getRowCount());
            size = pageData.getData().size();
            List<Entity> list = pageData.getData();
            for (int i = 0; i < size; i++) {
                JSONObject temp = new JSONObject();
                EntitySnap entity = (EntitySnap) list.get(i);
                temp.put("entityId", entity.getEntityId());
                temp.put("name", entity.getName());
                temp.put("ip", entity.getIp());
                if (entity.getIcon() == null || "".equals(entity.getIcon())) {
                    temp.put("icon", entity.getIcon48());
                } else {
                    temp.put("icon", entity.getIcon());
                }
                temp.put("state", entity.isState());
                if (entity.getAlertLevel() == Level.CLEAR_LEVEL) {
                    temp.put("alert", entity.isState() ? entity.getAlertLevel() : Level.OFFLINE);
                } else {
                    temp.put("alert", entity.getAlertLevel());
                }
                array.add(temp);
            }
        }
        json.put("data", array);
        writeDataToAjax(json);
        return NONE;
    }

    public String showUserAttention() {
        return SUCCESS;
    }

    public String loadUserAttentionList() throws JSONException, IOException {
        UserContext uc = (UserContext) getSession().get(UserContext.KEY);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("start", start);
        map.put("limit", limit);
        UserContext context = (UserContext) ServletActionContext.getRequest().getSession().getAttribute("UserContext");
        long userId = context.getUserId();
        map.put("userId", userId);
        if (queryText != null && !"".equals(queryText)) {
            // mysql中下划线是特殊的，like的时候必须转义
            if (queryText.contains("_")) {
                queryText = queryText.replace("_", "\\_");
            }
            map.put("queryText", queryText);
        }
        if (deviceType != null && deviceType != -1) {
            map.put("deviceType", deviceType);
        }
        if (onlineStatus != null && onlineStatus != -1) {
            map.put("onlineStatus", onlineStatus);
        }
        map.put("sort", getSort());// 排序字段
        map.put("dir", getDir());// 排序方向
        List<Snap> list = entityService.loadUserAttentionList(map);
        int count = entityService.userAttentionCount(map);
        List<JSONObject> array = new ArrayList<JSONObject>();
        // add by fanzidong,需要在展示前格式化MAC地址
        String macRule = uc.getMacDisplayStyle();
        for (Snap entity : list) {
            JSONObject temp = new JSONObject();
            temp.put("entityId", entity.getEntityId());
            temp.put("parentId", entity.getParentId());
            temp.put("module", entity.getModule());
            temp.put("modulePath", entity.getModulePath());
            temp.put("name", entity.getName());
            temp.put("sysName", entity.getSysName());
            temp.put("parentName", entity.getParentName());
            temp.put("alertNum", entity.getAlertNum());
            temp.put("typeId", entity.getTypeId());
            temp.put("ip", entity.getIp());
            temp.put("uplinkDevice", entity.getUplinkDevice());
            String mac = MacUtils.convertMacToDisplayFormat(entity.getMac(), macRule);
            temp.put("mac", mac);
            temp.put("typeName", entity.getTypeName());
            // 在线状态
            temp.put("state", entity.isState());
            array.add(temp);
        }
        JSONObject json = new JSONObject();
        json.put("rowCount", count);
        json.put("data", array);
        json.write(response.getWriter());
        return NONE;
    }

    /**
     * 显示代理设备信息.
     * 
     * @return
     */
    public String showAgentEntityJsp() {
        return loadMyEntityJsp();
    }

    /**
     * 显示指定类型设备信息.
     * 
     * @return
     */
    public String showEntityByTypeJsp() {
        return loadMyEntityJsp();
    }

    /**
     * 显示Snmp设备信息.
     * 
     * @return
     */
    public String showSnmpEntityJsp() {
        return loadMyEntityJsp();
    }

    public void setDisplayName(boolean displayName) {
        this.displayName = displayName;
    }

    public void setEntityService(EntityService entityService) {
        this.entityService = entityService;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    public void setRefreshInterval(long refreshInterval) {
        this.refreshInterval = refreshInterval;
    }

    public void setSortType(String sortType) {
        this.sortType = sortType;
    }

    public void setTypeId(long typeId) {
        this.typeId = typeId;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public void setViewType(String viewType) {
        this.viewType = viewType;
    }

    @Override
    public int getStart() {
        return start;
    }

    @Override
    public void setStart(int start) {
        this.start = start;
    }

    @Override
    public int getLimit() {
        return limit;
    }

    @Override
    public void setLimit(int limit) {
        this.limit = limit;
    }

    public String getQueryText() {
        return queryText;
    }

    public void setQueryText(String queryText) {
        this.queryText = queryText;
    }

    public String getEntityType() {
        return entityType;
    }

    public long getRefreshInterval() {
        return refreshInterval;
    }

    public long getTypeId() {
        return typeId;
    }

    public String getViewType() {
        return viewType;
    }

    public boolean isDisplayName() {
        return displayName;
    }

    public String getSortType() {
        return sortType;
    }

    public Long getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(Long deviceType) {
        this.deviceType = deviceType;
    }

    public Integer getOnlineStatus() {
        return onlineStatus;
    }

    public void setOnlineStatus(Integer onlineStatus) {
        this.onlineStatus = onlineStatus;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getUpperName() {
        return upperName;
    }

    public void setUpperName(String upperName) {
        this.upperName = upperName;
    }

    public List<Long> getFolderIds() {
        return folderIds;
    }

    public void setFolderIds(List<Long> folderIds) {
        this.folderIds = folderIds;
    }

    public String getEntityIds() {
        return entityIds;
    }

    public void setEntityIds(String entityIds) {
        this.entityIds = entityIds;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

}
