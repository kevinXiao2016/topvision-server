/***********************************************************************
 * $Id: OltTrunkServiceImpl.java,v1.0 2013-10-25 下午3:18:39 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.trunk.service.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NavigableSet;
import java.util.concurrent.ConcurrentSkipListMap;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.epon.exception.SetValueConflictException;
import com.topvision.ems.epon.exception.SniTrunkConfigException;
import com.topvision.ems.epon.olt.dao.OltSniDao;
import com.topvision.ems.epon.olt.domain.OltSniAttribute;
import com.topvision.ems.epon.trunk.dao.OltTrunkDao;
import com.topvision.ems.epon.trunk.domain.OltSniTrunkConfig;
import com.topvision.ems.epon.trunk.facade.OltTrunkFacade;
import com.topvision.ems.epon.trunk.service.OltTrunkService;
import com.topvision.ems.epon.vlan.dao.SniVlanDao;
import com.topvision.ems.network.service.EntityService;
import com.topvision.framework.service.BaseService;
import com.topvision.framework.snmp.SnmpParam;
import com.topvision.platform.facade.FacadeFactory;
import com.topvision.platform.message.event.SynchronizedEvent;
import com.topvision.platform.message.event.SynchronizedListener;
import com.topvision.platform.message.service.MessageService;

/**
 * @author flack
 * @created @2013-10-25-下午3:18:39
 *
 */
@Service("oltTrunkService")
public class OltTrunkServiceImpl extends BaseService implements OltTrunkService, SynchronizedListener {
    private static Logger logger = LoggerFactory.getLogger(OltTrunkServiceImpl.class);

    private static final int MAX_TRUNK_COUNT = 32;
    private static final int DEFAULT_TRUNK_INDEX = 1;
    private static Object trunkSy = new Object();
    //内存保存的所有trunk组镜像 需要保证内存和数据库的对应 key为trunkIndex（5位）
    private final ConcurrentSkipListMap<Long, ConcurrentSkipListMap<Integer, OltSniTrunkConfig>> trunks = new ConcurrentSkipListMap<Long, ConcurrentSkipListMap<Integer, OltSniTrunkConfig>>();
    @Autowired
    private SniVlanDao sniVlanDao;
    @Autowired
    private FacadeFactory facadeFactory;
    @Autowired
    private EntityService entityService;
    @Autowired
    private OltTrunkDao oltTrunkDao;
    @Autowired
    private OltSniDao oltSniDao;
    @Autowired
    private MessageService messageService;

    @Override
    public void destroy() {
        super.destroy();
        messageService.removeListener(SynchronizedListener.class, this);
    }

    @Override
    @PostConstruct
    public void initialize() {
        super.initialize();
        messageService.addListener(SynchronizedListener.class, this);
    }

    @Override
    public void insertEntityStates(SynchronizedEvent event) {
        try {
            refreshSniTrunkConfig(event.getEntityId());
            logger.info("refreshSniTrunkConfig finish");
        } catch (Exception e) {
            logger.error("refreshSniTrunkConfig wrong", e);
        }

        updateMemoryDataAfterDiscovery(event.getEntityId());
    }

    /**
     * 加载TrunkGroup数据。
     * 
     * @return
     */
    @Override
    public List<OltSniTrunkConfig> getTrunkConfigList(Long entityId) {
        // 获取trunkGroups
        ConcurrentSkipListMap<Integer, OltSniTrunkConfig> trunkGroups = getTrunkGroups(entityId);
        // 通过这个设备的trunkGroups构造返回结果
        List<OltSniTrunkConfig> re = new ArrayList<OltSniTrunkConfig>();
        for (OltSniTrunkConfig oltSniTrunkConfig : trunkGroups.values()) {
            re.add(oltSniTrunkConfig);
        }
        return re;
    }

