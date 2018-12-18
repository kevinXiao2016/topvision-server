/***********************************************************************
 * $Id: CmActionEventParser.java,v1.0 2015-9-8 上午10:09:09 $
 * 
 * @author: jay
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.fault.parser;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.cmc.cm.service.CmService;
import com.topvision.ems.cmc.cpe.dao.CpeAnalyseDao;
import com.topvision.ems.cmc.facade.domain.CmAttribute;
import com.topvision.ems.cmc.fault.trap.CmcTrapConstants;
import com.topvision.ems.cmc.performance.domain.CmAct;
import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.fault.domain.Event;
import com.topvision.ems.fault.parser.EventParser;
import com.topvision.ems.network.service.EntityService;
import com.topvision.ems.template.service.EntityTypeService;
import com.topvision.framework.snmp.Trap;
import com.topvision.platform.message.service.MessageService;

/**
 * @author jay
 * @created @2015-9-8-上午10:09:09
 * 
 */
@Service("cmActionEventParser")
public class CmActionEventParser extends EventParser {
    private static final int CMOFFLINECODE = 101004;
    private static final int CMONLINECODE = 101006;
    private ExecutorService cmActionExecutorService;
    @Autowired
    private MessageService messageService;
    @Autowired
    private CpeAnalyseDao cpeAnalyseDao;
    @Autowired
    private CmService cmService;
    @Autowired
    private EntityService entityService;
    @Autowired
    private EntityTypeService entityTypeService;
    private LinkedBlockingQueue<Event> cmActionQueue;
    private final int threadNum = 1;

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.fault.parser.EventParser#initialize()
     */
    @Override
    @PostConstruct
    public void initialize() {
        // 初始化线程队列
        cmActionQueue = new LinkedBlockingQueue<Event>();
        cmActionExecutorService = java.util.concurrent.Executors.newFixedThreadPool(threadNum);
        cmActionExecutorService.execute(new Runnable() {
            @Override
            public void run() {
                Thread.currentThread().setName("CmActionEventParser");
                while (true) {
                    try {
                        Event event = cmActionQueue.take();
                        if (event.getUserObject() instanceof Trap) {
                            if (event.getEntityId() == null) {
                                continue;
                            }
                            Entity entity = entityService.getEntity(event.getEntityId());
                            if (entity == null) {
                                continue;
                            }
                            Long entityId;
                            if (entityTypeService.isCcmtsWithoutAgent(entity.getTypeId())) {
                                entityId = entity.getParentId();
                            } else if (entityTypeService.isCcmtsWithAgent(entity.getTypeId())) {
                                entityId = entity.getEntityId();
                            } else {
                                continue;
                            }

                            Trap trap = (Trap) event.getUserObject();
                            String text = (String) trap.getVariableBindings().get(CmcTrapConstants.CCMTS_DEVEVENT_TEXT);
                            // while (codeText.endsWith(":00")) {
                            // codeText = codeText.substring(0,codeText.length() - 3);
                            // }
                            // String text = TrapUtil.getVisiableCharFromTrapByte(codeText);
                            logger.debug("CCMTS_DEVEVENT_TEXT : " + text);
                            int index = text.indexOf("CM-MAC=");
                            String cmmac = text.substring(index + 7, index + 7 + 14);
                            logger.debug("CM-MAC=" + cmmac);
                            CmAttribute cmAttribute = new CmAttribute();
                            cmAttribute.setStatusMacAddress(cmmac);

                            CmAct lastCmAct = cpeAnalyseDao.getCmLastStatus(entityId, cmAttribute);
                            if (lastCmAct != null) {
                                long now = System.currentTimeMillis();
                                CmAct perAct = null;
                                CmAct nowAct = lastCmAct.copyFrom();
                                nowAct.setRealtimeLong(now);
                                nowAct.setTimeLong(now);
                                if (event.getTypeId().intValue() == CMOFFLINECODE) {
                                    try {
                                        cmService.updateCmAttribute(entityId, cmmac, 1);
                                    } catch (Exception e) {
                                        logger.debug("", e);
                                    }
                                    if (lastCmAct.getAction() == CmAct.OFFLINE) {
                                        perAct = lastCmAct.copyFrom();
                                        perAct.setRealtimeLong(now - 60000);
                                        perAct.setTimeLong(now - 60000);
                                        perAct.setAction(CmAct.ONLINE);
                                    }
                                    nowAct.setAction(CmAct.OFFLINE);
                                    nowAct.setCmip(0L);
                                } else {
                                    try {
                                        cmService.updateCmAttribute(entityId, cmmac, 6);
                                    } catch (Exception e) {
                                        logger.debug("", e);
                                    }
                                    if (lastCmAct.getAction() == CmAct.ONLINE) {
                                        perAct = lastCmAct.copyFrom();
                                        perAct.setRealtimeLong(now - 60000);
                                        perAct.setTimeLong(now - 60000);
                                        perAct.setCmip(0L);
                                        perAct.setAction(CmAct.OFFLINE);
                                    }
                                    nowAct.setAction(CmAct.ONLINE);

                                }
                                if (perAct != null) {
                                    cpeAnalyseDao.insertCmAct(perAct);
                                }
                                cpeAnalyseDao.insertCmAct(nowAct);
                            } else {
                                logger.debug("CM[" + cmmac + "] lastCmAct is null, skip this event.");
                            }
                        }
                        doEvent(event);
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
    @PreDestroy
    public void destroy() {
        cmActionQueue = null;
        cmActionExecutorService.shutdown();
        getEventService().unRegistEventParser(this);
    }

    /**
     * @return the messageService
     */
    public MessageService getMessageService() {
        return messageService;
    }

    /**
     * @param messageService
     *            the messageService to set
     */
    public void setMessageService(MessageService messageService) {
        this.messageService = messageService;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.fault.parser.EventParser#parse(com.topvision.ems.fault.domain.Event)
     */
    @Override
    public boolean parse(Event event) {
        if (event.getTypeId().intValue() == CMOFFLINECODE || event.getTypeId().intValue() == CMONLINECODE) {
            if (event.getTypeId().intValue() == CMONLINECODE) {
                event.setClear(true);
            }
            cmActionQueue.add(event);
            return true;
        }
        return false;
    }

}
