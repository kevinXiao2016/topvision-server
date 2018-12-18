package com.topvision.platform.util;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import com.topvision.exception.service.DateFormatException;
import com.topvision.framework.common.DateUtils;

public class ReportUtils {

    /**
     * 产生临时的报表图象名称, 默认png格式.
     * 
     * @return
     */
    public static String buildTempImgName() {
        return ReportConstants.REPORT_TEMP_PNG + UUID.randomUUID().toString() + ".png";
    }

    public static String buildTempImgName(String format) {
        return ReportConstants.REPORT_TEMP_PNG + UUID.randomUUID().toString() + "." + format;
    }

    public static Integer getReportFormat(Boolean pdfEnabled, Boolean excelEnabled, Boolean wordEnabled,
            Boolean pptEnabled, Boolean exEnabled) {
        int sum = 0;
        if (pdfEnabled) {
            sum = sum + ReportConstants.TYPE_PDF;
        }
        if (excelEnabled) {
            sum = sum + ReportConstants.TYPE_EXCEL;
        }
        if (wordEnabled) {
            sum = sum + ReportConstants.TYPE_WORD;
        }
        if (pptEnabled) {
            sum = sum + ReportConstants.TYPE_PPT;
        }
        return sum;
    }

    public static void main(String[] args) {
        for (int i = 1; i < 11; i++) {
            Date[] dates = parseDateByCycle(i);
            System.out.println(DateUtils.FULL_FORMAT.format(dates[0]) + " - " + DateUtils.FULL_FORMAT.format(dates[1]));
        }
    }

    public static Date[] parseDateByCustom(String startDate, Integer startTime, String endDate, Integer endTime) {
        Date date1 = null;
        Date date2 = null;
        try {
            date1 = DateUtils.DATE_FORMAT.parse(startDate);
            date2 = DateUtils.DATE_FORMAT.parse(endDate);
        } catch (ParseException e) {
            throw new DateFormatException(e);
        }
        Calendar c1 = Calendar.getInstance();
        c1.setTime(date1);
        c1.set(Calendar.HOUR_OF_DAY, startTime);
        c1.set(Calendar.MINUTE, 0);
        c1.set(Calendar.SECOND, 0);

        Calendar c2 = Calendar.getInstance();
        c2.setTime(date2);
        c2.set(Calendar.HOUR_OF_DAY, endTime);
        c2.set(Calendar.MINUTE, 0);
        c2.set(Calendar.SECOND, 0);

        Date[] dates = new Date[2];
        dates[0] = c1.getTime();
        dates[1] = c2.getTime();
        return dates;
    }

