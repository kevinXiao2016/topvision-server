/***********************************************************************
 * $Id: OltBfsxServiceImpl.java,v1.0 2014年9月24日 上午11:33:18 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.olt.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.devicesupport.version.service.DeviceVersionService;
import com.topvision.ems.epon.mirror.dao.OltMirrorDao;
import com.topvision.ems.epon.mirror.domain.OltSniMirrorConfig;
import com.topvision.ems.epon.olt.dao.OltDao;
import com.topvision.ems.epon.olt.dao.OltPonDao;
import com.topvision.ems.epon.olt.dao.OltSlotDao;
import com.topvision.ems.epon.olt.dao.OltSniDao;
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
import com.topvision.ems.epon.olt.service.OltBfsxService;
import com.topvision.ems.epon.olt.service.OltSlotService;
import com.topvision.ems.epon.onu.service.RogueOnuService;
import com.topvision.ems.epon.ponprotect.dao.OltPonProtectDao;
import com.topvision.ems.epon.ponprotect.domain.OltPonProtectConfig;
import com.topvision.ems.epon.trunk.dao.OltTrunkDao;
import com.topvision.ems.epon.trunk.domain.OltSniTrunkConfig;
import com.topvision.ems.epon.trunk.service.OltTrunkService;
import com.topvision.ems.epon.vlan.service.SniVlanService;
import com.topvision.ems.network.service.EntityService;
import com.topvision.framework.service.BaseService;
import com.topvision.framework.snmp.SnmpParam;
import com.topvision.platform.facade.FacadeFactory;
import com.topvision.platform.message.event.EntityValueEvent;
import com.topvision.platform.message.event.EntityValueListener;
import com.topvision.platform.message.service.MessageService;

/**
 * @author Bravin
 * @created @2014年9月24日-上午11:33:18
 *
 */
@Service
public class OltBfsxServiceImpl extends BaseService implements OltBfsxService {
    @Autowired
    private FacadeFactory facadeFactory;
    @Autowired
    private EntityService entityService;
    @Autowired
    private OltSlotService oltSlotService;
    @Autowired
    private OltSlotDao oltSlotDao;
    @Autowired
    private OltDao oltDao;
    @Autowired
    private MessageService messageService;
    @Autowired
    private OltSniDao oltSniDao;
    @Autowired
    private OltPonDao oltPonDao;
    @Autowired
    private SniVlanService sniVlanService;
    @Autowired
    private OltMirrorDao oltMirrorDao;
    @Autowired
    private OltTrunkDao oltTrunkDao;
    @Autowired
    private OltTrunkService oltTrunkService;
    @Autowired
    private OltPonProtectDao oltPonProtectDao;
    @Autowired
    private DeviceVersionService deviceVersionService;
    @Autowired
    private RogueOnuService rogueOnuService;

    @SuppressWarnings("unchecked")
    @Override
    public void bfsxOltInfo(long entityId) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        String deviceVersion = deviceVersionService.getParamValue(entityId, "boardEntry", "deviceVersion");
        snmpParam.setDeviceVersion(deviceVersion);
        OltBfsxFacade facade = facadeFactory.getFacade(snmpParam.getIpAddress(), OltBfsxFacade.class);

        HashMap<Long, Long> oltMap = (HashMap<Long, Long>) oltDao.getOltMap(entityId);
        Map<String, Object> data = facade.bfsxOltInfo(snmpParam);

