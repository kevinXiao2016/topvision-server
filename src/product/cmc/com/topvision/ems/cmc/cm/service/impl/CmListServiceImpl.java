package com.topvision.ems.cmc.cm.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.cmc.ccmts.facade.domain.CmcAttribute;
import com.topvision.ems.cmc.ccmts.service.CmcService;
import com.topvision.ems.cmc.cm.dao.CmDao;
import com.topvision.ems.cmc.cm.dao.CmListDao;
import com.topvision.ems.cmc.cm.domain.CcmtsCmRela;
import com.topvision.ems.cmc.cm.domain.CcmtsLocation;
import com.topvision.ems.cmc.cm.domain.Cm3Signal;
import com.topvision.ems.cmc.cm.domain.CmLocation;
import com.topvision.ems.cmc.cm.domain.CmLocationInfo;
import com.topvision.ems.cmc.cm.domain.CmSignal;
import com.topvision.ems.cmc.cm.domain.CmTopo;
import com.topvision.ems.cmc.cm.domain.OltCcmtsRela;
import com.topvision.ems.cmc.cm.domain.OltLocation;
import com.topvision.ems.cmc.cm.service.CmListService;
import com.topvision.ems.cmc.cm.service.CmService;
import com.topvision.ems.cmc.cm.service.CmSignalService;
import com.topvision.ems.cmc.domain.CmcCmNumStatic;
import com.topvision.ems.cmc.facade.domain.CmAttribute;
import com.topvision.ems.cmc.facade.domain.CmPartialSvcState;
import com.topvision.ems.cmc.facade.domain.CmcEntityRelation;
import com.topvision.ems.cmc.facade.domain.DocsIf3CmtsCmUsStatus;
import com.topvision.ems.cmc.perf.service.CmcPerfService;
import com.topvision.ems.cmc.performance.domain.CmStatusPerf;
import com.topvision.ems.cmc.performance.domain.CmStatusPerfResult;
import com.topvision.ems.cmc.performance.handle.CmStatusHandle;
import com.topvision.ems.cmc.service.impl.CmcBaseCommonService;
import com.topvision.ems.cmc.util.ResourcesUtil;
import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.facade.domain.PerformanceData;
import com.topvision.ems.message.Message;
import com.topvision.ems.message.MessagePusher;
import com.topvision.ems.network.service.EntityService;
import com.topvision.ems.performance.handle.PerformanceHandleIf;
import com.topvision.ems.template.service.EntityTypeService;
import com.topvision.framework.common.MacUtils;
import com.topvision.framework.snmp.SnmpParam;
import com.topvision.framework.utils.CmcIndexUtils;
import com.topvision.platform.domain.UserContext;
import com.topvision.platform.message.event.CmcSynchronizedEvent;
import com.topvision.platform.message.event.CmcSynchronizedListener;
import com.topvision.platform.message.service.MessageService;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 
 * @author YangYi
 * @created @2013-10-30-下午8:28:55 从CmServiceImpl拆分,CM列表
 * 
 */
@Service("cmListService")
public class CmListServiceImpl extends CmcBaseCommonService implements CmListService, CmcSynchronizedListener {
    @Resource(name = "cmStatusHandle")
    private PerformanceHandleIf cmStatusHandle;
    @Resource(name = "cmListDao")
    private CmListDao cmListDao;
    @Resource(name = "cmcPerfService")
    private CmcPerfService cmcPerfService;
    @Resource(name = "cmService")
    private CmService cmService;
    @Resource(name = "cmSignalService")
    private CmSignalService cmSignalService;
    @Resource(name = "messagePusher")
    private MessagePusher messagePusher;
    @Resource(name = "cmDao")
    private CmDao cmDao;
    @Autowired
    private CmcService cmcService;
    @Autowired
    private MessageService messageService;
    @Autowired
    private EntityService entityService;

    @Resource(name = "entityTypeService")
    private EntityTypeService entityTypeService;

