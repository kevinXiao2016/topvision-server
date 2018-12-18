package com.topvision.platform.domain;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.topvision.framework.dao.mybatis.AliasesSuperType;
import com.topvision.framework.domain.BaseEntity;

public class User extends BaseEntity implements AliasesSuperType {
    private static final long serialVersionUID = -1057740007213146847L;

    public static long SUPERADMIN_ID = 1;
    public static long ADMINISTRATOR_ID = 2;
    public static byte STARTED = 1;
    public static byte STOPPED = 0;
    private int status = STARTED;

    private long userId;
    private String userName = null;
    private String familyName = null;
    private String firstName = null;
    private String passwd = null;
    private String note = null;
    private String mobile = null;
    private String homeTelphone = null;
    private String workTelphone = null;
    private String email;
    private String sex = null;
    private String age = null;
    private String homePlace = null;
    private Timestamp createTime = null;
    private Timestamp modifyTime = null;
    private Timestamp lastLoginTime = null;
    private String lastLoginIp = null;
    private String bindIp;
    private String limitIp;
    private int ipLoginActive = 1;
    private long placeId;
    private long departmentId;
    private Long userGroupId;// 用户当前根地域
    private List<Long> userGroupIds;// 用户可选根地域集合
    private String userGroupIdsStr;

    private String choose;
    private String extend = null;
    private String placeName;
    private String roleNames;
    private String roleIds;
    private String departmentName;
    private String userGroupName;
    private Boolean checked;
    private String language;
    protected Integer timeout;
    protected Boolean allowMutliIpLogin;

    public Integer getTimeout() {
        return timeout;
    }

    public void setTimeout(Integer timeout) {
        this.timeout = timeout;
    }

    public Boolean isAllowMutliIpLogin() {
        return allowMutliIpLogin;
    }

    public void setAllowMutliIpLogin(Boolean allowMutliIpLogin) {
        this.allowMutliIpLogin = allowMutliIpLogin;
    }

    public String getAge() {
        return age;
    }

    public String getBindIp() {
        return bindIp;
    }

    public Boolean getChecked() {
        return checked;
    }

    public void setChecked(Boolean checked) {
        this.checked = checked;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public long getDepartmentId() {
        return departmentId;
    }

    public String getEmail() {
        return email;
    }

    public String getFamilyName() {
        return familyName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getHomePlace() {
        return homePlace;
    }

    public String getHomeTelphone() {
        return homeTelphone;
    }

    public int getIpLoginActive() {
        return ipLoginActive;
    }

    public String getLastLoginIp() {
        return lastLoginIp;
    }

    public Timestamp getLastLoginTime() {
        return lastLoginTime;
    }

    public String getLimitIp() {
        return limitIp == null ? null : limitIp.trim();
    }

    public String getMobile() {
        return mobile;
    }

    public Timestamp getModifyTime() {
        return modifyTime;
    }

    public String getNote() {
        return note;
    }

    public String getPasswd() {
        return passwd;
    }

    public long getPlaceId() {
        return placeId;
    }

    public String getSex() {
        return sex;
    }

    public int getStatus() {
        return status;
    }

    public Long getUserGroupId() {
        return userGroupId;
    }

    public long getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public String getWorkTelphone() {
        return workTelphone;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public void setBindIp(String bindIp) {
        this.bindIp = bindIp;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public void setDepartmentId(long departmentId) {
        this.departmentId = departmentId;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setHomePlace(String homePlace) {
        this.homePlace = homePlace;
    }

    public void setHomeTelphone(String homeTelphone) {
        this.homeTelphone = homeTelphone;
    }

    public void setIpLoginActive(int ipLoginActive) {
        this.ipLoginActive = ipLoginActive;
    }

    public void setLastLoginIp(String lastLoginIp) {
        this.lastLoginIp = lastLoginIp;
    }

    public void setLastLoginTime(Timestamp lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public void setLimitIp(String limitIp) {
        this.limitIp = limitIp;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public void setModifyTime(Timestamp modifyTime) {
        this.modifyTime = modifyTime;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    public void setPlaceId(long placeId) {
        this.placeId = placeId;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public void setStatus(int state) {
        this.status = state;
    }

    public void setUserGroupId(Long userGroupId) {
        this.userGroupId = userGroupId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setWorkTelphone(String workTelphone) {
        this.workTelphone = workTelphone;
    }

    public String getExtend() {
        return extend;
    }

    public void setExtend(String extend) {
        this.extend = extend;
    }

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public String getRoleNames() {
        return roleNames;
    }

    public void setRoleNames(String roleNames) {
        this.roleNames = roleNames;
    }

    public String getRoleIds() {
        return roleIds;
    }

    public void setRoleIds(String roleIds) {
        this.roleIds = roleIds;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getUserGroupName() {
        return userGroupName;
    }

    public void setUserGroupName(String userGroupName) {
        this.userGroupName = userGroupName;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    @Override
    public String toString() {
        return "User{" + "age='" + age + '\'' + ", status=" + status + ", userId=" + userId + ", userName='" + userName
                + '\'' + ", familyName='" + familyName + '\'' + ", firstName='" + firstName + '\'' + ", passwd='"
                + passwd + '\'' + ", note='" + note + '\'' + ", mobile='" + mobile + '\'' + ", homeTelphone='"
                + homeTelphone + '\'' + ", workTelphone='" + workTelphone + '\'' + ", email='" + email + '\''
                + ", sex='" + sex + '\'' + ", homePlace='" + homePlace + '\'' + ", createTime=" + createTime
                + ", modifyTime=" + modifyTime + ", lastLoginTime=" + lastLoginTime + ", lastLoginIp='" + lastLoginIp
                + '\'' + ", bindIp='" + bindIp + '\'' + ", limitIp='" + limitIp + '\'' + ", ipLoginActive="
                + ipLoginActive + ", placeId=" + placeId + ", departmentId=" + departmentId + ", userGroupId="
                + userGroupId + ", language=" + language + '}';
    }

    public List<Long> getUserGroupIds() {
        return userGroupIds;
    }

    public void setUserGroupIds(List<Long> userGroupIds) {
        this.userGroupIds = userGroupIds;
    }

    public String getUserGroupIdsStr() {
        return userGroupIdsStr;
    }

    public void setUserGroupIdsStr(String userGroupIdsStr) {
        this.userGroupIdsStr = userGroupIdsStr;
        String[] idStr = userGroupIdsStr.split(",");
        this.userGroupIds = new ArrayList<Long>();
        for (int i = 0; i < idStr.length; i++) {
            this.userGroupIds.add(Long.valueOf(idStr[i]));
        }
    }

    public String getChoose() {
        return choose;
    }

    public void setChoose(String choose) {
        this.choose = choose;
    }
}
