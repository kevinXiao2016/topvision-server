/***********************************************************************

 * $Id: OnuListAction.java,v1.0 2015年4月23日 上午10:27:12 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.mobile.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.topvision.ems.epon.domain.Onu;
import com.topvision.ems.epon.exception.OnuForceReplaceException;
import com.topvision.ems.epon.onu.domain.OltOnuAttribute;
import com.topvision.ems.epon.onu.domain.OnuWanConnect;
import com.topvision.ems.epon.onu.domain.OnuWanSsid;
import com.topvision.ems.epon.onu.service.OnuReplaceService;
import com.topvision.ems.epon.onu.service.OnuService;
import com.topvision.ems.epon.onu.service.OnuWanService;
import com.topvision.ems.epon.onuauth.domain.OltAuthentication;
import com.topvision.ems.epon.onuauth.service.OnuAuthService;
import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.gpon.onuauth.GponConstant;
import com.topvision.ems.mobile.domain.MUIOnu;
import com.topvision.ems.mobile.domain.OnuAroundInfo;
import com.topvision.ems.mobile.domain.OnuOpenReport;
import com.topvision.ems.mobile.domain.OnuPreconfigInfo;
import com.topvision.ems.mobile.domain.PageView;
import com.topvision.ems.mobile.domain.VisitView;
import com.topvision.ems.mobile.service.MUIOnuService;
import com.topvision.ems.network.service.EntityService;
import com.topvision.framework.common.DateUtils;
import com.topvision.framework.common.MacUtils;
import com.topvision.framework.exception.engine.SnmpSetException;
import com.topvision.framework.utils.EponConstants;
import com.topvision.framework.utils.EponIndex;
import com.topvision.framework.web.struts2.BaseAction;
import com.topvision.platform.ResourceManager;
import com.topvision.platform.domain.UserContext;
import com.topvision.platform.util.CurrentRequest;
import com.topvision.platform.zetaframework.var.UnitConfigConstant;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * @author xiaoyue
 * @created @2017年5月3日-下午3:16:45
 *
 */
