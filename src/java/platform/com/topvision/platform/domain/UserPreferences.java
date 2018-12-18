package com.topvision.platform.domain;

import org.apache.ibatis.type.Alias;

import com.topvision.framework.domain.BaseEntity;

@Alias("userPreferences")
public class UserPreferences extends BaseEntity implements com.topvision.framework.dao.mybatis.AliasesSuperType {
    private static final long serialVersionUID = 1407227431066299318L;

    public static final String MACDISPLAYSTYLE = "macDisplayStyle";
    public static final String DISPLAY_FIELD_IP = "ip";
    public static final String DISPLAY_FIELD_MAC = "mac";
    public static final String DISPLAY_FIELD_NAME = "name";

    private long userId = 0;
    private String userName;
    private String name = null;
    private String value = null;
    private String module = null;

    public String getModule() {
        return module;
    }

    public String getName() {
        return name;
    }

    public long getUserId() {
        return userId;
    }

    public String getValue() {
        return value;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "UserPreferences{" + "module='" + module + '\'' + ", userId=" + userId + ", name='" + name + '\''
                + ", value='" + value + '\'' + '}';
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
