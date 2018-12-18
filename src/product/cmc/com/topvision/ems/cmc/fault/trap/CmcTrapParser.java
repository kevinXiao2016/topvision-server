/***********************************************************************
 * $Id: CmcTrapParser.java,v1.0 2012-4-10 下午04:09:24 $
 * 
 * @author: loyal
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.fault.trap;

import java.sql.Timestamp;
import java.util.Iterator;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.cmc.ccmts.dao.CmcDao;
import com.topvision.ems.cmc.fault.dao.CmcTrapCodeDao;
import com.topvision.ems.cmc.fault.domain.CmcEventSource;
import com.topvision.ems.cmc.util.CmcUtil;
import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.fault.domain.Event;
import com.topvision.ems.fault.domain.EventSource;
import com.topvision.ems.fault.exception.TrapInstanceException;
import com.topvision.ems.fault.parser.AbstractTrapParser;
import com.topvision.ems.fault.parser.TrapConstants;
import com.topvision.ems.fault.service.EventSender;
import com.topvision.framework.common.MacUtils;
import com.topvision.framework.snmp.Trap;
import com.topvision.framework.utils.CmcIndexUtils;
import com.topvision.platform.ResourceManager;

/**
 * @author loyal
 * @created @2012-4-10-下午04:09:24
 * @Modify by Rod John 2013-4-24
 * 
 */
@Service("cmcTrapParser")
public class CmcTrapParser extends AbstractTrapParser {
    private static final Logger logger = LoggerFactory.getLogger(CmcTrapParser.class);
    @Autowired
    private CmcTrapCodeDao cmcTrapCodeDao;
    @Autowired
    private CmcDao cmcDao;
    private static Integer cos = 200;
    // Modify by Rod
    private static final int CMC_LINK_UP_CODE = 11180;
    private static final int CMC_LINK_DOWN_CODE = 11181;
    

    @Override
    public boolean match(Long entityId, Trap trap) {
        String type = trap.getVarialbleValue(TrapConstants.TRAP_TYPE_OID);

        if (type == null || type.equals("")) {
            return false;
        }

        if (type.equals(CmcTrapConstants.CCMTS_DOMAIN_TRAP_TYPE)) {
            return true;
        }

        if (type.equals(TrapConstants.TRAP_LINKUP) || type.equals(TrapConstants.TRAP_LINKDOWN)) {
            Set<String> keyMap = trap.getVariableBindings().keySet();
            Long ifIndex = null;
            Iterator<String> keyIter = keyMap.iterator();
            while (keyIter.hasNext()) {
                String key = keyIter.next();
                if (key.startsWith(CmcTrapConstants.TRAP_IFINDEX)) {// 查找ifIndex字段
                    try {
                        ifIndex = Long.parseLong(trap.getVariableBindings().get(key).toString());
                        break;
                    } catch (Exception e) {
                        logger.info("ifIndex parser error:", e);
                    }
                }
            }
            if (ifIndex != null && isCmcChannel(ifIndex)) {
                // LinkUp LinkDown (CCMTS)
                return true;
            }
        }

        return false;

    }

    @Override
    public Long parseTrapCode(Trap trap) {
        return Long.parseLong(trap.getVariableBindings().get(CmcTrapConstants.CCMTS_EVENT_ID).toString());
    }

    @Override
    public Integer convertToEmsCode(Long entityId, Trap trap, Long trapCode) {
        String trapType = trap.getVarialbleValue(TrapConstants.TRAP_TYPE_OID);
        if (CmcTrapConstants.TRAP_LINKUP.equals(trapType)) {
            // 信道上线
            return CMC_LINK_UP_CODE;
        } else if (CmcTrapConstants.TRAP_LINKDOWN.equals(trapType)) {
            // 信道下线
            return CMC_LINK_DOWN_CODE;
        }

        if (trapCode.equals(CmcTrapConstants.CC_SPECTRUM_GP_HOP)) {
            String trapText = trap.getVariableBindings().get(CmcTrapConstants.CCMTS_DEVEVENT_TEXT).toString();
            if (trapText.contains(CmcTrapConstants.SPECTRUM_HOP_RECOVERY)) {
                trapCode = CmcTrapConstants.CC_SPECTRUM_GP_HOP_RECOVERY;
            }
        }

        Integer emsCode;
        try {
            emsCode = cmcTrapCodeDao.getEmsCodeFromTrap(trapCode);
            return emsCode;
        } catch (Exception e) {
            // 如果Dao层查询不到相应网管Code 抛出此种异常
            throw new TrapInstanceException(e);
        }
    }

