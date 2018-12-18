/***********************************************************************
 * $Id: BoardType4Agent.java,v1.0 2014年9月17日 下午2:51:20 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.olt.action;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.Arrays;

/**
 * agent判断bin文件类型是否匹配的方法：从bin文件流中读取第113-116之间的字节,转换为文件类型的int值
 * @author Bravin
 * @created @2014年9月17日-下午2:51:20
 *
 */
public class BoardType4Agent {
    public static final int E_FMGT_FILE_MPU = 0;
    public static final int E_FMGT_FILE_EPU = 1;
    public static final int E_FMGT_FILE_XPU = 2;
    public static final int E_FMGT_FILE_GPU = 3;
    public static final int E_FMGT_FILE_NPU = 4;
    public static final int E_FMGT_FILE_GEU = 5;
    public static final int E_FMGT_FILE_XGU = 6;
    public static final int E_FMGT_FILE_MPUB = 7;
    public static final int E_FMGT_FILE_BOOTROM = 8;
    public static final int E_FMGT_FILE_MPU_BAK = 9;
    public static final int E_FMGT_FILE_EPU_BAK = 10;
    public static final int E_FMGT_FILE_XPU_BAK = 11;
    public static final int E_FMGT_FILE_GPU_BAK = 12;
    public static final int E_FMGT_FILE_NPU_BAK = 13;
    public static final int E_FMGT_FILE_GEU_BAK = 14;
    public static final int E_FMGT_FILE_XGU_BAK = 15;
    public static final int E_FMGT_FILE_MPUB_BAK = 16;
    public static final int E_FMGT_FILE_BOOTROM_BAK = 17;
    public static final int E_FMGT_DIR_ONU = 18;
    public static final int E_FMGT_FILE_CONFIG = 19;
    public static final int E_FMGT_FILE_OEM = 20;
    public static final int E_FMGT_FILE_FPGA = 21;
    public static final int E_FMGT_FILE_MPUB_BOOTROM = 22;
    public static final int E_FMGT_FILE_MPUB_BOOTROM_BAK = 23;/* 版本兼容 */

    /**
     * 判断bin文件的类型
     * @param type
     * @param is
     * @return
     * @throws IOException
     */
    public static boolean compareType(int type, InputStream is) throws IOException {
        try {
            byte buffer[] = new byte[116];
            is.read(buffer, 0, 116);
            byte[] data = Arrays.copyOfRange(buffer, 113, 116);
            int value = 0;
            for (byte b : data) {
                value += b;
            }
            if (value == type) {
                return true;
            }
            return false;
        } finally {
            is.close();
        }
    }

    /**
     * 判断bin文件的类型,要求type的格式和上面的常量去掉E_FMGT_FILE_前缀后定义的格式一样
     * @param type
     * @param is
     * @return
     * @throws NoSuchFieldException
     * @throws SecurityException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws IOException
     */
    public static boolean compareType(String type, InputStream is) throws NoSuchFieldException, SecurityException,
            IllegalArgumentException, IllegalAccessException, IOException {
        Field field = BoardType4Agent.class.getField("E_FMGT_FILE_" + type.toUpperCase());
        int value = field.getInt(BoardType4Agent.class);
        return compareType(value, is);
    }

}
