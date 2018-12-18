/***********************************************************************
 * $Id: LicenseView.java,v1.0 2012-7-12 下午4:00:38 $
 * 
 * @author: dengl
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.platform.domain;

import java.util.List;

import com.topvision.license.common.domain.Module;

/**
 * 用于展现license信息的domain
 * 
 * @author dengl
 * @created @2012-7-12-下午4:00:38
 * 
 */
public class LicenseView {
    private String version;// license版本
    private String licenseType;// license类型
    private String organisation;
    private String numberOfDays;// 可用天数
    private String expiryDate;// 试用版导入的截止日期
    private String numberOfUsers;// 用户数量限制
    private String numberOfEngines;// 采集引擎数量限制
    private List<Module> modules;// 模块license信息
    private String endDate;// 许可证截止日期
    private String user;
    private String phone;
    private String email;
    private String selectEntityTypes;
    private String selectReports;
    private String unSelectReports;
    private String mobile;
    private Boolean mobileSurport;
    private Boolean reportSurport;
    private Boolean pnmpSurport;
    private Integer oltNum;
    private Integer cmcNum;
    private Integer onuNum;
    private Integer cmtsNum;

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getLicenseType() {
        return licenseType;
    }

    public void setLicenseType(String licenseType) {
        this.licenseType = licenseType;
    }

    public String getOrganisation() {
        return organisation;
    }

    public void setOrganisation(String organisation) {
        this.organisation = organisation;
    }

    public String getNumberOfDays() {
        return numberOfDays;
    }

    public void setNumberOfDays(String numberOfDays) {
        this.numberOfDays = numberOfDays;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getNumberOfUsers() {
        return numberOfUsers;
    }

    public void setNumberOfUsers(String numberOfUsers) {
        this.numberOfUsers = numberOfUsers;
    }

    public String getNumberOfEngines() {
        return numberOfEngines;
    }

    public void setNumberOfEngines(String numberOfEngines) {
        this.numberOfEngines = numberOfEngines;
    }

    public List<Module> getModules() {
        return modules;
    }

    public void setModules(List<Module> modules) {
        this.modules = modules;
    }

    /**
     * @return the user
     */
    public String getUser() {
        return user;
    }

    /**
     * @param user
     *            the user to set
     */
    public void setUser(String user) {
        this.user = user;
    }

    /**
     * @return the phone
     */
    public String getPhone() {
        return phone;
    }

    /**
     * @param phone
     *            the phone to set
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email
     *            the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return the selectEntityTypes
     */
    public String getSelectEntityTypes() {
        return selectEntityTypes;
    }

    /**
     * @param selectEntityTypes
     *            the selectEntityTypes to set
     */
    public void setSelectEntityTypes(String selectEntityTypes) {
        this.selectEntityTypes = selectEntityTypes;
    }

    public String getSelectReports() {
        return selectReports;
    }

    public void setSelectReports(String selectReports) {
        this.selectReports = selectReports;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getUnSelectReports() {
        return unSelectReports;
    }

    public void setUnSelectReports(String unSelectReports) {
        this.unSelectReports = unSelectReports;
    }

    public Boolean getMobileSurport() {
        return mobileSurport;
    }

    public void setMobileSurport(Boolean mobileSurport) {
        this.mobileSurport = mobileSurport;
    }

    public Boolean getReportSurport() {
        return reportSurport;
    }

    public void setReportSurport(Boolean reportSurport) {
        this.reportSurport = reportSurport;
    }

    public Integer getOltNum() {
        return oltNum;
    }

    public void setOltNum(Integer oltNum) {
        this.oltNum = oltNum;
    }

    public Integer getCmcNum() {
        return cmcNum;
    }

    public void setCmcNum(Integer cmcNum) {
        this.cmcNum = cmcNum;
    }

    public Integer getOnuNum() {
        return onuNum;
    }

    public void setOnuNum(Integer onuNum) {
        this.onuNum = onuNum;
    }

    public Integer getCmtsNum() {
        return cmtsNum;
    }

    public void setCmtsNum(Integer cmtsNum) {
        this.cmtsNum = cmtsNum;
    }

    public Boolean getPnmpSurport() {
        return pnmpSurport;
    }

    public void setPnmpSurport(Boolean pnmpSurport) {
        this.pnmpSurport = pnmpSurport;
    }

}
