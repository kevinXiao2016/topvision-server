/**
 * 
 */
package com.topvision.framework.remote.rmi;

import java.rmi.AccessException;
import java.rmi.NoSuchObjectException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.topvision.framework.service.ServiceLocator;

/**
 * 服务导出者, 用与邦定一个RMI服务, 并暴露所有其它非Remote服务借口给远程客户端.
 * 
 * @author niejun
 */
public final class RegistryServiceExporter {
    private Logger logger = LoggerFactory.getLogger(RegistryServiceExporter.class);
    private Registry registry = null;
    private int registryPort = 12121;
    private String serviceName = "ems";
    // RMI服务
    private RegisterService registryService = null;
    // 所有其他非Remote服务的定位器
    private ServiceLocator serviceLocator = null;

    /**
     * 
     * @throws Exception
     */
    public void initialize() {
        try {
            registry = LocateRegistry.createRegistry(registryPort);
            registryService = new RegisterServiceImpl(serviceLocator);
            registry.rebind(serviceName, registryService);
            logger.info("Bind RMI " + serviceName + " service successfully!");
        } catch (AccessException e) {
            logger.error("", e);
        } catch (RemoteException e) {
            logger.error("Bind RMI " + serviceName + " service unsuccessfully!", e);
        }
    }

    /**
     * 
     * @throws Exception
     */
    public void destroy() {
        try {
            registry.unbind(serviceName);
            if (registryService != null) {
                UnicastRemoteObject.unexportObject(registryService, true);
            }
            UnicastRemoteObject.unexportObject(registry, true);
            if (logger.isInfoEnabled()) {
                logger.info("Unbind RMI " + serviceName + " service successfully!");
            }
        } catch (AccessException e) {
            logger.error("", e);
        } catch (NoSuchObjectException e) {
            logger.error("", e);
        } catch (RemoteException e) {
            logger.error("Unbind RMI " + serviceName + " service unsuccessfully!", e);
        } catch (NotBoundException e) {
            logger.error("Unbind RMI " + serviceName + " service unsuccessfully!", e);
        }
    }

    /**
     * @return the registry
     */
    public Registry getRegistry() {
        return registry;
    }

    /**
     * @param registry
     *            the registry to set
     */
    public void setRegistry(Registry registry) {
        this.registry = registry;
    }

    /**
     * @return the registryPort
     */
    public int getRegistryPort() {
        return registryPort;
    }

    /**
     * @param registryPort
     *            the registryPort to set
     */
    public void setRegistryPort(int registryPort) {
        this.registryPort = registryPort;
    }

    /**
     * @return the serviceName
     */
    public String getServiceName() {
        return serviceName;
    }

    /**
     * @param serviceName
     *            the serviceName to set
     */
    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    /**
     * @return the registryService
     */
    public RegisterService getRegistryService() {
        return registryService;
    }

    /**
     * @param registryService
     *            the registryService to set
     */
    public void setRegistryService(RegisterService registryService) {
        this.registryService = registryService;
    }

    /**
     * @return the serviceLocator
     */
    public ServiceLocator getServiceLocator() {
        return serviceLocator;
    }

    /**
     * @param serviceLocator
     *            the serviceLocator to set
     */
    public void setServiceLocator(ServiceLocator serviceLocator) {
        this.serviceLocator = serviceLocator;
    }

}
