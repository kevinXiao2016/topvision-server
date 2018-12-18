/***********************************************************************
 * $Id: CmPartialServiceEventParser.java,v1.0 2016年11月20日 下午2:58:12 $
 * 
 * @author: vanzand
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.fault.parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Service;

import com.topvision.ems.cmc.fault.trap.CmcTrapConstants;
import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.fault.domain.Alert;
import com.topvision.ems.fault.domain.AlertType;
import com.topvision.ems.fault.domain.Event;
import com.topvision.ems.fault.domain.EventType;
import com.topvision.ems.fault.parser.EventParser;
import com.topvision.ems.network.domain.EntityAddress;
import com.topvision.framework.common.MacUtils;
import com.topvision.framework.snmp.Trap;

/**
 * @author vanzand
 * @created @2016年11月20日-下午2:58:12
 *
 */
@Service("cmPartialServiceEventParser")
public class CmPartialServiceEventParser extends EventParser {
    private static final int PARTIAL_SERVICE = 1904649;
    private static final int PARTIAL_SERVICE_REVOCERY = 1904650;

    private LinkedBlockingQueue<Event> partialEventQueue;
    private ExecutorService partialExecutorService;

    private final int threadNum = 1;

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.fault.parser.EventParser#initialize()
     */
    @Override
    public void initialize() {
        partialEventQueue = new LinkedBlockingQueue<Event>();
        partialExecutorService = java.util.concurrent.Executors.newFixedThreadPool(threadNum);
        partialExecutorService.execute(new Runnable() {

            @Override
            public void run() {
                Thread.currentThread().setName("CmPartialServiceEventParser");
                while (true) {
                    try {
                        Event event = partialEventQueue.take();
                        if (event.getUserObject() instanceof Trap) {

                            // 封装告警信息
                            // 解析partial service告警描述
                            String trapText = ((Trap) event.getUserObject()).getVariableBindings()
                                    .get(CmcTrapConstants.CCMTS_DEVEVENT_TEXT).toString();
                            if (event.getTypeId() == PARTIAL_SERVICE) {
                                // 封装信息和source
                                parsePartialDesc(event, trapText);
                            } else if (event.getTypeId() == PARTIAL_SERVICE_REVOCERY) {
                                // 封装信息和source
                                parsePartialRecoveryDesc(event, trapText);
                            }
                            doEvent(event);
                        }
                    } catch (Throwable e) {
                        logger.error("", e);
                    }
                }
            }
        });

        // 将自身添加到事件处理器队列
        getEventService().registEventParser(this);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.fault.parser.EventParser#destroy()
     */
    @Override
    public void destroy() {
        partialEventQueue = null;
        partialExecutorService.shutdown();
        getEventService().unRegistEventParser(this);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.fault.parser.EventParser#parse(com.topvision.ems.fault.domain.Event)
     */
    @Override
    public boolean parse(Event event) {
        if (event.getTypeId().intValue() == PARTIAL_SERVICE
                || event.getTypeId().intValue() == PARTIAL_SERVICE_REVOCERY) {
            if (event.getTypeId().intValue() == PARTIAL_SERVICE_REVOCERY) {
                event.setClear(true);
            }
            partialEventQueue.add(event);
            return true;
        }
        return false;
    }

    @Override
    protected List<Alert> selectCurrentAlert(Map<String, String> map) {
        // CM partial service，如果是恢复告警，需要模糊匹配source，匹配mac一致的
        if (map.containsKey("eventTypeId") && map.get("eventTypeId").equals(String.valueOf(PARTIAL_SERVICE_REVOCERY))) {
            if (map.containsKey("source")) {
                map.put("blurrySource", map.get("source"));
                map.remove("source");
            }
        }
        return super.selectCurrentAlert(map);
    }

    @Override
    public void doEvent(Event event) {
        List<EventType> eventTypes = getEventTypeByTypeId(event.getTypeId());
        if (eventTypes.size() == 0) {
            return;
        }
        AlertType alertType = null;
        for (EventType eventType : eventTypes) {
            event.setName(eventType.getDisplayName());
            if (eventType.getAlertTypeId() == null) {
                // 进入此处，表示该事件不存在关联告警，此处只可能对应一条eventType，用break或者continue均可
                continue;
                // alertType = alertTypes.get(eventType.getTypeId());
            } else {
                alertType = getAlertTypeByTypeId(eventType.getAlertTypeId());
            }
            if (alertType == null) {
                continue;
            }
            if (!alertType.getActive()) {
                continue;
            }
            if (entityAddressDao != null && event.getEntityId() == null) {
                EntityAddress ea = entityAddressDao.selectByAddress(event.getHost());
                if (ea != null) {
                    event.setEntityId(ea.getEntityId());
                }
            }
            Map<String, String> map = new HashMap<String, String>();
            map.put("eventTypeId", event.getTypeId().toString());
            map.put("typeId", String.valueOf(alertType.getTypeId()));
            map.put("source", event.getSource());
            if (event.getEntityId() != null) {
                map.put("entityId", String.valueOf(event.getEntityId()));
            } else {
                map.put("host", event.getHost());
            }
            if (event.getMonitorId() != null) {
                map.put("monitorId", String.valueOf(event.getMonitorId()));
            }
            List<Alert> oldAlertList = selectCurrentAlert(map);
            Alert alert = null;
            if (oldAlertList != null && oldAlertList.size() > 0 && event.isClear() != null && event.isClear()) {
                // 存在需求清除的告警，且此事件是清除告警事件
                // 要清除匹配的所有alert
                for (Alert oldAlert : oldAlertList) {
                    oldAlert.setIp(event.getIp());
                    oldAlert.setClearTime(event.getCreateTime());
                    alertService.txClearAlert(oldAlert, event.getMessage());
                }
            } else if (oldAlertList != null && oldAlertList.size() > 0) {
                // 事件再次发生，更新次数
                alert = oldAlertList.get(0);
                alert.happenedAgain();
                // 告警等级提升,改变告警等级
                if (alertType.getSmartUpdate()) {
                    if (alert.getHappenTimes() >= Integer.parseInt(alertType.getAlertTimes())) {
                        alert.setLevelId(Byte.parseByte((alertType.getUpdateLevel().toString())));
                    }
                } else {
                    // 网管可能改变了告警等级,每次都与网管等级保持一致
                    alert.setLevelId(alertType.getLevelId());
                }
                alert.setLastTime(event.getCreateTime());
                alertService.updateAlert(alert);
            } else if (event.isClear() == null || !event.isClear()) {
                // 插入事件
                alert = transform(event);
                alert.setTypeId(alertType.getTypeId());
                alert.setTypeName(alertType.getDisplayName());
                if (event.getLevelId() == null) {
                    alert.setLevelId(alertType.getLevelId());
                } else {
                    alert.setLevelId(event.getLevelId());
                }
                alertService.insertAlert(alert);
            } else if ((oldAlertList == null || oldAlertList.size() == 0) && event.isClear()) {
                // 当手动清除告警后的处理
                alert = transform(event);
                alert.setTypeId(alertType.getTypeId());
                alert.setTypeName(alertType.getDisplayName());
                // 清除告警事件，将推到前台的告警level设置为0屏蔽前台推送提示框
                alert.setLevelId((byte) 0);
                // 只需要通知页面即可
                alertService.fireAlert(alert, false);
            }
        }
        // event.setAlertId(alert.getAlertId());
        if (eventDao != null) {
            // 对于插入事件时网管数据已经不存在的情况,不进行任何处理(无法插入数据库)
            Entity entity = entityService.getEntityFromDB(event.getEntityId());
            if (entity != null) {
                eventDao.insertEntity(event);
            }
        }
    }

    /**
     * 封装partial service message和resource
     * 
     * @param eventText
     * @return
     */
    private static void parsePartialDesc(Event event, String eventText) {
        StringBuilder result = new StringBuilder();

        // 从文本中取出CM-MAC US信道 DS信道
        Pattern p = Pattern.compile("<(.*?)>");
        Matcher m = p.matcher(eventText);
        List<String> msgList = new ArrayList<String>();
        while (m.find()) {
            msgList.add(m.group(1));
        }
        if (msgList.size() == 3) {
            // 获取CM-MAC
            String macMsg = msgList.get(0);
            String cmMac = MacUtils.convertToMaohaoFormat(macMsg.substring(macMsg.indexOf("=") + 1, macMsg.length()));

            // 获取上行信道信息
            String usStr = "[" + msgList.get(1) + "]";

            // 获取下行信道信息
            String dsStr = "[" + msgList.get(2) + "]";

            // 添加CM相关信息，封装格式 CMTS[MAC] NAME [US] [DS] CM[MAC] Partial Service
            result.append(String.format("%s %s CM[%s] Partial Service", usStr, dsStr, cmMac));
            // 封装source（粒度精确到CMMAC_US_DS）
            event.setSource(String.format("%s_%s_%s", cmMac, usStr, dsStr));
        } else {
            // 不是约定的格式
            result.append(eventText);
        }
        event.setMessage(event.getMessage() + result.toString());
    }

    /**
     * 封装partial service recovery message和resource
     * 
     * @param event
     * @param eventText
     */
    private static void parsePartialRecoveryDesc(Event event, String eventText) {
        // 从文本中获取MAC地址
        Pattern p = Pattern.compile("<(.*?)>");
        Matcher m = p.matcher(eventText);
        List<String> msgList = new ArrayList<String>();
        while (m.find()) {
            msgList.add(m.group(1));
        }
        if (msgList.size() == 1) {
            String macMsg = msgList.get(0);
            String cmMac = MacUtils.convertToMaohaoFormat(macMsg.substring(macMsg.indexOf("=") + 1, macMsg.length()));
            event.setSource(cmMac);
        }
    }

}
