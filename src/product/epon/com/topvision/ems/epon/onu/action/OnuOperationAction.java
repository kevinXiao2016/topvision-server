package com.topvision.ems.epon.onu.action;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.topvision.ems.epon.onu.service.OnuService;
import com.topvision.ems.epon.onuauth.domain.OltAuthentication;
import com.topvision.ems.epon.onuauth.service.OnuAuthService;
import com.topvision.framework.web.struts2.BaseAction;

@Controller("onuOperationAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class OnuOperationAction extends BaseAction {

    private static final long serialVersionUID = 1865089688147760838L;

    @Autowired
    private OnuService onuService;
    @Autowired
    private OnuAuthService onuAuthService;

    private Long onuId;
    private Long entityId;
    private Long ponId;
    private Long onuIndex;

    //重启ONU
    public String resetOnu() {
        onuService.resetOnu(entityId, onuId);
        return NONE;
    }

    //替换ONU
    public String replaceOnu() {
        return NONE;
    }

    //解注册
    public String deregisterOnu() {
        onuService.deregisterOnu(entityId, onuId);
        return NONE;
    }

    //删除
    public String deleteOnu() {
        //TODO GPON需要单独做
        OltAuthentication auth = onuAuthService.getOltAuthenticationByIndex(entityId, onuIndex);
        onuAuthService.deleteAuthenPreConfig(entityId, ponId, onuIndex, auth.getAuthType());
        return NONE;
    }

    public String showOnuTagView() {
        return SUCCESS;
    }

    //设置标签
    public String setOnuTag() {
        return NONE;
    }

    //关注
    public String attentionOnu() {
        return NONE;
    }

    public Long getOnuId() {
        return onuId;
    }

    public void setOnuId(Long onuId) {
        this.onuId = onuId;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

}
