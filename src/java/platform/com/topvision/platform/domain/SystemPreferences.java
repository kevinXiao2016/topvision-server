package com.topvision.platform.domain;

import org.apache.ibatis.type.Alias;

import com.topvision.framework.domain.BaseEntity;

@Alias("systemPreferences")
public class SystemPreferences extends BaseEntity implements com.topvision.framework.dao.mybatis.AliasesSuperType {
    private static final long serialVersionUID = 8626799250008202300L;

    private String name;
    private String value;
    private String module;

    public String getModule() {
        return module;
    }

    public String getName() {
        return name;
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

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "SystemPreferences{" + "module='" + module + '\'' + ", name='" + name + '\'' + ", value='" + value
                + '\'' + '}';
    }
}
