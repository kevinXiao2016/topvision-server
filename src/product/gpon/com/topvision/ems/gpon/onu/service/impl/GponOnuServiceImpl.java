/***********************************************************************
 * $Id: GponOnuServiceImpl.java,v1.0 2016年10月17日 下午3:42:19 $
 * 
 * @author: Rod John
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.gpon.onu.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.epon.onu.dao.OnuDao;
import com.topvision.ems.epon.onu.dao.UniDao;
import com.topvision.ems.epon.onu.domain.OltOnuAttribute;
import com.topvision.ems.epon.topology.event.OnuSynchronizedEvent;
import com.topvision.ems.epon.topology.event.OnuSynchronizedListener;
import com.topvision.ems.gpon.onu.dao.GponOnuDao;
import com.topvision.ems.gpon.onu.domain.GponOnuCapability;
import com.topvision.ems.gpon.onu.domain.GponOnuInfoSoftware;
import com.topvision.ems.gpon.onu.domain.GponOnuIpHost;
import com.topvision.ems.gpon.onu.domain.GponOnuUniPvid;
import com.topvision.ems.gpon.onu.domain.GponUniAttribute;
import com.topvision.ems.gpon.onu.facade.GponOnuFacade;
import com.topvision.ems.gpon.onu.facade.GponUniFacade;
import com.topvision.ems.gpon.onu.service.GponOnuService;
import com.topvision.ems.gpon.onuauth.GponConstant;
import com.topvision.ems.network.service.EntityService;
import com.topvision.framework.service.BaseService;
import com.topvision.framework.snmp.SnmpParam;
import com.topvision.platform.facade.FacadeFactory;
import com.topvision.platform.message.service.MessageService;

/**
 * @author Rod John
 * @created @2016年10月17日-下午3:42:19
 *
 */
@Service
public class GponOnuServiceImpl extends BaseService implements GponOnuService, OnuSynchronizedListener {
    @Autowired
    private GponOnuDao gponOnuDao;
    @Autowired
    private UniDao uniDao;
    @Autowired
    private OnuDao onuDao;
    @Autowired
    private EntityService entityService;
    @Autowired
    private FacadeFactory facadeFactory;
    @Autowired
    protected MessageService messageService;
    public static final Integer ONU_SINGLE_TOPO = 1;

    @PostConstruct
    @Override
    public void initialize() {
        super.initialize();
        messageService.addListener(OnuSynchronizedListener.class, this);
    }

    @PreDestroy
    @Override
    public void destroy() {
        super.destroy();
        messageService.removeListener(OnuSynchronizedListener.class, this);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.epon.topology.event.OnuSynchronizedListener#insertEntityStates(com.
     * topvision.ems.epon.topology.event.OnuSynchronizedEvent)
     */
    @Override
    public void insertEntityStates(OnuSynchronizedEvent event) {
        Long entityId = event.getEntityId();
        List<Long> onuIndexList = event.getOnuIndexList();
        if (onuIndexList.size() == ONU_SINGLE_TOPO) {
            OltOnuAttribute onuAttr = onuDao.getOnuAttributeByIndex(entityId, onuIndexList.get(0));
            if (GponConstant.GPON_ONU.equals(onuAttr.getOnuEorG())) {
                try {
                    // 当刷新单个onu，用gettableline
                    // TODO OLT没有实现部分getNext 只能全设备刷新
                    refreshGponOnuHostIp(entityId);
                    logger.info("refreshGponOnuHostIp finish");
                } catch (Exception e) {
                    logger.error("refreshGponOnuHostIp error:", e);
                }
                //移到同步刷新
//                try {
//                    refreshGponOnuSoftware(entityId, onuIndexList.get(0));
//                    logger.info("refreshGponOnuSoftware finish");
//                } catch (Exception e) {
//                    logger.error("refreshGponOnuSoftware error:", e);
//                }
                try {
                    refreshGponOnuUniPvid(entityId, onuIndexList.get(0));
                    logger.info("refreshGponOnuUniPvid finish");
                } catch (Exception e) {
                    logger.error("refreshGponOnuUniPvid error:", e);
                }
            }

        } else if (onuIndexList.size() > ONU_SINGLE_TOPO) {
            try {
                refreshGponOnuHostIp(entityId);
                logger.info("refreshGponOnuHostIp finish");
            } catch (Exception e) {
                logger.error("refreshGponOnuHostIp error:", e);
            }

            try {
                refreshGponOnuSoftware(entityId);
                logger.info("refreshGponOnuSoftware finish");
            } catch (Exception e) {
                logger.error("refreshGponOnuSoftware error:", e);
            }

            try {
                refreshGponOnuUniPvid(entityId);
                logger.info("refreshGponOnuUniPvid finish");
            } catch (Exception e) {
                logger.error("refreshGponOnuUniPvid error:", e);
            }
        }

    }

