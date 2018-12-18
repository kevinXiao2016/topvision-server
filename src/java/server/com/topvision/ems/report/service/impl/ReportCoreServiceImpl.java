/***********************************************************************
 * $Id: ReportCoreServiceImpl.java,v1.0 2014-6-18 上午9:19:32 $
 * 
 * @author: Rod John
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.report.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.struts2.ServletActionContext;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;

import com.topvision.ems.report.dao.ReportCoreDao;
import com.topvision.ems.report.dao.StatReportDao;
import com.topvision.ems.report.domain.Report;
import com.topvision.ems.report.domain.ReportColumnReferences;
import com.topvision.ems.report.domain.ReportNode;
import com.topvision.ems.report.domain.ReportStructure;
import com.topvision.ems.report.domain.ReportTask;
import com.topvision.ems.report.service.ReportCoreService;
import com.topvision.ems.report.service.ReportInstanceService;
import com.topvision.ems.report.service.ReportTxDataService;
import com.topvision.ems.report.util.ReportExportUtil;
import com.topvision.ems.report.util.ReportTaskUtil;
import com.topvision.ems.report.util.ReportXmlParser;
import com.topvision.framework.common.DateUtils;
import com.topvision.framework.common.MacUtils;
import com.topvision.framework.service.BaseService;
import com.topvision.license.parser.LicenseIf;
import com.topvision.platform.ResourceManager;
import com.topvision.platform.SystemConstants;
import com.topvision.platform.domain.UserPreferences;
import com.topvision.platform.message.event.LicenseChangeEvent;
import com.topvision.platform.message.event.LicenseChangeListener;
import com.topvision.platform.message.service.MessageService;
import com.topvision.platform.service.UserPreferencesService;
import com.topvision.platform.service.UserService;
import com.topvision.platform.util.CurrentRequest;
import com.topvision.platform.util.FtpClientUtil;
import com.topvision.platform.zetaframework.var.UnitConfigConstant;
import com.topvision.report.core.ReportDataService;
import com.topvision.report.core.domain.DescrptionModel;
import com.topvision.report.core.domain.SelectModel;
import com.topvision.report.core.util.ExceedLimit;
import com.topvision.report.core.util.ReportCoreUtil;

/**
 * @author Rod John
 * @created @2014-6-18-上午9:19:32
 * 
 */
