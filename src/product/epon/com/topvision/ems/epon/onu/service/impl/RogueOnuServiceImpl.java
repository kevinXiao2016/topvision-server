/***********************************************************************
 * $Id: RogueOnuServiceImpl.java,v1.0 2017年6月17日 上午9:22:39 $
 * 
 * @author: lizongtian
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.onu.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.epon.olt.dao.OltPonDao;
import com.topvision.ems.epon.onu.dao.OnuDao;
import com.topvision.ems.epon.onu.dao.RogueOnuDao;
import com.topvision.ems.epon.onu.domain.OltOnuAttribute;
import com.topvision.ems.epon.onu.domain.TopOnuLaserEntry;
import com.topvision.ems.epon.onu.domain.TopPonPortRogueEntry;
import com.topvision.ems.epon.onu.domain.TopSystemRogueCheck;
import com.topvision.ems.epon.onu.facade.RogueOnuFacade;
import com.topvision.ems.epon.onu.service.RogueOnuService;
import com.topvision.ems.epon.topology.event.OnuSynchronizedEvent;
import com.topvision.ems.epon.topology.event.OnuSynchronizedListener;
import com.topvision.ems.network.dao.EntityDao;
import com.topvision.ems.network.domain.EntitySnap;
import com.topvision.ems.network.service.EntityService;
import com.topvision.framework.service.BaseService;
import com.topvision.framework.snmp.SnmpParam;
import com.topvision.framework.utils.EponConstants;
import com.topvision.framework.utils.EponIndex;
import com.topvision.platform.facade.FacadeFactory;
import com.topvision.platform.message.service.MessageService;

/**
 * @author lizongtian
 * @created @2017年6月17日-上午9:22:39
 *
 */
@Service("rogueOnuService")
public class RogueOnuServiceImpl extends BaseService implements RogueOnuService, OnuSynchronizedListener {

    public static final Integer ONU_SINGLE_TOPO = 1;
    @Autowired
    private MessageService messageService;
    @Autowired
    private FacadeFactory facadeFactory;
    @Autowired
    private EntityService entityService;
    @Autowired
    private OltPonDao oltPonDao;
    @Autowired
    private OnuDao onuDao;
    @Autowired
    private EntityDao entityDao;
    @Autowired
    private RogueOnuDao rogueOnuDao;

    @Override
    @PreDestroy
    public void destroy() {
        super.destroy();
        messageService.removeListener(OnuSynchronizedListener.class, this);
    }

    @Override
    @PostConstruct
    public void initialize() {
        super.initialize();
        messageService.addListener(OnuSynchronizedListener.class, this);
    }

    @Override
    public void insertEntityStates(OnuSynchronizedEvent event) {
        Long entityId = event.getEntityId();
        List<Long> onuIndexList = event.getOnuIndexList();
        try {
            if (onuIndexList.size() == ONU_SINGLE_TOPO) {
                Long onuId = onuDao.getOnuIdByIndex(entityId, onuIndexList.get(0));
                refreshOnuLaserSwitch(entityId, onuId);
            } else if (onuIndexList.size() > ONU_SINGLE_TOPO) {
                refreshOnuLaserSwitch(entityId);
            }
            logger.info("refreshOnuLaserSwitch finish");
        } catch (Exception e) {
            logger.error("refreshOnuLaserSwitch wrong ", e);
        }

    }

