/***********************************************************************
 * $Id: GponUniFilterMacAction.java,v1.0 2016年12月24日 下午2:33:38 $
 * 
 * @author: jay
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.gpon.unifiltermac.action;

import com.topvision.ems.gpon.unifiltermac.facade.domain.GponUniFilterMac;
import com.topvision.ems.gpon.unifiltermac.service.GponUniFilterMacService;
import com.topvision.ems.network.service.EntityService;
import com.topvision.framework.web.struts2.BaseAction;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author jay
 * @created @2016年12月24日-下午2:33:38
 *
 */
@Controller("gponUniFilterMacAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class GponUniFilterMacAction extends BaseAction {
    private static final long serialVersionUID = -1640091142239631847L;
    @Autowired
    private EntityService entityService;
    @Autowired
    private GponUniFilterMacService gponUniFilterMacService;

    private Long onuId;
    private Long uniId;
    private String mac;
    private Integer uniNo;

    /**
     * 展开GPON MAC地址过滤
     *
     * @return
     */
    public String showGponUniFilterMac() {
        return SUCCESS;
    }
    /**
     * 加载GPON MAC地址过滤
     *
     * @return
     */
    public String loadGponUniFilterMacList() {
        Map<String,Object> re = new HashMap<>();
        List<GponUniFilterMac> gponUniFilterMacs = gponUniFilterMacService.loadGponUniFilterMacList(uniId);
        re.put("data",gponUniFilterMacs);
        re.put("rowCount",gponUniFilterMacs.size());
        writeDataToAjax(JSONObject.fromObject(re));
        return NONE;
    }

    /**
     * 添加GPON MAC地址过滤
     *
     * @return
     */
    public String addGponUniFilterMac() {
        Map<String,String> re = new HashMap<>();
        boolean success;
        try {
            success = gponUniFilterMacService.addGponUniFilterMac(uniId,mac);
        } catch (Exception e) {
            success = false;
            logger.debug("",e);
        }
        re.put("success","" + success);
        writeDataToAjax(JSONObject.fromObject(re));
        return NONE;
    }

    /**
     * 删除GPON MAC地址过滤
     *
     * @return
     */
    public String deleteGponUniFilterMac() {
        gponUniFilterMacService.deleteGponUniFilterMac(uniId, mac);
        return NONE;
    }
    /**
     * 刷新GPON MAC地址过滤
     *
     * @return
     */
    public String refreshGponUniFilterMac() {
        gponUniFilterMacService.refreshGponUniFilterMac(uniId);
        return NONE;
    }

    public Long getUniId() {
        return uniId;
    }

    public void setUniId(Long uniId) {
        this.uniId = uniId;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }
	public Long getOnuId() {
		return onuId;
	}
	public void setOnuId(Long onuId) {
		this.onuId = onuId;
	}
	public Integer getUniNo() {
		return uniNo;
	}
	public void setUniNo(Integer uniNo) {
		this.uniNo = uniNo;
	}
    
}
