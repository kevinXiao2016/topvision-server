package com.topvision.ems.mobile.dao;

import java.util.List;
import java.util.Map;

import com.topvision.ems.epon.onu.domain.OltOnuAttribute;
import com.topvision.ems.mobile.domain.MUIOnu;
import com.topvision.ems.mobile.domain.OnuAroundInfo;
import com.topvision.ems.mobile.domain.OnuHealthyThreshold;
import com.topvision.ems.mobile.domain.OnuOpenReport;
import com.topvision.ems.mobile.domain.OnuPreconfigInfo;
import com.topvision.ems.mobile.domain.PageView;
import com.topvision.ems.mobile.domain.VisitView;

/**
 * @author xiaoyue
 * @created @2017年5月6日-上午9:40:43
 *
 */
public interface MUIOnuDao {

    /**
     * 查询所有预配置
     * 
     * @return
     */
    List<OnuPreconfigInfo> getOnuPreconfigInfo();

    /**
     * 查询所有开通报告
     * 
     * @return
     */
    List<OnuOpenReport> getOnuOpenReport();

    /**
     * 清空预配置
     */
    void clearPreconfig();

    /**
     * 保存预配置集合
     * 
     * @param preconfigInfosFromDB
     */
    void savePreconfigList(List<OnuPreconfigInfo> preconfigInfosFromDB);

    /**
     * 清空开通报告
     */
    void clearOpenreport();

    /**
     * 保存开通报告集合
     * 
     * @param onuOpenReportsFromDB
     */
    void saveOpenreportList(List<OnuOpenReport> onuOpenReportsFromDB);

    /**
     * 查询onu列表
     * 
     * @param queryMap
     * @return
     */
    List<MUIOnu> selectOnuList(Map<String, Object> queryMap);

    /**
     * 根据mac或sn查询上线onu属性
     * 
     * @param uniqueId
     * @return
     */
    OltOnuAttribute selectOnlineOnuAttributeByUniqueId(String uniqueId);

    /**
     * 保存app访问表
     * 
     * @param visitViewList
     */
    void saveVisitView(List<VisitView> visitViewList);

    /**
     * 保存app页面浏览表
     * 
     * @param pageViewList
     */
    void savePageView(List<PageView> pageViewList);

    /**
     * 批量插入或更新预配置信息
     * 
     * @param onuPreconfigInfos
     */
    void batchInsertOrUpdatePreconfigInfo(List<OnuPreconfigInfo> onuPreconfigInfos);

    /**
     * 批量插入或更新开通报告
     * 
     * @param onuOpenreportList
     */
    void batchInsertOrUpdateOpenreport(List<OnuOpenReport> onuOpenreportList);

    /**
     * 查询onu经纬度和健康度指标
     * 
     * @param
     * @return List<OnuAroundInfo>
     */
    List<OnuAroundInfo> selectOnuAroundInfo();

    /**
     * 查询onu指标阈值和对应告警等级
     * 
     * @param
     * @return OnuHeathyThreshold
     */
    List<OnuHealthyThreshold> selectOnuHeathyThreshold();

    /**
     * 查询该id所有onu
     * 
     * @param
     * @return OltOnuAttribute
     */
    List<OltOnuAttribute> selectAllOnuAttributeByUniqueId(String uniqueId);

}
