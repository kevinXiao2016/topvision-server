package com.topvision.ems.fault.domain;

import java.io.Serializable;
import java.util.List;

import com.topvision.framework.dao.mybatis.AliasesSuperType;

public class AlertAboutUsers implements Serializable, AliasesSuperType {

    private static final long serialVersionUID = 2854724011358924567L;
    private Long userId;
    private String userName;
    private String email;
    private String mobile;
    private String choose;
    private List<String> alertTypeId;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getChoose() {
        return choose;
    }

    public void setChoose(String choose) {
        this.choose = choose;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public List<String> getAlertTypeId() {
        return alertTypeId;
    }

    public void setAlertTypeId(List<String> alertTypeId) {
        this.alertTypeId = alertTypeId;
    }

}
