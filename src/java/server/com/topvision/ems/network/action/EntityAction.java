package com.topvision.ems.network.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.struts2.ServletActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.facade.domain.EntityType;
import com.topvision.ems.facade.domain.PingResultsEntry;
import com.topvision.ems.facade.domain.Snap;
import com.topvision.ems.network.service.EntityService;
import com.topvision.ems.network.service.TopologyService;
import com.topvision.ems.template.service.EntityTypeService;
import com.topvision.exception.service.ExistEntityException;
import com.topvision.framework.common.MacUtils;
import com.topvision.framework.exception.engine.SnmpNoSuchObjectException;
import com.topvision.framework.snmp.SnmpParam;
import com.topvision.framework.web.struts2.BaseAction;
import com.topvision.platform.ResourceManager;
import com.topvision.platform.domain.SystemPreferences;
import com.topvision.platform.domain.User;
import com.topvision.platform.domain.UserContext;
import com.topvision.platform.service.SystemPreferencesService;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;

@Controller("entityAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class EntityAction extends BaseAction {
    private static final long serialVersionUID = 4847709442506698352L;
    private static Logger logger = LoggerFactory.getLogger(EntityAction.class);
    @Autowired
    private EntityService entityService;
    @Autowired
    private EntityTypeService entityTypeService;
    @Autowired
    private TopologyService topologyService;
    @Autowired
    private SystemPreferencesService systemPreferencesService;
    private Entity entity;
    private SnmpParam snmpParam;
    private String name;
    private String location;
    private String contact;
    private String note;
    private long entityId;
    private long folderId;
    private long destFolderId;
    private String sysObjectID;
    private List<Long> entityIds;
    private String cmd;
    private String ip;
    private List<Long> userIds;
    private List<User> userList;
    private String sourceType;
    private String pageId;
    private List<Long> folderIds;
    private String entityIdStr;
    private String action = "editTopoFolder";

    private String pingCount;
    private String pingTimeout;

    /**
     * 取消设备的管理.
     * 
     * @return
     */
    public String cancelManagement() {
        entityService.txCancelManagement(entityIds, false);
        return NONE;
    }

    /**
     * 从回收站中删除设备.
     * 
     * @return
     */
    public String deleteEntityFromRecyle() {
        entityService.removeEntity(entityIds);
        return NONE;
    }

    /**
     * 跳转设备别名修改页面
     * 
     * @return
     */
    public String showRenameEntity() {
        entity = entityService.getEntity(entityId);
        return SUCCESS;
    }

    /**
     * 修改设备别名
     * 
     * @throws JSONException
     * @throws IOException
     */
    public String changeEntityInfoByEntityId() throws Exception {
    	entityService.modifyEntityInfo(entityId, name,location,contact,note);
        return NONE;
    }
    
    /**
     * 重命名
     * 
     * @throws JSONException
     * @throws IOException
     */
    public String renameEntityByEntityId() throws Exception {
        entityService.renameEntity(entityId, name);
        return NONE;
    }

    /**
     * 取消关注
     * 
     * @return
     */
    public String cancelAttention() {
        UserContext context = (UserContext) ServletActionContext.getRequest().getSession().getAttribute("UserContext");
        long userId = context.getUserId();
        entityService.cancelAttention(userId, entityId);
        return NONE;
    }

    /**
     * 添加关注
     * 
     * @return
     */
    public String pushAttention() {
        UserContext context = (UserContext) ServletActionContext.getRequest().getSession().getAttribute("UserContext");
        long userId = context.getUserId();
        entityService.pushAttention(userId, entityId);
        return NONE;
    }

    /**
     * 加载关注设备列表
     * 
     * @return
     * @throws IOException
     */
    public String getAttentionEntityList() throws IOException {
        Map<String, String> map = new HashMap<String, String>();
        UserContext context = (UserContext) getSession().get(UserContext.KEY);
        ResourceManager resourceManager = ResourceManager.getResourceManager("com.topvision.ems.resources.resources");

        long userId = context.getUserId();
        map.put("userId", userId + "");
        List<Snap> list = entityService.getAttentionEntityList(map);
        StringBuilder sb = new StringBuilder();
        sb.append(
                "<table class='dataTable zebra noWrap' width='100%' border='1' cellspacing='0' cellpadding='0' bordercolor='#DFDFDF' rules='all'><thead><tr><th>");
        sb.append(resourceManager.getString("Config.oltConfigFileImported.deviceName"));
        sb.append("</th><th width='100'>");
        sb.append(resourceManager.getString("COMMON.ip"));
        sb.append("</th><th>");
        sb.append(resourceManager.getString("WorkBench.OnlineStatus"));
        sb.append("</th><th width='64'>");
        sb.append(resourceManager.getString("WorkBench.AdminStatus"));
        sb.append("</th><tr><tbody>");
        for (Snap entity : list) {
            sb.append("<tr><td class='txtLeft wordBreak'>")
                    .append("<a  class=my-link href=\"javascript:showEntitySnap(" + entity.getEntityId() + ",'"
                            + entity.getIp() + "')\">" + entity.getName() + "</a>")
                    .append("</td><td>").append(entity.getIp()).append("</td><td align='center'>");
            if (entity.isState()) {
                sb.append("<img class='nm3kTip' nm3kTip=");
                sb.append(resourceManager.getString("COMMON.online"));
                sb.append(" src=\"../images/fault/trap_on.png\" border=0 align=absmiddle></td><td align='center'>");
            } else {
                sb.append("<img class='nm3kTip' nm3kTip=");
                sb.append(resourceManager.getString("COMMON.unreachable"));
                sb.append(" src='../images/fault/trap_off.png' border=0 align=absmiddle</td><td align='center'>");
            }
            if (entity.isStatus()) {
                sb.append("<img class='nm3kTip' nm3kTip=");
                sb.append(resourceManager.getString("COMMON.manageble"));
                sb.append(" src=\"../images/fault/online.gif\" border=0 align=absmiddle></td></tr>");
            } else {
                sb.append("<img class='nm3kTip' nm3kTip=");
                sb.append(resourceManager.getString("COMMON.unmanageble"));
                sb.append(" src=\"../images/fault/offline.gif\" border=0 align=absmiddle></td></tr>");
            }
        }
        sb.append("</tr></tbody></table>");
        writeDataToAjax(sb.toString());
        return NONE;
    }

    /**
     * 清空回收站.
     * 
     * @return
     */
    public String emptyRecyle() {
        entityService.txEmptyRecyle(null);
        return NONE;
    }

    /**
     * 激活设备的管理.
     * 
     * @return
     */
    public String enableManagement() {
        entityService.txCancelManagement(entityIds, true);
        return NONE;
    }

    /**
     * 移动设备到目标文件夹.
     * 
     * @return
     */
    public String moveEntityById() {
        topologyService.txMoveEntity(null, folderId, destFolderId, entityIds);
        return NONE;
    }

    /**
     * 从回收站移动设备到给定目标文件夹.
     * 
     * @return
     */
    public String moveEntityFromRecyle() {
        topologyService.txMoveEntity(destFolderId, entityIds);
        return NONE;
    }

    /**
     * 移动设备到回收站.
     * 
     * @return
     */
    public String moveEntiyToRecyle() {
        entityService.txMoveEntityToRecyle(entityIds);
        return NONE;
    }

    /**
     * replace entity
     * 
     * @return
     * @throws JSONException
     * @throws IOException
     */
    public String replaceEntity() throws Exception {
        JSONObject json = new JSONObject();
        try {
            // 获取基本信息
            Entity entity = entityService.getEntity(entityId);
            SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
            // TopoFolder folder = topologyService.getFolderIdAndName(entityId);

            // 删除设备
            List<Long> idsList = new ArrayList<Long>();
            idsList.add(entityId);
            entityService.txMoveEntityToRecyle(idsList);

            // 重新发现设备
            EntityType type = entityService.topoEntityTypeId(entity, snmpParam);
            if (type != null) {
                entity.setCorpId(entityTypeService.getCorpBySysObjId(type.getSysObjectID()));
                // entity.setType(type.getType());
                entity.setTypeId(type.getTypeId());
                entity.setIcon48(type.getIcon48());
            }
            entity.setFolderId(10L);
            entity.setParam(snmpParam);
            // 设置其默认为可管理的
            entity.setStatus(Boolean.TRUE);
            UserContext uc = (UserContext) getSession().get(UserContext.KEY);
            entity.setAuthorityUserId(uc.getUserId());
            entityService.txCreateEntity(entity);
            // 设备添加后发送消息
            entityService.txCreateMessage(entity);

            json.put("result", true);
        } catch (Exception e) {
            json.put("result", false);
            logger.debug("replace device fail:", e);
        } finally {
            writeDataToAjax(json);
        }
        return NONE;
    }

    /**
     * 对设备进行重命名, 更改设备在拓扑图中的显示.
     * 
     * @return
     */
    public String renameEntity() {
        Entity e = new Entity();
        e.setEntityId(entityId);
        e.setNameInFolder(name);
        e.setName(name);
        e.setFolderId(folderId);
        topologyService.txRenameEntity(e);
        return NONE;
    }

    /**
     * 运行命令.
     * 
     * @return
     */
    public String runCmd() {
        entityService.cmd(ServletActionContext.getRequest().getSession().getId(), cmd, ip);
        // 如果命令是ping，获取ping的次数和超时时间
        if ("ping".equals(cmd) || "snmpping".equals(cmd)) {
            Properties properties = systemPreferencesService.getModulePreferences("toolPing");
            setPingCount(properties.getProperty("Ping.count"));
            setPingTimeout(properties.getProperty("Ping.timeout"));
        }
        return SUCCESS;
    }

    /**
     * 运行命令.
     * 
     * @return
     */
    public String snmpPing() {
        Properties properties = systemPreferencesService.getModulePreferences("toolPing");
        setPingCount(properties.getProperty("Ping.count"));
        setPingTimeout(properties.getProperty("Ping.timeout"));
        return SUCCESS;
    }

    /**
     * ping
     * 
     * @return
     */
    public String ping() {
        // 先保存工具ping的配置
        List<SystemPreferences> preferences = new ArrayList<SystemPreferences>();
        SystemPreferences pingPreference = new SystemPreferences();
        // save Ping.retries
        pingPreference.setName("Ping.count");
        pingPreference.setValue(pingCount);
        pingPreference.setModule("toolPing");
        preferences.add(pingPreference);
        // save Ping.timeout
        pingPreference = new SystemPreferences();
        pingPreference.setName("Ping.timeout");
        pingPreference.setValue(pingTimeout);
        pingPreference.setModule("toolPing");
        preferences.add(pingPreference);
        // save to database
        systemPreferencesService.savePreferences(preferences);
        // 下发ping命令
        entityService.cmd(ServletActionContext.getRequest().getSession().getId(), "ping", ip);
        return NONE;
    }

    /**
     * 传回运行命令结果.
     * 
     * @return
     * @throws Exception
     */
    public String getRunCmdResult() throws Exception {
        JSONObject json = new JSONObject();
        String r = entityService.cmd(ServletActionContext.getRequest().getSession().getId(), cmd, null);
        if (r.endsWith("#OK#")) {
            json.put("result", r.substring(0, r.length() - 4));
            json.put("success", true);
        } else {
            json.put("result", r);
            json.put("success", false);
        }
        writeDataToAjax(json);
        return NONE;
    }

    public String getRunSnmpResult() {
        JSONObject json = new JSONObject();
        Entity entity = entityService.getEntity(entityId);
        if (entity.getIp() == null) {
            entity = entityService.getEntity(entity.getParentId());
        }
        StringBuilder desc = new StringBuilder(entity.getName()).append("[").append(entity.getIp()).append("]");
        try {
            PingResultsEntry r = entityService.snmpPing(entityId, ip, Integer.parseInt(pingCount),
                    Integer.parseInt(pingTimeout));
            if (r != null) {
                json.put("result", JSONObject.fromObject(r));
                json.put("success", true);
            } else {
                json.put("result", r);
                json.put("success", false);
            }
        } catch (SnmpNoSuchObjectException e) {
            json.put("result", "The device is not support snmp ping");
            json.put("success", false);
        } catch (RuntimeException e) {
            if (e.getMessage() != null && e.getMessage().contains("SnmpNoSuchObjectException")) {
                json.put("result", "The device is not support snmp ping");
                json.put("success", false);
            } else if (e.getMessage() != null && e.getMessage().contains("SnmpNoResponseException")) {
                json.put("result", "The device " + desc.toString() + " SnmpNoResponse");
                json.put("success", false);
            } else {
                logger.error("", e);
                json.put("result", e.getMessage());
                json.put("success", false);
            }
        } catch (Exception e) {
            logger.error("", e);
            json.put("result", e.getMessage());
            json.put("success", false);
        }
        writeDataToAjax(json);
        return NONE;
    }

    /**
     * 保存设备详细信息.
     * 
     * @return
     * @throws Exception
     */
    public String saveEntityDetail() throws Exception {
        JSONObject json = new JSONObject();
        try {
            // 更新设备的概要信息
            entityService.updateEntityOutline(entity);

            SnmpParam param = entityService.getSnmpParamByEntity(entity.getEntityId());
            param.setVersion(snmpParam.getVersion());
            param.setCommunity(snmpParam.getCommunity());
            param.setWriteCommunity(snmpParam.getWriteCommunity());
            param.setUsername(snmpParam.getUsername());
            param.setSecurityLevel(snmpParam.getSecurityLevel());
            param.setAuthProtocol(snmpParam.getAuthProtocol());
            param.setAuthPassword(snmpParam.getAuthPassword());
            param.setContextName(snmpParam.getContextName());
            param.setContextId(snmpParam.getContextId());
            param.setAuthoritativeEngineID(snmpParam.getAuthoritativeEngineID());
            param.setPrivProtocol(snmpParam.getPrivProtocol());
            param.setPrivPassword(snmpParam.getPrivPassword());
            entityService.updateSnmpParam(param);

            json.put("exist", false);
            json.put("ip", entity.getIp());
        } catch (ExistEntityException eee) {
            logger.debug("Save Entity detail.", eee);
            json.put("exist", true);
        } finally {
            writeDataToAjax(json);
        }

        return NONE;
    }

    /**
     * 显示编辑设备属性页面.
     * 
     * @return
     */
    public String showEditEntityJsp() {
        entity = entityService.getEntity(entityId);
        snmpParam = entityService.getSnmpParamByEntity(entityId);
        if (entityTypeService.isCcmtsAndCmts(entity.getTypeId())) {
            return "cmc";
        }
        return SUCCESS;
    }

    /**
     * 显示设备的概要信息.
     * 
     * @return
     */
    public String showEntityPropertyJsp() {
        if (folderId > 0) {
            entity = entityService.getEntity(entityId, folderId);
        } else {
            entity = entityService.getEntity(entityId);
        }
        // add by fanzidong,需要在展示前格式化MAC地址
        UserContext uc = (UserContext) super.getSession().get(UserContext.KEY);
        String macRule = uc.getMacDisplayStyle();
        String formatedMac = MacUtils.convertMacToDisplayFormat(entity.getMac(), macRule);
        entity.setMac(formatedMac);
        return SUCCESS;
    }

    /**
     * 更新指定设备fixed信息.
     * 
     * @return
     */
    public String updateEntityFixed() {
        entityService.updateEntityFixed(entity);
        return NONE;
    }

    /**
     * 更新指定设备拓扑文件夹图标信息.
     * 
     * @return
     */
    public String updateEntityIcon() {
        entity.setIcon(entity.getIconInFolder());
        topologyService.updateEntityIcon(entity);
        return NONE;
    }

    /**
     * 更新设备的概要信息:ip, 位置, 负责人和描述.
     * 
     * @return
     */
    public String updateEntityOutline() {
        entity.setEntityId(entityId);
        entityService.updateEntityOutline(entity);

        return NONE;
    }

    /**
     * 修改设备的类型.
     * 
     * @return
     */
    public String updateEntityType() {
        entityService.updateEntityType(entity);
        return NONE;
    }

    /**
     * 更新设备的URL信息并更改设备在拓扑图中的显示.
     * 
     * @return
     */
    public String updateEntityUrl() {
        // 最初设计时用nameInFolder表示不同文件夹下的别名，现在统一用name表示别名。
        entity.setName(entity.getNameInFolder());
        entityService.updateEntityUrl(entity);
        topologyService.txRenameEntity(entity);
        return NONE;
    }

    /**
     * 显示设备责任人
     * 
     * @return
     */
    public String loadEntityRelationUser() {
        List<User> userList = entityService.getEntityRelationUsers(entityId);
        this.setUserList(userList);
        return SUCCESS;
    }

    /**
     * 更新设备责任人
     * 
     * @return
     */
    public String updateEntityRelationUser() {
        String userIds[] = ServletActionContext.getRequest().getParameterValues("chkSon");
        entityService.updateEntityRelationUsers(entityId, userIds);
        return NONE;
    }

    public String editTopoFolder() {
        return SUCCESS;
    }

    /**
     * 获取该设备所在的地域列表
     * 
     * @return
     * @throws IOException
     */
    public String getEntityLocatedFolderIds() throws IOException {
        List<Long> folderIds = topologyService.getEntityLocatedFolderIds(entityId);
        JSONArray.fromObject(folderIds).write(response.getWriter());
        return NONE;
    }

    public String setEntityLocatedFolders() {
        UserContext uc = (UserContext) getSession().get(UserContext.KEY);
        entityService.setEntityLocatedFolders(uc.getUserId(), entityId, folderIds);
        return NONE;
    }

    public String setEntitiesLocatedFolders() {
        UserContext uc = (UserContext) getSession().get(UserContext.KEY);
        entityService.setEntitiesLocatedFolders(uc.getUserId(), entityIds, folderIds);
        return NONE;
    }

    public String addEntitiesToFolders() {
        entityService.addEntitiesToFolders(entityIds, folderIds);
        return NONE;
    }

    public String removeEntitiesFromFolders() throws JSONException, IOException {
        // 批量将设备从地域中移除时，需要剔除移除后没有地域的设备
        List<Long> canRemoveIds = new ArrayList<Long>();
        List<Long> noRemoveIds = new ArrayList<Long>();
        Boolean canRemove;
        for (Long entityId : entityIds) {
            canRemove = entityService.canRemoveEntityFromFolder(entityId, folderIds);
            if (canRemove) {
                canRemoveIds.add(entityId);
            } else {
                noRemoveIds.add(entityId);
            }
        }
        entityService.removeEntitiesFromFolders(canRemoveIds, folderIds);
        // 返回操作结果
        JSONObject json = new JSONObject();
        json.put("successIds", canRemoveIds);
        json.put("failedIds", noRemoveIds);
        // 根据失败Id获取失败entity的名称
        List<String> failedNames = entityService.getEntityNamesByIds(noRemoveIds);
        if (failedNames != null && failedNames.size() > 0) {
            json.put("failedNames", failedNames);
        }
        json.write(response.getWriter());
        return NONE;
    }

    public String getSourceType() {
        return sourceType;
    }

    public void setSourceType(String sourceType) {
        this.sourceType = sourceType;
    }

    public SnmpParam getSnmpParam() {
        return snmpParam;
    }

    public String getSysObjectID() {
        return sysObjectID;
    }

    public void setCmd(String cmd) {
        this.cmd = cmd;
    }

    public void setDestFolderId(long destFolderId) {
        this.destFolderId = destFolderId;
    }

    public void setEntity(Entity entity) {
        this.entity = entity;
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

    public void setUserIds(List<Long> userIds) {
        this.userIds = userIds;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSnmpParam(SnmpParam snmpParam) {
        this.snmpParam = snmpParam;
    }

    public void setSysObjectID(String sysObjectID) {
        this.sysObjectID = sysObjectID;
    }

    public void setTopologyService(TopologyService topologyService) {
        this.topologyService = topologyService;
    }

    public String getCmd() {
        return cmd;
    }

    public long getDestFolderId() {
        return destFolderId;
    }

    public Entity getEntity() {
        return entity;
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

    public List<Long> getUserIds() {
        return userIds;
    }

    public List<User> getUserList() {
        return userList;
    }

    public void setUserList(List<User> userList) {
        this.userList = userList;
    }

    public String getIp() {
        return ip;
    }

    public String getName() {
        return name;
    }

    public String getPageId() {
        return pageId;
    }

    public void setPageId(String pageId) {
        this.pageId = pageId;
    }

    public List<Long> getFolderIds() {
        return folderIds;
    }

    public void setFolderIds(List<Long> folderIds) {
        this.folderIds = folderIds;
    }

    public String getEntityIdStr() {
        return entityIdStr;
    }

    public void setEntityIdStr(String entityIdStr) {
        this.entityIdStr = entityIdStr;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getPingCount() {
        return pingCount;
    }

    public void setPingCount(String pingCount) {
        this.pingCount = pingCount;
    }

    public String getPingTimeout() {
        return pingTimeout;
    }

    public void setPingTimeout(String pingTimeout) {
        this.pingTimeout = pingTimeout;
    }

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}
       

}
