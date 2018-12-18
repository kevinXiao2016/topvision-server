package com.topvision.ems.epon.onu.service.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.epon.onu.dao.OnuAssemblyDao;
import com.topvision.ems.epon.onu.dao.OnuDao;
import com.topvision.ems.epon.onu.domain.OltOnuAttribute;
import com.topvision.ems.epon.onu.domain.OltOnuOpticalInfo;
import com.topvision.ems.epon.onu.domain.OltUniAttribute;
import com.topvision.ems.epon.onu.domain.OltUniExtAttribute;
import com.topvision.ems.epon.onu.domain.OnuCatvInfo;
import com.topvision.ems.epon.onu.domain.OnuEventLogEntry;
import com.topvision.ems.epon.onu.domain.OnuInfo;
import com.topvision.ems.epon.onu.domain.OnuQualityInfo;
import com.topvision.ems.epon.onu.domain.UniPort;
import com.topvision.ems.epon.onu.facade.OnuFacade;
import com.topvision.ems.epon.onu.facade.UniFacade;
import com.topvision.ems.epon.onu.service.OnuAssemblyService;
import com.topvision.ems.epon.onu.service.OnuOnOffRecordCollectService;
import com.topvision.ems.epon.onu.service.OnuWanService;
import com.topvision.ems.epon.onu.service.RogueOnuService;
import com.topvision.ems.epon.optical.domain.OltPonOptical;
import com.topvision.ems.epon.optical.service.OltOpticalService;
import com.topvision.ems.epon.performance.dao.OnuPerfDao;
import com.topvision.ems.epon.performance.domain.OnuLinkCollectInfo;
import com.topvision.ems.epon.performance.domain.PerfOnuQualityHistory;
import com.topvision.ems.epon.performance.service.OnuPerfService;
import com.topvision.ems.epon.topology.service.OnuRefreshService;
import com.topvision.ems.epon.univlanprofile.domain.UniVlanBindTable;
import com.topvision.ems.epon.univlanprofile.service.UniVlanProfileService;
import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.fault.domain.Alert;
import com.topvision.ems.fault.domain.AlertType;
import com.topvision.ems.fault.domain.HistoryAlert;
import com.topvision.ems.gpon.onu.domain.GponOnuQualityInfo;
import com.topvision.ems.gpon.onu.facade.GponOnuFacade;
import com.topvision.ems.gpon.onuauth.GponConstant;
import com.topvision.ems.message.Message;
import com.topvision.ems.message.MessagePusher;
import com.topvision.ems.network.service.EntityService;
import com.topvision.framework.common.DateUtils;
import com.topvision.framework.service.BaseService;
import com.topvision.framework.snmp.SnmpParam;
import com.topvision.framework.utils.EponConstants;
import com.topvision.framework.utils.EponIndex;
import com.topvision.platform.facade.FacadeFactory;

/**
 * 将原OnuDeviceService中的方法移动到这里，原OnuServiceService用来做ONU设备列表service
 * 
 * @author w1992wishes
 * @created @2017年12月22日-上午9:47:17
 *
 */
@Service
public class OnuAssemblyServiceImpl extends BaseService implements OnuAssemblyService {

    @Autowired
    private OnuAssemblyDao onuAssemblyDao;
    @Autowired
    private OnuDao onuDao;
    @Autowired
    private EntityService entityService;
    @Autowired
    private OnuRefreshService onuRefreshService;
    @Autowired
    private UniVlanProfileService uniVlanProfileService;
    @Autowired
    private FacadeFactory facadeFactory;
    @Autowired
    private OnuPerfDao onuPerfDao;
    @Autowired
    private MessagePusher messagePusher;
    @Autowired
    private OnuPerfService onuPerfService;
    @Autowired
    private OltOpticalService oltOpticalService;
    @Autowired
    private OnuOnOffRecordCollectService onuOnOffRecordCollectService;
    @Autowired
    private OnuWanService onuWanService;
    @Autowired
    private RogueOnuService rogueOnuService;

