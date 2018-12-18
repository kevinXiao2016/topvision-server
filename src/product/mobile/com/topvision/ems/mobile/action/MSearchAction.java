/***********************************************************************
 * $Id: MSearchAction.java,v1.0 2014-6-21 下午2:06:32 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.mobile.action;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.topvision.ems.mobile.domain.Location;
import com.topvision.ems.mobile.service.MSearchService;
import com.topvision.framework.common.MacUtils;
import com.topvision.framework.web.struts2.BaseAction;

/**
 * @author jay
 * @created @2014-6-21-下午2:06:32
 * 
 */
@Controller("mSearchAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class MSearchAction extends BaseAction {
    private static final long serialVersionUID = 1L;
    protected Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private MSearchService mSearchService;
    private String cmtsMac;
    private String cmtsName;
    private String cmMac;

    /**
     * 根据CMTS的MAC地址搜索
     * 
     * @return
     * @throws IOException
     */
    public String queryByCmtsMac() throws IOException {
        Map<String, String> map = new HashMap<String, String>();
        map.put("start", String.valueOf(start));
        map.put("limit", String.valueOf(limit));
        String formatQueryMac = MacUtils.formatQueryMac(cmtsMac);
        map.put("cmtsMac", formatQueryMac);
        List<Location> locations = mSearchService.queryByCmtsMac(map);
        Long totalCount = mSearchService.queryCountByCmtsMac(map);
        JSONObject json = new JSONObject();
        json.put("totalCount", totalCount);
        json.put("data", locations);
        json.write(response.getWriter());
        return NONE;
    }

    /**
     * 根据CMTS的别名搜索
     * 
     * @return
     * @throws IOException
     */
    public String queryByCmtsName() throws IOException {
        Map<String, String> map = new HashMap<String, String>();
        map.put("start", String.valueOf(start));
        map.put("limit", String.valueOf(limit));
        map.put("cmtsName", cmtsName);
        List<Location> locations = mSearchService.queryByCmtsName(map);
        Long totalCount = mSearchService.queryCountByCmtsName(map);
        JSONObject json = new JSONObject();
        json.put("totalCount", totalCount);
        json.put("data", locations);
        json.write(response.getWriter());
        return NONE;
    }

    /**
     * 根据CMTS下联CM的MAC地址搜索CMTS
     * 
     * @return
     * @throws IOException
     */
    public String queryByCmMac() throws IOException {
        Map<String, String> map = new HashMap<String, String>();
        map.put("start", String.valueOf(start));
        map.put("limit", String.valueOf(limit));
        String formatQueryMac = MacUtils.formatQueryMac(cmMac);
        map.put("cmMac", formatQueryMac);
        List<Location> locations = mSearchService.queryByCmMac(map);
        Long totalCount = mSearchService.queryCountByCmMac(map);
        JSONObject json = new JSONObject();
        json.put("totalCount", totalCount);
        json.put("data", locations);
        json.write(response.getWriter());
        return NONE;
    }

    public String getCmtsMac() {
        return cmtsMac;
    }

    public void setCmtsMac(String cmtsMac) {
        this.cmtsMac = cmtsMac;
    }

    public String getCmtsName() {
        return cmtsName;
    }

    public void setCmtsName(String cmtsName) {
        this.cmtsName = cmtsName;
    }

    public String getCmMac() {
        return cmMac;
    }

    public void setCmMac(String cmMac) {
        this.cmMac = cmMac;
    }
}
