/***********************************************************************
 * $Id: DispersionEngineDao.java,v1.0 2015-3-26 下午1:50:58 $
 * 
 * @author: Fanzidong
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cm.dispersion.engine.dao;

import java.util.List;

import com.topvision.ems.cm.dispersion.engine.domain.OpticalNodeRelation;

/**
 * @author fanzidong
 * @created @2015-3-26-下午1:50:58
 * 
 */
public interface DispersionEngineDao {
    /**
     * 获取光节点关系数据
     * 
     * @return
     */
    List<OpticalNodeRelation> selectOpticalNodeRelation();
}
