/**
 * 
 */
package com.topvision.ems.realtime.action;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.topvision.ems.network.domain.LinkEx;
import com.topvision.ems.network.domain.LinkRealFlow;
import com.topvision.ems.network.service.LinkService;
import com.topvision.framework.web.struts2.BaseAction;
import com.topvision.platform.service.SystemPreferencesService;

import net.sf.json.JSONObject;

/**
 * @author niejun
 * 
 */
@Controller("linkRealFlowAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class LinkRealFlowAction extends BaseAction {
    private static final long serialVersionUID = 6916107059923783301L;
    private static Logger logger = LoggerFactory.getLogger(LinkRealFlowAction.class);
    @Autowired
    private SystemPreferencesService systemPreferencesService;
    @Autowired
    private LinkService linkService;
    private long linkId;
    private LinkEx linkEx;
    private long synTime;
    private LinkRealFlow linkRealFlow;

    public String showLinkRealFlow() throws Exception {
        JSONObject json = new JSONObject();
        json.put("time", System.currentTimeMillis());
        try {
            synTime = System.currentTimeMillis();
            linkRealFlow = linkService.getLinkRealFlow(linkId);
            linkRealFlow = new LinkRealFlow();
            linkRealFlow.setReceiveFlow((long) (2000 * Math.random()));
            linkRealFlow.setSendFlow((long) (2000 * Math.random()));

            linkRealFlow.setTotalFlow(linkRealFlow.getSendFlow() + linkRealFlow.getReceiveFlow());

            linkRealFlow.setReceiveLosedPacketNumber((long) (400 * Math.random()));
            linkRealFlow.setSendLosedPacketNumber((long) (400 * Math.random()));

            linkRealFlow.setReceiveLosedPacketRate((long) (100 * Math.random()));
            linkRealFlow.setSendLosedPacketRate((long) (100 * Math.random()));

            json.put("totalFlow", linkRealFlow.getTotalFlow());
            json.put("sendFlow", linkRealFlow.getSendFlow());
            json.put("receiveFlow", linkRealFlow.getReceiveFlow());
            json.put("sendLosedPacketNumber", linkRealFlow.getSendLosedPacketNumber());
            json.put("receiveLosedPacketNumber", linkRealFlow.getReceiveLosedPacketNumber());
            json.put("sendLosedPacketRate", linkRealFlow.getSendLosedPacketRate());
            json.put("receiveLosedPacketRate", linkRealFlow.getReceiveLosedPacketRate());
        } catch (Exception ex) {
            logger.error("Load Real Disks.", ex);
            throw ex;
        } finally {
            writeDataToAjax(json);
        }
        return NONE;
    }

    public String showLinkRealFlowJsp() throws Exception {
        try {
            linkEx = linkService.getLinkEx(linkId);
            if (systemPreferencesService.isUsedApplet()) {
                return "applet";
            }
            synTime = System.currentTimeMillis();
            linkRealFlow = linkService.getLinkRealFlow(linkId);
            linkRealFlow = new LinkRealFlow();
            linkRealFlow.setReceiveFlow((long) (4000 * Math.random()));
            linkRealFlow.setSendFlow((long) (4000 * Math.random()));

            linkRealFlow.setTotalFlow(linkRealFlow.getSendFlow() + linkRealFlow.getReceiveFlow());

            linkRealFlow.setReceiveLosedPacketNumber((long) (400 * Math.random()));
            linkRealFlow.setSendLosedPacketNumber((long) (400 * Math.random()));

            linkRealFlow.setReceiveLosedPacketRate((long) (100 * Math.random()));
            linkRealFlow.setSendLosedPacketRate((long) (100 * Math.random()));
        } catch (Exception ex) {
            logger.error("Show Link Real Flow.", ex);
            throw ex;
        }
        return SUCCESS;
    }

    public LinkEx getLinkEx() {
        return linkEx;
    }

    public long getLinkId() {
        return linkId;
    }

    public LinkRealFlow getLinkRealFlow() {
        return this.linkRealFlow;
    }

    public long getSynTime() {
        return synTime;
    }

    public void setLinkEx(LinkEx linkEx) {
        this.linkEx = linkEx;
    }

    public void setLinkId(long linkId) {
        this.linkId = linkId;
    }

    public void setLinkRealFlow(LinkRealFlow linkRealFlow) {
        this.linkRealFlow = linkRealFlow;
    }

    public void setLinkService(LinkService linkService) {
        this.linkService = linkService;
    }

    public void setSynTime(long synTime) {
        this.synTime = synTime;
    }

    public void setSystemPreferencesService(SystemPreferencesService systemPreferencesService) {
        this.systemPreferencesService = systemPreferencesService;
    }

}