    /**
     * 可用的sni口OltSniAttribute的列表
     * 
     * @param entityId
     *            olt的id
     * @param trunkIndex
     *            所属trunk组
     * @param sniId
     *            加入新trunk组的第一个sni口的Id
     * @return OltSniAttribute列表
     * @exception SniTrunkConfigException
     *                没有可用端口时抛出
     */
    @Override
    public List<OltSniAttribute> availableSniList(Long entityId, Integer trunkIndex, Long sniId) {
        List<OltSniAttribute> sniList = null;
        ConcurrentSkipListMap<Integer, OltSniTrunkConfig> trunkGroups = getTrunkGroups(entityId);
        Long trunkSniIndex = null;
        // 如果trunkIndex和sniId之中任何一个不为空
        if (trunkIndex != null || sniId != null) {
            OltSniAttribute oltSniAttr = null;
            if (trunkIndex != null) {
                // 如果trunkIndex不为空，通过trunkIndex获取第一个sni口的属性
                OltSniTrunkConfig trunk = trunkGroups.get(trunkIndex);
                if (trunk != null && !trunk.getSniTrunkGroupConfigGroup().isEmpty()) {
                    Long sniIndex = trunk.getSniTrunkGroupConfigGroup().get(0);
                    oltSniAttr = oltSniDao.getSniAttribute(entityId, sniIndex);
                    trunkSniIndex = sniIndex;
                } else {
                    // trunk组为空的 使用entityId下所有可加入的sni口列表返回
                }
            } else {
                // 如果trunkIndex为空，则使用sniId来查询出sni口属性
                oltSniAttr = oltSniDao.getSniAttribute(sniId);
                trunkSniIndex = oltSniDao.getSniIndex(sniId);
            }
            if (oltSniAttr != null) {
                oltSniAttr.setEntityId(entityId);
                sniList = oltSniDao.availableSniListForTrunkGroupBySniAttribute(oltSniAttr);
            }
        } else {
            // 通过entityId获取该entityId下所有可用sni口列表
        }
        if (sniList == null) {
            // 以上两种情况都使用entityId获取所有可用的sni口列表
            sniList = oltSniDao.availableSniListForTrunkGroupByEntityId(entityId);
        } else {
            // 进入vlan参数过滤环节
            if (!sniList.isEmpty()) {
                // 如果sniList计算出来的结果存在
                // VLAN 配置: 端口上允许通过的VLAN、端口缺省VLAN ID、VLAN 报文是否带Tag 配置
                // 计算每个sni口的vlan属性是否和trunk组一致
                /* PortVlanAttribute trunkPortVlanAttribute = sniVlanDao.getSniPortVlanAttribute(entityId, trunkSniIndex);
                 for (Iterator<OltSniAttribute> it = sniList.iterator(); it.hasNext();) {
                     OltSniAttribute sniAttr = it.next();
                     PortVlanAttribute attr = sniVlanDao.getSniPortVlanAttribute(entityId, sniAttr.getSniIndex());
                     // 如果和trunk组vlan基本属性不一致则剔除
                     if (trunkPortVlanAttribute != null && attr != null && !attr.equals(trunkPortVlanAttribute)) {
                         if (logger.isDebugEnabled()) {
                             logger.debug("availableSniList vlan attr remove port [" + attr.getPortId() + "]");
                         }
                         it.remove();
                     }
                 }
                 // 获取trunk应该属于的所有taggedvlan
                 List<VlanAttribute> trunkTaggedVlans = new ArrayList<VlanAttribute>();
                 Map<Long, List<VlanAttribute>> sniTaggedVlansMap = new HashMap<Long, List<VlanAttribute>>();
                 // 获取trunk应该属于的所有untaggedvlan
                 List<VlanAttribute> trunkUnTaggedVlans = new ArrayList<VlanAttribute>();
                 Map<Long, List<VlanAttribute>> sniUnTaggedVlansMap = new HashMap<Long, List<VlanAttribute>>();
                 // trunkSniIndex 不可能为空
                 if (trunkSniIndex != null) {
                     // 设备下所有vlanAttribute
                     List<VlanAttribute> vlans = sniVlanDao.getOltVlanConfigList(entityId);
                     for (VlanAttribute vlanAttribute : vlans) {
                         // 计算trunk组的vlan
                         if (vlanAttribute.getTaggedPortIndexList() != null
                                 && vlanAttribute.getTaggedPortIndexList().contains(trunkSniIndex)) {
                             trunkTaggedVlans.add(vlanAttribute);
                         }
                         if (vlanAttribute.getUntaggedPortIndexList() != null
                                 && vlanAttribute.getUntaggedPortIndexList().contains(trunkSniIndex)) {
                             trunkUnTaggedVlans.add(vlanAttribute);
                         }
                         // 计算每个sni口所在的taggedvlan和untaggedvlan
                         for (OltSniAttribute sniAttribute : sniList) {
                             List<VlanAttribute> sniTaggedVlans;
                             if (sniTaggedVlansMap.containsKey(sniAttribute.getSniIndex())) {
                                 sniTaggedVlans = sniTaggedVlansMap.get(sniAttribute.getSniIndex());
                             } else {
                                 sniTaggedVlans = new ArrayList<VlanAttribute>();
                                 sniTaggedVlansMap.put(sniAttribute.getSniIndex(), sniTaggedVlans);
                             }
                             List<VlanAttribute> sniUnTaggedVlans;
                             if (sniUnTaggedVlansMap.containsKey(sniAttribute.getSniIndex())) {
                                 sniUnTaggedVlans = sniUnTaggedVlansMap.get(sniAttribute.getSniIndex());
                             } else {
                                 sniUnTaggedVlans = new ArrayList<VlanAttribute>();
                                 sniUnTaggedVlansMap.put(sniAttribute.getSniIndex(), sniUnTaggedVlans);
                             }
                             if (vlanAttribute.getTaggedPortIndexList() != null
                                     && vlanAttribute.getTaggedPortIndexList().contains(sniAttribute.getSniIndex())) {
                                 sniTaggedVlans.add(vlanAttribute);
                             }
                             if (vlanAttribute.getUntaggedPortIndexList() != null
                                     && vlanAttribute.getUntaggedPortIndexList().contains(sniAttribute.getSniIndex())) {
                                 sniUnTaggedVlans.add(vlanAttribute);
                             }
                         }
                     }
                     // 将统计结果进行计算 将和trunk组vlan配置不匹配的sni口剔除出去
                     for (Iterator<OltSniAttribute> iterator = sniList.iterator(); iterator.hasNext();) {
                         OltSniAttribute sniAttribute = iterator.next();
                         List<VlanAttribute> sniTaggedVlans = sniTaggedVlansMap.get(sniAttribute.getSniIndex());
                         List<VlanAttribute> sniUnTaggedVlans = sniUnTaggedVlansMap.get(sniAttribute.getSniIndex());
                         if (!trunkTaggedVlans.equals(sniTaggedVlans) || !trunkUnTaggedVlans.equals(sniUnTaggedVlans)) {
                             iterator.remove();
                         }
                     }
                 }*/
            } else {
                throw new SniTrunkConfigException("no available sni port.");
            }
        }
        if (sniList != null && !sniList.isEmpty()) {
            // 判定是否有已经存在于任何一个trunk组中的sni口，有则剔除
            // 构造一个已加入trunk组列表的sni口列表
            List<Long> sniTrunkList = new ArrayList<Long>();
            for (OltSniTrunkConfig oltSniTrunkConfig : trunkGroups.values()) {
                sniTrunkList.addAll(oltSniTrunkConfig.getSniTrunkGroupConfigGroup());
            }
            // 遍历可用sni口列表中找出已经加入该trunk组的口，并从列表中剔除
            for (Iterator<OltSniAttribute> longIterator = sniList.iterator(); longIterator.hasNext();) {
                OltSniAttribute oltSniAttribute = longIterator.next();
                if (sniTrunkList.contains(oltSniAttribute.getSniIndex())) {
                    longIterator.remove();
                }
            }
        } else {
            throw new SniTrunkConfigException("no available sni port.");
        }
        return sniList;
    }

