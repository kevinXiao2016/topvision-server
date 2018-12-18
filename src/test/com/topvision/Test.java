package com.topvision;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactory;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ProtocolConfig;
import com.alibaba.dubbo.config.RegistryConfig;
import com.topvision.ems.facade.CheckFacade;
import com.topvision.framework.annotation.EngineFacade;
import com.topvision.framework.exception.engine.SnmpException;
import com.topvision.framework.exception.engine.SnmpSetException;

//@TestBean
public class Test {
    protected Logger logger = LoggerFactory.getLogger(getClass());
    private int servicePort = 2222;
    private int timeout = 300000;
    private String dubboProtocol = "rmi";
    private BeanFactory beanFactory;
    private ApplicationConfig application;
    private RegistryConfig registry;
    private ProtocolConfig protocol;

    public static void main(String[] args) {
        try {
            Enumeration<NetworkInterface> nis = NetworkInterface.getNetworkInterfaces();
            InetAddress ip = null;
            while (nis.hasMoreElements()) {
                NetworkInterface netInterface = nis.nextElement();
                Enumeration<InetAddress> addresses = netInterface.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    ip = addresses.nextElement();
                    System.out.println("IP = " + ip);
                    System.out.println("IP = " + ip.isLoopbackAddress());
                    if (ip != null && ip instanceof Inet4Address && !ip.isLoopbackAddress()) {
                        System.out.println("本机的IP = " + ip.getHostAddress());
                    }
                }
            }
        } catch (Exception e) {
        }
    }

    @PostConstruct
    public void init() {
        try {
            application = new ApplicationConfig();
            application.setName("Engine");

            // 服务提供者协议配置
            protocol = new ProtocolConfig();
            protocol.setName(dubboProtocol);
            protocol.setPort(servicePort);
            protocol.setThreads(200);
            EngineFacade engineFacade = CheckFacade.class.getAnnotation(EngineFacade.class);
            // 注意：ServiceConfig为重对象，内部封装了与注册中心的连接，以及开启服务端口
            // 服务提供者暴露服务配置
            RegistryConfig r = new RegistryConfig();
            r.setAddress("N/A");
            r.setUsername("ems");
            r.setPassword("nm3000");
            r.setCheck(false);
            Map<String, String> parameters = new HashMap<String, String>();
            parameters.put(Constants.FILE_KEY, "tmp/dubbo-registry-engine-" + servicePort + ".cache");
            r.setParameters(parameters);

            logger.info("CheckFacade is ready.");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

}
