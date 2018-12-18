package com.topvision.ems.mobile.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.baidu.domain.BaiduEntity;
import com.topvision.ems.baidu.service.BaiduMapService;
import com.topvision.ems.epon.onu.domain.OltOnuAttribute;
import com.topvision.ems.epon.onu.service.OnuService;
import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.google.domain.GoogleEntity;
import com.topvision.ems.google.service.GoogleEntityService;
import com.topvision.ems.gpon.onu.domain.GponOnuCapability;
import com.topvision.ems.gpon.onu.service.GponOnuService;
import com.topvision.ems.mobile.dao.MUIOnuDao;
import com.topvision.ems.mobile.domain.MUIOnu;
import com.topvision.ems.mobile.domain.OnuAroundInfo;
import com.topvision.ems.mobile.domain.OnuHealthyConstants;
import com.topvision.ems.mobile.domain.OnuHealthyInfo;
import com.topvision.ems.mobile.domain.OnuHealthyPerfRule;
import com.topvision.ems.mobile.domain.OnuHealthyThreshold;
import com.topvision.ems.mobile.domain.OnuOpenReport;
import com.topvision.ems.mobile.domain.OnuPreconfigInfo;
import com.topvision.ems.mobile.domain.PageView;
import com.topvision.ems.mobile.domain.VisitView;
import com.topvision.ems.mobile.service.MUIOnuService;
import com.topvision.ems.network.service.EntityService;
import com.topvision.framework.common.MacUtils;
import com.topvision.framework.service.BaseService;

/**
 * @author xiaoyue
 * @created @2017年5月6日-上午9:39:00
 *
 */
@Service("mUIOnuService")
public class MUIOnuServiceImpl extends BaseService implements MUIOnuService {

    @Autowired
    private MUIOnuDao muiOnuDao;
    @Autowired
    private OnuService onuService;
    @Autowired
    private GponOnuService gponOnuService;
    @Autowired
    private GoogleEntityService googleEntityService;
    @Autowired
    private BaiduMapService baiduMapService;
    @Autowired
    private EntityService entityService;

    @Override
    public List<OnuPreconfigInfo> getOnuPreconfigInfo() {
        return muiOnuDao.getOnuPreconfigInfo();
    }

    @Override
    public List<OnuOpenReport> getOnuOpenReport() {
        return muiOnuDao.getOnuOpenReport();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.mobile.service.MUIOnuService#synchronizingInformation(java.util.List,
     * java.util.List)
     */
    @Override
    public ArrayList<Object> synchronizingInformation(List<OnuPreconfigInfo> onuPreconfigInfos,
            List<OnuOpenReport> openreportList) {
        List<OnuPreconfigInfo> preconfigInfosFromDB = getOnuPreconfigInfo();
        List<OnuOpenReport> onuOpenReportsFromDB = getOnuOpenReport();
        ArrayList<Object> list = new ArrayList<>();

        if (onuPreconfigInfos == null || onuPreconfigInfos.size() == 0) {
            list.add(0, preconfigInfosFromDB);
        } else {
            // 合并去重
            preconfigInfosFromDB.removeAll(onuPreconfigInfos);
            preconfigInfosFromDB.addAll(onuPreconfigInfos);
            removeDuplicate(preconfigInfosFromDB);
            // 保存结果到DB，先删除，再新增
            addPreconfigAfterClear(preconfigInfosFromDB);
            list.add(0, preconfigInfosFromDB);
        }

        if (openreportList == null || openreportList.size() == 0) {
            list.add(1, null);
        } else {
            // 合并去重
            onuOpenReportsFromDB.removeAll(openreportList);
            onuOpenReportsFromDB.addAll(openreportList);
            removeDuplicate2(onuOpenReportsFromDB);
            // 保存结果到DB，先删除，再新增
            addOpenReportAfterClear(onuOpenReportsFromDB);
            list.add(1, onuOpenReportsFromDB);
        }

        return list;
    }

    private void addOpenReportAfterClear(List<OnuOpenReport> onuOpenReportsFromDB) {
        muiOnuDao.clearOpenreport();
        muiOnuDao.saveOpenreportList(onuOpenReportsFromDB);
    }

    private void addPreconfigAfterClear(List<OnuPreconfigInfo> preconfigInfosFromDB) {
        muiOnuDao.clearPreconfig();
        muiOnuDao.savePreconfigList(preconfigInfosFromDB);
    }

