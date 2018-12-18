/***********************************************************************
 * $Id: OnuSlaDao.java,v1.0 2013年10月25日 下午5:53:19 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.qos.dao;

import java.util.List;

import com.topvision.ems.epon.qos.domain.SlaTable;
import com.topvision.framework.dao.BaseEntityDao;
import com.topvision.framework.domain.BaseEntity;

/**
 * @author Bravin
 * @created @2013年10月25日-下午5:53:19
 *
 */
public interface OnuSlaDao extends BaseEntityDao<BaseEntity> {

    /**
     * 保存onu的sla
     * @param entityId
     * @param slaTables
     */
    void saveSlaTable(List<SlaTable> slaTables);

}
