package com.topvision.framework.common;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public final class NumberUtils {

    public static final NumberFormat FORMAT = new DecimalFormat("###.#");

    /**
     * 小数点保留2位的数字格式化器.
     */
    public static final NumberFormat TWODOT_FORMAT = new DecimalFormat("#.##");

    /**
     * 小数点保留1位的数字格式化器.
     */
    public static final NumberFormat ONEDOT_FORMAT = new DecimalFormat("#.#");

    /**
     * 不保留小数点的格式化器.
     */
    public static final NumberFormat NODOT_FORMAT = new DecimalFormat("#");

    /**
     * 百分比格式化器.
     */
    public static final NumberFormat PERCENT_FORMAT = NumberFormat.getPercentInstance();

    /**
     * 百分比格式化器，保留小数点１位。
     */
    public static final NumberFormat PERCENT_FORMAT_ONEDOT = NumberFormat.getPercentInstance();
    static {
        PERCENT_FORMAT_ONEDOT.setMaximumFractionDigits(1);
        // PERCENT_FORMAT_ONEDOT.setMinimumFractionDigits(1);
    }

    /**
     * 以10进制为单位的K.
     */
    public static final long K10 = 1000;
    public static final long M10 = 1000 * K10;
    public static final long G10 = 1000 * M10;
    public static final long T10 = 1000 * G10;

    /**
     * K字节(KB)
     */
    public static final long K = 1024;
    /**
     * MB = 1024KB
     */
    public static final long M = 1024 * K;

    /**
     * GB = 1024MB
     */
    public static final long G = 1024 * M;

    /**
     * TB = 1024GB
     */
    public static final long T = 1024 * G;

    /**
     * Kbps(千位/秒)
     */
    public static final long Kbps = 1024;

    /**
     * Mbps = 1024Kbps
     */
    public static final long Mbps = 1024 * Kbps;

    /**
     * Gbps = 1024Mbps
     */
    public static final long Gbps = 1024 * Mbps;

    /**
     * Tbps = 1024Gbps
     */
    public static final long Tbps = 1024 * Gbps;

    /**
     * 按double类型的速率.
     */
    private static final double Kbpsf = 1024.0;

    public static final double Mbpsf = 1024 * Kbpsf;

    public static final double Gbpsf = 1024 * Mbpsf;

    public static final double Tbpsf = 1024 * Gbpsf;

    public static String bit2Byte(double value) {
        String lengthStr = "";
        double length = value / 8;
        if (length < K) {
            lengthStr = new StringBuilder().append((int) length).append("  B").toString();
        } else if (length < M) {
            lengthStr = new StringBuilder().append(TWODOT_FORMAT.format(length / 1024.0)).append(" KB").toString();
        } else if (length < G) {
            lengthStr = new StringBuilder().append(TWODOT_FORMAT.format(length / (K * 1024.0))).append(" MB")
                    .toString();
        } else if (length < T) {
            lengthStr = new StringBuilder().append(TWODOT_FORMAT.format(length / (M * 1024.0))).append(" GB")
                    .toString();
        } else {
            lengthStr = new StringBuilder().append(TWODOT_FORMAT.format(length / (G * 1024.0))).append(" TB")
                    .toString();
        }
        return lengthStr;
    }

    public static String format(double value) {
        return FORMAT.format(value);
    }

    /**
     * 得到带宽, 按照1000和1024的自适应.
     * 
     * @param ifSpeed
     * @return
     */
    public static String getBandWidth(double ifSpeed) {
        if (ifSpeed < Kbps) {
            return new StringBuilder().append(ONEDOT_FORMAT.format(ifSpeed)).append("  bps").toString();
        } else if (ifSpeed < Mbps) {
            if (ifSpeed % K10 == 0) {
                return new StringBuilder().append(ONEDOT_FORMAT.format(ifSpeed / K10)).append(" Kbps").toString();
            } else {
                return new StringBuilder().append(ONEDOT_FORMAT.format(ifSpeed / Kbpsf)).append(" Kbps").toString();
            }
        } else if (ifSpeed < Gbps) {
            if (ifSpeed % M10 == 0) {
                return new StringBuilder().append(ONEDOT_FORMAT.format(ifSpeed / M10)).append(" Mbps").toString();
            } else {
                return new StringBuilder().append(ONEDOT_FORMAT.format(ifSpeed / Mbpsf)).append(" Mbps").toString();
            }
        } else if (ifSpeed < Tbps) {
            if (ifSpeed % G10 == 0) {
                return new StringBuilder().append(ONEDOT_FORMAT.format(ifSpeed / G10)).append(" Gbps").toString();
            } else {
                return new StringBuilder().append(ONEDOT_FORMAT.format(ifSpeed / Gbpsf)).append(" Gbps").toString();
            }
        } else {
            if (ifSpeed % T10 == 0) {
                return new StringBuilder().append(ONEDOT_FORMAT.format(ifSpeed / T10)).append(" Tbps").toString();
            } else {
                return new StringBuilder().append(ONEDOT_FORMAT.format(ifSpeed / Tbpsf)).append(" Tbps").toString();
            }
        }
    }

    public static final String getBefore(long time) {
        return "";
    }

    public static String getByteLength(double length) {
        String lengthStr = "";
        if (length < K) {
            lengthStr = new StringBuilder().append((int) length).append("  B").toString();
        } else if (length < M) {
            lengthStr = new StringBuilder().append(TWODOT_FORMAT.format(length / 1024.0)).append(" KB").toString();
        } else if (length < G) {
            lengthStr = new StringBuilder().append(TWODOT_FORMAT.format(length / (K * 1024.0))).append(" MB")
                    .toString();
        } else if (length < T) {
            lengthStr = new StringBuilder().append(TWODOT_FORMAT.format(length / (M * 1024.0))).append(" GB")
                    .toString();
        } else {
            lengthStr = new StringBuilder().append(TWODOT_FORMAT.format(length / (G * 1024.0))).append(" TB")
                    .toString();
        }
        return lengthStr;
    }

    public static String getByteLength(float length) {
        String lengthStr = "";
        if (length < K) {
            lengthStr = new StringBuilder().append((int) length).append("  B").toString();
        } else if (length < M) {
            lengthStr = new StringBuilder().append(TWODOT_FORMAT.format(length / 1024.0)).append(" KB").toString();
        } else if (length < G) {
            lengthStr = new StringBuilder().append(TWODOT_FORMAT.format(length / (K * 1024.0))).append(" MB")
                    .toString();
        } else if (length < T) {
            lengthStr = new StringBuilder().append(TWODOT_FORMAT.format(length / (M * 1024.0))).append(" GB")
                    .toString();
        } else {
            lengthStr = new StringBuilder().append(TWODOT_FORMAT.format(length / (G * 1024.0))).append(" TB")
                    .toString();
        }
        return lengthStr;
    }

    /**
     * 得到时长, 返回小时单位.
     * 
     * @param time
     *            - 豪秒为单位
     * @return
     */
    public static String getHourStr(long time) {
        return TWODOT_FORMAT.format(1.0 * time / 3600000);
    }

    /**
     * 得到速率, 按照1024进制.
     * 
     * @param ifSpeed
     * @return
     */
    public static String getIfSpeedStr(double ifSpeed) {
        if (ifSpeed < Kbps) {
            return new StringBuilder().append(NODOT_FORMAT.format(ifSpeed)).append("  bps").toString();
        } else if (ifSpeed < Mbps) {
            return new StringBuilder().append(ONEDOT_FORMAT.format(ifSpeed / K10)).append(" Kbps").toString();
        } else if (ifSpeed < Gbps) {
            return new StringBuilder().append(ONEDOT_FORMAT.format(ifSpeed / M10)).append(" Mbps").toString();
        } else if (ifSpeed < Tbps) {
            return new StringBuilder().append(ONEDOT_FORMAT.format(ifSpeed / G10)).append(" Gbps").toString();
        } else {
            return new StringBuilder().append(ONEDOT_FORMAT.format(ifSpeed / T10)).append(" Tbps").toString();
        }
    }
    
    /**
     * 得到速率, 按照1024进制.保留两位小数
     * 
     * @param ifSpeed
     * @return
     */
    public static String getIfSpeedStrTwoDot(double ifSpeed) {
        if (ifSpeed < Kbps) {
            return new StringBuilder().append(NODOT_FORMAT.format(ifSpeed)).append("  bps").toString();
        } else if (ifSpeed < Mbps) {
            return new StringBuilder().append(TWODOT_FORMAT.format(ifSpeed / K10)).append(" Kbps").toString();
        } else if (ifSpeed < Gbps) {
            return new StringBuilder().append(TWODOT_FORMAT.format(ifSpeed / M10)).append(" Mbps").toString();
        } else if (ifSpeed < Tbps) {
            return new StringBuilder().append(TWODOT_FORMAT.format(ifSpeed / G10)).append(" Gbps").toString();
        } else {
            return new StringBuilder().append(TWODOT_FORMAT.format(ifSpeed / T10)).append(" Tbps").toString();
        }
    }

    /**
     * 按照指定保留小数点的格式来得到速率, 按照1024进制.
     * @param ifSpeed
     * @param dotFormat (推荐使用NumberUtils中提供的几个dotFormat)
     * @return
     */
    public static String getIfSpeedStr(double ifSpeed, NumberFormat dotFormat) {
        if (ifSpeed < Kbps) {
            return new StringBuilder().append(dotFormat.format(ifSpeed)).append("  bps").toString();
        } else if (ifSpeed < Mbps) {
            if (ifSpeed % K10 == 0) {
                return new StringBuilder().append(dotFormat.format(ifSpeed / K10)).append(" Kbps").toString();
            } else {
                return new StringBuilder().append(dotFormat.format(ifSpeed / Kbpsf)).append(" Kbps").toString();
            }
        } else if (ifSpeed < Gbps) {
            if (ifSpeed % M10 == 0) {
                return new StringBuilder().append(dotFormat.format(ifSpeed / M10)).append(" Mbps").toString();
            } else {
                return new StringBuilder().append(dotFormat.format(ifSpeed / Mbpsf)).append(" Mbps").toString();
            }
        } else if (ifSpeed < Tbps) {
            if (ifSpeed % G10 == 0) {
                return new StringBuilder().append(dotFormat.format(ifSpeed / G10)).append(" Gbps").toString();
            } else {
                return new StringBuilder().append(dotFormat.format(ifSpeed / Gbpsf)).append(" Gbps").toString();
            }

        } else {
            if (ifSpeed % T10 == 0) {
                return new StringBuilder().append(dotFormat.format(ifSpeed / T10)).append(" Tbps").toString();
            } else {
                return new StringBuilder().append(dotFormat.format(ifSpeed / Tbpsf)).append(" Tbps").toString();
            }
        }
    }

    /**
     * 以M为单位.
     * 
     * @param length
     * @return
     */
    public static String getMemStr(double length) {
        return NODOT_FORMAT.format(length / (K * 1024.0));
    }

    public static String getPercentStr(double value) {
        return PERCENT_FORMAT.format(value);
    }

    /**
     * 保留小数点的位数.
     * 
     * @param value
     * @param dot
     * @return
     */
    public static String getPercentStr(double value, int dot) {
        if (dot == 1) {
            return PERCENT_FORMAT_ONEDOT.format(value);
        } else {
            return PERCENT_FORMAT.format(value);
        }
    }

    /**
     * 得到时长, 豪秒为单位.
     * 
     * @param time
     * @return
     */
    public static String getTimeStr(long time) {
        if (time > 24 * 3600000) {
            return TWODOT_FORMAT.format(1.0 * time / (24 * 3600000)) + "D";
        } else if (time > 3600000) {
            return TWODOT_FORMAT.format(1.0 * time / 3600000) + "H";
        } else if (time > 60000) {
            return ONEDOT_FORMAT.format(1.0 * time / 60000) + "M";
        } else {
            return time / 1000 + "S";
        }
    }

    public static void main(String[] args) {
        System.out.println(getPercentStr(0.231));
        System.out.println(getPercentStr(0.235, 1));
    }

}
