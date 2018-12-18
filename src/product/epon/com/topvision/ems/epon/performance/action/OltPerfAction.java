/***********************************************************************
 * $Id: OltPerfAction.java,v1.0 2013-10-25 下午3:41:18 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.performance.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.struts2.ServletActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.topvision.ems.batchdeploy.domain.Type;
import com.topvision.ems.epon.batchdeploy.domain.Level;
import com.topvision.ems.epon.batchdeploy.domain.Target;
import com.topvision.ems.epon.batchdeploy.service.EponBatchDeployService;
import com.topvision.ems.epon.domain.PerfRecord;
import com.topvision.ems.epon.exception.RefreshPerfStatCycleException;
import com.topvision.ems.epon.exception.RefreshPerfStatsGlobalSetException;
import com.topvision.ems.epon.exception.SavePerfStatCycleException;
import com.topvision.ems.epon.exception.SavePerfStatsGlobalSetException;
import com.topvision.ems.epon.olt.domain.OltPonAttribute;
import com.topvision.ems.epon.olt.domain.OltSniAttribute;
import com.topvision.ems.epon.olt.service.OltPonService;
import com.topvision.ems.epon.olt.service.OltSniService;
import com.topvision.ems.epon.performance.domain.PerfStatCycle;
import com.topvision.ems.epon.performance.domain.PerfStatsGlobalSet;
import com.topvision.ems.epon.performance.service.OltPerfService;
import com.topvision.ems.facade.batchdeploy.domain.Result;
import com.topvision.ems.facade.batchdeploy.domain.ResultBundle;
import com.topvision.framework.annotation.OperationLogProperty;
import com.topvision.framework.common.DateUtils;
import com.topvision.framework.common.NumberUtils;
import com.topvision.framework.web.struts2.BaseAction;
import com.topvision.platform.ResourceManager;
import com.topvision.platform.domain.OperationLog;
import com.topvision.platform.domain.UserContext;
import com.topvision.platform.zetaframework.util.ZetaUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * @author flack
 * @created @2013-10-25-下午3:41:18
 * 
 */
