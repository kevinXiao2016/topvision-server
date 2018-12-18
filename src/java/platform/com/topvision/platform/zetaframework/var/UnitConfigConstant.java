package com.topvision.platform.zetaframework.var;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.topvision.exception.service.ConfigException;

@ZetaValueGetter("unitConfigConstant")
public class UnitConfigConstant {

    private static Map<String, Object> mapper = new HashMap<>();
    // 设备温度告警Id集合
    public static final List<Integer> DEVICETEMP_ALERTIDS = new ArrayList<Integer>();
    // 性能阀值温度告警Id集合
    public static final List<Integer> THRESHOLDTEMP_ALERTIDS = new ArrayList<Integer>();
    // 温度相关的性能阈值指标
    public static final List<String> TEMPRELATED_THRESHOLDS = new ArrayList<String>();
    // 电平相关的告警Id集合
    public static final List<Integer> POWERRELATED_ALERTIDS = new ArrayList<Integer>();
    static {
        // 设备温度告警Id集合
        DEVICETEMP_ALERTIDS.add(4103);
        DEVICETEMP_ALERTIDS.add(4105);
        DEVICETEMP_ALERTIDS.add(12313);
        DEVICETEMP_ALERTIDS.add(12314);
        DEVICETEMP_ALERTIDS.add(12323);
        DEVICETEMP_ALERTIDS.add(12324);
        DEVICETEMP_ALERTIDS.add(16404);
        DEVICETEMP_ALERTIDS.add(16405);
        DEVICETEMP_ALERTIDS.add(16423);
        DEVICETEMP_ALERTIDS.add(16424);
        // 性能阀值温度告警Id集合
        THRESHOLDTEMP_ALERTIDS.add(-840);
        THRESHOLDTEMP_ALERTIDS.add(-841);
        THRESHOLDTEMP_ALERTIDS.add(-842);
        THRESHOLDTEMP_ALERTIDS.add(-843);
        THRESHOLDTEMP_ALERTIDS.add(-844);
        THRESHOLDTEMP_ALERTIDS.add(-845);
        THRESHOLDTEMP_ALERTIDS.add(-832);
        THRESHOLDTEMP_ALERTIDS.add(-833);
        THRESHOLDTEMP_ALERTIDS.add(-838);
        THRESHOLDTEMP_ALERTIDS.add(-839);
        THRESHOLDTEMP_ALERTIDS.add(-734);
        THRESHOLDTEMP_ALERTIDS.add(-735);
        THRESHOLDTEMP_ALERTIDS.add(-755);
        THRESHOLDTEMP_ALERTIDS.add(-756);
        // 温度相关的性能阈值指标
        TEMPRELATED_THRESHOLDS.add("CC_DS_MODULE_TEMP");
        TEMPRELATED_THRESHOLDS.add("CC_ONU_MODULE_TEMP");
        TEMPRELATED_THRESHOLDS.add("CC_INSIDE_TEMP");
        TEMPRELATED_THRESHOLDS.add("CC_OUTSIDE_TEMP");
        TEMPRELATED_THRESHOLDS.add("CC_PON_TEMP");
        TEMPRELATED_THRESHOLDS.add("CC_PON_MODULE_TEMP");
        TEMPRELATED_THRESHOLDS.add("CC_US_MODULE_TEMP");
        TEMPRELATED_THRESHOLDS.add("OLT_BOARD_TEMP");
        TEMPRELATED_THRESHOLDS.add("OLT_PORT_TEMP");
        TEMPRELATED_THRESHOLDS.add("OLT_PON_OPT_TEMP");
        TEMPRELATED_THRESHOLDS.add("OLT_SNI_OPT_TEMP");
        TEMPRELATED_THRESHOLDS.add("ONU_CATV_TEMP");
        // 电平相关的告警Id集合
        POWERRELATED_ALERTIDS.add(-503);
        POWERRELATED_ALERTIDS.add(-510);
    }

    // CATV ONU 的输出电平,以dBμV存储,需要进行特殊处理
    public static final String ONU_CATV_RF = "ONU_CATV_RF";

    public static final String TEMP_UNIT = "tempUnit";
    public static final String POWER_UNIT = "elecLevelUnit";
    // 摄氏温度
    public static final String CENTI_TEMP_UNIT = "℃";
    // 华氏温度
    public static final String FAHR_TEMP_UNIT = "°F";
    // dBmV
    public static final String MILLI_VOLT_UNIT = "dBmV";
    // dBμV
    public static final String MICRO_VOLT_UNIT = "dBμV";

    public static Object get(String key) {
        return mapper.get(key);
    }

    public static Map<String, Object> getMapper() {
        return mapper;
    }

    public static void setMapper(Map<String, Object> mapper) {
        UnitConfigConstant.mapper = mapper;
    }

    public static void add(String key, Object value) {
        if (mapper == null) {
            mapper = new HashMap<>();
        }
        mapper.put(key, value);
    }

