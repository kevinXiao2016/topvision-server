/***********************************************************************
 * CmWebPortMap.java,v1.0 17-4-25 下午8:44 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.webproxy.domain;

import com.topvision.framework.dao.mybatis.AliasesSuperType;
import org.apache.ibatis.type.Alias;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author jay
 * @created 17-4-25 下午8:44
 */
@Alias("cmWebPortMap")
public class CmWebPortMap implements Serializable, AliasesSuperType {
    private static final long serialVersionUID = 8571259721660333279L;
    private Long cmId;
    //网管服务器Ip
    private String natIp;
    //网管侧代理端口
    private Integer forwoard;
    //需要映射的CM IP
    private String ip;
    //需要映射的CM 端口
    private Integer port;
    //启动代理的CC IP
    private String proxyIp;
    //启动代理的CC 端口
    private Integer proxyPort;
    //状态
    private Boolean status;
    private List<Long> heartbeatIds = Collections.synchronizedList(new ArrayList<>());

    public CmWebPortMap() {
    }

    public CmWebPortMap(Integer forwoard, String natIp, String ip, Integer port, String proxyIp, Integer proxyPort) {
        this.forwoard = forwoard;
        this.natIp = natIp;
        this.ip = ip;
        this.port = port;
        this.proxyIp = proxyIp;
        this.proxyPort = proxyPort;
    }

    public CmWebPortMap(Integer forwoard, String natIp, String ip, Integer port) {
        this(forwoard,natIp,ip,port,null,null);
    }

    public Long getCmId() {
        return cmId;
    }

    public void setCmId(Long cmId) {
        this.cmId = cmId;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public Integer getForwoard() {
        return forwoard;
    }

    public void setForwoard(Integer forwoard) {
        this.forwoard = forwoard;
    }

    public String getProxyIp() {
        return proxyIp;
    }

    public void setProxyIp(String proxyIp) {
        this.proxyIp = proxyIp;
    }

    public Integer getProxyPort() {
        return proxyPort;
    }

    public void setProxyPort(Integer proxyPort) {
        this.proxyPort = proxyPort;
    }

    public String getNatIp() {
        return natIp;
    }

    public void setNatIp(String natIp) {
        this.natIp = natIp;
    }

    public String getProxyKey() {
        return proxyIp + "_" + proxyPort;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public List<Long> getHeartbeatIds() {
        return heartbeatIds;
    }

    public Integer heartbeatNum() {
        return heartbeatIds.size();
    }

    public void addHeartbeatId(Long heartbeatId) {
        heartbeatIds.add(heartbeatId);
    }

    public void releaseHeartbeatId(Long heartbeatId) {
        heartbeatIds.remove(heartbeatId);
    }
    @Override
    public String toString() {
        return "CmWebPortMap{" + "<br>" +
                "&nbsp;&nbsp;&nbsp;&nbsp;natIp='" + natIp + '\'' + "<br>" +
                ",&nbsp;&nbsp;&nbsp;&nbsp;forwoard=" + forwoard + "<br>" +
                ",&nbsp;&nbsp;&nbsp;&nbsp;ip='" + ip + '\'' + "<br>" +
                ",&nbsp;&nbsp;&nbsp;&nbsp;port=" + port + "<br>" +
                ",&nbsp;&nbsp;&nbsp;&nbsp;proxyIp='" + proxyIp + '\'' + "<br>" +
                ",&nbsp;&nbsp;&nbsp;&nbsp;proxyPort=" + proxyPort + "<br>" +
                ",&nbsp;&nbsp;&nbsp;&nbsp;ProxyKey=" + getProxyKey() + "<br>" +
                ",&nbsp;&nbsp;&nbsp;&nbsp;status=" + status + "<br>" +
                ",&nbsp;&nbsp;&nbsp;&nbsp;heartbeatIds=" + heartbeatIds + "<br>" +
                '}';
    }
}
