/**
 * 
 */
package com.topvision.framework.remote.rmi;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Map;

import com.topvision.framework.service.ServiceLocator;

/**
 * @author niejun
 * 
 */
public class RegisterServiceImpl extends UnicastRemoteObject implements RegisterService {
    private static final long serialVersionUID = -1192503088300133886L;
    private ServiceLocator serviceLocator = null;

    public RegisterServiceImpl() throws RemoteException {
        super();
    }

    /**
     * 
     * @param serviceLocator
     * @throws RemoteException
     */
    public RegisterServiceImpl(ServiceLocator serviceLocator) throws RemoteException {
        super();
        setServiceLocator(serviceLocator);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.framework.remote.rmi.RegisterService#buildServiceExecutor(com.topvision.framework
     * .remote.rmi.ClientExecutor)
     */
    @Override
    public ServiceExecutor buildServiceExecutor(ClientExecutor executor) throws RemoteException {
        return new ServiceExecutorImpl(executor, serviceLocator);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.framework.remote.rmi.RegisterService#getServiceInterfaces()
     */
    @Override
    public Map<String, String> getServiceInterfaces() throws RemoteException {
        return serviceLocator.getServiceInterfaces();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.framework.remote.rmi.RegisterService#ping()
     */
    @Override
    public void ping() throws RemoteException {
    }

    /**
     * 
     * @param serviceLocator
     */
    public void setServiceLocator(ServiceLocator serviceLocator) {
        this.serviceLocator = serviceLocator;
    }

    /**
     * 
     * @return
     */
    public ServiceLocator getServiceLocator() {
        return serviceLocator;
    }
}
