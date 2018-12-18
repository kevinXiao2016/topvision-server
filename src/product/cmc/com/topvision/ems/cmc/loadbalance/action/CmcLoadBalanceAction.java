/***********************************************************************
 * $Id: LoadBalanceAction.java,v1.0 2011-12-8 上午10:29:01 $
 * 
 * @author: loyal
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.loadbalance.action;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.topvision.ems.cmc.ccmts.facade.domain.CmcAttribute;
import com.topvision.ems.cmc.ccmts.service.CmcChannelService;
import com.topvision.ems.cmc.ccmts.service.CmcService;
import com.topvision.ems.cmc.domain.CmcDownChannelBaseShowInfo;
import com.topvision.ems.cmc.downchannel.service.CmcDownChannelService;
import com.topvision.ems.cmc.loadbalance.domain.CmcLoadBalBasicRuleTpl;
import com.topvision.ems.cmc.loadbalance.domain.CmcLoadBalPolicyTpl;
import com.topvision.ems.cmc.loadbalance.domain.CmcLoadBalanceGroup;
import com.topvision.ems.cmc.loadbalance.exception.LoadBalException;
import com.topvision.ems.cmc.loadbalance.facade.domain.CmcLoadBalBasicRule;
import com.topvision.ems.cmc.loadbalance.facade.domain.CmcLoadBalCfg;
import com.topvision.ems.cmc.loadbalance.facade.domain.CmcLoadBalChannel;
import com.topvision.ems.cmc.loadbalance.facade.domain.CmcLoadBalExcludeCm;
import com.topvision.ems.cmc.loadbalance.facade.domain.CmcLoadBalPolicy;
import com.topvision.ems.cmc.loadbalance.facade.domain.CmcLoadBalRestrictCm;
import com.topvision.ems.cmc.loadbalance.service.CmcLoadBalanceService;
import com.topvision.ems.cmc.upchannel.domain.CmcUpChannelBaseShowInfo;
import com.topvision.ems.cmc.upchannel.service.CmcUpChannelService;
import com.topvision.ems.cmc.util.ResourcesUtil;
import com.topvision.framework.common.MacUtils;
import com.topvision.framework.web.struts2.BaseAction;
import com.topvision.platform.domain.UserContext;
import com.topvision.platform.zetaframework.util.ZetaUtil;
import com.topvision.platform.zetaframework.var.PageBehavior;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 负载均衡相关功能
 * 
 * @author loyal
 * @created @2011-12-8-上午10:29:01
 * 
 */
