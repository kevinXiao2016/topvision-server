/***********************************************************************
 * $Id: OltBfsxFacadeImpl.java,v1.0 2014年9月25日 上午9:36:34 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.olt.engine;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.topvision.ems.engine.executor.EmsFacade;
import com.topvision.ems.epon.mirror.domain.OltSniMirrorConfig;
import com.topvision.ems.epon.olt.domain.OltAttribute;
import com.topvision.ems.epon.olt.domain.OltFanAttribute;
import com.topvision.ems.epon.olt.domain.OltFanStatus;
import com.topvision.ems.epon.olt.domain.OltPonAttribute;
import com.topvision.ems.epon.olt.domain.OltPowerAttribute;
import com.topvision.ems.epon.olt.domain.OltPowerStatus;
import com.topvision.ems.epon.olt.domain.OltSlotAttribute;
import com.topvision.ems.epon.olt.domain.OltSlotStatus;
import com.topvision.ems.epon.olt.domain.OltSniAttribute;
import com.topvision.ems.epon.olt.facade.OltBfsxFacade;
import com.topvision.ems.epon.ponprotect.domain.OltPonProtectConfig;
import com.topvision.ems.epon.trunk.domain.OltSniTrunkConfig;
import com.topvision.framework.annotation.Facade;
import com.topvision.framework.snmp.SnmpExecutorService;
import com.topvision.framework.snmp.SnmpParam;
import com.topvision.framework.utils.EponIndex;

/**
 * @author Bravin
 * @created @2014年9月25日-上午9:36:34
 *
 */
@Facade("oltBfsxFacade")
public class OltBfsxFacadeImpl extends EmsFacade implements OltBfsxFacade {
    @Autowired
    private SnmpExecutorService snmpExecutorService;

    @Override
    public Map<String, Object> bfsxOltInfo(SnmpParam snmpParam) {
        Map<String, Object> map = new HashMap<String, Object>();
        long entityId = snmpParam.getEntityId();
        OltAttribute attr = snmpExecutorService.getData(snmpParam, OltAttribute.class);
        // 把启动时长换算为服务器的时刻，以便界面显示启动时长 ,此处的时间单位为10毫秒，不是SNMP4J标准的毫米
        attr.setOltDeviceUpTime(System.currentTimeMillis() - attr.getOltDeviceUpTime() * 10);
        map.put("oltAttribute", attr);
        List<OltSlotAttribute> slots = snmpExecutorService.getTable(snmpParam, OltSlotAttribute.class);
        if (slots != null) {
            for (OltSlotAttribute slot : slots) {
                slot.setEntityId(entityId);
                slot.setSlotIndex(EponIndex.getSlotIndex(slot.getSlotNo().intValue()));
            }
        }
        map.put("slots", slots);
        List<OltSlotStatus> slotStatus = snmpExecutorService.getTable(snmpParam, OltSlotStatus.class);
        for (OltSlotStatus $o : slotStatus) {
            $o.setEntityId(entityId);
        }
        map.put("slotStatus", slotStatus);
        List<OltPowerAttribute> powers = snmpExecutorService.getTable(snmpParam, OltPowerAttribute.class);
        for (OltPowerAttribute $o : powers) {
            $o.setEntityId(entityId);
        }
        map.put("powers", powers);
        List<OltPowerStatus> powerStatus = snmpExecutorService.getTable(snmpParam, OltPowerStatus.class);
        for (OltPowerStatus $o : powerStatus) {
            $o.setEntityId(entityId);
        }
        map.put("powerStatus", powerStatus);
        List<OltFanAttribute> fans = snmpExecutorService.getTable(snmpParam, OltFanAttribute.class);
        for (OltFanAttribute $o : fans) {
            $o.setEntityId(entityId);
        }
        map.put("fans", fans);
        List<OltFanStatus> fanStatus = snmpExecutorService.getTable(snmpParam, OltFanStatus.class);
        for (OltFanStatus $o : fanStatus) {
            $o.setEntityId(entityId);
        }
        map.put("fanStatus", fanStatus);
        List<OltPonAttribute> pons = snmpExecutorService.getTable(snmpParam, OltPonAttribute.class);
        for (OltPonAttribute $o : pons) {
            $o.setEntityId(entityId);
        }
        map.put("pons", pons);
        List<OltSniAttribute> snis = snmpExecutorService.getTable(snmpParam, OltSniAttribute.class);
        for (OltSniAttribute $o : snis) {
            $o.setEntityId(entityId);
        }
        map.put("snis", snis);
        return map;
    }

    @Override
    public List<OltSniMirrorConfig> bfsxOltMirror(SnmpParam snmpParam) {
        return snmpExecutorService.getTable(snmpParam, OltSniMirrorConfig.class);
    }

    @Override
    public List<OltSniTrunkConfig> bfsxOltTrunk(SnmpParam snmpParam) {
        return snmpExecutorService.getTable(snmpParam, OltSniTrunkConfig.class);
    }

    @Override
    public List<OltPonProtectConfig> bfsxOltPonProtectGroup(SnmpParam snmpParam) {
        return snmpExecutorService.getTable(snmpParam, OltPonProtectConfig.class);
    }

}
