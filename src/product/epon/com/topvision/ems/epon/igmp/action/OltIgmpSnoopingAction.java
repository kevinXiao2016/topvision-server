/***********************************************************************
 * $Id: OltIgmpSnoopingAction.java,v1.0 2013年10月28日 上午9:32:16 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.igmp.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.topvision.ems.epon.domain.IgmpForwardingSnooping;
import com.topvision.ems.epon.igmp.service.OltIgmpService;
import com.topvision.ems.epon.olt.action.AbstractEponAction;
import com.topvision.framework.common.DateUtils;
import com.topvision.platform.domain.UserContext;

/**
 * @author Bravin
 * @created @2013年10月28日-上午9:32:16
 *
 */
@Controller("oltIgmpSnoopingAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class OltIgmpSnoopingAction extends AbstractEponAction {
    private static final long serialVersionUID = -7452343339711780624L;
    @Autowired
    private OltIgmpService oltIgmpService;
    private ArrayList<Integer> vidList;
    private ArrayList<Long> portList;
    private Long portIndex;
    private Integer vid;

    /**
     * 跳转到igmp snooping页面
     * 
     * @return
     */
    public String showIgmpSnooping() {
        List<IgmpForwardingSnooping> list = oltIgmpService.loadIgmpSnoopingData(entityId, portIndex, vid);
        vidList = new ArrayList<Integer>();
        portList = new ArrayList<Long>();
        for (IgmpForwardingSnooping snooping : list) {
            if (!vidList.contains(snooping.getVid())) {
                vidList.add(snooping.getVid());
            }
            if (!portList.contains(snooping.getPortIndex())) {
                portList.add(snooping.getPortIndex());
            }
        }
        return SUCCESS;
    }

    /**
     * 加载igmp snooping信息
     * 
     * @return
     * @throws IOException
     */
    public String loadIgmpSnoopingData() throws IOException {
        JSONArray array = new JSONArray();
        Map<Integer, List<String>> map = new HashMap<Integer, List<String>>();
        List<IgmpForwardingSnooping> list = oltIgmpService.loadIgmpSnoopingData(entityId, portIndex, vid);
        Date time = null;
        for (IgmpForwardingSnooping snooping : list) {
            List<String> portList = null;
            if (map.containsKey(snooping.getVid())) {
                portList = map.get(snooping.getVid());
            } else {
                List<String> ports = portList = new ArrayList<String>();
                map.put(snooping.getVid(), ports);
            }
            if (time == null) {
                time = snooping.getLastChangeTime();
            }
            portList.add(snooping.getPortString());
        }
        Set<Integer> keyset = map.keySet();
        Iterator<Integer> it = keyset.iterator();
        UserContext uc = (UserContext) getSession().get(UserContext.KEY);
        while (it.hasNext()) {
            Integer v = it.next();
            JSONObject json = new JSONObject();
            json.put("firstRow", v);
            json.put("secondRow", map.get(v).toArray());
            json.put("lastChangeTime", DateUtils.getTimeDesInObscure(time.getTime(), uc.getUser().getLanguage()));
            array.add(json);
        }
        writeDataToAjax(array);
        return NONE;
    }

    /**
     * 从设备获取igmp Snooping信息
     * 
     * @return
     */
    public String refreshIgmpSnooping() {
        oltIgmpService.refreshIgmpSnooping(entityId);
        return NONE;
    }

    public OltIgmpService getOltIgmpService() {
        return oltIgmpService;
    }

    public void setOltIgmpService(OltIgmpService oltIgmpService) {
        this.oltIgmpService = oltIgmpService;
    }

    public ArrayList<Integer> getVidList() {
        return vidList;
    }

    public void setVidList(ArrayList<Integer> vidList) {
        this.vidList = vidList;
    }

    public ArrayList<Long> getPortList() {
        return portList;
    }

    public void setPortList(ArrayList<Long> portList) {
        this.portList = portList;
    }

    public Long getPortIndex() {
        return portIndex;
    }

    public void setPortIndex(Long portIndex) {
        this.portIndex = portIndex;
    }

    public Integer getVid() {
        return vid;
    }

    public void setVid(Integer vid) {
        this.vid = vid;
    }

}
