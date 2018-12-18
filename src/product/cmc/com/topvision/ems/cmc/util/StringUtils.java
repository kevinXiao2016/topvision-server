/***********************************************************************
 * $Id: StringUtils.java,v1.0 2013-4-2 上午10:58:32 $
 * 
 * @author: lzs
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.util;

/**
 * 对于字符串处理的工具类
 * @author lzs
 * @created @2013-4-2-上午10:58:32
 *
 */
public class StringUtils {

    /**
     * 空字符串，长度为0
     */
   private static final String EMPTY = "";
   /**
    * 字符串 0
    */
   private static final String ZERO = "0";
   /**
    * 判断字符串是否非空
    * 判断依据：不等于null,也不能是只包含空格的字符串
    * @param str
    * @return
    */
   public static boolean isEmpty(String str){
       return str==null||EMPTY.equals(str.trim());
   }
   /**
    * 判断一个字符串是否是 0
    * @param str
    * @return
    */
   public static boolean isZERO(String str){
       return ZERO.equals(str);
   }
}
