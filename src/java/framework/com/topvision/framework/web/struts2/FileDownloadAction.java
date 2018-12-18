package com.topvision.framework.web.struts2;

import java.io.File;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import org.apache.struts2.ServletActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.Action;

/**
 * 
 * @author niejun
 * 
 */
@Controller("fileDownloadAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class FileDownloadAction implements Action {
    private Logger logger = LoggerFactory.getLogger(getClass());
    private String fileName;
    private String inputPath;

    @Override
    public String execute() throws Exception {
        File file = new File(ServletActionContext.getServletContext().getRealPath("") + inputPath);
        if (file.exists() && file.isFile()) {
            return SUCCESS;
        } else {
            return NONE;
        }
    }

    public String getDownloadFileName() {
        String downFileName = fileName;
        try {
            downFileName = new String(downFileName.getBytes(), "ISO8859-1");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return downFileName;
    }

    public String getInputPath() {
        return inputPath;
    }

    public InputStream getInputStream() throws Exception {
        return ServletActionContext.getServletContext().getResourceAsStream(inputPath);

    }

    public Logger getLogger() {
        return logger;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setInputPath(String value) throws Exception {
        if (value != null) {
            value = new String(value.getBytes("ISO8859-1"), "UTF-8");
        }
        inputPath = value;
    }

}
