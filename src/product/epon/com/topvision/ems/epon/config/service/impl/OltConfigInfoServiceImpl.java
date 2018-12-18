/***********************************************************************
 * $Id: OltConfigInfoService.java,v1.0 2013-10-26 上午9:40:22 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.config.service.impl;

import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.epon.config.dao.OltConfigInfoDao;
import com.topvision.ems.epon.config.service.OltConfigInfoService;
import com.topvision.ems.epon.domain.OltVlanInterface;
import com.topvision.ems.epon.domain.Room;
import com.topvision.ems.epon.exception.SetValueConflictException;
import com.topvision.ems.epon.olt.dao.OltDao;
import com.topvision.ems.epon.olt.domain.OltAttribute;
import com.topvision.ems.epon.olt.facade.OltFacade;
import com.topvision.ems.epon.performance.facade.OltPerfFacade;
import com.topvision.ems.epon.vlan.domain.VlanAttribute;
import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.network.dao.EntityAddressDao;
import com.topvision.ems.network.dao.EntityDao;
import com.topvision.ems.network.service.EntityService;
import com.topvision.ems.network.util.XmlOperationUtil;
import com.topvision.framework.service.BaseService;
import com.topvision.framework.snmp.SnmpParam;
import com.topvision.platform.ResourceManager;
import com.topvision.platform.SystemConstants;
import com.topvision.platform.facade.FacadeFactory;

/**
 * @author flack
 * @created @2013-10-26-上午9:40:22
 *
 */
@Service("oltConfigInfoService")
public class OltConfigInfoServiceImpl extends BaseService implements OltConfigInfoService {
    @Autowired
    private OltDao oltDao;
    @Autowired
    private OltConfigInfoDao oltConfigInfoDao;
    @Autowired
    private FacadeFactory facadeFactory;
    @Autowired
    private EntityDao entityDao;
    @Autowired
    private EntityAddressDao entityAddressDao;
    @Autowired
    private EntityService entityService;

    @Override
    public Set<Room> getLocationList() {
        Set<Room> list = new HashSet<Room>();
        StringBuilder path = new StringBuilder();
        path.append(SystemConstants.WEB_INF_REAL_PATH);
        path.append("rooms");
        File rootDir = new File(path.toString());
        File[] files = rootDir.listFiles();
        for (File file : files) {
            if (!file.isDirectory()) {
                String[] parms = (file.toString()).split("\\.");
                if ("xml".equals(parms[parms.length - 1])) {
                    Room room = (Room) XmlOperationUtil.readObject(file);
                    list.add(room);
                }
            }
        }
        return list;
    }

    @Override
    public void modifyInBandConfig(OltAttribute oltAttribute, SnmpParam param) {
        // 4个参数，分别比较，否则一个参数设置失败，全都设置失败
        OltAttribute oltInBandConfig = getOltFacade(param.getIpAddress()).modifyInBandConfig(param, oltAttribute);
        oltConfigInfoDao.updateInBandConfig(oltInBandConfig);
    }

    @Override
    public void modifyOutBandConfig(OltAttribute oltAttribute, SnmpParam param) {
        // 4个参数，分别比较，否则一个参数设置失败，全都设置失败
        OltAttribute oltOutBandConfig = getOltFacade(param.getIpAddress()).modifyOutBandConfig(param, oltAttribute);
        StringBuilder sBuilder = new StringBuilder();
        if (oltOutBandConfig.getOutbandIp() == null && oltOutBandConfig.getOutbandMask() == null
                && oltOutBandConfig.getOutbandIpGateway() == null) {
            throw new SetValueConflictException("Business.connection");
        }
        if (!oltOutBandConfig.getOutbandIp().equals(oltAttribute.getOutbandIp())) {
            sBuilder.append("Business.setOutBandIp");
        }
        if (!oltOutBandConfig.getOutbandMask().equals(oltAttribute.getOutbandMask())) {
            sBuilder.append("Business.setOutbandMask");
        }
        if (!oltOutBandConfig.getOutbandGateway().equals(oltAttribute.getOutbandGateway())) {
            sBuilder.append("Business.setOutbandGateway");
        }
        oltConfigInfoDao.updateOutBandConfig(oltAttribute);
        if (sBuilder.length() > 0) {
            throw new SetValueConflictException(sBuilder.toString());
        }
    }