    @Override
    public EventSource parseEventSource(Trap trap, Long trapCode, Long entityId, Integer emsCode)
            throws TrapInstanceException {
        String trapType = trap.getVarialbleValue(TrapConstants.TRAP_TYPE_OID);
        if (CmcTrapConstants.TRAP_LINKUP.equals(trapType) || CmcTrapConstants.TRAP_LINKDOWN.equals(trapType)) {
            // 信道上线,信道下线
            return parseChannelEventSource(trap, trapCode, entityId, emsCode);
        } else {
            return parseCmtsEventSource(trap, trapCode, entityId, emsCode);
        }
    }

    private EventSource parseCmtsEventSource(Trap trap, Long trapCode, Long entityId, Integer emsCode) {
        CmcEventSource eventSource = new CmcEventSource(entityId);

        Entity entity = entityDao.selectByPrimaryKey(entityId);

        String mac = null;
        try {
            if (entityTypeService.isCcmtsWithAgent(entity.getTypeId())) {
                mac = entity.getMac();
            } else {
                mac = CmcUtil.getCCMTSMACFromTrapText(
                        trap.getVariableBindings().get(CmcTrapConstants.CCMTS_DEVEVENT_TEXT).toString());

                // Modify by Rod CC上报的告警必须包括MAC字段
                // MODIFY BY JAY 以上的限制是网管自认为的，其实有很多CC发送的告警无法携带mac地址，这会导致有些高级没有地方出来，所以做的扩展
                if (mac != null && mac.length() == 0) {
                    entity = cmcDao.getCmcByTrapInfo(trap.getAddress(), mac);
                    if (entity == null) {
                        throw new TrapInstanceException("cannot find ccmts, entityId: " + entityId);
                    }
                    entityId = entity.getEntityId();
                }
            }
        } catch (Exception e) {
            logger.info("CmcUtil.getCCMTSMACFromTrapText error:", e);
        }

        if (trapCode == CmcTrapConstants.CC_CLOSE_DOWNSTREAM || trapCode == CmcTrapConstants.CC_CLOSE_UPSTREAM
                || trapCode == CmcTrapConstants.CC_OPEN_DOWNSTREAM || trapCode == CmcTrapConstants.CC_OPEN_UPSTREAM) {
            String channelName = CmcUtil.getChannelIdFromTrapText(
                    trap.getVariableBindings().get(CmcTrapConstants.CCMTS_DEVEVENT_TEXT).toString());
            eventSource.setChannelName(channelName);
        } else if (trapCode == CmcTrapConstants.CC_SPECTRUM_GP_HOP) {
            // String a =
            // "CMTS-MAC=0024.6850.1141,channel:1,spectrum-group hop to:frequency 9000000,width
            // 1600000,power 6.0,modulation qpsk,current snr 38.8,corrCode rate 0,uncorrCode rate
            // 0."
            String trapText = trap.getVariableBindings().get(CmcTrapConstants.CCMTS_DEVEVENT_TEXT).toString();
            String channelId = trapText.split(":")[1].split(",")[0];
            eventSource.setSource(CmcEventSource.SOURCE_UP_CHANNEL);
            eventSource.setChannelId(Long.valueOf(channelId));
        }

        eventSource.setMac(mac);
        eventSource.setName(entity.getName());

        return eventSource;
    }

    private EventSource parseChannelEventSource(Trap trap, Long trapCode, Long entityId, Integer emsCode) {
        CmcEventSource eventSource = new CmcEventSource(entityId);

        // 获取CMC Mac
        Long ifIndex = getIfIndex(trap);
        String mac = new MacUtils(cmcDao.getCmcMacByEntityIdAndChannelIndex(entityId, ifIndex))
                .toString(MacUtils.MAOHAO).toUpperCase();
        eventSource.setMac(mac);
        Entity cmcEntity = cmcDao.getCmcByTrapInfo(trap.getAddress(), mac);
        // source: slot/pon/cmcId/[D/U]channelId
        String cmcName = cmcEntity.getName();
        eventSource.setName(cmcName);
        if (CmcIndexUtils.getChannelType(ifIndex) == 0) {
            eventSource.setSource(CmcEventSource.SOURCE_UP_CHANNEL);
            eventSource.setChannelId(CmcIndexUtils.getChannelId(ifIndex));
        } else if (CmcIndexUtils.getChannelType(ifIndex) == 1) {
            eventSource.setSource(CmcEventSource.SOURCE_DOWN_CHANNEL);
            eventSource.setChannelId(CmcIndexUtils.getChannelId(ifIndex));
        }

        return eventSource;
    }

