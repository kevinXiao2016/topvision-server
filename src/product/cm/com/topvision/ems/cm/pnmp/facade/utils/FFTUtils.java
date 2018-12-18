package com.topvision.ems.cm.pnmp.facade.utils;

/**
 * Created by jay on 17-6-22.
 */
public class FFTUtils {
    public static int parse(String str) {
        Integer i = Integer.parseInt(str,16) & 0xFFFF;
        int flag = i & 0x8000;
        if (flag == 0x8000) {
            i = ~i + 1;
            i = i & 0xFFFF;
            i= -1 * i;
        }
        return i;
    }
    public static int parse(int num) {
        Integer i = num & 0xFFFF;
        int flag = i & 0x8000;
        if (flag == 0x8000) {
            i = ~i + 1;
            i = i & 0xFFFF;
            i= -1 * i;
        }
        return i;
    }

    public static void main(String[] args) {
//        System.out.println(FFTUtils.parse("FFFF"));
//        System.out.println(FFTUtils.parse("0002"));
//        System.out.println(FFTUtils.parse("0001"));
//        System.out.println(FFTUtils.parse("0003"));
//        System.out.println(FFTUtils.parse("FFFC"));
    }
}