    @Override
    public OltPonOptical getOltOnuPonOptical(Long onuId) {
        OltPonOptical oltPonOptical = onuAssemblyDao.getOltOnuPonOptical(onuId);
        // 从性能采集数据中获取Olt pon口光信息
        if (oltPonOptical != null) {
            OltOnuOpticalInfo oltOptical = onuDao.selectOltOunOpticalInfo(oltPonOptical.getEntityId(),
                    oltPonOptical.getPortIndex());
            if (oltOptical != null) {
                oltPonOptical.setTransPower(oltOptical.getPonOpticalTrans());
                oltPonOptical.setRevPower(oltOptical.getPonOpticalRev());
            }
        }
        return oltPonOptical;
    }

    @Override
    public List<UniPort> loadUniList(Long onuId) {
        return onuAssemblyDao.selectUniList(onuId);
    }

    @Override
    public List<OnuInfo> queryForOnuList(Map<String, Object> queryMap) {
        return onuAssemblyDao.selectOnuList(queryMap);
    }

    @Override
    public void refreshOnuInfo(Long entityId, Long onuId, Long onuIndex) {

        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        OltOnuAttribute onuAttribute = onuDao.getOnuEntityById(onuId);

        refreshOnuCatvInfo(snmpParam, entityId, onuId, onuIndex);

        refreshOltPonTrans(onuId);

        refreshOnuQualityInfo(snmpParam, entityId, onuId, onuIndex, onuAttribute.getOnuEorG());

        refreshOnuOnOffRecords(snmpParam, entityId, onuId, onuIndex);

        statisticQualityHistory(entityId, onuId, onuIndex);

        refreshOnuUniInfo(snmpParam, entityId, onuId, onuIndex, onuAttribute.getOnuEorG());

        refreshOnuWanConnection(snmpParam, entityId, onuId, onuIndex);

        refreshOnuWanSsid(snmpParam, entityId, onuIndex);

        refreshOnuLaser(snmpParam, entityId, onuId);
    }

    @Override
    public void refreshOnuQuality(Long entityId, Long onuId, Long onuIndex) {

        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        OltOnuAttribute onuAttribute = onuDao.getOnuEntityById(onuId);

        refreshOnuCatvInfo(snmpParam, entityId, onuId, onuIndex);

        refreshOnuQualityInfo(snmpParam, entityId, onuId, onuIndex, onuAttribute.getOnuEorG());

        statisticQualityHistory(entityId, onuId, onuIndex);

    }

    @Override
    public void refreshOnuUniInfo(Long onuId) {
        Entity onu = entityService.getEntity(onuId);
        OltOnuAttribute onuAttribute = onuDao.getOnuEntityById(onuId);
        Long onuIndex = onuDao.getOnuIndex(onuId);
        Long entityId = onu.getParentId();
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        refreshOnuUniInfo(snmpParam, entityId, onuId, onuIndex, onuAttribute.getOnuEorG());
    }

    @Override
    public List<AlertType> getOnuAlertTypes() {
        List<AlertType> alertTypes = onuAssemblyDao.selectOnuAlertTypes();
        List<AlertType> onuAlertTypes = new ArrayList<AlertType>();
        List<Integer> onuCategoryList = Arrays.asList(AlertType.ALERT_ONU);
        List<Integer> specialCmtsType = Arrays.asList(AlertType.SPECIAL_CATEGORY_CMTS);
        List<Integer> specialOltType = Arrays.asList(AlertType.SPECIAL_CATEGORY_OLT);
        for (AlertType type : alertTypes) {
            Integer typeId = type.getTypeId();
            if (onuCategoryList.contains(type.getCategory()) && !specialCmtsType.contains(typeId)
                    && !specialOltType.contains(typeId)) {
                onuAlertTypes.add(type);
            }
        }
        return onuAlertTypes;
    }

    @Override
    public List<Alert> getOnuAlertList(Map<String, Object> map) {
        return onuAssemblyDao.selectOnuAlertList(map);
    }

    @Override
    public int getOnuAlertListNum(Map<String, Object> map) {
        return onuAssemblyDao.selectOnuAlertListNum(map);
    }

    @Override
    public List<HistoryAlert> getOnuHistoryAlertList(Map<String, Object> map) {
        return onuAssemblyDao.selectOnuHistoryAlertList(map);
    }

    @Override
    public int getOnuHistoryAlertListNum(Map<String, Object> map) {
        return onuAssemblyDao.selectOnuHistoryAlertListNum(map);
    }

    @Override
    public int queryForOnuCount(Map<String, Object> queryMap) {
        return onuAssemblyDao.selectOnuCount(queryMap);
    }