    @Override
    public void refreshSystemRogueCheck(Long entityId) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        Integer oltRogueSwitch = getRogueOnuFacade(snmpParam.getIpAddress()).getOltRogueSwitch(snmpParam);
        rogueOnuDao.updateOltRogueSwitch(entityId, oltRogueSwitch);
    }

    @Override
    public void modifySystemRogueCheck(Long entityId, Integer status) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        TopSystemRogueCheck systemRogueCheck = new TopSystemRogueCheck();
        systemRogueCheck.setSystemRogueCheck(status);
        getRogueOnuFacade(snmpParam.getIpAddress()).setOltRogueSwitch(snmpParam, systemRogueCheck);
        rogueOnuDao.updateOltRogueSwitch(entityId, status);
    }

    @Override
    public void refreshPonPortRogueEntry(Long entityId) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        List<TopPonPortRogueEntry> ponPortRogueList = getRogueOnuFacade(snmpParam.getIpAddress())
                .getPonPortRogueOnuList(snmpParam);
        for (TopPonPortRogueEntry topPonPortRogueEntry : ponPortRogueList) {
            List<Integer> onuNoList = topPonPortRogueEntry.getOnuNoList();
            if (onuNoList.size() > 0) {
                for (Integer onuNo : onuNoList) {
                    Long onuIndex = EponIndex.getOnuIndex(topPonPortRogueEntry.getRogueCardIndex(),
                            topPonPortRogueEntry.getRoguePortIndex(), onuNo + 1);
                    Long onuId = onuDao.getOnuIdByIndex(entityId, onuIndex);
                    rogueOnuDao.updateOnuRogueStatus(onuId, EponConstants.IS_ROGUE_ONU);
                }
            } else {
                Long ponId = oltPonDao.getPonIdByPonIndex(entityId, topPonPortRogueEntry.getPonIndex());
                List<OltOnuAttribute> onuList = onuDao.getOnuListByPonId(ponId);
                rogueOnuDao.batchUpdateOnuRogueStatus(onuList);
            }
        }
        rogueOnuDao.batchInsertOrUpdatePonRogueInfo(entityId, ponPortRogueList);
    }

    @Override
    public void refreshOnuLaserSwitch(Long entityId) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        List<TopOnuLaserEntry> onuLaserList = getRogueOnuFacade(snmpParam.getIpAddress()).getOnuLaserEntry(snmpParam);
        rogueOnuDao.batchInsertOrUpdateOnuLaser(entityId, onuLaserList);
    }

    @Override
    public void refreshOnuLaserSwitch(Long entityId, Long onuId) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        Long onuIndex = onuDao.getOnuIndex(onuId);
        TopOnuLaserEntry topOnuLaserEntry = new TopOnuLaserEntry();
        topOnuLaserEntry.setOnuIndex(onuIndex);
        TopOnuLaserEntry onuLaserSwitch = getRogueOnuFacade(snmpParam.getIpAddress()).getOnuLaserSwitch(snmpParam, topOnuLaserEntry);
        onuLaserSwitch.setOnuId(onuId);
        rogueOnuDao.insertOrupdateOnuLaserSwitch(onuLaserSwitch);
    }

    @Override
    public void modifyOnuLaser(Long entityId, Long onuId, Integer onuLaser) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        Long onuIndex = onuDao.getOnuIndex(onuId);
        TopOnuLaserEntry topOnuLaserEntry = new TopOnuLaserEntry();
        topOnuLaserEntry.setOnuIndex(onuIndex);
        topOnuLaserEntry.setLaserSwitch(onuLaser);
        getRogueOnuFacade(snmpParam.getIpAddress()).setOnuLaserSwitch(snmpParam, topOnuLaserEntry);
        rogueOnuDao.updateOnuLaserSwitch(onuId, onuLaser);
        //关闭激光器需要将ONU设置为下线、同时将ONU设置为流氓ONU,开启激光器同时需要将ONU从流氓ONU中剔除
        if (onuLaser != 1) {
            onuDao.updateOnuOperationStatus(onuId, EponConstants.ONU_STATUS_DOWN);
            rogueOnuDao.updateOnuRogueStatus(onuId, EponConstants.IS_ROGUE_ONU);
            // 更新entitysnap表中onu的在线状态
            EntitySnap onuSnap = new EntitySnap();
            onuSnap.setEntityId(onuId);
            onuSnap.setState(false);
            entityDao.updateOnuEntitySnap(onuSnap);
        } else {
            rogueOnuDao.changeOnuRogueStatus(entityId, onuIndex);
        }
    }

    @Override
    public void modifyPonPortRogueSwitch(Long entityId, Long ponId, Integer status) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        Long ponIndex = oltPonDao.getPonIndex(ponId);
        TopPonPortRogueEntry ponPortRogueEntry = new TopPonPortRogueEntry();
        ponPortRogueEntry.setPonIndex(ponIndex);
        ponPortRogueEntry.setRogueSwitch(status);
        getRogueOnuFacade(snmpParam.getIpAddress()).setPonPortRogueSwitch(snmpParam, ponPortRogueEntry);
        rogueOnuDao.updatePortRogueSwitch(ponId, status);
    }

    @Override
    public void ponPortRogueOnuCheck(Long entityId, Long ponId, List<Integer> onuNos) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        Long ponIndex = oltPonDao.getPonIndex(ponId);
        TopPonPortRogueEntry ponPortRogueEntry = new TopPonPortRogueEntry();
        ponPortRogueEntry.setPonIndex(ponIndex);
        ponPortRogueEntry.setCheckOnuNoList(onuNos);
        getRogueOnuFacade(snmpParam.getIpAddress()).setPonPortRogueOnuCheck(snmpParam, ponPortRogueEntry);
        updateRogueOnuList(entityId, ponPortRogueEntry.getRogueCardIndex(), ponPortRogueEntry.getRoguePortIndex(),
                onuNos);
    }

    private void updateRogueOnuList(Long entityId, Integer slotNo, Integer ponNo, List<Integer> onuNos) {
        for (Integer onuNo : onuNos) {
            Long onuIndex = EponIndex.getOnuIndex(slotNo, ponNo, onuNo);
            rogueOnuDao.changeOnuRogueStatus(entityId, onuIndex);
        }
    }

    private RogueOnuFacade getRogueOnuFacade(String ip) {
        return facadeFactory.getFacade(ip, RogueOnuFacade.class);
    }

    @Override
    public List<OltOnuAttribute> loadRogueOnuList(Map<String, Object> queryMap) {
        return rogueOnuDao.queryRogueOnuList(queryMap);
    }

    @Override
    public int queryRogueOnuCount(Map<String, Object> queryMap) {
        return rogueOnuDao.queryRogueOnuCount(queryMap);
    }

}
