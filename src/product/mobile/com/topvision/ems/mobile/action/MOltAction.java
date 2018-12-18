/***********************************************************************
 * $Id: MOltAction.java,v1.0 2016年7月16日 下午1:59:07 $
 * 
 * @author: YangYi
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.mobile.action;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.topvision.ems.cmc.ccmts.service.CmcService;
import com.topvision.ems.cmc.opticalreceiver.domain.TopCcmtsSysDorType;
import com.topvision.ems.cmc.util.CmcUtil;
import com.topvision.ems.devicesupport.version.service.DeviceVersionService;
import com.topvision.ems.epon.olt.service.OltPonService;
import com.topvision.ems.epon.olt.service.OltService;
import com.topvision.ems.epon.onuauth.domain.OltAuthentication;
import com.topvision.ems.epon.onuauth.domain.OnuAuthFail;
import com.topvision.ems.epon.onuauth.service.OnuAuthService;
import com.topvision.ems.facade.domain.DiscoveryData;
import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.mobile.domain.BaiduMapInfo;
import com.topvision.ems.mobile.domain.CmtsInfo;
import com.topvision.ems.mobile.domain.MEntityType;
import com.topvision.ems.mobile.domain.MobileOlt;
import com.topvision.ems.mobile.domain.MobileOnu;
import com.topvision.ems.mobile.service.MOltService;
import com.topvision.ems.mobile.util.MobileUtil;
import com.topvision.ems.network.domain.SubDeviceCount;
import com.topvision.ems.network.service.DiscoveryService;
import com.topvision.ems.network.service.EntityService;
import com.topvision.ems.template.service.EntityTypeService;
import com.topvision.framework.common.DateUtils;
import com.topvision.framework.common.MacUtils;
import com.topvision.framework.utils.EponConstants;
import com.topvision.framework.utils.EponIndex;
import com.topvision.framework.web.struts2.BaseAction;
import com.topvision.platform.domain.UserContext;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * @author YangYi
 * @created @2016年7月16日-下午1:59:07
 *
 */
