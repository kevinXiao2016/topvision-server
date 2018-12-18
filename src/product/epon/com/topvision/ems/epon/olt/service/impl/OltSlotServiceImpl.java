/***********************************************************************
 * $Id: OltServiceImpl.java,v1.0 2013-10-25 上午10:28:47 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.olt.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.devicesupport.version.VersionControl;
import com.topvision.ems.epon.domain.EponBoardStatistics;
import com.topvision.ems.epon.exception.BoardPreConfigChangedException;
import com.topvision.ems.epon.exception.SetValueConflictException;
import com.topvision.ems.epon.olt.dao.OltPonDao;
import com.topvision.ems.epon.olt.dao.OltSlotDao;
import com.topvision.ems.epon.olt.domain.OltFanAttribute;
import com.topvision.ems.epon.olt.domain.OltFanStatus;
import com.topvision.ems.epon.olt.domain.OltPonAttribute;
import com.topvision.ems.epon.olt.domain.OltSlotAttribute;
import com.topvision.ems.epon.olt.domain.OltSlotStatus;
import com.topvision.ems.epon.olt.domain.OltSniAttribute;
import com.topvision.ems.epon.olt.facade.OltFacade;
import com.topvision.ems.epon.olt.facade.OltSlotFacade;
import com.topvision.ems.epon.olt.service.OltBfsxService;
import com.topvision.ems.epon.olt.service.OltSlotService;
import com.topvision.ems.epon.onu.domain.OltUniAttribute;
import com.topvision.ems.message.Message;
import com.topvision.ems.message.MessagePusher;
import com.topvision.ems.network.dao.EntityDao;
import com.topvision.ems.network.service.EntityService;
import com.topvision.framework.service.BaseService;
import com.topvision.framework.snmp.SnmpParam;
import com.topvision.framework.utils.EponConstants;
import com.topvision.platform.facade.FacadeFactory;
import com.topvision.platform.message.service.MessageService;

/**
 * @author flack
 * @created @2013-10-25-上午10:28:47
 * 
 */
@Service("oltSlotService")
public class OltSlotServiceImpl extends BaseService implements OltSlotService {
    @Autowired
    private OltSlotDao oltSlotDao;
    @Autowired
    private OltPonDao oltPonDao;
    @Autowired
    private EntityService entityService;
    @Autowired
    private FacadeFactory facadeFactory;
    @Autowired
    private EntityDao entityDao;
    @Autowired
    private MessageService messageService;
    @Autowired
    private MessagePusher messagePusher;
    @Autowired
    private OltBfsxService oltBfsxService;

    @Override
    public void setOltBdPreConfigType(Long entityId, Long slotId, Integer boardPreConfigType) {
        Long slotIndex = oltSlotDao.getSlotIndex(slotId);
        // 设置设备上板卡预配置类型
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        /*
         * //add by bravin@20140603:EMS-8742 插入了XGUB板的情况下再做预配置会导致预配置时间大于10秒，最终反应就是SNMP超时，故这里将超时时间设大点
         * snmpParam.setTimeout(20000);
         */
        // 预配置类型不为non-board(0),检查设备上预配置类型是否以及被修改。
        if (!boardPreConfigType.equals(EponConstants.BOARD_PRECONFIG_NOBOARD)) {
            // 从设备上获取设备当前预配置类型
            Integer curPreConfigType = getOltSlotFacade(snmpParam.getIpAddress()).getSlotPreConfigType(snmpParam,
                    slotIndex);
            // 设备获取预配置类型不为non-board(0),提示用户先更新板卡数据。
            if (!curPreConfigType.equals(EponConstants.BOARD_PRECONFIG_NOBOARD)) {
                throw new BoardPreConfigChangedException("Business.needToRefreshPreConfig");
            }
        }

        Integer newType = getOltSlotFacade(snmpParam.getIpAddress()).setSlotPreConfigType(snmpParam, slotIndex,
                boardPreConfigType);
        if (newType == null) {
            throw new SetValueConflictException("Business.connection");
        } else {
            if (!newType.equals(boardPreConfigType)) {
                throw new SetValueConflictException("Business.setPreConfigType");
            } else {
                deleteOltSlot(entityId, slotId, boardPreConfigType);
            }
        }

        oltBfsxService.bfsxOltInfo(entityId);
    }

