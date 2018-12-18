package com.topvision.ems.network.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.facade.SnmpFacade;
import com.topvision.ems.facade.domain.SnmpData;
import com.topvision.ems.network.dao.EntityDao;
import com.topvision.ems.network.dao.PortDao;
import com.topvision.ems.network.domain.Port;
import com.topvision.ems.network.message.PortEvent;
import com.topvision.ems.network.message.PortListener;
import com.topvision.ems.network.service.PortService;
import com.topvision.exception.service.PortServiceException;
import com.topvision.framework.exception.engine.SnmpException;
import com.topvision.framework.service.BaseService;
import com.topvision.platform.dao.InterceptorLogDao;
import com.topvision.platform.domain.InterceptorLog;
import com.topvision.platform.facade.FacadeFactory;
import com.topvision.platform.message.service.MessageService;

@Service("portService")
public class PortServiceImpl extends BaseService implements PortService {
    public final static String ifAdminStatus = "1.3.6.1.2.1.2.2.1.7.";
    public final static String ifOperStatus = "1.3.6.1.2.1.2.2.1.8.";
    public final static String UP = "1";
    public final static String DOWN = "2";
    @Autowired
    private PortDao portDao;
    @Autowired
    private EntityDao entityDao;
    private InterceptorLogDao interceptorLogDao;
    @Autowired
    private MessageService messageService;
    @Autowired
    private FacadeFactory facadeFactory;

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.service.PortService#getConnectedPortByIp(java.lang.String)
     */
    @Override
    public Port getConnectedPortByIp(String ip) {
        return null;
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.service.PortService#getPort(Long)
     */
    @Override
    public Port getPort(Long portId) {
        return portDao.selectByPrimaryKey(portId);
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.service.PortService#getPort(Long, Integer)
     */
    @Override
    public Port getPort(Long entityId, Long ifIndex) {
        return portDao.getPortByIfIndex(entityId, ifIndex);
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.service.PortService#getPortByEntity(Long)
     */
    @Override
    public List<Port> getPortByEntity(Long entityId) {
        return portDao.getPortsByEntityId(entityId);
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.service.PortService#getPortCoord(Long)
     */
    @Override
    public List<Port> getPortCoord(Long entityId) {
        return portDao.getPortCoord(entityId);
    }

    /**
     * 
     * @return engine's snmpFacade
     */
    private SnmpFacade getSnmpFacade(String ip) {
        return facadeFactory.getFacade(ip, SnmpFacade.class);
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.service.PortService#savePortCoord(java.util.List)
     */
    @Override
    public void savePortCoord(List<Port> ports) {
        portDao.savePortCoord(ports);
    }

    /**
     * @param interceptorLogDao
     *            the interceptorLogDao to set
     */
    public final void setInterceptorLogDao(InterceptorLogDao interceptorLogDao) {
        this.interceptorLogDao = interceptorLogDao;
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.service.PortService#txClosePort(com.topvision.ems.network.domain.Port,
     *      com.topvision.platform.domain.InterceptorLog)
     */
    @Override
    public Boolean txClosePort(Port port, InterceptorLog log) throws PortServiceException {
        if (port.getEntityId() == 0 || port.getIfIndex() == 0) {
            port = getPort(port.getPortId());
        }
        SnmpData data = new SnmpData();
        data.setSnmpParam(entityDao.getSnmpParamByEntityId(port.getEntityId()));
        data.addData(ifAdminStatus + port.getIfIndex(), DOWN);
        data.addData(ifOperStatus + port.getIfIndex(), null);
        try {
            data = getSnmpFacade(data.getSnmpParam().getIpAddress()).set(data);
        } catch (SnmpException e) {
            getLogger().debug(e.getMessage(), e);
            if (log != null) {
                log.setResult(false);
                log.setReason(e.getMessage());
                interceptorLogDao.insertEntity(log);
            }
            throw new PortServiceException("close port error:", e);
        }
        port.setIfAdminStatus(Byte.parseByte(data.getData(ifAdminStatus + port.getIfIndex())));
        port.setIfOperStatus((byte) 2);
        portDao.updatePortStatus(port);
        if (log != null) {
            log.setResult(true);
            log.setReason("Success");
            interceptorLogDao.insertEntity(log);
        }
        PortEvent event = new PortEvent(port);
        event.setPort(port);
        event.setListener(PortListener.class);
        event.setActionName("stateChanged");
        messageService.fireMessage(event);

        return true;
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.service.PortService#txOpenPort(com.topvision.ems.network.domain.Port,
     *      com.topvision.platform.domain.InterceptorLog)
     */
    @Override
    public Boolean txOpenPort(Port port, InterceptorLog log) throws PortServiceException {
        if (port.getEntityId() == 0 || port.getIfIndex() == 0) {
            port = getPort(port.getPortId());
        }
        SnmpData data = new SnmpData();
        data.setSnmpParam(entityDao.getSnmpParamByEntityId(port.getEntityId()));
        data.addData(ifAdminStatus + port.getIfIndex(), UP);
        data.addData(ifOperStatus + port.getIfIndex(), null);
        try {
            data = getSnmpFacade(data.getSnmpParam().getIpAddress()).set(data);
        } catch (SnmpException e) {
            getLogger().debug(e.getMessage(), e);
            if (log != null) {
                log.setResult(false);
                log.setReason(e.getMessage());
                interceptorLogDao.insertEntity(log);
            }
            throw new PortServiceException("open port error:", e);
        }
        port.setIfAdminStatus(Byte.parseByte(data.getData(ifAdminStatus + port.getIfIndex())));
        port.setIfOperStatus(Byte.parseByte(data.getData(ifOperStatus + port.getIfIndex())));
        portDao.updatePortStatus(port);
        if (log != null) {
            log.setResult(true);
            log.setReason("Success");
            interceptorLogDao.insertEntity(log);
        }
        PortEvent event = new PortEvent(port);
        event.setPort(port);
        event.setListener(PortListener.class);
        event.setActionName("stateChanged");
        messageService.fireMessage(event);

        return true;
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.service.PortService#updatePort(com.topvision.ems.network.domain.Port)
     */
    @Override
    public void updatePort(Port port) {
        portDao.updatePortOutline(port);
    }

    @Override
    public Map<String, Port> getPortCaches() {
        return portDao.getPortCaches();
    }

}
