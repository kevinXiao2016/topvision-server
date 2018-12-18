/**
 * 
 */
package com.topvision.ems.portal.action;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.topvision.framework.web.struts2.BaseAction;

/**
 * @author niejun
 * 
 */
@Controller("monitorPortalAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class MonitorPortalAction extends BaseAction {

    private static final long serialVersionUID = -7636116534797637308L;

}
