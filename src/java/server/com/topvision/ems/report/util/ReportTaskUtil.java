/***********************************************************************
 * $Id: QuatzCronExpression.java,v1.0 2013-5-25 下午3:17:16 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.report.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.Colour;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WriteException;

import com.topvision.ems.report.domain.ExcutorTimePolicy;
import com.topvision.ems.report.domain.ReportTask;
import com.topvision.ems.report.domain.StartStopTime;
import com.topvision.platform.ResourceManager;

/**
 * Quartz‘s cron表达式的生成算法
 * 
 * @author Bravin
 * @created @2013-5-25-下午3:17:16
 * 
 */
public class ReportTaskUtil {

    public static final int DAILY_REPORT = 0;
    public static final int WEEKLY_REPORT = 1;
    public static final int MONTHLY_REPORT = 2;

    public static final int MON = 1;
    public static final int TUES = 2;
    public static final int WED = 3;
    public static final int THUR = 4;
    public static final int FRI = 5;
    public static final int SAT = 6;
    public static final int SUN = 7;

    public static final int THISPERIOD = 0;// 统计本周
    public static final int LASTPERIOD = 1;// 统计上周

    private static final DateFormat stTimeFormatter_Daily = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
    private static final DateFormat etTimeFormatter_Daily = new SimpleDateFormat("yyyy-MM-dd 23:59:59");
    private static final DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * 转换为cron表达式
     * 
     * @param excutorType
     * @param excutorDay
     * @param excutorHour
     * @param excutorMin
     * @return
     */
    public static String encodeCronExpression(int cycleType, int excutorDay, int excutorHour, int excutorMin) {
        StringBuilder sb = new StringBuilder();
        if (cycleType == DAILY_REPORT) {
            sb.append("0 ").append(excutorMin).append(" ").append(excutorHour).append(" * * ?");
        } else if (cycleType == WEEKLY_REPORT) {
            sb.append("0 ").append(excutorMin).append(" ").append(excutorHour).append(" ? * ").append(excutorDay);
        } else if (cycleType == MONTHLY_REPORT) {
            sb.append("0 ").append(excutorMin).append(" ").append(excutorHour).append(" ").append(excutorDay)
                    .append("  * ?");
        }
        return sb.toString();
    }

    /**
     * 反解析Cron表达式
     * 
     * @param task
     * @return
     */
    public static ExcutorTimePolicy decodeCronExpression(ReportTask task) {
        ExcutorTimePolicy policy = decodeCronExpression(task.getCycleType(), task.getCronExpression());
        if (task.getCycleType() == WEEKLY_REPORT) {
            int[] weekDaySelected = (int[]) task.getCondition().get("weekDaySelected");
            policy.setWeekDaySelected(weekDaySelected);
            policy.setExcutorType((Integer) task.getCondition("excutorType"));
            policy.setWeekStartDay((Integer) task.getCondition("weekStartDay"));
        }
        return policy;
    }

    /**
     * 反解析Cron表达式
     * 
     * @param cycleType
     * @param cronExpression
     * @return
     */
    public static ExcutorTimePolicy decodeCronExpression(int cycleType, String cronExpression) {
        String[] list = cronExpression.split(" ");
        ExcutorTimePolicy policy = new ExcutorTimePolicy();
        policy.setCycleType(cycleType);
        policy.setExcutorHour(Integer.parseInt(list[2]));
        policy.setExcutorMin(Integer.parseInt(list[1]));
        if (cycleType == WEEKLY_REPORT) {
            policy.setExcutorDay(Integer.parseInt(list[5]));
        } else if (cycleType == MONTHLY_REPORT) {
            policy.setExcutorDay(Integer.parseInt(list[3]));
        }
        return policy;
    }

