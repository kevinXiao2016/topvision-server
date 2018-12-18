/***********************************************************************
 * $Id: CmcOpticalAction.java,v1.0 2014-8-5 下午5:25:37 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.perf.action;

import java.io.IOException;
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

import com.topvision.ems.cmc.perf.domain.CmcOpticalTemp;
import com.topvision.ems.cmc.perf.service.CmcPerfService;
import com.topvision.ems.cmc.performance.domain.CmcLinkQualityData;
import com.topvision.ems.cmc.util.ResourcesUtil;
import com.topvision.ems.template.service.EntityTypeService;
import com.topvision.framework.common.DateUtils;
import com.topvision.framework.common.MacUtils;
import com.topvision.framework.utils.CmcIndexUtils;
import com.topvision.framework.utils.EponIndex;
import com.topvision.framework.web.struts2.BaseAction;
import com.topvision.platform.ResourceManager;
import com.topvision.platform.domain.UserContext;
import com.topvision.platform.zetaframework.var.UnitConfigConstant;

/**
 * @author flack
 * @created @2014-8-5-下午5:25:37
 *
 */
@Controller("cmcOpticalAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class CmcOpticalAction extends BaseAction {
    private static final long serialVersionUID = 2072415646238511983L;
    private final Logger logger = LoggerFactory.getLogger(CmcOpticalAction.class);

    private boolean loadMore;
    @Autowired
    private CmcPerfService cmcPerfService;
    @Autowired
    private EntityTypeService entityTypeService;
    private Long deviceType;
    private String nmName;

    /**
     * 在设备视图页面显示Cmc光功率Top10
     * 
     * @return
     * @throws IOException
     */
    public String loadCmcOpticalTop10() throws IOException {
        // 构造页面Portlet显示内容
        StringBuilder sb = new StringBuilder();
        UserContext uc = (UserContext) ServletActionContext.getRequest().getSession().getAttribute("UserContext");
        sb.append(
                "<table class='dataTable zebra noWrap' width='100%' border='1' cellspacing='0' cellpadding='0' bordercolor='#DFDFDF' rules='all' id='getTopCmcOptical'><thead><tr><th width='36'>")
                .append(getI18NString("resources", "WorkBench.Ranking")).append("</th><th>")
                .append(getI18NString("resources", "COMMON.alias")).append("</th><th width='69'>")
                .append(getI18NString("resources", "Common.deviceLocation")).append("</th><th width='80'>")
                .append(getI18NString("resources", "Common.deviceType")).append("</th><th width='80'>")
                .append(getI18NString("cmc", "CCMTS.txPower"))
                .append("</th><th width='80'><img src=\'../images/desc.gif\' border=0 align=absmiddle hspace=5>")
                .append(getI18NString("cmc", "CCMTS.rxPower")).append("</th><th width='126'>")
                .append(getI18NString("resources", "WorkBench.fromUpdateTime")).append("</th></tr></thead><tbody>");
        int i = 0;
        try {
            // 从数据库获取光功率数据
            Map<String, Object> paramMap = new HashMap<String, Object>();
            paramMap.put("start", 0);
            paramMap.put("limit", uc.getTopNumber());
            paramMap.put("deviceType", -1L);
            List<CmcLinkQualityData> opticalList = cmcPerfService.getCmcOpticalInfo(paramMap);
            for (CmcLinkQualityData opticalInfo : opticalList) {
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
                sb.append("<td class='txtLeft wordBreak'><a class=my-link href=\"javascript:showCcmtsPortal(");
                sb.append(opticalInfo.getCmcId());
                sb.append(",'");
                sb.append(opticalInfo.getMac());
                sb.append("','");
                sb.append(opticalInfo.getName());
                sb.append("');\">");
                sb.append(opticalInfo.getName());
                sb.append("</td>");
                sb.append("<td class='txtCenter'>");
                // 类A型和类B型处理方式不一样
                if (entityTypeService.isCcmtsWithoutAgent(opticalInfo.getTypeId())) {
                    sb.append(EponIndex.getOnuStringByIndex(opticalInfo.getPortIndex()));
                } else {
                    sb.append(CmcIndexUtils.getMarkFromIndex(opticalInfo.getPortIndex()));
                }
                sb.append("</td>");
                sb.append("<td class='txtCenter'>");
                sb.append(opticalInfo.getDisplayName());
                sb.append("</td>");
                sb.append("<td class='txtCenter'>");
                if (opticalInfo.getOptTxPowerFloat() != null && opticalInfo.getOptTxPowerFloat() != 0) {
                    sb.append(opticalInfo.getOptTxPowerFloat()).append(" dBm");
                } else {
                    sb.append("--");
                }
                sb.append("</td>");
                sb.append("<td class='txtCenter'>");
                if (opticalInfo.getOptRePowerFloat() != null && opticalInfo.getOptRePowerFloat() != 0) {
                    sb.append(opticalInfo.getOptRePowerFloat()).append(" dBm");
                } else {
                    sb.append("--");
                }
                sb.append("</td>");
                sb.append("<td  class='txtCenter'>");
                sb.append(DateUtils.getTimeDesInObscure(System.currentTimeMillis()
                        - opticalInfo.getCollectTime().getTime(), uc.getUser().getLanguage()));
                sb.append("</td></tr>");
            }
            sb.append("</tbody></table>");
            response.getWriter().print(sb.toString());
        } catch (Exception e) {
            logger.debug("Get CmcOptical Top10 failed", e);
        }
        return NONE;
    }

    /**
     * 显示CMC光功率显示页面
     * 
     * @return
     */
    public String showCmcOpticalInfo() {
        return SUCCESS;
    }

    /**
     * 显示CMC显示光机温度页面
     *
     * @return
     */
    public String showCmcOpticalTempInfo() {
        return SUCCESS;
    }

    /**
     * 加载CMC光功率展示数据
     * 
     * @return
     * @throws IOException
     */
    public String loadCmcOpticalInfo() throws IOException {
        UserContext uc = (UserContext) ServletActionContext.getRequest().getSession().getAttribute("UserContext");
        String macRule = uc.getMacDisplayStyle();
        // 从数据库获取光功率数据
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
        List<CmcLinkQualityData> opticalList = cmcPerfService.getCmcOpticalInfo(paramMap);
        for (CmcLinkQualityData opticalInfo : opticalList) {
            String formatedMac = MacUtils.convertMacToDisplayFormat(opticalInfo.getMac(), macRule);
            opticalInfo.setMac(formatedMac);
            // 类A型和类B型处理方式不一样
            if (entityTypeService.isCcmtsWithoutAgent(opticalInfo.getTypeId())) {
                opticalInfo.setLocation(EponIndex.getOnuStringByIndex(opticalInfo.getPortIndex()).toString());
            } else {
                opticalInfo.setLocation(CmcIndexUtils.getMarkFromIndex(opticalInfo.getPortIndex()));
            }
            // opticalInfo.setLocation(EponIndex.getOnuStringByIndex(opticalInfo.getPortIndex()).toString());
            opticalInfo.setFromLastTime(DateUtils.getTimeDesInObscure(System.currentTimeMillis()
                    - opticalInfo.getCollectTime().getTime(), uc.getUser().getLanguage()));
        }
        int totalNum = cmcPerfService.getCmcOpticalNum(paramMap);
        JSONArray opticalArray = JSONArray.fromObject(opticalList);
        JSONObject opticalJson = new JSONObject();
        opticalJson.put("total", totalNum);
        opticalJson.put("data", opticalArray);
        opticalJson.write(response.getWriter());
        return NONE;
    }

    /**
     * 加载CMC光机温度展示数据TopN
     * 
     * @return
     */
    public String getTopCmcOpticalTempLoading() {
        StringBuilder sb = new StringBuilder();
        UserContext uc = (UserContext) ServletActionContext.getRequest().getSession().getAttribute("UserContext");
        sb.append(
                "<table class='dataTable zebra noWrap' width='100%' border='1' cellspacing='0' cellpadding='0' bordercolor='#DFDFDF' rules='all' id='getTopCmcOpticalTemp'><thead><tr><th width='32'>")
                .append(ResourcesUtil.getString("WorkBench.Ranking")).append("</th><th width='70'>")
                .append(ResourcesUtil.getString("Config.oltConfigFileImported.deviceName")).append("</th><th>")
                .append(ResourcesUtil.getString("Common.deviceLocation")).append("</th><th width='70'>")
                .append(ResourcesUtil.getString("WorkBench.deviceType")).append("</th><th  class='wordBreak'>")
                .append("<img src=\"../images/desc.gif\" border=0 align=absmiddle hspace=5>")
                .append(ResourcesUtil.getString("WorkBench.opticalTemp")).append("</th><Th width='150'>")
                .append(ResourcesUtil.getString("WorkBench.fromUpdateTime")).append("</th></tr></thead><tbody>");
        int i = 0;
        try {
            Map<String, Object> paramMap = new HashMap<String, Object>();
            paramMap.put("start", 0);
            paramMap.put("limit", uc.getTopNumber());
            paramMap.put("deviceType", -1L);
            List<CmcOpticalTemp> cmcOpticalTemps = cmcPerfService.getCmcOpticalTempInfo(paramMap);
            for (CmcOpticalTemp temp : cmcOpticalTemps) {
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
                sb.append("<td class='txtLeft wordBreak'><a class=my-link href=\"javascript:showCcmtsPortal(");
                sb.append(temp.getCmcId());
                sb.append(",'");
                sb.append(temp.getMac());
                sb.append("','");
                sb.append(temp.getName());
                sb.append("');\">");
                sb.append(temp.getName());
                sb.append("</td>");
                sb.append("<td class='txtCenter'>");
                // 类A型和类B型处理方式不一样
                if (entityTypeService.isCcmtsWithoutAgent(temp.getTypeId())) {
                    sb.append(EponIndex.getOnuStringByIndex(temp.getPortIndex()));
                } else {
                    sb.append(CmcIndexUtils.getMarkFromIndex(temp.getPortIndex()));
                }
                sb.append("</td>");
                sb.append("<td class='txtCenter'>");
                sb.append(temp.getDisplayName());
                sb.append("</td>");
                sb.append("<td class='txtCenter'>");
                if (temp.getOptTempFloat() != null && temp.getOptTempFloat() != 0) {
                    Float t = temp.getOptTempFloat();
                    String tempUnit = (String) UnitConfigConstant.get(UnitConfigConstant.TEMP_UNIT);
                    sb.append(UnitConfigConstant.translateTemperature(t)).append(tempUnit);
                } else {
                    sb.append("--");
                }
                sb.append("</td>");
                sb.append("<td  class='txtCenter'>");
                sb.append(DateUtils.getTimeDesInObscure(System.currentTimeMillis() - temp.getCollectTime().getTime(),
                        uc.getUser().getLanguage()));
                sb.append("</td></tr>");
            }
            sb.append("</tbody></table>");
            response.getWriter().print(sb.toString());
        } catch (Exception e) {
            logger.debug("Get CmcOptical Top10 failed", e);
        }
        return NONE;
    }

    /**
     * 加载CMC光机温度展示数据
     * 
     * @return
     * @throws IOException
     */
    public String loadCmcOpticalTempInfo() throws IOException {
        UserContext uc = (UserContext) ServletActionContext.getRequest().getSession().getAttribute("UserContext");
        String macRule = uc.getMacDisplayStyle();
        // 从数据库获取光功率数据
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
        List<CmcOpticalTemp> opticalTempList = cmcPerfService.getCmcOpticalTempInfo(paramMap);
        for (CmcOpticalTemp opticalTemp : opticalTempList) {
            String formatedMac = MacUtils.convertMacToDisplayFormat(opticalTemp.getMac(), macRule);
            opticalTemp.setMac(formatedMac);
            // 类A型和类B型处理方式不一样
            if (entityTypeService.isCcmtsWithoutAgent(opticalTemp.getTypeId())) {
                opticalTemp.setLocation(EponIndex.getOnuStringByIndex(opticalTemp.getPortIndex()).toString());
            } else {
                opticalTemp.setLocation(CmcIndexUtils.getMarkFromIndex(opticalTemp.getPortIndex()));
            }
            // 温度转换
            String tempUnit = (String) UnitConfigConstant.get(UnitConfigConstant.TEMP_UNIT);
            if (opticalTemp.getOptTempFloat() != null && opticalTemp.getOptTempFloat() != 0) {
                Float tempValue = opticalTemp.getOptTempFloat();
                opticalTemp.setOptTempFloatStr(UnitConfigConstant.translateTemperature(tempValue) + tempUnit);
            } else {
                opticalTemp.setOptTempFloatStr(opticalTemp.getOptTempFloat() + tempUnit);
            }
            opticalTemp.setFromLastTime(DateUtils.getTimeDesInObscure(System.currentTimeMillis()
                    - opticalTemp.getCollectTime().getTime(), uc.getUser().getLanguage()));
        }
        int totalNum = cmcPerfService.getCmcOpticalTempNum(paramMap);
        JSONArray opticalTempArray = JSONArray.fromObject(opticalTempList);
        JSONObject opticalTempJson = new JSONObject();
        opticalTempJson.put("total", totalNum);
        opticalTempJson.put("data", opticalTempArray);
        opticalTempJson.write(response.getWriter());
        return NONE;
    }

    public boolean isLoadMore() {
        return loadMore;
    }

    public void setLoadMore(boolean loadMore) {
        this.loadMore = loadMore;
    }

    private String getI18NString(String module, String key) {
        module = String.format("com.topvision.ems.%s.resources", module);
        try {
            ResourceManager resourceManager = ResourceManager.getResourceManager(module);
            return resourceManager.getNotNullString(key);
        } catch (Exception e) {
            return key;
        }
    }

    public Long getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(Long deviceType) {
        this.deviceType = deviceType;
    }

    public String getNmName() {
        return nmName;
    }

    public void setNmName(String nmName) {
        this.nmName = nmName;
    }

}