    private void removeDuplicate(List<OnuPreconfigInfo> preconfigInfosFromDB) {
        Map<String, OnuPreconfigInfo> map = new HashMap<>();
        Set<OnuPreconfigInfo> tempSet = new HashSet<>();
        long modifytimeInMap = 0L;
        long modifytimeInNow = 0L;
        for (OnuPreconfigInfo preconfigInfo : preconfigInfosFromDB) {
            if (map.get(preconfigInfo.getUniqueId()) == null) {
                map.put(preconfigInfo.getUniqueId(), preconfigInfo);
            } else {
                modifytimeInMap = Long.valueOf(map.get(preconfigInfo.getUniqueId()).getModifyTime());
                modifytimeInNow = Long.valueOf(preconfigInfo.getModifyTime());
                if (modifytimeInNow > modifytimeInMap) {
                    // 当前onu修改时间比map中的晚，则去掉map中旧的onu信息
                    tempSet.add(map.get(preconfigInfo.getUniqueId()));
                    map.put(preconfigInfo.getUniqueId(), preconfigInfo);
                } else if (modifytimeInNow == modifytimeInMap) {
                    long timeInMap = Long.valueOf(map.get(preconfigInfo.getUniqueId()).getTime());
                    long timeInNow = Long.valueOf(preconfigInfo.getTime());
                    // 修改时间相同，比对访问时间，访问时间近的保存
                    if (timeInNow > timeInMap) {
                        tempSet.add(map.get(preconfigInfo.getUniqueId()));
                        map.put(preconfigInfo.getUniqueId(), preconfigInfo);
                    }
                } else {
                    tempSet.add(preconfigInfo);
                }
            }
        }
        // 去掉时间较早的信息
        preconfigInfosFromDB.removeAll(tempSet);
    }

    private void removeDuplicate2(List<OnuOpenReport> onuOpenReportsFromDB) {

        Map<String, OnuOpenReport> map = new HashMap<>();
        Set<OnuOpenReport> tempSet = new HashSet<>();
        Long timeInMap = null;
        Long timeInNow = null;
        for (OnuOpenReport onuOpenReport : onuOpenReportsFromDB) {
            if (map.get(onuOpenReport.getUniqueId()) == null) {
                map.put(onuOpenReport.getUniqueId(), onuOpenReport);
            } else {
                timeInMap = Long.valueOf(map.get(onuOpenReport.getUniqueId()).getTime());
                timeInNow = Long.valueOf(onuOpenReport.getTime());
                if (timeInNow > timeInMap) {
                    // 当前onu修改时间比map中的晚，则去掉map中旧的onu信息
                    tempSet.add(map.get(onuOpenReport.getUniqueId()));
                    map.put(onuOpenReport.getUniqueId(), onuOpenReport);
                } else {
                    tempSet.add(onuOpenReport);
                }
            }
        }
        // 去掉时间较早的信息
        onuOpenReportsFromDB.removeAll(tempSet);
    }

    @Override
    public List<MUIOnu> queryForOnuList(Map<String, Object> queryMap) {
        return muiOnuDao.selectOnuList(queryMap);
    }

    @Override
    public OltOnuAttribute queryOnlineOnuByUniqueId(String uniqueId) {
        return muiOnuDao.selectOnlineOnuAttributeByUniqueId(uniqueId);
    }
    
    @Override
    public List<OltOnuAttribute> queryAllOnuByUniqueId(String uniqueId) {
        return muiOnuDao.selectAllOnuAttributeByUniqueId(uniqueId);
    }

    @Override
    public Integer getOnuDownloadSpeed(String uniqueId) {
        OltOnuAttribute oltOnuAttribute = queryOnlineOnuByUniqueId(uniqueId);
        return onuService.getOnuDownRate(oltOnuAttribute.getEntityId(), oltOnuAttribute.getOnuId(),
                oltOnuAttribute.getOnuEorG());

    }

    @Override
    public void saveUserExper(List<VisitView> visitViewList, List<PageView> pageViewList) {
        muiOnuDao.saveVisitView(visitViewList);
        muiOnuDao.savePageView(pageViewList);
    }

