/***********************************************************************
 * $Id: GponUniFilterMacServiceImpl.java,v1.0 2016年12月24日 上午9:05:14 $
 * 
 * @author: jay
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.gpon.unifiltermac.service.impl;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.epon.onu.dao.OnuDao;
import com.topvision.ems.epon.onu.dao.UniDao;
import com.topvision.ems.epon.onu.domain.OltOnuAttribute;
import com.topvision.ems.epon.onu.domain.OltUniAttribute;
import com.topvision.ems.epon.topology.event.OnuSynchronizedEvent;
import com.topvision.ems.epon.topology.event.OnuSynchronizedListener;
import com.topvision.ems.gpon.onuauth.GponConstant;
import com.topvision.ems.gpon.unifiltermac.dao.GponUniFilterMacDao;
import com.topvision.ems.gpon.unifiltermac.facade.GponUniFilterMacFacade;
import com.topvision.ems.gpon.unifiltermac.facade.domain.GponUniFilterMac;
import com.topvision.ems.gpon.unifiltermac.service.GponUniFilterMacService;
import com.topvision.ems.network.service.EntityService;
import com.topvision.framework.service.BaseService;
import com.topvision.framework.snmp.RowStatus;
import com.topvision.framework.snmp.SnmpParam;
import com.topvision.framework.utils.EponIndex;
import com.topvision.platform.facade.FacadeFactory;
import com.topvision.platform.message.service.MessageService;

/**
 * @author jay
 * @created @2016年12月24日-上午9:05:14
 *
 */
