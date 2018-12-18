/***********************************************************************
 * $Id: ZooKeeperServiceImpl.java,v1.0 2015年4月8日 上午11:29:20 $
 * 
 * @author: Victor
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.platform.service.impl;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.apache.zookeeper.server.ServerConfig;
import org.apache.zookeeper.server.ZooKeeperServerMain;
import org.apache.zookeeper.server.quorum.QuorumPeerConfig;
import org.apache.zookeeper.server.quorum.QuorumPeerConfig.ConfigException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ProtocolConfig;
import com.alibaba.dubbo.config.RegistryConfig;
import com.topvision.framework.EnvironmentConstants;
import com.topvision.framework.service.BaseService;
import com.topvision.platform.service.ZooKeeperService;

/**
 * @author Victor
 * @created @2015年4月8日-上午11:29:20
 *
 */
@Service("zooKeeperService")
public class ZooKeeperServiceImpl extends BaseService implements ZooKeeperService {
    private ZooKeeperServerMain zkServer;
    @Value("${zooKeeper.dataDir:./zookeeper/data}")
    private String dataDir;
    @Value("${zooKeeper.dataLogDir:./zookeeper/log}")
    private String dataLogDir;
    @Value("${zookeeper.port:3007}")
    private int clientPort;
    @Value("${rmi.port}")
    private int servicePort;
    @Value("${dubbo.protocol:rmi}")
    private String dubboProtocol;
    private ApplicationConfig application;
    private RegistryConfig registry;
    private ProtocolConfig protocol;

    @PostConstruct
    @Override
    public void initialize() {
        new Thread() {
            public void run() {
                try {
                    Properties zkProp = new Properties();
                    new File(dataDir).mkdirs();
                    zkProp.put("dataDir", dataDir);
                    new File(dataLogDir).mkdirs();
                    zkProp.put("dataLogDir", dataLogDir);
                    zkProp.put("clientPort", clientPort);
                    zkProp.put("tickTime", "2000");
                    zkProp.put("initLimit", "10");
                    zkProp.put("syncLimit", "5");
                    zkProp.put("quorumListenOnAllIPs", "true");
                    // zkProp.put("authProvider.1", TopvisionProvider.class.getName());
                    QuorumPeerConfig quorumConfig = new QuorumPeerConfig();
                    quorumConfig.parseProperties(zkProp);
                    ServerConfig config = new ServerConfig();
                    config.readFrom(quorumConfig);

                    zkServer = new ZooKeeperServerMain();
                    zkServer.runFromConfig(config);
                } catch (IOException e) {
                    logger.error("ZooKeeperService.initialize", e);
                } catch (ConfigException e) {
                    logger.error("ZooKeeperService.initialize", e);
                }
            }
        }.start();
        try {
            URL url = new URL("zookeeper", EnvironmentConstants.getHostAddress(), clientPort);

            // 连接注册中心配置
            Map<String, String> parameters = new HashMap<String, String>();
            parameters.put(Constants.FILE_KEY, "tmp/dubbo-registry-server-" + url.getHost() + ".cache");
            registry = new RegistryConfig();
            registry.setAddress(url.toString());
            registry.setUsername("ems");
            registry.setPassword("nm3000");
            registry.setCheck(false);
            registry.setParameters(parameters);

            // 当前应用配置
            application = new ApplicationConfig();
            application.setName("Server");
            application.setRegistry(registry);

            // 服务提供者协议配置
            protocol = new ProtocolConfig();
            protocol.setName(dubboProtocol);
            protocol.setHost(url.getHost());
            protocol.setPort(servicePort);
            protocol.setThreads(200);
        } catch (Throwable e1) {
            logger.error("ZooKeeperServiceImpl.initialize", e1);
        }
    }

    @PreDestroy
    @Override
    public void destroy() {
    }

    @Override
    public void start() {
    }

    @Override
    public void stop() {
    }

    public ApplicationConfig getApplication() {
        return application;
    }

    public RegistryConfig getRegistry() {
        return registry;
    }

    public ProtocolConfig getProtocol() {
        return protocol;
    }
}
