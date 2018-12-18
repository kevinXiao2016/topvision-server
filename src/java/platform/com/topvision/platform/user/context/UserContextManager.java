/***********************************************************************
 * $Id: UserContextManager.java,v1.0 2014年3月6日 上午9:55:00 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.platform.user.context;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.servlet.http.HttpServletRequest;

import org.quartz.JobDetail;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.exception.service.AuthenticationException;
import com.topvision.framework.annotation.Database;
import com.topvision.framework.common.ClassAware;
import com.topvision.license.parser.LicenseIf;
import com.topvision.platform.SystemConstants;
import com.topvision.platform.dao.UserDao;
import com.topvision.platform.domain.User;
import com.topvision.platform.domain.UserContext;
import com.topvision.platform.domain.UserPreferences;
import com.topvision.platform.domain.UserPreferencesMap;
import com.topvision.platform.message.service.MessageService;
import com.topvision.platform.service.SchedulerService;
import com.topvision.platform.service.SystemPreferencesService;
import com.topvision.platform.service.UserPreferencesService;
import com.topvision.platform.user.job.TryPasswordCleanJob;
import com.topvision.platform.util.CurrentRequest;

/**
 * @author Bravin
 * @created @2014年3月6日-上午9:55:00
 *
 */
@Service
public class UserContextManager {
    private static final Logger logger = LoggerFactory.getLogger(UserContextManager.class);
    /* mapper: username and userContext.登录的时候是不知道UserId的，并且username是无法被更改的，所以可以使用username而不是userid */
    private Map<String, UserContext> usercontexts;
    private Map<String, Timestamp> lastLoginRecords;
    @Autowired
    private MessageService messageService;
    @Autowired
    private UserPreferencesService userPreferencesService;
    @Autowired
    private UserDao userDao;
    @Autowired
    private SchedulerService schedulerService;
    @Autowired
    private SystemPreferencesService systemPreferencesService;
    @Autowired
    private LicenseIf licenseIf;
    private Integer stopNumber;
    private Map<String, Database> modules;

    @PostConstruct
    public void initialize() {
        usercontexts = new ConcurrentHashMap<String, UserContext>();
        lastLoginRecords = new ConcurrentHashMap<String, Timestamp>();
        modules = new ConcurrentHashMap<String, Database>();
        loadModules();
        try {
            JobDetail job = newJob(TryPasswordCleanJob.class).withIdentity("cleantrypassword").build();
            job.getJobDataMap().put("manager", this);
            Trigger trg = newTrigger().withIdentity("cleantrypassword").withSchedule(cronSchedule("30 1 0 * * ?"))
                    .build();
            schedulerService.scheduleJob(job, trg);
        } catch (SchedulerException e) {
            logger.debug(e.getMessage(), e);
        }
        // load from database
        stopNumber = systemPreferencesService.getStopUserWhenErrorNumber();
    }

    @PreDestroy
    public void destory() {
        usercontexts.clear();
    }

    /**
     * 加载模块 --- @Bravin 通过扫描 java包 来决定哪些模块被加载了的
     */
    @SuppressWarnings("unchecked")
    public void loadModules() {
        ClassAware scaner = new ClassAware();
        Set<Class<?>> clazzes = scaner.scanAnnotation("com.topvision", Database.class);
        modules.clear();
        for (Class<?> clazz : clazzes) {
            Database setting = clazz.getAnnotation(Database.class);
            if (setting == null || modules.containsKey(setting.module())) {
                continue;
            }
            modules.put(setting.module(), setting);
            if (logger.isDebugEnabled()) {
                logger.debug("Load module:{}", setting.module());
            }
        }
    }

    public UserContext getUserContext(String username, boolean forceReset) {
        UserContext uc = usercontexts.get(username);
        if (uc == null || forceReset) {
            if (uc == null) {
                uc = new UserContext();
                uc.setLicenseIf(licenseIf);
            }
            User user = userDao.selectByUsername(username);
            if (user == null) {
                // return null;
                throw new AuthenticationException("UserNotExist");
            }
            uc.setUser(user);
            // Modify by Victor@20131015当用户没有设置语言时给用户设置系统默认配置语言
            if (user.getLanguage() == null) {
                SystemConstants sc = SystemConstants.getInstance();
                String lang = sc.getStringParam("language", "zh_CN");
                user.setLanguage(lang);
            }
            lastLoginRecords.put(username, user.getLastLoginTime());
            // Modify by Victor@20131015当用户没有设置语言时给用户设置系统默认配置语言
            if (user.getLanguage() == null) {
                SystemConstants sc = SystemConstants.getInstance();
                String lang = sc.getStringParam("language", "zh_CN");
                user.setLanguage(lang);
            }
            List<UserPreferences> allUserPerferences = userPreferencesService.getAllUserPerferences(user.getUserId());
            UserPreferencesMap<String, String> userPreferencesMap = uc.getUserPreferencesMap();
            for (int i = 0; i < allUserPerferences.size(); i++) {
                UserPreferences preference = allUserPerferences.get(i);
                String key = preference.getModule().concat(".").concat(preference.getName());
                userPreferencesMap.put(key, preference.getValue());
            }
            usercontexts.put(username, uc);
        }
        return uc;
    }

