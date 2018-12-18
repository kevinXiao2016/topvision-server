package com.topvision.ems.mobile.action;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.topvision.ems.cmc.domain.PortalChannelUtilizationShow;
import com.topvision.ems.cmc.perf.service.CmcPerfService;
import com.topvision.ems.cmc.performance.domain.ChannelCmNum;
import com.topvision.ems.cmc.performance.domain.CmcLinkQualityData;
import com.topvision.ems.cmc.performance.domain.SingleNoise;
import com.topvision.ems.cmc.performance.domain.UsBitErrorRate;
import com.topvision.ems.cmc.performance.facade.CmFlap;
import com.topvision.ems.cmc.util.CmcUtil;
import com.topvision.ems.epon.domain.PerfRecord;
import com.topvision.ems.epon.performance.service.OltPerfService;
import com.topvision.ems.fault.domain.Alert;
import com.topvision.ems.fault.service.AlertService;
import com.topvision.ems.network.domain.EntitySnap;
import com.topvision.ems.network.service.NetworkService;
import com.topvision.ems.template.service.EntityTypeService;
import com.topvision.framework.common.DateUtils;
import com.topvision.framework.common.MacUtils;
import com.topvision.framework.common.NumberUtils;
import com.topvision.framework.constants.Symbol;
import com.topvision.framework.utils.CmcIndexUtils;
import com.topvision.framework.utils.EponIndex;
import com.topvision.framework.web.struts2.BaseAction;
import com.topvision.platform.ResourceManager;
import com.topvision.platform.domain.UserContext;

import net.sf.json.JSONArray;

/**
 * @author xiaoyue
 * @created @2017年9月22日-上午9:20:04
 *
 */
