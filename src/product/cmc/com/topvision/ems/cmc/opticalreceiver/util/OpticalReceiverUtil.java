/***********************************************************************
 * $Id: OpticalReceiverUtil.java,v1.0 2016年9月18日 上午11:50:44 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.opticalreceiver.util;

/**
 * @author haojie
 * @created @2016年9月18日-上午11:50:44
 *
 */
public class OpticalReceiverUtil {
    /**
     * 按照光机Table的Index的规则，通过给定的index生成下一个index，光机的index使用低位开始的第二位为1开始编号，
     * 例如，第一个index为cmcIndex+2，第二个索引为cmcIndex + 2 +2。
     * 
     * @param index
     *            一个存在的index
     * @return 该index的下一个index
     */
    public static Long generateNextIndex(Long index) {
        return index + 2;
    }

}
