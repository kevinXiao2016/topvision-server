/**
 * 
 */
package com.topvision.ems.portal.action;

import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.apache.struts2.ServletActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.topvision.framework.web.struts2.BaseAction;
import com.topvision.framework.web.util.HttpResponseUtils;
import com.topvision.platform.ResourceManager;
import com.topvision.platform.domain.Role;
import com.topvision.platform.domain.User;
import com.topvision.platform.domain.UserContext;
import com.topvision.platform.domain.UserEx;
import com.topvision.platform.service.SystemPreferencesService;
import com.topvision.platform.service.UserService;
import com.topvision.platform.user.context.UserContextManager;

/**
 * @author niejun
 * 
 */
@Controller("systemPortalAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class SystemPortalAction extends BaseAction {
    private static final long serialVersionUID = -7752536446661463364L;
    private static Logger logger = LoggerFactory.getLogger(SystemPortalAction.class);
    @Autowired
    private UserService userService;
    @Autowired
    private UserContextManager ucm;
    private UserEx userEx = null;
    @Autowired
    private SystemPreferencesService systemPreferencesService;

    public String viewPersonalInfoForPortal() throws Exception {
        UserContext uc = (UserContext) getSession().get(UserContext.KEY);
        ResourceManager resourceManager = ResourceManager.getResourceManager("com.topvision.ems.portal.resources");
        User user = uc.getUser();
        try {
            userEx = userService.getUserEx(uc.getUserId());
            List<Role> roles = userService.getRoleByUserId(uc.getUserId());
            Writer writer = response.getWriter();
            writer.write("<table class='dataTable zebra' width='100%' border='1' cellspacing='0' cellpadding='0' bordercolor='#DFDFDF' rules='all'><tr><td width=100 class='rightBlueTxt'>");
            writer.write(resourceManager.getString("label.username", "label.username"));
            writer.write(":</td><td><a class='yellowLinks' href=\"#\" onclick=\"modifyPersonalInfo();\">");
            writer.write(userEx.getUserName());
            writer.write("</a></td></tr>");
            writer.write("<tr><td class='rightBlueTxt'>");
            writer.write(resourceManager.getString("label.role"));
            writer.write(":</td><td>");
            String s = roles.toString();
            writer.write(s.substring(1, s.length() - 1));
            writer.write("</td></tr>");

            writer.write("<tr><td class='rightBlueTxt'>");
            writer.write(resourceManager.getString("label.today"));
            writer.write(":</td><td>");
            Locale l;
            try {
                String[] ss = uc.getUser().getLanguage().split("_");
                l = new Locale(ss[0], ss[1]);
            } catch (Exception e) {
                l = Locale.getDefault();
            }
            if (uc.getUser().getLanguage() != null && uc.getUser().getLanguage().equalsIgnoreCase("en_US")) {
                Date now = new Date();
                int length = now.toString().length();
                writer.write(now.toString().substring(0, length - 17) + now.toString().substring(length - 4, length));
            } else {
                SimpleDateFormat DATE_FORMAT = new SimpleDateFormat(resourceManager.getString("today.date.type"), l);
                writer.write(DATE_FORMAT.format(new Date()));
            }
            writer.write("</td></tr>");

            if (user.getLastLoginTime() == null) {
                writer.write("<tr><td colspan=2 class='rightBlueTxt'>");
                writer.write(resourceManager.getString("first.login"));
                writer.write("</td></tr>");
            } else {
                writer.write("<tr><td class='rightBlueTxt'>");
                writer.write(resourceManager.getString("last.login.time"));
                writer.write(":</td><td>");
                //modify by bravin@20140529: 这个表的lastLogintime是CURRENT_TIMESTAMP,导致没有登录时就有了上次登录时间
                if (!user.getLastLoginTime().equals(user.getCreateTime())) {
                    if (uc.getUser().getLanguage() != null && uc.getUser().getLanguage().equalsIgnoreCase("en_US")) {
                        Date newDate = new Date(user.getLastLoginTime().getTime());
                        writer.write(newDate.toString().replace("CST", ""));
                    } else {
                        SimpleDateFormat DATE_FORMAT1 = new SimpleDateFormat(resourceManager.getString("date.type"), l);
                        writer.write(DATE_FORMAT1.format(user.getLastLoginTime()));
                    }
                }
                writer.write("</td></tr>");
                writer.write("<tr><td class='rightBlueTxt'>");
                writer.write(resourceManager.getString("last.login.ip"));
                writer.write(":</td><td>");
                writer.write(user.getLastLoginIp() == null ? "" : user.getLastLoginIp());
                writer.write("</td></tr>");
            }
            //userEx.isAllowMutliIpLogin();
            if (userEx.isAllowMutliIpLogin()) {
                writer.write("<tr><td class='rightBlueTxt'>");
                writer.write(resourceManager.getString("current.login.ip"));
                writer.write(":</td><td>");
                StringBuilder sb = new StringBuilder();
                Set<String> hosts = ucm.getSharedUserHosts(user.getUserName());
                for (String host : hosts) {
                    sb.append(",  ");
                    sb.append(host);
                }
                writer.write(sb.substring(2));
                writer.write("</td></tr>");
            }

            if (uc.hasPower("editPortal")) {
                writer.write("<tr><td align=right colspan=2 class='lastTdLinks'>");
                writer.write("<a class='yellowLinks'href=\"#\" onclick=\"setPersonalize()\">");
                writer.write(resourceManager.getString("personalize"));
                writer.write("</a>&nbsp;&nbsp;&nbsp;&nbsp;");
                writer.write("<a class='yellowLinks' id=grumble2 href=\"#\" onclick=\"setMyPortlet()\">");
                writer.write(resourceManager.getString("custom.mydesktop"));
                writer.write("</a></td></tr>");
            }
            writer.write("</table>");
        } catch (Throwable ex) {
            logger.error("View Personal Info For Portal.", ex);
        }
        return NONE;
    }

    public UserEx getUserEx() {
        return userEx;
    }

    public UserService getUserService() {
        return userService;
    }

    public String searchForPortal() {
        return NONE;
    }

    public void setUserEx(UserEx userEx) {
        this.userEx = userEx;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }
}