    @Override
    public void refreshOnuOptical(List<String> onuIdList, String jConnectedId, String sessionId) {
        // 从数据库获取ONU信息
        List<OnuLinkCollectInfo> onuLinkList = onuAssemblyDao.queryOnuRelaInfoList(onuIdList);
        // 将ONU按上联OLT进行分组
        Map<Long, List<OnuLinkCollectInfo>> oltOnuLinkMap = new HashMap<Long, List<OnuLinkCollectInfo>>();
        List<OnuLinkCollectInfo> oltOnuLinkList = null;
        Map<Long, List<OnuCatvInfo>> oltOnuCatvMap = new HashMap<Long, List<OnuCatvInfo>>();
        List<OnuCatvInfo> oltOnuCatvList = null;
        Map<Long, List<OltPonOptical>> oltPonOpticalMap = new HashMap<Long, List<OltPonOptical>>();
        List<OltPonOptical> oltPonOpticalList = null;
        for (OnuLinkCollectInfo onuLink : onuLinkList) {
            // EMS-14718
            OnuCatvInfo onuCatvInfo = new OnuCatvInfo();
            onuCatvInfo.setEntityId(onuLink.getEntityId());
            onuCatvInfo.setOnuId(onuLink.getOnuId());
            onuCatvInfo.setOnuIndex(onuLink.getOnuIndex());
            // 增加OltPonTransPower PON发送功率
            OltPonOptical oltPonOptical = onuAssemblyDao.getOltOnuPonOptical(onuLink.getOnuId());

            Long entityId = onuLink.getEntityId();
            oltOnuLinkList = oltOnuLinkMap.get(entityId);
            if (oltOnuLinkList == null) {
                oltOnuLinkList = new ArrayList<OnuLinkCollectInfo>();
                oltOnuLinkMap.put(entityId, oltOnuLinkList);
            }
            oltOnuCatvList = oltOnuCatvMap.get(entityId);
            if (oltOnuCatvList == null) {
                oltOnuCatvList = new ArrayList<OnuCatvInfo>();
                oltOnuCatvMap.put(entityId, oltOnuCatvList);
            }
            oltPonOpticalList = oltPonOpticalMap.get(entityId);
            if (oltPonOpticalList == null) {
                oltPonOpticalList = new ArrayList<OltPonOptical>();
                oltPonOpticalMap.put(entityId, oltPonOpticalList);
            }
            onuLink.setCardIndex(0);
            onuLink.setPortIndex(0);
            oltOnuLinkList.add(onuLink);
            oltOnuCatvList.add(onuCatvInfo);
            oltPonOpticalList.add(oltPonOptical);
        }

        // 分别对每个OLT的ONU光功率信息进行采集
        for (Entry<Long, List<OnuLinkCollectInfo>> onuEntry : oltOnuLinkMap.entrySet()) {
            try {
                SnmpParam snmpParam = entityService.getSnmpParamByEntity(onuEntry.getKey());
                List<OnuLinkCollectInfo> currentLinkList = getOnuFacade(snmpParam.getIpAddress())
                        .getOnuLinkInfoList(snmpParam, onuEntry.getValue());
                // catv光信息
                List<OnuCatvInfo> currentCatvList = null;
                try {
                    currentCatvList = getOnuFacade(snmpParam.getIpAddress()).getOnuCatvInfoList(snmpParam,
                            oltOnuCatvMap.get(onuEntry.getKey()));
                } catch (Exception e) {
                    logger.error("", e);
                }

                List<OltPonOptical> currentOltPonOpticalList = oltPonOpticalMap.get(onuEntry.getKey());
                // 将采集到的光功率信息入库和推送到页面
                for (int i = 0; i < currentLinkList.size(); i++) {
                    OnuLinkCollectInfo currentLink = currentLinkList.get(i);
                    if (currentLink.getOnuPonRevPower() != null
                            && EponConstants.RE_POWER != currentLink.getOnuPonRevPower()) {
                        currentLink.setOnuRevResult((float) currentLink.getOnuPonRevPower() / 100);
                    } else {
                        currentLink.setOnuRevResult(EponConstants.INVALID_VALUE);
                    }
                    if (currentLink.getOnuPonTransPower() != null
                            && EponConstants.TX_POWER != currentLink.getOnuPonTransPower()) {
                        currentLink.setOnuTransResult((float) currentLink.getOnuPonTransPower() / 100);
                    } else {
                        currentLink.setOnuTransResult(EponConstants.INVALID_VALUE);
                    }
                    if (currentLink.getOltPonRevPower() != null
                            && EponConstants.RE_POWER != currentLink.getOltPonRevPower()) {
                        currentLink.setOltRevResult((float) currentLink.getOltPonRevPower() / 100);
                    } else {
                        currentLink.setOltRevResult(EponConstants.INVALID_VALUE);
                    }
                    // 更新数据库
                    onuPerfDao.insertOrUpdateLinkInfo(currentLink);
                    OnuCatvInfo currentCatv = null;
                    if (currentCatvList != null && currentCatvList.size() > 0) {
                        currentCatv = currentCatvList.get(i);
                        currentCatv.setCollectTime(new Timestamp(new Date().getTime()));
                        onuPerfDao.insertOrUpdateCatvInfo(currentCatv);
                    }

                    // 刷新PON发送功率信息
                    OltPonOptical opo = currentOltPonOpticalList.get(i);
                    opo = oltOpticalService.loadOltPonOptical(opo.getEntityId(), opo.getPortIndex());

                    Map<String, Object> currentDataMap = new HashMap<>();
                    currentDataMap.put("catv", currentCatv);
                    currentDataMap.put("link", currentLink);
                    currentDataMap.put("oltPon", opo);
                    // 推送到页面
                    pushOnuOpticalMessage(currentDataMap, jConnectedId, sessionId, "refreshOnuOptical");
                }
            } catch (Exception e) {
                logger.error("", e);
            }
        }
    }

