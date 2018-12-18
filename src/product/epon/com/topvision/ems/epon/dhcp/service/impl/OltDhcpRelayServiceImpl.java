/***********************************************************************
 * $Id: OltDhcpRelayServiceImpl.java,v1.0 2013-10-25 下午5:44:08 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.dhcp.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.epon.dhcp.dao.OltDhcpRelayDao;
import com.topvision.ems.epon.dhcp.domain.DhcpBundle;
import com.topvision.ems.epon.dhcp.domain.DhcpGiaddrConfig;
import com.topvision.ems.epon.dhcp.domain.DhcpIntIpConfig;
import com.topvision.ems.epon.dhcp.domain.DhcpOption60;
import com.topvision.ems.epon.dhcp.domain.DhcpRelayConfig;
import com.topvision.ems.epon.dhcp.domain.DhcpRelayConfigSetting;
import com.topvision.ems.epon.dhcp.domain.DhcpRelayExtDevice;
import com.topvision.ems.epon.dhcp.domain.DhcpRelayExtGiaddrConfig;
import com.topvision.ems.epon.dhcp.domain.DhcpRelayExtServerConfig;
import com.topvision.ems.epon.dhcp.domain.DhcpRelayVlanMap;
import com.topvision.ems.epon.dhcp.domain.DhcpServerConfig;
import com.topvision.ems.epon.dhcp.facade.OltDhcpRelayFacade;
import com.topvision.ems.epon.dhcp.service.OltDhcpRelayService;
import com.topvision.ems.network.service.EntityService;
import com.topvision.framework.service.BaseService;
import com.topvision.framework.snmp.RowStatus;
import com.topvision.framework.snmp.SnmpParam;
import com.topvision.framework.utils.EponConstants;
import com.topvision.framework.utils.EponIndex;
import com.topvision.platform.facade.FacadeFactory;

/**
 * @author flack
 * @created @2013-10-25-下午5:44:08
 *
 */
@Service("oltDhcpRelayService")
public class OltDhcpRelayServiceImpl extends BaseService implements OltDhcpRelayService{
    @Autowired
    private FacadeFactory facadeFactory;
    @Autowired
    private OltDhcpRelayDao oltDhcpRelayDao;
    @Autowired
    private EntityService entityService;

    @PreDestroy
    @Override
    public void destroy() {
        super.destroy();

    }

    @PostConstruct
    @Override
    public void initialize() {
        super.initialize();
    }


    @Override
    public List<DhcpRelayConfig> getDhcpRelayConfigList(Long entityId) {
        List<DhcpRelayConfig> relayConfigList = new ArrayList<DhcpRelayConfig>();
        List<DhcpBundle> bundleList = oltDhcpRelayDao.getDhcpRelayBundleList(entityId);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("entityId", entityId);
        for (DhcpBundle bundle : bundleList) {
            int cableSourceVerify = bundle.getCableSourceVerify();
            int policy = bundle.getTopCcmtsDhcpBundlePolicy();
            String bundleInterface = bundle.getTopCcmtsDhcpBundleInterface();
            map.put("topCcmtsDhcpBundleInterface", bundleInterface);
            List<String> deviceTypes = new ArrayList<String>();
            deviceTypes.add(EponConstants.DHCP_RELAY_CM);
            deviceTypes.add(EponConstants.DHCP_RELAY_HOST);
            deviceTypes.add(EponConstants.DHCP_RELAY_MTA);
            deviceTypes.add(EponConstants.DHCP_RELAY_STB);
            deviceTypes.addAll(oltDhcpRelayDao.selectDeviceTypes(entityId, bundleInterface));
            List<DhcpIntIpConfig> virtualIpList = oltDhcpRelayDao.getDhcpRelayIntIpListByMap(map);
            for (String device : deviceTypes) {
                map.put("deviceTypeStr", device);
                DhcpRelayConfig relayConfig = new DhcpRelayConfig();
                relayConfig.setPolicy(policy);
                relayConfig.setCableSourceVerify(cableSourceVerify);
                relayConfig.setTopCcmtsDhcpBundleInterface(bundleInterface);
                relayConfig.setDeviceTypeStr(device);
                relayConfig.setVlanMapStr(bundle.getVlanMapStr());
                List<DhcpGiaddrConfig> giaddrs = oltDhcpRelayDao.getDhcpRelayGiAddrList(map);
                List<String> giaddrStr = new ArrayList<String>();
                if (policy == 1) {
                    giaddrStr.add(bundle.getVirtualPrimaryIpAddr());
                } else if (policy == 2) {
                    if (EponConstants.DHCP_RELAY_CM.equalsIgnoreCase(device)) {
                        giaddrStr.add(bundle.getVirtualPrimaryIpAddr());
                    } else {
                        for (DhcpIntIpConfig virtualIp : virtualIpList) {
                            giaddrStr.add(virtualIp.getTopCcmtsDhcpIntIpAddr());
                            break;
                        }
                    }
                }
                for (DhcpGiaddrConfig giaddr : giaddrs) {
                    giaddrStr.add(giaddr.getTopCcmtsDhcpGiAddress());
                }
                List<DhcpOption60> option60s = oltDhcpRelayDao.getDhcpRelayOption60List(map);
                List<String> optionStr = new ArrayList<String>();
                for (DhcpOption60 option : option60s) {
                    optionStr.add(option.getTopCcmtsDhcpOption60Str());
                }
                List<DhcpServerConfig> serverList = oltDhcpRelayDao.getDhcpRelayServerList(map);
                List<String> serverStr = new ArrayList<String>();
                for (DhcpServerConfig server : serverList) {
                    serverStr.add(server.getTopCcmtsDhcpHelperIpAddr());
                }
                relayConfig.setTopCcmtsDhcpGiAddress(giaddrStr);
                relayConfig.setTopCcmtsDhcpHelperIpAddr(serverStr);
                relayConfig.setTopCcmtsDhcpOption60Str(optionStr);
                map.remove("deviceTypeStr");
                relayConfigList.add(relayConfig);
            }
            map.remove("topCcmtsDhcpBundleInterface");
        }
        return relayConfigList;
    }

