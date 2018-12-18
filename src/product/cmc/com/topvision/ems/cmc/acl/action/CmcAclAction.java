package com.topvision.ems.cmc.acl.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.topvision.ems.cmc.acl.facade.domain.CmcAclDefAction;
import com.topvision.ems.cmc.acl.facade.domain.CmcAclInfo;
import com.topvision.ems.cmc.acl.service.CmcAclService;
import com.topvision.ems.cmc.ccmts.facade.domain.CmcAttribute;
import com.topvision.ems.cmc.ccmts.service.CmcService;
import com.topvision.ems.devicesupport.version.util.DeviceFuctionSupport;
import com.topvision.framework.common.MacUtils;
import com.topvision.framework.web.struts2.BaseAction;
import com.topvision.platform.domain.UserContext;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 创建管理CC设备ACL功能
 * 
 * @author lzs
 * @created @2013-4-20-下午03:21:49
 * 
 */
@Controller("cmcAclAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class CmcAclAction extends BaseAction {
    private static final long serialVersionUID = -6445289650598654661L;
    @SuppressWarnings("unused")
    private final static Logger logger = LoggerFactory.getLogger(CmcAclAction.class);
    @Resource(name = "cmcAclService")
    private CmcAclService cmcAclService;
    @Resource(name = "cmcService")
    private CmcService cmcService;
    private Integer productType;
    private CmcAttribute cmcAttribute;
    private Boolean isNewAclActionMask;
    private Long entityType;
    /*
     * 以下为页面相互数据对象
     */
    /**
     * ACL的ID，唯一性索引
     */
    private Integer aclID = null;
    /**
     * 一个ACL数据的封装对象
     */
    private CmcAclInfo aclInfo = null;
    /**
     * 封装一个放置点的默认动作
     */
    private CmcAclDefAction defAct = null;
    /**
     * 放置点：0 是未安装 uplinkIngress(1),uplinkEgress(2),cableEgress(3),cableIngress(4),
     */
    private Integer position = null;

    /**
     * CC 设备ID，从页面传递过来
     */
    private Long cmcId = null;

    /**
     * 查看一个ACL信息，如果不存在此ACL，则返回空页面，以供新建
     * 
     * @return
     */
    public String loadSingleAcl() {
        cmcAttribute = cmcService.getCmcAttributeByCmcId(cmcId);
        if (aclID != null) {
            aclInfo = cmcAclService.getSingleAclInfo(cmcId, aclID);
            // add by fanzidong,需要在展示前格式化MAC地址
            UserContext uc = (UserContext) super.getSession().get(UserContext.KEY);
            String macRule = uc.getMacDisplayStyle();
            String formatedSrcMac = MacUtils.convertMacToDisplayFormat(aclInfo.getTopMatchlSrcMac(), macRule);
            aclInfo.setTopMatchlSrcMac(formatedSrcMac);
            String formatedDestMac = MacUtils.convertMacToDisplayFormat(aclInfo.getTopMatchDstMac(), macRule);
            aclInfo.setTopMatchDstMac(formatedDestMac);
            aclInfo.setTopMatchDstMacMask(MacUtils.convertMacToDisplayFormat(aclInfo.getTopMatchDstMacMask(), macRule));
            aclInfo.setTopMatchSrcMacMask(MacUtils.convertMacToDisplayFormat(aclInfo.getTopMatchSrcMacMask(), macRule));
            aclInfo.setTopActionMask(aclInfo.getTopActionMask(), isNewAclActionMask(cmcAttribute.getDolVersion()));
        } else {
            // 新建ACL,设置默认值
            aclInfo = new CmcAclInfo();
            Integer newAclID = cmcAclService.getOneNewAclId(cmcId);
            if (newAclID == null) {
                /**
                 * 0 代表已经不能再创建ACL
                 */
                aclInfo.setTopCcmtsAclListIndex(0);
            } else {

                aclInfo.setTopCcmtsAclListIndex(newAclID);
                aclInfo.setTopCcmtsAclPrio(5);

                aclInfo.setTopMatchSrcPort(65536);
                aclInfo.setTopMatchDstPort(65536);
                aclInfo.setTopMatchVlanId(4095);
                aclInfo.setTopMatchVlanCos(8);
                aclInfo.setTopMatchEtherType(0);
                aclInfo.setTopMatchIpProtocol(256);
                aclInfo.setTopMatchDscp(64);
            }
        }
        this.writeDataToAjax(JSONObject.fromObject(aclInfo));

        return NONE;
    }

    /**
     * 添加一个新的ACL。
     * 
     * @return
     */
    public String addAcl() {
        JSONObject json = new JSONObject();
        cmcAttribute = cmcService.getCmcAttributeByCmcId(cmcId);
        if (aclInfo != null) {
            // 需要将MAC地址及MAC地址掩码的格式转成标准的6段式以冒号隔开
            aclInfo.setTopMatchDstMac(MacUtils.convertToMaohaoFormat(aclInfo.getTopMatchDstMac()));
            aclInfo.setTopMatchDstMacMask(MacUtils.convertToMaohaoFormat(aclInfo.getTopMatchDstMacMask()));
            aclInfo.setTopMatchlSrcMac(MacUtils.convertToMaohaoFormat(aclInfo.getTopMatchlSrcMac()));
            aclInfo.setTopMatchSrcMacMask(MacUtils.convertToMaohaoFormat(aclInfo.getTopMatchSrcMacMask()));
            aclInfo.convertToMibDefaultValue();
            if (isNewAclActionMask(cmcAttribute.getDolVersion())) {
                // 如果是V2290以后的版本，需要将位图前后置换
                String actionMaskBuffer = convertActionMaskBuffer(aclInfo.getActionMaskBuffer().toString());
                aclInfo.setTopActionMask(toHexString(actionMaskBuffer));
                
                String installBuffer = convertActionMaskBuffer(aclInfo.getInstallPosition().toString());
                aclInfo.setTopInstallPosition(toHexString(installBuffer));
            }
            cmcAclService.addSingleAclInfo(cmcId, aclInfo);
            json.put("success", "true");
            this.writeDataToAjax(json);
        }
        return NONE;
    }

    /**
     * 修改一个acl,1,未安装过的，可以修改匹配动作等，2，已经安装过的，只能修改放置点
     * 
     * @return
     */
    public String modifyAcl() {
        JSONObject json = new JSONObject();
        cmcAttribute = cmcService.getCmcAttributeByCmcId(cmcId);
        if (aclInfo != null) {
            // 需要将MAC地址及MAC地址掩码的格式转成标准的6段式以冒号隔开
            aclInfo.setTopMatchDstMac(MacUtils.convertToMaohaoFormat(aclInfo.getTopMatchDstMac()));
            aclInfo.setTopMatchDstMacMask(MacUtils.convertToMaohaoFormat(aclInfo.getTopMatchDstMacMask()));
            aclInfo.setTopMatchlSrcMac(MacUtils.convertToMaohaoFormat(aclInfo.getTopMatchlSrcMac()));
            aclInfo.setTopMatchSrcMacMask(MacUtils.convertToMaohaoFormat(aclInfo.getTopMatchSrcMacMask()));
            aclInfo.convertToMibDefaultValue();
            if (isNewAclActionMask(cmcAttribute.getDolVersion())) {
                // 如果是V2290以后的版本，需要将位图前后置换
                String actionMaskBuffer = convertActionMaskBuffer(aclInfo.getActionMaskBuffer().toString());
                aclInfo.setTopActionMask(toHexString(actionMaskBuffer));

                String installBuffer = convertActionMaskBuffer(aclInfo.getInstallPosition().toString());
                aclInfo.setTopInstallPosition(toHexString(installBuffer));
            }
            cmcAclService.modifySingleAclInfo(cmcId, aclInfo);
            json.put("success", "true");
            this.writeDataToAjax(json);
        }
        return NONE;
    }

    /**
     * 删除一个ACL
     * 
     * @return
     */
    public String deleteAcl() {
        if (aclID != null) {
            boolean isSuccess = cmcAclService.deleteSingleAclInfo(cmcId, aclID);

            JSONObject json = new JSONObject();
            json.put("success", String.valueOf(isSuccess));
            this.writeDataToAjax(json);
        }
        return NONE;
    }

    /**
     * 修改一个放置点的默认动作
     * 
     * @return
     */
    public String modifyDefAct() {
        if (this.defAct != null) {
            int position = defAct.getTopAclPositionIndex();
            int act = defAct.getTopPositionDefAction();
            cmcAclService.enablePositionDefAct(this.cmcId, position, act == 1);
        }
        JSONObject json = new JSONObject();
        json.put("success", "true");
        this.writeDataToAjax(json);
        return NONE;
    }

    /**
     * 查询所有ACL，然后根据放置点过滤，返回一个放置点的ACL position is null ,代表第一次打开页面，从设备采集全部数据，更新到数据库。返回未安装的acl列表
     * position is 1,2,3,4 代表四个放置点，直接从数据库查询 position is 0 ,代表未安装，直接从数据库查询。
     * 
     * @return
     */
    public String loadAllAclList() {
        cmcAttribute = cmcService.getCmcAttributeByCmcId(cmcId);
        if (position == null) {
            // cmcAclService.refreshAllAclInfo(cmcId);
            position = 0;

        }

        List<CmcAclInfo> returnAclList = new ArrayList<CmcAclInfo>();

        /**
         * 因为CC已经废弃放置点的默认动作这个功能，网管代码暂时注释获取默认动作的代码。 lzs 20130527
         * 一段时间后，如果不在重新开发默认放置点的功能，可以将service,db,mib等相关代码删除。
         */
        // CmcAclDefAction defAct = cmcAclService.getAclPositionDefInfo(cmcId, position);
        // if (defAct != null) {
        // CmcAclInfo def = new CmcAclInfo();
        // def.setDefActPosion(defAct.getTopAclPositionIndex());
        // def.setDefAct(defAct.getTopPositionDefAction());
        // returnAclList.add(def);
        // }

        List<CmcAclInfo> allAclList = cmcAclService.getOnePositionAclList(cmcId, position);
        if (allAclList != null) {
            returnAclList.addAll(allAclList);
        }
        // add by fanzidong,需要在展示前格式化MAC地址
        UserContext uc = (UserContext) super.getSession().get(UserContext.KEY);
        String macRule = uc.getMacDisplayStyle();
        for (CmcAclInfo cmcAclInfo : returnAclList) {
            String formatedSrcMac = MacUtils.convertMacToDisplayFormat(cmcAclInfo.getTopMatchlSrcMac(), macRule);
            cmcAclInfo.setTopMatchlSrcMac(formatedSrcMac);
            String formatedDestMac = MacUtils.convertMacToDisplayFormat(cmcAclInfo.getTopMatchDstMac(), macRule);
            cmcAclInfo.setTopMatchDstMac(formatedDestMac);
            cmcAclInfo.setTopActionMask(cmcAclInfo.getTopActionMask(),
                    isNewAclActionMask(cmcAttribute.getDolVersion()));
        }
        JSONObject json = new JSONObject();
        json.put("data", JSONArray.fromObject(returnAclList));
        json.put("rowCount", returnAclList.size());
        this.writeDataToAjax(json);
        return NONE;
    }

    /**
     * 跳转到查看所有ACL的页面
     * 
     * @return
     */
    public String viewAllAclList() {
        setCmcAttribute(cmcService.getCmcAttributeByCmcId(cmcId));
        cmcAttribute = cmcService.getCmcAttributeByCmcId(cmcId);
        entityType = cmcAttribute.getCmcDeviceStyle();
        isNewAclActionMask = isNewAclActionMask(cmcAttribute.getDolVersion());
        return SUCCESS;
    }

    /**
     * 获取指定放置点下的ACL数目
     * 
     * @return
     * @throws IOException
     */
    public String countPositionAcl() throws IOException {
        JSONObject json = new JSONObject();
        try {
            List<CmcAclInfo> allAclList = cmcAclService.getOnePositionAclList(cmcId, position);
            if (allAclList != null) {
                json.put("count", allAclList.size());
            } else {
                json.put("count", 0);
            }
        } catch (Exception e) {
            json.put("count", 0);
        }
        json.write(response.getWriter());
        return NONE;
    }

    /**
     * 从设备获取所有的Acl信息
     * 
     * @return
     */
    public String refreshAllCmcAcl() {
        cmcAclService.refreshAllAclInfo(cmcId);
        return NONE;
    }

    public CmcAclService getCmcAclService() {
        return cmcAclService;
    }

    public void setCmcAclService(CmcAclService cmcAclService) {
        this.cmcAclService = cmcAclService;
    }

    public Integer getAclID() {
        return aclID;
    }

    public void setAclID(Integer aclID) {
        this.aclID = aclID;
    }

    public CmcAclInfo getAclInfo() {
        return aclInfo;
    }

    public void setAclInfo(CmcAclInfo aclInfo) {
        this.aclInfo = aclInfo;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public Long getCmcId() {

        return cmcId;
    }

    public void setCmcId(Long cmcId) {

        this.cmcId = cmcId;
    }

    public CmcAclDefAction getDefAct() {
        return defAct;
    }

    public void setDefAct(CmcAclDefAction defAct) {
        this.defAct = defAct;
    }

    public CmcService getCmcService() {
        return cmcService;
    }

    public void setCmcService(CmcService cmcService) {
        this.cmcService = cmcService;
    }

    public Integer getProductType() {
        return productType;
    }

    public void setProductType(Integer productType) {
        this.productType = productType;
    }

    public CmcAttribute getCmcAttribute() {
        return cmcAttribute;
    }

    public void setCmcAttribute(CmcAttribute cmcAttribute) {
        this.cmcAttribute = cmcAttribute;
    }

    public Boolean getIsNewAclActionMask() {
        return isNewAclActionMask;
    }

    public void setIsNewAclActionMask(Boolean isNewAclActionMask) {
        this.isNewAclActionMask = isNewAclActionMask;
    }

    /**
     * 判断升级AclActionMask是否为修改后的CCMTS-11569
     * 
     * @param dolversion
     * @return
     */
    private boolean isNewAclActionMask(String dolVersion) {
        if (dolVersion != null) {
            if (DeviceFuctionSupport.compareVersion(dolVersion, "V2.2.9.0") >= 0) {
                return true;
            }
        }
        return false;
    }

    private String convertActionMaskBuffer(String actionMask) {
        String newActionMask = "";
        for (int i = actionMask.length(); i > 0; i--) {
            newActionMask = newActionMask + actionMask.substring(i - 1, i);
        }
        return newActionMask;

    }

    private String toHexString(String binStr) {
        StringBuffer sb = new StringBuffer();
        int m = Integer.parseInt(binStr, 2);
        sb = new StringBuffer(Integer.toHexString(m));
        while (sb.length() < 8) {
            sb.insert(sb.length(), "0");
        }
        sb = sb.insert(2, ":");
        sb = sb.insert(5, ":");
        sb = sb.insert(8, ":");
        return sb.toString().toUpperCase();
    }

    public Long getEntityType() {
        return entityType;
    }

    public void setEntityType(Long entityType) {
        this.entityType = entityType;
    }

}
