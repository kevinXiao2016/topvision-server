/***********************************************************************
 * $Id: RowHandler.java,v1.0 2013年12月23日 下午3:09:22 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.platform.zetaframework.util;

/**
 * @author Bravin
 * @created @2013年12月23日-下午3:09:22
 *
 */
public interface RowHandler {
    public static final boolean CONTINUE = false;
    public static final boolean TERMINATE = true;

    /**
     * 针对EXCEL的每一行，做回调处理
     * @param row
     * @return 如果返回true，则停止遍历Excel
     */
    Boolean handleRow(Row row);
}