@Controller("mOltAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class MOltAction extends BaseAction {
    private static final long serialVersionUID = 3268496169840536706L;
    private String queryContext;// 搜索条件

    private Long entityId;
    private MobileOlt mobileOlt;
    private Long ponIndex;
    private Integer authType;
    private Integer authAction;
    private Long onuIndex;
    private String mac;
    private String sn;
    private String password;
    private Integer onuPreType;
    private Integer onuNum;
    private String address;
    private Double latitude;
    private Double longitude;

    @Autowired
    private OnuAuthService onuAuthService;
    @Autowired
    private MOltService mOltService;
    @Autowired
    private EntityService entityService;
    @Autowired
    private DiscoveryService<DiscoveryData> discoveryService;
    @Autowired
    private EntityTypeService entityTypeService;
    @Resource(name = "deviceVersionService")
    private DeviceVersionService deviceVersionService;
    @Autowired
    private OltPonService oltPonService;
    @Autowired
    private CmcService cmcService;
    @Autowired
    private OltService oltService;

    public String getOltList() throws IOException {
        Map<String, Object> map = new HashMap<String, Object>();
        if (queryContext != null && !"".equals(queryContext.trim())) {
            // mysql中下划线是特殊的，like的时候必须转义
            if (queryContext.contains("_")) {
                queryContext = queryContext.replace("_", "\\_");
            }
            map.put("queryContext", queryContext.trim());
            String formatQueryMac = MacUtils.formatQueryMac(queryContext);
            if (formatQueryMac.indexOf(":") == -1) {
                map.put("queryMacWithoutSplit", formatQueryMac);
            }
            map.put("queryContentMac", formatQueryMac);
        }
        /*if (queryContext != null) {
            map.put("queryContext", MobileUtil.convertQueryContext(queryContext));
        }
        String formatQueryMac = MacUtils.formatQueryMac(MobileUtil.convertQueryContext(queryContext));
        if (formatQueryMac.indexOf(":") == -1) {
            map.put("queryMacWithoutSplit", formatQueryMac);
        }
        map.put("queryContentMac", formatQueryMac);*/

        map.put("start", start);
        map.put("limit", limit);
        List<MobileOlt> list = mOltService.getOltList(map);
        Integer totalCount = mOltService.getOltListCount(map);
        JSONObject json = new JSONObject();
        json.put("totalCount", totalCount);
        json.put("data", list);
        writeDataToAjax(json);
        return NONE;
    }

    /**
     * 全局ONU列表 1、获取当前的ONU列表 2、该数据从数据库直接读取，通过下拉滑动来加载新数据和从数据库重新加载列表 3、需要展示ONU名称、MAC、在线状态、设备类型
     * 4、需要支持分页，每页5项记录 5、每个ONU后面提供一个解注册按钮6、每个ONU后面提供一个重启按钮
     * 
     * @return
     */
    public String getOnuList() {
        Map<String, Object> map = new HashMap<String, Object>();
        if (queryContext != null && !"".equals(queryContext.trim())) {
            // mysql中下划线是特殊的，like的时候必须转义
            if (queryContext.contains("_")) {
                queryContext = queryContext.replace("_", "\\_");
            }
            map.put("queryContext", queryContext.trim());
            String formatQueryMac = MacUtils.formatQueryMac(queryContext);
            if (formatQueryMac.indexOf(":") == -1) {
                map.put("queryMacWithoutSplit", formatQueryMac);
            }
            map.put("queryContentMac", formatQueryMac);
        }
        /*if (queryContext != null) {
            map.put("queryText", queryContext);
        }
        if (queryContext != null) {
            map.put("queryContext", MobileUtil.convertQueryContext(queryContext));
        }
        String formatQueryMac = MacUtils.formatQueryMac(MobileUtil.convertQueryContext(queryContext));
        if (formatQueryMac.indexOf(":") == -1) {
            map.put("queryMacWithoutSplit", formatQueryMac);
        }
        map.put("queryContentMac", formatQueryMac);*/
        map.put("start", start);
        map.put("limit", limit);
        List<MobileOnu> list = mOltService.getOltOnuList(map);
        Integer totalCount = mOltService.getOltOnuCount(map);
        JSONObject json = new JSONObject();
        json.put("totalCount", totalCount);
        json.put("data", list);
        writeDataToAjax(json);
        return NONE;
    }
    //加入地域判断
    public String getOnuListWithRegion() {
        Map<String, Object> map = new HashMap<String, Object>();
        if (queryContext != null && !"".equals(queryContext.trim())) {
            // mysql中下划线是特殊的，like的时候必须转义
            if (queryContext.contains("_")) {
                queryContext = queryContext.replace("_", "\\_");
            }
            map.put("queryContext", queryContext.trim());
            String formatQueryMac = MacUtils.formatQueryMac(queryContext);
            if (formatQueryMac.indexOf(":") == -1) {
                map.put("queryMacWithoutSplit", formatQueryMac);
            }
            map.put("queryContentMac", formatQueryMac);
        }
        /*if (queryContext != null) {
            map.put("queryText", queryContext);
        }
        if (queryContext != null) {
            map.put("queryContext", MobileUtil.convertQueryContext(queryContext));
        }
        String formatQueryMac = MacUtils.formatQueryMac(MobileUtil.convertQueryContext(queryContext));
        if (formatQueryMac.indexOf(":") == -1) {
            map.put("queryMacWithoutSplit", formatQueryMac);
        }
        map.put("queryContentMac", formatQueryMac);*/
        map.put("start", start);
        map.put("limit", limit);
        List<MobileOnu> list = mOltService.getOltOnuListWithRegion(map);
        Integer totalCount = mOltService.getOltOnuCountWithRegion(map);
        JSONObject json = new JSONObject();
        json.put("totalCount", totalCount);
        json.put("data", list);
        writeDataToAjax(json);
        return NONE;
    }

    /**
     * OLT 基本信息展示 基本信息需要展示的内容包括设备类型、设备别名、管理IP、在线状态、在线时间、软件版本、CPU、内存、下级CC数量
     * 
     * @return
     */
    public String getOltBaseInfo() {
        JSONObject json = new JSONObject();
        mobileOlt = mOltService.getOltBaseInfo(entityId);
        SubDeviceCount subCountInfo = oltService.getSubCountInfo(entityId);
        mobileOlt.setOnuOnline(subCountInfo.getOnuOnline());
        mobileOlt.setOnuTotal(subCountInfo.getOnuTotal());
        subCountInfo = cmcService.getSubCountInfo(entityId);
        mobileOlt.setCmcOnline(subCountInfo.getCmcOnline());
        mobileOlt.setCmcTotal(subCountInfo.getCmcTotal());
        if (mobileOlt == null) {
            json.put("deviceExist", false);
        } else {
            mobileOlt.setSysUpTimeString(CmcUtil.timeFormatToZh(mobileOlt.getSysUpTime()));
            json = JSONObject.fromObject(mobileOlt);
            json.put("deviceExist", true);
        }
        writeDataToAjax(json);
        return NONE;
    }

    /**
     * OLT下CMTS列表 1、获取OLT下当前的CMTS列表 2、该数据从数据库直接读取，通过下拉滑动来加载新数据和从数据库重新加载列表 3、需要展示CMTS名称、在线状态、设备类型
     * 4、需要支持分页，每页5项记录。 5、在页面提供一个重新拓扑OLT的按钮，用于对OLT进行重新拓扑发现
     * 
     * @return
     */
    public String getOltCmtsList() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("entityId", entityId);
        if (queryContext != null) {
            map.put("queryContext", MobileUtil.convertQueryContext(queryContext));
        }
        map.put("start", start);
        map.put("limit", limit);
        List<CmtsInfo> list = mOltService.getOltCmtsList(map);
        for (CmtsInfo cmts : list) {
            Boolean version = deviceVersionService.isFunctionSupported(Long.parseLong(cmts.getCmtsId()),
                    "opticalReceiverRead");
            Boolean isSuppertOptical = false;
            if (version) {
                if (cmts.getTopCcmtsSysDorType() != null && !"".equalsIgnoreCase(cmts.getTopCcmtsSysDorType())) {
                    isSuppertOptical = true;
                }
            }
            cmts.setIsSupportOptical(isSuppertOptical);// 是否支持光机
        }
        Integer totalCount = mOltService.getOltCmtsCount(map);
        JSONObject json = new JSONObject();
        json.put("totalCount", totalCount);
        json.put("data", list);
        writeDataToAjax(json);
        return NONE;
    }

    public String getOltCmtsListWithRegion() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("entityId", entityId);
        if (queryContext != null) {
            map.put("queryContext", MobileUtil.convertQueryContext(queryContext));
        }
        map.put("start", start);
        map.put("limit", limit);
        List<CmtsInfo> list = mOltService.getOltCmtsListWithRegion(map);
        for (CmtsInfo cmts : list) {
            Boolean version =  deviceVersionService.isFunctionSupported(Long.parseLong(cmts.getCmtsId()),"opticalReceiverRead");
            Boolean isSuppertOptical = false;
            Boolean isSupportFOptical=false;
            if (version) {
                if (cmts.getTopCcmtsSysDorType() != null && !"".equalsIgnoreCase(cmts.getTopCcmtsSysDorType())) {
                    isSuppertOptical = true;
                    if(TopCcmtsSysDorType.OPTICALRECEIVER_TYPE_FFA.equalsIgnoreCase(cmts.getTopCcmtsSysDorType())
                                || TopCcmtsSysDorType.OPTICALRECEIVER_TYPE_FFB.equalsIgnoreCase(cmts.getTopCcmtsSysDorType())){
                        isSupportFOptical=true;
                    }
                }
            }
            cmts.setIsSupportOptical(isSuppertOptical);// 是否支持光机
            cmts.setIsFOptical(isSupportFOptical);
            if (entityTypeService.isCcmtsWithoutAgent(cmts.getTypeId())) {
                cmts.setIsCcWithoutAgent(true);
            } else {
                cmts.setIsCcWithoutAgent(false);
            }
            if (entityTypeService.isCcmtsWithAgent(cmts.getTypeId())) {
                cmts.setIsCcWithAgent(true);
            } else {
                cmts.setIsCcWithAgent(false);
            }
        }
        Integer onlineCount = mOltService.getOltCmtsOnlineCountWithRegion(map);
        Integer totalCount = mOltService.getOltCmtsCountWithRegion(map);
        JSONObject json = new JSONObject();
        json.put("onlineCount", onlineCount);
        json.put("totalCount", totalCount);
        json.put("data", list);
        writeDataToAjax(json);
        return NONE;
    }

    /**
     * OLT下ONU列表 1、获取OLT下当前的ONU列表 2、该数据从数据库直接读取，通过下拉滑动来加载新数据和从数据库重新加载列表 3、需要展示ONU名称、MAC、在线状态、设备类型
     * 4、需要支持分页，每页5项记录 5、页面提供一个重新拓扑OLT的按钮，用于对OLT进行重新拓扑发现 6、每个ONU后面提供一个解注册按钮 7、每个ONU后面提供一个重启按钮
     * 
     * @return
     */
    public String getOltOnuList() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("entityId", entityId);
        if (queryContext != null) {
            map.put("queryContext", MobileUtil.convertQueryContext(queryContext));
        }
        map.put("start", start);
        map.put("limit", limit);
        List<MobileOnu> list = mOltService.getOltOnuList(map);
        Integer totalCount = mOltService.getOltOnuCount(map);
        Integer onlineCount = mOltService.getOltOnuOnlineCount(map);
        JSONObject json = new JSONObject();
        json.put("totalCount", totalCount);
        json.put("onlineCount", onlineCount);
        json.put("data", list);
        writeDataToAjax(json);
        return NONE;
    }

    public String getOltOnuListWithRegion() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("entityId", entityId);
        if (queryContext != null) {
            map.put("queryContext", MobileUtil.convertQueryContext(queryContext));
        }
        map.put("start", start);
        map.put("limit", limit);
        List<MobileOnu> list = mOltService.getOltOnuListWithRegion(map);
        Integer totalCount = mOltService.getOltOnuCountWithRegion(map);
        Integer onlineCount = mOltService.getOltOnuOnlineCount(map);
        JSONObject json = new JSONObject();
        json.put("totalCount", totalCount);
        json.put("onlineCount", onlineCount);
        json.put("data", list);
        writeDataToAjax(json);
        return NONE;
    }

    /**
     * OLT下ONU认证失败列表 1、获取OLT下当前的ONU认证失败列表 2、该数据实时从OLT上获取 3、需要展示ONU MAC、最近认证时间、SN、密码
     * 4、在每一个认证失败的ONU后面提供一个添加认证按钮
     * 
     * @return
     */
    public String getOnuAuthFailedList() {
        // UserContext uc = (UserContext)
        // ServletActionContext.getRequest().getSession().getAttribute("UserContext");
        // 按照需求需要在获取信息时先刷新人之失败列表
        UserContext uc = (UserContext) super.getSession().get(UserContext.KEY);
        try {
            onuAuthService.refreshOnuAuthenBlockList(entityId);
        } catch (Exception e) {
            logger.info("refreshOnuAuthenBlockList error {}", e);
        }
        Map<String, Object> queryMap = new HashMap<String, Object>();
        queryMap.put("start", start);
        queryMap.put("limit", limit);
        queryMap.put("entityId", entityId);
        List<OnuAuthFail> onuAuthFailList = mOltService.getOnuAuthFailList(queryMap);
        for (OnuAuthFail authFail : onuAuthFailList) {
            if (authFail.getLastAuthTime() != null) {
                String authTimeStr = DateUtils.getTimeDesInObscure(authFail.getLastAuthTime() * 10,
                        uc.getUser().getLanguage());
                authFail.setLastAuthTimeString(authTimeStr);
            }
        }
        Long rowCount = mOltService.getOnuAuthFailCount(queryMap);
        JSONObject json = new JSONObject();
        json.put("data", onuAuthFailList);
        json.put("rowCount", rowCount);
        writeDataToAjax(json);
        return NONE;
    }

    public String showAddOnuAuth() {
        JSONObject json = new JSONObject();
        Long onuType = entityTypeService.getOnuType();
        List<MEntityType> entityTypeList = mOltService.getEntityTypeList(onuType);
        List<Long> onuNums = mOltService.getPonOnuIndex(entityId, ponIndex);
        Integer ponAuthMode = onuAuthService.getPonOnuAuthMode(entityId, ponIndex);
        OnuAuthFail authFail = mOltService.getOnuAuthFailObject(entityId, onuIndex);
        if (authFail != null) {
            json.put("mac", authFail.getMac());
            json.put("sn", authFail.getSn());
            json.put("password", authFail.getPassword());
        }
        json.put("entityId", entityId);
        json.put("ponIndex", ponIndex);
        json.put("ponString", EponIndex.getPortStringByIndex(ponIndex).toString());
        json.put("ponAuthMode", ponAuthMode);
        json.put("onuTypeList", JSONArray.fromObject(entityTypeList));
        json.put("onuNums", JSONArray.fromObject(onuNums));
        writeDataToAjax(json);
        return NONE;
    }

    /**
     * 认证阻塞表中添加ONU认证
     * 
     * @return
     */
    public String addOnuAuthRule() {
        JSONObject json = new JSONObject();
        try {
            if (!MacUtils.isMac(mac)) {
                json.put("errorMessage", "macError");
                return NONE;
            }
            if (EponConstants.OLT_AUTHEN_SN.equals(authType)) {
                if ("".equals(sn) || sn == null) {
                    json.put("errorMessage", "snError");
                    return NONE;
                } else {
                    if (sn.length() > 24) {
                        json.put("errorMessage", "snError");
                        return NONE;
                    }
                    if (password != null && password.length() > 12) {
                        json.put("errorMessage", "pwdError");
                        return NONE;
                    }
                }
            }
            OltAuthentication oltAuthentication = new OltAuthentication();
            oltAuthentication.setEntityId(entityId);
            oltAuthentication.setAuthType(authType);
            oltAuthentication.setAuthAction(authAction);
            oltAuthentication.setOnuAuthenMacAddress(mac);
            Integer slotNum = EponIndex.getSlotNo(ponIndex).intValue();
            Integer ponNum = EponIndex.getPonNo(ponIndex).intValue();
            Long onuIndex = EponIndex.getOnuIndex(slotNum, ponNum, onuNum);
            Long ponId = oltPonService.getPonIdByIndex(entityId, ponIndex);
            oltAuthentication.setPonId(ponId);
            oltAuthentication.setOnuIndex(onuIndex);
            oltAuthentication.setTopOnuAuthLogicSn(sn);
            oltAuthentication.setTopOnuAuthPassword(password);
            onuAuthService.addOnuAuthenPreConfig(oltAuthentication);
            if (mac != null && EponConstants.OLT_AUTHEN_MACACTION_ACCEPT.equals(authAction)) {
                onuAuthService.deleteOnuAuthBlock(ponNum.longValue(), onuIndex);
            } else if (!("").equalsIgnoreCase(sn)) {
                onuAuthService.deleteOnuAuthBlock(ponNum.longValue(), onuIndex);
            }
            if (!(("").equalsIgnoreCase(onuPreType.toString()) || (EponConstants.OLT_AUTHEN_MAC.equals(authType)
                    && EponConstants.OLT_AUTHEN_MACACTION_REJECT.equals(authAction)))) {
                onuAuthService.modifyOnuPreType(entityId, onuIndex, onuPreType.toString());
            }
            discoveryService.refresh(entityId);
            json.put("success", true);
        } catch (Exception ex) {
            logger.error("addOnuAuth error:{}", ex);
            json.put("success", false);
        } finally {
            writeDataToAjax(json);
        }
        return NONE;
    }

    /**
     * 重新拓扑OLT设备
     * 
     * @return
     */
    public String reTopoOlt() {
        JSONObject json = new JSONObject();
        try {
            discoveryService.refresh(entityId);
            json.put("result", 1);
        } catch (Exception e) {
            json.put("result", 0);
        }
        writeDataToAjax(json);
        return NONE;
    }

    public String getReTopoResult() {
        // entityId
        JSONObject json = new JSONObject();
        json.put("finished", 1);
        writeDataToAjax(json);
        return NONE;
    }
    
    public String modifyLocationOltOnu() {
        Map<String, Object> map = new HashMap<String, Object>();
        Map<String, String> message = new HashMap<String, String>();
        String result="success";
        BaiduMapInfo bmi=mOltService.getBaiduMapInfo(entityId);
        map.put("entityId", entityId);
        map.put("address", address);
        if(bmi!=null){
            map.put("latitude", bmi.getLatitude());
            map.put("longitude", bmi.getLongitude());  
        }else{
            map.put("latitude", 0.0);
            map.put("longitude", 0.0);
        }
        try{
            mOltService.modifyEntityLocation(map);
        }catch (Exception e) {
            logger.debug("", e);
            result = "fail";
        }
        message.put("message", result);
        writeDataToAjax(JSONObject.fromObject(message));
        return NONE;
    }

    public String transEntityMaptoDB() {
        Map<String, Object> map = new HashMap<String, Object>();
        Map<String, String> message = new HashMap<String, String>();
        String result="success";
        Entity entity=entityService.getEntity(entityId);
        map.put("entityId", entityId);
        map.put("typeId", entity.getTypeId());
        map.put("address", address);
        map.put("latitude", latitude);
        map.put("longitude", longitude);
        try{
            mOltService.saveMapDataToDB(map);
        }catch (Exception e) {
            logger.debug("", e);
            result = "fail";
        }
        message.put("message", result);
        writeDataToAjax(JSONObject.fromObject(message));
        return NONE;
    }
    public String getQueryContext() {
        return queryContext;
    }

    public void setQueryContext(String queryContext) {
        this.queryContext = queryContext;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public MobileOlt getMobileOlt() {
        return mobileOlt;
    }

    public void setMobileOlt(MobileOlt mobileOlt) {
        this.mobileOlt = mobileOlt;
    }

    public Long getPonIndex() {
        return ponIndex;
    }

    public void setPonIndex(Long ponIndex) {
        this.ponIndex = ponIndex;
    }

    public Integer getAuthType() {
        return authType;
    }

    public void setAuthType(Integer authType) {
        this.authType = authType;
    }

    public Integer getAuthAction() {
        return authAction;
    }

    public void setAuthAction(Integer authAction) {
        this.authAction = authAction;
    }

    public Long getOnuIndex() {
        return onuIndex;
    }

    public void setOnuIndex(Long onuIndex) {
        this.onuIndex = onuIndex;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getOnuPreType() {
        return onuPreType;
    }

    public void setOnuPreType(Integer onuPreType) {
        this.onuPreType = onuPreType;
    }

    public Integer getOnuNum() {
        return onuNum;
    }

    public void setOnuNum(Integer onuNum) {
        this.onuNum = onuNum;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

}
