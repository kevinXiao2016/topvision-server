package com.topvision.platform.domain;

import java.io.IOException;
import java.io.InputStream;
import java.util.InvalidPropertiesFormatException;
import java.util.Properties;

import org.apache.ibatis.type.Alias;

import com.topvision.framework.common.FileUtils;

@Alias("systemLogEx")
public class SystemLogEx extends SystemLog {
    private static final long serialVersionUID = -799027610930922964L;

    private static Properties properties = new Properties();

    /**
     * 得到给定关键字的描述.
     * 
     * @param key
     * @return
     */
    public static String getString(String key) {
        return properties.getProperty(key, key);
    }

    /**
     * 扩展的系统的日志描述资源文件.
     */
    public static void initialize(InputStream is) throws IOException, InvalidPropertiesFormatException {
        try {
            properties.loadFromXML(is);
        } finally {
            FileUtils.closeQuitely(is);
        }
    }

    private String userName;

    private String operationTypeName;

    private String applicationName;

    public String getApplicationName() {
        return applicationName;
    }

    public String getOperationTypeName() {
        return operationTypeName;
    }

    @Override
    public String getUserName() {
        return userName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    public void setOperationTypeName(String operationTypeName) {
        this.operationTypeName = operationTypeName;
    }

    @Override
    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public String toString() {
        return "SystemLogEx{" + "applicationName='" + applicationName + '\'' + ", userName='" + userName + '\''
                + ", operationTypeName='" + operationTypeName + '\'' + '}';
    }
}
