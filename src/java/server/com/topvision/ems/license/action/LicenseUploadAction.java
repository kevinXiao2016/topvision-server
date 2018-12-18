package com.topvision.ems.license.action;

import java.io.File;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.topvision.framework.common.FileUtils;
import com.topvision.framework.web.struts2.FileUploadAction;
import com.topvision.platform.SystemConstants;

@Controller("licenseUploadAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class LicenseUploadAction extends FileUploadAction {
    private static Logger logger = LoggerFactory.getLogger(LicenseUploadAction.class);
    private static final long serialVersionUID = -8465524736395688989L;

    private int daysLeft = 0;
    private boolean valid = false;

    /**
     * 更新License
     * 
     * @return
     * @throws IOException
     */
    public String uploadLicense() throws IOException {
        File file = null;
        String lic = SystemConstants.WEB_INF_REAL_PATH + File.separatorChar + "classes" + File.separatorChar
                + "license.lic";
        file = new File(lic);
        if (logger.isDebugEnabled()) {
            logger.debug("tmp license file:" + file.getAbsolutePath());
        }
        try {
            FileUtils.copy(getUpload(), file);
        } finally {
            if (file != null && file.exists()) {
                file.delete();
            }
        }

        //daysLeft = LicenseManager.getInstance().getDaysOfLeft(file);
        if (daysLeft > 0) {
            //LicenseManager.getInstance().expired = false;
            return "licenseOk";
        }

        return "licenseError";
    }

    public int getDaysLeft() {
        return daysLeft;
    }

    public boolean isValid() {
        return valid;
    }

    public void setDaysLeft(int daysLeft) {
        this.daysLeft = daysLeft;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

}
