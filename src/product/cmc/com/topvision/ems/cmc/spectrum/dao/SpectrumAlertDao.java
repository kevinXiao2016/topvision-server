/***********************************************************************
 * $Id: SpectrumAlertDao.java,v1.0 2015年3月11日 下午2:55:34 $
 * 
 * @author: Administrator
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.spectrum.dao;

/**
 * @author YangYi
 * @created @2015年3月11日-下午2:55:34
 * 
 */
public interface SpectrumAlertDao {

    /**
     * 判断频谱噪声告警是否已经存在
     * 
     * @param source
     * @return
     */
    boolean isSpectrumAlertExist(String source);
}