    @Override
    public void modifyDhcpRelayConfig(Long entityId, DhcpRelayConfigSetting setting) {
        SnmpParam snmp = entityService.getSnmpParamByEntity(entityId);
        OltDhcpRelayFacade dhcpRelayFacade = getDhcpRelayFacade(snmp.getIpAddress());
        Map<String, Object> map = new HashMap<String, Object>();
        DhcpBundle bundle = setting.getDhcpBundle();
        String bundleInterface = bundle.getTopCcmtsDhcpBundleInterface();
        map.put("entityId", entityId);
        map.put("topCcmtsDhcpBundleInterface", bundleInterface);
        if (oltDhcpRelayDao.getDhcpRelayBundle(map) != null) {
            bundle.setTopCcmtsDhcpBundleStatus(RowStatus.ACTIVE);
            // 1.修改设置
            modifyDhcpBundle(snmp, dhcpRelayFacade, bundle);
        } else {
            // 添加bundle
            bundle.setTopCcmtsDhcpBundleStatus(RowStatus.CREATE_AND_GO);
            addDhcpBundle(snmp, dhcpRelayFacade, bundle);
        }
        modifyDhcpRelayVlanMap(snmp, dhcpRelayFacade, setting.getDhcpRelayVlanMap());
        modifyDhcpRelayExtendConfig(snmp, dhcpRelayFacade, bundleInterface, setting);
    }

    @Override
    public void deleteDhcpRelayConfig(Long entityId, String bundleId) {
        SnmpParam snmp = entityService.getSnmpParamByEntity(entityId);
        OltDhcpRelayFacade dhcpRelayFacade = getDhcpRelayFacade(snmp.getIpAddress());
        DhcpBundle bundle = new DhcpBundle();
        bundle.setTopCcmtsDhcpBundleInterface(bundleId);
        bundle.setTopCcmtsDhcpBundleStatus(RowStatus.DESTORY);
        dhcpRelayFacade.modifyDhcpRelayBundleInfo(snmp, bundle);
        // 修改数据库
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("entityId", entityId);
        map.put("topCcmtsDhcpBundleInterface", bundle.getTopCcmtsDhcpBundleInterface());
        oltDhcpRelayDao.deleteDhcpBundle(map);
    }