@Controller("mPerfAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class MPerfAction extends BaseAction {
    private static final long serialVersionUID = -4539093871139144546L;

    @Autowired
    private CmcPerfService cmcPerfService;
    @Autowired
    private EntityTypeService entityTypeService;
    @Autowired
    private OltPerfService oltPerfService;
    @Autowired
    private NetworkService networkService;
    @Autowired
    private AlertService alertService;

    private static final String ICMP_TYPEID = "-301";// networkPortalAction 中定义此字段

    /**
     * CCMTS设备低信燥比Top10
     * 
     * @return
     * @throws IOException
     */
    public String getTopLowNoiseLoading() {
        UserContext uc = (UserContext) ServletActionContext.getRequest().getSession().getAttribute("UserContext");
        String macRule = uc.getMacDisplayStyle();
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("start", 0);
        paramMap.put("limit", uc.getTopNumber());
        try {
            List<SingleNoise> singleNoises = cmcPerfService.getTopLowNoiseLoading(paramMap);
            if (singleNoises != null) {
                for (SingleNoise singleNoise : singleNoises) {
                    Long typeId = singleNoise.getTypeId();
                    if (singleNoise.getCmcType() != null) {
                        if (entityTypeService.isCmts(typeId)) {
                            singleNoise.setCmcPortName(singleNoise.getIfName());
                        }
                        String formatedMac = MacUtils.convertMacToDisplayFormat(singleNoise.getMacAddress(), macRule);
                        String recentUpdateTime = DateUtils.getTimeDesInObscure(
                                System.currentTimeMillis() - singleNoise.getDt().getTime(), uc.getUser().getLanguage());
                        singleNoise.setMacAddress(formatedMac);
                        singleNoise.setRecentUpdateTime(recentUpdateTime);
                        // String cmmPortName = makePortName(singleNoise.getIfIndex().longValue(),
                        // new Long(singleNoise.getCmcType()), singleNoise.getIfDescr(),
                        // singleNoise.getIfType());
                        // entityTypeService.getEntityType(Long.valueOf(singleNoise.getCmcType())).getDisplayName();
                    }
                }
            }
            JSONArray jsonArray = JSONArray.fromObject(singleNoises);
            jsonArray.write(response.getWriter());
        } catch (Exception e) {
            logger.error("Get LowNoise Top10 failed", e);
        }
        return NONE;
    }

    /**
     * 通道利用率TOP 10
     * 
     * @return String
     * @throws IOException
     */
    public String getTopChnlUtiliLoading() {
        UserContext uc = (UserContext) ServletActionContext.getRequest().getSession().getAttribute("UserContext");
        String macRule = uc.getMacDisplayStyle();
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("start", 0);
        paramMap.put("limit", uc.getTopNumber());
        try {
            List<PortalChannelUtilizationShow> cmcChnlSnaps = cmcPerfService.getNetworkCcmtsDeviceLoadingTop(paramMap);
            if (cmcChnlSnaps != null) {
                for (PortalChannelUtilizationShow snap : cmcChnlSnaps) {
                    if (snap.getCmcType() != null) {
                        String formatedMac = MacUtils.convertMacToDisplayFormat(snap.getCmcMac(), macRule);
                        String cmcPortName = makePortName(snap.getChannelIndex(), snap.getCmcType(), snap.getIfDescr(),
                                snap.getIfType());
                        String typeName = entityTypeService.getEntityType(Long.valueOf(snap.getCmcType()))
                                .getDisplayName();
                        String recentUpdateTime = DateUtils.getTimeDesInObscure(
                                System.currentTimeMillis() - snap.getDt().getTime(), uc.getUser().getLanguage());
                        snap.setCmcMac(formatedMac);
                        snap.setTypeName(typeName);
                        snap.setCmcPortName(cmcPortName);
                        snap.setRecentUpdateTime(recentUpdateTime);
                    }
                }
            }
            JSONArray jsonArray = JSONArray.fromObject(cmcChnlSnaps);
            jsonArray.write(response.getWriter());
        } catch (Exception ex) {
            logger.debug("Get ChnlUtili Top10 failed", ex);
        }

        return NONE;
    }

    /**
     * CC设备用户数 TOP
     * 
     * @return String
     * @throws IOException
     */
    public String getTopCcUsersLoading() {
        UserContext uc = (UserContext) ServletActionContext.getRequest().getSession().getAttribute("UserContext");
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("start", 0);
        paramMap.put("limit", uc.getTopNumber());
        paramMap.put("target", "");
        try {
            List<ChannelCmNum> cmcChnlUsersSnaps = cmcPerfService.getNetworkCcmtsDeviceUsersLoadingTop(paramMap);
            if (cmcChnlUsersSnaps != null) {
                // add by fanzidong,需要在展示前格式化MAC地址
                String macRule = uc.getMacDisplayStyle();
                for (ChannelCmNum snap : cmcChnlUsersSnaps) {
                    if (snap.getCmcType() != null) {
                        Long typeId = snap.getCmcType().longValue();
                        if (typeId != null) {
                            String formatedMac = MacUtils.convertMacToDisplayFormat(snap.getCmcMac(), macRule);
                            if (formatedMac == null) {
                                formatedMac = "-";
                            }
                            snap.setCmcMac(formatedMac);
                            // String typeName =
                            // entityTypeService.getEntityType(Long.valueOf(snap.getCmcType())).getDisplayName();
                            if (snap.getDt() == null) {
                                snap.setRecentUpdateTime("--");
                            } else {
                                String recentUpdateTime = DateUtils.getTimeDesInObscure(
                                        System.currentTimeMillis() - snap.getDt().getTime(),
                                        uc.getUser().getLanguage());
                                snap.setRecentUpdateTime(recentUpdateTime);
                            }
                        }
                    }
                }
            }
            JSONArray jsonArray = JSONArray.fromObject(cmcChnlUsersSnaps);
            jsonArray.write(response.getWriter());
        } catch (Exception ex) {
            logger.debug("Get CcUsers Top10 failed", ex);
        }
        return NONE;
    }

    /**
     * 获取SNI端口速率排行
     * 
     * @return
     * @throws Exception
     */
    public String getTopSniLoading() throws IOException {
        UserContext uc = (UserContext) ServletActionContext.getRequest().getSession().getAttribute("UserContext");
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("start", 0);
        paramMap.put("limit", uc.getTopNumber());
        try {
            List<PerfRecord> perfRecords = oltPerfService.getTopSniLoading(paramMap);
            // NumberUtils.getBandWidth方式转换流量时单位存在问题，此方法是转换成速率，修改为NumberUtils.getByteLength方式
            // 修改人：lizongtian
            for (PerfRecord perfRecord : perfRecords) {
                /*
                 * if (perfRecord.getValue() < 0) { sb.append(NumberUtils.getIfSpeedStr(0)); } else
                 * { sb.append(NumberUtils.getIfSpeedStr(perfRecord.getValue())); }
                 * sb.append("</td>"); sb.append("<td align=center>"); if (perfRecord.getTempValue()
                 * < 0) { sb.append(NumberUtils.getIfSpeedStr(0)); } else {
                 * sb.append(NumberUtils.getIfSpeedStr(perfRecord.getTempValue())); }
                 * sb.append("</td>"); sb.append("<td align=center>");
                 */
                String recentUpdateTime = DateUtils.getTimeDesInObscure(
                        System.currentTimeMillis() - perfRecord.getCollectTime().getTime(), uc.getUser().getLanguage());
                perfRecord.setRecentUpdateTime(recentUpdateTime);
            }
            JSONArray jsonArray = JSONArray.fromObject(perfRecords);
            jsonArray.write(response.getWriter());
        } catch (Exception e) {
            logger.error("Get SniRate Top10 failed", e);
        }
        return NONE;
    }

    /**
     * Cmc光功率Top10
     * 
     * @return
     * @throws IOException
     */
    public String getCmcOpticalLoading() throws IOException {
        // 构造页面Portlet显示内容
        UserContext uc = (UserContext) ServletActionContext.getRequest().getSession().getAttribute("UserContext");
        try {
            // 从数据库获取光功率数据
            Map<String, Object> paramMap = new HashMap<String, Object>();
            paramMap.put("start", 0);
            paramMap.put("limit", uc.getTopNumber());
            paramMap.put("deviceType", -1L);
            List<CmcLinkQualityData> opticalList = cmcPerfService.getCmcOpticalInfo(paramMap);
            for (CmcLinkQualityData opticalInfo : opticalList) {
                // 类A型和类B型处理方式不一样
                if (entityTypeService.isCcmtsWithoutAgent(opticalInfo.getTypeId())) {
                    opticalInfo.setLocation(EponIndex.getOnuStringByIndex(opticalInfo.getPortIndex()).toString());
                } else {
                    opticalInfo.setLocation(CmcIndexUtils.getMarkFromIndex(opticalInfo.getPortIndex()).toString());
                }
                opticalInfo.setFromLastTime(DateUtils.getTimeDesInObscure(
                        System.currentTimeMillis() - opticalInfo.getCollectTime().getTime(),
                        uc.getUser().getLanguage()));
            }
            JSONArray jsonArray = JSONArray.fromObject(opticalList);
            jsonArray.write(response.getWriter());
        } catch (Exception e) {
            logger.debug("Get CmcOptical Top10 failed", e);
        }
        return NONE;
    }

    /**
     * CC上行通道用户数 TOP
     * 
     * @return String
     */
    public String getTopUpChnUsersLoading() {
        UserContext uc = (UserContext) ServletActionContext.getRequest().getSession().getAttribute("UserContext");
        String macRule = uc.getMacDisplayStyle();
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("start", 0);
        paramMap.put("limit", uc.getTopNumber());
        paramMap.put("target", "up");
        try {
            List<ChannelCmNum> cmcChnlUsersSnaps = cmcPerfService.getNetworkCcmtsDeviceUsersLoadingTop(paramMap);
            if (cmcChnlUsersSnaps != null) {
                for (ChannelCmNum snap : cmcChnlUsersSnaps) {
                    Long typeId = snap.getCmcType().longValue();
                    String cmtsPortName = "";
                    if (typeId != null) {
                        if (entityTypeService.isCmts(typeId)) {
                            cmtsPortName = snap.getIfName();
                        }
                        String cmcPortName = makePortName(snap.getChannelIndex(), snap.getCmcType().longValue(),
                                snap.getIfDescr(), snap.getIfType());
                        String formatedMac = MacUtils.convertMacToDisplayFormat(snap.getCmcMac(), macRule);
                        snap.setCmcMac(formatedMac);
                        snap.setDisplayName(
                                entityTypeService.getEntityType(Long.valueOf(snap.getCmcType())).getDisplayName());
                        if (entityTypeService.isCmts(typeId)) {
                            snap.setDisplayPortName(cmtsPortName);
                        } else {
                            snap.setDisplayPortName(cmcPortName);
                        }
                        if (snap.getDt() == null) {
                            snap.setRecentUpdateTime("--");
                        } else {
                            snap.setRecentUpdateTime(DateUtils.getTimeDesInObscure(
                                    System.currentTimeMillis() - snap.getDt().getTime(), uc.getUser().getLanguage()));
                        }
                    }
                }
            }
            JSONArray jsonArray = JSONArray.fromObject(cmcChnlUsersSnaps);
            jsonArray.write(response.getWriter());
        } catch (Exception ex) {
            logger.debug("Get cmts UpChnUsers Top10 failed", ex);
        }
        return NONE;
    }

    /**
     * CC下行通道 用户数 TOP
     * 
     * @return String
     */
    public String getTopDownChnUsersLoading() {
        UserContext uc = (UserContext) ServletActionContext.getRequest().getSession().getAttribute("UserContext");
        String macRule = uc.getMacDisplayStyle();
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("start", 0);
        paramMap.put("limit", uc.getTopNumber());
        paramMap.put("target", "down");
        try {
            List<ChannelCmNum> cmcChnlUsersSnaps = cmcPerfService.getNetworkCcmtsDeviceUsersLoadingTop(paramMap);
            if (cmcChnlUsersSnaps != null) {
                for (ChannelCmNum snap : cmcChnlUsersSnaps) {
                    Long typeId = snap.getCmcType().longValue();
                    String cmtsPortName = "";
                    if (typeId != null) {
                        if (entityTypeService.isCmts(typeId)) {
                            cmtsPortName = snap.getIfName();
                        }
                        String cmcPortName = makePortName(snap.getChannelIndex(), snap.getCmcType().longValue(),
                                snap.getIfDescr(), snap.getIfType());
                        String formatedMac = MacUtils.convertMacToDisplayFormat(snap.getCmcMac(), macRule);
                        snap.setCmcMac(formatedMac);
                        snap.setDisplayName(
                                entityTypeService.getEntityType(Long.valueOf(snap.getCmcType())).getDisplayName());
                        if (entityTypeService.isCmts(typeId)) {
                            snap.setDisplayPortName(cmtsPortName);
                        } else {
                            snap.setDisplayPortName(cmcPortName);
                        }
                        if (snap.getDt() == null) {
                            snap.setRecentUpdateTime("--");
                        } else {
                            snap.setRecentUpdateTime(DateUtils.getTimeDesInObscure(
                                    System.currentTimeMillis() - snap.getDt().getTime(), uc.getUser().getLanguage()));
                        }
                    }
                }
            }
            JSONArray jsonArray = JSONArray.fromObject(cmcChnlUsersSnaps);
            jsonArray.write(response.getWriter());
        } catch (Exception ex) {
            logger.debug("Get cmts DownChnUsers Top10 failed", ex);
        }
        return NONE;
    }

    /**
     * CCMTS设备误码率
     * 
     * @return String
     */
    public String getTopPortletErrorCodesLoading() {
        UserContext uc = (UserContext) ServletActionContext.getRequest().getSession().getAttribute("UserContext");
        String macRule = uc.getMacDisplayStyle();
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("start", 0);
        paramMap.put("limit", uc.getTopNumber());
        try {
            List<UsBitErrorRate> usBitErrorRates = cmcPerfService.getTopPortletErrorCodesLoading(paramMap);
            if (usBitErrorRates != null) {
                for (UsBitErrorRate usBitErrorRate : usBitErrorRates) {
                    if (usBitErrorRate.getCmcType() != null) {
                        String formatedMac = MacUtils.convertMacToDisplayFormat(usBitErrorRate.getMacAddress(),
                                macRule);
                        usBitErrorRate.setMacAddress(formatedMac);
                        // 数据库查询中有cmcPortName
                        String cmcPortName = makePortName(usBitErrorRate.getChannelIndex(),
                                new Long(usBitErrorRate.getCmcType()), usBitErrorRate.getIfDescr(),
                                usBitErrorRate.getIfType());
                        usBitErrorRate.setCmcPortName(cmcPortName);
                        usBitErrorRate.setDisplayName(entityTypeService
                                .getEntityType(Long.valueOf(usBitErrorRate.getCmcType())).getDisplayName());

                        if (usBitErrorRate.getDt() == null) {
                            usBitErrorRate.setRecentUpdateTime("--");
                        } else {
                            usBitErrorRate.setRecentUpdateTime(DateUtils.getTimeDesInObscure(
                                    System.currentTimeMillis() - usBitErrorRate.getDt().getTime(),
                                    uc.getUser().getLanguage()));
                        }

                    }
                }
            }
            JSONArray jsonArray = JSONArray.fromObject(usBitErrorRates);
            jsonArray.write(response.getWriter());
        } catch (Exception ex) {
            logger.debug("Get PortletErrorCodes Top10 failed", ex);
        }
        return NONE;
    }

    /**
     * CCMTS上CM flap的ins异常次数
     * 
     * @return
     */
    public String getTopPortletFlapInsGrowthLoading() {
        UserContext uc = (UserContext) ServletActionContext.getRequest().getSession().getAttribute("UserContext");
        String macRule = uc.getMacDisplayStyle();
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("start", 0);
        paramMap.put("limit", uc.getTopNumber());
        try {
            List<CmFlap> cmflapList = cmcPerfService.getTopPortletFlapInsGrowthLoading(paramMap);
            if (cmflapList != null) {
                for (CmFlap cmFlap : cmflapList) {
                    if (cmFlap.getCmcType() != null) {
                        String formatedMac = MacUtils.convertMacToDisplayFormat(cmFlap.getTopCmFlapMacAddr(), macRule);
                        cmFlap.setTopCmFlapMacAddr(formatedMac);
                        cmFlap.setDisplayName(
                                entityTypeService.getEntityType(Long.valueOf(cmFlap.getCmcType())).getDisplayName());
                        cmFlap.setRecentUpdateTime(DateUtils.getTimeDesInObscure(
                                System.currentTimeMillis() - cmFlap.getDt().getTime(), uc.getUser().getLanguage()));
                    }
                }
            }
            JSONArray jsonArray = JSONArray.fromObject(cmflapList);
            jsonArray.write(response.getWriter());
        } catch (Exception ex) {
            logger.debug("Get PortletFlapInsGrowth Top10 failed", ex);
        }
        return NONE;
    }

    /**
     * PON端口速率排行
     * 
     * @return
     * @throws Exception
     */
    public String getTopPonLoading() throws Exception {
        UserContext uc = (UserContext) ServletActionContext.getRequest().getSession().getAttribute("UserContext");
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("start", 0);
        paramMap.put("limit", uc.getTopNumber());
        try {
            List<PerfRecord> perfRecords = oltPerfService.getTopPonLoading(paramMap);
            // NumberUtils.getBandWidth方式转换流量时单位存在问题，此方法是转换成速率，修改为NumberUtils.getByteLength方式
            // 修改人：lizongtian
            for (PerfRecord perfRecord : perfRecords) {
                if (perfRecord.getValue() < 0) {
                    perfRecord.setInSpeed(NumberUtils.getIfSpeedStr(0));
                } else {
                    perfRecord.setInSpeed(NumberUtils.getIfSpeedStr(perfRecord.getValue()));
                }
                if (perfRecord.getTempValue() < 0) {
                    perfRecord.setOutSpeed(NumberUtils.getIfSpeedStr(0));
                } else {
                    perfRecord.setOutSpeed(NumberUtils.getIfSpeedStr(perfRecord.getTempValue()));
                }
                if (perfRecord.getCollectTime() == null) {
                    perfRecord.setRecentUpdateTime("--");
                } else {
                    perfRecord.setRecentUpdateTime(DateUtils.getTimeDesInObscure(
                            System.currentTimeMillis() - perfRecord.getCollectTime().getTime(),
                            uc.getUser().getLanguage()));
                }
            }
            JSONArray jsonArray = JSONArray.fromObject(perfRecords);
            jsonArray.write(response.getWriter());
        } catch (Exception e) {
            logger.error("Get PonRate Top10 failed", e);
        }
        return NONE;
    }

    /**
     * 网络设备响应延迟;
     * 
     * @return
     * @throws Exception
     */
    public String getDeviceDelayingTopLoading() throws Exception {
        UserContext uc = (UserContext) ServletActionContext.getRequest().getSession().getAttribute("UserContext");
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("start", 0);
        paramMap.put("limit", uc.getTopNumber());
        String macRule = uc.getMacDisplayStyle();
        try {
            List<EntitySnap> responses = networkService.getDeviceDelayTop(paramMap);
            if (responses != null) {
                for (EntitySnap snap : responses) {
                    if (snap.getMac() != null) {
                        String mac = MacUtils.convertMacToDisplayFormat(snap.getMac(), macRule);
                        snap.setMac(mac);
                    }
                    if (snap.getSnapTime() == null) {
                        snap.setRecentUpdateTime("--");
                    } else {
                        snap.setRecentUpdateTime(DateUtils.getTimeDesInObscure(
                                System.currentTimeMillis() - snap.getSnapTime().getTime(), uc.getUser().getLanguage()));
                    }
                }
            }
            JSONArray jsonArray = JSONArray.fromObject(responses);
            jsonArray.write(response.getWriter());
        } catch (Exception e) {
            logger.error("Get DeviceDelaying Top10 failed", e);
        }
        return NONE;
    }

    /**
     * 网络设备响应超时
     * 
     * @return
     * @throws Exception
     */
    public String getDeviceDelayingOutLoading() throws Exception {
        UserContext uc = (UserContext) ServletActionContext.getRequest().getSession().getAttribute("UserContext");
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("start", 0);
        paramMap.put("limit", uc.getTopNumber());
        String macRule = uc.getMacDisplayStyle();
        try {
            List<EntitySnap> responses = networkService.getDeviceDelayOut(paramMap);
            if (responses != null) {
                for (EntitySnap snap : responses) {
                    if (snap.getMac() != null) {
                        String mac = MacUtils.convertMacToDisplayFormat(snap.getMac(), macRule);
                        snap.setMac(mac);
                    }
                    Alert alert = alertService.getEntityAlertByType(snap.getEntityId(), ICMP_TYPEID);
                    if (alert != null) {
                        snap.setDelayOutTime(DateUtils.getTimeDesInObscure(
                                System.currentTimeMillis() - alert.getFirstTime().getTime(),
                                uc.getUser().getLanguage()));
                        snap.setDelayUpdateTime(DateUtils.getTimeDesInObscure(
                                System.currentTimeMillis() - alert.getLastTime().getTime(),
                                uc.getUser().getLanguage()));
                    }
                }
            }
            JSONArray jsonArray = JSONArray.fromObject(responses);
            jsonArray.write(response.getWriter());
        } catch (Exception e) {
            logger.error("Get DeviceDelayingOut Top10 failed", e);
        }
        return NONE;
    }

    /**
     * CPU负载
     * 
     * @return
     * @throws Exception
     */
    public String getTopCpuLoading() throws Exception {
        UserContext uc = (UserContext) ServletActionContext.getRequest().getSession().getAttribute("UserContext");
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("start", 0);
        paramMap.put("limit", uc.getTopNumber());
        paramMap.put("target", "cpu");
        String macRule = uc.getMacDisplayStyle();
        try {
            List<EntitySnap> snaps = networkService.getNetworkDeviceLoadingTop(paramMap);
            if (snaps != null) {
                for (EntitySnap snap : snaps) {
                    if (snap.getMac() != null) {
                        String mac = MacUtils.convertMacToDisplayFormat(snap.getMac(), macRule);
                        snap.setMac(mac);
                    }
                    snap.setCpuStr(NumberUtils.getPercentStr(snap.getCpu()));
                    if (snap.getSnapTime() == null) {
                        snap.setRecentUpdateTime("--");
                    } else {
                        snap.setRecentUpdateTime(DateUtils.getTimeDesInObscure(
                                System.currentTimeMillis() - snap.getSnapTime().getTime(), uc.getUser().getLanguage()));
                    }
                }
            }
            JSONArray jsonArray = JSONArray.fromObject(snaps);
            jsonArray.write(response.getWriter());
        } catch (Exception e) {
            logger.error("Get Cpu Top10 failed", e);
        }
        return NONE;
    }

    /**
     * MEM负载
     * 
     * @return
     * @throws Exception
     */
    public String getTopMemLoading() throws Exception {
        UserContext uc = (UserContext) ServletActionContext.getRequest().getSession().getAttribute("UserContext");
        String macRule = uc.getMacDisplayStyle();
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("start", 0);
        paramMap.put("limit", uc.getTopNumber());
        paramMap.put("target", "mem");
        try {
            List<EntitySnap> snaps = networkService.getNetworkDeviceLoadingTop(paramMap);
            if (snaps != null) {
                for (EntitySnap snap : snaps) {
                    // Mac
                    if (snap.getMac() != null) {
                        String mac = MacUtils.convertMacToDisplayFormat(snap.getMac(), macRule);
                        snap.setMac(mac);
                    }
                    snap.setMemStr((NumberUtils.getPercentStr(snap.getMem())));
                    if (snap.getSnapTime() == null) {
                        snap.setRecentUpdateTime("--");
                    } else {
                        snap.setRecentUpdateTime(DateUtils.getTimeDesInObscure(
                                System.currentTimeMillis() - snap.getSnapTime().getTime(), uc.getUser().getLanguage()));
                    }
                }
            }
            JSONArray jsonArray = JSONArray.fromObject(snaps);
            jsonArray.write(response.getWriter());
        } catch (Exception e) {
            logger.error("Get Mem Top10 failed", e);
        }
        return NONE;
    }

    /**
     * 组装channelName
     * 
     * @param channelIndex
     *            Long
     * @return String
     */
    private String makePortName(Long channelIndex, Long typeId, String ifDescr, Long ifType) {
        if (entityTypeService.isCmts(typeId)) {
            int channelType = CmcUtil.getCmtsChannelType(typeId, ifType, entityTypeService);
            if (channelType > 0) {
                return CmcUtil.getCmtsDownChannelIndex(typeId, ifDescr, entityTypeService);
            } else {
                return CmcUtil.getCmtsUpChannelIndex(typeId, ifDescr, entityTypeService);
            }
        } else {
            String type = CmcIndexUtils.getChannelType(channelIndex) == 0 ? "US" : "DS";
            Long slotNo = CmcIndexUtils.getSlotNo(channelIndex);
            Long ponNo = CmcIndexUtils.getPonNo(channelIndex);
            Long cmcNo = CmcIndexUtils.getCmcId(channelIndex);
            Long chNo = CmcIndexUtils.getChannelId(channelIndex);
            StringBuilder sb = new StringBuilder();
            sb.append(type).append(slotNo).append(Symbol.SLASH).append(ponNo).append(Symbol.SLASH).append(cmcNo)
                    .append(Symbol.SLASH).append(chNo);
            return sb.toString();
        }

    }

}
