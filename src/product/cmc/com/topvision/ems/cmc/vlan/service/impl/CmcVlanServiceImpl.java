package com.topvision.ems.cmc.vlan.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.cmc.config.service.CmcConfigService;
import com.topvision.ems.cmc.facade.domain.CmcIpSubVlanCfgEntry;
import com.topvision.ems.cmc.facade.domain.CmcIpSubVlanScalarObject;
import com.topvision.ems.cmc.facade.domain.CmcSystemIpInfo;
import com.topvision.ems.cmc.service.impl.CmcBaseCommonService;
import com.topvision.ems.cmc.util.CmcConstants;
import com.topvision.ems.cmc.util.CmcUtil;
import com.topvision.ems.cmc.vlan.dao.CmcVlanDao;
import com.topvision.ems.cmc.vlan.domain.CmcVlanData;
import com.topvision.ems.cmc.vlan.facade.domain.CmcVifSubIpEntry;
import com.topvision.ems.cmc.vlan.facade.domain.CmcVlanConfigEntry;
import com.topvision.ems.cmc.vlan.facade.domain.CmcVlanDhcpAllocEntry;
import com.topvision.ems.cmc.vlan.facade.domain.CmcVlanPrimaryIp;
import com.topvision.ems.cmc.vlan.service.CmcVlanService;
import com.topvision.ems.template.service.EntityTypeService;
import com.topvision.platform.message.event.CmcSynchronizedEvent;
import com.topvision.platform.message.event.CmcSynchronizedListener;
import com.topvision.platform.message.service.MessageService;

@Service("cmcVlanService")
public class CmcVlanServiceImpl extends CmcBaseCommonService implements CmcVlanService, CmcSynchronizedListener {

    @Resource(name = "cmcVlanDao")
    private CmcVlanDao cmcVlanDao;
    @Resource(name = "cmcConfigService")
    private CmcConfigService cmcConfigService;
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
    public CmcIpSubVlanScalarObject getCmcIpSubVlanScalarById(Long cmcId) {
        return cmcVlanDao.getCmcIpSubVlanScalarById(cmcId);
    }

    @Override
    public void modifyCmcIpSubVlanScalar(Long cmcId, Integer topCcmtsIpSubVlanCfi, String ipSubVlanTpid) {
        snmpParam = getSnmpParamByEntityId(cmcId);
        CmcIpSubVlanScalarObject obj = new CmcIpSubVlanScalarObject();
        obj.setCmcId(cmcId);
        obj.setTopCcmtsIpSubVlanCfi(topCcmtsIpSubVlanCfi);
        obj.setIpSubVlanTpid(ipSubVlanTpid);
        getCmcFacade(snmpParam.getIpAddress()).setCmcIpSubVlanScalar(snmpParam, obj);
        cmcVlanDao.updateCmcIpSubVlanScalar(obj);
    }

    @Override
    public List<CmcIpSubVlanCfgEntry> getCmcIpSubVlanCfgList(Map<String, Object> queryMap) {
        return cmcVlanDao.getCmcIpSubVlanCfgList(queryMap);
    }

    @Override
    public CmcIpSubVlanCfgEntry getCmcIpSubVlanCfg(Long cmcId, String cmcVlanIpIndex, String cmcVlanIpMaskIndex) {
        return cmcVlanDao.getCmcIpSubVlanCfg(cmcId, cmcVlanIpIndex, cmcVlanIpMaskIndex);
    }

    @Override
    public void addCmcIpSubVlanCfg(Long cmcId, String cmcSubVlanIp, String cmcSubVlanMask, Integer cmcSubVlanVlanId,
            Integer cmcSubVlanPri) {
        snmpParam = getSnmpParamByEntityId(cmcId);
        CmcIpSubVlanCfgEntry vlanCfgEntry = new CmcIpSubVlanCfgEntry();
        vlanCfgEntry.setCmcId(cmcId);
        vlanCfgEntry.setTopCcmtsIpSubVlanIfIndex(null);
        vlanCfgEntry.setTopCcmtsIpSubVlanIpIndex(cmcSubVlanIp);
        vlanCfgEntry.setTopCcmtsIpSubVlanIpMaskIndex(cmcSubVlanMask);
        vlanCfgEntry.setTopCcmtsIpSubVlanVlanId(cmcSubVlanVlanId);
        vlanCfgEntry.setTopCcmtsIpSubVlanPri(cmcSubVlanPri);
        getCmcFacade(snmpParam.getIpAddress()).createCmcIpSubVlanCfg(snmpParam, vlanCfgEntry);
        cmcVlanDao.addCmcIpSubVlanCfg(vlanCfgEntry);
    }

