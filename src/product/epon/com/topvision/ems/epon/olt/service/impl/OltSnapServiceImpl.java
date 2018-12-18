package com.topvision.ems.epon.olt.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.devicesupport.version.service.DeviceVersionService;
import com.topvision.ems.epon.olt.dao.OltDao;
import com.topvision.ems.epon.olt.domain.OltSlotStatus;
import com.topvision.ems.epon.performance.facade.OltPerfFacade;
import com.topvision.ems.epon.realtime.domain.OltBaseInfo;
import com.topvision.ems.epon.realtime.facade.OltRealtimeFacade;
import com.topvision.ems.facade.domain.DeviceBaseInfo;
import com.topvision.ems.network.domain.EntitySnap;
import com.topvision.ems.network.service.BfsxEntitySnapService;
import com.topvision.ems.network.service.EntityService;
import com.topvision.ems.template.service.EntityTypeService;
import com.topvision.framework.service.BaseService;
import com.topvision.framework.snmp.SnmpParam;
import com.topvision.platform.facade.FacadeFactory;
import com.topvision.platform.message.event.EntityValueEvent;
import com.topvision.platform.message.event.EntityValueListener;
import com.topvision.platform.message.service.MessageService;

@Service("oltSnapService")
public class OltSnapServiceImpl extends BaseService implements BfsxEntitySnapService {
    @Autowired
    private OltDao oltDao;
    @Autowired
    private EntityService entityService;
    @Autowired
    private MessageService messageService;
    @Autowired
    private FacadeFactory facadeFactory;
    @Autowired
    private EntityTypeService entityTypeService;
    @Autowired
    private DeviceVersionService deviceVersionService;

    @Override
    public void refreshSnapInfo(Long entityId, Long typeId) {
        SnmpParam param = entityService.getSnmpParamByEntity(entityId);
        OltRealtimeFacade realtimeFacade = facadeFactory.getFacade(param.getIpAddress(), OltRealtimeFacade.class);

        String deviceVersion = deviceVersionService.getParamValue(entityId, "topsysoltuptime", "deviceVersion");
        param.setDeviceVersion(deviceVersion);
        if (entityTypeService.isStandardOlt(typeId)) {
            DeviceBaseInfo deviceInfo = null;
            try {
                deviceInfo = realtimeFacade.getDeviceBaseInfo(param);
                // 更新OLT的基本信息
                entityService.updateSnapSysUptime(entityId, Long.parseLong(deviceInfo.getSysUpTime()));
                entityService.updateEntityDeviceName(entityId, deviceInfo.getSysName());
            } catch (Exception e) {
                // 此处很有可能出现设备已经不能连接 仍然执行monitor的问题
                logger.error("", e);
                return;
            }
        } else {
            // 获取OLT的基本信息
            OltBaseInfo baseInfo = null;
            try {
                baseInfo = realtimeFacade.getOltBaseInfo(param);
                baseInfo.setEntityId(entityId);
                // 更新OLT的基本信息
                entityService.updateSnapSysUptime(entityId, baseInfo.getSysUpTime());
                oltDao.updateOltBaseInfo(baseInfo);
                oltDao.updateOltSoftVersion(baseInfo);
                entityService.updateEntityDeviceName(entityId, baseInfo.getDeviceName());
            } catch (Exception e) {
                // 此处很有可能出现设备已经不能连接 仍然执行monitor的问题
                logger.error("", e);
                return;
            }
            // 获取OLT的CPU、内存利用率信息
            OltPerfFacade prefFacade = facadeFactory.getFacade(param.getIpAddress(), OltPerfFacade.class);
            List<OltSlotStatus> status = prefFacade.getOltSlotStatus(param);
            for (OltSlotStatus s : status) {
                if (s.getAttribute() == 1) {
                    EntitySnap snap = new EntitySnap();
                    snap.setCpu(s.getTopSysBdCpuUseRatio().doubleValue() / 100);
                    snap.setUsedMem(s.getTopSysBdlMemSize().doubleValue() - s.getTopSysBdFreeMemSize().doubleValue());
                    if (s.getTopSysBdlMemSize().doubleValue() != 0) {
                        snap.setMem((s.getTopSysBdlMemSize().doubleValue() - s.getTopSysBdFreeMemSize().doubleValue())
                                / s.getTopSysBdlMemSize().doubleValue());
                    } else {
                        snap.setMem(0D);
                    }
                    snap.setVmem(s.getTopSysBdFreeFlashOctets().doubleValue());
                    snap.setUsedDisk(s.getTopTotalFlashOctets().doubleValue()
                            - s.getTopSysBdFreeFlashOctets().doubleValue());
                    if (s.getTopTotalFlashOctets().doubleValue() != 0) {
                        snap.setDisk((s.getTopTotalFlashOctets().doubleValue() - s.getTopSysBdFreeFlashOctets()
                                .doubleValue()) / s.getTopTotalFlashOctets().doubleValue());
                    } else {
                        snap.setDisk(0D);
                    }
                    snap.setDiskio(s.getTopTotalFlashOctets().doubleValue());
                    snap.setSnapTimeMillis(System.currentTimeMillis());
                    // monitorService.updateSnapPerf(param.getIpAddress(), snap);
                    EntityValueEvent event = new EntityValueEvent(entityId);
                    event.setEntityId(entityId);
                    event.setCpu(snap.getCpu());
                    event.setMem(snap.getMem());
                    event.setVmem(snap.getVmem());
                    event.setDisk(snap.getDisk());
                    if (baseInfo != null && baseInfo.getSysUpTime() != null) {
                        event.setSysUpTime(baseInfo.getSysUpTime().toString());
                    }
                    event.setActionName("performanceChanged");
                    event.setListener(EntityValueListener.class);
                    messageService.addMessage(event);
                    break;
                }
            }
        }

    }

}
