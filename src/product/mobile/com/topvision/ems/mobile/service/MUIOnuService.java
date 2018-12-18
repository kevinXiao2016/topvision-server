package com.topvision.ems.mobile.service;

import java.util.ArrayList;
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
 * @created @2017年5月6日-上午9:31:52
 *
 */
public interface MUIOnuService {

    /**
     * 获取所有预配置信息
     * @return
     */
    public List<OnuPreconfigInfo> getOnuPreconfigInfo();
    
    /**
     * 获取所有开通报告
     * @return
     */
    public List<OnuOpenReport> getOnuOpenReport();

    /**
     * 同步onu预配置和开通信息
     * @param onuPreconfigInfos
     * @param openreportList
     * @return
     */
    public ArrayList<Object> synchronizingInformation(List<OnuPreconfigInfo> onuPreconfigInfos,
            List<OnuOpenReport> openreportList);

    /**
     * 查找onu列表
     * @param queryMap
     * @return
     */
    public List<MUIOnu> queryForOnuList(Map<String, Object> queryMap);

    
    /**
     * 获取上线onu属性
     * @param uniqueId
     * @return
     */
    public OltOnuAttribute queryOnlineOnuByUniqueId(String uniqueId);

    /**
     * 获取ONU下载速率
     * @param mac
     * @return
     */
    public Integer getOnuDownloadSpeed(String uniqueId);
    
    /**
     * 获得有经纬度的ONU的位置信息、健康度信息
     * @param
     * @return List<OnuAroundInfo>
     */
    public List<OnuAroundInfo> getOnuAroundInfo();

    /**
     * 获取在线和不在线onu属性
     * @param uniqueId
     * @return
     */
    public List<OltOnuAttribute> queryAllOnuByUniqueId(String uniqueId);

    /**
     * 保存用户体验数据
     * @param visitViewList
     * @param pageViewList
     */
    public void saveUserExper(List<VisitView> visitViewList, List<PageView> pageViewList);

    /**
     * 保存预配置信息
     * @param onuPreconfigInfos
     */
    public void savePreconfigInfo(List<OnuPreconfigInfo> onuPreconfigInfos);

    /**
     * 保存开通报告
     * @param onuOpenreportList
     */
    public void saveOpenreport(List<OnuOpenReport> onuOpenreportList);

}
