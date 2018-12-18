/***********************************************************************
 * $Id: RogueOnuDao.java,v1.0 2017年6月17日 上午9:23:54 $
 * 
 * @author: lizongtian
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.onu.dao;

import java.util.List;
import java.util.Map;

import com.topvision.ems.epon.onu.domain.OltOnuAttribute;
import com.topvision.ems.epon.onu.domain.TopOnuLaserEntry;
import com.topvision.ems.epon.onu.domain.TopPonPortRogueEntry;

/**
 * @author lizongtian
 * @created @2017年6月17日-上午9:23:54
 *
 */
public interface RogueOnuDao {

    void updateOltRogueSwitch(Long entityId, Integer rogueSwitch);

    void batchInsertOrUpdatePonRogueInfo(Long entityId, List<TopPonPortRogueEntry> ponPortRogueList);

    void batchInsertOrUpdateOnuLaser(Long entityId, List<TopOnuLaserEntry> onuLaserList);

    void updatePortRogueSwitch(Long ponId, Integer portRogueSwitch);

    void updateOnuRogueStatus(Long onuId, Integer rogueOnu);

    void batchUpdateOnuRogueStatus(List<OltOnuAttribute> onuList);

    void updateOnuLaserSwitch(Long onuId, Integer onuLaserSwitch);

    List<OltOnuAttribute> queryRogueOnuList(Map<String, Object> queryMap);

    int queryRogueOnuCount(Map<String, Object> queryMap);

    void changeOnuRogueStatus(Long entityId, Long onuIndex);

    void insertOrupdateOnuLaserSwitch(TopOnuLaserEntry topOnuLaserEntry);

}