    @Override
    public void initialize() {
        super.initialize();
        messageService.addListener(CmcSynchronizedListener.class, this);
    }

    @Override
    public void destroy() {
        super.destroy();
        messageService.removeListener(CmcSynchronizedListener.class, this);
    }

    @Override
    public void insertEntityStates(CmcSynchronizedEvent event) {
        long entityId = event.getEntityId();
        // 刷新p-online相关数据
        try {
            refreshCmPartialSvcState(entityId);
            logger.info("refreshCmPartialSvcState finish");
        } catch (Exception e) {
            logger.error("refreshCmPartialSvcState wrong", e);
        }
    }

    public void refreshCmPartialSvcState(Long entityId) {
        snmpParam = getSnmpParamByEntityId(entityId);
        List<CmPartialSvcState> data = getCmcFacade(snmpParam.getIpAddress()).refreshCmPartialSvcState(snmpParam);

        saveCmPartialSvcState(entityId, data);
    }

    @Override
    public void saveCmPartialSvcState(Long entityId, List<CmPartialSvcState> partialData) {
        // 取到数据库中该entityId对应的所有cmcId/cmcIndex
        List<CmcEntityRelation> relations = cmcDao.getCmcEntityRelationByEntityId(entityId);

        // 转换成以cmcId为key的map结构
        Map<Long, CmcEntityRelation> relationMap = new HashMap<Long, CmcEntityRelation>();
        for (CmcEntityRelation relation : relations) {
            relationMap.put(relation.getCmcIndex(), relation);
        }

        // 遍历partial service数据，填充cmcId
        Long cmIndex;
        Long cmcIndex;
        CmcEntityRelation curRelation;
        for (CmPartialSvcState partial : partialData) {
            cmIndex = partial.getCmPartialSvcIndex();
            cmcIndex = CmcIndexUtils.getCmcIndexFromCmIndex(cmIndex);
            curRelation = relationMap.get(cmcIndex);
            if (curRelation != null) {
                partial.setCmcId(curRelation.getCmcId());
            }
        }

        cmListDao.updateCmPartialSvcState(entityId, partialData);
    }

    @Override
    public JSONArray loadDeviceListByType(Long type) {
        JSONArray array = new JSONArray();
        List<Map<String, String>> devices = cmListDao.loadDeviceListByType(type);
        for (Map<String, String> deviceMap : devices) {
            JSONObject json = new JSONObject();
            json.put("entityId", deviceMap.get("entityId"));
            json.put("name", deviceMap.get("name"));
            json.put("ip", deviceMap.get("ip"));
            array.add(json);
        }
        return array;
    }

    @Override
    public JSONArray loadDeviceListByTypeId(Long typeId) {
        JSONArray array = new JSONArray();
        List<Map<String, String>> devices = cmListDao.loadDeviceListByTypeId(typeId);
        for (Map<String, String> deviceMap : devices) {
            JSONObject json = new JSONObject();
            json.put("entityId", deviceMap.get("entityId"));
            json.put("name", deviceMap.get("name"));
            json.put("ip", deviceMap.get("ip"));
            array.add(json);
        }
        return array;
    }

    @Override
    public JSONArray loadCcmtsOfPon(Long entityId, Long ponId) {
        JSONArray array = new JSONArray();
        List<Map<String, String>> devices = cmListDao.loadCcmtsOfPon(entityId, ponId);
        for (Map<String, String> deviceMap : devices) {
            JSONObject json = new JSONObject();
            json.put("cmcId", deviceMap.get("cmcId"));
            json.put("name", deviceMap.get("name"));
            array.add(json);
        }
        return array;
    }