    @Override
    public void modifyCmcIpSubVlanCfg(Long cmcId, String cmcVlanIpIndex, String cmcVlanIpMaskIndex,
            Integer cmcSubVlanPri) {
        snmpParam = getSnmpParamByEntityId(cmcId);
        CmcIpSubVlanCfgEntry vlanCfgEntry = cmcVlanDao.getCmcIpSubVlanCfg(cmcId, cmcVlanIpIndex, cmcVlanIpMaskIndex);
        /*
         * vlanCfgEntry.setCmcId(cmcId); vlanCfgEntry.setTopCcmtsIpSubVlanIfIndex(null);
         * vlanCfgEntry.setTopCcmtsIpSubVlanIpIndex(cmcVlanIpIndex);
         * vlanCfgEntry.setTopCcmtsIpSubVlanIpMaskIndex(cmcVlanIpMaskIndex);
         */
        vlanCfgEntry.setTopCcmtsIpSubVlanPri(cmcSubVlanPri);
        getCmcFacade(snmpParam.getIpAddress()).setCmcIpSubVlanCfg(snmpParam, vlanCfgEntry);
        cmcVlanDao.updateCmcIpSubVlanCfgEntry(vlanCfgEntry);

    }

    @Override
    public void deleteCmcIpSubVlanCfg(Long cmcId, String cmcVlanIpIndex, String cmcVlanIpMaskIndex) {
        snmpParam = getSnmpParamByEntityId(cmcId);
        CmcIpSubVlanCfgEntry vlanCfgEntry = new CmcIpSubVlanCfgEntry();
        vlanCfgEntry.setCmcId(cmcId);
        vlanCfgEntry.setTopCcmtsIpSubVlanIfIndex(null);
        vlanCfgEntry.setTopCcmtsIpSubVlanIpIndex(cmcVlanIpIndex);
        vlanCfgEntry.setTopCcmtsIpSubVlanIpMaskIndex(cmcVlanIpMaskIndex);
        getCmcFacade(snmpParam.getIpAddress()).destoryCmcIpSubVlanCfg(snmpParam, vlanCfgEntry);
        cmcVlanDao.deleteCmcIpSubVlanCfg(cmcId, cmcVlanIpIndex, cmcVlanIpMaskIndex);
    }

    @Override
    public void refreshCmcIpSubVlanCfg(Long cmcId) {
        snmpParam = getSnmpParamByEntityId(cmcId);
        CmcIpSubVlanScalarObject cmcIpSubVlanScalar = getCmcFacade(snmpParam.getIpAddress()).getCmcIpSubVlanScalar(
                snmpParam);
        cmcIpSubVlanScalar.setCmcId(cmcId);
        if (cmcVlanDao.getCmcIpSubVlanScalarById(cmcId) == null) {
            cmcVlanDao.addCmcIpSubVlanScalar(cmcIpSubVlanScalar);
        } else {
            cmcVlanDao.updateCmcIpSubVlanScalar(cmcIpSubVlanScalar);
        }

        List<CmcIpSubVlanCfgEntry> cmcIpSubVlanCfgEntries = getCmcFacade(snmpParam.getIpAddress())
                .getCmcIpSubVlanCfgList(snmpParam);
        cmcVlanDao.refreshCmcIpSubVlanCfg(cmcId, cmcIpSubVlanCfgEntries);
    }