    @Override
    public void queryOnuOpticalHistory(List<String> onuIdList, String jConnectedId, String sessionId) {
        // 从数据库获取ONU信息
        List<OnuLinkCollectInfo> onuLinkList = onuAssemblyDao.queryOnuRelaInfoList(onuIdList);
        // 分别对每个设备24h最低ONU和CATV收光功率进行汇总查询
        for (OnuLinkCollectInfo onuLinkCollectInfo : onuLinkList) {
            try {
                // 汇总 24h最低收光功率
                PerfOnuQualityHistory minONUQualityHistory = onuPerfService.queryMinPonRevPower(onuLinkCollectInfo);
                // 汇总 24h CATV最低收功率
                PerfOnuQualityHistory minCATVQualityHistory = onuPerfService.queryMinCATVRevPower(onuLinkCollectInfo);

                // 组合成24h内 ONU 和Catv最低收光功率
                PerfOnuQualityHistory minOnuCatvQualityHistory = new PerfOnuQualityHistory();
                minOnuCatvQualityHistory.setEntityId(onuLinkCollectInfo.getEntityId());
                minOnuCatvQualityHistory.setOnuId(onuLinkCollectInfo.getOnuId());
                minOnuCatvQualityHistory.setOnuIndex(onuLinkCollectInfo.getOnuIndex());
                if (minONUQualityHistory != null) {
                    minOnuCatvQualityHistory.setMinOnuPonRevPower(minONUQualityHistory.getMinOnuPonRevPower());
                    minOnuCatvQualityHistory.setMinPowerTime(minONUQualityHistory.getMinPowerTime());
                    minOnuCatvQualityHistory
                            .setMinPowerTimeStr(DateUtils.parseToString(minONUQualityHistory.getMinPowerTime()));
                }
                if (minCATVQualityHistory != null) {
                    minOnuCatvQualityHistory.setMinCatvRevPower(minCATVQualityHistory.getMinCatvRevPower());
                    minOnuCatvQualityHistory.setMinCatvTime(minCATVQualityHistory.getMinCatvTime());
                    minOnuCatvQualityHistory.setMinCatvTimeStr(minCATVQualityHistory.getMinCatvTime() == null ? null
                            : DateUtils.parseToString(minCATVQualityHistory.getMinCatvTime()));
                }
                // 更新到数据库
                onuPerfService.insertOrUpdateMinReceivedPower(minOnuCatvQualityHistory);
                // 推送到页面
                pushOnuOpticalHistoryMessage(minOnuCatvQualityHistory, jConnectedId, sessionId,
                        "queryOnuOpticalHistory");
            } catch (Exception e) {
                logger.error("query min onu and catv optical power fail!", e);
            }
        }
    }

