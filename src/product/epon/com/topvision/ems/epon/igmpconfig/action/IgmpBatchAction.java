/***********************************************************************
 * $Id: IgmpBatchAction.java,v1.0 2016年9月10日 上午10:12:16 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.igmpconfig.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.topvision.ems.epon.igmpconfig.IgmpConstants;
import com.topvision.ems.epon.igmpconfig.domain.IgmpCtcProfileGroupRela;
import com.topvision.ems.epon.igmpconfig.domain.IgmpVlanGroup;
import com.topvision.ems.epon.igmpconfig.domain.IgmpVlanInfo;
import com.topvision.ems.epon.igmpconfig.service.OltIgmpConfigService;
import com.topvision.ems.epon.olt.service.OltSniService;
import com.topvision.ems.message.CallbackableRequest;
import com.topvision.ems.message.LongRequestExecutorService;
import com.topvision.ems.message.Message;
import com.topvision.ems.message.MessagePusher;
import com.topvision.framework.exception.engine.SnmpException;
import com.topvision.framework.web.struts2.BaseAction;
import com.topvision.platform.zetaframework.util.Cell;
import com.topvision.platform.zetaframework.util.ExcelResolveException;
import com.topvision.platform.zetaframework.util.ExcelResolver;
import com.topvision.platform.zetaframework.util.FileUploadUtil;
import com.topvision.platform.zetaframework.util.Row;
import com.topvision.platform.zetaframework.util.RowHandler;
import com.topvision.platform.zetaframework.util.Validator;

/**
 * @author Bravin
 * @created @2016年9月10日-上午10:12:16
 *
 */