    @Override
    public void savePreconfigInfo(List<OnuPreconfigInfo> onuPreconfigInfos) {
        muiOnuDao.batchInsertOrUpdatePreconfigInfo(onuPreconfigInfos);
        //保存经纬度到GoogleEntity
        List<GoogleEntity> googleEntities = new ArrayList<>();
        List<BaiduEntity> baiduEntities = new ArrayList<>();
        GoogleEntity googleEntity = new GoogleEntity();
        BaiduEntity baiduEntity = new BaiduEntity();
        for (OnuPreconfigInfo onuPreconfig : onuPreconfigInfos) {
            //根据mac\sn找到系统中匹配的onu，暂时只匹配上线的onu
            OltOnuAttribute onuAttribute = queryOnlineOnuByUniqueId(formatUniqueId(onuPreconfig.getUniqueId()));            
            if (onuAttribute != null) {
                Entity entity=entityService.getEntity(onuAttribute.getOnuId());
                //插入googleEntity
                googleEntity.setEntityId(onuAttribute.getOnuId());
                googleEntity.setLocation(onuPreconfig.getAddress());
                if (onuPreconfig.getLatitude() != null) {
                    googleEntity.setLatitude(onuPreconfig.getLatitude());
                }
                if (onuPreconfig.getLongitude() != null) {
                    googleEntity.setLongitude(onuPreconfig.getLongitude());
                }
                googleEntities.add(googleEntity);
                
                //插入baiduEntity
                baiduEntity.setEntityId(onuAttribute.getOnuId());
                baiduEntity.setLocation(onuPreconfig.getAddress());
                if (onuPreconfig.getLatitude() != null) {
                    baiduEntity.setLatitude(onuPreconfig.getLatitude());
                }
                if (onuPreconfig.getLongitude() != null) {
                    baiduEntity.setLongitude(onuPreconfig.getLongitude());
                }
                baiduEntity.setTypeId(entity.getTypeId());
                baiduEntities.add(baiduEntity);                
            }
        }
        googleEntityService.batchInsertOrUpdateGoogleEntity(googleEntities);
        baiduMapService.batchInsertOrUpdateBaiduMap(baiduEntities);
    }

    @Override
    public void saveOpenreport(List<OnuOpenReport> onuOpenreportList) {
        muiOnuDao.batchInsertOrUpdateOpenreport(onuOpenreportList);
    }

    @Override
    public List<OnuAroundInfo> getOnuAroundInfo() {
        List<OnuAroundInfo> oai = muiOnuDao.selectOnuAroundInfo();
        Map<String, String> oht = this.getOnuHeathyThreshold();
        for (int i = 0; i < oai.size(); i++) {
            checkValueIsInThreshold(oai.get(i), oht);
        }
        return oai;
    }

    public Map<String, String> getOnuHeathyThreshold() {
        List<OnuHealthyThreshold> onuht = muiOnuDao.selectOnuHeathyThreshold();
        Map<String, String> onuHeathyMap = new HashMap<String, String>();
        for (int i = 0; i < onuht.size(); i++) {
            onuHeathyMap.put(onuht.get(i).getTargetId(), onuht.get(i).getThresholds());
        }
        return onuHeathyMap;
    }

    protected void checkValueIsInThreshold(OnuAroundInfo onuAroundInfo, Map<String, String> oht) {
        OltOnuAttribute onuAttribute = onuService.getOnuAttribute(onuAroundInfo.getOnuId());
        GponOnuCapability goc = gponOnuService.queryForGponOnuCapability(onuAroundInfo.getOnuId());
        Integer catvCapability = onuAttribute.getCatvCapability();
        Integer gponCatvCap = null;
        if (goc != null) {
            gponCatvCap = goc.getOnuTotalCatvNum();
        }
        List<OnuHealthyInfo> flags = new ArrayList<OnuHealthyInfo>();
        if (onuAttribute.getOnuEorG().equals("E")) {
            flags.add(parseAndCompareThreshold(1, onuAroundInfo.getOnuPonRevPowerForunit(),
                    oht.get(OnuHealthyConstants.ONUPRP)));
            flags.add(parseAndCompareThreshold(2, onuAroundInfo.getOnuPonTransPowerForunit(),
                    oht.get(OnuHealthyConstants.ONUPTP)));
            flags.add(parseAndCompareThreshold(3, onuAroundInfo.getOltPonRevPowerForunit(),
                    oht.get(OnuHealthyConstants.OLTPRP)));
        } else {
            flags.add(parseAndCompareThreshold(1, onuAroundInfo.getOnuPonRevPowerForunit(),
                    oht.get(OnuHealthyConstants.ONUGRP)));
            flags.add(parseAndCompareThreshold(2, onuAroundInfo.getOnuPonTransPowerForunit(),
                    oht.get(OnuHealthyConstants.ONUGTP)));
            flags.add(parseAndCompareThreshold(3, onuAroundInfo.getOltPonRevPowerForunit(),
                    oht.get(OnuHealthyConstants.OLTGPLLIDRP)));
        }
        if ((catvCapability != null && catvCapability == 1) || (gponCatvCap != null && gponCatvCap > 0)) {
            flags.add(parseAndCompareThreshold(4, onuAroundInfo.getCatvRxPowerForunit(),
                    oht.get(OnuHealthyConstants.ONUCRP)));
            flags.add(parseAndCompareThreshold(5, onuAroundInfo.getCatvRfOutVoltageForunit(),
                    oht.get(OnuHealthyConstants.ONUPR)));
            flags.add(parseAndCompareThreshold(6, onuAroundInfo.getCatvVoltageForunit(),
                    oht.get(OnuHealthyConstants.ONUCV)));
            flags.add(parseAndCompareThreshold(7, onuAroundInfo.getCatvTemperatureForunit(),
                    oht.get(OnuHealthyConstants.ONUCT)));
        }
        Collections.sort(flags);
        List<String>contentList=new ArrayList<String>();
        for(OnuHealthyInfo ohi:flags){
            if(ohi.getFlag()!=0){
                contentList.add(ohi.getContent());
            }
        }
        onuAroundInfo.setOnuHealthyFlag(flags.get(0).getFlag());
        onuAroundInfo.setHealthyContent(contentList);
//        onuAroundInfo.setHealthyTarget(flags.get(0).getHealthyTarget());
    }