    public void saveOnuServerLevel(Long onuId, Integer onuLevel) {
        onuAssemblyDao.saveOnuServerLevel(onuId, onuLevel);
    }

    public Integer getOnuServerLevel(Long onuId) {
        return onuAssemblyDao.getOnuServerLevel(onuId);
    }

    private UniFacade getUniFacade(String ip) {
        return facadeFactory.getFacade(ip, UniFacade.class);
    }

    private OnuFacade getOnuFacade(String ip) {
        return facadeFactory.getFacade(ip, OnuFacade.class);
    }

    private GponOnuFacade getGponOnuFacade(String ip) {
        return facadeFactory.getFacade(ip, GponOnuFacade.class);
    }

    private void pushOnuOpticalMessage(Map<String, Object> onuOptical, String jConnectedId, String seesionId,
            String msgType) {
        if (jConnectedId != null) {
            Message message = new Message(msgType);
            message.addSessionId(seesionId);
            message.setJconnectID(jConnectedId);
            message.setData(onuOptical);
            messagePusher.sendMessage(message);
        }
    }

    private void pushOnuOpticalHistoryMessage(PerfOnuQualityHistory perfOnuQualityHistory, String jConnectedId,
            String seesionId, String msgType) {
        if (jConnectedId != null) {
            Message message = new Message(msgType);
            message.addSessionId(seesionId);
            message.setJconnectID(jConnectedId);
            message.setData(perfOnuQualityHistory);
            messagePusher.sendMessage(message);
        }
    }

    /**
     * 刷新onu上下线记录
     * 
     * @param snmpParam
     * @param entityId
     * @param onuId
     * @param onuIndex
     */
    private void refreshOnuOnOffRecords(SnmpParam snmpParam, Long entityId, Long onuId, Long onuIndex) {

        try {
            OnuEventLogEntry onuEventLogEntry = new OnuEventLogEntry();
            onuEventLogEntry.setEntityId(entityId);
            onuEventLogEntry.setOnuIndex(onuIndex);
            onuEventLogEntry.setOnuId(onuId);
            onuOnOffRecordCollectService.refreshOnOffRecords(snmpParam, onuEventLogEntry);
        } catch (Exception e) {
            logger.error("refresh onu onoff records failure", e);
        }

    }

    /**
     * 刷新catv信息，是为了刷新catv最低24h收光功率做准备
     * 
     * @param snmpParam
     * @param entityId
     * @param onuId
     * @param onuIndex
     */
    private void refreshOnuCatvInfo(SnmpParam snmpParam, Long entityId, Long onuId, Long onuIndex) {

        try {
            OnuCatvInfo onuCatvInfo = new OnuCatvInfo();
            onuCatvInfo.setOnuCatvOrInfoDeviceIndex(EponIndex.getOnuMibIndexByIndex(onuIndex));
            onuCatvInfo = getOnuFacade(snmpParam.getIpAddress()).refreshOnuCatv(snmpParam, onuCatvInfo);
            onuCatvInfo.setEntityId(entityId);
            onuCatvInfo.setOnuId(onuId);
            onuCatvInfo.setOnuIndex(onuIndex);
            onuCatvInfo.setCollectTime(new Timestamp(new Date().getTime()));
            onuPerfDao.insertOrUpdateCatvInfo(onuCatvInfo);
        } catch (Exception e) {
            logger.error("refresh catv info failure", e);
        }

    }