    private Long getIfIndex(Trap trap) {
        Long ifIndex = null;

        Set<String> keyMap = trap.getVariableBindings().keySet();
        Iterator<String> keyIter = keyMap.iterator();
        while (keyIter.hasNext()) {
            String key = keyIter.next();
            if (key.startsWith(CmcTrapConstants.TRAP_IFINDEX)) {// 查找ifIndex字段
                try {
                    ifIndex = Long.parseLong(trap.getVariableBindings().get(key).toString());
                } catch (Exception e) {
                    logger.info("ifIndex parser error:", e);
                }
            }
        }
        return ifIndex;
    }

    @Override
    public Event buildEvent(Trap trap, Long trapCode, Integer emsCode, EventSource eventSource) {
        CmcEventSource cmcEventSource = (CmcEventSource) eventSource;

        Event event = EventSender.getInstance().createEvent(emsCode, trap.getAddress(), eventSource.formatSource());
        event.setEntityId(eventSource.getEntityId());
        event.setCreateTime(new Timestamp(trap.getTrapTime().getTime()));
        event.setUserObject(trap);

        String message = formatMessage(emsCode, cmcEventSource);

        event.setMessage(message);
        if (logger.isDebugEnabled()) {
            logger.debug("onTrap.toAlert:{}", event);
        }

        return event;
    }

    private String formatMessage(Integer emsCode, CmcEventSource cmcEventSource) {
        String message = "";
        
        message = parseEventDescr(emsCode, cmcEventSource.formatSource(), cmcEventSource.getName());

        /*if (CmcEventSource.SOURCE_UP_CHANNEL.equals(cmcEventSource.getSource())
                || CmcEventSource.SOURCE_DOWN_CHANNEL.equals(cmcEventSource.getSource())) {
            String channelType = "";
            if (CmcEventSource.SOURCE_UP_CHANNEL.equals(cmcEventSource.getSource())) {
                channelType = getString("upchannel");
            } else if (CmcEventSource.SOURCE_DOWN_CHANNEL.equals(cmcEventSource.getSource())) {
                channelType = getString("downchannel");
            }
            // message(slot/pon/cmcId/ 下行信道5（00:00:00:01:DD:6E ） 上线
            message = "CMTS[" + cmcEventSource.getMac() + "]" + channelType + cmcEventSource.getChannelId()
                    + Symbol.PARENTHESIS_LEFT + cmcEventSource.getName() + Symbol.PARENTHESIS_RIGHT;
            if (CMC_LINK_UP_CODE == emsCode) {
                message += getString("linkUp");
            } else if (CMC_LINK_DOWN_CODE == emsCode) {
                message += getString("linkDown");
            }
        } else {
            message = parseEventDescr(emsCode, cmcEventSource.formatSource(), cmcEventSource.getName());
        }*/

        return message;
    }

