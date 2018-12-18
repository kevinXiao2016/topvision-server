package com.topvision.ems.mobile.action;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Date;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.util.Base64Utils;

import com.topvision.ems.cm.pnmp.facade.domain.PnmpCmData;
import com.topvision.ems.cm.pnmp.facade.domain.PnmpTargetThreshold;
import com.topvision.ems.cm.pnmp.service.PnmpCmDataService;
import com.topvision.ems.cmc.ccmts.service.CmcService;
import com.topvision.ems.cmc.cm.domain.Cm3Signal;
import com.topvision.ems.cmc.cm.domain.RealtimeCpe;
import com.topvision.ems.cmc.cm.service.CmListService;
import com.topvision.ems.cmc.cm.service.CmService;
import com.topvision.ems.cmc.cm.service.CmSignalService;
import com.topvision.ems.cmc.cpe.service.CpeService;
import com.topvision.ems.cmc.domain.CmImportInfo;
import com.topvision.ems.cmc.facade.domain.CmAttribute;
import com.topvision.ems.cmc.facade.domain.CmCpe;
import com.topvision.ems.cmc.flap.service.CmcFlapService;
import com.topvision.ems.cmc.performance.domain.CMFlapHis;
import com.topvision.ems.cmc.performance.facade.CmFlap;
import com.topvision.ems.devicesupport.version.service.DeviceVersionService;
import com.topvision.ems.mobile.domain.Cm;
import com.topvision.ems.mobile.domain.CmInCmList;
import com.topvision.ems.mobile.domain.CmRelative;
import com.topvision.ems.mobile.domain.CmtsCm;
import com.topvision.ems.mobile.domain.MtrSnrOverlap;
import com.topvision.ems.mobile.service.MCmService;
import com.topvision.ems.performance.utils.PerfTargetUtil;
import com.topvision.framework.common.MacUtils;
import com.topvision.framework.web.struts2.BaseAction;
import com.topvision.platform.domain.UserContext;
import com.topvision.platform.util.StringUtil;
import com.topvision.platform.util.highcharts.domain.Point;
import com.topvision.platform.SystemConstants;
import com.topvision.platform.zetaframework.var.UnitConfigConstant;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Controller("mCmAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class MCmAction extends BaseAction {
    private static final long serialVersionUID = -4293143253615628247L;
    private final Integer ALIASSORT = 1;
    private final Integer STATESORT = 2;
    private final String ALIASSORTSTR = "alias";
    private final String STATESORTSTR = "state";
    protected Logger logger = LoggerFactory.getLogger(getClass());
    @Resource(name = "mCmService")
    private MCmService mCmService;
    @Resource(name = "cmService")
    private CmService cmService;
    @Resource(name = "cmcService")
    private CmcService cmcService;
    @Resource(name = "cpeService")
    private CpeService cpeService;
    @Resource(name = "cmSignalService")
    private CmSignalService cmSignalService;
    @Resource(name = "deviceVersionService")
    private DeviceVersionService deviceVersionService;
    @Autowired
    private CmcFlapService cmcFlapService;
    @Autowired
    private PnmpCmDataService pnmpCmDataService;
    @Resource(name = "cmListService")
    private CmListService cmListService;
    private Long cmtsId;
    private Long cmId;
    private String cmMacOrIp;
    private String cmMac;
    private Integer sortType;
    private String screeningstr;
    private String sortUpDown;
    private Integer correlationGroup;
    private Long cmChannelId;

    private String imageBase64;

    private final static DecimalFormat df = new DecimalFormat("0.00");

    public String getUpChannelSignalByCmId() {
        cmSignalService.refreshSignalWithSave(cmId);
        List<Cm3Signal> upChannels = cmService.getUpChannelSignalByCmId(cmId);
        for (Cm3Signal cm3Signal : upChannels) {
            String spectrumUnit = (String) UnitConfigConstant.get("elecLevelUnit");
            cm3Signal.setUpChannelFrequency(Double.parseDouble(cm3Signal.getUpChannelFrequency()) / 1000000 + " MHz");
            cm3Signal.setUpChannelSnr(cm3Signal.getUpChannelSnr() + " dB");
            cm3Signal.setUpChannelTx(UnitConfigConstant.parsePowerValue(Double.parseDouble(cm3Signal.getUpChannelTx()))
                    + " " + spectrumUnit);
        }
        writeDataToAjax(upChannels);
        return NONE;
    }

    public String getDownChannelSignalByCmId() {
        cmSignalService.refreshSignalWithSave(cmId);
        List<Cm3Signal> downChannels = cmService.getDownChannelSignalByCmId(cmId);
        for (Cm3Signal cm3Signal : downChannels) {
            String spectrumUnit = (String) UnitConfigConstant.get("elecLevelUnit");
            cm3Signal.setDownChannelFrequency(
                    Double.parseDouble(cm3Signal.getDownChannelFrequency()) / 1000000 + " MHz");
            cm3Signal.setDownChannelSnr(cm3Signal.getDownChannelSnr() + " dB");
            cm3Signal.setDownChannelTx(
                    UnitConfigConstant.parsePowerValue(Double.parseDouble(cm3Signal.getDownChannelTx())) + " "
                            + spectrumUnit);
        }
        writeDataToAjax(downChannels);
        return NONE;
    }

    public String getPowerUnit() throws IOException {
        String powerUnit = (String) UnitConfigConstant.get(UnitConfigConstant.POWER_UNIT);
        JSONObject json = new JSONObject();
        json.put("powerUnit", powerUnit);
        json.write(response.getWriter());
        return NONE;
    }

    public String getCmList() throws IOException {
        Map<String, Object> map = new HashMap<String, Object>();
        if (cmMacOrIp != null && !"".equals(cmMacOrIp.trim())) {
            // mysql中下划线是特殊的，like的时候必须转义
            if (cmMacOrIp.contains("_")) {
                cmMacOrIp = cmMacOrIp.replace("_", "\\_");
            }
            map.put("cmMacOrIp", cmMacOrIp.trim());
            String formatQueryMac = MacUtils.formatQueryMac(cmMacOrIp);
            if (formatQueryMac.indexOf(":") == -1) {
                map.put("queryMacWithoutSplit", formatQueryMac);
            }
            map.put("queryContentMac", formatQueryMac);
        }
        /*
         * if (cmMacOrIp != null) { map.put("cmMacOrIp", MobileUtil.convertQueryContext(cmMacOrIp));
         * } String formatQueryMac =
         * MacUtils.formatQueryMac(MobileUtil.convertQueryContext(cmMacOrIp)); if
         * (formatQueryMac.indexOf(":") == -1) { map.put("queryMacWithoutSplit", formatQueryMac); }
         * map.put("queryContentMac", formatQueryMac);
         */
        map.put("start", start);
        map.put("limit", limit);
        List<CmInCmList> list = mCmService.getCmList(map);
        Long totalCount = mCmService.getCmListCount(map);
        JSONObject json = new JSONObject();
        json.put("totalCount", totalCount);
        json.put("data", list);
        json.write(response.getWriter());
        return NONE;
    }

    public String getCmListWithRegion() throws IOException {
        Map<String, Object> map = new HashMap<String, Object>();
        if (cmMacOrIp != null && !"".equals(cmMacOrIp.trim())) {
            // mysql中下划线是特殊的，like的时候必须转义
            if (cmMacOrIp.contains("_")) {
                cmMacOrIp = cmMacOrIp.replace("_", "\\_");
            }
            map.put("cmMacOrIp", cmMacOrIp.trim());
            String formatQueryMac = MacUtils.formatQueryMac(cmMacOrIp);
            if (formatQueryMac.indexOf(":") == -1) {
                map.put("queryMacWithoutSplit", formatQueryMac);
            }
            map.put("queryContentMac", formatQueryMac);
        }

        /*
         * if (cmMacOrIp != null) { map.put("cmMacOrIp", MobileUtil.convertQueryContext(cmMacOrIp));
         * } String formatQueryMac =
         * MacUtils.formatQueryMac(MobileUtil.convertQueryContext(cmMacOrIp)); if
         * (formatQueryMac.indexOf(":") == -1) { map.put("queryMacWithoutSplit", formatQueryMac); }
         * map.put("queryContentMac", formatQueryMac);
         */
        map.put("start", start);
        map.put("limit", limit);
        List<CmInCmList> list = mCmService.getCmListWithRegion(map);
        Long totalCount = mCmService.getCmListCountWithRegion(map);
        JSONObject json = new JSONObject();
        json.put("totalCount", totalCount);
        json.put("data", list);
        json.write(response.getWriter());
        return NONE;
    }

    /**
     * 从设备获取
     * 
     * @return
     * @throws IOException
     */
    public String getCpeListByCmId() throws IOException {
        List<RealtimeCpe> cpeList = cpeService.getRealCpeListByCmId(cmId);
        JSONObject jsonObj = new JSONObject();
        jsonObj.put("cpeTotalNum", cpeList.size());
        JSONArray jsonArray = JSONArray.fromObject(cpeList);
        jsonObj.put("data", jsonArray);
        jsonObj.write(response.getWriter());
        return NONE;
    }

    /**
     * 从数据库查询
     * 
     * @return
     * @throws IOException
     */
    public String getCpeListByCmIdFromDb() throws IOException {
        List<CmCpe> cpeList = cpeService.getCpeListByCmId(cmId);
        JSONObject jsonObj = new JSONObject();
        jsonObj.put("cpeTotalNum", cpeList.size());
        JSONArray jsonArray = JSONArray.fromObject(cpeList);
        jsonObj.put("data", jsonArray);
        jsonObj.write(response.getWriter());
        return NONE;
    }

    /**
     * 通过Cmc的Id查询Cm列表
     * 
     * @return
     * @throws IOException
     */
    public String getCmListByCmcId() throws IOException {
        if (start == 0) {
            cmService.getRealtimeCmAttributeByCmcId(cmtsId);
        }
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("cmtsId", cmtsId);
        map.put("sortType", getSortTypeString(sortType));
        if (screeningstr != null && !screeningstr.equalsIgnoreCase("")) {
            String formatQueryMac = MacUtils.formatQueryMac(screeningstr);
            if (formatQueryMac.indexOf(":") == -1) {
                map.put("queryMacWithoutSplit", formatQueryMac);
            } else {
                map.put("screeningstr", formatQueryMac);
            }
        }
        map.put("start", start);
        map.put("limit", limit);
        List<CmtsCm> cmList = mCmService.getCmListByCmtsId(map);
        for (CmtsCm cm : cmList) {
            mCmService.refreshCm(cm.getCmId());
        }
        cmList = mCmService.getCmListByCmtsId(map);
        cmList = mCmService.getRealtimeData(cmList, cmtsId);
        Long onlineCount = mCmService.getOnlineCmListSizeByCmtsId(map);
        Long totalCount = mCmService.getCmListSizeByCmtsId(map);
        boolean isSupportRealtimeCpeQuery = deviceVersionService.isFunctionSupported(cmtsId, "realtimecpe");
        JSONObject json = new JSONObject();
        json.put("onlineCount", onlineCount);
        json.put("totalCount", totalCount);
        json.put("data", cmList);
        json.put("isCpeShow", isSupportRealtimeCpeQuery);
        json.write(response.getWriter());
        return NONE;
    }

    /**
     * 查询CMTS下CM列表(只从数据库查询)
     * 
     * @return
     * @throws Exception
     */
    public String getCmtsCmList() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("cmtsId", cmtsId);
        map.put("sortType", getSortTypeString(sortType));
        map.put("dir", sortUpDown);
        if (cmMacOrIp != null && !"".equals(cmMacOrIp)) {
            // mysql中下划线是特殊的，like的时候必须转义
            if (cmMacOrIp.contains("_")) {
                cmMacOrIp = cmMacOrIp.replace("_", "\\_");
            }
            map.put("cmMacOrIp", cmMacOrIp);
        }
        map.put("start", start);
        map.put("limit", limit);
        List<CmtsCm> cmList = mCmService.getCmtsCmList(map);
        Integer onlineCount = mCmService.getCmtsCmOnlineCount(map);
        Integer totalCount = mCmService.getCmtsCmTotalCount(map);
        boolean isSupportRealtimeCpeQuery = deviceVersionService.isFunctionSupported(cmtsId, "realtimecpe");
        JSONObject json = new JSONObject();
        json.put("onlineCount", onlineCount);
        json.put("totalCount", totalCount);
        json.put("data", cmList);
        json.put("isCpeShow", isSupportRealtimeCpeQuery);
        json.write(response.getWriter());
        return NONE;
    }

    /**
     * 刷新CMTS下CM列表(增加从CMTS上获取最新CM数据的步骤)
     * 
     * @return
     * @throws Exception
     */
    public String refreshCmtsCmList() throws Exception {
        // 获取CMTS下所有cm实时基本信息
        try {
            cmService.getRealtimeCmAttributeByCmcId(cmtsId);
            logger.debug("Refresh CMTS CM SUCCESS");
        } catch (Exception e) {
            logger.error("Refresh CMTS CM data failed: {}", e);
        }
        // 查询最新的CMTS下CM列表
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("cmtsId", cmtsId);
        map.put("sortType", getSortTypeString(sortType));
        if (cmMacOrIp != null && !"".equals(cmMacOrIp)) {
            map.put("cmMacOrIp", cmMacOrIp);
        }
        map.put("start", start);
        map.put("limit", limit);
        List<CmtsCm> cmList = mCmService.getCmtsCmList(map);
        Integer onlineCount = mCmService.getCmtsCmOnlineCount(map);
        Integer totalCount = mCmService.getCmtsCmTotalCount(map);
        boolean isSupportRealtimeCpeQuery = deviceVersionService.isFunctionSupported(cmtsId, "realtimecpe");
        JSONObject json = new JSONObject();
        json.put("onlineCount", onlineCount);
        json.put("totalCount", totalCount);
        json.put("data", cmList);
        json.put("isCpeShow", isSupportRealtimeCpeQuery);
        json.write(response.getWriter());
        return NONE;
    }

    /**
     * 根据CmId查询Cm的信息 主要是信号质量信息
     * 
     * @return
     * @throws IOException
     */
    public String getCmInfoByCmId() throws IOException {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("cmId", cmId);
        Cm cm = mCmService.getCmByCmId(cmId);
        JSONObject json = new JSONObject();
        if (cm != null) {
            cm = processSignalInfo(cm);
            json = JSONObject.fromObject(cm);
        }
        json.write(response.getWriter());
        return NONE;
    }

    /**
     * 在服务器端处理CM信号质量信息
     * 
     * @param signal
     * @return
     */
    private Cm processSignalInfo(Cm signal) {
        if (signal.getUpSnr() != null) {
            signal.setUpChannelSnr(df.format(signal.getUpSnr() / 10) + " dB");
        } else {
            signal.setUpChannelSnr("--");
        }
        String powerUnit = (String) UnitConfigConstant.get(UnitConfigConstant.POWER_UNIT);
        if (signal.getUpRx() != null) {
            Double upRecvPower = UnitConfigConstant.parsePowerValue(signal.getUpRx() / 10);
            signal.setUpRecvPower(df.format(upRecvPower) + " " + powerUnit);
        } else {
            signal.setUpRecvPower("--");
        }
        if (signal.getDownSnr() != null) {
            signal.setDownChannelSnr(df.format(signal.getDownSnr()) + " dB");
        } else {
            signal.setDownChannelSnr("--");
        }
        if (signal.getUpTx() != null) {
            Double upTransPower = UnitConfigConstant.parsePowerValue(signal.getUpTx());
            signal.setUpTransPower(df.format(upTransPower) + " " + powerUnit);
        } else {
            signal.setUpTransPower("--");
        }
        if (signal.getDownRx() != null) {
            Double downRecvPower = UnitConfigConstant.parsePowerValue(signal.getDownRx());
            signal.setDownRecvPower(df.format(downRecvPower) + " " + powerUnit);
        } else {
            signal.setDownRecvPower("--");
        }
        if (signal.getDownTx() != null) {
            Double downTransPower = UnitConfigConstant.parsePowerValue(signal.getDownTx() / 10);
            signal.setDownTransPower(df.format(downTransPower) + " " + powerUnit);
        } else {
            signal.setDownTransPower("--");
        }
        if (signal.getUpTx() != null && signal.getUpRx() != null) {
            Double upAtten = UnitConfigConstant.parsePowerValue(signal.getUpTx() - signal.getUpRx() / 10);
            signal.setUpAtten(df.format(upAtten) + " dB");
        } else {
            signal.setUpAtten("--");
        }
        if (signal.getDownTx() != null && signal.getDownRx() != null) {
            Double downAtten = UnitConfigConstant.parsePowerValue(signal.getDownTx() / 10 - signal.getDownRx());
            signal.setDownAtten(df.format(downAtten) + " dB");
        } else {
            signal.setDownAtten("--");
        }
        return signal;
    }

    /**
     * 刷新CM信息，使用remoteQuery方式
     * 
     * @return
     * @throws IOException
     */
    public String refreshCm() throws IOException {
        mCmService.refreshCm(cmId);
        return NONE;
    }

    /**
     * 刷新CM信号质量，并且获得数据
     * 
     * @return
     * @throws IOException
     */
    public String refreshAndGetCmSignal() throws IOException {
        try {
            mCmService.refreshCm(cmId);
        } catch (Exception e) {
        }
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("cmId", cmId);
        Cm cm = mCmService.getCmByCmId(cmId);
        JSONObject json = new JSONObject();
        if (cm != null) {
            cm = processSignalInfo(cm);
            json = JSONObject.fromObject(cm);
        }
        json.write(response.getWriter());
        return NONE;
    }

    /**
     * 获取CM基本信息
     * 
     * @return
     * @throws Exception
     */
    public String getCmBaseInfo() throws Exception {
        CmtsCm baseInfo = mCmService.getCmBaseInfo(cmId);
        JSONObject json = new JSONObject();
        if (baseInfo != null) {
            json = JSONObject.fromObject(baseInfo);
            json.put("deviceExist", true);
        } else {
            json.put("deviceExist", false);
        }
        json.write(response.getWriter());
        return NONE;
    }

    public String getCmFlapByCmId() throws IOException {
        CmFlap cmFlap = cmcFlapService.queryCmFlapInfo(cmId);
        JSONObject json = new JSONObject();
        if (cmFlap != null) {
            json = JSONObject.fromObject(cmFlap);
        }
        json.write(response.getWriter());
        return NONE;
    }

    public String getOneCMFlapHisData() {
        Calendar cur = Calendar.getInstance();
        Date endDate = new Date(cur.getTimeInMillis());
        cur.add(Calendar.DATE, -7);
        Date startDate = new Date(cur.getTimeInMillis());
        JSONArray insReal = new JSONArray();
        JSONArray insFail = new JSONArray();
        JSONArray range = new JSONArray();
        JSONArray power = new JSONArray();
        List<CMFlapHis> allFlap = cmcFlapService.queryOneCMFlapHis(MacUtils.convertToMaohaoFormat(cmMac),
                startDate.getTime(), endDate.getTime());
        Integer lastInsIns = 0;
        long lastFlapTime = 0L;
        for (int i = 0; i < allFlap.size(); i++) {
            CMFlapHis flap = allFlap.get(i);
            long time = flap.getCollectTime().getTime();
            if (time == lastFlapTime) { // 如果上一条数据与这一条采集相同，则直接跳过
                continue;
            }
            lastFlapTime = time;
            // 正常ins数统计
            Integer ins = flap.getInsFailNum();
            if (ins != null) {
                JSONArray pointArray = new JSONArray();
                pointArray.add(time);
                pointArray.add(ins);
                insReal.add(pointArray);
            }
            // 异常上线增长次数
            Integer insInc = 0;
            if (i != 0) {
                insInc = ins - lastInsIns;
            }
            lastInsIns = ins;
            JSONArray insPointArray = new JSONArray();
            insPointArray.add(time);
            insPointArray.add(insInc);
            insFail.add(insPointArray);
            // 电平调整增长次数
            Integer powAdj = flap.getIncreasePowerAdjNum();
            JSONArray powerPointArray = new JSONArray();
            powerPointArray.add(time);
            powerPointArray.add(powAdj);
            power.add(powerPointArray);
            // 测距命中率
            Float f = flap.getIncreaseHitPercent();
            JSONArray hitPointArray = new JSONArray();
            hitPointArray.add(time);
            hitPointArray.add(f);
            range.add(hitPointArray);
        }
        JSONObject allData = new JSONObject();
        allData.put("insRealNum", insReal);
        allData.put("insFailNum", insFail);
        allData.put("rangePercent", range);
        allData.put("poweAdjNum", power);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        allData.put("startTime", dateFormat.format(startDate));
        allData.put("endTime", dateFormat.format(endDate));
        writeDataToAjax(allData);
        return NONE;
    }

    public String loadCmtsDataByGroup() {
        JSONObject json = new JSONObject();
        Map<String, Object> queryMap = new HashMap<String, Object>();
        Long entityId=cmcService.getEntityIdByCmcId(cmtsId);
        queryMap.put("entityId", entityId);
        queryMap.put("cmcId", cmtsId);
        if (!StringUtil.isEmpty(cmMac)) {
            String formatQueryMac = MacUtils.formatQueryMac(cmMac);
            queryMap.put("cmMac", formatQueryMac);
            queryMap.put("deviceId", entityId+cmtsId+formatQueryMac);
        }
        PnmpCmData pnmpCmData = pnmpCmDataService.getDataByGroupForMobile(queryMap);
        List<Integer>upChannelList=mCmService.getUpchannelList(cmtsId);
        List<PnmpTargetThreshold> mtrThresholdList = mCmService.getMtrThresholds();
        json.put("data", pnmpCmData);
        json.put("upChannelList", upChannelList);
        json.put("mtrThresholdList", mtrThresholdList);
//        if (CmAttribute.isCmOnline(pnmpCmData.getStatusValue().intValue())) {
//            json.put("data", pnmpCmData);
//        }else{
//            json.put("data", null);
//        }
        writeDataToAjax(json);
        return NONE;
    }
    
    public String getUpchannelList(){
        JSONObject json = new JSONObject();
        List<Integer>upChannelList=mCmService.getUpchannelList(cmtsId);
        json.put("upChannelList", upChannelList);
        writeDataToAjax(json);
        return NONE;
    }

    /**
     * 实时读取pnmp数据
     *
     * @return String
     */
    public String realPnmp() {
        JSONObject json = new JSONObject();
        PnmpCmData pnmpCmData = mCmService.realPnmp(cmtsId,cmMac);
        CmImportInfo cmImportInfo= mCmService.getCmBossInfo(cmMac);
        if(cmImportInfo!=null&&cmImportInfo.getCmAlias()!=null){
            pnmpCmData.setCmAddress(cmImportInfo.getCmAlias());
        }
        if(cmImportInfo!=null&&cmImportInfo.getCmPhoneNo()!=null){
            pnmpCmData.setCmUserPhoneNo(cmImportInfo.getCmPhoneNo());
        }
        List<PnmpTargetThreshold> mtrThresholdList = mCmService.getMtrThresholds();
        json.put("data", pnmpCmData);
        json.put("mtrThresholdList", mtrThresholdList);
        writeDataToAjax(json);
        return NONE;
    }

    /**
     * ping docsis实时返回结果
     *
     * @return String
     */
    public String pingDocsis() {
        JSONObject json = new JSONObject();
        String result = "% ";
        try {
            result = mCmService.pingDocsis(cmtsId,cmMac);
        } catch (Exception e) {
            logger.error("PING CmDOCSIS error", e);
            result = "% PING DOCSIS error" + e.getMessage();
        }
        json.put("result", result);
        writeDataToAjax(json);
        return NONE;
    }

    /**
     * CM信道迁移，返回迁移是否成功
     *
     * @return String
     */
    public String moveCmChannel() {
        JSONObject json = new JSONObject();
        String re = "%";
        try {
            re = mCmService.moveCmChannel(cmtsId, cmChannelId,cmMac);
        } catch (Exception e) {
            logger.error("moveCmChannel error", e);
            re = "% moveCmChannel error " + e.getMessage();
        }
        json.put("result", !re.startsWith("%")&&!re.contains("fail"));
        writeDataToAjax(json);
        return NONE;
    }

    /**
     * 上传pnmp CM的工程图
     * 
     * @return
     */
    public String uploadProgramImg() {
        String type = "data:image/png;base64,";// 前台是以png的格式存储的
        imageBase64 = imageBase64.substring(type.length());
        String imageUrl = File.separator + "images" + File.separator + "pnmp" + File.separator
                + cmMac.replaceAll(":", "") + ".png";
        String realPath = SystemConstants.ROOT_REAL_PATH + imageUrl;

        JSONObject json = new JSONObject();
        try {
            this.uploadFileByBase64(realPath, imageBase64);
            Cm cm = new Cm();
            cm.setCmMac(cmMac);
            cm.setCmImgUrl(imageUrl);
            mCmService.updateOrInsertImg(cm);
            json.put("result", "success");
            json.put("message", imageUrl);
        } catch (Exception e) {
            json.put("result", "error");
            e.printStackTrace();
        }

        writeDataToAjax(json);
        return NONE;
    }

    public void uploadFileByBase64(String destRealPath, String imageBase64) {
        byte[] b = Base64Utils.decodeFromString(imageBase64);
        OutputStream out = null;
        try {
            File destFile = new File(destRealPath);
            if(!destFile.getParentFile().exists()){
                destFile.getParentFile().mkdirs();
            }
            out = new FileOutputStream(destFile);
            out.write(b);
        } catch (FileNotFoundException e) {
            logger.error("", e);
        } catch (IOException e) {
            logger.error("", e);
        } finally {
            if (out != null) {
                try {
                    out.flush();
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public String getCmProgramImg() {
        String url = mCmService.getCmProgramImg(cmMac);
        JSONObject json = new JSONObject();

        if (url == null) {
            url = "";
        }
        json.put("url", url);
        writeDataToAjax(json);
        return NONE;
    }
    
    public String loadMtrSnrGraph(){
        Calendar cur = Calendar.getInstance();
        Date endDate = new Date(cur.getTimeInMillis());
        cur.add(Calendar.DATE, -3);
        Date startDate = new Date(cur.getTimeInMillis());
        List<MtrSnrOverlap>points=mCmService.getMtrSnrGraph(cmMac,startDate.getTime(),endDate.getTime());
        JSONArray mtrArray = new JSONArray();
        JSONArray snrArray = new JSONArray();
        for (MtrSnrOverlap p : points) {
            JSONArray mtrPoint = new JSONArray();
            mtrPoint.add(p.getxTime().getTime());
            mtrPoint.add(p.getMtr());
            mtrArray.add(mtrPoint);
            
            JSONArray snrPoint = new JSONArray();
            snrPoint.add(p.getxTime().getTime());
            snrPoint.add(p.getUpSnr());
            snrArray.add(snrPoint);
        }
        JSONObject json = new JSONObject();
        json.put("mtr", mtrArray);
        json.put("snr", snrArray);
        writeDataToAjax(json);
        return NONE;
    }
    
    public String loadRelativeCm(){
        JSONObject json = new JSONObject();
        Map<String, Object> queryMap = new HashMap<String, Object>();
        queryMap.put("cmcId", cmtsId);
        queryMap.put("cmId", cmId);
        List<CmRelative> cmRelative = mCmService.getRelavtiveCm(queryMap);
        List<PnmpTargetThreshold> mtrThresholdList = mCmService.getMtrThresholds();
        json.put("data", cmRelative);
        json.put("threshold", mtrThresholdList);
        writeDataToAjax(json);
        return NONE;
    }
    
    public String refreshCmAfterMove() throws IOException{
        JSONObject json= new JSONObject();
        UserContext uc = (UserContext) super.getSession().get(UserContext.KEY);
        cmMac = MacUtils.convertToMaohaoFormat(cmMac);
        List<CmAttribute>caList=cmService.getCmAttributeByCmcId(cmtsId);
        for(CmAttribute ca:caList){
            if(cmMac.equals(ca.getStatusMacAddress())){
                json = cmListService.refreshCm(cmtsId, ca.getCmId(), ca.getStatusIndex(), cmMac, ca.getCmcDeviceStyle(), uc);
                break;
            }
        }
        json.write(response.getWriter());
        return NONE;
    }
    
    public Long getCmtsId() {
        return cmtsId;
    }

    public void setCmtsId(Long cmtsId) {
        this.cmtsId = cmtsId;
    }

    public Long getCmId() {
        return cmId;
    }

    public void setCmId(Long cmId) {
        this.cmId = cmId;
    }

    public Integer getSortType() {
        return sortType;
    }

    public void setSortType(Integer sortType) {
        this.sortType = sortType;
    }

    public String getScreeningstr() {
        return screeningstr;
    }

    public void setScreeningstr(String screeningstr) {
        this.screeningstr = screeningstr;
    }

    public String getSortTypeString(Integer sortType) {
        String re = ALIASSORTSTR;
        if (ALIASSORT.equals(sortType)) {
            re = ALIASSORTSTR;
        } else if (STATESORT.equals(sortType)) {
            re = STATESORTSTR;
        }
        return re;
    }

    public String getCmMacOrIp() {
        return cmMacOrIp;
    }

    public void setCmMacOrIp(String cmMacOrIp) {
        this.cmMacOrIp = cmMacOrIp;
    }

    public String getCmMac() {
        return cmMac;
    }

    public void setCmMac(String cmMac) {
        this.cmMac = cmMac;
    }

    public String getSortUpDown() {
        return sortUpDown;
    }

    public void setSortUpDown(String sortUpDown) {
        this.sortUpDown = sortUpDown;
    }

    public String getImageBase64() {
        return imageBase64;
    }

    public void setImageBase64(String imageBase64) {
        this.imageBase64 = imageBase64;
    }

    public Integer getCorrelationGroup() {
        return correlationGroup;
    }

    public void setCorrelationGroup(Integer correlationGroup) {
        this.correlationGroup = correlationGroup;
    }

    public Long getCmChannelId() {
        return cmChannelId;
    }

    public void setCmChannelId(Long cmChannelId) {
        this.cmChannelId = cmChannelId;
    }

}
