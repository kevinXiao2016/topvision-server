/**
 * 
 */
package com.topvision.ems.network.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.facade.PingFacade;
import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.facade.domain.HostService;
import com.topvision.ems.fault.domain.Alert;
import com.topvision.ems.fault.service.AlertService;
import com.topvision.ems.network.dao.HostServiceDao;
import com.topvision.ems.network.dao.NetworkDao;
import com.topvision.ems.network.domain.EntityAvailableStat;
import com.topvision.ems.network.domain.EntitySnap;
import com.topvision.ems.network.domain.PortPerfEx;
import com.topvision.ems.network.domain.ScanOptions;
import com.topvision.ems.network.domain.ServerTypeStat;
import com.topvision.ems.network.domain.Services;
import com.topvision.ems.network.domain.StateStat;
import com.topvision.ems.network.service.NetworkService;
import com.topvision.ems.template.service.EntityTypeService;
import com.topvision.framework.common.DateUtils;
import com.topvision.framework.service.BaseService;
import com.topvision.platform.domain.UserContext;
import com.topvision.platform.facade.FacadeFactory;

/**
 * @author niejun
 * 
 */
@Service("networkService")
public class NetworkServiceImpl extends BaseService implements NetworkService {
    @Autowired
    private HostServiceDao hostServiceDao;
    @Autowired
    private NetworkDao networkDao;
    @Autowired
    private FacadeFactory facadeFactory;
    @Autowired
    private EntityTypeService entityTypeService;
    @Autowired
    private AlertService alertService;
    private static final String ICMP_TYPEID = "-301";

    // private Map<Long, AgentAddressBook> agentAddrMapping = new HashMap<Long, AgentAddressBook>();
    // private Map<String, Long> agentMacMapping = new HashMap<String, Long>();
    // private Map<String, LegalAddrBook> legalAddrMapping = new HashMap<String, LegalAddrBook>();

    /*
     * (non-Javadoc)
     * 
     * @see com.topoview.nms.server.network.service.NetworkService#
     * scanEntity(com.topoview.nms.server.entity.domain.Entity,
     * com.topoview.nms.server.network.domain.ScanOptions)
     */
    @Override
    public void scanEntity(Entity entity, ScanOptions options) throws Exception {
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topoview.nms.server.network.service.NetworkService# scanEntity(java.util.List,
     * com.topoview.nms.server.network.domain.ScanOptions)
     */
    @Override
    public void scanEntity(List<Entity> entities, ScanOptions options) throws Exception {

    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topoview.nms.server.network.service.NetworkService# scanTarget(java.lang.String,
     * com.topoview.nms.server.network.domain.ScanOptions)
     */
    @Override
    public void scanTarget(String target, ScanOptions options) throws Exception {

    }

    @Override
    public int getResponseTimeByEntity(String ip) throws Exception {
        return getPingFacade(ip).ping(ip, 5000, 0);
    }

