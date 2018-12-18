/***********************************************************************
 * $Id: SpectrumCalculateUtil.java,v1.0 2014-3-1 上午10:11:19 $
 * 
 * @author: YangYi
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.spectrum.utils;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

/**
 * 频谱数据的测试工具类
 * 
 * @author YangYi
 * @created @2014-3-1-上午10:11:19
 * 
 */
public class SpectrumTestUtil {
    private static HashMap<Double, Double> specialPoints = new HashMap<Double, Double>();

    public static HashMap<Double, Double> getSpeactionPoints() {
        return specialPoints;
    }

    public static void addSpeactionPoints(Double freq, Double power) {
        specialPoints.put(freq, power + 60); // 转化为dbμV
    }

    public static void deleteTestPoints(Double freq) {
        specialPoints.remove(freq);
    }

    public static void addTestPoints(List<List<Number>> list) {
        for (List<Number> l : list) {
            Double freq = (Double) l.get(0);
            if (specialPoints.containsKey(freq)) {
                l.set(1, specialPoints.get(freq));
            }
        }
    }

    /**
     * 压缩数据，10倍压缩
     * 
     * @param originalList
     *            原始数据List
     * @return 压缩后数据List
     */
    public static List<List<Number>> compressData(List<List<Number>> originalList) {
        List<List<Number>> nowValueList = new ArrayList<List<Number>>();
        // int counter = 0;
        // for (List<Number> l : originalList) {
        // counter++;
        // if (counter == 10) {
        // nowValueList.add(l);
        // counter = 0;
        // }
        // }
        // 下面一句为测试假数据
        // if (SystemConstants.isDevelopment) {
        nowValueList = SpectrumTestUtil.generateTestData2(originalList);
        // }
        // nowValueList = originalList;
        return nowValueList;
    }

    public static List<List<Number>> generateTestData(List<List<Number>> originalList) {
        Random powerRandom = new Random();
        Random freqRandom = new Random();
        int nextPossible = 0;
        int powerMax = 1;
        for (List<Number> l : originalList) {
            if (nextPossible > 0) {
                l.set(1, l.get(1).doubleValue() + powerRandom.nextInt(powerMax));
                nextPossible--;
                continue;
            }
            int freqGate = freqRandom.nextInt(1000);
            if (freqGate < 20) {
                powerMax = (int) (l.get(0).doubleValue() * 0.5) + 1;
                l.set(1, l.get(1).doubleValue() + powerRandom.nextInt(powerMax));
                nextPossible = freqRandom.nextInt(4) + 2;
            }
        }
        return originalList;
    }

    public static List<List<Number>> generateTestData2(List<List<Number>> originalList) {
        DecimalFormat df = new DecimalFormat("#.00");
        List<List<Number>> list = new ArrayList<List<Number>>();
        Random powerRandom = new Random();
        Random plusRandom = new Random();
        int j = 0;
        for (List<Number> l : originalList) {
            boolean plus = plusRandom.nextBoolean();
            double d = 0d;
            if (plus == true) {
                d = l.get(1).doubleValue() + powerRandom.nextInt(20);
            } else {
                d = l.get(1).doubleValue() - powerRandom.nextInt(20);
            }
            l.set(0, Double.valueOf(df.format(l.get(0))));
            l.set(1, Double.valueOf(df.format(d)));
            list.add(l);
            j++;
            if (j == 1100) {
                l.set(1, Double.valueOf(df.format(65d)));
            }
        }
        return list;
    }

    public static void main(String args[]) {
        Random plusRandom = new Random();
        System.out.println(plusRandom.nextInt(2));
    }

}