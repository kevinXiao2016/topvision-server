package com.topvision.ems.network.service.impl;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.facade.ProbeFacade;
import com.topvision.ems.facade.domain.TopologyResult;
import com.topvision.ems.network.domain.Online;
import com.topvision.ems.network.message.PortEvent;
import com.topvision.ems.network.message.PortListener;
import com.topvision.ems.network.service.OnlineService;
import com.topvision.exception.facade.DeviceNotExistException;
import com.topvision.exception.service.NetworkException;
import com.topvision.framework.service.BaseService;
import com.topvision.platform.facade.FacadeFactory;
import com.topvision.platform.message.event.EntityEvent;
import com.topvision.platform.message.event.EntityListener;
import com.topvision.platform.message.service.MessageService;
import com.topvision.platform.service.SystemPreferencesService;
import com.topvision.platform.util.StringUtil;

@Service("onlineService")
public class OnlineServiceImpl extends BaseService implements OnlineService, PortListener, EntityListener {
    private static Logger logger = LoggerFactory.getLogger(OnlineServiceImpl.class);
    private final Map<String, String> ipMacs = new HashMap<String, String>();
    private final Map<String, Online> macs = new HashMap<String, Online>();
    @Autowired
    private FacadeFactory facadeFactory;
    private final int timeout = 300000;
    @Autowired
    private MessageService messageService;
    private LinkedBlockingQueue<String> ipQueue;
    @Autowired
    private SystemPreferencesService systemPreferencesService;
    private static final Long INIT_TIME_ZERO = 0L;

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.service.OnlineService#addData(com.topvision.ems.facade.domain.TopologyResult)
     */
    @Override
    public void addData(TopologyResult result) {
        if (result.getIpNetToMediaMap() != null && !result.getIpNetToMediaMap().isEmpty()) {
            for (String ip : result.getIpNetToMediaMap().keySet()) {
                String mac = result.getIpNetToMediaMap().get(ip);
                for (Iterator<String> itr = ipMacs.keySet().iterator(); itr.hasNext();) {
                    String s1 = itr.next();
                    String s2 = ipMacs.get(s1);
                    if (s2.equals(mac)) {
                        itr.remove();
                    }
                }
                ipMacs.put(ip, mac);
                getByMac(mac, ip);
            }
        }

        if (result.getMac() != null && !result.getMac().isEmpty()) {
            Online o = getByMac(result.getMac(), result.getIp());
            o.setOnline(true);
            o.setName(result.getHostName());
            o.setUpdateTime(result.getDiscoveryTime());
        }

        if (result.getFdbTableMap() != null && !result.getFdbTableMap().isEmpty()) {
            for (String mac : result.getFdbTableMap().keySet()) {
                String ip = null;
                if (ipMacs.containsValue(mac)) {
                    for (String tmp : ipMacs.keySet()) {
                        if (ipMacs.get(tmp).equals(mac)) {
                            ip = tmp;
                            break;
                        }
                    }
                }
                Online o = getByMac(mac, ip);
                o.setOnline(true);
                o.setUpdateTime(result.getDiscoveryTime());
            }
        }

        if (getLogger().isDebugEnabled()) {
            getLogger().debug("addData.ipMacs:" + ipMacs);
            getLogger().debug("addData.macs:" + macs);
        }
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.message.event.EntityListener#attributeChanged(long,
     *      java.lang.String[], java.lang.String[])
     */
    @Override
    public void attributeChanged(long entityId, String[] attrNames, String[] attrValues) {
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.framework.service.BaseService#destroy()
     */
    @Override
    public void destroy() {
        super.destroy();
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.message.event.EntityListener#entityAdded(com.topvision.ems.message.event.EntityEvent)
     */
    @Override
    public void entityAdded(EntityEvent event) {
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.message.event.EntityListener#entityDiscovered(com.topvision.ems.message
     *      .event.EntityEvent)
     */
    @Override
    public void entityDiscovered(EntityEvent event) {
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.message.event.EntityListener#entityChanged(com.topvision.ems.message.event.EntityEvent)
     */
    @Override
    public void entityChanged(EntityEvent event) {
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.message.event.EntityListener#entityRemoved(com.topvision.ems.message.event.EntityEvent)
     */
    @Override
    public void entityRemoved(EntityEvent event) {
    }

    private Online getByIp(String ip) {
        if (ipMacs.containsKey(ip)) {
            return getByMac(ipMacs.get(ip), ip);
        } else {
            return getByMac(ip, ip);
        }
    }

    private Online getByMac(String mac, String ip) {
        Online o = macs.get(mac);
        if (o == null) {
            o = new Online();
            o.setMac(mac);
            o.setUpdateTime(INIT_TIME_ZERO);
            macs.put(mac, o);
        }
        if (ip != null) {
            if (macs.containsKey(ip)) {
                macs.remove(ip);
            }
            o.setIp(ip);
        }
        return o;
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.service.OnlineService#getIpByMac(java.lang.String)
     */
    @Override
    public String getIpByMac(String mac) {
        if (ipMacs.containsValue(mac)) {
            for (String ip : ipMacs.keySet()) {
                if (ipMacs.get(ip).equals(mac)) {
                    return ip;
                }
            }
        }

        return getByMac(mac, null).getIp();
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.service.OnlineService#getMacByIp(java.lang.String)
     */
    @Override
    public String getMacByIp(String ip) {
        if (ipMacs.containsKey(ip)) {
            return ipMacs.get(ip);
        }
        return getByIp(ip).getMac();
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.service.OnlineService#getNameByIp(java.lang.String)
     */
    @Override
    public String getNameByIp(String ip) {
        if (ip == null) {
            return null;
        }
        String name = getByIp(ip).getName();
        if (StringUtil.isEmpty(name)) {
            ipQueue.offer(ip);
            return null;
        } else {
            return name;
        }
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.framework.service.BaseService#initialize()
     */
    @Override
    @PostConstruct
    public void initialize() {
        super.initialize();
        messageService.addListener(PortListener.class, this);
        messageService.addListener(EntityListener.class, this);
        ipQueue = new LinkedBlockingQueue<String>(500);
        Executors.newSingleThreadExecutor().submit(new Runnable() {
            @Override
            public void run() {
                Thread.currentThread().setName("OnlineService.checkThread");
                while (ipQueue != null) {
                    try {
                        String ip = ipQueue.take();
                        String name = facadeFactory.getFacade(ip, ProbeFacade.class).getHostname(ip);
                        if (name != null && !name.isEmpty()) {
                            Online o = getByIp(ip);
                            o.setIp(ip);
                            o.setName(name);
                        }
                    } catch (InterruptedException ex) {
                        try {
                            Thread.sleep(60000);
                        } catch (InterruptedException e) {
                            logger.debug("OnlineServiceImpl initialize error:", e);
                        }
                    }
                }
            }
        });
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.service.OnlineService#isOnlineByIp(java.lang.String)
     */
    @Override
    public Boolean isOnlineByIp(String ip) {
        Online o = getByIp(ip);
        if (isTimeout(o)) {
            ping(ip);
        }
        return o.isOnline();
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.service.OnlineService#isOnlineByMac(java.lang.String)
     */
    @Override
    public Boolean isOnlineByMac(String mac) {
        Online o = getByMac(mac, null);
        if (isTimeout(o)) {
            ping(o.getIp());
        }
        return o.isOnline();
    }

    private boolean isTimeout(Online online) {
        return System.currentTimeMillis() - online.getUpdateTime() > timeout;
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.message.event.EntityListener#managerChanged(com.topvision.ems.message.event.EntityEvent)
     */
    @Override
    public void managerChanged(EntityEvent event) {
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.service.OnlineService#ping(java.lang.String)
     */
    @Override
    public Integer ping(String ip) throws NetworkException {
        if (ip == null) {
            return -1;
        }
        Online o = getByIp(ip);
        try {
            Properties config = systemPreferencesService.getModulePreferences("Ping");
            int timeout = Integer.parseInt(config.getProperty("Ping.timeout", "40000"));
            int count = Integer.parseInt(config.getProperty("Ping.count", "1"));
            int retry = Integer.parseInt(config.getProperty("Ping.retry", "1"));
            o.setResponse(facadeFactory.getFacade(ip, ProbeFacade.class).ping(ip, timeout, count, retry));
        } catch (DeviceNotExistException e) {
            logger.error("device not exist:", e);
            throw new NetworkException("device not exist:", e);
        } catch (Exception e) {
            logger.error("ping:", e);
            throw new NetworkException("ping:", e);
        }

        if (o.getResponse() != null && o.getResponse() >= 0) {
            o.setOnline(true);
            o.setUpdateTime(System.currentTimeMillis());
            return o.getResponse();
        } else {
            return -1;
        }
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.service.OnlineService#setDownByIp(java.lang.String)
     */
    @Override
    public void setDownByIp(String ip) {
        Online o = getByIp(ip);
        o.setOnline(false);
        o.setUpdateTime(System.currentTimeMillis());
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.service.OnlineService#setDownByMac(java.lang.String)
     */
    @Override
    public void setDownByMac(String mac) {
        Online o = getByMac(mac, null);
        o.setOnline(false);
        o.setUpdateTime(System.currentTimeMillis());
    }

    /**
     * @param messageService
     *            the messageService to set
     */
    public void setMessageService(MessageService messageService) {
        this.messageService = messageService;
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.message.event.PortListener#stateChanged(com.topvision.ems.message.event.PortEvent)
     */
    @Override
    public void stateChanged(PortEvent event) {
    }

    /**
     * @param facadeFactory
     *            the facadeFactory to set
     */
    public void setFacadeFactory(FacadeFactory facadeFactory) {
        this.facadeFactory = facadeFactory;
    }

    @Override
    public void entityReplaced(EntityEvent event) {
    }
}
