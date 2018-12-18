package com.topvision.ems.network.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.facade.domain.Attribute;
import com.topvision.ems.facade.domain.TopologyParam;
import com.topvision.ems.network.dao.HostServiceDao;
import com.topvision.ems.network.dao.TopologyParamDao;
import com.topvision.ems.network.domain.Services;
import com.topvision.ems.network.service.TopologyParamService;
import com.topvision.exception.service.TopologyServiceException;
import com.topvision.framework.EnvironmentConstants;
import com.topvision.framework.service.BaseService;

@Service("topologyParamService")
public class TopologyParamServiceImpl extends BaseService implements TopologyParamService {
    @Autowired
    private HostServiceDao hostServiceDao;
    @Autowired
    private TopologyParamDao paramDao;

    /**
     * @return the paramDao
     */
    public TopologyParamDao getParamDao() {
        return paramDao;
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.service.TopologyParamService#insertParam(com.topvision.ems.facade.domain.TopologyParam)
     */
    @Override
    public void insertParam(TopologyParam topoParam) {
        List<Attribute> list = topoParam.toList();
        list.add(new Attribute("topology", "modifyTime", new Date().toString()));
        paramDao.insertEntity(list);
    }

    /**
     * @param hostServiceDao
     *            the hostServiceDao to set
     */
    public void setHostServiceDao(HostServiceDao hostServiceDao) {
        this.hostServiceDao = hostServiceDao;
    }

    /**
     * @param paramDao
     *            the paramDao to set
     */
    public void setParamDao(TopologyParamDao paramDao) {
        this.paramDao = paramDao;
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.service.TopologyParamService#txGetTopologyParam()
     */
    @Override
    public TopologyParam txGetTopologyParam() throws TopologyServiceException {
        TopologyParam param = new TopologyParam();
        List<Attribute> attrs = paramDao.selectByMap(null);
        if (attrs == null || attrs.isEmpty()) {
            String ip = EnvironmentConstants.getHostAddress();
            param.addSegment(new TopologyParam.Segment(ip, "255.255.255.0"));
            param.addCommunity("public");
            param.addCommunity("private");
            param.setTarget(ip + "/24");

            insertParam(param);
        } else {
            Properties props = new Properties();
            for (Attribute attr : attrs) {
                props.put(attr.getName(), attr.getValue() == null ? "" : attr.getValue());
            }
            param.parseMap(props);
        }

        return param;
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.service.TopologyParamService#updateParam(com.topvision.ems.facade.domain.TopologyParam)
     */
    @Override
    public void updateParam(TopologyParam topoParam) {
        paramDao.deleteByPrimaryKey("");
        insertParam(topoParam);
        List<Integer> services = topoParam.getServicePorts();
        if (services != null) {
            List<Services> arr = new ArrayList<Services>();
            int size = services.size();
            for (int i = 0; i < size; i++) {
                Services s = new Services();
                s.setPort(services.get(i));
                s.setScaned(true);
                arr.add(s);
            }
            hostServiceDao.setServicesScanned(arr);
        }
    }
}