    /**
     * 刷新onu Quality 信息
     * 
     * @param snmpParam
     * @param entityId
     * @param onuId
     * @param onuIndex
     */
    private void refreshOnuQualityInfo(SnmpParam snmpParam, Long entityId, Long onuId, Long onuIndex, String onuEorG) {

        if (EponConstants.EPON_ONU.equals(onuEorG)) {
            OnuQualityInfo onuQualityInfo = new OnuQualityInfo();
            onuQualityInfo.setOnuDeviceIndex(EponIndex.getOnuMibIndexByIndex(onuIndex));
            try {
                onuQualityInfo = getOnuFacade(snmpParam.getIpAddress()).refreshOnuQuality(snmpParam, onuQualityInfo);
            } catch (Exception e) {
                logger.error("refresh epon ouu quality info failure", e);
            }
            onuQualityInfo.setEntityId(entityId);
            onuQualityInfo.setOnuIndex(onuIndex);
            onuQualityInfo.setOnuId(onuId);
            onuAssemblyDao.updateOnuQuality(onuQualityInfo);
        } else if (GponConstant.GPON_ONU.equals(onuEorG)) {
            GponOnuQualityInfo gponOnuQualityInfo = new GponOnuQualityInfo();
            gponOnuQualityInfo.setOnuDeviceIndex(EponIndex.getOnuMibIndexByIndex(onuIndex));
            try {
                gponOnuQualityInfo = getGponOnuFacade(snmpParam.getIpAddress()).refreshGponOnuQuality(snmpParam,
                        gponOnuQualityInfo);
            } catch (Exception e) {
                logger.info("refresh gpon ouu quality info failure", e);
            }
            gponOnuQualityInfo.setEntityId(entityId);
            gponOnuQualityInfo.setOnuIndex(onuIndex);
            gponOnuQualityInfo.setOnuId(onuId);
            onuAssemblyDao.updateGponOnuQuality(gponOnuQualityInfo);
        }

    }

    /**
     * 统计onu 光功率记录，包括24h最低光功率和catv24h最低最低收光功率
     * 
     * @param snmpParam
     * @param entityId
     * @param onuId
     * @param onuIndex
     */
    private void statisticQualityHistory(Long entityId, Long onuId, Long onuIndex) {

        OnuLinkCollectInfo onuLinkCollectInfo = new OnuLinkCollectInfo();
        onuLinkCollectInfo.setOnuId(onuId);
        onuLinkCollectInfo.setEntityId(entityId);
        onuLinkCollectInfo.setOnuIndex(onuIndex);

        // 汇总 24h最低收光功率
        PerfOnuQualityHistory minONUQualityHistory = onuPerfService.queryMinPonRevPower(onuLinkCollectInfo);
        // 汇总 24h CATV最低收功率
        PerfOnuQualityHistory minCATVQualityHistory = onuPerfService.queryMinCATVRevPower(onuLinkCollectInfo);

        try {
            // 组合成24h内 ONU 和Catv最低收光功率
            PerfOnuQualityHistory minOnuCatvQualityHistory = new PerfOnuQualityHistory();
            minOnuCatvQualityHistory.setEntityId(entityId);
            minOnuCatvQualityHistory.setOnuId(onuId);
            minOnuCatvQualityHistory.setOnuIndex(onuIndex);
            if (minONUQualityHistory != null) {
                minOnuCatvQualityHistory.setMinOnuPonRevPower(minONUQualityHistory.getMinOnuPonRevPower());
                minOnuCatvQualityHistory.setMinPowerTime(minONUQualityHistory.getMinPowerTime());
                minOnuCatvQualityHistory
                        .setMinPowerTimeStr(DateUtils.parseToString(minONUQualityHistory.getMinPowerTime()));
            }
            if (minCATVQualityHistory != null) {
                minOnuCatvQualityHistory.setMinCatvRevPower(minCATVQualityHistory.getMinCatvRevPower());
                minOnuCatvQualityHistory.setMinCatvTime(minCATVQualityHistory.getMinCatvTime());
                minOnuCatvQualityHistory.setMinCatvTimeStr(minCATVQualityHistory.getMinCatvTime() == null ? null
                        : DateUtils.parseToString(minCATVQualityHistory.getMinCatvTime()));
            }
            // 更新到数据库
            onuPerfService.insertOrUpdateMinReceivedPower(minOnuCatvQualityHistory);
        } catch (Exception e) {
            logger.error("statistic quality history failure", e);
        }

    }

