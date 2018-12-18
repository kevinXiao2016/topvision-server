/***********************************************************************
 * $Id: CcmtsChannelListReportCreatorImpl.java,v1.0 2013-10-29 上午8:46:51 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.report.ccmtschannellist.service.impl;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.cmc.report.ccmtschannellist.dao.CcmtsChannelListReportDao;
import com.topvision.ems.cmc.report.ccmtschannellist.service.CcmtsChannelListReportCreator;
import com.topvision.ems.cmc.report.domain.CcmtsChannelReportCCMTS;
import com.topvision.ems.cmc.report.domain.CcmtsChannelReportLocation;
import com.topvision.ems.cmc.report.domain.CcmtsChannelReportOLT;
import com.topvision.ems.cmc.report.domain.CcmtsChannelReportPON;
import com.topvision.ems.cmc.report.domain.CcmtsChannelUsageDetail;
import com.topvision.ems.cmc.util.ResourcesUtil;
import com.topvision.ems.report.domain.ReportTask;
import com.topvision.ems.report.domain.TopoEntityStastic;
import com.topvision.ems.report.service.ReportInstanceService;
import com.topvision.ems.report.service.StatReportService;
import com.topvision.ems.report.util.ReportExportUtil;
import com.topvision.ems.report.util.ReportTaskUtil;
import com.topvision.ems.report.util.ReportUtils;
import com.topvision.ems.template.service.EntityTypeService;
import com.topvision.framework.common.MacUtils;
import com.topvision.framework.service.BaseService;
import com.topvision.platform.service.UserPreferencesService;

/**
 * @author haojie
 * @created @2013-10-29-上午8:46:51
 * 
 */
@Service("ccmtsChannelListReportCreator")
public class CcmtsChannelListReportCreatorImpl extends BaseService implements CcmtsChannelListReportCreator {
    @Autowired
    private CcmtsChannelListReportDao ccmtsChannelListReportDao;
    @Autowired
    private StatReportService statReportService;
    @Autowired
    private ReportInstanceService reportInstanceService;
    @Autowired
    private UserPreferencesService userPreferencesService;

    @Autowired
    private EntityTypeService entityTypeService;

    public static final String[] UP_MODULE_NAMES = { "", "QPSK-Fair-Scdma", "QAM16-Fair-Scdma", "QAM64-Fair-Scdma",
            "QAM256-Fair-Scdma", "QPSK-Good-Scdma", "QAM16-Good-Scdma", "QAM64-Good-Scdma", "QAM256-Good-Scdma",
            "QAM64-Best-Scdma", "QAM256-Best-Scdma", "QPSK-Atdma", "QAM16-Atdma", "QAM64-Atdma", "QAM256-Atdma",
            "QAM64-Lowlatency-Scdma", "QAM256-Lowlatency-Scdma", "QAM32-Good-Scdma", "QAM32-Atdma" };
    public static final String[] DOWN_MODULE_NAMES = { "", "", "QAM1024", "QAM64", "QAM256" };

