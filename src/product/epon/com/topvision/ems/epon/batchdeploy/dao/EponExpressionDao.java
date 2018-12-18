/***********************************************************************
 * $Id: EponExpressionDao.java,v1.0 2013年12月4日 上午9:44:39 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.batchdeploy.dao;

import java.util.List;

import com.topvision.ems.epon.batchdeploy.domain.Target;
import com.topvision.framework.dao.BaseEntityDao;

/**
 * @author Bravin
 * @created @2013年12月4日-上午9:44:39
 *
 */
public interface EponExpressionDao extends BaseEntityDao<Target> {

    /**
     * 查询板卡级别 
     * @param entityId
     * @param slots
     * @return
     */
    List<Target> selectSlotTarget(Long entityId, List<Integer> slots);

    /**
     * 
     * @param entityId
     * @param ids
     * @param ports
     * @param includeUplink
     * @return
     */
    List<Target> selectPortTarget(List<Long> ids, List<Integer> ports, boolean includeUplink);

    /**
     * @param entityId
     * @param ids
     * @param llids
     * @return
     */
    List<Target> selectLlidTarget(List<Long> ids, List<Integer> llids);

    /**
     * @param entityId
     * @param ids
     * @param unis
     * @return
     */
    List<Target> selectUniTarget(List<Long> ids, List<Integer> unis);

}
