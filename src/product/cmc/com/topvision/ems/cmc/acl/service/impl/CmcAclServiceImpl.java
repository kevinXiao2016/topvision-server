package com.topvision.ems.cmc.acl.service.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import com.topvision.ems.devicesupport.version.util.DeviceFuctionSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.cmc.acl.dao.CmcAclDao;
import com.topvision.ems.cmc.acl.facade.domain.CmcAclDefAction;
import com.topvision.ems.cmc.acl.facade.domain.CmcAclInfo;
import com.topvision.ems.cmc.acl.facade.domain.CmcAclInstall;
import com.topvision.ems.cmc.acl.service.CmcAclService;
import com.topvision.ems.cmc.ccmts.facade.domain.CmcAttribute;
import com.topvision.ems.cmc.ccmts.service.CmcService;
import com.topvision.ems.cmc.facade.CmcFacade;
import com.topvision.ems.cmc.service.impl.CmcBaseCommonService;
import com.topvision.framework.snmp.SnmpParam;

@Service("cmcAclService")
public class CmcAclServiceImpl extends CmcBaseCommonService implements CmcAclService {
    @Resource(name = "cmcAclDao")
    private CmcAclDao cmcAclDao;
    @Autowired
    private CmcService cmcService;
    private static int ROW_STATUS_ADD = 4;
    private static int ROW_STATUS_DEL = 6;

