/***********************************************************************
 * $Id: RogueOnuService.java,v1.0 2017年6月17日 上午9:22:02 $
 * 
 * @author: lizongtian
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.onu.service;

import java.util.List;
import java.util.Map;

import com.topvision.ems.epon.onu.domain.OltOnuAttribute;

/**
 * @author lizongtian
 * @created @2017年6月17日-上午9:22:02
 *
 */
public interface RogueOnuService {

    void refreshSystemRogueCheck(Long entityId);

    void modifySystemRogueCheck(Long entityId, Integer status);

    void refreshPonPortRogueEntry(Long entityId);

    void modifyPonPortRogueSwitch(Long entityId, Long ponId, Integer status);

    void ponPortRogueOnuCheck(Long entityId, Long ponId, List<Integer> onuIds);

    void refreshOnuLaserSwitch(Long entityId);

    void refreshOnuLaserSwitch(Long entityId, Long onuId);

    void modifyOnuLaser(Long entityId, Long onuId, Integer onuLaser);

    List<OltOnuAttribute> loadRogueOnuList(Map<String, Object> queryMap);

    int queryRogueOnuCount(Map<String, Object> queryMap);

}
