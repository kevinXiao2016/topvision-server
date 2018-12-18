package com.topvision.ems.cmc.cm.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.topvision.ems.template.service.EntityTypeService;
import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.topvision.ems.cmc.ccmts.facade.domain.CmcAttribute;
import com.topvision.ems.cmc.ccmts.service.CmcService;
import com.topvision.ems.cmc.cm.service.CcmtsCmListService;
import com.topvision.ems.cmc.cm.service.CmListService;
import com.topvision.ems.cmc.cm.service.CmService;
import com.topvision.framework.constants.Symbol;
import com.topvision.ems.cmc.domain.CmTopologyInfo;
import com.topvision.ems.cmc.downchannel.service.CmcDownChannelService;
import com.topvision.ems.cmc.facade.domain.CmAttribute;
import com.topvision.ems.cmc.facade.domain.CmStaticIp;
import com.topvision.ems.cmc.facade.domain.CmStatus;
import com.topvision.ems.cmc.facade.domain.CpeAttribute;
import com.topvision.ems.cmc.performance.domain.CpeAct;
import com.topvision.ems.cmc.upchannel.service.CmcUpChannelService;
import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.network.service.EntityService;
import com.topvision.framework.common.MacUtils;
import com.topvision.framework.web.struts2.BaseAction;
import com.topvision.platform.domain.OperationLog;
import com.topvision.platform.domain.UserContext;

