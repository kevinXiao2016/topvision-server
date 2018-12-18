/***********************************************************************
 * $Id: RestartAnalyzerAction.java,v1.0 2013-2-21 上午11:44:03 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.performance.action;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
import com.topvision.ems.network.service.EntityService;
import com.topvision.ems.performance.domain.RestartCount;
import com.topvision.ems.performance.domain.RestartRecord;
import com.topvision.ems.performance.service.RestartAnalyzerService;
import com.topvision.ems.report.util.ReportTaskUtil;
import com.topvision.framework.common.ColorUtils;
import com.topvision.framework.common.DateUtils;
import com.topvision.framework.web.struts2.BaseAction;

/**
 * @author Bravin
 * @created @2013-2-21-上午11:44:03
 * 
 */
@Controller("restartAnalyzerAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class RestartAnalyzerAction extends BaseAction {
    private static final long serialVersionUID = 1715931635709827790L;
    private static Logger logger = LoggerFactory.getLogger(RestartAnalyzerAction.class);
    private static final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static final Long OLT_DEVICE_MARK_INDEX = 0L;
    private static final Long DEFAULT_VALUE = 0L;
    @Autowired
    private RestartAnalyzerService restartAnalyzerService;
    @Autowired
    private EntityService entityService;
    private Long entityId;
    private Long deviceIndex = 0L;
    private String startTime;
    private String endTime;
    private String deviceDisplayName;
    private Date currentTime;
    private List<RestartRecord> restartRecords;
    public static final DateFormat fileNameFormatter = new SimpleDateFormat("yyyyMMddHHmmss");

    /**
     * 显示统计页面
     * 
     * @return
     */
    public String showRestartStasticAnalyzer() {
        return SUCCESS;
    }

    /**
     * 加载设备重启报表的数据
     * 
     * @return
     * @throws ParseException
     * @throws SQLException
     * @throws IOException
     */
    public String loadRestartStatistic() throws ParseException, SQLException, IOException {
        Map<String, String> map = new HashMap<String, String>();
        Long max = DEFAULT_VALUE;
        Long min = DEFAULT_VALUE;
        if (startTime != null) {
            max = formatter.parse(endTime).getTime();
            min = formatter.parse(startTime).getTime();
        } else {
            Long now = new Date().getTime();
            max = now;
            // 默认统计7天
            min = now - 1000 * 60 * 60 * 24 * 7;
        }
        map.put("queryStartTime", min.toString());
        map.put("queryEndTime", max.toString());
        List<RestartCount> records = restartAnalyzerService.loadRestartStatistic(map);
        JSONObject json = new JSONObject();
        for (RestartCount record : records) {
            record.setY(record.getCount());
            record.setColor(ColorUtils.generateColor());
            record.setDisplayName(parseDeviceName(record.getDeviceIndex(), record.getIp()));
        }
        json.put("startTime", formatter.format(min));
        json.put("endTime", formatter.format(max));
        json.put("startTimeLong", min);
        json.put("endTimeLong", max);
        json.put("data", records);
        writeDataToAjax(json);
        return NONE;
    }

    /**
     * 显示重启详细
     * 
     * @return
     * @throws SQLException
     * @throws ParseException
     */
    public String showRestartDetail() throws SQLException, ParseException {
        Entity entity = entityService.getEntity(entityId);
        deviceDisplayName = parseDeviceName(deviceIndex, entity.getIp());
        currentTime = new Date();
        Map<String, String> map = new HashMap<String, String>();
        map.put("entityId", entityId.toString());
        if (deviceIndex != null) {
            map.put("onuId", deviceIndex.toString());
        } else {
            map.put("onuId", "0");
        }
        if (startTime != null) {
            map.put("queryStartTime", startTime);
            map.put("queryEndTime", endTime);
        }
        startTime = formatter.format(Long.parseLong(startTime));
        endTime = formatter.format(Long.parseLong(endTime));
        restartRecords = restartAnalyzerService.loadRestartRecords(map);
        return SUCCESS;
    }

    /**
     * 加载统计数据，以设备为单位
     * 
     * @return
     * @throws SQLException
     * @throws IOException
     * @throws ParseException
     */
    public String loadRestartStasticData() throws SQLException, IOException, ParseException {
        Map<String, String> map = new HashMap<String, String>();
        Entity entity = entityService.getEntity(entityId);
        map.put("entityId", entityId.toString());
        Long max = DEFAULT_VALUE;
        Long min = DEFAULT_VALUE;
        if (startTime != null) {
            max = formatter.parse(endTime).getTime();
            min = formatter.parse(startTime).getTime();
        } else {
            Long now = new Date().getTime();
            max = now;
            // 默认统计7天
            min = now - 1000 * 60 * 60 * 24 * 7;
        }
        map.put("queryStartTime", min.toString());
        map.put("queryEndTime", max.toString());
        List<RestartCount> records = restartAnalyzerService.loadRestartStasticData(map);
        JSONObject json = new JSONObject();
        List<String> categories = new ArrayList<String>();
        boolean oltFounded = false;
        for (RestartCount record : records) {
            categories.add(parseDeviceName(record.getDeviceIndex(), entity.getIp()));
            record.setY(record.getCount());
            record.setColor(ColorUtils.generateColor());
            // 如果重启表中没有发现OLT，那么做好标记，之后手动添加
            if (record.getDeviceIndex() == 0L) {
                oltFounded = true;
            }
        }
        if (!oltFounded) {
            RestartCount record = new RestartCount();
            record.setEntityId(entityId);
            record.setDeviceIndex(0L);
            record.setColor(ColorUtils.generateColor());
            // 在首位置添加
            records.add(0, record);
            categories.add(0, parseDeviceName(0L, entity.getIp()));
        }
        json.put("categories", categories);
        json.put("startTime", formatter.format(min));
        json.put("endTime", formatter.format(max));
        json.put("startTimeLong", min);
        json.put("endTimeLong", max);
        json.put("data", records);
        json.put("entityId", entity.getIp());
        ServletActionContext.getResponse().setCharacterEncoding("UTF-8");
        json.write(response.getWriter());
        return NONE;
    }

    /**
     * 加载OLT设备列表
     * 
     * @return
     * @throws SQLException
     * @throws IOException
     */
    public String loadEponDeviceList() throws SQLException, IOException {
        List<Entity> list = restartAnalyzerService.loadEponDeviceList();
        JSONArray json = new JSONArray();
        for (Entity entity : list) {
            JSONObject o = new JSONObject();
            o.put("entityId", entity.getEntityId());
            o.put("ip", entity.getIp());
            json.add(o);
        }
        writeDataToAjax(json);
        return NONE;
    }

    /**
     * 到处设备重启清单
     * 
     * @return
     * @throws SQLException
     * @throws ParseException
     */
    public String exportRestartStatic() throws SQLException, ParseException {
        OutputStream out = null;
        FileInputStream fis = null;
        ServletActionContext.getResponse().setContentType("application/msexcel");
        Map<String, String> map = new HashMap<String, String>();
        Long max = DEFAULT_VALUE;
        Long min = DEFAULT_VALUE;
        if (startTime != null) {
            max = formatter.parse(endTime).getTime();
            min = formatter.parse(startTime).getTime();
        } else {
            Long now = new Date().getTime();
            max = now;
            // 默认统计7天
            min = now - 1000 * 60 * 60 * 24 * 7;
        }
        map.put("queryStartTime", min.toString());
        map.put("queryEndTime", max.toString());
        List<RestartCount> records = restartAnalyzerService.loadRestartStatistic(map);
        try {
            Date date = new Date();
            String fileName = URLEncoder.encode("设备重启列表" + "-" + DateUtils.format(date) + ".xls", "UTF-8");
            ServletActionContext.getResponse().addHeader("Content-Disposition", "attachment;filename=" + fileName);
            out = ServletActionContext.getResponse().getOutputStream();

            Label label;
            // 创建表头等信息
            WritableCellFormat titleCellFormat = ReportTaskUtil.getTitleCellFormat();
            WritableWorkbook workbook = Workbook.createWorkbook(out);
            WritableSheet sheet = workbook.createSheet("清单", 0);
            sheet.setColumnView(0, 20);
            sheet.setColumnView(1, 20);
            sheet.setColumnView(2, 20);
            sheet.setColumnView(3, 20);
            sheet.setColumnView(4, 20);
            String conditions = "\n统计开始时间:" + startTime + "\n";
            conditions += "统计结束时间:" + endTime;
            ReportTaskUtil.formatExcelHeader(sheet, "设备重启列表", date, conditions, 5);
            label = new Label(0, 5, "局端设备IP", titleCellFormat);
            sheet.addCell(label);
            label = new Label(1, 5, "设备位置", titleCellFormat);
            sheet.addCell(label);
            label = new Label(2, 5, "设备别名", titleCellFormat);
            sheet.addCell(label);
            label = new Label(3, 5, "重启次数", titleCellFormat);
            sheet.addCell(label);
            label = new Label(4, 5, "创建时间", titleCellFormat);
            sheet.addCell(label);
            int rowNum = 6;
            WritableCellFormat contentCellFormat = ReportTaskUtil.getContentCellFormat();
            for (RestartCount restartCount : records) {
                restartCount.setDisplayName(parseDeviceName(restartCount.getDeviceIndex(), restartCount.getIp()));
                label = new Label(0, rowNum, restartCount.getIp(), contentCellFormat);
                sheet.addCell(label);
                label = new Label(1, rowNum, restartCount.getDisplayName(), contentCellFormat);
                sheet.addCell(label);
                label = new Label(2, rowNum, restartCount.getEntityAlias(), contentCellFormat);
                sheet.addCell(label);
                label = new Label(3, rowNum, restartCount.getCount().toString(), contentCellFormat);
                sheet.addCell(label);
                label = new Label(4, rowNum, restartCount.getCreateTimeString(), contentCellFormat);
                sheet.addCell(label);
                rowNum++;
            }
            workbook.write();
            workbook.close();
        } catch (Exception e) {
            logger.debug("export restart stastics error:{}", e);
        } finally {
            try {
                if (fis != null) {
                    fis.close();
                }
                if (out != null) {
                    out.flush();
                    out.close();
                }

            } catch (Exception e) {
                logger.debug("something wrong with I/O:", e);
            }
        }
        return NONE;
    }

    /**
     * 导出详细
     * 
     * @return
     * @throws SQLException
     * @throws ParseException
     */
    public String exportRestartDetail() throws SQLException, ParseException {
        OutputStream out = null;
        FileInputStream fis = null;
        ServletActionContext.getResponse().setContentType("application/msexcel");
        Entity entity = entityService.getEntity(entityId);
        deviceDisplayName = parseDeviceName(deviceIndex, entity.getIp());
        currentTime = new Date();
        Map<String, String> map = new HashMap<String, String>();
        map.put("entityId", entityId.toString());
        if (deviceIndex != null) {
            map.put("onuId", deviceIndex.toString());
        } else {
            map.put("onuId", "0");
        }
        if (startTime != null) {
            map.put("queryStartTime", formatter.parse(startTime).getTime() + "");
            map.put("queryEndTime", formatter.parse(endTime).getTime() + "");
        }
        restartRecords = restartAnalyzerService.loadRestartRecords(map);
        try {
            Date date = new Date();
            String title = "设备 " + deviceDisplayName + " 重启详细";
            String fileName = URLEncoder.encode(title + "-" + DateUtils.format(date) + ".xls", "UTF-8");
            ServletActionContext.getResponse().addHeader("Content-Disposition", "attachment;filename=" + fileName);
            out = ServletActionContext.getResponse().getOutputStream();

            Label label;
            // 创建表头等信息
            WritableCellFormat titleCellFormat = ReportTaskUtil.getTitleCellFormat();
            WritableWorkbook workbook = Workbook.createWorkbook(out);
            WritableSheet sheet = workbook.createSheet("清单", 0);
            sheet.setColumnView(0, 20);
            sheet.setColumnView(1, 20);
            String conditions = "\n统计开始时间:" + startTime + "\n";
            conditions += "统计结束时间:" + endTime;
            ReportTaskUtil.formatExcelHeader(sheet, title, date, conditions, 2);
            label = new Label(0, 5, "重启时间", titleCellFormat);
            sheet.addCell(label);
            label = new Label(1, 5, "持续时长", titleCellFormat);
            sheet.addCell(label);
            int rowNum = 6;

            for (RestartRecord restartRecord : restartRecords) {
                label = new Label(0, rowNum, restartRecord.getDeviceReStart());
                sheet.addCell(label);
                label = new Label(1, rowNum, restartRecord.getRunningTimeString());
                sheet.addCell(label);
                rowNum++;
            }
            workbook.write();
            workbook.close();
        } catch (Exception e) {
            logger.debug("export restart stastics error:{}", e);
        } finally {
            try {
                if (fis != null) {
                    fis.close();
                }
                if (out != null) {
                    out.flush();
                }
            } catch (Exception e) {
                logger.debug("something wrong with I/O:", e);
            }
        }
        return NONE;
    }

    /**
     * 转换成展示的数据
     * 
     * @param deviceIndex
     * @param ip
     * @return
     */
    private String parseDeviceName(Long deviceIndex, String ip) {
        String name;
        if (deviceIndex.equals(OLT_DEVICE_MARK_INDEX)) {
            name = "局端OLT(" + ip + ")";
        } else {
            long slot = ((deviceIndex.longValue() & 0xFF00000000L) >> 32);
            long port = (deviceIndex.longValue() & 0x00FF000000L) >> 24;
            long ccmts = (deviceIndex.longValue() & 0x0000FF0000L) >> 16;
            name = String.format("CCMTS(%d/%d:%d)", slot, port, ccmts);
        }
        return name;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public RestartAnalyzerService getRestartAnalyzerService() {
        return restartAnalyzerService;
    }

    public void setRestartAnalyzerService(RestartAnalyzerService restartAnalyzerService) {
        this.restartAnalyzerService = restartAnalyzerService;
    }

    public EntityService getEntityService() {
        return entityService;
    }

    public void setEntityService(EntityService entityService) {
        this.entityService = entityService;
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

    public Long getDeviceIndex() {
        return deviceIndex;
    }

    public void setDeviceIndex(Long deviceIndex) {
        this.deviceIndex = deviceIndex;
    }

    public String getDeviceDisplayName() {
        return deviceDisplayName;
    }

    public void setDeviceDisplayName(String deviceDisplayName) {
        this.deviceDisplayName = deviceDisplayName;
    }

    public Date getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(Date currentTime) {
        this.currentTime = currentTime;
    }

    public List<RestartRecord> getRestartRecords() {
        return restartRecords;
    }

    public void setRestartRecords(List<RestartRecord> restartRecords) {
        this.restartRecords = restartRecords;
    }

}
