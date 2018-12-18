/**
 * 
 */
package com.topvision.ems.resources.action;

import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.struts2.ServletActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.facade.domain.EntityType;
import com.topvision.ems.network.domain.TopoFolder;
import com.topvision.ems.network.service.EntityService;
import com.topvision.ems.network.service.TopologyService;
import com.topvision.ems.template.service.EntityTypeService;
import com.topvision.framework.domain.PageData;
import com.topvision.framework.web.struts2.BaseAction;
import com.topvision.framework.web.util.HttpResponseUtils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * @author niejun
 * 
 */
@Controller("entitySearchAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class EntitySearchAction extends BaseAction {
    private static final long serialVersionUID = 4627359178083096679L;
    private static Logger logger = LoggerFactory.getLogger(EntitySearchAction.class);
    @Autowired
    private EntityService entityService;
    @Autowired
    private EntityTypeService entityTypeService;
    @Autowired
    private TopologyService topologyService;
    private List<TopoFolder> topoFolders;
    private List<EntityType> entityTypes;
    private long folderId;
    private long type;
    private String name;
    private String ip;

    public String loadAllEntityForCombox() throws Exception {
        HttpResponseUtils.setXMLResponse(ServletActionContext.getResponse());
        Writer writer = ServletActionContext.getResponse().getWriter();
        List<Entity> entities = entityService.queryEntityByFolderIdAndType(folderId, type, false);
        if (entities != null) {
            int size = entities.size();
            Entity entity = null;
            writer.write("<?xml version=\"1.0\" ?><complete>");
            for (int i = 0; i < size; i++) {
                entity = entities.get(i);
                writer.write("<option value=\"");
                writer.write(String.valueOf(entity.getEntityId()));
                writer.write("\">");
                if (entity.getIp().equals(entity.getName())) {
                    writer.write(entity.getIp());
                } else {
                    writer.write(entity.getIp());
                    writer.write(" [");
                    writer.write(entity.getName());
                    writer.write("]");
                }
                writer.write("</option>");
            }
            writer.write("</complete>");
            writer.flush();
        }
        return NONE;
    }

    @SuppressWarnings("unchecked")
    public String queryEntity() throws Exception {
        JSONObject json = new JSONObject();
        @SuppressWarnings("rawtypes")
        Map map = new HashMap();
        if (folderId > 0) {
            map.put("folderId", folderId);
        }
        if (type > 0) {
            map.put("typeId", type);
        }
        if (name != null && !"".equals(name)) {
            map.put("name", "%" + name + "%");
        }
        if (ip != null && !"".equals(ip)) {
            map.put("ip", "%" + ip + "%");
        }
        try {
            @SuppressWarnings("rawtypes")
            PageData pageData = entityService.queryEntity(map, getExtPage());
            int size = 0;
            JSONArray array = new JSONArray();
            if (pageData == null) {
                json.put("rowCount", 0);
            } else {
                json.put("rowCount", pageData.getRowCount());
                size = pageData.getData().size();
                List<Entity> list = pageData.getData();
                JSONObject temp = null;
                Entity entity = null;
                for (int i = 0; i < size; i++) {
                    entity = list.get(i);
                    temp = new JSONObject();
                    temp.put("entityId", entity.getEntityId());
                    temp.put("name", entity.getName());
                    temp.put("ip", entity.getIp());
                    temp.put("typeName", entity.getTypeName());
                    array.add(temp);
                }
            }
            json.put("data", array);
        } catch (Exception ex) {
            logger.error("Query Entity For report.", ex);
        } finally {
            writeDataToAjax(json);
        }
        return NONE;
    }

    public String showFindEntity() throws Exception {
        topoFolders = topologyService.loadTopoMap();
        return SUCCESS;
    }

    public String showPopEntityList() throws Exception {
        topoFolders = topologyService.loadTopoMap();
        Long oltType = entityTypeService.getOltType();
        entityTypes = entityTypeService.loadSubType(oltType);
        return SUCCESS;
    }

    public void setEntityService(EntityService entityService) {
        this.entityService = entityService;
    }

    public void setEntityTypes(List<EntityType> entityTypes) {
        this.entityTypes = entityTypes;
    }

    public void setEntityTypeService(EntityTypeService entityTypeService) {
        this.entityTypeService = entityTypeService;
    }

    public void setFolderId(long folderId) {
        this.folderId = folderId;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTopoFolders(List<TopoFolder> topoFolders) {
        this.topoFolders = topoFolders;
    }

    public void setTopologyService(TopologyService topologyService) {
        this.topologyService = topologyService;
    }

    public void setType(long type) {
        this.type = type;
    }

    public List<EntityType> getEntityTypes() {
        return entityTypes;
    }

    public long getFolderId() {
        return folderId;
    }

    public String getIp() {
        return ip;
    }

    public String getName() {
        return name;
    }

    public List<TopoFolder> getTopoFolders() {
        return topoFolders;
    }

    public long getType() {
        return type;
    }
}