@Controller("cmListOldAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class CmListOldAction extends BaseAction {
    private static final long serialVersionUID = 1L;
    Logger logger = LoggerFactory.getLogger(this.getClass());
    @Resource(name = "cmListService")
    private CmListService cmListService;
    @Resource(name = "ccmtsCmListService")
    private CcmtsCmListService ccmtsCmListService;
    @Resource(name = "cmService")
    private CmService cmService;
    @Resource(name = "cmcService")
    private CmcService cmcService;
    @Resource(name = "entityService")
    protected EntityService entityService;
    @Autowired
    protected EntityTypeService entityTypeService;
    @Resource(name = "cmcUpChannelService")
    private CmcUpChannelService cmcUpChannelService;
    @Resource(name = "cmcDownChannelService")
    private CmcDownChannelService cmcDownChannelService;
    private boolean hasSupportEpon;
    private Long ponId;
    private Long cmcId;
    private Long ponNo;
    private Long cmcNo;
    private Long slotNo;
    private Long entityId;
    private Long channelId;
    private Long deviceType;
    private Long upChannelId;
    private Long downChannelId;
    private Long channelType;
    private Long channelIndex;
    private Long totalNum;
    private Long deniedNum;
    private Long registeredNum;
    private Long cmId;
    private int start;
    private int limit;
    private String cmIp;
    private String cpeIp;
    private String cpeMac;
    private String realCpeMaxCpe;
    private String cmIdString;
    private String readCommunity;
    private String writeCommunity;
    private CmStatus cmStatus;
    private Entity oltEntity;
    private CmAttribute cmAttribute;
    private CmcAttribute cmcAttribute;
    private CmTopologyInfo cmTopologyInfo;
    private List<CmAttribute> cmAttributes;
    private ArrayList<String> cmsArrayId;// 批量重启的id参数
    protected Integer operationResult;

    /**
     * 所在OLT下的CM
     * 
     * @return String
     */
    public String getCmListByOlt() {
        Map<String, Object> json = new HashMap<String, Object>();
        // 通过cmId查询所在OLT下的CM列表
        List<CmAttribute> cmList = cmService.getCmListByOlt(cmId, start, limit);
        // 通过cmId查询所在OLT下的所有CM列表的数目
        Long size = cmService.getCmListByOltCount(cmId);
        cmTopologyInfo = cmService.getTopologyInfo(cmId);
        Map<String, Object> cmQueryMap = new HashMap<String, Object>();
        if (cmTopologyInfo.getEntityId() != null) {
            cmQueryMap.put("entityId", cmTopologyInfo.getEntityId().toString());
            cmQueryMap.put("status", Integer.toString(6));
            cmQueryMap.put("deviceType", cmTopologyInfo.getDeviceType().toString());
            registeredNum = cmService.getCmNumByStatus(cmQueryMap);
            cmQueryMap.remove("status");
            cmQueryMap.put("status", Integer.toString(7));
            deniedNum = cmService.getCmNumByStatus(cmQueryMap);
            json.put("entityId", cmTopologyInfo.getEntityId().toString());
            json.put("entityIp", cmTopologyInfo.getIp());
            json.put("totalNum", size);
            json.put("registeredNum", registeredNum);
            json.put("deniedNum", deniedNum);
        } else {
            json.put("totalNum", size);
            json.put("registeredNum", 0);
            json.put("deniedNum", 0);
        }
        json.put("rowCount", size);
        json.put("data", cmList);
        writeDataToAjax(JSONObject.fromObject(json));
        return NONE;
    }

    /**
     * 所在PON口下的CM
     * 
     * @return String
     */
    public String getCmListByPon() {
        Map<String, Object> json = new HashMap<String, Object>();
        // 通过cmId查询所在PON口下的CM列表
        List<CmAttribute> cmList = cmService.getCmListByPon(cmId, start, limit);
        // 通过cmId查询所在PON口下的CM列表数目
        Long size = cmService.getCmListByPonCount(cmId);
        cmTopologyInfo = cmService.getTopologyInfo(cmId);
        Map<String, Object> cmQueryMap = new HashMap<String, Object>();
        if (cmTopologyInfo.getPonId() != null) {
            cmQueryMap.put("ponId", cmTopologyInfo.getPonId().toString());
            cmQueryMap.put("status", Integer.toString(6));
            cmQueryMap.put("deviceType", cmTopologyInfo.getDeviceType().toString());
            registeredNum = cmService.getCmNumByStatus(cmQueryMap);
            cmQueryMap.remove("status");
            cmQueryMap.put("status", Integer.toString(7));
            deniedNum = cmService.getCmNumByStatus(cmQueryMap);
            json.put("cmTopologyInfo", cmTopologyInfo);
            json.put("totalNum", size);
            json.put("registeredNum", registeredNum);
            json.put("deniedNum", deniedNum);
        } else {
            json.put("totalNum", size);
            json.put("registeredNum", 0);
            json.put("deniedNum", 0);
        }
        json.put("rowCount", size);
        json.put("data", cmList);
        writeDataToAjax(JSONObject.fromObject(json));
        return NONE;
    }

    /**
     * 所在CMC下的CM
     * 
     * YangYi 修改 2013-10-19 针对CTMS下的CM数目
     * 
     * @return String
     */
    public String getCmListByCmc() {
        Map<String, Object> json = new HashMap<String, Object>();
        // 通过cmId查询所在CMC下的CM列表
        List<CmAttribute> cmList = cmService.getCmListByCmc(cmId, start, limit);
        // 通过cmId查询所在CMC下的CM列表数目
        Long size = cmService.getCmListByCmcCount(cmId);
        cmTopologyInfo = cmService.getTopologyInfo(cmId);
        Map<String, Object> cmQueryMap = new HashMap<String, Object>();
        Entity entity = entityService.getEntity(cmTopologyInfo.getCmcId());
        if (entityTypeService.isCcmts(entity.getTypeId()) && cmTopologyInfo.getCmcId() != null) {
            if (entityTypeService.isCcmtsWithAgent(entity.getTypeId())) {
                cmQueryMap.put("cmcId", cmTopologyInfo.getEntityId().toString());
                cmQueryMap.put("deviceType", cmTopologyInfo.getDeviceType().toString());
            } else {
                cmQueryMap.put("cmcId", cmTopologyInfo.getCmcId().toString());
                cmQueryMap.put("deviceType", cmTopologyInfo.getDeviceType().toString());
            }
            cmQueryMap.put("status", Integer.toString(6));
            // cmQueryMap.put("deviceType", cmTopologyInfo.getDeviceType().toString());
            registeredNum = cmService.getCmNumByStatus(cmQueryMap);
            cmQueryMap.remove("status");
            cmQueryMap.put("status", Integer.toString(7));
            deniedNum = cmService.getCmNumByStatus(cmQueryMap);
            // json.put("cmTopologyInfo", cmTopologyInfo);//移到if外面
            json.put("totalNum", size);
            json.put("registeredNum", registeredNum);
            json.put("deniedNum", deniedNum);
        } else if (entityTypeService.isCmts(entity.getTypeId())) {
            // 处理CMTS下CM
            cmQueryMap.put("cmcId", cmTopologyInfo.getEntityId().toString());
            cmQueryMap.put("status", Integer.toString(6));
            cmQueryMap.put("deviceType", cmTopologyInfo.getDeviceType().toString());
            registeredNum = cmService.getCmtsCmNumByStatus(cmQueryMap);
            cmQueryMap.remove("status");
            cmQueryMap.put("status", Integer.toString(7));
            deniedNum = cmService.getCmtsCmNumByStatus(cmQueryMap);
            // json.put("cmTopologyInfo", cmTopologyInfo);//移到if外面
            json.put("totalNum", size);
            json.put("registeredNum", registeredNum);
            json.put("deniedNum", deniedNum);
        } else {
            json.put("totalNum", size);
            json.put("registeredNum", 0);
            json.put("deniedNum", 0);
        }
        json.put("cmTopologyInfo", cmTopologyInfo);
        json.put("rowCount", size);
        json.put("data", cmList);
        writeDataToAjax(JSONObject.fromObject(json));
        return NONE;
    }

    /**
     * 所在上行通道下的CM
     * 
     * YangYi 修改 2013-10-19 针对CTMS下的CM数目
     * 
     * @return String
     */
    public String getCmListByUpPort() {
        Map<String, Object> json = new HashMap<String, Object>();
        // 通过cmId查询所在上行通道下的CM列表
        List<CmAttribute> cmList = cmService.getCmListByUpPortId(cmId, start, limit);
        // 通过cmId查询所在上行通道下的CM列表数目
        Long size = cmService.getCmListByUpPortIdCount(cmId);
        cmTopologyInfo = cmService.getTopologyInfo(cmId);
        Map<String, Object> cmQueryMap = new HashMap<String, Object>();
        Entity entity = entityService.getEntity(cmTopologyInfo.getCmcId());
        if (entityTypeService.isCcmts(entity.getTypeId()) && cmTopologyInfo.getDocsIfUpChannelId() != null) {
            if (entityTypeService.isCcmtsWithAgent(entity.getTypeId())) {
                cmQueryMap.put("cmcId", cmTopologyInfo.getEntityId().toString());
                cmQueryMap.put("deviceType", cmTopologyInfo.getDeviceType().toString());
            } else {
                cmQueryMap.put("cmcId", cmTopologyInfo.getCmcId().toString());
                cmQueryMap.put("deviceType", cmTopologyInfo.getDeviceType().toString());
            }
            cmQueryMap.put("channelId", cmTopologyInfo.getDocsIfUpChannelId().toString());
            cmQueryMap.put("status", Integer.toString(6));
            // cmQueryMap.put("deviceType", cmTopologyInfo.getDeviceType().toString());
            registeredNum = cmService.getCmNumByStatus(cmQueryMap);
            cmQueryMap.remove("status");
            cmQueryMap.put("status", Integer.toString(7));
            deniedNum = cmService.getCmNumByStatus(cmQueryMap);
            // json.put("cmTopologyInfo", cmTopologyInfo);//移到IF外面
            json.put("totalNum", size);
            json.put("registeredNum", registeredNum);
            json.put("deniedNum", deniedNum);
        } else if (entityTypeService.isCmts(entity.getTypeId())) {
            // 处理cmts 下cm
            cmQueryMap.put("cmcId", cmTopologyInfo.getEntityId().toString());
            cmQueryMap.put("upChannelIndex", cmTopologyInfo.getUpChannelIndex());
            cmQueryMap.put("status", Integer.toString(6));
            cmQueryMap.put("deviceType", cmTopologyInfo.getDeviceType().toString());
            registeredNum = cmService.getCmtsCmNumByStatus(cmQueryMap);
            cmQueryMap.remove("status");
            cmQueryMap.put("status", Integer.toString(7));
            deniedNum = cmService.getCmtsCmNumByStatus(cmQueryMap); //
            // json.put("cmTopologyInfo", cmTopologyInfo);//移到IF外面
            json.put("totalNum", size);
            json.put("registeredNum", registeredNum);
            json.put("deniedNum", deniedNum);
        } else {
            json.put("totalNum", size);
            json.put("registeredNum", 0);
            json.put("deniedNum", 0);
        }
        json.put("cmTopologyInfo", cmTopologyInfo);
        json.put("rowCount", size);
        json.put("data", cmList);
        writeDataToAjax(JSONObject.fromObject(json));
        return NONE;
    }

    /**
     * 所在下行通道下的CM
     * 
     * YangYi 修改 2013-10-19 针对CTMS下的CM数目
     * 
     * @return String
     */
    public String getCmListByDownPort() {
        Map<String, Object> json = new HashMap<String, Object>();
        // 通过cmId查询所在下行通道下的CM列表
        List<CmAttribute> cmList = cmService.getCmListByDownPortId(cmId, start, limit);
        // 通过cmId查询所在下行通道下的CM列表数目
        Long size = cmService.getCmListByDownPortIdCount(cmId);
        cmTopologyInfo = cmService.getTopologyInfo(cmId);
        Map<String, Object> cmQueryMap = new HashMap<String, Object>();
        Entity entity = entityService.getEntity(cmTopologyInfo.getCmcId());
        if (entityTypeService.isCcmts(entity.getTypeId()) && cmTopologyInfo.getDocsIfDownChannelId() != null) {
            if (entityTypeService.isCcmtsWithAgent(entity.getTypeId())) {
                cmQueryMap.put("cmcId", cmTopologyInfo.getEntityId().toString());
                cmQueryMap.put("deviceType", cmTopologyInfo.getDeviceType().toString());
            } else {
                cmQueryMap.put("cmcId", cmTopologyInfo.getCmcId().toString());
                cmQueryMap.put("deviceType", cmTopologyInfo.getDeviceType().toString());
            }
            cmQueryMap.put("docsIfDownChannelId", cmTopologyInfo.getDocsIfDownChannelId().toString());
            cmQueryMap.put("status", Integer.toString(6));
            // cmQueryMap.put("deviceType", cmTopologyInfo.getDeviceType().toString());
            registeredNum = cmService.getCmNumByStatus(cmQueryMap);
            cmQueryMap.remove("status");
            cmQueryMap.put("status", Integer.toString(7));
            deniedNum = cmService.getCmNumByStatus(cmQueryMap);
            // json.put("cmTopologyInfo", cmTopologyInfo); //移到if外面
            json.put("totalNum", size);
            json.put("registeredNum", registeredNum);
            json.put("deniedNum", deniedNum);
        } else if (entityTypeService.isCmts(entity.getTypeId())) {
            // 处理CMTS下CM
            cmQueryMap.put("cmcId", cmTopologyInfo.getEntityId().toString());
            cmQueryMap.put("downChannelIndex", cmTopologyInfo.getDownChannelIndex());
            cmQueryMap.put("status", Integer.toString(6));
            cmQueryMap.put("deviceType", cmTopologyInfo.getDeviceType().toString());
            registeredNum = cmService.getCmtsCmNumByStatus(cmQueryMap);
            cmQueryMap.remove("status");
            cmQueryMap.put("status", Integer.toString(7));
            deniedNum = cmService.getCmtsCmNumByStatus(cmQueryMap); //
            // json.put("cmTopologyInfo", cmTopologyInfo);//移到if外面
            json.put("totalNum", size);
            json.put("registeredNum", registeredNum);
            json.put("deniedNum", deniedNum);

        } else {
            json.put("totalNum", size);
            json.put("registeredNum", 0);
            json.put("deniedNum", 0);
        }
        json.put("cmTopologyInfo", cmTopologyInfo);
        json.put("rowCount", size);
        json.put("data", cmList);
        writeDataToAjax(JSONObject.fromObject(json));
        return NONE;
    }

    /**
     * 批量重启CM
     * 
     * @return String
     */
    // TODO 尚无法确定如何实现批量重启CM的操作日志
    // @OperationLogProperty(actionName = "cmAction", operationName = "Batch Restart CM${source}")
    public String restartCms() {
        String idList = cmsArrayId.get(0);
        String[] idStringList = idList.split(Symbol.COMMA);
        Map<String, Object> json = new HashMap<String, Object>();
        List<String> snmpUnreachable = new ArrayList<String>();
        List<String> icmpUnreachable = new ArrayList<String>();
        List<String> offline = new ArrayList<String>();
        for (int i = 0; i < idStringList.length; i++) {
            Long cmId = 0l;
            CmAttribute cm = null;
            String cmidString = idStringList[i];
            if (cmidString != null) {
                cmId = Long.parseLong(cmidString);
                cm = cmService.getCmAttributeByCmId(cmId);
            }
            logger.info("ip" + cm.getStatusInetAddress() + " mac:" + cm.getStatusMacAddress());
            int result = cmService.resetCm(cmId);
            switch (result) {
            case 1:
                offline.add(cm.getStatusMacAddress());
                break;
            case 2:
                operationResult = OperationLog.FAILURE;
                logger.error(cmIp + "reset fail!Cause by ICMP unreachable.");
                icmpUnreachable.add(cm.getStatusMacAddress());
                break;
            case 3:
                operationResult = OperationLog.FAILURE;
                logger.error(cmIp + "reset fail!Cause by SnmpException.");
                snmpUnreachable.add(cm.getStatusMacAddress());
                break;
            default:
                json.put("message", "success");
            }
        }
        json.put("snmpUnreachable", snmpUnreachable);
        json.put("icmpUnreachable", icmpUnreachable);
        json.put("offline", offline);
        writeDataToAjax(JSONObject.fromObject(json));
        return NONE;
    }

    public String checkCmSnmp() {
        JSONObject jObj = new JSONObject();
        try {
            jObj.put("success", cmService.checkCmSnmp(cmIp, this.readCommunity, this.writeCommunity));
        } catch (Exception e) {
            logger.warn("", e);
            jObj.put("success", false);
        }
        writeDataToAjax(jObj);
        return NONE;
    }

    /**
     * 显示CM 大客户IP信息页面
     * 
     * @return
     */
    public String showCmStaticIpInfo() {
        return SUCCESS;
    }

    /**
     * 显示CM详细信息页面
     * 
     * @return String
     */
    public String showCmInfo() {
        return SUCCESS;
    }

    /**
     * 显示cm上下线行为页面
     * 
     * @return
     */
    public String showCmActionInfo() {
        return SUCCESS;
    }

    /**
     * 全局Global 显示cpe上下线行为页面
     * 
     * @return
     */
    public String showCpeActionInfo() {
        return SUCCESS;
    }

    /**
     * 全局Global 获取cpe上下线行为数据
     * 
     * @return
     */
    public String loadCpeActionInfo() {
        Map<String, Object> map = new HashMap<String, Object>();
        String cmMac = cmService.getCmAttributeByCmId(cmId).getStatusMacAddress();
        map.put("cmMac", new MacUtils(cmMac).longValue());
        List<CpeAct> cpeActList = cmService.getCpeActionInfoByCmMac(map);
        // add by fanzidong,需要在展示前格式化MAC地址
        UserContext uc = (UserContext) super.getSession().get(UserContext.KEY);
        String displayRule = uc.getMacDisplayStyle();
        for (CpeAct cpeAct : cpeActList) {
            String formatedCmMac = MacUtils.convertMacToDisplayFormat(cpeAct.getCmmacString(), displayRule);
            cpeAct.setCmmacString(formatedCmMac);
            String formatedCpeMac = MacUtils.convertMacToDisplayFormat(cpeAct.getCpemacString(), displayRule);
            cpeAct.setCpemacString(formatedCpeMac);
        }
        Map<String, Object> json = new HashMap<String, Object>();
        Integer size = cpeActList.size();
        json.put("rowCount", size);
        json.put("data", cpeActList);
        writeDataToAjax(JSONObject.fromObject(json));
        return NONE;
    }

    /**
     * 全局Global 根据CMID查找对应CM上的cpe数据比较
     * 
     * @return String
     */
    public String showRealCpeMaxCpe() {
        Map<String, Object> json = new HashMap<String, Object>();
        String realCpeNum = cmService.getRealIpNum(cmId);
        String maxCpeNum = cmService.getCpeMaxIpNum(cmId);
        realCpeMaxCpe = realCpeNum + Symbol.SLASH + maxCpeNum;
        json.put("realCpeMaxCpe", realCpeMaxCpe);
        writeDataToAjax(JSONObject.fromObject(json));
        return NONE;
    }

    /**
     * 全局Global 根据CMID查找在该CM上的cpe列表
     * 
     * @return String
     */
    public String showCpeListByCmId() {
        Map<String, Object> json = new HashMap<String, Object>();
        List<CpeAttribute> cpeAttributes = cmService.getCpeListByCmId(cmId);
        Integer size = cpeAttributes.size();
        json.put("rowCount", size);
        json.put("data", cpeAttributes);
        writeDataToAjax(JSONObject.fromObject(json));
        return NONE;
    }

    /**
     * 全局Global
     * 
     * @return
     */
    public String clearCpeInfo() {
        String message;
        try {
            cmService.clearCpeInfo(cmId, cpeMac);
            message = "success";
        } catch (Exception e) {
            logger.error("", e);
            message = "failure";
        }
        writeDataToAjax(message);
        return NONE;
    }

    /**
     * 全局Global 获取CM下CPE信息
     * 
     * @return
     */
    public String loadCmStaticIpList() {
        Map<String, Object> json = new HashMap<String, Object>();
        List<CmStaticIp> cmStaticIpList = cmService.getCmStaticIpList(cmId);
        Integer size = cmStaticIpList.size();
        json.put("rowCount", size);
        json.put("data", cmStaticIpList);
        writeDataToAjax(JSONObject.fromObject(json));
        return NONE;
    }

    /**
     * 全局Global 根据CPE设备ip找到所在CM
     * 
     * @return String
     */
    public String showCmByCpeIp() {
        // 根据ip查找CM
        cmAttributes = ccmtsCmListService.showCmByCpeIp(cpeIp);
        Map<String, Object> json = new HashMap<String, Object>();
        Integer size = cmAttributes.size();
        json.put("rowCount", size);
        json.put("data", cmAttributes);
        writeDataToAjax(JSONObject.fromObject(json));
        return NONE;
    }

    public Logger getLogger() {
        return logger;
    }

    public void setLogger(Logger logger) {
        this.logger = logger;
    }

    public CmService getCmService() {
        return cmService;
    }

    public void setCmService(CmService cmService) {
        this.cmService = cmService;
    }

    public boolean isHasSupportEpon() {
        return hasSupportEpon;
    }

    public void setHasSupportEpon(boolean hasSupportEpon) {
        this.hasSupportEpon = hasSupportEpon;
    }

    public Long getPonId() {
        return ponId;
    }

    public void setPonId(Long ponId) {
        this.ponId = ponId;
    }

    public Long getCmcId() {
        return cmcId;
    }

    public void setCmcId(Long cmcId) {
        this.cmcId = cmcId;
    }

    public Long getPonNo() {
        return ponNo;
    }

    public void setPonNo(Long ponNo) {
        this.ponNo = ponNo;
    }

    public Long getCmcNo() {
        return cmcNo;
    }

    public void setCmcNo(Long cmcNo) {
        this.cmcNo = cmcNo;
    }

    public Long getSlotNo() {
        return slotNo;
    }

    public void setSlotNo(Long slotNo) {
        this.slotNo = slotNo;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public Long getChannelId() {
        return channelId;
    }

    public void setChannelId(Long channelId) {
        this.channelId = channelId;
    }

    public Long getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(Long deviceType) {
        this.deviceType = deviceType;
    }

    public Long getUpChannelId() {
        return upChannelId;
    }

    public void setUpChannelId(Long upChannelId) {
        this.upChannelId = upChannelId;
    }

    public Long getDownChannelId() {
        return downChannelId;
    }

    public void setDownChannelId(Long downChannelId) {
        this.downChannelId = downChannelId;
    }

    public Long getChannelType() {
        return channelType;
    }

    public void setChannelType(Long channelType) {
        this.channelType = channelType;
    }

    public Long getChannelIndex() {
        return channelIndex;
    }

    public void setChannelIndex(Long channelIndex) {
        this.channelIndex = channelIndex;
    }

    public Long getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(Long totalNum) {
        this.totalNum = totalNum;
    }

    public Long getDeniedNum() {
        return deniedNum;
    }

    public void setDeniedNum(Long deniedNum) {
        this.deniedNum = deniedNum;
    }

    public Long getRegisteredNum() {
        return registeredNum;
    }

    public void setRegisteredNum(Long registeredNum) {
        this.registeredNum = registeredNum;
    }

    public Long getCmId() {
        return cmId;
    }

    public void setCmId(Long cmId) {
        this.cmId = cmId;
    }

    public String getCmIp() {
        return cmIp;
    }

    public void setCmIp(String cmIp) {
        this.cmIp = cmIp;
    }

    public String getCpeIp() {
        return cpeIp;
    }

    public void setCpeIp(String cpeIp) {
        this.cpeIp = cpeIp;
    }

    public String getCpeMac() {
        return cpeMac;
    }

    public void setCpeMac(String cpeMac) {
        this.cpeMac = cpeMac;
    }

    public String getRealCpeMaxCpe() {
        return realCpeMaxCpe;
    }

    public void setRealCpeMaxCpe(String realCpeMaxCpe) {
        this.realCpeMaxCpe = realCpeMaxCpe;
    }

    public String getCmIdString() {
        return cmIdString;
    }

    public void setCmIdString(String cmIdString) {
        this.cmIdString = cmIdString;
    }

    public CmStatus getCmStatus() {
        return cmStatus;
    }

    public void setCmStatus(CmStatus cmStatus) {
        this.cmStatus = cmStatus;
    }

    public Entity getOltEntity() {
        return oltEntity;
    }

    public void setOltEntity(Entity oltEntity) {
        this.oltEntity = oltEntity;
    }

    public CmAttribute getCmAttribute() {
        return cmAttribute;
    }

    public void setCmAttribute(CmAttribute cmAttribute) {
        this.cmAttribute = cmAttribute;
    }

    public CmcAttribute getCmcAttribute() {
        return cmcAttribute;
    }

    public void setCmcAttribute(CmcAttribute cmcAttribute) {
        this.cmcAttribute = cmcAttribute;
    }

    public CmTopologyInfo getCmTopologyInfo() {
        return cmTopologyInfo;
    }

    public void setCmTopologyInfo(CmTopologyInfo cmTopologyInfo) {
        this.cmTopologyInfo = cmTopologyInfo;
    }

    public List<CmAttribute> getCmAttributes() {
        return cmAttributes;
    }

    public void setCmAttributes(List<CmAttribute> cmAttributes) {
        this.cmAttributes = cmAttributes;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public String getReadCommunity() {
        return readCommunity;
    }

    public void setReadCommunity(String readCommunity) {
        this.readCommunity = readCommunity;
    }

    public String getWriteCommunity() {
        return writeCommunity;
    }

    public void setWriteCommunity(String writeCommunity) {
        this.writeCommunity = writeCommunity;
    }

    public ArrayList<String> getCmsArrayId() {
        return cmsArrayId;
    }

    public void setCmsArrayId(ArrayList<String> cmsArrayId) {
        this.cmsArrayId = cmsArrayId;
    }

    public Integer getOperationResult() {
        return operationResult;
    }

    public void setOperationResult(Integer operationResult) {
        this.operationResult = operationResult;
    }

}