    @Override
    public void modifyOltSnmpConfig(OltAttribute oltAttribute, SnmpParam param) {
        OltAttribute oltSnmpConfig = getOltFacade(param.getIpAddress()).modifyOltSnmpConfig(param, oltAttribute);
        StringBuilder sBuilder = new StringBuilder();
        if (oltSnmpConfig.getSnmpHostIp() == null && oltSnmpConfig.getHostIpMask() == null
                && oltSnmpConfig.getTopSysSnmpVersion() == null) {
            throw new SetValueConflictException("Business.connection");
        }
        if (!oltSnmpConfig.getTopSysSnmpHostIp().equals(oltAttribute.getTopSysSnmpHostIp())) {
            sBuilder.append("Business.setSnmpHostIp");
        }
        if (!oltSnmpConfig.getTopSysSnmpHostIpMask().equals(oltAttribute.getTopSysSnmpHostIpMask())) {
            sBuilder.append("Business.setSnmpHostMask");
        }
        oltConfigInfoDao.updateOltSnmpConfig(oltAttribute);
        if (sBuilder.length() > 0) {
            throw new SetValueConflictException(sBuilder.toString());
        }
    }

    @Override
    public void updateEmsSnmpparam(SnmpParam snmpParam) {
        // 调用EntityService.()? 修改设备在网管中的snmp配置
        entityDao.updateEmsSnmpparam(snmpParam);
    }

    /**
     * 获取OltControlFacade对象
     * 
     * @param ip
     *            被采集设备IP
     * @return OltControlFacade
     */
    private OltFacade getOltFacade(String ip) {
        return facadeFactory.getFacade(ip, OltFacade.class);
    }

    @Override
    public void modifyOltBaseInfo(Entity entity, OltAttribute oltAttribute, Integer baseInfoModifiedFlag,
            SnmpParam param) {
        StringBuilder sBuilder = new StringBuilder();
        Long entityId = oltAttribute.getEntityId();
        Entity e = entityDao.selectByPrimaryKey(entity.getEntityId());
        // 需要设置设备上的属性 然后存数据库
        // baseInfoModifiedFlag=1：不需修改设备上的属性
        if (baseInfoModifiedFlag > 1) {
            OltAttribute oltBaseInfo = getOltFacade(param.getIpAddress()).modifyOltBaseInfo(param, oltAttribute);
            if (oltBaseInfo == null) {
                throw new SetValueConflictException("Business.connection");
            }
            oltDao.updateOltBaseInfo(entityId, oltBaseInfo.getOltName(), oltBaseInfo.getTopSysOltRackNum(),
                    oltBaseInfo.getTopSysOltFrameNum());
        }
        // 调用EntityService.updataEntity()
        // 注入entityDao,调用entityDao，update entity
        e.setSysLocation(oltAttribute.getSysLocation());
        e.setSysContact(oltAttribute.getSysContact());
        e.setName(entity.getName());
        e.setNote(entity.getNote());
        entityDao.updateEntityInfo(e);
        if (sBuilder.length() > 0) {
            throw new SetValueConflictException(sBuilder.toString());
        }
    }

    @Override
    public void updateOltEntityIp(Entity entity) {
        Entity e = entityDao.selectByPrimaryKey(entity.getEntityId());
        String name = entity.getName();
        if (name != null && !name.trim().equals("")) {
            e.setName(entity.getName());
        }
        e.setNote(entity.getNote());
        e.setIp(entity.getIp());
        entityDao.updateEntityInfo(e);
    }

    @Override
    public void modifySnmpV2CConfig(OltAttribute oltAttribute, SnmpParam param) {
        getOltFacade(param.getIpAddress()).modifyOltSnmpConfig(param, oltAttribute);
        // 在设置完设备snmp 读写共同体名后，通过设置后的读写共同体来获取设备某个节点判断是否设置成功
        StringBuilder sBuilder = new StringBuilder();
        param.setCommunity(oltAttribute.getTopSysReadCommunity());
        param.setWriteCommunity(oltAttribute.getTopSysWriteCommunity());
        try {
            // 获取sysName节点
            getOltFacade(param.getIpAddress()).getValueByOid(param, "1.3.6.1.2.1.1.5");
        } catch (Exception e) {
            // 产生异常，表示设置失败
            logger.error("modifyV2C failed:{}", e.toString());
            sBuilder.append("Bussiness.modifyV2Cfailed");
        }

        if (sBuilder.length() > 0) {
            throw new SetValueConflictException(sBuilder.toString());
        }
        // ADD BY @Bravin:不仅需要修改 oltAttribute表中关于读写共同体的参数，还要修改snmpParam表此entity的SNMP参数，只能修改其中的读写参数，不能修改SNMP访问版本
        entityDao.updateSnmpV2Param(param);
        oltConfigInfoDao.updateOltSnmpV2CConfig(oltAttribute);
    }

