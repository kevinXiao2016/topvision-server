/***********************************************************************
 * $Id: UniVlanServiceImpl.java,v1.0 2013-10-25 下午4:34:53 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.vlan.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.devicesupport.version.service.DeviceVersionService;
import com.topvision.ems.epon.olt.dao.OltDao;
import com.topvision.ems.epon.olt.dao.OltSniDao;
import com.topvision.ems.epon.vlan.dao.SniVlanDao;
import com.topvision.ems.epon.vlan.domain.OltPortVlanEntry;
import com.topvision.ems.epon.vlan.domain.OltVlanAttribute;
import com.topvision.ems.epon.vlan.domain.PortVlanAttribute;
import com.topvision.ems.epon.vlan.domain.TopOltVlanConfigTable;
import com.topvision.ems.epon.vlan.domain.TopOltVlanVifPriIpTable;
import com.topvision.ems.epon.vlan.domain.TopOltVlanVifSubIpTable;
import com.topvision.ems.epon.vlan.domain.VlanAttribute;
import com.topvision.ems.epon.vlan.facade.OltVlanFacade;
import com.topvision.ems.epon.vlan.service.SniVlanService;
import com.topvision.ems.network.service.EntityService;
import com.topvision.framework.service.BaseService;
import com.topvision.framework.snmp.SnmpParam;
import com.topvision.framework.utils.EponIndex;
import com.topvision.platform.facade.FacadeFactory;
import com.topvision.platform.message.event.SynchronizedEvent;
import com.topvision.platform.message.event.SynchronizedListener;
import com.topvision.platform.message.service.MessageService;

/**
 * @author jay
 * @created @2011-10-24-19:02:29
 */
@Service("sniVlanService")
public class SniVlanServiceImpl extends BaseService implements SniVlanService, SynchronizedListener {
    @Autowired
    private SniVlanDao sniVlanDao;
    @Autowired
    private OltDao oltDao;
    @Autowired
    private FacadeFactory facadeFactory;
    @Autowired
    protected MessageService messageService;
    @Autowired
    private EntityService entityService;
    @Autowired
    private OltSniDao oltSniDao;
    @Autowired
    private DeviceVersionService deviceVersionService;

    @PostConstruct
    @Override
    public void initialize() {
        super.initialize();
        messageService.addListener(SynchronizedListener.class, this);
    }

    @PreDestroy
    @Override
    public void destroy() {
        super.destroy();
        messageService.removeListener(SynchronizedListener.class, this);
    }

    @Override
    public void insertEntityStates(SynchronizedEvent event) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(event.getEntityId());
        try {
            refreshSniVlanView(snmpParam);
            logger.info("refreshSniVlanView finish");
        } catch (Exception e) {
            logger.error("refreshSniVlanView wrong", e);
        }
        try {
            refreshSniPortVlan(snmpParam);
            logger.info("refreshSniPortVlan finish");
        } catch (Exception e) {
            logger.error("refreshSniPortVlan wrong", e);
        }
        try {
            refreshSniVlanAttribute(snmpParam);
            logger.info("refreshSniVlanAttribute finish");
        } catch (Exception e) {
            logger.error("refreshSniVlanAttribute wrong", e);
        }

        try {
            refreshVlanVifPriIp(snmpParam);
            logger.info("refreshVlanVifPriIp finish");
        } catch (Exception e) {
            logger.error("refreshVlanVifPriIp wrong", e);
        }

        try {
            refreshVlanVifSubIp(snmpParam);
            logger.info("refreshVlanVifSubIp finish");
        } catch (Exception e) {
            logger.error("refreshVlanVifSubIp wrong", e);
        }

