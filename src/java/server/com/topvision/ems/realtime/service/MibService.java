/***********************************************************************
 * $Id: MibService.java,v 1.1 2009-10-5 上午12:17:08 kelers Exp $
 * 
 * @author: kelers
 * 
 * (c)Copyright 2011 Topoview All rights reserved.
 ***********************************************************************/
package com.topvision.ems.realtime.service;

import java.util.List;

import com.topvision.ems.facade.domain.Entity;
import com.topvision.framework.service.Service;
import com.topvision.platform.domain.Attribute;

import net.sf.json.JSONObject;

/**
 * @Create Date 2009-10-5 上午12:17:08
 * 
 * @author kelers
 * 
 */
public interface MibService extends Service {
    /**
     * 获取mib数据
     * 
     * @param entityId
     * @param clazz
     *            实现类名
     * @return
     */
    JSONObject getData(long entityId, String clazz);

    /**
     * 获取mib表头
     * 
     * @param clazz
     *            实现类名
     * @return
     */
    String[] getHeaders(String clazz);

    /**
     * 根据entity类型返回不同类型的mib信息列表
     * 
     * @return 返回所有mib信息表。Attribute<显示名称，实现类名>
     */
    List<Attribute> getTypes(Entity entity);
}