    protected OnuHealthyInfo parseAndCompareThreshold(Integer index, String valueStr, String thresholdStr) {
        String[] parseStr = thresholdStr.split("#");
        List<OnuHealthyPerfRule> healthyRules = new ArrayList<OnuHealthyPerfRule>();
        OnuHealthyInfo onuHealthyInfo = new OnuHealthyInfo();
        if(valueStr=="--"){
            onuHealthyInfo.setFlag(0);
            onuHealthyInfo.setContent("--");
            onuHealthyInfo.setHealthyTarget(0);
            return onuHealthyInfo;
        }
        Float value=Float.parseFloat(valueStr);
        for (int i = 0; i < parseStr.length; i++) {
            OnuHealthyPerfRule onuHealthyPerfRule = new OnuHealthyPerfRule();
            onuHealthyPerfRule.setCompareWay(Integer.parseInt(parseStr[i].split("_")[0]));
            onuHealthyPerfRule.setValue(Float.parseFloat(parseStr[i].split("_")[1]));
            onuHealthyPerfRule.setLevel(Integer.parseInt(parseStr[i].split("_")[2]));
            onuHealthyPerfRule.setPriority(Integer.parseInt(parseStr[i].split("_")[3]));
            healthyRules.add(onuHealthyPerfRule);
        }
        Collections.sort(healthyRules);
        for (OnuHealthyPerfRule ohpr : healthyRules) {
            if (value != null && comparePerf(ohpr.getCompareWay(), ohpr.getValue(), value)) {
                onuHealthyInfo.setFlag(ohpr.getLevel());
                onuHealthyInfo.setContent(index+"#"+value.toString());
                onuHealthyInfo.setHealthyTarget(index);
                break;
            } else {
                onuHealthyInfo.setFlag(0);
                onuHealthyInfo.setContent("--");
                onuHealthyInfo.setHealthyTarget(0);
                continue;
            }
        }
        return onuHealthyInfo;
    }

    protected Boolean comparePerf(Integer compareWay, Float setPerf, Float perf) {
        switch (compareWay) {
        case 1: // >
            return perf > setPerf;
        case 2: // >=
            return perf >= setPerf;
        case 3: // =
            return perf == setPerf;
        case 4: // <=
            return perf <= setPerf;
        case 5: // <
            return perf < setPerf;
        default:
            return false;
        }
    }
    
    private String formatUniqueId(String uniqueId) {
        if (!MacUtils.isMac(uniqueId)) {
            uniqueId = uniqueId.substring(0, 2) + ":" + uniqueId.substring(2, 4) + ":" + uniqueId.substring(4, 6) + ":"
                    + uniqueId.substring(6, 8) + ":" + uniqueId.substring(8, 10) + ":" + uniqueId.substring(10, 12)
                    + ":" + uniqueId.substring(12, 14) + ":" + uniqueId.substring(14, 16);
        }
        return uniqueId;
    }
}
