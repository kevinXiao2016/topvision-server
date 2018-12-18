/***********************************************************************
 * $Id: UserAlertAction.java,v1.0 2016年11月12日 上午9:43:54 $
 * 
 * @author: Rod John
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.fault.action;

import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.topvision.framework.web.struts2.BaseAction;
import com.topvision.platform.domain.UserContext;
import com.topvision.platform.service.UserService;

import net.sf.json.JSONObject;

/**
 * @author Rod John
 * @created @2016年11月12日-上午9:43:54
 *
 */
@Controller("userAlertAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class UserAlertAction extends BaseAction {

    private static final long serialVersionUID = 913387061752688891L;
    private static final Logger logger = LoggerFactory.getLogger(UserAlertAction.class);
    @Autowired
    private UserService userService;
    private List<Integer> alertTypeIds;

    /**
     * 加载用户告警ID
     * 
     * @return
     * @throws IOException
     */
    public String loadUserAlertType() throws IOException {
        UserContext uc = (UserContext) super.getSession().get(UserContext.KEY);
        JSONObject resultMsg = new JSONObject();
        try {
            List<Integer> alertTypeList = userService.getUserAlertTypeId(uc.getUserId());
            resultMsg.put("alertTypeList", alertTypeList);
        } catch (Exception e) {
            logger.error("loadUserAlertType error:", e);
            resultMsg.put("error", e.getMessage());
        }
        resultMsg.write(response.getWriter());
        return NONE;
    }

    /**
     * 更新用户告警ID
     * 
     * @return
     * @throws IOException 
     */
    public String updateUserAlertType() throws IOException {
        UserContext uc = (UserContext) super.getSession().get(UserContext.KEY);
        JSONObject resultMsg = new JSONObject();
        try {
            userService.updateUserAlertType(uc.getUserId(), alertTypeIds);
        } catch (Exception e) {
            logger.error("updateUserAlertType error:", e);
            resultMsg.put("error", e.getMessage());
        }
        resultMsg.write(response.getWriter());
        return NONE;
    }

    /**
     * @return the userService
     */
    public UserService getUserService() {
        return userService;
    }

    /**
     * @param userService the userService to set
     */
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    /**
     * @return the alertTypeIds
     */
    public List<Integer> getAlertTypeIds() {
        return alertTypeIds;
    }

    /**
     * @param alertTypeIds the alertTypeIds to set
     */
    public void setAlertTypeIds(List<Integer> alertTypeIds) {
        this.alertTypeIds = alertTypeIds;
    }

}