    /**
     * 得到报表的统计时间段
     * 
     * @param etp
     * @param date
     * @return
     */
    public static List<StartStopTime> getStAndEtTime(ExcutorTimePolicy etp, Date date) {
        List<StartStopTime> startStopTimes = new ArrayList<StartStopTime>();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        try {
            if (etp.getCycleType() == 0) {
                calendar.add(Calendar.DATE, -1);
                Date stTime = formatter.parse(stTimeFormatter_Daily.format(calendar.getTime()));
                Date etTime = formatter.parse(etTimeFormatter_Daily.format(calendar.getTime()));
                StartStopTime startStopTime = new StartStopTime(stTime, etTime);
                startStopTimes.add(startStopTime);
            } else if (etp.getCycleType() == 1) {
                if (etp.getExcutorType() == 1) {
                    calendar.add(Calendar.DATE, -7);
                }
                startStopTimes = getWeeklySegmentation(etp.getWeekDaySelected(), etp.getWeekStartDay(), calendar);
            } else if (etp.getCycleType() == 2) {
                calendar.add(Calendar.MONTH, -1);
                calendar.set(Calendar.DATE, 1);
                Date stTime = formatter.parse(stTimeFormatter_Daily.format(calendar.getTime()));
                calendar.roll(Calendar.DATE, -1);
                Date etTime = formatter.parse(etTimeFormatter_Daily.format(calendar.getTime()));
                StartStopTime startStopTime = new StartStopTime(stTime, etTime);
                startStopTimes.add(startStopTime);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return startStopTimes;
    }

    /**
     * 得到周报表的时间段
     * 
     * @param weekdaySelects
     * @param calendar
     * @return
     * @throws ParseException
     */
    private static List<StartStopTime> getWeeklySegmentation(int[] weekdaySelects, int weekStartDay,
            Calendar calendar) throws ParseException {
        List<StartStopTime> startStopTimes = new ArrayList<StartStopTime>();
        Date startTime = new Date();
        Date endTime = new Date();
        StartStopTime startStopTime = null;
        // Arrays.sort(weekdaySelects);
        // weekdaySelects存储的是哪些天被选中了进行统计
        // weekStartDay表示每周是从哪天开始的,调整回到每周开始那天
        if (weekStartDay == 1) {
            calendar.setFirstDayOfWeek(Calendar.SUNDAY);
            calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        } else if (weekStartDay == 2) {
            calendar.setFirstDayOfWeek(Calendar.MONDAY);
            calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        }
        boolean start = true;
        for (int i = 0; i < weekdaySelects.length; i++) {
            calendar.set(Calendar.DAY_OF_WEEK, weekdaySelects[i]);
            if (start) {
                // 该天当作这个时间段的开始
                startTime = formatter.parse(stTimeFormatter_Daily.format(calendar.getTime()));
                start = false;
            }
            if (i + 1 == weekdaySelects.length || weekdaySelects[i + 1] - weekdaySelects[i] > 1) {
                // 当这是最后一天或者后面一天与本天不连续时，将其作为结束时间
                endTime = formatter.parse(etTimeFormatter_Daily.format(calendar.getTime()));
                startStopTime = new StartStopTime(startTime, endTime);
                startStopTimes.add(startStopTime);
                start = true;
            }
        }
        return startStopTimes;
    }

    /**
     * 得到执行的时间SQL段
     * 
     * @param reportTask
     * @param timeFieldName
     * @return
     */
    public static String getTimeSegmentSql(ReportTask reportTask, String timeFieldName) {
        ExcutorTimePolicy policy = decodeCronExpression(reportTask);
        List<StartStopTime> stAndEtTimes = getStAndEtTime(policy, new Date());
        StringBuilder sb = new StringBuilder();
        for (StartStopTime time : stAndEtTimes) {
            sb.append(" OR ( ").append(timeFieldName).append(" BETWEEN ");
            Date st = time.getStTime();
            Date et = time.getEtTime();
            sb.append("'").append(formatter.format(st)).append("'").append(" AND ").append("'")
                    .append(formatter.format(et)).append("'");
            sb.append(" ) ");
        }
        if (stAndEtTimes.size() > 1) {
            return " ( " + sb.substring(4) + " ) ";
        } else {
            return sb.substring(4);
        }
    }

    /**
     * 查询某一时间段的SQL拼接
     * 
     * @param stTime
     * @param etTime
     * @param fieldName
     * @return
     */
    public static String getTimeSegmentSql(String stTime, String etTime, String fieldName) {
        return String.format("(%s BETWEEN '%s' AND '%s' ) ", fieldName, stTime, etTime);
    }

    /**
     * 查询某一时间段的SQL拼接
     * 
     * @param stTime
     * @param etTime
     * @param fieldName
     * @return
     */
    public static String getTimeSegmentSql(Date stTime, Date etTime, String fieldName) {
        return getTimeSegmentSql(formatter.format(stTime), formatter.format(etTime), fieldName);
    }

    /**
     * 查询某一时间段的SQL拼接
     * 
     * @param startStopTime
     * @param fieldName
     * @return
     */
    public static String getTimeSegmentSql(StartStopTime startStopTime, String fieldName) {
        return getTimeSegmentSql(formatter.format(startStopTime.getStTime()),
                formatter.format(startStopTime.getStTime()), fieldName);
    }

    /**
     * 生成并格式化生成EXCEL文件的头部
     * 
     * @param sheet
     * @param title
     *            报表标题
     * @param createTime
     *            报表生成时间
     * @param conditions
     *            报表统计条件字符串
     * @param colNum
     *            列数
     */
    public static void formatExcelHeader(WritableSheet sheet, String title, Date createTime, String conditions,
            int colNum) throws Exception {
        int rows = 0;
        // 第一行为报表标题
        sheet.setRowView(0, 1000);
        WritableCellFormat cellFormat = new WritableCellFormat(new WritableFont(WritableFont.ARIAL, 16,
                WritableFont.BOLD));
        cellFormat.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
        cellFormat.setAlignment(jxl.format.Alignment.CENTRE);
        Label label = new Label(0, rows, title, cellFormat);
        sheet.addCell(label);
        rows++;

        // 第二行为统计时间点
        cellFormat = new WritableCellFormat(new WritableFont(WritableFont.ARIAL, 13, WritableFont.NO_BOLD));
        cellFormat.setAlignment(jxl.format.Alignment.RIGHT);
        label = new Label(0, rows, formatter.format(createTime), cellFormat);
        sheet.addCell(label);
        rows++;

        if (conditions != null) {
            // 第三行为统计条件标题
            cellFormat = new WritableCellFormat(new WritableFont(WritableFont.ARIAL, 14, WritableFont.BOLD));
            cellFormat.setAlignment(jxl.format.Alignment.LEFT);
            label = new Label(0, rows, getString("report.condtions", "report"), cellFormat);
            sheet.addCell(label);
            rows++;

            // 第四行为统计区间和设备类型
            int rowLength = conditions.split("\\n").length;
            sheet.setRowView(3, 300 * rowLength);
            cellFormat = new WritableCellFormat(new WritableFont(WritableFont.ARIAL, 12, WritableFont.NO_BOLD));
            cellFormat.setWrap(true);
            label = new Label(0, rows, conditions, cellFormat);
            sheet.addCell(label);
            rows++;
        }

        // 第五行为查询结果标题
        cellFormat = new WritableCellFormat(new WritableFont(WritableFont.ARIAL, 14, WritableFont.BOLD));
        label = new Label(0, rows, getString("report.queryResult", "report"), cellFormat);
        sheet.addCell(label);
        rows++;

        for (int i = 0; i < rows; i++) {
            sheet.mergeCells(0, i, colNum - 1, i);
        }

        /*
         * sheet.mergeCells(0, 1, colNum-1, 1); sheet.mergeCells(0, 2, colNum-1, 2);
         * sheet.mergeCells(0, 3, colNum-1, 3); sheet.mergeCells(0, 4, colNum-1, 4);
         */
    }

    /**
     * 生成并格式化生成EXCEL文件的头部(不需要统计条件的报表)
     * 
     * @param sheet
     * @param title
     * @param createTime
     * @param colNum
     * @throws Exception
     */
    public static void formatExcelHeader(WritableSheet sheet, String title, Date createTime, int colNum)
            throws Exception {
        // 第一行为报表标题
        sheet.setRowView(0, 1000);
        WritableCellFormat cellFormat = new WritableCellFormat(new WritableFont(WritableFont.ARIAL, 16,
                WritableFont.BOLD));
        cellFormat.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
        cellFormat.setAlignment(jxl.format.Alignment.CENTRE);
        Label label = new Label(0, 0, title, cellFormat);
        sheet.addCell(label);

        // 第二行为统计时间点
        cellFormat = new WritableCellFormat(new WritableFont(WritableFont.ARIAL, 13, WritableFont.NO_BOLD));
        cellFormat.setAlignment(jxl.format.Alignment.RIGHT);
        label = new Label(0, 1, formatter.format(createTime), cellFormat);
        sheet.addCell(label);

        sheet.mergeCells(0, 0, colNum - 1, 0);
        sheet.mergeCells(0, 1, colNum - 1, 1);
    }

    public static WritableCellFormat getTitleCellFormat() throws WriteException {
        WritableCellFormat titleCellFormat = new WritableCellFormat(new WritableFont(WritableFont.ARIAL, 12,
                WritableFont.BOLD));
        titleCellFormat.setAlignment(jxl.format.Alignment.CENTRE);
        titleCellFormat.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
        titleCellFormat.setBackground(Colour.GRAY_25);
        titleCellFormat.setBorder(Border.ALL, BorderLineStyle.HAIR);
        return titleCellFormat;
    }

    public static WritableCellFormat getContentCellFormat() throws WriteException {
        WritableCellFormat contentCellFormat = new WritableCellFormat(new WritableFont(WritableFont.ARIAL, 11,
                WritableFont.NO_BOLD));
        contentCellFormat.setAlignment(jxl.format.Alignment.CENTRE);
        contentCellFormat.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
        contentCellFormat.setWrap(true);
        return contentCellFormat;
    }

    public static String getString(String key, String module) {
        module = String.format("com.topvision.ems.%s.resources", module);
        try {
            ResourceManager resourceManager = ResourceManager.getResourceManager(module);
            return resourceManager.getNotNullString(key);
        } catch (Exception e) {
            return key;
        }
    }

    /**
     * 获取信道流量报表中的流量范围查询条件sql
     * 
     * @param rangeName
     * @param string
     * @return
     */
    public static Object getRangeSql(String rangeName, String fieldName) {
        StringBuilder rangeSql = new StringBuilder();
        switch (rangeName) {
        case "usage0":
            rangeSql.append(fieldName).append(" = 0");
            break;
        case "usage0to10":
            rangeSql.append(fieldName).append(" > 0 and ").append(fieldName).append(" <= 10 ");
            break;
        case "usage10to20":
            rangeSql.append(fieldName).append(" > 10 and ").append(fieldName).append(" <= 20 ");
            break;
        case "usage20to30":
            rangeSql.append(fieldName).append(" > 20 and ").append(fieldName).append(" <= 30 ");
            break;
        case "usage30to40":
            rangeSql.append(fieldName).append(" > 30 and ").append(fieldName).append(" <= 40 ");
            break;
        case "usage40to50":
            rangeSql.append(fieldName).append(" > 40 and ").append(fieldName).append(" <= 50 ");
            break;
        case "usage50to60":
            rangeSql.append(fieldName).append(" > 50 and ").append(fieldName).append(" <= 60 ");
            break;
        case "usage60to70":
            rangeSql.append(fieldName).append(" > 60 and ").append(fieldName).append(" <= 70 ");
            break;
        case "usage70to80":
            rangeSql.append(fieldName).append(" > 70 and ").append(fieldName).append(" <= 80 ");
            break;
        case "usage80to90":
            rangeSql.append(fieldName).append(" > 80 and ").append(fieldName).append(" <= 90 ");
            break;
        case "usage90to100":
            rangeSql.append(fieldName).append(" > 90 and ").append(fieldName).append(" <= 100 ");
            break;
        default:
            break;
        }
        return rangeSql.toString();
    }
}
