/***********************************************************************
 * $Id: CmReportCreatorImpl.java,v1.0 2013-10-29 下午4:06:06 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.report.cm.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.cmc.facade.domain.CmAttribute;
import com.topvision.ems.cmc.report.cm.dao.CmReportDao;
import com.topvision.ems.cmc.report.cm.service.CmReportCreator;
import com.topvision.ems.cmc.report.domain.CmDailyNumStaticReport;
import com.topvision.ems.cmc.util.ResourcesUtil;
import com.topvision.ems.network.domain.FolderCategory;
import com.topvision.ems.network.domain.TopoFolder;
import com.topvision.ems.network.service.TopologyService;
import com.topvision.ems.report.domain.ReportTask;
import com.topvision.ems.report.service.ReportInstanceService;
import com.topvision.ems.report.util.ReportExportUtil;
import com.topvision.ems.report.util.ReportTaskUtil;
import com.topvision.ems.template.service.EntityTypeService;
import com.topvision.framework.common.MacUtils;
import com.topvision.framework.service.BaseService;
import com.topvision.platform.service.UserPreferencesService;
import com.topvision.platform.util.CurrentRequest;

/**
 * @author haojie
 * @created @2013-10-29-下午4:06:06
 * 
 */
@Service("cmReportCreator")
public class CmReportCreatorImpl extends BaseService implements CmReportCreator {
    @Autowired
    private CmReportDao cmReportDao;
    @Autowired
    private ReportInstanceService reportInstanceService;
    @Autowired
    private UserPreferencesService userPreferencesService;
    @Autowired
    private TopologyService topologyService;
    @Autowired
    private EntityTypeService entityTypeService;

    @Override
    public void bulidReport(ReportTask task) {
        Date date = new Date();
        Map<String, Object> condition = task.getCondition();
        Map<String, Object> queryMap = new HashMap<String, Object>();
        queryMap.put("sortName", condition.get("sortName"));
        if ("folders".equals(condition.get("range")) && condition.get("rangeDetail") != "") {
            queryMap.put("folderId", condition.get("rangeDetail"));
        } else if ("olts".equals(condition.get("range")) && condition.get("rangeDetail") != "") {
            queryMap.put("oltId", condition.get("rangeDetail"));
        } else if ("cmcs".equals(condition.get("range")) && condition.get("rangeDetail") != "") {
            queryMap.put("cmcId", condition.get("rangeDetail"));
        }
        List<CmAttribute> cmAttributes = statCmReport(queryMap);
        for (CmAttribute cmAttribute : cmAttributes) {
            if (cmAttribute.isCmOnline()) {
                cmAttribute.setStatusVlaueString("online");
            } else {
                cmAttribute.setStatusVlaueString("offline");
            }
            // add by fanzidong ,格式化MAC地址
            String macRule = userPreferencesService.getUserPreference(task.getUserId(), "macDisplayStyle").getValue();
            String formatedMac = MacUtils.convertMacToDisplayFormat(cmAttribute.getStatusMacAddress(), macRule);
            cmAttribute.setStatusMacAddress(formatedMac);
        }

        Map<String, Boolean> columnDisable = new HashMap<String, Boolean>();
        columnDisable.put("cmAliasDisable", Boolean.valueOf(condition.get("cmAliasDisable").toString()));
        columnDisable.put("cmClassifiedDisable", Boolean.valueOf(condition.get("cmClassifiedDisable").toString()));
        if (task.isExcelEnabled()) {
            String filePath = exportAsExcelFromTask(cmAttributes, columnDisable, date, task);
            reportInstanceService.addReportInstance(filePath, EXCEL, task);
        }
    }

    private String exportAsExcelFromTask(List<CmAttribute> deviceListItems, Map<String, Boolean> columnDisable,
            Date date, ReportTask task) {
        String filePath = ReportExportUtil.getExportUrl(task, EXCEL);
        File file = new File(filePath);
        try {
            file.createNewFile();
            WritableWorkbook workbook = Workbook.createWorkbook(file);
            createCmReportExcelFile(deviceListItems, columnDisable, workbook, date);
        } catch (IOException e) {
            logger.debug("something wrong with I/O:", e);
        }
        return filePath;
    }

    @Override
    public List<CmAttribute> statCmReport(Map<String, Object> map) {
        return cmReportDao.getCmBasicInfoList(map);
    }