public class ReportCoreServiceImpl extends BaseService implements ReportCoreService, BeanFactoryAware,
        LicenseChangeListener {
    private Map<String, Report> allReportCollection = new HashMap<String, Report>();
    private Map<String, Report> allTopReportCollection = new HashMap<String, Report>();// 所有报表，包括license不支持的报表，license功能使用
    private List<Report> topLevelReports = new ArrayList<>();
    private List<Report> allTopReports = new ArrayList<>();
    private List<String> xmlPath = new ArrayList<>();
    @Autowired
    private ReportTxDataService reportTxDataService;
    @Autowired
    private ReportCoreDao reportCoreDao;
    @Autowired
    private StatReportDao statReportDao;
    private BeanFactory beanFactory;
    private Resource[] reportXmlResources;
    @Autowired
    private UserService userService;
    @Autowired
    private ReportInstanceService reportInstanceService;
    @Autowired
    private UserPreferencesService userPreferencesService;
    @Autowired
    private LicenseIf licenseIf;
    @Autowired
    private MessageService messageService;

    public static final String CORE_REPORT_FOLDER = SystemConstants.ROOT_REAL_PATH + File.separator + "META-INF"
            + File.separator + "coreRootFolder" + File.separator;
    public static final String CORE_REPORT_TASK_FOLDER = SystemConstants.ROOT_REAL_PATH + File.separator + "META-INF"
            + File.separator + "coreRootTaskFolder" + File.separator;
    public static final DateFormat fileNameFormatter = new SimpleDateFormat("yyyyMMddHHmmss");
    public static final DateFormat formatter_ymd_hms_SSS = new SimpleDateFormat("yyyyMMddHHmmssSSS");
    private static final DateFormat stTimeFormatter_Daily = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
    private static final DateFormat etTimeFormatter_Daily = new SimpleDateFormat("yyyy-MM-dd 23:59:59");
    public static final Integer WEEK_REPORT = 1;
    public static final Integer MONTH_REPORT = 2;
    public static final int EXCEL = 0;
    private static final String REPORT_PREFIX = "report-";
    private static final String REPORT_SUFFIX = "-";
    public static final String RENDER_MAC = "renderMac";
    public static final String RENDER_DOUBLE = "renderDouble"; // 将double数据格式化
    public static final String RENDER_TEMPERATURE = "renderTemperature";
    public static final String RENDER_PERCENT = "renderPercent";

    @Override
    public void initialize() {
        super.initialize();
        messageService.addListener(LicenseChangeListener.class, this);
    }

    @Override
    public void start() {
        super.start();
        // 遍历注入的所有报表xml，进行报表解析
        Report report;
        for (Resource resource : reportXmlResources) {
            report = null;
            try {
                report = ReportXmlParser.parser(resource);
            } catch (Exception e) {
                logger.error("Parser Report Xml" + resource.getFilename() + " Error", e);
                continue;
            }
            // XML加载避免重复过滤
            if (allReportCollection.containsKey(report.getId())) {
                continue;
            }
            //所有报表集合，包括二级报表
            allReportCollection.put(report.getId(), report);
            
            if (report.isTopLevel()) {
                // 所有报表，包括license不支持的，add by loyal
                allTopReports.add(report);
                allTopReportCollection.put(report.getId(), report);
            }
            // 报表license过滤
            if (!isReportSupported(report)) {
                continue;
            }
            // Handle Top Level Report
            if (report.isTopLevel()) {
                topLevelReports.add(report);
            }

        }
    }

    @Override
    public void destroy() {
        super.destroy();
        messageService.removeListener(LicenseChangeListener.class, this);
    }

    @Override
    public void stop() {
        super.stop();
        allTopReports.clear();
        topLevelReports.clear();
        allReportCollection.clear();
        allTopReportCollection.clear();
    }

    @Override
    public void licenseChanged(LicenseChangeEvent evt) {
        stop();
        start();
    }

    /**
     * Handle ReportNode Data with Report Data and Report Structure
     * 
     * @param report
     * @param datas
     * @return
     * @throws Exception
     */
    public Map<String, Object> fetchReportNodeData(Report report, List<Object> datas, Map<String, String> preferenceMap)
            throws Exception {
        ReportNode reportNode = fetchReportNode(report, datas, preferenceMap);
        return handleReportNode(reportNode);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.report.service.ReportCoreService#fetchReportStructureInfo(java.lang.String)
     */
    @Override
    public Report fetchReportStructureInfo(String reportId) {
        return fetchReportOrReloadReport(reportId);
    }

    /**
     * 
     * 
     * @param reportId
     * @param datas
     * @param report
     * @return
     * @throws Exception
     */
    private ReportNode fetchReportNode(Report report, List<Object> datas, Map<String, String> preferenceMap)
            throws Exception {
        return handleReportNodeStep1(datas, report, report.getReportStructure(), preferenceMap);
    }

    /**
     * 
     * 
     * @param reportNode
     * @return
     */
    private Map<String, Object> handleReportNode(ReportNode reportNode) {
        return ReportXmlParser.handleJsonObject(reportNode);
    }

    /**
     * handle ReportNode with datas and report
     * 
     * @param objects
     * @param report
     * @param reportStructure
     * @return
     * @throws Exception
     */
    public ReportNode handleReportNodeStep1(List<Object> objects, Report report, ReportStructure reportStructure,
            Map<String, String> preferenceMap) throws Exception {
        List<ReportNode> reportNodes = new ArrayList<>();
        ReportNode rootNode = new ReportNode();
        reportNodes.add(rootNode);
        for (Object object : objects) {
            handleReportNodeStep2(rootNode, reportNodes, object, report, reportStructure, preferenceMap);
        }
        rootNode = handleReportNodeStep3(reportNodes);
        return rootNode;
    }

    /**
     * handle ReportNode with datas and current-level reportSrtucure
     * 
     * @param parentNode
     * @param list
     * @param object
     * @param report
     * @param reportStructure
     * @throws Exception
     */
    private void handleReportNodeStep2(ReportNode parentNode, List<ReportNode> list, Object object, Report report,
            ReportStructure reportStructure, Map<String, String> preferenceMap) throws Exception {
        if (reportStructure != null) {
            ReportNode reportNode = new ReportNode();
            reportNode.setParentNode(parentNode);
            String nodeKey = reportStructure.getNodeKey();
            Method getMethod = getGetMethod(object, nodeKey);
            String keyValue = String.valueOf(getMethod.invoke(object));
            reportNode.setNodeKey(nodeKey);
            reportNode.setNodeValue(keyValue);
            // Handle Node Maps
            for (Entry<String, Object> entry : reportStructure.getNodeMaps().entrySet()) {
                String keyEntry = entry.getKey();
                ReportColumnReferences columnReferences = (ReportColumnReferences) entry.getValue();
                String value = renderNodeValue(keyEntry, columnReferences, object, report, preferenceMap);
                reportNode.getNodeMaps().put(keyEntry, value);
            }
            // Handle Node Group
            reportNode.setReportGroup(reportStructure.getReportGroup());
            list.add(reportNode);
            handleReportNodeStep2(reportNode, list, object, report, reportStructure.getChildreNode(), preferenceMap);
        }
    }

    /**
     * Parser table data with globalization
     * 
     * use itself report object to handle
     * 
     * @param nodeKey
     * @param object
     * @param report
     * @return
     * @throws Exception
     */
    private String renderNodeValue(String nodeKey, ReportColumnReferences columnReferences, Object object,
            Report report, Map<String, String> preferenceMap) throws Exception {
        Method getFieldMethod = getGetMethod(object, nodeKey);
        String value = String.valueOf(getFieldMethod.invoke(object));
        // 先做国际化处理
        if (columnReferences.getNeedI18N()) {
            value = report.getString(value);
        }
        // 如果需要做render，则做render
        String render = columnReferences.getRender();
        if (render != null && !render.equals("")) {
            value = renderValue(value, render, preferenceMap);
        }
        return value;
    }

    private String renderValue(String value, String render, Map<String, String> preferenceMap) {
        if (render == null || render.equals("")) {
            return value;
        }
        String ret = null;
        try {
            switch (render) {
            case RENDER_DOUBLE:
                // 格式化小数
                ret = String.format("%.2f", Double.valueOf(value));
                break;
            case RENDER_MAC:
                // 转换MAC地址格式
                ret = MacUtils.convertMacToDisplayFormat(value, preferenceMap.get(UserPreferences.MACDISPLAYSTYLE));
                break;
            case RENDER_TEMPERATURE:
                // 转换温度
                double tempValue = Double.valueOf(value).doubleValue();
                ret = UnitConfigConstant.formatTemperature(tempValue, preferenceMap.get("tempUnit"));
                break;
            case RENDER_PERCENT:
                // 转换百分比
                double per_value = Double.valueOf(value).doubleValue() * 100;
                ret = String.format("%.2f", per_value) + "%";
                break;
            default:
                ret = value;
            }
        } catch (Exception e) {
            ret = value;
        }
        return ret;
    }

    /**
     * handle list reportNode to single reportNode with relationShip
     * 
     * @param reportNodes
     * @return
     */
    private ReportNode handleReportNodeStep3(List<ReportNode> reportNodes) {
        Map<ReportNode, ReportNode> rMap = new HashMap<>();
        for (ReportNode node : reportNodes) {
            if (!rMap.containsKey(node)) {
                rMap.put(node, node);
                ReportNode pNode = node.getParentNode();
                if (pNode == null) {
                    continue;
                }
                if (rMap.containsKey(pNode)) {
                    ReportNode tmpNode = rMap.get(pNode);
                    tmpNode.insertChildrenNode(node);
                }
            }
        }
        for (Entry<ReportNode, ReportNode> entry : rMap.entrySet()) {
            if (entry.getKey().getNodeKey().equals("ReportRoot")) {
                return entry.getValue();
            }
        }
        return null;
    }

    /**
     * Fetch Get Method
     * 
     * @param object
     * @param field
     * @return
     * @throws Exception
     */
    private Method getGetMethod(Object object, String field) throws Exception {
        Method getMethod = object.getClass()
                .getMethod("get" + field.substring(0, 1).toUpperCase() + field.substring(1));
        return getMethod;
    }

    /**
     * 
     * 
     * @param reportId
     * @return
     */
    @Override
    public Report fetchReportOrReloadReport(String reportId) {
        Report report = allReportCollection.get(reportId);
        if (report == null) {
            // ReLoad XML
        }
        return report;
    }

    @Override
    public JSONArray getQueryConditionById(String reportId) {
        Report report = fetchReportOrReloadReport(reportId);
        if (report.getReportConditions() == null) {
            return new JSONArray();
        }
        return JSONArray.fromObject(report.getReportConditions());
    }

    @Override
    public JSONObject initReportContent(String reportId, Map<String, String> queryMap) throws Exception {
        // querymap 查询条件处理
        prepareQueryMap(queryMap);
        Map<String, Object> result = new HashMap<>();
        Report report = fetchReportOrReloadReport(reportId);
        List<Object> datas = null;
        List<DescrptionModel> desc = null;
        if (report.getLazyLoad().equals("true")) {
            datas = new ArrayList<>();
        } else {
            ReportDataService reportDataService = (ReportDataService) beanFactory
                    .getBean(getReportServiceName(reportId));
            try {
                datas = reportTxDataService.txFetchReportData(reportDataService, queryMap);
                desc = reportTxDataService.fetchReportDescription(reportDataService, queryMap);
            } catch (Exception e) {
                logger.error("Fetch ReportDate Error ", e);
                datas = new ArrayList<>();
            }
        }
        result.put("title", report.getTitle());
        result.put("combination", report.isCombination());
        result.put("time", DateUtils.format(System.currentTimeMillis()));
        result.put("condition", null);
        if (desc != null && desc.size() != 0) {
            for(DescrptionModel descModel : desc) {
                try{
                    descModel.setText(report.globalization(descModel.getText()));
                }catch(Exception e) {
                    
                }
            }
            result.put("description", desc);
        }
        result.put("contentHeader", getContentHeader(report, queryMap));
        Map<String, String> preferenceMap = prepareUserPreferenceMap(Long.valueOf(queryMap.get("userId")));
        // add by fanzidong,判断数据条数是否超过限制
        if (isExceedLimit(datas)) {
            result.put("exceedLimit", true);
        } else {
            result.put("contentData", fetchReportNodeData(report, datas, preferenceMap));
        }
        return JSONObject.fromObject(result);
    }

    @Override
    public JSONObject getReportContent(String reportId, Map<String, String> queryMap) throws Exception {
        // querymap 查询条件处理
        prepareQueryMap(queryMap);
        Map<String, Object> result = new HashMap<>();
        Report report = fetchReportOrReloadReport(reportId);
        ReportDataService reportDataService = (ReportDataService) beanFactory.getBean(getReportServiceName(reportId));
        List<Object> datas = new ArrayList<>();
        List<DescrptionModel> desc = null;
        try {
            datas = reportTxDataService.txFetchReportData(reportDataService, queryMap);
            desc = reportTxDataService.fetchReportDescription(reportDataService, queryMap);
        } catch (Exception e) {
            logger.error("Fetch ReportDate Error ", e);
            datas = new ArrayList<>();
        }
        result.put("title", report.getTitle());
        result.put("combination", report.isCombination());
        result.put("time", DateUtils.format(System.currentTimeMillis()));
        result.put("condition", null);
        if (desc != null && desc.size() != 0) {
            for(DescrptionModel descModel : desc) {
                try{
                    descModel.setText(report.globalization(descModel.getText()));
                }catch(Exception e) {
                    
                }
            }
            result.put("description", desc);
        }
        result.put("contentHeader", getContentHeader(report, queryMap));
        Map<String, String> preferenceMap = prepareUserPreferenceMap(Long.valueOf(queryMap.get("userId")));
        // add by fanzidong,判断数据条数是否超过限制
        if (isExceedLimit(datas)) {
            result.put("exceedLimit", true);
        }
        result.put("contentData", fetchReportNodeData(report, datas, preferenceMap));
        return JSONObject.fromObject(result);
    }

    @Override
    public void generateTaskFile(ReportTask task) throws Exception {
        // 从task中解析出reportId和查询条件
        String reportId = task.getReportId();
        String taskName = task.getTaskName();
        Map<String, String> queryMap = buildQueryMap(task);
        Date statDate = new Date();
        JSONObject reportContent = getReportContent(reportId, queryMap);
        JSONArray queryCondition = getQueryConditionById(reportId);
        // 生成任务excel文件名
        String taskFileName = task.getTaskId() + "-" + fileNameFormatter.format(statDate);
        // 生成文件
        File rootFolder = new File(CORE_REPORT_TASK_FOLDER);
        if (!rootFolder.exists() || !rootFolder.isDirectory()) {
            rootFolder.mkdir();
        }
        File certainReportFolder = new File(CORE_REPORT_TASK_FOLDER + reportId);
        if (!certainReportFolder.exists() || !certainReportFolder.isDirectory()) {
            certainReportFolder.mkdir();
        }
        String filePath = CORE_REPORT_TASK_FOLDER + reportId + File.separator + taskFileName + ".xls";
        File generateFile = new File(filePath);
        OutputStream out = new FileOutputStream(generateFile);
        // 输出excel文件
        try {
            // 需要知道该报表导出excel是否是单层需要做分页的
            Boolean needPagination = false;
            Report report = fetchReportOrReloadReport(reportId);
            if (report.getPagination() != null && report.getPagination() > 0) {
                needPagination = true;
            }
            ReportExportUtil.writeContentToExcel(out, reportContent, queryCondition, queryMap, needPagination);
            reportInstanceService.addReportInstance(filePath, EXCEL, task);
        } catch (Exception e) {
            logger.debug("exportExcel method is error:{}", e);
            throw e;
        } finally {
            try {
                if (out != null) {
                    out.flush();
                }
                if (out != null) {
                    out.close();
                }
            } catch (Exception e) {
                logger.debug("something wrong with I/O:", e);
            }
        }
    }

    /**
     * 判断查询数据是否超出展示限制
     * 
     * @param datas
     * @return
     */
    private Boolean isExceedLimit(List<Object> datas) {
        if (datas == null) {
            return false;
        }
        // 数据总行数是否超出限制
        if (datas.size() > ReportCoreUtil.MAX_ROW) {
            return true;
        }
        // 如果数据只有一条，判断第一条数据是否为超限提示
        if (datas.size() == 1) {
            return datas.get(0) instanceof ExceedLimit;
        }
        return false;
    }

    private Map<String, String> buildQueryMap(ReportTask task) throws ParseException {
        Map<String, String> queryMap = new HashMap<String, String>();
        // 任务中存储的查询条件
        Map<String, Object> condition = task.getCondition();
        // 获取是周报表还是月报表，周报表还需要考虑是周一统计还是周日统计
        int cycleType = task.getCycleType();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        if (WEEK_REPORT.equals(cycleType)) { // 周报表，统计上周
            calendar.add(Calendar.DATE, -7);
            Integer weekStartDay = (Integer) condition.get("weekStartDay");
            calendar.setFirstDayOfWeek(weekStartDay);
            calendar.set(Calendar.DAY_OF_WEEK, weekStartDay);
            queryMap.put("startTime", stTimeFormatter_Daily.format(calendar.getTime()));
            queryMap.put("startTimeDisplay", stTimeFormatter_Daily.format(calendar.getTime()));
            calendar.add(Calendar.DATE, 6);
            queryMap.put("endTime", etTimeFormatter_Daily.format(calendar.getTime()));
            queryMap.put("endTimeDisplay", etTimeFormatter_Daily.format(calendar.getTime()));
        } else if (MONTH_REPORT.equals(cycleType)) { // 月报表，统计上个月
            calendar.add(Calendar.MONTH, -1);
            calendar.set(Calendar.DATE, 1);
            queryMap.put("startTime", stTimeFormatter_Daily.format(calendar.getTime()));
            queryMap.put("startTimeDisplay", stTimeFormatter_Daily.format(calendar.getTime()));
            calendar.roll(Calendar.DATE, -1);
            queryMap.put("endTime", etTimeFormatter_Daily.format(calendar.getTime()));
            queryMap.put("endTimeDisplay", etTimeFormatter_Daily.format(calendar.getTime()));
        }
        Iterator<?> it = condition.keySet().iterator();
        while (it.hasNext()) {
            String key = it.next().toString();
            queryMap.put(key, condition.get(key).toString());
        }
        Long userId = task.getUserId();
        queryMap.put("userId", userId.toString());
        queryMap.put("Authority", userService.getUserAuthorityViewName(userId));
        queryMap.put("topoAuthority", CurrentRequest.getUserAuthorityFolderName(userService.getUserEx(userId)));
        return queryMap;
    }

    private Map<String, String> prepareUserPreferenceMap(Long userId) {
        Map<String, String> map = new HashMap<String, String>();
        // 获取该用户的所有特性，并放入map
        List<UserPreferences> userPreferences = userPreferencesService.getAllUserPerferences(userId);
        for (UserPreferences userPreference : userPreferences) {
            map.put(userPreference.getName(), userPreference.getValue());
        }
        // 放入系统变量，温度单位，电平单位等
        map.put("tempUnit", (String) UnitConfigConstant.get("tempUnit"));
        map.put("elecLevelUnit", (String) UnitConfigConstant.get("elecLevelUnit"));
        return map;
    }

    @Override
    public String generateExcelFile(String reportId, Map<String, String> queryMap) throws Exception {
        Date statDate = new Date();
        JSONObject reportContent = getReportContent(reportId, queryMap);
        JSONArray reportCondition = getQueryConditionById(reportId);
        // 生成excel文件名
        String fileName = ReportTaskUtil.getString(reportContent.getString("title"), "report") + "-"
                + formatter_ymd_hms_SSS.format(statDate);
        logger.debug("generateExcelFile fileName=" + fileName);
        // 生成文件
        File rootFolder = new File(CORE_REPORT_FOLDER);
        if (!rootFolder.exists() || !rootFolder.isDirectory()) {
            rootFolder.mkdir();
        }
        String filePath = CORE_REPORT_FOLDER + fileName + ".xls";
        logger.debug("generateExcelFile filePath=" + filePath);
        File generateFile = new File(filePath);
        OutputStream out = new FileOutputStream(generateFile);
        // 输出excel文件
        try {
            // 需要知道该报表导出excel是否是单层需要做分页的
            Boolean needPagination = false;
            Report report = fetchReportOrReloadReport(reportId);
            if (report.getPagination() != null && report.getPagination() > 0) {
                needPagination = true;
            }
            ReportExportUtil.writeContentToExcel(out, reportContent, reportCondition, queryMap, needPagination);
            logger.debug("generateExcelFile after writeContentToExcel =" + filePath);
            return fileName;
        } catch (Exception e) {
            logger.debug("exportExcel method is error:{}", e);
            throw e;
        } finally {
            try {
                if (out != null) {
                    out.flush();
                    logger.debug("generateExcelFile after flush =" + filePath);
                }
                if (out != null) {
                    out.close();
                    logger.debug("generateExcelFile after close =" + filePath);
                }
            } catch (Exception e) {
                logger.debug("something wrong with I/O:", e);
            }
        }
    }

    @Override
    public void downloadExcelFile(String fileName) throws IOException {
        // 获取excel文件路径
        logger.debug("downloadExcelFile fileName =" + fileName);
        String filePath = CORE_REPORT_FOLDER + fileName + ".xls";
        logger.debug("downloadExcelFile filePath =" + filePath);
        FileInputStream fis = null;
        OutputStream out = null;
        File file = new File(filePath);
        try {
            fis = new FileInputStream(file);
            logger.debug("downloadExcelFile FileInputStream  =" + filePath);
            byte[] b = new byte[1024];
            ServletActionContext.getResponse().setContentType("application/x-download");
            // 转码文件名，解决中文名的问题
            fileName = new String(fileName.getBytes(FtpClientUtil.GBK), FtpClientUtil.ISO);
            logger.debug("downloadExcelFile fileName  =" + fileName);
            ServletActionContext.getResponse().addHeader("Content-Disposition",
                    "attachment;filename=" + fileName + ".xls");
            out = ServletActionContext.getResponse().getOutputStream();
            int i;
            while ((i = fis.read(b)) > 0) {
                out.write(b, 0, i);
            }
        } catch (IOException e) {
            logger.debug("", e);
            throw e;
        } finally {
            // 关闭流,并删除服务器端临时文件
            try {
                if (out != null)
                    out.close();
                if (fis != null)
                    fis.close();
            } catch (IOException e) {
                logger.debug("", e);
            } finally {
                // 删除文件
                logger.debug("downloadExcelFile delete  =" + filePath);
                file.delete();
            }
        }
    }

    private void prepareQueryMap(Map<String, String> queryMap) {
        if (queryMap == null) {
            return;
        }
        // 放入当前的语言版本
        SystemConstants sc = SystemConstants.getInstance();
        String lang = sc.getStringParam("language", "zh_CN");
        queryMap.put("lang", lang);
        if (queryMap.containsKey("folder")) {
            List<Long> folderIds = new ArrayList<Long>();
            for (String id : Arrays.asList(queryMap.get("folder").split(","))) {
                folderIds.add(Long.parseLong(id));
            }
            List<Long> authFolderIds = getAuthFolderIds(folderIds);
            String result = "";
            for (int i = 0; i < authFolderIds.size(); i++) {
                if (i + 1 == authFolderIds.size()) {
                    result += authFolderIds.get(i).toString();
                } else {
                    result += authFolderIds.get(i).toString() + ",";
                }
            }
            queryMap.put("expandFolderId", result);
        }
    }

    private List<Long> getAuthFolderIds(List<Long> folderIds) {
        return statReportDao.getAuthFolderIds(folderIds);
    }

    private String getReportServiceName(String reportId) {
        StringBuilder serviceName = new StringBuilder();
        serviceName.append(reportId.substring(0, 1).toLowerCase()).append(reportId.substring(1))
                .append("ReportService");
        return serviceName.toString();
    }

    public List<ReportColumnReferences> getReportColumns(String reportId) {
        Report report = fetchReportOrReloadReport(reportId);
        List<ReportColumnReferences> allColumns = report.getColumnReferences();
        return allColumns;
    }

    private Map<String, Object> getContentHeader(Report report, Map<String, String> queryMap) {
        Map<String, Object> result = new HashMap<>();
        List<ReportColumnReferences> allColumns = report.getColumnReferences();
        List<ReportColumnReferences> retColumns = new ArrayList<ReportColumnReferences>();
        if (queryMap.containsKey(ReportColumnReferences.SHOWCOLUMNS)) {
            // 如果有展示列属性
            String showColumnStr = queryMap.get(ReportColumnReferences.SHOWCOLUMNS);
            if (showColumnStr != "") {
                List<String> shownColumns = Arrays.asList(showColumnStr.split(","));
                for (ReportColumnReferences column : allColumns) {
                    if (shownColumns.contains(column.getId())) {
                        retColumns.add(column);
                    }
                }
            }
        } else {
            retColumns = allColumns;
        }
        result.put("columns", retColumns);
        return result;
    }

    @Override
    public List<SelectModel> fetchSelectConditionList(String reportId, String conditionId) {
        ReportDataService reportDataService = (ReportDataService) beanFactory.getBean(getReportServiceName(reportId));
        List<SelectModel> selectModels = new ArrayList<SelectModel>();
        try {
            selectModels = reportTxDataService.fetchSelectConditionList(reportDataService, conditionId);
            // 国际化处理
            translateSelectModel(selectModels);
        } catch (Exception e) {
            logger.error("Fetch ReportDate condition error", e);
        }
        return selectModels;
    }

    private void translateSelectModel(List<SelectModel> selectModels) {
        ResourceManager resourceManager = ResourceManager.getResourceManager("com.topvision.ems.report.resources");
        for (SelectModel selectModel : selectModels) {
            selectModel.setName(resourceManager.getNotNullString(selectModel.getName()));
        }
    }

    /**
     * @return the reportCollection
     */
    public Map<String, Report> getReportCollection() {
        return allReportCollection;
    }

    /**
     * @param reportCollection
     *            the reportCollection to set
     */
    public void setReportCollection(Map<String, Report> reportCollection) {
        this.allReportCollection = reportCollection;
    }

    /**
     * @return the xmlPath
     */
    public List<String> getXmlPath() {
        return xmlPath;
    }

    /**
     * @param xmlPath
     *            the xmlPath to set
     */
    public void setXmlPath(List<String> xmlPath) {
        this.xmlPath = xmlPath;
    }

    /**
     * @return the reportCoreDao
     */
    public ReportCoreDao getReportCoreDao() {
        return reportCoreDao;
    }

    /**
     * @param reportCoreDao
     *            the reportCoreDao to set
     */
    public void setReportCoreDao(ReportCoreDao reportCoreDao) {
        this.reportCoreDao = reportCoreDao;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.springframework.beans.factory.BeanFactoryAware#setBeanFactory(org.springframework.beans
     * .factory.BeanFactory)
     */
    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.report.service.ReportCoreService#loadAvailableReports()
     */
    @Override
    public List<Report> loadAvailableReports() {
        return topLevelReports;
    }

    /**
     * @return the reportXmlResources
     */
    public Resource[] getReportXmlResources() {
        return reportXmlResources;
    }

    /**
     * @param reportXmlResources
     *            the reportXmlResources to set
     */
    public void setReportXmlResources(Resource[] reportXmlResources) {
        this.reportXmlResources = reportXmlResources;
    }

    @SuppressWarnings("unused")
    private String getReportJarModule(String reportName) {
        int prefix = reportName.indexOf(REPORT_PREFIX) + REPORT_PREFIX.length();
        int suffix = reportName.substring(prefix).indexOf(REPORT_SUFFIX);
        return reportName.substring(prefix, prefix + suffix);
    }

    /**
     * 判断报表是否被license支持， 需要模块和工程同时支持
     * 
     * @param report
     * @return
     */
    private Boolean isReportSupported(Report report) {
        // 获取该报表的模块及工程
        String module = report.getModule();
        String project = report.getProject();
        String id = report.getId();
        // 判断都移到license，具体看license中的注释
        return licenseIf.isSupportReport(module, project, id);
    }

    @Override
    public List<Report> loadAllReports() {
        return allTopReports;
    }

    @Override
    public Report fetchReportById(String reportId) {
        return allTopReportCollection.get(reportId);
    }

}
