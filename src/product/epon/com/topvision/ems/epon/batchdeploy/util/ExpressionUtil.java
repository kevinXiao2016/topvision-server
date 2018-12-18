/***********************************************************************
 * $Id: ExpressionUtil.java,v1.0 2013年12月4日 上午9:55:16 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.batchdeploy.util;

import java.util.ArrayList;
import java.util.List;

import com.topvision.ems.epon.batchdeploy.domain.Expression;

/**
 * @author Bravin
 * @created @2013年12月4日-上午9:55:16
 *
 */
public final class ExpressionUtil {

    /**
     * 解析表达式结构，转换成SLOT-PORT-LLID-UNI各级别的列表
     * @param expression
     */
    public static void parseExpresson(Expression expression) {
        String expre = expression.getExpression();
        if (isInvalid(expre)) {
            return;
        }
        String[] split = expre.split("/|:");
        expression.setLevel(split.length);
        for (int i = 0; i < split.length; i++) {
            String s = split[i];
            if (s.startsWith("(")) {
                s = s.replaceAll("\\(|\\)", "");
            }
            List<Integer> list = new ArrayList<Integer>();
            String[] split2 = s.split(",");
            for (String s2 : split2) {
                if (s2.matches("\\d+")) {
                    list.add(Integer.parseInt(s2));
                } else if ("*".equals(s2)) {
                    // 如果是 * ，则用Integer的极值取代，表示任意匹配
                    list.clear();
                    list.add(Integer.MAX_VALUE);
                    break;
                } else {
                    String[] s3 = s2.split("-");
                    int start = Integer.parseInt(s3[0]);
                    int end = Integer.parseInt(s3[1]);
                    while (start <= end) {
                        list.add(start++);
                    }
                }
            }
            switch (i) {
            case 0:
                expression.setSlots(list);
                break;
            case 1:
                expression.setPorts(list);
                break;
            case 2:
                expression.setLlids(list);
                break;
            case 3:
                expression.setUnis(list);
                break;
            default:
                break;
            }
        }
    }

    /**
     * @param expre
     * @return
     */
    private static boolean isInvalid(String expre) {
        if (expre == null || "".equals(expre)) {
            return true;
        }
        return false;
    }
}