    @Override
    public void exportCmReportToExcel(List<CmAttribute> cmAttributes, Map<String, Boolean> columnDisable, Date statDate) {
        OutputStream out = null;
        FileInputStream fis = null;
        ServletActionContext.getResponse().setContentType("application/msexcel");

        String fileName;
        try {
            fileName = URLEncoder.encode(ReportTaskUtil.getString("report.cmDeviceList", "report") + "-"
                    + fileNameFormatter.format(statDate) + ".xls", "UTF-8");
            ServletActionContext.getResponse().addHeader("Content-Disposition", "attachment;filename=" + fileName);
            out = ServletActionContext.getResponse().getOutputStream();
            WritableWorkbook workbook = Workbook.createWorkbook(out);
            createCmReportExcelFile(cmAttributes, columnDisable, workbook, statDate);
        } catch (UnsupportedEncodingException e) {
            logger.debug("unsupported Encoding Exception:", e);
        } catch (IOException e) {
            logger.debug("something wrong with I/O:", e);
        } finally {
            try {
                if (fis != null) {
                    fis.close();
                }
                if (out != null) {
                    out.flush();
                }
                if (out != null) {
                    out.close();
                }
            } catch (Exception e) {
                logger.debug("close fileStream error:{}", e);
            }
        }

    }

    private void createCmReportExcelFile(List<CmAttribute> cmAttributes, Map<String, Boolean> columnDisable,
            WritableWorkbook workbook, Date date) {
        Label label;
        // 计算出应该分多少个sheet(每个sheet放60000个CM)
        Integer sheetNum = cmAttributes.size() / 60000 + 1, lastPageCm = cmAttributes.size() % 60000;
        CmAttribute cmAttribute = null;
        try {
            // 前面都是满配60000个
            int j = 0;
            for (; j < sheetNum - 1; j++) {
                // 初始化好当前sheet
                WritableSheet sheet = workbook.createSheet("Sheet_" + j, j);
                WritableCellFormat titleCellFormat = ReportTaskUtil.getTitleCellFormat();
                WritableCellFormat contentCellFormat = ReportTaskUtil.getContentCellFormat();

                label = new Label(0, 2, "#", titleCellFormat);
                sheet.addCell(label);
                label = new Label(1, 2, "IP", titleCellFormat);
                sheet.addCell(label);
                label = new Label(2, 2, ReportTaskUtil.getString("label.mac", "report"), titleCellFormat);
                sheet.addCell(label);
                label = new Label(3, 2, ReportTaskUtil.getString("report.status", "report"), titleCellFormat);
                sheet.addCell(label);
                label = new Label(4, 2, ReportTaskUtil.getString("report.ccmtsAlias", "report"), titleCellFormat);
                sheet.addCell(label);
                int titleCls = 5;
                if (columnDisable.get("cmAliasDisable")) {
                    label = new Label(titleCls++, 2, ReportTaskUtil.getString("report.aliasOrLocation", "report"),
                            titleCellFormat);
                    sheet.addCell(label);
                }
                if (columnDisable.get("cmClassifiedDisable")) {
                    label = new Label(titleCls++, 2, ReportTaskUtil.getString("report.usageOrCat", "report"),
                            titleCellFormat);
                    sheet.addCell(label);
                }

                sheet.setColumnView(0, 10);
                for (int i = 1; i < 4; i++) {
                    sheet.setColumnView(i, 25);
                }
                for (int i = 4; i < titleCls; i++) {
                    sheet.setColumnView(i, 40);
                }
                ReportTaskUtil.formatExcelHeader(sheet, ReportTaskUtil.getString("report.cmDeviceList", "report"),
                        date, titleCls);

                Integer rowNum = 3;
                Integer rowNumView = 1;

                // 填充当前sheet
                for (int cmNum = 0; cmNum < 60000; cmNum++) {
                    cmAttribute = cmAttributes.get(j * 60000 + cmNum);
                    label = new Label(0, rowNum, rowNumView.toString(), contentCellFormat);
                    sheet.addCell(label);
                    label = new Label(1, rowNum, cmAttribute.getStatusInetAddress(), contentCellFormat);
                    sheet.addCell(label);
                    label = new Label(2, rowNum, cmAttribute.getStatusMacAddress(), contentCellFormat);
                    sheet.addCell(label);
                    label = new Label(3, rowNum, cmAttribute.getStatusVlaueString(), contentCellFormat);
                    sheet.addCell(label);
                    label = new Label(4, rowNum, cmAttribute.getCmcAlias(), contentCellFormat);
                    sheet.addCell(label);
                    int cls = 5;
                    if (columnDisable.get("cmAliasDisable")) {
                        label = new Label(cls++, rowNum, cmAttribute.getCmAlias(), contentCellFormat);
                        sheet.addCell(label);
                    }
                    if (columnDisable.get("cmClassifiedDisable")) {
                        label = new Label(cls++, rowNum, cmAttribute.getCmClassified(), contentCellFormat);
                        sheet.addCell(label);
                    }
                    rowNum++;
                    rowNumView++;
                }
            }
            // 最后一页填充剩下的cm
            // 初始化好当前sheet
            WritableSheet sheet = workbook.createSheet("Sheet_" + j, j);
            WritableCellFormat titleCellFormat = ReportTaskUtil.getTitleCellFormat();
            WritableCellFormat contentCellFormat = ReportTaskUtil.getContentCellFormat();

            label = new Label(0, 2, "#", titleCellFormat);
            sheet.addCell(label);
            label = new Label(1, 2, "IP", titleCellFormat);
            sheet.addCell(label);
            label = new Label(2, 2, ReportTaskUtil.getString("label.mac", "report"), titleCellFormat);
            sheet.addCell(label);
            label = new Label(3, 2, ReportTaskUtil.getString("report.status", "report"), titleCellFormat);
            sheet.addCell(label);
            label = new Label(4, 2, ReportTaskUtil.getString("report.ccmtsAlias", "report"), titleCellFormat);
            sheet.addCell(label);
            int titleCls = 5;
            if (columnDisable.get("cmAliasDisable")) {
                label = new Label(titleCls++, 2, ReportTaskUtil.getString("report.aliasOrLocation", "report"),
                        titleCellFormat);
                sheet.addCell(label);
            }
            if (columnDisable.get("cmClassifiedDisable")) {
                label = new Label(titleCls++, 2, ReportTaskUtil.getString("report.usageOrCat", "report"),
                        titleCellFormat);
                sheet.addCell(label);
            }

            sheet.setColumnView(0, 10);
            for (int i = 1; i < 4; i++) {
                sheet.setColumnView(i, 25);
            }
            for (int i = 4; i < titleCls; i++) {
                sheet.setColumnView(i, 40);
            }
            ReportTaskUtil.formatExcelHeader(sheet, ReportTaskUtil.getString("report.cmDeviceList", "report"), date,
                    titleCls);

            Integer rowNum = 3;
            Integer rowNumView = 1;

            // 填充当前sheet
            for (int cmNum = 0; cmNum < lastPageCm; cmNum++) {
                cmAttribute = cmAttributes.get(j * 60000 + cmNum);
                label = new Label(0, rowNum, rowNumView.toString(), contentCellFormat);
                sheet.addCell(label);
                label = new Label(1, rowNum, cmAttribute.getStatusInetAddress(), contentCellFormat);
                sheet.addCell(label);
                label = new Label(2, rowNum, cmAttribute.getStatusMacAddress(), contentCellFormat);
                sheet.addCell(label);
                label = new Label(3, rowNum, cmAttribute.getStatusVlaueString(), contentCellFormat);
                sheet.addCell(label);
                label = new Label(4, rowNum, cmAttribute.getCmcAlias(), contentCellFormat);
                sheet.addCell(label);
                int cls = 5;
                if (columnDisable.get("cmAliasDisable")) {
                    label = new Label(cls++, rowNum, cmAttribute.getCmAlias(), contentCellFormat);
                    sheet.addCell(label);
                }
                if (columnDisable.get("cmClassifiedDisable")) {
                    label = new Label(cls++, rowNum, cmAttribute.getCmClassified(), contentCellFormat);
                    sheet.addCell(label);
                }
                rowNum++;
                rowNumView++;
            }

            workbook.write();
            workbook.close();
        } catch (Exception e) {
            logger.debug("createCmReportExcelFile method error:{}", e);
        }
    }

