package com.topvision.ems.report.util;

public final class ReportConstants {

    /**
     * 自定义.
     */
    public static final int CYCLE_CUSTOM = 0;

    /**
     * 最近1小时.
     */
    public static final int CYCLE_1 = 1;

    /**
     * 最近12小时.
     */
    public static final int CYCLE_12 = 2;

    /**
     * 最近24小时.
     */
    public static final int CYCLE_24 = 3;

    /**
     * 今天.
     */
    public static final int CYCLE_TODAY = 4;

    /**
     * 昨天.
     */
    public static final int CYCLE_YESDAY = 5;

    /**
     * 本周.
     */
    public static final int CYCLE_WEEK = 6;

    /**
     * 上周.
     */
    public static final int CYCLE_LASTWEEK = 7;

    /**
     * 本月.
     */
    public static final int CYCLE_MONTH = 8;

    /**
     * 上月.
     */
    public static final int CYCLE_LASTMONTH = 9;

    /**
     * 本季度.
     */
    public static final int CYCLE_SEASON = 10;

    /**
     * 本年.
     */
    public static final int CYCLE_YEAR = 11;

    /**
     * 本周工作日.
     */
    public static final int CYCLE_WORKDAY = 12;

    /**
     * 上半年.
     */
    public static final int CYCLE_LAST_SEMIYEAR = 13;

    /**
     * 下半年.
     */
    public static final int CYCLE_NEXT_SEMIYEAR = 14;

    /**
     * 上旬.
     */
    public static final int CYCLE_LAST_XUN = 15;
    /**
     * 中旬.
     */
    public static final int CYCLE_MIDDLE_XUN = 16;
    /**
     * 下旬.
     */
    public static final int CYCLE_NEXT_XUN = 17;

    public static final int TYPE_PDF = 1;
    public static final int TYPE_EXCEL = 2;
    public static final int TYPE_WORD = 4;
    public static final int TYPE_PPT = 8;

    /**
     * 性能数据保留天数.
     */
    public static int PERF_DURATION = 2;

    public static String TIME_UNIT_HOUR_MIN = "H:mm";

    public static String TIME_UNIT_DAY_HOUR = "d H";

    public static String TIME_UNIT_MONTH_DAY = "MM-dd";

    public static String TIME_UNIT_YEAR_MONTH = "yyyy-MM";

    public static String TIME_UNIT_MINUTE = "mm";

    public static String TIME_UNIT_HOUR = "HH";

    public static String TIME_UNIT_DAY = "dd";

    public static String TIME_UNIT_MONTH = "MM";

    /**
     * 临时报表产生路径.
     */
    public static String REPORT_TEMP_PNG = "/tempfile/report/png/";

    public static String REPORT_TEMP_PDF = "/tempfile/report/pdf/";

    public static String REPORT_TEMP_XLS = "/tempfile/report/xls/";

}
