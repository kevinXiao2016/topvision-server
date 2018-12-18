/***********************************************************************
 * $Id: SpectrumSmoothUtil.java,v1.0 2015年3月12日 下午8:23:57 $
 * 
 * @author: Administrator
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.spectrum.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.topvision.platform.SystemConstants;

/**
 * @author YangYi
 * @created @2015年3月12日-下午8:23:57
 * 
 */
public class SpectrumSmoothUtil {
    private static final Logger logger = LoggerFactory.getLogger(SpectrumSmoothUtil.class);
    private static Properties properties = null;

    /**
     * 为了去掉伪噪声，需要采用拉平算法，对伪噪声点取前一个点的值
     * @param dataList
     * @return
     */
    public static List<List<Number>> leveling(List<List<Number>> dataList) {
        List<Double> smoothPoints = getPoints();
        for (int i = 1; i < dataList.size(); i++) {
            List<Number> fc = dataList.get(i);
            if (smoothPoints.indexOf(fc.get(0)) > -1) {
                fc.set(1,dataList.get(i - 1).get(1));
            }
        }
        return dataList;
    }

    /**
     * Fc-2处的数值取Fc-5 ~ Fc-2处的平均值 4点平均 ****** Fc-1处的数值取Fc-5 ~ Fc-1处的平均值 5点平均 ***********************
     * Fc处的数值取 Fc-5 ~ Fc+5处的平均值 11点平均 ****** Fc+1处的数值取Fc+1 ~ Fc+5处的平均值 5点平均 ***********************
     * Fc+2处的数值取Fc+2 ~ Fc+5处的平均值 4点平均
     * 
     * @param dataList
     * @return
     */
    @Deprecated
    public static List<List<Number>> smooth(List<List<Number>> dataList) {
        // DecimalFormat df = new DecimalFormat("#.00");
        List<Double> smoothPoints = getPoints();
        for (int i = 0; i < dataList.size(); i++) {
            List<Number> fc = dataList.get(i);
            // Double x = Double.valueOf(df.format());
            if (smoothPoints.indexOf(fc.get(0)) > -1) {
                List<Double> listY = new ArrayList<Double>(11);// 存放Fc-5~Fc+5 共11个点的Y值
                for (int j = 0; j < 11; j++) {
                    listY.add(j, dBmV2mV(dataList.get(i - 5 + j).get(1).doubleValue()));// 转换为以mV为单位
                }
                // Fc-2处的数值取Fc-5 ~ Fc-2处的平均值 4点平均
                List<Number> fc_2 = dataList.get(i - 2);
                double fc_2_y = (listY.get(0) + listY.get(1) + listY.get(2) + listY.get(3)) / 4; // 取平均
                fc_2.set(1, mV2dBmV(fc_2_y));// 转换回以dBmV为单位
                // Fc-1处的数值取Fc-5 ~ Fc-1处的平均值 5点平均
                List<Number> fc_1 = dataList.get(i - 1);
                double fc_1_y = (listY.get(0) + listY.get(1) + listY.get(2) + listY.get(3) + listY.get(4)) / 5;
                fc_1.set(1, mV2dBmV(fc_1_y));
                // Fc 处的数值取 Fc-5 ~ Fc+5处的平均值 11点平均
                double fc_y = (listY.get(0) + listY.get(1) + listY.get(2) + listY.get(3) + listY.get(4) + listY.get(5)
                        + listY.get(6) + listY.get(7) + listY.get(8) + listY.get(9) + listY.get(10)) / 11;
                fc.set(1, mV2dBmV(fc_y));
                // Fc+1处的数值取Fc+1 ~ Fc+5处的平均值 5点平均
                List<Number> fc1 = dataList.get(i + 1);
                double fc_y_1 = (listY.get(6) + listY.get(7) + listY.get(8) + listY.get(9) + listY.get(10)) / 5;
                fc1.set(1, mV2dBmV(fc_y_1));
                // Fc+2处的数值取Fc+2 ~ Fc+5处的平均值 4点平均
                List<Number> fc2 = dataList.get(i + 2);
                double fc_y_2 = (listY.get(7) + listY.get(8) + listY.get(9) + listY.get(10)) / 4;
                fc2.set(1, mV2dBmV(fc_y_2));
            }
        }
        return dataList;
    }

    /**
     * 由dBmV为单位转化为mV为单位
     * 
     * @param dBmV
     * @return
     */
    private static double dBmV2mV(double dBmV) {
        return Math.pow(10, dBmV / 20);
    }

    /**
     * 由mV为单位转化为dBmV为单位
     * 
     * @param mV
     * @return
     */
    private static double mV2dBmV(double mV) {
        return Math.log10(mV) * 20;
    }

    /**
     * 获取需要平滑的频点默认情况下为 10.24MHz，30.72MHz，40.96MH
     * 
     * @return List<Double>
     */
    public static List<Double> getPoints() {
        List<Double> list = new ArrayList<Double>();
        if (properties == null) {
            try {
                InputStream is = new FileInputStream(new File(SystemConstants.WEB_INF_REAL_PATH
                        + "/conf/smoothPointsII.properties"));
                properties = new Properties();
                properties.load(is);
            } catch (IOException e) {
                logger.error("SpectrumSmoothUtil open file smoothPointsII.properties error", e);
            }
            try {
                File old = new File(SystemConstants.WEB_INF_REAL_PATH
                        + "/conf/smoothPoints.properties");
                if (old.exists() && old.isFile()) {
                    old.delete();
                }
            } catch (Exception e) {
                logger.error("SpectrumSmoothUtil delete smoothPoints.properties error", e);
            }

        }
        String key = "frequency";
        String value = properties.getProperty(key);
        if (value != null) {
            String[] fres = value.split(",");
            for (int i = 0; i < fres.length; i++) {
                list.add(Double.valueOf(fres[i]));
            }
        }
        return list;
    }

}
