package com.topvision.ems.mobile.service.impl;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.topvision.ems.cm.pnmp.facade.domain.PnmpCmData;
import com.topvision.ems.cm.pnmp.facade.domain.PnmpTargetThreshold;
import com.topvision.ems.cm.pnmp.service.PnmpCmDataService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.cm.pnmp.facade.domain.PnmpCmData;
import com.topvision.ems.cmc.cm.dao.CcmtsCmListDao;
import com.topvision.ems.cmc.cm.service.CcmtsCmListService;
import com.topvision.ems.cmc.cm.service.CmListService;
import com.topvision.ems.cmc.cm.service.CmService;
import com.topvision.ems.cmc.cm.service.CmSignalService;
import com.topvision.ems.cmc.domain.CmImportInfo;
import com.topvision.ems.cmc.facade.domain.CmAttribute;
import com.topvision.ems.cmc.remotequerycm.service.CmRemoteQueryService;
import com.topvision.ems.cmc.service.impl.CmcBaseCommonService;
import com.topvision.ems.mobile.dao.MCmDao;
import com.topvision.ems.mobile.domain.Cm;
import com.topvision.ems.mobile.domain.CmCmtsRelation;
import com.topvision.ems.mobile.domain.CmInCmList;
import com.topvision.ems.mobile.domain.CmRelative;
import com.topvision.ems.mobile.domain.CmtsCm;
import com.topvision.ems.mobile.domain.MtrSnrOverlap;
import com.topvision.ems.mobile.facade.MCmcFacade;
import com.topvision.ems.mobile.service.MCmService;
import com.topvision.framework.snmp.SnmpParam;
import com.topvision.platform.util.highcharts.domain.Point;

@Service("mCmService")
public class MCmServiceImpl extends CmcBaseCommonService implements MCmService {
    @Resource(name = "mCmDao")
    private MCmDao mCmDao;
    @Resource(name = "cmListService")
    private CmListService cmListService;
    @Resource(name = "cmService")
    private CmService cmService;
    @Resource(name = "cmRemoteQueryService")
    private CmRemoteQueryService cmRemoteQueryService;
    @Resource(name = "ccmtsCmListDao")
    private CcmtsCmListDao ccmtsCmListDao;
    @Resource(name = "cmSignalService")
    private CmSignalService cmSignalService;
    @Autowired
    private PnmpCmDataService pnmpCmDataService;

    private String[] docsisModes = new String[] { "", "1.0", "1.1", "1.2", "1.3" };
    private String[] realtimeStatusValues = new String[] { "", "other", "ranging", "rangingAborted", "rangingComplete",
            "ipComplete", "registrationComplete", "accessDenied", "operational", "registeredBPIInitializing" };

    @Override
    public List<CmtsCm> getCmListByCmtsId(Map<String, Object> map) {
        return mCmDao.getCmListByCmtsId(map);
    }

    @Override
    public Long getCmListSizeByCmtsId(Map<String, Object> map) {
        return mCmDao.getCmListSizeByCmtsId(map);
    }

    @Override
    public Cm getCmByCmId(Long cmId) {
        return mCmDao.getCmByCmId(cmId);
    }

    @Override
    public void refreshCm(Long cmId) {
        CmCmtsRelation ccr = mCmDao.getCmCmtsRelation(cmId);
        Long cmcId = ccr.getCmtsId();
        Long statusIndex = ccr.getCmStatusIndex();
        String cmMac = ccr.getCmMac();
        SnmpParam snmpParam = getSnmpParamByCmcId(cmcId);
        CmAttribute cmAttribute = null;
        try {
            cmAttribute = getCmFacade(snmpParam.getIpAddress()).getCmAttributeOnDol(snmpParam, statusIndex, cmMac);
        } catch (Exception e) {
            logger.debug("MCmServiceImpl refreshCm getCmAttributeOnDol failed", e);
        }
        if (cmAttribute != null) {
            cmAttribute.setCmcId(cmcId);
            try {
                cmService.refreshSingleCmAttribute(cmAttribute);
            } catch (Exception e) {
                logger.debug("MCmServiceImpl refreshCm refreshSingleCmAttribute failed", e);
            }
            try {
                cmSignalService.refreshSignalWithSave(cmId);
            } catch (Exception e) {
                logger.debug("MCmServiceImpl refreshCm insertOrUpdateCmSignal failed", e);
            }
        }
    }