    /**
     * 将温度转化为°F进行显示
     * 
     * @param tempValue
     * @return
     */
    public static int translateTemperature(double tempValue) {
        double transValue = tempValue;
        String tempUnit = (String) get(TEMP_UNIT);
        if (tempUnit != null && FAHR_TEMP_UNIT.equals(tempUnit)) {
            transValue = tempValue * 9 / 5 + 32;
        }
        return (int) Math.round(transValue);
    }

    /**
     * 将华氏温度转换为摄氏度
     * 
     * @param tempValue
     * @return
     */
    public static int translateValueToCenti(double tempValue) {
        double centiValue = (tempValue - 32) * 5 / 9;
        return (int) Math.round(centiValue);
    }

    /**
     * 将℃为°F
     * 
     * @param tempValue
     * @return
     */
    public static double transCentiToF(double temp) {
        return temp * 9 / 5 + 32;
    }

    /**
     * 将°F为℃
     * 
     * @param tempValue
     * @return
     */
    public static double transFToCenti(double temp) {
        return (temp - 32) * 5 / 9;
    }

    /**
     * 将以摄氏度为单位的温度数值转换为带上正确单位的字符串
     * 
     * @param tempValue
     * @param tempUnit
     * @return
     */
    public static String formatTemperature(double tempValue, String tempUnit) {
        if (tempUnit == null) {
            return null;
        }
        double _value = tempValue;
        if (FAHR_TEMP_UNIT.equals(tempUnit)) {
            _value = tempValue * 9 / 5 + 32;
        }
        return Math.round(_value) + tempUnit;
    }

    /**
     * 解析设备告警温度信息 告警信息格式: ONU[ONU_2/2:1] temperature is too high(Temperature: 90 C, threshold: 10-80
     * C.)
     * 
     * @param alertMsg
     * @return
     */
    private static String parseDeviceTempAlert(String alertMsg) {
        String tempUnit = (String) get("tempUnit");
        if (tempUnit != null && FAHR_TEMP_UNIT.equals(tempUnit)) {
            // alertMsg
            String[] infoArr = alertMsg.split("\\(");
            if (infoArr.length == 2) {
                String[] msgArr = infoArr[1].split(",");
                String[] tempArr = msgArr[0].split(":");
                String[] thresholdArr = msgArr[1].split(":");
                String tempValue = tempArr[1].trim().split(" ")[0];
                String[] valueArr = thresholdArr[1].trim().split(" ");
                String lowerValue = valueArr[0].split("-")[0];
                String upperValue = valueArr[0].split("-")[1];
                String result = String.format("(Temperature: %s %s, threshold: %s-%s %s.)",
                        translateTemperature(Integer.parseInt(tempValue)), tempUnit,
                        translateTemperature(Integer.parseInt(lowerValue)),
                        translateTemperature(Integer.parseInt(upperValue)), tempUnit);
                return infoArr[0] + result;
            } else {
                throw new ConfigException("Temperature message format error, do not handle!");
            }
        } else {
            return alertMsg;
        }

    }

    /**
     * 解析性能阀值温度告警信息 告警信息格式: CMTS[00:24:68:50:30:3D]MAC模块温度(80 °C )性能越界
     * 
     * @param alertMsg
     * @return
     */
    private static String parseThresholdTempAlert(String alertMsg) {
        String tempUnit = (String) get(TEMP_UNIT);
        if (tempUnit != null && FAHR_TEMP_UNIT.equals(tempUnit)) {
            String[] msgArr = alertMsg.split("\\(");
            String[] tempArr;
            if (msgArr.length == 2) {
                tempArr = msgArr[1].split("\\)");
            } else {
                throw new ConfigException("Temperature message format error, do not handle!");
            }
            String tempValue = tempArr[0].trim().split(" ")[0];
            String result = msgArr[0] + "(" + translateTemperature(Integer.parseInt(tempValue)) + " " + tempUnit + ")"
                    + tempArr[1];
            return result;
        } else {
            return alertMsg;
        }
    }

    /**
     * 温度告警信息解析
     * 
     * @param alertId
     * @param alertMsg
     * @return
     */
    public static String parseUnitConfigAlertMsg(Integer alertId, String alertMsg) {
        if (DEVICETEMP_ALERTIDS.contains(alertId)) {
            return parseDeviceTempAlert(alertMsg);
        } else if (THRESHOLDTEMP_ALERTIDS.contains(alertId)) {
            return parseThresholdTempAlert(alertMsg);
        } else if (POWERRELATED_ALERTIDS.contains(alertId)) {
            return parsePowerAlertMsg(alertMsg);
        } else {
            return alertMsg;
        }
    }

