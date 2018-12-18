package com.topvision.platform.action;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.topvision.framework.web.struts2.BaseAction;
import com.topvision.platform.domain.UserContext;
import com.topvision.platform.user.context.UserContextManager;

import net.sf.json.JSONObject;

@Controller("pingAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class PingAction extends BaseAction {
    private static final long serialVersionUID = 283411318025557552L;
    @Autowired
    private UserContextManager ucm;

    /**
     * PING测试
     * 
     * @return
     * @throws Exception
     */
    public String ping() throws Exception {
        if (request.getSession().getAttribute(UserContext.KEY) != null) {
            ucm.activeClient(request.getRemoteHost());
        }
        JSONObject json = new JSONObject();
        json.put("time", System.currentTimeMillis() / 1000);
        json.write(response.getWriter());
        return NONE;
    }
}