        try {
            refreshTopMcFloodMode(snmpParam);
            logger.info("refreshTopMcFloodMode finish");
        } catch (Exception e) {
            logger.error("refreshTopMcFloodMode wrong", e);
        }
    }

    @Override
    public void setVlanPriIp(Long entityId, Integer vlanIndex, String topOltVifPriIpAddr, String topOltVifPriIpMask) {
        TopOltVlanVifPriIpTable vlanVifPriIp = new TopOltVlanVifPriIpTable();
        vlanVifPriIp.setEntityId(entityId);
        vlanVifPriIp.setTopOltVifPriIpVlanIdx(vlanIndex);
        vlanVifPriIp.setTopOltVifPriIpAddr(topOltVifPriIpAddr);
        vlanVifPriIp.setTopOltVifPriIpMask(topOltVifPriIpMask);
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        TopOltVlanVifPriIpTable newObject = getOltVlanFacade(snmpParam.getIpAddress()).setVlanPriIp(snmpParam,
                vlanVifPriIp);
        if (newObject != null) {
            sniVlanDao.setVlanPriIp(vlanVifPriIp);
        }
        //Add by Rod 同步IP信息表
        entityService.updateEntityAddress(entityId);
    }

    @Override
    public void modifyVlanVifPriIp(Long entityId, Integer vlanIndex, String topOltVifPriIpAddr,
            String topOltVifPriIpMask) {
        TopOltVlanVifPriIpTable vlanVifPriIp = new TopOltVlanVifPriIpTable();
        vlanVifPriIp.setEntityId(entityId);
        vlanVifPriIp.setTopOltVifPriIpVlanIdx(vlanIndex);
        vlanVifPriIp.setTopOltVifPriIpAddr(topOltVifPriIpAddr);
        vlanVifPriIp.setTopOltVifPriIpMask(topOltVifPriIpMask);
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        TopOltVlanVifPriIpTable newObject = getOltVlanFacade(snmpParam.getIpAddress()).modifyVlanVifPriIp(snmpParam,
                vlanVifPriIp);
        if (newObject != null) {
            sniVlanDao.modifyVlanVifPriIp(vlanVifPriIp);
        }
        //Add by Rod 同步IP信息表
        entityService.updateEntityAddress(entityId);
    }

    @Override
    public void deleteVlanVif(Long entityId, Integer vlanIndex) {
        TopOltVlanVifPriIpTable vlanVifPriIp = new TopOltVlanVifPriIpTable();
        vlanVifPriIp.setEntityId(entityId);
        vlanVifPriIp.setTopOltVifPriIpVlanIdx(vlanIndex);
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        getOltVlanFacade(snmpParam.getIpAddress()).deleteVlanVif(snmpParam, vlanVifPriIp);
        sniVlanDao.deleteVlanVif(vlanVifPriIp);
        // 删除vlan虚接口同时清除该vlan虚接口的子IP
        sniVlanDao.deleteVlanVifSubIps(entityId, vlanIndex);
        //Add by Rod 同步IP信息表
        entityService.updateEntityAddress(entityId);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void addVlanVifSubIp(Long entityId, Integer vlanIndex, String topOltVifSubIpAddr, String topOltVifSubIpMask) {
        TopOltVlanVifSubIpTable vlanVifSubIp = new TopOltVlanVifSubIpTable();
        vlanVifSubIp.setEntityId(entityId);
        vlanVifSubIp.setTopOltVifSubIpVlanIdx(vlanIndex);
        //获取可用的index
        List<Integer> indexList = new ArrayList<Integer>(Arrays.asList(TopOltVlanVifSubIpTable.indexs));
        List<Integer> usedIndex = sniVlanDao.queryUsedSubIpIndex(entityId, vlanIndex);
        indexList.removeAll(usedIndex);
        vlanVifSubIp.setTopOltVifSubIpSeqIdx(indexList.get(0));
        vlanVifSubIp.setTopOltVifSubIpAddr(topOltVifSubIpAddr);
        vlanVifSubIp.setTopOltVifSubIpMask(topOltVifSubIpMask);
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        TopOltVlanVifSubIpTable newObject = getOltVlanFacade(snmpParam.getIpAddress()).addVlanVifSubIp(snmpParam,
                vlanVifSubIp);
        if (newObject != null) {
            sniVlanDao.addVlanVifSubIp(vlanVifSubIp);
        }
        //Add by Rod 同步IP信息表
        entityService.updateEntityAddress(entityId);
    }

    @Override
    public void modifyVlanVifSubIp(Long entityId, Integer vlanIndex, Integer topOltVifSubIpSeqIdx,
            String topOltVifSubIpAddr, String topOltVifSubIpMask) {
        TopOltVlanVifSubIpTable vlanVifSubIp = new TopOltVlanVifSubIpTable();
        vlanVifSubIp.setEntityId(entityId);
        vlanVifSubIp.setTopOltVifSubIpVlanIdx(vlanIndex);
        vlanVifSubIp.setTopOltVifSubIpSeqIdx(topOltVifSubIpSeqIdx);
        vlanVifSubIp.setTopOltVifSubIpAddr(topOltVifSubIpAddr);
        vlanVifSubIp.setTopOltVifSubIpMask(topOltVifSubIpMask);
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        TopOltVlanVifSubIpTable newObject = getOltVlanFacade(snmpParam.getIpAddress()).modifyVlanVifSubIp(snmpParam,
                vlanVifSubIp);
        if (newObject != null) {
            sniVlanDao.modifyVlanVifSubIp(vlanVifSubIp);
        }
        //Add by Rod 同步IP信息表
        entityService.updateEntityAddress(entityId);
    }

    @Override
    public void deleteVlanVifSubIp(Long entityId, Integer vlanIndex, Integer topOltVifSubIpSeqIdx) {
        TopOltVlanVifSubIpTable vlanVifSubIp = new TopOltVlanVifSubIpTable();
        vlanVifSubIp.setEntityId(entityId);
        vlanVifSubIp.setTopOltVifSubIpVlanIdx(vlanIndex);
        vlanVifSubIp.setTopOltVifSubIpSeqIdx(topOltVifSubIpSeqIdx);
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        getOltVlanFacade(snmpParam.getIpAddress()).deleteVlanVifSubIp(snmpParam, vlanVifSubIp);
        sniVlanDao.deleteVlanVifSubIp(vlanVifSubIp);
        //Add by Rod 同步IP信息表
        entityService.updateEntityAddress(entityId);
    }

    @Override
    public TopOltVlanVifPriIpTable getVlanVifPriIp(Long entityId, Integer vlanIndex) {
        return sniVlanDao.getVlanVifPriIp(entityId, vlanIndex);
    }

    @Override
    public List<TopOltVlanVifPriIpTable> getVlanVifPriIpList(Long entityId) {
        return sniVlanDao.getVlanVifPriIpList(entityId);
    }

    @Override
    public List<TopOltVlanVifSubIpTable> getVlanVifSubIp(Long entityId, Integer vlanIndex) {
        return sniVlanDao.getVlanVifSubIp(entityId, vlanIndex);
    }

    @Override
    public PortVlanAttribute getSniPortVlanAttribute(Long entityId, Long sniIndex) {
        return sniVlanDao.getSniPortVlanAttribute(entityId, sniIndex);
    }

    @Override
    public void updateSniPortVlanAttribute(Long sniId, Integer vlanPVid, Integer vlanTagPriority, String vlanTagTpid,
            Integer vlanMode, Long sniIndex, Long entityId) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        // 调用facade的modifyPortVlanAttributes方法修改端口Vlan基本属性
        Boolean isSupport = deviceVersionService.isFunctionSupported(entityId, "sniVlanEntry");
        PortVlanAttribute portVlanAttribute = sniVlanDao.getSniPortVlanAttribute(entityId, sniIndex);
        if (isSupport) {
            OltPortVlanEntry oltPortVlanEntry = new OltPortVlanEntry();
            oltPortVlanEntry.setPortIndex(sniIndex);
            oltPortVlanEntry.setEntityId(entityId);
            oltPortVlanEntry.setVlanMode(vlanMode);
            oltPortVlanEntry.setVlanPVid(vlanPVid);
            oltPortVlanEntry.setVlanTagPriority(vlanTagPriority);
            getOltVlanFacade(snmpParam.getIpAddress()).modifyOltPortVlanEntry(snmpParam, oltPortVlanEntry);
            portVlanAttribute.setVlanPVid(vlanPVid);
            portVlanAttribute.setVlanMode(vlanMode);
            portVlanAttribute.setVlanTagPriority(vlanTagPriority);
        } else {
            // cfi的值必须设置为null，否则无法设置成功
            portVlanAttribute.setVlanTagCfi(null);
            portVlanAttribute.setVlanPVid(vlanPVid);
            portVlanAttribute.setVlanTagPriority(vlanTagPriority);
            // portVlanAttribute.setVlanTagTpid(null);
            portVlanAttribute.setVlanTagTpidString(vlanTagTpid);
            portVlanAttribute.setVlanMode(null);
            getOltVlanFacade(snmpParam.getIpAddress()).updateSniPortAttribute(snmpParam, portVlanAttribute);
        }
        sniVlanDao.updateSniPortAttribute(portVlanAttribute);
    }

    @Override
    public void refreshVlanAttributeByChangePVid(Long entityId, List<Long> portIndex, Integer pvid, SnmpParam param) {
        VlanAttribute vlanAttribute = sniVlanDao.getOltVlanConfig(entityId, pvid);
        List<Long> untaggedPortIndexList = vlanAttribute.getUntaggedPortIndexList();
        untaggedPortIndexList.addAll(portIndex);

        List<Long> taggedPortIndexList = vlanAttribute.getTaggedPortIndexList();
        for (int i = 0; i < taggedPortIndexList.size(); i++) {
            if (portIndex.contains(taggedPortIndexList.get(i))) {
                taggedPortIndexList.remove(i);
            }
        }
        updateTagStatus(entityId, pvid, taggedPortIndexList, untaggedPortIndexList, param);
        refreshSniVlanAttribute(param);
    }

    @Override
    public void refreshSniPortVlanAttribute(Long entityId, Long sniIndex) {
        // 通过sniId从数据库取得数据，数据中有sniIndex
        // PortVlanAttribute portVlanAttribute = sniVlanDao.getSniPortVlanAttribute(sniId);
        // 从设备获取数据
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        // 调用方法获取sni口VLAN基本属性
        refreshSniPortVlan(snmpParam);// TODO 需要单个SNI更新方法
    }

    @Override
    public void modifyVlanName(Long entityId, Integer vlanIndex, String oltVlanName, Integer topMcFloodMode) {
        VlanAttribute vlanAttribute = new VlanAttribute();
        vlanAttribute.setEntityId(entityId);
        vlanAttribute.setDeviceNo(1l);
        vlanAttribute.setVlanIndex(vlanIndex);
        vlanAttribute.setOltVlanName(oltVlanName);
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        VlanAttribute vlanAttr = getOltVlanFacade(snmpParam.getIpAddress()).modifyVlanName(snmpParam, vlanAttribute);

        TopOltVlanConfigTable topOltVlanConfig = new TopOltVlanConfigTable();
        topOltVlanConfig.setEntityId(entityId);
        topOltVlanConfig.setVlanIndex(vlanIndex);
        topOltVlanConfig.setTopMcFloodMode(topMcFloodMode);
        TopOltVlanConfigTable topOltVlanConf = getOltVlanFacade(snmpParam.getIpAddress()).modifyMcFloodMode(snmpParam,
                topOltVlanConfig);

        if (vlanAttr != null && topOltVlanConf != null) {
            sniVlanDao.modifyVlanName(entityId, vlanIndex, oltVlanName, topMcFloodMode);
        }
    }

    @Override
    public OltVlanAttribute getOltVlanGlobalInfo(Long entityId) {
        return sniVlanDao.getOltVlanGlobalInfo(entityId);
    }

    @Override
    public List<VlanAttribute> getOltVlanConfigList(Long entityId) {
        return sniVlanDao.getOltVlanConfigList(entityId);
    }

    @Override
    public void addOltVlan(VlanAttribute vlanAttribute) {
        // 在设备上新增VLAN
        Integer topMcFloodMode = vlanAttribute.getTopMcFloodMode();
        vlanAttribute.setTopMcFloodMode(null);
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(vlanAttribute.getEntityId());
        VlanAttribute vlanAttr = getOltVlanFacade(snmpParam.getIpAddress()).addOltVlan(snmpParam, vlanAttribute);

        TopOltVlanConfigTable topOltVlanConfig = new TopOltVlanConfigTable();
        topOltVlanConfig.setEntityId(vlanAttribute.getEntityId());
        topOltVlanConfig.setVlanIndex(vlanAttribute.getVlanIndex());
        topOltVlanConfig.setTopMcFloodMode(topMcFloodMode);
        if (topMcFloodMode != 2) {
            getOltVlanFacade(snmpParam.getIpAddress()).modifyMcFloodMode(snmpParam, topOltVlanConfig);
        }
        if (vlanAttr != null) {
            vlanAttribute.setTopMcFloodMode(topMcFloodMode);
            sniVlanDao.addOltVlan(vlanAttribute);
        }
        // 更新全局VLAN属性
        refreshSniVlanView(snmpParam);
    }

    @Override
    public List<Integer> deleteOltVlan(Long entityId, String vidListStr) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        List<Integer> vidList = changeToArray(vidListStr);
        List<Integer> delFailList = new ArrayList<Integer>();
        for (Integer vlanIndex : vidList) {
            VlanAttribute vlanAttribute = new VlanAttribute();
            vlanAttribute.setEntityId(entityId);
            vlanAttribute.setVlanIndex(vlanIndex);
            try {
                // 在设备上删除VLAN
                getOltVlanFacade(snmpParam.getIpAddress()).deleteOltVlan(snmpParam, vlanAttribute);
                // 在设备上删除成功后，删除数据库
                sniVlanDao.deleteOltVlan(entityId, vlanIndex);
            } catch (Exception e) {
                logger.info("delete vlan {} fail", vlanIndex);
                logger.info("delete vlan fail: ", e);
                delFailList.add(vlanIndex);
                continue;
            }
        }
        refreshSniVlanView(snmpParam);
        return delFailList;
    }

    @Override
    public void updateTagStatus(Long entityId, Integer vlanIndex, List<Long> taggedPortList,
            List<Long> untaggedPortList, SnmpParam param) {
        if (param == null) {
            param = entityService.getSnmpParamByEntity(entityId);
        }
        VlanAttribute vlanAttribute = sniVlanDao.getOltVlanConfig(entityId, vlanIndex);
        vlanAttribute.setTaggedPortIndexList(taggedPortList);
        vlanAttribute.setUntaggedPortIndexList(untaggedPortList);
        vlanAttribute.setVlanIndex(vlanIndex);
        VlanAttribute vlanAttr = getOltVlanFacade(param.getIpAddress()).updateTagStatus(param, vlanAttribute);
        sniVlanDao.updateTagStatus(vlanAttr);
    }

    @Override
    public VlanAttribute getOltVlanConfig(Long entityId, Integer vlanIndex) {
        return sniVlanDao.getOltVlanConfig(entityId, vlanIndex);
    }

    private OltVlanFacade getOltVlanFacade(String ip) {
        return facadeFactory.getFacade(ip, OltVlanFacade.class);
    }

    @Override
    public void updateEntityStates(SynchronizedEvent event) {
    }

    /**
    * 更新VLAN视图中全局VLAN属性
    * 
    * @param snmpParam
    */
    @Override
    public void refreshSniVlanView(SnmpParam snmpParam) {
        // if (getOltVlanFacade(snmpParam.getIpAddress()).getOltVlanAttributes(snmpParam).size() >
        // 0) {
        OltVlanAttribute param = new OltVlanAttribute();
        OltVlanAttribute oltVlanAttribute = getOltVlanFacade(snmpParam.getIpAddress()).getOltVlanAttributes(snmpParam,
                param);
        oltVlanAttribute.setEntityId(snmpParam.getEntityId());
        sniVlanDao.updateOltVlanGlobalInfo(oltVlanAttribute);
        // }
    }

    /**
    * 更新SNI口VLAN基本信息
    * 
    * @param snmpParam
    */
    @Override
    public void refreshSniPortVlan(SnmpParam snmpParam) {
        Long entityId = snmpParam.getEntityId();
        List<PortVlanAttribute> sniPortVlanAttributes = getOltVlanFacade(snmpParam.getIpAddress())
                .getPortVlanAttributes(snmpParam);
        List<PortVlanAttribute> sniPortVlan = new ArrayList<PortVlanAttribute>();
        List<OltPortVlanEntry> oltPortVlanEntrys = new ArrayList<OltPortVlanEntry>();

        try {
            oltPortVlanEntrys = getOltVlanFacade(snmpParam.getIpAddress()).getOltPortVlanEntry(snmpParam);
            for (OltPortVlanEntry oltPortVlanEntry : oltPortVlanEntrys) {
                PortVlanAttribute portVlanAttribute = new PortVlanAttribute();
                portVlanAttribute.setPortIndex(oltPortVlanEntry.getPortIndex());
                portVlanAttribute.setVlanPVid(oltPortVlanEntry.getVlanPVid());
                portVlanAttribute.setVlanTagPriority(oltPortVlanEntry.getVlanTagPriority());
                portVlanAttribute.setVlanMode(oltPortVlanEntry.getVlanMode());
                portVlanAttribute.setEntityId(entityId);
                sniPortVlan.add(portVlanAttribute);
            }
        } catch (Exception e) {
            logger.debug("getOltPortVlanEntry fail,please check deviceVersion", e);
        }

        if (sniPortVlanAttributes != null) {
            for (PortVlanAttribute portVlanAttribute : sniPortVlanAttributes) {
                portVlanAttribute.setEntityId(snmpParam.getEntityId());
                if (EponIndex.getOnuNoByMibDeviceIndex(portVlanAttribute.getDeviceIndex()) == 0) {
                    sniPortVlan.add(portVlanAttribute);
                }
            }
            sniVlanDao.batchInsertOltPortVlan(sniPortVlan, entityId);
        }
    }

    @Override
    public void refreshSniVlanAttribute(SnmpParam snmpParam) {
        List<VlanAttribute> sniVlanAttributes = getOltVlanFacade(snmpParam.getIpAddress()).getSniVlanAttributes(
                snmpParam);
        for (VlanAttribute vlanAttribute : sniVlanAttributes) {
            vlanAttribute.setEntityId(snmpParam.getEntityId());
        }
        sniVlanDao.batchInsertOltVlanConfig(sniVlanAttributes, snmpParam.getEntityId());
    }

    @Override
    public VlanAttribute getSniVlanConfig(Long entityId, Integer vlanIndex) {
        return null;
    }

    @Override
    public void refreshVlanVifPriIp(SnmpParam snmpParam) {
        List<TopOltVlanVifPriIpTable> topOltVlanVifPriIpTables = getOltVlanFacade(snmpParam.getIpAddress())
                .getOltVlanVifPriIpTables(snmpParam);
        for (TopOltVlanVifPriIpTable vlanVifPriIpTable : topOltVlanVifPriIpTables) {
            vlanVifPriIpTable.setEntityId(snmpParam.getEntityId());
        }
        sniVlanDao.batchInsertVlanVifPriIp(topOltVlanVifPriIpTables, snmpParam.getEntityId());
    }

    @Override
    public void refreshVlanVifSubIp(SnmpParam snmpParam) {
        List<TopOltVlanVifSubIpTable> topOltVlanVifSubIpTables = getOltVlanFacade(snmpParam.getIpAddress())
                .getOltVlanVifSubIpTables(snmpParam);
        for (TopOltVlanVifSubIpTable vlanVifSubIpTable : topOltVlanVifSubIpTables) {
            vlanVifSubIpTable.setEntityId(snmpParam.getEntityId());
        }
        sniVlanDao.batchInsertVlanVifSubIp(topOltVlanVifSubIpTables, snmpParam.getEntityId());
    }

    @Override
    public void refreshVlanVif(Long entityId) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        refreshVlanVifPriIp(snmpParam);
        refreshVlanVifSubIp(snmpParam);
    }

    @Override
    public void refreshTopMcFloodMode(SnmpParam snmpParam) {
        List<TopOltVlanConfigTable> topOltVlanConfigTables = getOltVlanFacade(snmpParam.getIpAddress())
                .getTopMcFloodMode(snmpParam);
        for (TopOltVlanConfigTable oltVlanConfigTable : topOltVlanConfigTables) {
            oltVlanConfigTable.setEntityId(snmpParam.getEntityId());
        }
        sniVlanDao.batchInsertTopMcFloodMode(topOltVlanConfigTables, snmpParam.getEntityId());
    }

    private List<Integer> changeToArray(String str) {
        List<Integer> list = new ArrayList<Integer>();
        String[] s;
        if (str.indexOf(",") > -1) {
            s = str.split(",");
            for (String x : s) {
                if (x.indexOf("-") > -1) {
                    String[] tmp = x.split("-");
                    int i = Integer.parseInt(tmp[0]);
                    int y = Integer.parseInt(tmp[1]);
                    while (i <= y) {
                        list.add(i);
                        i++;
                    }
                } else {
                    list.add(Integer.parseInt(x));
                }
            }
        } else if (str.indexOf("-") > -1) {
            s = str.split("-");
            int i = Integer.parseInt(s[0]);
            int y = Integer.parseInt(s[1]);
            while (i <= y) {
                list.add(i);
                i++;
            }
        } else {
            list.add(Integer.parseInt(str));
        }
        return list;
    }

    public EntityService getEntityService() {
        return entityService;
    }

    public void setEntityService(EntityService entityService) {
        this.entityService = entityService;
    }

    public MessageService getMessageService() {
        return messageService;
    }

    public void setMessageService(MessageService messageService) {
        this.messageService = messageService;
    }

    public SniVlanDao getSniVlanDao() {
        return sniVlanDao;
    }

    public void setSniVlanDao(SniVlanDao sniVlanDao) {
        this.sniVlanDao = sniVlanDao;
    }

    public FacadeFactory getFacadeFactory() {
        return facadeFactory;
    }

    public void setFacadeFactory(FacadeFactory facadeFactory) {
        this.facadeFactory = facadeFactory;
    }

    public OltDao getOltDao() {
        return oltDao;
    }

    public void setOltDao(OltDao oltDao) {
        this.oltDao = oltDao;
    }

}
