package com.topvision.ems.network.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.List;

import net.sf.json.JSONObject;

import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.network.service.EntityService;
import com.topvision.ems.template.service.EntityTypeService;
import com.topvision.framework.common.MacUtils;
import com.topvision.framework.web.struts2.BaseAction;
import com.topvision.platform.domain.UserContext;
import com.topvision.platform.service.UserPreferencesService;

@Controller("networkSearchAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class NetworkSearchAction extends BaseAction {
    private static final long serialVersionUID = -8487242544961015303L;
    private String query;
    private List<Entity> entities;
    private String q;
    private int limit;
    private boolean include8800B;
    @Autowired
    private EntityService entityService;
    @Autowired
    private EntityTypeService entityTypeService;
    @Autowired
    private UserPreferencesService userPreferencesService;

    /**
     * Add bt @bravin:局端OLT设备选择专用,CCMTS,EPON,CMTS，AP等都可能会用到
     * @return
     * @throws IOException 
     */
    public String queryAllEntityList() throws IOException {
        JSONObject json = new JSONObject();
        List<Entity> entities = null;
        if (!include8800B) {
            entities = entityService.getEntityByType(entityTypeService.getOltType());
        } else {
            entities = entityService.getEntityByType(entityTypeService.getEntitywithipType());
        }
        UserContext uc = (UserContext) getSession().get(UserContext.KEY);
        //add by fanzidong,需要在展示前格式化MAC地址
        String macRule = uc.getMacDisplayStyle();
        for (Entity entity : entities) {
            String formatedMac = MacUtils.convertMacToDisplayFormat(entity.getMac(), macRule);
            entity.setMac(formatedMac);
        }
        json.put("list", entities);
        json.put("displayField", uc.getPreference("user.displayField"));
        json.write(response.getWriter());
        return NONE;
    }

    public String search() throws UnsupportedEncodingException {
        query = new String(query.getBytes("ISO-8859-1"), "UTF-8");
        return SUCCESS;
    }

    public String searchForAutoComplete() throws IOException {
        entities = entityService.queryEntityForAc("" + q + "%");
        if (entities != null) {
            PrintWriter writer;
            writer = ServletActionContext.getResponse().getWriter();
            for (int i = 0; i < entities.size(); i++) {
                writer.println(entities.get(i).getName());
            }
        }
        return NONE;
    }

    public void setEntities(List<Entity> entities) {
        this.entities = entities;
    }

    public void setEntityService(EntityService entityService) {
        this.entityService = entityService;
    }

    @Override
    public void setLimit(int limit) {
        this.limit = limit;
    }

    public void setQ(String q) {
        this.q = q;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public List<Entity> getEntities() {
        return entities;
    }

    @Override
    public int getLimit() {
        return limit;
    }

    public String getQ() {
        return q;
    }

    public String getQuery() {
        return query;
    }

    public boolean isInclude8800B() {
        return include8800B;
    }

    public void setInclude8800B(boolean include8800b) {
        include8800B = include8800b;
    }

}