    /**
     * 添加Trunk组
     * 
     * @param oltSniTrunkConfig
     *            新建的没有trunkIndex的对象
     * @return OltSniTrunkConfig 填入了trunkIndex的对象
     */
    @Override
    public OltSniTrunkConfig addSniTrunkConfig(OltSniTrunkConfig oltSniTrunkConfig) {
        synchronized (trunkSy) {
            List<OltSniAttribute> oltSniAttributes = availableSniList(oltSniTrunkConfig.getEntityId(), null, null);
            for (Long sniIndex : oltSniTrunkConfig.getSniTrunkGroupConfigGroup()) {
                boolean exist = false;
                for (OltSniAttribute oltSniAttribute : oltSniAttributes) {
                    if (oltSniAttribute.getSniIndex().equals(sniIndex)) {
                        exist = true;
                        break;
                    }
                }
                if (!exist) {
                    throw new SniTrunkConfigException();
                }
            }
            // 获取trunkGroups
            ConcurrentSkipListMap<Integer, OltSniTrunkConfig> trunkGroups = getTrunkGroups(oltSniTrunkConfig
                    .getEntityId());
            // 获取最大的trunkIndex
            Integer maxIndex = DEFAULT_TRUNK_INDEX;
            // 如果trunk组编号已被占满都没有找到则返回错误
            // @@MARK 这个索引由网管维护，如果网管没有刷新，可能导致明明可以配置一个trunk组但是通过网管却不行！
            if (trunkGroups.isEmpty()) {
                maxIndex = DEFAULT_TRUNK_INDEX;
            } else if (trunkGroups.size() == MAX_TRUNK_COUNT) {
                throw new SniTrunkConfigException("there is no avaialabe trunk index left");
            } else {
                maxIndex = trunkGroups.lastKey();
                // modify by @bravin/ 如果最大index不小于32的话从最小的开始找直到找到一个可用的,否则就将index+1后设置下去
                if (maxIndex > MAX_TRUNK_COUNT || maxIndex == MAX_TRUNK_COUNT) {
                    // @Algorithm desc : 从1-32的集合 与排序 map的keyset集合中同时拿出一个值比较，如果相同则继续，如果不同则取这个值
                    NavigableSet<Integer> keySet = trunkGroups.keySet();
                    Iterator<Integer> it = keySet.iterator();
                    for (int i = 1; i < MAX_TRUNK_COUNT + 1; i++) {
                        // 理论上不存在 maxIndex >= 32结果测试没结束的时候
                        // hasNext为假的情况.因为如果走到了maxIndex的话，之前的就全部找到了.故不考虑else
                        if (it.hasNext() && it.next().intValue() != i) {
                            maxIndex = i;
                            break;
                        }
                    }
                } else {
                    maxIndex++;
                }
            }
            // 将maxTrunkIndex加一后设置到新的对象中去
            oltSniTrunkConfig.setSniTrunkGroupConfigIndex(maxIndex);
            oltSniTrunkConfig.setSniTrunkGroupIndex(oltSniTrunkConfig.getSniTrunkGroupConfigIndex());
            // 获取设备对应的snmpParam
            SnmpParam snmpParam = entityService.getSnmpParamByEntity(oltSniTrunkConfig.getEntityId());
            // 调用facade的方法添加一个trunkGroup
            OltSniTrunkConfig newOltSniTrunkConfig = getOltFacade(snmpParam.getIpAddress()).addSniTrunkConfig(
                    snmpParam, oltSniTrunkConfig);
            if (newOltSniTrunkConfig == null) {
                throw new SetValueConflictException("Business.connection");
            } else {
                // 将数据保存到数据库
                oltTrunkDao.insertSniTrunkConfig(newOltSniTrunkConfig);
                // 更新内存镜像
                trunkGroups.put(newOltSniTrunkConfig.getSniTrunkGroupConfigIndex(), newOltSniTrunkConfig);
                return newOltSniTrunkConfig; // To change body of implemented
                                             // methods use File |
                                             // Settings | File Templates.
            }
        }
    }

