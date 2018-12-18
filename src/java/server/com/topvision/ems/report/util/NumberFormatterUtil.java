/***********************************************************************
 * $Id: NumberFormatterUtil.java,v1.0 2013-6-22 上午11:14:37 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.report.util;

import java.text.NumberFormat;

/**
 * @author Bravin
 * @created @2013-6-22-上午11:14:37
 * 
 */
public class NumberFormatterUtil {
    private static final NumberFormat formater = NumberFormat.getInstance();

    /**
     * 仅保留一位小数点
     * 
     * @return
     */
    public static String formatDecimalTwo(Double number) {
        formater.setMaximumFractionDigits(2);
        return formater.format(number);
    }

    /**
     * 不保留一位小数点
     * 
     * @return
     */
    public static String formatDecimalZero(Double number) {
        formater.setMaximumFractionDigits(0);
        return formater.format(number);
    }
}
