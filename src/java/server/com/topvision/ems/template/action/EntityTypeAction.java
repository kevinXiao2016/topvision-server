/**
 * 
 */
package com.topvision.ems.template.action;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.topvision.ems.facade.domain.EntityType;
import com.topvision.ems.template.domain.EntityCorp;
import com.topvision.ems.template.service.EntityTypeService;
import com.topvision.framework.web.struts2.BaseAction;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * @author niejun
 * 
 */
@Controller("entityTypeAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class EntityTypeAction extends BaseAction {
    private static final long serialVersionUID = -8416717315575449682L;
    private long corpId;
    private String match;
    private List<EntityCorp> entityCorps;
    private List<EntityType> entityTypes;
    private List<EntityType> entityCatories;
    @Autowired
    private EntityTypeService entityTypeService;
    private Long type;

    /**
     * 加载设备类型
     * 
     * @return
     */
    public String loadEntityType() {
        Map<Long, List<Long>> entityTypeMap = entityTypeService.getEntityTypeMap();
        Map<String, Object> map = new HashMap<String, Object>();
        Iterator<Long> iterator = entityTypeMap.keySet().iterator();
        while (iterator.hasNext()) {
            Long key = iterator.next();
            List<Long> list = entityTypeMap.get(key);
            map.put(key.toString(), list);
        }
        map.put("success", true);
        writeDataToAjax(JSONObject.fromObject(map));
        return NONE;
    }

    public String loadSubEntityType() {
        List<EntityType> entityTypes = entityTypeService.loadSubType(type);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("entityTypes", entityTypes);
        writeDataToAjax(JSONObject.fromObject(map));
        return NONE;
    }

    public String loadAllSubType() throws IOException {
        List<EntityType> entityTypes = entityTypeService.getLicenseGroupEntityTypes();
        JSONArray.fromObject(entityTypes).write(response.getWriter());
        return NONE;
    }

    public long getCorpId() {
        return corpId;
    }

    public List<EntityType> getEntityCatories() {
        return entityCatories;
    }

    public List<EntityCorp> getEntityCorps() {
        return entityCorps;
    }

    public List<EntityType> getEntityTypes() {
        return entityTypes;
    }

    public String getMatch() {
        return match;
    }

    public void setCorpId(long corpId) {
        this.corpId = corpId;
    }

    public void setEntityCatories(List<EntityType> entityCatories) {
        this.entityCatories = entityCatories;
    }

    public void setEntityCorps(List<EntityCorp> entityCorps) {
        this.entityCorps = entityCorps;
    }

    public void setEntityTypes(List<EntityType> entityTypes) {
        this.entityTypes = entityTypes;
    }

    public void setMatch(String match) {
        this.match = match;
    }

    public void setEntityTypeService(EntityTypeService entityTypeService) {
        this.entityTypeService = entityTypeService;
    }

    public Long getType() {
        return type;
    }

    public void setType(Long type) {
        this.type = type;
    }
}