@Controller("oltPerfAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@SuppressWarnings("unchecked")
public class OltPerfAction extends BaseAction {
    private static final long serialVersionUID = -5784815252565726715L;
    private static final Logger logger = LoggerFactory.getLogger(OltPerfAction.class);
    @Autowired
    private OltPerfService oltPerfService;
    private PerfStatsGlobalSet perfStatsGlobalSet;
    private PerfStatCycle perfStatCycle;
    private Integer topPerfStatOLTCycle;
    private Integer topPerfStatONUCycle;
    private Integer topPerfOLTTemperatureCycle;
    private Integer topPerfONUTemperatureCycle;
    private Integer perfStats15MinMaxRecord;
    private Integer perfStats24HourMaxRecord;
    private Long entityId;
    private Integer operationResult;
    private String nmName;
    private Long deviceType; // 需要绑定的端口表达式
    private String needBinds;
    // 需要排除绑定的端口表达式
    private String excudeBinds;
    @Autowired
    private EponBatchDeployService eponBatchDeployService;
    private Integer status;
    @Autowired
    private OltSniService oltSniService;
    @Autowired
    private OltPonService oltPonService;

    /**
     * 跳转到批量配置端口性能开启的页面
     * 
     * @return
     */
    public String showBatchPerfCollectSetting() {
        return SUCCESS;
    }

    /**
     * 批量配置OLT的端口性能使能
     * 
     * @return
     * @throws IOException
     */
    public String saveBatchPerfCollect() throws IOException {
        List<String> needList = new ArrayList<String>();
        if (needBinds != null && !"".equals(needBinds)) {
            needList = Arrays.asList(needBinds.split("_"));
        }
        List<String> excludeList = new ArrayList<String>();
        if (excudeBinds != null && !"".equals(excudeBinds)) {
            excludeList = Arrays.asList(excudeBinds.split("_"));
        }
        ResultBundle<Target> bindResult;
        if (status == 1) {
            bindResult = eponBatchDeployService.batchDeploy(needList, excludeList, entityId, status,
                    "eponStatsEnableExecutor", Type.PORT_PERF15_UP,
                    ZetaUtil.getStaticString("RECORD.batchOpenPerf", "oltperf"));
        } else {
            bindResult = eponBatchDeployService.batchDeploy(needList, excludeList, entityId, status,
                    "eponStatsEnableExecutor", Type.PORT_PERF15_DOWN,
                    ZetaUtil.getStaticString("RECORD.batchClosePerf", "oltperf"));
        }
        JSONObject json = new JSONObject();
        if (bindResult.getData().isEmpty()) {
            json.put("successCount", 0);
            json.put("failedCount", 0);
        } else {
            for (Result<Target> result : bindResult.getData()) {
                json.put("successCount", result.getSuccessList().size());
                json.put("failedCount", result.getFailureList().size());
                for (Target $target : result.getSuccessList()) {
                    switch ($target.getLevel()) {
                    case Level.SNI:
                        OltSniAttribute $port = new OltSniAttribute();
                        $port.setEntityId(entityId);
                        $port.setSniIndex($target.getTargetIndex());
                        $port.setSniPerfStats15minuteEnable(status);
                        oltSniService.updateSni15PerfStatus(entityId, $target.getTargetIndex(), status);
                        break;
                    case Level.PON:
                        OltPonAttribute $pon = new OltPonAttribute();
                        $pon.setEntityId(entityId);
                        $pon.setPonIndex($target.getTargetIndex());
                        $pon.setPerfStats15minuteEnable(status);
                        oltPonService.updatePon15PerfStatus(entityId, $target.getTargetIndex(), status);
                        break;
                    }
                }
            }
        }
        json.write(response.getWriter());
        return NONE;
    }

    /**
     * 获取SNI端口速率排行
     * 
     * @return
     * @throws Exception
     */
    public String getTopSniLoading() throws IOException {
        ResourceManager resourceManager = ResourceManager.getResourceManager("com.topvision.ems.resources.resources");
        UserContext uc = (UserContext) ServletActionContext.getRequest().getSession().getAttribute("UserContext");
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("start", 0);
        paramMap.put("limit", uc.getTopNumber());
        StringBuilder sb = new StringBuilder();
        sb.append("<table class='dataTable zebra noWrap' width='100%' border='1' cellspacing='0' cellpadding='0' bordercolor='#DFDFDF' rules='all' id='getTopSniLoading'><thead>");
        sb.append("<tr><th width='36'>");
        sb.append(resourceManager.getString("WorkBench.Ranking"));
        sb.append("</th><th>");
        sb.append(resourceManager.getString("Config.oltConfigFileImported.deviceName"));
        sb.append("</th><th width='100'>");
        sb.append(resourceManager.getString("COMMON.manageIp"));
        sb.append("</th><th width='30'>");
        sb.append(resourceManager.getString("COMMON.portHeader"));
        sb.append("</th><th class='wordBreak'><img src=\"../images/desc.gif\" border=0 align=absmiddle hspace=5>");
        sb.append(resourceManager.getString("WorkBench.sniPortFlowRate"));
        sb.append("</th><th class='wordBreak'>");
        sb.append(resourceManager.getString("WorkBench.sniPortOutFlowRate"));
        sb.append("</th><th width='126'>");
        sb.append(resourceManager.getString("WorkBench.fromUpdateTime"));
        sb.append("</th></tr></thead><tbody>");

        List<PerfRecord> perfRecords = oltPerfService.getTopSniLoading(paramMap);
        int i = 0;
        // NumberUtils.getBandWidth方式转换流量时单位存在问题，此方法是转换成速率，修改为NumberUtils.getByteLength方式
        // 修改人：lizongtian
        for (PerfRecord perfRecord : perfRecords) {
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

            sb.append("<td class=\"txtLeft wordBreak\"><a href=\"javascript:showEntitySnap(");
            sb.append(perfRecord.getEntityId());
            sb.append(",'");
            sb.append(perfRecord.getName());
            sb.append("');\">");
            sb.append(perfRecord.getName());
            sb.append("</a></td>");
            sb.append("<td class='txtLeft'>");
            sb.append(perfRecord.getIp());
            sb.append("</td>");
            sb.append("<td align='center'>");
            sb.append(perfRecord.getPortName());
            sb.append("</td>");
            sb.append("<td align=center>");
            if (perfRecord.getValue() < 0) {
                sb.append(NumberUtils.getIfSpeedStr(0));
            } else {
                sb.append(NumberUtils.getIfSpeedStr(perfRecord.getValue()));
            }
            sb.append("</td>");
            sb.append("<td align=center>");
            if (perfRecord.getTempValue() < 0) {
                sb.append(NumberUtils.getIfSpeedStr(0));
            } else {
                sb.append(NumberUtils.getIfSpeedStr(perfRecord.getTempValue()));
            }
            sb.append("</td>");
            sb.append("<td align=center>");
            sb.append(
                    DateUtils.getTimeDesInObscure(System.currentTimeMillis() - perfRecord.getCollectTime().getTime(),
                            uc.getUser().getLanguage())).append("</td></tr>");
        }
        sb.append("</tbody></table>");
        writeDataToAjax(sb.toString());
        return NONE;
    }

    public String showSniPortSpeed() {
        return SUCCESS;
    }

    public String loadSniSpeedList() throws IOException {
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
        List<PerfRecord> perfRecords = oltPerfService.getTopSniLoading(paramMap);
        for (PerfRecord re : perfRecords) {
            if (re.getValue() == null || re.getValue() < 0) {
                re.setValue(0d);
            }
            if (re.getTempValue() == null || re.getTempValue() < 0) {
                re.setTempValue(0d);
            }
        }
        int totalNum = oltPerfService.getTopSniSpeedCount(paramMap);
        JSONArray sniSpeedList = JSONArray.fromObject(perfRecords);
        JSONObject resultJson = new JSONObject();
        resultJson.put("rowCount", totalNum);
        resultJson.put("data", sniSpeedList);
        resultJson.write(response.getWriter());
        return NONE;
    }

    /**
     * 刷新PON端口速率排行
     * 
     * @return
     * @throws Exception
     */
    public String getTopPonLoading() throws Exception {
        ResourceManager resourceManager = ResourceManager.getResourceManager("com.topvision.ems.resources.resources");
        UserContext uc = (UserContext) ServletActionContext.getRequest().getSession().getAttribute("UserContext");
        StringBuilder sb = new StringBuilder();
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("start", 0);
        paramMap.put("limit", uc.getTopNumber());

        sb.append("<table class='dataTable zebra noWrap' width='100%' border='1' cellspacing='0' cellpadding='0' bordercolor='#DFDFDF' rules='all' id='getTopPonLoading'><thead><tr><th width='36'>");
        sb.append(resourceManager.getString("WorkBench.Ranking"));
        sb.append("</th><th>");
        sb.append(resourceManager.getString("Config.oltConfigFileImported.deviceName"));
        sb.append("</th><th width='100'>");
        sb.append(resourceManager.getString("COMMON.manageIp"));
        sb.append("</th><th width='30'>");
        sb.append(resourceManager.getString("COMMON.portHeader"));
        sb.append("</th><th class='wordBreak'>");
        sb.append("<img src=\"../images/desc.gif\" border=0 align=absmiddle hspace=5>");
        sb.append(resourceManager.getString("WorkBench.PonPortFlowRate"));
        sb.append("</th><th class='wordBreak'>");
        sb.append(resourceManager.getString("WorkBench.PonPortOutFlowRate"));
        sb.append("</th><th width='126'>");
        sb.append(resourceManager.getString("WorkBench.fromUpdateTime"));
        sb.append("</th></tr></thead><tbody>");
        List<PerfRecord> perfRecords = oltPerfService.getTopPonLoading(paramMap);
        int i = 0;
        // NumberUtils.getBandWidth方式转换流量时单位存在问题，此方法是转换成速率，修改为NumberUtils.getByteLength方式
        // 修改人：lizongtian
        for (PerfRecord perfRecord : perfRecords) {
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
            sb.append("<td class='txtLeft wordBreak'><a class=my-link href=\"javascript:showEntitySnap(");
            sb.append(perfRecord.getEntityId());
            sb.append(",'");
            sb.append(perfRecord.getName());
            sb.append("');\">");
            sb.append(perfRecord.getName());
            sb.append("</a></td>");
            sb.append("<td class='txtLeft'>");
            sb.append(perfRecord.getIp());
            sb.append("</td>");
            sb.append("<td align='center'>");
            sb.append(perfRecord.getPortName());
            sb.append("</td>");
            sb.append("<td align='center'>");
            if (perfRecord.getValue() < 0) {
                sb.append(NumberUtils.getIfSpeedStr(0));
            } else {
                sb.append(NumberUtils.getIfSpeedStr(perfRecord.getValue()));
            }
            sb.append("</td>");
            sb.append("<td align=center>");
            if (perfRecord.getTempValue() < 0) {
                sb.append(NumberUtils.getIfSpeedStr(0));
            } else {
                sb.append(NumberUtils.getIfSpeedStr(perfRecord.getTempValue()));
            }
            sb.append("</td>");
            sb.append("<td align='center'>");
            sb.append(
                    DateUtils.getTimeDesInObscure(System.currentTimeMillis() - perfRecord.getCollectTime().getTime(),
                            uc.getUser().getLanguage())).append("</td></tr>");
        }
        sb.append("</tbody></table>");
        writeDataToAjax(sb.toString());
        return NONE;
    }

    public String showPonPortSpeed() {
        return SUCCESS;
    }

    public String loadPonSpeedList() throws IOException {
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
        List<PerfRecord> perfRecords = oltPerfService.getTopPonLoading(paramMap);
        for (PerfRecord re : perfRecords) {
            if (re.getValue() == null || re.getValue() < 0) {
                re.setValue(0d);
            }
            if (re.getTempValue() == null || re.getTempValue() < 0) {
                re.setTempValue(0d);
            }
        }
        int totalNum = oltPerfService.getTopPonSpeedCount(paramMap);
        JSONArray ponSpeedList = JSONArray.fromObject(perfRecords);
        JSONObject resultJson = new JSONObject();
        resultJson.put("rowCount", totalNum);
        resultJson.put("data", ponSpeedList);
        resultJson.write(response.getWriter());
        return NONE;
    }

    /**
     * 显示轮询周期设置界面 struct
     * 
     * @return
     */
    public String showPerfStatCycle() {
        // 获取轮询周期参数
        perfStatCycle = oltPerfService.getPerfStatCycle(entityId);
        // 转换成JSON传递到界面
        // perfStatCycleJson = JSONObject.fromObject(perfStatCycle);
        return SUCCESS;
    }

    /**
     * 保存轮询周期设置
     * 
     * @return
     */
    @OperationLogProperty(actionName = "oltPerfAction", operationName = "savePerfStatCycle")
    public String savePerfStatCycle() throws IOException {
        // 通过界面参数构造 PerfStatCycle
        PerfStatCycle perfStatCycle = new PerfStatCycle();
        perfStatCycle.setEntityId(entityId);
        perfStatCycle.setTopPerfStatOLTCycle(topPerfStatOLTCycle);
        perfStatCycle.setTopPerfStatONUCycle(topPerfStatONUCycle);
        perfStatCycle.setTopPerfOLTTemperatureCycle(topPerfOLTTemperatureCycle);
        perfStatCycle.setTopPerfONUTemperatureCycle(topPerfONUTemperatureCycle);
        // 设置到服务器端
        String message;
        try {
            oltPerfService.savePerfStatCycle(perfStatCycle);
            message = "success";
            operationResult = OperationLog.SUCCESS;
        } catch (SavePerfStatCycleException e) {
            message = "error[" + e.getMessage() + "]";
            operationResult = OperationLog.FAILURE;
            logger.debug("savePerfStatCycle error: {}", e);
        }
        // 返回设置成功与否结果
        writeDataToAjax(message);
        return NONE;
    }

    /**
     * 从设备上刷新轮询间隔到数据库
     * 
     * @return
     */
    public String refreshPerfStatCycle() throws IOException {
        String message;
        // 从设备上刷新轮询间隔到数据库
        try {
            oltPerfService.refreshPerfStatCycle(entityId);
            message = "success";
        } catch (RefreshPerfStatCycleException e) {
            message = "error[" + e.getMessage() + "]";
        }
        // 判断是否修改成功
        writeDataToAjax(message);
        return NONE;
    }

    /**
     * 获取记录数配置参数
     * 
     * @return
     */
    public String showPerfStatsGlobalSet() {
        // 获取记录数参数
        perfStatsGlobalSet = oltPerfService.getPerfStatsGlobalSet(entityId);
        // 转换成JSON传递到界面
        // perfStatsGlobalSetJson = JSONObject.fromObject(perfStatsGlobalSet);
        return SUCCESS;
    }

    /**
     * 保存记录数配置参数
     * 
     * @return
     */
    @OperationLogProperty(actionName = "oltPerfAction", operationName = "savePerfStatsGlobalSet")
    public String savePerfStatsGlobalSet() throws IOException {
        // 通过界面参数构造 PerfStatsGlobalSet
        PerfStatsGlobalSet perfStatsGlobalSet = new PerfStatsGlobalSet();
        perfStatsGlobalSet.setEntityId(entityId);
        perfStatsGlobalSet.setPerfStats15MinMaxRecord(perfStats15MinMaxRecord);
        perfStatsGlobalSet.setPerfStats24HourMaxRecord(perfStats24HourMaxRecord);
        // 设置到服务器端
        String message;
        try {
            oltPerfService.savePerfStatsGlobalSet(perfStatsGlobalSet);
            message = "success";
            operationResult = OperationLog.SUCCESS;
        } catch (SavePerfStatsGlobalSetException e) {
            message = "error[" + e.getMessage() + "]";
            operationResult = OperationLog.FAILURE;
            logger.debug("savePerfStatsGlobalSet error: {}", e);
        }
        // 返回设置成功与否结果
        writeDataToAjax(message);
        return NONE;
    }

    /**
     * 从设备上刷新历史数据记录数配置到数据库
     * 
     * @return
     */
    public String refreshPerfStatsGlobalSet() throws IOException {
        String message;
        // 从设备上刷新历史数据记录数配置到数据库
        try {
            oltPerfService.refreshPerfStatsGlobalSet(entityId);
            message = "success";
        } catch (RefreshPerfStatsGlobalSetException e) {
            message = "error[" + e.getMessage() + "]";
        }
        // 判断是否修改成功
        writeDataToAjax(message);
        return NONE;
    }

    public Integer getTopPerfOLTTemperatureCycle() {
        return topPerfOLTTemperatureCycle;
    }

    public void setTopPerfOLTTemperatureCycle(Integer topPerfOLTTemperatureCycle) {
        this.topPerfOLTTemperatureCycle = topPerfOLTTemperatureCycle;
    }

    public Integer getTopPerfONUTemperatureCycle() {
        return topPerfONUTemperatureCycle;
    }

    public void setTopPerfONUTemperatureCycle(Integer topPerfONUTemperatureCycle) {
        this.topPerfONUTemperatureCycle = topPerfONUTemperatureCycle;
    }

    public Integer getTopPerfStatOLTCycle() {
        return topPerfStatOLTCycle;
    }

    public void setTopPerfStatOLTCycle(Integer topPerfStatOLTCycle) {
        this.topPerfStatOLTCycle = topPerfStatOLTCycle;
    }

    public Integer getTopPerfStatONUCycle() {
        return topPerfStatONUCycle;
    }

    public void setTopPerfStatONUCycle(Integer topPerfStatONUCycle) {
        this.topPerfStatONUCycle = topPerfStatONUCycle;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public Integer getPerfStats15MinMaxRecord() {
        return perfStats15MinMaxRecord;
    }

    public void setPerfStats15MinMaxRecord(Integer perfStats15MinMaxRecord) {
        this.perfStats15MinMaxRecord = perfStats15MinMaxRecord;
    }

    public Integer getPerfStats24HourMaxRecord() {
        return perfStats24HourMaxRecord;
    }

    public void setPerfStats24HourMaxRecord(Integer perfStats24HourMaxRecord) {
        this.perfStats24HourMaxRecord = perfStats24HourMaxRecord;
    }

    public PerfStatsGlobalSet getPerfStatsGlobalSet() {
        return perfStatsGlobalSet;
    }

    public void setPerfStatsGlobalSet(PerfStatsGlobalSet perfStatsGlobalSet) {
        this.perfStatsGlobalSet = perfStatsGlobalSet;
    }

    public PerfStatCycle getPerfStatCycle() {
        return perfStatCycle;
    }

    public void setPerfStatCycle(PerfStatCycle perfStatCycle) {
        this.perfStatCycle = perfStatCycle;
    }

    public Integer getOperationResult() {
        return operationResult;
    }

    public void setOperationResult(Integer operationResult) {
        this.operationResult = operationResult;
    }

    /**
     * 跳转到端口采集参数界面
     * 
     * @return
     */
    public String showPortPerfCycle() {
        return SUCCESS;
    }

    public String showSaveAsGolbal() {
        return SUCCESS;
    }

    public String showApplyToAllEpon() {
        return SUCCESS;
    }

    /**
     * 获取SNI端口速率排行
     * 
     * @return
     * @throws Exception
     */
    public String getDeviceSniLoading() throws IOException {
        ResourceManager resourceManager = ResourceManager.getResourceManager("com.topvision.ems.resources.resources");
        UserContext uc = (UserContext) ServletActionContext.getRequest().getSession().getAttribute("UserContext");
        StringBuilder sb = new StringBuilder();
        sb.append("<table class='dataTable zebra noWrap' width='100%' border='1' cellspacing='0' cellpadding='0' bordercolor='#DFDFDF' rules='all' id='getTopSniLoading'><thead>");
        sb.append("<tr><th width='80' align='center'>");
        sb.append(resourceManager.getString("COMMON.portHeader"));
        sb.append("</th><th class='wordBreak'>");
        sb.append(resourceManager.getString("WorkBench.sniPortFlowRate"));
        sb.append("</th><th class='wordBreak'>");
        sb.append(resourceManager.getString("WorkBench.sniPortOutFlowRate"));
        sb.append("</th><th width='150'>");
        sb.append(resourceManager.getString("WorkBench.fromUpdateTime"));
        sb.append("</th></tr></thead><tbody>");

        List<PerfRecord> perfRecords = oltPerfService.getDeviceSniLoading(entityId);
        // NumberUtils.getBandWidth方式转换流量时单位存在问题，此方法是转换成速率，修改为NumberUtils.getByteLength方式
        // 修改人：lizongtian
        for (PerfRecord perfRecord : perfRecords) {
            sb.append("<tr>")
                    .append("<td align='center' class='txtLeft wordBreak'><a href=\"javascript:showOltPerfView(")
                    .append(perfRecord.getEntityId())
                    .append(",'")
                    .append(perfRecord.getPortIndex())
                    .append("','")
                    .append("SNI")
                    .append("');\">")
                    .append(perfRecord.getPortName())
                    .append("</a></td>")
                    .append("<td align=center>")
                    .append(NumberUtils.getIfSpeedStr(perfRecord.getValue()))
                    .append("</td>")
                    .append("<td align=center>")
                    .append(NumberUtils.getIfSpeedStr(perfRecord.getTempValue()))
                    .append("</td>")
                    .append("<td align=center>")
                    .append(DateUtils.getTimeDesInObscure(System.currentTimeMillis()
                            - perfRecord.getCollectTime().getTime(), uc.getUser().getLanguage())).append("</td></tr>");
        }
        sb.append("</tbody></table>");
        writeDataToAjax(sb.toString());
        return NONE;
    }

    /**
     * 刷新PON端口速率排行
     * 
     * @return
     * @throws Exception
     */
    public String getDevicePonLoading() throws Exception {
        ResourceManager resourceManager = ResourceManager.getResourceManager("com.topvision.ems.resources.resources");
        UserContext uc = (UserContext) ServletActionContext.getRequest().getSession().getAttribute("UserContext");
        StringBuilder sb = new StringBuilder();
        sb.append("<table class='dataTable zebra noWrap' width='100%' border='1' cellspacing='0' cellpadding='0' bordercolor='#DFDFDF' rules='all' id='getTopPonLoading'><thead><tr>");
        sb.append("<th width='80' align='center'>");
        sb.append(resourceManager.getString("COMMON.portHeader"));
        sb.append("</th><th class='wordBreak'>");
        sb.append(resourceManager.getString("WorkBench.PonPortFlowRate"));
        sb.append("</th><th class='wordBreak'>");
        sb.append(resourceManager.getString("WorkBench.PonPortOutFlowRate"));
        sb.append("</th><th width='150'>");
        sb.append(resourceManager.getString("WorkBench.fromUpdateTime"));
        sb.append("</th></tr></thead><tbody>");
        List<PerfRecord> perfRecords = oltPerfService.getDevicePonLoading(entityId);
        // NumberUtils.getBandWidth方式转换流量时单位存在问题，此方法是转换成速率，修改为NumberUtils.getByteLength方式
        // 修改人：lizongtian
        for (PerfRecord perfRecord : perfRecords) {
            sb.append("<tr>")
                    .append("<td align='center'><a class=my-link href=\"javascript:showOltPerfView(")
                    .append(perfRecord.getEntityId())
                    .append(",'")
                    .append(perfRecord.getPortIndex())
                    .append("','")
                    .append("PON")
                    .append("');\">")
                    .append(perfRecord.getPortName())
                    .append("</a></td>")
                    .append("<td align='center'>")
                    .append(NumberUtils.getIfSpeedStr(perfRecord.getValue()))
                    .append("</td>")
                    .append("<td align=center>")
                    .append(NumberUtils.getIfSpeedStr(perfRecord.getTempValue()))
                    .append("</td>")
                    .append("<td align='center'>")
                    .append(DateUtils.getTimeDesInObscure(System.currentTimeMillis()
                            - perfRecord.getCollectTime().getTime(), uc.getUser().getLanguage())).append("</td></tr>");
        }
        sb.append("</tbody></table>");
        writeDataToAjax(sb.toString());
        return NONE;
    }

    /**
     * 国际化
     * 
     * @param key
     *            key
     * @return String
     */
    protected String getString(String key, String module) {
        module = String.format("com.topvision.ems.%s.resources", module);
        try {
            ResourceManager resourceManager = ResourceManager.getResourceManager(module);
            return resourceManager.getNotNullString(key);
        } catch (Exception e) {
            return key;
        }
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

    public String getNeedBinds() {
        return needBinds;
    }

    public void setNeedBinds(String needBinds) {
        this.needBinds = needBinds;
    }

    public String getExcudeBinds() {
        return excudeBinds;
    }

    public void setExcudeBinds(String excudeBinds) {
        this.excudeBinds = excudeBinds;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

}
