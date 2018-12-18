/***********************************************************************
 * $Id: UniVlanProfileServiceImpl.java,v1.0 2013-11-28 上午10:10:45 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.univlanprofile.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.epon.onu.dao.OnuDao;
import com.topvision.ems.epon.onu.dao.UniDao;
import com.topvision.ems.epon.onu.domain.OltOnuAttribute;
import com.topvision.ems.epon.topology.event.OnuSynchronizedEvent;
import com.topvision.ems.epon.topology.event.OnuSynchronizedListener;
import com.topvision.ems.epon.univlanprofile.dao.UniVlanProfileDao;
import com.topvision.ems.epon.univlanprofile.domain.UniVlanBindTable;
import com.topvision.ems.epon.univlanprofile.domain.UniVlanProfileTable;
import com.topvision.ems.epon.univlanprofile.domain.UniVlanRuleTable;
import com.topvision.ems.epon.univlanprofile.facade.UniVlanProfileFacade;
import com.topvision.ems.epon.univlanprofile.service.UniVlanProfileService;
import com.topvision.ems.network.service.EntityService;
import com.topvision.framework.service.BaseService;
import com.topvision.framework.snmp.SnmpParam;
import com.topvision.framework.utils.EponConstants;
import com.topvision.platform.facade.FacadeFactory;
import com.topvision.platform.message.service.MessageService;

/**
 * @author flack
 * @created @2013-11-28-上午10:10:45
 *
 */
@Service("uniVlanProfileService")
public class UniVlanProfileServiceImpl extends BaseService implements UniVlanProfileService, OnuSynchronizedListener {

    @Autowired
    private MessageService messageService;
    @Autowired
    private UniVlanProfileDao uniVlanProfileDao;
    @Autowired
    private EntityService entityService;
    @Autowired
    private FacadeFactory facadeFactory;
    @Autowired
    private UniDao uniDao;
    @Autowired
    private OnuDao onuDao;

    public static final Integer ONU_SINGLE_TOPO = 1;

    @Override
    @PreDestroy
    public void destroy() {
        super.destroy();
        messageService.removeListener(OnuSynchronizedListener.class, this);
    }

    @Override
    @PostConstruct
    public void initialize() {
        super.initialize();
        messageService.addListener(OnuSynchronizedListener.class, this);
    }

    @Override
    public void insertEntityStates(OnuSynchronizedEvent event) {
        Long entityId = event.getEntityId();
        List<Long> onuIndexlList = event.getOnuIndexList();
        try {
            if (onuIndexlList.size() == ONU_SINGLE_TOPO) {
                OltOnuAttribute onuAttr = onuDao.getOnuAttributeByIndex(entityId, onuIndexlList.get(0));
                if (EponConstants.EPON_ONU.equals(onuAttr.getOnuEorG())) {
                    refreshUniVlanData(entityId, onuIndexlList.get(0));
                }
            } else if (onuIndexlList.size() > ONU_SINGLE_TOPO) {
                refreshUniVlanData(entityId);
            }
            logger.info("refreshUniVlanData finish");
        } catch (Exception e) {
            logger.debug("refreshUniVlanData wrong", e);
        }
    }

    @Override
    public List<UniVlanProfileTable> getProfileList(Long entityId) {
        return uniVlanProfileDao.getUniVlanProfiles(entityId);
    }

    @Override
    public void addProfile(UniVlanProfileTable profile) {
        //先添加到设备上
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(profile.getEntityId());
        UniVlanProfileTable newProfile = this.getUniVlanProfileFacade(snmpParam.getIpAddress()).addUniVlanProfile(
                snmpParam, profile);
        //再添加到数据库
        uniVlanProfileDao.insertUniVlanProfile(newProfile);
    }