    @Override
    @PostConstruct
    public void initialize() {
        super.initialize();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topoview.nms.server.network.service.NetworkService# getHostService()
     */
    @Override
    public List<HostService> getHostService() throws Exception {
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topoview.nms.server.network.service.NetworkService#getNetworkServices ()
     */
    @Override
    public List<Services> getNetworkServices() throws Exception {
        return hostServiceDao.getNetworkServices();
    }

    public void setHostServiceDao(HostServiceDao hostServiceDao) {
        this.hostServiceDao = hostServiceDao;
    }

    /**
     * @return the pingService
     */
    public PingFacade getPingFacade(String ip) {
        return facadeFactory.getFacade(ip, PingFacade.class);
    }

    @Override
    public List<EntitySnap> getDeviceDelayTop(Map<String, Object> map) throws Exception {
        UserContext uc = (UserContext) ServletActionContext.getRequest().getSession().getAttribute("UserContext");
        List<EntitySnap> delayList = new ArrayList<EntitySnap>();
        Long entityTypeId = entityTypeService.getEntitywithipType();
        String updateTime = null;
        map.put("entityType", entityTypeId);
        delayList = networkDao.getDeviceDelayList(map);
        for (EntitySnap snap : delayList) {
            updateTime = DateUtils.getTimeDesInObscure(System.currentTimeMillis() - snap.getSnapTime().getTime(), uc
                    .getUser().getLanguage());
            snap.setDelayUpdateTime(updateTime);
        }
        return delayList;
    }

    @Override
    public Integer getDeviceDelayNum(Map<String, Object> map) {
        Long entityTypeId = entityTypeService.getEntitywithipType();
        map.put("entityType", entityTypeId);
        return networkDao.getDeviceDelayNum(map);
    }

    @Override
    public List<EntitySnap> getDeviceDelayOut(Map<String, Object> map) {
        UserContext uc = (UserContext) ServletActionContext.getRequest().getSession().getAttribute("UserContext");
        List<EntitySnap> entitySnaps = new ArrayList<EntitySnap>();
        List<EntitySnap> delayOuts = new ArrayList<EntitySnap>();
        String updateTime = null;
        String delayOutTime = null;
        Long entityTypeId = entityTypeService.getEntitywithipType();
        map.put("entityType", entityTypeId);
        delayOuts = networkDao.getDeviceDelayOutList(map);
        for (EntitySnap snap : delayOuts) {
            Alert alert = alertService.getEntityAlertByType(snap.getEntityId(), ICMP_TYPEID);
            if (alert != null) {
                updateTime = DateUtils.getTimeDesInObscure(System.currentTimeMillis() - alert.getLastTime().getTime(),
                        uc.getUser().getLanguage());
                snap.setDelayUpdateTime(updateTime);
                Long outTime = System.currentTimeMillis() - alert.getFirstTime().getTime();
                delayOutTime = DateUtils.getTimeDesInObscure(outTime, uc.getUser().getLanguage());
                snap.setOutTime(outTime);
                snap.setDelayOutTime(delayOutTime);
                entitySnaps.add(snap);
            }
        }
        Collections.sort(entitySnaps, new Comparator<EntitySnap>() {
            @Override
            public int compare(EntitySnap o1, EntitySnap o2) {
                if (o1.getOutTime() > o2.getOutTime()) {
                    return 1;
                } else if (o1.getOutTime() < o2.getOutTime()) {
                    return -1;
                }
                return 0;
            }
        });
        return entitySnaps;
    }

    @Override
    public Integer getDeviceDelayOutNum(Map<String, Object> map) {
        Long entityTypeId = entityTypeService.getEntitywithipType();
        map.put("entityType", entityTypeId);
        return networkDao.getDeviceDelayOutNum(map);
    }

    @Override
    public List<EntitySnap> getDeviceAttentionList(Map<String, Object> map) {
        return networkDao.getDeviceAttentionList(map);
    }

    @Override
    public Integer getDeviceAttentionCount(Map<String, Object> map) {
        return networkDao.getDeviceAttentionCount(map);
    }

    @Override
    public List<EntitySnap> getNetworkDeviceLoadingTop(Map<String, Object> map) {
        return networkDao.getNetworkDeviceLoadingTop(map);
    }

    @Override
    public List<EntitySnap> getNetworkDeviceLoadingList(Map<String, Object> map) {
        return networkDao.getNetworkDeviceLoadingList(map);
    }

    @Override
    public Integer getNetworkDeviceCount(Map<String, Object> map) {
        return networkDao.getNetworkDeviceCount(map);
    }

    @Override
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public List<PortPerfEx> getPortFlowTop(Map map) throws Exception {
        return networkDao.getPortFlowTop(map);
    }

    @Override
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public List<PortPerfEx> getPortRateTop(Map map) throws Exception {
        return networkDao.getPortRateTop(map);
    }

    public void setNetworkDao(NetworkDao networkDao) {
        this.networkDao = networkDao;
    }

    @Override
    public List<StateStat> statEntityCountByState() throws Exception {
        return networkDao.statEntityCountByState();
    }

    //
    // /**
    // *
    // * (non-Javadoc)
    // *
    // * @see com.topoview.nms.network.service.NetworkService#getAgentAddressBook(java.lang.String)
    // */
    // @Override
    // public AgentAddressBook getAgentAddressBook(String mac) {
    // Long id = agentMacMapping.get(mac);
    // if (id == null) {
    // return null;
    // } else {
    // AgentAddressBook book = agentAddrMapping.get(id);
    // if (book == null || book.isDeleted()) {
    // return null;
    // } else {
    // return book;
    // }
    // }
    // }
    //
    // /**
    // *
    // * (non-Javadoc)
    // *
    // * @see
    // com.topoview.nms.network.service.NetworkService#registerAgentAddressBook(com.topoview.nms.security.domain.AgentAddressBook)
    // */
    // @Override
    // public void registerAgentAddressBook(AgentAddressBook book) {
    // agentAddrMapping.put(book.getAgentId(), book);
    // }
    //
    // /**
    // *
    // * (non-Javadoc)
    // *
    // * @see
    // com.topoview.nms.network.service.NetworkService#registerAgentMacBook(com.topoview.nms.security.domain.AgentMacBook)
    // */
    // @Override
    // public void registerAgentMacBook(AgentMacBook book) {
    // agentMacMapping.put(book.getMac(), book.getAgentId());
    // }
    //
    // /**
    // *
    // * (non-Javadoc)
    // *
    // * @see com.topoview.nms.network.service.NetworkService#unregisterAgentAddressBook(long)
    // */
    // @Override
    // public void unregisterAgentAddressBook(long agentId) {
    // AgentAddressBook book = agentAddrMapping.remove(agentId);
    // if (book != null) {
    // book.setDeleted(true);
    // }
    // }
    //
    // /**
    // *
    // * (non-Javadoc)
    // *
    // * @see com.topoview.nms.network.service.NetworkService#setAgentActiveTime(long,
    // * long)
    // */
    // @Override
    // public boolean setAgentActiveTime(long agentId, long activeTime) {
    // AgentAddressBook book = agentAddrMapping.get(agentId);
    // if (book != null) {
    // book.setActiveTime(activeTime);
    // return true;
    // } else {
    // return false;
    // }
    // }
    //
    // /**
    // *
    // * (non-Javadoc)
    // *
    // * @see com.topoview.nms.network.service.NetworkService#getLegalAddrBook(java.lang.String)
    // */
    // @Override
    // public LegalAddrBook getLegalAddrBook(String mac) {
    // LegalAddrBook book = legalAddrMapping.get(mac);
    // if (book == null || book.isDeleted()) {
    // return null;
    // } else {
    // return book;
    // }
    // }
    //
    // /**
    // *
    // * (non-Javadoc)
    // *
    // * @see
    // com.topoview.nms.network.service.NetworkService#registerLegalAddrBook(com.topoview.nms.security.domain.LegalAddrBook)
    // */
    // @Override
    // public void registerLegalAddrBook(LegalAddrBook book) {
    // legalAddrMapping.put(book.getMac(), book);
    // }

    @Override
    public List<ServerTypeStat> statServerByOs() throws Exception {
        return null;
    }

    //
    // /**
    // *
    // * (non-Javadoc)
    // *
    // * @see
    // com.topoview.nms.network.service.NetworkService#unregisterLegalAddrBook(java.lang.String)
    // */
    // @Override
    // public void unregisterLegalAddrBook(String mac) {
    // LegalAddrBook book = legalAddrMapping.get(mac);
    // if (book != null) {
    // book.setDeleted(true);
    // }
    // }

    @Override
    public List<EntitySnap> getServerLoadingTop(String str) throws Exception {
        return networkDao.getServerLoadingTop(str);
    }

    @Override
    public List<EntityAvailableStat> statServerAvailable() throws Exception {
        return networkDao.statServerAvailable();
    }

    //
    // @Override
    // public void probeService(Entity entity) throws Exception {
    // List<String> services = new ArrayList<String>();
    // services.add("MySQL");
    // services.add("Oracle");
    // Map<String, ProbeService> probes = new HashMap<String, ProbeService>();
    // for (int i = 0; i < services.size(); i++) {
    // MysqlProbeServiceImpl.loadDriver("oracle.jdbc.driver.OracleDriver");
    // ProbeService p = new MysqlProbeServiceImpl();
    // Map<String, String> map = new HashMap<String, String>();
    // map.put("database.type", "Oracle");
    // map.put("database.host", "192.168.1.196");
    // map.put("database.port", "1521");
    // map.put("database.user", "monitor188");
    // map.put("database.passwd", "itc123");
    // ProbeResult result = p.probe(map);
    // System.out.println(result.getKey());
    // }
    // }

    // @Override
    // public void probeService(List<Entity> entities) throws Exception {
    // }

    // public static void main(String[] args) throws Exception {
    // NetworkService n = new NetworkServiceImpl();
    // n.probeService((Entity) null);
    // }

    public FacadeFactory getFacadeFactory() {
        return facadeFactory;
    }

    public void setFacadeFactory(FacadeFactory facadeFactory) {
        this.facadeFactory = facadeFactory;
    }
}