    @Override
    public void refreshBoardInfo(Long entityId, Long slotId) {
        /*
         * Long slotIndex = oltSlotDao.getSlotIndex(slotId); Entity entity =
         * entityDao.selectByPrimaryKey(entityId); SnmpParam snmpParam =
         * entityService.getSnmpParamByEntity(entityId); // 重新发现该槽位的信息 OltDiscoveryData slotData =
         * getOltDiscoveryFacade(snmpParam.getIpAddress()).discoverySlot(snmpParam, slotIndex); //
         * 删除数据库中该板卡信息 oltSlotDao.deleteOltSlot(slotId); //TODO 此处更加板卡ID删除ONU信息 调用的清除设备的cache需要确认 //
         * 将获取到的槽位相关信息更新至数据库 HashMap<Long, Long> oltMap = (HashMap<Long, Long>)
         * oltDao.getOltMap(entityId); if (slotData.getSlots() != null) {
         * oltSlotDao.batchInsertSlotAttribute(slotData.getSlots(), oltMap); if (slotData.getSnis()
         * != null) { oltSniDao.batchInsertSniAttribute(slotData.getSnis(), oltMap); } if
         * (slotData.getPons() != null) { oltPonDao.batchInsertPonAttribute(slotData.getPons(),
         * oltMap); if (slotData.getOnus() != null) {
         * onuDao.batchInsertOnuAttribute(slotData.getOnus(), oltMap, entity); if
         * (slotData.getOnuPons() != null) {
         * onuDao.batchInsertOnuPonAttribute(slotData.getOnuPons(), oltMap); } if
         * (slotData.getUnis() != null) { uniDao.batchInsertUniAttribute(slotData.getUnis(),
         * oltMap); } } } }
         */
    }

    @Override
    public void resetOltBoard(Long entityId, Long boardId) {
        Long slotNo = oltSlotDao.getSlotNoById(entityId, boardId);
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        getOltSlotFacade(snmpParam.getIpAddress()).resetOltBoard(snmpParam, slotNo);
    }

    @Override
    public void setOltFanSpeedControl(Long entityId, Long fanCardId, Integer oltFanSpeedControlLevel) {
        Long fanCardIndex = oltSlotDao.getFanCardIndex(fanCardId);
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        Integer newFanSpeedControlLevel = getOltSlotFacade(snmpParam.getIpAddress()).setOltFanSpeedControl(snmpParam,
                fanCardIndex, oltFanSpeedControlLevel);
        if (newFanSpeedControlLevel == null) {
            throw new SetValueConflictException("Business.connection");
        } else {
            OltFanStatus fanStatus = new OltFanStatus();
            fanStatus.setFanCardId(fanCardId);
            fanStatus.setFanCardIndex(fanCardIndex);
            fanStatus = getOltFacade(snmpParam.getIpAddress()).getDomainInfoLine(snmpParam, fanStatus);
            oltSlotDao.updateFanSpeedControlLevel(fanCardId, newFanSpeedControlLevel);
            oltSlotDao.updateFanSpeedControl(fanStatus);
            if (!newFanSpeedControlLevel.equals(oltFanSpeedControlLevel)) {
                throw new SetValueConflictException("Business.setOltFanSpeedControl");
            }
        }
    }

