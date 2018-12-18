/***********************************************************************
 * $Id: ColumnModelAction.java,v1.0 2015-4-14 下午2:16:54 $
 * 
 * @author: Fanzidong
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.platform.action;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.topvision.framework.web.struts2.BaseAction;
import com.topvision.platform.domain.UserContext;
import com.topvision.platform.domain.UserPreferences;
import com.topvision.platform.service.UserPreferencesService;

import net.sf.json.JSONObject;

/**
 * @author fanzidong
 * @created @2015-4-14-下午2:16:54
 * 
 */
@Controller("columnModelAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ColumnModelAction extends BaseAction {

    private static final long serialVersionUID = -5327874432956176908L;
    
    public static final String CM = "cm";
    public static final String SORTINFO = "sortInfo";

    @Autowired
    private UserPreferencesService userPreferencesService;

    private String id; // customColumnModel唯一标识, id_cm表示columnmodel, id_sort表示对应排序信息
    private String columns;
    private String sortInfo;

    /**
     * 保存列配置
     * 
     * @return
     */
    public String saveCustomColumns() {
        UserContext uc = (UserContext) super.getSession().get(UserContext.KEY);
        //保存该用户对应的columnmodel信息
        userPreferencesService.saveModulePreferences(getCmId(id), "core", columns, uc);
        return NONE;
    }
    
    public String saveCustomSortInfo(){
    	 UserContext uc = (UserContext) super.getSession().get(UserContext.KEY);
         //保存该用户对应的sort信息
         userPreferencesService.saveModulePreferences(getSortId(id), "core", sortInfo, uc);
         return NONE;
    }

    /**
     * 获得列配置
     * 
     * @return
     * @throws IOException
     */
    public String getColumnModelConfig() throws IOException {
    	JSONObject json = new JSONObject();
        UserContext uc = (UserContext) super.getSession().get(UserContext.KEY);
        
        UserPreferences cm_up = userPreferencesService.getUserPreference(uc.getUserId(), getCmId(id));
        String columns = cm_up == null ? null : cm_up.getValue();
        
        UserPreferences sort_up = userPreferencesService.getUserPreference(uc.getUserId(), getSortId(id));
        String sort = sort_up == null ? null : sort_up.getValue();
        
        json.put("columns", columns);
        json.put(SORTINFO, sort);
        json.write(response.getWriter());
        return NONE;
    }
    
    private String getCmId(String id){
    	return id+"_" + CM;
    }
    
    private String getSortId(String id){
    	return id+"_" + SORTINFO;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getColumns() {
        return columns;
    }

    public void setColumns(String columns) {
        this.columns = columns;
    }

	public String getSortInfo() {
		return sortInfo;
	}

	public void setSortInfo(String sortInfo) {
		this.sortInfo = sortInfo;
	}
    
    

}
