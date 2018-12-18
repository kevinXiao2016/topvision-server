/***********************************************************************
 * $Id: CcmtsMaintainEngineDao.java,v1.0 2015-5-26 下午8:38:51 $
 * 
 * @author: Fanzidong
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cm.ccmtsmaintain.engine.dao;

import java.util.List;

import com.topvision.ems.cm.ccmtsmaintain.engine.domain.CcmtsChannel;

/**
 * @author fanzidong
 * @created @2015-5-26-下午8:38:51
 *
 */
public interface CcmtsMaintainEngineDao {
    /**
     * 获取CCMTS上行信道的频宽数据
     * 
     * @return
     */
    List<CcmtsChannel> selectCcmtsChannels();
}