    /**
     * 删除Trunk组
     * 
     * @param entityId
     *            olt的id
     * @param trunkIndex
     *            需要删除的trunk组id
     */
    @Override
    public void deleteSniTrunkConfig(Long entityId, Integer trunkIndex) throws Exception {
        synchronized (trunkSy) {
            // 获取trunkGroups
            ConcurrentSkipListMap<Integer, OltSniTrunkConfig> trunkGroups = getTrunkGroups(entityId);
            // 获取设备对应的snmpParam
            SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
            // 调用facade的方法删除一个trunkGroup
            OltSniTrunkConfig oltSniTrunkConfig = new OltSniTrunkConfig();
            oltSniTrunkConfig.setEntityId(entityId);
            oltSniTrunkConfig.setSniTrunkGroupConfigIndex(trunkIndex);
            oltSniTrunkConfig.setSniTrunkGroupIndex(trunkIndex);
            // 删除数据库中的数据
            try {
                getOltFacade(snmpParam.getIpAddress()).deleteSniTrunkConfig(snmpParam, oltSniTrunkConfig);
                Thread.sleep(5000);
            } catch (Exception e) {
                logger.error("", e);
            }
            refreshSniTrunkConfig(entityId);
            // 删除内存中的镜像
            trunkGroups.remove(trunkIndex);
        }
    }