    /*
     * (non-Javadoc)
     * @see com.topvision.ems.epon.service.OltConfigurationService#saveBasicConfig(com.topvision.ems.epon.facade.domain.OltAttribute, com.topvision.ems.facade.domain.Entity)
     */
    @Override
    public void saveBasicConfig(OltAttribute oltAttribute, Entity entity) {
        StringBuilder sb = new StringBuilder();
        Long entityId = oltAttribute.getEntityId();
        SnmpParam param = entityService.getSnmpParamByEntity(entityId);
        // 需要设置设备上的属性 然后存数据库
        OltAttribute oltBaseInfo = getOltFacade(param.getIpAddress()).modifyOltBaseInfo(param, oltAttribute);
        if (oltBaseInfo == null) {
            throw new SetValueConflictException(getMessage("olt.message.connection"));
        }
        oltDao.updateOltBaseInfo(entityId, oltBaseInfo.getOltName(), oltBaseInfo.getTopSysOltRackNum(),
                oltBaseInfo.getTopSysOltFrameNum());
        // 调用EntityService.updataEntity()
        // 注入entityDao,调用entityDao，update entity
        Entity e = entityDao.selectByPrimaryKey(entityId);
        e.setSysName(oltAttribute.getOltName());
        e.setSysLocation(oltAttribute.getSysLocation());
        e.setSysContact(oltAttribute.getSysContact());
        //e.setName(entity.getName());
        //e.setNote(entity.getNote());
        entityDao.updateEntityInfo(e);
        if (sb.length() > 0) {
            throw new SetValueConflictException(sb.toString());
        }
    }

    @Override
    public void modifyInBandConfig(Long entityId, OltAttribute ipMaskInfo, SnmpParam param) {
        OltAttribute oltAttribute = getOltFacade(param.getIpAddress()).modifyInBandConfig(param, ipMaskInfo);
        /* 如果下发的是 0.0.0.0/0.0.0.0 则表示删除带内地址 */
        if ("0.0.0.0".equals(oltAttribute.getInbandIp())) {
            oltAttribute.setInbandVlanId(0); // 0 表示没有配置带内VLAN
        }
        oltAttribute.setInbandPortIndex(ipMaskInfo.getInbandPortIndex());
        oltConfigInfoDao.updateIpMaskInfo(oltAttribute);
    }

    @Override
    public void updateEntityIpAddress(Long entityId, String ip, String oldIp) {
        entityAddressDao.updateAddress(entityId, ip, oldIp);
    }

    @Override
    public void updateOutbandParamInfo(OltAttribute ipMaskInfo, SnmpParam param) {
        OltAttribute oltAttribute = getOltFacade(param.getIpAddress()).modifyOutBandConfig(param, ipMaskInfo);
        oltConfigInfoDao.updateOutbandParamInfo(oltAttribute);
    }

    @Override
    public void saveGate(Long entityId, OltAttribute gate, SnmpParam param) {
        OltAttribute oltAttribute = getOltFacade(param.getIpAddress()).modifyInBandConfig(param, gate);
        oltConfigInfoDao.updateGateInfo(oltAttribute);
    }

    @Override
    public void modifyInBandParamInfo(OltAttribute inbandInfo, SnmpParam param) {
        OltAttribute oltInBandConfig = getOltFacade(param.getIpAddress()).modifyInBandConfig(param, inbandInfo);
        oltConfigInfoDao.updateInBandConfig(oltInBandConfig);
    }

    @Override
    public List<OltVlanInterface> getVlanInterfaceList(Long entityId) {
        return oltConfigInfoDao.selectVlanInterfaceList(entityId);
    }

    @Override
    public List<VlanAttribute> getAvailableVlanIndexList(Long entityId) {
        return oltConfigInfoDao.selectAvailableVlanIndexList(entityId);
    }

    @Override
    public void refreshOltConfigInfo(Long entityId) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        OltAttribute oltAttribute = getOltPerfFacade(snmpParam.getIpAddress()).getOltAttribute(snmpParam);
        //更新OltAttribute表中的信息
        oltAttribute.setEntityId(entityId);
        oltDao.updateOltAttribute(oltAttribute);
        //更新Entity表中的信息
        Entity e = new Entity();
        e.setEntityId(entityId);
        e.setSysName(oltAttribute.getOltName());
        e.setSysLocation(oltAttribute.getSysLocation());
        e.setSysContact(oltAttribute.getSysContact());
        entityDao.updateEntityInfo(e);
    }

    private OltPerfFacade getOltPerfFacade(String ip) {
        return facadeFactory.getFacade(ip, OltPerfFacade.class);
    }

    /**
     * @return the facadeFactory
     */
    public FacadeFactory getFacadeFactory() {
        return facadeFactory;
    }

    /**
     * @param facadeFactory
     *            the facadeFactory to set
     */
    public void setFacadeFactory(FacadeFactory facadeFactory) {
        this.facadeFactory = facadeFactory;
    }

    /**
     * 国际化
     * 
     * @param key
     * @return
     */
    protected String getMessage(String key) {
        try {
            return ResourceManager.getResourceManager("/com/topvision/ems/epon/").getNotNullString(key);
        } catch (Exception e) {
            return key;
        }
    }
}