    @SuppressWarnings("unused")
    private Map<Long, Long> makeOnuIndexToOnuIdMap(List<OltOnuAttribute> onuList) {
        Map<Long, Long> re = new HashMap<>();
        if (onuList == null) {
            return re;
        }
        for (OltOnuAttribute oltOnuAttribute : onuList) {
            if (oltOnuAttribute != null) {
                re.put(oltOnuAttribute.getOnuIndex(), oltOnuAttribute.getOnuId());
            }
        }
        return re;
    }

    private void refreshGponOnuHostIp(Long entityId) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        List<GponOnuIpHost> gponOnuIpHosts = getGponOnuFacade(snmpParam.getIpAddress()).getGponOnuIpHosts(snmpParam);
        gponOnuDao.syncGponOnuIpHosts(entityId, gponOnuIpHosts);
    }

    @SuppressWarnings("unused")
    private void refreshGponOnuHostIp(Long entityId, Long onuIndex) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        GponOnuIpHost gponOnuIpHost = new GponOnuIpHost();
        gponOnuIpHost.setOnuIndex(onuIndex);
        gponOnuIpHost.setEntityId(entityId);
        List<GponOnuIpHost> gponOnuIpHosts = getGponOnuFacade(snmpParam.getIpAddress()).getGponOnuIpHosts(snmpParam,
                gponOnuIpHost);
        gponOnuDao.syncGponOnuIpHosts(entityId, gponOnuIpHosts);
    }

    private void refreshGponOnuSoftware(Long entityId) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        List<GponOnuInfoSoftware> gponOnuInfoSoftwares = getGponOnuFacade(snmpParam.getIpAddress())
                .getGponOnuSoftwares(snmpParam);
        gponOnuDao.syncGponSoftware(entityId, gponOnuInfoSoftwares);
    }

    @Override
    public void refreshGponOnuSoftware(Long entityId, Long onuIndex) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        List<GponOnuInfoSoftware> gponOnuInfoSoftwares = new ArrayList<GponOnuInfoSoftware>();
        GponOnuInfoSoftware software = new GponOnuInfoSoftware();
        software.setOnuIndex(onuIndex);
        software = getGponOnuFacade(snmpParam.getIpAddress()).getGponOnuSoftware(snmpParam, software);
        gponOnuInfoSoftwares.add(software);
        gponOnuDao.syncGponSoftware(entityId, gponOnuInfoSoftwares);
    }

    private void refreshGponOnuUniPvid(Long entityId) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        List<GponOnuUniPvid> gponOnuUniPvidList = getGponOnuFacade(snmpParam.getIpAddress())
                .getGponOnuUniPvidList(snmpParam);
        gponOnuDao.syncGponOnuUniPvid(entityId, gponOnuUniPvidList);
    }

    private void refreshGponOnuUniPvid(Long entityId, Long onuIndex) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        List<GponOnuUniPvid> gponOnuUniPvidList = new ArrayList<GponOnuUniPvid>();
        List<Long> uniIndexList = uniDao.getUniIndexListByEntityIdAndOnuIndex(entityId, onuIndex);
        for (Long uniIndex : uniIndexList) {
            try {
                GponOnuUniPvid gponOnuUniVlan = new GponOnuUniPvid();
                gponOnuUniVlan.setUniIndex(uniIndex);
                gponOnuUniVlan = getGponOnuFacade(snmpParam.getIpAddress()).getGponOnuUniPvid(snmpParam,
                        gponOnuUniVlan);
                gponOnuUniPvidList.add(gponOnuUniVlan);
            } catch (Exception e) {
                logger.info(e.getMessage());
            }
        }
        gponOnuDao.syncGponOnuUniPvid(entityId, gponOnuUniPvidList);
    }

    private GponOnuFacade getGponOnuFacade(String ip) {
        return facadeFactory.getFacade(ip, GponOnuFacade.class);
    }

    private GponUniFacade getGponUniFacade(String ip) {
        return facadeFactory.getFacade(ip, GponUniFacade.class);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.gpon.onu.service.GponOnuService#queryForGponOnuCapability(long)
     */
    @Override
    public GponOnuCapability queryForGponOnuCapability(long onuId) {
        return gponOnuDao.queryForGponOnuCapability(onuId);
    }

    @Override
    public GponOnuInfoSoftware queryForGponOnuSoftware(Long onuId) {
        return gponOnuDao.queryForGponOnuSoftware(onuId);
    }

    @Override
    public List<GponOnuIpHost> loadGponOnuIpHost(HashMap<String, Object> map) {
        return gponOnuDao.loadGponOnuIpHost(map);
    }

    @Override
    public void addGponOnuIpHost(GponOnuIpHost gponOnuIpHost) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(gponOnuIpHost.getEntityId());
        getGponOnuFacade(snmpParam.getIpAddress()).addGponOnuIpHost(snmpParam, gponOnuIpHost);
        gponOnuDao.addGponOnuIpHost(gponOnuIpHost);
    }

    @Override
    public GponOnuIpHost getGponOnuIpHost(Long onuId, Integer onuIpHostIndex) {
        return gponOnuDao.getGponOnuIpHost(onuId, onuIpHostIndex);
    }

    @Override
    public void modifyGponOnuIpHost(GponOnuIpHost gponOnuIpHost) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(gponOnuIpHost.getEntityId());
        getGponOnuFacade(snmpParam.getIpAddress()).modifyGponOnuIpHost(snmpParam, gponOnuIpHost);
        gponOnuDao.modifyGponOnuIpHost(gponOnuIpHost);
    }

    @Override
    public void delGponOnuIpHost(GponOnuIpHost gponOnuIpHost) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(gponOnuIpHost.getEntityId());
        getGponOnuFacade(snmpParam.getIpAddress()).deleteGponOnuIpHost(snmpParam, gponOnuIpHost);
        gponOnuDao.delGponOnuIpHost(gponOnuIpHost.getOnuId(), gponOnuIpHost.getOnuIpHostIndex());
    }

    @Override
    public void refreshOnuIpHost(Long entityId) {
        refreshGponOnuHostIp(entityId);
    }

    @Override
    public List<Integer> usedHostIpIndex(Long onuId) {
        return gponOnuDao.usedHostIpIndex(onuId);
    }

    @Override
    public List<GponUniAttribute> loadGponOnuUniList(Long onuId) {
        return gponOnuDao.loadGponOnuUniList(onuId);
    }

    @Override
    public GponUniAttribute loadUniVlanConfig(Long uniId) {
        return gponOnuDao.loadUniVlanConfig(uniId);
    }

    @Override
    public void setUniVlanConfig(Long entityId, Long uniId, Integer gponOnuUniPri, Integer gponOnuUniPvid) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        Long uniIndex = uniDao.getUniIndex(uniId);
        getGponUniFacade(snmpParam.getIpAddress()).setUniVlanConfig(snmpParam, uniIndex, gponOnuUniPri, gponOnuUniPvid);
        gponOnuDao.setUniVlanConfig(uniId, gponOnuUniPri, gponOnuUniPvid);
    }

}