    @Override
    public List<CmAttribute> getCmList(Map<String, Object> queryMap) {
        List<CmAttribute> cmAttributes = cmListDao.selectCmList(queryMap);

        for (CmAttribute cm : cmAttributes) {
            // CMTS的上下行信道展示需要特殊处理
            try {
                if (entityTypeService.isCmts(cm.getCmcDeviceStyle())) {
                    handleCmtsChannel(cm);
                }
            } catch (Exception e) {
                logger.debug("handleCmtsChannel error" + e.getMessage());
            }
        }
        return cmAttributes;
    }

    @Override
    public Integer getCmNum(Map<String, Object> queryMap) {
        Integer cmNum = cmListDao.selectCmListNum(queryMap);
        return cmNum;
    }

    private void handleCmtsChannel(CmAttribute cmAttribute) {
        // 获取该CMTS的上下行信道的ifDescr
        // 获取上下行信道索引(X/X/X)
        String upChannelIndexStr = cmAttribute.getUpChannelIfName();
        String downChannelIndexStr = cmAttribute.getDownChannelIfName();
        // 赋给处理对象
        cmAttribute.setUpChannelIndexString(upChannelIndexStr);
        cmAttribute.setDownChannelIndexString(downChannelIndexStr);
    }

    @Override
    public CmLocationInfo getCmLocation(Long cmId, Long cmcDeviceStyle) {
        CmLocationInfo cmLocationInfo = new CmLocationInfo();
        Long cmcId = cmListDao.selectCmcIdByCmId(cmId);
        if (entityTypeService.isCcmtsWithoutAgent(cmcDeviceStyle)) {
            // 只有8800A才有OLT信息和OLT-CC关联关系信息
            Long oltEntityId = cmListDao.selectOltIdByCmcId(cmcId);
            Long onuIndex = cmListDao.selectOnuIndex(oltEntityId, cmcId);// 用于获取ONU
                                                                         // pon的光功率
            Long ponIndex = cmListDao.selectPonIndex(oltEntityId, cmcId);

            // -------
            // OLT部分start------------------------------------------------
            OltLocation oltLocation = new OltLocation();
            oltLocation = cmListDao.selectOltLocation(oltEntityId);
            // pon出速率
            Double ponOutSpeed = cmListDao.selectPonOutSpeed(oltEntityId, ponIndex);
            if (ponOutSpeed != null && ponOutSpeed > 0) {
                oltLocation.setPonOutSpeed(ponOutSpeed);
            }
            // olt最高告警级别获取
            Integer oltMaxAlarmLevel = cmListDao.selectMaxAlarmLevel(oltEntityId);
            if (oltMaxAlarmLevel != null) {
                oltLocation.setMaxAlarmLevel(oltMaxAlarmLevel);
            }
            if (oltLocation != null) {
                cmLocationInfo.setOltLocation(oltLocation);
            }
            // -------
            // OLT部分end--------------------------------------------------

            // -------
            // OLT和CCMTS(CMTS)连接部分start----------------------------------
            OltCcmtsRela oltCcmtsRela = new OltCcmtsRela();
            OltCcmtsRela oltCcmtsRelaForOnuPon = cmListDao.selectOltCcmtsRelaForOnuPon(cmcId, onuIndex);
            OltCcmtsRela oltCcmtsRelaForPon = cmListDao.selectOltCcmtsRelaForPon(oltEntityId, ponIndex);
            if (oltCcmtsRelaForOnuPon != null) {
                if (oltCcmtsRelaForOnuPon.getOnuPonOutPower() != null) {
                    oltCcmtsRela.setOnuPonOutPower(oltCcmtsRelaForOnuPon.getOnuPonOutPower());
                }
                if (oltCcmtsRelaForOnuPon.getOnuPonInPower() != null) {
                    oltCcmtsRela.setOnuPonInPower(oltCcmtsRelaForOnuPon.getOnuPonInPower());
                }
            }
            if (oltCcmtsRelaForPon != null) {
                if (oltCcmtsRelaForPon.getPonOutPower() != null) {
                    oltCcmtsRela.setPonOutPower(oltCcmtsRelaForPon.getPonOutPower());
                }
                if (oltCcmtsRelaForPon.getPonInPower() != null) {
                    oltCcmtsRela.setPonInPower(oltCcmtsRelaForPon.getPonInPower());
                }
            }
            if (oltCcmtsRela != null) {
                cmLocationInfo.setOltCcmtsRela(oltCcmtsRela);
            }
            // ------- OLT和CCMTS(CMTS)连接部分end----------------------------------
        }

        // CM拓扑信息，上联CC所在的地域，可能存在多个
        List<CmTopo> cmTopos = new ArrayList<CmTopo>();
        cmTopos = cmListDao.selectCmTopos(cmcId);
        if (cmTopos != null) {
            for (int i = 0; i < cmTopos.size(); i++) {
                cmTopos.get(i).setFolderName(ResourcesUtil.getString(cmTopos.get(i).getFolderName()));
            }
            cmLocationInfo.setCmTopos(cmTopos);
        }

        // ---------
        // CCMTS(CMTS)部分start----------------------------------------------
        CcmtsLocation ccmtsLocation = new CcmtsLocation();
        ccmtsLocation = cmListDao.getCcmtsLocation(cmcId, cmId);
        // 处理CMTS在cmcattribute中无状态的情况
        if (entityTypeService.isCmts(cmcDeviceStyle)) {
            Integer state = cmListDao.getCmtsStateById(cmcId);
            ccmtsLocation.setState(state);
        }
        CmcCmNumStatic cmcCmNumStatic = cmcPerfService.getCmcCmNumStatic(cmcId);
        if (cmcCmNumStatic != null) {
            if (cmcCmNumStatic.getCmNumOnline() != null) {
                ccmtsLocation.setCmNumOnline(cmcCmNumStatic.getCmNumOnline());
            }
            if (cmcCmNumStatic.getCmNumTotal() != null) {
                ccmtsLocation.setCmNumTotal(cmcCmNumStatic.getCmNumTotal());
            }
        }
        // CCMTS的最高告警级别
        Integer ccmtsMaxAlarmLevel = cmListDao.getCcmtsMaxAlarmLevel(cmcId, ccmtsLocation.getMac());
        if (ccmtsMaxAlarmLevel != null) {
            ccmtsLocation.setMaxAlarmLevel(ccmtsMaxAlarmLevel);
        }
        if (ccmtsLocation != null) {
            cmLocationInfo.setCcmtsLocation(ccmtsLocation);
        }
        // ---------
        // CCMTS(CMTS)部分end----------------------------------------------

        // ---------
        // CCMTS(CMTS)和CM连接部分start--------------------------------------
        CcmtsCmRela ccmtsCmRela = new CcmtsCmRela();
        Long ccmtsOutPower = cmListDao.selectCcmtsOutPower(cmcId, cmId);
        Long ccmtsInPower = cmListDao.selectCcmtsInPower(cmId);
        if (ccmtsOutPower != null) {
            ccmtsCmRela.setCcmtsOutPower(ccmtsOutPower);
        }
        if (ccmtsInPower != null) {
            ccmtsCmRela.setCcmtsInPower(ccmtsInPower);
        }
        // CM侧的信号
        CmSignal cmSignal = cmListDao.selectCmsignal(cmId);
        if (cmSignal != null) {
            if (cmSignal.getDownChannelTx() != null) {
                ccmtsCmRela.setCmInPower(cmSignal.getDownChannelTx());
            }
            if (cmSignal.getUpChannelTx() != null) {
                ccmtsCmRela.setCmOutPower(cmSignal.getUpChannelTx());
            }
        }

        if (ccmtsCmRela != null) {
            cmLocationInfo.setCcmtsCmRela(ccmtsCmRela);
        }

        // --------- CCMTS(CMTS)和CM连接部分end--------------------------------------

        // ---------
        // CM部分start-------------------------------------------------------
        CmLocation cmLocation = new CmLocation();
        cmLocation = cmListDao.selectCmLocation(cmId);
        if (cmSignal != null) {
            if (cmSignal.getDownChannelFrequency() != null && Double.valueOf(cmSignal.getDownChannelFrequency()) > 0) {
                cmLocation.setDownChannelFrequency(cmSignal.getDownChannelFrequency());
            }
            if (cmSignal.getDownChannelSnr() != null && Double.valueOf(cmSignal.getDownChannelSnr()) > 0) {
                cmLocation.setDownChannelSnr(cmSignal.getDownChannelSnr());
            }
            if (cmSignal.getDownChannelTx() != null) {
                cmLocation.setDownChannelTx(cmSignal.getDownChannelTx());
            }
            if (cmSignal.getUpChannelFrequency() != null && Double.valueOf(cmSignal.getUpChannelFrequency()) > 0) {
                cmLocation.setUpChannelFrequency(cmSignal.getUpChannelFrequency());
            }
            if (cmSignal.getUpChannelTx() != null) {
                cmLocation.setUpChannelTx(cmSignal.getUpChannelTx());
            }
        }

        Long cpeNum = cmListDao.selectCpeNum(cmId);
        if (cpeNum != null) {
            cmLocation.setCpeNum(cpeNum);
        }
        Integer cmMaxAlarmLevel = cmListDao.getCmMaxAlarmLevel(cmLocation.getIp());
        if (cmMaxAlarmLevel != null) {
            cmLocation.setMaxAlarmLevel(cmMaxAlarmLevel);
        }
        if (cmLocation != null) {
            cmLocationInfo.setCmLocation(cmLocation);
        }
        // ---------
        // CM部分end-------------------------------------------------------
        return cmLocationInfo;
    }

