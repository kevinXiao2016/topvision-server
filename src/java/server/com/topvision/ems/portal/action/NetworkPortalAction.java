/**
 * 
 */
package com.topvision.ems.portal.action;

import java.io.IOException;
import java.io.Writer;
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

import com.topvision.ems.fault.domain.Alert;
import com.topvision.ems.fault.service.AlertService;
import com.topvision.ems.network.domain.EntitySnap;
import com.topvision.ems.network.service.EntityService;
import com.topvision.ems.network.service.NetworkService;
import com.topvision.ems.resources.domain.EntityStastic;
import com.topvision.ems.resources.service.ResourcesService;
import com.topvision.framework.common.DateUtils;
import com.topvision.framework.common.MacUtils;
import com.topvision.framework.common.NumberUtils;
import com.topvision.framework.web.struts2.BaseAction;
import com.topvision.framework.web.util.HttpResponseUtils;
import com.topvision.platform.ResourceManager;
import com.topvision.platform.domain.UserContext;

/**
 * @author niejun
 * 
 */
@Controller("networkPortalAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class NetworkPortalAction extends BaseAction {
    private static final long serialVersionUID = -8106243312441272274L;
    private static Logger logger = LoggerFactory.getLogger(NetworkPortalAction.class);
    @Autowired
    private NetworkService networkService;
    @Autowired
    private ResourcesService resourcesService;
    @Autowired
    private AlertService alertService;
    private long portletId;
    private String param;
    private static final String ICMP_TYPEID = "-301";
    private String nmName;
    private Long deviceType;

    public void setEntityService(EntityService entityService) {
    }

    public String getTopCpuLoading() throws Exception {
        ResourceManager resourceManager = ResourceManager.getResourceManager("com.topvision.ems.resources.resources");
        UserContext uc = (UserContext) ServletActionContext.getRequest().getSession().getAttribute("UserContext");
        //add by fanzidong, 需要在展示之前格式化MAC地址
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("start", 0);
        paramMap.put("limit", uc.getTopNumber());
        paramMap.put("target", "cpu");
        String macRule = uc.getMacDisplayStyle();
        StringBuilder sb = new StringBuilder();
        sb.append("<table class='dataTable zebra noWrap' width='100%' border='1' cellspacing='0' cellpadding='0' bordercolor='#DFDFDF' rules='all' id='getTopCpuLoading'><thead><tr><th width='36'>");
        sb.append(resourceManager.getString("WorkBench.Ranking"));
        sb.append("</th><th width='120'>");
        sb.append(resourceManager.getString("Config.oltConfigFileImported.deviceName"));
        sb.append("</th><Th width='100'>");
        sb.append(resourceManager.getString("COMMON.uplinkDevice"));
        sb.append("</th><Th width='70'>");
        sb.append(resourceManager.getString("MAIN.templateRootNode"));
        sb.append("</th><th><img src=\"../images/desc.gif\" border=0 align=absmiddle hspace=5>");
        sb.append(resourceManager.getString("WorkBench.CpuRanking"));
        sb.append("</th><th width='125'>");
        sb.append(resourceManager.getString("WorkBench.fromUpdateTime"));
        sb.append("</th></tr></thead><tbody>");
        try {
            List<EntitySnap> snaps = networkService.getNetworkDeviceLoadingTop(paramMap);
            if (snaps != null) {
                int i = 0;
                for (EntitySnap snap : snaps) {
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
                    sb.append("<Td class='txtLeft wordBreak'><a class=my-link href=\"javascript:showEntitySnap(");
                    sb.append(snap.getEntityId());
                    sb.append(",'");
                    sb.append(snap.getName());
                    sb.append("','");
                    // parentId(即cmcId)
                    if (snap.getParentId() != null) {
                        sb.append(snap.getEntityId());
                    }
                    sb.append("','");
                    // Mac
                    if (snap.getMac() != null) {
                        String mac = MacUtils.convertMacToDisplayFormat(snap.getMac(), macRule);
                        sb.append(mac);
                    }
                    sb.append("');\">");
                    sb.append(snap.getName());
                    sb.append("</a></Td>");
                    sb.append("<Td align='center'>");
                    sb.append(snap.getUplinkDevice());
                    sb.append("</Td>");
                    sb.append("<Td align=center>");
                    sb.append(snap.getTypeName());
                    sb.append("</Td>");
                    sb.append("<Td align=center>");
                    sb.append(NumberUtils.getPercentStr(snap.getCpu()));
                    sb.append("</Td><Td align=center>");
                    sb.append(DateUtils.getTimeDesInObscure(System.currentTimeMillis() - snap.getSnapTime().getTime(),
                            uc.getUser().getLanguage()));
                    sb.append("</Td></tr>");
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            sb.append("</tobdy></table>");
        }
        writeDataToAjax(sb.toString());
        return NONE;
    }

    public String showDeviceCpuJsp() {
        return SUCCESS;
    }

    public String loadDeviceCpuUsedList() throws Exception {
        Map<String, Object> paramMap = new HashMap<String, Object>();
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
        paramMap.put("target", "cpu");
        List<EntitySnap> responses = networkService.getNetworkDeviceLoadingList(paramMap);
        int totalNum = networkService.getNetworkDeviceCount(paramMap);
        JSONArray opticalArray = JSONArray.fromObject(responses);
        JSONObject opticalJson = new JSONObject();
        opticalJson.put("rowCount", totalNum);
        opticalJson.put("data", opticalArray);
        opticalJson.write(response.getWriter());
        return NONE;
    }

    public String getTopMemLoading() throws Exception {
        ResourceManager resourceManager = ResourceManager.getResourceManager("com.topvision.ems.resources.resources");
        UserContext uc = (UserContext) ServletActionContext.getRequest().getSession().getAttribute("UserContext");
        String macRule = uc.getMacDisplayStyle();
        StringBuilder sb = new StringBuilder();
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("start", 0);
        paramMap.put("limit", uc.getTopNumber());
        paramMap.put("target", "mem");

        sb.append("<table class='dataTable zebra noWrap' width='100%' border='1' cellspacing='0' cellpadding='0' bordercolor='#DFDFDF' rules='all' id='getTopMemLoading'><thead><tr><th width='36'>");
        sb.append(resourceManager.getString("WorkBench.Ranking"));
        sb.append("</th><th width='120'> ");
        sb.append(resourceManager.getString("Config.oltConfigFileImported.deviceName"));
        sb.append("</th><Th width='100'>");
        sb.append(resourceManager.getString("COMMON.uplinkDevice"));
        sb.append("</th><Th width='70'>");
        sb.append(resourceManager.getString("MAIN.templateRootNode"));
        sb.append("</th><th class='wordBreak'><img src=\"../images/desc.gif\" border=0 align=absmiddle hspace=5>");
        sb.append(resourceManager.getString("WorkBench.MemRanking"));
        sb.append("</th><th width='125'>");
        sb.append(resourceManager.getString("WorkBench.fromUpdateTime"));
        sb.append("</th></tr></thead><tbody>");
        try {
            List<EntitySnap> snaps = networkService.getNetworkDeviceLoadingTop(paramMap);
            if (snaps != null) {
                int i = 0;
                for (EntitySnap snap : snaps) {
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
                    sb.append("<Td class='txtLeft wordBreak'><a class=my-link href=\"javascript:showEntitySnap(");
                    sb.append(snap.getEntityId());
                    sb.append(",'");
                    sb.append(snap.getName());
                    sb.append("','");
                    // parentId(即cmcId)
                    if (snap.getParentId() != null) {
                        sb.append(snap.getEntityId());
                    }
                    sb.append("','");
                    // Mac
                    if (snap.getMac() != null) {
                        String mac = MacUtils.convertMacToDisplayFormat(snap.getMac(), macRule);
                        sb.append(mac);
                    }
                    sb.append("');\">");
                    sb.append(snap.getName());
                    sb.append("</a></Td>");
                    sb.append("<Td align='center'>");
                    sb.append(snap.getUplinkDevice());
                    sb.append("</Td>");
                    sb.append("<Td align=center>");
                    sb.append(snap.getTypeName());
                    sb.append("</Td>");
                    sb.append("<Td align='center'>");
                    sb.append(NumberUtils.getPercentStr(snap.getMem()));
                    sb.append("</Td><Td align='center'>");
                    sb.append(DateUtils.getTimeDesInObscure(System.currentTimeMillis() - snap.getSnapTime().getTime(),
                            uc.getUser().getLanguage()));
                    sb.append("</Td></tr>");
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            sb.append("</tbody></table>");
        }
        writeDataToAjax(sb.toString());
        return NONE;
    }

    public String showDeviceMemJsp() {
        return SUCCESS;
    }

    public String loadDeviceMemUsedList() throws Exception {
        Map<String, Object> paramMap = new HashMap<String, Object>();
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
        paramMap.put("target", "mem");
        List<EntitySnap> responses = networkService.getNetworkDeviceLoadingList(paramMap);
        int totalNum = networkService.getNetworkDeviceCount(paramMap);
        JSONArray opticalArray = JSONArray.fromObject(responses);
        JSONObject opticalJson = new JSONObject();
        opticalJson.put("rowCount", totalNum);
        opticalJson.put("data", opticalArray);
        opticalJson.write(response.getWriter());
        return NONE;
    }

    public String showDeviceDelayJsp() {
        return SUCCESS;
    }

    public String loadDeviceDelayList() throws Exception {
        Map<String, Object> paramMap = new HashMap<String, Object>();
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
        List<EntitySnap> responses = networkService.getDeviceDelayTop(paramMap);
        int totalNum = networkService.getDeviceDelayNum(paramMap);
        JSONArray opticalArray = JSONArray.fromObject(responses);
        JSONObject opticalJson = new JSONObject();
        opticalJson.put("rowCount", totalNum);
        opticalJson.put("data", opticalArray);
        opticalJson.write(response.getWriter());
        return NONE;
    }

    public String getDeviceDelayingTop() throws Exception {
        ResourceManager resourceManager = ResourceManager.getResourceManager("com.topvision.ems.resources.resources");
        UserContext uc = (UserContext) ServletActionContext.getRequest().getSession().getAttribute("UserContext");
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("start", 0);
        paramMap.put("limit", uc.getTopNumber());
        //add by fanzidong,在展示之前格式化MAC地址
        String macRule = uc.getMacDisplayStyle();
        StringBuilder sb = new StringBuilder();

        sb.append("<table id='getDeviceDelayingTop' class='dataTable zebra noWrap' width='100%' border='1' cellspacing='0' cellpadding='0' bordercolor='#DFDFDF' rules='all'><thead><tr><th width='36'>");
        sb.append(resourceManager.getString("WorkBench.Ranking"));
        sb.append("</th><th  width='120'>");
        sb.append(resourceManager.getString("Config.oltConfigFileImported.deviceName"));
        sb.append("</th><Th width='100'>");
        sb.append(resourceManager.getString("COMMON.manageIp"));
        sb.append("</th><Th width='70'>");
        sb.append(resourceManager.getString("MAIN.templateRootNode"));
        sb.append("</th><th><img src=\"../images/desc.gif\" border=0 align=absmiddle hspace=5>");
        sb.append(resourceManager.getString("WorkBench.delaying"));
        sb.append("</th><th width='125'>");
        sb.append(resourceManager.getString("WorkBench.fromUpdateTime"));
        sb.append("</th></tr></thead><tbody>");
        try {
            List<EntitySnap> responses = networkService.getDeviceDelayTop(paramMap);
            if (responses != null) {
                int i = 0;
                for (EntitySnap snap : responses) {
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
                    if (snap.getTypeId() != -1) {
                        sb.append("<Td class='txtLeft wordBreak'><a href=\"javascript:showEntitySnap(");
                        sb.append(snap.getEntityId());
                        sb.append(",'");
                        sb.append(snap.getName());
                        sb.append("','");
                        // parentId(即cmcId)
                        if (snap.getParentId() != null) {
                            sb.append(snap.getParentId());
                        }
                        sb.append("','");
                        // Mac
                        if (snap.getMac() != null) {
                            String mac = MacUtils.convertMacToDisplayFormat(snap.getMac(), macRule);
                            sb.append(mac);
                        }
                    } else {
                        sb.append("<Td class='txtLeft wordBreak'><a href=\"javascript:showUnKnownEntityJsp(");
                        sb.append(snap.getEntityId());
                        sb.append(",'");
                        sb.append(snap.getName());
                        sb.append("','");
                        // parentId(即cmcId)
                        if (snap.getIp() != null) {
                            sb.append(snap.getIp());
                        }
                    }
                    sb.append("');\">");
                    sb.append(snap.getName());
                    sb.append("</a></Td>");
                    sb.append("<Td class='txtLeft'>");
                    sb.append(snap.getIp());
                    sb.append("</Td>");
                    sb.append("<Td align=center>");
                    sb.append(snap.getTypeName());
                    sb.append("</Td>");
                    sb.append("</a></Td>");
                    sb.append("<Td align=center>");
                    if (snap.getDelay() == -1) {
                        sb.append(resourceManager.getString("WorkBench.pingOut") + "</Td>");
                    } else {
                        sb.append(snap.getDelay());
                        sb.append("ms</Td>");
                    }
                    sb.append("<Td align='center'>");
                    sb.append(DateUtils.getTimeDesInObscure(System.currentTimeMillis() - snap.getSnapTime().getTime(),
                            uc.getUser().getLanguage()));
                    sb.append("</Td>");
                    sb.append("</tr>");
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            sb.append("</tbody></table>");
        }
        writeDataToAjax(sb.toString());
        return NONE;
    }

    public String showDeviceDelayOutJsp() {
        return SUCCESS;
    }

    public String loadDeviceDelayOutList() throws Exception {
        Map<String, Object> paramMap = new HashMap<String, Object>();
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
        List<EntitySnap> responses = networkService.getDeviceDelayOut(paramMap);
        int totalNum = networkService.getDeviceDelayOutNum(paramMap);
        JSONArray opticalArray = JSONArray.fromObject(responses);
        JSONObject opticalJson = new JSONObject();
        opticalJson.put("total", totalNum);
        opticalJson.put("data", opticalArray);
        opticalJson.write(response.getWriter());
        return NONE;
    }

    public String showDeviceAttentionList() {
        return SUCCESS;
    }

    public String loadDeviceAttentionList() throws Exception {
        UserContext uc = (UserContext) ServletActionContext.getRequest().getSession().getAttribute("UserContext");
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("start", start);
        paramMap.put("limit", limit);
        paramMap.put("sort", sort);
        paramMap.put("dir", dir);
        paramMap.put("userId", uc.getUserId());
        if (nmName != null && !"".equals(nmName)) {
            if (nmName.contains("_")) {
                nmName = nmName.replace("_", "\\_");
            }
        }
        paramMap.put("name", nmName);
        paramMap.put("deviceType", deviceType);
        List<EntitySnap> responses = networkService.getDeviceAttentionList(paramMap);
        int totalNum = networkService.getDeviceAttentionCount(paramMap);
        JSONArray opticalArray = JSONArray.fromObject(responses);
        JSONObject opticalJson = new JSONObject();
        opticalJson.put("total", totalNum);
        opticalJson.put("data", opticalArray);
        opticalJson.write(response.getWriter());
        return NONE;
    }

    public String getDeviceDelayingOut() throws Exception {
        ResourceManager resourceManager = ResourceManager.getResourceManager("com.topvision.ems.resources.resources");
        UserContext uc = (UserContext) ServletActionContext.getRequest().getSession().getAttribute("UserContext");
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("start", 0);
        paramMap.put("limit", uc.getTopNumber());
        //add by fanzidong,在展示之前格式化MAC地址
        String macRule = uc.getMacDisplayStyle();
        StringBuilder sb = new StringBuilder();

        sb.append("<table id='getDeviceDelayingOut' class='dataTable zebra noWrap' width='100%' border='1' cellspacing='0' cellpadding='0' bordercolor='#DFDFDF' rules='all'><thead><tr><th width='36'>");
        sb.append(resourceManager.getString("WorkBench.Ranking"));
        sb.append("</th><th>");
        sb.append(resourceManager.getString("Config.oltConfigFileImported.deviceName"));
        sb.append("</th><Th width='100'>");
        sb.append(resourceManager.getString("COMMON.manageIp"));
        sb.append("</th><th width='60'>");
        sb.append(resourceManager.getString("WorkBench.deviceType"));
        sb.append("</th><th width='60'><img src=\"../images/moreup.gif\" border=0 align=absmiddle hspace=5>");
        sb.append(resourceManager.getString("WorkBench.delayOutTime"));
        sb.append("</th><th width='126'>");
        sb.append(resourceManager.getString("WorkBench.fromUpdateTime"));
        sb.append("</th></tr></thead><tbody>");
        try {
            List<EntitySnap> responses = networkService.getDeviceDelayOut(paramMap);
            if (responses != null) {
                int i = 0;
                for (EntitySnap snap : responses) {
                    Alert alert = alertService.getEntityAlertByType(snap.getEntityId(), ICMP_TYPEID);
                    if (alert != null) {
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
                        if (snap.getTypeId() != -1) {
                            sb.append("<Td class='txtLeft wordBreak'><a href=\"javascript:showEntitySnap(");
                            sb.append(snap.getEntityId());
                            sb.append(",'");
                            sb.append(snap.getName());
                            sb.append("','");
                            // parentId(即cmcId)
                            if (snap.getParentId() != null) {
                                sb.append(snap.getParentId());
                            }
                            sb.append("','");
                            // Mac
                            if (snap.getMac() != null) {
                                String mac = MacUtils.convertMacToDisplayFormat(snap.getMac(), macRule);
                                sb.append(mac);
                            }
                        } else {
                            sb.append("<Td class='txtLeft wordBreak'><a href=\"javascript:showUnKnownEntityJsp(");
                            sb.append(snap.getEntityId());
                            sb.append(",'");
                            sb.append(snap.getName());
                            sb.append("','");
                            // parentId(即cmcId)
                            if (snap.getIp() != null) {
                                sb.append(snap.getIp());
                            }
                        }
                        sb.append("');\">");
                        sb.append(snap.getName());
                        sb.append("</a></Td>");
                        sb.append("<Td class='txtLeft'>");
                        sb.append(snap.getIp());
                        sb.append("</Td>");
                        sb.append("</a></Td>");
                        sb.append("<Td align=center>");
                        sb.append(snap.getTypeName());
                        sb.append("</Td>");
                        sb.append("<Td align=center>");
                        sb.append(DateUtils.getTimeDesInObscure(System.currentTimeMillis()
                                - alert.getFirstTime().getTime(), uc.getUser().getLanguage()));
                        sb.append("</Td>");
                        sb.append("<Td align='center'>");
                        sb.append(DateUtils.getTimeDesInObscure(System.currentTimeMillis()
                                - alert.getLastTime().getTime(), uc.getUser().getLanguage()));
                        sb.append("</Td>");
                        sb.append("</tr>");
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            sb.append("</tbody></table>");
        }
        writeDataToAjax(sb.toString());
        return NONE;
    }

    /**
     * 跳转到 网络信息统计分布图 界面
     * @return
     * @throws Exception
     */
    public String getNetworkInfo() throws Exception {
        return SUCCESS;
    }

    /**
     * 跳转到 网络信息统计分布数据
     * @return
     * @throws IOException
     */
    public String loadNetworkInfoDist() throws IOException {
        JSONObject networkInfoStastic = new JSONObject();
        List<EntityStastic> entityStasticList = resourcesService.getEntityStasticByState();
        int[] onlineArray = new int[entityStasticList.size()];
        int[] offlineArray = new int[entityStasticList.size()];
        int[] entityCount = new int[entityStasticList.size()];
        String[] displayNamesArray = new String[entityStasticList.size()];
        int totalOffline = 0;
        int totalOnline = 0;
        int cursor = 0;
        for (EntityStastic stastic : entityStasticList) {
            /**
             * 存在count为 null的情况，这个问题在domain中已给默认值进行了处理
             */
            onlineArray[cursor] = stastic.getOnline();
            offlineArray[cursor] = stastic.getOffline();
            totalOffline += stastic.getOffline();
            totalOnline += stastic.getOnline();
            entityCount[cursor] = stastic.getOffline() + stastic.getOnline();
            displayNamesArray[cursor] = stastic.getDisplayName();
            cursor++;
        }

        networkInfoStastic.put("offline", totalOffline);
        networkInfoStastic.put("online", totalOnline);
        networkInfoStastic.put("onlineArray", onlineArray);
        networkInfoStastic.put("offlineArray", offlineArray);
        networkInfoStastic.put("entityCount", entityCount);
        networkInfoStastic.put("displayNameArray", displayNamesArray);
        networkInfoStastic.write(response.getWriter());
        return NONE;
    }

    public String getImportantEntity() throws Exception {
        StringBuilder sb = new StringBuilder();
        try {
            sb.append("<table><tr><td width=140>")
                    .append(getResourceString("Config.oltConfigFileImported.deviceName",
                            "com.topvision.ems.resources.resources")).append("</td><td width=120>")
                    .append(getResourceString("COMMON.ip", "com.topvision.ems.resources.resources"))
                    .append("</td><td width=60>")
                    .append(getResourceString("EVENT.statusHeader", "com.topvision.ems.resources.resources"))
                    .append("</td></tr>");
            sb.append("<tr><td><a href=\"#\" onclick=\"\">OK</a></td><td>127.0.0.1</td><td>OK</td></tr>");
            sb.append("<tr><td><a href=\"#\" onclick=\"\">OK</a></td><td>127.0.0.1</td><td>OK</td></tr>");
            sb.append("</table><br>");
            sb.append(
                    "<input id=ipInput type=text class=iptxt style=\"color:gray\" onfocus=\"focusImportantIp(this)\" onblur=\"blurImportantIp(this)\" onkeyup=\"keydownImportantIp(this)\" style=\"width:250px\" value=\"")
                    .append(getResourceString("NetworkPortalAction.note", "com.topvision.ems.resources.resources"))
                    .append("\">&nbsp;&nbsp;<button class=BUTTON75 onMouseOver=\"this.className='BUTTON_OVER75'\" onMouseDown=\"this.className='BUTTON_PRESSED75'\" onMouseOut=\"this.className='BUTTON75'\" onclick=\"addImportantEntity()\">")
                    .append(getResourceString("COMMON.add", "com.topvision.ems.resources.resources"))
                    .append("</button>");
        } catch (Exception ex) {
            logger.error("Get ImportantEntity.", ex);
        } finally {
            writeDataToAjax(sb.toString());
        }
        return NONE;
    }

    public String getEntityTopology() throws Exception {
        StringBuilder sb = new StringBuilder();
        try {
            sb.append("<div align=center><div><a href=\"#\" onclick=\"showTopoMap()\">")
                    .append(getResourceString("WorkBench.networkTopo", "com.topvision.ems.resources.resources"))
                    .append("</a></div><br><a href=\"#\" onclick=\"showTopoMap()\"><img width=400 height=300 src=\"../network/saveTopoAsPicture.tv?folderId=2\"></a>");
            sb.append("</div>");
        } catch (Exception ex) {
            logger.error("Get Entity Topology.", ex);
        } finally {
            writeDataToAjax(sb.toString());
        }
        return NONE;
    }

    /**
     * 将毫秒数转换为x天x小时x分x秒形式
     * @param time
     * @return
     */
    @SuppressWarnings("unused")
    private String formatDuring(long time) {
        StringBuilder duringTime = new StringBuilder();
        long days = time / (1000 * 60 * 60 * 24);
        long hours = (time % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
        long minutes = (time % (1000 * 60 * 60)) / (1000 * 60);
        long seconds = (time % (1000 * 60)) / 1000;
        if (days > 0) {
            duringTime.append(days + getResourceString("label.day", "com.topvision.ems.network.resources"));
        }
        if (hours > 0) {
            duringTime.append(hours + getResourceString("label.hours", "com.topvision.ems.network.resources"));
        }
        if (minutes > 0) {
            duringTime.append(minutes + getResourceString("label.minutes", "com.topvision.ems.network.resources"));
        }
        if (seconds > 0) {
            duringTime.append(seconds + getResourceString("label.seconds", "com.topvision.ems.network.resources"));
        }
        return duringTime.toString();
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

    public long getPortletId() {
        return portletId;
    }

    public void setPortletId(long portletId) {
        this.portletId = portletId;
    }

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }

    public void setNetworkService(NetworkService networkService) {
        this.networkService = networkService;
    }

    public void setResourcesService(ResourcesService resourcesService) {
        this.resourcesService = resourcesService;
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