    @Override
    public void addDhcpOption60(Long entityId, DhcpOption60 option60) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("entityId", entityId);
        map.put("topCcmtsDhcpBundleInterface", option60.getTopCcmtsDhcpBundleInterface());
        map.put("deviceType", option60.getTopCcmtsDhcpOption60DeviceType());
        int number = oltDhcpRelayDao.getDhcpRelayOption60List(map).size();
        option60.setTopCcmtsDhcpOption60Index(number + 1);
        SnmpParam snmp = entityService.getSnmpParamByEntity(entityId);
        OltDhcpRelayFacade dhcpRelayFacade = getDhcpRelayFacade(snmp.getIpAddress());
        dhcpRelayFacade.modifyDhcpRelayOption60Info(snmp, option60);
        // 修改数据库
        option60.setEntityId(entityId);
        oltDhcpRelayDao.addDhcpRelayOption60(option60);

    }

    @Override
    public void addDhcpServer(Long entityId, DhcpServerConfig server) {
        SnmpParam snmp = entityService.getSnmpParamByEntity(entityId);
        OltDhcpRelayFacade dhcpRelayFacade = getDhcpRelayFacade(snmp.getIpAddress());
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("entityId", snmp.getEntityId());
        map.put("topCcmtsDhcpBundleInterface", server.getTopCcmtsDhcpBundleInterface());
        map.put("deviceType", server.getTopCcmtsDhcpHelperDeviceType());
        int number = oltDhcpRelayDao.getDhcpRelayServerList(map).size();
        server.setTopCcmtsDhcpHelperIndex(number + 1);
        dhcpRelayFacade.modifyDhcpRelayServerInfo(snmp, server);
        // 修改数据库
        server.setEntityId(entityId);
        oltDhcpRelayDao.addDhcpServer(server);
    }

    /**
     * 新建一个Bundle
     * 
     * @param bundle
     */
    private void addDhcpBundle(SnmpParam snmp, OltDhcpRelayFacade cmcFacade, DhcpBundle bundle) {
        cmcFacade.modifyDhcpRelayBundleInfo(snmp, bundle);
        // Modify DB
        bundle.setEntityId(snmp.getEntityId());
        oltDhcpRelayDao.addDhcpBundle(bundle);
    }

    /**
     * 修改Bundle基本配置
     * 
     * @param bundle
     */
    private void modifyDhcpBundle(SnmpParam snmp, OltDhcpRelayFacade cmcFacade, DhcpBundle bundle) {
        cmcFacade.modifyDhcpRelayBundleInfo(snmp, bundle);
        // Modify DB
        oltDhcpRelayDao.updateDhcpBundle(bundle);
    }

    /**
     * 修改Giaddr
     * 
     * @param giAddr
     */
    private void modifyDhcpGiAddr(SnmpParam snmp, OltDhcpRelayFacade cmcFacade, String bundleInterface,
            List<DhcpGiaddrConfig> list) {
        if (list != null && list.size() > 0) {
            long entityId = snmp.getEntityId();
            List<DhcpGiaddrConfig> insertList = new ArrayList<DhcpGiaddrConfig>();
            List<DhcpGiaddrConfig> updateList = new ArrayList<DhcpGiaddrConfig>();
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("entityId", entityId);
            map.put("topCcmtsDhcpBundleInterface", bundleInterface);
            List<DhcpGiaddrConfig> oldList = oltDhcpRelayDao.getDhcpRelayGiAddrList(map);
            for (DhcpGiaddrConfig giAddr : list) {
                int i = 0;
                for (; i < oldList.size(); i++) {
                    if (oldList.get(i).getDeviceTypeStr().equals(giAddr.getDeviceTypeStr())) {
                        if (!oldList.get(i).getTopCcmtsDhcpGiAddress().equals(giAddr.getTopCcmtsDhcpGiAddress())) {
                            updateList.add(oldList.get(i));
                        }
                        break;
                    }
                }
                if (i < oldList.size()) {
                    oldList.remove(i);
                } else {
                    insertList.add(giAddr);
                }
            }
            for (DhcpGiaddrConfig giAddr : insertList) {
                if (giAddr.getTopCcmtsDhcpGiAddrDeviceType() == null) {
                    DhcpRelayExtGiaddrConfig extGiaddr = new DhcpRelayExtGiaddrConfig();
                    extGiaddr.setTopCcmtsDhcpBundleInterface(giAddr.getTopCcmtsDhcpBundleInterface());
                    extGiaddr.setDhcpRelayExtDeviceName(giAddr.getDeviceTypeStr());
                    extGiaddr.setDhcpRelayExtDevGiAddress(giAddr.getTopCcmtsDhcpGiAddress());
                    extGiaddr.setDhcpRelayExtDevGiAddrStatus(RowStatus.CREATE_AND_GO);
                    cmcFacade.modifyDhcpRelayExtGiaddr(snmp, extGiaddr);
                } else {
                    giAddr.setTopCcmtsDhcpGiAddrStatus(RowStatus.CREATE_AND_GO);
                    cmcFacade.modifyDhcpRelayGiAddrInfo(snmp, giAddr);
                }
            }
            for (DhcpGiaddrConfig giAddr : updateList) {
                if (giAddr.getTopCcmtsDhcpGiAddrDeviceType() == null) {
                    DhcpRelayExtGiaddrConfig extGiaddr = new DhcpRelayExtGiaddrConfig();
                    extGiaddr.setTopCcmtsDhcpBundleInterface(giAddr.getTopCcmtsDhcpBundleInterface());
                    extGiaddr.setDhcpRelayExtDeviceName(giAddr.getDeviceTypeStr());
                    extGiaddr.setDhcpRelayExtDevGiAddress(giAddr.getTopCcmtsDhcpGiAddress());
                    extGiaddr.setDhcpRelayExtDevGiAddrStatus(RowStatus.ACTIVE);
                    cmcFacade.modifyDhcpRelayExtGiaddr(snmp, extGiaddr);
                } else {
                    giAddr.setTopCcmtsDhcpGiAddrStatus(RowStatus.ACTIVE);
                    cmcFacade.modifyDhcpRelayGiAddrInfo(snmp, giAddr);
                }
            }
            for (DhcpGiaddrConfig giAddr : oldList) {
                if (giAddr.getTopCcmtsDhcpGiAddrDeviceType() == null) {
                    DhcpRelayExtGiaddrConfig extGiaddr = new DhcpRelayExtGiaddrConfig();
                    extGiaddr.setTopCcmtsDhcpBundleInterface(giAddr.getTopCcmtsDhcpBundleInterface());
                    extGiaddr.setDhcpRelayExtDeviceName(giAddr.getDeviceTypeStr());
                    extGiaddr.setDhcpRelayExtDevGiAddrStatus(RowStatus.DESTORY);
                    cmcFacade.modifyDhcpRelayExtGiaddr(snmp, extGiaddr);
                } else {
                    giAddr.setTopCcmtsDhcpGiAddress(null);
                    giAddr.setTopCcmtsDhcpGiAddrStatus(RowStatus.DESTORY);
                    cmcFacade.modifyDhcpRelayGiAddrInfo(snmp, giAddr);
                }
            }
            // Modify DB
            oltDhcpRelayDao.batchInsertDhcpRelayGiaddr(snmp.getEntityId(), insertList);
            oltDhcpRelayDao.batchUpdateDhcpRelayGiaddr(snmp.getEntityId(), updateList);
            oltDhcpRelayDao.batchDeleteDhcpRelayGiAddr(snmp.getEntityId(), oldList);
        }

    }

    /**
     * 修改Option60
     * 
     * @param list
     */
    private void modifyDhcpRelayExtendConfig(SnmpParam snmp, OltDhcpRelayFacade cmcFacade, String bundleInterface,
            DhcpRelayConfigSetting setting) {
        List<DhcpOption60> list = setting.getDhcpOption60();
        if (list != null && list.size() > 0) {
            List<DhcpOption60> addList = new ArrayList<DhcpOption60>();
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("entityId", snmp.getEntityId());
            map.put("topCcmtsDhcpBundleInterface", bundleInterface);
            List<DhcpOption60> deleteList = oltDhcpRelayDao.getDhcpRelayOption60List(map);
            for (DhcpOption60 option60 : list) {
                int i = 0;
                for (; i < deleteList.size(); i++) {
                    if (deleteList.get(i).getDeviceTypeStr().equals(option60.getDeviceTypeStr())
                            && deleteList.get(i).getTopCcmtsDhcpOption60Str()
                                    .equals(option60.getTopCcmtsDhcpOption60Str())) {
                        break;
                    }
                }
                if (i < deleteList.size()) {
                    deleteList.remove(i);
                } else {
                    addList.add(option60);
                }
            }
            addDhcpOption60(snmp, cmcFacade, addList);
            modifyDhcpGiAddr(snmp, cmcFacade, bundleInterface, setting.getDhcpGiAddr());
            modifyDhcpVirtualIp(snmp, cmcFacade, bundleInterface, setting.getVirtualIp());
            modifyDhcpServer(snmp, cmcFacade, bundleInterface, setting.getDhcpServer());
            deleteDhcpOption60(snmp, cmcFacade, deleteList);
        }
    }

    /**
     * 添加Option 60
     * 
     * @param list
     */
    private void addDhcpOption60(SnmpParam snmp, OltDhcpRelayFacade cmcFacade, List<DhcpOption60> list) {
        if (list != null && list.size() > 0) {
            Long entityId = snmp.getEntityId();
            for (DhcpOption60 option60 : list) {
                int index = EponIndex.generateIndex(oltDhcpRelayDao.selectDhcpRelayServerIndex(entityId,
                        option60.getTopCcmtsDhcpBundleInterface(), null, option60.getDeviceTypeStr()));
                option60.setTopCcmtsDhcpOption60Index(index);
                option60.setTopCcmtsDhcpOption60Status(RowStatus.CREATE_AND_GO);
                if (option60.getTopCcmtsDhcpOption60DeviceType() == null) {
                    DhcpRelayExtDevice dhcpRelayExtDevice = new DhcpRelayExtDevice();
                    dhcpRelayExtDevice.setTopCcmtsDhcpBundleInterface(option60.getTopCcmtsDhcpBundleInterface());
                    dhcpRelayExtDevice.setDhcpRelayExtDeviceName(option60.getDeviceTypeStr());
                    dhcpRelayExtDevice.setDhcpRelayExtDeviceOptionIndex(index);
                    dhcpRelayExtDevice.setDhcpRelayExtDeviceOptionStr(option60.getTopCcmtsDhcpOption60Str());
                    dhcpRelayExtDevice.setDhcpRelayExtDeviceOptionStatus(RowStatus.CREATE_AND_GO);
                    cmcFacade.modifyDhcpRelayExtDeviceOption(snmp, dhcpRelayExtDevice);
                } else {
                    cmcFacade.modifyDhcpRelayOption60Info(snmp, option60);
                }
                // 修改数据库
                oltDhcpRelayDao.insertDhcpRelayOption60(entityId, option60);
            }
        }
    }

    /**
     * 删除Option60
     * 
     * @param list
     */
    private void deleteDhcpOption60(SnmpParam snmp, OltDhcpRelayFacade cmcFacade, List<DhcpOption60> list) {
        if (list != null && list.size() > 0) {
            for (DhcpOption60 option60 : list) {
                if (option60.getTopCcmtsDhcpOption60DeviceType() == null) {
                    //扩展设备类型
                    DhcpRelayExtDevice dhcpRelayExtDevice = new DhcpRelayExtDevice();
                    dhcpRelayExtDevice.setTopCcmtsDhcpBundleInterface(option60.getTopCcmtsDhcpBundleInterface());
                    dhcpRelayExtDevice.setDhcpRelayExtDeviceName(option60.getDeviceTypeStr());
                    dhcpRelayExtDevice.setDhcpRelayExtDeviceOptionIndex(option60.getTopCcmtsDhcpOption60Index());
                    dhcpRelayExtDevice.setDhcpRelayExtDeviceOptionStatus(RowStatus.DESTORY);
                    cmcFacade.modifyDhcpRelayExtDeviceOption(snmp, dhcpRelayExtDevice);
                } else {
                    option60.setTopCcmtsDhcpOption60Status(RowStatus.DESTORY);
                    cmcFacade.modifyDhcpRelayOption60Info(snmp, option60);
                }
            }
            // Modify DB
            oltDhcpRelayDao.batchDeleteDhcpRelayOption60(snmp.getEntityId(), list);
        }

    }

    /**
     * 修改DHCP Server
     * 
     * @param list
     */
    private void modifyDhcpServer(SnmpParam snmp, OltDhcpRelayFacade cmcFacade, String bundleInterface,
            List<DhcpServerConfig> list) {
        if (list != null && list.size() > 0) {
            List<DhcpServerConfig> addList = new ArrayList<DhcpServerConfig>();
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("entityId", snmp.getEntityId());
            map.put("topCcmtsDhcpBundleInterface", bundleInterface);
            List<DhcpServerConfig> deleteList = oltDhcpRelayDao.getDhcpRelayServerList(map);
            for (DhcpServerConfig server : list) {
                int i = 0;
                for (; i < deleteList.size(); i++) {
                    if (deleteList.get(i).getDeviceTypeStr().equals(server.getDeviceTypeStr())
                            && deleteList.get(i).getTopCcmtsDhcpHelperIpAddr()
                                    .equals(server.getTopCcmtsDhcpHelperIpAddr())) {
                        break;
                    }
                }
                if (i < deleteList.size()) {
                    deleteList.remove(i);
                } else {
                    addList.add(server);
                }
            }
            addDhcpServer(snmp, cmcFacade, addList);
            deleteDhcpServer(snmp, cmcFacade, deleteList);
        }
    }

    /**
     * 添加DHCP Server
     * 
     * @param list
     */
    private void addDhcpServer(SnmpParam snmp, OltDhcpRelayFacade cmcFacade, List<DhcpServerConfig> list) {
        if (list != null && list.size() > 0) {
            Long entityId = snmp.getEntityId();
            for (DhcpServerConfig server : list) {
                int index = EponIndex.generateIndex(oltDhcpRelayDao.selectDhcpRelayServerIndex(entityId,
                        server.getTopCcmtsDhcpBundleInterface(), null, server.getDeviceTypeStr()));
                server.setTopCcmtsDhcpHelperIndex(index);
                if (server.getTopCcmtsDhcpHelperDeviceType() == null) {
                    DhcpRelayExtServerConfig dhcpRelayExtServer = new DhcpRelayExtServerConfig();
                    dhcpRelayExtServer.setTopCcmtsDhcpBundleInterface(server.getTopCcmtsDhcpBundleInterface());
                    dhcpRelayExtServer.setDhcpRelayExtDeviceName(server.getDeviceTypeStr());
                    dhcpRelayExtServer.setDhcpRelayExtHelperIndex(server.getTopCcmtsDhcpHelperIndex());
                    dhcpRelayExtServer.setDhcpRelayExtHelperIpAddr(server.getTopCcmtsDhcpHelperIpAddr());
                    dhcpRelayExtServer.setDhcpRelayExtHelperStatus(RowStatus.CREATE_AND_GO);
                    cmcFacade.modifyDhcpRelayExtServer(snmp, dhcpRelayExtServer);
                } else {
                    cmcFacade.modifyDhcpRelayServerInfo(snmp, server);
                }
                oltDhcpRelayDao.insertDhcpRelayServer(entityId, server);
            }
        }
    }

    /**
     * 删除DHCP Server
     * 
     * @param list
     */
    private void deleteDhcpServer(SnmpParam snmp, OltDhcpRelayFacade cmcFacade, List<DhcpServerConfig> list) {
        if (list != null && list.size() > 0) {
            for (DhcpServerConfig server : list) {
                if (server.getTopCcmtsDhcpHelperDeviceType() == null) {
                    DhcpRelayExtServerConfig dhcpRelayExtServer = new DhcpRelayExtServerConfig();
                    dhcpRelayExtServer.setTopCcmtsDhcpBundleInterface(server.getTopCcmtsDhcpBundleInterface());
                    dhcpRelayExtServer.setDhcpRelayExtDeviceName(server.getDeviceTypeStr());
                    dhcpRelayExtServer.setDhcpRelayExtHelperIndex(server.getTopCcmtsDhcpHelperIndex());
                    dhcpRelayExtServer.setDhcpRelayExtHelperStatus(RowStatus.DESTORY);
                } else {
                    server.setTopCcmtsDhcpHelperIpAddr(null);
                    server.setTopCcmtsDhcpHelperStatus(RowStatus.DESTORY);
                    cmcFacade.modifyDhcpRelayServerInfo(snmp, server);
                }
            }
            // Modify DB
            oltDhcpRelayDao.batchDeleteDhcpRelayServer(snmp.getEntityId(), list);
        }

    }

    private void modifyDhcpVirtualIp(SnmpParam snmp, OltDhcpRelayFacade cmcFacade, String bundleInterface,
            List<DhcpIntIpConfig> list) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("entityId", snmp.getEntityId());
        map.put("topCcmtsDhcpBundleInterface", bundleInterface);
        List<DhcpIntIpConfig> deleteList = oltDhcpRelayDao.getDhcpRelayIntIpListByMap(map);
        deleteDhcpVirtualIp(snmp, cmcFacade, bundleInterface, deleteList);
        addDhcpVirtualIp(snmp, cmcFacade, bundleInterface, list);

    }

    private void addDhcpVirtualIp(SnmpParam snmp, OltDhcpRelayFacade cmcFacade, String bundleInterface,
            List<DhcpIntIpConfig> list) {
        if (list != null && list.size() > 0) {
            Map<String, Object> map = new HashMap<String, Object>();
            long entityId = snmp.getEntityId();
            map.put("entityId", entityId);
            map.put("topCcmtsDhcpBundleInterface", bundleInterface);
            List<DhcpIntIpConfig> temp = oltDhcpRelayDao.getDhcpRelayIntIpListByMap(map);
            int index = 0;
            if (temp != null) {
                index = temp.size();
            }
            for (DhcpIntIpConfig virtualIp : list) {
                virtualIp.setTopCcmtsDhcpIntIpIndex(++index);
                virtualIp.setTopCcmtsDhcpIntIpStatus(RowStatus.CREATE_AND_GO);
                cmcFacade.modifyDhcpRelayIntIp(snmp, virtualIp);
                virtualIp.setEntityId(entityId);
                oltDhcpRelayDao.addDhcpRelayIntIp(virtualIp);
            }

        }
    }

    private void deleteDhcpVirtualIp(SnmpParam snmp, OltDhcpRelayFacade cmcFacade, String bundleInterface,
            List<DhcpIntIpConfig> ipList) {
        if (ipList != null && ipList.size() > 0) {
            for (DhcpIntIpConfig ip : ipList) {
                //由于索引或自动发生变化，因此需要删除之前获取信息
                List<DhcpIntIpConfig> intIps = cmcFacade.getDhcpRelayIntIp(snmp);
                for (DhcpIntIpConfig intIp : intIps) {
                    if (intIp.getTopCcmtsDhcpIntIpAddr().trim().equals(ip.getTopCcmtsDhcpIntIpAddr())) {
                        intIp.setTopCcmtsDhcpIntIpAddr(null);
                        intIp.setTopCcmtsDhcpIntIpMask(null);
                        intIp.setTopCcmtsDhcpIntIpStatus(RowStatus.DESTORY);
                        cmcFacade.modifyDhcpRelayIntIp(snmp, intIp);
                        oltDhcpRelayDao.deleteDhcpRelayIntIp(ip.getEntityId(), bundleInterface,
                                ip.getTopCcmtsDhcpIntIpAddr());

                    }
                }
            }
        }
    }

    private void modifyDhcpRelayVlanMap(SnmpParam snmp, OltDhcpRelayFacade cmcFacade, DhcpRelayVlanMap dhcpRelayVlanMap) {
        if (!"1-4094".equals(dhcpRelayVlanMap.getVlanMapStr())) {
            cmcFacade.modifyDhcpRelayVlanMap(snmp, dhcpRelayVlanMap);
            oltDhcpRelayDao.updateDhcpRelayVlanMap(snmp.getEntityId(), dhcpRelayVlanMap);
        }
    }

    @Override
    public DhcpBundle getCmcDhcpBundle(Long entityId, String bundleInterface) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("entityId", entityId);
        map.put("topCcmtsDhcpBundleInterface", bundleInterface);
        return oltDhcpRelayDao.getDhcpRelayBundle(map);
    }

    @Override
    public List<DhcpServerConfig> getCmcDhcpServerList(Long entityId, String bundleInterface) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("entityId", entityId);
        map.put("topCcmtsDhcpBundleInterface", bundleInterface);
        return oltDhcpRelayDao.getDhcpRelayServerList(map);
    }

    @Override
    public List<DhcpGiaddrConfig> getCmcDhcpGiAddrList(Long entityId, String bundleInterface) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("entityId", entityId);
        map.put("topCcmtsDhcpBundleInterface", bundleInterface);
        List<DhcpGiaddrConfig> giAddrs = oltDhcpRelayDao.getDhcpRelayGiAddrList(map);
        DhcpBundle bundle = oltDhcpRelayDao.getDhcpRelayBundle(map);
        for (DhcpGiaddrConfig giAddr : giAddrs) {
            if (giAddr.getTopCcmtsDhcpGiAddress().equals(bundle.getVirtualPrimaryIpAddr())) {
                giAddr.setTopCcmtsDhcpGiAddrMask(bundle.getVirtualPrimaryIpMask());
            }
        }
        return giAddrs;
    }

    @Override
    public List<DhcpIntIpConfig> getCmcDhcpIntIpList(Long entityId, String bundleInterface) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("entityId", entityId);
        map.put("topCcmtsDhcpBundleInterface", bundleInterface);
        List<DhcpIntIpConfig> virtualIps = oltDhcpRelayDao.getDhcpRelayIntIpListByMap(map);
        DhcpBundle bundle = oltDhcpRelayDao.getDhcpRelayBundle(map);
        if (!"0.0.0.0".equals(bundle.getVirtualPrimaryIpAddr())) {
            DhcpIntIpConfig virtualIp = new DhcpIntIpConfig();
            virtualIp.setEntityId(bundle.getEntityId());
            virtualIp.setTopCcmtsDhcpBundleInterface(bundleInterface);
            virtualIp.setTopCcmtsDhcpIntIpAddr(bundle.getVirtualPrimaryIpAddr());
            virtualIp.setTopCcmtsDhcpIntIpMask(bundle.getVirtualPrimaryIpMask());
            virtualIp.setTopCcmtsDhcpIntIpIndex(0);
            virtualIps.add(virtualIp);
        }
        return virtualIps;
    }

    @Override
    public List<DhcpOption60> getCmcDhcpOption60List(Long entityId, String bundleInterface) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("entityId", entityId);
        map.put("topCcmtsDhcpBundleInterface", bundleInterface);
        return oltDhcpRelayDao.getDhcpRelayOption60List(map);
    }

    @Override
    public List<DhcpIntIpConfig> getDhcpVirtualIPList(Long entityId, String bundleInterface) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("entityId", entityId);
        map.put("topCcmtsDhcpBundleInterface", bundleInterface);
        return oltDhcpRelayDao.getDhcpRelayIntIpListByMap(map);
    }

    @Override
    public List<String> getCmcDhcpBundleEndList(Long entityId) {
        List<DhcpBundle> bundleList = oltDhcpRelayDao.getDhcpRelayBundleList(entityId);
        List<String> bundleEndList = new ArrayList<String>();
        for (DhcpBundle bundle : bundleList) {
            bundleEndList.add(bundle.getTopCcmtsDhcpBundleInterface().replace("bundle", ""));
        }
        return bundleEndList;
    }

    @Override
    public List<DhcpIntIpConfig> getVirtulIpList(Long entityId) {
        List<DhcpIntIpConfig> virtualIpList = oltDhcpRelayDao.getDhcpRelayIntIpList(entityId);
        List<DhcpBundle> bundleList = oltDhcpRelayDao.getDhcpRelayBundleList(entityId);
        for (DhcpBundle bundle : bundleList) {
            if (!"0.0.0.0".equals(bundle.getVirtualPrimaryIpAddr())) {
                DhcpIntIpConfig virtualIp = new DhcpIntIpConfig();
                virtualIp.setTopCcmtsDhcpIntIpAddr(bundle.getVirtualPrimaryIpAddr());
                virtualIp.setTopCcmtsDhcpIntIpMask(bundle.getVirtualPrimaryIpMask());
                virtualIpList.add(virtualIp);
            }
        }
        return virtualIpList;
    }

    @Override
    public void refreshDhcpRelayConfig(Long entityId) {
        SnmpParam snmp = entityService.getSnmpParamByEntity(entityId);
        OltDhcpRelayFacade dhcpRelayFacade = getDhcpRelayFacade(snmp.getIpAddress());
        refreshDhcpRelayBaseConfig(entityId, snmp, dhcpRelayFacade);
        refreshDhcpRelayBundleConfig(entityId, snmp, dhcpRelayFacade);
        refreshDhcpRelayVlanMap(entityId, snmp, dhcpRelayFacade);
        refreshDhcpRelayServer(entityId, snmp, dhcpRelayFacade);
        refreshDhcpRelayOption(entityId, snmp, dhcpRelayFacade);
        refreshDhcpRelayGiaddr(entityId, snmp, dhcpRelayFacade);
        modifyDhcpRelayIntIp(entityId, snmp, dhcpRelayFacade);
    }

    private void refreshDhcpRelayBaseConfig(Long entityId, SnmpParam snmp, OltDhcpRelayFacade dhcpRelayFacade) {
        int relaySwitch = dhcpRelayFacade.getDhcpRelaySwitch(snmp);
        if (oltDhcpRelayDao.selectDhcpRelaySwitch(entityId) == null) {
            oltDhcpRelayDao.insertDhcpRelaySwitch(entityId, relaySwitch);
        } else {
            oltDhcpRelayDao.updateDhcpRelaySwitch(entityId, relaySwitch);
        }
    }

    private void refreshDhcpRelayBundleConfig(Long entityId, SnmpParam snmp, OltDhcpRelayFacade dhcpRelayFacade) {
        List<DhcpBundle> bundles = dhcpRelayFacade.getCmcDhcpRelayBundleInfo(snmp);
        oltDhcpRelayDao.batchInsertDhcpRelayBundleConfig(bundles, entityId);
    }

    private void refreshDhcpRelayVlanMap(Long entityId, SnmpParam snmp, OltDhcpRelayFacade dhcpRelayFacade) {
        List<DhcpRelayVlanMap> dhcpRelayVlanMaps = dhcpRelayFacade.getDhcpRelayVlanMaps(snmp);
        oltDhcpRelayDao.batchUpdateDhcpRelayVlanMap(entityId, dhcpRelayVlanMaps);
    }

    private void refreshDhcpRelayServer(Long entityId, SnmpParam snmp, OltDhcpRelayFacade dhcpRelayFacade) {
        List<DhcpServerConfig> servers = dhcpRelayFacade.getDhcpRelayServerConfigInfo(snmp);
        logger.debug("refresh topCcmtsDhcpHelperTable finished!");
        List<DhcpRelayExtServerConfig> extServers = dhcpRelayFacade.getDhcpRelayExtServers(snmp);
        logger.debug("refresh topCcmtsDhcpExtensionalDevHelperTable finished!");
        if (servers == null) {
            servers = new ArrayList<DhcpServerConfig>();
        }
        for (DhcpRelayExtServerConfig extServer : extServers) {
            DhcpServerConfig server = new DhcpServerConfig();
            server.setDeviceTypeStr(extServer.getDhcpRelayExtDeviceName());
            server.setTopCcmtsDhcpHelperIndex(extServer.getDhcpRelayExtHelperIndex());
            server.setTopCcmtsDhcpBundleInterface(extServer.getTopCcmtsDhcpBundleInterface());
            server.setTopCcmtsDhcpHelperIpAddr(extServer.getDhcpRelayExtHelperIpAddr());
            server.setTopCcmtsDhcpHelperStatus(extServer.getDhcpRelayExtHelperStatus());
            servers.add(server);
        }
        oltDhcpRelayDao.batchInsertDhcpRelayServerConfig(servers, entityId);
    }

    private void refreshDhcpRelayOption(Long entityId, SnmpParam snmp, OltDhcpRelayFacade dhcpRelayFacade) {
        List<DhcpOption60> option60s = dhcpRelayFacade.getDhcpRelayOption60Info(snmp);
        logger.debug("refresh topCcmtsDhcpOption60Table finished!");
        List<DhcpRelayExtDevice> extOption60s = dhcpRelayFacade.getDhcpRelayExtDeviceOptions(snmp);
        logger.debug("refresh topCcmtsDhcpExtensionalDevTable finished!");
        if (option60s == null) {
            option60s = new ArrayList<DhcpOption60>();
        }
        for (DhcpRelayExtDevice extOption : extOption60s) {
            DhcpOption60 option = new DhcpOption60();
            option.setDeviceTypeStr(extOption.getDhcpRelayExtDeviceName());
            option.setTopCcmtsDhcpBundleInterface(extOption.getTopCcmtsDhcpBundleInterface());
            option.setTopCcmtsDhcpOption60Index(extOption.getDhcpRelayExtDeviceOptionIndex());
            option.setTopCcmtsDhcpOption60Str(extOption.getDhcpRelayExtDeviceOptionStr());
            option.setTopCcmtsDhcpOption60Status(extOption.getDhcpRelayExtDeviceOptionStatus());
            option60s.add(option);
        }
        oltDhcpRelayDao.batchInsertDhcpRelayOption60Config(option60s, entityId);
    }

    private void refreshDhcpRelayGiaddr(Long entityId, SnmpParam snmp, OltDhcpRelayFacade dhcpRelayFacade) {
        List<DhcpGiaddrConfig> giaddrs = dhcpRelayFacade.getDhcpRelayGiAddrInfo(snmp);
        logger.debug("refresh topCcmtsDhcpGiAddrTable finished!");
        List<DhcpRelayExtGiaddrConfig> extGiaddrs = dhcpRelayFacade.getDhcpRelayExtGiaddrs(snmp);
        logger.debug("refresh topCcmtsDhcpExtensionalDevGiAddrTable finished!");
        if (giaddrs == null) {
            giaddrs = new ArrayList<DhcpGiaddrConfig>();
        }
        for (DhcpRelayExtGiaddrConfig extGiaddr : extGiaddrs) {
            DhcpGiaddrConfig giaddr = new DhcpGiaddrConfig();
            giaddr.setDeviceTypeStr(extGiaddr.getDhcpRelayExtDeviceName());
            giaddr.setTopCcmtsDhcpBundleInterface(extGiaddr.getTopCcmtsDhcpBundleInterface());
            giaddr.setTopCcmtsDhcpGiAddress(extGiaddr.getDhcpRelayExtDevGiAddress());
            giaddr.setTopCcmtsDhcpGiAddrStatus(extGiaddr.getDhcpRelayExtDevGiAddrStatus());
            giaddrs.add(giaddr);
        }
        oltDhcpRelayDao.batchInsertDhcpRelayGiaddrConfig(giaddrs, entityId);
    }

    private void modifyDhcpRelayIntIp(Long entityId, SnmpParam snmp, OltDhcpRelayFacade dhcpRelayFacade) {
        List<DhcpIntIpConfig> intIps = dhcpRelayFacade.getDhcpRelayIntIp(snmp);
        logger.debug("refresh topCcmtsDhcpVirtualSecendaryIpTable finished!");
        oltDhcpRelayDao.batchInsertDhcpRelayIntIpConfig(intIps, entityId);
    }

    @Override
    public void modifyDhcpRelaySwitch(Long entityId, Integer dhcpRelaySwitch) {
        SnmpParam snmp = entityService.getSnmpParamByEntity(entityId);
        OltDhcpRelayFacade dhcpRelayFacade = getDhcpRelayFacade(snmp.getIpAddress());
        dhcpRelayFacade.modifyDhcpRelaySwitch(snmp, dhcpRelaySwitch);
        oltDhcpRelayDao.updateDhcpRelaySwitch(entityId, dhcpRelaySwitch);
    }

    @Override
    public Integer getDhcpRelaySwitch(Long entityId) {
        return oltDhcpRelayDao.selectDhcpRelaySwitch(entityId);
    }

    private OltDhcpRelayFacade getDhcpRelayFacade(String ip) {
        return facadeFactory.getFacade(ip, OltDhcpRelayFacade.class);
    }

    public FacadeFactory getFacadeFactory() {
        return facadeFactory;
    }

    public void setFacadeFactory(FacadeFactory facadeFactory) {
        this.facadeFactory = facadeFactory;
    }

    @Override
    public List<String> getDeviceTypes(Long entityId, String bundleInterface) {
        return oltDhcpRelayDao.selectDeviceTypes(entityId, bundleInterface);
    }

}