    @Override
    public void restartCm(Long cmId, String dwrId) {
        // 重启CM
        int result = cmService.resetCm(cmId);
        // seesionId,在推送时使用
        String seesionId = ServletActionContext.getRequest().getSession().getId();
        Message message = new Message("refreshAllOnLineCm");
        message.addSessionId(seesionId);
        message.setId(dwrId);
        JSONObject json = new JSONObject();
        json.put("cmId", cmId);
        if (result != 0) {
            json.put("success", false);
        } else {
            json.put("success", true);
        }
        message.setData(json);
        messagePusher.sendMessage(message);
    }
    
    @Override
    public Integer clearSingleCm(Long cmId) {
        return cmService.clearSingleCM(cmId);
    }

    @Override
    public JSONObject refreshCm(Long cmcId, Long cmId, Long statusIndex, String cmMac, Long cmcDeviceStyle,
            UserContext uc) {
        JSONObject json = new JSONObject();
        SnmpParam snmpParam = getSnmpParamByCmcId(cmcId);
        CmAttribute cmAttribute = getCmFacade(snmpParam.getIpAddress()).getCmAttributeOnDol(snmpParam, statusIndex,
                cmMac);
        if (cmAttribute != null && cmAttribute.isCmOnline()) {// CM信息被获取并且在线
            json.put("success", true);
            cmAttribute.setCmcId(cmcId);
            cmService.refreshSingleCmAttribute(cmAttribute);
            // 需要同步刷新信号质量
            cmSignalService.refreshSignalWithSave(cmId);
            // 从数据库中取出该条CM相关属性,并对最近刷新时间做处理
            cmAttribute = cmSignalService.getCmSignal(cmId);
            // add by fanzidong,格式化mac地址
            String macRule = "";
            if (uc != null) {
                macRule = uc.getMacDisplayStyle();
            } else {
                macRule = "6#M#U";
            }
            String formatedMac = MacUtils.convertMacToDisplayFormat(cmAttribute.getStatusMacAddress(), macRule);
            cmAttribute.setStatusMacAddress(formatedMac);
            // add by fanzidong,需要特殊处理CMTS的MAC地址
            if (entityTypeService.isCmts(cmAttribute.getCmcDeviceStyle())) {
                handleCmtsChannel(cmAttribute);
            }
            json.put("cmAttribute", cmAttribute);
        } else {
            json.put("success", false);
        }
        return json;
    }

