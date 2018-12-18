package com.topvision.ems.network.action;

import java.io.InputStream;

import org.apache.struts2.ServletActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.topvision.framework.web.struts2.FileDownloadAction;

@Controller("downloadJGraphAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class DownloadJGraphAction extends FileDownloadAction {
    private static Logger logger = LoggerFactory.getLogger(DownloadJGraphAction.class);

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.framework.web.struts2.FileDownloadAction#execute()
     */
    @Override
    public String execute() throws Exception {
        String str = ServletActionContext.getRequest().getHeader("user-agent");
        logger.info(str);
        if (str != null) {
            if (str.indexOf("MSIE") > -1) {
                setInputPath("/js/zetaframework/jgraph.js");
            } else if (str.indexOf("Chrome") > -1) {
                setInputPath("/js/zetaframework/jgraph_chrome.js");
            } else if (str.indexOf("Firefox") > -1) {
                setInputPath("/js/zetaframework/jgraph_firefox.js");
            } else {
                setInputPath("/js/zetaframework/jgraph_firefox.js");
            }
        } else {
            setInputPath("/js/zetaframework/jgraph.js");
        }
        return SUCCESS;
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.framework.web.struts2.FileDownloadAction#getInputStream()
     */
    @Override
    public InputStream getInputStream() {
        return ServletActionContext.getServletContext().getResourceAsStream(getInputPath());
    }
}
