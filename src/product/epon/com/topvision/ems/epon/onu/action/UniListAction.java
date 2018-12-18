/***********************************************************************
 * $Id: UniListAction.java,v1.0 2015年4月22日 上午9:33:08 $
 * 
 * @author: Administrator
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.onu.action;

import java.io.IOException;
import java.util.List;

import net.sf.json.JSONArray;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.topvision.ems.epon.onu.domain.UniPort;
import com.topvision.ems.epon.onu.service.OnuAssemblyService;
import com.topvision.framework.web.struts2.BaseAction;

/**
 * @author Administrator
 * @created @2015年4月22日-上午9:33:08
 *
 */
@Controller("uniListAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class UniListAction extends BaseAction {
    private static final long serialVersionUID = 6803735910754398773L;
    @Autowired
    private OnuAssemblyService onuAssemblyService;
    private Long entityId;
    private Long onuId;

    /**
     * 加载UNI列表
     * 
     * @return
     * @throws IOException
     */
    public String loadUniList() throws IOException {
        List<UniPort> list = onuAssemblyService.loadUniList(onuId);
        JSONArray.fromObject(list).write(response.getWriter());
        return NONE;
    }

    /**
     * 刷新ONU的UNI信息
     * 
     * @return
     */
    public String refreshOnuUniInfo() {
        onuAssemblyService.refreshOnuUniInfo(onuId);
        return NONE;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public Long getOnuId() {
        return onuId;
    }

    public void setOnuId(Long onuId) {
        this.onuId = onuId;
    }

}