    /**
     * 转换阈值模板中温度相关的阈值 value格式 ： 1_20_3_1#1_30_4_2
     * 
     * @param value
     * @return
     */
    public static String parseThresholdRuleValue(String value) {
        StringBuilder valueBuilder = new StringBuilder();
        String[] ruleArr = value.split("#");
        for (String ruleValue : ruleArr) {
            String[] valueArr = ruleValue.split("_");
            Integer tempValue;
            try {
                tempValue = translateTemperature(Double.parseDouble(valueArr[1]));
                valueBuilder.append("#").append(valueArr[0]).append("_").append(tempValue).append("_")
                        .append(valueArr[2]).append("_").append(valueArr[3]);
            } catch (Exception e) {
                throw new ConfigException("Threshold rule Temperature value format is error", e.getCause());
            }
        }
        return valueBuilder.substring(1);
    }

    /**
     * 转换阈值模板中配置温度相关的阈值为摄氏度 value格式 ： 1_20_3_1#1_30_4_2
     * 
     * @param value
     * @return
     */
    public static String praseRuleValueToCenti(String value) {
        StringBuilder valueBuilder = new StringBuilder();
        String[] ruleArr = value.split("#");
        for (String ruleValue : ruleArr) {
            String[] valueArr = ruleValue.split("_");
            try {
                int tempValue = translateValueToCenti(Double.parseDouble(valueArr[1]));
                valueBuilder.append("#").append(valueArr[0]).append("_").append(tempValue).append("_")
                        .append(valueArr[2]).append("_").append(valueArr[3]);
            } catch (Exception e) {
                throw new ConfigException("Threshold rule Temperature value format is error", e.getCause());
            }
        }
        return valueBuilder.substring(1);
    }

    /**
     * 将系统单位值转换为dBμV
     * 
     * @param powerValue
     * @return
     */
    public static double parsePowerValue(double powerValue) {
        String powerUnit = (String) get(POWER_UNIT);
        if (powerUnit != null && !MILLI_VOLT_UNIT.equals(powerUnit)) {
            return powerValue + 60;
        }
        return powerValue;
    }

    /**
     * 将系统单位值转换为dBmV
     * 
     * @param powerValue
     * @return
     */
    public static double transPowerToDBmV(double powerValue) {
        String powerUnit = (String) get(POWER_UNIT);
        if (powerUnit != null && !MILLI_VOLT_UNIT.equals(powerUnit)) {
            return powerValue - 60;
        }
        return powerValue;
    }

    /**
     * 将在dBμV下配置的值转换为dBmV
     * 
     * @param powerValue
     * @return
     */
    public static double transDBμVToDBmV(double power) {
        return power - 60;
    }

    /**
     * 将在dBmV转换为dBμV
     * 
     * @param powerValue
     * @return
     */
    public static double transDBmVToDBμV(double power) {
        return power + 60;
    }

    /**
     * 转换电平告警
     * 
     * @param alertMsg
     * @return
     */
    private static String parsePowerAlertMsg(String alertMsg) {
        String powerUnit = (String) get(POWER_UNIT);
        if (powerUnit != null && !MILLI_VOLT_UNIT.equals(powerUnit)) {
            String[] msgArr = alertMsg.split(":");
            String[] powerArr;
            if (msgArr.length == 2) {
                powerArr = msgArr[1].split(" ");
            } else {
                throw new ConfigException("Power message format error, do not handle!");
            }
            String powerValue = powerArr[0].trim();
            String result = msgArr[0] + ":" + parsePowerValue(Double.parseDouble(powerValue)) + " " + powerUnit;
            return result;
        } else {
            return alertMsg;
        }
    }

    /**
     * 转换阈值模板中电平相关的阈值 value格式 ： 1_20_3_1#1_30_4_2
     * 
     * @param value
     * @return
     */
    public static String parseRuleDBμVToDBmV(String value) {
        StringBuilder valueBuilder = new StringBuilder();
        String[] ruleArr = value.split("#");
        for (String ruleValue : ruleArr) {
            String[] valueArr = ruleValue.split("_");
            Double powerValue;
            try {
                powerValue = transDBμVToDBmV(Double.parseDouble(valueArr[1]));
                valueBuilder.append("#").append(valueArr[0]).append("_").append(powerValue).append("_")
                        .append(valueArr[2]).append("_").append(valueArr[3]);
            } catch (Exception e) {
                throw new ConfigException("Threshold rule power value format is error", e.getCause());
            }
        }
        return valueBuilder.substring(1);
    }

    /**
     * 转换阈值模板中电平相关的阈值 value格式 ： 1_20_3_1#1_30_4_2
     * 
     * @param value
     * @return
     */
    public static String parseRuleDBmVToDBμV(String value) {
        StringBuilder valueBuilder = new StringBuilder();
        String[] ruleArr = value.split("#");
        for (String ruleValue : ruleArr) {
            String[] valueArr = ruleValue.split("_");
            Double powerValue;
            try {
                powerValue = transDBmVToDBμV(Double.parseDouble(valueArr[1]));
                valueBuilder.append("#").append(valueArr[0]).append("_").append(powerValue).append("_")
                        .append(valueArr[2]).append("_").append(valueArr[3]);
            } catch (Exception e) {
                throw new ConfigException("Threshold rule power value format is error", e.getCause());
            }
        }
        return valueBuilder.substring(1);
    }
}
