package com.topvision.ems.cmc.dhcp.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.cmc.dhcp.dao.CmcDhcpDao;
import com.topvision.ems.cmc.dhcp.domain.DhcpRelayConfig;
import com.topvision.ems.cmc.dhcp.domain.DhcpRelayConfigSetting;
import com.topvision.ems.cmc.dhcp.exception.SetDhcpRelayFailException;
import com.topvision.ems.cmc.dhcp.facade.domain.CmcDhcpBaseConfig;
import com.topvision.ems.cmc.dhcp.facade.domain.CmcDhcpBundle;
import com.topvision.ems.cmc.dhcp.facade.domain.CmcDhcpGiAddr;
import com.topvision.ems.cmc.dhcp.facade.domain.CmcDhcpIntIp;
import com.topvision.ems.cmc.dhcp.facade.domain.CmcDhcpOption60;
import com.topvision.ems.cmc.dhcp.facade.domain.CmcDhcpPacketVlan;
import com.topvision.ems.cmc.dhcp.facade.domain.CmcDhcpServerConfig;
import com.topvision.ems.cmc.dhcp.service.CmcDhcpRelayService;
import com.topvision.ems.cmc.facade.CmcFacade;
import com.topvision.ems.cmc.service.impl.CmcBaseCommonService;
import com.topvision.ems.cmc.util.CmcConstants;
import com.topvision.framework.utils.CmcIndexUtils;
import com.topvision.ems.template.service.EntityTypeService;
import com.topvision.framework.snmp.RowStatus;
import com.topvision.framework.snmp.SnmpParam;
import com.topvision.platform.message.event.CmcSynchronizedEvent;
import com.topvision.platform.message.event.CmcSynchronizedListener;
import com.topvision.platform.message.service.MessageService;