    /**
     * 判断上下线信息是否属于cc端口
     * 
     * @param ifIndex
     * @return
     */
    public boolean isCmcChannel(Long ifIndex) {
        return cmcDao.isCmcChannel(ifIndex);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.fault.parser.TrapParser#getTrapCos()
     */
    @Override
    public Integer getTrapCos() {
        return cos;
    }

    /**
     * @return the cmcDao
     */
    public CmcDao getCmcDao() {
        return cmcDao;
    }

    /**
     * @param cmcDao
     *            the cmcDao to set
     */
    public void setCmcDao(CmcDao cmcDao) {
        this.cmcDao = cmcDao;
    }

    private String getString(String key, String... strings) {
        try {
            return ResourceManager.getResourceManager("com.topvision.ems.cmc.resources").getString(key, strings);
        } catch (Exception e) {
            logger.debug("", e);
            return key;
        }
    }

    /*
     * private String getAlertString(String key, String... strings) { try { return
     * ResourceManager.getResourceManager("com.topvision.ems.fault.resources").getString(key,
     * strings); } catch (Exception e) { logger.error("", e); return key; } }
     */

    /**
     * 组合告警的描述
     * 
     * @Modify by Rod
     */
    private String parseEventDescr(Integer typeId, String source, String cmcName) {
        String result = "";
        switch (typeId) {
        case 101002:
            // 发现CC连接
            result = getString("CCMTS.DeviceEvent.discoveryCmcLink");
            break;
        case 101003:
            // 丢失CC连接
            result = getString("CCMTS.DeviceEvent.loseCmcLink");
            break;
        case 101004:
            // CM下线
            result = getString("CCMTS.DeviceEvent.cmLinkDown");
            break;
        case 101005:
            // CM上线(range通过)
            result = getString("CCMTS.DeviceEvent.cmLinkUpRange");
            break;
        case 101006:
            // CM上线(DHCP和tftp过程通过)
            result = getString("CCMTS.DeviceEvent.cmLinkUpDHCP");
            break;
        case 101010:
            // 修改下行信道参数
            result = getString("CCMTS.DeviceEvent.modifyDownStream");
            break;
        case 101011:
            // 关闭下行信道
            result = getString("CCMTS.DeviceEvent.closeDownStream");
            break;
        case 101012:
            // 开启下行信道
            result = getString("CCMTS.DeviceEvent.openDownStream");
            break;
        case 101013:
            // 关闭上行信道
            result = getString("CCMTS.DeviceEvent.closeUpStream");
            break;
        case 101014:
            // 开启上行信道
            result = getString("CCMTS.DeviceEvent.openUpStream");
            break;
        case 101015:
            // 修改上行信道参数
            result = getString("CCMTS.DeviceEvent.mofidyUpStream");
            break;
        case 101016:
            // 设置上行信道电平
            result = getString("CCMTS.DeviceEvent.configUpStream");
            break;
        case 101017:
            // CC配置异常(失败或告警)
            result = getString("CCMTS.DeviceEvent.cmcConfigError");
            break;
        case 101019:
            // 主线DB同步失败(线卡到主控)
            result = getString("CCMTS.DeviceEvent.DbSyncFailed1");
            break;
        case 101020:
            // 主线DB同步失败(主控到线卡)
            result = getString("CCMTS.DeviceEvent.DbSyncFailed2");
            break;
        case 101022:
            // 主线DB同步成功(主控到线卡)
            result = getString("CCMTS.DeviceEvent.DbSyncSuccess");
            break;
        case 101023:
            // 线卡上线
            result = getString("CCMTS.DeviceEvent.boardOnline");
            break;
        case 101024:
            // 线卡下线
            result = getString("CCMTS.DeviceEvent.boardOffline");
            break;
        case 101025:
            // 重启CC
            result = getString("CCMTS.DeviceEvent.cmcRestart");
            break;
        case 101026:
            // 设置下行信道为IPQAM模式
            result = getString("CCMTS.DeviceEvent.configDownStreamToIPQAM");
            break;
        case 101027:
            // CCMTS主动上报告警状态
            result = getString("CCMTS.DeviceEvent.ccmtsReportAlertStatus");
            break;
        case 101028:
            // CCMTS主动清除告警状态
            result = getString("CCMTS.DeviceEvent.ccmtsReportAlertClearStatus");
            break;
        case 104008:
            // DHCP Relay错误
            result = getString("CCMTS.DeviceEvent.dhcpError");
            break;
        case 101035:
            result = getString("CCMTS.SpectrumHopRecovery");
            break;
        case 101036:
            result = getString("CCMTS.SpectrumHop");
            break;
        }
        return new StringBuilder().append(source).append(" ").append(cmcName).append(" ").append(result).toString();
    }

    /**
     * @return the cmcTrapCodeDao
     */
    public CmcTrapCodeDao getCmcTrapCodeDao() {
        return cmcTrapCodeDao;
    }

    /**
     * @param cmcTrapCodeDao
     *            the cmcTrapCodeDao to set
     */
    public void setCmcTrapCodeDao(CmcTrapCodeDao cmcTrapCodeDao) {
        this.cmcTrapCodeDao = cmcTrapCodeDao;
    }
}