/***********************************************************************
 * $Id: CmPollTaskBuildDao.java,v1.0 2015年3月6日 下午2:29:45 $
 * 
 * @author: loyal
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cm.cmpoll.taskbuild.dao;

import java.util.List;

import com.topvision.ems.cmc.facade.domain.CmAttribute;
import com.topvision.ems.cm.cmpoll.facade.domain.CmPollTask;
import com.topvision.ems.cm.cmpoll.taskbuild.domain.CmPollAttribute;
import com.topvision.framework.dao.BaseEntityDao;

/**
 * @author loyal
 * @created @2015年3月6日-下午2:29:45
 * 
 */
public interface CmPollTaskBuildDao extends BaseEntityDao<CmPollTask> {
    /**
     * 获取cmattribute表中cmId>cmId的count条数据
     * 
     * @param start
     * @param count
     * @return
     */
    List<CmAttribute> selectCmListCountN(Long cmId, Long count);

    /**
     * 获取所有带IP的设备ID
     * 
     * @param start
     * @param count
     * @return
     */
    List<Long> selectEntityIdWithIp(Long type);

    /**
     * 获取设备下CM列表
     * 
     * @param start
     * @param count
     * @return
     */
    List<CmPollAttribute> selectCmListByEntityId(Long entityId);
    
    /**
     * 获取CM总数
     * @return
     */
    Long getCmOnLineNum();

}
