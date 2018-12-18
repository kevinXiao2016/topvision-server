package com.topvision.ems.epon.onu.dao;

import java.util.List;
import java.util.Map;

import com.topvision.ems.epon.onu.domain.OltOnuCapability;
import com.topvision.ems.epon.onu.domain.OltOnuCatv;
import com.topvision.ems.epon.onu.domain.OltOnuRstp;
import com.topvision.ems.epon.onu.domain.OltOnuVoip;
import com.topvision.ems.epon.onu.domain.OltTopOnuCapability;
import com.topvision.ems.epon.onu.domain.OltUniAttribute;
import com.topvision.ems.epon.onu.domain.OltUniExtAttribute;
import com.topvision.ems.epon.onu.domain.OltUniPortRateLimit;
import com.topvision.ems.epon.onu.domain.OnuInfo;
import com.topvision.ems.epon.onu.domain.OnuQualityInfo;
import com.topvision.ems.epon.onu.domain.UniPort;
import com.topvision.ems.epon.optical.domain.OltPonOptical;
import com.topvision.ems.epon.performance.domain.OnuLinkCollectInfo;
import com.topvision.ems.epon.univlanprofile.domain.UniVlanBindTable;
import com.topvision.ems.fault.domain.Alert;
import com.topvision.ems.fault.domain.AlertType;
import com.topvision.ems.fault.domain.HistoryAlert;
import com.topvision.ems.gpon.onu.domain.GponOnuQualityInfo;

/**
 * 将原OnuDeviceDao中的方法移动到这里，原OnuServiceDao用来做ONU设备列表dao
 * 
 * @author w1992wishes
 * @created @2017年12月22日-上午9:38:02
 *
 */
public interface OnuAssemblyDao {
    /**
     * 根据ONUID获取上联PON口的光信息
     * 
     * @param onuId
     * @return
     */
    OltPonOptical getOltOnuPonOptical(Long onuId);

    /**
     * 加载UNI端口列表
     * 
     * @param onuId
     * @return
     */
    List<UniPort> selectUniList(Long onuId);

    /**
     * 查询ONU列表
     * 
     * @param queryMap
     * @return
     */
    List<OnuInfo> selectOnuList(Map<String, Object> queryMap);

    /**
     * 更新ONU信号质量
     * 
     * @param onuQualityInfo
     */
    void updateOnuQuality(OnuQualityInfo onuQualityInfo);

    /**
     * 更新GPON ONU信号质量
     * 
     * @param onuQualityInfo
     */
    void updateGponOnuQuality(GponOnuQualityInfo gponOnuQualityInfo);

    /**
     * 获取所有ONU告警
     * 
     * @return
     */
    List<AlertType> selectOnuAlertTypes();

    /**
     * 查询ONU告警列表
     * 
     * @param map
     * @return
     */
    List<Alert> selectOnuAlertList(Map<String, Object> map);

    /**
     * 查询ONU告警列表总数
     * 
     * @param map
     * @return
     */
    int selectOnuAlertListNum(Map<String, Object> map);

    /**
     * 查询ONU历史告警列表
     * 
     * @param map
     * @return
     */
    List<HistoryAlert> selectOnuHistoryAlertList(Map<String, Object> map);

    /**
     * 查询ONU历史告警列表总数
     * 
     * @param map
     * @return
     */
    int selectOnuHistoryAlertListNum(Map<String, Object> map);

    /**
     * 查询ONU总数
     * 
     * @param queryMap
     * @return
     */
    int selectOnuCount(Map<String, Object> queryMap);

    /**
     * 更新ONU MAC
     * 
     * @param onuId
     * @param onuMac
     */
    void modifyOnuMac(Long onuId, String onuMac);

    /**
     * 更新ONU TOP能力信息
     * 
     * @param capability
     */
    void updateOnuTopCapacity(OltTopOnuCapability capability);

    /**
     * 更新ONU能力信息
     * 
     * @param capability
     */
    void updateOnuCapacity(OltOnuCapability capability);

    /**
     * 更新ONU RSTP信息
     * 
     * @param oltOnuRstp
     */
    void updateOltOnuRstp(OltOnuRstp oltOnuRstp);

    /**
     * 更新UNI属性
     * 
     * @param entityId
     * @param oltUniAttributeList
     */
    void batchUpdateUniAttribute(Long entityId, List<OltUniAttribute> oltUniAttributeList);

    /**
     * 更新UNI端口限速
     * 
     * @param onuId
     * @param oltUniPortRateLimitList
     */
    void batchUpdateUniPortRateLimit(Long onuId, List<OltUniPortRateLimit> oltUniPortRateLimitList);

    /**
     * 更新UNI EXT属性表
     * 
     * @param entityId
     * @param onuId
     * @param oltUniExtAttributeList
     */
    void batchUpdateUniExtAttribute(Long entityId, Long onuId, List<OltUniExtAttribute> oltUniExtAttributeList);

    /**
     * 更新ONU CATV
     * 
     * @param oltOnuCatv
     */
    void updateOltOnuCatv(OltOnuCatv oltOnuCatv);

    /**
     * 更新ONU VOIP
     * 
     * @param oltOnuVoip
     */
    void updateOltOnuVoip(OltOnuVoip oltOnuVoip);

    /**
     * 更新UNI VLAN
     * 
     * @param parentId
     * @param vlanList
     */
    void batchUpdateUniVlanTable(Long parentId, List<UniVlanBindTable> vlanList);

    /**
     * 根据ONUId列表获取ONU关系数据 包括entityId, onuId, onuIndex
     * 
     * @param onuIdList
     * @return
     */
    List<OnuLinkCollectInfo> queryOnuRelaInfoList(List<String> onuIdList);

    void saveOnuServerLevel(Long onuId, Integer onuLevel);

    Integer getOnuServerLevel(Long onuId);

    /**
     * 插入onu标签关系
     * 
     * @param onuId
     * @param tagId
     */
    void insertOrUpdateOnuTagRelation(Long onuId, Integer tagId);
}