    @Override
    public List<CmcVlanConfigEntry> getCmcVlanList(Long cmcId) {
        List<CmcVlanConfigEntry> cmcVlanConfigEntrys = cmcVlanDao.getCmcVlanList(cmcId);
        // List<CmcVlanPrimaryIp> primaryIps = cmcVlanDao.getCmcVlanPriIpList(cmcId);
        List<CmcVifSubIpEntry> vifIps = cmcVlanDao.getCmcVlanVisIpList(cmcId);
        List<CmcVlanConfigEntry> result = new ArrayList<CmcVlanConfigEntry>();
        for (CmcVlanConfigEntry cmcVlan : cmcVlanConfigEntrys) {
            cmcVlan.setPriTag(CmcConstants.PRI_TAG_IS);
            // dhcp自动获取IP不为关闭状态或者为空
            if (null == cmcVlan.getDhcpAlloc() || CmcConstants.DHCP_ALLOC_OFF.equals(cmcVlan.getDhcpAlloc())) {
                if (cmcVlan.getIpAddr() != null && cmcVlan.getIpMask() != null) {
                    cmcVlan.setPriIpExist(CmcConstants.PRIIP_EXIST);
                } else {
                    cmcVlan.setPriIpExist(CmcConstants.PRIIP_NO_EXIST);
                }
            } else {
                cmcVlan.setPriIpExist(CmcConstants.PRIIP_EXIST);
            }
            // DHCP自动获取Ip为关闭状态时，才展示从IP列表
            for (CmcVifSubIpEntry subIpEntry : vifIps) {
                if (cmcVlan.getTopCcmtsVlanIndex().equals(subIpEntry.getTopCcmtsVifSubIpVlanIdx())) {
                    CmcVlanConfigEntry cmcVlanEntry = new CmcVlanConfigEntry();
                    // IP类型为非主IP
                    cmcVlanEntry.setPriTag(CmcConstants.PRI_TAG_NO);
                    cmcVlanEntry.setCmcId(cmcId);
                    cmcVlanEntry.setSecVidIndex(subIpEntry.getTopCcmtsVifSubIpSeqIdx());
                    cmcVlanEntry.setTopCcmtsVlanIndex(cmcVlan.getTopCcmtsVlanIndex());
                    cmcVlanEntry.setIpAddr(subIpEntry.getTopCcmtsVifSubIpAddr());
                    cmcVlanEntry.setIpMask(subIpEntry.getTopCcmtsVifSubIpMask());
                    // added by 黄东升 @2013-9-4 存在子IP的话，肯定存在主IP
                    cmcVlanEntry.setPriIpExist(CmcConstants.PRIIP_EXIST);
                    result.add(cmcVlanEntry);
                }
            }
        }
        result.addAll(cmcVlanConfigEntrys);
        return result;
    }

    @Override
    public CmcVlanConfigEntry getCmcVlanCfgById(Long cmcId, Integer topCcmtsVlanIndex) {
        return cmcVlanDao.getCmcVlanCfgById(cmcId, topCcmtsVlanIndex);
    }

    @Override
    public void addCmcVlan(Long cmcId, Integer topCcmtsVlanIndex) {
        snmpParam = getSnmpParamByEntityId(cmcId);
        CmcVlanConfigEntry cmcVlan = new CmcVlanConfigEntry();
        cmcVlan.setCmcId(cmcId);
        cmcVlan.setTopCcmtsVlanIndex(topCcmtsVlanIndex);
        getCmcFacade(snmpParam.getIpAddress()).createCmcVlanCfg(snmpParam, cmcVlan);
        cmcVlanDao.addCmcVlan(cmcVlan);
    }

    @Override
    public void deleteCmcVlan(Long cmcId, Integer topCcmtsVlanIndex) {
        snmpParam = getSnmpParamByEntityId(cmcId);
        CmcVlanConfigEntry cmcVlan = new CmcVlanConfigEntry();
        cmcVlan.setCmcId(cmcId);
        cmcVlan.setTopCcmtsVlanIndex(topCcmtsVlanIndex);
        getCmcFacade(snmpParam.getIpAddress()).destoryCmcVlanCfg(snmpParam, cmcVlan);
        cmcVlanDao.deleteCmcVlan(cmcVlan);
    }

    @Override
    public void refreshCmcVlan(Long cmcId) {
        snmpParam = getSnmpParamByEntityId(cmcId);
        // getCmcFacade(snmpParam.getIpAddress()).getCmcIpSubVlanCfgList(snmpParam);
    }