@Service("gponUniFilterMacService")
public class GponUniFilterMacServiceImpl extends BaseService implements GponUniFilterMacService,
        OnuSynchronizedListener {
    @Autowired
    private FacadeFactory facadeFactory;
    @Autowired
    private EntityService entityService;
    @Autowired
    private UniDao uniDao;
    @Autowired
    private OnuDao onuDao;
    @Autowired
    private GponUniFilterMacDao gponUniFilterMacDao;
    @Autowired
    protected MessageService messageService;

    public static final Integer ONU_SINGLE_TOPO = 1;

    @PreDestroy
    @Override
    public void destroy() {
        super.destroy();
        messageService.removeListener(OnuSynchronizedListener.class, this);
    }

    @PostConstruct
    @Override
    public void initialize() {
        super.initialize();
        messageService.addListener(OnuSynchronizedListener.class, this);
    }

    @Override
    public List<GponUniFilterMac> loadGponUniFilterMacList(Long uniId) {
        List<GponUniFilterMac> gponUniFilterMacs = gponUniFilterMacDao.loadGponUniFilterMacList(uniId);
        return gponUniFilterMacs;
    }

    @Override
    public Boolean addGponUniFilterMac(Long uniId, String mac) {
        GponUniFilterMac gufm = gponUniFilterMacDao.loadGponUniFilterMac(uniId, mac);
        if (gufm != null) {
            return false;
        }
        OltUniAttribute oltUniAttribute = uniDao.getOnuUniAttribute(uniId);
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(oltUniAttribute.getEntityId());
        GponUniFilterMac gponUniFilterMac = new GponUniFilterMac();
        gponUniFilterMac.setEntityId(oltUniAttribute.getEntityId());
        gponUniFilterMac.setUniId(uniId);
        gponUniFilterMac.setUniIndex(oltUniAttribute.getUniIndex());
        gponUniFilterMac.setMacAddressString(mac);
        gponUniFilterMac.setRowStatus(RowStatus.CREATE_AND_GO);
        getGponUniFilterMacFacade(snmpParam).addGponUniFilterMac(snmpParam, gponUniFilterMac);
        gponUniFilterMacDao.addGponUniFilterMac(gponUniFilterMac);
        return true;
    }

    @Override
    public void deleteGponUniFilterMac(Long uniId, String mac) {
        OltUniAttribute oltUniAttribute = uniDao.getOnuUniAttribute(uniId);
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(oltUniAttribute.getEntityId());
        GponUniFilterMac gponUniFilterMac = new GponUniFilterMac();
        gponUniFilterMac.setUniId(uniId);
        gponUniFilterMac.setUniIndex(oltUniAttribute.getUniIndex());
        gponUniFilterMac.setMacAddressString(mac);
        gponUniFilterMac.setRowStatus(RowStatus.DESTORY);
        getGponUniFilterMacFacade(snmpParam).deleteGponUniFilterMac(snmpParam, gponUniFilterMac);
        gponUniFilterMacDao.deleteGponUniFilterMac(gponUniFilterMac);
    }

    @Override
    public void refreshGponUniFilterMac(Long uniId) {
        OltUniAttribute oltUniAttribute = uniDao.getOnuUniAttribute(uniId);
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(oltUniAttribute.getEntityId());
        GponUniFilterMac gponUniFilterMac = new GponUniFilterMac();
        gponUniFilterMac.setUniIndex(oltUniAttribute.getUniIndex());
        //TODO 设备缺陷限制必须全设备刷新
        List<GponUniFilterMac> gponUniFilterMacs = getGponUniFilterMacFacade(snmpParam)
                .refreshGponUniFilterMacForEntity(snmpParam);
        for (GponUniFilterMac uniFilterMac : gponUniFilterMacs) {
            uniFilterMac.setEntityId(oltUniAttribute.getEntityId());
            Long uId = uniDao.getUniIdByIndex(oltUniAttribute.getEntityId(),
                    EponIndex.getUniIndex(uniFilterMac.getSlotNo(), uniFilterMac.getPonNo(), uniFilterMac.getOnuNo(),
                            uniFilterMac.getUniNo()));
            uniFilterMac.setUniId(uId);
        }
        //TODO 设备缺陷限制必须全设备刷新 换成uni口刷新需要调整facade和dao的方法调用
        gponUniFilterMacDao.insertGponUniFilterMacForEntity(oltUniAttribute.getEntityId(), gponUniFilterMacs);
    }

    @Override
    public void refreshDeviceUniFilterMac(Long entityId) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        List<GponUniFilterMac> gponUniFilterMacs = getGponUniFilterMacFacade(snmpParam)
                .refreshGponUniFilterMacForEntity(snmpParam);
        for (GponUniFilterMac uniFilterMac : gponUniFilterMacs) {
            uniFilterMac.setEntityId(entityId);
            Long uId = uniDao.getUniIdByIndex(entityId, EponIndex.getUniIndex(uniFilterMac.getSlotNo(),
                    uniFilterMac.getPonNo(), uniFilterMac.getOnuNo(), uniFilterMac.getUniNo()));
            uniFilterMac.setUniId(uId);
        }
        gponUniFilterMacDao.insertGponUniFilterMacForEntity(entityId, gponUniFilterMacs);

    }

    public GponUniFilterMacFacade getGponUniFilterMacFacade(SnmpParam snmpParam) {
        return facadeFactory.getFacade(snmpParam.getIpAddress(), GponUniFilterMacFacade.class);
    }

    @Override
    public void insertEntityStates(OnuSynchronizedEvent event) {
        Long entityId = event.getEntityId();
        List<Long> onuIndexlList = event.getOnuIndexList();
        if (onuIndexlList.size() == ONU_SINGLE_TOPO) {
            OltOnuAttribute onuAttr = onuDao.getOnuAttributeByIndex(entityId, onuIndexlList.get(0));
            if (GponConstant.GPON_ONU.equals(onuAttr.getOnuEorG())) {
                //TODO 设备实现问题 暂时无法刷新单个UNI端口数据
                refreshDeviceUniFilterMac(entityId);
            }
        } else if (onuIndexlList.size() > ONU_SINGLE_TOPO) {
            refreshDeviceUniFilterMac(entityId);
        }
    }
}
