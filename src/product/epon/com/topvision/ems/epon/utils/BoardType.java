/***********************************************************************
 * $Id: BoardTypeUtil.java,v1.0 2014年11月26日 下午3:54:23 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.utils;

/**
 *  1: non-board(0) 
    2: mpua(1) 
    3: mpub(2) 
    4: epua(3) 
    5: epub(4) 
    6: geua(5) 
    7: geub(6) 
    8: xgua(7) 
    9: xgub(8) 
    10: xguc(9) 
    11: xpua(10) 
    12: mpu-geua(11) 
    13: mpu-geub(12) 
    14: mpu-xguc(13)
    12: unknown(255) 
 * @author Bravin
 * @created @2014年11月26日-下午3:54:23
 *
 */
public class BoardType {
    public static final int NON_BOARD = 0;
    public static final int MPUA = 1;
    public static final int MPUB = 2;
    public static final int EPUA = 3;
    public static final int EPUB = 4;
    public static final int GEUA = 5;
    public static final int GEUB = 6;
    public static final int XGUA = 7;
    public static final int XGUB = 8;
    public static final int XGUC = 9;
    public static final int XPUA = 10;
    public static final int MPU_GEUA = 11;
    public static final int MPU_GEUB = 12;
    public static final int MPU_XGUC = 13;
    public static final int GPUA = 14;
    public static final int EPUC = 15;
    public static final int EPUD = 16;
    public static final int MEUA = 17;
    public static final int MEUB = 18;
    public static final int MEFA = 19;
    public static final int MEFB = 20;
    public static final int MEFC = 21;
    public static final int MEFD = 22;
    public static final int MGUA = 23;
    public static final int MGUB = 24;
    public static final int MGFA = 25;
    public static final int MGFB = 26;
    public static final int UNKNOWN = 255;

    /**
     *  
     * @param type
     * @return
     */
    public static String getName(Integer type) {
        if (type == null) {
            return "unknown";
        }
        switch (type) {
        case NON_BOARD:
            return "non-board";
        case MPUA:
            return "mpua";
        case MPUB:
            return "mpub";
        case EPUA:
            return "epua";
        case EPUB:
            return "epub";
        case GEUA:
            return "geua";
        case GEUB:
            return "geub";
        case XGUA:
            return "xgua";
        case XGUB:
            return "xgub";
        case XGUC:
            return "xguc";
        case XPUA:
            return "xpua";
        case MPU_GEUA:
            return "mpu-geua";
        case MPU_GEUB:
            return "mpu-geub";
        case MPU_XGUC:
            return "mpu-xguc";
        case EPUC:
            return "epuc";
        case EPUD:
            return "epud";
        case MEUA:
            return "meua";
        case MEUB:
            return "meub";
        case MEFA:
            return "mefa";
        case MEFB:
            return "mefb";
        case GPUA:
            return "gpua";
        case MEFC:
            return "mefc";
        case MEFD:
            return "mefd";
        case MGUA:
            return "mgua";
        case MGUB:
            return "mgub";
        case MGFA:
            return "mgfa";
        case MGFB:
            return "mgfb";
        default:
            return "unknown";
        }
    }

}