    /**
     * 修改Trunk组
     * 
     * @param oltSniTrunkConfig
     *            修改的trunk对象
     */
    @Override
    public void modifySniTrunkConfig(OltSniTrunkConfig oltSniTrunkConfig) {
        synchronized (trunkSy) {
            // 获取trunkGroups
            ConcurrentSkipListMap<Integer, OltSniTrunkConfig> trunkGroups = getTrunkGroups(oltSniTrunkConfig
                    .getEntityId());
            // 获取设备对应的snmpParam
            SnmpParam snmpParam = entityService.getSnmpParamByEntity(oltSniTrunkConfig.getEntityId());
            // 调用facade的方法修改一个trunkGroup
            oltSniTrunkConfig = getOltFacade(snmpParam.getIpAddress()).modifySniTrunkConfig(snmpParam,
                    oltSniTrunkConfig);
            if (oltSniTrunkConfig == null) {
                throw new SetValueConflictException("Business.connection");
            } else {
                // 修改数据库中的数据
                oltTrunkDao.updateSniTrunkConfig(oltSniTrunkConfig);
                // 修改内存中的镜像
                OltSniTrunkConfig trunk = trunkGroups.get(oltSniTrunkConfig.getSniTrunkGroupConfigIndex());
                trunk.setSniTrunkGroupConfigName(oltSniTrunkConfig.getSniTrunkGroupConfigName());
                trunk.setSniTrunkGroupConfigPolicy(oltSniTrunkConfig.getSniTrunkGroupConfigPolicy());
                trunk.setSniTrunkGroupConfigMember(oltSniTrunkConfig.getSniTrunkGroupConfigMember());
            }
        }
    }

    @Override
    public void refreshSniTrunkConfig(Long entityId) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        List<OltSniTrunkConfig> sniTrunkConfigs = getOltFacade(snmpParam.getIpAddress()).refreshSniTrunkConfig(
                snmpParam);
        oltTrunkDao.batchInsertOltSniTrunkConfig(sniTrunkConfigs, entityId);
    }

    /**
     * 当发现完成的时候更新内存缓存的数据
     * 
     * @param entityId
     *            OltDiscoveryData
     */
    @Override
    public void updateMemoryDataAfterDiscovery(Long entityId) {
        // 使用dao层的接口获取trunk设备刷新后的最新数据，并构造内存数据
        List<OltSniTrunkConfig> oltSniTrunkConfigList = oltTrunkDao.getTrunkConfigList(entityId);
        trunks.put(entityId, makeTrunkMap(oltSniTrunkConfigList));
    }

    /**
     * 获取OltControlFacade对象
     * 
     * @param ip
     *            被采集设备IP
     * @return OltControlFacade
     */
    private OltTrunkFacade getOltFacade(String ip) {
        return facadeFactory.getFacade(ip, OltTrunkFacade.class);
    }

    /**
     * 通过一个entityId获取一个TrunkGroups
     * 
     * @param entityId
     *            设备id
     * @return trunkGroups
     */
    private ConcurrentSkipListMap<Integer, OltSniTrunkConfig> getTrunkGroups(Long entityId) {
        ConcurrentSkipListMap<Integer, OltSniTrunkConfig> trunkGroups;
        // 内存中是否存在这个olt的trunk组镜像
        if (trunks.containsKey(entityId)) {
            // 存在则直接获取trunkGroup
            trunkGroups = trunks.get(entityId);
        } else {
            // 加载TrunkGroup数据
            List<OltSniTrunkConfig> oltSniTrunkConfigList = oltTrunkDao.getTrunkConfigList(entityId);
            trunkGroups = makeTrunkMap(oltSniTrunkConfigList);
            trunks.put(entityId, trunkGroups);
        }
        return trunkGroups;
    }

    /**
     * 构造一个新的内存中的trunk组镜像
     * 
     * @param oltSniTrunkConfigList
     *            从数据库读出的新的trunk组列表
     */
    private ConcurrentSkipListMap<Integer, OltSniTrunkConfig> makeTrunkMap(List<OltSniTrunkConfig> oltSniTrunkConfigList) {
        ConcurrentSkipListMap<Integer, OltSniTrunkConfig> re = new ConcurrentSkipListMap<Integer, OltSniTrunkConfig>();
        for (OltSniTrunkConfig oltSniTrunkConfig : oltSniTrunkConfigList) {
            re.put(oltSniTrunkConfig.getSniTrunkGroupConfigIndex(), oltSniTrunkConfig);
        }
        return re;
    }

    @Override
    public void updateEntityStates(SynchronizedEvent event) {
    }

}