@Controller("igmpBatchAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class IgmpBatchAction extends BaseAction {
    private static final long serialVersionUID = 3815758048696840600L;
    @Autowired
    private OltIgmpConfigService oltIgmpConfigService;
    private String fileName;
    private String jconnectionId;
    private Long entityId;
    @Autowired
    private MessagePusher messagePusher;
    @Autowired
    private LongRequestExecutorService longRequestExecutorService;
    @Autowired
    private OltSniService oltSniService;
    private Integer igmpVersion;
    private List<Integer> groupIdList;
    private Integer profileId;

    public String showIgmpGroupImport() {
        return SUCCESS;
    }

    private void returnMessage(Object data) {
        Message message = new Message(IgmpConstants.IGMP_GROUP_CALLBACK_HANDLER);
        message.setJconnectID(jconnectionId);
        message.setData(data);
        messagePusher.sendMessage(message);
    }

    /**
     * 批量导入&配置 IGMP组播组
     * @return
     * @throws IOException
     */
    public String batchImportIgmpGroups() throws IOException {
        File file = FileUploadUtil.checkout(request, fileName);
        List<IgmpVlanInfo> vlanList = oltIgmpConfigService.getVlanInfoList(entityId);
        final List<Integer> vlanIdList = new ArrayList<Integer>();
        for (IgmpVlanInfo vlan : vlanList) {
            vlanIdList.add(vlan.getVlanId());
        }
        try {
            ExcelResolver.resolve(new RowHandler() {
                private boolean returnRowResult(Object data, Integer result) {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("result", result);
                    jsonObject.put("data", data);
                    returnMessage(jsonObject);
                    return CONTINUE;
                }

                @Override
                public Boolean handleRow(Row row) {
                    if (row.getRowNum() < 3) {
                        return CONTINUE;
                    }

                    IgmpVlanGroup groupInfo = new IgmpVlanGroup();
                    groupInfo.setEntityId(entityId);
                    try {
                        List<Cell> cells = row.getCells();
                        for (int i = 0; i < cells.size(); i++) {
                            /** 如果列大小超过预定的大小,则会抛出下标越界异常,故对这种列超限的情况无需特殊处理 */
                            Cell cell = cells.get(i);
                            String $value = cell.getContent().trim();
                            switch (i) {
                            case 0:
                                Integer vlanId = Integer.parseInt($value);
                                groupInfo.setVlanId(vlanId);
                                if (vlanId < 0 || vlanId > 4094) {
                                    return returnRowResult(groupInfo, IgmpConstants.VLANID_OVERFLOW);
                                }
                                if (!vlanIdList.contains(vlanId)) {
                                    return returnRowResult(groupInfo, IgmpConstants.VLANID_NOT_EXIST);
                                }
                                break;
                            case 1:
                                Integer gropuId = Integer.parseInt($value);
                                if (gropuId < 1 || gropuId > 2000) {
                                    return returnRowResult(groupInfo, IgmpConstants.GROUPID_OVERFLOW);
                                }
                                groupInfo.setGroupId(gropuId);
                                break;
                            case 2://组播组IP
                                groupInfo.setGroupIp($value);
                                if (!Validator.isIpV4($value)) {
                                    return returnRowResult(groupInfo, IgmpConstants.IP_ADDRESS_ERROR);
                                }
                                if (!isMulticast($value)) {
                                    return returnRowResult(groupInfo, IgmpConstants.MULTICAST_IPADDRESS_ERROR);
                                }
                                break;
                            case 3://组播组源IP
                                if (igmpVersion == IgmpConstants.IGMP_VERSION_V2) {//V2时必须为 0.0.0.0
                                    if (!"0.0.0.0".equals($value)) {
                                        return returnRowResult(groupInfo, IgmpConstants.SOURCEIP_NULL_ERROR);
                                    }
                                } else if (igmpVersion == IgmpConstants.IGMP_VERSION_V3) {
                                    if (!"0.0.0.0".equals($value)) {
                                        if (!Validator.isIpV4($value)) {
                                            return returnRowResult(groupInfo, IgmpConstants.SOURCE_IP_ERROR);
                                        }
                                    }
                                } else {
                                    if (!Validator.isIpV4($value)) {
                                        return returnRowResult(groupInfo, IgmpConstants.SOURCE_IP_ERROR);
                                    }
                                }
                                groupInfo.setGroupSrcIp($value);
                                break;
                            case 4:
                                if (!$value.matches("^[a-zA-Z\\d-_]{1,31}$")) {
                                    return returnRowResult(groupInfo, IgmpConstants.DESC_ERROR);
                                }
                                groupInfo.setGroupDesc($value);
                                break;
                            case 5:
                                if (!$value.matches("^[a-zA-Z\\d\\u4e00-\\u9fa5-_\\[\\]()\\/\\.:]{1,63}")) {
                                    return returnRowResult(groupInfo, IgmpConstants.ALIAS_ERROR);
                                }
                                groupInfo.setGroupName($value);
                                break;
                            case 6:
                                int maxbw = Integer.parseInt($value);
                                if (maxbw < 0 || maxbw > 1000000) {
                                    return returnRowResult(groupInfo, IgmpConstants.MAX_BW_ERROR);
                                }
                                groupInfo.setGroupMaxBW(Integer.parseInt($value));
                                break;
                            case 7:
                                if ("prejoin".equalsIgnoreCase($value)) {
                                    groupInfo.setJoinMode(IgmpConstants.PREJOIN);
                                } else if ("no prejoin".equalsIgnoreCase($value)) {
                                    groupInfo.setJoinMode(IgmpConstants.NO_PREJOIN);
                                } else {
                                    return returnRowResult(groupInfo, IgmpConstants.PREJOIN_ERROR);
                                }
                                break;
                            }
                        }
                        oltIgmpConfigService.batchAddVlanGroup(groupInfo);
                        returnRowResult(groupInfo, IgmpConstants.SUCCESS);
                    } catch (Exception e) {
                        if (e instanceof SnmpException) {
                            returnRowResult(groupInfo, IgmpConstants.IGMP_GROUP_ADD_ERROR);
                        } else {
                            returnRowResult(groupInfo, IgmpConstants.FORMAT_FAILD);
                        }
                        logger.error("", e);
                    }
                    return CONTINUE;
                }

            }, new FileInputStream(file));
            //oltIgmpConfigService.refreshVlanGroup(entityId);
            //因为组播组状态在全局Group表中维护,所以需要同时对全局Groupg表进行刷新
            returnMessage(IgmpConstants.REFRESH_GLOBAL_GROUP);
            oltIgmpConfigService.refreshGlobalGroup(entityId);
            writeDataToAjax(String.valueOf(IgmpConstants.SUCCESS));
        } catch (ExcelResolveException e) {
            writeDataToAjax(String.valueOf(IgmpConstants.EXCEL_ERROR));
        } catch (Exception e) {
            writeDataToAjax(String.valueOf(IgmpConstants.OTHER_ERROR));
        }
        return NONE;
    }

    private boolean isMulticast(String ip) {
        String[] ips = ip.split("\\.");
        int head = Integer.parseInt(ips[0]);
        int second = Integer.parseInt(ips[1]);
        int third = Integer.parseInt(ips[2]);
        /** OLT使用的有效组播范围是 224.0.1.0-239.255.255.255 */
        if (head == 224 && second == 0 && third < 1) {
            return false;
        }
        if (head >= 224 && head <= 239) {
            return true;
        }
        return false;
    }

    /**
     * 批量绑定组播组
     * @return
     */
    public String batchBindProfileGroup() {
        //groupIdList
        longRequestExecutorService.executeRequest(new CallbackableRequest(jconnectionId) {

            @Override
            protected void execute() {
                for (Integer groupId : groupIdList) {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("groupId", groupId);
                    try {
                        IgmpCtcProfileGroupRela profileGroup = new IgmpCtcProfileGroupRela();
                        profileGroup.setEntityId(entityId);
                        profileGroup.setProfileId(profileId);
                        profileGroup.setGroupId(groupId);
                        oltIgmpConfigService.addProfileGroupRela(profileGroup);
                        jsonObject.put("result", profileId);
                    } catch (Exception e) {
                        jsonObject.put("result", IgmpConstants.BIND_FAILD);
                    }
                    returnMessage(jsonObject);
                }
            }
        });
        return NONE;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getJconnectionId() {
        return jconnectionId;
    }

    public void setJconnectionId(String jconnectionId) {
        this.jconnectionId = jconnectionId;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public Integer getIgmpVersion() {
        return igmpVersion;
    }

    public void setIgmpVersion(Integer igmpVersion) {
        this.igmpVersion = igmpVersion;
    }

    public List<Integer> getGroupIdList() {
        return groupIdList;
    }

    public void setGroupIdList(List<Integer> groupIdList) {
        this.groupIdList = groupIdList;
    }

    public Integer getProfileId() {
        return profileId;
    }

    public void setProfileId(Integer profileId) {
        this.profileId = profileId;
    }

}