    public static Date[] parseDateByCycle(Integer cycle) {
        Calendar c1 = null;
        Calendar c2 = null;
        Date date1 = null;
        Date date2 = null;
        switch (cycle) {
        case ReportConstants.CYCLE_1:
            c2 = Calendar.getInstance();
            date1 = new Date(c2.getTimeInMillis() - DateUtils.HOUR_MILLS);
            date2 = c2.getTime();
            break;
        case ReportConstants.CYCLE_12:
            c2 = Calendar.getInstance();
            date1 = new Date(c2.getTimeInMillis() - 12 * DateUtils.HOUR_MILLS);
            date2 = c2.getTime();
            break;
        case ReportConstants.CYCLE_24:
            c2 = Calendar.getInstance();
            date1 = new Date(c2.getTimeInMillis() - 24 * DateUtils.HOUR_MILLS);
            date2 = c2.getTime();
            break;
        case ReportConstants.CYCLE_TODAY:
            c1 = Calendar.getInstance();
            c1.set(Calendar.HOUR_OF_DAY, 0);
            c1.set(Calendar.MINUTE, 0);
            c1.set(Calendar.SECOND, 0);
            c2 = Calendar.getInstance();
            c2.set(Calendar.HOUR_OF_DAY, 23);
            c2.set(Calendar.MINUTE, 59);
            c2.set(Calendar.SECOND, 59);
            date1 = c1.getTime();
            date2 = c2.getTime();
            break;
        case ReportConstants.CYCLE_YESDAY:
            c2 = Calendar.getInstance();
            c2.set(Calendar.HOUR_OF_DAY, 0);
            c2.set(Calendar.MINUTE, 0);
            c2.set(Calendar.SECOND, 0);
            date1 = new Date(c2.getTimeInMillis() - 24 * DateUtils.HOUR_MILLS);
            date2 = new Date(c2.getTimeInMillis() - 1000);
            break;
        case ReportConstants.CYCLE_WEEK:
            c1 = Calendar.getInstance();
            if (c1.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
                c1.set(Calendar.HOUR_OF_DAY, 23);
                c1.set(Calendar.MINUTE, 59);
                c1.set(Calendar.SECOND, 59);
                date1 = new Date(c1.getTimeInMillis() - DateUtils.WEEK_MILLS + 1000);
            } else {
                c1.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
                c1.set(Calendar.HOUR_OF_DAY, 0);
                c1.set(Calendar.MINUTE, 0);
                c1.set(Calendar.SECOND, 0);
                date1 = c1.getTime();
            }
            date2 = new Date(date1.getTime() + DateUtils.WEEK_MILLS - 1000);
            break;
        case ReportConstants.CYCLE_WORKDAY:
            c1 = Calendar.getInstance();
            if (c1.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
                c1.set(Calendar.HOUR_OF_DAY, 23);
                c1.set(Calendar.MINUTE, 59);
                c1.set(Calendar.SECOND, 59);
                date1 = new Date(c1.getTimeInMillis() - DateUtils.WEEKDAY_MILLS + 1000);
            } else {
                c1.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
                c1.set(Calendar.HOUR_OF_DAY, 0);
                c1.set(Calendar.MINUTE, 0);
                c1.set(Calendar.SECOND, 0);
                date1 = c1.getTime();
            }
            date2 = new Date(date1.getTime() + DateUtils.WEEKDAY_MILLS - 1000);
            break;
        case ReportConstants.CYCLE_LASTWEEK:
            c2 = Calendar.getInstance();
            if (c2.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
                c2.set(Calendar.HOUR_OF_DAY, 0);
                c2.set(Calendar.MINUTE, 0);
                c2.set(Calendar.SECOND, 0);
                date1 = new Date(c2.getTimeInMillis() - 13 * 24 * DateUtils.HOUR_MILLS);
                date2 = new Date(c2.getTimeInMillis() - 6 * 24 * DateUtils.HOUR_MILLS - 1000);
            } else {
                c2.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
                c2.set(Calendar.HOUR_OF_DAY, 0);
                c2.set(Calendar.MINUTE, 0);
                c2.set(Calendar.SECOND, 0);
                date1 = new Date(c2.getTimeInMillis() - 7 * 24 * DateUtils.HOUR_MILLS);
                date2 = new Date(c2.getTimeInMillis() - 1000);
            }
            break;
        case ReportConstants.CYCLE_MONTH:
            c1 = Calendar.getInstance();
            c2 = Calendar.getInstance();
            c1.set(Calendar.DAY_OF_MONTH, 1);
            c1.set(Calendar.HOUR_OF_DAY, 0);
            c1.set(Calendar.MINUTE, 0);
            c1.set(Calendar.SECOND, 0);
            date1 = c1.getTime();

            c2.set(Calendar.DAY_OF_MONTH, c1.getActualMaximum(Calendar.DAY_OF_MONTH));
            c2.set(Calendar.HOUR_OF_DAY, 23);
            c2.set(Calendar.MINUTE, 59);
            c2.set(Calendar.SECOND, 59);
            date2 = c2.getTime();
            break;
        case ReportConstants.CYCLE_LASTMONTH:
            c1 = Calendar.getInstance();
            c2 = Calendar.getInstance();
            if (c1.get(Calendar.MONTH) == Calendar.JANUARY) {
                c1.roll(Calendar.YEAR, -1);
                c1.set(Calendar.MONTH, Calendar.DECEMBER);
                c2.roll(Calendar.YEAR, -1);
                c2.set(Calendar.MONTH, Calendar.DECEMBER);
            } else {
                c1.roll(Calendar.MONTH, -1);
                c2.roll(Calendar.MONTH, -1);
            }
            c1.set(Calendar.DAY_OF_MONTH, 1);
            c1.set(Calendar.HOUR_OF_DAY, 0);
            c1.set(Calendar.MINUTE, 0);
            c1.set(Calendar.SECOND, 0);

            c2.set(Calendar.DAY_OF_MONTH, c2.getActualMaximum(Calendar.DAY_OF_MONTH));
            c2.set(Calendar.HOUR_OF_DAY, 23);
            c2.set(Calendar.MINUTE, 59);
            c2.set(Calendar.SECOND, 59);
            date1 = c1.getTime();
            date2 = c2.getTime();
            break;
        case ReportConstants.CYCLE_SEASON:
            c1 = Calendar.getInstance();
            c2 = Calendar.getInstance();
            int m = c2.get(Calendar.MONTH);
            if (m <= 2) {
                c1.set(Calendar.MONTH, Calendar.JANUARY);
                c2.set(Calendar.MONTH, Calendar.MARCH);
                c2.set(Calendar.DAY_OF_MONTH, 31);
            } else if (m <= 5) {
                c1.set(Calendar.MONTH, Calendar.APRIL);
                c2.set(Calendar.MONTH, Calendar.JUNE);
                c2.set(Calendar.DAY_OF_MONTH, 30);
            } else if (m <= 8) {
                c1.set(Calendar.MONTH, Calendar.JULY);
                c2.set(Calendar.MONTH, Calendar.SEPTEMBER);
                c2.set(Calendar.DAY_OF_MONTH, 30);
            } else if (m <= 11) {
                c1.set(Calendar.MONTH, Calendar.OCTOBER);
                c2.set(Calendar.MONTH, Calendar.DECEMBER);
                c2.set(Calendar.DAY_OF_MONTH, 31);
            }
            c1.set(Calendar.DAY_OF_MONTH, 1);
            c1.set(Calendar.HOUR_OF_DAY, 0);
            c1.set(Calendar.MINUTE, 0);
            c1.set(Calendar.SECOND, 0);

            c2.set(Calendar.HOUR_OF_DAY, 23);
            c2.set(Calendar.MINUTE, 59);
            c2.set(Calendar.SECOND, 59);
            date1 = c1.getTime();
            date2 = c2.getTime();
            break;
        case ReportConstants.CYCLE_YEAR:
            c1 = Calendar.getInstance();
            c2 = Calendar.getInstance();
            c1.set(Calendar.MONTH, Calendar.JANUARY);
            c1.set(Calendar.DAY_OF_MONTH, 1);
            c1.set(Calendar.HOUR_OF_DAY, 0);
            c1.set(Calendar.MINUTE, 0);
            c1.set(Calendar.SECOND, 0);
            date1 = c1.getTime();

            c2.set(Calendar.MONTH, Calendar.DECEMBER);
            c2.set(Calendar.DAY_OF_MONTH, 31);
            c2.set(Calendar.HOUR_OF_DAY, 23);
            c2.set(Calendar.MINUTE, 59);
            c2.set(Calendar.SECOND, 59);
            date2 = c2.getTime();
            break;
        case ReportConstants.CYCLE_LAST_SEMIYEAR:
            c1 = Calendar.getInstance();
            c2 = Calendar.getInstance();
            c1.set(Calendar.MONTH, Calendar.JANUARY);
            c1.set(Calendar.DAY_OF_MONTH, 1);
            c1.set(Calendar.HOUR_OF_DAY, 0);
            c1.set(Calendar.MINUTE, 0);
            c1.set(Calendar.SECOND, 0);
            date1 = c1.getTime();

            c2.set(Calendar.MONTH, Calendar.JUNE);
            c2.set(Calendar.DAY_OF_MONTH, 30);
            c2.set(Calendar.HOUR_OF_DAY, 23);
            c2.set(Calendar.MINUTE, 59);
            c2.set(Calendar.SECOND, 59);
            date2 = c2.getTime();
            break;
        case ReportConstants.CYCLE_NEXT_SEMIYEAR:
            c1 = Calendar.getInstance();
            c2 = Calendar.getInstance();
            c1.set(Calendar.MONTH, Calendar.JULY);
            c1.set(Calendar.DAY_OF_MONTH, 1);
            c1.set(Calendar.HOUR_OF_DAY, 0);
            c1.set(Calendar.MINUTE, 0);
            c1.set(Calendar.SECOND, 0);
            date1 = c1.getTime();

            c2.set(Calendar.MONTH, Calendar.DECEMBER);
            c2.set(Calendar.DAY_OF_MONTH, 31);
            c2.set(Calendar.HOUR_OF_DAY, 23);
            c2.set(Calendar.MINUTE, 59);
            c2.set(Calendar.SECOND, 59);
            date2 = c2.getTime();
            break;
        case ReportConstants.CYCLE_LAST_XUN:
            c1 = Calendar.getInstance();
            c2 = Calendar.getInstance();
            c1.set(Calendar.DAY_OF_MONTH, 1);
            c1.set(Calendar.HOUR_OF_DAY, 0);
            c1.set(Calendar.MINUTE, 0);
            c1.set(Calendar.SECOND, 0);
            date1 = c1.getTime();

            c2.set(Calendar.DAY_OF_MONTH, 10);
            c2.set(Calendar.HOUR_OF_DAY, 23);
            c2.set(Calendar.MINUTE, 59);
            c2.set(Calendar.SECOND, 59);
            date2 = c2.getTime();
            break;
        case ReportConstants.CYCLE_MIDDLE_XUN:
            c1 = Calendar.getInstance();
            c2 = Calendar.getInstance();
            c1.set(Calendar.DAY_OF_MONTH, 11);
            c1.set(Calendar.HOUR_OF_DAY, 0);
            c1.set(Calendar.MINUTE, 0);
            c1.set(Calendar.SECOND, 0);
            date1 = c1.getTime();

            c2.set(Calendar.DAY_OF_MONTH, 20);
            c2.set(Calendar.HOUR_OF_DAY, 23);
            c2.set(Calendar.MINUTE, 59);
            c2.set(Calendar.SECOND, 59);
            date2 = c2.getTime();
            break;
        case ReportConstants.CYCLE_NEXT_XUN:
            c1 = Calendar.getInstance();
            c2 = Calendar.getInstance();
            c1.set(Calendar.DAY_OF_MONTH, 21);
            c1.set(Calendar.HOUR_OF_DAY, 0);
            c1.set(Calendar.MINUTE, 0);
            c1.set(Calendar.SECOND, 0);
            date1 = c1.getTime();

            c2.set(Calendar.DAY_OF_MONTH, c1.getActualMaximum(Calendar.DAY_OF_MONTH));
            c2.set(Calendar.HOUR_OF_DAY, 23);
            c2.set(Calendar.MINUTE, 59);
            c2.set(Calendar.SECOND, 59);
            date2 = c2.getTime();
            break;
        default:
            return null;
        }
        Date[] dates = new Date[2];
        dates[0] = date1;
        dates[1] = date2;
        return dates;
    }

}
