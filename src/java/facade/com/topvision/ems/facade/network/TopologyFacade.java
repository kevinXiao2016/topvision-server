package com.topvision.ems.facade.network;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import com.topvision.ems.facade.Facade;
import com.topvision.ems.facade.domain.TopologyParam;
import com.topvision.ems.facade.domain.TopologyResult;
import com.topvision.exception.service.TopologyServiceException;
import com.topvision.framework.annotation.EngineFacade;
import com.topvision.framework.exception.engine.SnmpException;
import com.topvision.framework.snmp.SnmpParam;

@EngineFacade(serviceName = "TopologyFacade", beanName = "topologyFacade")
public interface TopologyFacade extends Facade {
    // TODO 添加注释
    /**
     * 
     * @param tr
     * @param snmpParam
     * @throws SnmpException
     */
    void addTopology(TopologyResult tr, SnmpParam snmpParam) throws SnmpException;

    /**
     * 
     * @param tr
     * @param param
     * @throws SnmpException
     */
    void addTopology(TopologyResult tr, TopologyParam param) throws SnmpException;

    // TODO 添加注释
    /**
     * Map<String,String>=Map<ip,mac>
     * 
     * @param ips
     * @param timeout
     * @param retries
     * @return map<ip,mac> of scan
     * @throws Exception
     */
    Map<String, String> scan(List<String> ips, Integer timeout, Integer retries);

    // TODO 添加注释
    /**
     * Map<String,String>=Map<ip,mac>
     * 
     * @param targets
     * @param timeout
     * @param retries
     * @return map<ip,mac> of scan
     * @throws Exception
     */
    Map<String, String> scan(String targets, Integer timeout, Integer retries);

    /**
     * 通过SNMP参数进行网络拓扑.
     * 
     * @param tr
     * @param snmpParam
     * @return
     * @throws TopologyServiceException
     * @throws ExecutionException
     * @throws InterruptedException
     * @throws SnmpException
     */
    TopologyResult topology(TopologyResult tr, SnmpParam snmpParam) throws TopologyServiceException, SnmpException,
            InterruptedException, ExecutionException;

    /**
     * 通过拓扑参数进行网络拓扑.
     * 
     * @param tr
     * @param param
     * @return
     * @throws TopologyServiceException
     * @throws ExecutionException
     * @throws InterruptedException
     * @throws SnmpException
     */
    TopologyResult topology(TopologyResult tr, TopologyParam param) throws TopologyServiceException, SnmpException,
            InterruptedException, ExecutionException;
}
