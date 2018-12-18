package com.topvision.platform.domain;

import com.topvision.framework.domain.BaseEntity;

public class Theme extends BaseEntity {
    private static final long serialVersionUID = 9027092071603271821L;

    public static final String OFFICE_2007 = "office";
    public static final String MAC = "office";
    private String name = "blue";
    private String baseColor = null;
    private String borderColor = "#6593CF";
    private String textColor = "#00156E";
    private String backgroundColor = "#BFDBFF";
    private String collapseBgColor = "red";
    private String globalFont = "Verdana, Arial, sans-serif";

    public String getBackgroundColor() {
        return backgroundColor;
    }

    public String getBaseColor() {
        return baseColor;
    }

    public String getBorderColor() {
        return borderColor;
    }

    public String getCollapseBgColor() {
        return collapseBgColor;
    }

    public String getGlobalFont() {
        return globalFont;
    }

    public String getName() {
        return name;
    }

    public String getTextColor() {
        return textColor;
    }

    public void setBackgroundColor(String backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public void setBaseColor(String baseColor) {
        this.baseColor = baseColor;
    }

    public void setBorderColor(String borderColor) {
        this.borderColor = borderColor;
    }

    public void setCollapseBgColor(String collapseBgColor) {
        this.collapseBgColor = collapseBgColor;
    }

    public void setGlobalFont(String globalFont) {
        this.globalFont = globalFont;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTextColor(String textColor) {
        this.textColor = textColor;
    }

    @Override
    public String toString() {
        return "Theme{" + "backgroundColor='" + backgroundColor + '\'' + ", name='" + name + '\'' + ", baseColor='"
                + baseColor + '\'' + ", borderColor='" + borderColor + '\'' + ", textColor='" + textColor + '\''
                + ", collapseBgColor='" + collapseBgColor + '\'' + ", globalFont='" + globalFont + '\'' + '}';
    }
}