    @Override
    public void addCmcVlanPriIp(Long cmcId, Integer topCcmtsVlanIndex, String cmcVlanIp, String cmcVlanMask) {
        snmpParam = getSnmpParamByEntityId(cmcId);
        CmcVlanPrimaryIp cmcVlanPriIp = new CmcVlanPrimaryIp();
        cmcVlanPriIp.setCmcId(cmcId);
        cmcVlanPriIp.setTopCcmtsVifPriIpVlanId(topCcmtsVlanIndex);
        cmcVlanPriIp.setTopCcmtsVifPriIpAddr(cmcVlanIp);
        cmcVlanPriIp.setTopCcmtsVifPriIpMask(cmcVlanMask);
        getCmcFacade(snmpParam.getIpAddress()).createCmcVlanPrimaryIp(snmpParam, cmcVlanPriIp);
        cmcVlanDao.updateCmcVlanPriIp(cmcVlanPriIp);
    }

    @Override
    public Boolean checkCmcVlanIpExist(Long cmcId, String cmcVlanIp, String cmcVlanMask) {
        List<CmcVlanConfigEntry> vlanConfigEntries = cmcVlanDao.selectCmcVlanPriIp(cmcId, cmcVlanIp, cmcVlanMask);
        List<CmcVifSubIpEntry> vlanSubIpEntries = cmcVlanDao.selectCmcVlanSubIp(cmcId, cmcVlanIp, cmcVlanMask);
        if (vlanConfigEntries.size() > 0 || vlanSubIpEntries.size() > 0) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void addCmcVlanSecIp(Long cmcId, Integer topCcmtsVlanIndex, String cmcVlanIp, String cmcVlanMask) {
        snmpParam = getSnmpParamByEntityId(cmcId);
        CmcVifSubIpEntry cmcVlanSubIp = new CmcVifSubIpEntry();
        cmcVlanSubIp.setCmcId(cmcId);
        // 从IP索引为1-62
        cmcVlanSubIp.setTopCcmtsVifSubIpSeqIdx(CmcUtil.getAviableIndex(1, 62,
                cmcVlanDao.getCmcVlanSubIpIndexList(cmcId)));
        cmcVlanSubIp.setTopCcmtsVifSubIpVlanIdx(topCcmtsVlanIndex);
        cmcVlanSubIp.setTopCcmtsVifSubIpAddr(cmcVlanIp);
        cmcVlanSubIp.setTopCcmtsVifSubIpMask(cmcVlanMask);
        getCmcFacade(snmpParam.getIpAddress()).createCmcVifSubIpEntry(snmpParam, cmcVlanSubIp);
        cmcVlanDao.addCmcVlanSubIp(cmcVlanSubIp);
    }

    @Override
    public void modifyCmcVlanSecIp(Long cmcId, Integer topCcmtsVlanIndex, Integer secVidIndex, String cmcVlanIp,
            String cmcVlanMask) {
        snmpParam = getSnmpParamByEntityId(cmcId);
        CmcVifSubIpEntry cmcVlanSubIp = new CmcVifSubIpEntry();
        cmcVlanSubIp.setCmcId(cmcId);
        cmcVlanSubIp.setTopCcmtsVifSubIpSeqIdx(secVidIndex);
        cmcVlanSubIp.setTopCcmtsVifSubIpVlanIdx(topCcmtsVlanIndex);
        cmcVlanSubIp.setTopCcmtsVifSubIpAddr(cmcVlanIp);
        cmcVlanSubIp.setTopCcmtsVifSubIpMask(cmcVlanMask);
        getCmcFacade(snmpParam.getIpAddress()).setCmcVifSubIpEntry(snmpParam, cmcVlanSubIp);
        cmcVlanDao.updateCmcVlanSubIp(cmcVlanSubIp);
    }

    @Override
    public void deleteCmcVlanPriIp(Long cmcId, Integer topCcmtsVlanIndex) {
        snmpParam = getSnmpParamByEntityId(cmcId);
        CmcVlanPrimaryIp cmcVlanPri = new CmcVlanPrimaryIp();
        cmcVlanPri.setCmcId(cmcId);
        cmcVlanPri.setTopCcmtsVifPriIpVlanId(topCcmtsVlanIndex);
        getCmcFacade(snmpParam.getIpAddress()).destoryCmcVlanPrimaryIp(snmpParam, cmcVlanPri);
        cmcVlanDao.deleteCmcVlanPriIp(cmcVlanPri);
        //cmcVlanDao.deleteCmcVlanSubIpByPriIp(cmcId, topCcmtsVlanIndex);
    }

    @Override
    public void deleteCmcVlanSecIp(Long cmcId, Integer topCcmtsVifSubIpVlanIdx, Integer secVidIndex) {
        snmpParam = getSnmpParamByEntityId(cmcId);
        CmcVifSubIpEntry cmcVlanIpSec = new CmcVifSubIpEntry();
        cmcVlanIpSec.setCmcId(cmcId);
        cmcVlanIpSec.setTopCcmtsVifSubIpVlanIdx(topCcmtsVifSubIpVlanIdx);
        cmcVlanIpSec.setTopCcmtsVifSubIpSeqIdx(secVidIndex);
        getCmcFacade(snmpParam.getIpAddress()).destoryCmcVifSubIpEntry(snmpParam, cmcVlanIpSec);
        cmcVlanDao.deleteCmcVlanSubIp(cmcVlanIpSec);
    }

    @Override
    public void deleteAllCmcVlanSubIpByVlan(Long cmcId, Integer topCcmtsVifSubIpVlanIdx) {
        cmcVlanDao.deleteAllCmcVlanSubIpByVlan(cmcId, topCcmtsVifSubIpVlanIdx);
    }

    @Override
    public void updateCmcVlanPriIpDhcpCfg(Long cmcId, Integer topCcmtsVlanIndex, Integer dhcpAlloc, String option60) {
        snmpParam = getSnmpParamByEntityId(cmcId);
        CmcVlanDhcpAllocEntry oldDhcpAlloc = getCmcFacade(snmpParam.getIpAddress()).getCmcVlanPriIpDhcpCfg(snmpParam,
                topCcmtsVlanIndex);
        if (dhcpAlloc != null && oldDhcpAlloc.getTopCcmtsVlanDhcpAlloc().intValue() != dhcpAlloc.intValue()) {
            CmcVlanDhcpAllocEntry cmcDhcpAlloc = new CmcVlanDhcpAllocEntry();
            cmcDhcpAlloc.setCmcId(cmcId);
            cmcDhcpAlloc.setTopCcmtsVlanIndex(topCcmtsVlanIndex);
            cmcDhcpAlloc.setTopCcmtsVlanDhcpAlloc(dhcpAlloc);
            cmcDhcpAlloc.setTopCcmtsOption60(option60);
            try {
                getCmcFacade(snmpParam.getIpAddress()).setCmcVlanPriIpDhcpCfg(snmpParam, cmcDhcpAlloc);
            } catch (Exception e) {
                logger.info("{}", e);
            } finally {
                cmcVlanDao.updateCmcVlanDhcpAlloc(cmcDhcpAlloc);
            }
        }
    }

    public CmcVlanDao getCmcVlanDao() {
        return cmcVlanDao;
    }

    public void setCmcVlanDao(CmcVlanDao cmcVlanDao) {
        this.cmcVlanDao = cmcVlanDao;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.cmc.service.CmcVlanService#getCmcVlanListFromVlan0(java.lang.Long)
     */
    @Override
    public List<CmcVifSubIpEntry> getCmcVlanListFromVlan0(Long entityId) {
        return cmcVlanDao.getCmcVlanListFromVlan0(entityId);
    }

    @Override
    public void modifyCmcVlanPriIp(Long cmcId, Integer topCcmtsVlanIndex, String cmcVlanIp, String cmcVlanMask) {
        snmpParam = getSnmpParamByEntityId(cmcId);
        CmcVlanPrimaryIp cmcVlanPriIp = new CmcVlanPrimaryIp();
        cmcVlanPriIp.setCmcId(cmcId);
        cmcVlanPriIp.setTopCcmtsVifPriIpVlanId(topCcmtsVlanIndex);
        cmcVlanPriIp.setTopCcmtsVifPriIpAddr(cmcVlanIp);
        cmcVlanPriIp.setTopCcmtsVifPriIpMask(cmcVlanMask);
        getCmcFacade(snmpParam.getIpAddress()).modifyCmcVlanPrimaryIp(snmpParam, cmcVlanPriIp);
        cmcVlanDao.updateCmcVlanPriIp(cmcVlanPriIp);
    }

    @Override
    public void refreshCmcVlanConfigFromDevice(Long cmcId) {
        snmpParam = getSnmpParamByEntityId(cmcId);
        CmcVlanData data = getCmcFacade(snmpParam.getIpAddress()).refreshCmcVlanConfig(snmpParam, cmcId);
        // 插入CMC VLAN
        List<CmcVlanConfigEntry> cmcVlanConfigEntries = data.getCmcVlanConfigEntries();
        if (cmcVlanConfigEntries != null) {
            try {
                cmcVlanDao.batchInsertCmcVlan(cmcVlanConfigEntries, cmcId);
            } catch (Exception e) {
                logger.error("Syn cmcVlanConfigEntries error ", e);
            }
        }

        // 插入VLAN 主IP
        List<CmcVlanPrimaryIp> cmcVlanPrimaryIps = data.getCmcVlanPrimaryIps();
        if (cmcVlanPrimaryIps != null) {
            try {
                cmcVlanDao.batchUpdateCmcVlanPriIp(cmcVlanPrimaryIps, cmcId);
            } catch (Exception e) {
                logger.error("Syn cmcVlanPrimaryIps error ", e);
            }
        }

        // 插入CMC VLAN 子IP
        List<CmcVifSubIpEntry> cmcVifSubIpEntries = data.getCmcVifSubIpEntries();
        if (cmcVifSubIpEntries != null) {
            try {
                cmcVlanDao.batchInsertCmcVlanSubIp(cmcId, cmcVifSubIpEntries);
            } catch (Exception e) {
                logger.error("Syn cmcVifSubIpEntries error ", e);
            }
        }

        // 插入VLAN 主Ip dhcp信息
        List<CmcVlanDhcpAllocEntry> cmcVlanDhcpAllocEntries = data.getCmcVlanDhcpAllocEntries();
        if (cmcVlanDhcpAllocEntries != null) {
            try {
                cmcVlanDao.batchUpdateCmcVlanDhcpAlloc(cmcVlanDhcpAllocEntries, cmcId);
            } catch (Exception e) {
                logger.error("Syn cmcVlanDhcpAllocEntries error ", e);
            }
        }

    }

    @Override
    public List<CmcVlanConfigEntry> getVlanConfigList(Long entityId) {
        List<CmcVlanConfigEntry> ipList = getCmcVlanList(entityId);
        CmcSystemIpInfo systemIpInfo = cmcConfigService.getCmcIpInfo(entityId);
        CmcVlanConfigEntry cmcVlan = new CmcVlanConfigEntry();
        // IP类型为非主IP
        cmcVlan.setPriTag(CmcConstants.PRI_TAG_IS);
        cmcVlan.setCmcId(entityId);
        cmcVlan.setTopCcmtsVlanIndex(0);
        cmcVlan.setIpAddr(systemIpInfo.getTopCcmtsEthIpAddr());
        cmcVlan.setIpMask(systemIpInfo.getTopCcmtsEthIpMask());
        ipList.add(cmcVlan);
        List<CmcVifSubIpEntry> subIpList = getCmcVlanListFromVlan0(entityId);
        for (CmcVifSubIpEntry subIpEntry : subIpList) {
            CmcVlanConfigEntry cmcVlanEntry = new CmcVlanConfigEntry();
            // IP类型为非主IP
            cmcVlanEntry.setPriTag(CmcConstants.PRI_TAG_NO);
            cmcVlanEntry.setCmcId(entityId);
            cmcVlanEntry.setSecVidIndex(subIpEntry.getTopCcmtsVifSubIpSeqIdx());
            cmcVlanEntry.setTopCcmtsVlanIndex(0);
            cmcVlanEntry.setIpAddr(subIpEntry.getTopCcmtsVifSubIpAddr());
            cmcVlanEntry.setIpMask(subIpEntry.getTopCcmtsVifSubIpMask());
            ipList.add(cmcVlanEntry);
        }
        return ipList;
    }

    @Override
    public void insertEntityStates(CmcSynchronizedEvent event) {
        if (event.getEntityType().equals(entityTypeService.getCcmtswithagentType())) {
            long entityId = event.getEntityId();
            try {
                // 刷新VLAN配置
                refreshCmcVlanConfigFromDevice(entityId);
                logger.info("refreshCmcVlanConfigFromDevice finish");
            } catch (Exception e) {
                logger.error("refreshCmcVlanConfigFromDevice wrong", e);
            }
            try {
                // 刷新子网VLAN配置
                refreshCmcIpSubVlanCfg(entityId);
                logger.info("refreshCmcIpSubVlanCfg finish");
            } catch (Exception e) {
                logger.error("refreshCmcIpSubVlanCfg wrong", e);
            }
        }
    }

}
