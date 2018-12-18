/**
 * 
 */
package com.topvision.ems.portal.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.struts2.ServletActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.fault.domain.AlertStat;
import com.topvision.ems.fault.domain.LevelStat;
import com.topvision.ems.fault.service.AlertService;
import com.topvision.ems.network.service.EntityService;
import com.topvision.framework.web.struts2.BaseAction;
import com.topvision.framework.web.util.HttpResponseUtils;
import com.topvision.platform.ResourceManager;
import com.topvision.platform.domain.UserContext;

/**
 * @author niejun
 * 
 */
@Controller("alertPortalAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class AlertPortalAction extends BaseAction {
    private static final long serialVersionUID = -4469366964659807338L;
    private static Logger logger = LoggerFactory.getLogger(AlertPortalAction.class);
    @Autowired
    private AlertService alertService;
    @Autowired
    private EntityService entityService;
    private String startTime;
    private String endTime;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private String nmName;
    private Long deviceType;

    /**
     * 跳转到告警统计分布图界面
     * @return
     * @throws Exception
     */
    public String getAlertDistGraph() throws Exception {
        return SUCCESS;
    }

    /**
     * 加载告警分布图数据
     * @return
     * @throws IOException 
     */
    public String loadAlertDistGraph() throws IOException {

        Map<String, String> map = new HashMap<String, String>();
        // 开始时间
        if (startTime != null && !startTime.equals("")) {
            map.put("startTime", startTime);
        }
        // 结束时间
        if (endTime != null && !endTime.equals("")) {
            map.put("endTime", endTime);
        }
        List<LevelStat> stats = alertService.statAlertByLevel(map);
        ResourceManager resourceManager = ResourceManager.getResourceManager("com.topvision.ems.resources.resources");
        JSONArray alertStateList = new JSONArray();
        for (LevelStat st : stats) {
            JSONObject o = new JSONObject();
            o.put("name", resourceManager.getString(st.getName()));
            o.put("y", st.getCount());
            o.put("levelId", st.getLevelId());
            alertStateList.add(o);
        }
        if (alertStateList.size() > 0) {
            JSONObject o = (JSONObject) alertStateList.get(0);
            o.put("sliced", true);
            o.put("selected", true);
        }
        alertStateList.write(response.getWriter());
        return NONE;
    }

    public String getDeviceAlertTop() throws Exception {
        UserContext uc = (UserContext) ServletActionContext.getRequest().getSession().getAttribute("UserContext");
        ResourceManager resourceManager = ResourceManager.getResourceManager("com.topvision.ems.resources.resources");
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("start", 0);
        paramMap.put("limit", uc.getTopNumber());
        StringBuilder sb = new StringBuilder("");
        try {
            sb.append("<table class='dataTable zebra' width='100%' border='1' cellspacing='0' cellpadding='0' bordercolor='#DFDFDF' rules='all'><thead><tr>");
            sb.append("<th width='36'>");
            sb.append(resourceManager.getString("WorkBench.Ranking"));
            sb.append("</th><th>");
            sb.append(resourceManager.getString("Config.oltConfigFileImported.deviceName"));
            sb.append("</th><th>");
            sb.append(resourceManager.getString("COMMON.uplinkDevice"));
            sb.append("</th><th><img src=\"../images/desc.gif\" border=0 align=absmiddle hspace=5>");
            sb.append(resourceManager.getString("WorkBench.AlertNum"));
            sb.append("</th></tr></thead><tobdy>");

            // 开始时间
            if (startTime != null && !startTime.equals("")) {
                paramMap.put("startTime", startTime);
            }
            // 结束时间
            if (endTime != null && !endTime.equals("")) {
                paramMap.put("endTime", sdf.format(new Date(System.currentTimeMillis())));
            }
            List<AlertStat> stats = alertService.statAlertByEntity(paramMap);
            int i = 0;
            for (AlertStat stat : stats) {
                Entity entityForIp = entityService.getEntityByIp(stat.getHost());
                if (i >= 10) {
                    sb.append("<tr><td><div class='topClsBg'>");
                    if (i >= 100) {
                        sb.append("<div>");
                        sb.append(++i);
                        sb.append("</div>");
                    } else {
                        sb.append(++i);
                    }
                    sb.append("</div></td>");
                } else {
                    sb.append("<tr><Td><div class=topCls");
                    sb.append(++i);
                    sb.append("></div></Td>");
                }
                sb.append("<td class='txtLeft wordBreak'><a class=my-link href=\"javascript: viewAlertByName('");
                sb.append(stat.getEntityName());
                sb.append("','");
                sb.append(stat.getEntityId());
                sb.append("','");
                sb.append(stat.getEntityType());
                sb.append("')\">");
                sb.append(stat.getEntityName());
                sb.append("</td><td align='center'><a class=my-link href=\"javascript: viewAlertByIp('");
                if (entityForIp != null) {
                    sb.append(entityForIp.getName());
                    sb.append("','");
                    sb.append(entityForIp.getIp());
                    sb.append("',");
                    sb.append(entityForIp.getEntityId());
                    sb.append(",");
                    sb.append(entityForIp.getTypeId());
                } else {
                    sb.append("N/A");
                    sb.append("','");
                    sb.append("N/A");
                    sb.append("',");
                    sb.append("N/A");
                    sb.append(",");
                    sb.append("N/A");
                }
                sb.append(")\">");
                sb.append(stat.getUplinkDevice());
                sb.append("</a></td>");
                sb.append("<td align='center'><a class=my-link href=\"javascript: viewAlertByAlert('");
                sb.append(stat.getEntityName());
                sb.append("','");
                sb.append(stat.getEntityId());
                sb.append("','");
                sb.append(stat.getEntityType());
                sb.append("')\">");
                sb.append(stat.getCount());
                sb.append("</td>");
                sb.append("</tr>");
            }
        } catch (Exception ex) {
            logger.error("Get Device Current Alert Top.", ex);
        } finally {
            sb.append("<tbody></table>");
            writeDataToAjax(sb.toString());
        }
        return NONE;
    }

    public String showDeviceAlertList() {
        return SUCCESS;
    }

    /**
     * 获取设备告警数
     * 
     * @return
     * @throws IOException 
     */
    public String loadDeviceAlertList() throws IOException {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        if (startTime != null && !startTime.equals("")) {
            paramMap.put("startTime", startTime);
        }
        if (endTime != null && !endTime.equals("")) {
            paramMap.put("endTime", sdf.format(new Date(System.currentTimeMillis())));
        }
        paramMap.put("start", start);
        paramMap.put("limit", limit);
        paramMap.put("sort", sort);
        paramMap.put("dir", dir);
        if (nmName != null && !"".equals(nmName)) {
            if (nmName.contains("_")) {
                nmName = nmName.replace("_", "\\_");
            }
        }
        paramMap.put("name", nmName);
        paramMap.put("deviceType", deviceType);
        List<AlertStat> stats = alertService.statAlertListByEntity(paramMap);
        int totalNum = alertService.statAlertCountByEntity(paramMap);
        JSONObject json = new JSONObject();
        json.put("rowCount", totalNum);
        json.put("data", stats);
        json.write(response.getWriter());
        return NONE;
    }

    public void setAlertService(AlertService alertService) {
        this.alertService = alertService;
    }

    /**
     * key：properties文件的keymodule：资源文件
     */
    protected String getResourceString(String key, String module) {
        try {
            ResourceManager resourceManager = ResourceManager.getResourceManager(module);
            return resourceManager.getNotNullString(key);
        } catch (Exception e) {
            return key;
        }
    }

    public AlertService getAlertService() {
        return alertService;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getNmName() {
        return nmName;
    }

    public void setNmName(String nmName) {
        this.nmName = nmName;
    }

    public Long getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(Long deviceType) {
        this.deviceType = deviceType;
    }

}