    @Override
    public Long getOnlineCmListSizeByCmtsId(Map<String, Object> map) {
        return mCmDao.getOnlineCmListSizeByCmtsId(map);
    }

    @Override
    public List<CmtsCm> getRealtimeData(List<CmtsCm> cmList, Long cmcId) {
        snmpParam = getSnmpParamByCmcId(cmcId);
        MCmcFacade mCmcFacade = getMCmcFacade(snmpParam.getIpAddress());
        for (CmtsCm cm : cmList) {
            try {
                cm.setDocsisMode(mCmcFacade.getDocsisMode(snmpParam, cm.getCmIndex()));
            } catch (Exception e) {
                cm.setDocsisMode(0);
                logger.error("cm getDocsisMode error cmIndex = " + cm.getCmIndex(), e);
            }
            cm.setRealtimeState(mCmcFacade.getRealtimeStatusValue(snmpParam, cm.getCmIndex()));
        }
        return cmList;
    }

    public SnmpParam getSnmpParamByCmcId(Long cmcId) {
        Long entityId = cmcDao.getEntityIdByCmcId(cmcId);
        return entityDao.getSnmpParamByEntityId(entityId);
    }

    private MCmcFacade getMCmcFacade(String ip) {
        return getFacadeFactory().getFacade(ip, MCmcFacade.class);
    }

    @Override
    public List<CmInCmList> getCmList(Map<String, Object> map) {
        return mCmDao.getCmList(map);
    }

    @Override
    public Long getCmListCount(Map<String, Object> map) {
        return mCmDao.getCmListCount(map);
    }

    @Override
    public CmtsCm getCmBaseInfo(Long cmId) {
        return mCmDao.getCmBaseInfo(cmId);
    }

    @Override
    public List<CmtsCm> getCmtsCmList(Map<String, Object> map) {
        return mCmDao.getCmtsCmList(map);
    }

    @Override
    public Integer getCmtsCmOnlineCount(Map<String, Object> map) {
        return mCmDao.getCmtsCmOnlineCount(map);
    }

    @Override
    public Integer getCmtsCmTotalCount(Map<String, Object> map) {
        return mCmDao.getCmtsCmTotalCount(map);
    }

    @Override
    public List<CmInCmList> getCmListWithRegion(Map<String, Object> map) {
        return mCmDao.getCmListWithRegion(map);
    }

    @Override
    public Long getCmListCountWithRegion(Map<String, Object> map) {
        return mCmDao.getCmListCountWithRegion(map);
    }
	@Override
    public void updateOrInsertImg(Cm cm) {
        mCmDao.updateOrInsertImg(cm);
    }

    @Override
    public String getCmProgramImg(String cmMac) {
        return mCmDao.getCmProgramImg(cmMac);
    }

    @Override
    public PnmpCmData realPnmp(Long cmtsId, String cmMac) {
        return pnmpCmDataService.realPnmp(cmtsId,cmMac);
    }

    @Override
    public String pingDocsis(Long cmtsId,String cmMac) {
        return pnmpCmDataService.pingDocsis(cmtsId,cmMac);
    }

    @Override
    public List<CmRelative> getRelavtiveCm(Map<String, Object> queryMap) {
        return mCmDao.getRelavtiveCm(queryMap);
    }

    @Override
    public String moveCmChannel(Long cmtsId,Long channelId,String cmMac) {
        return pnmpCmDataService.moveCmChannel(cmtsId,channelId,cmMac);
    }

    @Override
    public List<MtrSnrOverlap> getMtrSnrGraph(String cmMac, long time, long time2) {
        return mCmDao.getMtrSnrGraph(cmMac,time,time2);
    }

    @Override
    public List<Integer> getUpchannelList(Long cmtsId) {
        return mCmDao.getUpchannelList(cmtsId);
    }

    @Override
    public List<PnmpTargetThreshold> getMtrThresholds() {
        return mCmDao.getMtrThresholds();
    }

    @Override
    public CmImportInfo getCmBossInfo(String cmMac) {
        return mCmDao.getCmBossInfo(cmMac);
    }
}
