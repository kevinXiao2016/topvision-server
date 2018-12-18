/***********************************************************************
 * $Id: IgmpGroupAction.java,v1.0 2016年7月29日 下午2:07:02 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.igmpconfig.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.topvision.ems.epon.igmpconfig.IgmpConstants;
import com.topvision.ems.epon.igmpconfig.domain.IgmpVlanGroup;
import com.topvision.ems.epon.igmpconfig.service.OltIgmpConfigService;
import com.topvision.ems.message.Message;
import com.topvision.ems.message.MessagePusher;
import com.topvision.framework.web.struts2.BaseAction;
import com.topvision.platform.zetaframework.util.Cell;
import com.topvision.platform.zetaframework.util.ExcelResolver;
import com.topvision.platform.zetaframework.util.FileUploadUtil;
import com.topvision.platform.zetaframework.util.Row;
import com.topvision.platform.zetaframework.util.RowHandler;

/**
 * @author Bravin
 * @created @2016年7月29日-下午2:07:02
 *
 */
@Controller("igmpGroupAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class IgmpGroupAction extends BaseAction {
    private static final long serialVersionUID = 4474753027473587665L;
    private String fileName;
    private String jconnectionId;
    private Long entityId;
    @Autowired
    private MessagePusher messagePusher;
    @Autowired
    private OltIgmpConfigService oltIgmpConfigService;

    public String showIgmpGroupAliasImport() {
        return SUCCESS;
    }

    public String importIgmpGroupAlias() throws IOException {
        File file = FileUploadUtil.checkout(request, fileName);
        ExcelResolver.resolve(new RowHandler() {

            private void returnMessage(Object data) {
                Message message = new Message(IgmpConstants.IGMP_GROUP_CALLBACK_HANDLER);
                message.setJconnectID(jconnectionId);
                message.setData(data);
                messagePusher.sendMessage(message);
            }

            private void returnRowResult(Object data, boolean result) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("success", result);
                jsonObject.put("data", data);
                returnMessage(jsonObject);
            }

            @Override
            public Boolean handleRow(Row row) {
                if (row.getRowNum() < 2) {
                    return CONTINUE;
                }
                Map<String, String> map = new HashMap<>();
                try {
                    List<Cell> cells = row.getCells();
                    IgmpVlanGroup group = new IgmpVlanGroup();
                    group.setEntityId(entityId);
                    map.put("groupId", cells.get(0).getContent());
                    String groupName = cells.get(1).getContent();
                    map.put("groupName", groupName);
                    String nameReg = "^[a-zA-Z\\d\\u4e00-\\u9fa5-_\\[\\]()\\/\\.:]{1,63}$";
                    if (!groupName.matches(nameReg)) {
                        throw new RuntimeException();
                    }
                    group.setGroupId(Integer.parseInt(cells.get(0).getContent()));
                    group.setGroupName(groupName);
                    oltIgmpConfigService.updateIgmpGroupAlias(group);
                    returnRowResult(map, true);
                } catch (Exception e) {
                    returnRowResult(map, false);
                }
                return CONTINUE;
            }

        }, new FileInputStream(file));
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

}