@Controller("MUIOnuAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class MUIOnuAction extends BaseAction {
    private static final long serialVersionUID = 312804567663559761L;

    @Autowired
    private MUIOnuService muiOnuService;
    @Autowired
    private OnuService onuService;
    @Autowired
    private OnuReplaceService onuReplaceService;
    @Autowired
    private OnuAuthService onuAuthService;
    @Autowired
    private EntityService entityService;
    @Autowired
    private OnuWanService onuWanService;

    private String onuId;
    private String uniqueId;// onu唯一标识，可能是mac、sn（都含有冒号）
    private Integer wanId = 3;// 默认wan3
    private Integer ssid = 1;// 默认ssid1
    private String queryContext;// 手机网管搜索框传递来的值
    private String accessList; // 预配置
    private String openreportList; // 开通报告
    private String pageViews; // 页面访问记录
    private String visitViews;// app访问记录
    private Long entityId;
    private Long realOnuId;// 网管上的onuId
    private Long onuIndex;
    private String onuMac;
    private Integer forceReplace;
    private String sn;
    private String pwd;
    private String pp;//pppoe密码
    private String pn;//账号
    private String wp;//wifi密码
    private String wn;//账号

    /**
     * 根据mac/sn/别名查找onu所有展示信息
     * 
     * @return
     */
    public String getOnuAllInfo() {
        ResourceManager resourceManager = ResourceManager.getResourceManager("com.topvision.ems.resources.resources");
        Map<String, Object> queryMap = new HashMap<String, Object>();
        queryMap.put("start", 0);
        queryMap.put("limit", 10);
        queryMap.put("wanId", wanId);
        queryMap.put("ssid", ssid);
        if (uniqueId != null) {
            queryMap.put("uniqueId", uniqueId);
        }
        if (queryContext != null) {
            queryMap.put("queryContext", queryContext);
            // mysql中下划线是特殊的，like的时候必须转义
            if (queryContext.contains("_")) {
                queryContext = queryContext.replace("_", "\\_");
            }
            String formatQueryMac = MacUtils.formatQueryMac(queryContext);
            if (formatQueryMac.indexOf(":") == -1) {
                queryMap.put("queryMacWithoutSplit", formatQueryMac);
            }
            queryMap.put("queryContentMac", formatQueryMac);
        }
        List<MUIOnu> list = muiOnuService.queryForOnuList(queryMap);
        UserContext uc = CurrentRequest.getCurrentUser();
        for (MUIOnu muiOnu : list) {
            //设置运行时长
            if (Integer.valueOf(EponConstants.ADMIN_STATUS_ENABLE).equals(muiOnu.getOnuOperationStatus())) {
                String onuRunTime = DateUtils.getTimePeriod(System.currentTimeMillis()
                        - muiOnu.getChangeTime().getTime() + +muiOnu.getOnuTimeSinceLastRegister() * 1000,
                        uc.getUser().getLanguage());
                muiOnu.setOnuRunTime(onuRunTime);
            } else {
                String onuRunTime = resourceManager.getNotNullString("deviceInfo.offline");
                if (muiOnu.getLastDeregisterTime() != null) {
                    String offlineInfo = "(" + DateUtils.getTimeDesInObscure(
                            System.currentTimeMillis() - muiOnu.getLastDeregisterTime().getTime(),
                            uc.getUser().getLanguage()) + ")";
                    onuRunTime = onuRunTime + offlineInfo;
                }
                muiOnu.setOnuRunTime(onuRunTime);
            }
            //将SN转为16进制字符串，去掉冒号
            String _uniqueId = muiOnu.getUniqueId();
            if (!MacUtils.isMac(_uniqueId)) {
                muiOnu.setUniqueId(_uniqueId.replace(":", "").toUpperCase());
            }
        }
        JSONObject jo = new JSONObject();
        jo.put("onuList", list);
        writeDataToAjax(jo);
        return NONE;
    }

    /**
     * 预配置与开通报告同步
     * 
     * @return
     */
    /*
     * public String synchronizingInformation() { List<OnuPreconfigInfo> OnuPreconfigInfos = null;
     * List<OnuOpenReport> OnuOpenreportList = new ArrayList<>(); // 将app传来的accessList字符串转为List集合 if
     * (accessList != null && !accessList.equals("null")) { JSONArray array =
     * JSONArray.fromObject(accessList); OnuPreconfigInfos = (List<OnuPreconfigInfo>)
     * JSONArray.toCollection(array, OnuPreconfigInfo.class); } // 将app传来的openreportList字符串转为List集合
     * if (openreportList != null && !openreportList.equals("null")) { JSONArray array1 =
     * JSONArray.fromObject(openreportList); OnuOpenReport openreport = new OnuOpenReport(); if
     * (array1 != null && array1.size() != 0) { Object obj = null; JSONObject jo = null; for (int i
     * = 0,len=array1.size(); i < len; i++) { jo = JSONObject.fromObject(array1.get(i)); //
     * Timestamp time = new Timestamp(jo.getLong("time")); openreport.setMac(jo.getString("mac"));
     * openreport.setTime(jo.getString("time"));
     * openreport.setOnuOnlineState(jo.get("onuOnlineState").toString());
     * openreport.setOnuOptical(jo.get("onuOptical").toString());
     * openreport.setWanState(jo.get("wanState").toString());
     * openreport.setWifiState(jo.get("wifiState").toString());
     * openreport.setInternetConnect(jo.get("internetConnect").toString());
     * openreport.setNm3000Connect(jo.get("nm3000Connect").toString());
     * openreport.setNm3000Control(jo.get("nm3000Control").toString());
     * OnuOpenreportList.add(openreport); } } } ArrayList list =
     * muiOnuService.synchronizingInformation(OnuPreconfigInfos, OnuOpenreportList);
     * OnuPreconfigInfos = (List<OnuPreconfigInfo>) list.get(0); OnuOpenreportList =
     * (List<OnuOpenReport>) list.get(1);
     * 
     * List<OnuOpenReport2App> reportList2App = new ArrayList<>(); if (OnuOpenreportList != null &&
     * OnuOpenreportList.size() > 0) { OnuOpenReport2App openReport2App = new OnuOpenReport2App();
     * Map<String, String> map = new HashMap<>(); for (OnuOpenReport openreport : OnuOpenreportList)
     * { openReport2App.setMac(openreport.getMac()); openReport2App.setTime(openreport.getTime());
     * map.put("flag", JSONObject.fromObject(openreport.getOnuOnlineState()).getString("flag"));
     * map.put("text", JSONObject.fromObject(openreport.getOnuOnlineState()).getString("text"));
     * openReport2App.setOnuOnlineState(map); map.put("flag",
     * JSONObject.fromObject(openreport.getOnuOptical()).getString("flag")); map.put("text",
     * JSONObject.fromObject(openreport.getOnuOptical()).getString("text"));
     * openReport2App.setOnuOptical(map); map.put("flag",
     * JSONObject.fromObject(openreport.getWanState()).getString("flag")); map.put("text",
     * JSONObject.fromObject(openreport.getWanState()).getString("text"));
     * openReport2App.setWanState(map); map.put("flag",
     * JSONObject.fromObject(openreport.getWifiState()).getString("flag")); map.put("text",
     * JSONObject.fromObject(openreport.getWifiState()).getString("text"));
     * openReport2App.setWifiState(map); map.put("flag",
     * JSONObject.fromObject(openreport.getInternetConnect()).getString("flag")); map.put("text",
     * JSONObject.fromObject(openreport.getInternetConnect()).getString("text"));
     * openReport2App.setInternetConnect(map); map.put("flag",
     * JSONObject.fromObject(openreport.getNm3000Connect()).getString("flag")); map.put("text",
     * JSONObject.fromObject(openreport.getNm3000Connect()).getString("text"));
     * openReport2App.setNm3000Connect(map); map.put("flag",
     * JSONObject.fromObject(openreport.getNm3000Control()).getString("flag")); map.put("text",
     * JSONObject.fromObject(openreport.getNm3000Control()).getString("text"));
     * openReport2App.setNm3000Control(map); reportList2App.add(openReport2App); } } JSONObject json
     * = new JSONObject(); json.put("accessList", OnuPreconfigInfos); json.put("openreportList",
     * reportList2App); write(json); return NONE; }
     */

    /**
     * 上传用户体验数据
     * 
     * @return
     */
    @SuppressWarnings("unchecked")
    public String uploadUserExper() {
        List<VisitView> visitViewList = null;
        List<PageView> pageViewList = null;
        // 将app传来的accessList字符串转为List集合
        if (visitViews != null && !visitViews.equals("null")) {
            JSONArray array = JSONArray.fromObject(visitViews);
            visitViewList = (List<VisitView>) JSONArray.toCollection(array, VisitView.class);
        }
        if (pageViews != null && !pageViews.equals("null")) {
            JSONArray array = JSONArray.fromObject(pageViews);
            pageViewList = (List<PageView>) JSONArray.toCollection(array, PageView.class);
        }
        muiOnuService.saveUserExper(visitViewList, pageViewList);
        return NONE;
    }

    /**
     * 上传预配置
     * 
     * @return
     */
    @SuppressWarnings("unchecked")
    public String uploadPreconfigInfo() {
        List<OnuPreconfigInfo> onuPreconfigInfos = null;
        // 将app传来的accessList字符串转为List集合
        if (accessList != null && !accessList.equals("null")) {
            JSONArray array = JSONArray.fromObject(accessList);
            onuPreconfigInfos = (List<OnuPreconfigInfo>) JSONArray.toCollection(array, OnuPreconfigInfo.class);
        }
        muiOnuService.savePreconfigInfo(onuPreconfigInfos);
        return NONE;
    }

    /**
     * 上传开通报告
     * 
     * @return
     */
    public String uploadOpenreport() {
        List<OnuOpenReport> onuOpenreportList = new ArrayList<>();
        // 将app传来的openreportList字符串转为List集合
        if (openreportList != null && !openreportList.equals("null")) {
            JSONArray array = JSONArray.fromObject(openreportList);
            OnuOpenReport openreport = new OnuOpenReport();
            if (array != null && array.size() != 0) {
                JSONObject jo = null;
                for (int i = 0, len = array.size(); i < len; i++) {
                    jo = JSONObject.fromObject(array.get(i));
                    // Timestamp time = new Timestamp(jo.getLong("time"));
                    openreport.setUniqueId(jo.getString("uniqueId"));
                    openreport.setTime(Long.valueOf(jo.getString("time").toString()));
                    openreport.setOnuOnlineState(jo.get("onuOnlineState").toString());
                    openreport.setOnuOptical(jo.get("onuOptical").toString());
                    openreport.setWanState(jo.get("wanState").toString());
                    openreport.setWifiState(jo.get("wifiState").toString());
                    openreport.setInternetConnect(jo.get("internetConnect").toString());
                    openreport.setNm3000Connect(jo.get("nm3000Connect").toString());
                    openreport.setNm3000Control(jo.get("nm3000Control").toString());
                    openreport.setPppoeParamSet(jo.get("pppoeParamSet").toString());
                    openreport.setWifiParamSet(jo.get("wifiParamSet").toString());
                    onuOpenreportList.add(openreport);
                }
            }
        }
        muiOnuService.saveOpenreport(onuOpenreportList);
        return NONE;
    }

    /**
     * 根据唯一标识查找上线onu
     * 
     * @return
     */
    public String getOnuByUniqueId() {
        OltOnuAttribute onu = muiOnuService.queryOnlineOnuByUniqueId(uniqueId);
        JSONObject json = new JSONObject();
        json.put("onu", onu);
        writeDataToAjax(json);
        return NONE;
    }

    /**
     * mui手机网管重启onu
     * 
     * @return
     */
    public String rebootMUIOnu() {
        JSONObject json = new JSONObject();
        try {
            OltOnuAttribute onuAttr = muiOnuService.queryOnlineOnuByUniqueId(uniqueId);
            onuService.resetOnu(onuAttr.getEntityId(), onuAttr.getOnuId());
            json.put("result", true);
        } catch (Exception e) {
            json.put("result", false);
        }
        writeDataToAjax(json);
        return NONE;
    }

    /**
     * 获取onu下载速率
     * 
     * @return
     */
    public String getOnuDownloadSpeed() {
        JSONObject json = new JSONObject();
        Integer downRate = 0;
        try {
            downRate = muiOnuService.getOnuDownloadSpeed(uniqueId);
            if (downRate == -1) {
                json.put("TestState", 1);// 正在测试
            } else if (downRate == -2) {
                json.put("TestState", 2);// 失败
            } else if (downRate == -3) {
                json.put("TestState", 3);// dns解析错误
            } else if (downRate == -4) {
                json.put("TestState", 4);// 网络原因
            } else if (downRate == -5) {
                json.put("TestState", 5);// 未知错误
            } else {
                json.put("TestState", 0);// 成功
            }
        } catch (Exception e) {
            logger.error("", e);
            json.put("TestState", 5);
        }
        json.put("downRate", downRate);
        writeDataToAjax(json);
        return NONE;
    }

    public String getOnuAroundInfo() {
        List<OnuAroundInfo> onuAI = muiOnuService.getOnuAroundInfo();
        String powerUnit = (String) UnitConfigConstant.get(UnitConfigConstant.POWER_UNIT);
        String tempUnit = (String) UnitConfigConstant.get(UnitConfigConstant.TEMP_UNIT);
        JSONObject json = new JSONObject();
        json.put("onuAroundInfo", onuAI);
        json.put("powerUnit", powerUnit);
        json.put("tempUnit", tempUnit);
        writeDataToAjax(json);
        return NONE;
    }

    public String getPonAuthTypeAndShowView() {
        JSONObject json = new JSONObject();
        Integer ponTypeAuth = null;
        OltOnuAttribute onuAttribute = null;
        List<OltOnuAttribute> oltOnuAttribute = muiOnuService.queryAllOnuByUniqueId(uniqueId);
        if (oltOnuAttribute != null) {
            if (oltOnuAttribute.size() == 1) {
                onuAttribute = onuService.getOnuAttribute(oltOnuAttribute.get(0).getOnuId());
            } else {
                for (OltOnuAttribute ooa : oltOnuAttribute) {
                    if (ooa.getOnuOperationStatus()!=null&& ooa.getOnuOperationStatus()== 1) {
                        onuAttribute = onuService.getOnuAttribute(ooa.getOnuId());
                    }
                }
            }
        }
        if (onuAttribute != null) {
            json.put("isNull", "no");
            Long entityId = onuAttribute.getEntityId();
            Long onuIndex = onuAttribute.getOnuIndex();
            Entity entity = entityService.getEntity(onuAttribute.getOnuId());
            String onuIndexString = EponIndex.getOnuStringByIndex(onuIndex).toString();
            Map<String, Map<String, Object>> onuMacList = onuReplaceService.getOnuMacListByEntityId(entityId);
            JSONObject onuMacsJson = JSONObject.fromObject(onuMacList);
            Map<String, Map<String, Object>> onuSnList = onuReplaceService.getOnuSnListByEntityId(entityId);
            JSONObject onuSnJson = JSONObject.fromObject(onuSnList);
            OltAuthentication oltAuthentication = onuAuthService.getOltAuthenticationByIndex(entityId, onuIndex);
            if (GponConstant.GPON_ONU.equals(onuAttribute.getOnuEorG())) {
                json.put("ponTypeAuth", 0);
            } else {
                ponTypeAuth = onuReplaceService.getPonAuthType(entityId, onuIndex);
                json.put("ponTypeAuth", ponTypeAuth);
                json.put("entity", entity);
                json.put("entityId", entityId);
                json.put("onuId", onuAttribute.getOnuId());
                json.put("onuIndex", onuIndex);
                json.put("onuIndexString", onuIndexString);
                json.put("onuMacsJson", onuMacsJson);
                json.put("onuSnJson", onuSnJson);
                json.put("oltAuthentication", oltAuthentication);
            }
        } else {
            json.put("isNull", "yes");
        }
        writeDataToAjax(json);
        return NONE;
    }

    public String replaceOnuEntityByMac() throws Exception {
        String result = null;
        JSONObject json = new JSONObject();
        try {
            onuReplaceService.replaceOnuEntityByMac(entityId, realOnuId, onuIndex, onuMac, forceReplace);
            result = "success";
        } catch (OnuForceReplaceException e) {
            logger.error("replaceOnu error:", e);
            result = "onuforcereplaceerror";
        } catch (SnmpSetException e) {
            logger.error("replaceOnu error:", e);
            result = "seterror";
        } catch (Exception e) {
            logger.error("replaceOnu error:", e);
            result = "error";
        }
        json.put("result", result);
        writeDataToAjax(json);
        return NONE;
    }

    public String replaceOnuEntityBySnAndPwd() throws Exception {
        String result = null;
        JSONObject json = new JSONObject();
        try {
            onuReplaceService.replaceOnuEntityBySn(entityId, realOnuId, onuIndex, sn, pwd, forceReplace);
            result = "success";
        } catch (OnuForceReplaceException e) {
            logger.error("replaceOnu error:", e);
            result = "onuforcereplaceerror";
        } catch (SnmpSetException e) {
            logger.error("replaceOnu error:", e);
            result = "seterror";
        } catch (Exception e) {
            logger.error("replaceOnu error:", e);
            result = "error";
        }
        json.put("result", result);
        writeDataToAjax(json);
        return NONE;
    }
    
    public String configWifiParamByMobile(){
        String result;
        try{
            OltOnuAttribute onu = muiOnuService.queryOnlineOnuByUniqueId(uniqueId);
            OnuWanSsid wanSsid = onuWanService.getOnuWanSsid(onu.getOnuId(), ssid);
            Onu onuStructure = onuService.getOnuStructure(onu.getOnuId());
            wanSsid.setEntityId(onu.getEntityId());
            wanSsid.setOnuIndex(onuStructure.getOnuIndex());
            wanSsid.setSsidName(wn);
            wanSsid.setPassword(wp);
            onuWanService.updateOnuWanSsid(wanSsid);  
            result="success";
        }catch(Exception e){
            result="error";
        }
        writeDataToAjax(result);
        return NONE;
    }
    
    public String configPPPoEParamByMobile(){
        String result;
        try {
            OltOnuAttribute onu = muiOnuService.queryOnlineOnuByUniqueId(uniqueId);
            OnuWanConnect onuWanConnect = onuWanService.loadWanConnection(onu.getOnuId(), wanId);
            onuWanConnect.setEntityId(onu.getEntityId());
            onuWanConnect.setOnuId(onu.getOnuId());
            onuWanConnect.setConnectId(wanId);
            onuWanConnect.setPppoePassword(pp);
            onuWanConnect.setPppoeUserName(pn);
            onuWanService.updateOnuWanConnect(onuWanConnect);
            result = "success";
        } catch (Exception e) {
            result = "error";
        }
        writeDataToAjax(result);
        return NONE;
    }

    public String getQueryContext() {
        return queryContext;
    }

    public void setQueryContext(String queryContext) {
        this.queryContext = queryContext;
    }

    public String getAccessList() {
        return accessList;
    }

    public void setAccessList(String accessList) {
        this.accessList = accessList;
    }

    public String getOpenreportList() {
        return openreportList;
    }

    public void setOpenreportList(String openreportList) {
        this.openreportList = openreportList;
    }

    public Integer getWanId() {
        return wanId;
    }

    public void setWanId(Integer wanId) {
        this.wanId = wanId;
    }

    public Integer getSsid() {
        return ssid;
    }

    public void setSsid(Integer ssid) {
        this.ssid = ssid;
    }

    public String getOnuId() {
        return onuId;
    }

    public void setOnuId(String onuId) {
        this.onuId = onuId;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        if (!MacUtils.isMac(uniqueId)) {
            uniqueId = uniqueId.substring(0, 2) + ":" + uniqueId.substring(2, 4) + ":" + uniqueId.substring(4, 6) + ":"
                    + uniqueId.substring(6, 8) + ":" + uniqueId.substring(8, 10) + ":" + uniqueId.substring(10, 12)
                    + ":" + uniqueId.substring(12, 14) + ":" + uniqueId.substring(14, 16);
        }
        this.uniqueId = uniqueId;
    }

    public String getPageViews() {
        return pageViews;
    }

    public void setPageViews(String pageViews) {
        this.pageViews = pageViews;
    }

    public String getVisitViews() {
        return visitViews;
    }

    public void setVisitViews(String visitViews) {
        this.visitViews = visitViews;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public Long getRealOnuId() {
        return realOnuId;
    }

    public void setRealOnuId(Long realOnuId) {
        this.realOnuId = realOnuId;
    }

    public Long getOnuIndex() {
        return onuIndex;
    }

    public void setOnuIndex(Long onuIndex) {
        this.onuIndex = onuIndex;
    }

    public String getOnuMac() {
        return onuMac;
    }

    public void setOnuMac(String onuMac) {
        this.onuMac = onuMac;
    }

    public Integer getForceReplace() {
        return forceReplace;
    }

    public void setForceReplace(Integer forceReplace) {
        this.forceReplace = forceReplace;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getPp() {
        return pp;
    }

    public void setPp(String pp) {
        this.pp = pp;
    }

    public String getPn() {
        return pn;
    }

    public void setPn(String pn) {
        this.pn = pn;
    }

    public String getWp() {
        return wp;
    }

    public void setWp(String wp) {
        this.wp = wp;
    }

    public String getWn() {
        return wn;
    }

    public void setWn(String wn) {
        this.wn = wn;
    }

}