        OltAttribute oltAttr = (OltAttribute) data.get("oltAttribute");
        if (data.get("oltAttribute") != null) {
            oltAttr.setEntityId(entityId);
            oltDao.insertOrUpdateOltAttribute(oltAttr);
        }
        List<OltSlotAttribute> osas = oltSlotDao.getOltSlotList(entityId);
        Map<Long, OltSlotAttribute> tmp = make(osas);
        List<OltSlotAttribute> slots = (List<OltSlotAttribute>) data.get("slots");
        for (OltSlotAttribute oltSlotAttribute : slots) {
            OltSlotAttribute oltSlot = tmp.get(oltSlotAttribute.getSlotIndex());
            if (oltSlot != null && oltSlotAttribute.getTopSysBdPreConfigType() != null
                    && oltSlot.getTopSysBdPreConfigType() != null
                    && !oltSlot.getTopSysBdPreConfigType().equals(oltSlotAttribute.getTopSysBdPreConfigType())) {
                // oltSlotDao.deleteOltSlot(oltSlot.getSlotId());
                // oltMap.remove(oltSlot.getSlotIndex());
            }
        }
        oltSlotService.batchInsertOrUpdateSlotAttribute(slots, oltMap);
        List<OltSlotStatus> $slotstatus = (List<OltSlotStatus>) data.get("slotStatus");
        oltSlotDao.batchInsertOltSlotStatus($slotstatus);
        for (OltSlotStatus status : $slotstatus) {
            if (status.getAttribute() == 1) {
                EntityValueEvent event = new EntityValueEvent(entityId);
                event.setEntityId(entityId);
                event.setState(true);
                event.setCpu(status.getTopSysBdCpuUseRatio().doubleValue() / 100);
                event.setMem((status.getTopSysBdlMemSize() - status.getTopSysBdFreeMemSize())
                        / status.getTopSysBdlMemSize().doubleValue());
                event.setDisk((status.getTopTotalFlashOctets() - status.getTopSysBdFreeFlashOctets())
                        / status.getTopTotalFlashOctets().doubleValue());
                // event.setSysUpTime(oltAttr.getOltDeviceUpTime().toString());
                event.setActionName("performanceChanged");
                event.setListener(EntityValueListener.class);
                messageService.addMessage(event);
                break;
            }
        }

        oltSlotDao.batchInsertOltPowerAttribute(entityId, (List<OltPowerAttribute>) data.get("powers"));
        oltSlotDao.batchInsertOltPowerStatus((List<OltPowerStatus>) data.get("powerStatus"));
        oltSlotDao.batchInsertOltFanAttribute(entityId, (List<OltFanAttribute>) data.get("fans"));
        oltSlotDao.batchInsertOltFanStatus((List<OltFanStatus>) data.get("fanStatus"));
        oltSniDao.batchInsertSniAttribute(entityId, (List<OltSniAttribute>) data.get("snis"), oltMap);
        oltPonDao.batchInsertPonAttribute((List<OltPonAttribute>) data.get("pons"), oltMap);

        // 刷新长发光排查开关
        rogueOnuService.refreshPonPortRogueEntry(entityId);
    }

    private Map<Long, OltSlotAttribute> make(List<OltSlotAttribute> osas) {
        Map<Long, OltSlotAttribute> re = new HashMap<Long, OltSlotAttribute>();
        for (OltSlotAttribute oltSlotAttribute : osas) {
            re.put(oltSlotAttribute.getSlotIndex(), oltSlotAttribute);
        }
        return re;
    }

    @Override
    public void bfsxOltVlan(Long entityId) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);

        sniVlanService.refreshSniVlanAttribute(snmpParam);
        sniVlanService.refreshSniVlanView(snmpParam);
        sniVlanService.refreshSniPortVlan(snmpParam);
        sniVlanService.refreshVlanVif(entityId);
        sniVlanService.refreshTopMcFloodMode(snmpParam);
    }

    @Override
    public void bfsxOltMirror(Long entityId) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        OltBfsxFacade facade = facadeFactory.getFacade(snmpParam.getIpAddress(), OltBfsxFacade.class);
        List<OltSniMirrorConfig> list = facade.bfsxOltMirror(snmpParam);
        for (OltSniMirrorConfig entry : list) {
            entry.setEntityId(entityId);
        }
        oltMirrorDao.batchInsertOltSniMirrorConfig(list, entityId);
    }

    @Override
    public void bfsxOltTrunk(Long entityId) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        OltBfsxFacade facade = facadeFactory.getFacade(snmpParam.getIpAddress(), OltBfsxFacade.class);
        List<OltSniTrunkConfig> list = facade.bfsxOltTrunk(snmpParam);
        for (OltSniTrunkConfig entry : list) {
            entry.setEntityId(entityId);
        }
        oltTrunkDao.batchInsertOltSniTrunkConfig(list, entityId);
        oltTrunkService.updateMemoryDataAfterDiscovery(entityId);
    }

    @Override
    public void bfsxOltPonProtectGroup(Long entityId) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        OltBfsxFacade facade = facadeFactory.getFacade(snmpParam.getIpAddress(), OltBfsxFacade.class);
        List<OltPonProtectConfig> list = facade.bfsxOltPonProtectGroup(snmpParam);
        for (OltPonProtectConfig entry : list) {
            entry.setEntityId(entityId);
        }
        oltPonProtectDao.batchInsertOltPonProtectConfigs(list, entityId);
    }
}
