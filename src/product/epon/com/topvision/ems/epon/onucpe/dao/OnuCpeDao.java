/***********************************************************************
 * $Id: OnuCpeDao.java,v1.0 2016年7月6日 上午10:26:09 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.onucpe.dao;

import java.util.List;

import com.topvision.ems.epon.onucpe.domain.OnuUniCpe;
import com.topvision.ems.epon.performance.domain.OnuCpe;
import com.topvision.ems.epon.performance.domain.OnuUniCpeCount;
import com.topvision.framework.dao.Dao;

/**
 * @author Bravin
 * @created @2016年7月6日-上午10:26:09
 *
 */
public interface OnuCpeDao extends Dao {

    /**
     * @param onuId
     * @param limit 
     * @param start 
     * @return
     */
    List<OnuUniCpe> selectOltUniCpeList(Long entityId, int start, int limit);

    /**
     * @param onuId
     * @return
     */
    List<OnuUniCpe> selectOnuUniCpeList(Long onuId);

    /**
     * @param onuId
     */
    void deleteCpeListByOnuId(Long onuId);

    /**
     * @param cpeList
     */
    void batchinsertOnuCpe(List<OnuCpe> cpeList);

    /**
     * @param oltId
     * @return
     */
    int selectOltUniCpeListCount(Long oltId);

    /**
     * @param onuId
     */
    void deleteCpeCountListByOnuId(Long onuId);

    /**
     * @param onuUniCpeCounts
     */
    void batchInsertOnuCpeCount(List<OnuUniCpeCount> onuUniCpeCounts);

}