    @Override
    public void updateProfile(UniVlanProfileTable profile, boolean modeChange) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(profile.getEntityId());
        UniVlanProfileTable newProfile = this.getUniVlanProfileFacade(snmpParam.getIpAddress()).setUniVlanProfile(
                snmpParam, profile);
        uniVlanProfileDao.updateUniVlanProfile(newProfile);
        //如果更改了模式Vlan模式,需要清除对应的规则
        if (modeChange) {
            uniVlanProfileDao.deleteProfileRelRules(profile.getEntityId(), profile.getProfileId());
        }

    }

    @Override
    public void deleteProfile(UniVlanProfileTable profile) {
        //先从设备上删除
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(profile.getEntityId());
        this.getUniVlanProfileFacade(snmpParam.getIpAddress()).deleteUniVlanProfile(snmpParam, profile);
        //从 数据库删除
        uniVlanProfileDao.deleteUniVlanProfile(profile);
    }

    @Override
    public void refreshProfileData(Long entityId) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        List<UniVlanProfileTable> uniVlanProfiles = getUniVlanProfileFacade(snmpParam.getIpAddress())
                .getUniVlanProfiles(snmpParam);
        uniVlanProfileDao.batchInsertUniVlanProfiles(uniVlanProfiles, entityId);

    }

    @Override
    public void refreshProfileAndRule(Long entityId) {
        //由于在刷新模板数据时会先删除数据库中的模板数据,但由于外键关联会同时删除规则数据,所以需要在刷新模板数据后再刷新规则数据
        this.refreshProfileData(entityId);
        this.refreshRuleData(entityId);
    }

    @Override
    public List<UniVlanRuleTable> getRuleList(Long entityId, Integer profileId) {
        return uniVlanProfileDao.getProfileVlanRules(entityId, profileId);
    }

    @Override
    public void addRule(UniVlanRuleTable rule) {
        //先添加到设备
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(rule.getEntityId());
        UniVlanRuleTable newRule = this.getUniVlanProfileFacade(snmpParam.getIpAddress()).addUniVlanRule(snmpParam,
                rule);
        //再添加到数据库
        uniVlanProfileDao.insertUniVlanRule(newRule);
    }

    @Override
    public void deleteRule(UniVlanRuleTable rule) {
        //先从设备删除
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(rule.getEntityId());
        this.getUniVlanProfileFacade(snmpParam.getIpAddress()).deleteUniVlanRule(snmpParam, rule);
        //再从数据库删除
        uniVlanProfileDao.deleteUniVlanRule(rule);
    }

    @Override
    public void refreshRuleData(Long entityId) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        List<UniVlanRuleTable> uniVlanRules = getUniVlanProfileFacade(snmpParam.getIpAddress()).getUniVlanRules(
                snmpParam);
        uniVlanProfileDao.batchInsertUniVlanRules(uniVlanRules, entityId);
    }

    @Override
    public void refreshUniVlanData(Long entityId) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        List<UniVlanBindTable> uniVlanBinds = getUniVlanProfileFacade(snmpParam.getIpAddress()).getUniVlanBindTables(
                snmpParam);
        uniVlanProfileDao.batchInsertUniVlanBind(uniVlanBinds, entityId);
    }

    @Override
    public void refreshUniVlanData(Long entityId, Long onuIndex) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        List<Long> uniIndexList = uniDao.getUniIndexListByEntityIdAndOnuIndex(entityId, onuIndex);
        List<UniVlanBindTable> UniVlanBindTables = new ArrayList<UniVlanBindTable>();
        for (Long uniIndex : uniIndexList) {
            UniVlanBindTable uniVlanBindTable = new UniVlanBindTable();
            uniVlanBindTable.setUniIndex(uniIndex);
            UniVlanBindTables.add(uniVlanBindTable);
        }
        List<UniVlanBindTable> uniVlanBinds = getUniVlanProfileFacade(snmpParam.getIpAddress()).getUniVlanBindTables(
                snmpParam, UniVlanBindTables);

        if (uniVlanBinds != null) {
            uniVlanProfileDao.batchInsertUniVlanBind(uniVlanBinds, entityId);
        }
    }

    @Override
    public void updateProfileBind(List<UniVlanBindTable> bindList, UniVlanProfileTable profile) {
        //更新模板绑定记录
        uniVlanProfileDao.batchUpdateVlanBind(bindList);
    }

    @Override
    public void updateUnBind(List<UniVlanBindTable> bindList) {
        //更新模板解除绑定记录
        uniVlanProfileDao.batchUpdateVlanBind(bindList);
    }

    @Override
    public List<Long> getProfileBindList(Integer profileIndex, Long entityId) {
        return uniVlanProfileDao.queryProfileBindList(profileIndex, entityId);
    }

    @Override
    public UniVlanBindTable getUniBindInfo(Long uniId, Long entityId) {
        Long uniIndex = uniDao.getUniIndex(uniId);
        UniVlanBindTable bindTable = new UniVlanBindTable();
        bindTable.setEntityId(entityId);
        bindTable.setUniIndex(uniIndex);
        return uniVlanProfileDao.queryUniBindInfo(bindTable);
    }

    @Override
    public UniVlanProfileTable getUniVlanProfile(Long entityId, Integer profileId) {
        return uniVlanProfileDao.getUniVlanProfileById(entityId, profileId);
    }

    @Override
    public void updateUniPvid(UniVlanBindTable bindTable) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(bindTable.getEntityId());
        @SuppressWarnings("unused")
        UniVlanBindTable newBindTable = this.getUniVlanProfileFacade(snmpParam.getIpAddress()).setUniVlanBindTables(
                snmpParam, bindTable);
        uniVlanProfileDao.updateUniPvid(bindTable);
    }

    @Override
    public void refreshUniBindInfo(Long entityId, Long uniIndex) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        UniVlanBindTable bindTable = new UniVlanBindTable();
        bindTable.setEntityId(entityId);
        bindTable.setUniIndex(uniIndex);
        UniVlanBindTable newBindTable = this.getUniVlanProfileFacade(snmpParam.getIpAddress()).getUniVlanBindInfo(
                snmpParam, bindTable);
        uniVlanProfileDao.updateUniBindInfo(newBindTable);
    }

    private UniVlanProfileFacade getUniVlanProfileFacade(String ip) {
        return facadeFactory.getFacade(ip, UniVlanProfileFacade.class);
    }

    @Override
    public List<UniVlanBindTable> getEntityBindList(Long entityId) {
        return uniVlanProfileDao.queryEntityBindList(entityId);
    }

    @Override
    public void replaceBindProfile(UniVlanBindTable uniBindInfo) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(uniBindInfo.getEntityId());
        getUniVlanProfileFacade(snmpParam.getIpAddress()).replaceBindProfile(snmpParam, uniBindInfo);
        uniVlanProfileDao.updateVlanBind(uniBindInfo);
    }

    @Override
    public void unBindUniProfile(UniVlanBindTable bindTable) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(bindTable.getEntityId());
        getUniVlanProfileFacade(snmpParam.getIpAddress()).unBindUniProfile(snmpParam, bindTable);
        uniVlanProfileDao.updateVlanBind(bindTable);
    }

    @Override
    public UniVlanBindTable getUniVlanBindInfo(Long entityId, Long uniIndex) {
        UniVlanBindTable bindTable = new UniVlanBindTable();
        bindTable.setEntityId(entityId);
        bindTable.setUniIndex(uniIndex);
        return uniVlanProfileDao.queryUniBindInfo(bindTable);
    }

    @Override
    public void refreshUniVlanInfo(Long entityId, Long uniIndex) {
        //刷新模板和规则数据
        refreshProfileAndRule(entityId);
        //刷新端口绑定信息
        refreshUniBindInfo(entityId, uniIndex);
    }

}
