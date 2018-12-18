/***********************************************************************
 * $Id: ResourceKeyAnalyzer.java,v1.0 2013-4-2 上午9:36:57 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.platform.zetaframework.util;


/**
 * @author Bravin
 * @created @2013-4-2-上午9:36:57
 *
 */
public class ResourceKeyAnalyzer {
    /**
     * 必须保证数组按照自然顺序排序，否则必须要执行sort后才能做二分搜索
     * 被拒绝的 国际化key字符列表：
     * 32 : blank space
     * 44 : , 
     * 91 ： [
     * 93 : ]
     * 123 : {
     * 125 : }
     * 34 : "
     * 39 : '
     */
    private static final int[] REJECT_KEYSTRING_CHAR_ARRAY = new int[] { 32, 34, 39, 44, 91, 93, 123, 125 };
    private static final int CHAR_CODE_NOT_FOUND = -1;
    private static final int MAX_KEY_STRING_LENGTH = 100;
    public static final String MODULE_SPERATOR = "/";
    public static final char KEYSTRING_STOP_TOKENIZER = '@';

    /**
     * 判断传入的字符串是否是一个合法的key
     * @param str
     * @return
     */
    public static boolean isKeyString(String str) {
        //不能为空
        if (str.length() == 0) {
            return false;
        }
        //首字符不能为数字
        char char0 = str.charAt(0);
        if (char0 > '0' && char0 < '9') {
            return false;
        }
        //由于涉及到效率的问题，故采用排除法，只要出现不合法的字符，并且长度较长，就认为是错误的
        if (str.length() > MAX_KEY_STRING_LENGTH) {//如果长度超过50，则认为这个是非国际化字符串
            return false;
        }
        byte[] res = str.getBytes();
        for (byte ch : res) {
            if (ch > 127 || ch < 0) { //国际化字符串只能是由 英文,数字, . _等组成，在这个范围之外的数字只要遇到了就认为这不是国际化字符串
                return false;
            }/* else if (Arrays.binarySearch(REJECT_KEYSTRING_CHAR_ARRAY, ch) > CHAR_CODE_NOT_FOUND) { //只要包含了 被拒绝的字符就认定不是一个I18N KEY
                return false;
             }*/
        }
        return true;
    }
}