    /**
     * 刷新onu uni info
     * 
     * @param snmpParam
     * @param entityId
     * @param onuId
     * @param onuIndex
     * @param onuEorG
     */
    private void refreshOnuUniInfo(SnmpParam snmpParam, Long entityId, Long onuId, Long onuIndex, String onuEorG) {

        List<UniPort> uniPorts = onuAssemblyDao.selectUniList(onuId);

        if (EponConstants.EPON_ONU.equals(onuEorG)) {
            List<OltUniExtAttribute> uniExtList = new ArrayList<OltUniExtAttribute>();
            List<OltUniAttribute> uniList = new ArrayList<OltUniAttribute>();
            List<UniVlanBindTable> vlanList = new ArrayList<UniVlanBindTable>();
            uniVlanProfileService.refreshProfileAndRule(entityId);
            for (UniPort uniPort : uniPorts) {
                try {
                    OltUniExtAttribute oltUniExtAttribute = new OltUniExtAttribute();
                    oltUniExtAttribute.setUniIndex(uniPort.getUniIndex());
                    oltUniExtAttribute = getUniFacade(snmpParam.getIpAddress()).refreshOnuUniExtInfo(snmpParam,
                            oltUniExtAttribute);
                    uniExtList.add(oltUniExtAttribute);
                    OltUniAttribute oltUniAttribute = new OltUniAttribute();
                    oltUniAttribute.setUniIndex(uniPort.getUniIndex());
                    oltUniAttribute.setUniId(uniPort.getUniId());
                    oltUniAttribute = getUniFacade(snmpParam.getIpAddress()).refreshOnuUniAttribute(snmpParam,
                            oltUniAttribute);
                    uniList.add(oltUniAttribute);
                    oltUniAttribute.setFlowCtrl(oltUniExtAttribute.getFlowCtrl());
                    UniVlanBindTable vlanTable = new UniVlanBindTable();
                    vlanTable.setUniIndex(uniPort.getUniIndex());
                    vlanTable = getUniFacade(snmpParam.getIpAddress()).refreshUniVlanBindTable(snmpParam, vlanTable);
                    vlanList.add(vlanTable);
                } catch (Exception e) {
                    logger.error("refresh epon onu uni info failure", e);
                }
            }
            onuAssemblyDao.batchUpdateUniExtAttribute(entityId, onuId, uniExtList);
            onuAssemblyDao.batchUpdateUniAttribute(entityId, uniList);
            onuAssemblyDao.batchUpdateUniVlanTable(entityId, vlanList);
            uniVlanProfileService.refreshUniVlanData(entityId);
        } else if (GponConstant.GPON_ONU.equals(onuEorG)) {
            try {
                onuRefreshService.refreshOnu(entityId, onuIndex, onuId);
            } catch (Exception e) {
                logger.error("refresh epon onu uni info failure", e);
            }
        }

    }

    /**
     * 刷新ONU wan连接信息
     * 
     * @param entityId
     * @param onuId
     */
    private void refreshOnuWanConnection(SnmpParam snmpParam, Long entityId, Long onuId, Long onuIndex) {
        try {
            onuWanService.refreshWanConnection(snmpParam, entityId, onuId, onuIndex);
        } catch (Exception e) {
            logger.error("refresh onu wan connection failure", e);
        }
    }

    /**
     * 刷新ONU ssid
     * 
     * @param snmpParam
     * @param entityId
     * @param onuIndex
     */
    private void refreshOnuWanSsid(SnmpParam snmpParam, Long entityId, Long onuIndex) {
        try {
            onuWanService.refreshOnuWanSsid(snmpParam, entityId, onuIndex);
        } catch (Exception e) {
            logger.error("refresh onu wan ssid failure", e);
        }
    }

    /**
     * 刷新ONU激光器
     * 
     * @param snmpParam
     * @param entityId
     * @param onuIndex
     */
    private void refreshOnuLaser(SnmpParam snmpParam, Long entityId, Long onuId) {
        try {
            rogueOnuService.refreshOnuLaserSwitch(entityId, onuId);
        } catch (Exception e) {
            logger.error("refresh onu laser failure", e);
        }
    }
    
    
    /**
     * 刷新链路视图中的PON发送功率
     * 
     * @param onuId
     */
    private void refreshOltPonTrans(Long onuId) {
        try {
            OltPonOptical oltPonOptical = onuAssemblyDao.getOltOnuPonOptical(onuId);
            oltOpticalService.loadOltPonOptical(oltPonOptical.getEntityId(), oltPonOptical.getPortIndex());
        } catch (Exception e) {
            logger.error("refresh olt pon transPorwer failure", e);
        }
    }

    @Override
    public void saveOnuTagRelation(Long onuId, Integer tagId) {
        onuAssemblyDao.insertOrUpdateOnuTagRelation(onuId, tagId);
    }

}
