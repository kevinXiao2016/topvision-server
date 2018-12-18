package com.topvision.platform.action;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.topvision.framework.web.struts2.BaseAction;

@Controller("quickConfigAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class QuickConfigAction extends BaseAction {
    private static final long serialVersionUID = 7690631072123965979L;
    private String quickConfigTask;

    /**
     * 显示快速配置
     * 
     * @return
     */
    public String showQuickConfigTask() {
        return quickConfigTask;
    }

    public String getQuickConfigTask() {
        return quickConfigTask;
    }

    public void setQuickConfigTask(String quickConfigTask) {
        this.quickConfigTask = quickConfigTask;
    }
}