    @Override
    public void updateSlotBdTempDetectEnable(Long entityId, Long slotId, Integer slotBdTempDetectEnable) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        // Long slotIndex = oltDao.getSlotIndex(slotId);
        OltSlotAttribute oltSlotAttribute = oltSlotDao.getSlotAttribute(slotId);
        Integer tempDetectEnable = getOltSlotFacade(snmpParam.getIpAddress()).setSlotBdTempDetectEnable(snmpParam,
                oltSlotAttribute.getSlotNo(), slotBdTempDetectEnable);
        if (tempDetectEnable != null && tempDetectEnable.equals(slotBdTempDetectEnable)) {
            oltSlotDao.updateSlotBdTempDetectEnable(slotId, slotBdTempDetectEnable);
        } else {
            throw new SetValueConflictException("Business.updateSlotBdTempDetectEnable");
        }
    }

    @Override
    public void updataEntitySlotTempDetectEnable(List<OltSlotAttribute> slots) {
        for (OltSlotAttribute slot : slots) {
            updateSlotBdTempDetectEnable(slot.getEntityId(), slot.getSlotId(), 1);
        }
    }

    private OltSlotFacade getOltSlotFacade(String ip) {
        return facadeFactory.getFacade(ip, OltSlotFacade.class);
    }

    private OltFacade getOltFacade(String ip) {
        return facadeFactory.getFacade(ip, OltFacade.class);
    }

    @Override
    public List<OltSlotAttribute> getOltSlotList(Long entityId) {
        return oltSlotDao.getOltSlotList(entityId);
    }

    @Override
    public List<OltSlotAttribute> getOltPonSlotList(Long entityId) {
        return oltPonDao.getOltPonList(entityId);
    }

    @Override
    public List<OltSlotAttribute> getOltEponSlotList(Long entityId) {
        return oltPonDao.getOltEponList(entityId);
    }

    @Override
    public List<OltSlotAttribute> getOltGponSlotList(Long entityId) {
        return oltPonDao.getOltGponList(entityId);
    }

    @Override
    public List<OltPonAttribute> getSlotPonList(Long slotId) {
        return oltSlotDao.getSlotPonList(slotId);
    }

    @Override
    public List<OltSniAttribute> getSlotSniList(Long slotId) {
        return oltSlotDao.getSlotSniList(slotId);
    }

    @Override
    public List<OltUniAttribute> getSlotUniList(Long onuId) {
        return oltSlotDao.getSlotUniList(onuId);
    }

    @Override
    public List<Integer> getMpuaBoardList(Long entityId) {
        return oltSlotDao.getMpuaBoardList(entityId);
    }

    /**
     * 刷新板卡温度
     * 
     * @param entityId
     * @param slotId
     */
    @Override
    public Integer refreshBdTemperature(Long entityId, Long slotId) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        Long slotIndex = oltSlotDao.getSlotIndex(slotId);
        Integer bdTemp = getOltSlotFacade(snmpParam.getIpAddress()).getBdTemperature(snmpParam, slotIndex);
        if (bdTemp != null) {
            oltSlotDao.updateBdTemperature(slotId, bdTemp);
        }
        return bdTemp;
    }

    @Override
    public void setBoardAdminStatus(Long entityId, Long slotId, Integer status) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        Long slotIndex = oltSlotDao.getSlotIndex(slotId);
        Integer boardAdminStatus = getOltSlotFacade(snmpParam.getIpAddress()).setSlotBdAdminStatus(snmpParam,
                slotIndex, status);
        if (boardAdminStatus != null && boardAdminStatus.equals(status)) {
            oltSlotDao.updateSlotStatus(slotId, status);
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                logger.debug("refresh slot error!");
            }
            // refreshBoardInfo(entityId, slotId);
        } else {
            throw new SetValueConflictException("Business.updateSlotBdAdminStatus");
        }
    }

    @Override
    public Integer getFanRealSpeed(Long entityId, Long fanCardId) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        Long fanIndex = oltSlotDao.getFanCardIndex(fanCardId);
        OltFanStatus fanStatus = getOltSlotFacade(snmpParam.getIpAddress()).getOltFanStatus(snmpParam, fanIndex);
        fanStatus.setEntityId(entityId);
        fanStatus.setFanCardId(fanCardId);
        oltSlotDao.updateFanSpeedControl(fanStatus);
        return fanStatus.getTopSysFanSpeed();
    }

    @Override
    public List<EponBoardStatistics> getBoardList(Map<String, Object> map) {
        return oltSlotDao.getBoardList(map);
    }

    @Override
    public OltFanAttribute getFanAttribute(Long fanCardId) {
        return oltSlotDao.getFanAttribute(fanCardId);
    }

    @Override
    public Long getSlotNoByIndex(Long slotIndex, Long entityId) {
        return oltSlotDao.getSlotNoByIndex(slotIndex, entityId);
    }

    @Override
    public void refreshBoardLampStatus(Long entityId, Long phySLotNo, Long slotIndex) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        Long slotId = oltSlotDao.getSlotIdByPhyNo(entityId, phySLotNo);
        Integer lampStatus = getOltSlotFacade(snmpParam.getIpAddress()).getBdLampStatus(snmpParam, slotIndex);
        if (lampStatus != null) {
            oltSlotDao.updateBoardLampStatus(slotId, lampStatus);
        }
    }

    @Override
    public void deleteOltSlot(Long entityId, Long slotId, Integer boardPreConfigType) {
        List<Long> entityIds = oltSlotDao.getEntityIdBySlotId(slotId);
        entityService.removeEntity(entityIds);// 删除entity表
        oltSlotDao.recordOltSlotCollect(slotId, entityId, boardPreConfigType);
        oltSlotDao.deleteOltSlot(slotId, entityId, boardPreConfigType);
    }

    /* (non-Javadoc)
     * @see com.topvision.ems.epon.olt.service.OltSlotService#batchInsertOrUpdateSlotAttribute(java.util.List, java.util.HashMap)
     */
    @Override
    public void batchInsertOrUpdateSlotAttribute(List<OltSlotAttribute> slots, HashMap<Long, Long> oltMap) {
        oltSlotDao.batchInsertSlotAttribute(slots, oltMap);
        //只要对SLOT表进行了更改,就通知前端进行同步
        try {
            if (!slots.isEmpty()) {
                OltSlotAttribute oltSlotAttribute = slots.get(0);
                Message message = new Message(VersionControl.SYNC_VERSION_CALLBACK);
                message.setData(oltSlotAttribute.getEntityId());
                messagePusher.sendMessage(message);
            }
        } catch (Exception e) {
            logger.info("", e);
        }
    }

    @Override
    public boolean slotIsControlBooard(Long entityId, Integer slotNo) {
        HashMap<String, Object> paraMap = new HashMap<String, Object>();
        paraMap.put("entityId", entityId);
        paraMap.put("slotPhyNo", slotNo);
        Integer slotLogNo = oltSlotDao.querySlotLogNo(paraMap);
        if (slotLogNo != null && slotLogNo == 0) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void refreshBoardAlarmStatus(Long entityId, Long slotIndex) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        OltSlotStatus slotStatus = getOltSlotFacade(snmpParam.getIpAddress()).getOltSlotStatus(snmpParam, slotIndex);
        Long slotId = oltSlotDao.getSlotIdByIndex(entityId, slotIndex);
        Integer bAlarmStatus = -1;
        if (slotStatus != null) {
            bAlarmStatus = slotStatus.getBAlarmStatus();
        }
        oltSlotDao.updateBoardAlarmStatus(slotId, bAlarmStatus);
    }

    @Override
    public boolean slotIsGponBoard(Long entityId, Integer slotNo) {
        HashMap<String, Object> paraMap = new HashMap<String, Object>();
        paraMap.put("entityId", entityId);
        paraMap.put("slotNo", slotNo);
        Integer slotPreType = oltSlotDao.querySlotPreType(paraMap);
        if (EponConstants.BOARD_PRECONFIG_GPUA.equals(slotPreType)||EponConstants.BOARD_ONLINE_MGUA.equals(slotPreType)) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Integer getOltSlotActualType(Long entityId, Integer slotNo) {
        HashMap<String, Object> paraMap = new HashMap<String, Object>();
        paraMap.put("entityId", entityId);
        paraMap.put("slotNo", slotNo);
        return oltSlotDao.querySlotActualType(paraMap);
    }
}
