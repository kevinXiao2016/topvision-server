/***********************************************************************
 * $Id: CurrentRequest.java,v1.0 2013-3-27 下午1:46:22 $
 * 
 * @author: Victor
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.platform.util;

import com.topvision.framework.domain.LocalThreadBean;
import com.topvision.platform.domain.User;
import com.topvision.platform.domain.UserContext;

/**
 * @author Victor
 * @created @2013-3-27-下午1:46:22
 * 
 *          在Service/Dao层获取当前登录用户的用户信息，当前request属性，当前session属性
 */
public class CurrentRequest {
    private static LocalThreadBean bean = new LocalThreadBean();
    @SuppressWarnings("unused")
    private static final String VIEW_ENTITY_PRE = "V_Entity_";
    public static final String TABLE_ENTITY_PRE = "t_entity_";
    public static final String VIEW_TOPO_PRE = "V_Topo_";
    public static final String ADMIN_USER = "admin";
    public static final String ENTITY = "entity";
    public static final String TOPOFOLDER = "topofolder";

    /**
     * 获取当前用户信息
     * 
     * @return userContext实例
     */
    public static UserContext getCurrentUser() {
        return (UserContext) bean.getSession().getAttribute(UserContext.KEY);
    }

    /**
     * 获得当前用户权限视图
     * 
     * @return
     */
    /*
    public static String getUserAuthorityViewName() {
     try {
         UserContext context = getCurrentUser();
         if (context.getUser().getUserName().equals(ADMIN_USER)) {
             return ENTITY;
         }
         return VIEW_ENTITY_PRE + context.getUser().getUserGroupId();
     } catch (NullPointerException e) {
         // Modify by Rod 当系统Thread调用权限视图，会出现session为空的情况
         // 由于session是针对客户端请求，无法处理系统Thread
         return ENTITY;
     }
    }*/

    /**
     * 获得当前用户权限表
     * 
     * @return
     */
    public static String getUserAuthorityViewName() {
        try {
            UserContext context = getCurrentUser();
            if (context.getUser().getUserName().equals(ADMIN_USER)) {
                return ENTITY;
            }
            return TABLE_ENTITY_PRE + context.getUser().getUserGroupId();
        } catch (NullPointerException e) {
            // Modify by Rod 当系统Thread调用权限视图，会出现session为空的情况
            // 由于session是针对客户端请求，无法处理系统Thread
            return ENTITY;
        }
    }

    /**
     * 获取地域对应的用户极限表
     * @param folderId
     * @return
     */
    public static String getAuthorityViewName(Integer folderId) {
        return TABLE_ENTITY_PRE + folderId;
    }

    /**
     * 获取指定用户的地域权限
     * 
     * @param user
     * @return
     */
    public static String getUserAuthorityFolderName(User user) {
        if (ADMIN_USER.equals(user.getUserName())) {
            return TOPOFOLDER;
        }
        return VIEW_TOPO_PRE + user.getUserGroupId();
    }

    /**
     * 
     * 
     * @return
     */
    public static String getUserAuthorityFolderName() {
        UserContext context = getCurrentUser();
        if (context.getUser().getUserName().equals(ADMIN_USER)) {
            return TOPOFOLDER;
        }
        return VIEW_TOPO_PRE + context.getUser().getUserGroupId();
    }

    /**
     * 获取当前request的属性信息
     * 
     * @param key
     *            属性键
     * @return 属性值
     */
    public static Object getCurrentRequestAttribute(String key) {
        return bean.getRequest().getAttribute(key);
    }

    /**
     * 获取当前session的属性信息
     * 
     * @param key
     *            属性键
     * @return 属性值
     */
    public static Object getCurrentSessionAttribute(String key) {
        return bean.getSession().getAttribute(key);
    }

}