@Controller("cmcLoadBalanceAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class CmcLoadBalanceAction extends BaseAction {
    private static final long serialVersionUID = -1200427546317914273L;
    private final Logger logger = LoggerFactory.getLogger(CmcLoadBalanceAction.class);
    @Resource(name = "cmcLoadBalanceService")
    private CmcLoadBalanceService cmcLoadBalanceService;
    @Autowired
    private CmcChannelService cmcChannelService;
    @Resource(name = "cmcUpChannelService")
    private CmcUpChannelService cmcUpChannelService;
    @Resource(name = "cmcDownChannelService")
    private CmcDownChannelService cmcDownChannelService;
    @Resource(name = "cmcService")
    private CmcService cmcService;
    private Long cmcId;
    private Long entityId;
    private CmcLoadBalCfg loadBalanceGlobalCfg;
    private String rangeDetail;
    private Integer productType;

    private Long policyId;// 时间策略ID
    private Long policyTplId; // 策略模板ID
    private String policyName; // 生效策略模板名
    private Integer policyType;// 策略类型
    private String disSections;// 不生效的时间段（如：08:30-09:30）,V2版本传数组有问题，改成String传递数据

    // load balance group field
    private Long grpId;
    private String groupName;
    private String groupUpchannel;
    @Deprecated
    // 只在下面deprecated的方法中用到
    private Long[] channelIndexs;
    private String groupDownchannel;
    @Deprecated
    // 只在下面deprecated的方法中用到
    private String[] groupRanges;
    private Integer pageAction;
    private Long topLoadBalConfigDiffThresh;
    private Integer topLoadBalConfigEnable;
    private Long topLoadBalConfigInterval;
    private Long topLoadBalConfigMaxMoves;
    private Integer topLoadBalConfigMethod;
    private Integer topLoadBalConfigNumPeriod;
    private Long topLoadBalConfigPeriod;
    private Long topLoadBalConfigTriggerThresh;
    private Long docsLoadBalGrpId;
    private Integer topLoadBalConfigDccInitAtdma;
    private Integer topLoadBalConfigDbcInitAtdma;
    private Integer topLoadBalConfigDccInitScdma;
    private Integer topLoadBalConfigDbcInitScdma;

    @Deprecated
    // 只在下面deprecated的方法中用到
    private String[] macRanges;

    private String addedGroupChannelsStr;
    private String deletedGroupChannelsStr;
    private String addedGroupRangesStr;
    private String deletedGroupRangesStr;
    private String macRangesStr;

    private Long topLoadBalExcludeCmIndex;
    private Long topLoadBalConfigCmcIndex;
    private JSONObject cmcLoadBalanceGroup;
    private CmcAttribute cmcAttribute;

    private String macDisplayFormat;

    /**
     * 从设备刷新负载均衡相关的数据
     * 
     * @return
     */
    public String refreshLoadBalance() {
        cmcLoadBalanceService.refreshLoadBalanceConfig(cmcId);
        cmcLoadBalanceService.refreshLoadBalanceGroup(cmcId);
        cmcLoadBalanceService.refreshLoadBalanceExcludeCm(cmcId);
        return NONE;
    }

    /**
     * 显示负载均衡页面
     * 
     * @return String
     */
    public String showLoadbalance() {
    	setCmcAttribute(cmcService.getCmcAttributeByCmcId(cmcId));
        /** 获取CCMTS的负载均衡全局配置.跳转时可以较为方便的在JSP中填充数据 **/
        UserContext uc = (UserContext) super.getSession().get(UserContext.KEY);
        macDisplayFormat = uc.getMacDisplayStyle();
        loadBalanceGlobalCfg = cmcLoadBalanceService.getLoadBalanceGlobalCfg(cmcId);
        return SUCCESS;
    }

    /**
     * 将设备上的负载均衡策略同步到数据库
     * 
     * @return
     */
    public String syncLoadBalPolicy() {
        JSONObject jObj = new JSONObject();
        try {
            cmcLoadBalanceService.txSyncLoadBalPolicy(this.cmcId);
            jObj.put("success", true);
        } catch (Exception e) {
            jObj.put("success", false);
            logger.error("", e);
        }
        writeDataToAjax(jObj);
        return NONE;
    }

    /**
     * 获取CCMTS的负载均衡全局配置。刷新设备的时候方便做局部更新
     * 
     * @return
     * @throws IOException
     */
    public String getLBGlobalConfigData() throws IOException {
        CmcLoadBalCfg config = cmcLoadBalanceService.getLoadBalanceGlobalCfg(cmcId);
        JSONObject json = JSONObject.fromObject(config);
        json.write(response.getWriter());
        return NONE;
    }

    /**
     * 修改CCMTS负载均衡的全局配置
     * 
     * @return
     * @throws SQLException
     */
    public String modifyLBGlobalConfig() throws SQLException {
        CmcLoadBalCfg cmcLoadBalCfg = new CmcLoadBalCfg();
        cmcLoadBalCfg.setCmcId(cmcId);
        cmcLoadBalCfg.setTopLoadBalConfigCmcIndex(topLoadBalConfigCmcIndex);
        cmcLoadBalCfg.setTopLoadBalConfigDiffThresh(topLoadBalConfigDiffThresh);// 差值门限
        cmcLoadBalCfg.setTopLoadBalConfigEnable(topLoadBalConfigEnable);// 开启&关闭
        cmcLoadBalCfg.setTopLoadBalConfigInterval(topLoadBalConfigInterval);// 移动时间间隔
        cmcLoadBalCfg.setTopLoadBalConfigMaxMoves(topLoadBalConfigMaxMoves);// 最大移动CM数
        cmcLoadBalCfg.setTopLoadBalConfigMethod(topLoadBalConfigMethod);// 方式 : unitli,moderm
        cmcLoadBalCfg.setTopLoadBalConfigNumPeriod(topLoadBalConfigNumPeriod);// 周期加权
        cmcLoadBalCfg.setTopLoadBalConfigPeriod(topLoadBalConfigPeriod);// 周期
        cmcLoadBalCfg.setTopLoadBalConfigTriggerThresh(topLoadBalConfigTriggerThresh);// 阈值
        cmcLoadBalCfg.setTopLoadBalConfigDbcInitAtdma(topLoadBalConfigDbcInitAtdma);
        cmcLoadBalCfg.setTopLoadBalConfigDccInitAtdma(topLoadBalConfigDccInitAtdma);
        cmcLoadBalCfg.setTopLoadBalConfigDbcInitScdma(topLoadBalConfigDbcInitScdma);
        cmcLoadBalCfg.setTopLoadBalConfigDccInitScdma(topLoadBalConfigDccInitScdma);
        cmcLoadBalanceService.modifyLoadBalanceGlobalCfg(cmcLoadBalCfg);
        return NONE;
    }

    /**
     * 添加负载均衡组 未发现使用该方法
     * 
     * @deprecated
     * @return
     * @throws SQLException
     */
    public String addLoadBalanceGroup() throws SQLException {
        CmcLoadBalanceGroup group = new CmcLoadBalanceGroup();
        group.setCmcId(cmcId);
        group.setGroupName(groupName);
        // 添加上下行信道
        List<CmcLoadBalChannel> channelList = new ArrayList<CmcLoadBalChannel>();
        for (Long chanIndex : channelIndexs) {
            CmcLoadBalChannel channel = new CmcLoadBalChannel();
            channel.setDocsLoadBalChannelIfIndex(chanIndex);
            channelList.add(channel);
        }
        group.setChannels(channelList);
        // 添加ranges
        List<CmcLoadBalRestrictCm> rangeList = new ArrayList<CmcLoadBalRestrictCm>();
        for (String range : groupRanges) {
            CmcLoadBalRestrictCm groupRange = new CmcLoadBalRestrictCm();
            groupRange.setTopLoadBalRestrictCmMacRang(range);
            rangeList.add(groupRange);
        }
        group.setRanges(rangeList);
        cmcLoadBalanceService.addLoadBalanceGroup(group);
        return NONE;
    }

    /**
     * 修改负载均衡组名称
     * 
     * @return
     * @throws SQLException
     */
    public String modifyLoadBalanceGroup() throws SQLException {
        cmcLoadBalanceService.modifyLoadBalanceGroup(groupName, grpId);
        return NONE;
    }

    /**
     * 删除负载均衡组
     * 
     * @return
     * @throws SQLException
     */
    public String deleteLoadBalanceGroup() throws SQLException {
        cmcLoadBalanceService.deleteLoadBalanceGroup(docsLoadBalGrpId, cmcId);
        return NONE;
    }

    /**
     * 修改负载均衡组信道列表 需要事先确定好 删除的信道以及新增的信道 未发现使用该方法
     * 
     * @deprecated
     * @return
     */
    public String modifyLoadBalanceChannel() {
        List<Long> addChannelIndexs = new ArrayList<Long>();
        List<Long> deleteChannelIndexs = new ArrayList<Long>();
        cmcLoadBalanceService.modifyLoadBalanceChannel(cmcId, docsLoadBalGrpId, addChannelIndexs, deleteChannelIndexs);
        return NONE;
    }

    /**
     * 修改负载均衡组受限cm mac段列表 mark by @bravin: 组受限MAC地址段的index是一直变化的，所以都需要先删后加 未发现使用该方法
     * 
     * @deprecated
     * @return
     */
    public String modifyLoadBalanceMacRanges() {
        cmcLoadBalanceService.modifyLoadBalanceMacRanges(cmcId, docsLoadBalGrpId, Arrays.asList(macRanges), null);
        return NONE;
    }

    /**
     * 删除CCMTS的排除MAC地址段
     * 
     * @return
     */
    public String deleteLoadBalanceExcMacRange() {
        JSONObject jObj = new JSONObject();

        CmcLoadBalExcludeCm cmcLoadBalExcludeCm = new CmcLoadBalExcludeCm();
        cmcLoadBalExcludeCm.setCmcId(cmcId);
        cmcLoadBalExcludeCm.setTopLoadBalExcludeCmIndex(topLoadBalExcludeCmIndex);
        try {
            cmcLoadBalanceService.deleteLoadBalanceExcMacRange(cmcLoadBalExcludeCm);
            /*
             * 删除后由于设备上index改变，因此需要刷新。 为了防止底层没有处理完成，做时间延迟处理
             */
            Thread.sleep(1000);
            cmcLoadBalanceService.refreshLoadBalanceExcludeCm(cmcId);
            jObj.put("success", true);
        } catch (Exception e) {
            jObj.put("success", false);
            logger.error("error:", e);
        }
        writeDataToAjax(jObj);
        return NONE;
    }

    /**
     * 添加一个CCMTS的排除MAC地址段。如果重叠了则提示用户
     * 
     * @return
     * @throws IOException
     * @throws SQLException
     */
    public String addLoadBalanceExcMacRange() throws IOException, LoadBalException {
        JSONObject json = new JSONObject();
        List<CmcLoadBalExcludeCm> excMacRanges = cmcLoadBalanceService.getLoadBalanceExcMacRange(cmcId);
        String macRange = rangeDetail;
        boolean isOverlap = false;
        boolean overLimited = false;
        if (excMacRanges.size() >= 32) {
            overLimited = true;
        } else {
            for (CmcLoadBalExcludeCm cmRange : excMacRanges) {
                if (isMacRangeOverlap(macRange, cmRange.getTopLoadBalExcludeCmMacRang())) {
                    isOverlap = true;
                    break;
                }
            }
            if (!isOverlap) {
                CmcLoadBalExcludeCm exc = new CmcLoadBalExcludeCm();
                exc.setCmcId(cmcId);
                exc.setTopLoadBalExcludeCmMacRang(macRange);
                try {
                    cmcLoadBalanceService.addLoadBalanceExcMacRange(exc);
                } catch (LoadBalException e) {
                    logger.error("", e);
                    json.put("snmpError", true);
                } catch (SQLException e) {
                    logger.error("", e);
                }
            }
        }
        json.put("overLimited", overLimited);
        json.put("overlap", isOverlap);
        json.write(response.getWriter());

        return NONE;
    }

    /**
     * 分析2个MAC地址段是否有重叠，如果有重叠，则前端提示是哪个地址段与其重叠。必须保证地址段的顺序是正序的 算法： 不重叠必须满足如下条件 Max（range1） <
     * min(range2) 或者 max(range2) < min(range1)
     * 
     * @param targetMacRange
     * @param originMacRange
     * @return
     */
    private boolean isMacRangeOverlap(String targetMacRange, String originMacRange) {
        String[] target = targetMacRange.split(" ");
        String[] origin = originMacRange.split(" ");
        if (MacUtils.compare(target[1], origin[0]) == -1 || MacUtils.compare(target[0], origin[1]) == 1) {
            return false;// 没有重叠
        }
        return true; // 重叠了
    }

    /**
     * 得到CCMTS排除MAC地址段列表。由于存在动态添加与删除，并且存在刷新设备的需求，所以不应放在JSP跳转中。
     * 
     * @return
     * @throws IOException
     */
    public String getLoadBalanceExcMacRangeList() throws IOException {
        /** 获取CCMTS的排除MAC地址段 */
        List<CmcLoadBalExcludeCm> excMacRange = cmcLoadBalanceService.getLoadBalanceExcMacRange(cmcId);
        JSONArray excMacRangeArray = new JSONArray();
        if (excMacRange != null) {
            excMacRangeArray = JSONArray.fromObject(excMacRange);
        }
        excMacRangeArray.write(response.getWriter());
        return NONE;
    }

    /**
     * 获取时间策略
     * 
     * @return
     * @throws IOException
     */
    public String getLoadBalanceTimePolicy() throws IOException {
        CmcLoadBalPolicy policy = cmcLoadBalanceService.getLoadBalanceTimePolicy(cmcId);
        if (policy != null) {
            List<CmcLoadBalBasicRule> rules = policy.getRules();
            if (rules != null && !rules.isEmpty()) {
                /**
                 * 如果rules配置过，并且 rules 至少配置了一条记录，则取出其第一条记录并且取得其 enabled/disabled状态作为policy的状态
                 * 备注：标准MIB中定义的enabled
                 * /disabled状态放在RULE表中，CCMTS在policy的实现上进行了特殊处理，所以我们认为这个状态应该放在policy而不是rule中。
                 */
                Integer LBEnabled = rules.get(0).getDocsLoadBalBasicRuleEnable();
                policy.setLBEnabled(LBEnabled);
            }
        } else {
            // 如果policy不存在即未配置policy，则表示负载均衡一直生效
            policy = new CmcLoadBalPolicy();
            policy.setLBEnabled(1);
        }
        JSONObject json = JSONObject.fromObject(policy);
        json.write(response.getWriter());
        return NONE;
    }

    /**
     * 获取CCMTS的负载均衡组列表
     * 
     * @return
     * @throws IOException
     * @throws SQLException
     */
    public String getLoadBalanceGroupList() throws IOException, SQLException {
        JSONArray json = new JSONArray();
        List<CmcLoadBalanceGroup> grouplist = cmcLoadBalanceService.selectLoadBalanceGroupList(cmcId);
        json = JSONArray.fromObject(grouplist);
        json.write(response.getWriter());
        return NONE;
    }

    /**
     * 获取上行信道列表
     * 
     * @return
     * @throws IOException
     */
    public String getCcmtsUpchannelList() throws IOException {
        List<CmcUpChannelBaseShowInfo> upchannelList = cmcUpChannelService.getUpChannelBaseShowInfoList(cmcId);
        JSONArray json = new JSONArray();
        for (CmcUpChannelBaseShowInfo info : upchannelList) {
            JSONObject obj = new JSONObject();
            obj.put("channelId", ZetaUtil.getStaticString("CMC/CCMTS.upStreamChannel") + ":" + info.getChannelId());
            obj.put("channelIndex", info.getChannelIndex());
            json.add(obj);
        }
        json.write(response.getWriter());
        return NONE;
    }

    /**
     * 获取下行信道列表
     * 
     * @return
     * @throws IOException
     */
    public String getCcmtsDownchannelList() throws IOException {
        List<CmcDownChannelBaseShowInfo> upchannelList = cmcDownChannelService.getDownChannelBaseShowInfoList(cmcId);
        JSONArray json = new JSONArray();
        for (CmcDownChannelBaseShowInfo info : upchannelList) {
            JSONObject obj = new JSONObject();
            obj.put("channelId",
                    ZetaUtil.getStaticString("CMC/CCMTS.downStreamChannel") + ":" + info.getDocsIfDownChannelId());
            obj.put("channelIndex", info.getChannelIndex());
            json.add(obj);
        }
        json.write(response.getWriter());
        return NONE;
    }

    /**
     * 创建或者修改负载均衡组
     * 
     * @return
     * @throws SQLException
     */
    public String createOrModifyLoadbalance() {
        JSONObject js = new JSONObject();
        CmcLoadBalanceGroup group = new CmcLoadBalanceGroup();
        if (pageAction == PageBehavior.CREATE) { // create lb group
            try {
                List<CmcLoadBalanceGroup> groups = cmcLoadBalanceService.selectLoadBalanceGroupList(cmcId);
                if (groups.size() >= 16) {
                    js.put("msg", ResourcesUtil.getString("loadbalance.groupListSize"));
                } else {
                    group.setGroupName(groupName);
                    group.setCmcId(cmcId);
                    List<CmcLoadBalChannel> channelList = new ArrayList<CmcLoadBalChannel>();
                    if (groupUpchannel != null) {
                        String[] ups = groupUpchannel.split(",");
                        for (String c : ups) {
                            CmcLoadBalChannel channel = new CmcLoadBalChannel();
                            channel.setDocsLoadBalChannelIfIndex(Long.parseLong(c));
                            channelList.add(channel);
                        }
                    }
                    if (groupDownchannel != null) {
                        String[] downs = groupDownchannel.split(",");
                        for (String c : downs) {
                            CmcLoadBalChannel channel = new CmcLoadBalChannel();
                            channel.setDocsLoadBalChannelIfIndex(Long.parseLong(c));
                            channelList.add(channel);
                        }
                    }
                    group.setChannels(channelList);
                    String[] macRangesArray = null;
                    // TODO 测试设备及网管后台对数据的处理--32条数据
                    // macRangesStr =
                    // "00:00:00:00:00:01#00:00:00:00:00:01@00:00:00:00:00:02#00:00:00:00:00:02@00:00:00:00:00:03#00:00:00:00:00:03@00:00:00:00:00:04#00:00:00:00:00:04@00:00:00:00:00:05#00:00:00:00:00:05@00:00:00:00:00:06#00:00:00:00:00:06@00:00:00:00:00:07#00:00:00:00:00:07@00:00:00:00:00:08#00:00:00:00:00:08@00:00:00:00:00:09#00:00:00:00:00:09@00:00:00:00:00:10#00:00:00:00:00:10@00:00:00:00:00:11#00:00:00:00:00:11@00:00:00:00:00:12#00:00:00:00:00:12@00:00:00:00:00:13#00:00:00:00:00:13@00:00:00:00:00:14#00:00:00:00:00:14@00:00:00:00:00:15#00:00:00:00:00:15@00:00:00:00:00:16#00:00:00:00:00:16@00:00:00:00:00:17#00:00:00:00:00:17@00:00:00:00:00:18#00:00:00:00:00:18@00:00:00:00:00:19#00:00:00:00:00:19@00:00:00:00:00:20#00:00:00:00:00:20@00:00:00:00:00:21#00:00:00:00:00:21@00:00:00:00:00:22#00:00:00:00:00:22@00:00:00:00:00:23#00:00:00:00:00:23@00:00:00:00:00:24#00:00:00:00:00:24@00:00:00:00:00:25#00:00:00:00:00:25@00:00:00:00:00:26#00:00:00:00:00:26@00:00:00:00:00:27#00:00:00:00:00:27@00:00:00:00:00:28#00:00:00:00:00:28@00:00:00:00:00:29#00:00:00:00:00:29@00:00:00:00:00:30#00:00:00:00:00:30@00:00:00:00:00:31#00:00:00:00:00:31@00:00:00:00:00:32#00:00:00:00:00:32";

                    // 如果没有配置地址段，则不设置
                    if (macRangesStr != null && !"".equals(macRangesStr)) {
                        macRangesArray = macRangesStr.split("@");
                        List<CmcLoadBalRestrictCm> ranges = new ArrayList<CmcLoadBalRestrictCm>();

                        for (String r : macRangesArray) {
                            if (!"".equals(r)) {
                                CmcLoadBalRestrictCm range = new CmcLoadBalRestrictCm();
                                range.setTopLoadBalRestrictCmMacRang(r);
                                ranges.add(range);
                            }
                        }
                        group.setRanges(ranges);
                    }
                    if (macRangesArray != null && macRangesArray.length > 32) {
                        js.put("msg", ResourcesUtil.getString("loadbalance.groupRestrictCmSize"));
                    } else {
                        try {
                            cmcLoadBalanceService.addLoadBalanceGroup(group);
                        } catch (LoadBalException e) {
                            logger.error("", e);
                            js.put("msg", e.getMessage());
                        }
                    }

                }
            } catch (SQLException e1) {
                logger.error("", e1);
            }

        } else { // modify lb group

            CmcLoadBalanceGroup oldGroup = cmcLoadBalanceService.getLoadBalanceGroup(grpId);
            String[] addedGroupChannels = addedGroupChannelsStr.split("@");
            // 新增的信道
            List<Long> addedChannelList = new ArrayList<Long>();
            for (String c : addedGroupChannels) {
                if (!"".equals(c.trim())) {
                    addedChannelList.add(Long.parseLong(c));
                }
            }
            String[] deletedGroupChannels = deletedGroupChannelsStr.split("@");
            // 删除的信道
            List<Long> deletedChannelList = new ArrayList<Long>();
            for (String c : deletedGroupChannels) {
                if (!"".equals(c.trim())) {
                    deletedChannelList.add(Long.parseLong(c));
                }
            }
            String[] deletedGroupRanges = null;
            String[] addedGroupRanges = null;
            if (!"".equals(deletedGroupRangesStr.trim())) {
                deletedGroupRanges = deletedGroupRangesStr.split("@");
            }
            if (!"".equals(addedGroupRangesStr.trim())) {
                addedGroupRanges = addedGroupRangesStr.split("@");
            }
            List<Long> delGrpIndexRanges = new ArrayList<Long>();
            List<String> addGrpRanges = new ArrayList<String>();
            if (deletedGroupRanges != null) {
                for (int i = 0; i < deletedGroupRanges.length; i++) {
                    if (!"".equals(deletedGroupRanges[i].trim())) {
                        delGrpIndexRanges.add(Long.parseLong(deletedGroupRanges[i]));
                    }
                }
            }
            if (addedGroupRanges != null) {
                for (int i = 0; i < addedGroupRanges.length; i++) {
                    if (!"".equals(addedGroupRanges[i].trim())) {
                        addGrpRanges.add(addedGroupRanges[i]);
                    }
                }
            }
            int rangesSize = oldGroup.getRanges().size();
            rangesSize = rangesSize + addGrpRanges.size() - delGrpIndexRanges.size();
            if (rangesSize > 32) {
                js.put("msg", ResourcesUtil.getString("loadbalance.groupRestrictCmSize"));
            } else {
                // 修改组名称
                try {
                    cmcLoadBalanceService.modifyLoadBalanceGroup(groupName, grpId);
                } catch (Exception e) {
                    logger.error("", e);
                }
                // 修改负载均衡组的信道
                cmcLoadBalanceService.modifyLoadBalanceChannel(cmcId, grpId, addedChannelList, deletedChannelList);
                try {
                    cmcLoadBalanceService.modifyLoadBalanceMacRanges(cmcId, grpId, addGrpRanges, delGrpIndexRanges);
                } catch (LoadBalException e) {
                    logger.error("", e);
                    js.put("msg", e.getMessage());

                }
            }
        }
        writeDataToAjax(js);
        return NONE;
    }

    /**
     * 显示负载均衡组的mac地址段详细
     * 
     * @return
     */
    public String showMacRangeDetail() {
        return SUCCESS;
    }

    /**
     * 显示负载均衡组添加页码
     * 
     * @return
     */
    public String showLoadBalanceAddition() {
        UserContext uc = (UserContext) super.getSession().get(UserContext.KEY);
        macDisplayFormat = uc.getMacDisplayStyle();
        CmcLoadBalanceGroup group;
        if (pageAction != null && pageAction == PageBehavior.MODIFY) {
            group = cmcLoadBalanceService.getLoadBalanceGroup(grpId);
        } else {
            group = new CmcLoadBalanceGroup();
        }
        cmcLoadBalanceGroup = JSONObject.fromObject(group);
        return SUCCESS;
    }

    public String showExcMacRangeAddition() {
        return SUCCESS;
    }

    /**
     * 获得负载均衡组列表
     * 
     * @return String
     */
    public String getLoadBalanceGroups() {
        return NONE;
    }

    /**
     * 加载负载均衡生效策略模板
     * 
     * @return
     */
    public String getLoadBalPolicyTplList() {
        JSONObject ret = new JSONObject();

        ret.put("data", cmcLoadBalanceService.getLoadBalPolicyTplList());
        writeDataToAjax(ret);
        return NONE;
    }

    /**
     * 进入负载均衡生效策略模板添加界面
     * 
     * @return
     */
    public String enterLoadBalPolicyTplAddPage() {
        List<CmcLoadBalPolicyTpl> balPolicyTpls = cmcLoadBalanceService.getLoadBalPolicyTplList();
        List<String> strings = new ArrayList<String>();
        for (CmcLoadBalPolicyTpl cmcLoadBalPolicyTpl : balPolicyTpls) {
            strings.add(cmcLoadBalPolicyTpl.getPolicyName() + "");
        }
        JSONObject ret = new JSONObject();
        ret.put("data", strings);
        request.setAttribute("policyNames", ret);
        return SUCCESS;
    }

    /**
     * 进入设备的负载均衡时间策略修改页面
     * 
     * @return
     */
    public String enterLoadBalPolicyModifyPage() {
        CmcLoadBalPolicy balPolicy = cmcLoadBalanceService.getLoadBalPolicyById(this.policyId);
        if (balPolicy != null) {
            this.entityId = balPolicy.getEntityId();
            this.policyType = balPolicy.getPolicyType();
            List<CmcLoadBalBasicRule> rules = balPolicy.getRules();
            if (this.policyType.equals(CmcLoadBalBasicRuleTpl.RULE_DISABLED_PERIOD)) {
                // List<String> sections = new ArrayList<String>();
                String sectionsStr = "";
                for (CmcLoadBalBasicRule r : rules) {
                    sectionsStr = sectionsStr + "," + r.getStartTime() + "-" + r.getEndTime();
                }
                sectionsStr = sectionsStr.substring(1);
                this.disSections = sectionsStr;
            }
        }

        return SUCCESS;
    }

    /**
     * 进入设备的负载均衡时间策略添加页面
     * 
     * @return
     */
    public String enterLoadBalPolicyAddPage() {

        return SUCCESS;
    }

    /**
     * 添加设备的负载均衡时间策略
     * 
     * @return
     */
    public String addLoadBalPolicy() {
        JSONObject jObj = new JSONObject();
        try {
            CmcLoadBalPolicy policy = new CmcLoadBalPolicy();
            policy.setEntityId(this.entityId);
            List<CmcLoadBalBasicRule> rules = policy.getRules();
            if (this.policyType.equals(CmcLoadBalBasicRuleTpl.RULE_DISABLED_PERIOD)) {
                String[] strArray = disSections.split("@");
                for (String s : strArray) {
                    String[] times = s.split("-");
                    String startTime = times[0];
                    String endTime = times[1];
                    CmcLoadBalBasicRule r = new CmcLoadBalBasicRule();
                    r.setDocsLoadBalBasicRuleEnable(this.policyType);
                    r.setDocsLoadBalBasicRuleDisStart(Long.valueOf(CmcLoadBalBasicRuleTpl.toDisStart(startTime)));
                    r.setDocsLoadBalBasicRuleDisPeriod(Long.valueOf(CmcLoadBalBasicRuleTpl.toDisPeriod(startTime,
                            endTime)));
                    r.setEntityId(this.entityId);
                    rules.add(r);
                }
            } else {
                CmcLoadBalBasicRule r = new CmcLoadBalBasicRule();
                r.setDocsLoadBalBasicRuleEnable(this.policyType);
                r.setEntityId(this.entityId);
                rules.add(r);
            }
            cmcLoadBalanceService.txAddCmcLoadBalPolicy(policy);
            jObj.put("success", true);
        } catch (LoadBalException e) {
            jObj.put("success", false);
            jObj.put("msg", e.getMessage());
            logger.error("", e);
        }
        writeDataToAjax(jObj);
        return NONE;
    }

    /**
     * 添加负载均衡生效策略模板
     * 
     * @return
     */
    public String addLoadBalPolicyTpl() {
        JSONObject jObj = new JSONObject();
        try {
            CmcLoadBalPolicyTpl policyTpl = new CmcLoadBalPolicyTpl();
            policyTpl.setPolicyName(this.policyName);
            List<CmcLoadBalBasicRuleTpl> rules = policyTpl.getRules();
            if (this.policyType.equals(CmcLoadBalBasicRuleTpl.RULE_DISABLED_PERIOD)) {
                String[] strArray = disSections.split("@");
                for (String s : strArray) {
                    String[] times = s.split("-");
                    String startTime = times[0];
                    String endTime = times[1];
                    CmcLoadBalBasicRuleTpl r = new CmcLoadBalBasicRuleTpl();
                    r.setDocsLoadBalBasicRuleEnable(this.policyType);
                    r.setDocsLoadBalBasicRuleDisStart(CmcLoadBalBasicRuleTpl.toDisStart(startTime));
                    r.setDocsLoadBalBasicRuleDisPeriod(CmcLoadBalBasicRuleTpl.toDisPeriod(startTime, endTime));
                    rules.add(r);
                }
            } else {
                CmcLoadBalBasicRuleTpl r = new CmcLoadBalBasicRuleTpl();
                r.setDocsLoadBalBasicRuleEnable(this.policyType);
                rules.add(r);
            }
            cmcLoadBalanceService.addLoadBalPolicyTpl(policyTpl);
            jObj.put("success", true);
        } catch (Exception e) {
            jObj.put("success", false);
            logger.error("", e);
        }
        writeDataToAjax(jObj);
        return NONE;
    }

    /**
     * 进入策略模板修改页面
     * 
     * @return
     */
    public String enterLoadBalPolicyTplModifyPage() {
        CmcLoadBalPolicyTpl balPolicyTpl = cmcLoadBalanceService.getLoadBalPolicyTplById(this.policyTplId);
        this.policyName = balPolicyTpl.getPolicyName();
        this.policyType = balPolicyTpl.getPolicyType();
        List<CmcLoadBalBasicRuleTpl> rules = balPolicyTpl.getRules();
        if (this.policyType.equals(CmcLoadBalBasicRuleTpl.RULE_DISABLED_PERIOD)) {
            String sectionsStr = "";
            for (CmcLoadBalBasicRuleTpl r : rules) {
                sectionsStr = sectionsStr + "," + r.getDisStartTime() + "-" + r.getDisEndTime();
            }
            sectionsStr = sectionsStr.substring(1);
            this.disSections = sectionsStr;
        }
        /*
         * 传回所有策略模板名
         */
        List<CmcLoadBalPolicyTpl> balPolicyTpls = cmcLoadBalanceService.getLoadBalPolicyTplList();
        List<String> strings = new ArrayList<String>();
        for (CmcLoadBalPolicyTpl cmcLoadBalPolicyTpl : balPolicyTpls) {
            strings.add(cmcLoadBalPolicyTpl.getPolicyName() + "");
        }
        JSONObject ret = new JSONObject();
        ret.put("data", strings);
        request.setAttribute("policyNames", ret);

        return SUCCESS;
    }

    /**
     * 修改策略模板
     * 
     * @return
     */
    public String modifyLoadBalPolicyTpl() {
        JSONObject jObj = new JSONObject();
        try {
            CmcLoadBalPolicyTpl balPolicyTpl = new CmcLoadBalPolicyTpl();
            balPolicyTpl.setPolicyTplId(this.policyTplId);
            balPolicyTpl.setPolicyName(this.policyName);
            List<CmcLoadBalBasicRuleTpl> rules = balPolicyTpl.getRules();
            if (this.policyType.equals(CmcLoadBalBasicRuleTpl.RULE_DISABLED_PERIOD)) {
                String[] strArray = disSections.split("@");
                for (String s : strArray) {
                    String[] times = s.split("-");
                    String startTime = times[0];
                    String endTime = times[1];
                    CmcLoadBalBasicRuleTpl r = new CmcLoadBalBasicRuleTpl();
                    r.setDocsLoadBalBasicRuleEnable(this.policyType);
                    r.setDocsLoadBalBasicRuleDisStart(CmcLoadBalBasicRuleTpl.toDisStart(startTime));
                    r.setDocsLoadBalBasicRuleDisPeriod(CmcLoadBalBasicRuleTpl.toDisPeriod(startTime, endTime));
                    rules.add(r);
                }
            } else {
                CmcLoadBalBasicRuleTpl r = new CmcLoadBalBasicRuleTpl();
                r.setDocsLoadBalBasicRuleEnable(this.policyType);
                rules.add(r);
            }
            cmcLoadBalanceService.modifyLoadBalPolicyTpl(balPolicyTpl);
            jObj.put("success", true);
        } catch (Exception e) {
            jObj.put("success", false);
            logger.error("", e);
        }
        writeDataToAjax(jObj);
        return NONE;
    }

    /**
     * 进入负载均衡策略列表界面
     * 
     * @return
     */
    public String enterLoadBalPolicyListPage() {
        try {
            cmcLoadBalanceService.txSyncLoadBalPolicy(this.cmcId);
        } catch (Exception e) {
            logger.error("", e);
        }
        CmcLoadBalPolicy cmcLoadBalPolicy = this.cmcLoadBalanceService.getLoadBalanceTimePolicy(cmcId);
        if (cmcLoadBalPolicy != null) {
            this.policyId = cmcLoadBalPolicy.getPolicyId();
        }
        this.entityId = this.cmcLoadBalanceService.getEntityIdByCmcId(cmcId);
        return SUCCESS;
    }

    /**
     * 获取设备的负载均衡时间策略
     * 
     * @return
     */
    public String getLoadBalPolicyList() {
        JSONObject ret = new JSONObject();
        ret.put("data", cmcLoadBalanceService.getLoadBalPolicyListByEntityId(this.entityId));
        writeDataToAjax(ret);
        return NONE;
    }

    /**
     * 修改负载均衡时间策略
     * 
     * @return
     */
    public String modifyLoadBalPolicy() {
        JSONObject jObj = new JSONObject();
        try {
            CmcLoadBalPolicy balPolicy = new CmcLoadBalPolicy();
            balPolicy.setPolicyId(this.policyId);
            balPolicy.setEntityId(this.entityId);
            List<CmcLoadBalBasicRule> rules = balPolicy.getRules();
            if (this.policyType.equals(CmcLoadBalBasicRuleTpl.RULE_DISABLED_PERIOD)) {
                String[] strArray = disSections.split("@");
                for (String s : strArray) {
                    String[] times = s.split("-");
                    String startTime = times[0];
                    String endTime = times[1];
                    CmcLoadBalBasicRule r = new CmcLoadBalBasicRule();
                    r.setDocsLoadBalBasicRuleEnable(this.policyType);
                    r.setDocsLoadBalBasicRuleDisStart(Long.valueOf(CmcLoadBalBasicRuleTpl.toDisStart(startTime)));
                    r.setDocsLoadBalBasicRuleDisPeriod(Long.valueOf(CmcLoadBalBasicRuleTpl.toDisPeriod(startTime,
                            endTime)));
                    rules.add(r);
                }
            } else {
                CmcLoadBalBasicRule r = new CmcLoadBalBasicRule();
                r.setDocsLoadBalBasicRuleEnable(this.policyType);
                rules.add(r);
            }
            cmcLoadBalanceService.txModifyLoadBalPolicy(balPolicy);
            jObj.put("success", true);
        } catch (LoadBalException e) {
            jObj.put("msg", e.getMessage());
            jObj.put("success", false);
        } catch (Exception e) {
            jObj.put("success", false);
            logger.error("", e);
        }
        writeDataToAjax(jObj);
        return NONE;
    }

    /**
     * 设置Ccmts负载均衡时间策略
     * 
     * @return
     */
    public String modifyCcmtsPolicy() {
        JSONObject jObj = new JSONObject();
        try {
            cmcLoadBalanceService.modifyCcmtsPolicy(this.cmcId, this.policyId);
            jObj.put("success", true);
        } catch (Exception e) {
            jObj.put("success", false);
            logger.error("", e);
        }
        writeDataToAjax(jObj);
        return NONE;
    }

    /**
     * 删除负载均衡时间策略
     * 
     * @return
     */
    public String deleteLoadBalPolicy() {
        JSONObject jObj = new JSONObject();
        try {
            cmcLoadBalanceService.deleteLoadBalPolicy(this.policyId);
            jObj.put("success", true);
        } catch (Exception e) {
            jObj.put("success", false);
            logger.error("", e);
        }
        writeDataToAjax(jObj);
        return NONE;
    }

    public String deleteLoadBalPolicyTpl() {
        JSONObject jObj = new JSONObject();
        try {
            cmcLoadBalanceService.deleteLoadBalPolicyTpl(this.policyTplId);
            jObj.put("success", true);
        } catch (Exception e) {
            jObj.put("success", false);
            logger.error("", e);
        }
        writeDataToAjax(jObj);
        return NONE;
    }

    public CmcLoadBalanceService getCmcLoadBalanceService() {
        return cmcLoadBalanceService;
    }

    public void setCmcLoadBalanceService(CmcLoadBalanceService cmcLoadBalanceService) {
        this.cmcLoadBalanceService = cmcLoadBalanceService;
    }

    public Long getCmcId() {
        return cmcId;
    }

    public void setCmcId(Long cmcId) {
        this.cmcId = cmcId;
    }

    /**
     * @return the loadBalanceGlobalCfg
     */
    public CmcLoadBalCfg getLoadBalanceGlobalCfg() {
        return loadBalanceGlobalCfg;
    }

    /**
     * @param loadBalanceGlobalCfg
     *            the loadBalanceGlobalCfg to set
     */
    public void setLoadBalanceGlobalCfg(CmcLoadBalCfg loadBalanceGlobalCfg) {
        this.loadBalanceGlobalCfg = loadBalanceGlobalCfg;
    }

    public String getPolicyName() {
        return policyName;
    }

    public void setPolicyName(String policyName) {
        this.policyName = policyName;
    }

    public Integer getPolicyType() {
        return policyType;
    }

    public void setPolicyType(Integer policyType) {
        this.policyType = policyType;
    }

    public String getDisSections() {
        return disSections;
    }

    public void setDisSections(String disSections) {
        this.disSections = disSections;
    }

    public Long getPolicyTplId() {
        return policyTplId;
    }

    public void setPolicyTplId(Long policyTplId) {
        this.policyTplId = policyTplId;
    }

    /**
     * @return the cmcChannelService
     */
    public CmcChannelService getCmcChannelService() {
        return cmcChannelService;
    }

    /**
     * @param cmcChannelService
     *            the cmcChannelService to set
     */
    public void setCmcChannelService(CmcChannelService cmcChannelService) {
        this.cmcChannelService = cmcChannelService;
    }

    /**
     * @return the rangeDetail
     */
    public String getRangeDetail() {
        return rangeDetail;
    }

    /**
     * @param rangeDetail
     *            the rangeDetail to set
     */
    public void setRangeDetail(String rangeDetail) {
        this.rangeDetail = rangeDetail;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public Long getPolicyId() {
        return policyId;
    }

    public void setPolicyId(Long policyId) {
        this.policyId = policyId;
    }

    /**
     * @return the groupName
     */
    public String getGroupName() {
        return groupName;
    }

    /**
     * @param groupName
     *            the groupName to set
     */
    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    /**
     * @return the groupUpchannel
     */
    public String getGroupUpchannel() {
        return groupUpchannel;
    }

    /**
     * @param groupUpchannel
     *            the groupUpchannel to set
     */
    public void setGroupUpchannel(String groupUpchannel) {
        this.groupUpchannel = groupUpchannel;
    }

    /**
     * @return the groupDownchannel
     */
    public String getGroupDownchannel() {
        return groupDownchannel;
    }

    /**
     * @param groupDownchannel
     *            the groupDownchannel to set
     */
    public void setGroupDownchannel(String groupDownchannel) {
        this.groupDownchannel = groupDownchannel;
    }

    /**
     * @return the groupRanges
     */
    public String[] getGroupRanges() {
        return groupRanges;
    }

    /**
     * @param groupRanges
     *            the groupRanges to set
     */
    public void setGroupRanges(String[] groupRanges) {
        this.groupRanges = groupRanges;
    }

    /**
     * @return the pageAction
     */
    public Integer getPageAction() {
        return pageAction;
    }

    /**
     * @param pageAction
     *            the pageAction to set
     */
    public void setPageAction(Integer pageAction) {
        this.pageAction = pageAction;
    }

    /**
     * @return the grpId
     */
    public Long getGrpId() {
        return grpId;
    }

    /**
     * @param grpId
     *            the grpId to set
     */
    public void setGrpId(Long grpId) {
        this.grpId = grpId;
    }

    /**
     * @return the channelIndexs
     */
    public Long[] getChannelIndexs() {
        return channelIndexs;
    }

    /**
     * @param channelIndexs
     *            the channelIndexs to set
     */
    public void setChannelIndexs(Long[] channelIndexs) {
        this.channelIndexs = channelIndexs;
    }

    /**
     * @return the topLoadBalConfigDbcInitAtdma
     */
    public Integer getTopLoadBalConfigDbcInitAtdma() {
        return topLoadBalConfigDbcInitAtdma;
    }

    /**
     * @param topLoadBalConfigDbcInitAtdma
     *            the topLoadBalConfigDbcInitAtdma to set
     */
    public void setTopLoadBalConfigDbcInitAtdma(Integer topLoadBalConfigDbcInitAtdma) {
        this.topLoadBalConfigDbcInitAtdma = topLoadBalConfigDbcInitAtdma;
    }

    /**
     * @return the topLoadBalConfigDiffThresh
     */
    public Long getTopLoadBalConfigDiffThresh() {
        return topLoadBalConfigDiffThresh;
    }

    /**
     * @param topLoadBalConfigDiffThresh
     *            the topLoadBalConfigDiffThresh to set
     */
    public void setTopLoadBalConfigDiffThresh(Long topLoadBalConfigDiffThresh) {
        this.topLoadBalConfigDiffThresh = topLoadBalConfigDiffThresh;
    }

    /**
     * @return the topLoadBalConfigEnable
     */
    public Integer getTopLoadBalConfigEnable() {
        return topLoadBalConfigEnable;
    }

    /**
     * @param topLoadBalConfigEnable
     *            the topLoadBalConfigEnable to set
     */
    public void setTopLoadBalConfigEnable(Integer topLoadBalConfigEnable) {
        this.topLoadBalConfigEnable = topLoadBalConfigEnable;
    }

    /**
     * @return the topLoadBalConfigInterval
     */
    public Long getTopLoadBalConfigInterval() {
        return topLoadBalConfigInterval;
    }

    /**
     * @param topLoadBalConfigInterval
     *            the topLoadBalConfigInterval to set
     */
    public void setTopLoadBalConfigInterval(Long topLoadBalConfigInterval) {
        this.topLoadBalConfigInterval = topLoadBalConfigInterval;
    }

    /**
     * @return the topLoadBalConfigMaxMoves
     */
    public Long getTopLoadBalConfigMaxMoves() {
        return topLoadBalConfigMaxMoves;
    }

    /**
     * @param topLoadBalConfigMaxMoves
     *            the topLoadBalConfigMaxMoves to set
     */
    public void setTopLoadBalConfigMaxMoves(Long topLoadBalConfigMaxMoves) {
        this.topLoadBalConfigMaxMoves = topLoadBalConfigMaxMoves;
    }

    /**
     * @return the topLoadBalConfigMethod
     */
    public Integer getTopLoadBalConfigMethod() {
        return topLoadBalConfigMethod;
    }

    /**
     * @param topLoadBalConfigMethod
     *            the topLoadBalConfigMethod to set
     */
    public void setTopLoadBalConfigMethod(Integer topLoadBalConfigMethod) {
        this.topLoadBalConfigMethod = topLoadBalConfigMethod;
    }

    /**
     * @return the topLoadBalConfigNumPeriod
     */
    public Integer getTopLoadBalConfigNumPeriod() {
        return topLoadBalConfigNumPeriod;
    }

    /**
     * @param topLoadBalConfigNumPeriod
     *            the topLoadBalConfigNumPeriod to set
     */
    public void setTopLoadBalConfigNumPeriod(Integer topLoadBalConfigNumPeriod) {
        this.topLoadBalConfigNumPeriod = topLoadBalConfigNumPeriod;
    }

    /**
     * @return the topLoadBalConfigPeriod
     */
    public Long getTopLoadBalConfigPeriod() {
        return topLoadBalConfigPeriod;
    }

    /**
     * @param topLoadBalConfigPeriod
     *            the topLoadBalConfigPeriod to set
     */
    public void setTopLoadBalConfigPeriod(Long topLoadBalConfigPeriod) {
        this.topLoadBalConfigPeriod = topLoadBalConfigPeriod;
    }

    /**
     * @return the topLoadBalConfigTriggerThresh
     */
    public Long getTopLoadBalConfigTriggerThresh() {
        return topLoadBalConfigTriggerThresh;
    }

    /**
     * @param topLoadBalConfigTriggerThresh
     *            the topLoadBalConfigTriggerThresh to set
     */
    public void setTopLoadBalConfigTriggerThresh(Long topLoadBalConfigTriggerThresh) {
        this.topLoadBalConfigTriggerThresh = topLoadBalConfigTriggerThresh;
    }

    /**
     * @return the docsLoadBalGrpId
     */
    public Long getDocsLoadBalGrpId() {
        return docsLoadBalGrpId;
    }

    /**
     * @param docsLoadBalGrpId
     *            the docsLoadBalGrpId to set
     */
    public void setDocsLoadBalGrpId(Long docsLoadBalGrpId) {
        this.docsLoadBalGrpId = docsLoadBalGrpId;
    }

    /**
     * @return the macRanges
     */
    public String[] getMacRanges() {
        return macRanges;
    }

    /**
     * @param macRanges
     *            the macRanges to set
     */
    public void setMacRanges(String[] macRanges) {
        this.macRanges = macRanges;
    }

    /**
     * @return the topLoadBalExcludeCmIndex
     */
    public Long getTopLoadBalExcludeCmIndex() {
        return topLoadBalExcludeCmIndex;
    }

    /**
     * @param topLoadBalExcludeCmIndex
     *            the topLoadBalExcludeCmIndex to set
     */
    public void setTopLoadBalExcludeCmIndex(Long topLoadBalExcludeCmIndex) {
        this.topLoadBalExcludeCmIndex = topLoadBalExcludeCmIndex;
    }

    /**
     * @return the topLoadBalConfigCmcIndex
     */
    public Long getTopLoadBalConfigCmcIndex() {
        return topLoadBalConfigCmcIndex;
    }

    /**
     * @param topLoadBalConfigCmcIndex
     *            the topLoadBalConfigCmcIndex to set
     */
    public void setTopLoadBalConfigCmcIndex(Long topLoadBalConfigCmcIndex) {
        this.topLoadBalConfigCmcIndex = topLoadBalConfigCmcIndex;
    }

    /**
     * @return the topLoadBalConfigDccInitAtdma
     */
    public Integer getTopLoadBalConfigDccInitAtdma() {
        return topLoadBalConfigDccInitAtdma;
    }

    /**
     * @param topLoadBalConfigDccInitAtdma
     *            the topLoadBalConfigDccInitAtdma to set
     */
    public void setTopLoadBalConfigDccInitAtdma(Integer topLoadBalConfigDccInitAtdma) {
        this.topLoadBalConfigDccInitAtdma = topLoadBalConfigDccInitAtdma;
    }

    /**
     * @return the topLoadBalConfigDccInitScdma
     */
    public Integer getTopLoadBalConfigDccInitScdma() {
        return topLoadBalConfigDccInitScdma;
    }

    /**
     * @param topLoadBalConfigDccInitScdma
     *            the topLoadBalConfigDccInitScdma to set
     */
    public void setTopLoadBalConfigDccInitScdma(Integer topLoadBalConfigDccInitScdma) {
        this.topLoadBalConfigDccInitScdma = topLoadBalConfigDccInitScdma;
    }

    /**
     * @return the topLoadBalConfigDbcInitScdma
     */
    public Integer getTopLoadBalConfigDbcInitScdma() {
        return topLoadBalConfigDbcInitScdma;
    }

    /**
     * @param topLoadBalConfigDbcInitScdma
     *            the topLoadBalConfigDbcInitScdma to set
     */
    public void setTopLoadBalConfigDbcInitScdma(Integer topLoadBalConfigDbcInitScdma) {
        this.topLoadBalConfigDbcInitScdma = topLoadBalConfigDbcInitScdma;
    }

    /**
     * @return the cmcLoadBalanceGroup
     */
    public JSONObject getCmcLoadBalanceGroup() {
        return cmcLoadBalanceGroup;
    }

    /**
     * @param cmcLoadBalanceGroup
     *            the cmcLoadBalanceGroup to set
     */
    public void setCmcLoadBalanceGroup(JSONObject cmcLoadBalanceGroup) {
        this.cmcLoadBalanceGroup = cmcLoadBalanceGroup;
    }

    /**
     * @return the cmcService
     */
    public CmcService getCmcService() {
        return cmcService;
    }

    /**
     * @param cmcService
     *            the cmcService to set
     */
    public void setCmcService(CmcService cmcService) {
        this.cmcService = cmcService;
    }

    public String getAddedGroupChannelsStr() {
        return addedGroupChannelsStr;
    }

    public void setAddedGroupChannelsStr(String addedGroupChannelsStr) {
        this.addedGroupChannelsStr = addedGroupChannelsStr;
    }

    public String getDeletedGroupChannelsStr() {
        return deletedGroupChannelsStr;
    }

    public void setDeletedGroupChannelsStr(String deletedGroupChannelsStr) {
        this.deletedGroupChannelsStr = deletedGroupChannelsStr;
    }

    public String getAddedGroupRangesStr() {
        return addedGroupRangesStr;
    }

    public void setAddedGroupRangesStr(String addedGroupRangesStr) {
        this.addedGroupRangesStr = addedGroupRangesStr;
    }

    public String getDeletedGroupRangesStr() {
        return deletedGroupRangesStr;
    }

    public void setDeletedGroupRangesStr(String deletedGroupRangesStr) {
        this.deletedGroupRangesStr = deletedGroupRangesStr;
    }

    public String getMacRangesStr() {
        return macRangesStr;
    }

    public void setMacRangesStr(String macRangesStr) {
        this.macRangesStr = macRangesStr;
    }

    /**
     * @return the productType
     */
    public Integer getProductType() {
        return productType;
    }

    /**
     * @param productType
     *            the productType to set
     */
    public void setProductType(Integer productType) {
        this.productType = productType;
    }

    public String getMacDisplayFormat() {
        return macDisplayFormat;
    }

    public void setMacDisplayFormat(String macDisplayFormat) {
        this.macDisplayFormat = macDisplayFormat;
    }

	public CmcAttribute getCmcAttribute() {
		return cmcAttribute;
	}

	public void setCmcAttribute(CmcAttribute cmcAttribute) {
		this.cmcAttribute = cmcAttribute;
	}

}
