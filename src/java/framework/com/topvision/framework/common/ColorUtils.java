package com.topvision.framework.common;

import java.awt.Color;
import java.util.Random;

/**
 * "#4572A7,#AA4643,#89A54E,#80699B,#3D96AE,#DB843D,#92A8CD,#A47D7C,#B5CA92"
 * @author Bravin
 * @created @2013-5-16-上午11:54:18
 *
 */
public final class ColorUtils {
    private static final String[] DEFAULT_COLOR = new String[] { "#4572A7", "#AA4643", "#89A54E", "#80699B", "#3D96AE",
            "#DB843D", "#92A8CD", "#A47D7C", "#B5CA92" };

    public static Color decode(String str) {
        if (str == null || str.equals("")) {
            return null;
        }
        String temp = str;
        if (str.charAt(0) == '#') {
            temp = str.substring(1);
        }
        String r = "0";
        String g = "0";
        String b = "0";
        if (temp.length() == 3) {
            r = String.valueOf(temp.charAt(0));
            g = String.valueOf(temp.charAt(1));
            b = String.valueOf(temp.charAt(2));
        } else {
            r = temp.substring(0, 2);
            g = temp.substring(2, 4);
            b = temp.substring(4);
        }
        return new Color(Integer.parseInt(r, 16), Integer.parseInt(g, 16), Integer.parseInt(b, 16));
    }

    /**
     * 生成一个随机颜色
     * @return
     */
    public static String generateColor(int counter) {
        if (counter > -1 && counter < 9) {
            return DEFAULT_COLOR[counter];
        }
        String r, g, b;
        Random random = new Random();
        r = Integer.toHexString(random.nextInt(256)).toUpperCase();
        g = Integer.toHexString(random.nextInt(256)).toUpperCase();
        b = Integer.toHexString(random.nextInt(256)).toUpperCase();

        r = r.length() == 1 ? "0" + r : r;
        g = g.length() == 1 ? "0" + g : g;
        b = b.length() == 1 ? "0" + b : b;

        return r + g + b;
    }

    /**
     * 创建一个随机颜色
     * @return
     */
    public static String generateColor() {
        return generateColor(-1);
    }


}
