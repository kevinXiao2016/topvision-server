package com.topvision.ems.network.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.network.dao.LinkDao;
import com.topvision.ems.network.domain.Link;
import com.topvision.ems.network.domain.LinkEx;
import com.topvision.ems.network.domain.LinkRealFlow;
import com.topvision.ems.network.domain.PortEx;
import com.topvision.ems.network.domain.PortPerf;
import com.topvision.ems.network.message.LinkEvent;
import com.topvision.ems.network.message.LinkListener;
import com.topvision.ems.network.service.LinkService;
import com.topvision.framework.event.MyResultHandler;
import com.topvision.framework.service.BaseService;
import com.topvision.platform.domain.SystemLog;
import com.topvision.platform.message.service.MessageService;

@Service("linkService")
public class LinkServiceImpl extends BaseService implements LinkService {
    @Autowired
    private LinkDao linkDao;
    @Autowired
    private MessageService messageService;

    protected void firedLinkAdded(LinkEvent evt) {
        evt.setActionName("linkAdded");
        evt.setListener(LinkListener.class);

        messageService.addMessage(evt);
    }

    protected void firedRemoved(LinkEvent evt) {
        evt.setActionName("linkRemoved");
        evt.setListener(LinkListener.class);

        messageService.addMessage(evt);
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.service.LinkService#getAllLink()
     */
    @Override
    public List<LinkEx> getAllLink() {
        return linkDao.getAllLink(null);
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.service.LinkService#getAllLink(Long)
     */
    @Override
    public List<LinkEx> getAllLink(Long entityId) {
        Map<String, Long> map = new HashMap<String, Long>();
        map.put("entityId", entityId);

        return linkDao.getAllLink(map);
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.service.LinkService#getLinkEx(Long)
     */
    @Override
    public LinkEx getLinkEx(Long linkId) {
        return (LinkEx) linkDao.selectByPrimaryKey(linkId);
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.service.LinkService#getLinkFlowTop(java.util.Map)
     */
    @Override
    public List<LinkEx> getLinkFlowTop(Map<String, String> map) {
        return linkDao.getLinkFlowTop(map);
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.service.LinkService#getLinkRateTop(java.util.Map)
     */
    @Override
    public List<LinkEx> getLinkRateTop(Map<String, String> map) {
        return linkDao.getLinkRateTop(map);
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.service.LinkService#getLinkRealFlow(Long)
     */
    @Override
    public LinkRealFlow getLinkRealFlow(Long linkId) {
        return null;
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.service.LinkService#getLinkTableByEntity(Long)
     */
    @Override
    public List<PortEx> getLinkTableByEntity(Long entityId) {
        return linkDao.getLinkTableByEntity(entityId);
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.service.LinkService#getPortPerfByEntityId(Long)
     */
    @Override
    public List<PortPerf> getPortPerfByEntityId(Long entityId) {
        return null;
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.service.LinkService#insertLink(com.topvision.platform.domain.SystemLog,
     *      com.topvision.ems.network.domain.Link, Long)
     */
    @Override
    public void insertLink(SystemLog sysLog, Link link, Long folderId) {
        linkDao.insertEntity(link);
        LinkEvent evt = new LinkEvent(this);
        evt.setLink(link);
        evt.setFolderId(folderId);

        this.firedLinkAdded(evt);
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.service.LinkService#queryLink(java.lang.String)
     */
    @Override
    public List<LinkEx> queryLink(String query) {
        return linkDao.queryLink(query);
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.service.LinkService#restoreLinkCurrentState(com.topvision.framework.event.MyResultHandler)
     */
    @Override
    public void restoreLinkCurrentState(MyResultHandler handler) {
        linkDao.selectByMap(null, handler);
    }

    public void setLinkDao(LinkDao linkDao) {
        this.linkDao = linkDao;
    }

    /**
     * @param messageService
     *            the messageService to set
     */
    public final void setMessageService(MessageService messageService) {
        this.messageService = messageService;
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.service.LinkService#updateLink(com.topvision.ems.network.domain.LinkEx)
     */
    @Override
    public void updateLink(LinkEx link) {
        linkDao.updateEntity(link);
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.service.LinkService#updateOutline(com.topvision.ems.network.domain.LinkEx)
     */
    @Override
    public void updateOutline(LinkEx link) {
        linkDao.updateOutline(link);
    }

    /**
     * 通过entityId和portId.
     * 
     * @param entityId
     * @param ifIndex
     */
    @Override
    public Link getLinkByPort(Long entityId, Long ifIndex) {
        List<Link> links = linkDao.getLinkByPort(entityId, ifIndex); // To
                                                                     // change
                                                                     // body
                                                                     // of
                                                                     // implemented
                                                                     // methods
                                                                     // use
                                                                     // File
                                                                     // |
                                                                     // Settings
                                                                     // |
                                                                     // File
                                                                     // Templates.
        if (links != null && !links.isEmpty()) {
            return links.get(0);
        } else {
            return null;
        }
    }
}
