package com.topvision.platform.domain;

import java.util.HashSet;
import java.util.List;

import com.topvision.framework.domain.BaseEntity;

public class GeneralPreferences extends BaseEntity {
    private static final long serialVersionUID = 8224180139224306400L;

    private boolean tabbed = true;
    private boolean alarmWhenCloseManyTab = true;
    private boolean switchWhenNewTab = true;
    private boolean welcomeWhenStart = false;
    private boolean notifyWhenMsg = true;
    private int pageSize = 25;
    private int tooltipStyle = 1;
    private HashSet<String> navis = new HashSet<String>();
    private List<String> naviBars = null;
    private String startPage = "welcome";
    private String styleName = "blue";
    private boolean tabMaxLimit = true;
    private boolean displayInputTip = true;
    private String macDisplayStyle = "6#M#U";
    private int tipShowTime = 5;
    private int weekStartDay = 1;
    private int autoRefreshTime;
    private int topNumber;
    private int soundTimeInterval;
    private int topoTreeDisplayDevice;
    private int topoTreeClickToOpen;

    public int getTipShowTime() {
        return tipShowTime;
    }

    public void setTipShowTime(int tipShowTime) {
        this.tipShowTime = tipShowTime;
    }

    public List<String> getNaviBars() {
        return naviBars;
    }

    public HashSet<String> getNavis() {
        return navis;
    }

    public int getPageSize() {
        return pageSize;
    }

    public String getStartPage() {
        return startPage;
    }

    public String getStyleName() {
        return styleName;
    }

    public int getTooltipStyle() {
        return tooltipStyle;
    }

    public boolean isAlarmWhenCloseManyTab() {
        return alarmWhenCloseManyTab;
    }

    public boolean isDisplayInputTip() {
        return displayInputTip;
    }

    public boolean isNotifyWhenMsg() {
        return notifyWhenMsg;
    }

    public boolean isSwitchWhenNewTab() {
        return switchWhenNewTab;
    }

    public boolean isTabbed() {
        return tabbed;
    }

    public boolean isTabMaxLimit() {
        return tabMaxLimit;
    }

    public boolean isWelcomeWhenStart() {
        return welcomeWhenStart;
    }

    public void setAlarmWhenCloseManyTab(boolean alarmWhenCloseManyTab) {
        this.alarmWhenCloseManyTab = alarmWhenCloseManyTab;
    }

    public void setDisplayInputTip(boolean displayInputTip) {
        this.displayInputTip = displayInputTip;
    }

    public void setNaviBars(List<String> naviBars) {
        this.naviBars = naviBars;
        navis.addAll(naviBars);
    }

    public void setNavis(HashSet<String> navis) {
        this.navis = navis;
    }

    public void setNotifyWhenMsg(boolean notifyWhenMsg) {
        this.notifyWhenMsg = notifyWhenMsg;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public void setStartPage(String startPage) {
        this.startPage = startPage;
    }

    public void setStyleName(String styleName) {
        this.styleName = styleName;
    }

    public void setSwitchWhenNewTab(boolean switchWhenNewTab) {
        this.switchWhenNewTab = switchWhenNewTab;
    }

    public void setTabbed(boolean tabbed) {
        this.tabbed = tabbed;
    }

    public void setTabMaxLimit(boolean tabMaxLimit) {
        this.tabMaxLimit = tabMaxLimit;
    }

    public void setTooltipStyle(int tooltipStyle) {
        this.tooltipStyle = tooltipStyle;
    }

    public void setWelcomeWhenStart(boolean welcomeWhenStart) {
        this.welcomeWhenStart = welcomeWhenStart;
    }

    public int getTopoTreeDisplayDevice() {
        return topoTreeDisplayDevice;
    }

    public void setTopoTreeDisplayDevice(int topoTreeDisplayDevice) {
        this.topoTreeDisplayDevice = topoTreeDisplayDevice;
    }

    public int getTopoTreeClickToOpen() {
        return topoTreeClickToOpen;
    }

    public void setTopoTreeClickToOpen(int topoTreeClickToOpen) {
        this.topoTreeClickToOpen = topoTreeClickToOpen;
    }

    public String getMacDisplayStyle() {
        return macDisplayStyle;
    }

    public void setMacDisplayStyle(String macDisplayStyle) {
        this.macDisplayStyle = macDisplayStyle;
    }

    public int getWeekStartDay() {
        return weekStartDay;
    }

    public void setWeekStartDay(int weekStartDay) {
        this.weekStartDay = weekStartDay;
    }

    public int getAutoRefreshTime() {
        return autoRefreshTime;
    }

    public void setAutoRefreshTime(int autoRefreshTime) {
        this.autoRefreshTime = autoRefreshTime;
    }

    public int getTopNumber() {
        return topNumber;
    }

    public void setTopNumber(int topNumber) {
        this.topNumber = topNumber;
    }

    public int getSoundTimeInterval() {
        return soundTimeInterval;
    }

    public void setSoundTimeInterval(int soundTimeInterval) {
        this.soundTimeInterval = soundTimeInterval;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("GeneralPreferences [tabbed=");
        builder.append(tabbed);
        builder.append(", alarmWhenCloseManyTab=");
        builder.append(alarmWhenCloseManyTab);
        builder.append(", switchWhenNewTab=");
        builder.append(switchWhenNewTab);
        builder.append(", welcomeWhenStart=");
        builder.append(welcomeWhenStart);
        builder.append(", notifyWhenMsg=");
        builder.append(notifyWhenMsg);
        builder.append(", pageSize=");
        builder.append(pageSize);
        builder.append(", tooltipStyle=");
        builder.append(tooltipStyle);
        builder.append(", navis=");
        builder.append(navis);
        builder.append(", naviBars=");
        builder.append(naviBars);
        builder.append(", startPage=");
        builder.append(startPage);
        builder.append(", styleName=");
        builder.append(styleName);
        builder.append(", tabMaxLimit=");
        builder.append(tabMaxLimit);
        builder.append(", displayInputTip=");
        builder.append(displayInputTip);
        builder.append(", macDisplayStyle=");
        builder.append(macDisplayStyle);
        builder.append(", tipShowTime=");
        builder.append(tipShowTime);
        builder.append(", weekStartDay=");
        builder.append(weekStartDay);
        builder.append(", autoRefreshTime=");
        builder.append(autoRefreshTime);
        builder.append(", topNumber=");
        builder.append(topNumber);
        builder.append(", soundTimeInterval=");
        builder.append(soundTimeInterval);
        builder.append(", topoTreeDisplayDevice=");
        builder.append(topoTreeDisplayDevice);
        builder.append(", topoTreeClickToOpen=");
        builder.append(topoTreeClickToOpen);
        builder.append("]");
        return builder.toString();
    }

}