@Service("cmcDhcpRelayService")
public class CmcDhcpRelayServiceImpl extends CmcBaseCommonService implements CmcDhcpRelayService,
        CmcSynchronizedListener {
    @Resource(name = "cmcDhcpDao")
    private CmcDhcpDao cmcDhcpDao;
    private Map<String, Object> result;
    @Autowired
    private MessageService messageService;
    @Autowired
    private EntityTypeService entityTypeService;

    @PreDestroy
    public void destroy() {
        super.destroy();
        messageService.removeListener(CmcSynchronizedListener.class, this);
    }

    @PostConstruct
    public void initialize() {
        super.initialize();
        messageService.addListener(CmcSynchronizedListener.class, this);
    }

    @Override
    public List<DhcpRelayConfig> getDhcpRelayConfigList(Long entityId) {
        List<DhcpRelayConfig> relayConfigList = new ArrayList<DhcpRelayConfig>();
        List<CmcDhcpBundle> bundleList = cmcDhcpDao.getCmcDhcpBundleList(entityId);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("entityId", entityId);
        for (CmcDhcpBundle bundle : bundleList) {
            DhcpRelayConfig cmRelayConfig = new DhcpRelayConfig();
            DhcpRelayConfig hostRelayConfig = new DhcpRelayConfig();
            DhcpRelayConfig mtaRelayConfig = new DhcpRelayConfig();
            DhcpRelayConfig stbRelayConfig = new DhcpRelayConfig();
            String bundleInterface = bundle.getTopCcmtsDhcpBundleInterface();
            int cableSourceVerify = bundle.getCableSourceVerify();
            int policy = bundle.getTopCcmtsDhcpBundlePolicy();
            // 设置Bundle
            cmRelayConfig.setTopCcmtsDhcpBundleInterface(bundleInterface);
            hostRelayConfig.setTopCcmtsDhcpBundleInterface(bundleInterface);
            mtaRelayConfig.setTopCcmtsDhcpBundleInterface(bundleInterface);
            stbRelayConfig.setTopCcmtsDhcpBundleInterface(bundleInterface);
            cmRelayConfig.setCableSourceVerify(cableSourceVerify);
            hostRelayConfig.setCableSourceVerify(cableSourceVerify);
            mtaRelayConfig.setCableSourceVerify(cableSourceVerify);
            stbRelayConfig.setCableSourceVerify(cableSourceVerify);
            cmRelayConfig.setPolicy(policy);
            hostRelayConfig.setPolicy(policy);
            mtaRelayConfig.setPolicy(policy);
            stbRelayConfig.setPolicy(policy);
            // 设置设备类型
            cmRelayConfig.setTopCcmtsDhcpHelperDeviceType(CmcConstants.DHCP_DEVICETYPE_CM);
            hostRelayConfig.setTopCcmtsDhcpHelperDeviceType(CmcConstants.DHCP_DEVICETYPE_HOST);
            mtaRelayConfig.setTopCcmtsDhcpHelperDeviceType(CmcConstants.DHCP_DEVICETYPE_MTA);
            stbRelayConfig.setTopCcmtsDhcpHelperDeviceType(CmcConstants.DHCP_DEVICETYPE_STB);
            map.put("topCcmtsDhcpBundleInterface", bundleInterface);
            // 设置VLAN
            List<CmcDhcpPacketVlan> packetVlans = cmcDhcpDao.selectDhcpPacketVlanList(entityId, bundleInterface);
            for (CmcDhcpPacketVlan packetVlan : packetVlans) {
                int vlanTag = packetVlan.getTopCcmtsDhcpTagVlan();
                int vlanPriority = packetVlan.getTopCcmtsDhcpTagPriority();
                switch (packetVlan.getTopCcmtsDhcpDeviceType()) {
                case 1:
                    cmRelayConfig.setVlanTag(vlanTag);
                    cmRelayConfig.setVlanPriority(vlanPriority);
                    break;
                case 2:
                    hostRelayConfig.setVlanTag(vlanTag);
                    hostRelayConfig.setVlanPriority(vlanPriority);
                    break;
                case 3:
                    mtaRelayConfig.setVlanTag(vlanTag);
                    mtaRelayConfig.setVlanPriority(vlanPriority);
                    break;
                case 4:
                    stbRelayConfig.setVlanTag(vlanTag);
                    stbRelayConfig.setVlanPriority(vlanPriority);
                    break;
                }
            }
            // 组织GiAddr
            List<String> cmGiAddr = new ArrayList<String>();
            List<String> hostGiAddr = new ArrayList<String>();
            List<String> mtaGiAddr = new ArrayList<String>();
            List<String> stbGiAddr = new ArrayList<String>();
            List<String> cmGiAddrMask = new ArrayList<String>();
            List<String> hostGiAddrMask = new ArrayList<String>();
            List<String> mtaGiAddrMask = new ArrayList<String>();
            List<String> stbGiAddrMask = new ArrayList<String>();
            List<CmcDhcpIntIp> virtualIpList = cmcDhcpDao.getCmcDhcpIntIpListByMap(map);
            if (bundle.getTopCcmtsDhcpBundlePolicy().intValue() == CmcConstants.DHCP_PRIMARY) {
                List<String> giAddr = new ArrayList<String>();
                giAddr.add(bundle.getVirtualPrimaryIpAddr());
                List<String> giAddrMask = new ArrayList<String>();
                giAddrMask.add(bundle.getVirtualPrimaryIpMask());
                cmGiAddr = giAddr;
                hostGiAddr = giAddr;
                mtaGiAddr = giAddr;
                stbGiAddr = giAddr;
                cmGiAddrMask = giAddrMask;
                hostGiAddrMask = giAddrMask;
                mtaGiAddrMask = giAddrMask;
                stbGiAddrMask = giAddrMask;
            } else if (bundle.getTopCcmtsDhcpBundlePolicy().intValue() == CmcConstants.DHCP_STRICT) {
                List<CmcDhcpGiAddr> giAddrList = cmcDhcpDao.getCmcDhcpGiAddrList(map);
                for (CmcDhcpGiAddr giaddr : giAddrList) {
                    switch (giaddr.getTopCcmtsDhcpGiAddrDeviceType()) {
                    case 1:
                        cmGiAddr.add(giaddr.getTopCcmtsDhcpGiAddress());
                        cmGiAddrMask.add(giaddr.getTopCcmtsDhcpGiAddrMask());
                        break;
                    case 2:
                        hostGiAddr.add(giaddr.getTopCcmtsDhcpGiAddress());
                        hostGiAddrMask.add(giaddr.getTopCcmtsDhcpGiAddrMask());
                        break;
                    case 3:
                        mtaGiAddr.add(giaddr.getTopCcmtsDhcpGiAddress());
                        mtaGiAddrMask.add(giaddr.getTopCcmtsDhcpGiAddrMask());
                        break;
                    case 4:
                        stbGiAddr.add(giaddr.getTopCcmtsDhcpGiAddress());
                        stbGiAddrMask.add(giaddr.getTopCcmtsDhcpGiAddrMask());
                        break;
                    }
                }

            } else {
                if (!"0.0.0.0".equals(bundle.getVirtualPrimaryIpAddr())) {
                    cmGiAddr.add(bundle.getVirtualPrimaryIpAddr());
                    cmGiAddrMask.add(bundle.getVirtualPrimaryIpMask());
                }
                List<String> giAddr = new ArrayList<String>();
                List<String> giAddrMask = new ArrayList<String>();
                for (CmcDhcpIntIp virtualIp : virtualIpList) {
                    giAddr.add(virtualIp.getTopCcmtsDhcpIntIpAddr());
                    giAddrMask.add(virtualIp.getTopCcmtsDhcpIntIpMask());
                    break;
                }
                hostGiAddr = giAddr;
                mtaGiAddr = giAddr;
                stbGiAddr = giAddr;
                hostGiAddrMask = giAddrMask;
                mtaGiAddrMask = giAddrMask;
                stbGiAddrMask = giAddrMask;
            }
            cmRelayConfig.setTopCcmtsDhcpGiAddress(cmGiAddr);
            hostRelayConfig.setTopCcmtsDhcpGiAddress(hostGiAddr);
            mtaRelayConfig.setTopCcmtsDhcpGiAddress(mtaGiAddr);
            stbRelayConfig.setTopCcmtsDhcpGiAddress(stbGiAddr);
            cmRelayConfig.setTopCcmtsDhcpGiAddrMask(cmGiAddrMask);
            hostRelayConfig.setTopCcmtsDhcpGiAddrMask(hostGiAddrMask);
            mtaRelayConfig.setTopCcmtsDhcpGiAddrMask(mtaGiAddrMask);
            stbRelayConfig.setTopCcmtsDhcpGiAddrMask(stbGiAddrMask);
            // 组织option60数据
            List<CmcDhcpOption60> option60List = cmcDhcpDao.getCmcDhcpOption60List(map);
            List<String> cmOption60 = new ArrayList<String>();
            List<String> hostOption60 = new ArrayList<String>();
            List<String> mtaOption60 = new ArrayList<String>();
            List<String> stbOption60 = new ArrayList<String>();
            for (CmcDhcpOption60 option60 : option60List) {
                String str = option60.getTopCcmtsDhcpOption60Str();
                switch (option60.getTopCcmtsDhcpOption60DeviceType()) {
                case 1:
                    cmOption60.add(str);
                    break;
                case 2:
                    hostOption60.add(str);
                    break;
                case 3:
                    mtaOption60.add(str);
                    break;
                case 4:
                    stbOption60.add(str);
                    break;
                }
            }
            cmRelayConfig.setTopCcmtsDhcpOption60Str(cmOption60);
            hostRelayConfig.setTopCcmtsDhcpOption60Str(hostOption60);
            mtaRelayConfig.setTopCcmtsDhcpOption60Str(mtaOption60);
            stbRelayConfig.setTopCcmtsDhcpOption60Str(stbOption60);
            // 组织Server内容
            List<CmcDhcpServerConfig> serverList = cmcDhcpDao.getCmcDhcpServerList(map);
            List<String> cmServer = new ArrayList<String>();
            int cmSize = 0;
            List<String> hostServer = new ArrayList<String>();
            int hostSize = 0;
            List<String> mtaServer = new ArrayList<String>();
            int mtaSize = 0;
            List<String> stbServer = new ArrayList<String>();
            int stbSize = 0;
            for (CmcDhcpServerConfig server : serverList) {
                String serverStr = server.getTopCcmtsDhcpHelperIpAddr();
                switch (server.getTopCcmtsDhcpHelperDeviceType()) {
                case 1:
                    cmServer.add(serverStr);
                    cmSize++;
                    break;
                case 2:
                    hostServer.add(serverStr);
                    hostSize++;
                    break;
                case 3:
                    mtaServer.add(serverStr);
                    mtaSize++;
                    break;
                case 4:
                    stbServer.add(serverStr);
                    stbSize++;
                    break;
                default:
                    cmServer.add(serverStr);
                    hostServer.add(serverStr);
                    mtaServer.add(serverStr);
                    stbServer.add(serverStr);
                }
            }
            cmRelayConfig.setTopCcmtsDhcpHelperIpAddr(cmServer);
            cmRelayConfig.setServerSize(cmSize);
            hostRelayConfig.setTopCcmtsDhcpHelperIpAddr(hostServer);
            hostRelayConfig.setServerSize(hostSize);
            mtaRelayConfig.setTopCcmtsDhcpHelperIpAddr(mtaServer);
            mtaRelayConfig.setServerSize(mtaSize);
            stbRelayConfig.setTopCcmtsDhcpHelperIpAddr(stbServer);
            stbRelayConfig.setServerSize(stbSize);
            // 设置relayConfig
            relayConfigList.add(cmRelayConfig);
            relayConfigList.add(hostRelayConfig);
            relayConfigList.add(mtaRelayConfig);
            relayConfigList.add(stbRelayConfig);
            map.remove("topCcmtsDhcpBundleInterface");
        }
        return relayConfigList;
    }

    @Override
    public void addDhcpRelayConfig(Long entityId, DhcpRelayConfigSetting setting) {
        SnmpParam snmp = getSnmpParamByEntityId(entityId);
        CmcFacade cmcFacade = this.getCmcFacade(snmp.getIpAddress());
        // 1.添加Bundle
        addDhcpBundle(snmp, cmcFacade, setting.getCmcDhcpBundle());
        // 2.添加中继IP
        addDhcpGiAddr(snmp, cmcFacade, setting.getCmcDhcpGiAddr());
        // 3.添加DHCP Server
        addDhcpServer(snmp, cmcFacade, setting.getCmcDhcpServer());
        // 4.添加Option60
        addDhcpOption60(snmp, cmcFacade, setting.getCmcDhcpOption60());
        modifyDhcpPacketVlan(snmp, cmcFacade, setting.getCmcDhcpPacketVlan());

    }

    @Override
    public void modifyDhcpRelayConfig(Long entityId, DhcpRelayConfigSetting setting) {
        SnmpParam snmp = getSnmpParamByEntityId(entityId);
        CmcFacade cmcFacade = this.getCmcFacade(snmp.getIpAddress());
        Map<String, Object> map = new HashMap<String, Object>();
        result = new HashMap<String, Object>();
        CmcDhcpBundle bundle = setting.getCmcDhcpBundle();
        map.put("entityId", entityId);
        map.put("topCcmtsDhcpBundleInterface", setting.getBundleInterface());
        try {
            if (cmcDhcpDao.getCmcDhcpBundle(map) != null) {
                bundle.setTopCcmtsDhcpBundleStatus(1);
                // 1.修改设置
                modifyDhcpBundle(snmp, cmcFacade, setting.getCmcDhcpBundle());
            } else {
                // 添加bundle
                addDhcpBundle(snmp, cmcFacade, setting.getCmcDhcpBundle());
            }
            result.put("bundle", bundle);
        } catch (Exception e) {
            logger.error("", e);
            result.put("errorType", "bundle");
            result.put("error", bundle);
            throw new SetDhcpRelayFailException("Set DhcpBundle Failed!", e, result);
        }
        modifyDhcpGiAddr(snmp, cmcFacade, setting.getCmcDhcpGiAddr(), entityId, setting.getBundleInterface());

        // 2.删除设置
        deleteDhcpOption60(snmp, cmcFacade, setting.getDelOption60());
        deleteDhcpServer(snmp, cmcFacade, setting.getDelServerList());
        deleteDhcpVirtualIp(snmp, cmcFacade, setting.getBundleInterface(), setting.getDelVirtualIp());

        // 3.新增设置
        addDhcpServer(snmp, cmcFacade, setting.getCmcDhcpServer());
        addDhcpOption60(snmp, cmcFacade, setting.getCmcDhcpOption60());
        addDhcpVirtualIp(snmp, cmcFacade, setting.getVirtualIp());
        List<CmcDhcpPacketVlan> modifyVlanList = new ArrayList<CmcDhcpPacketVlan>();
        List<CmcDhcpPacketVlan> deleteVlanList = new ArrayList<CmcDhcpPacketVlan>();
        for (CmcDhcpPacketVlan vlan : setting.getCmcDhcpPacketVlan()) {
            if (vlan.getTopCcmtsDhcpTagVlan().intValue() == 0) {
                deleteVlanList.add(vlan);
            } else {
                modifyVlanList.add(vlan);
            }
        }
        modifyDhcpPacketVlan(snmp, cmcFacade, modifyVlanList);
        deleteDhcpPacketVlan(snmp, cmcFacade, deleteVlanList);

    }

    @Override
    public void deleteDhcpRelayConfig(Long entityId, String bundleId) {
        SnmpParam snmp = getSnmpParamByEntityId(entityId);
        CmcFacade cmcFacade = this.getCmcFacade(snmp.getIpAddress());
        CmcDhcpBundle bundle = new CmcDhcpBundle();
        bundle.setTopCcmtsDhcpBundleInterface(bundleId);
        bundle.setTopCcmtsDhcpBundleStatus(6);
        cmcFacade.modifyDhcpBundleInfo(snmp, bundle);
        // 修改数据库
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("entityId", entityId);
        map.put("topCcmtsDhcpBundleInterface", bundle.getTopCcmtsDhcpBundleInterface());
        cmcDhcpDao.deleteDhcpBundle(map);
    }

    @Override
    public void addDhcpOption60(Long entityId, CmcDhcpOption60 option60) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("entityId", entityId);
        map.put("topCcmtsDhcpBundleInterface", option60.getTopCcmtsDhcpBundleInterface());
        map.put("deviceType", option60.getTopCcmtsDhcpOption60DeviceType());
        int number = cmcDhcpDao.getCmcDhcpOption60List(map).size();
        option60.setTopCcmtsDhcpOption60Index(number + 1);
        SnmpParam snmp = getSnmpParamByEntityId(entityId);
        CmcFacade cmcFacade = this.getCmcFacade(snmp.getIpAddress());
        cmcFacade.modifyDhcpOption60Info(snmp, option60);
        // 修改数据库
        option60.setEntityId(entityId);
        cmcDhcpDao.addDhcpOption60(option60);

    }

    @Override
    public void addDhcpServer(Long entityId, CmcDhcpServerConfig server) {
        SnmpParam snmp = getSnmpParamByEntityId(entityId);
        CmcFacade cmcFacade = this.getCmcFacade(snmp.getIpAddress());
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("entityId", snmp.getEntityId());
        map.put("topCcmtsDhcpBundleInterface", server.getTopCcmtsDhcpBundleInterface());
        map.put("deviceType", server.getTopCcmtsDhcpHelperDeviceType());
        int number = cmcDhcpDao.getCmcDhcpServerList(map).size();
        server.setTopCcmtsDhcpHelperIndex(number + 1);
        cmcFacade.modifyDhcpServerInfo(snmp, server);
        // 修改数据库
        server.setEntityId(entityId);
        cmcDhcpDao.addDhcpServer(server);
    }

    /**
     * 新建一个Bundle
     * 
     * @param bundle
     */
    private void addDhcpBundle(SnmpParam snmp, CmcFacade cmcFacade, CmcDhcpBundle bundle) {
        cmcFacade.modifyDhcpBundleInfo(snmp, bundle);
        // Modify DB
        bundle.setEntityId(snmp.getEntityId());
        cmcDhcpDao.addDhcpBundle(bundle);
    }

    /**
     * 修改Bundle基本配置
     * 
     * @param bundle
     */
    private void modifyDhcpBundle(SnmpParam snmp, CmcFacade cmcFacade, CmcDhcpBundle bundle) {
        cmcFacade.modifyDhcpBundleInfo(snmp, bundle);
        // Modify DB
        cmcDhcpDao.updateDhcpBundle(bundle);
    }

    /**
     * 添加Giaddr
     * 
     * @param list
     */
    private void addDhcpGiAddr(SnmpParam snmp, CmcFacade cmcFacade, List<CmcDhcpGiAddr> list) {
        if (list != null && list.size() > 0) {
            List<CmcDhcpGiAddr> giAddrList = new ArrayList<CmcDhcpGiAddr>();
            for (CmcDhcpGiAddr giAddr : list) {
                // 修改Giaddr表
                CmcDhcpGiAddr giAddrModified = cmcFacade.modifyDhcpGiAddrInfo(snmp, giAddr);
                giAddrList.add(giAddrModified);

            }
            // Modify DB
            cmcDhcpDao.batchInsertDhcpGiaddr(snmp.getEntityId(), list);
        }

    }

    /**
     * 修改Giaddr
     * 
     * @param giAddr
     */
    private void modifyDhcpGiAddr(SnmpParam snmp, CmcFacade cmcFacade, List<CmcDhcpGiAddr> list, Long entityId,
            String bundleInterface) {
        if (list != null && list.size() > 0) {
            List<CmcDhcpGiAddr> insertList = new ArrayList<CmcDhcpGiAddr>();
            List<CmcDhcpGiAddr> updateList = new ArrayList<CmcDhcpGiAddr>();
            List<CmcDhcpGiAddr> deleteList = new ArrayList<CmcDhcpGiAddr>();
            List<CmcDhcpGiAddr> success = new ArrayList<CmcDhcpGiAddr>();
            result.put("giaddr", success);
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("entityId", entityId);
            map.put("topCcmtsDhcpBundleInterface", bundleInterface);
            List<CmcDhcpGiAddr> all = cmcDhcpDao.getCmcDhcpGiAddrList(map);
            int length = all.size();
            int i;
            for (CmcDhcpGiAddr giAddr : list) {
                for (i = 0; i < length; i++) {
                    CmcDhcpGiAddr item = all.get(i);
                    if (giAddr.getTopCcmtsDhcpGiAddrDeviceType().equals(item.getTopCcmtsDhcpGiAddrDeviceType())) {
                        updateList.add(giAddr);
                        break;
                    }
                }
                if (i >= length) {
                    insertList.add(giAddr);
                }
            }
            length = list.size();
            for (CmcDhcpGiAddr giAddr : all) {
                for (i = 0; i < length; i++) {
                    CmcDhcpGiAddr item = list.get(i);
                    if (giAddr.getTopCcmtsDhcpGiAddrDeviceType().equals(item.getTopCcmtsDhcpGiAddrDeviceType())) {
                        break;
                    }
                }
                if (i >= length) {
                    deleteList.add(giAddr);
                }
            }

            for (CmcDhcpGiAddr giAddr : insertList) {
                try {
                    giAddr.setTopCcmtsDhcpGiAddrStatus(RowStatus.CREATE_AND_GO);
                    cmcFacade.modifyDhcpGiAddrInfo(snmp, giAddr);
                    success.add(giAddr);
                } catch (Exception e) {
                    result.put("errorType", "giaddr");
                    result.put("error", giAddr);
                    logger.error("", e);
                    throw new SetDhcpRelayFailException("Set DhcpBundle Failed!", e, result);
                }
            }
            for (CmcDhcpGiAddr giAddr : updateList) {
                try {
                    giAddr.setTopCcmtsDhcpGiAddrStatus(RowStatus.ACTIVE);
                    cmcFacade.modifyDhcpGiAddrInfo(snmp, giAddr);
                    success.add(giAddr);
                } catch (Exception e) {
                    result.put("errorType", "giaddr");
                    result.put("error", giAddr);
                    logger.error("", e);
                    throw new SetDhcpRelayFailException("Set DhcpBundle Failed!", e, result);
                }
            }
            for (CmcDhcpGiAddr giAddr : deleteList) {
                try {
                    giAddr.setTopCcmtsDhcpGiAddrStatus(RowStatus.DESTORY);
                    giAddr.setTopCcmtsDhcpGiAddress(null);
                    cmcFacade.modifyDhcpGiAddrInfo(snmp, giAddr);
                    success.add(giAddr);
                } catch (Exception e) {
                    result.put("errorType", "giaddr");
                    result.put("error", giAddr);
                    logger.error("", e);
                    throw new SetDhcpRelayFailException("Set DhcpBundle Failed!", e, result);
                }
            }
            // Modify DB
            cmcDhcpDao.batchInsertDhcpGiaddr(entityId, insertList);
            cmcDhcpDao.batchUpdateDhcpGiaddr(entityId, updateList);
            cmcDhcpDao.batchDeleteDhcpGiAddr(entityId, deleteList);
        }

    }

    /**
     * 添加Option 60
     * 
     * @param list
     */
    private void addDhcpOption60(SnmpParam snmp, CmcFacade cmcFacade, List<CmcDhcpOption60> list) {
        if (list != null && list.size() > 0) {
            List<CmcDhcpOption60> option60List = new ArrayList<CmcDhcpOption60>();
            List<CmcDhcpOption60> success = new ArrayList<CmcDhcpOption60>();
            result.put("addOption", success);
            Long entityId = snmp.getEntityId();
            if (list.size() == 0) {
                return;
            }
            for (CmcDhcpOption60 option60 : list) {
                try {
                    int index = CmcIndexUtils.generateIndex(cmcDhcpDao.selectDhcpOption60Index(entityId,
                            option60.getTopCcmtsDhcpBundleInterface(), option60.getTopCcmtsDhcpOption60DeviceType()));
                    option60.setTopCcmtsDhcpOption60Index(index);
                    CmcDhcpOption60 option60Modified = cmcFacade.modifyDhcpOption60Info(snmp, option60);
                    option60List.add(option60Modified);
                    success.add(option60);
                } catch (Exception e) {
                    result.put("errorType", "addOption");
                    result.put("error", option60);
                    logger.error("", e);
                    throw new SetDhcpRelayFailException("Set addOption Failed!", e, result);
                }
                // 修改数据库
                cmcDhcpDao.insertDhcpOption60(entityId, option60);
            }
        }
    }

    /**
     * 删除Option60
     * 
     * @param list
     */
    private void deleteDhcpOption60(SnmpParam snmp, CmcFacade cmcFacade, List<Long> list) {
        if (list != null && list.size() > 0) {
            List<CmcDhcpOption60> option60List = new ArrayList<CmcDhcpOption60>();
            List<CmcDhcpOption60> success = new ArrayList<CmcDhcpOption60>();
            Map<String, Object> map = new HashMap<String, Object>();
            result.put("deleteOption", success);
            map.put("entityId", snmp.getEntityId());
            for (Long option60Id : list) {
                map.put("option60Id", option60Id);
                CmcDhcpOption60 option60 = cmcDhcpDao.getCmcDhcpOption60(map);
                option60.setTopCcmtsDhcpOption60Status(RowStatus.DESTORY);
                CmcDhcpOption60 clone = option60.clone();
                try {
                    option60.setTopCcmtsDhcpOption60Str(null);
                    cmcFacade.modifyDhcpOption60Info(snmp, option60);
                    option60List.add(option60);
                    map.remove("option60Id");
                    success.add(clone);
                } catch (Exception e) {
                    logger.error("", e);
                    result.put("errorType", "deleteOption");
                    result.put("error", clone);
                    throw new SetDhcpRelayFailException("Set DhcpBundle Failed!", e, result);
                }
            }
            // Modify DB
            cmcDhcpDao.batchDeleteDhcpOption60(snmp.getEntityId(), option60List);
        }

    }

    /**
     * 添加DHCP Server
     * 
     * @param list
     */
    private void addDhcpServer(SnmpParam snmp, CmcFacade cmcFacade, List<CmcDhcpServerConfig> list) {
        if (list != null && list.size() > 0) {
            List<CmcDhcpServerConfig> serverModifiedList = new ArrayList<CmcDhcpServerConfig>();
            List<CmcDhcpServerConfig> success = new ArrayList<CmcDhcpServerConfig>();
            result.put("addServer", success);
            Long entityId = snmp.getEntityId();
            for (CmcDhcpServerConfig server : list) {
                try {
                    int index = CmcIndexUtils.generateIndex(cmcDhcpDao.selectDhcpServerIndex(entityId,
                            server.getTopCcmtsDhcpBundleInterface(), server.getTopCcmtsDhcpHelperDeviceType()));
                    server.setTopCcmtsDhcpHelperIndex(index);
                    CmcDhcpServerConfig serverModified = cmcFacade.modifyDhcpServerInfo(snmp, server);
                    serverModifiedList.add(serverModified);
                    success.add(server);
                } catch (Exception e) {
                    logger.error("", e);
                    result.put("errorType", "addServer");
                    result.put("error", server);
                    throw new SetDhcpRelayFailException("Set addServer Failed!", e, result);
                }
                // 修改数据库
                cmcDhcpDao.insertDhcpServer(entityId, server);
            }
        }

    }

    /**
     * 删除DHCP Server
     * 
     * @param list
     */
    private void deleteDhcpServer(SnmpParam snmp, CmcFacade cmcFacade, List<Long> list) {
        if (list != null && list.size() > 0) {
            List<CmcDhcpServerConfig> serverModifiedList = new ArrayList<CmcDhcpServerConfig>();
            List<CmcDhcpServerConfig> success = new ArrayList<CmcDhcpServerConfig>();
            result.put("deleteServer", success);
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("entityId", snmp.getEntityId());
            for (Long serverId : list) {
                map.put("helperId", serverId);
                CmcDhcpServerConfig server = cmcDhcpDao.getCmcDhcpServerConfig(map);
                CmcDhcpServerConfig clone = server.clone();
                server.setTopCcmtsDhcpHelperIpAddr(null);
                try {
                    server.setTopCcmtsDhcpHelperStatus(RowStatus.DESTORY);
                    cmcFacade.modifyDhcpServerInfo(snmp, server);
                    serverModifiedList.add(server);
                    map.remove("helperId");
                    success.add(clone);
                } catch (Exception e) {
                    logger.error("", e);
                    result.put("errorType", "deleteServer");
                    result.put("error", clone);
                    throw new SetDhcpRelayFailException("Set deleteServer Failed!", e, result);
                }
            }
            // Modify DB
            cmcDhcpDao.batchDeleteDhcpServer(snmp.getEntityId(), serverModifiedList);
        }

    }

    private void addDhcpVirtualIp(SnmpParam snmp, CmcFacade cmcFacade, List<CmcDhcpIntIp> list) {
        if (list != null && list.size() > 0) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("entityId", snmp.getEntityId());
            map.put("topCcmtsDhcpBundleInterface", list.get(0).getTopCcmtsDhcpBundleInterface());
            List<CmcDhcpIntIp> temp = cmcDhcpDao.getCmcDhcpIntIpListByMap(map);
            List<CmcDhcpIntIp> success = new ArrayList<CmcDhcpIntIp>();
            result.put("addVirtualIp", success);
            int index = 0;
            if (temp != null) {
                index = temp.size();
            }
            for (CmcDhcpIntIp virtualIp : list) {
                virtualIp.setTopCcmtsDhcpIntIpIndex(++index);
                try {
                    cmcFacade.modifyDhcpVirtralIp(snmp, virtualIp);
                    success.add(virtualIp);
                } catch (Exception e) {
                    logger.error("", e);
                    result.put("errorType", "addVirtualIp");
                    result.put("error", virtualIp);
                    throw new SetDhcpRelayFailException("Set addVirtualIp Failed!", e, result);
                }
                cmcDhcpDao.addDhcpIntIp(virtualIp);
            }

        }
    }

    private void deleteDhcpVirtualIp(SnmpParam snmp, CmcFacade cmcFacade, String bundleInterface, List<String> ipList) {
        if (ipList != null && ipList.size() > 0) {
            long entityId = snmp.getEntityId();
            List<CmcDhcpIntIp> success = new ArrayList<CmcDhcpIntIp>();
            result.put("deleteVirtualIp", success);
            for (String ip : ipList) {
                // 由于索引或自动发生变化，因此需要删除之前获取信息
                List<CmcDhcpIntIp> intIps = cmcFacade.getCmcDhcpIntIp(snmp);
                for (CmcDhcpIntIp intIp : intIps) {
                    if (intIp.getTopCcmtsDhcpIntIpAddr().trim().equals(ip.trim())) {
                        CmcDhcpIntIp clone = intIp.clone();
                        intIp.setTopCcmtsDhcpIntIpAddr(null);
                        intIp.setTopCcmtsDhcpIntIpMask(null);
                        try {
                            intIp.setTopCcmtsDhcpIntIpStatus(RowStatus.DESTORY);
                            cmcFacade.modifyDhcpVirtralIp(snmp, intIp);
                            cmcDhcpDao.deleteDhcpIntIp(entityId, bundleInterface, ip);
                            success.add(clone);
                        } catch (Exception e) {
                            logger.error("", e);
                            result.put("errorType", "deleteVirtualIp");
                            result.put("error", clone);
                            throw new SetDhcpRelayFailException("Set deleteVirtualIp Failed!", e, result);
                        }
                    }
                }
            }
        }
    }

    private void modifyDhcpPacketVlan(SnmpParam snmp, CmcFacade cmcFacade, List<CmcDhcpPacketVlan> list) {
        if (list != null) {
            Long entityId = snmp.getEntityId();
            List<CmcDhcpPacketVlan> success = new ArrayList<CmcDhcpPacketVlan>();
            result.put("modifyPacketVlan", success);
            for (CmcDhcpPacketVlan packetVlan : list) {
                try {
                    String bundleInterface = packetVlan.getTopCcmtsDhcpBundleInterface();
                    int deviceType = packetVlan.getTopCcmtsDhcpDeviceType();
                    if (cmcDhcpDao.selectDhcpPacketVlan(entityId, bundleInterface, deviceType) == null) {
                        packetVlan.setTopCcmtsDhcpPacketVlanStatus(RowStatus.CREATE_AND_GO);
                        cmcFacade.modifyDhcpPacketVlan(snmp, packetVlan);
                        cmcDhcpDao.insertDhcpPacketVlan(entityId, packetVlan);
                    } else {
                        packetVlan.setTopCcmtsDhcpPacketVlanStatus(RowStatus.ACTIVE);
                        cmcFacade.modifyDhcpPacketVlan(snmp, packetVlan);
                        cmcDhcpDao.updateDhcpPacketVlan(entityId, packetVlan);
                    }
                    success.add(packetVlan);
                } catch (Exception e) {
                    logger.error("", e);
                    result.put("errorType", "modifyPacketVlan");
                    result.put("error", packetVlan);
                    throw new SetDhcpRelayFailException("Set modifyPacketVlan Failed!", e, result);
                }
            }
        }

    }

    private void deleteDhcpPacketVlan(SnmpParam snmp, CmcFacade cmcFacade, List<CmcDhcpPacketVlan> list) {
        if (list != null) {
            Long entityId = snmp.getEntityId();
            List<CmcDhcpPacketVlan> success = new ArrayList<CmcDhcpPacketVlan>();
            result.put("deletePacketVlan", success);
            for (CmcDhcpPacketVlan packetVlan : list) {
                String bundleInterface = packetVlan.getTopCcmtsDhcpBundleInterface();
                int deviceType = packetVlan.getTopCcmtsDhcpDeviceType();
                CmcDhcpPacketVlan clone = cmcDhcpDao.selectDhcpPacketVlan(entityId, bundleInterface, deviceType);
                if (clone != null) {
                    try {
                        packetVlan.setTopCcmtsDhcpTagVlan(null);
                        packetVlan.setTopCcmtsDhcpTagPriority(null);
                        packetVlan.setTopCcmtsDhcpPacketVlanStatus(RowStatus.DESTORY);
                        cmcFacade.modifyDhcpPacketVlan(snmp, packetVlan);
                        cmcDhcpDao.deleteDhcpPacketVlan(entityId, bundleInterface, deviceType);
                        success.add(clone);
                    } catch (Exception e) {
                        logger.error("", e);
                        result.put("errorType", "deletePacketVlan");
                        result.put("error", clone);
                        throw new SetDhcpRelayFailException("Set modifyPacketVlan Failed!", e, result);
                    }
                }
            }
        }

    }

    /**
     * @return the cmcDhcpDao
     */
    public CmcDhcpDao getCmcDhcpDao() {
        return cmcDhcpDao;
    }

    /**
     * @param cmcDhcpDao
     *            the cmcDhcpDao to set
     */
    public void setCmcDhcpDao(CmcDhcpDao cmcDhcpDao) {
        this.cmcDhcpDao = cmcDhcpDao;
    }

    @Override
    public void modifyDhcpBaseConfig(CmcDhcpBaseConfig cmcDhcpBaseConfigInfo) {
        SnmpParam snmp = getSnmpParamByEntityId(cmcDhcpBaseConfigInfo.getEntityId());
        CmcDhcpBaseConfig cmcDhcpBaseConfigAfterModified = getCmcFacade(snmp.getIpAddress())
                .modifyCmcDhcpRelayBaseInfo(snmp, cmcDhcpBaseConfigInfo);
        cmcDhcpBaseConfigAfterModified.setTopCcmtsDhcpMode(cmcDhcpBaseConfigAfterModified.getTopCcmtsDhcpMode()
                .substring(0, 2));
        cmcDhcpDao.batchInsertOrUpdateCC8800BCmcDhcpBaseConfig(cmcDhcpBaseConfigAfterModified,
                cmcDhcpBaseConfigInfo.getEntityId());
    }

    @Override
    public CmcDhcpBundle getCmcDhcpBundle(Long entityId, String bundleInterface) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("entityId", entityId);
        map.put("topCcmtsDhcpBundleInterface", bundleInterface);
        return cmcDhcpDao.getCmcDhcpBundle(map);
    }

    @Override
    public List<CmcDhcpServerConfig> getCmcDhcpServerList(Long entityId, String bundleInterface) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("entityId", entityId);
        map.put("topCcmtsDhcpBundleInterface", bundleInterface);
        return cmcDhcpDao.getCmcDhcpServerList(map);
    }

    @Override
    public List<CmcDhcpGiAddr> getCmcDhcpGiAddrList(Long entityId, String bundleInterface) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("entityId", entityId);
        map.put("topCcmtsDhcpBundleInterface", bundleInterface);
        List<CmcDhcpGiAddr> giAddrs = cmcDhcpDao.getCmcDhcpGiAddrList(map);
        CmcDhcpBundle bundle = cmcDhcpDao.getCmcDhcpBundle(map);
        for (CmcDhcpGiAddr giAddr : giAddrs) {
            if (giAddr.getTopCcmtsDhcpGiAddress().equals(bundle.getVirtualPrimaryIpAddr())) {
                giAddr.setTopCcmtsDhcpGiAddrMask(bundle.getVirtualPrimaryIpMask());
            }
        }
        return giAddrs;
    }

    @Override
    public List<CmcDhcpIntIp> getCmcDhcpIntIpList(Long entityId, String bundleInterface) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("entityId", entityId);
        map.put("topCcmtsDhcpBundleInterface", bundleInterface);
        List<CmcDhcpIntIp> virtualIps = new ArrayList<CmcDhcpIntIp>();
        CmcDhcpBundle bundle = cmcDhcpDao.getCmcDhcpBundle(map);
        if (!"0.0.0.0".equals(bundle.getVirtualPrimaryIpAddr())) {
            CmcDhcpIntIp virtualIp = new CmcDhcpIntIp();
            virtualIp.setEntityId(bundle.getEntityId());
            virtualIp.setTopCcmtsDhcpBundleInterface(bundleInterface);
            virtualIp.setTopCcmtsDhcpIntIpAddr(bundle.getVirtualPrimaryIpAddr());
            virtualIp.setTopCcmtsDhcpIntIpMask(bundle.getVirtualPrimaryIpMask());
            virtualIp.setTopCcmtsDhcpIntIpIndex(0);
            virtualIps.add(virtualIp);
        }
        virtualIps.addAll(cmcDhcpDao.getCmcDhcpIntIpListByMap(map));
        return virtualIps;
    }

    @Override
    public List<CmcDhcpOption60> getCmcDhcpOption60List(Long entityId, String bundleInterface) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("entityId", entityId);
        map.put("topCcmtsDhcpBundleInterface", bundleInterface);
        return cmcDhcpDao.getCmcDhcpOption60List(map);
    }

    @Override
    public List<CmcDhcpIntIp> getDhcpVirtualIPList(Long entityId, String bundleInterface) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("entityId", entityId);
        map.put("topCcmtsDhcpBundleInterface", bundleInterface);
        return cmcDhcpDao.getCmcDhcpIntIpListByMap(map);
    }

    @Override
    public List<CmcDhcpPacketVlan> getCmcDhcpPacketVlanList(Long entityId, String bundleInterface) {
        return cmcDhcpDao.selectDhcpPacketVlanList(entityId, bundleInterface);
    }

    @Override
    public List<String> getCmcDhcpBundleEndList(Long entityId) {
        List<CmcDhcpBundle> bundleList = cmcDhcpDao.getCmcDhcpBundleList(entityId);
        List<String> bundleEndList = new ArrayList<String>();
        for (CmcDhcpBundle bundle : bundleList) {
            bundleEndList.add(bundle.getTopCcmtsDhcpBundleInterface().replace("bundle", ""));
        }
        return bundleEndList;
    }

    @Override
    public List<CmcDhcpIntIp> getVirtulIpList(Long entityId) {
        List<CmcDhcpIntIp> virtualIpList = cmcDhcpDao.getCmcDhcpIntIpList(entityId);
        List<CmcDhcpBundle> bundleList = cmcDhcpDao.getCmcDhcpBundleList(entityId);
        for (CmcDhcpBundle bundle : bundleList) {
            if (!"0.0.0.0".equals(bundle.getVirtualPrimaryIpAddr())) {
                CmcDhcpIntIp virtualIp = new CmcDhcpIntIp();
                virtualIp.setTopCcmtsDhcpBundleInterface(bundle.getTopCcmtsDhcpBundleInterface());
                virtualIp.setTopCcmtsDhcpIntIpAddr(bundle.getVirtualPrimaryIpAddr());
                virtualIp.setTopCcmtsDhcpIntIpMask(bundle.getVirtualPrimaryIpMask());
                virtualIpList.add(virtualIp);
            }
        }
        return virtualIpList;
    }

    @Override
    public void refreshDhcpBaseConfig(Long entityId) {
        SnmpParam snmp = getSnmpParamByEntityId(entityId);
        CmcFacade cmcFacade = getCmcFacade(snmp.getIpAddress());
        CmcDhcpBaseConfig baseConfig = cmcFacade.getCmcDhcpRelayBaseInfo(snmp);
        if (baseConfig != null) {
            cmcDhcpDao.batchInsertOrUpdateCC8800BCmcDhcpBaseConfig(baseConfig, entityId);
        }
    }

    @Override
    public void refreshDhcpRelayConfig(Long entityId) {
        SnmpParam snmp = getSnmpParamByEntityId(entityId);
        CmcFacade cmcFacade = getCmcFacade(snmp.getIpAddress());
        List<CmcDhcpBundle> bundles = cmcFacade.getCmcDhcpBundleInfo(snmp);
        cmcDhcpDao.batchInsertCcDhcpBundleConfig(bundles, null, entityId);
        List<CmcDhcpServerConfig> servers = cmcFacade.getCmcDhcpServerConfigInfo(snmp);
        cmcDhcpDao.batchInsertCcDhcpServerConfig(servers, null, entityId);
        List<CmcDhcpGiAddr> giaddrs = cmcFacade.getCmcDhcpGiAddrInfo(snmp);
        cmcDhcpDao.batchInsertCcDhcpGiaddrConfig(giaddrs, null, entityId);
        List<CmcDhcpOption60> option60s = cmcFacade.getCmcDhcpOption60Info(snmp);
        cmcDhcpDao.batchInsertCcDhcpOption60Config(option60s, null, entityId);
        List<CmcDhcpIntIp> intIps = cmcFacade.getCmcDhcpIntIp(snmp);
        cmcDhcpDao.batchInsertCcDhcpIntIpConfig(intIps, entityId);
        List<CmcDhcpPacketVlan> packetVlans = cmcFacade.getCmcDhcpPacketVlan(snmp);
        cmcDhcpDao.batchInsertCcDhcpPacketVlanConfig(packetVlans, entityId);
    }

    @Override
    public void insertEntityStates(CmcSynchronizedEvent event) {
        //TODO DHCP待重构，沿用之前的刷新方法
        if (event.getEntityType().equals(entityTypeService.getCcmtswithagentType())) {
            try {
                long entityId = event.getEntityId();
                refreshDhcpBaseConfig(entityId);
                refreshDhcpRelayConfig(entityId);
                logger.info("Refresh CmcDhcpRelay finish.");
            } catch (Exception e) {
                logger.error("Refresh CmcDhcpRelay Info Wrong. ", e);
            }
        }
    }

}