    @Override
    public List<CmAttribute> getCmByCmcId(Map<String, Object> queryMap) {
        List<CmAttribute> cmList = cmListDao.getCmByCmcId(queryMap);
        for (CmAttribute cmAttribute : cmList) {
            List<DocsIf3CmtsCmUsStatus> docsIf3CmtsCmUsStatusList = cmDao
                    .queryDocsIf3CmtsCmUsStatusList(cmAttribute.getCmId());
            cmAttribute.setDocsIf3CmtsCmUsStatusList(docsIf3CmtsCmUsStatusList);
            // YangYi添加@20130824,将上行信道SNR单独显示
            /*
             * if (docsIf3CmtsCmUsStatusList.size() > 0) { for (DocsIf3CmtsCmUsStatus
             * docsIf3CmtsCmUsStatus : docsIf3CmtsCmUsStatusList) { if
             * (cmAttribute.getUpChannelId().equals(docsIf3CmtsCmUsStatus. getUpChannelId())) {
             * cmAttribute.setUpChannelSnr(docsIf3CmtsCmUsStatus. getCmUsStatusSignalNoiseString());
             * } } } else { cmAttribute.setUpChannelSnr(cmAttribute.
             * getDocsIfCmtsCmStatusSignalNoiseString()); }
             */

            if (cmAttribute.getTopCmFlapInsertionFailNum() != null && cmAttribute.getTopCmFlapInsertionFailNum() < 0) {
                cmAttribute.setTopCmFlapInsertionFailNum(0);
            }
        }
        return cmList;
    }