    @Override
    public JSONObject loadFolderOltCmcLists() {
        JSONObject json = new JSONObject();
        // 加载该用户所能查看的所有地域
        List<TopoFolder> list = topologyService.loadMyTopoFolder(CurrentRequest.getCurrentUser().getUserId(),
                FolderCategory.CLASS_NETWORK);
        JSONArray jsonArray = new JSONArray();
        for (TopoFolder folder : list) {
            JSONObject folderJsonObject = new JSONObject();
            folderJsonObject.put("id", folder.getFolderId());
            folderJsonObject.put("name", ResourcesUtil.getString(folder.getName().toString()));
            jsonArray.add(folderJsonObject);
        }
        List<Map<String, String>> olts = cmReportDao.loadIdAndNamePairsFromTable(CmDailyNumStaticReport.ENTITY,
                entityTypeService.getOltType());
        List<Map<String, String>> cmcs = cmReportDao.loadIdAndNamePairsFromTable(CmDailyNumStaticReport.ENTITY,
                entityTypeService.getCcmtsType());
        List<Map<String, String>> cmts = cmReportDao.loadIdAndNamePairsFromTable(CmDailyNumStaticReport.ENTITY,
                entityTypeService.getCmtsType());
        json.put("folders", jsonArray);
        json.put("olts", olts);
        json.put("cmcs", cmcs);
        json.put("cmts", cmts);
        return json;
    }

}