    /**
     *  用户登录时，相同用户不同session必须共同用一个usercontext
     * @param username
     * @return
     */
    public UserContext getUserContext(String username) {
        return getUserContext(username, false);
    }

    /**
     * 在每次登陆后都更新一次user，防止缓存中的用户信息不一致
     * @param username
     */
    public void resetusercontext(String username) {
        UserContext uc = usercontexts.get(username);
        User user = userDao.selectByUsername(username);
        uc.setUser(user);
        lastLoginRecords.put(username, user.getLastLoginTime());

    }

    /**
     * 用户离开时清除掉用户在线记录
     * @param request
     */
    public void userLogout(HttpServletRequest request) {
        UserContext uc = CurrentRequest.getCurrentUser();
        uc.unregistry(request.getRemoteHost());

    }

    /**
     * 批量启用用户
     * @param list
     */
    public void unlockUser(List<String> list) {
        for (String username : list) {
            unlockUser(username);
        }
    }

    /**
     * 检查用户是否已经被锁定了
     * @param username
     * @return
     */
    public boolean ifUserLocked(String username) {
        UserContext uc = getUserContext(username);
        int n = uc.increamentTryCounter(stopNumber);
        if (n >= stopNumber) {
            uc.setLocked(true);
            return true;
        } else {
            return false;
        }
    }

    /**
     * 每天凌晨将上一天被锁的用户解锁
     */
    public void cleanUser() {
        Set<String> set = usercontexts.keySet();
        for (String username : set) {
            unlockUser(username);
        }
    }

    /**
     * 判断用户是否已经登录
     * @param username
     * @return
     */
    public boolean ifUserLoggedon(String username, HttpServletRequest request) {
        UserContext uc = usercontexts.get(username);
        if (uc != null) {
            Set<String> hosts = uc.getHosts();
            // 由于在这之前已把自身加入到usercontexts中，所以至少会有一个
            if (hosts.size() == 0) {
                return false;
            } else if (hosts.contains(request.getRemoteHost())) {
                return false;
            } else {
                return true;
            }
        } else {
            return false;
        }
    }

    public Map<String, Database> getModules() {
        return modules;
    }

    public void setModules(Map<String, Database> modules) {
        this.modules = modules;
    }

    /**
     * 获取所有在线用户列表
     * @return
     */
    public List<User> getOnlineUser() {
        List<User> list = new ArrayList<User>();
        Set<String> set = usercontexts.keySet();
        for (String username : set) {
            UserContext uc = usercontexts.get(username);
            list.add(uc.getUser());
        }
        return list;
    }

    public Integer getStopNumber() {
        return stopNumber;
    }

    public void setStopNumber(Integer stopNumber) {
        this.stopNumber = stopNumber;
    }

    public void unlockUser(String userName) {
        UserContext uc = usercontexts.get(userName);
        if (uc != null) {
            uc.setTryPwdCounter(UserContext.DEFAULT_TRY_PWD_COUNT);
            uc.setLocked(false);
        }
    }

    /**
     * 用户退出登陆
     * @param uc
     * @param host
     */
    public void exitUser(UserContext uc, String host) {
        uc.unregistry(host);
    }

    /**
     * 当用户登陆时,注册所使用的session。
     * @param uc
     * @param request
     */
    public void registryClientHost(UserContext uc, String host) {
        uc.registry(host);
    }

    /**
     * 获取使用当前用户登陆的所有host
     * @param username
     * @return
     */
    public Set<String> getSharedUserHosts(String username) {
        UserContext uc = getUserContext(username);
        return uc.getHosts();
    }

    public Timestamp getLastLoginTime(String username) {
        return lastLoginRecords.get(username);
    }

    public void setLastLoginTime(String username, Timestamp time) {
        getUserContext(username).getUser().setLastLoginTime(lastLoginRecords.get(username));
        lastLoginRecords.put(username, time);
    }

    public void activeClient(String host) {
        UserContext uc = CurrentRequest.getCurrentUser();
        uc.activeClient(host);
    }

    /**
     * 用户被删除
     * @param username
     */
    public void userDeleted(String username) {
        usercontexts.remove(username);
    }

}