    @Override
    public CmcAclInfo getSingleAclInfo(Long cmcId, Integer aclID) {
        // SnmpParam snmpParam = getSnmpParamByCmcId(cmcId);
        // CmcFacade cmcFacade = getCmcFacade(snmpParam.getIpAddress());
        // CmcAclInfo aclInfo = new CmcAclInfo();
        // aclInfo.setTopCcmtsAclListIndex(aclID);
        // CmcAclInfo acl = cmcFacade.getAclInfo(snmpParam, aclInfo);
        CmcAclInfo acl = cmcAclDao.getOneAclInfo(cmcId, aclID);
        return acl;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.cmc.service.CmcAclService#addSingleAclInfo(com.topvision.ems.cmc.facade.
     * domain.CmcAclInfo)
     */
    @Override
    public CmcAclInfo addSingleAclInfo(Long cmcId, CmcAclInfo aclInfo) {
        SnmpParam snmpParam = getSnmpParamByCmcId(cmcId);
        CmcFacade cmcFacade = getCmcFacade(snmpParam.getIpAddress());
        aclInfo.setTopAclRowStatus(ROW_STATUS_ADD);
        cmcFacade.modifyAclInfo(snmpParam, aclInfo);
        CmcAclInfo acl = cmcFacade.getAclInfo(snmpParam, aclInfo);
        cmcAclDao.refreshOneAclInfo(cmcId, acl);
        return acl;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.cmc.service.CmcAclService#modifySingleAclInfo(com.topvision.ems.cmc.facade.
     * domain.CmcAclInfo)
     */
    @Override
    public CmcAclInfo modifySingleAclInfo(Long cmcId, CmcAclInfo aclInfo) {
        SnmpParam snmpParam = getSnmpParamByCmcId(cmcId);
        CmcFacade cmcFacade = getCmcFacade(snmpParam.getIpAddress());
        CmcAclInfo oldAcl = cmcAclDao.getOneAclInfo(cmcId, aclInfo.getTopCcmtsAclListIndex());
        // 解绑定或者修改绑定的时候，只设置position字段
        if (!"00:00:00:00".equalsIgnoreCase(oldAcl.getTopInstallPosition())) {
            CmcAclInstall install = new CmcAclInstall();
            install.setTopCcmtsAclListIndex(aclInfo.getTopCcmtsAclListIndex());
            install.setTopInstallPosition(aclInfo.getTopInstallPosition());
            cmcFacade.modifyAclInstall(snmpParam, install);
        } else {
            cmcFacade.modifyAclInfo(snmpParam, aclInfo);
        }
        CmcAclInfo acl = cmcFacade.getAclInfo(snmpParam, aclInfo);
        cmcAclDao.refreshOneAclInfo(cmcId, acl);
        return acl;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.cmc.service.CmcAclService#deleteSingleAclInfo(java.lang.Integer)
     */
    @Override
    public boolean deleteSingleAclInfo(Long cmcId, Integer aclID) {
        SnmpParam snmpParam = getSnmpParamByCmcId(cmcId);
        CmcFacade cmcFacade = getCmcFacade(snmpParam.getIpAddress());
        CmcAclInfo aclInfo = new CmcAclInfo();
        aclInfo.setTopAclRowStatus(ROW_STATUS_DEL);
        aclInfo.setTopCcmtsAclListIndex(aclID);
        @SuppressWarnings("unused")
        CmcAclInfo acl = cmcFacade.modifyAclInfo(snmpParam, aclInfo);
        cmcAclDao.deleteOneAclInfo(cmcId, aclID);
        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.cmc.service.CmcAclService#getAllAclPositionInfo()
     */
    @Override
    public CmcAclDefAction getAclPositionDefInfo(Long cmcId, Integer posion) {

        return cmcAclDao.getOnePositionDefAct(cmcId, posion);

    }

    @Override
    public boolean enablePositionDefAct(Long cmcID, Integer positionID, boolean isEnable) {

        SnmpParam snmpParam = getSnmpParamByCmcId(cmcID);
        CmcFacade cmcFacade = getCmcFacade(snmpParam.getIpAddress());
        CmcAclDefAction defAct = new CmcAclDefAction();
        defAct.setTopAclPositionIndex(positionID);

        /**
         * 0 丢弃报文，1 允许通过
         */
        defAct.setTopPositionDefAction(isEnable ? 1 : 0);
        cmcFacade.modifyAclPositionDefAct(snmpParam, defAct);
        List<CmcAclDefAction> allDefActList = cmcFacade.getAllAclPositionDefAct(snmpParam);
        cmcAclDao.refreshAllPositionDefAct(cmcID, allDefActList);
        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.cmc.service.CmcAclService#refreshAllAclInfo(java.lang.Long)
     */
    @Override
    public boolean refreshAllAclInfo(Long cmcId) {
        CmcAttribute cmcAttribute = cmcService.getCmcAttributeByCmcId(cmcId);
        SnmpParam snmpParam = getSnmpParamByCmcId(cmcId);
        CmcFacade cmcFacade = getCmcFacade(snmpParam.getIpAddress());
        List<CmcAclDefAction> allDefActList = cmcFacade.getAllAclPositionDefAct(snmpParam);
        List<CmcAclInfo> allAclList = cmcFacade.getAclAllList(snmpParam);
        for (CmcAclInfo cmcAclInfo : allAclList) {
            cmcAclInfo.setTopInstallPosition(cmcAclInfo.getTopInstallPosition(),
                    isNewAclActionMask(cmcAttribute.getDolVersion()));
        }
        cmcAclDao.refreshAllPositionDefAct(cmcId, allDefActList);
        cmcAclDao.refreshAllAclList(cmcId, allAclList);

        return true;
    }

    public CmcAclDao getCmcAclDao() {
        return cmcAclDao;
    }

    public void setCmcAclDao(CmcAclDao cmcAclDao) {
        this.cmcAclDao = cmcAclDao;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.cmc.service.CmcAclService#getOnePositionAclList(java.lang.Long,
     * java.lang.Integer)
     */
    @Override
    public List<CmcAclInfo> getOnePositionAclList(Long cmcId, Integer position) {
        return cmcAclDao.getOnePositionAclList(cmcId, position);

    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.cmc.service.CmcAclService#getOneNewAclId(java.lang.Long)
     */
    @Override
    public Integer getOneNewAclId(Long cmcId) {
        Integer newAclID = null;
        List<CmcAclInfo> allAclList = cmcAclDao.getAllAclList(cmcId);
        if (allAclList != null && allAclList.size() > 0) {
            Set<Integer> allID = new HashSet<Integer>();
            for (int i = 0; i < allAclList.size(); i++) {
                Integer aclId = allAclList.get(i).getTopCcmtsAclListIndex();
                allID.add(aclId);
            }
            for (int i = 1; i < 193; i++) {
                if (!allID.contains(i)) {
                    newAclID = i;
                    break;
                }
            }
        } else {
            newAclID = 1;
        }
        return newAclID;
    }

    /**
     * 判断升级AclActionMask是否为修改后的CCMTS-11569
     * 
     * @param dolVersion
     * @return
     */
    private boolean isNewAclActionMask(String dolVersion) {
        if (dolVersion != null) {
            if (DeviceFuctionSupport.compareVersion(dolVersion, "V2.2.9.0") >= 0) {
                return true;
            }
        }
        return false;
    }
}