    @Override
    public Integer getCmNumByCmcId(Map<String, Object> queryMap) {
        Integer cmNum = cmListDao.getCmNumByCmcId(queryMap);
        return cmNum;
    }

    @Override
    public void refreshCmList(Long entityId, Long cmcId) {
        Entity entity = entityService.getEntity(entityId);
        SnmpParam snmpParam = getSnmpParamByEntityId(entityId);
        Long startIndex = 0L;
        Long endIndex = Long.MAX_VALUE;
        if (cmcId != null) {
            CmcAttribute cmcAttribute = cmcService.getCmcAttributeByCmcId(cmcId);
            if (cmcAttribute != null) {
                startIndex = cmcAttribute.getCmcIndex();
                endIndex = CmcIndexUtils.getNextCmcIndex(cmcAttribute.getCmcIndex());
            }
        }
        List<CmAttribute> cmList = getCmFacade(entity.getIp()).getCmAttributeInfos(snmpParam, startIndex, endIndex);
        CmStatusPerfResult cmStatusPerfResult = new CmStatusPerfResult(new CmStatusPerf());
        cmStatusPerfResult.setEntityId(entityId);
        cmStatusPerfResult.setCmcId(cmcId);
        cmStatusPerfResult.setCmAttributes(cmList);
        cmStatusPerfResult.setRealtimeRefresh(true);
        cmStatusPerfResult.setAllDevice(cmcId == null);
        cmStatusPerfResult.setCmArrayEmpty(cmList.isEmpty());
        cmStatusPerfResult.setDt(System.currentTimeMillis());
        PerformanceData performanceData = new PerformanceData(entityId, CmStatusHandle.HANDLE_ID, cmStatusPerfResult);
        cmStatusHandle.handle(performanceData);
        // add by fanzidong,刷新CM列表时更新partial service信道信息
        refreshCmPartialSvcState(entityId);
    }

    @Override
    public List<Cm3Signal> getCmSignalByCmIds(List<Long> cmIds) {
        return cmListDao.getCmSignalByCmIds(cmIds);
    }



}
