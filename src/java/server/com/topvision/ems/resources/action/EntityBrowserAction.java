/**
 * 
 */
package com.topvision.ems.resources.action;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.facade.domain.EntityType;
import com.topvision.ems.network.domain.Port;
import com.topvision.ems.network.service.EntityService;
import com.topvision.ems.network.service.PortService;
import com.topvision.ems.resources.domain.EntityCategoryStat;
import com.topvision.ems.resources.service.ResourcesService;
import com.topvision.framework.web.struts2.BaseAction;
import com.topvision.platform.ResourceManager;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * @author niejun
 * 
 */
@Controller("entityBrowserAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class EntityBrowserAction extends BaseAction {
    private static final long serialVersionUID = 1294804467780257709L;
    private static Logger logger = LoggerFactory.getLogger(EntityBrowserAction.class);
    @Autowired
    private EntityService entityService;
    @Autowired
    private ResourcesService resourcesService;
    @Autowired
    private PortService portService;
    private boolean snmpSupport;
    private boolean agentInstalled;
    private String node = null;

    private JSONArray getDeviceByType(long typeId) throws Exception {
        JSONArray array = new JSONArray();
        JSONObject json = null;
        Entity entity = null;
        List<Entity> list = entityService.getEntityByTypeId(typeId);
        int size = list.size();
        for (int i = 0; i < size; i++) {
            entity = list.get(i);
            json = new JSONObject();
            json.put("id", "entity" + entity.getEntityId());
            json.put("iconCls", "entityIcon");
            json.put("leaf", !entity.isSnmpSupport());
            json.put("type", "entity");
            json.put("entityId", entity.getEntityId());
            json.put("entityIp", entity.getIp());
            json.put("snmpSupport", entity.isSnmpSupport());
            json.put("text", entity.getDisplayText());
            array.add(json);
        }
        return array;
    }

    private JSONArray getDeviceByType1(long typeId, boolean leaf) throws Exception {
        JSONArray array = new JSONArray();
        JSONObject json = null;
        Entity entity = null;
        List<Entity> list = entityService.getEntityByTypeId(typeId);
        int size = list.size();
        for (int i = 0; i < size; i++) {
            entity = list.get(i);
            json = new JSONObject();
            json.put("id", "entity" + entity.getEntityId());
            json.put("iconCls", "entityIcon");
            json.put("leaf", leaf);
            json.put("typeId", "-1");
            json.put("type", "entity");
            json.put("entityId", entity.getEntityId());
            json.put("snmpSupport", entity.isSnmpSupport());
            json.put("entityIp", entity.getIp());
            json.put("text", entity.getDisplayText());
            array.add(json);
        }
        return array;
    }

    private JSONArray getDeviceCategory(boolean snmp) throws Exception {
        ResourceManager resourceManager = ResourceManager.getResourceManager("com.topvision.ems.network.resources");
        JSONArray array = new JSONArray();
        JSONObject json = null;
        JSONArray array1 = new JSONArray();
        EntityCategoryStat entityCategoryStat = null;
        if (snmp) {
            entityCategoryStat = resourcesService.statSnmpEntityCategory();
        } else {
            entityCategoryStat = resourcesService.statEntityCategory();
        }
        int size = entityCategoryStat.getParentCategory().size();
        EntityType entityType = null;
        for (int i = 0; i < size; i++) {
            entityType = entityCategoryStat.getParentCategory().get(i);
            json = new JSONObject();
            json.put("id", "type" + entityType.getTypeId());
            json.put("iconCls", entityType.getName() + "Icon");
            json.put("text", entityType.getDisplayName());
            json.put("leaf", entityType.getEntityCount() == 0);
            json.put("typeId", entityType.getTypeId());
            json.put("entityId", entityType.getTypeId());
            array1.add(json);
        }

        JSONObject network = new JSONObject();
        network.put("text",
                snmp ? resourceManager.getString("allSnmpEntity") : resourceManager.getNotNullString("allEntity"));
        network.put("id", "type0");
        network.put("iconCls", "myEntityIcon");
        network.put("leaf", false);
        network.put("typeId", 0);
        network.put("entityId", 0);
        network.put("expanded", true);
        network.put("children", array1);
        array.add(network);
        return array;
    }

    private JSONArray getDeviceCategoryStat(boolean snmp) throws Exception {
        ResourceManager resourceManager = ResourceManager.getResourceManager("com.topvision.ems.network.resources");
        JSONArray array = new JSONArray();
        JSONObject json = null;
        EntityType entityType = null;
        JSONArray array1 = new JSONArray();
        EntityCategoryStat entityCategoryStat = null;
        if (snmp) {
            entityCategoryStat = resourcesService.statSnmpEntityCategory();
        } else {
            entityCategoryStat = resourcesService.statEntityCategory();
        }
        int size = entityCategoryStat.getParentCategory().size();
        int count = 0;
        for (int i = 0; i < size; i++) {
            entityType = entityCategoryStat.getParentCategory().get(i);
            count = count + entityType.getEntityCount();
            json = new JSONObject();
            json.put("id", "type" + entityType.getTypeId());
            json.put("iconCls", entityType.getName() + "Icon");
            if (entityType.getEntityCount() == 0) {
                json.put("text", entityType.getDisplayName());
                json.put("leaf", true);
            } else {
                json.put("text", entityType.getDisplayName() + " (" + String.valueOf(entityType.getEntityCount()) + ")");
                json.put("leaf", false);
            }
            json.put("type", "category");
            json.put("entityId", entityType.getTypeId());
            json.put("entityIp", "");
            array1.add(json);
        }

        JSONObject network = new JSONObject();
        network.put(
                "text",
                snmp ? resourceManager.getString("allSnmpEntity") + " (" + count + ")" : resourceManager
                        .getNotNullString("allEntity") + " (" + count + ")");
        network.put("leaf", false);
        network.put("id", "network");
        network.put("expanded", true);
        network.put("iconCls", "myEntityIcon");
        network.put("children", array1);
        array.add(network);
        return array;
    }

    public String getEntityBrowserTree() throws Exception {
        JSONArray array = null;
        try {
            if ("deviceRootNode".equals(node)) {
                array = getDeviceCategoryStat(false);
            } else if (node.startsWith("type")) {
                array = getDeviceByType(Long.parseLong(node.replace("type", "")));
            } else if (node.startsWith("entity")) {
                array = getPortByEntity(Long.parseLong(node.replace("entity", "")));
            }
        } catch (Exception ex) {
            logger.error("show Entity Browser Tree.", ex);
            throw ex;
        } finally {
            writeDataToAjax(array);
        }
        return NONE;
    }

    public EntityService getEntityService() {
        return entityService;
    }

    public String getEntityTree() throws Exception {
        JSONArray array = null;
        try {
            if ("deviceRootNode".equals(node)) {
                array = getDeviceCategory(false);
            } else if (node.startsWith("type")) {
                array = getDeviceByType1(Long.parseLong(node.replace("type", "")), true);
            }
        } catch (Exception ex) {
            logger.error("show Entity Tree.", ex);
            throw ex;
        } finally {
            writeDataToAjax(array);
        }
        return NONE;
    }

    public String getNode() {
        return node;
    }

    private JSONArray getPortByEntity(long entityId) throws Exception {
        JSONArray array = new JSONArray();
        JSONObject json = null;
        Port port = null;
        List<Port> ports = portService.getPortByEntity(entityId);
        int size = ports == null ? 0 : ports.size();
        for (int i = 0; i < size; i++) {
            port = ports.get(i);
            json = new JSONObject();
            json.put("id", "port" + port.getPortId());
            json.put("iconCls", "portIcon");
            json.put("leaf", true);
            json.put("text", port.getIfName());
            json.put("type", "port");
            json.put("entityId", port.getPortId());
            json.put("entityIp", "");
            json.put("ifIndex", port.getIfIndex());
            array.add(json);
        }
        return array;
    }

    private JSONArray getSnmpDeviceByType(long typeId) throws Exception {
        JSONArray array = new JSONArray();
        JSONObject json = null;
        Entity entity = null;
        List<Entity> list = entityService.getSnmpEntityByType(typeId);
        int size = list.size();
        for (int i = 0; i < size; i++) {
            entity = list.get(i);
            json = new JSONObject();
            json.put("id", "entity" + entity.getEntityId());
            json.put("iconCls", "entityIcon");
            json.put("leaf", false);
            json.put("type", "entity");
            json.put("entityId", entity.getEntityId());
            json.put("entityIp", entity.getIp());
            json.put("snmpSupport", entity.isSnmpSupport());
            json.put("text", entity.getDisplayText());
            array.add(json);
        }
        return array;
    }

    public String getSnmpEntityBrowserTree() throws Exception {
        JSONArray array = null;
        try {
            if ("deviceRootNode".equals(node)) {
                array = getDeviceCategoryStat(true);
            } else if (node.startsWith("type")) {
                array = getSnmpDeviceByType(Long.parseLong(node.replace("type", "")));
            } else if (node.startsWith("entity")) {
                array = getPortByEntity(Long.parseLong(node.replace("entity", "")));
            }
        } catch (Exception ex) {
            logger.error("show Entity Browser Tree.", ex);
            throw ex;
        } finally {
            writeDataToAjax(array);
        }
        return NONE;
    }

    public String getSnmpEntityTree() throws Exception {
        JSONArray array = null;
        try {
            if ("deviceRootNode".equals(node)) {
                array = getDeviceCategory(true);
            } else if (node.startsWith("type")) {
                array = getDeviceByType1(Long.parseLong(node.replace("type", "")), true);
            }
        } catch (Exception ex) {
            logger.error("show Entity Browser Tree.", ex);
            throw ex;
        } finally {
            writeDataToAjax(array);
        }
        return NONE;
    }

    public boolean isAgentInstalled() {
        return agentInstalled;
    }

    public boolean isSnmpSupport() {
        return snmpSupport;
    }

    public void setAgentInstalled(boolean agentInstalled) {
        this.agentInstalled = agentInstalled;
    }

    public void setEntityService(EntityService entityService) {
        this.entityService = entityService;
    }

    public void setNode(String node) {
        this.node = node;
    }

    public void setPortService(PortService portService) {
        this.portService = portService;
    }

    public void setResourcesService(ResourcesService resourcesService) {
        this.resourcesService = resourcesService;
    }

    public void setSnmpSupport(boolean snmpSupport) {
        this.snmpSupport = snmpSupport;
    }

}
