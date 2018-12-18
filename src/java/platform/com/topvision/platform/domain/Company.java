package com.topvision.platform.domain;

import com.topvision.framework.domain.BaseEntity;

public class Company extends BaseEntity {
    private static final long serialVersionUID = 7315302849955604640L;

    private long companyId;
    private String name;
    private String address;
    private String postal;
    private String contact;
    private String email;
    private String url;
    private String telphone;
    private String fax;
    private String note;

    public String getAddress() {
        return address;
    }

    public long getCompanyId() {
        return companyId;
    }

    public String getContact() {
        return contact;
    }

    public String getEmail() {
        return email;
    }

    public String getFax() {
        return fax;
    }

    public String getName() {
        return name;
    }

    public String getNote() {
        return note;
    }

    public String getPostal() {
        return postal;
    }

    public String getTelphone() {
        return telphone;
    }

    public String getUrl() {
        return url;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setCompanyId(long companyId) {
        this.companyId = companyId;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public void setPostal(String postal) {
        this.postal = postal;
    }

    public void setTelphone(String telphone) {
        this.telphone = telphone;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "Company{" + "address='" + address + '\'' + ", companyId=" + companyId + ", name='" + name + '\''
                + ", postal='" + postal + '\'' + ", contact='" + contact + '\'' + ", email='" + email + '\''
                + ", url='" + url + '\'' + ", telphone='" + telphone + '\'' + ", fax='" + fax + '\'' + ", note='"
                + note + '\'' + '}';
    }
}