    @Override
    public void bulidReport(ReportTask task) {
        Date date = new Date();
        Map<String, Object> condition = task.getCondition();
        Map<String, Object> queryMap = new HashMap<String, Object>();
        queryMap.put("userId", task.getUserId());
        List<CcmtsChannelReportLocation> locations = this.getCcmtsChannelUsageReport(queryMap);
        // add by fanzidong,格式化MAC地址
        try {
            String displayRule = userPreferencesService.getUserPreference(task.getUserId(), "macDisplayStyle")
                    .getValue();
            for (CcmtsChannelReportLocation location : locations) {
                if (location == null) {
                    continue;
                }
                for (CcmtsChannelReportOLT channelReportOLT : location.getCcmtsChannelReportOLT()) {
                    if (channelReportOLT == null) {
                        continue;
                    }
                    for (CcmtsChannelReportPON channelReportPON : channelReportOLT.getCcmtsChannelReportPON()) {
                        if (channelReportPON == null) {
                            continue;
                        }
                        for (CcmtsChannelReportCCMTS channelReportCCMTS : channelReportPON.getCcmtsChannelReportCCMTS()) {
                            if (channelReportCCMTS == null) {
                                continue;
                            }
                            String formatedMac = MacUtils.convertMacToDisplayFormat(channelReportCCMTS.getCcmtsMAC(),
                                    displayRule);
                            channelReportCCMTS.setCcmtsMAC(formatedMac);
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.debug("error happend, maybe cannot get userId:" + e.getMessage());
        }
        Map<String, Boolean> columnDisable = new HashMap<String, Boolean>();
        columnDisable.put("upModuleDisplayable", Boolean.valueOf(condition.get("upModuleDisplayable").toString()));
        columnDisable.put("downModuleDisplayable", Boolean.valueOf(condition.get("downModuleDisplayable").toString()));
        if (task.isExcelEnabled()) {
            String filePath = exportAsExcelFromTask(locations, columnDisable, date, task);
            reportInstanceService.addReportInstance(filePath, EXCEL, task);
        }
    }

    @Override
    public List<CcmtsChannelReportLocation> getCcmtsChannelUsageReport(Map<String, Object> queryMap) {
        Long oltType = entityTypeService.getOltType();
        Long ccWithOutAgentType = entityTypeService.getCcmtswithoutagentType();
        queryMap.put("oltType", oltType);
        queryMap.put("ccWithOutAgentType", ccWithOutAgentType);
        List<CcmtsChannelUsageDetail> reportList = ccmtsChannelListReportDao.getCcmtsChannelUsageReport(queryMap);
        // 将列表设置为4个List的树形结构
        // 第一级:地域，第二级：OLT，第三级：PON，第四级：CCMTS
        // 每一级存储该对象的相关属性如Id，Name，和下一级对象的List
        // 同时在每一级存储该对象的rowSpan以方便前台页面合并单元格
        List<CcmtsChannelReportLocation> locations = new ArrayList<CcmtsChannelReportLocation>();
        List<TopoEntityStastic> relates = new ArrayList<TopoEntityStastic>();
        relates = statReportService.getTopoEntityRelation(entityTypeService.getOltType(), queryMap);
        for (TopoEntityStastic t : relates) {
            String oltId = String.valueOf(t.getEntityId());
            for (CcmtsChannelUsageDetail ccLine : reportList) {
                if (ccLine.getOltId().equals(oltId)) {
                    String locationId = String.valueOf(t.getFolderId());
                    CcmtsChannelReportLocation location = null;
                    boolean locationExsit = false;
                    for (CcmtsChannelReportLocation loc : locations) {
                        if (loc.getLocationId().equals(locationId)) {
                            location = loc;
                            location.setRowSpan(location.getRowSpan() + 1);
                            locationExsit = true;
                            break;
                        }
                    }
                    if (!locationExsit) {
                        location = new CcmtsChannelReportLocation();
                        location.setRowSpan(1);
                        location.setLocationId(locationId);
                        location.setLocationName(ResourcesUtil.getString(t.getFolderName()));
                        location.setCcmtsChannelReportOLT(new ArrayList<CcmtsChannelReportOLT>());
                        locations.add(location);
                    }
                    CcmtsChannelReportOLT olt = null;
                    boolean oltExist = false;
                    List<CcmtsChannelReportOLT> olts = location.getCcmtsChannelReportOLT();
                    for (CcmtsChannelReportOLT o : olts) {
                        if (o.getOltId().equals(oltId)) {
                            olt = o;
                            olt.setRowSpan(olt.getRowSpan() + 1);
                            oltExist = true;
                            break;
                        }
                    }
                    if (!oltExist) {
                        olt = new CcmtsChannelReportOLT();
                        olt.setRowSpan(1);
                        olt.setOltId(oltId);
                        olt.setOltIp(t.getEntityIp());
                        olt.setOltName(t.getEntityName());
                        olt.setCcmtsChannelReportPON(new ArrayList<CcmtsChannelReportPON>());
                        olts.add(olt);
                    }
                    String ponId = ccLine.getPonId();
                    CcmtsChannelReportPON pon = null;
                    boolean ponExist = false;
                    List<CcmtsChannelReportPON> pons = olt.getCcmtsChannelReportPON();
                    for (CcmtsChannelReportPON p : pons) {
                        if (p.getPonId().equals(ponId)) {
                            pon = p;
                            pon.setRowSpan(pon.getRowSpan() + 1);
                            ponExist = true;
                            break;
                        }
                    }
                    if (!ponExist) {
                        pon = new CcmtsChannelReportPON();
                        pon.setRowSpan(1);
                        pon.setPonId(ponId);
                        pon.setPonIndex(ReportUtils.getPonIndexStr(Long.valueOf(ccLine.getPonIndex())));
                        pon.setCcmtsChannelReportCCMTS(new ArrayList<CcmtsChannelReportCCMTS>());
                        pons.add(pon);
                    }
                    String ccmtsId = ccLine.getCmcId();
                    CcmtsChannelReportCCMTS ccmts = null;
                    boolean ccmtsExist = false;
                    List<CcmtsChannelReportCCMTS> ccs = pon.getCcmtsChannelReportCCMTS();
                    for (CcmtsChannelReportCCMTS c : ccs) {
                        if (c.getCmcId().equals(ccmtsId)) {
                            ccmts = c;
                            ccmtsExist = true;
                            break;
                        }
                    }
                    if (!ccmtsExist) {
                        ccmts = new CcmtsChannelReportCCMTS();
                        ccmts.setCmcId(ccLine.getCmcId());
                        ccmts.setCcmtsName(ccLine.getCcmtsName());
                        ccmts.setCcmtsMAC(ccLine.getCcmtsMAC());
                        String downChannelModule = this.getDownMoldueName(ccLine.getCcmtsDownChannelModule());
                        ccmts.setCcmtsDownChannelModule(downChannelModule);
                        String upChannelModule = this.getUpModuleName(ccLine.getCcmtsUpChannelModule());
                        ccmts.setCcmtsUpChannelModule(upChannelModule);
                        ccmts.setCcmtsUpChannelNum(ccLine.getCcmtsUpChannelNum());
                        ccmts.setCcmtsDownChannelNum(ccLine.getCcmtsDownChannelNum());
                        ccs.add(ccmts);
                    }
                }
            }
        }
        return locations;
    }

    @Override
    public void exportCcmtsDeviceListReportToExcel(List<CcmtsChannelReportLocation> locations, Date statDate) {
        OutputStream out = null;
        ServletActionContext.getResponse().setContentType("application/msexcel");
        String fileName;
        Map<String, Boolean> columnDisable = new HashMap<String, Boolean>();
        columnDisable.put("upModuleDisplayable", Boolean.valueOf(true));
        columnDisable.put("downModuleDisplayable", Boolean.valueOf(true));
        try {
            fileName = URLEncoder.encode(ReportTaskUtil.getString("report.ccmtsChannelListReport", "report") + "-"
                    + fileNameFormatter.format(statDate) + ".xls", "UTF-8");
            ServletActionContext.getResponse().addHeader("Content-Disposition", "attachment;filename=" + fileName);
            out = ServletActionContext.getResponse().getOutputStream();
            WritableWorkbook workbook = Workbook.createWorkbook(out);
            createCcmtsDeviceListReportExcelFile(locations, columnDisable, workbook, statDate);
        } catch (UnsupportedEncodingException e) {
            logger.debug("unsupported Encoding Exception:", e);
            e.printStackTrace();
        } catch (IOException e) {
            logger.debug("something wrong with I/O:", e);
            e.printStackTrace();
        } finally {
            try {
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

    /**
     * 创建CCMTS信道使用情况的EXCEL文件
     * 
     * @author YangYi add
     * @created @2013-9-14
     * @param locations
     * @param columnDisable
     * @param workbook
     * @param date
     */
    public void createCcmtsDeviceListReportExcelFile(List<CcmtsChannelReportLocation> locations,
            Map<String, Boolean> columnDisable, WritableWorkbook workbook, Date date) {
        Label label;
        WritableSheet sheet = workbook.createSheet("Sheet1", 0);
        try {
            WritableCellFormat titleCellFormat = ReportTaskUtil.getTitleCellFormat();
            WritableCellFormat contentCellFormat = ReportTaskUtil.getContentCellFormat();
            // 设置单元格宽度
            sheet.setColumnView(0, 12);
            sheet.setColumnView(1, 12);
            sheet.setColumnView(2, 18);
            sheet.setColumnView(3, 12);
            sheet.setColumnView(4, 20);
            sheet.setColumnView(5, 20);
            sheet.setColumnView(6, 22);
            sheet.setColumnView(7, 22);
            int colNum = 8;
            if (columnDisable.get("upModuleDisplayable")) {
                colNum++;
                sheet.setColumnView(8, 25);
            }
            if (columnDisable.get("downModuleDisplayable")) {
                sheet.setColumnView(9, 25);
                colNum++;
            }
            // 设置表头
            ReportTaskUtil.formatExcelHeader(sheet,
                    ReportTaskUtil.getString("report.ccmtsChannelListReport", "report"), date, colNum);

            label = new Label(0, 2, ReportTaskUtil.getString("report.folder", "report"));
            label.setCellFormat(titleCellFormat);
            sheet.addCell(label);

            label = new Label(1, 2, ReportTaskUtil.getString("report.oltName", "report"));
            label.setCellFormat(titleCellFormat);
            sheet.addCell(label);

            label = new Label(2, 2, "OLT_IP");
            label.setCellFormat(titleCellFormat);
            sheet.addCell(label);

            label = new Label(3, 2, ReportTaskUtil.getString("report.pon", "report"));
            label.setCellFormat(titleCellFormat);
            sheet.addCell(label);

            label = new Label(4, 2, ReportTaskUtil.getString("report.ccmtsName", "report"));
            label.setCellFormat(titleCellFormat);
            sheet.addCell(label);

            label = new Label(5, 2, "CC_MAC");
            label.setCellFormat(titleCellFormat);
            sheet.addCell(label);

            label = new Label(6, 2, ReportTaskUtil.getString("report.ccmtsUpChannelNum", "report"));
            label.setCellFormat(titleCellFormat);
            sheet.addCell(label);

            label = new Label(7, 2, ReportTaskUtil.getString("report.ccmtsDownChannelNum", "report"));
            label.setCellFormat(titleCellFormat);
            sheet.addCell(label);

            if (columnDisable.get("upModuleDisplayable")) {
                label = new Label(8, 2, ReportTaskUtil.getString("report.ccmtsUpChannelModule", "report"));
                label.setCellFormat(titleCellFormat);
                sheet.addCell(label);
            }

            if (columnDisable.get("downModuleDisplayable")) {
                label = new Label(9, 2, ReportTaskUtil.getString("report.ccmtsDownChannelModule", "report"));
                label.setCellFormat(titleCellFormat);
                sheet.addCell(label);
            }

            int rowNum = 3;

            for (int i = 0; i < locations.size(); i++) {
                String loactionName = locations.get(i).getLocationName();
                List<CcmtsChannelReportOLT> olts = locations.get(i).getCcmtsChannelReportOLT();

                label = new Label(0, rowNum, loactionName);
                label.setCellFormat(contentCellFormat);
                sheet.addCell(label);
                sheet.mergeCells(0, rowNum, 0, rowNum + locations.get(i).getRowSpan() - 1);

                for (int j = 0; j < olts.size(); j++) {
                    String oltName = olts.get(j).getOltName();
                    String oltIp = olts.get(j).getOltIp();
                    List<CcmtsChannelReportPON> pons = olts.get(j).getCcmtsChannelReportPON();

                    label = new Label(1, rowNum, oltName);
                    label.setCellFormat(contentCellFormat);
                    sheet.addCell(label);
                    sheet.mergeCells(1, rowNum, 1, rowNum + olts.get(j).getRowSpan() - 1);

                    label = new Label(2, rowNum, oltIp);
                    label.setCellFormat(contentCellFormat);
                    sheet.addCell(label);
                    sheet.mergeCells(2, rowNum, 2, rowNum + olts.get(j).getRowSpan() - 1);

                    for (int k = 0; k < pons.size(); k++) {
                        String ponIndex = pons.get(k).getPonIndex();
                        List<CcmtsChannelReportCCMTS> ccs = pons.get(k).getCcmtsChannelReportCCMTS();

                        label = new Label(3, rowNum, ponIndex);
                        label.setCellFormat(contentCellFormat);
                        sheet.addCell(label);
                        sheet.mergeCells(3, rowNum, 3, rowNum + pons.get(k).getRowSpan() - 1);

                        for (int l = 0; l < ccs.size(); l++) {

                            label = new Label(4, rowNum, ccs.get(l).getCcmtsName());
                            label.setCellFormat(contentCellFormat);
                            sheet.addCell(label);

                            label = new Label(5, rowNum, ccs.get(l).getCcmtsMAC());
                            label.setCellFormat(contentCellFormat);
                            sheet.addCell(label);

                            label = new Label(6, rowNum, ccs.get(l).getCcmtsUpChannelNum());
                            label.setCellFormat(contentCellFormat);
                            sheet.addCell(label);

                            label = new Label(7, rowNum, ccs.get(l).getCcmtsDownChannelNum());
                            label.setCellFormat(contentCellFormat);
                            sheet.addCell(label);

                            if (columnDisable.get("upModuleDisplayable")) {
                                label = new Label(8, rowNum, ccs.get(l).getCcmtsUpChannelModule());
                                label.setCellFormat(contentCellFormat);
                                sheet.addCell(label);
                            }

                            if (columnDisable.get("downModuleDisplayable")) {
                                label = new Label(9, rowNum, ccs.get(l).getCcmtsDownChannelModule());
                                label.setCellFormat(contentCellFormat);
                                sheet.addCell(label);
                            }
                            rowNum++;
                        }
                    }
                }

            }
            workbook.write();
            workbook.close();
        } catch (Exception e) {
            logger.debug("createCcmtsDeviceListReportExcelFile method error:{}", e);
        }
    }

    private String exportAsExcelFromTask(List<CcmtsChannelReportLocation> locations,
            Map<String, Boolean> columnDisable, Date date, ReportTask task) {
        String filePath = ReportExportUtil.getExportUrl(task, EXCEL);
        File file = new File(filePath);
        try {
            file.createNewFile();
            WritableWorkbook workbook = Workbook.createWorkbook(file);
            createCcmtsDeviceListReportExcelFile(locations, columnDisable, workbook, date);
        } catch (IOException e) {
            logger.debug("something wrong with I/O:", e);
        }
        return filePath;
    }

    /**
     * 将下行信道模式由ID转化为名称
     * 
     * @author YangYi add
     * @created @2013-9-14
     * @param moduleId
     * @return moldueName
     */
    private String getDownMoldueName(String moduleId) {
        if (moduleId == null) {
            moduleId = "0";
        }
        int module = Integer.valueOf(moduleId);
        if (module > 4) {
            module = 0;
        }
        return CcmtsChannelListReportCreatorImpl.DOWN_MODULE_NAMES[module];
    }

    /**
     * 将上行信道模式由ID转化为名称
     * 
     * @author YangYi add
     * @created @2013-9-14
     * @param moduleId
     * @return moldueName
     */
    private String getUpModuleName(String moduleId) {
        if (moduleId == null) {
            moduleId = "0";
        }
        int module = Integer.valueOf(moduleId);
        if (module > UP_MODULE_NAMES.length + 1) {
            module = 0;
        }
        return CcmtsChannelListReportCreatorImpl.UP_MODULE_NAMES[module];
    }

}
